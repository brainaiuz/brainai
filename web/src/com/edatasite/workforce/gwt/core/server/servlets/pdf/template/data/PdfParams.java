package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import com.edatasite.workforce.gwt.profile.client.rpc.PdfTemplateTableSettingsItem;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class PdfParams {
    private String type;
    // Pdf outer params
    private String format;
    private Orientation orientation;
    private String headerHeight;
    private String footerHeight;
    private String marginTop;
    private String marginRight;
    private String marginBottom;
    private String marginLeft;

    //Pdf header params
    private Boolean companyLogoEnabled = true;
    private Boolean companyNameEnabled = true;
    private String companyNameFontSize = "9";
    private String companyNameFontColor = "#939598";
    private Boolean paginationEnabled = true;
    private Boolean documentTitleEnabled = true;
    private String documentTitleFontSize = "11";
    private String documentTitleFontColor = "#000000";

    //Pdf content params
    private Boolean tableBorderEnabled = true;
    private String tableBorderColor = "#ced5db";
    private Boolean itemRowEnabled = true;
    private String itemRowFontSize = "9";
    private Boolean itemRowBackgroundColorEnabled = false;
    private String itemRowBackgroundColor = "";
    private String itemRowFontColor = "#000000";
    private String tableHeaderFontSize = "9";
    private Boolean tableHeaderBackgroundColorEnabled = false;
    private String tableHeaderBackgroundColor = "";
    private String tableHeaderFontColor = "#000000";
    private List<PdfTemplateTableSettingsItem> tableColumns;

    //Pdf footer params
    private Boolean qrCodeEnabled = true;
    private Boolean poweredByEnabled = true;
    private Boolean customAddressEnabled = false;
    private String customAddress = "";
    private String customAddressFontSize = "7";
    private String customAddressFontColor = "#939598";
    private String footerBackgroundColor = "#ffffff";
    private String templateName = "";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
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

    public Boolean getCompanyLogoEnabled() {
        return companyLogoEnabled;
    }

    public void setCompanyLogoEnabled(Boolean companyLogoEnabled) {
        this.companyLogoEnabled = companyLogoEnabled;
    }

    public Boolean getCompanyNameEnabled() {
        return companyNameEnabled;
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
        return paginationEnabled;
    }

    public void setPaginationEnabled(Boolean paginationEnabled) {
        this.paginationEnabled = paginationEnabled;
    }

    public Boolean getDocumentTitleEnabled() {
        return documentTitleEnabled;
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

    public List<PdfTemplateTableSettingsItem> getTableColumns() {
        return tableColumns;
    }

    public void setTableColumns(List<PdfTemplateTableSettingsItem> tableColumns) {
        this.tableColumns = tableColumns;
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
        return customAddressEnabled;
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

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public enum Orientation {
        portrait,
        landscape;

        public static Orientation getOrientation(boolean isLandscape) {
            if (isLandscape) {
                return landscape;
            } else {
                return portrait;
            }
        }

        public static Orientation getByCode(String code) {
            if (StringUtils.isEmpty(code)) {
                return portrait;
            }
            for (Orientation orientation : Orientation.values()) {
                if (orientation.name().equals(code)) {
                    return orientation;
                }
            }
            return portrait;
        }
    }
}
