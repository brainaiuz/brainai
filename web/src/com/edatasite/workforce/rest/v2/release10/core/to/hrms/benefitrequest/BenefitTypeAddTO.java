package com.edatasite.workforce.rest.v2.release10.core.to.hrms.benefitrequest;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dilsh0d on 10/28/2017.
 */
public class BenefitTypeAddTO extends ResponseData {
    private Integer user_id;
    private ArrayList<Integer> approvers;
    @Schema(description = "Leave start DateTime (format: dd-MM-yyyy'T'hh:mm:ssZ)", required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'hh:mm:ssZ")
    private String date;
    private String description;
    private Integer benefit_type;
    private BigDecimal requested_amount;

    public BenefitTypeAddTO() {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBenefit_type() {
        return benefit_type;
    }

    public void setBenefit_type(Integer benefit_type) {
        this.benefit_type = benefit_type;
    }

    public BigDecimal getRequested_amount() {
        return requested_amount;
    }

    public void setRequested_amount(BigDecimal requested_amount) {
        this.requested_amount = requested_amount;
    }
}
