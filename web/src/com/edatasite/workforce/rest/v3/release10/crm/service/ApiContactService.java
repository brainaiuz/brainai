package com.edatasite.workforce.rest.v3.release10.crm.service;

import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.CrmCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.rest.base.enums.ContactParamEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.EmailTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CountriesListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.*;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_CRM_CONTACT;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.SERVER_ERROR;

@Service
public class ApiContactService {
    private final ContactServiceLocal contactServiceLocal;

    private final CommonServiceLocal commonServiceLocal;
    private final CrmContactManager crmContactManager;
    private final CompanyCustomFieldsManager companyCustomFieldsManager;
    private final CrmCustomFieldsManager crmCustomFieldsManager;
    private final AttachmentUtilsManager attachmentUtilsManager;
    private final ClientContactManager clientContactManager;
    private final UserManager userManager;


    public ApiContactService(ContactServiceLocal contactServiceLocal,
                             CommonServiceLocal commonServiceLocal,
                             CrmContactManager crmContactManager,
                             CompanyCustomFieldsManager companyCustomFieldsManager,
                             CrmCustomFieldsManager crmCustomFieldsManager,
                             AttachmentUtilsManager attachmentUtilsManager,
                             ClientContactManager clientContactManager,
                             UserManager userManager) {
        this.contactServiceLocal = contactServiceLocal;
        this.commonServiceLocal = commonServiceLocal;
        this.crmContactManager = crmContactManager;
        this.companyCustomFieldsManager = companyCustomFieldsManager;
        this.crmCustomFieldsManager = crmCustomFieldsManager;
        this.attachmentUtilsManager = attachmentUtilsManager;
        this.clientContactManager = clientContactManager;
        this.userManager = userManager;
    }

    public PagingListResultTO<ContactTO> getAllContacts(ListingFilterParameter filterParam) throws RestException {
        PagingListResultTO<ContactTO> contactListResult = new PagingListResultTO<>();
        ListResult<ContactListItem> result;
        try {
            result = contactServiceLocal.getNewContactList(filterParam);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        contactListResult.setTotal_count(result.getTotal());
        contactListResult.setCount(result.getList() != null ? result.getList().size() : 0);

        ArrayList<ContactTO> contactList = new ArrayList<>();
        for (ContactListItem contactListItem : result.getList()) {
            ContactTO contactItem = new ContactTO();
            if (StringUtils.isNotBlank(contactListItem.getName())) {
                contactItem.setName(contactListItem.getName().trim());
            }
            contactItem.setItem_id(contactListItem.getObjectId());
            contactItem.setAvatar_image(contactListItem.getContactImageUrl());
            if (contactListItem.getCrmAccount() != null) {
                CrmAccountTO company = new CrmAccountTO();
                company.setItem_id(contactListItem.getCrmAccount().getObjectId());
                company.setName(contactListItem.getCrmAccount().getName());
                company.setAvatar_image(contactListItem.getCrmAccount().getLogoUrl());
                contactItem.setCompany(company);
            }
            ContactsTO contactsTO = new ContactsTO();
            EdsCrmContact edsCrmContact = crmContactManager.get(contactListItem.getObjectId());
            if (edsCrmContact != null) {
                contactsTO.setPhones(contactServiceLocal.convertToPhoneTO(edsCrmContact));
                contactsTO.setEmails(contactServiceLocal.convertContactEmails(edsCrmContact));
            }
            contactItem.setContacts(contactsTO);
            contactList.add(contactItem);
        }
        contactListResult.setList(contactList);
        return contactListResult;
    }

    public ContactDetailsItemResponseTO getContactById(Integer id) throws RestException {
        ContactListItem contact = contactServiceLocal.getContact(id, false);
        EdsCrmContact edsCrmContact = Optional.ofNullable(crmContactManager.get(id))
                .orElseThrow(() -> new RestException("Contact with the given Id not found", "Contact with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.NOT_FOUND));

        ContactDetailsItemTO contactDetailsItem = new ContactDetailsItemTO();
        ContactTO contactBaseInfo = contactServiceLocal.convertToContactTO(edsCrmContact);

        contactBaseInfo.setItem_id(contact.getObjectId());
        contactBaseInfo.setName(contact.getName());

        contactBaseInfo.setFirst_name(contact.getFirstName());
        contactBaseInfo.setLast_name(contact.getLastName());
        contactBaseInfo.setJobTitle(contact.getJobTitle());
        contactBaseInfo.setTitleName(contact.getTitle());
        if (edsCrmContact.getPhoto() != null) {
            contactBaseInfo.setAvatar_image(commonServiceLocal.getImageUrl(contact.getPhotoId()));
        }

        ContactsTO contactsTO = new ContactsTO();

        if (contact.getAllPhonesAsMap() != null) {
            contact.getAllPhonesAsMap().forEach((k, v) -> {
                List<PhoneTO> list = v.stream().map(p -> {
                    PhoneTO phone = new PhoneTO();
                    phone.setCategory(k);
                    phone.setType(k);
                    phone.setPhone_number(p);
                    phone.setPrimaryContact(p.equals(contact.getPrimaryPhone()));
                    return phone;
                }).toList();
                ArrayList<PhoneTO> phoneTOS = Optional.ofNullable(contactsTO.getPhones()).orElse(new ArrayList<>());
                phoneTOS.addAll(list);
                contactsTO.setPhones(phoneTOS);
            });
        }

        List<String> emails = new ArrayList<>();
        if (contact.getHomeEmail() != null) {
            emails.addAll(contact.getHomeEmail());
        }
        if (contact.getWorkEmail() != null) {
            emails.addAll(contact.getWorkEmail());
        }
        if (contact.getOtherEmail() != null) {
            emails.addAll(contact.getOtherEmail());
        }
        emails = emails.stream().distinct().toList();
        if (!emails.isEmpty()) {
            contactsTO.setEmails(new ArrayList<>(emails));
            ArrayList<EmailTO> emailTo = emails.stream()
                    .map(EmailTO::new)
                    .peek(e -> e.addProperty("primary", e.getEmail().equals(contact.getPrimaryEmail())))
                    .collect(Collectors.toCollection(ArrayList::new));
            contactsTO.setEmailTo(emailTo);
        } else if (contact.getPrimaryEmail() != null) {
            contactsTO.setEmails(new ArrayList<>(List.of(contact.getPrimaryEmail())));
            EmailTO primaryEmailTo = new EmailTO(contact.getPrimaryEmail());
            primaryEmailTo.addProperty("primary", true);
            contactsTO.setEmailTo(new ArrayList<>(List.of(primaryEmailTo)));
        }

        contactBaseInfo.setContacts(contactsTO);

        contactBaseInfo.setCompany(convertCompany(contact.getCrmAccount()));
        //Addresses
        contactBaseInfo.setEntityAddresses(convertAddresses(contact));

        //contactBaseInfo = new ContactTO();
        contactDetailsItem.setBase_info(contactBaseInfo);
        contactDetailsItem.setCompany(contactBaseInfo.getCompany());
        ContactDetailsItemResponseTO result = new ContactDetailsItemResponseTO();
        ArrayList<CompanyCustomFieldItem> customFieldItems = commonServiceLocal.getCompanyCustomFields(ViewName.Contact);

        if (edsCrmContact.getCustomFields() != null && !CollectionUtils.isEmpty(customFieldItems)) {
            customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(edsCrmContact.getCustomFields(), customFieldItems);
            contactDetailsItem.setCustomFields(customFieldItems.stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));
        }
        List<FileResource> files = attachmentUtilsManager.getAttachments(F_CRM_CONTACT, id, id);
        List<AttachmentTO> attachments = Optional.ofNullable(files).orElse(new ArrayList<>()).stream()
                .map(file -> new AttachmentTO(file.getFileName(), file.getDownloadUrl()))
                .toList();
        contactDetailsItem.setAttachments(attachments);

        result.setItem(contactDetailsItem);

        return result;
    }

    public CrmAccountTO convertCompany(CrmAccountItem company) {
        if (company == null) {
            return null;
        }
        CrmAccountTO crmAccount = new CrmAccountTO();
        crmAccount.setItem_id(company.getObjectId());
        crmAccount.setName(company.getName());
        crmAccount.setAvatar_image(company.getLogoUrl());
        return crmAccount;
    }

    public ArrayList<EntityContactAddressTO> convertAddresses(ContactListItem contact) {
        ArrayList<EntityContactAddressTO> entityAddresses = new ArrayList<>();
        if (contact == null || contact.getAddresses() == null) {
            return entityAddresses;
        }
        ArrayList<Address> addresses = contact.getAddresses();
        addresses.forEach(address -> {
            EntityContactAddressTO entityAddress = new EntityContactAddressTO();
            entityAddress.setItem_id(address.getObjectID());
            if (ContactParamEnum.HOME.getId().equals(address.getRelationType())) {
                entityAddress.setType(ContactParamEnum.HOME.getCode());
            } else if (ContactParamEnum.WORK.getId().equals(address.getRelationType())) {
                entityAddress.setType(ContactParamEnum.WORK.getCode());
            } else {
                entityAddress.setType(ContactParamEnum.OTHER.getCode());
            }

            entityAddress.setName(address.getName());
            entityAddress.setLine_1(address.getAddress());
            entityAddress.setLine_2(address.getAddressb());
            entityAddress.setCity(address.getCity());
            entityAddress.setPost_code(address.getZipCode());
            entityAddress.setIs_primary(address.isPrimary());
            if (address.getCountry() != null) {
                CountriesListTO country = new CountriesListTO();
                country.setId(address.getCountryId());
                country.setTitle(address.getCountry());
                country.setCountry_code(address.getCountryCode());
                entityAddress.setCountry(country);
            }
            if (address.getState() != null) {
                entityAddress.setState(new CategoryTO(address.getStateId(), address.getState()));
            }
            entityAddresses.add(entityAddress);
        });
        return entityAddresses;
    }

    @Transactional
    public void saveContactCustomField(Integer itemId, String alias, FileResource fileResource, ViewName viewName) {
        ArrayList<CompanyCustomFieldItem> customFieldsValue = getCustomFieldsValue(companyCustomFieldsManager.getByAliasName(viewName.name(), alias), toFileItem(fileResource));
        EdsCrmContact edsCrmContact = crmContactManager.get(itemId);
        EdsCrmCustomFields edsCrmCustomFields = createContactCustomFields(edsCrmContact.getCustomFields(), customFieldsValue);
        edsCrmContact.setCustomFields(edsCrmCustomFields);
        crmContactManager.update(edsCrmContact);
    }

    private static FileItem[] toFileItem(FileResource fileResource) {
        List<FileResource> fileResources = List.of(fileResource);
        if (fileResources == null || fileResources.isEmpty()) {
            return null;
        }
        FileItem[] fileItems = new FileItem[fileResources.size()];
        for (int i = 0; i < fileResources.size(); i++) {
            FileResource attachment = fileResources.get(i);
            FileItem fileItem = new FileItem();
            fileItem.setFileName(attachment.getFileName());
            fileItem.setAttachmentId(attachment.getEmailAttachmentID());
            fileItem.setUploadType(attachment.getUploadType());
            fileItem.setId(attachment.getObjectId());
            fileItem.setAddedBy(attachment.getCreatedBy());
            fileItem.setContentType(attachment.getContentType());
            fileItem.setDescription(attachment.getDescription());
            fileItem.setSize(attachment.getContentLength());
            fileItems[i] = fileItem;
        }
        return fileItems;
    }

    public static ArrayList<CompanyCustomFieldItem> getCustomFieldsValue(EdsCompanyCustomFieldsSettings companyCustomFieldItem, FileItem[] fileItems) {
        CompanyCustomFieldItem resultItem = new CompanyCustomFieldItem();
        resultItem.setObjectId(companyCustomFieldItem.getObjectID());
        resultItem.setDataType(companyCustomFieldItem.getDataType());
        resultItem.setColumnCode(companyCustomFieldItem.getColumnCode());
        resultItem.setFieldName(companyCustomFieldItem.getFieldName());
        resultItem.setAliasName(companyCustomFieldItem.getAliasName());
        resultItem.setFileUploadFieldId(companyCustomFieldItem.getObjectID());
        resultItem.setUiType(companyCustomFieldItem.getUiType());
        resultItem.setEntityCategoryName(companyCustomFieldItem.getEntityCategoryName());
        resultItem.setPrefix(companyCustomFieldItem.getPrefix());
        resultItem.setScale(companyCustomFieldItem.getScale());
        resultItem.setAttachments(fileItems);
        return new ArrayList<CompanyCustomFieldItem>(List.of(resultItem));
    }

    @Transactional
    public EdsCrmCustomFields createContactCustomFields(EdsCrmCustomFields edsCrmCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems == null || customFieldItems.isEmpty()) {
            return null;
        }
        if (edsCrmCustomFields == null) {
            boolean isEmpty = customFieldItems.stream().noneMatch(fieldItem -> (fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                    || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                    || (fieldItem.getSelectItems() != null && !fieldItem.getSelectItems().isEmpty()));
            if (isEmpty) {
                return null;
            }
            edsCrmCustomFields = new EdsCrmCustomFields();
            crmCustomFieldsManager.create(edsCrmCustomFields);
        }
        CustomFieldsUtils.setDomenObjectCustomFields(edsCrmCustomFields, customFieldItems);
        return edsCrmCustomFields;
    }

    @Transactional
    public void updateClientContact(Integer clientContactId, EdsReference activeStatus) {
        EdsClientContact edsClientContact = clientContactManager.get(clientContactId);
        edsClientContact.setAccountStatus(activeStatus);
        clientContactManager.merge(edsClientContact);
        clientContactManager.flush();
        userManager.merge(edsClientContact);
        userManager.flush();
    }
}
