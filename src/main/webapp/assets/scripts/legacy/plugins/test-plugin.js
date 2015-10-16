/*
 * Test plugin to show pattern
 */

define(function(require){

    'use strict';

    var $               = require('jquery');
    var bsp_utils       = require('bsp-utils');
    var testUtility     = require('utilities/test-utility');

    var thePlugin = {

        // when we install the plugin, we create our instance
        _install: function() {
            var self = this;

            self.testInstance = Object.create(testUtility);

            self.testInstance.log('TEST PLUGIN: Install');
        },

        // run each time something gets into the DOM
        '_each': function(items) {
            var self = this;

            self.testInstance.log('TEST PLUGIN: EACH');
        }
    };

    return bsp_utils.plugin(false, 'bsp', 'test-plugin', thePlugin);
});
