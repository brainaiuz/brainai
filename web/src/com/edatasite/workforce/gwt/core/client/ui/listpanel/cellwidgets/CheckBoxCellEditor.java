package com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
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
 * Time: 15:54:24
 *
 * <E> Column Type In Listing Panel
 */
public abstract class CheckBoxCellEditor<E> extends InlineCellEditor<E> {

    private String prevValue;
    private VerticalPanel vp;
    private Map<String, KpiCheckBox> checkboxs = new HashMap<>();

    public CheckBoxCellEditor() {
        super(new VerticalPanel());
        this.vp = (VerticalPanel) getContentWidget();
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
        vp.setStyleName("gwt-InlineCellEditor__checkboxes");
    }

    public VerticalPanel getVp() {
        return vp;
    }

    public void addCheckBox(KpiCheckBox box) {
        vp.add(box);
        checkboxs.put(box.getText(), box);
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, E cellValue, Callback<E> callback) {
        super.editCell(cellEditInfo, cellValue, callback);
        for (Widget aVp : vp) {
            KpiCheckBox check = (KpiCheckBox) aVp;
            if (check.getValue()) {
                check.setFocus(true);
                return;
            }
        }
    }

    public void clearPrev() {
        prevValue = null;
        for (String key : checkboxs.keySet()){
            checkboxs.get(key).setValue(false);
        }
    }

    public void setCheckedBoxValues(String values) {
        clearPrev();
        if (values != null) {
            String[] splitVal = values.split(",");
            for (String key : splitVal) {
                if (checkboxs.containsKey(key)) {
                    checkboxs.get(key).setValue(true);
                }
            }
        }
    }

    public void setCheckBoxDoubleValues(String values) {
        clearPrev();
        if (values != null) {
            String[] splitVal = values.split(",");
            for (String aSplitVal : splitVal) {
                Double dv = Double.parseDouble(aSplitVal.replace(",", ""));
                for (String key : checkboxs.keySet()) {
                    if (dv.equals(Double.parseDouble(key))) {
                        checkboxs.get(key).setValue(true);
                    }
                }
            }
        }
    }

    public String getCheckedValues() {
        String values = "";
        for (Widget aVp : vp) {
            KpiCheckBox check = (KpiCheckBox) aVp;
            if (check.getValue()) {
                if (!"".equals(values)) {
                    values = values + ",";
                }
                values = values + check.getText();
            }
        }
        return "".equals(values) ? null : values;
    }

    @Override
    protected boolean onAccept() {
        if ((prevValue != null && getCheckedValues() != null && !prevValue.equals(getCheckedValues()))
                || (prevValue == null && getCheckedValues() != null)
                || (prevValue != null && getCheckedValues() == null)) {
            return true;
        }
        super.cancel();
        return false;
    }

    @Override
    public void show() {
        prevValue = getCheckedValues();
        super.show();
        int offset = Window.getClientWidth() - getWidget().getOffsetWidth() - 32;
        if ((getPopupLeft() + getWidget().getOffsetWidth()) > Window.getClientWidth()) {
            setPopupPosition(offset, getPopupTop());
        }
    }
}
