package com.edatasite.workforce.core.domain.googlemarketplace;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Anvarbek
 * Date: May 6, 2010
 * Time: 9:40:23 PM
 */

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "openIdConsumerAssociationStore")
public class EdsOpenIDConsumerAssociationStore {

    private String opurl;

//    @Column(unique = true)
    @Id
    private String handle;//primary key

    private String type;
    private String mackey;
    private Date expdate;

    public String getOpurl() {
        return opurl;
    }

    public void setOpurl(String opurl) {
        this.opurl = opurl;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMackey() {
        return mackey;
    }

    public void setMackey(String mackey) {
        this.mackey = mackey;
    }

    public Date getExpdate() {
        return expdate;
    }

    public void setExpdate(Date expdate) {
        this.expdate = expdate;
    }
}
