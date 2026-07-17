package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport;

import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.BudgetColumn;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.view.WorkflowFilterTable;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.*;

public class BudgetManagerColumnFilter extends KpiModal {
    private final FlowPanel panel;
    private final String section;
    private final LinkedHashMap<String, ModelField> fields = new LinkedHashMap<>();
    private final BudgetColumn budgetColumn;
    private final List<SelectItem> dateFields = new ArrayList<>();
    private final List<SelectItem> amountFields = new ArrayList<>();
    private WorkflowFilterTable filterTable;
    private DataListBox periodField;
    private DataListBox calculationType;
    private DataListBox calculationField;

    public BudgetManagerColumnFilter(String section, BudgetColumn budgetColumn) {
        this.section = section;
        this.budgetColumn = budgetColumn;
        panel = new FlowPanel();
        AllInOneService.App.get().getDefaultModelForm(getFormId(section), new AsyncCallback<ModelForm>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ModelForm modelForm) {
                fields.clear();
                Localize localize = new Localize();
                if (modelForm != null && modelForm.getFields().size() > 0) {
                    for (ModelField field : modelForm.getFields()) {
                        if (field.isUsableByWorkflow() && !field.isWorkflowAttribute()) {
                            fields.put(field.getField_ID(), field);
                            if (field.getWidgetForBm() != null) {
                                String localized;
                                if (field.getDynamicLabel() != null && !"".equals(field.getDynamicLabel())) {
                                    localized = field.getDynamicLabel();
                                } else if (field.isIsCustomField()) {
                                    localized = field.getLabel();
                                } else {
                                    localized = localize.localizeByFieldID(field.getForm_ID(), field.getField_ID());
                                }
                                SelectItem item = new SelectItem(field.getObjectID(), localized != null ? localized : field.getField_ID(), field.getField_ID());
                                if (field.getWidgetForBm().equals(DATE)) {
                                    dateFields.add(item);
                                } else if (field.getWidgetForBm().equals(AMOUNT)) {
                                    amountFields.add(item);
                                }
                            }
                        }
                    }
                    dateFields.add(new SelectItem(-1, wfmStrings.createdDate(), CREATED_DATE));
                    dateFields.add(new SelectItem(-2, wfmStrings.modifiedDate(), UPDATED_DATE));
                }
                initialize();
            }
        });
    }

    private void initialize() {
        filterTable = new WorkflowFilterTable();
        filterTable.setFields(fields);
        filterTable.clear();

        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_12, new FormGroup(wfmStrings.filters(), filterTable)));
        panel.add(row);

        periodField = new DataListBox();
        periodField.setWithoutNullLabel(true);
        periodField.setItems(dateFields.toArray(new SelectItem[]{}));

        GRow row2 = new GRow();
        row2.add(new GColumn(GColumnEnum.COL_12, new FormGroup(wfmStrings.period(), periodField)));
        panel.add(row2);

        calculationType = new DataListBox();
        calculationType.setWithoutNullLabel(true);
        calculationType.setItems(getCalculationTypes());

        calculationField = new DataListBox();
        calculationField.setWithoutNullLabel(true);
        calculationField.setItems(amountFields.toArray(new SelectItem[]{}));

        GRow row3 = new GRow();
        row3.add(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.taxCalcType(), calculationType)));
        GColumn gColumn = new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.calculate(), calculationField));
        if (budgetColumn == null || budgetColumn.getCalculationType().equals(COUNT)) {
            gColumn.setVisible(false);
        }
        row3.add(gColumn);

        calculationType.addValueChangeHandler(event -> gColumn.setVisible(!calculationType.getSelectedItem(true).getDescription().equals(COUNT)));
        panel.add(row3);

        if (budgetColumn != null) {
            setValues();
        }

        WfmButton2 okBtn = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY);
        okBtn.addClickHandler(click -> {
            close();
        });

        add(panel);
        addButton(okBtn);
    }

    public void getValues(BudgetColumn item) {
        item.setConditions(filterTable.getConditions());
        item.setPattern(filterTable.getPattern());
        item.setDynamicCondition(filterTable.isDynamicContion());
        item.setDynamicConditionQuery(filterTable.getDynamicConditionQuery());
        item.setPeriodField(periodField.getSelectedItem(true).getDescription());
        item.setCalculationType(calculationType.getSelectedItem(true).getDescription());
        if (!calculationType.getSelectedItem(true).getDescription().equals(COUNT)) {
            item.setCalculationField(calculationField.getSelectedItem(true).getDescription());
        }
    }

    private void setValues() {
        filterTable.setConditions(budgetColumn.getConditions(), budgetColumn.isDynamicCondition());
        filterTable.setDynamicConditionQuery(budgetColumn.getDynamicConditionQuery());
        filterTable.setPattern(budgetColumn.getPattern());

        if (budgetColumn.getPeriodField() != null) {
            periodField.setSelectedByDescription(budgetColumn.getPeriodField());
        }
        if (budgetColumn.getCalculationType() != null) {
            calculationType.setSelectedByDescription(budgetColumn.getCalculationType());
            calculationField.setSelectedByDescription(budgetColumn.getCalculationField());
        }
    }

    private String getFormId(String section) {
        switch (section) {
            case Constants.PURCHASE_ORDER:
                return LayoutRPC.PURCHASEORDER_FORM;
            case Constants.PRODUCTS:
                return LayoutRPC.PRODUCT_FORM;
            case Constants.EMPLOYEES:
                return LayoutRPC.HRMS_EMPLOYEE_FORM;
            case Constants.CUSTOMER:
                return LayoutRPC.CLIENT_FORM;
            case Constants.CHART_OF_ACCOUNT:
                return LayoutRPC.CHART_OF_ACCOUNT_FORM;
            case Constants.OPPORTUNITY:
                return LayoutRPC.OPPORTUNITY_FORM;
            default:
                return section;
        }
    }

    private SelectItem[] getCalculationTypes() {
        List<SelectItem> types = new ArrayList<>();
        types.add(new SelectItem(0, wfmStrings.count(), COUNT));
        types.add(new SelectItem(1, wfmStrings.sum(), SUM));
        types.add(new SelectItem(2, wfmStrings.average(), AVERAGE));
        return types.toArray(new SelectItem[]{});
    }
}
