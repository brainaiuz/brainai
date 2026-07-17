package com.edatasite.workforce.gwt.core.server.db.impl.rbac.permission;

import com.edatasite.workforce.core.domain.rbac.permission.EdsTaskPermission;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.permission.TaskPermissionManager;
import org.springframework.stereotype.Repository;

/**
 * User: Abdulaziz
 * Date: Mar 4, 2010
 * Time: 4:35:30 PM
 */
@Repository("taskPermissionManager")
public class TaskPermissionManagerImpl extends BaseManager<EdsTaskPermission> implements TaskPermissionManager {
    public TaskPermissionManagerImpl() {
        super(EdsTaskPermission.class);
    }
}
