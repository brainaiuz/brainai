package com.edatasite.workforce.rest.v2.release10.core.to.payroll;


import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 11/28/2017.
 */

public class CashAdvanceListResultTO extends ResponseData {

    private ArrayList<CashAdvanceListItemTO> cash_advances;


    public CashAdvanceListResultTO() {
    }

    public CashAdvanceListResultTO(ArrayList<CashAdvanceListItemTO> cash_advances) {
        this.cash_advances = cash_advances;
    }

    public ArrayList<CashAdvanceListItemTO> getCash_advances() {
        return cash_advances;
    }

    public void setCash_advances(ArrayList<CashAdvanceListItemTO> cash_advances) {
        this.cash_advances = cash_advances;
    }
}
