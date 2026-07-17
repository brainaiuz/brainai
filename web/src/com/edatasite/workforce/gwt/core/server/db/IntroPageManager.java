package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsIntroductionPage;

public interface IntroPageManager extends Manager<EdsIntroductionPage> {
    EdsIntroductionPage findByParentFormId(String parentFormId);
    boolean deleteByIdAndParentForm(String parentFormId, Integer id);
}
