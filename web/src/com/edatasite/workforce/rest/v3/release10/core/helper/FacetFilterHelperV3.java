package com.edatasite.workforce.rest.v3.release10.core.helper;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.rest.base.helpers.FacetFilterHelper;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ListingFilterDTO;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Normurod Buriev.
 * Date: 11/13/2020 9:35 PM
 */
public class FacetFilterHelperV3 {

    private static CommonService commonService;

    static {
        commonService = (CommonService) ApplicationContextProvider.applicationContext.getBean("commonService");
    }

    public static FacetFilterRpc createFacetFilter(ListPanelType listPanelType) {
        FacetFilterRpc facetFilter = FacetFilterHelper.createFacetFilter(null, listPanelType);

        List<CompanyCustomFieldItem> customFields = commonService.getCompanyCustomFields(listPanelType.getViewName());
        //filter facetable custom fields
        customFields = customFields.stream().filter(cf -> cf.isFacetable() && !Constants.DATA_TYPE_DATE.equals(cf.getDataType())).collect(Collectors.toList());

        for (CompanyCustomFieldItem cf : customFields) {
            if (!facetFilter.getShowFacetCodeName().contains(cf.getColumnCode())) {
                facetFilter.getShowFacetCodeName().add(cf.getColumnCode());
            }
            facetFilter.getShowSolrFieldMap().put(cf.getColumnCode(), new FacetSolrField(cf.getColumnCode(), cf.getColumnCode(), LocalizationType.REFERENCE, false));
            facetFilter.getFacetContentMap().put(cf.getColumnCode(), new FacetContentRpc());
        }
        return facetFilter;
    }

    public static FacetFilterRpc fillFacetFilter(FacetFilterRpc facetFilter, ListPanelType listPanelType, List<ListingFilterDTO> filters) {
        FacetContentType facetContentType = FacetFilterHelper.getFacetContentType(listPanelType);
        List<String> queryParams = FacetFilterHelper.getFacetCodeNames(facetContentType);

        if (facetFilter == null) {
            facetFilter = FacetFilterHelper.getFilterPrototype(listPanelType);
        }
        facetFilter.setFilterChanges(true);

        if (!CollectionUtils.isEmpty(filters)) {
            Map<String, ListingFilterDTO> map = filters.stream().collect(Collectors.toMap(f -> f.getFilterCodeName().trim().toLowerCase(), x -> x, (k1, k2) -> k1, LinkedHashMap::new));

            if (queryParams != null) {
                for (String param : queryParams) {
                    fillFacetContentMap(facetFilter, param, map.get(param.toLowerCase()));
                }
            }
        }

        return facetFilter;
    }

    private static void fillFacetContentMap(FacetFilterRpc facetFilter, String key, ListingFilterDTO filterParam) {
        if (filterParam == null) {
            return;
        }

        FacetSolrField solrField = facetFilter.getShowSolrFieldMap().get(key);

        List<SelectItem> selectItemList;

        if (facetFilter.getFacetContentMap().get(key).getFacetItems() == null) {
            selectItemList = new ArrayList<>();
        } else {
            selectItemList = new ArrayList<>(Arrays.asList(facetFilter.getFacetContentMap().get(key).getFacetItems()));
        }

        for (IdCode item : filterParam.getItems()) {
            if (solrField.isConditionItemId()) {
                selectItemList.add(new SelectItem(item.getId()));
            } else {
                selectItemList.add(new SelectItem(null, item.getCode()));
            }
        }

        facetFilter.getFacetContentMap().get(key).setFacetItems(selectItemList.toArray(new SelectItem[0]));
    }
}
