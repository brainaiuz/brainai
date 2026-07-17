package com.edatasite.workforce.rest.v2.release10.core.to.base.tax;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;

import java.math.BigDecimal;

/**
 * Created by Dilsh0d on 12/12/2017.
 */
public class TaxTO extends CategoryTO {

    private BigDecimal percent;

    public TaxTO() {
        super();
    }

    public TaxTO(Integer id, String title, BigDecimal percent) {
        super(id, title);
        this.percent = percent;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }
}
