package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.Consignment;
import com.edatasite.workforce.gwt.accounting.client.rpc.consignment.ConsignmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Normurod on 6/15/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "consignment")
public class EdsConsignment extends EdsObject {

    public static final String UNIQ_NUM = "@CONS@C_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    @Column(name = "number")
    private String number;

    @Column(name = "intNumber")
    private Integer intNumber;

    private String reference;

    private Date date;

    private Date creationDate;
    private Date updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "consignmentid")
    private List<EdsConsignmentItem> items = new ArrayList<>();

    @Column(name = "subsidiaryUniqNum")
    private String subsidiaryUniqNum;

    private boolean deleted;

    private Boolean subsidiaryConignment = false;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public List<EdsConsignmentItem> getItems() {
        return items;
    }

    public void setItems(List<EdsConsignmentItem> items) {
        this.items = items;
    }

    public String getSubsidiaryUniqNum() {
        return subsidiaryUniqNum;
    }

    public void setSubsidiaryUniqNum(String subsidiaryUniqNum) {
        this.subsidiaryUniqNum = subsidiaryUniqNum;
    }

    public Boolean isSubsidiaryConignment() {
        return subsidiaryConignment != null ? subsidiaryConignment : Boolean.FALSE;
    }

    public void setSubsidiaryConignment(Boolean subsidiaryConignment) {
        this.subsidiaryConignment = subsidiaryConignment;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void addItem(EdsConsignmentItem item) {
        item.setConsignment(this);
        items.add(item);
    }
    public Consignment getRPC() {
        Consignment consignment = new Consignment();
        consignment.setObjectID(getObjectID());
        consignment.setName(getName());
        consignment.setNumber(getNumber());
        consignment.setDate(new DateNonConvertable(getDate()));
        consignment.setSubsidiaryUniqNum(getSubsidiaryUniqNum());
        consignment.setDeleted(isDeleted());
        consignment.setSubsidiaryConsignment(isSubsidiaryConignment());
        consignment.setReference(getReference());

        if (items != null && !items.isEmpty()) {
            List<ConsignmentItem> citems = new ArrayList<>();

            for (EdsConsignmentItem item : getItems()) {
                citems.add(item.getRPC());
            }
            consignment.setItems(citems.toArray(new ConsignmentItem[]{}));
        }
        return consignment;
    }
}
