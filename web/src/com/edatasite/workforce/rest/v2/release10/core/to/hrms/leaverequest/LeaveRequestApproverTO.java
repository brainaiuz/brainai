package com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;

/**
 * Created by Anvar Akramov on 20/11/2017.
 */
public class LeaveRequestApproverTO extends EmployeeTO {

    private ApproverListStatusTO status;

    public LeaveRequestApproverTO() {
    }

    public ApproverListStatusTO getStatus() {
        return status;
    }

    public void setStatus(ApproverListStatusTO status) {
        this.status = status;
    }
}
