package com.edatasite.workforce.core.domain.pdf;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.enums.PDFTemplateTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.PdfGenerateTypeEnum;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateListItem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 13.10.2010
 * Time: 17:19:56
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "companypdftemplate")
public class EdsCompanyPdfTemplate extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "templateid")
    private EdsPdfTemplate template;

    private String name;
    private String documentTitle;
    private String fontFamily;

    private String numberFormat;
    private String numberFormatDecimalSeparator;
    private String numberFormatGroupSeparator;

    private String extendedNumberFormat;
    private String extendedNumberFormatDecimalSeparator;
    private String extendedNumberFormatGroupSeparator;

    private Boolean defaultTemplate = false;

    @Column(name = "pdf_query", length = 500)
    private String queryName;

    @Column(name = "param_url")
    private String paramUrl;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private Boolean deleted = false;

    @Column(columnDefinition = "boolean default false")
    private boolean browserVersion = false;

    @Enumerated(EnumType.STRING)
    private PdfGenerateTypeEnum generateType;

    @Enumerated(EnumType.STRING)
    private PDFTemplateTypeEnum templateType;

    private Boolean isClientPdf;

    private String pageFormat;
    private String orientation;
    private String marginTop;
    private String marginRight;
    private String marginBottom;
    private String marginLeft;
    private String headerHeight;
    private String footerHeight;
    private String section;
    private String customFormItemFormId;

    private Date createdDate = new Date();
    private Date updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id")
    private EdsUser updator;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsPdfTemplate getTemplate() {
        return template;
    }

    public void setTemplate(EdsPdfTemplate template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String getNumberFormatDecimalSeparator() {
        return numberFormatDecimalSeparator;
    }

    public void setNumberFormatDecimalSeparator(String numberFormatDecimalSeparator) {
        this.numberFormatDecimalSeparator = numberFormatDecimalSeparator;
    }

    public String getNumberFormatGroupSeparator() {
        return numberFormatGroupSeparator;
    }

    public void setNumberFormatGroupSeparator(String numberFormatGroupSeparator) {
        this.numberFormatGroupSeparator = numberFormatGroupSeparator;
    }

    public String getExtendedNumberFormat() {
        return extendedNumberFormat;
    }

    public void setExtendedNumberFormat(String extendedNumberFormat) {
        this.extendedNumberFormat = extendedNumberFormat;
    }

    public String getExtendedNumberFormatDecimalSeparator() {
        return extendedNumberFormatDecimalSeparator;
    }

    public void setExtendedNumberFormatDecimalSeparator(String extendedNumberFormatDecimalSeparator) {
        this.extendedNumberFormatDecimalSeparator = extendedNumberFormatDecimalSeparator;
    }

    public String getExtendedNumberFormatGroupSeparator() {
        return extendedNumberFormatGroupSeparator;
    }

    public void setExtendedNumberFormatGroupSeparator(String extendedNumberFormatGroupSeparator) {
        this.extendedNumberFormatGroupSeparator = extendedNumberFormatGroupSeparator;
    }

    public Boolean isDefaultTemplate() {
        return defaultTemplate != null ? defaultTemplate : false;
    }

    public void setDefaultTemplate(Boolean defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setToPdfQuery(String toPdfQuery) {
        this.queryName = toPdfQuery;
    }

    public String getParamUrl() {
        return paramUrl;
    }

    public void setParamUrl(String paramUrl) {
        this.paramUrl = paramUrl;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(boolean browserVersion) {
        this.browserVersion = browserVersion;
    }

    public PdfGenerateTypeEnum getGenerateType() {
        return generateType;
    }

    public void setGenerateType(PdfGenerateTypeEnum generateType) {
        this.generateType = generateType;
    }

    public Boolean getClientPdf() {
        return isClientPdf != null ? isClientPdf : false;
    }

    public void setClientPdf(Boolean clientPdf) {
        isClientPdf = clientPdf;
    }

    public String getPageFormat() {
        return pageFormat;
    }

    public void setPageFormat(String pageFormat) {
        this.pageFormat = pageFormat;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(String marginTop) {
        this.marginTop = (marginTop != null && !marginTop.isEmpty()) ? marginTop + "px" : marginTop;
    }

    public void setNullMarginTop() {
        this.marginTop = null;
    }

    public String getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(String marginRight) {
        this.marginRight = (marginRight != null && !marginRight.isEmpty()) ? marginRight + "px" : marginRight;
    }

    public void setNullMarginRight() {
        this.marginRight = null;
    }

    public String getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(String marginBottom) {
        this.marginBottom = (marginBottom != null && !marginBottom.isEmpty()) ? marginBottom + "px" : marginBottom;
    }

    public void setNullMarginBottom() {
        this.marginBottom = null;
    }

    public String getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(String marginLeft) {
        this.marginLeft = (marginLeft != null && !marginLeft.isEmpty()) ? marginLeft + "px" : marginLeft;
    }

    public void setNullMarginLeft() {
        this.marginLeft = null;
    }

    public String getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(String headerHeight) {
        this.headerHeight = headerHeight;
    }

    public String getFooterHeight() {
        return footerHeight;
    }

    public void setFooterHeight(String footerHeight) {
        this.footerHeight = footerHeight;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public EdsUser getUpdator() {
        return updator;
    }

    public void setUpdator(EdsUser updator) {
        this.updator = updator;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getCustomFormItemFormId() {
        return customFormItemFormId;
    }

    public void setCustomFormItemFormId(String customFormItemFormId) {
        this.customFormItemFormId = customFormItemFormId;
    }

    public PDFTemplateTypeEnum getTemplateType() {
        return templateType;
    }

    public void setTemplateType(PDFTemplateTypeEnum templateType) {
        this.templateType = templateType;
    }

    public SettingsPdfTemplateListItem toTO() {
        SettingsPdfTemplateListItem item = new SettingsPdfTemplateListItem();
        item.setObjectId(this.getObjectID());
        item.setName(this.getName());
        item.setDefault(this.isDefaultTemplate());
        if (this.getTemplate() != null && this.getTemplate().getType() != null) {
            item.setCategory(this.getTemplate().getType().getName());
            item.setPdfType(this.getTemplate().getType().getCode());
        }
        item.setCreationDate(this.getCreatedDate());
        item.setModifiedDate(this.getUpdatedDate());
        if (this.getUpdator() != null) {
            item.setModifiedBy(this.getUpdator().getName());
        }
        return item;
    }
}
