document.switch_stage = function () {

    $('.workareaContent').on('click', '.state_origin, .state_flipped', function () {
        if ($(this).hasClass('toggle_of_parent')) {
            return;//this logic is difference
        }
        $(this).toggleClass('state_flipped').toggleClass('state_origin');
    });





//    CHANGE PARENT AND CHILDS FUCTION
    var parent_of_toggleRev = function (parent) {
//        change parent ".active | .nonactive"


        parent.toggleClass('active').toggleClass('nonactive');

//        change state_* class for .toggle_of_parent
        parent.find('.toggle_of_parent')
            .each(function () {
                $(this).toggleClass('state_flipped')
                    .toggleClass('state_origin');
            });


    }
//    End parent_of_toggleRev function


    $('.workareaContent').on('click', '.parent_of_toggle .toggle_of_parent', function () {
        var clickEl = event.target;

        if ($(clickEl).hasClass('formula_edit_control state_flipped')) {
            $('.formula_edit .btn.state_flipped').addTemporaryClass("state_warning", 300);
        }

        else {
            parent_of_toggleRev($(this).closest('.parent_of_toggle'));

//        Set focus to.formula_edit_control when.formula_edit.active
            $('.formula_edit.active .formula_edit_control').focus();
        }
    });







//    FLIPP VALUE BETWIN VALUE="" AND PLACEHOLDER=""
    $('.workareaContent').on('focus', '.formula_edit_control', function() {
        var curVal = $(this).attr('placeholder');
        $(this).val(curVal);
    });

    $('.workareaContent').on('blur', '.formula_edit_control', function() {
        var curVal = $(this).val();
        $(this).attr('placeholder', curVal);
    });



//SET CSS CLASS FOR A TEMPORARY TIME jQuery CLASS
    $.fn.extend({

        addTemporaryClass: function (className, duration) {
            var elements = this;
            setTimeout(function () {
                elements.removeClass(className);
            }, duration);

            return this.each(function () {
                $(this).addClass(className);
            });
        }
    });

}


jQuery(document).ready(function ($) {
    document.switch_stage();
});


/**
 * Created by Stanislav on 24.08.14.
 */






