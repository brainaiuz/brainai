package com.edatasite.workforce.core.domain.emailfetching;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 6/30/11
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "corpEmailTracker")
public class EdsEmailTracker extends EdsObject {
    public static final int BIGGEST_COUNTER = 99999;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "prefix")
    private String prefix;

    @Column(name = "code")
    private String code;

    @Column(name = "counter")
    private Integer counter;

    @Column(name = "contactId")
    private Integer contactId;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getCode() {
        return code;
    }

    public Integer getCounter() {
        return counter;
    }

    public void setCounter(Integer counter) {
        this.counter = counter;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public void setCode(String code) {
        this.code = code;
        if (this.code != null && counter == null) {
            Integer counter = null;
            if (prefix != null && this.code.startsWith(prefix)) {
                String countr_ = this.code.substring(prefix.length());
                if (countr_ != null && !"".equals(countr_) && countr_.matches(Constants.REGEX_INTEGER)) {
                    counter = Integer.valueOf(countr_);
                }
            } else if (this.code.matches(Constants.REGEX_INTEGER)) {
                counter = Integer.valueOf(this.code);
            }
            if (counter != null) {
                setCounter(counter);
            }
        }
    }
}
