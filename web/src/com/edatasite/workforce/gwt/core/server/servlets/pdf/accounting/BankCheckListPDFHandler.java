package com.edatasite.workforce.gwt.core.server.servlets.pdf.accounting;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankCheckData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Mirjalol
 * Date: 28.11.12
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public class BankCheckListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private AccountingService accountingService;

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("writeChecks");
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        filterParameter.setLimit(1000);

        EdsUser user = uploadManager.getUser();

        ListResult<BankCheckData> solutionList = accountingService.getBankCheckList(filterParameter);
        List<BankCheckData> holListItem = solutionList.getList();
        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();

        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(BankCheckData.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(BankCheckData.NUMBER, new CellData(commonLocalizer.localize("number"), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankCheckData.BANK_ACCOUNT, new CellData(pdfWfmMessageSource.localize(PdfLocalizationName.bank), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankCheckData.PAY_TO, new CellData(pdfWfmMessageSource.localize(PdfLocalizationName.payTo), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankCheckData.DATE, new CellData(pdfWfmMessageSource.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankCheckData.AMOUNT, new CellData(pdfWfmMessageSource.localize(PdfLocalizationName.amount), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankCheckData.ADDRESS, new CellData(commonLocalizer.localize(PdfLocalizationName.address), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankCheckData.MEMO, new CellData(pdfWfmMessageSource.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankCheckData.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankCheckData.CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankCheckData.PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));

        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat decimalFormat = getPriceScaleNumberFormat(fs);
        SimpleDateFormat simpleDateFormat = getCompanyShortDateFormat(company);

        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        for (BankCheckData checkData : holListItem) {
            String[] temp = new String[header.size()];
            for (int j = 0; j < header.size(); j++) {
                if (BankCheckData.NUMBER.equals(header.get(j))) {
                    temp[j] = checkData.getNumberData() == null ? "" : checkData.getNumberData().getNumberString();
                } else if (BankCheckData.BANK_ACCOUNT.equals(header.get(j))) {
                    temp[j] = (checkData.getBankAccount() == null || checkData.getBankAccount().getName() == null) ? "" : checkData.getBankAccount().getName();
                } else if (BankCheckData.PAY_TO.equals(header.get(j))) {
                    temp[j] = checkData.getPayTo() == null ? "" : checkData.getPayTo();
                } else if (BankCheckData.DATE.equals(header.get(j))) {
                    temp[j] = checkData.getDate() == null ? "" : simpleDateFormat.format(checkData.getDate().getNonConvertedDate());
                } else if (BankCheckData.AMOUNT.equals(header.get(j))) {
                    temp[j] = checkData.getAmount() == null ? "" : decimalFormat.format(checkData.getAmount());
                } else if (BankCheckData.ADDRESS.equals(header.get(j))) {
                    temp[j] = checkData.getAddress() == null ? "" : "" + checkData.getAddress();
                } else if (BankCheckData.MEMO.equals(header.get(j))) {
                    temp[j] = checkData.getMemo() == null ? "" : "" + checkData.getMemo();
                } else if (BankCheckData.STATUS.equals(header.get(j))) {
                    temp[j] = checkData.isPostDatedTransaction() ? pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.postedDate, "Posted Date") : "Posted";
                } else if (BankCheckData.CREATOR.equals(header.get(j))) {
                    temp[j] = checkData.getCreator() == null ? "" : "" + checkData.getCreator();
                } else if (BankCheckData.PROJECT.equals(header.get(j))) {
                    temp[j] = checkData.getProject() == null ? "" : "" + checkData.getProject();
                }

            }
            tableList.addPdfTableRows(temp);
        }
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_BankCheckList_" + dateFormat(user.getUserDate()));
    }
}
