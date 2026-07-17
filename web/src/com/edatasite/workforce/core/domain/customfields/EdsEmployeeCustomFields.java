package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 6/1/11
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeecustomfields")
public class EdsEmployeeCustomFields extends EdsCustomFields {
}
