package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User: Ilhombek
 * Date: 09.11.2010
 * Time: 15:53:29
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "companyEmailNotificationSettings")
public class EdsCompanyEmailNotificationSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "notificationName")
    private String notificationName;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
    private String category;

    @Column(name = "isEnabled", columnDefinition = "boolean DEFAULT false")
    private boolean isEnabled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleGroupId")
    private EdsGroup roleGroup;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public EdsGroup getRoleGroup() {
        return roleGroup;
    }

    public void setRoleGroup(EdsGroup roleGroup) {
        this.roleGroup = roleGroup;
    }

    public boolean isForClient() {
        return this.roleGroup != null && EdsGroup.CLIENTS.equals(this.roleGroup.getConstantName()) && this.roleGroup.getEntryType() == 2;
    }
}
