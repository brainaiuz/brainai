package com.edatasite.workforce.rest.v2.release10.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.chartofaccount.AccountResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.AccountTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Chart of Account", description = "Account API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiChartOfAccountControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiChartOfAccountControllerV2.class);

    @Autowired
    private AccountingManager accountingManager;

    @Operation(summary = "Get Chart of Account List", description = "Retrieves list of accounts")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of chart od account."),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/chartofaccounts", method = RequestMethod.GET)
    public Object getAccountList() throws RestException {
        AccountResponseData accounts;
        try {
            accounts = objectToTransfer(accountingManager.getCompanyAccounts());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(accounts);
    }

    private AccountResponseData objectToTransfer(List<EdsAccount> accounts) {
        ArrayList<AccountTO> assets = new ArrayList<>();
        ArrayList<AccountTO> liabilities = new ArrayList<>();
        ArrayList<AccountTO> equity = new ArrayList<>();
        ArrayList<AccountTO> expenses = new ArrayList<>();
        ArrayList<AccountTO> revenue = new ArrayList<>();
        ArrayList<AccountTO> overhead = new ArrayList<>();
        for (EdsAccount ac : accounts) {
            String category = ac.getAccountType().getCategory();
            AccountTO accountTO = new AccountTO(ac.getObjectID(), ac.getAccountCode(), ac.getName());
            if (EdsAccountType.ASSETS.equals(category)) {
                assets.add(accountTO);
            } else if (EdsAccountType.LIABILITIES.equals(category) || category.equals(EdsAccountType.CURRENT_LIABILITY)) {
                liabilities.add(accountTO);
            } else if (EdsAccountType.EQUITY.equals(category)) {
                equity.add(accountTO);
            } else if (EdsAccountType.EXPENSES.equals(category)) {
                expenses.add(accountTO);
            } else if (EdsAccountType.REVENUE.equals(category)) {
                revenue.add(accountTO);
            } else if (EdsAccountType.OVERHEAD.equals(category)) {
                overhead.add(accountTO);
            }
        }
        AccountResponseData accountResponseData = new AccountResponseData();
        accountResponseData.setAssets(sortAccountItems(assets));
        accountResponseData.setLiabilities(sortAccountItems(liabilities));
        accountResponseData.setEquity(sortAccountItems(equity));
        accountResponseData.setExpenses(sortAccountItems(expenses));
        accountResponseData.setRevenue(sortAccountItems(revenue));
        accountResponseData.setOverhead(sortAccountItems(overhead));
        return accountResponseData;
    }

    private ArrayList<AccountTO> sortAccountItems(ArrayList<AccountTO> items) {
        items.sort((o1, o2) -> {
            if (o1.getAccount_code() == null) {
                return -1;
            }
            if (o2.getAccount_code() == null) {
                return 1;
            }
            return o1.getAccount_code().compareTo(o2.getAccount_code());
        });
        return items;
    }
}
