package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsProductPicture;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseItem;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductPictureManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.google.common.collect.Lists;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WarehouseProductsViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private AccountingService accountingService;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ProductPictureManager productPictureManager;
    @Autowired
    protected UserManager userManager;

    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
    WarehouseItem warehouseItem;

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        RequestObject requestObject = new RequestObject();
        requestObject.setObjectID(Integer.parseInt(request.getParameter("objectID")));
        requestObject.setIS_LANDSCAPE(Boolean.valueOf(request.getParameter("IS_LANDSCAPE")));
        warehouseItem = accountingService.getWarehouse(Integer.valueOf(request.getParameter("objectID")));
        return requestObject;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(accountingLocalizer.localizeAccounting(PdfLocalizationName.warehouse));
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        RequestObject requestObject = (RequestObject) dataClass;
        Integer objectId = requestObject.getObjectID();

        ListingFilterParameter filterParameters = new ListingFilterParameter();
        filterParameters.setWarehouseID(objectId);

        ListResult<ProductLocationItem> productItem = accountingService.getWarehouseProductsList(filterParameters);

        String productImageURL = commonService.getRentalProductImageURL(objectId);
        String productPhoto = productImageURL != null ? productImageURL : "";

        final EdsUser user = userManager.getUser();
        String date = format.format(ServerUtils.getCompanyDate(new Date(), user.getCompany()));
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        CustomisedITextTable viewTable = new CustomisedITextTable();
        final HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        if (warehouseItem == null) {
            return pdfData;
        }

        //Warehouse Data Information
        viewTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);

        viewTable.addRowWithCode("NAME", commonLocalizer.localize(PdfLocalizationName.name), escapeHtml(warehouseItem.getName() != null ? warehouseItem.getName() : ""));
        viewTable.addRowWithCode("NUMBER", commonLocalizer.localize(PdfLocalizationName.number), escapeHtml(warehouseItem.getObjectID() != null ? warehouseItem.getObjectID().toString() : ""));
        viewTable.addRowWithCode("OWNERS", commonLocalizer.localize(PdfLocalizationName.assignee), escapeHtml(warehouseItem.getSelectedOwners() != null ? warehouseItem.getSelectedOwners().stream().map(SelectItem::getName).collect(Collectors.joining(", ")) : ""));
        viewTable.addRowWithCode("PRIMARY_CONTACT", commonLocalizer.localize(PdfLocalizationName.contact), escapeHtml(warehouseItem.getContactname() != null ? warehouseItem.getContactname() : ""));
        viewTable.addRowWithCode("EMAIL", commonLocalizer.localize(PdfLocalizationName.email), escapeHtml(warehouseItem.getEmail() != null ? warehouseItem.getEmail() : ""));
        viewTable.addRowWithCode("PHONE", commonLocalizer.localize(PdfLocalizationName.phone), escapeHtml(warehouseItem.getPhone() != null ? warehouseItem.getPhone() : ""));
        viewTable.addRowWithCode("ADDRESS", commonLocalizer.localize(PdfLocalizationName.address), escapeHtml(warehouseItem.getAddress() != null ? warehouseItem.getAddress() : ""));
        viewTable.addRowWithCode("NOTES", commonLocalizer.localize(PdfLocalizationName.description), escapeHtml(warehouseItem.getNotes() != null ? warehouseItem.getNotes() : ""));
        viewTable.addRowWithCode("CURRENT_DATE_BY_COMPANY_FORMAT", commonLocalizer.localize(PdfLocalizationName.date), escapeHtml(date));
        viewTable.addRowWithCode("CURRENT_TIME", "", escapeHtml(simpleDateFormat.format(user.getUserDate())));

        //Product Information
        viewTable.addRowWithCode("IMAGE_URL", commonLocalizer.localize(PdfLocalizationName.image), productPhoto);
        viewTable.addRowWithCode("WAREHOUSE_PRODUCTS", commonLocalizer.localize(PdfLocalizationName.warehouseProducts), "");

        BigDecimal totalProductCostPrice = BigDecimal.ZERO;
        BigDecimal totalProducts = BigDecimal.ZERO;
        for (ProductLocationItem productLocationItem : productItem.getList()) {
            totalProductCostPrice = totalProductCostPrice .add(productLocationItem.getTotal());
            totalProducts = totalProducts .add(productLocationItem.getQty());
        }
        viewTable.addRowWithCode("TOTAL_PRODUCT_COST_PRICE", commonLocalizer.localize(PdfLocalizationName.total), decimalFormat.format(totalProductCostPrice));
        viewTable.addRowWithCode("TOTAL_PRODUCT", commonLocalizer.localize(PdfLocalizationName.product), decimalFormat.format(totalProducts));

        CustomisedITextTable productTable = new CustomisedITextTable();
        productTable.addColumn(IMAGE_URL, commonLocalizer.localize(PdfLocalizationName.image));
        productTable.addColumn(PDFConstants.ITEM_NUMBER, commonLocalizer.localize(PdfLocalizationName.number));
        productTable.addColumn(PDFConstants.ITEM_NAME, commonLocalizer.localize(PdfLocalizationName.name));
        productTable.addColumn(PDFConstants.ITEM_DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        productTable.addColumn(QTY, commonLocalizer.localize(PdfLocalizationName.qty));
        productTable.addColumn("COST_PRICE", commonLocalizer.localize(PdfLocalizationName.price));
        productTable.addColumn(TOTAL, commonLocalizer.localize(PdfLocalizationName.total));

        final List<String> productValues = Lists.newArrayList();

        for (ProductLocationItem locationItem : productItem.getList()) {

            String itemPictureUrl = "";
            List<EdsProductPicture> pictures = productPictureManager.getProductPictures(locationItem.getProductID() != null ? itemManager.get(locationItem.getProductID()) : null, 0);
            for (EdsProductPicture picture : pictures) {
                if (picture.isDefaultPicture() != null && picture.isDefaultPicture()) {
                    itemPictureUrl = uploadManager.getFileURL(picture);
                    break;
                }
            }

            String itemNumber = escapeHtml(locationItem.getProduct_number());
            String itemName = escapeHtml(locationItem.getProductName());
            String itemDescription = escapeHtml(locationItem.getProductLocationDescription());
            String itemQty = escapeHtml(decimalFormat.format(locationItem.getQty())); //temp value
            String costPrice = escapeHtml(decimalFormat.format(locationItem.getAverageCost()));
            String total = escapeHtml(decimalFormat.format(locationItem.getTotal()));

            productValues.add(itemPictureUrl);
            productValues.add(itemNumber);
            productValues.add(itemName);
            productValues.add(itemDescription);
            productValues.add(itemQty);
            productValues.add(costPrice);
            productValues.add(total);
            productTable.addRow(productValues.toArray(new String[]{}));
            productValues.clear();
        }

        customData.put("CUSTOM_FIELD", customFieldData(warehouseItem));
        baseInvoice.setCustomProductTable(productTable);
        baseInvoice.setObjectId(warehouseItem.getObjectID());
        baseInvoice.setCustomNumberAndDatesTable(viewTable);
        pdfData.setCustomData(customData);
        pdfData.setBaseInvoice(baseInvoice);
        pdfData.setUserId(user.getObjectID().toString());
        return pdfData;
    }

    public CustomisedITextTable customFieldData(WarehouseItem ip) {

        EdsCompany company = userManager.getUser().getCompany();
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(company);
        DecimalFormat numberFormat = getPriceScaleNumberFormat(company, null);
        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);

        if (ip != null && ip.getCustomFieldItems() != null && !ip.getCustomFieldItems().isEmpty()) {
            for (CompanyCustomFieldItem fieldItem : ip.getCustomFieldItems()) {
                switch (fieldItem.getDataType()) {
                    case DATA_TYPE_DATE:
                        String dateValue = "";
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                        if (fieldItem.getFieldDateNonConvertedValue() != null) {
                            dateValue = fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                            if (fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate() != null) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate());
                                calendar.add(Calendar.DAY_OF_MONTH, 14);
                            }
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), dateValue, DATA_TYPE_DATE);
                        break;
                    case DATA_TYPE_NUMBER:
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(fieldItem.getFieldStringValue())) {
                            numberValue = escapeHtml(numberFormat.format(Double.valueOf(fieldItem.getFieldStringValue())));
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), numberValue, DATA_TYPE_NUMBER);
                        break;
                    case DATA_TYPE_TEXT:
                        String textValue = "";
                        if (TYPE_ENTITY_LOOKUP.equals(fieldItem.getUiType())) {
                            String defaultValue = "";
                            if (StringUtils.isNotEmpty(fieldItem.getFieldStringValue())) {
                                Integer id = null;
                                try {
                                    id = Integer.valueOf(fieldItem.getFieldStringValue());
                                } catch (final NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                if (id != null && fieldItem.getQueryItems() != null) {
                                    for (final SelectItem selectItem : fieldItem.getQueryItems()) {
                                        if (selectItem.getId().equals(id)) {
                                            defaultValue = escapeHtml(selectItem.getName());
                                            break;
                                        }
                                    }
                                }
                            }
                            customFieldTable.addRowWithCode(fieldItem.getDefaultName(), fieldItem.getAliasName(), escapeHtml(defaultValue));
                        } else if (UI_TYPE_HTML_TEXTAREA.equals(fieldItem.getUiType())) {
                            String html = fieldItem.getFieldStringValue();
                            org.jsoup.nodes.Document doc = Jsoup.parse(html);
                            textValue = doc.body().text();
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(textValue), UI_TYPE_HTML_TEXTAREA);
                        } else {
                            textValue = fieldItem.getFieldStringValue();
                            customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(textValue), UI_TYPE_HTML_TEXTAREA);
                        }
                        break;
                    case DATA_TYPE_PROFILE_IMAGE:
                        String uploadImageId = "";
                        if (fieldItem.getProfielImageId() != null) {
                            uploadImageId = commonService.getImageUrl(fieldItem.getProfielImageId());
                        }
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), uploadImageId, UI_TYPE_PROFILE_IMAGE_WIDGET);
                        break;
                    default:
                        customFieldTable.addRowWithCode(fieldItem.getAliasName(), fieldItem.getAliasName(), escapeHtml(fieldItem.getFieldStringValue()), DATA_TYPE_TEXT);
                        break;
                }

            }
        }
        return customFieldTable;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.WARE_HOUSE_PRODUCT_LIST;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((RequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.warehouseProducts)+ "- "+(warehouseItem.getName() != null ? warehouseItem.getName() : "");
    }
}