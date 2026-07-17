package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.EdsSuperUser;
import com.edatasite.workforce.core.domain.EdsUser;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "stockTransferNote")
public class EdsStockTransferNote extends EdsSuperUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockTransferId")
    private EdsStockTransfer stockTransfer;

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

    public EdsStockTransfer getStockTransfer() {
        return stockTransfer;
    }

    public void setStockTransfer(EdsStockTransfer stockTransfer) {
        this.stockTransfer = stockTransfer;
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
