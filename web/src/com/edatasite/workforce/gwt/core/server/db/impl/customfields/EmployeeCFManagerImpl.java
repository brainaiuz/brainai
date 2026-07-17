package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsEmployeeCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.EmployeeCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 6/2/11
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("employeeCFManager")
public class EmployeeCFManagerImpl extends BaseManager<EdsEmployeeCustomFields> implements EmployeeCFManager {

    public EmployeeCFManagerImpl() {
        super(EdsEmployeeCustomFields.class);
    }
}
