package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget;

import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.AsyncWidget;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.DateRangeType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SortType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.SqlColumnType;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ViewType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.ReportingStepControlView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Div;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by Virus on 8/30/14.
 */
public class ReportingGrouping extends AsyncWidget {

    interface ReportingGroupingUiBinder extends UiBinder<HTMLPanel, ReportingGrouping> {
    }

    private static ReportingGroupingUiBinder ourUiBinder = GWT.create(ReportingGroupingUiBinder.class);
    @UiField
    DataListBox group11;
    @UiField
    DataListBox group12;
    @UiField
    DataListBox group13;
    @UiField
    DataListBox group21;
    @UiField
    DataListBox group22;
    @UiField
    DataListBox group23;
    @UiField
    DataListBox group31;
    @UiField
    DataListBox group32;
    @UiField
    DataListBox group33;
    @UiField
    Div summarizeByLabel;
    @UiField
    Div andThenByLabel2;
    @UiField
    Div andThenByLabel1;

    private ReportingStepControlView view;

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    public ReportingGrouping() {
        super(null, "customReport_tab_2");
    }

    private void setSortTypesOrViewType(DataListBox group1, DataListBox group2, DataListBox group3, int i) {
        ReportRpc reportRpc = view.getReport();
        if (reportRpc.getSortTypes().size() > i) {
            reportRpc.getSortTypes().set(i, group2.getSelectedItem(true).getName());
        } else {
            reportRpc.getSortTypes().add(group2.getSelectedItem(true).getName());
        }
        if (reportRpc.getViewTypes().size() > i) {
            reportRpc.getViewTypes().set(i, group3.getSelectedItem(true).getName());
        } else {
            reportRpc.getViewTypes().add(group3.getSelectedItem(true).getName());
        }
        ColumnRpc columnRpc = view.getReport().getColumnMap().get(group1.getSelectedItem(true).getDescription());
        if (columnRpc != null && SqlColumnType.DATE.getName().equals(columnRpc.getType())) {
            if (reportRpc.getRangeType().size() > i) {
                view.getReport().getRangeType().set(i, group2.getSelectedItem(true).getName());
            } else {
                view.getReport().getRangeType().add(group2.getSelectedItem(true).getName());
            }
        } else {
            if (reportRpc.getRangeType().size() > i) {
                view.getReport().getRangeType().set(i, "");
            } else {
                view.getReport().getRangeType().add("");
            }

        }
    }

    private void setGroupColumn(DataListBox group, DataListBox group2, DataListBox group3, int widgetId) {

        ColumnRpc columnRpc = view.getReport().getColumnMap().get(group.getSelectedItem(true).getDescription());
        if (SqlColumnType.DATE.getName().equals(columnRpc.getType())) {
            group2.setItems(DateRangeType.getAsSelectItems());
        } else {
            group2.setItems(SortType.getAsSelectItems());
        }
        group3.setItems(ViewType.getAsSelectItems());

        int i = 0;// 1 ta column ni 2 marta tanlamasligi uchun
        for (ColumnRpc rpc : view.getReport().getGroupColumns()) {
            if (rpc.getName().equals(group.getSelectedItem().getDescription())
                    && widgetId != i) {
                ColumnRpc item = view.getReport().getGroupColumns().get(widgetId);
                view.getReport().getGroupColumns().set(i, item);
                if (i == 0) {
                    group11.setSelectedByDescription(item.getName());
                } else {
                    if (i == 1) {
                        group21.setSelectedByDescription(item.getName());
                    } else {
                        if (i == 2) {
                            group31.setSelectedByDescription(item.getName());
                        }
                    }
                }
            }
            i++;
        }// set Data
        if (view.getReport().getGroupColumns().size() > widgetId) {
            view.getReport().getGroupColumns().set(widgetId,
                    view.getReport().getColumnMap().get(
                            group.getSelectedItem().getDescription()
                    ));
        } else {
            view.getReport().getGroupColumns().add(
                    view.getReport().getColumnMap().get(
                            group.getSelectedItem().getDescription()
                    ));
        }
        setSortTypesOrViewType(group, group2, group3, widgetId);
    }

    private void removeGroupColumn(int widgetId) {// 1 ta group ni o`chirganda undan pastdagilari ham o`chishi kerak
        for (int i = view.getReport().getGroupColumns().size() - 1; view.getReport().getGroupColumns().size() > widgetId; i--) {
            view.getReport().getGroupColumns().remove(i);
        }
    }

    protected Widget onInitialize() {
        LoadingPanel.loading(true);
        add(ourUiBinder.createAndBindUi(this));
        summarizeByLabel.setWidth("200px");
        andThenByLabel1.setWidth("200px");
        andThenByLabel2.setWidth("200px");
        summarizeByLabel.getElement().setInnerHTML(wfmStrings.summarizeInformationBy());
        andThenByLabel1.getElement().setInnerHTML(wfmStrings.andThenBy());
        andThenByLabel2.getElement().setInnerHTML(wfmStrings.andFinallyBy());
        loading();
        group11.addValueChangeHandler(event -> {
            boolean b = group11.getSelectedItem() != null;
            if (b) {
                setGroupColumn(group11, group12, group13, 0);
                group21.setItems(columnListBoxLoad(view.getReport().getGroupColumns().get(0).getName()));
            } else {
                removeGroupColumn(0);
                group21.setSelectedIndex(0);
                group31.setSelectedIndex(0);
                group31.setEnabled(b);
                group32.setEnabled(b);
                group33.setEnabled(b);
            }
            group21.setEnabled(b);
            group22.setEnabled(b);
            group23.setEnabled(b);
            view.includeGroupingChanges();
        });
        group21.addValueChangeHandler(event -> {
            boolean b = group21.getSelectedItem() != null;
            if (b) {
                setGroupColumn(group21, group22, group23, 1);
                group31.setItems(columnListBoxLoad(view.getReport().getGroupColumns().get(0).getName()
                        , view.getReport().getGroupColumns().get(1).getName()));
            } else {
                removeGroupColumn(1);
                group31.setSelectedIndex(0);
            }
            group31.setEnabled(b);
            group32.setEnabled(b);
            group33.setEnabled(b);
            view.includeGroupingChanges();
        });
        group31.addValueChangeHandler(event -> {
            boolean b = group31.getSelectedItem() != null;
            if (b) {
                setGroupColumn(group31, group32, group33, 2);
            } else {
                removeGroupColumn(2);
            }
            view.includeGroupingChanges();
        });
        group12.addValueChangeHandler(event -> setSortTypesOrViewType(group11, group12, group13, 0));
        group22.addValueChangeHandler(event -> setSortTypesOrViewType(group21, group22, group23, 1));
        group32.addValueChangeHandler(event -> setSortTypesOrViewType(group31, group32, group33, 2));

        group13.addValueChangeHandler(event -> setSortTypesOrViewType(group11, group12, group13, 0));
        group23.addValueChangeHandler(event -> setSortTypesOrViewType(group21, group22, group23, 1));
        group33.addValueChangeHandler(event -> setSortTypesOrViewType(group31, group32, group33, 2));
        LoadingPanel.loading(false);
        return null;
    }

    private void loading() {
        group21.setEnabled(false);
        group31.setEnabled(false);
        group22.setEnabled(false);
        group32.setEnabled(false);
        group23.setEnabled(false);
        group33.setEnabled(false);

        group12.setWithoutNullLabel(true);
        group22.setWithoutNullLabel(true);
        group32.setWithoutNullLabel(true);
        group13.setWithoutNullLabel(true);
        group23.setWithoutNullLabel(true);
        group33.setWithoutNullLabel(true);
        group12.setItems(SortType.getAsSelectItems());
        group22.setItems(SortType.getAsSelectItems());
        group32.setItems(SortType.getAsSelectItems());
        group13.setItems(ViewType.getAsSelectItems());
        group23.setItems(ViewType.getAsSelectItems());
        group33.setItems(ViewType.getAsSelectItems());

        group11.setItems(columnListBoxLoad());

        for (int i = 0; i < view.getReport().getViewTypes().size(); i++) {
            SelectItem selectedItem = new SelectItem();
            selectedItem.setName(view.getReport().getViewTypes().get(i));
            if (i == 0) {
                group13.setSelected(selectedItem);
            } else if (i == 1) {
                group23.setSelected(selectedItem);
            } else if (i == 2) {
                group33.setSelected(selectedItem);
            }
        }
        if (view.getReport().getGroupColumns().size() > 0) {
            group11.setSelectedByDescription(view.getReport().getGroupColumns().get(0).getName());
            group21.setItems(columnListBoxLoad(view.getReport().getGroupColumns().get(0).getName()));
            group21.setEnabled(true);
            group22.setEnabled(true);
            group23.setEnabled(true);
        } else {
            group21.setItems(new SelectItem[0]);
        }

        if (view.getReport().getGroupColumns().size() > 1) {
            group21.setSelectedByDescription(view.getReport().getGroupColumns().get(1).getName());
            group31.setItems(columnListBoxLoad(view.getReport().getGroupColumns().get(0).getName(),
                    view.getReport().getGroupColumns().get(1).getName()));
            group31.setEnabled(true);
            group32.setEnabled(true);
            group33.setEnabled(true);
        } else {
            group31.setItems(new SelectItem[0]);
        }
        if (view.getReport().getGroupColumns().size() > 2) {
            group31.setSelectedByDescription(view.getReport().getGroupColumns().get(2).getName());
        }
    }

    private SelectItem[] columnListBoxLoad(String... selectedText) {//yuqorida tanlangan column pastda chiqmasin
        LinkedList<SelectItem> list = new LinkedList<>();
        int x = 0;
        for (ColumnRpc rpc : view.getReport().getColumnMap().values()) {
            if (selectedText == null || !Arrays.asList(selectedText).contains(rpc.getName())) {
                list.add(new SelectItem(x++, rpc.getTitle(), rpc.getName()));
            }
        }
        Collections.sort(list, new Comparator<SelectItem>() {
            @Override
            public int compare(SelectItem o1, SelectItem o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return list.toArray(new SelectItem[list.size()]);
    }

    public void setView(ReportingStepControlView view) {
        this.view = view;
    }
}