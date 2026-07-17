package com.edatasite.workforce.gwt.hrms.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 13.11.2008
 * Time: 16:03:42
 * To change this template use File | Settings | File Templates.
 */
public interface HrmsMessages extends Messages {

    String currentlyThereAreNo(String o1);

    String youCanStartRegisteringYour(String o1);

    String hasBeenAddedSucc(String o1);

    String youCanStartRegistering(String o1);

    String attendanceReportForMonthYear(String o1, String o2);

    String attendanceReportForMonthYearToDepartment(String o1, String o2, String o3);

    class App {
        public static HrmsMessages get() {
            return (HrmsMessages) GWT.create(HrmsMessages.class);
        }
    }

}
