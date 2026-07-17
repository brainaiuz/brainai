/**
 * Created by Stanislav on 29.08.14.
 */
jQuery(document).ready(function($) {
    /*Set value of data-offset-top*/
    var offsetTop = $('.point_affix_top').offset().top;
//    $('.point_affix_top .stickerCell>*').attr('data-offset-top', offsetTop);
    $('.point_affix_top .stickerCell>*').affix({
        offset: {
            top: offsetTop
        }
    });


//    SET WIDTH TO .STICKERCELL
    $('.stickerCell').each(function() {
        var w = $(this).width();
        $(this).css({'width':w + 'px'}).children('div').css({'width':w + 'px'});
    });



//    SET WIDTH TO .BASE_PAGE_CONTROLS
    var base_page_controls_width = $('.base_page_controls').parent().width();
    $('.base_page_controls').css('width', base_page_controls_width);

/*    $('.base_page_controls').affix({
        offset: {
            bottom: $('#footer').height()
        }
    });*/


})