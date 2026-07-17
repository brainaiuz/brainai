package com.edatasite.workforce.rest.v2.release10.core.to.payroll;


import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 11/28/2017.
 */

public class PayslipListResultTO extends ResponseData {

    private ArrayList<PayslipListItemTO> payslips;


    public PayslipListResultTO() {
    }

    public PayslipListResultTO(ArrayList<PayslipListItemTO> payslips) {
        this.payslips = payslips;
    }

    public ArrayList<PayslipListItemTO> getPayslips() {
        return payslips;
    }

    public void setPayslips(ArrayList<PayslipListItemTO> payslips) {
        this.payslips = payslips;
    }
}
