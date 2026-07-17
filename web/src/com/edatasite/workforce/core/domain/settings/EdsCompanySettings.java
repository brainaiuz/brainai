package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsCompanySettingsCustomFields;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import io.jsonwebtoken.lang.Collections;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 08.05.2010
 * Time: 18:12:47
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "companySettings")
public class EdsCompanySettings extends EdsTraceable implements Constants {

    /*Short Date Format - Begin*/
    public static final String SHORT_DATE_FORMAT_1 = "MM/dd/yyyy";// e.g. 01/31/2010;
    public static final String SHORT_DATE_FORMAT_2 = "dd/MM/yyyy";// e.g. 31/01/2010;
    public static final String SHORT_DATE_FORMAT_3 = "yyyy/MM/dd";// e.g. 2010/01/31;
    public static final String SHORT_DATE_FORMAT_4 = "yyyy/dd/MM";// e.g. 2010/31/01;

    public static final String SHORT_DATE_FORMAT_5 = "MM-dd-yyyy";// e.g. 01-31-2010;
    public static final String SHORT_DATE_FORMAT_6 = "dd-MM-yyyy";// e.g. 31-01-2010;
    public static final String SHORT_DATE_FORMAT_7 = "yyyy-MM-dd";// e.g. 2010-01-31;
    public static final String SHORT_DATE_FORMAT_8 = "yyyy-dd-MM";// e.g. 2010-31-01;

    public static final String SHORT_DATE_FORMAT_9 = "MM.dd.yyyy";// e.g. 01.31.2010;
    public static final String SHORT_DATE_FORMAT_10 = "dd.MM.yyyy";// e.g. 31.01.2010;
    public static final String SHORT_DATE_FORMAT_11 = "yyyy.MM.dd";// e.g 2010.01.31;
    public static final String SHORT_DATE_FORMAT_12 = "yyyy.dd.MM";// e.g. 2010.31.01;

    public static final String SHORT_DATE_FORMAT_13 = "MMM dd, yyyy";// e.g. Jan 31, 2010;
    public static final String SHORT_DATE_FORMAT_14 = "dd MMM, yyyy";// e.g 31 Jan, 2010;

    public static final String SHORT_DATE_FORMAT_15 = "EEE, MMM dd, yyyy";// e.g Tue, Feb 22, 2016;
    public static final String SHORT_DATE_FORMAT_16 = "EEE, dd MMM, yyyy";// e.g Tue, 22 Feb, 2016;
    public static final String SHORT_DATE_FORMAT_17 = "YYYY-MM";// e.g 2021-03;
    public static final String SHORT_DATE_FORMAT_18 = "YYYY-MM (MMM)";// e.g 2021-03;
    /*Short Date Format - End*/

    /*Long Date Format - Begin*/
    public static final String LONG_DATE_FORMAT_1 = "MM/dd/yyyy HH:mm";// e.g. 01/31/2010 08:30;
    public static final String LONG_DATE_FORMAT_2 = "dd/MM/yyyy HH:mm";// e.g. 31/01/2010 08:30;
    public static final String LONG_DATE_FORMAT_3 = "yyyy/MM/dd HH:mm";// e.g. 2010/01/31 08:30;
    public static final String LONG_DATE_FORMAT_4 = "yyyy/dd/MM HH:mm";// e.g. 2010/31/01 08:30;

    public static final String LONG_DATE_FORMAT_5 = "MM-dd-yyyy HH:mm";// e.g. 01-31-2010 08:30;
    public static final String LONG_DATE_FORMAT_6 = "dd-MM-yyyy HH:mm";// e.g. 31-01-2010 08:30;
    public static final String LONG_DATE_FORMAT_7 = "yyyy-MM-dd HH:mm";// e.g. 2010-01-31 08:30;
    public static final String LONG_DATE_FORMAT_8 = "yyyy-dd-MM HH:mm";// e.g. 2010-31-01 08:30;

    public static final String LONG_DATE_FORMAT_9 = "MM.dd.yyyy HH:mm";// e.g. 01.31.2010 08:30;
    public static final String LONG_DATE_FORMAT_10 = "dd.MM.yyyy HH:mm";// e.g. 31.01.2010 08:30;
    public static final String LONG_DATE_FORMAT_11 = "yyyy.MM.dd HH:mm";// e.g 2010.01.31 08:30;
    public static final String LONG_DATE_FORMAT_12 = "yyyy.dd.MM HH:mm";// e.g. 2010.31.01 08:30;

    public static final String LONG_DATE_FORMAT_13 = "MMM dd, yyyy [HH:mm]";// e.g. Jan 31, 2010 [08:30];
    public static final String LONG_DATE_FORMAT_14 = "dd MMM, yyyy [HH:mm]";// e.g 31 Jan, 2010 [08:30];

    public static final String LONG_DATE_FORMAT_15 = "MM/dd/yyyy hh:mm a";// e.g. 01/31/2010 08:30;
    public static final String LONG_DATE_FORMAT_16 = "dd/MM/yyyy hh:mm a";// e.g. 31/01/2010 08:30;
    public static final String LONG_DATE_FORMAT_17 = "yyyy/MM/dd hh:mm a";// e.g. 2010/01/31 08:30;
    public static final String LONG_DATE_FORMAT_18 = "yyyy/dd/MM hh:mm a";// e.g. 2010/31/01 08:30;

    public static final String LONG_DATE_FORMAT_19 = "MM-dd-yyyy hh:mm a";// e.g. 01-31-2010 08:30;
    public static final String LONG_DATE_FORMAT_20 = "dd-MM-yyyy hh:mm a";// e.g. 31-01-2010 08:30;
    public static final String LONG_DATE_FORMAT_21 = "yyyy-MM-dd hh:mm a";// e.g. 2010-01-31 08:30;
    public static final String LONG_DATE_FORMAT_22 = "yyyy-dd-MM hh:mm a";// e.g. 2010-31-01 08:30;

    public static final String LONG_DATE_FORMAT_23 = "MM.dd.yyyy hh:mm a";// e.g. 01.31.2010 08:30;
    public static final String LONG_DATE_FORMAT_24 = "dd.MM.yyyy hh:mm a";// e.g. 31.01.2010 08:30;
    public static final String LONG_DATE_FORMAT_25 = "yyyy.MM.dd hh:mm a";// e.g 2010.01.31 08:30;
    public static final String LONG_DATE_FORMAT_26 = "yyyy.dd.MM hh:mm a";// e.g. 2010.31.01 08:30;

    public static final String LONG_DATE_FORMAT_27 = "MMM dd, yyyy [hh:mm a]";// e.g. Jan 31, 2010 [08:30];
    public static final String LONG_DATE_FORMAT_28 = "dd MMM, yyyy [hh:mm a]";// e.g 31 Jan, 2010 [08:30];

    public static final String LONG_DATE_FORMAT_29 = "EEE, MMM dd, yyyy [HH:mm]";// e.g Tue, Feb 22, 2016 [08:30];
    public static final String LONG_DATE_FORMAT_30 = "EEE, dd MMM, yyyy [HH:mm]";// e.g Tue, 22 Feb, 2016 [08:30];
    public static final String LONG_DATE_FORMAT_31 = "hh:mm a";// e.g 08:30;
    /*Long Date Format - End*/

    /**
     * For the first time we cannot allow all companies to use message center,
     * because, they can low down our servers. Maybe after some period we will
     * let all of them use from it, but now we are allowing it only for those
     * who requested message center functionality.
     * <p/>
     * For further info please refer Ruslan Muhammadov
     */
    @Column(name = "enablemessagingcenter")

    private Boolean enableMessageCenter = false;

    @Column(name = "isShowPrivateContact")
    //only is shown private contacts to ADMIN
    private Boolean isShowPrivateContact = false;

    @Column(name = "longDateFormat")
    private String longDateFormat = "MMM dd, yyyy [HH:mm]";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "shortDateFormat")
    private String shortDateFormat = "MMM dd, yyyy";

    @Column(name = "pdf_logo_width")
    private Integer pdfLogoWidth;

    @Column(name = "pdf_logo_height")
    private Integer pdfLogoHeight;

    @Column(name = "pdf_style_color")
    private String pdfStyleColor = DEFAULT_FONT_COLOR;

    @Column(name = "pdffont_id")
    private Integer pdfFontID;

    @Column(name = "pdflimit")
    private String pdfLimit;

    @Column(name = "excellimit")
    private String excelLimit;

    @Column(name = "indexedDocumentUpload", columnDefinition = "boolean default true")
    private Boolean indexedDocumentUpload;
    private String switchvoxUserName;
    private String switchvoxPassword;
    private String switchvoxServerId;

    //Enable upload types by default (Google, Office365)
    @Column(name = "enableUploadTypes")
    private String enableUploadTypes = "true;true;true";

    @Column(name = "sharePointSiteUrls")
    private String sharePointSiteUrls;

    @Column(name = "sharePointClientId")
    private String sharePointClientId;

    @Column(name = "sharePointClientSecret")
    private String sharePointClientSecret;

    @Column(name = "opportunity_stage")
    private Integer opportunityStageId;

    @Column(name = "opportunity_stage_source")
    private Integer opportunitySourceId;

    @Column(name = "converts_to")
    private String convertsTo;

    @Column(name = "reportingEmptyValueString", columnDefinition = "varchar(255) default 'n/a'")
    private String reportingEmptyValueString;

    public Integer getPdfFontID() {
        return pdfFontID;
    }

    public void setPdfFontID(Integer pdfFontID) {
        this.pdfFontID = pdfFontID;
    }

    public String getPdfLimit() {
        return pdfLimit;
    }

    public void setPdfLimit(String pdfLimit) {
        this.pdfLimit = pdfLimit;
    }

    public String getExcelLimit() {
        return excelLimit;
    }

    public void setExcelLimit(String excelLimit) {
        this.excelLimit = excelLimit;
    }


    /* The system may contain several themes and each company can choose
     * a proper one for itself.And that chosen theme has to be stored in
     * the system and each time when the company users log in, they have
     * to  view  that  selected  theme. Theme can be selected by company
     * administrator or director.
     * <p/>
     * For further info please refer to Ruslan Muhammadov*/
    private String themeForSystem;//Color schema for each company.
    /**
     * The property setup company subprojects to project
     */
    @Column(name = "isSetupSubProject")
    private Boolean isSetupSubProject = false;

    @Column(name = "isSetupSubProjectTwoLevel")
    private Boolean isSetupSubProjectTwoLevel = false;

    //this fields for the Opportunity Settings
    @Column(name = "is_fill_oi_with_inventory")
    private Boolean isFillOpportunityItemWithInventory = false;

    @Column()
    private Boolean opportunityRequireContractUpload = false;

    @Column(name = "isJoinOpportunityToExpenseClaim")
    private Boolean isJoinOpportunityToExpenseClaim = false;

    @Column(name = "emailAutoLinking")
    private Boolean emailAutoLinking = false;

    @Column(name = "generateCrmAccountNumber", columnDefinition = "boolean default true")
    private Boolean generateCrmAccountNumber = Boolean.TRUE;

    @Column(name = "importPreference")
    private String importPreference;

    @Column(name = "overwritePreference")
    private String overwritePreference;

    @Column(name = "showaccountingsettings")
    private Boolean showAccountingSettings;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companySettingsCustomFieldsID", unique = true)
    @ForeignKey(name = "none")
    private EdsCompanySettingsCustomFields companySettingsCustomFields;

    private Boolean enableXmlBuckup = false;

    @Column(name = "xmlBackupClientId")
    private String xmlBackupClientId;

    @Column(name = "xmlBackupClientSecret")
    private String xmlBackupClientSecret;

    @Column(name = "xmlBackupBuckedName")
    private String xmlBackupBuckedName;

    @Column(name = "customAuthId")
    private Integer customAuthId;

    @Column(name = "uploadType")
    private String uploadType;

    @Column(name = "miniOIp")
    private String miniOIp;

    @Column(name = "sipuni_contact_type")
    private Integer sipuniContactType;


    public Integer getObjectID() {
        return objectID;
    }

    public Boolean isEnableMessageCenter() {
        return enableMessageCenter;
    }

    public void setEnableMessageCenter(Boolean enableMessageCenter) {
        this.enableMessageCenter = enableMessageCenter;
    }

    public Boolean isShowPrivateContact() {
        return isShowPrivateContact;
    }

    public void setShowPrivateContact(Boolean showPrivateContact) {
        isShowPrivateContact = showPrivateContact;
    }

    public String getLongDateFormat() {
        return longDateFormat;
    }

    public void setLongDateFormat(String longDateFormat) {
        this.longDateFormat = longDateFormat;
    }

    public String getShortDateFormat() {
        return shortDateFormat;
    }

    public void setShortDateFormat(String shortDateFormat) {
        this.shortDateFormat = shortDateFormat;
    }

    public Integer getPdfLogoWidth() {
        return pdfLogoWidth;
    }

    public void setPdfLogoWidth(Integer pdfLogoWidth) {
        this.pdfLogoWidth = pdfLogoWidth;
    }

    public Integer getPdfLogoHeight() {
        return pdfLogoHeight;
    }

    public void setPdfLogoHeight(Integer pdfLogoHeight) {
        this.pdfLogoHeight = pdfLogoHeight;
    }

    public String getThemeForSystem() {
        return themeForSystem;
    }

    public void setThemeForSystem(String themeForSystem) {
        this.themeForSystem = themeForSystem;
    }

    public Boolean isSetupSubProject() {
        return isSetupSubProject != null ? isSetupSubProject : false;
    }

    public void setSetupSubProject(Boolean setupSubProject) {
        isSetupSubProject = setupSubProject;
    }

    public Boolean isSetupSubProjectTwoLevel() {
        return isSetupSubProjectTwoLevel != null ? isSetupSubProjectTwoLevel : false;
    }

    public void setSetupSubProjectTwoLevel(Boolean setupSubProjectTwoLevel) {
        isSetupSubProjectTwoLevel = setupSubProjectTwoLevel;
    }

    public Boolean isFillOpportunityItemWithInventory() {
        return isFillOpportunityItemWithInventory != null ? isFillOpportunityItemWithInventory : false;
    }

    public void setFillOpportunityItemWithInventory(Boolean fillOpportunityItemWithInventory) {
        isFillOpportunityItemWithInventory = fillOpportunityItemWithInventory;
    }

    public Boolean getOpportunityRequireContractUpload() {
        return opportunityRequireContractUpload != null ? opportunityRequireContractUpload : false;
    }

    public void setOpportunityRequireContractUpload(Boolean require) {
        opportunityRequireContractUpload = require;
    }

    public Boolean getJoinOpportunityToExpenseClaim() {
        return isJoinOpportunityToExpenseClaim != null ? isJoinOpportunityToExpenseClaim : false;
    }

    public void setJoinOpportunityToExpenseClaim(Boolean joinOpportunityToExpenseClaim) {
        isJoinOpportunityToExpenseClaim = joinOpportunityToExpenseClaim;
    }

    public Boolean getEmailAutoLinking() {
        return emailAutoLinking != null ? emailAutoLinking : false;
    }

    public void setEmailAutoLinking(Boolean emailAutoLinking) {
        this.emailAutoLinking = emailAutoLinking;
    }

    public Boolean getGenerateCrmAccountNumbering() {
        return this.generateCrmAccountNumber;
    }

    public void setGenerateCrmAccountNumbering(final Boolean generateCrmAccountNumber) {
        this.generateCrmAccountNumber = generateCrmAccountNumber;
    }

    public SelectItem[] getAllDateFormats() {
        List<SelectItem> items = new ArrayList<>();
        items.addAll(Collections.arrayToList(getShortDateFormats()));
        items.addAll(Collections.arrayToList(getLongDateFormats()));
        return items.toArray(new SelectItem[]{});
    }

    public SelectItem[] getShortDateFormats() {
        SelectItem[] items = new SelectItem[18];
        items[0] = new SelectItem(1, SHORT_DATE_FORMAT_1 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_1).format(new Date()) + ")", SHORT_DATE_FORMAT_1);
        items[1] = new SelectItem(2, SHORT_DATE_FORMAT_2 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_2).format(new Date()) + ")", SHORT_DATE_FORMAT_2);
        items[2] = new SelectItem(3, SHORT_DATE_FORMAT_3 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_3).format(new Date()) + ")", SHORT_DATE_FORMAT_3);
        items[3] = new SelectItem(4, SHORT_DATE_FORMAT_4 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_4).format(new Date()) + ")", SHORT_DATE_FORMAT_4);
        items[4] = new SelectItem(5, SHORT_DATE_FORMAT_5 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_5).format(new Date()) + ")", SHORT_DATE_FORMAT_5);
        items[5] = new SelectItem(6, SHORT_DATE_FORMAT_6 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_6).format(new Date()) + ")", SHORT_DATE_FORMAT_6);
        items[6] = new SelectItem(7, SHORT_DATE_FORMAT_7 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_7).format(new Date()) + ")", SHORT_DATE_FORMAT_7);
        items[7] = new SelectItem(8, SHORT_DATE_FORMAT_8 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_8).format(new Date()) + ")", SHORT_DATE_FORMAT_8);
        items[8] = new SelectItem(9, SHORT_DATE_FORMAT_9 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_9).format(new Date()) + ")", SHORT_DATE_FORMAT_9);
        items[9] = new SelectItem(10, SHORT_DATE_FORMAT_10 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_10).format(new Date()) + ")", SHORT_DATE_FORMAT_10);
        items[10] = new SelectItem(11, SHORT_DATE_FORMAT_11 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_11).format(new Date()) + ")", SHORT_DATE_FORMAT_11);
        items[11] = new SelectItem(12, SHORT_DATE_FORMAT_12 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_12).format(new Date()) + ")", SHORT_DATE_FORMAT_12);
        items[12] = new SelectItem(13, SHORT_DATE_FORMAT_13 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_13).format(new Date()) + ")", SHORT_DATE_FORMAT_13);
        items[13] = new SelectItem(14, SHORT_DATE_FORMAT_14 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_14).format(new Date()) + ")", SHORT_DATE_FORMAT_14);
        items[14] = new SelectItem(15, SHORT_DATE_FORMAT_15 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_15).format(new Date()) + ")", SHORT_DATE_FORMAT_15);
        items[15] = new SelectItem(16, SHORT_DATE_FORMAT_16 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_16).format(new Date()) + ")", SHORT_DATE_FORMAT_16);
        items[16] = new SelectItem(17, SHORT_DATE_FORMAT_17 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_17).format(new Date()) + ")", SHORT_DATE_FORMAT_17);
        items[17] = new SelectItem(18, SHORT_DATE_FORMAT_18 + " (eg. " + new SimpleDateFormat(SHORT_DATE_FORMAT_18).format(new Date()) + ")", SHORT_DATE_FORMAT_18);
        return items;
    }

    public SelectItem[] getLongDateFormats() {
        SelectItem[] items = new SelectItem[31];
        items[0] = new SelectItem(1, LONG_DATE_FORMAT_1 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_1).format(new Date()) + ")", LONG_DATE_FORMAT_1);
        items[1] = new SelectItem(2, LONG_DATE_FORMAT_2 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_2).format(new Date()) + ")", LONG_DATE_FORMAT_2);
        items[2] = new SelectItem(3, LONG_DATE_FORMAT_3 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_3).format(new Date()) + ")", LONG_DATE_FORMAT_3);
        items[3] = new SelectItem(4, LONG_DATE_FORMAT_4 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_4).format(new Date()) + ")", LONG_DATE_FORMAT_4);
        items[4] = new SelectItem(5, LONG_DATE_FORMAT_5 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_5).format(new Date()) + ")", LONG_DATE_FORMAT_5);
        items[5] = new SelectItem(6, LONG_DATE_FORMAT_6 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_6).format(new Date()) + ")", LONG_DATE_FORMAT_6);
        items[6] = new SelectItem(7, LONG_DATE_FORMAT_7 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_7).format(new Date()) + ")", LONG_DATE_FORMAT_7);
        items[7] = new SelectItem(8, LONG_DATE_FORMAT_8 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_8).format(new Date()) + ")", LONG_DATE_FORMAT_8);
        items[8] = new SelectItem(9, LONG_DATE_FORMAT_9 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_9).format(new Date()) + ")", LONG_DATE_FORMAT_9);
        items[9] = new SelectItem(10, LONG_DATE_FORMAT_10 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_10).format(new Date()) + ")", LONG_DATE_FORMAT_10);
        items[10] = new SelectItem(11, LONG_DATE_FORMAT_11 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_11).format(new Date()) + ")", LONG_DATE_FORMAT_11);
        items[11] = new SelectItem(12, LONG_DATE_FORMAT_12 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_12).format(new Date()) + ")", LONG_DATE_FORMAT_12);
        items[12] = new SelectItem(13, LONG_DATE_FORMAT_13 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_13).format(new Date()) + ")", LONG_DATE_FORMAT_13);
        items[13] = new SelectItem(14, LONG_DATE_FORMAT_14 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_14).format(new Date()) + ")", LONG_DATE_FORMAT_14);
        items[14] = new SelectItem(15, LONG_DATE_FORMAT_15 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_15).format(new Date()) + ")", LONG_DATE_FORMAT_15);
        items[15] = new SelectItem(16, LONG_DATE_FORMAT_16 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_16).format(new Date()) + ")", LONG_DATE_FORMAT_16);
        items[16] = new SelectItem(17, LONG_DATE_FORMAT_17 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_17).format(new Date()) + ")", LONG_DATE_FORMAT_17);
        items[17] = new SelectItem(18, LONG_DATE_FORMAT_18 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_18).format(new Date()) + ")", LONG_DATE_FORMAT_18);
        items[18] = new SelectItem(19, LONG_DATE_FORMAT_19 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_19).format(new Date()) + ")", LONG_DATE_FORMAT_19);
        items[19] = new SelectItem(20, LONG_DATE_FORMAT_20 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_20).format(new Date()) + ")", LONG_DATE_FORMAT_20);
        items[20] = new SelectItem(21, LONG_DATE_FORMAT_21 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_21).format(new Date()) + ")", LONG_DATE_FORMAT_21);
        items[21] = new SelectItem(22, LONG_DATE_FORMAT_22 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_22).format(new Date()) + ")", LONG_DATE_FORMAT_22);
        items[22] = new SelectItem(23, LONG_DATE_FORMAT_23 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_23).format(new Date()) + ")", LONG_DATE_FORMAT_23);
        items[23] = new SelectItem(24, LONG_DATE_FORMAT_24 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_24).format(new Date()) + ")", LONG_DATE_FORMAT_24);
        items[24] = new SelectItem(25, LONG_DATE_FORMAT_25 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_25).format(new Date()) + ")", LONG_DATE_FORMAT_25);
        items[25] = new SelectItem(26, LONG_DATE_FORMAT_26 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_26).format(new Date()) + ")", LONG_DATE_FORMAT_26);
        items[26] = new SelectItem(27, LONG_DATE_FORMAT_27 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_27).format(new Date()) + ")", LONG_DATE_FORMAT_27);
        items[27] = new SelectItem(28, LONG_DATE_FORMAT_28 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_28).format(new Date()) + ")", LONG_DATE_FORMAT_28);
        items[28] = new SelectItem(29, LONG_DATE_FORMAT_29 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_29).format(new Date()) + ")", LONG_DATE_FORMAT_29);
        items[29] = new SelectItem(30, LONG_DATE_FORMAT_30 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_30).format(new Date()) + ")", LONG_DATE_FORMAT_30);
        items[30] = new SelectItem(31, LONG_DATE_FORMAT_31 + " (eg. " + new SimpleDateFormat(LONG_DATE_FORMAT_31).format(new Date()) + ")", LONG_DATE_FORMAT_31);
        return items;
    }

    public String getPdfStyleColor() {
        return pdfStyleColor;
    }

    public void setPdfStyleColor(String pdfStyleColor) {
        this.pdfStyleColor = pdfStyleColor;
    }

    public String getSwitchvoxUserName() {
        return switchvoxUserName;
    }

    public void setSwitchvoxUserName(String switchvoxUserName) {
        this.switchvoxUserName = switchvoxUserName;
    }

    public String getSwitchvoxPassword() {
        return switchvoxPassword;
    }

    public void setSwitchvoxPassword(String switchvoxPassword) {
        this.switchvoxPassword = switchvoxPassword;
    }

    public String getSwitchvoxServerId() {
        return switchvoxServerId;
    }

    public void setSwitchvoxServerId(String switchvoxServerId) {
        this.switchvoxServerId = switchvoxServerId;
    }

    public Boolean getIndexedDocumentUpload() {
        return indexedDocumentUpload;
    }

    public Integer getOpportunityStageId() {
        return opportunityStageId;
    }

    public void setOpportunityStageId(Integer opportunityStageId) {
        this.opportunityStageId = opportunityStageId;
    }

    public Integer getOpportunitySourceId() {
        return opportunitySourceId;
    }

    public void setOpportunitySourceId(Integer opportunitySourceId) {
        this.opportunitySourceId = opportunitySourceId;
    }

    public void setIndexedDocumentUpload(Boolean indexedDocumentUpload) {
        this.indexedDocumentUpload = indexedDocumentUpload;
    }

    public Boolean isShowAccountingSettings() {
        return showAccountingSettings = showAccountingSettings != null ? showAccountingSettings : false;
    }

    public void setShowAccountingSettings(Boolean showAccountingSettings) {
        this.showAccountingSettings = showAccountingSettings;
    }

    public EdsCompanySettingsCustomFields getCompanySettingsCustomFields() {
        return companySettingsCustomFields;
    }

    public void setCompanySettingsCustomFields(EdsCompanySettingsCustomFields companySettingsCustomFields) {
        this.companySettingsCustomFields = companySettingsCustomFields;
    }

    public String getLongDateFormat4Postgres() {
        return postgresDateFormatWrapper(getLongDateFormat());
    }

    public String getShortDateFormat4Postgres() {
        return postgresDateFormatWrapper(getShortDateFormat());
    }

    private String postgresDateFormatWrapper(String javaDateFormat) {
        return javaDateFormat == null ? null : javaDateFormat.replace("MMM", "Mon").
                replace("mmm", "mon").
                replace("mm", "mi").
                replace("a", "am").
                replace("A", "AM").
                replace("HH", "HH24").
                replace("hh", "HH12").
                replace("EEE", "Dy");
    }

    public String getEnableUploadTypes() {
        return enableUploadTypes;
    }

    public void setEnableUploadTypes(String enableuploadTypes) {
        this.enableUploadTypes = enableuploadTypes;
    }

    public String getSharePointSiteUrls() {
        return sharePointSiteUrls;
    }

    public void setSharePointSiteUrls(String sharePointSiteUrls) {
        this.sharePointSiteUrls = sharePointSiteUrls;
    }

    public String getSharePointClientId() {
        return sharePointClientId;
    }

    public void setSharePointClientId(String sharePointClientId) {
        this.sharePointClientId = sharePointClientId;
    }

    public String getSharePointClientSecret() {
        return sharePointClientSecret;
    }

    public void setSharePointClientSecret(String sharePointClientSecret) {
        this.sharePointClientSecret = sharePointClientSecret;
    }

    public Boolean getEnableXmlBuckup() {
        return enableXmlBuckup;
    }

    public void setEnableXmlBuckup(Boolean enableXmlBuckup) {
        this.enableXmlBuckup = enableXmlBuckup;
    }

    public String getXmlBackupClientId() {
        return xmlBackupClientId;
    }

    public void setXmlBackupClientId(String xmlBackupClientId) {
        this.xmlBackupClientId = xmlBackupClientId;
    }

    public String getXmlBackupClientSecret() {
        return xmlBackupClientSecret;
    }

    public void setXmlBackupClientSecret(String xmlBackupClientSecret) {
        this.xmlBackupClientSecret = xmlBackupClientSecret;
    }

    public String getXmlBackupBuckedName() {
        return xmlBackupBuckedName;
    }

    public void setXmlBackupBuckedName(String xmlBackupBuckedName) {
        this.xmlBackupBuckedName = xmlBackupBuckedName;
    }

    @ForeignKey(name = "none")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "userChanges")
    public Set<EdsChanges> userChanges = new HashSet<>();

    public Set<EdsChanges> getUserChanges() {
        return userChanges;
    }

    public void setUserChanges(Set<EdsChanges> userChanges) {
        this.userChanges = userChanges;
    }

    public void addHistoryChange(String field, Object oldValue, Object newValue) {
        if (getObjectID() != null) {
            EdsChanges change = new EdsChanges();
            change.setField(field);
            change.setEntityID(getObjectID());
            change.setEntityName(getName());
            if (oldValue instanceof String || oldValue instanceof Double) {
                oldValue = oldValue == null ? "" : oldValue;
                change.setFromStringValue(String.valueOf(oldValue));
            } else if (oldValue instanceof Number) {
                change.setFromNumberValue((BigDecimal) oldValue);
            } else if (oldValue instanceof Date) {
                change.setFromDateValue((Date) oldValue);
            } else if (oldValue instanceof Boolean) {
                change.setFromStringValue((Boolean) oldValue ? "Yes" : "No");
            }
            if (newValue instanceof String || newValue instanceof Double) {
                newValue = newValue == null ? "" : newValue;
                change.setToStringValue(String.valueOf(newValue));
            } else if (newValue instanceof Number) {
                change.setToNumberValue((BigDecimal) newValue);
            } else if (newValue instanceof Date) {
                change.setToDateValue((Date) newValue);
            } else if (newValue instanceof Boolean) {
                change.setToStringValue((Boolean) newValue ? "Yes" : "No");
            }
            change.setModificationDate(new Date());
            change.setUpdater((EdsUser) SecurityContext.getInstance().getUser());
            change.setSuperUser(ServerUtils.isSuperUser());
            getUserChanges().add(change);
        }
    }

    public String getImportPreference() {
        return importPreference;
    }

    public void setImportPreference(String importPreference) {
        this.importPreference = importPreference;
    }

    public String getOverwritePreference() {
        return overwritePreference;
    }

    public void setOverwritePreference(String overwritePreference) {
        this.overwritePreference = overwritePreference;
    }

    public Integer getCustomAuthId() {
        return customAuthId;
    }

    public void setCustomAuthId(Integer customAuthId) {
        this.customAuthId = customAuthId;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
    }

    public String getMiniOIp() {
        return miniOIp;
    }

    public void setMiniOIp(String miniOIp) {
        this.miniOIp = miniOIp;
    }

    public Integer getSipuniContactType() {
        return sipuniContactType;
    }


    public String getConvertsTo() {
        return convertsTo;
    }

    public void setConvertsTo(String convertsTo) {
        this.convertsTo = convertsTo;
    }

    public void setSipuniContactType(Integer sipuniContactType) {
        this.sipuniContactType = sipuniContactType;
    }


    public String getReportingEmptyValueString() {
        return reportingEmptyValueString == null ? "n/a" : reportingEmptyValueString;
    }

    public void setReportingEmptyValueString(String reportingEmptyValueString) {
        this.reportingEmptyValueString = reportingEmptyValueString;
    }

    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        }
        if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            EdsCompanySettingsCustomFields customFields = getCompanySettingsCustomFields();
            return (customFields != null) ? CustomFieldsUtils.getObjectValue(customFields, fieldID) : "";
        }
        return super.getRealValue(fieldID);
    }

}
