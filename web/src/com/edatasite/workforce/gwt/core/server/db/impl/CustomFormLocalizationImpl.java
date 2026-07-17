package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CustomFormLocalizationManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("customFormLocalization")
public class CustomFormLocalizationImpl extends BaseManager<EdsCustomFormLocalization> implements CustomFormLocalizationManager {
    public CustomFormLocalizationImpl() {
        super(EdsCustomFormLocalization.class);
    }

    @Override
    public List<EdsCustomFormLocalization> getPredefinedValues(Integer parentId) {
        return find("select cfl from EdsCustomFormLocalization cfl where (cfl.deleted=false or cfl.deleted is null) and cfl.parent.objectID=? order by cfl.objectID", parentId);
    }

    @Override
    public EdsCustomFormLocalization getByName(String defaultName, String formId) {
        return (EdsCustomFormLocalization) findSingle("select cfl from EdsCustomFormLocalization cfl where (cfl.deleted=false or cfl.deleted is null) and cfl.defaultName=? and cfl.formId=?", defaultName, formId);
    }

    @Override
    public EdsCustomFormLocalization getByNameAndParent(String defaultName, Integer parentId) {
        return (EdsCustomFormLocalization) findSingle("select cfl from EdsCustomFormLocalization cfl where (cfl.deleted=false or cfl.deleted is null) and cfl.defaultName=? and cfl.parent.objectID=?", defaultName, parentId);
    }

    @Override
    public void deleteChildrenByParentId(Integer parentId) {
        update("update EdsCustomFormLocalization cfl set cfl.deleted = true where cfl.parent.id=?", parentId);
    }

    @Override
    public void deleteChildrenExceptGivenIds(List<Integer> ids, Integer parentId) {
        update("update EdsCustomFormLocalization cfl set cfl.deleted = true where cfl.parent.objectID=? and cfl.objectID not in(" + ServerUtils.getAsCommoDelimited(ids, "0") + ")", parentId);
    }
}
