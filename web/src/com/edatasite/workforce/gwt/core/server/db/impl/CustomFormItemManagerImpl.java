package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("customFormItemManager")
public class CustomFormItemManagerImpl extends BaseManager<EdsCustomFormItems> implements CustomFormItemManager {

    public CustomFormItemManagerImpl() {
        super(EdsCustomFormItems.class);
    }

    @Override
    public List<EdsCustomFormItems> list(ListingFilterParameter fp, int start, int limit) {
        return findInterval(getSqlForListing(fp, false), start, limit);
    }

    @Override
    public List<EdsCustomFormItems> allList(ListingFilterParameter fp) {
        return find(getSqlForListing(fp, false));
    }

    @Override
    public int count(ListingFilterParameter filterParameter) {
        Long count = (Long) findSingle(getSqlForListing(filterParameter, true));
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

    private String getSqlForListing(ListingFilterParameter filterParameter, boolean forCount) {
        StringBuilder sql = new StringBuilder("select ").append(forCount ? "count(p.objectID)" : "p").append(" from EdsCustomFormItems p where 1=1 ");

        if (StringUtils.isNotBlank(filterParameter.getForm())) {
            sql.append(" and p.customForm.formID='").append(filterParameter.getForm()).append("' ");
        }
        if (filterParameter.getUserID() != null) {
            sql.append(" and p.auditInfo.createdBy_id = ").append(filterParameter.getUserID());
        }
        sql.append(" and (p.deleted is null OR p.deleted <> true) ");
        return sql.toString();
    }

    @Override
    public List<Integer> getCustomFormIdsByIds(String ids) {
        return find("select s.objectID from EdsCustomFormItems s where s.objectID in (" + ids + ")");
    }

    @Override
    public List<EdsCustomFormItems> getCustomFormByIds(String ids) {
        return find("select s from EdsCustomFormItems s where s.objectID in (" + ids + ")");
    }

    @Override
    public List<Object[]> getCustomFormItemsByFormId(Integer form_id, EdsCompanyCustomFieldsSettings item) {
        if (item != null) {
            StringBuilder c = new StringBuilder("select item.id itemid,");
            c.append(" cf." + item.getColumnCode() + " ");

            c.append(" itemname ");
            c.append(" from " + getCompanyId() + ".custom_form_item item" +
                    " left join " + getCompanyId() + ".customform_customfields cf on item.form_customfieldsid = cf.id  " +
                    " left join " + getCompanyId() + ".custom_form cform on item.form_id = cform.form_id ");
            c.append(" where (item.deleted is null or item.deleted is not true) and cform.id = " + form_id);
            return (List<Object[]>) findNative(c.toString());
        }
        return null;
    }

    @Override
    public List<Integer> getCustomFormItemsByFormId(Integer form_id) {
        StringBuilder c = new StringBuilder("select item.id itemid");

        c.append(" from " + getCompanyId() + ".custom_form_item item" +
                " left join " + getCompanyId() + ".customform_customfields cf on item.form_customfieldsid = cf.id  " +
                " left join " + getCompanyId() + ".custom_form cform on item.form_id = cform.form_id ");
        c.append(" where (item.deleted is null or item.deleted is not true) and cform.id = " + form_id);
        return (List<Integer>) findNative(c.toString());
    }

    @Override
    public List<Integer> getIdsWithLimit(int startat, int limit) {
        return findInterval("select s.objectID from EdsCustomFormItems s", startat, limit);
    }

    @Override
    public EdsCustomFormItems getByFormID(String formID) {
        return (EdsCustomFormItems) findNativeSingle("select * from  " + getCompanyId() + ".custom_form_item where form_id = ' " + formID + "'", EdsCustomFormItems.class);
    }

    @Override
    public EdsCustomFormItems findByRelation(String formId, String relationType, Integer relationId, String relationObjectKey) {
        return (EdsCustomFormItems) findSingle("select s from EdsCustomFormItems s where s.customForm.formID = ? and s.relationType = ? and " + "s.deleted is false and " + (relationId != null ? (" s.relationId =  " + relationId) : (" s.relationObjectKey = '" + relationObjectKey + "'")), formId, relationType);
    }

    @Override
    public EdsCustomFormItems getByObjectKey(String objectKey) {
        return (EdsCustomFormItems) findSingle("select s from EdsCustomFormItems s where s.objectKey = ? and s.deleted is false", objectKey);
    }

}
