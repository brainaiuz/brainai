package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Normurod on 7/9/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "daily_depreciation_rate")
public class EdsDailyDepreciationRate extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Date financialYearStart;
    private Date financialYearEnd;

    @Column(precision = 25, scale = 5)
    private BigDecimal dailyDepreciation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixedAssetId")
    private EdsFixedAsset fixedAsset;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getFinancialYearStart() {
        return financialYearStart;
    }

    public void setFinancialYearStart(Date financialYearStart) {
        this.financialYearStart = financialYearStart;
    }

    public Date getFinancialYearEnd() {
        return financialYearEnd;
    }

    public void setFinancialYearEnd(Date financialYearEnd) {
        this.financialYearEnd = financialYearEnd;
    }

    public BigDecimal getDailyDepreciation() {
        return dailyDepreciation;
    }

    public void setDailyDepreciation(BigDecimal dailyDepreciation) {
        this.dailyDepreciation = dailyDepreciation;
    }

    public EdsFixedAsset getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(EdsFixedAsset fixedAsset) {
        this.fixedAsset = fixedAsset;
    }
}
