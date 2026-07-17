package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.server.app.CustomerSupplierApiService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.CustomerSupplierDto;
import com.edatasite.workforce.rest.v3.release10.accounting.request.CustomerSupplierRequest;
import com.edatasite.workforce.rest.v3.release10.accounting.utils.CustomerSupplierDtoUtils;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Normurod Buriev.
 * Date: 11/11/2020 3:58 PM
 */
@Tag(name = "Supplier Api Resource", description = "Here is a supplier api resouce that contains CRUD operations for supplier")
@RestController
@RequestMapping(value = "/supplier", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class SupplierApiResource implements ApiConstants {

    @Autowired
    private ClientService clientService;
    @Autowired
    private CustomerSupplierDtoUtils dtoUtils;
    @Autowired
    private CustomerSupplierApiService apiService;
    @Autowired
    private CrmAccountManager crmAccountManager;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ListResultTO<CustomerSupplierDto>> list(@RequestBody ListParamsDTO params) {
        ListingFilterParameter filterParameter = ListingFilterHelperV3.createListingFilter(params, ListPanelType.ClientListPanel);
        return ResponseEntity.ok(apiService.getSupplierList(filterParameter));
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<CustomerSupplierDto> create(@RequestBody @Valid CustomerSupplierRequest request) throws RestException {
        CrmAccountItem model = dtoUtils.wrapSupplierRequestToModel(request);
        Integer result = clientService.createSupplier(model, null);

        if (result == -2) {
            throw new RestException(ERROR, "Supplier with this number/name already exist.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResultTO.success(apiService.getSupplierById(result));
    }

    @RequestMapping(value = "/general-info/update", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO updateGeneralInfo(@RequestBody @Valid CustomerSupplierRequest request) throws RestException {
        if (!request.isExistingObject()) {
            throw new RestException(IN_VALID_DATA, "Supplier Id/objectKey or Number is required!", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        EdsCrmAccount supplier = null;
        if (StringUtils.isNotBlank(request.getObjectKey())) {
            supplier = crmAccountManager.getCrmAccountByObjectKey(request.getObjectKey());
        }
        if (supplier == null && request.getId() != null) {
            supplier = crmAccountManager.get(request.getId());
        }
        if (supplier == null && StringUtils.isNotBlank(request.getNumber())) {
            supplier = crmAccountManager.getCrmAccountByNumber(request.getNumber());
        }
        if (supplier == null) {
            throw new RestException(IN_VALID_DATA, "Supplier is not exist!", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        Integer result = clientService.createSupplier(dtoUtils.wrapGeneralInfoRequestToModel(request, supplier), null);
        return ResultTO.success(apiService.getSupplierById(supplier.getObjectID()));
    }

    @RequestMapping(value = "/{supplierId}", method = RequestMethod.GET)
    public ResultTO<CustomerSupplierDto> getDetails(@PathVariable("supplierId") Integer supplierId) {
        return ResultTO.success(apiService.getSupplierById(supplierId));
    }

    @RequestMapping(value = "/{supplierId}", method = RequestMethod.DELETE)
    public ResultTO deleteSupplier(@PathVariable("supplierId") Integer supplierId) {
        clientService.deleteSupplier(supplierId, true, true);
        return ResultTO.success();
    }
}
