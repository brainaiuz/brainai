package com.edatasite.workforce.core.domain.pdf;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfFooterHeaderContentItem;
import com.edatasite.workforce.gwt.profile.client.rpc.SettingsPdfTemplateItem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 12.12.2018 17:19
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "pdftemplate_settings")
public class EdsPdfTemplateSettings extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    //pdf outer params
    private String orientation;
    private String marginTop;
    private String marginRight;
    private String marginBottom;
    private String marginLeft;

    //pdf header
    private Boolean isCustomizedHeader = false;
    @Column(columnDefinition = "boolean default true")
    private Boolean companyLogoEnabled = true;
    @Column(columnDefinition = "boolean default true")
    private Boolean companyNameEnabled = true;
    private String companyNameFontSize;
    private String companyNameFontColor;
    @Column(columnDefinition = "boolean default true")
    private Boolean paginationEnabled = true;
    @Column(columnDefinition = "boolean default true")
    private Boolean documentTitleEnabled = true;
    private String documentTitleFontSize;
    private String documentTitleFontColor;

    private Boolean isCustomizedContent = false;
    @Column(columnDefinition = "boolean default false")
    private Boolean tableBorderEnabled = false;
    private String tableBorderColor;
    @Column(columnDefinition = "boolean default true")
    private Boolean itemRowEnabled = true;
    private String itemRowFontSize;
    @Column(columnDefinition = "boolean default false")
    private Boolean itemRowBackgroundColorEnabled = false;
    private String itemRowBackgroundColor;
    private String itemRowFontColor;
    private String tableHeaderFontSize;
    @Column(columnDefinition = "boolean default false")
    private Boolean tableHeaderBackgroundColorEnabled = false;
    private String tableHeaderBackgroundColor;
    private String tableHeaderFontColor;

    //pdf footer
    private Boolean isCustomizedFooter = false;
    @Column(columnDefinition = "boolean default true")
    private Boolean qrCodeEnabled = true;
    @Column(columnDefinition = "boolean default true")
    private Boolean poweredByEnabled = true;
    @Column(columnDefinition = "boolean default false")
    private Boolean customAddressEnabled = false;
    private String
            customAddress;
    private String customAddressFontSize;
    private String customAddressFontColor;
    private String footerBackgroundColor;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "template")
    private List<EdsPdfDynamicFooterHeader> dynamicFooterHeaders = new ArrayList<>();

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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
        this.marginTop = marginTop + "px";
    }

    public void setNullMarginTop() {
        this.marginTop = null;
    }

    public String getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(String marginRight) {
        this.marginRight = marginRight + "px";
    }

    public void setNullMarginRight() {
        this.marginRight = null;
    }

    public String getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(String marginBottom) {
        this.marginBottom = marginBottom + "px";
    }

    public void setNullMarginBottom() {
        this.marginBottom = null;
    }

    public String getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(String marginLeft) {
        this.marginLeft = marginLeft + "px";
    }

    public void setNullMarginLeft() {
        this.marginLeft = null;
    }

    public Boolean getCustomizedFooter() {
        return isCustomizedFooter != null ? isCustomizedFooter : false;
    }

    public void setCustomizedFooter(Boolean customizedFooter) {
        isCustomizedFooter = customizedFooter;
    }

    public Boolean getCompanyLogoEnabled() {
        return companyLogoEnabled != null ? companyLogoEnabled : true;
    }

    public void setCompanyLogoEnabled(Boolean companyLogoEnabled) {
        this.companyLogoEnabled = companyLogoEnabled;
    }

    public Boolean getCompanyNameEnabled() {
        return companyNameEnabled != null ? companyNameEnabled : true;
    }

    public void setCompanyNameEnabled(Boolean companyNameEnabled) {
        this.companyNameEnabled = companyNameEnabled;
    }

    public String getCompanyNameFontSize() {
        return companyNameFontSize;
    }

    public void setCompanyNameFontSize(String companyNameFontSize) {
        this.companyNameFontSize = companyNameFontSize;
    }

    public String getCompanyNameFontColor() {
        return companyNameFontColor;
    }

    public void setCompanyNameFontColor(String companyNameFontColor) {
        this.companyNameFontColor = companyNameFontColor;
    }

    public Boolean getPaginationEnabled() {
        return paginationEnabled != null ? paginationEnabled : true;
    }

    public void setPaginationEnabled(Boolean paginationEnabled) {
        this.paginationEnabled = paginationEnabled;
    }

    public Boolean getDocumentTitleEnabled() {
        return documentTitleEnabled != null ? documentTitleEnabled : true;
    }

    public void setDocumentTitleEnabled(Boolean documentTitleEnabled) {
        this.documentTitleEnabled = documentTitleEnabled;
    }

    public String getDocumentTitleFontSize() {
        return documentTitleFontSize;
    }

    public void setDocumentTitleFontSize(String documentTitleFontSize) {
        this.documentTitleFontSize = documentTitleFontSize;
    }

    public String getDocumentTitleFontColor() {
        return documentTitleFontColor;
    }

    public void setDocumentTitleFontColor(String documentTitleFontColor) {
        this.documentTitleFontColor = documentTitleFontColor;
    }

    public Boolean getCustomizedContent() {
        return isCustomizedContent != null ? isCustomizedContent : false;
    }

    public void setCustomizedContent(Boolean customizedContent) {
        isCustomizedContent = customizedContent;
    }

    public Boolean getTableBorderEnabled() {
        return tableBorderEnabled != null ? tableBorderEnabled : false;
    }

    public void setTableBorderEnabled(Boolean tableBorderEnabled) {
        this.tableBorderEnabled = tableBorderEnabled;
    }

    public String getTableBorderColor() {
        return tableBorderColor;
    }

    public void setTableBorderColor(String tableBorderColor) {
        this.tableBorderColor = tableBorderColor;
    }

    public Boolean getItemRowEnabled() {
        return itemRowEnabled != null ? itemRowEnabled : true;
    }

    public void setItemRowEnabled(Boolean itemRowEnabled) {
        this.itemRowEnabled = itemRowEnabled;
    }

    public String getItemRowFontSize() {
        return itemRowFontSize;
    }

    public void setItemRowFontSize(String itemRowFontSize) {
        this.itemRowFontSize = itemRowFontSize;
    }

    public Boolean getItemRowBackgroundColorEnabled() {
        return itemRowBackgroundColorEnabled != null ? itemRowBackgroundColorEnabled : false;
    }

    public void setItemRowBackgroundColorEnabled(Boolean itemRowBackgroundColorEnabled) {
        this.itemRowBackgroundColorEnabled = itemRowBackgroundColorEnabled;
    }

    public String getItemRowBackgroundColor() {
        return itemRowBackgroundColor;
    }

    public void setItemRowBackgroundColor(String itemRowBackgroundColor) {
        this.itemRowBackgroundColor = itemRowBackgroundColor;
    }

    public String getItemRowFontColor() {
        return itemRowFontColor;
    }

    public void setItemRowFontColor(String itemRowFontColor) {
        this.itemRowFontColor = itemRowFontColor;
    }

    public String getTableHeaderFontSize() {
        return tableHeaderFontSize;
    }

    public void setTableHeaderFontSize(String tableHeaderFontSize) {
        this.tableHeaderFontSize = tableHeaderFontSize;
    }

    public Boolean getTableHeaderBackgroundColorEnabled() {
        return tableHeaderBackgroundColorEnabled != null ? tableHeaderBackgroundColorEnabled : false;
    }

    public void setTableHeaderBackgroundColorEnabled(Boolean tableHeaderBackgroundColorEnabled) {
        this.tableHeaderBackgroundColorEnabled = tableHeaderBackgroundColorEnabled;
    }

    public String getTableHeaderBackgroundColor() {
        return tableHeaderBackgroundColor;
    }

    public void setTableHeaderBackgroundColor(String tableHeaderBackgroundColor) {
        this.tableHeaderBackgroundColor = tableHeaderBackgroundColor;
    }

    public String getTableHeaderFontColor() {
        return tableHeaderFontColor;
    }

    public void setTableHeaderFontColor(String tableHeaderFontColor) {
        this.tableHeaderFontColor = tableHeaderFontColor;
    }

    public Boolean getCustomizedHeader() {
        return isCustomizedHeader != null ? isCustomizedHeader : false;
    }

    public void setCustomizedHeader(Boolean customizedHeader) {
        isCustomizedHeader = customizedHeader;
    }

    public Boolean getQrCodeEnabled() {
        return qrCodeEnabled != null ? qrCodeEnabled : false;
    }

    public void setQrCodeEnabled(Boolean qrCodeEnabled) {
        this.qrCodeEnabled = qrCodeEnabled;
    }

    public Boolean getPoweredByEnabled() {
        return poweredByEnabled != null ? poweredByEnabled : true;
    }

    public void setPoweredByEnabled(Boolean poweredByEnabled) {
        this.poweredByEnabled = poweredByEnabled;
    }

    public Boolean getCustomAddressEnabled() {
        return customAddressEnabled != null ? customAddressEnabled : false;
    }

    public void setCustomAddressEnabled(Boolean customAddressEnabled) {
        this.customAddressEnabled = customAddressEnabled;
    }

    public String getCustomAddress() {
        return customAddress;
    }

    public void setCustomAddress(String customAddress) {
        this.customAddress = customAddress;
    }

    public String getCustomAddressFontSize() {
        return customAddressFontSize;
    }

    public void setCustomAddressFontSize(String customAddressFontSize) {
        this.customAddressFontSize = customAddressFontSize;
    }

    public String getCustomAddressFontColor() {
        return customAddressFontColor;
    }

    public void setCustomAddressFontColor(String customAddressFontColor) {
        this.customAddressFontColor = customAddressFontColor;
    }

    public String getFooterBackgroundColor() {
        return footerBackgroundColor;
    }

    public void setFooterBackgroundColor(String footerBackgroundColor) {
        this.footerBackgroundColor = footerBackgroundColor;
    }

    public SettingsPdfTemplateItem toHeaderAndFooterTO() {
        SettingsPdfTemplateItem item = new SettingsPdfTemplateItem();
        item.setObjectId(this.getObjectID());
        item.setCustomizedHeader(this.getCustomizedHeader());
        item.setCompanyLogoEnabled(this.getCompanyLogoEnabled());
        item.setCompanyNameEnabled(this.getCompanyNameEnabled());
        item.setCompanyNameFontSize(this.getCompanyNameFontSize());
        item.setCompanyNameFontColor(this.getCompanyNameFontColor());
        item.setCustomizedFooter(this.getCustomizedFooter());
        item.setPaginationEnabled(this.getPaginationEnabled());
        item.setDocumentTitleEnabled(this.getDocumentTitleEnabled());
        item.setDocumentTitleFontSize(this.getDocumentTitleFontSize());
        item.setDocumentTitleFontColor(this.getDocumentTitleFontColor());
        item.setValueByPosition(getDynamicFooterHeaderItems());
        return item;
    }

    public SettingsPdfTemplateItem toContentTO() {
        SettingsPdfTemplateItem item = new SettingsPdfTemplateItem();
        item.setObjectId(this.getObjectID());
        item.setCustomizedContent(this.getCustomizedContent());
        item.setTableBorderEnabled(this.getTableBorderEnabled());
        item.setTableBorderColor(this.getTableBorderColor());
        item.setItemRowEnabled(this.getItemRowEnabled());
        item.setItemRowFontSize(this.getItemRowFontSize());
        item.setItemRowBackgroundColorEnabled(this.getItemRowBackgroundColorEnabled());
        item.setItemRowBackgroundColor(this.getItemRowBackgroundColor());
        item.setItemRowFontColor(this.getItemRowFontColor());
        item.setTableHeaderFontSize(this.getTableHeaderFontSize());
        item.setTableHeaderBackgroundColorEnabled(this.getTableHeaderBackgroundColorEnabled());
        item.setTableHeaderBackgroundColor(this.getTableHeaderBackgroundColor());
        item.setTableHeaderFontColor(this.getTableHeaderFontColor());

        item.setCustomizedFooter(this.getCustomizedFooter());
        item.setQrCodeEnabled(this.getQrCodeEnabled());
        item.setPoweredByEnabled(this.getPoweredByEnabled());
        item.setCustomAddressEnabled(this.getCustomAddressEnabled());
        item.setCustomAddress(this.getCustomAddress());
        item.setCustomAddressFontSize(this.getCustomAddressFontSize());
        item.setCustomAddressFontColor(this.getCustomAddressFontColor());
        item.setFooterBackgroundColor(this.getFooterBackgroundColor());
        item.setValueByPosition(getDynamicFooterHeaderItems());
        return item;
    }

    public List<EdsPdfDynamicFooterHeader> getDynamicFooterHeaders() {
        return dynamicFooterHeaders;
    }

    public void setDynamicFooterHeaders(List<EdsPdfDynamicFooterHeader> dynamicFooterHeaders) {
        this.dynamicFooterHeaders = dynamicFooterHeaders;
    }

    private ArrayList<PdfFooterHeaderContentItem> getDynamicFooterHeaderItems() {
        ArrayList<PdfFooterHeaderContentItem> list = new ArrayList<>();
        getDynamicFooterHeaders().forEach(item -> {
            list.add(new PdfFooterHeaderContentItem(item.getKey(), item.getValue(), item.getEnable()));
        });
        return list;
    }
}
