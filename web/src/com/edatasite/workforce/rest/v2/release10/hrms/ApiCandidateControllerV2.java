package com.edatasite.workforce.rest.v2.release10.hrms;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.reference.PhoneReference;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldPostTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.AddCandidateResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.CandidateAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.SpokenLanguageTO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
@Tag(name = "Candidate", description = "Candidate API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCandidateControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiCandidateControllerV2.class);
    @Autowired
    private ContactServiceLocal contactServiceLocal;

    @Operation(summary = "Add Candidate", description = "Adds new candidate \n\n Date format should be YYYY-MM-DD ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true if successfully added or false if not added successfully with error code"),
            @ApiResponse(responseCode = "400", description = "First name required"),
            @ApiResponse(responseCode = "409", description = "Candidate with provided name or email already exists in the system"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/add_candidate", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object addCandidate(@RequestBody CandidateAddTO candidate) throws RestException {
        try {
            if (StringUtils.isBlank(candidate.getFirst_name())) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "First name required", REQUIRED, HttpStatus.BAD_REQUEST);
            }

            ContactListItem contactListItem = new ContactListItem();
            if (candidate.getVacancy_list() != null && !candidate.getVacancy_list().isEmpty()) {
                ArrayList<SelectItem> vacancyList = new ArrayList<>();
                for (Integer vacancyId : candidate.getVacancy_list()) {
                    vacancyList.add(new SelectItem(vacancyId, true));
                }
                contactListItem.setVacancies(vacancyList);
            }
            contactListItem.setFirstName(candidate.getFirst_name());
            contactListItem.setLastName(candidate.getLast_name());
            contactListItem.setBirthDate(candidate.getDate_of_birth() != null ? new DateNonConvertable(candidate.getDate_of_birth()) : null);
            contactListItem.setCurrentEmployer(candidate.getEmployer());
            contactListItem.setSkills(candidate.getSkills());
            contactListItem.setExpectedSalary(candidate.getExpected_salary());
            contactListItem.setWorkExperience(candidate.getWork_experience());
            //Email
            contactListItem.setEmails();
            contactListItem.addParam(Constants.CONTACT_EMAILS, AddressReference.HOME.getId(), candidate.getEmail());
            contactListItem.setPrimaryEmail(candidate.getEmail());
            //Phone
            contactListItem.setPhones();
            contactListItem.addParam(Constants.CONTACT_PHONES, PhoneReference.MOBILE.getId(), candidate.getPhone());
            contactListItem.setPrimaryPhone(candidate.getPhone());
            if (candidate.getAddresses() != null) {
                ArrayList<Address> addresses = new ArrayList<>();
                for (AddressTO addressTO : candidate.getAddresses()) {
                    addresses.add(addressTO.toAddressItem());
                }
                contactListItem.setAddresses(addresses);
            }
            if (candidate.getSpoken_languages() != null) {
                ArrayList<SpokenLanguageItem> spokenLanguages = new ArrayList<>();
                for (SpokenLanguageTO spokenLanguageTO : candidate.getSpoken_languages()) {
                    spokenLanguages.add(spokenLanguageTO.toLanguageItem());
                }
                contactListItem.setSpokingLanguages(spokenLanguages);
            }
            if (candidate.getCustom_fields() != null) {
                ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();
                for (CustomFieldPostTO customFieldPostTO : candidate.getCustom_fields()) {
                    customFieldItems.add(customFieldPostTO.toCompanyCustomFieldItem());
                }
                contactListItem.setCustomFields(customFieldItems);
            }
            contactListItem.setContactType(ContactListItem.CANDIDATE);
            Integer createdCandidateId = contactServiceLocal.saveContact(contactListItem, null, null, false, false);
            if (createdCandidateId > 0) {
                return successResponse(new AddCandidateResultTO(createdCandidateId));
            } else {
                throw new RestException(ERROR_MESSAGE, "Candidate with name " + candidate.getFirst_name() + " or email " + candidate.getEmail() + " exists in the system. Please, ensure you are not entering a duplicate or enter a different value.", CONFLICT, HttpStatus.CONFLICT);
            }
        } catch (RestException e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Upload Candidate Attachment", description = "Uploads the file to the specified candidate")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true if successfully uploaded or false if not uploaded successfully with error code"),
            @ApiResponse(responseCode = "400", description = "candidate_id and file are required"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/candidate_attachment_upload", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object uploadCandidateAttachment(@RequestParam("candidate_id") Integer candidate_id,
                                            @RequestParam("file") MultipartFile file) throws RestException {
        if (candidate_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "candidate_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (file == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        FileResource fileResource = documentsServiceLocal.saveDocumentFile(file, null, Constants.F_CANDIDATE, candidate_id, "");
        if (fileResource != null) {
            return successResponse(new ResponseData());
        } else {
            throw new RestException(ERROR_MESSAGE, "Error occurred while uploading candidate attachment", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
