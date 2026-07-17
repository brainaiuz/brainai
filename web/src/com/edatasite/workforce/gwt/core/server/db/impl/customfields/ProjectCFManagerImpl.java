package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsProjectCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.ProjectCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 24-Nov-2010
 * Time: 15:43:33
 */
@Repository("projectCFManager")
public class ProjectCFManagerImpl extends BaseManager<EdsProjectCustomFields> implements ProjectCFManager {
    public ProjectCFManagerImpl() {
        super(EdsProjectCustomFields.class);
    }
}
