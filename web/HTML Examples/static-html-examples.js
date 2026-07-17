document.addEventListener("DOMContentLoaded", function() {
// TABS
// Выбираем все элементы с классом tabs
    var tabs = document.querySelectorAll('.tabs');
// Перебираем все элементы с помощью метода forEach
    tabs.forEach(function(tab) {
        // Инициализируем вкладки для каждого элемента с помощью функции M.Tabs.init
        var instance = M.Tabs.init(tab, {
            // Добавление обработчика события onShow
            onShow: function(tab) {
                if (tab.getAttribute('data-action') == 'has-optional-content') {
                    // Выбираем все элементы с атрибутом data-action="optional-content"
                    var optContent = document.querySelectorAll('[data-action="optional-content"]');
                    // Перебираем все элементы с помощью метода forEach
                    optContent.forEach(function(optContent) {
                        // Добавляем каждому элементу класс active
                        optContent.classList.add('active');
                    });
                }
            }
        });
    });
// END TABS

// COLLAPSIBLE
    var collapsible = document.querySelectorAll('.collapsible');
    var instances = M.Collapsible.init(collapsible, {});
// \COLLAPSIBLE

// DROPDOWN LIST
//     var dropdownList = document.querySelectorAll('.dropdown-trigger');
//     var instances = M.Dropdown.init(dropdownList, {});

// Получаем все элементы .dropdown-button
    let buttons = document.querySelectorAll(".dropdown-button");
// Для каждого элемента .dropdown-button
    buttons.forEach(function(button) {
        // Добавляем обработчик события клика
        button.addEventListener("click", function() {
            // Переключаем класс .active у самого элемента
            button.classList.toggle("active");
            // Находим ближайший элемент .dropdown-content
            let content = button.nextElementSibling;
            // Переключаем класс .active у него
            content.classList.toggle("active");
        });
    });
// \ DROPDOWN LIST

// TOOLTIPS
    var tooltip = document.querySelectorAll('.tooltipstered');
    var instances = M.Tooltip.init(tooltip, {});
// \TOOLTIPS

// DROPDOWN SPLIT BUTTONS
// Получаем элементы по селекторам
    let dropdownSplit = document.querySelector(".dropdown-split");
    let toggle = document.querySelector(".dropdown-split__toggle");
// Добавляем обработчик события клика по кнопке - в случае, если такой элемент есть.
    toggle && toggle.addEventListener("click", function () {
        // Переключаем класс .dropdown-split--open у родительского элемента
        dropdownSplit.classList.toggle("dropdown-split--open");
    });
// \ DROPDOWN SPLIT BUTTONS


// FORM SELECT
    var select = document.querySelectorAll('select');
    var instances = M.FormSelect.init(select);
// END FORM SELECT

    M.AutoInit();
});

// Start JQuery
$(document).ready(function () {

    // left menu open/close
    $('.left-menu-trigger').click(function () {
        $('body').toggleClass('left-menu-open left-menu-closed')
        console.log('click')
    });
});



// Foldable tree list
$(function () {

    // --- Раскрытие / сворачивание по стрелке ---
    $(document).on('click', '.treeFold__toggle', function (e) {
        e.stopPropagation();

        const $toggle = $(this);
        const $li     = $toggle.closest('.treeFold__li');

        // если лист — не раскрываем
        if ($toggle.hasClass('treeFold__toggle--leaf') ||
            $li.hasClass('treeFold__li--disabled')) {
            return;
        }

        const isOpen = $li.hasClass('treeFold__li--open');

        if (isOpen) {
            // закрываем
            $li.removeClass('treeFold__li--open');
            $toggle.removeClass('expandedElement');
        } else {
            // открываем
            $li.addClass('treeFold__li--open');
            $toggle.addClass('expandedElement');
        }
    });


    // --- Выбор элемента ---
    $(document).on('click', '.treeFold__text', function (e) {
        e.stopPropagation();

        const $li = $(this).closest('.treeFold__li');

        if ($li.hasClass('treeFold__li--disabled')) return;

        // снимаем active со всех
        $('.treeFold__li.active').removeClass('active');

        // ставим active на кликнутый
        $li.addClass('active');

        // здесь можно вызвать внешний хендлер:
        // onTreeSelect($(this).text().trim());
    });

});


// ------------------------------------------
//   CUSTOM MODALS FOR STATIC HTML EXAMPLES
// ------------------------------------------
// Открыть конкретную модалку по id
function openModalById(id) {
    var $holder = $('#' + id + '.modal-holder');
    if (!$holder.length) return;

    // убрать инлайновый display:none, если вдруг есть
    $holder.css('display', '');

    $holder.addClass('active');

    var $overlay = $holder.children('.lean-overlay');
    if ($overlay.length) {
        $overlay.css('display', 'block');
    }
}

// Закрыть модалку
function closeModal($holder) {
    if (!$holder.length) return;

    $holder.removeClass('active');

    var $overlay = $holder.children('.lean-overlay');
    if ($overlay.length) {
        $overlay.css('display', 'none');
    }
}

$(function () {
    // открытие нужной модалки
    $(document).on('click', '.js-open-modal', function (e) {
        e.preventDefault();

        var targetId = $(this).data('modal-id');
        if (!targetId) return;

        openModalById(targetId);
    });

    // закрытие по крестику или по overlay
    $(document).on('click', '.modal-holder .js-close-modal, .modal-holder .close-x, .modal-holder .lean-overlay', function (e) {
        e.preventDefault();
        var $holder = $(this).closest('.modal-holder');
        closeModal($holder);
    });

    // закрытие по Esc
    $(document).on('keyup', function (e) {
        if (e.key === 'Escape') {
            $('.modal-holder.active').each(function () {
                closeModal($(this));
            });
        }
    });
});



