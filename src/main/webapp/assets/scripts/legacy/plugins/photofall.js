define(function (require) {

    var $               = require('jquery');
    var bsp_utils       = require('bsp-utils');
    var testUtility     = require('utilities/test-utility');

    // we are not using dari frame here, as we cant just add items into the DOM via dari frame and reset
    // the wall. If we did that, it might reorder some previous imagery. We actually need a data set
    // and we need to use the wall.appendBlock function to push the new imagery in

    // I'm not super happy with this code as I would like to make it a bit more modular, especially
    // the AJAX handler and error messaging, but I definitely ran out of time
    var thePlugin = {

        defaults: {
            wrapperSelector: '.photofallLayout',
            freewallSelector: '.free-wall',
            loadMoreSelector: '.loadMore',
            brickSelector: '.cell',
            screenSize : 'large',
            gutter: 10,
            containHeight: true,
            large : {
                zoneWidth: 1080,
                zoneHeight: 545,
                cellWidth: 175,
                cellHeight: 175,
                fixMeta: 1
            },
            medium : {
                zoneWidth: 880,
                zoneHeight: 545,
                cellWidth: 130,
                cellHeight: 130,
                fixMeta: 1
            },
            xs : {
                zoneWidth: $(window).width - 20,
                zoneHeight: 545,
                cellWidth: 90,
                cellHeight: 90,
                fixMeta: 0
            }
        },
        _install: function() {
            var self = this;
            self.testInstance = Object.create(testUtility);
            self.testInstance.log('photofall: Install');
        },

        '_init': function(options) {

            if($('.free-wall-zoned').length) {
                this.initializeWalls({
                    freewallSelector: '.free-wall-zoned'
                });
            }
            if($('.free-wall').length) {
                this.initializeWalls({
                    freewallSelector: '.free-wall',
                    containHeight: false
                });
            }

        },

        'initializeWalls': function(options) {
            var self = this,
                $freewall,
                $loadMore;

            // gather any options
            self.settings = $.extend(self.defaults, options);

            self.$freewall = $(self.settings.freewallSelector);
            self.$loadMore = $(self.settings.loadMoreSelector);

            // if we have load more wall buttons take care of them
            if(self.$loadMore.length) {
                // take over the load more button and make it ajax
                // this will pull parameters from the load more link data elements
                self._handleLoadMore();
            }

            // if we dont have any walls, get out now
            if(!self.$freewall.length) {
                return;
            }

            // initialize the wall(s)
            self.$freewall.each(function() {
                self._initializeWall(this);
            });

            return this;
        },

        // creating private helper as we need to access this a lot
        _figureOutScreenSize: function() {
            if(window.matchMedia("only screen and (max-width: 767px)").matches) {
                return 'xs';
            }
            else {
                if(window.matchMedia("only screen and (max-width: 1270px)").matches) {
                    return 'medium';
                }
                else {
                    return 'large';
                }
            }
        },

        _initializeWall: function(currentWall) {
            var self = this,
                wall;

            // set screen size
            self.screenSize = self._figureOutScreenSize();


            // set the height of each wrapper selector
            if(self.settings.containHeight) {
                $(currentWall).css('height',self.settings[self.screenSize].zoneHeight);
            }

            // create a new instance of wall for each selector. Ignoring the lowercase constructor
            /* jshint -W055 */
            wall = new freewall(currentWall);

            // initial wall settings. These are the set once per page for each free wall
            wall.reset({
                selector: self.settings.brickSelector,
                animate: false,
                gutterX: self.settings.gutter,
                gutterY: self.settings.gutter,

                onResize: function() {
                    // on resize, we need to reset the width and height again
                    self._resetWall(wall);
                    // and reinstantiate the wall to refit itself with possibly new parameters
                    self._triggerWall(wall);
                }
            });

            // this will set the reset the width and height based on screen size
            self._resetWall(wall);
            // go ahead and do the free wall and fit within the zone
            self._triggerWall(wall);

            // expose this wall into the local namespace
            self.wall = wall;

            // shows the imags, we were hiding them to prevent flash of unstyled content
            $(currentWall).addClass('initialized');

        },

        _resetWall: function(wallInstance) {
            var self = this,
                wall = wallInstance,
                fixMeta = 1;

            // every time this is run, we need to regather screen size
            self.screenSize = self._figureOutScreenSize();

            // reset the cell width and height for the appropriate screen size
            wall.reset({
                cellW: self.settings[self.screenSize].cellWidth,
                cellH: self.settings[self.screenSize].cellHeight
            });


            wall.fixPos({
                top: 0,
                left: self.settings[self.screenSize].fixMeta,
                block: $('.galleryMeta')
            });

            if(self.screenSize === 'xs') {
                wall.fixSize({
                    width:320,
                    height:90,
                    block: $('.galleryMeta')
                });
            }
        },

        _triggerWall: function(wallInstance) {
            var self = this,
                wall = wallInstance;

            if (self.settings.containHeight) {
                wall.fitZone(self.settings[self.screenSize].zoneWidth,self.settings[self.screenSize].zoneHeight);
            }
            else {
                wall.fitWidth();
            }
        },

        _handleLoadMore: function() {
            var self = this,
                $loadMoreLink;

            $loadMoreLink = self.$loadMore.find('a');

            $loadMoreLink.on('click',function(){

                self.$loadMore.addClass('loading');



                // get the ajax info
                var ajaxURL = $(this).attr('data-ajax-url'),
                    hrefURL = $(this).attr('href'),
                    $ajaxTarget = $('.' + $(this).attr('data-ajax-target-class')),
                    parameters = ajaxURL.split("?")[1],
                    offset = parseInt(self.getUrlParameter(parameters, "offset"),10),
                    limit = parseInt(self.getUrlParameter(parameters, "limit"),10),
                    id = self.getUrlParameter(parameters, "id");

                
                // if no ajax URL, let the click go through
                if(!ajaxURL) {
                    return true;
                }

                $.ajax({
                        url:ajaxURL,
                        dataType:'html'}
                ).done(function(data) {
                        // need to sanitize the data and just pull out the divs. The JSP seems to include a
                        // ton of stuff we dont want, like returns and blank text elements.
                        // There has to be a cleaner way of doing this

                        // pull the data into a jquery div
                        var $cleanData = $('<div>').html(data),
                            $temp = $('<div>'),
                            $newFreeWalls;

                        // TODO: better way of out of content messaging
                        if($(data).length === 0) {
                            self.$loadMore.hide();
                        }

                        // if we have an ajax target, easy append the data there
                        // if we do not, means we were not provided one, so in that case
                        // we are going to assume its a wall and use that functionality instead
                        if($ajaxTarget.length) {

                            $newFreeWalls = $cleanData.find(self.settings.freewallSelector);

                            $ajaxTarget.append($cleanData.html());

                            // regather the freewalls as we added some new ones
                            self.$freewall = $(self.settings.freewallSelector);

                            // initialize the wall(s)
                            self.$freewall.each(function() {
                                self._initializeWall(this);
                            });
                        }
                        else {

                            // go through that div, only select the bricks and move them into a temp object
                            $cleanData.find(self.settings.brickSelector).each(function() {
                                $temp.append(this);
                            });

                            // append them into the wall
                            self.wall.appendBlock($temp.html());



                        }

                        // always remove loading
                        self.$loadMore.removeClass('loading');

                        // lastly update the ajax URL and the href to the new correct counts for the next click
                        var newOffset = offset + limit;
                        $loadMoreLink.attr('href', "/ajax/photoFallServlet?offset=" + newOffset + "&limit=" + limit + "&id=" + id);
                        $loadMoreLink.attr('data-ajax-url', "/ajax/photoFallServlet?offset=" + newOffset + "&limit=" + limit + "&id=" + id);

                    })
                    .fail(function() {
                        alert('There has been an error while getting more photos, please try again.');
                    });

                return false;
            });
        },

        getUrlParameter: function(sPageUrl, sParam) {
            var sURLVariables = sPageUrl.split('&');

            var sParameterName, i;

            for (i = 0; i < sURLVariables.length; i++) {
                sParameterName = sURLVariables[i].split('=');

                if (sParameterName[0] === sParam) {
                    return sParameterName[1] === undefined ? true : sParameterName[1];
                }
            }
        }

    };
    return bsp_utils.plugin(false, 'bsp', 'photofall', thePlugin);
});
