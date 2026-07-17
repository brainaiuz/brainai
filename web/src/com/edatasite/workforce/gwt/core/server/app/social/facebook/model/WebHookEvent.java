package com.edatasite.workforce.gwt.core.server.app.social.facebook.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public record WebHookEvent(
        @JsonProperty("entry") List<Entry> entry,
        @JsonProperty("object") String object
) implements IsSerializable {

    public record Entry(
            @JsonProperty("id") String id,
            @JsonProperty("time") Long time,
            @JsonProperty("changes") List<Change> changes
    ) implements IsSerializable {}

    public record Change(
            @JsonProperty("field") String field,
            @JsonProperty("value") Value value
    ) implements IsSerializable {}

    public record Value(
            @JsonProperty("form_id") String form_id,
            @JsonProperty("leadgen_id") String leadgen_id,
            @JsonProperty("created_time") Long created_time,
            @JsonProperty("page_id") String page_id,
            @JsonProperty("adgroup_id") String adgroup_id,
            @JsonProperty("ad_id") String ad_id
    ) implements IsSerializable {}
}
