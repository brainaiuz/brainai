package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface PropertManager extends Manager<EdsProperty> {

    List<EdsProperty> findByModuleCode(String moduleName);

    Integer parentCountByModuleCode(String moduleName);

    List<EdsProperty> getListingByParent(Integer parent, String moduleName);

    EdsProperty findByCode(String instanceName);

    List<EdsProperty> list(ListingFilterParameter filterParameter);

    int count(ListingFilterParameter filterParameter);

    List<EdsProperty> findByModuleCodeFromBackend(String moduleName);

    EdsProperty zeroSchemaProperty(String objectName);

    String findByPlural(String pluralName, Integer companyId);
}
