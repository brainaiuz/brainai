package com.edatasite.workforce.rest.v2.release10.payroll;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.PaymentMethodManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CashAdvanceManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverListStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApproverStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CustomStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.FromValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseItemsListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.StatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.TitleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.RequestActionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.leaverequest.RequestUserActionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.ApproversTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.CashAdvanceAddRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.CashAdvanceAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.CashAdvanceCategoriesTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.CashAdvanceDetailsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.CashAdvanceListItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.CashAdvanceListResultTO;
import com.edatasite.workforce.rest.v2.release10.enums.CreateTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RequestActionEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RequestStatusEnum;
import com.edatasite.workforce.rest.v2.release10.enums.TermsEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v2.release10.utils.ApiUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.multipart.MultipartRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 11/28/2017.
 */

@Tag(name = "Cash Advance", description = "Cash Advance API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCashAdvanceControllerV2 extends BaseApiControllerV2 {

    @Autowired
    private PayrollServiceLocal payrollServiceLocal;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private PaymentMethodManager paymentMethodManager;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private CashAdvanceManager cashAdvanceManager;
    @Autowired
    private PayrollCategoryManager payrollCategoryManager;
    @Autowired
    private EmployeeManager employeeManager;


    private static final Logger log = LoggerFactory.getLogger(ApiCashAdvanceControllerV2.class);

    @Operation(summary = "Get Cash Advance Categories", description = "Retrieves cash advance categories")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have category id, category name"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/payments/cash_advance/categories", method = RequestMethod.GET)
    public Object getCashAdvanceCategories() throws RestException {

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setAccountType(PayrollConstants.CATEGORY_DEDUCTION);
        filterParameter.setActive(true);
        filterParameter.setStart(0);
        filterParameter.setLimit(MAX_LIMIT);
        filterParameter.setCorporate(ServerUtils.isArabicCompany(userManager.getUser().getCompany()));

        PaymentDeductionSelectItem[] cashAdvanceCategories;
        try {
            cashAdvanceCategories = payrollServiceLocal.getCategoriesForLookUp(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<CategoryTO> cashAdvanceCategoryList = new ArrayList<>();

        if (cashAdvanceCategories != null) {
            for (PaymentDeductionSelectItem cashAdvanceCategory : cashAdvanceCategories) {
                cashAdvanceCategoryList.add(new CategoryTO(cashAdvanceCategory.getId(), cashAdvanceCategory.getName()));
            }
        }

        return successResponse(new CashAdvanceCategoriesTO(cashAdvanceCategoryList));
    }

    @Operation(summary = "Create Cash Advance", description = "Creates new cash advance with provided data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "400", description = "JSON body format is wrong"),
            @ApiResponse(responseCode = "422", description = "Invalid date format")
    })
    @RequestMapping(value = "/payments/cash_advance", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.PM_MAIN_MENU})
    public Object createCashAdvance(MultipartRequest multipartRequest,
                                    @RequestParam(name = "body") String jsonString) throws RestException {
        CashAdvanceAddTO cashAdvanceAdd;
        ObjectMapper mapper = new ObjectMapper();

        EdsUser user = userManager.get(employeeManager.getUser().getObjectID());

        if (user.getDeleted() || Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) {
            throw new RestException("Your account was disabled. Please contact your company admin.", "User is deleted/resigned.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        String currentUserStatus = userManager.getUserStatus(user.getObjectID());
        if (!Constants.EMPLOYEE_STATUS_ACTIVE.equals(currentUserStatus)) {
            throw new RestException("Please verify your registration from a confirmation email sent to you to proceed.", "User is not active.", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        try {
            cashAdvanceAdd = mapper.readValue(jsonString, CashAdvanceAddTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong. " + e.getMessage(), REQUIRED, HttpStatus.BAD_REQUEST);
        }

        CashAdvanceAddRequestTO cashAdvanceAddRequest = cashAdvanceAdd.getRequest() != null ? cashAdvanceAdd.getRequest() : new CashAdvanceAddRequestTO();

        String permission = cashAdvanceAddRequest.getId() == null ? PermissionConstants.PAYROLL_CASH_ADVANCE_ADD : PermissionConstants.PAYROLL_CASH_ADVANCE_EDIT;
        if (!ServerUtils.hasPermission(permission)) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        if (!CreateTypeEnum.DRAFT.name().equals(cashAdvanceAdd.getCreate_type()) && !CreateTypeEnum.FINAL.name().equals(cashAdvanceAdd.getCreate_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invalid create_type. It should be one of DRAFT,FINAL", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        EdsEmployee employee;
        if (cashAdvanceAddRequest.getRequester_id() != null && cashAdvanceAddRequest.getRequester_id() > 0) {
            employee = employeeManager.get(cashAdvanceAddRequest.getRequester_id());
            if (employee == null || !employee.isEmployee() || employee.getDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Requester with id " + cashAdvanceAddRequest.getRequester_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            if (cashAdvanceAddRequest.getId() == null ? !ServerUtils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_ADD) : !ServerUtils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_EDIT)) {
                throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } else {
            employee = employeeManager.get(user.getObjectID());
            if (employee == null || !employee.isEmployee()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, user.getFullName() + " is not employee.", INVALID, HttpStatus.BAD_REQUEST);
            }
        }
        Date date = null;

        //if cash advance create type is final, validate required fields
        if (CreateTypeEnum.FINAL.name().equals(cashAdvanceAdd.getCreate_type())) {
            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
            try {
                date = longDateTimezoneFormat.parse(cashAdvanceAddRequest.getDate());
            } catch (ParseException e) {
                log.error("", e);
                throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (ApiUtils.getTotal(cashAdvanceAddRequest.getRequested_amount()) == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Requested amount is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (cashAdvanceAddRequest.getRequested_amount().compareTo(BigDecimal.ZERO) < 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Requested amount cannot be lass then zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (ApiUtils.getTotal(cashAdvanceAddRequest.getPayment_amount()) == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Payment amount is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (cashAdvanceAddRequest.getPayment_amount().compareTo(BigDecimal.ZERO) < 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Payment amount cannot be lass then zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (!TermsEnum.PERCENTAGE.getId().equals(cashAdvanceAddRequest.getPayment_terms()) && !TermsEnum.FIXED.getId().equals(cashAdvanceAddRequest.getPayment_terms())) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Payment terms id should be one of 0, 1", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            BigDecimal paymentAmount;
            if (TermsEnum.PERCENTAGE.getId().equals(cashAdvanceAddRequest.getPayment_terms())) {
                double percent = cashAdvanceAddRequest.getPayment_amount().doubleValue();
                paymentAmount = cashAdvanceAddRequest.getRequested_amount().multiply(BigDecimal.valueOf(percent)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

                if (paymentAmount.compareTo(cashAdvanceAddRequest.getRequested_amount()) > 0) {
                    StringBuilder message = new StringBuilder("Payment amount cannot be more than requested amount. You've chosen percentage payment terms, which means payment amount specified is in percentages ");
                    message.append("(").append(cashAdvanceAddRequest.getPayment_amount()).append(" % from ").append(cashAdvanceAddRequest.getRequested_amount()).append(")");
                    throw new RestException(message.toString(), message.toString(), CONFLICT, HttpStatus.CONFLICT);
                }
            } else {
                paymentAmount = cashAdvanceAddRequest.getPayment_amount();
                if (paymentAmount.compareTo(cashAdvanceAddRequest.getRequested_amount()) > 0) {
                    throw new RestException("Payment amount cannot be more than requested amount.", "Payment amount cannot be more than requested amount.", CONFLICT, HttpStatus.CONFLICT);
                }
            }

            if (cashAdvanceAddRequest.getApprovers() == null || cashAdvanceAddRequest.getApprovers().size() == 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Cash advance approvers are required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            ApprovalListResult cashAdvanceApprovers = allInOneServiceLocal.getApprovers(RelationItem.TYPE_CASH_ADVANCE, null, false, null, false);
            if (cashAdvanceAddRequest.getApprovers().size() != cashAdvanceApprovers.getList().size()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Cash advance approvers count should be " + cashAdvanceApprovers.getList().size(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (date == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Cash advance date is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (cashAdvanceAddRequest.getCategory() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Cash advance category is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        }

        cashAdvanceAddRequest.setRequested_amount(cashAdvanceAddRequest.getRequested_amount() != null ? cashAdvanceAddRequest.getRequested_amount() : BigDecimal.ZERO);
        cashAdvanceAddRequest.setPayment_amount(cashAdvanceAddRequest.getPayment_amount() != null ? cashAdvanceAddRequest.getPayment_amount() : BigDecimal.ZERO);

        CashAdvanceItem item = new CashAdvanceItem();
        if (cashAdvanceAddRequest.getId() == null) {//ADD
            item.setCreationDate(new DateNonConvertable(new Date()));
            BankTransferNumberData numberData = payrollServiceLocal.generateCashAdvanceNumberFormat();
            item.setBankTransferNumberData(numberData);
            item.setNumber(numberData.getTransferNumber());
            item.setIntNumber(Integer.parseInt(numberData.getFourDigitNumber()));

            SelectItem status = new SelectItem();
            if (CreateTypeEnum.DRAFT.name().equals(cashAdvanceAdd.getCreate_type())) {
                status.setCode(Constants.DRAFT);
            } else {
                status.setCode(Constants.SUBMITTED_TO_MANAGER);
            }
            item.setStatus(status);
            if (cashAdvanceAddRequest.getApprovers() != null && cashAdvanceAddRequest.getApprovers().size() > 0) {
                item.setApprovers(getChosenApprovers(cashAdvanceAddRequest.getApprovers(), getAllAvailableApprovers(RelationItem.TYPE_CASH_ADVANCE)));
            }
        } else {//EDIT
            EdsCashAdvance edsCashAdvance = cashAdvanceManager.get(cashAdvanceAddRequest.getId());
            if (edsCashAdvance == null || edsCashAdvance.getDeleted()) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Cash advance with id " + cashAdvanceAddRequest.getId() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            item = edsCashAdvance.getRPC();
            if (Constants.DRAFT.equals(edsCashAdvance.getStatus().getCode())) {//update cash advance approvers when the cash advance status is draft
                if (cashAdvanceAddRequest.getApprovers() != null && cashAdvanceAddRequest.getApprovers().size() > 0) {
                    item.setApprovers(getChosenApprovers(cashAdvanceAddRequest.getApprovers(), getAllAvailableApprovers(RelationItem.TYPE_CASH_ADVANCE)));
                }
            }
            if (CreateTypeEnum.FINAL.name().equals(cashAdvanceAdd.getCreate_type()) && Constants.DRAFT.equals(edsCashAdvance.getStatus().getCode())) {
                item.setStatus(new SelectItem(Constants.SUBMITTED_TO_MANAGER));
            }
        }

        item.setEmployee(employee.getAsSelectItem());
        item.setType("Loan");
        item.setPurpose(cashAdvanceAddRequest.getPurpose());
        item.setReference(cashAdvanceAddRequest.getReference());
        if (cashAdvanceAddRequest.getDate() != null) {
            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
            try {
                date = longDateTimezoneFormat.parse(cashAdvanceAddRequest.getDate());
                item.setDate(new DateNonConvertable(date));
            } catch (ParseException e) {
                log.error("", e);
                if (CreateTypeEnum.FINAL.name().equals(cashAdvanceAdd.getCreate_type())) {
                    throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
        }

        Integer employeeCurrencyId = employee.getSalaryCurrency() != null ? employee.getSalaryCurrency().getObjectID() : currencyServiceLocal.getBaseCurrency().getId();
        CurrencyItem baseCurrency = currencyServiceLocal.getBaseCurrency();
        CurrencyItem currencyItem = currencyServiceLocal.getCurrency(employeeCurrencyId);
        currencyItem = currencyItem != null ? currencyItem : baseCurrency;
        item.setCurrency(currencyItem);
        if (baseCurrency.getId().equals(currencyItem.getId())) {
            item.setExchangeRate(BigDecimal.ONE);
            item.setTotalInBaseAmount(cashAdvanceAddRequest.getRequested_amount());
        } else {
            Double exchangeRate = currencyServiceLocal.getCurrencyRateByDate(currencyItem.getId(), date != null ? new DateNonConvertable(date) : new DateNonConvertable(new Date())).getExchangeRate();
            item.setExchangeRate(BigDecimal.valueOf(exchangeRate));
            item.setTotalInBaseAmount(cashAdvanceAddRequest.getRequested_amount().divide(BigDecimal.valueOf(exchangeRate), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
        }

        if (cashAdvanceAddRequest.getPayment_terms() != null) {
            if (!TermsEnum.PERCENTAGE.getId().equals(cashAdvanceAddRequest.getPayment_terms()) && !TermsEnum.FIXED.getId().equals(cashAdvanceAddRequest.getPayment_terms())) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Payment terms id should be one of 0, 1", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
        }
        if (cashAdvanceAddRequest.getPayment_method() != null && cashAdvanceAddRequest.getPayment_method() > 0) {
            EdsPaymentMethod paymentMethod = paymentMethodManager.get(cashAdvanceAddRequest.getPayment_method());
            if (paymentMethod != null && !paymentMethod.getDeleted()) {
                item.setPaymentMethod(paymentMethod.getAsSelectItem());
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Payment method with id " + cashAdvanceAddRequest.getPayment_method() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        }
        //if cash advance create type is draft, skip validation
        BigDecimal paymentAmount;
        if (TermsEnum.PERCENTAGE.getId().equals(cashAdvanceAddRequest.getPayment_terms())) {
            double percent = cashAdvanceAddRequest.getPayment_amount().doubleValue();
            item.setPercent(percent);
            paymentAmount = cashAdvanceAddRequest.getRequested_amount().multiply(BigDecimal.valueOf(percent)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
        } else {
            paymentAmount = cashAdvanceAddRequest.getPayment_amount();
            item.setPercent(null);
        }
        if (!CreateTypeEnum.DRAFT.name().equals(cashAdvanceAdd.getCreate_type()) && paymentAmount.compareTo(cashAdvanceAddRequest.getRequested_amount()) > 0) {
            throw new RestException("Payment amount cannot be more than requested amount", "Payment amount cannot be more than requested amount", CONFLICT, HttpStatus.CONFLICT);

        }
        item.setPaymentAmount(paymentAmount);
        item.setTotalAmount(cashAdvanceAddRequest.getRequested_amount());

        if (cashAdvanceAddRequest.getCategory() != null && cashAdvanceAddRequest.getCategory() > 0) {
            EdsPayrollCategory category = payrollCategoryManager.get(cashAdvanceAddRequest.getCategory());
            if (category != null && !category.getDeleted()) {
                PaymentDeductionSelectItem categoryItem = new PaymentDeductionSelectItem();
                categoryItem.setId(category.getObjectID());
                item.setCategoryItem(categoryItem);
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Category with id " + cashAdvanceAddRequest.getCategory() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        }

        TestRPC result;
        try {
            result = payrollServiceLocal.saveCashAdvance(item);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (result != null && result.getId() != null && result.getId() > 0) {
            //if request is draft, there will not be draft attachments, but there may be files. If there are files,they should be uploaded
            if (cashAdvanceAddRequest.getId() == null) {
                uploadCashAdvanceFiles(result.getId(), multipartRequest, null);
            } else {
                //Compare draft files to old files by unique keys: filename & file size. If there is a difference between them by name or size, delete the differ old files
                // but keep other non changed files
                ArrayList<FileResource> oldAttachments = documentsServiceLocal.getFileResources(Constants.F_CASH_ADVANCE, cashAdvanceAddRequest.getId(), cashAdvanceAddRequest.getId());
                HashSet<Integer> deleteIDs = new HashSet<>();
                //if draft attachments are empty, remove all old cash advance attachments.
                if (cashAdvanceAddRequest.getDraft_attachments() == null || cashAdvanceAddRequest.getDraft_attachments().size() == 0) {
                    if (oldAttachments != null && oldAttachments.size() > 0) {
                        List<Integer> oldAttachmentIDs = new ArrayList<>();
                        for (FileResource fileResource : oldAttachments) {
                            oldAttachmentIDs.add(fileResource.getObjectId());
                        }
                        try {
                            documentsServiceLocal.deleteFiles(oldAttachmentIDs);
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            log.error("", e);
                        }
                    }
                    //if draft attachments do not match with cash advance old attachments by filename and file size, delete not matched old attachments
                } else if (cashAdvanceAddRequest.getDraft_attachments() != null && cashAdvanceAddRequest.getDraft_attachments().size() > 0) {
                    if (oldAttachments != null && oldAttachments.size() > 0) {
                        LinkedHashMap<String, String> draftAttachmentMap = new LinkedHashMap<>();
                        for (AttachmentTO draftAttachment : cashAdvanceAddRequest.getDraft_attachments()) {
                            draftAttachmentMap.put(draftAttachment.getFile_name(), draftAttachment.getFile_name());
                        }
                        for (FileResource oldAttachment : oldAttachments) {
                            String draftFilename = draftAttachmentMap.get(oldAttachment.getFileName());
                            if (StringUtils.isNotBlank(draftFilename)) {
                                FileResource fileResource = documentsServiceLocal.getFileResourceByFileTypeAndName(Constants.F_CASH_ADVANCE, draftFilename);
                                if (fileResource != null && !fileResource.getContentLength().equals(oldAttachment.getContentLength())) {
                                    deleteIDs.add(oldAttachment.getObjectId());
                                }
                            } else {
                                deleteIDs.add(oldAttachment.getObjectId());
                            }
                        }
                        if (deleteIDs.size() > 0) {
                            try {
                                documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                            } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                                log.error("", e);
                            }
                        }
                        //after delete old attachments, get not deleted attachment as old attachments
                        oldAttachments = documentsServiceLocal.getFileResources(Constants.F_CASH_ADVANCE, cashAdvanceAddRequest.getId(), cashAdvanceAddRequest.getId());
                    }
                }

                uploadCashAdvanceFiles(result.getId(), multipartRequest, oldAttachments);
            }
            return successResponse(new ResponseData());
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Cash advance has not been created.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void uploadCashAdvanceFiles(Integer cashAdvanceID, MultipartRequest multipartRequest, ArrayList<FileResource> oldAttachments) {
        try {
            if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
                //if old files are empty, upload new files
                if (oldAttachments == null || oldAttachments.size() == 0) {
                    for (MultipartFile file : multipartRequest.getFileMap().values()) {
                        try {
                            documentsServiceLocal.saveDocumentFile(file, null, Constants.F_CASH_ADVANCE, cashAdvanceID, "");
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                } else {//If old files aren't empty, merge old and new files
                    HashSet<Integer> deleteIDs = new HashSet<>();
                    LinkedHashMap<String, FileResource> oldFilesMap = new LinkedHashMap<>();
                    for (FileResource file : oldAttachments) {
                        oldFilesMap.put(file.getFileName(), file);
                    }

                    for (MultipartFile multipartFile : multipartRequest.getFileMap().values()) {
                        FileResource oldFile = oldFilesMap.get(multipartFile.getOriginalFilename());
                        if (oldFile != null) {
                            deleteIDs.add(oldFile.getObjectId());
                        }
                    }

                    if (deleteIDs.size() > 0) {
                        try {
                            documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            log.error("", e);
                        }
                    }
                    for (MultipartFile file : multipartRequest.getFileMap().values()) {
                        try {
                            documentsServiceLocal.saveDocumentFile(file, null, Constants.F_CASH_ADVANCE, cashAdvanceID, "");
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Operation(summary = "Get Cash Advance List or Details", description = "Retrieves list of cash advance list if year is provided. \n" +
            "If request_id is provided, related cash advance details will be returned")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of cash advances or cash advance details")})
    @RequestMapping(value = "/payments/cash_advance", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.PAYROLL_MAIN_MENU})
    public Object getCashAdvanceListOrDetailMethod(@RequestParam(value = "request_id", required = false) Integer request_id,
                                                   @RequestParam(value = "year", required = false) Integer year) throws RestException {
        if (request_id != null) {
            if (ServerUtils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_VIEW)) {
                return getCashAdvanceDetails(request_id);
            } else {
                throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        } else if (year != null) {
            if (ServerUtils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_LIST)) {
                return getCashAdvanceList(year);
            } else {
                throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
            }
        }

        throw new RestException(GENERAL_ERROR_MESSAGE, "One of param request_id or year should be provided", REQUIRED, HttpStatus.BAD_REQUEST);
    }


    @Operation(summary = "Cash Advance Action", description = "Approves or rejects the cash advance accordingly")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code"),
            @ApiResponse(responseCode = "400", description = "request_id and request_action are required"),
            @ApiResponse(responseCode = "400", description = "cash advance with provided request_id is not found")})
    @RequestMapping(value = "/payments/cash_advance", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @CheckPermission(permissions = {PermissionConstants.PM_MAIN_MENU})
    public Object cashAdvanceAction(@RequestBody RequestActionTO cashAdvanceAction) throws RestException {
        if (cashAdvanceAction.getRequest_id() == null || cashAdvanceAction.getRequest_id() <= 0) {
            throw new RestException(ERROR_MESSAGE, "request_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(cashAdvanceAction.getAction())) {
            throw new RestException(ERROR_MESSAGE, "Request action is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        String action = getRequestAction(cashAdvanceAction.getAction());
        if (StringUtils.isBlank(action)) {
            throw new RestException(ERROR_MESSAGE, "Request action should be one of APPROVE, REJECT, APPROVE_FOR_ALL", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(cashAdvanceAction.getRequest_id());

        CashAdvanceItem cashAdvanceItem = payrollServiceLocal.getCashAdvancedItem(filterParameter);

        if (cashAdvanceItem == null || cashAdvanceItem.getObjectID() == null) {
            throw new RestException("Cash advance is not found.", "Cash advance with id " + cashAdvanceAction.getRequest_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        String status = null;
        RequestUserActionTO userAction = getUserAction(cashAdvanceItem, userManager.getUser());
        if (RequestActionEnum.APPROVE.name().equals(action)) {
            if (userAction.isApprove()) {
                status = Constants.APPROVED;
            } else if (Constants.REJECTED.equals(cashAdvanceItem.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Rejected cash advance cannot be approved", CONFLICT, HttpStatus.CONFLICT);
            } else if (Constants.APPROVED.equals(cashAdvanceItem.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Cash advance has already been approved", CONFLICT, HttpStatus.CONFLICT);
            } else if (!userAction.isApprove()) {
                throw new RestException(ERROR_MESSAGE, "Cash Advance with " + cashAdvanceItem.getStatus().getCode() + " status cannot be approved", CONFLICT, HttpStatus.CONFLICT);
            }
        } else if (RequestActionEnum.REJECT.name().equals(action)) {
            if (userAction.isReject()) {
                status = Constants.REJECTED;
            } else if (Constants.REJECTED.equals(cashAdvanceItem.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Cash advance has already been rejected", CONFLICT, HttpStatus.CONFLICT);
            } else if (Constants.APPROVED.equals(cashAdvanceItem.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Approved cash advance cannot be rejected", CONFLICT, HttpStatus.CONFLICT);
            } else if (!userAction.isReject()) {
                throw new RestException(ERROR_MESSAGE, "Cash Advance with " + cashAdvanceItem.getStatus().getCode() + " status cannot be rejected", CONFLICT, HttpStatus.CONFLICT);
            }
        } else if (RequestActionEnum.APPROVE_FOR_ALL.name().equals(action)) {
            if (userAction.isApprove_for_all()) {
                status = Constants.APPROVED;
            } else if (Constants.APPROVED.equals(cashAdvanceItem.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Cash advance has already been approved", CONFLICT, HttpStatus.CONFLICT);
            } else if (Constants.REJECTED.equals(cashAdvanceItem.getOverallStatus().getCode())) {
                throw new RestException(ERROR_MESSAGE, "Rejected cash advance cannot be approved", CONFLICT, HttpStatus.CONFLICT);
            } else if (!userAction.isApprove()) {
                throw new RestException(ERROR_MESSAGE, "Cash Advance with " + cashAdvanceItem.getStatus().getCode() + " status cannot be approved", CONFLICT, HttpStatus.CONFLICT);
            }
        }

        cashAdvanceItem.setStatus(new SelectItem(status));
        cashAdvanceItem.setApproveForAll(RequestActionEnum.APPROVE_FOR_ALL.name().equals(action));

        try {
            payrollServiceLocal.saveCashAdvance(cashAdvanceItem);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return successResponse(new ResponseData());
    }

    private Object getCashAdvanceDetails(Integer request_id) throws RestException {
        if (request_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (request_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "request_id should be more then zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        EdsCashAdvance edsCashAdvance = cashAdvanceManager.get(request_id);
        if (edsCashAdvance == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Cash advance with " + request_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(request_id);

        CashAdvanceItem cashAdvanceItem;
        try {
            cashAdvanceItem = payrollServiceLocal.getCashAdvancedItem(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage() != null ? e.getMessage() : e.toString(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (cashAdvanceItem == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Cash advance with id " + request_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        Integer calculationScale = ServerUtils.getCalculationScale();

        CashAdvanceDetailsTO cashAdvanceDetails = new CashAdvanceDetailsTO();
        cashAdvanceDetails.setId(cashAdvanceItem.getObjectID());
        cashAdvanceDetails.setNumber(cashAdvanceItem.getNumber());
        if (cashAdvanceItem.getEmployee() != null && !userManager.getUser().getObjectID().equals(cashAdvanceItem.getEmployee().getId())) {
            EmployeeTO owner = new EmployeeTO();
            owner.setId(cashAdvanceItem.getEmployee().getId());
            owner.setName(cashAdvanceItem.getEmployee().getName());
            if (cashAdvanceItem.getEmployee().getName().contains("->")) {
                owner.setName(cashAdvanceItem.getEmployee().getName().split("->")[1].trim());
            }
            owner.setAvatar(hrmsServiceLocal.getEmployeeImageURL(cashAdvanceItem.getEmployee().getId()));
            EdsEmployee employee = (EdsEmployee) userManager.get(cashAdvanceItem.getEmployee().getId());
            if (employee != null && employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
                owner.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
            }
            cashAdvanceDetails.setOwner(owner);
        }
        cashAdvanceDetails.setStatus(getStatus(cashAdvanceItem.getStatus()));

        List<ApproverItemMini> edsApprovers = cashAdvanceItem.getApprovers();
        if (cashAdvanceItem.getStatus() != null && Constants.SUBMITTED_TO_MANAGER.equals(cashAdvanceItem.getStatus().getCode())) {
            if (edsApprovers != null && edsApprovers.size() > 0) {
                ApproverListStatusTO cashAdvanceStatus = new ApproverListStatusTO();
                if (edsApprovers.size() == 1) {
                    if (Constants.APPROVED.equals(edsApprovers.get(0).getStatus().getCode())) {
                        cashAdvanceStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                    } else if (Constants.REJECTED.equals(edsApprovers.get(0).getStatus().getCode())) {
                        cashAdvanceStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                    } else if (Constants.SUBMITTED_TO_MANAGER.equals(edsApprovers.get(0).getStatus().getCode())) {
                        cashAdvanceStatus.setType(RequestStatusEnum.PENDING.getStatus());
                    }
                } else {
                    //Means there are more than one approvers and we must set statuses based on them
                    FromValueTO dataTO = new FromValueTO();
                    dataTO.setFrom(edsApprovers.size());
                    dataTO.setValue(0);
                    for (ApproverItemMini approver : edsApprovers) {
                        if (Constants.APPROVED.equals(approver.getStatus().getCode())) {
                            dataTO.setValue(dataTO.getValue() + 1);
                        }
                    }
                    cashAdvanceStatus.setData(dataTO);
                    if (cashAdvanceItem.getStatus() != null && Constants.APPROVED.equals(cashAdvanceItem.getStatus().getCode())) {
                        cashAdvanceStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                    } else if (cashAdvanceItem.getStatus() != null && Constants.REJECTED.equals(cashAdvanceItem.getStatus().getCode())) {
                        cashAdvanceStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                    } else {
                        if (dataTO.getValue() == 0) {
                            cashAdvanceStatus.setType(RequestStatusEnum.PENDING.getStatus());
                        } else if (dataTO.getFrom().intValue() == dataTO.getValue().intValue()) {
                            cashAdvanceStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                        } else if (dataTO.getFrom() > dataTO.getValue() && dataTO.getValue() > 0) {
                            cashAdvanceStatus.setType(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
                        }
                    }
                }
                cashAdvanceDetails.setStatus(cashAdvanceStatus);
            }
        }

        if (cashAdvanceItem.getCategoryItem() != null) {
            cashAdvanceDetails.setCategory(new CategoryTO(cashAdvanceItem.getCategoryItem().getId(), cashAdvanceItem.getCategoryItem().getName()));
        }

        CurrencyItem baseCurrency = currencyServiceLocal.getBaseCurrency();
        String currencyCode = cashAdvanceItem.getCurrency() != null ? cashAdvanceItem.getCurrency().getName() : baseCurrency.getName();

        if (ApiUtils.getTotal(cashAdvanceItem.getTotalAmount()) != null) {
            cashAdvanceDetails.setRequested_amount(new CurrencyValueTO(cashAdvanceItem.getTotalAmount().setScale(calculationScale, RoundingMode.HALF_UP), currencyCode));
        }

        //If cash advance status is DARFT, payment amount should be calculated.
        if (cashAdvanceItem.getStatus() != null && Constants.DRAFT.equals(cashAdvanceItem.getStatus().getCode())) {
            if (cashAdvanceItem.getPercent() != null) {
                cashAdvanceDetails.setPayment_terms(new CategoryTO(TermsEnum.PERCENTAGE.getId(), TermsEnum.PERCENTAGE.getCode()));
                cashAdvanceDetails.setPayment_amount(new CurrencyValueTO(BigDecimal.valueOf(cashAdvanceItem.getPercent()), currencyCode));
            } else {
                cashAdvanceDetails.setPayment_terms(new CategoryTO(TermsEnum.FIXED.getId(), TermsEnum.FIXED.getCode()));
                if (ApiUtils.getTotal(cashAdvanceItem.getPaymentAmount()) != null) {
                    cashAdvanceDetails.setPayment_amount(new CurrencyValueTO(cashAdvanceItem.getPaymentAmount().setScale(calculationScale, RoundingMode.HALF_UP), currencyCode));
                }
            }
        } else {
            if (cashAdvanceItem.getPercent() != null) {
                cashAdvanceDetails.setPayment_terms(new CategoryTO(TermsEnum.PERCENTAGE.getId(), TermsEnum.PERCENTAGE.getCode()));
                if (ApiUtils.getTotal(cashAdvanceItem.getTotalAmount()) != null) {
                    BigDecimal calculateAmount = cashAdvanceItem.getTotalAmount().multiply(BigDecimal.valueOf(cashAdvanceItem.getPercent())).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    if (ApiUtils.getTotal(calculateAmount) != null) {
                        cashAdvanceDetails.setPayment_amount(new CurrencyValueTO(calculateAmount.setScale(calculationScale, RoundingMode.HALF_UP), currencyCode));
                    }
                }
            } else {
                cashAdvanceDetails.setPayment_terms(new CategoryTO(TermsEnum.FIXED.getId(), TermsEnum.FIXED.getCode()));
                if (ApiUtils.getTotal(cashAdvanceItem.getPaymentAmount()) != null) {
                    cashAdvanceDetails.setPayment_amount(new CurrencyValueTO(cashAdvanceItem.getPaymentAmount().setScale(calculationScale, RoundingMode.HALF_UP), currencyCode));
                }
            }
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        if (cashAdvanceItem.getDate() != null && cashAdvanceItem.getDate().getNonConvertedDate() != null) {
            cashAdvanceDetails.setDate(longDateTimezoneFormat.format(cashAdvanceItem.getDate().getNonConvertedDate()));
        }
        if (cashAdvanceItem.getPaymentMethod() != null && cashAdvanceItem.getPaymentMethod().getId() != null) {
            EdsPaymentMethod edsPaymentMethod = paymentMethodManager.get(cashAdvanceItem.getPaymentMethod().getId());
            if (edsPaymentMethod != null) {
                cashAdvanceDetails.setPayment_method(new CategoryTO(edsPaymentMethod.getObjectID(), edsPaymentMethod.getName()));
            }
        }
        if (cashAdvanceItem.getApprover() != null && cashAdvanceItem.getApprovers() != null) {
            ArrayList<ApproversTO> approvers = new ArrayList<>();
            for (ApproverItemMini approver : cashAdvanceItem.getApprovers()) {
                if (approver.getExactEmployee() != null) {
                    EdsUser employeeApprover = userManager.get(approver.getExactEmployee().getId());
                    if (employeeApprover != null) {
                        ApproversTO approversTO = new ApproversTO();
                        approversTO.setId(employeeApprover.getObjectID());
                        approversTO.setIndex(approver.getApproverOrder());
                        approversTO.setName(employeeApprover.getName());
                        if (employeeApprover.getPhoto() != null) {
                            approversTO.setAvatar(hrmsServiceLocal.getEmployeeImageURL(employeeApprover.getObjectID()));
                        }
                        EdsEmployee employee = employeeApprover.isEmployee() ? employeeApprover.getEmployee() : null;
                        if (employee != null && employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
                            approversTO.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
                        }
                        if (approver.getStatus() != null && approver.getStatus().getCode() != null) {
                            if (Constants.APPROVED.equals(approver.getStatus().getCode()) || Constants.PAID.equals(approver.getStatus().getCode())) {
                                approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.APPROVED.getStatus()));
                            } else if (Constants.REJECTED.equals(approver.getStatus().getCode())) {
                                approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.DECLINED.getStatus()));
                            } else {
                                approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.PENDING.getStatus()));
                            }
                        } else {
                            approversTO.setStatus(new ApproverStatusTO(RequestStatusEnum.PENDING.getStatus()));
                        }
                        approvers.add(approversTO);
                    }
                }
            }
            cashAdvanceDetails.setApprovers(approvers);
        }
        if (StringUtils.isNotBlank(cashAdvanceItem.getReference())) {
            cashAdvanceDetails.setReference(cashAdvanceItem.getReference());
        }
        if (StringUtils.isNotBlank(cashAdvanceItem.getPurpose())) {
            cashAdvanceDetails.setPurpose(cashAdvanceItem.getPurpose());
        }

        ArrayList<AttachmentTO> attachments = new ArrayList<>();
        ArrayList<FileResource> attachmentList = documentsServiceLocal.getFileResources(Constants.F_CASH_ADVANCE, cashAdvanceItem.getObjectID(), cashAdvanceItem.getObjectID());
        if (attachmentList != null && attachmentList.size() > 0) {
            for (FileResource fileItem : attachmentList) {
                attachments.add(new AttachmentTO(fileItem.getFileName(), fileItem.getDownloadUrl()));
            }
        }
        cashAdvanceDetails.setAttachments(attachments);

        cashAdvanceDetails.setUser_actions(getUserAction(cashAdvanceItem, userManager.getUser()));
        return successResponse(cashAdvanceDetails);
    }

    @Operation(summary = "Get Payment Methods", description = "Retrieves the payment methods of Cash Advance")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have id and title")})
    @RequestMapping(value = "/payments/cash_advance/payment_methods", method = RequestMethod.GET)
    public Object getCashAdvancePaymentMethods() throws RestException {
        List<EdsPaymentMethod> paymentMethodList;
        try {
            paymentMethodList = paymentMethodManager.list();
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<CategoryTO> paymentMethods = new ArrayList<>();
        if (paymentMethodList != null && paymentMethodList.size() > 0) {
            for (EdsPaymentMethod edsPaymentMethod : paymentMethodList) {
                paymentMethods.add(new CategoryTO(edsPaymentMethod.getObjectID(), edsPaymentMethod.getName()));
            }
        }
        return successResponse(new ResponseItemsListData<>(paymentMethods));
    }

    private Object getCashAdvanceList(Integer year) throws RestException {
        if (year == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "year field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();

        EdsUser user = userManager.getUser();
        if (!user.isEmployee()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, user.getFullName() + " is not employee.", INVALID, HttpStatus.BAD_REQUEST);
        }
        filterParameter.setEmployeeId(user.getObjectID());
        filterParameter.setAccessEnabled(true);
        filterParameter.setStartDate(ServerUtils.getYearStartDate(year));
        filterParameter.setEndDate(ServerUtils.getYearEndDate(year));
        filterParameter.setHRMS(true);
        filterParameter.setStart(0);
        filterParameter.setLimit(MAX_LIMIT);

        ListResult<CashAdvanceItem> cashAdvanceItemListResult;
        ArrayList<CashAdvanceListItemTO> cashAdvanceListResult = new ArrayList<>();
        try {
            cashAdvanceItemListResult = payrollServiceLocal.getCashAdvanceList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        String baseCurrency = currencyServiceLocal.getBaseCurrency().getName();

        Integer calculationScale = ServerUtils.getCalculationScale();

        if (cashAdvanceItemListResult.getList() != null && !cashAdvanceItemListResult.getList().isEmpty()) {
            for (CashAdvanceItem cashAdvanceItem : cashAdvanceItemListResult.getList()) {
                CashAdvanceListItemTO cashAdvanceListItem = new CashAdvanceListItemTO();
                cashAdvanceListItem.setId(cashAdvanceItem.getObjectID());
                cashAdvanceListItem.setStatus(getStatus(cashAdvanceItem.getStatus()));

                List<ApproverItemMini> approvers = cashAdvanceItem.getApprovers();
                if (cashAdvanceItem.getStatus() != null && Constants.SUBMITTED_TO_MANAGER.equals(cashAdvanceItem.getStatus().getCode())) {
                    if (approvers != null && approvers.size() > 0) {
                        ApproverListStatusTO cashAdvanceStatus = new ApproverListStatusTO();
                        if (approvers.size() == 1) {
                            if (Constants.APPROVED.equals(approvers.get(0).getStatus().getCode())) {
                                cashAdvanceStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                            } else if (Constants.REJECTED.equals(approvers.get(0).getStatus().getCode())) {
                                cashAdvanceStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                            } else if (Constants.SUBMITTED_TO_MANAGER.equals(approvers.get(0).getStatus().getCode())) {
                                cashAdvanceStatus.setType(RequestStatusEnum.PENDING.getStatus());
                            }
                        } else {
                            //Means there are more than one approvers and we must set statuses based on them
                            FromValueTO dataTO = new FromValueTO();
                            dataTO.setFrom(approvers.size());
                            dataTO.setValue(0);
                            for (ApproverItemMini approver : approvers) {
                                if (Constants.APPROVED.equals(approver.getStatus().getCode())) {
                                    dataTO.setValue(dataTO.getValue() + 1);
                                }
                            }
                            cashAdvanceStatus.setData(dataTO);
                            if (cashAdvanceItem.getStatus() != null && Constants.APPROVED.equals(cashAdvanceItem.getStatus().getCode())) {
                                cashAdvanceStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                            } else if (cashAdvanceItem.getStatus() != null && Constants.REJECTED.equals(cashAdvanceItem.getStatus().getCode())) {
                                cashAdvanceStatus.setType(RequestStatusEnum.DECLINED.getStatus());
                            } else {
                                if (dataTO.getValue() == 0) {
                                    cashAdvanceStatus.setType(RequestStatusEnum.PENDING.getStatus());
                                } else if (dataTO.getFrom().intValue() == dataTO.getValue().intValue()) {
                                    cashAdvanceStatus.setType(RequestStatusEnum.APPROVED.getStatus());
                                } else if (dataTO.getFrom() > dataTO.getValue() && dataTO.getValue() > 0) {
                                    cashAdvanceStatus.setType(RequestStatusEnum.PARTIALLY_APPROVED.getStatus());
                                }
                            }
                        }
                        cashAdvanceListItem.setStatus(cashAdvanceStatus);
                    }
                }
                if (cashAdvanceItem.getApprover() != null) {
                    cashAdvanceListItem.setApprover(cashAdvanceItem.getApprover().getName());
                    if (cashAdvanceItem.getApprover().getName().contains("-")) {
                        cashAdvanceListItem.setApprover(cashAdvanceItem.getApprover().getName().split("-")[1].trim());
                    }
                }
                String currencyCode = cashAdvanceItem.getCurrency() != null ? cashAdvanceItem.getCurrency().getName() : baseCurrency;
                if (ApiUtils.getTotal(cashAdvanceItem.getTotalAmount()) != null) {
                    cashAdvanceListItem.setRequested_amount(new CurrencyValueTO(cashAdvanceItem.getTotalAmount().setScale(calculationScale, RoundingMode.HALF_UP), currencyCode));
                }
                if (ApiUtils.getTotal(cashAdvanceItem.getRemainingAmount()) != null) {
                    cashAdvanceListItem.setRemaining_amount(new CurrencyValueTO(cashAdvanceItem.getRemainingAmount().setScale(calculationScale, RoundingMode.HALF_UP), currencyCode));
                }
                cashAdvanceListResult.add(cashAdvanceListItem);
            }
        }

        return successResponse(new CashAdvanceListResultTO(cashAdvanceListResult));
    }

    private RequestUserActionTO getUserAction(CashAdvanceItem cashAdvanceAction, EdsUser user) {
        boolean hasPermission = ServerUtils.hasPermission(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP);
        RequestUserActionTO userAction = new RequestUserActionTO();

        if (hasPermission && Constants.SUBMITTED_TO_MANAGER.equals(cashAdvanceAction.getStatus().getCode())) {
            userAction.setApprove_for_all(true);
            userAction.setApprove(true);
            userAction.setReject(true);
            return userAction;
        }
        if (!Constants.SUBMITTED_TO_MANAGER.equals(cashAdvanceAction.getStatus().getCode())) {
            userAction.setApprove_for_all(false);
            userAction.setApprove(false);
            userAction.setReject(false);
            return userAction;
        }
        if (cashAdvanceAction.getOverallStatus() != null && !Constants.SUBMITTED_TO_MANAGER.equals(cashAdvanceAction.getOverallStatus().getCode())) {
            userAction.setApprove_for_all(false);
            userAction.setApprove(false);
            userAction.setReject(false);
            return userAction;
        }
        //if current user is one of the approvers
        if (cashAdvanceAction.getApprover() != null && cashAdvanceAction.getApprover().getId() != null && user.getObjectID().equals(cashAdvanceAction.getApprover().getId())) {
            userAction.setApprove_for_all(false);
            userAction.setApprove(true);
            userAction.setReject(true);
            return userAction;
        }
        //if current user is not one of the approvers but the user has permission
        if (cashAdvanceAction.getApprover() != null && cashAdvanceAction.getApprover().getId() != null && !user.getObjectID().equals(cashAdvanceAction.getApprover().getId())) {
            userAction.setApprove_for_all(hasPermission);
            userAction.setApprove(hasPermission);
            userAction.setReject(hasPermission);
            return userAction;
        }

        return userAction;
    }

    public static Object getStatus(SelectItem status) {
        if (status == null || StringUtils.isBlank(status.getCode())) {
            return null;
        }
        return switch (status.getCode()) {
            case Constants.APPROVED -> new StatusTO(RequestStatusEnum.APPROVED.getStatus());
            case Constants.SUBMITTED_TO_MANAGER -> new StatusTO(RequestStatusEnum.PENDING.getStatus());
            case Constants.REJECTED -> new StatusTO(RequestStatusEnum.DECLINED.getStatus());
            case Constants.DRAFT -> new StatusTO(RequestStatusEnum.DRAFT.getStatus());
            case Constants.PAID -> new StatusTO(RequestStatusEnum.PAID.getStatus());
            case Constants.POSTED, Constants.PARTIALLY_PAID -> new CustomStatusTO(RequestStatusEnum.CUSTOM.getStatus(), new TitleTO(status.getName()));
            default -> new CustomStatusTO(RequestStatusEnum.CUSTOM.getStatus(), new TitleTO(status.getName()));
        };
    }

}
