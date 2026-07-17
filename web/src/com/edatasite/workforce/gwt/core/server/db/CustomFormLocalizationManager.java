package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;

import java.util.List;

public interface CustomFormLocalizationManager extends Manager<EdsCustomFormLocalization> {

    List<EdsCustomFormLocalization> getPredefinedValues(Integer parentId);

    EdsCustomFormLocalization getByName(String defaultName, String formId);

    EdsCustomFormLocalization getByNameAndParent(String defaultName, Integer parentId);

    void deleteChildrenByParentId(Integer parentId);

    void deleteChildrenExceptGivenIds(List<Integer> ids, Integer parentId);

}
