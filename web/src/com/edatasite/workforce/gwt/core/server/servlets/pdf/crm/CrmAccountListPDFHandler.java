package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 12.08.2009
 * Time: 20:26:15
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountListPDFHandler extends AbstractITextPostPdfHandler {

    private CRMService crmService;
    private CrmStrings crmStrings;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("company");
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setAllByFilter(false);

        ListLoadConfig config = new ListLoadConfig();
        config.setSortField(filterParametrs.getSortField());

        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        config.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        ListResult<CrmAccountItem> list = getList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        Map<String, CellData> mapColumnHeader = getColumnHeaderMap();
        if (panelTools.isCustomFieldsShown()) {
            CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
        }

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(mapColumnHeader::containsKey)
                .map(mapColumnHeader::get)
                .toList();
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[0]));

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        for (CrmAccountItem item : list.getList()) {
            Map<String, CellData> mapColumns = new HashMap<>();
            String clientbalance = BigDecimal.ZERO.toString();
            String supplierbalance = BigDecimal.ZERO.toString();
            if (item.getClientBalance() != null) {
                if (item.getClientBalance() >= 0) {
                    clientbalance = priceScaleNumberFormat.format(item.getClientBalance());
                } else {
                    clientbalance = "(" + priceScaleNumberFormat.format((-1) * item.getClientBalance()) + ")";
                }
            }
            if (item.getSupplierBalance() != null) {
                if (item.getSupplierBalance() >= 0) {
                    supplierbalance = priceScaleNumberFormat.format(item.getSupplierBalance());
                } else {
                    supplierbalance = "(" + priceScaleNumberFormat.format((-1) * item.getSupplierBalance()) + ")";
                }
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.OWNER)) {
                //mapColumns.put(CrmAccountItem.OWNER, new CellData(refactor(item.getOwnerName()), Element.ALIGN_LEFT));
                mapColumns.put(CrmAccountItem.OWNER, new CellData(refactor(item.getOwnerNames()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.PARENT_ACCOUNT_NAME)) {
                mapColumns.put(CrmAccountItem.PARENT_ACCOUNT_NAME, item.getParent() != null ? new CellData(refactor(item.getParent().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.ACCOUNT_NAME)) {
                mapColumns.put(CrmAccountItem.ACCOUNT_NAME, new CellData(refactor(item.getName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.ACCOUNT_NUMBER)) {
                mapColumns.put(CrmAccountItem.ACCOUNT_NUMBER, new CellData(refactor(item.getNumber()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.PHONE)) {
                mapColumns.put(CrmAccountItem.PHONE, item.getPhone() != null ? new CellData(refactor(item.getPhone().replace("|", "")), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.FAX)) {
                mapColumns.put(CrmAccountItem.FAX, new CellData(refactor(item.getFax().replace("|", "")), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.WEBSITE)) {
                mapColumns.put(CrmAccountItem.WEBSITE, new CellData(refactor(item.getWebsite()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.EMAIL)) {
                mapColumns.put(CrmAccountItem.EMAIL, new CellData(refactor(item.getEmail()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.ACCOUNT_TYPE)) {
                mapColumns.put(CrmAccountItem.ACCOUNT_TYPE, new CellData(refactor(ServerUtils.getSelectItemsAsCommaDelimeted(item.getAccountTypes(), true)), Element.ALIGN_LEFT));
            }
            Address billAddress = item.getDefaultAddress(true);
            Address mailAddress = item.getDefaultAddress(false);
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.BILLING_ADDRESS)) {
                mapColumns.put(CrmAccountItem.BILLING_ADDRESS, new CellData(refactor(billAddress.getAddress()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.BILLING_ADDRESS2)) {
                mapColumns.put(CrmAccountItem.BILLING_ADDRESS2, new CellData(refactor(billAddress.getAddressb()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.CITY)) {
                mapColumns.put(CrmAccountItem.CITY, new CellData(refactor(billAddress.getCity()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.COUNTRY)) {
                mapColumns.put(CrmAccountItem.COUNTRY, new CellData(refactor(billAddress.getCountry()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.STATE)) {
                mapColumns.put(CrmAccountItem.STATE, new CellData(refactor(billAddress.getState()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.POST_CODE)) {
                mapColumns.put(CrmAccountItem.POST_CODE, new CellData(refactor(billAddress.getZipCode()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.MAILING_ADDRESS)) {
                mapColumns.put(CrmAccountItem.MAILING_ADDRESS, new CellData(refactor(mailAddress.getAddress()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.MAILING_ADDRESS2)) {
                mapColumns.put(CrmAccountItem.MAILING_ADDRESS2, new CellData(refactor(mailAddress.getAddressb()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.CITY2)) {
                mapColumns.put(CrmAccountItem.CITY2, new CellData(refactor(mailAddress.getCity()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.COUNTRY2)) {
                mapColumns.put(CrmAccountItem.COUNTRY2, new CellData(refactor(mailAddress.getCountry()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.STATE2)) {
                mapColumns.put(CrmAccountItem.STATE2, new CellData(refactor(mailAddress.getState()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.POST_CODE2)) {
                mapColumns.put(CrmAccountItem.POST_CODE2, new CellData(refactor(mailAddress.getZipCode()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.INDUSTRY)) {
                mapColumns.put(CrmAccountItem.INDUSTRY, new CellData(refactor(item.getIndustry()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.CURRENCY)) {
                mapColumns.put(CrmAccountItem.CURRENCY, new CellData(refactor(item.getCurrency()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.VAT_NUMBER)) {
                mapColumns.put(CrmAccountItem.VAT_NUMBER, new CellData(refactor(item.getVatNumber()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.TAX)) {
                mapColumns.put(CrmAccountItem.TAX, new CellData(refactor(item.getTaxName()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.PAYMENT_METHOD)) {
                mapColumns.put(CrmAccountItem.PAYMENT_METHOD, new CellData(refactor(item.getPaymentMethod()), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.BLOCKED)) {
                mapColumns.put(CrmAccountItem.BLOCKED, item.isBlocked() ? new CellData(commonLocalizer.localize(PdfLocalizationName.yes), Element.ALIGN_LEFT) : new CellData(commonLocalizer.localize(PdfLocalizationName.no), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.LAST_MODIFIED)) {
                mapColumns.put(CrmAccountItem.LAST_MODIFIED, new CellData(refactor(dateFormat(item.getLastUpdatedDate())), Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.STATUS)) {
                mapColumns.put(CrmAccountItem.STATUS, new CellData(item.isBlocked() ? commonLocalizer.localize(PdfLocalizationName.blocked) : commonLocalizer.localize(PdfLocalizationName.active)));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.CREATION_DATE)) {
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    mapColumns.put(CrmAccountItem.CREATION_DATE, new CellData(ServerUtils.convertToUzbDateFormat(refactor(dateFormat(item.getCreatedDate()))), Element.ALIGN_LEFT));
                } else {
                    mapColumns.put(CrmAccountItem.CREATION_DATE, new CellData(refactor(dateFormat(item.getCreatedDate())), Element.ALIGN_LEFT));
                }
//                mapColumns.put(CrmAccountItem.CREATION_DATE, new CellData(refactor(dateFormat(item.getCreatedDate())),Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.CONTACT_NAME)) {
                mapColumns.put(CrmAccountItem.CONTACT_NAME, item.getPrimaryContact() != null ? new CellData(refactor(item.getPrimaryContact().getName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.CONTACT_EMAIL)) {
                mapColumns.put(CrmAccountItem.CONTACT_EMAIL, item.getPrimaryContactEmail() != null ? new CellData(refactor(item.getPrimaryContactEmail()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.BANK_ACCOUNT)) {
                mapColumns.put(CrmAccountItem.BANK_ACCOUNT, item.getBankAccount() != null ? new CellData(refactor(item.getBankAccount()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.CLIENT_BALANCE)) {
                mapColumns.put(CrmAccountItem.CLIENT_BALANCE, new CellData(clientbalance, Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.SUPPLIER_BALANCE)) {
                mapColumns.put(CrmAccountItem.SUPPLIER_BALANCE, new CellData(supplierbalance, Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.CREDIT_LIMIT)) {
                mapColumns.put(CrmAccountItem.CREDIT_LIMIT, item.getCreditLimit() != null ? new CellData(refactor(priceScaleNumberFormat.format(item.getCreditLimit())), Element.ALIGN_RIGHT) : new CellData(BigDecimal.ZERO.toString(), Element.ALIGN_RIGHT));
            }
            if (panelTools.getColumnCodeName().contains(CrmAccountItem.TERMS)) {
                mapColumns.put(CrmAccountItem.TERMS, item.getTermName() != null ? new CellData(refactor(item.getTermName()), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
            }

            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, company);

            List<CellData> columns = panelTools.getColumnCodeName().stream()
                    .filter(mapColumns::containsKey)
                    .map(mapColumns::get)
                    .toList();
            tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    private String refactor(String value) {
        return StringUtils.isNotEmpty(value) ? value : "—";
    }

    public ListResult<CrmAccountItem> getList(ListingFilterParameter filterParametrs) {
        return crmService.getCrmAccounts(filterParametrs);
    }

    public Map<String, CellData> getColumnHeaderMap() {
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(CrmAccountItem.OWNER, new CellData(commonLocalizer.localize(PdfLocalizationName.accountEmployee), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.PARENT_ACCOUNT_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.parentaccount), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.ACCOUNT_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.ACCOUNT_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.PHONE, new CellData(commonLocalizer.localize(PdfLocalizationName.phone), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.FAX, new CellData(commonLocalizer.localize(PdfLocalizationName.fax), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.WEBSITE, new CellData(commonLocalizer.localize(PdfLocalizationName.website), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.EMAIL, new CellData(commonLocalizer.localize(PdfLocalizationName.email), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.ACCOUNT_TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.accountType), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.BILLING_ADDRESS, new CellData(commonLocalizer.localize(PdfLocalizationName.billingStreet1), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.BILLING_ADDRESS2, new CellData(commonLocalizer.localize(PdfLocalizationName.billingStreet2), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.CITY, new CellData(commonLocalizer.localize(PdfLocalizationName.billingCity), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.COUNTRY, new CellData(commonLocalizer.localize(PdfLocalizationName.country), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.STATE, new CellData(commonLocalizer.localize(PdfLocalizationName.state), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.POST_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.billingPostcode), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.MAILING_ADDRESS, new CellData(commonLocalizer.localize(PdfLocalizationName.mailingStreet1), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.MAILING_ADDRESS2, new CellData(commonLocalizer.localize(PdfLocalizationName.mailingStreet2), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.CITY2, new CellData(commonLocalizer.localize(PdfLocalizationName.mailingCity), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.COUNTRY2, new CellData(commonLocalizer.localize(PdfLocalizationName.mailingCountry), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.STATE2, new CellData(commonLocalizer.localize(PdfLocalizationName.mailingState), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.POST_CODE2, new CellData(commonLocalizer.localize(PdfLocalizationName.mailingPostcode), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.INDUSTRY, new CellData(commonLocalizer.localize(PdfLocalizationName.industry), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.CURRENCY, new CellData(commonLocalizer.localize(PdfLocalizationName.currency), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.VAT_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.vatNumber), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.TAX, new CellData(commonLocalizer.localize(PdfLocalizationName.taxRate), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.PAYMENT_METHOD, new CellData(commonLocalizer.localize(PdfLocalizationName.paymentMethod), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.BLOCKED, new CellData(commonLocalizer.localize(PdfLocalizationName.blocked), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.LAST_MODIFIED, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.CREATION_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.creationDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.CONTACT_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.contactName), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.CLIENT_BALANCE, new CellData(commonLocalizer.localize(PdfLocalizationName.clientBalance), Element.ALIGN_RIGHT));
        mapColumnHeader.put(CrmAccountItem.SUPPLIER_BALANCE, new CellData(commonLocalizer.localize(PdfLocalizationName.balance), Element.ALIGN_RIGHT));
        mapColumnHeader.put(CrmAccountItem.CREDIT_LIMIT, new CellData(commonLocalizer.localize(PdfLocalizationName.creditLimit), Element.ALIGN_RIGHT));
        mapColumnHeader.put(CrmAccountItem.TERMS, new CellData(pdfWfmMessageSource.localize("terms"), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.CONTACT_EMAIL, new CellData(commonLocalizer.localize(PdfLocalizationName.email), Element.ALIGN_LEFT));
        mapColumnHeader.put(CrmAccountItem.BANK_ACCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.bankAccount), Element.ALIGN_LEFT));
        return mapColumnHeader;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_CRM_Account_List_" + dateFormat(new Date()));
    }

    public void setCrmService(CRMService crmService) {
        this.crmService = crmService;
    }
}
