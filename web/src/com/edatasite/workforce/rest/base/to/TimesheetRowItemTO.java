package com.edatasite.workforce.rest.base.to;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov on 26.10.2016.
 * This class for Timesheet API Syc from Excel plugin.
 */

public class TimesheetRowItemTO implements IsSerializable {

    EmployeeTO employee;
    ArrayList<TaskMiniTO> tasks;

    public EmployeeTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeTO employee) {
        this.employee = employee;
    }

    public ArrayList<TaskMiniTO> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<TaskMiniTO> tasks) {
        this.tasks = tasks;
    }

}
