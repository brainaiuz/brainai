package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.customform.EdsModelFieldCustom;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 08-Feb-2014
 * Time: 19:09:00
 * To change this template use File | Settings | File Templates.
 */
public interface ModelFieldManager extends Manager<EdsModelField> {

    List<ModelField> getFields(String formID);

    List<EdsModelField> getSpecificFields(String formID, ArrayList<String> fieldTypes);

    EdsModelField get(Integer objectID, boolean isCustom);

    Integer getMaxSortOrder(String formID);

    EdsModelField getByFieldID(String formID, String fieldID);

    void clearAllDeletedModelFields(String formId);

    Set<String> getFieldIDs(String formID);

    void deleteFieldsByFormID(String formID);

    EdsModelField getWorkflowField(String formID, String fieldID, String section, boolean defaultOne);

    Integer getMaxSortOrder(String formID, String section);

    List<EdsModelField> getModelFields(String formID);

    List<EdsModelField> getModelFieldsCustom(String formID);

    Integer getSectionFields(String form_id, String section);

    List<EdsModelField> getFieldsForWorkflowAlert(String formID);

    List<EdsModelField> getFieldsForWorkflowUpdate(String formID);

    List<EdsModelField> getFieldsForWorkflowEmployee(String formID);

    List<EdsModelField> getCustomFieldsForWorkflowAttributes(String formID);

    List<EdsModelFieldCustom> getModelFieldsBySection(String formID, String sectionName);
}
