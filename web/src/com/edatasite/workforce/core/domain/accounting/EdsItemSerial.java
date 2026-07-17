package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "item_serial")
public class EdsItemSerial extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "serial")
    private String serial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private EdsItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private EdsWarehouse warehouse;

    @Column(name = "used", columnDefinition = "boolean default false")
    private Boolean used = false;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "serial_id")
    private Set<EdsItemSerialDetail> serialDetails = new HashSet<>();

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public EdsItem getItem() {
        return item;
    }

    public void setItem(EdsItem item) {
        this.item = item;
    }

    public EdsWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(EdsWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Set<EdsItemSerialDetail> getSerialDetails() {
        return serialDetails;
    }

    @Override
    public SelectItem getAsSelectItem() {
        return new SelectItem(getObjectID(), getSerial());
    }
}
