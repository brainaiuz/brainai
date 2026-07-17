package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 11-Nov-2010
 * Time: 17:10:38
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "onboardingstepcustomfields")
public class EdsOnboardingStepCustomFields extends EdsCustomFields {
}
