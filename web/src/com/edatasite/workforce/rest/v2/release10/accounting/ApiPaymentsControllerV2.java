package com.edatasite.workforce.rest.v2.release10.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.transactions.PaymentListItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anvar Akramov on 19/6/2020.
 */

@Tag(name = "Payments", description = "Payments API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiPaymentsControllerV2 extends BaseApiControllerV2 implements Constants {

    private static final Logger log = LoggerFactory.getLogger(ApiPaymentsControllerV2.class);
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    private HashMap<String, String> EXPENSE_STATUSES = new HashMap<>();

    @Operation(summary = "Get Payments List", description = "Retrieves list of Payments.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of payments"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/payments/list", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.HRMS_MAIN_MENU})
    public Object getPaymentsList(@Parameter(name = "type", description = """
            <pre> One of :

            0 - Receive Money = Bank Receipt

            1 - Spend Money = Bank Payment

            2 - Cash Receive = Cash Receipt

            3 - Cash Spend = Cash Payment</pre>""")
                                  @RequestParam(value = "type", required = false) Integer type,
                                  @RequestParam(value = "project_id", required = false) Integer projectId,
                                  @RequestParam(value = "employee_id", required = false) Integer employeeId,
                                  @RequestParam(value = "start_date", required = false) Date startDate,
                                  @RequestParam(value = "end_date", required = false) Date endDate,
                                  @RequestParam(value = "from_amount", required = false) Double fromAmount,
                                  @RequestParam(value = "to_amount", required = false) Double toAmount,
                                  @RequestParam(value = "query", required = false) String query,
                                  @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
                                  @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset, HttpServletRequest servletRequest) throws RestException {


        ListingFilterParameter filterParameter = new ListingFilterParameter();
        if (type != null) {
            filterParameter.setType(type);
        }
        filterParameter.setProjectId(projectId);
        filterParameter.setEmployeeId(employeeId);
        filterParameter.setStartDate(startDate);
        filterParameter.setEndDate(endDate);
        filterParameter.setFromAmount(fromAmount);
        filterParameter.setToAmount(toAmount);
        filterParameter.setSearchKey(query);
        filterParameter.setStart(offset);
        filterParameter.setLimit(limit);

        ListResult<NewManualTransaction> paymentsList;
        try {
            paymentsList = accountingServiceLocal.getBankCashTransferList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        List<PaymentListItemTO> resultList = new ArrayList<>();

        if (paymentsList.getList() != null && !paymentsList.getList().isEmpty()) {

            for (NewManualTransaction newManualTransaction : paymentsList.getList()) {

                PaymentListItemTO paymentListItemTO = new PaymentListItemTO();
                paymentListItemTO.setId(newManualTransaction.getObjectId());
                paymentListItemTO.setPaid_amount(newManualTransaction.getTotal());
//                "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
                paymentListItemTO.setPaid_date(newManualTransaction.getDate().getNonConvertedDate());

                if (newManualTransaction.getAccount() != null) {
                    paymentListItemTO.setAccount(new IdNameTO(newManualTransaction.getAccount().getId(), newManualTransaction.getAccount().getName()));
                }
                paymentListItemTO.setReference(newManualTransaction.getReference());
                paymentListItemTO.setType(newManualTransaction.getTransactionType());

                resultList.add(paymentListItemTO);
            }
        }

        return successResponse(new PagingListResultTO<PaymentListItemTO>(resultList, paymentsList.getTotal()));

    }


}
