package com.workforcetrack.api.controllers;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import com.workforcetrack.api.presenter.BaseApiPresenter;
import com.workforcetrack.api.presenter.PosApiPresenter;
import com.workforcetrack.api.presenter.PosProductApiPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 16.03.13
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/pos")
public class PosProductApiController {

    @Autowired
    private ProductService productService;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private PosApiPresenter posApiPresenter;

    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object searchProject(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                @RequestParam(value = "rows", required = false, defaultValue = "15") int rows,
                                @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey) throws BaseApiException {
        try {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStart(page);
            fp.setLimit(rows);
            fp.setSearchKey(searchKey);

            ListResult<ProductItem> productList = productService.getProductsList(fp);
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put(BaseApiPresenter.TOTAL_COUNT, productList.getTotal());

            if (productList != null && productList.getList() != null && productList.getList().size() > 0) {
                ArrayList<Map<String, Object>> items = new ArrayList<>();
                for (ProductItem item : productList.getList()) {
                    Map<String, Object> itemMap = PosProductApiPresenter.convertToMapItem(item);
                    ProductPicture[] productPictures = productService.getProductPictures(item.getObjectId(), Constants.FILE_SIZE_DEFAULT);
                    if (productPictures != null && productPictures.length > 0) {
                        itemMap.put(PosProductApiPresenter.DEFAULT_IMAGE_URL, productPictures[0].getUrl());
                    }
                    items.add(itemMap);
                }
                resultMap.put(BaseApiPresenter.ITEMS, items);
            }

            return resultMap;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object save(@RequestBody Map<String, Object> params) throws BaseApiException {
        try {
            Map<String, Object> saveDataMap = (Map<String, Object>) params.get(APIConstants.SAVE_DATA);
            if (saveDataMap == null || saveDataMap.isEmpty()) {
                throw ApiExceptions.PARAMS_INCORRECT;
            }
            Integer objectId = (Integer) saveDataMap.get(BaseApiPresenter.OBJECT_ID);
            NewInvoice posInvoiceItem = posApiPresenter.getPosInvoiceItem(saveDataMap);
            SaveResult saveResult = null;
            if (objectId != null && !objectId.equals(0)) {
                saveResult = invoiceService.saveSaleInvoice(posInvoiceItem);
            } else {
                saveResult = invoiceService.updateSaleInvoice(posInvoiceItem);
            }
            if (saveResult.isInvoiceExist()) {
            } else if (saveResult.getExceededCreditLimit() && saveResult.getRemainingBalance() != null && saveResult.getRemainingBalance() != null) {
                String message = "The credit limit for {Customer} is {Credit Limit}. The customer balance, including this invoice is {Remaining Balance + Invoice Amount}. Continue?";
            }
            return saveResult;
        } catch (ParseException | ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }

    }



}
