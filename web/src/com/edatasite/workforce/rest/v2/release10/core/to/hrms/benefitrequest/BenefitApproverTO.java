
package com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.StatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;


/**
 * Created by Dilsh0d on 10/28/2017.
 */

public class BenefitApproverTO extends EmployeeTO {
    private StatusTO status;

    public StatusTO getStatus() {
        return status;
    }

    public void setStatus(StatusTO status) {
        this.status = status;
    }
}

