import $ from 'jquery';
import bsp_utils from 'bsp-utils';

class TestJS {
    constructor($el, options) {
        this.$el = $el;
        this.$dynamicLead = $el.find('.gallery-module');
        this.addIndexes();
    }
    addIndexes() {
        // alert('init')
    }
}

export default bsp_utils.plugin(false, 'dev', 'gallery-test', {
    '_each': function(item) {
        new TestJS($(item), this.option(item));
    }
});
