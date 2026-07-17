package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 26.11.2008
 * Time: 13:59:04
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "expenseHistory")
public class EdsExpenseHistory extends EdsHistory {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportId")
    private EdsExpenseReport expenseReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentatorId")
    private EdsEmployee employee;

    @Column(name = "comment")
    @Type(type = "text")
    private String comment;

    public HistoryListItem getHistoryItem() {

        HistoryListItem item = new HistoryListItem();
        item.setObjectID(getObjectID());
        item.setEventDate(new Date(getEventDate().getTime()));
        item.setEventDescription(getEventDescription());
        if (isSuperUser()) {
            item.setEmployee(Constants.defaultSupportName);
        } else {
            item.setEmployee(getEmployee().getFullName());
        }
        item.setEmployeeID(getEmployee().getObjectID());
        item.setComment(getComment());

        return item;
    }

    public EdsExpenseReport getExpenseReport() {
        return expenseReport;
    }

    public void setExpenseReport(EdsExpenseReport expenseReport) {
        this.expenseReport = expenseReport;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
