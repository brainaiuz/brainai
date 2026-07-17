package com.finnetlimited.reportservice.core.client.ui.content.report.steps;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.xml.RpcConvertToXml;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.ExportFormPanel;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.MailingListPopup;
import com.finnetlimited.reportservice.core.client.enumtype.HistoryNamesType;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.module.ReportingModuleSettings;
import com.finnetlimited.reportservice.core.client.ui.body.AbstractStepReportBody;
import com.finnetlimited.reportservice.core.client.ui.listbox.DRSComboBox;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.finnetlimited.reportservice.core.client.ui.panel.DRSSaveReportPopup;
import com.finnetlimited.reportservice.core.client.ui.panel.DRSSaveReportSchedulePopup;
import com.finnetlimited.reportservice.core.client.ui.table.FilterListing;
import com.finnetlimited.reportservice.core.client.ui.table.FilterTable;
import com.finnetlimited.reportservice.core.client.ui.table.ReportTable;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

//import com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.MailingListPopup;


/**
 * User: ${Dilsh0d}
 * Date: 13-Mar-2010
 * Time: 19:37:03
 */
public class ReportListBody extends AbstractStepReportBody {

    private static final String viewLabelId = "viewLabelList";

    private HTML viewLabel;
    private HTMLPanel thema;
    private FilterTable filterTable;
    //    private DRSListBox roles;
    private ReportTable reportTable;
    private DRSSaveReportPopup savePopupPanel;
    //    private Boolean folderType;
    private FlowPanel pnlChartTopOfReport;
    private FlowPanel pnlChartBottomOfReport;
    private FilterListing filterListing;
//    private ArrayList<ColumnRpc> columnRpcs;

    private ExportFormPanel csv;
    private ExportFormPanel xls;
    private ExportFormPanel pdf;
    private DRSComboBox otherReports;
    private ActionButton filterButton;
    private ActionButton scheduleReport;
    private ActionButton mailingButton;
    private ActionButton showDetailsButton;
//    private ReportListPanel reportListPanel;


    public ReportListBody(String name, ReportingModuleSettings settings) {
        super(name, settings);
        setEnabledNextReportButton(false);
        setEnabledSaveReportButton(true);
        setEnabledBackReportButton(true);
    }

    @Override
    public Widget onInitialize() {

        setUptContent();
        getReportList();
        return null;
    }

    private void getReportList() {
        DRSLoadingPanel.show();
        CoreService.App.get().getReportStructure(getReportRpc(), null, new AsyncCallback<ReportRpc>() {
            @Override
            public void onFailure(Throwable caught) {
                DRSLoadingPanel.hide();
                GWT.log(caught.getMessage());
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.error());
                messageBox.setMessage(reportingStrings.errorOcuredPleaseTryAgainOrRefreshYouBrouzer());
                messageBox.addCloseHandler(new com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler() {
                    @Override
                    public void onSubmit() {
                        Window.Location.reload();
                    }
                });
                messageBox.open();
            }

            @Override
            public void onSuccess(ReportRpc result) {
                setReport(result);
                savePopupPanel = new DRSSaveReportPopup(reportingStrings.saveCustomReport(), getXmlString());
                savePopupPanel.addSaveEvent(() -> getReportRpc());


                if (result != null) {
                    viewLabel.setText(result.getViewName());
                }
                getReportRpc().setFilterOptionChanged(false);
                checkReportTypeSetColumnGroupingWithoutClear();
                if (filterListing != null) {
                    filterListing.getLimit().setEnabled(!ReportType.SUMMARY.name().equals((getReport().getTableType())));
                    filterListing.getUserFilter().drawSaveReportCriteriya(getReport());
                }
                if (ReportType.TABULAR.name().equals(getReport().getTableType())) {
                    xls.setAction("common/reportExcel");
                    showDetailsButton.setVisible(false);
                } else {
                    showDetailsButton.setVisible(true);
                    if (getReport().getIsDetailed()) {
                        showDetailsButton.setText(reportingStrings.hideDetails());
                    } else {
                        showDetailsButton.setText(wfmStrings.showDetails());
                    }

                }
                reportTable.drawReportTable(getReport());

                if (filterListing != null) {
                    filterListing.close();
                }

                if (getReport().getPromtList() != null && getReport().getPromtList().size() > 0) {
                    filterButton.removeStyleName("markFilter");
                    filterButton.addStyleName("markfilter-act");
                } else {
                    filterButton.removeStyleName("markfilter-act");
                    filterButton.addStyleName("markFilter");
                }

                if (scheduleReport != null) {
                    if (getReport().getRecurrenceJobItem() != null && getReport().getRecurrenceJobItem().getEmployees() != null) {
                        scheduleReport.removeStyleName("markShare-unact");
                        scheduleReport.addStyleName("markShare");
                    } else {
                        scheduleReport.removeStyleName("markShare");
                        scheduleReport.addStyleName("markShare-unact");
                    }
                }
//                DRSLoadingPanel.hide();
            }
        });
    }

    private void setUpFilterListing() {
        filterListing = new FilterListing(getReport());
        filterListing.getApplySubmit().addClickHandler(event -> {
            checkReportTypeSetColumnGroupingWithoutClear();
            filterListing.getApplySubmit().setVisible(false);
            filterListing.getLoader().setVisible(true);
            setReport(filterListing.getUserFilter().getReportRpc(getReport()));
            getReport().setPosition(1);
            getReportList();
        });
        filterListing.getResetChanges().addClickHandler(event -> {
            setReport(filterListing.clearFilter(getReport()));
            getReportList();

        });
        filterListing.getLimit().addChangeHandler(event -> {
            String limitType = filterListing.getLimit().getValue(filterListing.getLimit().getSelectedIndex());

            if (!"all".equals(limitType)) {
                getReportRpc().setLimit(Integer.parseInt(limitType));
            } else {
                getReportRpc().setLimit(-1);
            }
        });
        filterListing.getUserFilter().drawSaveReportCriteriya(getReport());
    }

    public void checkReportTypeSetColumnGroupingWithoutClear() {
        if (getReport().getTableType().equals(ReportType.SUMMARY.name())) {
            getReport().setModified(true);
            //  getReport().setPosition(1);
            getStepPanel().insertStep(reportingStrings.selectGrouping(), HistoryNamesType.AddGroupingReport.name(), 1);
            getStepPanel().getElement().getStyle().setWidth(920, Style.Unit.PX);
        } else {
            getReport().setModified(true);
            //   getReport().setPosition(1);
            getReport().setGroupColumns(new LinkedList<>());
            getStepPanel().removeStep(HistoryNamesType.AddGroupingReport.name());
            getStepPanel().getElement().getStyle().setWidth(781, Style.Unit.PX);
        }
    }

    private void setUptContent() {
        filterTable = new FilterTable();
        filterTable.setWidth("646px");
//        roles = new DRSListBox();

        addWidget(filterTable);
        xls = new ExportFormPanel("XLS", "operPanel markExcel", "common/reportExcel");
        csv = new ExportFormPanel("CSV", "operPanel markCSV", "common/reportCsv");

        csv.addClickEvent(() -> {
            csv.setParam(getXmlString());
            csv.submit();
        });
        xls.addClickEvent(() -> {
            xls.setParam(getXmlString());
            xls.submit();
        });

        pdf = new ExportFormPanel("PDF", "operPanel markPDF", "common/runtimeReportPdf");
        pdf.addClickEvent(() -> {
            pdf.setParam(getXmlString());
            pdf.submit();
        });
        getHeaderPanel();

        viewLabel = new HTML(getReport().getViewName() + "&nbsp;");
        viewLabel.getElement().setAttribute("style", "padding-top:5px;font-size:13px");
        thema = new HTMLPanel("<div style='margin-top:5px; font-size:16px' id='" + viewLabelId + "'><div style='float:left;font-weight:bold; padding-top:5px; font-size:13px;'>" + wfmStrings.myFavouriteReports() + "-&nbsp;</div></div>");
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.setStyleName("left lineBtnSet");
        if (getReport().getId() != null && getReport().getId() > 0) {
            horizontalPanel.add(otherReports = new DRSComboBox(new ArrayList<>(), wfmStrings.pleaseWait()));
            otherReports.getElement().setAttribute("style", "width:220px;height:26px;margin-right:5px;");
            this.fillOtherReports(getReport().getId());
        } else {
            horizontalPanel.add(viewLabel);
        }

        thema.add(horizontalPanel, viewLabelId);
        thema.setVisible(reportingModuleSettings.getShowSteps());
        addWidget(thema);
        if (getReport().getId() != null && getReport().getId() > 0) {
            final FlowPanel starPanel = new FlowPanel();
            final ActionButton starLink = new ActionButton("Favourite");
            starLink.setStyleName("optBtn sameIcon");
            starLink.getElement().setAttribute("padding-left", "10px;");
            final Image starImage = new Image("/images/ajax-loader.gif");
            setStarStatus(starLink);
            starImage.setWidth("16px");
            starImage.setVisible(false);
            starPanel.add(starLink);
            starPanel.add(starImage);
            starLink.addClickHandler(event -> {
                starLink.setText(wfmStrings.pleaseWait());

                CoreService.App.get().createFavouriteReportTemplate(getReport().getId(), new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        starLink.setText(reportingStrings.favourite());
                        Window.alert(reportingStrings.errorOcuredPleaseTryAgain());
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            starLink.removeStyleName("keyOptArrowStar");
                            starLink.addStyleName("keyOptArrowStar-act");
                        } else {
                            starLink.removeStyleName("keyOptArrowStar-act");
                            starLink.addStyleName("keyOptArrowStar");
                        }
                        starLink.setText(reportingStrings.favourite());
                    }
                });
            });
            horizontalPanel.add(starPanel);
        }

        if (getReport().getId() != null && getReport().getId() > 0) {
            scheduleReport = new ActionButton(wfmStrings.share());
            scheduleReport.setStyleName("optBtn markShare sameIcon");
            scheduleReport.setTitle(reportingStrings.shareResults());
            final DRSSaveReportSchedulePopup mysavePopupPanel = new DRSSaveReportSchedulePopup(reportingStrings.scheduleReport(), getXmlString());
            final Command changeModifyStatus = () -> getReport().setModified(false);

            mysavePopupPanel.setCommand(changeModifyStatus);
            mysavePopupPanel.addSaveEvent(() -> getReport());
            scheduleReport.addClickHandler(event -> {
                SelectItem emailTemplateItem = getReport().getEmailTemplateItem();
                mysavePopupPanel.showPopup(getReport().getId(), (emailTemplateItem != null ? emailTemplateItem.getId() : null), true);

            });
            horizontalPanel.add(scheduleReport);
        }


        filterButton = new ActionButton(wfmStrings.filter());
        filterButton.addStyleName("optBtn markFilter sameIcon");


        filterButton.addClickHandler(event -> {

            if (filterListing == null) {
                setUpFilterListing();
            }

            if (filterListing.getApplySubmit() != null) {
                filterListing.getApplySubmit().setVisible(true);
            }
            if (filterListing.getLoader() != null) {
                filterListing.getLoader().setVisible(false);
            }
            filterListing.center();
        });
        horizontalPanel.add(filterButton);

        showDetailsButton = new ActionButton(reportingStrings.hideDetails());
        showDetailsButton.addStyleName("optBtn sameIcon");
        showDetailsButton.setVisible(false);
        showDetailsButton.addClickHandler(event -> {
            getReport().setIsDetailed(!getReport().getIsDetailed());
            if (getReport().getIsDetailed()) {
                showDetailsButton.setText(reportingStrings.hideDetails());
            } else {
                showDetailsButton.setText(wfmStrings.showDetails());
            }
            getReportList();
        });
        horizontalPanel.add(showDetailsButton);

        String contacts = " CRM LEADS | CRM CONTACTS | CUSTOM CONTACT REPORT ";
        if (contacts.contains(" " + getReport().getViewCode() + " ")) {
            mailingButton = new ActionButton(wfmStrings.mailingList());
            mailingButton.addStyleName("optBtn mailing-list sameIcon");
            final MailingListPopup mailingList = new MailingListPopup(getReport());
            mailingButton.addClickHandler(event -> {
                mailingList.setReport(getReport());
                if (!mailingList.isInit()) {
                    mailingList.init(getReport().getViewCode());
                }
                mailingList.center();
            });
            horizontalPanel.add(mailingButton);
        }

        FlowPanel wrapperPanel = null;
        final DisclosurePanel panel = new DisclosurePanel();
//        if (CompanyConstants.C30871.equals(Utils.getEncryptedCompanyID())) {
//            wrapperPanel = new FlowPanel();
//            panel.setOpen(true);
//            panel.setWidth("100%");
//            panel.getElement().getStyle().setProperty("clear", "both");
//            panel.setAnimationEnabled(true);
//            panel.addEventHandler(new DisclosureHandler() {
//                @Override
//                public void onClose(DisclosureEvent event) {
//                    panel.getElement().getElementsByTagName("em").getItem(0).removeClassName("colapse");
//                    panel.getElement().getElementsByTagName("em").getItem(0).addClassName("expand");
//                }
//
//                @Override
//                public void onOpen(DisclosureEvent event) {
//                    panel.getElement().getElementsByTagName("em").getItem(0).removeClassName("expand");
//                    panel.getElement().getElementsByTagName("em").getItem(0).addClassName("colapse");
//                }
//            });
//            panel.setHeader(createHeader());
//        }
        reportingModuleSettings.setActivePagers("bottom");
        reportTable = new ReportTable(reportingModuleSettings);
        reportTable.addPagingEvent((beganPositon, step) -> {
            getReport().setLimit(step);
            getReport().setPosition(beganPositon);
            getReportList();
        });
        reportTable.setCommand(() -> {
            if (getReport().getSortTableByColumnType() == null || "".equals(getReport().getSortTableByColumnType()) || "DESC".equals(getReport().getSortTableByColumnType())) {
                getReport().setSortTableByColumnType("ASC");
            } else {
                getReport().setSortTableByColumnType("DESC");
            }
            getReport().setSortTableByColumn(reportTable.getSortByColumn());
            runReport();
        });

        if (CompanyConstants.C30871.equals(Utils.getEncryptedCompanyID())) {
            panel.add(reportTable);
            wrapperPanel.add(panel);
            addWidget(wrapperPanel);
        }

        else {
            addWidget(reportTable);
        }

        pnlChartTopOfReport = new FlowPanel();
        addWidget(pnlChartTopOfReport);

        pnlChartBottomOfReport = new FlowPanel();
        addWidget(pnlChartBottomOfReport);
    }

//    private Widget createHeader() {
//        FlowPanel headerPanel = new FlowPanel();
//        headerPanel.addStyleName("header-massdata");
//        headerPanel.setWidth("100%");
//        HTML headerText = new HTML();
//        headerText.setHTML("<em class='colapse'></em>");
//        headerText.setStyleName("cw-StackPanelHeader");
//        headerPanel.add(headerText);
//
//        return headerPanel;
//    }

    private void getHeaderPanel() {

        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.setVerticalAlignment(VerticalPanel.ALIGN_MIDDLE);
        hPanel.setWidth("auto");
        HTML exportText = new HTML("<b class='exportas' style='font-size:13px;' >" + wfmStrings.exportAs() + ": </b>");
        hPanel.add(exportText);
        hPanel.add(csv);
        hPanel.add(new HTML("<span style='padding:0 5px 0 5px;font-size:13px;'>|</span>"));
        hPanel.add(xls);
        hPanel.add(new HTML("<span style='padding:0 5px 0 5px;font-size:13px;'>|</span>"));
        hPanel.add(pdf);

        HorizontalPanel horzPanel = new HorizontalPanel();
        horzPanel.add(hPanel);
        horzPanel.setCellHorizontalAlignment(hPanel, HasHorizontalAlignment.ALIGN_LEFT);
        horzPanel.setCellVerticalAlignment(hPanel, HasVerticalAlignment.ALIGN_MIDDLE);
        HorizontalPanel limitPanel = new HorizontalPanel();
        horzPanel.add(limitPanel);
        horzPanel.setCellHorizontalAlignment(limitPanel, HasHorizontalAlignment.ALIGN_RIGHT);
        horzPanel.setCellVerticalAlignment(limitPanel, HasVerticalAlignment.ALIGN_MIDDLE);
        FlowPanel expertPanel = new FlowPanel();
        expertPanel.add(horzPanel);
        expertPanel.addStyleName("right clear");
        addWidget(expertPanel);
    }

    public String getXmlString() {
        RpcConvertToXml rpcToXml = new RpcConvertToXml(getReport());
        return rpcToXml.generate();
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

    public void next() {

    }

    public void back() {
        goToBackStep(HistoryNamesType.AddChartReport.name());
    }

    public void save() {
        if (getReport().getClonable()) {
            getReport().setId(null);
            getReport().setCode(null);
        }
        savePopupPanel.showPopup(getReport().getId());
        savePopupPanel.addCloseHandler(popupPanelCloseEvent -> {
            if (savePopupPanel.getSavedReportId() != null && savePopupPanel.getSavedReportId() != 0) {
                getReport().setId(savePopupPanel.getSavedReportId());
            }
        });
    }

    @Override
    public void addReportToDashboard() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void runReport() {
        checkReportTypeSetColumnGroupingWithoutClear();
        getReportList();
    }

    public void refreshChanges() {
        getReportList();

    }

    public ReportRpc getReportRpc() {
        getReport().setBrowserTimeZone(getTimeZone());
        if (getReport().getGroupColumns().size() > 0) {
            getReport().setTableType(ReportType.SUMMARY.name());
        } else {
            getReport().setTableType(ReportType.TABULAR.name());
        }
        return getReport();
    }

    private String getTimeZone() {
        Date date = new Date();
        Integer minutes = date.getTimezoneOffset();
        String gmt = "GMT-";
        String timeZone = gmt + Utils.formatMinutes(minutes);
        if (minutes < 0) {
            gmt = "GMT+";
            minutes = (-1) * minutes;
            timeZone = gmt + Utils.formatMinutes(minutes);
        } else if (minutes == 0) {
            timeZone = "GMT";
        }
        return timeZone;
    }

    private void fillOtherReports(final int selectedid) {
        final LinkedList<SelectItem> reportRpcs = new LinkedList<>();
        CoreService.App.get().getUserReportList(getReport().getId(), getReport().isLibrary(), new AsyncCallback<LinkedList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error occured. Please try again");
            }

            @Override
            public void onSuccess(LinkedList<SelectItem> result) {

                for (Integer i = 0; i < result.size(); i++) {
                    SelectItem report = result.get(i);
                    otherReports.addItem(report.getName(), report.getId().toString());
                    if (selectedid == report.getId()) {
                        otherReports.setSelectedIndex(i);
                    }
                    reportRpcs.add(report);
                }
            }
        });

        Command command = () -> {
            WfmUiEventsBus.getInstance().getEventsListenersMap().remove(getReport().getXmlTemplateId() + getReport().getId());
            //SinksContainerFactory.entryPoint.tabs.getSelection().close();
            getReportResult(reportRpcs.get(otherReports.getSelectedIndex()), true);

            for (Integer i = 0; i < reportRpcs.size(); i++) {
                if (reportRpcs.get(i).getId() == getReport().getId()) {
                    otherReports.setSelectedIndex(i);
                    break;
                }
            }
        };

        otherReports.setCommand(command);
    }

    private SinksContainer getReportResult(SelectItem selectListRpc, boolean runReport) {
        return SinksContainerFactory.entryPoint.onHistoryChanged("reporting|summary/" + selectListRpc.getParam() + "/" + selectListRpc.getName().replace("|", "").replace("/", "") + "/" + runReport + "/" + selectListRpc.getId() + "/" + selectListRpc.isSelected() + "/" + selectListRpc.getDescription());
    }

    private void setStarStatus(final ActionButton anchor) {
        CoreService.App.get().getReportStar(getReport().getId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    anchor.addStyleName("keyOptArrowStar-act");
                } else {
                    anchor.addStyleName("keyOptArrowStar");
                }
            }
        });
    }
}