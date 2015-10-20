/*
 * bsp-gallery
 */

define(function(require){

    'use strict';

    var $                       = require('jquery');
    var bsp_utils               = require('bsp-utils');
    var bsp_carousel_thumbnav   = require('bsp-carousel-thumbnav');

    var module = {
        init: function($el, options) {
            alert('yes');
            bsp_carousel_thumbnav.init($el, options);
            var galleryAPI = $el.find('.bsp-carousel-stage').data('bsp_carousel');
            $el.find('.bsp-carousel-nav-mobile .bsp-gallery-thumb').on('click', function(){
                $el.removeClass('bsp-thumbnail-open');
                galleryAPI.goTo($(this).index());
            });
            $el.find('.bsp-carousel-stage').on('afterChange', function(event, slick, currentSlide, nextSlide){
                $('.bsp-gallery-count').text(currentSlide+1);
            });

            // Disable going to the last slide from the first slide
            if($('.gallery-page-carousel').length){
                if(galleryAPI.currentSlide() == 0){
                    $(".slick-prev").hide();
                }
            }
            $el.find('.gallery-page-carousel').on('beforeChange', function(event, slick, currentSlide, nextSlide){
                if(currentSlide == 0 && nextSlide == galleryAPI.slideCount() - 1){
                    $el.find('.gallery-page-carousel').on('swipe', function(event, slick, direction){
                        if(direction == "right"){
                            location.reload();
                        }
                    });
                }
                if(currentSlide == galleryAPI.slideCount() - 2 && nextSlide == galleryAPI.slideCount() - 1){
                    window.location = $("#bsp-gallery-thumbnav").data("nextgalleryurl") + "?galleryView=next-gallery-view";
                }  
            });
            $el.find('.gallery-page-carousel').on('afterChange', function(event, slick, currentSlide, nextSlide){            
                if(currentSlide == 0){
                    $(".slick-prev").hide();
                }
                else{
                    $(".slick-prev").show();
                }
               if(currentSlide == galleryAPI.slideCount() - 2){
                    $('.bsp-gallery-count').text("end");
               }
            });
        }
    };


    var thePlugin = {
        '_each': function(item) {
            var options = this.option(item);
            var moduleInstance = Object.create(module);
            moduleInstance.init($(item), options);
        }
    };

    return bsp_utils.plugin(false, 'bsp', 'gallery', thePlugin);
});
