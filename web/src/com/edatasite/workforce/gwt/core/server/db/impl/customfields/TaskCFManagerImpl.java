package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsTaskCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.TaskCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 11-Nov-2010
 * Time: 17:23:19
 */
@Repository("taskCFManager")
public class TaskCFManagerImpl extends BaseManager<EdsTaskCustomFields> implements TaskCFManager {
    public TaskCFManagerImpl() {
        super(EdsTaskCustomFields.class);
    }
}
