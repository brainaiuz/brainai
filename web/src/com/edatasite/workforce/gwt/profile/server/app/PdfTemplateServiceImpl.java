package com.edatasite.workforce.gwt.profile.server.app;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyAttachment;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.pdf.EdsPdfDynamicFooterHeader;
import com.edatasite.workforce.core.domain.pdf.EdsPdfReference;
import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplate;
import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplateSettings;
import com.edatasite.workforce.core.domain.pdf.EdsPdfTemplateTableSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.enums.PdfGenerateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.DynamicFooterHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.PdfReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.PdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.PdfTemplateSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.PdfTemplateTableSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextBaseInvoice;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCompanyData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfFooteHederAttributeEnum;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfFooterHeaderContentItem;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateService;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateTableSettingsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateTableTypeEnum;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateGenerateItem;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateItem;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateListItem;
import com.edatasite.workforce.mail.EdsTemplate;
import com.edatasite.workforce.utils.EdsContextParams;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_;

/**
 * User: Abror Abdukadirov
 * Date: 12.02.2019 16:24
 */
@Transactional
@Service("pdfTemplateService")
public class PdfTemplateServiceImpl implements PdfTemplateService, PdfTemplateServiceLocal {

    private static final Logger log = LoggerFactory.getLogger(PdfTemplateServiceImpl.class);

    @Autowired
    private PdfTemplateManager pdfTemplateManager;
    @Autowired
    private PdfTemplateTableSettingsManager pdfTemplateTableSettingsManager;
    @Autowired
    @Qualifier("pdfWfmMessageSource")
    private WfmResourceBundleMessageSource pdfWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private PdfTemplateSettingsManager pdfTemplateSettingsManager;
    @Autowired
    private CompanyAttachmentManager companyAttachmentManager;
    @Autowired
    private PdfReferenceManager pdfReferenceManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CompanyPayrollSettingsManager companyPayrollSettingsManager;
    @Autowired
    private CompanyCustomFieldsManager companyCustomFieldsManager;
    @Autowired
    private DynamicFooterHeaderManager dynamicFooterHeaderManager;

    @Override
    public ListResult<SettingsPdfTemplateListItem> getSettingsPdfTemplateList(ListingFilterParameter fp) {
        ArrayList<SettingsPdfTemplateListItem> result = Lists.newArrayList();
        Integer count = companyPdfTemplateManager.getSettingsPdfTemplatesCount(fp);
        if (count != null && count > 0) {
            List<EdsCompanyPdfTemplate> list = companyPdfTemplateManager.getSettingsPdfTemplates(fp);
            result.addAll(list.stream().map(EdsCompanyPdfTemplate::toTO).toList());
        }
        return new ListResult<>(result, count);
    }

    @Override
    public SettingsPdfTemplateItem getSettingsPdfTemplateFooterAndHeader(Integer objectId, String pdfType) {
        SettingsPdfTemplateItem result = new SettingsPdfTemplateItem();
        EdsUser edsUser = userManager.getUser();
        if (edsUser == null || edsUser.getCompany() == null) {
            return result;
        }
        EdsPdfTemplateSettings edsSettings = pdfTemplateSettingsManager.getPdfSettings();
        if (edsSettings != null) {
            result = edsSettings.toHeaderAndFooterTO();
        } else {
            List<EdsPdfDynamicFooterHeader> defaultFooterHeaders = dynamicFooterHeaderManager.getDefaultFooterHeaderValues();
            ArrayList<PdfFooterHeaderContentItem> widgetItems = new ArrayList<>();
            if (defaultFooterHeaders != null && defaultFooterHeaders.size() > 0) {
                defaultFooterHeaders.forEach(item -> {
                    PdfFooterHeaderContentItem widget = new PdfFooterHeaderContentItem(item.getKey().substring(DEFAULT_.length()), item.getValue(), item.getEnable());
                    widgetItems.add(widget);
                });
            }
            result.setValueByPosition(widgetItems);
            result.setCustomizedFooter(true);
            result.setCustomizedHeader(true);
        }
        EdsCompanyPdfTemplate edsTemplate = companyPdfTemplateManager.get(objectId);
        if (edsTemplate != null && StringUtils.isNotEmpty(edsTemplate.getDocumentTitle())) {
            result.setDocumentTitle(edsTemplate.getDocumentTitle());
        }
        result.setDefaultDocumentTitle(getDefaultPdfName(pdfType));
        result.setCompanyName(edsUser.getCompany().getName());

        EdsCompany edsCompany = edsUser.getCompany();
        StringBuilder address = new StringBuilder();
        if (StringUtils.isNotEmpty(edsCompany.getAddress1())) {
            address.append(edsCompany.getAddress1()).append(", ");
        }
        if (StringUtils.isNotEmpty(edsCompany.getBillAddress2())) {
            address.append(edsCompany.getBillAddress2()).append(", ");
        }
        if (StringUtils.isNotEmpty(edsCompany.getCity())) {
            address.append(edsCompany.getCity()).append(", ");
        }
        if (edsCompany.getCountryRegion() != null && StringUtils.isNotEmpty(edsCompany.getCountryRegion().getName())) {
            address.append(edsCompany.getCountryRegion().getName()).append(", ");
        }
        if (edsCompany.getCountryZone() != null && edsCompany.getCountryZone().getCountry() != null
                && StringUtils.isNotEmpty(edsCompany.getCountryZone().getName())) {
            address.append(edsCompany.getCountryZone().getCountry().getName());
        }
        result.setCompanyAddress(address.toString());
        return result;
    }
    @Override
    public SettingsPdfTemplateItem getSettingsPdfTemplateContentLayout() {
        SettingsPdfTemplateItem result = new SettingsPdfTemplateItem();

        EdsPdfTemplateSettings edsSettings = pdfTemplateSettingsManager.getPdfSettings();
        if (edsSettings == null) {
            return result;
        }
        return result = edsSettings.toContentTO();
    }

    @Override
    public List<PdfTemplateTableSettingsItem> getPdfTableActiveColumns(Integer pdfId, String pdfType) {
        PdfTemplateTypeEnum typeEnum = PdfTemplateTypeEnum.get(pdfType);
        if (typeEnum == null) {
            return Collections.emptyList();
        }
        List<EdsPdfTemplateTableSettings> edsTableSettings = pdfTemplateTableSettingsManager.getListByTypeAndTableType(pdfId,
                                                                                                                       typeEnum,
                                                                                                                       PdfTemplateTableTypeEnum.PRODUCT_TABLE);
        if (!edsTableSettings.isEmpty()) {
            return edsTableSettings.stream().map(item -> item.toTO()).collect(Collectors.toList());
        }
        LinkedHashMap<String, PdfTemplateTableSettingsItem> map = switch (typeEnum) {
            case SALES_INVOICE, SALES_QUOTE, SALES_ORDER, PURCHASE_INVOICE, PURCHASE_ORDER, RECEIVABLE_CREDIT_NOTE, PAYABLE_CREDIT_NOTE ->
                    getDefaultSalesInvoiceColumns();
            case EXPENSE_REPORT -> getDefaultExpenseColumns();
            case RFQ -> getDefaultRFQColumns();
            case MANUAL_ENTRY -> getDefaultManualEntryColumns();
            case BATCH_RECEIVE_PAYMENT, BATCH_PAY_BILL -> getDefaultPaymentColumns();
            case BANK_PAYMENT, BANK_RECEIPT, CASH_PAYMENT, CASH_RECEIPT -> getDefaultSpendReceiveMoneyColumns();
            case GOODS_DELIVERED_NOTES, GOODS_RECEIVED_NOTES -> getDefaultGDNorGRNColumns();
            default -> new LinkedHashMap<>();
        };
        return map.values().stream().filter(PdfTemplateTableSettingsItem::isSelected).collect(Collectors.toList());
    }

    @Override
    public ArrayList<PdfTemplateTableSettingsItem> getPdfTableColumns(Integer pdfId, String pdfType) {
        PdfTemplateTypeEnum typeEnum = PdfTemplateTypeEnum.get(pdfType);
        if (typeEnum == null) {
            return Lists.newArrayList();
        }
        LinkedHashMap<String, PdfTemplateTableSettingsItem> columnMap = getAllColumnsByType(typeEnum);
        List<EdsPdfTemplateTableSettings> edsTableSettings = pdfTemplateTableSettingsManager.getListByTypeAndTableType(pdfId,
                                                                                                                       typeEnum,
                                                                                                                       PdfTemplateTableTypeEnum.PRODUCT_TABLE);
        if (!edsTableSettings.isEmpty()) {
            columnMap.forEach((k, v) -> v.setSelected(false));

            for (EdsPdfTemplateTableSettings edsColumn : edsTableSettings) {
                if (columnMap.get(edsColumn.getColumnCode()) != null) {
                    PdfTemplateTableSettingsItem item = columnMap.get(edsColumn.getColumnCode());
                    item.setColumnTitle(edsColumn.getColumnTitle());
                    item.setColumnDefaultTitle(getColumnDefaultTitle(edsColumn.getColumnCode(), edsColumn.getColumnTitle()));
                    if (edsColumn.getSorder() != null) {
                        item.setSorder(edsColumn.getSorder());
                    }
                    if (edsColumn.getWidth() != null) {
                        item.setWidth(edsColumn.getWidth());
                    }
                    if (edsColumn.getAlignment() != null) {
                        item.setAlignment(edsColumn.getAlignment());
                    }
                    if (edsColumn.getCustomField() != null) {
                        item.setCustomField(edsColumn.getCustomField());
                    }
                    item.setSelected(true);
                }
            }
            columnMap.forEach((k, v) -> {
                if (v.getSorder() == 0) {
                    v.setSorder(columnMap.size() + 2);
                }
            });
            return columnMap.values()
                            .stream()
                            .sorted(Comparator.comparing(item -> item.getSorder()))
                            .collect(Collectors.toCollection(ArrayList::new));
        }
        return Lists.newArrayList(columnMap.values());
    }

    private String getColumnDefaultTitle(String columnCode, String defaultTitle) {
        if (columnCode == null) {
            return defaultTitle;
        }
        return switch (columnCode) {
            case PDFConstants.ITEM_NO -> pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number);
            case PDFConstants.ITEM_NAME -> pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.name);
            case PDFConstants.ITEM_DESCRIPTION, PDFConstants.INV_NUMBER ->
                    pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description);
            case PDFConstants.ITEM_QTY_HRS -> pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.qty);
            case PDFConstants.ITEM_UNIT_PRICE -> pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitPrice);
            case PDFConstants.ITEM_DISCOUNT -> pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.discount);
            case PDFConstants.ITEM_TAX_AMOUNT -> pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.tax);
            case PDFConstants.ITEM_PAYMENT_REFERENCE ->
                    pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference);
            case PDFConstants.INV_DATE -> commonLocalizer.localize(PdfLocalizationName.invoiceDate);
            case PDFConstants.ITEM_TOTAL_AMOUNT -> accountingLocalizer.localizeAccounting(PdfLocalizationName.amount);
            case PDFConstants.ITEM_NET_AMOUNT ->
                    accountingLocalizer.localizeAccounting(PdfLocalizationName.paymentAmount);
            case PDFConstants.ACCOUNT_NAME -> accountingLocalizer.localizeAccounting(PdfLocalizationName.account);
            case PDFConstants.PROJECT_NAME, PDFConstants.RELATED_PROJECT ->
                    pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project);
            case PDFConstants.PO_NUMBER -> accountingLocalizer.localizeAccounting(PdfLocalizationName.poNumber);
            case PDFConstants.DUE_AMOUNT -> accountingLocalizer.localizeAccounting(PdfLocalizationName.dueAmount);
            case PDFConstants.ITEM_BASE_AMOUNT, PDFConstants.ITEM_BASE_TOTAL ->
                    accountingLocalizer.localizeAccounting(PdfLocalizationName.baseTotal);
            case PDFConstants.CURRENCY -> accountingLocalizer.localizeAccounting(PdfLocalizationName.currency);
            case PDFConstants.DEBIT -> accountingLocalizer.localizeAccounting(PdfLocalizationName.debit);
            case PDFConstants.CREDIT -> accountingLocalizer.localizeAccounting(PdfLocalizationName.credit);
            case PDFConstants.ITEM_DEPARTMENT -> commonLocalizer.localize(PdfLocalizationName.department);
            case PDFConstants.ACCOUNT_CODE -> commonLocalizer.localizeAccounting(PdfLocalizationName.accountCode);
            case PDFConstants.PARENT_PROJECT ->
                    pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.parentProject);
            case PDFConstants.EXP_BILL_TO -> pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.billTo);
            case PDFConstants.QTY, PDFConstants.ITEM_QTY -> commonLocalizer.localizeAccounting(PdfLocalizationName.qty);
            case PDFConstants.ITEM_COMISSION -> commonLocalizer.localizeAccounting(PdfLocalizationName.commission);
            case PDFConstants.UNIT -> commonLocalizer.localizeAccounting(PdfLocalizationName.um);
            case PDFConstants.ITEM_COST_PRICE -> commonLocalizer.localize(PdfLocalizationName.cost);
            case Constants.SUPPLIER -> commonLocalizer.localizeAccounting(PdfLocalizationName.supplier);
            case PDFConstants.REMARKS -> commonLocalizer.localizeAccounting(PdfLocalizationName.remarks);
            case PDFConstants.ITEM_ALLOCATE -> accountingLocalizer.localizeAccounting(PdfLocalizationName.allocate);
            case PDFConstants.ITEM_RECIEVE -> accountingLocalizer.localizeAccounting(PdfLocalizationName.receiveType);
            case PDFConstants.ITEM_PROJECT -> pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project);
            case PDFConstants.ITEM_UNIT_MEASUREMENT ->
                    pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.unitOfMeasure);
            default -> defaultTitle;
        };
    }

    @Override
    public SettingsPdfTemplateItem getSettingsPdfTemplateProperties(Integer pdfId, String pdfType) {
        SettingsPdfTemplateItem result = new SettingsPdfTemplateItem();
        EdsCompanyPdfTemplate edsTemplate = companyPdfTemplateManager.get(pdfId);
        if (edsTemplate != null) {
            result.setPdfName(edsTemplate.getName());
            result.setOrientation(edsTemplate.getOrientation());
            result.setMarginTop(edsTemplate.getMarginTop());
            result.setMarginRight(edsTemplate.getMarginRight());
            result.setMarginBottom(edsTemplate.getMarginBottom());
            result.setMarginLeft(edsTemplate.getMarginLeft());
            result.setDefaultTemplate(edsTemplate.isDefaultTemplate());
            result.setTemplateItem(new SelectItem(edsTemplate.getObjectID(), edsTemplate.getName()));
        } else {
            EdsPdfTemplateSettings edsSettings = pdfTemplateSettingsManager.getPdfSettings();
            if (edsSettings == null) {
                return result;
            }
            result.setOrientation(edsSettings.getOrientation());
            result.setMarginTop(edsSettings.getMarginTop());
            result.setMarginRight(edsSettings.getMarginRight());
            result.setMarginBottom(edsSettings.getMarginBottom());
            result.setMarginLeft(edsSettings.getMarginLeft());
            result.setPdfName(getDefaultPdfName(pdfType));
        }
        return result;
    }

    private String getDefaultPdfName(String pdfType) {
        PdfTemplateTypeEnum typeEnum = PdfTemplateTypeEnum.get(pdfType);
        if (typeEnum == null) {
            return accountingLocalizer.localizeAccounting(PdfLocalizationName.templateName);
        }
        return switch (typeEnum) {
            case SALES_INVOICE -> accountingLocalizer.localizeAccounting(PdfLocalizationName.salesInvoice);
            case SALES_QUOTE -> accountingLocalizer.localizeAccounting(PdfLocalizationName.salesQuote);
            case SALES_ORDER -> accountingLocalizer.localizeAccounting(PdfLocalizationName.salesOrder);
            case RECEIVABLE_CREDIT_NOTE -> accountingLocalizer.localizeAccounting(PdfLocalizationName.creditNote);
            case PAYABLE_CREDIT_NOTE -> accountingLocalizer.localizeAccounting(PdfLocalizationName.debitNote);
            case PURCHASE_INVOICE -> commonLocalizer.localize(PdfLocalizationName.purchaseInvoice);
            case PURCHASE_ORDER -> accountingLocalizer.localizeAccounting(PdfLocalizationName.purchaseOrder);
            case EXPENSE_REPORT -> pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.expenseClaim);
            case RFQ -> accountingLocalizer.localizeAccounting(PdfLocalizationName.requestForQuote);
            case MANUAL_ENTRY -> pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.manualTransaction);
            case BATCH_RECEIVE_PAYMENT -> "Payment Receipt";
            case BATCH_PAY_BILL -> "Payment Voucher";
            case BANK_RECEIPT -> commonLocalizer.localizeAccounting(PdfLocalizationName.bankReceipts);
            case BANK_PAYMENT -> commonLocalizer.localizeAccounting(PdfLocalizationName.bankPayments);
            case CASH_RECEIPT -> commonLocalizer.localizeAccounting(PdfLocalizationName.cashReceipt);
            case CASH_PAYMENT -> commonLocalizer.localizeAccounting(PdfLocalizationName.cashPayment);
            case GOODS_RECEIVED_NOTES -> pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.goodsReceivedNotes);
            case GOODS_DELIVERED_NOTES ->
                    pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.goodsDeliveredNotes);
            default -> accountingLocalizer.localizeAccounting(PdfLocalizationName.templateName);
        };
    }

    @Override
    public Integer savePdfPropertiesSettings(SettingsPdfTemplateItem item) {
        if (item == null) {
            return null;
        }
        EdsCompanyPdfTemplate companyPdfTemplate = companyPdfTemplateManager.get(item.getObjectId());
        if (companyPdfTemplate == null) {
            companyPdfTemplate = new EdsCompanyPdfTemplate();
        }
        EdsPdfTemplate pdfTemplate = companyPdfTemplate.getTemplate();
        if (pdfTemplate == null) {
            pdfTemplate = new EdsPdfTemplate();
        }
        EdsPdfReference edsType = pdfReferenceManager.getByCode(item.getPdfType());
        pdfTemplate.setType(edsType);
        companyPdfTemplate.setTemplate(pdfTemplate);
        companyPdfTemplate.setName(item.getPdfName());
        companyPdfTemplate.setGenerateType(null);
        companyPdfTemplate.setDefaultTemplate(item.isDefaultTemplate());

        if (item.isSystemPdf()) {
            EdsPdfTemplateSettings edsSettings = pdfTemplateSettingsManager.getPdfSettings();
            if (edsSettings == null) {
                edsSettings = new EdsPdfTemplateSettings();
            }
            edsSettings.setMarginTop(item.getMarginTop());
            edsSettings.setMarginRight(item.getMarginRight());
            edsSettings.setMarginBottom(item.getMarginBottom());
            edsSettings.setMarginLeft(item.getMarginLeft());
            edsSettings.setOrientation(PdfParams.Orientation.getByCode(item.getOrientation()).name());

            pdfTemplateSettingsManager.createOrUpdate(edsSettings);
        } else {
            companyPdfTemplate.setClientPdf(true);
            companyPdfTemplate.setMarginTop(item.getMarginTop());
            companyPdfTemplate.setMarginRight(item.getMarginRight());
            companyPdfTemplate.setMarginBottom(item.getMarginBottom());
            companyPdfTemplate.setMarginLeft(item.getMarginLeft());
            companyPdfTemplate.setOrientation(PdfParams.Orientation.getByCode(item.getOrientation()).name());
        }
        if (companyPdfTemplate.getObjectID() != null) {
            companyPdfTemplate.setUpdatedDate(new Date());
            companyPdfTemplate.setUpdator(userManager.getUser());
        }
        companyPdfTemplateManager.createOrUpdate(companyPdfTemplate);

        if (item.isDefaultTemplate() && edsType != null) {
            companyPdfTemplateManager.updateDefaultTemplates(companyPdfTemplate.getObjectID(), edsType.getObjectID());
        }
        return companyPdfTemplate.getObjectID();
    }

    @Override
    public Integer savePdfFooterHeaderSettings(SettingsPdfTemplateItem item) {
        if (item == null) {
            return null;
        }
        EdsPdfTemplateSettings edsSettings = pdfTemplateSettingsManager.getPdfSettings();
        if (edsSettings == null) {
            edsSettings = new EdsPdfTemplateSettings();
        }
        edsSettings.setCustomizedHeader(item.getCustomizedHeader());
        edsSettings.setCompanyLogoEnabled(item.getCompanyLogoEnabled());
        edsSettings.setCompanyNameEnabled(item.getCompanyNameEnabled());
        edsSettings.setCompanyNameFontSize(item.getCompanyNameFontSize());
        edsSettings.setCompanyNameFontColor(item.getCompanyNameFontColor());
        edsSettings.setPaginationEnabled(item.getPaginationEnabled());
        edsSettings.setDocumentTitleEnabled(item.getDocumentTitleEnabled());
        edsSettings.setDocumentTitleFontSize(item.getDocumentTitleFontSize());
        edsSettings.setDocumentTitleFontColor(item.getDocumentTitleFontColor());
        edsSettings.setQrCodeEnabled(item.getQrCodeEnabled()); //MUNIRUPDATED

        edsSettings.setCustomizedFooter(item.getCustomizedFooter());
        edsSettings.setQrCodeEnabled(item.getQrCodeEnabled());
        edsSettings.setPoweredByEnabled(item.getPoweredByEnabled());
        edsSettings.setCustomAddressEnabled(item.getCustomAddressEnabled());
        edsSettings.setCustomAddress(item.getCustomAddress());
        edsSettings.setCustomAddressFontSize(item.getCustomAddressFontSize());
        edsSettings.setCustomAddressFontColor(item.getCustomAddressFontColor());
        pdfTemplateSettingsManager.createOrUpdate(edsSettings);
        EdsCompanyPdfTemplate edsTemplate = companyPdfTemplateManager.get(item.getObjectId());
        if (edsTemplate != null) {
            edsTemplate.setDocumentTitle(item.getDocumentTitle());
            companyPdfTemplateManager.update(edsTemplate);
        }
        final Integer templateId = edsSettings.getObjectID();
        EdsPdfTemplateSettings s = edsSettings;
        List<EdsPdfDynamicFooterHeader> dynamicFooterHeaders = new ArrayList<>();
        if (item.getValueByPosition() != null) {
            item.getValueByPosition().forEach((value) -> {
//                if (templateId == null)
                EdsPdfDynamicFooterHeader dynamic = dynamicFooterHeaderManager.getByKeyAndTemplateSettingId(value.getPosition(), templateId);
                if (dynamic == null) {
                    dynamic = new EdsPdfDynamicFooterHeader();
                }
                dynamic.setKey(value.getPosition());
                dynamic.setValue(ServerUtils.decrypt(value.getContent()));
                dynamic.setEnable(value.getEnable());
                dynamic.setTemplate(s);
                dynamicFooterHeaderManager.createOrUpdate(dynamic);
                dynamicFooterHeaders.add(dynamic);
            });
        }
        edsSettings.setDynamicFooterHeaders(dynamicFooterHeaders);
        return null;
    }

    @Override
    public Integer savePdfContentSettings(SettingsPdfTemplateItem item) {
        if (item == null || item.getTableColumns() == null || item.getTableColumns().isEmpty()) {
            return null;
        }
        PdfTemplateTypeEnum typeEnum = PdfTemplateTypeEnum.get(item.getPdfType());
        if (typeEnum == null) {
            return null;
        }
        EdsPdfTemplateSettings edsSettings = pdfTemplateSettingsManager.getPdfSettings();
        if (edsSettings == null) {
            edsSettings = new EdsPdfTemplateSettings();
        }
        edsSettings.setCustomizedContent(true);
        edsSettings.setTableBorderEnabled(item.getTableBorderEnabled());
        edsSettings.setTableBorderColor(item.getTableBorderColor());
        edsSettings.setItemRowEnabled(item.getItemRowEnabled());
        edsSettings.setItemRowFontSize(item.getItemRowFontSize());
        edsSettings.setItemRowBackgroundColorEnabled(item.getItemRowBackgroundColorEnabled());
        edsSettings.setItemRowBackgroundColor(item.getItemRowBackgroundColor());
        edsSettings.setItemRowFontColor(item.getItemRowFontColor());
        edsSettings.setTableHeaderFontSize(item.getTableHeaderFontSize());
        edsSettings.setTableHeaderBackgroundColorEnabled(item.getTableHeaderBackgroundColorEnabled());
        edsSettings.setTableHeaderBackgroundColor(item.getTableHeaderBackgroundColor());
        edsSettings.setTableHeaderFontColor(item.getTableHeaderFontColor());
        pdfTemplateSettingsManager.createOrUpdate(edsSettings);

        EdsCompanyPdfTemplate edsPdfTemplate = null;
        if (!item.isSystemPdf()) {
            EdsPdfTemplate edsTemplate;
            if (item.getObjectId() != null) {
                edsPdfTemplate = companyPdfTemplateManager.get(item.getObjectId());
                edsTemplate = edsPdfTemplate.getTemplate();
                edsPdfTemplate.setUpdatedDate(new Date());
                edsPdfTemplate.setUpdator(userManager.getUser());
            } else {
                edsPdfTemplate = new EdsCompanyPdfTemplate();
                edsTemplate = new EdsPdfTemplate();
            }
            edsTemplate.setType(pdfReferenceManager.getByCode(typeEnum.name()));
            edsPdfTemplate.setName(item.getPdfName());
            edsPdfTemplate.setTemplate(edsTemplate);
            edsPdfTemplate.setGenerateType(PdfGenerateTypeEnum.PHANTOM_JS);
            edsPdfTemplate.setClientPdf(true);
            companyPdfTemplateManager.createOrUpdate(edsPdfTemplate);
        }

        List<Integer> columnIds = Lists.newArrayList();
        for (PdfTemplateTableSettingsItem tableColumn : item.getTableColumns()) {
            if (!tableColumn.isSelected()) {
                continue;
            }
            EdsPdfTemplateTableSettings edsTableSettings = pdfTemplateTableSettingsManager.getItemByTypeAndTableTypeAndColumnCode(item.getObjectId(),
                                                                                                                                  typeEnum,
                                                                                                                                  PdfTemplateTableTypeEnum.PRODUCT_TABLE,
                                                                                                                                  tableColumn.getColumnCode());
            if (edsTableSettings == null) {
                edsTableSettings = new EdsPdfTemplateTableSettings();
            }
            edsTableSettings.setColumnCode(tableColumn.getColumnCode());
            edsTableSettings.setColumnTitle(tableColumn.getColumnTitle());
            edsTableSettings.setSorder(tableColumn.getSorder());
            edsTableSettings.setWidth(tableColumn.getWidth());
            edsTableSettings.setAlignment(tableColumn.getAlignment());
            edsTableSettings.setCustomField(tableColumn.isCustomField());
            edsTableSettings.setPdfType(typeEnum);
            edsTableSettings.setPdfTableType(PdfTemplateTableTypeEnum.PRODUCT_TABLE);
            edsTableSettings.setCompanyPdfTemplate(edsPdfTemplate);
            pdfTemplateTableSettingsManager.createOrUpdate(edsTableSettings);
            columnIds.add(edsTableSettings.getObjectID());
        }
        if (!columnIds.isEmpty()) {
            pdfTemplateTableSettingsManager.deleteNotExistByIds(item.getObjectId(), columnIds, PdfTemplateTableTypeEnum.PRODUCT_TABLE);
        }
        return edsPdfTemplate != null ? edsPdfTemplate.getObjectID() : null;
    }

    @Override
    public SettingsPdfTemplateGenerateItem generateSettingsPdf(SettingsPdfTemplateItem item) {
        EdsUser edsUser = userManager.getUser();
        if (edsUser == null || edsUser.getCompany() == null) {
            return null;
        }
        PdfReferenceCodeNameEnum typeEnum = PdfReferenceCodeNameEnum.get(item.getPdfType());
        if (typeEnum == null) {
            return null;
        }
        PdfParams pdfParams = new PdfParams();
        pdfParams.setCompanyLogoEnabled(item.getCompanyLogoEnabled());
        pdfParams.setCompanyNameEnabled(item.getCompanyNameEnabled());
        pdfParams.setCompanyNameFontSize(item.getCompanyNameFontSize());
        pdfParams.setCompanyNameFontColor(item.getCompanyNameFontColor());
        pdfParams.setPaginationEnabled(item.getPaginationEnabled());
        pdfParams.setDocumentTitleEnabled(item.getDocumentTitleEnabled());
        pdfParams.setDocumentTitleFontSize(item.getDocumentTitleFontSize());
        pdfParams.setDocumentTitleFontColor(item.getDocumentTitleFontColor());
        pdfParams.setQrCodeEnabled(item.getQrCodeEnabled());//MUNIRUPDATED

        pdfParams.setTableBorderEnabled(item.getTableBorderEnabled());
        pdfParams.setTableBorderColor(item.getTableBorderColor());
        pdfParams.setItemRowEnabled(item.getItemRowEnabled());
        pdfParams.setItemRowFontSize(item.getItemRowFontSize());
        pdfParams.setItemRowBackgroundColorEnabled(item.getItemRowBackgroundColorEnabled());
        pdfParams.setItemRowBackgroundColor(item.getItemRowBackgroundColor());
        pdfParams.setItemRowFontColor(item.getItemRowFontColor());
        pdfParams.setTableHeaderFontSize(item.getTableHeaderFontSize());
        pdfParams.setTableHeaderBackgroundColorEnabled(item.getTableHeaderBackgroundColorEnabled());
        pdfParams.setTableHeaderBackgroundColor(item.getTableHeaderBackgroundColor());
        pdfParams.setTableHeaderFontColor(item.getTableHeaderFontColor());

        pdfParams.setQrCodeEnabled(item.getQrCodeEnabled());
        pdfParams.setPoweredByEnabled(item.getPoweredByEnabled());
        pdfParams.setCustomAddressEnabled(item.getCustomAddressEnabled());
        pdfParams.setCustomAddress(item.getCustomAddress());
        pdfParams.setCustomAddressFontSize(item.getCustomAddressFontSize());
        pdfParams.setCustomAddressFontColor(item.getCustomAddressFontColor());

        if (item.getTableColumns() != null && !item.getTableColumns().isEmpty()) {
            List<PdfTemplateTableSettingsItem> columns = Lists.newArrayList();
            for (PdfTemplateTableSettingsItem column : item.getTableColumns()) {
                if (!column.isSelected()) {
                    continue;
                }
                columns.add(column);
            }
            pdfParams.setTableColumns(columns);
        }
        String jsonFileName = switch (typeEnum) {
            case SALES_INVOICE -> "sales_invoice.json";
            case RECEIVABLE_CREDIT_NOTE -> "credit_note.json";
            case PAYABLE_CREDIT_NOTE -> "debit_note.json";
            case SALES_QUOTE -> "sales_quote.json";
            case SALES_ORDER -> "sales_order.json";
            case PURCHASE_INVOICE -> "purchase_invoice.json";
            case PURCHASE_ORDER -> "purchase_order.json";
            case EXPENSE_REPORT -> "expense_report.json";
            case RFQ -> "request_for_quote.json";
            case MANUAL_ENTRY -> "manual_entry.json";
            case BATCH_RECEIVE_PAYMENT, BATCH_PAY_BILL -> "batch_receive_payment.json";
            case BANK_PAYMENT, BANK_RECEIPT, CASH_PAYMENT, CASH_RECEIPT -> "spend_receive_money.json";
            case GOODS_DELIVERED_NOTES, GOODS_RECEIVED_NOTES -> "grn_gdn.json";
            default -> "sales_invoice.json";
        };
        ITextGenericPdfData pdfData = getPdfSampleData(item, jsonFileName);

        if (StringUtils.isNotEmpty(item.getDocumentTitle())) {
            pdfData.setTableName(item.getDocumentTitle());
        } else {
            pdfData.setTableName(getDefaultPdfName(typeEnum.name()));
        }
        pdfData.setParams(pdfParams);
        pdfData.setCompanyData(generatePdfCompanyData(item));

        SettingsPdfTemplateGenerateItem result = new SettingsPdfTemplateGenerateItem();
        Map<String, String> localizeMap = new HashMap<>();
        localizeMap.put("PAGE_LABEL", pdfWfmMessageSource.localize("page"));
        localizeMap.put("OF_LABEL", pdfWfmMessageSource.localize("of"));
        localizeMap.put("POWERED_BY_LABEL", pdfWfmMessageSource.localize("poweredBy"));
        localizeMap.put("REGISTRATED_OFFICE_LABEL", pdfWfmMessageSource.localize("registratedOffice"));
        localizeMap.put("FAX_LABEL", pdfWfmMessageSource.localize("fax"));
        localizeMap.put("PHONE_LABEL", pdfWfmMessageSource.localize("phone"));
        localizeMap.put("EMAIL_LABEL", pdfWfmMessageSource.localize("email"));
        pdfData.setLocalizeMap(localizeMap);
        if (item.getValueByPosition() != null && item.getValueByPosition().size() > 0) {
            item.getValueByPosition().forEach(val -> {
                String res = ServerUtils.decrypt(val.getContent());
                val.setContent(res);
            });
        }
        result.setBodyHtml(replaceHtmlContent(null, pdfData, typeEnum.getUrl(), true));
        result.setHeaderHtml(replaceHtmlContent(item, pdfData, "header.html", false));
        result.setFooterHtml(replaceHtmlContent(item, pdfData, "footer.html", false));

        return result;
    }

    private String replaceCustomPdfDatas(ArrayList<PdfFooterHeaderContentItem> pdfTemplateItems, String content, ITextGenericPdfData pdfData) {
        if (pdfTemplateItems != null && pdfTemplateItems.size() > 0) {
            ArrayList<PdfFooterHeaderContentItem> replacedValuMap = getReplacedValueMap(new ArrayList<>(pdfTemplateItems), pdfData);
            for (PdfFooterHeaderContentItem v : replacedValuMap) {
                if (v.getEnable() && v.getContent() != null && !v.getPosition().contains(DEFAULT_)) {
                    content = content.replace(v.getPosition(), v.getContent());
                } else if (v.getPosition().contains(DEFAULT_)) {
                    String key = v.getPosition().substring(DEFAULT_.length());
                    content = content.replace(key, v.getContent());
                } else {
                    content = content.replace(v.getPosition(), "");
                }
            }
        } else {
            content = content.replace(Constants.FOOTER_RIGHT, "");
            content = content.replace(Constants.FOOTER_CENTER, "");
            content = content.replace(Constants.FOOTER_LEFT, "");
            content = content.replace(Constants.HEADER_LEFT, "");
            content = content.replace(Constants.HEADER_CENTER, "");
            content = content.replace(Constants.HEADER_RIGHT, "");
        }
        return content;
    }

    private ArrayList<PdfFooterHeaderContentItem> getReplacedValueMap(ArrayList<PdfFooterHeaderContentItem> contentItems, ITextGenericPdfData pdfData) {
        String[] valueCodes = PdfFooteHederAttributeEnum.getCodesAsArray();
        HashMap<String, String> realValues = getAttributeMapByValue(pdfData);
        ArrayList<PdfFooterHeaderContentItem> result = new ArrayList<>();

        for (PdfFooterHeaderContentItem fh : contentItems) {
            PdfFooterHeaderContentItem item = new PdfFooterHeaderContentItem(fh.getPosition(), ServerUtils.isNullOrEmpty(fh.getContent()) ? "" : fh.getContent(), fh.getEnable());
            for (String str : valueCodes) {
                if ((item.getContent().contains(str))) {
                    item.setContent(item.getContent().replace(str, realValues.get(str) != null ? realValues.get(str) : ""));
                }
            }
            result.add(item);
        }
        return result;
    }

    private HashMap<String, String> getAttributeMapByValue(ITextGenericPdfData pdfData) {
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        ITextCompanyData companyData = pdfData.getCompanyData();

        String companyLogo = "<img alt=\"company logo\" src=\"" + companyData.getCompanyLogoUrl() + "\"" + " style=\"width: 66px; height: 66px\"/>";
        String companyName = "<div style=\"vertical-align: top;height: 70px; padding-top: 19px; white-space: nowrap;font-size: $fontSize;color: $fontColor;\"\n" +
                "                width=\"34%\">" +
                "                <strong>" + (!ServerUtils.isNullOrEmpty(company.getName()) ? company.getName() : "") + "</strong> </div>";
        String companyWebsite = "<a href=\"http://" + companyData.getWebsite() + "\">" + companyData.getWebsite() + "</a>";
        String poweredBy = "<div style=\"font-size: 7pt; margin-bottom:6px;\"> $poweredByLabel <a href=\"#\">www.kpi.com</a></div>";
        String pagination = "<span>$pageLabel <b>{#pageNum}</b> $ofLabel <b>{#numPages}</b></span>";
        String qrCode = "<img alt=\"images\"  src=\"https://workforcetrack.s3.amazonaws.com/000000000000/public/65159/a2113e82-397f-49fd-a58a-cdf4f4b93dff?AWSAccessKeyId=AKIAIROQMC77E5UKWBWQ\" style=\"width: 60px; vertical-align: bottom; height: 60px; margin-right:15px;\"/>";
        String phoneNumber = !ServerUtils.isNullOrEmpty(company.getPhone()) ? "$phoneLabel:" + company.getPhone() : "";
        String email = !ServerUtils.isNullOrEmpty(company.getEmail()) ? " <a href=\"mailto:" + company.getEmail() + "\">" + company.getEmail() + "</a>" : "";
        String locationEmail = !ServerUtils.isNullOrEmpty(user.getLocation().getEmail()) ? " <a href=\"mailto:" + user.getLocation().getEmail() + "\">" + user.getLocation().getEmail() + "</a>" : "";
        String faxNum = !ServerUtils.isNullOrEmpty(company.getFaxNumber()) ? "$faxLabel:" + company.getFaxNumber() : "";
        String address = getCompanyAdress(companyData);

        HashMap<String, String> realValues = new HashMap<>();
        realValues.put(PdfFooteHederAttributeEnum.COMPANY_LOGO.getCode(), companyLogo);
        realValues.put(PdfFooteHederAttributeEnum.COMPANY_NAME.getCode(), companyName);
        realValues.put(PdfFooteHederAttributeEnum.COMPANY_MAIN_ADDRESS.getCode(), address);
        realValues.put(PdfFooteHederAttributeEnum.COMPANY_WEBSITE.getCode(), companyWebsite);
        realValues.put(PdfFooteHederAttributeEnum.POWERED_BY.getCode(), poweredBy);
        realValues.put(PdfFooteHederAttributeEnum.PAGINATION.getCode(), pagination);
        realValues.put(PdfFooteHederAttributeEnum.QR_CODE.getCode(), qrCode);
        realValues.put(PdfFooteHederAttributeEnum.PHONE_NUMBER.getCode(), phoneNumber);
        realValues.put(PdfFooteHederAttributeEnum.EMAIL_ID.getCode(), email);
        realValues.put(PdfFooteHederAttributeEnum.FAX_NUM.getCode(), faxNum);
        realValues.put(PdfFooteHederAttributeEnum.DOCUMENT_TITLE.getCode(), !ServerUtils.isNullOrEmpty(pdfData.getTableName()) ? pdfData.getTableName() : "");
        if (user != null && user.getLocation() != null) {
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_ADRESS.getCode(), getUserLocationAddress(user.getLocation()));
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_PHONE.getCode(), !ServerUtils.isNullOrEmpty(user.getLocation().getPhone()) ? user.getLocation().getPhone() : "");
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_EMAIL.getCode(), locationEmail);
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_ZIP_CODE.getCode(), !ServerUtils.isNullOrEmpty(user.getLocation().getZipCode()) ? user.getLocation().getZipCode() : "");
        } else {
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_ADRESS.getCode(), "");
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_PHONE.getCode(), "");
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_EMAIL.getCode(), "");
            realValues.put(PdfFooteHederAttributeEnum.USER_LOCATION_ZIP_CODE.getCode(), "");
        }
        return realValues;
    }

    private String getUserLocationAddress(EdsLocation location) {
        StringBuilder address = new StringBuilder();
        if (location.getState() != null && !ServerUtils.isNullOrEmpty(location.getState().getName())) {
            address.append(location.getState().getName());
            address.append(", ");
        }
        if (location.getCityDistrict() != null && !ServerUtils.isNullOrEmpty(location.getCityDistrict().getName())) {
            address.append(location.getCityDistrict().getName());
            address.append(", ");
        }
        if (!ServerUtils.isNullOrEmpty(location.getCity())) {
            address.append(location.getCity());
            address.append(", ");
        }
        if (location.getCountry() != null && !ServerUtils.isNullOrEmpty(location.getCountry().getName())) {
            address.append(location.getCountry().getName());
        }
        return getValueWithParagraphTeg(address.toString());
    }

    private String getCompanyAdress(ITextCompanyData companyData) {
        StringBuilder address = new StringBuilder();
        StringBuilder ad = new StringBuilder();
        if (!ServerUtils.isNullOrEmpty(companyData.getAddress())) {
            address.append(getValueWithParagraphTeg(companyData.getAddress()));
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getAddress2())) {
            address.append(companyData.getAddress2()).append(", ");
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getCity())) {
            ad.append(companyData.getCity());
            ad.append(", ");
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getState())) {
            ad.append(companyData.getState());
            ad.append(", ");
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getPostCode())) {
            ad.append(companyData.getPostCode());
            ad.append(", ");
        }
        if (!ad.toString().isEmpty()) {
            address.append(ad);
        }
        if (!ServerUtils.isNullOrEmpty(companyData.getCountry())) {
            address.append(companyData.getCountry());
        }
        return address.toString();
    }

    private String getValueWithParagraphTeg(String val) {
        String result = val +
                "<br>";
        return result;
    }

    private ITextCompanyData generatePdfCompanyData(SettingsPdfTemplateItem item) {
        EdsUser edsUser = userManager.getUser();
        if (edsUser == null || edsUser.getCompany() == null) {
            return null;
        }
        EdsCompany edsCompany = edsUser.getCompany();
        ITextCompanyData companyData = new ITextCompanyData();
        companyData.setCompanyName(edsCompany.getName());
        String imageUrl = null;
        try {
            imageUrl = getPdfLogoUrl(edsUser.getCompany());
            if (imageUrl != null) {
                companyData.setCompanyLogoUrl(imageUrl.replaceAll("[&]", "&amp;"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        companyData.setAddress(edsCompany.getAddress1() != null ? edsCompany.getAddress1() : "");
        companyData.setAddress2(edsCompany.getBillAddress2() != null ? edsCompany.getBillAddress2() : "");
        companyData.setCity(edsCompany.getCity() != null ? edsCompany.getCity() : "");
        companyData.setState(edsCompany.getCountryRegion() != null ? edsCompany.getCountryRegion().getName() : "");
        companyData.setCountry((edsCompany.getCountryZone() != null && edsCompany.getCountryZone().getCountry() != null)
                               ? edsCompany.getCountryZone().getCountry().getName()
                               : "");
        companyData.setCompanyFax(StringUtils.isNotEmpty(edsCompany.getFaxNumber()) ? edsCompany.getFaxNumber() : "");
        companyData.setCompanyPhone(StringUtils.isNotEmpty(edsCompany.getPhone()) ? edsCompany.getPhone() : "");
        companyData.setCompanyEmail(StringUtils.isNotEmpty(edsCompany.getEmail()) ? edsCompany.getEmail() : "");

        EdsCompanyPayrollSettings companyWebsite = companyPayrollSettingsManager.getCompanySettingValue(Constants.WEBSITE);
        companyData.setWebsite(companyWebsite != null && companyWebsite.getValue() != null
                               ? companyWebsite.getValue()
                               : "");

        EdsCompanySettings companySettings = edsCompany.getCompanySettings();

        return companyData;
    }

    private ITextGenericPdfData getPdfSampleData(SettingsPdfTemplateItem item, String fileName) {
        EdsUser edsUser = userManager.getUser();
        if (edsUser == null || edsUser.getCompany() == null) {
            return null;
        }
        EdsCompany edsCompany = edsUser.getCompany();
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextBaseInvoice baseInvoice = new ITextBaseInvoice();
        baseInvoice.setCurrency("USD");
        baseInvoice.setCurrencyName("USD");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        InputStream is = this.getClass().getClassLoader().getResourceAsStream("template/sampledata/" + fileName);
        try {
            JSONObject parentObjectJSON = (JSONObject) new JSONParser().parse(IOUtils.toString(is, StandardCharsets.UTF_8));
            JSONObject innerObjectJSON = (JSONObject) parentObjectJSON.get("customProductTable");
            if (innerObjectJSON != null) {
                baseInvoice.setCustomProductTable(mapper.readValue(innerObjectJSON.toJSONString(), CustomisedITextTable.class));
            }
            JSONObject numberAndDatesJSON = (JSONObject) parentObjectJSON.get("customNumberAndDatesTable");
            if (numberAndDatesJSON != null) {
                baseInvoice.setCustomNumberAndDatesTable(mapper.readValue(numberAndDatesJSON.toJSONString(), CustomisedITextTable.class));
            }
            JSONObject totalJSON = (JSONObject) parentObjectJSON.get("customTotalTable");
            if (totalJSON != null) {
                baseInvoice.setCustomTotalTable(mapper.readValue(totalJSON.toJSONString(), CustomisedITextTable.class));
            }
            JSONObject billAddressJSON = (JSONObject) parentObjectJSON.get("customBillToAddress");
            if (billAddressJSON != null) {
                baseInvoice.setCustomBillToAddress(mapper.readValue(billAddressJSON.toJSONString(), CustomisedITextTable.class));
            }
            JSONObject customDataJSON = (JSONObject) parentObjectJSON.get("customData");
            if (customDataJSON != null && customDataJSON.get("ITEMS") != null) {
                JSONObject itemsObjectJSON = (JSONObject) customDataJSON.get("ITEMS");
                HashMap<String, CustomisedITextTable> itemsMap = Maps.newHashMap();
                itemsMap.put("ITEMS", mapper.readValue(itemsObjectJSON.toJSONString(), CustomisedITextTable.class));
                pdfData.setCustomData(itemsMap);
            }
        } catch (ParseException e) {
            log.error("Parse exception", e);
        } catch (JsonParseException e) {
            log.error("Parse JSON exception", e);
        } catch (JsonMappingException e) {
            log.error("Parse Mapping exception", e);
        } catch (IOException e) {
            log.error("Parse IO exception", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("InputStream close exception", e);
                }
            }
        }
        CustomisedITextTable termsTable = new CustomisedITextTable();
        termsTable.addColumnOrder(PDFConstants.COLUMN_VALUE);
        termsTable.addHeaderColumns("Payment instructions:");
        termsTable.addRow("Lorem ipsum dolor sit amet, consectetur adipisicing elit. A alias amet animi, cumque, dignissimos doloremque et id inventore magnam minima odit quaerat quos repellat, repellendus sunt tempora tenetur? Facilis, rem.");
        baseInvoice.setCustomTermsConditions(termsTable);

        CustomisedITextTable introductionTable = new CustomisedITextTable();
        introductionTable.addColumnOrder(PDFConstants.COLUMN_VALUE);
        introductionTable.addHeaderColumns("Introduction:");
        introductionTable.addRow("Payment must be paid strictly within 5 days");
        introductionTable.addRow("for this invoice number IVN0001 as indicated in due date of 17-01-2019.");
        baseInvoice.setCustomIntroduction(introductionTable);

        pdfData.setBaseInvoice(baseInvoice);
        return pdfData;
    }

    private String replaceHtmlContent(SettingsPdfTemplateItem pdfTemplate, ITextGenericPdfData pdfData, String s, Boolean isBody) {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("/template/" + s);
        String result = null;
        String htmlContent = "";
        try {
            htmlContent = IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        if (!isBody) {
            if (pdfTemplate != null) {
                htmlContent = replaceCustomPdfDatas(pdfTemplate.getValueByPosition(), htmlContent, pdfData);
            } else {
                List<EdsPdfDynamicFooterHeader> defaultFooterHeaders = dynamicFooterHeaderManager.getDefaultFooterHeaderValues();
                if (defaultFooterHeaders != null && defaultFooterHeaders.size() > 0) {
                    ArrayList<PdfFooterHeaderContentItem> widgetItems = new ArrayList<>();
                    defaultFooterHeaders.forEach(item -> {
                        PdfFooterHeaderContentItem widget = new PdfFooterHeaderContentItem(item.getKey(), item.getValue(), item.getEnable());
                        widgetItems.add(widget);
                    });
                    htmlContent = replaceCustomPdfDatas(widgetItems, htmlContent, pdfData);
                }
            }
        }
        EdsTemplate template = new EdsTemplate(htmlContent);
        try {
            result = template.process(pdfData);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }

    private String getPdfLogoUrl(EdsCompany edsCompany) throws IOException {
        String companyLogoUrl = getCompanyLogoUrl(edsCompany);
        if ((companyLogoUrl == null || "".equals(companyLogoUrl)) && edsCompany.getShowWorkforceLogoOnPDF()) {
            String pdfLogoUrl = EdsContextParams.getPdfLogo();
            if (pdfLogoUrl.startsWith("/")) {
                pdfLogoUrl = pdfLogoUrl.substring(1);
            }
            String fullHost = EdsContextParams.getFullHost();
            if (fullHost.contains("localhost")) {
                fullHost = "https://apps.kpi.com/";
            }
            companyLogoUrl = fullHost + pdfLogoUrl;
        }
        return companyLogoUrl;
    }

    private String getCompanyLogoUrl(EdsCompany company) {
        if (company == null) {
            return null;
        }
        String url = companyAttachmentManager.getCompanyLogoUrl(company, CommandConstants.FOR_PDF);
        if (StringUtil.isEmpty(url) && Constants.LOCAL.equals(EdsContextParams.getUploadType())) {
            SelectItem item = companyAttachmentManager.getCompanyLogo(company, CommandConstants.FOR_PDF);
            if (item != null) {
                EdsCompanyAttachment logo = companyAttachmentManager.get(item.getId());
                url = logo.getLocalPath() + logo.getObjectID();
            }
        }
        return url;
    }

    @Override
    public void resetPdfSettings(Integer tabStep, String pdfType, Integer pdfId) {
        EdsPdfTemplateSettings edsSettings = pdfTemplateSettingsManager.getPdfSettings();
        if (tabStep == 0) {
            EdsCompanyPdfTemplate edsTemplate = companyPdfTemplateManager.get(pdfId);
            if (edsTemplate != null) {
                edsTemplate.setNullMarginTop();
                edsTemplate.setNullMarginRight();
                edsTemplate.setNullMarginBottom();
                edsTemplate.setNullMarginLeft();
                edsTemplate.setOrientation(null);
                companyPdfTemplateManager.update(edsTemplate);
            } else if (edsSettings != null) {
                edsSettings.setNullMarginTop();
                edsSettings.setNullMarginRight();
                edsSettings.setNullMarginBottom();
                edsSettings.setNullMarginLeft();
                edsSettings.setOrientation(null);
            }
        }
        if (edsSettings == null) {
            return;
        }
        if (tabStep == 1) {
            edsSettings.setCustomizedHeader(true);
            edsSettings.setCompanyLogoEnabled(true);
            edsSettings.setCompanyNameEnabled(true);
            edsSettings.setCompanyNameFontSize(null);
            edsSettings.setCompanyNameFontColor(null);
            edsSettings.setPaginationEnabled(true);
            edsSettings.setDocumentTitleEnabled(true);
            edsSettings.setDocumentTitleFontSize(null);
            edsSettings.setDocumentTitleFontColor(null);
            edsSettings.setQrCodeEnabled(true);

            edsSettings.setCustomizedFooter(true);
            edsSettings.setQrCodeEnabled(true);
            edsSettings.setPoweredByEnabled(true);
            edsSettings.setCustomAddressEnabled(false);
            edsSettings.setCustomAddress(null);
            edsSettings.setCustomAddressFontSize(null);
            edsSettings.setCustomAddressFontColor(null);
            edsSettings.setFooterBackgroundColor(null);
            dynamicFooterHeaderManager.updateDynamicSettingsBytemplateId(edsSettings.getObjectID());
        } else if (tabStep == 2) {
            edsSettings.setCustomizedContent(false);
            edsSettings.setTableBorderEnabled(false);
            edsSettings.setTableBorderColor(null);
            edsSettings.setItemRowEnabled(true);
            edsSettings.setItemRowFontSize(null);
            edsSettings.setItemRowBackgroundColorEnabled(false);
            edsSettings.setItemRowBackgroundColor(null);
            edsSettings.setItemRowFontColor(null);
            edsSettings.setTableHeaderFontSize(null);
            edsSettings.setTableHeaderBackgroundColorEnabled(false);
            edsSettings.setTableHeaderBackgroundColor(null);
            edsSettings.setTableHeaderFontColor(null);

            pdfTemplateTableSettingsManager.deleteByTypeAndPdfId(PdfTemplateTypeEnum.get(pdfType), pdfId);
        }
        pdfTemplateSettingsManager.update(edsSettings);
    }

    @Override
    public void deleteSettingsPdfTemplate(Integer objectId) {
        EdsCompanyPdfTemplate edsCompanyPdfTemplate = companyPdfTemplateManager.get(objectId);
        if (edsCompanyPdfTemplate == null) {
            return;
        }
        edsCompanyPdfTemplate.setDeleted(true);
        companyPdfTemplateManager.update(edsCompanyPdfTemplate);

        pdfTemplateTableSettingsManager.deleteByPdfId(objectId);

        EdsPdfTemplate pdfTemplate = edsCompanyPdfTemplate.getTemplate();
        if (pdfTemplate == null) {
            return;
        }
        pdfTemplate.setDeleted(true);
        pdfTemplateManager.update(pdfTemplate);
    }

    @Override
    public ArrayList<SelectItem> getClientPdfTemplatesByType(String type) {
        List<EdsCompanyPdfTemplate> templates = companyPdfTemplateManager.getClientPDFTemplatesByType(type);
        return templates.stream().map(item -> new SelectItem(item.getObjectID(), item.getName())).collect(Collectors.toCollection(ArrayList::new));
    }

    private LinkedHashMap<String, PdfTemplateTableSettingsItem> getAllColumnsByType(PdfTemplateTypeEnum typeEnum) {
        LinkedHashMap<String, PdfTemplateTableSettingsItem> map = Maps.newLinkedHashMap();
        if (typeEnum == null) {
            return map;
        }
        switch (typeEnum) {
            case SALES_INVOICE, RECEIVABLE_CREDIT_NOTE -> {
                map = getDefaultSalesInvoiceColumns();
                getCustomFields(CustomFieldSection.SaleInvoiceItem, map);
            }
            case SALES_QUOTE, SALES_ORDER -> {
                map = getDefaultSalesInvoiceColumns();
                getCustomFields(CustomFieldSection.SaleQuoteItem, map);
            }
            case PURCHASE_INVOICE, PAYABLE_CREDIT_NOTE -> {
                map = getDefaultSalesInvoiceColumns();
                getCustomFields(CustomFieldSection.PurchaseInvoiceItem, map);
            }
            case PURCHASE_ORDER -> {
                map = getDefaultSalesInvoiceColumns();
                getCustomFields(CustomFieldSection.PurchaseOrderItem, map);
            }
            case EXPENSE_REPORT -> {
                map = getDefaultExpenseColumns();
                getCustomFields(CustomFieldSection.ExpenseReportItem, map);
            }
            case RFQ -> {
                map = getDefaultRFQColumns();
                getCustomFields(CustomFieldSection.RFQItem, map);
            }
            case MANUAL_ENTRY -> map = getDefaultManualEntryColumns();
            case BATCH_RECEIVE_PAYMENT, BATCH_PAY_BILL -> map = getDefaultPaymentColumns();
            case BANK_PAYMENT, BANK_RECEIPT, CASH_PAYMENT, CASH_RECEIPT -> map = getDefaultSpendReceiveMoneyColumns();
            case GOODS_DELIVERED_NOTES, GOODS_RECEIVED_NOTES -> map = getDefaultGDNorGRNColumns();
        }
        return map;
    }

    private LinkedHashMap<String, PdfTemplateTableSettingsItem> getDefaultSalesInvoiceColumns() {
        LinkedHashMap<String, PdfTemplateTableSettingsItem> map = new LinkedHashMap<>();
        String productName = pdfWfmMessageSource.localize(PdfLocalizationName.name);
        String description = pdfWfmMessageSource.localize(PdfLocalizationName.description);
        String qty = pdfWfmMessageSource.localize(PdfLocalizationName.qty);
        String unitPrice = pdfWfmMessageSource.localize(PdfLocalizationName.unitPrice);
        String discount = pdfWfmMessageSource.localize(PdfLocalizationName.discount);
        String tax = pdfWfmMessageSource.localize(PdfLocalizationName.tax);
        String amount = pdfWfmMessageSource.localize(PdfLocalizationName.amount);
        String measurement = pdfWfmMessageSource.localize(PdfLocalizationName.unitOfMeasure);
        String no = pdfWfmMessageSource.localize(PdfLocalizationName.number);

        map.put(PDFConstants.ITEM_NAME, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_NAME, productName, productName, true, 15, 0));
        map.put(PDFConstants.ITEM_DESCRIPTION, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_DESCRIPTION, description, description, true, 25, 0));
        map.put(PDFConstants.ITEM_QTY_HRS, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_QTY_HRS, qty, qty, true, 12, 2));
        map.put(PDFConstants.ITEM_UNIT_PRICE, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_UNIT_PRICE, unitPrice, unitPrice, true, 12, 2));
        map.put(PDFConstants.ITEM_DISCOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_DISCOUNT, discount, discount, true, 10, 2));
        map.put(PDFConstants.ITEM_TAX_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_TAX_AMOUNT, tax, tax, true, 10, 2));
        map.put(PDFConstants.ITEM_TOTAL_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_TOTAL_AMOUNT, amount, amount, true, 16, 2));
        map.put(PDFConstants.ITEM_UNIT_MEASUREMENT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_UNIT_MEASUREMENT, measurement, measurement, false, 10, 0));
        map.put(PDFConstants.ITEM_NO, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_NO, no, no, false, 5, 0));
        return map;
    }

    private LinkedHashMap<String, PdfTemplateTableSettingsItem> getDefaultExpenseColumns() {
        LinkedHashMap<String, PdfTemplateTableSettingsItem> map = new LinkedHashMap<>();
        String category = commonLocalizer.localizeAccounting(PdfLocalizationName.category);
        String description = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description);
        String units = commonLocalizer.localizeAccounting(PdfLocalizationName.units);
        String costPerUnit = commonLocalizer.localizeAccounting(PdfLocalizationName.costPerUnit);
        String tax = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.tax);
        String total = commonLocalizer.localizeAccounting(PdfLocalizationName.total);
        String baseTotal = accountingLocalizer.localizeAccounting(PdfLocalizationName.baseTotal);
        String billTo = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.billTo);
        String department = commonLocalizer.localize(PdfLocalizationName.department);


        map.put(PDFConstants.ITEM_NAME, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_NAME, category, category, true, 22, 0));
        map.put(PDFConstants.ITEM_DESCRIPTION, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_DESCRIPTION, description, description, true, 25, 0));
        map.put(PDFConstants.ITEM_QTY_HRS, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_QTY_HRS, units, units, true, 12, 2));
        map.put(PDFConstants.ITEM_UNIT_PRICE, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_UNIT_PRICE, costPerUnit, costPerUnit, true, 15, 2));
        map.put(PDFConstants.ITEM_TAX_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_TAX_AMOUNT, tax, tax, true, 10, 2));
        map.put(PDFConstants.ITEM_TOTAL_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_TOTAL_AMOUNT, total, total, true, 16, 2));
        map.put(PDFConstants.ITEM_BASE_TOTAL, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_BASE_TOTAL, baseTotal, baseTotal, false, 15, 2));
        map.put(PDFConstants.EXP_BILL_TO, new PdfTemplateTableSettingsItem(PDFConstants.EXP_BILL_TO, billTo, billTo, false, 15, 0));
        map.put(PDFConstants.ITEM_DEPARTMENT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_DEPARTMENT, department, department, false, 15, 0));
        return map;
    }

    private LinkedHashMap<String, PdfTemplateTableSettingsItem> getDefaultRFQColumns() {
        LinkedHashMap<String, PdfTemplateTableSettingsItem> map = new LinkedHashMap<>();
        String number = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number);
        String name = commonLocalizer.localizeAccounting(PdfLocalizationName.name);
        String description = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description);
        String qty = commonLocalizer.localizeAccounting(PdfLocalizationName.qty);
        String commission = commonLocalizer.localizeAccounting(PdfLocalizationName.commission);
        String um = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.um);
        String cost = commonLocalizer.localize(PdfLocalizationName.cost);
        String supplier = commonLocalizer.localizeAccounting(PdfLocalizationName.supplier);
        String remarks = commonLocalizer.localizeAccounting(PdfLocalizationName.remarks);

        map.put(PDFConstants.ITEM_NO, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_NO, number, number, true, 4, 0));
        map.put(PDFConstants.ITEM_NAME, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_NAME, name, name, true, 20, 0));
        map.put(PDFConstants.ITEM_DESCRIPTION, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_DESCRIPTION, description, description, true, 30, 0));
        map.put(Constants.SUPPLIER, new PdfTemplateTableSettingsItem(Constants.SUPPLIER, supplier, supplier, true, 25, 0));
        map.put(PDFConstants.QTY, new PdfTemplateTableSettingsItem(PDFConstants.QTY, qty, qty, true, 10, 2));
        map.put(PDFConstants.ITEM_COST_PRICE, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_COST_PRICE, cost, cost, true, 11, 0));
        map.put(PDFConstants.ITEM_COMISSION, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_COMISSION, commission, commission, false, 12, 2));
        map.put(PDFConstants.UNIT, new PdfTemplateTableSettingsItem(PDFConstants.UNIT, um, um, false, 8, 0));
        map.put(PDFConstants.REMARKS, new PdfTemplateTableSettingsItem(PDFConstants.REMARKS, remarks, remarks, false, 12, 0));
        return map;
    }

    private LinkedHashMap<String, PdfTemplateTableSettingsItem> getDefaultManualEntryColumns() {
        LinkedHashMap<String, PdfTemplateTableSettingsItem> map = new LinkedHashMap<>();
        String account = commonLocalizer.localizeAccounting(PdfLocalizationName.account);
        String name = commonLocalizer.localizeAccounting(PdfLocalizationName.name);
        String description = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description);
        String debit = accountingLocalizer.localizeAccounting(PdfLocalizationName.debit);
        String credit = accountingLocalizer.localizeAccounting(PdfLocalizationName.credit);
        String project = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project);
        String department = commonLocalizer.localize(PdfLocalizationName.department);
        String reference = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference);
        String accountCode = commonLocalizer.localizeAccounting(PdfLocalizationName.accountCode);
        String parentProject = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.parentProject);

        map.put(PDFConstants.ACCOUNT_NAME, new PdfTemplateTableSettingsItem(PDFConstants.ACCOUNT_NAME, account, account, true, 14, 0));
        map.put(PDFConstants.ITEM_NAME, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_NAME, name, name, true, 14, 0));
        map.put(PDFConstants.ITEM_DESCRIPTION, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_DESCRIPTION, description, description, true, 18, 0));
        map.put(PDFConstants.DEBIT, new PdfTemplateTableSettingsItem(PDFConstants.DEBIT, debit, debit, true, 14, 2));
        map.put(PDFConstants.CREDIT, new PdfTemplateTableSettingsItem(PDFConstants.CREDIT, credit, credit, true, 14, 2));
        map.put(PDFConstants.RELATED_PROJECT, new PdfTemplateTableSettingsItem(PDFConstants.RELATED_PROJECT, project, project, true, 13, 0));
        map.put(Constants.DEPARTMENT, new PdfTemplateTableSettingsItem(Constants.DEPARTMENT, department, department, true, 13, 0));
        map.put(PDFConstants.REFERENCE, new PdfTemplateTableSettingsItem(PDFConstants.REFERENCE, reference, reference, false, 15, 0));
        map.put(PDFConstants.ACCOUNT_CODE, new PdfTemplateTableSettingsItem(PDFConstants.ACCOUNT_CODE, accountCode, accountCode, false, 15, 0));
        map.put(PDFConstants.PARENT_PROJECT, new PdfTemplateTableSettingsItem(PDFConstants.PARENT_PROJECT, parentProject, parentProject, false, 15, 0));
        return map;
    }

    private LinkedHashMap<String, PdfTemplateTableSettingsItem> getDefaultPaymentColumns() {
        LinkedHashMap<String, PdfTemplateTableSettingsItem> map = new LinkedHashMap<>();
        String description = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description);
        String reference = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference);
        String invoiceDate = commonLocalizer.localize(PdfLocalizationName.invoiceDate);
        String amount = accountingLocalizer.localizeAccounting(PdfLocalizationName.amount);
        String pAmount = accountingLocalizer.localizeAccounting(PdfLocalizationName.paymentAmount);
        String account = accountingLocalizer.localizeAccounting(PdfLocalizationName.account);
        String project = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project);
        String poNumber = accountingLocalizer.localizeAccounting(PdfLocalizationName.poNumber);
        String dueAmount = commonLocalizer.localizeAccounting(PdfLocalizationName.dueAmount);
        String baseTotal = commonLocalizer.localizeAccounting(PdfLocalizationName.baseTotal);
        String currency = commonLocalizer.localizeAccounting(PdfLocalizationName.currency);

        map.put(PDFConstants.INV_NUMBER, new PdfTemplateTableSettingsItem(PDFConstants.INV_NUMBER, description, description, true, 15, 0));
        map.put(PDFConstants.ITEM_PAYMENT_REFERENCE, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_PAYMENT_REFERENCE, reference, reference, true, 32, 0));
        map.put(PDFConstants.INV_DATE, new PdfTemplateTableSettingsItem(PDFConstants.INV_DATE, invoiceDate, invoiceDate, true, 15, 0));
        map.put(PDFConstants.ITEM_TOTAL_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_TOTAL_AMOUNT, pAmount, pAmount, true, 19, 2));
        map.put(PDFConstants.ITEM_NET_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_NET_AMOUNT, amount, amount, true, 19, 2));
        map.put(PDFConstants.ACCOUNT_NAME, new PdfTemplateTableSettingsItem(PDFConstants.ACCOUNT_NAME, account, account, false, 15, 0));
        map.put(PDFConstants.PROJECT_NAME, new PdfTemplateTableSettingsItem(PDFConstants.PROJECT_NAME, project, project, false, 15, 0));
        map.put(PDFConstants.PO_NUMBER, new PdfTemplateTableSettingsItem(PDFConstants.PO_NUMBER, poNumber, poNumber, false, 15, 0));
        map.put(PDFConstants.DUE_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.DUE_AMOUNT, dueAmount, dueAmount, false, 15, 2));
        map.put(PDFConstants.ITEM_BASE_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_BASE_AMOUNT, baseTotal, baseTotal, false, 15, 2));
        map.put(PDFConstants.CURRENCY, new PdfTemplateTableSettingsItem(PDFConstants.CURRENCY, currency, currency, false, 10, 0));
        return map;
    }

    private LinkedHashMap<String, PdfTemplateTableSettingsItem> getDefaultSpendReceiveMoneyColumns() {
        LinkedHashMap<String, PdfTemplateTableSettingsItem> map = new LinkedHashMap<>();
        String account = accountingLocalizer.localizeAccounting(PdfLocalizationName.account);
        String description = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.description);
        String reference = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.reference);
        String name = accountingLocalizer.localizeAccounting(PdfLocalizationName.name);
        String amount = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.amount);
        String tax = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.tax);
        String taxRate = commonLocalizer.localizeAccounting(PdfLocalizationName.taxRate);
        String project = pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.project);
        String baseTotal = commonLocalizer.localizeAccounting(PdfLocalizationName.baseTotal);
        String department = commonLocalizer.localize(PdfLocalizationName.department);

        map.put(PDFConstants.ACCOUNT_NAME, new PdfTemplateTableSettingsItem(PDFConstants.ACCOUNT_NAME, account, account, true, 20, 0));
        map.put(PDFConstants.DESCRIPTION, new PdfTemplateTableSettingsItem(PDFConstants.DESCRIPTION, description, description, true, 20, 0));
        map.put(PDFConstants.REFERENCE, new PdfTemplateTableSettingsItem(PDFConstants.REFERENCE, reference, reference, true, 15, 0));
        map.put(PDFConstants.NAME, new PdfTemplateTableSettingsItem(PDFConstants.NAME, name, name, true, 15, 0));
        map.put(PDFConstants.ITEM_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_AMOUNT, amount, amount, true, 15, 2));
        map.put(PDFConstants.ITEM_TAX_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_TAX_AMOUNT, tax, tax, true, 15, 2));
        map.put(PDFConstants.ITEM_TAX_RATE, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_TAX_RATE, taxRate, taxRate, false, 15, 2));
        map.put(PDFConstants.ITEM_BASE_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_BASE_AMOUNT, baseTotal, baseTotal, false, 10, 2));
        map.put(PDFConstants.RELATED_PROJECT, new PdfTemplateTableSettingsItem(PDFConstants.RELATED_PROJECT, project, project, false, 15, 0));
        map.put(PDFConstants.ITEM_DEPARTMENT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_DEPARTMENT, department, department, false, 15, 0));
        return map;
    }

    private LinkedHashMap<String, PdfTemplateTableSettingsItem> getDefaultGDNorGRNColumns() {
        LinkedHashMap<String, PdfTemplateTableSettingsItem> map = new LinkedHashMap<>();
        String number = pdfWfmMessageSource.localize(PdfLocalizationName.number);
        String name = pdfWfmMessageSource.localize(PdfLocalizationName.name);
        String description = pdfWfmMessageSource.localize(PdfLocalizationName.description);
        String unitOfMeasure = pdfWfmMessageSource.localize(PdfLocalizationName.unitOfMeasure);
        String qty = commonLocalizer.localize("delivered");
        String receiveType = accountingLocalizer.localize(PdfLocalizationName.receiveType);
        String allocate = accountingLocalizer.localize(PdfLocalizationName.allocate);
        String project = pdfWfmMessageSource.localize(PdfLocalizationName.project);
        String unitPrice = pdfWfmMessageSource.localize(PdfLocalizationName.unitPrice);
        String tax = pdfWfmMessageSource.localize(PdfLocalizationName.tax);
        String netAmount = pdfWfmMessageSource.localize(PdfLocalizationName.netAmount);
        String totalAmount = pdfWfmMessageSource.localize(PdfLocalizationName.totalAmount);

        map.put(PDFConstants.ITEM_NO, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_NO, number, number, true, 4, 0));
        map.put(PDFConstants.ITEM_NAME, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_NAME, name, name, true, 25, 0));
        map.put(PDFConstants.DESCRIPTION, new PdfTemplateTableSettingsItem(PDFConstants.DESCRIPTION, description, description, true, 56, 0));
        map.put(PDFConstants.ITEM_ALLOCATE, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_ALLOCATE, allocate, allocate, false, 15, 2));
        map.put(PDFConstants.ITEM_QTY, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_QTY, qty, qty, true, 15, 2));
        map.put(PDFConstants.ITEM_UNIT_MEASUREMENT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_UNIT_MEASUREMENT, unitOfMeasure, unitOfMeasure, false, 20, 0));
        map.put(PDFConstants.ITEM_RECIEVE, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_RECIEVE, receiveType, receiveType, false, 20, 2));
        map.put(PDFConstants.ITEM_PROJECT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_PROJECT, project, project, false, 20, 0));
        map.put(PDFConstants.ITEM_UNIT_PRICE, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_UNIT_PRICE, unitPrice, unitPrice, false, 20, 2));
        map.put(PDFConstants.ITEM_TAX_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_TAX_AMOUNT, tax, tax, false, 20, 2));
        map.put(PDFConstants.ITEM_NET_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_NET_AMOUNT, netAmount, netAmount, false, 20, 2));
        map.put(PDFConstants.ITEM_TOTAL_AMOUNT, new PdfTemplateTableSettingsItem(PDFConstants.ITEM_TOTAL_AMOUNT, totalAmount, totalAmount, false, 20, 2));
        return map;
    }

    @Override
    public void getCustomFields(CustomFieldSection section, LinkedHashMap<String, PdfTemplateTableSettingsItem> columnMap) {
        List<EdsCompanyCustomFieldsSettings> edsFields = companyCustomFieldsManager.getCompanyCustomFieldsByEntityName(section.name());

        if (edsFields == null || edsFields.isEmpty()) {
            return;
        }
        for (EdsCompanyCustomFieldsSettings edsField : edsFields) {
            columnMap.put(edsField.getFieldName(),
                          new PdfTemplateTableSettingsItem(edsField.getFieldName(),
                                                           edsField.getFieldName(),
                                                           edsField.getFieldName(),
                                                           false,
                                                           10,
                                                           getColumnAlignment(edsField.getDataType()), true));
        }
    }

    private int getColumnAlignment(String dataType) {
        if (Constants.DATA_TYPE_NUMBER.equals(dataType)) {
            return 2;
        } else {
            return 0;
        }
    }
}
