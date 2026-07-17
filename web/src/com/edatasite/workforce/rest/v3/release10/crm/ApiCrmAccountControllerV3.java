package com.edatasite.workforce.rest.v3.release10.crm;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.client.server.app.ClientSupplierAccessService;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmAccountList;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CrmAccountTO;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.auth.service.ApiAuthService;
import com.edatasite.workforce.rest.v3.release10.core.BaseApiControllerV3;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.GoogleAuthTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.CrmAccountAddDTO;
import com.edatasite.workforce.rest.v3.release10.crm.dto.GymCrmAccountTO;
import com.edatasite.workforce.rest.v3.release10.crm.dto.contact.CompanyDto;
import com.edatasite.workforce.rest.v3.release10.crm.dto.crmAccount.CrmAccountSaveDto;
import com.edatasite.workforce.rest.v3.release10.crm.service.ApiContactService;
import com.edatasite.workforce.rest.v3.release10.crm.service.ApiGymCrmAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_STATUS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_STATUS_ACTIVE;
import static com.edatasite.workforce.gwt.core.server.app.ServerUtils.getAsSelectItem;

/**
 * User : Dilsh0d Madrahimov on 9/16/2019 5:35 PM
 */
@Tag(name = "CRM Accounts", description = "CRM Accounts Public API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiCrmAccountControllerV3 extends BaseApiControllerV3 {

    private static final Logger log = LoggerFactory.getLogger(ApiCrmAccountControllerV3.class);
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CRMService crmService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ClientSupplierAccessService clientSupplierAccessService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private ApiContactService apiContactService;
    @Autowired
    private ApiAuthService apiAuthService;
    @Autowired
    private ApiGymCrmAccountService apiGymCrmAccountService;
    @Autowired
    private BackendServiceLocal backendServiceLocal;
    @Autowired
    private TCService tcService;


    @RequestMapping(value = "/crm_accounts", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object create(@RequestBody CrmAccountAddDTO crmAccountAddDTO) throws RestException {

        if (crmAccountAddDTO == null) {
            throw new RestException(ERROR_MESSAGE, "Object can not be null or empty", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (crmAccountAddDTO.getAccount_information() == null) {
            throw new RestException(ERROR_MESSAGE, "Account main information can not be null or empty", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (crmAccountAddDTO.getAccount_information().getAccount_types() == null || crmAccountAddDTO.getAccount_information().getAccount_types().size() == 0) {
            throw new RestException("types must be one of SUPPLIER/CUSTOMER", "types must be one of SUPPLIER/CUSTOMER", REQUIRED, HttpStatus.BAD_REQUEST);
        }

//        for (String accountType : crmAccountAddDTO.getAccount_information().getAccount_types()) {
//            if (EntityTypeEnum.SUPPLIER.name().equalsIgnoreCase(accountType) || EntityTypeEnum.CUSTOMER.name().equalsIgnoreCase(accountType)) {
//                throw new RestException("types must be one of SUPPLIER/CUSTOMER", "types must be one of SUPPLIER/CUSTOMER", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
//            }
//        }

        if (StringUtils.isBlank(crmAccountAddDTO.getAccount_information().getName())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "name field is required.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SelectItem[] companyIndustures = getAsSelectItem(referenceManager.listReferences("_COMPANY_WORKAREA"), ServerUtils.REFERENCE);
        String indsutry = crmAccountAddDTO.getAccount_information().getIndsutry();
        boolean hasIndustry = false;
        for (SelectItem companyIndusture : companyIndustures) {
            if (companyIndusture.getName().equals(indsutry)) {
                hasIndustry = true;
                break;
            }
        }

        if (!hasIndustry) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "There is no sistem industry by given name", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        CrmAccountItem crmAccountItem = new CrmAccountItem();
        crmAccountItem.setName(crmAccountAddDTO.getAccount_information().getName());
        crmAccountItem.setEmail(crmAccountAddDTO.getAccount_information().getEmail());
        crmAccountItem.setPhone(crmAccountAddDTO.getAccount_information().getPhone());
        crmAccountItem.setFax(crmAccountAddDTO.getAccount_information().getFax());
        crmAccountItem.setWebsite(crmAccountAddDTO.getAccount_information().getWebsite());
        crmAccountItem.setIndustry(crmAccountAddDTO.getAccount_information().getIndsutry());

        ArrayList<SelectItem> accountTypes = new ArrayList<>();
        for (String accountTypeCode : crmAccountAddDTO.getAccount_information().getAccount_types()) {
            EdsReference accountType;
            if (EntityTypeEnum.SUPPLIER.name().equalsIgnoreCase(accountTypeCode)) {
                accountType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER);
            } else if (EntityTypeEnum.CUSTOMER.name().equalsIgnoreCase(accountTypeCode)) {
                accountType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER);
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "There is no sistem account type by given types", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            SelectItem accountTypeItem = accountType.getAsSelectItem();
            accountTypeItem.setSelected(true);
            accountTypes.add(accountTypeItem);
        }

        crmAccountItem.setAccountTypes(accountTypes.toArray(new SelectItem[0]));

        try {
            crmServiceLocal.saveAccount(crmAccountItem, null, null, false, false, false, true);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseData());
    }

    @PostMapping(path = "/crm/account", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public GymCrmAccountTO createCrmAccount(@RequestBody CrmAccountSaveDto requestBody,
                                            HttpServletRequest request,
                                            HttpServletResponse response) throws IOException {
        log.info("REST requestBody to create crm account");
        EdsCrmAccount crmAccountByEmail = crmAccountManager.getCrmAccountByEmail(requestBody.getEmail(), null);
        if (crmAccountByEmail != null) {
            return new GymCrmAccountTO(-1, -44, null, -1);
        }
        if (globalAuthJdbcSpringManager.existsOAuthToken(requestBody.getEmail(), requestBody.getGoogleUserId())) {
            GymCrmAccountTO crmAccount = apiGymCrmAccountService.gymCrmAccount(request, response, null, null, requestBody.getEmail(),null);
            if (crmAccount.getSessionId() != null) {
                return crmAccount;
            }
        }
        Integer savedCrmAccountId = crmServiceLocal.saveAccount(setValuesToRPC(requestBody), null, null, false, false, false, true);
        if (savedCrmAccountId < 0) {
            return new GymCrmAccountTO(savedCrmAccountId, -44, null, -1);
        }
        ContactListItem contactItem = toContactItem(requestBody);
        CrmAccountItem contactCrmAccount = new CrmAccountItem();
        contactCrmAccount.setObjectId(savedCrmAccountId);
        contactItem.setCrmAccount(contactCrmAccount);
        Integer contactId = contactServiceLocal.saveContact(contactItem, null, clientContactManager.getUser(), true, true);

        StudentItem studentItem = new StudentItem();
        studentItem.setContactID(contactId);
        studentItem.setCustomerID(savedCrmAccountId);
        studentItem.setActive(true);
        Integer studentId = tcService.saveGymStudentItem(studentItem);

        if (requestBody.getFromGoogle()) {
            GoogleAuthTO auth = new GoogleAuthTO();
            auth.setEmail(requestBody.getEmail());
            auth.setUser_id(requestBody.getGoogleUserId());
            auth.setAccess_token("");
            globalAuthJdbcSpringManager.createOauthToken(auth);
        }
        return apiGymCrmAccountService.gymCrmAccount(request, response, savedCrmAccountId, contactId, requestBody.getEmail(), studentId);
    }

    private ContactListItem toContactItem(CrmAccountSaveDto request) {
        ContactListItem item = new ContactListItem();
        item.setWorkEmail(request.getEmail());
        item.setWorkPhone(request.getPhone());
        item.setFirstName(request.getName());
        return item;
    }

    @DeleteMapping(path = "/crm/account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Integer> deleteCrmAccount(@PathVariable("id") Integer id) {
        log.info("REST request to delete crm account");
        return crmService.deleteCrmAccount(new ArrayList<>(List.of(id)), true);
    }

    @GetMapping(path = "/crm/account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CrmAccountItem getCrmAccountById(@PathVariable("id") Integer id) {
        log.info("REST request to get crm account");
        CrmAccountItem crmAccount = crmService.getAccount(id, Constants.CRM_ACCOUNT_TYPE);
        return crmAccount;
    }

    public CrmAccountItem setValuesToRPC(CrmAccountSaveDto request) {
        CrmAccountItem item = new CrmAccountItem();
        item.setObjectId(request.getObjectId());
        ArrayList<SelectItem> selectedOwners = request.getSelectedOwners().stream().map(SelectItem::new).collect(Collectors.toCollection(ArrayList::new));
        item.setSelectedOwners(selectedOwners);
        item.setParent(null);
        item.setName(request.getName());
        if (StringUtils.isNotBlank(request.getPrefix()) || request.getIntNumber() != null) {
            String code = request.getPrefix();
            if (request.getObjectId() == null) {
                code += request.getIntNumber();
            }
            item.setCode(code);
            item.setPrefix(request.getPrefix());
            item.setIntNumber(request.getIntNumber());
        }
        SelectItem[] accountTypes = request.getAccountTypes().stream().map(id -> new SelectItem(id, true)).toArray(SelectItem[]::new);
        item.setAccountTypes(accountTypes);
        item.setIndustryID(request.getIndustryId());
        item.setEmail(request.getEmail());
        item.setPhone(request.getPhone());
        item.setFax(request.getFax());
        item.setWebsite(request.getWebsite());
//        item.setAttachments(request.getAttachments());

        item.setCurrencyId(request.getCurrencyId());
        item.setCurrency(request.getCurrency());

        item.setPaymentMethodId(request.getPaymentMethodId());
        item.setPaymentMethod(request.getPaymentMethod());

        if (request.getAddress() != null) {
        ArrayList<Address> billingAddressList = new ArrayList<>();
        Address address = new Address();
        address.setName(request.getAddress().getName());
        address.setAddress(request.getAddress().getAddress_line_1());
        address.setAddressb(request.getAddress().getAddress_line_2());
        address.setCity(request.getAddress().getCity());
        address.setCountryId(request.getAddress().getCountry_id());
        address.setStateId(request.getAddress().getState_id());
        address.setPrimary(Optional.ofNullable(request.getAddress().getIs_primary()).orElse(false));
        address.setZipCode(request.getAddress().getPost_code());
        address.setEntityType(EdsAddress.ENTITY_TYPE_COMPANY);
        address.setRelationType(EdsAddress.HOME);
            item.setBillAddresses(new Address[]{address});
        }

        item.setVatNumber(request.getVatNumber());
        item.setRegistrationNumber(request.getRegistrationNumber());
        item.setCustomFields(CustomFieldsUtils.convertCustomFields(request.getCustomFields(), commonService.getCompanyCustomFields(ViewName.CrmAccount), null));

        if (request.getLogoId() != null) {
            item.setLogoId(request.getLogoId());
        }

        return item;
    }

    @RequestMapping(path = "/crm/accounts", method = RequestMethod.GET)
    public Object getCrmAccounts(@RequestParam(value = "search", required = false) String search) throws RestException {

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(0);
        filterParameter.setLimit(50);
        if (search != null) {
            search = search.replaceAll("%20", " ").trim();
            filterParameter.setSearchKey(search);
        }
        filterParameter.setDetectDuplicates(false);
        filterParameter.setWithImage(true);
        filterParameter.setSearchButton(true);
        filterParameter.setFromMobile(true);
        filterParameter.setCRM(true);

        CrmAccountList result = crmServiceLocal.getCrmAccounts(filterParameter);
        ArrayList<CrmAccountTO> accountList = new ArrayList<>();
        for (CrmAccountItem accountListItem : result.getList()) {
            CrmAccountTO accountItem = new CrmAccountTO();
            if (accountListItem.getName() != null) {
                accountItem.setName(accountListItem.getName().trim());
            }
            accountItem.setItem_id(accountListItem.getObjectId());
            accountItem.setAvatar_image(accountListItem.getLogoUrl());

            accountList.add(accountItem);
        }
        return ResultTO.success(accountList);
    }


    @Operation(summary = "Get Account list")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Accounts"))
    @RequestMapping(path = "/crm/account/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getCrmAccountList(@RequestBody ListParamsDTO params,
                                    @RequestParam(value = "search", required = false) String search) throws RestException {

        ListingFilterParameter filterParameter = ListingFilterHelperV3.createListingFilter(params, ListPanelType.CrmAccountListPanel);
        if (search != null) {
            search = search.replaceAll("%20", " ").trim();
            filterParameter.setSearchKey(search);
        }
        filterParameter.setDetectDuplicates(false);
        filterParameter.setWithImage(true);
        filterParameter.setSearchButton(true);
        filterParameter.setFromMobile(true);
        filterParameter.setCRM(true);
        filterParameter.setSearchButton(false);

        CrmAccountList result = crmServiceLocal.getCrmAccounts(filterParameter);
        ArrayList<CompanyDto> companyDtoArrayList = new ArrayList<>();
        for (CrmAccountItem accountListItem : result.getList()) {
            CompanyDto companyDto = new CompanyDto();
            companyDto.setId(accountListItem.getObjectId());
            if (accountListItem.getName() != null) {
                companyDto.setName(accountListItem.getName().trim());
            }
            companyDto.setEmail(accountListItem.getEmail());
            companyDto.setEmailId(accountListItem.getEmailId());

            companyDto.setPhone(accountListItem.getPhone());
            companyDto.setPhoneId(accountListItem.getPhoneId());
            companyDto.setIndustry(accountListItem.getIndustry());
            companyDto.setIndustryID(accountListItem.getIndustryID());
            companyDto.setIndustryCode(accountListItem.getIndustryCode());
            companyDto.setOtherIndustry(accountListItem.getOtherIndustry());
            companyDto.setOwnerName(accountListItem.getOwnerName());
            companyDto.setOwnerNames(accountListItem.getOwnerNames());
            companyDto.setOwnerID(accountListItem.getOwnerID());
            companyDto.setOwnerItems(accountListItem.getOwnerItems());
            companyDto.setBillAddresses(accountListItem.getBillAddresses());
            companyDto.setNumber(accountListItem.getNumber());
            companyDto.setNumberId(accountListItem.getNumberId());

            companyDtoArrayList.add(companyDto);
        }
        return ResultTO.success(companyDtoArrayList);
    }

    @Operation(summary = "Generate account number")
    @GetMapping(value = "/crm/accounts/number", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<String> generateAccountNumber() {
        NumberData numberData = allInOneService.generateAccountNumberData(null);
        return ResultTO.success(numberData.getNumberString());
    }

    @Operation(summary = "Get owners list by permission")
    @GetMapping(value = "/crm/accounts/owners", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<SelectItem[]> getOwnersListByPermission(@RequestParam String permissionCode) {
        SelectItem[] ownersList = crmServiceLocal.getOwnersListByPermission(permissionCode);
        return ResultTO.success(ownersList);
    }

    @PatchMapping(path = "/crm/account/active")
    public GymCrmAccountTO activateCrmAccount(@RequestParam("contactId") Integer contactId,
                                              @RequestParam(value = "googleUserId", required = false) String googleUserId,
                                              HttpServletRequest request,
                                              HttpServletResponse response) throws IOException {
        log.info("REST request to activate crm account : {}", contactId);
        EdsCrmContact edsCrmContact = crmContactManager.get(contactId);
        if (globalAuthJdbcSpringManager.existsOAuthToken(edsCrmContact.getPrimaryEmail(), googleUserId)) {
            EdsReference activeStatus = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE);
            Integer clientContactId = clientSupplierAccessService.enableAccess(contactId, false, false);
            apiContactService.updateClientContact(clientContactId, activeStatus);
            return apiGymCrmAccountService.gymCrmAccount(request, response, null, null, edsCrmContact.getPrimaryEmail(),null);
        }
        contactService.enableAccess(contactId, true);
        return apiGymCrmAccountService.gymCrmAccount(request, response, null, null, edsCrmContact.getPrimaryEmail(),null);
    }


    @GetMapping(path = "/crm/account/me")
    public CrmAccountItem getMyCrmAccount() {
        EdsUser currentUser = crmAccountManager.getUser();
        log.info("REST request to get my crm account: {}", currentUser.getObjectID());
        return Optional.ofNullable(clientContactManager.get(currentUser.getObjectID()))
                .map(EdsClientContact::getCrmContact)
                .map(EdsCrmContact::getCrmAccount)
                .map(EdsCrmAccount::getObjectID)
                .map(e -> crmService.getAccount(e, Constants.CRM_ACCOUNT_TYPE))
                .orElse(null);
    }
    @RequestMapping(path = "/public-data", method = RequestMethod.GET, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ObjectNode> getCompanyByKey(@RequestParam Integer companyId, @RequestParam(required = false) Boolean isSaveUser) throws RestException {

        String data=backendServiceLocal.getInsertPublicData(companyId);
        ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper
        ObjectNode jsonResponse = objectMapper.createObjectNode(); // JSON object for response


        if (data!=null){
            String regex = "SELECT saveUserAuthenticationData\\s*\\(.*?\\);";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(data);

            if (isSaveUser!=null && isSaveUser ) {
                while (matcher.find()) {
                    jsonResponse.put("query",matcher.group());
                }
            }else {
                while (matcher.find()) {
                    jsonResponse.put("query", data.replaceAll(regex,"").trim());
                }
            }
        }
        return ResultTO.success(jsonResponse);
    }

}
