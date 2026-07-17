package com.edatasite.shared.sms;

import com.edatasite.workforce.core.domain.EdsSmsSettings;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 12/11/12
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class MvaayooSmsProvider extends SmsProvider {

    private final String STATUS_SUCCESS = "status=0";

    public MvaayooSmsProvider(EdsSmsSettings edsSmsSettings, Map<String, String> replacements) {
        super(edsSmsSettings, replacements);
    }

    @Override
    protected boolean checkUrlResult(String response) {
        setResponse(response);
        return response.toLowerCase().contains(STATUS_SUCCESS.toLowerCase());
    }
}
