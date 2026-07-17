package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBackendManagement;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 4/23/12
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BackendManagementManager extends Manager<EdsBackendManagement> {

	List<EdsBackendManagement> getBackendManagements(ListingFilterParameter fp);

	Integer getBackendManagementsCount(ListingFilterParameter fp);

    EdsBackendManagement getBackendManagement(Integer companyID, Integer userID);

}