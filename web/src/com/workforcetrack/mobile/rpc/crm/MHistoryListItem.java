package com.workforcetrack.mobile.rpc.crm;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 19.01.12
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MHistoryListItem {

    private Integer objectID;
    private Date eventDate;
    private String description;
    private String employee;
    private String comment;

    public MHistoryListItem() {

    }

    public MHistoryListItem(HistoryListItem item) {
        if (item != null) {
            this.objectID = item.getObjectID();
            this.eventDate = item.getEventDate();
            this.comment = item.getComment();
            this.employee = item.getEmployee();
            this.description = item.getEventDescription();
        }
    }

    public HistoryListItem convertFromMobile(HistoryListItem item) {
        if (item == null) {
            item = new HistoryListItem();
        }
        item.setObjectID(getObjectID());
        item.setEventDate(getEventDate());
        item.setComment(getComment());
        item.setEmployee(getEmployee());
        item.setEventDescription(getDescription());

        return item;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
