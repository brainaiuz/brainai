// static-orgchart-assets.js
// Скрипты для статических примеров оргструктуры
// БЕЗ jQuery

(function () {
  // ---------- ХЕЛПЕРЫ ----------

  function qs(root, selector) {
    return (root || document).querySelector(selector);
  }

  function qsa(root, selector) {
    return Array.prototype.slice.call(
        (root || document).querySelectorAll(selector)
    );
  }

  function on(root, event, selector, handler) {
    root.addEventListener(event, function (e) {
      var target = e.target.closest(selector);
      if (!target || !root.contains(target)) return;
      handler(e, target);
    });
  }

  function trim(str) {
    return (str || '').replace(/\s+/g, ' ').trim();
  }

  // =========================================================
  // 1. ВЫБОР ВАРИАНТА ОРГСТРУКТУРЫ (setupOpt)
  // =========================================================
  function initSetupOptions() {
    var setupOpts = qsa(document, '.setupOpt');
    var mainCreateBtn = qs(document, '#setupMainCreateBtn');

    if (!setupOpts.length || !mainCreateBtn) return;

    setupOpts.forEach(function (el) {
      el.addEventListener('click', function () {
        setupOpts.forEach(function (o) {
          o.classList.remove('active');
        });
        el.classList.add('active');
        mainCreateBtn.classList.remove('disabled');
      });
    });
  }

  // =========================================================
  // 2. ГЛОБАЛЬНАЯ РАБОТА С МОДАЛКАМИ (data-modal-id)
  // =========================================================

  function openModal(holder) {
    if (!holder) return;

    var overlay = qs(holder, '.lean-overlay');

    // убрать возможный inline display:none
    holder.style.display = '';
    if (overlay) overlay.style.display = '';

    holder.classList.add('active');

    var firstInput = qs(holder, '.form-group__content input[type="text"]');
    if (firstInput) {
      firstInput.focus();
    }
  }

  function closeModal(holder) {
    if (!holder) return;

    var overlay = qs(holder, '.lean-overlay');

    holder.classList.remove('active');
    holder.style.display = '';
    if (overlay) overlay.style.display = '';
  }

  function initModals() {
    // Открытие по data-modal-id
    on(document, 'click', '[data-modal-id]', function (e, btn) {
      if (btn.classList.contains('disabled') || btn.disabled) return;

      var modalId = btn.getAttribute('data-modal-id');
      if (!modalId) return;

      var holder = document.getElementById(modalId);
      if (!holder) return;

      e.preventDefault();
      openModal(holder);
    });

    // Закрытие по .close-x и .btn--default
    on(document, 'click', '.modal-holder .close-x, .modal-holder .btn--default', function (e, el) {
      var holder = el.closest('.modal-holder');
      if (holder) closeModal(holder);
    });

    // Закрытие по клику на подложку
    on(document, 'click', '.modal-holder .lean-overlay', function (e, overlay) {
      var holder = overlay.closest('.modal-holder');
      if (holder) closeModal(holder);
    });
  }

  // =========================================================
  // 3. ВАЛИДАЦИЯ МОДАЛОК "Создать оргструктуру"
  // =========================================================

  function initModalValidation() {
    var holders = qsa(document, '.modal-holder');

    holders.forEach(function (holder) {
      var createBtn = qs(holder, '.modal-footer .btn--primary');
      if (!createBtn) return;

      var textInputs = qsa(holder, '.form-group__content input[type="text"]');
      var nameInput = textInputs[0] || null;
      var descInput = textInputs[1] || null;

      function updateCreateState() {
        var hasName = !nameInput || trim(nameInput.value).length > 0;
        var hasDesc = !descInput || trim(descInput.value).length > 0;
        var hasColor = qsa(holder, '.colorPickItem.active').length > 0;

        if (hasName && hasDesc && hasColor) {
          createBtn.classList.remove('disabled');
        } else {
          createBtn.classList.add('disabled');
        }
      }

      // сохраняем функцию в dataset, чтобы colorPick мог её вызвать
      holder._updateCreateState = updateCreateState;

      if (nameInput) {
        nameInput.addEventListener('input', updateCreateState);
      }
      if (descInput) {
        descInput.addEventListener('input', updateCreateState);
      }

      updateCreateState();
    });
  }

  // =========================================================
  // 4. COLOR PICKER (colorPickBox / colorPickList / colorPickItem)
  // =========================================================

  function initColorPicker() {
    // инициализация превью
    qsa(document, '.colorPickBox').forEach(function (box) {
      var active = qs(box, '.colorPickItem.active');
      var prev = qs(box, '.colorPickBox__preview');

      if (active && prev) {
        var color =
            active.getAttribute('data-color') ||
            active.style.getPropertyValue('--theme-color') ||
            '';
        if (color) {
          prev.style.backgroundColor = color;
        }
      }
    });

    // клик по кружку
    on(document, 'click', '.colorPickItem', function (e, btn) {
      e.preventDefault();

      var color =
          btn.getAttribute('data-color') ||
          btn.style.getPropertyValue('--theme-color') ||
          '';

      var list = btn.closest('.colorPickList');
      var box = btn.closest('.colorPickBox');
      var prev = box ? qs(box, '.colorPickBox__preview') : null;

      if (list) {
        qsa(list, '.colorPickItem').forEach(function (el) {
          el.classList.remove('active');
        });
      }
      btn.classList.add('active');

      if (color) {
        btn.style.backgroundColor = color;
      }
      if (prev && color) {
        prev.style.backgroundColor = color;
      }

      // обновляем состояние модалки, если есть
      var holder = btn.closest('.modal-holder');
      if (holder && typeof holder._updateCreateState === 'function') {
        holder._updateCreateState();
      }
    });
  }

  // =========================================================
  // 5. ТАБЫ "Вертикально / Горизонтально" (tabs--orgDir)
  // =========================================================

  function initOrgDirTabs() {
    on(document, 'click', '.tabs.tabs--orgDir .tab > a', function (e, a) {
      e.preventDefault();
      var tabs = a.closest('.tabs');
      if (!tabs) return;

      qsa(tabs, '.tab > a').forEach(function (el) {
        el.classList.remove('active');
      });
      a.classList.add('active');

      // Здесь можно дергать смену направления оргчарта
      // по data-атрибуту, если понадобится.
    });
  }

  // =========================================================
  // 6. CUSTOM SELECT (data-custom-select / data-custom-select-trigger)
  // =========================================================

  function updateCustomControlSearchState(selectRoot) {
    if (!selectRoot) return;
    var control = qs(selectRoot, '.customControl');
    if (!control) return;

    var isOpen = selectRoot.classList.contains('is-open');
    var hasValue = selectRoot.classList.contains('customSelect--choosed');

    if (isOpen && !hasValue) {
      control.classList.add('customControl--search');
    } else {
      control.classList.remove('customControl--search');
    }
  }

  function closeAllCustomSelects(except) {
    qsa(document, '[data-custom-select].is-open').forEach(function (sel) {
      if (sel !== except) {
        sel.classList.remove('is-open');
        updateCustomControlSearchState(sel);
      }
    });
  }

  function initCustomSelect() {
    // Открытие / закрытие по любому триггеру
    on(document, 'click', '[data-custom-select-trigger]', function (e, trigger) {
      var selectRoot = trigger.closest('[data-custom-select]');
      if (!selectRoot) return;

      if (selectRoot.classList.contains('disabled')) return;

      var isOpen = selectRoot.classList.contains('is-open');

      if (isOpen) {
        selectRoot.classList.remove('is-open');
      } else {
        closeAllCustomSelects(selectRoot);
        selectRoot.classList.add('is-open');
      }

      updateCustomControlSearchState(selectRoot);

      e.preventDefault();
      e.stopPropagation();
    });

    // клик внутри дропа — не закрывать
    on(document, 'click', '.customSelect-dropBox', function (e, box) {
      e.stopPropagation();
    });

    // клик вне — закрываем всё
    document.addEventListener('click', function (e) {
      if (e.target.closest('[data-custom-select]')) return;
      closeAllCustomSelects(null);
    });

    // Очистка по крестику (controlClear)
    document.addEventListener('click', function (event) {
      var clearBtn = event.target.closest('.controlClear');
      if (!clearBtn) return;

      var selectRoot = clearBtn.closest('[data-custom-select]');
      if (!selectRoot) return;

      var control = qs(selectRoot, '.customControl');
      if (!control) return;

      var input =
          qs(control, 'input.form-control') ||
          qs(control, '.select-dropdown');

      if (input) {
        input.value = '';
      }

      // снимаем "выбрано"
      selectRoot.classList.remove('customSelect--choosed');

      // снимаем active с элементов дерева
      qsa(selectRoot, '.treeFold__li.active').forEach(function (li) {
        li.classList.remove('active');
      });

      // после очистки, если список открыт, должен быть режим "search"
      updateCustomControlSearchState(selectRoot);

      event.preventDefault();
      event.stopPropagation();
    });

    // Выбор пункта в treeFold внутри customSelect
    document.addEventListener('click', function (event) {
      var textNode = event.target.closest('[data-custom-select] .treeFold__text');
      if (!textNode) return;

      var selectRoot = textNode.closest('[data-custom-select]');
      if (!selectRoot) return;

      var control =
          qs(selectRoot, '.customControl') ||
          qs(selectRoot, '[data-custom-select-trigger]');

      if (!control) return;

      var input =
          qs(control, 'input.form-control') ||
          qs(control, '.select-dropdown');

      if (input) {
        input.value = trim(textNode.textContent);
      }

      // помечаем, что значение выбрано
      selectRoot.classList.add('customSelect--choosed');
      // закрываем выпадающий список
      selectRoot.classList.remove('is-open');

      // обновляем разновидность контрола (убрать customControl--search)
      updateCustomControlSearchState(selectRoot);
    });
  }

  // =========================================================
  // ИНИЦИАЛИЗАЦИЯ
  // =========================================================

  document.addEventListener('DOMContentLoaded', function () {
    initSetupOptions();
    initModals();
    initModalValidation();
    initColorPicker();
    initOrgDirTabs();
    initCustomSelect();
  });
})();