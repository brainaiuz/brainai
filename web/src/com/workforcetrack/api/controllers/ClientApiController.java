package com.workforcetrack.api.controllers;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.client.client.rpc.NewClientList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import com.workforcetrack.api.presenter.BaseApiPresenter;
import com.workforcetrack.api.presenter.ClientApiPresenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 19.06.12
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/client")
public class ClientApiController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private CRMService crmService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private UserManager userManager;
    @Autowired
    AllInOneService allInOneService;

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object search(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                         @RequestParam(value = "rows", required = false, defaultValue = "15") int rows,
                         @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey) throws BaseApiException {
        try {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStart(page);
            fp.setLimit(rows);
            fp.setSearchKey(searchKey);
            NewClientList searchResult = clientService.getNewClients(fp);

            ClientApiPresenter presenter = new ClientApiPresenter();
            return presenter.convertToMapListing(searchResult);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/{Id}", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object search(@PathVariable Integer Id) throws BaseApiException {
        try {
            CrmAccountItem searchResult = clientService.getClientForEdit(Id);
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCRM(true);
            fp.setLookUp(true);
            SelectItem[] accountNames = allInOneService.getLookUpItems(fp, CrmConstants.CRM_ACCOUNT_ID, null);
            ClientApiPresenter presenter = new ClientApiPresenter();
            Map<String, Object> resultMap = presenter.convertToMap(searchResult);
            resultMap.put("accountNames", accountNames);

            return resultMap;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/contact", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object searchContacts(@RequestParam(value = "accountId", required = false, defaultValue = "0") int accountId,
                                 @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                 @RequestParam(value = "rows", required = false, defaultValue = "15") int rows,
                                 @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey) throws BaseApiException {
        try {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStart(page);
            fp.setLimit(rows);
            fp.setSearchKey(searchKey);
            fp.setCrmAccountId(accountId);

            ListLoadConfig config = new ListLoadConfig();
            config.setSortField(fp.getSortField());
            config.setStart(fp.getStart());
            config.setLimit(fp.getLimit());
            config.setSortDir(fp.isAscending() ? 1 : 2);

            ContactList searchResult = contactService.getContactList(fp, config);
            ClientApiPresenter presenter = new ClientApiPresenter();
            return presenter.convertToMap(searchResult);

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

            Integer objectID = (Integer) saveDataMap.get(BaseApiPresenter.OBJECT_ID);
            Integer saveResult = -1;
            ClientApiPresenter presenter = new ClientApiPresenter();
            Integer ownerID = userManager.getUser().getObjectID();

            if (objectID != null && objectID > 0) {
                CrmAccountItem existingAccount = clientService.getClientForEdit(objectID);
                existingAccount = presenter.convertToItem(saveDataMap, existingAccount);
                saveResult = crmService.saveAccount(existingAccount, EdsCrmAccount.CUSTOMER, ownerID, false, false, false, true);
            } else {
                CrmAccountItem newAccount = new CrmAccountItem();
                newAccount = presenter.convertToItem(saveDataMap, newAccount);
                saveResult = clientService.createClient(newAccount, ownerID);
            }
            return saveResult;
        } catch (ParseException | ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object delete(@RequestBody Map<String, Object> params) throws BaseApiException {
        try {
            Integer objectID = (Integer) params.get(APIConstants.OBJECT_ID);
            if (objectID == null || objectID.equals(0)) {
                throw ApiExceptions.PARAMS_INCORRECT;
            }
            return clientService.deleteClient(objectID, false, false);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        }
    }


}
