package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsReservation;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 16, 2010
 * Time: 5:20:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "shippingmethod")
public class EdsShippingMethod extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", precision = 14, scale = 4)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vatid")
    private EdsVat vat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "ShippingMethodRelation",
            joinColumns = {@JoinColumn(name = "shipping_method_id")},
            inverseJoinColumns = {@JoinColumn(name = "customer_id")}
    )
    @Where(clause = " deleted = 'false' ")
    private List<EdsCrmAccount> customers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shippingMethod")
//    @Where(clause = " deleted = 'false' ")
    private List<EdsSaleQuote> quotes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shippingMethod")
    @Where(clause = " deleted = 'false' ")
    private List<EdsReservation> reservations = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shippingMethod")
//    @Where(clause = " deleted = 'false' ")
    private List<EdsSaleInvoice> invoices = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private EdsAccount account;

    @Column(columnDefinition = "boolean default false")
    private Boolean deleted = false;

    public List<EdsSaleQuote> getQuotes() {
        return quotes;
    }

    public List<EdsReservation> getReservations() {
        return reservations;
    }

    public List<EdsSaleInvoice> getInvoices() {
        return invoices;
    }

    public EdsShippingMethod() {
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public EdsVat getVat() {
        return vat;
    }

    public void setVat(EdsVat vat) {
        this.vat = vat;
    }

    public List<EdsCrmAccount> getCustomers() {
        return customers;
    }

    public void setCustomers(List<EdsCrmAccount> customers) {
        this.customers = customers;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ShippingMethod getRPC() {
        ShippingMethod item = new ShippingMethod();
        item.setId(getObjectID());
        item.setName(getName());
        item.setPrice(getPrice());
        item.setDescription(getDescription());
        item.setExchangeRate(getExchangeRate());
        item.setCurrencyId(getCurrency() != null ? getCurrency().getObjectID() : null);

        if (getVat() != null) {
            item.setTaxItem(getVat().createTaxItem());
        }
        if (getAccount() != null) {
            item.setAccount(getAccount().getAsSelectItem());
        }
        return item;
    }

    public ShippingMethod toTO() {
        ShippingMethod to = new ShippingMethod();
        to.setId(getObjectID());
        to.setName(getName());
        to.setDescription(getDescription());
        to.setPrice(getPrice());
        return to;
    }
}
