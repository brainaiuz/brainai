package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Faxriddin Taslimov on 14/08/2019.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "reportdatacustomfields")
public class EdsReportDataCustomFields extends EdsCustomFields {
}
