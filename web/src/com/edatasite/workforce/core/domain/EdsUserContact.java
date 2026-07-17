package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.lucene.Indexable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 23.10.2008
 * Time: 12:17:06
 * To change this template use File | Settings | File Templates.
 */
@Entity
//@Indexed
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "userContact")

/**
 * Current class provides storing one off users of company.
 *
 */
public class EdsUserContact extends EdsUser implements Indexable {

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="companyid")
    private EdsCompany company;

    
    public EdsCompany getCompany() {
        return company;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setCompany(EdsCompany company) {
        this.company = company;
    }*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    public EdsUser getCreator() {
        return creator;
    }
    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }
}
