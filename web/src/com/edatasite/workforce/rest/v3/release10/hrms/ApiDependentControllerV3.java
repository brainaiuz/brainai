package com.edatasite.workforce.rest.v3.release10.hrms;

import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v3.release10.core.BaseApiControllerV3;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.DependentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * User : Asadbek Raxmatov on 1/31/2024 2:57 PM
 */
@Tag(name = "Dependents", description = "Dependents API")
@RestController
@RequestMapping(value = "/dependent", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_VALUE})
public class ApiDependentControllerV3 extends BaseApiControllerV3{
    @Autowired
    private HrmsService hrmsService;

    @Operation(summary = "Get Dependents list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Dependents"))
    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResultTO<ListResultTO<DependentDTO>> getDependentsList(@RequestBody ListParamsDTO params,
                                                              @RequestParam(value = "employeeID", required = false) Integer employeeID) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.DependentListPanel);
        fp.setEmployeeId(employeeID);
        fp.setSearchKey(null);

        ListResultTO<DependentDTO> dependentListDTO = new ListResultTO<>();
        ListResult<DependentItem> dependentItemList  = new ListResult<>();
        if (employeeID != null) {
            dependentItemList = hrmsService.getDependentsList(fp);
        }
        if (dependentItemList != null) {
            ArrayList<DependentDTO> items = dependentItemList.getList().stream()
                    .map(e -> ConvertUtils.toDto(e))
                    .collect(Collectors.toCollection(ArrayList::new));
            dependentListDTO.setTotalNumber(items.size());
            dependentListDTO.setItems(items);
        }


        return ResultTO.success(dependentListDTO);
    }


}


