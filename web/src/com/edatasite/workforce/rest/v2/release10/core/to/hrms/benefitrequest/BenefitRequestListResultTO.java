package com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class BenefitRequestListResultTO extends ResponseData {
    private ArrayList<BenefitRequestTO> benefits;

    public BenefitRequestListResultTO() {
    }

    public BenefitRequestListResultTO(ArrayList<BenefitRequestTO> benefits) {
        this.benefits = benefits;
    }

    public ArrayList<BenefitRequestTO> getBenefits() {
        return benefits;
    }

    public void setBenefits(ArrayList<BenefitRequestTO> benefits) {
        this.benefits = benefits;
    }
}
