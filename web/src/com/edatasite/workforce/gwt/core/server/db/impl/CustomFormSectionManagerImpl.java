package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customform.EdsCustomFormSection;
import com.edatasite.workforce.gwt.core.server.db.CustomFormSectionManager;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository ("customFormSectionManager")
public class CustomFormSectionManagerImpl extends BaseManager<EdsCustomFormSection> implements CustomFormSectionManager {

    public CustomFormSectionManagerImpl() {
        super(EdsCustomFormSection.class);
    }

    @Override
    public EdsCustomFormSection getCustomizeFormSection(String form_id, String section) {
        return (EdsCustomFormSection) findSingle("select cs from EdsCustomFormSection cs where cs.form_ID=? and cs.section=?", form_id, section);
    }

    @Override
    public List<EdsCustomFormSection> getSections(String form_id) {
        return find("select cs from EdsCustomFormSection cs where cs.form_ID=? order by cs.sorder ", form_id);
    }

    @Override
    public Integer getSorder(String formID) {
        return (Integer) findSingle("select max(cs.sorder) from EdsCustomFormSection cs where cs.form_ID=? ", formID);
    }

    @Override
    public EdsCustomFormSection getSectionByName(String formID, String name) {
        return (EdsCustomFormSection) findSingle("select cs from EdsCustomFormSection cs where cs.form_ID=? and (lower(cs.section)=? or lower(cs.label)=?) "
                , formID, (name != null ? name.toLowerCase() : ""), (name != null ? name.toLowerCase() : ""));
    }

    @Override
    public Long getDefaultPageName(String formId) {
        return (Long) findSingle("select count(cs) from EdsCustomFormSection cs where cs.form_ID=? and cs.section like '%Page-%'", formId);
    }
}
