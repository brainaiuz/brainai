package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.currency.ExchangeCurrencyManager;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CustomFormValueSaveTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.HttpProxyRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.NameValueDto;
import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestCustomFormValues;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseResultListData;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.google.gson.Gson;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "Custom Forms", description = "Custom Forms API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCustomFormControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiCustomFormControllerV2.class);
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ExchangeCurrencyManager exchangeCurrencyManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ProfileServiceLocal profileServiceLocal;
    @Autowired
    private AllInOneServiceLocal allInOneService;
    @Autowired
    private CustomFormManager customFormManager;

    /*@Operation(summary = "Get Main Currency", description = "Retrieves base currency of the company")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have a base currency of the current company"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/payments/main_currency", method = RequestMethod.GET)
    public Object getMainCurrency() throws RestException {
        CurrencyItem currencyItem = currencyServiceLocal.getCompanyBaseCurrency();
        if (currencyItem != null) {
            return successResponse(new ProductCurrencyTO(currencyItem.getId(), currencyItem.getName(), BigDecimal.ONE));
        }
        return successResponse(new ResponseData());
    }*/

    @Operation(summary = "Get Custom Forms", description = "Retrieves list of custom forms")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/customform/list", method = RequestMethod.GET)
    public Object getFormsList() throws RestException {

        ListingFilterParameter filterParameter = new ListingFilterParameter();

        ListResult<PropertyItem> forms = profileServiceLocal.getPropertyItems(filterParameter);
        //Filter forms without formID because those are not custom forms
        List<PropertyItem> items = forms.getList().stream().filter(form -> StringUtils.isNotBlank(form.getFormID())).collect(Collectors.toList());

        return successResponse(new ResponseResultListData<PropertyItem>(items, forms.getTotal()));
    }

    @Operation(summary = "Get Custom Form Values", description = "Retrieves list of filled form values")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/customform/values/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object getFormValuesList(@RequestBody RequestCustomFormValues searchData) throws RestException {

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setParentID(searchData.getParent_id());
        filterParameter.setForm(searchData.getForm_id());
        filterParameter.setStart(searchData.getStart());
        filterParameter.setLimit(searchData.getLimit());
        filterParameter.setSearchKey(searchData.getSearch_text());
        if (StringUtils.isNotBlank(searchData.getFilter_field())) {
            filterParameter.setColumnCode(searchData.getFilter_field());
        }

        ListPanelToolRpc panelTools = new ListPanelToolRpc();

        ArrayList<String> colums = new ArrayList<String>(Arrays.asList(FormItems.CREATER, FormItems.CREATED_DATE,
                FormItems.UPDATER, FormItems.UPDATED_DATE, FormItems.STATUS, FormItems.APPROVER));
        if (searchData.getCustom_fields() != null) {
            colums.addAll(searchData.getCustom_fields());
        }
        panelTools.setColumnCodeName(colums);

        filterParameter.setListPanelTool(panelTools);


        ListResult<FormItems> formValues = commonServiceLocal.getCustomFormItems(filterParameter);

        return successResponse(new ResponseResultListData<FormItems>(formValues.getList(), formValues.getTotal()));
    }

    @Operation(summary = "Get Custom Form Value", description = "Retrieves Custom Form Value")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have main Custom Form Value"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/customform/{fid}/{form_id}/{id}", method = RequestMethod.GET)
    public Object getCustomFormValue(@PathVariable(value = "fid") Integer fID, @PathVariable(value = "form_id") String formId, @PathVariable(value = "id") Integer id) throws RestException {
        if (StringUtils.isBlank(formId)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "form_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        FormItems formValues = commonServiceLocal.getCustomFormItem(id, fID, formId, false, null, null, null, null);
        return formValues;
    }

    @Operation(summary = "Get Custom Form", description = "Retrieves Custom Form")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have main Custom Form"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/customform/{form_id}", method = RequestMethod.GET)
    public Object getCustomForm(@PathVariable(value = "form_id") String formId) throws RestException {
        if (StringUtils.isBlank(formId)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "form_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        Map<String, LinkedList<CustomizeFormItem>> form = allInOneService.getCustomizeGridForm(formId);
        return form;
    }

    @Operation(summary = "Save Custom Form Values", description = "Save Custom Form Values")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have saved Custom Form value id"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/customform/values/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object saveCustomFormValues(@RequestBody CustomFormValueSaveTO valueSaveTO) throws RestException {
        if (valueSaveTO == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "data is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        FormItems formItems = new FormItems();
        formItems.setObjectID(valueSaveTO.getObjectID());
        formItems.setFormID(valueSaveTO.getFormID());
        formItems.setCustomFieldItems(valueSaveTO.getCustomFieldItems());
        formItems.setTableItems(valueSaveTO.getTableItems());
        formItems.setStatusCode(valueSaveTO.getStatusCode());
        return commonServiceLocal.saveCustomFormItem(formItems);
    }

    @Operation(summary = "Http Proxy", description = "Calls proxy")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have 3d party response"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/proxy/call", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object callProxy(@RequestBody HttpProxyRequestTO requestTO) throws RestException, IOException {
        /*if (formItems==null){
            throw new RestException(GENERAL_ERROR_MESSAGE, "data is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }*/
        switch (requestTO.getMethod()) {
            case GET -> {
                StringBuilder url = new StringBuilder(requestTO.getUrl());
                if (requestTO.getParams() != null) {
                    url.append("?1=1");
                    requestTO.getParams().forEach(param -> url.append("&").append(param.getName()).append("=").append(param.getValue()));
                }
                ResponseEntity<String> resp = restTemplate.exchange(url.toString(),
                        HttpMethod.GET, new HttpEntity<>(createHeaders(requestTO.getHeaders())), String.class);
                HashMap<String, Object> result = new HashMap<>();
                result.put("statusCode", resp.getStatusCode());
                result.put("body", resp.getBody());
                result.put("headers", resp.getHeaders());
                return result;
            }
            case POST -> {
                if (StringUtils.isNotBlank(requestTO.getRequestJson())) {
                    ResponseEntity<HashMap> resp = restTemplate.exchange(requestTO.getUrl(),
                            HttpMethod.POST, getRequestEntity(new Gson().fromJson(requestTO.getRequestJson(), SaveUp.class), requestTO.getHeaders()), HashMap.class);
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("statusCode", resp.getStatusCode());
                    result.put("body", resp.getBody());
                    result.put("headers", resp.getHeaders());
                    return result;
                } else {
                    ResponseEntity<HashMap> resp = restTemplate.exchange(requestTO.getUrl(),
                            HttpMethod.POST, getRequestEntity(requestTO.getRequestJson(), requestTO.getHeaders()), HashMap.class);
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("statusCode", resp.getStatusCode());
                    result.put("body", resp.getBody());
                    result.put("headers", resp.getHeaders());
                    return result;

                }

            }
            case PUT -> {
                if (StringUtils.isNotBlank(requestTO.getRequestJson())) {
                    return restTemplate.exchange(requestTO.getUrl(),
                            HttpMethod.PUT, getRequestEntity(requestTO.getRequestJson(), requestTO.getHeaders()), HashMap.class);
                }
            }
        }

        return null;
    }

    @Operation(summary = "Delete Custom Form", description = "Delete particular entity like Lead, Opportunity, Company, Contact etc. Particular entity is described in path, like other requests. Server should check if current user has permissions to delete this particular item, and if no give user message: You don't have permissions to delete this entry. Please contact your administrator")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/customform/{item_id}/delete", method = RequestMethod.DELETE)
    public Object deleteCustomForm(@PathVariable(value = "item_id") Integer item_id) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        try {
            commonService.deleteCustomFormItem(item_id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return successResponse(new ResponseData());
    }

    private <T> HttpEntity<T> getRequestEntity(T body, List<NameValueDto> pairs) {
        return new HttpEntity<T>(body,
                createHeaders(pairs));
    }

    private HttpHeaders createHeaders(List<NameValueDto> pairs) {
        HttpHeaders headers = new HttpHeaders();
        pairs.forEach(pair -> headers.set(pair.getName(), pair.getValue()));
        return headers;
    }

    /**
     * TODO need to discuss with Anvar aka
     */
    class SaveUp implements Serializable {
        String code;
        Casheir casheir;
        Receipt receipt;

        public SaveUp() {
        }

        public SaveUp(String code, Casheir casheir, Receipt receipt) {
            this.code = code;
            this.casheir = casheir;
            this.receipt = receipt;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Casheir getCasheir() {
            return casheir;
        }

        public void setCasheir(Casheir casheir) {
            this.casheir = casheir;
        }

        public Receipt getReceipt() {
            return receipt;
        }

        public void setReceipt(Receipt receipt) {
            this.receipt = receipt;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SaveUp saveUp)) return false;

            if (code != null ? !code.equals(saveUp.code) : saveUp.code != null) return false;
            if (casheir != null ? !casheir.equals(saveUp.casheir) : saveUp.casheir != null) return false;
            if (receipt != null ? !receipt.equals(saveUp.receipt) : saveUp.receipt != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = code != null ? code.hashCode() : 0;
            result = 31 * result + (casheir != null ? casheir.hashCode() : 0);
            result = 31 * result + (receipt != null ? receipt.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "SaveUp{" +
                    "code='" + code + '\'' +
                    ", casheir=" + casheir +
                    ", receipt=" + receipt +
                    '}';
        }
    }

    class Receipt implements Serializable {
        double total;
        double cash;
        int points;

        public Receipt() {
        }

        public Receipt(double total, double cash, int points) {
            this.total = total;
            this.cash = cash;
            this.points = points;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public double getCash() {
            return cash;
        }

        public void setCash(double cash) {
            this.cash = cash;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Receipt receipt)) return false;

            if (Double.compare(receipt.total, total) != 0) return false;
            if (Double.compare(receipt.cash, cash) != 0) return false;
            if (points != receipt.points) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(total);
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(cash);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + points;
            return result;
        }

        @Override
        public String toString() {
            return "Receipt{" +
                    "total=" + total +
                    ", cash=" + cash +
                    ", points=" + points +
                    '}';
        }
    }

    class Casheir implements Serializable {
        int externalId;

        public Casheir() {
        }

        public Casheir(int externalId) {
            this.externalId = externalId;
        }

        public int getExternalId() {
            return externalId;
        }

        public void setExternalId(int externalId) {
            this.externalId = externalId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Casheir casheir)) return false;

            if (externalId != casheir.externalId) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return externalId;
        }

        @Override
        public String toString() {
            return "Casheir{" +
                    "externalId=" + externalId +
                    '}';
        }
    }
}
