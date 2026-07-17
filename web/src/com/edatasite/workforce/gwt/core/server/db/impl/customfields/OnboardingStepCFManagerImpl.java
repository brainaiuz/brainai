package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsOnboardingStepCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.OnboardingStepCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 11-Nov-2010
 * Time: 17:23:19
 */
@Repository("onboardingStepCFManager")
public class OnboardingStepCFManagerImpl extends BaseManager<EdsOnboardingStepCustomFields> implements OnboardingStepCFManager {
    public OnboardingStepCFManagerImpl() {
        super(EdsOnboardingStepCustomFields.class);
    }
}
