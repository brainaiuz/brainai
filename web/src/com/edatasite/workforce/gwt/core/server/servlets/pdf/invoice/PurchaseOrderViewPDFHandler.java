package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextUserData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: DELL
 * Date: 31-May-2009
 * Time: 07:30:32
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseOrderViewPDFHandler extends BaseInvoicePdfHandler {

    @Autowired
    private GenericSettingsManager genericSettingsManager;

    public String getFileName() {
        return PO_FILE_NAME;
    }

    protected Map<String, String> getFileNameParams(Integer objectID) {
        Map<String, String> map = new HashMap<>();
        EdsQuote order = quoteManager.get(objectID);
        map.put(PDF_CLIENT, order.getClientOrSupplier().getName());
        map.put(PDF_CLIENT_CODE, order.getClientOrSupplier().getNumber());
        map.put(PDF_NUMBER, order.getNumber());
        return map;
    }

    @Override
    protected boolean isClient() {
        return false;
    }

    protected String getFromInvoice() {
        return PURCHASE_ORDER;
    }

    @Override
    public <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceData(NewInvoice invoice, EdsUser edsUser, EdsCurrency edsCurrency, ClientOrSupplier clientOrSupplier, EdsCrmContact clientContact) {
        boolean isAddDiscountColumn = addDiscountColumn(invoice);
        boolean isAddTaxColumn = addTaxColumn(invoice);
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        pdfData.setTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.purchaseOrder));
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        pdfData.setBaseInvoice(baseInvoice);

        Map<String, String> supplierData = getBillToAddressMap(clientOrSupplier, clientContact, invoice, false);
        baseInvoice.setClientSupplierData(supplierData);

        HashMap<String, String> numDateTableRowKeys = new HashMap<>();
        numDateTableRowKeys.put(INV_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poNumber));
        numDateTableRowKeys.put(REFERENCE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference));
        numDateTableRowKeys.put("SHIP_TO_LABEL", pdfWfmMessageSource.localize(PdfLocalizationName.shipTo));
        numDateTableRowKeys.put("SUPPLIER_LABEL", pdfWfmMessageSource.localize(PdfLocalizationName.supplier));
        numDateTableRowKeys.put(INV_DATE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.purchaseOrderDate));
        numDateTableRowKeys.put(QRCODE, "QR code");
        numDateTableRowKeys.put(INV_DUE_DATE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.dueDate));
        if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.HIDE_PROJECT_IN_PURCHASE_ORDER_TEMPLATE)) {
            numDateTableRowKeys.put(PROJECT_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project));
        } else {
            numDateTableRowKeys.put(PROJECT_CODE_ONLY, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project));
        }

        baseInvoice.setNumberAndDatesTable(getNumberAndDatesTableData(invoice, edsUser, numDateTableRowKeys/*numDatesColumns, addRowNumDates*/));
        baseInvoice.setPoDataTable(getPOTableData(invoice, edsCurrency));
        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderInfomation));

        String curSymbol = getCurrencySymbol(edsCurrency, false);
        boolean isProjectLineItemEnable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);

        LinkedHashMap<String, String> columns = new LinkedHashMap<>();

        if (invoice.getCustomItemColumns() != null && invoice.getCustomItemColumns().length > 0) {
            ArrayList<Float> widths = new ArrayList<>();

            columns.put(ITEM_NO, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number));
            widths.add(.5f);

            for (ColumnConfigs column : invoice.getCustomItemColumns()) {
                switch (column.getCode()) {
                    case ItemTableConstants.PRODUCT:
                        columns.put(ITEM_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name));
                        widths.add(2f);
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        columns.put(ITEM_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
                        widths.add(3.7f);
                        break;
                    case ItemTableConstants.QTY:
                        columns.put(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.qty));
                        widths.add(1f);
                        break;
                    case ItemTableConstants.MEASUREMENT:
                        columns.put(ITEM_UNIT_MEASUREMENT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitMeasurement));
                        widths.add(1f);
                        break;
                    case ItemTableConstants.UNITPRICE:
                        columns.put(ITEM_UNIT_PRICE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice) + curSymbol);
                        widths.add(1f);
                        break;
                    case ItemTableConstants.COMISSION:
                        columns.put(ITEM_COMISSION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.commission));
                        widths.add(1f);
                    case ItemTableConstants.DISCOUNT_AMT:
                        if (isAddDiscountColumn) {
                            columns.put(ITEM_DISCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount));
                            widths.add(1f);
                        }
                        break;
                    case ItemTableConstants.DEPARTMENT:
                        columns.put(ITEM_DEPARTMENT, commonLocalizer.localize(PdfLocalizationName.department));
                        widths.add(1f);
                        break;
                    case ItemTableConstants.ACCOUNT:
                        columns.put(ITEM_ACCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.account));
                        widths.add(1f);
                        break;
                    case ItemTableConstants.NET_AMT:
                        columns.put(ITEM_NET_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.netAmount));
                        widths.add(1f);
                        break;
                    case ItemTableConstants.DISCOUNT_LIST:
                        columns.put(ITEM_DISCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount));
                        widths.add(1f);
                        break;
                    case ItemTableConstants.TAX_LIST:
                        if (isAddTaxColumn) {
                            columns.put(ITEM_TAX_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxAmount));
                            widths.add(1f);
                        }
                        break;
                    case ItemTableConstants.DOUBLE_TAX_LIST:
                        columns.put(ITEM_DOUBLE_TAX_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxAmount));
                        widths.add(1f);
                        break;
                    case ItemTableConstants.TOTAL_AMT:
                        columns.put(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount) + curSymbol);
                        widths.add(1f);
                        break;
                    case ItemTableConstants.PROJECT:
                        if (isProjectLineItemEnable) {
                            columns.put(PROJECT_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project));
                            widths.add(1f);
                        }
                        break;
                    default:
                        columns.put(column.getCode(), column.getTitle());
                        widths.add(1f);
                        break;
                }
            }

            if (columns.get(ITEM_TOTAL_AMOUNT) == null) {
                columns.put(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount) + curSymbol);
                widths.add(1f);
            }

            float[] cwidths = new float[widths.size()];
            for (int i = 0; i< widths.size(); i++) {
                cwidths[i] = widths.get(i);
            }

            baseInvoice.setProductTable(getProducTableData(invoice, edsUser, edsCurrency, columns));
            baseInvoice.getProductTable().addTableWidthPercentage(cwidths);
        } else {
            columns.put(ITEM_NO, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number));
            columns.put(ITEM_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name));
            columns.put(ITEM_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
            columns.put(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.qty));
            columns.put(ITEM_UNIT_PRICE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice) + " " + curSymbol);
            if (isAddDiscountColumn) {
                columns.put(ITEM_DISCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount));
            }
            if (isAddTaxColumn) {
                columns.put(ITEM_TAX_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxAmount));
            }
            columns.put(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount) + " " + curSymbol);
            baseInvoice.setProductTable(getProducTableData(invoice, edsUser, edsCurrency, columns/*productColumnName, addColumnProduct*/));

            if (isAddDiscountColumn && isAddTaxColumn) {
                baseInvoice.getProductTable().addTableWidthPercentage(0.5f, 2f, 3.2f, 1f, 1f, 1f, 1f, 1.2f);
            } else if (isAddDiscountColumn || isAddTaxColumn) {
                baseInvoice.getProductTable().addTableWidthPercentage(0.5f, 2f, 3.2f, 1f, 1f, 1f, 1.2f);
            } else {
                baseInvoice.getProductTable().addTableWidthPercentage(0.5f, 2f, 3.3f, 1f, 1f, 1.2f);
            }
        }

        LinkedHashMap<String, String> rowsMap = new LinkedHashMap<>();
        rowsMap.put(SUBTOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.subTotal));
        rowsMap.put(TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.total));
        rowsMap.put(DISCOUNT_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discountAmount));
        rowsMap.put(SHIPPING_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.shipping) + ":");

        baseInvoice.setInvoiceTotalTable(getTotalTable(edsUser, edsCurrency, invoice, rowsMap));

        baseInvoice.setTermsConditions(getTermsConditionsTableData(invoice, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.termsAndCondition)));
        return pdfData;
    }

    @Override
    protected <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceDataCustomise(NewInvoice invoice, EdsUser edsUser, EdsCurrency edsCurrency, ClientOrSupplier clientOrSupplier, EdsCrmContact clientContact) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        pdfData.setTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.purchaseOrder));
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        baseInvoice.setObjectId(invoice.getID());
        pdfData.setBaseInvoice(baseInvoice);

        //User Data
        ITextUserData userData = new ITextUserData();
        pdfData.setUserData(userData);
        userData.setFullName(edsUser.getFullName());
        if (edsUser.isEmployee()) {
            EdsEmployee emp = getEmployeeManager().get(edsUser.getObjectID());
            userData.setPhone(Utils.formatPhoneNumber((emp.getWorkPhoneFirst() != null && !emp.getWorkPhoneFirst().equals("")) ? escapeHtml(emp.getWorkPhoneFirst()) : ""));
            userData.setEmail(edsUser.getEmail() != null && !edsUser.getEmail().equals("") ? escapeHtml(edsUser.getEmail()) : "");
            userData.setPosition(emp.getPosition() != null ? emp.getPosition().getName() : "");
        }

        baseInvoice.setCustomBillToAddress(getCustomAddressTable(clientOrSupplier, clientContact, invoice, edsUser));

        String[] numAndDateCodes = {PO_NUMBER, QT_NUMBER, INV_DATE, INV_DUE_DATE, INVOICE_DUE_TERMS, REFERENCE, PAYMENT_TERMS, SHIPPING_TERMS, INVOICE_STATUS, QRCODE, INV_DATE_UNIQUE_FORMAT, INV_DUE_DATE_UNIQUE_FORMAT};
        String[] numAndDateLabels = {pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poNumber),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteNumber),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poDate),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.dueDate),
                pdfWfmMessageSource.localize(PdfLocalizationName.paymentTerms),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference),
                pdfWfmMessageSource.localize(PdfLocalizationName.paymentTerms),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.shippingTerms),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceStatus),
                "QR code",
                "Invoice Date Unique Format",
                "Invoice Due Date Unique Format"};
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(invoice, edsUser, numAndDateCodes, numAndDateLabels));

        baseInvoice.setCustomPOTable(getCustomPOTableData(invoice, edsCurrency));
        baseInvoice.setCustomApproverTable(getCustomApproverData(invoice));

        // Company Data
        pdfData.setCompanyData(getCompanyData(edsUser.getCompany(), true, false));

        // Creator Data
        pdfData.setCreatorData(getCreatorData(invoice));
        // Set Currency
        baseInvoice.setCurrency(getCurrencySymbol(edsCurrency, true));
        baseInvoice.setCustomAccountTable(getCustomisedAccountTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomBankTable(getCustomisedBankTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        // Set Currency Name
        baseInvoice.setCurrencyName(getCurrencyName(edsCurrency));

        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderInfomation));

//        List<String> columnCodes = Arrays.asList(ITEM_NO, ITEM_NAME, ITEM_DESCRIPTION, ITEM_QTY_HRS, ITEM_UNIT_PRICE, ITEM_NET_AMOUNT, ITEM_TAX_AMOUNT, ITEM_TOTAL_AMOUNT);
//        List<String> columnLabels = Arrays.asList(" No.:", "Product/Service", "Description", "Qty/Hrs", " Unit Price ", "Net Amount", "Tax ", "Total Amount ");
        baseInvoice.setCustomProductTable(getCustomProducTableData(invoice, edsUser, edsCurrency/*, columnCodes, columnLabels*/));

        baseInvoice.setCustomTotalTable(getCustomisedTotalTable(edsUser, edsCurrency, invoice));
        baseInvoice.setCustomTermsConditions(getCustomTermsConditionsTableData(invoice, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.termsAndCondition)));
        baseInvoice.setCustomFooterData(getCustomFooterData(edsUser));
        baseInvoice.setCustomClientSupplierEntityCustomFieldTable(getCustomClientSupplierEntityCustomFieldTable(invoice.getCustomFieldItems()));
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PDF_GROUP_BY_PRODUCT_CATEGORY)) {
            baseInvoice.setCustomProductCategoriesITextTables(getCustomProducCategoriesTableData(invoice, edsUser, edsCurrency));
        }
        return pdfData;
    }


    private boolean addDiscountColumn(NewInvoice newInvoice) {
        if (newInvoice != null) {
            if (newInvoice.getItems() != null) {
                newInvoice.getItems();
                for (NewInvoiceItem invoiceItem : newInvoice.getItems()) {
                    if ((invoiceItem.getDiscountAmount() != null && invoiceItem.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0) ||
                            (invoiceItem.getDiscountPercent() != null && invoiceItem.getDiscountPercent().compareTo(BigDecimal.ZERO) > 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private boolean addTaxColumn(NewInvoice newInvoice) {
        if (newInvoice != null) {
            if (newInvoice.getItems() != null) {
                newInvoice.getItems();
                for (NewInvoiceItem invoiceItem : newInvoice.getItems()) {
                    if (invoiceItem.getTaxAmount() != null && invoiceItem.getTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    protected String getFooterContactText() {
        return pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.footerPurchaseOrderText);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PURCHASE_ORDER;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return accountingLocalizer.localizeAccounting(PdfLocalizationName.purchaseOrder);
    }
}
