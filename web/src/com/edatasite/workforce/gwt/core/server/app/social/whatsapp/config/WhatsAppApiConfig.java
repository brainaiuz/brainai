package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.config;

public class WhatsAppApiConfig {
    /**
     * The constant API_VERSION.
     */
    public final static String API_VERSION = "v18.0";

    public final static String SOLUTION_PHONE_NUMBER_ID = "143145362223245";
    /**
     * The constant BASE_DOMAIN.
     */
    public static String BASE_DOMAIN = "https://graph.facebook.com";

    /**
     * Sets base domain.
     *
     * @param baseDomain the base domain
     */
    public static void setBaseDomain(String baseDomain) {
        BASE_DOMAIN = baseDomain;
    }
}
