package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.AsyncWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.SummaryColumns;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Virus on 8/27/14.
 */
public class ReportingSummaryColumns extends AsyncWidget {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    interface ReportingSummaryColumnsUiBinder extends UiBinder<HTMLPanel, ReportingSummaryColumns> {
    }

    private ReportingStepControlView view;
    private static ReportingSummaryColumnsUiBinder ourUiBinder = GWT.create(ReportingSummaryColumnsUiBinder.class);
    @UiField
    TableSectionElement tbody;
    @UiField
    SpanElement columns, sum, average, largestValue, smallestValue, count;
//    @UiField
//    KpiCheckBox checkAll;


    public ReportingSummaryColumns() {
        super(null, "customReport_tab_4");
    }

    public void setView(ReportingStepControlView view) {
        this.view = view;
    }

    private ArrayList<SummaryColumns> list = new ArrayList<>();

    protected Widget onInitialize() {
        add(ourUiBinder.createAndBindUi(this));
//        checkAll.setText(wfmStrings.selectAll());
        columns.setInnerHTML(wfmStrings.columns().toUpperCase());
        largestValue.setInnerHTML(wfmStrings.largestValue().toUpperCase());
        count.setInnerHTML(wfmStrings.count().toUpperCase());
        sum.setInnerHTML(wfmStrings.sum().toUpperCase());
        average.setInnerHTML(wfmStrings.average().toUpperCase());
        smallestValue.setInnerHTML(wfmStrings.smallestValue().toUpperCase());
        loading();
        initChangeEventHandler();
        return null;
    }

    private void initChangeEventHandler() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REPORTING_COLUMN_CHANGE, (sender, args) -> {
            loading();
        });
    }

    private void loading() {
        for (SummaryColumns widgets : list) {
            widgets.removeFromParent();
        }
        list.clear();
        tbody.removeAllChildren();
        List<ColumnRpc> columns = new ArrayList<>(view.getReport().getSelectedColumns());
//        columns = new ArrayList<>(columns);
        // removing title == null values and then sorting by title
        columns = columns.stream().filter(columnRpc -> columnRpc.getTitle() != null).sorted(Comparator.comparing(o -> o.getTitle().toLowerCase())).collect(Collectors.toList());
        ArrayList<ColumnRpc> restColumns = new ArrayList<>();

        for (ColumnRpc rpc : columns) {
            if ("number".equals(rpc.getType()) && "percent".equals(rpc.getColumnFormat()) || "string".equals(rpc.getType()) || "date".equals(rpc.getType())) {
                restColumns.add(rpc);
            } else {
                SummaryColumns widgets = new SummaryColumns(tbody, rpc);
                widgets.setHandler(event -> {

                    view.getReport().getSumaries().clear();
                    for (SummaryColumns widgets1 : list) {
                        ColumnRpc rpc1 = widgets1.getColumnRpc();
                        if (rpc1 != null) {
                            view.getReport().getColumnMap().put(rpc1.getName(), rpc1);
                            view.getReport().getSumaries().add(rpc1);
                        }
                    }
                });
                list.add(widgets);
            }
        }

        for (ColumnRpc restColumn : restColumns) {
            SummaryColumns widgets = new SummaryColumns(tbody, restColumn);

            widgets.setHandler(
                    (event) -> {

                        view.getReport().getSumaries().clear();
                        for (SummaryColumns widgets1 : list) {
                            ColumnRpc rpc1 = widgets1.getColumnRpc();
                            if (rpc1 != null) {
                                view.getReport().getColumnMap().put(rpc1.getName(), rpc1);
                                view.getReport().getSumaries().add(rpc1);
                            }
                        }

                    });
            list.add(widgets);
        }
//        checkAll.addValueChangeHandler(valueChangeEvent -> {
//            for (SummaryColumns widgets : list) {
//                widgets.check(checkAll.isChecked());
//            }
//        });
    }

}