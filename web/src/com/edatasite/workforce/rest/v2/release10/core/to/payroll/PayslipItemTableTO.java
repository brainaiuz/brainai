package com.edatasite.workforce.rest.v2.release10.core.to.payroll;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

public class PayslipItemTableTO extends ResponseData {
    private CurrencyValueTO total;
    private ArrayList<PayslipRecordTO> records;

    public PayslipItemTableTO() {
    }

    public PayslipItemTableTO(CurrencyValueTO total, ArrayList<PayslipRecordTO> records) {
        this.total = total;
        this.records = records;
    }

    public CurrencyValueTO getTotal() {
        return total;
    }

    public void setTotal(CurrencyValueTO total) {
        this.total = total;
    }

    public ArrayList<PayslipRecordTO> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<PayslipRecordTO> records) {
        this.records = records;
    }
}
