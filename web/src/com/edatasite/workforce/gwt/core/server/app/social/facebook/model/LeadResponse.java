package com.edatasite.workforce.gwt.core.server.app.social.facebook.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LeadResponse {

    private String id;

    @JsonProperty("created_time")
    private String createdTime;

    @JsonProperty("field_data")
    private List<FieldData> fieldData;
    private String form_id;

    @JsonProperty("ad_id")
    private String adId;

    @JsonProperty("ad_name")
    private String adName;

    @JsonProperty("campaign_name")
    private String campaignName;

    @JsonProperty("platform")
    private String platform;

    public String getForm_id() {
        return form_id;
    }
    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCreatedTime() { return createdTime; }
    public void setCreatedTime(String createdTime) { this.createdTime = createdTime; }

    public List<FieldData> getFieldData() { return fieldData; }
    public void setFieldData(List<FieldData> fieldData) { this.fieldData = fieldData; }

    public String getAdId() { return adId; }
    public void setAdId(String adId) { this.adId = adId; }

    public String getAdName() { return adName; }
    public void setAdName(String adName) { this.adName = adName; }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCampaignName() { return campaignName; }
    public void setCampaignName(String campaignName) { this.campaignName = campaignName; }

    public static class FieldData {
        private String name;
        private List<String> values;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public List<String> getValues() { return values; }
        public void setValues(List<String> values) { this.values = values; }
    }
}
