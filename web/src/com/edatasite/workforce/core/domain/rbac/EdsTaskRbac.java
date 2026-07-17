package com.edatasite.workforce.core.domain.rbac;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.rbac.history.EdsBaseTaskRbac;
import com.edatasite.workforce.core.domain.rbac.permission.EdsTaskPermission;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User: Abdulaziz
 * Date: Oct 7, 2009
 * Time: 3:10:54 PM
 */

/**
 * Role Based Access Control for EdsTask
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "taskrbac",
        indexes = {
                @Index(columnList = "userid", name = "taskrbac_userid_index"),
                @Index(columnList = "groupid", name = "taskrbac_groupid_index"),
                @Index(columnList = "taskid", name = "taskrbac_taskid_index")
        })
public class EdsTaskRbac extends EdsBaseTaskRbac {

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "taskPermissionId")
    private EdsTaskPermission taskPermission;

    public EdsTaskPermission getTaskPermission() {
        return taskPermission;
    }

    public void setTaskPermission(EdsTaskPermission taskPermission) {
        this.taskPermission = taskPermission;
    }
}
