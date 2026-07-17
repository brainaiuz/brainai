package com.edatasite.workforce.gwt.core.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

/** Post-processor for DynamicTable: adds truncation classes & pop wrapper on overflow. */
public final class TblDynamicSmartTxtCell {
    private TblDynamicSmartTxtCell() {}

    // по умолчанию обрабатываем и dynamictable, и truncTxtCatch
    public static void process(Widget root) {
        process(root, "table.dynamictable, table.truncTxtCatch", false);
    }

    public static void process(Widget root, String tableSelector, boolean debug) {
        if (root == null) return;
        Element el = root.getElement();
        if (el == null) return;
        if (tableSelector == null || tableSelector.trim().isEmpty()) {
            tableSelector = "table.dynamictable, table.truncTxtCatch";
        }
        processInternal(el, tableSelector, debug);
    }

private static native void processInternal(Element tableRoot, String selector, boolean debug) /*-{
  function log(){ if (debug && $wnd.console) $wnd.console.log.apply($wnd.console, arguments); }
  var tables = tableRoot.querySelectorAll(selector);
  if (!tables || !tables.length) return;

  function find(el, tag){ tag = tag.toUpperCase(); while (el && el.tagName !== tag) el = el.parentElement; return el; }
  function visible(el){ var r = el && el.getClientRects && el.getClientRects(); return !!(el && (el.offsetWidth || el.offsetHeight || (r && r.length))); }

  for (var t=0; t<tables.length; t++){
    var table = tables[t];
    if (!/\btruncTxt-table\b/.test(table.className)) table.className += ' truncTxt-table';

    // КАНДИДАТЫ (без ручной разметки)
    var candidates = table.querySelectorAll(
      'td > div, td .gwt-Label, td .gwt-InlineLabel, td .gwt-HTML, td a, td span'
    );
    for (var i=0; i<candidates.length; i++){
      var node = candidates[i];
      if (!visible(node)) continue;

      // ensure .truncTxt-root перед измерением
      var added = false;
      if (!/\btruncTxt-root\b/.test(node.className)) { node.className += (node.className?' ':'') + 'truncTxt-root'; added = true; }

      // измерение с nowrap
      var ws = node.style.whiteSpace, ov = node.style.overflow, d = node.style.display, mw = node.style.maxWidth;
      node.style.whiteSpace = 'nowrap'; node.style.overflow='hidden'; node.style.display='block'; node.style.maxWidth='100%';
      var overflowed = node.scrollWidth > node.clientWidth;
      node.style.whiteSpace = ws; node.style.overflow = ov; node.style.display = d; node.style.maxWidth = mw;

      var td = find(node,'TD'), tr = find(node,'TR');

      if (overflowed){
        // wrap, если нужно
        if (!node.querySelector('.truncTxt-pop')) {
          var pop = (table.ownerDocument || $doc).createElement('div');
          pop.className = 'truncTxt-pop';
          while (node.firstChild) pop.appendChild(node.firstChild);
          node.appendChild(pop);
        }
        if (td && !/\btruncTxt-cell\b/.test(td.className)) td.className += ' truncTxt-cell';
        if (tr && !/\btruncTxt-row\b/.test(tr.className)) tr.className += ' truncTxt-row';
      } else {
        // unwrap, если был wrap
        var ex = node.querySelector('.truncTxt-pop');
        if (ex){ while (ex.firstChild) node.insertBefore(ex.firstChild, ex); node.removeChild(ex); }
        // если класс мы добавили только что и он не нужен — уберём, чтобы не «засорять» DOM
        if (added) node.className = node.className.replace(/\btruncTxt-root\b/g,'').trim();
        // снять .truncTxt-cell, если в TD больше нет переполненных
        if (td && /\btruncTxt-cell\b/.test(td.className)){
          var others = td.querySelectorAll('.truncTxt-root .truncTxt-pop');
          if (!others || !others.length) td.className = td.className.replace(/\btruncTxt-cell\b/g,'').trim();
        }
      }
    }

    // hover делегаты — один раз
    if (!table.__truncTxtBound){
      table.__truncTxtBound = true;
      function find2(el,tag){ tag = tag.toUpperCase(); while (el && el.tagName !== tag) el = el.parentElement; return el; }
      table.addEventListener('mouseover', function(e){
        var cell = find2(e.target,'TD'); if (!cell || !/\btruncTxt-cell\b/.test(cell.className)) return;
        var row = find2(cell,'TR'); if (!row) return;
        if (!/\btruncTxt-hover\b/.test(row.className)) row.className += ' truncTxt-hover';
        var p=row.previousElementSibling,n=row.nextElementSibling;
        if (p && !/\btruncTxt-prev\b/.test(p.className)) p.className += ' truncTxt-prev';
        if (n && !/\btruncTxt-next\b/.test(n.className)) n.className += ' truncTxt-next';
      });
      table.addEventListener('mouseout', function(e){
        var cell = find2(e.target,'TD'); if (!cell || !/\btruncTxt-cell\b/.test(cell.className)) return;
        var row = find2(cell,'TR'); if (!row) return;
        row.className = row.className.replace(/\btruncTxt-hover\b/g,'').trim();
        var p=row.previousElementSibling,n=row.nextElementSibling;
        if (p) p.className = p.className.replace(/\btruncTxt-prev\b/g,'').trim();
        if (n) n.className = n.className.replace(/\btruncTxt-next\b/g,'').trim();
      });
    }
  }
}-*/;
}
