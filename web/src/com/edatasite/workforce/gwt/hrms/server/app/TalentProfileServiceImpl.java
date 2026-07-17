package com.edatasite.workforce.gwt.hrms.server.app;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsAward;
import com.edatasite.workforce.core.domain.EdsEducation;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsTalentProfileCustomFields;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.gwt.assessment.server.app.AssessmentServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.TalentProfileCFManager;
import com.edatasite.workforce.gwt.core.server.db.talentprofile.AwardManager;
import com.edatasite.workforce.gwt.core.server.db.talentprofile.EducationManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.AwardItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.EducationItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileEnum;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileListItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Dilshod Madrahimov
 * Date: 6/22/12
 * Time: 4:38 PM
 */

@Transactional
@Service("talentProfileService")
public class TalentProfileServiceImpl implements TalentProfileService {

    private static final Logger log = LoggerFactory.getLogger(TalentProfileServiceImpl.class);

    @Autowired
    private AwardManager awardManager;
    @Autowired
    private EducationManager educationManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private AssessmentServiceLocal assessmentServiceLocal;
    @Autowired
    private TalentProfileCFManager talentProfileCFManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CrmContactManager crmContactManager;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<TalentProfileListItem> getTalentProfileList(ListingFilterParameter filterParameter) {
        return awardManager.getTalentProfileList(filterParameter);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EducationItem getEducation(Integer objectId) {
        EducationItem item = new EducationItem();
        if (objectId != null) {
            EdsEducation education = educationManager.get(objectId);
            item.setObjectId(education.getObjectID());
            if (education.getCandidate() != null) {
                item.setFromCandidate(true);
            } else {
                item.setEmployeeId(education.getEmployee().getObjectID());
            }
            item.setSchoolName(education.getSchool());
            item.setFieldOfStudy(education.getFieldOfStudy());
            if (education.getStartDate() != null) {
                item.setStartDate(new DateNonConvertable(education.getStartDate()));
            }
            if (education.getEndDate() != null) {
                item.setEndDate(new DateNonConvertable(education.getEndDate()));
            }
            item.setActivitiesAndSocieties(education.getActivityAndSocieties());
            item.setComment(education.getComment());
            if (education.getCountry() != null) {
                item.setCountry(education.getCountry().getAsSelectItem());
            }
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.TalentProfileView);
            item.setCustomFieldItems((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(education.getCustomFields(), customFieldsItems));
            if (education.getDegree() != null) {
                item.setDegree(new ReferenceItem(education.getDegree().getObjectID(), education.getDegree().getName()));
            }
        }
        item.setDegrees(commonService.convertReference2SelectItem(EdsVacancy.VACANCY_DEGREES, true, null));
        return item;
    }

    @Transactional
    public void saveEducation(EducationItem item) {
        EdsEmployee employee = null;
        EdsCrmContact candidate = null;
        EdsEducation education = new EdsEducation();
        if (item.getObjectId() != null) {
            education = educationManager.get(item.getObjectId());
        }

        if (item.isFromCandidate() && item.getEmployeeId() != null) {
            candidate = crmContactManager.get(item.getEmployeeId());
        } else {
            if (item.getEmployeeId() != null) {
                employee = employeeManager.get(item.getEmployeeId());
            }
            if (employee == null) {
                employee = employeeManager.getUser().getEmployee();
            }
        }
        if (item.getCountry() != null) {
            education.setCountry(countryManager.get(item.getCountry().getId()));
        } else {
            education.setCountry(null);
        }
        if (item.getObjectId() == null) {
            education.setEmployee(employee);
        }
        education.setSchool(item.getSchoolName());
        if (candidate != null) {
            education.setCandidate(candidate);
        }
        if (item.getDegree() != null) {
            education.setDegree(referenceManager.get(item.getDegree().getObjectID()));
        }
        education.setFieldOfStudy(item.getFieldOfStudy());
        if (item.getStartDate() != null) {
            education.setStartDate(item.getStartDate().getNonConvertedDate());
        }
        if (item.getEndDate() != null) {
            education.setEndDate(item.getEndDate().getNonConvertedDate());
        }
        EdsTalentProfileCustomFields customFields = createTalentProfileCustomFields(item.getCustomFieldItems());
        education.setCustomFields(customFields);

        education.setActivityAndSocieties(item.getActivitiesAndSocieties());
        education.setComment(item.getComment());
        education.setLastUpdateDate(new Date());

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEducation.class.getSimpleName());

        if (item.getObjectId() != null) {
            educationManager.update(education);
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(education.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Update education");
        } else {
            educationManager.create(education);
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(education.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Add education");

        }
    }

    private Boolean deleteEducation(Integer educationID) {
        EdsEducation education = educationManager.get(educationID);
        if (education != null) {
            education.setDeleted(true);
            try {
                educationManager.update(education);
            } catch (Exception e) {
                log.error("Error occurred while deleting education", e);
                return false;
            }
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsEducation.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(educationID);
            ServerUtils.kpiLog(log, kpiLog, "Delete education");
            return true;
        }
        return false;
    }

    private EdsTalentProfileCustomFields createTalentProfileCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsTalentProfileCustomFields talentProfileCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                talentProfileCustomFields = talentProfileCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                talentProfileCustomFields = new EdsTalentProfileCustomFields();
                talentProfileCFManager.create(talentProfileCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(talentProfileCustomFields, customFieldItems);
            return talentProfileCustomFields;
        }
        return null;
    }

    private Boolean deleteAward(Integer awardID) {
        EdsAward award = awardManager.get(awardID);
        if (award != null) {
            award.setDeleted(true);
            try {
                awardManager.update(award);
            } catch (Exception e) {
                log.error("Error occurred while deleting award", e);
                return false;
            }
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsAward.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(awardID);
            ServerUtils.kpiLog(log, kpiLog, "Delete award");
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean deleteTalentProfileItem(Integer objectID, TalentProfileEnum type) {
        switch (type) {
            case EDUCATION -> {
                return deleteEducation(objectID);
            }
            case AWARD -> {
                return deleteAward(objectID);
            }
            case COMPETENCY -> {
                assessmentServiceLocal.deleteCompetency(objectID);
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AwardItem getAward(Integer objectId) {
        AwardItem item = new AwardItem();
        if (objectId != null) {
            EdsAward award = awardManager.get(objectId);
            item.setObjectId(award.getObjectID());
            item.setAwardName(award.getName());
            if (award.getEmployee() != null) {
                item.setEmployeeId(award.getEmployee().getObjectID());
            }
            if (award.getCountry() != null) {
                item.setCountry(award.getCountry().getAsSelectItem());
            }
            if (award.getIssueDate() != null) {
                item.setIssueDate(new DateNonConvertable(award.getIssueDate()));
            }
            if (award.getExpiryDate() != null) {
                item.setExpiryDate(new DateNonConvertable(award.getExpiryDate()));
            }
            item.setDescription(award.getDescription());
            item.setCity(award.getCity());
        }
        return item;
    }

    @Transactional
    public void saveAward(AwardItem item) {
        EdsAward award = new EdsAward();
        EdsEmployee employee = null;
        if (item.getEmployeeId() != null) {
            employee = employeeManager.get(item.getEmployeeId());
        }
        if (employee == null) {
            employee = employeeManager.getUser().getEmployee();
        }
        if (item.getObjectId() != null) {
            award = awardManager.get(item.getObjectId());
        }

        if (item.getCountry() != null) {
            award.setCountry(countryManager.get(item.getCountry().getId()));
        } else {
            award.setCountry(null);
        }
        if (item.getObjectId() == null) {
            award.setEmployee(employee);
        }
        award.setName(item.getAwardName());
        if (item.getIssueDate() != null) {
            award.setIssueDate(item.getIssueDate().getNonConvertedDate());
        } else {
            award.setIssueDate(null);
        }
        if (item.getExpiryDate() != null) {
            award.setExpiryDate(item.getExpiryDate().getNonConvertedDate());
        } else {
            award.setExpiryDate(null);
        }
        award.setDescription(item.getDescription());
        award.setCity(item.getCity());
        award.setLastUpdateDate(new Date());

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsAward.class.getSimpleName());
        if (item.getObjectId() != null) {
            awardManager.update(award);
            kpiLog.setEntityId(award.getObjectID());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(log, kpiLog, "Update award");
        } else {
            awardManager.create(award);
            kpiLog.setEntityId(award.getObjectID());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add award");
        }
    }
}
