package com.edatasite.workforce.core.domain.rbac;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Abdulaziz
 * Date: Oct 20, 2009
 * Time: 6:51:07 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "trusteegroup", uniqueConstraints = @UniqueConstraint(columnNames = {"constantName"}))
public class EdsGroup extends EdsObject implements Constants {
    // System built-in groups this names will be sent on constant name
    public static final String DIRECTORS = "DIRECTORS";
    public static final String ADMINISTRATORS = "ADMINISTRATORS";
    public static final String PROJECT_MANAGERS = "PROJECT_MANAGERS";
    public static final String DEPARTMENT_LEADERS = "DEPARTMENT_LEADERS";
    public static final String MEMBERS = "MEMBERS";
    public static final String HRS = "HRS";
    public static final String CLIENTS = "CLIENTS";
    public static final String ACCOUNTANTS = "ACCOUNTANTS";
    public static final String SALESMEN = "SALESMEN";
    public static final String CUSTOMER_SERVICE_REPRESENTATIVES = "CUSTOMER_SERVICE_REPRESENTATIVES";
    public static final String CUSTOMER_SERVICE_MANAGER = "CUSTOMER_SERVICE_MANAGER";
//    public static final String ONE_OFFS = "ONE_OFFS";
    public static final String SALESPERSONS = "SALESPERSONS";
    public static final String ADMIN_LOCATIONS = "ADMIN_LOCATIONS";
    public static final String CALENDAR_EDITORS = "CALENDAR_EDITORS";
    public static final String CALENDAR_VIEWERS = "CALENDAR_VIEWERS";
    public static final String TIMESHEET_EDITORS = "TIMESHEET_EDITORS";
    public static final String CHAT_EXPERT = "CHAT-EXPERTS";
    public static final String GUEST = "GUEST";
    public static final String CUSTOM_MEMBER = "CUSTOM_MEMBER";
    public static final String PROJECTS_DIRECTOR = "PROJECTS_DIRECTOR";
    public static final String AUDITOR = "AUDITOR";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private int entryType = EdsObject.BUILT_IN;//0 builtin, 1 custom, 2 inherited

    // There is relation to trustees who are members of this goroup
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "trusteegroup_trustee",
            joinColumns = {@JoinColumn(name = "trusteegroup_id")},
            inverseJoinColumns = {@JoinColumn(name = "members_id")}
    )
    private Set<EdsTrustee> members = new HashSet<>();

    @Column
    private String constantName;

    @Column
    private String name;//Adminstrators, Direcotors, HRs.....

    @Column
    private String description;

    /**
     * The user that owns this group.
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private EdsTrustee owner;

    @Column
    private String type = IS_EMPLOYEE;//IS_EMPLOYEE, IS_CLIENT ....

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

    public int getEntryType() {
        return entryType;
    }

    public void setEntryType(int entryType) {
        this.entryType = entryType;
    }

//    public EdsCompany getCompany() {
//        return company;
//    }
//
//    public void setCompany(EdsCompany company) {
//        this.company = company;
//    }

    public Set<EdsTrustee> getMembers() {
        return members;
    }

    public void setMembers(Set<EdsTrustee> members) {
        this.members = members;
    }

    public String getConstantName() {
        return constantName;
    }

    public void setConstantName(String constantName) {
        this.constantName = constantName;
    }

    public EdsTrustee getOwner() {
        return owner;
    }

    public void setOwner(EdsTrustee owner) {
        this.owner = owner;
    }

    public String getType() {
        if (type == null) {
            type = IS_EMPLOYEE;
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns a Data Transfer Object for this Group
     *
     * @return GroupDTO
     */
    public GroupMembersViewItem getDTO() {
        final GroupMembersViewItem g = new GroupMembersViewItem();
        g.setGroupID(getObjectID());
        g.setGroupConstantName(getConstantName());
        g.setGroupName(getName());
        g.setGroupDescription(getDescription());
        g.setType(getType());
        ArrayList<GroupMemberItem> memberList = new ArrayList<>();
        for (final EdsTrustee member : members) {
            GroupMemberItem mItem = new GroupMemberItem();
            mItem.setTrusteeID(member.getTrusteeID());
            mItem.setTrusteeType(member.getType().getObjectID());
            mItem.setTrusteeDescription(member.getType().getDescription());
            mItem.setTrusteeName(getName());
            memberList.add(mItem);
        }
        g.setMembers(memberList.toArray(new GroupMemberItem[]{}));
        return g;
    }

    /**
     * Checks if this groups contains the specified user
     *
     * @param user
     * @return boolean
     */
    public boolean contains(final EdsUser user) {
        for (final EdsTrustee member : members) {
            if (member.getTrusteeID().equals(user.getObjectID())) {
                return true;
            }
        }
        return false;
    }
}
