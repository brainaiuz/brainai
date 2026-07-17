(function($) {
    $(document).ready(function() {

        function dotSync(eq) {
            $('.cp_slider__bg-img.active').addClass('freeze');
            $('.cp_slider__bg-img').removeClass('active').eq(eq).addClass('active');

        }

        $('.slick-dots li').click(function() {
            nextsld = $(this).index();
            $('.cp_slider__bg-img.active').addClass('freeze');
            $('.cp_slider__bg-img').removeClass('active').eq(nextsld - 1).addClass('active');
        });

        ladingSlider = $('.cp_slider__items');
        ladingSlider.on('init', function(slick) {
            dotSync(0);
        });
        ladingSlider.slick({
            slidesToShow: 1,
            slidesToScroll: 1,
            arrows: false,
            fade: true,
            dots: true,
            autoplay: true,
            adaptiveHeight: true
        });
        ladingSlider.on('afterChange', function(slick, currentSlide) {
            currentSlideID = currentSlide.currentSlide;
            $('.cp_slider__bg-img').removeClass('freeze');
        }).on('beforeChange', function(event, slick, currentSlide, nextSlider) {
            dotSync(nextSlider);

        });


    }); //doc ready

}(jQuery));