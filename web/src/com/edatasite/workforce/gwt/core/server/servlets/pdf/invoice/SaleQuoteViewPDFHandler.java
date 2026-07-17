package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextUserData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: Dilshod
 */
public class SaleQuoteViewPDFHandler extends BaseInvoicePdfHandler {

    protected boolean isSalesOrder;
    @Autowired
    private GenericSettingsManager genericSettingsManager;


    protected String getFromInvoice() {
        return SALE_QUOTE;
    }

    protected Map<String, String> getFileNameParams(Integer objectID) {
        Map<String, String> map = new HashMap<>();
        EdsQuote quote = quoteManager.get(objectID);
        map.put(PDF_CLIENT, quote.getClientOrSupplier().getName());
        map.put(PDF_CLIENT_CODE, quote.getClientOrSupplier().getNumber());
        map.put(PDF_NUMBER, quote.getNumber());
        return map;
    }

    @Override
    public <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceData(NewInvoice invoice, EdsUser edsUser, EdsCurrency edsCurrency, ClientOrSupplier clientOrSupplier, EdsCrmContact clientContact) {
        ITextGenericPdfData invoiceGenericData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        boolean isAddDiscountColumn = addDiscountColumn(invoice);
        boolean isAddDoubleDiscountColumn = addDoubleDiscountColumn(invoice);
        boolean isAddTaxColumn = addTaxColumn(invoice);
        boolean isProjectLineItemEnable = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE);
        invoiceGenericData.setBaseInvoice(baseInvoice);
        invoiceGenericData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        if (isSalesOrder()) {
            invoiceGenericData.setTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.salesOrder));
        } else {
            invoiceGenericData.setTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.salesQuote));
        }

        Map<String, String> clientData = getBillToAddressMap(clientOrSupplier, clientContact, invoice, false);
        baseInvoice.setClientSupplierData(clientData);

        baseInvoice.setIntroduction(getIntroductionTableData(invoice.getIntroduction(), pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.introduction)));

        HashMap<String, String> numDateTableRowKeys = new HashMap<>();
        numDateTableRowKeys.put(INV_NUMBER, isSalesOrder() ? pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderNumber) : pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteNumber));
        numDateTableRowKeys.put(PO_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poNumber));
        numDateTableRowKeys.put(REFERENCE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference));
        numDateTableRowKeys.put(INV_DATE, isSalesOrder() ? pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderDate) : pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteDate));
        numDateTableRowKeys.put(INV_DUE_DATE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.dueDate));
        if (!isProjectLineItemEnable) {
            numDateTableRowKeys.put(PROJECT_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project));
        }
        numDateTableRowKeys.put(OPPORTUNITY_NUMBER, crmLocalizer.localize(PdfLocalizationName.opportunityNumber));

        baseInvoice.setNumberAndDatesTable(getNumberAndDatesTableData(invoice, edsUser, numDateTableRowKeys/*numberDatesColumn, addRowNumberAndDate*/));

        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderInfomation));

        String curSymbol = getCurrencySymbol(edsCurrency, false);
        if (edsUser.getCompany().getObjectID() == 4847) {
            LinkedHashMap<String, String> columns = new LinkedHashMap<>();
            columns.put(ITEM_NO, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number));
            columns.put(ITEM_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name));
            columns.put(ITEM_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
            columns.put(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.qty));
            columns.put(ITEM_UNIT_PRICE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice) + curSymbol);
            columns.put(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount) + curSymbol);
            baseInvoice.setProductTable(getProducTableData(invoice, edsUser, edsCurrency, columns/*productColName, addColumnProduct*/));
            baseInvoice.getProductTable().addTableWidthPercentage(0.5f, 2.5f, 3f, 1.3f, 1.3f, 1.3f);

//            String[] totalColumn = {pdfWfmMessageSource.getMessage(PdfLocalizationName.subTotal),
//                    pdfWfmMessageSource.getMessage(PdfLocalizationName.shipping),
//                    pdfWfmMessageSource.getMessage(PdfLocalizationName.total)};
//            boolean[] addColumnTotal = {true, false, false, true, true, true};

            LinkedHashMap<String, String> rowsMap = new LinkedHashMap<>();
            rowsMap.put(SUBTOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.subTotal));
            rowsMap.put(SHIPPING_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.shipping));
            rowsMap.put(TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.total));
            baseInvoice.setInvoiceTotalTable(getTotalTable(edsUser, edsCurrency, invoice, rowsMap));
        } else {
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

                            break;
                        case ItemTableConstants.UNITPRICE:
                            columns.put(ITEM_UNIT_PRICE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice) + curSymbol);
                            widths.add(1f);
                            break;
                        case ItemTableConstants.COMISSION:
                            columns.put(ITEM_COMISSION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.commission, "Commission"));
                            widths.add(1f);
                        case ItemTableConstants.DISCOUNT_AMT:
                            if (isAddDiscountColumn) {
                                columns.put(ITEM_DISCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount));
                                widths.add(1f);
                            }
                            break;
                        case ItemTableConstants.DOUBLE_DISCOUNT_AMT:
                            columns.put(ITEM_DOUBLE_DISCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount2));
                            widths.add(1f);
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
                        case ItemTableConstants.RECEIPTS:
                            columns.put(RECEIPT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.receipts));
                            widths.add(1f);
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
                for (int i = 0; i < widths.size(); i++) {
                    cwidths[i] = widths.get(i);
                }

                baseInvoice.setProductTable(getProducTableData(invoice, edsUser, edsCurrency, columns));
                baseInvoice.getProductTable().addTableWidthPercentage(cwidths);
            } else {
                columns.put(ITEM_NO, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number));
                columns.put(ITEM_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name));
                columns.put(ITEM_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
                columns.put(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.qty));
                columns.put(ITEM_UNIT_PRICE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice) + curSymbol);
                if (isAddDiscountColumn) {
                    columns.put(ITEM_DISCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount));
                }
                if (isAddDoubleDiscountColumn) {
                    columns.put(ITEM_DOUBLE_DISCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount2));
                }
                if (isAddTaxColumn) {
                    columns.put(ITEM_TAX_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxAmount));
                }
                //      columns.put(ITEM_TAX_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.tax) + curSymbol);
                columns.put(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount) + curSymbol);
                if (isProjectLineItemEnable) {
                    columns.put(PROJECT_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project));
                }
                baseInvoice.setProductTable(getProducTableData(invoice, edsUser, edsCurrency, columns));

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
            if (isAddDiscountColumn) {
                rowsMap.put(DISCOUNT_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discountAmount));
            }
            rowsMap.put(SHIPPING_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.shipping));
            rowsMap.put(TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.total));
            baseInvoice.setInvoiceTotalTable(getTotalTable(edsUser, edsCurrency, invoice, rowsMap));
            rowsMap.put(RECEIPT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.receipts));
        }

        baseInvoice.setTermsConditions(getTermsConditionsTableData(invoice, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.termsAndCondition)));
        baseInvoice.setBank(getBankTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setAccount(getAccountTable(edsUser, invoice, getSupplier(clientOrSupplier)));
        return invoiceGenericData;
    }

    @Override
    protected <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceDataCustomise(NewInvoice invoiceData, EdsUser edsUser, EdsCurrency edsCurrency, ClientOrSupplier clientOrSupplier, EdsCrmContact clientContact) {
        ITextGenericPdfData invoiceGenericData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        invoiceGenericData.setBaseInvoice(baseInvoice);
        invoiceGenericData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        if (isSalesOrder()) {
            invoiceGenericData.setTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.salesOrder));
        } else {
            invoiceGenericData.setTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.salesQuote));
        }
        if (isSalesOrder()) {
            invoiceData.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.SALE_ORDER_ITEM));
        } else {
            invoiceData.setCustomItemColumns(itemTableSettingService.getColumnConfigs(ItemTableEnum.SALE_QUOTE_ITEM));
        }

        //User Data
        ITextUserData userData = new ITextUserData();
        invoiceGenericData.setUserData(userData);
        userData.setFullName(edsUser.getFullName());
        EdsCompany edsCompany = edsUser.getCompany();
        if (edsUser.isEmployee()) {
            EdsEmployee emp = getEmployeeManager().get(edsUser.getObjectID());
            userData.setPhone(Utils.formatPhoneNumber((emp.getWorkPhoneFirst() != null && !emp.getWorkPhoneFirst().equals("")) ? escapeHtml(emp.getWorkPhoneFirst()) : ""));
            userData.setEmail(edsUser.getEmail() != null && !edsUser.getEmail().equals("") ? escapeHtml(edsUser.getEmail()) : "");
            userData.setPosition(emp.getPosition() != null ? emp.getPosition().getName() : "");
        }

        // Company Data
        invoiceGenericData.setCompanyData(getCompanyData(edsUser.getCompany(), true, false));
        //Creator Data
        invoiceGenericData.setCreatorData(getCreatorData(invoiceData));
        // Set Currency
        baseInvoice.setCurrency(getCurrencySymbol(edsCurrency, true));
        // Set Currency Name
        baseInvoice.setCurrencyName(getCurrencyName(edsCurrency));
//         Set Exchange Rate
        baseInvoice.setExchangeRate(getExchangeRate(invoiceData));
        //Client code
        EdsCrmAccount client = clientManager.get(invoiceData.getClientID());
        baseInvoice.setClientCode(client.getNumber() != null ? escapeHtml(client.getNumber()) : "");

        baseInvoice.setCustomBillToAddress(getCustomAddressTable(clientOrSupplier, clientContact, invoiceData, edsUser));

        baseInvoice.setCustomIntroduction(getCustomIntroductionTableData(invoiceData.getIntroduction(), pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.introduction)));

        baseInvoice.setObjectId(invoiceData.getID());

        String[] codes = {INV_NUMBER, PO_NUMBER, INV_DATE, INV_DUE_DATE, INVOICE_STATUS, REFERENCE, SHIPPING_METHOD, PERIOD_DAYS, INVOICE_DUE_TERMS, APPROVER, FROM_QUOTE_NUMBER, QUOTATION_DATE, RECEIPT, INV_DATE_UNIQUE_FORMAT, INV_DUE_DATE_UNIQUE_FORMAT};
        String[] labels = {(isSalesOrder() ? pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderNumber) : pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteNumber)),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poNumber),
                (isSalesOrder() ? pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderDate) : pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteDate)),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.dueDate),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceStatus),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.shippingMethod),
                "Day",
                accountingLocalizer.localizeAccounting(PdfLocalizationName.dueTerms),
                hrmsLocalizer.localizeAccounting(PdfLocalizationName.approver),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.quoteNumber, "Quote Number"),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.quoteDate, "Quotation Date"),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.receipts, "Receipts"),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.orderDate, "Invoice Date Unique Format"),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.dueDate, "Invoice Due Date Unique Format")
        };
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(invoiceData, edsUser, codes, labels));

        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderInfomation));
        baseInvoice.setCustomProductTable(getCustomProducTableData(invoiceData, edsUser, edsCurrency/*, columnCodes, columnLabels*/));
        if (invoiceData != null && invoiceData.getInvoicedItems() != null && invoiceData.getInvoicedItems().length > 0) {
            baseInvoice.setCustomInvoicedItemTable(getCustomInvoicedItemTable(invoiceData));
        }

        baseInvoice.setCustomTotalTable(getCustomisedTotalTable(edsUser, edsCurrency, invoiceData));
        baseInvoice.setCustomBankTable(getCustomisedBankTableData(edsUser, invoiceData, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomAccountTable(getCustomisedAccountTableData(edsUser, invoiceData, getSupplier(clientOrSupplier)));

        baseInvoice.setCustomTermsConditions(getCustomTermsConditionsTableData(invoiceData, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.termsAndCondition)));
        baseInvoice.setCustomFooterData(getCustomFooterData(edsUser));
        baseInvoice.setCustomClientSupplierEntityCustomFieldTable(getCustomClientSupplierEntityCustomFieldTable(invoiceData.getCustomFieldItems()));
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EMPLOYEE_LOOK_UP_CUSTOM_FIELD)) {
            baseInvoice.setCustomEmployeeEntityCustomFieldTable(getCustomEmployeeEntityCustomFieldTable(invoiceData.getCustomFieldItems()));
        }
        baseInvoice.setCustomClientOrSupplierTypeTable(getCustomClientOrSupplierTypeTable(clientOrSupplier));
        baseInvoice.setCustomProductCategoriesITextTables(getCustomProducCategoriesTableData(invoiceData, edsUser, edsCurrency));
        baseInvoice.setCustomItemCustomFieldITextTables(getGroupItemWithCustomField(invoiceData, edsUser, edsCurrency));

        List<CustomisedITextTable> citList = getCustomFieldTables(invoiceData.getCustomFieldItems());
        invoiceGenericData.setCustomEntityTables(citList);

        //Paypal and Google Chekout Links
        baseInvoice.setGoogleData(getGoogleLinkAndImgUrl(invoiceData, edsCurrency, edsCompany.getObjectID(), true));
        baseInvoice.setPaypallData(getPayPallLinkAndImgUrl(invoiceData, edsCompany.getObjectID(), true, false));
        baseInvoice.setStripeData(getStripeLinkAndImgUrl(invoiceData, edsCompany.getObjectID(), true, false));
        baseInvoice.setCustomApproverTable(getCustomApproverData(invoiceData));

        return invoiceGenericData;
    }

    private CustomisedITextTable getCustomInvoicedItemTable(NewInvoice invoice) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        EdsUser user = userManager.getUser();
        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(user.getCompany(), invoice.getPdfTemplateID());
        int scale = 2;
        if (fs != null && fs.getCalculationScale() != null) {
            scale = fs.getCalculationScale();
        }
        CustomisedITextTable productTableData = new CustomisedITextTable();
        productTableData.addColumnOrder(ITEM_NO, INV_DATE, INV_DUE_DATE, ITEM_NAME, ITEM_DESCRIPTION, Constants.PERCENTAGE, SUBTOTAL, TOTAL, "TOTAL_INVOICE_CURRENCY", PROJECT_NAME, ITEM_UNIT_PRICE);
        productTableData.addHeaderColumns(
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number),
                commonLocalizer.localize(PdfLocalizationName.startDate),
                commonLocalizer.localize(PdfLocalizationName.endDate),
                commonLocalizer.localize("item"),
                commonLocalizer.localize(PdfLocalizationName.description),
                commonLocalizer.localize("percentage"),
                commonLocalizer.localize(PdfLocalizationName.subtotal),
                commonLocalizer.localize(PdfLocalizationName.total),
                "Total Invoice Currency",
                commonLocalizer.localize(PdfLocalizationName.project),
                "Quote Unit Price"
        );
        int count = 0;
        for (NewInvoice item : invoice.getInvoicedItems()) {
            count = count + 1;
            String invoiceDate = item.getInvoiceDate() != null ? shortDateFormat.format(item.getInvoiceDate().getNonConvertedDate()) : "";
            String dueDate = item.getDueDate() != null ? shortDateFormat.format(item.getDueDate().getNonConvertedDate()) : "";
            String itemName = item.getName() != null ? item.getName() : "";
            String description = item.getDescription() != null ? item.getDescription() : "";
            String percentage = item.getPercentage() != null ? priceScaleNumberFormat.format(item.getPercentage()) : "";
            String subtotal = item.getSubtotal() != null ? priceScaleNumberFormat.format(item.getSubtotal()) : "";
            String total = item.getTotalInInvoiceCurrency() != null ? priceScaleNumberFormat.format(item.getTotalInInvoiceCurrency()) : "";
            String totalInvoiceCurrency = item.getTotalInInvoiceCurrency() != null ? priceScaleNumberFormat.format(item.getTotalInInvoiceCurrency()) : "";
            String projectName = item.getProjectName() != null ? item.getProjectName() : "";
            String quoteUnitPrice = item.getUnitPrice() != null ? priceScaleNumberFormat.format(item.getUnitPrice()) : "";

            productTableData.addRow(count + "", invoiceDate, dueDate, itemName, description, percentage, subtotal, total, totalInvoiceCurrency, projectName, quoteUnitPrice);
        }
        return productTableData;
    }

    private boolean addDiscountColumn(NewInvoice newInvoice) {
        if (newInvoice != null) {
            if (newInvoice.getItems() != null && newInvoice.getItems().length > 0) {
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

    private boolean addDoubleDiscountColumn(NewInvoice newInvoice) {
        if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DOUBLE_DISCOUNT_ENABLE)) {
            return false;
        }
        if (newInvoice != null) {
            if (newInvoice.getItems() != null && newInvoice.getItems().length > 0) {
                for (NewInvoiceItem invoiceItem : newInvoice.getItems()) {
                    if ((invoiceItem.getDoubleDiscountAmount() != null && invoiceItem.getDoubleDiscountAmount().compareTo(BigDecimal.ZERO) > 0) ||
                            (invoiceItem.getDoubleDiscountPercent() != null && invoiceItem.getDoubleDiscountPercent().compareTo(BigDecimal.ZERO) > 0)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private boolean addTaxColumn(NewInvoice newInvoice) {
        if (newInvoice != null) {
            if (newInvoice.getItems() != null && newInvoice.getItems().length > 0) {
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
        return pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.footerSaleQuoteText);
    }

    public String getFileName() {
        if (isSalesOrder()) {
            return SO_FILE_NAME;
        } else {
            return SQ_FILE_NAME;
        }
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return isSalesOrder() ? PdfReferenceCodeNameEnum.SALES_ORDER : PdfReferenceCodeNameEnum.SALES_QUOTE;
    }

    protected boolean isSalesOrder() {
        return isSalesOrder;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return accountingLocalizer.localizeAccounting(PdfLocalizationName.salesQuote);
    }
}
