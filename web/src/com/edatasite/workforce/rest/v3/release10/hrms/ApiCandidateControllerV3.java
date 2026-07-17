package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.CandidateDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.service.ApiCandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

/**
 * User : Akhror on 02/06/2021
 */
@Tag(name = "Candidate", description = "Candidate Public API")
@RestController
@RequestMapping(value = "/candidate", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiCandidateControllerV3 {

    @Autowired
    private ApiCandidateService apiCandidateService;
    @Autowired
    private CrmContactManager contactManager;
    @Autowired
    private ContactService contactService;

    @Operation(summary = "Get Candidates list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Candidates"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResultTO<CandidateDTO>> getCandidates(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.CandidateListPanel);

        return ResultTO.success(apiCandidateService.getCandidatesList(fp));
    }

    @Operation(summary = "Get existing candidate by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "candidate"))
    @RequestMapping(path = "/summary", method = RequestMethod.POST)
    public ResultTO<CandidateDTO> getCandidateById(@RequestBody IdCode dto) throws RestException {
        return ResultTO.success(apiCandidateService.getById(dto));
    }

    @Operation(summary = "Create candidate")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Candidate"))
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<CandidateDTO> createCandidate(@Validated @RequestBody CandidateDTO candidateDTO) throws RestException {
//        if (candidateDTO.getId() != null || candidateDTO.getNumber() != null) {
//            throw new RestException(GENERAL_ERROR_MESSAGE, "Candidate ID or Number is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
//        }

        CandidateDTO newCandidate = apiCandidateService.save(candidateDTO, true);
        return ResultTO.success(newCandidate);
    }

    @Operation(summary = "Edit candidate")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "candidate"))
    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<CandidateDTO> updateCandidate(@Validated @RequestBody CandidateDTO candidateDTO) throws RestException {
        if (candidateDTO.getId() == null || candidateDTO.getId() < 1) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Candidate ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        CandidateDTO updatedCandidate = apiCandidateService.save(candidateDTO, false);
        return ResultTO.success(updatedCandidate);
    }

    @Operation(summary = "Patch Update existing candidate")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Candidate"))
    @RequestMapping(method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<CandidateDTO> patchUpdateCandidate(@RequestBody CandidateDTO candidateDTO) throws RestException {
        if (candidateDTO.getId() == null && candidateDTO.getObjectKey() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Candidate ID and Number are not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        CandidateDTO res = apiCandidateService.savePatch(candidateDTO);
        return ResultTO.success(res);
    }

    @Operation(summary = "Delete existing candidate by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Candidate"))
    @RequestMapping(path = "/{candidateId}", method = RequestMethod.DELETE)
    public Object deleteCandidate(@PathVariable final Integer candidateId) throws RestException {
        Optional.ofNullable(contactManager.get(candidateId)).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Candidate with this id is not found", NOT_FOUND, HttpStatus.BAD_REQUEST));

        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(candidateId);
        contactService.deleteContacts(ids, null, false);
        return ResultTO.success();
    }
}
