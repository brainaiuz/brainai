package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.ReceiveTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.LeaveRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductPictureManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode.QRCodeGenerator;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ZERO;

/**
 * Created by IntelliJ IDEA.
 * User: xushnud
 * Date: 01-Jun-2010
 * Time: 18:06:19
 */
public class ShippingDataPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    protected CommonService commonService;
    @Autowired
    protected QuoteManager quoteManager;
    @Autowired
    protected ItemTableSettingService itemTableSettingService;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    ItemManager itemManager;
    @Autowired
    private ProductPictureManager productPictureManager;
    private final boolean isGrn;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public ShippingDataPDFHandler(boolean isGrn) {
        this.isGrn = isGrn;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        final LeaveRequestObject requestObject = new LeaveRequestObject();
        final String objectID = request.getParameter("objectID");

        if (objectID != null) {
            requestObject.setObjectID(Integer.valueOf(objectID));
        }
        if (!StringUtil.isEmpty(request.getParameter("pdfTemplateID"))) {
            requestObject.setPdfTemplateID(Integer.valueOf(request.getParameter("pdfTemplateID")));
        }
        return requestObject;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        final RequestObject requestObject = (RequestObject) dataClass;
        final ITextGenericPdfData pdf = new ITextGenericPdfData();
        SimpleDateFormat uniqueDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        final EdsUser user = shippingDataManager.getUser();
        final SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());
        final DecimalFormat qtyNumberFormat = getQtyNumberFormat(user.getCompany(), null);
        final EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        final DecimalFormat calculationNumberFormat = getPriceScaleNumberFormat(fs);
        final EdsShippingData shippingData = shippingDataManager.get(requestObject.getObjectID());
        final ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        final CustomisedITextTable table = new CustomisedITextTable();
        final CustomisedITextTable customFieldTable = new CustomisedITextTable();
        final HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        baseInvoice.setObjectId(shippingData.getObjectID());
        pdf.setBaseInvoice(baseInvoice);
        baseInvoice.setCustomNumberAndDatesTable(table);
        table.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);

        Date currentDate = new Date();
        String date = ServerUtils.shortDateFormat(ServerUtils.getCompanyDate(currentDate, user.getCompany()), user);

        EdsEmployee edsEmployee = shippingData.getCreator() != null ? shippingData.getCreator().getEmployee() != null ? shippingData.getCreator().getEmployee() : null : null;

        EdsDepartment edsDepartment = edsEmployee != null && edsEmployee.getTeam() != null ? edsEmployee.getTeam() : null;
        String departmentEn = "";
        String departmentRu = "";
        String departmentUz = "";
        if (edsDepartment != null && edsDepartment.getLocale() != null) {
            departmentEn = edsDepartment.getLocale().getEnglish() != null && !edsDepartment.getLocale().getEnglish().isEmpty() ? edsDepartment.getLocale().getEnglish() : "N/A";
            departmentRu = edsDepartment.getLocale().getRussian() != null && !edsDepartment.getLocale().getRussian().isEmpty() ? edsDepartment.getLocale().getRussian() : "N/A";
            departmentUz = edsDepartment.getLocale().getUzbek() != null && !edsDepartment.getLocale().getUzbek().isEmpty() ? edsDepartment.getLocale().getUzbek() : "N/A";
        } else if (edsDepartment != null) {
            departmentEn = edsDepartment.getName();
            departmentRu = edsDepartment.getName();
            departmentUz = edsDepartment.getName();
        }

        EdsPosition edsPosition = edsEmployee != null && edsEmployee.getPosition() != null ? edsEmployee.getPosition() : null;
        String positionEn = "";
        String positionRu = "";
        String positionUz = "";
        if (edsPosition != null && edsPosition.getLocale() != null) {
            positionEn = edsPosition.getLocale().getEnglish() != null && !edsPosition.getLocale().getEnglish().isEmpty() ? edsPosition.getLocale().getEnglish() : "N/A";
            positionRu = edsPosition.getLocale().getRussian() != null && !edsPosition.getLocale().getRussian().isEmpty() ? edsPosition.getLocale().getRussian() : "N/A";
            positionUz = edsPosition.getLocale().getUzbek() != null && !edsPosition.getLocale().getUzbek().isEmpty() ? edsPosition.getLocale().getUzbek() : "N/A";
        } else if (edsPosition != null) {
            positionEn = edsPosition.getName();
            positionRu = edsPosition.getName();
            positionUz = edsPosition.getName();
        }

        String grnCreatorPhone = "";
        String grnCreatorEmail = "";
        if (edsEmployee != null) {
            grnCreatorPhone = edsEmployee.getPrimaryPhone() != null ? edsEmployee.getPrimaryPhone() : "N/A";
            grnCreatorEmail = edsEmployee.getEmail() != null ? edsEmployee.getEmail() : "N/A";
        }
        String qrCode = edsEmployee != null ? edsEmployee.getFullName() : "N/A" + "\n" + positionEn + "\n" + departmentEn + "/n" + grnCreatorPhone + "\n" + grnCreatorEmail;

        table.addRowWithCode(CURRENT_DATE_BY_COMPANY_FORMAT, "", escapeHtml(date));
        table.addRowWithCode(CURRENT_TIME, "", escapeHtml(simpleDateFormat.format(user.getUserDate())));
        table.addRowWithCode(CREATED_DATE, "", escapeHtml(shippingData.getCreationTime() != null ? uniqueDateFormat.format(user.getUserDate(shippingData.getCreationTime())) : ""));
        table.addRowWithCode(PHONE_NUMBER, "", escapeHtml(grnCreatorPhone));
        table.addRowWithCode(EMAIL, "", escapeHtml(grnCreatorEmail));
        table.addRowWithCode("DEPARTMENT_EN", "", escapeHtml(departmentEn));
        table.addRowWithCode("DEPARTMENT_RU", "", escapeHtml(departmentRu));
        table.addRowWithCode("DEPARTMENT_UZ", "", escapeHtml(departmentUz));
        table.addRowWithCode("POSITION_EN", "", escapeHtml(positionEn));
        table.addRowWithCode("POSITION_RU", "", escapeHtml(positionRu));
        table.addRowWithCode("POSITION_UZ", "", escapeHtml(positionUz));
        table.addRowWithCode("CREATOR_QR_CODE", "", escapeHtml(QRCodeGenerator.generate(qrCode, 150, 150)));

        if (isGrn) {
            table.addRowWithCode(GRN_NUMBER, pdfWfmMessageSource.localize(PdfLocalizationName.grnNumber), escapeHtml(shippingData.getNumber()));
        } else {
            table.addRowWithCode(GDN_NUMBER, pdfWfmMessageSource.localize(PdfLocalizationName.gdnNumber), escapeHtml(shippingData.getNumber()));
        }
        table.addRowWithCode(PDFConstants.SHIPPING_LABEL, accountingLocalizer.localize(PdfLocalizationName.shippingLabel), escapeHtml(shippingData.getShippingLabel()));
        if (isGrn) {
            table.addRowWithCode(PDFConstants.CLIENT, commonLocalizer.localize(PdfLocalizationName.supplier), shippingData.getCrmAccount() != null ? escapeHtml(shippingData.getCrmAccount().getName()) : "");
        } else {
            table.addRowWithCode(PDFConstants.CLIENT, commonLocalizer.localize(PdfLocalizationName.customer), shippingData.getCrmAccount() != null ? escapeHtml(shippingData.getCrmAccount().getName()) : "");
        }
        table.addRowWithCode(PDFConstants.CLIENT_CODE, pdfWfmMessageSource.localize(PdfLocalizationName.supplierCode), shippingData.getCrmAccount() != null ? escapeHtml(shippingData.getCrmAccount().getNumber()) : "");
        String shippingDate = "";
        if (shippingData.getShippingDate() != null) {
            if (ServerUtils.getUserLocale().getLanguage().equals("ru")) {
                shippingDate = ServerUtils.convertToRuDateFormat(shortDateFormat.format(shippingData.getShippingDate()));
            } else if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                shippingDate = ServerUtils.convertToUzbDateFormat(shortDateFormat.format(shippingData.getShippingDate()));
            } else {
                shippingDate = shortDateFormat.format(shippingData.getShippingDate());
            }
        }
        table.addRowWithCode(PDFConstants.SHIPPING_DATE, commonLocalizer.localize(isGrn ? PdfLocalizationName.receivedDate : PdfLocalizationName.shippedDate), shippingDate);
        table.addRowWithCode(PDFConstants.CURRENCY, accountingLocalizer.localize(PdfLocalizationName.currency), shippingData.getCurrency() != null ? shippingData.getCurrency().getName() : "");
        if (shippingData.getCrmAccount() != null) {
            table.addRowWithCode(CLIENT_CONTACT, commonLocalizer.localize(PdfLocalizationName.contact), shippingData.getCrmAccount().getPrimaryContact() != null ? escapeHtml(shippingData.getCrmAccount().getPrimaryContact().getName()) : "");
            table.addRowWithCode(TITLE, commonLocalizer.localize(PdfLocalizationName.title), shippingData.getCrmAccount().getPrimaryContact() != null ? escapeHtml(shippingData.getCrmAccount().getPrimaryContact().getTitle()) : "");
            table.addRowWithCode(CLIENT_EMAIL, commonLocalizer.localize(PdfLocalizationName.email), escapeHtml(shippingData.getCrmAccount().getEmail()));
            table.addRowWithCode(CLIENT_PHONE, pdfWfmMessageSource.localize(PdfLocalizationName.phone), shippingData.getCrmAccount().getPhone() != null ? escapeHtml(shippingData.getCrmAccount().getPhone().replace("|", "")) : "");
            table.addRowWithCode(CLIENT_FAX, commonLocalizer.localize(PdfLocalizationName.fax), shippingData.getCrmAccount().getFax() != null ? escapeHtml(shippingData.getCrmAccount().getFax().replace("|", "")) : "");
            EdsAddress billAddress = shippingData.getCrmAccount().getBillingAddress();
            if (shippingData.getQuote().getBillAddressID() != null) {
                billAddress = addressManager.get(shippingData.getQuote().getBillAddressID());
            }
            if (billAddress != null) {
                table.addRowWithCode(BILL_ADDRESS_NAME, "", escapeHtml(billAddress.getName()));
                table.addRowWithCode(BILL_ADDRESS, "", escapeHtml(billAddress.getAddress()));
                table.addRowWithCode(BILL_ADDRESS2, "", escapeHtml(billAddress.getAddressb()));
                table.addRowWithCode(BILL_CITY, "", escapeHtml(billAddress.getCity()));
                table.addRowWithCode(BILL_COUNTRY, "", escapeHtml(billAddress.getCountryName()));
                table.addRowWithCode(BILL_ZIPCODE, "", escapeHtml(billAddress.getZipCode()));
            }
            EdsAddress mailAddress = shippingData.getCrmAccount().getMailingAddress();
            if (shippingData.getQuote().getMailAddressID() != null) {
                mailAddress = addressManager.get(shippingData.getQuote().getMailAddressID());
            }
            if (mailAddress != null) {
                table.addRowWithCode("SHIPPING_ADDRESS", commonLocalizer.localize("shippingAddress"), "");
                table.addRowWithCode(MAIL_ADDRESS_NAME, "", escapeHtml(mailAddress.getName()));
                table.addRowWithCode(MAIL_ADDRESS, "", escapeHtml(mailAddress.getAddress()));
                table.addRowWithCode(MAIL_ADDRESS2, "", escapeHtml(mailAddress.getAddressb()));
                table.addRowWithCode(MAIL_CITY, "", escapeHtml(mailAddress.getCity()));
                table.addRowWithCode(MAIL_COUNTRY, "", escapeHtml(mailAddress.getCountryName()));
                table.addRowWithCode(MAIL_ZIPCODE, "", escapeHtml(mailAddress.getZipCode()));
            }
        }
        if (shippingData.getQuote() != null) {
            table.addRowWithCode(INV_DATE, pdfWfmMessageSource.localize(PdfLocalizationName.date), shippingData.getQuote().getInvoiceDate() != null ? shortDateFormat.format(shippingData.getQuote().getInvoiceDate()) : "");
            table.addRowWithCode(INV_DUE_DATE, pdfWfmMessageSource.localize(PdfLocalizationName.dueDate), shippingData.getQuote().getDueDate() != null ? shortDateFormat.format(shippingData.getQuote().getDueDate()) : "");
            table.addRowWithCode(QT_NUMBER, pdfWfmMessageSource.localize(PdfLocalizationName.orderNumber), escapeHtml(shippingData.getQuote().getNumber()));
            table.addRowWithCode(INV_NUMBER, pdfWfmMessageSource.localize(PdfLocalizationName.invoiceNo), shippingData.getQuote().getInvoices() != null && shippingData.getQuote().getInvoices().size() > 0 ? escapeHtml(shippingData.getQuote().getInvoices().get(0).getNumber()) : "");
            table.addRowWithCode(PO_NUMBER, pdfWfmMessageSource.localize(PdfLocalizationName.poNumber), escapeHtml(shippingData.getQuote().getPoNumber()));
            table.addRowWithCode(CREATOR, commonLocalizer.localize(PdfLocalizationName.preparedBy), shippingData.getQuote().getCreator() != null ? escapeHtml(shippingData.getQuote().getCreator().getName()) : "");
            table.addRowWithCode("INTRODUCTION", pdfWfmMessageSource.localize(PdfLocalizationName.introduction), escapeHtml(shippingData.getQuote().getIntroduction()));
            table.addRowWithCode("TERMS_CONDITION", "", escapeHtml(shippingData.getQuote().getPaymentInstruction()));
            table.addRowWithCode(REFERENCE, commonLocalizer.localize(PdfLocalizationName.reference), escapeHtml(shippingData.getQuote().getReference()));
            table.addRowWithCode(PROJECT, commonLocalizer.localize(PdfLocalizationName.projectName), shippingData.getQuote().getRelatedProject() != null ? escapeHtml(shippingData.getQuote().getRelatedProject().getName()) : "");
        }
        table.addRowWithCode(GRN_CREATOR, commonLocalizer.localize(PdfLocalizationName.preparedBy), shippingData.getCreator() != null ? escapeHtml(shippingData.getCreator().getName()) : "");
        table.addRowWithCode(COMP_VAT_NUMBER, commonLocalizer.localize(PdfLocalizationName.vatNumber), fs != null && fs.getTaxIdNumber() != null ? fs.getTaxIdNumber() : "");
        table.addRowWithCode(
                CLIENT_VAT_NUMBER,
                commonLocalizer.localize(PdfLocalizationName.vatNumber),
                shippingData.getCrmAccount() != null
                        ? (StringUtils.isNotBlank(shippingData.getCrmAccount().getTrn())
                        ? escapeHtml(shippingData.getCrmAccount().getTrn())
                        : escapeHtml(shippingData.getCrmAccount().getVatNumber()))
                        : ""
        );
        pdf.setCompanyData(getCompanyData(user.getCompany(), true, hasPhantom));

        if (shippingData.getItems().isEmpty()) {
            return pdf;
        }
        final CustomisedITextTable customisedITextTable = new CustomisedITextTable();

        customisedITextTable.addColumn(ITEM_NAME, commonLocalizer.localize(PdfLocalizationName.item));
        customisedITextTable.addColumn(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        customisedITextTable.addColumn(ITEM_UNIT_MEASUREMENT, pdfWfmMessageSource.localize(PdfLocalizationName.um));
        customisedITextTable.addColumn(ITEM_QTY, commonLocalizer.localize(PdfLocalizationName.receivedQty));
        customisedITextTable.addColumn(ITEM_WAREHOUSE, commonLocalizer.localize(PdfLocalizationName.warehouse));
        customisedITextTable.addColumn(ITEM_RECIEVE, commonLocalizer.localize(PdfLocalizationName.receiveType));
        customisedITextTable.addColumn(ITEM_ALLOCATE, commonLocalizer.localize(PdfLocalizationName.allocate));
        customisedITextTable.addColumn(ITEM_PRODUCT_NAME, pdfWfmMessageSource.localize(PdfLocalizationName.name));
        customisedITextTable.addColumn(ITEM_PROJECT, pdfWfmMessageSource.localize(PdfLocalizationName.project));
        customisedITextTable.addColumn(PROJECT_NUMBER, commonLocalizer.localize(PdfLocalizationName.projectNumber));
        customisedITextTable.addColumn(ITEM_UNIT_PRICE, pdfWfmMessageSource.localize(PdfLocalizationName.unitPrice));
        customisedITextTable.addColumn(ITEM_UNIT_PRICE_IN_BASE, pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice));
        customisedITextTable.addColumn(ITEM_TAX_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.tax));
        customisedITextTable.addColumn(ITEM_NET_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.netAmount));
        customisedITextTable.addColumn(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.totalAmount));
        customisedITextTable.addColumn(ITEM_NO, pdfWfmMessageSource.localize(PdfLocalizationName.number));
        customisedITextTable.addColumn(ITEM_PART_NUMBER, "");
        customisedITextTable.addColumn(QTY, commonLocalizer.localize(PdfLocalizationName.orderedQty));
        customisedITextTable.addColumn("QTY_MINUS_RECEIVED_QTY", "");
        customisedITextTable.addColumn(REFERENCE, pdfWfmMessageSource.localize(PdfLocalizationName.reference));
        customisedITextTable.addColumn(ITEM_PICTURE, "");

        final ColumnConfigs[] columnConfigs = itemTableSettingService.getColumnConfigs(ItemTableEnum.SALE_QUOTE_ITEM);
        final Map<String, String> lineItemCustomField = Maps.newHashMap();

        if (columnConfigs != null) {
            for (ColumnConfigs column : columnConfigs) {
                switch (column.getCode()) {
                    case ItemTableConstants.PRODUCT:
                    case ItemTableConstants.DESCRIPTION:
                    case ItemTableConstants.QTY:
                    case ItemTableConstants.MEASUREMENT:
                    case ItemTableConstants.UNITPRICE:
                    case ItemTableConstants.DISCOUNT_AMT:
                    case ItemTableConstants.DEPARTMENT:
                    case ItemTableConstants.ACCOUNT:
                    case ItemTableConstants.NET_AMT:
                    case ItemTableConstants.TAX_LIST:
                    case ItemTableConstants.DOUBLE_TAX_LIST:
                    case ItemTableConstants.WAREHOUSE:
                    case ItemTableConstants.TOTAL_AMT:
                    case ItemTableConstants.PROJECT:
                    case ItemTableConstants.DISCOUNT_LIST:
                        break;
                    default:
                        lineItemCustomField.put(column.getCode(), column.getTitle());
                        break;
                }
            }
        }
        final List<String> values = Lists.newArrayList();

        BigDecimal subTotal = BigDecimal.ZERO;
        BigDecimal discountTotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;
        BigDecimal netTotal = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal exchangeRate = shippingData.getQuote().getExchangeRate().compareTo(ZERO) != 0 ? shippingData.getQuote().getExchangeRate() : new BigDecimal("1.00");
        int counter = 0;
        for (EdsShippingDataItem item : shippingData.getItems()) {
            final EdsQuoteItem quoteItem = item.getQuoteItem();

            if (quoteItem == null) {
                continue;
            }
            final BigDecimal receivedQty = item.getReceiveType() != ReceiveTypeEnum.RECEIVE_BY_QTY ? item.getReceivedAmount() : item.getReceivedQty();
            String productNumber = "";
            String productName = "";
            String productNumberAndNameConcat = "";
            String pictureUrl = "";
            if (item.getQuoteItem() != null && item.getQuoteItem().getItem() != null) {
                productNumber = item.getQuoteItem().getItem().getProductNumber();
                productName = escapeHtml(item.getQuoteItem().getItem().getName());
                productNumberAndNameConcat = !ServerUtils.isNullOrEmpty(productNumber) ? productNumber + " → " + productName : productName;
                List<EdsProductPicture> pictures = productPictureManager.getProductPictures(item.getQuoteItem().getItem(),0);
                for (EdsProductPicture picture : pictures) {
                    if (picture.isDefaultPicture() != null && picture.isDefaultPicture()) {
                        pictureUrl = uploadManager.getFileURL(picture);
                        break;
                    }
                }
            }
            values.add(productNumberAndNameConcat);
            values.add(escapeHtml(quoteItem.getDescription()));
            values.add(quoteItem.getUnitMeasurement() != null ? escapeHtml(quoteItem.getUnitMeasurement().getName()) : "");
            values.add(qtyNumberFormat.format(Optional.ofNullable(receivedQty).orElse(BigDecimal.ZERO)));
            values.add(item.getWarehouse() != null ? escapeHtml(item.getWarehouse().getName()) : "");
            values.add(item.getReceiveType() != null ? escapeHtml(item.getReceiveType().getTitle()) : "");
            final String allocationAmount = qtyNumberFormat.format(Optional.ofNullable(item.getReceivedAllocation()).orElse(BigDecimal.ZERO));
            values.add(allocationAmount);
            values.add(quoteItem.getItem() != null ? escapeHtml(quoteItem.getItem().getProductNumber()) : "");
            values.add(quoteItem.getProject() != null ? escapeHtml(quoteItem.getProject().getName()) : "");
            values.add(quoteItem.getProject() != null ? escapeHtml(quoteItem.getProject().getNumber()) : "");

            BigDecimal unitPrice = quoteItem.getUnitPrice();
            BigDecimal unitPriceInBase = quoteItem.getUnitPrice().divide(exchangeRate, 5, RoundingMode.HALF_UP);
            BigDecimal netAmount = receivedQty.multiply(unitPrice);
            BigDecimal itemDiscount = BigDecimal.ZERO;
            if (quoteItem.getDiscount() != null) {
                itemDiscount = netAmount.multiply(quoteItem.getDiscount()).divide(AccountingConstants.HUNDRED, 4, RoundingMode.HALF_UP);
            } else if (quoteItem.getDiscountAmount() != null) {
                itemDiscount = quoteItem.getDiscountAmount();
            }
            BigDecimal itemDiscountedNetAmount = netAmount.subtract(itemDiscount);
            String netData = qtyNumberFormat.format(Optional.ofNullable(itemDiscountedNetAmount).orElse(BigDecimal.ZERO));
            String taxAmount = qtyNumberFormat.format(Optional.ofNullable(quoteItem.getTaxAmount()).orElse(BigDecimal.ZERO));
            BigDecimal bigDecimalTaxAmount = Optional.ofNullable(quoteItem.getTaxAmount()).orElse(BigDecimal.ZERO);
            BigDecimal totalAmount = itemDiscountedNetAmount.add(bigDecimalTaxAmount);

            //total table
            subTotal = subTotal.add(netAmount);
            discountTotal = discountTotal.add(itemDiscount);
            taxTotal = taxTotal.add(bigDecimalTaxAmount);
            netTotal = netTotal.add(itemDiscountedNetAmount);
            total = total.add(totalAmount);
            //counter
            counter = counter + 1;
            //unit price
            values.add(qtyNumberFormat.format(Optional.ofNullable(unitPrice).orElse(BigDecimal.ZERO)));
            //unit price in base
            values.add(qtyNumberFormat.format(Optional.ofNullable(unitPriceInBase).orElse(BigDecimal.ZERO)));
            //tax amount
            values.add(taxAmount);
            //net amount
            values.add(netData);
            //total amount
            values.add(qtyNumberFormat.format(Optional.ofNullable(totalAmount).orElse(BigDecimal.ZERO)));
            //no
            values.add(String.valueOf(counter));
            //part number
            values.add(quoteItem.getItem() != null ? escapeHtml(quoteItem.getItem().getPartNumber()) : "");
            //order qty
            values.add(qtyNumberFormat.format(Optional.ofNullable(quoteItem.getQty()).orElse(BigDecimal.ZERO)));
            //shortfall
            BigDecimal receiveQty = item.getReceivedQty() != null ? item.getReceivedQty() : BigDecimal.ZERO;
            BigDecimal qtyMinusRecieveQty = quoteItem.getQty() != null ? quoteItem.getQty().subtract(receiveQty) : BigDecimal.ZERO;
            values.add(qtyNumberFormat.format(Optional.ofNullable(qtyMinusRecieveQty).orElse(BigDecimal.ZERO)));
            values.add(escapeHtml(quoteItem.getReference()));
            values.add(escapeHtml(pictureUrl));

            if (quoteItem.getCustomFields() != null && lineItemCustomField.size() > 0) {
                for (Map.Entry<String, String> stringStringEntry : lineItemCustomField.entrySet()) {
                    String customFieldCode = stringStringEntry.getKey();

                    String customFieldName = stringStringEntry.getValue().replaceAll("&quot;", " &quot;");
                    String customFieldValue = "";
                    if (customFieldCode.contains("string_value")) {
                        customFieldValue = quoteItem.getCustomFields().getStringValue(customFieldCode);
                    }

                    if (lineItemCustomField.containsValue(customFieldName)) {
                        customisedITextTable.addColumn(customFieldName, customFieldName);
                        values.add(customFieldValue);
                    }
                }
            }
            customisedITextTable.addRow(values.toArray(new String[]{}));
            values.clear();
        }

        CustomisedITextTable totalTable = new CustomisedITextTable();
        totalTable.addColumnOrder(PDFConstants.COLUMN_VALUE);
        totalTable.addRowWithCode(SUBTOTAL, qtyNumberFormat.format(Optional.ofNullable(subTotal).orElse(BigDecimal.ZERO)));
        totalTable.addRowWithCode(TAX_TOTAL, qtyNumberFormat.format(Optional.ofNullable(taxTotal).orElse(BigDecimal.ZERO)));
        totalTable.addRowWithCode(DISCOUNT_TOTAL, qtyNumberFormat.format(Optional.ofNullable(discountTotal).orElse(BigDecimal.ZERO)));
        totalTable.addRowWithCode("NET_TOTAL", qtyNumberFormat.format(Optional.ofNullable(netTotal).orElse(BigDecimal.ZERO)));
        totalTable.addRowWithCode(TOTAL, qtyNumberFormat.format(Optional.ofNullable(total).orElse(BigDecimal.ZERO)));

        customData.put("ITEMS", customisedITextTable);
        customFieldTable.setCustomFields(getCustomFields(shippingData));
        customData.put("CUSTOM_FIELD", customFieldTable);
        customData.put("TOTAL_TABLE", totalTable);
        pdf.setUserId(user.getObjectID().toString());
        pdf.setCustomData(customData);

        return pdf;
    }

    private Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(EdsShippingData shippingData) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();

        if (shippingData != null && shippingData.getQuote() != null && shippingData.getQuote().getCustomFields() != null) {
            EdsSaleQuote saleQuote = quoteManager.getSaleQuote(shippingData.getQuote().getObjectID());
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(shippingData.getQuote().getCustomFields(),
                    commonService.getCompanyCustomFields(isGrn ? ViewName.PurchaseOrder : saleQuote.isSalesOrder() ? ViewName.SaleOrder : ViewName.SaleQuote));
            if (customFieldItems != null && customFieldItems.size() > 0) {
                LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
                for (CompanyCustomFieldItem item : customFieldItems) {
                    if (item != null) {
                        Map<String, String> cols = new HashMap<>();
                        cols.put(COLUMN_NAME, item.getFieldName() != null ? escapeHtml(item.getFieldName()) : null);
                        if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                            cols.put(COLUMN_VALUE, item.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate())) : null);
                        } else {
                            cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : null);
                        }
                        if (item.getFieldName() != null) {
                            itemCusFields.put(escapeHtml(item.getFieldName()), cols);
                        }
                    }
                }
                customFields.put(PDFConstants.INVOICE, itemCusFields);
            }
        }
        return customFields;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName((isGrn ? "GoodsReceivedNotes" : "GoodsDeliveredNotes") + user.getName());
    }

    @Override
    protected String getTableName(Object dataClass) {
        String tableName = "";
        if (isGrn) {
            tableName = pdfWfmMessageSource.localize(PdfLocalizationName.goodsReceivedNotes);
        } else {
            tableName = pdfWfmMessageSource.localize(PdfLocalizationName.goodsDeliveredNotes);
        }

        return tableName;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        LeaveRequestObject requestObject = (LeaveRequestObject) object;
        return requestObject.getPdfTemplateID();
    }

    @Override
    public PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        if (isGrn) {
            return PdfReferenceCodeNameEnum.GOODS_RECEIVED_NOTES;
        }
        return PdfReferenceCodeNameEnum.GOODS_DELIVERED_NOTES;
    }
}
