package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;


public interface CustomFormManager extends Manager<EdsCustomForm> {

    EdsCustomForm findByName(String name);

    void deleteCustom(Integer objectID, String formID);

    EdsCustomForm findByFormID(String formID);

    List<EdsCustomForm> findByContext(String context);

    List getCustomFormsByEmployeeId(Integer id);

    List getCustomFormsCustomFieldsByEmployeeId(Integer id);

    List<EdsCustomForm> list(ListingFilterParameter listingFilterParameter);

    List<EdsCustomForm> getForms();

    SelectItem[] getLookUpItems(ListingFilterParameter filterParametrs);
}
