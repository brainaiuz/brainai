package com.edatasite.workforce.gwt.core.client.ui.dynamicTable.headers;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

/**
 * Simple table with one row and one column.
 * Some classes using it for aligning.
 * (If you are using div block you can't align text or html in block.
 * So you add this widget and then put all text in it.)
 */
public class InnerTable extends Widget {

    private Element table;
    private Element td;

    public InnerTable() {
        init();
    }


    /**
     * Initialization logic.
     * Simply draws table.
     */
    private void init() {
        table = DOM.createTable();

        DOM.setElementProperty(table, "height", "100%");
        DOM.setElementAttribute(table, "width", "100%");

        Element tBody = DOM.createTBody();
        DOM.appendChild(table, tBody);

        Element tr = DOM.createTR();
        DOM.appendChild(tBody, tr);

        td = DOM.createTD();
//        DOM.setElementProperty(td, "align", "center");
        DOM.appendChild(tr, td);

        setElement(table);
    }

    /**
     * Inserts text.
     *
     * @param text text you want to insert.
     */
    public void setInnerText(String text) {
        if (text != null) {
            DOM.setInnerText(td, text);
        }
    }


    /**
     * Method to set tables style class.
     *
     * @param id class name
     */
    public void setTableId(String id) {
        DOM.setElementProperty(table, "id", id);
    }


}
