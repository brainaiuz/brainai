var swiper = new Swiper('.simple-slider', {
    pagination: '.swiper-pagination',
    paginationClickable: true,
    loop: true,
    paginationBulletRender: function (swiper, index, className) {
        return '<span class="' + className + '">0' +  (index + 1) + '</span>';
    }
});