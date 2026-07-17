package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * User: Abror Abdukadirov
 * Date: 12.12.2018 17:00
 */
public class SettingsPdfTemplateItem implements IsSerializable {
    private Integer objectId;
    private String pdfType;
    private String pdfName;
    private boolean isSystemPdf;
    private boolean isDefaultTemplate;
    private SelectItem templateItem;

    private String orientation;
    private String marginTop;
    private String marginRight;
    private String marginBottom;
    private String marginLeft;

    //Header config
    private Boolean isCustomizedHeader;
    private Boolean companyLogoEnabled;
    private Boolean companyNameEnabled;
    private String companyName;
    private String companyNameFontSize;
    private String companyNameFontColor;
    private Boolean paginationEnabled;
    private Boolean documentTitleEnabled;
    private String documentTitle;
    private String defaultDocumentTitle;
    private String documentTitleFontSize;
    private String documentTitleFontColor;

    //Content config
    private Boolean isCustomizedContent;
    private Boolean tableBorderEnabled;
    private String tableBorderColor;
    private Boolean itemRowEnabled;
    private String itemRowFontSize;
    private Boolean itemRowBackgroundColorEnabled;
    private String itemRowBackgroundColor;
    private String itemRowFontColor;
    private String tableHeaderFontSize;
    private Boolean tableHeaderBackgroundColorEnabled;
    private String tableHeaderBackgroundColor;
    private String tableHeaderFontColor;
    private LinkedList<PdfTemplateTableSettingsItem> tableColumns;

    //Footer config
    private Boolean isCustomizedFooter;
    private Boolean qrCodeEnabled;
    private Boolean poweredByEnabled;
    private Boolean customAddressEnabled;
    private String companyAddress;
    private String customAddress;
    private String customAddressFontSize;
    private String customAddressFontColor;
    private String footerBackgroundColor;

    private ArrayList<PdfFooterHeaderContentItem> valueByPosition;
    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getPdfType() {
        return pdfType;
    }

    public void setPdfType(String pdfType) {
        this.pdfType = pdfType;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public boolean isSystemPdf() {
        return isSystemPdf;
    }

    public void setSystemPdf(boolean systemPdf) {
        isSystemPdf = systemPdf;
    }

    public boolean isDefaultTemplate() {
        return isDefaultTemplate;
    }

    public void setDefaultTemplate(boolean defaultTemplate) {
        isDefaultTemplate = defaultTemplate;
    }

    public SelectItem getTemplateItem() {
        return templateItem;
    }

    public void setTemplateItem(SelectItem templateItem) {
        this.templateItem = templateItem;
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

    public Boolean getCustomizedFooter() {
        return isCustomizedFooter != null ? isCustomizedFooter : false;
    }

    public void setCustomizedFooter(Boolean customizedFooter) {
        isCustomizedFooter = customizedFooter;
    }

    public Boolean getCompanyLogoEnabled() {
        return companyLogoEnabled != null ? companyLogoEnabled : false;
    }

    public void setCompanyLogoEnabled(Boolean companyLogoEnabled) {
        this.companyLogoEnabled = companyLogoEnabled;
    }

    public Boolean getCompanyNameEnabled() {
        return companyNameEnabled != null ? companyNameEnabled : false;
    }

    public void setCompanyNameEnabled(Boolean companyNameEnabled) {
        this.companyNameEnabled = companyNameEnabled;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
        return paginationEnabled != null ? paginationEnabled : false;
    }

    public void setPaginationEnabled(Boolean paginationEnabled) {
        this.paginationEnabled = paginationEnabled;
    }

    public Boolean getDocumentTitleEnabled() {
        return documentTitleEnabled != null ? documentTitleEnabled : false;
    }

    public void setDocumentTitleEnabled(Boolean documentTitleEnabled) {
        this.documentTitleEnabled = documentTitleEnabled;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getDefaultDocumentTitle() {
        return defaultDocumentTitle;
    }

    public void setDefaultDocumentTitle(String defaultDocumentTitle) {
        this.defaultDocumentTitle = defaultDocumentTitle;
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
        return isCustomizedContent;
    }

    public void setCustomizedContent(Boolean customizedContent) {
        isCustomizedContent = customizedContent;
    }

    public Boolean getTableBorderEnabled() {
        return tableBorderEnabled;
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
        return itemRowEnabled;
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
        return itemRowBackgroundColorEnabled;
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
        return tableHeaderBackgroundColorEnabled;
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

    public LinkedList<PdfTemplateTableSettingsItem> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(LinkedList<PdfTemplateTableSettingsItem> tableColumns) {
        this.tableColumns = tableColumns;
    }

    public Boolean getCustomizedHeader() {
        return isCustomizedHeader != null ? isCustomizedHeader : false;
    }

    public void setCustomizedHeader(Boolean customizedHeader) {
        isCustomizedHeader = customizedHeader;
    }

    public Boolean getQrCodeEnabled() {
        return qrCodeEnabled;
    }

    public void setQrCodeEnabled(Boolean qrCodeEnabled) {
        this.qrCodeEnabled = qrCodeEnabled;
    }

    public Boolean getPoweredByEnabled() {
        return poweredByEnabled;
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

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
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

    public ArrayList<PdfFooterHeaderContentItem> getValueByPosition() {
        if (valueByPosition == null) {
            valueByPosition = new ArrayList<>();
        }
        return valueByPosition;
    }

    public void setValueByPosition(ArrayList<PdfFooterHeaderContentItem> valueByPosition) {
        this.valueByPosition = valueByPosition;
    }
}
