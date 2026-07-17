package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * The type Order.
 */
public record Order(

        @JsonProperty("catalog_id") String catalogId,

        @JsonProperty("product_items") List<Product> productItems,

        @JsonProperty("text") String text) {
}