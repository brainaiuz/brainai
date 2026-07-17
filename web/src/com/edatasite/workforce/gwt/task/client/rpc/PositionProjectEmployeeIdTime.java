package com.edatasite.workforce.gwt.task.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 14.11.2008
 * Time: 20:53:55
 * To change this template use File | Settings | File Templates.
 */
public class PositionProjectEmployeeIdTime implements IsSerializable {
    private IdTime projectEmployee[];

    public IdTime[] getProjectEmployee() {
        return projectEmployee;
    }

    public void setProjectEmployee(IdTime[] projectEmployee) {
        this.projectEmployee = projectEmployee;
    }

}
