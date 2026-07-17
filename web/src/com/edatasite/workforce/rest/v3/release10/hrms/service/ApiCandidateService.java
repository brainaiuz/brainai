package com.edatasite.workforce.rest.v3.release10.hrms.service;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageTO;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.AllowanceDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.CandidateDTO;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.IMWebAddressDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.LanguagesDto;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.VacancyDTO;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

/**
 * User : Akhror on 02/06/2021
 */
@Service
public class ApiCandidateService implements Constants {

    private final CrmServiceLocal crmServiceLocal;
    private final UserManager userManager;
    private final ContactService contactService;
    private final CrmContactManager candidateManager;
    private final AllInOneService allInOneService;
    private final ProjectManager projectManager;
    private final PayrollCategoryManager payrollCategoryManager;
    private final CommonService commonService;
    private final AttachmentUtilsManager attachmentUtilsManager;
    private final PositionManager positionManager;
    private final VacancyManager vacancyManager;

    @Autowired
    public ApiCandidateService(CrmServiceLocal crmServiceLocal, UserManager userManager, ContactService contactService, CrmContactManager candidateManager, AllInOneService allInOneService, ProjectManager projectManager, PayrollCategoryManager payrollCategoryManager, CommonService commonService, AttachmentUtilsManager attachmentUtilsManager, PositionManager positionManager, VacancyManager vacancyManager) {
        this.crmServiceLocal = crmServiceLocal;
        this.userManager = userManager;
        this.contactService = contactService;
        this.candidateManager = candidateManager;
        this.allInOneService = allInOneService;
        this.projectManager = projectManager;
        this.payrollCategoryManager = payrollCategoryManager;
        this.commonService = commonService;
        this.attachmentUtilsManager = attachmentUtilsManager;
        this.positionManager = positionManager;
        this.vacancyManager = vacancyManager;
    }


    public ListResultTO<CandidateDTO> getCandidatesList(ListingFilterParameter fp) {
        EdsUser user = userManager.getUser();
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(crmServiceLocal.getSolrQueryForCandidate(fp, user), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        ListResultTO<CandidateDTO> candidates = new ListResultTO<>();
        if (resp != null) {
            List<Integer> ids = resp.getResults().stream().map(doc -> Objects.requireNonNull(SolrUtils.asInteger(doc, SolrContactRepresenter.FIELD_CONTACT_ID))).toList();
            candidates.setTotalNumber(ids.size());
            ArrayList<CandidateDTO> items = new ArrayList<>();
            ids.forEach(id -> {
                ContactListItem item = contactService.getContact(id, false);
                List<FileResource> files = attachmentUtilsManager.getAttachments(F_CANDIDATE, id, id);
                items.add(ConvertUtils.toCandidateDto(item, files));
            });
            candidates.setItems(items);
        }
        return candidates;
    }

    @Transactional(readOnly = true)
    public CandidateDTO getById(IdCode dto) throws RestException {
        EdsCrmContact edsItem = getItem(dto.getId(), dto.getObjectKey());
        ContactListItem candidate = contactService.getContact(edsItem.getObjectID(), false);
        List<FileResource> files = attachmentUtilsManager.getAttachments(F_CANDIDATE, edsItem.getObjectID(), edsItem.getObjectID());
        return ConvertUtils.toCandidateDto(candidate, files);
    }

    @Transactional
    public CandidateDTO save(final CandidateDTO candidateDTO, boolean isNew) throws RestException {
        ContactListItem item = Optional.ofNullable(contactService.editContact(ContactListItem.CANDIDATE, candidateDTO.getId(), null, null, false)).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Candidate with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
        if (candidateDTO.getTitle() != null) {
            item.setTitle(candidateDTO.getTitle().getCode());
            item.setTitleId(candidateDTO.getTitle().getId());
        }

        item.setWorkPhone(candidateDTO.getPhone());

        SelectItem vacancyLocation = null;
        if (isNew){
            if (candidateDTO.getPositionId() != null) {
                EdsPosition position = positionManager.get(candidateDTO.getPositionId());
                if (position != null) {
                    item.setPosition(position.getName());
                    item.setPositionItem(new SelectItem(position.getObjectID(), position.getName()));
                }
            }

           item.setPhotoId(candidateDTO.getPhotoId());

            EdsVacancy vacancy = vacancyManager.getVacancyByNumber(candidateDTO.getVacancyNumber());
            if (vacancy != null) {
                ArrayList<SelectItem> vacancies = new ArrayList<>();
                vacancies.add(new SelectItem(vacancy.getObjectID(),vacancy.getName()));
                item.setVacancies(vacancies);
                if (item.getPosition() == null || item.getPosition().isEmpty()) {
                    EdsPosition vacancyPosition = vacancy.getPosition();
                    if (vacancyPosition!= null) {
                        item.setPosition(vacancyPosition.getName());
                        item.setPositionItem(new SelectItem(vacancyPosition.getObjectID(),vacancyPosition.getName()));
                    }
                    EdsDepartment vacancyDepartment = vacancy.getDepartment();
                    if (vacancyDepartment!= null) {
                        item.setDepartment(vacancyDepartment.getName());
                        item.setDepartmentItem(new SelectItem(vacancyDepartment.getObjectID(),vacancyDepartment.getName()));
                    }
                }
                vacancyLocation = vacancy.getLocation() != null ? vacancy.getLocation().getAsSelectItem() : null;
            }

            if (candidateDTO.getTimeSlotId() != null && candidateDTO.getTimeSlotId() > 0) {
                item.setTimeSlotItem(new SelectItem(candidateDTO.getTimeSlotId(),""));
            }
            boolean married = candidateDTO.isMarried();
            item.setMartialStatusList(item.getMartialStatusList());
            for (SelectItem status : item.getMartialStatusList()) {
                if ("MARRIED".equals(status.getDescription()) && married) {
                    item.setMartialStatusId(status.getId());
                    item.setMartialStatus(status.getName());
                    break;
                } else if ("SINGLE".equals(status.getDescription()) && !married) {
                    item.setMartialStatusId(status.getId());
                    item.setMartialStatus(status.getName());
                    break;
                }
            }

        }

        if (candidateDTO.getNumber() != null) {
            NumberData candidate_number = item.getNumberData();
            candidate_number.setNumberString(candidateDTO.getNumber());
            item.setNumberData(candidate_number);
        }

        item.setFirstName(candidateDTO.getFirstName());
        item.setLastName(candidateDTO.getLastName());
        item.setBirthDate(candidateDTO.getDateOfBirth() != null ? new DateNonConvertable(candidateDTO.getDateOfBirth()) : null);

        if (candidateDTO.getStatus() != null && item.getCandidateStatuses() != null) {
            item.getCandidateStatuses();
            for (SelectItem status : item.getCandidateStatuses()) {
                if (status.getId().equals(candidateDTO.getStatus().getId()) || status.getDescription().equals(candidateDTO.getStatus().getCode()) || status.getName().equals(candidateDTO.getStatus().getName()) || status.getCode().equals(candidateDTO.getStatus().getCode())) {
                    status.setSelected(true);
                    item.setCandidateStatus(status);
                    break;
                }
            }
        }

        if (candidateDTO.getProject() != null) {
            EdsProject project = null;
            if (candidateDTO.getProject().getId() != null) {
                project = projectManager.get(candidateDTO.getProject().getId());
            }
            if (candidateDTO.getProject().getName() != null && project == null) {
                List<EdsProject> projects = projectManager.getProjectByName(candidateDTO.getProject().getName());
                if (projects != null && !projects.isEmpty()) {
                    project = projects.get(0);
                }
            }
            if (candidateDTO.getProject().getCode() != null && project == null) {
                project = projectManager.getProjectByNumber(candidateDTO.getProject().getCode());
            }
            if (project != null) {
                item.setProjectItem(new SelectItem(project.getObjectID(), true));
            }
        }

        if (candidateDTO.getVacancies() != null && !candidateDTO.getVacancies().isEmpty()) {
            ArrayList<SelectItem> candidateVacancies = new ArrayList<>();
            if (item.getProjectItem() != null) {
                List<SelectItem> projectVacancies = contactService.getProjectVacancyItem(item.getObjectId(), item.getProjectItem().getId());
                if (projectVacancies != null && !projectVacancies.isEmpty()) {
                    for (VacancyDTO vacancy : candidateDTO.getVacancies()) {
                        for (SelectItem projectVacancy : projectVacancies) {
                            if ((vacancy.getId() != null & vacancy.getId().equals(projectVacancy.getId())) || (vacancy.getJobTitle() != null && vacancy.getJobTitle().equals(projectVacancy.getName()))) {
                                projectVacancy.setSelected(true);
                                candidateVacancies.add(projectVacancy);
                            }
                        }
                    }
                }
            } else {
                if (item.getVacancies() != null && !item.getVacancies().isEmpty()) {
                    for (VacancyDTO vacancy : candidateDTO.getVacancies()) {
                        for (SelectItem projectVacancy : item.getVacancies()) {
                            if ((vacancy.getId() != null & vacancy.getId().equals(projectVacancy.getId())) || (vacancy.getJobTitle() != null && vacancy.getJobTitle().equals(projectVacancy.getName()))) {
                                projectVacancy.setSelected(true);
                                candidateVacancies.add(projectVacancy);
                            }
                        }
                    }
                }
            }
            item.setVacancies(candidateVacancies);
        }

        if (candidateDTO.getSource() != null && item.getCandidateSources() != null) {
            item.getCandidateSources();
            for (SelectItem source : item.getCandidateSources()) {
                if (source.getId().equals(candidateDTO.getSource().getId()) || source.getDescription().equals(candidateDTO.getSource().getCode()) || source.getName().equals(candidateDTO.getSource().getName())) {
                    source.setSelected(true);
                    item.setCandidateSource(source);
                    break;
                }
            }
        }

        if (candidateDTO.getWorkExperience() != null) {
            item.setWorkExperienceMonthOrYear(candidateDTO.getWorkExperience().getCode().equalsIgnoreCase("MONTH") ? 1 : 2);
            item.setWorkExperience(candidateDTO.getWorkExperience().getId());
        }

        item.setExpectedSalary(candidateDTO.getExpectedSalary());
        item.setStartSalary(candidateDTO.getStartSalary());
        item.setCurrentEmployer(candidateDTO.getCurrentEmployer());
        item.setSkills(candidateDTO.getSkills());

        if (candidateDTO.getLocation() != null && item.getLocations() != null) {
            for (SelectItem location : item.getLocations()) {
                if (location.getId().equals(candidateDTO.getLocation().getId()) || location.getName().equals(candidateDTO.getLocation().getName())) {
                    location.setSelected(true);
                    item.setPreferredLocation(location);
                    break;
                }
            }
        }

        if (item.getPreferredLocation() == null && vacancyLocation!=null) {
            item.setPreferredLocation(vacancyLocation);
        }



        if (candidateDTO.getOwner() != null && item.getLeadAssignees() != null) {
            item.getLeadAssignees();
            for (SelectItem owner : item.getLeadAssignees()) {
                if (owner.getId().equals(candidateDTO.getOwner().getId()) || owner.getName().equals(candidateDTO.getOwner().getName())) {
                    item.setOwner(owner.getName());
                    item.setOwnerId(owner.getId());
                    break;
                }
            }
        }

        if (candidateDTO.getLanguages() != null && !candidateDTO.getLanguages().isEmpty()) {
            ArrayList<SpokenLanguageItem> spokenLanguageItems = new ArrayList<>();
            SpokenLanguageTO spokenLanguages = allInOneService.getLanguagesWithLevels();
            if (spokenLanguages != null) {
                for (LanguagesDto language : candidateDTO.getLanguages()) {
                    SpokenLanguageItem newLanguage = new SpokenLanguageItem();
                    for (SelectItem languageItem : spokenLanguages.getLanguages()) {
                        if (language.getLanguage().getId().equals(languageItem.getId()) || language.getLanguage().getName().equals(languageItem.getName())) {
                            newLanguage.setLanguage(languageItem);
                            break;
                        }
                    }
                    for (SelectItem languageLevel : spokenLanguages.getLanguageLevels()) {
                        if (language.getLevel().getId().equals(languageLevel.getId()) || language.getLevel().getName().equals(languageLevel.getName())) {
                            newLanguage.setLevel(languageLevel);
                            break;
                        }
                    }
                    spokenLanguageItems.add(newLanguage);
                }
            }
            item.setSpokingLanguages(spokenLanguageItems);
        } else if (candidateDTO.getSpokenLanguages() != null && !candidateDTO.getSpokenLanguages().isEmpty()) {
            ArrayList<SpokenLanguageItem> spokenLanguageItems = new ArrayList<>();
            SpokenLanguageTO spokenLanguages = allInOneService.getLanguagesWithLevels();

            if (spokenLanguages != null && spokenLanguages.getLanguages() != null && spokenLanguages.getLanguageLevels()!=null) {
                String[] langs = candidateDTO.getSpokenLanguages().split("-:-");
                for (String rawLang : langs) {
                    String langName = rawLang.trim();
                    if (langName.isEmpty()) continue;

                    SpokenLanguageItem newLanguage = new SpokenLanguageItem();

                    for (SelectItem languageItem : spokenLanguages.getLanguages()) {
                        String description = languageItem.getDescription();
                        if (description!=null) {
                            if (description.equalsIgnoreCase(langName)) {
                                newLanguage.setLanguage(languageItem);
                                break;
                            }
                        }
                    }

                    ArrayList<SelectItem> languageLevels = spokenLanguages.getLanguageLevels();
                    if (!languageLevels.isEmpty()) {
                        newLanguage.setLevel(languageLevels.get(languageLevels.size() - 1));
                    }else{
                        break;
                    }

                    if (newLanguage.getLanguage() != null) {
                        spokenLanguageItems.add(newLanguage);
                    }
                }
            }

            item.setSpokingLanguages(spokenLanguageItems);
        }

        item.setPrimaryEmail(candidateDTO.getEmail());
        item.setPrimaryPhone(candidateDTO.getPhone());

        if (candidateDTO.getImAddresses() != null && !candidateDTO.getImAddresses().isEmpty() && item.getContactImAddress() != null && item.getContactImAddress().length > 0) {
            for (IMWebAddressDto im : candidateDTO.getImAddresses()) {
                for (SelectItem selectItem : item.getContactImAddress()) {
                    if (im.getType().equalsIgnoreCase(selectItem.getName())) {
                        selectItem.setDescription(im.getAddress());
                        selectItem.setSelected(true);
                        item.addSelectedImAddresses(selectItem);
                    }
                }
            }
        }

        if (candidateDTO.getWebAddresses() != null && !candidateDTO.getWebAddresses().isEmpty()) {
            for (IMWebAddressDto webAddress : candidateDTO.getWebAddresses()) {
                int id = getWebAddressType(webAddress);
                item.addParam(Constants.CONTACT_WEBSITES, id, webAddress.getAddress());
            }
        }

        if (candidateDTO.getAllowances() != null && !candidateDTO.getAllowances().isEmpty()) {
            ArrayList<PaymentDeductionObject> allowanceList = new ArrayList<>();
            for (AllowanceDto allowance : candidateDTO.getAllowances()) {
                if (allowance.getAllowance() != null) {
                    EdsPayrollCategory payrollCategory = null;
                    if (allowance.getAllowance().getId() != null) {
                        payrollCategory = payrollCategoryManager.get(allowance.getAllowance().getId());
                    }
                    if (allowance.getAllowance().getName() != null && payrollCategory == null) {
                        payrollCategory = payrollCategoryManager.getCategoryByName(allowance.getAllowance().getName(), null);
                    }
                    if (allowance.getAllowance().getCode() != null && payrollCategory == null) {
                        payrollCategory = payrollCategoryManager.getCategoryByCode(allowance.getAllowance().getCode());
                    }
                    if (payrollCategory != null) {
                        PaymentDeductionObject object = new PaymentDeductionObject();
                        object.setCategoryItem(payrollCategory.createPaymentDeductionSelectItem());
                        object.setType(0);
                        object.setPaymentAmount(allowance.getAmount());

                        allowanceList.add(object);
                    }
                }
            }
            item.setAllowanceCategories(allowanceList);
        }

        if (candidateDTO.getNotes() != null && !candidateDTO.getNotes().isEmpty()) {
            ArrayList<HistoryListItem> notes = new ArrayList<>();
            EdsUser user = userManager.getUser();
            String username = user != null ? user.getUserName() : "";
            candidateDTO.getNotes().forEach(n -> notes.add(ConvertUtils.toEntity(n, username)));
            item.setNotes(notes);
        }

        if (candidateDTO.getAddresses() != null && !candidateDTO.getAddresses().isEmpty()) {
            ArrayList<Address> addresses = new ArrayList<>();
            candidateDTO.getAddresses().forEach(a -> addresses.add(ConvertUtils.toEntity(a)));
            item.setAddresses(addresses);
        }

        EdsCrmContact edsCandidate = null;
        if (!isNew && candidateDTO.getId() != null) {
            edsCandidate = candidateManager.get(candidateDTO.getId());
        }else {
            item.setCandaidateNewFromApi(true);
        }
        ArrayList<CompanyCustomFieldItem> companyCustomFieldItems = CustomFieldsUtils.convertCustomFieldsCanidate(candidateDTO.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Candidate), !isNew && edsCandidate != null ? edsCandidate.getCustomFields() : null);

        item.setCustomFields(companyCustomFieldItems);
        item.setContactType(ContactListItem.CANDIDATE);
        contactService.saveCandidate(item);

        candidateDTO.setNumber(item.getNumberData().getNumberString());
        candidateDTO.setCreatedAt(item.getCreatedDate());
        candidateDTO.setUpdatedAt(item.getUpdatedDate());
        candidateDTO.setId(item.getObjectId());
        return candidateDTO;
    }

    private EdsCrmContact getItem(Integer id, String objectKey) throws RestException {
        return (id != null ? Optional.ofNullable(candidateManager.get(id)) : Optional.ofNullable(candidateManager.getByObjectKey(objectKey)))
                .orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Candidate with this id or object key is not found", NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Transactional
    public CandidateDTO savePatch(CandidateDTO candidateDTO) throws RestException {
        EdsCrmContact candidate = getItem(candidateDTO.getId(), candidateDTO.getObjectKey());
        if (candidate == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Candidate with this id is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        ContactListItem item = contactService.editContact(ContactListItem.CANDIDATE, candidate.getObjectID(), null, null, false);
        if (candidateDTO.getTitle() != null) {
            Optional.ofNullable(candidateDTO.getTitle().getCode()).ifPresent(item::setTitle);
            Optional.ofNullable(candidateDTO.getTitle().getId()).ifPresent(item::setTitleId);
        }

        if (candidateDTO.getNumber() != null) {
            NumberData candidate_number = item.getNumberData();
            candidate_number.setNumberString(candidateDTO.getNumber());
            item.setNumberData(candidate_number);
        }

        Optional.ofNullable(candidateDTO.getFirstName()).ifPresent(item::setFirstName);
        Optional.ofNullable(candidateDTO.getLastName()).ifPresent(item::setLastName);

        Optional.ofNullable(candidateDTO.getDateOfBirth())
                .map(DateNonConvertable::new)
                .ifPresent(item::setBirthDate);

        if (candidateDTO.getStatus() != null && item.getCandidateStatuses() != null) {
            for (SelectItem status : item.getCandidateStatuses()) {
                if (status.getId().equals(candidateDTO.getStatus().getId()) || status.getDescription().equals(candidateDTO.getStatus().getCode()) || status.getName().equals(candidateDTO.getStatus().getName())) {
                    status.setSelected(true);
                    item.setCandidateStatus(status);
                    break;
                }
            }
        }

        if (candidateDTO.getProject() != null) {
            EdsProject project = null;
            if (candidateDTO.getProject().getId() != null) {
                project = projectManager.get(candidateDTO.getProject().getId());
            }
            if (candidateDTO.getProject().getName() != null && project == null) {
                List<EdsProject> projects = projectManager.getProjectByName(candidateDTO.getProject().getName());
                if (projects != null && !projects.isEmpty()) {
                    project = projects.get(0);
                }
            }
            if (candidateDTO.getProject().getCode() != null && project == null) {
                project = projectManager.getProjectByNumber(candidateDTO.getProject().getCode());
            }
            if (project != null) {
                item.setProjectItem(new SelectItem(project.getObjectID(), true));
            }
        }

        if (candidateDTO.getVacancies() != null && !candidateDTO.getVacancies().isEmpty()) {
            ArrayList<SelectItem> candidateVacancies = new ArrayList<>();
            if (item.getProjectItem() != null) {
                List<SelectItem> projectVacancies = contactService.getProjectVacancyItem(item.getObjectId(), item.getProjectItem().getId());
                if (projectVacancies != null && !projectVacancies.isEmpty()) {
                    for (VacancyDTO vacancy : candidateDTO.getVacancies()) {
                        for (SelectItem projectVacancy : projectVacancies) {
                            if ((vacancy.getId() != null & vacancy.getId().equals(projectVacancy.getId())) || (vacancy.getJobTitle() != null && vacancy.getJobTitle().equals(projectVacancy.getName()))) {
                                projectVacancy.setSelected(true);
                                candidateVacancies.add(projectVacancy);
                            }
                        }
                    }
                }
            } else {
                if (item.getVacancies() != null && !item.getVacancies().isEmpty()) {
                    for (VacancyDTO vacancy : candidateDTO.getVacancies()) {
                        for (SelectItem projectVacancy : item.getVacancies()) {
                            if ((vacancy.getId() != null & vacancy.getId().equals(projectVacancy.getId())) || (vacancy.getJobTitle() != null && vacancy.getJobTitle().equals(projectVacancy.getName()))) {
                                projectVacancy.setSelected(true);
                                candidateVacancies.add(projectVacancy);
                            }
                        }
                    }
                }
            }
            item.setVacancies(candidateVacancies);
        }

        if (candidateDTO.getSource() != null && item.getCandidateSources() != null) {
            item.getCandidateSources();
            for (SelectItem source : item.getCandidateSources()) {
                if (source.getId().equals(candidateDTO.getSource().getId()) || source.getDescription().equals(candidateDTO.getSource().getCode()) || source.getName().equals(candidateDTO.getSource().getName())) {
                    source.setSelected(true);
                    item.setCandidateSource(source);
                    break;
                }
            }
        }

        if (candidateDTO.getWorkExperience() != null) {
            Optional.ofNullable(candidateDTO.getWorkExperience().getCode())
                    .map(c -> c.equalsIgnoreCase("MONTH") ? 1 : 2)
                    .ifPresent(item::setWorkExperienceMonthOrYear);
            Optional.ofNullable(candidateDTO.getWorkExperience().getId()).ifPresent(item::setWorkExperience);
        }

        Optional.ofNullable(candidateDTO.getExpectedSalary()).ifPresent(item::setExpectedSalary);
        Optional.ofNullable(candidateDTO.getCurrentEmployer()).ifPresent(item::setCurrentEmployer);
        Optional.ofNullable(candidateDTO.getSkills()).ifPresent(item::setSkills);

        if (candidateDTO.getLocation() != null && item.getLocations() != null) {
            item.getLocations();
            for (SelectItem location : item.getLocations()) {
                if (location.getId().equals(candidateDTO.getLocation().getId()) || location.getName().equals(candidateDTO.getLocation().getName())) {
                    location.setSelected(true);
                    item.setPreferredLocation(location);
                    break;
                }
            }
        }

        if (candidateDTO.getOwner() != null && item.getLeadAssignees() != null) {
            item.getLeadAssignees();
            for (SelectItem owner : item.getLeadAssignees()) {
                if (owner.getId().equals(candidateDTO.getOwner().getId()) || owner.getName().equals(candidateDTO.getOwner().getName())) {
                    item.setOwner(owner.getName());
                    item.setOwnerId(owner.getId());
                    break;
                }
            }
        }

        if (candidateDTO.getLanguages() != null && !candidateDTO.getLanguages().isEmpty()) {
            ArrayList<SpokenLanguageItem> spokenLanguageItems = new ArrayList<>();
            SpokenLanguageTO spokenLanguages = allInOneService.getLanguagesWithLevels();
            if (spokenLanguages != null) {
                for (LanguagesDto language : candidateDTO.getLanguages()) {
                    SpokenLanguageItem newLanguage = new SpokenLanguageItem();
                    for (SelectItem languageItem : spokenLanguages.getLanguages()) {
                        if (language.getLanguage().getId().equals(languageItem.getId()) || language.getLanguage().getName().equals(languageItem.getName())) {
                            newLanguage.setLanguage(languageItem);
                            break;
                        }
                    }
                    for (SelectItem languageLevel : spokenLanguages.getLanguageLevels()) {
                        if (language.getLevel().getId().equals(languageLevel.getId()) || language.getLevel().getName().equals(languageLevel.getName())) {
                            newLanguage.setLevel(languageLevel);
                            break;
                        }
                    }
                    spokenLanguageItems.add(newLanguage);
                }
            }
            item.setSpokingLanguages(spokenLanguageItems);
        }

        Optional.ofNullable(candidateDTO.getEmail()).ifPresent(item::setPrimaryEmail);
        Optional.ofNullable(candidateDTO.getPhone()).ifPresent(item::setPrimaryPhone);

        if (candidateDTO.getImAddresses() != null && !candidateDTO.getImAddresses().isEmpty() && item.getContactImAddress() != null && item.getContactImAddress().length > 0) {
            for (IMWebAddressDto im : candidateDTO.getImAddresses()) {
                for (SelectItem selectItem : item.getContactImAddress()) {
                    if (im.getType().equalsIgnoreCase(selectItem.getName())) {
                        selectItem.setDescription(im.getAddress());
                        selectItem.setSelected(true);
                        item.addSelectedImAddresses(selectItem);
                    }
                }
            }
        }

        if (candidateDTO.getWebAddresses() != null && !candidateDTO.getWebAddresses().isEmpty()) {
            for (IMWebAddressDto webAddress : candidateDTO.getWebAddresses()) {
                item.addParam(Constants.CONTACT_WEBSITES, getWebAddressType(webAddress), webAddress.getAddress());
            }
        }

        if (candidateDTO.getAllowances() != null && !candidateDTO.getAllowances().isEmpty()) {
            ArrayList<PaymentDeductionObject> allowanceList = new ArrayList<>();
            for (AllowanceDto allowance : candidateDTO.getAllowances()) {
                if (allowance.getAllowance() != null) {
                    EdsPayrollCategory payrollCategory = null;
                    if (allowance.getAllowance().getId() != null) {
                        payrollCategory = payrollCategoryManager.get(allowance.getAllowance().getId());
                    }
                    if (allowance.getAllowance().getName() != null && payrollCategory == null) {
                        payrollCategory = payrollCategoryManager.getCategoryByName(allowance.getAllowance().getName(), null);
                    }
                    if (allowance.getAllowance().getCode() != null && payrollCategory == null) {
                        payrollCategory = payrollCategoryManager.getCategoryByCode(allowance.getAllowance().getCode());
                    }
                    if (payrollCategory != null) {
                        PaymentDeductionObject object = new PaymentDeductionObject();
                        object.setCategoryItem(payrollCategory.createPaymentDeductionSelectItem());
                        object.setType(0);
                        object.setPaymentAmount(allowance.getAmount());

                        allowanceList.add(object);
                    }
                }
            }
            item.setAllowanceCategories(allowanceList);
        }

        if (candidateDTO.getNotes() != null && !candidateDTO.getNotes().isEmpty()) {
            ArrayList<HistoryListItem> notes = new ArrayList<>();
            EdsUser user = userManager.getUser();
            String username = user != null ? user.getUserName() : "";
            candidateDTO.getNotes().forEach(n -> notes.add(ConvertUtils.toEntity(n, username)));
            item.setNotes(notes);
        }

        if (candidateDTO.getAddresses() != null && !candidateDTO.getAddresses().isEmpty()) {
            ArrayList<Address> addresses = new ArrayList<>();
            candidateDTO.getAddresses().forEach(a -> addresses.add(ConvertUtils.toEntity(a)));
            item.setAddresses(addresses);
        }

        if (candidateDTO.getId() != null) {
            EdsCrmContact edsCandidate = candidateManager.get(candidateDTO.getId());
            if (edsCandidate != null) {
                item.setCustomFields(CustomFieldsUtils.convertCustomFields(candidateDTO.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Candidate), edsCandidate.getCustomFields()));
            }
        }
        item.setContactType(ContactListItem.CANDIDATE);
        contactService.saveCandidate(item);

        candidateDTO.setNumber(item.getNumberData().getNumberString());
        candidateDTO.setCreatedAt(item.getCreatedDate());
        candidateDTO.setUpdatedAt(item.getUpdatedDate());
        candidateDTO.setId(item.getObjectId());
        return candidateDTO;
    }

    private int getWebAddressType(IMWebAddressDto webAddress) {
        return switch (webAddress.getType().toUpperCase()) {
            case "WORK" -> Constants.G_WORK;
            case "HOME PAGE" -> Constants.G_HOME_PAGE;
            case "FTP" -> Constants.G_FTP;
            case "BLOG" -> Constants.G_BLOG;
            case "PROFILE" -> Constants.G_PROFILE;
            case "OTHER" -> Constants.G_OTHER;
            case "LINKEDIN" -> Constants.G_LINKEDIN;
            case "FACEBOOK" -> Constants.G_FACEBOOK;
            case "TWITTER" -> Constants.G_TWITTER;
            case "INSTAGRAM" -> Constants.G_INSTAGRAM;
            default -> Constants.G_HOME;
        };
    }
}
