package com.edatasite.workforce.core.domain.pdf;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateTableSettingsItem;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateTableTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.PdfTemplateTypeEnum;

import javax.persistence.*;

/**
 * User: Abror Abdukadirov
 * Date: 14.01.2019 18:50
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "pdftemplate_table_settings")
public class EdsPdfTemplateTableSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Enumerated(EnumType.STRING)
    private PdfTemplateTypeEnum pdfType;

    @Enumerated(EnumType.STRING)
    private PdfTemplateTableTypeEnum pdfTableType;

    private String columnCode;
    private String columnTitle;
    private Integer sorder;
    private Integer width;
    private Integer alignment;
    private Boolean isCustomField;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyPdfTemplateid")
    private EdsCompanyPdfTemplate companyPdfTemplate;

    @Override
    public Integer getObjectID() {
        return this.objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public PdfTemplateTypeEnum getPdfType() {
        return pdfType;
    }

    public void setPdfType(PdfTemplateTypeEnum pdfType) {
        this.pdfType = pdfType;
    }

    public PdfTemplateTableTypeEnum getPdfTableType() {
        return pdfTableType;
    }

    public void setPdfTableType(PdfTemplateTableTypeEnum pdfTableType) {
        this.pdfTableType = pdfTableType;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getAlignment() {
        return alignment;
    }

    public void setAlignment(Integer alignment) {
        this.alignment = alignment;
    }

    public Boolean getCustomField() {
        return isCustomField;
    }

    public void setCustomField(Boolean customField) {
        isCustomField = customField;
    }

    public EdsCompanyPdfTemplate getCompanyPdfTemplate() {
        return companyPdfTemplate;
    }

    public void setCompanyPdfTemplate(EdsCompanyPdfTemplate companyPdfTemplate) {
        this.companyPdfTemplate = companyPdfTemplate;
    }

    public PdfTemplateTableSettingsItem toTO() {
        PdfTemplateTableSettingsItem to = new PdfTemplateTableSettingsItem();
        to.setColumnCode(this.getColumnCode());
        to.setColumnTitle(this.getColumnTitle());
        to.setSorder(this.getSorder());
        if (this.getWidth() != null) {
            to.setWidth(this.getWidth());
        }
        if (this.getAlignment() != null) {
            to.setAlignment(this.getAlignment());
        }
        return to;
    }
}
