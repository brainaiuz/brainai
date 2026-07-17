package com.workforcetrack.mobile.rpc.timesheet;

import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskStatus;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * User: Abdulaziz
 * Date: 8/18/11
 * Time: 5:50 PM
 */
@XmlRootElement
public class MTaskStatus implements Serializable{

    private int taskId;
    private int employeeTaskId;
    private int status;
    private String statusName;

    public MTaskStatus(){

    }
    public MTaskStatus(TaskStatus status){
        this.taskId = status.getTaskId();
        this.employeeTaskId = status.getEmployeeTaskId();
        this.status = status.getStatus();
        this.statusName = status.getStatusName();
    }
    public static TaskStatus convertFromMobile(MTaskStatus mstatus){
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setStatus(mstatus.status);
        taskStatus.setTaskId(mstatus.taskId);
        taskStatus.setEmployeeTaskId(mstatus.employeeTaskId);
        taskStatus.setStatusName(mstatus.statusName);
        return  taskStatus;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getEmployeeTaskId() {
        return employeeTaskId;
    }

    public void setEmployeeTaskId(int employeeTaskId) {
        this.employeeTaskId = employeeTaskId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

}
