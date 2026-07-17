package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/8/12
 * Time: 5:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "productserial")
public class EdsProductSerial extends EdsObject{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "orderitemid")
    private Integer orderItemID;

    @Column(name = "invoiceitemid")
    private Integer invoiceItemID;

    @Column(name = "itemId")
    private Integer itemID;

    @Column(name = "gdnid")
    private Integer gdnid;

    @Column(name = "grnid")
    private Integer grnid;

    @Column(name = "serial")
    private String serial;

    @Column(name = "lotNumber")
    private String lotNumber;

    @Column(name = "refNumber")
    private String refNumber;

    @Column(name = "expirationdate")
    private Date expirationDate;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(Integer orderItemID) {
        this.orderItemID = orderItemID;
    }

    public Integer getInvoiceItemID() {
        return invoiceItemID;
    }

    public void setInvoiceItemID(Integer invoiceItemID) {
        this.invoiceItemID = invoiceItemID;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public Integer getGdnid() {
        return gdnid;
    }

    public void setGdnid(Integer gdnid) {
        this.gdnid = gdnid;
    }

    public Integer getGrnid() {
        return grnid;
    }

    public void setGrnid(Integer grnid) {
        this.grnid = grnid;
    }

    public ProductSerialItem getAsRPC() {
        ProductSerialItem serialItem = new ProductSerialItem();
        serialItem.setObjectID(getObjectID());
        serialItem.setSerial(getSerial() + " (" + getExpirationDate() + ")");
        serialItem.setExpirationDate(getExpirationDate());
        serialItem.setLotNumber(getLotNumber());
        serialItem.setRefNumber(getRefNumber());
        return serialItem;
    }
}
