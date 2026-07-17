package com.edatasite.workforce.gwt.core.client.ui.editableTable.cell;

import com.edatasite.workforce.gwt.core.client.interfaces.LinkableCellInterface;
import com.edatasite.workforce.gwt.core.client.interfaces.LinkedLinkableCellInterface;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.cell.AbstractCell;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 15.05.15
 * Time: 10:42
 * To change this template use File | Settings | File Templates.
 */
public class LinkedLinkableCell extends AbstractCell {

    private Widget customWidget = null;
    private String customStyle = null;
    private Command clickHandler;

    public LinkedLinkableCell(String customStyle) {
        this.customStyle = customStyle;
    }


    @Override
    public void setValue(Object value) {

        if (value != null && value instanceof Widget) {
            customWidget = (Widget) value;
            if (clickHandler == null && customWidget != null) {
                setClickHandler();
            }
            super.setValue(value);
        } else if (value != null) {
            ((LinkableCellInterface) customWidget).setItemValue(value);
        }
    }

    @Override
    protected Widget createActive() {
        return getCustomWidget();
    }


    @Override
    protected Widget createInactive() {
        HorizontalPanel labelPanel = new HorizontalPanel();
        Label label = new Label();
        String dispValue = String.valueOf(getValue());
        dispValue = dispValue.replace("\n", "<br/>");
        label.setText(dispValue);
        labelPanel.add(label);
        if (customWidget != null && ((LinkedLinkableCellInterface) customWidget).isShowLink()) {
            Anchor link = new Anchor();
            String text = String.valueOf(getLinkValue());
            text = text.replace("\n", "<br/>");
            link.setText(text);
//            link.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
            link.getElement().addClassName("visitedLink");
            link.getElement().getStyle().setFloat(Style.Float.RIGHT);
//            link.getElement().getStyle().setTextDecoration(Style.TextDecoration.UNDERLINE);
            link.addClickHandler(clickEvent -> {
                if (clickHandler != null) {
                    clickHandler.execute();
                }
            });
            labelPanel.setSpacing(3);
            labelPanel.add(link);
        }
        if (customStyle != null && !"".equals(customStyle)) {
            labelPanel.setStyleName(customStyle);
        }
        return labelPanel;
    }

    @Override
    public Object getValue() {
        Object result = null;
        if (customWidget != null) {
            result = ((LinkedLinkableCellInterface) customWidget).getDisplayValue();
        }
        return result;
    }

    public Object getLinkValue() {
        Object result = null;
        if (customWidget != null) {
            result = ((LinkedLinkableCellInterface) customWidget).getLinkValue();
        }
        return result;
    }

    public Widget InActive() {
        return createInactive();
    }

    public void setFocus(boolean focused) {
        if (customWidget != null) {
            ((LinkedLinkableCellInterface) customWidget).setItemFocus(focused);
        }

    }

    private void setClickHandler() {
        this.clickHandler = ((LinkedLinkableCellInterface) customWidget).getClickHandler();
    }

    @Override
    public void prepare(Widget widget) {
        super.prepare(widget);
    }

    @Override
    public Object getNewValue() {
        return null;
    }

    public Widget getCustomWidget() {
        return customWidget;
    }


}
