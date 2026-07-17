package com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 27-Nov-2010
 * Time: 15:55:28
 *
 * <E> Column Type In Listing Panel
 */
public abstract class RadioButtonCellEditor<E> extends InlineCellEditor<E> {

    private Map<String, KpiRadioButton> radioBtns = new HashMap<>();
    private String prevValue;
    private VerticalPanel vp;

    public RadioButtonCellEditor() {
        super(new VerticalPanel());
        this.vp = (VerticalPanel) getContentWidget();
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
        vp.setStyleName("gwt-InlineCellEditor__radiobuttons");
    }

    public VerticalPanel getVp() {
        return vp;
    }

    public void addRadio(KpiRadioButton radioBtn) {
        vp.add(radioBtn);
        radioBtns.put(radioBtn.getText(), radioBtn);
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, E cellValue, Callback<E> callback) {
        super.editCell(cellEditInfo, cellValue, callback);
        for(String key : radioBtns.keySet()){
            if (radioBtns.get(key).getValue()) {
                radioBtns.get(key).setFocus(true);
            }
        }
    }

    public void clearPrev(){
        prevValue = null;
        for (String key : radioBtns.keySet()) {
            radioBtns.get(key).setValue(false);
        }
    }

    public void setCheckedValue(String value) {
        clearPrev();
        if (radioBtns.containsKey(value)) {
            radioBtns.get(value).setValue(true);
        }
    }

    public void setCheckedDoubleValue(String value) {
        clearPrev();
        Double dv = Double.parseDouble(value.replace(",", ""));
        for (String key : radioBtns.keySet()) {
            if (dv.equals(Double.parseDouble(key))) {
                radioBtns.get(key).setValue(true);
                break;
            }
        }
    }

    public String getCheckedValue() {
        for (Widget aVp : vp) {
            KpiRadioButton radio = (KpiRadioButton) aVp;
            if (radio.getValue()) {
                return radio.getText();
            }
        }
        return null;
    }

    @Override
    protected boolean onAccept() {
        if ((prevValue != null && getCheckedValue() != null && !prevValue.equals(getCheckedValue()))
                || (prevValue == null && getCheckedValue() != null)
                || (prevValue != null && getCheckedValue() == null)) {
            return true;
        }
        super.cancel();
        return false;
    }

    @Override
    public void show() {
        prevValue = getCheckedValue();
        super.show();
        int offset = Window.getClientWidth() - getWidget().getOffsetWidth() - 32;
        if ((getPopupLeft() + getWidget().getOffsetWidth()) > Window.getClientWidth()) {
            setPopupPosition(offset, getPopupTop());
        }
    }
}
