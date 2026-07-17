package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextPdfViewTypeEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextUserData;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: DELL
 * Date: 31-May-2009
 * Time: 07:30:04
 * To change this template use File | Settings | File Templates.
 */
public class SaleInvoiceViewPDFHandler extends BaseInvoicePdfHandler {

    public String getFileName() {
        return SI_FILE_NAME;
    }

    protected Map<String, String> getFileNameParams(Integer objectID) {
        Map<String, String> map = new HashMap<>();
        EdsInvoice invoice = invoiceManager.get(objectID);
        map.put(PDF_CLIENT, invoice.getClientOrSupplier().getName());
        map.put(PDF_CLIENT_CODE, invoice.getClientOrSupplier().getNumber());
        map.put(PDF_NUMBER, invoice.getNumber());
        return map;
    }

    @Override
    protected String getFromInvoice() {
        return SALE_INVOICE;
    }

    @Override
    public <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceData(NewInvoice invoice, EdsUser edsUser, EdsCurrency edsCurrency, ClientOrSupplier clientOrSupplier, EdsCrmContact clientContact) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setPdfViewType(ITextPdfViewTypeEnum.BASEINVOICE);
        pdfData.setTableName(getTableName(invoice) != null ? getTableName(invoice) : pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.salesInvoice));
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        boolean isAddDiscountColumn = addDiscountColumn(invoice);
        boolean isAddTaxColumn = addTaxColumn(invoice);

        Map<String, String> clientData = getBillToAddressMap(clientOrSupplier, clientContact, invoice, false);
        baseInvoice.setClientSupplierData(clientData);

        baseInvoice.setIntroduction(getIntroductionTableData(invoice.getIntroduction(), pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.introduction)));

        HashMap<String, String> numDateTableRowKeys = new HashMap<>();
        numDateTableRowKeys.put(INV_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceNo));
        numDateTableRowKeys.put(QT_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteNumber));
        numDateTableRowKeys.put(PO_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poNumber));
        numDateTableRowKeys.put(REFERENCE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference));
        numDateTableRowKeys.put(COMP_VAT_NUMBER, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxNumber));
        numDateTableRowKeys.put(INV_DATE, commonLocalizer.localize(PdfLocalizationName.invoiceDate));
        if (invoice.getInvoiceTermsItem() != null) {
            numDateTableRowKeys.put(INVOICE_DUE_TERMS, "Terms");
        } else {
            numDateTableRowKeys.put(INV_DUE_DATE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.dueDate));
        }
        numDateTableRowKeys.put(PROJECT_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project));
        numDateTableRowKeys.put(QRCODE, "QR code");
        numDateTableRowKeys.put(INV_DATE_UNIQUE_FORMAT, "Invoice Date Unique Format");
        numDateTableRowKeys.put(QRCODE_GOOGLE, "QR Code");

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        if (fs != null && fs.isShowVatNumberInInvoices()) {
            numDateTableRowKeys.put(COMP_VAT_NUMBER, fs.getTaxIdDisplayNumber() != null ? fs.getTaxIdDisplayNumber() : "");
        }

        baseInvoice.setNumberAndDatesTable(getNumberAndDatesTableData(invoice, edsUser, numDateTableRowKeys/*numDatesColumns, addRowNumDates*/));
        pdfData.setBaseInvoice(baseInvoice);
        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.orderInfomation));
        baseInvoice.setExpenseTableName(commonLocalizer.localizeAccounting(PdfLocalizationName.expense));

        String curSymbol = getCurrencySymbol(edsCurrency, false);
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
                        columns.put(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(invoice.isProjectBasedInvoice() ? PdfLocalizationName.hours : PdfLocalizationName.qty));
                        widths.add(1f);
                        break;
                    case ItemTableConstants.MEASUREMENT:

                        break;
                    case ItemTableConstants.UNITPRICE:
                        columns.put(ITEM_UNIT_PRICE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice) + " " + curSymbol);
                        widths.add(1f);
                        break;
                    case ItemTableConstants.DISCOUNT_AMT:
                        if (isAddDiscountColumn) {
                            columns.put(ITEM_DISCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount));
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
                            columns.put(ITEM_TAX_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxAmount));
                            widths.add(1f);
                        }
                        break;
                    case ItemTableConstants.DOUBLE_TAX_LIST:
                        columns.put(ITEM_DOUBLE_TAX_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxAmount));
                        widths.add(1f);
                        break;
                    case ItemTableConstants.WAREHOUSE:

                        break;
                    case ItemTableConstants.TOTAL_AMT:
                        columns.put(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount) + curSymbol);
                        widths.add(1f);
                        break;
                    case ItemTableConstants.PROJECT:
                        columns.put(PROJECT_NAME, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project));
                        widths.add(1.2f);
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

            if (invoice.isProgressInvoicing()) {
                columns.put(ITEM_EST_AMOUNT, accountingLocalizer.localizeAccounting(PdfLocalizationName.estAmount));
                columns.put(ITEM_PRIOR_AMOUNT, accountingLocalizer.localizeAccounting(PdfLocalizationName.priorAmount));
            }
            columns.put(ITEM_QTY_HRS, pdfWfmMessageSource.localizeAccounting(invoice.isProjectBasedInvoice() ? PdfLocalizationName.hours : PdfLocalizationName.qty));
            columns.put(ITEM_UNIT_PRICE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice) + " " + curSymbol);
            if (isAddDiscountColumn) {
                columns.put(ITEM_DISCOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount));
            }
            if (isAddTaxColumn) {
                columns.put(ITEM_TAX_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.taxAmount));
            }
            columns.put(ITEM_NET_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.netAmount) + " " + curSymbol);
            columns.put(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount) + " " + curSymbol);
            baseInvoice.setProductTable(getProducTableData(invoice, edsUser, edsCurrency, columns));

            if (isAddDiscountColumn && isAddTaxColumn) {
                baseInvoice.getProductTable().addTableWidthPercentage(0.5f, 2f, 3.2f, 1f, 1f, 1f, 1f, 1f, 1.2f);
            } else if (isAddDiscountColumn || isAddTaxColumn) {
                baseInvoice.getProductTable().addTableWidthPercentage(0.5f, 2f, 3.2f, 1f, 1f, 1f, 1f, 1.2f);
            } else {
                baseInvoice.getProductTable().addTableWidthPercentage(0.5f, 2f, 3.3f, 1f, 1f, 1f, 1.2f);
            }
        }

        LinkedHashMap<String, String> columnsExpense = new LinkedHashMap<>();
        columnsExpense.put(ITEM_EXPENSE_CATEGORY, commonLocalizer.localizeAccounting(PdfLocalizationName.category));
        columnsExpense.put(ITEM_EXPENSE_DESCRIPTION, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description));
        columnsExpense.put(ITEM_EXPENSE_TOTAL_AMOUNT, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.totalAmount));
        columnsExpense.put(ITEM_EXPENSE_MARKUP_AMOUNT, "Markup Amount");
        columnsExpense.put(ITEM_EXPENSE_MARKUP_TAX_AMOUNT, "Markup Tax Amount");

        baseInvoice.setExpenseTable(getExpenseTableData(invoice, edsUser, edsCurrency, columnsExpense/*productColumnName, addColumnProduct*/));
        baseInvoice.getExpenseTable().addTableWidthPercentage(0.25f, 0.5f, 0.25f);

        if (invoice.getPaymentItems() == null && invoice.getID() != null) {
            invoice.setPaymentItems(EdsInvoice.getPaymentItemsList(invoiceManager.get(invoice.getID())).toArray(new PaymentItem[]{}));
        }

        LinkedHashMap<String, String> rowsMap = new LinkedHashMap<>();
        rowsMap.put(SUBTOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.subTotal));
        if (isAddDiscountColumn) {
            rowsMap.put(DISCOUNT_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discountAmount));
        }
        rowsMap.put(SHIPPING_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.shipping));
        if (invoice.getBillableExpenseAmount() != null && invoice.getBillableExpenseAmount().compareTo(ZERO) != 0) {
            rowsMap.put(BILL_EXP_TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.billableExpenseTotal));
        }
        if (invoice.getBillableExpenseTaxAmount() != null && invoice.getBillableExpenseTaxAmount().compareTo(ZERO) != 0) {
            rowsMap.put(BILL_EXP_TAX_TOTAL, "Billable Expense Tax Total");
        }
        rowsMap.put(TOTAL, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.total));
        baseInvoice.setInvoiceTotalTable(getTotalTable(edsUser, edsCurrency, invoice, rowsMap));

        if (!SALES_RECEIPT.equals(getFromInvoice())) {
            baseInvoice.setTermsConditions(getTermsConditionsTableData(invoice, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.paymentInstructions)));
            baseInvoice.setBank(getBankTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
            baseInvoice.setAccount(getAccountTable(edsUser, invoice, getSupplier(clientOrSupplier)));

            baseInvoice.setGoogleData(getGoogleLinkAndImgUrl(invoice, edsCurrency, edsUser.getCompany().getObjectID(), false));
            baseInvoice.setPaypallData(getPayPallLinkAndImgUrl(invoice, edsUser.getCompany().getObjectID(), false, true));
            baseInvoice.setStripeData(getStripeLinkAndImgUrl(invoice, edsUser.getCompany().getObjectID(), false, true));
            baseInvoice.setMasterCardData(getMasterCardPaymentURL(invoice, edsUser.getCompany().getObjectID(), false));
            baseInvoice.setElavonData(getElavonPaymentURL(invoice, edsUser.getCompany().getObjectID(), false));
            baseInvoice.setPayMeData(getPayMeData(invoice));
            baseInvoice.setClickData(getClickData(invoice));
            baseInvoice.setRevolutData(getRevolutData(invoice));

        }
        return pdfData;
    }

    @Override
    protected String getFooterContactText() {
        return pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.footerInvoiceText);
    }

    @Override
    protected <ClientOrSupplier extends EdsCrmAccount> ITextGenericPdfData getInvoiceDataCustomise(NewInvoice invoice, EdsUser edsUser, EdsCurrency edsCurrency, ClientOrSupplier clientOrSupplier, EdsCrmContact clientContact) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        ITextUserData userData = new ITextUserData();
        pdfData.setBaseInvoice(baseInvoice);
        pdfData.setUserData(userData);
        EdsCompany edsCompany = edsUser.getCompany();
        // Company Data
        pdfData.setCompanyData(getCompanyData(edsCompany, true, false));
        // Creator Data
        pdfData.setCreatorData(getCreatorData(invoice));
        pdfData.setExtraData(escapeHtml(invoice.getStatus()));
        // User Data
        userData.setFullName(edsUser.getFullName().replace("&", "&amp;"));
        if (edsUser.isEmployee()) {
            EdsEmployee emp = getEmployeeManager().get(edsUser.getObjectID());
            userData.setPhone(Utils.formatPhoneNumber((emp.getWorkPhoneFirst() != null && !emp.getWorkPhoneFirst().equals("")) ? escapeHtml(emp.getWorkPhoneFirst()) : ""));
            userData.setEmail(edsUser.getEmail() != null && !edsUser.getEmail().equals("") ? escapeHtml(edsUser.getEmail()) : "");
            userData.setPosition(emp.getPosition() != null ? emp.getPosition().getName() : "");
        }
        // Set Currency
        baseInvoice.setCurrency(getCurrencySymbol(edsCurrency, true));
        // Set Currency Name
        baseInvoice.setCurrencyName(getCurrencyName(edsCurrency));
        //Set tax calculation type
        baseInvoice.setTaxCalcType(getTaxCalculationType(invoice.getTaxCalculationType()));
        // Set Exchange Rate
        baseInvoice.setExchangeRate(getExchangeRate(invoice));
        //Client code
        EdsCrmAccount client = clientManager.get(invoice.getClientID());
        baseInvoice.setClientCode(client.getNumber() != null ? escapeHtml(client.getNumber()) : "");
        // Calculate total date
        baseInvoice.setTotalDay(invoicingSettingsManager.getInvoiceSettings(edsCompany).getPaymentDue());
        // Get Bill Address
        baseInvoice.setCustomBillToAddress(getCustomAddressTable(clientOrSupplier, clientContact, invoice, edsUser));
        // Set Client/Supplier primary contact secondary address
        baseInvoice.setCustomPrimaryContactAddress(getCustomPrimaryContactAddressTable(clientOrSupplier.getPrimaryContact()));

        baseInvoice.setCustomIntroduction(getCustomIntroductionTableData(invoice.getIntroduction(), pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.introduction)));
        //Set Invoice products sub items by ASSEMBLY & PRODUCT KIT types
        baseInvoice.setCustomProductAssemblyItemsTable(getCustomProductAssemblyItemsTable(invoice.getAssemblyItems(), edsCompany));
        baseInvoice.setCustomProductKitItemsTable(getCustomProductKitItemsTable(invoice.getProductKitItems(), edsCompany));

        // Get Number and Dates
        String[] numAndDatesCodes = {RECEIPT_NO, INV_NUMBER, QT_NUMBER, PO_NUMBER, REFERENCE, INV_DATE, INV_DUE_DATE, QT_DATE, INVOICE_DUE_TERMS, PERIOD, PAYMENT_DATE, INVOICE_STATUS, SHIPPING_METHOD, PERIOD_START_DATE, PERIOD_END_DATE, START_MONTH, END_MONTH, PERIOD_DAYS, QRCODE, INV_DATE_UNIQUE_FORMAT, QRCODE_GOOGLE};
        String[] numAndDatesLabels = {pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reciepNumber),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceNo),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteNumber),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.poNumber),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference),
                commonLocalizer.localize(PdfLocalizationName.invoiceDate),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.dueDate),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quoteDate),
                pdfWfmMessageSource.localize(PdfLocalizationName.paymentTerms),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.period),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.paymentDate),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoiceStatus),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.shippingMethod),
                "Period Start",
                "Period End",
                "Start Month",
                "End Month",
                "Period Days",
                "QR code",
                "Invoice Date Unique Format",
                "QR Code Google"};
        baseInvoice.setCustomNumberAndDatesTable(getCustomNumberAndDatesTable(invoice, edsUser, numAndDatesCodes, numAndDatesLabels));

        baseInvoice.setProductTableName(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.invoice));
        baseInvoice.setCustomProductTable(getCustomProducTableData(invoice, edsUser, edsCurrency));
        baseInvoice.setCustomMultiQuoteAndGdnConverToInvoiceProductTableData(getMultiQuoteAndGdnConverToInvoiceProductTableData(invoice, edsUser));
        baseInvoice.setPaymentHistoryTable(getCustomPaymentHistory(invoice, edsUser, edsCurrency));
        baseInvoice.setExpenseTableName(commonLocalizer.localizeAccounting(PdfLocalizationName.expense));
        baseInvoice.setCustomExpenseTable(getExpenseCustomTableData(invoice, edsUser));
        baseInvoice.setCustomTotalTable(getCustomisedTotalTable(edsUser, edsCurrency, invoice));
        baseInvoice.setCustomDueAmountTable(getDueAmountTable(invoice, edsUser));
        baseInvoice.setCustomPrepaymentTable(getPrepaymentTable(invoice, edsUser));

        baseInvoice.setCustomTermsConditions(getCustomTermsConditionsTableData(invoice, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.paymentInstructions)));
        baseInvoice.setCustomBankTable(getCustomisedBankTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomAccountTable(getCustomisedAccountTableData(edsUser, invoice, getSupplier(clientOrSupplier)));
        baseInvoice.setCustomFooterData(getCustomFooterData(edsUser));
        baseInvoice.setNotes(getInvoiceQuoteNotes(invoice.getHistoryList()));

        //Paypal and Google Chekout Links
        baseInvoice.setGoogleData(getGoogleLinkAndImgUrl(invoice, edsCurrency, edsCompany.getObjectID(), true));
        baseInvoice.setPaypallData(getPayPallLinkAndImgUrl(invoice, edsCompany.getObjectID(), true, true));
        baseInvoice.setStripeData(getStripeLinkAndImgUrl(invoice, edsCompany.getObjectID(), true, true));
        baseInvoice.setMasterCardData(getMasterCardPaymentURL(invoice, edsCompany.getObjectID(), true));
        baseInvoice.setElavonData(getElavonPaymentURL(invoice, edsCompany.getObjectID(), true));
        baseInvoice.setClientApproveData(getInvoiceClientApproveUrl(invoice, edsCompany.getObjectID(), true));
        baseInvoice.setPayMeData(getPayMeData(invoice));
        baseInvoice.setClickData(getClickData(invoice));
        baseInvoice.setRevolutData(getRevolutData(invoice));
        baseInvoice.setCustomClientSupplierEntityCustomFieldTable(getCustomClientSupplierEntityCustomFieldTable(invoice.getCustomFieldItems()));
        baseInvoice.setCustomGroupTaxRateTable(getCustomGroupTaxRateTable(edsUser, invoice, edsCurrency));
        baseInvoice.setCustomProductCategoriesITextTables(getCustomProducCategoriesTableData(invoice, edsUser, edsCurrency));
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_GROUP_BY_ITEM)) {
            baseInvoice.setCustomGroupItemNameForProductTableData(getGroupItemNameForProductTableData(invoice, edsUser));
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_GROUP_BY_ITEM_NAME_AND_ITEM_UNIT_PRICE)) {
                baseInvoice.setCustomGroupItemNameAndUnitPriceTable(getGroupItemNameAndUnitPriceTableData(invoice, edsUser));
            }
        }
        ArrayList<EdsQuote> quotes = new ArrayList<>();
        ArrayList<EdsQuoteItem> itemList = new ArrayList<>();
        Map<String, ArrayList<EdsQuoteItem>> itemMap = new LinkedHashMap<>();
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_GROUP_BY_ITEM) && (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_GROUP_BY_ITEM_NAME_AND_ITEM_TAX_RATE) ||
                genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_DETAILED_ITEMS_FROM_ORDER_BASEINVOICE))) {
            if (invoice.getOrderBaseinvoicedOrderIds() != null) {
                List<String> ids = new ArrayList<>(Arrays.asList(invoice.getOrderBaseinvoicedOrderIds().split(",")));
                List<Integer> idsInt = ids.stream().map(Integer::valueOf).collect(Collectors.toList());
                quotes = (ArrayList<EdsQuote>) quoteManager.get(idsInt);
                ArrayList<EdsQuote> saleQuotesByDetailed = quoteManager.getSaleQuotesByDetailed(idsInt);
                for (EdsQuote quote : saleQuotesByDetailed) {
                    itemList.addAll(quote.getQuoteItems());
                    for (EdsQuoteItem edsQuoteItem: quote.getQuoteItems()) {
                        if (edsQuoteItem.getItemName() != null) {
                            if (itemMap.containsKey(edsQuoteItem.getItemName())) {
                                itemMap.get(edsQuoteItem.getItemName()).add(edsQuoteItem);
                            } else {
                                itemMap.put(edsQuoteItem.getItemName(), new ArrayList<>(Collections.singletonList(edsQuoteItem)));
                            }
                        }
                    }
                }
            }
        }
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_GROUP_BY_ITEM_NAME_AND_ITEM_TAX_RATE)) {
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_GROUP_BY_ITEM)) {
                baseInvoice.setCustomGroupItemNameAndTaxRateTable(getGroupItemNameAndTaxRate(invoice, edsUser));
            }
            baseInvoice.setCustomGroupItemNameTable(getGroupItemName(invoice, edsUser));
        }
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_GROUP_BY_ITEM) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_DETAILED_ITEMS_FROM_ORDER_BASEINVOICE)) {
            baseInvoice.setCustomDetailedItemsFromOrderBaseInvoice(getDetailedItemsFromOrderBaseInvoice(invoice, edsUser, itemList));
            baseInvoice.setCustomTotalForDetailedItemsFromOrderBaseInvoice(getTotalForDetailedItemsFromOrderBaseInvoice(invoice, edsUser, quotes));
        }
        if (invoice.getConvertedItemID() != null) {
            baseInvoice.setCustomOutstandingInvoiceTable(getConvertedInvoiceCustomTableData(invoice, edsUser));
        }

        List<CustomisedITextTable> citList = getCustomFieldTables(invoice.getCustomFieldItems());
        pdfData.setCustomEntityTables(citList);
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


    private CustomisedITextTable getCustomGroupTaxRateTable(EdsUser edsUser, NewInvoice invoiceData, EdsCurrency edsCurrency) {
        boolean isGroupTax = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.GROUP_TAX_ENABLED);
        boolean isDoubleTax = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DOUBLE_TAX_ENABLED);
        if (isGroupTax) {
            CustomisedITextTable resultTable = new CustomisedITextTable();
            resultTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
            Map<Integer, InnerVatClass> taxMap = new HashMap<>();
            DecimalFormat defaultScaleFormat = getDefaultScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
            DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(edsUser.getCompany(), invoiceData.getPdfTemplateID());
            for (int i = 0; i <= invoiceData.getItems().length - 1; i++) {
                NewInvoiceItem item = invoiceData.getItems()[i];
                BigDecimal netAmount = item.getQuantity().multiply(item.getUnitPrice());
                BigDecimal itemDiscount = ZERO;
                if (item.getDiscountPercent() != null) {
                    itemDiscount = netAmount.multiply(item.getDiscountPercent() != null ? item.getDiscountPercent() : ZERO).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                } else if (item.getDiscountAmount() != null) {
                    itemDiscount = item.getDiscountAmount();
                }
                BigDecimal itemDiscount2 = ZERO;
                if (item.getDiscountPercent() != null) {
                    itemDiscount2 = netAmount.multiply(item.getDoubleDiscountPercent() != null ? item.getDoubleDiscountPercent() : ZERO).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                } else if (item.getDoubleDiscountAmount() != null) {
                    itemDiscount2 = item.getDoubleDiscountAmount();
                }
                BigDecimal itemNetAmount = netAmount.subtract(itemDiscount.add(itemDiscount2));
                if (item.getTaxItem() != null && item.getTaxItem().getId() != null) {
                    EdsVat vat = vatManager.get(item.getTaxItem().getId());
                    InnerVatClass vatClass = new InnerVatClass();
                    vatClass.setVat(vat);
                    vatClass.setVatID(vat.getObjectID());

                    BigDecimal taxAmount = item.getTaxAmount();
                    if (taxAmount == null) {
                        BigDecimal taxPercent = (item.getTaxItem() != null && item.getTaxItem().getEffectiveTaxPercent() != null) ? item.getTaxItem().getEffectiveTaxPercent() : ZERO;
                        if (invoiceData.getTaxCalculationType() != null && TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                            taxAmount = itemNetAmount.multiply(taxPercent).divide(HUNDRED.add(taxPercent), 4, RoundingMode.HALF_UP);
                        } else {
                            taxAmount = itemNetAmount.multiply(taxPercent).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                        }
                    }
                    vatClass.setVatAmount(taxAmount);

                    if (vat.getGroupTax()) {
                        for (EdsTaxGroupItem edsTaxGroupItem : vat.getGroupItems()) {
                            EdsVat edsVatItem = edsTaxGroupItem.getItem();
                            InnerVatClass vatItemClass = new InnerVatClass();
                            vatItemClass.setVat(edsVatItem);
                            vatItemClass.setVatID(edsVatItem.getObjectID());
                            BigDecimal taxPercent2 = edsVatItem.getEffectiveRateAsBigDecimal() != null ? edsVatItem.getEffectiveRateAsBigDecimal() : ZERO;
                            if (invoiceData.getTaxCalculationType() != null && TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                                taxAmount = itemNetAmount.multiply(taxPercent2).divide(HUNDRED.add(taxPercent2), 4, RoundingMode.HALF_UP);
                            } else {
                                taxAmount = itemNetAmount.multiply(taxPercent2).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                            }
                            if (taxMap.containsKey(vatItemClass.getVatID())) {
                                InnerVatClass existVatClass = taxMap.get(vatItemClass.getVatID());
                                taxAmount = existVatClass.getVatAmount().add(taxAmount);
                                existVatClass.setVatAmount(taxAmount);
                                taxMap.put(existVatClass.getVatID(), existVatClass);
                            } else {
                                vatItemClass.setVatAmount(taxAmount);
                                taxMap.put(vatItemClass.getVatID(), vatItemClass);
                            }
                        }
                    } else {
                        if (taxMap.containsKey(vatClass.getVatID())) {
                            InnerVatClass existVatClass = taxMap.get(vatClass.getVatID());
                            taxAmount = existVatClass.getVatAmount().add(vatClass.getVatAmount());
                            existVatClass.setVatAmount(taxAmount);
                            taxMap.put(existVatClass.getVatID(), existVatClass);
                        } else {
                            taxMap.put(vatClass.getVatID(), vatClass);
                        }
                    }
                }
                if (isDoubleTax && item.getDoubleTaxItem() != null && item.getDoubleTaxItem().getId() != null) {
                    EdsVat vat = vatManager.get(item.getDoubleTaxItem().getId());
                    InnerVatClass vatClass = new InnerVatClass();
                    vatClass.setVat(vat);
                    vatClass.setVatID(vat.getObjectID());

                    BigDecimal taxAmount = item.getDoubleTaxAmount();
                    if (taxAmount == null) {
                        BigDecimal taxPercent = (item.getDoubleTaxItem() != null && item.getDoubleTaxItem().getEffectiveTaxPercent() != null) ? item.getDoubleTaxItem().getEffectiveTaxPercent() : ZERO;
                        if (invoiceData.getTaxCalculationType() != null && TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                            taxAmount = itemNetAmount.multiply(taxPercent).divide(HUNDRED.add(taxPercent), 4, RoundingMode.HALF_UP);
                        } else {
                            taxAmount = itemNetAmount.multiply(taxPercent).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                        }
                    }
                    vatClass.setVatAmount(taxAmount);

                    if (vat.getGroupTax()) {
                        for (EdsTaxGroupItem edsTaxGroupItem : vat.getGroupItems()) {
                            EdsVat edsVatItem = edsTaxGroupItem.getItem();
                            InnerVatClass vatItemClass = new InnerVatClass();
                            vatItemClass.setVat(edsVatItem);
                            vatItemClass.setVatID(edsVatItem.getObjectID());
                            BigDecimal taxPercent2 = edsVatItem.getEffectiveRateAsBigDecimal() != null ? edsVatItem.getEffectiveRateAsBigDecimal() : ZERO;
                            if (invoiceData.getTaxCalculationType() != null && TAX_CALCULATION_INCLUSIVE.equals(invoiceData.getTaxCalculationType())) {
                                taxAmount = itemNetAmount.multiply(taxPercent2).divide(HUNDRED.add(taxPercent2), 4, RoundingMode.HALF_UP);
                            } else {
                                taxAmount = itemNetAmount.multiply(taxPercent2).divide(HUNDRED, 4, RoundingMode.HALF_UP);
                            }
                            if (taxMap.containsKey(vatItemClass.getVatID())) {
                                InnerVatClass existVatClass = taxMap.get(vatItemClass.getVatID());
                                taxAmount = existVatClass.getVatAmount().add(taxAmount);
                                existVatClass.setVatAmount(taxAmount);
                                taxMap.put(existVatClass.getVatID(), existVatClass);
                            } else {
                                vatItemClass.setVatAmount(taxAmount);
                                taxMap.put(vatItemClass.getVatID(), vatItemClass);
                            }
                        }
                    } else {
                        if (taxMap.containsKey(vatClass.getVatID())) {
                            InnerVatClass existVatClass = taxMap.get(vatClass.getVatID());
                            taxAmount = existVatClass.getVatAmount().add(vatClass.getVatAmount());
                            existVatClass.setVatAmount(taxAmount);
                            taxMap.put(existVatClass.getVatID(), existVatClass);
                        } else {
                            taxMap.put(vatClass.getVatID(), vatClass);
                        }
                    }
                }
            }

            if (taxMap != null && taxMap.size() > 0) {
                for (InnerVatClass groupTaxItem : taxMap.values()) {
                    resultTable.addRow(groupTaxItem.getVat().getName() == null ? "" : (escapeHtml(groupTaxItem.getVat().getName()) + "(" + defaultScaleFormat.format(groupTaxItem.getVat().getTaxRate()) + "%)"),
                            groupTaxItem.getVatAmount() != null ? priceScaleNumberFormat.format(groupTaxItem.getVatAmount()) : "");
                }
                return resultTable;
            }
        }
        return null;
    }


    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.SALES_INVOICE;
    }

    @Override
    protected String getTableName(Object dataClass) {
        EdsProperty property = propertManager.findByCode(Constants.SALE_INVOICE);
        String tableName = property != null ? property.getSingular() : accountingLocalizer.localizeAccounting(PdfLocalizationName.salesInvoice);
        return tableName;
    }
}
