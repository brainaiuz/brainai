package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/5/12
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "productlocation")
public class EdsProductLocation extends EdsObject{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String aisle;
    private String rack;
    private String shelf;
    private String bin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aisleId")
    private EdsReference aisleReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rackId")
    private EdsReference rackReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelfId")
    private EdsReference shelfReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseid")
    private EdsWarehouse warehouse;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public EdsWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(EdsWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public EdsReference getAisleReference() {
        return aisleReference;
    }

    public void setAisleReference(EdsReference aisleReference) {
        this.aisleReference = aisleReference;
    }

    public EdsReference getRackReference() {
        return rackReference;
    }

    public void setRackReference(EdsReference rackReference) {
        this.rackReference = rackReference;
    }

    public EdsReference getShelfReference() {
        return shelfReference;
    }

    public void setShelfReference(EdsReference shelfReference) {
        this.shelfReference = shelfReference;
    }

    @Override
    public SelectItem getAsSelectItem() {
        return new SelectItem(getObjectID(),
                    ((getAisle()!=null && !"".equals(getAisle().trim())) ? ("Aisle : " + getAisle() + "; ") : "") +
                    ((getRack()!=null && !"".equals(getRack().trim())) ? ("Rack : " + getRack() + "; ") : "") +
                    ((getShelf()!=null && !"".equals(getShelf().trim())) ? ("Shelf : " + getShelf() + "; ") : "") +
                    ((getBin()!=null && !"".equals(getBin().trim())) ? ("Bin : " + getBin()) : ""));
    }
}
