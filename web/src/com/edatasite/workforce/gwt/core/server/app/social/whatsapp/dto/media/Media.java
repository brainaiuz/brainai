package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.media;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Media.
 */
public record Media(

        @JsonProperty("sha256") String sha256,

        @JsonProperty("mime_type") FileType mimeType,

        @JsonProperty("messaging_product") String messagingProduct,

        @JsonProperty("id") String id,

        @JsonProperty("url") String url,

        @JsonProperty("file_size") long fileSize) {
}