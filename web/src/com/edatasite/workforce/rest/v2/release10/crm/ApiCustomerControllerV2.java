package com.edatasite.workforce.rest.v2.release10.crm;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CustomerLookupRequestData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.AddContactTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
@Tag(name = "Customers", description = "Customers API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiCustomerControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiCustomerControllerV2.class);

    @Autowired
    private ContactServiceLocal contactServiceLocal;


    @Operation(summary = "Client Users Lookup", description = """
            Retrieves customers and suppliers who have access to the system\s

            account_type should be CUSTOMER or SUPPLIER""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have leads based on search query"),
            @ApiResponse(responseCode = "400", description = "account_type, start point and limit are required"),
            @ApiResponse(responseCode = "422", description = "account_type should be one of CUSTOMER or SUPPLIER"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time")})
    @RequestMapping(value = "/customer_user_lookup", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object clientContactLookup(@RequestBody CustomerLookupRequestData requestListSearchData) throws RestException {

        if (StringUtils.isBlank(requestListSearchData.getAccount_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "account_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!CrmConstants.CUSTOMER.equals(requestListSearchData.getAccount_type()) && !CrmConstants.SUPPLIER.equals(requestListSearchData.getAccount_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "account_type should be one of CUSTOMER or SUPPLIER", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getStart().equals(requestListSearchData.getLimit()) && requestListSearchData.getLimit() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point and limit can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ArrayList<EmployeeTO> clientUserTOS = new ArrayList<>();

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStart(requestListSearchData.getStart());
        fp.setLimit(requestListSearchData.getLimit());
        fp.setSearchKey(requestListSearchData.getSearch_text());
        //To retrieve only ClientContact who can login we must set this flag ACCESS_ENABLED
        fp.setAccessEnabled(Boolean.TRUE);
        //To filter by account type because there are other types who can login to KPI (eg. SUPPLIERs)
        fp.setAccountType(requestListSearchData.getAccount_type());
        fp.setWithImage(true);

        //Calling Solr to retrieve list of ClientContacts
        ListResult<ContactListItem> clientContacts = contactServiceLocal.getNewContactList(fp);

        if (clientContacts != null) {
            for (ContactListItem contact : clientContacts.getList()) {
//                if (contact.getClientContactId() != null) {

                EmployeeTO clientContactTO = new EmployeeTO();
                //We need to set ClientContactId who is actuall user in context of KPI
                clientContactTO.setId(contact.getClientContactId());
                clientContactTO.setName(contact.getName());
                    /*if(contact.getCrmAccount()!=null && StringUtils.isNotBlank(contact.getCrmAccount().getName())) {
                        clientContactTO.setDepartment(contact.getCrmAccount().getName());
                    }*/

                    /*EdsUpload photo = userManager.get(contact.getClientContactId()).getPhoto();
                    if (photo != null) {
                        clientContactTO.setAvatar(commonServiceLocal.getImageUrl(photo.getObjectID()));
                    }*/ //todo see fp.setWithImage(true); ContactServiceImpl.java line 1035
                clientUserTOS.add(clientContactTO);
//                }
            }
        }

        return successResponse(new EmployeeListTO(clientUserTOS));
    }

    @Operation(summary = "Customer Contact Lookup", description = """
            Retrieves customers and suppliers\s

            account_type should be CUSTOMER or SUPPLIER""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have leads based on search query"),
            @ApiResponse(responseCode = "400", description = "account_type, start point and limit are required"),
            @ApiResponse(responseCode = "422", description = "account_type should be one of CUSTOMER or SUPPLIER"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time")})
    @RequestMapping(value = "/customer_contact_lookup", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object customerContactLookup(@RequestBody CustomerLookupRequestData requestListSearchData) throws RestException {

        if (StringUtils.isBlank(requestListSearchData.getAccount_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "account_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!CrmConstants.CUSTOMER.equals(requestListSearchData.getAccount_type()) && !CrmConstants.SUPPLIER.equals(requestListSearchData.getAccount_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "account_type should be one of CUSTOMER or SUPPLIER", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getStart().equals(requestListSearchData.getLimit()) && requestListSearchData.getLimit() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point and limit can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ArrayList<AddContactTO> contactTOS = new ArrayList<>();

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStart(requestListSearchData.getStart());
        fp.setLimit(requestListSearchData.getLimit());
        fp.setSearchKey(requestListSearchData.getSearch_text());
        //To filter by account type because there are other types who can login to KPI (eg. SUPPLIERs)
        fp.setAccountType(requestListSearchData.getAccount_type());

        //Calling Solr to retrieve list of ClientContacts
        ListResult<ContactListItem> clientContacts = contactServiceLocal.getNewContactList(fp);

        if (clientContacts != null) {
            for (ContactListItem contact : clientContacts.getList()) {

                AddContactTO addContactTO = new AddContactTO();

                addContactTO.setFirst_name(contact.getFirstName());
                addContactTO.setLast_name(contact.getLastName());
                addContactTO.setEmail(contact.getPrimaryEmail());
                addContactTO.setPhone_number(contact.getPrimaryPhone());
                if (contact.getCrmAccount() != null) {
                    addContactTO.setCompany_name(contact.getCrmAccount().getName());
                }
                contactTOS.add(addContactTO);

            }
        }

        return contactTOS;
    }

}
