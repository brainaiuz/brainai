package com.edatasite.workforce.rest.v1.release10.accounting;

import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.ProductTO;
import com.edatasite.workforce.rest.base.to.TaxTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Faxriddin Taslimov 06/28/2017
 */
@Tag(name = "Product", description = "Product API")
@RestController
@RequestMapping(value = "/product", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiProductControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private ProductServiceLocal productServiceLocal;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody MListingFilterParameter mFilterParameter) {
        ListResult<ProductItem> productList = productServiceLocal.getProductsListFromSolr(mFilterParameter.convertToFilterParameters());
        ArrayList<ProductTO> productTOs = new ArrayList<>();
        for (ProductItem product : productList.getList()) {
            productTOs.add(new ProductTO(product));
        }

        return successResponse(new ListResultTO<>(productList.getTotal(), productTOs));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) {
        return successResponse(new ProductTO(productServiceLocal.getProductEditData(id, false)));
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "id") Integer id) {
        boolean isDeleted = productServiceLocal.deleteProduct(id);
        if (isDeleted) {
            return this.successResponse(SUCCESS_DELETE);
        } else {
            return this.errorResponse(ERROR_FAIL_DELETE);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody ProductTO productTO) {
        return successResponse(SUCCESS_SAVE);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object update(@PathVariable(value = "id") Integer id, @RequestBody ProductTO productTO) {
        productTO.setId(id);
        return add(productTO);
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET)
    public Object getProducts(@RequestBody MListingFilterParameter filterParameter) {
        return successResponse(WrapUtils.wrapSelectItemList(productServiceLocal.getProductsAsSelectItem(filterParameter.convertToFilterParameters())));
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public Object getCategories() {
        return successResponse(WrapUtils.wrapSelectItemList(accountingServiceLocal.getCategoriesAsSelectItem()));
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
    public Object getCategory(@PathVariable(value = "id") Integer id) {
        ProductCategoryItem item = accountingServiceLocal.getProductCategory(id);
        if (item != null) {
            return successResponse(item);
        } else {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
    public Object deleteProductCategory(@PathVariable(value = "id") Integer id) {
        try {
            accountingServiceLocal.deleteProductCategory(id);
            return successResponse(SUCCESS_DELETE);
        } catch (Exception e) {
            return errorResponse(ERROR_FAIL_DELETE);
        }
    }

    @RequestMapping(value = "/warehouses", method = RequestMethod.GET)
    public Object getWarehouses() {
        return successResponse(WrapUtils.wrapSelectItemTOs(accountingServiceLocal.getWarehousesAsSelectItem()));
    }

    @RequestMapping(value = "/unitMeasurements", method = RequestMethod.GET)
    public Object getUnitMeasurements() {
        return successResponse(WrapUtils.wrapSelectItemTOs(accountingServiceLocal.getUnitMeasurementsAsSelectItem()));
    }

    @RequestMapping(value = "/brands", method = RequestMethod.GET)
    public Object getBrands() {
        return successResponse(WrapUtils.wrapSelectItemTOs(accountingServiceLocal.getBrandsAsSelectItem()));
    }

    @RequestMapping(value = "/vendors", method = RequestMethod.GET)
    public Object getVendors() {
        return successResponse(WrapUtils.wrapSelectItemTOs(accountingServiceLocal.getVendorsAsSelectItem()));
    }

    @RequestMapping(value = "/taxRates", method = RequestMethod.GET)
    public Object getTaxRates() {
        List<EdsVat> vatList = accountingServiceLocal.companyVatList(null, null);
        ArrayList<TaxTO> result = new ArrayList<>();
        for (EdsVat edsVat : vatList) {
            result.add(new TaxTO(edsVat.getObjectID(), edsVat.getName(), edsVat.getVatAmount(), edsVat.getEffectiveRateAsBigDecimal()));
        }
        return successResponse(result);
    }

}
