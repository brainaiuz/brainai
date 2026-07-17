package com.edatasite.workforce.rest.v3.release10.accounting.dto.fai;

public class FaiWebhookPayload {
    private String id;
    private String original_order_id;
    private LongInvoicePreviewLinks long_invoice_preview_links;
    private LongInvoicePreviewLinks simple_invoice_preview_links;
    private String zatca_status;
    private FaiZatcaResponse zatca_response;
    private String zatca_xml_file;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal_order_id() {
        return original_order_id;
    }

    public void setOriginal_order_id(String original_order_id) {
        this.original_order_id = original_order_id;
    }

    public LongInvoicePreviewLinks getLong_invoice_preview_links() {
        return long_invoice_preview_links;
    }

    public void setLong_invoice_preview_links(LongInvoicePreviewLinks long_invoice_preview_links) {
        this.long_invoice_preview_links = long_invoice_preview_links;
    }

    public LongInvoicePreviewLinks getSimple_invoice_preview_links() {
        return simple_invoice_preview_links;
    }

    public void setSimple_invoice_preview_links(LongInvoicePreviewLinks simple_invoice_preview_links) {
        this.simple_invoice_preview_links = simple_invoice_preview_links;
    }

    public String getPdfLinkAr() {
        return long_invoice_preview_links != null
                ? long_invoice_preview_links.getAr()
                : simple_invoice_preview_links != null
                ? simple_invoice_preview_links.getAr()
                : null;
    }

    public String getPdfLinkEn() {
        return long_invoice_preview_links != null
                ? long_invoice_preview_links.getEn()
                : simple_invoice_preview_links != null
                ? simple_invoice_preview_links.getEn()
                : null;
    }

    public String getZatca_status() {
        return zatca_status;
    }

    public void setZatca_status(String zatca_status) {
        this.zatca_status = zatca_status;
    }

    public boolean isZatcaStatusSuccess() {
        return zatca_status != null && zatca_status.toLowerCase().contains("success");
    }

    public boolean isZatcaStatusFailed() {
        return zatca_status != null && zatca_status.equalsIgnoreCase("failed");
    }

    public FaiZatcaResponse getZatca_response() {
        return zatca_response;
    }

    public void setZatca_response(FaiZatcaResponse zatca_response) {
        this.zatca_response = zatca_response;
    }

    public String getZatca_xml_file() {
        return zatca_xml_file;
    }

    public void setZatca_xml_file(String zatca_xml_file) {
        this.zatca_xml_file = zatca_xml_file;
    }
}
