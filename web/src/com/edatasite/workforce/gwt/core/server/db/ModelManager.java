package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsModel;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 08-Feb-2014
 * Time: 19:09:00
 * To change this template use File | Settings | File Templates.
 */
public interface ModelManager extends Manager<EdsModel> {
    EdsModel get(String formID);

    EdsModel get(Integer objectID, boolean isCustom);

    EdsModel get(String formID, boolean isCustom);

    List<EdsModel> getModelList(String viewName);

    void copyForm(Integer fromCompanyID, Integer toCompanyID, String formID);

    List<EdsModel> getStepForms(String formID, boolean employeeInclude);

    EdsModel getStepForm(String formID);

    EdsModel getCustomFormModel(String entityName);

    EdsModel getCustomForm(String formId);
}
