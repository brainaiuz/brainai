package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Index;

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
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 27.03.2012
 * Time: 22:02:08
 * Software Team
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rolepermission")
public class EdsRolePermission extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "permissioncode")
    private String permissioncode;

    @Index(name = "rolepermission_rolecode_index")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rolecode", nullable = false, referencedColumnName = "code")
    private EdsRole role;
    @Column(insertable = false, updatable = false)
    private String rolecode;

    @Column(name = "access")
    private String priviledgeCode;

    public EdsRolePermission() {

    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getPermissioncode() {
        return permissioncode;
    }

    public void setPermissioncode(String permissioncode) {
        this.permissioncode = permissioncode;
    }

    public EdsRole getRole() {
        return role;
    }

    public void setRole(EdsRole role) {
        this.role = role;
    }

    public String getPriviledgeCode() {
        return priviledgeCode;
    }

    public void setPriviledgeCode(String priviledgeCode) {
        this.priviledgeCode = priviledgeCode;
    }
}
