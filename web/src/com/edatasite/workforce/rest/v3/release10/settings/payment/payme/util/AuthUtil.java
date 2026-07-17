package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.util;

import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
public class AuthUtil {
    @Autowired
    protected InvoicingSettingsManager invoicingSettingsManager;

    public boolean isAuthorizedKpiVersion(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Basic ")) {
            return false;
        }
        String encodedCredentials = authHeader.substring(6).trim();

        try {
            String decoded = new String(Base64.getDecoder().decode(encodedCredentials), StandardCharsets.UTF_8);
            // decoded: "Paycom:6@iBC5F%kp8HGb%RokpydtAP@SixXHhQJNla"
            String expectedKey = invoicingSettingsManager.getInvoiceSettings().getPaymeServiceId();

            String expected = "Paycom:" + expectedKey;

            return expected.equals(decoded);

        } catch (Exception e) {
            return false;
        }
    }
}
