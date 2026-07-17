package com.edatasite.workforce.gwt.dashboard.server.app;

/**
 * Created by IntelliJ IDEA.
 * User: DELL
 * Date: 20-May-2009
 * Time: 05:46:51
 * To change this template use File | Settings | File Templates.
 */
public class InOutKey {
    private String employeeId;
    private String date;

    public InOutKey(String employeeId, String date) {
        this.date = date;
        this.employeeId = employeeId;
    }

    public boolean equals(Object o) {
        return
                (o instanceof InOutKey) && (employeeId.equals(((InOutKey) o).employeeId)) && (date.equals(((InOutKey) o).date));
    }


    public int hashCode() {
        return Integer.valueOf(employeeId);
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
