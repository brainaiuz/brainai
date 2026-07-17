package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface CustomFormItemManager extends Manager<EdsCustomFormItems> {

    List<EdsCustomFormItems> list(ListingFilterParameter filterParameter, int start, int limit);

    List<EdsCustomFormItems> allList(ListingFilterParameter filterParameter);

    List<Object[]> getCustomFormItemsByFormId(Integer form_id, EdsCompanyCustomFieldsSettings item);

    List<Integer> getCustomFormItemsByFormId(Integer form_id);

    int count(ListingFilterParameter filterParameter);

    List<Integer> getCustomFormIdsByIds(String toString);

    List<EdsCustomFormItems> getCustomFormByIds(String toString);

    List<Integer> getIdsWithLimit(int startat, int limit);

    EdsCustomFormItems getByFormID(String formID);

    EdsCustomFormItems findByRelation(String formId, String relationType, Integer relationId, String relationObjectKey);

    EdsCustomFormItems getByObjectKey(String objectKey);

}
