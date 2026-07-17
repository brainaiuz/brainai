package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsSuperUser;
import com.edatasite.workforce.core.domain.EdsUser;
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
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "stockTransferNote")
public class EdsStockAdjustmentNote extends EdsSuperUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockAdjustmentId")
    private EdsStockAdjustment stockAdjustment;

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

    public EdsStockAdjustment getStockAdjustment() {
        return this.stockAdjustment;
    }

    public void setStockAdjustment(final EdsStockAdjustment stockAdjustment) {
        this.stockAdjustment = stockAdjustment;
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
}
