package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataStatus;
import com.edatasite.workforce.gwt.core.client.enums.ShippingDataType;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPurchaseInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingDataSolrItem;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.common.SolrInputDocument;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 27.05.2009
 * Time: 17:28:27
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "shipping_data")
public class EdsShippingData extends EdsTraceable implements ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String number;
    private Integer intNumber;
    private String shippingLabel;
    private Date shippingDate;

    @Enumerated(EnumType.STRING)
    private ShippingDataType shippingType;

    @Enumerated(EnumType.STRING)
    private ShippingDataStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCurrency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quoteId")
    private EdsQuote quote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmAccountId")
    private EdsCrmAccount crmAccount;

    @Column(name = "createdDate")
    private Date creationTime;
    @Column(name = "modifiedDate")
    private Date lastUpdateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedBy")
    private EdsUser updater;

    @Column(columnDefinition = "boolean DEFAULT false ")
    private Boolean deleted;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY,
            mappedBy = "shippingData"
    )
    private List<EdsShippingDataItem> items = new ArrayList<>();

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getCreator() {
        return creator;
    }

    @Override
    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getCreationTime() {
        return this.creationTime != null ? new Date(this.creationTime.getTime()) : null;
    }

    @Override
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime != null ? new Date(creationTime.getTime()) : null;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    @Override
    public void setUpdater(EdsUser udater) {
        this.updater = udater;
    }

    public Date getLastUpdateTime() {
        return this.lastUpdateTime != null ? new Date(this.lastUpdateTime.getTime()) : null;
    }

    @Override
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime != null ? new Date(lastUpdateTime.getTime()) : null;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getShippingLabel() {
        return shippingLabel;
    }

    public void setShippingLabel(String shippingLabel) {
        this.shippingLabel = shippingLabel;
    }

    public Date getShippingDate() {
        return shippingDate != null ? new Date(this.shippingDate.getTime()) : null;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate != null ? new Date(shippingDate.getTime()) : null;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public EdsCrmAccount getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(EdsCrmAccount crmAccount) {
        this.crmAccount = crmAccount;
    }

    public EdsQuote getQuote() {
        return quote;
    }

    public void setQuote(EdsQuote quote) {
        this.quote = quote;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public ShippingDataType getShippingType() {
        return shippingType;
    }

    public void setShippingType(ShippingDataType shippingType) {
        this.shippingType = shippingType;
    }

    public List<EdsShippingDataItem> getItems() {
        return items;
    }

    public void setItems(List<EdsShippingDataItem> items) {
        this.items = items;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public boolean isDeleted() {
        return Optional.ofNullable(this.getDeleted()).orElse(false);
    }


    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ShippingDataStatus getStatus() {
        return status;
    }

    public void setStatus(ShippingDataStatus status) {
        this.status = status;
    }

    public ShippingData toTO() {
        final ShippingData item = new ShippingData();

        item.setId(this.getObjectID());
        item.setNumber(this.getNumber());
        item.setShippingLabel(this.getShippingLabel());
        item.setShippingType(this.getShippingType());
        item.setStatus(this.getStatus());
        if (this.getShippingDate() != null) {
            item.setShippingDate(new DateNonConvertable(ServerUtils.getDayStartTime(this.getShippingDate())));
        }
        if (this.getCrmAccount() != null) {
            item.setClientName(this.getCrmAccount().getName());
            item.setCustomer(getCrmAccount().getRPC(null, true));
        }
        if (this.getCurrency() != null) {
            item.setCurrencyName(this.getCurrency().getFullName());
        }
        if (this.getCreator() != null) {
            item.setCreatorName(this.getCreator().getName());
        }
        if (getQuote() != null) {
            item.setOrderNumber(getQuote().getNumber());
            item.setQuoteId(getQuote().getObjectID());
        }
        return item;
    }

    public SolrInputDocument wrapToSolrDocument(EdsShippingData shippingData, Integer companyID) {
        String compositID = companyID + "_" + shippingData.getObjectID();
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_ID, shippingData.getObjectID());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_NUMBER, shippingData.getNumber());
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATE, shippingData.getShippingDate());

        if (shippingData.getCrmAccount() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID, shippingData.getCrmAccount().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME, shippingData.getCrmAccount().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CLIENT_ID_NAME, shippingData.getCrmAccount().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + shippingData.getCrmAccount().getName());
        }

        if (shippingData.getCurrency() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID, shippingData.getCurrency().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_NAME, shippingData.getCurrency().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CURRENCY_ID_NAME, shippingData.getCurrency().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + shippingData.getCurrency().getName());
        }

        if (shippingData.getQuote() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_QUOTE_NUMBER, shippingData.getQuote().getNumber());
        }

        if (shippingData.getStatus() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_SHIPPING_DATA_STATUS_NAME, shippingData.getStatus().name());
        }

        if (shippingData.getCreator() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID, shippingData.getCreator().getObjectID());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_NAME, shippingData.getCreator().getName());
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATOR_ID_NAME, shippingData.getCreator().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + shippingData.getCreator().getName());
        }
        if (shippingData.getCreationTime() != null) {
            doc.addField(SolrSaleInvoiceRepresenter.FIELD_CREATION_DATE, shippingData.getCreationTime());
        }
        doc.addField(SolrSaleInvoiceRepresenter.FIELD_IS_GDN, ShippingDataType.OUT.equals(shippingData.getShippingType()));

        if (CollectionUtils.isNotEmpty(getItems())) {
            getItems().stream()
                    .filter(item -> item.getWarehouse() != null)
                    .forEach(item
                            -> doc.addField(SolrPurchaseInvoiceRepresenter.FIELD_WAREHOUSE_ID, item.getWarehouse() != null ? item.getWarehouse().getObjectID() : null));

        }
        return doc;
    }

    public ShippingDataSolrItem getSolrRPC() {
        ShippingDataSolrItem shippingDataSolrItem = new ShippingDataSolrItem();

        shippingDataSolrItem.setObjectId(getObjectID());
        shippingDataSolrItem.setShippingDataNumber(getNumber());
        shippingDataSolrItem.setShippingDate(getShippingDate());
        if (getCrmAccount() != null) {
            shippingDataSolrItem.setClient(getCrmAccount().getAsSelectItem());
        }
        if (getCurrency() != null) {
            shippingDataSolrItem.setCurrency(getCurrency().getAsSelectItem());
        }
        if (getQuote() != null) {
            shippingDataSolrItem.setQuoteNumber(getQuote().getNumber());
        }
        if (getStatus() != null) {
            shippingDataSolrItem.setShippingDataStatusName(getStatus().name());
        }
        if (getCreator() != null) {
            shippingDataSolrItem.setCreator(getCreator().getAsSelectItem());
            EdsLocation location = getCreator().getLocation();
            if (location != null) {
                shippingDataSolrItem.setCreatorLocationId(location.getObjectID());
            }
        }
        if (getCreationTime() != null) {
            shippingDataSolrItem.setCreationDate(getCreationTime());
        }
        shippingDataSolrItem.setGdn(ShippingDataType.OUT.equals(getShippingType()) ? true : false);

        if (getQuote() != null && getQuote().getObjectID() != null) {
            shippingDataSolrItem.setQuoteId(getQuote().getObjectID());
        }
        if (!getItems().isEmpty()) {
            getItems().stream()
                    .filter(item -> item.getWarehouse() != null)
                    .forEach(item -> {
                        if (item.getWarehouse() != null) {
                            shippingDataSolrItem.getWarehouseIds().add(item.getWarehouse().getObjectID());
                        }
                    });

        }
        return shippingDataSolrItem;
    }
}
