package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.gwt.client.client.rpc.*;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CrmAccountRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ClientViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    @Qualifier("payrollLocalizer")
    protected WfmMessageSource payrollLocalizer;
    @Autowired
    private ClientService clientService;
    @Autowired
    private InvoiceManager invoiceManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        CrmAccountRequestObject requestObject = (CrmAccountRequestObject) dataClass;
        CrmAccountItem accountItem = clientService.getClient(requestObject.getObjectID());

        EdsUser edsUser = userManager.getUser();
        DecimalFormat numberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), null);
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(edsUser.getCompany());

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        customData.put("RELATED_SALES_INVOICES_TABLE", getRelatedSalesInvoices(accountItem));
        CustomisedITextTable clientTable = new CustomisedITextTable();
        String tableName = "";
        String clientName = "";
        String clientCode = "";
        if (CRM_ACCOUNT_TYPE.equals(requestObject.getType())) {
            tableName = commonLocalizer.localize(PdfLocalizationName.company);
            clientName = commonLocalizer.localize(PdfLocalizationName.companyName);
            clientCode = payrollLocalizer.localize(PdfLocalizationName.companyCode);
        } else if (Constants.CUSTOMER.equals(requestObject.getType())) {
            tableName = commonLocalizer.localize(PdfLocalizationName.customer);
            clientName = accountItem.getFormProperty() != null && accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_NAME) != null &&
                    accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_NAME).isChanged() ? accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_NAME).getTitle() : commonLocalizer.localize(PdfLocalizationName.clientName);
            clientCode = accountItem.getFormProperty() != null && accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_NUMBER) != null &&
                    accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_NUMBER).isChanged() ? accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_NUMBER).getTitle() : commonLocalizer.localize(PdfLocalizationName.clientCode);
        } else if (Constants.SUPPLIER.equals(requestObject.getType())) {
            tableName = commonLocalizer.localize(PdfLocalizationName.supplier);
            clientName = pdfWfmMessageSource.localize(PdfLocalizationName.supplier);
            clientCode = pdfWfmMessageSource.localize(PdfLocalizationName.supplierCode);
        }
        clientTable.setName(tableName + " " + commonLocalizer.localize(PdfLocalizationName.summaryOnly));
        clientTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        clientTable.addRowWithCode(NAME, clientName, refactor(accountItem.getName()));
        clientTable.addRowWithCode(PDFConstants.CLIENT_CODE, clientCode, refactor(accountItem.getCode()));
        clientTable.addRowWithCode(CURRENCY, accountItem.getFormProperty() != null && accountItem.getFormProperty().get(CustomFormConstants.CURRENCY) != null &&
                accountItem.getFormProperty().get(CustomFormConstants.CURRENCY).isChanged() ? accountItem.getFormProperty().get(CustomFormConstants.CURRENCY).getTitle() : commonLocalizer.localize(PdfLocalizationName.currency), refactor(accountItem.getCurrency()));
        clientTable.addRowWithCode(CLIENT_VAT_NUMBER, accountItem.getFormProperty() != null && accountItem.getFormProperty().get(CustomFormConstants.VAT_NUMBER) != null &&
                accountItem.getFormProperty().get(CustomFormConstants.VAT_NUMBER).isChanged() ? accountItem.getFormProperty().get(CustomFormConstants.VAT_NUMBER).getTitle() : commonLocalizer.localize(PdfLocalizationName.vatNumber), refactor(accountItem.getVatNumber()));
        clientTable.addRowWithCode(PAYMENT_METHOD, accountItem.getFormProperty() != null && accountItem.getFormProperty().get(CustomFormConstants.PAYMENT_METHOD) != null &&
                accountItem.getFormProperty().get(CustomFormConstants.PAYMENT_METHOD).isChanged() ? accountItem.getFormProperty().get(CustomFormConstants.PAYMENT_METHOD).getTitle() : commonLocalizer.localize(PdfLocalizationName.paymentMethod), refactor(accountItem.getPaymentMethod()));
        customData.put("CLIENT_TABLE", clientTable);

        CustomisedITextTable billAddressTable = new CustomisedITextTable();
        billAddressTable.setName(commonLocalizer.localize(PdfLocalizationName.billingAddress));
        billAddressTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        Address billAddress = accountItem.getDefaultAddress(true);
        if (billAddress != null) {
            billAddressTable.addRowWithCode(BILL_ADDRESS, commonLocalizer.localize(PdfLocalizationName.streetAddress1), refactor(billAddress.getAddress()));
            billAddressTable.addRowWithCode(BILL_ADDRESS2, commonLocalizer.localize(PdfLocalizationName.streetAddress2), refactor(billAddress.getAddressb()));
            billAddressTable.addRowWithCode(BILL_CITY, commonLocalizer.localize(PdfLocalizationName.city), refactor(billAddress.getCity()));
            billAddressTable.addRowWithCode(BILL_BUILDING_NUMBER, commonLocalizer.localize(PdfLocalizationName.buildingNumber), refactor(billAddress.getBuildingNumber()));
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
            mailAddressTable.addRowWithCode(MAIL_CITY, commonLocalizer.localize(PdfLocalizationName.city), refactor(mailAddress.getCity()));
            mailAddressTable.addRowWithCode(MAIL_COUNTRY, commonLocalizer.localize(PdfLocalizationName.country), countryLocalizer.localize(mailAddress.getCountryCode(), refactor(mailAddress.getCountry())));
            mailAddressTable.addRowWithCode(MAIL_STATE, commonLocalizer.localize(PdfLocalizationName.state), refactor(mailAddress.getState()));
            mailAddressTable.addRowWithCode(MAIL_ZIPCODE, commonLocalizer.localize(PdfLocalizationName.postCode), refactor(mailAddress.getZipCode()));
        }
        customData.put("MAIL_ADDRESS_TABLE", mailAddressTable);

        List<String> columnsValue = Lists.newArrayList();
        CustomisedITextTable clientContactTable = new CustomisedITextTable();
        clientContactTable.setName(commonLocalizer.localize(PdfLocalizationName.contacts));
        clientContactTable.addColumn(FIRST_NAME, accountItem.getFormProperty() != null && accountItem.getFormProperty().get(CustomFormConstants.FIRST_NAME) != null &&
                accountItem.getFormProperty().get(CustomFormConstants.FIRST_NAME).isChanged() ? accountItem.getFormProperty().get(CustomFormConstants.FIRST_NAME).getTitle() : commonLocalizer.localize(PdfLocalizationName.firstName));
        clientContactTable.addColumn(LAST_NAME, accountItem.getFormProperty() != null && accountItem.getFormProperty().get(CustomFormConstants.LAST_NAME) != null &&
                accountItem.getFormProperty().get(CustomFormConstants.LAST_NAME).isChanged() ? accountItem.getFormProperty().get(CustomFormConstants.LAST_NAME).getTitle() : commonLocalizer.localize(PdfLocalizationName.lastName));
        clientContactTable.addColumn(EMAIL, accountItem.getFormProperty() != null && accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_EMAIL) != null &&
                accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_EMAIL).isChanged() ? accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_EMAIL).getTitle() : commonLocalizer.localize(PdfLocalizationName.email));
        clientContactTable.addColumn(PHONE, accountItem.getFormProperty() != null && accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_PHONE) != null &&
                accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_PHONE).isChanged() ? accountItem.getFormProperty().get(CustomFormConstants.CRM_ACCOUNT_PHONE).getTitle() : commonLocalizer.localize(PdfLocalizationName.phone));
        clientContactTable.addColumn(ADDRESS1, commonLocalizer.localize(PdfLocalizationName.address));

        ClientContactList contactList = clientService.getContacts(requestObject.getObjectID());
        ClientContactListItem[] contactListItems = contactList.getResult();
        if (contactListItems != null) {
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
        customData.put("CLIENT_CONTACT_TABLE", clientContactTable);

        ListLoadConfig config = new ListLoadConfig();
        config.setStart(0);
        config.setLimit(10);
        ClientProjectList projectList = clientService.getProjects(requestObject.getObjectID(), config);
        ClientProjectListItem[] projectListItems = projectList.getResult();
        CustomisedITextTable projectTable = new CustomisedITextTable();
        projectTable.setName(commonLocalizer.localize(PdfLocalizationName.projects));
        projectTable.addColumn(NAME, commonLocalizer.localize(PdfLocalizationName.name));
        projectTable.addColumn(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        projectTable.addColumn(MANAGER_REJECT, commonLocalizer.localize(PdfLocalizationName.manager));
        projectTable.addColumn(EXP_START_DATE, commonLocalizer.localize(PdfLocalizationName.startDate));
        projectTable.addColumn(EXP_END_DATE, commonLocalizer.localize(PdfLocalizationName.endDate));

        if (projectListItems != null) {
            for (ClientProjectListItem projectListItem : projectListItems) {
                columnsValue.clear();
                columnsValue.add(refactor(projectListItem.getName()));
                columnsValue.add(refactor(projectListItem.getDescription()));
                columnsValue.add(refactor(projectListItem.getManager()));
                columnsValue.add(projectListItem.getStartDate() != null ? dateFormat.format(projectListItem.getStartDate()) : "");
                columnsValue.add(projectListItem.getEndDate() != null ? dateFormat.format(projectListItem.getEndDate()) : "");
                projectTable.addRow(columnsValue.toArray(new String[]{}));
            }
        }
        customData.put("PROJECT_TABLE", projectTable);

        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        if (accountItem.getCustomFields() != null && !accountItem.getCustomFields().isEmpty()) {
            for (CompanyCustomFieldItem customField : accountItem.getCustomFields()) {
                switch (customField.getDataType()) {
                    case CompanyCustomFieldItem.DATE -> {
                        String dateValue = "";
                        if (customField.getFieldDateNonConvertedValue() != null) {
                            dateValue = dateFormat.format(customField.getFieldDateNonConvertedValue().getDate());
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

        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setCustomData(customData);
        return pdf;
    }

    private String refactor(String str) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    private CustomisedITextTable getRelatedSalesInvoices(CrmAccountItem accountItem) {
        List<EdsSaleInvoice> saleInvoices = invoiceManager.getSaleInvoicesByCrmAccountID(accountItem.getObjectId());
        Collections.sort(saleInvoices, (o1, o2) -> o2.getInvoiceDate().compareTo(o1.getInvoiceDate()));

        CustomisedITextTable salesInvoiceTable = new CustomisedITextTable();
        List<String> columnsValue = Lists.newArrayList();
        SimpleDateFormat uniqueDateFormat = new SimpleDateFormat("dd-MM-yyyy");

        salesInvoiceTable.addColumn(INV_NUMBER, commonLocalizer.localize(PdfLocalizationName.name));
        salesInvoiceTable.addColumn(INV_DATE, commonLocalizer.localize(PdfLocalizationName.description));
        salesInvoiceTable.addColumn(INV_DUE_DATE, commonLocalizer.localize(PdfLocalizationName.manager));
        salesInvoiceTable.addColumn(STATUS, commonLocalizer.localize(PdfLocalizationName.startDate));

        for (EdsSaleInvoice invoice : saleInvoices) {
            columnsValue.clear();
            columnsValue.add(refactor(invoice.getNumber()));
            columnsValue.add(invoice.getInvoiceDate() != null ? uniqueDateFormat.format(invoice.getInvoiceDate()) : "");
            columnsValue.add(invoice.getDueDate() != null ? uniqueDateFormat.format(invoice.getDueDate()) : "");
            columnsValue.add(invoice.getStatus() != null ? invoice.getStatus().getName() : "");
            salesInvoiceTable.addRow(columnsValue.toArray(new String[]{}));
        }

        return salesInvoiceTable;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new CrmAccountRequestObject();
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        CrmAccountRequestObject requestObject = (CrmAccountRequestObject) dataClass;
        Integer clientId = requestObject.getObjectID();
        CrmAccountItem clientSingleItem = clientService.getClient(clientId);
        setFileName((clientSingleItem.getName().length() > 24 ? clientSingleItem.getName().substring(0, 24)
                : clientSingleItem.getName()) + "_" + dateFormat(user.getUserDate()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        if (dataClass instanceof CrmAccountRequestObject) {
            CrmAccountRequestObject object = (CrmAccountRequestObject) dataClass;
            if (CRM_ACCOUNT_TYPE.equals(object.getType())) {
                return commonLocalizer.localize(PdfLocalizationName.companies);
            } else if (Constants.CUSTOMER.equals(object.getType())) {
                return commonLocalizer.localize(PdfLocalizationName.customer);
            } else if (Constants.SUPPLIER.equals(object.getType())) {
                return commonLocalizer.localize(PdfLocalizationName.supplier);
            }
        }
        return commonLocalizer.localize(PdfLocalizationName.customer);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.CUSTOMER;
    }
}
