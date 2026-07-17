package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBugReport;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Dec 11, 2008
 * Time: 1:36:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BugReportManager extends Manager<EdsBugReport> {
    List<EdsBugReport> getBugLists(ListingFilterParameter fp);

    List<Object[]> getBugsPerEmployees(String newStatusRef, String resolvedStatusRef, String underInvestStatusRef,
                                       String inProgressStatusRef, String ignoredStatusRef, String doneStatusRef, ListingFilterParameter fp);

    List<Object[]> getBugsPerSection(String newStatusRef, String resolvedStatusRef, String underInvestStatusRef,
                                     String inProgressStatusRef, String ignoredStatusRef, String doneStatusRef, ListingFilterParameter fp);

    List<EdsBugReport> getFeedBacksByUser(Integer userID);

    int getBugsPerEmployeesCount(String newStatusRef, String resolvedStatusRef, String underInvestStatusRef, String inProgressStatusRef, String ignoredStatusRef, String doneStatusRef, ListingFilterParameter fp);
}
