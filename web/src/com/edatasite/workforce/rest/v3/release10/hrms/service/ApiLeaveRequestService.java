package com.edatasite.workforce.rest.v3.release10.hrms.service;

import com.edatasite.workforce.core.domain.EdsAnnualLeaveAllowance;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsFormProperty;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.availability.client.rpc.NewLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.TimeSlot;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.UnitType;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.LeaveRequestChartRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.AnnualLeaveAllowanceManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FormPropertyManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestDurationManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.LeaveReasonStateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.RequestUserActionTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.LeaveRequestDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.MultiLeaveRequestDTO;
import com.google.gson.Gson;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.TYPE_LEAVE_REQUEST;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.DESCRIPTION;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.REASON;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TAKE_LIVE_TYPE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.TYPE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEES;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_LEAVE_REQUEST;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.VALIDATION;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

/**
 * User : Akhror
 * Date : 10.07.2021
 */
@Service
public class ApiLeaveRequestService {

    private static final String unauthorized_leave = "LR_TYPE_UNAUTHORIZED_LEAVE";

    private final Gson gson = new Gson();
    private final AvailabilityService availabilityService;
    private final AttachmentUtilsManager attachmentUtilsManager;
    private final SickRequestManager sickRequestManager;
    private final EmployeeManager employeeManager;
    private final AllInOneServiceLocal allInOneServiceLocal;
    private final UserManager userManager;
    private final LeaveReasonManager leaveReasonManager;
    private final CommonService commonService;
    private final FormPropertyManager formPropertyManager;
    private final CommonServiceLocal commonServiceLocal;
    private final CustomFormItemManager customFormItemManager;
    private final CustomFormManager customFormManager;
    private final RelationManager relationManager;
    private final AvailabilityServiceLocal availabilityServiceLocal;
    private final SickRequestDurationManager sickRequestDurationManager;
    private final AnnualLeaveAllowanceManager annualLeaveAllowanceManager;

    public ApiLeaveRequestService(AvailabilityService availabilityService,
                                  AttachmentUtilsManager attachmentUtilsManager,
                                  SickRequestManager sickRequestManager,
                                  EmployeeManager employeeManager,
                                  AllInOneServiceLocal allInOneServiceLocal,
                                  UserManager userManager,
                                  LeaveReasonManager leaveReasonManager,
                                  CommonService commonService,
                                  FormPropertyManager formPropertyManager,
                                  CommonServiceLocal commonServiceLocal,
                                  CustomFormItemManager customFormItemManager,
                                  CustomFormManager customFormManager,
                                  RelationManager relationManager,
                                  AvailabilityServiceLocal availabilityServiceLocal,
                                  SickRequestDurationManager sickRequestDurationManager,
                                  AnnualLeaveAllowanceManager annualLeaveAllowanceManager) {
        this.availabilityService = availabilityService;
        this.attachmentUtilsManager = attachmentUtilsManager;
        this.sickRequestManager = sickRequestManager;
        this.employeeManager = employeeManager;
        this.allInOneServiceLocal = allInOneServiceLocal;
        this.userManager = userManager;
        this.leaveReasonManager = leaveReasonManager;
        this.commonService = commonService;
        this.formPropertyManager = formPropertyManager;
        this.commonServiceLocal = commonServiceLocal;
        this.customFormItemManager = customFormItemManager;
        this.customFormManager = customFormManager;
        this.relationManager = relationManager;
        this.availabilityServiceLocal = availabilityServiceLocal;
        this.sickRequestDurationManager = sickRequestDurationManager;
        this.annualLeaveAllowanceManager = annualLeaveAllowanceManager;
    }

    public ListResultTO<LeaveRequestDTO> getLeaveRequestsList(ListingFilterParameter fp) {
        ListResult<LeaveRequestLisItem> leaveRequestsList = availabilityService.getLeaveRequestList(fp);

        ListResultTO<LeaveRequestDTO> leaveRequests = new ListResultTO<>();
        if (leaveRequestsList != null) {
            List<Integer> ids = leaveRequestsList.getList().stream().map(doc -> Objects.requireNonNull(doc.getObjectId())).toList();
            leaveRequests.setTotalNumber(ids.size());
            ArrayList<LeaveRequestDTO> items = new ArrayList<>();
            ids.forEach(id -> {
                StatisticsLeaveRequest item = availabilityService.getLeaveRequest(id);
                List<FileResource> files = attachmentUtilsManager.getAttachments(F_LEAVE_REQUEST, id, id);
                items.add(ConvertUtils.toDto(item, files, userManager.getUser().getObjectID()));

            });
            leaveRequests.setItems(items);
        }
        return leaveRequests;
    }

    @Transactional(readOnly = true)
    public LeaveRequestDTO getById(final Integer id) throws RestException {
        Optional.ofNullable(sickRequestManager.get(id)).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Leave Request with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST));
        StatisticsLeaveRequest item = availabilityService.getLeaveRequest(id);
        List<FileResource> files = attachmentUtilsManager.getAttachments(F_LEAVE_REQUEST, id, id);
        LeaveRequestDTO leaveRequestDTO = ConvertUtils.toDto(item, files, userManager.getUser().getObjectID());
        leaveRequestDTO.setUserAction(getUserAction(item, userManager.getUser()));
        try {
            leaveRequestDTO.setState_records(getLeaveReasonStateList(leaveReasonManager.get(item.getReasonId()), userManager.getUser(), item.getStartDDate().getDate(), item.getEndDDate().getDate()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return leaveRequestDTO;
    }

    private RequestUserActionTO getUserAction(StatisticsLeaveRequest leaveRequest, EdsUser user) {
        RequestUserActionTO userAction = new RequestUserActionTO();
        if (!leaveRequest.isAction()) {
            userAction.setApprove_for_all(false);
            userAction.setApprove(false);
            userAction.setReject(false);
        } else if (leaveRequest.getOverallStatus() != null && EdsSickRequest.NOT_DEFINED.equals(leaveRequest.getOverallStatus().getCode())) {
            if (user.hasRole(EdsRole.ADMIN_CODE) || (leaveRequest.getCurrentApproverEmployeeID() != null && user.getObjectID().equals(leaveRequest.getCurrentApproverEmployeeID()))) {
                if (leaveRequest.isApproveForAll()) {
                    userAction.setApprove_for_all(true);
                    userAction.setApprove(true);
                    userAction.setReject(true);
                } else {
                    userAction.setApprove_for_all(false);
                    userAction.setApprove(true);
                    userAction.setReject(true);
                }
            } else {
                userAction.setApprove_for_all(false);
                userAction.setApprove(false);
                userAction.setReject(false);
            }
        } else {
            userAction.setApprove_for_all(false);
            userAction.setApprove(false);
            userAction.setReject(false);
        }
        return userAction;
    }


    private ArrayList<LeaveReasonStateTO> getLeaveReasonStateList(EdsLeaveReason reason, EdsUser user, Date startDate, Date endDate) throws RestException {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(user.getObjectID());
        fp.setReasonID(reason.getObjectID());
        fp.setYear(Integer.parseInt(new SimpleDateFormat("yyyy").format(startDate)));
        fp.setStartDate(startDate);
        fp.setEndDate(endDate);
        fp.setStatusCode(Constants.LR_STATUS_SS_APPROVED);
        fp.setReasonCode(reason.getCode());
        fp.setPaid(true);
        LeaveRequestChartRpc data = availabilityServiceLocal.getLeaveRequestChartData(fp);

        ArrayList<LeaveReasonStateTO> leaveReasonStates = new ArrayList<>();

        EdsAnnualLeaveAllowance edsAnnualLeaveAllowance = annualLeaveAllowanceManager.getLeaveAllowanceByReason(fp.getYear(), user.getObjectID(), reason.getCode(), null);
        if (edsAnnualLeaveAllowance != null) {
            leaveReasonStates.add(new LeaveReasonStateTO("Allowance", 0d, edsAnnualLeaveAllowance.getAllowanceDays()));
        }
        if (data.getPaid().length > 0 && data.getPaid()[0] != null) {
            leaveReasonStates.add(new LeaveReasonStateTO("Approved Paid Days", 0d, data.getPaid()[0]));
        }
        if (data.getLeft().length > 0 && data.getLeft()[0] != null) {
            leaveReasonStates.add(new LeaveReasonStateTO("Left Days", 0d, data.getLeft()[0]));
        }
        if (data.getNonPaid().length > 0 && data.getNonPaid()[0] != null) {
            leaveReasonStates.add(new LeaveReasonStateTO("Non-Paid Days", 0d, data.getNonPaid()[0]));
        }
        leaveReasonStates.add(setLeaveRequestDetail(fp));
        return leaveReasonStates;
    }

    private LeaveReasonStateTO setLeaveRequestDetail(ListingFilterParameter fp) {
        HashMap<Integer, Double> duration = this.sickRequestDurationManager.getEmployeeLeaveDurations(fp);
        Double dayTaken = 0d;
        Double durationArray = duration.get(fp.getEmployeeId());
        if (durationArray != null) {
            dayTaken = durationArray;
        }
        return new LeaveReasonStateTO("Taken", 0.0d, dayTaken);
    }

    public Integer save(LeaveRequestDTO dto) throws RestException {
        var item = new NewLeaveRequest();
        item.setObjectID(dto.getId());
        LinkedHashMap<String, FormProperty> fields = new LinkedHashMap<>();
        EdsFormProperty edsFormProperty = formPropertyManager.getByFormID(LayoutRPC.LEAVE_REQUEST_FORM);
        EdsUser currentUser = userManager.getUser();
        if (edsFormProperty != null) {
            FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
            for (FormProperty formProperty : formFields) {
                if (formProperty == null) {
                    continue;
                }
                if (StringUtils.isEmpty(formProperty.getDefaultValue())) {
                    formProperty.setDefaultValue(null);
                }

                if (CollectionUtils.isNotEmpty(formProperty.getRoleEdit())) {
                    // fixme, check if extra calls are not sent to db
                    if (currentUser.hasEitherRoles(formProperty.getRoleEdit().toArray(new Integer[]{}))) {
                        formProperty.setDisabled(false);
                    }
                }
                fields.put(formProperty.getCode(), formProperty);
            }
        }

        EdsEmployee employee = null;
        if (dto.getEmployee() != null) {
            if (dto.getEmployee().getId() != null) {
                employee = employeeManager.get(dto.getEmployee().getId());
            } else if (dto.getEmployee().getName() != null) {
                employee = employeeManager.getEmployeeByFirstNameViaLastName(dto.getEmployee().getName());
            } else if (dto.getEmployee().getCode() != null) {
                employee = employeeManager.getEmployeeByNumber(dto.getEmployee().getCode());
            }
        }

        if (fields.get(EMPLOYEES).isRequired() && employee == null && fields.get(EMPLOYEES) != null && fields.get(EMPLOYEES).getDefaultValue() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Employee is not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (employee != null) {
            item.setEmployee(employee.getObjectID());
        } else {
            employee = employeeManager.get(fields.get(EMPLOYEES).getSelectedId());
            item.setEmployee(employee.getObjectID());
        }
        ApprovalListResult approvalListResult = allInOneServiceLocal.getApprovers(TYPE_LEAVE_REQUEST, null, true, employee.getObjectID(), false, true);
        ArrayList<ApproverItemMini> approvers = new ArrayList<>();
        if (dto.getApprover() != null && approvalListResult != null && approvalListResult.getList() != null && !approvalListResult.getList().isEmpty()) {
            int i = 0;
            for (ItemDto approver : dto.getApprover()) {
                EdsEmployee edsEmployee = null;
                if (approver.getId() != null) {
                    edsEmployee = employeeManager.get(approver.getId());
                } else if (approver.getName() != null) {
                    edsEmployee = employeeManager.getEmployeeByFirstNameViaLastName(approver.getName());
                } else if (approver.getCode() != null) {
                    edsEmployee = employeeManager.getEmployeeByNumber(approver.getCode());
                }
                if (edsEmployee != null) {
                    ApproverItemMini approverItem = new ApproverItemMini();
                    approverItem.setExactEmployee(edsEmployee.getAsSelectItem());
                    approverItem.setClonedFrom(approvalListResult.getList().get(i).getObjectID());
                    approverItem.setApproverOrder(approvalListResult.getList().get(i).getApproverOrder());
                    approvers.add(approverItem);
                    i++;
                }
            }
        }
        if (approvers.isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Approver is not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        item.setApprovers(approvers);

        if (approvers.get(0).getExactEmployee().getId().equals(employee.getObjectID()) || dto.isAutoApprove()) {
            item.setSelfApprover(true);
            item.setStatusCode(Constants.APPROVED);
        } else {
            item.setStatusCode(Constants.LR_STATUS_NOT_DEFINED);
        }

        EdsLeaveReason reason = null;
        if (dto.getReason() != null) {
            if (dto.getReason().getId() != null) {
                reason = leaveReasonManager.get(dto.getReason().getId());
            } else if (dto.getReason().getName() != null) {
                reason = leaveReasonManager.getReasonByName(dto.getReason().getName(), null);
            } else if (dto.getReason().getCode() != null) {
                reason = leaveReasonManager.findByCode(dto.getReason().getCode());
            }
        }
        if (fields.get(REASON).isRequired() && reason == null && fields.get(REASON) != null && fields.get(REASON).getDefaultValue() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Reason is not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (reason != null) {
            item.setReasonId(reason.getObjectID());
        } else {
            reason = leaveReasonManager.get(fields.get(REASON).getSelectedId());
            item.setReasonId(reason.getObjectID());
        }
        item.setAllDay(UnitType.DAILY.equals(reason.getUnitType()));

        boolean hourly = reason.getUnitType() != null && reason.getUnitType().equals(UnitType.HOURLY);
        item.setAllDay(!hourly);

        if (!hourly) {
            TimeSlot timeSlot = commonServiceLocal.getEmployeeTimeSlot(employee.getObjectID());
            int startHour = timeSlot.getStartHour() != null ? Integer.parseInt(timeSlot.getStartHour()) : 9;
            int startMinute = timeSlot.getStartMin() != null ? Integer.parseInt(timeSlot.getStartMin()) : 30;
            //
            int endHour = timeSlot.getEndHour() != null ? Integer.parseInt(timeSlot.getEndHour()) : 18;
            int endMinute = timeSlot.getEndMin() != null ? Integer.parseInt(timeSlot.getEndMin()) : 0;
            Date startDate = dto.getStartDate();
            startDate.setHours(startHour);
            startDate.setMinutes(startMinute);
            Date endDate = dto.getEndDate();
            endDate.setHours(endHour);
            endDate.setMinutes(endMinute);
            item.setStartNonConverable(new DateNonConvertable(startDate));
            item.setEndNonConverable(new DateNonConvertable(endDate));
            item.setStartHour(startHour);
            item.setStartMinut(startMinute);
            item.setEndHour(endHour);
            item.setEndMinut(endMinute);
        } else {
            item.setStartHour(dto.getStartDate().getHours());
            item.setStartMinut(dto.getStartDate().getMinutes());
            item.setEndHour(dto.getEndDate().getHours());
            item.setEndMinut(dto.getEndDate().getMinutes());
            item.setStartNonConverable(new DateNonConvertable(dto.getStartDate()));
            item.setEndNonConverable(new DateNonConvertable(dto.getEndDate()));
        }

        if (fields.get(TYPE).isRequired() && item.getType() == null && (fields.get(TYPE) == null || fields.get(TYPE).getDefaultValue() == null)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Type is not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (dto.getType() != null) {
            item.setType(dto.getType());
        } else {
            item.setType(fields.get(TYPE).getDefaultValue());
        }

        if (fields.get(TAKE_LIVE_TYPE).isRequired() && dto.getTakeLeaveBy() == null && (fields.get(TAKE_LIVE_TYPE) == null || fields.get(TAKE_LIVE_TYPE).getDefaultValue() == null)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Take live type is not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (dto.getTakeLeaveBy() != null) {
            item.setTakeByMoney(dto.getTakeLeaveBy().equals(Constants.MONEY));
        } else {
            item.setTakeByMoney(fields.get(TAKE_LIVE_TYPE).getDefaultValue().equals(Constants.MONEY));
        }

        if (fields.get(DESCRIPTION).isRequired() && dto.getDescription() == null && (fields.get(DESCRIPTION) == null || fields.get(DESCRIPTION).getDefaultValue() == null)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Description is not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        } else {
            item.setDescription(fields.get(DESCRIPTION).getDefaultValue());
        }
        if (dto.getId() == null && item.getStatusCode() == null) {
            item.setStatusCode(Constants.LR_STATUS_NOT_DEFINED);
        }
        item.setCustomFields(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.LeaveRequest), null));
        item.setFrom(LayoutRPC.LEAVE_REQUEST_FORM);
        String hasAccess = availabilityService.hasAccessInsertRequest(item.getEmployee(), item, item.getStartNonConverable(), item.getEndNonConverable(), false);
        if (!Constants.TRUE.equals(hasAccess)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, hasAccess, ApiConstants.SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        if (!unauthorized_leave.equals(reason.getDescription()) && dto.getType().equals("ST_PAID")) {
            NewLeaveRequest validItem = availabilityService.validateAllowanceLimit(item);
            if (!validItem.getValid()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Leave Request Limit Exceeded", ApiConstants.SERVER_ERROR, HttpStatus.BAD_REQUEST);
            }
        }
        Integer id = availabilityService.createLeaveRequest(item);
        if (id == VALIDATION) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Probation period not passed for this employee", ApiConstants.SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        return id;
    }

    public List<Integer> createBulkLrs(MultiLeaveRequestDTO dto) throws RestException {
        List<Integer> ids = new ArrayList<>();
        EdsCustomForm customForm = customFormManager.findByFormID(dto.getFormId());
        if (customForm == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Custom Form with this form_id is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        List<EdsCustomFormItems> cfItem = customFormItemManager.getCustomFormByIds(dto.getEntityId().toString());
        if (cfItem == null || cfItem.isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Custom Form Item with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Integer entityID = dto.getEntityId();
        String formId = dto.getFormId();
        String name = cfItem.get(0).getName();
        EdsRelation relation;
        for (LeaveRequestDTO lr : dto.getLrs()) {
            Integer id = save(lr);
            relation = new EdsRelation(new RelationItem(null, entityID, formId, name, id, TYPE_LEAVE_REQUEST, null));
            relationManager.create(relation);
            ids.add(id);
        }
        return ids;
    }

    public Collection<LeaveRequestLisItem> getLeaveStats(String startDate, String endDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ListingFilterParameter fp = new ListingFilterParameter();
        int employeeID = employeeManager.getUser().getObjectID();
        fp.setEmployeeId(employeeID);
        try {
            fp.setStartDate(format.parse(startDate));
            fp.setEndDate(format.parse(endDate));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ListResult<LeaveRequestLisItem> leaveRequestList = availabilityService.getLeaveRequestList(fp);
        Map<String, LeaveRequestLisItem> map = new HashMap<>();
        for (LeaveRequestLisItem lr : leaveRequestList.getList()) {
            try {
                LeaveRequestLisItem value;
                if (map.containsKey(lr.getReasonCode())) {
                    value = map.get(lr.getReasonCode());
                    value.setLeaveDays(String.valueOf(Double.parseDouble(value.getLeaveDays()) + Double.parseDouble(lr.getLeaveDays())));
                } else {
                    value = new LeaveRequestLisItem();
                    value.setReason(lr.getReason());
                    value.setReasonCode(lr.getReasonCode());
                    value.setLeaveDays(lr.getLeaveDays());
                    Double.parseDouble(lr.getLeaveDays());
                }
                map.put(lr.getReasonCode(), value);
            } catch (NumberFormatException e) {
                //ignore
            }
        }
        for (String s : map.keySet()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fp.getStartDate());
            EdsAnnualLeaveAllowance leaveAllowanceByReason = annualLeaveAllowanceManager.getLeaveAllowanceByReason(calendar.get(Calendar.YEAR), employeeID, s, null);
            if (leaveAllowanceByReason == null || leaveAllowanceByReason.getAllowanceDays() == null) {
                continue;
            }
            LeaveRequestLisItem value = map.get(s);
            value.setPaid(String.valueOf(leaveAllowanceByReason.getAllowanceDays()));
            map.put(s, value);
        }
        return map.values();
    }

    public ListResultTO<LeaveRequestDTO> getSimpleLeaveRequests(ListingFilterParameter fp) {
        ListResult<LeaveRequestLisItem> leaveRequestItems = availabilityService.getLeaveRequestList(fp);
        EdsUser currentUser = userManager.getUser();
        Integer currentUserId = currentUser.getObjectID();
        ArrayList<LeaveRequestDTO> leaveRequestDto = leaveRequestItems.getList().stream()
                .map(item -> {
                    return ConvertUtils.toDto(item, currentUserId);
                })
                .collect(Collectors.toCollection(ArrayList::new));
        ListResultTO<LeaveRequestDTO> leaveRequests = new ListResultTO<>();
        leaveRequests.setTotalNumber(leaveRequestDto.size());
        leaveRequests.setItems(leaveRequestDto);
        return leaveRequests;
    }
}
