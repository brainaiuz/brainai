package com.edatasite.workforce.gwt.core.server.servlets.csv;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: HaveANiceDay
 * Date: 02.12.11
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */

public class QueryListCSVHandler extends AbstractBaseCSVHandler {
    private static final String CUSTOM_ENTITY_ID = "customentityid";
    private static final String QUERY_NAME = "queryname";
    private static final String SQL_TEXT = "sqltext";

    @Autowired
    private UserManager userManager;

    @Override
    protected CSVTransferObject buildCSV(CSVTransferObject transferObject, Object dataClass) {
        if (transferObject == null) {
            transferObject = new CSVTransferObject();
        }

        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        filterParametrs.setAllByFilter(true);
        filterParametrs.setForCSVonly(true);

        transferObject.setTitles(CUSTOM_ENTITY_ID, QUERY_NAME, SQL_TEXT);
        transferObject.setTitlesSet(true);

        /*List<WebsiteQueryItem> queriesList = websiteService.getQueryList(filterParametrs).getList();
        for (WebsiteQueryItem item : queriesList) {
            transferObject.getRows().add(
                    new String[]{
                            item.getCustomEntityGUID() != null ? item.getCustomEntityGUID() : "",
                            item.getQueryName(),
                            item.getSqlText()
                    });
        }
        transferObject.getRows().get(0);*/
        return transferObject;
    }

    //    @Override
    public String getFileName() {
        EdsUser user = userManager.getUser();
        return user.getFirstName() + "_" + user.getLastName() + "_Queries";
    }

    @Override
    protected Object prepareRequest(HttpServletRequest request) {
        Map filterMap = request.getParameterMap();
        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter fp = new ListingFilterParameter();
        HashMap<String, String> paramsMap = fp.getRequestParams();
        for (Map map : (Iterable<Map>) filterMap.entrySet()) {
            Map.Entry entry = (Map.Entry) map;
            if (paramsMap.containsKey(entry.getKey())) {
                String[] value = (String[]) entry.getValue();
                paramsMap.put((String) entry.getKey(), value[0]);
            }
        }
        fp.setRequestParams(paramsMap);
        return fp;
    }
}
