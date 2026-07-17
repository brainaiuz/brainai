package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsSubsidiaryProduct;
import com.edatasite.workforce.gwt.core.server.db.accounting.SubsidiaryProductManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 22/11/12
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
@Repository("subsidiaryProductManager")
public class SubsidiaryProductManagerImpl extends BaseManager<EdsSubsidiaryProduct> implements SubsidiaryProductManager {
    public SubsidiaryProductManagerImpl() {
        super(EdsSubsidiaryProduct.class);
    }

    @Override
    public EdsSubsidiaryProduct getSubsidiaryByUniqueID(String subsidiaryProductUniqueID) {
        return (EdsSubsidiaryProduct)findSingle("select sp from EdsSubsidiaryProduct sp where sp.uniqNumber = ?", subsidiaryProductUniqueID);
    }
}
