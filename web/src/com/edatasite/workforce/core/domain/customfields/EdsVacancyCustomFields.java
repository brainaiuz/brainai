package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by dilsh0d on 21.01.16.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "vacancycustomfields")
public class EdsVacancyCustomFields extends EdsCustomFields {
}
