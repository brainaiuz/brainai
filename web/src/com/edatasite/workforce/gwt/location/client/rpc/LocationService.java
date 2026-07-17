package com.edatasite.workforce.gwt.location.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.ChartNode;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeLocationItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 01.12.2009
 * Time: 19:08:57
 * To change this template use File | Settings | File Templates.
 */
public interface LocationService extends RemoteService {

    SelectItem[] getCountryList();

    Integer updateLocation(CompLocationRpc location);

    LocationList getLocation(ListLoadConfig config, ListingFilterParameter filterParametrs);

    ListResult<CompLocationRpc> getLocations(ListingFilterParameter  filterParameter);

    EmployeeLocation getLocationAndEmployees(Integer locationId);

    CompLocationRpc getLocation(Integer locationId);

    LocationList getLocationMaps(ListingFilterParameter fp);

    void deleteLocation(Integer locationID);

    SelectItem[] getLocationsAsSelectItem(ListingFilterParameter fp);

    ListResult<EmployeeLocationItem> getEmployeelocations(ListingFilterParameter filterParameter);

    Boolean deleteEmployeeLocation(Integer objectID);

    EmployeeLocationItem getEmployeeLocation(Integer objectID);

    void saveEmployeeLocation(EmployeeLocationItem item);

    List<ChartNode> getLocationNodes();

    SelectItem getLocationAsSelectItem(Integer locationId);

    NumberData generateLocationNumber();

    SelectItem[] getLocationOwnersByRole();

    /**
     * Utility/Convenience class.
     * Use LocationService.App.getInstance() to access static instance of LocationServiceAsync
     */
    class App {
        public static LocationServiceAsync get() {
            ServiceDefTarget target = GWT.create(LocationService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/location");
            return (LocationServiceAsync) target;
        }
    }
}
