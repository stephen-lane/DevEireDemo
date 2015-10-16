import $ from 'jquery';
import bsp_utils from 'bsp-utils';

class TournamentSchedule {
	constructor($el, options) {
		this.$content = $el.find('.jsg-tournament-sched-content');
		this.$select = $el.find('[name=jsg-tournament-sched-filters]');
		this.options = options;

		if (this.$content.length && this.$select.length) {
			this.addEvents();
		}
	}
	addEvents() {
		var self = this;
		self.$select.on('change', (e) => {
			if (e.currentTarget.value) {
				self.loadContent(e.currentTarget.value);
			}
		});
	}
	loadContent(value) {
		var self = this;
		$.get(value).then((content) => {
			self.$content.html(content);
		});
	}
}

export default bsp_utils.plugin(false, 'jsg', 'tournament-schedule', {
    '_each': function(item) {
        new TournamentSchedule($(item), this.option(item));
    }
});