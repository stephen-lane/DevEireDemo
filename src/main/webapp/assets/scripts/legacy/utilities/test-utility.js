/*
 * Utility for lazy loading images
 *
 */

define(function(require){

    'use strict';

    var $           = require('jquery');

    var logger = {

        settings: {
            'log' : true          
        },

        log: function(string) {

            var self = this;

            if(self.settings.log) {
                console.log(string);
            }
        }

    };

    return logger;

});
