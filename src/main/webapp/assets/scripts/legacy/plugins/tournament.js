/**
 * tournament
 * 
 * To be used on Tournaments Page, for these purposes:
 *  - when year filter is changed, it reloads the page with a new 'yearFilter' query parameter 
 */

define(function(require){

    'use strict';

    var $               = require('jquery');
    var bsp_utils       = require('bsp-utils');
    var testUtility     = require('utilities/test-utility');


    var thePlugin = {
        '_defaultOptions': {
        },

        // when we install the plugin, we create our instance
        _install: function() {
            var self = this;
            self.testInstance = Object.create(testUtility);
            self.testInstance.log('tournament: Install');
        },
        '_init': function(roots, selector) { 
            var self = this;
            self.testInstance.log('tournament init....');

            $('#yearFilter').on('change', function() {
                var $slt = $(this),
                    loc = location.href;
                //If there's already a year filter, remove it.
                loc = loc.replace(/&?yearFilter=\d*&?/,'');
                // append new parameter
                location.href = loc + (loc.indexOf('?') > -1 ? '&' : '?') + 'yearFilter=' + $slt.val();
            });
            
        },
        // run each time something gets into the DOM
        '_each': function(item) {
            var self = this;
            var options = this.option(item);
            self.testInstance.log('tournament: EACH');
            self.testInstance.log(options);
            $(item).truncate(options);  
        }
    };

    return bsp_utils.plugin(false, 'bsp', 'tournament', thePlugin);
});
