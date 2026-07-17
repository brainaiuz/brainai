package com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class BenefitTypeListTO extends ResponseData {

    private ArrayList<CategoryTO> benefit_types;

    public BenefitTypeListTO() {
    }

    public ArrayList<CategoryTO> getBenefit_types() {
        return benefit_types;
    }

    public void setBenefit_types(ArrayList<CategoryTO> benefit_types) {
        this.benefit_types = benefit_types;
    }
}
