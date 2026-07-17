package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsSuperUser;
import com.edatasite.workforce.core.domain.EdsUser;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Shohruh on 03-Feb-16.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rfqrfpNote")
public class EdsRfqRfpNote extends EdsSuperUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfpId")
    private EdsRFP rfp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rfqId")
    private EdsRFQ rfq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentatorId")
    private EdsUser commentator;

    @Column(name = "comment", length = 1000)
    @Type(type = "text")
    private String comment;

    private Date date;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsRFP getRfp() {
        return rfp;
    }

    public void setRfp(EdsRFP rfp) {
        this.rfp = rfp;
    }

    public EdsRFQ getRfq() {
        return rfq;
    }

    public void setRfq(EdsRFQ rfq) {
        this.rfq = rfq;
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
