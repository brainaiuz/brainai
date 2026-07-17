package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.customform.EdsModelFieldCustom;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 08-Feb-2014
 * Time: 19:09:55
 * To change this template use File | Settings | File Templates.
 */
@Repository("modelFieldManager")
public class ModelFieldManagerImpl extends BaseManager<EdsModelField> implements ModelFieldManager {

    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private UserManager userManager;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    private static final String FORM_SUFFIX = "_FORM";

    public ModelFieldManagerImpl() {
        super(EdsModelField.class);
    }

    @Override
    @Transactional
    @Deprecated
    public List<ModelField> getFields(String formID) {

        List<ModelField> result;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("formID", formID);

        String selectfields = """
                 id,customlabel,defaultvalue,field_id,form_id,helpmessage,hide,iscustomfield,label,
                mandatory,sorder,systemmandatory,widget,source,isentityfield,nolabelfor,nowrapperfor,
                usablebyworkflow,type,fullwidth,place,split,fieldsetstyle,fieldstyle,halfsetstyle,rowstyle,
                disableupdate,hideincustomizeform,systemdisable,isworkflowattribute,columnType,forder\s""";

        List<EdsModelField> prFields = find("select t from EdsModelFieldCustom t where t.form_ID = ? order by t.forder", formID);
        if (prFields == null || prFields.size() == 0) {
            String qry = "select " + selectfields + " from " + getPublic() + ".modelfield where form_id=:formID order by sorder";
            result = jdbcSpringManager.getSimpleJdbcTemplate().query(qry, params, BeanPropertyRowMapper.newInstance(ModelField.class));
        } else {
            String queryString = "with hasfields as (select field_id from " + getCompanyId() + ".modelfield where form_id=:formID), " +
                    "sectionwithsorder as (select distinct section sectionid, sorder sectionsorder \n" +
                    "from " + getCompanyId() + ".customformsection where active is true and  form_id=:formID), \n" +
                    "prfields as (select " + selectfields + ",fsection as section from " + getCompanyId() + ".modelfield md where form_id=:formID) \n" +
                    "select id as objectID," + selectfields + ", section from (select * from prfields) flds \n" +
                    "join sectionwithsorder ss on flds.section=ss.sectionid \n" +
                    "order by ss.sectionsorder,flds.sorder \n";

            result = jdbcSpringManager.getSimpleJdbcTemplate().query(queryString, params, BeanPropertyRowMapper.newInstance(ModelField.class));

        }
        return result;
    }

    @Override
    @Transactional
    public List<EdsModelField> getSpecificFields(String formID, ArrayList<String> fieldTypes) {
        return find("select t from EdsModelFieldCustom t where t.field_ID IN ('" + ServerUtils.getAsCommoDelimited(fieldTypes, "0", "','") + "') and t.form_ID = ? and t.hide is not true", formID);
    }

    @Override
    public EdsModelField get(Integer objectID, boolean isCustom) {
        String table = "EdsModelField" + (isCustom ? "Custom" : "Default");
        return (EdsModelField) findSingle("select l from " + table + " l where l.objectID = " + objectID);

    }

    @Override
    public Integer getMaxSortOrder(String formID) {
        return (Integer) findNativeSingle("select max(sorder) from " + getCompanyId() + ".modelfield where section='ADDITIONAL_INFORMATION' and form_id = '" + formID + "'");
    }

    @Override
    public void clearAllDeletedModelFields(String formId) {

        updateNative("delete from " + getCompanyId() + ".modelfield where form_id = '" + formId + "' and deleted is true");
    }

    @Override
    public Set<String> getFieldIDs(String formID) {
        List<String> fields = slaveEntityManager.createQuery("select mf.field_ID from EdsModelFieldCustom mf where mf.form_ID=:formID", String.class)
                .setParameter("formID", formID).getResultList();
        if (CollectionUtils.isEmpty(fields)) {
            fields = slaveEntityManager.createQuery("select mf.field_ID from EdsModelFieldDefault mf where mf.form_ID=:formID", String.class)
                    .setParameter("formID", formID).getResultList();
        }
        return new HashSet<>(fields);
    }

    @Override
    public void deleteFieldsByFormID(String formID) {
        updateNative("delete from " + getCompanyId() + ".modelField where form_id = '" + formID + "'");
        updateNative("delete from " + getCompanyId() + ".customformsection where form_ID = '" + formID + "'");
    }

    @Override
    public EdsModelField getByFieldID(String formID, String fieldID) {
        return (EdsModelField) findSingle("select l from EdsModelFieldCustom l where (l.deleted is null or l.deleted = false) and l.field_ID = '" + fieldID + "'" + (formID != null ? " and l.form_ID = '" + formID + "'" : ""));
    }

    @Override
    public EdsModelField getWorkflowField(String formID, String fieldID, String section, boolean defaultOne) {
        EdsModelField result = !defaultOne ? (EdsModelField) findSingle("select l from EdsModelFieldCustom l where l.usableByWorkflow = true and l.section = '" + section + "' and l.field_ID = '" + fieldID + "'" + (formID != null ? " and l.form_ID = '" + formID + "'" : "")) : null;
        if (result == null) {
            result = (EdsModelField) findSingle("select l from EdsModelFieldDefault l where l.usableByWorkflow = true and l.section = '" + section + "' and l.field_ID = '" + fieldID + "'" + (formID != null ? " and l.form_ID = '" + formID + "'" : ""));
        }
        return result;
    }

    @Override
    public Integer getMaxSortOrder(String formID, String section) {
        Integer sorder = (Integer) findNativeSingle("select max(sorder) from " + getCompanyId() + ".modelfield where usableByWorkflow = true and section = '" + section + "' and form_id = '" + formID + "'");
        if (sorder == null) {
            sorder = (Integer) findNativeSingle("select max(sorder) from " + getPublic() + ".modelfield where usableByWorkflow = true and section = '" + section + "' and form_id = '" + formID + "'");
        }
        return sorder;
    }

    @Override
    public List<EdsModelField> getModelFields(String formID) {
        return find("select t from EdsModelFieldCustom t where (t.hideInCustomizeForm is null or t.hideInCustomizeForm is false) and (t.deleted is null or t.deleted = false) and t.form_ID = ? order by t.columnType, t.forder", formID);
    }

    @Override
    public List<EdsModelField> getModelFieldsCustom(String formID) {
        return find("select t from EdsModelFieldCustom t where (t.hideInCustomizeForm is null or t.hideInCustomizeForm is false) and t.form_ID = ? and t.isCustomField is true order by t.columnType, t.forder", formID);
    }

    @Override
    public Integer getSectionFields(String form_id, String section) {
        return (Integer) findSingle("select t.id from EdsModelFieldCustom t where (t.deleted is null or t.deleted = false) and t.form_ID = ? and t.fsection=? ", form_id, section);
    }


    @Override
    public List<EdsModelField> getFieldsForWorkflowAlert(String formID) {
        String formIdCF = formID.endsWith(FORM_SUFFIX)
                ? formID.substring(0, formID.length() - FORM_SUFFIX.length())
                : formID;

        List<EdsModelField> custom = slaveEntityManager.createQuery(
                        """
                        SELECT mf FROM EdsModelFieldCustom mf
                        WHERE mf.deleted IS NOT TRUE
                        AND (mf.form_ID = :formID OR mf.form_ID = :formIdCF)
                        AND (mf.usableByWorkflow IS TRUE OR mf.isWorkflowAttribute IS TRUE)
                        """,
                        EdsModelField.class)
                .setParameter("formID", formID)
                .setParameter("formIdCF", formIdCF)
                .getResultList();

        List<EdsModelField> defaults = slaveEntityManager.createQuery(
                        """
                        SELECT mf FROM EdsModelFieldDefault mf
                        WHERE mf.form_ID = :formID
                        AND (mf.usableByWorkflow IS TRUE OR mf.isWorkflowAttribute IS TRUE)
                        """,
                        EdsModelField.class)
                .setParameter("formID", formID)
                .getResultList();

        return Stream.concat(custom.stream(), defaults.stream())
                .collect(Collectors.toList());
    }

    @Override
    public List<EdsModelField> getFieldsForWorkflowUpdate(String formID) {
        List<EdsModelField> result = find("select t from EdsModelFieldCustom t where t.usableByWorkflow is true and t.disableUpdate is not true and t.form_ID = ? ", formID);
        if (CollectionUtils.isEmpty(result)) {
            result = find("select t from EdsModelFieldDefault t where t.usableByWorkflow is true and t.disableUpdate is not true and t.form_ID = ? ", formID);
        }
        return result;
    }

    @Override
    public List<EdsModelField> getFieldsForWorkflowEmployee(String formID) {
        List<EdsModelField> result = find("select t from EdsModelFieldCustom t where t.usableByWorkflow is true and t.disableUpdate is not true and (t.isCustomField is true or (t.source is null or t.source = '')) and t.form_ID = ? order by t.sorder", formID);
        if (CollectionUtils.isEmpty(result)) {
            result = find("select t from EdsModelFieldDefault t where t.usableByWorkflow is true and t.disableUpdate is not true and (t.isCustomField is true or (t.source is null or t.source = '')) and t.form_ID = ? order by t.sorder", formID);
        }
        return result;
    }

    @Override
    public List<EdsModelField> getCustomFieldsForWorkflowAttributes(String formID) {
        List<EdsModelField> result = find("select t from EdsModelFieldCustom t where t.usableByWorkflow is true and t.isCustomField is true and t.form_ID = ? ", formID);
        if (CollectionUtils.isEmpty(result)) {
            result = find("select t from EdsModelFieldDefault t where t.usableByWorkflow is true and t.isCustomField is true and t.form_ID = ? ", formID);
        }
        return result;
    }

    @Override
    public List<EdsModelFieldCustom> getModelFieldsBySection(String formID, String sectionName) {
        return (List<EdsModelFieldCustom>) find("select t from EdsModelFieldCustom t where t.form_ID = ? and t.fsection=? ", formID, sectionName);
    }
}
