package com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 11/10/2017.
 */
@Schema(name = "CreateLeaveRequest")
public class CreateLeaveRequestTO extends ResponseData {
    @Schema(description = "id of Employee", required = true)
    private Integer user_id;//Employee
    @Schema(description = "array of Approvers id", required = true)
    private ArrayList<Integer> approvers;//Approvers
    @Schema(description = "id of Reason", required = true)
    private Integer leave_reason;//Reason
    @Schema(description = "Leave start DateTime (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String start_date;
    @Schema(description = "Leave end DateTime (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String end_date;
    @Schema(description = "Description", required = false)
    private String description;
    @Schema(description = "Leave by (available values are DAY or MONEY)", required = true)
    private String leave_by;//DAY or MONEY
    @Schema(description = "Annual allowance", required = true)
    private boolean annual_allowance;

    public CreateLeaveRequestTO() {
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public ArrayList<Integer> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<Integer> approvers) {
        this.approvers = approvers;
    }

    public Integer getLeave_reason() {
        return leave_reason;
    }

    public void setLeave_reason(Integer leave_reason) {
        this.leave_reason = leave_reason;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLeave_by() {
        return leave_by;
    }

    public void setLeave_by(String leave_by) {
        this.leave_by = leave_by;
    }

    public boolean isAnnual_allowance() {
        return annual_allowance;
    }

    public void setAnnual_allowance(boolean annual_allowance) {
        this.annual_allowance = annual_allowance;
    }
}
