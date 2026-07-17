package com.edatasite.workforce.rest.v1.release10.core;

import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.SaveFilterSelectItems;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.FacetFilterHelper;
import com.edatasite.workforce.rest.base.to.FacetFilterTO;
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
 * Created by Umidbek on 31.01.2015.
 */
@Tag(name = "Facet Filter", description = "Facet Filter API")
@RestController
@RequestMapping(value = "/facetFilter", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiFacetFilterControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private RbacService rbacService;
    @Autowired
    private CommonServiceLocal commonServiceLocal;


    @RequestMapping(value = "/{type}", method = RequestMethod.GET)
    public ArrayList<FacetFilterTO> getList(@PathVariable(value = "type") String type) {
        ListPanelType listPanelType = this.validatePanelType(type);
        SaveFilterSelectItems savedItems = commonServiceLocal.getSavedFacetFilterList(listPanelType, null);
        ArrayList<FacetFilterTO> result = new ArrayList<>();

        savedItems.getDefaultFilterID();

        for (SelectItem item : savedItems.getItems()) {
            FacetFilterTO filter = new FacetFilterTO();

            filter.setId(item.getId());
            filter.setName(item.getName());
            filter.setIsDefault(item.getId().equals(savedItems.getDefaultFilterID()));

            if (savedItems.getPublicFilds().containsKey(item.getId())) {
                filter.setIsPublic(savedItems.getPublicFilds().get(item.getId()));
            }

            result.add(filter);
        }

        return result;
    }

    @RequestMapping(value = "/{type}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public FacetFilterTO saveItem(@RequestBody FacetFilterTO filter,
                                  @PathVariable(value = "type") String type) {
        ListPanelType listPanelType = this.validatePanelType(type);

        if (filter.getId() != null) {

        }

        return this.save(filter, listPanelType);
    }

    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public FacetFilterTO updateItem(@RequestBody FacetFilterTO filter,
                                    @PathVariable(value = "id") Integer filterID,
                                    @PathVariable(value = "type") String type) {
        ListPanelType listPanelType = this.validatePanelType(type);

        if (filter == null || !filter.getId().equals(filterID)) {
        }

        return this.save(filter, listPanelType);
    }

    private FacetFilterTO save(FacetFilterTO filter, ListPanelType listPanelType) {
        if (StringUtil.isEmpty(filter.getName())) {
        }

        FacetFilterRpc facetFilter = FacetFilterHelper.getFilterPrototype(listPanelType);
        Integer objectID = commonServiceLocal.saveFacetFilter(FacetFilterHelper.assign(filter, facetFilter), listPanelType);

        if (objectID == null) {
        } else {
            filter.setId(objectID);
        }

        return filter;
    }

    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.GET)
    public FacetFilterTO getItem(@PathVariable(value = "id") Integer filterID, @PathVariable(value = "type") String type) {
        return this.getFacetFilterItem(filterID, this.validatePanelType(type));
    }

    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.DELETE)
    public void deleteItem(@PathVariable(value = "id") Integer id, @PathVariable(value = "type") String type) {
        this.validatePanelType(type);

        try {
            commonServiceLocal.deleteFacetFilter(id);
        } catch (Exception ignored) {
        }
    }

    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public FacetFilterTO updateField(@PathVariable(value = "id") Integer id,
                                     @PathVariable(value = "type") String type,
                                     @PathVariable(value = "setPublic") Boolean setPublic,
                                     @PathVariable(value = "setFavour") Boolean setFavour,
                                     @PathVariable(value = "setDefault") Boolean setDefault) {
        ListPanelType listPanelType = this.validatePanelType(type);
        FacetFilterTO filter = this.getFacetFilterItem(id, listPanelType);

        if (filter == null) {

        }

        Boolean hasChange = false;

        if (setPublic != null) {
            hasChange = true;
            filter.setIsPublic(setPublic);
        }

        if (setFavour != null) {
            hasChange = true;
            filter.setIsFavour(setFavour);
        }

        if (setDefault != null) {
            hasChange = true;
            filter.setIsDefault(setDefault);
        }

        if (hasChange) {
            return this.save(filter, listPanelType);
        }
        return null;
    }

    @RequestMapping(value = "/{type}/codes", method = RequestMethod.GET)
    public ArrayList<String> getCodes(@PathVariable(value = "type") String type) {
        ListPanelType listPanelType = this.validatePanelType(type);
        return FacetFilterHelper.getFacetCodeNames(listPanelType);
    }

    private FacetFilterTO getFacetFilterItem(Integer filterID, ListPanelType type) {
        FacetFilterRpc facetFilter = FacetFilterHelper.createFacetFilter(filterID, type);
        return FacetFilterHelper.mapFilter(this.getFacetFilterData(facetFilter, type));
    }

    private FacetFilterRpc getFacetFilterData(FacetFilterRpc facetFilter, ListPanelType type) {
        switch (type) {
            case TaskListPanel -> facetFilter = rbacService.getTaskFacetFilterData(facetFilter, false);
            case ProjectListPanel -> facetFilter = rbacService.getProjectFacetFilterData(facetFilter);
            case CaseListPanel -> facetFilter = rbacService.getCaseFacetFilterData(facetFilter);
            case ContactListPanel ->
                    facetFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_CONTACT, facetFilter);
            case LeadListPanel -> facetFilter = rbacService.getCRMFacetFilterData(CrmConstants.CRM_LEAD, facetFilter);
        }

        facetFilter.setType(type);

        return facetFilter;
    }
}
