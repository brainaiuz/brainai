package com.edatasite.workforce.core.domain.rbac;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.Permission;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Oct 7, 2009
 * Time: 3:09:16 PM
 * Package name abbreviation for Role Based Entity Access Control Index
 * To change this template use File | Settings | File Templates.
 */

/**
 * Role Based Access Control for EdsProject
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "projectindexrbac", uniqueConstraints = @UniqueConstraint(columnNames = {"projectid", "userid"/*, "companyid"*/}))
public class EdsProjectIndexRbac extends EdsObject implements Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @ManyToOne
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @Column
    private int permission;

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }
}
