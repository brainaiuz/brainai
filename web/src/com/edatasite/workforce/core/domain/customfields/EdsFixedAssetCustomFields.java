package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Dilshod Madrahimov
 * Date: 8/23/14
 * Time: 8:15 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "fixedassetcustomfields")
public class EdsFixedAssetCustomFields extends EdsCustomFields {

}
