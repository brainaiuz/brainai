package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.TAX_CALCULATION_INCLUSIVE;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 07.11.2007
 * Time: 12:09:12
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "vat")
public class EdsVat extends EdsObject {

    public static final String FAI_VAT = "_FAI_VAT";
    public static final String FAI_CATEGORY = "_FAI_CATEGORY";
    public static final String FAI_PURCHASE_VAT = "_FAI_PURCHASE_VAT";
    public static final String FAI_PURCHASE_CATEGORY = "_FAI_PURCHASE_CATEGORY";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "taxid")
    private Set<EdsTaxComponent> components = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "taxid")
    private Set<EdsTaxGroupItem> groupItems = new HashSet<>();

    @Column(name = "isdefault", columnDefinition = "boolean default false")
    private boolean selectedByTaxDefault;

    @Column(name = "faiid")
    private Integer faiId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faiid", insertable = false, updatable = false)
    private EdsReference faiVat;

    @Column(name = "faiPurchaseId")
    private Integer faiPurchaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faiPurchaseId", insertable = false, updatable = false)
    private EdsReference faiPurchaseVat;

    @Column(name = "status", columnDefinition = "boolean default true")
    private Boolean status;

    private String name;

    @Column(precision = 11, scale = 5)
    private BigDecimal vatAmount;

    //This is for UK company only
    // SALES = 1, PURCHASES = 2, EXEMPT_SALES = 3, EXEMPT_PURCHASES = 4, EC_SALES = 5, EC_PURCHASES = 6
    private Integer taxType;

    //This is for edit/delete taxes  1 = NON_DELETABLE, 2 = NON_EDITABLE/NON_DELETABLE, null = EDITABLE/DELETABLE
    private Integer permissionType;

    private Date updatedDate;

    @Column(name = "outdated", columnDefinition = "boolean default false")
    private boolean outdated;

    private Boolean groupTax = false;

    @Enumerated(EnumType.STRING)
    private TaxKeyEnum key;

    @Type(type = "jsonb")
    @Column(name = "faiCategorieIds", columnDefinition = "jsonb default '[]'")
    private List<Integer> faiCategorieIds;

    @Type(type = "jsonb")
    @Column(name = "faiPurchaseCategoryIds", columnDefinition = "jsonb default '[]'")
    private List<Integer> faiPurchaseCategoryIds;


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

    public BigDecimal getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(BigDecimal vatAmount) {
        this.vatAmount = vatAmount;
    }

    public Integer getTaxType() {
        return taxType;
    }

    public void setTaxType(Integer taxType) {
        this.taxType = taxType;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Set<EdsTaxComponent> getComponents() {
        return components;
    }

    public void setComponents(Set<EdsTaxComponent> components) {
        this.components = components;
    }

    public Set<EdsTaxGroupItem> getGroupItems() {
        return groupItems;
    }

    public void setGroupItems(Set<EdsTaxGroupItem> groupItems) {
        this.groupItems = groupItems;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }

    public boolean isOutdated() {
        return outdated;
    }

    public void setOutdated(boolean outdated) {
        this.outdated = outdated;
    }

    public Boolean getGroupTax() {
        return groupTax != null ? groupTax : false;
    }

    public void setGroupTax(Boolean groupTax) {
        this.groupTax = groupTax;
    }

    public TaxKeyEnum getKey() {
        return key;
    }

    public void setKey(TaxKeyEnum key) {
        this.key = key;
    }

    public boolean isSelectedByTaxDefault() {
        return selectedByTaxDefault;
    }

    public void setSelectedByTaxDefault(boolean selectedByDefault) {
        this.selectedByTaxDefault = selectedByDefault;
    }

    public String getTaxNameAndRateAsString() {

        if (TaxKeyEnum.EXEMPT.equals(getKey()) || TaxKeyEnum.OUT_OF_SCOPE.equals(getKey())) {
            return name;
        }
        FinancialSettingsManager companyManager = StaticContextAccessor.getBean(FinancialSettingsManager.class);

        if (companyManager.getFinancialSettings() != null) {
            Integer taxRateScale = companyManager.getFinancialSettings().getTaxRateScale();
            return name + "(" + getTaxRateAsBigDecimal().setScale(taxRateScale, BigDecimal.ROUND_HALF_UP) + "%)";
        } else {
            return name + "(" + getTaxRateAsBigDecimal().setScale(0, BigDecimal.ROUND_HALF_UP) + "%)";
        }
    }

    public String getTaxNameAndRateAsString(DecimalFormat format) {

        if (TaxKeyEnum.EXEMPT.equals(getKey()) || TaxKeyEnum.OUT_OF_SCOPE.equals(getKey())) {
            return name;
        }
        FinancialSettingsManager companyManager = StaticContextAccessor.getBean(FinancialSettingsManager.class);
        Integer taxRateScale = companyManager.getFinancialSettings().getTaxRateScale();
        return name + "(" + format.format(getTaxRateAsBigDecimal().setScale(taxRateScale, BigDecimal.ROUND_HALF_UP)) + "%)";
    }

    public double getTaxRate() {
        double vatPercent = 0;
        if (getGroupTax()) {
            for (EdsTaxGroupItem tgi : getGroupItems()) {
                vatPercent += tgi.getItem().getTaxRateAsBigDecimal().doubleValue();
            }
        } else {
            for (EdsTaxComponent c : getComponents()) {
                vatPercent += c.getRate() != null ? c.getRate().doubleValue() : 0;
            }
        }
        return vatPercent;
    }

    public BigDecimal getTaxRateAsBigDecimal() {
        BigDecimal vatPercent = AccountingConstants.ZERO;
        if (getGroupTax()) {
            for (EdsTaxGroupItem tgi : getGroupItems()) {
                vatPercent = vatPercent.add(tgi.getItem().getTaxRateAsBigDecimal());
            }
        } else {
            for (EdsTaxComponent c : getComponents()) {
                if (c.getRate() != null) {
                    vatPercent = vatPercent.add(c.getRate());
                }
            }
        }
        return vatPercent;
    }

    public double getEffectiveTaxRate() {
        return getEffectiveRateAsBigDecimal().doubleValue();
    }

    public BigDecimal getEffectiveRateAsBigDecimal() {
        if (getGroupTax()) {
            BigDecimal effectiveTaxRate = BigDecimal.ZERO;
            for (EdsTaxGroupItem tgi : getGroupItems()) {
                effectiveTaxRate = effectiveTaxRate.add(tgi.getItem().getEffectiveRateAsBigDecimal());
            }
            return effectiveTaxRate;
        } else {
            Double totalRateWithoutCompound = 0d, compoundRate = 0d;
            for (EdsTaxComponent tc : getComponents()) {
                if (tc.getCompound()) {
                    compoundRate = tc.getRate().doubleValue();
                } else {
                    totalRateWithoutCompound += tc.getRate().doubleValue();
                }
            }
            double effectiveTaxRate = compoundRate > 0d ? ((totalRateWithoutCompound / 100 + 1) * (compoundRate / 100) * 100 + totalRateWithoutCompound) : totalRateWithoutCompound;
            return new BigDecimal(effectiveTaxRate).setScale(ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
        }
    }

    public TaxItem createTaxItem() {
        TaxItem item = new TaxItem(objectID, getTaxNameAndRateAsString(), getTaxRateAsBigDecimal(), getEffectiveRateAsBigDecimal(), taxType);
        if (getKey() == null) {
            if (getName() != null && getName().contains("Zero Rate")) {
                item.setTaxKey(TaxKeyEnum.OUT_OF_SCOPE);
            } else if (getName() != null && getName().contains("Exempt")) {
                item.setTaxKey(TaxKeyEnum.EXEMPT);
            } else if (getName() != null && getName().contains("Out of Scope")) {
                item.setTaxKey(TaxKeyEnum.OUT_OF_SCOPE);
            }
        } else {
            item.setTaxKey(getKey());
        }
        return item;
    }

    public HashMap<Integer, BigDecimal> calculateAccountTaxes(BigDecimal netAmount, Integer taxCalculationType) {
        return calculateAccountTaxes(netAmount, taxCalculationType, null);
    }

    public HashMap<Integer, BigDecimal> calculateAccountTaxes(BigDecimal netAmount, Integer taxCalculationType, HashMap<Integer, BigDecimal> accountTaxesMap) {
        if (accountTaxesMap == null) {
            accountTaxesMap = new HashMap<>();
        }
        if (getGroupTax()) {
            for (EdsTaxGroupItem tgi : getGroupItems()) {
                accountTaxesMap = tgi.getItem().calculateAccountTaxes(netAmount, taxCalculationType, accountTaxesMap);
            }
        } else {
            BigDecimal compoundRate = BigDecimal.ZERO;
            EdsAccount compoundAccount = null;
            BigDecimal simpleTaxTotal = BigDecimal.ZERO;
            for (EdsTaxComponent tc : getComponents()) {
                if (tc.getCompound()) {
                    compoundRate = tc.getRate();
                    compoundAccount = tc.getAccount();
                } else {
                    BigDecimal calculatedTax = BigDecimal.ZERO;
                    if (TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
                        calculatedTax = netAmount.multiply(tc.getRate()).divide(AccountingConstants.HUNDRED.add(tc.getRate()), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    } else {
                        calculatedTax = netAmount.multiply(tc.getRate()).divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    }
                    Integer accountKey = (tc.getAccount() != null ? tc.getAccount().getObjectID() : -1);
                    if (accountTaxesMap.containsKey(accountKey)) {
                        accountTaxesMap.put(accountKey, accountTaxesMap.get(accountKey).add(calculatedTax));
                    } else {
                        accountTaxesMap.put(accountKey, calculatedTax);
                    }
                    simpleTaxTotal = simpleTaxTotal.add(calculatedTax);
                }
            }
            if (compoundRate.compareTo(BigDecimal.ZERO) != 0) {
                BigDecimal calculatedTax = BigDecimal.ZERO;
                if (TAX_CALCULATION_INCLUSIVE.equals(taxCalculationType)) {
                    calculatedTax = (netAmount.add(simpleTaxTotal))
                            .multiply(compoundRate)
                            .divide(AccountingConstants.HUNDRED.add(compoundRate), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                } else {
                    calculatedTax = (netAmount.add(simpleTaxTotal))
                            .multiply(
                                    compoundRate.divide(AccountingConstants.HUNDRED, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)
                            );
                }

                Integer accountKey = (compoundAccount != null ? compoundAccount.getObjectID() : -1);
                if (accountTaxesMap.containsKey(accountKey)) {
                    accountTaxesMap.put(accountKey, accountTaxesMap.get(accountKey).add(calculatedTax));
                } else {
                    accountTaxesMap.put(accountKey, calculatedTax);
                }
            }
        }
        return accountTaxesMap;
    }

    public Integer getFaiId() {
        return faiId;
    }

    public void setFaiId(Integer faiId) {
        this.faiId = faiId;
    }

    public EdsReference getFaiVat() {
        return faiVat;
    }

    public void setFaiVat(EdsReference faiVat) {
        this.faiVat = faiVat;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Integer> getFaiCategorieIds() {
        return faiCategorieIds != null ? faiCategorieIds : List.of();
    }

    public void setFaiCategorieIds(List<Integer> faiCategorieIds) {
        this.faiCategorieIds = faiCategorieIds;
    }

    public Integer getFaiPurchaseId() {
        return faiPurchaseId;
    }

    public void setFaiPurchaseId(Integer faiPurchaseId) {
        this.faiPurchaseId = faiPurchaseId;
    }

    public EdsReference getFaiPurchaseVat() {
        return faiPurchaseVat;
    }

    public void setFaiPurchaseVat(EdsReference faiPurchaseVat) {
        this.faiPurchaseVat = faiPurchaseVat;
    }

    public List<Integer> getFaiPurchaseCategoryIds() {
        return faiPurchaseCategoryIds != null ? faiPurchaseCategoryIds : List.of();
    }

    public void setFaiPurchaseCategoryIds(List<Integer> faiPurchaseCategoryIds) {
        this.faiPurchaseCategoryIds = faiPurchaseCategoryIds;
    }
}
