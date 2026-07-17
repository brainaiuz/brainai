package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.RoleListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Lochin
 * Date: 27.03.2001
 * Time: 22:02:08
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "role")
public class EdsRole extends EdsObject {

    @Serial
    private static final long serialVersionUID = -5692421372260586533L;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // DEFAULT - is used when view as parameter is not specified, and the user can view what he is allowed to view.
    public static final Integer DEFAULT = 0;
    public static final Integer DR = 1;
    public static final String DR_CODE = Constants.DR_CODE;//Director
    public static final Integer TL = 2;
    public static final String TL_CODE = Constants.TL_CODE;//Department leader
    public static final Integer PM = 3;
    public static final String PM_CODE = Constants.PM_CODE;//Project manager
    public static final Integer HR = 4;
    public static final String HR_CODE = Constants.HR_CODE;//Hr manager
    public static final Integer ADMIN = 5;
    public static final String ADMIN_CODE = Constants.ADMIN_CODE;//Admin
    public static final Integer MEM = 6;
    public static final String MEM_CODE = Constants.MEM_CODE;//Member
    public static final Integer CLIENT = 7;
    public static final String CLIENT_CODE = Constants.CLIENT_CODE;//Client
    public static final Integer ACCOUNTANT = 9;
    public static final String ACCOUNTANT_CODE = Constants.ACCOUNTANT_CODE;//Accountant
    public static final Integer SALESMAN = 10;//Salesman
    public static final String SALESMAN_CODE = Constants.SALESMAN_CODE;//Salesman
    public static final Integer CUSTOMER_SERVICE_REPRESENTATIVE = 11;//Customer Service Representative
    public static final String CUSTOMER_SERVICE_REPRESENTATIVE_CODE = Constants.CUSTOMER_SERVICE_REPRESENTATIVE_CODE;//Customer Service Representative
    //    public static final Integer ONE_OFF = 8;
    public static final Integer SALESPERSON = 12;
    public static final String SALESPERSON_CODE = Constants.SALESPERSON_CODE;//Salesperson
    public static final Integer ADMIN_LOCATION = 13;
    public static final String ADMIN_LOCATION_CODE = Constants.ADMIN_LOCATION_CODE;//Admin location
    public static final Integer CALENDAR_EDITOR = 14;
    public static final String CALENDAR_EDITOR_CODE = Constants.CALENDAR_EDITOR_CODE;//Calendar editor
    public static final Integer CALENDAR_VIEWER = 15;
    public static final String CALENDAR_VIEWER_CODE = Constants.CALENDAR_VIEWER_CODE;//Calendar viewer
    public static final Integer CHAT_EXPERT = 16;
    public static final String CHAT_EXPERT_CODE = Constants.CHAT_EXPERT_CODE;//Chat expert
    public static final Integer TIMESHEET_EDITOR = 17;
    public static final String TIMESHEET_EDITOR_CODE = Constants.TIMESHEET_EDITOR_CODE;//TimeSheet editor
    public static final Integer GUEST = 18;
    public static final String GUEST_CODE = Constants.GUEST_CODE;//Guest
    public static final Integer CUSTOM_MEMBER = 19;
    public static final String CUSTOM_MEMBER_CODE = Constants.CUSTOM_MEMBER_CODE;//Custom member
    public static final String CREATOR_CODE = Constants.CREATOR;//Custom member

    public static final Integer PROJECTS_DIRECTOR = 24;
    public static final String PROJECTS_DIRECTOR_CODE = Constants.PROJECTS_DIRECTOR_CODE;//Project director
    public static final Integer AUDITOR = 25;
    public static final String AUDITOR_CODE = Constants.AUDITOR_CODE;//Auditor
    public static final Integer CUSTOMER_SERVICE_MANAGER = 26;
    public static final String CUSTOMER_SERVICE_MANAGER_CODE = Constants.CUSTOMER_SERVICE_MANAGER_CODE;//Customer service manager
    public static final Integer SUPPLIER = 27;
    public static final String SUPPLIER_CODE = Constants.SUPPLIER;
    public static final Integer INSTRUCTOR = 28;
    public static final String INSTRUCTOR_CODE = Constants.INSTRUCTOR_CODE;//Instructor
    public static final String SUPERVISOR_CODE = Constants.SUPERVISOR_CODE;//Supervisor

    @Deprecated
    public static final Integer ESS_USER = 29;
    public static final String ESS_USER_CODE = Constants.ESS_USER_CODE;//Admin

    public static final String PARTNER_ADMIN_CODE = Constants.PARTNER_ADMIN_CODE;//Partner Admin

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    @Column(name = "isSystem")
    private Boolean isSystem = false;
    @Column(name = "isDeleted", columnDefinition = "boolean default false")
    private Boolean isDeleted = false;
    @Column(name = "active", columnDefinition = "boolean default true")
    private boolean active = true;
    private Boolean isEntitySpecific = false;
    private Integer sorder;

    @Column(name = "module_code")
    private String moduleCode;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "role")
    private List<EdsRolePermission> rolePermissions = new ArrayList<>();

    public EdsRole() {
    }

    public EdsRole(String name) {
        this.name = name;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<EdsRolePermission> getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(List<EdsRolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    public Boolean getSystem() {
        return isSystem;
    }

    public void setSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public RoleListItem getRPC(RoleListItem item) {
        item = item != null ? item : new RoleListItem();
        item.setName(getName());
        item.setObjectID(getObjectID());
        item.setActive(isActive());
        item.setCode(getCode());
        item.setSystem(getSystem());
        item.setDescription(getDescription());
        return item;
    }
}
