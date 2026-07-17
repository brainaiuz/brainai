package com.edatasite.workforce.gwt.location.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ChartNode;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeLocationItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 19:08:57
 * To change this template use File | Settings | File Templates.
 */
public interface LocationServiceAsync {

    Request getLocation(ListLoadConfig config, ListingFilterParameter filterParametrs, AsyncCallback<LocationList> async);

    void getCountryList(AsyncCallback<SelectItem[]> async);

    void updateLocation(CompLocationRpc location, AsyncCallback<Integer> async);

    void getLocationAndEmployees(Integer locationId, AsyncCallback<EmployeeLocation> async);

    void getLocation(Integer locationId, AsyncCallback<CompLocationRpc> async);

    void getLocationMaps(ListingFilterParameter fp, AsyncCallback<LocationList> async);

    void deleteLocation(Integer locationID, AsyncCallback<Void> callback);

    Request getLocations(ListingFilterParameter filterParameter, AsyncCallback<ListResult<CompLocationRpc>> async);

    void getLocationsAsSelectItem(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void generateLocationNumber(AsyncCallback<NumberData> callback);

    void getEmployeelocations(ListingFilterParameter filterParameter, AsyncCallback<ListResult<EmployeeLocationItem>> async);

    void deleteEmployeeLocation(Integer objectID, AsyncCallback<Boolean> async);

    void getEmployeeLocation(Integer objectID, AsyncCallback<EmployeeLocationItem> async);

    void saveEmployeeLocation(EmployeeLocationItem item, AsyncCallback<Void> async);

    void getLocationNodes(AsyncCallback<List<ChartNode>> async);

    void getLocationAsSelectItem(Integer locationId, AsyncCallback<SelectItem> async);

    void getLocationOwnersByRole(AsyncCallback<SelectItem[]> async);

}
