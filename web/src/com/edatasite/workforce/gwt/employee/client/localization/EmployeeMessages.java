package com.edatasite.workforce.gwt.employee.client.localization;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 13.11.2008
 * Time: 14:56:10
 * To change this template use File | Settings | File Templates.
 */

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface EmployeeMessages extends Messages {

    String userNameON(String p0);

    String isProjectManager(String p0);

    String isDepartmentLeader(String p0);

    String wantToDeleteEmployee(String p0);
    
    String wantToTerminateTheEmploymentOf(String p0);


    class App {
        public static EmployeeMessages get() {
            return (EmployeeMessages) GWT.create(EmployeeMessages.class);
        }
    }
}