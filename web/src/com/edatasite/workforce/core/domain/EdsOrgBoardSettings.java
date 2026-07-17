package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.team.client.services.dto.OrgBoardSettingsItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "org_board_settings")
public class EdsOrgBoardSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @Column(name = "show_employees")
    private Boolean showEmployees;
    @Column(name = "show_short_description")
    private Boolean showShortDescription;
    @Column(name = "show_description")
    private Boolean showDescription;
    @Column(name = "show_goals")
    private Boolean showGoals;
    @Column(name = "employeeId")
    private Integer employeeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationId")
    private EdsLocation location;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getShowEmployees() {
        return showEmployees;
    }

    public void setShowEmployees(Boolean showEmployees) {
        this.showEmployees = showEmployees;
    }

    public Boolean getShowShortDescription() {
        return showShortDescription;
    }

    public void setShowShortDescription(Boolean showShortDescription) {
        this.showShortDescription = showShortDescription;
    }

    public Boolean getShowDescription() {
        return showDescription;
    }

    public void setShowDescription(Boolean showDescription) {
        this.showDescription = showDescription;
    }

    public Boolean getShowGoals() {
        return showGoals;
    }

    public void setShowGoals(Boolean showGoals) {
        this.showGoals = showGoals;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public OrgBoardSettingsItem getAsDTO() {
        OrgBoardSettingsItem dto = new OrgBoardSettingsItem();
        dto.setObjectId(getObjectID());
        dto.setShowEmployees(getShowEmployees());
        dto.setShowGoals(getShowGoals());
        dto.setShowDescription(getShowDescription());
        dto.setEmployeeId(getEmployeeId());
        dto.setShowShortDescription(getShowShortDescription());
        if (getLocation()!=null) {
            SelectItem location = getLocation().getAsSelectItem();
            dto.setLocationId(location.getId());
            dto.setLocation(location);
        }
        return dto;
    }
}
