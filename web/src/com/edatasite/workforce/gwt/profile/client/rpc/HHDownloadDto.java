package com.edatasite.workforce.gwt.profile.client.rpc;

public class HHDownloadDto {
    private HHUrlDto pdf;
    private HHUrlDto rtf;

    public HHUrlDto getPdf() {
        return pdf;
    }

    public void setPdf(HHUrlDto pdf) {
        this.pdf = pdf;
    }

    public HHUrlDto getRtf() {
        return rtf;
    }

    public void setRtf(HHUrlDto rtf) {
        this.rtf = rtf;
    }
}
