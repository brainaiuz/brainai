package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsHistory;
import com.edatasite.workforce.core.domain.EdsUser;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jul 24, 2009
 * Time: 1:12:08 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "quoteHistory")
public class EdsQuoteHistory extends EdsHistory {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quoteId")
    private EdsSaleQuote quote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentatorId")
    private EdsUser commentator;

    @Column(name = "comment")
    @Type(type = "text")
    private String comment;

    public EdsSaleQuote getQuote() {
        return quote;
    }

    public void setQuote(EdsSaleQuote quote) {
        this.quote = quote;
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
}
