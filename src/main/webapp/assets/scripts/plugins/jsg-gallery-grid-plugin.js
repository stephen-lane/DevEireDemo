import $ from 'jquery';
import bsp_utils from 'bsp-utils';

class GalleryGrid {
	constructor($el, options) {
		if (options.lowRes) {
			this.$el = $el;
			this.$grid = $el.find('.dev-gallery-grid');
			this.$gallery = $el.find('.dev-gallery-grid-gallery');
			this.addIndexes();
			this.addEvents();
		}
	}
	addIndexes() {
		var i = 0;
		this.$grid.find('.dev-gallery-grid-thumb').each((key, thumb) => {
			var $thumb = $(thumb);
			$thumb.data('grid-index', i);
			i++;
		});
	}
	addEvents() {
		var self = this;
		this.$el.find('.dev-gallery-grid-thumb').on('click', (e) => {
			self.thumbClickEvent( $(e.currentTarget) );
		});
		this.$gallery.find('a').on('click', (e) => {
			e.preventDefault();
		});
	}
	thumbClickEvent($thumb) {
		var carousel = this.$gallery.data('bsp_carousel');
		var index = $thumb.data('grid-index');
		carousel.goTo(index);
		this.hideGrid();
		this.showGallery();
	}
	hideGrid() {
		this.$el.removeClass('showing-grid');
		this.$grid.addClass('dev-gallery-grid-hidden');
	}
	showGallery() {
		this.$gallery.removeClass('dev-gallery-grid-hidden').addClass('dev-gallery-grid-show');
	}
}

export default bsp_utils.plugin(false, 'dev', 'gallery-grid', {
    '_each': function(item) {
        new GalleryGrid($(item), this.option(item));
    }
});