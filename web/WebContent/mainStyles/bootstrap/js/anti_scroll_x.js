/**
 * Created by Stanislav on 29.08.14.
 */
jQuery(document).ready(function($) {
    anti_scroll_x();
})
/*!!! You should remove above declaration if you call frame_affix() thrue GWT !!!*/

$.fn.extend({
    scrollRight: function(property) {
        return this[0].scrollWidth - (this[0].scrollLeft + this[0].clientWidth) + 1;
    }
});

function anti_scroll_x() {
    var scrollArea = $('.scrollArea'); /*div.workareaContent.scrollArea*/
    var el_affix = $('.point_affix_top'); /*thead*/
//        console.log( el_affix.offsetParent() );


    var scrollLeft;

    var direction = 'ltr';
    try {
        direction = $('html').attr('dir');
    } catch (e) {
    }

    /*Stick thead and synchronize scrolls*/
    scrollArea.on('scroll', function() {
        scrollLeft = scrollArea.scrollLeft();

        /*if( $(this).scrollTop() > el_offsetTop ) {
            el_affix.addClass('has_affix').css( {'top' : scrollArea_offsetTop} );
            el_affix.css( {'width' : scrollAreaWidth} ); *//*thead width = .scrollArea width (can be less than table width when more cols)*//*
        }
        else {
            el_affix.removeClass('has_affix');
        }*/
        //this is the arabic
        el_affix.scrollLeft( scrollLeft );

        if (!(direction === undefined) && 'rtl' === direction) {
            var scrollRight = $(scroll_container).scrollRight();
            $('.workareaContent_head').css({
                'color': 'red',
                '-webkit-transform': 'translateX(-' + scrollRight + 'px)',
                '-moz-transform': 'translateX(-' + scrollRight + 'px)',
                'transform': 'translateX(-' + scrollRight + 'px)'
            });
        } else {
            $('.workareaContent_head').css({
                '-webkit-transform': 'translateX(' + scrollLeft + 'px)',
                '-moz-transform': 'translateX(' + scrollLeft + 'px)',
                'transform': 'translateX(' + scrollLeft + 'px)'
            });
        }

    });

} /*End frame_affix()*/