package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.gwt.assessment.client.rpc.BonusSettingsItem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Sher
 * Date: 7/24/12
 * Time: 4:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bonus_settings")
public class EdsBonusSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private EdsValidityPeriod validityPeriod;

    @Column(name = "budget_id")
    private String budgetId;

    private Double budgetAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "bonus_settings_score_item",
            joinColumns = {@JoinColumn(name = "bonus_settings_id")},
            inverseJoinColumns = {@JoinColumn(name = "scoreitems_id")})
    private Set<EdsScoreItem> scoreItems = new HashSet<>();

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean deleted = false;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean enableForcedDistributionRanking = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(EdsValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(String budgetId) {
        this.budgetId = budgetId;
    }

    public Double getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(Double budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public Set<EdsScoreItem> getScoreItems() {
        return scoreItems;
    }

    public void setScoreItems(Set<EdsScoreItem> scoreItems) {
        this.scoreItems = scoreItems;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public boolean isEnableForcedDistributionRanking() {
        return enableForcedDistributionRanking;
    }

    public void setEnableForcedDistributionRanking(boolean enableForcedDistributionRanking) {
        this.enableForcedDistributionRanking = enableForcedDistributionRanking;
    }

    public BonusSettingsItem getDTO() {
        BonusSettingsItem item = new BonusSettingsItem();

        item.setObjectId(objectID);
        item.setValidityPeriod(validityPeriod.getAsSelectItem(null));
        item.setBudgetId(budgetId);
        item.setBudgetAmount(budgetAmount);
        item.setEnableForcedDistributionRanking(isEnableForcedDistributionRanking());
        if (getStatus() != null) {
            item.setStatus(getStatus().getAsSelectItem());
            item.setStatusCode(getStatus().getCode());
        }
        for (EdsScoreItem score : getScoreItems()) {
            item.getScoreItemHashMap().put(score.getName(), score.getDTO());
        }
        return item;
    }
}
