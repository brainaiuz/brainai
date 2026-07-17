package com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 20/11/2017.
 */
public class LeaveRequestDetailsTO extends LeaveRequestTO {

    private EmployeeTO owner;
    @Schema(description = "Leave start DateTime (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String created_at;
    private boolean from_annual;
    private ArrayList<LeaveRequestApproverTO> approvers;
    private ArrayList<AttachmentTO> attachments;
    private ArrayList<LeaveReasonStateTO> state_records;
    private RequestUserActionTO user_actions;

    public LeaveRequestDetailsTO() {
    }

    public EmployeeTO getOwner() {
        return owner;
    }

    public void setOwner(EmployeeTO owner) {
        this.owner = owner;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isFrom_annual() {
        return from_annual;
    }

    public void setFrom_annual(boolean from_annual) {
        this.from_annual = from_annual;
    }

    public ArrayList<LeaveRequestApproverTO> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<LeaveRequestApproverTO> approvers) {
        this.approvers = approvers;
    }

    public ArrayList<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    public ArrayList<LeaveReasonStateTO> getState_records() {
        return state_records;
    }

    public void setState_records(ArrayList<LeaveReasonStateTO> state_records) {
        this.state_records = state_records;
    }

    public RequestUserActionTO getUser_actions() {
        return user_actions;
    }

    public void setUser_actions(RequestUserActionTO user_actions) {
        this.user_actions = user_actions;
    }
}
