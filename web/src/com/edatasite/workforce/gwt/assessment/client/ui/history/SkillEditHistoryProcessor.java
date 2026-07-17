package com.edatasite.workforce.gwt.assessment.client.ui.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.hrms.client.SkillEditSinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: Abror Abdukadirov
 * Date: 10.07.2017 17:17
 */
public class SkillEditHistoryProcessor implements HistoryProcessor {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SkillEditSinksContainer(containerName, hrmsStrings.editSkill(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return null;
    }
}
