package com.edatasite.workforce.gwt.hrms.server.app;

import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCandidateStatusHistory;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsDependent;
import com.edatasite.workforce.core.domain.EdsEducation;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsFormProperty;
import com.edatasite.workforce.core.domain.EdsJobFamily;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsSpokenLanguages;
import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.analyzer.EdsSolrDbConsistency;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsPlacementCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsVacancyCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.customform.EdsCustomItemTable;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacementItemTable;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacementItemTableCF;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancyItemTable;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancyItemTableCF;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancyNote;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancyQuestion;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeStepSolrComponent;
import com.edatasite.workforce.core.solr.component.VacancySolrComponent;
import com.edatasite.workforce.core.solr.document.ContactSolrDoc;
import com.edatasite.workforce.core.solr.document.VacancySolrDoc;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.LocationItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyQuestionTableItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrVacancyRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.RolePermissionServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.CandidateStatusHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.DependentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.FormPropertyManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.PlacementItemTableCFManager;
import com.edatasite.workforce.gwt.core.server.db.PlacementItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.PlacementManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.SpokenLanguagesManager;
import com.edatasite.workforce.gwt.core.server.db.StepEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyCustomQuestionsManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyItemTableCFManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.analyzer.SolrDbConsistencyManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.PLacementCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.VacancyCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PaymentDeductionManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.talentprofile.EducationManager;
import com.edatasite.workforce.gwt.core.server.db.vacancyNote.VacancyNoteManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.PlacementEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.KanbanCalculationEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.gwt.hrms.client.rpc.VacancyItemForTelegram;
import com.edatasite.workforce.gwt.hrms.server.db.JobFamilyManager;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

//import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;

/**
 * User: Ilxom Lutfullaev
 * Date: 6/22/12
 * Time: 4:38 PM
 */

@Transactional
@Service("recruitmentService")
public class RecruitmentServiceImpl implements RecruitmentService, RecruitmentServiceLocal, Constants {
    private static final Logger log = LoggerFactory.getLogger(RecruitmentServiceImpl.class);
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private SpokenLanguagesManager spokenLanguagesManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    UploadManager uploadManager;
    @Autowired
    CrmAccountManager crmAccountManager;
    @Autowired
    EventManager eventManager;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private JobFamilyManager jobFamilyManager;
    @Autowired
    private FormPropertyManager formPropertyManager;
    @Autowired
    private CommonServiceLocal commonService;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    @Qualifier("contactService")
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private ContactService contactService;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private PlacementManager placementManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    @Qualifier("rolePermissionService")
    private RolePermissionServiceLocal rolePermissionServiceLocal;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    @Qualifier("employeeService")
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private BugReportService bugReportService;
    @Autowired
    private SolrDbConsistencyManager solrDbConsistencyManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private StepEmployeeManager stepEmployeeManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private PaymentDeductionManager paymentDeductionManager;
    @Autowired
    private VacancyCFManager vacancyCFManager;
    @Autowired
    private PLacementCFManager pLacementCFManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("invoiceService")
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private VacancyNoteManager vacancyNoteManager;
    @Autowired
    private VacancyItemTableCFManager vacancyItemTableCFManager;
    @Autowired
    private VacancyItemTableManager vacancyItemTableManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private VacancySolrComponent vacancySolrComponent;


    @Autowired
    private CandidateStatusHistoryManager statusHistoryManager;


    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private PlacementItemTableManager placementItemTableManager;
    @Autowired
    private PlacementItemTableCFManager placementItemTableCFManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private MyUpdateTypeManager myUpdateTypeManager;
    @Autowired
    private CustomFormItemManager customFormItemManager;
    @Autowired
    private DocumentsService documentsService;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private EmployeeStepSolrComponent employeeStepSolrComponent;
    @Autowired
    private EducationManager educationManager;
    @Autowired
    private DependentManager dependentManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;
    @Autowired
    private VacancyCustomQuestionsManager vacancyCustomQuestionsManager;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public VacancyItem getVacancyItem(Integer objectID) {
        return this.getVacancyItem(objectID, null, null);
    }

    @Override
    public VacancyItem getVacancyItem(Integer objectID, String formType, Integer convertedFormId) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsVacancy.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectID);
        ServerUtils.kpiLog(log, kpiLog, "View vacancies summary");
        VacancyItem vacancyItem = new VacancyItem();
        PositionItem positionItem = new PositionItem();
        vacancyItem.setPositionItem(positionItem);
        EdsVacancy vacancy = null;

        EdsUser user = userManager.getUser();
        if (user.getLocation() != null) {
            vacancyItem.setCreatorLocation(user.getLocation().getAsSelectItem());
        }

        if (user.getEmployee().getTeam() != null) {
            vacancyItem.setCreatorDepatment(user.getEmployee().getTeam().getAsSelectItem());
        }

        if (objectID != null) {
            vacancy = vacancyManager.get(objectID);
            if (vacancy != null) {
                vacancyItem = vacancy.getRPC();

//                if (!ServerUtils.isNullOrEmpty(vacancyItem.getProposedSalary())) {
//                    String propSalary = vacancyItem.getProposedSalary().replaceAll(",", "").replaceAll(" ", "");
//                    vacancyItem.setProposedSalary(Utils.formatDouble(Double.parseDouble(propSalary.replaceAll(",", ""))));
//                }
                // Gender
                if (Constants.MALE.equals(vacancyItem.getGender()) || Constants.FEMALE.equals(vacancyItem.getGender())
                        || Constants.IRRELEVANT_GENDER.equals(vacancyItem.getGender())) {
                    vacancyItem.setGender(commonLocalizer.localize(vacancyItem.getGender().toLowerCase()));
                }
                EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                if (settings != null && settings.getVacancyNumberingFormat() != null) {
                    vacancyItem.getNumberData().setNumberFormat(settings.getVacancyNumberingFormat());
                } else {
                    vacancyItem.setNumberData(EdsNumberingSettings.getDefaultData(vacancyItem.getNumberData() != null &&
                            vacancyItem.getNumberData().getIntNumber() != null ?
                            vacancyItem.getNumberData().getIntNumber() - 1 : 1, EdsNumberingSettings.DEF_VACANCY_PREFIX));
                }
                if (vacancy.getPosition() != null) {
                    positionItem.setObjectID(vacancy.getPosition().getObjectID());
                    positionItem.setName(vacancy.getPosition().getName());
                }
                if (vacancy.getFullPartTime() != null) {
                    EdsReference fullPartTime = referenceManager.get(vacancy.getFullPartTime());
                    vacancyItem.setJobType(fullPartTime.getAsSelectItem());
                }
                if (vacancy.getJobFamily() != null) {
                    EdsJobFamily jobFamily = jobFamilyManager.get(vacancy.getJobFamily());
                    vacancyItem.setJobfamily(new SelectItem(vacancy.getJobFamily(), jobFamily != null ? jobFamily.getName() : ""));
                }
                vacancyItem.setTemplates(invoiceServiceLocal.getCompanyPdfTemplates(PDFConstants.VACANCY).getItems());
                EdsCompanyPdfTemplate template = companyPdfTemplateManager.getDefaultCompanyPdfTemplateByType(PDFConstants.VACANCY);
                if (template != null) {
                    vacancyItem.setSelectedTemplateId(template.getObjectID());
                }
                if (vacancy.getLocation() != null) {
                    vacancyItem.setLocation(vacancy.getLocation().getAsSelectItem());
                }
                if (vacancy.getLocale() != null) {
                    vacancyItem.setReferenceLocale(vacancy.getLocale().toRPC());
                }

                if (vacancy.getCurrency() != null) {
                    vacancyItem.setCurrency(vacancy.getCurrency().getAsSelectItem());
                }
            }
        } else {
            vacancyItem.setNumberData(generateVacancyNumber(objectID));
        }

        //Positions
        List<EdsPosition> positions;
        if (vacancyItem.getCreatorDepatment() != null) {
            positions = positionManager.getPositionListByDepartment(vacancyItem.getCreatorDepatment().getId());
        } else {
            positions = positionManager.list();
        }
        if (positions != null && !positions.isEmpty()) {
            ArrayList<SelectItem> result = new ArrayList<>();
            for (EdsPosition position : positions) {
                result.add(new SelectItem(position.getObjectID(), position.getNumberData() + " --> " + position.getName()));
            }
            vacancyItem.setPositions(ServerUtils.sortSelectItem(result.toArray(new SelectItem[]{})));
        }

        if (vacancy != null && vacancy.getDepartment() != null && vacancy.getDepartment().getDepartmentName() != null && vacancy.getDepartment().getDepartmentName().getLocale() != null) {
            EdsDepartment department = vacancy.getDepartment();
            EdsReferenceLocale locale = department.getDepartmentName().getLocale();
            switch (ServerUtils.getUserLocale().getLanguage()) {
                case "en":
                    vacancyItem.setDepartment(new SelectItem(vacancy.getDepartment().getObjectID(), department.getNumberData() + "-> " + locale.getEnglish()));
                    break;
                case "ru":
                    vacancyItem.setDepartment(new SelectItem(vacancy.getDepartment().getObjectID(), department.getNumberData() + "-> " + locale.getRussian()));
                    break;
                case "uz":
                    vacancyItem.setDepartment(new SelectItem(vacancy.getDepartment().getObjectID(), department.getNumberData() + "-> " + locale.getUzbek()));
                    break;
                case "ar":
                    vacancyItem.setDepartment(new SelectItem(vacancy.getDepartment().getObjectID(), department.getNumberData() + "-> " + locale.getArabic()));
                    break;
            }


        }
        //Job Types
        vacancyItem.setStatuses(commonService.convertReference2SelectItem(EdsVacancy.VACANCY_STATUSES, true, null));
        vacancyItem.setTimeTypes(commonService.convertReference2SelectItem(TIME_TYPES, true, null));


        //Job Families
        List<EdsJobFamily> jobs = jobFamilyManager.getJobFamilies(userManager.getUser().getCompany());
        SelectItem[] jobFamilies = new SelectItem[jobs.size()];
        for (int i = 0; i < jobs.size(); i++) {
            jobFamilies[i] = new SelectItem(jobs.get(i).getObjectID(), jobs.get(i).getName());
        }
        vacancyItem.setJobFamilies(jobFamilies);

        //Required Degrees
        vacancyItem.setRequiredDegrees(commonService.convertReference2SelectItem(EdsVacancy.VACANCY_DEGREES, true, null));

        //set matched candidates
        List<Object[]> contactList = vacancyManager.getVacancyMatchedCandidates(objectID);
        ArrayList<SelectItem> matchedVacancies = new ArrayList<>();
        for (Object[] contactItem : contactList) {
            if ((contactItem[0] != null) && (contactItem[1] != null)) {
                matchedVacancies.add(new SelectItem((Integer) contactItem[0], contactItem[1] + "(" + contactItem[2] + ")"));
            }
        }
        vacancyItem.setMatchedCandidates(matchedVacancies);
        ArrayList<CompanyCustomFieldItem> customFieldsItems = new ArrayList<>();
        if (vacancy != null) {
            customFieldsItems = commonService.getCompanyCustomFields(ViewName.Vacancy);
            vacancyItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(vacancy.getVacancyCustomFields(), customFieldsItems));
            vacancyItem.setJobTitleLocalize(WfmJsonUtils.jsonStringConvertToObject(vacancy.getJobTitleLocalize(), HashMap.class));
            vacancyItem.setDescriptionLocalize(WfmJsonUtils.jsonStringConvertToObject(vacancy.getDescriptionLocalize(), HashMap.class));
            vacancyItem.setJobRequirementLocalize(WfmJsonUtils.jsonStringConvertToObject(vacancy.getJobRequirementsLocalize(), HashMap.class));
            vacancyItem.setResponsibilitiesLocalize(WfmJsonUtils.jsonStringConvertToObject(vacancy.getResponsibilityLocalize(), HashMap.class));
        }
        if (vacancy != null) {
            ArrayList<EdsSpokenLanguages> spokenLanguages = spokenLanguagesManager.getListByRelation(vacancy.getObjectID(), EdsSpokenLanguages.TYPE_VACANCY);
            if (spokenLanguages != null) {
                ArrayList<SpokenLanguageItem> languageItems = new ArrayList<>(spokenLanguages.size());
                spokenLanguages.forEach(sl -> languageItems.add(new SpokenLanguageItem(sl.getLanguage() != null ? new SelectItem(sl.getLanguage().getObjectID(), referenceWfmMessageSource.localize(sl.getLanguage().getCode(), sl.getLanguage().getName())) : null, sl.getLevel() != null ? new SelectItem(sl.getLevel().getObjectID(), referenceWfmMessageSource.localize(sl.getLevel().getCode(), sl.getLevel().getName())) : null)));
                vacancyItem.setSpokenLanguages(languageItems);
            }
        }

        Set<EdsVacancyItemTable> vacancyItemTables = new HashSet<>();
        if (vacancy != null) {
            vacancyItemTables = vacancy.getItemTables();

            List<EdsVacancyQuestion> questions = vacancy.getCustomQuestions();

            VacancyQuestionTableItem[] items = (questions == null)
                    ? new VacancyQuestionTableItem[0]
                    : questions.stream()
                    .filter(Objects::nonNull)
                    .map(q -> {
                        VacancyQuestionTableItem item = new VacancyQuestionTableItem();
                        item.setFieldId(q.getFieldId());
                        item.setQuestionReference(q.getQuestionReference().getRPC());
                        return item;
                    })
                    .toArray(VacancyQuestionTableItem[]::new);
            vacancyItem.setVacancyQuestionItems(items);
        }

        HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

        if (vacancyItemTables != null && !vacancyItemTables.isEmpty()) {

            for (EdsVacancyItemTable itemTable : vacancyItemTables) {
                CustomTableRpc rpc = itemTable.getRpc();

                rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                        commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.VacancyItemTable, rpc.getUuid())));

                map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
            }
            vacancyItem.setCustomTableItems(map);
        }

        if (!StringUtils.isEmpty(formType) && convertedFormId != null && vacancy == null) {
            if (convertedFormId != null && formType.contains("_FORM")) {

                vacancyItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null,
                        this.commonService.getCompanyCustomFields(ViewName.Vacancy)));

                EdsFormProperty formProperty = formPropertyManager.getByFormID(LayoutRPC.VACANCY_FORM);
                vacancyItem.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(formType, convertedFormId)));
                Gson gson = new Gson();
                FormProperty[] fields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);

                EdsCustomFormItems edsItem = customFormItemManager.get(convertedFormId);
                FormItems formItems = edsItem.toRpc();
                Set<EdsCustomItemTable> itemTables = edsItem.getItemTables();
                vacancyItem.setRelationName(edsItem.getCustomForm().getName());

                HashMap<String, ArrayList<CustomTableRpc>> customFormItemTableMap = new HashMap<>();

                if (itemTables != null && itemTables.size() > 0) {

                    for (EdsCustomItemTable itemTable : itemTables) {
                        CustomTableRpc rpc = itemTable.getRpc();

                        rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                                commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, rpc.getUuid())));

                        customFormItemTableMap.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                    }
                    formItems.setTableItems(customFormItemTableMap);
                }
                Map<String, ArrayList<CustomTableRpc>> tableItems = formItems.getTableItems();

                for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                    tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
                }

                formItems.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getFormCustomFields(),
                        commonServiceLocal.getCompanyCategoryCustomFields(edsItem.getCustomForm() != null ? edsItem.getCustomForm().getObjectID() : null)));

                if (formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
                    for (int i = 0; i < formItems.getCustomFieldItems().size(); i++) {
                        if (UI_TYPE_AUTONUMBER.equals(formItems.getCustomFieldItems().get(i).getUiType()) && formItems.getCustomFieldItems().get(i).getFieldStringValue() != null) {
                            formItems.setAutoNumber(formItems.getCustomFieldItems().get(i).getFieldStringValue());
                            break;
                        }
                    }
                }
//                vacancyItem.setFromName(formItems.getAutoNumber() != null ? formItems.getAutoNumber() : formItems.getFormName() + ": " + formItems.getObjectID().toString());

                // formItems custom form itemni fieldari
                if (formItems.getCustomFieldItems() != null && formItems.getCustomFieldItems().size() > 0) {
                    for (CompanyCustomFieldItem companyCustomFieldItem : formItems.getCustomFieldItems()) {
                        convertFormCustomFields(vacancyItem, fields, companyCustomFieldItem, convertedFormId);
                    }
                }
            }
        } else {
            vacancyItem.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_VACANCY, objectID)));
        }

        return vacancyItem;
    }

    //vacancy form system fields
    private void convertFormCustomFields(VacancyItem item, FormProperty[] fields, CompanyCustomFieldItem companyCustomFieldItem, Integer formItemId) {
        if (companyCustomFieldItem != null) {
            for (FormProperty formProperty1 : fields) {
                if (formProperty1 != null) {
                    if (companyCustomFieldItem.getAliasName().equals(formProperty1.getAliasName())) {
                        switch (formProperty1.getCode()) {
                            case "contractPeriod":
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setContractPeriod(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                                break;
                            case "vacancyNumberID":
                                if (companyCustomFieldItem.getUiType().equals(formProperty1.getWidget()) && DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType()) && companyCustomFieldItem.getFieldStringValue() != null) {
                                    EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                                    NumberData numberData = null;
                                    if (companyCustomFieldItem.getFieldStringValue() != null) {
                                        if (settings != null && settings.getEmployeeNumberingFormat() != null) {
                                            numberData = settings.parseNumberData(new BigDecimal(companyCustomFieldItem.getFieldStringValue()).intValue(), settings.getEmployeeNumberingFormat());
                                        } else {
                                            numberData = EdsNumberingSettings.getDefaultData(new BigDecimal(companyCustomFieldItem.getFieldStringValue()).intValue(), EdsNumberingSettings.DEF_VACANCY_PREFIX);
                                        }
                                    }
                                    item.setNumberData(numberData);
                                }
                            case "vacancyManager":
                                if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                                    item.setManager(companyCustomFieldItem.getItem());
                                }
                                break;

                            case "position":
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.POSITION.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getItem() != null) {
                                    item.setPositionItem(new PositionItem(companyCustomFieldItem.getItem().getId(), companyCustomFieldItem.getItem().getName()));
                                }
                                break;

                            case "vacancyLocation":
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.LOCATION.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getItem() != null) {
                                    item.setLocation(companyCustomFieldItem.getItem());
                                }
                                break;
                            case "vacancyJobTitle":
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setJobTitle(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                                break;
                            case "PROJECT":
                                if ((UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) && companyCustomFieldItem.getSelectedId() != null && CustomFieldLookUpTypeEnum.PROJECT.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                    item.setProjectId(companyCustomFieldItem.getSelectedId());
                                }
                                break;

                            case "gender":
                                if (!ServerUtils.isNullOrEmpty(companyCustomFieldItem.getFieldStringValue())) {
                                    item.setGender(companyCustomFieldItem.getFieldStringValue());
                                }
                                break;
                            case "proposedSalary":
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setProposedSalary(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                                break;
                            case "jobRequirement":
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setJobRequirements(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                                break;
                            case "vacancyType":
                                if ((UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) && companyCustomFieldItem.getItem() != null) {
                                    item.setVacancyType(companyCustomFieldItem.getItem().getId());
                                    item.setVacancyTypeName(companyCustomFieldItem.getItem().getName());
                                }
                                break;

                            case "vacancyDescription":
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setDescription(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                                break;
                            case "vacancyStartDate":
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setStartDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue().getDate() : null);
                                }
                                break;
                            case "vacancyEndDate":
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setEndDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue().getDate() : null);
                                }
                                break;
                            case "vacancyStatus":
                                if (companyCustomFieldItem.getUiType().equals(UI_TYPE_LOOKUP) && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getSelectedId() != null && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                    EdsReference reference = referenceManager.get(companyCustomFieldItem.getSelectedId());
                                    if (reference != null) {
                                        item.setStatus(reference.getRPC());
                                    }
                                }
                                break;
                            case "vacancyPlaceCount":
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setVacantPlaces(companyCustomFieldItem.getFieldStringValue() != null ? Integer.valueOf(companyCustomFieldItem.getFieldStringValue()) : null);
                                }
                                break;
                            case "vacancyJobType":
                                if (companyCustomFieldItem.getUiType().equals(UI_TYPE_LOOKUP) && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getSelectedId() != null && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                    EdsReference reference = referenceManager.get(companyCustomFieldItem.getSelectedId());
                                    if (reference != null) {
                                        item.setJobType(reference.getAsSelectItem());
                                    }
                                }
                                break;
                            case "vacancyJobFamily":
                                if ((companyCustomFieldItem.getUiType().equals(UI_TYPE_LOOKUP) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) && companyCustomFieldItem.getSelectedId() != null) {
//                                    item.setJobFamilies(companyCustomFieldItem.getItem());
                                }
                                break;
                            case "vacancyResponsibilities":
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setResponsibility(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                                break;
                            case "vacancyRequiredDegree":
                                EdsReference reference = referenceManager.get(companyCustomFieldItem.getSelectedId());
                                if (reference != null) {
                                    item.setRequiredDegree(reference.getRPC());
                                }
                                break;
                            case "VACANCY_ATTACHMENTS":
                                ArrayList<FileResource> fileResources = (ArrayList<FileResource>) attachmentUtilsManager.getAttachments(F_CUSTOM_FIELD_ITEM, Double.valueOf(companyCustomFieldItem.getFieldStringValue()).intValue(), companyCustomFieldItem.getObjectId());

                                item.setConvertedFileResources(fileResources.toArray(new FileResource[]{}));

                                break;
                            case "VACANCY_NOTES":
                                if (UI_TYPE_COMMITBOX.equals(companyCustomFieldItem.getUiType())) {
                                    ArrayList<HistoryListItem> cFnotes = allInOneService.getCFCommitBoxNotes(companyCustomFieldItem, formItemId);
                                    cFnotes.forEach(note -> note.setObjectID(null));
                                    item.setVacancyNotes(cFnotes);
//                                    item.setPassportIssueItem(new SelectItem(companyCustomFieldItem.getSelectedId(), companyCustomFieldItem.getFieldStringValue()));
                                }
                                break;
                        }
                    }
                }
            }

            if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                for (CompanyCustomFieldItem empCustomField : item.getCustomFieldItems()) {
                    if (companyCustomFieldItem.getAliasName().equals(empCustomField.getAliasName()) && companyCustomFieldItem.getUiType().equals(empCustomField.getUiType()) && companyCustomFieldItem.getDataType().equals(empCustomField.getDataType())) {
                        if (UI_TYPE_LOOKUP.equals(empCustomField.getUiType())) {
                            if (empCustomField.getLookUpTypeEnum().equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                empCustomField.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                                empCustomField.setSelectedId(companyCustomFieldItem.getSelectedId());
                                empCustomField.setItem(companyCustomFieldItem.getItem());
                            }
                        } else {
                            empCustomField.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                            empCustomField.setSelectedId(companyCustomFieldItem.getSelectedId());
                            empCustomField.setItem(companyCustomFieldItem.getItem());
                            empCustomField.setFieldDateNonConvertedValue(companyCustomFieldItem.getFieldDateNonConvertedValue());
                        }
                    }
                }
            }
        }
    }


    @Override
    @Transactional
    public Integer saveCandidate(ContactListItem item) {
        item.setContactType(ContactListItem.CANDIDATE);
        return contactServiceLocal.saveContact(item, null, userManager.getUser(), false, false);
    }

    @Transactional
    public Integer saveVacancy(VacancyItem item) {

        EdsVacancy vacancy = new EdsVacancy();
        boolean isNew = true;
        if (item.getObjectID() != null) {
            vacancy = vacancyManager.get(item.getObjectID());
            isNew = false;
        }
        vacancy.setIntNumber(item.getNumberData().getIntNumber());
        vacancy.setVacancyNumber(item.getNumberData().getNumberString());

        if (item.getManager() != null) {
            vacancy.setManager(userManager.get(item.getManager().getId()));
        }

        if (item.getProjectId() != null) {
            vacancy.setProject(projectManager.get(item.getProjectId()));
        }
        vacancy.setGender(item.getGender());
        vacancy.setProposedSalary(item.getProposedSalary());
        vacancy.setJobrequirements(item.getJobRequirements());
        vacancy.setJobRequirementsLocalize(WfmJsonUtils.objectConvertToJsonString(item.getJobRequirementLocalize()));
        vacancy.setContractFrom(item.getContractFrom());
        vacancy.setContractTo(item.getContractTo());

        if (item.getVacancyType() != null) {
            vacancy.setVacancyTypeId(item.getVacancyType());
        }

        if (item.getVacancyTypeName() != null) {
            vacancy.setVacancyTypeName(item.getVacancyTypeName());
        }

        if (item.getObjectID() == null) {
            vacancy.setCreatedBy(userManager.getUser().getName());
            vacancy.setCreationTime(new Date());
        }

        vacancy.setModifiedBy(userManager.getUser().getName());
        vacancy.setLastUpdatedTime(new Date());


        if (item.getPositionItem() != null) {
            vacancy.setPosition(positionManager.get(item.getPositionItem().getObjectID()));
        }
        if (item.getLocationItem() != null && item.getLocationItem().getId() != null) {
            vacancy.setLocation(locationManager.get(Integer.valueOf(item.getLocationItem().getId())));
        }
        if (item.getJobType() != null) {
            vacancy.setFullPartTime(item.getJobType().getId());
        } else {
            vacancy.setFullPartTime(null);

        }
        if (item.getJobfamily() != null) {
            vacancy.setJobFamily(item.getJobfamily().getId());
        } else {
            vacancy.setJobFamily(null);
        }

        if (item.getDepartment() != null) {
            vacancy.setDepartment(departmentManager.get(item.getDepartment().getId()));
        } else {
            vacancy.setDepartment(null);
        }

        if (item.getCurrency() != null) {
            vacancy.setCurrency(currencyManager.get(item.getCurrency().getId()));
        }
        vacancy.setJobTitle(item.getJobTitle());
        vacancy.setJobTitleLocalize(WfmJsonUtils.objectConvertToJsonString(item.getJobTitleLocalize()));
        vacancy.setDescription(item.getDescription());
        vacancy.setDescriptionLocalize(WfmJsonUtils.objectConvertToJsonString(item.getDescriptionLocalize()));
        vacancy.setStartDate(item.getStartDate());
        vacancy.setEndDate(item.getEndDate());
        EdsReference status = null;
        if (item.getStatus() != null) {
            status = referenceManager.get(item.getStatus().getObjectID());
            vacancy.setStatus(status);
        }
        vacancy.setVacantPlaces(item.getVacantPlaces());
        vacancy.setResponsibility(item.getResponsibility());
        vacancy.setResponsibilityLocalize(WfmJsonUtils.objectConvertToJsonString(item.getResponsibilitiesLocalize()));

        if (item.getRequiredDegree() != null && item.getRequiredDegree().getObjectID() != null) {
            vacancy.setRequiredDegree(referenceManager.get(item.getRequiredDegree().getObjectID()));
        }

        if (checkReferenceLocale(item.getReferenceLocale())) {
            EdsReferenceLocale edsReferenceLocale = allInOneServiceLocal.saveEntityLocale(item.getReferenceLocale());
            vacancy.setLocale(edsReferenceLocale);
        }
        vacancyManager.createOrUpdate(vacancy);
        ArrayList<Integer> newList = new ArrayList<>();
        if (item.getSpokenLanguages() != null && item.getSpokenLanguages().size() > 0) {
            for (SpokenLanguageItem languageItem : item.getSpokenLanguages()) {
                if (languageItem.getLanguage() != null && languageItem.getLanguage().getId() != null && languageItem.getLevel() != null && languageItem.getLevel().getId() != null) {
                    EdsSpokenLanguages language = spokenLanguagesManager.getByRelation(vacancy.getObjectID(), EdsSpokenLanguages.TYPE_VACANCY, languageItem.getLanguage().getId());
                    if (language == null) {
                        language = new EdsSpokenLanguages();
                        language.setEntityType(EdsSpokenLanguages.TYPE_VACANCY);
                        language.setEntityId(vacancy.getObjectID());
                        language.setLanguage(referenceManager.get(languageItem.getLanguage().getId()));
                    }
                    language.setLevel(referenceManager.get(languageItem.getLevel().getId()));
                    spokenLanguagesManager.createOrUpdate(language);
                    newList.add(languageItem.getLanguage().getId());
                }
            }
        }
        //Delete old language entities
        if (!newList.isEmpty()) {
            spokenLanguagesManager.removedLanguages(vacancy.getObjectID(), EdsSpokenLanguages.TYPE_VACANCY, newList);
        }

        Map<String, ArrayList<CustomTableRpc>> customTableItems = item.getCustomTableItems();

        for (Map.Entry<String, ArrayList<CustomTableRpc>> map : customTableItems.entrySet()) {
            List<CustomTableRpc> values = map.getValue();
            if (vacancy.getObjectID() != null) {
                for (CustomTableRpc customTableRpc : values) {
                    List<EdsVacancyItemTable> oldValuesVacancy = vacancyItemTableManager.findByUuid(vacancy.getObjectID(), customTableRpc.getUuid());

                    if (oldValuesVacancy != null && oldValuesVacancy.size() > 0) {
                        for (EdsVacancyItemTable itemTable : oldValuesVacancy) {
                            vacancyItemTableManager.delete(itemTable);
                        }
                    }
                }
            }

            for (CustomTableRpc rpc : values) {
                EdsVacancyItemTable customItemTable = new EdsVacancyItemTable();
                customItemTable.setUuid(map.getKey());
                customItemTable.setName(rpc.getItemName());
                customItemTable.setDescription(rpc.getDescription());
                customItemTable.setCustomFields(saveVacancyCustomItemTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()));
                customItemTable.setVacancy(vacancy);
                if (saveVacancyCustomItemTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()) != null) {
                    vacancyItemTableManager.createOrUpdate(customItemTable);
                }
            }
        }
        EdsVacancyCustomFields edsVacancyCustomFields = createVacancyCustomFields(item.getCustomFieldItems());
        vacancy.setVacancyCustomFields(edsVacancyCustomFields);
        if (!isNew) {
            vacancyCustomQuestionsManager.deleteQuestionsByVacanyId(item.getObjectID());
        }
        mapCustomQuestionsToVacancy(item, vacancy);

        vacancyManager.createOrUpdate(vacancy);


        if (!VACANCY_APPROVAL_STATUS_DRAFT.equals(item.getApprovalStatusCode()) && isNew) {
            createVacancyHistory(vacancy.getObjectID(), new HistoryListItem("created"));
        }

        ArrayList<HistoryListItem> vacancyNotes = item.getVacancyNotes();
        if (vacancy.getObjectID() != null && vacancyNotes != null && vacancyNotes.size() > 0) {
            for (HistoryListItem vacancyNote : vacancyNotes) {
                if (vacancyNote != null && vacancyNote.isNew()) {
                    vacancyNote.setSubject("");
                    vacancyNote.setRelatedId(vacancy.getObjectID());
                    vacancyNote.setRelatedToId(EdsNoteHistory.VACANCY);
                    bugReportService.addNote(vacancyNote);
                }
            }
        }

        if (item.getAttachments() != null && item.getAttachments().length > 0) {
            attachmentUtilsManager.saveAttachments(F_VACANCY, vacancy.getObjectID(), vacancy.getObjectID(), item.getAttachments());
        }

        if (isOk(item.getApprovers())) {
            saveVacancyApprovers(vacancy, item.getApprovers(), item.getApprovalStatusCode());
        }

        if (isOk(item.getApprovers())) {
            updateVacancyStatus(vacancy, item.getApprovalStatusCode(), "");
        }

        try {
            vacancySolrComponent.index(vacancy);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
        if (item.isRelationChanged()) {
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_VACANCY, vacancy.getObjectID(), vacancy.getName(), item.getRelations());
        }
        EdsCompany company = userManager.getUser().getCompany();
        if (company.isHasTelegramBot() != null && company.isHasTelegramBot()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            VacancyItemForTelegram request = new VacancyItemForTelegram(company.getObjectID(), vacancy.getName(), status != null ? status.getName() : "",
                    status != null ? vacancy.getDescription() : "", vacancy.getVacancyNumber());
            HttpEntity<VacancyItemForTelegram> httpRequest = new HttpEntity<>(request, httpHeaders);
            restTemplate.postForObject("https://kpitgbot.kpi.com/api/v1/company/vacancy/save", httpRequest, VacancyItemForTelegram.class);
        }

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsVacancy.class.getSimpleName());
        if (vacancy.getObjectID() != null) {
            kpiLog.setEntityId(vacancy.getObjectID());
        }
        if (isNew) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add vacancy");
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, vacancy, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_VACANCY);
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(log, kpiLog, "Update vacancy");
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, vacancy, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_VACANCY);
        }
        return vacancy.getObjectID();
    }

    private void mapCustomQuestionsToVacancy(VacancyItem item, EdsVacancy vacancy) {
        vacancy.getCustomQuestions().clear();

        VacancyQuestionTableItem[] quItems = item.getVacancyQiestionItems();
        if (quItems == null || quItems.length == 0) return;

        List<Integer> fieldIds = Arrays.stream(quItems)
                .map(VacancyQuestionTableItem::getFieldId)
                .filter(Objects::nonNull)
                .toList();

        List<Integer> referenceIds = Arrays.stream(quItems)
                .map(VacancyQuestionTableItem::getQuestionReferenceId)
                .filter(Objects::nonNull)
                .toList();

        Map<Integer, EdsReference> referenceMap = Optional.ofNullable(referenceManager.getRefernceByIds(referenceIds))
                .orElse(Collections.emptyMap());
        Map<Integer, EdsCompanyCustomFieldsSettings> fieldsMap = Optional.ofNullable(companyCFSettingsManager.getCFByIdsForHrBot(fieldIds))
                .orElse(Collections.emptyMap());

        for (VacancyQuestionTableItem qItem : quItems) {
            EdsCompanyCustomFieldsSettings field = fieldsMap.get(qItem.getFieldId());
            EdsReference questionReference = referenceMap.get(qItem.getQuestionReferenceId());

            if (field == null || questionReference == null) {
                log.warn("Missing field or reference for fieldId={} or referenceId={}", qItem.getFieldId(), qItem.getQuestionReferenceId());
                continue;
            }

            EdsVacancyQuestion question = new EdsVacancyQuestion();
            question.setVacancy(vacancy);
            question.setUiType(field.getUiType());
            question.setFieldId(field.getObjectID());
            question.setFieldName(field.getFieldName());
            question.setColumnCode(field.getColumnCode());
            question.setQuestionReference(questionReference);
            question.setLookUpReference(field.getEdsReference());

            vacancy.getCustomQuestions().add(question);
        }
    }

    private boolean checkReferenceLocale(ReferenceLocale referenceLocale) {
        return referenceLocale != null && (referenceLocale.getUzbek() != null ||
                referenceLocale.getRussian() != null ||
                referenceLocale.getEnglish() != null ||
                referenceLocale.getArabic() != null);
    }

    private void saveVacancyApprovers(EdsVacancy edsApprovable, List<ApproverItemMini> approvers, String statusCode) {
        approvers.sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
        boolean isFirstApprover = true;
        for (ApproverItemMini approverItem : approvers) {
            EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
            if (approverItem.getObjectID() != null) {
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                    _edsApprover.setExactEmployee(user_);
                }
                approverManager.update(_edsApprover);
                if (edsApprovable.getCurrentApprover() != null && statusCode != null && isFirstApprover) {
                    edsApprovable.getCurrentApprover().setStatus(referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, statusCode));
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, Constants.VACANCY_APPROVAL_STATUS_SUBMITTED));
                    isFirstApprover = false;
                } else if (edsApprovable.getCurrentApprover() != null && statusCode != null) {
                    edsApprovable.getCurrentApprover().setStatus(referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, Constants.VACANCY_APPROVAL_STATUS_SUBMITTED));
                }
                if (statusCode != null && !Constants.VACANCY_APPROVAL_STATUS_APPROVED.equals(statusCode)) {
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, statusCode));
                }
                if (edsApprovable.isCurrentApproverRejected()) {
                    edsApprovable.setEntityStatus(edsApprovable.getCurrentApprover().getStatus());
                }
                continue;
            }
            EdsApprover edsApprover = _edsApprover.cloneShallow();
            edsApprover.setObjectID(null);
            edsApprover.setApproverHistory(new HashSet<>());
            edsApprover.setEntityID(edsApprovable.getObjectID());
            edsApprover.setIs_default(false);
            if (statusCode != null && isFirstApprover) {
                edsApprover.setStatus(referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, statusCode));
                if (Constants.VACANCY_APPROVAL_STATUS_DRAFT.equals(statusCode)) {
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, statusCode));
                } else {
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, Constants.VACANCY_APPROVAL_STATUS_SUBMITTED));
                }
                isFirstApprover = false;
            } else if (statusCode != null) {
                edsApprover.setStatus(referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, Constants.VACANCY_APPROVAL_STATUS_SUBMITTED));
            }
            if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                edsApprover.setExactEmployee(user_);
            }
            edsApprover.setApproverRoles(new HashSet<>());
            edsApprover.setApproverEmployees(new HashSet<>());
            edsApprover.setDynamicQueries(new HashSet<>());
            approverManager.createOrUpdate(edsApprover);

            for (EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                EdsApproverRoles roles = new EdsApproverRoles();
                roles.setApproverId(edsApprover.getObjectID());
                roles.setRoleId(roleapp.getRoleId());
                roles.setApproveForAll(roleapp.getApproveForAll());
                edsApprover.getApproverRoles().add(roles);
            }
            for (EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                EdsApproverEmployees employees = new EdsApproverEmployees();
                employees.setApproveForAll(ucerapp.getApproveForAll());
                employees.setEmployeeId(ucerapp.getEmployeeId());
                employees.setApproverId(edsApprover.getObjectID());
                edsApprover.getApproverEmployees().add(employees);
            }
            if (edsApprovable.getCurrentApprover() == null) {
                edsApprovable.setCurrentApprover(edsApprover);
            }
            edsApprovable.getApprovers().add(edsApprover);
        }
    }

    @Override
    public void saveVacancyEditCellValue(VacancyItem rowValue, String columnCodeName) {
        try {
            EdsVacancy vacancy = vacancyManager.get(rowValue.getObjectID());

            if (VacancyItem.VACANCY_JOB_TITLE.equals(columnCodeName)) {
                vacancy.setJobTitle(rowValue.getJobTitle());
            } else if (VacancyItem.VACANCY_START_DATE.equals(columnCodeName)) {
                vacancy.setStartDate(rowValue.getStartDate());
            } else if (VacancyItem.VACANCY_END_DATE.equals(columnCodeName)) {
                vacancy.setEndDate(rowValue.getEndDate());
            } else if (VacancyItem.VACANCY_STATUS.equals(columnCodeName) && rowValue.getStatus() != null) {
                EdsReference reference = referenceManager.get(rowValue.getStatus());
                vacancy.setStatus(reference);
            } else if (VacancyItem.VACANCY_MANAGER.equals(columnCodeName) && rowValue.getManager() != null) {
                EdsUser manager = userManager.get(rowValue.getManager());
                vacancy.setManager(manager);
            } else if (VacancyItem.VACANCY_POSITION.equals(columnCodeName) && rowValue.getPositionItem() != null) {
                EdsPosition position = positionManager.get(rowValue.getPositionItem().getId());
                vacancy.setPosition(position);
            } else if (VacancyItem.VACANCY_GENDER.equals(columnCodeName)) {
                vacancy.setGender(rowValue.getGender());
            } else if (VacancyItem.VACANCY_PROPOSED_SALARY.equals(columnCodeName)) {
                vacancy.setProposedSalary(rowValue.getProposedSalary());
            } else if (VacancyItem.VACANCY_JOB_REQUIREMENT.equals(columnCodeName)) {
                vacancy.setJobrequirements(rowValue.getJobRequirements());
            } else if (VacancyItem.VACANCY_CONTRACT_FROM.equals(columnCodeName)) {
                vacancy.setContractFrom(rowValue.getContractFrom());
            } else if (VacancyItem.VACANCY_CONTRACT_TO.equals(columnCodeName)) {
                vacancy.setContractTo(rowValue.getContractTo());
            } else if (VacancyItem.VACANCY_TYPE.equals(columnCodeName)) {
                vacancy.setVacancyTypeId(rowValue.getVacancyType());
            } else if (VacancyItem.VACANCY_TYPE_NAME.equals(columnCodeName)) {
                vacancy.setVacancyTypeName(rowValue.getVacancyTypeName());
            } else if (VacancyItem.VACANCY_JOB_TYPE.equals(columnCodeName)) {
                vacancy.setFullPartTime(rowValue.getJobType().getId());
            } else if (VacancyItem.VACANCY_JOB_FAMILY.equals(columnCodeName)) {
                vacancy.setJobFamily(rowValue.getJobfamily().getId());
            } else if (VacancyItem.VACANCY_REQUIRED_DEGREE.equals(columnCodeName)) {
                EdsReference edsReference = referenceManager.get(rowValue.getRequiredDegree());
                vacancy.setRequiredDegree(edsReference);
            } else if (VacancyItem.PROJECT.equals(columnCodeName)) {
                EdsProject edsProject = projectManager.get(rowValue.getProjectId());
                vacancy.setProject(edsProject);
            } else {
                EdsVacancyCustomFields vacancyCF = vacancy.getVacancyCustomFields();
                if (vacancyCF == null) {
                    vacancyCF = new EdsVacancyCustomFields();
                    vacancyCFManager.create(vacancyCF);
                    vacancy.setVacancyCustomFields(vacancyCF);
                }
                CustomFieldsUtils.setDomenObjectFieldChange(vacancyCF, rowValue.getCustomFieldValuesItems(), columnCodeName);
            }
            if (isOk(vacancy.getApprovers())) {
                saveVacancyApprovers(vacancy, vacancy.getRPC().getApprovers(), Constants.VACANCY_APPROVAL_STATUS_SUBMITTED);
            }
            vacancyManager.update(vacancy);

            try {
                vacancySolrComponent.index(vacancy);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, vacancy, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_VACANCY);
        } catch (Exception e) {
            System.out.println("Vacancy Edit Cell Column Code :" + columnCodeName);
        }
    }

    private EdsVacancyCustomFields createVacancyCustomFields(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsVacancyCustomFields edsVacancyCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                edsVacancyCustomFields = (EdsVacancyCustomFields) vacancyManager.get(EdsVacancyCustomFields.class, customFieldItems.get(0).getObjectId());
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
                edsVacancyCustomFields = new EdsVacancyCustomFields();
                vacancyManager.createObject(edsVacancyCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsVacancyCustomFields, customFieldItems);
            return edsVacancyCustomFields;
        }
        return null;
    }

    @Transactional
    public ListResult<VacancyItem> getVacancyList(ListingFilterParameter filterParameter) {
//        SolrClient server = WfmJpaTemplate.getSolrServerForCore(Constants.SOLR_VACANCY_CORE);
//        QueryResponse resp = null;
//        try {
//            resp = server.query(getSolrQueryForVacancy(filterParameter), SolrRequest.METHOD.POST);
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
        Page<VacancySolrDoc> vacancySolrDocPage = vacancySolrComponent.getList(filterParameter);

        ListResult<VacancyItem> result = getVacancyFromSolrResult(vacancySolrDocPage, filterParameter);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsVacancy.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get vacancies list");

        return result;
    }

    private ListResult<VacancyItem> getVacancyFromSolrResult(Page<VacancySolrDoc> vacancySolrDocPage, ListingFilterParameter filterParameter) {
        ListPanelToolRpc panelSettings = filterParameter.getListPanelTool();
        ArrayList<VacancyItem> vacancyItemList = new ArrayList<>();
        List<Integer> vacancyIds = vacancySolrDocPage.getContent().stream().map(VacancySolrDoc::getVacancyId).collect(Collectors.toList());
        List<Integer> existingVacancyIDs = vacancyManager.getVacancyIdsForSolr(vacancyIds);
        SelectItem[] vacancyStatus = commonService.convertReference2SelectItem(EdsVacancy.VACANCY_STATUSES, true, null);
        SelectItem[] vacancyTypes = commonService.convertReference2SelectItem(EdsVacancy.VACANCY_TYPE, true, null);

        for (VacancySolrDoc solrDoc : vacancySolrDocPage.getContent()) {
            VacancyItem vacancyItem = new VacancyItem();
            Integer vacancyId = solrDoc.getVacancyId();
            if (existingVacancyIDs.contains(vacancyId)) {
                vacancyItem.setObjectID(vacancyId);
                vacancyItem.setNumberData(new NumberData(solrDoc.getVacancyNumber(), -1));
                vacancyItem.setJobTitle(solrDoc.getJobTitle());
                vacancyItem.setStartDate(solrDoc.getStartDate());
                vacancyItem.setEndDate(solrDoc.getEndDate());
                vacancyItem.setStatus(solrDoc.getVacancyStatus() != null ? new ReferenceItem(solrDoc.getVacancyStatusId(),
                        referenceWfmMessageSource.localize(solrDoc.getVacancyStatusCode(), solrDoc.getVacancyStatus())) : null);
                EdsReference statusNameDef = referenceManager.get(solrDoc.getVacancyStatusId());
                if (statusNameDef != null && statusNameDef.getLocale() != null) {
                    String localeByCode = statusNameDef.getLocale().getLocaleByCode(ServerUtils.getUserLocale().getLanguage());
                    if (localeByCode != null && !localeByCode.equals("")) {
                        vacancyItem.getStatus().setName(localeByCode);
                    } else {
                        vacancyItem.getStatus().setName(statusNameDef.getName());
                    }
                }
                vacancyItem.setProjectId(solrDoc.getProjectId());
                vacancyItem.setProjectName(solrDoc.getProjectName());
//                vacancyItem.setCountryId(solrDoc.getCountryId());
//                vacancyItem.setCountryName(solrDoc.getCountryName());
//                vacancyItem.setEmbassyId(solrDoc.getEmbassyId());
//                vacancyItem.setEmbassyName(solrDoc.getEmbassyName());
                vacancyItem.setGender(solrDoc.getGender());
                vacancyItem.setProposedSalary(solrDoc.getProposedSalary());
                vacancyItem.setJobRequirements(solrDoc.getJobRequirements());
                vacancyItem.setContractFrom(solrDoc.getContractFrom());
                vacancyItem.setContractTo(solrDoc.getContractTo());
                vacancyItem.setCreatedDate(solrDoc.getCreatedDate());
                vacancyItem.setCreatedBy(solrDoc.getCreatedBy());
                vacancyItem.setModifiedDate(solrDoc.getLastUpdateDate());
                vacancyItem.setModifiedBy(solrDoc.getModifiedBy());


                if (solrDoc.getVacancyType() != null) {
                    for (SelectItem vacancyType : vacancyTypes) {
                        if (vacancyType.getId().equals(solrDoc.getVacancyType())) {
                            vacancyItem.setVacancyTypeName(vacancyType.getName());
                            vacancyItem.setVacancyType(vacancyType.getId());
                        }
                    }
                }

                if (solrDoc.getCurrencyId() != null) {
                    vacancyItem.setCurrency(new SelectItem(solrDoc.getCurrencyId(), solrDoc.getCurrencyName()));
                }

                PositionItem positionItem = new PositionItem();
                positionItem.setObjectID(solrDoc.getPositionId());
                positionItem.setName(solrDoc.getPositionName());
                vacancyItem.setPositionItem(positionItem);
                vacancyItem.setStatuses(vacancyStatus);
                vacancyItem.setJobType(new SelectItem(solrDoc.getJobTypeId(), solrDoc.getJobTypeName()));
                vacancyItem.setJobfamily(new SelectItem(solrDoc.getJobFamilyId(), solrDoc.getJobFamilyName() != null ? solrDoc.getJobFamilyName() : ""));
                vacancyItem.setManager(new SelectItem(solrDoc.getManagerId(), solrDoc.getManagerName()));
                vacancyItem.setLocationItem(new LocationItem((solrDoc.getLocationId() != null ? solrDoc.getLocationId().toString() : null), null, solrDoc.getLocationName()));
                vacancyItem.setRequiredDegree(new ReferenceItem(solrDoc.getRdegreeStatusId(), solrDoc.getRdegreeStatus()));
                vacancyItem.setDepartment(new SelectItem(solrDoc.getDepartmentId(), solrDoc.getDepartmentName() != null ? solrDoc.getDepartmentName() : ""));

                if (panelSettings != null) {
                    HashMap<String, Object> map = CustomFieldsUtils.getBaseSolrDocDynamicFields(solrDoc, panelSettings.getColumnCodeName());
                    vacancyItem.setCustomFieldValuesItems(commonServiceLocal.getLocaledCustomFiledMap(map, panelSettings.getListViewCustomFields()));
                }
                ReferenceItem referenceItem = new ReferenceItem(solrDoc.getApprovalStatusId());
                referenceItem.setCode(referenceWfmMessageSource.localize(solrDoc.getApprovalStatusName() != null ? solrDoc.getApprovalStatusName() : "N/A"));
                vacancyItem.setOverallStatus(referenceItem);
                vacancyItem.setApprover(new SelectItem(solrDoc.getApproverId(), solrDoc.getApproverName() != null ? solrDoc.getApproverName() : ""));

                vacancyItemList.add(vacancyItem);
            }
        }
        return new ListResult<>(vacancyItemList, (int) vacancySolrDocPage.getTotalElements());
    }

    @Override
    public SolrQuery getSolrQueryForVacancy(ListingFilterParameter fp) {
        EdsUser user = vacancyManager.getUser();
        EdsCompany company = user.getCompany();

        FacetFilterRpc vacancyFacetFilter = fp.getFacetFilter();
        if (vacancyFacetFilter != null && !vacancyFacetFilter.isFilterChanges()) {
            vacancyFacetFilter = commonServiceLocal.getUserFacetFilter(vacancyFacetFilter);
        }

        String solrQuery = QueryBuilderForSolr.getVacancySolrQuery(fp, user, company) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(vacancyFacetFilter, company,
                        SolrVacancyRepresenter.FIELD_START_DATE, SolrVacancyRepresenter.FIELD_END_DATE);


        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(fp.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(fp.getLimit() != 0 ? fp.getLimit() : 20));

        if (!fp.isSearchButton()) {
            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
                String lang = ServerUtils.getUserLocale().getLanguage();
                boolean desc = !fp.isAscending();
                if (VacancyItem.VACANCY_ID.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_SORTABLE_VACANCY_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_JOB_TITLE.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_SORTABLE_NAME + lang.toUpperCase(), (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_START_DATE.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_START_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_END_DATE.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_END_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_STATUS.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_SORTABLE_VACANCY_STATUS, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_MANAGER.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_SORTABLE_MANAGER_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_POSITION.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_SORTABLE_POSITION_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_JOB_TYPE.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_SORTABLE_JOB_TYPE_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_JOB_FAMILY.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_SORTABLE_JOB_FAMILY_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_REQUIRED_DEGREE.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_SORTABLE_RDEGREE_STATUS, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_DEPARTMENT.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_SORTABLE_DEPARTMENT_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_MODIFIED_BY.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_SORTABLE_MODIFIED_BY, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                } else if (VacancyItem.VACANCY_CURRENCY.equals(fp.getSortField())) {
                    query.setSort(SolrVacancyRepresenter.FIELD_SORTABLE_CURRENCY, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
                }
            } else {
                query.setSort(SolrVacancyRepresenter.FIELD_LAST_UPDATE_DATE, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

    public void deleteVacancy(Integer vacancyID) {
        if (vacancyID != null) {
            EdsVacancy vacancy = vacancyManager.get(vacancyID);
            if (vacancy != null) {
                vacancy.setDeleted(true);
                vacancy.getCustomQuestions().clear();
                vacancyManager.update(vacancy);
                try {
                    solrManager.removeVacances(vacancyID);
                } catch (SolrServerException | IOException e) {
                }
                EdsCompany company = userManager.getUser().getCompany();
                if (company.isHasTelegramBot() != null && company.isHasTelegramBot()) {
                    restTemplate.delete("https://kpitgbot.kpi.com/api/v1/company/vacancy/delete?companyId=" + company.getObjectID() + "&vacancyNumber=" + vacancy.getVacancyNumber());
                }
                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsVacancy.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.DELETE);
                kpiLog.setEntityId(vacancyID);
                ServerUtils.kpiLog(log, kpiLog, "Delete vacancy");
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, vacancy, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_VACANCY);
            }
        }
    }

    public void changeVacancyStatus(Integer vacancyID, Integer statusID) {
        EdsVacancy edsVacancy = vacancyManager.get(vacancyID);

        if (edsVacancy != null) {
            edsVacancy.setStatus(referenceManager.get(statusID));
            vacancyManager.update(edsVacancy);
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsVacancy, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_VACANCY);
            try {
                vacancySolrComponent.index(edsVacancy);
            } catch (IOException | SolrServerException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteVacancyComment(Integer commentID) {
        EdsVacancyNote vacancyNote = vacancyNoteManager.get(commentID);
        vacancyNoteManager.delete(vacancyNote);
    }

    public Integer createVacancyHistory(Integer vacancyId, HistoryListItem hisItem) {
        if (vacancyId != null && hisItem != null) {
            EdsUser user = userManager.getUser();
            if (user instanceof EdsEmployee) {
                user = userManager.get(user.getObjectID());
            }
            EdsVacancyNote vacancyNote = new EdsVacancyNote();
            vacancyNote.setVacancy(vacancyManager.get(vacancyId));
            vacancyNote.setCreationDate(new Date());
            vacancyNote.setUser(user);
            vacancyNote.setSuperUser(ServerUtils.isSuperUser());
            vacancyNote.setText(hisItem.getComment());

            vacancyNoteManager.create(vacancyNote);
            return vacancyNote.getObjectID();
        }
        return null;
    }

    @Override
    public ListResult<ContactListItem> getNewKanbanCandidates(ListingFilterParameter filterParameter, SelectItem columnMetadata) {
        filterParameter.setColumnMetadataId(columnMetadata.getId());
        filterParameter.setSortField(null);
        filterParameter.setSortDir(1);

        ListLoadConfig config = new ListLoadConfig();
        config.setStart(filterParameter.getStart());
        config.setLimit(filterParameter.getLimit());
        config.setSortField(filterParameter.getSortField());
        config.setSortDir(filterParameter.getSortDir());

        ListResult<ContactListItem> result = getKanbanCandidates(filterParameter, config);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
        kpiLog.setEntityType(CrmConstants.CANDIDATE);
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Candidate Kanban list");
        return result;


    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult getKanbanCandidates(ListingFilterParameter filterParameter, ListLoadConfig config) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getSolrQueryForCandidate(filterParameter, config), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }

        return getKanbanLeadFromSolrResult(resp);


    }


    @Override
    public Integer changeCandidateKanbanOrder(SelectItem columnLayoutData, Integer candidateId, Integer widgetIndex, Integer prevItem, Integer afterItem) {

        if (candidateId != null) {
            EdsCrmContact candidate = crmContactManager.get(candidateId);
            EdsReference edsCandidateStatus = referenceManager.get(columnLayoutData.getId());
            boolean hasStatusChange = this.isValueChanged(candidate.getCandidateStatus(), edsCandidateStatus);
            candidate.setCandidateStatus(edsCandidateStatus);
            if (prevItem != null && afterItem == null) {
                EdsCrmContact potentialCandidate = crmContactManager.getSiblingContactByKanbanOrderAndContactType(prevItem, EdsCrmContact.CANDIDATE,
                        (candidate.getCandidateStatus() != null ? candidate.getCandidateStatus().getCode() : null));
                afterItem = potentialCandidate != null ? potentialCandidate.getObjectID() : null;
            }

            if (columnLayoutData.getCategory() != null) {
                EdsNoteHistory edsNote = new EdsNoteHistory();
                edsNote.setEmployee(userManager.getUser());
                edsNote.setComment(columnLayoutData.getCategory());
                edsNote.setEventDate(new Date());
                edsNote.setRelatedId(candidate.getObjectID());
                edsNote.setRelatedTo(EdsNoteHistory.getRelatedToByEntityType(RelationItem.TYPE_CANDIDATE));
                edsNote.setSuperUser(ServerUtils.isSuperUser());
                noteHistoryManager.createOrUpdate(edsNote);

                candidate.setNote(columnLayoutData.getCategory());
            } else {
                candidate.setNote("");
            }
            if (columnLayoutData.getEntityId() != null) {
                candidate.setSelectedReference(columnLayoutData.getEntityId());
            }
            insertCandidateStatusHistory(candidate, edsCandidateStatus, columnLayoutData.getCategory());

            contactServiceLocal.updateEdsCrmContactAndIndex(candidate, false, crmContactManager.getUser());
            baseEventPostProcessor.registerCustomEvent(KanbanCalculationEventListenerImpl.TYPE, EdsMyUpdate.ADD, candidate, prevItem, afterItem);

            if (hasStatusChange) {
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, candidate, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_CANDIDATE);
            }

        }
        return 0;

    }

    private SolrQuery.ORDER getSolrOrder(boolean desc) {
        return desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc;
    }

    private Set<String> getContactDuplicateNames(String solrQuery, List<Integer> inIDs) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.setStart(0);
        query.setRows(10000);
        QueryResponse resp = null;
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return crmContactManager.getDuplicateNamesSet(SolrUtils.getIdsFromSolrDocument(SolrContactRepresenter.FIELD_CONTACT_ID, resp.getResults()), inIDs);
    }

    private SolrQuery getSolrQueryForCandidate(ListingFilterParameter fp, ListLoadConfig config) {
        FacetFilterRpc candidateFacetFilter = fp.getFacetFilter();
        if (candidateFacetFilter != null) {
            candidateFacetFilter.setUserID(fp.getUserID());
            if (!candidateFacetFilter.isFilterChanges()) {
                candidateFacetFilter = commonServiceLocal.getUserFacetFilter(candidateFacetFilter);
            }
        }
        EdsUser edsUser;
        if (fp.getUserID() != null) {
            edsUser = userManager.get(fp.getUserID());
        } else {
            edsUser = crmContactManager.getUser();
        }
        EdsCompany edsCompany = edsUser.getCompany();
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getCandidateListFacetFilterAssigneeQuery(edsCompany, edsUser, fp, candidateFacetFilter, null));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(candidateFacetFilter, edsCompany, null, null));
        if (fp.isDetectDuplicates()) {
            Set<String> duplicates = getContactDuplicateNames(solrQuery.toString(), fp.getObjectIDs());
            if (duplicates != null && duplicates.size() > 0) {
                StringBuilder duplicateQuery = new StringBuilder();
                boolean isFirst = true;
                boolean found = false;
                for (String duplicate : duplicates) {
                    duplicate = duplicate.trim();
                    if (!"".equals(duplicate)) {
                        if (isFirst) {
                            found = true;
                            duplicateQuery.append(SolrContactRepresenter.FIELD_CONTACT_NAME_L_TEXT_FIELD).append(":(");
                        }
                        duplicateQuery.append(!isFirst ? " OR " : "").append(duplicate).append(" OR ").append(duplicate).append("*");
                        isFirst = false;
                    }
                }
                if (found) {
                    duplicateQuery.append(")");
                }
                if (duplicateQuery.length() > 0) {
                    solrQuery.append(" AND (").append(duplicateQuery).append(")");
                }
            }
        }
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(config.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(config.getLimit()));

        if (!fp.isSearchButton() && !fp.isLookUp()) {
            if (!fp.isDetectDuplicates()) {
                if (config.getSortField() != null && !"".equals(config.getSortField())) {
                    boolean desc = Constants.DESC == config.getSortDir();
                    String solrSortField = SolrContactRepresenter.getSortingField(config.getSortField());
                    if (solrSortField != null) {
                        query.setSort(solrSortField, getSolrOrder(desc));
                    } else {
                        CustomFieldsUtils.setCustomFieldsSortableNameToSolr(config.getSortField(), desc, query, true);
                    }
                } else {
                    query.setSort(SolrContactRepresenter.FIELD_UPDATE_DATE, SolrQuery.ORDER.desc);
                }
            }
        }
        return query;
    }

    private boolean isValueChanged(EdsReference oldValue, EdsReference newValue) {
        if (oldValue == null && newValue == null) {
            return false;
        }
        if (oldValue == null || newValue == null) {
            return true;
        } else {
            return !oldValue.getCode().equals(newValue.getCode());
        }

    }

    public List<HistoryNote> loadVacancyHistory(Integer objectId) {
        List<EdsVacancyNote> historyList = vacancyNoteManager.getComments(objectId);
        if (historyList == null) {
            historyList = new ArrayList<>();
        }

        List<HistoryNote> noteItemsList = new ArrayList<>();
        for (EdsVacancyNote item : historyList) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(item.getText())) {
                HistoryListItem historyListItem = new HistoryListItem();
                historyListItem.setObjectID(item.getObjectID());
                if (item.isSuperUser()) {
                    historyListItem.setEmployee(Constants.defaultSupportName);
                } else {
                    historyListItem.setEmployee(item.getUser().getFullName());
                }
                historyListItem.setEmployeeID(item.getUser().getObjectID());
                if (item.getText().split(":").length > 1 && item.getText().split(":")[0].equals("rejectionReason")) { // For: Rejection Reason
                    historyListItem.setComment(commonLocalizer.localize(PdfLocalizationName.rejectionReason, "Rejection Reason:") + " " + item.getText().split(":")[1]);
                } else {
                    try {
                        historyListItem.setComment(commonLocalizer.localize(item.getText().toLowerCase()));
                    } catch (Exception e) {
                        historyListItem.setComment(item.getText());
                    }
                }
                historyListItem.setEventDate(item.getCreationDate());

                noteItemsList.add(historyListItem);
            }
        }
        return noteItemsList;
    }

    @Override
    public void updateVacancyStatus(final Integer objectId, final String statusCode, final String note) {
        final EdsVacancy vacancy = this.vacancyManager.get(objectId);
        updateVacancyStatus(vacancy, statusCode, note);
    }

    private void updateVacancyStatus(final EdsVacancy vacancy, final String statusCode, final String note) {
        final EdsReference edsStatus = this.referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, statusCode);

        if (!Constants.VACANCY_APPROVAL_STATUS_APPROVED.equals(statusCode)) {
            vacancy.setEntityStatus(edsStatus);
        }
        vacancy.updateStatus(edsStatus);
        this.vacancyManager.update(vacancy);
        try {
            this.solrManager.indexVacancy(vacancy);
        } catch (final IOException | SolrServerException e) {
            e.printStackTrace();
        }
        if (VACANCY_APPROVAL_STATUS_APPROVED.equals(edsStatus.getCode()) || VACANCY_APPROVAL_STATUS_REJECTED.equals(edsStatus.getCode())) {
            createVacancyHistory(vacancy.getObjectID(), new HistoryListItem(edsStatus.getCode().equals(VACANCY_APPROVAL_STATUS_APPROVED) ? "approved" : "rejectionReason:" + note));
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), vacancy, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_VACANCY);
        } else {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_EDIT.name(), vacancy, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_VACANCY);
        }
    }

    public NumberData generateVacancyNumber(Integer vacancyID) {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = vacancyID != null ? vacancyManager.get(vacancyID).getIntNumber() : vacancyManager.getVacancyLastIntNumber();
        if (settings != null && settings.getVacancyNumberingFormat() != null) {
            return settings.parseNumberData(vacancyID != null ? intNumber - 1 : intNumber, settings.getVacancyNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_VACANCY_PREFIX);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NumberData generateCandidateNumber(Integer candidateID) {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = candidateID != null ? crmContactManager.get(candidateID).getNumberInteger() : crmContactManager.getCandidateLastNumber();
        if (settings != null && settings.getCandidateNumberingFormat() != null) {
            return settings.parseNumberData(candidateID != null ? intNumber - 1 : intNumber, settings.getCandidateNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_CANDIDATE_PREFIX);
        }
    }

    @Override
    public ContactListItem getCandidateById(Integer id) {
        ContactListItem item = null;
        EdsCrmContact candidateById = crmContactManager.getCandidateById(id);
        ContactListItem candidate = candidateById.getRPC(null, item);
        return candidate;
    }


    //Candidate SECTION

    //    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashSet<String> getPermissions(Integer candidateID, String context) {
        return rolePermissionServiceLocal.getPermissionList(context, rolePermissionServiceLocal.checkForArtificateRoles(candidateID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ContactListItem> listCandidates(ListingFilterParameter filterParameter) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityType(CrmConstants.CANDIDATE);
        ServerUtils.kpiLog(log, kpiLog, "Get contact list");
        EdsUser user = userManager.getUser();
//        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CONTACT_CORE);
//        QueryResponse resp = null;
//        try {
//            resp = server.query(crmServiceLocal.getSolrQueryForCandidate(filterParameter, user), SolrRequest.METHOD.POST);
//        } catch (SolrServerException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//
//        }
//
//        return getContactFromSolrResult(resp, filterParameter);
        Page<ContactSolrDoc> contactSolrDocPage = contactSolrComponent.getCandidateList(filterParameter, user);
        return getContactFromSolrResult(contactSolrDocPage, filterParameter);

    }

    @Override
    @Transactional
    public void indexCompanyVacancy(SolrReindexRpc solrReindexRpc) {
        ServerSecurityContext.getInstance().setCompanyId(solrReindexRpc.getCompanyId());
//        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindexRpc.getCompanyId()));
        solrDbConsistencyManager.removeInconsistences(solrReindexRpc.getCompanyId(), EdsSolrDbConsistency.VACANCY);
        solrDbConsistencyManager.flushAndClear();

        try {
            if (solrReindexRpc.isAllReindex()) {
                solrManager.removeCompanyVacancy(solrReindexRpc.getCompanyId());
            } else if (solrReindexRpc.getLastUpdateTime() != null) {
                List<Integer> deleteVacancyIds = vacancyManager.getCompanyDeleteVacancyForSolr(solrReindexRpc);
                solrManager.removeVacances(deleteVacancyIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Vacany Index. Company ID : {} , Message : {} ", solrReindexRpc.getCompanyId(), e.getMessage());
        }

        int start = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsVacancy> vacancyList = this.vacancyManager.getVacancyListForSolr(solrReindexRpc, start, limit);
        while (!vacancyList.isEmpty()) {
            try {
                vacancySolrComponent.indexConcurrently(vacancyList);
            } catch (SolrServerException | IOException | InterruptedException e) {
                log.error("Error Vacany Index. Company ID : {} , Message : {} ", solrReindexRpc.getCompanyId(), e.getMessage());
            }
            vacancyManager.flushAndClear();
            start++;
            vacancyList = this.vacancyManager.getVacancyListForSolr(solrReindexRpc, (start * limit), limit);
        }
        vacancyManager.flushAndClear();
    }

    @Override
    @Transactional
    public void indexCompanyEmployeeStep(SolrReindexRpc solrReindexRpc) {
        ServerSecurityContext.getInstance().setCompanyId(solrReindexRpc.getCompanyId());
//        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindexRpc.getCompanyId()));
        solrDbConsistencyManager.removeInconsistences(solrReindexRpc.getCompanyId(), EdsSolrDbConsistency.EMPLOYEE_STEP);
        solrDbConsistencyManager.flushAndClear();

        try {
            if (solrReindexRpc.isAllReindex()) {
                solrManager.removeCompanyEmployeeStep(solrReindexRpc.getCompanyId());
            } else if (solrReindexRpc.getLastUpdateTime() != null) {
                List<Integer> deletedStepIds = stepEmployeeManager.getCompanyDeleteEmployeeStepForSolr(solrReindexRpc);
                solrManager.removeEmployeeSteps(deletedStepIds.toArray(new Integer[]{}));
            }
        } catch (IOException | SolrServerException e) {
            log.error("Error Employee Step Index. Company ID : {} , Message : {} ", solrReindexRpc.getCompanyId(), e.getMessage());
        }

        int start = 0;
        int limit = HIBERNATE_CHUNK_SIZE; // Do not change the limit
        List<EdsStepEmployee> stepEmployeeList = this.stepEmployeeManager.getEmployeeStepListForSolr(solrReindexRpc, start, limit);
        while (!stepEmployeeList.isEmpty()) {
            try {
                employeeStepSolrComponent.indexConcurrently(stepEmployeeList);
            } catch (SolrServerException | IOException | InterruptedException e) {
                log.error("Error Employee Step Index. Company ID : {} , Message : {} ", solrReindexRpc.getCompanyId(), e.getMessage());
            }
            stepEmployeeManager.flushAndClear();
            start++;
            stepEmployeeList = this.stepEmployeeManager.getEmployeeStepListForSolr(solrReindexRpc, (start * limit), limit);
        }
        stepEmployeeManager.flushAndClear();
    }

    private ListResult<ContactListItem> getContactFromSolrResult(Page<ContactSolrDoc> contactSolrDocPage, ListingFilterParameter fp) {
        ArrayList<ContactListItem> contactListItems = new ArrayList<>();
        int totalCount = 0;
        if (contactSolrDocPage != null && contactSolrDocPage.getContent() != null) {
            totalCount = (int) contactSolrDocPage.getTotalElements();
            // adding solr proposed results to map
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            ListPanelToolRpc panelTools = fp.getListPanelTool();
            if (panelTools == null) {
                ArrayList<String> columnCodeName = ContactListItem.defaultCandidateColumnNames;
                panelTools = new ListPanelToolRpc();
                panelTools.setColumnCodeName(columnCodeName);
            }
            fp.setColumnsOfListing(panelTools.getColumnCodeName());
            if (panelTools.isCustomFieldsShown()) {
                fp.setCustomFieldsShown(panelTools.isCustomFieldsShown());
                panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.Candidate));
            }
            List<ContactSolrDoc> dataBaseContactIDs = null;
            dataBaseContactIDs = contactSolrComponent.getDocumentsExistingInBase2(contactSolrDocPage.getContent());


            List<Integer> contactIds = fp.isAsSelectItem() ? contactSolrDocPage.stream()
                    .map(ContactSolrDoc::getContactId).toList() : dataBaseContactIDs.stream()
                    .map(ContactSolrDoc::getContactId).toList();


//            List<CompanyCustomFieldItem> cfResultForFiltering = commonService.getCompanyCustomFieldsForFiltering(ViewName.Candidate, null);
            Map<Integer, EdsCountry> countries = ServerUtils.getListAsMapIntegerAndValue(countryManager.list());
            Map<Integer, Integer> hasPlacement = crmContactManager.getMapIdAndHasPlacement(contactIds);
            for (ContactSolrDoc relevantDoc : (fp.isAsSelectItem() ? contactSolrDocPage.getContent() : dataBaseContactIDs)) {
                contactListItems.add(contactServiceLocal.getRPCFromContactSolrDoc(relevantDoc, fp, null, countries, false, false, null, hasPlacement, null));
            }
        }
        return new ListResult<>(contactListItems, totalCount);
    }

    public String getImageUrl(Integer id) {
        return uploadManager.getFileURL(id);
    }

    public List<SolrDocument> getDocumentsExistingInBase(String core, SolrDocumentList results, String fieldObjectID, String type) {
        List<SolrDocument> documents = new ArrayList<>();
        Map<Integer, SolrDocument> mapDocuments = new HashMap<>();
        if (results != null && results.size() > 0) {
            for (SolrDocument doc : results) {
                documents.add(doc);
                Object objectID = doc.getFieldValue(fieldObjectID);
                mapDocuments.put(objectID instanceof String ? Integer.parseInt((String) objectID) : (Integer) objectID, doc);
            }
        }
        List<Integer> objectIDsFromDatabase = null;
        if (SOLR_CONTACT_CORE.equals(core)) {
            objectIDsFromDatabase = RelationItem.TYPE_LEAD.equals(type) ? crmContactManager.getLeadIDsByIDs(new ArrayList<>(mapDocuments.keySet())) : crmContactManager.getContactIDsByIDs(new ArrayList<>(mapDocuments.keySet()));
        } else if (SOLR_CRM_ACCOUNT_CORE.equals(core)) {
            objectIDsFromDatabase = crmAccountManager.getCrmAccountIDsByIDs(new ArrayList<>(mapDocuments.keySet()));
        } else if (SOLR_EVENT_CORE.equals(core)) {
            objectIDsFromDatabase = eventManager.getEventIDsBySolrIDs(new ArrayList<>(mapDocuments.keySet()));
        }
        if (objectIDsFromDatabase != null && objectIDsFromDatabase.size() > 0) {
            for (Integer objectID : objectIDsFromDatabase) {
                mapDocuments.remove(objectID);
            }
            if (mapDocuments.size() > 0) {
                documents.removeAll(mapDocuments.values());
            }
        }
        return documents;
    }

    private ListResult getKanbanLeadFromSolrResult(QueryResponse resp) {
        int totalCount = 0;
        ArrayList<ContactListItem> candidateListItems = new ArrayList<>();
        EdsUser user = userManager.getUser();
        Map<Integer, String> imgs = new HashMap<>();
        if (resp != null && resp.getResults() != null) {
            totalCount = (int) resp.getResults().getNumFound();
            List<SolrDocument> realSolrDocuments = getDocumentsExistingInBase(SOLR_CONTACT_CORE,
                    resp.getResults(),
                    SolrContactRepresenter.FIELD_CONTACT_ID,
                    RelationItem.TYPE_CANDIDATE);
            Map<Integer, String> candidateNotes = noteHistoryManager.getLastNotesAsMap(EdsNoteHistory.CANDIDATE, SolrUtils.getIdsFromSolrDocument(SolrContactRepresenter.FIELD_CONTACT_ID, realSolrDocuments.toArray(new SolrDocument[]{})));
            List<String> columnCodes = commonServiceLocal.getCFsColumnCodeByUiTypes(ViewName.Candidate, ListUtils.getCFUITypesForKanbanItem());
            candidateListItems.addAll(realSolrDocuments.stream()
                    .map(doc -> {
                        ContactListItem res = contactServiceLocal.getKanbanCandidateFromSolrDoc(doc, user);

                        if (candidateNotes.containsKey(res.getObjectId())) {
                            res.setNote(candidateNotes.get(res.getObjectId()));
                        }
                        Integer assigneeId = res.getLeadAssigneeID();
                        if (imgs.get(assigneeId) != null) {
                            res.setContactImageUrl(imgs.get(assigneeId));
                        } else {
                            EdsUser assignee = userManager.get(assigneeId);
                            if (assigneeId != null && assignee.getPhoto() != null) {
                                String imgUrl = getImageUrl(assignee.getPhoto().getObjectID());
                                imgs.put(assigneeId, imgUrl);
                                res.setContactImageUrl(imgUrl);
                            }
                        }
                        res.setCustomFieldsMap(CustomFieldsUtils.getInSolrCustomFields(doc, columnCodes));
                        return res;
                    })
                    .collect(Collectors.toSet()));

        }
        return new ListResult(candidateListItems, totalCount);

    }


    private List<SolrDocument> getDocumentsExistingInBase(SolrDocumentList results, String fieldObjectID) {
        List<SolrDocument> documents = new ArrayList<>();
        Map<Integer, SolrDocument> mapContactDocuments = new HashMap<>();
        Map<Integer, SolrDocument> mapLeadDocuments = new HashMap<>();
        if (results != null && results.size() > 0) {
            for (SolrDocument doc : results) {
                documents.add(doc);
                Integer contactType = (Integer) doc.getFieldValue(SolrContactRepresenter.FIELD_CONTACT_TYPE);
                if (contactType != null) {
                    if (contactType.equals(CrmConstants.TYPE_LEAD_CONTACT)) {
                        mapLeadDocuments.put((Integer) doc.getFieldValue(fieldObjectID), doc);
                    } else {
                        mapContactDocuments.put((Integer) doc.getFieldValue(fieldObjectID), doc);
                    }
                }
            }
        }
        List<Integer> contactIDsFromDatabase = crmContactManager.getContactIDsByIDs(new ArrayList<>(mapContactDocuments.keySet()));
        List<Integer> leadIDsFromDatabase = crmContactManager.getLeadIDsByIDs(new ArrayList<>(mapLeadDocuments.keySet()));
        if (contactIDsFromDatabase != null && contactIDsFromDatabase.size() > 0) {
            for (Integer objectID : contactIDsFromDatabase) {
                mapContactDocuments.remove(objectID);
            }
            if (mapContactDocuments.size() > 0) {
                documents.removeAll(mapContactDocuments.values());
            }
        }

        if (leadIDsFromDatabase != null && leadIDsFromDatabase.size() > 0) {
            for (Integer objectID : leadIDsFromDatabase) {
                mapLeadDocuments.remove(objectID);
            }
            if (mapLeadDocuments.size() > 0) {
                documents.removeAll(mapLeadDocuments.values());
            }
        }
        return documents;
    }

    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getCandidateList() {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> candidateListItems = new LinkedHashMap<>();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSelectCandidate(true);
        ListResult<ContactListItem> candidateListItemListResult = listCandidates(fp);


        for (ContactListItem candidateListItem : candidateListItemListResult.getList()) {
            if (candidateListItem != null && candidateListItem.getVacancies() != null) {
                KpiTreeInfo sItem = new KpiTreeInfo();
                boolean existing = false;
                sItem.setId(candidateListItem.getObjectId());
                String candidateName = candidateListItem.getName();
                StringBuilder vocansies = new StringBuilder(" (");
                int i = 0;
                for (SelectItem item : candidateListItem.getVacancies()) {
                    vocansies.append(item.getName());
                    if (i != candidateListItem.getVacancies().size() - 1) {
                        vocansies.append(", ");
                    } else {
                        vocansies.append(")");
                    }
                    i++;
                }
                sItem.setName(candidateName + vocansies);
                candidateListItem.getLeadStatus(true);
                sItem.setDepartmentId(0);

                for (KpiTreeInfo s : candidateListItems.keySet()) {
                    if (s.getId().equals(candidateListItem.getLeadStatus(true).getId())) {
                        existing = true;
                        candidateListItems.get(s).add(sItem);
                        break;
                    }
                }

                if (!existing) {
                    KpiTreeInfo statusInfo = new KpiTreeInfo(candidateListItem.getLeadStatus(true).getObjectID(), candidateListItem.getLeadStatus(true).getName());
                    ArrayList<KpiTreeInfo> list = new ArrayList<>();
                    list.add(sItem);
                    candidateListItems.put(statusInfo, list);
                }
            }
        }

        return candidateListItems;
    }

    /**
     * Generate Candidate vacancies
     *
     * @param candidateID - candidate ID
     * @return - vacancies
     */
    public ArrayList<SelectItem> getCandidateVacancies(Integer candidateID) {
        ArrayList<SelectItem> vacanciesList = new ArrayList<>();

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setBriefly(false);
        List<EdsVacancy> vacancyList = vacancyManager.list(fp);
        if (vacancyList != null && vacancyList.size() > 0) {
            for (EdsVacancy cVacancy : vacancyList) {
                vacanciesList.add(cVacancy.getAsSelectItem());
            }
        }

        EdsCrmContact candidate = candidateID != null ? crmContactManager.get(candidateID) : null;
        Set<EdsVacancy> vacancies = candidate != null ? candidate.getVacancies() : null;
        if (vacancies != null && vacancies.size() > 0 && vacanciesList.size() > 0) {
            for (EdsVacancy vacancy : vacancies) {
                SelectItem asSelect = vacancy.getAsSelectItem();
                for (SelectItem s : vacanciesList) {
                    if (s.getId().equals(asSelect.getId())) {
                        s.setSelected(true);
                    }
                }

            }
        }
        return vacanciesList;
    }

    /**
     * Register candidate vacancies
     *
     * @param vacancies   - candidate cacancies
     * @param candidateID - candidate ID
     */
    public void saveCandidateVacancies(ArrayList<SelectItem> vacancies, Integer candidateID) {

        EdsCrmContact candidate = candidateID != null ? crmContactManager.get(candidateID) : null;

        if (candidate != null && vacancies != null && vacancies.size() > 0) {
            contactServiceLocal.saveCandidateVacancies(vacancies, candidate);
            crmContactManager.update(candidate, true);
        }
    }

    /**
     * change Status of entityClass to @statusCode
     *
     * @param classEntity - Class
     * @param entityIDs   - objectID of entities
     * @param parentCode  - parentCode
     * @param statusCode  - EdsReference;
     * @return - permission code
     */
    public String changeStatus(String classEntity, ArrayList<Integer> entityIDs, String parentCode, String statusCode) {
        boolean t = false;
        EdsReference reference = referenceManager.findReference(parentCode, statusCode);
        if (classEntity != null) {
            if (classEntity.equals(HRMS.RECRUITMENT.CANDIDATE)) {
                for (Integer entityID : entityIDs) {
                    if (getPermissions(entityID, PermissionConstants.HRMS_CONTEXT).contains(PermissionConstants.HRMS_ADD_TO_SHORT_LIST)) {
                        EdsCrmContact candidate = crmContactManager.get(entityID);
                        if (candidate != null) {
                            candidate.setLeadStatus(reference);
                            if (EdsCrmContact.CANDIDATE_STATUS_SHORTLIST.equals(statusCode)) {
                                candidate.setShortList(true);
                            }
                            if (EdsCrmContact.CANDIDATE_STATUS_REJECTED.equals(statusCode) || EdsCrmContact.CANDIDATE_STATUS_UNQUALIFIED.equals(statusCode) || EdsCrmContact.CANDIDATE_STATUS_HIRED.equals(statusCode)) {
                                candidate.setShortList(false);
                            }
                            crmContactManager.update(candidate, true);
                            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, candidate, userManager.getUser());
                            workflowEvent.setEntityType(RelationItem.TYPE_CANDIDATE);
                        }
                        t = true;
                    }
                    KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                    kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
                    kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                    kpiLog.setEntityType(CrmConstants.CANDIDATE);
                    kpiLog.setEntityId(entityID);
                    ServerUtils.kpiLog(log, kpiLog, "Change contact status");
                }
            }
        }
        if (t) {
            return PermissionConstants.ALLOW;
        } else {
            return PermissionConstants.DENY;
        }
    }

    /**
     * Register placement item
     *
     * @param placementItem - placementItem
     */
    @Transactional
    public void savePlacement(PlacementItem placementItem, DateNonConvertable hireDate) {
        EdsPlacement placement = new EdsPlacement();
        EdsUser user = userManager.getUser();
        boolean isNew = true;

        if (placementItem.getObjectID() != null) {
            placement = placementManager.get(placementItem.getObjectID());
            isNew = false;
        }
        if (isNew) {
            placement.setCreator(user);
        }
        if (placementItem.getCandidateID() != null) {
            placement.setCandidate(crmContactManager.get(placementItem.getCandidateID()));
        }
        ArrayList<SelectItem> vacancies = placementItem.getVacancies();
        if (vacancies != null && vacancies.size() > 0) {
            placement.getVacancies().clear();
            for (SelectItem vacancy : vacancies) {
                if (vacancy.isSelected()) {
                    EdsVacancy edsVacancy = vacancyManager.get(vacancy.getId());
                    if (edsVacancy != null) {
                        placement.getVacancies().add(edsVacancy);
                    }
                }
            }
        }
        if (placementItem.getDepartmentID() != null) {
            placement.setDepartment(departmentManager.get(placementItem.getDepartmentID()));
        }
        if (placementItem.getLocationID() != null) {
            placement.setLocation(locationManager.get(placementItem.getLocationID()));
        }
        if (placementItem.getPositionID() != null) {
            placement.setPosition(positionManager.get(placementItem.getPositionID()));
        }
        if (placementItem.getProjectID() != null) {
            placement.setProject(projectManager.get(placementItem.getProjectID()));
        } else {
            placement.setProject(null);
        }
        if (placementItem.getStatusID() != null) {
            placement.setStatus(referenceManager.get(placementItem.getStatusID()));
        }
        if (placementItem.getCandidateType() != null) {
            placement.setCandidateType(placementItem.getCandidateType());
        }
        if (placementItem.getGroupPlacementId() != null) {
            placement.setGroupPlacementId(placementItem.getGroupPlacementId());
        }
        if (placementItem.getNumberData() != null) {
            placement.setIntNumber(placementItem.getNumberData().getIntNumber());
            placement.setNumberData(placementItem.getNumberData().getNumberString());
        } else {
            final NumberData numberData = generatePlacementNumber();
            placement.setIntNumber(numberData.getIntNumber());
            placement.setNumberData(numberData.getNumberString());
        }
        placement.setOfferDate(placementItem.getDateOffed());
        if (placement.getObjectID() == null) {
            EdsCrmContact candidate = placement.getCandidate();
            if (candidate != null) {
                if (candidate.getLeadStatus() == null ||
                        !EdsCrmContact.CANDIDATE_STATUS_PLACED.equals(candidate.getLeadStatus().getCode())) {
                    candidate.clear();
                    EdsReference placedCandidateStatus = referenceManager.findReference(EdsCrmContact._CANDIDATE_STATUS, EdsCrmContact.CANDIDATE_STATUS_PLACED);
                    candidate.setLeadStatus(placedCandidateStatus);
                    crmContactManager.update(candidate, true);
                    EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, candidate, userManager.getUser());
                    workflowEvent.setEntityType(RelationItem.TYPE_CANDIDATE);
                }
            }
        }
        EdsPlacementCustomFields edsPlacementCustomFields = createPlacementCustomFields(placementItem.getCustomFieldItems());
        placement.setPlacementCustomFields(edsPlacementCustomFields);

        placementManager.createOrUpdate(placement);

        if (isNew) {
            EdsCrmContact candidate = placement.getCandidate();
            List<FileResource> attachs = attachmentUtilsManager.getAttachments(F_CANDIDATE, candidate.getObjectID(), candidate.getObjectID());
            if (attachs != null && attachs.size() > 0) {
                for (FileResource file : attachs) {
                    attachmentUtilsManager.copyFileWhenConvert(F_PLACEMENT, placement.getObjectID(), file.getObjectId(), placement.getObjectID(), file);
                }
            }
        }

        if (!isOk(placementItem.getApprovers())) {
            placement.setEntityStatus(referenceManager.findReference(Constants.PLACEMENT_STATUS, placementItem.getStatusCode()));
        }

        if (isOk(placementItem.getApprovers())) {
            placementItem.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : placementItem.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (placement.getCurrentApprover() != null && placementItem.getStatusCode() != null && isFirstApprover) {
                        placement.getCurrentApprover().setStatus(referenceManager.findReference(Constants.PLACEMENT_STATUS, placementItem.getStatusCode()));
                        placement.setEntityStatus(referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_SUBMITTED));
                        isFirstApprover = false;
                    } else if (placement.getCurrentApprover() != null && placementItem.getStatusCode() != null) {
                        placement.getCurrentApprover().setStatus(referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_SUBMITTED));
                    }
                    if (placementItem.getStatusCode() != null && !Constants.PLACEMENT_STATUS_APPROVED.equals(placementItem.getStatusCode())) {
                        placement.setEntityStatus(referenceManager.findReference(Constants.PLACEMENT_STATUS, placementItem.getStatusCode()));
                    }
                    if (placement.isCurrentApproverRejected()) {
                        placement.setEntityStatus(placement.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(placement.getObjectID());
                edsApprover.setIs_default(false);

                if (placementItem.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.PLACEMENT_STATUS, placementItem.getStatusCode()));
                    if (Constants.PLACEMENT_STATUS_SAVE_AS_DRAFT.equals(placementItem.getStatusCode())) {
                        placement.setEntityStatus(referenceManager.findReference(Constants.PLACEMENT_STATUS, placementItem.getStatusCode()));
                    } else {
                        placement.setEntityStatus(referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_SUBMITTED));
                    }
                    isFirstApprover = false;
                } else if (placementItem.getStatusCode() != null) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_SUBMITTED));
                }
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                    edsApprover.setExactEmployee(user_);
                }
                edsApprover.setApproverRoles(new HashSet<>());
                edsApprover.setApproverEmployees(new HashSet<>());
                edsApprover.setDynamicQueries(new HashSet<>());
                approverManager.createOrUpdate(edsApprover);

                for (EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                    edsApprover.getApproverRoles().add(roleapp);
                }

                for (EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                    edsApprover.getApproverEmployees().add(ucerapp);
                }

                if (placement.getCurrentApprover() == null) {
                    placement.setCurrentApprover(edsApprover);
                }
                placement.getApprovers().add(edsApprover);
            }
        }

        if (placementItem.getAttachments() != null && placementItem.getAttachments().length > 0) {
            attachmentUtilsManager.saveAttachments(F_PLACEMENT, placement.getObjectID(), placement.getObjectID(), placementItem.getAttachments());
        }
        ArrayList<HistoryListItem> placementNotes = placementItem.getNotes();
        if (placement.getObjectID() != null && placementNotes != null && placementNotes.size() > 0) {
            for (HistoryListItem placementNote : placementNotes) {
                if (placementNote != null && placementNote.isNew()) {
                    placementNote.setSubject("");
                    placementNote.setRelatedId(placement.getObjectID());
                    placementNote.setRelatedToId(EdsNoteHistory.PLACEMENT);
                    bugReportService.addNote(placementNote);
                }
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPlacement.class.getSimpleName());
        if (placement.getObjectID() != null) {
            kpiLog.setEntityId(placement.getObjectID());
        }
        HashMap<String, ArrayList<CustomTableRpc>> customTableItems = placementItem.getCustomTableItems();

        for (Map.Entry<String, ArrayList<CustomTableRpc>> map : customTableItems.entrySet()) {
            List<CustomTableRpc> values = map.getValue();
            if (placement != null && placement.getObjectID() != null) {
                for (CustomTableRpc customTableRpc : values) {
                    List<EdsPlacementItemTable> oldValuesPlacement = placementItemTableManager.findByUuid(placement.getObjectID(), customTableRpc.getUuid());

                    if (oldValuesPlacement != null && oldValuesPlacement.size() > 0) {
                        for (EdsPlacementItemTable itemTable : oldValuesPlacement) {
                            placementItemTableManager.delete(itemTable);
                        }
                    }
                }
            }

            for (CustomTableRpc rpc : values) {
                EdsPlacementItemTable customItemTable = new EdsPlacementItemTable();
                customItemTable.setUuid(map.getKey());
                customItemTable.setName(rpc.getItemName());
                customItemTable.setDescription(rpc.getDescription());
                customItemTable.setCustomFields(savePlacementCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()));
                customItemTable.setPlacement(placement);
                if (savePlacementCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()) != null) {
                    placementItemTableManager.createOrUpdate(customItemTable);
                }
            }
        }
        allInOneServiceLocal.saveRelations(RelationItem.TYPE_PLACEMENT, placement.getObjectID(), placement.getCandidate().getFullName(), placementItem.getRelations());
        EdsBusinessEvent placementWorkflow = null;
        if (isNew) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add new placement");
            placementWorkflow = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, placement, user);
            placementWorkflow.setEntityType(RelationItem.TYPE_PLACEMENT);
            baseEventPostProcessor.registerEvent(PlacementEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, placement, user);
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(log, kpiLog, "Update placement");
            placementWorkflow = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, placement, user);
            placementWorkflow.setEntityType(RelationItem.TYPE_PLACEMENT);
            baseEventPostProcessor.registerEvent(PlacementEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, placement, user);
        }
        if (placement.getOverallStatus() != null && (placement.getOverallStatus().getCode().equals(PLACEMENT_STATUS_APPROVED) || placement.getOverallStatus().getCode().equals(PLACEMENT_STATUS_REJECTED))){
            hrmsServiceLocal.updateCandidateStatusOnApproval(placement.getCandidate().getObjectID(),placement.getOverallStatus().getCode());
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), placement, user);
        workflowEvent.setEntityType(RelationItem.TYPE_PLACEMENT);

    }

    public void savePlacementEditCellValue(PlacementItem rowValue, String columnCodeName) {
        try {
            EdsPlacement placement = placementManager.get(rowValue.getObjectID());
            PlacementItem placementItem = new PlacementItem();
            placementItem.setObjectID(placement.getObjectID());
            if (placement.getCandidate() != null) {
                placementItem.setCandidateID(placement.getCandidate().getObjectID());
                placementItem.setCandidateName(placement.getCandidate().getFullName());
            }
            if (placement.getPosition() != null) {
                placementItem.setPositionID(placement.getPosition().getObjectID());
                placementItem.setPositionName(placement.getPosition().getName());
            }
            if (placement.getStatus() != null) {
                placementItem.setStatusID(placement.getStatus().getObjectID());
                placementItem.setStatusName(referenceWfmMessageSource.localize(placement.getStatus().getCode(), placement.getStatus().getName()));
                placementItem.setStatusCode(placement.getStatus().getCode());

                if (!placement.getStatus().getCode().equals(PLACEMENT_STATUS_HIRED)) {
                    placementItem.setEditable(true);
                }
            }
            if (placement.getOfferDate() != null) {
                placementItem.setDateOffed(placement.getOfferDate());
            }

            if (placement != null) {
                Set<EdsPlacementItemTable> itemTables = placement.getItemTables();

                HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

                if (itemTables != null && itemTables.size() > 0) {

                    for (EdsPlacementItemTable itemTable : itemTables) {
                        CustomTableRpc rpc = itemTable.getRpc();

                        rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                                commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.PlacementItemTable, rpc.getUuid())));

                        map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                    }
                    placementItem.setCustomTableItems(map);
                }
                HashMap<String, ArrayList<CustomTableRpc>> tableItems = placementItem.getCustomTableItems();

                if (tableItems.size() > 0) {
                    for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                        tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
                    }
                }
            }
            if (PlacementItem.PLACEMENT_STATUS_OFFER.equals(columnCodeName)) {
                EdsReference edsReference = referenceManager.get(rowValue.getStatusID());
                placement.setStatus(edsReference);
            } else if (PlacementItem.PLACEMENT_DATE_OFFERED.equals(columnCodeName)) {
                placement.setOfferDate(rowValue.getDateOffed());
            } else if (PlacementItem.PLACEMENT_POSITION_OFFERED.equals(columnCodeName)) {
                EdsPosition edsPosition = positionManager.get(rowValue.getPositionID());
                placement.setPosition(edsPosition);
            } else {
                EdsPlacementCustomFields placemntCF = placement.getPlacementCustomFields();
                if (placemntCF == null) {
                    placemntCF = new EdsPlacementCustomFields();
                    pLacementCFManager.create(placemntCF);
                    placement.setPlacementCustomFields(placemntCF);
                }
                CustomFieldsUtils.setDomenObjectFieldChange(placemntCF, rowValue.getCustomFieldsMap(), columnCodeName);
            }
            placementManager.update(placement);

        } catch (Exception e) {
            System.out.println("Placement Edit Cell Column Code :" + columnCodeName);
        }
    }

    private EdsPlacementCustomFields createPlacementCustomFields(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsPlacementCustomFields edsPlacementCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                edsPlacementCustomFields = (EdsPlacementCustomFields) placementManager.get(EdsPlacementCustomFields.class, customFieldItems.get(0).getObjectId());
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
                edsPlacementCustomFields = new EdsPlacementCustomFields();
                placementManager.createObject(edsPlacementCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsPlacementCustomFields, customFieldItems);
            return edsPlacementCustomFields;
        }
        return null;
    }

    /**
     * Register approved/hired placement
     *
     * @param placementID - placement ID
     * @param approved    - approved status
     */
    public void saveApprovedHiredPlacement(Integer placementID, boolean approved, DateNonConvertable hireDate) {
        if (placementID != null) {
            EdsPlacement placement = placementManager.get(placementID);

            EdsReference status = referenceManager.findReference(Constants.PLACEMENT_STATUS, approved ? Constants.PLACEMENT_STATUS_APPROVED : Constants.PLACEMENT_STATUS_HIRED);
            placement.setStatus(status);
            if (placement.getCandidate() != null) {
                EdsCrmContact candidate = placement.getCandidate();
                candidate.clear();
                if (approved) {
                    if (candidate.getLeadStatus() == null || !EdsCrmContact.CANDIDATE_STATUS_PLACED.equals(candidate.getLeadStatus().getCode())) {
                        candidate.clear();
                        EdsReference placedCandidateStatus = referenceManager.findReference(EdsCrmContact._CANDIDATE_STATUS, EdsCrmContact.CANDIDATE_STATUS_PLACED);
                        candidate.setLeadStatus(placedCandidateStatus);
                        crmContactManager.update(candidate, true);
                    }
                } else {
                    EdsReference hiredCandidateStatus = referenceManager.findReference(EdsCrmContact._CANDIDATE_STATUS, EdsCrmContact.CANDIDATE_STATUS_HIRED);
                    candidate.setLeadStatus(hiredCandidateStatus);
                    crmContactManager.update(candidate, true);
                    //from candidate to new employee create logic
                    NewEmployee newEmployee = new NewEmployee();
                    newEmployee.setHasAccess(false);//the hired employee can't have access to the system at this point
                    newEmployee.setCreatedFrom(FROM_HIRED_PLACEMENT_CANDIDATE);
                    newEmployee.setFname(candidate.getFirstName());
                    newEmployee.setLname(candidate.getLastName());
                    newEmployee.setEmail(candidate.getPrimaryEmail());
                    newEmployee.setMname(candidate.getMiddleName());
                    newEmployee.setGender(candidate.getGender());
                    if (candidate.getMartialStatus() != null) {
                        newEmployee.setMartialStatusId(candidate.getMartialStatus().getObjectID());
                    }
                    newEmployee.setExistingContactID(candidate.getObjectID());
                    List<EdsAddress> addresses = candidate.getAddresses();
                    newEmployee.setHomeAddress(addresses.toString());
                    newEmployee.setIsFromCandidate(true);
                    if (candidate.getPhoto() != null) {
                        newEmployee.setPhotoID(candidate.getPhoto().getObjectID());
                    }

                    //register departmentID
                    if (placement.getDepartment() != null) {
                        newEmployee.setDepartment(placement.getDepartment().getObjectID());
                    }
                    //register position
                    if (placement.getPosition() != null) {
                        newEmployee.setPositionId(placement.getPosition().getObjectID());
                        newEmployee.setPosition(placement.getPosition().getName());
                    }
                    //register location
                    if (placement.getLocation() != null) {
                        newEmployee.setLocationId(placement.getLocation().getObjectID());
                    }
                    //register project
                    if (placement.getProject() != null) {
                        newEmployee.setProjectID(placement.getProject().getObjectID());
                    }
                    if (hireDate != null) {
                        newEmployee.setStartDate(hireDate);
                    }
                    //register new employee from candidate
                    Integer employeeID = employeeServiceLocal.createEmployeeInternal(newEmployee, null);
                    EdsEmployee employee = employeeManager.get(employeeID);
                    ListingFilterParameter fp = new ListingFilterParameter();
                    fp.setContactID(candidate.getObjectID());
                    List<EdsEducation> talentProfileDataByCandidate = educationManager.getTalentProfileDataByCandidate(fp);
                    for (EdsEducation edsEducation : talentProfileDataByCandidate) {
                        edsEducation.setEmployee(employee);
                        educationManager.update(edsEducation);
                    }
                    List<EdsDependent> dependenstByCandidate = dependentManager.getDependenstByCandidate(candidate);
                    for (EdsDependent edsDependent : dependenstByCandidate) {
                        edsDependent.setUser(employee);
                        dependentManager.update(edsDependent);
                    }

                    List<FileResource> attachs = attachmentUtilsManager.getAttachments(F_PLACEMENT, placement.getObjectID(), placement.getObjectID());
//                    List<FileResource> candidateAttachs = attachmentUtilsManager.getAttachments(F_CANDIDATE, candidate.getObjectID(), candidate.getObjectID());
                    if (attachs != null && attachs.size() > 0) {
                        for (FileResource file : attachs) {
//                            if (!existFile(file, candidateAttachs)) {
                            attachmentUtilsManager.copyFileWhenConvert(F_EMPLOYEE_PROFILE, employeeID, file.getObjectId(), employeeID, file);
//                            }
                        }
                    }

                    if (candidate.getAllowances() != null && candidate.getAllowances().size() > 0) {
                        EdsPaymentDeduction newPaymentDeduction;
                        for (EdsPaymentDeduction paymentDeduction : candidate.getAllowances()) {
                            newPaymentDeduction = new EdsPaymentDeduction();
                            newPaymentDeduction.setEmployeeId(employee.getObjectID());
                            newPaymentDeduction.setCategoryId(paymentDeduction.getCategory() != null ? paymentDeduction.getCategory().getObjectID() : null);
                            newPaymentDeduction.setPaymentAmount(paymentDeduction.getPaymentAmount());
                            newPaymentDeduction.setPaymentDate(paymentDeduction.getPaymentDate());
                            newPaymentDeduction.setPayType(paymentDeduction.getPayType());
                            newPaymentDeduction.setRecurring(true);
                            paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                        }
                    }
                    EdsRelation relation = new EdsRelation();
                    relation.setFromType(RelationItem.TYPE_PLACEMENT);
                    relation.setFromID(placementID);
                    relation.setFromName(placement.getCandidate().getFullName());
                    relation.setToType(RelationItem.TYPE_EMPLOYEE);
                    relation.setToID(employeeID);
                    relation.setToName(newEmployee.getFullName());
                    relationManager.create(relation);
                    //workflow for Employee
                    EdsBusinessEvent workflowEvent2 = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, employee, userManager.getUser());
                    workflowEvent2.setEntityType(RelationItem.TYPE_EMPLOYEE);
                }
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, candidate, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_CANDIDATE);
            }
        }
    }

    public void updateStatusPlacement(Integer objectID, String statusCode, String rejectionReason) {
        EdsPlacement edsPlacement = placementManager.get(objectID);

        if (edsPlacement != null) {
            final EdsUser user = this.employeeManager.getUser();
            final EdsReference edsReference = this.referenceManager.findReference(Constants.PLACEMENT_STATUS, statusCode);

            if (Constants.PLACEMENT_STATUS_APPROVED.equals(edsReference.getCode())) {
                edsPlacement.setOverallStatus(edsReference);
            } else if (Constants.PLACEMENT_STATUS_APPROVED.equals(edsReference.getCode()) && edsPlacement.getOverallStatus() != null
                    && Constants.PLACEMENT_STATUS_SAVE_AS_DRAFT.equals(edsPlacement.getOverallStatus().getCode())) {
                edsPlacement.setOverallStatus(this.referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_SUBMITTED));
            }
            edsPlacement.updateStatus(edsReference);
            if (Constants.PLACEMENT_STATUS_REJECTED.equals(statusCode)) {
                edsPlacement.setOverallStatus(this.referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_REJECTED));
            }

            placementManager.update(edsPlacement);
            EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsPlacement, user);
            workflowEvent.setEntityType(RelationItem.TYPE_PLACEMENT);

            if (Constants.PLACEMENT_STATUS_SUBMITTED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(PlacementEventListenerImpl.TYPE, PlacementEventListenerImpl.PLACEMENT_STATUS_SUBMITTED, edsPlacement, user);
            } else if (Constants.PLACEMENT_STATUS_APPROVED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(PlacementEventListenerImpl.TYPE, PlacementEventListenerImpl.PLACEMENT_STATUS_APPROVED, edsPlacement, user);
            } else if (Constants.PLACEMENT_STATUS_REJECTED.equals(statusCode)) {
                this.baseEventPostProcessor.registerEvent(PlacementEventListenerImpl.TYPE, PlacementEventListenerImpl.PLACEMENT_STATUS_REJECTED, edsPlacement, user);
            }

            if (edsPlacement.getOverallStatus() != null && (edsPlacement.getOverallStatus().getCode().equals(PLACEMENT_STATUS_APPROVED) || edsPlacement.getOverallStatus().getCode().equals(PLACEMENT_STATUS_REJECTED))){
                hrmsServiceLocal.updateCandidateStatusOnApproval(edsPlacement.getCandidate().getObjectID(),edsPlacement.getOverallStatus().getCode());
            }
        }
    }

    @Override
    public List<HistoryNote> loadPlacementNoteAndHistory(Integer objectId) {
        if (objectId == null) {
            return new ArrayList<>();
        }

        List<HistoryNote> result = new ArrayList<>(getAllHistory(objectId, PLACEMENT));
        return result;
    }

    public List<MyUpdateItem> getAllHistory(Integer objectID, String viewType) {

        List<EdsMyUpdate> myUpdates = new ArrayList<>();
        String relationType = null;

        if (Constants.PLACEMENT.equals(viewType)) {
            myUpdates = myUpdateManager.getUpdatesForAffectedID(objectID, MyUpdateTypeManager.HRMS_PLACEMENT);
            relationType = RelationItem.TYPE_PLACEMENT;
        }

        ArrayList<MyUpdateItem> result = new ArrayList<>();
        if (myUpdates.size() > 0) {
            EdsUser user = myUpdateTypeManager.getUser();
            for (EdsMyUpdate myUpdate : myUpdates) {

                MyUpdateItem item = new MyUpdateItem();
                item.setType(myUpdate.getEventType());
                item.setEventDate(myUpdate.getDate());
                item.setLink(myUpdateTypeManager.getUpdatesLink(myUpdate));
                myUpdateTypeManager.getMyUpdateMessageAllHistory(myUpdate, item, !user.getObjectID().equals(myUpdate.getReceiver()), true);

                result.add(item);
            }
        }

        if (relationType != null) {
            List<MyUpdateItem> attachmentUpdates = myUpdateManager.getAttachmentUpdates(objectID, relationType);
            if (attachmentUpdates != null && attachmentUpdates.size() > 0) {
                result.addAll(attachmentUpdates);
            }
        }

        result.sort(Comparator.comparing(MyUpdateItem::getEventDate));

        return result;

    }

    private boolean existFile(FileResource file, List<FileResource> candidateAttachs) {
        if (candidateAttachs != null && candidateAttachs.size() > 0) {
            for (FileResource fileResource : candidateAttachs) {
                if (file.getEncodedName().equals(fileResource.getEncodedName()) && fileResource.getContentType().equals(file.getContentType())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void hireCandidate(Integer candidateID, Integer placementId) {
        if (candidateID != null) {
            NewEmployee newEmployee = new NewEmployee();
            newEmployee.setCreatedFrom(FROM_HIRED_PLACEMENT_CANDIDATE);
            newEmployee.setIsFromCandidate(true);
            //register candidate
            EdsCrmContact candidate = crmContactManager.get(candidateID);
            if (candidate != null) {
                candidate.clear();
                EdsReference hiredCandidateStatus = referenceManager.findReference(EdsCrmContact._CANDIDATE_STATUS, EdsCrmContact.CANDIDATE_STATUS_HIRED);
                candidate.setLeadStatus(hiredCandidateStatus);
                newEmployee.setFname(candidate.getFirstName());
                newEmployee.setLname(candidate.getLastName());
                newEmployee.setEmail(candidate.getPrimaryEmail());
                newEmployee.setMname(candidate.getMiddleName());
                newEmployee.setExistingContactID(candidate.getObjectID());
                newEmployee.setLocationId(candidate.getPrefferedLocation() != null ? candidate.getPrefferedLocation().getObjectID() : null);
                newEmployee.setPlacementId(placementId);
                if (candidate.getPhoto() != null) {
                    newEmployee.setPhotoID(candidate.getPhoto().getObjectID());
                }
                if (candidate.getVacancies() != null && candidate.getVacancies().size() > 0) {
                    for (EdsVacancy vacancy : candidate.getVacancies()) {
                        if (vacancy.getPosition() != null) {
                            newEmployee.setPosition(vacancy.getPosition().getName());
                            newEmployee.setPositionId(vacancy.getPosition().getObjectID());
                            break;
                        }
                    }
                }
                //register new employee from candidate
                Integer employeeID = employeeServiceLocal.createEmployeeInternal(newEmployee, null);
                EdsEmployee employee = employeeManager.get(employeeID);
                if (candidate.getAllowances() != null && candidate.getAllowances().size() > 0) {
                    EdsPaymentDeduction newPaymentDeduction;
                    for (EdsPaymentDeduction paymentDeduction : candidate.getAllowances()) {
                        newPaymentDeduction = new EdsPaymentDeduction();
                        newPaymentDeduction.setEmployeeId(employee.getObjectID());
                        newPaymentDeduction.setCategoryId(paymentDeduction.getCategory() != null ? paymentDeduction.getCategory().getObjectID() : null);
                        newPaymentDeduction.setPaymentAmount(paymentDeduction.getPaymentAmount());
                        newPaymentDeduction.setPaymentDate(paymentDeduction.getPaymentDate());
                        newPaymentDeduction.setPayType(paymentDeduction.getPayType());
                        newPaymentDeduction.setRecurring(true);
                        paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                    }
                }
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, candidate, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_CANDIDATE);
            }
        }
    }

    @Override
    public SelectItem[] getVacancyStatusListItem() {
        return commonService.convertReference2SelectItem(EdsVacancy.VACANCY_STATUSES, true, null);
    }

    @Override
    public SelectItem[] getVacancyTypes() {
        return commonService.convertReference2SelectItem(EdsVacancy.VACANCY_TYPE, true, null);
    }

    @Override
    public SelectItem[] getVacancyReligions() {
        return commonService.convertReference2SelectItem(EdsVacancy.VACANCY_RELIGION, true, null);
    }

    @Override
    public SelectItem[] getCandidateStatuses() {
        List<EdsReference> edsReferences = referenceManager.listReferences(EdsCrmContact._CANDIDATE_STATUS, false);
        SelectItem[] stages = new SelectItem[edsReferences.size()];

        int i = 0;
        for (EdsReference item : edsReferences) {
            ReferenceItem referenceItem = item.getRPC();
            referenceItem.setName(item.isSystemReference() && !item.isChanged() ? referenceWfmMessageSource.localize(item.getCode()) : item.getName());
            referenceItem.setParam(item.getCode());
            referenceItem.setSelected(referenceItem.isRequiredComment());
            referenceItem.setDraggable(isDragable(item));
            referenceItem.setAllowEdit(canEdit(item));

            stages[i++] = referenceItem;
        }
        return stages;
    }

    private boolean isDragable(EdsReference item) {
        if (item.getAllowedRoles().isEmpty() && item.getEmployeesCanEdit().isEmpty()) {
            return true;
        } else if (!item.getAllowedRoles().isEmpty() && item.getEmployeesCanEdit().isEmpty()) {
            return userManager.getUser().hasEitherRoles(item.getAllowedRoles().toArray(new EdsRole[]{}));
        } else {
            return item.getEmployeesCanEdit().stream()
                    .anyMatch(e -> userManager.getUser().getObjectID().equals(e.getObjectID()));
        }
    }

    private boolean canEdit(EdsReference item) {
        if (item.getOppEditBtnRole().isEmpty() && item.getEmployeesCanEdit().isEmpty()) {
            return true;
        } else if (!item.getOppEditBtnRole().isEmpty() && item.getEmployeesCanEdit().isEmpty()) {
            return userManager.getUser().hasEitherRoles(item.getOppEditBtnRole().toArray(new EdsRole[]{}));
        } else {
            return item.getEmployeesCanEdit().stream()
                    .anyMatch(e -> userManager.getUser().getObjectID().equals(e.getObjectID()));
        }
    }

    @Override
    public SelectItem[] getCandidateSources() {
        return ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact._CANDIDATE_SOURCE, false), ServerUtils.REFERENCE);
    }

    @Override
    public SelectItem[] getPlacementStatus() {
        return ServerUtils.getAsSelectItem(referenceManager.listReferences(Constants.PLACEMENT_STATUS, false), ServerUtils.REFERENCE);
    }

    @Override
    public SelectItem[] getOwners() {
        return crmServiceLocal.getOwnersListByPermission(PermissionConstants.HRMS_SHOW_IN_CANDIDATE_OWNER);
    }

    @Override
    public SelectItem[] getVacancyJobType() {
        return commonService.convertReference2SelectItem(TIME_TYPES, true, null);
    }

    @Override
    public SelectItem[] getVacancyJobFamily() {
        List<EdsJobFamily> jobs = jobFamilyManager.getJobFamilies(userManager.getUser().getCompany());
        SelectItem[] jobFamilies = new SelectItem[jobs.size()];
        for (int i = 0; i < jobs.size(); i++) {
            jobFamilies[i] = new SelectItem(jobs.get(i).getObjectID(), jobs.get(i).getName());
        }
        return jobFamilies;
    }

    @Override
    public SelectItem[] getPlacementPosition() {
        List<EdsPosition> positions = positionManager.list();

        SelectItem[] positionList = new SelectItem[positions.size()];
        int i = 0;
        for (EdsPosition position : positions) {
            positionList[i] = position.getAsSelectItem();
            i++;
        }
        return positionList;
    }

    @Override
    public SelectItem[] getVacancyReqDegree() {
        return commonService.convertReference2SelectItem(EdsVacancy.VACANCY_DEGREES, true, null);
    }

    /**
     * Generate PlacementItem
     *
     * @param placementID - placementID
     * @return - placementItem
     */
    public PlacementItem getPlacementItem(Integer placementID, String formType, Integer convertFormId) {
        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();
        PlacementItem placementItem = new PlacementItem();
        EdsPlacement placement = null;
        if (placementID != null) {
            placement = placementManager.get(placementID);

            placementItem = placement.getRPC();

            EdsPosition position = placement.getPosition();
            if (position != null) {
                final List<EdsEmployee> employeeList = this.employeeManager.getPositionEmployees(position);
                if (employeeList != null) {
                    placementItem.setHeadCount(String.valueOf(employeeList.size()));
                }
                placementItem.setPlannedPlaceCount(placement.getPosition() != null ? placement.getPosition().getCount() : "0");
            }

            if (placement.getStatus() != null) {
                EdsReference approvedStatus = referenceManager.findReference(Constants.PLACEMENT_STATUS, Constants.PLACEMENT_STATUS_APPROVED);
                boolean showHireButton = placement.getStatus().getObjectID().equals(approvedStatus.getObjectID());
                placementItem.setShowHireButton(showHireButton);
            }
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsPlacement.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.VIEW);
            kpiLog.setEntityId(placementID);
            ServerUtils.kpiLog(log, kpiLog, "Get placement list");
        } else {
            placementItem.setNumberData(generatePlacementNumber());
        }
        placementItem.setTemplates(getPlacementPdfTempletes(PdfReferenceCodeNameEnum.PLACEMENT.name()).getItems());

        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Placement);
        placementItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(placement != null ? placement.getPlacementCustomFields() : null, customFieldsItems));
        Set<EdsPlacementItemTable> itemTables = new HashSet<>();
        if (placement != null) {
            placementItem.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_PLACEMENT, placement.getObjectID())));
            itemTables = placement.getItemTables();
        }

        HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

        if (itemTables != null && itemTables.size() > 0) {

            for (EdsPlacementItemTable itemTable : itemTables) {
                CustomTableRpc rpc = itemTable.getRpc();

                rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                        commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.PlacementItemTable, rpc.getUuid())));

                map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
            }
            placementItem.setCustomTableItems(map);
        }

        if (formType != null && convertFormId != null) {
            placementItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.Placement)));

            if (RelationItem.TYPE_CANDIDATE.equals(formType)) {
                EdsFormProperty formProperties = formPropertyManager.getByFormID(LayoutRPC.PLACEMENT_FORM);

                Gson gson = new Gson();
                FormProperty[] fields = gson.fromJson(formProperties.getSettingsJSONData(), FormProperty[].class);

                ContactListItem candidateItem = contactService.getContact(convertFormId, false);

                if (candidateItem != null && candidateItem.getCustomFields() != null) {
                    for (CompanyCustomFieldItem companyCustomFieldItem : candidateItem.getCustomFields()) {
                        convertPlacementCF(placementItem, fields, companyCustomFieldItem);

                    }
                }
                if (candidateItem != null && candidateItem.getObjectId() != null && candidateItem.getName() != null) {
                    placementItem.setCandidateID(candidateItem.getObjectId());
                    placementItem.setCandidateName(candidateItem.getName());
                }

                if (placementItem.getCustomFieldItems() != null && placementItem.getCustomFieldItems().size() > 0) {
                    for (CompanyCustomFieldItem placementCustomFields : placementItem.getCustomFieldItems()) {
                        convertCandidateFieldstoPlacementCF(placementCustomFields, candidateItem);
                    }
                }

                if (candidateItem != null) {
                    if (candidateItem.getPreferredLocation() != null) {
                        placementItem.setLocationID(candidateItem.getPreferredLocation() != null ? candidateItem.getPreferredLocation().getId() : null);
                        placementItem.setLocationName(candidateItem.getPreferredLocation() != null ? candidateItem.getPreferredLocation().getName() : "");
                    }
                    if (candidateItem.getProjectItem() != null) {
                        placementItem.setProjectName(candidateItem.getProjectItem() != null ? candidateItem.getProjectItem().getName() : "");
                        placementItem.setProjectID(candidateItem.getProjectItem() != null ? candidateItem.getProjectItem().getId() : null);
                    }
                    if (candidateItem.getVacancies() != null) {
                        placementItem.setVacancies(candidateItem.getVacancies());
                    }
                    if (allInOneService.getNotes(convertFormId, formType) != null) {
                        placementItem.setNotes(allInOneService.getNotes(convertFormId, formType));
                    }
                    if (candidateItem.getAttachments() != null) {
                        placementItem.setAttachments(candidateItem.getAttachments());
                    }

                }

            }
        }

        placementItem.setApproveProcessEnabled(approverManager.isExistApproverByEntityType(RelationItem.TYPE_PLACEMENT));
        return placementItem;
    }

    private void convertPlacementCF(PlacementItem item, FormProperty[] fields, CompanyCustomFieldItem companyCustomFieldItem) {
        if (companyCustomFieldItem != null) {
            for (FormProperty formProperty1 : fields) {
                if (formProperty1 != null) {
                    if (companyCustomFieldItem.getAliasName().equals(formProperty1.getAliasName())) {
                        switch (formProperty1.getAliasName()) {
                            case "CANDIDATE" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                                    item.setCandidateID(companyCustomFieldItem.getSelectedId());
                                    item.setCandidateName(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                            case "DATEOFFER" -> {
                                if ((UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || DATA_TYPE_DATE.equals(companyCustomFieldItem.getDataType())) && companyCustomFieldItem.getFieldDateNonConvertedValue() != null) {
                                    item.setDateOffed(companyCustomFieldItem.getFieldDateNonConvertedValue().getDate());
                                }
                            }
                            case "DEPARTMENT" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                                    item.setDepartmentID(companyCustomFieldItem.getSelectedId());
                                    item.setDepartmentName(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                            case "POSITION" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                                    item.setPositionID(companyCustomFieldItem.getSelectedId());
                                    item.setPositionName(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                            case "LOCATION" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                                    item.setLocationID(companyCustomFieldItem.getSelectedId());
                                    item.setLocationName(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                        }
                    }
                }
            }
            if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                for (CompanyCustomFieldItem placementCustomFields : item.getCustomFieldItems()) {
                    if (companyCustomFieldItem.getAliasName().equals(placementCustomFields.getAliasName()) && companyCustomFieldItem.getUiType().equals(placementCustomFields.getUiType()) && companyCustomFieldItem.getDataType().equals(placementCustomFields.getDataType())) {
                        if (UI_TYPE_LOOKUP.equals(placementCustomFields.getUiType())) {
                            if (placementCustomFields.getLookUpTypeEnum().equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                placementCustomFields.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                                placementCustomFields.setSelectedId(companyCustomFieldItem.getSelectedId());
                                placementCustomFields.setItem(companyCustomFieldItem.getItem());
                            }
                        } else {
                            placementCustomFields.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                            placementCustomFields.setSelectedId(companyCustomFieldItem.getSelectedId());
                            placementCustomFields.setItem(companyCustomFieldItem.getItem());
                            placementCustomFields.setFieldDateNonConvertedValue(companyCustomFieldItem.getFieldDateNonConvertedValue());
                        }
                    }
                }
            }
        }

    }

    private void convertCandidateFieldstoPlacementCF(CompanyCustomFieldItem companyCustomFieldItem, ContactListItem candidateItem) {
        switch (companyCustomFieldItem.getAliasName()) {
            case "FIRSTNAME" -> {
                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) && candidateItem.getFirstName() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getFirstName());
                    companyCustomFieldItem.setSelectedId(candidateItem.getFirstNameId());
                }
            }
            case "LASTNAME" -> {
                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) && candidateItem.getLastName() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getLastName());
                    companyCustomFieldItem.setSelectedId(candidateItem.getLastNameId());
                }
            }
            case "BIRTHDAY" -> {
                if (DATA_TYPE_DATE.equals(companyCustomFieldItem.getDataType()) || UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) && candidateItem.getBirthDate() != null) {
                    companyCustomFieldItem.setFieldDateNonConvertedValue(candidateItem.getBirthDate());
                }
            }
            case "STATUS" -> {
                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType()) && candidateItem.getCandidateStatus() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getCandidateStatus().getOriginalName());
                    companyCustomFieldItem.setSelectedId(candidateItem.getCandidateStatus().getId());
                }
            }
            case "SOURCE" -> {
                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType()) && candidateItem.getCandidateSource() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getCandidateSource().getName());
                    companyCustomFieldItem.setSelectedId(candidateItem.getCandidateSource().getId());
                }
            }
            case "WORK_EXPERIENCE" -> {
                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType()) && candidateItem.getWorkExperience() != null && candidateItem.getWorkExperience() != null)) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getWorkExperience().toString());
                    companyCustomFieldItem.setSelectedId(candidateItem.getWorkExperienceMonthOrYear());
                }
            }
            case "EXPECTED_SALARY" -> {
                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) && candidateItem.getExpectedSalary() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getExpectedSalary());
                    companyCustomFieldItem.setSelectedId(candidateItem.getExpectedSalaryID());
                }
            }
            case "EMPLOYER" -> {
                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) && candidateItem.getCurrentEmployer() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getCurrentEmployer());
                    companyCustomFieldItem.setSelectedId(candidateItem.getCurrentEmployerID());
                }
            }
            case "SKILLS" -> {
                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) && candidateItem.getSkills() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getSkills());
                    companyCustomFieldItem.setSelectedId(candidateItem.getSkillsID());
                }
            }
            case "NUMBER" -> {
                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) && candidateItem.getNumberData() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getNumberData().getNumberString());
                }
            }
            case "OWNER" -> {
                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType()) && candidateItem.getOwner() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getOwner());
                    companyCustomFieldItem.setSelectedId(candidateItem.getOwnerId());
                }
            }
            case "Phone" -> {
                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) && candidateItem.getPrimaryPhone() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getPrimaryPhone());
                }
            }
            case "EMAIL" -> {
                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType()) && candidateItem.getPrimaryEmail() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getPrimaryEmail());
                }
            }
            case "IM_ADDRESS" -> {
                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) && candidateItem.getImAddress() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getImAddress().toString());
                }
            }
            case "CREATED_DATE" -> {
                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) && candidateItem.getCreatedDate() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getCreatedDate().toString());
                }
            }
            case "GENDER" -> {
                if (UI_TYPE_RADIOBUTTON.equals(companyCustomFieldItem.getUiType()) && candidateItem.getGender() != null) {
                    companyCustomFieldItem.setFieldStringValue(candidateItem.getGender());
                }
            }
        }
    }

    public NumberData generatePlacementNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = placementManager.getPlacementLastIntNumber();
        if (intNumber == null) {
            intNumber = 0;
        }
        if (settings != null && settings.getPlacementNumberingFormat() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getPlacementNumberingFormat(), settings.getDelimetrPlacementNumbering(), null, null, null, "placement");
            numberData.setDelimiter(settings.getDelimetrPlacementNumbering());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_PLACEMENT_PREFIX /*true*/);
        }

    }

    /**
     * Generate Placement vacancies
     *
     * @param placementID - placement ID
     * @param candidateID - candidate ID
     * @return - vacancies
     */
    public ArrayList<SelectItem> getPlacementVacancies(Integer placementID, Integer candidateID) {
        ArrayList<SelectItem> vacanciesList = new ArrayList<>();

        EdsPlacement placement = null;
        if (placementID != null) {
            placement = placementManager.get(placementID);
        }
        EdsCrmContact candidate = placement != null ? placement.getCandidate() : (candidateID != null ? crmContactManager.get(candidateID) : null);
        if (candidate != null && candidate.getVacancies() != null && candidate.getVacancies().size() > 0) {
            int size = candidate.getVacancies().size();
            for (EdsVacancy cVacancy : candidate.getVacancies()) {
                SelectItem item = cVacancy.getAsSelectItem();
                if (size == 1) {
                    item.setSelected(true);
                }
                vacanciesList.add(item);
            }
        }
        Set<EdsVacancy> vacancies = placement != null ? placement.getVacancies() : null;
        if (vacancies != null && vacancies.size() > 0 && vacanciesList.size() > 0) {
            for (EdsVacancy vacancy : vacancies) {
                SelectItem asSelect = vacancy.getAsSelectItem();
                for (SelectItem s : vacanciesList) {
                    if (s.getId().equals(asSelect.getId())) {
                        s.setSelected(true);
                    }
                }

            }
        }
        return vacanciesList;
    }

    /**
     * Generate Placement item list
     *
     * @param filterParameter - fp
     * @return - placements list
     */
    public ListResult<PlacementItem> getPlacementList(ListingFilterParameter filterParameter) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPlacement.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get placement list");


        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        if (panelTools != null && panelTools.isCustomFieldsShown()) {
            filterParameter.setCustomFieldsShown(true);
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.Placement));
        }

        List<EdsPlacement> placements = placementManager.getPlacementList(filterParameter, userManager.getUser());
        int totalCount = placements.size();
        if (filterParameter.getLimit() > 0) {
            placements = ListUtils.getSublist(placements, filterParameter.getStart(), filterParameter.getLimit());
        }
        ArrayList<PlacementItem> placementItems = new ArrayList<>();
        for (EdsPlacement placement : placements) {
            PlacementItem placementItem = new PlacementItem();
            placementItem.setObjectID(placement.getObjectID());
            if (placement.getCandidate() != null) {
                placementItem.setCandidateID(placement.getCandidate().getObjectID());
                placementItem.setCandidateName(placement.getCandidate().getFullName());
            }
            if (placement.getPosition() != null) {
                placementItem.setPositionID(placement.getPosition().getObjectID());
                placementItem.setPositionName(placement.getPosition().getName());
            }
            if (placement.getStatus() != null) {
                placementItem.setStatusID(placement.getStatus().getObjectID());
                placementItem.setStatusName(referenceWfmMessageSource.localize(placement.getStatus().getCode(), placement.getStatus().getName()));
                placementItem.setStatusCode(placement.getStatus().getCode());

                if (!placement.getStatus().getCode().equals(PLACEMENT_STATUS_HIRED)) {
                    placementItem.setEditable(true);
                }
            }
            if (placement.getOfferDate() != null) {
                placementItem.setDateOffed(placement.getOfferDate());
            }
            if (placement.getNumberData() != null) {
                placementItem.setPlacementCode(placement.getNumberData());
            }

            EdsEmployee edsEmployee = profileManager.getEmployeeByContactId(placement.getCandidate().getObjectID());
            if (edsEmployee != null) {
                placementItem.setEmployeeProfileId(edsEmployee.getObjectID());
            }
            if (filterParameter.isCustomFieldsShown()) {
                placementItem.setCustomFieldsMap(CustomFieldsUtils.getRPCCustomFields(placement.getPlacementCustomFields(), filterParameter.getListPanelTool().getColumnCodeName()));
            }

            placementItems.add(placementItem);
        }

        return new ListResult<>(placementItems, totalCount);
    }

    public List<EdsPlacement> getPlacements(ListingFilterParameter filterParameter) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPlacement.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get placement list");


        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        if (panelTools != null && panelTools.isCustomFieldsShown()) {
            filterParameter.setCustomFieldsShown(true);
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.Placement));
        }

        List<EdsPlacement> placements = placementManager.getPlacementList(filterParameter, userManager.getUser());
        int totalCount = placements.size();
        if (filterParameter.getLimit() > 0) {
            placements = ListUtils.getSublist(placements, filterParameter.getStart(), filterParameter.getLimit());
        }

        return placements;
    }

    /**
     * Generate placement delete option method
     *
     * @param placementID - placement ID
     * @return - placement delete or not
     */
    public Boolean deletePlacement(Integer placementID) {
        if (placementID != null) {
            EdsPlacement placement = placementManager.get(placementID);
            if (placement != null) {
                placement.setDeleted(true);
                placement.getVacancies().clear();
                placementManager.createOrUpdate(placement);
                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsPlacement.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.DELETE);
                kpiLog.setEntityId(placementID);
                ServerUtils.kpiLog(log, kpiLog, "Delete placement");
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, placement, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_PLACEMENT);
                return Boolean.TRUE;
            }

        }
        return Boolean.FALSE;
    }

    private CustomFormItemPdfTemplateList getPlacementPdfTempletes(String type) {
        List<EdsCompanyPdfTemplate> templates = companyPdfTemplateManager.getCompanyPDFTemplatesByType(type, false);
        SelectItem[] items = new SelectItem[templates.size()];
        int i = 0;
        Integer defaultTemplateID = null;
        for (EdsCompanyPdfTemplate t : templates) {
            items[i] = new SelectItem(t.getObjectID(), t.getName());
            if (t.isDefaultTemplate()) {
                defaultTemplateID = t.getObjectID();
            }
            i++;
        }
        return new CustomFormItemPdfTemplateList(items, defaultTemplateID);
    }

    public ArrayList<Appointment> getCurrentInterviews() {
        ArrayList<Appointment> result = new ArrayList<>();
        List<Object[]> currentInterviews = relationManager.getCurrentInterviews();
        if (currentInterviews != null) {
            Appointment contactListItem = null;
            for (Object[] item : currentInterviews) {
                contactListItem = new Appointment();
                contactListItem.setObjectID((Integer) item[0]);
//                contactListItem.setContactName((String) item[1]);
                contactListItem.setOwnerName((String) item[1]);
                contactListItem.setCreatedDate((Date) item[2]);
                contactListItem.setAllDay(item[3] != null ? ((Boolean) item[3]) : false);
                contactListItem.setSharedEmployeesString(item[4] != null ? (String) item[4] : "");//there are shared employees
                result.add(contactListItem);
            }
        }
        return result;
    }

    @Override
    public PlacementItem getCandidateData(Integer candidateID) {
        PlacementItem item = new PlacementItem();
        ArrayList<SelectItem> matchedVacancies = new ArrayList<>();
        if (candidateID != null) {
            EdsCrmContact candidate = crmContactManager.get(candidateID);

            item.setCandidate(new SelectItem(candidateID,candidate.getFullName()));
            item.setDepartment(candidate.getCandidateDepartment() != null ? candidate.getCandidateDepartment().getAsSelectItem() : null);
            item.setLocation(candidate.getPrefferedLocation() != null ? candidate.getPrefferedLocation().getAsSelectItem() : null);
            item.setPosition(candidate.getCandidatePosition() != null ? candidate.getCandidatePosition().getAsSelectItem() : null);
            if (candidate.getVacancies() != null && candidate.getVacancies().size() > 0){
                candidate.getVacancies().forEach(vacancy -> {
                    matchedVacancies.add(new SelectItem(vacancy.getObjectID(),vacancy.getName()));
                });
            }
            item.setVacancies(matchedVacancies);
        }
        return item;
    }

    @Override
    public ArrayList<SelectItem> getProjectVacancyItem(Integer objectID, Integer projectID) {
        ArrayList<SelectItem> list = new ArrayList<>();
        EdsCrmContact contact = null;
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setBriefly(false);
        fp.setProjectId(projectID);
        List<EdsVacancy> vacancies = vacancyManager.list(fp);
        if (objectID != null) {
            contact = crmContactManager.get(objectID);
        }
        if (vacancies != null && vacancies.size() > 0) {
            SelectItem item;
            for (EdsVacancy vacancy : vacancies) {
                item = vacancy.getAsSelectItem();
                if (contact != null && contact.getVacancies() != null && contact.getVacancies().contains(vacancy)) {
                    item.setSelected(true);
                }
                list.add(item);
            }
        }
        return list;
    }

    @Override
    public SelectItem[] getVacancyLookUpItems(ListingFilterParameter filterParameter) {
        ListResult<VacancyItem> items = getVacancyList(filterParameter);
        List<SelectItem> vacancies = new ArrayList<>();
        for (VacancyItem item : items.getList()) {
            SelectItem selectItem = new SelectItem();
            selectItem.setId(item.getObjectID());
            selectItem.setName(!item.getJobTitle().equals("") ? item.getJobTitle() : item.getPositionItem().getName());

            vacancies.add(selectItem);
        }

        return vacancies.toArray(new SelectItem[]{});
    }

    @Override
    public void changeCandidateStatus(ArrayList<Integer> ids, Integer statusId) {
        if (statusId != null && ids != null && ids.size() > 0) {
            List<Integer> statusChangedCandidates = crmContactManager.getStatusChangedLeads(statusId, ids);
            crmContactManager.changeLeadStatus(statusId, ids);

            List<EdsCrmContact> candidates = crmContactManager.getCandidatesByIDs(ids);
            for (EdsCrmContact contact : candidates) {
                if (statusChangedCandidates.contains(contact.getObjectID())) {
                    contact.setPropertiesChanged(true);
                    crmContactManager.createHistory(contact);
                    if (candidates.size() == 1) {
                        contact.clear();
                        contact.addChange(CustomFormConstants.STATUS);
                        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, contact, userManager.getUser());
                        workflowEvent.setEntityType(RelationItem.TYPE_CANDIDATE);
                    }
                }
            }
            try {
                contactSolrComponent.indexes(candidates);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public SelectItem getCandidateProject(Integer candidateID) {
        EdsCrmContact crmContact = null;
        if (candidateID != null) {
            crmContact = crmContactManager.get(candidateID);
            if (crmContact != null && crmContact.getCandidateProject() != null) {
                return new SelectItem(crmContact.getCandidateProject().getObjectID(), crmContact.getCandidateProject().getName());
            }
        }
        return null;
    }

    @Override
    public VacancyItem getVacancyQuickData() {
        VacancyItem result = new VacancyItem();
        result.setNumberData(generateVacancyNumber(null));

        List<EdsPosition> edsPositions = positionManager.list();
        if (edsPositions != null && edsPositions.size() > 0) {
            List<SelectItem> positions = edsPositions.stream().map(pos -> new SelectItem(pos.getObjectID(), pos.getNumberData() + " --> " + pos.getName())).toList();
            result.setPositions(positions.toArray(new SelectItem[]{}));
        }
        PositionItem positionItem = new PositionItem();
        positionItem.setPosStatus(commonService.convertReference2SelectItem(EdsVacancy.VACANCY_STATUSES, true, null));
        result.setPositionItem(positionItem);

        EdsUser edsUser = userManager.getUser();
        List<EdsEmployee> edsEmployees = userManager.getUsersByROLES(edsUser.getCompany().getObjectID(), ADMIN, DR, TL, PM, HR, SALESMAN);
        if (edsEmployees != null && !edsEmployees.isEmpty()) {
            List<SelectItem> employeeList = edsEmployees.stream()
                    .map(edsEmployee -> new SelectItem(edsEmployee.getObjectID(), edsEmployee.getFullName()))
                    .toList();

            result.setManagers(employeeList.toArray(new SelectItem[]{}));
            result.setManager(new SelectItem(edsUser.getObjectID()));
        }
        return result;
    }

    public EdsPlacementItemTableCF savePlacementCustomTableFields(EdsPlacementItemTableCF customfField, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (customfField == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && fieldItem.getFieldStringValue().length() > 0)
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || fieldItem.getProfielImageId() != null
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                customfField = new EdsPlacementItemTableCF();
                placementItemTableCFManager.create(customfField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customfField, customFieldItems);
            return customfField;
        }
        return null;
    }

    public EdsVacancyItemTableCF saveVacancyCustomItemTableFields(EdsVacancyItemTableCF customfField, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (customfField == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && fieldItem.getFieldStringValue().length() > 0)
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || fieldItem.getProfielImageId() != null
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                customfField = new EdsVacancyItemTableCF();
                vacancyItemTableCFManager.create(customfField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customfField, customFieldItems);
            return customfField;
        }
        return null;
    }

    public void insertCandidateStatusHistory(EdsCrmContact contact, EdsReference newStatus, String note) {
        EdsUser user = userManager.getUser();
        EdsCandidateStatusHistory statusHistory = new EdsCandidateStatusHistory();
        statusHistory.setModifiedDate(new Date());
        statusHistory.setModifier(user.getEmployee());
        if (note != null) {
            statusHistory.setComment(note);
        }
        statusHistory.setStatus(newStatus);
        statusHistory.setCandidate(contact);
        statusHistoryManager.create(statusHistory);
    }

}
