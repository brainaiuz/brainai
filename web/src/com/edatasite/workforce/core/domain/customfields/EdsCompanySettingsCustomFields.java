package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Hurshid on 2/4/2016.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "companySettingscustomfields")
public class EdsCompanySettingsCustomFields extends EdsCustomFields {
}
