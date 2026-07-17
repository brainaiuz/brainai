package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.server.app.FixedAssetServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Ilxom Lutfullaev on 20.12.2017.
 */

public class FixedAssetViewPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    @Autowired
    private FixedAssetServiceLocal fixedAssetService;
    @Autowired
    protected GenericSettingsManager genericSettingsManager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        EdsUser user = userManager.getUser();
        DecimalFormat numberFormat = getPriceScaleNumberFormat(user.getCompany(), null);
        SimpleDateFormat dateFormat = getCompanyShortDateFormat(user.getCompany());

        FixedAssetItem item = ((FixedAssetItem) dataClass);
        item = fixedAssetService.getFixedAssetData(item.getObjectID());

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();
        CustomisedITextTable viewTable = new CustomisedITextTable();
        viewTable.setName(item.getName());
        viewTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        viewTable.addRowWithCode(OWNER, commonLocalizer.localize(PdfLocalizationName.owner), escapeHtml(item.getOwner() != null ? item.getOwner().getName() : ""));
        viewTable.addRowWithCode(CATEGORY, commonLocalizer.localize(PdfLocalizationName.category), escapeHtml(item.getAccount() != null ? item.getAccount().getName() : ""));
        viewTable.addRowWithCode(NUMBER, commonLocalizer.localize(PdfLocalizationName.number), escapeHtml(item.getNumberData().getNumberString()));
        viewTable.addRowWithCode(NAME, commonLocalizer.localize(PdfLocalizationName.name), escapeHtml(item.getName()));
        viewTable.addRowWithCode(DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description), escapeHtml(item.getDescription()));
        viewTable.addRowWithCode(COST, commonLocalizer.localize(PdfLocalizationName.cost), escapeHtml(numberFormat.format(item.getCost())));
        viewTable.addRowWithCode(PURCHASE_DATE, commonLocalizer.localize(PdfLocalizationName.purchaseDate), escapeHtml(item.getCreationDate() != null ? dateFormat.format(item.getCreationDate().getDate()) : ""));
        viewTable.addRowWithCode(USE_FUL_LIFE, commonLocalizer.localize(PdfLocalizationName.useFulLife), escapeHtml(numberFormat.format(item.getUsefulLife())));
        viewTable.addRowWithCode(RESIDUAL_VALUE, commonLocalizer.localize(PdfLocalizationName.residualValue), escapeHtml(numberFormat.format(item.getResidualValue())));
        viewTable.addRowWithCode(IMAGE_URL, "", escapeHtml(item.getImageLink()));

        String barCodeImg = "https://chart.googleapis.com/chart?chs=350x350&cht=qr&chl=";
        String creationDate = "";
        if (item.getCreationDate() != null) {
            creationDate = dateFormat.format(item.getCreationDate().getDate());
        }
        barCodeImg += item.getBarcodeGenerateText(item.getShowDescInBarcode(), creationDate, genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_FULL_BARCODE_DATA));
        viewTable.addRowWithCode(BARCODE_IMAGE_URL, "", barCodeImg);

        // dd-MM-yyyy dates for the fixed-asset LABEL template only. The PDF renderer
        // (Flying Saucer) has no date tool in the Velocity context, and the generic
        // PURCHASE_DATE row above is already formatted in the company short date format,
        // so the label reads these dedicated rows instead. PRINT_DATE is the moment the
        // PDF is generated.
        SimpleDateFormat labelDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        viewTable.addRowWithCode("LABEL_PURCHASE_DATE", "",
                escapeHtml(item.getCreationDate() != null ? labelDateFormat.format(item.getCreationDate().getDate()) : ""));
        viewTable.addRowWithCode("PRINT_DATE", "", escapeHtml(labelDateFormat.format(new Date())));
        customData.put("VIEW_TABLE", viewTable);

        CustomisedITextTable financingTable = new CustomisedITextTable();
        financingTable.setName("Fixed Asset Financing");
        financingTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        financingTable.addRowWithCode(ACCOUNT, commonLocalizer.localize(PdfLocalizationName.account), escapeHtml(item.getFinancedByAccount() != null ? item.getFinancedByAccount().getName() : ""));
        String convertedItem = item.getPurchaseInvoiceID() != null ? commonLocalizer.localize(PdfLocalizationName.purchaseInvoice) :  commonLocalizer.localize(PdfLocalizationName.purchaseOrder);
        if (item.getConvertedItemNumber() != null) {
            convertedItem += ": " + item.getConvertedItemNumber();
        }
        financingTable.addRowWithCode(CONVERTED, commonLocalizer.localize(PdfLocalizationName.convertedItem), escapeHtml(convertedItem));
        customData.put("FINANCING_TABLE", financingTable);

        CustomisedITextTable depreciationTable = new CustomisedITextTable();
        depreciationTable.setName("Depreciation Accounts");
        depreciationTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE);
        depreciationTable.addRowWithCode(FIXED_ASSETS, commonLocalizer.localize(PdfLocalizationName.fixedAsset), escapeHtml(item.getFixedAssetAccount() != null ? item.getFixedAssetAccount().getName() : ""));
        depreciationTable.addRowWithCode(EXPENSE, commonLocalizer.localize(PdfLocalizationName.expense), escapeHtml(item.getExpenseAccount() != null ? item.getExpenseAccount().getName() : ""));
        customData.put("DEPRECIATION_TABLE", depreciationTable);

        CustomisedITextTable customFieldTable = new CustomisedITextTable();
        customFieldTable.setName(commonLocalizer.localize(PdfLocalizationName.additionalInformation));
        customFieldTable.addColumnOrder(COLUMN_NAME, COLUMN_VALUE, TYPE);
        if (item.getCustomFields() != null && !item.getCustomFields().isEmpty()) {
            for (CompanyCustomFieldItem customField : item.getCustomFields()) {
                switch (customField.getDataType()) {
                    case CompanyCustomFieldItem.DATE -> {
                        String dateValue = "";
                        if (customField.getFieldDateNonConvertedValue() != null) {
                            dateValue = escapeHtml(dateFormat.format(customField.getFieldDateNonConvertedValue().getNonConvertedDate()));
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), dateValue, DATA_TYPE_DATE);
                    }
                    case CompanyCustomFieldItem.NUMBER -> {
                        String numberValue = "";
                        if (StringUtils.isNotEmpty(customField.getFieldStringValue())) {
                            numberValue = escapeHtml(numberFormat.format(Double.valueOf(customField.getFieldStringValue())));
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), numberValue, DATA_TYPE_NUMBER);
                    }
                    case DATA_TYPE_PROFILE_IMAGE -> {
                        String uploadImageId = "";
                        if (customField.getProfielImageId() != null) {
                            uploadImageId = commonService.getImageUrl(customField.getProfielImageId());
                        }
                        customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), uploadImageId, DATA_TYPE_PROFILE_IMAGE);
                    }
                    default ->
                            customFieldTable.addRowWithCode(customField.getFieldName(), customField.getFieldName(), escapeHtml(customField.getFieldStringValue()), DATA_TYPE_TEXT);
                }
            }
        }
        customData.put("CUSTOM_FIELD", customFieldTable);

        ITextGenericPdfData pdf = new ITextGenericPdfData();
        pdf.setCustomData(customData);
        return pdf;
    }

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        FixedAssetItem requestObject = new FixedAssetItem();
        if (StringUtils.isNotBlank(request.getParameter("pdfTemplateID"))) {
            requestObject.setPdfTemplateId(Integer.valueOf(request.getParameter("pdfTemplateID")));
        }
        return requestObject;
    }

    @Override
    protected Integer getCustomisedPDFTemplateId(Object object) {
        if (object instanceof FixedAssetItem) {
            return ((FixedAssetItem) object).getPdfTemplateId();
        }
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Fixed_asset_" + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localizeAccounting(PdfLocalizationName.fixedAsset);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.FIXED_ASSET;
    }
}
