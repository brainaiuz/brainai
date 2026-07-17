package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Feb 28, 2011
 * Time: 3:33:00 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "item_stock")
public class EdsItemStock extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private EdsItem item;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "from_stock_id")
    private Integer from_stock_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouseid")
    private EdsWarehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionid")
    private EdsTransaction transaction;

    @Column(name = "transaction_code")
    private String tranCode;

    @Column(precision = 25, scale = 5)
    private BigDecimal quantity = new BigDecimal(1);

    @Column(precision = 25, scale = 10)
    private BigDecimal price = new BigDecimal(0);

    @Column(name = "date")
    private Date date;

    @Column(name = "transaction_date")
    private Date tranDate;

    @Column(name = "transaction_value", precision = 25, scale = 5)
    private BigDecimal tranValue;

    @Column(name = "sorder")
    private Integer order = 0;

    @Column(name = "product_identifier")
    private Integer productIdentifier = 1;

    @Column(name = "transaction_item_id")
    private Integer transactionItemId; //it could be invoice item id, adjustment item id etc.


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(EdsWarehouse warehouse) {
        this.warehouse = warehouse;
    }

    public EdsTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(EdsTransaction transaction) {
        this.transaction = transaction;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getRealQuantity() {
        return "OUT".equals(getTranCode()) ? getQuantity().multiply(new BigDecimal(-1)) : getQuantity();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTranCode() {
        return tranCode;
    }

    public void setTranCode(String tranCode) {
        this.tranCode = tranCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        this.date = cal.getTime();
    }

    public EdsItem getItem() {
        return item;
    }

//    public void setItem(EdsItem item) {
//        this.item = item;
//    }

    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
    }

    public BigDecimal getTranValue() {
        return tranValue;
    }

    public void setTranValue(BigDecimal tranValue) {
        this.tranValue = tranValue;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getProductIdentifier() {
        return productIdentifier;
    }

    public void setProductIdentifier(Integer productIdentifier) {
        this.productIdentifier = productIdentifier;
    }

    public Integer getTransactionItemId() {
        return transactionItemId;
    }

    public void setTransactionItemId(Integer transactionItemId) {
        this.transactionItemId = transactionItemId;
    }

    public static StockItem toDto(EdsItemStock stock) {
        return new StockItem(stock.getObjectID(), stock.getWarehouse() != null ? stock.getWarehouse().getObjectID() : null, stock.getDate(), stock.getQuantity(), stock.getPrice(), stock.getOrder());
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getFrom_stock_id() {
        return from_stock_id;
    }

    public void setFrom_stock_id(Integer from_stock_id) {
        this.from_stock_id = from_stock_id;
    }
}
