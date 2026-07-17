package com.edatasite.workforce.rest.v2.release10.core.to.payroll;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.StatusTO;

public class PayslipRecordTO extends ResponseData {
    private Integer id;
    private String title;
    private StatusTO record_type;
    private String remarks;
    private CurrencyValueTO sum;

    public PayslipRecordTO() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StatusTO getRecord_type() {
        return record_type;
    }

    public void setRecord_type(StatusTO record_type) {
        this.record_type = record_type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public CurrencyValueTO getSum() {
        return sum;
    }

    public void setSum(CurrencyValueTO sum) {
        this.sum = sum;
    }
}
