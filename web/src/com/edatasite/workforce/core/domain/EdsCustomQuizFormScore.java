package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "companyCustomQuizFormScores")
public class EdsCustomQuizFormScore extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "custom_form_id")
    private String customFormId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EdsEmployee employee;

    @Column(name = "total_score")
    private BigDecimal totalScore;

    @Column(name = "created_date")
    private Date createdDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_form_item_id")
    private EdsCustomFormItems customFormItemId;

    @Override
    public Integer getObjectID() {
        return this.objectID;
    }

    public String getCustomFormId() {
        return customFormId;
    }

    public void setCustomFormId(String customFormId) {
        this.customFormId = customFormId;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public EdsCustomFormItems getCustomFormItemId() {
        return customFormItemId;
    }

    public void setCustomFormItemId(EdsCustomFormItems customFormItemId) {
        this.customFormItemId = customFormItemId;
    }
}
