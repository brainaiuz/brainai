package com.edatasite.workforce.gwt.assessment.client.ui.history;

import com.edatasite.workforce.gwt.assessment.client.SkillAddSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

/**
 * User: Admin
 * Date: 15.07.2009
 * Time: 19:09:33
 */
public class SkillHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new SkillAddSinksContainer(containerName + strings[0], hrmsStrings.addSkill(), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new SkillAddSinksContainer("addSkill", hrmsStrings.addSkill(), params);
    }
}
