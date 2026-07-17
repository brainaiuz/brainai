package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * User : Akhror
 * Date : 11.03.2024
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payroll_zone")
public class EdsPayrollZone extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            schema = EdsScope.PRIVATE_SCHEMA, name = "payroll_zone_location",
            joinColumns = @JoinColumn(name = "zone_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    private Set<EdsLocation> locations;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<EdsLocation> getLocations() {
        return locations;
    }

    public void setLocations(Set<EdsLocation> locations) {
        this.locations = locations;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public SelectItem getAsSelectItem() {
        SelectItem item = new SelectItem(getObjectID(), getName());
        if (getLocations() != null) {
            item.setRelatedItems(getLocations().stream().map(EdsLocation::getAsSelectItem).toList().toArray(new SelectItem[]{}));
        }
        return item;
    }
}
