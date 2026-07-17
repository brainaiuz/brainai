package com.edatasite.workforce.gwt.project.client.ui.view.customWidgets;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectInvoice;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by Normurod on 10/15/2016.
 */
public class ProjectInvoicesWidget extends Composite {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private Integer projectId;
    private KpiDataGrid<ProjectInvoice> dataGrid;
    private ListDataProvider<ProjectInvoice> dataProvider;
    public static final ProvidesKey<ProjectInvoice> KEY_PROVIDER = item -> item == null ? null : item.getID();
    private int WIDGET_MAX_HEIGHT = 400;
    private VerticalPanelDiv pdfButton;

    public ProjectInvoicesWidget(Integer projectId, VerticalPanelDiv horizontalPanel) {
        this.projectId = projectId;
        this.pdfButton = horizontalPanel;
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("120px");
        dataGrid.getElement().getStyle().setOverflow(Style.Overflow.AUTO);
        dataProvider.addDataDisplay(dataGrid);

        initWidget(dataGrid);
    }

    private BigDecimal total = BigDecimal.ZERO, totalPaid = BigDecimal.ZERO, totalDue = BigDecimal.ZERO;
    public void loadData() {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setLimit(100);
        ProjectService.App.get().getInvoiceList(projectId, filterParametrs, new AbstractAsyncCallback<ListResult<ProjectInvoice>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ListResult<ProjectInvoice> result) {
                dataProvider.getList().clear();
                dataProvider.getList().addAll(result.getList());

                if (result.getList() != null && !result.getList().isEmpty()) {
                    for (ProjectInvoice inv : result.getList()) {
                        total = total.add(new BigDecimal(inv.getTotal()));
                        totalPaid = totalPaid.add((inv.getFullPaymentInBase()));
                        totalDue = totalDue.add(new BigDecimal(inv.getTotal()).subtract(inv.getFullPaymentInBase()));
                    }
                }

                initProjectMembersTableColumns();
                if (result.getList() != null && result.getList().size() > 0) {
                    int height = result.getList().size() * 40 + 50;

                    if (height > WIDGET_MAX_HEIGHT) {
                        height = WIDGET_MAX_HEIGHT;
                    }
                    dataGrid.setHeight(height+"px");
                }
            }
        });
    }

    private void initProjectMembersTableColumns() {
        Column<ProjectInvoice, String> number = new Column<ProjectInvoice, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(ProjectInvoice object) {
                return object.getInvoiceNumber() != null ? object.getInvoiceNumber() : "";
            }
        };
        dataGrid.addColumn(number, SafeHtmlUtils.fromString(wfmStrings.invoiceNumber()), HtmlTemplates.getInstance().bold("Total"));
        dataGrid.setColumnWidth(number, 20, com.google.gwt.dom.client.Style.Unit.PCT);
        number.setFieldUpdater((i, projectInvoice, s) -> {
            if (Utils.hasPermission(PermissionConstants.PM_SALES_INVOICE_LIST)) {
                Utils.openURL(GWT.getHostPageBaseURL() + "Accounting.html#" + Constants.SALE_INVOICE + "|summary/" + projectInvoice.getID());
            }
        });

        Column<ProjectInvoice, String> date = new Column<ProjectInvoice, String>(new TextCell()) {
            @Override
            public String getValue(ProjectInvoice object) {
                return DateUtils.format(object.getInvoiceDate().getNonConvertedDate());
            }
        };
        dataGrid.addColumn(date, wfmStrings.date());
        dataGrid.setColumnWidth(date, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<ProjectInvoice, SafeHtml> amount = new Column<ProjectInvoice, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(ProjectInvoice object) {
                return SafeHtmlUtils.fromString(Utils.getNumberFormat().format(object.getTotal()));
            }
        };
        dataGrid.addColumn(amount, SafeHtmlUtils.fromString(wfmStrings.amount()), HtmlTemplates.getInstance().blueValue(Utils.getNumberFormat().format(total)));
        dataGrid.setColumnWidth(date, 15, Style.Unit.PCT);

        Column<ProjectInvoice, SafeHtml> paidAmount = new Column<ProjectInvoice, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(ProjectInvoice object) {
                return SafeHtmlUtils.fromString(Utils.getNumberFormat().format(object.getFullPaymentInBase()));
            }
        };
        dataGrid.addColumn(paidAmount, SafeHtmlUtils.fromString(wfmStrings.paidAmount()), HtmlTemplates.getInstance().greenValue(Utils.getNumberFormat().format(totalPaid)));
        dataGrid.setColumnWidth(date, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<ProjectInvoice, SafeHtml> dueAmount = new Column<ProjectInvoice, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(ProjectInvoice object) {
                return SafeHtmlUtils.fromString(Utils.getNumberFormat().format(new BigDecimal(object.getTotal()).subtract(object.getFullPaymentInBase())));
            }
        };
        dataGrid.addColumn(dueAmount, SafeHtmlUtils.fromString(wfmStrings.dueAmount()), HtmlTemplates.getInstance().redValue(Utils.getNumberFormat().format(totalDue)));
        dataGrid.setColumnWidth(date, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<ProjectInvoice, String> status = new Column<ProjectInvoice, String>(new TextCell()) {
            @Override
            public String getValue(ProjectInvoice object) {
                return object.getStatus();
            }
        };
        dataGrid.addColumn(status, wfmStrings.status());
        dataGrid.setColumnWidth(date, 15, com.google.gwt.dom.client.Style.Unit.PCT);

        /*ClickableImageResourceCell clickableImageResourceCell = new ClickableImageResourceCell();
        clickableImageResourceCell.setAppendClass("pointer");
        Column<ProjectInvoice, ImageResource> exportToPDF = new Column<ProjectInvoice, ImageResource>(clickableImageResourceCell) {
            @Override
            public ImageResource getValue(ProjectInvoice projectInvoice) {
                return null;
            }
        };
        exportToPDF.setFieldUpdater((index, object, value) -> {
            new PDFTemplateSelector(Constants.SALE_INVOICE, new ExtendedCommand() {
                @Override
                public void execute(Integer id) {
                    generatePDF(id, object.getID(), false);
                }
            });
        });
        exportToPDF.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        dataGrid.addColumn(exportToPDF, wfmStrings.exportToPDF());
        dataGrid.setColumnWidth(exportToPDF, 10, Style.Unit.PCT);*/
    }
    private void generatePDF(Integer pdfTemplateID, Integer objectId, boolean isCreditNote) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject(objectId, pdfTemplateID, null);
        String pdfURL = CommandConstants.PDF_URL + (isCreditNote ? "/savedReceivableCreditNoteViewPDFHandler" : "/savedSaleInvoceViewPDFHandler");
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(pdfButton, pdfURL, parametrs, "_blank");
    }

    static class HtmlTemplates {

        interface HtmlTemplatesInterface extends SafeHtmlTemplates {
            @Template("<div style=\"width: 100%; color:red;\"><b>{0}</b></div>")
            SafeHtml redValue(String value);

            @Template("<div style=\"width: 100%; color:blue;\"><b>{0}</b></div>")
            SafeHtml blueValue(String value);

            @Template("<div style=\"width: 100%; color:green;\"><b>{0}</b></div>")
            SafeHtml greenValue(String value);

            @Template("<div style=\"width: 100%;\"><b>{0}</b></div>")
            SafeHtml bold(String value);
        }

        private static HtmlTemplatesInterface instance;
        public static HtmlTemplatesInterface getInstance() {
            if (instance == null) {
                instance = GWT.create(HtmlTemplatesInterface.class);
            }

            return instance;
        }
    }
}
