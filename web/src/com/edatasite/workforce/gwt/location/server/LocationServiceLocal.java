package com.edatasite.workforce.gwt.location.server;

import com.edatasite.workforce.gwt.core.client.rpc.ChartNode;
import com.edatasite.workforce.gwt.core.client.rpc.CompLocationRpc;

import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 23, 2010
 * Time: 7:39:50 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LocationServiceLocal {
    Integer saveLocation(CompLocationRpc compLocationRpc);

    CompLocationRpc getLocation(Integer locationId);

    Integer updateLocation(CompLocationRpc locationRpc);

    void saveEmployeeLocation(Set<Integer> locationMembers, Integer objectID, boolean isChecked);

    List<ChartNode> getLocationNodes();
}
