package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Normurod on 3/23/2017.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "itemtable_settings")
public class EdsItemTableSettings extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Enumerated(EnumType.STRING)
    private ItemTableEnum section;

    @Column(name = "settingsJSONData")
    @Type(type = "text")
    private String settingsJSONData;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public ItemTableEnum getSection() {
        return section;
    }

    public void setSection(ItemTableEnum section) {
        this.section = section;
    }

    public String getSettingsJSONData() {
        return settingsJSONData;
    }

    public void setSettingsJSONData(String settingsJSONData) {
        this.settingsJSONData = settingsJSONData;
    }
}
