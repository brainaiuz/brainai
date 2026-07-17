package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.HasEdsObjectPermission;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.PriceLevelOperationTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 24, 2011
 * Time: 5:18:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "price_level")
public class EdsPriceLevel extends HasEdsObjectPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    private Integer type;

    @Column(name = "price_level_case")
    private Integer plCase;

    private Double percent;

    @Enumerated(EnumType.STRING)
    private PriceLevelOperationTypeEnum operationType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "priceLevel")
    private List<EdsPriceLevelPP> priceLevelPPs = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "priceLevel")
    private List<EdsPriceLevelBB> priceLevelBBs = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "client_price_level",
            joinColumns = {@JoinColumn(name = "price_level_id")},
            inverseJoinColumns = {@JoinColumn(name = "client_id")})
    private List<EdsCrmAccount> clients = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "client_type_price_level",
            joinColumns = {@JoinColumn(name = "price_level_id")},
            inverseJoinColumns = {@JoinColumn(name = "client_type_id")})
    private List<EdsReference> clientTypes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    private Boolean deleted = false;

    @Column(name = "quickbook_pl_id")
    private String quickbookPriceLevelID;

    @Column(name = "quickbook_edit_sequence")
    private String quickbookEditSequence;

    @Column(name = "external_guid")
    private String externalGUID;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPLCase() {
        return plCase;
    }

    public void setPLCase(Integer plCase) {
        this.plCase = plCase;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public List<EdsCrmAccount> getClients() {
        return clients;
    }

    public void setClients(List<EdsCrmAccount> clients) {
        this.clients = clients;
    }

    public List<EdsReference> getClientTypes() {
        return clientTypes;
    }

    public void setClientTypes(List<EdsReference> clientTypes) {
        this.clientTypes = clientTypes;
    }

    public List<EdsPriceLevelPP> getPriceLevelPPs() {
        return priceLevelPPs;
    }

    public void setPriceLevelPPs(List<EdsPriceLevelPP> priceLevelPPs) {
        this.priceLevelPPs = priceLevelPPs;
    }

    public List<EdsPriceLevelBB> getPriceLevelBBs() {
        return priceLevelBBs;
    }

    public void setPriceLevelBBs(List<EdsPriceLevelBB> priceLevelBBs) {
        this.priceLevelBBs = priceLevelBBs;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public Boolean isDeleted() {
        return deleted != null ? deleted : false;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getQuickbookPriceLevelID() {
        return quickbookPriceLevelID;
    }

    public void setQuickbookPriceLevelID(String quickbookPriceLevelID) {
        this.quickbookPriceLevelID = quickbookPriceLevelID;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    public PriceLevelOperationTypeEnum getOperationType() {
        return operationType;
    }

    public void setOperationType(PriceLevelOperationTypeEnum operationType) {
        this.operationType = operationType;
    }
    
    public PriceLevelItem getRPC(){
        PriceLevelItem item = new PriceLevelItem();
        item.setId(getObjectID());
        item.setName(getName());
        item.setType(getType());
        item.setPLCase(getPLCase());
        item.setPercent(getPercent());
        item.setCurrency(getCurrency() != null ? getCurrency().createCurrencyItem() : null);
        return item;
    }
}
