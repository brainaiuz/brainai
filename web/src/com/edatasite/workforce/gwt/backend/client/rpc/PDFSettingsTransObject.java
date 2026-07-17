package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.PdfGenerateTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/*
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 29.10.2010
 * Time: 22:40:23
 * To change this template use File | Settings | File Templates.
 */
public class PDFSettingsTransObject implements IsSerializable {
    private Integer objectID;
    private Integer companyID;
    private boolean defaultTemplate;
    private boolean browserVersion;
    private Integer pdfReferenceID;
    private String templateName;
    private String imageName;
    private String content;
    private String header;
    private String footer;
    private String fontFileName;
    private String numFormat;
    private String numFormatDecSeparator;
    private String numFormatGroupSeparator;
    private String exNumFormat;
    private String exNumFormatDecSeparator;
    private String exNumFormatGroupSeparator;
    private PdfGenerateTypeEnum generateType;
    private String pageFormat;
    private String orientation;
    private String marginTop;
    private String marginRight;
    private String marginBottom;
    private String marginLeft;
    private String headerHeight;
    private String footerHeight;
    private BigDecimal price;
    private SelectItem[] references;
    private SelectItem[] fonts;
    private SelectItem[] customFormItems;
    private String customFormItemFormId;
    private String section;
    private FileItem[] attachedFiles;

    public PDFSettingsTransObject() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public boolean isDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(boolean defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public boolean isBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(boolean browserVersion) {
        this.browserVersion = browserVersion;
    }

    public Integer getPdfReferenceID() {
        return pdfReferenceID;
    }

    public void setPdfReferenceID(Integer pdfReferenceID) {
        this.pdfReferenceID = pdfReferenceID;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getFontFileName() {
        return fontFileName;
    }

    public void setFontFileName(String fontFileName) {
        this.fontFileName = fontFileName;
    }

    public String getNumFormat() {
        return numFormat;
    }

    public void setNumFormat(String numFormat) {
        this.numFormat = numFormat;
    }

    public String getNumFormatDecSeparator() {
        return numFormatDecSeparator;
    }

    public void setNumFormatDecSeparator(String numFormatDecSeparator) {
        this.numFormatDecSeparator = numFormatDecSeparator;
    }

    public String getNumFormatGroupSeparator() {
        return numFormatGroupSeparator;
    }

    public void setNumFormatGroupSeparator(String numFormatGroupSeparator) {
        this.numFormatGroupSeparator = numFormatGroupSeparator;
    }

    public String getExNumFormat() {
        return exNumFormat;
    }

    public void setExNumFormat(String exNumFormat) {
        this.exNumFormat = exNumFormat;
    }

    public String getExNumFormatDecSeparator() {
        return exNumFormatDecSeparator;
    }

    public void setExNumFormatDecSeparator(String exNumFormatDecSeparator) {
        this.exNumFormatDecSeparator = exNumFormatDecSeparator;
    }

    public String getExNumFormatGroupSeparator() {
        return exNumFormatGroupSeparator;
    }

    public void setExNumFormatGroupSeparator(String exNumFormatGroupSeparator) {
        this.exNumFormatGroupSeparator = exNumFormatGroupSeparator;
    }

    public PdfGenerateTypeEnum getGenerateType() {
        return generateType;
    }

    public void setGenerateType(PdfGenerateTypeEnum generateType) {
        this.generateType = generateType;
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
        this.marginTop = marginTop;
    }

    public String getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(String marginRight) {
        this.marginRight = marginRight;
    }

    public String getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(String marginBottom) {
        this.marginBottom = marginBottom;
    }

    public String getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(String marginLeft) {
        this.marginLeft = marginLeft;
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

    public SelectItem[] getReferences() {
        return references;
    }

    public void setReferences(SelectItem[] references) {
        this.references = references;
    }

    public SelectItem[] getFonts() {
        return fonts;
    }

    public void setFonts(SelectItem[] fonts) {
        this.fonts = fonts;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public SelectItem[] getCustomFormItems() {
        return customFormItems;
    }

    public void setCustomFormItems(SelectItem[] customFormItems) {
        this.customFormItems = customFormItems;
    }

    public String getCustomFormItemFormId() {
        return customFormItemFormId;
    }

    public void setCustomFormItemFormId(String customFormItemFormId) {
        this.customFormItemFormId = customFormItemFormId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public FileItem[] getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(FileItem[] attachedFiles) {
        this.attachedFiles = attachedFiles;
    }
}