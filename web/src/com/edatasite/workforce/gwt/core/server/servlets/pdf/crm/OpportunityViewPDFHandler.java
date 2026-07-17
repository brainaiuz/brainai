package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsProductPicture;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CrmAccountRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductPictureManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.qrcode.QRCodeGenerator;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.*;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Azam on 08/16/2020.
 * Created date: 10:59
 */
public class OpportunityViewPDFHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    @Qualifier("wfmLocalizer")
    private WfmMessageSource wfmLocalizer;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ProductPictureManager productPictureManager;
    protected SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        CrmAccountRequestObject requestObject = new CrmAccountRequestObject();
        String objectID = request.getParameter("objectID");

        if (objectID != null && !StringUtils.isEmpty(objectID)) {
            requestObject.setObjectID(Integer.valueOf(objectID));
        }
        if (StringUtils.isNotBlank(request.getParameter("pdfTemplateID"))) {
            requestObject.setPdfTemplateId(Integer.valueOf(request.getParameter("pdfTemplateID")));
        }
        return requestObject;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("opportunity");
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        CrmAccountRequestObject requestObject = (CrmAccountRequestObject) dataClass;
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        final CustomisedITextTable customFieldTable = new CustomisedITextTable();

        EdsUser user = uploadManager.getUser();
        Integer objectId = requestObject.getObjectID();
        OpportunityListItem item = crmServiceLocal.getOpportunity(objectId);
        if (item == null) {
            return pdfData;
        }

        customData.put("OPPORTUNITY_INFORMATION", getOpportunityInformation(item, user));
        customData.put("CUSTOM_TOTAL_TABLE", getTotalTableData(item, user));
        customData.put("CUSTOMER_DETAIL", getCustomerDetails(item.getCrmAccountItem(), user));
        customData.put("CUSTOM_TABLE_ITEMS", getCustomTableItems(item));

        customFieldTable.setCustomFields(getCustomFields(item));
        customData.put("CUSTOM_FIELD", customFieldTable);
        customData.put("ATTACHMENTS_TABLE", customAttachmentsTable(item));
        customData.put("CREATION_DATA", getCreationData(item));

        pdfData.setCustomData(customData);

        baseInvoice.setCustomProductTable(getCustomProductTable(item, user));
        baseInvoice.setObjectId(item.getObjectId());
        baseInvoice.setCustomProductCategoriesITextTables(getCustomProducCategoriesTableData(item, user));
        pdfData.setBaseInvoice(baseInvoice);
        pdfData.setUserId(user.getObjectID().toString());

        return pdfData;
    }

    private CustomisedITextTable customAttachmentsTable(OpportunityListItem item) {
        CustomisedITextTable attachmentsTable = new CustomisedITextTable();
        attachmentsTable.addColumnOrder("FILE_NAME", "FILE_DOWNLOAD_URL");
        List<FileResource> fileResources = attachmentUtilsManager.getAttachments(F_OPPORTUNITY, item.getObjectId(), item.getObjectId());

        for (FileResource attachment : fileResources) {
            String fileName = escapeHtml(attachment.getEncodedName());
            String fileDownloadURL = escapeHtml(getDownloadURL(attachment));
            attachmentsTable.addRow(fileName, fileDownloadURL);
        }
        return attachmentsTable;
    }

    private String getDownloadURL(FileResource fileResource) {
        String url = "";
        if (Constants.GOOGLE.equals(fileResource.getUploadType())) {
            url = fileResource.getGoogleDownloadLink();
        } else if (Constants.OFFICE_365.equals(fileResource.getUploadType()) || Constants.OFFICE_365_SHARE_POINT.equals(fileResource.getUploadType())) {
            url = fileResource.getOfficeDownloadLink();
        } else {
            url = fileResource.getAmazonLink();
        }
        return StringUtils.isNotEmpty(url) ? url : "";
    }

    private CustomisedITextTable getCustomTableItems(OpportunityListItem item) {
        CustomisedITextTable customTableItems = new CustomisedITextTable();
        if (item.getCustomTableItems() == null || item.getCustomTableItems().size() == 0) {
            return customTableItems;
        }
        customTableItems.addColumn("PRODUCT_NAME_FOR_SAMPLE", "");
        customTableItems.addColumn("SUPPLIER", "");
        customTableItems.addColumn("REQUTESTED_DATE", "");
        customTableItems.addColumn("DELIVERED_DATE", "");
        customTableItems.addColumn("SAMPLE_STATUS", "");
        customTableItems.addColumn("UPDATES_AND_PROJECT_DETAILS", "");

        for (CustomTableRpc tableRpc : item.getCustomTableItems().get("ITEM_TABLE_c4gUTtHWRw")) {
            String itemSuplementory = "";
            String umSuplementory = "";
            String qtySuplementory = "";
            String supplierSuplementory = "";
            String description = "";
            String requestedDate = "";
            String deliveredDate = "";
            String samplestatus = "";
            String updatesAndProjectDetails = "";
            for (CompanyCustomFieldItem fieldItem : tableRpc.getItemCustomFields()) {
                if (fieldItem.getAliasName().equalsIgnoreCase("ItemSuplementory")) {
                    itemSuplementory = escapeHtml(fieldItem.getFieldStringValue());
                }
                if (fieldItem.getAliasName().equalsIgnoreCase("UMSuplementory")) {
                    umSuplementory = escapeHtml(fieldItem.getFieldStringValue());
                }
                if (fieldItem.getAliasName().equalsIgnoreCase("QtySuplementory")) {
                    qtySuplementory = escapeHtml(fieldItem.getFieldStringValue());
                }
                if (fieldItem.getAliasName().equalsIgnoreCase("SupplierSuplementory")) {
                    supplierSuplementory = escapeHtml(fieldItem.getFieldStringValue());
                }
                if (fieldItem.getAliasName().equalsIgnoreCase("Description")) {
                    description = escapeHtml(fieldItem.getFieldStringValue());
                }
                if (fieldItem.getAliasName().equalsIgnoreCase("Requested Date")) {
                    requestedDate = fieldItem.getFieldDateNonConvertedValue() != null ? dateFormat(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                }
                if (fieldItem.getAliasName().equalsIgnoreCase("Delivered Date")) {
                    deliveredDate = fieldItem.getFieldDateNonConvertedValue() != null ? dateFormat(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                }
                if (fieldItem.getAliasName().equalsIgnoreCase("SAMPLE STATUS")) {
                    samplestatus = escapeHtml(fieldItem.getFieldStringValue());
                }
                if (fieldItem.getAliasName().equalsIgnoreCase("Updates & Project Details")) {
                    updatesAndProjectDetails = escapeHtml(fieldItem.getFieldStringValue());
                }
            }
            customTableItems.addRow(itemSuplementory, supplierSuplementory, requestedDate, deliveredDate, samplestatus, updatesAndProjectDetails);
        }

        return customTableItems;
    }

    private CustomisedITextTable getOpportunityInformation(OpportunityListItem item, EdsUser user) {
        DecimalFormat numberFormat = getPriceScaleNumberFormat(user.getCompany(), null);
        SimpleDateFormat uniqueDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat uniqueDateFormatForQr = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        CustomisedITextTable opportunityInformationData = new CustomisedITextTable();
        opportunityInformationData.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);

        String opportunityName = escapeHtml(item.getOpportunityName());
        String currency = escapeHtml(item.getCurrency());
        String baseCurrency = escapeHtml(item.getBaseCurrencyName());
        String opportunityNumber = item.getNumberData() != null ? escapeHtml(item.getNumberData().getNumberString()) : "";
        String contact = escapeHtml(item.getContact());
        String contactPhone = escapeHtml(item.getContactPrimaryPhone());
        String contactEmail = escapeHtml(item.getContactPrimaryEmail());
        String assignee = escapeHtml(item.getAssignee());
        String stage = escapeHtml(item.getStageName());
        String amount = item.getAmount() != null ? numberFormat.format(item.getAmount()) : BigDecimal.ZERO.toString();
        String closeDate = item.getClosingDate() != null ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getClosingDate())) : dateFormat(item.getClosingDate()) : "";
        String closeDateUniqueFormat = item.getClosingDate() != null ? uniqueDateFormat.format(item.getClosingDate()) : "";
        EdsEmployee edsEmployee = item.getAssigneeId() != null ? employeeManager.get(item.getAssigneeId()) : null;
        EdsDepartment edsDepartment = edsEmployee != null && edsEmployee.getTeam() != null ? departmentManager.get(edsEmployee.getTeam().getObjectID()) : null;
        String departmentEn = "";
        String departmentRu = "";
        String departmentUz = "";
        if (edsDepartment != null && edsDepartment.getLocale() != null) {
            departmentEn = edsDepartment.getLocale().getEnglish() != null && !edsDepartment.getLocale().getEnglish().isEmpty() ? edsDepartment.getLocale().getEnglish() : "-";
            departmentRu = edsDepartment.getLocale().getRussian() != null && !edsDepartment.getLocale().getRussian().isEmpty() ? edsDepartment.getLocale().getRussian() : "-";
            departmentUz = edsDepartment.getLocale().getUzbek() != null && !edsDepartment.getLocale().getUzbek().isEmpty() ? edsDepartment.getLocale().getUzbek() : "-";
        } else if (edsDepartment != null) {
            departmentEn = edsDepartment.getName();
            departmentRu = edsDepartment.getName();
            departmentUz = edsDepartment.getName();
        }

        EdsPosition edsPosition = edsEmployee != null && edsEmployee.getPosition() != null ? positionManager.get(edsEmployee.getPosition().getObjectID()) : null;
        String positionEn = "";
        String positionRu = "";
        String positionUz = "";
        if (edsPosition != null && edsPosition.getLocale() != null) {
            positionEn = edsPosition.getLocale().getEnglish() != null && !edsPosition.getLocale().getEnglish().isEmpty() ? edsPosition.getLocale().getEnglish() : "-";
            positionRu = edsPosition.getLocale().getRussian() != null && !edsPosition.getLocale().getRussian().isEmpty() ? edsPosition.getLocale().getRussian() : "-";
            positionUz = edsPosition.getLocale().getUzbek() != null && !edsPosition.getLocale().getUzbek().isEmpty() ? edsPosition.getLocale().getUzbek() : "-";
        } else if (edsPosition != null) {
            positionEn = edsPosition.getName();
            positionRu = edsPosition.getName();
            positionUz = edsPosition.getName();
        }


        String assigneePhone = "";
        String assigneeEmail = "";
        if (edsEmployee != null) {
            assigneePhone = edsEmployee.getPrimaryPhone() != null ? edsEmployee.getPrimaryPhone() : "N/A";
            assigneeEmail = edsEmployee.getEmail() != null ? edsEmployee.getEmail() : "N/A";
        }
        String createdDate = item.getCreatedDate() != null ? uniqueDateFormatForQr.format(item.getCreatedDate()) : "";
        String assigneeQrCode = assignee + "\n" + positionEn + "\n" + departmentEn + "\n" + assigneePhone + "\n" + assigneeEmail;

        Date currentDate = new Date();
        String date = ServerUtils.shortDateFormat(ServerUtils.getCompanyDate(currentDate, user.getCompany()), user);

        opportunityInformationData.addRowWithCode(CURRENT_TIME, "" , timeFormat.format(user.getUserDate()));
        opportunityInformationData.addRowWithCode(CURRENT_DATE_BY_COMPANY_FORMAT, "", date);

        opportunityInformationData.addRowWithCode("OPPORTUNITY_NAME", pdfWfmMessageSource.localize(PdfLocalizationName.name), opportunityName);
        opportunityInformationData.addRowWithCode("CURRENCY", pdfWfmMessageSource.localize(PdfLocalizationName.currency), currency);
        opportunityInformationData.addRowWithCode("BASE_CURRENCY", pdfWfmMessageSource.localize(PdfLocalizationName.currency), baseCurrency);
        opportunityInformationData.addRowWithCode("OPPORTUNITY_NUMBER", pdfWfmMessageSource.localize(PdfLocalizationName.Qnumber), opportunityNumber);
        opportunityInformationData.addRowWithCode("CONTACT", pdfWfmMessageSource.localize(PdfLocalizationName.contact), contact);
        opportunityInformationData.addRowWithCode("CONTACT_PHONE", commonLocalizer.localize(PdfLocalizationName.phone), contactPhone);
        opportunityInformationData.addRowWithCode("CONTACT_EMAIL", commonLocalizer.localize(PdfLocalizationName.email), contactEmail);
        opportunityInformationData.addRowWithCode("ASSIGNEE", pdfWfmMessageSource.localize(PdfLocalizationName.assignee), assignee);
        opportunityInformationData.addRowWithCode("STAGE", pdfWfmMessageSource.localize(PdfLocalizationName.stage), stage);
        opportunityInformationData.addRowWithCode("AMOUNT", pdfWfmMessageSource.localize(PdfLocalizationName.amount), amount);
        opportunityInformationData.addRowWithCode("GRAND_TOTAL", commonLocalizer.localize(PdfLocalizationName.grandTotal), amount);
        opportunityInformationData.addRowWithCode("CLOSE_DATE", pdfWfmMessageSource.localize(PdfLocalizationName.closeDate), closeDate);
        opportunityInformationData.addRowWithCode("CLOSE_DATE_UNIQUE_FORMAT", pdfWfmMessageSource.localize(PdfLocalizationName.closeDate), closeDateUniqueFormat);
        opportunityInformationData.addRowWithCode("OPPORTUNITY_INFORMATION", pdfWfmMessageSource.localize("opportunityInformation"), "");
        opportunityInformationData.addRowWithCode("ADDITIONAL_INFORMATION", pdfWfmMessageSource.localize("additionalInformation"), "");
        opportunityInformationData.addRowWithCode("ASSIGNEE_POSITION_EN", "", positionEn);
        opportunityInformationData.addRowWithCode("ASSIGNEE_POSITION_RU", "", positionRu);
        opportunityInformationData.addRowWithCode("ASSIGNEE_POSITION_UZ", "", positionUz);
        opportunityInformationData.addRowWithCode("ASSIGNEE_DEPARTMENT_EN", "", departmentEn);
        opportunityInformationData.addRowWithCode("ASSIGNEE_DEPARTMENT_RU", "", departmentRu);
        opportunityInformationData.addRowWithCode("ASSIGNEE_DEPARTMENT_UZ", "", departmentUz);
        opportunityInformationData.addRowWithCode("ASSIGNEE_PHONE", "", assigneePhone);
        opportunityInformationData.addRowWithCode("ASSIGNEE_EMAIL", "", assigneeEmail);
        opportunityInformationData.addRowWithCode("CREATED_DATE", "", createdDate);
        opportunityInformationData.addRowWithCode("ASSIGNEE_QR_CODE", "", escapeHtml(QRCodeGenerator.generate(assigneeQrCode, 200, 200)));

        return opportunityInformationData;
    }

    private CustomisedITextTable getTotalTableData(OpportunityListItem item, EdsUser user) {
        DecimalFormat numberFormat = getPriceScaleNumberFormat(user.getCompany(), null);

        CustomisedITextTable totalData = new CustomisedITextTable();
        totalData.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);

        String subtotal = item.getSubTotal() != null ? numberFormat.format(item.getSubTotal()) : BigDecimal.ZERO.toString();
        String total = item.getTotal() != null ? numberFormat.format(item.getTotal()) : BigDecimal.ZERO.toString();
        String totalInBase = item.getTotal() != null && item.getTotal().compareTo(BigDecimal.ZERO) > 0 ? numberFormat.format(item.getExchangeRate() != null ?
                item.getTotal().divide(item.getExchangeRate(), 8, RoundingMode.HALF_UP) : item.getTotal()) : "0.00";
        String taxTotal = item.getTaxTotal() != null ? numberFormat.format(item.getTaxTotal()) : BigDecimal.ZERO.toString();
        String discountTotal = item.getDiscountTotal() != null ? numberFormat.format(item.getDiscountTotal()) : BigDecimal.ZERO.toString();
        String quantityTotal = item.getQuantityTotal() != null ? numberFormat.format(item.getQuantityTotal()) : BigDecimal.ZERO.toString();

        totalData.addRowWithCode("SUB_TOTAL", wfmLocalizer.localize("subtotal"), subtotal);
        totalData.addRowWithCode("TOTAL", wfmLocalizer.localize("total"), total);
        totalData.addRowWithCode("TOTAL_IN_BASE", wfmLocalizer.localize("total"), totalInBase);
        totalData.addRowWithCode("TAX_TOTAL", wfmLocalizer.localize("taxTotal"), taxTotal);
        totalData.addRowWithCode("DISCOUNT_TOTAL", wfmLocalizer.localize("discount"), discountTotal);
        totalData.addRowWithCode("QUANTITY_TOTAL", wfmLocalizer.localize("qty"), quantityTotal);

        return totalData;
    }

    private List<CustomisedProductCategoriesITextTable> getCustomProducCategoriesTableData(OpportunityListItem opportunityListItem, EdsUser user) {
        List<CustomisedProductCategoriesITextTable> productCategoriesITextTable = new ArrayList<>();
        Map<String, ArrayList<EdsItem>> itemMap = new LinkedHashMap<>();

        OpportunityItem[] items = opportunityListItem.getItems();
        if (items == null) {
            return productCategoriesITextTable;
        }

        Set<Integer> categoryIds = new HashSet<>();
        for (OpportunityItem item : items) {
            EdsItem edsItem = itemManager.get(item.getItemID());
            if (edsItem != null && edsItem.getCategory() != null) {
                categoryIds.add(edsItem.getCategory().getObjectID());
            }
        }

        List<EdsItem> products = itemManager.getProductsByCategoryIds(categoryIds, false);

        for (EdsItem item : products) {
            if (item.getObjectID() != null && item.getCategory() != null) {
                String category = item.getCategory().getName();
                if (itemMap.containsKey(item.getCategory().getName())) {
                    itemMap.get(category).add(item);
                } else {
                    itemMap.put(category, new ArrayList<>(Collections.singletonList(item)));
                }
            } else {
                if (itemMap.containsKey(PA_NOT_AVAILABLE_STRING)) {
                    itemMap.get(PA_NOT_AVAILABLE_STRING).add(item);
                } else {
                    itemMap.put(PA_NOT_AVAILABLE_STRING, new ArrayList<>(Collections.singletonList(item)));
                }
            }
        }

        for (Map.Entry<String, ArrayList<EdsItem>> entry : itemMap.entrySet()) {
            CustomisedProductCategoriesITextTable categoriesTable = new CustomisedProductCategoriesITextTable();
            Map<String, String> rows = new HashMap<>();

            rows.put(ITEM_CATEGORY, entry.getKey());
            categoriesTable.setRows(rows);
            CustomisedITextTable table = getProductItems(entry.getValue());
            categoriesTable.setTable(table);
            productCategoriesITextTable.add(categoriesTable);
        }
        return productCategoriesITextTable;
    }

    private CustomisedITextTable getProductItems(ArrayList<EdsItem> edsItems) {
        CustomisedITextTable productTableData = new CustomisedITextTable();
        if (edsItems.size() == 0) {
            return productTableData;
        }

        productTableData.addColumn(ITEM_NUMBER, pdfWfmMessageSource.localize(PdfLocalizationName.Qnumber, ""));
        productTableData.addColumn(ITEM_NAME, pdfWfmMessageSource.localize(PdfLocalizationName.product, ""));
        productTableData.addColumn(DESCRIPTION, pdfWfmMessageSource.localize(PdfLocalizationName.description, ""));
        productTableData.addColumn("ITEM_IMAGE", "Image");

        for (EdsItem item : edsItems) {
            String pictureUrl = "";
            List<EdsProductPicture> pictures = productPictureManager.getProductPictures(item, 0);
            for (EdsProductPicture picture : pictures) {
                if (picture.isDefaultPicture() != null && picture.isDefaultPicture()) {
                    pictureUrl = uploadManager.getFileURL(picture);
                    break;
                }
            }
            String itemNumber = escapeHtml(item.getProductNumber());
            String itemName = escapeHtml(item.getName());
            String description = escapeHtml(item.getDescription());

            productTableData.addRow(itemNumber, itemName, description, pictureUrl);
        }

        return productTableData;
    }

    private CustomisedITextTable getCustomProductTable(OpportunityListItem opportunityListItem, EdsUser user) {
        OpportunityItem[] items = opportunityListItem.getItems();
        DecimalFormat numberFormat = getPriceScaleNumberFormat(user.getCompany(), null);
        CustomisedITextTable productTableData = new CustomisedITextTable();
        if (items == null) {
            return productTableData;
        }

        productTableData.setName(wfmLocalizer.localize("productsOrServices"));
        productTableData.addColumn(ITEM_NUMBER, pdfWfmMessageSource.localize(PdfLocalizationName.Qnumber));
        productTableData.addColumn(ITEM_NAME, pdfWfmMessageSource.localize(PdfLocalizationName.product));
        productTableData.addColumn(DESCRIPTION, pdfWfmMessageSource.localize(PdfLocalizationName.description));
        productTableData.addColumn(ITEM_UNIT_MEASUREMENT, pdfWfmMessageSource.localize("measurementUnit"));
        productTableData.addColumn(QTY, pdfWfmMessageSource.localize(PdfLocalizationName.qty));
        productTableData.addColumn(ITEM_UNIT_PRICE, pdfWfmMessageSource.localize(PdfLocalizationName.price));
        productTableData.addColumn(ITEM_TOTAL_AMOUNT, pdfWfmMessageSource.localize(PdfLocalizationName.total));
        productTableData.addColumn(SUPPLIER, pdfWfmMessageSource.localize(PdfLocalizationName.supplier));
        productTableData.addColumn(ITEM_CATEGORY, "Category");
        productTableData.addColumn(ITEM_BRAND_NAME, "Brand");
        productTableData.addColumn(ITEM_TAX_RATE, "Tax Rate");
        productTableData.addColumn(ITEM_TAX_AMOUNT, "Tax Amount");
        productTableData.addColumn("UNIT_PRICE_WITH_TAX_RATE", "");
        productTableData.addColumn("UNIT_PRICE_WITH_TAX_RATE_MULTIPLY_QTY", "");
        productTableData.addColumn(ITEM_NET_AMOUNT, "Net Amount");
        productTableData.addColumn("UNIT_PRICE_DISCOUNTED", "Unit Price Discounted");
        productTableData.addColumn(ITEM_PICTURE, "Item Picture");

        if (items[0].getItemCustomFields() != null) {
            for (CompanyCustomFieldItem item : items[0].getItemCustomFields()) {
                productTableData.addColumn(item.getFieldName(), item.getFieldName());
            }
        }
        LinkedHashMap<String, HashMap<String, String>> mapOfRows = new LinkedHashMap<>();
        for (OpportunityItem item : items) {
            EdsItem edsItem = item.getItemID() != null ? itemManager.get(item.getItemID()) : null;
//            ArrayList<CompanyCustomFieldItem> companyCustomFieldItems = edsItem != null ? CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getCustomFields(), commonService.getCompanyCustomFields(ViewName.ProductServiceView)) : null;
            LinkedList<String> rows = new LinkedList<>();
            LinkedHashMap<String, String> rowsMap = new LinkedHashMap<>();

            String itemNumber = escapeHtml(item.getItemNumber());
            String itemName = escapeHtml(item.getItemName());
            String description = escapeHtml(item.getDescription());
            String uom = item.getUnitMeasurement() != null ? escapeHtml(item.getUnitMeasurement().getName()) : "";
            String qty = item.getQty() != null ? numberFormat.format(item.getQty()) : BigDecimal.ZERO.toString();
            String unitPrice = item.getPrice() != null ? numberFormat.format(item.getPrice()) : BigDecimal.ZERO.toString();

            BigDecimal qtyBigDecimal = Optional.ofNullable(item.getQty()).orElse(BigDecimal.ZERO);
            BigDecimal unitPriceBigDecimal = Optional.ofNullable(item.getPrice()).orElse(BigDecimal.ZERO);
            BigDecimal totalAmountBigDecimal = qtyBigDecimal.multiply(unitPriceBigDecimal);

            String totalAmount = numberFormat.format(totalAmountBigDecimal);
            String supplier = escapeHtml(item.getSupplierName());

            String taxRate = item.getTaxItem() != null && item.getTaxItem().getTaxPercent() != null ? numberFormat.format(item.getTaxItem().getTaxPercent()) : "";

            String productCategory = item.getProductCategory() != null ? escapeHtml(item.getProductCategory().getName()) : "";
            String brand = item.getProductBrand() != null ? escapeHtml(item.getProductBrand().getName()) : "";
            String taxAmount = item.getTaxAmount() != null ? numberFormat.format(item.getTaxAmount()) : "";
            String netAmount = item.getNet() != null ? numberFormat.format(item.getNet()) : "";
            BigDecimal discountAmount = BigDecimal.ZERO;
            if (item.getDiscountPercent() != null) {
                discountAmount = unitPriceBigDecimal.multiply(item.getDiscountPercent().divide(new BigDecimal(100)));
            }
            BigDecimal unitPriceDiscounted = unitPriceBigDecimal.subtract(discountAmount);
            BigDecimal taxPercent = item.getTaxItem() != null && item.getTaxItem().getTaxPercent() != null ? item.getTaxItem().getTaxPercent() : BigDecimal.ZERO;

            BigDecimal unitPriceTaxAmount = BigDecimal.ZERO;
            BigDecimal unitPriceWithTaxRate = BigDecimal.ZERO;
            String unitPriceWithTaxRateString = "";
            String unitPriceWithTaxRateMultiplyQtyString = "";
            if(opportunityListItem.getTaxCalculationType() != null) {
                if (AccountingConstants.TAX_CALCULATION_EXCLUSIVE.equals(opportunityListItem.getTaxCalculationType())) {
                    unitPriceTaxAmount = unitPriceDiscounted.multiply(taxPercent.divide(new BigDecimal(100), RoundingMode.HALF_UP));
                    unitPriceWithTaxRate = unitPriceDiscounted.add(unitPriceTaxAmount);
                    unitPriceWithTaxRateString = numberFormat.format(unitPriceWithTaxRate);
                    unitPriceWithTaxRateMultiplyQtyString =  numberFormat.format(unitPriceWithTaxRate.multiply(qtyBigDecimal));
                } else if (AccountingConstants.TAX_CALCULATION_INCLUSIVE.equals(opportunityListItem.getTaxCalculationType())) {
                    /*unitPriceTaxAmount = unitPriceDiscounted.multiply(taxPercent.divide(taxPercent.add(new BigDecimal(100)), RoundingMode.HALF_UP));
                    unitPriceWithTaxRate = unitPriceDiscounted.add(unitPriceTaxAmount);
                    unitPriceWithTaxRateString = numberFormat.format(unitPriceWithTaxRate);
                    unitPriceWithTaxRateMultiplyQtyString =  numberFormat.format(unitPriceWithTaxRate.multiply(qtyBigDecimal));*/
                    unitPriceWithTaxRateString = numberFormat.format(unitPriceDiscounted);
                    unitPriceWithTaxRateMultiplyQtyString =  numberFormat.format(item.getNet());
                } else if (AccountingConstants.NO_TAX_CALCULATION.equals(opportunityListItem.getTaxCalculationType())) {
                    unitPriceWithTaxRateString = numberFormat.format(unitPriceDiscounted);
                    unitPriceWithTaxRateMultiplyQtyString = item.getNet() != null ? numberFormat.format(item.getNet()) : "";
                }
            }
            String pictureUrl = "";
            List<EdsProductPicture> pictures = productPictureManager.getProductPictures(edsItem, 2);
            for (EdsProductPicture picture : pictures) {
                if (picture.isDefaultPicture() != null && picture.isDefaultPicture()) {
                    pictureUrl = uploadManager.getFileURL(picture);
                    break;
                }
            }

            rows.add(itemNumber);
            rows.add(itemName);
            rows.add(description);
            rows.add(uom);
            rows.add(qty);
            rows.add(unitPrice);
            rows.add(totalAmount);
            rows.add(supplier);
            rows.add(productCategory);
            rows.add(brand);
            rows.add(taxRate);
            rows.add(taxAmount);
            rows.add(unitPriceWithTaxRateString);
            rows.add(unitPriceWithTaxRateMultiplyQtyString);
            rows.add(netAmount);
            rows.add(numberFormat.format(unitPriceDiscounted));
            rows.add(escapeHtml(pictureUrl));

            if (item.getItemCustomFields() != null) {
                for (CompanyCustomFieldItem customFieldItem : item.getItemCustomFields()) {
                    rows.add(customFieldItem.getFieldStringValue() != null ? customFieldItem.getFieldStringValue() : "");
                }
            }
//            if (companyCustomFieldItems != null) {
//                for (CompanyCustomFieldItem companyCustomFieldItem : companyCustomFieldItems) {
//                    if (companyCustomFieldItem != null) {
//                        productTableData.addColumn(companyCustomFieldItem.getFieldName(), companyCustomFieldItem.getFieldName());
//                        rows.add(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
//                    }
//                }
//            }
            int i = 0;
            for (String list : rows) {
                rowsMap.put(productTableData.getColumnOrder().get(i), list);
                i++;
            }
            mapOfRows.put(String.valueOf(mapOfRows.size()), rowsMap);
        }
        productTableData.setRows(mapOfRows);
        productTableData.setCustomFields(getProductCustomFields(items));
        return productTableData;
    }

    public Map<String, LinkedHashMap<String, Map<String, String>>> getProductCustomFields(OpportunityItem[] items) {
        final DecimalFormat decimalFormat = new DecimalFormat(",##0.00");
        Map<String, LinkedHashMap<String, Map<String, String>>> itemCusFields = new HashMap<>();
        int counter = 0;
        for (OpportunityItem item : items) {
            EdsItem edsItem = item.getItemID() != null ? itemManager.get(item.getItemID()) : null;
            ArrayList<CompanyCustomFieldItem> customFieldTemplates = commonService.getCompanyCustomFields(ViewName.ProductServiceView);
            ArrayList<CompanyCustomFieldItem> filledFields = edsItem != null ? CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getCustomFields(), customFieldTemplates) : null;
            LinkedHashMap<String, Map<String, String>> customFieldsMap = new LinkedHashMap<>();
            if (filledFields != null && !filledFields.isEmpty()) {
                SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(userManager.getUser().getCompany());
                for (CompanyCustomFieldItem field : filledFields) {
                    if (field == null || field.getAliasName() == null) continue;
                    Map<String, String> fieldData = new HashMap<>();
                    fieldData.put(COLUMN_NAME, escapeHtml(field.getAliasName()));
                    String value = "";
                    if (CompanyCustomFieldItem.DATE.equals(field.getDataType())) {
                        if (field.getFieldDateNonConvertedValue() != null) {
                            value = escapeHtml(shortDateFormat.format(field.getFieldDateNonConvertedValue().getNonConvertedDate()));
                        }
                    } else if (CompanyCustomFieldItem.NUMBER.equals(field.getDataType())) {
                        if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                            value = escapeHtml(decimalFormat.format(Double.parseDouble(field.getFieldStringValue())));
                        }
                    } else {
                        if (StringUtils.isNotEmpty(field.getFieldStringValue())) {
                            value = escapeHtml(field.getFieldStringValue());
                        }
                    }
                    fieldData.put(COLUMN_VALUE, value);
                    customFieldsMap.put(field.getAliasName(), fieldData);
                }
            }
            itemCusFields.put(String.valueOf(counter), customFieldsMap);
            counter++;
        }

        return itemCusFields;
    }

    private CustomisedITextTable getCustomerDetails(CrmAccountItem accountItem, EdsUser user) {
        CustomisedITextTable customerTable = new CustomisedITextTable();
        DecimalFormat numberFormat = getPriceScaleNumberFormat(user.getCompany(), null);
        if (accountItem == null) {
            return customerTable;
        }
        customerTable.addColumnOrder(PDFConstants.COLUMN_NAME, PDFConstants.COLUMN_VALUE);

        EdsCrmAccount customer = accountItem.getObjectId() != null ? crmAccountManager.get(accountItem.getObjectId()) : null;
        if (customer == null) {
            return customerTable;
        }
        Double customerBalance = accountItem.getObjectId() != null && crmAccountManager.getClientBalance(accountItem.getObjectId()) != null ?
                crmAccountManager.getClientBalance(accountItem.getObjectId()).doubleValue() : 0d;
        EdsAddress billAddress = customer.getBillingAddress() != null ? customer.getBillingAddress() : null;
        if (billAddress != null) {
            customerTable.addRowWithCode(BILL_ADDRESS_NAME, "", escapeHtml(billAddress.getName()));
            customerTable.addRowWithCode(BILL_ADDRESS, commonLocalizer.localize(PdfLocalizationName.addressLine1), escapeHtml(billAddress.getAddress()));
            customerTable.addRowWithCode(BILL_ADDRESS2, commonLocalizer.localize(PdfLocalizationName.addressLine2), escapeHtml(billAddress.getAddressb()));
            customerTable.addRowWithCode(BILL_CITY, commonLocalizer.localize(PdfLocalizationName.city), escapeHtml(billAddress.getCity()));
            customerTable.addRowWithCode(BILL_COUNTRY, commonLocalizer.localize(PdfLocalizationName.country), escapeHtml(billAddress.getCountryName()));
            customerTable.addRowWithCode(BILL_ZIPCODE, commonLocalizer.localize(PdfLocalizationName.postCode), escapeHtml(billAddress.getZipCode()));
        }
        EdsAddress mailAddress = customer.getMailingAddress() != null ? customer.getMailingAddress() : null;
        if (mailAddress != null) {
            customerTable.addRowWithCode(MAIL_ADDRESS_NAME, "", escapeHtml(mailAddress.getName()));
            customerTable.addRowWithCode(MAIL_ADDRESS, commonLocalizer.localize(PdfLocalizationName.addressLine1), escapeHtml(mailAddress.getAddress()));
            customerTable.addRowWithCode(MAIL_ADDRESS2, commonLocalizer.localize(PdfLocalizationName.addressLine2), escapeHtml(mailAddress.getAddressb()));
            customerTable.addRowWithCode(MAIL_CITY, commonLocalizer.localize(PdfLocalizationName.city), escapeHtml(mailAddress.getCity()));
            customerTable.addRowWithCode(MAIL_COUNTRY, commonLocalizer.localize(PdfLocalizationName.country), escapeHtml(mailAddress.getCountryName()));
            customerTable.addRowWithCode(MAIL_ZIPCODE, commonLocalizer.localize(PdfLocalizationName.postCode), escapeHtml(mailAddress.getZipCode()));
        }

        String customerName = escapeHtml(customer.getName());
        String customerContact = customer.getPrimaryContact() != null ? escapeHtml(customer.getPrimaryContact().getName()) : "";
        String customerContactPhone = customer.getPrimaryContact() != null && customer.getPrimaryContact().getPrimaryPhone() != null ? customer.getPrimaryContact().getPrimaryPhone() : "";
        String customerPhone = escapeHtml(customer.getPhone());
        String customerEmail = escapeHtml(customer.getEmail());
        String paymentTerms = customer.getTerms() != null ? escapeHtml(customer.getTerms().getName()) : "";

        customerTable.addRowWithCode(PDFConstants.CUSTOMER, commonLocalizer.localize(PdfLocalizationName.customer), customerName);
        customerTable.addRowWithCode(PDFConstants.MEMBER, wfmLocalizer.localize(PdfLocalizationName.member), customerName);
        customerTable.addRowWithCode("CUSTOMER_BALANCE", "Customer Balance", numberFormat.format(customerBalance));
        customerTable.addRowWithCode(CLIENT_CONTACT, commonLocalizer.localize(PdfLocalizationName.contact), customerContact);
        customerTable.addRowWithCode("CLIENT_CONTACT_PHONE", "Contact Phone", customerContactPhone);
        customerTable.addRowWithCode(CLIENT_PHONE, commonLocalizer.localize(PdfLocalizationName.phone), customerPhone);
        customerTable.addRowWithCode(CLIENT_EMAIL, commonLocalizer.localize(PdfLocalizationName.email), customerEmail);
        customerTable.addRowWithCode(PAYMENT_TERMS, commonLocalizer.localize(PdfLocalizationName.paymentTerms), paymentTerms);

        return customerTable;
    }

    private Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields(OpportunityListItem opportunityItem) {
        Map<String, LinkedHashMap<String, Map<String, String>>> customFields = new HashMap<>();
        if (opportunityItem.getCustomFields() != null && opportunityItem.getCustomFields().size() > 0) {
            LinkedHashMap<String, Map<String, String>> itemCusFields = new LinkedHashMap<>();
            for (CompanyCustomFieldItem item : opportunityItem.getCustomFields()) {
                if (item != null) {
                    Map<String, String> cols = new HashMap<>();
                    cols.put(COLUMN_NAME, item.getDefaultName() != null ? escapeHtml(item.getDefaultName()) : "");
                    if (CompanyCustomFieldItem.DATE.equals(item.getDataType())) {
                        String dateValue = "";
                        EdsCompany company = userManager.getUser().getCompany();
                        SimpleDateFormat shortDateFormat = getCompanyShortDateFormat(company);
                        if (item.getFieldDateNonConvertedValue() != null) {
                            if (company.getLocale() != null && "ru".equals(company.getLocale())) {
                                Locale ruLocale = new Locale("ru", "RU");
                                SimpleDateFormat ruDateFormat = new SimpleDateFormat(shortDateFormat.toPattern(), ruLocale);
                                dateValue = item.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? ruDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                            } else {
                                dateValue = item.getFieldDateNonConvertedValue().getNonConvertedDate() != null ? shortDateFormat.format(item.getFieldDateNonConvertedValue().getNonConvertedDate()) : "—";
                            }
                            cols.put(COLUMN_VALUE, dateValue);
                        }
                    } else {
                        cols.put(COLUMN_VALUE, item.getFieldStringValue() != null ? escapeHtml(item.getFieldStringValue()) : "");
                    }
                    if (item.getDefaultName() != null) {
                        itemCusFields.put(escapeHtml(item.getDefaultName()), cols);
                    }
                }
            }
            customFields.put("OPPORTUNITY", itemCusFields);
        }
        return customFields;
    }

    private CustomisedITextTable getCreationData(OpportunityListItem item) {
        CustomisedITextTable customTable = new CustomisedITextTable();
        customTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd.MM.yyyy");

        if (item == null) {
            return customTable;
        }

        String creationDate = item.getCreatedDate() != null ? shortDateFormat.format(item.getCreatedDate()) : "";
        String modifiedDate = item.getUpdatedDate() != null ? shortDateFormat.format(item.getUpdatedDate()) : "";
        customTable.addRowWithCode("CREATED_DATE", "", creationDate);
        customTable.addRowWithCode("MODIFIED_DATE", "", modifiedDate);
        customTable.addRowWithCode("CURRENT_DATE", "", shortDateFormat.format(new Date()));
        return customTable;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(pdfWfmMessageSource.localize("opportunity") + "_" + dateFormat(new Date()));
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.OPPORTUNITY;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        return ((CrmAccountRequestObject) dataClass).getIS_LANDSCAPE() ? PdfParams.Orientation.landscape : null;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        CrmAccountRequestObject requestObject = (CrmAccountRequestObject) object;
        return requestObject.getPdfTemplateId();
    }
}
