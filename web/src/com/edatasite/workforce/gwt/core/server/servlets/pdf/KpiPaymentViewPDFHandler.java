package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.client.rpc.KpiPaymentRequestObject;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCompanyData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ar;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ru;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by dst on 9/7/18.
 */
public class KpiPaymentViewPDFHandler extends AbstractITextPostPdfHandler implements IPostPDFHandler, PDFConstants {

    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;

    @Override
    protected Object getDataClass(HttpServletRequest request) {
        return new KpiPaymentRequestObject();
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        final KpiPaymentRequestObject kpiRequesObj = (KpiPaymentRequestObject) dataClass;
        if (kpiRequesObj == null) {
            return null;
        }

        HashMap<String, CustomisedITextTable> customData = new HashMap<>();

        CustomisedITextTable recurringTable = new CustomisedITextTable();

        //RECCURING SUBSCRIPTION TITLE
        String reccurSubscripTitle = escapeHtml(kpiRequesObj.getReccurSubscripTitle());

        //USERS
        String usersItem = escapeHtml(kpiRequesObj.getUsersItem());
        String usersUnitPrice = escapeHtml(kpiRequesObj.getUsersUnitPrice());
        String usersQty = escapeHtml(kpiRequesObj.getUsersQty());
        String usersTotalYear = escapeHtml(kpiRequesObj.getUsersTotalYear());

        //ESS
        String essItem = escapeHtml(kpiRequesObj.getEssItem());
        String essUnitPrice = escapeHtml(kpiRequesObj.getEssUnitPrice());
        String essQty = escapeHtml(kpiRequesObj.getEssQty());
        String essTotalYear = escapeHtml(kpiRequesObj.getEssTotalYear());

        //NONUSER
        String nonUserItem = escapeHtml(kpiRequesObj.getNonUserItem());
        String nonUserUnitPrice = escapeHtml(kpiRequesObj.getNonUserUnitPrice());
        String nonUserQty = escapeHtml(kpiRequesObj.getNonUserQty());
        String nonUserTotalYear = escapeHtml(kpiRequesObj.getNonUserTotalYear());

        recurringTable.addColumnOrder(COLUMN_VALUE);
        recurringTable.addRowWithCode("RECCURING_SUBSCRIPTION_TITLE", reccurSubscripTitle);
        recurringTable.addRowWithCode("USERS_ITEM", usersItem);
        recurringTable.addRowWithCode("USERS_UNIT_PRICE", usersUnitPrice);
        recurringTable.addRowWithCode("USERS_QTY", usersQty);
        recurringTable.addRowWithCode("USERS_TOTAL_YEAR", usersTotalYear);

        recurringTable.addRowWithCode("ESS_ITEM", essItem);
        recurringTable.addRowWithCode("ESS_UNIT_PRICE", essUnitPrice);
        recurringTable.addRowWithCode("ESS_QTY", essQty);
        recurringTable.addRowWithCode("ESS_TOTAL_YEAR", essTotalYear);

        recurringTable.addRowWithCode("NON_USER_ITEM", nonUserItem);
        recurringTable.addRowWithCode("NON_USER_UNIT_PRICE", nonUserUnitPrice);
        recurringTable.addRowWithCode("NON_USER_QTY", nonUserQty);
        recurringTable.addRowWithCode("NON_USER_TOTAL_YEAR", nonUserTotalYear);

        //ADDONS TILE AND ONLINE TRAINING
        String addonsTitle = escapeHtml(kpiRequesObj.getAddonsTitle());
        String addonsOnlineTrainingItem = escapeHtml(kpiRequesObj.getAddonsOnlineTrainingItem());
        String addonsOnlineTrainingQty = escapeHtml(kpiRequesObj.getAddonsOnlineTrainingQty());
        String addonsOnlineTrainingTotalYear = escapeHtml(kpiRequesObj.getAddonsOnlineTrainingTotalYear());

        //ADDONS INITIAL SET UP PACKAGE
        String addonsInitialSetUpPackageItem = escapeHtml(kpiRequesObj.getAddonsInitialSetUpPackageItem());
        String addonsInitialSetUpPackageQty = escapeHtml(kpiRequesObj.getAddonsInitialSetUpPackageQty());
        String addonsInitialSetUpPackageTotalYear = escapeHtml(kpiRequesObj.getAddonsInitialSetUpPackageTotalYear());

        //ADDONS PREMIUM SUPPORT
        String addonsPremiumSupportItem = escapeHtml(kpiRequesObj.getAddonsPremiumSupportItem());
        String addonsPremiumSupportQty = escapeHtml(kpiRequesObj.getAddonsPremiumSupportQty());
        String addonsPremiumSupportTotalYear = escapeHtml(kpiRequesObj.getAddonsPremiumSupportTotalYear());

        //CUSTOM PDF
        String addonsCustomPDFItem = escapeHtml(kpiRequesObj.getAddonsCustomPDFItem());
        String addonsCustomPDFQty = escapeHtml(kpiRequesObj.getAddonsCustomPDFQty());
        String addonsCustomPDFTotalYear = escapeHtml(kpiRequesObj.getAddonsCustomPDFTotalYear());

        //EXTRA STORAGE
        String addonsExtraStorageItem = escapeHtml(kpiRequesObj.getAddonsExtraStorageItem());
        String addonsExtraStorageQty = escapeHtml(kpiRequesObj.getAddonsExtraStorageQty());
        String addonsExtraStorageTotalYear = escapeHtml(kpiRequesObj.getAddonsExtraStorageTotalYear());

        //DEDICATED DEVELOPER
        String addonsDedicatedDeveloperItem = escapeHtml(kpiRequesObj.getAddonsDedicatedDeveloperItem());
        String addonsDedicatedDeveloperQty = escapeHtml(kpiRequesObj.getAddonsDedicatedDeveloperQty());
        String addonsDedicatedDeveloperTotalYear = escapeHtml(kpiRequesObj.getAddonsDedicatedDeveloperTotalYear());

        CustomisedITextTable addonsTable = new CustomisedITextTable();

        addonsTable.addColumnOrder(COLUMN_VALUE);
        addonsTable.addRowWithCode("ADDONS_TITLE", addonsTitle);
        addonsTable.addRowWithCode("ADDONS_ONLINE_TRAINING_ITEM", addonsOnlineTrainingItem);
        addonsTable.addRowWithCode("ADDONS_ONLINE_TRAINING_QTY", addonsOnlineTrainingQty);
        addonsTable.addRowWithCode("ADDONS_ONLINE_TRAINING_TOTAL_YEAR", addonsOnlineTrainingTotalYear);

        addonsTable.addRowWithCode("ADDONS_INITIAL_SET_UP_PACKAGE_ITEM", addonsInitialSetUpPackageItem);
        addonsTable.addRowWithCode("ADDONS_INITIAL_SET_UP_PACKAGE_QTY", addonsInitialSetUpPackageQty);
        addonsTable.addRowWithCode("ADDONS_INITIAL_SET_UP_PACKAGE_TOTAL_YEAR", addonsInitialSetUpPackageTotalYear);

        addonsTable.addRowWithCode("ADDONS_PREMIUM_SUPPORT_ITEM", addonsPremiumSupportItem);
        addonsTable.addRowWithCode("ADDONS_PREMIUM_SUPPORT_QTY", addonsPremiumSupportQty);
        addonsTable.addRowWithCode("ADDONS_PREMIUM_SUPPORT_TOTAL_YEAR", addonsPremiumSupportTotalYear);

        addonsTable.addRowWithCode("ADDONS_CUSTOM_PDF_ITEM", addonsCustomPDFItem);
        addonsTable.addRowWithCode("ADDONS_CUSTOM_PDF_QTY", addonsCustomPDFQty);
        addonsTable.addRowWithCode("ADDONS_CUSTOM_PDF_TOTAL_YEAR", addonsCustomPDFTotalYear);

        addonsTable.addRowWithCode("ADDONS_EXTRA_STORAGE_ITEM", addonsExtraStorageItem);
        addonsTable.addRowWithCode("ADDONS_EXTRA_STORAGE_QTY", addonsExtraStorageQty);
        addonsTable.addRowWithCode("ADDONS_EXTRA_STORAGE_TOTAL_YEAR", addonsExtraStorageTotalYear);

        addonsTable.addRowWithCode("ADDONS_DEDICATED_DEVELOPER_ITEM", addonsDedicatedDeveloperItem);
        addonsTable.addRowWithCode("ADDONS_DEDICATED_DEVELOPER_QTY", addonsDedicatedDeveloperQty);
        addonsTable.addRowWithCode("ADDONS_DEDICATED_DEVELOPER_TOTAL_YEAR", addonsDedicatedDeveloperTotalYear);

        //users discount
        String usersDiscountTitle = escapeHtml(kpiRequesObj.getUsersDiscountTitle());
        String usersDiscountTotal = escapeHtml(kpiRequesObj.getUsersDiscountTotal());

        //total / subscription
        String totalSubscriptionTitle = escapeHtml(kpiRequesObj.getTotalSubscriptionTitle());
        String totalSubscriptionTotal = escapeHtml(kpiRequesObj.getTotalSubscriptionTotal());

        //total add-on
        String totalAddonTitle = escapeHtml(kpiRequesObj.getTotalAddonTitle());
        String totalAddonTotal = escapeHtml(kpiRequesObj.getTotalAddonTotal());

        //to be paid
        String tobePaidTitle = escapeHtml(kpiRequesObj.getTobePaidTitle());
        String tobePaidTotal = escapeHtml(kpiRequesObj.getTobePaidTotal());
        String paidTotalWord = tobePaidTotal.replace(",", "");

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        int scale = 2;
        if (fs != null && fs.getCalculationScale() != null) {
            scale = fs.getCalculationScale();
        }

        NumberToWord numberToWordConverter = new NumberToWord_en();
        EdsUser user = usagePlanManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        if (userSettings != null && userSettings.getInternationalization() != null) {
            numberToWordConverter = switch (userSettings.getInternationalization()) {
                case UiSettings.ENGLISH -> new NumberToWord_en();
                case UiSettings.ARABIC -> new NumberToWord_ar();
                case UiSettings.RUSSIAN -> new NumberToWord_ru();
                default -> new NumberToWord_en();
            };
        }

        String totalWordAll;
        try {
            totalWordAll = numberToWordConverter.convert(new BigDecimal(paidTotalWord).abs().setScale(scale, BigDecimal.ROUND_HALF_UP));
        } catch (Exception e) {
            totalWordAll = "";
            logger.error("Error occurred while converting tobePaidTotal ".concat(tobePaidTotal).concat(" to BigDecimal"), e);
            logger.error("Company id:" + usagePlanManager.getUser().getCompany().getObjectID());
            logger.error("User id:" + usagePlanManager.getUser().getObjectID());
        }

        CustomisedITextTable totalTable = new CustomisedITextTable();

        totalTable.addColumnOrder(COLUMN_VALUE);
        totalTable.addRowWithCode("USERS_DISCOUNT_TITLE", usersDiscountTitle);
        totalTable.addRowWithCode("USERS_DISCOUNT_TOTAL", usersDiscountTotal);

        totalTable.addRowWithCode("TOTAL_SUBSCRIPTION_TITLE", totalSubscriptionTitle);
        totalTable.addRowWithCode("TOTAL_SUBSCRIPTION_TOTAL", totalSubscriptionTotal);

        totalTable.addRowWithCode("TOTAL_ADDON_TITLE", totalAddonTitle);
        totalTable.addRowWithCode("TOTAL_ADDON_TOTAL", totalAddonTotal);

        totalTable.addRowWithCode("TOBE_PAID_TITLE", tobePaidTitle);
        totalTable.addRowWithCode("TOBE_PAID_TOTAL", tobePaidTotal);

        totalTable.addRowWithCode("PAID_TOTAL_WORD_ALL", totalWordAll);

        customData.put("RECCURING_SUBSCRIPTION_TABLE", recurringTable);
        customData.put("ADDONS_TABLE", addonsTable);
        customData.put("TOTAL_TABLE", totalTable);

        pdfData.setCustomData(customData);
        return pdfData;
    }

    @Override
    protected ITextCompanyData getCompanyData(EdsCompany edsCompany, boolean customised, boolean hasPhantom) {
        ITextCompanyData companyData = new ITextCompanyData();
        companyData.setCompanyName("KPI Software Limited");
        companyData.setAddress("80 Alleyn Park, Dulwich");
        companyData.setAddress2("SE21 8SL");
        companyData.setCity("London");
        companyData.setPostCode("19702");
        companyData.setCountry("United Kingdom");
        companyData.setState("");
        companyData.setCompanyEmail("sales@kpi.com");
        companyData.setCompanyFax("");
        companyData.setCompanyPhone("+1 844 726 8446");
        companyData.setCompanyLogoUrl("https://workforcetrack.s3.amazonaws.com/000000000000/public/65159/7689b4d4-7d84-46e3-bf48-45935d55f8c9?AWSAccessKeyId=AKIAIROQMC77E5UKWBWQ".replaceAll("[&]", "&amp;"));
        return companyData;
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.KPI_PAYMENT_VIEW;
    }

    @Override
    protected String getTableName(Object dataClass) {
        Date currentDate = new Date();
        EdsUser user = usagePlanManager.getUser();
        EdsCompany company = user.getCompany();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        String title;
        if (company != null) {
            title = "Order #: " + dateFormat.format(currentDate) + " - " + company.getObjectID() + " - " + company.getName() + " - Order Summary";
        } else {
            title = "Order #: " + dateFormat.format(currentDate) + " - Order Summary";
        }
        return title;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        if (user == null) {
            return;
        }
        String companyName = user.getCompany() != null && user.getCompany().getName() != null ? user.getCompany().getName() : "";
        setFileName(companyName + "_" + dateFormat(user.getUserDate()));
    }
}
