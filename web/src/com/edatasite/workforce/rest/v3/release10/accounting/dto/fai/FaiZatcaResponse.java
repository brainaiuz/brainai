package com.edatasite.workforce.rest.v3.release10.accounting.dto.fai;

import java.util.List;

public class FaiZatcaResponse {
    private List<Object> errors;
    private List<FaiResponseInfo> warnings;
    private List<FaiResponseInfo> info;

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    public List<FaiResponseInfo> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<FaiResponseInfo> warnings) {
        this.warnings = warnings;
    }

    public List<FaiResponseInfo> getInfo() {
        return info;
    }

    public void setInfo(List<FaiResponseInfo> info) {
        this.info = info;
    }
}
