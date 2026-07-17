package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.AsyncWidget;
import com.edatasite.workforce.gwt.core.client.ui.CheckListBox;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.LookUp2;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.OperationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Anchor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

/**
 * Created by Virus on 9/6/14.
 */
public class AdvancedFilterRow extends AsyncWidget {
    static final String OPERATOR_AND = "and";
    static final String OPERATOR_OR = "or";
    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private HtmlTh col0;
    protected HtmlTh col1;
    private HtmlColumn col3;
    private HtmlColumn col4;
    private HtmlColumn valueWidgetColumn;
    private HtmlDiv buttonPanel;
    private Button orButton;
    private Button andButton;
    protected LookUp2 columns;
    DataListBox columnsItems;
    protected DataListBox operation;
    private HtmlDiv valuePanel;
    private HtmlDiv fromDatePanel;
    private HtmlDiv toDatePanel;
    protected DRSDateBox fromDate;
    protected DRSDateBox toDate;
    private HtmlDiv agingOperPanel;
    private HtmlDiv agingValuePanel;
    DataListBox agingOper;
    TextBox agingValueBox;
    private TextBox textBox;
    protected ReportingLookUp lookUp;
    private CheckListBox checkListBox = new CheckListBox();
    private DataListBox listBox;
    private Anchor removeRow;
    private Anchor addRow;
    private HtmlDiv columnsPanel;
    private HtmlDiv operationPanel;
    private HtmlDiv textBoxPanel;
    private HtmlDiv lookUpPanel;
    private HtmlLabel columnsLabel;
    private HtmlLabel operationLabel;
    private HtmlLabel fromDateLabel;
    private HtmlLabel toDateLabel;
    private HtmlLabel textBoxLabel;
    private HtmlLabel lookUpLabel;
    protected String operator = OPERATOR_AND;
    private int count = 7;
    protected ReportingStepControlView view;
    private Command addRowCommand;
    private boolean hidden;

    private static final String[] RANGE_SYMBOLS = new String[]{">", ">=", "<", "<="};

    String andOrText = "And";
    protected Integer set = 1;
    protected Integer id;
    private boolean showElement;

    public AdvancedFilterRow(ReportingStepControlView view, Integer id, boolean showElements) {
        super("tr");
        this.id = id;
        this.set = id == null ? 0 : set;
        this.showElement = showElements;
        this.view = view;
    }

    public void setShowElement(boolean showElement) {
        this.showElement = showElement;
        if (showElement) {
            col0.setWidth("230px");
        } else {
            col0.setWidth("120px");
        }
        buttonPanel.setVisible(showElement);
        if (!showElement) {
            col1.getElement().getStyle().setColor("#000000");
            columns.addStyleName("filter_panel_flex");
            operation.addStyleName("default-selection-width");
            columnsPanel.setStyleName("filter_panel_flex");
            operationPanel.setStyleName("filter_panel_flex");
            fromDatePanel.addStyleName("filter_panel_flex");
            toDatePanel.addStyleName("filter_panel_flex");
            agingOperPanel.addStyleName("filter_panel_flex");
            agingValuePanel.addStyleName("filter_panel_flex");
            textBoxPanel.setStyleName("filter_panel_flex");
            lookUpPanel.setStyleName("filter_panel_flex");
            columnsPanel.add(columnsLabel);
            operationPanel.add(operationLabel);
            lookUpPanel.add(lookUpLabel);
            textBoxPanel.add(textBoxLabel);
            fromDatePanel.add(fromDateLabel);
            toDatePanel.add(toDateLabel);
        }
    }

    @Override
    protected Widget onInitialize() {
        initialization();
        initWidget();
        loadingData();
        initStyle();
        initHandler();
        getCurrentRow();
        if (null != id) {
            setFilter(id);
        }
        return null;
    }

    private void initialization() {
        col0 = new HtmlTh();
        col1 = new HtmlTh();
        col1.addStyleName("customReport_tab_6__num");
        col3 = new HtmlColumn();
        col4 = new HtmlColumn();
        valueWidgetColumn = new HtmlColumn();

        buttonPanel = new HtmlDiv();
        buttonPanel.addStyleName("customReport_tab_6__and_or");
        orButton = new Button(wfmStrings.or());
        andButton = new Button(wfmStrings.and());

        columns = new LookUp2();
        columnsItems = new DataListBox();
        operation = new DataListBox();
        operation.setWithoutNullLabel(true);

        valuePanel = new HtmlDiv();
        valuePanel.addStyleName("form-control--reporting-tab-due-date");
        fromDatePanel = new HtmlDiv();
        toDatePanel = new HtmlDiv();

        agingOperPanel = new HtmlDiv();
        agingValuePanel = new HtmlDiv();


        agingOper = new DataListBox();
        agingOper.setWithoutNullLabel(true);
        agingOper.setItems(SelectItem.asItems(RANGE_SYMBOLS));
        agingValueBox = new TextBox();
        Validation.addNumericKeyboardListener(agingValueBox, 0);

        fromDate = new DRSDateBox();
        toDate = new DRSDateBox();
        textBox = new TextBox();
        lookUp = new ReportingLookUp();
        checkListBox = new CheckListBox();
        listBox = new DataListBox();

        MaterialIcon removeIcon = new MaterialIcon();
        removeIcon.addStyleName("ficon--trash");
        removeRow = new Anchor();
        removeRow.add(removeIcon);

        addRow = new Anchor();
        addRow.addStyleName("rowControl rowControl--add btn btn--icon btn--success");
        MaterialIcon addIcon = new MaterialIcon();
        addIcon.addStyleName("ficon--plus");
        addRow.add(addIcon);
        addRow.addClickHandler((event) -> {
            if (addRowCommand != null) {
                addRowCommand.execute();
            }
        });

        if (hidden) {
            addRow.setVisible(false);
            removeRow.setVisible(false);
        }
        columnsPanel = new HtmlDiv();
        operationPanel = new HtmlDiv();
        textBoxPanel = new HtmlDiv();
        lookUpPanel = new HtmlDiv();

        columnsLabel = new HtmlLabel();
        columnsLabel.setText(wfmStrings.column() + ":");
        operationLabel = new HtmlLabel();
        operationLabel.setText(wfmStrings.criteria() + ":");
        fromDateLabel = new HtmlLabel();
        fromDateLabel.setText(wfmStrings.from() + ":");
        toDateLabel = new HtmlLabel();
        toDateLabel.setText(wfmStrings.to() + ":");
        textBoxLabel = new HtmlLabel();
        textBoxLabel.setText(wfmStrings.text() + ":");
        lookUpLabel = new HtmlLabel();
        lookUpLabel.setText(wfmStrings.text() + ":");

        lookUp.setView(view);
        setEnable(false);
    }

    protected void setEnable(boolean enable) {
        lookUp.setEnabled(enable);
        fromDate.setEnabled(enable);
        toDate.setEnabled(enable);
        checkListBox.setEnabled(enable);
        listBox.setEnabled(enable);
        agingOper.setEnabled(enable);
        agingValueBox.setEnabled(enable);
        if (enable) {
            textBox.getElement().removeAttribute("disabled");
        } else {
            textBox.getElement().setPropertyString("disabled", "disabled");
        }
    }

    private <T> ValueChangeHandler<T> parentRefresh(boolean value) {
        return event -> {
            getParentPanel(value);
        };
    }

    protected void getParentPanel(boolean value) {
        RHTMLPanel parent = ((RHTMLPanel) AdvancedFilterRow.this.getParent());
        parent.refresh(value);
    }

    private void initHandler() {
        columns.getSuggestBox().addSelectionHandler(event -> {

            if (columns.getSelectedItem() != null) {
                GWT.log("Selected Column: " + columns.getSelectedItem());
                ColumnRpc rpc = view.getReport().getColumnMap().get(columns.getSelectedItem().getDescription());
                columnSelectedHandler();
                setEnable(columns.getSelectedItem() != null);
                setListBoxData(rpc, null);
                parentRefresh(true).onValueChange(null);
                lookUp.clear();
                lookUp.clearOracleItems();
                lookUp.letters.clear();
            } else {
                GWT.log("Selected Column: NULL");
            }
        });
        operation.addValueChangeHandler(event -> {
            operationSelectedHandler();
            parentRefresh(true).onValueChange(null);
            if (hidden) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REPORT_FILTER_ADD, view, AdvancedFilterRow.this);
            }
        });
        removeRow.addClickHandler(event -> {
            removeOrClearFromParent();
        });
        andButton.addClickHandler(event -> {
            operator = OPERATOR_AND;
            andOrText = "And";
            setAndOrLogic(andOrText);
            parentRefresh(true).onValueChange(null);
        });
        orButton.addClickHandler(event -> {
            operator = OPERATOR_OR;
            andOrText = "Or";
            setAndOrLogic(andOrText);
            parentRefresh(true).onValueChange(null);
        });
        HandlerUtils.change(textBox.getElement(), event -> parentRefresh(true).onValueChange(null));
        agingValueBox.addValueChangeHandler(parentRefresh(false));
        agingOper.addValueChangeHandler(parentRefresh(false));
        fromDate.dateBox.addValueChangeHandler(parentRefresh(false));
        toDate.dateBox.addValueChangeHandler(parentRefresh(false));
        listBox.addValueChangeHandler(event -> parentRefresh(false).onValueChange(null));
        lookUp.getTextBox().addValueChangeHandler(parentRefresh(false));
        lookUp.getSuggestBox().addSelectionHandler(event -> parentRefresh(false).onValueChange(null));
    }

    protected void removeOrClearFromParent() {
        RHTMLPanel parent = ((RHTMLPanel) AdvancedFilterRow.this.getParent());
        if (parent.getWidgetCount() > 1) {
            parent.remove(AdvancedFilterRow.this);
            parent.refresh(true);
        } else if (parent.getWidgetCount() == 1) {
            columns.clear();
            if (columns.getSelectedItem() != null) {
                ColumnRpc rpc = view.getReport().getColumnMap().get(columns.getSelectedItem(/*true*/).getDescription());
                columnSelectedHandler();
                setEnable(columns.getSelectedItem() != null);
                setListBoxData(rpc, null);
                parentRefresh(true).onValueChange(null);
                lookUp.clear();
                lookUp.clearOracleItems();
                lookUp.letters.clear();

                operation.clearSelected();
                operationSelectedHandler();
            }

        }
    }

    private void loadingData() {
        LinkedList<SelectItem> items = new LinkedList<>();
        int i = 0;
        for (ColumnRpc rpc : view.getReport().getColumnMap().values()) {
            items.add(new SelectItem(i, rpc.getTitle(), rpc.getName(), rpc.getType()));
            i++;
        }
        items.sort(Comparator.comparing(SelectItem::getName));
        columns.setItems(items.toArray(new SelectItem[0]));
        columnsItems.setItems(items.toArray(new SelectItem[0]));
        columnSelectedHandler();
    }

    private void hideWidget(Widget... widgets) {
        if (widgets != null) {
            for (Widget widget : widgets) {
                if (widget.isVisible()) {
                    widget.setVisible(false);
                    count--;
                }
            }
        }
        customStyling();
    }

    private void showWidget(Widget... widgets) {
        if (widgets != null && widgets.length > 1) {
            valuePanel.addStyleName("form-control--reporting-tab-due-date");
        } else {
            valuePanel.removeStyleName("form-control--reporting-tab-due-date");
        }
        if (widgets != null) {
            for (Widget widget : widgets) {
                if (!widget.isVisible()) {
                    widget.setVisible(true);
                    count++;
                }
            }
        }
        customStyling();
    }

    private void customStyling() {
        if (count > 0) {
            this.removeStyleName("customReport_tab_6__no-value");
        }
        if (count < 1) {
            this.addStyleName("customReport_tab_6__no-value");
        }
    }

    void columnSelectedHandler() {
        lookUp.clearOracleItems();
        lookUp.clearFilter();
        lookUp.letters.clear();
        textBox.setText("");
        fromDate.setText("");
        agingValueBox.setValue("");
        toDate.setText("");
        listBox.clear();
        checkListBox.clear();
        ReportRpc reportRpc = view.getReport();
        if (columns.getSelectedItem() != null) {
            if (columns.getSelectedItem() != null) {
                final ColumnRpc rpc = reportRpc.getColumnMap().get(columns.getSelectedItem(/*true*/).getDescription());
                if (SqlColumnType.DATE.getName().equals(rpc.getType())) {
                    SelectItem[] durationTypes = DurationType.asSelectItems();
                    Arrays.sort(durationTypes, Comparator.comparing(SelectItem::getName));
                    operation.setItems(durationTypes);
                    operation.setSelected(DurationType.asSelectItems()[0]);
                    hideWidget(textBoxPanel, lookUpPanel, checkListBox, listBox);
                    operationSelectedHandler();
                } else {
                    operation.setItems(OperationType.asSelectItems(rpc.getType()));
                    if (columns.getSelectedItem() != null) {
                        operation.setSelected(OperationType.asSelectItems(columns.getSelectedItem().getCategory())[0]);
                    }
                    if (operation.getElement() != null && operation.getElement().getFirstChildElement() != null) {
                        operation.getElement().getFirstChildElement().setClassName("select-wrapper form-control gwt-ListBox");
                    }
                    showWidget(getWidget());
                    hideWidget(fromDatePanel, toDatePanel, agingOperPanel, agingValuePanel);
                }
            }

        } else {
            hideWidget(textBoxPanel, lookUpPanel, checkListBox, listBox);
        }
        operation.setSelectedItem(operation.getSelectedItem(true));
    }

    void setListBoxData(ColumnRpc rpc, final String value) {
        if (!SqlColumnType.DATE.getName().equals(rpc.getType()) && getWidget() instanceof DataListBox) {
            ReportingService.App.get().getFilterSelectItems("", rpc, view.getReport(), new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void onSuccess(SelectItem[] result) {
                    listBox.clearSelected();
                    listBox.setItems(result, value);
                }
            });
        }
    }

    void operationSelectedHandler() {
        if (operation.getSelectedItem(true) != null) {
            String value = operation.getSelectedItem(true).getDescription();
            if (DurationType.AgeInDays.name().equals(value)) {
                showWidget(agingOperPanel, agingValuePanel);
                hideWidget(fromDatePanel, toDatePanel, lookUpPanel);
                agingValueBox.setPlaceHolder(wfmStrings.ageInDays());
            } else if (DurationType.Between.name().equals(value)) {
                fromDateLabel.setText(wfmStrings.from() + ":");
                fromDateLabel.addStyleName("label-margin-right");
                showWidget(fromDatePanel, toDatePanel);
                fromDate.setPlaceholder(wfmStrings.from());
                toDate.setPlaceholder(wfmStrings.to());
                toDate.dateBox.addValueChangeHandler(event -> {
                    if (hidden) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REPORT_FILTER_ADD, view, AdvancedFilterRow.this);
                    }
                });
                hideWidget(lookUpPanel, agingOperPanel, agingValuePanel);
            } else {
                fromDate.setPlaceholder(reportingStrings.chooseDate());
                toDate.setPlaceholder(reportingStrings.chooseDate());
                if (!DurationType.Equals.name().equals(value) && !DurationType.After.name().equals(value) &&
                        !DurationType.Before.name().equals(value)) {
                    hideWidget(fromDatePanel, toDatePanel, agingOperPanel, agingValuePanel);
                } else {
                    fromDateLabel.setText(wfmStrings.date() + ":");
                    fromDateLabel.removeStyleName("label-margin-right");
                    showWidget(fromDatePanel);
                    hideWidget(toDatePanel, agingOperPanel, agingValuePanel);
                }
            }
            operation.setTitle(operation.getSelectedItem(true).getName());
        }
    }

    private void initStyle() {

        if (showElement) {
            col0.setWidth("230px");
        } else {
            col0.setWidth("120px");
        }
        col1.add(new HTML("1"));

        col1.setWidth("30px");
        if (!showElement) {
            col1.getElement().getStyle().setColor("#000000");
        }
        col3.setWidth("25%");
        buttonPanel.setStyleName("btn-group btn-group-sm btn-toggle customReport_tab_6__and_or");
        buttonPanel.setVisible(showElement);
        orButton.addStyleName("btn");
        andButton.addStyleName("btn active");
//        columns.setStyleName("simpleGwt-ComboBox form-control");
        operation.setStyleName("need-add-form-control-to-child form-control form-control--parent");
        if (!showElement) {
            columns.addStyleName("default-selection-width");
            operation.addStyleName("default-selection-width");
        }
        textBox.setStyleName("form-control default-text-box-width");
        lookUp.addStyleName("form-control");
        checkListBox.setStyleName("form-control");
        listBox.setStyleName("form-control");
        fromDatePanel.setStyleName("col-md-4 no_padding_left");
        toDatePanel.setStyleName("col-md-4 no_padding_left");
        removeRow.setStyleName("rowControl btn btn--icon btn--white");
        removeRow.setTitle(reportingStrings.removeRow());

        if (!showElement) { // is not custom filter
            columnsPanel.setStyleName("filter_panel_flex");
            operationPanel.setStyleName("filter_panel_flex");
            fromDatePanel.addStyleName("filter_panel_flex");
            toDatePanel.addStyleName("filter_panel_flex");
            textBoxPanel.setStyleName("filter_panel_flex");
            lookUpPanel.setStyleName("filter_panel_flex");
        }

        columnsLabel.setStyleName("filter_label");
        operationLabel.setStyleName("filter_label");
        fromDateLabel.setStyleName("filter_label");
        toDateLabel.setStyleName("filter_label");
        textBoxLabel.setStyleName("filter_label");
        lookUpLabel.setStyleName("filter_label");
    }

    private void initWidget() {
        add(col0);
        add(col1);
        add(col3);
        add(col4);
        add(valueWidgetColumn);
        col0.add(addRow);
        col0.add(removeRow);
        col0.add(buttonPanel);
        buttonPanel.add(orButton);
        buttonPanel.add(andButton);
        if (!showElement) {
            columnsPanel.add(columnsLabel);
            operationPanel.add(operationLabel);
        }
        columnsPanel.add(columns);
        operationPanel.add(operation);

        col3.add(columnsPanel);
        col4.add(operationPanel);

        valuePanel.add(fromDatePanel);
        valuePanel.add(toDatePanel);
        valuePanel.add(agingOperPanel);
        valuePanel.add(agingValuePanel);
        valueWidgetColumn.add(valuePanel);
        if (!showElement) {
            lookUpPanel.add(lookUpLabel);
            textBoxPanel.add(textBoxLabel);
        }
        lookUpPanel.add(lookUp);
        textBoxPanel.add(textBox);

        valueWidgetColumn.add(textBoxPanel);
        valueWidgetColumn.add(lookUpPanel);
        valueWidgetColumn.add(checkListBox);
        valueWidgetColumn.add(listBox);
        if (!showElement) {
            fromDatePanel.add(fromDateLabel);
            toDatePanel.add(toDateLabel);
        }
        agingOperPanel.add(agingOper);
        agingValuePanel.add(agingValueBox);
        fromDatePanel.add(fromDate);
        toDatePanel.add(toDate);
    }


    public void setFilter(int i) {
        ReportRpc reportRpc = view.getReport();
        try {
            if (i > 0) {
                andOrText = reportRpc.getBoolTypeAt(i - 1);
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
        if (view.getReport().getSett().size() > i) {
            set = view.getReport().getSett().get(i);
        }
        ColumnRpc columnRpc = reportRpc.getColumnMap().get(reportRpc.getFieldd().get(i).getName());
        columnsItems.setSelectedByDescription(columnRpc.getName());
        columns.setSelected(columnsItems.getSelectedItem());
        columnSelectedHandler();
        operation.setSelectedByDescription(reportRpc.getOperators().get(i));
        operationSelectedHandler();
        String value = reportRpc.getValues().get(i);
        String operationType = reportRpc.getOperators().get(i);

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

    void setAndOrLogic(String andOrText) {
        if ("Or".equals(andOrText)) {
            orButton.addStyleName("active");
            andButton.removeStyleName("active");
        } else {
            andButton.addStyleName("active");
            orButton.removeStyleName("active");
        }
    }

    String getAndOrText() {
        if (buttonPanel.isVisible()) {
            return andOrText;
        } else {
            return "";
        }
    }

    public void getFilter(int i) {
        String value = getValue();
        if (columns.getSelectedItem() == null || value == null || value.isEmpty()) {
            return;
        }

        ReportRpc reportRpc = view.getReport();
        ColumnRpc columnRpc = reportRpc.getColumnMap().get(columns.getSelectedItem(/*true*/).getDescription());
        reportRpc.getFieldd().add(columnRpc);
        reportRpc.getValues().add(value);
        if (reportRpc.getBoolType().size() > 0) {
            reportRpc.setBoolTypeAt(reportRpc.getBoolType().size() - 1, getAndOrText());
        }
        reportRpc.addToBoolType("");
        if (reportRpc.getSett().size() <= i) {
            reportRpc.getSett().add(set);
        }
        reportRpc.addOperator(operation.getSelectedItem(true).getDescription());
    }

    protected String getValue() {
        if (columns.getSelectedItem() == null) {
            return "";
        }
        ColumnRpc rpc = view.getReport().getColumnMap().get(columns.getSelectedItem(/*true*/).getDescription());
        String operationType = operation.getSelectedItem(true) != null ? operation.getSelectedItem(true).getDescription() : "";
        String value = "";
        if (SqlColumnType.NUMBER.getName().equals(rpc.getType())) {
            value = ReportingUtils.getValue(getWidget());
        } else {
            if (SqlColumnType.DATE.getName().equals(rpc.getType())) {
                if (DurationType.Between.name().equals(operationType)) {
                    Date fromDateValue = fromDate.getSelectedDate();
                    Date toDateValue = toDate.getSelectedDate();
                    if (!(fromDateValue == null || toDateValue == null)) {
                        if (!toDate.getText().contains("23:59:59")) {
                            value = FilterRow.getDate(fromDateValue) + "_" + FilterRow.getDate(toDateValue) + " 23:59:59";
                        }
                        value = FilterRow.getDate(fromDateValue) + "_" + FilterRow.getDate(toDateValue);
                    }
                } else if (DurationType.AgeInDays.name().equals(operationType)) {
                    if (agingOper.getSelectedItem(true) != null && agingValueBox.getValue() != null && !agingValueBox.getValue().isEmpty()) {
                        value = agingOper.getSelectedItem(true).getName() + "_" + agingValueBox.getValue();
                    }
                } else {
                    if (DurationType.Before.name().equals(operationType)) {
                        value = FilterRow.getDate(fromDate.getSelectedDate());
                    } else {
                        if (!DurationType.Equals.name().equals(operationType)
                                && !DurationType.After.name().equals(operationType) &&
                                !DurationType.Before.name().equals(operationType) && !DurationType.AgeInDays.name().equals(operationType)) {

                            String endDate = DurationType.valueOf(operationType).getEndDate();
                            value = DurationType.valueOf(operationType).getStartDate() + (endDate == null ? "" : ("_" + endDate));
                        } else {
                            value = FilterRow.getDate(fromDate.getSelectedDate());
                        }
                    }
                }
            } else {
                value = ReportingUtils.getValue(getWidget());
            }
        }
        return value;
    }

    public void getCurrentRow() {
        int n = ((RHTMLPanel) this.getParent()).getWidgetCount();
        int index = 1;
        for (int i = 0; i < n; i++) {
            Widget widget = ((RHTMLPanel) this.getParent()).getWidget(i);
            if (widget instanceof AdvancedFilterRow) {
                if (widget.equals(this)) {
                    col1.clear();
                    col1.add(new HTML("" + index));
                    return;
                }
                index++;
            }
        }
    }

    protected Widget getWidget() {
        if (columns.getSelectedItem() == null) {
            hideWidget(textBoxPanel, checkListBox, listBox);
            //lookUp.setColumnName(rpc.getName());
            return lookUp;
        }
        ColumnRpc rpc = view.getReport().getColumnMap().get(columns.getSelectedItem(/*true*/).getDescription());
        String widgetType = String.valueOf(rpc.getFilterWidgetType()).trim().toLowerCase().replace("null", "");
        if (SqlColumnType.TIME.getName().equals(rpc.getType())) {
            widgetType = "textbox";
        }
        if ("dropdown".equals(widgetType)) {
            showWidget(listBox);
            hideWidget(lookUpPanel, checkListBox, textBoxPanel);
            return listBox;
        } else if ("checkbox".equals(widgetType)) {
            showWidget(checkListBox);
            hideWidget(lookUpPanel, textBoxPanel, listBox);
            return checkListBox;
        } else if ("lookup".equals(widgetType)) {
            showWidget(lookUpPanel);
            hideWidget(textBoxPanel, checkListBox, listBox);
            lookUp.setColumnName(rpc.getName());
            return lookUp;
        } else if ("textbox".equals(widgetType)) {
            showWidget(textBoxPanel);
            hideWidget(lookUpPanel, checkListBox, listBox);
            return textBox;
        } else {
            if (!SqlColumnType.DATE.getName().equals(rpc.getType())) {
                showWidget(lookUpPanel);
            }
            hideWidget(textBoxPanel, checkListBox, listBox);
            lookUp.setColumnName(rpc.getName());
            return lookUp;
        }
    }

    public void setValue(Widget widget, String text) {
        if (widget instanceof TextBox) {
            ((TextBox) widget).setText(text);
        } else if (widget instanceof ReportingLookUp) {
            ((ReportingLookUp) widget).getTextBox().setText(text);
        } else if (widget instanceof CheckListBox) {
            ((CheckListBox) widget).setSelectedItems(text.split("<->"));
        } else if (widget instanceof DataListBox) {
            ((DataListBox) widget).setSelectedByDescription(text);
        }
    }

    public String getOperator() {
        return operator;
    }

    public boolean hasFocus() {
        if (getWidget() instanceof ReportingLookUp) {
            return lookUp.hasFocus();
        }
        return Utils.hasFocus(getWidget().getElement());
    }

    public void setAddRowCommand(Command addRowCommand) {
        this.addRowCommand = addRowCommand;
    }

    public void setHideCommand(boolean hidden) {
        this.hidden = hidden;
    }
}
