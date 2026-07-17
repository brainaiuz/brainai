package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 27.03.2012
 * Time: 22:02:08
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "permission")
public class EdsPermission extends EdsPermissionTable {
}
