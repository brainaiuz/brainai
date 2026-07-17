package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.shared.db.ObjectIdentifier;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.core.solr.document.ProductsServicesSolrDoc;
import com.edatasite.workforce.gwt.accounting.client.rpc.*;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.ProductKitCostAllocationType;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProductServiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.HistoryLogManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.ui.view.AccountingCustomFormConstants;
import org.apache.commons.collections.CollectionUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@DynamicUpdate
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "item")
public class EdsItem extends EdsTraceable implements AccountingConstants, Constants, ObjectIdentifier {


    public static final String PRODUCT_TYPE = "_PRODUCT_TYPE";
    public static final String PRODUCT_UNIQ_NUM = "@PRO@C_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "objectKey", unique = true, updatable = false)
    private String objectKey;

    public EdsItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    @Override
    public String getObjectKey() {
        return objectKey;
    }

    @Override
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Column(name = "product_number")
    private String productNumber;

    @Column(name = "intNumber")
    private Integer intNumber;

    @Column(name = "name", length = 1000)
    @Type(type = "text")
    private String name;

    @Type(type = "text")
    @Column(name = "description")
    private String description;

    @Column(name = "unitPrice", precision = 24, scale = 4)
    private BigDecimal unitPrice;

    @Column(name = "unitCost", precision = 11, scale = 2)
    private BigDecimal unitCost;

    /*@Column(name = "qty", precision = 11, scale = 2)
    private BigDecimal qty = new BigDecimal(1);*/

    @Column(name = "net", precision = 11, scale = 2)
    private BigDecimal net;

    @Column(name = "trackInventory")
    private Boolean trackInventory;

    @Column(name = "inventory", precision = 11, scale = 2)
    private BigDecimal inventory;

    @Column(name = "rentalItem")
    private Boolean rentalItem;

    @Column(name = "rentalPeriod")
    private Integer rentalPeriod;

    @Column(name = "rentalRate", precision = 14, scale = 4)
    private BigDecimal rentalRate;

    @Column(name = "overdueRate", precision = 14, scale = 4)
    private BigDecimal overdueRate;

    @Column(name = "cancelationPeriod")
    private Integer cancelationPeriod;

    @Column(name = "cancelationPeriodType")
    private Integer cancelationPeriodType;

    @Column(name = "cancelationFee", precision = 14, scale = 4)
    private BigDecimal cancelationFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vatid")
    private EdsVat vat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "double_vat_id")
    private EdsVat doubleVat;

    private Integer type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsAccount account;  //(Income Account)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cogs_accountid")
    private EdsAccount cogsAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_accountid")
    private EdsAccount assetAccount;

    //For Purchase Item Only
    @Column(name = "useditems")
    private BigDecimal usedItems;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryid")
    private EdsProductCategory category;

    @Column(name = "skunumber")
    private String internalSKUNumber;

    @Type(type = "text")
    @Column(name = "manufacturer")
    private String manufacturer;

    @Type(type = "text")
    @Column(name = "part_number")
    private String partNumber;

    @Column(name = "barcode")
    private String barCode;

    @Column(name = "qrcodesizeid")
    private Integer QRCodeSizeID;

    @Column(name = "upcnumber")
    private String upcNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unitmeasurementid")
    private EdsUnitMeasurement unitMeasurement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendorid")
    private EdsCrmAccount vendor;

    @Column(name = "weightperunit")
    private String weightPerUnit;

    @Column(name = "sellingprice", precision = 24, scale = 4)
    private BigDecimal sellingPrice;

    @Column(name = "storefront_enable")
    private Boolean storefrontEnable = false;

    @Column(name = "sorder")
    private Integer order = 0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "brandid")
    private EdsBrand brand;

    @Column(name = "isfeatured")
    private Boolean featured;

    @Column(name = "isspecial")
    private Boolean special;

    @Column(name = "showonhomepage")
    private Boolean showOnHomePage;

    @Column(name = "virtual")
    private Boolean virtual;

    @Column(name = "freeshipping")
    private Boolean freeShipping;

    @Column(name = "condition")
    private Integer condition;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "productKit")
    private final List<EdsProductKitItems> productKitItems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<EdsProductWarehouseLocation> productWarehouseLocations = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "discount_applied_products",
            joinColumns = {@JoinColumn(name = "product_id")},
            inverseJoinColumns = {@JoinColumn(name = "discount_id")})
    private List<EdsDiscount> discounts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "item")
    private List<EdsReservation> reservation = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    private List<EdsItemVariations> variationses = new ArrayList<>();

    private Boolean deleted = false;

    @Column(name = "global_reorder_point", precision = 11, scale = 2)
    private BigDecimal globalReorderPoint = new BigDecimal(1);

    @Column(name = "total_value", precision = 25, scale = 4)
    private BigDecimal totalValue = new BigDecimal(0);

    private Date asOf = new Date();

    @Column(name = "purchased_from_supplier")
    private Boolean purchasedFromSupplier = false;

    @Column(name = "sold_to_customer")
    private Boolean soldToCustomer = true;

    @Column(name = "enable_inventory_transaction")
    private Boolean enableIT = true; //Enable Inventory Transaction;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemcustomfieldsid")
    private EdsItemCustomFields itemCustomFields;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsItemCustomFields customFields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private EdsItem parent;

    @Column(name = "has_variations")
    private Boolean hasVariations = false;

    @Column(name = "comision", precision = 5, scale = 2)
    private BigDecimal comission = BigDecimal.ZERO;

//    @Column(name = "show_on_opportunity")
//    private Boolean showOnOpportunity = false;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "item")
    private List<EdsAssemblyItem> assemblyItems = new ArrayList<>();

    @Column(name = "saasu_guid")
    private String saasuGUID;

    @Column(name = "sasuuLastUpdatedDate")
    private Date sasuuLastUpdatedDate;

    @Column(name = "saasuLastUpdatedUid")
    private String saasuLastUpdatedUid;

    @Column(name = "quickbook_item_id")
    private String quickbookItemID;

    @Column(name = "quickbook_edit_sequence")
    private String quickbookEditSequence;

    @Column(name = "magentoEntityID")
    private Integer magentoEntityID;

    @Column(name = "magentoSyncDate")
    private Date magentoSyncDate;

    @Column(name = "external_guid")
    private String externalGUID;

    private String nimbleOfferID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pictureid")
    private EdsProductPicture picture;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private final List<EdsQuoteItem> quoteItems = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private final List<EdsInvoiceItem> invoiceItems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "item")
    private List<EdsItemStock> itemStockList = new ArrayList<>();

    @Column(name = "isactive")
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locale_parent_id")
    private EdsItem localeParent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localeid")
    @ForeignKey(name = "none")
    private EdsLocale itemLocale;

    @Column(name = "inventory_tracking_enabled", columnDefinition = "boolean default false")
    private Boolean inventoryTrackingEnabled = false;

    @Column(name = "batch_tracking_enabled", columnDefinition = "boolean default false")
//this is for batch trackig Serials
    private Boolean batchTrackingEnabled = false;

    @Column(name = "track_batches_enabled", columnDefinition = "boolean default false")
    private Boolean trackBatchesEnabled = false;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "item")
    private List<EdsItemMultiPrice> multiPrices = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "item")
    private List<EdsRentalProductItem> rentalItems = new ArrayList<>();

    @Column(name = "extraHour", precision = 14, scale = 4)
    private BigDecimal extraHour;

    @Column(name = "extraDay", precision = 14, scale = 4)
    private BigDecimal extraDay;

    @Column(name = "securityTime", precision = 14, scale = 4)
    private BigDecimal securityTime;

    public List<EdsQuoteItem> getQuoteItems() {
        return quoteItems;
    }

    public List<EdsInvoiceItem> getInvoiceItems() {
        return invoiceItems;
    }

    @Column(name = "subsidiaryProductUniqNum")
    private String subsidiaryProductUniqNum;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @Column(name = "discountType")
    private Integer discountType;

    @Column(name = "discountAmount", precision = 11, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "stockChanged", columnDefinition = "boolean default false")
    private boolean stockChanged = false;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Where(clause = "parentId is null")
    private final List<EdsProductPicture> pictures = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "product_suppliers",
            joinColumns = @JoinColumn(name = "productid"),
            inverseJoinColumns = @JoinColumn(name = "supplierid")
    )
    private Set<EdsCrmAccount> suppliers = new HashSet<>();

    @Transient
    private BigDecimal averageCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "barcodeFileid")
    private EdsUpload barcodeFile;

    private String barcodeChecksum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaultWarehouse")
    private EdsWarehouse defaultWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerId")
    private EdsCrmAccount customer;

    private Long zapiervariantid;//were added for zapier/shopify integration

    @Enumerated(EnumType.STRING)
    private ProductKitCostAllocationType costAllocationType;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedBy")
    private EdsUser updater;

//    @Type(type = "jsonb")
//    @Column(name = "log_histories", columnDefinition = "jsonb")
//    private List<LogHistoryItem> logHistories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photoId")
    private EdsUpload photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_status_id")
    private EdsReference rentStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rent_item_id")
    private EdsItem rentItem;

    @Transient
    public Set<EdsPriceLevelPP> transientPriceLevels = new HashSet<>();

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            schema = EdsScope.PRIVATE_SCHEMA, name = "product_location",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id"))
    private Set<EdsLocation> locations = new HashSet<>();

    @Transient
    public boolean hasItemOutTransaction = false;

    @Column(name = "is_sent_to_textile_finds", columnDefinition = "boolean default false")
    private Boolean sentToTextileFinds = false;

    public Set<EdsCrmAccount> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<EdsCrmAccount> suppliers) {
        this.suppliers = suppliers;
    }


    public void setFields(NewInvoiceItem item, EdsVat vat, EdsCompany company, EdsReference type) {
        this.name = item.getItemName();
        this.description = item.getDescription();
        this.unitPrice = item.getUnitPrice();
        this.unitCost = item.getUnitCost();
        this.net = item.getNet();
        this.vat = vat;
    }

    public NewInvoiceItem getTransferObject() {
        NewInvoiceItem item = new NewInvoiceItem();
        item.setID(objectID);
        item.setItemName(name);
        item.setDescription(description);
        item.setUnitPrice(unitPrice != null ? unitPrice : ZERO);
        item.setUnitCost(unitCost != null ? unitCost : ZERO);
        item.setNet(net != null ? net : ZERO);
        if (vat != null) {
            item.setTaxItem(vat.createTaxItem());
        }

        return item;
    }

    public boolean isUsedInItems() {

        /*check for quote items*/
        if (quoteItems != null && !quoteItems.isEmpty()) {
            for (EdsQuoteItem quoteItem : quoteItems) {
                if (quoteItem.getQuote() != null && !quoteItem.getQuote().isDeleted() && !quoteItem.getQuote().getStatus().getCode().equals(REVERSED)) {
                    return true;
                }
            }
        }

        /*check for invoice items*/
        if (invoiceItems != null && !invoiceItems.isEmpty()) {
            for (EdsInvoiceItem invoiceItem : invoiceItems) {
                if (invoiceItem.getInvoice() != null && !invoiceItem.getInvoice().isDeleted() && !invoiceItem.getInvoice().getStatus().getCode().equals(REVERSED)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isTrackInventoryEnabled() {
        return (getTrackInventory() != null && getTrackInventory());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!ServerUtils.equalsString(this.name, name)) {
            addChange(CustomFormConstants.NAME);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("name", "Name"), this.name != null ? this.name : "", name != null ? name : "", this.name == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionWithEnter() {
        return description != null ? description.replace("\n", "<br>") : null;
    }

    public void setDescription(String description) {
        if (!ServerUtils.equalsString(this.description, description)) {
            addChange(CustomFormConstants.DESCRIPTION);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("description", "Description"), this.description != null ? this.description : "", description != null ? description : "", this.description == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        if (!ServerUtils.equalsBigDecimal(this.unitPrice, unitPrice)) {
            addChange(CustomFormConstants.COST_PRICE);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("purchasePrice", "Purchase Price"), this.unitPrice != null ? this.unitPrice : BigDecimal.ZERO, unitPrice != null ? unitPrice : BigDecimal.ZERO, this.unitPrice == null || (this.unitPrice != null && this.unitPrice.compareTo(BigDecimal.ZERO) == 0) ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQty() {
        return getItemsInStock();
    }

    public BigDecimal getItemsInStock() {
        ItemStockManager itemStockManager = ApplicationContextProvider.applicationContext.getBean("itemStockManager", ItemStockManager.class);
        return itemStockManager.getAvailableStock(getObjectID(), null, null);
    }

    public BigDecimal getStockByWarehouse(Integer warehouseID) {
        ItemStockManager itemStockManager = ApplicationContextProvider.applicationContext.getBean("itemStockManager", ItemStockManager.class);
        return itemStockManager.getAvailableStock(getObjectID(), warehouseID, null);
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public EdsVat getVat() {
        return vat;
    }

    public void setVat(EdsVat vat) {
        if (!ServerUtils.equalsEdsObject(this.vat, vat)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("taxRate", "Tax Rate"), this.vat != null ? this.vat.getName() : "", vat != null ? vat.getName() : "",
                    this.vat == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.vat = vat;
    }

    /*public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }*/

    public void setTrackInventory(Boolean trackInventory) {
        this.trackInventory = trackInventory;
    }

    public Boolean getTrackInventory() {
        return trackInventory;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getInventory() {
        return inventory;
    }

    public void setInventory(BigDecimal inventory) {
        this.inventory = inventory;
    }

    public Boolean getRentalItem() {
        return rentalItem;
    }

    public void setRentalItem(Boolean rentalItem) {
        this.rentalItem = rentalItem;
    }

    public Integer getRentalPeriod() {
        return rentalPeriod;
    }

    public void setRentalPeriod(Integer rentalPeriod) {
        this.rentalPeriod = rentalPeriod;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public BigDecimal getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }

    public Integer getCancelationPeriod() {
        return cancelationPeriod;
    }

    public void setCancelationPeriod(Integer cancelationPeriod) {
        this.cancelationPeriod = cancelationPeriod;
    }

    public Integer getCancelationPeriodType() {
        return cancelationPeriodType;
    }

    public void setCancelationPeriodType(Integer cancelationPeriodType) {
        this.cancelationPeriodType = cancelationPeriodType;
    }

    public BigDecimal getCancelationFee() {
        return cancelationFee;
    }

    public void setCancelationFee(BigDecimal cancelationFee) {
        this.cancelationFee = cancelationFee;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        if (!ServerUtils.equalsEdsObject(this.account, account)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("salesAccount", "Sales Account"), this.account != null ? this.account.getName() : "", account != null ? account.getName() : "", this.account == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.account = account;
    }

    public BigDecimal getUsedItems() {
        return usedItems;
    }

    public void setUsedItems(BigDecimal usedItems) {
        this.usedItems = usedItems;
    }

    public EdsProductCategory getCategory() {
        return category;
    }

    public void setCategory(EdsProductCategory category) {
        if (!ServerUtils.equalsEdsObject(this.category, category)) {
            addChange(CustomFormConstants.CATEGORY);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("category", "Category"), this.category != null ? this.category.getName() : "", category != null ? category.getName() : "",
                    this.category == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.category = category;
    }

    public String getInternalSKUNumber() {
        return internalSKUNumber;
    }

    public void setInternalSKUNumber(String internalSKUNumber) {
        if (!ServerUtils.equalsString(this.internalSKUNumber, internalSKUNumber)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("skuNumber", "SKU Number"), this.internalSKUNumber != null ? this.internalSKUNumber : "", internalSKUNumber != null ? internalSKUNumber : "", this.internalSKUNumber == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.internalSKUNumber = internalSKUNumber;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        if (!ServerUtils.equalsString(this.barCode, barCode)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("barcode", "Barcode"), this.barCode != null ? this.barCode : "", barCode != null ? barCode : "",
                    this.barCode == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.barCode = barCode;
    }

    public String getUpcNumber() {
        return upcNumber;
    }

    public void setUpcNumber(String upcNumber) {
        if (!ServerUtils.equalsString(this.upcNumber, upcNumber)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("upcNumber", "UPC Number"), this.upcNumber != null ? this.upcNumber : "", upcNumber != null ? upcNumber : "",
                    this.upcNumber == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.upcNumber = upcNumber;
    }

    public EdsUnitMeasurement getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(EdsUnitMeasurement unitMeasurement) {
        if (!ServerUtils.equalsEdsObject(this.unitMeasurement, unitMeasurement)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("unitMeasurement", "Unit Measurement"), this.unitMeasurement != null ? this.unitMeasurement.getName() : "", unitMeasurement != null ? unitMeasurement.getName() : "", this.unitMeasurement == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.unitMeasurement = unitMeasurement;
    }

    public EdsCrmAccount getVendor() {
        return vendor;
    }

    public void setVendor(EdsCrmAccount vendor) {
        if (!ServerUtils.equalsEdsObject(this.vendor, vendor)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("supplier", "Supplier"), this.vendor != null ? this.vendor.getName() : "", vendor != null ? vendor.getName() : "",
                    this.vendor == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.vendor = vendor;
    }

    public String getWeightPerUnit() {
        return weightPerUnit;
    }

    public void setWeightPerUnit(String weightPerUnit) {
        this.weightPerUnit = weightPerUnit;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice != null ? sellingPrice : BigDecimal.ZERO;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        if (!ServerUtils.equalsBigDecimal(this.sellingPrice, sellingPrice)) {
            addChange(CustomFormConstants.SELLING_PRICE);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("sellingPrice", "Selling Price"), this.sellingPrice != null ? this.sellingPrice : BigDecimal.ZERO, sellingPrice != null ? sellingPrice : BigDecimal.ZERO, this.sellingPrice == null || (this.sellingPrice != null && this.sellingPrice.compareTo(BigDecimal.ZERO) == 0) ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.sellingPrice = sellingPrice;
    }

    public Boolean isStorefrontEnable() {
        return storefrontEnable != null ? storefrontEnable : true;
    }

    public void setStorefrontEnable(Boolean storefrontEnable) {
        this.storefrontEnable = storefrontEnable;
    }

    public Integer getOrder() {
        return order == null ? 0 : order;
    }

    public void setOrder(Integer order) {
        if (!ServerUtils.equalsInteger(this.order, order)) {
            addHistoryChange("Order", this.order != null ? this.order : 0, order != null ? order : 0, this.order == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.order = order;
    }

    public EdsBrand getBrand() {
        return brand;
    }

    public void setBrand(EdsBrand brand) {
        if (!ServerUtils.equalsEdsObject(this.brand, brand)) {
            addChange(CustomFormConstants.BRAND);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("brand", "Brand"), this.brand != null ? this.brand.getName() : "", brand != null ? brand.getName() : "",
                    this.brand == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.brand = brand;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getSpecial() {
        return special;
    }

    public void setSpecial(Boolean special) {
        this.special = special;
    }

    public Boolean getShowOnHomePage() {
        return showOnHomePage;
    }

    public void setShowOnHomePage(Boolean showOnHomePage) {
        this.showOnHomePage = showOnHomePage;
    }

    public Boolean getVirtual() {
        return virtual;
    }

    public void setVirtual(Boolean virtual) {
        this.virtual = virtual;
    }

    public Boolean getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public Integer getCondition() {
        return condition;
    }

    public List<EdsHistoryLog> getLogHistories() {
        HistoryLogManager historyLogManager = ApplicationContextProvider.applicationContext.getBean("historyLogManager", HistoryLogManager.class);
        return historyLogManager.getEntityHistoryLog(getObjectID(),"ITEM");
    }


    public void setCondition(Integer condition) {
        this.condition = condition;
    }

    public List<EdsProductKitItems> getProductKitItems() {
        return productKitItems;
    }

    public BigDecimal getProductKitStandardPrice() {
        BigDecimal standardPrice = BigDecimal.ZERO;
        if (productKitItems != null) {
            for (EdsProductKitItems pki : productKitItems) {
                if (pki.getItem() != null && pki.getQuantity() != null && pki.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                    standardPrice = standardPrice.add(pki.getProductKitItemNetAmount());
                }
            }
        }
        return standardPrice;
    }

    public HashMap<Integer, Integer> getProductSubItems(HashMap<Integer, Integer> itemsMap) {
        if (PRODUCT_KIT.equals(getType())) {
            if (productKitItems != null) {
                for (EdsProductKitItems pki : productKitItems) {
                    EdsItem product = pki.getItem();
                    if (product != null) {
                        if (PRODUCT_KIT.equals(product.getType()) || ASSEMBLY_ITEM.equals(product.getType())) {
                            itemsMap.put(product.getObjectID(), product.getObjectID());
                            product.getProductSubItems(itemsMap);
                        }
                    }
                }
            }
        } else if (ASSEMBLY_ITEM.equals(getType())) {
            if (assemblyItems != null) {
                for (EdsAssemblyItem ai : assemblyItems) {
                    EdsItem product = ai.getProductItem();
                    if (product != null) {
                        if (ASSEMBLY_ITEM.equals(product.getType()) || PRODUCT_KIT.equals(product.getType())) {
                            itemsMap.put(product.getObjectID(), product.getObjectID());
                            product.getProductSubItems(itemsMap);
                        }
                    }
                }
            }
        }
        return itemsMap;
    }

    public HashMap<Integer, BigDecimal> getInventoryItemsRequired(HashMap<Integer, BigDecimal> productMap, BigDecimal qty) {
        if (ASSEMBLY_ITEM.equals(getType())) {
            if (assemblyItems != null) {
                for (EdsAssemblyItem ai : assemblyItems) {
                    EdsItem product = ai.getProductItem();
                    if (PRODUCT_KIT.equals(product.getType()) || INVENTORY_ITEM.equals(product.getType())) {
                        product.getInventoryItemsRequired(productMap, ai.getQty().multiply(qty));
                    }
                    if (ASSEMBLY_ITEM.equals(product.getType())) {
                        if (productMap.containsKey(product.getObjectID())) {
                            productMap.put(product.getObjectID(), productMap.get(product.getObjectID()).add(qty));
                        } else {
                            productMap.put(product.getObjectID(), ai.getQty().multiply(qty));
                        }
                    }
                }
            }
        } else if (PRODUCT_KIT.equals(getType())) {
            if (productKitItems != null) {
                for (EdsProductKitItems pki : productKitItems) {
                    EdsItem product = pki.getItem();
                    if (PRODUCT_KIT.equals(product.getType()) || ASSEMBLY_ITEM.equals(product.getType()) || INVENTORY_ITEM.equals(product.getType())) {
                        product.getInventoryItemsRequired(productMap, qty);
                    }
                }
            }
        } else if (INVENTORY_ITEM.equals(getType())) {
            if (productMap.containsKey(getObjectID())) {
                productMap.put(getObjectID(), productMap.get(getObjectID()).add(qty));
            } else {
                productMap.put(getObjectID(), qty);
            }
        }
        return productMap;
    }

    public boolean hasInventoryInProductKit() {
        if (productKitItems != null) {
            for (EdsProductKitItems kItem : productKitItems)
                if (kItem.getItem() != null && INVENTORY_ITEM.equals(kItem.getItem().getType()))
                    return true;
        }

        return false;
    }

    public List<EdsProductWarehouseLocation> getProductWarehouseLocations() {
        return productWarehouseLocations;
    }

    public void setProductWarehouseLocations(List<EdsProductWarehouseLocation> productWarehouseLocations) {
        this.productWarehouseLocations = productWarehouseLocations;
    }

    public Boolean isSentToTextileFinds() {
        return sentToTextileFinds != null ? sentToTextileFinds : false;
    }

    public void setSentToTextileFinds(Boolean sentToTextileFinds) {
        this.sentToTextileFinds = sentToTextileFinds;
    }

    public void addCustomFieldChanges(String changes) {
        if (changes != null && !"".equals(changes)) {
            for (String change : changes.split(",")) {
                if (!"".equals(change.trim())) {
                    addChange(change);
                }
            }
        }
    }

    public List<EdsDiscount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<EdsDiscount> discounts) {
        this.discounts = discounts;
    }

    public Integer getType() {
        return type != null ? type : INVENTORY_ITEM;
    }

    public Integer getProductType() {
        return type;
    }

    public void setType(Integer type) {
        if (!ServerUtils.equalsInteger(this.type, type)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("type", "Type"), this.type != null ? this.type : null, type, this.type == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.type = type;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

//    @Override
//    public void setCreator(EdsUser value) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public boolean isStockChanged() {
        return stockChanged;
    }

    public void setStockChanged(boolean stockChanged) {
        this.stockChanged = stockChanged;
    }


    //    @Override
//    public void setUpdater(EdsUser user) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }

    public String getTypeName() {
        String typeName = "";
        if (type != null) {
            if (type.equals(INVENTORY_ITEM)) {
                typeName = INVENTORY_ITEM_STR;
            } else if (type.equals(NON_INVENTORY_ITEM)) {
                typeName = NON_INVENTORY_ITEM_STR;
            } else if (type.equals(ASSEMBLY_ITEM)) {
                typeName = ASSEMBLY_ITEM_STR;
            } else if (type.equals(OTHER_CHARGE)) {
                typeName = OTHER_CHARGE_STR;
            } else if (type.equals(SERVICE)) {
                typeName = SERVICE_STR;
            } else if (type.equals(PRODUCT_KIT)) {
                typeName = PRODUCT_KIT_STR;
            } else if (type.equals(RENTAL_ITEM)) {
                typeName = RENTAL_ITEM_STR;
            }
        }

        return typeName;
    }

    public String getDiscountTypeName() {
        String disType = "";
        if (discountType != null) {
            if (discountType.equals(AccountingConstants.PERCENTAGE)) {
                disType = PERCENTAGE_STR;
            } else if (discountType.equals(AccountingConstants.FIXED_AMOUNT)) {
                disType = FIXED_AMOUNT_STR;
            }
        }
        return disType;
    }

    public List<EdsReservation> getReservation() {
        return reservation;
    }

    public void setReservation(List<EdsReservation> reservation) {
        this.reservation = reservation;
    }

    public Boolean getDeleted() {
        return deleted != null ? deleted : false;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsAccount getCogsAccount() {
        return cogsAccount;
    }

    public void setCogsAccount(EdsAccount cogsAccount) {
        if (!ServerUtils.equalsEdsObject(this.cogsAccount, cogsAccount)) {
            addChange(CustomFormConstants.COGS_ACCOUNT);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("purchaseAccount", "Purchase Account"), this.cogsAccount != null ? this.cogsAccount.getName() : "", cogsAccount != null ? cogsAccount.getName() : "", this.cogsAccount == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.cogsAccount = cogsAccount;
    }

    public EdsAccount getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(EdsAccount assetAccount) {
        if (!ServerUtils.equalsEdsObject(this.assetAccount, assetAccount)) {
            addChange(CustomFormConstants.ASSET_ACCOUNT);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("assetAccount", "Asset Account"), this.assetAccount != null ? this.assetAccount.getName() : "", assetAccount != null ? assetAccount.getName() : "", this.assetAccount == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.assetAccount = assetAccount;
    }

    public BigDecimal getGlobalReorderPoint() {
        return globalReorderPoint;
    }

    public void setGlobalReorderPoint(BigDecimal globalReorderPoint) {
        if (!ServerUtils.equalsBigDecimal(this.globalReorderPoint, globalReorderPoint)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("minReorderPoint", "Reorder Point"), this.globalReorderPoint != null ? this.globalReorderPoint : BigDecimal.ZERO, globalReorderPoint != null ? globalReorderPoint : BigDecimal.ZERO, this.globalReorderPoint == null || (this.globalReorderPoint != null && this.globalReorderPoint.compareTo(BigDecimal.ZERO) == 0) ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.globalReorderPoint = globalReorderPoint;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Date getAsOf() {
        return asOf;
    }

    public void setAsOf(Date asOf) {
        if (!ServerUtils.equalsDate(this.asOf, asOf)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("asOf", "As of"), this.asOf != null ? this.asOf : "", asOf != null ? asOf : "",
                    this.asOf == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.asOf = asOf;
    }

    public String getProductNumber() {
        return productNumber != null ? productNumber : "";
    }

    public void setProductNumber(String productNumber) {
        if (!ServerUtils.equalsString(this.productNumber, productNumber)) {
            addChange(CustomFormConstants.NUMBER);
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("number", "Number"), this.productNumber != null ? this.productNumber : "", productNumber != null ? productNumber : "",
                    this.productNumber == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.productNumber = productNumber;
    }

    public Boolean isPurchasedFromSupplier() {
        return purchasedFromSupplier != null ? purchasedFromSupplier : false;
    }

    public void setPurchasedFromSupplier(Boolean purchasedFromSupplier) {
        if (!ServerUtils.equalsBoolean(this.purchasedFromSupplier, purchasedFromSupplier)) {
            addHistoryChange("Is it purchased from a supplier", this.purchasedFromSupplier, purchasedFromSupplier, getCreator());
        }
        this.purchasedFromSupplier = purchasedFromSupplier;
    }

    public Boolean isSoldToCustomer() {
        return soldToCustomer != null ? soldToCustomer : false;
    }

    public void setSoldToCustomer(Boolean soldToCustomer) {
        if (!ServerUtils.equalsBoolean(this.soldToCustomer, soldToCustomer)) {
            addHistoryChange("Is it sold to customer", this.soldToCustomer, soldToCustomer, getCreator());
        }
        this.soldToCustomer = soldToCustomer;
    }

    public Boolean enableIT() {
        return enableIT != null ? enableIT : false;
    }

    public void setEnableIT(Boolean enableIT) {
        this.enableIT = enableIT;
    }

    public EdsItemCustomFields getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(EdsItemCustomFields itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public EdsItem getParent() {
        return parent;
    }

    public void setParent(EdsItem parent) {
        if (!ServerUtils.equalsEdsObject(this.parent, parent)) {
            addHistoryChange("Parent", this.parent != null ? this.parent.getProductNumber() : "", parent != null ? parent.getProductNumber() : "", this.parent == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.parent = parent;
    }

    public Boolean getHasVariations() {
        return hasVariations;
    }

    public void setHasVariations(Boolean hasVariations) {
        this.hasVariations = hasVariations;
    }

    public List<EdsItemStock> getItemStockList() {
        return itemStockList;
    }

    public void setItemStockList(List<EdsItemStock> itemStockList) {
        this.itemStockList = itemStockList;
    }

    public BigDecimal getComission() {
        return comission != null ? comission : BigDecimal.ZERO;
    }

    public void setComission(BigDecimal comission) {
        this.comission = comission;
    }

    public Integer getQRCodeSizeID() {
        return QRCodeSizeID;
    }

    public void setQRCodeSizeID(Integer QRCodeSizeID) {
        this.QRCodeSizeID = QRCodeSizeID;
    }

    public List<EdsItemVariations> getVariationses() {
        return variationses;
    }

    public void setVariationses(List<EdsItemVariations> variationses) {
        this.variationses = variationses;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public List<EdsAssemblyItem> getAssemblyItems() {
        return assemblyItems;
    }

    public void setAssemblyItems(List<EdsAssemblyItem> assemblyItems) {
        this.assemblyItems = assemblyItems;
    }

    public String getSaasuGUID() {
        return saasuGUID;
    }

    public void setSaasuGUID(String saasuGUID) {
        this.saasuGUID = saasuGUID;
    }

    public Date getSasuuLastUpdatedDate() {
        return sasuuLastUpdatedDate;
    }

    public void setSasuuLastUpdatedDate(Date lastUpdatedDate) {
        this.sasuuLastUpdatedDate = lastUpdatedDate;
    }

    public EdsProductPicture getPicture() {
        return picture;
    }

    public void setPicture(EdsProductPicture picture) {
        if (!ServerUtils.equalsEdsObject(this.picture, picture)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("picture", "Picture"), this.picture != null ? this.picture.getName() : "", picture != null ? picture.getName() : "",
                    this.picture == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.picture = picture;
    }

    public String getSaasuLastUpdatedUid() {
        return saasuLastUpdatedUid;
    }

    public void setSaasuLastUpdatedUid(String saasuLastUpdatedUid) {
        this.saasuLastUpdatedUid = saasuLastUpdatedUid;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        if (!ServerUtils.equalsString(this.manufacturer, manufacturer)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("manufacturer", "Manufacturer"), this.manufacturer != null ? this.manufacturer : "", manufacturer != null ? manufacturer : "", this.manufacturer == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.manufacturer = manufacturer;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        if (!ServerUtils.equalsString(this.partNumber, partNumber)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("partNumber", "Part Number"), this.partNumber != null ? this.partNumber : "", partNumber != null ? partNumber : "",
                    this.partNumber == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.partNumber = partNumber;
    }

    public String getQuickbookItemID() {
        return quickbookItemID;
    }

    public void setQuickbookItemID(String quickbookItemID) {
        this.quickbookItemID = quickbookItemID;
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

    public String getNimbleOfferID() {
        return nimbleOfferID;
    }

    public void setNimbleOfferID(String nimbleOfferID) {
        this.nimbleOfferID = nimbleOfferID;
    }

    public EdsVat getDoubleVat() {
        return doubleVat;
    }

    public void setDoubleVat(EdsVat doubleVat) {
        this.doubleVat = doubleVat;
    }

    public String getSubsidiaryProductUniqNum() {
        return subsidiaryProductUniqNum;
    }

    public void setSubsidiaryProductUniqNum(String subsidiaryProductUniqNum) {
        this.subsidiaryProductUniqNum = subsidiaryProductUniqNum;
    }

    public Boolean isActive() {
        return active != null ? active : false;
    }

    public void setActive(Boolean active) {
        if (!ServerUtils.equalsBoolean(this.active, active)) {
            addHistoryChange("ACTIVE", this.active, active, getCreator());
            addChange("ACTIVE");
        }
        this.active = active;
    }

    public EdsItemCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsItemCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsItem getLocaleParent() {
        return localeParent;
    }

    public void setLocaleParent(EdsItem localeParent) {
        this.localeParent = localeParent;
    }

    public EdsLocale getItemLocale() {
        return itemLocale;
    }

    public void setItemLocale(EdsLocale itemLocale) {
        this.itemLocale = itemLocale;
    }

    public List<EdsItemMultiPrice> getMultiPrices() {
        return multiPrices;
    }

    public void setMultiPrices(List<EdsItemMultiPrice> multiPrices) {
        this.multiPrices = multiPrices;
    }

    public List<EdsRentalProductItem> getRentalItems() {
        return this.rentalItems;
    }

    public void setRentalItems(final List<EdsRentalProductItem> rentalItems) {
        this.rentalItems = rentalItems;
    }

    public void addRentalItem(EdsRentalProductItem item) {
        item.setItem(this);
        rentalItems.add(item);
    }

    public Integer getMagentoEntityID() {
        return magentoEntityID;
    }

    public void setMagentoEntityID(Integer magentoEntityID) {
        this.magentoEntityID = magentoEntityID;
    }

    public Date getMagentoSyncDate() {
        return magentoSyncDate;
    }

    public void setMagentoSyncDate(Date magentoSyncDate) {
        this.magentoSyncDate = magentoSyncDate;
    }

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        if (!ServerUtils.equalsEdsObject(this.currency, currency)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("currency", "Currency"), this.currency != null ? this.currency.getName() : "", currency != null ? currency.getName() : "", this.currency == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.currency = currency;
    }

    public Boolean getInventoryTrackingEnabled() {
        return inventoryTrackingEnabled != null ? inventoryTrackingEnabled : false;
    }

    public void setInventoryTrackingEnabled(Boolean inventoryTrackingEnabled) {
        this.inventoryTrackingEnabled = inventoryTrackingEnabled;
    }

    public Boolean getBatchTrackingEnabled() {
        return batchTrackingEnabled != null ? batchTrackingEnabled : false;
    }

    public void setBatchTrackingEnabled(Boolean batchTrackingEnabled) {
        this.batchTrackingEnabled = batchTrackingEnabled;
    }

    public Boolean getTrackBatchesEnabled() {
        return trackBatchesEnabled != null ? trackBatchesEnabled : false;
    }

    public void setTrackBatchesEnabled(Boolean trackBatchesEnabled) {
        this.trackBatchesEnabled = trackBatchesEnabled;
    }

    public BigDecimal getExtraHour() {
        return this.extraHour;
    }

    public void setExtraHour(final BigDecimal extraHour) {
        this.extraHour = extraHour;
    }

    public BigDecimal getExtraDay() {
        return this.extraDay;
    }

    public void setExtraDay(final BigDecimal extraDay) {
        this.extraDay = extraDay;
    }

    public BigDecimal getSecurityTime() {
        return this.securityTime;
    }

    public void setSecurityTime(final BigDecimal securityTime) {
        this.securityTime = securityTime;
    }

    public static ProductItem wrapSolrDocumentToRPC(SolrDocument doc) {
        ProductItem item = new ProductItem();
        item.setObjectId(SolrUtils.asInteger(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_ID));
        item.setParentId(SolrUtils.asInteger(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_PARENT_ID));
        item.setProductNumber(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_NUMBER));
        item.setName(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_NAME));
        item.setType(SolrUtils.asInteger(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_ID));
        item.setTypeName(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_NAME));
        item.setProductRentalItemId(SolrUtils.asInteger(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_RENTAL_ITEM_ID));
        item.setDiscountType(SolrUtils.asInteger(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_DISCOUNT_TYPE_ID));
        item.setDiscountTypeName(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_DISCOUNT_TYPE_NAME));
        item.setDiscountAmount(SolrUtils.asBigDecimal(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_DISCOUNT_AMOUNT));
        item.setAccount(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_ACCOUNT_NAME));
        item.setCogsAccount(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_COGS_ACCOUNT_NAME));
        item.setAssetAccount(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_ASSET_ACCOUNT_NAME));
        item.setDescription(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_DESCRIPTION));
        item.setUnitpPrice(SolrUtils.asBigDecimal(doc, SolrProductServiceRepresenter.FIELD_UNITPRICE));
        item.setCostPrice(SolrUtils.asBigDecimal(doc, SolrProductServiceRepresenter.FIELD_COSTPRICE));
        item.setTaxAmountId(SolrUtils.asInteger(doc, SolrProductServiceRepresenter.FIELD_TAXRATE_ID));
        item.setTaxRate(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_TAXRATE));
        item.setTaxAmount(SolrUtils.asBigDecimal(doc, SolrProductServiceRepresenter.FIELD_TAX_EFFECTIVE_RATE));
        item.setActive(SolrUtils.asBoolean(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_ACTIVE));
        item.setRentStatus(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_RENT_STATUS));
        item.setStorefrontEnable(SolrUtils.asBoolean(doc, SolrProductServiceRepresenter.FIELD_PRODUCT_STOREFRONT_ENABLE));
        item.setSuppliers(ServerUtils.asListToSelectItem(SolrUtils.asListInteger(doc, SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_ID), SolrUtils.asListString(doc, SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_NAME), SolrUtils.asListString(doc, SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_NUMBER)));
        item.setLocations(ServerUtils.asListToSelectItem(SolrUtils.asListInteger(doc, SolrProductServiceRepresenter.FIELD_MULTI_LOCATION_ID), SolrUtils.asListString(doc, SolrProductServiceRepresenter.FIELD_MULTI_LOCATION_NAME)));
        item.setVendor(ServerUtils.asListToString(SolrUtils.asListString(doc, SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_NAME)));
        item.setCategory(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_CATEGORY));
        item.setCategoryId(SolrUtils.asInteger(doc, SolrProductServiceRepresenter.FIELD_CATEGORY_ID));
        item.setPartNumber(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_PART_NUMBER));
        item.setBarCodeString(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_BARCODE));
        item.setManufacturer(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_MANUFACTURER));
        item.setSkuNumber(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_SKU_NUMBER));
        item.setUpcNumber(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_UPC_NUMBER));
        item.setSubsidiaryProductUniqNum(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_SUBSIDIARY_PRODUCT_UNIQ_NUM));
        item.setUnitMeasurementName(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_NAME));
        item.setUnitMeasurementId(SolrUtils.asInteger(doc, SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_ID));
        item.setBrand(SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_BRAND_NAME));
        String averageCost = SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_AVERAGE_COST);
        item.setAverageCost(averageCost != null ? new BigDecimal(averageCost) : null);
        item.setItemsInStock(SolrUtils.asBigDecimal(doc, SolrProductServiceRepresenter.FIELD_QUANTITY_ON_HAND));
        item.setCreatedDate(SolrUtils.asDate(doc, SolrProductServiceRepresenter.FIELD_CREATED_DATE));
        item.setUpdatedDate(SolrUtils.asDate(doc, SolrProductServiceRepresenter.FIELD_UPDATED_DATE));

        Integer creatorID = SolrUtils.asInteger(doc, SolrProductServiceRepresenter.FIELD_CREATOR_ID);
        if (creatorID != null) {
            item.setCreator(new SelectItem(creatorID, SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_CREATOR_NAME)));
        }

        Integer updaterID = SolrUtils.asInteger(doc, SolrProductServiceRepresenter.FIELD_UPDATER_ID);
        if (updaterID != null) {
            item.setUpdater(new SelectItem(updaterID, SolrUtils.asString(doc, SolrProductServiceRepresenter.FIELD_UPDATER_NAME)));
        }

        return item;
    }

    public ProductSolrItem getSolrRPC() {
        ProductSolrItem productSolrItem = new ProductSolrItem();

        productSolrItem.setObjectId(getObjectID());
        productSolrItem.setProductNumber(getProductNumber());
        productSolrItem.setProductName(getName());
        productSolrItem.setProductType(new SelectItem(getType(), getTypeName()));
        productSolrItem.setProductActive(isActive());
        productSolrItem.setProductStorefrontEnable(isStorefrontEnable());
        productSolrItem.setPartNumber(getPartNumber());
        productSolrItem.setSkuNumber(getInternalSKUNumber());
        productSolrItem.setUpsNumber(getUpcNumber());
        productSolrItem.setManufacturer(getManufacturer());
        productSolrItem.setSubsidiaryProductUniqNum(getSubsidiaryProductUniqNum());
        productSolrItem.setBarcode(getBarCode());
        productSolrItem.setCreatedDate(getCreationTime());
        productSolrItem.setUpdatedDate(getLastUpdateTime());
        productSolrItem.setQuantityOnHand(getItemsInStock() != null ? getItemsInStock().doubleValue() : null);
        productSolrItem.setAverageCost(getAverageCost() != null ? getAverageCost().toString() : null);
        productSolrItem.setProductParentId(getParent() != null ? getParent().getObjectID() : null);
        productSolrItem.setProductDiscountAmount(getDiscountAmount() != null ? getDiscountAmount().doubleValue() : 0.0);
        productSolrItem.setProductDiscountType(new SelectItem(getDiscountType(), getDiscountTypeName()));

        if (getAccount() != null) {
            productSolrItem.setAccount(getAccount().getAsSelectItem());
        }

        if (getCogsAccount() != null) {
            productSolrItem.setCogsAccount(getCogsAccount().getAsSelectItem());
        }

        if (getAssetAccount() != null) {
            productSolrItem.setAssetAccount(getAssetAccount().getAsSelectItem());
        }
        productSolrItem.setDescription(getDescription());
        if (getSellingPrice() != null) {
            productSolrItem.setUnitPrice(getSellingPrice().doubleValue());
        }
        if (getUnitPrice() != null) {
            productSolrItem.setCostPrice(getUnitPrice().doubleValue());
        }
        if (getVat() != null) {
            productSolrItem.setTaxRate(getVat().getAsSelectItem());
        }
        if (getUnitMeasurement() != null) {
            productSolrItem.setUnitMeasurement(getUnitMeasurement().getAsSelectItem());
        }
        if (getBrand() != null) {
            productSolrItem.setBrand(getBrand().getAsSelectItem());
        }
        if (getSuppliers() != null) {
            getSuppliers().forEach(edsCrmAccount -> {
                String supplierName = edsCrmAccount.getName() != null ? edsCrmAccount.getName() : "N/A";
                String supplierNumber = edsCrmAccount.getNumber() != null ? edsCrmAccount.getNumber() : "N/A";
                SelectItem multiSupplier = new SelectItem(edsCrmAccount.getObjectID(), supplierName);
                multiSupplier.setNumber(supplierNumber);

                productSolrItem.getMultiSupplier().add(multiSupplier);
            });
        }
        if (getLocations() != null) {
            getLocations().forEach(location -> {
                productSolrItem.getMultiLocation().add(location.getAsSelectItem());
            });
        }
        if (getCategory() != null) {
            productSolrItem.setCategory(getCategory().getAsSelectItem());
        }
        if (getCategory() != null && getCategory().getParent() != null) {
            productSolrItem.setParentCategory(getCategory().getParent().getAsSelectItem());
        }
        productSolrItem.setInventoryTrackingEnabled(getInventoryTrackingEnabled() != null ? getInventoryTrackingEnabled() : false);
        productSolrItem.setTrackBatchesEnabled(getTrackBatchesEnabled() != null ? getTrackBatchesEnabled() : false);
        if (getCreator() != null) {
            productSolrItem.setCreator(getCreator().getAsSelectItem());
        }
        if (getUpdater() != null) {
            productSolrItem.setUpdater(getUpdater().getAsSelectItem());
        }

        productSolrItem.setCustomDescriptionData(getCustomDescriptionData());

        return productSolrItem;
    }

    public EdsUser getCreator() {
        return this.creator;
    }

    public void setCreator(final EdsUser creator) {
        this.creator = creator;
    }

    public EdsUser getUpdater() {
        return this.updater;
    }

    public void setUpdater(final EdsUser updater) {
        this.updater = updater;
    }

    public SolrInputDocument wrapToSolrDocument(boolean isCustomSubItemsEnabled) {
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField(SolrProductServiceRepresenter.FIELD_DOC_TYPE, SolrProductServiceRepresenter.PRODUCT_SOLR_DOC);
        doc.addField(SolrProductServiceRepresenter.FIELD_COMPANY_ID, SecurityContext.getCompanyID());
        doc.addField(SolrProductServiceRepresenter.FIELD_COMPOSITE_ID, SecurityContext.getCompanyID() + "_" + getObjectID());
        doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_ID, getObjectID());
        doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_NUMBER, getProductNumber());
        doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_NAME, getName());
        doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_ID, getType());
        doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_NAME, getTypeName());
        doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_DISCOUNT_TYPE_ID, getDiscountType());
        doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_DISCOUNT_TYPE_NAME, getDiscountTypeName());
        doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_DISCOUNT_AMOUNT, getDiscountAmount() != null ? getDiscountAmount().doubleValue() : 0);
        doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_TYPE_ID_NAME, getType() + SolrProductServiceRepresenter.SPLIT + getTypeName());
        doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_ACTIVE, isActive());
        doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_STOREFRONT_ENABLE, isStorefrontEnable());
        doc.addField(SolrProductServiceRepresenter.FIELD_PART_NUMBER, getPartNumber());
        doc.addField(SolrProductServiceRepresenter.FIELD_SKU_NUMBER, getInternalSKUNumber());
        doc.addField(SolrProductServiceRepresenter.FIELD_UPC_NUMBER, getUpcNumber());
        doc.addField(SolrProductServiceRepresenter.FIELD_MANUFACTURER, getManufacturer());
        doc.addField(SolrProductServiceRepresenter.FIELD_SUBSIDIARY_PRODUCT_UNIQ_NUM, getSubsidiaryProductUniqNum());
        doc.addField(SolrProductServiceRepresenter.FIELD_BARCODE, getBarCode());
        doc.addField(SolrProductServiceRepresenter.FIELD_CREATED_DATE, getCreationTime());
        doc.addField(SolrProductServiceRepresenter.FIELD_UPDATED_DATE, getLastUpdateTime());
        BigDecimal quantityOnHand = getItemsInStock();
        doc.addField(SolrProductServiceRepresenter.FIELD_QUANTITY_ON_HAND, String.valueOf(quantityOnHand));

        BigDecimal averageCost = getAverageCost();
        if (averageCost != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_AVERAGE_COST, String.valueOf(averageCost));
        }

        if (getParent() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_PARENT_ID, getParent().getObjectID());
        }
        if (getAccount() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_ACCOUNT_ID, getAccount().getObjectID());
            doc.addField(SolrProductServiceRepresenter.FIELD_ACCOUNT_NAME, getAccount().getName());
            doc.addField(SolrProductServiceRepresenter.FIELD_ACCOUNT_ID_NAME, getAccount().getObjectID() + SolrProductServiceRepresenter.SPLIT + getAccount().getName());
        }
        if (getCogsAccount() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_COGS_ACCOUNT_ID, getCogsAccount().getObjectID());
            doc.addField(SolrProductServiceRepresenter.FIELD_COGS_ACCOUNT_NAME, getCogsAccount().getName());
            doc.addField(SolrProductServiceRepresenter.FIELD_COGS_ACCOUNT_ID_NAME, getCogsAccount().getObjectID() + SolrProductServiceRepresenter.SPLIT + getCogsAccount().getName());
        }
        if (getAssetAccount() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_ASSET_ACCOUNT_ID, getAssetAccount().getObjectID());
            doc.addField(SolrProductServiceRepresenter.FIELD_ASSET_ACCOUNT_NAME, getAssetAccount().getName());
            doc.addField(SolrProductServiceRepresenter.FIELD_ASSET_ACCOUNT_ID_NAME, getAssetAccount().getObjectID() + SolrProductServiceRepresenter.SPLIT + getAssetAccount().getName());
        }
        doc.addField(SolrProductServiceRepresenter.FIELD_DESCRIPTION, getDescription());
        if (getSellingPrice() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_UNITPRICE, String.valueOf(getSellingPrice()));
        }
        if (getUnitPrice() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_COSTPRICE, String.valueOf(getUnitPrice()));
        }
        if (getVat() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_TAXRATE_ID, getVat().getObjectID());
            doc.addField(SolrProductServiceRepresenter.FIELD_TAXRATE, getVat().getName());
            doc.addField(SolrProductServiceRepresenter.FIELD_TAX_EFFECTIVE_RATE, String.valueOf(getVat().getEffectiveTaxRate()));
        }
        if (getUnitMeasurement() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_ID, getUnitMeasurement().getObjectID());
            doc.addField(SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_NAME, getUnitMeasurement().getName());
            doc.addField(SolrProductServiceRepresenter.FIELD_UNIT_MEASUREMENT_ID_NAME, getUnitMeasurement().getObjectID() + SolrProductServiceRepresenter.SPLIT + getUnitMeasurement().getName());
        }
        if (getRentItem() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_RENTAL_ITEM_ID, getRentItem().getObjectID());
        }
        if (getBrand() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_BRAND_ID, getBrand().getObjectID());
            doc.addField(SolrProductServiceRepresenter.FIELD_BRAND_NAME, getBrand().getName());
            doc.addField(SolrProductServiceRepresenter.FIELD_BRAND_ID_NAME, getBrand().getObjectID() + SolrProductServiceRepresenter.SPLIT + getBrand().getName());
        }
        for (EdsCrmAccount supplier : suppliers) {
            doc.addField(SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_ID, supplier.getObjectID());
            doc.addField(SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_NAME, supplier.getName());
            doc.addField(SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_NUMBER, supplier.getNumber());
            doc.addField(SolrProductServiceRepresenter.FIELD_MULTI_SUPPLIER_ID_NAME, supplier.getObjectID()
                    + SolrProductServiceRepresenter.SPLIT + supplier.getName());
        }

        if (getCategory() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_CATEGORY, getCategory().getName());
            doc.addField(SolrProductServiceRepresenter.FIELD_CATEGORY_ID, getCategory().getObjectID());
        }

        if (getCategory() != null && getCategory().getParent() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_PARENT_CATEGORY, getCategory().getParent().getName());
            doc.addField(SolrProductServiceRepresenter.FIELD_PARENT_CATEGORY_ID, getCategory().getParent().getObjectID());
        }
        doc.addField(SolrProductServiceRepresenter.FIELD_INVENTORY_TRACKING_ENABLED, getInventoryTrackingEnabled() != null ? getInventoryTrackingEnabled() : false);
        doc.addField(SolrProductServiceRepresenter.FIELD_TRACKING_BATCHES_ENABLED, getTrackBatchesEnabled() != null ? getTrackBatchesEnabled() : false);
        if (getCreator() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_CREATOR_ID, getCreator().getObjectID());
            doc.addField(SolrProductServiceRepresenter.FIELD_CREATOR_NAME, getCreator().getName());
            doc.addField(SolrProductServiceRepresenter.FIELD_CREATOR_ID_NAME, getCreator().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getCreator().getName());
        }
        if (isCustomSubItemsEnabled) {
            doc.addField(SolrProductServiceRepresenter.FIELD_CUSTOM_DESCRIPTION, getCustomDescriptionSolrData());
        }

        if (getUpdater() != null) {
            doc.addField(SolrProductServiceRepresenter.FIELD_UPDATER_ID, getUpdater().getObjectID());
            doc.addField(SolrProductServiceRepresenter.FIELD_UPDATER_NAME, getUpdater().getName());
            doc.addField(SolrProductServiceRepresenter.FIELD_UPDATER_ID_NAME, getUpdater().getObjectID() + SolrSaleInvoiceRepresenter.SPLIT + getUpdater().getName());
        }

        if (quantityOnHand.compareTo(BigDecimal.ZERO) > 0) {
            ItemStockManager itemStockManager = ApplicationContextProvider.applicationContext.getBean("itemStockManager", ItemStockManager.class);
            List<StockItem> stockItems = itemStockManager.getWarehouseStocks(getObjectID());

            if (!CollectionUtils.isEmpty(stockItems)) {
                List<SolrInputDocument> warehouseDocs = new ArrayList<>();
                stockItems.stream().filter(i -> i.getQuantity().compareTo(BigDecimal.ZERO) > 0).forEach(stock -> {
                    SolrInputDocument wareHouseDoc = new SolrInputDocument();
                    wareHouseDoc.setField(SolrProductServiceRepresenter.FIELD_DOC_TYPE, SolrProductServiceRepresenter.WAREHOUSE_SOLR_DOC);
                    wareHouseDoc.addField(SolrProductServiceRepresenter.FIELD_COMPANY_ID, SecurityContext.getCompanyID());
                    wareHouseDoc.addField(SolrProductServiceRepresenter.FIELD_COMPOSITE_ID, SecurityContext.getCompanyID() + "_" + getObjectID() + "_" + (stock.getWarehouseID() != null ? stock.getWarehouseID() : 1));
                    wareHouseDoc.addField(SolrProductServiceRepresenter.FIELD_WAREHOUSE_ID, stock.getWarehouseID());
                    wareHouseDoc.addField(SolrProductServiceRepresenter.FIELD_PRODUCT_ID, getObjectID());
                    wareHouseDoc.addField(SolrProductServiceRepresenter.FIELD_WAREHOUSE_NAME, stock.getWarehouseName());
                    wareHouseDoc.addField(SolrProductServiceRepresenter.FIELD_WAREHOUSE_STOCK, String.valueOf(stock.getQuantity()));
                    warehouseDocs.add(wareHouseDoc);
                });
                doc.addChildDocuments(warehouseDocs);
            }
        }
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }

    private List<ProductsServicesSolrDoc.NewProductCustomDescription> getCustomDescriptionSolrData() {
        return getCustomDescriptionData().stream()
                .map(i -> {
                    ProductsServicesSolrDoc.NewProductCustomDescription description = new ProductsServicesSolrDoc.NewProductCustomDescription();
                    description.setId(i.getId());
                    description.setProductName(i.getName());
                    description.setPrice(i.getPrice().doubleValue());
                    description.setQuantity(i.getQty().doubleValue());
                    return description;
                })
                .toList();
    }

    public List<EdsProductPicture> getPictures() {
        return pictures;
    }

    public ProductSelectItem getAsProductSelectItem() {
        ProductSelectItem productSelectItem = new ProductSelectItem(getObjectID(), getProductNumber() + " -> " + getName(), getDescription(), getProductType(), isPurchasedFromSupplier());
        productSelectItem.setActive(this.isActive());

        return productSelectItem;
    }

    public boolean isHasInventoryInProductKit() {
        if (getProductKitItems() != null && !getProductKitItems().isEmpty()) {
            for (EdsProductKitItems productKitItems : getProductKitItems()) {
                if (productKitItems.getItem() != null) {
                    if (INVENTORY_ITEM.equals(productKitItems.getItem().getType()) || ASSEMBLY_ITEM.equals(productKitItems.getItem().getType())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<NewProductCustomDescription> getCustomDescriptionData() {
        ArrayList<NewProductCustomDescription> customDescriptionList = new ArrayList<>();
        if (assemblyItems != null && !assemblyItems.isEmpty()) {
            for (EdsAssemblyItem ai : assemblyItems) {
                NewProductCustomDescription customDescription = new NewProductCustomDescription();
                customDescription.setId(ai.getProductItem().getObjectID());
                customDescription.setName(ai.getProductItem().getName());
                customDescription.setQty(ai.getQty());
                customDescription.setPrice(ai.getProductItem().getSellingPrice());
                customDescriptionList.add(customDescription);
            }
        }
        return customDescriptionList;
    }

    public ProductKitCostAllocationType getCostAllocationType() {
        return costAllocationType;
    }

    public void setCostAllocationType(ProductKitCostAllocationType costAllocationType) {
        this.costAllocationType = costAllocationType;
    }

    public void setInvoiceItemData(NewInvoiceItem invoiceItem) {
        invoiceItem.setItemID(getObjectID());
        String fulltextName = getProductNumber() + " -> " + getName();
        invoiceItem.setItemName(getName());
        invoiceItem.setFullItemName(fulltextName);
        invoiceItem.setItemNumber(getProductNumber());
        if (getCategory() != null)
            invoiceItem.setItemCategory(getCategory().getName());
        invoiceItem.setProductType(getProductType());
        invoiceItem.setProductPurchasedFromSupplier(isPurchasedFromSupplier());
        invoiceItem.setHasInventoryInProductKit(isHasInventoryInProductKit());
        invoiceItem.setInventoryTrackingEnabled(getInventoryTrackingEnabled());
        invoiceItem.setBatchTrackingEnabled(getBatchTrackingEnabled());//for track serials
        invoiceItem.setTrackBatchesEnabled(getTrackBatchesEnabled());
        if (getBrand() != null) {
            invoiceItem.setProductBrandID(getBrand().getObjectID());
            invoiceItem.setProductBrand(getBrand().getName());
        }
        invoiceItem.setItemOriginalPrice(getSellingPrice());
        if (getMultiPrices() != null && !getMultiPrices().isEmpty()) {
            for (EdsItemMultiPrice itemMultiPrice : getMultiPrices()) {
                invoiceItem.getMultiPricesMap().put(itemMultiPrice.getType() + itemMultiPrice.getCurrency().getObjectID(), itemMultiPrice.getSellingPrice());
            }
            invoiceItem.getMultiPricesMap().put(RECEIVABLE + "-1", getSellingPrice());
            invoiceItem.getMultiPricesMap().put(PAYABLE + "-1", getUnitPrice());
        }
    }

    public static String getProductTypeAsStr(Integer type) {
        if (INVENTORY_ITEM.equals(type)) {
            return INVENTORY_ITEM_STR;
        } else if (NON_INVENTORY_ITEM.equals(type)) {
            return NON_INVENTORY_ITEM_STR;
        } else if (SERVICE.equals(type)) {
            return SERVICE_STR;
        } else if (ASSEMBLY_ITEM.equals(type)) {
            return ASSEMBLY_ITEM_STR;
        } else if (OTHER_CHARGE.equals(type)) {
            return OTHER_CHARGE_STR;
        } else if (PRODUCT_KIT.equals(type)) {
            return PRODUCT_KIT_STR;
        } else if (RENTAL_ITEM.equals(type)) {
            return RENTAL_ITEM_STR;
        }
        return "";
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public EdsUpload getBarcodeFile() {
        return barcodeFile;
    }

    public void setBarcodeFile(EdsUpload barcodeFile) {
        this.barcodeFile = barcodeFile;
    }

    public String getBarcodeChecksum() {
        return barcodeChecksum;
    }

    public void setBarcodeChecksum(String barcodeChecksum) {
        this.barcodeChecksum = barcodeChecksum;
    }

    public EdsWarehouse getDefaultWarehouse() {
        return defaultWarehouse;
    }

    public void setDefaultWarehouse(EdsWarehouse defaultWarehouse) {
        if (!ServerUtils.equalsEdsObject(this.defaultWarehouse, defaultWarehouse)) {
            WfmResourceBundleMessageSource commonLocalizer = (WfmResourceBundleMessageSource) ApplicationContextProvider.applicationContext.getBean("commonLocalizer");
            addHistoryChange(commonLocalizer.localize("warehouse", "Warehouse"), this.defaultWarehouse != null ? this.defaultWarehouse.getName() : "", defaultWarehouse != null ? defaultWarehouse.getName() : "", this.defaultWarehouse == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.defaultWarehouse = defaultWarehouse;
    }

    public EdsCrmAccount getCustomer() {
        return customer;
    }

    public void setCustomer(EdsCrmAccount customer) {
        if (!ServerUtils.equalsEdsObject(this.customer, customer)) {
            addHistoryChange("Customer", this.customer != null ? this.customer.getName() : "", customer != null ? customer.getName() : "", this.customer == null ? (EdsUser) SecurityContext.getInstance().getUser() : getCreator());
        }
        this.customer = customer;
    }

    public boolean isHasItemOutTransaction() {
        return this.hasItemOutTransaction;
    }

    public void setHasItemOutTransaction(final boolean hasItemOutTransaction) {
        this.hasItemOutTransaction = hasItemOutTransaction;
    }

    public Long getZapiervariantid() {
        return zapiervariantid;
    }

    public void setZapiervariantid(Long zapiervariantid) {
        this.zapiervariantid = zapiervariantid;
    }

    public BigDecimal getProductKitTotalQuantity() {
        BigDecimal totalQty = BigDecimal.ZERO;

        if (this.getProductKitItems() == null) {
            return totalQty;
        }
        for (EdsProductKitItems pki : this.getProductKitItems()) {
            final EdsItem item = pki.getItem();

            if (item == null || pki.getQuantity() == null) {
                continue;
            }
            if (PRODUCT_KIT.equals(item.getType())) {
                totalQty = totalQty.add(item.getProductKitTotalQuantity());
            } else {
                totalQty = totalQty.add(pki.getQuantity());
            }
        }
        return totalQty;
    }

    public BigDecimal getProductKitTotalCostPrice() {
        BigDecimal totalCostPrice = BigDecimal.ZERO;

        if (this.getProductKitItems() == null) {
            return totalCostPrice;
        }
        for (EdsProductKitItems pki : this.getProductKitItems()) {
            final EdsItem item = pki.getItem();

            if (item == null || pki.getQuantity() == null) {
                continue;
            }
            totalCostPrice = totalCostPrice.add(Optional.ofNullable(item.getUnitPrice()).orElse(BigDecimal.ZERO).multiply(pki.getQuantity()));
            /*if (PRODUCT_KIT.equals(item.getType())) {
                totalCostPrice = totalCostPrice.add(item.getProductKitTotalCostPrice());
            } else if (item.getUnitPrice() != null) { // TODO quick solution. Remove when solved generally for Service item
                totalCostPrice = totalCostPrice.add(item.getUnitPrice().multiply(pki.getQuantity()));
            }*/
        }
        return totalCostPrice;
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            switch (fieldID) {
                case CustomFormConstants.NAME -> setName((String) value);
                case CustomFormConstants.NUMBER -> setProductNumber((String) value);
                case CustomFormConstants.DESCRIPTION -> setDescription((String) value);
            }
        }
        super.setValueForField(field, value);
    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.NAME)) {
            return getName();
        } else if (fieldID.equals(CustomFormConstants.NUMBER)) {
            return getProductNumber();
        } else if (fieldID.equals(CustomFormConstants.DESCRIPTION)) {
            return getDescription();
        } else if (fieldID.equals(CustomFormConstants.CATEGORY)) {
            return getCategory() != null ? getCategory().getName() : null;
        } else if (fieldID.equals(CustomFormConstants.BRAND)) {
            return getBrand() != null ? getBrand().getName() : null;
        } else if (fieldID.equals(CustomFormConstants.COST_PRICE) || fieldID.equals(AccountingCustomFormConstants.PURCHASE_PRICE)) {
            return getUnitPrice();
        } else if (fieldID.equals(CustomFormConstants.SELLING_PRICE) || fieldID.equals(AccountingCustomFormConstants.SALES_PRICE)) {
            return getSellingPrice();
        } else if (fieldID.equals(CustomFormConstants.COGS_ACCOUNT)) {
            return getCogsAccount();
        } else if (fieldID.equals(CustomFormConstants.ASSET_ACCOUNT)) {
            return getAssetAccount();
        } else if (fieldID.equals(CustomFormConstants.UNIT_MEASUREMENT)) {
            return getUnitMeasurement();
        } else if (fieldID.equals(CustomFormConstants.SUPPLIERS)) {
            return getSuppliers();
        } else if (fieldID.equals(CustomFormConstants.QUANTITY_ON_HAND)) {
            return getItemsInStock();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getCustomFields() != null ? CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID) : "";
        }
        return super.getRealValue(fieldID);
    }

    public void addHistoryChange(String field, Object oldValue, Object newValue, EdsUser creator) {
        if (getObjectID() != null) {
            LogHistoryItem change = new LogHistoryItem();
            change.setField(field);
            change.setEntityID(getObjectID());
            change.setEntityType("ITEM");
            if (creator != null) {
                change.setCreator(creator.getFullName());
                change.setCreatorID(creator.getObjectID());
            }
            if (oldValue instanceof String || oldValue instanceof Double) {
                oldValue = oldValue == null ? "" : oldValue;
                change.setFromStringValue(String.valueOf(oldValue));
            } else if (oldValue instanceof Integer) {
                change.setFromNumberValue(new BigDecimal((Integer) oldValue));
            } else if (oldValue instanceof Number) {
                change.setFromNumberValue((BigDecimal) oldValue);
            } else if (oldValue instanceof Date) {
                change.setFromDateValue((Date) oldValue);
            } else if (oldValue instanceof Boolean) {
                change.setFromStringValue((Boolean) oldValue ? "Yes" : "No");
            }
            if (newValue instanceof String || newValue instanceof Double) {
                newValue = newValue == null ? "" : newValue;
                change.setToStringValue(String.valueOf(newValue));
            } else if (newValue instanceof Integer) {
                change.setToNumberValue(new BigDecimal((Integer) newValue));
            } else if (newValue instanceof Number) {
                change.setToNumberValue((BigDecimal) newValue);
            } else if (newValue instanceof Date) {
                change.setToDateValue((Date) newValue);
            } else if (newValue instanceof Boolean) {
                change.setToStringValue((Boolean) newValue ? "Yes" : "No");
            }
            change.setUpdatedDate(new Date());
            change.setCreator(((EdsUser) SecurityContext.getInstance().getUser()).getFullName());
            change.setCreatorID(((EdsUser) SecurityContext.getInstance().getUser()).getObjectID());

            HistoryLogManager historyLogManager = ApplicationContextProvider.applicationContext.getBean("historyLogManager", HistoryLogManager.class);
            historyLogManager.create(new EdsHistoryLog().convertToDb(change));
        }
    }

    public EdsUpload getPhoto() {
        return photo;
    }

    public void setPhoto(EdsUpload photo) {
        this.photo = photo;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Set<EdsLocation> getLocations() {
        return locations;
    }

    public void setLocations(Set<EdsLocation> locations) {
        this.locations = locations;
    }

    public EdsReference getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(EdsReference rentStatus) {
        this.rentStatus = rentStatus;
    }

    public EdsItem getRentItem() {
        return rentItem;
    }

    public void setRentItem(EdsItem rentItem) {
        this.rentItem = rentItem;
    }
}
