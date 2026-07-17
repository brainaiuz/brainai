package com.edatasite.workforce.rest.v3.release10.crm;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.actions.CreateDocumentCommand;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.UserFilterManager;
import com.edatasite.workforce.gwt.core.server.servlets.WfmMultipartFile;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactDetailsItemResponseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EmailDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.PhoneDto;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.crm.dto.contact.ContactCreateDto;
import com.edatasite.workforce.rest.v3.release10.crm.service.ApiContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.*;
import static java.util.stream.Collectors.*;

@Tag(name = "Contact", description = "Contacts")
@RestController
@RequestMapping(value = "/contacts", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
public class ApiContactControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiContactControllerV3.class);

    private final WfmCommandServiceLocal wfmCommandServiceLocal;
    private final ApiContactService apiContactService;
    private final ContactServiceLocal contactServiceLocal;
    private final CommonServiceLocal commonServiceLocal;
    private final UserFilterManager userFilterManager;
    private final CrmContactManager crmContactManager;

    @Autowired
    public ApiContactControllerV3(ApiContactService apiContactService, ContactServiceLocal contactServiceLocal, WfmCommandServiceLocal wfmCommandServiceLocal, CommonServiceLocal commonServiceLocal, UserFilterManager userFilterManager, CrmContactManager crmContactManager) {
        this.apiContactService = apiContactService;
        this.contactServiceLocal = contactServiceLocal;
        this.wfmCommandServiceLocal = wfmCommandServiceLocal;
        this.commonServiceLocal = commonServiceLocal;
        this.userFilterManager = userFilterManager;
        this.crmContactManager = crmContactManager;
    }

    @Operation(summary = "Get all contacts")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<PagingListResultTO<ContactTO>> getContacts(@RequestParam("page") Integer page,
                                                               @RequestParam("limit") int limit,
                                                               @RequestParam(value = "query", required = false) String query) throws RestException {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setCurrentPage(page);
        filterParameter.setLimit(limit);
        if (query != null) {
            filterParameter.setSearchKey(query.replace("%20", " ").trim());
        }
        filterParameter.setDetectDuplicates(false);
        filterParameter.setWithImage(true);
        filterParameter.setSearchButton(true);
        filterParameter.setFromMobile(true);
        filterParameter.setLookUp(true);
        PagingListResultTO<ContactTO> allContacts = apiContactService.getAllContacts(filterParameter);
        return ResultTO.success(allContacts);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_CONTACTS_LIST, PermissionConstants.CRM_ADD_NEW_CONTACT})
    public ResultTO<?> createContact(@Valid @RequestBody ContactCreateDto request) throws RestException {
        ContactListItem contactListItem = new ContactListItem();
        contactListItem.setFirstName(request.getFirstname());
        contactListItem.setLastName(request.getLastname());
        contactListItem.setHomeEmail(request.getEmail());
        contactListItem.setMobile(request.getMobile());
        contactListItem.setAttachments(ConvertUtils.toFileItem(request.getAttachments()));
        contactServiceLocal.saveContact(contactListItem, null, true);
        return ResultTO.success();
    }

    @Operation(summary = "Get contact by id")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<ContactDetailsItemResponseTO> getContactById(@PathVariable("id") Integer id) throws RestException {
        ContactDetailsItemResponseTO contact = apiContactService.getContactById(id);
        return ResultTO.success(contact);
    }

    @Operation(summary = "Delete contact by id")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> deleteContact(@PathVariable("id") Integer id) {
        ArrayList<Integer> ids = new ArrayList<>(List.of(id));
        contactServiceLocal.deleteContacts(ids, null, true);
        return ResultTO.success();
    }

    @Operation(summary = "Upload contact avatar")
    @PostMapping(path = "/avatar/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<?> uploadContactAvatar(@RequestParam(value = "file") MultipartFile file, @PathVariable("id") Integer id) throws RestException {

        EdsUser user = userFilterManager.getUser();

        if (file == null || file.getSize() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        CreateDocumentCommand documentCommand = new CreateDocumentCommand();
        documentCommand.setImgType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH));
        documentCommand.setCompanyID(user.getCompany().getObjectID());
        documentCommand.setFolderName("static");
        documentCommand.setNotdownloadable("YES");
        WfmMultipartFile multipartFile = new WfmMultipartFile("", file);
        documentCommand.addFile(multipartFile);


        String url = "";
        try {
            String[] attachmentHandler = wfmCommandServiceLocal.createAttachmentHandler(documentCommand);
            if (attachmentHandler != null && attachmentHandler.length > 0) {
                url = commonServiceLocal.saveCrmContactImageUrl(Integer.valueOf(attachmentHandler[0]), id);
            }
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        }
        return ResultTO.success(url);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @CheckPermission(permissions = {PermissionConstants.CRM_CONTACTS_LIST, PermissionConstants.CRM_EDIT_CONTACT})
    public ResultTO<?> updateContact(@PathVariable("id") Integer id, @Valid @RequestBody ContactCreateDto request) throws RestException {
        log.info("REST request to update contact");
        EdsCrmContact contact = Optional.ofNullable(crmContactManager.get(id))
                .orElseThrow(() -> new RestException("Contact not found", "Contact not found", NOT_FOUND, HttpStatus.BAD_REQUEST));

        ContactListItem contactListItem = new ContactListItem();
        contactListItem.setObjectId(id);
        contactListItem.setFirstName(request.getFirstname());
        contactListItem.setLastName(request.getLastname());
        contactListItem.setHomeEmail(request.getEmail());
        contactListItem.setJobTitle(request.getJobTitle());
        contactListItem.setTitleId(request.getTitleId());
        contactListItem.setCrmAccount(ConvertUtils.toAccountItem(request.getCrmAccount()));
        if (CollectionUtils.isNotEmpty(request.getEmails())) {
            request.getEmails().stream()
                    .filter(EmailDto::isPrimary)
                    .findFirst()
                    .map(EmailDto::getEmail)
                    .ifPresent(contactListItem::setPrimaryEmail);
            ArrayList<String> emails = request.getEmails().stream()
                    .map(EmailDto::getEmail)
                    .collect(Collectors.toCollection(ArrayList::new));
            contactListItem.setWorkEmail(emails);
        }

        if (CollectionUtils.isNotEmpty(request.getPhoneNumbers())) {
            contactListItem.setPhones(new HashMap<>());
            request.getPhoneNumbers().stream()
                    .filter(PhoneDto::isPrimary)
                    .findFirst()
                    .map(PhoneDto::getNumber)
                    .ifPresent(contactListItem::setPrimaryPhone);

            Map<Integer, ArrayList<String>> phonesByCategory = request.getPhoneNumbers().stream()
                    .collect(groupingBy(p -> getPhoneType(p.getPhoneCategory()), mappingToNumber()));
            contactListItem.setPhones(phonesByCategory);
        } else {
            contactListItem.setPhones(new HashMap<>());
        }

        contactListItem.setMobile(request.getMobile());
        contactListItem.setAttachments(ConvertUtils.toFileItem(request.getAttachments()));
        var contactCompanyCustomFields = commonServiceLocal.getCompanyCustomFields(ViewName.Contact);
        var contactCustomFields = CustomFieldsUtils.convertCustomFields(request.getCustomFields(), contactCompanyCustomFields, contact.getCustomFields());
        contactListItem.setCustomFields(contactCustomFields);
        contactServiceLocal.saveContact(contactListItem, null, true);
        return ResultTO.success();
    }

    private static Collector<PhoneDto, ?, ArrayList<String>> mappingToNumber() {
        return mapping(PhoneDto::getNumber, toCollection(ArrayList::new));
    }

    private int getPhoneType(String type) {
        if (type == null) return 0;
        return switch (type) {
            case "HOME" -> G_HOME;
            case "WORK" -> G_WORK;
            case "MOBILE" -> G_MOBILE;
            case "FAX" -> G_FAX;
            case "WHATSAPP" -> G_WHATS_APP;
            case "TELEGRAM" -> G_TELEGRAM;
            case "VIBER" -> G_VIBER;
            default -> 0;
        };
    }
}
