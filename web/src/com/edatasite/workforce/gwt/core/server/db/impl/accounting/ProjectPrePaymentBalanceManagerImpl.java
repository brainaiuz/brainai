package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsProjectPrepaymentBalance;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProjectPrePaymentBalanceManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/3/11
 * Time: 8:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("projectPrePaymentBalance")
public class ProjectPrePaymentBalanceManagerImpl extends BaseManager<EdsProjectPrepaymentBalance> implements ProjectPrePaymentBalanceManager{
    public ProjectPrePaymentBalanceManagerImpl() {
        super(EdsProjectPrepaymentBalance.class);
    }

    public EdsProjectPrepaymentBalance getCustomerPrePaymentBalance(Integer customerID) {
        return (EdsProjectPrepaymentBalance) findSingle("select pb from EdsProjectPrepaymentBalance pb where pb.crmAccount.objectID = ?", customerID);
    }
}
