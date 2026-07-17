package com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;


/**
 * Created by Dilsh0d on 10/28/2017.
 */

public class BenefitRequestCalcItemListTO extends ResponseData {
    private ArrayList<BenefitRequestCalcItemTO> benefit_calc_records;

    public BenefitRequestCalcItemListTO() {
    }

    public BenefitRequestCalcItemListTO(ArrayList<BenefitRequestCalcItemTO> benefit_calc_records) {
        this.benefit_calc_records = benefit_calc_records;
    }

    public ArrayList<BenefitRequestCalcItemTO> getBenefit_calc_records() {
        return benefit_calc_records;
    }

    public void setBenefit_calc_records(ArrayList<BenefitRequestCalcItemTO> benefit_calc_records) {
        this.benefit_calc_records = benefit_calc_records;
    }
}

