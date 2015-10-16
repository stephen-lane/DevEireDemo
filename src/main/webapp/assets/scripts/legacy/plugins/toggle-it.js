/*
 * toggle plugin allows, toggling of elements
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
            self.testInstance.log('toggle: Install');
        },
        '_init': function(roots, selector) { 
            var self = this;
            self.testInstance.log('toggle init....');
        },
        // run each time something gets into the DOM
        '_each': function(item) {
            var self = this;
            var options = this.option(item);
            var initText = $(item).text();
            var toggleText = options.toggleText;
            $(item).on('click', function(){
                var text = $(this).text();
                // if (text === initText && toggleText){
                //     $(this).text(options.toggleText);
                // }else {
                //     $(this).text(initText);
                // }
                $(options.toggleTarget).toggleClass(options.toggleClass); 
            }); 
        }
    };

    return bsp_utils.plugin(false, 'bsp', 'toggle-it', thePlugin);
});
