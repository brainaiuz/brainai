/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/19 0:55:46                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCaseSolution;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CaseSolutionManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 14, 2009
 * Time: 3:59:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("caseSolutionManager")
public class CaseSolutionManagerImpl extends BaseManager<EdsCaseSolution> implements CaseSolutionManager {
    public CaseSolutionManagerImpl() {
        super(EdsCaseSolution.class);
    }

    public List<EdsCaseSolution> getCaseSolutions(Integer caseId) {
        return find("select distinct cs from EdsCaseSolution cs" + " where cs.crmCase.objectID= ? and (cs.deleted is null or cs.deleted =?)", caseId, false);
    }

    public List<EdsCaseSolution> getCaseSolutions(ListingFilterParameter fp) {
        StringBuilder s = new StringBuilder();
        s.append("select distinct cs from EdsCaseSolution cs");
        if (fp != null && fp.getAccountID() != null && !"".equals(fp.getAccountID())) {
            s.append(" where (cs.crmCase.deleted <> true and cs.crmCase.crmAccount is not null and cs.crmCase.crmAccount.objectID = " + fp.getAccountID() + " and cs.crmCase.crmAccount.deleted <> true) and");
        } else {
            s.append(" where ");
        }
        s.append(" (cs.deleted is null or cs.deleted <> true) ");
        return find(s.toString());
    }

    public EdsCaseSolution getSolutionCase(Integer solutionId) {
        return (EdsCaseSolution) findSingle("select distinct cs from EdsCaseSolution cs" + " where cs.solution.objectID=? and (cs.deleted is null or cs.deleted =?)", solutionId, false);
    }
}
