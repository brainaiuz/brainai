package com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 10/11/2017.
 */
public class LeaveStatesCalResponseTO extends RequestListData {

    private ArrayList<LeaveReasonStateTO> state_records;

    public LeaveStatesCalResponseTO() {
    }

    public LeaveStatesCalResponseTO(ArrayList<LeaveReasonStateTO> state_records) {
        this.state_records = state_records;
    }

    public ArrayList<LeaveReasonStateTO> getState_records() {
        return state_records;
    }

    public void setState_records(ArrayList<LeaveReasonStateTO> state_records) {
        this.state_records = state_records;
    }
}
