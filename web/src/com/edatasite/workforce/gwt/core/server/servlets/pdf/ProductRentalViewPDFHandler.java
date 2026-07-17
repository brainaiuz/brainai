package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.RentalProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceQuoteRequestObject;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductRentalViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private ProductService productService;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CommonService commonService;

    DecimalFormat decimalFormat = new DecimalFormat(",##0.00");

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        InvoiceQuoteRequestObject requestObject = new InvoiceQuoteRequestObject();
        requestObject.setObjectID(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        RequestObject requestObject = (RequestObject) dataClass;
        setFileName("Rental Product");
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        RequestObject requestObject = (RequestObject) dataClass;
        Integer objectId = requestObject.getObjectID();
        EdsAccount edsAccount = accountingManager.get(objectId);
        EdsUser user = accountingManager.getUser();
        EdsItem item = itemManager.get(objectId);

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);

        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(user.getCompany());


        NewProduct productItem = productService.getRentalProductEditData(objectId, true);
        String employeeImageURL = commonService.getRentalProductImageURL(objectId);
        String profilePhoto = employeeImageURL != null ? employeeImageURL : "";

        if (productItem == null) {
            return pdfData;
        }


        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        CustomisedITextTable viewTable = new CustomisedITextTable();
        final HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        StringBuilder suppliersString = new StringBuilder();
        if (productItem.getSuppliers() != null) {
            for (int i = 0; i < productItem.getSuppliers().length; i++) {
                suppliersString.append(productItem.getSuppliers()[i].getName()).append(",");
            }
        }
        viewTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);
        viewTable.addRowWithCode("NAME", commonLocalizer.localize(PdfLocalizationName.name), escapeHtml(productItem.getItemName() != null ? productItem.getItemName() : ""));
        viewTable.addRowWithCode("NUMBER", commonLocalizer.localize(PdfLocalizationName.number), escapeHtml(productItem.getNumberData() != null ? productItem.getNumberData().getNumberString() : ""));
        viewTable.addRowWithCode("ACTIVE", commonLocalizer.localize(PdfLocalizationName.active), escapeHtml(productItem.isActive() != null ? productItem.isActive().toString() : ""));
        viewTable.addRowWithCode("CATEGORY", commonLocalizer.localize(PdfLocalizationName.category), escapeHtml(productItem.getProductCategories() != null ? productItem.getCategoryName() : ""));
        viewTable.addRowWithCode("BRAND", commonLocalizer.localize(PdfLocalizationName.brand), escapeHtml(productItem.getBrands() != null ? productItem.getBrandName() : ""));
        viewTable.addRowWithCode("SUPPLIERS", commonLocalizer.localize(PdfLocalizationName.suppliers), escapeHtml(suppliersString != null ? suppliersString.toString() : ""));
        viewTable.addRowWithCode("BARCODE", commonLocalizer.localize(PdfLocalizationName.barcode), escapeHtml(productItem.getBarCodeText() != null ? productItem.getBarCodeText() : ""));
        viewTable.addRowWithCode("PURCHASE_PRICE", commonLocalizer.localize("purchasePrice"), escapeHtml(productItem.getUnitPrice() != null ? priceScaleFormat.format(productItem.getUnitPrice()) : ""));
        viewTable.addRowWithCode("SALES_PRICE", commonLocalizer.localize("sellingPrice"), escapeHtml(productItem.getSellingPrice() != null ? priceScaleFormat.format(productItem.getSellingPrice()) : ""));
        viewTable.addRowWithCode("PURCHASE_ACCOUNT", commonLocalizer.localize("purchaseAccount"), escapeHtml(productItem.getCogsAccount() != null ? productItem.getCogsAccount().getName() : ""));
        viewTable.addRowWithCode("SALES_ACCOUNT", commonLocalizer.localize("salesAccount"), escapeHtml(productItem.getAccountId() != null ? productItem.getAccountItem().getName() : ""));
        viewTable.addRowWithCode("TAX", commonLocalizer.localize(PdfLocalizationName.taxRate), escapeHtml(productItem.getTaxItem() != null ? productItem.getTaxItem().getName() : ""));
        viewTable.addRowWithCode("OVERTIME_END_DATE", commonLocalizer.localize("extraDay"), escapeHtml(productItem.getExtraDay() != null ? priceScaleFormat.format(productItem.getExtraDay()) : ""));
        viewTable.addRowWithCode("OVERTIME_HOURS", commonLocalizer.localize("extraHour"), escapeHtml(productItem.getExtraHour() != null ? priceScaleFormat.format(productItem.getExtraHour()) : ""));
        viewTable.addRowWithCode("SECURITY_TIME", commonLocalizer.localize("securityTime"), escapeHtml(productItem.getSecurityTime() != null ? priceScaleFormat.format(productItem.getSecurityTime()) : ""));
        viewTable.addRowWithCode("TYPE", commonLocalizer.localize(PdfLocalizationName.type), escapeHtml(productItem.getItemName() != null ? "Consumable" : ""));
        viewTable.addRowWithCode("IMAGE_UPLOAD", commonLocalizer.localize(PdfLocalizationName.image), profilePhoto);
        viewTable.addRowWithCode("ADDITIONAL_INFORMATION", pdfWfmMessageSource.localize("additionalInformation"), "");


        CustomisedITextTable productTable = new CustomisedITextTable();
        productTable.addColumn(UNIT, commonLocalizer.localize(PdfLocalizationName.units));
        productTable.addColumn(ITEM_UNIT_PRICE, pdfWfmMessageSource.localize(PdfLocalizationName.price));
        productTable.addColumn(DESCRIPTION, pdfWfmMessageSource.localize(PdfLocalizationName.description));

        final List<String> values = Lists.newArrayList();
        for (RentalProductItem items : productItem.getRentalProductItems()) {
            String itemUnit = escapeHtml(items.getUnitCode() != null ? items.getUnitCode() : "");
            String itemPrice = escapeHtml(items.getPrice() != null ? priceScaleFormat.format(items.getPrice()) : "");
            String itemDesc = escapeHtml(items.getDescription() != null ? items.getDescription() : "");

            values.add(itemUnit);
            values.add(itemPrice);
            values.add(itemDesc);

            productTable.addRow(values.toArray(new String[]{}));
            values.clear();
        }

        baseInvoice.setCustomProductTable(productTable);
        baseInvoice.setCustomNumberAndDatesTable(viewTable);
        customData.put("CUSTOM_FIELD", getCustomField(productItem));
        pdfData.setCustomData(customData);
        pdfData.setBaseInvoice(baseInvoice);
        return pdfData;
    }

    private CustomisedITextTable getCustomField(NewProduct customFielditem) {
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (customFielditem.getProductCustomFieldItems() != null && customFielditem.getProductCustomFieldItems().size() > 0) {
            SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem field : customFielditem.getProductCustomFieldItems()) {
                if (field != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, escapeHtml(field.getFieldName()));
                    if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, field.getFieldDateNonConvertedValue() != null ? escapeHtml(shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate())) : "—");
                    } else if (CompanyCustomFieldItem.NUMBER.equals(field.getDataType())) {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(decimalFormat.format(Double.valueOf(field.getFieldStringValue()))) : "—");
                    } else {
                        cols.put(COLUMN_VALUE, StringUtils.isNotEmpty(field.getFieldStringValue()) ? escapeHtml(field.getFieldStringValue()) : "—");
                    }
                    if (field.getFieldName() != null) {
                        itemCusFields.put(field.getFieldName(), cols);
                    }
                }
            }
            customFields.put("RENTAL_PRODUCT", itemCusFields);
            customFieldTable.setCustomFields(customFields);
        }

        return customFieldTable;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return "Rental Product";
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.RENTAL_PRODUCT;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object != null && object instanceof InvoiceQuoteRequestObject) {
            return ((InvoiceQuoteRequestObject) object).getTemplateID();
        }
        return null;
    }
}
