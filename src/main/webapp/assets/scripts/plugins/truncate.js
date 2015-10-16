/*
 * Test plugin to show pattern
 */

import $ from 'jquery';
import bsp_utils from 'bsp-utils';
import jquery_truncate from 'jquery-truncate';

alert('hey');

var thePlugin = {
    '_defaultOptions': {
    },
    // when we install the plugin, we create our instance
    _install: function() {
        var self = this;
    },
    '_init': function(roots, selector) {
        var self = this;
        alert('he');
    },
    // run each time something gets into the DOM
    '_each': function(item) {
        var self = this;
        var options = this.option(item);
        $(item).truncate(options);
    }
};

export default bsp_utils.plugin(false, 'bsp', 'truncate', thePlugin);

