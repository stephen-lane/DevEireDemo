import $ from 'jquery';
import bsp_utils from 'bsp-utils';
import bsp_tabber from 'bsp-tabber';

export default bsp_utils.plugin(false, 'dev', 'tabber', {
    '_each': function(item) {
        var options = this.option(item);
        var moduleInstance = Object.create(bsp_tabber);
        moduleInstance.init($(item), options);
    }
});
