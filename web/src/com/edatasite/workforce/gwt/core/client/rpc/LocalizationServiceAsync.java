package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.core.domain.EdsLocalization;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface LocalizationServiceAsync {

    void getLastUpdatedDate(AsyncCallback<Date> async);

    void update(String name, String language, String path, HashMap<String, String> map, HashMap<String, EdsLocalization> valueMap, boolean importFromCsv, AsyncCallback<Void> async);

    void getResourceData(String resourceName, AsyncCallback<ArrayList<EdsLocalization>> async);
}
