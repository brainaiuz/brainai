package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants.CHECK_NUMBER;

/**
 * User: Dilsh0d Madrahimov
 * Date: 03.03.2017
 */
public class BankTransferListPDFHandler extends AbstractITextPostPdfHandler implements AccountingConstants {

    @Autowired
    private AccountingService accountingService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        EdsUser user = uploadManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();

        filterParameters.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : 1000);

        filterParameters.setStartDate(parseFilterParameterDate(filterParameters.getStartDateNC()));
        filterParameters.setEndDate(parseFilterParameterDate(filterParameters.getEndDateNC()));
        filterParameters.setFromExcelPDF(true);
        ListResult<NewManualTransaction> bankTransferList = accountingService.getBankCashTransferList(filterParameters);
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(NUMBER_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(ACCOUNT_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.account), Element.ALIGN_LEFT));
        mapColumnHeader.put(DATE_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.date), Element.ALIGN_LEFT));
        mapColumnHeader.put(PROJECT_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        mapColumnHeader.put(REFERENCE_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.reference), Element.ALIGN_LEFT));
        mapColumnHeader.put(AMOUNT_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.amount), Element.ALIGN_RIGHT));
        mapColumnHeader.put(CURRENCY_COLUMN, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        mapColumnHeader.put(POST_DATED, new CellData(commonLocalizer.localize(PdfLocalizationName.postedDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(CHECK_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.checkNumber), Element.ALIGN_LEFT));

        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());

        for (NewManualTransaction item : bankTransferList.getList()) {
            Map<String, CellData> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(NUMBER_COLUMN)) {
                String number = getResultOrLongDash(item.getNumber());
                mapColumns.put(NUMBER_COLUMN, new CellData(number, Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(ACCOUNT_COLUMN)) {
                String account = item.getAccount() != null ? escapeHtml(item.getAccount().getName()) : "—";
                mapColumns.put(ACCOUNT_COLUMN, new CellData(account, Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(DATE_COLUMN)) {
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    String date = item.getDate() != null && item.getDate().getNonConvertedDate() != null ? shortDateFormat.format(item.getDate().getNonConvertedDate()) : "—";
                    mapColumns.put(DATE_COLUMN, new CellData(ServerUtils.convertToUzbDateFormat(date), Element.ALIGN_LEFT));
                } else {
                    String date = item.getDate() != null && item.getDate().getNonConvertedDate() != null ? shortDateFormat.format(item.getDate().getNonConvertedDate()) : "—";
                    mapColumns.put(DATE_COLUMN, new CellData(date, Element.ALIGN_LEFT));
                }
//                String date = item.getDate() != null && item.getDate().getNonConvertedDate() != null ? shortDateFormat.format(item.getDate().getNonConvertedDate()) : "—";
            }
            if (panelTools.getColumnCodeName().contains(PROJECT_COLUMN)) {
                String project = item.getProject() != null ? escapeHtml(item.getProject().getName()) : "—";
                mapColumns.put(PROJECT_COLUMN, new CellData(project, Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(REFERENCE_COLUMN)) {
                String reference = getResultOrLongDash(item.getReference());
                mapColumns.put(REFERENCE_COLUMN, new CellData(reference, Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(AMOUNT_COLUMN)) {
                String total = priceScaleFormat.format(item.getTotal() != null ? item.getTotal() : BigDecimal.ZERO);
                mapColumns.put(AMOUNT_COLUMN, new CellData(total, Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(CURRENCY_COLUMN)) {
                String currency = item.getCurrency() != null ? item.getCurrency().getName() : fs.getCurrency() != null ? fs.getCurrency().getName() : "—";
                mapColumns.put(CURRENCY_COLUMN, new CellData(currency, Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(POST_DATED)) {
                String postDateTransaction = item.isPostDatedTransaction() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no);
                mapColumns.put(POST_DATED, new CellData(postDateTransaction, Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CREATOR)) {
                String creator = getResultOrLongDash(item.getCreator());
                mapColumns.put(CREATOR, new CellData(creator, Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CHECK_NUMBER)) {
                String creator = getResultOrLongDash(item.getCheckNumber());
                mapColumns.put(CHECK_NUMBER, new CellData(creator, Element.ALIGN_LEFT));
            }
            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, company);

            List<CellData> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> mapColumns.containsKey(columnCode))
                    .map(columnCode -> mapColumns.get(columnCode))
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
        }

        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    public String getTableName(Object dataClass) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParameters.getPropertyCode());
        if (filterParameters.getPropertyCode().equals("CASH_RECEIPT")) {
            return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.cashReceipt);
        } else if (filterParameters.getPropertyCode().equals("CASH_PAYMENT")) {
            return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.cashPayment);
        } else if (filterParameters.getPropertyCode().equals("SPEND_MONEY")) {
            return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.bankPayment);
        } else if (filterParameters.getPropertyCode().equals("RECEIVE_MONEY")) {
            return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.bankReceipt);
        }
        return null;

    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        setFileName(filterParameters.getViewType() + dateFormat(user.getUserDate()));
    }

}
