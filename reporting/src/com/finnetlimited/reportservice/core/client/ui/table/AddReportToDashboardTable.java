/*
package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.finnetlimited.reportservice.core.client.gwtrpc.DashboardRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.DashboardService;
import com.finnetlimited.reportservice.core.client.gwtrpc.DashletRpc;
import com.finnetlimited.reportservice.core.client.ui.button.DRSButton;
import com.finnetlimited.reportservice.core.client.ui.element.UlListPanel;
import com.finnetlimited.reportservice.core.client.ui.listbox.DRSListBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.List;

*/
/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Jul 2, 2011
 * Time: 7:43:03 PM
 * To change this template use File | Settings | File Templates.
 *//*

public class AddReportToDashboardTable extends FlowPanel {

    private int topVerticalPosition = 1;
    private int bottomVerticalPosition = 999;

    private List<DashboardRpc> dashboards;
    private DashboardRpc selectedDashboard;
    private Integer selectedDashboardId;
    private Integer reportId;

    private HTML error;
    protected DRSListBox drlDashboards;
    protected RadioButton rbtnDataTableOnly;
    protected RadioButton rbtnChartOnly;
    protected RadioButton rbtnDataTableAndChart;
    protected DRSListBox drlColumns;
    protected RadioButton rbtnFirst;
    protected RadioButton rbtnAtPosition;
    protected TextBox txtPosition;
    protected RadioButton rbtnBottom;
    protected DRSButton btnSave;
    protected DRSButton btnClose;

    public AddReportToDashboardTable(Integer customDashboardId, Integer reportId) {
        selectedDashboardId = customDashboardId;
        this.reportId = reportId;
        init();
    }

    private void init() {
        error = new HTML();
        if (selectedDashboardId == null || selectedDashboardId == 0) {
            drlDashboards = new DRSListBox();
            drlDashboards.setWidth("200px");
        }
        rbtnDataTableOnly = new RadioButton("showas", "Datatable only");
        rbtnChartOnly = new RadioButton("showas", "Chart only");
        rbtnChartOnly.setValue(true);
        rbtnDataTableAndChart = new RadioButton("showas", "Datatable / chart");

        drlColumns = new DRSListBox();
        drlColumns.setWidth("200px");
        rbtnFirst = new RadioButton("verticalposition", "First(on top");
        rbtnFirst.setValue(true);
        rbtnAtPosition = new RadioButton("verticalposition", "At position");
        txtPosition = new TextBox();
        txtPosition.setWidth("200px");
        txtPosition.setEnabled(false);
        txtPosition.setValue("2");
        rbtnBottom = new RadioButton("verticalposition", "Last(at the bottom)");

        */
/*  btnSave = new DRSButton("Save Report", DRSButton.BUTTON_STYLE);
    btnClose = new DRSButton("Close", DRSButton.BUTTON_STYLE);*//*


        int row = 0;
        FlexTable popupBody = new FlexTable();
        popupBody.setStyleName("order-table");
        popupBody.setCellPadding(5);
        popupBody.setCellSpacing(5);


        popupBody.setHTML(row, 0, "<b style='font-size:16px;color:#015d9f;'>Add report to dashboard<b>");
        popupBody.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
        popupBody.getFlexCellFormatter().setColSpan(row++, 0, 2);


        popupBody.setWidget(row, 0, error);
        popupBody.getFlexCellFormatter().setColSpan(row, 0, 2);
        popupBody.getFlexCellFormatter().setAlignment(row++, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
        if (selectedDashboardId == null || selectedDashboardId == 0) {
            popupBody.setHTML(row, 0, "<b style='color:#015d9f;'>Dashboard<span style='color:red;'>*</span>:</b>");
            popupBody.setWidget(row++, 1, drlDashboards);
        }

        popupBody.setHTML(row, 0, "<b style='color:#015d9f;'>Show Dashlet As<span style='color:red;'>*</span>:</b>");
        popupBody.getFlexCellFormatter().setRowSpan(row, 0, 3);
        popupBody.setWidget(row++, 1, rbtnDataTableOnly);
        popupBody.setWidget(row++, 0, rbtnChartOnly);
        popupBody.setWidget(row++, 0, rbtnDataTableAndChart);

        UlListPanel listpanel = new UlListPanel();
        listpanel.addStyleName("dataSheetView");
        listpanel.add(rbtnDataTableOnly, "viewDataTable");
        listpanel.add(rbtnChartOnly, "viewChart");
        listpanel.add(rbtnDataTableAndChart, "viewData-Chart");


        if (selectedDashboardId == null || selectedDashboardId == 0) {
            popupBody.setHTML(row, 0, "<b style='color:#015d9f;'>Show on the<span style='color:red;'>*</span>:</b>");
            popupBody.setWidget(row++, 1, drlColumns);
            popupBody.setHTML(row, 0, "<b style='color:#015d9f;'> Vertical Position<span style='color:red;'>*</span>:</b>");
            popupBody.getFlexCellFormatter().setRowSpan(row, 0, 3);
            popupBody.setWidget(row++, 1, rbtnFirst);
            HorizontalPanel atPositionpaPanel = new HorizontalPanel();
            atPositionpaPanel.add(rbtnAtPosition);
            atPositionpaPanel.add(txtPosition);
            popupBody.setWidget(row++, 0, atPositionpaPanel);
            popupBody.setWidget(row++, 0, rbtnBottom);
        }
        */
/*  HorizontalPanel savePanel = new HorizontalPanel();
                savePanel.add(btnSave);
                savePanel.setCellVerticalAlignment(btnSave, VerticalPanel.ALIGN_MIDDLE);
                savePanel.add(btnClose);
                savePanel.setCellVerticalAlignment(btnClose, VerticalPanel.ALIGN_MIDDLE);

                popupBody.setWidget(row, 0, savePanel);
                popupBody.getFlexCellFormatter().setColSpan(row, 0, 2);
                popupBody.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_RIGHT, VerticalPanel.ALIGN_MIDDLE);
        *//*

        add(popupBody);
        add(listpanel);
        refreshListboxs();

        rbtnFirst.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                txtPosition.setEnabled(false);
            }
        });

        rbtnAtPosition.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent clickEvent) {
                txtPosition.setEnabled(true);
            }
        });

        rbtnBottom.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent clickEvent) {
                txtPosition.setEnabled(false);
            }
        });
    }

    private void renderColumns() {

        if (selectedDashboardId == null || selectedDashboardId == 0) {
            for (DashboardRpc dashboard : dashboards) {
                if (dashboard.getId().toString().equals(drlDashboards.getValue(drlDashboards.getSelectedIndex()))) {
                    selectedDashboard = dashboard;
                }
            }
        }
        drlColumns.clear();
        if (selectedDashboard != null) {
            if (selectedDashboard.getColumnCount() == 1) {
                drlColumns.addItem("Center (" + selectedDashboard.getCenterColumnTitle() + ")", "1");
            } else if (selectedDashboard.getColumnCount() == 2) {
                drlColumns.addItem("Left (" + selectedDashboard.getLeftColumnTitle() + ")", "1");
                drlColumns.addItem("Right (" + selectedDashboard.getRightColumnTitle() + ")", "2");
            } else {
                drlColumns.addItem("Left (" + selectedDashboard.getLeftColumnTitle() + ")", "1");
                drlColumns.addItem("Center (" + selectedDashboard.getCenterColumnTitle() + ")", "2");
                drlColumns.addItem("Right (" + selectedDashboard.getRightColumnTitle() + ")", "3");
            }
        }
    }

    public DashletRpc getDashlet(Integer reportingId) {
        if (validate(reportingId)) {
            DashletRpc dashletRpc = new DashletRpc();
            dashletRpc.setDashboard(selectedDashboard);
            dashletRpc.setReportId(reportId);

            if (rbtnDataTableOnly.getValue()) {
                dashletRpc.setDashletType(0);
            } else if (rbtnChartOnly.getValue()) {
                dashletRpc.setDashletType(1);
            } else {
                dashletRpc.setDashletType(2);
            }
            if (selectedDashboardId == null || selectedDashboardId == 0) {
                dashletRpc.setColumnIndex(0);
                dashletRpc.setVerticalPosition(++bottomVerticalPosition);
                */
/*dashletRpc.setColumnIndex(Integer.parseInt(drlColumns.getValue(drlColumns.getSelectedIndex())));
                if (rbtnFirst.getValue()) {
                    dashletRpc.setVerticalPosition(--topVerticalPosition);
                } else if (rbtnBottom.getValue()) {
                    dashletRpc.setVerticalPosition(++bottomVerticalPosition);
                } else {
                    dashletRpc.setVerticalPosition(Integer.parseInt(txtPosition.getValue()));
                }*//*

            }
            return dashletRpc;
        }
        return null;
    }

    public void refreshListboxs() {
        if (selectedDashboardId == null || selectedDashboardId == 0) {
            drlDashboards.clear();
            DashboardService.App.get().getDashboardList(new ListingFilterParameter(), new AsyncCallback<ListResult<DashboardRpc>>() {

                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void onSuccess(ListResult<DashboardRpc> dashboardRpcs) {

                    dashboards = dashboardRpcs.getList();

                    for (DashboardRpc dashboard : dashboardRpcs.getList()) {
                        drlDashboards.addItem(dashboard.getName(), dashboard.getId().toString());
                    }

                    drlDashboards.addValueChangeHandler(new ChangeHandler() {
                        public void onChange(ChangeEvent changeEvent) {
                            renderColumns();
                        }
                    });
                    renderColumns();
                }
            });

        } else {
            DashboardService.App.get().getDashboard(selectedDashboardId, new AsyncCallback<DashboardRpc>() {
                @Override
                public void onFailure(Throwable throwable) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                @Override
                public void onSuccess(DashboardRpc dashboardRpc) {
                    selectedDashboard = dashboardRpc;
                    renderColumns();
                }
            });
        }
    }

    private boolean validate(Integer reportingId) {

        */
/* if (reportingId == null && this.reportId == null) {

            error.setHTML("<b style='color:red;'>This report have not created. Please save report before to add to dashboard.</b>");
            return false;
        }*//*

        this.reportId = reportingId;
        int errorCount = 0;
        error.setHTML("");
        if (selectedDashboardId == null || selectedDashboardId == 0) {
            if (drlDashboards.getSelectedIndex() == 0) {
                errorCount++;
            }

            if (drlColumns.getSelectedIndex() == 0) {
                errorCount++;
            }
        }
        if (errorCount > 0) {
            error.setHTML("<b style='color:red;'>Please enter required fields</b>");
            return false;
        }
        return true;
    }

}
*/
