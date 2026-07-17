package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.finnetlimited.reportservice.core.client.ui.listbox.DRSListBox;
import com.finnetlimited.reportservice.core.client.ui.panel.DRSHorizontalPanel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: May 18, 2011
 * Time: 7:01:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class DrillDownReportAddRow extends VerticalPanel {

    private LinkedList<ColumnRpc> columns;
    private ReportRpc currentReport;
    private ArrayList<SelectListRpc> selectList;
    private Integer selectedReportId;
    private Integer row = 0;
    private ArrayList<SelectListRpc> reports;

    public DrillDownReportAddRow(LinkedList<ColumnRpc> columns, ReportRpc currentReport) {
        this.columns = columns;
        this.currentReport = currentReport;
    }

    public DrillDownReportAddRow(LinkedList<ColumnRpc> columns, ArrayList<SelectListRpc> reports, ReportRpc currentReport) {
        this.columns = columns;
        this.currentReport = currentReport;
        this.reports = reports;
    }

    public void init() {
        selectList = new ArrayList<>();
        if (columns != null) {
            for (int i = 0; i < columns.size(); i++) {
                SelectListRpc select = new SelectListRpc();
                select.setId(i);
                select.setName(columns.get(i).getTitle());
                select.setDescription(columns.get(i).getName());
                selectList.add(select);
            }
        }
        addRow();
    }

    public void addRow() {
        AddRow row = new AddRow();
        add(row);
    }

    private void removeWidget(Widget widget) {
        remove(widget);
    }

    public void setReports(ArrayList<SelectListRpc> reports) {
        this.reports = reports;
    }

    public void drawBySavedOptions(ReportRpc report) {
        for (int i = 1; i < report.getSelectedColumns().size(); i++) {
            ColumnRpc column = report.getSelectedColumns().get(i);
            if (column.getDrillDownReport()) {
                addRow();
            }
        }

        int index = 0;
        for (int i = 0; i < report.getSelectedColumns().size(); i++) {
            ColumnRpc column = report.getSelectedColumns().get(i);
            if (column.getDrillDownReport()) {
                AddRow row = (AddRow) getWidget(index);
                row.fillValues(column);
                index++;
            }
        }

    }

    public ReportRpc getReport(ReportRpc report) {

        for (int i = 0; i < report.getSelectedColumns().size(); i++) {
            report.getSelectedColumns().get(i).setDrillDownReport(false);
        }

        for (int i = 0; i < row; i++) {
            AddRow row = (AddRow) getWidget(i);
            String value = row.getValue();
            if (!value.equals("")) {
                String[] tokens = value.split("_");
                Integer columnIndex = Integer.parseInt(tokens[0]);
                ColumnRpc column = report.getSelectedColumns().get(columnIndex);

                column.setDrillDownReport(true);
                column.setLinkedReportId(Integer.parseInt(tokens[1]));
                column.setFilterParametr(Integer.parseInt(tokens[2]));
                report.getSelectedColumns().set(columnIndex, column);
            }
        }
        return report;
    }

    public class AddRow extends HorizontalPanel {

        private ColumnRpc column;

        private DRSListBox drlColumns;
        private DRSListBox drlReports;
        private DRSListBox drlParamaters;
        private Anchor remove;

        public AddRow() {
            init();
        }

        private void init() {
            drlColumns = new DRSListBox();
            drlColumns.setItemsNoNone(selectList);

            drlReports = new DRSListBox();
            drlReports.addChangeHandler(changeEvent -> setFilterParamsValue());

            if (reports != null && reports.size() > 0) {
                drlReports.setItems(reports);
            }

            drlParamaters = new DRSListBox();
            drlParamaters.setEnabled(false);

            remove = new Anchor("<span style='color:#ff7300;'>remove</span>", true);
            remove.setName(row + "");

            remove.addClickHandler(clickEvent -> {
                Anchor button = (Anchor) clickEvent.getSource();
                removeWidget(button.getParent());
                row--;
            });

            if (row != 0) {
                remove.setVisible(true);
            } else {
                remove.setVisible(false);
            }

            add(new DRSHorizontalPanel("Link Columns", drlColumns));
            add(new DRSHorizontalPanel("Link to Report", drlReports));
            add(new DRSHorizontalPanel("Report Parameters", drlParamaters));
            add(remove);
            row++;
        }

        private void fillValues(ColumnRpc column) {
            this.column = column;
            if (column != null) {
                for (int i = 0; i < drlColumns.getItemCount(); i++) {
                    if (drlColumns.getItemText(i).equals(column.getTitle())) {
                        drlColumns.setSelectedIndex(i);
                        break;
                    }
                }

                for (int i = 0; i < drlReports.getItemCount(); i++) {
                    if (drlReports.getValue(i).equals(column.getLinkedReportId().toString())) {
                        drlReports.setSelectedIndex(i);
                        break;
                    }
                }
                drlParamaters.setEnabled(true);
                setFilterParamsValue();
            }
        }

        public String getValue() {
            if (drlReports.getSelectedIndex() == 0 || drlParamaters.getSelectedIndex() == 0) {
                return "";
            }

            return drlColumns.getSelectedValue() + "_" + drlReports.getSelectedValue() + "_" + drlParamaters.getSelectedValue();
        }

        private void setFilterParamsValue() {
            selectedReportId = Integer.parseInt(drlReports.getValue(drlReports.getSelectedIndex()));
            CoreService.App.get().getReport(selectedReportId, drlReports.getSelectId(), new AsyncCallback<ReportRpc>() {
                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(ReportRpc reportRpc) {
                    drlParamaters.setEnabled(true);
                    drlParamaters.clear();

                    for (int i = 0; i < reportRpc.getPromtList().size(); i++) {
                        if (reportRpc.getPromtList().get(i).equals(1)) {
                            drlParamaters.addItem(reportRpc.getFieldd().get(i).getTitle(), Integer.toString(i));
                            if (column != null) {
                                if (i == column.getFilterParametr()) {
                                    drlParamaters.setSelectedIndex(i + 1);
                                }
                            }
                        }
                    }
                }
            });
        }
    }
}
