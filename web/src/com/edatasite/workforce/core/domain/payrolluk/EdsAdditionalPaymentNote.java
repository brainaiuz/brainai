package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsSuperUser;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "additionalPaymentNote")
public class EdsAdditionalPaymentNote extends EdsSuperUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "additionalPaymentId")
    private EdsAdditionalPayment payment;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentatorId")
    private EdsUser commentator;


    @Column(name = "comment", length = 1000)
    @Type(type = "text")
    private String comment;

    private Date date;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsAdditionalPayment getPayment() {
        return payment;
    }

    public void setPayment(EdsAdditionalPayment payment) {
        this.payment = payment;
    }

    public EdsUser getCommentator() {
        return commentator;
    }

    public void setCommentator(EdsUser commentator) {
        this.commentator = commentator;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public HistoryListItem getHistoryItem() {

        HistoryListItem item = new HistoryListItem();
        item.setObjectID(getObjectID());
        item.setEventDate(getDate());
        if (isSuperUser()) {
            item.setEmployee(Constants.defaultSupportName);
        } else {
            item.setEmployee(getCommentator().getFullName());
        }
        item.setEmployeeID(getCommentator().getObjectID());
        item.setComment(getComment());
        return item;
    }
}
