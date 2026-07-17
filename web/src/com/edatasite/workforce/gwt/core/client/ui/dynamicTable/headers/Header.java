package com.edatasite.workforce.gwt.core.client.ui.dynamicTable.headers;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.SortCommand;
import com.google.gwt.dom.client.Style;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.10.2008
 * Time: 13:27:21
 * To change this template use File | Settings | File Templates.
 */
public class Header extends AbstractHeader {

    private Div cell;
    private String headerStyle = "";
    private Icon sortIcon;
    private SortCommand sortCommand;

    public Header(String headerStyle) {

        this.headerStyle = headerStyle;
        init();
        initWidget(cell);
    }

    private void init() {

        cell = new Div();
        cell.addStyleName(headerStyle);

    }

    public void setHeaderText(String text) {

        cell.getElement().setInnerHTML(text);

        if (sortCommand != null) {
            cell.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            sortIcon = new Icon();
            sortIcon.setStyleName("ficon--keyboard-arrow-up");
            sortIcon.setDisplay(Display.NONE);
            cell.add(sortIcon);
        }
    }

    @Override
    public void setSortCommand(SortCommand sortCommand) {
        this.sortCommand = sortCommand;
        cell.addClickHandler(event -> {
            sortIcon.setDisplay(Display.INLINE);
            if (sortIcon.getStyleName() == null || "ficon--keyboard-arrow-down".equals(sortIcon.getStyleName())) {
                sortIcon.setStyleName("ficon--keyboard-arrow-up");
                sortCommand.execute(Constants.ASC_STR);
            } else {
                sortIcon.setStyleName("ficon--keyboard-arrow-down");
                sortCommand.execute(Constants.DESC_STR);
            }
        });
    }
}
