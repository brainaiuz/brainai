package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsPerformanceNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User: Ilhombek
 * Date: 10/24/12
 * Time: 6:46 PM
 */
public interface PerformanceNoteManager extends Manager<EdsPerformanceNote> {

    List<EdsPerformanceNote> getList(ListingFilterParameter fp);

    List<EdsPerformanceNote> getIncidentList(ListingFilterParameter fp);

    List<EdsPerformanceNote> getIncidentListByEmployee(ListingFilterParameter fp, Integer employeeId);
}