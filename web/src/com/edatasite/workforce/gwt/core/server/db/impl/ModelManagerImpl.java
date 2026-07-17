package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customform.EdsModel;
import com.edatasite.workforce.core.domain.customform.EdsModelCustom;
import com.edatasite.workforce.core.domain.customform.EdsModelDefault;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.customform.EdsModelFieldCustom;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.server.db.ModelManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 08-Feb-2014
 * Time: 19:09:55
 * To change this template use File | Settings | File Templates.
 */
@Repository("modelManager")
public class ModelManagerImpl extends BaseManager<EdsModel> implements ModelManager {

    public ModelManagerImpl() {
        super(EdsModel.class);
    }

    @Override
    public EdsModel get(String formID) {
        EdsModelCustom model = (EdsModelCustom) findSingle("select l from EdsModelCustom l where l.formID = ? and l.active = true", formID);
        if (model != null) {
            return model;
        }
        return (EdsModelDefault) findSingle("select l from EdsModelDefault l where l.formID = ? and l.active = true", formID);
    }

    @Override
    public EdsModel get(Integer objectID, boolean isCustom) {
        String table = "EdsModel" + (isCustom ? "Custom" : "Default");
        return (EdsModel) findSingle("select l from " + table + " l where l.objectID = " + objectID);
    }

    @Override
    public EdsModel get(String formID, boolean isCustom) {
        String table = "EdsModel" + (isCustom ? "Custom" : "Default");
        return (EdsModel) findSingle("select l from " + table + " l where l.formID = '" + formID + "'");
    }

    @Override
    public List<EdsModel> getModelList(String viewName) {
        List<EdsModel> model = find("select l from EdsModelCustom l where l.viewName = ? and l.active = true", viewName);
        if (model != null && model.size() > 0) {
            return model;
        }
        return find("select l from EdsModelDefault l where l.viewName = ? and l.active = true", viewName);
    }

    @Override
    public void copyForm(Integer fromCompanyID, Integer toCompanyID, String formID) {
        if (fromCompanyID != null && toCompanyID != null && formID != null) {
            EdsModelCustom model = (EdsModelCustom) findNativeSingle("select * from \"" + fromCompanyID + "\".model where active is true and formid = '" + formID + "'", EdsModelCustom.class);
            if (model != null) {
                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO \"").append(toCompanyID).append("\".model (active,formID,title,viewname) VALUES ");
                sql.append("(").append(model.isActive()).append(",'").append(model.getFormID()).append("','").append(model.getTitle()).append("','").append(model.getViewName()).append("')");
                updateNative(sql.toString());
            }
            List<EdsModelFieldCustom> modelFields = (List<EdsModelFieldCustom>) findNative("select * from \"" + fromCompanyID + "\".modelField where form_id = '" + formID + "'", EdsModelFieldCustom.class);
            if (modelFields != null && modelFields.size() > 0) {
                int i = 0;
                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO \"").append(toCompanyID).append("\".modelField (form_ID,field_ID,mandatory,hide,isCustomField,widget," +
                        "systemmandatory,nolabelfor,usableByWorkflow,label,source,type,disableUpdate,isEntityField) VALUES ");
                for (EdsModelField field : modelFields) {
                    sql.append("(").append(field.getForm_ID() != null ? "'" + field.getForm_ID() + "'" : null).append(",");
                    sql.append(field.getField_ID() != null ? "'" + field.getField_ID() + "'" : null).append(",");
                    /*sql.append(field.getSorder()).append(",");*/
                    sql.append(field.isMandatory()).append(",");
                    sql.append(field.isHide()).append(",");
                    sql.append(field.isCustomField()).append(",");
                    sql.append(field.getWidget() != null ? "'" + field.getWidget() + "'" : null).append(",");
                    sql.append(field.isSystemMandatory()).append(",");
                    sql.append(field.getNoLabelFor() != null ? "'" + field.getNoLabelFor() + "'" : null).append(",");
                    sql.append(field.isUsableByWorkflow()).append(",");
                    sql.append(field.getLabel() != null ? "'" + field.getLabel() + "'" : null).append(",");
                    sql.append(field.getSource() != null ? "'" + field.getSource() + "'" : null).append(",");
                    sql.append(field.getType() != null ? "'" + field.getType() + "'" : null).append(",");
                    sql.append(field.isDisableUpdate()).append(",");
                    sql.append(field.isEntityField()).append(")");
                    i++;
                    if (i < modelFields.size()) {
                        sql.append(", ");
                    }
                }
                updateNative(sql.toString());
            }
        }
    }

    @Override
    public List<EdsModel> getStepForms(String formID, boolean employeeInclude) {
        StringBuilder sql = new StringBuilder();
        sql.append(" where ").append("(l.stepForm is true").append(employeeInclude ? " or l.formID in ('" + LayoutRPC.HRMS_EMPLOYEE_FORM + "','" + LayoutRPC.CANDIDATE_FORM + "')" : "").append(") ");
        sql.append(formID != null ? " and l.formID != '" + formID + "' " : " ");
        sql.append(" and l.active is true");
        List<EdsModel> model = find("select l from EdsModelCustom l " + sql);
        if (model != null && model.size() > 0) {
            return model;
        }
        return find("select l from EdsModelDefault l " + sql);
    }

    @Override
    public EdsModel getStepForm(String formID) {
        return (EdsModel) findSingle("select l from EdsModelCustom l where l.formID = '" + formID + "' AND l.stepForm is true AND l.active is true");
    }

    @Override
    public EdsModel getCustomFormModel(String entityName) {
        return (EdsModel) findSingle("select l from EdsModelCustom l where l.viewName = '" + entityName + "' AND l.active is true");
    }

    @Override
    public EdsModel getCustomForm(String formId) {
        return (EdsModel) findSingle("select l from EdsModelCustom l where l.formID = '" + formId + "' AND l.customForm is true AND l.active is true");
    }
}
