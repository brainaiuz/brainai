package com.edatasite.workforce.rest.v3.release10.core.to.hrms;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceValueItems;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class VacancyQuestionDTO extends ResponseData {
    private Integer fieldId;
    private String fieldName;
    private String uiType;

    private Integer questionId;
    private String question;
    private String questionUz;
    private String questionEn;
    private String questionRu;
    private ReferenceValueItems[] referenceItems;


    public VacancyQuestionDTO() {
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


    public String getUiType() {
        return uiType;
    }

    public void setUiType(String uiType) {
        this.uiType = uiType;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionUz() {
        return questionUz;
    }

    public void setQuestionUz(String questionUz) {
        this.questionUz = questionUz;
    }

    public String getQuestionEn() {
        return questionEn;
    }

    public void setQuestionEn(String questionEn) {
        this.questionEn = questionEn;
    }

    public String getQuestionRu() {
        return questionRu;
    }

    public void setQuestionRu(String questionRu) {
        this.questionRu = questionRu;
    }

    public ReferenceValueItems[] getReferenceItems() {
        return referenceItems;
    }

    public void setReferenceItems(ReferenceValueItems[] referenceItems) {
        this.referenceItems = referenceItems;
    }
}
