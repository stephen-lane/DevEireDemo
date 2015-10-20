/*
 * tooltip plugin allows, toggling of elements
 */

define(function(require){

    'use strict';

    var $               = require('jquery');
    var bsp_utils       = require('bsp-utils');
    var testUtility     = require('utilities/test-utility');
    var bowerTooltip     = require('bower/bootstrap/tooltip');

    var thePlugin = {
        '_defaultOptions': {
        },
        // when we install the plugin, we create our instance
        _install: function() {
            var self = this;
            self.testInstance = Object.create(testUtility);
            self.testInstance.log('tooltip: Install');
        },
        '_init': function(roots, selector) { 
            var self = this;
            self.testInstance.log('tooltip init....');

        },
        // run each time something gets into the DOM
        '_each': function(item) {
            var self = this;
            var options = this.option(item);
            $(item).tooltip(options);
        }
    };

    return bsp_utils.plugin(false, 'bsp', 'tooltip', thePlugin);
});
