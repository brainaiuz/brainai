package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCaseSolution;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 14, 2009
 * Time: 4:01:30 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CaseSolutionManager extends Manager<EdsCaseSolution> {
    List<EdsCaseSolution> getCaseSolutions(Integer caseId);

    List<EdsCaseSolution> getCaseSolutions(ListingFilterParameter fp);

    EdsCaseSolution getSolutionCase(Integer solutionId);
}
