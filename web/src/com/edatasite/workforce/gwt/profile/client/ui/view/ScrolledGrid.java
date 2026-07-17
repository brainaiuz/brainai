package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.StyleElement;
import com.google.gwt.dom.client.Text;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.HeaderPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.view.client.ProvidesKey;

/**
 * Created by Hurshid on 6/13/2016.
 */
public class ScrolledGrid<T> extends DataGrid<T> {

    private final Text cssText;
    private boolean addedClass = false;
    private ScrollPanel scrollPanel;

    public ScrolledGrid(int pageSize, ProvidesKey keyProvider) {
        super(pageSize, keyProvider);
        cssText = Document.get().createTextNode("");

        StyleElement styleElement = Document.get().createStyleElement();
        styleElement.setType("text/css");
        if (!Utils.isIE()) {
            styleElement.appendChild(cssText);
        }

        HeaderPanel headerPanel = (HeaderPanel) getWidget();
        headerPanel.getElement().insertFirst(styleElement);

        scrollPanel = (ScrollPanel) headerPanel.getContentWidget();

    }

   /* private void cellchange(int scrollLeft) {
        if (!addedClass) {
            NodeList<TableRowElement> rows;
            TableRowElement row;
            TableCellElement cell;

            rows = getTableHeadElement().getRows();
            for (int i = 0; i < rows.getLength(); ++i) {
                row = rows.getItem(i);
                cell = row.getCells().getItem(0);
                cell.setInnerHTML("<div>" + cell.getInnerHTML() + "</div>");
                cell.addClassName("ScrolledGrid-frozen");
            }

            rows = getTableBodyElement().getRows();
            for (int i = 0; i < rows.getLength(); ++i) {
                row = rows.getItem(i);
                cell = row.getCells().getItem(0);

                cell.addClassName("ScrolledGrid-frozen");
            }
            addedClass = true;
        }

    }

    @Override
    protected void onDetach() {
        super.onDetach();
        cssText.setData("ScrolledGrid-frozen { } th.ScrolledGrid-frozen { background-color: white; }");
        cellchange(scrollPanel.getHorizontalScrollPosition());
    }*/
}
