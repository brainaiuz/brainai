package com.edatasite.workforce.rest.v3.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * User : Dilsh0d Madrahimov on 9/17/2019 3:46 PM
 */
public class UserBatchAddDTO extends ResponseData {
    private ArrayList<UserAddDTO> users;

    public ArrayList<UserAddDTO> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserAddDTO> users) {
        this.users = users;
    }
}
