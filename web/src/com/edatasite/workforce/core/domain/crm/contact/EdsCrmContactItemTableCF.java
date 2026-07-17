package com.edatasite.workforce.core.domain.crm.contact;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "crmContactitemcustomfields")
public class EdsCrmContactItemTableCF extends EdsCustomFields {
}
