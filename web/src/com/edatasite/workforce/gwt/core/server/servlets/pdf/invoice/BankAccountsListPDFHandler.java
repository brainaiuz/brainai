package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankAccount;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Aug 10, 2009
 * Time: 2:37:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankAccountsListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;

    private AccountingService accountingService;

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("bankAccounts");
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsUser user = uploadManager.getUser();

        ListResult<BankAccount> solutionList = accountingService.getBankAccounts(filterParametrs);
        List<BankAccount> holListItems = solutionList.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        filterParametrs.setLimit(1000);

        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(BankAccount.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(BankAccount.CODE_COLUMN, new CellData(accountingLocalizer.localize(PdfLocalizationName.code), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankAccount.NUMBER_COLUMN, new CellData(accountingLocalizer.localize(PdfLocalizationName.accountNumber), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankAccount.NAME_COLUMN, new CellData(accountingLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankAccount.CURRENCY_COLUMN, new CellData(accountingLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        mapColumnHeader.put(BankAccount.AMOUNT_COLUMN, new CellData(accountingLocalizer.localize("balance"), Element.ALIGN_RIGHT));

        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        for (BankAccount bankAccounts : holListItems) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                if (BankAccount.CODE_COLUMN.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(bankAccounts.getCode());
                    cell.add(header.indexOf(BankAccount.CODE_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (BankAccount.NUMBER_COLUMN.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(bankAccounts.getAccountNumber());
                    cell.add(header.indexOf(BankAccount.NUMBER_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (BankAccount.NAME_COLUMN.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(bankAccounts.getName());
                    cell.add(header.indexOf(BankAccount.NAME_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if(BankAccount.CURRENCY_COLUMN.equals(header.get(j))) {
                    temp[j] = bankAccounts.getCurrency() != null && bankAccounts.getCurrency().getName() != null ? bankAccounts.getCurrency().getName() : "—";
                    cell.add(header.indexOf(BankAccount.CURRENCY_COLUMN), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (BankAccount.AMOUNT_COLUMN.equals(header.get(j))) {
                    temp[j] = priceScaleNumberFormat.format(bankAccounts.getBalance() != null ? bankAccounts.getBalance() : BigDecimal.ZERO);
                    cell.add(header.indexOf(BankAccount.AMOUNT_COLUMN), new CellData(temp[j], Element.ALIGN_RIGHT));
                }
                if (bankAccounts.getCustomFieldsMap() != null && bankAccounts.getCustomFieldsValue(header.get(j)) != null) {
                    if (bankAccounts.getCustomFieldsValue(header.get(j)) instanceof Date) {
                        temp[j] = dateFormat((Date) bankAccounts.getCustomFieldsValue(header.get(j)));
                        cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                    } else {
                        temp[j] = bankAccounts.getCustomFieldsValue(header.get(j)) != null ? bankAccounts.getCustomFieldsValue(header.get(j)).toString() : "—";
                        cell.add(new CellData(temp[j], Element.ALIGN_LEFT));
                    }
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }

        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_BankAccountsList_" + dateFormat(user.getUserDate()));
    }

    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }
}
