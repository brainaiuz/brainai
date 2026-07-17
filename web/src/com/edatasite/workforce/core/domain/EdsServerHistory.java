package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 24.03.11
 * Time: 20:21
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "serverHistory")
public class EdsServerHistory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name="downTimeFrom")
    private Date downTimeFrom;

    @Column(name="downTimeTo")
    private Date downTimeTo;

    @Column(name="catchUp")
    private Boolean catchUp = false;

    @Override
    public Integer getObjectID() {
        return null;
    }

    public Date getDownTimeFrom() {
        return downTimeFrom;
    }

    public void setDownTimeFrom(Date downTimeFrom) {
        this.downTimeFrom = downTimeFrom;
    }

    public Date getDownTimeTo() {
        return downTimeTo;
    }

    public void setDownTimeTo(Date downTimeTo) {
        this.downTimeTo = downTimeTo;
    }

    public Boolean getCatchUp() {
        return catchUp;
    }

    public void setCatchUp(Boolean catchUp) {
        this.catchUp = catchUp;
    }
}
