package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsCustomFormSection;

import java.util.List;


public interface CustomFormSectionManager extends Manager<EdsCustomFormSection> {


    EdsCustomFormSection getCustomizeFormSection(String form_id, String section);

    List<EdsCustomFormSection> getSections(String form_id);

    Integer getSorder(String formID);

    EdsCustomFormSection getSectionByName(String formID, String name);

    Long getDefaultPageName(String formId);
}
