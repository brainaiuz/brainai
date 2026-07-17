package com.edatasite.workforce.gwt.core.server.controllers.hmrc.dto;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.List;

public class HmrcObligations extends ResponseData {

    List<HmrcObligation> obligations;

    public List<HmrcObligation> getObligations() {
        return obligations;
    }

    public void setObligations(List<HmrcObligation> obligations) {
        this.obligations = obligations;
    }
}
