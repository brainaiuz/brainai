package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 10/22/11
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "accountNumberSettings")
public class EdsAccountNumberSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    @Column(name = "startNumber")
    private Integer start; //chart of account is start with the number

    @Column(name = "endNumber")
    private Integer end; //chart of account is end with the number

    @ManyToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "none")
    private EdsAccountType accountType;

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public EdsAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(EdsAccountType accountType) {
        this.accountType = accountType;
    }
}
