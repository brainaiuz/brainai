package com.edatasite.workforce.rest.v2.release10.core.to.payroll;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class PayslipTotalTO extends ResponseData {
    private Integer id;
    private String title;
    private CurrencyValueTO sum;

    public PayslipTotalTO() {
    }

    public PayslipTotalTO(Integer id, String title, CurrencyValueTO sum) {
        this.id = id;
        this.title = title;
        this.sum = sum;
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

    public CurrencyValueTO getSum() {
        return sum;
    }

    public void setSum(CurrencyValueTO sum) {
        this.sum = sum;
    }
}
