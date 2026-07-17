package com.edatasite.workforce.rest.v3.release10.crm.service;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.rest.base.enums.ContactParamEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EmailDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.LeadTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.NoteDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.PhoneDto;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.ActivityTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.LeadPatchDto;
import com.edatasite.workforce.rest.v3.release10.crm.dto.LeadAddDto;
import com.edatasite.workforce.rest.v3.release10.crm.dto.LeadByStatusTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApiLeadService implements ApiConstants {
    private static final Logger log = LoggerFactory.getLogger(ApiLeadService.class);
    private static final SimpleDateFormat formatWithDatetimeAndTimezone = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
    private static final SimpleDateFormat formatWithDateTime = new SimpleDateFormat(FORMAT_WITH_DATETIME);

    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CRMService crmService;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private RegionManager regionManager;


    public List<LeadByStatusTO> leadByStatus(ListingFilterParameter fp) {
        ListResult<ContactListItem> newLeads = crmServiceLocal.getNewLeads(fp);
        Map<String, LeadByStatusTO> map = new HashMap<>();
        List<EdsReference> statuses = referenceManager.listReferences(EdsCrmContact._LEAD_STATUS);
        for (EdsReference status : statuses) {
            LeadByStatusTO leadByStatusTO = new LeadByStatusTO();
            leadByStatusTO.setStatusId(status.getObjectID());
            leadByStatusTO.setStatusTitle(status.getName());
            map.put(status.getName(), leadByStatusTO);
        }
        for (ContactListItem contactListItem : newLeads.getList()) {
            String status = contactListItem.getLeadStatus() != null ? contactListItem.getLeadStatus().getName() : "Unknown";
            LeadByStatusTO leadList = map.getOrDefault(status, new LeadByStatusTO());
            LeadTO lead = new LeadTO();
            lead.setName(contactListItem.getName());
            if (contactListItem.getCrmAccount() != null) {
                lead.setCompany(contactListItem.getCrmAccount().getName());
            }
            lead.setItem_id(contactListItem.getObjectId());
            lead.setStatus_id(contactListItem.getLeadStatus() != null ? contactListItem.getLeadStatus().getId() : 0);
            lead.setDate_added(formatWithDateTime.format(contactListItem.getCreatedDate()));
            lead.setAvatar_image(contactListItem.getContactImageUrl());
            leadList.getLeads().add(lead);
            map.put(status, leadList);
        }
        return new ArrayList<>(map.values());
    }

    public LeadDto getLeadById(Integer id) {
        ContactListItem lead = crmServiceLocal.getLead(id);
        return ConvertUtils.toDto(lead);
    }

    public void updateLeadStatus(Integer id, Integer status) {
        crmService.changeLeadStatus(new ArrayList<>(Collections.singletonList(id)), status);
    }

    public LeadAddDto saveLead(LeadAddDto leadAddDTO) {
        if (leadAddDTO.getId() != null) {
            crmService.getLead(leadAddDTO.getId());
        }
        ContactListItem leadItem = new ContactListItem();
        leadItem.setObjectId(leadAddDTO.getId());
        leadItem.setContactType(CrmConstants.TYPE_LEAD_CONTACT);
        leadItem.setFirstName(leadAddDTO.getFirstName());
        leadItem.setLastName(leadAddDTO.getLastName());
        leadItem.setMiddleName(leadAddDTO.getMiddleName());
        leadItem.setOtherName(leadAddDTO.getOtherName());
        leadItem.setBirthDate(new DateNonConvertable(leadAddDTO.getBirthDate()));
        leadItem.setJobTitle(leadAddDTO.getJobTitle());
        leadItem.setDepartment(leadAddDTO.getDepartment());
        leadItem.setRefIndNumber(leadAddDTO.getRefIndNumber());
        leadItem.setAssets(leadAddDTO.getAssets());
        leadItem.setAccountIndustry(leadAddDTO.getAccountIndustry());
        leadItem.setTelegram(leadAddDTO.getTelegram());
        leadItem.setImAddresses(leadAddDTO.getImAddresses());
        leadItem.setWebAddresses(leadAddDTO.getWebAddresses());
        leadItem.setPrimaryPhone(leadAddDTO.getPhoneNumbers().stream().filter(PhoneDto::isPrimary).map(PhoneDto::getNumber).findFirst().orElse(""));
        leadItem.setPrimaryEmail(leadAddDTO.getEmails().stream().filter(EmailDto::isPrimary).map(EmailDto::getEmail).findFirst().orElse(""));

        if (leadAddDTO.getEmails() != null) {
            HashMap<Integer, ArrayList<String>> emails = new HashMap<>();
            ArrayList<String> workEmail = leadAddDTO.getEmails().stream().map(EmailDto::getEmail).collect(Collectors.toCollection(ArrayList::new));
            emails.put(ContactParamEnum.WORK.getId(), workEmail);
            leadItem.setEmails(emails);
        }

        if (leadAddDTO.getPhoneNumbers() != null && !leadAddDTO.getPhoneNumbers().isEmpty()) {
            for (PhoneDto phoneDto : leadAddDTO.getPhoneNumbers()) {
                if (leadItem.getAllPhones().contains(phoneDto.getNumber())) {
                    continue;
                }
                if (phoneDto.isPrimary()) {
                    leadItem.setPrimaryPhone(phoneDto.getNumber());
                }
                if (ContactParamEnum.WORK.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setWorkPhone(phoneDto.getNumber());
                } else if (ContactParamEnum.HOME.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setHomePhone(phoneDto.getNumber());
                } else if (ContactParamEnum.MOBILE.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.FAX.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.TELEGRAM.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.VIBER.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.WHATSAPP.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setMobile(phoneDto.getNumber());
                }
            }
        }

        CrmAccountItem crmAccountItem = new CrmAccountItem();
        crmAccountItem.setObjectId(leadAddDTO.getCompanyId());
        leadItem.setCrmAccount(crmAccountItem);

        if (leadAddDTO.getNotes() != null) {
            EdsUser user = countryManager.getUser();
            ArrayList<HistoryListItem> notes1 = leadAddDTO.getNotes().stream()
                    .peek(n -> n.setEmployee(user.getName()))
                    .map(ConvertUtils::toEntity)
                    .collect(Collectors.toCollection(ArrayList::new));
            ArrayList<HistoryListItem> notes = notes1;
            leadItem.setNotes(notes);
        }

        if (leadAddDTO.getAddresses() != null) {
            leadItem.setAddresses(leadAddDTO.getAddresses().stream().map(ConvertUtils::toEntity).collect(Collectors.toCollection(ArrayList::new)));
        }

        leadItem.setCampaignId(leadAddDTO.getCampaignId());
        Optional.ofNullable(leadAddDTO.getSource())
                .map(referenceManager::getByName)
                .map(EdsReference::getObjectID)
                .ifPresent(leadItem::setLeadSourceID);

        Optional.ofNullable(leadAddDTO.getStatus())
                .map(referenceManager::getByName)
                .map(EdsReference::getRPC)
                .ifPresent(leadItem::setLeadStatus);

        Optional.ofNullable(leadAddDTO.getIndustry())
                .map(IdCode::getId)
                .map(referenceManager::get)
                .map(EdsReference::getObjectID)
                .ifPresent(leadItem.getCrmAccount()::setIndustryID);
        Optional.ofNullable(leadAddDTO.getIndustry())
                .map(IdCode::getCode)
                .map(referenceManager::getByCode)
                .map(EdsReference::getObjectID)
                .ifPresent(leadItem.getCrmAccount()::setIndustryID);
        leadItem.setLeadAssigneeID(leadAddDTO.getAssigneeId());


        leadItem.setCustomFields(CustomFieldsUtils.convertCustomFields(leadAddDTO.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Lead), null));
        Integer leadId = contactServiceLocal.saveContact(leadItem, null, false);
        leadAddDTO.setId(leadId);
        leadAddDTO.setEntityId(contactServiceLocal.getEntityIdByLeadId(leadId));
        return leadAddDTO;
    }

    public Integer applyChanges(LeadPatchDto dto) throws RestException, ParseException {
        ContactListItem leadItem = crmService.getLead(dto.getId());
        if (leadItem == null) {
            throw new RestException(ApiConstants.ERROR, "Lead is not found by given Id/Number.", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        Optional.ofNullable(leadItem.getFirstName()).ifPresent(leadItem::setFirstName);
        Optional.ofNullable(leadItem.getLastName()).ifPresent(leadItem::setLastName);
        Optional.ofNullable(leadItem.getOwner()).ifPresent(leadItem::setOwner);
        Optional.ofNullable(leadItem.getOwnerId()).ifPresent(leadItem::setOwnerId);

//        leadItem.setCompanyPhotoId(dto.getCompanyId());
        if (dto.getEmails() != null && !dto.getEmails().isEmpty()) {
            leadItem.setPrimaryEmail(dto.getEmails().get(0).getEmail());
            ArrayList<String> emails = new ArrayList<>();
            for (EmailDto emailDto : dto.getEmails()) {

                if (emailDto.isPrimary()) {
                    leadItem.setPrimaryEmail(emailDto.getEmail());
                }
                emails.add(emailDto.getEmail());
            }
            leadItem.setWorkEmail(emails);
        }
        if (dto.getPhoneNumbers() != null && !dto.getPhoneNumbers().isEmpty()) {
            leadItem.setPrimaryPhone(dto.getPhoneNumbers().get(0).getNumber());
            for (PhoneDto phoneDto : dto.getPhoneNumbers()) {
                if (leadItem.getAllPhones().contains(phoneDto.getNumber())) {
                    continue;
                }
                if (phoneDto.isPrimary()) {
                    leadItem.setPrimaryPhone(phoneDto.getNumber());
                }

                if (ContactParamEnum.WORK.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setWorkPhone(phoneDto.getNumber());
                } else if (ContactParamEnum.HOME.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setHomePhone(phoneDto.getNumber());
                } else if (ContactParamEnum.MOBILE.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.FAX.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.TELEGRAM.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.VIBER.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setMobile(phoneDto.getNumber());
                } else if (ContactParamEnum.WHATSAPP.getName().equalsIgnoreCase(phoneDto.getPhoneCategory())) {
                    leadItem.setMobile(phoneDto.getNumber());
                }
            }
        }
        Optional.ofNullable(leadItem.getStatus()).ifPresent(leadItem::setStatus);
        if (dto.getNotes() != null && !dto.getNotes().isEmpty()) {
            ArrayList<HistoryListItem> notes = new ArrayList<>();
            for (NoteDto noteDto : dto.getNotes()) {
                noteDto.setEntityId(leadItem.getObjectId());
                notes.add(ConvertUtils.toEntity(noteDto, crmContactManager.getUser().getName()));
            }
            leadItem.setNotes(notes);
        }
        Optional.ofNullable(leadItem.getJobTitle()).ifPresent(leadItem::setJobTitle);

        if (dto.getAddresses() != null && !dto.getAddresses().isEmpty()) {
            ArrayList<Address> addresses = new ArrayList<>();
            for (AddressDto addressDto : dto.getAddresses()) {
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
                addressDto.setEntityId(leadItem.getObjectId());
                addressDto.setEntityType(EdsAddress.ENTITY_TYPE_CONTACT);
                addresses.add(ConvertUtils.toEntity(addressDto));

                if (addressDto.isPrimary()) {
                    leadItem.setPrimaryAddress(ConvertUtils.toEntity(addressDto));
                }
            }
            leadItem.setAddresses(addresses);
        }
        Optional.ofNullable(dto.getCustomFields()).ifPresent(c -> {
            EdsCrmContact lead = crmContactManager.get(leadItem.getObjectId());
            leadItem.setCustomFields(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Lead), lead.getCustomFields()));
        });
        Integer id = contactServiceLocal.saveContact(leadItem, null, false);

        return id;
    }

    public void deleteLeadById(Integer id) {
        contactServiceLocal.deleteContacts(new ArrayList<>(Collections.singletonList(id)), null, false);
    }

    public List<ActivityTO> getLastActivities(Integer id) {
        ListResult<Appointment> activities = crmService.getLastActivities(id, RelationItem.TYPE_LEAD);
        return activities.getList().stream()
                .map(this::toAppointmentDto)
                .toList();
    }

    public List<ActivityTO> getActivities(Integer relationId) {
        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setRelationID(relationId);
        filterParametrs.setRelationType("lead");
        filterParametrs.setCreatedFrom(Appointment.FROM_CRM);
        return crmService.getEventList(filterParametrs).getList().stream()
                .map(event -> toAppointmentDto(event))
                .toList();
    }

    private ActivityTO toAppointmentDto(Appointment appointment) {
        ActivityTO to = new ActivityTO();
        to.setType(appointment.getRelationType());
        to.setName(appointment.getSubject());
        to.setDescription(appointment.getDescription());
        Optional.ofNullable(appointment.getStartDate())
                .map(formatWithDatetimeAndTimezone::format)
                .ifPresent(to::setStart_date);
        Optional.ofNullable(appointment.getEndDate())
                .map(formatWithDatetimeAndTimezone::format)
                .ifPresent(to::setEnd_date);
        to.setItem_id(appointment.getObjectID());
        return to;
    }

    public PagingListResultTO<LeadTO> getLeadKanban(ListingFilterParameter filterParameter) {
        ListResult<ContactListItem> result = crmService.getNewKanbanLeads(filterParameter, new SelectItem(filterParameter.getColumnMetadataId()));

        return toLeadResult(filterParameter, result);
    }

    private static PagingListResultTO<LeadTO> toLeadResult(ListingFilterParameter filterParameter, ListResult<ContactListItem> result) {
        PagingListResultTO<LeadTO> leadListResult = new PagingListResultTO<>();
        leadListResult.setTotal_count(result.getTotal());
        leadListResult.setCount(result.getList() != null ? result.getList().size() : 0);
        if (filterParameter.getStart() != null) {
            leadListResult.setOffset(filterParameter.getStart());
        } else if (filterParameter.getCurrentPage() != null && filterParameter.getLimit() != null) {
            leadListResult.setOffset(filterParameter.getCurrentPage() * filterParameter.getLimit());
        }

        List<LeadTO> leads = result.getList().stream()
                .map(ApiLeadService::toLeadTo)
                .toList();

        leadListResult.setList(leads);
        return leadListResult;
    }

    public PagingListResultTO<LeadTO> getLeadList(ListingFilterParameter filterParameter) throws RestException {
        ListResult<ContactListItem> result;
        try {
            result = crmServiceLocal.getNewLeads(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return toLeadResult(filterParameter, result);
    }

    private static LeadTO toLeadTo(ContactListItem item) {
        LeadTO lead = new LeadTO();
        lead.setName(item.getName());
        lead.setItem_id(item.getObjectId());
        lead.setDate_added(formatWithDateTime.format(item.getCreatedDate()));
        lead.setAvatar_image(item.getContactImageUrl());
        Optional.ofNullable(item.getLeadStatus())
                .map(SelectItem::getId)
                .or(() -> Optional.of(0))
                .ifPresent(lead::setStatus_id);
        Optional.ofNullable(item.getCrmAccount())
                .map(CrmAccountItem::getName)
                .ifPresent(lead::setCompany);
        return lead;
    }
}
