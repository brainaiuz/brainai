package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Omonullo Abdullaev on 03.03.16.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "meetingminutescustomfields")
public class EdsMeetingMinutesCustomFields extends EdsCustomFields{
}
