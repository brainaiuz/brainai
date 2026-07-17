package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetFilterItem;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Faxriddinbek Taslimov on 19/06/19
 */
public class KpiWidgetFilterRow extends AdvancedFilterRow {

    private boolean isFirst;

    public KpiWidgetFilterRow(ReportingStepControlView view, Integer id, boolean isFirst, boolean showElements) {
        super(view, id, showElements);
        this.isFirst = isFirst;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    public void setFilter(int i) {
        KpiWidgetFilterItem kpiWidgetItem;
        if (isFirst) {
            kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne();
        } else {
            kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo();
        }
        try {
            if (i > 0) {
                andOrText = kpiWidgetItem.getBoolTypeAt(i - 1);
                if ("Or".equals(andOrText)) {
                    operator = OPERATOR_OR;
                } else if ("And".equals(andOrText)) {
                    operator = OPERATOR_AND;
                }
            } else {
                operator = null;
                andOrText = "";
            }
        } catch (IndexOutOfBoundsException e) {
            andOrText = "";
        }
        setAndOrLogic(andOrText);
        if (kpiWidgetItem.getSett().size() > i) {
            set = kpiWidgetItem.getSett().get(i);
        }
        ColumnRpc columnRpc = view.getReport().getColumnMap().get(kpiWidgetItem.getFieldd().get(i).getName());
        columnsItems.setSelectedByDescription(columnRpc.getName());
        columns.setSelected(columnsItems.getSelectedItem());
        columnSelectedHandler();
        operation.setSelectedByDescription(kpiWidgetItem.getOperators().get(i));
        operationSelectedHandler();
        String value = kpiWidgetItem.getValues().get(i);
        String operationType = kpiWidgetItem.getOperators().get(i);

        if (SqlColumnType.NUMBER.getName().equals(columnRpc.getType())) {
            setValue(getWidget(), value);
        } else {
            if (SqlColumnType.DATE.getName().equals(columnRpc.getType())) {
                if (DurationType.Between.name().equals(operationType)) {
                    fromDate.setText(FilterRow.setDate(value.split("_")[0]));
                    toDate.setText(FilterRow.setDate(value.split("_")[1]));
                } else if (DurationType.AgeInDays.name().equals(operationType)) {
                    agingOper.setSelectedByDescription(value.split("_")[0]);
                    agingValueBox.setValue(value.split("_")[1]);
                } else {
                    if (DurationType.Before.name().equals(columnRpc.getType())) {
                        fromDate.setText(FilterRow.setDate(value));
                    } else {
                        if (!DurationType.Equals.name().equals(operationType)
                                && !DurationType.After.name().equals(operationType) &&
                                !DurationType.Before.name().equals(operationType) && !DurationType.AgeInDays.name().equals(operationType)) {

                            String endDate = DurationType.valueOf(operationType).getEndDate();
                            value = DurationType.valueOf(operationType).getStartDate() + (endDate == null ? "" : ("_" + endDate));
                        } else {
                            fromDate.setText(FilterRow.setDate(value));
                        }
                    }
                }
            } else {
                setValue(getWidget(), value);
            }
        }
        setEnable(columns.getSelectedItem() != null);
        setListBoxData(columnRpc, value);
    }

    @Override
    public void getParentPanel(boolean value) {
        RHTMLPanel parent = ((RHTMLPanel) KpiWidgetFilterRow.this.getParent());
        parent.refresh(value);
    }


    @Override
    public void getCurrentRow() {
        int n = ((RHTMLPanel) this.getParent()).getWidgetCount();
        int index = 1;
        for (int i = 0; i < n; i++) {
            Widget widget = ((RHTMLPanel) this.getParent()).getWidget(i);
            if (widget instanceof KpiWidgetFilterRow) {
                if (widget.equals(this)) {
                    col1.clear();
                    col1.add(new HTML("" + index));
                    return;
                }
                index++;
            }
        }
    }

    @Override
    public void removeOrClearFromParent() {
        RHTMLPanel parent = ((RHTMLPanel) KpiWidgetFilterRow.this.getParent());
        if (parent.getWidgetCount() > 1) {
            parent.remove(KpiWidgetFilterRow.this);
        } else if (parent.getWidgetCount() == 1) {
            columns.clear();
            if (columns.getSelectedItem() != null) {
                ColumnRpc rpc = view.getReport().getColumnMap().get(columns.getSelectedItem(/*true*/).getDescription());
                columnSelectedHandler();
                setEnable(columns.getSelectedItem() != null);
                setListBoxData(rpc, null);
                lookUp.clear();
                lookUp.clearOracleItems();
                lookUp.letters.clear();

                operation.clearSelected();
                operationSelectedHandler();
            }

        }
        parent.refresh(true);
    }

    @Override
    public void getFilter(int i) {
        String value = getValue();
        if (columns.getSelectedItem() == null || value == null || value.isEmpty()) {
            return;
        }

        KpiWidgetFilterItem kpiWidgetItem;
        if (isFirst) {
            kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemOne();
        } else {
            kpiWidgetItem = view.getReport().getKpiWidgetItem().getKpiWidgetFilterItemTwo();
        }
        ColumnRpc columnRpc = view.getReport().getColumnMap().get(columns.getSelectedItem(/*true*/).getDescription());
        kpiWidgetItem.getFieldd().add(columnRpc);
        kpiWidgetItem.getValues().add(String.valueOf(value));
        if (kpiWidgetItem.getBoolType().size() > 0) {
            kpiWidgetItem.setBoolTypeAt(kpiWidgetItem.getBoolType().size() - 1, getAndOrText());
        }
        kpiWidgetItem.addToBoolType("");
        if (kpiWidgetItem.getSett().size() <= i) {
            kpiWidgetItem.getSett().add(set);
        }
        kpiWidgetItem.addOperator(operation.getSelectedItem(true).getDescription());
    }

}
