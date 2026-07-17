package com.edatasite.workforce.gwt.core.client.ui.editableTable.cell;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.invoice.client.ui.view.CustomCellCheckBox;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomPercentageField;
import com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable.CustomTextBoxField;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.jquery.client.api.JQuery;
import org.gwt.advanced.client.ui.widget.cell.AbstractCell;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/24/12
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomCell extends AbstractCell {

    private Widget customWidget = null;
    private String customStyle = null;
    private Integer left;
    private Integer top;

    public CustomCell(String customStyle) {
        this.customStyle = customStyle;
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            return;
        }
        if (value instanceof Widget) {
            customWidget = (Widget) value;
            JQuery.$(customWidget).on("valueChange", (e, args) -> {
                InActive();
               return null;
            });
            super.setValue(value);
            return;
        }
        if (customWidget != null) {
            ((CustomCellInterface) customWidget).setItemValue(value);
        }
    }


    @Override
    protected Widget createActive() {
        if (customWidget instanceof DatePicker) {
            DatePicker customDatePicker = (DatePicker) customWidget;
            if (customDatePicker.isEnabled()) {
                customDatePicker.setDefaultPosition(true);
                customDatePicker.showPopupForCellWidget(left, top, getLabel().getOffsetHeight() != 0 ? getLabel().getOffsetHeight() : 15);
            }/* else {
                Info.show("", "Please select the month first", Info.Type.WARNING);
            }*/
        }
        return getCustomWidget();
    }

    @Override
    protected Widget createInactive() {
        Label label = getLabel();
        if (customWidget instanceof TextArea2) {
            label.setStyleName("cell-TextAreaLabel");
        } else if (customWidget instanceof TextArea) {
            label.getElement().getStyle().setProperty("textAlign", "left");
        } else if (customWidget instanceof CustomTextBoxField) {
            CustomTextBoxField customTextBoxField = (CustomTextBoxField) customWidget;
            if (customTextBoxField.isNumberwidget()) {
                label.getElement().getStyle().setProperty("textAlign", "right");
            }

        } else if (customWidget instanceof CustomPercentageField) {
            label.getElement().getStyle().setProperty("textAlign", "right");
        } else if (customStyle != null && !"".equals(customStyle)) {
            label.setStyleName(customStyle);
        } else if (customWidget instanceof CustomCellCheckBox) {
            CustomCellCheckBox checkBox = (CustomCellCheckBox) customWidget;
            String value = String.valueOf(getValue());
            checkBox.setValue(Boolean.valueOf(value));
            return checkBox;
        }
        String text = String.valueOf(getValue());
        text = text.replace("\n", "<br/>");
        label.setTextAsHtml(text);
        return label;
    }

    @Override
    public Object getValue() {
        Object result = null;
        if (customWidget != null) {
            result = ((CustomCellInterface) customWidget).getDisplayValue();
        }
        return result;
    }

    public Widget InActive() {
        //     fireEvent();
        return createInactive();
    }

    public void fireEvent() {
        if (customWidget != null /*&& customWidget.getEvents() != null && customWidget.getEvents().size() > 0*/) {

            customWidget.fireEvent(new GwtEvent<ChangeHandler>() {

                @Override
                public Type<ChangeHandler> getAssociatedType() {
                    return ChangeEvent.getType();
                }

                @Override
                protected void dispatch(ChangeHandler handler) {
                    handler.onChange(null);
                }
            });
        }
    }

    public void setFocus(boolean focused) {
        if (customWidget != null) {
            ((CustomCellInterface) customWidget).setItemFocus(focused);
        }

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

    public String getCustomStyle() {
        return customStyle;
    }

    public void setCustomStyle(String customStyle) {
        this.customStyle = customStyle;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }
}
