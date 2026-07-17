package com.edatasite.workforce.rest.v1.release10.payroll;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.PaymentMethodManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.rest.base.enums.ApiActionEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.base.to.SinglePayrunTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dilsh0d Madrahimov on 06.02.2017.
 */
@Tag(name = "Payroll", description = "Payroll API")
@RestController
@RequestMapping(value = "/payroll", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiPayrollControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private PayrollServiceLocal payrollServiceLocal;
    @Autowired
    private PayslipTableItemManager payslipTableItemManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private PaymentMethodManager paymentMethodManager;


    @RequestMapping(value = "/singlePayrun/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getSinglePayrunList(@RequestBody MListingFilterParameter filterParameter) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsCurrency baseCurrency = financialSettings.getCurrency();
        Integer calculationScale = financialSettings.getCalculationScale() != null ? financialSettings.getCalculationScale() : 2;
        if (filterParameter == null) {
            filterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter fp = filterParameter.convertToFilterParameters();
        fp.setSortDir(Constants.DESC);
        ArrayList<SinglePayrunTO> result = new ArrayList<>();
        List<EdsPayslipTableItem> singlePayrunList = payslipTableItemManager.getPayslipTableItemList(fp);
        Integer singlePayrunListTotal = payslipTableItemManager.getPayslipTableItemListTotal(fp);
        for (EdsPayslipTableItem item : singlePayrunList) {
            SinglePayrunTO payrunTO = new SinglePayrunTO(item, calculationScale);
            payrunTO.setBaseCurrency(new SelectItemTO(baseCurrency.getObjectID(), baseCurrency.getName(), baseCurrency.getSymbol(), ""));
            result.add(payrunTO);
        }
        return successResponse(new ListResultTO<>(singlePayrunListTotal, result));
    }


    @RequestMapping(value = "/singlePayrun/{id}", method = RequestMethod.GET)
    public Object getSinglePayrun(@PathVariable(value = "id") Integer id) {
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsCurrency baseCurrency = financialSettings.getCurrency();
        Integer calculationScale = financialSettings.getCalculationScale() != null ? financialSettings.getCalculationScale() : 2;
        PayslipItemFilter filter = new PayslipItemFilter();
        filter.setObjectID(id);
        filter.setFromView(true);
        SinglePayrunItem singlePayrunItem = payrollServiceLocal.getSinglePayrunData(filter);
        SinglePayrunTO payrunTO = new SinglePayrunTO(singlePayrunItem, calculationScale);
        if (payrunTO.getStatus() != null && Constants.PAYRUN_STATUS_SUBMITTED.equals(payrunTO.getStatus().getCode()) &&
                ServerUtils.hasPermission(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP)) {
            payrunTO.setCanApprove(true);
        } else if (payrunTO.getStatus() != null && !Constants.PAYRUN_STATUS_APPROVED.equals(payrunTO.getStatus().getCode()) && ServerUtils.hasPermission(PermissionConstants.PAYROLL_CAN_APPROVE_PAYSLIP) &&
                payrunTO.getApprover() != null && payrunTO.getApprover().getId().equals(financialSettingsManager.getUser().getObjectID())) {
            payrunTO.setCanApprove(true);
        }
        if (payrunTO.getStatus() != null && Constants.PAYRUN_STATUS_DRAFT.equals(payrunTO.getStatus().getCode()) && payrunTO.getApprover() != null && !payrunTO.getApprover().getId().equals(financialSettingsManager.getUser().getObjectID())) {
            payrunTO.setCanSubmit(true);
        }

        payrunTO.setBaseCurrency(new SelectItemTO(baseCurrency.getObjectID(), baseCurrency.getName(), baseCurrency.getSymbol(), ""));
        return successResponse(payrunTO);
    }

    @RequestMapping(value = "/singlePayrun/status", method = RequestMethod.GET)
    public Object getStatus() {
        return successResponse(WrapUtils.wrapSelectItemTOs(referenceManager.listReferences(Constants.PAYRUN_STATUS)));
    }

    @RequestMapping(value = "/singlePayrun/paymentMethod", method = RequestMethod.GET)
    public Object getPaymentMethod() {
        List<EdsPaymentMethod> paymentMethods = paymentMethodManager.list();
        ArrayList<SelectItemTO> result = new ArrayList<>();
        for (EdsPaymentMethod paymentMethod : paymentMethods) {
            result.add(new SelectItemTO(paymentMethod.getObjectID(), paymentMethod.getName(), paymentMethod.getCode(), paymentMethod.getDescription()));
        }
        return successResponse(result);
    }

    @RequestMapping(value = "/singlePayrun/{id}/{actionType}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object doAction(@PathVariable(value = "id") Integer id,
                           @PathVariable(value = "actionType") String actionType,
                           @QueryParam(value = "isSendNotification") Boolean isSendNotification) {
        ApiActionEnum actionTypeEnum = getActionType(actionType);
        if (actionTypeEnum == null) {
            return this.errorResponse("Action type should be approve or submit");
        }
        PayslipItemFilter filter = new PayslipItemFilter();
        filter.setObjectID(id);
        filter.setFromView(true);
        SinglePayrunItem data = payrollServiceLocal.getSinglePayrunData(filter);
        if (ApiActionEnum.APPROVE.code.equalsIgnoreCase(actionType.toUpperCase())) {
            data.setStatus(Constants.PAYRUN_STATUS_APPROVED);
            data.setSendNotification(isSendNotification);
            data.setApprovedDate(new DateNonConvertable(new Date()));
            try {
                payrollServiceLocal.approveSinglePayrun(data);
                return successResponse(SUCCESS_SAVE);
            } catch (Exception e) {
                e.printStackTrace();
                return errorResponse(ERROR_FAILED_SAVE);
            }
        }

        if (ApiActionEnum.SUBMIT.code.equalsIgnoreCase(actionType.toUpperCase())) {
            if (data.getApprover() != null && data.getApprover().getId().equals(referenceManager.getUser().getObjectID())) {
                data.setStatus(Constants.PAYRUN_STATUS_APPROVED);
            } else {
                data.setStatus(Constants.PAYRUN_STATUS_SUBMITTED);
            }
            try {
                payrollServiceLocal.saveSinglePayrun(data);
                return successResponse(SUCCESS_SAVE);
            } catch (Exception e) {
                e.printStackTrace();
                return errorResponse(ERROR_FAILED_SAVE);
            }
        }
        return errorResponse(ERROR_RESOURCE_NOT_FOUND);
    }


}
