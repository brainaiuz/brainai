package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.VacancyDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.service.ApiVacancyService;
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

import java.util.Optional;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

/**
 * User : Akhror on 29/06/2021
 */
@Tag(name = "Vacancy", description = "Vacancy Public API")
@RestController
@RequestMapping(value = "/vacancy", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiVacancyControllerV3 {

    @Autowired
    private ApiVacancyService apiVacancyService;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private RecruitmentService recruitmentService;

    @Operation(summary = "Get Vacancies list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Vacancies"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ListResultTO<VacancyDTO>> getVacancies(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.VacancyListPanel);

        return ResultTO.success(apiVacancyService.getVacanciesList(fp));
    }

    @Operation(summary = "Get existing vacancy by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "vacancy"))
    @RequestMapping(path = "/{vacancyId}", method = RequestMethod.GET)
    public ResultTO<VacancyDTO> getVacancyById(@PathVariable final Integer vacancyId) throws RestException {
        return ResultTO.success(apiVacancyService.getById(vacancyId));
    }

    @Operation(summary = "Create vacancy")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Vacancy"))
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<VacancyDTO> createVacancy(@Validated @RequestBody VacancyDTO vacancyDTO) throws RestException {
        if (vacancyDTO.getId() != null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Candidate ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        VacancyDTO newVacancy = apiVacancyService.save(vacancyDTO, true);
        return ResultTO.success(newVacancy);
    }

    @Operation(summary = "Edit vacancy")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "vacancy"))
    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<VacancyDTO> updateVacancy(@Validated @RequestBody VacancyDTO vacancyDTO) throws RestException {
        if (vacancyDTO.getId() == null || vacancyDTO.getId() < 1) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Vacancy ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        VacancyDTO updatedVacancy = apiVacancyService.save(vacancyDTO, false);
        return ResultTO.success(updatedVacancy);
    }

    @Operation(summary = "Delete existing vacancy by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "News"))
    @RequestMapping(path = "/{vacancyId}", method = RequestMethod.DELETE)
    public Object deleteVacancy(@PathVariable final Integer vacancyId) throws RestException {
        Optional.ofNullable(vacancyManager.get(vacancyId)).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Vacancy with this id is not found", NOT_FOUND, HttpStatus.BAD_REQUEST));

        recruitmentService.deleteVacancy(vacancyId);
        return ResultTO.success();
    }

}
