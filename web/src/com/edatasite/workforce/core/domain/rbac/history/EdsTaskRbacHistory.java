package com.edatasite.workforce.core.domain.rbac.history;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Abdulaziz
 * Date: Feb 9, 2010
 * Time: 4:01:13 PM
 * Keeps history of Access Control Entries
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "taskrbachistory")
public class EdsTaskRbacHistory extends EdsBaseTaskRbac {

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
