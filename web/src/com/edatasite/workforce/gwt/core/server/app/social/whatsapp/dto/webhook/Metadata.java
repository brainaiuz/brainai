package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Metadata.
 *
 * @param phoneNumberId      The ID of the phone number receiving the Webhooks. You can use this phone_number_id to send messages back to customers.
 * @param displayPhoneNumber The phone number of the business account that is receiving the Webhooks.
 */
public record Metadata(
/*
The ID of the phone number receiving the Webhooks. You can use this phone_number_id to send messages back to customers.
 */
        @JsonProperty("phone_number_id") String phoneNumberId,
/*
The phone number of the business account that is receiving the Webhooks.
 */
        @JsonProperty("display_phone_number") String displayPhoneNumber) {
}