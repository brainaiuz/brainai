package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.RequestUserActionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.ApproversTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.OwnerTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 04.01.2018
 */
public class OtherRequestDetailsTO extends ResponseData {
    private Integer id;
    private OwnerTO owner;
    private String title;
    private String description;
    private Object status;
    private ArrayList<ApproversTO> approvers;
    private ArrayList<CustomFieldsTO> custom_fields;
    private RequestUserActionTO user_actions;

    public OtherRequestDetailsTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public OwnerTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerTO owner) {
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

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public ArrayList<ApproversTO> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<ApproversTO> approvers) {
        this.approvers = approvers;
    }

    public ArrayList<CustomFieldsTO> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<CustomFieldsTO> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public RequestUserActionTO getUser_actions() {
        return user_actions;
    }

    public void setUser_actions(RequestUserActionTO user_actions) {
        this.user_actions = user_actions;
    }
}
