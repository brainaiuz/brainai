jQuery(document).ready( function($) {

//    SET WIDTH TO .BASE_PAGE_CONTROLS
    var scrollArea = $('.scrollArea'); /*div.workareaContent.scrollArea*/
    var base_page_controls = $('.base_page_controls');
    var base_page_controls_height = base_page_controls.height();

    if( base_page_controls.hasClass('has_affix') ) {
        scrollArea.css('bottom', base_page_controls_height);
    }

} );
/**
 * Created by Stanislav on 20.09.14.
 */
