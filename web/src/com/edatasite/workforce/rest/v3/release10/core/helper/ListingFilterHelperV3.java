package com.edatasite.workforce.rest.v3.release10.core.helper;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrClientRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Normurod Buriev.
 * Date: 11/14/2020 3:41 PM
 */
public class ListingFilterHelperV3 {

    private static final CommonService commonService;

    static {
        commonService = (CommonService) ApplicationContextProvider.applicationContext.getBean("commonService");
    }

    public static ListingFilterParameter createListingFilter(ListParamsDTO listParams, ListPanelType listPanelType) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setSearchKey(listParams.getSearchText());
        filterParameter.setStart(listParams.getStart());
        filterParameter.setLimit(listParams.getLimit() != null ? listParams.getLimit() : 50);
        filterParameter.setSortField(listParams.getSortBy());
        filterParameter.setSearchButton(StringUtils.isNotBlank(filterParameter.getSearchKey()) && StringUtils.isBlank(filterParameter.getSortField()));
        filterParameter.setAscending(listParams.getSortAs() != null && !Constants.DESC_STR.equalsIgnoreCase(listParams.getSortAs()));
        filterParameter.setStartDate(listParams.getStartDate());
        filterParameter.setEndDate(listParams.getEndDate());
        filterParameter.setObjectIDs(listParams.getObjectIds());
        filterParameter.setLanguage(listParams.getLanguage());
        filterParameter.setCategory(listParams.getCategory());
        filterParameter.setCurrentPage(listParams.getCurrentPage());
        filterParameter.setStatusCode(listParams.getStatusCode());
        filterParameter.setRelationType(listParams.getRelationType());
        filterParameter.setRelationID(listParams.getRelationID());
        filterParameter.setColumnMetadataId(listParams.getColumnMetadataId());
        filterParameter.setAccountType(listParams.getAccountType());

        Map<String, Object> customFilters = listParams.getCustomFilters();
        if (customFilters != null && !customFilters.isEmpty() && customFilters.containsKey("favourite")) {
            filterParameter.setFavourite((Boolean) customFilters.get("favourite"));
        }

        FacetFilterRpc facetFilter = FacetFilterHelperV3.fillFacetFilter(FacetFilterHelperV3.createFacetFilter(listPanelType), listPanelType, listParams.getFilters());
        facetFilter.setStartDate(listParams.getStartDate());
        facetFilter.setEndDate(listParams.getEndDate());

        if (StringUtils.isNotBlank(listParams.getDateColumn())
                && listParams.getStartDate() != null
                && listParams.getEndDate() != null) {
            listParams.setDateColumn(listParams.getDateColumn().trim());
            fillFacetWithDateRange(facetFilter, listPanelType, listParams);
        }
        filterParameter.setFacetFilter(facetFilter);
        return filterParameter;
    }

    private static void fillFacetWithDateRange(FacetFilterRpc facetFilter, ListPanelType listPanelType, ListParamsDTO params) {
        List<String> allowedDateFields = new ArrayList<>();

        switch (listPanelType) {
            case ClientListPanel, SupplierListPanel -> allowedDateFields.add(SolrClientRepresenter.FIELD_CREATED_DATE);
            case LeadListPanel ->
                    allowedDateFields.addAll(Arrays.asList(SolrContactRepresenter.FIELD_UPDATE_DATE, SolrContactRepresenter.FIELD_CREATION_DATE));
            case TaskListPanel -> allowedDateFields.add(SolrClientRepresenter.FIELD_DUE_DATE);
        }
        allowedDateFields.addAll(getFacetableCustomFieldsInDateType(listPanelType.getViewName()));

        if (allowedDateFields.contains(params.getDateColumn())) {
            facetFilter.setSelectedDateSolrCodeName(params.getDateColumn());
            facetFilter.setStartDate(params.getStartDate());
            facetFilter.setEndDate(params.getEndDate());
        }
    }

    private static List<String> getFacetableCustomFieldsInDateType(ViewName viewName) {
        List<CompanyCustomFieldItem> customFields = commonService.getCompanyCustomFields(viewName);
        //filter facetable custom fields in date type
        return customFields.stream().filter(cf -> cf.isFacetable() && Constants.DATA_TYPE_DATE.equals(cf.getDataType()))
                .map(cf -> cf.getColumnCode().toUpperCase())
                .collect(Collectors.toList());
    }
}
