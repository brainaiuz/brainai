package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "hrsettings")
public class EdsHRSettings extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name="openingBalanceDate")
    private Date openingBalanceDate;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public Date getOpeningBalanceDate() {
        return openingBalanceDate;
    }

    public void setOpeningBalanceDate(Date openingBalanceDate) {
        this.openingBalanceDate = openingBalanceDate;
    }
}
