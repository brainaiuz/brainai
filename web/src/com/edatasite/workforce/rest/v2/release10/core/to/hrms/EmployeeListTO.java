package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListData;

import java.util.List;

/**
 * Created by Anvar Akramov on 11/06/2017.
 */
public class EmployeeListTO extends RequestListData {

    private Integer total_count;
    private Integer left;
    private Integer count;
    private Integer offset;
    private List<EmployeeTO> users_list;

    public EmployeeListTO() {
    }

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public EmployeeListTO(List<EmployeeTO> users_list) {
        this.users_list = users_list;
    }

    public List<EmployeeTO> getUsers_list() {
        return users_list;
    }

    public void setUsers_list(List<EmployeeTO> users_list) {
        this.users_list = users_list;
    }
}
