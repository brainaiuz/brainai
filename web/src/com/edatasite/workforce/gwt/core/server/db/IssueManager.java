package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;


/**
 * Created by IntelliJ IDEA. User: Acer Date: 07-Jan-2008 Time: 15:07:58 To
 * change this template use File | Settings | File Templates.
 */

public interface IssueManager extends Manager<EdsIssue> {
    List<EdsIssue> list();

    List<EdsIssue> list(ListingFilterParameter fp);

    List getProjectIssue(ListingFilterParameter fp);

    List getProjectIssueStatistic(ListingFilterParameter fp);

    List<EdsIssue> getProjectIssues(Integer projectID);

    Integer getIssuesLastIntNumber();

    NumberData generateIssueNumber();

    boolean isIssueNumberExists(String number, Integer objectId);
}
