package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTCScheduledTask;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TCScheduledTaskManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfBaseInvoiceTemplate;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCScheduleItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/7/12
 * Time: 11:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class TCScheduledInvoicesViewPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler {

    @Autowired
    private TCScheduledTaskManager tcScheduledTaskManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private LocationManager locationManager;

    private DecimalFormat fourDigitFormat = new DecimalFormat("0000");

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        ITextBaseInvoice viewData = new ITextBaseInvoice();
        pdfData.setBaseInvoice(viewData);

        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;

        EdsUser user = tcScheduledTaskManager.getUser();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        DecimalFormat numberFormat = new DecimalFormat(",##0.00");

        Integer scheduledTaskID = filterParameter.getObjectId();

        EdsTCScheduledTask scheduledTask = tcScheduledTaskManager.get(scheduledTaskID);

        String referenceNumber = fourDigitFormat.format(scheduledTaskID);
        String customerName = crmAccountManager.get(scheduledTask.getCustomerID()).getName();
        String period = shortDateFormat.format(user.getUserDate(scheduledTask.getPeriodStart())) + " - " + shortDateFormat.format(user.getUserDate(scheduledTask.getPeriodEnd()));

        HashMap<String, String> customerData = new HashMap<>();
        customerData.put(PDFConstants.TYPE, ITextPdfBaseInvoiceTemplate.TC_CONSOLIDATED_INVOICE);
        customerData.put(PDFConstants.TC_REFERENCE_NUMBER, referenceNumber);
        customerData.put(PDFConstants.TC_CUSTOMER_NAME, customerName);
        customerData.put(PDFConstants.TC_PERIOD, period);
        if (filterParameter.getLocationId() != null) {
            EdsLocation location = locationManager.get(filterParameter.getLocationId());
            if (location != null) {
                customerData.put(PDFConstants.TC_LOCATION, location.getName());
            }
        }
        viewData.setClientSupplierData(customerData);

        ITextTableList itemsTable = new ITextTableList(3);
        itemsTable.addTableWidthPercentage(3, 1, 1);
        CellData numberHeaderCell = new CellData("Number");
        CellData dateHeaderCell = new CellData("Date");
        CellData amountHeaderCell = new CellData("Amount");
        itemsTable.addPdfTableHeader(numberHeaderCell, dateHeaderCell, amountHeaderCell);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<TCScheduleItem> tcsItemList = tcScheduledTaskManager.getInvoiceSummaryReportData(scheduledTaskID, filterParameter.getLocationId());
        for (TCScheduleItem tcsItem : tcsItemList) {
            totalAmount = totalAmount.add(tcsItem.getAmount());

            CellData numberCell = new CellData(tcsItem.getNumber());
            CellData dateCell = new CellData(shortDateFormat.format(tcsItem.getDate()));
            CellData amountCell = new CellData(numberFormat.format(tcsItem.getAmount()));
            amountCell.setAlignment(Element.ALIGN_RIGHT);
            itemsTable.addPdfTableRows(numberCell, dateCell, amountCell);
        }

        viewData.setProductTable(itemsTable);

        ITextTableList totalTable = new ITextTableList(2);
        CellData totalCell = new CellData(numberFormat.format(totalAmount));
        totalCell.setAlignment(Element.ALIGN_RIGHT);
        totalTable.addPdfTableRows(new CellData("Total"), totalCell);

        viewData.setInvoiceTotalTable(totalTable);

        return pdfData;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new ListingFilterParameter();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        Integer scheduledTaskID = fp.getObjectId();
        EdsTCScheduledTask scheduledTask = tcScheduledTaskManager.get(scheduledTaskID);
        EdsCrmAccount crmAccount = crmAccountManager.get(scheduledTask.getCustomerID());

        StringBuilder fileName = new StringBuilder();
        fileName.append("Consolidated_Invoice_");
        fileName.append(crmAccount.getName());
        fileName.append(dateFormat.format(scheduledTask.getPeriodStart()) + "_" + dateFormat.format(scheduledTask.getPeriodEnd()));
        if (fp.getLocationId() != null) {
            EdsLocation location = locationManager.get(fp.getLocationId());
            if (location != null) {
                fileName.append("_").append(location.getName());
            }
        }

        setFileName(fileName.toString());
    }
}
