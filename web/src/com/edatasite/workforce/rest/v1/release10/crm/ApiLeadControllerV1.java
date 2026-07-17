package com.edatasite.workforce.rest.v1.release10.crm;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.gwtupload.UUID;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.GwtUploadServlet;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.rest.base.enums.ContactParamEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.AddressTO;
import com.edatasite.workforce.rest.base.to.ContactParamTO;
import com.edatasite.workforce.rest.base.to.ContactTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.QuickLeadTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import com.edatasite.workforce.utils.EdsContextParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 09/19/2017 10:25 AM
 */
@Tag(name = "Lead", description = "Lead API")
@RestController
@RequestMapping(value = "/lead", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiLeadControllerV1 extends BaseApiControllerV1 {

    static String uploadFileName = null;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private ServletContext servletContext;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setContactType(CrmConstants.TYPE_LEAD_CONTACT);
        filterParameter.setWithImage(true);
        ListResult<ContactListItem> leadList = crmServiceLocal.getNewLeads(filterParameter);
        ArrayList<ContactTO> result = new ArrayList<>();
        for (ContactListItem item : leadList.getList()) {
            result.add(new ContactTO(item));
        }
        return successResponse(new ListResultTO<>(leadList.getTotal(), result));
    }

    @RequestMapping(value = "/quickAdd", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object quickAdd(@RequestBody QuickLeadTO leadTO) {
        if (leadTO == null || leadTO.getEmail() == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }

        HashMap<Integer, ArrayList<String>> paramMap = new HashMap<>();
        ContactListItem lead = new ContactListItem();
        lead.setFirstName(leadTO.getFirstName());
        lead.setLastName(leadTO.getLastName());
        lead.setPrimaryEmail(leadTO.getEmail());
        lead.setContactType(CrmConstants.TYPE_LEAD_CONTACT);

        ArrayList<String> emails = new ArrayList<>();
        emails.add(leadTO.getEmail());
        paramMap.put(ContactParamEnum.WORK.getId(), emails);
        lead.setEmails(paramMap);

        Integer result;
        try {
            result = contactServiceLocal.saveContact(lead, null, false);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_SAVE);
        }
        if (result == -2 || result == -1) {
            return this.errorResponse(ERROR_FAILED_SAVE);
        }
        return this.successResponse(SUCCESS_SAVE, result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody ContactTO contactTO) {
        if (contactTO == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        Integer result;

        contactTO.setContactType(CrmConstants.TYPE_LEAD_CONTACT);
        ContactListItem contactListItem = contactTO.wrap(contactTO);
        contactListItem.setContactType(CrmConstants.TYPE_LEAD_CONTACT);

        if (contactTO.getEmails() != null) {
            setContactParam(contactTO.getEmails(), contactListItem, Constants.CONTACT_EMAILS);
        }
        if (contactTO.getPhones() != null) {
            setContactParam(contactTO.getPhones(), contactListItem, Constants.CONTACT_PHONES);
        }
        if (contactTO.getWebAddresses() != null) {
            setContactParam(contactTO.getWebAddresses(), contactListItem, Constants.CONTACT_WEBSITES);
        }
        if (contactTO.getImAddresses() != null) {
            setContactParam(contactTO.getImAddresses(), contactListItem, Constants.CONTACT_IMADDRESSES);
        }

        try {
            result = contactServiceLocal.saveContact(contactListItem, null, false);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_SAVE);
        }
        if (result == -2 || result == -1) {
            return this.errorResponse(ERROR_FAILED_SAVE);
        }
        if (result > 0) {
            if (uploadFileName != null) {
                ArrayList<FileResource> files = new ArrayList<>();
                FileResource fileResource = new FileResource();
                fileResource.setName(uploadFileName);
                fileResource.setPath(GwtUploadServlet.realPath + uploadFileName);
                fileResource.setUploadType(EdsContextParams.getUploadType());
                files.add(fileResource);

                FolderResource folderResource = documentsServiceLocal.getFolderResource(Constants.F_LEAD, result);
                try {
                    folderResource.setEntityId(result);
                    ArrayList<FileResource> fileResources = documentsServiceLocal.uploadAllFiles(files, folderResource, files.get(0).getDescription());
                    if (fileResources.size() > 1) {
                        commonServiceLocal.saveCrmContactImageUrl(fileResources.get(1).getBodyId(), result);
                    }
                    uploadFileName = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    uploadFileName = null;
                }
            }
        }

        return this.successResponse(SUCCESS_SAVE, result);

    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) {
        if (id == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        ContactListItem contactItem = crmServiceLocal.getLead(id);

        if (contactItem == null || contactItem.getObjectId() == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        ContactTO contactTO = new ContactTO(contactItem, false);
        contactTO.setPhones(contactServiceLocal.getContactParams(id, Constants.CONTACT_PHONES));
        contactTO.setWebAddresses(contactServiceLocal.getContactParams(id, Constants.CONTACT_WEBSITES));
        contactTO.setEmails(contactServiceLocal.getContactParams(id, Constants.CONTACT_EMAILS));
        contactTO.setImAddresses(contactServiceLocal.getContactParams(id, Constants.CONTACT_IMADDRESSES));

        return this.successResponse(contactTO);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "id") Integer id) {
        if (id == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        ArrayList<Integer> ids = new ArrayList<>(1);
        ids.add(id);
        try {
            contactServiceLocal.deleteContacts(ids, null, true);
        } catch (Exception e) {
            e.printStackTrace();
            return this.errorResponse(ERROR_FAIL_DELETE);
        }
        return successResponse(SUCCESS_DELETE);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object update(@PathVariable(value = "id") Integer id, @RequestBody ContactTO contactTO) {
        if (id == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (contactTO == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        contactTO.setId(id);
        return add(contactTO);
    }

    @Operation(summary = "Lead Image Upload")
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object uploadProfileImage(@RequestParam("file") MultipartFile fileForUpload) {
        if (fileForUpload == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        String originalFileName = fileForUpload.getOriginalFilename().replace("%20", " ");

        if (GwtUploadServlet.realPath == null) {
            GwtUploadServlet.realPath = servletContext.getRealPath("uploads") + "/";
        }

        String fileName = UUID.uuid() + "_upld_" + originalFileName;

        uploadFileName = fileName;

        try {
            String filename = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            String url = GwtUploadServlet.realPath + filename;
            final File file = new File(url);
            file.getParentFile().mkdirs();
            FileOutputStream os = new FileOutputStream(file);
            IOUtils.copy(fileForUpload.getInputStream(), os);
            fileForUpload.getInputStream().close();
            os.flush();
            os.close();
            return successResponse("Successfully uploaded.");
        } catch (IOException e) {
            e.printStackTrace();
            uploadFileName = null;
            return errorResponse("Upload failed.");
        }
    }


    @RequestMapping(value = "/owners", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getOwners() {
        return successResponse(WrapUtils.wrapUserTOs(crmServiceLocal.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE)));
    }

    @RequestMapping(value = "/assignees", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getAssignees() {
        return getOwners();
    }

    @RequestMapping(value = "/backupAssignees", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getBackupAssignees() {
        return getOwners();
    }

    @Operation(summary = "Company Name", description = "Company Name is CRM Account")
    @RequestMapping(value = "/accounts", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getAccounts(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setCRM(true);
        filterParameter.setAvoidType(Constants.SUPPLIER);

        return successResponse(WrapUtils.wrapSelectItemList(allInOneServiceLocal.getLookUpItems(filterParameter, CrmConstants.CRM_ACCOUNT_ID, null)));
    }

    @RequestMapping(value = "/accountTypes", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getAccountTypes() {
        return successResponse(WrapUtils.wrapCheckListItemTOs(contactServiceLocal.getAccountTypes()));
    }

    @Operation(summary = "Selected Account Types")
    @RequestMapping(value = "/{id}/accountTypes", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getSelectedAccountTypes(@PathVariable(value = "id") Integer id) {
        if (id == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        ContactListItem contactItem = contactServiceLocal.getContact(id, false);
        if (contactItem == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return this.successResponse(WrapUtils.wrapCheckListItemTOs(contactItem.getCrmAccount().getAccountTypes()));
    }

    @RequestMapping(value = "/statuses", method = RequestMethod.GET)
    public Object getStatuses() {
        return successResponse(WrapUtils.wrapSelectItemTOs(referenceManager.listReferences(EdsCrmContact._LEAD_STATUS)));
    }

    @RequestMapping(value = "/sources", method = RequestMethod.GET)
    public Object getSources() {
        return successResponse(WrapUtils.wrapSelectItemTOs(referenceManager.listReferences(EdsCrmContact._LEAD_SOURCE)));
    }

    @RequestMapping(value = "/campaigns", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getCampaigns(@RequestBody MListingFilterParameter mListingFilterParameter) {
        if (mListingFilterParameter == null) {
            mListingFilterParameter = new MListingFilterParameter();
        }
        ListingFilterParameter filterParameters = mListingFilterParameter.convertToFilterParameters();
        filterParameters.setCRM(true);
        return successResponse(WrapUtils.wrapUserTOs(allInOneServiceLocal.getLookUpItems(filterParameters, LookUpConstants.CRM_CAMPAIGN_ID, null)));
    }

    @RequestMapping(value = "/industries", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getIndustries() {
        return successResponse(WrapUtils.wrapSelectItemTOs(contactServiceLocal.getContactSelectItems(Constants._COMPANY_WORKAREA)));
    }

    @RequestMapping(value = "/organizationTypes", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getOrganizationTypes() {
        return successResponse(WrapUtils.wrapSelectItemTOs(contactServiceLocal.getContactSelectItems(Constants.CONTACT_ORGANIZATION_TYPES)));
    }

    @RequestMapping(value = "/numberOfEmployees", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getNumberOfEmployees() {
        return successResponse(WrapUtils.wrapSelectItemTOs(contactServiceLocal.getContactSelectItems(Constants.NUMBER_OF_EMPLOYEES)));
    }

    @RequestMapping(value = "/ratings", method = RequestMethod.GET)
    public Object getRatings() {
        return successResponse(WrapUtils.wrapSelectItemTOs(referenceManager.listReferences(EdsCrmContact._LEAD_RATING)));
    }

    @RequestMapping(value = "/annualRevenues", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getAnnualRevenues() {
        return successResponse(WrapUtils.wrapSelectItemTOs(contactServiceLocal.getContactSelectItems(Constants.ANNUAL_REVENUE)));
    }

    @RequestMapping(value = "/countries", method = RequestMethod.GET)
    public Object getCountries() {
        return successResponse(WrapUtils.wrapSelectItemTOs(commonServiceLocal.getCountries()));
    }

    @Operation(summary = "Get States By Country")
    @RequestMapping(value = "/states/{countryId}", method = RequestMethod.GET)
    public Object getStates(@PathVariable(value = "countryId") Integer countryId) {
        return successResponse(WrapUtils.wrapSelectItemTOs(commonServiceLocal.getRegions(countryId)));
    }

    @RequestMapping(value = "/{id}/addresses", method = RequestMethod.GET)
    public Object getAddresses(@PathVariable(value = "id") Integer id) {
        if (id == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return successResponse(contactServiceLocal.getAddresses(id));
    }

    @RequestMapping(value = "/{id}/addresses", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object saveAddresses(@RequestBody List<AddressTO> addressTOs, @PathVariable(value = "id") Integer leadId) {
        if (leadId == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (addressTOs == null) {
            return errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        ContactListItem contactItem = contactServiceLocal.getContact(leadId, false);
        if (contactItem == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        ArrayList<Address> addresses = new ArrayList<>(addressTOs.size());
        for (AddressTO addressTO : addressTOs) {
            Address address = new Address();
            address.setPrimary(addressTO.getIsPrimary() != null ? addressTO.getIsPrimary() : false);
            address.setName(addressTO.getName());
            address.setAddress(addressTO.getAddress1());
            address.setAddressb(addressTO.getAddress2());
            address.setCity(addressTO.getCity());
            if (addressTO.getCountry() != null) {
                address.setCountryId(addressTO.getCountry().getId());
                address.setCountry(addressTO.getCountry().getName());
                address.setCountryCode(addressTO.getCountry().getCode());
            }
            if (addressTO.getState() != null) {
                address.setStateId(addressTO.getState().getId());
                address.setState(addressTO.getState().getName());
            }
            address.setEntityID(leadId);
            address.setEntityType(EdsAddress.ENTITY_TYPE_CONTACT);
            address.setRelationType(addressTO.getType() != null ? addressTO.getType().getId() : EdsAddress.HOME);
        }
        contactItem.setAddresses(addresses);
        try {
            contactServiceLocal.saveContact(contactItem, null, false);
            return successResponse(SUCCESS_SAVE);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_SAVE);
        }
    }

    @RequestMapping(value = "/{id}/phones", method = RequestMethod.GET)
    public Object getPhones(@PathVariable(value = "id") Integer id) {
        return successResponse(contactServiceLocal.getContactParams(id, Constants.CONTACT_PHONES));
    }

    @RequestMapping(value = "/{id}/emails", method = RequestMethod.GET)
    public Object getEmails(@PathVariable(value = "id") Integer id) {
        return successResponse(contactServiceLocal.getContactParams(id, Constants.CONTACT_EMAILS));
    }

    @RequestMapping(value = "/{id}/webAddresses", method = RequestMethod.GET)
    public Object getWebAddresses(@PathVariable(value = "id") Integer id) {
        return successResponse(contactServiceLocal.getContactParams(id, Constants.CONTACT_WEBSITES));
    }

    @RequestMapping(value = "/{id}/imAddresses", method = RequestMethod.GET)
    public Object getImAddresses(@PathVariable(value = "id") Integer id) {
        return successResponse(contactServiceLocal.getContactParams(id, Constants.CONTACT_IMADDRESSES));
    }

    @RequestMapping(value = "/{id}/leadParams", method = RequestMethod.GET)
    public Object getLeadAllParams(@PathVariable(value = "id") Integer id) {
        if (id == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return successResponse(contactServiceLocal.getContactParams(id, null));
    }

    @RequestMapping(value = "/{id}/phones", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object savePhones(@PathVariable(value = "id") Integer id, @RequestBody List<ContactParamTO> phones) {
        return saveContactParam(phones, id, Constants.CONTACT_PHONES);
    }

    @RequestMapping(value = "/{id}/emails", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object saveEmails(@PathVariable(value = "id") Integer id, @RequestBody List<ContactParamTO> emails) {
        return saveContactParam(emails, id, Constants.CONTACT_EMAILS);
    }

    @RequestMapping(value = "/{id}/webAddresses", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object saveWebAddresses(@PathVariable(value = "id") Integer id, @RequestBody List<ContactParamTO> webAddresses) {
        return saveContactParam(webAddresses, id, Constants.CONTACT_WEBSITES);
    }

    @RequestMapping(value = "/{id}/imAddresses", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object saveImAddresses(@PathVariable(value = "id") Integer id, @RequestBody List<ContactParamTO> imAddresses) {
        return saveContactParam(imAddresses, id, Constants.CONTACT_IMADDRESSES);
    }

    @RequestMapping(value = "/{id}/contactParams", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object saveContactParams(@PathVariable(value = "id") Integer id, @RequestBody HashMap<String, ArrayList<ContactParamTO>> paramsMap) {
        if (id == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (paramsMap == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        ContactListItem contactItem = contactServiceLocal.getContact(id, false);
        contactItem.setFromAPI(false);

        List<ContactParamTO> phones = paramsMap.get(ContactParamEnum.PHONES.getCode().toLowerCase());
        List<ContactParamTO> emails = paramsMap.get(ContactParamEnum.EMAILS.getCode().toLowerCase());
        List<ContactParamTO> imAddresses = paramsMap.get(ContactParamEnum.IM_ADDRESSES.getCode().toLowerCase());
        List<ContactParamTO> webAddresses = paramsMap.get(ContactParamEnum.WEB_ADDRESSES.getCode().toLowerCase());

        HashMap<Integer, ArrayList<String>> paramMap = new HashMap<>();

        if (phones != null && !phones.isEmpty()) {
            ArrayList<String> home = new ArrayList<>();
            ArrayList<String> work = new ArrayList<>();
            ArrayList<String> other = new ArrayList<>();
            ArrayList<String> mobile = new ArrayList<>();
            ArrayList<String> homeFax = new ArrayList<>();
            ArrayList<String> workFax = new ArrayList<>();
            ArrayList<String> pager = new ArrayList<>();
            ArrayList<String> extension = new ArrayList<>();

            for (ContactParamTO paramTO : phones) {
                if (ContactParamEnum.HOME.getId().equals(paramTO.getType().getId())) {
                    home.add(paramTO.getName());
                } else if (ContactParamEnum.WORK.getId().equals(paramTO.getType().getId())) {
                    work.add(paramTO.getName());
                } else if (ContactParamEnum.MOBILE.getId().equals(paramTO.getType().getId())) {
                    mobile.add(paramTO.getName());
                } else if (ContactParamEnum.HOME_FAX.getId().equals(paramTO.getType().getId())) {
                    homeFax.add(paramTO.getName());
                } else if (ContactParamEnum.WORK_FAX.getId().equals(paramTO.getType().getId())) {
                    workFax.add(paramTO.getName());
                } else if (ContactParamEnum.PAGER.getId().equals(paramTO.getType().getId())) {
                    pager.add(paramTO.getName());
                } else if (ContactParamEnum.OTHER.getId().equals(paramTO.getType().getId())) {
                    other.add(paramTO.getName());
                } else if (ContactParamEnum.EXTENSION.getId().equals(paramTO.getType().getId())) {
                    extension.add(paramTO.getName());
                }
                if (paramTO.getIsPrimary()) {
                    contactItem.setPrimaryPhone(paramTO.getName());
                }
            }
            paramMap.put(ContactParamEnum.HOME.getId(), home);
            paramMap.put(ContactParamEnum.WORK.getId(), work);
            paramMap.put(ContactParamEnum.MOBILE.getId(), mobile);
            paramMap.put(ContactParamEnum.HOME_FAX.getId(), homeFax);
            paramMap.put(ContactParamEnum.WORK_FAX.getId(), workFax);
            paramMap.put(ContactParamEnum.PAGER.getId(), pager);
            paramMap.put(ContactParamEnum.OTHER.getId(), other);
            paramMap.put(ContactParamEnum.EXTENSION.getId(), extension);

            contactItem.setPhones(paramMap);
        }

        if (emails != null && !emails.isEmpty()) {
            paramMap = new HashMap<>();
            ArrayList<String> home = new ArrayList<>();
            ArrayList<String> work = new ArrayList<>();
            ArrayList<String> other = new ArrayList<>();
            for (ContactParamTO paramTO : emails) {
                if (ContactParamEnum.HOME.getId().equals(paramTO.getType().getId())) {
                    home.add(paramTO.getName());
                } else if (ContactParamEnum.WORK.getId().equals(paramTO.getType().getId())) {
                    work.add(paramTO.getName());
                } else if (ContactParamEnum.OTHER.getId().equals(paramTO.getType().getId())) {
                    other.add((paramTO.getName()));
                }
                if (paramTO.getIsPrimary()) {
                    contactItem.setPrimaryEmail(paramTO.getName());
                }
            }
            paramMap.put(ContactParamEnum.HOME.getId(), home);
            paramMap.put(ContactParamEnum.WORK.getId(), work);
            paramMap.put(ContactParamEnum.OTHER.getId(), other);

            contactItem.setEmails(paramMap);
        }

        if (imAddresses != null && !imAddresses.isEmpty()) {
            paramMap = new HashMap<>();
            ArrayList<String> gtalk = new ArrayList<>();
            ArrayList<String> aim = new ArrayList<>();
            ArrayList<String> yahoo = new ArrayList<>();
            ArrayList<String> skype = new ArrayList<>();
            ArrayList<String> qq = new ArrayList<>();
            ArrayList<String> msn = new ArrayList<>();
            ArrayList<String> icq = new ArrayList<>();
            ArrayList<String> jabber = new ArrayList<>();

            for (ContactParamTO paramTO : imAddresses) {
                if (ContactParamEnum.GOOGLE_TALK.getId().equals(paramTO.getType().getId())) {
                    gtalk.add(paramTO.getName());
                } else if (ContactParamEnum.AIM.getId().equals(paramTO.getType().getId())) {
                    aim.add(paramTO.getName());
                } else if (ContactParamEnum.YAHOO.getId().equals(paramTO.getType().getId())) {
                    yahoo.add(paramTO.getName());
                } else if (ContactParamEnum.SKYPE.getId().equals(paramTO.getType().getId())) {
                    skype.add(paramTO.getName());
                } else if (ContactParamEnum.QQ.getId().equals(paramTO.getType().getId())) {
                    qq.add(paramTO.getName());
                } else if (ContactParamEnum.MSN.getId().equals(paramTO.getType().getId())) {
                    msn.add(paramTO.getName());
                } else if (ContactParamEnum.ICQ.getId().equals(paramTO.getType().getId())) {
                    icq.add(paramTO.getName());
                } else if (ContactParamEnum.JABBER.getId().equals(paramTO.getType().getId())) {
                    jabber.add(paramTO.getName());
                }
            }
            paramMap.put(ContactParamEnum.GOOGLE_TALK.getId(), gtalk);
            paramMap.put(ContactParamEnum.AIM.getId(), aim);
            paramMap.put(ContactParamEnum.YAHOO.getId(), yahoo);
            paramMap.put(ContactParamEnum.SKYPE.getId(), skype);
            paramMap.put(ContactParamEnum.QQ.getId(), qq);
            paramMap.put(ContactParamEnum.MSN.getId(), msn);
            paramMap.put(ContactParamEnum.ICQ.getId(), icq);
            paramMap.put(ContactParamEnum.JABBER.getId(), jabber);

            contactItem.setImAddresses(paramMap);
        }
        if (webAddresses != null && !webAddresses.isEmpty()) {
            paramMap = new HashMap<>();
            ArrayList<String> home = new ArrayList<>();
            ArrayList<String> work = new ArrayList<>();
            ArrayList<String> other = new ArrayList<>();
            ArrayList<String> homePage = new ArrayList<>();
            ArrayList<String> ftp = new ArrayList<>();
            ArrayList<String> blog = new ArrayList<>();
            ArrayList<String> profile = new ArrayList<>();
            for (ContactParamTO paramTO : webAddresses) {
                if (ContactParamEnum.HOME.getId().equals(paramTO.getType().getId())) {
                    home.add(paramTO.getName());
                } else if (ContactParamEnum.WORK.getId().equals(paramTO.getType().getId())) {
                    work.add(paramTO.getName());
                } else if (ContactParamEnum.HOME_PAGE.getId().equals(paramTO.getType().getId())) {
                    homePage.add(paramTO.getName());
                } else if (ContactParamEnum.FTP.getId().equals(paramTO.getType().getId())) {
                    ftp.add(paramTO.getName());
                } else if (ContactParamEnum.BLOG.getId().equals(paramTO.getType().getId())) {
                    blog.add(paramTO.getName());
                } else if (ContactParamEnum.PROFILE.getId().equals(paramTO.getType().getId())) {
                    profile.add(paramTO.getName());
                } else if (ContactParamEnum.OTHER.getId().equals(paramTO.getType().getId())) {
                    other.add(paramTO.getName());
                }
            }

            paramMap.put(ContactParamEnum.HOME.getId(), home);
            paramMap.put(ContactParamEnum.WORK.getId(), work);
            paramMap.put(ContactParamEnum.HOME_PAGE.getId(), homePage);
            paramMap.put(ContactParamEnum.FTP.getId(), ftp);
            paramMap.put(ContactParamEnum.BLOG.getId(), blog);
            paramMap.put(ContactParamEnum.PROFILE.getId(), profile);
            paramMap.put(ContactParamEnum.OTHER.getId(), other);

            contactItem.setWebAddresses(paramMap);
        }
        try {
            contactServiceLocal.saveContact(contactItem, null, false);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_SAVE);
        }

        return successResponse(contactServiceLocal.getContactParams(id));

    }

    @RequestMapping(value = "/emails", method = RequestMethod.GET)
    public Object getEmails() {
        return getContactStaticParams(ContactParamEnum.EMAILS);
    }

    @RequestMapping(value = "/phones", method = RequestMethod.GET)
    public Object getPhones() {
        return getContactStaticParams(ContactParamEnum.PHONES);
    }

    @RequestMapping(value = "/imAddresses", method = RequestMethod.GET)
    public Object getImAddresses() {
        return getContactStaticParams(ContactParamEnum.IM_ADDRESSES);
    }

    @RequestMapping(value = "/webAddresses", method = RequestMethod.GET)
    public Object getWebAddresses() {
        return getContactStaticParams(ContactParamEnum.WEB_ADDRESSES);
    }

    @RequestMapping(value = "/addressTypes", method = RequestMethod.GET)
    public Object getAddressTypes() {
        return getContactStaticParams(ContactParamEnum.ADDRESS_TYPES);
    }

    private Object getContactStaticParams(ContactParamEnum paramTypeEnum) {
        return switch (paramTypeEnum) {
            case EMAILS -> successResponse(new ArrayList<SelectItemTO>() {{
                this.add(new SelectItemTO(ContactParamEnum.HOME.getId(), ContactParamEnum.HOME.getName(), ContactParamEnum.HOME.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.WORK.getId(), ContactParamEnum.WORK.getName(), ContactParamEnum.WORK.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.OTHER.getId(), ContactParamEnum.OTHER.getName(), ContactParamEnum.OTHER.getCode(), null));
            }});
            case PHONES -> successResponse(new ArrayList<SelectItemTO>() {{
                this.add(new SelectItemTO(ContactParamEnum.HOME.getId(), ContactParamEnum.HOME.getName(), ContactParamEnum.HOME.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.WORK.getId(), ContactParamEnum.WORK.getName(), ContactParamEnum.WORK.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.MOBILE.getId(), ContactParamEnum.MOBILE.getName(), ContactParamEnum.MOBILE.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.HOME_FAX.getId(), ContactParamEnum.HOME_FAX.getName(), ContactParamEnum.HOME_FAX.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.WORK_FAX.getId(), ContactParamEnum.WORK_FAX.getName(), ContactParamEnum.WORK_FAX.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.PAGER.getId(), ContactParamEnum.PAGER.getName(), ContactParamEnum.PAGER.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.OTHER.getId(), ContactParamEnum.OTHER.getName(), ContactParamEnum.OTHER.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.EXTENSION.getId(), ContactParamEnum.EXTENSION.getName(), ContactParamEnum.EXTENSION.getCode(), null));
            }});
            case IM_ADDRESSES -> successResponse(new ArrayList<SelectItemTO>() {{
                this.add(new SelectItemTO(ContactParamEnum.GOOGLE_TALK.getId(), ContactParamEnum.GOOGLE_TALK.getName(), ContactParamEnum.GOOGLE_TALK.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.AIM.getId(), ContactParamEnum.AIM.getName(), ContactParamEnum.AIM.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.YAHOO.getId(), ContactParamEnum.YAHOO.getName(), ContactParamEnum.YAHOO.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.SKYPE.getId(), ContactParamEnum.SKYPE.getName(), ContactParamEnum.SKYPE.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.QQ.getId(), ContactParamEnum.QQ.getName(), ContactParamEnum.QQ.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.MSN.getId(), ContactParamEnum.MSN.getName(), ContactParamEnum.MSN.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.ICQ.getId(), ContactParamEnum.ICQ.getName(), ContactParamEnum.ICQ.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.JABBER.getId(), ContactParamEnum.JABBER.getName(), ContactParamEnum.JABBER.getCode(), null));
            }});
            case WEB_ADDRESSES -> successResponse(new ArrayList<SelectItemTO>() {{
                this.add(new SelectItemTO(ContactParamEnum.HOME.getId(), ContactParamEnum.HOME.getName(), ContactParamEnum.HOME.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.WORK.getId(), ContactParamEnum.WORK.getName(), ContactParamEnum.WORK.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.HOME_PAGE.getId(), ContactParamEnum.HOME_PAGE.getName(), ContactParamEnum.HOME_PAGE.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.FTP.getId(), ContactParamEnum.FTP.getName(), ContactParamEnum.FTP.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.BLOG.getId(), ContactParamEnum.BLOG.getName(), ContactParamEnum.BLOG.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.PROFILE.getId(), ContactParamEnum.PROFILE.getName(), ContactParamEnum.PROFILE.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.OTHER.getId(), ContactParamEnum.OTHER.getName(), ContactParamEnum.OTHER.getCode(), null));
            }});
            case RELATIONSHIPS -> successResponse(WrapUtils.wrapSelectItemTOs(contactServiceLocal.getRelationships()));
            case ADDRESS_TYPES -> successResponse(new ArrayList<SelectItemTO>() {{
                this.add(new SelectItemTO(ContactParamEnum.HOME.getId(), ContactParamEnum.HOME.getName(), ContactParamEnum.HOME.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.WORK.getId(), ContactParamEnum.WORK.getName(), ContactParamEnum.WORK.getCode(), null));
                this.add(new SelectItemTO(ContactParamEnum.OTHER.getId(), ContactParamEnum.OTHER.getName(), ContactParamEnum.OTHER.getCode(), null));
            }});
            default -> errorResponse(ERROR_RESOURCE_NOT_FOUND);
        };
    }

    @RequestMapping(value = "/{id}/email/{emailId}", method = RequestMethod.DELETE)
    public Object deleteEmail(@PathVariable(value = "id") Integer id,
                              @PathVariable(value = "emailId") Integer emailId) {
        return deleteContactParam(id, emailId);
    }

    @RequestMapping(value = "/{id}/phone/{phoneId}", method = RequestMethod.DELETE)
    public Object deletePhone(@PathVariable(value = "id") Integer id,
                              @PathVariable(value = "phoneId") Integer phoneId) {
        return deleteContactParam(id, phoneId);
    }

    @RequestMapping(value = "/{id}/webAddress/{webAddressId}", method = RequestMethod.DELETE)
    public Object deleteWebAddress(@PathVariable(value = "id") Integer id,
                                   @PathVariable(value = "webAddressId") Integer webAddressId) {
        return deleteContactParam(id, webAddressId);
    }

    @RequestMapping(value = "/{id}/imAddress/{imAddressId}", method = RequestMethod.DELETE)
    public Object deleteImAddress(@PathVariable(value = "id") Integer id,
                                  @PathVariable(value = "imAddressId") Integer imAddressId) {
        return deleteContactParam(id, imAddressId);
    }

    private Object deleteContactParam(Integer id, Integer paramId) {
        if (id == null || paramId == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        try {
            contactServiceLocal.deleteContactParam(paramId);
            return successResponse(SUCCESS_DELETE);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAIL_DELETE);
        }
    }

    private void setContactParam(List<ContactParamTO> contactParamTOs, ContactListItem contactItem, Integer paramType) {

        HashMap<Integer, ArrayList<String>> paramMap = new HashMap<>();

        ArrayList<String> home = new ArrayList<>();
        ArrayList<String> work = new ArrayList<>();
        ArrayList<String> other = new ArrayList<>();

        switch (paramType) {
            case Constants.CONTACT_EMAILS -> {
                for (ContactParamTO paramTO : contactParamTOs) {
                    if (ContactParamEnum.HOME.getId().equals(paramTO.getType().getId())) {
                        home.add(paramTO.getName());
                    } else if (ContactParamEnum.WORK.getId().equals(paramTO.getType().getId())) {
                        work.add(paramTO.getName());
                    } else if (ContactParamEnum.OTHER.getId().equals(paramTO.getType().getId())) {
                        other.add((paramTO.getName()));
                    }
                    if (paramTO.getIsPrimary()) {
                        contactItem.setPrimaryEmail(paramTO.getName());
                    }
                }
                paramMap.put(ContactParamEnum.HOME.getId(), home);
                paramMap.put(ContactParamEnum.WORK.getId(), work);
                paramMap.put(ContactParamEnum.OTHER.getId(), other);
                contactItem.setEmails(paramMap);
            }
            case Constants.CONTACT_PHONES -> {
                ArrayList<String> mobile = new ArrayList<>();
                ArrayList<String> homeFax = new ArrayList<>();
                ArrayList<String> workFax = new ArrayList<>();
                ArrayList<String> pager = new ArrayList<>();
                ArrayList<String> extension = new ArrayList<>();
                for (ContactParamTO paramTO : contactParamTOs) {
                    if (ContactParamEnum.HOME.getId().equals(paramTO.getType().getId())) {
                        home.add(paramTO.getName());
                    } else if (ContactParamEnum.WORK.getId().equals(paramTO.getType().getId())) {
                        work.add(paramTO.getName());
                    } else if (ContactParamEnum.MOBILE.getId().equals(paramTO.getType().getId())) {
                        mobile.add(paramTO.getName());
                    } else if (ContactParamEnum.HOME_FAX.getId().equals(paramTO.getType().getId())) {
                        homeFax.add(paramTO.getName());
                    } else if (ContactParamEnum.WORK_FAX.getId().equals(paramTO.getType().getId())) {
                        workFax.add(paramTO.getName());
                    } else if (ContactParamEnum.PAGER.getId().equals(paramTO.getType().getId())) {
                        pager.add(paramTO.getName());
                    } else if (ContactParamEnum.OTHER.getId().equals(paramTO.getType().getId())) {
                        other.add(paramTO.getName());
                    } else if (ContactParamEnum.EXTENSION.getId().equals(paramTO.getType().getId())) {
                        extension.add(paramTO.getName());
                    }
                    if (paramTO.getIsPrimary()) {
                        contactItem.setPrimaryPhone(paramTO.getName());
                    }
                }
                paramMap.put(ContactParamEnum.HOME.getId(), home);
                paramMap.put(ContactParamEnum.WORK.getId(), work);
                paramMap.put(ContactParamEnum.MOBILE.getId(), mobile);
                paramMap.put(ContactParamEnum.HOME_FAX.getId(), homeFax);
                paramMap.put(ContactParamEnum.WORK_FAX.getId(), workFax);
                paramMap.put(ContactParamEnum.PAGER.getId(), pager);
                paramMap.put(ContactParamEnum.OTHER.getId(), other);
                paramMap.put(ContactParamEnum.EXTENSION.getId(), extension);
                contactItem.setPhones(paramMap);
            }
            case Constants.CONTACT_IMADDRESSES -> {
                ArrayList<String> gtalk = new ArrayList<>();
                ArrayList<String> aim = new ArrayList<>();
                ArrayList<String> yahoo = new ArrayList<>();
                ArrayList<String> skype = new ArrayList<>();
                ArrayList<String> qq = new ArrayList<>();
                ArrayList<String> msn = new ArrayList<>();
                ArrayList<String> icq = new ArrayList<>();
                ArrayList<String> jabber = new ArrayList<>();
                for (ContactParamTO paramTO : contactParamTOs) {
                    if (ContactParamEnum.GOOGLE_TALK.getId().equals(paramTO.getType().getId())) {
                        gtalk.add(paramTO.getName());
                    } else if (ContactParamEnum.AIM.getId().equals(paramTO.getType().getId())) {
                        aim.add(paramTO.getName());
                    } else if (ContactParamEnum.YAHOO.getId().equals(paramTO.getType().getId())) {
                        yahoo.add(paramTO.getName());
                    } else if (ContactParamEnum.SKYPE.getId().equals(paramTO.getType().getId())) {
                        skype.add(paramTO.getName());
                    } else if (ContactParamEnum.QQ.getId().equals(paramTO.getType().getId())) {
                        qq.add(paramTO.getName());
                    } else if (ContactParamEnum.MSN.getId().equals(paramTO.getType().getId())) {
                        msn.add(paramTO.getName());
                    } else if (ContactParamEnum.ICQ.getId().equals(paramTO.getType().getId())) {
                        icq.add(paramTO.getName());
                    } else if (ContactParamEnum.JABBER.getId().equals(paramTO.getType().getId())) {
                        jabber.add(paramTO.getName());
                    }
                }
                paramMap.put(ContactParamEnum.GOOGLE_TALK.getId(), gtalk);
                paramMap.put(ContactParamEnum.AIM.getId(), aim);
                paramMap.put(ContactParamEnum.YAHOO.getId(), yahoo);
                paramMap.put(ContactParamEnum.SKYPE.getId(), skype);
                paramMap.put(ContactParamEnum.QQ.getId(), qq);
                paramMap.put(ContactParamEnum.MSN.getId(), msn);
                paramMap.put(ContactParamEnum.ICQ.getId(), icq);
                paramMap.put(ContactParamEnum.JABBER.getId(), jabber);
                contactItem.setImAddresses(paramMap);
            }
            case Constants.CONTACT_WEBSITES -> {
                ArrayList<String> homePage = new ArrayList<>();
                ArrayList<String> ftp = new ArrayList<>();
                ArrayList<String> blog = new ArrayList<>();
                ArrayList<String> profile = new ArrayList<>();
                for (ContactParamTO paramTO : contactParamTOs) {
                    if (ContactParamEnum.HOME.getId().equals(paramTO.getType().getId())) {
                        home.add(paramTO.getName());
                    } else if (ContactParamEnum.WORK.getId().equals(paramTO.getType().getId())) {
                        work.add(paramTO.getName());
                    } else if (ContactParamEnum.HOME_PAGE.getId().equals(paramTO.getType().getId())) {
                        homePage.add(paramTO.getName());
                    } else if (ContactParamEnum.FTP.getId().equals(paramTO.getType().getId())) {
                        ftp.add(paramTO.getName());
                    } else if (ContactParamEnum.BLOG.getId().equals(paramTO.getType().getId())) {
                        blog.add(paramTO.getName());
                    } else if (ContactParamEnum.PROFILE.getId().equals(paramTO.getType().getId())) {
                        profile.add(paramTO.getName());
                    } else if (ContactParamEnum.OTHER.getId().equals(paramTO.getType().getId())) {
                        other.add(paramTO.getName());
                    }
                }
                paramMap.put(ContactParamEnum.HOME.getId(), home);
                paramMap.put(ContactParamEnum.WORK.getId(), work);
                paramMap.put(ContactParamEnum.HOME_PAGE.getId(), homePage);
                paramMap.put(ContactParamEnum.FTP.getId(), ftp);
                paramMap.put(ContactParamEnum.BLOG.getId(), blog);
                paramMap.put(ContactParamEnum.PROFILE.getId(), profile);
                paramMap.put(ContactParamEnum.OTHER.getId(), other);
                contactItem.setWebAddresses(paramMap);
            }
            default -> {
            }
        }

    }


    private Object saveContactParam(List<ContactParamTO> contactParamTOs, Integer id, Integer paramType) {
        if (id == null) {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        if (contactParamTOs == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }
        ContactListItem contactItem = new ContactListItem();
        contactItem.setObjectId(id);

        HashMap<Integer, ArrayList<String>> paramMap = new HashMap<>();

        ArrayList<String> home = new ArrayList<>();
        ArrayList<String> work = new ArrayList<>();
        ArrayList<String> other = new ArrayList<>();

        switch (paramType) {
            case Constants.CONTACT_EMAILS -> {
                for (ContactParamTO paramTO : contactParamTOs) {
                    if (ContactParamEnum.HOME.getId().equals(paramTO.getType().getId())) {
                        home.add(paramTO.getName());
                    } else if (ContactParamEnum.WORK.getId().equals(paramTO.getType().getId())) {
                        work.add(paramTO.getName());
                    } else if (ContactParamEnum.OTHER.getId().equals(paramTO.getType().getId())) {
                        other.add((paramTO.getName()));
                    }
                    if (paramTO.getIsPrimary()) {
                        contactItem.setPrimaryEmail(paramTO.getName());
                    }
                }
                paramMap.put(ContactParamEnum.HOME.getId(), home);
                paramMap.put(ContactParamEnum.WORK.getId(), work);
                paramMap.put(ContactParamEnum.OTHER.getId(), other);
                contactItem.setEmails(paramMap);
            }
            case Constants.CONTACT_PHONES -> {
                ArrayList<String> mobile = new ArrayList<>();
                ArrayList<String> homeFax = new ArrayList<>();
                ArrayList<String> workFax = new ArrayList<>();
                ArrayList<String> pager = new ArrayList<>();
                ArrayList<String> extension = new ArrayList<>();
                for (ContactParamTO paramTO : contactParamTOs) {
                    if (ContactParamEnum.HOME.getId().equals(paramTO.getType().getId())) {
                        home.add(paramTO.getName());
                    } else if (ContactParamEnum.WORK.getId().equals(paramTO.getType().getId())) {
                        work.add(paramTO.getName());
                    } else if (ContactParamEnum.MOBILE.getId().equals(paramTO.getType().getId())) {
                        mobile.add(paramTO.getName());
                    } else if (ContactParamEnum.HOME_FAX.getId().equals(paramTO.getType().getId())) {
                        homeFax.add(paramTO.getName());
                    } else if (ContactParamEnum.WORK_FAX.getId().equals(paramTO.getType().getId())) {
                        workFax.add(paramTO.getName());
                    } else if (ContactParamEnum.PAGER.getId().equals(paramTO.getType().getId())) {
                        pager.add(paramTO.getName());
                    } else if (ContactParamEnum.OTHER.getId().equals(paramTO.getType().getId())) {
                        other.add(paramTO.getName());
                    } else if (ContactParamEnum.EXTENSION.getId().equals(paramTO.getType().getId())) {
                        extension.add(paramTO.getName());
                    }
                    if (paramTO.getIsPrimary()) {
                        contactItem.setPrimaryPhone(paramTO.getName());
                    }
                }
                paramMap.put(ContactParamEnum.HOME.getId(), home);
                paramMap.put(ContactParamEnum.WORK.getId(), work);
                paramMap.put(ContactParamEnum.MOBILE.getId(), mobile);
                paramMap.put(ContactParamEnum.HOME_FAX.getId(), homeFax);
                paramMap.put(ContactParamEnum.WORK_FAX.getId(), workFax);
                paramMap.put(ContactParamEnum.PAGER.getId(), pager);
                paramMap.put(ContactParamEnum.OTHER.getId(), other);
                paramMap.put(ContactParamEnum.EXTENSION.getId(), extension);
                contactItem.setPhones(paramMap);
            }
            case Constants.CONTACT_IMADDRESSES -> {
                ArrayList<String> gtalk = new ArrayList<>();
                ArrayList<String> aim = new ArrayList<>();
                ArrayList<String> yahoo = new ArrayList<>();
                ArrayList<String> skype = new ArrayList<>();
                ArrayList<String> qq = new ArrayList<>();
                ArrayList<String> msn = new ArrayList<>();
                ArrayList<String> icq = new ArrayList<>();
                ArrayList<String> jabber = new ArrayList<>();
                for (ContactParamTO paramTO : contactParamTOs) {
                    if (ContactParamEnum.GOOGLE_TALK.getId().equals(paramTO.getType().getId())) {
                        gtalk.add(paramTO.getName());
                    } else if (ContactParamEnum.AIM.getId().equals(paramTO.getType().getId())) {
                        aim.add(paramTO.getName());
                    } else if (ContactParamEnum.YAHOO.getId().equals(paramTO.getType().getId())) {
                        yahoo.add(paramTO.getName());
                    } else if (ContactParamEnum.SKYPE.getId().equals(paramTO.getType().getId())) {
                        skype.add(paramTO.getName());
                    } else if (ContactParamEnum.QQ.getId().equals(paramTO.getType().getId())) {
                        qq.add(paramTO.getName());
                    } else if (ContactParamEnum.MSN.getId().equals(paramTO.getType().getId())) {
                        msn.add(paramTO.getName());
                    } else if (ContactParamEnum.ICQ.getId().equals(paramTO.getType().getId())) {
                        icq.add(paramTO.getName());
                    } else if (ContactParamEnum.JABBER.getId().equals(paramTO.getType().getId())) {
                        jabber.add(paramTO.getName());
                    }
                }
                paramMap.put(ContactParamEnum.GOOGLE_TALK.getId(), gtalk);
                paramMap.put(ContactParamEnum.AIM.getId(), aim);
                paramMap.put(ContactParamEnum.YAHOO.getId(), yahoo);
                paramMap.put(ContactParamEnum.SKYPE.getId(), skype);
                paramMap.put(ContactParamEnum.QQ.getId(), qq);
                paramMap.put(ContactParamEnum.MSN.getId(), msn);
                paramMap.put(ContactParamEnum.ICQ.getId(), icq);
                paramMap.put(ContactParamEnum.JABBER.getId(), jabber);
                contactItem.setImAddresses(paramMap);
            }
            case Constants.CONTACT_WEBSITES -> {
                ArrayList<String> homePage = new ArrayList<>();
                ArrayList<String> ftp = new ArrayList<>();
                ArrayList<String> blog = new ArrayList<>();
                ArrayList<String> profile = new ArrayList<>();
                for (ContactParamTO paramTO : contactParamTOs) {
                    if (ContactParamEnum.HOME.getId().equals(paramTO.getType().getId())) {
                        home.add(paramTO.getName());
                    } else if (ContactParamEnum.WORK.getId().equals(paramTO.getType().getId())) {
                        work.add(paramTO.getName());
                    } else if (ContactParamEnum.HOME_PAGE.getId().equals(paramTO.getType().getId())) {
                        homePage.add(paramTO.getName());
                    } else if (ContactParamEnum.FTP.getId().equals(paramTO.getType().getId())) {
                        ftp.add(paramTO.getName());
                    } else if (ContactParamEnum.BLOG.getId().equals(paramTO.getType().getId())) {
                        blog.add(paramTO.getName());
                    } else if (ContactParamEnum.PROFILE.getId().equals(paramTO.getType().getId())) {
                        profile.add(paramTO.getName());
                    } else if (ContactParamEnum.OTHER.getId().equals(paramTO.getType().getId())) {
                        other.add(paramTO.getName());
                    }
                }
                paramMap.put(ContactParamEnum.HOME.getId(), home);
                paramMap.put(ContactParamEnum.WORK.getId(), work);
                paramMap.put(ContactParamEnum.HOME_PAGE.getId(), homePage);
                paramMap.put(ContactParamEnum.FTP.getId(), ftp);
                paramMap.put(ContactParamEnum.BLOG.getId(), blog);
                paramMap.put(ContactParamEnum.PROFILE.getId(), profile);
                paramMap.put(ContactParamEnum.OTHER.getId(), other);
                contactItem.setWebAddresses(paramMap);
            }
        }
        try {
            return successResponse(contactServiceLocal.saveContactParams(contactItem, paramType));
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_SAVE);
        }

    }

}
