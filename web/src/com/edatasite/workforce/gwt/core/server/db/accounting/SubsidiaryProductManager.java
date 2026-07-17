package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsSubsidiaryProduct;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 22/11/12
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
public interface SubsidiaryProductManager extends Manager<EdsSubsidiaryProduct> {
    EdsSubsidiaryProduct getSubsidiaryByUniqueID(String subsidiaryProductUniqueID);
}
