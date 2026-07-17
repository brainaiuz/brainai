package com.edatasite.workforce.core.domain.recruitment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "vacancy_questions")
public class EdsVacancyQuestion extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "field_id")
    private Integer fieldId;

    @Column(name = "fieldname")
    private String fieldName;

    @Column(name = "columnode")
    private String columnCode;

    @Column(name = "uitype")
    private String uiType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id")
    private EdsVacancy vacancy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private EdsReference questionReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookupreference_id")
    private EdsReference lookUpReference;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsReference getQuestionReference() {
        return questionReference;
    }

    public void setQuestionReference(EdsReference questionReference) {
        this.questionReference = questionReference;
    }

    public EdsVacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(EdsVacancy vacancy) {
        this.vacancy = vacancy;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }


    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    public EdsReference getLookUpReference() {
        return lookUpReference;
    }

    public void setLookUpReference(EdsReference lookUpReference) {
        this.lookUpReference = lookUpReference;
    }
}
