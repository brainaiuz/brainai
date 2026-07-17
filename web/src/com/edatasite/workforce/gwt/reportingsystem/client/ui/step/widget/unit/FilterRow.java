package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.CheckListBox;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DurationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.OperationType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.impl.cldr.DateTimeFormatInfoImpl_en;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;
import java.util.Iterator;

/**
 * Created by Virus on 9/5/14.
 */
public class FilterRow extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static FilterRowUiBinder ourUiBinder = GWT.create(FilterRowUiBinder.class);
    @UiField
    DataListBox columnSelectBox;
    @UiField
    DataListBox operation;
    @UiField
    DRSDateBox fromDate;
    @UiField
    DRSDateBox toDate;
    @UiField
    DivElement toDatePanel;
    @UiField
    DivElement fromDatePanel;
    @UiField
    DivElement textBoxPanel;
    @UiField
    LabelElement textBoxField;
    @UiField
    LabelElement toDateField, textLabel;
    @UiField
    LabelElement fromDateField, criteria;
    @UiField
    ButtonElement updateButton;
    @UiField
    DivElement lookupPanel;
    @UiField
    ReportingLookUp lookUp;
    @UiField
    DivElement listBoxPanel;
    @UiField
    DivElement checkListBoxPanel;
    @UiField
    CheckListBox checkListBox;
    @UiField
    DataListBox listBox;
    @UiField
    TextBox textBox;
    private ReportingStepControlView view;
    public FilterRow() {
        HTMLPanel rootElement = ourUiBinder.createAndBindUi(this);
        initWidget(rootElement);
        initHandlers();
    }

    public static String getDate(Date date) {
        if (date == null) {
            return null;
        }
        return date(date, "dd MMM, yyyy");
    }

    public static String setDate(String date) {
        try {
            return date(DateTimeFormat.getFormat("dd MMM, yyyy").parse(date), Utils.getShortDateFormat());
        } catch (Exception e) {
            return date;
        }
    }

    private static String date(Date date, String format) {
        return DateUtil.CustomDateTimeFormat.getFormatByEnglish(format, new DateTimeFormatInfoImpl_en()).format(date);
    }

    public void oninitialize() {
        lookUp.getTextBox().setStyleName("form-control");
        updateButton.setInnerHTML(wfmStrings.update());
        criteria.setInnerHTML(wfmStrings.criteria());
        fromDateField.setInnerHTML(wfmStrings.from());
        toDateField.setInnerHTML(wfmStrings.to());
        textBoxField.setInnerHTML(wfmStrings.text());
        textLabel.setInnerHTML(wfmStrings.text());
        SelectItem[] items = new SelectItem[view.getReport().getColumnMap().size()];
        int i = 0;
        for (ColumnRpc rpc : view.getReport().getColumnMap().values()) {
            items[i] = new SelectItem(i, rpc.getTitle(), rpc.getName());
            i++;
        }
        columnSelectBox.setItems(items);
        columnSelectedHandler();
        setListBoxData();
        ReportRpc reportRpc = view.getReport();
        String columnName = reportRpc.getFilterColumn();
        String operationType = reportRpc.getFilterOperation();
        String value = reportRpc.getFilterValue();
        if (reportRpc.getId() == null) {
            if (!Utils.isNullOrEmpty(columnName) && !Utils.isNullOrEmpty(operationType)) {
                ColumnRpc columnRpc = reportRpc.getColumnMap().get(columnName);
                if (columnRpc != null) {

                    columnSelectBox.setSelectedByDescription(reportRpc.getFilterColumn());
                    columnSelectedHandler();
                    operation.setSelectedByDescription(operationType);
                    operationSelectedHandler();

                    if (SqlColumnType.NUMBER.getName().equals(columnRpc.getType())) {
                        setValue(widget(), value);
                    } else {
                        if (SqlColumnType.DATE.getName().equals(columnRpc.getType())) {
                            if (DurationType.Between.name().equals(operationType)) {
                                fromDate.setText(FilterRow.setDate(value.split("_")[0]));
                                toDate.setText(FilterRow.setDate(value.split("_")[1]));
                            } else if (DurationType.AgeInDays.name().equals(operationType)) {

                            }else {
                                if (DurationType.Before.name().equals(columnRpc.getType())) {
                                    fromDate.setText(FilterRow.setDate(value));
                                } else {
                                    if (!DurationType.Equals.name().equals(operationType)
                                            && !DurationType.After.name().equals(operationType) &&
                                            !DurationType.Before.name().equals(operationType)) {
                                        String endDate = DurationType.valueOf(operationType).getEndDate();
                                        value = DurationType.valueOf(operationType).getStartDate() + (endDate == null ? "" : ("_" + endDate));
                                    } else {
                                        fromDate.setText(FilterRow.setDate(value));
                                    }
                                }
                            }
                        } else {
                            setValue(widget(), value);
                        }
                    }

                }

            } else {
                for (ColumnRpc columnRpc : reportRpc.getColumnMap().values()) {
                    if (SqlColumnType.DATE.getName().equals(columnRpc.getType())) {
                        columnSelectBox.setSelectedByDescription(columnRpc.getName());
                        fromDate.setText(date(DateUtil.getMonthFirstDay(new Date()), Utils.getShortDateFormat()));
                        toDate.setText(date(DateUtil.getMonthLastDate(new Date()), Utils.getShortDateFormat()));
                        columnSelectedHandler();
                        operation.setSelectedByDescription(DurationType.Between.name());
                        operationSelectedHandler();
                        break;
                    }
                }
            }
        }
    }

    private void setListBoxData() {
        ColumnRpc rpc = view.getReport().getColumnMap().get(columnSelectBox.getSelectedItem(true).getDescription());
        if (!SqlColumnType.DATE.getName().equals(rpc.getType()) && widget() instanceof DataListBox) {
            final String value = ReportingUtils.getValue(widget());
            ReportingService.App.get().getFilterSelectItems("", rpc, view.getReport(), new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void onSuccess(SelectItem[] result) {
                    listBox.clearSelected();
                    listBox.setItems(result, value);
                }
            });
        }
    }

    private void initHandlers() {
        columnSelectBox.addValueChangeHandler(event -> {
            columnSelectedHandler();
            lookUp.clear();
            lookUp.clearOracleItems();
            lookUp.letters.clear();
            setFilter();
            setListBoxData();
        });
        operation.addValueChangeHandler(event -> {
            ColumnRpc rpc = view.getReport().getColumnMap().get(columnSelectBox.getSelectedItem(true).getDescription());
            if (SqlColumnType.DATE.getName().equals(rpc.getType())) {
                operationSelectedHandler();
            } else {
                widget();
            }
        });
        lookUp.setBeforeSearch(() -> clearListingFilter());
        HandlerUtils.click(updateButton, event -> {
            LoadingPanel.loading(true);
            view.updateReport();
        });

    }

    public void setFilter() {
        clearListingFilter();
        if (columnSelectBox.getSelectedItem() == null) {
            return;
        }
        ReportRpc reportRpc = view.getReport();
        ColumnRpc rpc = reportRpc.getColumnMap().get(columnSelectBox.getSelectedItem().getDescription());
        String operationType = operation.getSelectedItem(true).getDescription();

        //create mew filter
        String value = "";
        if (SqlColumnType.NUMBER.getName().equals(rpc.getType())) {
            value = ReportingUtils.getValue(widget());
        } else {
            if (SqlColumnType.DATE.getName().equals(rpc.getType())) {
                if (DurationType.Between.name().equals(operationType)) {
                    Date fromDateValue = fromDate.getSelectedDate();
                    Date toDateValue = toDate.getSelectedDate();
                    if (!(fromDateValue == null || toDateValue == null)) {
                        if (!toDate.getText().contains("23:59:59")) {
                            value = getDate(fromDateValue) + "_" + getDate(toDateValue) + " 23:59:59";
                        }
                        value = getDate(fromDateValue) + "_" + getDate(toDateValue);
                    }
                } else {
                    if (DurationType.Before.name().equals(operationType)) {
                        value = getDate(fromDate.getSelectedDate());
                    } else {
                        if (!DurationType.Equals.name().equals(operationType)
                                && !DurationType.After.name().equals(operationType) &&
                                !DurationType.Before.name().equals(operationType) && !DurationType.AgeInDays.name().equals(operationType)) {
                            String endDate = DurationType.valueOf(operationType).getEndDate();
                            value = DurationType.valueOf(operationType).getStartDate() + (endDate == null ? "" : ("_" + endDate));
                        } else if (DurationType.AgeInDays.name().equals(operationType)) {
                            value = "";
                        } else {
                            value = getDate(fromDate.getSelectedDate());
                        }
                    }
                }
            } else {
                value = ReportingUtils.getValue(widget());
            }
        }
        if (value == null || value.isEmpty()) {
            return;
        }
        reportRpc.getValues().add(value);

        rpc.setListFilter(true);
        reportRpc.getFieldd().add(rpc);

        reportRpc.addOperator(operationType);

        if (reportRpc.getFieldd().size() > 1) {
            reportRpc.setBoolTypeAt(reportRpc.getFieldd().size() - 2, "And");
            reportRpc.addToBoolType("");
        } else {
            if (reportRpc.getBoolType().size() > 0) {
                reportRpc.setBoolTypeAt(0, "");
            } else {
                reportRpc.addToBoolType("");
            }
        }
        reportRpc.getSett().add(100);
    }

    private void clearListingFilter() {
        ReportRpc reportRpc = view.getReport();

        //clear old filters
        Iterator<ColumnRpc> iterator = reportRpc.getFieldd().iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (iterator.next().isListFilter()) {
                iterator.remove();
                reportRpc.getValues().remove(i);
                reportRpc.removeOperator(i);
                reportRpc.getSett().remove(i);
                if (i > 0) {
                    reportRpc.removeBoolAt(i - 1);
                }
                i--;
            }
            i++;
        }
        for (i = reportRpc.getValues().size(); i < reportRpc.getBoolType().size(); i++) {
            reportRpc.removeBoolAt(i);
        }
    }

    private void operationSelectedHandler() {
        String value = operation.getSelectedItem(true).getDescription();
        if (DurationType.Between.name().equals(value)) {
            show(fromDatePanel, toDatePanel);
            fromDateField.setInnerHTML(wfmStrings.from());
            toDateField.setInnerHTML(wfmStrings.to());
        } else if (DurationType.AgeInDays.name().equals(value)) {

        } else {
            if (!DurationType.Equals.name().equals(value) && !DurationType.After.name().equals(value) &&
                    !DurationType.Before.name().equals(value)) {
                show();
            } else {
                show(fromDatePanel);
                fromDateField.setInnerHTML("Date:");
            }
        }
        operation.setTitle(operation.getSelectedItem(true).getName());
    }

    private void columnSelectedHandler() {
        ReportRpc reportRpc = view.getReport();
        ColumnRpc rpc = reportRpc.getColumnMap().get(columnSelectBox.getSelectedItem(true).getDescription());
        if (SqlColumnType.DATE.getName().equals(rpc.getType())) {
            operation.setItems(DurationType.asSelectItems());
            operationSelectedHandler();
        } else {
            operation.setItems(OperationType.asSelectItems(rpc.getType()));
            widget();
        }
    }

    public void setView(ReportingStepControlView view) {
        this.view = view;
        lookUp.setView(view);
    }

    private void show(Element... elements) {
        NodeList nodeList = Utils.getElementsByName("filter");
        for (int i = 0; i < nodeList.getLength(); i++) {
            ((Element) nodeList.getItem(i).cast()).addClassName("hide");
        }
        for (Element element : elements) {
            element.removeClassName("hide");
        }
    }

    private Widget widget() {
        ColumnRpc rpc = view.getReport().getColumnMap().get(columnSelectBox.getSelectedItem(true).getDescription());
        String widgetType = String.valueOf(rpc.getFilterWidgetType()).trim().toLowerCase().replace("null", "");
        if (SqlColumnType.TIME.getName().equals(rpc.getType())) {
            widgetType = "textbox";
        }
        if ("dropdown".equals(widgetType)) {
            show(listBoxPanel);
            return listBox;
        } else if ("checkbox".equals(widgetType)) {
            show(checkListBoxPanel);
            return checkListBox;
        } else if ("lookup".equals(widgetType)) {
            show(lookupPanel);
            lookUp.setColumnName(rpc.getName());
            return lookUp;
        } else if ("textbox".equals(widgetType)) {
            show(textBoxPanel);
            return textBox;
        } else {
            show(lookupPanel);
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

    public void clearSelected() {
        columnSelectBox.setSelectedNullLabel();
        columnSelectedHandler();
        lookUp.clear();
        lookUp.clearOracleItems();
        lookUp.letters.clear();
    }

    interface FilterRowUiBinder extends UiBinder<HTMLPanel, FilterRow> {
    }
}