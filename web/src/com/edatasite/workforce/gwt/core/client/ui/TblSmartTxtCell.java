package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Умная ячейка: если текст визуально переполняет ширину, создаём popup:
 *
 *   td.truncTxt-cell
 *     div.truncTxt-root   (host: сам div[__gwt_cell] или наш div#uid)
 *       div.truncTxt-pop  (внутрь переносим ВСЕ дочерние узлы root)
 *
 * Также помечаем:
 *   tr.truncTxt-row        — если в строке есть хотя бы одна переполненная ячейка
 *   table.truncTxt-table   — если в таблице есть хотя бы одна переполненная ячейка
 *
 * При наведении на root/host добавляется/снимается tr.truncTxt-hover (для затемнения соседей).
 */
public class TblSmartTxtCell extends AbstractCell<String> {

    private static boolean resizeHookInstalled = false;

    @Override
    public void render(Context context, String value, SafeHtmlBuilder sb) {
        if (value == null) return;

        // Единожды ставим глобальные хуки пересчёта
        if (!resizeHookInstalled) {
            installGlobalResizeHooks();
            resizeHookInstalled = true;
        }

        final String uid = Document.get().createUniqueId();
        final String escaped = SafeHtmlUtils.htmlEscape(value);

        // Рисуем минимальный контейнер (без инлайновых стилей)
        sb.appendHtmlConstant("<div id=\"" + uid + "\" class=\"truncTxt-root\">" + escaped + "</div>");

        // После того как DOM/лейаут устаканится — измеряем и применяем обёртку / снимаем её
        Scheduler.get().scheduleFinally(() -> waitForMeasurable(uid));
    }

    /** Ждём, пока элемент появится в DOM и будет измерим, затем обработаем. */
    private void waitForMeasurable(String uid) {
        Element root = Document.get().getElementById(uid);
        if (root == null) return;

        // Если есть внешний div[__gwt_cell] — переносим контент и помечаем его как настоящий root
        Element adopted = adoptToHostIfAny(root);
        ensureMeasuredThenProcess(adopted, 0);
    }

    /** Повторяем измерение несколько тактов, пока элемент не станет измерим. */
    private void ensureMeasuredThenProcess(Element root, int tries) {
        if (root == null) return;
        if (isMeasurable(root)) {
            processRoot(root);
        } else if (tries < 8) {
            defer(() -> ensureMeasuredThenProcess(root, tries + 1));
        }
    }

    /** Основная логика: обернуть/развернуть и поднять/снять классы вокруг. */
    private void processRoot(Element root) {
        if (root == null) return;

        boolean overflowing = isOverflowing(root);
        boolean hasPop = hasDirectPop(root); // только прямой ребёнок .truncTxt-pop

        if (overflowing && !hasPop) {
            // Стало переполняться — оборачиваем и поднимаем классы
            wrapChildrenToPop(root);
            liftClasses(root, true);
        } else if (!overflowing && hasPop) {
            // Перестало переполняться — разворачиваем и снимаем классы
            unwrapPop(root);
            liftClasses(root, false);
        } else {
            // На всякий случай синхронизируем классы (если что-то поменялось вокруг)
            liftClasses(root, overflowing);
        }
    }

    // ========= JSNI helpers =========

    /** Найти TD/TH, TR, TABLE вокруг узла */
    private native Element findCell(Element el) /*-{
        var p = el;
        while (p && p.nodeType === 1) {
            if (p.tagName === 'TD' || p.tagName === 'TH') return p;
            p = p.parentNode;
        }
        return null;
    }-*/;

    private native Element findRow(Element el) /*-{
        var p = el;
        while (p && p.nodeType === 1) {
            if (p.tagName === 'TR') return p;
            p = p.parentNode;
        }
        return null;
    }-*/;

    private native Element findTable(Element el) /*-{
        var p = el;
        while (p && p.nodeType === 1) {
            if (p.tagName === 'TABLE') return p;
            p = p.parentNode;
        }
        return null;
    }-*/;


    /** В строке есть хотя бы одна ячейка с прямым .truncTxt-pop? */
    private native boolean rowHasAnyOverflow(Element row) /*-{
      if (!row) return false;
      return !!row.querySelector(
        'td.truncTxt-cell > .truncTxt-root > .truncTxt-pop,' +
        'th.truncTxt-cell > .truncTxt-root > .truncTxt-pop'
      );
    }-*/;

    /** Синхронизировать классы на всех tr в таблице + саму таблицу. */
    private native void sweepTableState(Element table) /*-{
        if (!table) return;
        var anyPop = false;
        var rows = table.rows; // быстрее, чем querySelectorAll('tr')
        for (var i = 0; i < rows.length; i++) {
            var tr = rows[i];
            var has = !!tr.querySelector(
                'td.truncTxt-cell > .truncTxt-root > .truncTxt-pop,' +
                'th.truncTxt-cell > .truncTxt-root > .truncTxt-pop'
            );
            if (has) {
                anyPop = true;
                tr.classList.add('truncTxt-row');
            } else {
                tr.classList.remove('truncTxt-row');
            }
        }
        if (anyPop) table.classList.add('truncTxt-table');
        else table.classList.remove('truncTxt-table');
    }-*/;



    /** В таблице остались строки с переполнением? */
    private native boolean tableHasAnyTruncRow(Element tbl) /*-{
        return !!(tbl && tbl.querySelector('tr.truncTxt-row'));
    }-*/;

    private native void addClass(Element el, String cls) /*-{
        if (el) el.classList.add(cls);
    }-*/;

    private native void removeClass(Element el, String cls) /*-{
        if (el) el.classList.remove(cls);
    }-*/;

    /** Навешивает/снимает .truncTxt-hover на TR при наведении на root (делегировано) */
private native void ensureHoverHandlers(Element root) /*-{
  if (!root || root.__truncHoverInstalled) return;
  root.__truncHoverInstalled = true;

  function rowOf(n){ while(n){ if(n.tagName==='TR') return n; n = n.parentNode; } return null; }

  function onEnter(){
    var r = rowOf(root);
    if (!r) return;
    r.classList.add('truncTxt-hover');

    // соседи
    var prev = r.previousElementSibling;
    var next = r.nextElementSibling;
    if (prev) prev.classList.add('truncTxt-prev');
    if (next) next.classList.add('truncTxt-next');
  }

  function onLeave(){
    var r = rowOf(root);
    if (!r) return;
    r.classList.remove('truncTxt-hover');

    var prev = r.previousElementSibling;
    var next = r.nextElementSibling;
    if (prev) prev.classList.remove('truncTxt-prev');
    if (next) next.classList.remove('truncTxt-next');
  }

  root.addEventListener('mouseenter', onEnter, true);
  root.addEventListener('mouseleave', onLeave, true);
}-*/;

    /** Переносим наш #uid внутрь div[__gwt_cell] (если он есть), сам host помечаем как truncTxt-root. */
    private native Element adoptToHostIfAny(Element root) /*-{
        if (!root) return root;
        var p = root.parentNode;
        if (!(p && p.nodeType === 1 && p.hasAttribute && p.hasAttribute('__gwt_cell'))) {
            return root; // host-а нет — работаем с самим root
        }

        var host = p;

        // Перенесём всех детей root в host
        while (root.firstChild) host.appendChild(root.firstChild);

        // Идентификацию переносим на host
        if (root.id) host.id = root.id;
        host.classList.add('truncTxt-root');

        // Удаляем временный узел
        if (root.parentNode) root.parentNode.removeChild(root);

        return host;
    }-*/;

    /** Элемент имеет ненулевую ширину (можно мерить)? */
    private native boolean isMeasurable(Element el) /*-{
        if (!el) return false;
        var r = el.getBoundingClientRect();
        return !!(r && r.width > 0);
    }-*/;

    /** Переполнение по горизонтали? */
    private native boolean isOverflowing(Element el) /*-{
        if (!el) return false;
        return el.scrollWidth > el.clientWidth;
    }-*/;

    /** Прямой ребёнок .truncTxt-pop существует? */
    private native boolean hasDirectPop(Element root) /*-{
        if (!root) return false;
        return !!root.querySelector(':scope > .truncTxt-pop');
    }-*/;

    /** Поднять/снять классы у TD/TH, TR, TABLE вокруг root. */
    private void liftClasses(Element root, boolean overflowing) {
        Element cell = findCell(root);
        Element row  = findRow(root);
        Element tbl  = findTable(root);

        if (overflowing) {
            if (cell != null) addClass(cell, "truncTxt-cell");
            if (row  != null) addClass(row,  "truncTxt-row");
            if (tbl  != null) addClass(tbl,  "truncTxt-table");
            ensureHoverHandlers(root);
        } else {
            if (cell != null) removeClass(cell, "truncTxt-cell");
            if (row  != null && !rowHasAnyOverflow(row)) {
                removeClass(row, "truncTxt-row");
            }
            if (tbl != null && !tableHasAnyTruncRow(tbl)) {
                removeClass(tbl, "truncTxt-table");
            }
        }
    }

    /** Обернуть всех детей root в div.truncTxt-pop. */
    private native void wrapChildrenToPop(Element root) /*-{
        if (!root) return;
        if (root.querySelector(':scope > .truncTxt-pop')) return;

        var pop = $doc.createElement('div');
        pop.className = 'truncTxt-pop';

        while (root.firstChild) {
          pop.appendChild(root.firstChild);
        }
        root.appendChild(pop);
    }-*/;

    /** Развернуть: вынуть детей из .truncTxt-pop и удалить сам поп. */
    private native void unwrapPop(Element root) /*-{
        if (!root) return;
        var pop = root.querySelector(':scope > .truncTxt-pop');
        if (!pop) return;

        while (pop.firstChild) {
          root.appendChild(pop.firstChild);
        }
        root.removeChild(pop);
    }-*/;

    /** Отложить выполнение на rAF / ~16ms. */
    private native void defer(Runnable r) /*-{
        var fn = $entry(function(){ r.@java.lang.Runnable::run()(); });
        if ($wnd.requestAnimationFrame) $wnd.requestAnimationFrame(fn);
        else $wnd.setTimeout(fn, 16);
    }-*/;

    /** Глобальные хуки пересчёта: ResizeObserver для таблиц + fallback на window.resize. */
    private native void installGlobalResizeHooks() /*-{
        if ($wnd.__truncTxtHooksInstalled) return;
        $wnd.__truncTxtHooksInstalled = true;

        function recalcOne(root){
          if (!root || !root.parentNode) return;
          var overflowing = root.scrollWidth > root.clientWidth;
          var hasPop = !!root.querySelector(':scope > .truncTxt-pop');

          if (overflowing && !hasPop) {
            // wrap
            var pop = $doc.createElement('div'); pop.className = 'truncTxt-pop';
            while (root.firstChild) pop.appendChild(root.firstChild);
            root.appendChild(pop);
          } else if (!overflowing && hasPop) {
            // unwrap
            var pop2 = root.querySelector(':scope > .truncTxt-pop');
            if (pop2){
              while (pop2.firstChild) root.appendChild(pop2.firstChild);
              root.removeChild(pop2);
            }
          }

          // Поднять/снять классы вокруг
          (function lift(n){
            function up(x, tag){ while (x && x.nodeType===1){ if (x.tagName===tag) return x; x=x.parentNode; } return null; }
            var td = up(n,'TD') || up(n,'TH');
            var tr = up(n,'TR');
            var table = up(n,'TABLE');
            if (td){ if (overflowing) td.classList.add('truncTxt-cell'); else td.classList.remove('truncTxt-cell'); }
            if (tr){ if (overflowing) tr.classList.add('truncTxt-row'); else if (!tr.querySelector('td.truncTxt-cell .truncTxt-pop, th.truncTxt-cell .truncTxt-pop')) tr.classList.remove('truncTxt-row'); }
            if (table){
              if (overflowing) table.classList.add('truncTxt-table');
              else if (!table.querySelector('tr.truncTxt-row')) table.classList.remove('truncTxt-table');
            }
          })(root);
        }

        function recalcAllIn(node){
          var roots = (node || $doc).getElementsByClassName('truncTxt-root');

          // Соберём уникальные TABLE, к которым принадлежат эти root
          function up(n, tag){ while(n && n.nodeType===1){ if(n.tagName===tag) return n; n=n.parentNode; } return null; }
          var tables = [];
          for (var i=0; i<roots.length; i++) {
            recalcOne(roots[i]);
            var t = up(roots[i], 'TABLE');
            if (t && tables.indexOf(t) === -1) tables.push(t);
          }
          // Финальная синхронизация строк/таблиц
          for (var j=0; j<tables.length; j++) sweepTableState(tables[j]);
        }

        // ResizeObserver (если есть) — наблюдаем таблицы, где встречаются наши корни
        if ($wnd.ResizeObserver) {
          var ro = new $wnd.ResizeObserver(function(){
            ($wnd.requestAnimationFrame || function(f){ $wnd.setTimeout(f,16); })(function(){ recalcAllIn($doc); });
          });

          function scanTables(){
            var tables = $doc.getElementsByTagName('TABLE');
            for (var i=0; i<tables.length; i++) {
              var t = tables[i];
              if (!t.__truncTxtObserved && t.querySelector('.truncTxt-root')) {
                try { ro.observe(t); t.__truncTxtObserved = true; } catch(e){}
              }
            }
          }
          scanTables();
          // аккуратная периодическая проверка на появление новых таблиц
          $wnd.setInterval(scanTables, 2000);
        }

        // Fallback: окно ресайзится → пересчитать все корни (с дебаунсом)
        var scheduled = false;
        function onResize(){
          if (scheduled) return;
          scheduled = true;
          ($wnd.requestAnimationFrame || function(f){ $wnd.setTimeout(f,16); })(function(){
            scheduled = false;
            recalcAllIn($doc);
          });
        }
        $wnd.addEventListener('resize', onResize);
    }-*/;
}
