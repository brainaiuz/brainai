package com.edatasite.workforce.gwt.core.client.ui.editableTable.cell;

import com.edatasite.workforce.gwt.core.client.interfaces.LinkableCellInterface;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;
import org.gwt.advanced.client.ui.widget.cell.AbstractCell;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 11.06.14
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public class LinkableCell extends AbstractCell {

    private Widget customWidget = null;
    private String customStyle = null;
    private Command clickHandler;

    public LinkableCell(String customStyle) {
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
        return createInactive();
    }

    @Override
    protected Widget createInactive() {
        Div p = new Div();
        Anchor label = new Anchor();
        if (customStyle != null && !"".equals(customStyle)) {
            p.setStyleName(customStyle);
        }
        String text = String.valueOf(getValue());
        text = text.replace("\n", "<br/>");
        label.setText(text);
        label.addClickHandler(clickEvent -> {
            if (clickHandler != null) {
                clickHandler.execute();
            }
        });
        p.add(label);
        return p;
    }

    @Override
    public Object getValue() {
        Object result = null;
        if (customWidget != null) {
            result = ((LinkableCellInterface) customWidget).getDisplayValue();
        }
        return result;
    }

    public Widget InActive() {
        return createInactive();
    }

    public void setFocus(boolean focused) {
        if (customWidget != null) {
            ((LinkableCellInterface) customWidget).setItemFocus(focused);
        }

    }

    private void setClickHandler() {
        this.clickHandler = ((LinkableCellInterface) customWidget).getClickHandler();
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
