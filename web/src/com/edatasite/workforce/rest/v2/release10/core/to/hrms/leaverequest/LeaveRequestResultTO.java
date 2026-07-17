package com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class LeaveRequestResultTO extends ResponseData {
    private ArrayList<LeaveRequestStatusTO> leave_statuses;
    private ArrayList<LeaveRequestTO> leave_requests;

    public ArrayList<LeaveRequestStatusTO> getLeave_statuses() {
        return leave_statuses;
    }

    public void setLeave_statuses(ArrayList<LeaveRequestStatusTO> leave_statuses) {
        this.leave_statuses = leave_statuses;
    }

    public ArrayList<LeaveRequestTO> getLeave_requests() {
        return leave_requests;
    }

    public void setLeave_requests(ArrayList<LeaveRequestTO> leave_requests) {
        this.leave_requests = leave_requests;
    }
}
