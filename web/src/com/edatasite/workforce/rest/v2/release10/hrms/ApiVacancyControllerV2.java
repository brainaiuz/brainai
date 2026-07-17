package com.edatasite.workforce.rest.v2.release10.hrms;

import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.app.RecruitmentServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseResultListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.PositionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.VacanciesRequestListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.VacancyTO;
import com.edatasite.workforce.rest.v2.release10.enums.VacancyStatusEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
@Tag(name = "Vacancy", description = "Vacancy API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiVacancyControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiVacancyControllerV2.class);

    @Autowired
    private RecruitmentServiceLocal recruitmentServiceLocal;
    @Autowired
    private CommonServiceLocal commonService;

    @Operation(summary = "Get Vacancy List", description = "Retrieves list of vacancies based on provided parameters \n" +
            "Status code can be followings: OPEN, IN_PROGRESS, PARTIALLY_FILLED, FILLED")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of vacancies"),
            @ApiResponse(responseCode = "400", description = "Start point and limit are required"),
            @ApiResponse(responseCode = "422", description = "Limit required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "400", description = "Job Family Id is required and should be more than zero")
    })
    @RequestMapping(value = "/list_vacancies", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getVacancyList(@RequestBody VacanciesRequestListTO requestListData) throws RestException {

        if (requestListData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Limit required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getStart().equals(requestListData.getLimit()) && requestListData.getLimit() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point and limit can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        /*if (requestListData.getJob_family_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Job Family Id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getJob_family_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Job Family Id must be positive", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }*/
        String status = null;
        if (StringUtils.isNotBlank(requestListData.getStatus_code())) {
            if (VacancyStatusEnum.OPEN.name().equalsIgnoreCase(requestListData.getStatus_code())) {
                status = EdsVacancy.VS_OPEN;
            } else if (VacancyStatusEnum.FILLED.name().equalsIgnoreCase(requestListData.getStatus_code())) {
                status = EdsVacancy.VS_FILLED;
            } else if (VacancyStatusEnum.IN_PROGRESS.name().equalsIgnoreCase(requestListData.getStatus_code())) {
                status = EdsVacancy.VS_IN_PROGRESS;
            } else if (VacancyStatusEnum.PARTIALLY_FILLED.name().equalsIgnoreCase(requestListData.getStatus_code())) {
                status = EdsVacancy.VS_PARTIALLY_FILLED;
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invalid status code. status_code should be of one OPEN, FILLED, IN_PROGRESS, PARTIALLY_FILLED ", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        try {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setStart(requestListData.getStart());
            filterParameter.setLimit(requestListData.getLimit());
            filterParameter.setStatusCode(status);
            filterParameter.setJobFamilyID(requestListData.getJob_family_id());

            List<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Vacancy);
            if (CollectionUtils.isNotEmpty(customFieldsItems)) {
                List<String> columnCodeNames = customFieldsItems.stream().map(CompanyCustomFieldItem::getColumnCode).toList();
                ListPanelToolRpc panelTools = new ListPanelToolRpc();
                panelTools.setColumnCodeName(new ArrayList<>(columnCodeNames));
                filterParameter.setListPanelTool(panelTools);
            }
            ListResult<VacancyItem> vacancyItemListResult = recruitmentServiceLocal.getVacancyList(filterParameter);
            ArrayList<VacancyTO> vacancyList = new ArrayList<>();

            for (VacancyItem vacancyItem : vacancyItemListResult.getList()) {
                VacancyTO vacancyTO = new VacancyTO();
                vacancyTO.setVacancy_id(vacancyItem.getObjectID());
                vacancyTO.setVacancy_description(vacancyItem.getDescription());
                vacancyTO.setVacancy_job_title(vacancyItem.getJobTitle());
                vacancyTO.setVacancy_job_requirements(vacancyItem.getJobRequirements());
                if (vacancyItem.getStatus() != null) {
                    vacancyTO.setVacancy_status(new SelectItemTO(vacancyItem.getStatus()));
                }
                if (vacancyItem.getNumberData() != null) {
                    vacancyTO.setVacancy_number(vacancyItem.getNumberData().getNumberString());
                }

                SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

                if (vacancyItem.getStartDate() != null) {
                    vacancyTO.setVacancy_start_date(longDateTimezoneFormat.format(vacancyItem.getStartDate()));
                }
                if (vacancyItem.getEndDate() != null) {
                    vacancyTO.setVacancy_end_date(longDateTimezoneFormat.format(vacancyItem.getEndDate()));
                }
                if (vacancyItem.getPositionItem() != null) {
                    vacancyTO.setVacancy_position(new PositionTO(vacancyItem.getPositionItem()));
                }

                //vacancy custom fields
                if (CollectionUtils.isNotEmpty(customFieldsItems)) {
                    List<CompanyCustomFieldItem> vacancyCustomFieldItems = new ArrayList<>();
                    customFieldsItems.forEach(cf -> {
                        if (vacancyItem.getCustomFieldValuesItems() != null && vacancyItem.getCustomFieldValuesItems().get(cf.getColumnCode()) != null) {
                            if (cf.getColumnCode().contains("string")) {
                                cf.setFieldStringValue(vacancyItem.getCustomFieldValuesItems().get(cf.getColumnCode()).toString());
                            } else if (cf.getColumnCode().contains("double")) {
                                cf.setFieldStringValue(vacancyItem.getCustomFieldValuesItems().get(cf.getColumnCode()).toString());
                            } else if (cf.getColumnCode().contains("date")) {
                                cf.setFieldDateNonConvertedValue(new DateNonConvertable((Date) (vacancyItem.getCustomFieldValuesItems().get(cf.getColumnCode()))));
                            }
                            vacancyCustomFieldItems.add(cf);
                        }
                    });
                    vacancyTO.setCustom_fields(getCustomFields(vacancyCustomFieldItems));
                }

                vacancyList.add(vacancyTO);
            }

            return successResponse(new ResponseResultListData<>(vacancyList, vacancyItemListResult.getTotal()));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Job Family List", description = "Retrieves list of job families available")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of job families")
    })
    @RequestMapping(value = "/list_job_families", method = RequestMethod.GET)
    public Object getJobFamilyList() throws RestException {
        try {
            SelectItem[] jobFamiliesList = recruitmentServiceLocal.getVacancyJobFamily();
            ArrayList<SelectItemTO> jobFamilies = new ArrayList<>();
            for (SelectItem it : jobFamiliesList) {
                jobFamilies.add(new SelectItemTO(it));
            }
            ResponseListData<SelectItemTO> result = new ResponseListData<>();
            result.setList(jobFamilies);
            return successResponse(result);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
