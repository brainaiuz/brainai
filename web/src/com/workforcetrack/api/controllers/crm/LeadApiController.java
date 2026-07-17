package com.workforcetrack.api.controllers.crm;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * Created by Virus on 7/28/14.
 */
@Controller
@RequestMapping(value = "/lead")
public class LeadApiController {
	@Autowired
	private ContactService contactService;
	@Autowired
	private CRMService crmService;

	@RequestMapping(value = "/list", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
	@CheckRequest
	@ResponseBody
	public Object list(@RequestBody ListingFilterParameter data) throws BaseApiException {
		try {
			if (data == null) {
				throw ApiExceptions.PARAMS_INCORRECT;
			}

			return crmService.getNewLeads(data);
		} catch (ClassCastException e) {
			throw ApiExceptions.PARAMS_INCORRECT;
		} catch (Exception e) {
			throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
	@CheckRequest
	@ResponseBody
	public Object save(@RequestBody ContactListItem data) throws BaseApiException {
		try {
			if (data == null) {
				throw ApiExceptions.PARAMS_INCORRECT;
			}


			return crmService.saveLead(data, null);
		} catch (ClassCastException e) {
			throw ApiExceptions.PARAMS_INCORRECT;
		} catch (Exception e) {
			throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
		}
	}

	@CheckRequest
	@ResponseBody
	@RequestMapping(value = "/delete/{id}-{ownerID}", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
	public Object delete(@PathVariable Integer id, @PathVariable Integer ownerID) throws BaseApiException {
		try {
			if (id == null || id < 1 || ownerID == null || ownerID < 1) {
				throw ApiExceptions.PARAMS_INCORRECT;
			}
			ArrayList<Integer> list = new ArrayList<>();
			list.add(id);
			return contactService.deleteContacts(list, ownerID, false);
		} catch (ClassCastException e) {
			throw ApiExceptions.PARAMS_INCORRECT;
		} catch (Exception e) {
			throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
		}
	}

	@CheckRequest
	@ResponseBody
	@RequestMapping(value = "/get/{id}", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
	public Object get(@PathVariable Integer id) throws BaseApiException {
		try {
			if (id == null || id < 1) {
				throw ApiExceptions.PARAMS_INCORRECT;
			}
			return crmService.getLead(id);
		} catch (ClassCastException e) {
			throw ApiExceptions.PARAMS_INCORRECT;
		} catch (Exception e) {
			throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
		}
	}

}
