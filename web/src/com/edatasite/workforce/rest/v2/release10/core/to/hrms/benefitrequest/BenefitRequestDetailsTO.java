package com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.RequestUserActionTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class BenefitRequestDetailsTO extends ResponseData {
    private Integer id;
    private EmployeeTO owner;
    private String title;
    private String description;
    private double amount;
    @Schema(description = "Leave start DateTime (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String date;
    private ArrayList<BenefitApproverTO> approvers;
    private Object status;
    @Schema(description = "Leave start DateTime (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String created_at;
    private RequestUserActionTO user_actions;

    public BenefitRequestDetailsTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EmployeeTO getOwner() {
        return owner;
    }

    public void setOwner(EmployeeTO owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<BenefitApproverTO> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<BenefitApproverTO> approvers) {
        this.approvers = approvers;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public RequestUserActionTO getUser_actions() {
        return user_actions;
    }

    public void setUser_actions(RequestUserActionTO user_actions) {
        this.user_actions = user_actions;
    }
}
