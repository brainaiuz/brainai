package com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;


/**
 * Created by Dilsh0d on 10/28/2017.
 */

public class BenefitRequestCalcItemTO extends ResponseData {
    private String title;
    private String amount;

    public BenefitRequestCalcItemTO() {
    }

    public BenefitRequestCalcItemTO(String title, String amount) {
        this.title = title;
        this.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

