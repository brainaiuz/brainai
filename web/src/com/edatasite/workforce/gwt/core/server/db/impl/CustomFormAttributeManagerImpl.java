package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customform.EdsCustomFormAttributes;
import com.edatasite.workforce.gwt.core.server.db.CustomFormAttributeManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 11.10.2019 15:26
 */
@Repository
public class CustomFormAttributeManagerImpl extends BaseManager<EdsCustomFormAttributes> implements CustomFormAttributeManager {

    public CustomFormAttributeManagerImpl() {
        super(EdsCustomFormAttributes.class);
    }

    @Override
    public EdsCustomFormAttributes findByFieldType(String fieldType) {
        return (EdsCustomFormAttributes) findSingle("select a from EdsCustomFormAttributes a" +
                " where a.deleted is not true and a.fieldType = '" + fieldType + "'");
    }

    @Override
    public EdsCustomFormAttributes findByFieldTypeAndFieldID(String fieldId, String fieldType){
        return (EdsCustomFormAttributes) findSingle("select a from EdsCustomFormAttributes a" +
                " where a.deleted is not true and a.fieldType = '" + fieldType + "'" + " and a.fieldId='" + fieldId +"'" );
    }

    @Override
    public List<EdsCustomFormAttributes> getAttributesByFormId(String formId) {
        String sql = "select a from EdsCustomFormAttributes a" +
                "  left join a.customForm c " +
                "  where a.deleted is null or a.deleted = false" +
                "      and c.formID =:formId";
        return slaveEntityManager.createQuery(sql, EdsCustomFormAttributes.class)
                .setParameter("formId", formId)
                .getResultList();
    }

    @Override
    public List<EdsCustomFormAttributes> getAttByFormIdAndFieldType(String fieldType, String formId) {
        String sql = "select a from EdsCustomFormAttributes a" +
                "  left join a.customForm c " +
                "  where a.deleted is null or a.deleted = false" +
                "      and a.fieldType =:fieldType" +
                "      and c.formID =:formId";
        return slaveEntityManager.createQuery(sql, EdsCustomFormAttributes.class)
                .setParameter("formId", formId)
                .setParameter("fieldType", fieldType)
                .getResultList();
    }
}
