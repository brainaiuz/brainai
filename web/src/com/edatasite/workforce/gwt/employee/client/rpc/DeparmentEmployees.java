package com.edatasite.workforce.gwt.employee.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 22.05.12
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public class DeparmentEmployees extends SelectItem {

    private ArrayList<EmployeeListItem> members;

    public DeparmentEmployees() {
        super();
    }

    public DeparmentEmployees(Integer id, String name) {
        super(id, name);
    }

    public ArrayList<EmployeeListItem> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<EmployeeListItem> members) {
        this.members = members;
    }
}
