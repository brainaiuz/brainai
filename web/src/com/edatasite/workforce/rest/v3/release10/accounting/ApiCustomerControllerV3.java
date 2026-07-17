package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.BaseApiControllerV3;
import com.edatasite.workforce.rest.v3.release10.core.to.IdDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.AddressAddDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.AddressEditDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.AddressInformationEditDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.CustomerAddDTO;
import com.edatasite.workforce.rest.v3.release10.crm.ApiCrmAccountControllerV3;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

/**
 * User : Dilsh0d Madrahimov on 9/16/2019 11:12 PM
 */
@Tag(name = "Customers", description = "Customers Public API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiCustomerControllerV3 extends BaseApiControllerV3 {

    private static final Logger log = LoggerFactory.getLogger(ApiCrmAccountControllerV3.class);

    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CommonService commonService;


    @Operation(summary = "Add Customer", description = "Add Customer")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code ")})
    @RequestMapping(value = "/customers", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody CustomerAddDTO customerAddDTO) throws RestException {

        if (customerAddDTO == null) {
            throw new RestException(ERROR_MESSAGE, "Object can not be null", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (customerAddDTO.getAccount_information() == null) {
            throw new RestException(ERROR_MESSAGE, "Account main information can not be null or empty", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (StringUtils.isBlank(customerAddDTO.getAccount_information().getName())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "name field is required.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        CrmAccountItem crmAccountItem = new CrmAccountItem();
        crmAccountItem.setName(customerAddDTO.getAccount_information().getName());
        crmAccountItem.setNumber(customerAddDTO.getAccount_information().getNumber());
        crmAccountItem.setEmail(customerAddDTO.getAccount_information().getEmail());
        crmAccountItem.setPhone(customerAddDTO.getAccount_information().getPhone());
        crmAccountItem.setFax(customerAddDTO.getAccount_information().getFax());
        crmAccountItem.setWebsite(customerAddDTO.getAccount_information().getWebsite());
        crmAccountItem.setCustomFields(CustomFieldsUtils.convertCustomFields(customerAddDTO.getAccount_information().getCustomFields(), commonService.getCompanyCustomFields(ViewName.CrmAccount), null));

        EdsReference accountType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER);
        SelectItem accountTypeItem = accountType.getAsSelectItem();
        accountTypeItem.setSelected(true);
        crmAccountItem.setAccountTypes(new SelectItem[]{accountTypeItem});

        if (customerAddDTO.getAddress_information() != null) {
            if (customerAddDTO.getAddress_information().getBilling_addresses() != null && customerAddDTO.getAddress_information().getBilling_addresses().size() > 0) {
                ArrayList<Address> billingAddressList = new ArrayList<>();
                for (AddressAddDTO addressAddDTO : customerAddDTO.getAddress_information().getBilling_addresses()) {
                    Address address = new Address();
                    address.setName(addressAddDTO.getName());
                    address.setAddress(addressAddDTO.getAddress_line_1());
                    address.setAddressb(addressAddDTO.getAddress_line_2());
                    address.setCity(addressAddDTO.getCity());
                    address.setCountryId(addressAddDTO.getCountry_id());
                    address.setStateId(addressAddDTO.getState_id());
                    address.setPrimary(Optional.ofNullable(addressAddDTO.getIs_primary()).orElse(false));
                    address.setZipCode(addressAddDTO.getPost_code());
                    address.setEntityType(EdsAddress.ENTITY_TYPE_COMPANY);
                    address.setRelationType(EdsAddress.HOME);
                    billingAddressList.add(address);
                }
                crmAccountItem.setBillAddresses(billingAddressList.toArray(new Address[0]));

                if (customerAddDTO.getAddress_information().getMailing_addresses() != null && customerAddDTO.getAddress_information().getMailing_addresses().size() > 0) {
                    ArrayList<Address> mailingAddressList = new ArrayList<>();
                    for (AddressAddDTO addressAddDTO : customerAddDTO.getAddress_information().getMailing_addresses()) {
                        Address address = new Address();
                        address.setName(addressAddDTO.getName());
                        address.setAddress(addressAddDTO.getAddress_line_1());
                        address.setAddressb(addressAddDTO.getAddress_line_2());
                        address.setCity(addressAddDTO.getCity());
                        address.setCountryId(addressAddDTO.getCountry_id());
                        address.setStateId(addressAddDTO.getState_id());
                        address.setPrimary(Optional.ofNullable(addressAddDTO.getIs_primary()).orElse(false));
                        address.setZipCode(addressAddDTO.getPost_code());
                        address.setEntityType(EdsAddress.ENTITY_TYPE_COMPANY);
                        address.setRelationType(EdsAddress.HOME);
                        mailingAddressList.add(address);
                    }
                    crmAccountItem.setMailAddresses(mailingAddressList.toArray(new Address[0]));
                }
            }
        }

        if (customerAddDTO.getFinancial_information() != null) {
            crmAccountItem.setRegistrationNumber(customerAddDTO.getFinancial_information().getRegistration_number());
            crmAccountItem.setVatNumber(customerAddDTO.getFinancial_information().getVat_number());
            if (customerAddDTO.getFinancial_information().getOpening_balance() != null) {
                crmAccountItem.setBalanceAmount(customerAddDTO.getFinancial_information().getOpening_balance().doubleValue());
            }

            if (StringUtils.isNotBlank(customerAddDTO.getFinancial_information().getAs_of_date())) {
                SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

                try {
                    crmAccountItem.setBalanceDate(new DateNonConvertable(longDateTimezoneFormat.parse(customerAddDTO.getFinancial_information().getAs_of_date())));
                } catch (ParseException e) {
                    log.error("", e);
                    throw new RestException("Invalid As of date format", "Invalid As of date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }

            if (customerAddDTO.getFinancial_information().getBank_account_id() != null && customerAddDTO.getFinancial_information().getBank_account_id() > 0) {
                crmAccountItem.setBankAccountId(customerAddDTO.getFinancial_information().getBank_account_id());
            }

            if (customerAddDTO.getFinancial_information().getTerms_id() != null && customerAddDTO.getFinancial_information().getTerms_id() > 0) {
                crmAccountItem.setTermsItem(new SelectItem(customerAddDTO.getFinancial_information().getTerms_id()));
            }
            if (customerAddDTO.getFinancial_information().getPayment_method_id() != null && customerAddDTO.getFinancial_information().getPayment_method_id() > 0) {
                crmAccountItem.setPaymentMethodId(customerAddDTO.getFinancial_information().getPayment_method_id());
            }
            if (customerAddDTO.getFinancial_information().getTax_id() != null && customerAddDTO.getFinancial_information().getTax_id() > 0) {
                crmAccountItem.setVat(new TaxItem(customerAddDTO.getFinancial_information().getTax_id(), null));
            }
            if (customerAddDTO.getFinancial_information().getCurrency_id() != null && customerAddDTO.getFinancial_information().getCurrency_id() > 0) {
                crmAccountItem.setCurrencyId(customerAddDTO.getFinancial_information().getCurrency_id());
                if (!currencyServiceLocal.getBaseCurrency().getId().equals(customerAddDTO.getFinancial_information().getCurrency_id())) {
                    crmAccountItem.setBalanceAmount(null);
                    crmAccountItem.setBalanceDate(null);
                }
            }
        }


        try {
            Integer result = crmServiceLocal.saveAccount(crmAccountItem, CrmAccountItem.CUSTOMER, null, false, false, false, true);
            if (result < 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return successResponse(new IdDTO(result));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Get Customer Addresses")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Customer addresses")})
    @RequestMapping(value = "/customers/{id}/addresses", method = RequestMethod.GET)
    public Object getCustomerAddresses(@PathVariable(value = "id") Integer id) throws RestException {

        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsCrmAccount edsCrmAccount;
        try {
            edsCrmAccount = crmAccountManager.get(id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (edsCrmAccount == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "There is no customer with id: " + id, NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        AddressInformationEditDTO addressInformationDTO = new AddressInformationEditDTO();
        if (edsCrmAccount.getBillingAddresses() != null && !edsCrmAccount.getBillingAddresses().isEmpty()) {
            ArrayList<AddressEditDTO> billingAddresses = new ArrayList<>();
            for (EdsAddress address : edsCrmAccount.getBillingAddresses()) {
                AddressEditDTO addressDTO = new AddressEditDTO();
                addressDTO.setId(address.getObjectID());
                addressDTO.setName(address.getName());
                addressDTO.setAddress_line_1(address.getAddress());
                addressDTO.setAddress_line_2(address.getAddressb());
                addressDTO.setIs_primary(address.isPrimary());
                addressDTO.setCity(address.getCity());
                addressDTO.setPost_code(address.getZipCode());
                if (address.getCountry() != null) {
                    addressDTO.setCountry_id(address.getCountry().getObjectID());
                    addressDTO.setCountry_code(address.getCountry().getCode());
                }
                if (address.getState() != null) {
                    addressDTO.setState_id(address.getState().getObjectID());
                    addressDTO.setState(address.getState().getName());
                }
                billingAddresses.add(addressDTO);
            }
            addressInformationDTO.setBilling_addresses(billingAddresses);
        }

        if (edsCrmAccount.getMailingAddresses() != null && !edsCrmAccount.getMailingAddresses().isEmpty()) {
            ArrayList<AddressEditDTO> mailingAddresses = new ArrayList<>();
            for (EdsAddress address : edsCrmAccount.getMailingAddresses()) {
                AddressEditDTO addressDTO = new AddressEditDTO();
                addressDTO.setId(address.getObjectID());
                addressDTO.setName(address.getName());
                addressDTO.setAddress_line_1(address.getAddress());
                addressDTO.setAddress_line_2(address.getAddressb());
                addressDTO.setIs_primary(address.isPrimary());
                addressDTO.setCity(address.getCity());
                addressDTO.setPost_code(address.getZipCode());
                if (address.getCountry() != null) {
                    addressDTO.setCountry_id(address.getCountry().getObjectID());
                    addressDTO.setCountry_code(address.getCountry().getCode());
                }
                if (address.getState() != null) {
                    addressDTO.setState_id(address.getState().getObjectID());
                    addressDTO.setState(address.getState().getName());
                }
                mailingAddresses.add(addressDTO);
            }
            addressInformationDTO.setMailing_addresses(mailingAddresses);
        }

        return successResponse(addressInformationDTO);
    }

}
