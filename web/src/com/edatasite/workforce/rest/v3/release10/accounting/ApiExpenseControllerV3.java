package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ExpenseDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ExpenseItemDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ReceiveReaderDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ReceiveReaderItemDto;
import com.edatasite.workforce.rest.v3.release10.accounting.service.ApiExpenseService;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finnetlimited.reportservice.core.client.ui.Constants;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.INVALID;

@Tag(name = "Expense", description = "Collection of public APIs for Expense")
@RestController
@RequestMapping(value = "/expense", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiExpenseControllerV3 implements Constants {

    @Autowired
    ApiExpenseService apiExpenseService;
    @Autowired
    ExpenseServiceLocal expenseServiceLocal;

    @Operation(summary = "Get expense list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Expense"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})

    public ResultTO<ListResultTO<ExpenseDto>> getExpenses(@RequestBody ListParamsDTO params,
                                                          @RequestParam(value = "simple", required = false) Boolean simple){

        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.ExpenceReportListPanel);
        ListResultTO<ExpenseDto> expenseList;
        if (simple != null && simple) {
            expenseList = apiExpenseService.getSimpleExpenseList(fp);
        } else {
            expenseList = apiExpenseService.getExpenseList(fp);
        }
        return ResultTO.success(expenseList);
    }

    @Operation(summary = "Get existing expense by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Expense"))
    @RequestMapping(path = "/{expenseId}", method = RequestMethod.GET)
    public ResultTO<ExpenseDto> getExpenseById(@PathVariable final Integer expenseId) throws RestException {
        return ResultTO.success(apiExpenseService.getById(expenseId));
    }

    @Operation(summary = "Create new Expense")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Expense"))
    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ExpenseDto> createExpense(@Validated @RequestBody ExpenseDto expenseDto) throws RestException {
        if (expenseDto.getId() != null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Expense ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiExpenseService.save(expenseDto);
        return ResultTO.success(expenseDto);
    }

    @Operation(summary = "Put update Expense", description = "Update existing Expense", tags = {"Expense"})
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Expense"))
    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ExpenseDto> putUpdateExpense(@RequestBody ExpenseDto expenseDto) throws RestException {
        if (expenseDto == null || expenseDto.getId() < 1) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Fixed Asset DTO is not specified", INVALID, HttpStatus.BAD_REQUEST);
        }
        apiExpenseService.update(expenseDto);
        return ResultTO.success(expenseDto);
    }

    @Operation(summary = "Patch Update existing expense")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Expense"))
    @RequestMapping(method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<ExpenseDto> updateExpense(@Validated @RequestBody ExpenseDto expenseDto) throws RestException {
        if (expenseDto.getId() == null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Expense ID is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiExpenseService.save(expenseDto);
        return ResultTO.success(expenseDto);
    }

    @Operation(summary = "Delete existing expense by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Expense"))
    @RequestMapping(value = "/{expenseId}", method = RequestMethod.DELETE)
    public Object deleteExpense(@PathVariable final Integer expenseId) {
        expenseServiceLocal.deleteExpenseReport(expenseId);
        return ResultTO.success();
    }

    @Operation(summary = "Upload Expenses", description = "Receives and processes expense data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Expenses successfully uploaded"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @RequestMapping(
            path = "/receive-reader",
            method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    @Transactional(propagation = Propagation.REQUIRED)
    public ResultTO<ExpenseDto> receiveExpenses(
            @RequestParam(value = "file") MultipartFile multipartFile) throws RestException {
        try {
            String url = "http://3.209.242.234:4000/process-image/";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(MediaType.MULTIPART_FORM_DATA_VALUE));

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image_file", multipartFile.getResource());
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ObjectMapper mapper = new ObjectMapper();
                ReceiveReaderDto receiveReaderDto = mapper.readValue(response.getBody(), ReceiveReaderDto.class);

                ExpenseDto expenseDto = getExpenseDto(receiveReaderDto);
                Integer reportId = apiExpenseService.save(expenseDto);
                expenseDto.setId(reportId);
                return ResultTO.success(expenseDto);
            } else {
                throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE,
                        "Invalid response from API: " + response.getStatusCode(),
                        ApiConstants.INVALID,
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE,
                    "Failed to process expenses", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
    }

    private static ExpenseDto getExpenseDto(ReceiveReaderDto receiveReaderDto) {
        ExpenseDto expenseDto = new ExpenseDto();
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        expenseDto.setEmployee(new IdName(user.getObjectID(), user.getName()));
        expenseDto.setReportTitle(receiveReaderDto.getVendor());
        expenseDto.setDate(new Date());
        List<ExpenseItemDto> expenseItems = getExpenseItemDtos(receiveReaderDto);
        expenseDto.setItems(expenseItems);
        expenseDto.setStatus(EXPENSE_DRAFT);
        return expenseDto;
    }

    private static List<ExpenseItemDto> getExpenseItemDtos(ReceiveReaderDto receiveReaderDto) {
        List<ExpenseItemDto> expenseItems = new ArrayList<>();
        if (receiveReaderDto != null && !receiveReaderDto.getLineItems().isEmpty()) {
            for (ReceiveReaderItemDto lineItem : receiveReaderDto.getLineItems()) {
                ExpenseItemDto expenseItemDto = new ExpenseItemDto();
                expenseItemDto.setUnitPrice(lineItem.getPrice());
                expenseItemDto.setQuantity(lineItem.getQuantity());
                expenseItemDto.setDescription(lineItem.getName());
                expenseItems.add(expenseItemDto);
            }
        }
        return expenseItems;
    }

//    @Operation(summary = "Create new expanse by Receive reader")
//    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Expense"))
//    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResultTO<ExpenseDto> createExpenses(@Validated @RequestBody ExpenseDto expenseDto) throws RestException {
//        String url = "http://3.209.242.234:4000/docs";
//        apiExpenseService.save(expenseDto);
//        return ResultTO.success(expenseDto);
//    }
}
