package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.Operands;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowCondition;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.LookUp2;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.ui.DynamicInputWidget;
import com.google.gwt.dom.client.Style;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Icon;

import java.util.*;

/**
 * Created by Hayot on 3/3/14.
 */
public class WorkflowFilterTable extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private FlexTable filterTable;
    private final VerticalPanel vp;
    private KpiRadioButton standartCriteria;
    private KpiRadioButton dynamicCriteria;
    private HorizontalPanel criteriaPatternLinks;
    private FlexTable criteriaPatternTable;
    private FlexTable dynamicCriteriaTable;

    private TextArea criteriaPattern;
    private TextArea dynamicCriteriaPanel;
    private String patternBeforeChange;
    private LinkedHashMap<String, ModelField> fields;
    private String[] additionalFields;
    private final Localize localize;

    public WorkflowFilterTable() {
        vp = new VerticalPanel();
        localize = new Localize();
        initWidget(vp);
        initFilterTable(false);
        initFilterRow(null);
        initCriteriaPattern(false);
        initDynamicCriteria(false);
    }

    private void initFilterTable(boolean dynamicCondition) {
        vp.clear();
        standartCriteria = new KpiRadioButton("ruleCriteriaType", wfmStrings.standardKpi());
        standartCriteria.setValue(!dynamicCondition);
        standartCriteria.addValueChangeHandler(event -> {
            if (standartCriteria.getValue()) {
                dynamicCriteriaTable.setVisible(false);
                criteriaPatternTable.setVisible(true);
                filterTable.setVisible(true);
            }
        });
        vp.add(standartCriteria);
        filterTable = new FlexTable();
        filterTable.setVisible(!dynamicCondition);
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
        criteriaPattern.setValue("");
        criteriaPattern.setValue("");
        initFilterRow(null);
    }

    private void initFilterRow(WorkflowCondition condition) {
        // add new row to filterTable
        int row = filterTable.getRowCount();
        int index = row + 1;
        //Operators
        DataListBox operators = new DataListBox();
        operators.setItems(getOpersAsSelectItem(new String[]{"AND", "OR"}, false));
        operators.ensureDebugId("firstColumn_"+index);
        operators.addValueChangeHandler(changeEvent -> onChange_());
        //Modules
        //DataListBox moduleColumns = new DataListBox();
        LookUp2 moduleColumns = new LookUp2();
        moduleColumns.setItems(getColumnsAsReferenceItems());
        moduleColumns.ensureDebugId("modulcolumn_"+index);
        moduleColumns.addValueChangeHandler(changeEvent -> {
            onChange_();
            notifyOnColumnSelected(moduleColumns);
        });
        moduleColumns.getSuggestBox().addSelectionHandler(event -> {
            onChange_();
            notifyOnColumnSelected(moduleColumns);
        });
        //Operands
        DataListBox operands = new DataListBox();
        operands.ensureDebugId("operands_"+index);
        operands.addValueChangeHandler(changeEvent -> {
            onChange_();
            notifyOnOperandSelected((DataListBox) changeEvent.getSource());
        });
        operands.setItems(getOpersAsSelectItem(Operands.StringT.ALL));
        //Value
        DynamicInputWidget value = new DynamicInputWidget(null);
        value.ensureDebugId("valuefield_"+index);
        value.addBlurHandler(blurEvent -> onChange_());
        //Add Link
        Icon addrow = new Icon();
        addrow.setStyleName("ficon--plus pointer");
        addrow.ensureDebugId("rule_criteria_add" + index);
        addrow.addClickHandler(event -> initFilterRow(null));
        //Remove Link
        Icon remove = new Icon();
        remove.setStyleName("ficon--trash pointer");
        remove.ensureDebugId("rule_criteria_remove" + index);
        remove.addClickHandler(clickEvent -> removeRemove(getWidgetRow((Widget) clickEvent.getSource(), filterTable)));

        filterTable.setWidget(row, 0, new HTML("<b>" + index + "</b>"));
        filterTable.setWidget(row, 1, operators);
        filterTable.setWidget(row, 2, moduleColumns);
        filterTable.setWidget(row, 3, operands);
        filterTable.setWidget(row, 4, value);
        filterTable.setWidget(row, 5, addrow);
        filterTable.setWidget(row, 6, remove);

        if (condition != null) {
            moduleColumns.setSelectedByDescription(condition.getColumn());
            notifyOnColumnSelected(moduleColumns);
            operators.setSelectedByValue(condition.getOperator(), true);
            if (moduleColumns.getSelectedItem() != null) {
                operands.setSelectedByValue(localize.localizeByCode(condition.getOperand()), true);
                notifyOnOperandSelected(operands);
                value.setValue(condition.getValue());
            }
        } else {
            onChange_();
        }
    }

    private WorkflowCondition getRowAsCondition(int row) {
        DataListBox operators = (DataListBox) filterTable.getWidget(row, 1);
        LookUp2 columns = (LookUp2) filterTable.getWidget(row, 2);
        DataListBox operands = (DataListBox) filterTable.getWidget(row, 3);
        DynamicInputWidget value = (DynamicInputWidget) filterTable.getWidget(row, 4);
        WorkflowCondition condition = new WorkflowCondition();
        if (operators != null && operators.getSelectedItem() != null) {
            condition.setOperator(operators.getSelectedItem().getReferenceCode());
        }
        if (columns != null && columns.getSelectedItem() != null) {
            String columnCode = columns.getSelectedItem().getReferenceCode();
            if (columnCode != null && (columnCode.startsWith("string_value") || columnCode.startsWith("double_value") || columnCode.startsWith("date_value"))) {
                condition.setColumn(columnCode);
                condition.setCustomFieldName(columns.getSelectedItem().getName());
            } else {
                condition.setColumn(columnCode);
            }
        }
        if (operands != null && operands.getSelectedItem() != null) {
            condition.setOperand(operands.getSelectedItem().getReferenceCode());
        }
        if (value != null) {
            condition.setValue(value.getValue());
        }
        condition.setConditionID(row + 1);
        return condition;
    }

    private void notifyOnColumnSelected(LookUp2 moduleColumns) {
        if (moduleColumns != null && moduleColumns.getSelectedItem() != null && moduleColumns.getSelectedItem().getReferenceCode() != null) {
            int row = getWidgetRow(moduleColumns, filterTable);
            DataListBox operands = (DataListBox) filterTable.getWidget(row, 3);
            DynamicInputWidget value = (DynamicInputWidget) filterTable.getWidget(row, 4);
            String columnCode = moduleColumns.getSelectedItem().getReferenceCode();

            if (columnCode != null) {
                if (fields.containsKey(columnCode)) {
                    ModelField field = fields.get(columnCode);
                    value.setField(field);
                    if (field != null && field.getType() != null) {
                        if (Constants.UI_TYPE_CHECKBOX.equals(field.getWidget())) {
                            operands.setItems(getOpersAsSelectItem(Operands.StringT.SOME));
                        } else if (ModelField.TYPE.STRING.equals(field.getType()) && !Constants.UI_TYPE_DROPDOWN.equals(field.getWidget()) && !Constants.UI_TYPE_RADIOBUTTON.equals(field.getWidget())) {
                            operands.setItems(getOpersAsSelectItem(Operands.StringT.ALL));
                        } else if (ModelField.TYPE.INTEGER.equals(field.getType()) && !Constants.UI_TYPE_DROPDOWN.equals(field.getWidget()) && !Constants.UI_TYPE_RADIOBUTTON.equals(field.getWidget())) {
                            operands.setItems(getOpersAsSelectItem(Operands.NumberT.ALL));
                        } else if (ModelField.TYPE.DATE.equals(field.getType())) {
                            operands.setItems(getOpersAsSelectItem(Operands.DateT.ALL));
                        } else {
                            operands.setItems(getOpersAsSelectItem(Operands.Core.ALL));
                        }
                    }
                } else { //bu faqat creationDate, updatedDate uchun qilingan, ula modelFieldda yuq
                    operands.setItems(getOpersAsSelectItem(Operands.DateT.ALL));
                    value.initCustomDateWidget(null);
                }
            }
        }
    }

    private void notifyOnOperandSelected(DataListBox operandColumns) {
        if (operandColumns != null && operandColumns.getSelectedItem() != null && operandColumns.getSelectedItem().getReferenceCode() != null) {
            int row = getWidgetRow(operandColumns, filterTable);
            DynamicInputWidget value = (DynamicInputWidget) filterTable.getWidget(row, 4);
            String columnCode = operandColumns.getSelectedItem().getReferenceCode();

            if (columnCode != null && getDateOperandsAsList().contains(columnCode)) {
//                if (Operands.DateT.BETWEEN.equals(columnCode) || Operands.DateT.NOT_BETWEEN.equals(columnCode)) {
//                    value.setWidth("330px");
//                }
                value.initCustomDateWidget(columnCode);
            }
        }
    }

    private ArrayList<String> getDateOperandsAsList() {
        ArrayList<String> operands = new ArrayList<>();
        operands.addAll(Arrays.asList(Operands.DateT.ALL));
        return operands;
    }

    private void removeRemove(int row) {
        if (filterTable.getRowCount() > 1) {
            filterTable.removeRow(row);
            onChange_();
        } else {
            Info.warn(wfmStrings.youCannotDeleteTheLastRow());
        }
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
        generatePattern();
    }

    private void generatePattern() {
        StringBuilder pattern = new StringBuilder();
        String lastOperator = null;
        boolean opened = false;
        for (int row = 0; row < filterTable.getRowCount(); row++) {
            int index = row + 1;
            if (row != 0 && filterTable.getWidget(row, 1) instanceof DataListBox) {
                DataListBox operators = (DataListBox) filterTable.getWidget(row, 1);
                if (operators.getSelectedItem() != null && operators.getSelectedItem().getReferenceCode() != null) {
                    String operator = operators.getSelectedItem().getReferenceCode();
                    if (lastOperator != null) {
                        if ("OR".equals(operator) && !"OR".equals(lastOperator)) {
                            String firstOfParantesis = opened ? "" : "(";
                            pattern = new StringBuilder(firstOfParantesis + pattern + ")");
                            opened = false;
                        } else if ("AND".equals(operator)) {
                            if ("OR".equals(lastOperator)) {
                                int lastIndexOfOR = pattern.lastIndexOf("OR");
                                if (lastIndexOfOR >= 0) {
                                    String sub_1 = pattern.substring(0, lastIndexOfOR);
                                    String sub_2 = pattern.substring(lastIndexOfOR + 2);
                                    pattern = new StringBuilder(sub_1 + "OR (" + sub_2);
                                    opened = true;
                                }
                            }
                        }
                    }
                    lastOperator = operator;
                    pattern.append(" ").append(operator).append(" ").append(index);
                }
            } else {
                pattern = new StringBuilder("" + index);
            }
        }
        if (opened) {
            pattern.append(")");
        }
        if (criteriaPattern != null) {
            criteriaPattern.setValue(pattern.toString());
        }
    }


    private void initCriteriaPattern(boolean dynamicCondition) {
        criteriaPatternTable = new FlexTable();
        criteriaPatternTable.setVisible(!dynamicCondition);
        vp.add(criteriaPatternTable);
        criteriaPattern = new TextArea();
        criteriaPattern.ensureDebugId("criteriaPattern");
//        criteriaPattern.setWidth("660px");
        criteriaPattern.setCharacterWidth(131);
        criteriaPatternLinks = new HorizontalPanel();
        criteriaPatternTable.setWidget(0, 0, new HTML(wfmStrings.criteriaPattern()));
        criteriaPatternTable.setWidget(0, 1, criteriaPattern);
        criteriaPatternTable.setWidget(0, 2, criteriaPatternLinks);
        addChangeLink();
        criteriaPatternTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
        criteriaPatternTable.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_MIDDLE);
    }

    private void initDynamicCriteria(boolean dynamicCondition) {
        dynamicCriteria = new KpiRadioButton("ruleCriteriaType", wfmStrings.dynamicType());
        dynamicCriteria.setValue(dynamicCondition);
        vp.add(dynamicCriteria);
        dynamicCriteriaTable = new FlexTable();
        dynamicCriteriaTable.setVisible(dynamicCondition);
        vp.add(dynamicCriteriaTable);
        dynamicCriteriaPanel = new TextArea();
        dynamicCriteriaPanel.ensureDebugId("forkflowFilterTable_dynamicCriteriaPanel");
        if (!Utils.isAdmin()) {
            dynamicCriteriaPanel.setVisible(false);
        }
        dynamicCriteriaPanel.setCharacterWidth(5000);

        TextArea emptyCriteriaPanel = new TextArea();
        emptyCriteriaPanel.ensureDebugId("forkflowFilterTable_emptyCriteriaPanel");
        emptyCriteriaPanel.setCharacterWidth(5000);
        emptyCriteriaPanel.setEnabled(false);
        dynamicCriteriaTable.setWidget(0, 0, new HTML(wfmStrings.queryPanel()));
        if (Utils.isAdmin()) {
            dynamicCriteriaTable.setWidget(0, 1, dynamicCriteriaPanel);
        } else {
            dynamicCriteriaTable.setWidget(0, 1, emptyCriteriaPanel);
        }
        addChangeLink();
        dynamicCriteriaTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
        dynamicCriteriaTable.getCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_MIDDLE);

        dynamicCriteria.addValueChangeHandler(event -> {
            if (dynamicCriteria.getValue()) {
                dynamicCriteriaTable.setVisible(true);
                criteriaPatternTable.setVisible(false);
                filterTable.setVisible(false);
            }
        });
    }

    private void addChangeLink() {
        criteriaPatternLinks.clear();
        criteriaPattern.setEnabled(false);
        SimpleLink changePatternLink = new SimpleLink(wfmStrings.changePattern());
        changePatternLink.ensureDebugId("change_pattern");
        criteriaPatternLinks.add(changePatternLink);
        changePatternLink.addClickHandler(clickEvent -> {
            patternBeforeChange = criteriaPattern.getValue();
            addSaveClosePatternLink();
        });
    }

    private void addSaveClosePatternLink() {
        criteriaPatternLinks.clear();
        criteriaPattern.setEnabled(true);
        SimpleLink savePattern = new SimpleLink(wfmStrings.save());
        savePattern.ensureDebugId("save_pattern");
        criteriaPatternLinks.add(savePattern);
        savePattern.addClickHandler(clickEvent -> {
            patternBeforeChange = criteriaPattern.getValue();
            addChangeLink();
        });
        criteriaPatternLinks.add(new HTML("  |  "));
        SimpleLink closeChangePattern = new SimpleLink(wfmStrings.cancel());
        closeChangePattern.ensureDebugId("cancel_pattern");
        criteriaPatternLinks.add(closeChangePattern);
        closeChangePattern.addClickHandler(clickEvent -> {
            if (patternBeforeChange == null) {
                patternBeforeChange = "";
            }
            criteriaPattern.setValue(patternBeforeChange);
            addChangeLink();
        });
    }

    public String getPattern() {
        return criteriaPattern.getValue();
    }

    public void setPattern(String pattern) {
        criteriaPattern.setValue(pattern);
    }

    private int getWidgetRow(Widget widget, FlexTable table) {
        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getCellCount(row); col++) {
                Widget w = table.getWidget(row, col);
                if (w == widget) {
                    return row;
                }
            }
        }
        throw new RuntimeException("Unable to determine widget row");
    }

    public void setFields(LinkedHashMap<String, ModelField> fields) {
        this.fields = fields;
    }

    public LinkedHashMap<String, ModelField> getFields() {
        return fields;
    }

    public void setAdditionalFields(String[] additionalFields) {
        this.additionalFields = additionalFields;
    }

    public SelectItem[] getColumnsAsReferenceItems() {
        ArrayList<SelectItem> result = new ArrayList<>();
        int i = 0;
        if (fields != null && fields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
                String localized;
                if (entry.getValue().getDynamicLabel() != null && !"".equals(entry.getValue().getDynamicLabel())) {
                    localized = entry.getValue().getDynamicLabel();
                } else if (entry.getValue().isIsCustomField()) {
                    localized = entry.getValue().getLabel();
                } else {
                    localized = localize.localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
                }
                ReferenceItem referenceItem = new ReferenceItem(i, localized != null ? localized : entry.getValue().getField_ID(), entry.getValue().getField_ID());
                result.add(referenceItem);
                i++;
            }
        }
        if (additionalFields != null) {
            for (String s : additionalFields) {
                SelectItem item = new SelectItem(i, localize.localizeByCode(s), s);
                result.add(item);
                i++;
            }
        }
        result.sort(Comparator.comparing(SelectItem::getName));
        return result.toArray(new SelectItem[]{});
    }

    public SelectItem[] getOpersAsSelectItem(String[] operators) {
        return getOpersAsSelectItem(operators, true);
    }

    public SelectItem[] getOpersAsSelectItem(String[] operands, boolean isLocolize) {
        ArrayList<SelectItem> items = new ArrayList<>();
        int i=0;
        for (String s : operands) {
            items.add(new SelectItem(i, isLocolize ? localize.localizeByCode(s) : s, s));
            i++;
        }
        return items.toArray(new SelectItem[]{});
    }

    public HashMap<Integer, WorkflowCondition> getConditions() {
        HashMap<Integer, WorkflowCondition> conditions = new HashMap<>();
        for (int row = 0; row < filterTable.getRowCount(); row++) {
            int index = row + 1;
            WorkflowCondition condition = getRowAsCondition(row);
            if (condition.getColumn() != null) {
                conditions.put(index, condition);
            }
        }
        return conditions;
    }

    public void setConditions(HashMap<Integer, WorkflowCondition> conditions, boolean dynamicCondition) {
        initFilterTable(dynamicCondition);
        if (conditions != null && conditions.size() > 0) {
            for (WorkflowCondition condition : conditions.values()) {
                initFilterRow(condition);
            }
            reindex();
        } else {
            initFilterRow(null);
        }
        initCriteriaPattern(dynamicCondition);
        initDynamicCriteria(dynamicCondition);
    }

    public boolean isDynamicContion() {
        return dynamicCriteria.getValue();
    }

    public String getDynamicConditionQuery() {
        return dynamicCriteriaPanel.getValue();
    }

    public void setDynamicConditionQuery(String query) {
        dynamicCriteriaPanel.setValue(query);
    }

}
