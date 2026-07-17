package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.client.client.rpc.ClientContactList;
import com.edatasite.workforce.gwt.client.client.rpc.ClientContactListItem;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CrmAccountRequestObject;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Xushnud
 * Date: 29.12.2009
 * Time: 19:22:55
 * To change this template use File | Settings | File Templates.
 */
public class SuppliersViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {
    @Autowired
    @Qualifier("payrollLocalizer")
    protected WfmMessageSource payrollLocalizer;

    ClientService clientService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdf = new ITextGenericPdfData();

        CrmAccountRequestObject requestObject = (CrmAccountRequestObject) dataClass;
        CrmAccountItem accountItem = clientService.getSupplier(requestObject.getObjectID());

        EdsUser edsUser = userManager.getUser();
        DecimalFormat numberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), null);
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(edsUser.getCompany());

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable clientTable = new CustomisedITextTable();
        String tableName = "";
        String supplierName = "";
        String supplierCode = "";
        if (CRM_ACCOUNT_TYPE.equals(requestObject.getType())) {
            tableName = commonLocalizer.localize(PdfLocalizationName.company);
            supplierName = commonLocalizer.localize(PdfLocalizationName.companyName);
            supplierCode = payrollLocalizer.localize(PdfLocalizationName.companyCode);
        } else {
            tableName = commonLocalizer.localize(PdfLocalizationName.supplier);
            supplierName = pdfWfmMessageSource.localize(PdfLocalizationName.supplier);
            supplierCode = pdfWfmMessageSource.localize(PdfLocalizationName.supplierCode);
        }
        clientTable.setName(tableName + " " + commonLocalizer.localize(PdfLocalizationName.summaryOnly));
        clientTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        clientTable.addRowWithCode(NAME, supplierName, refactor(accountItem.getName()));
        clientTable.addRowWithCode(PDFConstants.CLIENT_CODE, supplierCode, refactor(accountItem.getCode()));
        clientTable.addRowWithCode(CURRENCY, commonLocalizer.localize(PdfLocalizationName.currency), refactor(accountItem.getCurrency()));
        clientTable.addRowWithCode(CLIENT_VAT_NUMBER, commonLocalizer.localize(PdfLocalizationName.vatNumber), refactor(accountItem.getVatNumber()));
        clientTable.addRowWithCode(PAYMENT_METHOD, commonLocalizer.localize(PdfLocalizationName.paymentMethod), refactor(accountItem.getPaymentMethod()));
        customData.put("SUPPLIER_TABLE", clientTable);

        CustomisedITextTable billAddressTable = new CustomisedITextTable();
        billAddressTable.setName(commonLocalizer.localize(PdfLocalizationName.billingAddress));
        billAddressTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        Address billAddress = accountItem.getDefaultAddress(true);
        if (billAddress != null) {
            billAddressTable.addRowWithCode(BILL_ADDRESS, commonLocalizer.localize(PdfLocalizationName.streetAddress1), refactor(billAddress.getAddress()));
            billAddressTable.addRowWithCode(BILL_ADDRESS2, commonLocalizer.localize(PdfLocalizationName.streetAddress2), refactor(billAddress.getAddressb()));
            billAddressTable.addRowWithCode(BILL_CITY, commonLocalizer.localize(PdfLocalizationName.cityOnly), refactor(billAddress.getCity()));
            billAddressTable.addRowWithCode(BILL_COUNTRY, commonLocalizer.localize(PdfLocalizationName.country), countryLocalizer.localize(billAddress.getCountryCode(), refactor(billAddress.getCountry())));
            billAddressTable.addRowWithCode(BILL_STATE, commonLocalizer.localize(PdfLocalizationName.state), refactor(billAddress.getState()));
            billAddressTable.addRowWithCode(BILL_ZIPCODE, commonLocalizer.localize(PdfLocalizationName.postCode), refactor(billAddress.getZipCode()));
        }
        customData.put("BILL_ADDRESS_TABLE", billAddressTable);

        CustomisedITextTable mailAddressTable = new CustomisedITextTable();
        mailAddressTable.setName(commonLocalizer.localize(PdfLocalizationName.mailingShippingAddress));
        mailAddressTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        Address mailAddress = accountItem.getDefaultAddress(false);
        if (mailAddress != null) {
            mailAddressTable.addRowWithCode(MAIL_ADDRESS, commonLocalizer.localize(PdfLocalizationName.streetAddress1), refactor(mailAddress.getAddress()));
            mailAddressTable.addRowWithCode(MAIL_ADDRESS2, commonLocalizer.localize(PdfLocalizationName.streetAddress2), refactor(mailAddress.getAddressb()));
            mailAddressTable.addRowWithCode(MAIL_CITY, commonLocalizer.localize(PdfLocalizationName.cityOnly), refactor(mailAddress.getCity()));
            mailAddressTable.addRowWithCode(MAIL_COUNTRY, commonLocalizer.localize(PdfLocalizationName.country), countryLocalizer.localize(mailAddress.getCountryCode(), refactor(mailAddress.getCountry())));
            mailAddressTable.addRowWithCode(MAIL_STATE, commonLocalizer.localize(PdfLocalizationName.state), refactor(mailAddress.getState()));
            mailAddressTable.addRowWithCode(MAIL_ZIPCODE, commonLocalizer.localize(PdfLocalizationName.postCode), refactor(mailAddress.getZipCode()));
        }
        customData.put("MAIL_ADDRESS_TABLE", mailAddressTable);

        List<String> columnsValue = Lists.newArrayList();
        CustomisedITextTable clientContactTable = new CustomisedITextTable();
        clientContactTable.setName(commonLocalizer.localize(PdfLocalizationName.contacts));
        clientContactTable.addColumn(FIRST_NAME, commonLocalizer.localize(PdfLocalizationName.firstName));
        clientContactTable.addColumn(LAST_NAME, commonLocalizer.localize(PdfLocalizationName.lastName));
        clientContactTable.addColumn(EMAIL, commonLocalizer.localize(PdfLocalizationName.email));
        clientContactTable.addColumn(PHONE, commonLocalizer.localize(PdfLocalizationName.phone));
        clientContactTable.addColumn(ADDRESS1, commonLocalizer.localize(PdfLocalizationName.address));

        ClientContactList contactList = clientService.getSupplierContactLists(requestObject.getObjectID());
        ClientContactListItem[] contactListItems = contactList.getResult();
        if (contactListItems != null && contactListItems.length > 0) {
            for (ClientContactListItem contactListItem : contactListItems) {
                columnsValue.clear();
                columnsValue.add(refactor(contactListItem.getFirstName()));
                columnsValue.add(refactor(contactListItem.getLastName()));
                columnsValue.add(refactor(contactListItem.getEmail()));
                columnsValue.add(Utils.formatPhoneNumber(contactListItem.getPhone()));
                columnsValue.add(refactor(contactListItem.getAddress()));
                clientContactTable.addRow(columnsValue.toArray(new String[]{}));
            }
        }
        customData.put("SUPPLIER_CONTACT_TABLE", clientContactTable);

        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        if (accountItem.getCustomFields() != null && !accountItem.getCustomFields().isEmpty()) {
            for (CompanyCustomFieldItem customField : accountItem.getCustomFields()) {
                switch (customField.getDataType()) {
                    case CompanyCustomFieldItem.DATE -> {
                        String dateValue = "";
                        if (customField.getFieldDateNonConvertedValue() != null) {
                            dateValue = dateFormat.format(customField.getFieldDateNonConvertedValue().getNonConvertedDate());
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), dateValue);
                    }
                    case CompanyCustomFieldItem.NUMBER -> {
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(customField.getFieldStringValue())) {
                            numberValue = numberFormat.format(Double.valueOf(customField.getFieldStringValue()));
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), numberValue);
                    }
                    default ->
                            customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), refactor(customField.getFieldStringValue()));
                }
            }
        }
        customData.put("CUSTOM_FIELD", customFieldTable);

        pdf.setCustomData(customData);
        return pdf;
    }

    private String refactor(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new CrmAccountRequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName((user.getName().length() > 24 ? user.getName().substring(0, 24) : user.getName()) + user.getLastName() + "_Supplier Summary_" + dateFormat(user.getUserDate()));
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    protected String getTableName(Object dataClass) {
        if (dataClass instanceof CrmAccountRequestObject) {
            CrmAccountRequestObject object = (CrmAccountRequestObject) dataClass;
            if (CRM_ACCOUNT_TYPE.equals(object.getType())) {
                return commonLocalizer.localize(PdfLocalizationName.companies);
            } else {
                return commonLocalizer.localize(PdfLocalizationName.supplier);
            }
        }
        return commonLocalizer.localize(PdfLocalizationName.supplier);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.SUPPLIER;
    }
}
