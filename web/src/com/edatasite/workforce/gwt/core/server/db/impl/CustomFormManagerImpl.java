package com.edatasite.workforce.gwt.core.server.db.impl;


import com.edatasite.workforce.core.domain.customfields.EdsCustomFormCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.customform.EdsCustomItemTable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomFormManagerImpl extends BaseManager<EdsCustomForm> implements CustomFormManager {

    public CustomFormManagerImpl() {
        super(EdsCustomForm.class);
    }

    @Override
    public EdsCustomForm findByName(String name) {
        return (EdsCustomForm) findSingle("select cf from EdsCustomForm cf where cf.deleted is not true and lower(cf.name) = '" + name.toLowerCase() + "'");
    }

    @Override
    public void deleteCustom(Integer objectID, String formID) {

        updateNative("DELETE FROM " + getCompanyId() + ".custom_form_attributes  where customformid = " + objectID);

        updateNative("DELETE FROM " + getCompanyId() + ".custom_item_table  where id in (select cit.id from " + getCompanyId() + ".custom_item_table cit " +
                "left join " + getCompanyId() + ".form_item_table_setting itc on cit.uuid = itc.uuid where itc.form_Id = '" + formID + "')");

        updateNative("DELETE FROM " + getCompanyId() + ".custom_form_item c where c.form_id='" + formID + "'");


        updateNative("DELETE FROM " + getCompanyId() + ".reference c  where  c.code='" + WorkflowRule._WORKFLOW_MODULE + "_" + formID.replace("_FORM", "") + "'");
    }

    @Override
    public EdsCustomForm findByFormID(String formID) {
        return (EdsCustomForm) findSingle("select cf from EdsCustomForm cf where cf.deleted is not true and cf.formID = '" + formID + "'");
    }

    @Override
    public List<EdsCustomForm> findByContext(String context) {
        List<EdsCustomForm> list = find("select cf from EdsCustomForm cf left join cf.property p where cf.deleted is not true and p.active is true and p.moduleCode = '" + context + "'");
        if (list != null) {
            return list;
        }
        return new ArrayList<>();
    }

    @Override
    public List<EdsCustomItemTable> getCustomFormsByEmployeeId(Integer objectId) {
        String query = "SELECT DISTINCT cit.*  " +
                "FROM " +
                getCompanyId() + ".custom_form_item cfi " +
                "JOIN" +
                getCompanyId() + ".custom_form cf on cf.form_id = cfi.form_id " +
                "JOIN" +
                getCompanyId() + ".custom_item_table cit on cit.form_item_id = cfi.id " +
                "JOIN" +
                getCompanyId() + ".custom_item_table_customfields citc on citc.id = cit.customfieldsid " +
                "JOIN" +
                getCompanyId() + ".customform_customfields cc on cfi.form_customfieldsid = cc.id " +
                "WHERE cfi.deleted is not true and citc.string_value5 = '" +
                objectId + "'";
        return findNative(query,EdsCustomItemTable.class);
    }

    @Override
    public List<EdsCustomFormCustomFields> getCustomFormsCustomFieldsByEmployeeId(Integer objectId) {
        String query = "SELECT DISTINCT cc.*  " +
                "FROM " +
                getCompanyId() + ".custom_form_item cfi " +
                "JOIN" +
                getCompanyId() + ".custom_form cf on cf.form_id = cfi.form_id " +
                "JOIN" +
                getCompanyId() + ".custom_item_table cit on cit.form_item_id = cfi.id " +
                "JOIN" +
                getCompanyId() + ".custom_item_table_customfields citc on citc.id = cit.customfieldsid " +
                "JOIN" +
                getCompanyId() + ".customform_customfields cc on cfi.form_customfieldsid = cc.id " +
                "WHERE cfi.deleted is not true and citc.string_value5 = '" +
                objectId + "'";
        return findNative(query, EdsCustomFormCustomFields.class);
    }

@Override
    public List<EdsCustomForm> list(ListingFilterParameter fp) {
    String sql = "select distinct cf.* from " + getCompanyId() + ".custom_form cf " +
            /*sql.append(" left join " + getCompanyId() + ".form_item_table_setting its on cf.form_id = its.form_Id " );*/
            " where cf.deleted is null or cf.deleted is not true ";

        List<EdsCustomForm> list = findNative(sql, EdsCustomForm.class);

        return list;
    }

    @Override
    public List<EdsCustomForm> getForms() {
        String q = "select cf from EdsCustomForm cf where cf.deleted is null or cf.deleted is not true ";
        return (List<EdsCustomForm>) slaveEntityManager.createQuery(q).getResultList();
    }

    @Override
    public SelectItem[] getLookUpItems(ListingFilterParameter filterParametrs) {
        List<EdsCustomForm> getCustomForms = this.list(filterParametrs);

        SelectItem[] selectItems;

        selectItems = new SelectItem[getCustomForms.size()];
        int i = 0;
        for (EdsCustomForm customForm : getCustomForms) {
            selectItems[i] = new SelectItem(customForm.getObjectID(), customForm.getName(), customForm.getName());
            i++;
        }

        return selectItems;
    }
}
