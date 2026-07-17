package com.finnetlimited.reportservice.core.client.ui.content.report;

import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.ReportType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.xml.RpcConvertToXml;
import com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit.ExportFormPanel;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.ReportGenerateTableRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.ReportTreeItem;
import com.finnetlimited.reportservice.core.client.ui.content.report.steps.ReportListBody;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.google.gwt.gen2.table.client.AbstractScrollTable;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Jul 27, 2011
 * Time: 4:03:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportListPanel {

    public static final ReportingStrings reportingStrings = ReportingStrings.App.get();

    protected ListingPanel<ReportTreeItem> listing;
    private ReportRpc report;
    private ReportGenerateTableRpc reportTable;

    private ExportFormPanel csv;
    private ExportFormPanel pdf;

    public ReportListPanel(ReportRpc report, ReportGenerateTableRpc reportTable, ReportListBody reportListBody) {
        this.report = report;
        this.reportTable = reportTable;
    }

    public ListingPanel<ReportTreeItem> getList() {
        DRSLoadingPanel.show();
        listing = new ListingPanel<>(null, getColumns(), getListData(), getDesign());
        listing.hideSearchButton();
        return listing;
    }

    protected ColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columnsConfigList = new ColumnDefinitionConfig[(0 == report.getSelectedColumns().size()) ? reportTable.getTitleRows().size() : report.getSelectedColumns().size()];

        int i = 0;
        for (ColumnRpc columnRpc : (0 == report.getSelectedColumns().size()) ? reportTable.getTitleRows() : report.getSelectedColumns()) {
            final int ii = i;
            columnsConfigList[i++] = new ColumnDefinitionConfig<ReportTreeItem, HTML>(columnRpc.getTitle(), columnRpc.getName(), 100) {
                @Override
                public HTML getCellValue(ReportTreeItem item) {
                    return new HTML(item.getCells().get(ii));
                }

                @Override
                public boolean isHtml() {
                    return true;
                }
            };
        }

        return columnsConfigList;
    }

    protected ListingRequestProvider<ReportTreeItem> getListData() {
        return (filterParameter, callback) -> {
            if (filterParameter.getSortField() != null) {
                report.setSortTableByColumn(filterParameter.getSortField());
                if (filterParameter.isAscending()) {
                    report.setSortTableByColumnType("ASC");
                } else {
                    report.setSortTableByColumnType("DESC");
                }
            }
            report.setPosition(filterParameter.getStart() + 1);
            report.setLimit(filterParameter.getLimit());
            DRSLoadingPanel.show(listing);
            CoreService.App.get().getReportResult(report, new AsyncCallback<ReportGenerateTableRpc>() {
                public void onFailure(Throwable throwable) {
                    DRSLoadingPanel.hide();
                    callback.onFailure(throwable);
                }

                public void onSuccess(ReportGenerateTableRpc reportGenerateRpc) {
                    report = reportGenerateRpc.getReport();
                    listing.setVisible(true);
                    ArrayList<ReportTreeItem> objectList = reportGenerateRpc.getTreeItems();
                    if (objectList != null) {
                        objectList.remove(objectList.size() - 1);
                        callback.onSuccess(new ListResult(objectList, reportGenerateRpc.getRowCount()));
                        listing.getPagingScrollTable().setScrollPolicy(AbstractScrollTable.ScrollPolicy.BOTH);
                    }
                    DRSLoadingPanel.hide();
                }
            });
        };
    }

    protected ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public void initImportExportToolBarWidgets(ExportImportOption exportOption) {
                if (ReportType.TABULAR.name().equals(report.getTableType()) && report.getExcelTemplateId() != null) {
                    csv = new ExportFormPanel("Excel", "operPanel markExcel", "common/reportExcel");
                } else {
                    csv = new ExportFormPanel("CSV", "operPanel markCSV", "common/reportCsv");
                }
                pdf = new ExportFormPanel("PDF", "operPanel markPDF", "common/runtimeReportPdf");

                FlowPanel hp = new FlowPanel();
                hp.add(new HTML("Export as: "));
                hp.add(csv);
                hp.add(pdf);
                csv.addClickEvent(() -> {
                    csv.setParam(getXmlString());
                    csv.submit();
                });
                pdf.addClickEvent(() -> {
                    pdf.setParam(getXmlString());
                    pdf.submit();
                });

                exportOption.initExport(hp, false);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(reportingStrings.currentlyYouDontHaveData());
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };

    }

    public ReportRpc getReportRpc() {
        return report;
    }

    public String getXmlString() {
        RpcConvertToXml rpcToXml = new RpcConvertToXml(report);
        return rpcToXml.generate();
    }

}
