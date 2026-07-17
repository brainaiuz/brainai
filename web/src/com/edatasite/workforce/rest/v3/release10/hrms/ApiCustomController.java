package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsShiftItem;
import com.edatasite.workforce.core.domain.EdsShiftSettings;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsSpokenLanguages;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFormCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCustomItemTableCF;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.customform.EdsCustomItemTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.NewLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.TimeSlot;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.enums.AttendanceHoursType;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeePresentItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AttendanceHoursManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.db.DependentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.RecruitmentIntegrationManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.SpokenLanguagesManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.gwt.hrms.client.rpc.RotationItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItem;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.FacetFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.CFItemExistanceDto;
import com.edatasite.workforce.rest.v3.release10.core.to.CheckExistanceRequestDto;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFieldTo;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFormDto;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.ListingFilterDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.LocaleDto;
import com.edatasite.workforce.rest.v3.release10.core.to.QuizScoreDto;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.GTLTripDemoDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.HHIdDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.HHLanguageDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.HHRequestDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.HHSalaryDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.TripDemoRequestDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovAddressDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovAddressResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovDependentDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovDependentResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovMarriageDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovMarriageResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovPassportResponseDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovPositionDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovPositionResponseDto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_LEAVE_REQUEST;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CUSTOM_FORM_ITEM_STATUS_APPROVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CUSTOM_FORM_ITEM_STATUS_REJECTED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DRAFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.LR_STATUS_SS_DENIED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ROTATION_APPROVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ROTATION_DRAFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ROTATION_SUBMITTED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SALARY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SHIFT_APPROVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SHIFT_REJECTED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_ADDITIONAL_PAYMENT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CASE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CASH_ADVANCE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CERTIFICATE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CHART_OF_ACCOUNT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CONTACT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_COURSE_BOOKING_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_COURSE_SCHEDULE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CRM_ACCOUNT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_CUSTOM_FORM_ITEM_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_DEPARTMENT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_EMPLOYEE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_EMPLOYEE_STEP_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_EVENT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_EXPENSE_REPORT_CLAIMS_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_FOLDER_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_GROUP_PAYRUN_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_LEAVE_REQUEST_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_NEWS_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_OPPORTUNITY_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_POSITION_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_PRODUCTS_SERVICES_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_PROJECT_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_PURCHASE_INVOICE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_PURCHASE_ORDER_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_REQUEST_FOR_QUOTE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_SALEINVOICE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_SALEQUOTE_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_SHIPPING_DATA_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_SINGLE_PAYRUN_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_TASK_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_VACANCY_CORE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.VALIDATION;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.SERVER_ERROR;

/**
 * User : Akhror
 * Date : 14.12.2021
 */
@Tag(name = "Custom Controller For GTL/Agrobank", description = "Custom Public API For GTL/Agrobank")
@RestController
@RequestMapping(value = "/custom", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiCustomController {
    private static final Logger log = LoggerFactory.getLogger(ApiCustomController.class);
    private final EmployeeManager employeeManager;
    private final CommonServiceLocal commonServiceLocal;
    private final AllInOneServiceLocal allInOneServiceLocal;
    private final LeaveReasonManager reasonManager;
    private final AvailabilityService availabilityService;
    private final CommonService commonService;
    private final CustomFormItemManager customFormItemManager;
    private final RelationManager relationManager;
    private final UserManager userManager;
    private final ReferenceManager referenceManager;
    private final RecruitmentService recruitmentService;
    private final ContactService contactService;
    private final PayrollService payrollService;
    private final PayrollServiceLocal payrollServiceLocal;
    private final EmployeePayrollSettingsManager employeePayrollSettingsManager;
    private final HrmsService hrmsService;
    private final HrmsServiceLocal hrmsServiceLocal;
    private final ShiftManager shiftManager;
    private final AttendanceHoursManager attendanceHoursManager;
    private final SickRequestManager sickRequestManager;
    private final VacancyManager vacancyManager;
    private final CompanyCustomFieldsManager customFieldsManager;
    private final SpokenLanguagesManager spokenLanguagesManager;
    private final RecruitmentIntegrationManager recruitmentIntegrationManager;
    private final CrmContactManager candidateManager;
    private final CompanyCustomFieldsManager companyCustomFieldsManager;
    private final CustomFormManager customFormManager;
    private final RestTemplate restTemplate = new RestTemplate();
    private final DependentManager dependentManager;
    private final BackendService backendService;

    @Autowired
    public ApiCustomController(EmployeeManager employeeManager, CommonServiceLocal commonServiceLocal, AllInOneServiceLocal allInOneServiceLocal, LeaveReasonManager reasonManager, AvailabilityService availabilityService, CommonService commonService, CustomFormItemManager customFormItemManager, RelationManager relationManager, UserManager userManager, ReferenceManager referenceManager, RecruitmentService recruitmentService, ContactService contactService, PayrollService payrollService, PayrollServiceLocal payrollServiceLocal, EmployeePayrollSettingsManager employeePayrollSettingsManager, HrmsService hrmsService, HrmsServiceLocal hrmsServiceLocal, ShiftManager shiftManager, AttendanceHoursManager attendanceHoursManager, SickRequestManager sickRequestManager, VacancyManager vacancyManager, CompanyCustomFieldsManager customFieldsManager, SpokenLanguagesManager spokenLanguagesManager, RecruitmentIntegrationManager recruitmentIntegrationManager, CrmContactManager candidateManager, CompanyCustomFieldsManager companyCustomFieldsManager, CustomFormManager customFormManager, DependentManager dependentManager, BackendService backendService) {
        this.employeeManager = employeeManager;
        this.commonServiceLocal = commonServiceLocal;
        this.allInOneServiceLocal = allInOneServiceLocal;
        this.reasonManager = reasonManager;
        this.availabilityService = availabilityService;
        this.commonService = commonService;
        this.customFormItemManager = customFormItemManager;
        this.relationManager = relationManager;
        this.userManager = userManager;
        this.referenceManager = referenceManager;
        this.recruitmentService = recruitmentService;
        this.payrollService = payrollService;
        this.payrollServiceLocal = payrollServiceLocal;
        this.contactService = contactService;
        this.employeePayrollSettingsManager = employeePayrollSettingsManager;
        this.hrmsService = hrmsService;
        this.hrmsServiceLocal = hrmsServiceLocal;
        this.shiftManager = shiftManager;
        this.attendanceHoursManager = attendanceHoursManager;
        this.sickRequestManager = sickRequestManager;
        this.vacancyManager = vacancyManager;
        this.customFieldsManager = customFieldsManager;
        this.spokenLanguagesManager = spokenLanguagesManager;
        this.recruitmentIntegrationManager = recruitmentIntegrationManager;
        this.candidateManager = candidateManager;
        this.companyCustomFieldsManager = companyCustomFieldsManager;
        this.customFormManager = customFormManager;
        this.dependentManager = dependentManager;
        this.backendService = backendService;
    }

    @Operation(summary = "Create Leave Request")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Leave Request"))
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<DynamicDto> createLeaveRequest(@Validated @RequestBody TripDemoRequestDto requestDto) throws RestException {
        return ResultTO.success(createLR(requestDto, null));
    }

    @Operation(summary = "Create Mat Pomosh")
    @ApiResponses(value = @ApiResponse(responseCode = "200",description = "Mat Pomosh"))
    @RequestMapping(path = "/mat_pomosh",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<DynamicDto> createMatPomosh(@Validated @RequestBody HashMap dto) throws RestException{
        System.out.println(dto.toString());
        if ("MATPOMOSH_DRAFT".equals((String) dto.get("kpiDocumentType"))) {
            FormItems item = new FormItems();
            item.setFormID("MATPOMOSCH_FORM");
            item.setStatusCode(Constants.CUSTOM_FORM_ITEM_STATUS_DRAFT);
            ArrayList<CompanyCustomFieldItem> cfs = commonService.getCompanyCategoryCustomFields(115);
            List<CustomFieldRequest> customFields = new ArrayList<>();
            final HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

            Map json = (Map) dto.get("json");
//            customFields.add(new CustomFieldRequest("",(String) dto.get("kpiDocumentType")));
            customFields.add(new CustomFieldRequest("С даты", json.get("fromDate")));
            customFields.add(new CustomFieldRequest("До даты", json.get("toDate")));
            customFields.add(new CustomFieldRequest("Дата", json.get("protocolDate")));
            customFields.add(new CustomFieldRequest("Номер Приказа", (String) json.get("sys_reg_number_by_type")));
            customFields.add(new CustomFieldRequest("mat_pomosh_qr_code", (String) dto.get("verifyUrl")));
            if (json.get("sys_union_chief") != null &&((LinkedHashMap) json.get("sys_union_chief")).get("pinfl") != null ) {
                EdsEmployee unionChef = employeeManager.getEmployeeByNumber((String) ((LinkedHashMap) json.get("sys_union_chief")).get("pinfl"));
                customFields.add(new CustomFieldRequest("Руководитель профсоюза", new ItemDto(unionChef.getObjectID(), unionChef.getFullName())));
            }


            ArrayList<Map<String, ArrayList>> tableDataList = (ArrayList<Map<String, ArrayList>>) json.get("tableData");
            ArrayList<CustomTableRpc> participants = new ArrayList<>();
            for (Map<String, ArrayList> stringObjectMap : tableDataList) {
                for (ArrayList value : stringObjectMap.values()) {
                    for (Object o : value) {
                        LinkedHashMap linkedHashMap = (LinkedHashMap) o;
                        if (employeeManager.getEmployeeByNumber((String) linkedHashMap.get("pinfl")) != null ) {
                            CustomTableRpc customTableRpc = new CustomTableRpc();
                            customTableRpc.setUuid("ITEM_TABLE_qx4KT9LF97");
                            List<CustomFieldRequest> customFieldRequests = new ArrayList<>();
                            ArrayList<CompanyCustomFieldItem> itemTableCfs = commonServiceLocal.getCompanyCustomFieldsByCategoryForListView(ViewName.CustomFormItemTable, "ITEM_TABLE_qx4KT9LF97");
                            EdsEmployee employee = employeeManager.getEmployeeByNumber((String) linkedHashMap.get("pinfl"));
                            CustomFieldRequest matPomoshEmployee = new CustomFieldRequest("MatPomoshEmployee", new ItemDto(employee.getObjectID(), employee.getFullName()));
                            CustomFieldRequest clause = new CustomFieldRequest("Clause", (String) linkedHashMap.get("code"));
                            CustomFieldRequest edocNumber = new CustomFieldRequest("EdocDocNumber", (String) linkedHashMap.get("documentNumber"));
                            CustomFieldRequest salaryRate = new CustomFieldRequest("SalaryRate", (String) linkedHashMap.get("minSalaryRate"));
                            customFieldRequests.add(matPomoshEmployee);
                            customFieldRequests.add(clause);
                            customFieldRequests.add(edocNumber);
                            customFieldRequests.add(salaryRate);

                            customTableRpc.setItemCustomFields(CustomFieldsUtils.convertCustomFields(customFieldRequests, itemTableCfs, null));
                            participants.add(customTableRpc);
                        }
                    }
                }
            }
            map.put("ITEM_TABLE_qx4KT9LF97", participants);
            item.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(customFields, cfs, null));
            item.setTableItems(map);
            Integer id = commonService.saveCustomFormItem(item);
            DynamicDto result = new DynamicDto();
            result.addProperty("id", id);
            result.addProperty("type", "MATPOMOSH_DRAFT");
            return ResultTO.success(result);
        } else {
            Integer id = dto.get("kpiDocumentId") != null ? Integer.parseInt((String) dto.get("kpiDocumentId")) : null;
            FormItems item = commonService.getCustomFormItem(id, 115, "MATPOMOSCH_FORM", false, null, null, null, null);
            item.setStatusCode(CUSTOM_FORM_ITEM_STATUS_SUBMITTED);
            commonService.saveCustomFormItem(item);

            DynamicDto result = new DynamicDto();
            result.addProperty("id", id);
            result.addProperty("type", "MATPOMOSH_SUBMIT");
            return ResultTO.success(result);
        }
    }



    @Operation(summary = "Create Komandirovka Form")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Leave Request"))
    @RequestMapping(path = "/custom_form", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<DynamicDto> createCustomForm(@Validated @RequestBody GTLTripDemoDto dto) throws RestException {
        TripDemoRequestDto requestDto = dto.getJson();
        int typeId = Integer.parseInt((String) ((LinkedHashMap) requestDto.getProperties().get("selectedTitle")).get("Uniqueid"));
        String summary = (String) requestDto.getProperties().get("summary");
        CompanyCustomFieldItem summaryCf = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "Краткое содержание");
        summaryCf.setFieldStringValue(summary);
        CompanyCustomFieldItem subject = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "subject");
        Integer id = null;
        Integer documentId = null;
        if (requestDto.getProperties().get("adm_text") != null && requestDto.getProperties().get("adm_text") instanceof LinkedHashMap) {
            documentId = (Integer) ((LinkedHashMap) requestDto.getProperties().get("adm_text")).get("id");
        }
        if (typeId == 2 && documentId != null) {
            FormItems oldItem = commonService.getCustomFormItem(documentId, 87, "KOMANDIROVKA_FORM", false, null, null, null, null);
            HashSet<Integer> empIds = new HashSet<>();
            for (int i = 0; i < 50; i++) {
                LinkedHashMap<String, Object> employee = (LinkedHashMap) requestDto.getSys_table_for_information().getProperties().get("sys_for_information_" + i);
                if (employee == null) {
                    continue;
                }
                LinkedHashMap<String, Object> object = employee.get("new") != null ? (LinkedHashMap) employee.get("new") : null;
                String pnfl = ((String) ((LinkedHashMap<String, Object>) employee.get("user")).get("pinfl"));
                EdsEmployee emp = employeeManager.getEmployeeByNumber(pnfl);
                if (emp == null) {
                    throw new RestException("Employee with pinfl : " + pnfl + " not found", "Not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
                empIds.add(emp.getObjectID());
            }
            HashSet<Integer> lrIds = new HashSet<>();
            oldItem.getRelations().stream().filter(r -> r.getToType().equals(TYPE_LEAVE_REQUEST)).forEach(r -> lrIds.add(r.getToID()));
            for (Integer lrId : lrIds) {
                EdsSickRequest sickRequest = sickRequestManager.get(lrId);
                if (empIds.contains(sickRequest.getEmployee().getObjectID())) {
                    availabilityService.updateApprove(LR_STATUS_SS_DENIED, lrId, true, null, true);
                }
            }
            FormItems item = new FormItems();
            ArrayList<CompanyCustomFieldItem> cfs = new ArrayList<>();
            item.setStatusCode(CUSTOM_FORM_ITEM_STATUS_REJECTED);
            subject.setFieldStringValue(String.valueOf(typeId));
            cfs.add(subject);

            CompanyCustomFieldItem numberCf = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "ord_number");
            String number = commonService.getMaxValueOfAutoNumbering(numberCf);
            numberCf.setFieldStringValue(number);
            cfs.add(numberCf);

            CompanyCustomFieldItem oldNumber = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "Old Number");
//            for (CompanyCustomFieldItem cf : item.getCustomFieldItems()) {
//                if (cf.getColumnCode().equals("string_value17")) {
//                    oldNumber.setFieldStringValue(cf.getFieldStringValue());
//                    break;
//                }
//            }
            cfs.add(oldNumber);

            CompanyCustomFieldItem rejectedDate = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "rejected date");
            SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            rejectedDate.setFieldStringValue(shortDateFormat.format(new Date()));
            cfs.add(rejectedDate);


            CompanyCustomFieldItem numberCf1 = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "ord_number");
            String number1 = commonService.getMaxValueOfAutoNumbering(numberCf);
            CustomFieldRequest ord_number1 = new CustomFieldRequest("ord_number", number);

            item.setAutoNumber(String.valueOf(ord_number1));
            cfs.add(summaryCf);
            item.setCustomFieldItems(cfs);
            item.setFormID("KOMANDIROVKA_FORM");
            id = commonService.saveCustomFormItem(item);
            ArrayList<RelationItem> relations = saveKomandirovka(item, requestDto, typeId, summary, dto);
            allInOneServiceLocal.saveRelations("KOMANDIROVKA_FORM", id, "", relations);
        } else if (typeId == 5 && documentId != null) {
            FormItems item = commonService.getCustomFormItem(documentId, 87, "KOMANDIROVKA_FORM", false, null, null, null, null);
            HashSet<Integer> lrIds = new HashSet<>();
            item.getRelations().stream().filter(r -> r.getToType().equals(TYPE_LEAVE_REQUEST)).forEach(r -> lrIds.add(r.getToID()));
            HashMap<Integer, Date> recallDates = new HashMap<>();
            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            for (int i = 0; i <= 50; i++) {
                LinkedHashMap<String, Object> employee = (LinkedHashMap) requestDto.getSys_table_for_information().getProperties().get("sys_for_information_" + i);
                if (employee == null) {
                    continue;
                }
                String pnfl = ((String) ((LinkedHashMap<String, Object>) employee.get("user")).get("pinfl"));
                EdsEmployee emp = employeeManager.getEmployeeByNumber(pnfl);
                if (emp == null) {
                    throw new RestException("Employee with pinfl : " + pnfl + " not found", "Not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
                Object business_trip_date = employee.get("business_trip_date");

                Date recallDate = null;
                try {
                    recallDate = formatter.parse((String) business_trip_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                recallDates.put(emp.getObjectID(), recallDate);
            }
            for (Integer lrId : lrIds) {
                StatisticsLeaveRequest lr = availabilityService.getLeaveRequest(lrId);
                if (recallDates.containsKey(lr.getEmployeeId())) {
                    DateNonConvertable dueDate = lr.getRecallDDate() != null ? lr.getRecallDDate() : lr.getEndDDate();
                    if (recallDates.get(lr.getEmployeeId()) == null) {
                        throw new RestException(GENERAL_ERROR_MESSAGE, "Recall date is not specified", NOT_FOUND, HttpStatus.NOT_FOUND);
                    }
                    Date recallDate = recallDates.get(lr.getEmployeeId());
                    if (DateUtil.compareByDate(recallDate, lr.getStartDDate().getNonConvertedDate()) && DateUtil.compareByDate(dueDate.getNonConvertedDate(), recallDate)) {
                        availabilityService.restoreLeave(lr.getEmployeeId(), lrId, new DateNonConvertable(recallDate), dueDate, lr.getReasonCode());
                    } else {
                        throw new RestException(GENERAL_ERROR_MESSAGE, "The recall date must be between the start date and end date (recalled date - if already has been recalled)", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }
            subject.setFieldStringValue(String.valueOf(typeId));
            item.getCustomFieldItems().add(subject);
            item.getCustomFieldItems().add(summaryCf);
            commonService.saveCustomFormItem(item);
            id = item.getObjectID();
        } else {
            FormItems item;
            if (typeId == 1 || documentId == null) {
                item = new FormItems();
            } else {
                item = commonService.getCustomFormItem(documentId, 87, "KOMANDIROVKA_FORM", false, null, null, null, null);
                HashSet<Integer> lrIds = new HashSet<>();
                item.getRelations().stream().filter(r -> r.getToType().equals(TYPE_LEAVE_REQUEST)).forEach(r -> lrIds.add(r.getToID()));
                for (Integer lrId : lrIds) {
                    availabilityService.deleteSickRequestListByParent(lrId);
                }
            }
            ArrayList<RelationItem> relations = saveKomandirovka(item, requestDto, typeId, summary, dto);
            id = commonService.saveCustomFormItem(item);
            allInOneServiceLocal.saveRelations("KOMANDIROVKA_FORM", id, "", relations);
        }
        DynamicDto result = new DynamicDto();
        result.addProperty("id", id);
        result.addProperty("type", "CUSTOM_FORM_KOMANDIROVKA");
        return ResultTO.success(result);
    }

    private ArrayList<RelationItem> saveKomandirovka(FormItems item, TripDemoRequestDto requestDto, int typeId, String summary, GTLTripDemoDto dto) throws RestException {
        item.setFormID("KOMANDIROVKA_FORM");
        item.setStatusCode(Constants.CUSTOM_FORM_ITEM_STATUS_DRAFT);

        EdsEmployee approver = null;
        if (requestDto.getSys_approval_1() != null && !requestDto.getSys_approval_1().getProperties().isEmpty()) {
            String approverPnfl = (String) requestDto.getSys_approval_1().getProperties().get("pinfl");
            approver = employeeManager.getEmployeeByNumber(approverPnfl);
            if (approver == null) {
                throw new RestException("Employee with pinfl : " + approverPnfl + " not found", "Not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }

        }
        ArrayList<CompanyCustomFieldItem> cfs = commonService.getCompanyCategoryCustomFields(87);
        List<CustomFieldRequest> customFields = new ArrayList<>();

        customFields.add(new CustomFieldRequest("Кому ", approver != null ? approver.getObjectID() : null));
        customFields.add(new CustomFieldRequest("Тема", getTranslates("selectedTitle", requestDto, false)));
        customFields.add(new CustomFieldRequest("Дата", requestDto.getProperties().get("date")));
        customFields.add(new CustomFieldRequest("Место командирования - международный", getTranslates("selectedTripeCity", requestDto, true)));
        customFields.add(new CustomFieldRequest("Место командирования - Узбекистан", getTranslates("selectedTripPlace", requestDto, true)));
        customFields.add(new CustomFieldRequest("Принимающая организация (наименование, адрес)", getTranslates("selectedHostOrg", requestDto, true)));
        customFields.add(new CustomFieldRequest("Срок командирования (без учета нахождения в пути)", requestDto.getProperties().get("term_of_business_trip")));
        customFields.add(new CustomFieldRequest("Order Number", requestDto.getProperties().get("sys_reg_number_by_type")));
        customFields.add(new CustomFieldRequest("Цель командирования", requestDto.getProperties().get("trip_purpose")));
        customFields.add(new CustomFieldRequest("Основание (письмо-приглашение, распоряжение и т.д.)", requestDto.getProperties().get("reason")));
        customFields.add(new CustomFieldRequest("Subject", typeId));
        customFields.add(new CustomFieldRequest("Краткое содержание", summary));

        CompanyCustomFieldItem numberCf = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "ord_number");
        String number = commonService.getMaxValueOfAutoNumbering(numberCf);
        customFields.add(new CustomFieldRequest("ord_number", number));

        Map<String, Object> sysAgreed1Map = ((LinkedHashMap) requestDto.getProperties().get("sys_agreed_1"));
        Map<String, Object> sysAgreed2Map = ((LinkedHashMap) requestDto.getProperties().get("sys_agreed_2"));
        Map<String, Object> sysAgreed3Map = ((LinkedHashMap) requestDto.getProperties().get("sys_agreed_3"));
        Map<String, Object> sysAgreed4Map = ((LinkedHashMap) requestDto.getProperties().get("sys_agreed_4"));
        if (sysAgreed1Map != null && !sysAgreed1Map.isEmpty()) {
            EdsEmployee sys_agreed1 = employeeManager.getEmployeeByNumber((String) sysAgreed1Map.get("pinfl"));
            customFields.add(new CustomFieldRequest("Административный отдел ", sys_agreed1 != null ? new ItemDto(sys_agreed1.getObjectID(), sys_agreed1.getFullName()) : null));
        }
        if (sysAgreed2Map != null && !sysAgreed2Map.isEmpty()) {
            EdsEmployee sys_agreed2 = employeeManager.getEmployeeByNumber((String) sysAgreed2Map.get("pinfl"));
            customFields.add(new CustomFieldRequest("Отдел управления персоналом", sys_agreed2 != null ? new ItemDto(sys_agreed2.getObjectID(), sys_agreed2.getFullName()) : null));
        }
        if (sysAgreed3Map != null && !sysAgreed3Map.isEmpty()) {
            EdsEmployee sys_agreed3 = employeeManager.getEmployeeByNumber((String) sysAgreed3Map.get("pinfl"));
            customFields.add(new CustomFieldRequest("Отдел эксплуатации автотранспорта", sys_agreed3 != null ? new ItemDto(sys_agreed3.getObjectID(), sys_agreed3.getFullName()) : null));
        }
        if (sysAgreed4Map != null && !sysAgreed4Map.isEmpty()) {
            EdsEmployee sys_agreed4 = employeeManager.getEmployeeByNumber((String) sysAgreed4Map.get("pinfl"));
            customFields.add(new CustomFieldRequest("Административный отдел 2", sys_agreed4 != null ? new ItemDto(sys_agreed4.getObjectID(), sys_agreed4.getFullName()) : null));
        }
        EdsEmployee creator = employeeManager.getEmployeeByNumber(((String) ((LinkedHashMap) requestDto.getProperties().get("sys_initiator")).get("pinfl")));
        customFields.add(new CustomFieldRequest("От кого", creator != null ? creator.getObjectID() : null));

        customFields.add(new CustomFieldRequest("qrlink2", "https://edoc.uzgtl.com/verify/" + dto.getOrdDocumentId()));

        Map<String, Object> soglasovano = (LinkedHashMap) requestDto.getProperties().get("sys_table_agreed");
        StringBuilder soglasovanoString = null;
        if (soglasovano != null && !soglasovano.isEmpty()) {
            for (int i = 0; i < 50; i++) {
                Map<String, Object> emp = (LinkedHashMap) soglasovano.get("sys_agreed_" + i);
                if (emp == null || emp.isEmpty()) {
                    continue;
                }
                EdsEmployee employee = employeeManager.getEmployeeByNumber((String) ((Map) emp.get("user")).get("pinfl"));
                if (employee == null) {
                    continue;
                }
                String department;
                EdsEmployeeDepartment employeeDepartment = employee.getEmployeeDepartment();
                if (employeeDepartment != null) {
                    EdsDepartment edsDepartment = employeeDepartment.getTeam();
                    if (edsDepartment.getLocale() != null) {
                        department = edsDepartment.getLocale().getEnglish() + ";" + edsDepartment.getLocale().getUzbek();
                    } else {
                        department = edsDepartment.getName();
                    }
                } else {
                    department = "N/A";
                }
                String position;
                EdsPosition employeePosition = employee.getPosition();
                if (employeePosition != null) {
                    if (employeePosition.getLocale() != null) {
                        position = employeePosition.getLocale().getEnglish() + ";" + employeePosition.getLocale().getUzbek();
                    } else {
                        position = employeePosition.getName();
                    }
                } else {
                    position = "N/A";
                }
                if (soglasovanoString == null) {
                    soglasovanoString = new StringBuilder();
                    soglasovanoString.append(employee.getFullName()).append(" ").append(employee.getMiddleName()).append("--").append(position).append("--").append(department);
                } else {
                    soglasovanoString.append(",").append(employee.getFullName()).append(" ").append(employee.getMiddleName()).append("--").append(position).append("--").append(department);
                }
            }
        }
        customFields.add(new CustomFieldRequest("Согласовано (служебка)", soglasovanoString != null ? soglasovanoString.toString() : null));
        item.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(customFields, cfs, null));

        ArrayList<RelationItem> relations = new ArrayList<>();
        final HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();
        ArrayList<CustomTableRpc> participants = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            CustomTableRpc customTableRpc = new CustomTableRpc();
            customTableRpc.setUuid("ITEM_TABLE_eajC8xXCGt");
            ArrayList<CompanyCustomFieldItem> itemTableCfs = commonServiceLocal.getCompanyCustomFieldsByCategoryForListView(ViewName.CustomFormItemTable, "ITEM_TABLE_eajC8xXCGt");
            List<CustomFieldRequest> customFieldRequests = new ArrayList<>();
            LinkedHashMap<String, Object> employee = (LinkedHashMap) requestDto.getSys_table_for_information().getProperties().get("sys_for_information_" + i);
            if (employee == null) {
                continue;
            }
            LinkedHashMap<String, Object> object = employee.get("new") != null ? (LinkedHashMap) employee.get("new") : null;
            String pnfl = ((String) ((LinkedHashMap<String, Object>) employee.get("user")).get("pinfl"));
            EdsEmployee emp = employeeManager.getEmployeeByNumber(pnfl);
            if (emp == null) {
                throw new RestException("Employee with pinfl : " + pnfl + " not found", "Not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            Object business_trip_date = (typeId == 1 || typeId == 2 ? employee : object).get("business_trip_date");
            Object going_back_date = (typeId == 1 || typeId == 2 ? employee : object).get("going_back_date");
            customFieldRequests.add(new CustomFieldRequest("Сотрудник", emp.getObjectID()));
            customFieldRequests.add(new CustomFieldRequest("Планируемая дата и время выезда в командировку", business_trip_date));
            customFieldRequests.add(new CustomFieldRequest("Планируемая дата и время выезда обратно", going_back_date));
            customFieldRequests.add(new CustomFieldRequest("Маршрут", (typeId == 1 || typeId == 2 ? employee : object).get("route")));
            customFieldRequests.add(new CustomFieldRequest("Транспорт", (typeId == 1 || typeId == 2 ? employee : object).get("transportTextRu")));
            customFieldRequests.add(new CustomFieldRequest("Проживание", (typeId == 1 || typeId == 2 ? employee : object).get("residenceTextRu")));
            customFieldRequests.add(new CustomFieldRequest("Имя держателя карты", (typeId == 1 || typeId == 2 ? employee : object).get("corporate_card_number")));
            customFieldRequests.add(new CustomFieldRequest("Сумма пополнения", (typeId == 1 || typeId == 2 ? employee : object).get("corporate_card_sum")));
            customFieldRequests.add(new CustomFieldRequest("Совмещение", (typeId == 1 || typeId == 2 ? employee : object).get("combining_with_a_personal_trip")));
            customTableRpc.setItemCustomFields(CustomFieldsUtils.convertCustomFields(customFieldRequests, itemTableCfs, null));
            participants.add(customTableRpc);
            relations.add(new RelationItem(null, emp.getObjectID(), RelationItem.TYPE_EMPLOYEE, emp.getFullName(), null, null, null));

            DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            Date startDate = null;
            Date endDate = null;
            try {
                startDate = formatter.parse((String) business_trip_date);
                endDate = formatter.parse((String) going_back_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<EdsSickRequest> leaves = sickRequestManager.findApprovedLeaveRequestsByUserId(emp.getObjectID(), startDate, endDate);

            if (leaves != null && leaves.size() != 0 && leaves.get(0).getLeaveReason() != null) {
                for (EdsSickRequest r : leaves) {
                    if (r.getLeaveReason() != null && !r.getLeaveReason().getMarkAsDraft()) {
                        throw new RestException(GENERAL_ERROR_MESSAGE, emp.getName() + " already applied for " + r.getLeaveReason().getName() + " on " + ServerUtils.shortDateFormat(r.getStartDate(), emp), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }
        }
        map.put("ITEM_TABLE_eajC8xXCGt", participants);
        item.setTableItems(map);
        return relations;
    }

    @Operation(summary = "Get Komandirovka Prikaz")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Komandirovka Prikaz"))
    @RequestMapping(path = "/custom_form", method = RequestMethod.GET)
    public List<DynamicDto> getKomandirovkaPrikaz() {
        EdsUser user = userManager.getUser();
        ListingFilterParameter fp = new ListingFilterParameter();
        ListPanelToolRpc panelTools = commonService.getUserListPanelSettings(ListPanelType.CustomFormItemsPanel, "KOMANDIROVKA_FORM", 87, null);
        fp.setListPanelTool(panelTools);
        fp.setParentID(87);
        fp.setForm("KOMANDIROVKA_FORM");
        ArrayList<ListingFilterDTO> params = new ArrayList<>();
        params.add(new ListingFilterDTO("status", Collections.singletonList(new IdCode(referenceManager.getByCode(CUSTOM_FORM_ITEM_STATUS_APPROVED).getObjectID(), null))));
        FacetFilterRpc facetFilter = FacetFilterHelperV3.fillFacetFilter(FacetFilterHelperV3.createFacetFilter(ListPanelType.CustomFormItemsPanel), ListPanelType.CustomFormItemsPanel, params);
        facetFilter.setEndDate(new Date());
        ZoneId defaultZoneId = ZoneId.systemDefault();
        facetFilter.setStartDate(Date.from(LocalDate.now().minusMonths(3).atStartOfDay(defaultZoneId).toInstant()));
        facetFilter.setSelectedDateSolrCodeName("createdDate");
        fp.setFacetFilter(facetFilter);
        ListResult<FormItems> approvedItems = commonService.getCustomFormItems(fp);
        ArrayList<DynamicDto> result = new ArrayList<>();
        if (approvedItems != null && approvedItems.getTotal() > 0) {
            for (FormItems item : approvedItems.getList()) {
                DynamicDto dto = new DynamicDto();
                dto.addProperty("id", item.getObjectID());
                dto.addProperty("createdDate", ServerUtils.shortDateFormat(item.getCreatedDate(), user));
                dto.addProperty("subject", item.getCustomFieldsValue("string_value18"));
                dto.addProperty("number", item.getCustomFieldsValue("string_value17"));
                dto.addProperty("orderNumber", item.getCustomFieldsValue("string_value13"));
                result.add(dto);
            }
        }
        return result;
    }

    private String getTranslates(String fieldName, DynamicDto requestDto, boolean isArray) {
        StringBuilder result = new StringBuilder();
        if (isArray) {
            if (!((ArrayList) requestDto.getProperties().get(fieldName)).isEmpty()) {
                List<LinkedHashMap<String, Object>> list = (ArrayList) requestDto.getProperties().get(fieldName);
                boolean isFirst = true;
                for (LinkedHashMap<String, Object> subject : list) {
                    if (subject != null && !subject.isEmpty()) {
                        StringBuilder field = new StringBuilder();
                        if (!isFirst) {
                            result.append("-:-");
                        }
                        List<LinkedHashMap> translates = (ArrayList) subject.get("translates");
                        Map<Integer, String> map = new TreeMap<>();
                        for (LinkedHashMap t : translates) {
                            map.put((Integer) t.get("languageId"), (String) t.get("name"));
                        }
                        for (int i = 1; i < 5; i++) {
                            if (field.toString().equals("")) {
                                field = new StringBuilder(map.get(i) != null ? map.get(i) : " ");
                            } else {
                                field.append(";").append(map.get(i) != null ? map.get(i) : " ");
                            }
                        }
                        result.append(field);
                        isFirst = false;
                    }
                }
            }
        } else if (requestDto.getProperties().get(fieldName) != null) {
            LinkedHashMap<String, Object> subject = (LinkedHashMap) requestDto.getProperties().get(fieldName);
            if (subject != null && !subject.isEmpty()) {
                List<LinkedHashMap> translates = (ArrayList) subject.get("translates");
                Map<Integer, String> map = new TreeMap<>();
                for (LinkedHashMap t : translates) {
                    if (StringUtils.isNotBlank((String) t.get("name"))) {
                        map.put((Integer) t.get("languageId"), (String) t.get("name"));
                    } else if (StringUtils.isNotBlank((String) t.get("translateText"))) {
                        map.put((Integer) t.get("languageId"), (String) t.get("translateText"));
                    }
                }
                for (int i = 1; i < 5; i++) {
                    if (result.toString().equals("")) {
                        result = new StringBuilder(map.get(i) != null ? map.get(i) : " ");
                    } else {
                        result.append(";").append(map.get(i) != null ? map.get(i) : " ");
                    }
                }
            }
        }
        return result.toString();
    }

    @Operation(summary = "Create Uvolnenie Form")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Uvolnenie"))
    @RequestMapping(path = "/custom_form_uvolnenie", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<DynamicDto> createCustomForm2(@Validated @RequestBody GTLTripDemoDto dto) throws
            RestException {
        Integer id;
        if (dto.getKpiDocumentId() != null && dto.getKpiDocumentType().equals("CUSTOM_FORM_UVOLNENIYA_SOZDAT_PRIKAZ")) {
            FormItems item = commonService.getCustomFormItem(Integer.valueOf(dto.getKpiDocumentId()), 101, "UVOLJNENIE2_FORM", false, null, null, null, null);
            item.setStatusCode(CUSTOM_FORM_ITEM_STATUS_APPROVED);
            commonService.saveCustomFormItem(item);
            id = item.getObjectID();
        } else {
            TripDemoRequestDto requestDto = dto.getJson();
            FormItems item = new FormItems();
            item.setFormID("UVOLJNENIE2_FORM");
            item.setStatusCode(Constants.CUSTOM_FORM_ITEM_STATUS_DRAFT);

            EdsEmployee employee = null;
            if (requestDto.getProperties().get("sys_initiator") != null) {
                String employeePnfl = (String) ((Map) requestDto.getProperties().get("sys_initiator")).get("pinfl");
                employee = employeeManager.getEmployeeByNumber(employeePnfl);
                if (employee == null) {
                    throw new RestException("Employee with pinfl : " + employeePnfl + " not found", "Not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
            }

            ArrayList<CompanyCustomFieldItem> cfs = commonService.getCompanyCategoryCustomFields(101);
            List<CustomFieldRequest> customFields = new ArrayList<>();

            customFields.add(new CustomFieldRequest("resigned_emp", employee != null ? employee.getObjectID() : null));
            customFields.add(new CustomFieldRequest("Текущий Баланс (Отпуск)", "0"));
            customFields.add(new CustomFieldRequest("email_resigned", employee != null ? employee.getEmail() : ""));
            customFields.add(new CustomFieldRequest("Статья", "99- по собственному желанию"));
            customFields.add(new CustomFieldRequest("resign_date", requestDto.getProperties().get("date")));
            customFields.add(new CustomFieldRequest("qr_url", requestDto.getProperties().get("verifyUrl")));

            EdsEmployee approver = employeeManager.getEmployeeByNumber(((String) (requestDto.getSys_approval_1().getProperties()).get("pinfl")));
            customFields.add(new CustomFieldRequest("backup", approver != null ? approver.getObjectID() : null));

            Map<String, Object> ageedMap = null;
            String sysAgreedStr = "sys_agreed_";
            for (int i = 1; i <= 10; i++) {
                if (requestDto.getProperties().get(sysAgreedStr + i) != null) {
                    ageedMap = (Map) requestDto.getProperties().get(sysAgreedStr + i);
                    break;
                }
            }

            EdsEmployee agreed = employeeManager.getEmployeeByNumber((String) (ageedMap).get("pinfl"));
            customFields.add(new CustomFieldRequest("Согласовано", agreed != null ? agreed.getObjectID() : null));

            CompanyCustomFieldItem numberCf = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "ord_number2");
            String number = commonService.getMaxValueOfAutoNumbering(numberCf);
            customFields.add(new CustomFieldRequest("ord_number2", number));

            item.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(customFields, cfs, null));
            id = commonService.saveCustomFormItem(item);
            ArrayList<RelationItem> relations = new ArrayList<>();
            relations.add(new RelationItem(null, employee.getObjectID(), RelationItem.TYPE_EMPLOYEE, employee.getFullName(), null, null, null));
            allInOneServiceLocal.saveRelations(item.getFormID(), id, item.getAutoNumber() != null ? item.getAutoNumber() : item.getFormName() + ":" + id, relations);
        }
        DynamicDto result = new DynamicDto();
        result.addProperty("id", id);
        result.addProperty("type", "CUSTOAM_FORM_UVOLNENIYA");
        return ResultTO.success(result);
    }

    @Operation(summary = "Create Zaym Form")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Zaym"))
    @RequestMapping(path = "/cash_advance_form", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<DynamicDto> createZaym(@Validated @RequestBody DynamicDto dto) throws RestException, ParseException {
        Integer id;
        if (dto.getProperties().get("kpiDocumentType") != null && dto.getProperties().get("kpiDocumentType").equals("ZAYM_SOZDAT_PRIKAZ")) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setObjectId(Integer.valueOf(dto.getProperties().get("kpiDocumentId").toString()));
            CashAdvanceItem cashAdvancedItem = payrollService.getCashAdvancedItem(filterParameter);
            cashAdvancedItem.getStatus().setCode("SUBMITTED");
            for (CompanyCustomFieldItem item : cashAdvancedItem.getCustomFieldItems()) {
                if ("qr code link".equals(item.getAliasName())) {
                    if (ServerUtils.isNullOrEmpty(item.getFieldStringValue())) {
                        item.setFieldStringValue((String) dto.getProperties().get("verifyUrl"));
                    }
                }
            }
            payrollService.saveCashAdvance(cashAdvancedItem);
            id = cashAdvancedItem.getObjectID();
        } else {
            CashAdvanceItem item = new CashAdvanceItem();
            Map<String, Object> requestDto = (HashMap) dto.getProperties().get("json");
            if (requestDto.get("sys_initiator") != null) {
                String employeePnfl = (String) ((Map) requestDto.get("sys_initiator")).get("pinfl");
                EdsEmployee requester = employeeManager.getEmployeeByNumber(employeePnfl);
                if (requester == null) {
                    throw new RestException("Employee with pinfl : " + employeePnfl + " not found", "Not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
                item.setEmployee(new SelectItem(requester.getObjectID()));
            }
            if (requestDto.get("sys_initiator_date") != null) {
                String date = (String) requestDto.get("sys_initiator_date");
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                item.setDate(new DateNonConvertable(format.parse(date)));
            }
            String monthString = "";
            LinkedHashMap<String, String> approvedMonth = (LinkedHashMap) requestDto.get("approvedMonth");
            if (requestDto.get("amount") != null) {
                String requestedAmountString = requestDto.get("amount").toString();
                item.setTotalAmount(new BigDecimal(requestedAmountString.trim()));
                if (approvedMonth == null) {
                    monthString = requestDto.get("month").toString().substring(0, requestDto.get("month").toString().indexOf("_"));
                    Double paymentAmount = Double.parseDouble(requestedAmountString.trim()) / Double.parseDouble(monthString.replace("_month", ""));
                    item.setPaymentAmount(new BigDecimal(String.valueOf(paymentAmount)));
                } else if (approvedMonth.get("code") != null) {
                    monthString = approvedMonth.get("code");
                    Double paymentAmount = Double.parseDouble(requestedAmountString.trim()) / Double.parseDouble(monthString.replace("_month", ""));
                    item.setPaymentAmount(new BigDecimal(String.valueOf(paymentAmount)));
                }
            }
            item.setCategoryItem(new PaymentDeductionSelectItem(67, "Cash Advance", "CASH_ADVANCE", "Deduction"));


            BankTransferNumberData newNumberData = payrollServiceLocal.generateCashAdvanceNumberFormat();

            item.setNumber(newNumberData.getTransferNumber());
            item.setIntNumber(Integer.parseInt(newNumberData.getFourDigitNumber()));
            DynamicDto dynamicDto = new DynamicDto();
            dynamicDto.getProperties().putAll(requestDto);
            item.setReference(getTranslates("selectedEdu", dynamicDto, false));


            ArrayList<CompanyCustomFieldItem> cfs = commonService.getCompanyCustomFields(ViewName.CashAdvanceList);
            List<CustomFieldRequest> customFields = new ArrayList<>();
            LinkedHashMap<String, String> benefit = (LinkedHashMap) requestDto.get("benefit");
            if (benefit != null) {
                customFields.add(new CustomFieldRequest("Benefit", benefit.get("code")));
            }
            customFields.add(new CustomFieldRequest("Relationship", getTranslates("selectedRelationshipValue", dynamicDto, false)));
            customFields.add(new CustomFieldRequest("Relative's Name", requestDto.get("relationshipFIO")));
            customFields.add(new CustomFieldRequest("Birthday of relative", requestDto.get("relationshipYear")));
            customFields.add(new CustomFieldRequest("Month", monthString.replace("_month", "")));
            customFields.add(new CustomFieldRequest("Doc Number", dto.getProperties().get("docNumber")));
            customFields.add(new CustomFieldRequest("qr code link", dto.getProperties().get("verifyUrl")));
            customFields.add(new CustomFieldRequest("Loan Type", requestDto.get("loanType")));
            ArrayList<LinkedHashMap> attachments = (ArrayList) requestDto.get("attachments");
            if (attachments != null && !attachments.isEmpty()) {
                int i = 1;
                for (LinkedHashMap attachment : attachments) {
                    customFields.add(new CustomFieldRequest("Attachment" + i, attachment.get("url")));
                    i++;
                }
            }

            if (dto.getProperties().get("signedUser") != null) {
                EdsEmployee signedUser = employeeManager.getEmployeeByNumber((String) ((LinkedHashMap) dto.getProperties().get("signedUser")).get("pinfl"));
                customFields.add(new CustomFieldRequest("Signed User", signedUser.getObjectID().toString()));
            }

            if (dto.getProperties().get("agreedUser") != null) {
                EdsEmployee agreedUser = employeeManager.getEmployeeByNumber((String) ((LinkedHashMap) dto.getProperties().get("agreedUser")).get("pinfl"));
                customFields.add(new CustomFieldRequest("Agreed User", agreedUser.getObjectID().toString()));
            }

            if (dto.getProperties().get("approvedUser") != null) {
                EdsEmployee approvedUser = employeeManager.getEmployeeByNumber((String) ((LinkedHashMap) dto.getProperties().get("approvedUser")).get("pinfl"));
                customFields.add(new CustomFieldRequest("Approved User", approvedUser.getObjectID().toString()));
            }

            Date dueDate = DateUtil.addMonths(new Date(), Integer.parseInt(monthString.replace("_month", "")) - 1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
            customFields.add(new CustomFieldRequest("срок", simpleDateFormat.format(ServerUtils.getMonthEndDate(dueDate))));
            item.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(customFields, cfs, null));

            ReferenceItem overallStatus = new ReferenceItem();
            overallStatus.setCode(DRAFT);

            item.setOverallStatus(overallStatus);
            item.setStatus(new SelectItem(DRAFT));
            TestRPC testRPC = payrollService.saveCashAdvance(item);
            id = testRPC.getId();
        }
        DynamicDto result = new DynamicDto();
        result.addProperty("id", id);
        result.addProperty("type", "ZAYM");
        return ResultTO.success(result);
    }

    @Operation(summary = "Create Bulk Leave Requests")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Leave Requests"))
    @RequestMapping(path = "/bulk", method = RequestMethod.POST)
    public ResultTO<DynamicDto> createBulkLeaveRequests(@RequestParam(name = "id") Integer
                                                                entityId, @RequestParam(name = "approver pinfl") String approverPinfl) throws RestException {
        EdsCustomFormItems customFormItems = customFormItemManager.get(entityId);
        if (customFormItems == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Custom Form Item with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        GTLTripDemoDto request = new GTLTripDemoDto();
        TripDemoRequestDto requestDto = new TripDemoRequestDto();
        EdsCustomFormCustomFields cfs = customFormItems.getFormCustomFields();

        Gson gson = new Gson();
        Map<String, String> jsonEntities = gson.fromJson(cfs.getJsonEntities(), Map.class);
        if (jsonEntities.get("string_value7") != null) {
            EdsEmployee approver = employeeManager.get(Integer.parseInt(jsonEntities.get("string_value7")));
            if (approver != null) {
                DynamicDto sys_approver = new DynamicDto();
                sys_approver.addProperty("pinfl", approver.getProfile().getEmployeeCode());
                requestDto.setSys_approval_1(sys_approver);
            }
        } else if (StringUtils.isNotBlank(approverPinfl)) {
            DynamicDto sys_approver = new DynamicDto();
            sys_approver.addProperty("pinfl", approverPinfl);
            requestDto.setSys_approval_1(sys_approver);
        }
        requestDto.addProperty("date", cfs.getDateValue1());
        requestDto.addProperty("place", cfs.getStringValue3());
        requestDto.addProperty("host_organization", cfs.getStringValue4());
        requestDto.addProperty("term_of_business_trip", cfs.getStringValue6());

        if (jsonEntities.get("string_value1") != null) {
            EdsEmployee creator = employeeManager.get(Integer.parseInt(jsonEntities.get("string_value1")));
            if (creator != null) {
                Map<String, String> initiator = new LinkedHashMap<>();
                initiator.put("pinfl", creator.getProfile().getEmployeeCode());
                requestDto.addProperty("sys_initiator", initiator);
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        DynamicDto trip_participants = new DynamicDto();
        List<EdsCustomItemTable> participants = customFormItems.getItemTables().stream().filter(item -> item.getUuid().equals("ITEM_TABLE_eajC8xXCGt")).toList();
        int i = 1;
        for (EdsCustomItemTable item : participants) {
            DynamicDto participant = new DynamicDto();
            EdsCustomItemTableCF cf = item.getCustomFields();
            Map<String, String> empJsonEntities = gson.fromJson(cf.getJsonEntities(), Map.class);
            EdsEmployee emp = employeeManager.get(Integer.parseInt(cf.getStringValue5()));
            Map<String, Object> user = new LinkedHashMap<>();
            user.put("pinfl", emp.getProfile().getEmployeeCode());
            participant.addProperty("user", user);

            participant.addProperty("business_trip_date", dateFormat.format(cf.getDateValue1()));
            participant.addProperty("going_back_date", dateFormat.format(cf.getDateValue2()));
            participant.addProperty("route", cf.getStringValue2());

            List<Map<String, String>> transport = new ArrayList<>();
            Map<String, String> map = new LinkedHashMap<>();
            map.put("text", cf.getStringValue3());
            transport.add(map);
            participant.addProperty("transport", transport);

            List<Map<String, String>> residence = new ArrayList<>();
            Map<String, String> mapResidence = new LinkedHashMap<>();
            mapResidence.put("text", cf.getStringValue4());
            residence.add(mapResidence);
            participant.addProperty("residence", residence);
            trip_participants.addProperty("sys_for_information_" + i, participant);
            i++;
        }
        requestDto.setSys_table_for_information(trip_participants);

        if (jsonEntities.get("string_value9") != null) {
            requestDto.addProperty("sys_agreed_1", employeeManager.get(Integer.parseInt(jsonEntities.get("string_value9"))));
        }
        if (jsonEntities.get("string_value10") != null) {
            requestDto.addProperty("sys_agreed_2", employeeManager.get(Integer.parseInt(jsonEntities.get("string_value10"))));
        }
        if (jsonEntities.get("string_value11") != null) {
            requestDto.addProperty("sys_agreed_3", employeeManager.get(Integer.parseInt(jsonEntities.get("string_value11"))));
        }
        request.setJson(requestDto);
        return ResultTO.success(createLR(requestDto, customFormItems));
    }

    @Operation(summary = "14-salary")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "14-salary updated"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(path = "/14-salary", method = RequestMethod.POST)
    public ResultTO<DynamicDto> updateFouteensSalry(@RequestBody DynamicDto dto) throws RestException, ParseException {
        String idString = (String) dto.getProperties().get("id");
        FormItems item = commonService.getCustomFormItem(Integer.parseInt(idString), 112, "_ZP_FORM", false, null, null, null, null);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse((String) dto.getProperties().get("date"), formatter);
        ArrayList<EdsEmployee> employees = (ArrayList<EdsEmployee>) employeeManager.getEmployeesForForteensSalary(date);

        List<RelationItem> relations = new ArrayList<>();
        HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();
        ArrayList<CustomTableRpc> participants = (ArrayList<CustomTableRpc>) employees.stream()
                .map(employee -> {
                    CustomTableRpc customTableRpc = new CustomTableRpc();
                    customTableRpc.setUuid("ITEM_TABLE_SLSn7qk1AX");
                    ArrayList<CompanyCustomFieldItem> itemTableCfs = commonServiceLocal.getCompanyCustomFieldsByCategoryForListView(ViewName.CustomFormItemTable, "ITEM_TABLE_xodzNLTOJh");

                    LocalDate hireDate = employee.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    Period period = Period.between(hireDate, date);
                    int experience = period.getYears();
                    double koef = 0;
                    if (experience >= 1 && experience <= 3) {
                        koef = 1;
                    } else if (experience == 4 || experience == 5) {
                        koef = 1.5;
                    } else {
                        koef = 2;
                    }

                    List<CustomFieldRequest> customFieldRequests = new ArrayList<>();
                    customFieldRequests.add(new CustomFieldRequest("Сотрудник1", employee.getObjectID()));
                    customFieldRequests.add(new CustomFieldRequest("Hire date", hireDate.format(formatter)));
                    customFieldRequests.add(new CustomFieldRequest("Ish Staji", experience));
                    customFieldRequests.add(new CustomFieldRequest("Koeffisent", koef));
                    customTableRpc.setItemCustomFields(CustomFieldsUtils.convertCustomFields(customFieldRequests, itemTableCfs, null));
                    return customTableRpc;
                }).collect(Collectors.toList());

        map.put("ITEM_TABLE_xodzNLTOJh", participants);
        item.setTableItems(map);

        commonService.saveCustomFormItem(item);

        return ResultTO.success(new DynamicDto());
    }


    @Transactional
    @Operation(summary = "Add QR Code Link")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "QR Code"))
    @RequestMapping(path = "/qr_code", method = RequestMethod.POST)
    public ResultTO<DynamicDto> postFormQrCode(@RequestBody DynamicDto dto) throws RestException, ParseException {
        Gson gson = new Gson();
        log.info("---------------qr_code API--------date: " + new Date() + " ----------- body: " + gson.toJson(dto));
        String type = (String) dto.getProperties().get("kpiDocumentType");
        Integer id = dto.getProperties().get("kpiDocumentId") != null ? Integer.parseInt((String) dto.getProperties().get("kpiDocumentId")) : null;
        String summary = (String) dto.getProperties().get("summary");
        Integer placementId = null;
        if ("MATPOMOSH_APROVE".equals(type)) {
            FormItems item = commonService.getCustomFormItem(id, 115, "MATPOMOSCH_FORM", false, null, null, null, null);
            item.setStatusCode(CUSTOM_FORM_ITEM_STATUS_APPROVED);

            CompanyCustomFieldItem approver = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "Approver of Order");
            String approverPinfl = (String) ((LinkedHashMap) dto.getProperties().get("approvedUser")).get("pinfl");
            EdsEmployee employee = employeeManager.getEmployeeByNumber(approverPinfl);
            approver.setFieldStringValue(employee.getObjectID().toString());
            item.getCustomFieldItems().add(approver);

            commonService.saveCustomFormItem(item);

        } else if ("CUSTOM_FORM_KOMANDIROVKA".equals(type)) {
            FormItems item = commonService.getCustomFormItem(id, 87, "KOMANDIROVKA_FORM", false, null, null, null, null);
            HashSet<Integer> lrIds = new HashSet<>();
            item.getRelations().stream().filter(r -> r.getToType().equals(TYPE_LEAVE_REQUEST)).forEach(r -> lrIds.add(r.getToID()));
            for (Integer lrId : lrIds) {
                availabilityService.deleteRequest(lrId);
            }
            item.setStatusCode(CUSTOM_FORM_ITEM_STATUS_APPROVED);

            CompanyCustomFieldItem cf = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "qr code link");
            cf.setFieldStringValue((String) dto.getProperties().get("verifyUrl"));
            item.getCustomFieldItems().add(cf);

            CompanyCustomFieldItem approver = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "approver name");
            String approverPinfl = (String) ((LinkedHashMap) dto.getProperties().get("signedUser")).get("pinfl");
            EdsEmployee employee = employeeManager.getEmployeeByNumber(approverPinfl);
            approver.setFieldStringValue(employee.getObjectID().toString());
            item.getCustomFieldItems().add(approver);

            CompanyCustomFieldItem summaryCf = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "Краткое содержание");
            summaryCf.setFieldStringValue((String) dto.getProperties().get("summary"));
            item.getCustomFieldItems().add(summaryCf);
            commonService.saveCustomFormItem(item);
            userManager.flushAndClear();

            createBulkLeaveRequests(id, approverPinfl);
        } else if (type.equals("CUSTOM_FORM_UVOLNENIYA_SOZDAT_PRIKAZ") || type.equals("CUSTOM_FORM_UVOLNENIYA")) {
            FormItems item = commonService.getCustomFormItem(id, 101, "UVOLJNENIE2_FORM", false, null, null, null, null);
            if (type.equals("CUSTOM_FORM_UVOLNENIYA_SOZDAT_PRIKAZ")) {
                item.setStatusCode(CUSTOM_FORM_ITEM_STATUS_APPROVED);
            } else {
                CompanyCustomFieldItem cf = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "qr_url");
                cf.setFieldStringValue((String) dto.getProperties().get("verifyUrl"));
                item.getCustomFieldItems().add(cf);

                CompanyCustomFieldItem approver = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "Утверждающее лицо(приказ)");
                String approverPinfl = (String) ((LinkedHashMap) dto.getProperties().get("signedUser")).get("pinfl");
                EdsEmployee employee = employeeManager.getEmployeeByNumber(approverPinfl);
                approver.setFieldStringValue(employee.getObjectID().toString());
                item.getCustomFieldItems().add(approver);
            }
            CompanyCustomFieldItem cf = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "Краткое содержание УВ");
            cf.setFieldStringValue(summary);
            item.getCustomFieldItems().add(cf);
            commonService.saveCustomFormItem(item);
        } else if (type.startsWith("LEAVE_REQUEST")) {
            if (type.equals("LEAVE_REQUEST_REJECT")) {
                availabilityService.updateApprove(LR_STATUS_SS_DENIED, id, true, null, true);
            } else {
                StatisticsLeaveRequest lr = availabilityService.getLeaveRequest(id);
                ArrayList<CompanyCustomFieldItem> cfs = new ArrayList<>();
                CompanyCustomFieldItem url = commonService.getCustomFieldByAlias(ViewName.LeaveRequest.name(), "URL_ORDER_APPROVED");
                url.setFieldStringValue((String) dto.getProperties().get("verifyUrl"));
                boolean isSozdatPrikaz = false;
                boolean isPrikaz = false;
                cfs.add(url);

                CompanyCustomFieldItem summaryCf = commonService.getCustomFieldByAlias(ViewName.LeaveRequest.name(), "Description Edoc");
                summaryCf.setFieldStringValue(summary);
                cfs.add(summaryCf);
                if (type.equals("LEAVE_REQUEST_PRIKAZ")) {
                    LinkedHashMap<String, Object> signedUser = (LinkedHashMap) dto.getProperties().get("signedUser");
                    CompanyCustomFieldItem date = commonService.getCustomFieldByAlias(ViewName.LeaveRequest.name(), "URL_APPROVED_DATE_TIME");
                    date.setFieldStringValue((String) signedUser.get("signDate"));
                    cfs.add(date);

                    CompanyCustomFieldItem approver = commonService.getCustomFieldByAlias(ViewName.LeaveRequest.name(), "Approver Name");
                    String approverPinfl = (String) signedUser.get("pinfl");
                    EdsEmployee edsApprover = employeeManager.getEmployeeByNumber(approverPinfl);
                    approver.setFieldStringValue(edsApprover.getObjectID().toString());
                    cfs.add(approver);
                    lr.setCustomFields(cfs);
                    isPrikaz = true;
                } else if (type.equals("LEAVE_REQUEST_SOZDAT_PRIKAZ")) {
                    isSozdatPrikaz = true;
                } else {
                    //initiator
                    LinkedHashMap<String, Object> initiator = (LinkedHashMap) dto.getProperties().get("initiator");
                    CompanyCustomFieldItem initiateDate = commonService.getCustomFieldByAlias(ViewName.LeaveRequest.name(), "URL_Imzolandi_Date_Time");
                    initiateDate.setFieldStringValue((String) initiator.get("signDate"));
                    cfs.add(initiateDate);

                    //agreedUser
                    LinkedHashMap<String, Object> agreedUser = (LinkedHashMap) dto.getProperties().get("agreedUser");
                    CompanyCustomFieldItem agreedDate = commonService.getCustomFieldByAlias(ViewName.LeaveRequest.name(), "Kelishildi Date and Time");
                    agreedDate.setFieldStringValue((String) agreedUser.get("signDate"));
                    cfs.add(agreedDate);

                    CompanyCustomFieldItem agreedEmp = commonService.getCustomFieldByAlias(ViewName.LeaveRequest.name(), "KELISHILDI");
                    String approverPinfl = (String) agreedUser.get("pinfl");
                    EdsEmployee agreedApprover = employeeManager.getEmployeeByNumber(approverPinfl);
                    agreedEmp.setFieldStringValue(agreedApprover.getObjectID().toString());
                    cfs.add(agreedEmp);

                    //signedUser
                    LinkedHashMap<String, Object> signedUser = (LinkedHashMap) dto.getProperties().get("signedUser");
                    CompanyCustomFieldItem date = commonService.getCustomFieldByAlias(ViewName.LeaveRequest.name(), "Tasdiqlandi Date and Time");
                    date.setFieldStringValue((String) signedUser.get("signDate"));
                    cfs.add(date);

                    CompanyCustomFieldItem signedEmp = commonService.getCustomFieldByAlias(ViewName.LeaveRequest.name(), "TASDIQLANDI");
                    String signedPinfl = (String) signedUser.get("pinfl");
                    EdsEmployee edsApprover = employeeManager.getEmployeeByNumber(signedPinfl);
                    signedEmp.setFieldStringValue(edsApprover.getObjectID().toString());
                    cfs.add(signedEmp);
                    lr.setCustomFields(cfs);

                    CompanyCustomFieldItem edocId = commonService.getCustomFieldByAlias(ViewName.LeaveRequest.name(), "e-Doc ID");
                    edocId.setFieldStringValue((String) dto.getProperties().get("docNumber"));
                    cfs.add(edocId);
                }
                NewLeaveRequest item = new NewLeaveRequest();
                item.setObjectID(lr.getObjectID());
                if (isPrikaz || lr.getReasonCode().equals("ОТГУЛ")) {
                    item.setStatusCode(Constants.LR_STATUS_SS_APPROVED);
                } else if (isSozdatPrikaz) {
                    item.setStatusCode(Constants.LR_STATUS_NOT_DEFINED);
                } else {
                    item.setStatusCode(Constants.DRAFT);
                }
                item.setEmployee(lr.getEmployeeId());
                item.setApprovers(lr.getApprovers());
                if (lr.getNumberData() != null) {
                    item.setNumberData(lr.getNumberData());
                    item.setLeaveRequestCode(lr.getNumberData().getNumberString());
                }
                item.setStartNonConverable(lr.getStartDDate());
                item.setEndNonConverable(lr.getEndDDate());
                item.setFrom(LayoutRPC.LEAVE_REQUEST_FORM);
                item.setType(lr.getType());
                item.setTakeByMoney(lr.getTakeByMoney());
                item.setReasonId(lr.getReasonId());
                item.setDescription(lr.getDescription());
                item.setCustomFields(lr.getCustomFields());
                item.setApprovers(lr.getApprovers());
                item.setCurrentApprover(lr.getCurrentApprover());
                item.setFromApi(true);

                availabilityService.createLeaveRequest(item);
            }
        } else if (type.equals("PROTOCOL_HIRING_KPI")) {
            PlacementItem item = recruitmentService.getPlacementItem(id, null, null);

            EdsReference approvedStatus = referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_APPROVED);
            item.setApprovers(null);
            item.setStatusID(approvedStatus.getObjectID());
            item.setStatusCode(approvedStatus.getCode());

            CompanyCustomFieldItem url = commonService.getCustomFieldByAlias(ViewName.Placement.name(), "QR Link");
            url.setFieldStringValue((String) dto.getProperties().get("verifyUrl"));
            if (item.getCustomFieldItems() == null) {
                ArrayList<CompanyCustomFieldItem> cfs = new ArrayList<>();
                cfs.add(url);
                item.setCustomFieldItems(cfs);
            } else {
                item.getCustomFieldItems().add(url);
            }
            CompanyCustomFieldItem summaryCf = commonService.getCustomFieldByAlias(ViewName.Placement.name(), "Description Edoc");
            summaryCf.setFieldStringValue((String) dto.getProperties().get("summary"));
            item.getCustomFieldItems().add(summaryCf);
            recruitmentService.savePlacement(item, null);
        } else if (type.equals("EMPLOYEE_HIRING_KPI") || type.equals("NAYM_SOZDAT_PRIKAZ") || type.equals("NAYM_PRIKAZ_APPROVE")) {
            if (!type.equals("NAYM_PRIKAZ_APPROVE")) {
                placementId = (Integer) ((LinkedHashMap) dto.getProperties().get("json")).get("protocolId");
            }
            PlacementItem placement = recruitmentService.getPlacementItem(type.equals("NAYM_PRIKAZ_APPROVE") ? id : placementId, null, null);
            CompanyCustomFieldItem statementStatus = commonService.getCustomFieldByAlias(ViewName.Placement.name(), "Statement Status");
            statementStatus.setFieldStringValue(type.equals("EMPLOYEE_HIRING_KPI") ? "Approved" : type.equals("NAYM_SOZDAT_PRIKAZ") ? "Sozdat Prikaz" : "Prikaz Approved");
            if (placement.getCustomFieldItems() == null) {
                ArrayList<CompanyCustomFieldItem> cfs = new ArrayList<>();
                cfs.add(statementStatus);
                placement.setCustomFieldItems(cfs);
            } else {
                placement.getCustomFieldItems().add(statementStatus);
            }
            CompanyCustomFieldItem url = commonService.getCustomFieldByAlias(ViewName.Placement.name(), "QR Link");
            url.setFieldStringValue((String) dto.getProperties().get("verifyUrl"));
            placement.getCustomFieldItems().add(url);
            if (type.equals("NAYM_PRIKAZ_APPROVE")) {
                CompanyCustomFieldItem approver = commonService.getCustomFieldByAlias(ViewName.Placement.name(), "approver_name");
                String approverPinfl = (String) ((LinkedHashMap) dto.getProperties().get("signedUser")).get("pinfl");
                EdsEmployee employee = employeeManager.getEmployeeByNumber(approverPinfl);
                approver.setFieldStringValue(employee.getObjectID().toString());
                placement.getCustomFieldItems().add(approver);
            }
            CompanyCustomFieldItem summaryCf = commonService.getCustomFieldByAlias(ViewName.Placement.name(), "Description Edoc");
            summaryCf.setFieldStringValue((String) dto.getProperties().get("summary"));
            placement.getCustomFieldItems().add(summaryCf);
            recruitmentService.savePlacement(placement, null);

            if (type.equals("EMPLOYEE_HIRING_KPI")) {
                EdsEmployee employee = employeeManager.getEmployeeByPlacementIds(placementId);
                ProfileItem item = contactService.editProfile(employee.getObjectID());
                item.setHireDate(new DateNonConvertable(new SimpleDateFormat("dd.MM.yyyy").parse((String) ((LinkedHashMap) dto.getProperties().get("json")).get("date"))));
                contactService.updateProfile(item);
            }
        } else if (type.equals("SHIFT_APPROVE") || type.equals("OVERTIME_APPROVE")) {
            ShiftItem item = hrmsService.getShiftItem(id, false);
            item.setStatusCode(SHIFT_APPROVED);

            ArrayList<CompanyCustomFieldItem> cfs = new ArrayList<>();

            CompanyCustomFieldItem approver = commonService.getCustomFieldByAlias(ViewName.ShiftList.name(), "Approver Name");
            String approverPinfl = "";
            if (dto.getProperties().get("approvedUser") != null) {
                approverPinfl = (String) ((LinkedHashMap) dto.getProperties().get("approvedUser")).get("pinfl");
            } else {
                approverPinfl = (String) ((LinkedHashMap) dto.getProperties().get("agreedUser")).get("pinfl");
            }
            EdsEmployee agreedApprover = employeeManager.getEmployeeByNumber(approverPinfl);
            approver.setFieldStringValue(agreedApprover.getObjectID().toString());
            cfs.add(approver);

            CompanyCustomFieldItem qrLink = commonService.getCustomFieldByAlias(ViewName.ShiftList.name(), "Qr Link");
            qrLink.setFieldStringValue((String) dto.getProperties().get("verifyUrl"));
            cfs.add(qrLink);
            item.setApprovers(null);
            item.setCustomFieldItems(cfs);

            hrmsService.saveShiftItem(item);
        } else if (type.equals("ZAYM_APPROVED")) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setObjectId(id);
            CashAdvanceItem cashAdvancedItem = payrollService.getCashAdvancedItem(filterParameter);
            cashAdvancedItem.getStatus().setCode("APPROVED");
            for (CompanyCustomFieldItem item : cashAdvancedItem.getCustomFieldItems()) {
                if ("qr code link".equals(item.getAliasName())) {
                    item.setFieldStringValue((String) dto.getProperties().get("verifyUrl"));
                } else if ("approver name".equals(item.getAliasName())) {
                    EdsEmployee employeeByNumber = employeeManager.getEmployeeByNumber((String) ((LinkedHashMap) dto.getProperties().get("approvedUser")).get("pinfl"));
                    item.setFieldStringValue(employeeByNumber.getObjectID().toString());
                    cashAdvancedItem.setApprovedDate(new DateNonConvertable(new Date()));
                }
            }
            CompanyCustomFieldItem cf = commonService.getCustomFieldByAlias(ViewName.CashAdvanceList.name(), "Краткое содержание Займ");
            cf.setFieldStringValue(summary);
            if (cashAdvancedItem.getCustomFieldItems() == null) {
                cashAdvancedItem.setCustomFieldItems(new ArrayList<>());
            }
            cashAdvancedItem.getCustomFieldItems().add(cf);
            payrollService.saveCashAdvance(cashAdvancedItem);
        } else if ("ROTATION_APPROVED".equals(type) || "ROTATION_PRATAKOL_APPROVE".equals(type) || "ROTATION_SUBMITTED".equals(type)) {
            RotationItem rotationItem = hrmsService.getRotationItem(id, false);
            if ("ROTATION_PRATAKOL_APPROVE".equals(type)) {
                for (CompanyCustomFieldItem item : rotationItem.getCustomFieldItems()) {
                    if ("Qr Code Link".equals(item.getAliasName())) {
                        item.setFieldStringValue((String) dto.getProperties().get("verifyUrl"));
                    } else if ("Approver name".equals(item.getAliasName())) {
                        EdsEmployee employeeByNumber = employeeManager.getEmployeeByNumber((String) ((LinkedHashMap) dto.getProperties().get("approvedUser")).get("pinfl"));
                        item.setFieldStringValue(employeeByNumber.getObjectID().toString());
                        rotationItem.setApprovedDate(new DateNonConvertable(new Date()));
                    }
                }
                rotationItem.setStatusCode(ROTATION_DRAFT);
            }

            if ("ROTATION_SUBMITTED".equals(type)) {
                rotationItem.setStatusCode(ROTATION_SUBMITTED);
            }

            if ("ROTATION_APPROVED".equals(type)) {
                for (CompanyCustomFieldItem item : rotationItem.getCustomFieldItems()) {
                    if ("Qr Link Prikaz".equals(item.getAliasName())) {
                        item.setFieldStringValue((String) dto.getProperties().get("verifyUrl"));
                    } else if ("Prikaz Approver".equals(item.getAliasName())) {
                        EdsEmployee employeeByNumber = employeeManager.getEmployeeByNumber((String) ((LinkedHashMap) dto.getProperties().get("approvedUser")).get("pinfl"));
                        item.setFieldStringValue(employeeByNumber.getObjectID().toString());
                        rotationItem.setApprovedDate(new DateNonConvertable(new Date()));
                    }
                }
                rotationItem.setStatusCode(ROTATION_APPROVED);
                rotationItem.setApproverEmployee(employeeManager.getEmployeeByNumber((String) ((LinkedHashMap) dto.getProperties().get("approvedUser")).get("pinfl")).getAsSelectItem());
            }

            CompanyCustomFieldItem cf = commonService.getCustomFieldByAlias(ViewName.RotationList.name(), "Краткое содержание Ротация");
            cf.setFieldStringValue(summary);
            if (rotationItem.getCustomFieldItems() == null) {
                rotationItem.setCustomFieldItems(new ArrayList<>());
            }
            rotationItem.getCustomFieldItems().add(cf);
            hrmsService.createRotation(rotationItem);

        } else if ("SHIFT_REJECT".equals(type) || "OVERTIME_REJECT".equals(type) || "DUTY_REJECT".equals(type)) {
            ShiftItem item = hrmsService.getShiftItem(id, false);
            item.setStatusCode(SHIFT_REJECTED);
            item.setApprovers(null);

            hrmsService.saveShiftItem(item);
            hrmsServiceLocal.deleteTimeRecordsByShiftId(id);
        } else if ("CUSTOM_FORM_14_ZP_APPROVE".equals(type)) {
            FormItems item = commonService.getCustomFormItem(id, 112, "_ZP_FORM", false, null, null, null, null);

            item.setStatusCode(CUSTOM_FORM_ITEM_STATUS_APPROVED);

            CompanyCustomFieldItem cf = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "Qr Link Of Prikaz");
            cf.setFieldStringValue((String) dto.getProperties().get("verifyUrl"));
            item.getCustomFieldItems().add(cf);

            CompanyCustomFieldItem approver = commonService.getCustomFieldByAlias(ViewName.CustomFormItems.name(), "Approver of Prikaz");
            String approverPinfl = (String) ((LinkedHashMap) dto.getProperties().get("signedUser")).get("pinfl");
            EdsEmployee employee = employeeManager.getEmployeeByNumber(approverPinfl);
            approver.setFieldStringValue(employee.getObjectID().toString());
            item.getCustomFieldItems().add(approver);

            commonService.saveCustomFormItem(item);
        }
        DynamicDto result = new DynamicDto();
        result.addProperty("id", id != null ? id : placementId);
        result.addProperty("type", type);
        log.info("------------------------successfully done for /qr_code API--------------------------------");
        return ResultTO.success(result);
    }

    @Transactional
    public DynamicDto createLR(TripDemoRequestDto requestDto, EdsCustomFormItems edsCustomFormItems) throws
            RestException {
        EdsEmployee approver = null;
        if (requestDto.getSys_approval_1() != null) {
            String approverPnfl = (String) requestDto.getSys_approval_1().getProperties().get("pinfl");
            approver = employeeManager.getEmployeeByNumber(approverPnfl);
            if (approver == null) {
                throw new RestException("Employee with pinfl : " + approverPnfl + " not found", "Not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        }
        EdsLeaveReason reason = reasonManager.findByCode("КОМАНДИРОВКА");
        ArrayList<CompanyCustomFieldItem> cfs = commonService.getCompanyCustomFields(ViewName.LeaveRequest);
        List<CustomFieldRequest> customFields = new ArrayList<>();
        customFields.add(new CustomFieldRequest("Дата1", requestDto.getProperties().get("date")));
        customFields.add(new CustomFieldRequest("type", "Служебная записка на командирование сотрудника"));
        customFields.add(new CustomFieldRequest("3. Место командирования (страна, город)", requestDto.getProperties().get("place")));
        customFields.add(new CustomFieldRequest("4. Принимающая организация (наименование, адрес)", requestDto.getProperties().get("host_organization")));
        customFields.add(new CustomFieldRequest("6. Срок командирования (без учета нахождения в пут", requestDto.getProperties().get("term_of_business_trip")));
        EdsEmployee sys_agreed1 = (EdsEmployee) requestDto.getProperties().get("sys_agreed_1");
        EdsEmployee sys_agreed2 = (EdsEmployee) requestDto.getProperties().get("sys_agreed_2");
        EdsEmployee sys_agreed3 = (EdsEmployee) requestDto.getProperties().get("sys_agreed_3");
        customFields.add(new CustomFieldRequest("Административный отдел", sys_agreed1.getFullName()));
        customFields.add(new CustomFieldRequest("Отдел управления персоналом", sys_agreed2.getFullName()));
        customFields.add(new CustomFieldRequest("Отдел эксплуатации автотранспорта", sys_agreed3.getFullName()));
        Integer creatorId = null;
        if (requestDto.getProperties().get("sys_initiator") != null) {
            EdsEmployee creator = employeeManager.getEmployeeByNumber(((String) ((LinkedHashMap) requestDto.getProperties().get("sys_initiator")).get("pinfl")));
            if (creator != null) {
                creatorId = creator.getObjectID();
            }
        }
//        if (requestDto.getList_of_travelers() != null) {
//            StringBuilder travelers = null;
//            for (DynamicDto traveler : requestDto.getList_of_travelers()) {
//                if (travelers != null) {
//                    travelers.append(", ");
//                } else {
//                    travelers = new StringBuilder();
//                }
//                travelers.append(traveler.getProperties().get("text"));
//            }
//            customFields.add(new CustomFieldRequest("5. Список командируемых – ФИО, должность", travelers != null ? travelers.toString() : null));
//        }
        Integer lrId = null;
        ArrayList<RelationItem> relations = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            DynamicDto employee = (DynamicDto) requestDto.getSys_table_for_information().getProperties().get("sys_for_information_" + i);
            if (employee == null) {
                break;
            }
            NewLeaveRequest item = new NewLeaveRequest();
            String pnfl = ((String) ((LinkedHashMap<String, Object>) employee.getProperties().get("user")).get("pinfl"));
            EdsEmployee emp = employeeManager.getEmployeeByNumber(pnfl);
            if (emp == null) {
                throw new RestException("Employee with pinfl : " + pnfl + " not found", "Not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            item.setEmployee(emp.getObjectID());

            TimeSlot timeSlot = commonServiceLocal.getCurrentEmployeeTimeSlot();
            int startHour = timeSlot.getStartHour() != null ? Integer.parseInt(timeSlot.getStartHour()) : 9;
            int startMinute = timeSlot.getStartMin() != null ? Integer.parseInt(timeSlot.getStartMin()) : 30;
            //
            int endHour = timeSlot.getEndHour() != null ? Integer.parseInt(timeSlot.getEndHour()) : 18;
            int endMinute = timeSlot.getEndMin() != null ? Integer.parseInt(timeSlot.getEndMin()) : 0;
            Date startDate = ServerUtils.parseDate((String) employee.getProperties().get("business_trip_date"), "dd.MM.yyy");
            startDate.setHours(startHour);
            startDate.setMinutes(startMinute);
            Date endDate = ServerUtils.parseDate((String) employee.getProperties().get("going_back_date"), "dd.MM.yyy");
            endDate.setHours(endHour);
            endDate.setMinutes(endMinute);
            item.setStartNonConverable(new DateNonConvertable(startDate));
            item.setEndNonConverable(new DateNonConvertable(endDate));
            item.setStartHour(startHour);
            item.setStartMinut(startMinute);
            item.setEndHour(endHour);
            item.setEndMinut(endMinute);
            ApprovalListResult approvalListResult = allInOneServiceLocal.getApprovers(TYPE_LEAVE_REQUEST, null, true, emp.getObjectID(), false, true);
            ArrayList<ApproverItemMini> approvers = new ArrayList<>();
            if (approver != null) {
                ApproverItemMini approverItem = new ApproverItemMini();
                approverItem.setExactEmployee(approver.getAsSelectItem());
                approverItem.setClonedFrom(approvalListResult.getList().get(0).getObjectID());
                approvers.add(approverItem);
            }
            item.setApprovers(approvers);
            item.setReasonId(reason.getObjectID());
            item.setStatusCode(Constants.APPROVED);
            item.setAllDay(true);
            item.setType("ST_PAID");
            item.setTakeByMoney(true);
            item.setCreatorId(creatorId);
            item.setSelfApprover(true);

            customFields.add(new CustomFieldRequest("Маршрут", employee.getProperties().get("route")));
            customFields.add(new CustomFieldRequest("Транспорт", ((LinkedHashMap) ((ArrayList) employee.getProperties().get("transport")).get(0)).get("text")));
            customFields.add(new CustomFieldRequest("Проживание", ((LinkedHashMap) ((ArrayList) employee.getProperties().get("residence")).get(0)).get("text")));
            customFields.add(new CustomFieldRequest("Имя держателя карты", employee.getProperties().get("corporate_card_number")));
            customFields.add(new CustomFieldRequest("Сумма пополнения", employee.getProperties().get("corporate_card_sum")));
            if (approver != null) {
                customFields.add(new CustomFieldRequest("Кому", approver.getFullName()));
            }

            item.setCustomFields(CustomFieldsUtils.convertCustomFields(customFields, cfs, null));
            String hasAccess = availabilityService.hasAccessInsertRequest(item.getEmployee(), item, item.getStartNonConverable(), item.getEndNonConverable(), false);
            if (Constants.TRUE.equals(hasAccess)) {
                if ("LR_TYPE_UNAUTHORIZED_LEAVE".equals(reason.getDescription())) {
                    NewLeaveRequest validItem = availabilityService.validateAllowanceLimit(item);
                    if (!validItem.getValid()) {
                        throw new RestException(GENERAL_ERROR_MESSAGE, "Leave Request Limit Exceeded", SERVER_ERROR, HttpStatus.BAD_REQUEST);
                    }
                }
                Integer id = availabilityService.createLeaveRequest(item);
                if (id == VALIDATION) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Probation period not passed for this employee", SERVER_ERROR, HttpStatus.BAD_REQUEST);
                }
                if (lrId == null)
                    lrId = id;
                if (edsCustomFormItems != null) {
                    relations.add(new RelationItem(null, id, TYPE_LEAVE_REQUEST, item.getNumberData().getNumberString(), null, null, null));
                }
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, hasAccess, SERVER_ERROR, HttpStatus.BAD_REQUEST);
            }
        }
        if (!relations.isEmpty()) {
            allInOneServiceLocal.saveRelations(edsCustomFormItems.getCustomForm().getFormID(), edsCustomFormItems.getObjectID(), edsCustomFormItems.getName(), relations);
        }
        DynamicDto result = new DynamicDto();
        result.addProperty("id", lrId);
        result.addProperty("type", "LEAVE_REQUEST");
        return result;
    }

    @Operation(summary = "Send LR To E-doc")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "LR to E-doc"))
    @RequestMapping(path = "/lr_to_edoc", method = RequestMethod.POST)
    public String sendLRToEdoc(@RequestBody DynamicDto dto, @RequestParam("employeeId") Integer employeeId,
                               @RequestParam("isHrm") boolean isHrm) {
        Map<String, Object> map = dto.getProperties();
        String type = (String) map.get("documentTypeCode");
        EdsEmployee employee = employeeManager.get(employeeId);
        int razryad = Integer.parseInt(employee.getCustomFields().getStringValue20().replaceAll("[^0-9]", ""));
        if (type.equals("kpi-application-taking-time-off")) {
            if (razryad > 0 && razryad <= 7) {
                type = "kpi-taking-time-off-type1";
            } else if (razryad <= 11) {
                type = "kpi-taking-time-off-type2";
            } else {
                type = "kpi-taking-time-off-type3";
            }
        } else if (type.equals("kpi-collective-agreement-type")) {
            if (razryad > 0 && razryad <= 7) {
                type = "kpi-collective-agreement-type1";
            } else if (razryad <= 11) {
                type = "kpi-collective-agreement-type2";
            } else {
                type = "kpi-collective-agreement-type3";
            }
        } else {
            if (razryad > 0 && razryad <= 7) {
                type = "kpi-leave-type1";
            } else if (razryad <= 11) {
                type = "kpi-leave-type2";
            } else {
                type = "kpi-leave-type3";
            }
        }
        map.put("documentTypeCode", type);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic a3BpOi4+YzdUSGh2cSk5ekglPkxtflJjcC5TWWV4RnksVyZMKDYqMlRiQnNkUDJBJjRbKDhyblM2bU0=");
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> httpRequest = new HttpEntity<>(map, httpHeaders);
        String responseText;
        try {
            HttpEntity<?> response = restTemplate.exchange(isHrm ? "https://apiedoc.uzgtl.com/api/v1/integration/kpi/orddocument/CreatePdfLinkDocument" : "https://apitedoc2.uzgtl.com/api/v1/integration/kpi/orddocument/CreatePdfLinkDocument",
                    HttpMethod.POST, httpRequest, String.class);
            responseText = response.toString();
        } catch (Exception e) {
            responseText = e.getMessage();
            if (e instanceof HttpClientErrorException) {
                responseText = ((HttpClientErrorException) e).getResponseBodyAsString();
            } else if (e instanceof HttpServerErrorException) {
                responseText = ((HttpServerErrorException) e).getResponseBodyAsString();
            }
        }
        return responseText;
    }


    @Operation(summary = "Get employee details")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Get employee details"))
    @RequestMapping(path = "/employee-details/{protocolId}", method = RequestMethod.GET)
    public DynamicDto employeeDetails(@PathVariable("protocolId") Integer id) {
        EdsEmployee employee = employeeManager.getEmployeeByPlacementIds(id);
        DynamicDto result = new DynamicDto();
        result.addProperty("trialPeriod", employee.getProbationDays().intValue() / 30);
        EdsEmployeePayrollSettings salary = employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), SALARY);
        double salaryValue = 0d;
        if (salary != null) {
            salaryValue = Double.parseDouble(salary.getValue());
        }
        result.addProperty("basicSalary", salaryValue);
        ArrayList<DynamicDto> contractType = new ArrayList<>();
        String value = employee.getStringValue("string_value26");

        CompanyCustomFieldItem type = commonService.getCustomFieldByAlias(ViewName.Employee.name(), "Вид найма");
        for (CustomFormLocalization locale : type.getLocalization().getChildren()) {
            if (value.equals(locale.getDefaultName())) {
                DynamicDto ru = new DynamicDto();
                ru.addProperty("languageId", 1);
                ru.addProperty("name", locale.getRussianName());
                contractType.add(ru);

                DynamicDto crl = new DynamicDto();
                crl.addProperty("languageId", 2);
                crl.addProperty("name", locale.getArabicName());
                contractType.add(crl);

                DynamicDto uz = new DynamicDto();
                uz.addProperty("languageId", 3);
                uz.addProperty("name", locale.getUzbekName());
                contractType.add(uz);

                DynamicDto en = new DynamicDto();
                en.addProperty("languageId", 4);
                en.addProperty("name", locale.getEnglishName());
                contractType.add(en);
                break;
            }
        }
        result.addProperty("contractType", contractType);
        result.addProperty("razryad", employee.getStringValue("string_value20"));
        return result;
    }

    @Operation(summary = "Rotatsiya")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Rotatsiya"))
    @RequestMapping(path = "/rotatsiya", method = RequestMethod.POST)
    public void rotatsiya(@RequestParam("id") Integer id) {
        EdsCustomFormItems items = customFormItemManager.get(id);

        if (items != null) {
            final Gson gson = new Gson();
            final Type dataType = new TypeToken<HashMap<String, String>>() {
            }.getType();
            Set<EdsCustomItemTable> itemTables = items.getItemTables();
            for (EdsCustomItemTable itemTable : itemTables) {
                if (itemTable.getUuid().equals("ITEM_TABLE_HF0jVlWL5h")) {
                    EdsCustomItemTableCF cf = itemTable.getCustomFields();
                    Integer employeeId = cf.getStringValue1() != null ? Integer.parseInt(cf.getStringValue1()) : null;
                    if (employeeId != null) {
                        ProfileItem item = contactService.editProfile(employeeId);
                        final HashMap<String, String> map = gson.fromJson(cf.getJsonEntities(), dataType);
                        if (map.get("string_value2") != null) {
                            item.setPositionId(Integer.parseInt(map.get("string_value2")));
                        }
                        if (map.get("string_value4") != null) {
                            item.setPmDepartmentID(Integer.parseInt(map.get("string_value4")));
                        }
                        if (map.get("string_value5") != null) {
                            List<CustomFieldRequest> cfs = new ArrayList<>();
                            cfs.add(new CustomFieldRequest("Разряд2", new ItemDto(Integer.parseInt(map.get("string_value5")), cf.getStringValue5())));
                            EdsEmployee employee = employeeManager.get(employeeId);
                            ArrayList<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.convertCustomFields(cfs, commonServiceLocal.getCompanyCustomFields(ViewName.Employee), employee.getCustomFields());
                            if (customFieldItems != null) {
                                item.setCustomFields(customFieldItems);
                            }
                        }
                        contactService.updateProfile(item);
                    }
                }
            }
        }
    }

    @Operation(summary = "Insert shift into attendance report")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Shift to AR"))
    @RequestMapping(path = "/shift/{id}", method = RequestMethod.POST)
    public void createRecordsInAR(@PathVariable("id") Integer id) {
        hrmsServiceLocal.insertEmployeePresentTime(shiftManager.get(id));
    }

    @Operation(summary = "Check whether employee has duty or not")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Checked duty"))
    @RequestMapping(path = "/duty/check", method = RequestMethod.POST)
    public ResultTO<Boolean> checkEmployeeDuty(@RequestBody DynamicDto dto) {
        List<LinkedHashMap<String, String>> dates = (List<LinkedHashMap<String, String>>) dto.getProperties().get("dutyDates");
        boolean result = true;
        if (dates != null) {
            EdsEmployee employee = employeeManager.getEmployeeByNumber((String) ((LinkedHashMap<?, ?>) dto.getProperties().get("sys_initiator")).get("pinfl"));
            for (LinkedHashMap<String, String> date : dates) {
                Date period = ServerUtils.parseDate(date.get("date") != null ? date.get("date") : date.get("fromDate"), "dd.MM.yyyy");
                EdsShiftItem duty = shiftManager.getEmployeeDuty(period, employee.getObjectID(), true);
                if (duty == null) {
                    result = false;
                    break;
                }
            }
        }
        return ResultTO.success(result);
    }

    @Operation(summary = "Change employee's duty date")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Changed duty date"))
    @RequestMapping(path = "/duty/change", method = RequestMethod.POST)
    @Transactional
    public ResultTO<Boolean> changeEmployeeDuty(@RequestBody GTLTripDemoDto request) throws RestException {
        DynamicDto dto = request.getJson();
        EdsEmployee employee = employeeManager.getEmployeeByNumber((String) ((LinkedHashMap<?, ?>) dto.getProperties().get("sys_initiator")).get("pinfl"));

        List<LinkedHashMap<String, String>> dates = (List<LinkedHashMap<String, String>>) dto.getProperties().get("dutyDates");
        for (LinkedHashMap<String, String> date : dates) {
            if (date.get("date") != null) {
                Date dutyDate = ServerUtils.parseDate(date.get("date"), "dd.MM.yyyy");
                EdsShiftItem duty = shiftManager.getEmployeeDuty(dutyDate, employee.getObjectID(), true);
                shiftManager.updateEmployeeDuty(duty, null);
                if (duty.getShift().getOverallStatus().getCode().equals(SHIFT_APPROVED)) {
                    attendanceHoursManager.deleteEqualsStartDate(employee.getObjectID(), dutyDate, AttendanceHoursType.DUTY);
                }
            } else {
                Date fromDate = ServerUtils.parseDate(date.get("fromDate"), "dd.MM.yyyy");
                Date toDate = ServerUtils.parseDate(date.get("toDate"), "dd.MM.yyyy");

                EdsShiftItem fromDuty = shiftManager.getEmployeeDuty(fromDate, employee.getObjectID(), true);
                EdsShiftSettings timeslot = fromDuty.getTimeSlot();
                EdsShiftItem toDuty = shiftManager.getEmployeeDuty(toDate, employee.getObjectID(), false);
                if (toDuty == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Employee already has duty on this date.", SERVER_ERROR, HttpStatus.BAD_REQUEST);
                }
                shiftManager.updateEmployeeDuty(toDuty, timeslot);
                shiftManager.updateEmployeeDuty(fromDuty, null);
                if (fromDuty.getShift().getOverallStatus().getCode().equals(SHIFT_APPROVED)) {
                    attendanceHoursManager.deleteEqualsStartDate(employee.getObjectID(), fromDate, AttendanceHoursType.DUTY);
                    Date start = (Date) toDate.clone();
                    start.setHours(timeslot.getStartTime() / 60);
                    start.setMinutes(timeslot.getStartTime() % 60);

                    Date end = (Date) toDate.clone();
                    if (timeslot.getStartTime() > timeslot.getEndTime()) {
                        end.setDate(end.getDate() + 1);
                    }
                    end.setHours(timeslot.getEndTime() / 60);
                    end.setMinutes(timeslot.getEndTime() % 60);
                    commonService.saveAttendanceHour(new EmployeePresentItem(employee.getObjectID(), new DateNonConvertable(toDate), new DateNonConvertable(start), new DateNonConvertable(end), null, toDuty.getShift().getObjectID(), timeslot.getObjectID()));
                }
            }
        }
        return ResultTO.success(true);
    }

    @Operation(summary = "Send Vacancy to HH.uz")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Sent vacancy"))
    @RequestMapping(path = "/send_to_hh", method = RequestMethod.POST)
    public IdName sendToHH(@RequestParam Integer vacancyId) throws IOException, ParseException {
        EdsVacancy vacancy = vacancyManager.get(vacancyId);
        List<EdsCompanyCustomFieldsSettings> cfs = customFieldsManager.getCompanyCustomFieldsByEntityName(ViewName.Vacancy.name());
        Map<String, EdsCompanyCustomFieldsSettings> cfMap = cfs == null ? new HashMap<>() : cfs.stream().collect(Collectors.toMap(EdsCompanyCustomFieldsSettings::getAliasName, cf -> cf));
        HHRequestDto request = new HHRequestDto();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", "Bearer " + recruitmentIntegrationManager.getCompanyCredentials().getHhAccessToken());

        request.setName(vacancy.getJobTitle());
        request.setCode(vacancy.getVacancyNumber());
        String description = "";
        if (vacancy.getDescription() != null) {
            description += "DESCRIPTION \n" + vacancy.getDescription() + "\n\n";
        }
        if (vacancy.getJobrequirements() != null) {
            description += "JOB REQUIREMENTS \n" + vacancy.getJobrequirements() + "\n\n";
        }
        if (vacancy.getResponsibility() != null) {
            description += "RESPONSIBILITIES \n" + vacancy.getResponsibility() + "\n";
        }
        request.setDescription(description);

        if (vacancy.getProposedSalary() != null) {
            HHSalaryDto salaryDto = new HHSalaryDto();
            salaryDto.setFrom(((Long) new DecimalFormat(",##0.00").parse(vacancy.getProposedSalary())).intValue());
            int salaryTo = salaryDto.getFrom();
            if (cfMap.get("SALARY_TO") != null && vacancy.getVacancyCustomFields() != null) {
                salaryTo = (vacancy.getVacancyCustomFields().getDoubleValue(cfMap.get("SALARY_TO").getColumnCode())).intValue();
            }
            salaryDto.setTo(salaryTo);
            salaryDto.setCurrency(vacancy.getCurrency() != null ? vacancy.getCurrency().getName() : "USD");
            request.setSalary(salaryDto);
        }

//        if (vacancy.getLocation() != null && vacancy.getLocation().getLocale() != null && vacancy.getLocation().getLocale().getArabic() != null) {
//            String locationName = vacancy.getLocation().getLocale().getArabic();
//            HHAreaDto locations = restTemplate.getForObject("https://api.hh.ru/areas/97", HHAreaDto.class, headers); // 97 is area id for Uzbekistan
//
//            if (locations != null) {
//                Optional<HHIdDto> area = locations.getAreas().stream().filter(l -> l.getName().equals(locationName)).findFirst();
//                area.ifPresent(hhIdDto -> request.setAreas(Collections.singletonList(new HHIdDto(hhIdDto.getId()))));
//
//                //TODO need to discuss about address
//            }
//        }

        if (cfMap.get("EXPERIENCE") != null && vacancy.getVacancyCustomFields() != null && StringUtils.isNotBlank(vacancy.getVacancyCustomFields().getStringValue(cfMap.get("EXPERIENCE").getColumnCode()))) {
            double experience = Double.valueOf(vacancy.getVacancyCustomFields().getStringValue(cfMap.get("EXPERIENCE").getColumnCode()));
            String experienceId = "noExperience";
            if (experience >= 1 && experience < 3) {
                experienceId = "between1And3";
            } else if (experience >= 3 && experience < 6) {
                experienceId = "between3And6";
            } else if (experience >= 6) {
                experienceId = "moreThan6";
            }
            request.setExperience(new HHIdDto(experienceId));
        }

        ArrayList<EdsSpokenLanguages> spokenLanguages = spokenLanguagesManager.getListByRelation(vacancy.getObjectID(), EdsSpokenLanguages.TYPE_VACANCY);
        if (spokenLanguages != null && !spokenLanguages.isEmpty()) {
            List<HHLanguageDto> languages = new ArrayList<>();
            for (EdsSpokenLanguages language : spokenLanguages) {
                languages.add(new HHLanguageDto(language.getLanguage().getDescription(), new HHIdDto(language.getLevel().getDescription())));
            }
            request.setLanguages(languages);
        }
        return restTemplate.exchange("https://api.hh.ru/vacancies/drafts?host=hh.uz", HttpMethod.POST, new HttpEntity<>(request, headers), IdName.class).getBody();
    }

    @Operation(summary = "Get Quiz Custom Form Scores", description = "Get Quiz Custom Form Scores")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have get Quiz Custom Form fields score values"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/quiz-form/score", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<List<CustomFieldTo>> getQuizFormFieldsScore(@RequestParam("objectKey") String objectKey) throws RestException {
        EdsCrmContact candidate = candidateManager.getByObjectKey(objectKey);
        if (candidate == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Candidate is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (candidate.getVacancies() == null || candidate.getVacancies().isEmpty()) {
            return null;
        }

        List<EdsCompanyCustomFieldsSettings> cfs = companyCustomFieldsManager.getCompanyCustomFieldsWithCategory(ViewName.CustomFormItemTable.name(), "ITEM_TABLE_BnKGQ6Dl91");
        if (cfs == null || cfs.isEmpty()) {
            return null;
        }
        Map<String, String> columnCodes = cfs.stream().collect(Collectors.toMap(EdsCompanyCustomFieldsSettings::getAliasName, EdsCompanyCustomFieldsSettings::getColumnCode));
        String positionCategory = candidate.getVacancies().iterator().next().getPosition().getCustomFields().getStringValue6();

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setForm("TEST_FORM");
        List<EdsCustomFormItems> items = customFormItemManager.list(fp, 0, 1);
        if (items == null || items.isEmpty()) {
            return null;
        }

        fp.setForm("TEST_SETTINGS_FORM");
        List<EdsCustomFormItems> settings = customFormItemManager.allList(fp);
        if (settings == null || settings.isEmpty()) {
            return null;
        }
        EdsCustomFormItems setting = settings.stream().filter(s -> s.getFormCustomFields().getStringValue1().equals(positionCategory)).findFirst().orElse(null);
        if (setting == null) {
            return null;
        }
        Map<String, Integer> questionCategories = Arrays.stream(setting.getFormCustomFields().getStringValue3().split(",")).map(s -> s.split("\\$")).collect(Collectors.toMap(s -> s[0], s -> Integer.parseInt(s[1])));

        List<EdsCustomItemTable> itemTables = new ArrayList<>();
        for (String groupId : questionCategories.keySet()) {
            itemTables.addAll(items.get(0).getItemTables().stream().filter(i -> {
                String posIds = i.getCustomFields().getStringValue(columnCodes.get("Group ID"));
                if (posIds != null && !"".equals(posIds)) {
                    String[] positions = posIds.split(",");
                    return Arrays.stream(positions).anyMatch(p -> p.trim().equals(groupId));
                }
                return false;
            }).toList().stream().sorted((a, b) -> new Random().nextInt(3) - 1).limit(questionCategories.get(groupId)).toList());
        }

        List<CustomFieldTo> result = new ArrayList<>();
        for (EdsCustomItemTable itemTable : itemTables) {
            EdsCustomItemTableCF cf = itemTable.getCustomFields();
            CustomFieldTo customField = new CustomFieldTo();
            customField.setAliasName(cf.getStringValue(columnCodes.get("Question")));
            customField.setLocale(new LocaleDto(cf.getStringValue(columnCodes.get("Question RU")), cf.getStringValue(columnCodes.get("Question")),
                    cf.getStringValue(columnCodes.get("Question UZ")), null));

            List<QuizScoreDto> options = new ArrayList<>();

            QuizScoreDto option = new QuizScoreDto();
            option.setName(cf.getStringValue(columnCodes.get("A")));
            option.setLocale(new LocaleDto(cf.getStringValue(columnCodes.get("A RU")), cf.getStringValue(columnCodes.get("A")),
                    cf.getStringValue(columnCodes.get("A UZ")), null));
            options.add(option);

            option = new QuizScoreDto();
            option.setName(cf.getStringValue(columnCodes.get("B")));
            option.setLocale(new LocaleDto(cf.getStringValue(columnCodes.get("B RU")), cf.getStringValue(columnCodes.get("B")),
                    cf.getStringValue(columnCodes.get("B UZ")), null));
            options.add(option);

            option = new QuizScoreDto();
            option.setName(cf.getStringValue(columnCodes.get("C")));
            option.setLocale(new LocaleDto(cf.getStringValue(columnCodes.get("C RU")), cf.getStringValue(columnCodes.get("C")),
                    cf.getStringValue(columnCodes.get("C UZ")), null));
            options.add(option);

            option = new QuizScoreDto();
            option.setName(cf.getStringValue(columnCodes.get("D")));
            option.setLocale(new LocaleDto(cf.getStringValue(columnCodes.get("D RU")), cf.getStringValue(columnCodes.get("D")),
                    cf.getStringValue(columnCodes.get("D UZ")), null));
            options.add(option);

            customField.setCustomFieldScoreValues(options);
            result.add(customField);
        }
        return ResultTO.success(result);
    }

    @Operation(summary = "Start Test", description = "Start Test")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Id and Object key of created test"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/test/start", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResultTO<IdCode> startTest(@RequestBody CustomFormDto dto) throws RestException {
        FormItems item = new FormItems();
        EdsCustomForm form = null;
        if (dto.getForm().getId() != null) {
            form = customFormManager.get(dto.getForm().getId());
        }
        if (dto.getForm().getCode() != null && form == null) {
            form = customFormManager.findByFormID(dto.getForm().getCode());
        }
        if (form == null) {
            throw new RestException("Form is not found", "Form is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        item.setFormID(form.getFormID());
        item.setTimerStartedAt(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
        item.setRelationObjectKey(dto.getRelationObjectKey());
        item.setRelationType(dto.getRelationType());

        EdsCrmContact candidate = candidateManager.getByObjectKey(dto.getRelationObjectKey());
        item.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(Collections.singletonList(new CustomFieldRequest("Candidate", new ItemDto(candidate.getObjectID(), candidate.getFullName()))),
                commonService.getCompanyCategoryCustomFields(form.getObjectID()), null));
        Integer id = commonService.saveCustomFormItem(item);
        EdsCustomFormItems items = customFormItemManager.get(id);
        return ResultTO.success(new IdCode(id, null, items.getObjectKey()));
    }


    @Operation(summary = "Patch Update existing Quiz")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Quiz"))
    @RequestMapping(method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<IdCode> patchUpdate(@RequestBody CustomFormDto dto) throws RestException {
        EdsCustomFormItems edsCustomFormItems = getItem(dto.getId(), dto.getObjectKey());
        FormItems item = commonService.getCustomFormItem(edsCustomFormItems.getObjectID(), edsCustomFormItems.getCustomForm().getObjectID(), edsCustomFormItems.getCustomForm().getFormID(), false, null, null, null, null);
        if (edsCustomFormItems.getCustomForm().getTimer() != null) {
            String[] estimatedTime = edsCustomFormItems.getCustomForm().getTimer().split(",");
            long duration = (Long.parseLong(estimatedTime[0]) * 60 * 60 * 1000) + (Long.parseLong(estimatedTime[1]) * 60 * 1000);
            if (new Date().getTime() - edsCustomFormItems.getAuditInfo().getCreationDate().getTime() > duration) {
                item.setStatusCode("TIME_IS_UP");
                item.setDurationTime(getDuration(edsCustomFormItems.getAuditInfo().getCreationDate().getTime(), edsCustomFormItems.getAuditInfo().getCreationDate().getTime() + duration));
                commonService.saveCustomFormItem(item);
                return ResultTO.success(new IdCode(edsCustomFormItems.getObjectID(), "TIME_IS_UP", edsCustomFormItems.getObjectKey()));
            }
        }

        item.setDurationTime(getDuration(edsCustomFormItems.getAuditInfo().getCreationDate().getTime(), new Date().getTime()));
        double score = edsCustomFormItems.getFormCustomFields().getDoubleValue1() != null ? edsCustomFormItems.getFormCustomFields().getDoubleValue1() : 0;
        if (dto.getCustomFields() != null && !dto.getCustomFields().isEmpty()) {
            CustomFieldRequest request = dto.getCustomFields().get(0);

            List<EdsCompanyCustomFieldsSettings> questionCfs = companyCustomFieldsManager.getCompanyCustomFieldsWithCategory(ViewName.CustomFormItemTable.name(), "ITEM_TABLE_BnKGQ6Dl91");
            if (questionCfs == null || questionCfs.isEmpty()) {
                return null;
            }
            Map<String, String> questionColumnCodes = questionCfs.stream().collect(Collectors.toMap(EdsCompanyCustomFieldsSettings::getAliasName, EdsCompanyCustomFieldsSettings::getColumnCode));

            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setForm("TEST_FORM");
            List<EdsCustomFormItems> items = customFormItemManager.list(fp, 0, 1);
            if (items == null || items.isEmpty()) {
                return null;
            }

            EdsCustomItemTable customItemTable = items.get(0).getItemTables().stream().filter(t -> t.getCustomFields().getStringValue(questionColumnCodes.get("Question")).equals(request.getAlias()))
                    .findFirst().get();
            String correctAnswer = customItemTable.getCustomFields().getStringValue(questionColumnCodes.get(customItemTable.getCustomFields().getStringValue(questionColumnCodes.get("Answer"))));
            String category = customItemTable.getCustomFields().getStringValue(questionColumnCodes.get("Group ID"));

            ArrayList<CompanyCustomFieldItem> cfs = commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, "ITEM_TABLE_ROfpFK3NxK");
            ArrayList<CustomTableRpc> tableItems = item.getTableItems() != null && item.getTableItems().get("ITEM_TABLE_ROfpFK3NxK") != null ? item.getTableItems().get("ITEM_TABLE_ROfpFK3NxK") : new ArrayList<>();
            for (CompanyCustomFieldItem cf : cfs) {
                switch (cf.getAliasName()) {
                    case "Savol" -> cf.setFieldStringValue(request.getAlias());
                    case "Javob" -> cf.setFieldStringValue((String) request.getValue());
                    case "Score" -> {
                        boolean isCorrect = correctAnswer.equals(request.getValue());
                        score += isCorrect ? 2 : 0;
                        cf.setFieldStringValue(isCorrect ? "2" : "0");
                    }
                    case "Category" -> {
                        EdsReference edsCategory = referenceManager.getByCode(category);
                        if (edsCategory != null) {
                            cf.setFieldStringValue(edsCategory.getName());
                            cf.setSelectedId(edsCategory.getObjectID());
                        }
                    }

                }
            }

            CustomTableRpc rpc = new CustomTableRpc();
            rpc.setUuid("ITEM_TABLE_ROfpFK3NxK");
            rpc.setItemCustomFields(cfs);
            rpc.setSorder(tableItems.isEmpty() ? 1 : tableItems.get(tableItems.size() - 1).getSorder() + 1);
            tableItems.add(rpc);
            item.setTableItems(new HashMap<>(Collections.singletonMap("ITEM_TABLE_ROfpFK3NxK", tableItems)));
        }
        ArrayList<CompanyCustomFieldItem> customFields = CustomFieldsUtils.convertCustomFields(Arrays.asList(new CustomFieldRequest("Overall Score", score),
                        new CustomFieldRequest("Duration", item.getDurationTime())),
                commonService.getCompanyCategoryCustomFields(customFormManager.findByFormID(item.getFormID()).getObjectID()),
                edsCustomFormItems.getFormCustomFields());
        if (item.getCustomFieldItems() != null) {
            customFields.forEach(cf -> {
                item.getCustomFieldItems().removeIf(cfItem -> cf.getObjectId() != null && cf.getObjectId().equals(cfItem.getObjectId()));
            });
            item.getCustomFieldItems().addAll(customFields);
        } else {
            item.setCustomFieldItems(customFields);
        }
        commonService.saveCustomFormItem(item);
        return ResultTO.success(new IdCode(edsCustomFormItems.getObjectID(), null, edsCustomFormItems.getObjectKey()));
    }

    @Operation(summary = "Get existing Custom Form Item by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Custom Form Item"))
    @RequestMapping(value = "/summary", method = RequestMethod.POST)
    public ResultTO<CustomFormDto> getById(@RequestBody IdCode dto) throws RestException {
        EdsUser user = userManager.getUser();
        EdsCustomFormItems items = getItem(dto.getId(), dto.getObjectKey());
        CustomFormDto result = new CustomFormDto();
        result.setId(items.getObjectID());
        result.setObjectKey(items.getObjectKey());
        result.setRelationType(items.getRelationType());
        result.setRelationId(items.getRelationId());
        result.setRelationObjectKey(items.getRelationObjectKey());
        result.setDuration(items.getDurationTime());

        List<EdsCompanyCustomFieldsSettings> cfs = companyCustomFieldsManager.getCompanyCustomFieldsWithCategory(ViewName.CustomFormItems.name(), "CUSTOM_VIEW_TEST_RESULTS");
        List<EdsCompanyCustomFieldsSettings> itemCfs = companyCustomFieldsManager.getCompanyCustomFieldsWithCategory(ViewName.CustomFormItemTable.name(), "ITEM_TABLE_ROfpFK3NxK");
        if (cfs == null || cfs.isEmpty()) {
            return null;
        }
        Map<String, String> columnCodes = cfs.stream().collect(Collectors.toMap(cf -> cf.getAliasName(), cf -> cf.getColumnCode()));
        Map<String, String> itemColumnCodes = itemCfs.stream().collect(Collectors.toMap(cf -> cf.getAliasName(), cf -> cf.getColumnCode()));
        result.setScore(BigDecimal.valueOf(items.getFormCustomFields().getDoubleValue(columnCodes.get("Overall Score"))));

        result.setCreatedAt(user.getUserDate(items.getAuditInfo().getCreationDate()));
        result.setUpdatedAt(user.getUserDate(items.getAuditInfo().getModificationDate()));
        result.setCreatedBy(new IdName(null, items.getAuditInfo().getCreatedBy().getFullName()));
        result.setUpdatedBy(new IdName(null, items.getAuditInfo().getModifiedBy().getFullName()));

        if (items.getDurationTime() != null) {
            String[] duration = items.getDurationTime().split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(items.getAuditInfo().getCreationDate());

            calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(duration[0]));
            calendar.add(Calendar.MINUTE, Integer.valueOf(duration[1]));
            calendar.add(Calendar.SECOND, Integer.valueOf(duration[2]));

            result.setEndAt(user.getUserDate(calendar.getTime()));
        }

        result.setTotalQuestions(50);
        result.setAnsweredQuestions(items.getItemTables().size());
        result.setCorrectAnswers((int) items.getItemTables().stream().filter(t -> {
            if (t.getCustomFields() == null | t.getCustomFields().getDoubleValue(itemColumnCodes.get("Score")) == null) {
                return false;
            }
            return t.getCustomFields().getDoubleValue(itemColumnCodes.get("Score")) > 0;
        }).count());
        return ResultTO.success(result);
    }

    @Operation(summary = "Check existance Custom Form Item")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Custom Form Item"))
    @RequestMapping(value = "/check_existance", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<CFItemExistanceDto> checkExistance(@RequestBody CheckExistanceRequestDto dto) throws RestException {
        CFItemExistanceDto result = new CFItemExistanceDto();
        EdsCustomFormItems items = customFormItemManager.findByRelation(dto.getFormId(), dto.getRelationType(), dto.getRelationId(), dto.getRelationObjectKey());
        if (items != null) {
            result.setExists(true);
            CustomFormDto item = new CustomFormDto();

            if (items.getDurationTime() != null) {
                String[] duration = items.getDurationTime().split(":");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(items.getAuditInfo().getCreationDate());

                calendar.add(Calendar.HOUR_OF_DAY, Integer.valueOf(duration[0]));
                calendar.add(Calendar.MINUTE, Integer.valueOf(duration[1]));
                calendar.add(Calendar.SECOND, Integer.valueOf(duration[2]));

                item.setEndAt(userManager.getUser().getUserDate(calendar.getTime()));
            }
            item.setDuration(items.getDurationTime());
            item.setScore(items.getFormCustomFields().getDoubleValue1() != null ? BigDecimal.valueOf(items.getFormCustomFields().getDoubleValue1()) : null);
            result.setItem(item);
        }
        return ResultTO.success(result);
    }

    @Operation(summary = "Insert dependents")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Insert dependents"))
    @RequestMapping(value = "/insert_dependents/{type}/{id}", method = RequestMethod.POST)
    public void insertDependents(@PathVariable("type") String type, @PathVariable("id") Integer id) {
        EdsEmployee employee = type == null || type.equals("EMPLOYEE") ? employeeManager.get(id) : null;
        EdsCrmContact candidate = type != null && type.equals("CANDIDATE") ? candidateManager.getCandidateById(id) : null;
        String pinfl = employee != null ? employee.getProfile().getEmployeeCode() : candidate.getNumber();
        dependentManager.deleteRelatedDependents(id, type);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> request = new HashMap<>();
        request.put("id", "111");
        request.put("pin", pinfl);
        MyGovDependentResponseDto result = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/birthdates", new HttpEntity<>(request, httpHeaders), MyGovDependentResponseDto.class);
        if (result != null && result.getItems() != null && !result.getItems().isEmpty()) {
            MyGovDependentDto item = result.getItems().get(0);
            DependentItem father = new DependentItem();
            father.setFirstName(item.getF_first_name());
            father.setLastName(item.getF_family());
            father.setMiddleName(item.getF_patronym());
            father.setRelationship("Father");
            if (candidate != null) {
                father.setCandidateId(id);
                father.setFromCandidate(true);
            } else {
                father.setEmployeeId(id);
            }
            List<CustomFieldRequest> cfs = new ArrayList<>();
            cfs.add(new CustomFieldRequest("BIRTH_DAY", item.getF_birth_day()));
            MyGovAddressResponseDto addressResponse = getAddress(item.getF_pnfl());
            if (addressResponse != null && addressResponse.getPermanentRegistration() != null) {
                MyGovAddressDto address = addressResponse.getPermanentRegistration();
                father.setCity((String) address.getRegion().getValue());
                cfs.add(new CustomFieldRequest("COUNTRY", address.getCountry().getValue()));
                father.setAddress(address.getDistrict().getValue() + " " + address.getAddress());
            }
            MyGovPassportResponseDto passResponse = getPassInfo(item.getF_pnfl(), item.getF_birth_day());
            if (passResponse != null) {
                if (passResponse.getCurrentDocument() != null) {
                    cfs.add(new CustomFieldRequest("PASS_NUMBER", passResponse.getCurrentDocument().getSerNum()));
                    cfs.add(new CustomFieldRequest("PASS_GIVE_PLACE", passResponse.getCurrentDocument().getGivePlace()));
                    cfs.add(new CustomFieldRequest("PASS_BEGIN_DATE", passResponse.getCurrentDocument().getBeginDate()));
                    cfs.add(new CustomFieldRequest("PASS_END_DATE", passResponse.getCurrentDocument().getEndDate()));
                }
                cfs.add(new CustomFieldRequest("SEX", passResponse.getSex().equals(1) ? "Erkak" : "Ayol"));
                if (passResponse.getBirth() != null) {
                    cfs.add(new CustomFieldRequest("BIRTH_PLACE", passResponse.getBirth().getPlace()));
                }
            }
            cfs.add(new CustomFieldRequest("PINFL", item.getF_pnfl()));

            MyGovPositionResponseDto position = getPosition(item.getF_pnfl());
            if (position != null && position.getPositions() != null && !position.getPositions().isEmpty()) {
                MyGovPositionDto positionDto = position.getPositions().get(0);
                cfs.add(new CustomFieldRequest("ORGANIZATION", positionDto.getOrg()));
                cfs.add(new CustomFieldRequest("DEPARTMENT", positionDto.getDepName()));
                cfs.add(new CustomFieldRequest("POSITION", positionDto.getPosition()));
            }
            father.setCustomFields(CustomFieldsUtils.convertCustomFields(cfs, commonService.getCompanyCustomFields(ViewName.Dependent), null));
            hrmsService.saveDependent(father);

            DependentItem mother = new DependentItem();
            mother.setFirstName(item.getM_first_name());
            mother.setLastName(item.getM_family());
            mother.setMiddleName(item.getM_patronym());
            mother.setRelationship("Mother");
            if (candidate != null) {
                mother.setCandidateId(id);
                mother.setFromCandidate(true);
            } else {
                mother.setEmployeeId(id);
            }
            cfs = new ArrayList<>();
            cfs.add(new CustomFieldRequest("BIRTH_DAY", item.getM_birth_day()));
            MyGovAddressResponseDto mAddressResponse = getAddress(item.getM_pnfl());
            if (mAddressResponse != null && mAddressResponse.getPermanentRegistration() != null) {
                MyGovAddressDto mAddress = mAddressResponse.getPermanentRegistration();
                mother.setCity((String) mAddress.getRegion().getValue());
                cfs.add(new CustomFieldRequest("COUNTRY", mAddress.getCountry().getValue()));
                mother.setAddress(mAddress.getDistrict().getValue() + " " + mAddress.getAddress());
            }
            MyGovPassportResponseDto passMResponse = getPassInfo(item.getM_pnfl(), item.getM_birth_day());
            if (passMResponse != null) {
                if (passMResponse.getCurrentDocument() != null) {
                    cfs.add(new CustomFieldRequest("PASS_NUMBER", passMResponse.getCurrentDocument().getSerNum()));
                    cfs.add(new CustomFieldRequest("PASS_GIVE_PLACE", passMResponse.getCurrentDocument().getGivePlace()));
                    cfs.add(new CustomFieldRequest("PASS_BEGIN_DATE", passMResponse.getCurrentDocument().getBeginDate()));
                    cfs.add(new CustomFieldRequest("PASS_END_DATE", passMResponse.getCurrentDocument().getEndDate()));
                }
                cfs.add(new CustomFieldRequest("SEX", passMResponse.getSex().equals(1) ? "Erkak" : "Ayol"));
                if (passMResponse.getBirth() != null) {
                    cfs.add(new CustomFieldRequest("BIRTH_PLACE", passMResponse.getBirth().getPlace()));
                }
            }
            cfs.add(new CustomFieldRequest("PINFL", item.getM_pnfl()));

            MyGovPositionResponseDto mPosition = getPosition(item.getM_pnfl());
            if (mPosition != null && mPosition.getPositions() != null && !mPosition.getPositions().isEmpty()) {
                MyGovPositionDto positionDto = mPosition.getPositions().get(0);
                cfs.add(new CustomFieldRequest("ORGANIZATION", positionDto.getOrg()));
                cfs.add(new CustomFieldRequest("DEPARTMENT", positionDto.getDepName()));
                cfs.add(new CustomFieldRequest("POSITION", positionDto.getPosition()));
            }
            mother.setCustomFields(CustomFieldsUtils.convertCustomFields(cfs, commonService.getCompanyCustomFields(ViewName.Dependent), null));
            hrmsService.saveDependent(mother);

            MyGovMarriageResponseDto marriageResponse = restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/marriages", new HttpEntity<>(request, httpHeaders), MyGovMarriageResponseDto.class);
            if (marriageResponse != null && marriageResponse.getItems() != null && !marriageResponse.getItems().isEmpty()) {
                MyGovMarriageDto marriage = marriageResponse.getItems().get(0);
                if (marriage.getH_pnfl() != null && marriage.getW_pnfl() != null) {
                    boolean isHusband = marriage.getW_pnfl().equals(pinfl);

                    DependentItem dependent = new DependentItem();
                    dependent.setFirstName(isHusband ? marriage.getH_first_name() : marriage.getW_first_name());
                    dependent.setLastName(isHusband ? marriage.getH_family() : marriage.getW_family_after() != null ? marriage.getW_family_after() : marriage.getW_family());
                    dependent.setMiddleName(isHusband ? marriage.getH_patronym() : marriage.getW_patronym());
                    dependent.setRelationship(isHusband ? "Husband" : "Wife");
                    if (candidate != null) {
                        dependent.setCandidateId(id);
                        dependent.setFromCandidate(true);
                    } else {
                        dependent.setEmployeeId(id);
                    }
                    cfs = new ArrayList<>();
                    cfs.add(new CustomFieldRequest("BIRTH_DAY", isHusband ? marriage.getH_birth_day() : marriage.getW_birth_day()));
                    MyGovAddressResponseDto dAddressResponse = getAddress(isHusband ? marriage.getH_pnfl() : marriage.getW_pnfl());
                    if (dAddressResponse != null && dAddressResponse.getPermanentRegistration() != null) {
                        MyGovAddressDto dAddress = dAddressResponse.getPermanentRegistration();
                        dependent.setCity((String) dAddress.getRegion().getValue());
                        cfs.add(new CustomFieldRequest("COUNTRY", dAddress.getCountry().getValue()));
                        dependent.setAddress(dAddress.getDistrict().getValue() + " " + dAddress.getAddress());
                    }
                    MyGovPassportResponseDto passDResponse = getPassInfo(isHusband ? marriage.getH_pnfl() : marriage.getW_pnfl(), isHusband ? marriage.getH_birth_day() : marriage.getW_birth_day());
                    if (passDResponse != null) {
                        if (passDResponse.getCurrentDocument() != null) {
                            cfs.add(new CustomFieldRequest("PASS_NUMBER", passDResponse.getCurrentDocument().getSerNum()));
                            cfs.add(new CustomFieldRequest("PASS_GIVE_PLACE", passDResponse.getCurrentDocument().getGivePlace()));
                            cfs.add(new CustomFieldRequest("PASS_BEGIN_DATE", passDResponse.getCurrentDocument().getBeginDate()));
                            cfs.add(new CustomFieldRequest("PASS_END_DATE", passDResponse.getCurrentDocument().getEndDate()));
                        }
                        cfs.add(new CustomFieldRequest("SEX", passDResponse.getSex().equals(1) ? "Erkak" : "Ayol"));
                        if (passDResponse.getBirth() != null) {
                            cfs.add(new CustomFieldRequest("BIRTH_PLACE", passDResponse.getBirth().getPlace()));
                        }
                    }
                    cfs.add(new CustomFieldRequest("PINFL", isHusband ? marriage.getH_pnfl() : marriage.getW_pnfl()));

                    MyGovPositionResponseDto mDPosition = getPosition(isHusband ? marriage.getH_pnfl() : marriage.getW_pnfl());
                    if (mDPosition != null && mDPosition.getPositions() != null && !mDPosition.getPositions().isEmpty()) {
                        MyGovPositionDto positionDto = mDPosition.getPositions().get(0);
                        cfs.add(new CustomFieldRequest("ORGANIZATION", positionDto.getOrg()));
                        cfs.add(new CustomFieldRequest("DEPARTMENT", positionDto.getDepName()));
                        cfs.add(new CustomFieldRequest("POSITION", positionDto.getPosition()));
                    }
                    dependent.setCustomFields(CustomFieldsUtils.convertCustomFields(cfs, commonService.getCompanyCustomFields(ViewName.Dependent), null));
                    hrmsService.saveDependent(dependent);
                }
            }
        }
    }

    private MyGovAddressResponseDto getAddress(String pinfl) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> request = new HashMap<>();
        request.put("pin", pinfl);
        try {
            return restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/addresses-pin", new HttpEntity<>(request, httpHeaders), MyGovAddressResponseDto.class);
        } catch (Exception e) {
            return null;
        }
    }

    private MyGovPassportResponseDto getPassInfo(String pinfl, String birthDay) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date = LocalDate.parse(birthDay, inputFormatter);
        String outputDate = date.format(outputFormatter);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> request = new HashMap<>();
        request.put("pin", pinfl);
        request.put("lang_id", 3);
        request.put("birth_date", outputDate);
        request.put("document", "uz");
        request.put("is_photo", "Y");
        try {
            return restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/people/info", new HttpEntity<>(request, httpHeaders), MyGovPassportResponseDto.class);
        } catch (Exception e) {
            return null;
        }
    }

    private MyGovPositionResponseDto getPosition(String pinfl) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        DynamicDto request = new DynamicDto();
        request.addProperty("pin", pinfl);
        try {
            return restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/current-positions", new HttpEntity<>(request, httpHeaders), MyGovPositionResponseDto.class);
        } catch (Exception e) {
            return null;
        }
    }

    private EdsCustomFormItems getItem(Integer id, String objectKey) throws RestException {
        return (id != null ? Optional.ofNullable(customFormItemManager.get(id)) : Optional.ofNullable(customFormItemManager.getByObjectKey(objectKey)))
                .orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Custom form item with this id or object key is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    private String getDuration(long startedAt, long finishedAt) {
        long totalTime = finishedAt - startedAt;
        int hh, mm, ss;
        hh = (int) ((totalTime / 1000) / 3600);
        mm = (int) (((totalTime / 1000) / 60) % 60);
        ss = (int) ((totalTime / 1000) % 60);
        return hh + ":" + mm + ":" + ss;
    }

    @RequestMapping(path = "/reindexing", method = RequestMethod.GET, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ResponseEntity<Void>> reIndexing(@RequestParam Integer companyId, @RequestParam String core) throws RestException {
        try {
            SolrReindexRpc solrReindexRpc = new SolrReindexRpc();
            solrReindexRpc.setCompanyId(companyId);
            switch (core) {
                case SOLR_TASK_CORE:
                    backendService.reindexCompanyTasks(solrReindexRpc);
                    break;
                case "leadsCore":
                    backendService.indexCompanyLeads(solrReindexRpc);
                    break;
                case "candidatesCore":
                    backendService.indexCompanyCandidates(solrReindexRpc);
                    break;
                case SOLR_CRM_ACCOUNT_CORE:
                    backendService.indexCompanyCrmAccounts(solrReindexRpc);
                    break;
                case SOLR_CONTACT_CORE:
                    backendService.indexCompanyContacts(solrReindexRpc);
                    break;
                case SOLR_NEWS_CORE:
                    backendService.indexCompanyNews(solrReindexRpc);
                    break;
                case SOLR_FOLDER_CORE:
                    //developer be careful, this method removes all items from FolderRbac and sets default permissions so all old permissions will be lost.
                    //if you want using this method,  please contact with teamleader
                    /* backendService.indexCompanyFolders(solrReindexRpc);*/
                    break;
                case "filesCore":
                    backendService.indexCompanyFiles(solrReindexRpc);
                    break;
                case "systemFolderCore":
                    backendService.indexCompanySystemFolders(solrReindexRpc);
                    break;
                case SOLR_SALEINVOICE_CORE:
                    backendService.indexSaleInvoice(solrReindexRpc);
                    break;
                case SOLR_PROJECT_CORE:
                    backendService.indexCompanyProjects(solrReindexRpc);
                    break;
                case SOLR_CASE_CORE:
                    backendService.indexCompanyCrmCase(solrReindexRpc);
                    break;
                case SOLR_SALEQUOTE_CORE:
                    backendService.indexSaleQuote(solrReindexRpc);
                    break;
                case SOLR_PURCHASE_ORDER_CORE:
                    backendService.indexPurchaseOrder(solrReindexRpc);
                    break;
                case SOLR_OPPORTUNITY_CORE:
                    backendService.indexOpportunities(solrReindexRpc);
                    break;
                case SOLR_EVENT_CORE:
                    backendService.indexEvents(solrReindexRpc);
                    break;
                case SOLR_PRODUCTS_SERVICES_CORE:
                    backendService.indexProductsServices(solrReindexRpc);
                    break;
                case SOLR_PURCHASE_INVOICE_CORE:
                    backendService.indexPurchaseInvoice(solrReindexRpc);
                    break;
                case SOLR_EXPENSE_REPORT_CLAIMS_CORE:
                    backendService.indexExpenseReportClaims(solrReindexRpc);
                    break;
                case SOLR_COURSE_BOOKING_CORE:
                    backendService.indexCourseBookings(solrReindexRpc);
                    break;
                case SOLR_COURSE_SCHEDULE_CORE:
                    backendService.indexCourseSchedule(solrReindexRpc);
                    break;
                case SOLR_EMPLOYEE_CORE:
                    backendService.indexEmployee(solrReindexRpc);
                    break;
                case SOLR_SINGLE_PAYRUN_CORE:
                    backendService.indexSinglePayrun(solrReindexRpc);
                    break;
                case SOLR_GROUP_PAYRUN_CORE:
                    backendService.indexGroupPayrun(solrReindexRpc);
                    break;
                case SOLR_CASH_ADVANCE_CORE:
                    backendService.indexCashAdvance(solrReindexRpc);
                    break;
                case SOLR_VACANCY_CORE:
                    backendService.indexVacancy(solrReindexRpc);
                    break;
                case SOLR_EMPLOYEE_STEP_CORE:
                    backendService.indexEmployeeStep(solrReindexRpc);
                    break;
                case SOLR_ADDITIONAL_PAYMENT_CORE:
                    backendService.indexAdditionalPayment(solrReindexRpc);
                    break;
                case SOLR_CHART_OF_ACCOUNT_CORE:
                    backendService.indexChartOfAccount(solrReindexRpc);
                    break;
                case SOLR_LEAVE_REQUEST_CORE:
                    backendService.indexLeaveRequest(solrReindexRpc);
                    break;
                case SOLR_CUSTOM_FORM_ITEM_CORE:
                    backendService.indexCustomFormItems(solrReindexRpc);
                    break;
                case SOLR_SHIPPING_DATA_CORE:
                    backendService.indexShippingData(solrReindexRpc);
                    break;
                case SOLR_REQUEST_FOR_QUOTE_CORE:
                    backendService.indexRFQ(solrReindexRpc);
                    break;
                case SOLR_CERTIFICATE_CORE:
                    backendService.indexCertificates(solrReindexRpc);
                    break;
                case SOLR_POSITION_CORE:
                    backendService.indexPositions(solrReindexRpc);
                    break;
                case SOLR_DEPARTMENT_CORE:
                    backendService.indexDepartments(solrReindexRpc);
                    break;
                default:
                    break;
            }
            return ResultTO.success(ResponseEntity.ok().build());
        } catch (Exception e) {
            return ResultTO.failure(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build().toString(),500);
        }
    }
}
