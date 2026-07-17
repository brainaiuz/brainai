package com.edatasite.workforce.rest.v3.release10.crm;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.note.server.NoteServiceLocal;
import com.edatasite.workforce.rest.base.enums.ContactParamEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.ActivityTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.BaseApiControllerV3;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.AddressAddDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.LeadAddDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.LeadPatchDto;
import com.edatasite.workforce.rest.v3.release10.crm.dto.LeadAddDto;
import com.edatasite.workforce.rest.v3.release10.crm.dto.LeadByStatusTO;
import com.edatasite.workforce.rest.v3.release10.crm.dto.lead.LeadConvertTO;
import com.edatasite.workforce.rest.v3.release10.crm.dto.lead.OpportunityConvertTO;
import com.edatasite.workforce.rest.v3.release10.crm.service.ApiLeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created By : Dilsh0d Madrahimov on 10/7/2019 5:17 PM
 */
@Tag(name = "Lead", description = "Lead Public API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiLeadControllerV3 extends BaseApiControllerV3 implements ApiConstants {

    private static final Logger log = LoggerFactory.getLogger(ApiLeadControllerV3.class);

    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private NoteServiceLocal noteServiceLocal;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private RbacService rbacService;
    @Autowired
    private ApiLeadService apiLeadService;
    @Autowired
    private CRMService crmService;

    @RequestMapping(value = "/leads", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object create(@RequestBody LeadAddDTO leadAddDTO) throws RestException {

        if (leadAddDTO == null) {
            throw new RestException(ERROR_MESSAGE, "Object can not be null or empty", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (leadAddDTO.getLead_information() == null) {
            throw new RestException(ERROR_MESSAGE, "Lead information can not be null or empty", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (StringUtils.isBlank(leadAddDTO.getLead_information().getFirst_name())) {
            throw new RestException("First name is required", "First name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(leadAddDTO.getLead_information().getLast_name())) {
            throw new RestException("Last name is required", "Last name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(leadAddDTO.getLead_information().getEmail())) {
            throw new RestException("Email address is required", "Email address is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(leadAddDTO.getLead_information().getPhone())) {
            throw new RestException("Phone number is required", "Phone number is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!EMAIL_PATTERN.matcher(leadAddDTO.getLead_information().getEmail()).matches()) {
            throw new RestException("Invalid email address", "Invalid email address " + leadAddDTO.getLead_information().getEmail(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (StringUtils.isBlank(leadAddDTO.getLead_information().getCompany_name())) {
            throw new RestException("Company name is required", "Company name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ContactListItem leadItem = new ContactListItem();
        leadItem.setContactType(CrmConstants.TYPE_LEAD_CONTACT);
        leadItem.setFirstName(leadAddDTO.getLead_information().getFirst_name());
        leadItem.setLastName(leadAddDTO.getLead_information().getLast_name());
        leadItem.setPrimaryEmail(leadAddDTO.getLead_information().getEmail());
        leadItem.setPrimaryPhone(leadAddDTO.getLead_information().getPhone());
        Optional.ofNullable(leadItem.getCrmAccount())
                .ifPresent(l -> l.setIndustryID(leadAddDTO.getIndustryId()));

        HashMap<Integer, ArrayList<String>> emailMap = new HashMap<>();
        ArrayList<String> emails = new ArrayList<>();
        emails.add(leadAddDTO.getLead_information().getEmail());
        emailMap.put(ContactParamEnum.WORK.getId(), emails);
        leadItem.setEmails(emailMap);

        HashMap<Integer, ArrayList<String>> phoneMap = new HashMap<>();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(leadAddDTO.getLead_information().getPhone());
        phoneMap.put(ContactParamEnum.WORK.getId(), phones);
        leadItem.setPhones(phoneMap);

        EdsUser user = countryManager.getUser();
        CrmAccountItem crmAccountItem = new CrmAccountItem();
        crmAccountItem.setName(leadAddDTO.getLead_information().getCompany_name());
        SelectItem owner = new SelectItem(user.getObjectID(), user.getName());
        crmAccountItem.setOwnerItems(new SelectItem[]{owner});

        Integer crmAccountID = crmServiceLocal.saveAccount(crmAccountItem, null, null, false, false, false, true);
        crmAccountItem.setObjectId(crmAccountID);
        leadItem.setCrmAccount(crmAccountItem);

        if (leadAddDTO.getAddress_information() != null) {
            if (CollectionUtils.isNotEmpty(leadAddDTO.getAddress_information().getBilling_addresses())) {
                ArrayList<Address> addressList = new ArrayList<>();
                for (AddressAddDTO addressDTO : leadAddDTO.getAddress_information().getBilling_addresses()) {
                    Address address = new Address();
                    address.setPrimary(addressDTO.getIs_primary() != null ? addressDTO.getIs_primary() : false);
                    address.setName(addressDTO.getName());
                    address.setAddress(addressDTO.getAddress_line_1());
                    address.setAddressb(addressDTO.getAddress_line_2());
                    address.setCity(addressDTO.getCity());
                    address.setZipCode(addressDTO.getPost_code());

                    EdsCountry country = null;
                    if (StringUtils.isNotBlank(addressDTO.getCountry_code())) {
                        country = countryManager.getCountryByCode(addressDTO.getCountry_code());
                        if (country == null) {
                            throw new RestException(ERROR_MESSAGE, "There is no any country with this code " + addressDTO.getCountry_code(), NOT_FOUND, HttpStatus.NOT_FOUND);
                        }
                    } else if (addressDTO.getCountry_id() != null) {
                        country = countryManager.get(addressDTO.getCountry_id());
                        if (country == null) {
                            throw new RestException(ERROR_MESSAGE, "There is no any country with this id " + addressDTO.getCountry_id(), NOT_FOUND, HttpStatus.NOT_FOUND);
                        }
                    }
                    if (country != null) {
                        address.setCountryId(country.getObjectID());
                        address.setCountry(country.getName());
                        address.setCountryCode(country.getCode());
                    }
                    address.setStateId(addressDTO.getState_id());
                    //address.setEntityID(leadId);
                    address.setEntityType(EdsAddress.ENTITY_TYPE_CONTACT);
                    address.setRelationType(EdsAddress.HOME);
                    addressList.add(address);
                }
                leadItem.setAddresses(addressList);
            }
        }
        Integer leadID;
        try {
            leadID = contactServiceLocal.saveContact(leadItem, null, false);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (StringUtils.isNotBlank(leadAddDTO.getNote())) {
            HistoryListItem noteItem = new HistoryListItem();
            noteItem.setComment(leadAddDTO.getNote());
            noteItem.setRelatedId(leadID);
            noteItem.setRelatedToId(EdsNoteHistory.CRM_LEAD);
            noteItem.setEventDate(new Date());
            noteItem.setVisibility(null);
            noteServiceLocal.saveNote(noteItem);
        }

        return successResponse(new ResponseData());
    }


    @Operation(summary = "Get leads list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Lead"))
    @RequestMapping(value = "/leads/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ApiResult leadList(@RequestBody ListParamsDTO listParams) throws RestException, ParseException {
        ListingFilterParameter filterParameter = ListingFilterHelperV3.createListingFilter(listParams, ListPanelType.LeadListPanel);

        PagingListResultTO<LeadTO> leadListResult = apiLeadService.getLeadList(filterParameter);

        return successResponse(leadListResult);
    }

    @Operation(summary = "Get leads list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Lead"))
    @RequestMapping(value = "/leads/kanban", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<PagingListResultTO<LeadTO>> leadKanban(@RequestBody ListParamsDTO listParams) throws RestException, ParseException {
        ListingFilterParameter filterParameter = ListingFilterHelperV3.createListingFilter(listParams, ListPanelType.LeadKanbanPanel);

        PagingListResultTO<LeadTO> leadListResult = apiLeadService.getLeadKanban(filterParameter);

        return ResultTO.success(leadListResult);
    }

    @RequestMapping(value = "/update2", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<LeadDto> applyChanges(@RequestBody LeadPatchDto dto) throws RestException, ParseException {
        log.info("=====LEAD APPLY CHANGE PROCESS IS STARTED=====");
        log.info("REQUEST BODY: {}", dto);
        validateForExistince(dto);
        Integer objectId = apiLeadService.applyChanges(dto);
        return ResultTO
                .success(apiLeadService.getLeadById(objectId));
    }

    @RequestMapping(value = "/leads/list/by-status", method = RequestMethod.POST)
    public ResultTO<List<LeadByStatusTO>> leadByStatus(@RequestBody ListParamsDTO listParams) {
        log.info("REST request to get lead list grouped by status");
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(listParams, ListPanelType.LeadListPanel);

        List<LeadByStatusTO> response = apiLeadService.leadByStatus(fp);
        return ResultTO.success(response);
    }

    @RequestMapping(value = "/leads/{id}", method = RequestMethod.GET)
    public ResultTO<LeadDto> getLeadById(@PathVariable("id") Integer id) {
        log.info("REST request to get lead by id: {}", id);
        LeadDto lead = apiLeadService.getLeadById(id);
        return ResultTO.success(lead);
    }

    @RequestMapping(value = "/leads/{id}/status", method = RequestMethod.PATCH)
    public ResultTO<?> updateLeadStatus(@PathVariable("id") Integer id,
                                        @RequestParam("statusId") Integer statusId) {
        log.info("REST request to update lead status by id: {}", id);
        apiLeadService.updateLeadStatus(id, statusId);
        return ResultTO.success();
    }

    @RequestMapping(value = "/leads/create", method = RequestMethod.POST)
    public ResultTO<LeadAddDto> createLead(@RequestBody LeadAddDto leadDto) throws RestException {
        log.info("REST request to create lead: {}", leadDto);
        if (leadDto.getId() != null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Lead id is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        LeadAddDto lead = apiLeadService.saveLead(leadDto);
        return ResultTO.success(lead);
    }

    @RequestMapping(value = "/leads/update", method = RequestMethod.PUT)
    public ResultTO<LeadAddDto> updateLead(@RequestBody LeadAddDto leadDto) throws RestException {
        log.info("REST request to create lead: {}", leadDto);
        if (leadDto.getId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Lead id is required", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        LeadAddDto lead = apiLeadService.saveLead(leadDto);
        return ResultTO.success(lead);
    }

    @RequestMapping(value = "/leads/delete/{id}", method = RequestMethod.DELETE)
    public ResultTO<?> deleteLead(@PathVariable("id") Integer id) {
        apiLeadService.deleteLeadById(id);
        return ResultTO.success();
    }

    private void validateForExistince(LeadPatchDto dto) throws RestException {
        if (dto.getId() == null) {
            throw new RestException(ERROR, "You must proviced Id or Number of Opportunity to apply changes to it.", INVALID, HttpStatus.BAD_REQUEST);
        }
        ContactListItem lead = null;
        if (dto.getId() != null) {
            lead = crmServiceLocal.getLead(dto.getId());
        }
        if (lead == null) {
            throw new RestException(ERROR, "Opportunity is not found by given Id/Number.", INVALID, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/leads/activities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<ActivityTO>> getLeadActivities(@RequestParam("leadId") Integer id,
                                                        @RequestParam(value = "brief", required = false) Boolean brief) {
        log.info("REST request to get activities by lead id: {}", id);
        List<ActivityTO> activities;
        if (brief != null && brief) {
            activities = apiLeadService.getActivities(id);
        } else {
            activities = apiLeadService.getLastActivities(id);
        }
        return ResultTO.success(activities);
    }

    @PostMapping(path = "/leads/convert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> convertLead(@RequestBody LeadConvertTO request) {
        log.info("REST request to convert lead");
        OpportunityListItem opportunity = Optional.ofNullable(request.getOpportunity())
                .map(this::getOpportunityListItem)
                .orElse(null);
        crmService.convertLead(opportunity, request.getId());
        return ResultTO.success();
    }

    private OpportunityListItem getOpportunityListItem(OpportunityConvertTO opportunity) {
        OpportunityListItem item = new OpportunityListItem();
        item.setAssigneeId(opportunity.getAssignee().getId());
        item.setOpportunityName(opportunity.getName());
        item.setAmount(opportunity.getAmount());
        item.setStageId(opportunity.getStage().getId());
        item.setCopyLeadDetails(opportunity.isCopyLeadDetails());
        return item;
    }

}
