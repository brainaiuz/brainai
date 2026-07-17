package com.edatasite.workforce.rest.v1.release10.core;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.to.CustomFieldTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by dilshod madrahimov on 3/24/15.
 */
@Tag(name = "Custom Field", description = "Custom Field API")
@RestController
@RequestMapping(value = "/customField", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCustomFieldControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private ProfileServiceLocal profileServiceLocal;


    @RequestMapping(value = "/{relationType}/{relationId}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getCustomFields(@PathVariable(value = "relationType") String relationType,
                                  @PathVariable(value = "relationId") Integer relationId,
                                  @RequestBody MListingFilterParameter mListingFilterParameter) {
        if (relationType == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setEntityName("'" + ListPanelType.getByViewName(relationType).getViewName().name() + "'");
        filterParameter.setRelationID(relationId);
        ListResult<CompanyCustomFieldItem> customFields = profileServiceLocal.getCustomFields(filterParameter);
        ArrayList<CustomFieldTO> result = new ArrayList<>();
        for (CompanyCustomFieldItem customField : customFields.getList()) {
            result.add(new CustomFieldTO(customField));
        }
        return successResponse(new ListResultTO<>(customFields.getTotal(), result));
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{id}", method = RequestMethod.GET)
    public Object getCustomField(@PathVariable(value = "relationType") String relationType,
                                 @PathVariable(value = "relationId") Integer relationId,
                                 @PathVariable(value = "id") Integer id) {
        if (relationType == null || relationId == null || id == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        return successResponse();

    }

}
