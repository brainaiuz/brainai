package com.edatasite.workforce.core.domain.customform;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Hayot on 2/7/14.
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "modelfield")
public class EdsModelFieldDefault extends EdsModelField {
}
