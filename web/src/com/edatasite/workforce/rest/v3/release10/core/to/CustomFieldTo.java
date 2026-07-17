package com.edatasite.workforce.rest.v3.release10.core.to;

import java.util.List;

public class CustomFieldTo {
    private Integer objectId;
    private String aliasName;
    private String customFormId;
    private LocaleDto locale;
    private List<QuizScoreDto> customFieldScoreValues;

    public CustomFieldTo() {
    }

    public CustomFieldTo(Integer objectId, String aliasName, String customFormId, List<QuizScoreDto> customFieldScoreValues) {
        this.objectId = objectId;
        this.aliasName = aliasName;
        this.customFormId = customFormId;
        this.customFieldScoreValues = customFieldScoreValues;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getCustomFormId() {
        return customFormId;
    }

    public void setCustomFormId(String customFormId) {
        this.customFormId = customFormId;
    }

    public List<QuizScoreDto> getCustomFieldScoreValues() {
        return customFieldScoreValues;
    }

    public void setCustomFieldScoreValues(List<QuizScoreDto> customFieldScoreValues) {
        this.customFieldScoreValues = customFieldScoreValues;
    }

    public LocaleDto getLocale() {
        return locale;
    }

    public void setLocale(LocaleDto locale) {
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "CustomFieldTo{" +
                "objectId=" + objectId +
                ", aliasName='" + aliasName + '\'' +
                ", customFormId='" + customFormId + '\'' +
                ", customFieldScoreValues=" + customFieldScoreValues +
                '}';
    }
}
