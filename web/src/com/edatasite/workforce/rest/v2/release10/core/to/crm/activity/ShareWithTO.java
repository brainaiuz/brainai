package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 03/23/2018.
 */
public class ShareWithTO extends ResponseData {

    private ArrayList<String> emails;
    private Boolean send_invites;
    private ArrayList<ShareWithDepartmentsTO> departments;
    private String employees_type;

    public ShareWithTO() {
    }

    public ArrayList<String> getEmails() {
        return emails;
    }

    public void setEmails(ArrayList<String> emails) {
        this.emails = emails;
    }

    public Boolean getSend_invites() {
        return send_invites;
    }

    public void setSend_invites(Boolean send_invites) {
        this.send_invites = send_invites;
    }

    public ArrayList<ShareWithDepartmentsTO> getDepartments() {
        return departments;
    }

    public void setDepartments(ArrayList<ShareWithDepartmentsTO> departments) {
        this.departments = departments;
    }

    public String getEmployees_type() {
        return employees_type;
    }

    public void setEmployees_type(String employees_type) {
        this.employees_type = employees_type;
    }
}
