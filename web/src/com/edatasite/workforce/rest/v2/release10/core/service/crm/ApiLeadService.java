package com.edatasite.workforce.rest.v2.release10.core.service.crm;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.rest.base.enums.ContactParamEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EmailDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.PhoneDto;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;

@Service
public class ApiLeadService {

    private static Logger log = LoggerFactory.getLogger(ApiLeadService.class);

    @Autowired
    ContactService contactService;
    @Autowired
    CRMService crmService;
    @Autowired
    CrmContactManager contactManager;
    @Autowired
    CountryManager countryManager;
    @Autowired
    RegionManager regionManager;
    @Autowired
    ReferenceManager referenceManager;
    @Autowired
    private CommonService commonService;

    @Transactional
    public void save(final LeadDto leadDto) throws RestException, ParseException {
        log.debug("Creating new lead from {}", leadDto);
        if (leadDto.getId() != null) {
            EdsCrmContact crmContact = contactManager.get(leadDto.getId());
            if (crmContact == null) {
                throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Lead with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
        }
        ContactListItem contactListItem = crmService.editLead(leadDto.getId(), null);
        contactListItem.setLeadAssigneeID(leadDto.getAssigneeId());
        contactListItem.setFirstName(leadDto.getFirstName());
        contactListItem.setLastName(leadDto.getLastName());
        contactListItem.setJobTitle(leadDto.getJobTitle());
        contactListItem.setOwnerId(regionManager.getUser().getObjectID());
        contactListItem.setOwner(regionManager.getUser().getName());
        contactListItem.setContactType(5);
        contactListItem.setCheckForDuplicates(true);
        contactListItem.setPrimaryContact(true);

        if (leadDto.getEmails() != null && !leadDto.getEmails().isEmpty()) {
            contactListItem.setPrimaryEmail(leadDto.getEmails().get(0).getEmail());
            ArrayList<String> emails = new ArrayList<>();
            for (EmailDto emailDto : leadDto.getEmails()) {

                if (emailDto.isPrimary()) {
                    contactListItem.setPrimaryEmail(emailDto.getEmail());
                }
                emails.add(emailDto.getEmail());
            }
            contactListItem.setWorkEmail(emails);
        }

        if (leadDto.getCompanyId() != null) {
            CrmAccountItem crmAccountItem = new CrmAccountItem();
            crmAccountItem.setObjectId(leadDto.getCompanyId());
            contactListItem.setCrmAccount(crmAccountItem);
        }

        if (StringUtils.isNotBlank(leadDto.getStatus())) {
            EdsReference status = referenceManager.findByParentCodeAndName(EdsCrmContact._LEAD_STATUS, leadDto.getStatus());
            if (status != null) {
                contactListItem.setLeadStatusID(status.getObjectID());
                contactListItem.setLeadStatus(status.getRPC());
            }
        }

        if (StringUtils.isNotBlank(leadDto.getSource())) {
            EdsReference source = referenceManager.findByParentCodeAndName(EdsCrmContact._LEAD_SOURCE, leadDto.getSource());
            if (source != null) {
                contactListItem.setLeadSourceID(source.getObjectID());
                contactListItem.setLeadSource(source.getName());
            }
        }
        contactListItem.setCampaignId(leadDto.getCampaignId());

        if (leadDto.getPhoneNumbers() != null && !leadDto.getPhoneNumbers().isEmpty()) {
            contactListItem.setPrimaryPhone(leadDto.getPhoneNumbers().get(0).getNumber());
            for (PhoneDto phoneDto : leadDto.getPhoneNumbers()) {
                if (contactListItem.getAllPhones().contains(phoneDto.getNumber())) {
                    continue;
                }
                if (phoneDto.isPrimary()) {
                    contactListItem.setPrimaryPhone(phoneDto.getNumber());
                }


                if (ContactParamEnum.WORK.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    contactListItem.setWorkPhone(phoneDto.getNumber());
                } else if (ContactParamEnum.HOME.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    contactListItem.setHomePhone(phoneDto.getNumber());
                } else if (ContactParamEnum.MOBILE.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    contactListItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.FAX.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    contactListItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.TELEGRAM.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    contactListItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.VIBER.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    contactListItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.WHATSAPP.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    contactListItem.setMobile(phoneDto.getNumber());
                }
            }
        }

        if (leadDto.getNotes() != null && !leadDto.getNotes().isEmpty()) {
            ArrayList<HistoryListItem> notes = new ArrayList<>();
            for (NoteDto noteDto : leadDto.getNotes()) {
                noteDto.setEntityId(contactListItem.getObjectId());
                notes.add(ConvertUtils.toEntity(noteDto, contactManager.getUser().getName()));
            }
            contactListItem.setNotes(notes);
        }

        if (leadDto.getAddresses() != null && !leadDto.getAddresses().isEmpty()) {
            ArrayList<Address> addresses = new ArrayList<>();
            for (AddressDto addressDto : leadDto.getAddresses()) {
                if (StringUtils.isNotBlank(addressDto.getCountry())) {
                    EdsCountry country = countryManager.getCountryByName(addressDto.getCountry());
                    if (country == null) {
                        country = countryManager.getCountryByCode(addressDto.getCountry());
                    }
                    if (country != null) {
                        addressDto.setCountryId(country.getObjectID());
                        addressDto.setCountryCode(country.getCode());
                    }
                }
                if (StringUtils.isNotBlank(addressDto.getState())) {
                    EdsRegion region = regionManager.getRegionByName(addressDto.getState());
                    if (region != null) {
                        addressDto.setStateId(region.getObjectID());
                    }
                }
                addressDto.setEntityId(contactListItem.getObjectId());
                addressDto.setEntityType(EdsAddress.ENTITY_TYPE_CONTACT);
                addresses.add(ConvertUtils.toEntity(addressDto));

                if (addressDto.isPrimary()) {
                    contactListItem.setPrimaryAddress(ConvertUtils.toEntity(addressDto));
                }
            }
            contactListItem.setAddresses(addresses);
        }

        ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.Lead);
        if (leadDto.getCustomFields() != null && !leadDto.getCustomFields().isEmpty() && !customFieldItems.isEmpty()) {

            contactListItem.setCustomFields(CustomFieldsUtils.convertCustomFields(leadDto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Lead), null));
        }
        contactListItem.setCheckForDuplicates(leadDto.isCheckForDuplicates());
        Integer result = contactService.saveContact(contactListItem, null, true);

        if (result == -2) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Lead with this email address already exists", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        } else if (result == -1) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Lead with this name already exists", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        } else if (result == -3) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Lead with this phone already exists", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }
        leadDto.setId(contactListItem.getObjectId());
        leadDto.setCreatedAt(contactListItem.getCreatedDate());
        leadDto.setUpdatedAt(contactListItem.getUpdatedDate());
    }

    @Transactional(readOnly = true)
    public LeadDto getById(final Integer id) throws RestException {
        EdsCrmContact crmContact = contactManager.get(id);
        if (crmContact == null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Lead with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        ContactListItem item = crmService.getLead(id);
        return ConvertUtils.toDto(item);
    }
}
