package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsDependentCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.DependentCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 6/2/11
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("dependentCFManager")
public class DependentCFManagerImpl extends BaseManager<EdsDependentCustomFields> implements DependentCFManager {

    public DependentCFManagerImpl() {
        super(EdsDependentCustomFields.class);
    }
}
