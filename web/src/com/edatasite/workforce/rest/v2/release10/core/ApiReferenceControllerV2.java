package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCountryZone;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSettings;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.TimeZoneManager;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CountriesListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SmsAccountsTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Reference", description = "Reference API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiReferenceControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiReferenceControllerV2.class);
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private ProfileServiceLocal profileServiceLocal;
    @Autowired
    private TimeZoneManager timeZoneManager;


    @Operation(summary = "Get Languages", description = "Retrieves list of languages supported by system")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of languages")})
    @RequestMapping(value = "/languages", method = RequestMethod.GET)
    public Object getLanguages() throws RestException {
        try {
            SelectItem[] referenceList = commonServiceLocal.getReferences(ReferenceParentEnum._LANGUAGES);
            ArrayList<SelectItemTO> references = new ArrayList<>();
            if (referenceList != null) {
                for (SelectItem item : referenceList) {
                    references.add(new SelectItemTO(item));
                }
            }
            return successResponse(new ResponseListData<>(references));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Language Levels", description = "Retrieves list of languages and their levels")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of language levels")})
    @RequestMapping(value = "/language_levels", method = RequestMethod.GET)
    public Object getLanguageLevels() throws RestException {
        try {
            SelectItem[] referenceList = commonServiceLocal.getReferences(ReferenceParentEnum._LANGUAGE_LEVELS);
            ArrayList<SelectItemTO> references = new ArrayList<>();
            if (referenceList != null) {
                for (SelectItem item : referenceList) {
                    references.add(new SelectItemTO(item));
                }
            }
            return successResponse(new ResponseListData<>(references));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Countries List", description = "Retrieves list of countries ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of countries")})
    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public Object getCountriesList() throws RestException {
        try {
            ArrayList<CountriesListTO> countriesList = new ArrayList<>();
            List<EdsCountry> edsCountries = countryManager.list();
            if (edsCountries != null) {
                edsCountries.forEach(edsCountry -> {
                    if (StringUtils.isNotBlank(edsCountry.getName())) {
                        CountriesListTO country = new CountriesListTO();
                        country.setId(edsCountry.getObjectID());
                        country.setTitle(edsCountry.getName());
                        country.setHas_states(edsCountry.getStates() != null && edsCountry.getStates().size() > 0);
                        countriesList.add(country);
                    }
                });
            }
            return successResponse(new ResponseListData<>(countriesList));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Country States", description = "Retrieves list of states of the country")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of states of the country")})
    @RequestMapping(value = "/countries/{item_id}/states", method = RequestMethod.GET)
    public Object getCountryStates(@PathVariable(value = "item_id") Integer item_id) throws RestException {
        if (item_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "country_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "country_id is more than zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        try {
            ArrayList<CategoryTO> countryStates = new ArrayList<>();
            List<EdsRegion> statesList = regionManager.listByCountry(item_id);
            if (statesList != null) {
                statesList.forEach(state -> {
                    CategoryTO countryState = new CategoryTO();
                    countryState.setId(state.getObjectID());
                    countryState.setTitle(state.getName());
                    countryStates.add(countryState);
                });
            }
            return successResponse(new ResponseListData<>(countryStates));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Country States", description = "Retrieves list of timezones of the country")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of states of the country")})
    @RequestMapping(value = "/countries/{item_id}/timezones", method = RequestMethod.GET)
    public Object getCountryTimezones(@PathVariable(value = "item_id") Integer item_id) throws RestException {
        if (item_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "country_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "country_id is more than zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        try {
            ArrayList<CategoryTO> countryStates = new ArrayList<>();
            List<EdsCountryZone> cZones = timeZoneManager.getCountryZones(List.of(item_id));

            if (cZones != null) {
                cZones.forEach(cZone -> {
                    CategoryTO countryState = new CategoryTO();
                    countryState.setId(cZone.getObjectID());
                    countryState.setTitle(cZone.getZone().getName());
                    countryStates.add(countryState);
                });
            }
            return successResponse(new ResponseListData<>(countryStates));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get SMS Account List", description = "Retrieves list of SMS Accounts in the system ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of SMS Accounts")})
    @RequestMapping(value = "/sms_accounts", method = RequestMethod.GET)
    public Object getSMSAccounts() throws RestException {
        try {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setObjectId(userManager.getUser().getObjectID());
            ArrayList<SmsAccountsTO> smsAccounts = new ArrayList<>();
            ListResult<SmsSettings> smsAccountsList = profileServiceLocal.getSmsSettingList(filterParameter);
            if (smsAccountsList != null && smsAccountsList.getList() != null && smsAccountsList.getList().size() > 0) {
                smsAccountsList.getList().forEach(smsAccount -> {
                    SmsAccountsTO account = new SmsAccountsTO();
                    account.setId(smsAccount.getObjectID());
                    account.setTitle(smsAccount.getName());
                    account.setProvider_id(smsAccount.getProviderID());
                    smsAccounts.add(account);
                });
            }
            return successResponse(new ResponseListData<>(smsAccounts));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Import File Headers", description = "Retrieves Import file headers")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of states of the country")})
    @RequestMapping(value = "/headers/{attachmentid}/{needrowcount}", method = RequestMethod.GET)
    public Object getImportFileHeaders(@PathVariable(value = "attachmentid") Integer attachmentid,
                                       @PathVariable(value = "needrowcount") Integer needrowcount) throws RestException {
        if (attachmentid == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "attachmentId is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (attachmentid <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "attachmentId is more than zero", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        try {
            ArrayList<SelectItem[]> headerAndDefaultRows = commonServiceLocal.getCSVColumns(attachmentid, needrowcount);
            return successResponse(new ResponseListData<>(headerAndDefaultRows));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
