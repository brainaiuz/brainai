package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextUserData;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: DELL
 * Date: 31-May-2009
 * Time: 07:31:38
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseInvoiceViewPDFHandler extends BaseInvoicePdfHandler {

    @Autowired
    private ExpenseServiceLocal expenseService;
    @Autowired
    protected GenericSettingsManager genericSettingsManager;

    @Override
    public String getFileName() {
        return PI_FILE_NAME;
    }

    @Override
    protected Map<String, String> getFileNameParams(Integer objectID) {
        final EdsInvoice invoice = invoiceManager.get(objectID);

        if (invoice == null) {
            return Collections.emptyMap();
        }
        return new HashMap<String, String>() {{
            put(PDF_CLIENT, invoice.getClientOrSupplier().getName());
            put(PDF_CLIENT_CODE, invoice.getClientOrSupplier().getNumber());
            put(PDF_NUMBER, invoice.getNumber());
        }};
    }

    @Override
    protected String getFromInvoice() {
        return PURCHASE_INVOICE;
    }

    @Override
    public <T extends EdsCrmAccount> ITextGenericPdfData getInvoiceData(NewInvoice invoice,
                                                                        EdsUser edsUser,
                                                                        EdsCurrency edsCurrency,
                                                                        T clientOrSupplier,
                                                                        EdsCrmContact clientContact) {
        final ITextGenericPdfData pdfData = new ITextGenericPdfData();
        final ITextBaseInvoice baseInvoice = new ITextBaseInvoice();

        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        pdfData.setTableName(commonLocalizer.localize(PdfLocalizationName.purchaseInvoice));
        pdfData.setBaseInvoice(baseInvoice);
        final Map<String, String> supplierData = this.getBillToAddressMap(clientOrSupplier, clientContact, invoice, false);
        final Map<String, String> numDateTableRowKeys = Maps.newHashMap();

        baseInvoice.setClientSupplierData(supplierData);
        numDateTableRowKeys.put(INV_NUMBER, pdfWfmMessageSource.localize(PdfLocalizationName.invoiceNo));
        numDateTableRowKeys.put(PO_NUMBER, commonLocalizer.localize(PdfLocalizationName.poNumber));
        numDateTableRowKeys.put(REFERENCE, accountingLocalizer.localize(PdfLocalizationName.reference));
        numDateTableRowKeys.put(INV_DATE, commonLocalizer.localize(PdfLocalizationName.date));
        numDateTableRowKeys.put(INV_DUE_DATE, accountingLocalizer.localize(PdfLocalizationName.dueDate));
        numDateTableRowKeys.put(PROJECT_NAME, pdfWfmMessageSource.localize(PdfLocalizationName.project));
        numDateTableRowKeys.put(QRCODE, "QR code");
        baseInvoice.setNumberAndDatesTable(this.getNumberAndDatesTableData(invoice, edsUser, numDateTableRowKeys));
        baseInvoice.setProductTableName(pdfWfmMessageSource.localize(PdfLocalizationName.orderInfomation));

        final String curSymbol = this.getCurrencySymbol(edsCurrency, false);
        final boolean isAddDiscountColumn = this.addDiscountColumn(invoice);
        final boolean isAddTaxColumn = this.addTaxColumn(invoice);
        final boolean isProjectLineItemEnable = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        final Map<String, String> columns = Maps.newLinkedHashMap();

        if (invoice.getCustomItemColumns() != null && invoice.getCustomItemColumns().length > 0) {
            final List<Float> widths = Lists.newArrayList();

            columns.put(ITEM_NO, pdfWfmMessageSource.localize(PdfLocalizationName.number));
            widths.add(.5f);

            for (ColumnConfigs column : invoice.getCustomItemColumns()) {
                switch (column.getCode()) {
                    case ItemTableConstants.PRODUCT:
                        columns.put(ITEM_NAME, pdfWfmMessageSource.localize(PdfLocalizationName.name));
                        widths.add(2f);
                        break;
                    case ItemTableConstants.DESCRIPTION:
                        columns.put(ITEM_DESCRIPTION, pdfWfmMessageSource.localize(PdfLocalizationName.description));
                        widths.add(3.2f);
                        break;
                    case ItemTableConstants.QTY:
                        columns.put(ITEM_QTY_HRS, pdfWfmMessageSource.localize(invoice.isProjectBasedInvoice() ? PdfLocalizationName.hours : PdfLocalizationName.qty));
                        widths.add(1f);
                        break;
                    case ItemTableConstants.MEASUREMENT:
                        break;
                    case ItemTableConstants.UNITPRICE:
                        columns.put(ITEM_UNIT_PRICE, pdfWfmMessageSource.localize(PdfLocalizationName.unitPrice) + " " + curSymbol);
                        widths.add(1f);
                        break;
                    case ItemTableConstants.DISCOUNT_AMT:
                        if (isAddDiscountColumn) {
                            columns.put(ITEM_DISCOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.discount));
                            widths.add(1f);
                        }
                        break;
                    case ItemTableConstants.DEPARTMENT:
                        break;
                    case ItemTableConstants.ACCOUNT:
                        break;
                    case ItemTableConstants.NET_AMT:
                        break;
                    case ItemTableConstants.DISCOUNT_LIST:
                        break;
                    case ItemTableConstants.TAX_LIST:
                        if (isAddTaxColumn) {
                            columns.put(ITEM_TAX_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.taxAmount));
                            widths.add(1f);
                        }
                        break;
                    case ItemTableConstants.DOUBLE_TAX_LIST:
                        columns.put(ITEM_DOUBLE_TAX_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.taxAmount));
                        widths.add(1f);
                        break;
                    case ItemTableConstants.WAREHOUSE:
                        columns.put(ITEM_WAREHOUSE, pdfWfmMessageSource.localize(PdfLocalizationName.warehouse));
                        widths.add(1.2f);
                        break;
                    case ItemTableConstants.TOTAL_AMT:
                        columns.put(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.totalAmount) + curSymbol);
                        widths.add(1.5f);
                        break;
                    case ItemTableConstants.PROJECT:
                        if (isProjectLineItemEnable) {
                            columns.put(PROJECT_NAME, pdfWfmMessageSource.localize(PdfLocalizationName.project));
                            widths.add(1.2f);
                        }
                        break;
                    case ItemTableConstants.CLIENT:
                        break;
                    default:
                        columns.put(column.getCode(), column.getTitle());
                        widths.add(1f);
                        break;
                }
            }

            if (columns.get(ITEM_TOTAL_AMOUNT) == null) {
                columns.put(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.totalAmount) + curSymbol);
                widths.add(1f);
            }
            final float[] cwidths = new float[widths.size()];

            for (int i = 0; i< widths.size(); i++) {
                cwidths[i] = widths.get(i);
            }
            baseInvoice.setProductTable(this.getProducTableData(invoice, edsUser, edsCurrency, columns));
            baseInvoice.getProductTable().addTableWidthPercentage(cwidths);
        } else {
            columns.put(ITEM_NO, pdfWfmMessageSource.localize(PdfLocalizationName.number));
            columns.put(ITEM_NAME, pdfWfmMessageSource.localize(PdfLocalizationName.name));
            columns.put(ITEM_DESCRIPTION, pdfWfmMessageSource.localize(PdfLocalizationName.description));
            columns.put(ITEM_QTY_HRS, pdfWfmMessageSource.localize(PdfLocalizationName.qtyOrHrs));
            columns.put(ITEM_UNIT_PRICE, " " + pdfWfmMessageSource.localize(PdfLocalizationName.unitPrice) + " " + curSymbol);
            columns.put(ITEM_DISCOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.discount));
            columns.put(ITEM_NET_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.netAmount) + " " + curSymbol);
            columns.put(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.totalAmount) + " " + curSymbol);
            baseInvoice.setProductTable(getProducTableData(invoice, edsUser, edsCurrency, columns));
            baseInvoice.getProductTable().addTableWidthPercentage(0.5f, 2f, 3.3f, 1f, 1f, 1f, 1f, 1.2f);
        }
        baseInvoice.setTermsConditions(getTermsConditionsTableData(invoice, pdfWfmMessageSource.localize(PdfLocalizationName.paymentInstructions)));
        baseInvoice.setBank(getBankTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setAccount(getAccountTable(edsUser, invoice, getSupplier(clientOrSupplier)));

        if (invoice.getPaymentItems() == null && invoice.getID() != null) {
            invoice.setPaymentItems(EdsInvoice.getPaymentItemsList(invoiceManager.get(invoice.getID())).toArray(new PaymentItem[]{}));
        }
        final Map<String, String> rowsMap = Maps.newLinkedHashMap();

        rowsMap.put(SUBTOTAL, pdfWfmMessageSource.localize(PdfLocalizationName.subTotal));
        rowsMap.put(DISCOUNT_TOTAL, pdfWfmMessageSource.localize(PdfLocalizationName.discountAmount));
        rowsMap.put(TOTAL, pdfWfmMessageSource.localize(PdfLocalizationName.total));
        baseInvoice.setInvoiceTotalTable(this.getTotalTable(edsUser, edsCurrency, invoice, rowsMap));
        return pdfData;
    }

    @Override
    protected <T extends EdsCrmAccount> ITextGenericPdfData getInvoiceDataCustomise(NewInvoice invoice,
                                                                                    EdsUser edsUser,
                                                                                    EdsCurrency edsCurrency,
                                                                                    T clientOrSupplier,
                                                                                    EdsCrmContact clientContact) {
        final ITextGenericPdfData pdfData = new ITextGenericPdfData();
        final ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        final ITextUserData userData = new ITextUserData();

        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        baseInvoice.setObjectId(invoice.getID());
        pdfData.setBaseInvoice(baseInvoice);
        pdfData.setUserData(userData);
        userData.setFullName(edsUser.getFullName());
        if (edsUser.isEmployee()) {
            final EdsEmployee emp = this.getEmployeeManager().get(edsUser.getObjectID());

            userData.setPhone("");
            if (!ServerUtils.isNullOrEmpty(emp.getWorkPhoneFirst())) {
                userData.setPhone(Utils.formatPhoneNumber(this.escapeHtml(emp.getWorkPhoneFirst())));
            }
            userData.setEmail("");
            if (!ServerUtils.isNullOrEmpty(edsUser.getEmail())) {
                userData.setEmail(this.escapeHtml(edsUser.getEmail()));
            }
            userData.setPosition(emp.getPosition() != null ? emp.getPosition().getName() : "");
        }
        pdfData.setCompanyData(this.getCompanyData(edsUser.getCompany(), true, false));
        pdfData.setCreatorData(this.getCreatorData(invoice));
        baseInvoice.setCurrency(this.getCurrencySymbol(edsCurrency, true));
        baseInvoice.setCurrencyName(this.getCurrencyName(edsCurrency));
        baseInvoice.setCustomFooterData(this.getCustomFooterData(edsUser));
        //Bill To Address
        baseInvoice.setCustomBillToAddress(this.getCustomAddressTable(clientOrSupplier, clientContact, invoice, edsUser));
        final String[] numAndDatesCodes = {
                INV_NUMBER, PO_NUMBER,
                REFERENCE,
                INV_DATE,
                INV_DUE_DATE,
                INVOICE_DUE_TERMS,
                INVOICE_STATUS,
                INV_DATE_UNIQUE_FORMAT,
                QRCODE,
                "SHIP_TO_LABEL",
                "SUPPLIER_LABEL",
        };
        final String[] numAndDatesLabels = {
                pdfWfmMessageSource.localize(PdfLocalizationName.invoiceNo),
                pdfWfmMessageSource.localize(PdfLocalizationName.poNumber),
                pdfWfmMessageSource.localize(PdfLocalizationName.reference),
                commonLocalizer.localize(PdfLocalizationName.date),
                pdfWfmMessageSource.localize(PdfLocalizationName.dueDate),
                pdfWfmMessageSource.localize(PdfLocalizationName.paymentTerms),
                pdfWfmMessageSource.localize(PdfLocalizationName.invoiceStatus),
                pdfWfmMessageSource.localize(PdfLocalizationName.shipTo),
                pdfWfmMessageSource.localize(PdfLocalizationName.supplier),
                "INV_DATE_UNIQUE_FORMAT",
                "QR code"
        };
        final String paymentInstructionHeader = this.pdfWfmMessageSource.localize(PdfLocalizationName.paymentInstructions);

        baseInvoice.setCustomTermsConditions(this.getCustomTermsConditionsTableData(invoice, paymentInstructionHeader));
        baseInvoice.setCustomNumberAndDatesTable(this.getCustomNumberAndDatesTable(invoice, edsUser, numAndDatesCodes, numAndDatesLabels));
        baseInvoice.setProductTableName(this.pdfWfmMessageSource.localize(PdfLocalizationName.orderInfomation));
        baseInvoice.setCustomProductTable(this.getCustomProducTableData(invoice, edsUser, edsCurrency));
        baseInvoice.setPaymentHistoryTable(this.getCustomPaymentHistory(invoice, edsUser, edsCurrency));
        baseInvoice.setCustomTotalTable(this.getCustomisedTotalTable(edsUser, edsCurrency, invoice));
        baseInvoice.setCustomBankTable(this.getCustomisedBankTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomAccountTable(this.getCustomisedAccountTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomProductSerialTable(this.getCustomProductSerialTable(invoice, edsUser));
        if (this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PURCHASE_ORDER_CUSTOM_ITEM_RECIEVE_IMPORT)) {
            baseInvoice.setCustomProductArticleTable(this.getCustomProductArticleTable(invoice, edsUser));
            baseInvoice.setCustomLandedCostTable(this.getLandedCostTable(invoice, edsUser));
        }

        if (invoice.getConvertedItemID() != null) {
            baseInvoice.setCustomOutstandingInvoiceTable(getConvertedInvoiceCustomTableData(invoice, edsUser));
        }

        return pdfData;
    }

    private CustomisedITextTable getLandedCostTable(NewInvoice invoiceData, EdsUser edsUser) {
        final CustomisedITextTable landedCostTable = new CustomisedITextTable();
        landedCostTable.addColumn("CATEGORY", "Category");
        landedCostTable.addColumn("BASE_TOTAL", "Base subtotal");

        if (!PURCHASE_INVOICE.equals(this.getFromInvoice())) {
            return landedCostTable;
        }
        final ExpenseListItem[] expenseItemArray = expenseService.getExpenseItemsForPOAllocation(invoiceData.getConvertedItemID());

        if (expenseItemArray == null || expenseItemArray.length == 0) {
            return landedCostTable;
        }
        final DecimalFormat priceScaleNumberFormat = this.getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
        BigDecimal totalCost = BigDecimal.ZERO;

        for (ExpenseListItem expenseListItem : expenseItemArray) {
            if (ServerUtils.isNullOrEmpty(expenseListItem.getCategoryName()) ||
                expenseListItem.getBaseSubtotal() == null ||
                !expenseListItem.isAllocatedToPO()) {
                continue;
            }
            landedCostTable.addRow(expenseListItem.getCategoryName(), priceScaleNumberFormat.format(expenseListItem.getBaseSubtotal()));
            totalCost = totalCost.add(expenseListItem.getBaseSubtotal());
        }
        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            landedCostTable.addRow("Total expenses", priceScaleNumberFormat.format(totalCost));
        }
        return landedCostTable;
    }

    protected CustomisedITextTable getCustomProductArticleTable(NewInvoice invoiceData, EdsUser edsUser) {
        final CustomisedITextTable productArticleTable = new CustomisedITextTable();

        productArticleTable.addColumn(ITEM_NO, pdfWfmMessageSource.localize(PdfLocalizationName.number));
        productArticleTable.addColumn(ITEM_NAME, "Product/Service");
        productArticleTable.addColumn(ITEM_LOT_NUMBER, "Lot Number");
        productArticleTable.addColumn(ITEM_EXPIRATION_DATE, "Expiry Date");
        productArticleTable.addColumn(ITEM_QTY_HRS, "Qty");
        productArticleTable.addColumn(ITEM_UNIT_PRICE, "Unit price");
        if (!PURCHASE_INVOICE.equals(this.getFromInvoice())) {
            return productArticleTable;
        }
        final Map<String, String> customFieldsMap = this.getCustomFieldsAliasValueMap(invoiceData.getCustomItemColumns());
        final SimpleDateFormat dateFormat = this.getCompanyShortDateFormat(edsUser.getCompany());
        final DecimalFormat qtyNumberFormat = getQtyNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
        final List<String> columnsValue = Lists.newArrayList();
        final Table<Integer, String, List<List<String>>> articleLotTable = HashBasedTable.create();

        for (NewInvoiceItem invoiceItem: invoiceData.getItems()) {
            if (invoiceItem.getItemID() == null) {
                continue;
            }
            final List<ProductSerialItem> productSerialItems = productSerialManager.getProductSerialByIds(invoiceItem.getQuoteItemId(),
                                                                                                          invoiceItem.getItemID(),
                                                                                                          PURCHASE_ORDER);
            if (productSerialItems == null || productSerialItems.isEmpty()) {
                continue;
            }
            for (ProductSerialItem serialItem : productSerialItems) {
                columnsValue.clear();
                final String serialNumber = serialItem.getSerial();
                final String lotNumber = serialItem.getLotNumber();
                final String refNumber = serialItem.getRefNumber();
                final String itemName = invoiceItem.getItemName();
                String expirationDate = "";

                if (serialItem.getExpirationDate() != null) {
                    Date date = ServerUtils.convertServerDateToUserDate(serialItem.getExpirationDate(), edsUser.getUserTimezone());
                    expirationDate = dateFormat.format(date);
                }
                if (productArticleTable.containsColumn(ITEM_NAME)) {
                    columnsValue.add(this.escapeHtml(itemName));
                }
                if (productArticleTable.containsColumn(ITEM_LOT_NUMBER)) {
                    columnsValue.add(this.escapeHtml(lotNumber));
                }
                if (productArticleTable.containsColumn(ITEM_EXPIRATION_DATE)) {
                    columnsValue.add(this.escapeHtml(expirationDate));
                }
                if (productArticleTable.containsColumn(ITEM_QTY_HRS)) {
                    columnsValue.add(this.escapeHtml("0"));
                }
                if (productArticleTable.containsColumn(ITEM_UNIT_PRICE) && invoiceItem.getUnitPrice() != null) {
                    columnsValue.add(qtyNumberFormat.format(invoiceItem.getUnitPrice()));
                }
                if (invoiceItem.getCustomFieldItems() != null) {
                    for (CompanyCustomFieldItem ccfi : invoiceItem.getCustomFieldItems()) {
                        if (customFieldsMap.containsKey(ccfi.getFieldName())) {
                            productArticleTable.addColumn(customFieldsMap.get(ccfi.getFieldName()),
                                                          customFieldsMap.get(ccfi.getFieldName()));
                            columnsValue.add(ccfi.getFieldStringValue());
                        }
                    }
                }
                List<List<String>> valueList = articleLotTable.get(invoiceItem.getItemID(), lotNumber);

                if (valueList == null) {
                    valueList = Lists.newArrayList();
                }
                valueList.add(Lists.newArrayList(columnsValue));
                articleLotTable.put(invoiceItem.getItemID(), lotNumber, valueList);
            }
        }
        int i = 1;

        for (Map.Entry<Integer, Map<String, List<List<String>>>> tableEntry : articleLotTable.rowMap().entrySet()) {
            final Integer edsItemId = tableEntry.getKey();

            for (Map.Entry<String, List<List<String>>> lotEntry : tableEntry.getValue().entrySet()) {
                final String uniqLotNumber = lotEntry.getKey();
                final List<List<String>> totalValueList = lotEntry.getValue();

                if (totalValueList == null || totalValueList.isEmpty()) {
                    continue;
                }
                final Integer quantity = totalValueList.size();
                final List<String> valueList = totalValueList.get(0);//for gettting data
                if (valueList == null || valueList.isEmpty()) {
                    continue;
                }
                valueList.set(3, String.valueOf(Optional.ofNullable(quantity).orElse(0)));
                final List<String> newValueList = Lists.newArrayList((i++) + ".");

                newValueList.addAll(valueList);
                productArticleTable.addRow(newValueList.toArray(new String[]{}));
            }
        }
        return productArticleTable;
    }

    private boolean addDiscountColumn(NewInvoice newInvoice) {
        if (newInvoice == null || newInvoice.getItems() == null) {
            return false;
        }
        for (NewInvoiceItem invoiceItem : newInvoice.getItems()) {
            if (Optional.ofNullable(invoiceItem.getDiscountAmount()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0 ||
                    Optional.ofNullable(invoiceItem.getDiscountPercent()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0) {
                return true;
            }
        }
        return false;
    }

    private boolean addTaxColumn(NewInvoice newInvoice) {
        if (newInvoice == null || newInvoice.getItems() == null) {
            return false;
        }
        for (NewInvoiceItem invoiceItem : newInvoice.getItems()) {
            if (Optional.ofNullable(invoiceItem.getTaxAmount()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String getFooterContactText() {
        return pdfWfmMessageSource.localize(PdfLocalizationName.footerInvoiceText);
    }

    @Override
    protected boolean isClient() {
        return false;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.PURCHASE_INVOICE;
    }


    public Map<String, String> getCustomFieldsAliasValueMap(ColumnConfigs[] columnConfigs) {
        if (columnConfigs == null || columnConfigs.length == 0) {
            return Collections.emptyMap();
        }
        final Map<String, String> lineItemCustomField = Maps.newHashMap();

        for (ColumnConfigs column : columnConfigs) {
            if (Objects.equals(column.getCode(), ItemTableConstants.PRODUCT) ||
                Objects.equals(column.getCode(), ItemTableConstants.DESCRIPTION) ||
                Objects.equals(column.getCode(), ItemTableConstants.QTY) ||
                Objects.equals(column.getCode(), ItemTableConstants.MEASUREMENT) ||
                Objects.equals(column.getCode(), ItemTableConstants.UNITPRICE) ||
                Objects.equals(column.getCode(), ItemTableConstants.DISCOUNT_AMT) ||
                Objects.equals(column.getCode(), ItemTableConstants.DEPARTMENT) ||
                Objects.equals(column.getCode(), ItemTableConstants.ACCOUNT) ||
                Objects.equals(column.getCode(), ItemTableConstants.NET_AMT) ||
                Objects.equals(column.getCode(), ItemTableConstants.TAX_LIST) ||
                Objects.equals(column.getCode(), ItemTableConstants.DOUBLE_TAX_LIST) ||
                Objects.equals(column.getCode(), ItemTableConstants.WAREHOUSE) ||
                Objects.equals(column.getCode(), ItemTableConstants.TOTAL_AMT) ||
                Objects.equals(column.getCode(), ItemTableConstants.PROJECT) ||
                Objects.equals(column.getCode(), ItemTableConstants.CLIENT) ||
                Objects.equals(column.getCode(), ItemTableConstants.DISCOUNT_LIST)) {
                continue;
            }
            lineItemCustomField.put(column.getTitle(), column.getTitle());
        }
        return lineItemCustomField;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.purchaseInvoice);
    }
}
