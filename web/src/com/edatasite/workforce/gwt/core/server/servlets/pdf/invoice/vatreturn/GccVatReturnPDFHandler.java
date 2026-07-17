package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice.vatreturn;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnData;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public abstract class GccVatReturnPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants, PdfLocalizationName {
    @Autowired
    protected VatReturnService vatReturnService;
    @Autowired
    protected CurrencyService currencyService;
    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        VatReturnData vatReturn = vatReturnService.generateVatReturn(filterParametrs.getObjectId());
        VatReturnItem vatReturnItem = vatReturnService.getVatReturn(filterParametrs.getObjectId());

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        customData.put("SALES_TABLE", getVatSalesTable(vatReturn));
        customData.put("EXPENSE_TABLE", getVatExpenseTable(vatReturn));
        customData.put("NET_TABLE", getNetVatTable(vatReturn));
        pdfData.setCustomData(customData);
        pdfData.setExtraData(getVatReturnDate(vatReturnItem));
        return pdfData;
    }

    protected abstract CustomisedITextTable getVatSalesTable(VatReturnData vatReturn);

    protected abstract CustomisedITextTable getVatExpenseTable(VatReturnData vatReturn);

    protected abstract CustomisedITextTable getNetVatTable(VatReturnData vatReturn);

    protected String getVatReturnDate(VatReturnItem vatReturnItem) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return "From <span>" + simpleDateFormat.format(vatReturnItem.getFromDate().getNonConvertedDate()) + "</span> To <span>" + simpleDateFormat.format(vatReturnItem.getToDate().getNonConvertedDate()) + "</span>";
    }

    protected String getValueAsString(BigDecimal value) {
        DecimalFormat priceFormat = new DecimalFormat(",##0.00");
        if (value == null) {
            return "";
        }
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return priceFormat.format(value.setScale(ServerUtils.getCalculationScale(), BigDecimal.ROUND_HALF_UP).doubleValue());
        } else {
            return "(" + priceFormat.format(value.abs().setScale(ServerUtils.getCalculationScale(), BigDecimal.ROUND_HALF_UP).doubleValue()) + ")";
        }
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(Integer.valueOf(request.getParameter("objectId")));
        return filterParameter;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("VAT_Return_" + dateFormat(user.getUserDate()));
    }

}
