/*
 * Copyright (c) 2022.
 * Bilol Boydedayev
 */

package com.edatasite.workforce.gwt.core.server.app.hmrc.constants;

public final class HmrcConstants {
    private HmrcConstants() {
    }

    public static final String CALLBACK_URL = "/hmrc/auth/callback";
    public static final String OAUTH_AUTHORIZE_URL = "/oauth/authorize";
    public static final String OAUTH_TOKEN_URL = "/oauth/token";
    public static final String VRN_LOOKUP_URL = "/organisations/vat/check-vat-number/lookup/${vrn}";
    public static final String VAT_OBLIGATIONS_URL = "/organisations/vat/{vrn}/obligations";
    public static final String VAT_RETURNS_URL = "/organisations/vat/{vrn}/returns";
}
