package com.edatasite.workforce.gwt.core.client.form.formbuild;


import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.Operands;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.google.gwt.dom.client.Style;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;

/**
 * Created by Muhammadrizo 03/12/2021
 */
public class CustomFormFilterTable extends Composite {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private FlexTable filterTable;
    private VerticalPanel vp;
    private Localize localize;
    private CustomFormRuleItem ruleITem;

    public CustomFormFilterTable(CustomFormRuleItem rule) {
        vp = new VerticalPanel();
        localize = new Localize();
        this.ruleITem = rule;
        initWidget(vp);
        initFilterTable();
        initFilterRow();
        setValuesFromItemToTable();
    }

    private void initFilterTable() {
        vp.clear();
        filterTable = new FlexTable();
        filterTable.getElement().getStyle().setProperty("borderSpacing", "5px 10px");
        filterTable.getElement().getStyle().setProperty("borderCollapse", "separate");
        vp.add(filterTable);
    }

    public void clear() {
        if (filterTable.getRowCount() > 0) {
            for (int row = filterTable.getRowCount(); row > 0; row--) {
                filterTable.removeRow(row - 1);
            }
        }
        initFilterRow();
    }

    private void initFilterRow() {
        int row = 0;
        int index = row + 1;
        DataListBox operands = new DataListBox();
        operands.ensureDebugId("operands_" + index);
        operands.setItems(getOpersAsSelectItem(Operands.DateT.ALL));
        setSelectedItemToOperands(operands);

        TextBox value = new TextBox();
        value.ensureDebugId("valuefield_" + index);
        value.addBlurHandler(blurEvent -> onChange_());
        Validation.addNumericKeyboardListener(value,0);

        DataListBox ranges = new DataListBox();
        ranges.ensureDebugId("ranges_" + index);
        ranges.setItems(getOpersAsSelectItem(Operands.DateT.RANGERS, false));

//        filterTable.setWidget(row, 0, new HTML("<b>" + index + "</b>"));
        filterTable.setWidget(row, 1, operands);
        filterTable.setWidget(row, 2, ranges);
        filterTable.setWidget(row, 3, value);
    }

    public void setSelectedItemToOperands(DataListBox operands) {
        operands.setEnabled(false);
        for (SelectItem item : operands.getItems()) {
            if (item.getDescription().equals(Operands.DateT.AGE_IN_DAYS)) {
                operands.setSelected(item);
            }
        }
    }

    private void setValuesFromItemToTable(){
        if (ruleITem == null) return;
        DataListBox rangesList = (DataListBox) filterTable.getWidget(0, 2);
        TextBox widget = (TextBox) filterTable.getWidget(0, 3);
        rangesList.setSelectedByValue(ruleITem.getRange());
        widget.setValue(String.valueOf(ruleITem.getConditionValue()));
    }

    private void onChange_() {
        reindex();
    }

    private void reindex() {
        for (int row = 0; row < filterTable.getRowCount(); row++) {
            HTML index = (HTML) filterTable.getWidget(row, 0);
            index.setHTML("<b>" + (row + 1) + "</b>");
            if (row == 0 && filterTable.getWidget(row, 1) instanceof DataListBox) {
                DataListBox operators = (DataListBox) filterTable.getWidget(row, 1);
                operators.getElement().getStyle().setDisplay(Style.Display.NONE);
            }
        }
    }

    public SelectItem[] getOpersAsSelectItem(String[] operators) {
        return getOpersAsSelectItem(operators, true);
    }

    public SelectItem[] getOpersAsSelectItem(String[] operands, boolean isLocolize) {
        ArrayList<SelectItem> items = new ArrayList<>();
        int i = 0;
        for (String s : operands) {
            items.add(new SelectItem(i, isLocolize ? localize.localizeByCode(s) : s, s));
            i++;
        }
        return items.toArray(new SelectItem[]{});
    }

    public CustomFormRuleItem getRuleITem() {
        return setValuesToRuleItem();
    }

    public void setRuleITem(CustomFormRuleItem ruleITem) {
        this.ruleITem = ruleITem;
        setValuesFromItemToTable();
    }

    private CustomFormRuleItem setValuesToRuleItem() {
        ruleITem = new CustomFormRuleItem();
        DataListBox rangesList = (DataListBox) filterTable.getWidget(0, 2);
        String range = rangesList.getSelectedIndex() == -1 ? null : rangesList.getSelectedItemText();
        Integer value;
        TextBox widget = (TextBox) filterTable.getWidget(0, 3);

        //// checking input box is not null or empty
        if (widget != null && widget.getValue() != null && !widget.getValue().isEmpty()) {
            String widgetValue = widget.getValue();
            try {
                value = Integer.parseInt(widgetValue);
                ruleITem.setConditionValue(value);
            } catch (NumberFormatException ex) {
                return null;
            }
        }else{
            return null;
        }

        //// range dataListBox is not selected
        if (range == null || range.isEmpty() || range.equalsIgnoreCase("Please Select")) {
            return null;
        } else {
            ruleITem.setRange(range);
        }
        return ruleITem;
    }
}

