package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CompanyContactsDTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SearchPeopleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.finnetlimited.reportservice.core.server.CoreServiceLocal;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.WEBSITE;

/**
 * Created by Dilsh0d Madrahimov on 23/03/2018.
 */

@Tag(name = "Core", description = "Core API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCoreControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiCoreControllerV2.class);
    @Autowired
    private CoreServiceLocal coreServiceLocal;
    @Autowired
    private CompanyPayrollSettingsManager companyPayrollSettingsManager;

    @Operation(summary = "People Search", description = "People Search within Employees, Customers, Suppliers, Leads, Contacts. Search by email, mobile, name like or match against. No email - no item")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of search result")})
    @RequestMapping(value = "/search_people", method = RequestMethod.GET)
    public Object searchPeople(@RequestParam(value = "query") String query,
                               @RequestParam(value = "limit") Integer limit,
                               @RequestParam(value = "offset") Integer offset) throws RestException {

        if (StringUtils.isBlank(query)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "query is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (offset == null || offset < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "offset is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (limit == null || limit <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        query = query.replace("%20", " ").trim();

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setSearchKey(query);
        filterParameter.setStart(offset);
        filterParameter.setLimit(limit);

        ListResult<SearchPeopleTO> listResult;
        try {
            listResult = coreServiceLocal.searchPeople(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        PagingListResultTO<SearchPeopleTO> searchResult = new PagingListResultTO<>();
        searchResult.setTotal_count(listResult.getTotal());
        if (listResult.getTotal() < (limit + offset)) {
            searchResult.setLeft(0);
        } else {
            searchResult.setLeft(listResult.getTotal() - (limit + offset));
        }
        searchResult.setCount(listResult.getList().size());
        searchResult.setOffset(offset);

        searchResult.setList(listResult.getList());

        return successResponse(searchResult);

    }

    @Operation(summary = "Dynamic LookUp", description = "Dynamic LookUp")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Response will have the dynamic look-up items ")})
    @RequestMapping(value = "/dynamic/look-up", method = RequestMethod.GET)
    public Object searchProject(@RequestParam(value = "queryName") String queryName,
                                @RequestParam(value = "searchKey", required = false) String searchKey,
                                @RequestParam(value = "limit", required = false) Integer limit) throws BaseApiException {
        try {
            SelectItem[] projects = coreServiceLocal.dynamicLookUpResult(queryName, searchKey, limit);
            ArrayList<SelectItemTO> projectItems = new ArrayList<>();
            for (SelectItem it : projects) {
                projectItems.add(new SelectItemTO(it));
            }
            return successResponse(new ResponseListData<>(projectItems));
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @Operation(summary = "Get Company Contacts Info")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Company Contacts Info"))
    @RequestMapping(value = "/company_contacts", method = RequestMethod.GET,
            headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<CompanyContactsDTO> getCompanyContacts() {
        CompanyContactsDTO result = new CompanyContactsDTO();
        EdsCompany company = userManager.getUser().getCompany();
        result.setOfficeNumber(company.getPhone());
        result.setMobileNumber(company.getMobilePhone());
        result.setFaxNumber(company.getFaxNumber());
        result.setEmail(company.getEmail());
        EdsCompanyPayrollSettings website = companyPayrollSettingsManager.getCompanySettingValue(WEBSITE);
        if (website != null) {
            result.setWebsite(website.getValue());
        }
        return ResultTO.success(result);
    }
}
