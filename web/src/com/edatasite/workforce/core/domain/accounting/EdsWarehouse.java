package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsWarehouseCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 14, 2010
 * Time: 2:39:32 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "Warehouse")
public class EdsWarehouse extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id")
    private Integer objectID;
    @Column(name = "name") //warehouse name
    private String name;
    @Column(name = "notes") //description
    @Type(type = "text")
    private String notes;
    @Column(name = "isDefaultWarehouse")
    private Boolean defaultWarehouse;
    @Column(name = "primaryContactID")
    private Integer primaryContactID;

    @OneToOne
    @JoinColumn(name = "customfieldsid")
    private EdsWarehouseCustomFields customFields;

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "warehouse_owners",
            joinColumns = {@JoinColumn(name = "warehouse_id")},
            inverseJoinColumns = {@JoinColumn(name = "owner_id")})
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsUser> owners = new ArrayList<>();

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

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Boolean isDefaultWarehouse() {
        return defaultWarehouse != null ? defaultWarehouse : false;
    }
    public void setDefaultWarehouse(Boolean defaultWarehouse) {
        this.defaultWarehouse = defaultWarehouse;
    }

    public List<EdsUser> getOwners() {
        return owners;
    }
    public void setOwners(List<EdsUser> owners) {
        this.owners = owners;
    }
    public Integer getPrimaryContactID() {
        return primaryContactID;
    }
    public void setPrimaryContactID(Integer primaryContactID) {
        this.primaryContactID = primaryContactID;
    }

    public EdsWarehouseCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsWarehouseCustomFields customFields) {
        this.customFields = customFields;
    }


//    public void setAssigneeID(String assigneeID) {
//        this.assigneeID = assigneeID;
//    }
//    public String getAssigneeID() {
//        return assigneeID;
//    }

    public HashMap<Integer, EdsUser> getOwnersMap() {
        HashMap<Integer, EdsUser> ownersMap = new HashMap<>();
        if (getOwners() == null || getOwners().isEmpty()) {
            return ownersMap;
        }
        getOwners().forEach(owner -> ownersMap.put(owner.getObjectID(), owner));
        return ownersMap;
    }

    public WarehouseItem getRPC() {
        WarehouseItem warehouse = new WarehouseItem();
        warehouse.setObjectID(getObjectID());
        warehouse.setName(getName());
        warehouse.setNotes(getNotes());

       // warehouse.setContactname(getContactname());
       // warehouse.setPhone(getPhone());
      //  warehouse.setEmail(getEmail());
      //  warehouse.setAddress(getAddress1());

        warehouse.setPrimaryContactID(getPrimaryContactID());
//      warehouse.setAssignee(getAssigneeID());

        ArrayList<SelectItem> owners= (ArrayList<SelectItem>) getOwners().stream().map(ow -> new SelectItem(ow.getObjectID(),ow.getFullName(),null,true)).collect(Collectors.toList());
        warehouse.setSelectedOwners(owners);

        return warehouse;
    }
}
