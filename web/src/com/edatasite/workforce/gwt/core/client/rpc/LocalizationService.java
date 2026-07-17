package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.core.domain.EdsLocalization;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Dilshod Madrahimov
 * Date: Nov 3, 2009 4:54:05 PM
 */
public interface LocalizationService extends RemoteService {

    Date getLastUpdatedDate();

    void update(String name, String language, String path, HashMap<String, String> map, HashMap<String, EdsLocalization> valueMap, boolean importFromCsv);

    ArrayList<EdsLocalization> getResourceData(String resourceName);

    class App {
        public static CommonServiceAsync get() {
            ServiceDefTarget target = GWT.create(LocalizationService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/localization");
            return (CommonServiceAsync) target;
        }
    }
}