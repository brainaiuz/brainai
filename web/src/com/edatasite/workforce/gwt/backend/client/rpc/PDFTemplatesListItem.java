package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.PdfGenerateTypeEnum;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 18, 2011
 * Time: 3:48:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class PDFTemplatesListItem implements IsSerializable{
    private Integer objectID;
    private Integer companyID;
    private boolean defaultTemplate;
    private String companyName;
    private String templateName;
    private String type;
    private String font;
    private String shortNumberFormat;
    private String extendedNumberFormat;
    private PdfGenerateTypeEnum generateType;

    public PDFTemplatesListItem() {
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public String getShortNumberFormat() {
        return shortNumberFormat;
    }

    public void setShortNumberFormat(String shortNumberFormat) {
        this.shortNumberFormat = shortNumberFormat;
    }

    public String getExtendedNumberFormat() {
        return extendedNumberFormat;
    }

    public void setExtendedNumberFormat(String extendedNumberFormat) {
        this.extendedNumberFormat = extendedNumberFormat;
    }

    public PdfGenerateTypeEnum getGenerateType() {
        return generateType;
    }

    public void setGenerateType(PdfGenerateTypeEnum generateType) {
        this.generateType = generateType;
    }
}
