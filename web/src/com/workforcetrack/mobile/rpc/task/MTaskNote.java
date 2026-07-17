package com.workforcetrack.mobile.rpc.task;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: shahob
 * Date: 24/08/12
 * Time: 20:42
 * To change this template use File | Settings | File Templates.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "taskNote")
public class MTaskNote {
    public static String action = "action";
    public static String SUBJECT = "subject";
    public static String NOTE = "note";
    public static String relatedTo = "relatedTo";
    public static String modified = "modified";
    public static String visibilit = "visibility";
    public static String owner = "owner";


    private Integer objectID;
    private String employee;
    private Date eventDate;

    public MTaskNote() {
    }

    public MTaskNote(HistoryListItem item) {
        if (item != null) {
            setObjectID(item.getObjectID());
            setEmployee(item.getEmployee());
            setEventDate(item.getEventDate());
        }
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
}
