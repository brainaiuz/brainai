package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ProductDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ProductwithPictureLinksDto;
import com.edatasite.workforce.rest.v3.release10.accounting.service.ApiProductService;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Products/Services", description = "Collection of public APIs for Product/Services")
@RestController
@RequestMapping(value = "/product", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN})
public class ApiProductServiceControllerV3 implements ApiConstants {
    private static final Logger log = LoggerFactory.getLogger(ApiProductServiceControllerV3.class);

    private final ApiProductService apiProductService;
    private final ProductService productService;
    private final ItemManager itemManager;
    private final AccountingService accountingService;

    public ApiProductServiceControllerV3(ApiProductService apiProductService,
                                         ProductService productService,
                                         ItemManager itemManager,
                                         AccountingService accountingService) {
        this.apiProductService = apiProductService;
        this.productService = productService;
        this.itemManager = itemManager;
        this.accountingService = accountingService;
    }

    @Operation(summary = "Get products list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Products/Services"))
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ListResultTO<ProductDto>> getProducts(@RequestBody ListParamsDTO params,
                                                          @RequestParam(value = "simple", required = false) Boolean simple) {
        log.info("REST request to get products list");
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.ProductServiceListPanel);

        ListResultTO<ProductDto> productlist;
        if (simple != null && simple) {
            productlist = apiProductService.getSimpleProductResultList(fp);
        } else {
            productlist = apiProductService.getProductList(fp);
        }
        return ResultTO.success(productlist);
    }

    @Operation(summary = "Get products list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Products/Services"))
    @PostMapping(value = "/list/simple", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<ProductDto>> getSimpleProducts(@RequestBody ListParamsDTO params) {
        log.info("REST request to get simple products list");
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.ProductServiceListPanel);
        List<ProductDto> products = apiProductService.getSimpleProductList(fp);
        return ResultTO.success(products);
    }

    @Operation(summary = "Get existing product by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "product"))
    @GetMapping(path = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ProductDto> getProductById(@PathVariable final Integer productId) throws RestException {
        log.info("REST request to get product by id: {}", productId);
        return ResultTO.success(apiProductService.getById(productId));
    }

    @Operation(summary = "Create new product")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Product"))
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ProductDto> createProduct(@Validated @RequestBody ProductDto productDto) throws RestException {
        log.info("REST request to create product");
        if (productDto.getId() != null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Product ID is specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiProductService.save(productDto, true);
        return ResultTO.success(productDto);
    }

    @Operation(summary = "Bulk Create new product")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Product"))
    @PostMapping(path = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<ProductDto>> bulkCreateProduct(@Validated @RequestBody List<ProductDto> productDtoList) throws RestException {
        log.info("REST request to bulk create product");
        List<ProductDto> result = new ArrayList<>();
        for (ProductDto productDto : productDtoList) {
            if (itemManager.getItemByName(productDto.getName()) == null) {
                apiProductService.save(productDto, true);
            }
            result.add(productDto);
        }
        return ResultTO.success(result);
    }

    @Operation(summary = "Update existing product")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Product"))
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ProductDto> updateProduct(@Validated @RequestBody ProductDto productDto) throws RestException {
        log.info("REST request to update product");
        if (!productDto.isExistingObject()) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Product objectKey/ID or Number is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiProductService.save(productDto, false);
        return ResultTO.success(productDto);
    }

    @Operation(summary = "Patch Update existing product")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Product"))
    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ProductDto> patchUpdateProduct(@RequestBody ProductDto productDto) throws RestException {
        log.info("REST request to patch update product");
        if (!productDto.isExistingObject()) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Product objectKey/ID or Number is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiProductService.savePatch(productDto);
        return ResultTO.success(productDto);
    }

    @Operation(summary = "Patch Update existing product with POST method")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Product"))
    @PostMapping(value = "/patch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ProductDto> postUpdateProduct(@RequestBody ProductDto productDto) throws RestException {
        log.info("REST request to update product /patch");
        if (!productDto.isExistingObject()) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Product objectKey/ID or Number is not specified", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        apiProductService.savePatch(productDto);
        return ResultTO.success(productDto);
    }

    @Operation(summary = "Delete existing product by id")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Product"))
    @DeleteMapping(value = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> deleteProduct(@PathVariable Integer productId) throws RestException {
        log.info("REST request to delete product by id: {}", productId);
        apiProductService.deleteById(productId);
        return ResultTO.success();
    }

    @Operation(summary = "Get product category")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Product Category"))
    @PostMapping(value = "/category", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ListResult<ProductCategoryItem>> getProductCategory(@RequestBody ListParamsDTO request) {
        log.info("REST request to get product category");
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(request, ListPanelType.ProductCategoryListPanel);

        return ResultTO.success(accountingService.getProductCategoriesList(fp));
    }

    @Operation(summary = "Get product picture links")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Product picture links"))
    @PostMapping(value = "/links", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductwithPictureLinksDto> getAllProductPictureLinks(@RequestBody List<Integer> ids) {
        log.info("REST request to get all product picture links");
        List<ProductwithPictureLinksDto> productwithPictureLinksDtos = new ArrayList<>();
        for (Integer id : ids) {
            List<String> list = new ArrayList<>();
            ProductwithPictureLinksDto productwithPictureLinksDto = new ProductwithPictureLinksDto();

            NewProduct product = productService.getProduct(id);
            productwithPictureLinksDto.setProductId(id);
            productwithPictureLinksDto.setProductName(product.getItemName());
            productwithPictureLinksDto.setProductNumber(product.getNumberData().getNumberString());
            ProductPicture[] productPicture = productService.getProductPictures(id, 0);
            for (ProductPicture productPicture1 : productPicture) {
                list.add(productPicture1.getUrl());
                if (productPicture1.isDefaultPicture()) {
                    productwithPictureLinksDto.setDefultPictureLink(productPicture1.getUrl());
                }
            }
            productwithPictureLinksDto.setPictureList(list);
            productwithPictureLinksDtos.add(productwithPictureLinksDto);

        }
        return productwithPictureLinksDtos;
    }

    @PostMapping(value = "/copy/public", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> uploadProductPictureToPublicBucket(@RequestParam("productIds") List<Integer> productIds,
                                                           @RequestParam(value = "tempLink", required = false) String tempLink) throws RestException {
        log.info("REST request to copy product picture to public bucket : {}", productIds);
        return apiProductService.uploadProductPictureToPublicBucket(productIds, tempLink);
    }

    @PostMapping(value = "/activate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<Boolean> updateMultipleStatus(@RequestParam("ids") List<Integer> ids,
                                                  @RequestParam("active") boolean active) {
        log.info("REST request to update multiple active: {} by ids: {}", active, ids);
        apiProductService.updateActive(ids, active);
        return ResultTO.success(true);
    }
}
