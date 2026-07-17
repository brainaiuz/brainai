package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.MappedSuperclass;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public abstract class HasEdsObjectPermission extends EdsObject {
    @Column(name = "denied", columnDefinition = "boolean default false")
    private boolean denied = false;

    private Integer objectCreatorId;

    @Column(name = "uniqueId")
    private String uniqueId;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "objectpermission_roles",
            joinColumns = {@JoinColumn(name = "objectpermission_id",referencedColumnName="uniqueId")},
            inverseJoinColumns = {@JoinColumn(name = "roles_id")}
    )
    private Set<EdsRole> roles = new HashSet<>();

    public boolean isDenied() {
        return denied;
    }

    public void setDenied(boolean denied) {
        this.denied = denied;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Set<EdsRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<EdsRole> roles) {
        this.roles = roles;
    }

    public Integer getObjectCreatorId() {
        return objectCreatorId;
    }

    public void setObjectCreatorId(Integer object_creator_id) {
        this.objectCreatorId = object_creator_id;
    }

    public ArrayList<SelectItem> getRolesAsDTO() {
        ArrayList<SelectItem> result = new ArrayList<>();
        if(getRoles() != null && getRoles().size() > 0){
            for(EdsRole role : getRoles()){
                result.add(role.getAsSelectItem());
            }
        }
        return result;
    }
}
