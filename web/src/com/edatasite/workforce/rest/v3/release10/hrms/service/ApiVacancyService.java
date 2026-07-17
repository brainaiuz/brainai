package com.edatasite.workforce.rest.v3.release10.hrms.service;


import com.edatasite.workforce.core.domain.EdsJobFamily;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.solr.component.VacancySolrComponent;
import com.edatasite.workforce.core.solr.document.VacancySolrDoc;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.LocationItem;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.gwt.hrms.server.app.RecruitmentServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.db.JobFamilyManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.VacancyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User : Akhror on 29/06/2021
 */
@Service
public class ApiVacancyService implements Constants {

    private final RecruitmentService recruitmentService;
    private final AttachmentUtilsManager attachmentUtilsManager;
    private final VacancyManager vacancyManager;
    private final ReferenceManager referenceManager;
    private final CountryManager countryManager;
    private final ProjectManager projectManager;
    private final JobFamilyManager jobFamilyManager;
    private final UserManager userManager;
    private final CommonService commonService;
    private final RecruitmentServiceLocal recruitmentServiceLocal;
    private final VacancySolrComponent vacancySolrComponent;

    @Autowired
    public ApiVacancyService(RecruitmentService recruitmentService, AttachmentUtilsManager attachmentUtilsManager, VacancyManager vacancyManager, ReferenceManager referenceManager, CountryManager countryManager, ProjectManager projectManager, JobFamilyManager jobFamilyManager, UserManager userManager, CommonService commonService, RecruitmentServiceLocal recruitmentServiceLocal, VacancySolrComponent vacancySolrComponent) {
        this.recruitmentService = recruitmentService;
        this.attachmentUtilsManager = attachmentUtilsManager;
        this.vacancyManager = vacancyManager;
        this.referenceManager = referenceManager;
        this.countryManager = countryManager;
        this.projectManager = projectManager;
        this.jobFamilyManager = jobFamilyManager;
        this.userManager = userManager;
        this.commonService = commonService;
        this.recruitmentServiceLocal = recruitmentServiceLocal;
        this.vacancySolrComponent = vacancySolrComponent;
    }

    public ListResultTO<VacancyDTO> getVacanciesList(ListingFilterParameter fp) {
//        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_VACANCY_CORE);
//        QueryResponse resp = null;
//        try {
//            resp = server.query(recruitmentServiceLocal.getSolrQueryForVacancy(fp), SolrRequest.METHOD.POST);
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
        Page<VacancySolrDoc> vacancySolrDocs = vacancySolrComponent.getList(fp);

        ListResultTO<VacancyDTO> vacancies = new ListResultTO<>();
        if (vacancySolrDocs != null) {
            List<Integer> ids = vacancySolrDocs.getContent().stream().map(doc -> Objects.requireNonNull(doc.getVacancyId())).collect(Collectors.toList());
            ArrayList<VacancyDTO> items = new ArrayList<>();
            List<Integer> existingVacancyIDs = vacancyManager.getVacancyIdsForSolr(ids);
            ids.forEach(id -> {
                if (existingVacancyIDs.contains(id)) {
                    VacancyItem item = recruitmentService.getVacancyItem(id);
                    List<FileResource> files = attachmentUtilsManager.getAttachments(F_VACANCY, id, id);
                    if (mapHasValueForLang(item.getDescriptionLocalize(), fp.getLanguage())) {
                        item.setDescription(item.getDescriptionLocalize().get(fp.getLanguage()));
                    }
                    if (mapHasValueForLang(item.getJobRequirementLocalize(), fp.getLanguage())) {
                        item.setJobRequirements(item.getJobRequirementLocalize().get(fp.getLanguage()));
                    }
                    if (mapHasValueForLang(item.getResponsibilitiesLocalize(), fp.getLanguage())) {
                        item.setResponsibility(item.getResponsibilitiesLocalize().get(fp.getLanguage()));
                    }
                    items.add(ConvertUtils.toVacancyDTO(item, files));
                }
            });
            vacancies.setTotalNumber(items.size());
            vacancies.setItems(items);
        }
        return vacancies;
    }

    private boolean mapHasValueForLang(HashMap<String, String> map, String lang) {
        return lang != null && map != null && map.get(lang) != null && !map.get(lang).isEmpty();
    }

    @Transactional(readOnly = true)
    public VacancyDTO getById(final Integer id) throws RestException {
        Optional.ofNullable(vacancyManager.get(id)).orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Vacancy with the given Id not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST));
        VacancyItem vacancy = recruitmentService.getVacancyItem(id);
        List<FileResource> files = attachmentUtilsManager.getAttachments(F_VACANCY, id, id);
        return ConvertUtils.toVacancyDTO(vacancy, files);
    }

    @Transactional
    public VacancyDTO save(final VacancyDTO dto, final boolean isNew) {
        VacancyItem item = recruitmentService.getVacancyItem(dto.getId());

        item.setJobTitle(dto.getJobTitle());
        item.setDescription(dto.getDescription());
        item.setJobRequirements(dto.getRequirements());

        if (dto.getStatus() != null && item.getStatuses() != null) {
            item.getStatuses();
            for (SelectItem status : item.getStatuses()) {
                if (status.getId().equals(dto.getStatus().getId()) || status.getDescription().equals(dto.getStatus().getCode()) || status.getName().equals(dto.getStatus().getName())) {
                    status.setSelected(true);
                    item.setStatus(new ReferenceItem(status.getId(), status.getName()));
                    break;
                }
            }
        }

        if (dto.getType() != null) {
            EdsReference type = null;
            if (dto.getType().getId() != null) {
                type = referenceManager.getReference(dto.getType().getId());
            } else if (dto.getType().getName() != null) {
                type = referenceManager.findByParentCodeAndName(EdsVacancy.VACANCY_TYPE, dto.getType().getName());
            } else if (dto.getType().getCode() != null) {
                type = referenceManager.getByCode(dto.getType().getCode());
            }
            if (type != null) {
                item.setVacancyType(type.getObjectID());
                item.setVacancyTypeName(type.getName());
            }
        }

        item.setVacantPlaces(dto.getVacantPlaceCount());
        item.setProposedSalary(dto.getProposedSalary());

        if (dto.getGender() != null && !"".equals(dto.getGender())) {
            if ("male".equalsIgnoreCase(dto.getGender())) {
                item.setGender("Male");
            } else if ("female".equalsIgnoreCase(dto.getGender())) {
                item.setGender("Female");
            }
        }
        item.setStartDate(dto.getStartDate());
        item.setEndDate(dto.getEndDate());

        if (dto.getLocation() != null && item.getLocations() != null) {
            item.getLocations();
            for (SelectItem location : item.getLocations()) {
                if (location.getId().equals(dto.getLocation().getId()) || location.getName().equals(dto.getLocation().getName())) {
                    location.setSelected(true);
                    item.setLocationItem(new LocationItem(location.getId().toString(), null, location.getName()));
                    break;
                }
            }
        }

        if (dto.getPosition() != null && item.getPositions() != null) {
            item.getPositions();
            for (SelectItem position : item.getPositions()) {
                if (position.getId().equals(dto.getPosition().getId()) || position.getName().equals(dto.getPosition().getName())) {
                    position.setSelected(true);
                    item.setPositionItem(new PositionItem(position.getId(), position.getName()));
                    break;
                }
            }
        }

//        if (dto.getCountry() != null) {
//            EdsCountry country = null;
//            if (dto.getCountry().getId() != null) {
//                country = countryManager.get(dto.getCountry().getId());
//            } else if (dto.getCountry().getName() != null) {
//                country = countryManager.getCountryByName(dto.getCountry().getName());
//            }
////            if (country != null) {
////                item.setCountryId(country.getObjectID());
////                item.setCountryName(country.getName());
////            }
//        }
////
////        if (dto.getEmbassy() != null && dto.getEmbassy().getId() != null) {
////            EdsEmbassy embassy = countryManager.getEmbassyById(dto.getEmbassy().getId());
////            if (embassy != null) {
////                item.setEmbassyId(embassy.getObjectID());
////                item.setEmbassyName(embassy.getName());
////            }
////        }

        item.setContractFrom(dto.getContractStartDate());
        item.setContractTo(dto.getContractEndDate());

        if (dto.getProject() != null) {
            EdsProject project = null;
            if (dto.getProject().getId() != null) {
                project = projectManager.get(dto.getProject().getId());
            } else if (dto.getProject().getName() != null) {
                List<EdsProject> projects = projectManager.getProjectByName(dto.getProject().getName());
                if (projects != null && !projects.isEmpty()) {
                    project = projects.get(0);
                }
            } else if (dto.getProject().getCode() != null) {
                project = projectManager.getProjectByNumber(dto.getProject().getCode());
            }
            if (project != null) {
                item.setProjectId(project.getObjectID());
                item.setProjectName(project.getName());
            }
        }

        if (dto.getManager() != null && item.getManagers() != null) {
            item.getManagers();
            for (SelectItem manager : item.getManagers()) {
                if (manager.getId().equals(dto.getManager().getId()) || manager.getName().equals(dto.getManager().getName())) {
                    manager.setSelected(true);
                    item.setManager(new SelectItem(manager.getId(), manager.getName()));
                    break;
                }
            }
        }

//        if (dto.getReligion() != null) {
//            EdsReference religion = null;
//            if (dto.getReligion().getId() != null) {
//                religion = referenceManager.getReference(dto.getReligion().getId());
//            } else if (dto.getReligion().getName() != null) {
//                religion = referenceManager.findByParentCodeAndName(EdsVacancy.VACANCY_RELIGION, dto.getReligion().getName());
//            } else if (dto.getReligion().getCode() != null) {
//                religion = referenceManager.getByCode(dto.getReligion().getCode());
//            }
////            if (religion != null) {
////                item.setReligionId(religion.getObjectID());
////                item.setReligionName(religion.getName());
////            }
//        }

        if (dto.getRequiredDegree() != null && item.getRequiredDegrees() != null) {
            item.getRequiredDegrees();
            for (SelectItem degree : item.getRequiredDegrees()) {
                if (degree.getId().equals(dto.getRequiredDegree().getId()) || degree.getName().equals(dto.getRequiredDegree().getName())) {
                    degree.setSelected(true);
                    item.setRequiredDegree(new ReferenceItem(degree.getId(), degree.getName()));
                    break;
                }
            }
        }
        item.setResponsibility(dto.getResponsibilities());

        if (dto.getJobFamily() != null && dto.getJobFamily().getId() != null) {
            EdsJobFamily jobFamily = jobFamilyManager.get(dto.getJobFamily().getId());
            item.setJobfamily(new SelectItem(jobFamily.getObjectID(), jobFamily.getName()));
        }
        if (dto.getJobType() != null && item.getTimeTypes() != null) {
            item.getTimeTypes();
            for (SelectItem jobType : item.getTimeTypes()) {
                if (jobType.getId().equals(dto.getJobType().getId()) || jobType.getName().equals(dto.getJobType().getName())) {
                    jobType.setSelected(true);
                    item.setJobType(new SelectItem(jobType.getId(), jobType.getName()));
                    break;
                }
            }
        }

        if (dto.getNotes() != null && !dto.getNotes().isEmpty()) {
            ArrayList<HistoryListItem> notes = new ArrayList<>();
            EdsUser user = userManager.getUser();
            String username = user != null ? user.getUserName() : "";
            dto.getNotes().forEach(n -> notes.add(ConvertUtils.toEntity(n, username)));
            item.setVacancyNotes(notes);
        }

        EdsVacancy edsVacancy = null;
        if (!isNew && dto.getId() != null) {
            edsVacancy = vacancyManager.get(dto.getId());
        }
        item.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(dto.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Vacancy), !isNew && edsVacancy != null ? edsVacancy.getVacancyCustomFields() : null));

        recruitmentService.saveVacancy(item);
        dto.setId(item.getObjectID());
        dto.setNumber(item.getNumberData().getNumberString());
        return dto;
    }
}
