/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/7 6:57:46                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.core.domain.crm;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 21-Jul-2009
 * Time: 13:32:41
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "crmcasedetails")
public class EdsEmailDetails extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "ccemails")
    @Type(type = "text")
    private String toCC;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "replyto")
    @Type(type = "text")
    private String replyTo;

    @Column(name = "toBCC")
    @Type(type = "text")
    private String toBCC;

    public void settoCC(String ccEmails) {
        this.toCC = refactorNulls(ccEmails);
    }

    public void setDescription(String description) {
        this.description = refactorNulls(description);
    }

    public void setReplyTo(String replyTo) {
        if (replyTo != null) {
            replyTo = replyTo.length() > 254 ? replyTo.substring(0, 254) : replyTo;
        }
        this.replyTo = replyTo;
    }

    public CaseItem getRPC(CaseItem item) {
        if (item == null) {
            item = new CaseItem();
        }
        item.setDescription(getDescription());
        item.setCcEmails(getToCC());
        return item;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getToCC() {
        return toCC;
    }

    public void setToCC(String toCC) {
        this.toCC = toCC;
    }

    public String getDescription() {
        return description;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public String getToBCC() {
        return toBCC;
    }

    public void setToBCC(String toBCC) {
        this.toBCC = toBCC;
    }
}
