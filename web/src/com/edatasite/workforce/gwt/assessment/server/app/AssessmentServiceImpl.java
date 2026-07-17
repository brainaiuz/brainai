package com.edatasite.workforce.gwt.assessment.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsEmployeeSkills;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsShiftItem;
import com.edatasite.workforce.core.domain.EdsSkill;
import com.edatasite.workforce.core.domain.EdsSkillGroup;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserContact;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.core.domain.assessment.EdsAppraisalApproval;
import com.edatasite.workforce.core.domain.assessment.EdsAppraisalRate;
import com.edatasite.workforce.core.domain.assessment.EdsAppraisalsSettings;
import com.edatasite.workforce.core.domain.assessment.EdsApprasialScoreType;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsAssessmentTemplate;
import com.edatasite.workforce.core.domain.assessment.EdsAssessmentTemplateSkill;
import com.edatasite.workforce.core.domain.assessment.EdsBonusDistribution;
import com.edatasite.workforce.core.domain.assessment.EdsBonusSettings;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeBonusItem;
import com.edatasite.workforce.core.domain.assessment.EdsGoalRating;
import com.edatasite.workforce.core.domain.assessment.EdsRatingComment;
import com.edatasite.workforce.core.domain.assessment.EdsScoreItem;
import com.edatasite.workforce.core.domain.assessment.EdsSkillRating;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.solr.component.EmployeeSolrComponent;
import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsScoreTypeItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentListData;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentSkills;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentsListElem;
import com.edatasite.workforce.gwt.assessment.client.rpc.BonusSettingsItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.BoolItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.DepartmentPeriodAppraisalItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.GoalSkillItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.InProgressAssessmentListElem;
import com.edatasite.workforce.gwt.assessment.client.rpc.InitiatedAssessmentItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.ScoreItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElemsStruct;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillGroupItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillList;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillRatingItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.TemplateItem;
import com.edatasite.workforce.gwt.assessment.client.rpc.TemplateListItem;
import com.edatasite.workforce.gwt.assessment.server.struct.ServerAssessmentHelper;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.department.DepartmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmTreeItemFactory;
import com.edatasite.workforce.gwt.core.server.db.AppraisalApprovalManager;
import com.edatasite.workforce.gwt.core.server.db.AssessmentManager;
import com.edatasite.workforce.gwt.core.server.db.AssessmentScheduleManager;
import com.edatasite.workforce.gwt.core.server.db.AssessmentTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.AssessmentTemplateSkillManager;
import com.edatasite.workforce.gwt.core.server.db.BonusDistributionManager;
import com.edatasite.workforce.gwt.core.server.db.BonusSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormLocalizationManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeAssessmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeSkillsManager;
import com.edatasite.workforce.gwt.core.server.db.GoalRatingManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.MonthManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.RatingCommentManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftItemManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftManager;
import com.edatasite.workforce.gwt.core.server.db.SkillGroupManager;
import com.edatasite.workforce.gwt.core.server.db.SkillManager;
import com.edatasite.workforce.gwt.core.server.db.SkillRatingManager;
import com.edatasite.workforce.gwt.core.server.db.UserContactManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.ValidityPeriodManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.AssessmentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.ComparatorFactory;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.hrms.client.rpc.BonusDistributionItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.EligibleEmployeeItem;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service("assessmentService")
public class AssessmentServiceImpl implements AssessmentService, AssessmentServiceLocal, Constants {
    private static final Logger log = LoggerFactory.getLogger(AssessmentServiceImpl.class);
    @Autowired
    private AssessmentManager assessmentManager;
    @Autowired
    private AssessmentTemplateManager assessmentTemplateManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private UserContactManager userContactManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmployeeAssessmentManager employeeAssessmentManager;
    @Autowired
    private AssessmentScheduleManager assessmentScheduleManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private MonthManager monthManager;
    @Autowired
    private SkillManager skillManager;
    @Autowired
    private GoalManager goalManager;
    @Autowired
    private SkillGroupManager skillGroupManager;
    @Autowired
    private ShiftItemManager shiftItemManager;
    @Autowired
    private ShiftManager shiftManager;
    @Autowired
    private EmployeeSkillsManager employeeSkillsManager;
    @Autowired
    private AssessmentTemplateSkillManager assessmentTemplateSkillManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SkillRatingManager skillRatingManager;
    @Autowired
    @Qualifier("assessmentViewPDFHandler1")
    private IPostPDFHandler assessmentViewPDFHandler;
    @Autowired
    private AssessmentCircularResolver assessmentCircularResolver;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private GoalRatingManager goalRatingManager;
    @Autowired
    private RatingCommentManager ratingCommentManager;
    @Autowired
    @Qualifier("hrmsLocalizer")
    private WfmMessageSource hrmsLocalizer;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private ValidityPeriodManager validityPeriodManager;
    @Autowired
    private BonusSettingsManager bonusSettingsManager;
    @Autowired
    private AppraisalApprovalManager appraisalApprovalManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private BonusDistributionManager bonusDistributionManager;
    @Autowired
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private NotificationMsgManager notificationMsgManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private CustomFormLocalizationManager customFormLocalizationManager;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;

    private final String DEFAULT_SKILL_CODE = "DEFAULT_SKILL_CODE";

    private static final Map<String, ComparatorFactory<EdsEmployeeBonusItem>> comparatorFactories = new HashMap<>();

    static {

        comparatorFactories.put(EligibleEmployeeItem.EB_EMPLOYEE_NAME,
                sortOrder -> new AbstractComparator<EdsEmployeeBonusItem>() {
                    public int compare(EdsEmployeeBonusItem o1, EdsEmployeeBonusItem o2) {
                        return internalCompare(o1.getEmployee().getFirstName(),
                                o2.getEmployee().getFirstName(), sortOrder);
                    }
                });
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AssessmentListData getAssessments() {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsAssessment.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get simple appraisal list");
        EdsUser user = employeeManager.getUser();
        List<EdsEmployeeAssessment> assessments;
        AssessmentListData listData = new AssessmentListData();

        boolean isTeamLeader = roleManager.hasRoles(user, EdsRole.TL);
        if (user.isEmployee()) {
//            if (isReviewer || hasReviewerSupervisor) {
            assessments = employeeAssessmentManager.getReviewerSimpleAssessments((EdsEmployee) user, true);
//            } else {
//                assessments = employeeAssessmentManager.getEmployeeSimpleAssessments((EdsEmployee) user, true);
//            }
        } else {
            assessments = employeeAssessmentManager.getClientSimpleAssessments((EdsClientContact) user);
        }
        ArrayList<InProgressAssessmentListElem> allAssessments = new ArrayList<>();
        int i = 0;
        List<Double> overallRates = new ArrayList<>();

        double overallAverage = 0;
        for (EdsEmployeeAssessment employeeAssessment : assessments) {
            EdsAssessment edsAssessment = employeeAssessment.getAssessment();

            Double overAllRate = employeeAssessment.getOverAllRate();
            if (overAllRate != null && overAllRate > 0) {
                overallRates.add(overAllRate);
                overallAverage += overAllRate;
            }
            InProgressAssessmentListElem item = new InProgressAssessmentListElem();
            if (employeeAssessment.getEmployee() != null) {
                item.setEmployeeId(employeeAssessment.getEmployee().getObjectID());
                item.setEmployeeName(employeeAssessment.getEmployee().getName());
                item.setEmployeeUsername(employeeAssessment.getEmployee().getUserName());
                if (employeeAssessment.getEmployee().getTeam() != null &&
                        employeeAssessment.getEmployee().getTeam().getLeader() != null) {
                    item.setManager(employeeAssessment.getEmployee().getTeam().getLeader().getName());
                }
            }
            item.setManagerPong(employeeAssessment.getManagerPong());
            item.setEmployeePong(employeeAssessment.getEmployeePing());
            item.setEmployeeAssessmentId(employeeAssessment.getObjectID());
            item.setReviewer(user.getObjectID().equals(employeeAssessment.getAssessment().getReviewer().getObjectID()));
            if (edsAssessment.getReviewer() != null) {
                item.setReviewerUsername(edsAssessment.getReviewer().getUserName());
            }
            item.setOverAllRate(employeeAssessment.getOverAllRate());
            try {
                item.setAverage(employeeAssessment.getAverage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (employeeAssessment.getStatus() != null) {
                item.setStatus(employeeAssessment.getStatus().getCode());
                item.setStatusName(employeeAssessment.getStatus().getName());
            }

            if (edsAssessment.getAssessmentType() != null) {
                item.setAssessmentType(referenceWfmMessageSource.localizeRef(edsAssessment.getAssessmentType()));
            }
            if (edsAssessment.getInitiator() != null) {
                item.setInitiatorName(edsAssessment.getInitiator().getName());
                item.setInitiatorID(edsAssessment.getInitiator().getObjectID());
            }
            if (employeeAssessment.getCollaborator() != null) {
                item.setCollaborator(employeeAssessment.getCollaborator().getName());
                item.setCollaboratorUsername(employeeAssessment.getCollaborator().getUserName());
            }
            if (edsAssessment.getReviewer() != null) {
                item.setReviewerName(edsAssessment.getReviewer().getName());
                item.setReviewerID(edsAssessment.getReviewer().getObjectID());
            }
            if (edsAssessment.getLastUpdater() != null) {
                item.setLastUpdaterID(edsAssessment.getLastUpdater().getObjectID());
                item.setLastUpdaterName(edsAssessment.getLastUpdater().getName());
            }
            if (employeeAssessment.getDate() != null) {
                item.setCompletedDate(new Date(employeeAssessment.getDate().getTime()));
            }
            if (edsAssessment.getInititateDate() != null) {
                item.setInitiationDate(new Date(edsAssessment.getInititateDate().getTime()));
            }
            item.setEncryptedID(EncryptionHelper.encryptURL("employeeAssessment/" + employeeAssessment.getObjectID()));
            item.setObjectID(employeeAssessment.getObjectID());
            allAssessments.add(item);
        }

        if (overallRates.size() > 0) {
            Double highest = Collections.max(overallRates);
            Double lovest = Collections.min(overallRates);

            if (highest != null) {
                listData.setHighestScore(highest);
            }
            if (lovest != null) {
                listData.setLowestScore(lovest);
            }
            listData.setAverageScore(overallAverage / overallRates.size());
        }

        listData.setTeamLeader(isTeamLeader);
        getItemsAssessment(allAssessments, listData, user);
        return listData;
    }

    private AssessmentListData getItemsAssessment(ArrayList<InProgressAssessmentListElem> allAssessments, AssessmentListData assessmentListData, EdsUser user) {
        ArrayList<InProgressAssessmentListElem> assessments = new ArrayList<>();
        ArrayList<InProgressAssessmentListElem> yourPendingReviewAssessmentsList = new ArrayList<>();
        for (InProgressAssessmentListElem item : allAssessments) {
            if (APPROVED.equals(item.getStatus()) || APPROVED_BY_MANAGER.equals(item.getStatus()) || APPROVED_BY_HR.equals(item.getStatus())) {
                assessments.add(item);
            } else if (SAVED_AS_DRAFT.equals(item.getStatus())) {
                if ((item.getLastUpdaterID() != null && item.getLastUpdaterID().equals(user.getObjectID()))) {
                    assessments.add(item);
                } else if ((item.getInitiatorID() != null && item.getInitiatorID().equals(user.getObjectID())) ||
                        (item.getReviewerID() != null && item.getReviewerID().equals(user.getObjectID())) ||
                        (item.getEmployeeId() != null && item.getEmployeeId().equals(user.getObjectID()))) {
                    yourPendingReviewAssessmentsList.add(item);
                }
            } else if (REVIEWED_BY_EMPLOYEE.equals(item.getStatus())) {

                if (item.getReviewerID() != null && item.getReviewerID().equals(user.getObjectID())) {
                    assessments.add(item);
                } else {
                    yourPendingReviewAssessmentsList.add(item);
                }
            } else if (REVIEWED_BY_MANAGER.equals(item.getStatus())) {
                if (item.getEmployeeId() != null && item.getEmployeeId().equals(user.getObjectID())) {
                    assessments.add(item);
                } else {
                    yourPendingReviewAssessmentsList.add(item);
                }
            } else if (INITIATED.equals(item.getStatus())) {
                if (item.getEmployeeId() != null && item.getEmployeeId().equals(user.getObjectID())) {
                    assessments.add(item);
                } else {
                    yourPendingReviewAssessmentsList.add(item);
                }
            }
        }
        assessmentListData.setAssessments(assessments.toArray(new InProgressAssessmentListElem[]{}));
        assessmentListData.setYourPendingReviewAssessments(yourPendingReviewAssessmentsList.toArray(new InProgressAssessmentListElem[]{}));

        return assessmentListData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getAssessmentTemplates(Integer employeeId) {
        EdsEmployee edsEmployee = employeeManager.get(employeeId);
        List<EdsAssessmentTemplate> result = null;
        if (edsEmployee != null && edsEmployee.getTeam() != null) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setDepartmentId(edsEmployee.getTeam().getObjectID());
            filterParameter.setUserID(userManager.getUser().getObjectID());
            result = assessmentTemplateManager.getAssessmentTemplates(filterParameter);
        } else {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setUserID(userManager.getUser().getObjectID());
            result = assessmentTemplateManager.getAssessmentTemplates(filterParameter);
        }
        SelectItem[] assessmentTemplates = new SelectItem[result.size()];
        int i = 0;
        for (EdsAssessmentTemplate assessmentTemplate : result) {
            assessmentTemplates[i] = new SelectItem(assessmentTemplate.getObjectID(), assessmentTemplate.getName());
            i++;
        }
        return assessmentTemplates;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getReviewers(Integer employeeID) {
        AppraisalsSettingsItem appraisalsSettingsItem = getAppraisalsSettings();

        List<String> roles = new ArrayList<>();

        List<String> accessedRoles = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.HRMS_APPRAISALS_REVIEWER);
        if (accessedRoles != null && accessedRoles.size() > 0) {
            roles.addAll(accessedRoles);
        } else {
            roles.add(EdsRole.TL_CODE);
        }

        List<EdsEmployee> appraisalReviewersByRole = employeeManager.getEmployeesByPermissionCode(PermissionConstants.HRMS_APPRAISALS_REVIEWER);

        int i = 0;
        SelectItem[] appraisalReviewersItems = new SelectItem[appraisalReviewersByRole.size()];
        for (EdsEmployee reviewer : appraisalReviewersByRole) {
            appraisalReviewersItems[i++] = new SelectItem(reviewer.getObjectID(), reviewer.getName());
        }

        Arrays.sort(appraisalReviewersItems, Comparator.comparing(SelectItem::getName));

        return appraisalReviewersItems;
    }

    @Transactional
    public InitiatedAssessmentItem initiateAssessment(InitiatedAssessmentItem assessmentItem) {
        EdsEmployee user = (EdsEmployee) employeeManager.getUser();
        EdsAssessment assessment = new EdsAssessment();
        if (assessmentItem.isWeightTable()) {
            assessment.setWeightable(assessmentItem.isWeightTable());
            if ((assessmentItem.getCompetencyPercentINT() + assessmentItem.getGoalPercentINT()) == 100) {
                assessment.setSkillsWeightPercent(assessmentItem.getCompetencyPercentINT());
                assessment.setGoalsWeightPercent(assessmentItem.getGoalPercentINT());
            }
        } else {
            assessment.setWeightable(false);
        }
        ByteArrayOutputStream outputStream;
        assessment.setAssessmentType(referenceManager.findReference(EdsAssessment._ASSESSMENT_TYPE, EdsAssessment.ASSESSMENT_SIMPLE));
        EdsEmployee employee;
        if (assessmentItem.getEmployeeId() == null) {
            employee = user;
        } else {
            employee = employeeManager.get(assessmentItem.getEmployeeId());
        }
        if (assessmentItem.getValidityPeriodId() != null) {
            EdsValidityPeriod validityPeriod = validityPeriodManager.get(assessmentItem.getValidityPeriodId());
            assessment.setValidityPeriod(validityPeriod);
        }

        EdsAssessmentTemplate template = null;
        if (assessmentItem.getTemplateID() != null) {
            template = assessmentTemplateManager.get(assessmentItem.getTemplateID());
        }

        assessment.setTemplate(template);
        if (assessmentItem.getReviewerId() != null) {
            EdsUser reviewer = userManager.get(assessmentItem.getReviewerId());
            assessment.setReviewer(reviewer);
        } else {
            assessment.setReviewer(user.getTeam() != null ? user.getTeam().getLeader() : null);
        }
        assessment.setInitiator(user);
        assessment.setInititateDate(assessmentItem.getDate());
        assessment.setAssessmentDay(assessmentItem.getAssessmentDate());
        assessment.setLastUpdater(user);
        assessment.setLastUpdateTime(assessmentItem.getDate());
        StringBuffer sb;
        String shortDateFormat = "MM/dd/yyyy";
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        if (assessmentItem.getTemplateID() == null || assessmentItem.getTemplateID() == 0) {
            sb = new StringBuffer(employee.getName() + " Custom" + ", " + ServerUtils.dateFormat(assessment.getInititateDate(), shortDateFormat));
        } else {
            sb = new StringBuffer(employee.getName() + ", " + assessment.getTemplate().getName() + ", " + ServerUtils.dateFormat(assessment.getInititateDate(), shortDateFormat));
        }
        assessment.setName(sb.toString());

        EdsEmployeeAssessment employeeAssessment = new EdsEmployeeAssessment();
        employeeAssessment.setEmployee(employee);
        if (assessmentItem.isSaveCompetencies()) {
            //creating new Skill"Template" for the entire employee the next time when the employee is selected the skill"Template" will be shown in the skill table;
            if (skillManager.getSkillListByEmployeeForSimple(employee.getObjectID()).size() > 0) {
                //first we must delete the previous skills from EdsEmployeeSkills
                employeeSkillsManager.deleteEmployeeSkills(employee.getObjectID(), EdsEmployeeSkills.Typer.SIMPLE);
            }
        }

        for (GoalSkillItem skillItem : assessmentItem.getSkillItems()) {
            EdsSkillRating skillRating = new EdsSkillRating();
            EdsSkill edsSkill = skillManager.get(skillItem.getObjectId());
            skillRating.setSkill(edsSkill);
            if (assessmentItem.isSaveCompetencies()) {
                EdsEmployeeSkills employeeSkills = new EdsEmployeeSkills();
                employeeSkills.setEmployee(employee);
                employeeSkills.setSkill(edsSkill);
                employeeSkills.setType(EdsEmployeeSkills.Typer.SIMPLE);
                employeeSkillsManager.create(employeeSkills);
            }
            if (assessmentItem.isWeightTable()) {
                skillRating.setWeight(skillItem.getWeight());
                skillRating.setRating(skillItem.getGivenScore());
            }
            skillRating.setShowSlider(skillItem.getShowSlider());
            employeeAssessment.getSkillAssessment().addSkillRating(skillRating);
        }

        for (GoalSkillItem goalItem : assessmentItem.getGoalItems()) {
            EdsGoalRating goalRating = new EdsGoalRating();
            goalRating.setGoal(goalManager.get(goalItem.getObjectId()));
            if (assessmentItem.isWeightTable()) {
                goalRating.setWeight(goalItem.getWeight());
                goalRating.setRating(goalItem.getGivenScore());
            }
            goalRating.setShowSlider(goalItem.getShowSlider());
            employeeAssessment.getGoalAssessment().addGoalRating(goalRating);
        }

        EdsReference reference;
        if (assessmentItem.isGoToReview()) {//It means goToReview is true and not reviewed yet.
            reference = referenceManager.findReference(Constants.ASSESSMENT_STATUS, Constants.SAVED_AS_DRAFT);
        } else {
            reference = referenceManager.findReference(Constants.ASSESSMENT_STATUS, assessmentItem.getStatus() != null ? assessmentItem.getStatus() : Constants.INITIATED);
        }
        employeeAssessment.setStatus(reference);
        employeeAssessment.setDate(assessmentItem.getDate());
        assessment.addEmployeeAssessment(employeeAssessment);
        assessmentManager.create(assessment);
        employeeAssessmentManager.create(employeeAssessment);
        if (assessmentItem.isFromShift()) {
            assess(employeeAssessment.getObjectID(), assessmentItem.getCompetencyElements(), null, APPROVED);
            EdsShiftItem shiftItem = shiftItemManager.get(assessmentItem.getShiftItemId());
            shiftItem.setEmployeeAssessment(employeeAssessment);
            shiftItemManager.update(shiftItem);
        }
        assessment.setKeyEmployeeAssessment(employeeAssessment);

        //Sending Email if approved by manager
        try {
            EdsReference approvedStatus = referenceManager.findReference(Constants.ASSESSMENT_STATUS, Constants.APPROVED_BY_MANAGER);
            if (approvedStatus != null && approvedStatus.equals(employeeAssessment.getStatus())) {
                outputStream = assessmentViewPDFHandler.getPDFStream(new RequestObject(employeeAssessment.getObjectID(), user.getObjectID()));
                messageManager.sendAssessmentInitiateNotification(employeeAssessment, outputStream, assessmentItem.isSendEmailToEmployee());
            } else if (Constants.INITIATED.equalsIgnoreCase(employeeAssessment.getStatus().getCode())) {

                messageManager.sendAssessmentInitiateNotification(employeeAssessment, null, assessmentItem.isSendEmailToEmployee());
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
        assessmentItem.setId(employeeAssessment.getObjectID());
        assessmentItem.setEncryptedId(EncryptionHelper.encryptURL("employeeAssessment/" + employeeAssessment.getObjectID()));

        //Send email
        try {
            sendAssessmentResultToEmployee(employeeAssessment.getObjectID(), user.getObjectID());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsAssessment.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        kpiLog.setEntityId(assessment.getObjectID());
        ServerUtils.kpiLog(log, kpiLog, "Add appraisal");
        return assessmentItem;
    }

    /**
     * Getting the skills and comments for ping-pong in Simple assessment and  collaborators and employees in 360 deg.ass.
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SkillAssessmentElemsStruct getSkillAssessmentElemGroups(Integer employeeAssessmentId) {
        EdsUser currentUser = userManager.getUser();

        AppraisalsSettingsItem appraisalsSettings = getAppraisalsSettings();
        boolean hasReviewerSupervisor = appraisalsSettings.getReviewers().contains(EdsRole.SUPERVISOR_CODE);

        SkillAssessmentElemsStruct struct = assessmentCircularResolver.getSkillAssessmentElemGroups(employeeAssessmentId, currentUser.getObjectID(), hasReviewerSupervisor);
        if (struct.getValidityPeriodId() != null) {
            EdsValidityPeriod validityPeriod = validityPeriodManager.get(struct.getValidityPeriodId());
            EdsBonusSettings edsBonusSettings = bonusSettingsManager.getBonusSettingsByDate(validityPeriod.getFromDate(), validityPeriod.getToDate());
            if (edsBonusSettings != null) {
                BonusSettingsItem settingsItem = edsBonusSettings.getDTO();
                struct.setBonusSettingsItem(settingsItem);
            }
        }
        return struct;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SkillAssessmentElemsStruct getGoalAssessmentElemGroups(Integer employeeAssessmentId) {
        EdsUser currentUser = userManager.getUser();

        AppraisalsSettingsItem appraisalsSettings = getAppraisalsSettings();
        boolean hasReviewerSupervisor = appraisalsSettings.getReviewers().contains(EdsRole.SUPERVISOR_CODE);

        SkillAssessmentElemsStruct struct = assessmentCircularResolver.getGoalAssessmentElemGroups(employeeAssessmentId, currentUser.getObjectID(), hasReviewerSupervisor);
        if (struct.getValidityPeriodId() != null) {
            EdsValidityPeriod validityPeriod = validityPeriodManager.get(struct.getValidityPeriodId());
            EdsBonusSettings edsBonusSettings = bonusSettingsManager.getBonusSettingsByDate(validityPeriod.getFromDate(), validityPeriod.getToDate());
            if (edsBonusSettings != null) {
                BonusSettingsItem settingsItem = edsBonusSettings.getDTO();
                struct.setBonusSettingsItem(settingsItem);
            }
        }
        return struct;
    }

    /**
     * Assessing the employee both assessments
     *
     * @param employeeAssessmentId - employeeAssessmentId
     * @param skillElems           -   skillElems
     * @param goalElems            - goalElems
     * @param status               - status
     */
    @Transactional
    public void assess(Integer employeeAssessmentId, SkillAssessmentElem[] skillElems, SkillAssessmentElem[] goalElems, String status) {
        EdsEmployeeAssessment employeeAssessment = employeeAssessmentManager.get(employeeAssessmentId);
        EdsAssessment assessment = employeeAssessment.getAssessment();
        int competencyPercent = assessment.getSkillsWeightPercent();
        int goalPercent = assessment.getGoalsWeightPercent();
        String generalComment = assessment.getName();
        assess2(employeeAssessmentId, skillElems, goalElems, status, generalComment, competencyPercent, goalPercent);
    }

    /**
     * Assessing the employee both assessments
     *
     * @param employeeAssessmentId - employeeAssessmentId
     * @param skillElems           -   skillElems
     * @param goalElems            - goalElems
     * @param status               - status
     * @param competencyPercent    - competencyPercent
     * @param goalPercent          - goalPercent
     */
    @Transactional
    public void assess2(Integer employeeAssessmentId, SkillAssessmentElem[] skillElems, SkillAssessmentElem[] goalElems, String status, String generalComment, int competencyPercent, int goalPercent) {
        EdsEmployeeAssessment employeeAssessment = employeeAssessmentManager.get(employeeAssessmentId);
        boolean statusChanged = true;
        if (employeeAssessment.getStatus().getName().equals(status)) {
            statusChanged = false;
        }
        if (statusChanged) {
            if (status != null) {
                EdsReference assStatus = referenceManager.findReference(Constants.ASSESSMENT_STATUS, status);
                if (Constants.REVIEWED_BY_MANAGER.equals(status) || Constants.APPROVED_BY_MANAGER.equals(status) || Constants.APPROVED.equals(status)) {
                    employeeAssessment.setManagerPong(employeeAssessment.getManagerPong() + 1);
                } else if (status.equals(Constants.REVIEWED_BY_EMPLOYEE)) {
                    employeeAssessment.setEmployeePing(employeeAssessment.getEmployeePing() + 1);
                }

                if (Constants.APPROVED.equals(status)) {
                    employeeAssessment.setDate(new Date());
                }

                employeeAssessment.setStatus(assStatus);
            }
        }

        //Handling skill ratings
        for (EdsSkillRating rating : employeeAssessment.getSkillAssessment().getRatings()) {
            for (SkillAssessmentElem elem : skillElems) {
                if ((rating.getSkill() != null && rating.getSkill().getObjectID().equals(elem.getSkillId())) || rating.getObjectID().equals(elem.getSkillRatingId())) {
                    boolean savedAsDraftStatus = SAVED_AS_DRAFT.equals(status);
                    if (savedAsDraftStatus) {
                        if (elem.getLastRatingComment() != null) {
                            if (elem.isTurn() == null || elem.isTurn() == MANAGER_TURN) {
                                rating.setSavedAsDraftComment(elem.getLastRatingComment().getReviewerComment());
                            } else {
                                rating.setSavedAsDraftComment(elem.getLastRatingComment().getEmployeeComment());
                            }
                        }
                    } else {
                        rating.setSavedAsDraftComment(null);
                        if (elem.getLastRatingComment() != null) {
                            EdsRatingComment ratingComment = ratingCommentManager.getCreateAndGetRatingComment(elem.getLastRatingComment());
                            ratingComment.setSkillRating(rating);
                        }
                    }

                    if (elem.isTurn() == null || elem.isTurn() == MANAGER_TURN) {
                        if (elem.getManagersGrade() != null) {
                            rating.setManagerGrade(elem.getManagersGrade());
                        }
                        if (elem.isShowRadio() != null && !elem.isShowRadio()) {
                            rating.setShowSlider(false);
                        } else {
                            rating.setShowSlider(true);
                        }

                    } else if (elem.isTurn() == EMPLOYEE_TURN) {
                        if (elem.getEmployeeGrade() != null) {
                            rating.setEmployeeGrade(elem.getEmployeeGrade());
                        }
                    }
                    if (elem.getWeight() != null) {
                        rating.setWeight(elem.getWeight());
                    }
                    break;
                }
            }
        }

        //Handling goal ratings
        if (goalElems != null && employeeAssessment.getGoalAssessment() != null) {
            for (EdsGoalRating rating : employeeAssessment.getGoalAssessment().getRatings()) {
                for (SkillAssessmentElem elem : goalElems) {
                    if (elem.getSkillRatingId().equals(rating.getObjectID())) {
                        boolean savedAsDraftStatus = SAVED_AS_DRAFT.equals(status);
                        if (savedAsDraftStatus) {
                            if (elem.getLastRatingComment() != null) {
                                if (elem.isTurn() == null || elem.isTurn() == MANAGER_TURN) {
                                    rating.setSavedAsDraftComment(elem.getLastRatingComment().getReviewerComment());
                                } else {
                                    rating.setSavedAsDraftComment(elem.getLastRatingComment().getEmployeeComment());
                                }
                            }
                        } else {
                            rating.setSavedAsDraftComment(null);
                            if (elem.getLastRatingComment() != null) {
                                EdsRatingComment ratingComment = ratingCommentManager.getCreateAndGetRatingComment(elem.getLastRatingComment());
                                ratingComment.setGoalRating(rating);
                            }
                        }

                        if (elem.isTurn() == null || elem.isTurn() == MANAGER_TURN) {
                            if (elem.getManagersGrade() != null) {
                                rating.setManagerGrade(elem.getManagersGrade());
                            }
                            if (elem.isShowRadio() != null && !elem.isShowRadio()) {
                                rating.setShowSlider(false);
                            } else {
                                rating.setShowSlider(true);
                            }

                        } else if (elem.isTurn() == EMPLOYEE_TURN) {
                            if (elem.getEmployeeGrade() != null) {
                                rating.setEmployeeGrade(elem.getEmployeeGrade());
                            }
                        }
                        if (elem.getWeight() != null) {
                            rating.setWeight(elem.getWeight());
                        }
                        break;
                    }
                }
            }
        }
        //Setting overall rate, only for APPROVED status
        EdsUser currentUser = userManager.getUser();
        EdsAssessment assessment = employeeAssessment.getAssessment();
        assessment.setOverallRate(employeeAssessment.getOverAllRate());
        assessment.setLastUpdater(currentUser);
        assessment.setLastUpdateTime(new Date());
        assessment.setGeneralComment(generalComment);

        boolean isWeighTable = assessment.isWeightable() != null ? assessment.isWeightable() : false;
        if (isWeighTable) {
            assessment.setWeightable(isWeighTable);
            if ((competencyPercent + goalPercent) == 100) {
                assessment.setSkillsWeightPercent(competencyPercent);
                assessment.setGoalsWeightPercent(goalPercent);
            }
        } else {
            assessment.setWeightable(false);
        }

        if (statusChanged) {
            EdsUser user = employeeAssessment.getCollaborator();
            if (!(user instanceof EdsUserContact)) {
                if (status != null) {
                    baseEventPostProcessor.registerEvent(AssessmentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, employeeAssessment, currentUser);
                }
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsAssessment.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(assessment.getObjectID());
        if (status != null && !"".equals(status)) {
            kpiLog.setEntityType(status);
        }
        ServerUtils.kpiLog(log, kpiLog, "Update appraisal");
    }

    /**
     * this method return overall average of collaborators and initiator rates for Assessment    (only after approved)
     *
     * @param emplAssessment
     */
    private Double getOverallAverageRate(EdsEmployeeAssessment emplAssessment) {
        double overAll = 0d;
        int counter = 0;
        //Collaborators
        for (EdsEmployeeAssessment employeeAssessment : emplAssessment.getAssessment().getEmployeeAssessments()) {
            if (employeeAssessment != null) {

                for (EdsSkillRating skillRating : employeeAssessment.getSkillAssessment().getRatings()) {
                    if (skillRating.getRating() != null && skillRating.getRating() > 0) {
                        overAll += skillRating.getRating();
                        counter++;
                    }
                }

                if (employeeAssessment.getGoalAssessment() != null) {
                    for (EdsGoalRating goalRating : employeeAssessment.getGoalAssessment().getRatings()) {
                        if (goalRating.getRating() != null && goalRating.getRating() > 0) {
                            overAll += goalRating.getRating();
                            counter++;
                        }
                    }
                }
            }
        }

        if (counter > 0) {
            overAll = overAll / counter;
        }

        return overAll;
    }

    /**
     * this methos retrieves average of employee self rates for each skill, available after RATED status
     *
     * @param edsEmployeeAssessment
     */
    private Double getEmployeeSelfRatesAverage(EdsEmployeeAssessment edsEmployeeAssessment) {
        double average = 0d;
        int count = 0;
        if (edsEmployeeAssessment.getSkillAssessment() != null && edsEmployeeAssessment.getSkillAssessment().getRatings() != null) {
            for (EdsSkillRating skillRating : edsEmployeeAssessment.getSkillAssessment().getRatings()) {
                if (skillRating.getEmployeeRating() != null) {
                    average += skillRating.getEmployeeRating();
                }
                count++;
            }
        }
        if (count > 0) {
            average = average / count;
        }
        return average;
    }

    /**
     * Retrieves the result of data for Reviewer and Employee in 360 degree assessment
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AssessmentSkills getAssessmentSkillsComments(Integer assessmentId) {
        return assessmentCircularResolver.getAssessmentSkillsComments(assessmentId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AssessmentSkills getAssessmentGoalsComments(Integer assessmentId) {
        return assessmentCircularResolver.getAssessmentGoalsComments(assessmentId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public LinkedList<WfmTreeItem> getGroups() {
        return ListUtils.createTreeItemArray(skillManager.skillGroupList(),
                new WfmTreeItemFactory<EdsSkillGroup>() {
                    public WfmTreeItem createItem(EdsSkillGroup o) {
                        WfmTreeItem result = new WfmTreeItem(o.getObjectID(), hrmsLocalizer.localize(o.getCode(), o.getName()));
                        result.setChildren(true);
                        return result;

                    }
                });
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedList<WfmTreeItem> getSkills(Integer groupId) {
        List<EdsSkill> skills = skillManager.skillListBySkillGroupId(groupId);
        if (!skills.isEmpty()) {
            return ListUtils.createTreeItemArray(skills,
                    new WfmTreeItemFactory<EdsSkill>() {
                        public WfmTreeItem createItem(EdsSkill o) {
                            String skillGroupName = hrmsLocalizer.localize(o.getGroup().getCode(), o.getGroup().getName());
                            String skillName = hrmsLocalizer.localize(o.getCode(), o.getName());
                            String skillDescription = hrmsLocalizer.localize(o.getDescriptionCode(), o.getDescription());

                            return new WfmTreeItem(new WfmTreeItem(o.getGroup().getObjectID(), skillGroupName),
                                    o.getObjectID(), skillName, skillDescription, o.getDefaultWeight());
                        }
                    });
        } else {
            return new LinkedList<>();
        }

    }

    @Transactional
    public void saveTemplate(Integer id, String name, BoolItem[] skills,ArrayList<SelectItem> departmnets,SelectItem owner) {
        EdsAssessmentTemplate template = null;
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsAssessmentTemplate.class.getSimpleName());
        if (id == null) {//Save
            template = new EdsAssessmentTemplate();
            template.setName(name);
            if (owner != null) {
                template.setOwner(employeeManager.get(owner.getId()));
            }
            for (BoolItem item : skills) {
                EdsAssessmentTemplateSkill templateSkill = new EdsAssessmentTemplateSkill();
                templateSkill.setSkill(skillManager.get(item.getId()));
                templateSkill.setShowSlider(item.isChecked());
                templateSkill.setWeight(item.getWeight());
                template.addAssessmentTemplateSkill(templateSkill);
            }
            final HashSet<EdsDepartment> departmentSet = new HashSet<>();
            departmnets.forEach(department -> departmentSet.add(this.departmentManager.get(department.getId())));
            template.setDepartments(departmentSet);

            assessmentTemplateManager.create(template);
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(template.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Added new template");
        } else {//Update
            template = assessmentTemplateManager.get(id);
            List<EdsAssessmentTemplateSkill> templateSkillsList = new ArrayList<>();
            for (BoolItem item : skills) {
                List<EdsAssessmentTemplateSkill> templateSkills = assessmentTemplateSkillManager.getTemplateSkill(template, skillManager.get(item.getId()));
                if (templateSkills.size() == 0) {
                    EdsAssessmentTemplateSkill newTemplateSkill = new EdsAssessmentTemplateSkill();
                    newTemplateSkill.setSkill(skillManager.get(item.getId()));
                    newTemplateSkill.setShowSlider(item.isChecked());
                    newTemplateSkill.setWeight(item.getWeight());
                    newTemplateSkill.setAssessmentTemplate(template);
                    templateSkillsList.add(newTemplateSkill);
                } else {
                    EdsAssessmentTemplateSkill templateSkill = templateSkills.get(0);
                    templateSkill.setShowSlider(item.isChecked());
                    templateSkill.setWeight(item.getWeight());
                    templateSkillsList.add(templateSkill);

                }

            }
            final HashSet<EdsDepartment> departmentSet = new HashSet<>();
            departmnets.forEach(department -> departmentSet.add(this.departmentManager.get(department.getId())));
            template.setDepartments(departmentSet);

            template.setAssessmentTemplateSkills(templateSkillsList);
            template.setName(name);
            if (owner != null) {
                template.setOwner(employeeManager.get(owner.getId()));
            }
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(template.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "template updated");
        }
    }

    public List<EdsSkill> getCompetencyList(Integer employeeId, int type) {
        if (employeeId == null) {
            employeeId = referenceManager.getUser().getEmployee().getObjectID();
        }
        List<EdsSkill> skills = null;
        if (type == ASSESSMENT_SKILLS_SIMPLE) {
            skills = skillManager.getSkillListByEmployeeForSimple(employeeId);
        } else if (type == ASSESSMENT_SKILLS_360) {
            skills = skillManager.getSkillListByEmployeeFor360(employeeId);
        }
        return skills;

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TemplateItem getCompetencyListAsTableItem(Integer employeeId, int type) {
        if (employeeId == null) {
            employeeId = referenceManager.getUser().getEmployee().getObjectID();
        }
        List<EdsSkill> list = getCompetencyList(employeeId, type);
        if (list.size() == 0) {
            LinkedList<WfmTreeItem> emptyItem = new LinkedList<>();
            emptyItem.add(new WfmTreeItem(0, "No competencies in this template"));
            new TemplateItem("Empty", emptyItem,null,null);
        }
        return new TemplateItem("Custom", ListUtils.createTreeItemArray(list, new WfmTreeItemFactory<EdsSkill>() {
            public WfmTreeItem createItem(EdsSkill o) {
                String skillGroupName = hrmsLocalizer.localize(o.getGroup().getCode(), o.getGroup().getName());
                String skillName = hrmsLocalizer.localize(o.getCode(), o.getName());
                String skillDescription = hrmsLocalizer.localize(o.getDescriptionCode(), o.getDescription());

                return new WfmTreeItem(new WfmTreeItem(o.getGroup().getObjectID(), skillGroupName),
                        o.getObjectID(), skillName, true, skillDescription);
            }
        }),null,null);
    }

    @Transactional
    /*(propagation = Propagation.SUPPORTS, readOnly = true)*/
    public TemplateItem getTemplate(Integer id) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsAssessmentTemplate.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(id);
        ServerUtils.kpiLog(log, kpiLog, "View template");
        if (id == null || id == 0) {
            return null;
        }

        EdsAssessmentTemplate template = assessmentTemplateManager.get(id);
        List<EdsAssessmentTemplateSkill> list = template.getAssessmentTemplateSkills();
        if (list.size() == 0) {
            EdsSkill skill = new EdsSkill();
            skill.setName(hrmsLocalizer.localize(DEFAULT_SKILL_CODE, "Default competency"));
            skill.setCode(DEFAULT_SKILL_CODE);
            skill.setDescription("");
            EdsSkillGroup generalGroup = skillGroupManager.get(2);
            if (generalGroup != null) {
                skill.setGroup(generalGroup);
            }
            skillManager.create(skill);
            EdsAssessmentTemplateSkill templateSkill = new EdsAssessmentTemplateSkill();
            templateSkill.setSkill(skill);
            template.addAssessmentTemplateSkill(templateSkill);

            list = template.getAssessmentTemplateSkills();
        }
        return new TemplateItem(template.getName(), ListUtils.createTreeItemArray(list, new WfmTreeItemFactory<EdsAssessmentTemplateSkill>() {
            public WfmTreeItem createItem(EdsAssessmentTemplateSkill o) {
                if (o.getSkill() != null) {
                    String skillGroupName = hrmsLocalizer.localize(o.getSkill().getGroup().getCode(), o.getSkill().getGroup().getName());
                    String skillName = hrmsLocalizer.localize(o.getSkill().getCode(), o.getSkill().getName());
                    String skillDescription = hrmsLocalizer.localize(o.getSkill().getDescriptionCode(), o.getSkill().getDescription());

                    return new WfmTreeItem(new WfmTreeItem(o.getSkill().getGroup().getObjectID(), skillGroupName),
                            o.getSkill().getObjectID(), skillName, o.getShowSlider(), skillDescription, o.getWeight());
                }
                return null;
            }
        }), template.getDepartments() != null ? template.getDepartments() != null
                ? template.getDepartments().stream()
                .map(dept -> new SelectItem(dept.getObjectID(), dept.getName()))
                .collect(Collectors.toCollection(ArrayList::new))
                : null : null,template.getOwner() != null ? template.getOwner().getAsSelectItem() : null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<AssessmentsListElem> getAssessmentsList(ListingFilterParameter filterParametrs,Integer employeeId) {
        EdsUser user = employeeManager.getUser();
        if (filterParametrs.getEmployeeId() == null) {
            filterParametrs.setEmployeeId(employeeId);
        }
        List<EdsAssessment> assessments = assessmentManager.getAssessmentsByEmployee(user, filterParametrs);
        return createAssessmentsList(filterParametrs, assessments, user);
    }

    private ListResult<AssessmentsListElem> createAssessmentsList(ListingFilterParameter filterParametrs, List<EdsAssessment> assessments, EdsUser user) {
        Long totalCount = assessmentManager.getAssessmentsByEmployeeTotal(user, filterParametrs);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsAssessment.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get appraisal archive list");
        ArrayList<AssessmentsListElem> list = new ArrayList<>();
        for (EdsAssessment assessment : assessments) {
            list.add(createAssessmentListElem(assessment));
        }
        return new ListResult<>(list, totalCount.intValue());
    }

    private AssessmentsListElem createAssessmentListElem(EdsAssessment assessment) {
        EdsUser user = employeeManager.getUser();
        AssessmentsListElem item = new AssessmentsListElem();
        item.setAssessmentId(assessment.getObjectID());
        item.setAssessmentName(assessment.getName());
        if (assessment.getAssessmentType() != null) {
            item.setAssessmentType(referenceWfmMessageSource.localize(assessment.getAssessmentType().getCode(), "Simple appraisal"));
            item.setAssessmentTypeCode(assessment.getAssessmentType().getCode());
        }
        item.setInitiationDate(new Date(assessment.getInititateDate().getTime()));
        item.setInitiatorName(assessment.getInitiator().getName());
        item.setInitiatorID(assessment.getInitiator().getObjectID());
        item.setReviewerName(assessment.getReviewer().getName());
        if (assessment.getTemplate() != null) {
            item.setTemplateName(assessment.getTemplate().getName());
        }
        EdsEmployeeAssessment keyEmployeeAssessment = assessment.getKeyEmployeeAssessment();
        EdsEmployeeAssessment lastEmployeeAssessment = null;
        for (EdsEmployeeAssessment ea : assessment.getEmployeeAssessments()) {
            if (ea.getCollaborator() != null) {
                if (user.getObjectID().equals(ea.getCollaborator().getObjectID())) {//the collaborator will see the same page employee sees
                    item.setStatus(referenceWfmMessageSource.localize(ea.getStatus().getCode(), ea.getStatus().getName()));
                    item.setStatusCode(ea.getStatus().getCode());
                    item.setEmployeeAssessmentId(ea.getObjectID());
                    item.setCollaborator(true);
                }
            } else {
                if (user.getObjectID().equals(ea.getEmployee().getObjectID())) {
                    item.setStatus(referenceWfmMessageSource.localizeRef(ea.getStatus()));
                    item.setStatusCode(ea.getStatus().getCode());
                    item.setEmployeeAssessmentId(ea.getObjectID());
                    item.setCollaborator(false);
                } else if (user.getObjectID().equals(assessment.getReviewer().getObjectID())) {
                    item.setStatus(referenceWfmMessageSource.localizeRef(ea.getStatus()));
                    item.setStatusCode(ea.getStatus().getCode());
                    item.setEmployeeAssessmentId(ea.getObjectID());
                    item.setCollaborator(false);
                }
            }
            lastEmployeeAssessment = ea;
            EdsEmployee eaEmployee = ea.getEmployee();
            if (eaEmployee != null) {
                item.setEmployeeID(eaEmployee.getObjectID());
                item.setEmployeeName(eaEmployee.getName());
                if (eaEmployee.getEmployeeDepartment() != null) {
                    item.setDepartmentName(eaEmployee.getEmployeeDepartment().getTeam().getName());
                }
                if (eaEmployee.getTeam() != null) {
                    item.setDepartmentName(eaEmployee.getTeam().getName());
                }
            }
        }
        if (item.getEmployeeAssessmentId() == null) {
            if (keyEmployeeAssessment != null) {
                item.setStatus(referenceWfmMessageSource.localizeRef(keyEmployeeAssessment.getStatus()));
                item.setStatusCode(keyEmployeeAssessment.getStatus().getCode());
                item.setEmployeeAssessmentId(keyEmployeeAssessment.getObjectID());
                EdsEmployee keyEmployee = keyEmployeeAssessment.getEmployee();
                if (keyEmployee != null) {
                    item.setEmployeeID(keyEmployee.getObjectID());
                    item.setEmployeeName(keyEmployee.getName());
                    if (keyEmployee.getEmployeeDepartment() != null) {
                        item.setDepartmentName(keyEmployee.getEmployeeDepartment().getTeam().getName());
                    }
                }
            } else if (lastEmployeeAssessment != null) {
                item.setStatus(referenceWfmMessageSource.localizeRef(lastEmployeeAssessment.getStatus()));
                item.setStatusCode(lastEmployeeAssessment.getStatus().getCode());
                item.setEmployeeAssessmentId(lastEmployeeAssessment.getObjectID());
                EdsEmployee lastEmployee = lastEmployeeAssessment.getEmployee();
                if (lastEmployee != null) {
                    item.setEmployeeID(lastEmployee.getObjectID());
                    item.setEmployeeName(lastEmployee.getName());
                    if (lastEmployee.getEmployeeDepartment() != null) {
                        item.setDepartmentName(lastEmployee.getEmployeeDepartment().getTeam().getName());
                    }
                }
            }
        }
        EdsValidityPeriod validityPeriod = assessment.getValidityPeriod();
        if (validityPeriod != null) {
            item.setValidityPeriod(validityPeriod.getAsSelectItem(user).getName());
            EdsBonusSettings edsBonusSettings = bonusSettingsManager.getBonusSettingsByDate(validityPeriod.getFromDate(), validityPeriod.getToDate());
            if (edsBonusSettings != null) {
                BonusSettingsItem settingsItem = edsBonusSettings.getDTO();
                item.setBonusSettingsItem(settingsItem);
            }

        }
        if (assessment.getKeyEmployeeAssessment().getSkillAssessment() != null) {
            List<EdsSkillRating> ratings = assessment.getKeyEmployeeAssessment().getSkillAssessment().getRatings();
            double avgManagerRate = (ratings != null && !ratings.isEmpty())
                    ? ratings.stream()
                    .mapToInt(r -> {
                        try { return Integer.parseInt(r.getManagerGrade()); } catch (Exception e) { return 0; }
                    })
                    .average().orElse(0.0)
                    : 0.0;
            item.setOverallScore(avgManagerRate);
        } else if (assessment.getOverallRate() != null) {
            item.setOverallScore(assessment.getOverallRate());
        } else {
            item.setOverallScore(0d);
        }
        //set rejection comment
        if (REJECTED.equals(item.getStatusCode())) {
            String rejectionReasonComment = appraisalApprovalManager.getRejectionReasonComment(item.getAssessmentId());
            item.setRejectionReasonComment(rejectionReasonComment);
        }
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<TemplateListItem> getTemplates(ListingFilterParameter filterParametrs) {
        List<EdsAssessmentTemplate> templates = assessmentTemplateManager.getAssessmentTemplates(filterParametrs);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsAssessmentTemplate.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get template list");
        return createTemplatesList(filterParametrs, templates);

    }

    private ListResult<TemplateListItem> createTemplatesList(ListingFilterParameter filterParametrs, List<EdsAssessmentTemplate> templates) {
        Long totalCount = assessmentTemplateManager.getTemplatesTotal(filterParametrs);
        ArrayList<TemplateListItem> list = new ArrayList<>();
        for (EdsAssessmentTemplate template : templates) {
            if (template != null) {
                list.add(createTemplateListItem(template));
            }
        }
        return new ListResult<>(list, totalCount.intValue());
    }

    private TemplateListItem createTemplateListItem(EdsAssessmentTemplate template) {
        TemplateListItem item = new TemplateListItem();
        item.setName(template.getName() != null ? template.getName() : "N/A");
        item.setId(template.getObjectID());
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean managersFirstAppraisal() {
        EdsUser user = employeeManager.getUser();
        int count = assessmentManager.getManagersAssessmentsCount(user);
        ListingFilterParameter fpE = new ListingFilterParameter();
        boolean adminOrDrOrHr = roleManager.hasRoles(user, EdsRole.ADMIN) || roleManager.hasRoles(user, EdsRole.DR) || roleManager.hasRoles(user, EdsRole.HR);
        fpE.setAllEmployees(true);
        if (!adminOrDrOrHr) {
            fpE.setDepartmentId(((EdsEmployee) user).getTeam().getObjectID());
        }
        ListResult<EmployeeListItem> employees = employeeServiceLocal.getEmployeeList(fpE);
        //if employees count more than 1 then i should show employees list
        return count == 0 && (employees.getList() != null ? employees.getList().size() : 0) < 2;
    }

    @Transactional
    public void removeAssessmentSkillRating(Integer assessmentId, Integer skillId, boolean isCompetencyContainer) {
        EdsEmployeeAssessment employeeAssessment = employeeAssessmentManager.get(assessmentId);
        if (isCompetencyContainer) {
            EdsSkillRating skillRating = skillRatingManager.get(skillId);
            List<EdsSkillRating> skillRatings = employeeAssessment.getSkillAssessment().getRatings();
            if (skillRating != null && skillRatings != null && skillRatings.size() > 0) {
                skillRatings.remove(skillRating);
            }
        } else {
            EdsGoalRating goalRating = goalRatingManager.get(skillId);
            List<EdsGoalRating> goalRatings = employeeAssessment.getGoalAssessment().getRatings();
            if (goalRating != null && goalRatings != null && goalRatings.size() > 0) {
                goalRatings.remove(goalRating);
            }
        }

    }

    @Transactional
    public SkillRatingItem[] addAssessmentSkills(Integer employeeAssessmentId, BoolItem[] skills) {
        EdsEmployeeAssessment employeeassessment = employeeAssessmentManager.get(employeeAssessmentId);
        SkillRatingItem[] skillRatings = new SkillRatingItem[skills.length];
        int i = 0;
        for (BoolItem skill : skills) {
            EdsSkillRating skillRating = new EdsSkillRating();
            EdsSkill edsSkill = skillManager.get(skill.getId());
            skillRating.setSkill(edsSkill);
            skillRating.setShowSlider(skill.isChecked());
            if (edsSkill.getDefaultWeight() != null) {
                skillRating.setWeight(edsSkill.getDefaultWeight());
            }
            skillRatingManager.create(skillRating);
            if (employeeassessment != null) {
                employeeassessment.getSkillAssessment().addSkillRating(skillRating);
            }
            skillRatings[i] = new SkillRatingItem();
            skillRatings[i].setSkillRatingId(skillRating.getObjectID());
            skillRatings[i].setRateable(skill.isChecked());
            String skillName = hrmsLocalizer.localize(skillRating.getSkill().getCode(), skillRating.getSkill().getName());
            String skillDescription = hrmsLocalizer.localize(skillRating.getSkill().getDescriptionCode(), skillRating.getSkill().getDescription());
            skillRatings[i].setSkillName(skillName);
            skillRatings[i].setSkillDescription(skillDescription);
            skillRatings[i].setSkillId(skill.getId());
            skillRatings[i].setSkillWeight(skillRating.getWeight());
            i++;
        }
        return skillRatings;
    }

    @Transactional
    public void sendReminderMessage(Integer keyEmployeeAssessmentId, String messageContent) {
        EdsEmployeeAssessment employeeAssessment = employeeAssessmentManager.get(keyEmployeeAssessmentId);
        try {
            messageManager.send360ReviewReminederNotification(employeeAssessment, messageContent);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getSkillGroupAsSelectItem() {
        List<EdsSkillGroup> groups = skillManager.skillGroupList();
        SelectItem[] list = new SelectItem[groups.size()];
        int i = 0;
        for (EdsSkillGroup group : groups) {
            String localizedName = ServerUtils.isNullOrEmpty(group.getName()) ? hrmsLocalizer.localize(group.getCode(), group.getName()) : group.getName();
            list[i++] = new SelectItem(group.getObjectID(), localizedName);
        }
        return list;
    }

    @Override
    @Transactional
    public SkillItem getSkill(Integer objectId) {
        SkillItem result = new SkillItem();
        if (objectId != null) {
            EdsSkill edsSkill = skillManager.get(objectId);
            if (edsSkill != null) {
                result.setId(edsSkill.getObjectID());
                result.setName(edsSkill.getRealName());
                result.setDescription(edsSkill.getRealDescription());
                result.setWeight(edsSkill.getDefaultWeight());
                if (edsSkill.getNameLocalize() != null) {
                    result.setSkillNameLocalization(edsSkill.getNameLocalize().getRPC());
                }
                if (edsSkill.getDiscriptionLocalize() != null) {
                    result.setSkillDescriptionLoc(edsSkill.getDiscriptionLocalize().getRPC());
                }
                if (edsSkill.getGroup() != null) {
                    result.setGroupId(edsSkill.getGroup().getObjectID());
                }
            }
        }
        result.setSkillGroups(getSkillGroupAsSelectItem());
        return result;
    }

    @Transactional
    public LinkedList<WfmTreeItem>
    addSkill(SkillList skillList) {
        try {
            EdsSkillGroup group = skillGroupManager.get(skillList.getSkilGroupId());
            ArrayList<EdsSkill> skills = new ArrayList<>();
            Integer employeeID = skillList.getEmployeeID();

            for (SkillItem skillItem : skillList.getSkillItems()) {
                EdsSkill skill = new EdsSkill();
                if (skillItem.getId() != null) {
                    skill = skillManager.get(skillItem.getId());
                    if (skill == null) {
                        skill = new EdsSkill();
                    }
                }
                skill.setName(skillItem.getName());
                skill.setDescription(skillItem.getDescription());
                skill.setDefaultWeight(skillItem.getWeight());
                if (skillItem.getSkillNameLocalization() != null) {
                    EdsCustomFormLocalization nameLoc = customFormLocalizationManager.get(skillItem.getSkillNameLocalization().getId());
                    if (nameLoc == null) {
                        nameLoc = new EdsCustomFormLocalization();
                    }
                    nameLoc.setRussianName(skillItem.getSkillNameLocalization().getRussianName());
                    nameLoc.setEnglishName(skillItem.getSkillNameLocalization().getEnglishName());
                    nameLoc.setUzbekName(skillItem.getSkillNameLocalization().getUzbekName());
                    nameLoc.setArabicName(skillItem.getSkillNameLocalization().getArabicName());
                    skill.setNameLocalize(nameLoc);
                    customFormLocalizationManager.createOrUpdate(nameLoc);
                }
                if (skillItem.getSkillDescriptionLoc() != null) {
                    EdsCustomFormLocalization nameLoc = customFormLocalizationManager.get(skillItem.getSkillDescriptionLoc().getId());
                    if (nameLoc == null) {
                        nameLoc = new EdsCustomFormLocalization();
                    }
                    nameLoc.setRussianName(skillItem.getSkillDescriptionLoc().getRussianName());
                    nameLoc.setEnglishName(skillItem.getSkillDescriptionLoc().getEnglishName());
                    nameLoc.setUzbekName(skillItem.getSkillDescriptionLoc().getUzbekName());
                    nameLoc.setArabicName(skillItem.getSkillDescriptionLoc().getArabicName());
                    skill.setDiscriptionLocalize(nameLoc);
                    customFormLocalizationManager.createOrUpdate(nameLoc);
                }
                if (group != null) {
                    skill.setGroup(group);
                }
                skill.setLastUpdateDate(new Date());
                skillManager.createOrUpdate(skill);

                if (skill.getObjectID() != null && employeeID != null) {
                    EdsEmployee employee = employeeManager.get(employeeID);
                    if (employee != null) {
                        EdsEmployeeSkills employeeSkills = new EdsEmployeeSkills();
                        employeeSkills.setEmployee(employee);
                        employeeSkills.setSkill(skill);
                        employeeSkills.setType(EdsEmployeeSkills.Typer.SIMPLE);
                        employeeSkillsManager.create(employeeSkills);
                    }
                }

                skills.add(skill);
                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsSkill.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.ADD);
                kpiLog.setEntityId(skill.getObjectID());
                ServerUtils.kpiLog(log, kpiLog, "Add competency");
            }
            return ListUtils.createTreeItemArray(skills, new WfmTreeItemFactory<EdsSkill>() {
                public WfmTreeItem createItem(EdsSkill o) {
                    String skillName = hrmsLocalizer.localize(o.getCode(), o.getName());
                    String skillDescription = hrmsLocalizer.localize(o.getDescriptionCode(), o.getDescription());

                    WfmTreeItem parent = new WfmTreeItem(o.getGroup().getObjectID(), hrmsLocalizer.localize(o.getGroup().getCode(), o.getGroup().getName()));
                    return new WfmTreeItem(parent, o.getObjectID(), skillName, skillDescription, o.getDefaultWeight());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<SkillItem> getCompetencies(ListingFilterParameter filterParameters) {
        List<EdsSkill> competencies = skillManager.getSkillList(filterParameters);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSkill.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Competency list");
        return createCompetenciesList(filterParameters, competencies);
    }

    private ListResult<SkillItem> createCompetenciesList(ListingFilterParameter filterParameters, List<EdsSkill> competencies) {
        Long totalCount = skillManager.getSkillsTotal(filterParameters);
        ArrayList<SkillItem> list = new ArrayList<>();
        for (EdsSkill competency : competencies) {
            if (competency != null) {
                SkillItem item = new SkillItem();
                item.setId(competency.getObjectID());
                item.setName(hrmsLocalizer.localize(competency.getCode(), competency.getName()));
                item.setDescription(hrmsLocalizer.localize(competency.getDescriptionCode(), competency.getDescription()));
                if (competency.getGroup() != null && competency.getGroup().getObjectID() > 0) {
                    item.setGroupId(competency.getGroup().getObjectID());
                    String groupName = ServerUtils.isNullOrEmpty(competency.getGroup().getName()) ? hrmsLocalizer.localize(competency.getGroup().getCode(), competency.getGroup().getRealName()) : competency.getGroup().getName();
                    item.setGroupName(groupName);
                }
                list.add(item);
            }
        }
        return new ListResult<>(list, totalCount.intValue());
    }

    @Transactional
    public void deleteCompetency(Integer competencyID) {
        EdsSkill competency = skillManager.get(competencyID);
        if (competency != null) {
            competency.setDeleted(true);
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSkill.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(competencyID);
        ServerUtils.kpiLog(log, kpiLog, "Delete Competency");
    }

    @Transactional
    public void deleteEmployeeCompetency(Integer employeeID, Integer competencyID) {
        if (employeeID != null && competencyID != null) {
            EdsEmployeeSkills employeeSkill = employeeSkillsManager.getEmployeeSkill(employeeID, competencyID, EdsEmployeeSkills.Typer.SIMPLE);
            if (employeeSkill != null) {
                employeeSkill.setDeleted(true);
            }
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsEmployeeSkills.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(competencyID);//zdes mojet, employee skill ID?
            ServerUtils.kpiLog(log, kpiLog, "Delete Employee Competency");
        }
    }

    @Transactional
    public Integer addSkillGroup(SkillGroupItem skillGroupItem) {
        try {
            EdsSkillGroup edsSkillGroup = null;
            boolean isEdit = skillGroupItem.getId() != null;

            // Fetch existing for edit
            if (isEdit) {
                edsSkillGroup = skillGroupManager.get(skillGroupItem.getId());
            }

            if (edsSkillGroup == null) {
                edsSkillGroup = new EdsSkillGroup();
            }

            // Duplicate check

            if (StringUtils.isNotBlank(skillGroupItem.getName())) {
                EdsSkillGroup existing = skillGroupManager.findByName(skillGroupItem.getName().trim());
                if (existing != null && (existing.getDeleted() == null || !existing.getDeleted())) {
                    if (!isEdit) { // Adding → block duplicate
                        log.warn("Duplicate SkillGroup '{}' not saved (already exists)", skillGroupItem.getName());
                        return null;
                    }
                    // Editing → block duplicate if different ID
                    if (!existing.getObjectID().equals(skillGroupItem.getId())) {
                        log.warn("Duplicate SkillGroup '{}' not saved (already exists under another ID)", skillGroupItem.getName());
                        return null;
                    }
                }
            }


            // Set name
            edsSkillGroup.setName(StringUtils.defaultString(skillGroupItem.getName()));

            // Set localization if present
            if (skillGroupItem.getLocalization() != null) {
                edsSkillGroup.setLocalization(
                        customFormLocalizationManager.get(skillGroupItem.getLocalization().getId())
                );
            }

            // Handle parentName and set parent only if changed
            String parentNameInput = StringUtils.trimToNull(skillGroupItem.getParentName());
            EdsSkillGroup currentParent = edsSkillGroup.getParent();

            if (parentNameInput != null) {
                // Only update if the parent is different
                if (currentParent == null || !currentParent.getName().equalsIgnoreCase(parentNameInput)) {
                    EdsSkillGroup parentGroup = skillGroupManager.findByName(parentNameInput);
                    if (parentGroup != null) {
                        edsSkillGroup.setParent(parentGroup);
                        log.info("Set parent group '{}' for skill group '{}'",
                                parentGroup.getName(), skillGroupItem.getName());
                    } else {
                        log.warn("Parent SkillGroup not found for name: {}", parentNameInput);
                        edsSkillGroup.setParent(null); // optional: clear if not found
                    }
                }
            } else {
                // Remove parent if input is empty
                if (currentParent != null) {
                    edsSkillGroup.setParent(null);
                    log.debug("Parent removed for '{}'", skillGroupItem.getName());
                }
            }

            // Save or update
            skillGroupManager.createOrUpdate(edsSkillGroup);
            log.info("SkillGroup '{}' saved with ID {}", edsSkillGroup.getName(), edsSkillGroup.getObjectID());

            return edsSkillGroup.getObjectID();

        } catch (Exception e) {
            log.error("Error saving SkillGroup: {}", skillGroupItem.getName(), e);
            return null;
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyDepartments() {
        DepartmentItem[] departmentItems = departmentService.getDepartmentsSelectItem();
        SelectItem[] items = new SelectItem[departmentItems.length];
        for (int i = 0;
             i < departmentItems.length;
             i++) {
            items[i] = new SelectItem(departmentItems[i].getDepatmentID(), departmentItems[i].getDepartmentName());
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmployeeByDepartment(Integer departmentId, Integer roleId, Integer appraisedEmplId) {
        SelectItem[] employeeItems = null;
        EdsUser initiator = assessmentManager.getUser();
        Integer initiatorId = initiator.getObjectID();
        int i = 0;
        List<EdsEmployee> employeeList = assessmentManager.getTeamEmployeeByRole(departmentId, roleId, initiatorId, appraisedEmplId);
        if (employeeList != null && employeeList.size() > 0) {
            employeeItems = new SelectItem[employeeList.size()];
            for (EdsEmployee employee : employeeList) {
                employeeItems[i] = new SelectItem(employee.getObjectID(), employee.getName());
                i++;
            }
        }
        return employeeItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyClientContacts() {
        SelectItem[] clientItems = null;
        EdsCompany company = employeeManager.getUser().getCompany();
        List<Object[]> clients = clientContactManager.getClientsByCompany(company);
        if (clients != null && clients.size() > 0) {
            int i = 0;
            String clientName = null;
            clientItems = new SelectItem[clients.size()];
            for (Object[] client : clients) {
                Integer clientId = (Integer) client[0];
                if (client[1] != null || client[1].equals("")) {
                    clientName = (String) client[1];
                }
                clientItems[i] = new SelectItem(clientId, clientName);
                i++;
            }
        }

        return clientItems;
    }

    @Transactional
    public void deleteTemplate(Integer templateID) {
        EdsAssessmentTemplate template = assessmentTemplateManager.get(templateID);
        template.setDeleted(true);
        assessmentTemplateManager.update(template);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsAssessmentTemplate.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(templateID);
        ServerUtils.kpiLog(log, kpiLog, "Delete template");
    }

    @Transactional
    public void deleteAssessment(Integer emploAssessmentID) {
        EdsEmployeeAssessment employeeAssessment = employeeAssessmentManager.get(emploAssessmentID);
        EdsAssessment assessment = employeeAssessment.getAssessment();
        assessment.setDeleted(true);
        employeeAssessment.setDeleted(true);
        EdsUser user = userManager.getUser();
        assessment.setLastUpdater(user);
        assessment.setLastUpdateTime(new Date());
        employeeAssessmentManager.update(employeeAssessment);

        try {
            messageManager.sendDeleteAssessmentNotification(employeeAssessment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsAssessment.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(assessment.getObjectID());
        ServerUtils.kpiLog(log, kpiLog, "Delete appraisal");
    }

    @Transactional
    public void sendAssessmentResultToEmployee(Integer employeeAssessmentId, Integer loggedUserId) {
        try {
            log.info("SENDING NOTIFICATION OF ASSESSMENT ID: " + employeeAssessmentId);
            EdsEmployeeAssessment employeeAssessment = employeeAssessmentManager.get(employeeAssessmentId);
            String status = employeeAssessment.getStatus().getCode();
            log.info("ASSESSMENT STATUS: " + status);

            switch (status) {
                case Constants.APPROVED -> {
                    ByteArrayOutputStream baos = assessmentViewPDFHandler.getPDFStream(new RequestObject(employeeAssessment.getObjectID(), loggedUserId));
                    messageManager.sendAssessmentApproveNotification(employeeAssessment, baos);
                }
                case Constants.RATED ->
                        messageManager.sendAssessmentRateNotification(employeeAssessment, loggedUserId, null/*baos*/);
                case Constants.REVIEWED_BY_MANAGER ->
                        messageManager.sendAssessmentReviewNotification(employeeAssessment, null/*baos*/);
                case Constants.REVIEWED_BY_EMPLOYEE ->
                        messageManager.sendAssessmentReviewNotification(employeeAssessment, null);
            }
        } catch (EdsDbException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public AppraisalsSettingsItem getAppraisalsSettings() {
        EdsUser user = userManager.getUser();
        return getAppraisalsSettings(user.getObjectID());
    }

    @Override
    public AppraisalsSettingsItem getAppraisalsSettings(Integer currentUserID) {
        EdsAppraisalsSettings appraisalsSettings = assessmentManager.getAppraisalsSettings();
        List<EdsAppraisalRate> appraisalRates = assessmentManager.getAppraisalRates();
        List<EdsApprasialScoreType> appraisalScoreTypes = assessmentManager.getAppraisalScoreTypes();
        if (appraisalsSettings == null) {
            appraisalsSettings = new EdsAppraisalsSettings();
            appraisalsSettings.setFromScale(0d);
            appraisalsSettings.setToScale(7d);
            appraisalsSettings.getReviewers().add(roleManager.getByCode(EdsRole.HR_CODE));
            appraisalsSettings.getReviewers().add(roleManager.getByCode(EdsRole.ADMIN_CODE));
            appraisalsSettings.getReviewers().add(roleManager.getByCode(EdsRole.TL_CODE));

            appraisalsSettings.setUseGoals(true);
            appraisalsSettings.setUseCompetencies(true);
            assessmentManager.createOrUpdateAppraisalsSettings(appraisalsSettings);
        }
        AppraisalsSettingsItem settingsItem = appraisalsSettings.getDTO();

        EdsUser user = userManager.get(currentUserID);
        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
        boolean customRateEnable = companySystemSettings != null ? companySystemSettings.getCustomRateEnable() : false;
        settingsItem.setCustomRateEnable(customRateEnable);

        ArrayList<AppraisalsScoreTypeItem> typeItems = new ArrayList<>();
        for (EdsApprasialScoreType appraisalScoreType : appraisalScoreTypes) {
            AppraisalsScoreTypeItem item = new AppraisalsScoreTypeItem();
            item.setName(appraisalScoreType.getName());
            item.setRate(appraisalScoreType.getRate());
            item.setGrade(appraisalScoreType.getGrade());
            typeItems.add(item);
            settingsItem.setScoreTypeItems(typeItems);
        }
        if (customRateEnable) {
            for (EdsAppraisalRate rate : appraisalRates) {
                settingsItem.getCustomRates().put(rate.getRating(), rate.getName());
            }

            settingsItem.setFromScale(ServerAssessmentHelper.getMinValue(settingsItem.getCustomRates()).doubleValue());
            settingsItem.setToScale(ServerAssessmentHelper.getMaxValue(settingsItem.getCustomRates()).doubleValue());
        }
        return settingsItem;
    }

    @Override
    @Transactional
    public void updateAppraisalsSettings(AppraisalsSettingsItem item) {
        EdsAppraisalsSettings appraisalsSettings = assessmentManager.getAppraisalsSettings();
        if (appraisalsSettings == null) {
            appraisalsSettings = new EdsAppraisalsSettings();
        }

        //set old from/to scale & stage size
        appraisalsSettings.setOldFromScale(appraisalsSettings.getFromScale());
        appraisalsSettings.setOldToScale(appraisalsSettings.getToScale());
        appraisalsSettings.setOldStepSize(appraisalsSettings.getStepSize());

        appraisalsSettings.setFromScale(item.getFromScale());
        appraisalsSettings.setToScale(item.getToScale());
        appraisalsSettings.setStepSize(item.getStepSize());
        appraisalsSettings.setUseCompetencies(item.isUseCompetencies());
        appraisalsSettings.setUseGoals(item.isUseGoals());
        appraisalsSettings.setEmployeeRate(item.isEmployeeRate());
        if (item.getScoreTypeItems() != null && item.getScoreTypeItems().size() > 0) {
            assessmentManager.deleteScoreTypes();
            for (AppraisalsScoreTypeItem scoreTypeItem : item.getScoreTypeItems()) {
                EdsApprasialScoreType scoreType = new EdsApprasialScoreType();
                scoreType.setRate(scoreTypeItem.getRate());
                scoreType.setName(scoreTypeItem.getName());
                scoreType.setGrade(scoreTypeItem.getGrade());
                assessmentManager.createScoreTypes(scoreType);
            }
        }
        assessmentManager.createOrUpdateAppraisalsSettings(appraisalsSettings);
    }

    @Override
    public void deletedValidityPeriodItem(ValidityPeriodItem item) throws InsufficientPermissionsException {
        EdsValidityPeriod validityPeriod = validityPeriodManager.get(item.getId());
        if (validityPeriod != null) {
            try {
                Boolean isUsedGoal = goalManager.isUsedValidityPeriod(validityPeriod);
                Boolean isUsedAssessment = assessmentManager.isUsedValidityPeriod(validityPeriod);
                if (isUsedGoal || isUsedAssessment) {
                    throw new InsufficientPermissionsException("You don't have the necessary permissions");
                }
            } catch (Exception e) {
                throw new InsufficientPermissionsException("You don't have the necessary permissions");
            }

            validityPeriod.setDeleted(true);
            validityPeriodManager.update(validityPeriod);
        }
    }

    @Override
    public Integer createValidityPeriodItem(ValidityPeriodItem item) throws InsufficientPermissionsException {
        boolean exist = validityPeriodManager.checkOverlaps(item);
        if (exist) {
            throw new InsufficientPermissionsException("There can't be 2 bonus type validity periods for the indicated time interval. Please modify start and end dates of the validity period!");
        }
        EdsValidityPeriod validityPeriod;
        if (item.getId() != null) {
            validityPeriod = validityPeriodManager.get(item.getId());
        } else {
            validityPeriod = new EdsValidityPeriod();
        }
        validityPeriod.setName(item.getName());
        validityPeriod.setDescription(item.getDescription());
        validityPeriod.setFromDate(item.getFromDate());
        validityPeriod.setToDate(item.getToDate());
        validityPeriod.getPeriodTypeItems().clear();
        Boolean isPeriodAppraisal = false;
        for (String periodTypeCode : item.getPeriodTypeCodeItems()) {
            validityPeriod.getPeriodTypeItems().add(referenceManager.findReference(ValidityPeriodItem._VALIDITY_PERIOD_TYPE, periodTypeCode));
            if (ValidityPeriodItem.VALIDITY_PERIOD_APPRAISAL.equals(periodTypeCode)) {
                isPeriodAppraisal = true;
            }
        }

        validityPeriodManager.createOrUpdate(validityPeriod);
        if (validityPeriod.getToDate().after(new Date()) && isPeriodAppraisal) {
            if (item.getId() == null) {
                createRecurrence(validityPeriod);
            } else {
                EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(SchedulerConstant.RECURRING_ASSESSMENT_REMINDER, item.getId(), userManager.getUser().getCompany().getObjectID());
                if (recurrence != null) {
                    recurrence.setStartDate(validityPeriod.getToDate());
                    recurrence.setEndDate(validityPeriod.getToDate());
                    recurrenceService.reLoadTrigger(recurrence);
                } else {
                    createRecurrence(validityPeriod);
                }
            }
        }
        return validityPeriod.getObjectID();
    }

    private void createRecurrence(EdsValidityPeriod validityPeriod) {
        final long ONE_MINUTE_IN_MILLIS = 60000;//millisecs
        RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
        recurrenceJobItem.setEnabled(true);
        recurrenceJobItem.setJobType(SchedulerConstant.RECURRING_ASSESSMENT_REMINDER);
        recurrenceJobItem.setUserTimeZone(null);
        recurrenceJobItem.setStartDate(validityPeriod.getToDate());

        recurrenceJobItem.setEndDate(new Date(validityPeriod.getToDate().getTime() + (10 * ONE_MINUTE_IN_MILLIS)));
        recurrenceJobItem.setBusObjectId(validityPeriod.getObjectID());
        recurrenceJobItem.setType(SchedulerConstant.RECURRENCE_TYPE_YEARLY);
        recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
        recurrenceJobItem.setMonthlyOrYearlyDay(validityPeriod.getFromDate().getDate());
        recurrenceJobItem.setYearlyMonth(validityPeriod.getFromDate().getMonth() + 1);
        recurrenceJobItem.setEndType(SchedulerConstant.END_AFTER_OCCURRENCES);
        recurrenceJobItem.setOccurrence(1);
        recurrenceJobItem.setInterval(1);
        recurrenceJobItem.setDailyPatternOptions(SchedulerConstant.DAILY_PATTERN_OPTION_INTERVAL);
        recurrenceService.saveRecurrenceJob(recurrenceJobItem);
    }

    @Override
    public ListResult<ValidityPeriodItem> getValidityPeriodList(ListingFilterParameter filterParameters) {
        List<EdsValidityPeriod> periodList = validityPeriodManager.list(filterParameters);
        Long total = validityPeriodManager.listSize(filterParameters);
        ArrayList<ValidityPeriodItem> periodItems = new ArrayList<>();
        for (EdsValidityPeriod validityPeriod : periodList) {
            periodItems.add(validityPeriod.getDTO());
        }
        return new ListResult<>(periodItems, total.intValue());
    }

    @Override
    public ValidityPeriodItem getValidityPeriod(Integer id) {
        EdsValidityPeriod validityPeriod = validityPeriodManager.get(id);
        return validityPeriod.getDTO();
    }

    @Override
    public ValidityPeriodItem[] getValidityPeriods(String periodType) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStatusCode(periodType);
        return validityPeriodManager.getValidityPeriods(fp);
    }

    @Override
    public void updateAppraisalsStatus(ArrayList<Integer> ids, String statusCode) {
        assessmentManager.updateAppraisalsStatus(ids, statusCode);
    }

    @Override
    public BonusSettingsItem getBonusSettings(Integer periodId) {
        EdsValidityPeriod validityPeriod;
        if (periodId == null) {
            validityPeriod = validityPeriodManager.getCurrentValidityPeriod(ValidityPeriodItem.VALIDITY_PERIOD_BONUS);
        } else {
            validityPeriod = validityPeriodManager.get(periodId);
        }
        EdsBonusSettings bonusSettings = bonusSettingsManager.getBonusSettingsByPeriod(validityPeriod);
        if (bonusSettings == null) {
            bonusSettings = new EdsBonusSettings();
            bonusSettings.setValidityPeriod(validityPeriod);
            bonusSettings.setBudgetAmount(0d);
            bonusSettings.setBudgetId("");
            bonusSettings.setStatus(referenceManager.findReference(BonusSettingsItem._BONUS_SETTINGS_STATUS, BonusSettingsItem.BONUS_SETTINGS_DRAFT));
            bonusSettingsManager.createOrUpdate(bonusSettings);
        }
        return bonusSettings.getDTO();
    }

    @Override
    public void saveBonusSettings(BonusSettingsItem item) {
        EdsBonusSettings bonusSettings;
        if (item.getObjectId() == null) {
            bonusSettings = new EdsBonusSettings();
        } else {
            bonusSettings = bonusSettingsManager.get(item.getObjectId());
        }

        bonusSettings.setStatus(referenceManager.findReference(BonusSettingsItem._BONUS_SETTINGS_STATUS, item.getStatusCode()));
        bonusSettings.setValidityPeriod(validityPeriodManager.get(item.getValidityPeriod().getId()));
        bonusSettings.setBudgetAmount(item.getBudgetAmount());
        bonusSettings.setBudgetId(item.getBudgetId());
        bonusSettings.setEnableForcedDistributionRanking(item.isEnableForcedDistributionRanking());
        bonusSettings.getScoreItems().clear();

        for (ScoreItem scoreItem : item.getScoreItemHashMap().values()) {
            EdsScoreItem edsScoreItem;
            if (scoreItem.getObjectId() == null) {
                edsScoreItem = new EdsScoreItem();
            } else {
                edsScoreItem = bonusSettingsManager.getScoreItem(scoreItem.getObjectId());
            }
            edsScoreItem.setBonusDistribution(scoreItem.getBonusDistribution());
            edsScoreItem.setEmployeePercentage(scoreItem.getEmployeePercentage());
            edsScoreItem.setFromScore(scoreItem.getFromScore());
            edsScoreItem.setToScore(scoreItem.getToScore());
            edsScoreItem.setName(scoreItem.getName());
            edsScoreItem.setRemainderBonusDistribution(scoreItem.getRemainderBonusDistribution());
            bonusSettingsManager.createOrUpdateScoreItem(edsScoreItem);

            bonusSettings.getScoreItems().add(edsScoreItem);
        }

        bonusSettingsManager.createOrUpdate(bonusSettings);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DepartmentPeriodAppraisalItem getDepartmentPeriodAppraisalDataForPeriodAppraisal(Integer periodId) throws ObjectNotFoundException {
        EdsUser user = userManager.getUser();
        DepartmentPeriodAppraisalItem appraisalDataItem = new DepartmentPeriodAppraisalItem();

        EdsDepartment department = departmentManager.getDepartmentByLeader(user);
        EdsValidityPeriod validityPeriod = validityPeriodManager.get(periodId);
        EdsAppraisalApproval edsAppraisalApproval = appraisalApprovalManager.getAppraisalApprovalByDepartmentAndPeriod(department, validityPeriod);

        if (edsAppraisalApproval != null) {
            appraisalDataItem.setStatusCode(edsAppraisalApproval.getStatus().getCode());
            appraisalDataItem.setObjectId(edsAppraisalApproval.getObjectID());
        }

        if (department != null) {
            appraisalDataItem.setDepartmentName(department.getName());
            appraisalDataItem.setDepartmentId(department.getObjectID());
            appraisalDataItem.setDepartmentLeaderId(department.getLeader().getObjectID());
            appraisalDataItem.setDepartmentLeaderName(department.getLeader().getFullName());
            appraisalDataItem.setMembersCount(employeeManager.getEmployeesCountByDepartment(department).intValue());
        }
        appraisalDataItem.setValidityPeriodItem(validityPeriod.getDTO());

        EdsBonusSettings bonusSettings = bonusSettingsManager.getBonusSettingsByDate(validityPeriod.getFromDate(), validityPeriod.getToDate());
        if (bonusSettings == null) {
            throw new ObjectNotFoundException("Please setup bonus settings!");
        }
        appraisalDataItem.setBonusSettingsItem(bonusSettings.getDTO());

        return appraisalDataItem;
    }

    @Override
    public DepartmentPeriodAppraisalItem getDepartmentPeriodAppraisalItem(Integer objectId) throws ObjectNotFoundException {
        EdsAppraisalApproval approval = appraisalApprovalManager.get(objectId);
        return wrapAppraisalApprovalToDTO(approval);
    }

    private DepartmentPeriodAppraisalItem wrapAppraisalApprovalToDTO(EdsAppraisalApproval approval) throws ObjectNotFoundException {
        DepartmentPeriodAppraisalItem appraisalDataItem = new DepartmentPeriodAppraisalItem();

        appraisalDataItem.setObjectId(approval.getObjectID());
        appraisalDataItem.setComment(approval.getComments());
        appraisalDataItem.setDate(approval.getDate());

        EdsBonusSettings bonusSettings = bonusSettingsManager.getBonusSettingsByDate(approval.getValidityPeriod().getFromDate(), approval.getValidityPeriod().getToDate());
        if (bonusSettings == null) {
            throw new ObjectNotFoundException("Please setup bonus settings!");
        }
        BonusSettingsItem bonusSettingsItem = bonusSettings.getDTO();

        ArrayList<AssessmentsListElem> assessmentsListElems = new ArrayList<>();
        for (EdsAssessment assessment : approval.getAssessments()) {
            assessmentsListElems.add(createAssessmentListElem(assessment));
        }

        HashMap<String, Integer> scoreMap = new HashMap<>();
        List<Integer> employeeIdList = new ArrayList<>();
        int assessedEmployees = 0;
        for (AssessmentsListElem listElem : assessmentsListElems) {
            if (!employeeIdList.contains(listElem.getEmployeeID())) {
                employeeIdList.add(listElem.getEmployeeID());
                assessedEmployees++;
                ScoreItem scoreItem = bonusSettingsItem.getScoreItem(listElem.getOverallScore());
                if (scoreItem != null) {
                    Integer count = 0;
                    if (scoreMap.containsKey(scoreItem.getName())) {
                        count = scoreMap.get(scoreItem.getName());
                    }
                    scoreMap.put(scoreItem.getName(), count + 1);
                }
            }
        }

        appraisalDataItem.setMembersCount(employeeManager.getEmployeesCountByDepartment(approval.getDepartment()).intValue());
        appraisalDataItem.setAssessmentsListElems(assessmentsListElems);
        appraisalDataItem.setEmployeeAssessed(assessedEmployees);
        appraisalDataItem.setEmployeeNotAssessed(approval.getDepartment().getMembers().size() - assessedEmployees);
        appraisalDataItem.setDepartmentName(approval.getDepartment().getName());
        appraisalDataItem.setDepartmentId(approval.getDepartment().getObjectID());
        appraisalDataItem.setDepartmentLeaderId(approval.getDepartment().getLeader().getObjectID());
        appraisalDataItem.setDepartmentLeaderName(approval.getDepartment().getLeader().getFullName());
        appraisalDataItem.setValidityPeriodItem(approval.getValidityPeriod().getDTO());
        appraisalDataItem.setBonusSettingsItem(bonusSettingsItem);
        appraisalDataItem.setScoreMap(scoreMap);
        appraisalDataItem.setStatusCode(approval.getStatus().getCode());
        appraisalDataItem.setStatusName(approval.getStatus().getName());
        appraisalDataItem.setRejectionReasonComment(approval.getRejectionReasonComment());
        return appraisalDataItem;
    }

    @Override
    public void updateDepartmentPeriodAppraisal(DepartmentPeriodAppraisalItem item) throws InsufficientPermissionsException {
        if (employeeManager.getHRManagers().isEmpty()) {
            throw new InsufficientPermissionsException("There are no HR employees with HR Role in your company, please contact your administrator!");
        }

        EdsDepartment department = departmentManager.get(item.getDepartmentId());
        EdsValidityPeriod validityPeriod = validityPeriodManager.get(item.getValidityPeriodItem().getId());

        EdsAppraisalApproval edsAppraisalApproval = null;
        if (item.getObjectId() != null) {
            edsAppraisalApproval = appraisalApprovalManager.get(item.getObjectId());
        }
        if (edsAppraisalApproval == null) {
            edsAppraisalApproval = appraisalApprovalManager.getAppraisalApprovalByDepartmentAndPeriod(department, validityPeriod);
            if (edsAppraisalApproval == null) {
                edsAppraisalApproval = new EdsAppraisalApproval();
            }
            edsAppraisalApproval.setDepartment(department);
            edsAppraisalApproval.setValidityPeriod(validityPeriod);
            edsAppraisalApproval.setComments(item.getComment());
        }

        if (!item.getEmployeeAssessments().isEmpty()) {
            edsAppraisalApproval.setAssessments(assessmentManager.getAssessmentsByIds(item.getEmployeeAssessments()));
        }

        edsAppraisalApproval.setUpdater(appraisalApprovalManager.getUser());
        edsAppraisalApproval.setLastModifiedDate(new Date());
        edsAppraisalApproval.setDate(new Date());
        EdsReference status = referenceManager.findReference(DepartmentPeriodAppraisalItem._PERIOD_APPRAISAL, item.getStatusCode());
        edsAppraisalApproval.setStatus(status);

        //set rejection reason comment
        if (DepartmentPeriodAppraisalItem.PERIOD_REJECTED.equals(status.getCode())) {
            edsAppraisalApproval.setRejectionReasonComment(item.getRejectionReasonComment());
        }

        appraisalApprovalManager.createOrUpdate(edsAppraisalApproval);

        if (DepartmentPeriodAppraisalItem.PERIOD_APPROVED.equals(status.getCode())) {
            EdsReference approvedByHr = referenceManager.findReference(ASSESSMENT_STATUS, APPROVED);
            List<Integer> employeeAssessmentIdList = new ArrayList<>();
            for (AssessmentsListElem listElem : item.getAssessmentsListElems()) {
                employeeAssessmentIdList.add(listElem.getEmployeeAssessmentId());
            }
            assessmentManager.updatePeriodAssessmentsByDepartment(employeeAssessmentIdList, approvedByHr);
        }
        if (DepartmentPeriodAppraisalItem.PERIOD_REJECTED.equals(status.getCode())) {
            EdsReference rejectedByHr = referenceManager.findReference(ASSESSMENT_STATUS, REJECTED);
            List<Integer> employeeAssessmentIdList = new ArrayList<>();
            for (AssessmentsListElem listElem : item.getAssessmentsListElems()) {
                employeeAssessmentIdList.add(listElem.getEmployeeAssessmentId());
            }
            assessmentManager.updatePeriodAssessmentsByDepartment(employeeAssessmentIdList, rejectedByHr);
        }
        messageManager.sendPeriodAppraisalNotification(edsAppraisalApproval);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<DepartmentPeriodAppraisalItem> getDepartmentPeriodAppraisalItems(ListingFilterParameter fp) throws ObjectNotFoundException {
        List<Integer> statusIds = new ArrayList<>();
        if (fp.getStatusCodes() != null) {
            for (String code : fp.getStatusCodes()) {
                EdsReference status = referenceManager.findReferenceByCode(code);
                if (status != null) {
                    statusIds.add(status.getObjectID());
                }
            }
            fp.setStatusIDs(statusIds.toArray(new Integer[0]));
        }
        List<EdsAppraisalApproval> edsItems = appraisalApprovalManager.list(fp);

        Long total = appraisalApprovalManager.size(fp);

        ArrayList<DepartmentPeriodAppraisalItem> rpcItems = new ArrayList<>();

        for (EdsAppraisalApproval approval : edsItems) {
            DepartmentPeriodAppraisalItem item = wrapAppraisalApprovalToDTO(approval);
            rpcItems.add(item);
        }

        return new ListResult<>(rpcItems, total.intValue());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmployeesByReviewerId(Integer reviewerId) {
        EdsUser user;
        if (reviewerId != null) {
            user = userManager.get(reviewerId);
        } else {
            user = userManager.getUser();
        }
        AppraisalsSettingsItem settingsItem = getAppraisalsSettings();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setAllEmployees(true);
        if (settingsItem.getReviewers().contains(EdsRole.ADMIN_CODE) && user.hasEitherRoles(EdsRole.ADMIN_CODE)) {

        } else if (settingsItem.getReviewers().contains(EdsRole.HR_CODE) && user.hasEitherRoles(EdsRole.HR_CODE)) {

        } else if (settingsItem.getReviewers().contains(EdsRole.TL_CODE) && user.hasEitherRoles(EdsRole.TL_CODE)) {
            EdsDepartment department = departmentManager.getDepartmentByLeader(user);
            if (department != null) {
                fp.setDepartmentId(department.getObjectID());
            }
        }
        fp.setResignedEmployeesIncluded(false);
        ListResult<EmployeeListItem> employees = employeeServiceLocal.getEmployeeList(fp);
        if (employees == null) {
            return new SelectItem[1];
        }
        SelectItem[] emSelectItems = new SelectItem[employees.getList().size()];
        int i = 0;
        for (EmployeeListItem employee : employees.getList()) {
            emSelectItems[i++] = new SelectItem(employee.getObjectID(), employee.getFullName(), employee.getStatusCode());
        }
        return emSelectItems;
    }

    /**
     * Create or Update Bonus Distribution object
     *
     * @param bonusDistributionItem RPC object
     */
    @Override
    @Transactional
    public void saveBonusDistribution(BonusDistributionItem bonusDistributionItem) {
        EdsBonusDistribution edsBonusDistribution;
        if (bonusDistributionItem.getObjectId() != null) {
            edsBonusDistribution = bonusDistributionManager.get(bonusDistributionItem.getObjectId());
        } else {
            edsBonusDistribution = new EdsBonusDistribution();
        }
        edsBonusDistribution.setValidityPeriod(validityPeriodManager.get(bonusDistributionItem.getValidityPeriodId()));
        if (bonusDistributionItem.getDepartmentId() != null) {
            edsBonusDistribution.setDepartment(departmentManager.get(bonusDistributionItem.getDepartmentId()));
        } else {
            edsBonusDistribution.setDepartment(null);
        }
        //Bonus Distribution stage
        edsBonusDistribution.setStepName(bonusDistributionItem.getBonusDistributionStep().getStepName());
        //Bonus Distribution status
        if (bonusDistributionItem.getDistributionStatus() != null) {
            edsBonusDistribution.setDistributionStatus(referenceManager.get(bonusDistributionItem.getDistributionStatus().getId()));
        }
        //Approval Status(draft or approved)
        edsBonusDistribution.setApprovalStatus(referenceManager.findReference(BonusDistributionItem._BONUS_DISTRIBUTION_APPROVAL_STATUS, bonusDistributionItem.getApprovalStatus()));
        //Bonus Settings
        edsBonusDistribution.setBonusSettings(bonusSettingsManager.get(bonusDistributionItem.getBonusSettingsItem().getObjectId()));
        Map<Integer, EdsEmployeeBonusItem> employeeBonusItemMap = new HashMap<>();

        for (EdsEmployeeBonusItem employeeBonusItem : edsBonusDistribution.getEmployeeBonusItems()) {
            employeeBonusItemMap.put(employeeBonusItem.getObjectID(), employeeBonusItem);
        }

        edsBonusDistribution.getEmployeeBonusItems().clear();
        if (BonusDistributionItem.BonusDistributionStep.STEP1.equals(bonusDistributionItem.getBonusDistributionStep())) {
            String employeeIds = ServerUtils.getAsCommoDelimited(bonusDistributionItem.getSelectedEmployeeIdList(), "0");
            List<EdsEmployee> edsEmployees = employeeManager.getEmployeesByIds(employeeIds);
            for (EdsEmployee edsEmployee : edsEmployees) {
                EdsEmployeeBonusItem edsEmployeeBonusItem = new EdsEmployeeBonusItem();
                edsEmployeeBonusItem.setEmployee(edsEmployee);
                edsBonusDistribution.getEmployeeBonusItems().add(edsEmployeeBonusItem);
            }
        } else if (BonusDistributionItem.BonusDistributionStep.STEP2.equals(bonusDistributionItem.getBonusDistributionStep()) || BonusDistributionItem.BonusDistributionStep.STEP3.equals(bonusDistributionItem.getBonusDistributionStep())) {
            for (EligibleEmployeeItem employeeItem : bonusDistributionItem.getEligibleEmployeeItemList()) {
                EdsEmployeeBonusItem edsEmployeeBonusItem;
                if (employeeItem.getObjectId() != null && employeeBonusItemMap.containsKey(employeeItem.getObjectId())) {
                    edsEmployeeBonusItem = employeeBonusItemMap.get(employeeItem.getObjectId());
                } else {
                    edsEmployeeBonusItem = new EdsEmployeeBonusItem();
                    EdsEmployee edsEmployee = employeeManager.get(employeeItem.getEmployeeId());
                    edsEmployeeBonusItem.setEmployee(edsEmployee);
                }
                edsEmployeeBonusItem.setBonusAmount(employeeItem.getBonusAmount());
                edsEmployeeBonusItem.setRedistributedBonusAmount(employeeItem.getRedistributedBonusAmount());
                edsEmployeeBonusItem.setAverageScore(employeeItem.getAverageScore());
                edsBonusDistribution.getEmployeeBonusItems().add(edsEmployeeBonusItem);
            }
        }
        bonusDistributionManager.createOrUpdate(edsBonusDistribution);
    }

    /**
     * Create or update employee salary.
     *
     * @param employeeId  EdsEmployee id
     * @param basicSalary Employee basic salary.
     */
    @Override
    @Transactional
    public void updateEmployeeSalary(Integer employeeId, Double basicSalary) {
        EdsEmployee edsEmployee = employeeManager.get(employeeId);
        if (edsEmployee.getProfile() != null) {
            edsEmployee.getProfile().setSalaryAmount(basicSalary);
        }
        employeePayrollSettingsManager.update(edsEmployee, SALARY, String.valueOf(basicSalary));
        try {
            employeeSolrComponent.index(edsEmployee);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * For Employee Bonuses
     *
     * @param filterParametrs ListingFilterParameter
     * @return ListResult<EligibleEmployeeItem>
     */
    @Override
    public ListResult<EligibleEmployeeItem> getEligibleEmployeeList(ListingFilterParameter filterParametrs) {
        if (filterParametrs.getValidityPeriodId() == null) {
            return new ListResult<>(new ArrayList<>(), 0);
        }

        EdsBonusDistribution edsBonusDistribution = bonusDistributionManager.getApprovedBonusDistributionItem(filterParametrs.getValidityPeriodId(), filterParametrs.getDepartmentId());

        if (edsBonusDistribution == null) {
            return new ListResult<>(new ArrayList<>(), 0);
        }

        ComparatorFactory factory = null;
        if (filterParametrs.getSortField() != null && !"".equals(filterParametrs.getSortField())) {
            factory = comparatorFactories.get(filterParametrs.getSortField());
        }
        int sortDir = filterParametrs.isAscending() ? Constants.ASC : Constants.DESC;
        if (factory == null) {
            factory = comparatorFactories.get(EligibleEmployeeItem.EB_EMPLOYEE_NAME);
            sortDir = Constants.DESC;
        }
        List<EdsEmployeeBonusItem> edsEmployeeBonusItems = new ArrayList<EdsEmployeeBonusItem>(edsBonusDistribution.getEmployeeBonusItems());
        edsEmployeeBonusItems.sort(factory.createComparator(sortDir));

        int totalCount = edsEmployeeBonusItems.size();
        if (filterParametrs.getLimit() > 0) {
            edsEmployeeBonusItems = ListUtils.getSublist(edsEmployeeBonusItems, filterParametrs.getStart(), filterParametrs.getLimit());
        }

        BonusSettingsItem bonusSettingsItem = edsBonusDistribution.getBonusSettings().getDTO();
        ArrayList<EligibleEmployeeItem> eligibleEmployeeItems = (ArrayList<EligibleEmployeeItem>) wrapEmployeeBonusItems(edsBonusDistribution, bonusSettingsItem, edsEmployeeBonusItems);
        return new ListResult<>(eligibleEmployeeItems, totalCount);
    }

    private ArrayList<EligibleEmployeeItem> wrapEmployeeBonusItems(EdsBonusDistribution edsBonusDistribution, BonusSettingsItem bonusSettingsItem, List<EdsEmployeeBonusItem> edsEmployeeBonusItems) {
        ArrayList<EligibleEmployeeItem> eligibleEmployeeItems = new ArrayList<>();
        for (EdsEmployeeBonusItem edsEmployeeBonusItem : edsEmployeeBonusItems) {
            EligibleEmployeeItem employeeItem = new EligibleEmployeeItem();
            employeeItem.setObjectId(edsEmployeeBonusItem.getObjectID());
            employeeItem.setBonusSettingsItem(bonusSettingsItem);
            employeeItem.setBonusDistributionId(edsBonusDistribution.getObjectID());
            employeeItem.setEmployeeId(edsEmployeeBonusItem.getEmployee().getObjectID());
            employeeItem.setEmployeeName(edsEmployeeBonusItem.getEmployee().getFullName());
            EdsEmployeePayrollSettings salary = employeePayrollSettingsManager.getEmployeeSettingValue(employeeItem.getEmployeeId(), SALARY);
            if (salary != null) {
                employeeItem.setBasicSalary(Double.parseDouble(salary.getValue()));
            } else {
                employeeItem.setBasicSalary(0d);
            }
            employeeItem.setBonusAmount(edsEmployeeBonusItem.getBonusAmount());
            employeeItem.setRedistributedBonusAmount(edsEmployeeBonusItem.getRedistributedBonusAmount());


            employeeItem.setAverageScore(edsEmployeeBonusItem.getAverageScore());
            eligibleEmployeeItems.add(employeeItem);
        }
        return eligibleEmployeeItems;
    }

    @Override
    public BonusDistributionItem getApprovedBonusDistributionItem(Integer objectId) {
        BonusDistributionItem item = new BonusDistributionItem();
        EdsBonusDistribution edsBonusDistribution = bonusDistributionManager.get(objectId);
        BonusSettingsItem bonusSettingsItem = edsBonusDistribution.getBonusSettings().getDTO();
        String companyBalance = genericSettingsManager.getValueByKey(GenericSettingsEnum.COMPANY_BALANCE);
        if (!StringUtils.isEmpty(companyBalance)) {
            item.setCompanyBalance(Double.valueOf(companyBalance));
        } else {
            item.setCompanyBalance(0d);
        }

        item.setBonusSettingsItem(bonusSettingsItem);
        if (edsBonusDistribution.getDepartment() != null) {
            item.setDepartmentId(edsBonusDistribution.getDepartment().getObjectID());
            item.setDepartmentName(edsBonusDistribution.getDepartment().getName());
        }
        List<EdsReference> distributionStatusList = referenceManager.listReferences(BonusDistributionItem._BONUS_DISTRIBUTION_STATUS);

        SelectItem[] distributionStatusItems = new SelectItem[distributionStatusList.size()];

        int i = 0;
        for (EdsReference reference : distributionStatusList) {
            distributionStatusItems[i++] = reference.getAsSelectItem();
        }
        item.setDistributionStatusItems(distributionStatusItems);
        item.setDistributionStatus(edsBonusDistribution.getDistributionStatus().getAsSelectItem());
        item.setValidityPeriod(edsBonusDistribution.getValidityPeriod().getDTO());
        List<EdsEmployeeBonusItem> edsEmployeeBonusItems = new ArrayList<EdsEmployeeBonusItem>(edsBonusDistribution.getEmployeeBonusItems());
        ArrayList<EligibleEmployeeItem> eligibleEmployeeItems = wrapEmployeeBonusItems(edsBonusDistribution, bonusSettingsItem, edsEmployeeBonusItems);
        item.setEligibleEmployeeItemList(eligibleEmployeeItems);
        return item;
    }

    @Override
    public void returnLeftoverMoneyToCompany(Double remainingAmount) {
        String companyBalance = genericSettingsManager.getValueByKey(GenericSettingsEnum.COMPANY_BALANCE);
        Double companyBalanceValue = null;
        if (companyBalance != null && !"null".equals(companyBalance)) {
            companyBalanceValue = Double.valueOf(companyBalance) + remainingAmount;
        } else {
            companyBalanceValue = remainingAmount;
        }
        genericSettingsManager.saveGenericSettings(null, GenericSettingsEnum.COMPANY_BALANCE, String.valueOf(companyBalanceValue));
    }

    @Override
    public void recurrentlySendEmailToHR(Integer validityPeriodID) throws InsufficientPermissionsException {
        if (employeeManager.getHRManagers().isEmpty()) {
            throw new InsufficientPermissionsException("There are no HR employees with HR Role in your company, please contact your administrator!");
        }
        Map<EdsDepartment, List<EdsEmployee>> employees = getUnassessedEmployes(validityPeriodID);
        if (employees != null && !employees.isEmpty()) {
            messageManager.sendNotificationToHrRecurrently(employees);
        } else {
        }
    }


    @Override
    public ListResult<SkillGroupItem> getCompetencyGroupList(
            ListingFilterParameter filterParameter, String sortColumn, boolean ascending) {

        List<EdsSkillGroup> skillGroups = skillGroupManager.skillGroupList(filterParameter);

        ArrayList<SkillGroupItem> items = skillGroups.stream()
                .map(EdsSkillGroup::getRpc)
                .collect(Collectors.toCollection(ArrayList::new));

        // Sort in-memory
        if ("name".equalsIgnoreCase(sortColumn)) {
            items.sort((a, b) -> ascending
                    ? a.getName().compareToIgnoreCase(b.getName())
                    : b.getName().compareToIgnoreCase(a.getName()));
        } else if ("parent".equalsIgnoreCase(sortColumn)) {
            items.sort((a, b) -> ascending
                    ? a.getParentName().compareToIgnoreCase(b.getParentName())
                    : b.getParentName().compareToIgnoreCase(a.getParentName()));
        }

        return new ListResult<>(items, skillGroupManager.getCount().intValue());
    }



    @Override
    public void deleteCompetencyGroup(Integer id) {
        EdsSkillGroup skillGroup = skillGroupManager.get(id);
        skillGroup.setDeleted(true);
        skillGroupManager.createOrUpdate(skillGroup);
    }

    @Override
    public InitiatedAssessmentItem getDataFromShift(Integer shiftItemId) {
        InitiatedAssessmentItem item = new InitiatedAssessmentItem();
        EdsShiftItem edsShiftItem = shiftItemManager.get(shiftItemId);
        Date period = edsShiftItem.getShift().getPeriod();
        Calendar cal = Calendar.getInstance();
        cal.setTime(period);
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(edsShiftItem.getKey()));
        Date resultDate = cal.getTime();

        item.setEmployee(employeeManager.get(edsShiftItem.getGroupId()).getAsSelectItem());
        item.setAssessmentDate(resultDate);

        return item;
    }

    private Map<EdsDepartment, List<EdsEmployee>> getUnassessedEmployes(Integer validityPeriodID) {
        List<EdsEmployeeDepartment> notAssessedEmployeeList = assessmentManager.getUnassessedEmployeesByPeriodID(validityPeriodID);
        HashMap<EdsDepartment, List<EdsEmployee>> employeesGroupedByDepartment = new HashMap<>();
        if (notAssessedEmployeeList.size() != 0) {
            for (EdsEmployeeDepartment ed : notAssessedEmployeeList) {
                if (ed.getEmployee().getObjectID() != ed.getTeam().getLeader().getObjectID()) {
                    if (employeesGroupedByDepartment.containsKey(ed.getTeam())) {
                        if (!employeesGroupedByDepartment.get(ed.getTeam()).contains(ed.getEmployee())) {
                            employeesGroupedByDepartment.get(ed.getTeam()).add(ed.getEmployee());
                        }
                    } else {
                        LinkedList<EdsEmployee> employeeList = new LinkedList<>();
                        employeeList.add(ed.getEmployee());
                        employeesGroupedByDepartment.put(ed.getTeam(), employeeList);
                    }
                }
            }
        }
        return employeesGroupedByDepartment;
    }
}