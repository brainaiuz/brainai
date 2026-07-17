/*
package com.finnetlimited.reportservice.core.client.ui.content.report.steps;

import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.gwtrpc.DashboardService;
import com.finnetlimited.reportservice.core.client.gwtrpc.DashletRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.body.AbstractStepReportBody;
import com.finnetlimited.reportservice.core.client.ui.button.DRSButton;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.finnetlimited.reportservice.core.client.ui.table.AddReportToDashboardTable;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.xml.RpcConvertToXml;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

*/
/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Jul 2, 2011
 * Time: 7:17:26 PM
 * To change this template use File | Settings | File Templates.
 *//*

public class AddReportToDashboard extends AbstractStepReportBody {

    private DialogBox alertPopup;
    private AddReportToDashboardTable table;
    //  private DRSSaveReportPopup savePopupPanel;

    public AddReportToDashboard(String name, ReportingModuleSettings settings) {
        super(name, settings);
        setEnabledSaveReportButton(true);
        setEnabledAddReportToDashboardButton(false);
        setEnabledNextReportButton(false);
    }


    @Override
    public Widget onInitialize() {
        setUpContent();
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());

            }
        });
    }

    private void setUpContent() {
        */
/* viewLabel = new HTML("");
           thema = new HTMLPanel("<h2 class='title' id='" + viewLabelId + "'><div style='float:left'>Add report to Dashboard -&nbsp;</div></h2>");
           thema.add(viewLabel, viewLabelId);
           addWidget(thema);
        *//*

        table = new AddReportToDashboardTable(this.reportingModuleSettings.getCustomDashboardId(), getReportingRpc().getId());
        //  savePopupPanel = new DRSSaveReportPopup("Save Custom Report", getXmlString());
        */
/* savePopupPanel.addSaveEvent(new DRSSaveReportPopup.SaveReportEvent() {
            public ReportRpc saveReportEvent() {
                return getReportingRpc();
            }
        });*//*

        addWidget(table);
    }

    private void setUpAlertPopup() {
        alertPopup = new DialogBox();
        alertPopup.setText("Close Report Page");
        alertPopup.setPixelSize(230, 140);
        alertPopup.setAnimationEnabled(true);
        alertPopup.setGlassEnabled(true);
        alertPopup.center();
        DRSButton close = new DRSButton("Close", DRSButton.BUTTON_STYLE);
        close.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                alertPopup.hide();
                //refreshAndGoTo(HistoryNamesType.FolderList.name());
                //goToClearAndCreateContent(HistoryNamesType.FolderList.name());
            }
        });

        DRSButton cancel = new DRSButton("Cancel", DRSButton.BUTTON_STYLE);
        cancel.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent clickEvent) {
                alertPopup.hide();
            }
        });


        //HTML infoMsg = new HTML("Are you closing report page");

        HorizontalPanel exportPanel = new HorizontalPanel();
        exportPanel.add(close);
        */
/*   buttonPanel.add(cancel);*//*

        int row = 0;
        FlexTable fTable = new FlexTable();
        fTable.setStyleName("order-table");
        fTable.setHTML(row, 0, "<b style='font-size:12px;color:#015d9f'>Dashlet succesfully created.</b>");
        fTable.getFlexCellFormatter().setAlignment(row++, 0, HorizontalPanel.ALIGN_LEFT, VerticalPanel.ALIGN_MIDDLE);
        fTable.setHTML(row++, 0, "&nbsp;");
        fTable.setWidget(row, 0, buttonPanel);
        fTable.getFlexCellFormatter().setAlignment(row, 0, HorizontalPanel.ALIGN_RIGHT, VerticalPanel.ALIGN_MIDDLE);
        alertPopup.add(fTable);
    }

    @Override
    public void next() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void back() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void save() {
        if (getReport() == null || getReport().getId() == null || getReport().getId() == 0) {
            saveReport();
        }
    }

    private void saveDashboard() {
        DashletRpc dashletRpc = table.getDashlet(getReport().getId());

        if (dashletRpc != null) {
            DashboardService.App.get().createDashlet(dashletRpc, new AsyncCallback<Integer>() {

                public void onFailure(Throwable throwable) {

                }

                public void onSuccess(Integer integer) {
                    setUpAlertPopup();
                    //  alertPopup.show();
                }
            });
        }
    }

    @Override
    public void addReportToDashboard() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void runReport() {
        goToClickStep(HistoryNamesType.ReportList.name());
    }

    @Override
    public void refreshChanges() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ReportRpc getReportingRpc() {
        return getReport();
    }

    @Override
    public void show() {
        DRSLoadingPanel.show();
    }

    @Override
    public void hide() {
        DRSLoadingPanel.hide();
    }

    public void saveReport() {

        */
/*   savePopupPanel.showPopup(getReport().getId());
        savePopupPanel.addCloseHandler(new CloseHandler<PopupPanel>() {
            @Override
            public void onClose(CloseEvent<PopupPanel> popupPanelCloseEvent) {
                if (savePopupPanel.getSavedReportId() != null && savePopupPanel.getSavedReportId() != 0)
                    getReport().setId(savePopupPanel.getSavedReportId());
                saveDashboard();
            }
        });*//*

    }

    public String getXmlString() {
        RpcConvertToXml rpcToXml = new RpcConvertToXml(getReport());
        return rpcToXml.generate();
    }
}
*/
