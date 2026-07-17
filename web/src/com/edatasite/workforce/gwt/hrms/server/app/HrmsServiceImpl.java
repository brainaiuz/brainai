package com.edatasite.workforce.gwt.hrms.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.log.KpiEntityType;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.assessment.EdsApprasialScoreType;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsSkillRating;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmployeeNote;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmployment;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmploymentFields;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmploymentType;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsBackupsEmployeeCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsBrigadaCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCertificateCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsDependentCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeItemTableCF;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeStepCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsGoalCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsOnboardingStepCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsPositionCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsRotationCutomFields;
import com.edatasite.workforce.core.domain.customfields.EdsShiftCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.customform.EdsCustomItemTable;
import com.edatasite.workforce.core.domain.customform.EdsEmployeeCustomItemTable;
import com.edatasite.workforce.core.domain.customform.EdsModel;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.documents.EdsFileBody;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.goal.EdsBusinessGoal;
import com.edatasite.workforce.core.domain.goal.EdsDepartmentGoalChartSettings;
import com.edatasite.workforce.core.domain.goal.EdsDepartmentGoalEmployeeMetricHistory;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.domain.goal.EdsGoalAssignees;
import com.edatasite.workforce.core.domain.goal.EdsGroupGoal;
import com.edatasite.workforce.core.domain.hmrc.EdsEmployeeExperienceItemTable;
import com.edatasite.workforce.core.domain.hmrc.EdsEmployeeExperienceItemTableCF;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacementCustomFields;
import com.edatasite.workforce.core.domain.recruitment.EdsGroupPlacementItemTable;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsRotation;
import com.edatasite.workforce.core.domain.recruitment.EdsRotationItemTable;
import com.edatasite.workforce.core.domain.recruitment.EdsRotationItemTableCF;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.reporting.EdsReportTemplate;
import com.edatasite.workforce.core.domain.reporting.EdsReportTemplateCategory;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowRule;
import com.edatasite.workforce.core.solr.component.CertificateSolrComponent;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeStepSolrComponent;
import com.edatasite.workforce.core.solr.component.PositionSolrComponent;
import com.edatasite.workforce.core.solr.document.CertificateSolrDoc;
import com.edatasite.workforce.core.solr.document.EmployeeStepSolrDoc;
import com.edatasite.workforce.core.solr.document.PositionSolrDoc;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.ai.service.PositionAiService;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillItem;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.UserFingerPrintDeviceItem;
import com.edatasite.workforce.gwt.contact.client.rpc.AnnualLeaveItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ExperienceTableItems;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.DepartmentGoalChartSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.DepartmentGoalEmployeeHistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeePresentItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.EncryptionUtils;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionAiRequest;
import com.edatasite.workforce.gwt.core.client.rpc.PositionAiResponse;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.profile.ActionTimesTO;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCertificateRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeStepRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrPositionRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.BugReportServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.SalaryHistoryLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.controllers.EmailAddressValidator;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.benefit.BenefitManager;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentManager;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentNoteManager;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentTypeManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.BackupsEmployeeCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.BrigadaCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.CertificateCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.DependentCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.EmployeeCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.EmployeeExperienceItemTableCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.EmployeeStepCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.GoalCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.OnboardingStepCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.PositionCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.RotationCfManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.RotationItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.goal.BusinessGoalManager;
import com.edatasite.workforce.gwt.core.server.db.goal.DepartmentGoalChartSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.goal.DepartmentGoalEmployeeMetricHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalAssigneesManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.db.goal.GroupGoalManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PaymentDeductionManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipPaymentsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TrusteeManager;
import com.edatasite.workforce.gwt.core.server.db.settings.EmployeeDocumentReminderSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.HrReminderSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ScheduledCourseManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportTemplateCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportTemplateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BackupsEmployeeEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EmployeeEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.LocationEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.NewEmployeeEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.TimeslotEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.UserAuthEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.EmployeeStepCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.EmployeeSupervisorChangeEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.hrms.GroupPlacementEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.hrms.RotationEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.hrms.ShiftEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.hrms.client.EmployeeProfileConstans;
import com.edatasite.workforce.gwt.hrms.client.rpc.BackupsEmployeeObject;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupGoalITem;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupPlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupPlacementTableItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsAPIItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RemindersDataRpc;
import com.edatasite.workforce.gwt.hrms.client.rpc.RotationItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RotationTableItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItems;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftTeamsItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.SubscriptionItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.SubscriptionUsageItem;
import com.edatasite.workforce.gwt.hrms.client.ui.CertificateUtils;
import com.edatasite.workforce.gwt.hrms.server.db.JobFamilyManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.gwt.profile.client.rpc.BenefitItem;
import com.edatasite.workforce.gwt.profile.client.rpc.HrReminderItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileImItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.ui.EmailNotificationConstants;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectViewItem;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.mail.EdsTemplates;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.finnetlimited.reportservice.core.server.CoreServiceLocal;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Errors.EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS;
import static com.edatasite.workforce.gwt.core.client.ui.Errors.EMPLOYEE_WITH_THIS_EMAIL_DOES_NOT_EXIST;
import static com.edatasite.workforce.gwt.core.client.ui.Errors.EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_SHIFT_SEE_ALL;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.SHOW_ALL_EMPLOYEE_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.BRIGADA_ID;
import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.CANDIDATE_ID;
import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.EMPLOYEE_ID;
import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * User: Sherali
 * Date: Oct 14, 2009
 * Time: 6:59:50 PM
 */
@Transactional
@Service("hrmsService")
public class HrmsServiceImpl implements HrmsService, HrmsServiceLocal, Constants {

    public static final DecimalFormat decimalFormat = new DecimalFormat("0000");

    private static final Logger log = LoggerFactory.getLogger(HrmsServiceImpl.class);
    private final Gson gson = new Gson();
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private PayrollCategoryManager categoryManager;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private HostBasedSettingManager hostBasedSettingManager;
    @Autowired
    private ImManager imManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private AnnualLeaveAllowanceManager annualLeaveAllowanceManager;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private GradeManager gradeManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private RecruitmentServiceLocal recruitmentService;
    @Autowired
    private ShiftManager shiftManager;
    @Autowired
    private CustomFormManager customFormManager;
    @Autowired
    private RotationManager rotationManager;
    @Autowired
    private GroupPlacementManager groupPlacementManager;
    @Autowired
    private GroupPlacementItemTableManager groupPlacementItemTableManager;
    @Autowired
    private ItemTableSettingService itemTableSettingService;
    @Autowired
    private RotationItemTableManager rotationItemTableManager;
    @Autowired
    private ShiftTeamsManager shiftTeamsManager;
    @Autowired
    private ShiftItemManager shiftItemManager;
    @Autowired
    private ShiftCustomFieldsManager shiftCustomFieldsManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private BrigadaManager brigadaManager;
    @Autowired
    private BrigadaEmployeesManager brigadaEmployeesManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private DependentManager dependentManager;
    @Autowired
    private DepartmentTreeManager departmentTreeManager;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private BusinessGoalManager businessGoalManager;
    @Autowired
    private GoalManager goalManager;
    @Autowired
    private DepartmentGoalEmployeeMetricHistoryManager employeeMetricHistoryManager;
    @Autowired
    private DepartmentGoalChartSettingsManager departmentGoalChartSettingsManager;
    @Autowired
    private GoalAssigneesManager goalAssigneesManager;
    @Autowired
    private WorkflowAlertManager workflowAlertManager;
    @Autowired
    private BaseEventsPostProcessor baseEventsPostProcessor;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private PositionBenefitAllowanceManager positionBenefitAllowanceManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private LabourPeriodManager labourPeriodManager;
    @Autowired
    private SinxDocumentsSettingsManager sinxDocumentsSettingsManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private JobFamilyManager jobFamilyManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private EmployeeCFManager employeeCFManager;
    @Autowired
    private DependentCFManager dependentCFManager;
    @Autowired
    private OnboardingStepManager onboardingStepManager;
    @Autowired
    private OnboardingPeriodManager onboardingPeriodManager;
    @Autowired
    private StepEmployeeManager stepEmployeeManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private UserBankAccountManager userBankAccountManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("hrmsLocalizer")
    private WfmMessageSource hrmsLocalizer;
    @Autowired
    @Qualifier("contactService")
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private GoalCFManager goalCustomFieldsManager;
    @Autowired
    private FormPropertyManager formPropertyManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private CourseManager courseManager;
    @Autowired
    private ValidityPeriodManager validityPeriodManager;
    @Autowired
    private PerformanceNoteManager performanceNoteManager;
    @Autowired
    private TrusteeManager trusteeManager;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    @Qualifier("bugReportService")
    private BugReportServiceLocal bugReportServiceLocal;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private EmailNotificationSettingsManager emailNotificationSettingsManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private PermissionManager permissionManager;
    @Autowired
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;
    @Autowired
    private PaymentDeductionManager paymentDeductionManager;
    @Autowired
    private CertificateOfEmploymentManager certificateOfEmploymentManager;
    @Autowired
    private CertificateOfEmploymentTypeManager certificateOfEmploymentTypeManager;
    @Autowired
    private CertificateOfEmploymentFieldsManager certificateOfEmploymentFieldsManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private ScheduledCourseManager scheduledCourseManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private BenefitManager benefitManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private ModelManager modelManager;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private OnboardingStepCFManager onboardingStepCFManager;
    @Autowired
    private EmployeeStepCFManager employeeStepCFManager;
    @Autowired
    private EmailTemplateManager emailTemplateManager;
    @Autowired
    @Qualifier("companyCFSettingsManager")
    private CompanyCustomFieldsManager companyCFManager;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private EmployeeBenefitAllowanceManager employeeBenefitAllowanceManager;
    @Autowired
    private HrReminderSettingsManager hrReminderSettingsManager;
    @Autowired
    private EmployeeDocumentReminderSettingsManager employeeDocumentReminderSettingsManager;
    @Autowired
    private WorkflowRuleManager workflowRuleManager;
    @Autowired
    @Qualifier("reportingCoreService")
    private CoreServiceLocal reportingServiceLocal;
    @Autowired
    private ReportTemplateCategoryManager reportTemplateCategoryManager;
    @Autowired
    private TimeSheetManager timesheetManager;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    private TimeSlotItemManager timeSlotItemManager;
    @Autowired
    private ReportTemplateManager reportTemplateManager;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private HolidayManager holidayManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private UserFingerPrintDeviceManager userFingerPrintDeviceManager;
    @Autowired
    private UserFingerPrintAdjustmentServiceLocal userFingerPrintAdjustmentServiceLocal;
    @Autowired
    private PayslipPaymentsManager payslipPaymentsManager;
    @Autowired
    @Qualifier("changesManager")
    private ChangesManager changesManager;
    @Autowired
    private SkillManager skillManager;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private GroupGoalManager groupGoalManager;
    @Autowired
    private SickRequestDurationManager sickRequestDurationManager;
    @Autowired
    private AssessmentManager assessmentManager;
    @Autowired
    private ReferenceLocaleManager referenceLocaleManager;
    @Autowired
    private EmployeeAssessmentManager employeeAssessmentManager;
    @Autowired
    private TelegramChatService telegramChatService;
    @Autowired
    private CertificateCFManager certificateCFManager;
    @Autowired
    private SpokenLanguagesManager spokenLanguagesManager;
    @Autowired
    private EmployeeItemTableManager employeeItemTableManager;
    @Autowired
    private EmployeeItemTableCFManager employeeItemTableCFManager;
    @Autowired
    private PositionCFManager positionCFManager;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private CertificateOfEmploymentNoteManager certificateOfEmploymentNoteManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private PlacementManager placementManager;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private UserFingerPrintmanager fingerPrintmanager;
    @Autowired
    private TimeTrackManager timeTrackManager;
    @Autowired
    private BrigadaCFManager brigadaCFManager;
    @Autowired
    private ShiftSettingsManager shiftSettingsManager;
    @Autowired
    private RotationCfManager rotationCfManager;
    @Autowired
    private GroupPlacementCustomFieldManager groupPlacementCustomFieldManager;
    @Autowired
    private RotationItemCFManager rotationItemCFManager;
    @Autowired
    private CustomFormItemManager customFormItemManager;
    @Autowired
    private AttendanceHoursManager attendanceHoursManager;
    @Autowired
    private LaborPeriodHistoryManager periodHistoryManager;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;
    @Autowired
    private EmployeeStepSolrComponent employeeStepSolrComponent;
    @Autowired
    private CertificateSolrComponent certificateSolrComponent;
    @Autowired
    private PositionSolrComponent positionSolrComponent;
    @Autowired
    private EmployeeExperienceItemTableCFManager employeeExperienceItemTableCFManager;
    @Autowired
    private EmployeeExperienceItemTableManager employeeExperienceItemTableManager;
    @Autowired
    private CompanyPayrollSettingsManager companyPayrollSettingsManager;
    @Autowired
    private EmployeeLocationManager employeeLocationManager;
    @Autowired
    private BackupsEmployeeManager backupsEmployeeManager;
    @Autowired
    private PayrollServiceLocal payrollServiceLocal;
    @Autowired
    private BackupEmployeeManager backupEmployeeManager;
    @Autowired
    private BackupsEmployeeCFManager backupsEmployeeCFManager;
    @Autowired
    private AdditionalPaymentManager additionalPaymentManager;
    @Autowired
    private SalaryHistoryLocal salaryHistoryLocal;
    @Autowired
    private AttendanceTerminalManager attendanceTerminalManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProfileItem getProfile(final Integer employeeID) {
        ProfileItem profileItem = new ProfileItem();
        final EdsEmployeeProfile profile;

        if (employeeID != null)
            profile = this.profileManager.getProfile(employeeID);
        else
            profile = this.profileManager.getProfile();

        final List<EdsProfileIm> ims = this.imManager.getImList();

        final ProfileImItem[] profileImItem = new ProfileImItem[ims.size()];
        int i = 0;

        for (final EdsProfileIm im : ims) {
            profileImItem[i] = new ProfileImItem();
            profileImItem[i].setIm(im.getIm().getName());
            profileImItem[i].setAccount(im.getAccount());
            i++;
        }

        if (profile != null) {
            final EdsEmployee employee = this.employeeManager.getEmployeeByProfileID(profile.getObjectID());
            if (employee != null) {
                profileItem = profile.getRPC(profileItem);
                profileItem.setEmployeeId(employee.getObjectID());
                profileItem.setObjectId(profile.getObjectID());
                profileItem.setFirstName(employee.getFirstName() == null ? "N/A" : employee.getFirstName());
                profileItem.setMiddleName(employee.getMiddleName() == null ? "N/A" : employee.getMiddleName());
                profileItem.setLastName(employee.getLastName() == null ? "N/A" : employee.getLastName());
                //contactInformation
                profileItem.setHomeEmail(employee.getEmail());
                profileItem.setWorkPhone(employee.getWorkPhoneFirst() == null ? "N/A" : employee.getWorkPhoneFirst());
                profileItem.setHomePhone(employee.getHomePhoneFirst() == null ? "N/A" : employee.getHomePhoneFirst());
                profileItem.setMobile(employee.getMobilePhoneFirst() == null ? "N/A" : employee.getMobilePhoneFirst());

                if (profile.getContact() != null) {
                    final ListingFilterParameter fp = new ListingFilterParameter(false);
                    fp.setHRMS(true);
                    profileItem = (ProfileItem) profile.getContact().getRPC(fp, profileItem);
                }
                profileItem.setCareerLevel(profile.getCareerLevel() == null
                        ? "N/A"
                        : this.referenceWfmMessageSource.localizeRef(profile.getCareerLevel()));
                profileItem.setCareerLevelId(profile.getCareerLevel() == null
                        ? null
                        : profile.getCareerLevel().getObjectID());
                profileItem.setExperience(profile.getExperience() == null
                        ? null
                        : this.referenceWfmMessageSource.localizeRef(profile.getExperience()));
                profileItem.setExperienceId(profile.getExperience() == null
                        ? null
                        : profile.getExperience().getObjectID());
                profileItem.setEducationLevel(profile.getEducationLevel() == null
                        ? "N/A"
                        : this.referenceWfmMessageSource.localizeRef(profile.getEducationLevel()));
                profileItem.setEducationLevelId(profile.getEducationLevel() == null
                        ? null
                        : profile.getEducationLevel().getObjectID());
                profileItem.setManagementExperience(profile.getManagementExperience() == null
                        ? "N/A"
                        : this.referenceWfmMessageSource.localizeRef(profile.getManagementExperience()));
                profileItem.setManagementExperienceId(profile.getManagementExperience() == null
                        ? null
                        : profile.getManagementExperience().getObjectID());
                profileItem.setProjectLeadershipExperience(profile.getProjectLeadershipExperience() == null
                        ? "N/A"
                        : this.referenceWfmMessageSource.localizeRef(profile.getProjectLeadershipExperience()));
                profileItem.setProjectLeadershipExperienceId(profile.getProjectLeadershipExperience() == null
                        ? null
                        : profile.getProjectLeadershipExperience().getObjectID());
                profileItem.setDob(employee.getBirthDay() != null ? new DateNonConvertable(employee.getBirthDay()) : null);
                profileItem.setGender(profile.getGender() == null ? "N/A" : profile.getGender());
                profileItem.setMartialStatus(profile.getMartialStatus() == null
                        ? "N/A"
                        : this.referenceWfmMessageSource.localizeRef(profile.getMartialStatus()));

                setEmployeeLanguages(profileItem);
                profileItem.setEmpCode(profile.getEmployeeCode());
                profileItem.setPosition(employee.getPosition() != null ? employee.getPosition().getName() : "");
                profileItem.setStatus(this.referenceWfmMessageSource.localize(employee.getAccountStatus().getCode(), employee.getAccountStatus().getName()));
                profileItem.setStatusCode(employee.getAccountStatus().getCode());
                profileItem.setPosition(employee.getPosition() != null ? employee.getPosition().getName() : "N/A");
                profileItem.setEmpMode(profile.getEmploymentMode() != null ? profile.getEmploymentMode().getName() : "");
                profileItem.setHireDate(employee.getStartDate() != null ? new DateNonConvertable(employee.getStartDate()) : null);
                profileItem.setFireDate(employee.getEndDate() != null ? new DateNonConvertable(employee.getEndDate()) : null);
                profileItem.setReportsTo(profile.getReportsTo() != null ? profile.getReportsTo().getName() : "");
                profileItem.setTermsOfContract(profile.getTermsOfContract());

                if (profile.getTermsOfCMonthOrYear() != null) {
                    profileItem.setTermsOfCMonthORYear(profile.getTermsOfCMonthOrYear());
                }

                profileItem.setSalaryGrade(profile.getSalaryGrade() != null
                        ? profile.getSalaryGrade().getGradeCode() + " " + profile.getSalaryGrade().getGradeLevel()
                        : "");
                final EdsEmployeePayrollSettings salary = this.employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), Constants.SALARY);

                if (salary != null && salary.getValue() != null) {
                    profileItem.setSalaryAmount(Double.parseDouble(salary.getValue()));
                } else {
                    profileItem.setSalaryAmount(0d);
                }

                final EdsEmployeePayrollSettings jobTitle = this.employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), CustomFormConstants.JOB_TITLE);

                if (jobTitle != null) {
                    final EdsEmployeePayrollSettings jobTitleText = this.employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), Constants.JOB_TITLE_TEXT);
                    profileItem.setJobTitleId(Integer.valueOf(jobTitle.getValue()));
                    profileItem.setJobTitle(jobTitleText.getValue());
                }

                if (profile.getVisaExpirationDate() != null) {
                    profileItem.setVisaExpirationDate(new DateNonConvertable(profile.getVisaExpirationDate()));
                }
                if (profile.getVisaExpirationDateReminders() != null) {
                    profileItem.setVisaExpirationDateReminder(profile.getVisaExpirationDateReminders());
                }
                if (profile.getEmpHistory() != null) {
                    profileItem.setEmpHistory(profile.getEmpHistory());
                }

                profileItem.setDepartment(employee.getTeam() != null ? employee.getTeam().getName() : "");
                profileItem.setRoleName(this.getSortedRolesAsString(employee.getRoles()));

                final EdsLocation location = employee.getLocation();

                if (location != null) {
                    profileItem.setLocationName(location.getName());
                }
                profileItem.setEmployeeImageUrl(this.getUserImageUrl(employee));
                profileItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(),
                        this.commonService.getCompanyCustomFields(ViewName.Employee)));
            } else {
                return null;
            }
        } else {
            return null;
        }
        return profileItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getImageUrl(final Integer id) {
        return uploadManager.getFileURL(id, false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DependentItem[] getDependents(final Integer employeeId) {
        final EdsEmployee employee;

        if (employeeId != null)
            employee = this.employeeManager.get(employeeId);
        else
            employee = this.employeeManager.getUser().getEmployee();

        final List<EdsDependent> dependents = this.dependentManager.getDependentList(employee);
        final DependentItem[] dependentItems = new DependentItem[dependents.size()];
        int i = 0;

        for (final EdsDependent dependent : dependents) {
            dependentItems[i] = dependent.getRPC();
            dependentItems[i].setRelationship(hrmsLocalizer.localize(dependent.getRelationship(), dependent.getRelationship()));
            dependentItems[i].setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(dependent.getCustomFields(),
                    this.commonService.getCompanyCustomFields(ViewName.Dependent)));
            i++;
        }
        return dependentItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<OnboardingItem> getOnboardingPeriodList(final ListingFilterParameter fp) {
        final List<EdsOnboardingPeriod> onboardingPeriodList = this.onboardingPeriodManager.getOnboardingPeriodList(fp);
        final Integer count = this.onboardingPeriodManager.getOnboardingPeriodTotalCount(fp);
        final ArrayList<OnboardingItem> onboardingItem = onboardingPeriodList.stream().map(EdsOnboardingPeriod::getRPC).collect(Collectors.toCollection(ArrayList::new));
        return new ListResult<>(onboardingItem, count);
    }


    @Override
    public LinkedHashMap<String, Integer> getOnboardingChartData(final ListingFilterParameter fp) {
        final List<EdsStepEmployee> stepList = this.stepEmployeeManager.getStepList(fp);
        final LinkedHashMap<String, Integer> resultMap = new LinkedHashMap<>(stepList.size());
        String status;
        for (final EdsStepEmployee stepEmployee : stepList) {
            status = stepEmployee.getStatus() != null ? stepEmployee.getStatus().getName() : "Untitled";
            if (resultMap.containsKey(status)) {
                resultMap.put(status, resultMap.get(status) + 1);
            } else {
                resultMap.put(status, 1);
            }
        }
        return resultMap;
    }

    @Override
    public Integer copyOnboardingSteps(final ArrayList<Integer> stepIDs, final Integer fromCompanyID, final Integer toCompanyID) {
        final List<EdsOnboardingStep> oldSteps = this.onboardingStepManager.getStepsForCopy(fromCompanyID, stepIDs);
        final ArrayList<Integer> createdIDs = new ArrayList<>();
        if (oldSteps != null && oldSteps.size() > 0) {
            this.copySteps(fromCompanyID, toCompanyID, oldSteps, createdIDs, null, null);
        }
        stepIDs.removeAll(createdIDs);
        return stepIDs.size();
    }

    @Override
    public void approveOrRejectEmployeeStep(final Integer objectID, final Integer stepID, final boolean approve) {

        final EdsUser user = this.employeeManager.getUser();

        final EdsStepEmployee employeeStep = this.stepEmployeeManager.get(objectID);
        employeeStep.clear();
        final EdsReference statusReference = this.referenceManager.get(approve ? employeeStep.getCurrentApprover().getApproveStatusID() : employeeStep.getCurrentApprover().getRejectStatusID());
        employeeStep.updateStatus(statusReference);

        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employeeStep, user);
        workflowEvent.setEntityType(employeeStep.getOnboardingStep().getFormID());

        /* add workflow event to start workflow rule... */
        final EdsBusinessEvent workflowEvent2 = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), employeeStep, user);
        workflowEvent2.setEntityType(RelationItem.TYPE_EMPLOYEE_STEP);

    }

    @Override
    public boolean isEnableApprovers(final String formID) {
        return this.approverManager.list(formID, null).size() > 0;
    }

    @Override
    public ArrayList<SelectItem> getFingerprintSetup() {
        final Integer companyID = this.userManager.getUser().getCompany().getObjectID();
        final ArrayList<CompanyDomain> companyDomainList = this.globalAuthJdbcSpringManager.getFingerprintSetup(companyID);
        final ArrayList<SelectItem> itemList = new ArrayList<>();
        companyDomainList.forEach(item -> {
            final SelectItem selectItem = new SelectItem();
            selectItem.setName(item.getCompanyBranchName());
            selectItem.setReferenceCode(item.getCompanyUniqueID());
            itemList.add(selectItem);
        });

        return itemList;
    }

    @Override
    public HashMap<String, String> getUserDeviceFingerPrint() {
        final HashMap<String, String> result = new HashMap<>();
        final List<EdsUserFingerPrintDevice> fingerPrintDevices = this.userFingerPrintDeviceManager.getUserDeviceFingerPrintList();
        if (fingerPrintDevices != null && !fingerPrintDevices.isEmpty() && fingerPrintDevices.size() > 0) {
            fingerPrintDevices.forEach(item -> {
                final String key = item.getUserId() + "__" + item.getDeviceId();
                final String value = item.getFingerprintId();
                result.put(key, value);
            });
        }
        return result;
    }

    @Override
    public ArrayList<UserFingerPrintDeviceItem> getComapanyUsers(final Integer userId) {
        final List<EdsEmployee> userList;

        if (userId != null) {
            final EdsEmployee user = this.employeeManager.get(userId);
            userList = new ArrayList<>();
            userList.add(user);
        } else {
            final EdsCompany company = this.userManager.getUser().getCompany();
            userList = this.employeeManager.getEmployeeSortName(company);
        }
        final ArrayList<UserFingerPrintDeviceItem> result = new ArrayList<>();
        userList.forEach(item -> {
            final UserFingerPrintDeviceItem deviceItem = new UserFingerPrintDeviceItem();
            deviceItem.setUserId(item.getObjectID());
            deviceItem.setUserName(item.getFullName() == null ? "" : item.getFullName());
            result.add(deviceItem);
        });
        return result;
    }

    @Override
    public ArrayList<UserFingerPrintDeviceItem> getUserFingerprintDevicesByDeviceId(String deviceId) {
        return new ArrayList<>(userFingerPrintAdjustmentServiceLocal.getAllByDeviceId(deviceId));
    }

    @Override
    public void saveUserFingerPrintDeviceData(final HashMap<String, String> items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        for (final String key : items.keySet()) {
            final String[] item = key.split("__");
            final String value = items.get(key);
            final EdsUser user = this.userManager.getUserByUserID(Integer.parseInt(item[0]));
            EdsUserFingerPrintDevice userFingerPrintDevice = this.userFingerPrintDeviceManager.getUserFingerPrintdByUserIdAndDeviceId(Integer.parseInt(item[0]), item[1]);

            final String fakeFPDeviceId = this.genericSettingsManager.getValueByKey(GenericSettingsEnum.FAKE_FINGERPRINT_DEVICE_ID);
            final EdsUserFingerPrintDevice userFakeFingerPrintDevice = this.userFingerPrintDeviceManager.getUserFingerPrintdByUserIdAndDeviceId(Integer.parseInt(item[0]), fakeFPDeviceId);

            if (userFakeFingerPrintDevice != null) {
                userFakeFingerPrintDevice.setFingerprintId(value);
                this.userFingerPrintDeviceManager.update(userFakeFingerPrintDevice);
            } else if (userFingerPrintDevice != null) {
                userFingerPrintDevice.setFingerprintId(value);
                this.userFingerPrintDeviceManager.update(userFingerPrintDevice);
            } else {
                userFingerPrintDevice = new EdsUserFingerPrintDevice();
                userFingerPrintDevice.setUser(user);
                userFingerPrintDevice.setDeviceId(item[1]);
                userFingerPrintDevice.setFingerprintId(value);
                this.userFingerPrintDeviceManager.create(userFingerPrintDevice);
            }
        }
    }

    private void copySteps(final Integer fromCompanyID, final Integer toCompanyID, final List<EdsOnboardingStep> oldSteps, final ArrayList<Integer> createdIDs, final Integer parentID, final EdsOnboardingStep newParent) {
        final boolean one = oldSteps.size() == 1;
        for (final EdsOnboardingStep temp : oldSteps) {
            if (!createdIDs.contains(temp.getObjectID())) {
                ServerSecurityContext.getInstance().setCompanyId(fromCompanyID);
                if (one || (temp.getParent() == null && parentID == null) || (parentID != null && temp.getParent() != null && parentID.equals(temp.getParent().getObjectID()))) {
                    ServerSecurityContext.getInstance().setCompanyId(toCompanyID);
                    EdsOnboardingStep s = this.onboardingStepManager.getByName(temp.getName());
                    if (s == null) {
                        s = new EdsOnboardingStep();
                        s.setName(temp.getName());
                        s.setDescription(temp.getDescription());
                        s.setFormID(temp.getFormID());
                        s.setCreateForm(temp.isCreateForm());
                        s.setViewName(temp.getViewName());
                        s.setShowInEmployeeProfile(temp.getShowInEmployeeProfile());
                        ServerSecurityContext.getInstance().setCompanyId(fromCompanyID);
                        if (temp.getStatus() != null) {
                            final Integer id = temp.getStatus().getObjectID();
                            final String code = temp.getStatus().getCode();
                            final String name = temp.getStatus().getName();
                            ServerSecurityContext.getInstance().setCompanyId(toCompanyID);
                            EdsReference newStatus = this.referenceManager.findReference(EdsOnboardingStep._ONBOARDING_STEP_STATUSES, code);
                            if (newStatus == null) {
                                newStatus = new EdsReference();
                                newStatus.setParent(this.referenceManager.getByCode(EdsOnboardingStep._ONBOARDING_STEP_STATUSES));
                                newStatus.setCode(code);
                                newStatus.setName(name);
                                newStatus.setSystemReference(true);
                                newStatus.setRemovable(false);
                                this.referenceManager.create(newStatus);
                                s.setStatus(newStatus);
                                this.referenceManager.copyStepStatuses(id, newStatus.getObjectID(), fromCompanyID, toCompanyID);
                            } else {
                                s.setStatus(newStatus);
                                final List<EdsReference> childs = this.referenceManager.listReferences(code);
                                if (childs == null || childs.size() == 0) {
                                    this.referenceManager.copyStepStatuses(id, newStatus.getObjectID(), fromCompanyID, toCompanyID);
                                }
                            }
                        }
                        if (temp.getViewName() != null && !"".equals(temp.getViewName())) {
                            final String viewName = ViewName.OnboardingStep.name();
                            final List<EdsCompanyCustomFieldsSettings> cfS = this.companyCFManager.getCompanyCustomFieldsWithCategory(viewName, temp.getViewName());
                            if (cfS == null || cfS.size() == 0) {
                                this.companyCFManager.copyCustomFields(fromCompanyID, toCompanyID, viewName, temp.getViewName());
                            }
                        }
                        if (temp.isCreateForm() && temp.getFormID() != null && this.modelManager.get(temp.getFormID()) == null) {
                            this.modelManager.copyForm(fromCompanyID, toCompanyID, temp.getFormID());
                        }
                        s.setParent(newParent);
                        this.onboardingStepManager.create(s, true);
                    }
                    createdIDs.add(temp.getObjectID());
                    if (!one) {
                        this.copySteps(fromCompanyID, toCompanyID, oldSteps, createdIDs, temp.getObjectID(), s);
                    }
                }
            }
        }
    }

    @Override
    public SelectItem[] getOnboaringStepList() {
        final List<EdsOnboardingStep> onboardingStepList = this.onboardingStepManager.getOnboardingStepList(null);
        int i = 0;
        final SelectItem[] onboardingStepItems = new SelectItem[onboardingStepList.size()];
        for (final EdsOnboardingStep onboardingStep : onboardingStepList) {
            onboardingStepItems[i] = onboardingStep.getAsSelectItem();
            i++;
        }
        return onboardingStepItems;
    }

    public ListResult<OnboardingItem> getOnboardingStepdList(final ListingFilterParameter fp) {
        if (fp.getCompanyID() != null && fp.getCompanyID() > 0) {
            ServerSecurityContext.getInstance().setCompanyId(fp.getCompanyID());
        }

        final ListPanelToolRpc panelTools = fp.getListPanelTool();
        if (panelTools != null && panelTools.isCustomFieldsShown()) {
            fp.setCustomFieldsShown(true);
            panelTools.setListViewCustomFields(this.commonService.getCompanyCustomFieldsForListView(ViewName.OnboardingStep));
        }

        final List<EdsOnboardingStep> onboardingStepList = this.onboardingStepManager.getOnboardingStepList(fp);
        final Integer count = this.onboardingStepManager.getOnboardingStepTotalCount(fp);
        final ArrayList<OnboardingItem> onboardingItem = new ArrayList<>();
        onboardingStepList.forEach(onboardingStep -> {
            final OnboardingItem item = onboardingStep.getRPC();
            final StringBuilder roles = new StringBuilder();
            if (onboardingStep.getRoles() != null && onboardingStep.getRoles().size() > 0) {
                for (final EdsRole role : onboardingStep.getRoles()) {
                    roles.append(roles.toString() != "" ? ", " : "").append(role.getName());
                }
            }
            item.setSelectedRoles(roles.toString());
            if (fp.isCustomFieldsShown()) {
                item.setCustomFieldsMap(CustomFieldsUtils.getRPCCustomFields(onboardingStep.getOnboardingStepCustomFields(), fp.getListPanelTool().getColumnCodeName()));
            }
            onboardingItem.add(item);
        });
        return new ListResult<>(onboardingItem, count);
    }

    @Override
    public ArrayList<OnboardingItem> getOnboardingStepsForListing() {
        final ListingFilterParameter fp = new ListingFilterParameter();
        fp.setShowInListing(true);
        final List<EdsOnboardingStep> onboardingStepList = this.onboardingStepManager.getOnboardingStepList(fp);
        final ArrayList<OnboardingItem> items = onboardingStepList.stream().map(EdsOnboardingStep::getRPCShort).collect(Collectors.toCollection(ArrayList::new));
        return items;
    }

    @Override
    public RemindersDataRpc getOnboardingDataForReminders(final RemindersDataRpc rpc) {
        final RemindersDataRpc to = new RemindersDataRpc();
        final ListingFilterParameter fp = rpc.getFilterParameter();
        fp.setUseSelectedDate(true);
        final ListResult<OnboardingItem> onboardingList = this.getOnboardingStepdList(rpc.getFilterParameter());
        final ArrayList<EmployeeStepItem> steps = new ArrayList<>();
        if (onboardingList != null && onboardingList.getList() != null) {
            for (final OnboardingItem item : onboardingList.getList()) {
                boolean hasCustomField = false;
                final EmployeeStepItem employeeStepItem = new EmployeeStepItem();
                employeeStepItem.setObjectID(item.getStepId());
                employeeStepItem.setStepID(item.getStepId());
                employeeStepItem.setStepName(item.getStepName());
                final List<SelectItem> onboardingCustomField = new ArrayList<>();
                if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                    for (final CompanyCustomFieldItem cfItem : item.getCustomFieldItems()) {
                        if (cfItem.getDataType() != null && cfItem.getDataType().equals("Date")) {
                            onboardingCustomField.add(new SelectItem(cfItem.getFileUploadFieldId(), cfItem.getFieldName(), cfItem.getColumnCode()));
                            hasCustomField = true;
                        }
                    }
                }
                employeeStepItem.setOnboardingCustomFieldItems(onboardingCustomField.toArray(new SelectItem[]{}));
                if (hasCustomField) {
                    steps.add(employeeStepItem);
                }
            }
            to.setEmployeeStepItem(steps);
        }

        if (rpc.getFilterParameter().getCompanyID() != null) {
            ServerSecurityContext.getInstance().setCompanyId(rpc.getFilterParameter().getCompanyID());
        }

        final List<EdsCompanyCustomFieldsSettings> customFieldsSettings = this.companyCFManager.getCustomFields(rpc.getFilterParameter());
        if (customFieldsSettings != null && customFieldsSettings.size() > 0) {
            final SelectItem[] customFieldItem = new SelectItem[customFieldsSettings.size()];

            int index = 0;
            for (final EdsCompanyCustomFieldsSettings field : customFieldsSettings) {
                customFieldItem[index] = new SelectItem(field.getObjectID(), field.getFieldName(), field.getColumnCode());
                index++;
            }
            to.setCustomField(customFieldItem);
        }

        final List<EdsEmailTemplate> emailTemplates = this.emailTemplateManager.getEmailTemplatesByCategory(rpc.getCategoryCode());
        EdsEmailTemplate defaultEmailTemplate = this.emailTemplateManager.getCompanyDefaultEmailTemplatesByCategory(rpc.getCategoryCode());

        if (defaultEmailTemplate == null) {
            defaultEmailTemplate = this.emailTemplateManager.getDefaultEmailTemplateByCategory(rpc.getCategoryCode());
        }

        final ArrayList<SelectItem> emailTemplateItem = new ArrayList<>();

        if (defaultEmailTemplate != null) {
            emailTemplateItem.add(new SelectItem(defaultEmailTemplate.getObjectID(), defaultEmailTemplate.getName()));
        }

        for (final EdsEmailTemplate emailTemplate : emailTemplates) {
            if (!emailTemplate.equals(defaultEmailTemplate)) {
                emailTemplateItem.add(new SelectItem(emailTemplate.getObjectID(), emailTemplate.getName()));
            }
        }
        to.setEmailTemplate(emailTemplateItem);

        final List<EdsRole> roles = this.roleManager.getListWithEntitySpecificRoles();
        roles.forEach(role -> to.getRole().add(role.getAsSelectItem()));

        final ListResult<WorkflowRule> workflowRuleList = this.profileService.listWorkflowRules(rpc.getFilterParameter());
        if (workflowRuleList != null && workflowRuleList.getList() != null && workflowRuleList.getList().size() > 0) {
            for (final WorkflowRule list : workflowRuleList.getList()) {
                to.getWorkflowRule().add(new SelectItem(list.getObjectID(), list.getName()));
            }
        }
        return to;
    }

    @Override
    public ListResult<EmployeeStepItem> getEmployeeStepList(final ListingFilterParameter filterParameter) {
        FacetFilterRpc stepEmployeeFacetFilter = filterParameter.getFacetFilter();
        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        if (panelTools == null) {
            final ArrayList<String> columnCodeName = EmployeeStepItem.defaultColumnNames;
            panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(columnCodeName);
        }
        if (panelTools.isCustomFieldsShown()) {
            filterParameter.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(this.commonService.getCompanyStepCategoryCustomFields(filterParameter.getStepID()));
        }
        if (stepEmployeeFacetFilter != null && !stepEmployeeFacetFilter.isFilterChanges()) {
            stepEmployeeFacetFilter = this.commonServiceLocal.getUserFacetFilter(stepEmployeeFacetFilter);
        }
        filterParameter.setColumnsOfListing(panelTools.getColumnCodeName());
        final EdsUser edsUser = this.employeeManager.getUser();
        final EdsCompany edsCompany = edsUser.getCompany();

        String solrQuery = QueryBuilderForSolr.getEmployeeStepCoreSolrQuery(edsUser, filterParameter) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(stepEmployeeFacetFilter, edsCompany,
                        SolrEmployeeStepRepresenter.FIELD_CREATION_DATE,
                        SolrEmployeeStepRepresenter.FIELD_MODIFICATION_DATE);
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsStepEmployee.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Get Employee Step list");
        return this.getEmployeeStepListResponse(solrQuery, filterParameter);
    }

    private ListResult<EmployeeStepItem> getEmployeeStepListResponse(final String solrQuery, final ListingFilterParameter filterParameter) {
        Page<EmployeeStepSolrDoc> employeeStepSolrDocPage = employeeStepSolrComponent.getList(filterParameter, solrQuery);
        return getStepFromSolrResult(employeeStepSolrDocPage, filterParameter);
    }

    private ListResult<EmployeeStepItem> getStepFromSolrResult(Page<EmployeeStepSolrDoc> employeeStepSolrDocPage, ListingFilterParameter filterParameter) {
        int totalNumber = (int) employeeStepSolrDocPage.getTotalElements();
        ArrayList<EmployeeStepItem> stepItems = new ArrayList<>();
        int i = 0;
        if (employeeStepSolrDocPage != null && employeeStepSolrDocPage.getContent() != null) {
            boolean hasApprover = false;
            if (filterParameter.getStepID() != null) {
                final EdsOnboardingStep edsStep = this.onboardingStepManager.get(filterParameter.getStepID());
                hasApprover = this.approverManager.list(edsStep.getFormID(), null).size() > 0;
            }
            List<Integer> opportunityIds = employeeStepSolrDocPage.getContent().stream().map(EmployeeStepSolrDoc::getStepId).collect(Collectors.toList());
            String existingStepIDs = stepEmployeeManager.getStepIDsBySolrIDs(opportunityIds);
            final HashMap<Integer, String> approvestatues = this.stepEmployeeManager.getApproversStatus(existingStepIDs);

            for (EmployeeStepSolrDoc doc : employeeStepSolrDocPage.getContent()) {
                if (doc != null) {
                    final EmployeeStepItem step = this.getStepSolrDocumentAsRPC(doc, filterParameter, approvestatues, hasApprover);
                    stepItems.add(step);
                }
            }
        }
        final ListResult<EmployeeStepItem> result = new ListResult<>(stepItems, totalNumber);
        if (filterParameter.getStepID() != null) {
            result.setDefaultOne(this.getEmployeeStep(null, filterParameter.getStepID()));
        }
        return result;

    }

    private EmployeeStepItem getStepSolrDocumentAsRPC(EmployeeStepSolrDoc doc, ListingFilterParameter fp, HashMap<Integer, String> approvestatues, boolean hasApprover) {
        final EmployeeStepItem item = new EmployeeStepItem();
        Integer objectId = doc.getStepId();
        item.setObjectID(objectId);
        item.setArchived(doc.getArchived());
        item.setStepID(doc.getOnboardingStepId());
        item.setFormID(doc.getOnboardingStepFormId());
        item.setStepName(doc.getOnboardingStepName());
        item.setStatusID(doc.getStatusId());
        item.setStatusName(doc.getStatusName());
        item.setAssignStatues(doc.getStatusName());
        item.setTypeID(doc.getTypeId());
        item.setTypeName(doc.getTypeName());
        item.setTypeCode(doc.getTypeCode());
        item.setEmployeeID(doc.getEmployeeId());
        item.setEmployeeName(doc.getEmployeeName());
        item.setEmployeeCode(doc.getEmployeeCode());
        item.setCandidateCode(doc.getCandidateCode());
        item.setLocationID(doc.getEmployeeLocationId());
        item.setLocation(doc.getEmployeeLocationName());
        item.setCreatorID(doc.getCreatorId());
        item.setCreatorName(doc.getCreatorName());
        item.setCreationDate(doc.getCreationDate());
        item.setUpdatedDate(doc.getModificationDate());
        if (fp.getListPanelTool() != null) {
            item.setCustomFieldsMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(doc, fp.getListPanelTool().getColumnCodeName()));
        }
        Integer currentApprover = doc.getCurrentApproverId();
        Integer approverApproveStatusId = doc.getApproverApproveStatusId();
        Integer approverRejectStatusId = doc.getApproverRejectStatusId();

        if (hasApprover) {
            item.setAssignStatues(approvestatues.get(objectId));
            item.setHasApprover(true);
            if (currentApprover != null) {
                item.setCanApprove(currentApprover.equals(this.userManager.getUser().getObjectID()));
                item.setAppoveStatusId(approverApproveStatusId);
                item.setRejectStatusId(approverRejectStatusId);
            }
        }
        return item;
    }

    @Override
    public int saveEmployeeStep(final EmployeeStepItem item) {
        List<EdsStepEmployee> steps = new ArrayList<>();
        final EdsUser user = this.stepEmployeeManager.getUser();
        EdsStepEmployee employeeStep = new EdsStepEmployee();
        if (item.getObjectID() != null) {
            employeeStep = this.stepEmployeeManager.get(item.getObjectID());
        }
        employeeStep.clear();
        employeeStep.setType(this.referenceManager.findReference(EdsStepEmployee._STEP_TYPES, item.getTypeCode()));
        if (item.getEmployeeID() != null) {
            if (EmployeeStepItem.EMPLOYEE_TYPE.equals(item.getTypeCode())) {
                employeeStep.setEmployee(this.employeeManager.get(item.getEmployeeID()));
                employeeStep.setCandidate(null);
            } else {
                employeeStep.setEmployee(null);
                employeeStep.setCandidate(this.crmContactManager.get(item.getEmployeeID()));
            }
            steps = this.stepEmployeeManager.archiveOthers(item.getEmployeeID(), item.getObjectID(), item.getStepID(), item.getTypeCode());
        }
        if (item.getStatusID() != null && !isOk(item.getApprovers())) {
            employeeStep.setEntityStatus(this.referenceManager.get(item.getStatusID()));
        }
        if (item.getStepID() != null) {
            employeeStep.setOnboardingStep(this.onboardingStepManager.get(item.getStepID()));
        }
        employeeStep.getExpenseClaims().clear();
        for (final SelectItem it : item.getExpenses()) {
            employeeStep.getExpenseClaims().add(this.expenseReportManager.get(it.getId()));
        }
        if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
            final StringBuilder changes = new StringBuilder();
            for (final CompanyCustomFieldItem cit : item.getCustomFieldItems()) {
                changes.append(employeeStep.getEmployeeStepCustomFields() != null && CustomFieldsUtils.getObjectValue(employeeStep.getEmployeeStepCustomFields(), cit.getColumnCode()) != null ? this.getChanges(CustomFieldsUtils.getObjectValue(employeeStep.getEmployeeStepCustomFields(), cit.getColumnCode()), cit) : (cit.getColumnCode() + ","));
            }
            if (!"".contentEquals(changes)) {
                employeeStep.addCustomFieldChanges(changes.toString());
            }
        }
        final EdsEmployeeStepCustomFields employeeStepCustomFields = this.createEmployeeStepCustomFields(item.getCustomFieldItems());
        employeeStep.setEmployeeStepCustomFields(employeeStepCustomFields);

        employeeStep.setWorkflowID(item.getWorkflowID());
        employeeStep.setWorkflowItem(item.getWorkflowID() != null);
        final boolean isNew = this.stepEmployeeManager.createOrUpdate(employeeStep);
        steps.add(employeeStep);
        try {
            employeeStepSolrComponent.indexes(steps);
        } catch (final SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Integer startStatusId = null;
        final EdsReference notDefinedStatus = this.referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.NOT_DEFINED);
        if (isOk(item.getApprovers())) {
            item.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));

            for (final ApproverItemMini approverItem : item.getApprovers()) {
                final EdsApprover _edsApprover = this.approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    this.approverManager.update(_edsApprover);
                    if (item.getStatusID() != null) {
                        employeeStep.updateStatus(this.referenceManager.get(item.getStatusID()));
                    }
                    if (startStatusId == null) {
                        startStatusId = _edsApprover.getStartStatusID();
                    }
                    continue;
                }

                final EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(employeeStep.getObjectID());
                edsApprover.setIs_default(false);
                edsApprover.setStatus(edsApprover.getStartStatusID() != null ? this.referenceManager.get(edsApprover.getStartStatusID()) : notDefinedStatus);
                employeeStep.setEntityStatus(edsApprover.getStartStatusID() != null ? this.referenceManager.get(edsApprover.getStartStatusID()) : notDefinedStatus);
                if (startStatusId == null) {
                    startStatusId = edsApprover.getStartStatusID();
                }
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                    edsApprover.setExactEmployee(user_);
                }
                edsApprover.setApproverRoles(new HashSet<>());
                edsApprover.setApproverEmployees(new HashSet<>());
                edsApprover.setDynamicQueries(new HashSet<>());
                this.approverManager.createOrUpdate(edsApprover);

                for (final EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                    edsApprover.getApproverRoles().add(roleapp);
                }

                for (final EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                    edsApprover.getApproverEmployees().add(ucerapp);
                }

                if (employeeStep.getCurrentApprover() == null) {
                    employeeStep.setCurrentApprover(edsApprover);
                }
                employeeStep.getApprovers().add(edsApprover);
            }
        }
        this.stepEmployeeManager.update(employeeStep);

        final boolean isCurrentApproverApproved = employeeStep.isCurrentApproverApproved();
        final boolean isCurrentApproverRejected = employeeStep.isCurrentApproverRejected();
        if (isOk(employeeStep.getStatus())) {
            if (!isCurrentApproverApproved && !isCurrentApproverRejected && startStatusId != null && startStatusId.equals(employeeStep.getStatus().getObjectID())) {
                this.baseEventPostProcessor.registerEvent(EmployeeStepCustomEventListenerImpl.TYPE, EmployeeStepCustomEventListenerImpl.STATUS_SUBMITTED, employeeStep, this.userManager.getUser());
            } else if (isCurrentApproverApproved) {
                this.baseEventPostProcessor.registerEvent(EmployeeStepCustomEventListenerImpl.TYPE, EmployeeStepCustomEventListenerImpl.STATUS_APPROVED, employeeStep, this.userManager.getUser());
            } else if (isCurrentApproverRejected) {
                this.baseEventPostProcessor.registerEvent(EmployeeStepCustomEventListenerImpl.TYPE, EmployeeStepCustomEventListenerImpl.STATUS_REJECTED, employeeStep, this.userManager.getUser());
            }
        }

        if (item.getWorkflowID() == null) {
            /* add workflow event to start workflow rule... */
            if (isOk(item.getApprovers())) {
                final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), employeeStep, user);
                workflowEvent.setEntityType(RelationItem.TYPE_EMPLOYEE_STEP);
                workflowEvent.setCustomStringField(isNew ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT);
            } else {
                final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, isNew ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employeeStep, user);
                workflowEvent.setEntityType(employeeStep.getOnboardingStep().getFormID());
            }
        }

        return employeeStep.getObjectID();
    }

    private String getChanges(final Object ob, final CompanyCustomFieldItem item) {
        if (ob != null) {
            if (Constants.DATA_TYPE_TEXT.equals(item.getDataType())) {
                final String text = (String) ob;
                return !text.equals(item.getFieldStringValue()) ? (item.getColumnCode() + ",") : "";
            } else if (Constants.DATA_TYPE_NUMBER.equals(item.getDataType())) {
                final String s = String.valueOf(((Double) ob).intValue());
                return !s.equals(item.getFieldStringValue()) ? (item.getColumnCode() + ",") : "";
            } else if (Constants.DATA_TYPE_DATE.equals(item.getDataType())) {
                final Date date = (Date) ob;
                return !date.equals(item.getFieldDateNonConvertedValue() != null ? item.getFieldDateNonConvertedValue().getNonConvertedDate() : null) ? (item.getColumnCode() + ",") : "";
            }
        }
        return "";
    }

    @Override
    public EmployeeStepItem getEmployeeStep(final Integer objectID, final Integer stepID) {
        final EmployeeStepItem item = new EmployeeStepItem();
        if (stepID != null) {
            final EdsOnboardingStep step = this.onboardingStepManager.get(stepID);
            item.setStepName(step.getName());
            if (step.getStatus() != null) {
                item.setStatuses(ServerUtils.getAsSelectItem(this.referenceManager.listReferences(step.getStatus().getCode()), ServerUtils.REFERENCE));
            }

            final List<EdsApprover> approvers = this.approverManager.list(step.getFormID(), null);
            if (approvers != null) {
                item.setHasApprover(approvers.size() > 0);
                if (approvers.size() > 0) {
                    item.setStatusID(approvers.get(0).getStartStatusID());
                }
            }
        }
        if (objectID != null) {
            final EdsStepEmployee stepEmployee = this.stepEmployeeManager.get(objectID);
            final EdsEmployeeStepCustomFields edsEmployeeStepCustomFields = stepEmployee.getEmployeeStepCustomFields();
            final ArrayList<CompanyCustomFieldItem> customFieldsItems = this.commonService.getCompanyStepCategoryCustomFields(stepID);
            item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsEmployeeStepCustomFields, customFieldsItems));
            if (stepEmployee.getCurrentApprover() != null) {
                if (stepEmployee.getCurrentApprover().getExactEmployee() != null && stepEmployee.getCurrentApprover().getExactEmployee().getObjectID().equals(this.userManager.getUser().getObjectID())) {
                    item.setCanApprove(true);
                }
                item.setAppoveStatusId(stepEmployee.getCurrentApprover().getApproveStatusID());
                item.setRejectStatusId(stepEmployee.getCurrentApprover().getRejectStatusID());
            }
            return stepEmployee.getRPC(item);
        }
        final Integer currentUserId = this.userManager.getUser().getObjectID();
        final EdsEmployee edsEmployee = this.employeeManager.get(currentUserId);
        if (edsEmployee != null) {
            item.setCurrentUserID(edsEmployee.getObjectID());
            final String code = edsEmployee.getProfile() != null ? edsEmployee.getProfile().getEmployeeCode() : null;
            final String employeeName = (code != null && !"".equals(code) ? code + " - " : "") + edsEmployee.getName();
            item.setCurrentUserName(employeeName + this.referenceWfmMessageSource.localize("mySelf", " (" + Constants.MYSELF + ")"));
        }
        return item;
    }

    @Override
    public void deleteEmployeeStep(final Integer objectID) {
        final EdsUser user = this.stepEmployeeManager.getUser();
        final EdsStepEmployee stepEmployee = this.stepEmployeeManager.get(objectID);
        stepEmployee.setDeleted(true);
        this.stepEmployeeManager.update(stepEmployee);
        try {
            this.solrManager.removeEmployeeSteps(objectID);
        } catch (final IOException | SolrServerException e) {
            e.printStackTrace();
        }
        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, stepEmployee, user);
        workflowEvent.setEntityType(stepEmployee.getOnboardingStep().getFormID());
    }

    @Override
    public void saveHrReminderItems(final ArrayList<HrReminderItem> reminderTOs) {
        final Integer objId = 1;
        this.hrReminderSettingsManager.deleteHrReminders();
        for (final HrReminderItem reminderTO : reminderTOs) {
            final EdsHrReminderSettings hrReminderSettings = new EdsHrReminderSettings();
            hrReminderSettings.setEntityType(reminderTO.getEntityType());
            hrReminderSettings.setFieldValue(reminderTO.getFieldValue());
            hrReminderSettings.setFieldcode(reminderTO.getFieldCode());
            if (reminderTO.getOnboardingStepId() != null) {
                hrReminderSettings.setOnboardingStepId(reminderTO.getOnboardingStepId());
            }
            if (reminderTO.getEmailTemplateId() != null) {
                hrReminderSettings.setTemplate(this.emailTemplateManager.get(reminderTO.getEmailTemplateId()));
            }

            EdsHrReminderTimeAction edsTimeAction;
            hrReminderSettings.getTimeActions().clear();
            if (reminderTO.getActionTimes() != null && reminderTO.getActionTimes().size() > 0) {
                for (final ActionTimesTO actiontime : reminderTO.getActionTimes()) {
                    edsTimeAction = new EdsHrReminderTimeAction();
                    edsTimeAction.setActiontype(actiontime.getActiontype());
                    if (actiontime.getActionNumber() != null && !"".equals(actiontime.getActionNumber())) {
                        edsTimeAction.setActionNumber(Integer.valueOf(actiontime.getActionNumber()));
                    }
                    edsTimeAction.setActionPeriod(actiontime.getActionPeriod());
                    hrReminderSettings.getTimeActions().add(edsTimeAction);
                }
            }

            hrReminderSettings.getRoles().clear();
            if (reminderTO.getRoleItems() != null && reminderTO.getRoleItems().size() > 0) {
                final Set<EdsRole> roles = hrReminderSettings.getRoles();
                for (final SelectItem roleItem : reminderTO.getRoleItems()) {
                    final EdsRole hr_role = this.roleManager.get(roleItem.getId());
                    roles.add(hr_role);
                    hrReminderSettings.setRoles(roles);
                }
            }

            hrReminderSettings.getWorkflowRules().clear();
            if (reminderTO.getWorkFlowItems() != null && reminderTO.getWorkFlowItems().size() > 0) {
                final Set<EdsWorkflowRule> roles = hrReminderSettings.getWorkflowRules();
                for (final SelectItem wokflowItem : reminderTO.getWorkFlowItems()) {
                    final EdsWorkflowRule wf_role = this.workflowRuleManager.get(wokflowItem.getId());
                    roles.add(wf_role);
                    hrReminderSettings.setWorkflowRules(roles);
                }
            }
            this.hrReminderSettingsManager.createOrUpdate(hrReminderSettings);
        }
        final EdsRecurrence existingRecurrence = this.recurrenceService.getRecurrenceByJobId(objId, SchedulerConstant.DAILY_HR_REMINDERP_ROCEDURE_JOB);
        if (existingRecurrence == null) {
            this.createDailyHrReminderProcedureJob(objId);
        }
    }

    public ListResult<HrReminderItem> getHrReminderItems() {
        final Integer companyID = this.userManager.getUser().getCompany().getObjectID();
        final List<EdsHrReminderSettings> reminderSettingsList = this.hrReminderSettingsManager.getReminders(companyID);
        final ArrayList<HrReminderItem> itemList = new ArrayList<>();
        int totalcount = 0;
        for (final EdsHrReminderSettings reminderSettings : reminderSettingsList) {
            itemList.add(reminderSettings.getRPC());
            totalcount++;
        }
        return new ListResult<>(itemList, totalcount);
    }

    private void createDailyHrReminderProcedureJob(final Integer objId) {
        final RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
        recurrenceJobItem.setEnabled(true);
        recurrenceJobItem.setType(SchedulerConstant.RECURRENCE_TYPE_DAILY);
        recurrenceJobItem.setJobType(SchedulerConstant.DAILY_HR_REMINDERP_ROCEDURE_JOB);
        recurrenceJobItem.setBusObjectId(objId);
        recurrenceJobItem.setBusObjectParams("DAILY_HR_REMINDERP_ROCEDURE_JOB");
        recurrenceJobItem.setInterval(1);
        recurrenceJobItem.setDailyPatternOptions(SchedulerConstant.DAILY_PATTERN_OPTION_INTERVAL);
        recurrenceJobItem.setEndType(SchedulerConstant.NO_END_DATE);
        recurrenceJobItem.setStartDate(new Date());
        this.recurrenceService.saveRecurrenceJob(recurrenceJobItem);
    }

    public void onboardingPeriodDelete(final Integer objectID) {
        final EdsOnboardingPeriod period = this.onboardingPeriodManager.get(objectID);
        period.setDeleted(true);
        this.onboardingPeriodManager.update(period);
    }

    public void onboardingStepDelete(final Integer objectID) {
        final EdsOnboardingStep step = this.onboardingStepManager.get(objectID);
        step.setDeleted(true);
        if (step.getStatus() != null) {
            this.deleteReferences(this.referenceManager.listReferences(step.getStatus().getCode()));
            step.setStatus(null);
        }
        if (step.isCreateForm()) {
            final Integer companyId = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
            final String code = step.getName().replaceAll("[^\\p{L}\\p{Nd}]|[\\p{InLatin-1Supplement}]+", "").toUpperCase();
            this.reportingServiceLocal.deleteOnboardingReportTemplate(code, companyId);
        }
        if (step.getFormID() != null) {
            this.commonServiceLocal.createWorkflowModule(step.getFormID().replaceAll(Constants.ONBOARDING_STEP_FORM, ""), step.getName(), false);
            this.createPermissions(step.getFormID().replaceAll(Constants.ONBOARDING_STEP_FORM, ""), step.getName(), false);
        }
        this.onboardingStepManager.updateChild(objectID);
        this.companyCFManager.deleteStepCustomFieldPermissions(step.getViewName());
        this.companyCFManager.deleteStepCustomFields(step.getViewName());
        this.onboardingStepManager.update(step);
    }

    private void deleteReferences(final List<EdsReference> statuses) {
        if (statuses != null && statuses.size() > 0) {
            this.stepEmployeeManager.updateStepStatuses(EdsObject.getObjectIDs(statuses));
            for (final EdsReference ref : statuses) {
                ref.setDeleted(true);
                this.referenceManager.update(ref);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<DependentItem> getDependentsList(final ListingFilterParameter fp) {
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsDependent.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Dependent list");

        if (fp.getEmployeeId() == null && fp.getContactID() == null) {
            final EdsUser currentUser = this.userManager.getUser();
            fp.setEmployeeId(currentUser.getObjectID());
        }
        List<EdsDependent> dependents = this.dependentManager.getDependentList(fp);
        for (EdsDependent item : dependents) {
            if (item.getRelationship() != null)
                item.setRelationship(hrmsLocalizer.localizeRef(referenceManager.getByName(item.getRelationship())));
            else
                item.setRelationship(item.getRelationship());
        }
        final int totalCount = dependents.size();
        if (fp.getLimit() != 0) {
            dependents = ListUtils.getSublist(dependents, fp.getStart(), fp.getLimit());
        }

        final ListPanelToolRpc panelTools = fp.getListPanelTool();
        if (panelTools != null && panelTools.isCustomFieldsShown()) {
            fp.setCustomFieldsShown(true);
            panelTools.setListViewCustomFields(this.commonService.getCompanyCustomFieldsForListView(ViewName.Dependent));
        }
        final ArrayList<DependentItem> results = new ArrayList<>();
        for (final EdsDependent dependent : dependents) {
            final DependentItem dependentItem = dependent.getRPC();

            if (fp.isCustomFieldsShown()) {
                dependentItem.setCustomFieldsMap(CustomFieldsUtils.getRPCCustomFields(dependent.getCustomFields(), fp.getListPanelTool().getColumnCodeName()));
            }
            results.add(dependentItem);
        }
        return new ListResult<>(results, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DependentItem getDependent(final Integer objectId) {
        DependentItem dependentItem = new DependentItem();
        if (objectId != null) {
            final EdsDependent dependent = this.dependentManager.get(objectId);
            dependentItem = dependent.getRPC();
            boolean ref = false;
            final List<EdsReference> list = this.referenceManager.listReferences("RELATIONSHIP");
            for (final EdsReference edsReference : list) {
                final ReferenceItem referenceItem = edsReference.getRPC(this.roleManager.list());
                if (referenceItem.getOriginalName().equals(dependent.getRelationship())) {
                    EdsReference byCode = referenceManager.getByCode(referenceItem.getCode());
                    dependentItem.setRelationship(hrmsLocalizer.localizeRef(byCode));
                    ref = true;
                }
            }
            if (!ref) {
                dependentItem.setRelationship(hrmsLocalizer.localize(dependent.getRelationship(), dependent.getRelationship()));
            }
            dependentItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(dependent.getCustomFields(),
                    this.commonService.getCompanyCustomFields(ViewName.Dependent)));
        }
        return dependentItem;
    }

    public void saveDependent(final DependentItem item) {
        EdsEmployee employee = new EdsEmployee();
        EdsCrmContact candidate = null;

        if (item.isFromCandidate() && item.getCandidateId() != null) {
            candidate = crmContactManager.get(item.getCandidateId());
        } else {
            if (item.getEmployeeId() == null) {
                final EdsUser currentUser = this.employeeManager.getUser();
                employee = this.employeeManager.get(currentUser.getObjectID());
            } else {
                employee = this.employeeManager.get(item.getEmployeeId());
            }
        }

        final EdsDependent dependent;
        if (item.getObjectId() == null) {
            dependent = new EdsDependent();
        } else {
            dependent = this.dependentManager.get(item.getObjectId());
        }

        if (item.getObjectId() == null && !item.isFromCandidate()) {
            dependent.setUser(employee);
        }
        if (candidate != null) {
            dependent.setCandidate(candidate);
        }

        dependent.setFirstName(item.getFirstName());
        dependent.setLastName(item.getLastName());
        dependent.setMiddleName(item.getMiddleName());
        dependent.setRelationship(item.getRelationship());
        dependent.setAddress(item.getAddress());
        dependent.setAddressb(item.getAddressb());
        dependent.setCity(item.getCity());
        dependent.setTown(item.getTown());

        if (item.getCountryId() != null) {
            dependent.setCountry(this.countryManager.get(item.getCountryId()));
        } else {
            dependent.setCountry(null);
        }
        dependent.setPhone1(item.getPhone1());
        dependent.setPhone2(item.getPhone2());
        dependent.setCustomFields(this.saveDependentCustomFields(dependent.getCustomFields(), item.getCustomFields()));

        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsDependent.class.getSimpleName());

        final String message;
        final KpiLog.ActionType actionType;
        if (dependent.getObjectID() == null) {
            actionType = KpiLog.ActionType.ADD;
            message = "Add new dependent";
        } else {
            actionType = KpiLog.ActionType.UPDATE;
            message = "Update dependent";
        }

        this.dependentManager.createOrUpdate(dependent);
        if (item.getAttachments() != null && item.getAttachments().length > 0) {
            this.saveDependantAttachments(item.getAttachments(), dependent);
        }

        kpiLog.setActionType(actionType);
        kpiLog.setEntityId(dependent.getObjectID());
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, message);
    }

    private void saveDependantAttachments(final FileItem[] attachments, final EdsDependent dependent) {
        this.attachmentUtilsManager.saveAttachments(Constants.F_DEPENDENTS, dependent.getObjectID(), dependent.getObjectID(), attachments);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DependentItem editDependent(final Integer objectId) {
        DependentItem dependentItem = new DependentItem();
        if (objectId != null) {
            final EdsDependent dependent = this.dependentManager.get(objectId);
            dependentItem = dependent.getRPC();
            dependentItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(dependent.getCustomFields(),
                    this.commonService.getCompanyCustomFields(ViewName.Dependent)));
        }
        dependentItem.setCountry(this.getCountries());
        return dependentItem;
    }

    public String getEmployeeImageURL(final Integer employeeId) {
        final EdsUser user;
        if (employeeId != null)
            user = this.userManager.get(employeeId);
        else
            user = this.userManager.getUser();
        return this.getUserImageUrl(user);
    }

    public String getUserImageUrl(final EdsUser user) {
        if (user != null && user.getPhoto() != null)
            return this.getImageUrl(user.getPhoto().getObjectID());
        else
            return null;

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public GoalItem editGoal(final Integer objectId, final String type) {
        boolean isPersonalGoal = false;
        boolean isDepartmentGoal = false;
        boolean isProjectGoal = false;
        boolean isBussinessGoal = false;
        final GoalItem item = new GoalItem();
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();

        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectId);
        if (Constants.PERSONAL_GOAL.equals(type)) {
            isPersonalGoal = true;
            kpiLog.setEntityType(EdsGoal.PERSONAL_GOAL);
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "View Personal goal");
        } else if (Constants.DEPARTMENT_GOAL.equals(type)) {
            isDepartmentGoal = true;
            kpiLog.setEntityType(EdsGoal.DEPARTMENT_GOAL);
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "View department goal");
        } else if (Constants.PROJECT_GOAL.equals(type)) {
            isProjectGoal = true;
            kpiLog.setEntityType(EdsGoal.PROJECT_GOAL);
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "View Project goal");
        } else if (Constants.BUSINESS_GOAL.equals(type)) {
            isBussinessGoal = true;
            kpiLog.setEntityType(EdsGoal.BUSINESS_GOAL);
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "View Business goal");
        }

        final EdsReference goalReference;
        if (isPersonalGoal) {
            goalReference = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PERSONAL_GOAL);
            ListingFilterParameter filterParametrs = new ListingFilterParameter();
            filterParametrs.setRelationType(Constants.PROJECT_GOAL);
            SelectItem[] accountingRelatedProjects = allInOneService.getAccountingRelatedProjects(filterParametrs);
            item.setPersonalGoalId(goalReference.getObjectID());
            item.setProjectGoals(accountingRelatedProjects);
        } else if (isDepartmentGoal) {
            goalReference = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.DEPARTMENT_GOAL);
            item.setDepartmentGoalId(goalReference.getObjectID());
        } else if (isProjectGoal) {
            goalReference = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PROJECT_GOAL);
            item.setProjectGoalId(goalReference.getObjectID());
        } else if (isBussinessGoal) {
            goalReference = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.BUSINESS_GOAL);
            item.setBusinGoalId(goalReference.getObjectID());
        }

        item.setStatuss(this.commonServiceLocal.convertReference2SelectItem2(EdsTask.TASK_STATUS));
        item.setScores(ServerUtils.getAsSelectItem(this.referenceManager.listReferences("_SCORE_CALCULATION"), ServerUtils.REFERENCE));

        final ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStatusCode(ValidityPeriodItem.VALIDITY_PERIOD_GOAL);
        item.setValidityPeriodItems(this.validityPeriodManager.getValidityPeriods(fp));

        if (isDepartmentGoal) {
            final ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
            final EdsUser user = this.userManager.getUser();
            if (ServerUtils.hasPermission(SHOW_DEPARTMENT_EMPLOYEE_LIST) && !(user.hasRole(this.roleManager.get(EdsRole.ADMIN)) || ServerUtils.hasPermission(SHOW_ALL_EMPLOYEE_LIST))) {
                listingFilterParameter.setViewAsId(EdsRole.TL);
            } else {
                listingFilterParameter.setViewAsId(EdsRole.DR);
            }
            final ListResult<TeamListItem> departments = this.departmentService.getTeams(listingFilterParameter);
            final SelectItem[] result = new SelectItem[departments.getList().size()];

            int i = 0;
            for (final TeamListItem team : departments.getList()) {
                result[i] = new SelectItem();
                result[i].setId(team.getObjectID());
                result[i].setName(team.getName());
                i++;
            }
            item.setDepartments(result);

            SelectItem[] locations = this.locationManager.getLocationsAsSelecItem();
            item.setLocations(locations);
        }

        final List<EdsBusinessGoal> companyGoals = this.businessGoalManager.list(new ListingFilterParameter());
        SelectItem[] selectItems = new SelectItem[companyGoals.size()];
        int i = 0;
        for (final EdsBusinessGoal businessGoal : companyGoals) {
            final SelectItem it = new SelectItem();
            it.setId(businessGoal.getObjectID());
            it.setName(businessGoal.getTitle());
            selectItems[i++] = it;
        }
        item.setCompanyGoals(selectItems);
        //for prject goal
        if (isProjectGoal) {
            final List<EdsProject> projectList = this.projectManager.list(new ListingFilterParameter());
            selectItems = new SelectItem[projectList.size()];
            i = 0;
            for (final EdsProject edsProject : projectList) {
                final SelectItem it = new SelectItem();
                it.setId(edsProject.getObjectID());
                it.setName(edsProject.getName());
                selectItems[i++] = it;
            }
            item.setProjects(selectItems);
        }

        final Integer projectId;
        final ListingFilterParameter fp2 = new ListingFilterParameter();
        EdsGoal goal = null;
        if (objectId != null) {
            goal = this.goalManager.get(objectId);
            if (goal != null && !goal.isDeleted()) {
                if (goal.getValidityPeriod() != null) {
                    final EdsValidityPeriod validityPeriod = goal.getValidityPeriod();
                    item.setValidityPeriodItem(validityPeriod.getDTO());
                    fp2.setValidityPeriodId(validityPeriod.getObjectID());
                }
                if (Constants.PERSONAL_GOAL.equals(type)) {
                    Integer assigneeID = null;
                    if (goal.getUndeletedGoalAssignees() != null && !goal.getUndeletedGoalAssignees().isEmpty()) {
                        for (final EdsGoalAssignees assignees : goal.getUndeletedGoalAssignees()) {
                            if (assignees.getAssignee() != null) {
                                assigneeID = assignees.getAssignee().getObjectID();
                                break;
                            }
                        }
                    }
                    if (goal.getNumberData() != null) {
                        NumberData numberData = new NumberData();
                        numberData.setNumberString(goal.getNumberData());
                        numberData.setFirstNumberString(goal.getNumberData());
                        numberData.setNumberFormat("_");
                        item.setGoalNumber(numberData);
                    }
                    item.setSelectedEmployeeID(assigneeID);
                    if (goal.getProjectGoal() != null) {
                        item.setSelectedProjectGoalId(goal.getProjectGoal().getObjectID());
                        item.setProjectGoalTitle(goal.getProjectGoal().getNumberData() + " - " + goal.getProjectGoal().getTitle());
                        item.setProjectStartDate(new DateNonConvertable(goal.getProjectGoal().getFromDate()));
                        item.setProjectEndDate(new DateNonConvertable(ServerUtils.getEndDate(goal.getProjectGoal().getToDate())));
                    }
                }
                EdsUnitMeasurement measurementUnit = goal.getMeasurementUnit();
                if (measurementUnit != null) {
                    item.setMeasurementUnit(new SelectItem(measurementUnit.getObjectID(), measurementUnit.getName()));
                }
                if (goal.getProject() != null && Constants.PROJECT_GOAL.equals(type)) {
                    projectId = goal.getProject().getObjectID();
                    item.setProjectId(projectId);
                    item.setProject(goal.getProject().getName());
                    item.setProjectStartDate(new DateNonConvertable(goal.getProject().getStartDate()));
                    item.setProjectEndDate(new DateNonConvertable(ServerUtils.getEndDate(goal.getProject().getEndDate())));
                    fp2.setProjectId(projectId);
                    if (goal.getNumberData() != null) {
                        NumberData numberData = new NumberData();
                        numberData.setNumberString(goal.getNumberData());
                        numberData.setFirstNumberString(goal.getNumberData());
                        numberData.setNumberFormat("_");
                        item.setGoalNumber(numberData);
                    }

                    if (!goal.getUndeletedGoalAssignees().isEmpty()) {
                        final List<GoalAssigneeItem> list = goal.getUndeletedGoalAssignees().stream().map(EdsGoalAssignees::toAssigneItem).toList();
                        item.setGoalAssigneeItem(list.toArray(new GoalAssigneeItem[]{}));
                    }

                } else if (goal.getDepartment() != null && Constants.DEPARTMENT_GOAL.equals(type)) {
                    projectId = goal.getDepartment().getObjectID();
                    item.setDepartmentId(projectId);
                    item.setDepartment(goal.getDepartment().getName());
                    if (goal.getLocation() != null) {
                        item.setLocationId(goal.getLocation().getObjectID());
                        item.setLocation(goal.getLocation().getName());
                    }
                    item.setAvialableWeight(goalManager.getDepartmentGoalAvailableWeight(projectId));
                    item.setDepartmentGoalWeight(goal.getWeight());
                    item.setTargetGoal(goal.getTargetGoal());
                    fp2.setDepartmentId(projectId);
                    if (!goal.getUndeletedGoalAssignees().isEmpty()) {
                        final List<GoalAssigneeItem> list = goal.getUndeletedGoalAssignees().stream().map(EdsGoalAssignees::toAssigneItem).toList();
                        item.setGoalAssigneeItem(list.toArray(new GoalAssigneeItem[]{}));
                    }
                } else {
                    if (!goal.getUndeletedGoalAssignees().isEmpty()) {
                        item.setGoalAssigneeItem(goal.getUndeletedGoalAssignees()
                                .stream()
                                .map(EdsGoalAssignees::toAssigneItem)
                                .toArray(GoalAssigneeItem[]::new));
                    }
                }
            }
        } else {
            switch (type) {
                case Constants.PERSONAL_GOAL -> {
                    NumberData personalGoalNumber = availabilityService.generateGoalNumber(EdsGoal.PERSONAL_GOAL);
                    item.setGoalNumber(personalGoalNumber);
                }
                case Constants.PROJECT_GOAL -> {
                    NumberData projectGoalNumber = availabilityService.generateGoalNumber(EdsGoal.PROJECT_GOAL);
                    item.setGoalNumber(projectGoalNumber);
                }
            }
        }

        if (objectId != null) {
            if (goal != null && !goal.isDeleted()) {
                item.setObjectId(goal.getObjectID());
                item.setActionSteps(goal.getActionSteps());
                item.setDescription(goal.getDescription());
                item.setFromDate(new DateNonConvertable(goal.getFromDate()));
                item.setToDate(new DateNonConvertable(goal.getToDate()));
                if (goal.getGoalCategory() != null) {
                    item.setGoalCategoryId(goal.getGoalCategory().getObjectID());
                    item.setGoalCategory(this.referenceWfmMessageSource.localize(goal.getGoalCategory().getCode(), goal.getGoalCategory().getName()));
                }
                item.setProgress(goal.getProgress());
                if (goal.getResolver() != null) {
                    item.setResolverId(goal.getResolver().getObjectID());
                    item.setResolver(goal.getResolver().getFullName());
                }
                if (goal.getStatus() != null) {
                    item.setStatusId(goal.getStatus().getObjectID());
                    item.setStatus(this.referenceWfmMessageSource.localize(goal.getStatus().getCode(), goal.getStatus().getName()));
                }
                if (goal.getBusinessGoal() != null) {
                    item.setCompanyGoalId(goal.getBusinessGoal().getObjectID());
                    item.setCompanyGoal(goal.getBusinessGoal().getTitle());
                }

                item.setTitle(goal.getTitle());

                if (Constants.DEPARTMENT_GOAL.equals(type)) {
                    EdsDepartmentGoalChartSettings depGaol = departmentGoalChartSettingsManager.getByGoalId(goal.getObjectID());
                    item.setChartSettings(depGaol ==null ? null :depGaol.toDto());
                }

                if (goal.getCreator() != null) {
                    item.setCreatorId(goal.getCreator().getObjectID());
                } else {
                    item.setCreatorId(0);
                }
                item.setGoalAttachments(this.getGoalAttachments(goal));
                //set score
                final EdsReference reference = goal.getScoreCalculation();
                if (reference != null) {
                    final SelectItem selectItem = reference.getAsSelectItem();
                    item.setScore(selectItem);
                }
                final ViewName viewNameConst = isPersonalGoal
                        ? ViewName.PersonalGoal
                        : isDepartmentGoal
                        ? ViewName.DepartmentGoal
                        : isProjectGoal ? ViewName.ProjectGoal : ViewName.BusinessGoal;
                item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(goal.getGoalCustomFields(),
                        this.commonService.getCompanyCustomFields(viewNameConst)));

                if (isPersonalGoal) {
                    item.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_PERSONAL_GOAL, item.getObjectId())));
                } else if (isDepartmentGoal) {
                    item.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_DEPARTMENT_GOAL, item.getObjectId())));
                } else if (isProjectGoal) {
                    item.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_PROJECT_GOAL, item.getObjectId())));
                } else if (isBussinessGoal) {
                    item.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_BUSINESS_GOAL, item.getObjectId())));
                }
            }
        }
        return item;
    }

    @Override
    public GoalItem getGoal(Integer objectId, String type) {
        GoalItem item = new GoalItem();
        EdsGoal goal = null;
        if (objectId != null) {
            goal = this.goalManager.get(objectId);
        }
        if (goal != null) {
            if (Constants.PERSONAL_GOAL.equals(type)) {
                if (goal.getProjectGoal() != null) {
                    item.setSelectedProjectGoalId(goal.getProjectGoal().getObjectID());
                    item.setProjectGoalTitle(goal.getProjectGoal().getTitle());
                    item.setProjectStartDate(new DateNonConvertable(goal.getProjectGoal().getFromDate()));
                    item.setProjectEndDate(new DateNonConvertable(ServerUtils.getEndDate(goal.getProjectGoal().getToDate())));
                }
            }

            if (goal.getProject() != null && Constants.PROJECT_GOAL.equals(type)) {
                item.setProject(goal.getProject().getName());
                item.setProjectStartDate(new DateNonConvertable(goal.getFromDate()));
                item.setProjectEndDate(new DateNonConvertable(ServerUtils.getEndDate(goal.getToDate())));
            }
        }
        return item;
    }

    @Override
    public Boolean checkPositionAvailability(Integer positionId) {
        EdsPosition position = positionManager.get(positionId);
        if (!ServerUtils.isNullOrEmpty(position.getCount())) {
            int positionCount = Integer.parseInt(position.getCount());
            return positionCount > employeeManager.getEmployeesCountByPosition(position);
        }
        return true;
    }

    @Override
    public Boolean checkPositionAvailability(ArrayList<Integer> positionIds) {
        return positionIds.stream()
                .map(this::checkPositionAvailability)
                .reduce(Boolean.TRUE, (a, b) -> a && b);
    }

    private FileResource[] getGoalAttachments(final EdsGoal goal) {
        List<FileResource> goalAttachments = null;
        if (EdsGoal.PERSONAL_GOAL.equals(goal.getGoalCategory().getCode())) {
            goalAttachments = this.attachmentUtilsManager.getAttachments(Constants.F_PERS_GOAL, goal.getObjectID(), goal.getObjectID());
        } else if (EdsGoal.DEPARTMENT_GOAL.equals(goal.getGoalCategory().getCode())) {
            goalAttachments = this.attachmentUtilsManager.getAttachments(Constants.F_DEP_GOAL, goal.getObjectID(), goal.getObjectID());
        } else if (EdsGoal.PROJECT_GOAL.equals(goal.getGoalCategory().getCode())) {
            goalAttachments = this.attachmentUtilsManager.getAttachments(Constants.F_PROJ_GOAL, goal.getObjectID(), goal.getObjectID());
        } else if (EdsGoal.BUSINESS_GOAL.equals(goal.getGoalCategory().getCode())) {
            goalAttachments = this.attachmentUtilsManager.getAttachments(Constants.F_BUSS_GOAL, goal.getObjectID(), goal.getObjectID());
        }
        return goalAttachments.toArray(new FileResource[]{});
    }

    @Override
    public ArrayList<SelectItem> getCompanyGoals() {
        final List<EdsBusinessGoal> companyGoals = businessGoalManager.list(new ListingFilterParameter());
        ArrayList<SelectItem> selectItems = new ArrayList<>();
        companyGoals.forEach(c -> selectItems.add(new SelectItem(c.getObjectID(), c.getTitle())));
        return selectItems;
    }

    public Integer saveGoal(final GoalItem item) {
        return this.saveGoalItem(item);
    }

    public Integer saveGoalItem(final GoalItem item) {
        boolean isNewGoal = item.getObjectId() == null;

        EdsReference category = this.referenceManager.get(item.getGoalCategoryId());
        if (item.getGoalNumber() == null) {
            item.setGoalNumber(availabilityService.generateGoalNumber(category.getCode()));
        }
        final EdsGoal goal;
        if (isNewGoal) {
            Boolean code = goalManager.getGoalByNumberData(item.getGoalNumber().getNumberString());
            if (code) {
                item.setGoalNumber(availabilityService.generateGoalNumber(category.getCode()));
            }
            goal = new EdsGoal();
        } else {
            goal = this.goalManager.get(item.getObjectId());
            if (goal != null) {
                if (goal.getNumberData() != null && !goal.getNumberData().equals(item.getGoalNumber().getNumberString())) {
                    Boolean code = goalManager.getGoalByNumberData(item.getGoalNumber().getNumberString());
                    if (code) {
                        return -1;
                    }
                }
            }
        }
        if (goal.getIntNumber() == null && item.getGoalNumber() != null) {
            goal.setIntNumber(item.getGoalNumber().getIntNumber());
            goal.setNumberData(item.getGoalNumber().getNumberString());
        }
        if (item.getSelectedProjectGoalId() != null) {
            EdsGoal selectedProjectGoal = goalManager.get(item.getSelectedProjectGoalId());
            goal.setProjectGoal(selectedProjectGoal);
        }
        goal.setActionSteps(item.getActionSteps());
        goal.setCreator(this.goalManager.getUser());
        goal.setDescription(item.getDescription());
        goal.setFromDate(item.getFromDate().getNonConvertedDate());
        goal.setToDate(item.getToDate().getNonConvertedDate());

        // A department goal's period cannot be narrowed past its already-entered data:
        // fromDate must stay <= earliest entry, toDate must stay >= latest entry.
        // TODO: T103477
//        if (!isNewGoal && EdsGoal.DEPARTMENT_GOAL.equals(category.getCode())) {
//            Date minEntry = employeeMetricHistoryManager.getMinEntryDateByGoalId(goal.getObjectID());
//            Date maxEntry = employeeMetricHistoryManager.getMaxEntryDateByGoalId(goal.getObjectID());
//            if (minEntry != null && goal.getFromDate() != null && goal.getFromDate().after(minEntry)) {
//                return -2;
//            }
//            if (maxEntry != null && goal.getToDate() != null && goal.getToDate().before(maxEntry)) {
//                return -2;
//            }
//        }

        goal.setGoalCategory(category);
        goal.setProgress(item.getProgress());
        if (item.getResolverId() != null) {
            goal.setResolver(this.employeeManager.get(item.getResolverId()));
        }
        goal.setStatus(this.referenceManager.get(item.getStatusId()));
        if (item.getCompanyGoalId() != null) {
            goal.setBusinessGoal(this.businessGoalManager.get(item.getCompanyGoalId()));
        }
        goal.setTitle(item.getTitle());
        goal.setWeight(0);
        if (item.getWeightId() != null) {
            goal.setWeight(item.getWeightId());
        }
        if (!isNewGoal) {
            this.goalManager.update(goal);
        } else {
            this.goalManager.create(goal);
        }
        if (EdsGoal.PERSONAL_GOAL.equals(category.getCode())) {
            if (goal.getObjectID() != null) {
                availabilityService.createGoalHistory(goal.getObjectID(), new HistoryListItem("created"));
            }
        }
        if (EdsGoal.DEPARTMENT_GOAL.equals(category.getCode())) {
            goal.setWeight(item.getDepartmentGoalWeight());
        }
        goal.setTargetGoal(item.getTargetGoal());
        if (item.getProjectId() != null) {
            goal.setProject(this.projectManager.get(item.getProjectId()));
        }
        if (item.getDepartmentId() != null) {
            goal.setDepartment(this.departmentManager.get(item.getDepartmentId()));
        }
        if (item.getLocationId() != null) {
            goal.setLocation(this.locationManager.get(item.getLocationId()));
        }

        if (item.getValidityPeriodItem() != null) {
            goal.setValidityPeriod(this.validityPeriodManager.get(item.getValidityPeriodItem().getId()));
        } else {
            goal.setValidityPeriod(null);
        }
        if (item.getMeasurementUnit() != null) {
            EdsUnitMeasurement measurementUnit = new EdsUnitMeasurement();
            measurementUnit.setObjectID(item.getMeasurementUnit().getId());
            goal.setMeasurementUnit(measurementUnit);
        }

        if (item.getScore() != null) {
            goal.setScoreCalculation(this.referenceManager.get(item.getScore().getId()));
        } else {
            goal.setScoreCalculation(null);
        }

        goal.setGoalCustomFields(this.saveCustomFields(goal.getGoalCustomFields(), item.getCustomFields()));


        if (item.getGoalAssigneeItem().length > 0) {
            final Set<EdsGoalAssignees> assignees = new HashSet<>();
            final List<EdsEmployee> employeeList = new ArrayList<>();
            // Track existing assignee IDs so removed ones can be soft-deleted
            // (preserves their metric-history links instead of orphaning them).
            final Set<Integer> existingAssigneeIds = new HashSet<>();
            final Set<Integer> keptAssigneeIds = new HashSet<>();
            if (!isNewGoal && goal.getGoalAssigneeses() != null) {
                for (EdsGoalAssignees existing : goal.getGoalAssigneeses()) {
                    if (existing.getObjectID() != null) {
                        existingAssigneeIds.add(existing.getObjectID());
                    }
                }
            }
            for (final GoalAssigneeItem assignItem : item.getGoalAssigneeItem()) {
                final EdsGoalAssignees goalAssignees;
                if (!isNewGoal && assignItem.getObjectId() != null) {
                    goalAssignees = this.goalAssigneesManager.get(assignItem.getObjectId());
                    keptAssigneeIds.add(goalAssignees.getObjectID());
                } else {
                    goalAssignees = new EdsGoalAssignees();
                }
                goalAssignees.setAssignee(this.employeeManager.get(assignItem.getId()));
                goalAssignees.setGoal(goal);
                goalAssignees.setDeleted(false);
                goalAssignees.setTarget(assignItem.getTarget());
                goalAssignees.setActual(assignItem.getActual());
                goalAssignees.setWeight(assignItem.getWeight());
                if (!isNewGoal && assignItem.getObjectId() != null) {
                    if (EdsGoal.DEPARTMENT_GOAL.equals(category.getCode())) {
                        Double total = employeeMetricHistoryManager.getActualTotalByGoalAssigneeIdAndEmployeeId(goalAssignees.getObjectID(), assignItem.getId());
                        goalAssignees.setActual(total);
                    }
                    this.goalAssigneesManager.update(goalAssignees);
                } else {
                    this.goalAssigneesManager.create(goalAssignees);
                }
                assignees.add(goalAssignees);
                employeeList.add(goalAssignees.getAssignee());
            }
            // Soft-delete assignees that were removed (in DB but not in the save list)
            for (Integer existingId : existingAssigneeIds) {
                if (!keptAssigneeIds.contains(existingId)) {
                    EdsGoalAssignees removed = this.goalAssigneesManager.get(existingId);
                    if (removed != null) {
                        removed.setDeleted(true);
                        this.goalAssigneesManager.update(removed);
                    }
                }
            }
            goal.setGoalAssigneeses(assignees);
            goalManager.update(goal);
            try {
                String templateNotification = EmailNotificationConstants.PERSONAL_GOAL_ASSIGNEE_NOTIFICATION;
                if (goal.getGoalCategory() != null) {
                    if (EdsGoal.BUSINESS_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                        templateNotification = EmailNotificationConstants.BUSINESS_GOAL_ASSIGNESS_NOTIFICATION;
                    } else if (EdsGoal.DEPARTMENT_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                        templateNotification = EmailNotificationConstants.DEPARTMENT_GOAL_ASSIGNEE_NOTIFICATION;
                    } else if (EdsGoal.PROJECT_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                        templateNotification = EmailNotificationConstants.PROJECT_GOAL_ASSIGNEE_NOTIFICATION;
                    }
                }
                for (final EdsEmployee employee : employeeList) {
                    final boolean emailNotificationSettings = this.emailNotificationSettingsManager.hasEmailNotification(employee.getObjectID(), templateNotification);
                    if (emailNotificationSettings) {
                        this.messageManager.sendGoalAssignNotification(goal, employee, employeeList);
                    }
                }
            } catch (final EdsDbException e) {
                e.printStackTrace();
            }
        }
        if (item.getAttachments() != null && item.getAttachments().length > 0) {
            this.saveGoalAttachments(item.getAttachments(), goal);
        }

        final ArrayList<HistoryListItem> goalNotes = item.getNotes();
        if (goal.getObjectID() != null && goalNotes != null && !goalNotes.isEmpty()) {
            for (final HistoryListItem goalNote : goalNotes) {
                if (goalNote != null && goalNote.isNew()) {
                    goalNote.setSubject("");
                    goalNote.setRelatedId(goal.getObjectID());
                    if (EdsGoal.PERSONAL_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                        goalNote.setRelatedToId(EdsNoteHistory.PERSONAL_GOAL);
                    } else if (EdsGoal.BUSINESS_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                        goalNote.setRelatedToId(EdsNoteHistory.BUSINESS_GOAL);
                    } else if (EdsGoal.DEPARTMENT_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                        goalNote.setRelatedToId(EdsNoteHistory.DEPARTMENT_GOAL);
                    } else if (EdsGoal.PROJECT_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                        goalNote.setRelatedToId(EdsNoteHistory.PROJECT_GOAL);
                    }
                    this.bugReportServiceLocal.addNote(goalNote);
                }
            }
        }

        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setEntityId(goal.getObjectID());
        if (isNewGoal) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            if (EdsGoal.PERSONAL_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                kpiLog.setEntityType(EdsGoal.PERSONAL_GOAL);
                ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Personal goal saved");
            } else if (EdsGoal.BUSINESS_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                kpiLog.setEntityType(EdsGoal.BUSINESS_GOAL);
                ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Business goal saved");
            } else if (EdsGoal.DEPARTMENT_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                kpiLog.setEntityType(EdsGoal.DEPARTMENT_GOAL);
                ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Department goal saved");
            } else if (EdsGoal.PROJECT_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                kpiLog.setEntityType(EdsGoal.PROJECT_GOAL);
                ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Project goal saved");
            }
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            if (EdsGoal.PERSONAL_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                kpiLog.setEntityType(EdsGoal.PERSONAL_GOAL);
                ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Personal goal updated");
            } else if (EdsGoal.BUSINESS_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                kpiLog.setEntityType(EdsGoal.BUSINESS_GOAL);
                ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Business goal updated");
            } else if (EdsGoal.DEPARTMENT_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                kpiLog.setEntityType(EdsGoal.DEPARTMENT_GOAL);
                ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Department goal updated");
            } else if (EdsGoal.PROJECT_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                kpiLog.setEntityType(EdsGoal.PROJECT_GOAL);
                ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Project goal updated");
            }
        }

        if (goal != null && goal.getObjectID() != null && item.isRelationChanged()) {
            if (EdsGoal.PERSONAL_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                this.allInOneServiceLocal.saveRelations(RelationItem.TYPE_PERSONAL_GOAL, goal.getObjectID(), goal.getTitle(), item.getRelations());
            } else if (EdsGoal.BUSINESS_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                this.allInOneServiceLocal.saveRelations(RelationItem.TYPE_BUSINESS_GOAL, goal.getObjectID(), goal.getTitle(), item.getRelations());
            } else if (EdsGoal.DEPARTMENT_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                this.allInOneServiceLocal.saveRelations(RelationItem.TYPE_DEPARTMENT_GOAL, goal.getObjectID(), goal.getTitle(), item.getRelations());
            } else if (EdsGoal.PROJECT_GOAL.equalsIgnoreCase(goal.getGoalCategory().getCode())) {
                this.allInOneServiceLocal.saveRelations(RelationItem.TYPE_PROJECT_GOAL, goal.getObjectID(), goal.getTitle(), item.getRelations());
            }
        }
        return goal.getObjectID();
    }

    private EdsGoalCustomFields saveCustomFields(EdsGoalCustomFields goalCustomField, final List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (goalCustomField == null) {
                boolean isEmpty = true;
                for (final CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                goalCustomField = new EdsGoalCustomFields();
                this.goalCustomFieldsManager.create(goalCustomField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(goalCustomField, customFieldItems);
            return goalCustomField;
        }
        return null;
    }


    private void saveGoalAttachments(final FileItem[] attachments, final EdsGoal goal) {
        if (EdsGoal.PERSONAL_GOAL.equals(goal.getGoalCategory().getCode())) {
            this.attachmentUtilsManager.saveAttachments(Constants.F_PERS_GOAL, goal.getObjectID(), goal.getObjectID(), attachments);
        } else if (EdsGoal.DEPARTMENT_GOAL.equals(goal.getGoalCategory().getCode())) {
            this.attachmentUtilsManager.saveAttachments(Constants.F_DEP_GOAL, goal.getObjectID(), goal.getObjectID(), attachments);
        } else if (EdsGoal.PROJECT_GOAL.equals(goal.getGoalCategory().getCode())) {
            this.attachmentUtilsManager.saveAttachments(Constants.F_PROJ_GOAL, goal.getObjectID(), goal.getObjectID(), attachments);
        } else if (EdsGoal.BUSINESS_GOAL.equals(goal.getGoalCategory().getCode())) {
            this.attachmentUtilsManager.saveAttachments(Constants.F_BUSS_GOAL, goal.getObjectID(), goal.getObjectID(), attachments);
        }
    }

    public Integer saveCompanyGoal(final GoalItem item) {
        final EdsBusinessGoal businessGoal;
        boolean isNew = false;
        if (item.getObjectId() != null) {
            businessGoal = this.businessGoalManager.get(item.getObjectId());
        } else {
            businessGoal = new EdsBusinessGoal();
            isNew = true;
        }
        businessGoal.setDescription(item.getDescription());
        businessGoal.setFromDate(item.getFromDate().getNonConvertedDate());
        businessGoal.setToDate(item.getToDate().getNonConvertedDate());
        businessGoal.setOutcome(item.getOutcome());
        businessGoal.setStatus(this.referenceManager.get(item.getStatusId()));
        businessGoal.setTitle(item.getTitle());
        if (item.getValidityPeriodItem() != null) {
            businessGoal.setValidityPeriod(this.validityPeriodManager.get(item.getValidityPeriodItem().getId()));
        }
        if (item.getObjectId() != null) {
            this.businessGoalManager.update(businessGoal);
        } else {
            this.businessGoalManager.create(businessGoal);
        }

        businessGoal.setGoalCustomFields(this.saveCustomFields(businessGoal.getGoalCustomFields(), item.getCustomFields()));

        if (item.getAttachments() != null && item.getAttachments().length > 0) {
            this.saveCompanyGoalAttachments(item.getAttachments(), businessGoal);
        }
        //save company goal notes
        final ArrayList<HistoryListItem> goalNotes = item.getNotes();
        if (businessGoal.getObjectID() != null && goalNotes != null && goalNotes.size() > 0) {
            for (final HistoryListItem goalNote : goalNotes) {
                if (goalNote != null && goalNote.isNew()) {
                    goalNote.setSubject("");
                    goalNote.setRelatedId(businessGoal.getObjectID());
                    goalNote.setRelatedToId(EdsNoteHistory.COMPANY_GOAL);
                    this.bugReportServiceLocal.addNote(goalNote);
                }
            }
        }

        this.allInOneServiceLocal.saveRelations(RelationItem.TYPE_COMPANY_GOAL, businessGoal.getObjectID(), businessGoal.getTitle(), item.getRelations());

        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        if (businessGoal.getObjectID() != null) {
            kpiLog.setEntityId(businessGoal.getObjectID());
        }
        kpiLog.setEntityType(Constants.COMPANY_GOAL);
        if (isNew) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Company goal added");
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Company goal updated");
        }
        return businessGoal.getObjectID();
    }

    private void saveCompanyGoalAttachments(final FileItem[] attachments, final EdsBusinessGoal businessGoal) {
        this.attachmentUtilsManager.saveAttachments(Constants.F_COMP_GOAL, businessGoal.getObjectID(), businessGoal.getObjectID(), attachments);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public GoalItem editCompanyGoal(final Integer objectId) {
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectId);
        kpiLog.setEntityType(Constants.COMPANY_GOAL);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "View company goal");

        final GoalItem item = new GoalItem();
        final SelectItem[] selectItems = this.commonServiceLocal.convertReference2SelectItem2(EdsTask.TASK_STATUS);

        final ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStatusCode(ValidityPeriodItem.VALIDITY_PERIOD_GOAL);
        item.setValidityPeriodItems(this.validityPeriodManager.getValidityPeriods(fp));

        item.setStatuss(selectItems);
        if (objectId != null) {
            final EdsBusinessGoal goal = this.businessGoalManager.get(objectId);
            item.setTitle(goal.getTitle());
            item.setDescription(goal.getDescription());
            item.setOutcome(goal.getOutcome());
            if (goal.getStatus() != null) {
                item.setStatusId(goal.getStatus().getObjectID());
                item.setStatus(this.referenceWfmMessageSource.localizeRef(goal.getStatus()));
            }
            if (goal.getFromDate() != null) {
                item.setFromDate(new DateNonConvertable(goal.getFromDate()));
            }
            if (goal.getToDate() != null) {
                item.setToDate(new DateNonConvertable(goal.getToDate()));
            }

            if (goal.getValidityPeriod() != null) {
                final EdsValidityPeriod validityPeriod = goal.getValidityPeriod();
                item.setValidityPeriodItem(validityPeriod.getDTO());
            }

            item.setGoalAttachments(this.getCompanyGoalAttachments(objectId));

            item.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_COMPANY_GOAL, objectId)));

            item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(goal.getGoalCustomFields(),
                    this.commonService.getCompanyCustomFields(ViewName.CompanyGoal)));
        }
        return item;
    }

    private FileResource[] getCompanyGoalAttachments(final Integer goalId) {
        final List<FileResource> goalAttachments = this.attachmentUtilsManager.getAttachments(Constants.F_COMP_GOAL, goalId, goalId);
        return goalAttachments.toArray(new FileResource[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<GoalItem> getPersonalGoalList(final ListingFilterParameter filterParametrs) {
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityType(EdsGoal.PERSONAL_GOAL);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Get personal goals list");
        final EdsReference personalGoal = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PERSONAL_GOAL);
        filterParametrs.setCrmEntityId(personalGoal.getObjectID());
        filterParametrs.setEntityName(ViewName.PersonalGoal.name());
        final ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        if (panelTools != null && panelTools.isCustomFieldsShown()) {
            filterParametrs.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(this.commonService.getCompanyCustomFieldsForListView(ViewName.PersonalGoal));
        }
        Set<Integer> allEmployeeIds = new HashSet<>();
        boolean showAllPersonalGoals = ServerUtils.hasPermission(PermissionConstants.HRMS_GOAL_SEE_ALL);
        boolean showAllPersonalGoalsByDepartment = ServerUtils.hasPermission(PermissionConstants.HRMS_GOAL_SEE_ALL_BY_DEPARTMENT);
        boolean showAllPersonalGoalsBySupervisor = ServerUtils.hasPermission(PermissionConstants.HRMS_GOAL_SEE_ALL_BY_SUPERVISOR);
        EdsUser user = employeeManager.getUser();

        if (showAllPersonalGoals) {
            filterParametrs.setAllGoals(true);
        } else {
            allEmployeeIds.add(user.getObjectID());
            if (showAllPersonalGoalsByDepartment) {
                getChildEmployeeIds(user, allEmployeeIds);
            }
            if (showAllPersonalGoalsBySupervisor) {
                List<EdsEmployee> employeesBySupervisorId = employeeManager.getEmployeesBySupervisorId(user.getObjectID());
                if (employeesBySupervisorId != null && employeesBySupervisorId.size() > 0)
                    employeesBySupervisorId.forEach(e -> allEmployeeIds.add(e.getObjectID()));
            }
            if (allEmployeeIds.size() > 0) {
                String empIds = allEmployeeIds.stream().map(Objects::toString).collect(Collectors.joining(","));
                filterParametrs.setEmployeeIDs(empIds);
            }
        }
        return this.getGoalList(filterParametrs);
    }

    private void getChildEmployeeIds(EdsUser user, Set<Integer> allEmployeeIds) {
        EdsDepartment departmentByLeader = departmentManager.getDepartmentByLeader(user);
        if (departmentByLeader != null) {
            ArrayList<Integer> employeeIDsByTeamLeader = departmentManager.getEmployeeIDsByTeamLeader(user.getObjectID());
            allEmployeeIds.addAll(employeeIDsByTeamLeader);
            List<Integer> childList = departmentTreeManager.getChildList(departmentByLeader.getObjectID());
            if (childList != null && childList.size() > 0) {
                childList.forEach(ch -> {
                    List<EdsEmployee> teamEmployees = employeeDepartmentManager.getTeamEmployees2(ch);
                    if (teamEmployees != null && teamEmployees.size() > 0) {
                        teamEmployees.forEach(e -> allEmployeeIds.add(e.getObjectID()));
                    }
                });
            }
        }
    }

    private ListResult<GoalItem> getGoalList(final ListingFilterParameter filterParametrs) {
        final List<EdsGoal> goalList = this.goalManager.list(filterParametrs);
        final int totalCount = goalList.size();
        final ArrayList<GoalItem> results = new ArrayList<>();
        //Goal Employees
        goalList.forEach(goal -> {
            final GoalItem item = new GoalItem();
            item.setObjectId(goal.getObjectID());
            if (goal.getNumberData() != null) {
                NumberData numberData = new NumberData();
                numberData.setNumberString(goal.getNumberData());
                numberData.setFirstNumberString(goal.getNumberData());
                numberData.setNumberFormat("_");
                item.setGoalNumber(numberData);
            }
            if (goal.getProjectGoal() != null) {
                item.setProjectGoalTitle(goal.getProjectGoal().getTitle());
            }
            item.setCreatorName(goal.getCreator() != null ? goal.getCreator().getName() : null);
            item.setActionSteps(goal.getActionSteps());
            item.setDescription(goal.getDescription());
            item.setFromDate(new DateNonConvertable(goal.getFromDate()));
            item.setToDate(new DateNonConvertable(goal.getToDate()));
            if (goal.getDepartment() != null) {
                item.setDepartmentId(goal.getDepartment().getObjectID());
                item.setDepartment(goal.getDepartment().getName());
            }
            if (goal.getLocation() != null) {
                item.setLocationId(goal.getLocation().getObjectID());
                item.setLocation(goal.getLocation().getName());
            }
            if (goal.getCreator() != null) {
                item.setCreatorId(goal.getCreator().getObjectID());
            } else {
                item.setCreatorId(0);
            }
            if (goal.getProject() != null) {
                item.setProjectId(goal.getProject().getObjectID());
                item.setProject(goal.getProject().getName());
            }
            if (goal.getGoalCategory() != null) {
                item.setGoalCategory(this.referenceWfmMessageSource.localizeRef(goal.getGoalCategory()));
            }
            item.setProgress(goal.getProgress());
            if (goal.getResolver() != null) {
                item.setResolver(goal.getResolver().getFullName());
            }
            if (goal.getStatus() != null) {
                item.setStatus(this.referenceWfmMessageSource.localizeRef(goal.getStatus()));
            }
            if (goal.getBusinessGoal() != null) {
                item.setCompanyGoal(goal.getBusinessGoal().getTitle());
            }
            item.setTitle(goal.getTitle());
            item.setWeight(goal.getWeight());
            final StringBuilder employee = new StringBuilder();
            final StringBuilder weightBuilder = new StringBuilder();
            final Set<EdsGoalAssignees> assign = goal.getUndeletedGoalAssignees();
            if (assign != null) {
                for (final EdsGoalAssignees empl : assign) {
                    if ("PERSONAL_GOAL".equals(goal.getGoalCategory().getCode())) {
                        item.setAssigneeId(empl.getAssignee().getObjectID());
                    }
                    employee.append(empl.getAssignee().getName());
                    employee.append(",");

                    if (empl.getWeight() != null) {
                        if (!"".contentEquals(weightBuilder)) {
                            weightBuilder.append(", ");
                        }
                        weightBuilder.append(empl.getWeight());
                    }
                }
                if (employee.lastIndexOf(",") != -1) {
                    employee.deleteCharAt(employee.lastIndexOf(","));
                }
                item.setGoalAssignedTo(employee.toString());
                item.setWeightString(weightBuilder.toString());
            } else {
                item.setGoalAssignedTo("N/A");
                item.setWeightString("N/A");
            }
            if (filterParametrs.isCustomFieldsShown()) {
                item.setCustomFieldsMap(CustomFieldsUtils.getRPCCustomFields(goal.getGoalCustomFields(), filterParametrs.getListPanelTool().getColumnCodeName()));
            }
            if (goal.getValidityPeriod() != null) {
                item.setValidityPeriodItem(goal.getValidityPeriod().getDTO());
            }
            results.add(item);
        });
        return new ListResult<>(results, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<GoalItem> getDepartmentGoalList(final ListingFilterParameter filterParametrs) {
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityType(EdsGoal.DEPARTMENT_GOAL);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Get department goals list");
        final EdsReference departmentGoal = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.DEPARTMENT_GOAL);
        filterParametrs.setCrmEntityId(departmentGoal.getObjectID());
        filterParametrs.setEntityName(ViewName.DepartmentGoal.name());
        final ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        if (panelTools.isCustomFieldsShown()) {
            filterParametrs.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(this.commonService.getCompanyCustomFieldsForListView(ViewName.DepartmentGoal));
        }
        return this.getGoalList(filterParametrs);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<GoalItem> getCompanyGoalList(final ListingFilterParameter fp) {
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityType(Constants.COMPANY_GOAL);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Get company goal list");
        final ListPanelToolRpc panelTools = fp.getListPanelTool();
        if (panelTools.isCustomFieldsShown()) {
            fp.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(this.commonService.getCompanyCustomFieldsForListView(ViewName.CompanyGoal));
        }
        fp.setEntityName(ViewName.CompanyGoal.name());
        final List<EdsBusinessGoal> goalList = this.businessGoalManager.list(fp);
        final int totalCount = goalList.size();

        final ArrayList<GoalItem> results = new ArrayList<>();
        goalList.forEach(goal -> {
            final GoalItem item = new GoalItem();
            item.setObjectId(goal.getObjectID());
            item.setOutcome(goal.getOutcome());
            item.setDescription(goal.getDescription());
            item.setFromDate(new DateNonConvertable(goal.getFromDate()));
            item.setToDate(new DateNonConvertable(goal.getToDate()));
            if (goal.getStatus() != null) {
                item.setStatus(this.referenceWfmMessageSource.localizeRef(goal.getStatus()));
            }
            item.setTitle(goal.getTitle());
            if (fp.isCustomFieldsShown()) {
                item.setCustomFieldsMap(CustomFieldsUtils.getRPCCustomFields(goal.getGoalCustomFields(), fp.getListPanelTool().getColumnCodeName()));
            }
            if (goal.getValidityPeriod() != null) {
                item.setValidityPeriodItem(goal.getValidityPeriod().getDTO());
            }
            results.add(item);
        });
        return new ListResult<>(results, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<GoalItem> getBusinGoalList(final ListingFilterParameter filterParametrs) {
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityType(EdsGoal.BUSINESS_GOAL);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Get business goals list");
        final EdsReference departmentGoal = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.BUSINESS_GOAL);
        filterParametrs.setCrmEntityId(departmentGoal.getObjectID());
        filterParametrs.setEntityName(ViewName.BusinessGoal.name());
        final ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        if (panelTools.isCustomFieldsShown()) {
            filterParametrs.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(this.commonService.getCompanyCustomFieldsForListView(ViewName.BusinessGoal));
        }
        return this.getGoalList(filterParametrs);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountries() {
        return this.commonService.getCountries(true);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileItem[] getAttachments(final Integer employmentId) {
        final List<EdsAttachment> employmentAttachments = this.attachmentManager.getEmploymentAttachmentsById(employmentId);
        final FileItem[] fileItems = new FileItem[employmentAttachments.size()];
        for (int i = 0; i < employmentAttachments.size(); i++) {
            final EdsAttachment employmentAttachment = employmentAttachments.get(i);
            final FileItem fileItem = new FileItem();
            fileItem.setAttachmentId(employmentAttachment.getObjectID());
            fileItem.setFileName(employmentAttachment.getOriginalName());
            fileItem.setDescription(employmentAttachment.getDescription());
            fileItem.setSize(employmentAttachment.getSize());
            fileItem.setUploadType(employmentAttachment.getType().getCode());
            if (employmentAttachment.getType().getCode().equals(Constants.GOOGLE)) {
                fileItem.setGoogleDocumentLink(this.sinxDocumentsSettingsManager.getSinxDocsSettings(employmentAttachment).getDocumentLink());
            } else if (employmentAttachment.getType().getCode().equals(Constants.OFFICE_365) || employmentAttachment.getType().getCode().equals(Constants.OFFICE_365_SHARE_POINT)) {
                final EdsSinxDocumentsSettings sdc = this.sinxDocumentsSettingsManager.getSinxDocsSettings(employmentAttachment);
                fileItem.setDocumentID(sdc.getDocumentID());
                fileItem.setDocumentOpenID(sdc.getDocumentOpenID());
                fileItem.setOfficeDocumentLink(sdc.getDocumentLink());
            }
            fileItems[i] = fileItem;
        }

        return fileItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileResource[] getRelatedFiles(final Integer employeeId) {
        List<FileResource> attachments = new ArrayList<>();
        if (employeeId != null) {
            attachments = this.attachmentUtilsManager.getAttachments(Constants.F_EMPLOYEE_PROFILE, employeeId, employeeId);
        }
        return attachments.toArray(new FileResource[]{});
    }

    private void saveEmployeeProfileAttachments(final FileItem[] attachments, final Integer employeeID) {
        this.attachmentUtilsManager.saveAttachments(Constants.F_EMPLOYEE_PROFILE, employeeID, employeeID, attachments);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProfileItem editProfile(Integer objectID) {
        return this.editProfile(objectID, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProfileItem editProfile(Integer objectID, String from) {
        return this.editProfile(objectID, from, false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProfileItem editProfile(Integer objectID, String from, boolean isView) {
        return editProfile(objectID, from, isView, null, null, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProfileItem editProfile(Integer objectID, String from, boolean isView, Integer placementId, String formType, Integer convertedFormId) {
        ProfileItem profileItem = new ProfileItem();
        EdsEmployeeProfile profile = null;
        EdsEmployee employee = null;
        EdsPlacement placement = null;
        EdsUser currentUser = userManager.getUser();
        boolean fromImport = Constants.EMPLOYEE_IMPORT.equals(from);
        if (objectID != null) {
            employee = employeeManager.get(objectID);
            profile = employee != null ? employee.getProfile() : null;
        } else if (placementId != null) {
            placement = placementManager.get(placementId);
        } else if (!Constants.TC_INSTRUCTOR_ADD_FORM.equals(from) && !Constants.FROM_SINGLE_EMPLOYEE_ADD.equals(from)) {
            profile = profileManager.getProfile();
        }

        profileItem.setRelationItems(getEmployeeRelations(objectID));
        List<EdsAttendanceTerminal> fingerprintSetup = attendanceTerminalManager.getAll();
        if (employee != null && employee.getFingerprintDeviceUuids() != null) {
            List<String> fingerprintDeviceUuids = employee.getFingerprintDeviceUuids();
            Integer[] array = fingerprintSetup.stream()
                    .filter(fs -> fingerprintDeviceUuids.contains(fs.getCompanyUniqueID()))
                    .map(EdsAttendanceTerminal::getObjectID)
                    .toArray(Integer[]::new);
            profileItem.setFingerprintDeviceId(array);
        }
        SelectItem[] resultImContact = commonServiceLocal.reference2SelectItem(referenceManager.listReferences(EdsCrmContact._IM_ADDRESSES), null);
        profileItem.setContactImAddress(resultImContact);

        if (profile != null) {
            profileItem.setEmployeeId(employee.getObjectID()); // fixme, potential NPE for employee
            profileItem.setNationality(profile.getNationality());
            profileItem.setPassportNumber(profile.getPassportNumber());
            profileItem.setPassportIssueDate(profile.getPassportIssueDate() != null ? new DateNonConvertable(profile.getPassportIssueDate()) : null);
            profileItem.setPassportExpiryDate(profile.getPassportExpiryDate() != null ? new DateNonConvertable(profile.getPassportExpiryDate()) : null);
            profileItem.setMedicalInsuranceExpireDate(profile.getMedicalInsuranceExDate() != null ? new DateNonConvertable(profile.getMedicalInsuranceExDate()) : null);
            profileItem.setVisaNumber(profile.getVisaNumber());
            profileItem.setVisaIssueDate(profile.getVisaIssueDate() != null ? new DateNonConvertable(profile.getVisaIssueDate()) : null);
            profileItem.setPaymentMethod(employee.getPaymentMethod());

            EdsCountry passportIssue = profile.getCountry();
            if (passportIssue != null) {
                profileItem.setPassportIssueItem(new SelectItem(passportIssue.getObjectID(), passportIssue.getName()));
            }
            profileItem.setInsuranceNumber(profile.getInsuranceNumber());
            if (employee.getDriverNumber() != null) {
                profileItem.setDriverID(employee.getDriverNumber().toString());
            }
        } else if (placement != null) {
            profileItem.setFromPlacement(true);
            ArrayList<CompanyCustomFieldItem> customFieldItems = convertPlacementCfToEmployeeCf(placement, profileItem);
            profileItem.setCustomFields(customFieldItems);

            EdsCrmContact candidate = placement.getCandidate();
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setHRMS(true);
            profileItem = (ProfileItem) candidate.getRPC(fp, profileItem);
            profileItem.setPmDepartmentID(placement.getDepartment() != null ? placement.getDepartment().getObjectID() : null);
            profileItem.setDepartment(placement.getDepartment() != null ? placement.getDepartment().getName() : null);
            profileItem.setPositionId(placement.getPosition() != null ? placement.getPosition().getObjectID() : null);
            profileItem.setLocationId(placement.getLocation() != null ? placement.getLocation().getObjectID() : null);
            profileItem.setLocation(placement.getLocation() != null ? placement.getLocation().getAsSelectItem() : null);
            if (candidate != null) {
                profileItem.setWorkPhone(candidate.getPrimaryPhone() != null ? candidate.getPrimaryPhone() : null);
                profileItem.setWorkEmail(candidate.getPrimaryEmail() != null ? candidate.getPrimaryEmail() : null);
                ArrayList<EdsSpokenLanguages> spokenLanguages = spokenLanguagesManager.getListByRelation(candidate.getObjectID(), EdsSpokenLanguages.TYPE_CANDIDATE);
                if (spokenLanguages != null) {
                    ArrayList<SpokenLanguageItem> languageItems = new ArrayList<>(spokenLanguages.size());
                    spokenLanguages.forEach(sl -> languageItems.add(new SpokenLanguageItem(sl.getLanguage() != null ? new SelectItem(sl.getLanguage().getObjectID(), referenceWfmMessageSource.localize(sl.getLanguage().getCode(), sl.getLanguage().getName())) : null, sl.getLevel() != null ? new SelectItem(sl.getLevel().getObjectID(), referenceWfmMessageSource.localize(sl.getLevel().getCode(), sl.getLevel().getName())) : null)));
                    profileItem.setSpokingLanguages(languageItems);
                }
            }
        }

        if (!isView && !fromImport) {
            SelectItem[] contactTitles = contactServiceLocal.getContactSelectItems(Constants._TITLE);
            Locale userLocale = ServerSecurityContext.getInstance().getUserLocale();
            ArrayList<SelectItem> titleList = new ArrayList<>();
            for (SelectItem title : contactTitles) {
                if (userLocale != null && "uz".equalsIgnoreCase(userLocale.toString()) && !"MRS".equalsIgnoreCase(title.getDescription()) && !"MS".equalsIgnoreCase(title.getDescription())) {
                    titleList.add(title);
                }
            }
            if (userLocale != null && "uz".equalsIgnoreCase(userLocale.toString())) {
                profileItem.setTitleList(titleList.toArray(new SelectItem[0]));
            } else {
                profileItem.setTitleList(contactTitles);
            }

            profileItem.setMartialStatusList(commonServiceLocal.convertReference2SelectItem(EdsEmployeeProfile.MARTIAL_STATUS, false, null));
            profileItem.setQualifications(ServerUtils.getAsSelectItem(referenceManager.listReferences(Constants.Q_QUALIFICATION), ServerUtils.REFERENCE));
            profileItem.setPmDepartmentItems(departmentService.getTeamsList());
            profileItem.setTimeslots(timeSlotManager.getTimeslotsAsSelectItem(null));
            profileItem.setDefaultTimeslot(userManager.getUser().getCompany().getDefaultTimeSlot().getAsSelectItem());
        }

        if (!isView && !fromImport) {
            List<EdsReference> employeeStatusList = referenceManager.listReferences(Constants.EMPLOYEE_STATUS);
            List<EdsReference> necessaryEmployeeStatusList = new ArrayList<>();
            String employeeStatusCode = employee != null ? employee.getAccountStatus().getCode() : referenceManager.findReference(Constants.EMPLOYEE_STATUS, Constants.EMPLOYEE_STATUS_PENDING).getCode();//for training center -> instructor
            boolean canAddNoAccessUsers = false;
            Integer[] count = employeeServiceLocal.getAllEmployeesMaxCount(null, null);
            if (count != null && count.length > 0) {
                canAddNoAccessUsers = count[1] > 0; //no-access employees count
            }
            for (EdsReference status : employeeStatusList) {
                //we shouldn't add 'RESIGNED' as it's not possible to resign from edit view unless current status is 'RESIGNED' and
                //also shouldn't add 'NO ACCESS' status, in case such option is not enabled in generic settings
                if ((!Constants.EMPLOYEE_STATUS_RESIGNED.equals(employeeStatusCode) && Constants.EMPLOYEE_STATUS_RESIGNED.equals(status.getCode())) || (Constants.EMPLOYEE_STATUS_NO_ACCCESS.equals(status.getCode()) && !canAddNoAccessUsers)) {
                    continue;
                }
                if (Constants.EMPLOYEE_STATUS_ACTIVE.equals(employeeStatusCode) && !Constants.EMPLOYEE_STATUS_PENDING.equals(status.getCode())) {
                    //add ACTIVE, INACTIVE, RESIGNED, NO ACCESS
                    necessaryEmployeeStatusList.add(status);
                } else if (Constants.EMPLOYEE_STATUS_INACTIVE.equals(employeeStatusCode) && !Constants.EMPLOYEE_STATUS_PENDING.equals(status.getCode())) {
                    //add ACTIVE, INACTIVE, RESIGNED, NO ACCESS
                    necessaryEmployeeStatusList.add(status);
                } else if (Constants.EMPLOYEE_STATUS_PENDING.equals(employeeStatusCode) && Constants.EMPLOYEE_STATUS_PENDING.equals(status.getCode())) {
                    //add PENDING
                    necessaryEmployeeStatusList.add(status);
                } else if (Constants.EMPLOYEE_STATUS_RESIGNED.equals(employeeStatusCode) && (Constants.EMPLOYEE_STATUS_RESIGNED.equals(status.getCode()) || Constants.EMPLOYEE_STATUS_ACTIVE.equals(status.getCode()))) {
                    //add ACTIVE, RESIGNED
                    necessaryEmployeeStatusList.add(status);
                } else if (Constants.EMPLOYEE_STATUS_NO_ACCCESS.equals(employeeStatusCode) && (!Constants.EMPLOYEE_STATUS_INACTIVE.equals(status.getCode()) && !Constants.EMPLOYEE_STATUS_PENDING.equals(status.getCode()))) {
                    //add ACTIVE, RESIGNED, NO ACCESS
                    necessaryEmployeeStatusList.add(status);
                }
            }
            profileItem.setStatusList(commonServiceLocal.reference2SelectItem(necessaryEmployeeStatusList, null));
            profileItem.setEmpModeList(commonServiceLocal.convertReference2SelectItem(EdsEmployeeProfile.EMPLOYMENT_MODE, false, null));

            List<EdsGrade> salaryGradeList = gradeManager.getGradeListByCompany(employee != null ? employee.getCompany().getObjectID() : currentUser.getCompany().getObjectID());
            SelectItem[] sg = salaryGradeList.stream()
                    .map(s -> new SelectItem(s.getObjectID(), s.getGradeCode() + " " + s.getGradeLevel()))
                    .toArray(SelectItem[]::new);

            profileItem.setSalaryGradeList(sg);
            profileItem.setUserLimit(count);
        }
        profileItem.setRoleList(getRoles(from));
        profileItem.setFingerprintDeviceList(getFingerprintDevices());
        profileItem.setStatusId(employee != null ? employee.getAccountStatus().getObjectID() : referenceManager.findReference(Constants.EMPLOYEE_STATUS, Constants.EMPLOYEE_STATUS_PENDING).getObjectID());
        profileItem.setStatus(employee != null && employee.getAccountStatus() != null ? referenceWfmMessageSource.localize(employee.getAccountStatus().getCode(), employee.getAccountStatus().getName()) : "");
        profileItem.setStatusCode(employee != null && employee.getAccountStatus() != null ? employee.getAccountStatus().getCode() : "");


        if (profile != null) {
            profileItem = profile.getRPC(profileItem);
        }
        if (profileItem.getTitleId() != null && profile != null && profile.getContact() != null && profile.getContact().getTitleRef() != null) {
            if (!"other".equalsIgnoreCase(profile.getContact().getTitleRef().getName())) {
                profileItem.setTitle(referenceWfmMessageSource.localizeRef(profile.getContact().getTitleRef()));
            }
        }

        if (employee != null && profile != null) {
            profileItem.setFirstName(employee.getFirstName());
            profileItem.setMiddleName(employee.getMiddleName());
            profileItem.setLastName(employee.getLastName());
            if (!fromImport) {
                profileItem.setEmployeeImageUrl(getUserImageUrl(employee));
            }

            profileItem.setDob(employee.getBirthDay() != null ? new DateNonConvertable(employee.getBirthDay()) : null);

            profileItem.setGender(profile.getGender());

            if (profile.getMartialStatus() == null) {
                profileItem.setMartialStatus("N/A");
            } else {
                profileItem.setMartialStatus(referenceWfmMessageSource.localize(profile.getMartialStatus().getCode(), profile.getMartialStatus().getName()));
                profileItem.setMartialStatusId(profile.getMartialStatus().getObjectID());
            }

            if (!isView && !fromImport) {
                if (profile.getEmployeeCode() != null && !"".equals(profile.getEmployeeCode()) && profile.getSavedNumberFormula() != null) {
                    profileItem.setNumberData(employeeServiceLocal.generateEmployeeNumber(profile.getObjectID()));
                    profileItem.getNumberData().setNumberString(profile.getEmployeeCode());
                    profileItem.getNumberData().setIntNumber(profile.getIntNumber());
                } else {
                    EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                    profileItem.setNumberData(generateOldEmployeeNumber(profile.getEmployeeCode(), profile.getIntNumber()));
                    profileItem.getNumberData().setNumberString(profile.getEmployeeCode());
                    profileItem.getNumberData().setIntNumber(profile.getIntNumber());
                    profileItem.getNumberData().setDelimiter(settings != null ? settings.getDelimetrEmployeeNumbering() : null);
                }
            }
            profileItem.setEmpCode(profile.getEmployeeCode());
            profileItem.setSalaryMode(employee.getSalaryMode());
            profileItem.setFingerprintDeviceUuids(employee.getFingerprintDeviceUuids());

            profileItem.setWageRate(employee.getWageRate());
            profileItem.setClientChargeRate(employee.getClientChargeRate());

            EdsReference employeeQualification = employee.getQualification();
            if (employeeQualification != null) {
                profileItem.setQualificationID(employeeQualification.getObjectID());
                profileItem.setQualificationCode(employeeQualification.getCode());
                profileItem.setQualificationName(referenceWfmMessageSource.localize(employeeQualification.getCode(), employeeQualification.getName()));
            }

            if (employee.getTimeSlot() != null) {
                profileItem.setTimeslot(employee.getTimeSlot().getAsSelectItem());
            }

            if (!fromImport) {
                EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(employee);
                if (userBankAccount != null) {
                    UserBankAccountData bankAccountData = new UserBankAccountData();
                    bankAccountData.setBankName(userBankAccount.getBankName());
                    bankAccountData.setBankAddress(userBankAccount.getBankAddress());
                    bankAccountData.setAccountNumber(userBankAccount.getAccountNumber());
                    bankAccountData.setAccountName(userBankAccount.getAccountName());
                    bankAccountData.setSwiftCode(userBankAccount.getSwiftCode());
                    bankAccountData.setSortCode(userBankAccount.getSortCode());
                    bankAccountData.setIbanCode(userBankAccount.getIbanCode());
                    bankAccountData.setAgentID(userBankAccount.getAgentID());

                    profileItem.setBankAccountData(bankAccountData);
                }
            }

            if (employee.getEmployeeTeam() != null) {
                if (employee.getTeam() != null) {
                    profileItem.setPmDepartmentID(employee.getTeam().getObjectID());
                }
            }
        } else if (Constants.TC_INSTRUCTOR_ADD_FORM.equals(from) || Constants.FROM_SINGLE_EMPLOYEE_ADD.equals(from)) {
            profileItem.setNumberData(employeeServiceLocal.generateEmployeeNumber(null));

            if (employee != null && employee.getCustomFields() != null) {
                profileItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Instructor)));
            }
        }
        if (currentUser != null && (currentUser.hasRole(roleManager.get(EdsRole.ADMIN)) ||
                currentUser.hasRole(roleManager.get(EdsRole.DR)) || currentUser.hasRole(roleManager.get(EdsRole.DR)))) {
            profileItem.setEditablePermission(Constants.EDIT);
        }

        if (employee != null && profile != null) {

            profileItem.setPosition(employee.getPosition() != null ? employee.getPosition().getName() : "");
            if (employee.getPosition() != null) {
                profileItem.setPositionId(employee.getPosition().getObjectID());
            }
            if (profile.getEmploymentMode() != null) {
                profileItem.setEmpMode(profile.getEmploymentMode().getName());
                profileItem.setEmpModeId(profile.getEmploymentMode().getObjectID());
            }

            profileItem.setHireDate(employee.getStartDate() != null ? new DateNonConvertable(employee.getStartDate()) : null);
            profileItem.setFireDate(employee.getEndDate() != null ? new DateNonConvertable(employee.getEndDate()) : null);
            profileItem.setOpeningBalanceDays(employee.getOpeningBalanceDays());
            profileItem.setProbationDays(employee.getProbationDays());
            if (profile.getReportsTo() != null) {
                try {
                    String code = profile.getReportsTo().getProfile() != null ? profile.getReportsTo().getProfile().getEmployeeCode() : "";
                    profileItem.setReportsTo(((code != null && !code.trim().isEmpty()) ? code + " - " : "") + profile.getReportsTo().getFullName());
                    profileItem.setReportsToId(profile.getReportsTo().getObjectID());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                profileItem.setReportsTo(null);
                profileItem.setReportsToId(null);
            }
            profileItem.setTermsOfContract(profile.getTermsOfContract());

            if (profile.getTermsOfCMonthOrYear() != null) {
                profileItem.setTermsOfCMonthORYear(profile.getTermsOfCMonthOrYear());
            }

            if (profile.getSalaryGrade() != null) {
                profileItem.setSalaryGradeId(profile.getSalaryGrade().getObjectID());
                String desc = "";
                if (profile.getSalaryGrade().getDescription() != null && !profile.getSalaryGrade().getDescription().isEmpty()) {
                    desc = " - " + (profile.getSalaryGrade().getDescription().length() > 25 ? profile.getSalaryGrade().getDescription().substring(0, 25) : profile.getSalaryGrade().getDescription());
                }
                profileItem.setSalaryGrade(profile.getSalaryGrade() != null
                        ? profile.getSalaryGrade().getGradeCode() + " " + profile.getSalaryGrade().getGradeLevel() + desc : "");
            }


            if (profile.getVisaExpirationDate() != null) {
                profileItem.setVisaExpirationDate(new DateNonConvertable(profile.getVisaExpirationDate()));
            }
            if (profile.getVisaExpirationDateReminders() != null) {
                profileItem.setVisaExpirationDateReminder(profile.getVisaExpirationDateReminders());
            }

            if (profile.getEmpHistory() != null) {
                profileItem.setEmpHistory(profile.getEmpHistory());
            }

            if (employee.getLocation() != null) {
                profileItem.setLocationId(employee.getLocation().getObjectID());
                profileItem.setLocationName(employee.getLocation().getName());
                profileItem.setLocation(employee.getLocation().getAsSelectItem());
            }
            profileItem.setDepartment(employee.getTeam() != null ? employee.getTeam().getName() : "");
            if (employee.getRoles() != null && !employee.getRoles().isEmpty()) {
                Integer[] roleIDs = new Integer[employee.getRoles().size()];
                int j = 0;
                for (EdsRole employeeRole : employee.getRoles()) {
                    roleIDs[j] = employeeRole.getObjectID();
                    if (Constants.ESS_USER_CODE.equals(employeeRole.getCode())) {
                        profileItem.setEss(true);
                    }
                    j++;
                }
                profileItem.setRoleId(roleIDs);
            }

            if (profile.getContact() != null && profile.getContact().getJobTitles() != null && !profile.getContact().getJobTitles().equals("()")) {
                profileItem.setJobTitle(profile.getContact().getJobTitles());
            }
            Map<Integer, ArrayList<String>> telegramChats = profile.getContact().getParams(EdsCrmContactItemParams.TELEGRAM_CHATS);
            if (!telegramChats.isEmpty()) {
                ArrayList<SelectItem> chats = new ArrayList<>();
                for (Integer botId : telegramChats.keySet()) {
                    TelegramSettingsItem bot = telegramChatService.getTelegramSettingsItem(botId);
                    TelegramChatListItem chat = telegramChatService.getChat(Integer.valueOf(telegramChats.get(botId).get(0)));
                    chats.add(new SelectItem(botId, bot.getBotName(), chat.getObjectId(), chat.getChatName(), bot.getToken()));
                }
                profileItem.setTelegramChats(chats);
            }
            profileItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(),
                    commonService.getCompanyCustomFields(ViewName.Employee)));

            Set<EdsEmployeeCustomItemTable> itemTables = employee.getItemTables();

            HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

            if (itemTables != null && !itemTables.isEmpty()) {

                for (EdsEmployeeCustomItemTable itemTable : itemTables) {
                    CustomTableRpc rpc = itemTable.getRpc();

                    rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                            commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.EmployeeItemTable, rpc.getUuid())));

                    map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                }
                profileItem.setCustomTableItems(map);
            }
            Map<String, ArrayList<CustomTableRpc>> tableItems = profileItem.getCustomTableItems();

            if (!tableItems.isEmpty()) {
                for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                    tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
                }
            }

            List<EdsEmployeeExperienceItemTable> sortedExperienceItemTables = new ArrayList<>(employee.getExperienceItemTables());
            Comparator<EdsEmployeeExperienceItemTable> comparator = Comparator.comparing(EdsEmployeeExperienceItemTable::getHireDate);
            sortedExperienceItemTables.sort(comparator);
            if (!sortedExperienceItemTables.isEmpty()) {
                ExperienceTableItems[] table = new ExperienceTableItems[employee.getExperienceItemTables().size()];
                int counter = 0;
                for (EdsEmployeeExperienceItemTable item : sortedExperienceItemTables) {
                    ExperienceTableItems experienceTableItems = new ExperienceTableItems();
                    experienceTableItems.setId(item.getObjectID());
                    experienceTableItems.setHireDate(item.getHireDate());
                    experienceTableItems.setResignDate(item.getResignDate());
                    if (item.getIndustry() != null) {
                        experienceTableItems.setIndustry(item.getIndustry().getAsSelectItem());
                    }
                    experienceTableItems.setPosition(item.getPos() != null ? item.getPos().getName() : item.getPosition());
                    experienceTableItems.setDepartment(item.getDep() != null ? item.getDep().getName() : item.getDepartment());
                    experienceTableItems.setOrganization(item.getOrganization());
                    experienceTableItems.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(),
                            commonService.getCompanyCustomFields(ViewName.ExperienceItemTable)));
                    table[counter++] = experienceTableItems;
                }
                profileItem.setExperienceTableItems(table);
            }

        }
        if (Constants.TC_INSTRUCTOR_ADD_FORM.equals(from) && (profileItem.getRoleId() == null || profileItem.getRoleId().length == 0)) {
            EdsRole instructorRole = roleManager.getByCode(Constants.INSTRUCTOR_CODE);
            EdsRole memberRole = roleManager.get(EdsRole.MEM);
            ArrayList<Integer> roleIDs = new ArrayList<>();
            if (memberRole != null) {
                roleIDs.add(memberRole.getObjectID());
            }

            if (employee != null && employee.getCustomFields() != null) {
                profileItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Instructor)));
            }
            if (instructorRole != null) {
                roleIDs.add(instructorRole.getObjectID());
            }
            profileItem.setRoleId(roleIDs.toArray(new Integer[]{}));
        }
        if (Constants.TC_INSTRUCTOR_ADD_FORM.equals(from)) {
            profileItem.setCoursesMap(getCourses(objectID));

            if (employee != null && employee.getCustomFields() != null) {
                profileItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Instructor)));
            }
        }

        if (!fromImport) {
            profileItem.setLanguages(getLanguages());
        }

        if (employee != null && employee.getObjectID() != null) {
            KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsEmployee.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.VIEW);
            kpiLog.setEntityId(employee.getObjectID());
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, Constants.TC_INSTRUCTOR_ADD_FORM.equals(from) ? "View Instructor" : "View Employee");
        } else if (Constants.TC_INSTRUCTOR_ADD_FORM.equals(from)) {
            KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsEmployee.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Add Instructor");
        }
        if (objectID != null && !fromImport) {
            List<EdsEmployeePayrollSettings> epsList = employeePayrollSettingsManager.getEmployeeSettings(objectID);
            if (epsList != null && !epsList.isEmpty()) {
                HashMap<String, String> payrollSettings = new HashMap<>();
                for (EdsEmployeePayrollSettings eps : epsList) {
                    payrollSettings.put(eps.getKey(), eps.getValue());
                }
                profileItem.setPayrollSettings(payrollSettings);
                String salaryValue = profileItem.getPayrollSettings().get(Constants.SALARY);
                if (salaryValue != null && !salaryValue.isEmpty()) {
                    profileItem.setSalaryAmount(Double.parseDouble(salaryValue));
                } else {
                    profileItem.setSalaryAmount(0d);
                }

                String jobTitle = profileItem.getPayrollSettings().get(CustomFormConstants.JOB_TITLE);
                if (jobTitle != null) {
                    String jobTitleText = profileItem.getPayrollSettings().get(Constants.JOB_TITLE_TEXT);
                    profileItem.setJobTitleId(Integer.valueOf(jobTitle));
                    profileItem.setJobTitle(jobTitleText);
                }

            }
        }
        List<EdsPaymentDeduction> categories = employee != null ? employee.getCategories() : null;
        if (categories != null && !categories.isEmpty()) {
            PaymentDeductionObject object;
            for (EdsPaymentDeduction category : categories) {
                if (category != null) {
                    object = category.getRPC();
                    if (!fromImport) {
                        object.setUsed(payslipPaymentsManager.checkPaymentDeductionForUsed(category.getObjectID()));
                    }
                    if (category.getLinkedCategories() != null && !category.getLinkedCategories().isEmpty()) {
                        PaymentDeductionObject linkedObject;
                        for (EdsPayrollCategory linkedCategory : category.getLinkedCategories()) {
                            linkedObject = new PaymentDeductionObject();
                            linkedObject.setCategoryItem(linkedCategory.createPaymentDeductionSelectItem());
                            object.getLinkedCategories().add(linkedObject);
                        }
                    }
                    if (object.isPaymentCategory()) {
                        profileItem.getPaymentCategories().add(object);
                    } else if (object.isLoan()) {
                        if (!category.isFullPayed()) {
                            object.setRemainingAmount(category.getRemainingAmount());
                            profileItem.getLoanCategories().add(object);
                        }
                    } else if (object.isDeductionCategory()) {
                        profileItem.getDeductionCategories().add(object);
                    } else if (object.isTaxCategory()) {
                        profileItem.getTaxes().add(object);
                    } else if (object.isEmployerContributionCategory()) {
                        profileItem.getEmployerContributions().add(object);
                    }
                }
            }
        }
        if (employee != null) {
            wrapSpokenLanguages(employee, profileItem);
            profileItem.setemployeeDegree(profile.getEmployeeDegree() != null ? profile.getEmployeeDegree().getRPC() : null); // fixme, potential NPE on profile variable
        }
        if (!fromImport) {
            profileItem.setemployeeDegrees(commonService.convertReference2SelectItem(EdsVacancy.VACANCY_DEGREES, true, null));
        }
        if (!StringUtils.isEmpty(formType)
                && convertedFormId != null
                && employee == null
                && profile == null
                && formType.contains("_FORM")) {

            profileItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(null,
                    commonService.getCompanyCustomFields(ViewName.Employee)));

            profileItem.setConvertedRelations(EdsRelation.asRPCs(relationManager.getAllRelations(formType, convertedFormId)));

            EdsFormProperty formProperty = formPropertyManager.getByFormID(LayoutRPC.HRMS_EMPLOYEE_FORM);


            FormProperty[] fields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);

            EdsCustomFormItems edsItem = customFormItemManager.get(convertedFormId);
            FormItems formItems = edsItem.toRpc();
            Set<EdsCustomItemTable> itemTables = edsItem.getItemTables();

            HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

            if (itemTables != null && !itemTables.isEmpty()) {

                for (EdsCustomItemTable itemTable : itemTables) {
                    CustomTableRpc rpc = itemTable.getRpc();

                    rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                            commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, rpc.getUuid())));

                    map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                }
                formItems.setTableItems(map);
            }
            Map<String, ArrayList<CustomTableRpc>> tableItems = formItems.getTableItems();

            for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
            }

            formItems.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getFormCustomFields(),
                    commonServiceLocal.getCompanyCategoryCustomFields(edsItem.getCustomForm() != null ? edsItem.getCustomForm().getObjectID() : null)));

            if (formItems.getCustomFieldItems() != null && !formItems.getCustomFieldItems().isEmpty()) {
                for (int i = 0; i < formItems.getCustomFieldItems().size(); i++) {
                    if (UI_TYPE_AUTONUMBER.equals(formItems.getCustomFieldItems().get(i).getUiType()) && formItems.getCustomFieldItems().get(i).getFieldStringValue() != null) {
                        formItems.setAutoNumber(formItems.getCustomFieldItems().get(i).getFieldStringValue());
                        break;
                    }
                }
            }
            profileItem.setFromName(formItems.getAutoNumber() != null ? formItems.getAutoNumber() : formItems.getFormName() + ": " + formItems.getObjectID().toString());

            if (formItems.getCustomFieldItems() != null && !formItems.getCustomFieldItems().isEmpty()) {
                for (CompanyCustomFieldItem companyCustomFieldItem : formItems.getCustomFieldItems()) {
                    convertFormCustomFields(profileItem, fields, companyCustomFieldItem);
                }
            }
        }
        return profileItem;
    }

    private void convertFormCustomFields(ProfileItem item, FormProperty[] fields, CompanyCustomFieldItem companyCustomFieldItem) {
        if (companyCustomFieldItem != null) {
            for (FormProperty formProperty1 : fields) {
                if (formProperty1 != null) {
                    if (companyCustomFieldItem.getAliasName().equals(formProperty1.getAliasName())) {
                        switch (formProperty1.getCode()) {
                            case "FIRST_NAME" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setFirstName(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "LAST_NAME" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setLastName(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "MIDDLE_NAME" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setMiddleName(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "EMPLOYEE_CODE" -> {
                                if (companyCustomFieldItem.getUiType().equals(formProperty1.getWidget()) && DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType()) && companyCustomFieldItem.getFieldStringValue() != null) {
                                    EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                                    NumberData numberData = null;
                                    if (companyCustomFieldItem.getFieldStringValue() != null) {
                                        if (settings != null && settings.getEmployeeNumberingFormat() != null) {
                                            numberData = settings.parseNumberData(new BigDecimal(companyCustomFieldItem.getFieldStringValue()).intValue(), settings.getEmployeeNumberingFormat());
                                        } else {
                                            numberData = EdsNumberingSettings.getDefaultData(new BigDecimal(companyCustomFieldItem.getFieldStringValue()).intValue(), EdsNumberingSettings.DEF_EMPLOYEE_PREFIX);
                                        }
                                    }
                                    item.setNumberData(numberData);
                                }
                            }
                            case "BIRTH_DAY" -> {
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setBirthDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue() : null);
                                }
                            }
                            case "MARTIAL_STATUS" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getReferenceItem() != null && "_MARTIAL_STATUS".equals(companyCustomFieldItem.getReferenceItem().getCode())) {
                                    if (companyCustomFieldItem.getSelectedId() != null) {
                                        EdsReference reference = referenceManager.get(companyCustomFieldItem.getSelectedId());
                                        if (reference != null) {
                                            item.setMartialStatus(this.referenceWfmMessageSource.localize(reference.getCode(), reference.getName()));
                                            item.setMartialStatusId(reference.getObjectID());
                                        }
                                    }
                                }
                            }
                            case "EMPLOYEE_DEGREE" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getReferenceItem() != null && "VACANCY_DEGREES".equals(companyCustomFieldItem.getReferenceItem().getCode())) {
                                    if (companyCustomFieldItem.getSelectedId() != null) {
                                        EdsReference reference = referenceManager.get(companyCustomFieldItem.getSelectedId());
                                        if (reference != null) {
                                            item.setemployeeDegree(reference.getRPC());
                                        }
                                    }
                                }
                            }
                            case "QUALIFICATION" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getReferenceItem() != null && "Q_QUALIFICATION".equals(companyCustomFieldItem.getReferenceItem().getCode())) {
                                    if (companyCustomFieldItem.getSelectedId() != null) {
                                        EdsReference reference = referenceManager.get(companyCustomFieldItem.getSelectedId());
                                        if (reference != null) {
                                            item.setQualificationName(this.referenceWfmMessageSource.localize(reference.getCode(), reference.getName()));
                                            item.setQualificationID(reference.getObjectID());
                                            item.setQualificationCode(reference.getCode());
                                        }
                                    }
                                }
                            }
                            case "EMPLOYMENT_MODE" -> {
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getReferenceItem() != null && "_EMPLOYMENT_MODE".equals(companyCustomFieldItem.getReferenceItem().getCode())) {
                                    if (companyCustomFieldItem.getSelectedId() != null) {
                                        EdsReference reference = referenceManager.get(companyCustomFieldItem.getSelectedId());
                                        if (reference != null) {
                                            item.setEmpMode(reference.getName());
                                            item.setEmpModeId(reference.getObjectID());
                                        }
                                    }
                                }
                            }
                            case "PHONE" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setWorkPhone(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "EMAIL" -> {
                                if (Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    item.setWorkEmail(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "NATIONALITY" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType())) {
                                    item.setNationality(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "SUPERVISOR" -> {
                                if (companyCustomFieldItem.getUiType().equals(UI_TYPE_LOOKUP) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getSelectedId() != null) {
                                    item.setReportsToId(companyCustomFieldItem.getSelectedId());
                                    item.setReportsTo(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                            case "POSITION" -> {
                                if (companyCustomFieldItem.getUiType().equals(UI_TYPE_LOOKUP) && CustomFieldLookUpTypeEnum.POSITION.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getSelectedId() != null) {
                                    item.setPositionId(companyCustomFieldItem.getSelectedId());
                                }
                            }
                            case "DEPARTMENT" -> {
                                if (companyCustomFieldItem.getUiType().equals(UI_TYPE_LOOKUP) && CustomFieldLookUpTypeEnum.DEPARTMENT.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getSelectedId() != null) {
                                    item.setPmDepartmentID(companyCustomFieldItem.getSelectedId());
                                    item.setDepartment(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                            case "WAGE_RATE" -> {
                                if (DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType()) && Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType())) {
                                    item.setWageRate(companyCustomFieldItem.getFieldStringValue() != null ? Double.valueOf(companyCustomFieldItem.getFieldStringValue()) : 0.0);
                                }
                            }
                            case "CLIENT_CHARGE_RATE" -> {
                                if (DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType()) && Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType())) {
                                    item.setClientChargeRate(companyCustomFieldItem.getFieldStringValue() != null ? Double.valueOf(companyCustomFieldItem.getFieldStringValue()) : 0.0);
                                }
                            }
                            case "PROBATION_DAYS" -> {
                                if (DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType()) && Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType())) {
                                    item.setProbationDays(companyCustomFieldItem.getFieldStringValue() != null ? Double.valueOf(companyCustomFieldItem.getFieldStringValue()) : 0.0);
                                }
                            }
                            case "OPENING_BALANCE_DATE" -> {
                                if (DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType()) && Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType())) {
                                    item.setOpeningBalanceDays(companyCustomFieldItem.getFieldStringValue() != null ? Double.valueOf(companyCustomFieldItem.getFieldStringValue()) : 0.0);
                                }
                            }
                            case "HIRE_DATE" -> {
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setHireDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue() : null);
                                }
                            }
                            case "RESIGNATION_DATE" -> {
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setFireDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue() : null);
                                }
                            }
                            case "LOCATION_FIELD" -> {
                                if (companyCustomFieldItem.getUiType().equals(UI_TYPE_LOOKUP) && CustomFieldLookUpTypeEnum.LOCATION.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getSelectedId() != null) {
                                    item.setLocationId(companyCustomFieldItem.getSelectedId());
                                    item.setLocationName(companyCustomFieldItem.getFieldStringValue());
                                }
                            }
                            case "PASSPORT_NUMBER" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    item.setPassportNumber(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "PASSPORT_ISSUE" -> {
                                if (companyCustomFieldItem.getUiType().equals(UI_TYPE_LOOKUP) && CustomFieldLookUpTypeEnum.COUNTRY.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getSelectedId() != null) {
                                    item.setPassportIssueItem(new SelectItem(companyCustomFieldItem.getSelectedId(), companyCustomFieldItem.getFieldStringValue()));
                                }
                            }
                            case "PASSPORT_ISSUE_DATE" -> {
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setPassportIssueDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue() : null);
                                }
                            }
                            case "PASSPORT_EXPIRY_DATE" -> {
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setPassportExpiryDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue() : null);
                                }
                            }
                            case "VISA_ISSUE_DATE" -> {
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setVisaIssueDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue() : null);
                                }
                            }
                            case "VISA_EXPIRATION_DATE" -> {
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setVisaExpirationDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue() : null);
                                }
                            }
                            case "VISA_NUMBER" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    item.setVisaNumber(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "INSURANCE_NUMBER" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    item.setInsuranceNumber(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                }
                            }
                            case "INSURANCE_EXPIRY_DATE" -> {
                                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DATEPICKER_TIME.equals(companyCustomFieldItem.getUiType())) {
                                    item.setMedicalInsuranceExpireDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue() : null);
                                }
                            }
                            case "BANK_NAME" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    UserBankAccountData userBankAccountData = item.getBankAccountData();
                                    if (userBankAccountData == null) {
                                        userBankAccountData = new UserBankAccountData();
                                    }
                                    userBankAccountData.setBankName(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                    item.setBankAccountData(userBankAccountData);
                                }
                            }
                            case "BANK_ADDRESS" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    UserBankAccountData userBankAccountData = item.getBankAccountData();
                                    if (userBankAccountData == null) {
                                        userBankAccountData = new UserBankAccountData();
                                    }
                                    userBankAccountData.setBankAddress(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                    item.setBankAccountData(userBankAccountData);
                                }
                            }
                            case "AGENT_ID" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    UserBankAccountData userBankAccountData = item.getBankAccountData();
                                    if (userBankAccountData == null) {
                                        userBankAccountData = new UserBankAccountData();
                                    }
                                    userBankAccountData.setAgentID(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                    item.setBankAccountData(userBankAccountData);
                                }
                            }
                            case "ACCOUNT_NAME" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    UserBankAccountData userBankAccountData = item.getBankAccountData();
                                    if (userBankAccountData == null) {
                                        userBankAccountData = new UserBankAccountData();
                                    }
                                    userBankAccountData.setAccountName(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                    item.setBankAccountData(userBankAccountData);
                                }
                            }
                            case "ACCOUNT_NUMBER" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    UserBankAccountData userBankAccountData = item.getBankAccountData();
                                    if (userBankAccountData == null) {
                                        userBankAccountData = new UserBankAccountData();
                                    }
                                    userBankAccountData.setAccountNumber(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                    item.setBankAccountData(userBankAccountData);
                                }
                            }
                            case "SWIFT_CODE" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    UserBankAccountData userBankAccountData = item.getBankAccountData();
                                    if (userBankAccountData == null) {
                                        userBankAccountData = new UserBankAccountData();
                                    }
                                    userBankAccountData.setSwiftCode(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                    item.setBankAccountData(userBankAccountData);
                                }
                            }
                            case "IBAN_CODE" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    UserBankAccountData userBankAccountData = item.getBankAccountData();
                                    if (userBankAccountData == null) {
                                        userBankAccountData = new UserBankAccountData();
                                    }
                                    userBankAccountData.setIbanCode(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                    item.setBankAccountData(userBankAccountData);
                                }
                            }
                            case "SORT_CODE" -> {
                                if (Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_TEXTBOX_EMAIL.equals(companyCustomFieldItem.getUiType())) {
                                    UserBankAccountData userBankAccountData = item.getBankAccountData();
                                    if (userBankAccountData == null) {
                                        userBankAccountData = new UserBankAccountData();
                                    }
                                    userBankAccountData.setSortCode(companyCustomFieldItem.getFieldStringValue() != null ? companyCustomFieldItem.getFieldStringValue() : "");
                                    item.setBankAccountData(userBankAccountData);
                                }
                            }
                            case "SALARY_AMOUNT" -> {
                                if (DATA_TYPE_NUMBER.equals(companyCustomFieldItem.getDataType()) && Constants.UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType())) {
                                    item.setSalaryAmount(companyCustomFieldItem.getFieldStringValue() != null ? Double.valueOf(companyCustomFieldItem.getFieldStringValue()) : 0.0);
                                }
                            }
                            case "TIMESLOT" -> {
                                if (companyCustomFieldItem.getUiType().equals(UI_TYPE_LOOKUP) && CustomFieldLookUpTypeEnum.TIMESLOT.equals(companyCustomFieldItem.getLookUpTypeEnum()) && companyCustomFieldItem.getSelectedId() != null) {
                                    item.setTimeslot(new SelectItem(companyCustomFieldItem.getSelectedId(), companyCustomFieldItem.getFieldStringValue()));
                                }
                            }
                        }
                    }
                }
            }

            if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
                for (CompanyCustomFieldItem empCustomField : item.getCustomFields()) {
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

    private void setEmployeeLanguages(ProfileItem profileItem) {
        List<EdsSpokenLanguages> spokenLanguages = spokenLanguagesManager.getListByRelation(profileItem.getEmployeeId(), profileItem.isCandidate() ? EdsSpokenLanguages.TYPE_CANDIDATE : EdsSpokenLanguages.TYPE_EMPLOYEE);
        if (Utils.isOk(spokenLanguages)) {
            final List<SelectItem> sl = spokenLanguages.stream().filter(Objects::nonNull).map(item -> item.getLanguage().getAsSelectItem()).toList();
            profileItem.setSpokenLanguages(sl.toArray(new SelectItem[]{}));
        }
    }

    private void wrapSpokenLanguages(EdsEmployee employee, ProfileItem item) {
        ArrayList<EdsSpokenLanguages> spokenLanguages = spokenLanguagesManager.getListByRelation(employee.getObjectID(), item.isCandidate() ? EdsSpokenLanguages.TYPE_CANDIDATE : EdsSpokenLanguages.TYPE_EMPLOYEE);
        if (spokenLanguages != null) {
            ArrayList<SpokenLanguageItem> languageItems = new ArrayList<>(spokenLanguages.size());
            spokenLanguages.forEach(sl -> languageItems.add(new SpokenLanguageItem(sl.getLanguage() != null ? new SelectItem(sl.getLanguage().getObjectID(), referenceWfmMessageSource.localize(sl.getLanguage().getCode(), sl.getLanguage().getName())) : null, sl.getLevel() != null ? new SelectItem(sl.getLevel().getObjectID(), referenceWfmMessageSource.localize(sl.getLevel().getCode(), sl.getLevel().getName())) : null)));
            item.setSpokingLanguages(languageItems);
        }
    }

    private NumberData generateOldEmployeeNumber(final String employeeCode, final Integer intNumber) {
        final NumberData numberData = new NumberData();
        numberData.setIntNumber(intNumber);
        numberData.setFirstNumberString(employeeCode);
        numberData.setNumberFormat(employeeCode + "_" + intNumber);
        numberData.setNumberString(employeeCode + intNumber);
        return numberData;
    }

    private SelectItem[] getLanguages() {
        final List<EdsReference> references = this.referenceManager.listReferences(Constants.LANGUAGES);

        if (references != null && references.size() > 0) {
            final SelectItem[] languages = new SelectItem[references.size()];
            for (int i = 0; i < references.size(); i++) {
                languages[i] = new SelectItem(references.get(i).getObjectID(), (this.referenceWfmMessageSource.localize(references.get(i).getCode(), references.get(i).getName())));
            }
            return languages;
        }
        return new SelectItem[0];
    }

    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getCourses(final Integer instructorID) {

        final List<Integer> selectedCoursesIds = this.courseManager.getInstructorCoursesIds(instructorID);

        final LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> courseListItems = new LinkedHashMap<>();
        final List<EdsCourse> courses = this.courseManager.list(null);

        final KpiTreeInfo coursesTeam = new KpiTreeInfo(0, "COURSES");
        courseListItems.put(coursesTeam, new ArrayList<>());

        for (final EdsCourse course : courses) {
            if (course != null) {
                final KpiTreeInfo sItem = new KpiTreeInfo();
                sItem.setId(course.getObjectID());
                sItem.setName(course.getName());
                sItem.setDepartmentId(0);
                if (selectedCoursesIds != null && selectedCoursesIds.contains(course.getObjectID())) {
                    sItem.setSelected(true);
                }
                for (final KpiTreeInfo s : courseListItems.keySet()) {
                    courseListItems.get(s).add(sItem);
                }
            }
        }
        return courseListItems;
    }

    private boolean checkSupervisor(final Integer currentEmployeeID, final Integer supervisorId) {
        final EdsEmployee employee = this.employeeManager.get(supervisorId == null ? currentEmployeeID : supervisorId);
        if (employee.getProfile() != null && employee.getProfile().getReportsTo() != null) {
            final EdsEmployee supervisor = employee.getProfile().getReportsTo();
            if (currentEmployeeID.equals(supervisor.getObjectID())) {
                return true;
            }
            return this.checkSupervisor(currentEmployeeID, employee.getProfile().getReportsTo().getObjectID());
        }
        return false;
    }

    public ArrayList<CompanyCustomFieldItem> convertPlacementCfToEmployeeCf(EdsPlacement placement, ProfileItem profileItem) {
        String inn = "";
        String inps = "";
        String passNumber = "";
        String givenBy = "";
        String passSerie = "";

        List<CompanyCustomFieldItem> companyCustomFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(placement.getPlacementCustomFields(),
                this.commonService.getCompanyCustomFields(ViewName.Placement));
        for (CompanyCustomFieldItem companyCustomFieldItem : companyCustomFieldItems) {
            switch (companyCustomFieldItem.getAliasName()) {
                case "inn" -> inn = companyCustomFieldItem.getFieldStringValue();
                case "ИНПС" -> inps = companyCustomFieldItem.getFieldStringValue();
                case "PASSPORT_NUMBER" -> profileItem.setPassportNumber(companyCustomFieldItem.getFieldStringValue());
                case "Кем выдан документ" -> givenBy = companyCustomFieldItem.getFieldStringValue();
                case "Серия Пасспорта" -> passSerie = companyCustomFieldItem.getFieldStringValue();
                case "PASSPORT_ISSUE_DATE" ->
                        profileItem.setPassportIssueDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue() : null);
                case "PASSPORT_EXPIRY_DATE" ->
                        profileItem.setPassportExpiryDate(companyCustomFieldItem.getFieldDateNonConvertedValue() != null ? companyCustomFieldItem.getFieldDateNonConvertedValue() : null);
            }
        }

        ArrayList<CompanyCustomFieldItem> companyCustomFields = this.commonService.getCompanyCustomFields(ViewName.Employee);
        for (CompanyCustomFieldItem companyCustomField : companyCustomFields) {
            switch (companyCustomField.getAliasName()) {
                case "inn" -> companyCustomField.setFieldStringValue(inn);
                case "ИНПС" -> companyCustomField.setFieldStringValue(inps);
                case "PASSPORT_NUMBER" -> companyCustomField.setFieldStringValue(passNumber);
                case "Кем выдан документ" -> companyCustomField.setFieldStringValue(givenBy);
                case "Серия Пасспорта" -> companyCustomField.setFieldStringValue(passSerie);
            }
        }
        return companyCustomFields;
    }

    @Transactional
    public Integer updateProfile(ProfileItem editProfile) {
        Integer existingEmployeeID = editProfile.getEmployeeId();
        try {
            if (Constants.TC_INSTRUCTOR_ADD_FORM.equals(editProfile.getFrom()) && editProfile.getEmployeeId() == null) {
                final NewEmployee newEmployee = new NewEmployee();
                newEmployee.setCreatedFrom(Constants.TC_INSTRUCTOR_ADD_FORM);
                newEmployee.setFname(editProfile.getFirstName());
                newEmployee.setMname(editProfile.getMiddleName());
                newEmployee.setLname(editProfile.getLastName());
                if (editProfile.getEmail() != null && !"".equals(editProfile.getEmail())) {
                    newEmployee.setEmail(editProfile.getEmail());
                }
                if (editProfile.getPmDepartmentID() != null) {
                    newEmployee.setDepartment(editProfile.getPmDepartmentID());
                }
                if (editProfile.getPositionId() != null) {
                    newEmployee.setPositionId(editProfile.getPositionId());
                }
                if (editProfile.getLocationId() != null) {
                    newEmployee.setLocationId(editProfile.getLocationId());
                }
                final EdsRole instructorRole = this.roleManager.getByCode(Constants.INSTRUCTOR_CODE);
                if (instructorRole != null) {
                    newEmployee.setRole(instructorRole.getObjectID());
                }

                final Integer instructorID = this.employeeServiceLocal.createEmployeeInternal(newEmployee, null);
                if (instructorID != null) {
                    if (instructorID < 1) {
                        return instructorID;
                    }
                    editProfile.setEmployeeId(instructorID);
                    final EdsEmployeeProfile profile = this.profileManager.getProfile(instructorID);
                    if (profile != null) {
                        final EdsCrmContact contact = profile.getContact();
                        if (contact != null) {
                            editProfile.setContactID(contact.getObjectID());
                        }
                    }
                    existingEmployeeID = this.updateExistingProfile(editProfile);
                    return existingEmployeeID;
                }
            } else {
                existingEmployeeID = this.updateExistingProfile(editProfile);
                return existingEmployeeID;
            }
        } catch (final Throwable throwable) {
            HrmsServiceImpl.log.error("Unexpected exception:", throwable);
            throw new RuntimeException(throwable);
        }
        return existingEmployeeID;
    }

    private Integer updateExistingProfile(final ProfileItem editProfile) {
        final EdsUser user = this.userManager.getUser();
        final EdsEmployeeProfile employeeProfile;
        final EdsEmployee employee = this.employeeManager.get(editProfile.getEmployeeId());
        employee.clear();
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEmployee.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        kpiLog.setEntityId(employee.getObjectID());
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, Constants.TC_INSTRUCTOR_ADD_FORM.equals(editProfile.getFrom())
                ? "Update Instructor"
                : "Update Employee");
        if (editProfile.getEmployeeId() != null) {
            employeeProfile = this.profileManager.getProfile(editProfile.getEmployeeId());
        } else {
            employeeProfile = this.profileManager.getProfile();
        }
        final Integer employeeProfileObjectID = employeeProfile.getObjectID();

        final EdsEmployee oldReportsTo = employeeProfile.getReportsTo();
        boolean isSupervisorChanged = false;

        if ((editProfile.getHireDate() != null && employee.getStartDate() == null)
                || (editProfile.getHireDate() == null && employee.getStartDate() != null)
                || (editProfile.getHireDate() != null && employee.getStartDate() != null
                && !editProfile.getHireDate().getNonConvertedDate().equals(employee.getStartDate()))) {
            if (labourPeriodManager.isUsedEmployeeLabourPeriod(employee.getObjectID())) {
                return Errors.EMPLOYEE_LABOUR_PERIOD_USED;
            } else {
                labourPeriodManager.clearEmployeeLabourPeriod(employee.getObjectID());
                createLabourPeriodToEmployee(employee, editProfile.getHireDate() != null ? editProfile.getHireDate().getNonConvertedDate() : null);
            }
        } else {
            List<EdsLabourPeriod> periodsByEmployeeId = labourPeriodManager.periodListByEmployeeId(editProfile.getEmployeeId());
            if (editProfile.getHireDate() != null && (periodsByEmployeeId == null || periodsByEmployeeId.size() == 0)) {
                createLabourPeriodToEmployee(employee, editProfile.getHireDate() != null ? editProfile.getHireDate().getNonConvertedDate() : null);
            }
        }

        if (editProfile.getReportsToId() != null) {
            employeeProfile.setReportsTo(this.employeeManager.get(editProfile.getReportsToId()));
        } else {
            employeeProfile.setReportsTo(null);
        }

        final EdsEmployee reportsTo = employeeProfile.getReportsTo();

        if (reportsTo == null ? editProfile.getReportsToId() != null : reportsTo.getObjectID().equals(editProfile.getReportsToId())) {

            final boolean circular = this.checkSupervisor(editProfile.getEmployeeId(), null);
            if (circular) {
                employeeProfile.setReportsTo(null);
                return Errors.SUPERVISOR_CIRCULAR_REFERENCE;
            }
        }

        if (oldReportsTo != null && !oldReportsTo.equals(employeeProfile.getReportsTo())) {
            isSupervisorChanged = true;
        } else if (oldReportsTo == null && employeeProfile.getReportsTo() != null) {
            isSupervisorChanged = true;
        } else if (oldReportsTo != null && employeeProfile.getReportsTo() == null) {
            isSupervisorChanged = true;
        }
        if (isSupervisorChanged) {
            this.baseEventPostProcessor.registerEvent(EmployeeSupervisorChangeEventListenerImpl.TYPE, EmployeeSupervisorChangeEventListenerImpl.SOLR_UPDATE, employee, user);
            //for supervisor structure
            Integer employeeGraphChartMapSize = RedisClient.getKey("EmployeeGraphChartMapSize_" + SecurityContext.getCompanyID(), Integer.class);
            if (employeeGraphChartMapSize != null && employeeGraphChartMapSize != 0) {
                RedisClient.removeKey("EmployeeGraphChartMapSize_" + SecurityContext.getCompanyID());
                RedisClient.setKey("employeeGraphChartMapIsChanged_" + SecurityContext.getCompanyID(), true, Boolean.class);
                for (int i = 2; i <= employeeGraphChartMapSize; i++) {
                    RedisClient.removeKey("EmployeeGraphChart_" + i + "_level_" + SecurityContext.getCompanyID());
                }
                EdsBusinessEvent event = baseEventPostProcessor.registerEvent(NewEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, null, null);
                event.setUpdateOrgChartCash(true);
            }
        }

        employee.setNewUser(Constants.EMPLOYEE_IMPORT.equals(editProfile.getFrom()));
        Boolean isEssUser = Boolean.FALSE;
        Boolean isNoAccess = Boolean.FALSE;
        final EdsRole ess = this.roleManager.getByCode(Constants.ESS_USER_CODE);
        if (employee.getRoles().contains(ess)) {
            isEssUser = Boolean.TRUE;
        }
        if (Constants.EMPLOYEE_STATUS_NO_ACCCESS.equals(employee.getAccountStatus().getCode())) {
            isNoAccess = Boolean.TRUE;
        }
        if (!editProfile.getEss().equals(isEssUser) || !editProfile.getNoAccess().equals(isNoAccess)) {
            final Integer limit = this.employeeServiceLocal.checkUserLimit(editProfile.getEss(), !editProfile.getNoAccess(), null);
            if (limit < 0) {
                return limit;
            }
        }

        final String statusCode = editProfile.getStatusCode();
        if (statusCode != null && !employee.getAccountStatus().getCode().equals(statusCode)) {
            if (Constants.EMPLOYEE_STATUS_ACTIVE.equals(statusCode)) {
                if (Constants.EMPLOYEE_STATUS_NO_ACCCESS.equals(employee.getAccountStatus().getCode())) {
                    if (editProfile.getEmail() != null && !"".equals(editProfile.getEmail())) {
                        if (employee.getEmail() == null || !editProfile.getEmail().equals(employee.getEmail())) {
                            if (isEmailAvailable(editProfile.getEmail(), user.getCompany().getObjectID()) == EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS) {
                                return EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS;
                            }
                            if (!EmailAddressValidator.checkHost(editProfile.getEmail())) {
                                return EMPLOYEE_WITH_THIS_EMAIL_HOST_DOES_NOT_EXIST;
                            }
                            employee.setUserName(editProfile.getEmail());
                        }
                    }
                    //GRANT ACCESS
                    final Boolean successfull = this.employeeServiceLocal.grantAccessToEmployee(employee.getObjectID(), true, false, editProfile.getEss());
                    if (!successfull) {
                        return Errors.EMPLOYEE_STATUS_NO_ACCESS;
                    }
                }
            } else if (Constants.EMPLOYEE_STATUS_INACTIVE.equals(statusCode)) {
                //DEACTIVATE
                this.employeeServiceLocal.activateOrDisactivateEmployee(employee.getObjectID(), false, false);
            } else if (Constants.EMPLOYEE_STATUS_NO_ACCCESS.equals(statusCode)) {
                //REVOKE ACCESS
                this.employeeServiceLocal.grantAccessToEmployee(employee.getObjectID(), false, false);
            }
        }

        if (editProfile.getNoAccess() != null && editProfile.getNoAccess() && !employee.getAccountStatus().getCode().equals(Constants.EMPLOYEE_STATUS_NO_ACCCESS)) {
            this.employeeServiceLocal.grantAccessToEmployee(employee.getObjectID(), false, false);
        }

        if (editProfile.getAttachments() != null && editProfile.getAttachments().length > 0) {
            this.saveEmployeeProfileAttachments(editProfile.getAttachments(), employee.getObjectID());
        }

        Date birthDateN = null;
        Date birthDateOld = null;
        if (editProfile.getBirthDate() != null) {
            birthDateN = editProfile.getBirthDate().getNonConvertedDate();
        }
        if (employeeProfile.getContact() != null) {
            birthDateOld = employeeProfile.getContact().getDateOfBirth();
        }

        if (!ServerUtils.equalsDate(birthDateOld, birthDateN)) {
            employee.addCustomFieldChanges(employee.getChangedField(CustomFormConstants.BIRTH_DAY, birthDateN));
            employee.addHistoryChange("Date of Birth", birthDateOld, birthDateN);
        }

        long start = System.currentTimeMillis();
        final Integer contactID = this.saveContact(editProfile.getContactID(), editProfile);
        HrmsServiceImpl.log.info("----saveContact took:" + (System.currentTimeMillis() - start));

        if (contactID != null) {
            employeeProfile.setContact(this.crmContactManager.get(contactID));
            if (editProfile.getCompanyPhotoId() != null) {
                final EdsUpload companyPhoto = (EdsUpload) this.uploadManager.get(editProfile.getCompanyPhotoId());
                employeeProfile.getContact().setCompanyPhoto(companyPhoto);
            }
        }
        if (editProfile.getEmail() != null && !"".equals(editProfile.getEmail())) {
            employee.setEmail(editProfile.getEmail());
        }
        employee.setFirstName(editProfile.getFirstName());
        employee.setMiddleName(editProfile.getMiddleName());
        employee.setLastName(editProfile.getLastName());
        final Long driverNumber = employee.getDriverNumber();
        employee.setDriverNumber(null);
        employee.setPaymentMethod(editProfile.getPaymentMethod());
        employee.setSalaryMode(editProfile.getSalaryMode());
        List<EdsAttendanceTerminal> fingerprintSetup = attendanceTerminalManager.getAll();
        Integer[] fingerprintDeviceIds = editProfile.getFingerprintDeviceId();
        List<Integer> fingerprintIdList = fingerprintDeviceIds != null ? List.of(fingerprintDeviceIds) : List.of();
        List<String> unique = fingerprintSetup.stream()
                .filter(fs -> fingerprintIdList.contains(fs.getObjectID()))
                .map(EdsAttendanceTerminal::getCompanyUniqueID)
                .toList();
        employee.setFingerprintDeviceUuids(unique);

        boolean isDriverNumberChanged = false;
        if (editProfile.getDriverID() != null && !editProfile.getDriverID().isEmpty()) {
            if (driverNumber == null || !String.valueOf(driverNumber).equals(editProfile.getDriverID())) {
                isDriverNumberChanged = true;
            }
            employee.setDriverNumber(Long.valueOf(editProfile.getDriverID()));
        }
        //update cash advance driver ID
        if (isDriverNumberChanged) {
            this.baseEventPostProcessor.registerEvent(EmployeeEventListenerImpl.TYPE, EmployeeEventListenerImpl.SOLR_UPDATE, employee, user);
        }

        employee.setLastUpdateTime(new Date());
        employeeProfile.setGender(editProfile.getGender());

        if (editProfile.getMartialStatusId() != null) {
            employeeProfile.setMartialStatus(this.referenceManager.get(editProfile.getMartialStatusId()));
        }
//        if (Constants.COO_CONNECT.equals(editProfile.getFrom())) {
//            employeeProfile.setAvailableForCooMembers(editProfile.isAvailableForCooMembers());
//        }

        if (editProfile.getJobTitle() != null) {
            employeeProfile.getContact().setJobTitles(editProfile.getJobTitle());
        }

        employeeProfile.setEmployeeCode(editProfile.getEmpCode() != null ? editProfile.getEmpCode() : employeeProfile.getEmployeeCode());
        if (editProfile.getNumberData() != null) {
            employeeProfile.setIntNumber(editProfile.getNumberData().getIntNumber());
            employeeProfile.setSavedNumberFormula(editProfile.getNumberData().getSavedNumberFormula());
        }

        employee.setStartDate(editProfile.getHireDate() != null && editProfile.getHireDate().getDate() != null ? editProfile.getHireDate().getNonConvertedDate() : null);
        employee.setEndDate(editProfile.getFireDate() != null && editProfile.getFireDate().getDate() != null ? editProfile.getFireDate().getNonConvertedDate() : null);
        employee.setOpeningBalanceDays(editProfile.getOpeningBalanceDays());
        employee.setProbationDays(editProfile.getProbationDays());
        employee.setImportFileID(editProfile.getImportFileID());
        //for history changes
        final String monthOrYear1 = employeeProfile.getTermsOfCMonthOrYear() != null ? employeeProfile.getTermsOfCMonthOrYear().equals(1) ? "Month" : "Year" : "";
        final String monthOrYear2 = editProfile.getTermsOfCMonthORYear() != null ? editProfile.getTermsOfCMonthORYear().equals(1) ? "Month" : "Year" : "";
        if (!ServerUtils.equalsInteger(employeeProfile.getTermsOfContract(), editProfile.getTermsOfContract()) ||
                !ServerUtils.equalsInteger(employeeProfile.getTermsOfCMonthOrYear(), editProfile.getTermsOfCMonthORYear())) {
            employee.addHistoryChange("Employment Contract Terms", employeeProfile.getTermsOfContract() != null ? employeeProfile.getTermsOfContract() + " " + monthOrYear1 : "",
                    editProfile.getTermsOfContract() != null ? editProfile.getTermsOfContract() + " " + monthOrYear2 : "");
        }
        employeeProfile.setTermsOfContract(editProfile.getTermsOfContract());

        if (editProfile.getTermsOfCMonthORYear() != null) {
            employeeProfile.setTermsOfCMonthOrYear(editProfile.getTermsOfCMonthORYear());//
        }

        if (editProfile.getEmpModeId() != null) {
            employeeProfile.setEmploymentMode(this.referenceManager.get(editProfile.getEmpModeId()));
        } else {
            employeeProfile.setEmploymentMode(null);
        }
        if (editProfile.getSalaryGradeId() != null) {
            employeeProfile.setSalaryGrade(this.gradeManager.get(editProfile.getSalaryGradeId()));
        }
        if (editProfile.getSalaryAmount() != null && ServerUtils.hasPermission(PermissionConstants.EMP_PROFILE_BASIC_SALARY)) {
            employee.addCustomFieldChanges(employee.getChangedField(CustomFormConstants.SALARY_AMOUNT, editProfile.getSalaryAmount()));

            employeeProfile.setSalaryAmount(editProfile.getSalaryAmount());
            this.employeePayrollSettingsManager.update(employee, Constants.SALARY, String.valueOf(editProfile.getSalaryAmount()));
            editProfile.getPayrollSettings().put(Constants.SALARY, String.valueOf(editProfile.getSalaryAmount()));

//            SalaryHistory salaryHistory = new SalaryHistory();
//            salaryHistory.setEmployeeId(employee.getObjectID());
//            salaryHistory.setSalary(BigDecimal.valueOf(editProfile.getSalaryAmount()));
//            salaryHistory.setEffectiveDate(new DateNonConvertable(employee.getStartDate() != null ? employee.getStartDate() : new Date()));
//            salaryHistory.setRelationId(employee.getProfile().getObjectID());
//            salaryHistory.setRelationType(EdsSalaryHistory.TYPE_PROFILE);
//            salaryHistoryLocal.save(salaryHistory);
        }
        if (editProfile.getJobTitleId() != null) {
            this.employeePayrollSettingsManager.update(employee, CustomFormConstants.JOB_TITLE, String.valueOf(editProfile.getJobTitleId()));
            this.employeePayrollSettingsManager.update(employee, Constants.JOB_TITLE_TEXT, editProfile.getJobTitle());
        }
        boolean hasVisaExpirationDate = false;
        if (editProfile.getVisaExpirationDate() != null && editProfile.getVisaExpirationDate().getNonConvertedDate() != null) {
            employeeProfile.setVisaExpirationDate(editProfile.getVisaExpirationDate().getNonConvertedDate());
            hasVisaExpirationDate = true;
        } else {
            employeeProfile.setVisaExpirationDate(null);
        }
        final ArrayList<CalendarEventReminder> visaExpirationDateReminder = editProfile.getVisaExpirationDateReminder();
        if (hasVisaExpirationDate && employeeProfileObjectID != null) {
            final ArrayList<CalendarEventReminder> oldReminders = employeeProfile.getVisaExpirationDateReminders();
            final ArrayList<CalendarEventReminder> newReminders = editProfile.getVisaExpirationDateReminder();
            if (!this.equalsListReminders(oldReminders, newReminders)) {
                final String r1 = oldReminders.size() > 0 ? this.remindersAsString(oldReminders, null, false) : null;
                final String r2 = newReminders.size() > 0 ? this.remindersAsString(newReminders, editProfile.getVisaExpirationDate().getNonConvertedDate(), true) : null;
                if (!ServerUtils.equalsString(r1, r2)) {
                    employee.addHistoryChange("Expiry Reminder", r1, r2);
                }
            }
            //clear employee visa expiration reminders
            this.profileManager.deleteEmployeeVisaExpirationReminder(employeeProfileObjectID);
            //
            final List<EdsRecurrence> recurrenceList = this.recurrenceManager.getRecurrenceJobList(SchedulerConstant.EMPLOYEE_VISA_EXPIRATION_REMINDER, employeeProfileObjectID, employee.getCompany().getObjectID());
            if (recurrenceList != null && recurrenceList.size() > 0) {
                for (final EdsRecurrence rec : recurrenceList) {
                    this.recurrenceService.updateRecurrence(rec, true, true);
                }
            }
            if (visaExpirationDateReminder != null && visaExpirationDateReminder.size() > 0) {
                final RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
                recurrenceJobItem.setEnabled(true);
                recurrenceJobItem.setType(SchedulerConstant.RECURRENCE_TYPE_YEARLY);
                recurrenceJobItem.setJobType(SchedulerConstant.EMPLOYEE_VISA_EXPIRATION_REMINDER);
                recurrenceJobItem.setBusObjectId(employeeProfileObjectID);
                recurrenceJobItem.setInterval(1);
                recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                recurrenceJobItem.setEndType(SchedulerConstant.END_BY_DATE);

                for (final CalendarEventReminder reminder : visaExpirationDateReminder) {
                    if (reminder.getReminderTimes() != null) {
                        final Date recStartDate = DateUtil.addMinutes(employeeProfile.getVisaExpirationDate(), (-1) * reminder.getReminderTimes());
                        if (recStartDate.after(new Date())) {
                            recurrenceJobItem.setEndDate(DateUtil.addMinutes(recStartDate, 5));
                            recurrenceJobItem.setStartDate(recStartDate);
                            recurrenceJobItem.setBusObjectParams(reminder.getReminderTimes().toString());
                            recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                            recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());

                            recurrenceJobItem.setStartDate(recStartDate);
                            recurrenceJobItem.setYearlyMonth(recStartDate.getMonth() + 1);
                            recurrenceJobItem.setMonthlyOrYearlyDay(recStartDate.getDate());
                            this.recurrenceService.saveRecurrenceJob(recurrenceJobItem);

                            //create/update employee visa expiration date reminder
                            final EdsEmployeeProfileVisaExpirationReminder visaExpirationReminder = new EdsEmployeeProfileVisaExpirationReminder();
                            visaExpirationReminder.setMinutes(reminder.getReminderTimes());
                            visaExpirationReminder.setEmployeeProfile(employeeProfile);
                            employeeProfile.getVisaExpirationReminders().add(visaExpirationReminder);
                        }
                    }
                }
            }
        }
        if (editProfile.getEmpHistory() != null) {
            employeeProfile.setEmpHistory(editProfile.getEmpHistory());
        }
        final EdsPosition position = editProfile.getPositionId() != null ? this.positionManager.get(editProfile.getPositionId()) : null;
        if (position != null) {
            employee.setPosition(position);
            if (StringUtils.isNotEmpty(position.getCount()) && position.getStatus() != null && !position.getStatus().getCode().equals(POS_STATUS_FROZEN)) {
                Long empCount = employeeManager.getEmployeesCountByPosition(position);
                EdsReference openStatus = referenceManager.findReference(POS_STATUS, POS_STATUS_OPEN);
                EdsReference compStatus = referenceManager.findReference(POS_STATUS, POS_STATUS_ACTIVE);
                if (Integer.valueOf(position.getCount()) <= empCount + 1) {
                    position.setStatus(compStatus.getObjectID());
                } else {
                    position.setStatus(openStatus.getObjectID());
                }
                positionManager.update(position);
                try {
                    solrManager.indexAddPosition(Collections.singletonList(position), userManager.getUser().getCompany().getObjectID());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SolrServerException e) {
                    e.printStackTrace();
                }
            }
        }

        if (editProfile.getPmDepartmentID() != null) {
            if (employee.getEmployeeTeam() == null || employee.getEmployeeTeam().getTeam() == null || !editProfile.getPmDepartmentID().equals(employee.getEmployeeTeam().getTeam().getObjectID())) {
                this.departmentService.saveEmployeeDepartment(new HashSet<>(Collections.singletonList(employee.getObjectID())), editProfile.getPmDepartmentID(), true, false, editProfile.getDeptStartDate(), false);
            }
        }

        for (final Map.Entry<String, String> payrollSetting : editProfile.getPayrollSettings().entrySet()) {
            if (payrollSetting.getValue() == null) {
                continue;
            }
            EdsEmployeePayrollSettings eps = this.employeePayrollSettingsManager.getEmployeeSettingValue(editProfile.getEmployeeId(), payrollSetting.getKey());
            if (payrollSetting.getValue().equals(PayrollConstants.EMPTY_VALUE)) {
                if (eps != null) {
                    this.employeePayrollSettingsManager.delete(eps);
                }
                continue;
            }
            if (eps == null) {
                eps = new EdsEmployeePayrollSettings();
            }
            eps.setEmployeeId(employee.getObjectID());
            eps.setKey(payrollSetting.getKey());
            eps.setValue(payrollSetting.getValue());
            this.employeePayrollSettingsManager.createOrUpdate(eps);
        }

        EdsPaymentDeduction newPaymentDeduction;
        if (editProfile.getPayments() != null && editProfile.getPayments().size() > 0) {
            for (final PaymentDeductionObject paymentOrDeductionItem : editProfile.getPayments()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = this.paymentDeductionManager.get(paymentOrDeductionItem.getId());
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem() != null ? paymentOrDeductionItem.getCategoryItem().getId() : null);
                newPaymentDeduction.setEmployeeId(employee.getObjectID());
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                newPaymentDeduction.setPaymentType(paymentOrDeductionItem.getPaymentType() != null ? paymentOrDeductionItem.getPaymentType() : EPPaymentType.RECURRING);
                newPaymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentOrDeductionItem.getPaymentType()));

                this.paymentDeductionManager.createOrUpdate(newPaymentDeduction);
            }
        }

        if (editProfile.getDeductions() != null && editProfile.getDeductions().size() > 0) {
            for (final PaymentDeductionObject paymentOrDeductionItem : editProfile.getDeductions()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = this.paymentDeductionManager.get(paymentOrDeductionItem.getId());
                    this.paymentDeductionManager.get(paymentOrDeductionItem.getId()).getLinkedCategories().clear();
                    this.paymentDeductionManager.flush();
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem() != null ? paymentOrDeductionItem.getCategoryItem().getId() : null);
                newPaymentDeduction.setEmployeeId(employee.getObjectID());
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                newPaymentDeduction.setPaymentType(paymentOrDeductionItem.getPaymentType() != null ? paymentOrDeductionItem.getPaymentType() : EPPaymentType.RECURRING);
                newPaymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentOrDeductionItem.getPaymentType()));
                newPaymentDeduction.setFromAllAllowances(paymentOrDeductionItem.isFromAllAllowances());
                newPaymentDeduction.getLinkedCategories().clear();
                this.paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                if (paymentOrDeductionItem.getLinkedCategories() != null && paymentOrDeductionItem.getLinkedCategories().size() > 0) {
                    EdsPayrollCategory category;
                    for (final PaymentDeductionObject linkedCategory : paymentOrDeductionItem.getLinkedCategories()) {
                        category = this.categoryManager.get(linkedCategory.getCategoryItem().getId());
                        if (category != null) {
                            category.addPaymentDeduction(newPaymentDeduction);
                        }
                    }
                }
            }
        }

        if (editProfile.getTaxes() != null && editProfile.getTaxes().size() > 0) {
            for (final PaymentDeductionObject paymentOrDeductionItem : editProfile.getTaxes()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = this.paymentDeductionManager.get(paymentOrDeductionItem.getId());
                    this.paymentDeductionManager.get(paymentOrDeductionItem.getId()).getLinkedCategories().clear();
                    this.paymentDeductionManager.flush();
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem() != null ? paymentOrDeductionItem.getCategoryItem().getId() : null);
                newPaymentDeduction.setEmployeeId(employee.getObjectID());
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                newPaymentDeduction.setPaymentType(paymentOrDeductionItem.getPaymentType() != null ? paymentOrDeductionItem.getPaymentType() : EPPaymentType.RECURRING);
                newPaymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentOrDeductionItem.getPaymentType()));
                newPaymentDeduction.setFromAllAllowances(paymentOrDeductionItem.isFromAllAllowances());
                newPaymentDeduction.getLinkedCategories().clear();
                this.paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                if (paymentOrDeductionItem.getLinkedCategories() != null && paymentOrDeductionItem.getLinkedCategories().size() > 0) {
                    EdsPayrollCategory category;
                    for (final PaymentDeductionObject linkedCategory : paymentOrDeductionItem.getLinkedCategories()) {
                        category = this.categoryManager.get(linkedCategory.getCategoryItem().getId());
                        if (category != null) {
                            category.addPaymentDeduction(newPaymentDeduction);
                        }
                    }
                }
            }
        }

        if (editProfile.getEmployerContributions() != null && editProfile.getEmployerContributions().size() > 0) {
            for (final PaymentDeductionObject paymentOrDeductionItem : editProfile.getEmployerContributions()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = this.paymentDeductionManager.get(paymentOrDeductionItem.getId());
                    this.paymentDeductionManager.get(paymentOrDeductionItem.getId()).getLinkedCategories().clear();
                    this.paymentDeductionManager.flush();
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem().getId());
                newPaymentDeduction.setEmployeeId(employee.getObjectID());
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                newPaymentDeduction.setPaymentType(paymentOrDeductionItem.getPaymentType() != null ? paymentOrDeductionItem.getPaymentType() : EPPaymentType.RECURRING);
                newPaymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentOrDeductionItem.getPaymentType()));
                newPaymentDeduction.setFromAllAllowances(paymentOrDeductionItem.isFromAllAllowances());
                newPaymentDeduction.getLinkedCategories().clear();
                this.paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                if (paymentOrDeductionItem.getLinkedCategories() != null && paymentOrDeductionItem.getLinkedCategories().size() > 0) {
                    EdsPayrollCategory category;
                    for (final PaymentDeductionObject linkedCategory : paymentOrDeductionItem.getLinkedCategories()) {
                        category = this.categoryManager.get(linkedCategory.getCategoryItem().getId());
                        if (category != null) {
                            category.addPaymentDeduction(newPaymentDeduction);
                        }
                    }
                }
            }
        }

        if (editProfile.getLoans() != null && editProfile.getLoans().size() > 0) {
            for (final PaymentDeductionObject paymentOrDeductionItem : editProfile.getLoans()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = this.paymentDeductionManager.get(paymentOrDeductionItem.getId());
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem().getId());
                newPaymentDeduction.setEmployeeId(employee.getObjectID());
                if (paymentOrDeductionItem.getPercentage() != null && paymentOrDeductionItem.getPercentage().compareTo(BigDecimal.ZERO) != 0) {
                    newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                }
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setStartDate(paymentOrDeductionItem.getStarttDate().getNonConvertedDate());
                newPaymentDeduction.setTotalAmount(paymentOrDeductionItem.getTotalAmount());
                newPaymentDeduction.setRecurring(true);
                this.paymentDeductionManager.createOrUpdate(newPaymentDeduction);
            }
        }


        Set<EdsEmployeeExperienceItemTable> items = new HashSet<>();
        if (editProfile.getExperienceTableItems() != null) {
            for (ExperienceTableItems experienceTableItem : editProfile.getExperienceTableItems()) {
                if (experienceTableItem.getHireDate() != null) {
                    EdsEmployeeExperienceItemTable edsEmployeeExperienceItemTable = employeeExperienceItemTableManager.get(experienceTableItem.getId()) != null ? employeeExperienceItemTableManager.get(experienceTableItem.getId()) : new EdsEmployeeExperienceItemTable();
                    if (experienceTableItem.getHireDate() != null) {
                        edsEmployeeExperienceItemTable.setHireDate(experienceTableItem.getHireDate());
                    }
                    if (experienceTableItem.getResignDate() != null) {
                        edsEmployeeExperienceItemTable.setResignDate(experienceTableItem.getResignDate());
                    }
                    if (experienceTableItem.getPosition() != null) {
                        edsEmployeeExperienceItemTable.setPosition(experienceTableItem.getPosition());
                    }
                    if (experienceTableItem.getIndustry() != null) {
                        edsEmployeeExperienceItemTable.setIndustryId(experienceTableItem.getIndustry().getId());
                    }
                    if (experienceTableItem.getDepartment() != null) {
                        edsEmployeeExperienceItemTable.setDepartment(experienceTableItem.getDepartment());
                    }
                    if (experienceTableItem.getOrganization() != null) {
                        edsEmployeeExperienceItemTable.setOrganization(experienceTableItem.getOrganization());
                    }
                    edsEmployeeExperienceItemTable.setCustomFields(saveExperienceItemCustomFields(edsEmployeeExperienceItemTable.getCustomFields(), experienceTableItem.getItemCustomFields()));
                    edsEmployeeExperienceItemTable.setEdsEmployee(employee);
                    employeeExperienceItemTableManager.createOrUpdate(edsEmployeeExperienceItemTable);
                    items.add(edsEmployeeExperienceItemTable);
                }
            }
        }
        employee.setExperienceItemTables(items);

        if (editProfile.getDeletedCategories() != null && editProfile.getDeletedCategories().size() > 0) {
            for (final Integer id : editProfile.getDeletedCategories()) {
                this.paymentDeductionManager.deletePaymentOrDeduction(id);
            }
        }

        if (editProfile.getInactiveCategories() != null && editProfile.getInactiveCategories().size() > 0) {
            EdsPaymentDeduction paymentDeduction;
            for (final Integer id : editProfile.getInactiveCategories()) {
                paymentDeduction = this.paymentDeductionManager.get(id);
                if (paymentDeduction != null) {
                    paymentDeduction.setRecurring(false);
                }
            }
        }

        /* WAGE RATE RELATED*/
        Date applyDate = employee.getCompany().getCompanyDate();
        boolean applyRate = false;
        if (editProfile.getApplyWageRateFrom() != null && !employee.getWageRate().equals(editProfile.getWageRate())) {
            this.timesheetManager.updateWageRate(employee.getObjectID(), editProfile.getWageRate(), editProfile.getApplyWageRateFrom());
            applyDate = editProfile.getApplyWageRateFrom();
            applyRate = true;
        }
        if (editProfile.getApplyClientChargeRateFrom() != null && !employee.getClientChargeRate().equals(editProfile.getClientChargeRate())) {
            this.timesheetManager.updateClientChargeRate(employee.getObjectID(), editProfile.getClientChargeRate(), editProfile.getApplyClientChargeRateFrom());
            applyDate = editProfile.getApplyClientChargeRateFrom();
            applyRate = true;
        }
        if (applyRate) {
            try {
                final List<EmployeeWageClientRateHistory> rates = new ArrayList<>();
                for (final EmployeeWageClientRateHistory wageClientRateHistory : employee.getWageClientRatesHistory()) {
                    if (applyDate.after(wageClientRateHistory.getChangeDate())) {
                        rates.add(wageClientRateHistory);
                    }
                }
                employee.getWageClientRatesHistory().clear();
                employee.getWageClientRatesHistory().addAll(rates);
                this.projectEmployeeManager.updateProjectWageRates(employee.getObjectID(), editProfile.getWageRate(), editProfile.getClientChargeRate(), applyDate);
            } catch (final Exception ex) {
                ex.printStackTrace();
            }

        }


        if (!employee.getWageRate().equals(editProfile.getWageRate()) || !employee.getClientChargeRate().equals(editProfile.getClientChargeRate()) || applyRate) {
            if (editProfile.getWageRate() != null) {
                employee.setWageRate(editProfile.getWageRate());
            }
            if (editProfile.getClientChargeRate() != null) {
                employee.setClientChargeRate(editProfile.getClientChargeRate());
            }
            final EmployeeWageClientRateHistory hist = new EmployeeWageClientRateHistory();
            hist.setChangeDate(applyDate);
            hist.setWageRate(employee.getWageRate());
            hist.setClientChargeRate(employee.getClientChargeRate());
            hist.setEmployee(employee);
            employee.getWageClientRatesHistory().add(hist);
        }
        /* END WAGE RATE RELATED*/

        //employee qualifications
        if (editProfile.getQualificationID() != null) {
            employee.setQualification(this.referenceManager.get(editProfile.getQualificationID()));
        } else {
            employee.setQualification(null);
        }

        EdsTimeSlot timeslot = editProfile.getTimeslot() != null ? timeSlotManager.get(editProfile.getTimeslot().getId()) : null;
        if (timeslot != null && !Objects.equals(employee.getTimeSlot(), timeslot)) {
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(TimeslotEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, null, user);
            event.setSourceID(timeslot.getObjectID());
            event.setCustomStringField(employee.getObjectID().toString());
        }
        employee.setTimeSlot(timeslot);

        final EdsLocation location = editProfile.getLocationId() != null ? this.locationManager.get(editProfile.getLocationId()) : null;
        if (!Objects.equals(employee.getLocation(), location)) {
            this.baseEventPostProcessor.registerEvent(LocationEventListenerImpl.TYPE, LocationEventListenerImpl.EMPLOYEE_LOCATION_CHANGE, location, employee, employee.getLocation());
        }
        if (location == null) {
            employeeLocationManager.removeLocationHistory(employee);
        } else {
            employeeLocationManager.removeLocationHistory(employee, location);
            EdsEmployeeLocation employeeLocation = new EdsEmployeeLocation();
            employeeLocation.setUser(employee);
            employeeLocation.setLocation(location);
            employeeLocationManager.create(employeeLocation);
        }
        employee.setLocation(location);

        if (editProfile.getCustomFields() != null && editProfile.getCustomFields().size() > 0) {
            final StringBuilder changes = new StringBuilder();
            final List<CompanyCustomFieldItem> changedCustomFields = new ArrayList<>();
            for (final CompanyCustomFieldItem cit : editProfile.getCustomFields()) {
                if (cit != null && cit.getColumnCode() != null) {
                    if (employee.getCustomFields() == null) {
                        if ((cit.getFieldStringValue() != null && !"".equals(cit.getFieldStringValue()))
                                || cit.getFieldDateNonConvertedValue() != null || (cit.getAttachments() != null && cit.getAttachments().length > 0)) {
                            changes.append(cit.getColumnCode() + ",");
                        }
                    } else {
                        changes.append(this.getChanges(CustomFieldsUtils.getObjectValue(employee.getCustomFields(), cit.getColumnCode()), cit));
                        changedCustomFields.add(cit);
                    }
                }
            }
            if (!"".contentEquals(changes)) {
                employee.addCustomFieldChanges(changes.toString());
                if (employee.getCustomFields() != null) {
                    final EdsEmployeeCustomFields oldCusFields = employee.getCustomFields();
                    for (final CompanyCustomFieldItem cfItem : changedCustomFields) {
                        if (Constants.DATA_TYPE_TEXT.equals(cfItem.getDataType())) {
                            final String oldString = oldCusFields.getStringValue(cfItem.getColumnCode()) != null ? oldCusFields.getStringValue(cfItem.getColumnCode()) : "";
                            if (!oldString.equals(cfItem.getFieldStringValue())) {
                                employee.addHistoryChange(cfItem.getFieldName(), oldString, cfItem.getFieldStringValue());
                            }
                        } else if (Constants.DATA_TYPE_NUMBER.equals(cfItem.getDataType())) {
                            final Double oldNumber = oldCusFields.getDoubleValue(cfItem.getColumnCode());
                            final String oldNumberString = oldNumber != null ? String.valueOf(oldNumber) : "";
                            if (cfItem != null && cfItem.getFieldStringValue() != null) {
                                final String newNumber = cfItem.getFieldStringValue().isEmpty() ? "" : String.valueOf(Double.valueOf(cfItem.getFieldStringValue()));
                                if (!oldNumberString.equals(newNumber)) {
                                    employee.addHistoryChange(cfItem.getFieldName(), oldNumberString, cfItem.getFieldStringValue());
                                }
                            }
                        } else if (Constants.DATA_TYPE_DATE.equals(cfItem.getDataType())) {
                            final Date oldDate = oldCusFields.getDateValue(cfItem.getColumnCode());
                            final DateNonConvertable cnc = cfItem.getFieldDateNonConvertedValue();
                            final Date newDate = cnc != null ? cnc.getNonConvertedDate() : null;
                            if ((oldDate != null && newDate != null && (oldDate.after(newDate) || oldDate.before(newDate))) ||
                                    (oldDate != null && newDate == null) || (oldDate == null && newDate != null)) {
                                employee.addHistoryChange(cfItem.getFieldName(), oldDate, newDate);
                            }
                        }
                    }
                }
            }
        }
        employee.setCustomFields(this.saveEmployeeCustomFields(employee.getCustomFields(), editProfile.getCustomFields()));

        for (Map.Entry<String, ArrayList<CustomTableRpc>> map : editProfile.getCustomTableItems().entrySet()) {
            List<CustomTableRpc> values = map.getValue();
            if (employee != null && employee.getObjectID() != null) {
                for (CustomTableRpc customTableRpc : values) {
                    List<EdsEmployeeCustomItemTable> oldValuesEmployee = employeeItemTableManager.findByUuid(employee.getObjectID(), customTableRpc.getUuid());

                    if (oldValuesEmployee != null && oldValuesEmployee.size() > 0) {
                        for (EdsEmployeeCustomItemTable itemTable : oldValuesEmployee) {
                            employeeItemTableManager.delete(itemTable);
                        }
                    }
                }
            }

            for (CustomTableRpc rpc : values) {
                EdsEmployeeCustomItemTable customItemTable = new EdsEmployeeCustomItemTable();
                customItemTable.setUuid(map.getKey());
                customItemTable.setName(rpc.getItemName());
                customItemTable.setDescription(rpc.getDescription());
                customItemTable.setCustomFields(saveCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()));
                customItemTable.setEmployee(employee);
                if (saveCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()) != null) {
                    employeeItemTableManager.createOrUpdate(customItemTable);
                }
            }
        }

        boolean admin = false;
        if (editProfile.getRoleId() != null) {
            for (final Integer roleID : editProfile.getRoleId()) {
                if (roleID != null && roleID > 0) {
                    if (roleID == 5) {
                        admin = true;
                    }
                }
            }
        }
        if (!admin) {
            final List<EdsEmployee> otherAdmins = this.employeeManager.getCompanyOtherAdmins(editProfile.getEmployeeId());
            if (otherAdmins.size() == 0) {
                return Errors.LEAST_ONE_ADMIN_ROLE;
            }
        }

        final StringBuffer oldRolesString = new StringBuffer();
        final StringBuffer newRolesString = new StringBuffer();

        if (editProfile.getRoleId() != null && editProfile.getRoleId().length > 0) {
            final ArrayList<Integer> oldRoleIDs = new ArrayList<>(employee.getRoleIds());
            oldRolesString.append(employee.getRolesNameAsString());
            if (editProfile.getEss()) {
                employee.getRoles().clear();
                employee.getRoles().add(ess);
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ESS_AS_PROJECT_MANAGER) && editProfile.getRoleId()[0] != null && editProfile.getRoleId()[0] == EdsRole.PM) {
                    employee.getRoles().add(this.roleManager.get(editProfile.getRoleId()[0]));
                } else {
                    employee.getRoles().add(ess);
                }
            } else if (employee.getRoles().contains(ess)) {
                employee.getRoles().remove(ess);
                if (employee.getRoles() == null || employee.getRoles().size() == 0) {
                    employee.getRoles().add(this.roleManager.getByCode(Constants.MEM_CODE));
                }
            }

            if (!editProfile.getEss() && !editProfile.getNoAccess() && editProfile.getRoleId()[0] != null) {
                employee.getRoles().clear();
                for (final Integer roleID : editProfile.getRoleId()) {
                    if (roleID != null && roleID > 0) {
                        employee.getRoles().add(this.roleManager.get(roleID));
                    }
                }
            }
            boolean roleAdmin = false;
            for (final EdsRole testRole : this.userManager.getUser().getRoles()) {
                if (testRole.equals(Constants.ADMIN)) {
                    roleAdmin = true;
                    break;
                }

            }
            for (final Integer id : oldRoleIDs) {
                if (!this.userManager.getUser().hasEitherRoles(EdsRole.ADMIN) && id.equals(Constants.ADMIN) && !roleAdmin) {
                    employee.getRoles().add(this.roleManager.get(Constants.ADMIN));
                }
            }

            final EdsTrustee userTrustee = this.trusteeManager.getTrustee(employee);
            final List<EdsGroup> builtinGroups = this.groupManager.getCompanyDefaultGroups();
            final Set<EdsGroup> changedGroups = new HashSet<>();
            final Map<String, EdsGroup> groupMap = new HashMap<>();
            final Set<EdsGroup> membershipGroups = employee.getMembershipGroups();
            for (final EdsGroup group : builtinGroups) {
                membershipGroups.remove(group);
                if (group.getMembers().contains(userTrustee)) {
                    changedGroups.add(group);
                    group.getMembers().remove(userTrustee);
                }
                groupMap.put(group.getConstantName(), group);
            }

            for (final EdsRole role : employee.getRoles()) {
                EdsGroup group = null;
                if (role.getObjectID().equals(EdsRole.MEM) || role.getCode().equals(Constants.ESS_USER_CODE)) {
                    group = groupMap.get(EdsGroup.MEMBERS);
                } else if (role.getObjectID().equals(EdsRole.ADMIN)) {
                    group = groupMap.get(EdsGroup.ADMINISTRATORS);
                } else if (role.getObjectID().equals(EdsRole.PM)) {
                    group = groupMap.get(EdsGroup.PROJECT_MANAGERS);
                } else if (role.getObjectID().equals(EdsRole.DR)) {
                    group = groupMap.get(EdsGroup.DIRECTORS);
                } else if (role.getObjectID().equals(EdsRole.TL)) {
                    group = groupMap.get(EdsGroup.DEPARTMENT_LEADERS);
                } else if (role.getObjectID().equals(EdsRole.HR)) {
                    group = groupMap.get(EdsGroup.HRS);
                } else if (role.getObjectID().equals(EdsRole.CLIENT)) {
                    group = groupMap.get(EdsGroup.CLIENTS);
                } else if (role.getObjectID().equals(EdsRole.ACCOUNTANT)) {
                    group = groupMap.get(EdsGroup.ACCOUNTANTS);
                } else if (role.getObjectID().equals(EdsRole.SALESMAN)) {
                    group = groupMap.get(EdsGroup.SALESMEN);
                } else if (role.getObjectID().equals(EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE)) {
                    group = groupMap.get(EdsGroup.CUSTOMER_SERVICE_REPRESENTATIVES);
                } else if (role.getObjectID().equals(EdsRole.CUSTOMER_SERVICE_MANAGER)) {
                    group = groupMap.get(EdsGroup.CUSTOMER_SERVICE_MANAGER);
                } else if (role.getObjectID().equals(EdsRole.SALESPERSON)) {
                    group = groupMap.get(EdsGroup.SALESPERSONS);
                } else if (role.getObjectID().equals(EdsRole.ADMIN_LOCATION)) {
                    group = groupMap.get(EdsGroup.ADMIN_LOCATIONS);
                } else if (role.getObjectID().equals(EdsRole.CALENDAR_EDITOR)) {
                    group = groupMap.get(EdsGroup.CALENDAR_EDITORS);
                } else if (role.getObjectID().equals(EdsRole.CALENDAR_VIEWER)) {
                    group = groupMap.get(EdsGroup.CALENDAR_VIEWERS);
                } else if (role.getObjectID().equals(EdsRole.CHAT_EXPERT)) {
                    group = groupMap.get(EdsGroup.CHAT_EXPERT);
                } else if (role.getObjectID().equals(EdsRole.GUEST)) {
                    group = groupMap.get(EdsGroup.GUEST);
                } else if (role.getObjectID().equals(EdsRole.TIMESHEET_EDITOR)) {
                    group = groupMap.get(EdsGroup.TIMESHEET_EDITORS);
                } else if (role.getObjectID().equals(EdsRole.CUSTOM_MEMBER)) {
                    group = groupMap.get(EdsGroup.CUSTOM_MEMBER);
                } else if (role.getObjectID().equals(EdsRole.PROJECTS_DIRECTOR)) {
                    group = groupMap.get(EdsGroup.PROJECTS_DIRECTOR);
                } else if (role.getObjectID().equals(EdsRole.AUDITOR)) {
                    group = groupMap.get(EdsGroup.AUDITOR);
                }

                if (group != null) {
                    group.getMembers().add(userTrustee);
                    membershipGroups.add(group);
                    changedGroups.add(group);
                }
            }

            for (final EdsGroup group : changedGroups) {
                this.groupManager.update(group);
            }


            final ArrayList<Integer> newRoleIDs = new ArrayList<>(employee.getRoleIds());

            newRolesString.append(employee.getRolesNameAsString());
            if (!ServerUtils.equalsIntegerList(oldRoleIDs, newRoleIDs)) {
                employee.addHistoryChange("System Roles", oldRolesString.toString(), newRolesString.toString());
            }

            final ArrayList<Integer> intersect = (ArrayList<Integer>) ServerUtils.intersect(newRoleIDs, oldRoleIDs);
            this.employeeManager.onRolesChanged(employee, oldRoleIDs, newRoleIDs, intersect);
            this.employeeManager.update(employee);
        } else {
            final ArrayList<Integer> oldRoleIDs = new ArrayList<>(employee.getRoleIds());
            employee.getRoles().clear();
            employee.getRoles().add(this.roleManager.get(EdsRole.MEM));
            final EdsTrustee userTrustee = this.trusteeManager.getTrustee(employee);
            final List<EdsGroup> builtinGroups = this.groupManager.getCompanyDefaultGroups();
            final Set<EdsGroup> changedGroups = new HashSet<>();
            final Map<String, EdsGroup> groupMap = new HashMap<>();
            final Set<EdsGroup> membershipGroups = employee.getMembershipGroups();
            for (final EdsGroup group : builtinGroups) {
                membershipGroups.remove(group);
                if (group.getMembers().contains(userTrustee)) {
                    changedGroups.add(group);
                    group.getMembers().remove(userTrustee);
                }
                groupMap.put(group.getConstantName(), group);
            }
            final EdsGroup group = groupMap.get(EdsGroup.MEMBERS);
            group.getMembers().add(userTrustee);
            membershipGroups.add(group);
            changedGroups.add(group);
            for (final EdsGroup groupf : changedGroups) {
                this.groupManager.update(groupf);
            }
            final ArrayList<Integer> newRoleIDs = new ArrayList<>(employee.getRoleIds());
            newRolesString.append(employee.getRolesNameAsString());
            if (!ServerUtils.equalsIntegerList(oldRoleIDs, newRoleIDs)) {
                employee.addHistoryChange("System Roles", oldRolesString.toString(), newRolesString.toString());
            }
            final ArrayList<Integer> intersect = (ArrayList<Integer>) ServerUtils.intersect(newRoleIDs, oldRoleIDs);
            this.employeeManager.onRolesChanged(employee, oldRoleIDs, newRoleIDs, intersect);
            this.employeeManager.update(employee);
        }
        employeeProfile.setNationality(editProfile.getNationality());
        employeeProfile.setPassportNumber(editProfile.getPassportNumber());
        employeeProfile.setPassportIssueDate(editProfile.getPassportIssueDate() != null ? editProfile.getPassportIssueDate().getNonConvertedDate() : null);
        if (editProfile.getPassportIssueItem() != null && editProfile.getPassportIssueItem().getId() != null) {
            employeeProfile.setCountry(this.countryManager.get(editProfile.getPassportIssueItem().getId()));
        } else {
            employeeProfile.setCountry(null);
        }
        employeeProfile.setPassportExpiryDate(editProfile.getPassportExpiryDate() != null ? editProfile.getPassportExpiryDate().getNonConvertedDate() : null);
        employeeProfile.setMedicalInsuranceExDate(editProfile.getMedicalInsuranceExpireDate() != null ? editProfile.getMedicalInsuranceExpireDate().getNonConvertedDate() : null);
        employeeProfile.setVisaNumber(editProfile.getVisaNumber());
        employeeProfile.setVisaIssueDate(editProfile.getVisaIssueDate() != null ? editProfile.getVisaIssueDate().getNonConvertedDate() : null);
        employeeProfile.setInsuranceNumber(editProfile.getInsuranceNumber());

        ArrayList<Integer> newList = new ArrayList<>();
        String languageEntityType = editProfile.isCandidate() ? EdsSpokenLanguages.TYPE_CANDIDATE : EdsSpokenLanguages.TYPE_EMPLOYEE;

        if (editProfile.getSpokingLanguages() != null) {

            spokenLanguagesManager.firstRemoveEmployeeLanguages(editProfile.getEmployeeId(), languageEntityType);
            for (SpokenLanguageItem languageItem : editProfile.getSpokingLanguages()) {
                if (languageItem.getLanguage() != null && languageItem.getLanguage().getId() != null && languageItem.getLevel() != null && languageItem.getLevel().getId() != null) {
                    EdsSpokenLanguages language = spokenLanguagesManager.getByRelation(editProfile.getEmployeeId(), languageEntityType, languageItem.getLanguage().getId());
                    if (language == null) {
                        language = new EdsSpokenLanguages();
                        language.setEntityType(languageEntityType);
                        language.setEntityId(editProfile.getEmployeeId());
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
            spokenLanguagesManager.removedLanguages(editProfile.getEmployeeId(), languageEntityType, newList);
        }

        if (editProfile.getemployeeDegree() != null) {
            employeeProfile.setEmployeeDegree(referenceManager.get(editProfile.getemployeeDegree()));
        }

        this.profileManager.update(employeeProfile);

        if (Constants.TC_INSTRUCTOR_ADD_FORM.equals(editProfile.getFrom())) {
            //instructor courses
            this.courseManager.deleteInstructorInCourses(employee.getObjectID());
            if (editProfile.getCoursesItems() != null && editProfile.getCoursesItems().size() > 0) {
                for (final SelectItem selectedCourse : editProfile.getCoursesItems()) {
                    final EdsCourse edsCourse = this.courseManager.get(selectedCourse.getId());
                    if (edsCourse != null) {
                        if (!edsCourse.getInstructors().contains(employee)) {
                            edsCourse.getInstructors().add(employee);
                        }
                    }
                }
            }
            final List<EdsCourseSchedule> scheduledCourseList = this.scheduledCourseManager.getInstructorCourseSchedules(employee.getObjectID());
            if (scheduledCourseList != null && scheduledCourseList.size() > 0) {
                try {
                    this.solrManager.addCourseScheduleToIndex(scheduledCourseList.toArray(new EdsCourseSchedule[]{}));
                } catch (final SolrServerException | IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //user bank account details;
        EdsUserBankAccount userBankAccount = this.userBankAccountManager.getUserBankAccountByUser(employee);
        final UserBankAccountData bankAccountData = editProfile.getBankAccountData();
        if (bankAccountData != null) {
            if (userBankAccount == null) {
                userBankAccount = new EdsUserBankAccount();
                userBankAccount.setUser(employee);
            }

            final StringBuilder changes = new StringBuilder();
            if (bankAccountData.getBankName() == null ? userBankAccount.getBankName() != null : !bankAccountData.getBankName().equals(userBankAccount.getBankName())) {
                changes.append(CustomFormConstants.BANK_NAME + ",");
            }
            if (bankAccountData.getBankAddress() == null ? userBankAccount.getBankAddress() != null : !bankAccountData.getBankAddress().equals(userBankAccount.getBankAddress())) {
                changes.append(CustomFormConstants.BANK_ADDRESS + ",");
            }
            if (bankAccountData.getAccountNumber() == null ? userBankAccount.getAccountNumber() != null : !bankAccountData.getAccountNumber().equals(userBankAccount.getAccountNumber())) {
                changes.append(CustomFormConstants.ACCOUNT_NUMBER + ",");
            }
            if (bankAccountData.getAccountName() == null ? userBankAccount.getAccountName() != null : !bankAccountData.getAccountName().equals(userBankAccount.getAccountName())) {
                changes.append(CustomFormConstants.ACCOUNT_NAME + ",");
            }
            if (bankAccountData.getSwiftCode() == null ? userBankAccount.getSwiftCode() != null : !bankAccountData.getSwiftCode().equals(userBankAccount.getSwiftCode())) {
                changes.append(CustomFormConstants.SWIFT_CODE + ",");
            }
            if (bankAccountData.getSortCode() == null ? userBankAccount.getSortCode() != null : !bankAccountData.getSortCode().equals(userBankAccount.getSortCode())) {
                changes.append(CustomFormConstants.SORT_CODE + ",");
            }
            if (bankAccountData.getIbanCode() == null ? userBankAccount.getIbanCode() != null : !bankAccountData.getIbanCode().equals(userBankAccount.getIbanCode())) {
                changes.append(CustomFormConstants.IBAN_CODE + ",");
            }
            if (bankAccountData.getAgentID() == null ? userBankAccount.getAgentID() != null : !bankAccountData.getAgentID().equals(userBankAccount.getAgentID())) {
                changes.append(CustomFormConstants.AGENT_ID + ",");
            }
            if (!"".contentEquals(changes)) {
                employee.addCustomFieldChanges(changes.toString());
            }

            userBankAccount.setBankName(bankAccountData.getBankName());
            userBankAccount.setBankAddress(bankAccountData.getBankAddress());
            userBankAccount.setAccountNumber(bankAccountData.getAccountNumber());
            userBankAccount.setAccountName(bankAccountData.getAccountName());
            userBankAccount.setSwiftCode(bankAccountData.getSwiftCode());
            userBankAccount.setSortCode(bankAccountData.getSortCode());
            userBankAccount.setIbanCode(bankAccountData.getIbanCode());
            userBankAccount.setAgentID(bankAccountData.getAgentID());

            if (userBankAccount.getObjectID() != null) {
                this.userBankAccountManager.update(userBankAccount);
            } else {
                this.userBankAccountManager.create(userBankAccount);
            }
        } else {
            if (userBankAccount != null) {
                this.userBankAccountManager.delete(userBankAccount);
            }
        }

        //user's password is not getting changed
        final EdsBusinessEvent event = this.baseEventPostProcessor.registerEvent(UserAuthEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employee, employee);

        event.setCustomStringField(employee.getPassword());
        this.employeeServiceLocal.createAssignUsersToGroups(employee);

        try {
            start = System.currentTimeMillis();
            employeeSolrComponent.index(employee);
            HrmsServiceImpl.log.info("solrManager.addEmployeeToIndex" + (System.currentTimeMillis() - start));
        } catch (final SolrServerException | InterruptedException e) {
            HrmsServiceImpl.log.error("SAVE EMPLOYEE ERROR:" + e.getMessage(), e);
        } catch (final IOException e) {
            HrmsServiceImpl.log.error("SAVE EMPLOYEE ERROR2:" + e.getMessage(), e);
        }
        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employee, this.employeeManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_EMPLOYEE);
        return employee.getObjectID();
    }

    private int isEmailAvailable(String userName, Integer companyID) {
        try {
            if (userManager.searchUserByUserName(userName.toLowerCase(), companyID) != null) {
                return EMPLOYEE_WITH_THIS_EMAIL_ALREADY_EXISTS;
            }
        } catch (Exception ignored) {
        }
        return EMPLOYEE_WITH_THIS_EMAIL_DOES_NOT_EXIST;
    }


    private EdsEmployeeExperienceItemTableCF saveExperienceItemCustomFields(EdsEmployeeExperienceItemTableCF edsItemCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsItemCustomFields == null) {
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
                edsItemCustomFields = new EdsEmployeeExperienceItemTableCF();
                employeeExperienceItemTableCFManager.createOrUpdate(edsItemCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsItemCustomFields, customFieldItems);
            return edsItemCustomFields;
        }
        return null;
    }

    public void createLabourPeriodToEmployee(EdsEmployee employee, Date startDate) {
        if (startDate != null) {
            EdsUser modifiedBy = userManager.getUser();
            Calendar calendar = Calendar.getInstance();
            int lastyear = calendar.get(Calendar.YEAR) + 5;
            calendar.setTime(startDate);
            calendar.set(Calendar.HOUR, 0);
            int firstYear = calendar.get(Calendar.YEAR);
            EdsLeaveReason leaveReason = leaveReasonManager.findByCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
            for (int i = firstYear; i <= lastyear; i++) {
                EdsLabourPeriod elb = new EdsLabourPeriod();
                elb.setEmployee(employee);
                elb.setStartDate(calendar.getTime());
                calendar.add(Calendar.YEAR, 1);
                calendar.add(Calendar.DATE, -1);
                elb.setEndDate(calendar.getTime());
                elb.setAllowance(leaveReason != null ? leaveReason.getLeaveDays() : 24d);
                elb.setCreatedDate(new Date());
                elb.setModifiedBy(modifiedBy);
                labourPeriodManager.create(elb);
                calendar.add(Calendar.DATE, 1);
                createlaborPeriodHistory(elb.getObjectID(), new HistoryListItem("created"));
            }
        }
    }

    public boolean equalsListReminders(final List<CalendarEventReminder> list1, final List<CalendarEventReminder> list2) {
        if (list1 == null || list2 == null)
            return true;
        if (list1.size() != list2.size())
            return false;

        final Set<CalendarEventReminder> set1 = new TreeSet<>(list1);
        final Set<CalendarEventReminder> set2 = new TreeSet<>(list2);
        return set1.equals(set2);
    }

    private String remindersAsString(final ArrayList<CalendarEventReminder> reminders, final Date expireDate, final boolean isNew) {
        final StringBuffer reminderTime = new StringBuffer();
        for (final CalendarEventReminder reminder : reminders) {
            if (isNew) {
                final Date recStartDate = DateUtil.addMinutes(expireDate, (-1) * reminder.getReminderTimes());
                if (recStartDate.after(new Date())) {
                    if (reminderTime.length() != 0) {
                        reminderTime.append(", ");
                    }
                    reminderTime.append(this.remindersAsString(reminder));
                }
            } else {
                if (reminderTime.length() != 0) {
                    reminderTime.append(", ");
                }
                reminderTime.append(this.remindersAsString(reminder));
            }
        }
        return reminderTime.toString();
    }

    private String remindersAsString(final CalendarEventReminder reminder) {
        String reminderTime = "";
        if (reminder.getReminderTimes().intValue() == 60 * 24) {
            reminderTime = "1 day";
        } else if (reminder.getReminderTimes().intValue() == 60 * 24 * 2) {
            reminderTime = "2 days";
        } else if (reminder.getReminderTimes().intValue() == 60 * 24 * 3) {
            reminderTime = "3 days";
        } else if (reminder.getReminderTimes().intValue() == 60 * 24 * 5) {
            reminderTime = "5 days";
        } else if (reminder.getReminderTimes().intValue() == 60 * 24 * 7) {
            reminderTime = "1 week";
        } else if (reminder.getReminderTimes().intValue() == 60 * 24 * 7 * 2) {
            reminderTime = "2 weeks";
        } else if (reminder.getReminderTimes().intValue() == 60 * 24 * 30) {
            reminderTime = "1 Month";
        } else if (reminder.getReminderTimes().intValue() == 60 * 24 * 45) {
            reminderTime = "45 days";
        } else if (reminder.getReminderTimes().intValue() == 60 * 24 * 60) {
            reminderTime = "2 Months";
        } else if (reminder.getReminderTimes().intValue() == 60 * 24 * 90) {
            reminderTime = "3 Months";
        }
        return reminderTime;
    }

    @Override
    public void updateHolidayDetails(final EdsEmployee employee, final EdsLocation oldLocation, final EdsLocation newLocation) {
        final Calendar todaysDate = Calendar.getInstance();
        todaysDate.setTime(employee.getUserDate(new Date()));
        todaysDate.set(Calendar.HOUR_OF_DAY, 0);
        todaysDate.set(Calendar.MINUTE, 0);
        todaysDate.set(Calendar.SECOND, 0);
        todaysDate.set(Calendar.MILLISECOND, 0);

        if (oldLocation != null) {
            final List<EdsHoliday> oldHolidays = this.holidayManager.getLocationHolidays(todaysDate.getTime(), oldLocation);

            for (final EdsHoliday holiday : oldHolidays) {
                if (holiday.getStartDate().compareTo(employee.getUserDate(new Date())) >= 0) {
                    this.attendanceRawDataManager.updateHolidays(holiday, employee.getObjectID(), false, false);
                }
            }
        }
        if (newLocation != null) {
            final List<EdsHoliday> newHolidays = this.holidayManager.getLocationHolidays(todaysDate.getTime(), newLocation);
            newHolidays.forEach(h -> {
                if (h.getStartDate().compareTo(employee.getUserDate(new Date())) >= 0) {
                    this.attendanceRawDataManager.updateHolidays(h, employee.getObjectID(), h.isDayOff(), h.isTakeAnnual());
                }
            });
        }
    }

    public void sendEmployeeVisaExpirationDateEmailNotification(final Integer recurrenceID, final Integer employeeProfileID) {
        try {
            final EdsEmployeeProfile employeeProfile = this.profileManager.get(employeeProfileID);
            if (employeeProfile != null) {
                final EdsEmployee employeeProfileEmployee = employeeProfile.getEmployee();
                if (employeeProfileEmployee != null && !employeeProfileEmployee.getDeleted()) {
                    if (ServerUtils.hasPermission(PermissionConstants.HRMS_VISA_EXPIRATION_DATE_REMINDER)) {
                        this.messageManager.sendEmployeeVisaExpirationDateReminder(recurrenceID, employeeProfileEmployee, employeeProfileEmployee, employeeProfileID);
                    }

                    final List<String> roleCodes = this.rolePermissionManager.getRolesByPermissionCode(PermissionConstants.HRMS_VISA_EXPIRATION_DATE_REMINDER);
                    if (roleCodes.isEmpty()) {
                        roleCodes.add(EdsRole.ADMIN_CODE);
                    }
                    Integer maxRoleID;
                    final List<EdsEmployee> receivers = this.employeeManager.getEmployeesForAccounting(null, roleCodes.toArray(new String[]{}));
                    for (final EdsEmployee receiver : receivers) {
                        maxRoleID = ServerUtils.getMaxRoleID(receiver.getRolesAsIntegersString());
                        if (!receiver.getObjectID().equals(employeeProfileEmployee.getObjectID()) && !Constants.MEM.equals(maxRoleID)) {
                            this.messageManager.sendEmployeeVisaExpirationDateReminder(recurrenceID, receiver, employeeProfileEmployee, employeeProfileID);
                        }
                    }
                }
            }
        } catch (final EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendHrReminders(final Integer typeID, final Integer userId, final Integer companyId) {
        EdsUser user = this.userManager.getUserByUserIdAndCompanyId(userId, companyId);
        if (user == null) {
            final EdsRole role = this.roleManager.getByCode(Constants.ADMIN_CODE);
            user = this.employeeManager.getUserByRole(role.getObjectID()).get(0);
        }
        if (typeID != null && "2".equals(typeID.toString())) {
            this.sentEmployeeDocumentReminder(user, companyId);
            return;
        }
        final List<EdsHrReminderSettings> reminders = this.hrReminderSettingsManager.getReminders(companyId);
        for (final EdsHrReminderSettings reminderSetting : reminders) {
            final ArrayList<Date> reminderTimeActions = this.getReminderTimeActionsDates(false, reminderSetting.getObjectID(), companyId);
            final Map<Boolean, List<EdsUser>> recipentsMap = this.getHrReminderRecipents(false, reminderSetting.getObjectID(), companyId);
            final String columncode = switch (reminderSetting.getFieldcode()) {
                case "passportIssueDate" -> "emp.passportIssueDate";
                case "passportExpireDate" -> "emp.passportExpiryDate";
                case "visaExpirationDate" -> "emp.visaExpirationDate";
                case "visaIssueDate" -> "emp.visaIssueDate";
                case "insuranceExpiryDate" -> "emp.medicalInsuranceExDate";
                default -> "itemcf." + reminderSetting.getFieldcode();
            };
            for (final Boolean key : recipentsMap.keySet()) {
                this.reminderDateActionsReminder(columncode, reminderSetting, reminderTimeActions, key, recipentsMap.get(key), user, companyId);
            }
        }
    }

    private void sentEmployeeDocumentReminder(final EdsUser user, final Integer companyId) {
        final List<EdsEmployeeDocumentReminderSettings> reminders = this.employeeDocumentReminderSettingsManager.getReminders(companyId);
        if (!reminders.isEmpty()) {
            for (final EdsEmployeeDocumentReminderSettings reminderSetting : reminders) {
                final ArrayList<Date> reminderTimeActions = this.getReminderTimeActionsDates(true, reminderSetting.getObjectID(), companyId);
                final Map<Boolean, List<EdsUser>> recipentsMap = this.getHrReminderRecipents(true, reminderSetting.getObjectID(), companyId);
                for (final Boolean key : recipentsMap.keySet()) {
                    this.documentReminderDateActionsReminder(reminderSetting, reminderTimeActions, recipentsMap.get(key), user, companyId);
                }
            }
        }
    }

    private void documentReminderDateActionsReminder(final EdsEmployeeDocumentReminderSettings reminderSetting, final ArrayList<Date> reminderTimeActions, final List<EdsUser> recipents, final EdsUser user, final Integer companyId) {
        for (final Date reminderdate : reminderTimeActions) {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            final String formatDate = dateFormat.format(reminderdate);
            final String endformatDate = endDateFormat.format(reminderdate);
            this.reminderForDocumentReminder(reminderSetting, recipents, user, companyId, formatDate, endformatDate);
        }

    }

    private void reminderForDocumentReminder(final EdsEmployeeDocumentReminderSettings reminderSetting, final List<EdsUser> recipents, final EdsUser user, final Integer companyId, final String formatDate, final String endformatDate) {
        this.expiredFileReminder(reminderSetting, recipents, user, companyId, formatDate, endformatDate);
    }

    private void expiredFileReminder(final EdsEmployeeDocumentReminderSettings reminderSetting, final List<EdsUser> recipents, final EdsUser user, final Integer companyId, final String formatDate, final String endformatDate) {
        final EdsFileHeader expiredFile = this.employeeDocumentReminderSettingsManager.getReminderDateDocument("expireDate", reminderSetting.getItemId(), formatDate, endformatDate);
        if (expiredFile != null) {
            final EdsUser employee = this.userManager.get(expiredFile.getEntityId());
            final List<EdsUser> employees = new ArrayList<>();
            if (!(employee == null || employee.getAccountStatus() == null || employee.getAccountStatus().getCode() == null || employee.getAccountStatus().getCode().equals(Constants.EMPLOYEE_STATUS_RESIGNED))) {
                employees.add(employee);
            }
            this.messageManager.setHrReminderNotification(reminderSetting.getTemplate(), reminderSetting.getFieldValue(), formatDate, user, employees, recipents, companyId);
        }
    }

    private void reminderForOnboardingCustomFields(final String columnCode, final EdsHrReminderSettings reminderSetting, final Boolean key, final List<EdsUser> recipents, final EdsUser user, final Integer companyId, final String formatDate, final String endformatDate) {
        final List<EdsOnboardingStep> onboardings = this.employeeManager.getOnboardingstepForReminder(columnCode, companyId, formatDate, endformatDate);
        if (onboardings != null && onboardings.size() > 0) {
            this.sentHrReminderEmailForOnboarding(reminderSetting, recipents, formatDate, user, companyId, onboardings);
        }
        this.runWorkflowForHrReminder(reminderSetting, companyId, null, onboardings);
    }

    private void sentHrReminderEmailForOnboarding(final EdsHrReminderSettings reminderSetting, final List<EdsUser> recipents, final String reminderdate, final EdsUser user, final Integer companyId, final List<EdsOnboardingStep> onboardings) {
        if (reminderSetting.getTemplate() != null && (recipents != null && recipents.size() > 0)) {
            final List<EdsUser> employee = this.getEmployeeFromOnboardintStep(onboardings);
            this.messageManager.setHrReminderNotification(reminderSetting.getTemplate(), reminderSetting.getFieldValue(), reminderdate, user, employee, recipents, companyId);
        }
    }

    private List<EdsUser> getEmployeeFromOnboardintStep(final List<EdsOnboardingStep> onboardings) {
        final List<EdsUser> users = new ArrayList<>();
        for (final EdsOnboardingStep onb : onboardings) {
            final ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCategoryID(onb.getObjectID());
            final List<EdsStepEmployee> stepEmployees = this.stepEmployeeManager.getStepList(fp);
            for (final EdsStepEmployee stepEmployee : stepEmployees) {
                if (!stepEmployee.getEmployee().getAccountStatus().getCode().equals(Constants.EMPLOYEE_STATUS_RESIGNED)) {
                    users.add(stepEmployee.getEmployee());
                }
            }
        }
        return users;
    }

    private void reminderDateActionsReminder(final String columnCode, final EdsHrReminderSettings reminderSetting, final ArrayList<Date> reminderTimeActions, final Boolean key, final List<EdsUser> recipents, final EdsUser user, final Integer companyId) {
        for (final Date reminderdate : reminderTimeActions) {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            final String formatDate = dateFormat.format(reminderdate);
            final String endformatDate = endDateFormat.format(reminderdate);
            if (reminderSetting.getEntityType().equals(0)) {
                this.reminderForEmployeeReminder(columnCode, reminderSetting, key, recipents, user, companyId, formatDate, endformatDate);
            } else {
                this.reminderForOnboardingCustomFields(columnCode, reminderSetting, key, recipents, user, companyId, formatDate, endformatDate);
            }
        }
    }

    private void reminderForEmployeeReminder(final String columnCode, final EdsHrReminderSettings reminderSetting, final Boolean key, List<EdsUser> recipents, final EdsUser user, final Integer companyId, final String formatDate, final String endformatDate) {
        final List<EdsUser> employees = this.employeeManager.getEmployeesForReminder(columnCode, companyId, formatDate, endformatDate);
        if (employees == null || employees.size() == 0) {
            HrmsServiceImpl.log.info("----------------Employee not match this period : " + formatDate + "------------------------");
            return;
        }
        if (reminderSetting.getTemplate() == null) {
            HrmsServiceImpl.log.info("----------------Email template not selected in Reminder Settings!!!------------------------");
            return;
        }
        if (recipents == null) {
            recipents = new ArrayList<>();
        }
        recipents.addAll(employees);
        this.messageManager.setHrReminderNotification(reminderSetting.getTemplate(), reminderSetting.getFieldValue(), formatDate, user, employees, recipents, companyId);
        HrmsServiceImpl.log.info("----------------Send Email Successfully!!!------------------------");
        this.runWorkflowForHrReminder(reminderSetting, companyId, employees, null);
        if (key) {
            final Map<Integer, ArrayList<EdsUser>> teamLEaderMap = this.getTeamLeaderWithEmployees(employees);
            this.messageManager.setHrReminderNotificationTeamLeaderSpesific(reminderSetting.getTemplate(), reminderSetting.getFieldValue(), formatDate, user, teamLEaderMap, companyId);
        }
    }

    private Map<Integer, ArrayList<EdsUser>> getTeamLeaderWithEmployees(final List<EdsUser> employees) {
        final Map<Integer, ArrayList<EdsUser>> teamLEaderMap = new HashMap<>();
        for (final EdsUser employee : employees) {
            final SelectItem employeeWithTeamLeader = this.employeeManager.getEmployeesWithTeamLeader(employee.getObjectID());
            if (employeeWithTeamLeader.getCategory() == null) {
                HrmsServiceImpl.log.info("--------------------------- Team leader dooesn't exist this team------------------------------");
                continue;
            }
            final Integer leaderid = Integer.valueOf(employeeWithTeamLeader.getCategory());
            ArrayList<EdsUser> leaderEmployees = teamLEaderMap.get(leaderid);
            if (leaderEmployees == null) {
                leaderEmployees = new ArrayList<>();
            }
            leaderEmployees.add(employee);
            teamLEaderMap.put(leaderid, leaderEmployees);

        }
        return teamLEaderMap;
    }

    private void runWorkflowForHrReminder(final EdsHrReminderSettings reminderSetting, final Integer companyId, final List<EdsUser> employees, final List<EdsOnboardingStep> onboardings) {
        final List<EdsWorkflowRule> workflowRules = this.getHrReminderWorkflowRules(reminderSetting, companyId);
        if (workflowRules != null && workflowRules.size() > 0) {
            for (final EdsWorkflowRule workflowRule : workflowRules) {
                if (reminderSetting.getEntityType().equals(0)) {
                    this.runWorkflowForEmployee(employees, workflowRule);
                } else {
                    this.runWorkflowForOnboarding(onboardings, workflowRule);
                }
            }
        }
    }

    private void runWorkflowForOnboarding(final List<EdsOnboardingStep> onboardings, final EdsWorkflowRule workflowRule) {
        for (final EdsOnboardingStep usRm : onboardings) {
            final ListingFilterParameter fp = new ListingFilterParameter();
            fp.setCategoryID(usRm.getObjectID());
            final List<EdsStepEmployee> stepEmployees = this.stepEmployeeManager.getStepList(fp);
            for (final EdsStepEmployee stepEmployee : stepEmployees) {
                this.allInOneServiceLocal.runActionTrack(workflowRule, RelationItem.TYPE_EMPLOYEE, stepEmployee);
            }
        }
    }

    private void runWorkflowForEmployee(final List<EdsUser> employees, final EdsWorkflowRule workflowRule) {
        for (final EdsUser usRm : employees) {
            final EdsEmployee employee = this.employeeManager.get(usRm.getObjectID());
            if (employee != null) {
                this.allInOneServiceLocal.runActionTrack(workflowRule, RelationItem.TYPE_EMPLOYEE, employee);
            }
        }
    }

    private List<EdsWorkflowRule> getHrReminderWorkflowRules(final EdsHrReminderSettings reminderSetting, final Integer companyId) {
        final List<EdsWorkflowRule> reEmployees = new ArrayList<>();
        final List<Integer> workflowRoles = this.hrReminderSettingsManager.getReminderWorkflowRoles(reminderSetting.getObjectID(), companyId);
        if (workflowRoles != null && workflowRoles.size() > 0) {
            for (final Integer role : workflowRoles) {
                reEmployees.add(this.workflowRuleManager.get(role));
            }
        }
        return reEmployees;
    }

    private Map<Boolean, List<EdsUser>> getHrReminderRecipents(final boolean isEmployeeDocReminder, final Integer reminderSettingId, final Integer companyId) {
        final Map<Boolean, List<EdsUser>> empMap = new HashMap<>();
        boolean key = false;
        final List<EdsUser> reEmployees = new ArrayList<>();
        final List<Integer> reminderRoles;
        final EdsRole teamLeaderSpesic = this.roleManager.getByCode(Constants.DLOFPR);
        if (isEmployeeDocReminder) {
            reminderRoles = this.employeeDocumentReminderSettingsManager.getReminderRecipentRoles(reminderSettingId, companyId);
        } else {
            reminderRoles = this.hrReminderSettingsManager.getReminderRecipentRoles(reminderSettingId, companyId);
        }
        if (reminderRoles != null && reminderRoles.size() > 0) {
            for (final Integer role : reminderRoles) {
                if (!role.equals(Constants.MEM)) {
                    if (teamLeaderSpesic != null && role.equals(teamLeaderSpesic.getObjectID())) {
                        key = true;
                    } else {
                        reEmployees.addAll(this.employeeManager.getUserByRole(role));
                    }
                }
            }
        }
        empMap.put(key, reEmployees);
        return empMap;
    }

    private ArrayList<Date> getReminderTimeActionsDates(final boolean isEmployeeDocReminder, final Integer reminderSettingId, final Integer companyId) {
        final List<EdsHrReminderTimeAction> reminderTimeActions = this.hrReminderSettingsManager.getReminderTimeActions(isEmployeeDocReminder, reminderSettingId, companyId);
        final ArrayList<Date> dates = new ArrayList<>();
        for (final EdsHrReminderTimeAction reminderTimeAction : reminderTimeActions) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            final int numberItem = reminderTimeAction.getActiontype().equals("AFTER") ? (-1) * reminderTimeAction.getActionNumber() : reminderTimeAction.getActionNumber();
            switch (reminderTimeAction.getActionPeriod()) {
                case "" + (60 * 24) -> calendar.add(Calendar.DATE, numberItem);
                case "" + (60 * 24 * 7) -> calendar.add(Calendar.DATE, 7 * numberItem);
                default -> calendar.add(Calendar.MONTH, numberItem);
            }
            dates.add(calendar.getTime());
        }
        return dates;
    }

    private EdsDependentCustomFields saveDependentCustomFields(EdsDependentCustomFields edsDependentCustomFields, final List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsDependentCustomFields == null) {
                boolean isEmpty = true;
                for (final CompanyCustomFieldItem fieldItem : customFieldItems) {
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
                edsDependentCustomFields = new EdsDependentCustomFields();
                this.dependentCFManager.create(edsDependentCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsDependentCustomFields, customFieldItems);
            return edsDependentCustomFields;
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EdsEmployeeCustomFields saveEmployeeCustomFields(EdsEmployeeCustomFields edsEmployeeCustomField, final List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsEmployeeCustomField == null) {
                boolean isEmpty = true;
                for (final CompanyCustomFieldItem fieldItem : customFieldItems) {
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
                edsEmployeeCustomField = new EdsEmployeeCustomFields();
                this.employeeCFManager.create(edsEmployeeCustomField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsEmployeeCustomField, customFieldItems);
            return edsEmployeeCustomField;
        }
        return null;
    }

    @Override
    public void saveEmployeeDocumentReminder(final FileResource item) {
        final Integer objId = 2;
        this.employeeDocumentReminderSettingsManager.deleteHrReminders(item.getObjectId());
        if (item.getActionTimes() == null)
            return;
        final List<EdsEmailTemplate> emailTemplates = this.emailTemplateManager.getEmailTemplatesByCategory(Constants.HR_REMINDERS_CATEGORY);
        for (final EdsEmailTemplate emailTemplate : emailTemplates) {
            final EdsEmployeeDocumentReminderSettings employeeDocumentReminderSettings = new EdsEmployeeDocumentReminderSettings();
            if (item.getTypeId() != null) {
                employeeDocumentReminderSettings.setEntityType(item.getTypeId());
                employeeDocumentReminderSettings.setFieldValue(item.getFileName() + " (Type - " + this.referenceManager.get(item.getTypeId()).getName() + ")");
                employeeDocumentReminderSettings.setFieldcode(this.referenceManager.get(item.getTypeId()).getCode());
            } else {
                employeeDocumentReminderSettings.setEntityType(0);
                employeeDocumentReminderSettings.setFieldValue(item.getFileName());
                employeeDocumentReminderSettings.setFieldcode("FILE_NAME");
            }
            employeeDocumentReminderSettings.setTemplate(emailTemplate);
            employeeDocumentReminderSettings.setItemId(item.getObjectId());
            EdsHrReminderTimeAction edsTimeAction;
            for (final ActionTimesTO actiontime : item.getActionTimes()) {
                edsTimeAction = new EdsHrReminderTimeAction();
                edsTimeAction.setActiontype(actiontime.getActiontype());
                if (actiontime.getActionNumber() != null && !"".equals(actiontime.getActionNumber())) {
                    edsTimeAction.setActionNumber(Integer.valueOf(actiontime.getActionNumber()));
                }
                edsTimeAction.setActionPeriod(actiontime.getActionPeriod());
                employeeDocumentReminderSettings.getTimeActions().add(edsTimeAction);
            }

            final Set<EdsRole> roles = employeeDocumentReminderSettings.getRoles();
            final EdsRole hr_role = this.roleManager.getByCode(EdsRole.HR_CODE);
            roles.add(hr_role);
            employeeDocumentReminderSettings.setRoles(roles);

            this.employeeDocumentReminderSettingsManager.createOrUpdate(employeeDocumentReminderSettings);
        }
        final EdsRecurrence existingRecurrence = this.recurrenceService.getRecurrenceByJobId(objId, SchedulerConstant.DAILY_HR_REMINDERP_ROCEDURE_JOB);
        if (existingRecurrence == null) {
            this.createDailyHrReminderProcedureJob(objId);
        }
    }

    private Integer saveContact(final Integer contactID, final ContactListItem contactListItem) {
        contactListItem.setObjectId(contactID);
        return this.contactServiceLocal.saveContact(contactListItem, null, null, true, true);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<GoalItem> getOwnGoalList(final ListingFilterParameter fp) {
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityType(KpiEntityType.EPMLOYEE_GOAL);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Get employee own goals");
        if (fp.getEmployeeId() == null) {
            fp.setEmployeeId(this.goalManager.getUser().getObjectID());
        }

        final EdsReference ref_per = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PERSONAL_GOAL);
        final List<EdsGoal> goalList = this.goalManager.getOwnGoalList(fp, ref_per);
        final int totalCount = goalList.size();
        final ArrayList<GoalItem> results = new ArrayList<>();

        for (final EdsGoal goal : goalList) {
            final GoalItem item = new GoalItem();
            item.setObjectId(goal.getObjectID());
            if (goal.getNumberData() != null) {
                NumberData numberData = new NumberData();
                numberData.setNumberString(goal.getNumberData());
                numberData.setFirstNumberString(goal.getNumberData());
                numberData.setNumberFormat("_");
                item.setGoalNumber(numberData);
            }
            if (goal.getProjectGoal() != null) {
                item.setProjectGoalTitle(goal.getProjectGoal().getTitle());
            }
            item.setActionSteps(goal.getActionSteps());
            item.setDescription(goal.getDescription());
            item.setFromDate(new DateNonConvertable(goal.getFromDate()));
            item.setToDate(new DateNonConvertable(goal.getToDate()));
            if (goal.getGoalCategory() != null) {
                item.setGoalCategory(this.referenceWfmMessageSource.localize(goal.getGoalCategory().getCode(), goal.getGoalCategory().getName()));
            }
            item.setProgress(goal.getProgress());
            if (goal.getResolver() != null) {
                item.setResolver(goal.getResolver().getFullName());
            }
            if (goal.getStatus() != null) {
                item.setStatus(this.referenceWfmMessageSource.localize(goal.getStatus().getCode(), goal.getStatus().getName()));
            }
            if (goal.getBusinessGoal() != null) {
                item.setCompanyGoal(goal.getBusinessGoal().getTitle());
            }
            item.setTitle(goal.getTitle());
            item.setWeight(goal.getWeight());
            results.add(item);

        }
        return new ListResult<>(results, totalCount);
    }

    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getRoleAsHashMap(final List<EdsRole> rolesOriginal) {
        final ArrayList<KpiTreeInfo> roles = new ArrayList<>();
        final String categoryName = "Roles";
        final Integer categoryId = 1;
        final KpiTreeInfo category = new KpiTreeInfo(categoryId, categoryName);
        final List<EdsRole> allRoles = this.getUserRolesByPattern(this.roleManager.list());

        for (final EdsRole role : allRoles) {
            if (!role.getObjectID().equals(EdsRole.CLIENT) && !role.getObjectID().equals(EdsRole.TL)) {
                final KpiTreeInfo kpiTreeInfo = new KpiTreeInfo(role.getObjectID(), role.getName());

                if (rolesOriginal != null && rolesOriginal.contains(role)) {
                    kpiTreeInfo.setSelected(true);
                }

                kpiTreeInfo.setDepartmentName(categoryName);
                kpiTreeInfo.setDepartmentId(categoryId);
                roles.add(kpiTreeInfo);
            }
        }
        final LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> roleItems = new LinkedHashMap<>();
        roleItems.put(category, roles);
        return roleItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OnboardingItem getOnboardingStep(final Integer stepId) {
        EdsOnboardingStep onboardingStep = new EdsOnboardingStep();
        OnboardingItem onboardingItem = new OnboardingItem();
        if (stepId != null) {
            onboardingStep = this.onboardingStepManager.get(stepId);
            onboardingItem = onboardingStep.getRPC();
            final ArrayList<ReferenceItem> statusItems = new ArrayList<>();
            if (onboardingStep.getStatus() != null) {
                final List<EdsReference> references = this.referenceManager.listReferences(onboardingStep.getStatus().getCode());
                for (final EdsReference reference : references) {
                    statusItems.add(reference.getRPC());
                }
            }
            onboardingItem.setStatusItems(statusItems.size() > 0 ? statusItems : null);
            onboardingItem.setRoles(this.getRoleAsHashMap(onboardingStep.getRoles()));

            final EdsOnboardingStepCustomFields edsOnboardingStepCustomFields = onboardingStep.getOnboardingStepCustomFields();
            final ArrayList<CompanyCustomFieldItem> customFieldsItems = this.commonService.getCompanyCustomFields(ViewName.OnboardingStep);
            onboardingItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsOnboardingStepCustomFields, customFieldsItems));
        } else {
            onboardingItem.setRoles(this.getRoleAsHashMap(null));
        }

        final ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(500);
        final List<EdsOnboardingPeriod> onboardingPeriods = this.onboardingPeriodManager.getOnboardingPeriodList(fp);
        final SelectItem[] periodSelectItems = new SelectItem[onboardingPeriods.size()];
        int i = 0;
        for (final EdsOnboardingPeriod period : onboardingPeriods) {
            periodSelectItems[i] = new SelectItem(period.getObjectID(), period.getName());
            i++;
        }
        onboardingItem.setPeriods(periodSelectItems);

        final List<EdsOnboardingStep> allSteps = this.onboardingStepManager.getOnboardingStepList(null);
        final List<EdsOnboardingStep> parentSteps = this.onboardingStepManager.getParentSteps(onboardingStep.getObjectID());
        List<EdsOnboardingStep> newParentSteps = null;
        if (stepId != null) {
            allSteps.remove(onboardingStep);
            parentSteps.remove(onboardingStep);
            newParentSteps = new ArrayList<>(parentSteps);
            this.removeChilds(onboardingStep, allSteps, newParentSteps);
        }
        final ArrayList<SelectItem> parentStepItems = new ArrayList<>();
        for (final EdsOnboardingStep step : (newParentSteps != null ? newParentSteps : parentSteps)) {
            parentStepItems.add(new SelectItem(step.getObjectID(), step.getName()));
        }
        onboardingItem.setParentSteps(parentStepItems.toArray(new SelectItem[]{}));

        return onboardingItem;
    }

    private void removeChilds(final EdsOnboardingStep step, final List<EdsOnboardingStep> parentSteps, final List<EdsOnboardingStep> newParentSteps) {
        for (final EdsOnboardingStep st : parentSteps) {
            if (step.equals(st.getParent())) {
                newParentSteps.remove(st);
                this.removeChilds(st, parentSteps, newParentSteps);
                break;
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OnboardingItem getOnboardingPeriod(final Integer positionId) {
        final EdsOnboardingPeriod onboardingPeriod = this.onboardingPeriodManager.get(positionId);
        return onboardingPeriod.getRPC();
    }

    @Override
    public GroupPlacementItem getCandidateItems(Integer candidateId) {
        EdsCrmContact crmContact = crmContactManager.get(candidateId);
        EdsDepartment candidateDepartment = crmContact.getCandidateDepartment();
        EdsPosition candidatePosition = crmContact.getCandidatePosition();
        GroupPlacementItem item = new GroupPlacementItem();
        item.setDepartment(candidateDepartment != null ? new SelectItem(candidateDepartment.getObjectID(), candidateDepartment.getName()) : null);
        item.setPosition(candidatePosition != null ? new SelectItem(candidatePosition.getObjectID(), candidatePosition.getName()) : null);
        return item;
    }

    public void saveOnboardingPeriod(final OnboardingItem item) {
        EdsOnboardingPeriod onboardingPeriod = new EdsOnboardingPeriod();
        if (item.getPeriodId() != null) {
            onboardingPeriod = this.onboardingPeriodManager.get(item.getPeriodId());
        }
        onboardingPeriod.setName(item.getPeriodName());
        onboardingPeriod.setDescription(item.getPeriodDescription());
        onboardingPeriod.setActive(item.getPeriodActive());
        onboardingPeriod.setBeforeHireDate(item.getBeforeHireDate());
        onboardingPeriod.setDuration(item.getDuration());
        onboardingPeriod.setRelativeStart(item.getPeriodRelativeStart());
        this.onboardingPeriodManager.createOrUpdate(onboardingPeriod);
    }

    public Integer saveOnboardingStep(final OnboardingItem item) {
        EdsOnboardingStep onboardingStep = this.onboardingStepManager.getByName(item.getStepName());
        if (onboardingStep != null && (item.getStepId() == null || !onboardingStep.getObjectID().equals(item.getStepId()))) {
            return -1;
        } else {
            if (item.getStepId() == null) {
                onboardingStep = new EdsOnboardingStep();
            } else if (onboardingStep == null) {
                onboardingStep = this.onboardingStepManager.get(item.getStepId());
            }
        }
        final boolean nameChanged = !onboardingStep.isNew() && item.isCreateForm() && !onboardingStep.getName().equals(item.getStepName());
        onboardingStep.setName(item.getStepName());

        String STEPNAME = item.getStepName().
                replaceAll("а", "a").
                replaceAll("б", "b").
                replaceAll("в", "v").
                replaceAll("г", "g").
                replaceAll("д", "d").
                replaceAll("е", "e").
                replaceAll("ё", "yo").
                replaceAll("ж", "zh").
                replaceAll("з", "z").
                replaceAll("и", "i").
                replaceAll("й", "j").
                replaceAll("к", "k").
                replaceAll("л", "l").
                replaceAll("м", "m").
                replaceAll("н", "n").
                replaceAll("о", "o").
                replaceAll("п", "p").
                replaceAll("р", "r").
                replaceAll("с", "s").
                replaceAll("т", "t").
                replaceAll("у", "u").
                replaceAll("ф", "f").
                replaceAll("х", "h").
                replaceAll("ц", "c").
                replaceAll("ч", "ch").
                replaceAll("ш", "sh").
                replaceAll("щ", "sch").
                replaceAll("ъ", "j").
                replaceAll("ы", "i").
                replaceAll("ь", "j").
                replaceAll("э", "e").
                replaceAll("ю", "yu").
                replaceAll("я", "ya").
                replaceAll("А", "A").
                replaceAll("Б", "B").
                replaceAll("В", "V").
                replaceAll("Г", "G").
                replaceAll("Д", "D").
                replaceAll("Е", "E").
                replaceAll("Ё", "Yo").
                replaceAll("Ж", "Zh").
                replaceAll("З", "Z").
                replaceAll("И", "I").
                replaceAll("Й", "J").
                replaceAll("К", "K").
                replaceAll("Л", "L").
                replaceAll("М", "M").
                replaceAll("Н", "N").
                replaceAll("О", "O").
                replaceAll("П", "P").
                replaceAll("Р", "R").
                replaceAll("С", "S").
                replaceAll("Т", "T").
                replaceAll("У", "U").
                replaceAll("Ф", "F").
                replaceAll("Х", "H").
                replaceAll("Ц", "C").
                replaceAll("Ч", "Ch").
                replaceAll("Ш", "Sh").
                replaceAll("Щ", "Sch").
                replaceAll("Ъ", "J").
                replaceAll("Ы", "I").
                replaceAll("Ь", "J").
                replaceAll("Э", "E").
                replaceAll("Ю", "Yu").
                replaceAll("Я", "Ya").
                replaceAll(" ", "_").
                replaceAll("()", "").
                toUpperCase();

        String STEPFORM_ID = Constants.ONBOARDING_STEP_FORM + STEPNAME;

        if (onboardingStep.isNew()) {
            final EdsReference status = new EdsReference();
            status.setCode(STEPNAME);
            status.setName(item.getStepName());
            status.setParent(this.referenceManager.getByCode(EdsOnboardingStep._ONBOARDING_STEP_STATUSES));
            status.setSystemReference(true);
            status.setRemovable(false);
            this.referenceManager.create(status);
            onboardingStep.setStatus(status);
            onboardingStep.setFormID(STEPFORM_ID);
            onboardingStep.setViewName(STEPFORM_ID);
        }
        onboardingStep.setDescription(item.getStepDescription());

        if (item.getPeriodId() != null) {
            onboardingStep.setOnboardingPeriod(this.onboardingPeriodManager.get(item.getPeriodId()));
        } else {
            onboardingStep.setOnboardingPeriod(null);
        }
        if (item.getParentID() != null) {
            onboardingStep.setParent(this.onboardingStepManager.get(item.getParentID()));
        } else {
            onboardingStep.setParent(null);
        }
        onboardingStep.setShowInEmployeeProfile(item.getShowInEmployeeProfile());
        if (item.isCreateForm()) {
            if (onboardingStep.isNew() || this.modelManager.get(onboardingStep.getFormID()) == null) {
                this.createSubStepForm(item.getStepName(), STEPFORM_ID);
            }
            this.commonServiceLocal.createWorkflowModule(STEPNAME, item.getStepName(), true);
            this.createPermissions(STEPNAME, item.getStepName(), true);
            if (nameChanged) {
                this.renameWorkflowModule(STEPNAME, item.getStepName());
                this.renamePermissions(STEPNAME, item.getStepName());
            }
        } else {
            this.commonServiceLocal.createWorkflowModule(STEPNAME, item.getStepName(), false);
            this.createPermissions(STEPNAME, item.getStepName(), false);
        }
        onboardingStep.setCreateForm(item.isCreateForm());
        final EdsOnboardingStepCustomFields onboardingStepCustomFields = this.createOnboardingStepCustomFields(item.getCustomFieldItems());
        onboardingStep.setOnboardingStepCustomFields(onboardingStepCustomFields);
        this.onboardingStepManager.createOrUpdate(onboardingStep);

        List<EdsReference> oldReferences = new ArrayList<>();
        if (item.getStepId() != null && onboardingStep.getStatus() != null) {
            oldReferences = this.referenceManager.listReferences(onboardingStep.getStatus().getCode());
        }
        if (item.getStatusItems() != null && item.getStatusItems().size() > 0) {
            final EdsReference parent = onboardingStep.getStatus();
            for (final ReferenceItem sts : item.getStatusItems()) {
                final EdsReference ref;
                if (sts.getObjectID() == null) {
                    ref = new EdsReference();
                    ref.setParent(parent);
                    ref.setSystemReference(true);
                    ref.setName(sts.getName());
                    ref.setDescription(sts.getDescription());
                    ref.setCode(sts.getName(true).replace(" ", "_").toUpperCase());
                    ref.setSorder(sts.getOrder());
                } else {
                    ref = this.referenceManager.get(sts.getObjectID());
                    ref.setName(sts.getName());
                    ref.setDescription(sts.getDescription());
                    ref.setSorder(sts.getOrder());
                    oldReferences.remove(ref);
                }
                this.referenceManager.createOrUpdate(ref);
            }
            this.deleteReferences(oldReferences);
        } else {
            this.deleteReferences(oldReferences);
        }
        return onboardingStep.getObjectID();
    }

    private void createPermissions(final String code, final String stepName, final boolean create) {
        final Integer companyID = SecurityContext.getCompanyID();
        final String parentCode = PermissionConstants.EMPLOYEE_STEP_ + code + "_LIST" + (companyID == null ? "" : "_" + companyID);
        EdsPermission listPermission = this.permissionManager.findByCode(parentCode, PermissionConstants.HRMS_CONTEXT);
        if (listPermission == null) {
            if (create) {
                final ArrayList<EdsPermission> permissions = new ArrayList<>();
                listPermission = new EdsPermission();
                listPermission.setIsMainMenu(false);
                listPermission.setContext(PermissionConstants.HRMS_CONTEXT);
                listPermission.setCode(parentCode);
                listPermission.setName(stepName + " List");
                final Integer sorder = this.permissionManager.getLastSorderByParent(PermissionConstants.HRMS_EMPLOYEE_STEPS, PermissionConstants.HRMS_CONTEXT);
                listPermission.setSorder(sorder == null || sorder == 0 ? 1 : sorder + 1);
                final EdsPermission parent = this.permissionManager.findByCode(PermissionConstants.HRMS_EMPLOYEE_STEPS, PermissionConstants.HRMS_CONTEXT);
                listPermission.setParent(parent != null ? parent.getObjectID() : null);
                listPermission.setModuleCode(PermissionConstants.ONBOARDING);
                listPermission.setCompanyId(SecurityContext.getCompanyID());
                this.permissionManager.create(listPermission);
                permissions.add(listPermission);
                //Add
                final EdsPermission addPermission = new EdsPermission();
                addPermission.setIsMainMenu(false);
                addPermission.setContext(PermissionConstants.HRMS_CONTEXT);
                addPermission.setCode(PermissionConstants.EMPLOYEE_STEP_ + code + "_ADD" + (companyID == null ? "" : "_" + companyID));
                addPermission.setName(stepName + " Add");
                addPermission.setSorder(1);
                addPermission.setParent(listPermission.getObjectID());
                addPermission.setModuleCode(PermissionConstants.ONBOARDING);
                addPermission.setCompanyId(SecurityContext.getCompanyID());
                this.permissionManager.create(addPermission);
                permissions.add(addPermission);
                //Edit
                final EdsPermission editPermission = new EdsPermission();
                editPermission.setIsMainMenu(false);
                editPermission.setContext(PermissionConstants.HRMS_CONTEXT);
                editPermission.setCode(PermissionConstants.EMPLOYEE_STEP_ + code + "_EDIT" + (companyID == null ? "" : "_" + companyID));
                editPermission.setName(stepName + " Edit");
                editPermission.setSorder(2);
                editPermission.setParent(listPermission.getObjectID());
                editPermission.setModuleCode(PermissionConstants.ONBOARDING);
                editPermission.setCompanyId(SecurityContext.getCompanyID());
                this.permissionManager.create(editPermission);
                permissions.add(editPermission);
                //Delete
                final EdsPermission deletePermission = new EdsPermission();
                deletePermission.setIsMainMenu(false);
                deletePermission.setContext(PermissionConstants.HRMS_CONTEXT);
                deletePermission.setCode(PermissionConstants.EMPLOYEE_STEP_ + code + "_DELETE" + (companyID == null ? "" : "_" + companyID));
                deletePermission.setName(stepName + " Delete");
                deletePermission.setSorder(3);
                deletePermission.setParent(listPermission.getObjectID());
                deletePermission.setModuleCode(PermissionConstants.ONBOARDING);
                deletePermission.setCompanyId(SecurityContext.getCompanyID());
                this.permissionManager.create(deletePermission);
                permissions.add(deletePermission);
                //Export
                final EdsPermission exportPermission = new EdsPermission();
                exportPermission.setIsMainMenu(false);
                exportPermission.setContext(PermissionConstants.HRMS_CONTEXT);
                exportPermission.setCode(PermissionConstants.EMPLOYEE_STEP_ + code + "_EXPORT" + (companyID == null ? "" : "_" + companyID));
                exportPermission.setName(stepName + " Export");
                exportPermission.setSorder(4);
                exportPermission.setParent(listPermission.getObjectID());
                exportPermission.setModuleCode(PermissionConstants.ONBOARDING);
                exportPermission.setCompanyId(SecurityContext.getCompanyID());
                this.permissionManager.create(exportPermission);
                permissions.add(exportPermission);
                //Role permissions
                final EdsRole admin = this.roleManager.getByCode(EdsRole.ADMIN_CODE);
                final EdsRole dr = this.roleManager.getByCode(EdsRole.DR_CODE);
                final EdsRole hr = this.roleManager.getByCode(EdsRole.HR_CODE);
                final List<String> permissionCodes = Lists.newArrayList();
                for (final EdsPermission p : permissions) {
                    if (p != null && p.getCode() != null) {
                        permissionCodes.add(p.getCode());
                    }
                    //Admin
                    final EdsRolePermission adminPermission = new EdsRolePermission();
                    adminPermission.setPermissioncode(p.getCode());
                    adminPermission.setRole(admin);
                    adminPermission.setPriviledgeCode(PermissionConstants.ALLOW);
                    this.rolePermissionManager.create(adminPermission);
                    //DR
                    final EdsRolePermission drPermission = new EdsRolePermission();
                    drPermission.setPermissioncode(p.getCode());
                    drPermission.setRole(dr);
                    drPermission.setPriviledgeCode(PermissionConstants.ALLOW);
                    this.rolePermissionManager.create(drPermission);
                    //HR
                    final EdsRolePermission hrPermission = new EdsRolePermission();
                    hrPermission.setPermissioncode(p.getCode());
                    hrPermission.setRole(hr);
                    hrPermission.setPriviledgeCode(PermissionConstants.ALLOW);
                    this.rolePermissionManager.create(hrPermission);
                }
                this.permissionManager.createPermissionContext(permissionCodes);
            }
        } else if (!create) {
            this.permissionManager.deletePermissions(parentCode, PermissionConstants.HRMS_CONTEXT);
        }
    }

    private void renamePermissions(final String code, final String name) {
        final Integer companyID = SecurityContext.getCompanyID();
        final String parentCode = PermissionConstants.EMPLOYEE_STEP_ + code + "_LIST" + (companyID == null ? "" : "_" + companyID);
        final EdsPermission listPermission = this.permissionManager.findByCode(parentCode, PermissionConstants.HRMS_CONTEXT);
        if (listPermission != null) {
            listPermission.setName(name + " List");
            this.permissionManager.update(listPermission);
        }
        final List<EdsPermission> permissions = this.permissionManager.childByCode(parentCode, PermissionConstants.HRMS_CONTEXT);
        for (final EdsPermission p : permissions) {
            if (p.getCode() != null) {
                if (p.getCode().endsWith("_ADD")) {
                    p.setName(name + " Add");
                } else if (p.getCode().endsWith("_EDIT")) {
                    p.setName(name + " Edit");
                } else if (p.getCode().endsWith("_DELETE")) {
                    p.setName(name + " Delete");
                } else if (p.getCode().endsWith("_EXPORT")) {
                    p.setName(name + " Export");
                }
                this.permissionManager.update(p);
            }
        }
    }

    private void renameWorkflowModule(final String code, final String name) {
        final EdsReference module = this.referenceManager.findReference(WorkflowRule._WORKFLOW_MODULE, WorkflowRule._WORKFLOW_MODULE + "_" + code);
        if (module != null) {
            module.setName(name);
            this.referenceManager.update(module);
        }
    }

    private EdsOnboardingStepCustomFields createOnboardingStepCustomFields(final ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            final EdsOnboardingStepCustomFields edsOnboardingStepCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                edsOnboardingStepCustomFields = this.onboardingStepCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (final CompanyCustomFieldItem fieldItem : customFieldItems) {
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
                edsOnboardingStepCustomFields = new EdsOnboardingStepCustomFields();
                this.onboardingStepCFManager.create(edsOnboardingStepCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsOnboardingStepCustomFields, customFieldItems);
            return edsOnboardingStepCustomFields;
        }
        return null;
    }

    private EdsEmployeeStepCustomFields createEmployeeStepCustomFields(final ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            final EdsEmployeeStepCustomFields edsEmployeeStepCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                edsEmployeeStepCustomFields = this.employeeStepCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (final CompanyCustomFieldItem fieldItem : customFieldItems) {
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
                edsEmployeeStepCustomFields = new EdsEmployeeStepCustomFields();
                this.employeeStepCFManager.create(edsEmployeeStepCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsEmployeeStepCustomFields, customFieldItems);
            return edsEmployeeStepCustomFields;
        }
        return null;
    }

    private void createSubStepForm(final String stepName, String stepFormId) {
        //create model
        final ModelForm model = new ModelForm();
        model.setFormID(stepFormId);
        model.setTitle(stepName.toUpperCase());
        model.setViewName(stepFormId);
        model.setActive(true);
        model.setStepForm(true);
        model.setCustom(true);
        //fields
        final ArrayList<ModelField> fields = new ArrayList<>();
        //Employee
        final ModelField type = new ModelField();
        type.setForm_ID(stepFormId);
        type.setSection(CustomFormConstants.GENERAL_INFORMATION);
        type.setField_ID(CustomFormConstants.TYPE);
        type.setSorder(0);
        type.setSystemMandatory(true);
        type.setWidget("UNKNOWN");
        type.setIsCustomField(false);
        type.setUsableByWorkflow(false);
        type.setSource(null);
        type.setColumnType(ColumnType.COL_1);
        fields.add(type);
        //Employee
        final ModelField employee = new ModelField();
        employee.setForm_ID(stepFormId);
        employee.setSection(CustomFormConstants.GENERAL_INFORMATION);
        employee.setField_ID(CustomFormConstants.EMPLOYEE);
        employee.setSorder(1);
        employee.setSystemMandatory(true);
        employee.setWidget(Constants.UI_TYPE_LOOKUP);
        employee.setIsCustomField(false);
        employee.setUsableByWorkflow(true);
        employee.setSource(ModelField.SOURCE.CRM.CRM_CASE_RESOLVER);
        employee.setColumnType(ColumnType.COL_1);
        fields.add(employee);
        //Status
        final ModelField status = new ModelField();
        status.setForm_ID(stepFormId);
        status.setSection(CustomFormConstants.GENERAL_INFORMATION);
        status.setField_ID(CustomFormConstants.STATUS);
        status.setSorder(3);
        status.setSystemMandatory(true);
        status.setWidget(Constants.UI_TYPE_DROPDOWN);
        status.setIsCustomField(false);
        status.setUsableByWorkflow(true);
        status.setSource(ModelField.SOURCE.REFERENCE + stepName.replace(" ", "_").toUpperCase());
        status.setColumnType(ColumnType.COL_2);
        fields.add(status);
        //expense claim
        final ModelField expense = new ModelField();
        expense.setForm_ID(stepFormId);
        expense.setSection(CustomFormConstants.GENERAL_INFORMATION);
        expense.setField_ID(CustomFormConstants.EXPENSE_CLAIM);
        expense.setSorder(4);
        expense.setSystemMandatory(false);
        expense.setWidget("UNKNOWN");
        expense.setIsCustomField(false);
        expense.setUsableByWorkflow(false);
        expense.setSource(null);
        expense.setColumnType(ColumnType.COL_2);
        fields.add(expense);
        //certificate links
        final ModelField certLinks = new ModelField();
        certLinks.setForm_ID(stepFormId);
        certLinks.setSection(CustomFormConstants.GENERAL_INFORMATION);
        certLinks.setField_ID(CustomFormConstants.LINKED_CERTIFICATES);
        certLinks.setSorder(2);
        certLinks.setSystemMandatory(false);
        certLinks.setWidget("UNKNOWN");
        certLinks.setIsCustomField(false);
        certLinks.setUsableByWorkflow(false);
        certLinks.setSource(null);
        certLinks.setColumnType(ColumnType.COL_1);
        fields.add(certLinks);
        model.setFields(fields);
        this.allInOneServiceLocal.saveModelForm(model);
    }

    public NumberData generatePositionNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = positionManager.getPositionLastIntNumber();
        if (intNumber == null) {
            intNumber = 0;
        }
        if (settings != null && settings.getPositionNumberingFormat() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getPositionNumberingFormat(), settings.getDelimetrPositionNumbering(), null, null, null, "position");
            numberData.setDelimiter(settings.getDelimetrPositionNumbering());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_POSITION_PREFIX);
        }
    }


    private static Map<String, Double> getCertificateSearchFields() {
        final Map<String, Double> fields = new HashMap<>();
        fields.put(SolrCertificateRepresenter.FIELD_NUMBER, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_EMPLOYEE_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_ISSUED_BY_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_TYPE_NAME, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_EMPLOYEE_CODE, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_CURRENT_APPROVER_NAME, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrCertificateRepresenter.FIELD_STATUS_NAME, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    private HashMap<Integer, AnnualLeaveItem> getPositionBenefitData(final Integer positionId) {
        final HashMap<Integer, AnnualLeaveItem> itemHashMap = new HashMap<>();
        if (positionId != null) {
            final Integer currentyear = ServerUtils.getYear(new Date());
            final List<EdsPositionBenefitAllowance> allowances = this.positionBenefitAllowanceManager.listPositionBenefitAllowances(positionId, currentyear);
            EdsReference qtyType;
            EdsCurrency currency;
            for (final EdsPositionBenefitAllowance allowance : allowances) {
                final AnnualLeaveItem item = new AnnualLeaveItem();
                item.setAllowanceYear(allowance.getAllowanceYear());
                item.setAnnualallowancedays(allowance.getAnnualallowance());
                item.setPositionId(positionId);
                item.setReasonId(allowance.getBenefit().getObjectID());
                item.setReasonName(allowance.getBenefit().getName());
                String benefitType = "";
                if (allowance.getBenefit() != null) {
                    qtyType = allowance.getBenefit().getQtytype();
                    currency = allowance.getBenefit().getCurrency();
                    if (qtyType != null) {
                        if (EdsBenefit._CURRENCY.equals(qtyType.getCode())) {
                            benefitType = currency != null ? currency.getName() : "";
                        } else {
                            benefitType = qtyType.getName();
                        }
                    }
                }
                item.setBenefitType(benefitType);
                itemHashMap.put(allowance.getBenefit().getObjectID(), item);
            }
        }
        return this.getFullBenefitAllowance(itemHashMap);
    }

    private HashMap<Integer, AnnualLeaveItem> getFullBenefitAllowance(final HashMap<Integer, AnnualLeaveItem> itemHashMap) {
        final ListingFilterParameter fp = new ListingFilterParameter();
        fp.setActive(true);
        final List<EdsBenefit> benefits = this.benefitManager.getBenefitList(fp);
        EdsReference qtyType;
        EdsCurrency currency;
        for (final EdsBenefit benefit : benefits) {
            if (!itemHashMap.containsKey(benefit.getObjectID())) {
                final AnnualLeaveItem item = new AnnualLeaveItem();
                item.setAllowanceYear(ServerUtils.getYear(new Date()));
                item.setAnnualallowancedays(benefit.getAllowance());
                item.setReasonId(benefit.getObjectID());
                item.setReasonName(benefit.getName());
                qtyType = benefit.getQtytype();
                currency = benefit.getCurrency();
                if (qtyType != null) {
                    if (EdsBenefit._CURRENCY.equals(qtyType.getCode())) {
                        item.setBenefitType(currency != null ? currency.getName() : "");
                    } else {
                        item.setBenefitType(qtyType.getName());
                    }
                }
                itemHashMap.put(benefit.getObjectID(), item);
            }
        }
        return itemHashMap;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getTeamsList() {
        return this.departmentService.getTeamsList();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPositionsList() {
        final List<EdsPosition> pos = this.positionManager.listCompanyPositions();
        final SelectItem[] result = new SelectItem[pos.size()];
        for (int i = 0; i < pos.size(); i++) {
            result[i] = new SelectItem(pos.get(i).getObjectID(), pos.get(i).getName());
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PositionItem getPositionParams() {
        final PositionItem item = new PositionItem();
        item.setNumberData(generatePositionNumber());
        item.setPosStatus(this.commonServiceLocal.convertReference2SelectItem(Constants.POS_STATUS, false, null));
        item.setTimeTypes(this.commonServiceLocal.convertReference2SelectItem(Constants.TIME_TYPES, false, null));
        item.setWeekMonth(this.commonServiceLocal.convertReference2SelectItem(Constants.WEEK_MONTH, false, null));
        final SelectItem[] teamsList = this.getTeamsList();
        if (teamsList != null) {
            item.setTeams(teamsList);
        }
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getSalaryGradeListItems() {
        final EdsUser user = this.userManager.getUser();
        final List<EdsGrade> grades = this.gradeManager.getGradeListByCompany(user.getCompany().getObjectID());
        final SelectItem[] selectGrades = new SelectItem[grades.size()];
        int i = 0;
        for (final EdsGrade grade : grades) {
            final String salaryGrade;
            salaryGrade = grade.getGradeCode() + " " + grade.getGradeLevel();
            selectGrades[i] = new SelectItem(grade.getObjectID(), salaryGrade);
            i++;
        }
        return selectGrades;
    }

    public Integer savePosition(final PositionItem position) {
        final EdsPosition pos;
        NumberData numberData = position.getNumberData();
        if (this.positionManager.getPositionByCode(position.getNumberData().getNumberString(), position.getObjectID()) != null) {
            return Errors.THIS_NAME_IS_ALREADY_EXIST;
        }
        if (position.getObjectID() == null) {
            pos = new EdsPosition();
        } else {
            pos = this.positionManager.get(position.getObjectID());
        }
        if (pos.getExternalGUID() == null) {
            UUID externalGUID = UUID.randomUUID();
            pos.setExternalGUID(externalGUID.toString());
        }
        EdsReference edsReference;
        if (position.getPositionName() == null) {
            EdsReference referencePositionTitles = referenceManager.getByCode("POSITION_TITLES");
            ReferenceItem parentReferenceItem = referencePositionTitles != null ? referencePositionTitles.getRPC() : null;
            ReferenceItem childReferenceItem = new ReferenceItem();
            childReferenceItem.setName(position.getName());
            childReferenceItem.setParentID(parentReferenceItem.getObjectID());
            childReferenceItem.setParent(parentReferenceItem.getName());
            childReferenceItem.setParentCode(parentReferenceItem.getCode());

            Integer newTeamDepartmentId = allInOneService.saveReference(childReferenceItem, null, true);
            edsReference = referenceManager.get(newTeamDepartmentId);
        } else {
            edsReference = referenceManager.get(position.getPositionName().getId());
        }
        pos.setPositionName(edsReference);
        if (edsReference.getLocale() != null) {
            pos.setLocale(edsReference.getLocale());
        }

        //position code
        if (numberData != null) {
            pos.setIntNumber(numberData.getIntNumber());
            pos.setNumberData(numberData.getNumberString());
        }
        //position name (title)
        if (position.getName() != null) {
            pos.setName(position.getName());
        }
        //position established date
        if (position.getEstablished() != null) {
            pos.setEstablished(position.getEstablished());
        }
        //position available date
        if (position.getAvailable() != null) {
            pos.setAvailable(position.getAvailable());
        }
        //position end date
        if (position.getEndDate() != null) {
            pos.setEnddate(position.getEndDate());
        }


        //position status
        if (position.getStatus() != null) {
            EdsReference inactiveStatus = referenceManager.findReference(POS_STATUS, POS_STATUS_FROZEN);
            if (!position.getStatus().getId().equals(inactiveStatus.getObjectID()) && StringUtils.isNotEmpty(position.getCount()) && position.getMembers() != null) {
                boolean isFull = Integer.valueOf(position.getCount()) <= position.getMembers().size();
                EdsReference autoStatus = referenceManager.findReference(POS_STATUS, isFull ? POS_STATUS_ACTIVE : POS_STATUS_OPEN);
                pos.setStatus(autoStatus.getObjectID());
                pos.setStatusEnt(autoStatus);
            } else {
                pos.setStatus(position.getStatus().getId());
                pos.setStatusEnt(referenceManager.get(position.getStatus().getId()));
            }
        }
        //position job family
        if (position.getJobfamily() != null) {
            pos.setJobFamily(position.getJobfamily().getId());
        }

        if (position.getCoefficent() != null) {
            pos.setCoefficient(position.getCoefficent());
        }

        //position full time/part-time
        if (position.getFullPartTime() != null) {
            pos.setFullPartTime(position.getFullPartTime().getId());
        }

        //position department
        if (position.getDepartment() != null) {
            pos.setDepartment(position.getDepartment().getId());
            pos.setDepartmentObject(departmentManager.get(position.getDepartment().getId()));
        } else {
            pos.setDepartment(null);
        }
        //position reports to
        if (position.getReportsTo() != null) {
            pos.setReportsTo(position.getReportsTo().getId());
        }

        if (position.getLocation() != null) {
            pos.setLocationId(position.getLocation().getId());
            pos.setLocation(locationManager.get(position.getLocation().getId()));
        } else {
            pos.setLocationId(null);
        }

        if (position.getType() != null) {
            pos.setType(referenceManager.get(position.getType().getId()));
        } else {
            pos.setType(null);
        }

        pos.setSalaryBasis(position.getSalaryBasis());
        pos.setJobrequirements(position.getJobRequirements());
        pos.setJobRequirementsLocalize(WfmJsonUtils.objectConvertToJsonString(position.getJobRequirementLocalize()));

        pos.setDetailingDescription(position.getPositionDescription());
        pos.setDescriptionLocalize(WfmJsonUtils.objectConvertToJsonString(position.getDescriptionLocalize()));

        pos.setResponsibility(position.getResponsibility());
        pos.setResponsibilityLocalize(WfmJsonUtils.objectConvertToJsonString(position.getResponsibilitiesLocalize()));


        pos.setMeasuringEmployeePerformance(position.getMeasuringEmployeePerformance());
        pos.setMeasuringEmployeePerformanceLocalize(WfmJsonUtils.objectConvertToJsonString(position.getMeasuringEmployeePerformanceLocalize()));

        pos.setPersonalQualities(position.getPersonalQualities());
        pos.setPersonalQualitiesLocalize(WfmJsonUtils.objectConvertToJsonString(position.getPersonalQualitiesLocalize()));

        pos.setKnowledge(position.getKnowledge());
        pos.setKnowledgeLocalize(WfmJsonUtils.objectConvertToJsonString(position.getKnowledgeLocalize()));


        pos.setCount(position.getCount());
        pos.setWageRate(position.getWageRate());
        pos.setClientChargeRate(position.getClientChargeRate());
        pos.setMinSalary(position.getMinSalary());
        pos.setMidSalary(position.getMidSalary());
        pos.setMaxSalary(position.getMaxSalary());
        //position street address
        pos.setStreetAddress(position.getStreetAddress());
        //position description
        pos.setDescription(position.getDescription());

        if (position.getObjectID() == null) {
            pos.setCreator(userManager.getUser());
            pos.setCreationTime(new Date());
        }


        pos.setLastUpdateTime(new Date());
        pos.setUpdater(userManager.getUser());

        EdsPositionCustomFields customFields = createPositionCustomFields(position.getCustomFieldItems());
        pos.setCustomFields(customFields);

        EdsPositionBenefitAllowance allowance = null;
        pos.getBenefitAllowances().clear();
        if (position.getBenefititems() != null && position.getBenefititems().size() > 0) {
            for (final Map.Entry<Integer, AnnualLeaveItem> entry : position.getBenefititems().entrySet()) {
                if (entry.getValue() != null) {
                    allowance = new EdsPositionBenefitAllowance();
                    allowance.setAnnualallowance(entry.getValue().getAnnualallowancedays());
                    allowance.setAllowanceYear(ServerUtils.getYear(new Date()));
                    allowance.setBenefit(this.benefitManager.get(entry.getKey()));
                }
                pos.getBenefitAllowances().add(allowance);
            }
        }

        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPosition.class.getSimpleName());
        if (position.getObjectID() != null) {
            this.positionManager.update(pos);
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(pos.getObjectID());
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Update position");
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, pos, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_POSITION);
        } else {
            this.positionManager.create(pos);
            if (pos.getObjectID() != null) {
                kpiLog.setActionType(KpiLog.ActionType.ADD);
                kpiLog.setEntityId(pos.getObjectID());
                ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Add position");
                EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, pos, userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_POSITION);
            }
        }
        try {
            positionSolrComponent.index(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (position.getApplyBenefitForEmployees() != null) {
            this.createPostionEmployeesBenefitAllowance(pos, position.getBenefititems());
        }
        //assign/unassign employees

        ArrayList<Integer> oldEmployeeIDs = this.employeeManager.getEmployeePosition(pos.getObjectID());
        oldEmployeeIDs = oldEmployeeIDs == null ? new ArrayList<>() : oldEmployeeIDs;
        final ArrayList<Integer> toAssignIDs = new ArrayList<>();
        if (position.getMembers() != null && position.getMembers().size() > 0) {
            for (final KpiTreeInfo employee : position.getMembers()) {
                if (!oldEmployeeIDs.contains(employee.getEmployeeId())) {
                    toAssignIDs.add(employee.getEmployeeId());
                } else {
                    oldEmployeeIDs.remove(employee.getEmployeeId());
                }
            }
        }
        if (!oldEmployeeIDs.isEmpty()) {
            this.employeeManager.updateEmployeesPosition(oldEmployeeIDs, null);
        }
        if (!toAssignIDs.isEmpty()) {
            this.employeeManager.updateEmployeesPosition(toAssignIDs, pos.getObjectID());
        }
        final ArrayList<Integer> affectedEmployeeIDs = new ArrayList<>(toAssignIDs);
        affectedEmployeeIDs.addAll(oldEmployeeIDs);
        if (!affectedEmployeeIDs.isEmpty()) {
            final EdsBusinessEvent s = this.baseEventPostProcessor.registerEvent(EmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_CUSTOM, new EdsEmployee(), this.userManager.getUser());
            s.setCustomStringField(affectedEmployeeIDs.stream().map(Object::toString).collect(Collectors.joining(",")));
        }
        return pos.getObjectID();
    }

    @Override
    public void savePositionItems(ArrayList<PositionItem> positionItems) {
        positionItems.forEach(p -> {
            p.setNumberData(generatePositionNumber());
            savePosition(p);
        });
    }

    @Override
    public void savePositionEditCellValue(PositionItem rowValue, String columnCodeName) {
        try {
            EdsPosition position = positionManager.get(rowValue.getObjectID());
            EdsPositionCustomFields positionCF = position.getCustomFields();
            if (positionCF == null) {
                positionCF = new EdsPositionCustomFields();
                positionCFManager.create(positionCF);
                position.setCustomFields(positionCF);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(positionCF, rowValue.getCustomFieldValuesItems(), columnCodeName);
            positionManager.update(position);
        } catch (Exception e) {
            System.out.println("Position Edit Cell Column Code :" + columnCodeName);
        }
    }

    private void createPostionEmployeesBenefitAllowance(final EdsPosition pos, final HashMap<Integer, AnnualLeaveItem> benefitItems) {
        if (benefitItems != null && benefitItems.size() > 0) {
            final ListingFilterParameter fp = new ListingFilterParameter();
            fp.setPositionID(pos.getObjectID());
            final List<EdsEmployee> employees = this.employeeManager.list(fp);
            for (final EdsEmployee employee : employees) {
                this.createEmployeeBenefitAllowance(employee, benefitItems);
            }
        }
    }

    public Integer saveJobDescription(final PositionItem positionItem) {
        if (positionItem.getObjectID() != null) {
            final EdsPosition position = this.positionManager.get(positionItem.getObjectID());
            position.setJobPurpose(positionItem.getJobPurpose());
            position.setDescription(positionItem.getDescription());
            if (positionItem.getJobEvalPoints() != null) {
                position.setJobEvalPoints(positionItem.getJobEvalPoints());
            }
            if (positionItem.getPrimaryResponsibilities() != null) {
                position.setPrimaryResponse(positionItem.getPrimaryResponsibilities());
            }
            if (positionItem.getWorkingConditions() != null) {
                position.setWorkingConditions(positionItem.getWorkingConditions());
            }
            if (positionItem.getKnowledge() != null) {
                position.setKnowledge(positionItem.getKnowledge());
            }
            if (positionItem.getExpirence() != null) {
                position.setExpirence(positionItem.getExpirence());
            }
            if (positionItem.getSkills() != null) {
                position.setSkills(positionItem.getSkills());
            }
            if (positionItem.getQualification() != null) {
                position.setQualification(positionItem.getQualification());
            }
            if (positionItem.getPersonalAttributes() != null) {
                position.setPersonalAttr(positionItem.getPersonalAttributes());
            }

            final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsPosition.class.getSimpleName());
            if (position.getObjectID() != null) {
                this.positionManager.update(position);
                kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                kpiLog.setEntityId(position.getObjectID());
                ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Update Job Description");
            }

            return position.getObjectID();
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getJobFamilies() {
        final List<EdsJobFamily> jobs = this.jobFamilyManager.getJobFamilies(this.userManager.getUser().getCompany());
        final SelectItem[] result = new SelectItem[jobs.size()];
        for (int i = 0; i < jobs.size(); i++) {
            result[i] = new SelectItem(jobs.get(i).getObjectID(), jobs.get(i).getName());
        }
        return result;
    }

    public Integer createJobFamily(final SelectItem jfamily) {
        final EdsJobFamily jf = new EdsJobFamily();
        jf.setName(jfamily.getName());
        jf.setDescription(jfamily.getDescription());
        this.jobFamilyManager.create(jf);
        return jf.getObjectID();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<GoalItem> getProjectGoalList(final ListingFilterParameter filterParametrs) {
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityType(EdsGoal.PROJECT_GOAL);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Get project goal list");
        final EdsReference projectGoal = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PROJECT_GOAL);
        filterParametrs.setCrmEntityId(projectGoal.getObjectID());
        filterParametrs.setEntityName(ViewName.ProjectGoal.name());
        final ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        if (panelTools != null && panelTools.isCustomFieldsShown()) {
            filterParametrs.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(this.commonService.getCompanyCustomFieldsForListView(ViewName.ProjectGoal));
        }
        return this.getGoalList(filterParametrs);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRoles(final String formId) {
        final List<EdsRole> roles = this.getUserRolesByPattern(this.roleManager.list());
        final LinkedList<SelectItem> r = new LinkedList<>();
        for (final EdsRole rol : roles) {
            if (!rol.getObjectID().equals(EdsRole.CLIENT)) {
                final SelectItem selectItem = new SelectItem();
                selectItem.setId(rol.getObjectID());
                selectItem.setName(this.commonLocalizer.localize(rol.getCode(), rol.getName()));
                if (!rol.getObjectID().equals(EdsRole.ADMIN) || rol.getObjectID().equals(EdsRole.ADMIN) && this.userManager.getUser().hasEitherRoles(EdsRole.ADMIN)) {
                    r.add(selectItem);
                }
            }
        }
        return r.toArray(new SelectItem[]{});
    }

    public SelectItem[] getFingerprintDevices() {
        return attendanceTerminalManager.getAll().stream()
                .map(fs -> new SelectItem(fs.getObjectID(), fs.getCompanyBranchName()))
                .sorted(Comparator.comparing(SelectItem::getName))
                .toArray(SelectItem[]::new);
    }

    private List<EdsRole> getUserRolesByPattern(final List<EdsRole> roles) {
        Integer[] sortRoleByPattern = {EdsRole.ADMIN, EdsRole.DR, EdsRole.HR, EdsRole.ACCOUNTANT, EdsRole.ADMIN_LOCATION,
                EdsRole.SALESMAN, EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE, EdsRole.CUSTOMER_SERVICE_MANAGER, EdsRole.SALESPERSON, EdsRole.TL, EdsRole.PM,
                EdsRole.MEM, EdsRole.CALENDAR_EDITOR, EdsRole.CALENDAR_VIEWER, EdsRole.CLIENT, EdsRole.TIMESHEET_EDITOR, EdsRole.GUEST, EdsRole.INSTRUCTOR};
        final List<EdsRole> userRoles = new ArrayList<>();
        EdsUser currentUser = userManager.getUser();
        if (currentUser != null && currentUser.getCompany() != null && currentUser.getCompany().getObjectID() != null) {
            //add EXPERT ROLE to COO and ATM
            if (currentUser.getCompany().getObjectID().equals(5377) || currentUser.getCompany().getObjectID().equals(8934)) {
                sortRoleByPattern = new Integer[]{EdsRole.ADMIN, EdsRole.DR, EdsRole.HR, EdsRole.ACCOUNTANT, EdsRole.ADMIN_LOCATION,
                        EdsRole.SALESMAN, EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE, EdsRole.SALESPERSON, EdsRole.TL, EdsRole.PM,
                        EdsRole.MEM, EdsRole.CALENDAR_EDITOR, EdsRole.CALENDAR_VIEWER, EdsRole.CLIENT, EdsRole.CHAT_EXPERT, EdsRole.TIMESHEET_EDITOR, EdsRole.GUEST, EdsRole.INSTRUCTOR};
            }
        }
        final EdsModule trainingCenter = this.moduleManager.getModuleByCode(PermissionConstants.TRAINING_CENTER);
        for (final Integer aSortRoleByPattern : sortRoleByPattern) {
            final EdsRole rol;

            if (EdsRole.INSTRUCTOR.equals(aSortRoleByPattern)) {
                rol = this.roleManager.getByCode(Constants.INSTRUCTOR_CODE);
            } else {
                rol = this.roleManager.get(aSortRoleByPattern);
            }

            if (roles.contains(rol)) {
                if (Constants.INSTRUCTOR_CODE.equals(rol.getCode())) {
                    if (trainingCenter != null) {
                        userRoles.add(rol);
                    }
                } else {
                    userRoles.add(rol);
                }
            }
        }
        for (final EdsRole role : roles) {
            if (!userRoles.contains(role) && (role.getDeleted() == null || !role.getDeleted()) && (role.getSystem() == null || !role.getSystem())) {
                userRoles.add(role);
            }
        }
        return userRoles;
    }

    private String getSortedRolesAsString(final Set<EdsRole> roles) {
        final StringBuilder rolesString = new StringBuilder();
        final List rolesList = this.getRolesSortedByPattern(roles);
        for (final Object aRolesList : rolesList) {
            if (rolesString.length() > 0) {
                rolesString.append(", ");
            }
            rolesString.append(this.roleManager.get((Integer) aRolesList).getName());
        }
        return rolesString.toString();
    }

    private List getRolesSortedByPattern(final Set<EdsRole> roles) {
        return this.getRolesSortedByPattern(roles, new Integer[]{EdsRole.ADMIN, EdsRole.ADMIN_LOCATION, EdsRole.DR, EdsRole.HR, EdsRole.ACCOUNTANT, EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE, EdsRole.SALESMAN, EdsRole.TL, EdsRole.PM, EdsRole.MEM, EdsRole.SALESPERSON, EdsRole.CALENDAR_EDITOR, EdsRole.CALENDAR_VIEWER, EdsRole.CLIENT, EdsRole.TIMESHEET_EDITOR, EdsRole.GUEST});
    }

    private List getRolesSortedByPattern(final Set<EdsRole> roles, final Integer[] sortPattern) {
        final List rolesList = new ArrayList();
        for (final Integer aSortPattern : sortPattern) {
            if (roles.contains(this.roleManager.get(aSortPattern))) {
                rolesList.add(aSortPattern);
            }
        }
        return rolesList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public GoalAssigneeItem[] getEmployees(final ListingFilterParameter fp) {

        final EdsUser user = this.userManager.getUser();

        if (ServerUtils.hasPermission(SHOW_DEPARTMENT_EMPLOYEE_LIST)
                && !(user.hasRole(this.roleManager.get(EdsRole.ADMIN))
                || ServerUtils.hasPermission(SHOW_ALL_EMPLOYEE_LIST))) {

            if (user instanceof EdsEmployee
                    && user.getEmployee() != null
                    && ((EdsEmployee) user).getTeam() != null) {

                fp.setDepartmentId(((EdsEmployee) user).getTeam().getObjectID());
            }
        }

        final List<EdsEmployee> employees = this.employeeManager.list(fp);
        final GoalAssigneeItem[] result = new GoalAssigneeItem[employees.size()];

        EdsGoal goal = null;
        if (fp.getObjectId() != null) {
            goal = this.goalManager.get(fp.getObjectId());
        }

        Set<EdsGoalAssignees> assignees = Collections.emptySet();
        if (goal != null && !goal.getUndeletedGoalAssignees().isEmpty()) {
            assignees = new HashSet<>(goal.getUndeletedGoalAssignees());
        }

        // Build fast lookup map: employeeId -> goalAssignee
        Map<Integer, EdsGoalAssignees> assigneeMap = new HashMap<>();
        for (EdsGoalAssignees ga : assignees) {
            if (ga.getAssignee() != null) {
                assigneeMap.put(ga.getAssignee().getObjectID(), ga);
            }
        }

        int index = 0;

        for (final EdsEmployee employee : employees) {

            GoalAssigneeItem item = new GoalAssigneeItem();

            item.setId(employee.getObjectID());
            item.setName(employee.getName());

            if (employee.getEmployeeTeam() != null
                    && employee.getEmployeeTeam().getTeam() != null) {

                item.setDepartmentId(employee.getEmployeeTeam().getTeam().getObjectID());
                item.setDepartmentName(employee.getEmployeeTeam().getTeam().getName());
            }

            final Double usedWeight =
                    this.goalAssigneesManager.getEmployeeWeightSum(
                            employee, fp.getValidityPeriodId());

            if (usedWeight != null) {
                item.setAvaWeight(Math.max(0, 100 - usedWeight));
            }

            EdsGoalAssignees ga = assigneeMap.get(employee.getObjectID());
            if (ga != null) {
                item.setObjectId(ga.getObjectID());
                item.setActual(ga.getActual());
                item.setTarget(ga.getTarget());
                item.setWeight(ga.getWeight());
                item.setAssignee(true);
            }

            result[index++] = item;
        }

        return result;
    }

    @Override
    public RotationItem getDepartmentAndPositionForRotation(Integer employeeId) {
        RotationItem item = new RotationItem();
        EdsEmployee employee = employeeManager.get(employeeId);
        item.setCurLocation(employee.getLocation().getAsSelectItem());
        item.setCurDepartment(employee.getTeam().getAsSelectItem());
        item.setCurPosition(employee.getPosition().getAsSelectItem());
        ArrayList<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(), commonService.getCompanyCustomFields(ViewName.Employee));
        HashMap<String, CompanyCustomFieldItem> empCustomField = new HashMap<>();
        for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
            empCustomField.put(customFieldItem.getAliasName(), customFieldItem);
        }
        item.setEmployeeCustomFields(empCustomField);
        item.setCustomFieldItems(customFieldItems);
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getLocationList() {
        final List<EdsLocation> list = this.locationManager.list(new ListingFilterParameter());
        final SelectItem[] items = new SelectItem[list.size()];
        int j = 0;
        for (final EdsLocation location : list) {
            final String locations = location.getCountry().getName() + "," + location.getCity();
            items[j] = new SelectItem(location.getObjectID(), locations);
            j++;
        }
        return items;
    }

    private static Map<String, Double> getPositionSearchFields() {
        final Map<String, Double> fields = new HashMap<>();
        fields.put(SolrPositionRepresenter.FIELD_NUMBER, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_LOCATION_NAME, SolrSearchUtils.ABOVE_NORMAL_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_DEPARTMENT_NAME, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_CREATED_BY_NAME, SolrSearchUtils.NORMAL_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_TYPE_NAME, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_TYPE_NAME_UZ, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_TYPE_NAME_RU, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_TYPE_NAME_AR, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_TYPE_NAME_EN, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrPositionRepresenter.FIELD_STATUS_NAME, SolrSearchUtils.LOW_PRIORITY);
        fields.put(SolrTaskRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.LOW_PRIORITY);
        return fields;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PositionItem getPositionForEdit(final Integer positionId, String type) {
        final PositionItem item = new PositionItem();
        if (positionId != null) {
            final EdsPosition pos = this.positionManager.getPositionForEdit(positionId);

            item.setObjectID(pos.getObjectID());
            item.setName("EDIT".equals(type) ? pos.getRealPositionName() : pos.getName());
            item.setDescription(pos.getDescription());

            if (pos.getNumberData() != null) {
                NumberData numberData = new NumberData();
                numberData.setFirstNumberString(pos.getNumberData());
                numberData.setNumberFormat("_");
                item.setNumberData(numberData);
            }
            if (pos.getLocale() != null) {
                item.setLocaleItem(pos.getLocale().toRPC());
            }
            if (pos.getPositionName() != null) {
                item.setPositionName(pos.getPositionName().getAsSelectItem());
            } else if (referenceManager.getByName(pos.getOriginalName()) != null) {
                item.setPositionName(referenceManager.getByName(pos.getOriginalName()).getAsSelectItem());
            }
            if (pos.getFullPartTime() != null) {
                item.setFullPartTime(new SelectItem(pos.getFullPartTime(), this.referenceWfmMessageSource.localizeRef(this.referenceManager.getReference(pos.getFullPartTime()))));
            }
            if (pos.getSalaryGrade() != null) {
                item.setSalaryGrade(pos.getSalaryGrade());
            }
            if (pos.getEstablished() != null) {
                item.setEstablished(pos.getEstablished());
            }
            if (pos.getAvailable() != null) {
                item.setAvailable(pos.getAvailable());
            }
            if (pos.getEnddate() != null) {
                item.setEndDate(pos.getEnddate());
            }
            if (pos.getStatus() != null) {
                item.setStatus(new SelectItem(pos.getStatus().getObjectID(), pos.getStatus().getName()));
            }
            if (pos.getLocation() != null) {
                item.setLocation(new SelectItem(pos.getLocation().getObjectID(), pos.getLocation().getName()));
            }
            if (pos.getCount() != null) {
                item.setCount(pos.getCount());
            }
            if (pos.getType() != null) {
                item.setType(pos.getType().getAsSelectItem());
            }
            if (pos.getCoefficient() != null) {
                item.setCoefficent(pos.getCoefficient());
            }

            EdsJobFamily jobFamily = this.jobFamilyManager.get(pos.getJobFamily());
            if (jobFamily != null) {
                item.setJobfamily(new SelectItem(jobFamily.getObjectID(), jobFamily.getName()));
            }

            if (pos.getDepartment() != null) {
                final EdsDepartment department = this.departmentManager.get(pos.getDepartment());
                item.setDepartment(department != null ? new SelectItem(pos.getDepartment(), department.getName()) : null);
                if (department != null && department.getDepartmentName() != null && department.getDepartmentName().getLocale() != null) {
                    EdsReferenceLocale locale = department.getDepartmentName().getLocale();
                    String userLocale = ServerUtils.getUserLocale().getLanguage();
                    switch (userLocale) {
                        case "en" ->
                                item.setDepartment(new SelectItem(pos.getDepartment(), department.getNumberData() + "-> " + locale.getEnglish()));
                        case "ru" ->
                                item.setDepartment(new SelectItem(pos.getDepartment(), department.getNumberData() + "-> " + locale.getRussian()));
                        case "uz" ->
                                item.setDepartment(new SelectItem(pos.getDepartment(), department.getNumberData() + "-> " + locale.getUzbek()));
                        case "ar" ->
                                item.setDepartment(new SelectItem(pos.getDepartment(), department.getNumberData() + "-> " + locale.getArabic()));
                    }

                }
                item.setDepartmentId(department != null ? department.getObjectID() : null);
            }
            item.setStreetAddress(pos.getStreetAddress());
            if (pos.getReportsTo() != null) {
                item.setReportsTo(new SelectItem(pos.getReportsTo(), this.positionManager.get(pos.getReportsTo()).getName()));
            }
            if (pos.getType() != null) {
                item.setType(pos.getType().getAsSelectItem());
            }

            item.setSalaryBasis(pos.getSalaryBasis());
            item.setResponsibility(pos.getResponsibility());
            item.setJobRequirements(pos.getJobrequirements());
            item.setPositionDescription(pos.getDetailingDescription());
            item.setDescriptionLocalize(WfmJsonUtils.jsonStringConvertToObject(pos.getDescriptionLocalize(), HashMap.class));
            item.setJobRequirementLocalize(WfmJsonUtils.jsonStringConvertToObject(pos.getJobRequirementsLocalize(), HashMap.class));
            item.setResponsibilitiesLocalize(WfmJsonUtils.jsonStringConvertToObject(pos.getResponsibilityLocalize(), HashMap.class));


            item.setMeasuringEmployeePerformance(pos.getMeasuringEmployeePerformance());
            item.setPersonalQualities(pos.getPersonalQualities());
            item.setKnowledge(pos.getKnowledge());

            item.setMeasuringEmployeePerformanceLocalize(WfmJsonUtils.jsonStringConvertToObject(pos.getMeasuringEmployeePerformanceLocalize(), HashMap.class));
            item.setPersonalQualitiesLocalize(WfmJsonUtils.jsonStringConvertToObject(pos.getPersonalQualitiesLocalize(), HashMap.class));
            item.setKnowledgeLocalize(WfmJsonUtils.jsonStringConvertToObject(pos.getKnowledgeLocalize(), HashMap.class));


            item.setJobEvalPoints(pos.getJobEvalPoints());
            item.setJobPurpose(pos.getJobPurpose());
            item.setPrimaryResponsibilities(pos.getPrimaryResponse());
            item.setWorkingConditions(pos.getWorkingConditions());
            item.setKnowledge(pos.getKnowledge());
            item.setExpirence(pos.getExpirence());
            item.setSkills(pos.getSkills());
            item.setQualification(pos.getQualification());
            item.setPersonalAttributes(pos.getPersonalAttr());
            item.setBudgetedHours(pos.getBudgetedHours() != null ? pos.getBudgetedHours() : Float.valueOf("0.0"));
            item.setBudgetedPay(pos.getBudgetedPay() != null ? pos.getBudgetedPay() : Float.valueOf("0.0"));
            item.setWageRate(pos.getWageRate());
            item.setClientChargeRate(pos.getClientChargeRate());
            item.setMinSalary(pos.getMinSalary());
            item.setMidSalary(pos.getMidSalary());
            item.setMaxSalary(pos.getMaxSalary());

            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Positions);
            item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(pos.getCustomFields(), customFieldsItems));

            if (pos.getBHPeriod() != null) {
                final EdsReference bhperiod = this.referenceManager.getReference(pos.getBHPeriod());
                item.setBHPeriod(new SelectItem(bhperiod.getObjectID(), this.referenceWfmMessageSource.localizeRef(bhperiod)));
            }
            if (pos.getBPPeriod() != null) {
                final EdsReference bpperiod = this.referenceManager.getReference(pos.getBPPeriod());
                item.setBPPeriod(new SelectItem(bpperiod.getObjectID(), this.referenceWfmMessageSource.localizeRef(bpperiod)));
            }
            item.setAnnualCost(pos.getAnnualCost() != null ? pos.getAnnualCost() : Float.valueOf("0.0"));
            item.setPosStatus(this.commonServiceLocal.convertReference2SelectItem(Constants.POS_STATUS, false, null));
            item.setTimeTypes(this.commonServiceLocal.convertReference2SelectItem(Constants.TIME_TYPES, false, null));
            item.setWeekMonth(this.commonServiceLocal.convertReference2SelectItem(Constants.WEEK_MONTH, false, null));
            if (this.getTeamsList() != null) {
                item.setTeams(this.getTeamsList());
            }
            final List<EdsEmployee> employeeList = this.employeeManager.getPositionEmployees(pos);
            if (employeeList != null) {
                item.setHeadCount(employeeList.size());
            }
            final int size = employeeList != null ? employeeList.size() : 0;
            final EmployeeListItem[] empItems = new EmployeeListItem[size];
            int index = 0;
            for (final EdsEmployee employee : employeeList) {
                final EmployeeListItem empItem = new EmployeeListItem();
                empItem.setObjectID(employee.getObjectID());
                empItem.setFirstName(employee.getFirstName());
                empItem.setLastName(employee.getLastName());
                empItem.setDepartment(employee.getTeam().getName());
                empItem.setStatus(this.referenceWfmMessageSource.localize(employee.getAccountStatus().getCode(), employee.getAccountStatus().getName()));
                empItems[index++] = empItem;
            }
            item.setEmployeesData(empItems);
        } else {
            item.setNumberData(generatePositionNumber());
            EdsUser user = userManager.getUser();
            if (user.getLocation() != null) {
                EdsDepartment team = user.getEmployee().getTeam();
                if (team != null)
                    item.setCreatorDepartment(team.getAsSelectItem());
            }
            if (user.getLocation() != null) {
                item.setCreatorLocation(user.getLocation().getAsSelectItem());
            }
        }
        item.setPositionRefId(referenceManager.getByCode(POSITION_TITLES).getObjectID());
        item.setBenefititems(this.getPositionBenefitData(positionId));
        item.setTemplates(getRotationPdfTemplates(PdfReferenceCodeNameEnum.POSITION.name()).getItems());
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<PositionItem> getPositionList(final ListingFilterParameter filterParametrs) {
//        SolrClient server = WfmJpaTemplate.getSolrServerForCore(Constants.SOLR_POSITION_CORE);
//        QueryResponse resp = null;
//        try {
//            resp = server.query(getSolrQueryForPosition(filterParametrs), SolrRequest.METHOD.POST);
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
        Page<PositionSolrDoc> positionSolrDocs = positionSolrComponent.getList(filterParametrs);
        return getPositionsFromSolrResult(positionSolrDocs, filterParametrs);
    }

    @Override
    public SelectItem[] getLookUpItems(ListingFilterParameter filterParametrs, final Integer type) {
        if (type == LookUpConstants.EMPLOYEE_ID) {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            return this.allInOneServiceLocal.getEmployeesAsSelectItem(filterParametrs);
        } else if (type == LookUpConstants.POSITION_ID) {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;

            final List<EdsPosition> edsPositions = this.positionManager.list();
            if (edsPositions != null && edsPositions.size() > 0) {
                final List<SelectItem> positions = edsPositions.stream().map(EdsPosition::getAsSelectItem).toList();
                return positions.toArray(new SelectItem[]{});
            }
            return null;
        } else if (type == LookUpConstants.LOCATION_ID) {
            return this.locationManager.getLocationsAsSelectItems(filterParametrs);

        } else if (type == LookUpConstants.HR_DEPARTMENT_ID) {
            return this.departmentManager.getDepartmentsForAccounting(filterParametrs);
        }
        if (type == LookUpConstants.TIMESLOT_ID) {
            return timeSlotManager.getTimeslotsAsSelectItem(filterParametrs);
        }
        return new SelectItem[0];
    }

//    @Transactional
//    public SolrQuery getSolrQueryForPosition(ListingFilterParameter fp) {
//        EdsUser user = userManager.getUser();
//        EdsCompany company = user.getCompany();
//
//        FacetFilterRpc positionFacetFilter = fp.getFacetFilter();
//        if (positionFacetFilter != null && !positionFacetFilter.isFilterChanges()) {
//            positionFacetFilter = commonServiceLocal.getUserFacetFilter(positionFacetFilter);
//        }
//        SolrQuery query = new SolrQuery();
//        fp.setLocationId(user.getLocation() != null ? user.getLocation().getObjectID() : null);
//        query.setQuery(QueryBuilderForSolr.getPositionListCore(fp, user, company) +
//                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(positionFacetFilter, company, null, null));
//
//        query.setStart(fp.getStart());
//        query.setParam(CommonParams.ROWS, String.valueOf(fp.getLimit()));
//
//        if (!fp.isSearchButton()) {
//            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
//                boolean desc = Constants.DESC == fp.asConfig().getSortDir();
//                if (PositionItem.POSITION_TITLE.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (PositionItem.POSITION_CODE.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_NUMBER, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (PositionItem.STATUS.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_STATUS_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (PositionItem.EMPLOYEE_COUNT.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_EMPLOYEE_COUNT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (PositionItem.LOCATION.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_LOCATION_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (PositionItem.DEPARTMENT.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_DEPARTMENT_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (PositionItem.POSITION_COUNT.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_VACANT_COUNT, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (PositionItem.CREATED_DATE.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_CREATED_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (PositionItem.CREATED_BY.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_CREATED_BY_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (PositionItem.MODIFIED_DATE.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_MODIFIED_DATE, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (PositionItem.MODIFIED_BY.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_MODIFIED_BY_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                } else if (PositionItem.TYPE.equals(fp.getSortField())) {
//                    query.setSort(SolrPositionRepresenter.SORTABLE_TYPE_NAME, (desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc));
//                }
//            } else {
//                query.setSort(SolrPositionRepresenter.SORTABLE_NUMBER, SolrQuery.ORDER.desc);
//            }
//        }
//        return query;
//    }

    @Override
    @Transactional
    public void saveGoalAssigneeItems(final GoalAssigneeItem[] assigneeItems) {
        if (assigneeItems != null) {
            for (final GoalAssigneeItem assignItem : assigneeItems) {
                final EdsGoalAssignees goalAssignees = this.goalAssigneesManager.get(assignItem.getObjectId());
                goalAssignees.setDeleted(false);
                if (assignItem.getValidityPeriodId() != null && goalAssignees.getGoal() != null) {
                    final EdsValidityPeriod validityPeriod = this.validityPeriodManager.get(assignItem.getValidityPeriodId());
                    goalAssignees.getGoal().setValidityPeriod(validityPeriod);
                }
                goalAssignees.setWeight(assignItem.getWeight());
                goalAssignees.setActual(assignItem.getActual());
                goalAssignees.setDescription(assignItem.getDescription());
                this.goalAssigneesManager.update(goalAssignees);
            }
        }
    }

    private ListResult<PositionItem> getPositionsFromSolrResult(Page<PositionSolrDoc> resp, ListingFilterParameter fp) {
        ArrayList<PositionItem> items = new ArrayList<>();
        int totalCount = 0;
        if (resp != null) {
            totalCount = (int) resp.getTotalElements();
            String lang = ServerUtils.getUserLocale().getLanguage();
            for (PositionSolrDoc relevantDoc : resp.getContent()) {
                PositionItem item = new PositionItem();
                item.setObjectID(relevantDoc.getPositionId());
                item.setNumberData(new NumberData(relevantDoc.getNumber(), -1));
                item.setNumber(relevantDoc.getNumber());
                String name = null, type = null, status = null, department = null;
                if (lang != null && !lang.isEmpty()) {
                    switch (lang) {
                        case "uz" -> {
                            name = relevantDoc.getNameUz();
                            status = relevantDoc.getStatusUz();
                            type = relevantDoc.getTypeNameUz();
                            department = relevantDoc.getDepartmentNameUz();
                        }
                        case "ru" -> {
                            name = relevantDoc.getNameRu();
                            status = relevantDoc.getStatusRu();
                            type = relevantDoc.getTypeNameRu();
                            department = relevantDoc.getDepartmentNameRu();
                        }
                        case "en" -> {
                            name = relevantDoc.getNameEn();
                            status = relevantDoc.getStatusEn();
                            type = relevantDoc.getTypeNameEn();
                            department = relevantDoc.getDepartmentNameEn();
                        }
                        case "ar" -> {
                            name = relevantDoc.getNameAr();
                            status = relevantDoc.getStatusAr();
                            type = relevantDoc.getTypeNameAr();
                            department = relevantDoc.getDepartmentNameAr();
                        }
                    }
                }

                if (name == null || name.isEmpty()) name = relevantDoc.getName();
                if (status == null || status.isEmpty()) status = relevantDoc.getStatusName();
                if (type == null || type.isEmpty()) type = relevantDoc.getTypeName();

                item.setName(name);


                if (relevantDoc.getStatusId() != null) {
                    item.setStatus(new SelectItem(relevantDoc.getStatusId(), status));
                }

                if (relevantDoc.getTypeId() != null) {
                    item.setType(new SelectItem(relevantDoc.getTypeId(), type));
                }

                if (relevantDoc.getLocationId() != null) {
                    item.setLocation(new SelectItem(relevantDoc.getLocationId(), relevantDoc.getLocationName()));
                }
                if (relevantDoc.getDepartmentId() != null) {
                    if (department == null || department.isEmpty()) {
                        department = relevantDoc.getDepartmentName();
                    }
                    item.setDepartment(new SelectItem(relevantDoc.getDepartmentId(), department));
                }

                if (relevantDoc.getVacantCount() != null) {
                    item.setHeadCount(relevantDoc.getVacantCount());
                }

                item.setCreatedBy(relevantDoc.getCreatedByName());
                item.setCreatedDate(relevantDoc.getCreatedDate());
                item.setModifiedBy(relevantDoc.getModifiedByName());
                item.setModifiedDate(relevantDoc.getModifiedDate());

                if (fp.getListPanelTool() != null) {
                    item.setCustomFieldValuesItems(CustomFieldsUtils.getBaseSolrDocDynamicFields(relevantDoc, fp.getListPanelTool().getColumnCodeName()));
                }
                items.add(item);
            }
        }
        return new ListResult<>(items, totalCount);
    }

    public void deleteGoal(final Integer objectId, final String type) {
        final EdsGoal goal = this.goalManager.get(objectId);
        this.goalManager.deleteGoal(goal);
        this.goalAssigneesManager.deleteGoalAssignees(goal.getObjectID());
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(objectId);
        if (Constants.PERSONAL_GOAL.equals(type)) {
            kpiLog.setEntityType(EdsGoal.PERSONAL_GOAL);
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Delete personal goal");
        } else if (Constants.DEPARTMENT_GOAL.equals(type)) {
            employeeMetricHistoryManager.deleteEmployeeMetricHistoriesByDepartmentGaolId(goal.getObjectID());
            goalAssigneesManager.deleteGoalAssignees(goal.getObjectID());
            kpiLog.setEntityType(EdsGoal.DEPARTMENT_GOAL);
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Delete department goal");
        } else if (Constants.PROJECT_GOAL.equals(type)) {
            kpiLog.setEntityType(EdsGoal.PROJECT_GOAL);
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Delete project goal");
        } else if (Constants.BUSINESS_GOAL.equals(type)) {
            kpiLog.setEntityType(EdsGoal.BUSINESS_GOAL);
            ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Delete business goal");
        }
    }

    public void deleteGroupGoals(final Integer groupId) {
        final EdsGroupGoal edsGroupGoal = this.groupGoalManager.get(groupId);
        if (edsGroupGoal.getGoals() != null) {
            for (final EdsGoal goal : edsGroupGoal.getGoals()) {
                this.deleteGoal(goal.getObjectID(), Constants.PERSONAL_GOAL);
            }
        }
        edsGroupGoal.setDeleted(true);
        this.groupGoalManager.createOrUpdate(edsGroupGoal);
    }

    public void deleteCompanyGoal(final Integer objectId) {
        final EdsBusinessGoal companyGoal = this.businessGoalManager.get(objectId);
        this.businessGoalManager.deleteCompanyGoal(companyGoal.getObjectID());
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoal.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(objectId);
        kpiLog.setEntityType(Constants.COMPANY_GOAL);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Delete Company goal");
    }

    @Override
    public void saveGoalEditCellValue(final HrmsAPIItem apiItem) {
        try {
            final EdsGoal goal = this.goalManager.get(apiItem.getGoalItem().getObjectId());
            EdsGoalCustomFields goalCustomFields = goal.getGoalCustomFields();
            if (goalCustomFields == null) {
                goalCustomFields = new EdsGoalCustomFields();
                this.goalCustomFieldsManager.create(goalCustomFields);
                goal.setGoalCustomFields(goalCustomFields);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(goalCustomFields, apiItem.getGoalItem().getCustomFieldsMap(), apiItem.getColumnCodeName());
        } catch (final Exception e) {
            System.out.println("Goal List Edit Cell Column Code :" + apiItem.getColumnCodeName());
        }
    }

    @Override
    public void saveCompanyGoalEditCellValue(final HrmsAPIItem apiItem) {
        try {
            final EdsBusinessGoal goal = this.businessGoalManager.get(apiItem.getGoalItem().getObjectId());
            EdsGoalCustomFields goalCustomFields = goal.getGoalCustomFields();
            if (goalCustomFields == null) {
                goalCustomFields = new EdsGoalCustomFields();
                this.goalCustomFieldsManager.create(goalCustomFields);
                goal.setGoalCustomFields(goalCustomFields);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(goalCustomFields, apiItem.getGoalItem().getCustomFieldsMap(), apiItem.getColumnCodeName());
        } catch (final Exception e) {
            System.out.println("Goal List Edit Cell Column Code :" + apiItem.getColumnCodeName());
        }
    }

    @Override
    public void saveShiftItem(ShiftItem item) {
        NumberData numberData = item.getNumberData();
        EdsShift shift = item.getId() != null ? shiftManager.get(item.getId()) : new EdsShift();
        shift.setShiftName(item.getShiftName());
        shift.setPeriod(item.getPeriod());
        shift.setManager(item.getManager());
        shift.setBackupManager(item.getBackupManager());
        shift.setCustomFields(createShiftCustomFields(shift.getCustomFields(), item.getCustomFieldItems()));
        shift.setLookupType(item.getLookUpType());
        shift.setPeriodType(item.getPeriodType());
        shift.setOwnersId(item.getOwnersId());
        shift.setDepartment(item.getDepartment() != null ? departmentManager.get(item.getDepartment().getId()) : null);
        shift.setEndDate(item.getEndDate() != null ? item.getEndDate().getDate() : null);
        EdsBrigada edsBrigada1 = brigadaManager.get(item.getBirgada() != null ? item.getBirgada().getId() : null);
        shift.setBrigada(edsBrigada1);
        EdsUser user = userManager.getUser();
        if (item.getId() == null) {
            shift.setCreator(user);
            shift.setCreatedDate(new Date());
            shift.setUpdater(user);
            shift.setUpdatedDate(new Date());
        } else {
            shift.setUpdater(user);
            shift.setUpdatedDate(new Date());
        }

        NumberData newNumberData = numberData;
        if (shiftManager.isShiftNumberExist(item.getId() == null ? numberData.getNumberString() : shift.getShiftCode(), item.getId())) {
            newNumberData = this.generateShiftCode();
        }

        if (item.getNumberData() != null && !item.getNumberData().getNumberString().equals("")) {
            shift.setIntNumber(newNumberData.getIntNumber());
            shift.setShiftCode(newNumberData.getNumberString());
        }
        if (item.getOwnersId() != null && !item.getOwnersId().isEmpty()) {
            shift.setOwners(userManager.getByIDs(item.getOwnersId()));
        }

        shiftManager.createOrUpdate(shift);

        Map<Integer, Map<String, Integer>> dataMap = new HashMap<>();
        if (item.getId() != null) {
            ArrayList<EdsShiftItem> shiftItemsByShiftId = shiftItemManager.getShiftItemsByShiftId(item.getId());
            ArrayList<EdsShiftTeams> shiftTeamsByShiftId = shiftTeamsManager.getShiftTeamsByShiftId(item.getId());
            for (EdsShiftItem shiftItem : shiftItemsByShiftId) {
                if (shiftItem.getEmployeeAssessment() != null) {
                    Integer groupId = shiftItem.getGroupId();
                    String key = shiftItem.getKey();
                    Integer assessmentId = shiftItem.getEmployeeAssessment().getObjectID();
                    dataMap.computeIfAbsent(groupId, g -> new HashMap<>())
                            .put(key, assessmentId);
                }
                shiftItemManager.deleteShiftItem(shiftItem);
            }
            for (EdsShiftTeams edsShiftTeams : shiftTeamsByShiftId) {
                shiftTeamsManager.deleteShiftTeam(edsShiftTeams);
            }
        }
        LinkedHashMap<Integer, EdsShiftSettings> timeslotMap = new LinkedHashMap<>();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLookUp(true);
        shiftSettingsManager.getShiftSettings(fp).forEach(t -> timeslotMap.put(t.getObjectID(), t));

        HashMap<Integer, EdsBrigada> brigadasMap = new HashMap<>();
        item.getShiftTeams().forEach((k, v) -> {
            for (ShiftTeamsItem shiftTeam : v) {
                EdsEmployee employee = employeeManager.getEmployeeByNumber(shiftTeam.getEmployeeCode());
                EdsShiftTeams edsShiftTeams = new EdsShiftTeams();
                edsShiftTeams.setShift(shift);
                edsShiftTeams.setTeam(shiftTeam.getTeam() == null ? null : shiftTeam.getTeam().getName());
                edsShiftTeams.setFullname(employee.getName());
                edsShiftTeams.setDepartment(shiftTeam.getDepartment());
                edsShiftTeams.setEmployeeCode(shiftTeam.getEmployeeCode());
                edsShiftTeams.setLabel(shiftTeam.getLabel());
                edsShiftTeams.setPosition(shiftTeam.getPosition());
                edsShiftTeams.setTeamId(shiftTeam.getTeam() != null ? shiftTeam.getTeam().getId() : null);
                edsShiftTeams.setEmpId(employee.getObjectID());
                if (item.getLookUpType() != null && item.getLookUpType().equals(BRIGADA_ID)) {
                    if (brigadasMap.get(shiftTeam.getTeam().getId()) != null) {
                        EdsBrigada edsBrigada = brigadasMap.get(shiftTeam.getTeam().getId());
                        if (edsBrigada.getManager().getObjectID().equals(employee.getObjectID())) {
                            edsShiftTeams.setAdditionalPosition("Manager");
                        } else if (edsBrigada.getBackupManagerIDs().contains(employee.getObjectID())) {
                            edsShiftTeams.setAdditionalPosition("BackupManager");
                        } else {
                            edsShiftTeams.setAdditionalPosition("Employee");
                        }

                    } else {
                        EdsBrigada edsBrigada = brigadaManager.get(shiftTeam.getTeam().getId());
                        brigadasMap.put(shiftTeam.getTeam().getId(), edsBrigada);
                        if (edsBrigada.getManager().getObjectID().equals(employee.getObjectID())) {
                            edsShiftTeams.setAdditionalPosition("Manager");
                        } else if (edsBrigada.getBackupManagerIDs().contains(employee.getObjectID())) {
                            edsShiftTeams.setAdditionalPosition("BackupManager");
                        } else {
                            edsShiftTeams.setAdditionalPosition("Employee");
                        }
                    }
                }
                shiftTeamsManager.create(edsShiftTeams);
            }
        });


        ArrayList<ShiftItems> shiftItems = item.getShiftItems();
        if ("week".equals(item.getPeriodType())) {
            SimpleDateFormat weekSdf = new SimpleDateFormat("yyyy-MM-dd");
            int year = shift.getPeriod().getYear() + 1900;
            for (int i = 0; i < shiftItems.size(); i++) {
                SelectItem selectedGroup = shiftItems.get(i).getSelectedGroup();
                Integer rowId = shiftItems.get(i).getRowId();
                for (Map.Entry<String, SelectItem> entry : shiftItems.get(i).getDayAndSelectedTimeSlotS().entrySet()) {
                    int weekNum = Integer.parseInt(entry.getKey());
                    Date weekStart = getFirstDayOfISOWeekServer(year, weekNum);
                    EdsShiftSettings timeSlot = entry.getValue() != null ? timeslotMap.get(entry.getValue().getId()) : null;
                    Set<Integer> includedDaySet = null;
                    if (timeSlot != null && timeSlot.getIncludedDays() != null && !timeSlot.getIncludedDays().isEmpty()) {
                        includedDaySet = Arrays.stream(timeSlot.getIncludedDays().split(","))
                                .map(String::trim)
                                .map(Integer::parseInt)
                                .collect(Collectors.toSet());
                    }
                    for (int d = 0; d < 7; d++) {
                        if (includedDaySet != null && !includedDaySet.contains(d)) continue;
                        Calendar dayCal = Calendar.getInstance();
                        dayCal.setTime(weekStart);
                        dayCal.add(Calendar.DAY_OF_YEAR, d);
                        String dateKey = weekSdf.format(dayCal.getTime());
                        EdsShiftItem shiftItem = new EdsShiftItem();
                        shiftItem.setShift(shift);
                        shiftItem.setKey(dateKey);
                        shiftItem.setRow(rowId);
                        shiftItem.setTimeSlot(timeSlot);
                        shiftItem.setGroupId(selectedGroup.getId());
                        shiftItemManager.createOrUpdate(shiftItem);
                    }
                }
            }
        } else if (shift.getEndDate() == null) {
            for (int i = 0; i < shiftItems.size(); i++) {
                SelectItem selectedGroup = shiftItems.get(i).getSelectedGroup();
                Integer rowId = shiftItems.get(i).getRowId();
                Map<String, Integer> assesments = dataMap.get(selectedGroup.getId());
                shiftItems.get(i).getDayAndSelectedTimeSlotS().forEach((k, v) -> {
                    EdsShiftItem shiftItem = new EdsShiftItem();
                    shiftItem.setShift(shift);
                    shiftItem.setKey(k);
                    shiftItem.setRow(rowId);
                    if (item.getId() != null && assesments != null && assesments.get(k) != null) {
                        EdsEmployeeAssessment edsEmployeeAssessment = employeeAssessmentManager.get(assesments.get(k));
                        if (v != null && timeslotMap.containsKey(v.getId())) {
                            shiftItem.setEmployeeAssessment(edsEmployeeAssessment);
                        } else {
                            employeeAssessmentManager.delete(edsEmployeeAssessment);
                        }
                    }
                    shiftItem.setTimeSlot(v != null ? timeslotMap.get(v.getId()) : null);
                    shiftItem.setGroupId(selectedGroup.getId());
                    shiftItemManager.createOrUpdate(shiftItem);
                });
            }
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < shiftItems.size(); i++) {
                Date tempDate = new Date(shift.getPeriod().getTime());
                Date endDate = new Date(shift.getEndDate().getTime());
                SelectItem selectedGroup = shiftItems.get(i).getSelectedGroup();
                Integer rowId = shiftItems.get(i).getRowId();
                Map<String, Integer> assesments = dataMap.get(selectedGroup.getId());
                HashMap<String, SelectItem> dayAndSelectedTimeSlotS = shiftItems.get(i).getDayAndSelectedTimeSlotS();
                while (!tempDate.after(endDate)) {
                    SelectItem selectItem = dayAndSelectedTimeSlotS.get(simpleDateFormat.format(tempDate));
                    EdsShiftItem shiftItem = new EdsShiftItem();
                    shiftItem.setShift(shift);
                    shiftItem.setKey(simpleDateFormat.format(tempDate));
                    shiftItem.setRow(rowId);
                    shiftItem.setTimeSlot(selectItem != null ? timeslotMap.get(selectItem.getId()) : null);
                    shiftItem.setGroupId(selectedGroup.getId());
                    shiftItemManager.createOrUpdate(shiftItem);
                    tempDate.setDate(tempDate.getDate() + 1);
                }
            }
        }
        boolean statusChanged = shift.getOverallStatus() != null && !item.getStatusCode().equals(shift.getOverallStatus().getCode());

        if (!isOk(item.getApprovers())) {
            shift.setEntityStatus(referenceManager.findReference(Constants.SHIFT_STATUS, item.getStatusCode()));
        }

        if (isOk(item.getApprovers())) {
            item.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : item.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (shift.getCurrentApprover() != null && item.getStatusCode() != null && isFirstApprover) {
                        shift.getCurrentApprover().setStatus(referenceManager.findReference(Constants.SHIFT_STATUS, item.getStatusCode()));
                        shift.setEntityStatus(referenceManager.findReference(Constants.SHIFT_STATUS, Constants.SHIFT_SUBMITTED));
                        isFirstApprover = false;
                    } else if (shift.getCurrentApprover() != null && item.getStatusCode() != null) {
                        shift.getCurrentApprover().setStatus(referenceManager.findReference(Constants.SHIFT_STATUS, Constants.SHIFT_SUBMITTED));
                    }
                    if (item.getStatusCode() != null && !SHIFT_APPROVED.equals(item.getStatusCode())) {
                        shift.setEntityStatus(referenceManager.findReference(Constants.SHIFT_STATUS, item.getStatusCode()));
                    }
                    if (shift.isCurrentApproverRejected()) {
                        shift.setEntityStatus(shift.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(shift.getObjectID());
                edsApprover.setIs_default(false);

                if (item.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.SHIFT_STATUS, item.getStatusCode()));
                    if (Constants.SHIFT_DRAFT.equals(item.getStatusCode())) {
                        shift.setEntityStatus(referenceManager.findReference(Constants.SHIFT_STATUS, item.getStatusCode()));
                    } else {
                        shift.setEntityStatus(referenceManager.findReference(Constants.SHIFT_STATUS, SHIFT_SUBMITTED));
                    }
                    isFirstApprover = false;
                } else if (item.getStatusCode() != null) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.SHIFT_STATUS, SHIFT_SUBMITTED));
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

                if (shift.getCurrentApprover() == null) {
                    shift.setCurrentApprover(edsApprover);
                }
                shift.getApprovers().add(edsApprover);
            }
        }

        if (item.getId() == null) {
            baseEventsPostProcessor.registerEvent(ShiftEventListenerImpl.TYPE, MyUpdateItem.ADD, shift, user);
            if (item.getStatusCode().equals(Constants.SHIFT_DRAFT)) {
                baseEventsPostProcessor.registerEvent(ShiftEventListenerImpl.TYPE, ShiftEventListenerImpl.SHIFT_DRAFT, shift, user);
            }
        } else if (!Constants.SHIFT_APPROVED.equals(item.getStatusCode()) && !Constants.SHIFT_DRAFT.equals(item.getStatusCode())) {
            baseEventsPostProcessor.registerEvent(ShiftEventListenerImpl.TYPE, MyUpdateItem.EDIT, shift, user);
        }

        if (item.getStatusCode().equals(Constants.SHIFT_APPROVED)) {
            baseEventsPostProcessor.registerEvent(ShiftEventListenerImpl.TYPE, ShiftEventListenerImpl.SHIFT_APPROVED, shift, user);
        }

        if (statusChanged) {
            baseEventsPostProcessor.registerEvent(ShiftEventListenerImpl.TYPE, item.getStatusCode(), shift, user);
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), shift, user);
        workflowEvent.setEntityType(RelationItem.TYPE_SHIFT);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsShift.class.getSimpleName());
        if (item.getId() != null) {
            kpiLog.setEntityId(item.getId());
        }
        if (item.getId() == null) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add shift");
            EdsBusinessEvent workflowEventAdd = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, shift, user);
            workflowEventAdd.setEntityType(RelationItem.TYPE_SHIFT);
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(log, kpiLog, "Update shift");
            EdsBusinessEvent workflowEventEdit = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, shift, user);
            workflowEventEdit.setEntityType(RelationItem.TYPE_SHIFT);
        }

        if (SHIFT_SUBMITTED.equals(item.getStatusCode())) {
            boolean hasAlerts = false;
            List<EdsWorkflowRule> rules = workflowRuleManager.getByModuleAndActions(WorkflowRule._WORKFLOW_MODULE_SHIFT, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT);
            if (rules != null && rules.size() > 0) {
                for (EdsWorkflowRule rule : rules) {
                    hasAlerts = workflowAlertManager.hasAlertsByRoleID(rule.getObjectID());
                    if (hasAlerts) {
                        break;
                    }
                }
            }

            if (!hasAlerts) {
                try {
                    messageManager.sendShiftToApprover(shift);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (statusChanged || item.getStatusCode().equals(SHIFT_SUBMITTED)) {
                baseEventPostProcessor.registerEvent(ShiftEventListenerImpl.TYPE, ShiftEventListenerImpl.SHIFT_SUBMITTED, shift, user);
            }

        }
    }


    private static Date getFirstDayOfISOWeekServer(int year, int weekNumber) {
        Calendar cal = Calendar.getInstance();
        cal.setMinimalDaysInFirstWeek(4);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.WEEK_OF_YEAR, weekNumber);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private static int getISOWeekNumberServer(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setMinimalDaysInFirstWeek(4);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    private EdsShiftCustomFields createShiftCustomFields(EdsShiftCustomFields customFields, ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (customFields == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                customFields = new EdsShiftCustomFields();
                shiftCustomFieldsManager.create(customFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customFields, customFieldItems);
            return customFields;
        }
        return null;
    }

    public void deleteDependent(final Integer objectId) {
        final EdsDependent dependent = this.dependentManager.get(objectId);
        dependent.setDeleted(true);
        this.dependentManager.update(dependent);
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsDependent.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(objectId);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Delete dependent");
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployeesWithTeams(final Integer timeslotID) {
        final List<EdsEmployee> employees = this.employeeManager.getEmployeesForPayroll(this.employeeManager.getUser().getCompany());
        final LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneeList = new LinkedHashMap<>();

        KpiTreeInfo sItem;
        EdsDepartment department;
        boolean team;
        for (final EdsEmployee employee : employees) {
            team = false;
            sItem = new KpiTreeInfo();
            sItem.setId(employee.getObjectID());
            sItem.setName(employee.getName());
            if (employee.getTimeSlot() != null) {
                if (employee.getTimeSlot().getObjectID() != null && employee.getTimeSlot().getObjectID().equals(timeslotID)) {
                    sItem.setSelected(true);
                }
            }

            department = employee.getTeam();
            if (department != null) {
                sItem.setDepartmentId(department.getObjectID());
                sItem.setDepartmentName(department.getName());
                for (final KpiTreeInfo s : assigneeList.keySet()) {
                    if (s.getId().equals(employee.getTeam().getObjectID())) {
                        team = true;
                        assigneeList.get(s).add(sItem);
                        break;
                    }
                }

                if (!team) {
                    final KpiTreeInfo departmentInfo = new KpiTreeInfo(employee.getTeam().getObjectID(), employee.getTeam().getName());
                    final ArrayList<KpiTreeInfo> list = new ArrayList<>();
                    list.add(sItem);
                    assigneeList.put(departmentInfo, list);
                }
            }
        }
        return assigneeList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployeesByTeamsList() {
        return this.commonService.getCompanyEmployeesWithTeams(null, true);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewsComment[] getGoalNoteComments(final Integer noteID) {
        return this.commonService.getNotecomments(noteID);
    }

    public HistoryListItem[] getGoalNotes(final Integer goalID, final String goalTypeName) {
        final HistoryListItem[] goalNotes;
        Integer relatedId = null;
        final ListingFilterParameter filterParametrs = new ListingFilterParameter();
        switch (goalTypeName) {
            case Constants.PERSONAL_GOAL -> relatedId = EdsNoteHistory.PERSONAL_GOAL;
            case Constants.DEPARTMENT_GOAL -> relatedId = EdsNoteHistory.DEPARTMENT_GOAL;
            case Constants.PROJECT_GOAL -> relatedId = EdsNoteHistory.PROJECT_GOAL;
            case Constants.BUSINESS_GOAL -> relatedId = EdsNoteHistory.BUSINESS_GOAL;
            case Constants.COMPANY_GOAL -> relatedId = EdsNoteHistory.COMPANY_GOAL;
            case Constants.VACANCY -> relatedId = EdsNoteHistory.VACANCY;
        }

        final EdsNoteHistory[] goalNote = this.noteHistoryManager.getNoteList(filterParametrs).toArray(new EdsNoteHistory[]{});

        final List<EdsNoteHistory> histrItems = new LinkedList<>();
        if (goalID != null) {
            for (final EdsNoteHistory noteHistr : goalNote) {
                if ((relatedId != null && relatedId == noteHistr.getRelatedTo() && noteHistr.getRelatedId() != null) && (noteHistr.getRelatedId().intValue() == goalID)) {
                    histrItems.add(noteHistr);
                }
            }
        }
        final EdsUser user = this.employeeManager.getUser();
        goalNotes = new HistoryListItem[histrItems.size()];
        for (int i = 0; i < histrItems.size(); i++) {
            final EdsNoteHistory notes = histrItems.get(i);
            final HistoryListItem items = new HistoryListItem();
            items.setObjectID(notes.getObjectID());
            items.setEmployee(notes.getEmployee().getName());
            items.setSubject(notes.getSubject());
            items.setComment(notes.getComment());
            items.setVisibility(notes.isVisibility());
            items.setEventDate(notes.getEventDate() != null ? new Date(notes.getEventDate().getTime()) : null);
            items.setEditable(user.equals(notes.getEmployee()));
            final NewsComment[] noteComments = this.getGoalNoteComments(notes.getObjectID());
            if (noteComments.length > 0) {
                items.setNotesComments(noteComments);
            } else {
                items.setNotesComments(new NewsComment[0]);
            }
            goalNotes[i] = items;
        }
        return goalNotes;
    }

    public NewsComment saveGoalNoteComments(final NewsComment data) {
        return this.commonService.saveNoteComment(data);
    }

    @Override
    public LinkedHashMap<OnboardingItem, ArrayList<OnboardingItem>> getOnboardingData(Integer userId) {
        final LinkedHashMap<OnboardingItem, ArrayList<OnboardingItem>> onboardingItems = new LinkedHashMap<>();

        if (userId == null) {
            userId = this.employeeManager.getUser().getObjectID();
        }

        final List<EdsOnboardingPeriod> onboardingPeriods = this.onboardingPeriodManager.getOnboardingPeriodListOrderByRelativeStart();

        final List<EdsStepEmployee> statuses = this.stepEmployeeManager.getEmployeeStepsByEmployeeId(userId);
        final HashMap<Integer, Boolean> statusesList = new HashMap<>();

        for (final EdsStepEmployee status : statuses) {
            statusesList.put(status.getOnboardingStep().getObjectID(), status.getDone());

        }
        for (final EdsOnboardingPeriod onboardingPeriod : onboardingPeriods) {
            final List<EdsOnboardingStep> onboardingSteps = this.onboardingStepManager.getOnboardingStepListByPeriod(onboardingPeriod.getObjectID());
            final ArrayList<OnboardingItem> steps = new ArrayList<>();
            for (final EdsOnboardingStep onboardingStep : onboardingSteps) {
                final OnboardingItem step = onboardingStep.getRPC();
                final EmployeeStepItem employeeStepItem = new EmployeeStepItem();
                final EdsEmployee employee = this.employeeManager.get(userId);
                if (employee != null) {
                    final EdsStepEmployee stepEmployee = this.stepEmployeeManager.getEmployeeStepByEmployeeIdAndStepId(employee.getObjectID(), step.getStepId());
                    if (stepEmployee != null) {
                        step.setAssignedEmployee(stepEmployee.getRPC(employeeStepItem));
                    }
                    step.setStepStatus(statusesList.get(onboardingStep.getObjectID()));
                    step.setRoles(this.getRoleAsHashMap(onboardingStep.getRoles()));
                    if (onboardingStep.getStatus() != null) {
                        final ArrayList<ReferenceItem> referenceItems = new ArrayList<>();
                        final List<EdsReference> statusList = this.referenceManager.listReferences(onboardingStep.getStatus().getCode());
                        for (final EdsReference status : statusList) {
                            referenceItems.add(status.getRPC());
                        }
                        step.setStatusItems(referenceItems);
                    }
                }
                steps.add(step);
            }

            onboardingItems.put(onboardingPeriod.getRPC(), steps);
        }
        final List<EdsOnboardingStep> onboardingStepsWithoutPeriod = this.onboardingStepManager.getOnboardingStepListWithoutPeriodId();
        if (onboardingStepsWithoutPeriod != null && onboardingStepsWithoutPeriod.size() > 0) {
            final ArrayList<OnboardingItem> stepsWithPeriod = new ArrayList<>();
            for (final EdsOnboardingStep onStepItem : onboardingStepsWithoutPeriod) {
                final OnboardingItem step = onStepItem.getRPC();
                final EmployeeStepItem employeeStepItem = new EmployeeStepItem();
                final EdsEmployee employee = this.employeeManager.get(userId);
                if (employee != null) {
                    final EdsStepEmployee stepEmployee = this.stepEmployeeManager.getEmployeeStepByEmployeeIdAndStepId(employee.getObjectID(), step.getStepId());
                    if (stepEmployee != null) {
                        step.setAssignedEmployee(stepEmployee.getRPC(employeeStepItem));
                    }
                    step.setStepStatus(statusesList.get(onStepItem.getObjectID()));
                    step.setRoles(this.getRoleAsHashMap(onStepItem.getRoles()));
                    if (onStepItem.getStatus() != null) {
                        final ArrayList<ReferenceItem> referenceItems = new ArrayList<>();
                        final List<EdsReference> statusList = this.referenceManager.listReferences(onStepItem.getStatus().getCode());
                        for (final EdsReference status : statusList) {
                            referenceItems.add(status.getRPC());
                        }
                        step.setStatusItems(referenceItems);
                    }
                }

                stepsWithPeriod.add(step);
            }
            final OnboardingItem item = new OnboardingItem();
            onboardingItems.put(item, stepsWithPeriod);
        }

        return onboardingItems;

    }

    @Override
    public void saveOnboardingData(final OnboardingItem stepItem) {

        EdsEmployee employee = new EdsEmployee();
        if (stepItem.getEmployeeId() == null) {
            employee = (EdsEmployee) this.employeeManager.getUser();
        } else {
            employee = this.employeeManager.get(stepItem.getEmployeeId());
        }

        EdsStepEmployee stepEmployee = this.stepEmployeeManager.getEmployeeStepByEmployeeIdAndStepId(employee.getObjectID(), stepItem.getStepId());

        if (stepEmployee == null) {
            stepEmployee = new EdsStepEmployee();
        }
        if (stepItem.getAssignedEmployee().getStatusID() != null) {
            stepEmployee.setEntityStatus(this.referenceManager.get(stepItem.getAssignedEmployee().getStatusID()));
        }
        final EdsOnboardingStep step = this.onboardingStepManager.get(stepItem.getStepId());
        stepEmployee.setEmployee(employee);
        stepEmployee.setOnboardingStep(step);

        stepEmployee.setDone(stepItem.getStepStatus());
        this.stepEmployeeManager.createOrUpdate(stepEmployee);
    }

    /**
     * Related performance note (incident) item
     *
     * @param int_objectID - performance note (incident) ID
     * @return performance note (incident) item
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PerformanceNoteItem getPerformanceNote(final Integer int_objectID) {
        final EdsUser currentUser = this.userManager.getUser();

        PerformanceNoteItem item = new PerformanceNoteItem();
        if (int_objectID != null) {
            final EdsPerformanceNote performanceNote = this.performanceNoteManager.get(int_objectID);
            if (performanceNote != null && !performanceNote.getDeleted()) {
                item = performanceNote.getRPC();
            }
        }
        final Integer currentUserObjectID = currentUser.getObjectID();
        item.setCurrentUserID(currentUserObjectID);
        final boolean isResolver = currentUserObjectID.equals(item.getRelatedToID());
        item.setRelatedToEmployees(this.getDepartmentEmployees());
        item.setStatuses(ServerUtils.getAsSelectItem(this.referenceManager.getIssueStatuses(isResolver), ServerUtils.REFERENCE));
        item.setPriorities(this.referenceManager.getAsSelectItems(EdsPerformanceNote.PERFORMANCE_NOTE_PRIORITIES));
        item.setReportedByItems(this.getDepartmentEmployees());

        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPerformanceNote.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "get Performance Note");

        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getDepartmentEmployees() {
        final EdsEmployee user = (EdsEmployee) this.employeeManager.getUser();
        final SelectItem[] leaderEmployees;
        final ListingFilterParameter fpE = new ListingFilterParameter();
        if (user.hasRole(this.roleManager.get(EdsRole.DR)) || user.hasRole(this.roleManager.get(EdsRole.ADMIN)) || user.hasRole(this.roleManager.get(EdsRole.HR))) {
            fpE.setViewAsId(EdsRole.DR);
        } else if (user.hasRole(this.roleManager.get(EdsRole.TL))) {
            fpE.setDepartmentId(user.getTeam().getObjectID());
        } else if (user.hasRole(this.roleManager.get(EdsRole.PM))) {
            fpE.setViewAsId(EdsRole.PM);
        } else if (user.hasRole(this.roleManager.get(EdsRole.ADMIN_LOCATION))) {
            fpE.setLocationId(user.getLocation().getObjectID());       //
        } else if (user.hasRole(this.roleManager.get(EdsRole.MEM))) {
            leaderEmployees = new SelectItem[]{new SelectItem(user.getObjectID(), user.getName())};
            return leaderEmployees;
        } else {
            fpE.setDepartmentId(user.getTeam().getObjectID());
        }
        fpE.setResignedEmployeesIncluded(false);
        final List<EdsEmployee> employees = this.employeeManager.list(fpE);
        leaderEmployees = new SelectItem[employees.size()];
        int i = 0;
        for (final EdsEmployee employee : employees) {
            leaderEmployees[i] = new SelectItem(employee.getObjectID(), employee.getName());
            i++;
        }
        return leaderEmployees;
    }

    public ListResult<PerformanceNoteItem> getPerformanceNoteList(final ListingFilterParameter fp) {

        List<EdsPerformanceNote> performanceNotes = this.performanceNoteManager.getList(fp);
        final int totalCount = performanceNotes.size();
        if (fp.getLimit() > 0) {
            performanceNotes = ListUtils.getSublist(performanceNotes, fp.getStart(), fp.getLimit());
        }
        final ArrayList<PerformanceNoteItem> result = new ArrayList<>();

        for (final EdsPerformanceNote performanceNote : performanceNotes) {
            final PerformanceNoteItem item = performanceNote.getRPC();
            if (performanceNote.getStatus() != null) {
                item.setStatusName(this.referenceWfmMessageSource.localizeRef(performanceNote.getStatus()));
            }
            result.add(item);
        }

        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPerformanceNote.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "get Performance Note list");

        return new ListResult<>(result, totalCount);

    }

    @Override
    public SelectItem[] getGoalLookUpItems(final ListingFilterParameter fp) {
        if (Constants.COMPANY_GOAL.equals(fp.getViewType())) {
            return this.businessGoalManager.getListAsSelectItems(fp);
        } else {
            return this.goalManager.getListAsSelectItems(fp);
        }
    }

    @Override
    @Transactional
    public void deletePosition(final Integer posId) {
        final EdsPosition position = this.positionManager.get(posId);
        position.setDeleted(true);
        this.positionManager.update(position);
        this.employeeManager.deletePositionEmployee(position);

        try {
            solrManager.removePosition(posId, SecurityContext.getCompanyID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPosition.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(posId);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Deleted position");
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, position, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_POSITION);
    }

    @Override
    public ListResult<CertificateItem> getCertificateList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (fp.getStartDateNC() != null) {
            fp.setStartDate(ServerUtils.parseFilterParameterDate(fp.getStartDateNC()));
        }
        if (fp.getEndDateNC() != null) {
            fp.setEndDate(ServerUtils.parseFilterParameterDate(fp.getEndDateNC()));
        }

        Page<CertificateSolrDoc> certificateSolrDocs = certificateSolrComponent.getList(fp);

        int totalCount = (int) certificateSolrDocs.getTotalElements();

        ArrayList<CertificateItem> resultList = new ArrayList<>();

        for (CertificateSolrDoc doc : certificateSolrDocs.getContent()) {
            CertificateItem item = new CertificateItem();
            EdsCertificateOfEmployment certificate = this.certificateOfEmploymentManager.get(doc.getCertificateId());
            item.setObjectId(doc.getCertificateId());
            if (item.getObjectId() != null && certificate != null) {
                item = certificate.createCertificateData();
            }
            item.setFormID(certificate != null ? certificate.getCertificateType().getFormID() : null);
            item.setCertificateNumber(new NumberData(doc.getNumber()));
            item.setUpdatedDate(doc.getIssuedDate());
            item.setUpdatedBy(SolrUtils.asSelectItem(doc.getIssuedById(), doc.getIssuedByName()));
            item.setCreationDate(doc.getCreatedDate());
            item.setCreatedBy(SolrUtils.asSelectItem(doc.getCreatedById(), doc.getCreatedByName()));
            item.setCertificateType(SolrUtils.asSelectItem(doc.getTypeId(), doc.getTypeName()));
            item.setEmployee(SolrUtils.asSelectItem(doc.getEmployeeId(), doc.getEmployeeName()));
            item.setEmployeeCode(doc.getEmployeeCode());
            item.setOverallStatus(SolrUtils.asReferenceItem(doc.getStatusId(), doc.getStatusName()));
            if (fp.getListPanelTool() != null) {
                item.setCustomFieldValuesItems(CustomFieldsUtils.getBaseSolrDocDynamicFields(doc, fp.getListPanelTool().getColumnCodeName()));
            }
            resultList.add(item);
        }

        return new ListResult<>(resultList, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getPositionFacetQuery(final ListingFilterParameter fp, final EdsUser user) {
        final StringBuffer sql = new StringBuffer();
        sql.append(SolrPositionRepresenter.FIELD_COMPANY_ID).append(":").append(user.getCompany().getObjectID());

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND ").append(SolrPositionRepresenter.FIELD_COMPOSITE).append(":( ").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey()));
            if (!fp.isLookUp()) {
                final SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(sql, getPositionSearchFields(), fp.getSearchKey());
            }
            sql.append(")");
        }
        return sql.toString();
    }

    @Override
    public CertificateItem getCertificateData(final Integer certificateId) {
        final CertificateItem item;
        final EdsCertificateOfEmployment certificate;
        if (certificateId != null) {
            certificate = this.certificateOfEmploymentManager.get(certificateId);
            item = certificate.createCertificateData();
            final ArrayList<FileResource> files = new ArrayList<>();
            final ArrayList<Integer> fileIds = new ArrayList<>();
            if (certificate.getAttachmentIDs() != null && !"".equals(certificate.getAttachmentIDs())) {
                for (final String id : certificate.getAttachmentIDs().split(",")) {
                    if (id == null)
                        continue;
                    final EdsFileBody body = (EdsFileBody) this.uploadManager.get(Integer.valueOf(id));
                    if (body == null)
                        continue;
                    final FileResource fr = new FileResource();
                    fr.setBodyId(body.getObjectID());
                    fr.setDescription(body.getDescription());
                    fr.setAmazonLink(this.commonService.getImageUrl(body.getObjectID()));
                    files.add(fr);
                    fileIds.add(fr.getBodyId());
                }
                item.setDucumentList(files);
                item.setDucumentIds(fileIds);
            }
            EdsCertificateOfEmploymentType certificateTemplate = null;
            if (certificate.getCertificateType() != null) {
                certificateTemplate = this.certificateOfEmploymentTypeManager.get(certificate.getCertificateType().getObjectID());
            }
            if (certificateTemplate != null && certificateTemplate.getCustomHTML() != null && !"".equals(certificateTemplate.getCustomHTML())) {
                item.setCustomHTMLcontent(this.replaceVelocity(certificateTemplate.getCustomHTML(), certificate.getEmployee().getObjectID(), files, certificateId));
            } else {
                item.setContent(this.replaceVelocity(certificate.getContentHTML(), certificate.getEmployee().getObjectID(), files, certificateId));
            }
            if (item.getFormID() != null) {
                item.setSetupApproval(this.approverManager.isExistApproverByEntityTypeAndStepType(item.getFormID(), RelationItem.TYPE_CERTIFICATE_OF_EMPLOYMENT));
            }
            if (certificate.getCurrentApprover() != null) {
                if (certificate.getCurrentApprover().getExactEmployee() != null && certificate.getCurrentApprover().getExactEmployee().getObjectID().equals(this.userManager.getUser().getObjectID())) {
                    item.setCanApprove(true);
                }
            }
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Certificates);
            item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(certificate.getCustomFields(), customFieldsItems));
        } else {
            item = new CertificateItem();
            item.setCertificateNumber(this.certificateOfEmploymentManager.getCertificateNumber());
        }
        final Integer currentUserId = this.userManager.getUser().getObjectID();
        final EdsEmployee edsEmployee = this.employeeManager.get(currentUserId);
        if (edsEmployee != null) {
            item.setCurrentUserID(edsEmployee.getObjectID());
            final String code = edsEmployee.getProfile() != null ? edsEmployee.getProfile().getEmployeeCode() : null;
            final String employeeName = (code != null && !"".equals(code) ? code + " - " : "") + edsEmployee.getName();
            item.setCurrentUserName(employeeName + this.referenceWfmMessageSource.localize("mySelf", " (" + Constants.MYSELF + ")"));
        }
        item.setTypes(this.getCertificateTypes());
        return item;
    }

    public void deleteCertificateComment(Integer commentID) {
        EdsCertificateOfEmployeeNote certificateComment = certificateOfEmploymentNoteManager.get(commentID);
        certificateOfEmploymentNoteManager.delete(certificateComment);
    }

    @Override
    public ListResult<ShiftItem> getShiftList(ListingFilterParameter fp) {
        if (fp == null) fp = new ListingFilterParameter();

        EdsUser user = userManager.getUser();
        if (ServerUtils.hasPermission(PermissionConstants.HRMS_SHIFT_SEE_OWN_BY_DEPARTMENT) && user.getEmployee().getTeam() != null) {
            fp.setDepartmentId(user.getEmployee().getTeam().getObjectID());
        }
        boolean seeAll = ServerUtils.hasPermission(HRMS_SHIFT_SEE_ALL);
        List<EdsShift> list = shiftManager.getList(fp, user.getObjectID(), seeAll);
        ArrayList<ShiftItem> shiftItemList = new ArrayList<>();

        if (list != null) {
            for (EdsShift edsShift : list) {
                ShiftItem item = edsShift.toRpc();
                shiftItemList.add(item);
            }
        }


        Integer totalCount = shiftManager.getTotalCount(fp, user.getObjectID(), seeAll);
        return new ListResult<>(shiftItemList, totalCount);
    }

    @Override
    public ListResult<RotationItem> getRotationList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        List<EdsRotation> list = rotationManager.getList(fp);
        ArrayList<RotationItem> rotationItems = new ArrayList<>();

        if (list != null) {
            for (EdsRotation edsRotation : list) {
                RotationItem item = edsRotation.toRpc();
                rotationItems.add(item);
            }
        }

        Integer totalCount = rotationManager.getTotalCount(fp);
        return new ListResult<>(rotationItems, totalCount);
    }

    @Override
    public ListResult<GroupPlacementItem> getGroupPlacementList(ListingFilterParameter fp) {
        if (fp == null) fp = new ListingFilterParameter();

        List<EdsGroupPlacement> list = groupPlacementManager.getList(fp);
        ArrayList<GroupPlacementItem> groupPlacementItems = new ArrayList<>();

        if (list != null) {
            for (EdsGroupPlacement edsGroupPlacement : list) {
                GroupPlacementItem item = edsGroupPlacement.toRpc();
                groupPlacementItems.add(item);
            }
        }

        Integer totalCount = groupPlacementManager.getTotalCount(fp);
        return new ListResult<>(groupPlacementItems, totalCount);
    }


    @Override
    public String getTeamsIdsForAttendanceLink(Integer shiftId, Integer lookUpType) {
        String teamsIds = null;
        if (lookUpType == null || lookUpType.equals(BRIGADA_ID)) {
            String brigadasId = ServerUtils.getAsCommoDelimited(shiftItemManager.getTeamsIdByShift(shiftId), "0", ",");
            List<Integer> brigadaEmployees = brigadaEmployeesManager.getBrigadaEmployees(brigadasId);
            teamsIds = ServerUtils.getAsCommoDelimited(brigadaEmployees, "0", ",");
        } else {
            teamsIds = ServerUtils.getAsCommoDelimited(shiftItemManager.getShiftItemsGroupId(shiftId), "0", ",");
        }

        return teamsIds;
    }

    @Override
    public ArrayList<String> getSelectedTeamsByPeriodAndIds(String period, HashMap<Integer, ArrayList<String>> periodMap, ArrayList<Integer> teamIds, Integer shiftId, Integer shiftType) {
        if (shiftType.equals(OVERTIME) || shiftType.equals(EMPLOYEE_ID)) {
            return brigadaManager.getSavedOvertimeForThisPeriod(teamIds, getOvertimePeriodCheckQuery(periodMap), period, shiftId, shiftType);
        }
        return brigadaManager.getSavedBrigadasForThisPeriod(teamIds, period, shiftId, shiftType);
    }


    @Override
    public ShiftItem getShiftItem(Integer objectId, boolean fromSummary) {
        EdsShift edsShift = shiftManager.get(objectId);
        if (edsShift != null) {
            LinkedHashMap<Integer, List<ShiftItems>> shiftItemsByGroupIdMap = shiftItemManager.getShiftItemsByGroupId(edsShift.getObjectID());
            ArrayList<ShiftItems> shiftItems = new ArrayList<>();
            Set<Integer> shiftItemsGroupId = shiftItemsByGroupIdMap.keySet();
            boolean isWeekShift = "week".equals(edsShift.getPeriodType());
            HashMap<Integer, EmployeeReport> employeeReportMap = new HashMap<>();
            HashMap<String, ReasonItem> leaveTypes = new HashMap<>();
            int maxDaysCount = 0;
            if (!isWeekShift) {
                ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
                listingFilterParameter.setStartDateNC(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(DateUtil.getMonthFirstDay(edsShift.getPeriod())));
                listingFilterParameter.setEndDateNC(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(DateUtil.getMonthLastDate(edsShift.getPeriod())));
                listingFilterParameter.setEmployeeIDs(shiftItemsGroupId.stream().map(String::valueOf).collect(Collectors.joining(",")));
                maxDaysCount = DateUtil.countDays(edsShift.getPeriod());
                listingFilterParameter.setDepartmentIds(edsShift.getDepartment() != null ? String.valueOf(edsShift.getDepartment().getObjectID()) : null);
                listingFilterParameter.setFromShift(true);
                EmployeeAttendanceReport employeeAttendanceReport = availabilityService.getEmployeeAttendanceReport(listingFilterParameter, maxDaysCount);
                for (EmployeeReport report : employeeAttendanceReport.getEmployeeReports()) {
                    employeeReportMap.put(report.getId(), report);
                }
                leaveTypes = employeeAttendanceReport.getLeaveTypes();
            }
            for (Integer groupId : shiftItemsGroupId) {
                HashMap<String, SelectItem> dayAndTimeSlot = new HashMap<>();
                HashMap<String, Integer> dayAndShiftItemId = new HashMap<>();
                HashMap<String, Double> rates = new HashMap<>();
                HashMap<String, Integer> assesments = new HashMap<>();
                HashMap<Integer, String> leaveDays = new HashMap<>();
                List<ShiftItems> shiftItemsByGroupId = shiftItemsByGroupIdMap.get(groupId);
                for (ShiftItems shiftItem : shiftItemsByGroupId) {
                    dayAndTimeSlot.put(shiftItem.getKey(), shiftItem.getTimeSlotId() != null ? new SelectItem(shiftItem.getTimeSlotId(), shiftItem.getTimeSlotShortName()) : null);
                    dayAndShiftItemId.put(shiftItem.getKey(), shiftItem.getId());
                    if (shiftItem.getAssessmentId() != null) {
                        EdsEmployeeAssessment edsEmployeeAssessment = employeeAssessmentManager.get(shiftItem.getAssessmentId());
                        List<EdsSkillRating> ratings = edsEmployeeAssessment.getSkillAssessment().getRatings();
                        double avgManagerRate = (ratings != null && !ratings.isEmpty())
                                ? ratings.stream()
                                .mapToInt(r -> {
                                    try {
                                        return Integer.parseInt(r.getManagerGrade());
                                    } catch (Exception e) {
                                        return 0;
                                    }
                                })
                                .average().orElse(0.0)
                                : 0.0;
                        rates.put(shiftItem.getKey(), avgManagerRate);
                        assesments.put(shiftItem.getKey(), shiftItem.getAssessmentId());
                    }
                }
                if (isWeekShift) {
                    HashMap<String, SelectItem> weekMap = new HashMap<>();
                    HashMap<String, Integer> weekIdMap = new HashMap<>();
                    SimpleDateFormat weekSdf = new SimpleDateFormat("yyyy-MM-dd");
                    for (Map.Entry<String, SelectItem> entry : dayAndTimeSlot.entrySet()) {
                        try {
                            Date parsedDate = weekSdf.parse(entry.getKey());
                            int weekNum = getISOWeekNumberServer(parsedDate);
                            weekMap.putIfAbsent("" + weekNum, entry.getValue());
                            weekIdMap.putIfAbsent("" + weekNum, dayAndShiftItemId.get(entry.getKey()));
                        } catch (ParseException e) {
                            // eski hafta-raqam formatidagi yozuvlar (legacy)
                            weekMap.putIfAbsent(entry.getKey(), entry.getValue());
                            weekIdMap.putIfAbsent(entry.getKey(), dayAndShiftItemId.get(entry.getKey()));
                        }
                    }
                    dayAndTimeSlot = weekMap;
                    dayAndShiftItemId = weekIdMap;
                }
                ShiftItems shiftItems1 = new ShiftItems();
                if (edsShift.getLookupType() == null || edsShift.getLookupType().equals(BRIGADA_ID)) {
                    EdsBrigada edsProject = brigadaManager.get(groupId);
                    String manager = edsProject.getManager() != null ? edsProject.getManager().getFullName() + " - " + edsProject.getName() + " - " + shiftItemsByGroupId.size() : null;
                    String backupManager = null;
                    shiftItems1.setSelectedGroup(new SelectItem(edsProject.getObjectID(), edsProject.getNumber() + " - " + edsProject.getName(), manager, backupManager));
                    if (edsProject.getBackupManagers() != null && !edsProject.getBackupManagers().isEmpty()) {
                        backupManager = edsProject.getName() + " - " + edsProject.getBackupManagers().stream().map(EdsEmployee::getFullName).collect(Collectors.joining(","));
                    }
                } else {
                    EdsEmployee employee = employeeManager.get(groupId);
                    shiftItems1.setSelectedGroup(new SelectItem(employee.getObjectID(), employee.getFormmattedName()));
                    shiftItems1.setPositionName(employee.getPosition() != null ? employee.getPosition().getName() : null);
                    if (!isWeekShift && employeeReportMap.containsKey(employee.getObjectID())) {
                        EmployeeReport report = employeeReportMap.get(employee.getObjectID());
                        int[] al = report.getAl();
                        for (int i = 1; i < maxDaysCount; i++) {
                            if (al[i] == 1 || al[i] == 2 || al[i] == 4 || al[i] == 3) {
                                leaveDays.put(i, leaveTypes.get(report.getLeaveCodes()[i]).getShortName());
                            }
                        }
                    }
                }
                shiftItems1.setRowId(1);
                shiftItems1.setDayAndSelectedTimeSlotS(dayAndTimeSlot);
                shiftItems1.setDayAndShiftItemId(dayAndShiftItemId);
                shiftItems1.setLeaveDays(leaveDays);
                shiftItems1.setRates(rates);
                shiftItems1.setAssesments(assesments);
                shiftItems.add(shiftItems1);
            }
            ShiftItem shiftItem = edsShift.toRpc();
            shiftItem.setShiftItems(shiftItems);
            shiftItem.setManager(edsShift.getManager() != null ? edsShift.getManager().replace("-:-", "<br>") : null);
            shiftItem.setBackupManager(edsShift.getBackupManager() != null ? edsShift.getBackupManager().replace("-:-", "<br>") : null);
            shiftItem.setShiftTeams(getShiftTeamsData(objectId, shiftItemsGroupId, edsShift.getLookupType(), shiftItem.getPeriod(), fromSummary));
            shiftItem.setTemplates(getShiftPdfTemplates(PdfReferenceCodeNameEnum.SHIFT.name()).getItems());
            shiftItem.setGroupsId(fromSummary ? getTeamsIdsForAttendanceLink(edsShift.getObjectID(), edsShift.getLookupType()) : null);
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.ShiftList);
            shiftItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsShift.getCustomFields(), customFieldsItems));
            shiftItem.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_SHIFT));
            shiftItem.setBirgada(edsShift.getBrigada() != null ? edsShift.getBrigada().getAsSelectItem() : null);
            ArrayList<SelectItem> owners = new ArrayList<>();
            if (edsShift.getOwnersId() != null && !edsShift.getOwnersId().isEmpty()) {
                for (EdsEmployee employeesById : employeeManager.getEmployeesByIds(edsShift.getOwnersId())) {
                    owners.add(new SelectItem(employeesById.getObjectID(), employeesById.getFormmattedName()));
                }
            }
            shiftItem.setOwnersSelectItem(owners);
            return shiftItem;
        } else {
            ShiftItem item = new ShiftItem();
            item.setCreatorDepartment(userManager.getUser().getEmployee().getTeam().getAsSelectItem());
            return item;
        }
    }


    @Override
    public RotationItem getRotationItem(Integer objectId, boolean fromSummary) {
        EdsRotation edsRotation = rotationManager.get(objectId);
        RotationItem item = new RotationItem();
        if (edsRotation != null) {
            item = edsRotation.toRpc();
            item.setNumberData(EdsNumberingSettings.getDefaultData(edsRotation.getIntNumber() - 1, EdsNumberingSettings.DEF_ROTATION_PREFIX));
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.RotationList);
            item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsRotation.getCustomFields(), customFieldsItems));
            if (edsRotation.getItemTables() != null) {
                RotationTableItem[] table = new RotationTableItem[edsRotation.getItemTables().size()];
                int counter = 0;
                for (EdsRotationItemTable itemTable : edsRotation.getItemTables()) {
                    RotationTableItem rotationTableItem = new RotationTableItem();
                    rotationTableItem.setItemId(itemTable.getObjectID());
                    rotationTableItem.setEmployee(itemTable.getEmployee().getAsSelectItem());
                    rotationTableItem.setCurrentLocation(itemTable.getCurrentLocation() != null ? itemTable.getCurrentLocation().getAsSelectItem() : null);
                    rotationTableItem.setNewLocation(itemTable.getNewLocation() != null ? itemTable.getNewLocation().getAsSelectItem() : null);
                    rotationTableItem.setCurrentDepartment(itemTable.getCurrentDepartment() != null ? itemTable.getCurrentDepartment().getAsSelectItem() : null);
                    rotationTableItem.setNewDepartment(itemTable.getNewDepartment() != null ? itemTable.getNewDepartment().getAsSelectItem() : null);
                    rotationTableItem.setCurrentPosition(itemTable.getCurrentPosition() != null ? itemTable.getCurrentPosition().getAsSelectItem() : null);
                    rotationTableItem.setNewPosition(itemTable.getNewPosition() != null ? itemTable.getNewPosition().getAsSelectItem() : null);
                    rotationTableItem.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                            commonService.getCompanyCustomFields(ViewName.RotationItemTable)));
                    table[counter] = rotationTableItem;
                    counter++;
                }
                item.setRotationTableItems(table);
            }
        } else {
            item.setNumberData(generateRotationNumber(objectId));
        }


        item.setColumnConfigs(itemTableSettingService.getColumnConfigs(ItemTableEnum.ROTATION_ITEM_TABLE));
        item.setTemplates(getRotationPdfTemplates(PdfReferenceCodeNameEnum.ROTATION.name()).getItems());
        item.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_ROTATION));
        return item;
    }

    @Override
    public GroupPlacementItem getGroupPlacementItem(Integer objectId, boolean fromSummary) {
        EdsGroupPlacement edsGroupPlacement = groupPlacementManager.get(objectId);
        GroupPlacementItem item = new GroupPlacementItem();
        if (edsGroupPlacement != null) {
            item = edsGroupPlacement.toRpc();
            item.setNumberData(EdsNumberingSettings.getDefaultData(edsGroupPlacement.getIntNumber() - 1, EdsNumberingSettings.DEF_GROUP_PLACEMENT_PREFIX));
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.GroupPlacementList);
            item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsGroupPlacement.getCustomFields(), customFieldsItems));
            if (edsGroupPlacement.getItemTables() != null) {
                item.setPlacementTableItems(edsGroupPlacement.getItemTables().stream()
                        .map(itemTable -> {
                            GroupPlacementTableItem groupPlacementTableItem = new GroupPlacementTableItem();
                            groupPlacementTableItem.setObjectID(itemTable.getObjectID());
                            groupPlacementTableItem.setLocation(itemTable.getLocation() != null ? itemTable.getLocation().getAsSelectItem() : null);
                            groupPlacementTableItem.setType(itemTable.getType() != null ? itemTable.getType() : null);
                            groupPlacementTableItem.setCandidate(itemTable.getType() != null ? itemTable.getType().equals(EMPLOYEE_ID) ? itemTable.getEmployee().getAsSelectItem() : itemTable.getCandidate().getAsSelectItem() : null);
                            groupPlacementTableItem.setDepartment(itemTable.getDepartment() != null ? itemTable.getDepartment().getAsSelectItem() : null);
                            groupPlacementTableItem.setPosition(itemTable.getPosition() != null ? itemTable.getPosition().getAsSelectItem() : null);
                            groupPlacementTableItem.setMatchedVacancy(itemTable.getVacancy() != null ? itemTable.getVacancy().getAsSelectItem() : null);
                            groupPlacementTableItem.setEffectiveDate(itemTable.getEffectiveDate() != null ? itemTable.getEffectiveDate() : null);
                            return groupPlacementTableItem;
                        }).toArray(GroupPlacementTableItem[]::new));
            }
        } else {
            item.setNumberData(generateGroupPlacementNumber(objectId));
        }


        item.setColumnConfigs(itemTableSettingService.getColumnConfigs(ItemTableEnum.GROUP_PLACEMENT_ITEM_TABLE));
        item.setTemplates(getRotationPdfTemplates(PdfReferenceCodeNameEnum.GROUP_PLACEMENT.name()).getItems());
        item.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_GROUP_PLACEMENT));
        return item;
    }

    private CustomFormItemPdfTemplateList getRotationPdfTemplates(String type) {
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


    @Override
    public void deleteRotationItem(Integer objectId) {
        EdsRotation edsRotation = rotationManager.get(objectId);
        edsRotation.setDeleted(true);
        rotationManager.update(edsRotation);
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, edsRotation, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_ROTATION);
    }

    @Override
    public void deleteGroupPlacementItem(Integer objectId) {
        EdsGroupPlacement edsGroupPlacement = groupPlacementManager.get(objectId);
        edsGroupPlacement.setDeleted(true);
        groupPlacementManager.update(edsGroupPlacement);
        placementManager.getPlacementByGroupPlacement(objectId)
                .forEach(p -> {
                    p.setDeleted(true);
                    placementManager.update(p);
                });
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, edsGroupPlacement, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_GROUP_PLACEMENT);
    }


    @Override
    @Transactional
    public void createRotation(RotationItem item) {
        NumberData numberData = item.getNumberData();
        EdsRotation rotation = rotationManager.get(item.getId()) != null ? rotationManager.get(item.getId()) : new EdsRotation();
        NumberData newNumberData = numberData;
        if (rotation == null && rotationManager.isRotationNumberExist(item.getId() == null ? numberData.getNumberString() : rotation.getRotationCode(), item.getId())) {
            newNumberData = this.generateRotationNumber();
        }
        if (item.getNumberData() != null && !item.getNumberData().getNumberString().equals("")) {
            rotation.setIntNumber(newNumberData.getIntNumber());
            rotation.setRotationCode(newNumberData.getNumberString());
        }
        rotation.setDate(item.getDate().getNonConvertedDate());

        rotation.setCustomFields(createRotationCustomFields(rotation.getCustomFields(), item.getCustomFieldItems()));


        for (EdsRotationItemTable itemTable : rotation.getItemTables()) {
            rotationItemTableManager.delete(itemTable);
        }

        Set<EdsRotationItemTable> items = new HashSet<>();

        for (RotationTableItem rotationTableItem : item.getRotationTableItems()) {
            EdsRotationItemTable edsRotationItemTable = rotationItemTableManager.get(rotationTableItem.getItemId()) != null ? rotationItemTableManager.get(rotationTableItem.getItemId()) : new EdsRotationItemTable();
            edsRotationItemTable.setEmpId(rotationTableItem.getEmployee().getId());
            if (rotationTableItem.getCurrentLocation() != null) {
                edsRotationItemTable.setCurLocId(rotationTableItem.getCurrentLocation().getId());
            }
            if (rotationTableItem.getNewLocation() != null) {
                edsRotationItemTable.setNewLocId(rotationTableItem.getNewLocation().getId());
            }
            if (rotationTableItem.getCurrentDepartment() != null) {
                edsRotationItemTable.setCurDepId(rotationTableItem.getCurrentDepartment().getId());
            }
            if (rotationTableItem.getNewDepartment() != null) {
                edsRotationItemTable.setNewDepID(rotationTableItem.getNewDepartment().getId());
            }
            if (rotationTableItem.getCurrentPosition() != null) {
                edsRotationItemTable.setCurPosId(rotationTableItem.getCurrentPosition().getId());
            }
            if (rotationTableItem.getNewPosition() != null) {
                edsRotationItemTable.setNewPosId(rotationTableItem.getNewPosition().getId());
            }
            edsRotationItemTable.setCustomFields(saveItemCustomFields(edsRotationItemTable.getCustomFields(), rotationTableItem.getItemCustomFields()));
            edsRotationItemTable.setEdsRotation(rotation);
            items.add(edsRotationItemTable);
        }
        rotation.setItemTables(items);


        EdsUser user = userManager.getUser();
        if (item.getId() == null) {
            rotation.setCreatorId(user.getObjectID());
            rotation.setCreatedDate(new Date());
            rotation.setUpdaterId(user.getObjectID());
            rotation.setUpdatedDate(new Date());
        } else {
            rotation.setUpdaterId(user.getObjectID());
            rotation.setUpdatedDate(new Date());
        }

        if (Constants.ROTATION_APPROVED.equals(item.getStatusCode())) {
            rotation.setApprovedDate(new Date());
        }

        rotationManager.createOrUpdate(rotation);

//        if (ROTATION_APPROVED.equals(item.getStatusCode())) {
//            updateEmployeeByRotation(rotation);
//        }


        boolean statusChanged = rotation.getOverallStatus() != null && !item.getStatusCode().equals(rotation.getOverallStatus().getCode());

        if (!isOk(item.getApprovers())) {
            rotation.setEntityStatus(referenceManager.findReference(Constants.ROTATION_STATUS, item.getStatusCode()));
        }

        if (isOk(item.getApprovers())) {
            item.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : item.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (rotation.getCurrentApprover() != null && item.getStatusCode() != null && isFirstApprover) {
                        rotation.getCurrentApprover().setStatus(referenceManager.findReference(Constants.ROTATION_STATUS, item.getStatusCode()));
                        rotation.setEntityStatus(referenceManager.findReference(Constants.ROTATION_STATUS, Constants.ROTATION_SUBMITTED));
                        isFirstApprover = false;
                    } else if (rotation.getCurrentApprover() != null && item.getStatusCode() != null) {
                        rotation.getCurrentApprover().setStatus(referenceManager.findReference(Constants.ROTATION_STATUS, Constants.ROTATION_SUBMITTED));
                    }
                    if (item.getStatusCode() != null && !ROTATION_APPROVED.equals(item.getStatusCode())) {
                        rotation.setEntityStatus(referenceManager.findReference(Constants.ROTATION_STATUS, item.getStatusCode()));
                    }
                    if (rotation.isCurrentApproverRejected()) {
                        rotation.setEntityStatus(rotation.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(rotation.getObjectID());
                edsApprover.setIs_default(false);

                if (item.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.ROTATION_STATUS, item.getStatusCode()));
                    if (Constants.ROTATION_DRAFT.equals(item.getStatusCode())) {
                        rotation.setEntityStatus(referenceManager.findReference(Constants.ROTATION_STATUS, item.getStatusCode()));
                    } else {
                        rotation.setEntityStatus(referenceManager.findReference(Constants.ROTATION_STATUS, ROTATION_SUBMITTED));
                    }
                    isFirstApprover = false;
                } else if (item.getStatusCode() != null) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.ROTATION_STATUS, ROTATION_SUBMITTED));
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

                if (rotation.getCurrentApprover() == null) {
                    rotation.setCurrentApprover(edsApprover);
                }
                rotation.getApprovers().add(edsApprover);
            }
        }

        if (item.getId() == null) {
            baseEventsPostProcessor.registerEvent(RotationEventListenerImpl.TYPE, MyUpdateItem.ADD, rotation, userManager.getUser());
            if (item.getStatusCode().equals(Constants.ROTATION_DRAFT)) {
                baseEventsPostProcessor.registerEvent(RotationEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, rotation, userManager.getUser());
            }
            if (item.getStatusCode().equals(Constants.ROTATION_APPROVED)) {
                baseEventsPostProcessor.registerEvent(RotationEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, rotation, userManager.getUser());
            }
        } else if (!Constants.ROTATION_APPROVED.equals(item.getStatusCode()) && !Constants.ROTATION_DRAFT.equals(item.getStatusCode())) {
            baseEventsPostProcessor.registerEvent(RotationEventListenerImpl.TYPE, MyUpdateItem.EDIT, rotation, userManager.getUser());
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), rotation, user);
        workflowEvent.setEntityType(RelationItem.TYPE_ROTATION);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsRotation.class.getSimpleName());
        if (item.getId() != null) {
            kpiLog.setEntityId(item.getId());
        }
        if (item.getId() == null) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add Rotation");
            EdsBusinessEvent workflowEventAdd = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, rotation, user);
            workflowEventAdd.setEntityType(RelationItem.TYPE_ROTATION);
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(log, kpiLog, "Update rotation");
            EdsBusinessEvent workflowEventEdit = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, rotation, user);
            workflowEventEdit.setEntityType(RelationItem.TYPE_ROTATION);
        }

        if (ROTATION_SUBMITTED.equals(item.getStatusCode())) {
            boolean hasAlerts = false;
            List<EdsWorkflowRule> rules = workflowRuleManager.getByModuleAndActions(WorkflowRule._WORKFLOW_MODULE_ROTATION, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT);
            if (rules != null && rules.size() > 0) {
                for (EdsWorkflowRule rule : rules) {
                    hasAlerts = workflowAlertManager.hasAlertsByRoleID(rule.getObjectID());
                    if (hasAlerts) {
                        break;
                    }
                }
            }

            if (!hasAlerts) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (statusChanged || item.getStatusCode().equals(ROTATION_SUBMITTED)) {
                baseEventPostProcessor.registerEvent(RotationEventListenerImpl.TYPE, RotationEventListenerImpl.ROTATION_SUBMITTED, rotation, user);
            }

        }
    }

    @Override
    public void createGroupPlacement(GroupPlacementItem item) {
        NumberData numberData = item.getNumberData();
        EdsGroupPlacement placement = groupPlacementManager.get(item.getId());
        if (placement == null) {
            placement = new EdsGroupPlacement();
        }

        NumberData newNumberData = null;
        String placementCode = item.getId() == null ? numberData.getNumberString() : placement.getPlacementCode();
        if (groupPlacementManager.isGroupPlacementNumberExist(placementCode, item.getId())) {
            newNumberData = this.generateGroupPlacementNumber(placement.getObjectID());
        } else {
            newNumberData = numberData;
        }

        if (newNumberData != null && !newNumberData.getNumberString().equals("")) {
            placement.setIntNumber(newNumberData.getIntNumber());
            placement.setPlacementCode(newNumberData.getNumberString());
        }

        placement.setDate(item.getDate());
        placement.setCustomFields(createGroupPlacementCustomFields(placement.getCustomFields(), item.getCustomFieldItems()));

        if (item.getId() != null) {
            placement.getItemTables().forEach(groupPlacementItemTableManager::delete);
        }

        Set<EdsGroupPlacementItemTable> items = new HashSet<>();

        for (GroupPlacementTableItem placementTableItem : item.getPlacementTableItems()) {
            EdsGroupPlacementItemTable edsPlacementItemTable = new EdsGroupPlacementItemTable();
            if (placementTableItem.getLocation() != null) {
                edsPlacementItemTable.setLocationId(placementTableItem.getLocation().getId());
            }
            if (placementTableItem.getType() != null) {
                edsPlacementItemTable.setType(placementTableItem.getType());
            }
            if (placementTableItem.getCandidate() != null && placementTableItem.getType() != null) {
                if (placementTableItem.getType().equals(CANDIDATE_ID)) {
                    edsPlacementItemTable.setCandidateId(placementTableItem.getCandidate().getId());
                } else {
                    edsPlacementItemTable.setEmpId(placementTableItem.getCandidate().getId());
                }
            }
            if (placementTableItem.getDepartment() != null) {
                edsPlacementItemTable.setCurDepId(placementTableItem.getDepartment().getId());
            }
            if (placementTableItem.getPosition() != null) {
                edsPlacementItemTable.setPosId(placementTableItem.getPosition().getId());
            }
            if (placementTableItem.getMatchedVacancy() != null) {
                edsPlacementItemTable.setVacancyId(placementTableItem.getMatchedVacancy().getId());
            }
            if (placementTableItem.getEffectiveDate() != null) {
                edsPlacementItemTable.setEffectiveDate(placementTableItem.getEffectiveDate());
            }
            edsPlacementItemTable.setEdsGroupPlacement(placement);
            items.add(edsPlacementItemTable);
        }
        placement.setItemTables(items);


        EdsUser user = userManager.getUser();
        if (item.getId() == null) {
            placement.setCreator(user);
            placement.setCreatedDate(new Date());
        }
        placement.setUpdater(user);
        placement.setUpdatedDate(new Date());

        groupPlacementManager.createOrUpdate(placement);

        boolean statusChanged = placement.getOverallStatus() != null && !item.getStatusCode().equals(placement.getOverallStatus().getCode());

        if (!isOk(item.getApprovers())) {
            placement.setEntityStatus(referenceManager.findReference(Constants.GROUP_PLACEMENT_STATUS, item.getStatusCode()));
        }

        if (isOk(item.getApprovers())) {
            item.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : item.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (placement.getCurrentApprover() != null && item.getStatusCode() != null && isFirstApprover) {
                        placement.getCurrentApprover().setStatus(referenceManager.findReference(Constants.GROUP_PLACEMENT_STATUS, item.getStatusCode()));
                        placement.setEntityStatus(referenceManager.findReference(Constants.GROUP_PLACEMENT_STATUS, Constants.GROUP_PLACEMENT_SUBMITTED));
                        isFirstApprover = false;
                    } else if (placement.getCurrentApprover() != null && item.getStatusCode() != null) {
                        placement.getCurrentApprover().setStatus(referenceManager.findReference(Constants.GROUP_PLACEMENT_STATUS, Constants.GROUP_PLACEMENT_SUBMITTED));
                    }
                    if (item.getStatusCode() != null && !GROUP_PLACEMENT_APPROVED.equals(item.getStatusCode())) {
                        placement.setEntityStatus(referenceManager.findReference(Constants.GROUP_PLACEMENT_STATUS, item.getStatusCode()));
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

                if (item.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.GROUP_PLACEMENT_STATUS, item.getStatusCode()));
                    if (Constants.GROUP_PLACEMENT_DRAFT.equals(item.getStatusCode())) {
                        placement.setEntityStatus(referenceManager.findReference(Constants.GROUP_PLACEMENT_STATUS, item.getStatusCode()));
                    } else {
                        placement.setEntityStatus(referenceManager.findReference(Constants.GROUP_PLACEMENT_STATUS, GROUP_PLACEMENT_SUBMITTED));
                    }
                    isFirstApprover = false;
                } else if (item.getStatusCode() != null) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.GROUP_PLACEMENT_STATUS, GROUP_PLACEMENT_SUBMITTED));
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

        if (item.getId() == null) {
            baseEventsPostProcessor.registerEvent(GroupPlacementEventListenerImpl.TYPE, MyUpdateItem.ADD, placement, userManager.getUser());
            if (item.getStatusCode().equals(Constants.GROUP_PLACEMENT_DRAFT)) {
                baseEventsPostProcessor.registerEvent(GroupPlacementEventListenerImpl.TYPE, GROUP_PLACEMENT_DRAFT, placement, userManager.getUser());
            }
            if (item.getStatusCode().equals(Constants.GROUP_PLACEMENT_APPROVED)) {
                baseEventsPostProcessor.registerEvent(GroupPlacementEventListenerImpl.TYPE, GROUP_PLACEMENT_APPROVED, placement, userManager.getUser());
            }
        } else if (!Constants.GROUP_PLACEMENT_APPROVED.equals(item.getStatusCode()) && !Constants.GROUP_PLACEMENT_DRAFT.equals(item.getStatusCode())) {
            baseEventsPostProcessor.registerEvent(GroupPlacementEventListenerImpl.TYPE, MyUpdateItem.EDIT, placement, userManager.getUser());
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), placement, user);
        workflowEvent.setEntityType(RelationItem.TYPE_GROUP_PLACEMENT);

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGroupPlacement.class.getSimpleName());
        if (item.getId() != null) {
            kpiLog.setEntityId(item.getId());
        }
        if (item.getId() == null) {
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            ServerUtils.kpiLog(log, kpiLog, "Add Group Placement");
            EdsBusinessEvent workflowEventAdd = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, placement, user);
            workflowEventAdd.setEntityType(RelationItem.TYPE_GROUP_PLACEMENT);
        } else {
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            ServerUtils.kpiLog(log, kpiLog, "Update Group Placement");
            EdsBusinessEvent workflowEventEdit = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, placement, user);
            workflowEventEdit.setEntityType(RelationItem.TYPE_GROUP_PLACEMENT);
        }

        if (GROUP_PLACEMENT_SUBMITTED.equals(item.getStatusCode())) {
            boolean hasAlerts = false;
            List<EdsWorkflowRule> rules = workflowRuleManager.getByModuleAndActions(WorkflowRule._WORKFLOW_MODULE_GROUP_PLACEMENT, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT);
            if (rules != null && rules.size() > 0) {
                for (EdsWorkflowRule rule : rules) {
                    hasAlerts = workflowAlertManager.hasAlertsByRoleID(rule.getObjectID());
                    if (hasAlerts) {
                        break;
                    }
                }
            }

            if (!hasAlerts) {
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (statusChanged || item.getStatusCode().equals(GROUP_PLACEMENT_SUBMITTED)) {
                baseEventPostProcessor.registerEvent(GroupPlacementEventListenerImpl.TYPE, GroupPlacementEventListenerImpl.GROUP_PLACEMENT_SUBMITTED, placement, user);
            }

        }
        if (item.getStatusCode().equals(GROUP_PLACEMENT_APPROVED)) {
            baseEventPostProcessor.registerEvent(GroupPlacementEventListenerImpl.TYPE, GROUP_PLACEMENT_APPROVED, placement, user);
        }

    }


    private EdsRotationItemTableCF saveItemCustomFields(EdsRotationItemTableCF edsItemCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsItemCustomFields == null) {
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
                edsItemCustomFields = new EdsRotationItemTableCF();
                rotationItemCFManager.createOrUpdate(edsItemCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsItemCustomFields, customFieldItems);
            return edsItemCustomFields;
        }
        return null;
    }


    public NumberData generateRotationNumber(Integer rotationID) {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = rotationID != null ? rotationManager.get(rotationID).getIntNumber() : rotationManager.getRotationLastIntNumber();
        if (settings != null && settings.getVacancyNumberingFormat() != null) {
            return settings.parseNumberData(rotationID != null ? intNumber - 1 : intNumber, settings.getVacancyNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_ROTATION_PREFIX);
        }
    }

    public NumberData generateGroupPlacementNumber(Integer placementId) {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = placementId != null ? groupPlacementManager.get(placementId).getIntNumber() : groupPlacementManager.getGroupPlacementLastIntNumber();
        if (settings != null && settings.getDelimetrGroupPlacementNumberingFormat() != null && !settings.getDelimetrGroupPlacementNumberingFormat().isEmpty()) {
            return settings.parseNumberData(placementId != null ? intNumber - 1 : intNumber, settings.getDelimetrGroupPlacementNumberingFormat());
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_GROUP_PLACEMENT_PREFIX);
        }
    }

    private EdsRotationCutomFields createRotationCustomFields(EdsRotationCutomFields
                                                                      edsRotationCutomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsRotationCutomFields == null) {
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
                edsRotationCutomFields = new EdsRotationCutomFields();
                rotationCfManager.create(edsRotationCutomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsRotationCutomFields, customFieldItems);
            return edsRotationCutomFields;
        }
        return null;
    }

    private EdsGroupPlacementCustomFields createGroupPlacementCustomFields(EdsGroupPlacementCustomFields
                                                                                   edsGroupPlacementCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsGroupPlacementCustomFields == null) {
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
                edsGroupPlacementCustomFields = new EdsGroupPlacementCustomFields();
                groupPlacementCustomFieldManager.create(edsGroupPlacementCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsGroupPlacementCustomFields, customFieldItems);
            return edsGroupPlacementCustomFields;
        }
        return null;
    }

    private CustomFormItemPdfTemplateList getShiftPdfTemplates(String type) {
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

    @Override
    public LinkedHashMap<String, List<EmployeeReport>> getProjectEmployeesSelectItem(LinkedList<Integer> groupsId, Date period, Integer lookUpType) {
        LinkedHashMap<String, List<EmployeeReport>> groupAndSelectedEmpoyee = new LinkedHashMap<>();
        String teamName = "";
        for (Integer id : groupsId) {
            ArrayList<EmployeeReport> groupEmployees = new ArrayList<>();
            if (lookUpType.equals(BRIGADA_ID)) {
                List<EdsBrigadaEmployee> employeesByProject = brigadaManager.getEmployeesByBrigada(id);
                for (EdsBrigadaEmployee edsProjectEmployee : employeesByProject) {
                    teamName = edsProjectEmployee.getProject().getName();
                    EmployeeReport employeeReport = new EmployeeReport();
                    EdsEmployee employee = edsProjectEmployee.getEmployeeDepartment().getEmployee();
                    if (employee != null) {
                        setEmployeesInfo(employeeReport, employee, period);
                        employeeReport.setUnit(edsProjectEmployee.getUnit() != null ? edsProjectEmployee.getUnit() : "");
                        employeeReport.setTeam(new SelectItem(edsProjectEmployee.getProject().getObjectID(), edsProjectEmployee.getProject().getName()));
                        groupEmployees.add(employeeReport);
                    }
                }
            } else if (lookUpType.equals(LookUpConstants.EMPLOYEE_ID) || lookUpType.equals(LookUpConstants.OVERTIME)) {
                EmployeeReport employeeReport = new EmployeeReport();
                EdsEmployee employee = employeeManager.get(id);
                setEmployeesInfo(employeeReport, employee, period);
                groupEmployees.add(employeeReport);
            }
            groupEmployees.sort(Comparator.comparing(EmployeeReport::getEmployeeName));
            if (lookUpType.equals(BRIGADA_ID)) {
                groupAndSelectedEmpoyee.put(teamName, groupEmployees);
            } else {
                groupAndSelectedEmpoyee.put(String.valueOf(id), groupEmployees);
            }

        }

        return groupAndSelectedEmpoyee;
    }

    @Override
    public EmployeeAttendanceReport getDepartmentEmployeesSelectItem(ListingFilterParameter filterParameter, int monthMaxDay) {
        EmployeeAttendanceReport employeeAttendanceReport = availabilityService.getEmployeeAttendanceReport(filterParameter, monthMaxDay);
        return employeeAttendanceReport;
    }

    private void setEmployeesInfo(EmployeeReport employeeReport, EdsEmployee employee, Date period) {
        Date from = ServerUtils.getMonthStartDate(period);
        Date to = ServerUtils.getMonthEndDate(period);
        employeeReport.setId(employee.getObjectID());
        employeeReport.setCode(employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null ? employee.getProfile().getEmployeeCode() : "");
        employeeReport.setEmployeeName(new SelectItem(employee.getObjectID(), employee.getFormmattedName()));
        employeeReport.setName(employee.getFormmattedName());
        employeeReport.setPosition(employee.getPosition() != null ? employee.getPosition().getName() : "");
        employeeReport.setDepartmentOrPosition(employee.getEmployeeDepartment() != null ? employee.getEmployeeDepartment().getTeam().getName() : "");
        List<EdsSickRequest> sickRequestByEmployeeAndPeriod = sickRequestManager.getSickRequestByEmployeeAndPeriod(employee, from, to);
        int leaveCounter = 0;
        ArrayList<StatisticsLeaveRequest> leavs = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat(employee.getCompany().getCompanySettings().getShortDateFormat());
        for (EdsSickRequest sickRequest : sickRequestByEmployeeAndPeriod) {
            if (!sickRequest.getLeaveReason().getMarkAsDraft() && sickRequest.getOverallStatus() != null && "SS_APPROVED".equals(sickRequest.getOverallStatus().getCode())) {
                StatisticsLeaveRequest leaveRequestDTO = new StatisticsLeaveRequest();
                leaveRequestDTO.setObjectID(sickRequest.getObjectID());
                leaveRequestDTO.setFromDate(sickRequest.getStartDate() != null ? dateFormat.format(sickRequest.getStartDate()) : "");
                leaveRequestDTO.setToDate(sickRequest.getEndDate() != null ? dateFormat.format(sickRequest.getEndDate()) : "");
                leaveRequestDTO.setDescription(sickRequest.getLeaveReason().getColor());
                leaveRequestDTO.setReason(sickRequest.getLeaveReason().getLocale() != null ? sickRequest.getLeaveReason().getLocale().getLocaleByCode(String.valueOf(ServerUtils.getUserLocale())) : sickRequest.getLeaveReason().getName());
                leavs.add(leaveRequestDTO);
                leaveCounter++;
            }
        }
        employeeReport.setLeave(leavs);
        employeeReport.setLeaveCount(leaveCounter);
    }

    @Override
    public LinkedHashMap<Integer, List<ShiftTeamsItem>> getShiftTeamsData(Integer shiftId, Set<Integer> teamsIdByShift, Integer lookUpType, Date period, boolean fromSummary) {
        Date from = ServerUtils.getMonthStartDate(period);
        Date to = ServerUtils.getMonthEndDate(period);
        LinkedHashMap<Integer, List<StatisticsLeaveRequest>> sickRequestByEmployeeAndPeriod = sickRequestManager.getSickRequestByEmployeeAndPeriod(from, to);

        LinkedHashMap<Integer, List<ShiftTeamsItem>> shiftItems = new LinkedHashMap<>();

        for (Integer id : teamsIdByShift) {
            List<ShiftTeamsItem> managers = new LinkedList<>();
            List<ShiftTeamsItem> employees = new LinkedList<>();
            List<EdsShiftTeams> shiftTeamsByShiftAndGroupId = lookUpType == null || lookUpType.equals(BRIGADA_ID) ? shiftTeamsManager.getShiftTeamsByShiftAndGroupId(shiftId, id) : shiftTeamsManager.getShiftTeamsByShiftAndGroupId(shiftId, null);

            shiftTeamsByShiftAndGroupId.forEach(edsShiftTeams -> {
                ShiftTeamsItem shiftTeamsItem = new ShiftTeamsItem();
                shiftTeamsItem.setTeam(new SelectItem(edsShiftTeams.getTeamId(), edsShiftTeams.getTeam()));
                shiftTeamsItem.setLabel(edsShiftTeams.getLabel());
                shiftTeamsItem.setFullName(new SelectItem(edsShiftTeams.getEmpId(), edsShiftTeams.getFullname()));
                shiftTeamsItem.setDepartment(edsShiftTeams.getDepartment());
                shiftTeamsItem.setPosition(edsShiftTeams.getPosition());
                shiftTeamsItem.setEmployeeCode(edsShiftTeams.getEmployeeCode());
                shiftTeamsItem.setAdditionalPosition(edsShiftTeams.getAdditionalPosition());

                if ("Manager".equals(shiftTeamsItem.getAdditionalPosition())) {
                    managers.add(0, shiftTeamsItem);
                } else if ("BackupManager".equals(shiftTeamsItem.getAdditionalPosition())) {
                    managers.add(shiftTeamsItem);
                } else {
                    employees.add(shiftTeamsItem);
                }

                if (!fromSummary) {
                    List<StatisticsLeaveRequest> sickRequests = sickRequestByEmployeeAndPeriod.get(edsShiftTeams.getEmpId());
                    List<StatisticsLeaveRequest> leaves = new ArrayList<>();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                    int leaveCounter = 0;

                    if (sickRequests != null) {
                        leaves = sickRequests.stream()
                                .filter(sickRequest -> (sickRequest.isMarkAsDraft() != null && !sickRequest.isMarkAsDraft() || sickRequest.isMarkAsDraft() == null) && sickRequest.getStatus() != null && "SS_APPROVED".equals(sickRequest.getLeaveRrequestCode()))
                                .map(sickRequest -> {
                                    StatisticsLeaveRequest leaveRequestDTO = new StatisticsLeaveRequest();
                                    leaveRequestDTO.setObjectID(sickRequest.getObjectID());
                                    leaveRequestDTO.setFromDate(sickRequest.getStartDate() != null ? dateFormat.format(sickRequest.getStartDate()) : "");
                                    leaveRequestDTO.setToDate(sickRequest.getEndDate() != null ? dateFormat.format(sickRequest.getEndDate()) : "");
                                    leaveRequestDTO.setDescription(sickRequest.getDescription());
                                    leaveRequestDTO.setReason(sickRequest.getReason());
                                    return leaveRequestDTO;
                                })
                                .collect(Collectors.toList());

                        leaveCounter = leaves.size();
                    }

                    shiftTeamsItem.setLeave(leaves);
                    shiftTeamsItem.setLeaveCount(leaveCounter);
                }
            });

            List<ShiftTeamsItem> mergedItems = new LinkedList<>();
            mergedItems.addAll(managers);
            mergedItems.addAll(employees);
            mergedItems.sort(Comparator.comparing(ShiftTeamsItem::getFullName));
            shiftItems.put(id, mergedItems);
        }

        LinkedHashMap<Integer, List<ShiftTeamsItem>> finalShiftItems = new LinkedHashMap<>();

        if (lookUpType == null || lookUpType.equals(BRIGADA_ID)) {
            teamsIdByShift.forEach(integer -> {
                List<ShiftTeamsItem> shiftTeamsItemsManagers = shiftItems.get(integer);
                shiftTeamsItemsManagers.sort(Comparator.comparing(ShiftTeamsItem::getFullName));
                finalShiftItems.put(integer, shiftTeamsItemsManagers);
            });
        } else {
            List<ShiftTeamsItem> shiftTeamsItems = shiftItems.get(teamsIdByShift.iterator().next());
            shiftTeamsItems.sort(Comparator.comparing(ShiftTeamsItem::getFullName));
            finalShiftItems.put(teamsIdByShift.iterator().next(), shiftTeamsItems);
        }

        return finalShiftItems;

    }

    @Override
    public ArrayList<SelectItem> getTeamsManagers(LinkedList<Integer> groupsId) {
        ArrayList<SelectItem> teamTransferList = new ArrayList<>();
        for (Integer id : groupsId) {
            List<EdsBrigadaEmployee> employeesByProject = brigadaManager.getEmployeesByBrigada(id);
            EdsBrigada edsProject = employeesByProject.get(0).getProject();
            String manager = edsProject.getManager().getFullName() + " - " + edsProject.getName() + " - " + employeesByProject.size();
            String backupManager = null;
            if (edsProject.getBackupManagers() != null && !edsProject.getBackupManagers().isEmpty()) {
                backupManager = edsProject.getName() + " - " + edsProject.getBackupManagers().stream().map(EdsEmployee::getFullName).collect(Collectors.joining(","));
            }

            teamTransferList.add(new SelectItem(id, edsProject.getName(), manager, backupManager));
        }
        return teamTransferList;
    }

    @Override
    public HashMap<Integer, ArrayList<SelectItem>> getTeamsEmployeesInfo() {
        HashMap<Integer, ArrayList<SelectItem>> groupAndSelectedEmpoyee = new HashMap<>();
        List<Integer> activeTeamsId = brigadaManager.getActiveTeamsId();
        for (Integer id : activeTeamsId) {
            ArrayList<SelectItem> groupEmployees = new ArrayList<>();
            List<EdsBrigadaEmployee> employeesByProject = brigadaManager.getEmployeesByBrigada(id);
            for (EdsBrigadaEmployee edsBrigadaEmployee : employeesByProject) {
                groupEmployees.add(new SelectItem(edsBrigadaEmployee.getEmployeeDepartment().getEmployee().getObjectID(), edsBrigadaEmployee.getEmployeeDepartment().getEmployee().getFormmattedName()));
            }
            Collections.sort(groupEmployees);
            groupAndSelectedEmpoyee.put(id, groupEmployees);
        }

        return groupAndSelectedEmpoyee;
    }

    @Override
    public void deleteShiftItem(Integer objectId) {
        EdsShift edsShift = shiftManager.get(objectId);
        EdsUser user = userManager.getUser();
        if (edsShift != null) {
            edsShift.setDeleted(true);
            edsShift.setUpdater(user);
            edsShift.setUpdatedDate(new Date());
            shiftManager.update(edsShift);
            ArrayList<EdsShiftItem> shiftItemsByShiftId = shiftItemManager.getShiftItemsByShiftId(edsShift.getObjectID());
            for (EdsShiftItem shiftItem : shiftItemsByShiftId) {
                shiftItem.setDeleted(true);
                shiftItemManager.update(shiftItem);
            }


            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, edsShift, user);
            event.setEntityType(RelationItem.TYPE_SHIFT);

            baseEventPostProcessor.registerEvent(ShiftEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, edsShift, user);
        }
    }

    @Override
    public void deleteBrigada(Integer brigadaId) {
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsProject.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(brigadaId);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Brigada deleted");


        EdsBrigada project = brigadaManager.get(brigadaId);

        EdsUser user = this.employeeManager.getUser();
        //Who is deleted this project
        project.setUpdater(user);
        project.setLastUpdateTime(user.getCompany().getCompanyDate());


        //Delete project
        this.brigadaManager.deleteBrigada(project);
        this.brigadaManager.deleteBrigada(project);
    }

    @Override
    public void saveBrigadaEditCellValue(ProjectListItem rowValue, String columnCodeName, boolean changeTaskStatus) {
        EdsUser user = this.projectManager.getUser();

        EdsBrigada project = brigadaManager.get(rowValue.getObjectId());

        if (ProjectListItem.START_DATE.equals(columnCodeName)) {

        } else if (ProjectListItem.END_DATE.equals(columnCodeName)) {

        } else if (ProjectListItem.STATUS.equals(columnCodeName)) {


        }
        project.setLastUpdateTime(new Date());
        project.setUpdater(this.userManager.getUser());
//            try {
//                this.solrManager.indexAddProject(project, edsCompany.getObjectID());
//            } catch (Exception e) {
//                edsBusinessEvent.setSolrIndexed(false);
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            System.out.println("Task List Edit Cell Column Code :" + columnCodeName);
//        }
    }

    @Override
    public ListResult<ProjectListItem> getBrigadaList(ListingFilterParameter fp) {
        ArrayList<ProjectListItem> projectList = new ArrayList<>();
        List<EdsBrigada> brigadaList = new ArrayList<>();
        List<String> backups = new ArrayList<>();
        Integer userId = userManager.getUser().getObjectID();
        Integer totalCount = 0;
        boolean seeOwn = ServerUtils.hasPermission(PermissionConstants.HRMS_BRIGADA_SEE_OWN);
        boolean seeAll = ServerUtils.hasPermission(PermissionConstants.HRMS_BRIGADA_SEE_ALL);
        if (seeAll) {
            brigadaList = brigadaManager.getList(fp, null);
            totalCount = brigadaManager.getTotalCount(fp, null);
        } else if (seeOwn) {
            brigadaList = brigadaManager.getList(fp, userId);
            totalCount = brigadaManager.getTotalCount(fp, userId);
        } else {
            return new ListResult<>(projectList, 0, null);
        }
        for (EdsBrigada edsBrigada : brigadaList) {
            ProjectListItem projectRpc = new ProjectListItem();
            projectRpc.setObjectId(edsBrigada.getObjectID());
            projectRpc.setNumber(edsBrigada.getNumber());
            projectRpc.setName(edsBrigada.getName());
            if (fp.isLookUp()) {
                projectList.add(projectRpc);
                continue;
            }

            projectRpc.setDescription(edsBrigada.getDescription());
            projectRpc.setManager(edsBrigada.getManager().getFullName());
            edsBrigada.getBackupManagers().forEach(b -> backups.add(b.getFullName()));
            projectRpc.setBackupManager(ServerUtils.asListToString(backups));
            projectRpc.setBackupManagerIDs(edsBrigada.getBackupManagerIDs());
            projectRpc.setCreatedBy(edsBrigada.getCreator().getName());
            projectRpc.setCreatedDate(edsBrigada.getCreationTime());
            if (edsBrigada.getUpdater() != null) {
                projectRpc.setModifiedBy(edsBrigada.getUpdater().getName());
            }
            if (edsBrigada.getLastUpdateTime() != null) {
                projectRpc.setModifiedDate(edsBrigada.getLastUpdateTime());
            }
            projectRpc.setHeadCount(brigadaManager.getEmployeesByBrigada(edsBrigada.getObjectID()).size());
            projectRpc.setStatus(edsBrigada.getStatus() != null ? edsBrigada.getStatus().getName() : "N/A");
            projectList.add(projectRpc);
        }

        return new ListResult<>(projectList, totalCount, null);
    }

    @Override
    public EditProject getBrigadaForEdit(Integer projectId, Date date, Integer clientID) {
        EditProject projectItem = new EditProject();
        projectItem.setNumberData(generateProjectNumber(date, clientID, projectId));

        EdsBrigada project = brigadaManager.get(projectId);
        EdsUser user = this.employeeManager.getUser();

        if (project != null) {
            projectItem.setObjectId(project.getObjectID());
            projectItem.getNumberData().setNumberString(project.getNumber());
            projectItem.getNumberData().setIntNumber(project.getIntNumber());

            projectItem.setStatusId(project.getStatus() != null ? project.getStatus().getObjectID() : null);
            projectItem.setNumber(project.getNumber());
            projectItem.setName(project.getName());
            projectItem.setDescription(project.getDescription());
            projectItem.setManagerName(project.getManager() != null ? project.getManager().getFullName() : null);
            projectItem.setManagerId(project.getManager() != null ? project.getManager().getObjectID() : null);
            projectItem.setBackupManagerIDs(project.getBackupManagerIDs());
            projectItem.setEmployeeAssignment(project.getEmployeeAssignment());
            projectItem.setBillable(project.getBillable());
            ArrayList<SelectItem> owners = new ArrayList<>();
            if (project.getOwnersId() != null && !project.getOwnersId().isEmpty()) {
                for (EdsEmployee employeesById : employeeManager.getEmployeesByIds(project.getOwnersId())) {
                    owners.add(new SelectItem(employeesById.getObjectID(), employeesById.getFormmattedName()));
                }
            }
            projectItem.setOwners(owners);

            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.BrigadaList);
            projectItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(project != null ? project.getCustomFields() : null, customFieldsItems));

            StringBuilder names = new StringBuilder();
            for (EdsEmployee backupManager : project.getBackupManagers()) {
                if (names.toString().equals("")) {
                    names.append(backupManager.getFullName());
                } else {
                    names.append(", ").append(backupManager.getFullName());
                }
            }
            projectItem.setBackupManagerName(names.toString());
            projectItem.setLastUpdate(project.getLastUpdateTime() != null ? new Date(project.getLastUpdateTime().getTime()) : null);
            projectItem.setLocationId(project.getProjectLocation() != null ? project.getProjectLocation().getObjectID() : null);
            projectItem.setParentId(project.getParent() != null ? project.getParent().getObjectID() : null);
            projectItem.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_PROJECT, project.getObjectID())));
            projectItem.setDefaultProject(user.getCompany().getDefaultProject() != null && user.getCompany().getDefaultProject().getObjectID().equals(projectId));

            // Sets editable if user is PM or Project Backup Manager or Company Director, or Company Administrator
            if (!user.isClientContact() && (project.getManager().getObjectID().equals(user.getObjectID()) || (project.isUserBackupManager(user.getObjectID()))
                    || user.hasRole(this.roleManager.get(EdsRole.DR)) || user.hasRole(this.roleManager.get(EdsRole.ADMIN)))) {
                projectItem.setPermission(Constants.EDIT);
            } else {
                projectItem.setPermission(Constants.READ);
            }

            if (EmployeeAssignmentEnum.BY_POSITION.equals(project.getEmployeeAssignment())) {
                projectItem.setProjectPositions(this.getProjectPositions(project.getObjectID()));
            }

            boolean isEmployeeAssignmentEnable = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE);
            if (isEmployeeAssignmentEnable) {
                projectItem.setManagers(getManagers());
            }
        }
        return projectItem;
    }

    public HashSet<SelectItem> getManagers() {
        HashSet<SelectItem> items = new HashSet<>();

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setRoles(Constants.PM_CODE);
        fp.setResignedEmployeesIncluded(false);
        fp.setSortField(EmployeeListItem.FIRST_NAME);
        fp.setLimit(200);
        ListResult<EmployeeListItem> managers = this.employeeServiceLocal.getEmployeeList(fp);
        if (managers == null || managers.getList() == null || managers.getList().size() == 0) {
            return new HashSet<>();
        }
        managers.getList().stream()
                .filter(item -> !Constants.EMPLOYEE_STATUS_RESIGNED.equals(item.getStatusCode()))
                .forEach(item -> items.add(new SelectItem(item.getObjectID(), item.getFullName())));
        return items;
    }

    public ProjectPosition[] getProjectPositions(Integer projectID) {
        ArrayList<ProjectPosition> list = new ArrayList<>();
        Map<Integer, ArrayList<ProjectMember>> peMap = new HashMap<>();
        List<EdsProjectEmployee> pemployees = this.projectManager.getProjectInvolvedEmployees(this.projectManager.get(projectID));

        if (pemployees != null && !pemployees.isEmpty()) {
            for (EdsProjectEmployee item : pemployees) {
                Integer positionID = item.getPosition() != null ? item.getPosition().getObjectID() : 0;

                ProjectMember projectMember = new ProjectMember();
                projectMember.setId(item.getEmployeeDepartment().getEmployee().getObjectID());
                projectMember.setName(item.getEmployeeDepartment().getEmployee().getName());
                projectMember.setPositionId(positionID);
                projectMember.setPosititon(item.getPosition() != null ? item.getPosition().getName() : "");
                projectMember.setWageRate(item.getWageRate());
                projectMember.setClientChargeRate(item.getClientChargeRate());
                projectMember.setContractStart(item.getContractStartDate() != null ? new DateNonConvertable(item.getContractStartDate()) : null);
                projectMember.setContractEnd(item.getContractEndDate() != null ? new DateNonConvertable(item.getContractEndDate()) : null);
                projectMember.setProjectEmployeeId(item.getObjectID());
                projectMember.setCreateDate(item.getCreationdate());

                if (item.getEmployeeDepartment().getEmployee() != null) {
                    projectMember.setEmployeeNumber(item.getEmployeeDepartment().getEmployee().getProfile().getEmployeeCode());
                }

                if (peMap.get(positionID) == null) {
                    ArrayList<ProjectMember> al = new ArrayList<>();
                    al.add(projectMember);
                    peMap.put(positionID, al);
                } else {
                    peMap.get(positionID).add(projectMember);
                }
            }
        }

        List<EdsProjectPosition> projectPositions = this.projectManager.getProjectPositions(projectID);

        if (projectPositions != null && !projectPositions.isEmpty()) {
            for (EdsProjectPosition pp : projectPositions) {
                ProjectPosition item = new ProjectPosition();
                item.setObjectID(pp.getObjectID());
                item.setPositionId(pp.getPosition().getObjectID());
                item.setContractStart(new DateNonConvertable(pp.getContractStartDate()));
                item.setContractEnd(pp.getContractEndDate() != null ? new DateNonConvertable(pp.getContractEndDate()) : null);
                item.setUnitPrice(pp.getUnitPrice());
                item.setPriceType(pp.getPriceType());
                item.setOvertimeRate(pp.getOvertimeRate());
                item.setWeekendOvertimeRate(pp.getWeekendOvertimeRate());
                item.setHolidayOvertimeRate(pp.getHolidayOvertimeRate());
                item.setNumberOfWorker(pp.getNumberOfWorker());

                if (peMap.get(item.getPositionId()) != null) {
                    item.setMembers(peMap.get(item.getPositionId()).toArray(new ProjectMember[]{}));
                }
                list.add(item);
            }
        }

        return list.toArray(new ProjectPosition[]{});
    }

    public NumberData generateProjectNumber(Date date, Integer clientId, Integer objectID) {
        EdsUser user = this.userManager.getUser();
        if (user == null) {
            user = this.userManager.get(ServerSecurityContext.getInstance().getStaticUserID());
        }
        if (date != null && user != null) {
            date = user.getUserDate(date);
        }
        EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
        Integer intNumber = this.projectManager.getProjectLastIntNumber();
        if (settings != null && settings.getProjectLastIntNumber() != null && settings.getProjectNumberingFormat() != null && !"".equals(settings.getProjectNumberingFormat()) && settings.getProjectNumberingFormat().contains(Constants.WIDGET_PREFIX)) {
            intNumber = settings.getProjectLastIntNumber();
        }
        String clientCode = null;
        if (clientId != null) {
            List<String> clientcodeList = crmAccountManager.getCrmAccountNumberById(clientId);
            if (clientcodeList != null && clientcodeList.size() > 0) {
                clientCode = crmAccountManager.getCrmAccountNumberById(clientId).get(0);
            }
        }
        if (settings != null && settings.getProjectNumberingFormat() != null) {
            if (objectID != null) {
                String savedNumberFormat = this.projectManager.getSavedNumberformat(objectID);
                return settings.parsNumberDataForEdit(intNumber, savedNumberFormat, settings.getProjectNumberingFormat());
            }
            return settings.parseNumberDataForALL(intNumber, settings.getProjectNumberingFormat(), settings.getDelimetrProject(), date, clientCode, null, "project");
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_PROJ_PREFIX /*true*/);
        }
    }

    @Override
    public KpiTreeInfo[] getBrigadaEmployeesForView(Integer projectID) {
        List<EdsBrigadaEmployee> pemployees = brigadaManager.getEmployeesByBrigada(projectID);
        EdsDepartment department;
        KpiTreeInfo[] employees = null;
        if (pemployees != null) {
            employees = new KpiTreeInfo[pemployees.size()];
            int count = 0;
            for (EdsBrigadaEmployee prEmp : pemployees) {
                department = prEmp.getEmployeeDepartment().getEmployee().getTeam();
                EdsUser emp = prEmp.getEmployeeDepartment().getEmployee();
                employees[count] = new KpiTreeInfo();
                employees[count].setId(emp.getObjectID());
                employees[count].setName(emp.getName());
                employees[count].setEmployeeId(emp.getObjectID());
                employees[count].setDepartmentId(department.getObjectID());
                employees[count].setDepartmentName(department.getName());
                employees[count].setSelected(true);
                count++;
            }
        }
        return employees;
    }

    @Override
    public void updateBrigada(EditProject editProject) throws NumberExistingException {
        EdsBrigada project = brigadaManager.get(editProject.getObjectId());
        NumberData numberData = editProject.getNumberData();

        if ("".equals(project.getNumber())) {
            project.setNumber(null);
        }

        if (project.getNumber() != null && (numberData == null || numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim()))) {
            throw new NumberExistingException("Incorrect project number format.");
        }

        try {
            Date timer = new Date();
            EdsUser user = this.employeeManager.getUser();
            project.setUpdater(user);
            project.setLastUpdateTime(new Date());

            project.setObjectID(editProject.getObjectId());

            String oldprintnumber = project.getIntNumber() != null ? project.getIntNumber().toString() : "0";
            String currentnumber = numberData.getIntNumber() != null ? numberData.getIntNumber().toString() : "0";
            if (numberData != null) {
                project.setNumber("".equals(numberData.getNumberString()) ? null : numberData.getNumberString());
                project.setSavedNumberFormula(editProject.getNumberData().getSavedNumberFormula());
                project.setIntNumber(numberData.getIntNumber());
            }
            if (numberData.getIntNumber() != null && !"".equals(numberData.getIntNumber())) {
                EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
                if (settings != null && !oldprintnumber.equals(currentnumber) && (settings.getProjectLastIntNumber() == null || numberData.getIntNumber() >= settings.getProjectLastIntNumber())) {
                    settings.setProjectLastIntNumber(numberData.getIntNumber() + 1);
                    this.numberingSettingsManager.createOrUpdate(settings);
                }
            }

            project.setName(editProject.getName());
            project.setDescription(editProject.getDescription());
            project.setLastUpdateTime(new Date());
            project.setUpdater(user);
            project.setBillable(editProject.isBillable());
            project.setCustomFields(createBrigadaCustomFields(project.getCustomFields(), editProject.getCustomFieldItems()));
            project.setOwnersId(editProject.getOwnersId());
            if (editProject.getOwnersId() != null && !editProject.getOwnersId().isEmpty()) {
                project.setOwners(userManager.getByIDs(editProject.getOwnersId()));
            }


//            if (editProject.getParentId() != null && editProject.getParentId() != 0) {
//                project.setParent(this.projectManager.get(editProject.getParentId()));
//            }
            boolean newMemberAdded = false;
            boolean memberDeleted = false;
            boolean managerChanged = false;
            boolean backupManagerChanged = true;
            if (editProject.getManagerId() != null && editProject.getManagerId() != 0) {
                if (!project.getManager().getObjectID().equals(editProject.getManagerId())) {
                    EdsEmployee manager = this.employeeManager.get(editProject.getManagerId());
                    project.setManager(manager);
                    managerChanged = true;
                }
            }

            List<Integer> backupManagersBeforeEdit = project.getBackupManagerIDs();
            this.assignBackupMangers(project, editProject.getBackupManagerIDs());
            if (new HashSet<>(backupManagersBeforeEdit).containsAll(project.getBackupManagerIDs()) && project.getBackupManagerIDs().containsAll(backupManagersBeforeEdit)) {
                backupManagerChanged = false;
            }


//            EdsProjectCustomFields edsProjectCustomFields = this.createProjectCustomFields(editProject.getCustomFieldItems());
//            project.setProjectCustomFields(edsProjectCustomFields);

            if (editProject.getStatusId() != null && editProject.getStatusId() != 0) {
                EdsReference status = this.referenceManager.get(editProject.getStatusId());
                project.setStatus(status);
            }

            HashMap<Integer, EdsBrigadaEmployee> peMap = this.brigadaEmployeesManager.getBrigadaEmployeesAsMap(project);

            if (editProject.getMembers() != null && editProject.getMembers().length > 0 && (editProject.getMembers()[0].getId() != null)) {

                List<ProjectMember> newMembers = new ArrayList<>();

                for (int j = 0; j < editProject.getMembers().length; j++) {

                    ProjectMember member = editProject.getMembers()[j];

                    if (peMap.get(member.getProjectEmployeeId()) == null) {
                        newMembers.add(member);
                    } else {
                        EdsBrigadaEmployee pemployee = peMap.get(member.getProjectEmployeeId());
                        peMap.remove(member.getProjectEmployeeId());
                        pemployee.setUnit(member.getUnit());
                        this.brigadaEmployeesManager.update(pemployee);
                    }
                }

                //adding new employee to the project
                if (!newMembers.isEmpty()) {
                    newMemberAdded = true;
                    this.addMembers(editProject.getObjectId(), newMembers.toArray(new ProjectMember[]{}));

                }

                if (!peMap.isEmpty()) {
                    memberDeleted = true;

                    for (EdsBrigadaEmployee pe : peMap.values()) {
                        pe.setDeleted(true);

//                        this.deleteTaskAssignees(pe);
                    }
                }

                System.out.println("-->> 1. Project Members Took: " + ((new Date()).getTime() - timer.getTime()));

                timer = new Date();


            }
//            if (editProject.getProjectPositions() != null && editProject.getProjectPositions().length > 0 && editProject.getMembers() != null && editProject.getMembers().length == 0) {
//                List<EdsProjectEmployee> projectEmployee = this.projectManager.getProjectInvolvedEmployees(this.projectManager.get(editProject.getObjectId()));
//
//                this.updateProjectPositions(editProject, project, user);
//
//                for (EdsProjectEmployee employees : projectEmployee) {
//                    employees.setDeleted(true);
//                }
//            }
//
//            if (editProject.getProjectPositions() != null && editProject.getProjectPositions().length == 0) {
//                List<EdsProjectPosition> edsProjectPositions = this.projectManager.getProjectPositions(editProject.getObjectId());
//                for (EdsProjectPosition projectPosition : edsProjectPositions) {
//                    projectPosition.setDeleted(true);
//                    projectPosition.setUpdatedDate(new Date());
//                    projectPosition.setUpdater(user);
//                }
//            }

            timer = new Date();
//            if (memberDeleted || managerChanged || backupManagerChanged || (newMemberAdded && editProject.isCopyNewEmployeesToProjectTasks())) {
//                List<EdsTask> tasks = this.taskManager.getProjectTasks(project);
//                int k = 0;
//                for (EdsTask itask : tasks) {
//                    EdsTask task = this.taskManager.get(itask.getObjectID());
//                    this.taskRbacManager.addRbacEntries(task);
//                    if (!project.isSolrSensitiveFieldsChanged()) {// if project changed some filed projectsolrsensitivedatachangelistener will reindex all task anyway
//                        // so there is no need to reindex it
//                        this.baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);
//                    }
//
//                    if (k++ >= 10) {
//                        k = 0;
//                        this.taskRbacManager.flushAndClear();
//                    }
//                }
//            }

//            if (isProjectNameChanged) {
//                this.baseEventPostProcessor.registerEvent(AccountingProjectSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
//            }

            //Reindex to projectFolderRbac
//            this.reIndexProjectDocuments(project, user);
//            this.baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
//            if (project.isSolrSensitiveFieldsChanged()) {
//                this.baseEventPostProcessor.registerEvent(ProjectSolrSensitiveDataChangeListener.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
//            }

//            if (project.getObjectID() != null && editProject.isRelationChanged()) {
//                this.allInOneService.saveRelations(RelationItem.TYPE_PROJECT, project.getObjectID(),
//                        project.getName(), editProject.getRelations());
//            }
//
//            this.saveProjectReminder(editProject.getObjectId(), user.getCompany(), editProject.getReminders());
//
//            this.updateProjectStatus(project);

//            if (editProject.getAttachments() != null && editProject.getAttachments().length > 0) {
//                //Create a new folder related to EdsProject.
//                this.commonServiceLocal.createProjectFolder(project.getObjectID());
//
//                // ---- with Document Management logic ---------------------------
//                this.saveProjectAttachments(editProject.getAttachments(), project);
//            }
//
//            EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, project, user);
//            workflowEvent.setEntityType(RelationItem.TYPE_PROJECT);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }


    private EdsBrigadaCustomFields createBrigadaCustomFields(EdsBrigadaCustomFields
                                                                     edsBrigadaCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsBrigadaCustomFields == null) {
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
                edsBrigadaCustomFields = new EdsBrigadaCustomFields();
                brigadaCFManager.create(edsBrigadaCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsBrigadaCustomFields, customFieldItems);
            return edsBrigadaCustomFields;
        }
        return null;
    }

    public void addMembers(Integer projectId, ProjectMember[] members) {

        EdsBrigada project = this.brigadaManager.get(projectId);
        for (ProjectMember member : members) {
            EdsEmployee employee = this.employeeManager.get(member.getId());
            EdsEmployeeDepartment employeeDepartment = employee.getEmployeeTeam();

            EdsBrigadaEmployee existingPE;


            existingPE = this.brigadaEmployeesManager.getBrigadaEmployee(employee, project);


            if (employeeDepartment != null && existingPE == null) {
                EdsBrigadaEmployee pe = new EdsBrigadaEmployee(employeeDepartment, project);

                if (member.getPositionId() != null) {
                    EdsPosition position = this.positionManager.get(member.getPositionId());
                    pe.setPosition(position);
                    pe.setContractStartDate(member.getContractStart().getNonConvertedDate());
                    pe.setContractEndDate(member.getContractEnd() != null ? member.getContractEnd().getNonConvertedDate() : null);

                }


                this.brigadaEmployeesManager.create(pe);

            }
        }
    }

    @Override
    public Integer saveBrigada(ProjectSingleItem item) throws NumberExistingException {
//        boolean isEmployeeAssignmentEnable = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.EMPLOYEE_ASSIGNMENT_ENABLE);
//        boolean isEnableMultiClientToProject = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT);

        NumberData numberData = item.getNumberData();
        if (numberData == null || numberData.getNumberString() == null || "".equals(numberData.getNumberString().trim())/* || EdsNumberingSettings.validateNumberData(numberData) == null*/) {
            throw new NumberExistingException("Incorrect brigada number format.");
        }

        EdsUser user = this.employeeManager.getUser();
        EdsEmployee manager = this.employeeManager.get(item.getManagerId());
        EdsBrigada project = new EdsBrigada();
        project.setCreator(user);
        project.setUpdater(user);
        project.setCreationTime(new Date());
        project.setLastUpdateTime(new Date());
        project.setName(item.getName());
        project.setDescription(item.getDescription());

        project.setManager(manager);
        project.setCreator(user);
        project.setBillable(item.isBillable());
        project.setEmployeeAssignment(item.getEmployeeAssignment());
        project.setCustomFields(createBrigadaCustomFields(project.getCustomFields(), item.getCustomFieldItems()));
        project.setOwnersId(item.getOwnersId());
        if (item.getOwnersId() != null && !item.getOwnersId().isEmpty()) {
            project.setOwners(userManager.getByIDs(item.getOwnersId()));
        }

        if (item.getParentId() != null) {
            project.setParent(this.projectManager.get(item.getParentId()));
        }

        this.assignBackupMangers(project, item.getBackupManagerIDs());
        project.setNumber(numberData.getNumberString());

        EdsReference status = this.referenceManager.get(item.getStatusId());
        project.setStatus(status);
        project.setIntNumber(numberData.getIntNumber());
        project.setSavedNumberFormula(item.getNumberData().getSavedNumberFormula());
        if (numberData.getIntNumber() != null && !"".equals(numberData.getIntNumber())) {
            EdsNumberingSettings settings = this.numberingSettingsManager.getNumberingSetting();
            if (settings != null && (settings.getProjectLastIntNumber() == null || numberData.getIntNumber() >= settings.getProjectLastIntNumber())) {
                settings.setProjectLastIntNumber(numberData.getIntNumber() + 1);
                this.numberingSettingsManager.createOrUpdate(settings);
            }
        }

//        EdsProjectCustomFields edsProjectCustomFields = this.createProjectCustomFields(item.getCustomFieldItems());
//        project.setProjectCustomFields(edsProjectCustomFields);

//        if (item.getProjectSource() != null) {
//            project.setProjectSource(item.getProjectSource());
//        }

        this.brigadaManager.create(project);

//        this.saveProjectReminder(project.getObjectID(), user.getCompany(), item.getReminder());


//        EdsBusinessEvent projectBusinessEvent = this.baseEventPostProcessor.registerEvent(ProjectEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, project, user);
//        this.baseEventPostProcessor.registerEvent(ProjectManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, project, user);
//        for (EdsEmployee backupManager : project.getBackupManagers()) {
//            this.baseEventPostProcessor.registerEvent(ProjectBackupManagerEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, project, user, backupManager);
//        }


        if (item.getProjectMembers() != null) {
            for (ProjectMember member : item.getProjectMembers()) {
                EdsEmployee employee = this.employeeManager.get(member.getId());
                EdsBrigadaEmployee pe = new EdsBrigadaEmployee();
                pe.setProject(project);
                pe.setEmployeeDepartment(employee.getEmployeeTeam());
                pe.setUnit(member.getUnit());
                if (member.getPositionId() != null) {
                    EdsPosition position = this.positionManager.get(member.getPositionId());
                    pe.setPosition(position);
                    pe.setCode(employee.getEmployee().getProfile().getEmployeeCode());
                    pe.setContractStartDate(member.getContractStart() != null ? member.getContractStart().getNonConvertedDate() : null);
                    pe.setContractEndDate(member.getContractEnd() != null ? member.getContractEnd().getNonConvertedDate() : null);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(pe.getContractEndDate() != null ? (Date) pe.getContractEndDate().clone() : (Date) pe.getContractStartDate().clone());
                    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
                }

                this.brigadaEmployeesManager.create(pe);
//                if (!pe.getEmployeeDepartment().getEmployee().equals(project.getManager())
//                        && !project.isUserBackupManager(pe.getEmployeeDepartment().getEmployee().getObjectID())
//                        && !pe.getEmployeeDepartment().getEmployee().getAccountStatus().getCode().equals(Constants.EMPLOYEE_STATUS_NO_ACCCESS)) {
//                    this.baseEventPostProcessor.registerEvent(ProjectEmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, pe, user);
//
//                }
            }
        }

        this.roleManager.addRole(manager, Constants.PM);


        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsProject.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.ADD);
        if (project.getObjectID() != null) {
            kpiLog.setEntityId(project.getObjectID());
        }
        return project.getObjectID();
    }

    @Override
    public ProjectViewItem viewBrigada(Integer objectID) {


        EdsBrigada project = brigadaManager.get(objectID);
        ProjectViewItem projectViewItem = new ProjectViewItem();
        EdsUser user = this.employeeManager.getUser();
        projectViewItem.setObjectID(objectID);
        EdsProject defaultProject = user.getCompany().getDefaultProject();
        EdsProject crmProject = this.projectManager.getCrmProject();

        if (defaultProject != null) {
            projectViewItem.setDefaultProjectID(defaultProject.getObjectID());
        }
        if (crmProject != null) {
            projectViewItem.setCrmProjectID(crmProject.getObjectID());
        }

        projectViewItem.setName(project.getName());
        if (project.getNumber() != null) {
            NumberData numberData = new NumberData();
            numberData.setNumberString(project.getNumber());
            projectViewItem.setNumberData(numberData);
        }

        projectViewItem.setDescription(project.getDescription());
        ArrayList<SelectItem> backupMangers = new ArrayList<>();
        for (EdsEmployee backupManager : project.getBackupManagers()) {
            SelectItem item = new SelectItem();
            item.setId(backupManager.getObjectID());
            item.setName(backupManager.getName());
            backupMangers.add(item);
        }
        backupMangers.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        projectViewItem.setBackupManagers(backupMangers);
        SelectItem[] statusesCount = this.projectManager.getTasksCountByProject(project.getObjectID());
        for (SelectItem item : statusesCount) {
            String status = item.getName();
            int count = item.getId() != null ? item.getId() : 0;
            switch (status) {
                case EdsTask.CANCELLED -> projectViewItem.setCancelledTasks(count);
                case EdsTask.IN_PROGRESS -> projectViewItem.setInProgressTasks(count);
                case EdsTask.NOT_STARTED -> projectViewItem.setNotStartedTasks(count);
                case EdsTask.COMPLETED -> projectViewItem.setCompletedTasks(count);
                case EdsTask.WAITING_FOR_SOMEONE_ELSE -> projectViewItem.setWaitingTasks(count);
                case EdsTask.CLOSED -> projectViewItem.setClosedTasks(count);
            }
        }
        if (project.getStatus() != null) {
            projectViewItem.setStatus(this.referenceWfmMessageSource.localize(project.getStatus().getCode(), project.getStatus().getName()));
            projectViewItem.setStatusID(project.getStatus().getObjectID());
            projectViewItem.setStatusCode(project.getStatus().getCode());
        } else {
            projectViewItem.setStatus(this.commonLocalizer.localize("notAvailable", "N/A"));
        }
        projectViewItem.setManager(project.getManager() != null ? project.getManager().getFullName() : this.commonLocalizer.localize("notAvailable", "N/A"));
        projectViewItem.setManagerId(project.getManager() != null ? project.getManager().getObjectID() : null);
        projectViewItem.setCreator(project.getCreator() != null ? project.getCreator().getFullName() : this.commonLocalizer.localize("notAvailable", "N/A"));
        projectViewItem.setCreatorID(project.getCreator() != null ? project.getCreator().getObjectID() : null);
        projectViewItem.setCreationDate(project.getCreationTime());
        projectViewItem.setLastUpdaterName(project.getUpdater() != null ? project.getUpdater().getFullName() : this.commonLocalizer.localize("notAvailable", "N/A"));
        projectViewItem.setLastUpdateTime(project.getLastUpdateTime());
        projectViewItem.setEncryptedID(EncryptionHelper.encryptURL("project/" + projectViewItem.getObjectID()));
        projectViewItem.setEmployeeAssignment(project.getEmployeeAssignment());
        projectViewItem.setBillable(project.getBillable());
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.BrigadaList);
        projectViewItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(project != null ? project.getCustomFields() : null, customFieldsItems));
        ArrayList<SelectItem> owners = new ArrayList<>();
        if (project.getOwnersId() != null && !project.getOwnersId().isEmpty()) {
            for (EdsEmployee employeesById : employeeManager.getEmployeesByIds(project.getOwnersId())) {
                owners.add(new SelectItem(employeesById.getObjectID(), employeesById.getFormmattedName()));
            }
        }
        projectViewItem.setOwners(owners);

        List<EdsBrigadaEmployee> projectEmployeeList = this.brigadaEmployeesManager.getBrigadaEmployees(project);

        if (projectEmployeeList != null && !projectEmployeeList.isEmpty()) {
            ArrayList<PositionsSelectItem> positionsSelectItemList = new ArrayList<>();

            for (EdsBrigadaEmployee projectEmployee : projectEmployeeList) {
                EdsEmployee employee = projectEmployee.getEmployeeDepartment().getEmployee();

                PositionsSelectItem positionsSelectItem = new PositionsSelectItem();
                positionsSelectItem.setId(employee.getObjectID());
                positionsSelectItem.setName(employee.getName());
                if (employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null) {
                    positionsSelectItem.setEmployeeNumber(employee.getProfile().getEmployeeCode());
                }
                positionsSelectItem.setStartDate(projectEmployee.getContractStartDate() != null ? new DateNonConvertable(projectEmployee.getContractStartDate()) : null);
                positionsSelectItem.setEndDate(projectEmployee.getContractEndDate() != null ? new DateNonConvertable(projectEmployee.getContractEndDate()) : null);

                if (employee.getEmployeeDepartment() != null && employee.getEmployeeDepartment().getTeam() != null) {
                    positionsSelectItem.setDepartmentName(employee.getEmployeeDepartment().getTeam().getName());
                }

                if (employee.getPosition() != null) {
                    positionsSelectItem.setPositionName(employee.getPosition().getName());

                }

                if (projectEmployee.getUnit() != null) {
                    positionsSelectItem.setLabel(projectEmployee.getUnit());
                }

                positionsSelectItemList.add(positionsSelectItem);
            }

            projectViewItem.setProjectEmployees(positionsSelectItemList.toArray(new PositionsSelectItem[]{}));
        }


        if (project.getProjectLocation() != null) {
            EdsLocation location = project.getProjectLocation();
            projectViewItem.setProjectLocation(location.getCountry().getName() + "," + location.getCity());
            projectViewItem.setLocationID(location.getObjectID());
        } else {
            projectViewItem.setProjectLocation(this.commonLocalizer.localize("notAvailable", "N/A"));
        }
        projectViewItem.setProjectAttachments(new FileResource[0]);

        projectViewItem.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(RelationItem.TYPE_PROJECT, project.getObjectID())));
//        projectViewItem.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(project.getProjectCustomFields(), this.commonService.getCompanyCustomFields(ViewName.Project)));
        projectViewItem.setSupplier(user.hasRole(Constants.SUPPLIER));
        projectViewItem.setTemplates(getBrigadaPdfTemplates(PdfReferenceCodeNameEnum.BRIGADA.name()).getItems());
        return projectViewItem;
    }

    @Transactional
    public void updateApprove(ShiftItem data) {
        EdsShift shift = shiftManager.get(data.getId());
        EdsUser user = userManager.getUser();

        //updating status
        EdsReference referenceStatus = referenceManager.findReference(Constants.SHIFT_STATUS, data.getStatusCode());
        if (!SHIFT_APPROVED.equals(data.getStatusCode())) {
            shift.setOverallStatus(referenceStatus);
        } else if (SHIFT_APPROVED.equals(data.getStatusCode()) && shift.getOverallStatus() != null && SHIFT_DRAFT.equals(shift.getOverallStatus().getCode())) {
            shift.setOverallStatus(referenceManager.findReference(Constants.SHIFT_STATUS, Constants.SHIFT_SUBMITTED));
        }
        shift.updateStatus(referenceStatus);
        shiftManager.update(shift);

        if (!SHIFT_APPROVED.equals(data.getStatusCode()) && !SHIFT_DRAFT.equals(data.getStatusCode())) {
            baseEventsPostProcessor.registerEvent(ShiftEventListenerImpl.TYPE, MyUpdateItem.EDIT, shift, user);
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), shift, user);
        workflowEvent.setEntityType(RelationItem.TYPE_SHIFT);

        if (data.getStatusCode().equals(SHIFT_SUBMITTED)) {
            baseEventsPostProcessor.registerEvent(ShiftEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, shift, user);
        }
        if (data.getStatusCode().equals(SHIFT_APPROVED)) {
            baseEventPostProcessor.registerEvent(ShiftEventListenerImpl.TYPE, ShiftEventListenerImpl.SHIFT_APPROVED, shift, user);
        }
    }

    @Override
    public ArrayList<RelationItem> getEmployeeRelations(Integer empId) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MMMM");
//        List<EdsCustomItemTable> customItemTables = customFormManager.getCustomFormsByEmployeeId(empId);
//        List<EdsCustomFormCustomFields> customFields = customFormManager.getCustomFormsCustomFieldsByEmployeeId(empId);
        List<EdsShift> shifts = shiftManager.getShiftsTypeTeamByEmployeeId(empId);
        shifts.addAll(shiftManager.getShiftsTypeEmployeeByEmployeeId(empId));
        List<RelationItem> allShifts = shifts.stream()
                .map(shift -> {
                    RelationItem relationItem = new RelationItem();
                    relationItem.setFromID(empId);
                    relationItem.setFromType("EMPLOYEE");
                    relationItem.setToType("SHIFT");
                    relationItem.setFromName(String.valueOf(empId));
                    relationItem.setToName(shift.getShiftCode() + "(" + simpleDateFormat.format(shift.getPeriod()) + ")");
                    relationItem.setToID(shift.getObjectID());
                    return relationItem;
                })
                .collect(Collectors.toList());

//        for (int i = 0; i < customItemTables.size(); i++) {
//            EdsCustomItemTable customItemTable = customItemTables.get(i);
//            EdsCustomFormCustomFields customFormCustomFields = customFields.get(i);
//            RelationItem relationItem = new RelationItem();
//            relationItem.setFromID(empId);
//            relationItem.setFromType("EMPLOYEE");
//            relationItem.setToType(customItemTable.getFormItem().getCustomForm().getFormID());
//            relationItem.setFromName(String.valueOf(empId));
//            relationItem.setToName(customFormCustomFields.getStringValue17() + "("+simpleDateFormat.format(customFormCustomFields.getDateValue1()) + ")");
//            relationItem.setToID(customItemTable.getFormItem().getObjectID());
//            allShifts.add(relationItem);
//        }
        allShifts.addAll(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_EMPLOYEE, empId)));

        return (ArrayList<RelationItem>) allShifts.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateRotationApprove(RotationItem data) {
        EdsRotation shift = rotationManager.get(data.getId());
        EdsUser user = userManager.getUser();

        //updating status
        EdsReference referenceStatus = referenceManager.findReference(Constants.ROTATION_STATUS, data.getStatusCode());
        if (!ROTATION_APPROVED.equals(data.getStatusCode())) {
            shift.setOverallStatus(referenceStatus);
        } else if (ROTATION_APPROVED.equals(data.getStatusCode()) && shift.getOverallStatus() != null && ROTATION_DRAFT.equals(shift.getOverallStatus().getCode())) {
            shift.setOverallStatus(referenceManager.findReference(Constants.ROTATION_STATUS, Constants.ROTATION_SUBMITTED));
        }
        shift.updateStatus(referenceStatus);
        rotationManager.update(shift);

        if (!ROTATION_APPROVED.equals(data.getStatusCode()) && !ROTATION_DRAFT.equals(data.getStatusCode())) {
            baseEventsPostProcessor.registerEvent(RotationEventListenerImpl.TYPE, MyUpdateItem.EDIT, shift, userManager.getUser());
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), shift, user);
        workflowEvent.setEntityType(RelationItem.TYPE_ROTATION);

        if (data.getStatusCode().equals(ROTATION_SUBMITTED)) {
            baseEventsPostProcessor.registerEvent(RotationEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, shift, userManager.getUser());
        }
    }

    @Transactional
    public void updateGroupPlacementApprove(GroupPlacementItem data) {
        EdsGroupPlacement placement = groupPlacementManager.get(data.getId());
        EdsUser user = userManager.getUser();

        //updating status
        EdsReference referenceStatus = referenceManager.findReference(Constants.GROUP_PLACEMENT_STATUS, data.getStatusCode());
        if (!GROUP_PLACEMENT_APPROVED.equals(data.getStatusCode())) {
            placement.setOverallStatus(referenceStatus);
        } else if (GROUP_PLACEMENT_APPROVED.equals(data.getStatusCode()) && placement.getOverallStatus() != null && GROUP_PLACEMENT_DRAFT.equals(placement.getOverallStatus().getCode())) {
            placement.setOverallStatus(referenceManager.findReference(Constants.GROUP_PLACEMENT_STATUS, Constants.GROUP_PLACEMENT_SUBMITTED));
        }
        placement.updateStatus(referenceStatus);
        groupPlacementManager.update(placement);

        if (!GROUP_PLACEMENT_APPROVED.equals(data.getStatusCode()) && !GROUP_PLACEMENT_DRAFT.equals(data.getStatusCode())) {
            baseEventsPostProcessor.registerEvent(GroupPlacementEventListenerImpl.TYPE, MyUpdateItem.EDIT, placement, user);
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), placement, user);
        workflowEvent.setEntityType(RelationItem.TYPE_GROUP_PLACEMENT);

        if (data.getStatusCode().equals(GROUP_PLACEMENT_SUBMITTED)) {
            baseEventsPostProcessor.registerEvent(GroupPlacementEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, placement, user);
        }
        if (data.getStatusCode().equals(GROUP_PLACEMENT_APPROVED)) {
            baseEventPostProcessor.registerEvent(GroupPlacementEventListenerImpl.TYPE, GROUP_PLACEMENT_APPROVED, placement, user);
        }


    }

    @Override
    public SelectItem getLocationByEmployeeId(Integer employeeId) {
        return employeeManager.get(employeeId).getLocation() != null ? employeeManager.get(employeeId).getLocation().getAsSelectItem() : null;
    }

    @Override
    public SelectItem getLocationByDepartmentId(Integer departmentId) {
        return departmentManager.get(departmentId).getLocation() != null ? departmentManager.get(departmentId).getLocation().getAsSelectItem() : null;
    }

    @Override
    public Integer getPositionsSizeByNameForValidation(PositionItem item) {
        return positionManager.getPositionsSizeByNameForValidation(item.getName(), item.getLocation() != null ? item.getLocation().getId() : null, item.getObjectID());
    }

    @Override
    public LinkedHashMap<SelectItem, ArrayList<PositionItem>> getPositionsByLocationId(Integer positionId) {
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Positions);
        LinkedHashMap<SelectItem, ArrayList<PositionItem>> departmentToPositionItemsMap =
                positionManager.getPostionsByLocation(positionId).stream()
                        .map((position) -> {
                            PositionItem positionItem = position.getRPC();
                            positionItem.setEmployeesData(getPositionEmployees(positionItem.getObjectID()));
                            positionItem.setCategory(position.getCustomFields() != null ? String.valueOf(position.getCustomFields().getDoubleValue1()) : null);
                            positionItem.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(position.getCustomFields(), customFieldsItems));
                            positionItem.setType((position.getType() != null) ? position.getType().getAsSelectItem() : new SelectItem());
                            positionItem.setResponsibility(position.getResponsibility());
                            positionItem.setDescription(position.getDescription());
                            positionItem.setJobRequirements(position.getJobrequirements());
                            return positionItem;
                        })
                        .filter(positionItem -> positionItem.getDepartment() != null)
                        .collect(Collectors.groupingBy(
                                PositionItem::getDepartment,
                                LinkedHashMap::new,
                                Collectors.toCollection(ArrayList::new)));


        return departmentToPositionItemsMap;
    }

    @Override
    public NumberData generateShiftCode() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = shiftManager.getShiftLastIntNumber();
        if (intNumber == null) {
            intNumber = 0;
        }
        if (settings != null && settings.getShiftNumberingFormat() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getShiftNumberingFormat(), settings.getDelimetrShiftNumberingFormat(), null, null, null, "");
            numberData.setDelimiter(settings.getDelimetrShiftNumberingFormat());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_SHIFT_PREFIX /*true*/);
        }
    }

    @Override
    public NumberData generateRotationNumber() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = rotationManager.getRotationLastIntNumber();
        if (intNumber == null) {
            intNumber = 0;
        }
        if (settings != null && settings.getRotationNumberingDate() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getRotationNumberingDate(), settings.getDelimetrRotationNumberingFormat(), null, null, null, "");
            numberData.setDelimiter(settings.getDelimetrRotationNumberingFormat());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_ROTATION_PREFIX /*true*/);
        }
    }

    @Override
    public NumberData generateBrigadaCode() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = brigadaManager.getBrigadaLastIntNumber();
        if (intNumber == null) {
            intNumber = 0;
        }
        if (settings != null && settings.getShiftNumberingFormat() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getBrigadaNumberingFormat(), settings.getDelimetrBrigadaNumberingFormat(), null, null, null, "");
            numberData.setDelimiter(settings.getDelimetrBrigadaNumberingFormat());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_BRIGADA_PREFIX /*true*/);
        }
    }


    public CustomFormItemPdfTemplateList getAttendanceReportPDFTemplates() {
        List<EdsCompanyPdfTemplate> templates = companyPdfTemplateManager.getCompanyPDFTemplatesByType(PdfReferenceCodeNameEnum.ATTENDANCE_REPORT.name(), false);
        SelectItem[] items = new SelectItem[templates.size()];
        int i = 0;
        Integer defaultTemplateID = -1;
        for (EdsCompanyPdfTemplate t : templates) {
            items[i] = new SelectItem(t.getObjectID(), t.getName());
            if (t.isDefaultTemplate()) {
                defaultTemplateID = t.getObjectID();
            }
            i++;
        }
        return new CustomFormItemPdfTemplateList(items, defaultTemplateID);
    }

    public CustomFormItemPdfTemplateList getTerminalAttendancePDFTemplates() {
        List<EdsCompanyPdfTemplate> templates = companyPdfTemplateManager.getCompanyPDFTemplatesByType(PdfReferenceCodeNameEnum.TERMINAL_ATTENDANCE.name(), false);
        SelectItem[] items = new SelectItem[templates.size()];
        int i = 0;
        Integer defaultTemplateID = -1;
        for (EdsCompanyPdfTemplate t : templates) {
            items[i] = new SelectItem(t.getObjectID(), t.getName());
            if (t.isDefaultTemplate()) {
                defaultTemplateID = t.getObjectID();
            }
            i++;
        }
        return new CustomFormItemPdfTemplateList(items, defaultTemplateID);
    }

    private CustomFormItemPdfTemplateList getBrigadaPdfTemplates(String type) {
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

    private void assignBackupMangers(EdsBrigada project, List<Integer> backupManagers) {
        int index = 1;
        project.setBackupManagersBeforeEdit(project.getBackupManagerIDs());
        if (backupManagers == null) {
            backupManagers = new ArrayList<>();
        }
        for (Integer backupManagerID : backupManagers) {
            EdsEmployee backupManager = this.employeeManager.get(backupManagerID);
            switch (index) {
                case 1 -> project.setBackupManager(backupManager);
                case 2 -> project.setBackupManager2(backupManager);
                case 3 -> project.setBackupManager3(backupManager);
                case 4 -> project.setBackupManager4(backupManager);
                case 5 -> project.setBackupManager5(backupManager);
                case 6 -> project.setBackupManager6(backupManager);
                case 7 -> project.setBackupManager7(backupManager);
                case 8 -> project.setBackupManager8(backupManager);
                case 9 -> project.setBackupManager9(backupManager);
                case 10 -> project.setBackupManager10(backupManager);
            }
            this.roleManager.addRole(backupManager, Constants.PM);
            index++;
        }
        project.clearProjectManagers(backupManagers.size());
    }


    public Integer createCertificateHistory(Integer certificateId, HistoryListItem hisItem) {
        if (certificateId != null && hisItem != null) {
            EdsUser user = userManager.getUser();
            if (user instanceof EdsEmployee) {
                user = userManager.get(user.getObjectID());
            }
            EdsCertificateOfEmployeeNote certificateHistory = new EdsCertificateOfEmployeeNote();
            certificateHistory.setCertificateOfEmployment(certificateOfEmploymentManager.get(certificateId));
            certificateHistory.setCreationDate(new Date());
            certificateHistory.setUser(user);
            certificateHistory.setSuperUser(ServerUtils.isSuperUser());
            certificateHistory.setText(hisItem.getComment());

            certificateOfEmploymentNoteManager.create(certificateHistory);
            return certificateHistory.getObjectID();
        }
        return null;
    }

    public List<HistoryNote> loadCertificateHistory(Integer objectId) {
        List<EdsCertificateOfEmployeeNote> historyList = certificateOfEmploymentNoteManager.getComments(objectId);
        if (historyList == null) {
            historyList = new ArrayList<>();
        }

        List<HistoryNote> noteItemsList = new ArrayList<>();
        for (EdsCertificateOfEmployeeNote item : historyList) {
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

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CertificateItem getCertificateData(Integer objectId, String formType, Integer convertFormId) {
        CertificateItem item = new CertificateItem();

        item.setCertificateNumber(this.certificateOfEmploymentManager.getCertificateNumber());
        final Integer currentUserId = this.userManager.getUser().getObjectID();
        final EdsEmployee edsEmployee = this.employeeManager.get(currentUserId);
        if (edsEmployee != null) {
            item.setCurrentUserID(edsEmployee.getObjectID());
            final String code = edsEmployee.getProfile() != null ? edsEmployee.getProfile().getEmployeeCode() : null;
            final String employeeName = (code != null && !"".equals(code) ? code + " - " : "") + edsEmployee.getName();
            item.setCurrentUserName(employeeName + this.referenceWfmMessageSource.localize("mySelf", " (" + Constants.MYSELF + ")"));
        }
        //set Certificate types
        item.setTypes(this.getCertificateTypes());

        if (formType != null && convertFormId != null) {
            item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, commonService.getCompanyCustomFields(ViewName.Certificates)));
            if (RelationItem.TYPE_LEAVE_REQUEST.equals(formType)) {

                EdsFormProperty formProperties = formPropertyManager.getByFormID(LayoutRPC.CERTIFICATE_OF_EMPLOYMENT_FORM);

                Gson gson = new Gson();
                FormProperty[] fields = gson.fromJson(formProperties.getSettingsJSONData(), FormProperty[].class);

                StatisticsLeaveRequest leaveRequestLisItem = availabilityService.getLeaveRequest(convertFormId);


                if (leaveRequestLisItem != null && leaveRequestLisItem.getCustomFields() != null) {
                    for (CompanyCustomFieldItem companyCustomFieldItem : leaveRequestLisItem.getCustomFields()) {
                        convertCertificateCF(item, fields, companyCustomFieldItem);
                    }
                }

                if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                    for (CompanyCustomFieldItem certificateCustomFields : item.getCustomFieldItems()) {
                        convertLeRequestFieldstoCertificateCF(certificateCustomFields, leaveRequestLisItem);
                    }
                }

                if (leaveRequestLisItem != null) {
                    if (leaveRequestLisItem.getEmployee() != null) {
                        final TypeItem typeItem = new TypeItem();
                        typeItem.setId(leaveRequestLisItem.getEmployee() != null ? leaveRequestLisItem.getEmployeeId() : null);
                        typeItem.setName(leaveRequestLisItem.getEmployee() != null ? leaveRequestLisItem.getEmployee() : "");
                        item.setEmployee(typeItem);
                    }
                    if (leaveRequestLisItem.getNumberData() != null) {
                        item.setCertificateNumber(leaveRequestLisItem.getNumberData());
                    }
                    if (leaveRequestLisItem.getApprovers() != null) {
                        item.setApprovers(leaveRequestLisItem.getApprovers());
                    }
                }
            }

        }
        return item;
    }


    private void convertLeRequestFieldstoCertificateCF(CompanyCustomFieldItem companyCustomFieldItem, StatisticsLeaveRequest leaveRequestLisItem) {
        switch (companyCustomFieldItem.getAliasName()) {
            case "EMPLOYEE" -> {
                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(companyCustomFieldItem.getLookUpTypeEnum()) && leaveRequestLisItem.getType() != null) {
                    companyCustomFieldItem.setSelectedId(leaveRequestLisItem.getTypeId());
                    companyCustomFieldItem.setFieldStringValue(leaveRequestLisItem.getType());
                }
            }
            case "LEAVE_REQUEST_NUMBER" -> {
                if (UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) && leaveRequestLisItem.getNumberData() != null) {
                    companyCustomFieldItem.setFieldStringValue(leaveRequestLisItem.getNumberData().getNumberFormat());
                }
            }
            case "REASON" -> {
                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType()) && leaveRequestLisItem.getType() != null) {
                    companyCustomFieldItem.setSelectedId(leaveRequestLisItem.getTypeId());
                    companyCustomFieldItem.setFieldStringValue(leaveRequestLisItem.getType());
                }
            }
            case "DATEPERIOD" -> {
                if (UI_TYPE_DATEPICKER.equals(companyCustomFieldItem.getUiType()) || DATA_TYPE_DATE.equals(companyCustomFieldItem.getUiType()) && leaveRequestLisItem.getStartDDate() != null && leaveRequestLisItem.getEndDDate() != null) {
                    companyCustomFieldItem.setFieldDateNonConvertedValue(leaveRequestLisItem.getStartDDate());
                    companyCustomFieldItem.setFieldDateNonConvertedValue(leaveRequestLisItem.getEndDDate());
                }
            }
            case "DESCRIPTION" -> {
                if (UI_TYPE_TEXTBOX.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_TEXTAREA.equals(companyCustomFieldItem.getUiType()) || (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(companyCustomFieldItem.getUiType()) && leaveRequestLisItem.getDescription() != null)) {
                    companyCustomFieldItem.setFieldStringValue(leaveRequestLisItem.getDescription());
                }
            }
            case "TYPE", "TAKELEAVETYPE" -> {
                if (UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType()) && leaveRequestLisItem.getType() != null) {
                    companyCustomFieldItem.setFieldStringValue(leaveRequestLisItem.getType());
                }
            }
        }
    }


    private void convertCertificateCF(CertificateItem item, FormProperty[] fields, CompanyCustomFieldItem companyCustomFieldItem) {
        if (companyCustomFieldItem != null) {
            for (FormProperty formProperty1 : fields) {
                if (formProperty1 != null) {
                    if (companyCustomFieldItem.getAliasName().equals(formProperty1.getAliasName())) {
                        switch (formProperty1.getCode()) {
                            case "CERTIFICATE_TYPE":
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) || UI_TYPE_DROPDOWN.equals(companyCustomFieldItem.getUiType())) {
                                    item.setCertificateType(companyCustomFieldItem.getEntityType());  //tekshirish
                                }
                                break;
                            case "APPROVER":
                                if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
//                                item.setApprovers(companyCustomFieldItem.getEntityType());
//                                item.setApprovers(companyCustomFieldItem.getFieldStringValue());
                                }

                                break;
                            case "DOCUMENT_CELL_TREE":
//                            if (UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType()) )  {
//                              final   EmployeeDocumentDragView documentDragView =  new EmployeeDocumentDragView();
//                              documentDragView.getSelectedDocuments();
//                              item.setDucumentList(companyCustomFieldItem.getLookUpTypeEnum(documentDragView));
//                            }
                                // ATTechment
                                break;
                        }
                    }
                }
            }

            if (item.getCustomFieldItems() != null && item.getCustomFieldItems().size() > 0) {
                for (CompanyCustomFieldItem certificateCustomFields : item.getCustomFieldItems()) {
                    if (companyCustomFieldItem.getAliasName().equals(certificateCustomFields.getAliasName()) && companyCustomFieldItem.getUiType().equals(certificateCustomFields.getUiType()) && companyCustomFieldItem.getDataType().equals(certificateCustomFields.getDataType())) {
                        if (UI_TYPE_LOOKUP.equals(certificateCustomFields.getUiType())) {
                            if (certificateCustomFields.getLookUpTypeEnum().equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                                certificateCustomFields.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                                certificateCustomFields.setSelectedId(companyCustomFieldItem.getSelectedId());
                                certificateCustomFields.setItem(companyCustomFieldItem.getItem());
                            }
                        } else {
                            certificateCustomFields.setFieldStringValue(companyCustomFieldItem.getFieldStringValue());
                            certificateCustomFields.setSelectedId(companyCustomFieldItem.getSelectedId());
                            certificateCustomFields.setItem(companyCustomFieldItem.getItem());
                            certificateCustomFields.setFieldDateNonConvertedValue(companyCustomFieldItem.getFieldDateNonConvertedValue());
                        }
                    }
                }
            }
        }
    }

    public SelectItem[] getCertificateTypes() {
        int i = 0;
        final List<EdsCertificateOfEmploymentType> certificateTypes;
        certificateTypes = this.certificateOfEmploymentManager.getCertificateTypesWithPermission();
        final SelectItem[] certificateTypeItems = new SelectItem[certificateTypes.size()];
        for (final EdsCertificateOfEmploymentType ct : certificateTypes) {
            certificateTypeItems[i++] = new SelectItem(ct.getObjectID(), ct.getName(), ct.getFormID());
        }
        if (certificateTypeItems != null) {
            Arrays.sort(certificateTypeItems, Comparator.comparing(SelectItem::getName));
        }
        return certificateTypeItems;
    }


    @Override
    public CertificateItem getCertificateHTML(final Integer employeeId, final Integer certificateTypeID, final ArrayList<FileResource> files) {
        if (employeeId != null && certificateTypeID != null) {
            final CertificateItem item = new CertificateItem();
            final EdsCertificateOfEmploymentType type = this.certificateOfEmploymentManager.getCertificateType(certificateTypeID);
            if (type != null) {
                if (type.getCustomHTML() != null && !"".equals(type.getCustomHTML())) {
                    final String content = this.replaceVelocity(type.getCustomHTML(), employeeId, files, null);
                    item.setContent(content);
                } else {
                    final String content = this.replaceVelocity(type.getDefaultHTML(), employeeId, files, null);
                    item.setContent(content);
                }
            }
            if (type.getFormID() != null) {
                item.setFormID(type.getFormID());
                item.setSetupApproval(this.approverManager.isExistApproverByEntityTypeAndStepType(item.getFormID(), RelationItem.TYPE_CERTIFICATE_OF_EMPLOYMENT));
            }
            return item;
        }
        return null;
    }

    @Override
    public String replaceVelocity(String defaultHTML, final Integer employeeId, final ArrayList<FileResource> relatedFiles, final Integer certificateId) {
        final EdsEmployee employee;
        final EdsUser user = this.userManager.getUser();
        final EdsCompany company = user.getCompany();
        final EdsEmployeeProfile profile = this.profileManager.getProfile(employeeId);

        ProfileItem profileItem = new ProfileItem();
        employee = this.employeeManager.getEmployeeByProfileID(profile.getObjectID());
        final EdsEmployeeProfile emProfile = this.profileManager.get(profile.getObjectID());
        profileItem.setEmpCode(emProfile != null ? emProfile.getEmployeeCode() != null ? emProfile.getEmployeeCode() : "" : "");
        profileItem.setEmployeeId(employee.getObjectID());
        profileItem.setNationality(profile.getNationality());
        profileItem.setPassportNumber(profile.getPassportNumber());
        profileItem.setPassportIssueDate(profile.getPassportIssueDate() != null ? new DateNonConvertable(profile.getPassportIssueDate()) : null);
        profileItem.setPassportIssueBy(profile.getCountry() != null ? profile.getCountry().getName() : null);
        profileItem.setPassportExpiryDate(profile.getPassportExpiryDate() != null ? new DateNonConvertable(profile.getPassportExpiryDate()) : null);
        profileItem.setMedicalInsuranceExpireDate(profile.getMedicalInsuranceExDate() != null ? new DateNonConvertable(profile.getMedicalInsuranceExDate()) : null);
        profileItem.setVisaNumber(profile.getVisaNumber());
        profileItem.setVisaIssueDate(profile.getVisaIssueDate() != null ? new DateNonConvertable(profile.getVisaIssueDate()) : null);
        profileItem.setVisaExpirationDate(profile.getVisaExpirationDate() != null ? new DateNonConvertable(profile.getVisaExpirationDate()) : null);
        profileItem = profile.getRPC(profileItem);
        profileItem.setEmpCode(profile.getEmployeeCode());
        profileItem.setWageRate(employee.getWageRate());
        profileItem.setClientChargeRate(employee.getClientChargeRate());
        profileItem.setLocationName(employee.getLocation() != null ? employee.getLocation().getName() : null);
        profileItem.setReportsTo(profile.getReportsTo() != null ? profile.getReportsTo().getFullName() : null);
        if (employee.getContact() != null && employee.getContact().getPrimaryAddressFromAll() != null) {
            profileItem.setPrimaryAddress(employee.getContact().getPrimaryAddressFromAll());
        }
        String address = company.getAddress1() != null ? company.getAddress1() : "";
        if (Objects.equals(address, "")) {
            address = company.getAddress2() != null ? company.getAddress2() : "";
        }
        address = address + (company.getPostCode() != null && !Objects.equals(company.getPostCode(), "") ? !Objects.equals(address, "") ? ", " + company.getPostCode() : company.getPostCode() : "");

        final EdsReference employeeQualification = employee.getQualification();
        if (employeeQualification != null) {
            profileItem.setQualificationID(employeeQualification.getObjectID());
            profileItem.setQualificationCode(employeeQualification.getCode());
            profileItem.setQualificationName(this.referenceWfmMessageSource.localize(employeeQualification.getCode(), employeeQualification.getName()));
        }

        String departmentRu = "", departmentUz = "";
        if (employee.getEmployeeTeam() != null) {
            if (employee.getTeam() != null) {
                profileItem.setDepartment(employee.getTeam().getName());
                if (employee.getTeam().getLocale() != null) {
                    departmentRu = employee.getTeam().getLocale().getRussian();
                    departmentUz = employee.getTeam().getLocale().getUzbek();
                }
            }
        }

        profileItem.setPosition(employee.getPosition() != null ? employee.getPosition().getName() : "");
        profileItem.setHireDate(employee.getStartDate() != null ? new DateNonConvertable(employee.getStartDate()) : null);
        profileItem.setFireDate(employee.getEndDate() != null ? new DateNonConvertable(employee.getEndDate()) : null);
        final EdsEmployeePayrollSettings salary = this.employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), Constants.SALARY);
        final HashMap<Integer, BigDecimal> paymentsTotalMap = this.paymentDeductionManager.getEmployeeCategoriesTotal(employee.getObjectID().toString(), Constants.PAYMENT);
        final HashMap<Integer, BigDecimal> deductionsTotalMap = this.paymentDeductionManager.getEmployeeCategoriesTotal(employee.getObjectID().toString(), Constants.DEDUCTION);
        final HashMap<Integer, BigDecimal> loansTotalMap = this.paymentDeductionManager.getEmployeeCategoriesTotal(employee.getObjectID().toString(), Constants.LOAN);
        BigDecimal paymentsTotalAmount = BigDecimal.ZERO;
        BigDecimal totalAllowancesAmount = BigDecimal.ZERO;
        BigDecimal totalDeductionsAmount = BigDecimal.ZERO;
        BigDecimal totalLoansAmount = BigDecimal.ZERO;
        BigDecimal grossSalaryAmount = BigDecimal.ZERO;
        if (salary != null) {
            profileItem.setSalaryAmount(Double.parseDouble(salary.getValue()));
        } else {
            profileItem.setSalaryAmount(0d);
        }
        //Payment Categories Total
        if (paymentsTotalMap != null && paymentsTotalMap.size() > 0) {
            for (final BigDecimal paymentTotal : paymentsTotalMap.values()) {
                paymentsTotalAmount = paymentTotal;
                totalAllowancesAmount = paymentTotal;
                break;
            }
        }
        if (deductionsTotalMap != null && deductionsTotalMap.size() > 0) {
            for (final BigDecimal deductionTotal : deductionsTotalMap.values()) {
                totalDeductionsAmount = deductionTotal;
                break;
            }
        }
        if (loansTotalMap != null && loansTotalMap.size() > 0) {
            for (final BigDecimal loanTotal : loansTotalMap.values()) {
                totalLoansAmount = loanTotal;
                break;
            }
        }
        if (paymentsTotalAmount != null && profileItem.getSalaryAmount() != null) {
            paymentsTotalAmount = paymentsTotalAmount.add(BigDecimal.valueOf(profileItem.getSalaryAmount()));
        }

        if (totalDeductionsAmount != null && totalLoansAmount != null) {
            grossSalaryAmount = paymentsTotalAmount != null ? paymentsTotalAmount.subtract(totalDeductionsAmount.add(totalLoansAmount)) : BigDecimal.ZERO;
        }

        final EdsEmployeePayrollSettings jobTitle = this.employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), CustomFormConstants.JOB_TITLE);
        if (jobTitle != null) {
            final EdsEmployeePayrollSettings jobTitleText = this.employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), Constants.JOB_TITLE_TEXT);
            profileItem.setJobTitle(jobTitleText.getValue());
        }
        final EdsUserBankAccount userBankAccount = this.userBankAccountManager.getUserBankAccountByUser(employee);

        final EdsCurrency currency = this.financialSettingsManager.getFinancialSettings().getCurrency();
        final EdsEmployeeProfile userProfile = this.profileManager.getProfile(user.getObjectID());
        final EdsEmployee userEmployee = this.employeeManager.getEmployeeByProfileID(userProfile.getObjectID());
        final String userPosition = userEmployee != null && userEmployee.getPosition() != null ? userEmployee.getPosition().getName() : "____________";

        final Map<String, Object> values = new TreeMap<>();
        EdsCertificateOfEmployment certificate = null;
        if (certificateId != null) {
            certificate = this.certificateOfEmploymentManager.get(certificateId);
        }

        final EdsSickRequest edsSickRequest = this.sickRequestManager.getEmployeeLastApprovedLeaveRequest(employee.getObjectID(), EdsSickRequest.APPROVED);
        Date lastLeaveRequestStartDate = null;
        Date lastLeaveRequestEndDate = null;
        if (edsSickRequest != null) {
            lastLeaveRequestStartDate = edsSickRequest.getStartDate();
            lastLeaveRequestEndDate = edsSickRequest.getEndDate();

            final ArrayList<CompanyCustomFieldItem> customFieldList = CustomFieldsUtils.setRPCCustomFieldItems(edsSickRequest.getCustomFields(),
                    this.commonService.getCompanyCustomFields(ViewName.LeaveRequest));
            defaultHTML = this.replaceCustomFieldAttributes(defaultHTML, values, customFieldList);
        } else {
            defaultHTML = this.replaceCustomFieldAttributes(defaultHTML, values, this.commonService.getCompanyCustomFields(ViewName.LeaveRequest));
        }

        final NumberToWord numberToWordConverter = new NumberToWord_en();
        final EdsPosition edsPosition = employee.getPosition() != null ? employee.getPosition() : null;
        String positionRu = "", positionUz = "";
        if (edsPosition != null && (edsPosition.getLocale() != null)) {
            positionRu = edsPosition.getLocale().getRussian();
            positionUz = edsPosition.getLocale().getUzbek();
        }

        values.put(EmployeeProfileConstans.CERTIFICATE_NUMBERS, certificate != null ? this.getCertificateNumbers(false, certificate.getNumber()) : "_____");
        values.put(EmployeeProfileConstans.CERTIFICATE_LETTERS, certificate != null ? this.getCertificateNumbers(true, certificate.getNumber()) : "_____");
        values.put(EmployeeProfileConstans.EMPLOYEE_CODE, profileItem != null ? profileItem.getEmpCode() != null ? profileItem.getEmpCode() : "_________" : "_________");
        values.put(EmployeeProfileConstans.YOUR_NAME, user.getFullName() != null && !"".equals(user.getFullName()) ? user.getFullName() : " ________________________ ");
        values.put(EmployeeProfileConstans.ISSUER_NAME, user.getFullName() != null && !"".equals(user.getFullName()) ? user.getFullName() : " ________________________ ");
        values.put(EmployeeProfileConstans.YOUR_ROLE, user.getRolesSortedByPattern() != null && !"".equals(user.getRolesSortedByPattern().get(0).getName()) ? user.getRolesSortedByPattern().get(0).getName() : " ________________ ");
        values.put(EmployeeProfileConstans.ISSUER_POSITION, userPosition);
        values.put(EmployeeProfileConstans.CREATOR, certificate != null ? certificate.getCreaterBy() != null ? certificate.getCreaterBy().getName() != null ? certificate.getCreaterBy().getName() : "" : null : null);
        values.put(EmployeeProfileConstans.COMPANY_NAME, user.getCompany() != null && !"".equals(user.getCompany().getName()) ? user.getCompany().getName().replace("&", "&amp;") : " ____________________ ");
        values.put(EmployeeProfileConstans.COMPANY_ADDRESS, address != null && !address.isEmpty() ? address : " _______________________ ");
        values.put(EmployeeProfileConstans.CURRENT_DATE, user.getUserTimezone() != null && !"".equals(user.getUserTimezone()) ? Utils.formatDate(new Date(new Date().getTime() + user.getUserTimezone().getRawOffset()), user.getCompany()) : " ________________ ");
        values.put(EmployeeProfileConstans.SALUTATION, profileItem.getTitle() != null && !"".equals(profileItem.getTitle()) ? profileItem.getTitle() : "");
        values.put(EmployeeProfileConstans.FIRST_NAME, profileItem.getFirstName() != null && !"".equals(profileItem.getFirstName()) ? profileItem.getFirstName() : "");
        values.put(EmployeeProfileConstans.LAST_NAME, profileItem.getLastName() != null && !"".equals(profileItem.getLastName()) ? profileItem.getLastName() : "");
        values.put(EmployeeProfileConstans.MIDDLE_NAME, profileItem.getMiddleName() != null && !"".equals(profileItem.getMiddleName()) ? profileItem.getMiddleName() : "");
        values.put(EmployeeProfileConstans.DATE_OF_BIRTH, profileItem.getBirthDate() != null && !"".equals(profileItem.getBirthDate()) ? Utils.formatDate(new Date(profileItem.getBirthDate().getDate().getTime() + user.getUserTimezone().getRawOffset()), user.getCompany()) : " ______________ ");
        values.put(EmployeeProfileConstans.NATIONALITY, profileItem.getNationality() != null && !"".equals(profileItem.getNationality()) ? profileItem.getNationality() : " ________________ ");
        values.put(EmployeeProfileConstans.MARITAL_STATUS, profileItem.getMartialStatus() != null && !"".equals(profileItem.getMartialStatus()) ? profileItem.getMartialStatus() : "________ ");
        values.put(EmployeeProfileConstans.EMAIL, profileItem.getPrimaryEmail() != null && !"".equals(profileItem.getPrimaryEmail()) ? profileItem.getPrimaryEmail() : " ___________________ ");
        values.put(EmployeeProfileConstans.PHONE_NUMBER, profileItem.getPrimaryPhone() != null && !"".equals(profileItem.getPrimaryPhone()) ? profileItem.getPrimaryPhone() : " ________________ ");
        values.put(EmployeeProfileConstans.PASSPORT_NUMBER, profileItem.getPassportNumber() != null && !"".equals(profileItem.getPassportNumber()) ? profileItem.getPassportNumber() : " ________________ ");
        values.put(EmployeeProfileConstans.PASSPORT_ISSUED_BY, profileItem.getPassportIssueBy() != null && !"".equals(profileItem.getPassportIssueBy()) ? profileItem.getPassportIssueBy() : " ________________ ");
        values.put(EmployeeProfileConstans.DEPARTMENT, profileItem.getDepartment() != null && !"".equals(profileItem.getDepartment()) ? profileItem.getDepartment() : " ________________ ");
        values.put(EmployeeProfileConstans.DEPARTMENT_RU, departmentRu != null && !departmentRu.isEmpty() ? departmentRu : "");
        values.put(EmployeeProfileConstans.DEPARTMENT_UZ, departmentUz != null && !departmentUz.isEmpty() ? departmentUz : "");
        values.put(EmployeeProfileConstans.POSITION, profileItem.getPosition() != null && !"".equals(profileItem.getPosition()) ? profileItem.getPosition() : " ________________ ");
        values.put(EmployeeProfileConstans.POSITION_RU, positionRu != null && !positionRu.isEmpty() ? positionRu : "");
        values.put(EmployeeProfileConstans.POSITION_UZ, positionUz != null && !positionUz.isEmpty() ? positionUz : "");
        values.put(EmployeeProfileConstans.CURRENCY, currency.getName() != null && !"".equals(currency.getName()) ? currency.getName() : " ______ ");
        values.put(EmployeeProfileConstans.WAGE_RATE, profileItem.getWageRate() != null && !"".equals(profileItem.getWageRate()) ? profileItem.getWageRate() : " ___________ ");
        values.put(EmployeeProfileConstans.CLIENT_CHARGE_RATE, profileItem.getClientChargeRate() != null && !"".equals(profileItem.getClientChargeRate()) ? profileItem.getClientChargeRate() : " _____________ ");
        final DecimalFormat defaultScaleFormat = new DecimalFormat(",##0.00");
        values.put(EmployeeProfileConstans.SALARY_AMOUNT, profileItem.getSalaryAmount() != null && !"".equals(profileItem.getSalaryAmount()) ? defaultScaleFormat.format(profileItem.getSalaryAmount()) : " ____________ ");
        values.put(EmployeeProfileConstans.BASIC_ALLOWANCE, paymentsTotalAmount != null ? defaultScaleFormat.format(paymentsTotalAmount) : " ____________ ");
        values.put(EmployeeProfileConstans.OVERALL_STATUS, certificate != null && certificate.getOverallStatus() != null ? certificate.getOverallStatus().getCode() : "");
        if (certificate != null && certificate.getOverallStatus() != null && certificate.getOverallStatus().getCode() != null && Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED.equals(certificate.getOverallStatus().getCode())) {
            if (certificate.getApprovers() != null && certificate.getApprovers().get(0) != null &&
                    certificate.getApprovers().get(0).getApproverHistory() != null &&
                    certificate.getApprovers().get(0).getApproverHistory().iterator().hasNext() &&
                    certificate.getApprovers().get(0).getApproverHistory().iterator().next() != null &&
                    certificate.getApprovers().get(0).getApproverHistory().iterator().next().getApproveDate() != null) {
                values.put(EmployeeProfileConstans.APPROVED_DATE, Utils.formatDate(certificate.getApprovers().get(0).getApproverHistory().iterator().next().getApproveDate(), user.getCompany()));
            } else {
                values.put(EmployeeProfileConstans.APPROVED_DATE, " ________________ ");
            }
        } else {
            values.put(EmployeeProfileConstans.APPROVED_DATE, " ________________ ");
        }
        for (EdsPaymentDeduction deduction : employee.getCategories()) {

            if (deduction.getCategory().getCode().equals("TRANSPORTATION ALLOWANCE")) {
                values.put(EmployeeProfileConstans.TRANSPORT_ALLOWANCE, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.TRANSPORT_ALLOWANCE_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("HOUSING_ALLOWANCE")) {
                values.put(EmployeeProfileConstans.HOUSING_ALLOWANCE, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.HOUSING_ALLOWANCE_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("ALLOWANCE")) {
                values.put(EmployeeProfileConstans.ACCOMODATION_ALLOWANCE, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.ACCOMODATION_ALLOWANCE_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("AIR_TICKET_REIMBURSEMENT")) {
                values.put(EmployeeProfileConstans.AIR_TICKET_REIMBURSEMENT, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.AIR_TICKET_REIMBURSEMENT_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("AIR_TICKET_ALLOWANCE")) {
                values.put(EmployeeProfileConstans.AIR_TICKET_ALLOWANCE, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.AIR_TICKET_ALLOWANCE_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("ARREARS")) {
                values.put(EmployeeProfileConstans.ARREARS, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.ARREARS_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("BASIC_SALARY")) {
                values.put(EmployeeProfileConstans.BASIC_SALARY, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.BASIC_SALARY_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("BENEFIT_PAYMENT")) {
                values.put(EmployeeProfileConstans.BENEFIT_PAYMENT, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.BENEFIT_PAYMENT_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("BONUS")) {
                values.put(EmployeeProfileConstans.BONUS, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.BONUS_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("COST_LIVING_ALLOWANCE")) {
                values.put(EmployeeProfileConstans.COST_OF_LIVING_ALLOWANCE, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.COST_OF_LIVING_ALLOWANCE_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("EDUCATION_ALLOWANCE")) {
                values.put(EmployeeProfileConstans.EDUCATION_ALLOWANCE, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.EDUCATION_ALLOWANCE_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("END_OF_SERVICE")) {
                values.put(EmployeeProfileConstans.END_OF_SERVICE_GRATUITY, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.END_OF_SERVICE_GRATUITY_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("EXPENSE_REPORT")) {
                values.put(EmployeeProfileConstans.EXPENSE_REPORT, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.EXPENSE_REPORT_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("EXTRA_ADDITIONAL")) {
                values.put(EmployeeProfileConstans.EXTRA_ADDITIONAL, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.EXTRA_ADDITIONAL_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("FAMILY_ALLOWANCE")) {
                values.put(EmployeeProfileConstans.FAMILY_ALLOWANCE, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.FAMILY_ALLOWANCE_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("FAR_LOCATION_ALLOWANCE")) {
                values.put(EmployeeProfileConstans.FAR_LOCATION_ALLOWANCE, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.FAR_LOCATION_ALLOWANCE_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("JOB_ALLOWANCE")) {
                values.put(EmployeeProfileConstans.JOB_ALLOWANCE, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.JOB_ALLOWANCE_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }

            if (deduction.getCategory().getCode().equals("LEAVE_ENCHASHMENT")) {
                values.put(EmployeeProfileConstans.LEAVE_ENCASHMENT, deduction.getPaymentAmount() != null ? String.valueOf(deduction.getPaymentAmount().doubleValue()) : " _____________ ");
                values.put(EmployeeProfileConstans.LEAVE_ENCASHMENT_IN_WORDS, deduction.getPaymentAmount() != null ? numberToWordConverter.convert(deduction.getPaymentAmount().abs().setScale(2, RoundingMode.HALF_UP)) : " _____________ ");
            }
        }
        values.put(EmployeeProfileConstans.TOTAL_ALLOWANCES, totalAllowancesAmount != null ? defaultScaleFormat.format(totalAllowancesAmount) : " ____________ ");
        values.put(EmployeeProfileConstans.TOTAL_DEDUCTIONS, totalDeductionsAmount != null ? defaultScaleFormat.format(totalDeductionsAmount) : " ____________ ");
        values.put(EmployeeProfileConstans.TOTAL_LOANS, totalLoansAmount != null ? defaultScaleFormat.format(totalLoansAmount) : " ____________ ");
        values.put(EmployeeProfileConstans.GROSS_SALARY, grossSalaryAmount != null ? defaultScaleFormat.format(grossSalaryAmount) : " ____________ ");
        values.put(EmployeeProfileConstans.GROSS_SALARY_INWORDS, grossSalaryAmount != null ? numberToWordConverter.convert(grossSalaryAmount.abs().setScale(2, RoundingMode.HALF_UP)) : " ____________ ");
        values.put(EmployeeProfileConstans.SUPERVISOR, profileItem.getReportsTo() != null ? profileItem.getReportsTo() : "");

        final String paymentsTotal_word = paymentsTotalAmount != null ? numberToWordConverter.convert(paymentsTotalAmount.abs().setScale(2, RoundingMode.HALF_UP)) : "";

        values.put(EmployeeProfileConstans.BASIC_ALLOWANCE_AMOUNT, paymentsTotal_word != null && !"".equals(paymentsTotal_word) ? WordUtils.capitalizeFully(paymentsTotal_word) : " ____________ ");
        values.put(EmployeeProfileConstans.SALARY_IN_WORD, profileItem.getSalaryAmount() != null && !"".equals(profileItem.getSalaryAmount()) ? numberToWordConverter.convert(profileItem.getSalaryAmount().longValue()) : " ____________ ");

        values.put(EmployeeProfileConstans.HIRE_DATE, profileItem.getHireDate() != null && !"".equals(profileItem.getHireDate()) ? Utils.formatDate(profileItem.getHireDate().getNonConvertedDate(), user.getCompany()) : " ________________ ");
        values.put(EmployeeProfileConstans.FIRE_DATE, profileItem.getFireDate() != null && !"".equals(profileItem.getFireDate()) ? Utils.formatDate(profileItem.getFireDate().getNonConvertedDate(), user.getCompany()) : " ________________ ");
        values.put(EmployeeProfileConstans.JOB_TITLE, profileItem.getJobTitle() != null && !"".equals(profileItem.getJobTitle()) ? profileItem.getJobTitle() : " ________________ ");
        values.put(EmployeeProfileConstans.VISA_NUMBER, profileItem.getVisaNumber() != null && !"".equals(profileItem.getVisaNumber()) ? profileItem.getVisaNumber() : " ________________ ");
        values.put(EmployeeProfileConstans.VISA_EXPIRATION_DATE, profileItem.getVisaExpirationDate() != null && profileItem.getVisaExpirationDate().getDate() != null ? Utils.formatDate(new Date(profileItem.getVisaExpirationDate().getDate().getTime() + user.getUserTimezone().getRawOffset()), user.getCompany()) : " ________________ ");
        values.put(EmployeeProfileConstans.LOCATION, profileItem.getLocationName() != null && !"".equals(profileItem.getLocationName()) ? profileItem.getLocationName() : " ________________ ");
        values.put(EmployeeProfileConstans.LAST_LEAVE_REQUEST_START_DATE, lastLeaveRequestStartDate != null ? Utils.formatDate(new Date(lastLeaveRequestStartDate.getTime() + user.getUserTimezone().getRawOffset()), user.getCompany()) : " ________________ ");
        values.put(EmployeeProfileConstans.LAST_LEAVE_REQUEST_END_DATE, lastLeaveRequestEndDate != null ? Utils.formatDate(new Date(lastLeaveRequestEndDate.getTime() + user.getUserTimezone().getRawOffset()), user.getCompany()) : " ________________ ");
        if (userBankAccount != null) {
            values.put(EmployeeProfileConstans.BANK_NAME, userBankAccount.getBankName() != null && !"".equals(userBankAccount.getBankName()) ? userBankAccount.getBankName() : " ___________________ ");
            values.put(EmployeeProfileConstans.BANK_ADDRESS, userBankAccount.getBankAddress() != null && !"".equals(userBankAccount.getBankAddress()) ? userBankAccount.getBankAddress() : " ____________________________ ");
            values.put(EmployeeProfileConstans.ACCOUNT_NUMBER, userBankAccount.getAccountNumber() != null && !"".equals(userBankAccount.getAccountNumber()) ? userBankAccount.getAccountNumber() : " _____________ ");
            values.put(EmployeeProfileConstans.ACCOUNT_NAME, userBankAccount.getAccountName() != null && !"".equals(userBankAccount.getAccountName()) ? userBankAccount.getAccountName() : " ________________ ");
            values.put(EmployeeProfileConstans.SWIFT_CODE, userBankAccount.getSwiftCode() != null && !"".equals(userBankAccount.getSwiftCode()) ? userBankAccount.getSwiftCode() : " ____________ ");
            values.put(EmployeeProfileConstans.SORT_CODE, userBankAccount.getSortCode() != null && !"".equals(userBankAccount.getSortCode()) ? userBankAccount.getSortCode() : " _____________ ");
            values.put(EmployeeProfileConstans.IBAN_CODE, userBankAccount.getIbanCode() != null && !"".equals(userBankAccount.getIbanCode()) ? userBankAccount.getIbanCode() : " _____________ ");
        }
//        new fields
        values.put(EmployeeProfileConstans.TIMESLOT, this.getEmployeTimeslot(employee));
        if (employee.getProfile() != null && employee.getProfile().getGender() != null) {
            if (employee.getProfile().getGender().equalsIgnoreCase("female")) {
                values.put(EmployeeProfileConstans.HE_SHE, "She");
                values.put(EmployeeProfileConstans.HE_SHE_LOWER_CASE, "she");
                values.put(EmployeeProfileConstans.HIS_HER, "Her");
                values.put(EmployeeProfileConstans.HIS_HER_LOWER_CASE, "her");
                values.put(EmployeeProfileConstans.HIM_HER, "Her");
                values.put(EmployeeProfileConstans.HIM_HER_LOWER_CASE, "her");
            } else if (employee.getProfile().getGender().equalsIgnoreCase("male")) {
                values.put(EmployeeProfileConstans.HE_SHE, "He");
                values.put(EmployeeProfileConstans.HE_SHE_LOWER_CASE, "he");
                values.put(EmployeeProfileConstans.HIS_HER, "His");
                values.put(EmployeeProfileConstans.HIS_HER_LOWER_CASE, "his");
                values.put(EmployeeProfileConstans.HIM_HER, "Him");
                values.put(EmployeeProfileConstans.HIM_HER_LOWER_CASE, "him");
            }
        } else {
            values.put(EmployeeProfileConstans.HE_SHE, "He/She");
            values.put(EmployeeProfileConstans.HE_SHE_LOWER_CASE, "he/she");
            values.put(EmployeeProfileConstans.HIS_HER, "His/Her");
            values.put(EmployeeProfileConstans.HIS_HER_LOWER_CASE, "his/her");
            values.put(EmployeeProfileConstans.HIM_HER, "Him/Her");
            values.put(EmployeeProfileConstans.HIM_HER_LOWER_CASE, "him/her");
        }
        if (profileItem.getPrimaryAddress() != null) {
            StringBuilder addresses = new StringBuilder();
            if (profileItem.getPrimaryAddress().getAddress() != null) {
                addresses.append(profileItem.getPrimaryAddress().getAddress());
            }
            if (profileItem.getPrimaryAddress().getAddress() != null && profileItem.getPrimaryAddress().getAddressb() != null) {
                addresses.append(", ");
            }
            if (profileItem.getPrimaryAddress().getAddressb() != null) {
                addresses.append(profileItem.getPrimaryAddress().getAddressb());
            }
            values.put(EmployeeProfileConstans.EMPLOYEE_ADDRESS, addresses);
        } else {
            values.put(EmployeeProfileConstans.EMPLOYEE_ADDRESS, "");
        }
        EdsCertificateOfEmployment edsCertificateOfEmployment = certificateOfEmploymentManager.get(certificateId);
        if (edsCertificateOfEmployment != null) {
            StringBuilder creationdate = new StringBuilder();
            if (profileItem.getCreatedDate() != null) {
                String shortDateFormat;
                if (company.getCompanySettings() != null && company.getCompanySettings().getShortDateFormat() != null) {
                    shortDateFormat = company.getCompanySettings().getShortDateFormat();
                } else {
                    shortDateFormat = "yyyy-MM-dd";
                }
                creationdate.append(ServerUtils.getTimeFormatted(edsCertificateOfEmployment.getCreationDate(), shortDateFormat));
            }
            values.put(EmployeeProfileConstans.CREATION_DATE, creationdate);
        } else {
            values.put(EmployeeProfileConstans.CREATION_DATE, "N/A");
        }


        if (employee != null) {
            final ArrayList<CompanyCustomFieldItem> customFieldList = CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(),
                    this.commonService.getCompanyCustomFields(ViewName.Employee));
            defaultHTML = this.replaceCustomFieldAttributes(defaultHTML, values, customFieldList);
            int i = 1;

            for (final FileResource file : relatedFiles) {
                values.put("${attachment" + i++ + "}", "<img src=\"" + file.getAmazonLink().replace("&", "&amp;") + "\" style='max-width: 900px;' />");

            }

            if (!defaultHTML.contains("${attachment") && relatedFiles.size() > 0) {
                final StringBuilder attachments = new StringBuilder();
                for (final FileResource file : relatedFiles) {
                    attachments.append("<img src=\"").append(file.getAmazonLink().replace("&", "&amp;")).append("\" style='max-width: 900px;'/><br/>");
                }
                values.put("${attachments}", attachments.toString());
                defaultHTML = "<div>" + defaultHTML + "${attachments}</div>";
            }
        }

        try {
            return EdsTemplates.evaluateTemplate(values, defaultHTML);
        } catch (final EdsTemplateException e) {
            return null;
        }
    }

    private String replaceCustomFieldAttributes(String defaultHTML, final Map<String, Object> values, final ArrayList<CompanyCustomFieldItem> customFieldList) {
        if (customFieldList != null && customFieldList.size() > 0) {
            for (final CompanyCustomFieldItem customField : customFieldList) {
                final String value;
                if (Constants.DATA_TYPE_DATE.equals(customField.getDataType())) {
                    value = customField.getFieldDateNonConvertedValue() != null ? Utils.refactor(customField.getFieldDateNonConvertedValue().getNonConvertedDate()) : "";
                } else {
                    value = customField.getFieldStringValue();
                }
                if (defaultHTML.contains("<b>" + customField.getFieldName()) || defaultHTML.contains(customField.getFieldName() + "</b>")) {
                    final String fieldValue;
                    if (customField.getFieldName().contains(" ")) {
                        fieldValue = customField.getFieldName().replace(" ", "_");
                    } else {
                        fieldValue = customField.getFieldName();
                    }
                    defaultHTML = defaultHTML.replace("<b>" + customField.getFieldName() + "</b>", fieldValue);
                    values.put("${" + fieldValue + "}", Utils.refactor(value, true));
                } else {
                    final String aliasName = customField.getAliasName().replace(" ", "_");
                    if (defaultHTML.contains(customField.getFieldName())) {
                        defaultHTML = defaultHTML.replace("${" + customField.getFieldName() + "}", "${" + aliasName + "}");
                        values.put("${" + aliasName + "}", Utils.refactor(value, false));
                    }
                }
            }
        }
        return defaultHTML;
    }

    private String getCertificateNumbers(final Boolean numbers, final String code) {
        final StringBuilder sb = new StringBuilder();
        if (numbers != null && code != null) {
            final String tempCode = code.toUpperCase();
            if (numbers == Boolean.TRUE) {
                for (int i = 0; i < code.length(); i++) {
                    final char tempChar = code.toUpperCase().charAt(i);
                    if (tempChar >= 'A' && tempChar <= 'Z') {
                        sb.append(tempChar);
                    } else {
                        return sb.toString();
                    }
                }
            } else {
                int exclude = 0;
                for (int k = 0; k < code.length(); k++) {
                    final char tempChar = code.toUpperCase().charAt(k);
                    if (tempChar >= 'A' && tempChar <= 'Z') {
                        exclude++;
                    } else {
                        break;
                    }
                }
                return code.substring(exclude);
            }
        }
        return "_____";
    }

    private String getEmployeTimeslot(final EdsEmployee employee) {
        if (employee == null) {
            return "";
        }
        int timeSlotId;
        if (employee.getTimeSlot() != null) {
            timeSlotId = employee.getTimeSlot().getObjectID();
        } else {
            timeSlotId = employee.getCompany().getDefaultTimeSlot().getObjectID();
        }
        if (this.timeSlotItemManager.getTimeSlotItems(this.timeSlotManager.get(timeSlotId)).size() == 0) {
            timeSlotId = 1;

        }
        final EdsTimeSlot timeSlot = this.timeSlotManager.get(timeSlotId);
        final List<EdsTimeSlotItem> timeSlotItem = this.timeSlotItemManager.getTimeSlotItems(timeSlot);
        String workingHours = "";
        final Map<Integer, EdsTimeSlotItem> map = new HashMap<>();
        final List<Integer> days = new ArrayList<>();
        for (final EdsTimeSlotItem item : timeSlotItem) {
            map.put(item.getDay(), item);
            days.add(item.getDay());
        }
        Collections.sort(days);
        int i = 1;
        if (map.get(days.get(days.size() - i)).getStartTime() == 0 && map.get(days.get(days.size() - i)).getEndTime() == 0) {
            i = 2;
        }
        days.get(0);
        final EdsTimeSlotItem firstDay = map.get(days.get(1));
        final EdsTimeSlotItem fifthDay = map.get(days.get(days.size() - (i + 1)));
        final EdsTimeSlotItem lastDay = map.get(days.get(days.size() - i));
        final String firsdayStartTime = this.getTimeSlot(firstDay.getStartTime() / 60) + ":" + this.getTimeSlot(fifthDay.getStartTime() % 60);
        final String firsdayEndTime = this.getTimeSlot(firstDay.getEndTime() / 60) + ":" + this.getTimeSlot(fifthDay.getCoffeeEnd() % 60);
        final String lastStartTime = this.getTimeSlot(lastDay.getStartTime() / 60) + ":" + this.getTimeSlot(lastDay.getStartTime() % 60);
        final String lastDayEndTime = this.getTimeSlot(lastDay.getEndTime() / 60) + ":" + this.getTimeSlot(lastDay.getCoffeeEnd() % 60);
        if (firsdayStartTime.equals(lastStartTime) && firsdayEndTime.equals(lastDayEndTime)) {
            workingHours = firsdayStartTime + "-" + firsdayEndTime + " " + this.getWeekDay(firstDay.getDay()) + " to " + this.getWeekDay(lastDay.getDay());
        } else {
            workingHours += firsdayStartTime + "-" + firsdayEndTime + " " + this.getWeekDay(firstDay.getDay()) + " to " + this.getWeekDay(fifthDay.getDay()) + " " + lastStartTime + "-" + lastDayEndTime + " " + this.getWeekDay(lastDay.getDay());
        }
        return workingHours;
    }

    private String getWeekDay(final int day) {
        return switch (day) {
            case 1 -> "Monday";
            case 2 -> "Tuesday";
            case 3 -> "Wednesday";
            case 4 -> "Thursday";
            case 5 -> "Friday";
            case 6 -> "Saturday";
            default -> "";
        };

    }

    private String getTimeSlot(final int time) {
        if (String.valueOf(time).length() == 1) {
            return "0" + time;
        }
        return String.valueOf(time);
    }

//    public SolrQuery getCertificateSolrQuery(final ListingFilterParameter fp) {
//        FacetFilterRpc certificateFacetFilter = fp.getFacetFilter();
//        if (certificateFacetFilter != null && !certificateFacetFilter.isFilterChanges()) {
//            certificateFacetFilter = commonServiceLocal.getUserFacetFilter(certificateFacetFilter);
//        }
//
//        if (certificateFacetFilter != null) {
//            if (certificateFacetFilter.getSearchKey() != null && !"".equals(certificateFacetFilter.getSearchKey())) {
//                fp.setSearchKey(certificateFacetFilter.getSearchKey());
//            }
//            fp.setFacetFilter(certificateFacetFilter);
//        }
//
//        EdsUser edsUser = employeeManager.getUser();
//
//        final SolrQuery query = new SolrQuery();
//        String solrQuery = null;
//        if (edsUser != null && edsUser.getCompany().getObjectID() != null) {
//            solrQuery = getCertificateFacetQuery(fp, edsUser) + SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(certificateFacetFilter, edsUser.getCompany(),
//                    SolrSaleInvoiceRepresenter.FIELD_CREATION_DATE, null);
//        }
//
//        query.setQuery(solrQuery);
//        query.setStart(fp.getStart());
//        if (fp.getLimit() > 0) {
//            query.setParam(CommonParams.ROWS, String.valueOf(fp.getLimit()));
//        } else {
//            query.setParam(CommonParams.ROWS, "500");
//        }
//
//        if (!fp.isSearchButton()) {
//            if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
//                final SolrQuery.ORDER order = fp.isAscending() ? SolrQuery.ORDER.asc : SolrQuery.ORDER.desc;
//                switch (fp.getSortField()) {
//                    case CertificateItem.NUMBER:
//                        query.setSort(SolrCertificateRepresenter.SORTABLE_NUMBER, order);
//                        break;
//                    case CertificateItem.EMPLOYEE_CODE:
//                        query.setSort(SolrCertificateRepresenter.SORTABLE_EMPLOYEE_CODE, order);
//                        break;
//                    case CertificateItem.EMPLOYEE:
//                        query.setSort(SolrCertificateRepresenter.SORTABLE_EMPLOYEE_NAME, order);
//                        break;
//                    case CertificateItem.CERTIFICATE_TYPE:
//                        query.setSort(SolrCertificateRepresenter.SORTABLE_TYPE_NAME, order);
//                        break;
//                    case CertificateItem.ISSUED_DATE:
//                        query.setSort(SolrCertificateRepresenter.SORTABLE_ISSUED_DATE, order);
//                        break;
//                    case CertificateItem.ISSUED_BY:
//                        query.setSort(SolrCertificateRepresenter.SORTABLE_ISSUED_BY_NAME, order);
//                        break;
//                    case CertificateItem.CREATED_DATE:
//                        query.setSort(SolrCertificateRepresenter.SORTABLE_CREATED_DATE, order);
//                        break;
//                    case CertificateItem.CREATED_BY:
//                        query.setSort(SolrCertificateRepresenter.SORTABLE_CREATED_BY_NAME, order);
//                        break;
//                    case CertificateItem.APPROVER:
//                        query.setSort(SolrCertificateRepresenter.SORTABLE_CURRENT_APPROVER_NAME, order);
//                        break;
//                    case CertificateItem.STATUS:
//                        query.setSort(SolrCertificateRepresenter.SORTABLE_STATUS_NAME, order);
//                        break;
//                    default:
//                        CustomFieldsUtils.setCustomFieldsSortableNameToSolr(fp.getSortField(), !fp.isAscending(), query, true);
//                        break;
//                }
//            } else {
//                query.setSort(SolrCertificateRepresenter.FIELD_CERTIFICATE_ID, SolrQuery.ORDER.desc);
//            }
//        }
//        return query;
//    }

    private EdsCertificateCustomFields createCertificateCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsCertificateCustomFields certificateCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                certificateCustomFields = certificateCFManager.get(customFieldItems.get(0).getObjectId());
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
                certificateCustomFields = new EdsCertificateCustomFields();
                certificateCFManager.create(certificateCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(certificateCustomFields, customFieldItems);
            return certificateCustomFields;
        }
        return null;
    }

    @Override
    public void updateCertificateStatus(final Integer objectId, final String statusCode, final String note) {
        final EdsCertificateOfEmployment edsCertificate = this.certificateOfEmploymentManager.get(objectId);

        final EdsReference edsStatus = this.referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, statusCode);

        if (!Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED.equals(statusCode)) {
            edsCertificate.setEntityStatus(edsStatus);
        }
        edsCertificate.updateStatus(edsStatus);
        if (Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_REJECTED.equals(statusCode)) {
            edsCertificate.setRejectionNote(note);
        }
        this.certificateOfEmploymentManager.update(edsCertificate);

        createCertificateHistory(edsCertificate.getObjectID(), new HistoryListItem(edsStatus.getCode().equals(CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED) ? "approved" : "rejectionReason:" + note));

        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsCertificate, this.userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_CERTIFICATE_OF_EMPLOYMENT);
    }

    @Override
    public Integer saveCertificate(final CertificateItem item) {
        final EdsCertificateOfEmployment employment;
        if (item.getObjectId() != null) {
            employment = this.certificateOfEmploymentManager.get(item.getObjectId());
        } else {
            employment = new EdsCertificateOfEmployment();
        }
        if (item.getObjectId() == null) {
            employment.setCreaterBy(this.userManager.getUser());
            employment.setCreationDate(item.getCreationDate());
        }
        if (item.getObjectId() != null) {
            employment.setUpdatedBy(this.userManager.getUser());
            employment.setUpdatedDate(item.getUpdatedDate());
            employment.setLastUpdateTime(item.getUpdatedDate());
        }
        employment.setNumber(item.getCertificateNumber().getNumberString());
        employment.setIntNumber(item.getCertificateNumber().getIntNumber());

        if (item.getStepEmployeeId() != null) {
            final EdsStepEmployee stepEmployee = this.stepEmployeeManager.get(item.getStepEmployeeId());
            employment.setStepEmployee(stepEmployee);
        }

        String attachments = "";

        if (item.getDucumentList() != null) {
            for (final FileResource file : item.getDucumentList()) {
                attachments = attachments.concat(file.getBodyId() + ",");
            }
            employment.setAttachmentIDs(attachments);
        }
        final EdsEmployee employee = this.employeeManager.get(item.getEmployee().getId());
        employment.setEmployee(employee);
        employment.setEmployeeid(employee.getObjectID());

        final EdsCertificateOfEmploymentType employmentType = this.certificateOfEmploymentManager.getCertificateType(item.getCertificateType().getId());
        employment.setCertificateType(employmentType);
        if (employmentType != null && employmentType.getCustomHTML() != null && !"".equals(employmentType.getCustomHTML())) {
            employment.setContentHTML(employmentType.getCustomHTML());
        } else {
            employment.setContentHTML(employmentType.getDefaultHTML());
        }

        final EdsCertificateOfEmploymentFields fields;
        if (employment.getFields() != null) {
            fields = this.certificateOfEmploymentFieldsManager.get(employment.getFields().getObjectID());
        } else {
            fields = new EdsCertificateOfEmploymentFields();
        }
        fields.setTextBox1(item.getTextBox1());
        fields.setTextBox2(item.getTextBox2());
        fields.setTextBox3(item.getTextBox3());
        fields.setTextBox4(item.getTextBox4());
        fields.setTextBox5(item.getTextBox5());
        fields.setTextBox6(item.getTextBox6());
        fields.setTextBox7(item.getTextBox7());
        fields.setTextBox8(item.getTextBox8());
        fields.setTextBox9(item.getTextBox9());
        fields.setTextBox10(item.getTextBox10());
        fields.setTextBox11(item.getTextBox11());
        fields.setTextBox12(item.getTextBox12());
        fields.setTextBox13(item.getTextBox13());
        fields.setTextBox14(item.getTextBox14());
        fields.setTextBox15(item.getTextBox15());
        fields.setTextBox16(item.getTextBox16());
        fields.setTextBox17(item.getTextBox17());
        fields.setTextBox18(item.getTextBox18());
        fields.setTextArea1(item.getTextArea1());
        fields.setTextArea2(item.getTextArea2());
        fields.setTextArea3(item.getTextArea3());
        fields.setTextArea4(item.getTextArea4());
        fields.setTextArea5(item.getTextArea5());
        fields.setTextArea6(item.getTextArea6());
        fields.setTextArea7(item.getTextArea7());
        fields.setTextArea8(item.getTextArea8());
        this.certificateOfEmploymentFieldsManager.createOrUpdate(fields);

        employment.setFields(fields);

        EdsCertificateCustomFields customFields = createCertificateCustomFields(item.getCustomFieldItems());
        employment.setCustomFields(customFields);
        this.certificateOfEmploymentManager.createOrUpdate(employment);
        if (!CERTIFICATE_OF_EMPLOYMENT_STATUS_DRAFT.equals(item.getStatusCode())) {
            createCertificateHistory(employment.getObjectID(), new HistoryListItem("created"));
        }
        if (isOk(item.getApprovers())) {
            saveCertificateApprovers(employment, item.getApprovers(), item.getStatusCode());
            this.certificateOfEmploymentManager.update(employment);
        }


        if (isOk(item.getApprovers())) {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), employment, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_CERTIFICATE_OF_EMPLOYMENT);
        }

        try {
            certificateSolrComponent.index(employment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employment.getObjectID();
    }

    private void saveCertificateApprovers(EdsCertificateOfEmployment edsApprovable, List<ApproverItemMini> approvers, String statusCode) {
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
                    edsApprovable.getCurrentApprover().setStatus(referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, statusCode));
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED));
                    isFirstApprover = false;
                } else if (edsApprovable.getCurrentApprover() != null && statusCode != null) {
                    edsApprovable.getCurrentApprover().setStatus(referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED));
                }
                if (statusCode != null && !Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_APPROVED.equals(statusCode)) {
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, statusCode));
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
                edsApprover.setStatus(referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, statusCode));
                if (Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_DRAFT.equals(statusCode)) {
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, statusCode));
                } else {
                    edsApprovable.setEntityStatus(referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED));
                }
                isFirstApprover = false;
            } else if (statusCode != null) {
                edsApprover.setStatus(referenceManager.findReference(Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS, Constants.CERTIFICATE_OF_EMPLOYMENT_STATUS_SUBMITTED));
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
    public CertificateItem getCertificateTypeData(final Integer certificateTypeId, final Integer employeeID) {
        CertificateItem item = new CertificateItem();
        if (certificateTypeId != null) {
            final EdsCertificateOfEmploymentType certificateType = this.certificateOfEmploymentTypeManager.get(certificateTypeId);
            if (certificateType != null) {
                item = certificateType.createCertificateTypeData();
            }
        }
        final List<EdsReference> statuses = this.referenceManager.listReferences(EdsCertificateOfEmploymentType.CERTIFICATE_TEMPLATE_TYPE);
        item.setTypes(this.reference2SelectItem(statuses));

        final Map<String, String> personalAttrMap = this.getPersonalCategories(EmployeeProfileConstans.CERTIFICATION_OF_EMPLOYMENT);
        final Map<String, String> treeMap = new TreeMap<>(personalAttrMap);
        final Iterator<Map.Entry<String, String>> iterator = treeMap.entrySet().iterator();
        final SelectItem[] result = new SelectItem[treeMap.size()];
        int i = 0;
        while (iterator.hasNext()) {
            final Map.Entry<String, String> entry = iterator.next();
            result[i] = new SelectItem(i, entry.getValue(), entry.getKey());
            i++;
        }
        item.setFields(result);

        final EdsEmployee employee;

        if (employeeID != null) {
            employee = this.employeeManager.get(employeeID);
        } else {
            employee = this.employeeManager.get(this.userManager.getUser().getObjectID());
        }
        if (employee != null) {
            item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(),
                    this.commonService.getCompanyCustomFields(ViewName.Employee)));
        }
        item.setLeaveRequestCustomFields(this.commonService.getCompanyCustomFields(ViewName.LeaveRequest));

        return item;
    }

    private SelectItem[] reference2SelectItem(final List<EdsReference> references) {
        final SelectItem[] selectItems = new SelectItem[references.size()];
        int i = 0;
        for (final EdsReference status : references) {
            selectItems[i] = new SelectItem();
            selectItems[i].setId(status.getObjectID());
            selectItems[i].setDescription(status.getCode());
            final String value = this.referenceWfmMessageSource.localize(status.getCode(), status.getName());
            selectItems[i].setName(value);
            i++;
        }
        return selectItems;
    }

    @Override
    public boolean checkCertificateTypeName(final Integer objectId, String name) {
        if (StringUtils.isEmpty(name)) {
            return false;
        }
        name = name.trim();
        final EdsCertificateOfEmploymentType edsEmploymentType = this.certificateOfEmploymentTypeManager.getByName(name);
        if (edsEmploymentType != null && !edsEmploymentType.getDeleted() && !edsEmploymentType.getObjectID().equals(objectId)) {
            return false;
        }
        final String formID = name.replace(" ", "_").toUpperCase();
        final EdsReference edsReference = this.referenceManager.findReference(WorkflowRule._WORKFLOW_MODULE, WorkflowRule._WORKFLOW_MODULE + "_" + formID);
        return objectId != null || edsReference == null || edsReference.isDeleted();
    }

    @Override
    public Integer saveCertificateType(final CertificateItem item) {
        final EdsCertificateOfEmploymentType certificateType;

        if (item != null && item.getObjectId() != null) {
            certificateType = this.certificateOfEmploymentTypeManager.get(item.getObjectId());
        } else {
            certificateType = new EdsCertificateOfEmploymentType();
        }

        if (item.getName() != null) {
            item.setName(item.getName().trim());
        }

        final boolean isNameChanged = !certificateType.isNew() && !certificateType.getName().equals(item.getName());

        if (certificateType.isNew() || certificateType.getFormID() == null) {
            this.createCertificateForm(item.getName());
            this.commonServiceLocal.createWorkflowModule(item.getFormID().replace("_FORM", ""), item.getName(), true);
            this.createCertificatePermissions(item.getFormID().replace("_FORM", ""), item.getName(), true);
        } else if (isNameChanged) {
            this.renameWorkflowModule(item.getFormID().replace("_FORM", ""), item.getName());
            this.renameCertificatePermissions(item.getFormID().replace("_FORM", ""), item.getName());
        } else if (certificateType.getObjectID() != null && certificateType.getFormID() != null) {
            this.commonServiceLocal.createWorkflowModule(certificateType.getFormID().replace("_FORM", ""), item.getName(), true);
            this.createCertificatePermissions(certificateType.getFormID().replace("_FORM", ""), item.getName(), true);
        }

        if (certificateType.isNew() || certificateType.getFormID() == null) {
            String form_id = item.getName().replace(" ", "_").toUpperCase();
            form_id += form_id.endsWith("_FORM") ? "" : "_FORM";
            certificateType.setFormID(form_id);
        }

        certificateType.setName(item.getName());
        certificateType.setDescription(item.getDescription());

        if (item.getType() != null) {
            certificateType.setType(this.referenceManager.get(item.getType().getId()));
        }

        certificateType.setHeaderFooter(item.isPdfHeaderFooter());
        certificateType.setCreationDate(item.getCreationDate());
        certificateType.setCreaterBy(this.userManager.getUser());
        certificateType.setDefaultHTML(item.getContent());
        String decodedHtml = StringEscapeUtils.unescapeHtml4(item.getCustomHTMLcontent());
        certificateType.setCustomHTML(decodedHtml);

        this.certificateOfEmploymentTypeManager.createOrUpdate(certificateType);
        return certificateType.getObjectID();

    }

    private void createCertificateForm(final String name) {
        if (name != null) {
            String form_ID = name.replace(" ", "_").toUpperCase();
            form_ID += form_ID.endsWith("_FORM") ? "" : "_FORM";
            final EdsModel edsModel = this.modelManager.get(form_ID);
            if (edsModel == null) {
                final ModelForm model = new ModelForm();
                model.setFormID(form_ID);
                model.setTitle(name.toUpperCase());
                model.setViewName(name.replace(" ", ""));
                model.setActive(true);
                model.setCertificateForm(true);
                model.setCustom(true);
                model.setFields(new ArrayList<>());
                this.allInOneServiceLocal.saveModelForm(model);
            }
        }
    }

    private void createCertificatePermissions(final String code, final String name, final boolean create) {
        final Integer companyID = SecurityContext.getCompanyID();
        final String parentCode = PermissionConstants.CERTIFICATE_OF_EMPLOYMENT_ + code + "_ADD" + (companyID == null ? "" : "_" + companyID);
        EdsPermission addPermission = this.permissionManager.findByCode(parentCode, PermissionConstants.HRMS_CONTEXT);
        if (addPermission == null) {
            if (create) {
                final ArrayList<EdsPermission> permissions = new ArrayList<>();
                addPermission = new EdsPermission();
                addPermission.setIsMainMenu(false);
                addPermission.setContext(PermissionConstants.HRMS_CONTEXT);
                addPermission.setCode(parentCode);
                addPermission.setName(name + " Add");
                final Integer sorder = this.permissionManager.getLastSorderByParent(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_LIST, PermissionConstants.HRMS_CONTEXT);
                addPermission.setSorder(sorder == null || sorder == 0 ? 5 : sorder + 5);
                final EdsPermission parent = this.permissionManager.findByCode(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_LIST, PermissionConstants.HRMS_CONTEXT);
                addPermission.setParent(parent != null ? parent.getObjectID() : null);
                addPermission.setModuleCode(PermissionConstants.HRMS_MODULE);
                addPermission.setCompanyId(SecurityContext.getCompanyID());
                this.permissionManager.create(addPermission);
                permissions.add(addPermission);
//Edit
                final EdsPermission editPermission = new EdsPermission();
                editPermission.setIsMainMenu(false);
                editPermission.setContext(PermissionConstants.HRMS_CONTEXT);
                editPermission.setCode(PermissionConstants.CERTIFICATE_OF_EMPLOYMENT_ + code + "_EDIT" + (companyID == null ? "" : "_" + companyID));
                editPermission.setName(name + " Edit");
                editPermission.setSorder(1);
                editPermission.setParent(addPermission.getObjectID());
                editPermission.setModuleCode(PermissionConstants.HRMS_MODULE);
                editPermission.setCompanyId(SecurityContext.getCompanyID());
                this.permissionManager.create(editPermission);
                permissions.add(editPermission);
//Delete
                final EdsPermission deletePermission = new EdsPermission();
                deletePermission.setIsMainMenu(false);
                deletePermission.setContext(PermissionConstants.HRMS_CONTEXT);
                deletePermission.setCode(PermissionConstants.CERTIFICATE_OF_EMPLOYMENT_ + code + "_DELETE" + (companyID == null ? "" : "_" + companyID));
                deletePermission.setName(name + " Delete");
                deletePermission.setSorder(2);
                deletePermission.setParent(addPermission.getObjectID());
                deletePermission.setModuleCode(PermissionConstants.HRMS_MODULE);
                deletePermission.setCompanyId(SecurityContext.getCompanyID());
                this.permissionManager.create(deletePermission);
                permissions.add(deletePermission);
//Export
                final EdsPermission exportPermission = new EdsPermission();
                exportPermission.setIsMainMenu(false);
                exportPermission.setContext(PermissionConstants.HRMS_CONTEXT);
                exportPermission.setCode(PermissionConstants.CERTIFICATE_OF_EMPLOYMENT_ + code + "_PDF" + (companyID == null ? "" : "_" + companyID));
                exportPermission.setName(name + " Pdf");
                exportPermission.setSorder(3);
                exportPermission.setParent(addPermission.getObjectID());
                exportPermission.setModuleCode(PermissionConstants.HRMS_MODULE);
                exportPermission.setCompanyId(SecurityContext.getCompanyID());
                this.permissionManager.create(exportPermission);
                permissions.add(exportPermission);
//Role permissions
                final EdsRole admin = this.roleManager.getByCode(EdsRole.ADMIN_CODE);
                final EdsRole dr = this.roleManager.getByCode(EdsRole.DR_CODE);
                final EdsRole hr = this.roleManager.getByCode(EdsRole.HR_CODE);
                final List<String> permissionCodes = Lists.newArrayList();
                for (final EdsPermission p : permissions) {
                    if (p != null && p.getCode() != null) {
                        permissionCodes.add(p.getCode());
                    }
//Admin
                    final EdsRolePermission adminPermission = new EdsRolePermission();
                    adminPermission.setPermissioncode(p.getCode());
                    adminPermission.setRole(admin);
                    adminPermission.setPriviledgeCode(PermissionConstants.ALLOW);
                    this.rolePermissionManager.create(adminPermission);
                    if (!p.getCode().contains("PDF")) {
//DR
                        final EdsRolePermission drPermission = new EdsRolePermission();
                        drPermission.setPermissioncode(p.getCode());
                        drPermission.setRole(dr);
                        drPermission.setPriviledgeCode(PermissionConstants.ALLOW);
                        this.rolePermissionManager.create(drPermission);
//HR
                        final EdsRolePermission hrPermission = new EdsRolePermission();
                        hrPermission.setPermissioncode(p.getCode());
                        hrPermission.setRole(hr);
                        hrPermission.setPriviledgeCode(PermissionConstants.ALLOW);
                        this.rolePermissionManager.create(hrPermission);
                    }
                }
                this.permissionManager.createPermissionContext(permissionCodes);
            }
        } else if (!create) {
            this.permissionManager.deletePermissions(parentCode, PermissionConstants.HRMS_CONTEXT);
        }
    }

    private void renameCertificatePermissions(final String code, final String name) {
        final Integer companyID = SecurityContext.getCompanyID();
        final String parentCode = PermissionConstants.CERTIFICATE_OF_EMPLOYMENT_ + code + "_ADD" + (companyID == null ? "" : "_" + companyID);
        final EdsPermission addPermission = this.permissionManager.findByCode(parentCode, PermissionConstants.HRMS_CONTEXT);
        if (addPermission != null) {
            addPermission.setName(name + " Add");
            this.permissionManager.update(addPermission);
        }
        final List<EdsPermission> permissions = this.permissionManager.childByCode(parentCode, PermissionConstants.HRMS_CONTEXT);
        for (final EdsPermission p : permissions) {
            if (p.getCode() != null) {
                if (p.getCode().endsWith("_EDIT" + (companyID == null ? "" : "_" + companyID))) {
                    p.setName(name + " Edit");
                } else if (p.getCode().endsWith("_DELETE" + (companyID == null ? "" : "_" + companyID))) {
                    p.setName(name + " Delete");
                } else if (p.getCode().endsWith("_PDF" + (companyID == null ? "" : "_" + companyID))) {
                    p.setName(name + " Pdf");
                }
                this.permissionManager.update(p);
            }
        }
    }

    private Map<String, String> getPersonalCategories(final String code) {
        final Map<String, String> personalAttrMap;
        if (code.equals(EmployeeProfileConstans.CERTIFICATION_OF_EMPLOYMENT))
            personalAttrMap = CertificateUtils.getEmploymentFields();
        else {
            personalAttrMap = new LinkedHashMap<>();
        }
        return personalAttrMap;
    }


    @Override
    public ListResult<CertificateItem> getCertificateTypeList(final ListingFilterParameter fp) {
        final ArrayList<CertificateItem> list = new ArrayList<>();
        final List<EdsCertificateOfEmploymentType> certificateTypes = this.certificateOfEmploymentTypeManager.getCertificateTypeList(fp);
        final Integer totalCountCertificateType = this.certificateOfEmploymentTypeManager.getCertificateTypeTotalCount(fp);
        for (final EdsCertificateOfEmploymentType cert : certificateTypes) {
            list.add(cert.createCertificateTypeData());
        }
        return new ListResult<>(list, totalCountCertificateType);
    }

    @Override
    public boolean deleteCertificateType(final Integer objectId) {
        if (objectId != null) {
            final EdsCertificateOfEmploymentType type = this.certificateOfEmploymentTypeManager.get(objectId);
            if (type != null) {
                type.setDeleted(true);
                this.certificateOfEmploymentTypeManager.createOrUpdate(type);

                if (type.getFormID() != null) {
                    final EdsReference module = this.referenceManager.findReference(WorkflowRule._WORKFLOW_MODULE, WorkflowRule._WORKFLOW_MODULE + "_" + type.getFormID().replace("_FORM", ""));
                    if (module != null) {
                        module.setDeleted(true);
                        this.referenceManager.update(module);
                    }
                    this.createCertificatePermissions(type.getFormID().replace("_FORM", ""), null, false);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ProfileItem getEmployeeBenefitAllowance(final Integer employeeID, final Integer year) {
        final EdsEmployee edsEmployee = this.employeeManager.get(employeeID);
        final ProfileItem item = new ProfileItem();
        item.setEmployeeId(edsEmployee.getObjectID());
        item.setFirstName(edsEmployee.getFullName());
        item.setEmployeeBenefits(this.getEmployeeBenefits(employeeID, year));
        return item;
    }

    private HashMap<Integer, AnnualLeaveItem> getEmployeeBenefits(final Integer employeeID, Integer year) {
        final HashMap<Integer, AnnualLeaveItem> itemHashMap = new HashMap<>();
        EdsReference qtyType;
        EdsCurrency currency;
        String benefitType = "";
        if (employeeID != null) {
            if (year == null) {
                year = ServerUtils.getYear(new Date());
            }
            final List<EdsEmployeeBenefitAllowance> allowances = this.employeeBenefitAllowanceManager.getBenefitAllowanceByEmpID(year, employeeID);
            for (final EdsEmployeeBenefitAllowance allowance : allowances) {
                final AnnualLeaveItem item = new AnnualLeaveItem();
                item.setAllowanceYear(allowance.getAllowanceYear());
                item.setAnnualallowancedays(allowance.getAllowance());
                item.setObjectID(allowance.getBenefit().getObjectID());
                item.setReasonName(allowance.getBenefit().getName());
                if (allowance.getBenefit() != null) {
                    qtyType = allowance.getBenefit().getQtytype();
                    currency = allowance.getBenefit().getCurrency();
                    if (EdsBenefit._CURRENCY.equals(qtyType.getCode())) {
                        benefitType = currency != null ? currency.getName() : "";
                    } else {
                        benefitType = qtyType != null ? qtyType.getName() : "";
                    }
                }
                item.setBenefitType(benefitType);
                itemHashMap.put(allowance.getBenefit().getObjectID(), item);
            }
        }
        return this.getEmployeeFullBenefitAllowance(itemHashMap);
    }

    private HashMap<Integer, AnnualLeaveItem> getEmployeeFullBenefitAllowance(final HashMap<Integer, AnnualLeaveItem> itemHashMap) {
        final ListingFilterParameter fp = new ListingFilterParameter();
        fp.setActive(true);
        final List<EdsBenefit> benefits = this.benefitManager.getBenefitList(fp);
        EdsReference qtyType;
        EdsCurrency currency;
        for (final EdsBenefit benefit : benefits) {
            if (!itemHashMap.containsKey(benefit.getObjectID())) {
                final AnnualLeaveItem item = new AnnualLeaveItem();
                item.setAllowanceYear(ServerUtils.getYear(new Date()));
                item.setAnnualallowancedays(0.00);
                item.setObjectID(benefit.getObjectID());
                item.setReasonName(benefit.getName());
                qtyType = benefit.getQtytype();
                currency = benefit.getCurrency();
                if (EdsBenefit._CURRENCY.equals(qtyType.getCode())) {
                    item.setBenefitType(currency != null ? currency.getName() : "");
                } else {
                    item.setBenefitType(qtyType != null ? qtyType.getName() : "");
                }
                itemHashMap.put(benefit.getObjectID(), item);
            }
        }
        return itemHashMap;
    }

    @Override
    public Integer saveEmployeeBenefitAllowance(final ProfileItem employeeItem) {
        final EdsEmployee empl = this.employeeManager.get(employeeItem.getEmployeeId());
        if (employeeItem.getEmployeeBenefits() != null && employeeItem.getEmployeeBenefits().size() > 0) {
            this.createEmployeeBenefitAllowance(empl, employeeItem.getEmployeeBenefits());
        }
        return empl.getObjectID();
    }

    private void createEmployeeBenefitAllowance(final EdsEmployee empl, final HashMap<Integer, AnnualLeaveItem> benefitItems) {
        final Integer currentyear = ServerUtils.getYear(new Date());
        for (final Map.Entry<Integer, AnnualLeaveItem> entry : benefitItems.entrySet()) {
            final AnnualLeaveItem item = entry.getValue();
            if (item != null) {
                EdsEmployeeBenefitAllowance benefitAllowance = this.employeeBenefitAllowanceManager.getBenefitAllowance(currentyear, empl.getObjectID(), entry.getKey()); //entry.getKey()()-> benefit ID
                if (benefitAllowance == null) {
                    benefitAllowance = new EdsEmployeeBenefitAllowance();
                }
                benefitAllowance.setEmployee(empl);
                benefitAllowance.setAllowance(item.getAnnualallowancedays());
                benefitAllowance.setAllowanceYear(currentyear);
                benefitAllowance.setBenefit(this.benefitManager.get(item.getObjectID()));
                this.employeeBenefitAllowanceManager.createOrUpdate(benefitAllowance);
            }
        }
    }

    @Override
    public ArrayList<LaborPeriodRequest> getPeriodList(Integer employeeID) {
        List<EdsLabourPeriod> periodList = labourPeriodManager.periodListByEmployeeId(employeeID);
        ArrayList<LaborPeriodRequest> list = new ArrayList<>();

        if (periodList != null && periodList.size() > 0) {
            for (EdsLabourPeriod item : periodList) {
                LaborPeriodRequest request = item.toRpc();
                String periodStartDate = ServerUtils.shortDateFormat(item.getStartDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);
                String periodEndDate = ServerUtils.shortDateFormat(item.getEndDate(), companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId())), true);
                Double totalApprovedLeaveDays = labourPeriodManager.getTotalTakenLeaveDaysByPeriodId(item.getObjectID(), true);
                Double totalSubmittedLeaveDays = labourPeriodManager.getTotalTakenLeaveDaysByPeriodId(item.getObjectID(), false);

                request.setPeriodID(item.getObjectID());
                request.setAllowance(item.getAllowance() != null ? item.getAllowance() : 0d);
                request.setLaborPeriod(periodStartDate + " - " + periodEndDate);
                request.setApprovedTakenDays(totalApprovedLeaveDays != null ? totalApprovedLeaveDays : 0d);
                request.setOverAllSubmittedLeaveDays(totalSubmittedLeaveDays != null ? totalSubmittedLeaveDays : 0d);
                request.setOutOfSystemDays(item.getOutOfSystemDays() != null ? item.getOutOfSystemDays() : 0d);
                list.add(request);
            }
        }
        return list;
    }

    public ArrayList<LaborPeriodRequest> getLeaveAllowances(Integer employeeId) {
        ArrayList<LaborPeriodRequest> requestList = new ArrayList<>();
        List<EdsAnnualLeaveAllowance> employeesAllAllowance = annualLeaveAllowanceManager.getLeaveAllowancesByEmployee(employeeId, EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
        for (EdsAnnualLeaveAllowance empAll : employeesAllAllowance) {
            LaborPeriodRequest request = new LaborPeriodRequest();
            request.setLaborPeriod(empAll.getAllowanceYear().toString());
            request.setAllowance(empAll.getAllowanceDays());
            requestList.add(request);
        }
        return requestList;
    }

    @Override
    public ProfileItem getEmployeeLeaveTypes(final Integer objectId, final Integer selectedYear) {
        final EdsEmployee edsEmployee = this.employeeManager.get(objectId);
        final ProfileItem item = new ProfileItem();
        item.setEmployeeId(edsEmployee.getObjectID());
        item.setFirstName(edsEmployee.getFullName());
        item.setLeaveitems(this.getEmployeeLeaveData(objectId, selectedYear));
        item.setCurrentYear(new DateNonConvertable(new Date()));
        return item;
    }

    @Override
    public Integer saveEmployeeLeaveTypes(final ProfileItem employee, final Integer selectedYear) {
        final EdsEmployee empl = this.employeeManager.get(employee.getEmployeeId());
        this.createEmployeeLeaveAllowance(empl, employee.getLeaveitems(), selectedYear);
        return empl.getObjectID();
    }

    @Override
    public ListResult<BenefitItem> getBenefitList(final ListingFilterParameter fp) {
        final ArrayList<BenefitItem> list = new ArrayList<>();
        fp.setActive(false);
        final List<EdsBenefit> benefits = this.benefitManager.getBenefitList(fp);
        final Integer totalCount = this.benefitManager.getBenefitTotalCount(fp);
        int index = 0;
        String type = "";
        String qtyType = "";
        for (final EdsBenefit benefit : benefits) {
            list.add(benefit.toBenefitItem());
            type = list.get(index).getType();
            qtyType = list.get(index).getQtytype();
            list.get(index).setType(this.commonLocalizer.localize(type, type));
            list.get(index).setQtytype(this.commonLocalizer.localize(qtyType, qtyType));
            index++;
        }
        return new ListResult<>(list, totalCount);

    }

    @Override
    public BenefitItem getBenefitData(final Integer benefitId, final Boolean withDropdownValues) {
        final BenefitItem item;
        EdsBenefit benefit = null;
        if (benefitId != null) {
            benefit = this.benefitManager.get(benefitId);
            item = benefit.toBenefitItem();
        } else {
            item = new BenefitItem();
        }
        if (benefit != null && benefit.getDebitToAccountID() != null) {
            final EdsAccount debitAccount = this.accountingManager.get(benefit.getDebitToAccountID());
            if (debitAccount != null) {
                item.setDebitToAccount(debitAccount.getAsSelectItem());
            }
        }
        if (benefit != null && benefit.getCreditToAccountID() != null) {
            final EdsAccount creditAccount = this.accountingManager.get(benefit.getCreditToAccountID());
            if (creditAccount != null) {
                item.setCreditToAccount(creditAccount.getAsSelectItem());
            }
        }
        if (withDropdownValues) {
            int i = 0;
            final List<EdsReference> types = this.referenceManager.listReferences("_BENEFIT_TYPE");
            final SelectItem[] typeItems = new SelectItem[types.size()];
            for (final EdsReference ct : types) {
                typeItems[i] = ct.getAsSelectItem();
                typeItems[i].setName(this.commonLocalizer.localize(typeItems[i].getName(), typeItems[i].getName()));
                i++;
            }
            item.setTypes(typeItems);

            i = 0;
            final List<EdsReference> qtyTypes = this.referenceManager.listReferences("_BENEFIT_QTYTYPE");
            final SelectItem[] qtyTypeItems = new SelectItem[qtyTypes.size()];
            for (final EdsReference ct : qtyTypes) {
                qtyTypeItems[i] = ct.getAsSelectItem();
                qtyTypeItems[i].setName(this.commonLocalizer.localize(qtyTypeItems[i].getName(), qtyTypeItems[i].getName()));
                i++;
            }
            item.setQtyTypes(qtyTypeItems);

            final List<EdsCurrency> currencies = this.currencyManager.getAllCurrency();

            final CurrencyItem[] currItems = new CurrencyItem[currencies.size()];
            i = 0;
            for (final EdsCurrency currency : currencies) {
                currItems[i] = currency.createCurrencyItem();
                i++;
            }
            item.setCurrencys(currItems);


        }

        if (benefit != null) {
            final ArrayList<SelectItem> emps = new ArrayList<>();
            for (final EdsEmployee employee : benefit.getEmployees()) {
                emps.add(new SelectItem(employee.getObjectID(), employee.getName()));
            }
            item.setEmployees(emps);
        }

        return item;
    }

    @Override
    public Integer deleteBenefit(final Integer objectId) {
        try {
            final EdsBenefit benefit = this.benefitManager.get(objectId);
            if (benefit == null) {
                return -1;
            }
            final boolean hasBenefitRequest = this.benefitManager.hasBenefitRequest(objectId);
            if (hasBenefitRequest) {
                return -2;
            }
            benefit.setDeleted(true);
            this.benefitManager.update(benefit);

            final List<EdsPositionBenefitAllowance> positionBenefitAllowanceList = this.positionBenefitAllowanceManager.getPositionAllowanceByBenefit(benefit.getObjectID());
            if (positionBenefitAllowanceList != null && positionBenefitAllowanceList.size() > 0) {
                positionBenefitAllowanceList.forEach(positionBenefitAllowance -> this.positionBenefitAllowanceManager.delete(positionBenefitAllowance));
            }

            final List<EdsEmployeeBenefitAllowance> employeeBenefitAllowanceList = this.employeeBenefitAllowanceManager.getEmployeeAllowanceByBenefit(benefit.getObjectID());
            if (employeeBenefitAllowanceList != null && employeeBenefitAllowanceList.size() > 0) {
                employeeBenefitAllowanceList.forEach(employeeBenefitAllowance -> this.employeeBenefitAllowanceManager.delete(employeeBenefitAllowance));
            }

            return 1;
        } catch (final Exception e) {
            HrmsServiceImpl.log.error("", e);
            return -1;
        }
    }

    @Override
    public Integer saveBenefit(final BenefitItem benefitItem) {
        EdsBenefit benefit = new EdsBenefit();
        if (benefitItem.getObjectId() != null) {
            benefit = this.benefitManager.get(benefitItem.getObjectId());
        }
        benefit.setLastUpdateTime(new Date());
        benefit.setName(benefitItem.getName());
        benefit.setCode(benefitItem.getName().replace(" ", "_").toUpperCase());
        benefit.setType(this.referenceManager.get(benefitItem.getTypeID()));
        benefit.setQtytype(this.referenceManager.get(benefitItem.getQtytypeID()));
        benefit.setCurrency(null);
        if (benefitItem.getCurrencyID() != null)
            benefit.setCurrency(this.currencyManager.get(benefitItem.getCurrencyID()));
        benefit.setTransferrable(benefitItem.getTransferrable());
        benefit.setQtyRestriction(benefitItem.getQtyRestriction());
        benefit.setExpireDate(null);
        if (benefitItem.getExpireDate() != null) {
            benefit.setExpireDate(ServerUtils.getEndDate(benefitItem.getExpireDate().getNonConvertedDate()));
        }
        benefit.setDescription(benefitItem.getDescription());
        benefit.setAllowance(benefitItem.getAllowance());
        benefit.setIsActive(benefitItem.isActive());
        benefit.setDebitToAccountID(benefitItem.getDebitToAccount() != null ? benefitItem.getDebitToAccount().getId() : null);
        benefit.setCreditToAccountID(benefitItem.getCreditToAccount() != null ? benefitItem.getCreditToAccount().getId() : null);
        benefit.setDeleted(false);

        //Save Employees
        final HashSet<EdsEmployee> employees = new HashSet<>();
        benefitItem.getEmployees().forEach(employee -> employees.add(this.employeeManager.get(employee.getId())));

        benefit.setEmployees(employees);
        this.benefitManager.createOrUpdate(benefit);
        if (benefitItem.isApplyAll()) {
            this.applyAllPositionBenefit(benefit);
            this.applyEmployeeBenefit(benefit);
        }
        final EdsReference type = this.referenceManager.findReference("_BENEFIT_QTYTYPE", "CURRENCY");
        if (type != null && benefitItem.getQtytypeID() != null && benefitItem.getQtytypeID().equals(type.getObjectID())) {
            final EdsPayrollCategory benefitPaymentCategory = this.categoryManager.getCategoryByCode(Constants.BENEFIT_PAYMENT);
            if (benefitPaymentCategory != null && benefitItem.getDebitToAccount() != null && benefitItem.getCreditToAccount() != null) {
                benefitPaymentCategory.setDebitToAccountID(benefitItem.getDebitToAccount().getId());
                benefitPaymentCategory.setCreditToAccountID(benefitItem.getCreditToAccount().getId());
                this.categoryManager.update(benefitPaymentCategory);
            }
        }
        return benefitItem.getObjectId();
    }

    /**
     * Set allowance for all positions from current date to benefit expire date.
     * If the benefit expire date is not provided, allowance should be set for a year
     *
     * @param benefit
     */
    private void applyAllPositionBenefit(final EdsBenefit benefit) {
        final List<EdsPosition> positions = this.positionManager.getPositionList(new ListingFilterParameter());
        final Date currentDate = new Date();
        final Date expireDate = benefit.getExpireDate();
        final Date yearStartDate;
        final Date yearEndDate;
        if (expireDate != null) {
            final int currentYear = ServerUtils.getYear(currentDate);
            final int expireYear = ServerUtils.getYear(expireDate);
            if (currentYear == expireYear) {
                yearStartDate = ServerUtils.getYearStartDate(expireYear);
                yearEndDate = ServerUtils.getYearEndDate(currentYear);
            } else if (currentYear < expireYear) {
                yearStartDate = ServerUtils.getYearStartDate(currentYear);
                yearEndDate = ServerUtils.getYearEndDate(expireYear);
            } else {
                yearStartDate = ServerUtils.getYearStartDate(expireYear);
                yearEndDate = ServerUtils.getYearEndDate(expireYear);
            }
        } else {
            yearStartDate = ServerUtils.getYearStartDate(ServerUtils.getYear(currentDate));
            yearEndDate = ServerUtils.getYearEndDate(ServerUtils.getYear(currentDate));
        }

        final List<Integer> years = ServerUtils.getYears(yearStartDate, yearEndDate);
        years.forEach(year -> positions.forEach(position -> {
            EdsPositionBenefitAllowance benefitAllowance = this.positionBenefitAllowanceManager.getBenefitAllowanceFromPosition(benefit.getObjectID(), position.getObjectID(), year);
            if (benefitAllowance == null) {
                benefitAllowance = new EdsPositionBenefitAllowance();
            }
            benefitAllowance.setPosition(position);
            benefitAllowance.setAnnualallowance(benefit.getAllowance());
            benefitAllowance.setAllowanceYear(year);
            benefitAllowance.setBenefit(benefit);
            this.positionBenefitAllowanceManager.createOrUpdate(benefitAllowance);
        }));
    }

    /**
     * Set allowance for all employees from current date to benefit expire date.
     * If the benefit expire date is not provided, allowance should be set for a year
     *
     * @param benefit
     */
    private void applyEmployeeBenefit(final EdsBenefit benefit) {
        final List<EdsEmployee> employees = this.employeeManager.list(new ListingFilterParameter());
        final Date currentDate = new Date();
        final Date expireDate = benefit.getExpireDate();
        final Date yearStartDate;
        final Date yearEndDate;
        if (expireDate != null) {
            final int currentYear = ServerUtils.getYear(currentDate);
            final int expireYear = ServerUtils.getYear(expireDate);
            if (currentYear == expireYear) {
                yearStartDate = ServerUtils.getYearStartDate(expireYear);
                yearEndDate = ServerUtils.getYearEndDate(currentYear);
            } else if (currentYear < expireYear) {
                yearStartDate = ServerUtils.getYearStartDate(currentYear);
                yearEndDate = ServerUtils.getYearEndDate(expireYear);
            } else {
                yearStartDate = ServerUtils.getYearStartDate(expireYear);
                yearEndDate = ServerUtils.getYearEndDate(expireYear);
            }
        } else {
            yearStartDate = ServerUtils.getYearStartDate(ServerUtils.getYear(currentDate));
            yearEndDate = ServerUtils.getYearEndDate(ServerUtils.getYear(currentDate));
        }

        final List<Integer> years = ServerUtils.getYears(yearStartDate, yearEndDate);
        years.forEach(year -> employees.forEach(employee -> {
            EdsEmployeeBenefitAllowance empAllowance = this.employeeBenefitAllowanceManager.getBenefitAllowance(year, employee.getObjectID(), benefit.getObjectID());
            if (empAllowance == null) {
                empAllowance = new EdsEmployeeBenefitAllowance();
            }
            empAllowance.setEmployee(employee);
            empAllowance.setAllowance(benefit.getAllowance());
            empAllowance.setAllowanceYear(year);
            empAllowance.setBenefit(benefit);
            this.employeeBenefitAllowanceManager.createOrUpdate(empAllowance);
        }));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void createEmployeeLeaveAllowance(final EdsEmployee employee, final Map<Integer, AnnualLeaveItem> leaveitems, final Integer selectedYear) {
        if (employee == null) {
            return;
        }
        employee.clear();
        Integer currentyear = Calendar.getInstance().get(Calendar.YEAR);
        currentyear = selectedYear != null ? selectedYear : currentyear;
        for (final Integer key : leaveitems.keySet()) {
            final EdsLeaveReason reason = this.leaveReasonManager.get(leaveitems.get(key).getReasonId());
            EdsAnnualLeaveAllowance leaveAllowance = this.annualLeaveAllowanceManager.getLeaveAllowanceByReason(currentyear, employee.getObjectID(), reason.getCode(), null);
            if (leaveAllowance == null) {
                leaveAllowance = new EdsAnnualLeaveAllowance();
            }

            leaveAllowance.setEmployee(employee);
            leaveAllowance.setAddPrevious(leaveitems.get(key).getAddPrevious() != null ? leaveitems.get(key).getAddPrevious() : false);
            leaveAllowance.setAllowanceDays(leaveitems.get(key).getAnnualallowancedays());
            leaveAllowance.setAllowanceYear(currentyear);
            leaveAllowance.setReasonCode(reason.getCode());

            if ("LR_TYPE_ANNUAL_LEAVE".equals(reason.getCode()) && leaveitems.get(key).getRequestList() != null && leaveitems.get(key).getRequestList().size() > 0) {
                createOrUpdateLeavePeriod(leaveAllowance, leaveitems.get(key).getRequestList());
            }
            this.annualLeaveAllowanceManager.createOrUpdate(leaveAllowance);
        }
        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT,
                employee,
                this.employeeManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_EMPLOYEE);
    }

    private void createOrUpdateLeavePeriod(EdsAnnualLeaveAllowance leaveAllowance, List<LaborPeriodRequest> list) {
        EdsUser modifiedBy = userManager.getUser();
        for (LaborPeriodRequest request : list) {
            boolean hasChanged = false;
            EdsLabourPeriod period = labourPeriodManager.getById(request.getPeriodID());
            Double allowance = request.getAllowance();
            if (allowance != null) {
                period.setAllowance(allowance);
                hasChanged = true;
            }
            if (request.getOutOfSystemDays() != null) {
                period.setOutOfSystemDays(request.getOutOfSystemDays());
                hasChanged = true;
            }

            if (period.getEndDate() != null) {
                LocalDate localDate = period.getEndDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                if (localDate.getYear() == leaveAllowance.getAllowanceYear()) {
                    leaveAllowance.setAllowanceDays(allowance);
                }
            }

            period.setModifiedDate(new Date());
            period.setModifiedBy(modifiedBy);
            if (hasChanged) {
                labourPeriodManager.createOrUpdate(period);
                createlaborPeriodHistory(period.getObjectID(), new HistoryListItem(period.getObjectID() != null ? "updated" : "created"));
            }
        }
    }

    @Override
    public Integer createlaborPeriodHistory(Integer periodId, HistoryListItem hisItem) {
        if (periodId != null && hisItem != null) {
            EdsUser user = userManager.getUser();
            if (user instanceof EdsEmployee) {
                user = userManager.get(user.getObjectID());
            }
            EdsLaborPeriodHistory laborPeriodHistory = new EdsLaborPeriodHistory();
            laborPeriodHistory.setLabourPeriod(labourPeriodManager.get(periodId));
            laborPeriodHistory.setCreationDate(new Date());
            laborPeriodHistory.setUser(user);
            laborPeriodHistory.setSuperUser(ServerUtils.isSuperUser());
            laborPeriodHistory.setText(hisItem.getComment());

            periodHistoryManager.create(laborPeriodHistory);
            return laborPeriodHistory.getObjectID();
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<HistoryNote> loadLaborPeriodHistory(Integer periodID) {
        List<EdsLaborPeriodHistory> historyList = periodHistoryManager.getComments(periodID);
        if (historyList == null) {
            historyList = new ArrayList<>();
        }

        ArrayList<HistoryNote> noteItemsList = new ArrayList<>();
        for (EdsLaborPeriodHistory item : historyList) {
            if (org.apache.commons.lang3.StringUtils.isNotBlank(item.getText())) {
                HistoryListItem historyListItem = new HistoryListItem();
                historyListItem.setObjectID(item.getObjectID());
                historyListItem.setEmployee(item.getUser().getFullName());
                historyListItem.setEmployeeID(item.getUser().getObjectID());
                historyListItem.setComment(commonLocalizer.localize(item.getText().toLowerCase(), item.getText()));
                historyListItem.setEventDate(item.getCreationDate());

                noteItemsList.add(historyListItem);
            }
        }
        return noteItemsList;
    }

    @Transactional
    @Override
    public void deleteLaborPeriodHistory(Integer periodHistoryID) {
        EdsLaborPeriodHistory requestComment = periodHistoryManager.get(periodHistoryID);
        requestComment.setDeleted(true);
        periodHistoryManager.createOrUpdate(requestComment);
    }

    private LinkedHashMap<Integer, AnnualLeaveItem> getEmployeeLeaveData(final Integer employeeId, final Integer selectedYear) {
        final LinkedHashMap<Integer, AnnualLeaveItem> leaveItemLinkedHashMap = new LinkedHashMap<>();
        final EdsEmployee employee = this.employeeManager.get(employeeId);

        final Calendar c = Calendar.getInstance();
        final List<Integer> employeeids = new ArrayList<>();
        employeeids.add(employeeId);
        final Integer cureentYear = selectedYear != null ? selectedYear : c.get(Calendar.YEAR);
        final List<EdsAnnualLeaveAllowance> employeeLeaveAllowances = this.annualLeaveAllowanceManager.getLeaveAllowance(cureentYear, employeeids);
        for (final EdsAnnualLeaveAllowance leaveAllowance : employeeLeaveAllowances) {
            if ((leaveAllowance.getReason() != null && !Constants.LR_TYPE_UNAUTHORIZED_LEAVE.equals(leaveAllowance.getReason().getCode())) &&
                    ((leaveAllowance.getReason().getGender() != null && employee.getProfile().getGender() != null
                            && employee.getProfile().getGender().equalsIgnoreCase(leaveAllowance.getReason().getGender().getName()))
                            || (leaveAllowance.getReason().getGender() == null || employee.getProfile().getGender() == null))) {
                final AnnualLeaveItem empLeaveItem = new AnnualLeaveItem();
                empLeaveItem.setAddPrevious(leaveAllowance.getAddPrevious());
                empLeaveItem.setAllowanceYear(leaveAllowance.getAllowanceYear());
                empLeaveItem.setAnnualallowancedays(leaveAllowance.getAllowanceDays());
                empLeaveItem.setEmployeeId(employeeId);
                empLeaveItem.setReasonId(leaveAllowance.getReason().getObjectID());
                empLeaveItem.setReasonCode(leaveAllowance.getReason().getCode());
                empLeaveItem.setReasonName(leaveAllowance.getReason().getName());
                //Get lasy year allowance days by previous year, reason and employee

                final ListingFilterParameter fp = new ListingFilterParameter();
                fp.setYear(cureentYear - 1);
                fp.setEmployeeId(employeeId);
                fp.setStatusCode(Constants.LR_STATUS_SS_APPROVED);
                fp.setReasonCode(leaveAllowance.getReason().getCode());
                fp.setPaid(true);

                final HashMap<Integer, Double> duration = this.sickRequestDurationManager.getEmployeeLeaveDurations(fp);
                final Map<Integer, EdsAnnualLeaveAllowance> allowance = this.annualLeaveAllowanceManager.getAllowancesMapByYearAndReasonAndEmployee(cureentYear - 1, leaveAllowance.getReason().getCode(), employeeids);

                Double dayTaken = 0d;
                Double allowanceDays = 0d;
                final Double durationArray = duration.get(employeeId);
                if (durationArray != null) {
                    dayTaken = durationArray;
                }
                final EdsAnnualLeaveAllowance edsLeaveAllowance = allowance.get(employeeId);
                if (edsLeaveAllowance != null) {
                    allowanceDays = edsLeaveAllowance.getAllowanceDays();
                }

                empLeaveItem.setLastAllowanceDays(allowanceDays - dayTaken);
                if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION)) {
                    leaveItemLinkedHashMap.put(leaveAllowance.getReason().getObjectID(), empLeaveItem);
                }
            }
        }
        this.getFullEmployeeAllowance(leaveItemLinkedHashMap, employeeId);
        return leaveItemLinkedHashMap;
    }

    private LinkedHashMap<Integer, AnnualLeaveItem> getFullEmployeeAllowance(final LinkedHashMap<Integer, AnnualLeaveItem> leaveItemLinkedHashMap, final Integer employeeId) {
        final List<EdsLeaveReason> references = this.leaveReasonManager.listActiveReasons();
        final Map<Integer, AnnualLeaveItem> annualItemLinkedHashMap = new LinkedHashMap<>();
        final EdsEmployee employee = this.employeeManager.get(employeeId);

        for (final EdsLeaveReason reason : references) {
            if (Constants.LR_TYPE_UNAUTHORIZED_LEAVE.equals(reason.getCode())) {
                continue;
            }
            if (!leaveItemLinkedHashMap.containsKey(reason.getObjectID()) && ((reason.getGender() != null && employee.getProfile().getGender() != null
                    && employee.getProfile().getGender().equalsIgnoreCase(reason.getGender().getName()))
                    || (reason.getGender() == null || employee.getProfile().getGender() == null))) {
                final AnnualLeaveItem empLeaveItem = new AnnualLeaveItem();
                empLeaveItem.setAddPrevious(false);
                empLeaveItem.setAllowanceYear(ServerUtils.getYear(new Date()));
                empLeaveItem.setAnnualallowancedays(0.0);
                empLeaveItem.setReasonId(reason.getObjectID());
                if ("LR_TYPE_ANNUAL_LEAVE".equals(reason.getCode())) {
                    empLeaveItem.setReasonName(reason.getName());
                } else {
                    empLeaveItem.setReasonName(this.referenceWfmMessageSource.localize(this.leaveReasonManager.get(reason.getObjectID()) != null ? this.leaveReasonManager.get(reason.getObjectID()).getCode() : null, reason.getName()));
                }
                empLeaveItem.setReasonCode(reason.getCode());
                empLeaveItem.setRequestList(getPeriodList(employeeId));

                if ("LR_TYPE_ANNUAL_LEAVE".equals(reason.getCode())) {
                    annualItemLinkedHashMap.put(reason.getObjectID(), empLeaveItem);
                } else {
                    leaveItemLinkedHashMap.put(reason.getObjectID(), empLeaveItem);
                }
            }
        }
        leaveItemLinkedHashMap.putAll(annualItemLinkedHashMap);
        return leaveItemLinkedHashMap;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPositionsListForEmployeeEdit(ListingFilterParameter fp) {
        final EdsReference reference = this.referenceManager.getByCode(Constants.POS_STATUS_FROZEN);
        if (reference == null) {
            return new SelectItem[0];
        }
        List<EdsPosition> positionList = this.positionManager.getPositionListByStatusID(reference.getObjectID(), fp);

        return positionList.stream()
                .map(position -> new SelectItem(position.getObjectID(), position.getNumberData() + " --> " + position.getName()))
                .toArray(SelectItem[]::new);
    }

    @Override
    public void saveStepColumnChanges(final EmployeeStepItem item, final String columnCode) {
        final EdsStepEmployee step = this.stepEmployeeManager.get(item.getObjectID());
        step.clear();
        if (EmployeeStepItem.STATUS.equals(columnCode)) {
            step.setEntityStatus(item.getStatusID() != null ? this.referenceManager.get(item.getStatusID()) : null);
        } else {
            EdsEmployeeStepCustomFields edsCrmCustomFields = step.getEmployeeStepCustomFields();
            if (edsCrmCustomFields == null) {
                edsCrmCustomFields = new EdsEmployeeStepCustomFields();
                this.employeeStepCFManager.create(edsCrmCustomFields);
                step.setEmployeeStepCustomFields(edsCrmCustomFields);
            }
            final Object ob = CustomFieldsUtils.getObjectValue(edsCrmCustomFields, columnCode);
            if (ob != null) {
                if (ob instanceof String) {
                    final String text = (String) ob;
                    if (!text.equals(item.getCustomFieldsMap().get(columnCode))) {
                        step.addChange(columnCode);
                    }
                } else if (ob instanceof Number) {
                    final String text = String.valueOf(((Double) ob).intValue());
                    if (!text.equals(item.getCustomFieldsMap().get(columnCode))) {
                        step.addChange(columnCode);
                    }
                } else if (ob instanceof Date date) {
                    if (!date.equals(item.getCustomFieldsMap().get(columnCode))) {
                        step.addChange(columnCode);
                    }
                }
            } else {
                step.addChange(columnCode);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(edsCrmCustomFields, item.getCustomFieldsMap(), columnCode);
        }
        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, step, this.stepEmployeeManager.getUser());
        workflowEvent.setEntityType(step.getOnboardingStep().getFormID());
        try {
            employeeStepSolrComponent.index(step);
        } catch (final SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional(propagation = Propagation.NEVER)
    public void createReportXmlTemplate(final String stepName, final Integer stepId) {
        final Integer companyId = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        final ArrayList<SelectItem> roles = this.reportingServiceLocal.getTemplateRoles(companyId, null);
        final EdsReportTemplateCategory templateCategory = this.reportTemplateCategoryManager.getReportTemplateCategory("HRMS");
        final EdsReportTemplate reportTemplate = this.reportTemplateManager.getByCode("ONBOARTINGSTEPCOPYTEMPLATE");
        if (reportTemplate == null) {
            System.out.println("COPY QILISH UCHUN DEFAULT ONBOARTINGSTEP TEMPLATE TOPILMADI!!!");
        }

        final Integer[] companyIDs = new Integer[1];
        companyIDs[0] = companyId;
        final ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setSelected(true);
        filterParameter.setName(stepName + " (" + companyId + ")");
        filterParameter.setDescription(reportTemplate.getBody().replace("{copy_onboarding_step_name}", stepName).replace("{copy_onboarding_step_id}", stepId.toString()));
        if (templateCategory != null) {
            filterParameter.setCategoryID(templateCategory.getObjectID());
        }
        filterParameter.setCategory(templateCategory.getCode());
        filterParameter.setCompaines(companyIDs);
        filterParameter.setLibrary(false);
        filterParameter.setDeleted(true);
        filterParameter.setColumnsOfListing(new ArrayList<>());
        filterParameter.setIsSimpilifiedReportTemplate(false);
        filterParameter.setStepID(stepId);

        final ArrayList<String> checkedRoles = new ArrayList<>();
        for (final SelectItem role : roles) {
            if ("ACCOUNTANT".equals(role.getDescription()) || "ADMIN".equals(role.getDescription()) || "DR".equals(role.getDescription()) || "SALESMAN".equals(role.getDescription())) {
                checkedRoles.add(role.getDescription());
            }
        }
        if (checkedRoles.size() > 0) {
            filterParameter.setColumnsOfListing(checkedRoles);
        }
        this.reportingServiceLocal.saveOrUpdateReportTemplate(filterParameter);
    }

    @Override
    public Integer companyIdForCertificatePermissions() {
        Integer companyId = null;
        if (this.userManager.getUser() != null && this.userManager.getUser().getCompany() != null) {
            companyId = this.userManager.getUser().getCompany().getObjectID();
        }
        return companyId;
    }

    @Override
    public ListResult<HistoryItem> getEmployeeUpdatesList(final ListingFilterParameter fp) {
        final ArrayList<HistoryItem> itemList = (ArrayList<HistoryItem>) this.changesManager.changeList(fp);
        final Long totalCount = this.changesManager.getChangesCount(fp);
        return new ListResult<>(itemList, totalCount != null ? totalCount.intValue() : 0);
    }

    @Override
    public boolean saveDependantEditCellValue(final DependentItem rowValue, final String columnCodeName) {
        final EdsDependent dependent = this.dependentManager.get(rowValue.getObjectId());
        try {
            EdsDependentCustomFields dependentCustomfield = dependent.getCustomFields();
            if (dependentCustomfield == null) {
                dependentCustomfield = new EdsDependentCustomFields();
                this.dependentCFManager.create(dependentCustomfield);
                dependent.setCustomFields(dependentCustomfield);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(dependentCustomfield, rowValue.getCustomFieldsMap(), columnCodeName);
            return true;
        } catch (final Exception e) {
            HrmsServiceImpl.log.error("Dependant List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }

    @Override
    public boolean saveOnboardingStepEditCellValue(final OnboardingItem rowValue, final String columnCodeName) {
        final EdsOnboardingStep onboardingStep = this.onboardingStepManager.get(rowValue.getStepId());
        try {
            EdsOnboardingStepCustomFields onboardingStepCF = onboardingStep.getOnboardingStepCustomFields();
            if (onboardingStepCF == null) {
                onboardingStepCF = new EdsOnboardingStepCustomFields();
                this.onboardingStepCFManager.create(onboardingStepCF);
                onboardingStep.setOnboardingStepCustomFields(onboardingStepCF);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(onboardingStepCF, rowValue.getCustomFieldsMap(), columnCodeName);
            return true;
        } catch (final Exception e) {
            HrmsServiceImpl.log.error("Onboarding stage List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }

    @Override
    public PerformanceNoteItem[] getPerformanceNoteItems(final Integer employeeID) {
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPerformanceNote.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "View Performance Note Items");
        final ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(employeeID);
        final List<EdsPerformanceNote> performanceNotes = this.performanceNoteManager.getList(fp);
        final PerformanceNoteItem[] performanceNoteItems = new PerformanceNoteItem[performanceNotes.size()];
        int i = 0;
        for (final EdsPerformanceNote performanceNote : performanceNotes) {
            performanceNoteItems[i] = performanceNote.getRPC();
            if (performanceNote.getStatus() != null) {
                performanceNoteItems[i].setStatusName(this.referenceWfmMessageSource.localizeRef(performanceNote.getStatus()));
            }
            i++;
        }
        return performanceNoteItems;
    }

    @Override
    public void deletePerformanceNote(final Integer int_objectID) {
        final EdsUser user = this.userManager.getUser();
        final EdsPerformanceNote performanceNote = this.performanceNoteManager.get(int_objectID);

        performanceNote.setUpdater(user);
        performanceNote.setLastUpdateTime(new Date());
        performanceNote.setDeleted(true);
        this.performanceNoteManager.update(performanceNote);

        if (performanceNote.getIncident()) {
            //delete incident attachments
            final List<FileResource> attachments = this.attachmentUtilsManager.getAttachments(Constants.F_INCIDENT, int_objectID, int_objectID);
            final List<Integer> incidentAttachmentIds = new ArrayList<>();
            for (final FileResource incidentAttachment : attachments) {
                incidentAttachmentIds.add(incidentAttachment.getObjectId());
            }
            this.commonServiceLocal.deleteFiles(incidentAttachmentIds);
        }

        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPerformanceNote.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(int_objectID);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Delete " + (performanceNote.getIncident() ? "incident" : "performance note"));
        if (performanceNote.getIncident()) {
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, performanceNote, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_INCIDENT);
        }
    }

    @Override
    public Integer savePerformanceNote(final PerformanceNoteItem item) {
        final EdsPerformanceNote performanceNote;
        if (item.getObjectID() == null) {
            performanceNote = new EdsPerformanceNote();
        } else {
            performanceNote = this.performanceNoteManager.get(item.getObjectID());
        }
        //performance note(incident) name
        performanceNote.setName(item.getName());
        //performance note(incident) description
        if (item.getDescription() != null) {
            performanceNote.setDescription(item.getDescription());
        }
        //performance note(incident) start date
        performanceNote.setDate_start(item.getStartDate().getNonConvertedDate());
        //performance note(incident) end date
        performanceNote.setDate_end(item.getEndDate().getNonConvertedDate());
        //performance note(incident) status
        if (item.getStatusID() != null) {
            performanceNote.setStatus(this.referenceManager.get(item.getStatusID()));
        }
        if (item.getPriorityID() != null) {
            performanceNote.setPriority(this.referenceManager.get(item.getPriorityID()));
        } else {
            performanceNote.setPriority(null);
        }
        //performance note(incident) related to employee ID
        if (item.getRelatedToID() != null) {
            performanceNote.setRelatedTo(this.userManager.get(item.getRelatedToID()));
        }
        //performance note(incident) resolver ID
        if (item.getResolverID() != null) {
            performanceNote.setResolver(this.userManager.get(item.getResolverID()));
        }
        //performance note(incident) reporter ID
        if (item.getReportedByID() != null) {
            performanceNote.setReportedBy(this.userManager.get(item.getReportedByID()));
        }
        //performance note OR incident
        performanceNote.setIncident(item.isIncident());
        //performance note(incident) visibility (Public or Private)
        performanceNote.setVisibility(item.isPublic());
        //kpi log
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsPerformanceNote.class.getSimpleName());

        final String message;
        final KpiLog.ActionType actionType;
        if (performanceNote.getObjectID() == null) {
            actionType = KpiLog.ActionType.ADD;
            message = "Add " + (item.isIncident() ? "Incident" : "Performance Note");
        } else {
            actionType = KpiLog.ActionType.UPDATE;
            message = "Update " + (item.isIncident() ? "Incident" : "Performance Note");
        }
        //performance note(incident) create OR update
        this.performanceNoteManager.createOrUpdate(performanceNote);

        kpiLog.setActionType(actionType);
        kpiLog.setEntityId(performanceNote.getObjectID());
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, message);

        //save incident attachments
        if (item.isIncident() && item.getAttachments() != null && item.getAttachments().length > 0) {
            this.attachmentUtilsManager.saveAttachments(Constants.F_INCIDENT, performanceNote.getObjectID(), performanceNote.getObjectID(), item.getAttachments());
        }

        if (item.isIncident()) {
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, item.getObjectID() != null ? BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT : BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, performanceNote, userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_INCIDENT);
        }
        //return performance note(incident) ID
        return performanceNote.getObjectID();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean checkCompetencyList(final Integer employeeId, final int type) {
        if (employeeId != null) {
            final List<?> list = this.skillManager.getSkillListByEmployeeByType(employeeId, type);
            if (list != null && list.size() > 0) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SkillItem[] getCompetencyList(Integer employeeId, final int type) {
        if (employeeId == null) {
            employeeId = this.referenceManager.getUser().getEmployee().getObjectID();
        }
        List<EdsSkill> skills = null;
        if (type == Constants.ASSESSMENT_SKILLS_SIMPLE) {
            skills = this.skillManager.getSkillListByEmployeeForSimple(employeeId);
        } else if (type == Constants.ASSESSMENT_SKILLS_360) {
            skills = this.skillManager.getSkillListByEmployeeFor360(employeeId);
        }
        SkillItem[] skillItems = null;
        if (skills.size() > 0) {
            skillItems = new SkillItem[skills.size()];
            int s = 0;
            for (final EdsSkill skill : skills) {
                final SkillItem skillItem = new SkillItem();
                skillItem.setDescription(this.hrmsLocalizer.localize(skill.getDescriptionCode(), skill.getDescription()));
                skillItem.setName(this.hrmsLocalizer.localize(skill.getCode(), skill.getName()));
                skillItem.setId(skill.getObjectID());
                skillItems[s] = skillItem;
                s++;
            }
        }
        final KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsSkill.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(HrmsServiceImpl.log, kpiLog, "Get competencies list");
        return skillItems;
    }

    @Override
    public GoalItem[] getOwnEmployeeGoalList(final ListingFilterParameter parameter) {
        if (parameter.getEmployeeId() == null) {
            parameter.setEmployeeId(this.goalManager.getUser().getObjectID());
        }

        final Date fromDate;
        final Date toDate;
        final List<EdsGoal> goalList;
        if (parameter.getValidityPeriodId() != null && parameter.getValidityPeriodId() != 0) {
            final EdsValidityPeriod validityPeriod = this.validityPeriodManager.get(parameter.getValidityPeriodId());
            fromDate = validityPeriod.getFromDate();
            toDate = validityPeriod.getToDate();

            parameter.setStartDate(fromDate);
            parameter.setDueDate(toDate);
            goalList = this.goalManager.getGoalsPeerAssign(parameter);
        } else {
            goalList = this.goalManager.getGoalsPeerAssignOutValidity(parameter);
        }


        final GoalItem[] results = new GoalItem[goalList.size()];
        int i = 0;
        for (final EdsGoal goal : goalList) {
            final GoalItem item = new GoalItem();

            item.setObjectId(goal.getObjectID());
            item.setDescription(goal.getDescription());
            item.setGoalCategory(this.referenceWfmMessageSource.localizeRef(goal.getGoalCategory()));
            item.setProgress(goal.getProgress());
            if (goal.getResolver() != null) {
                item.setResolver(goal.getResolver().getFullName());
            }
            if (goal.getStatus() != null) {
                item.setStatus(this.referenceWfmMessageSource.localizeRef(goal.getStatus()));
            }
            if (goal.getBusinessGoal() != null) {
                item.setCompanyGoal(goal.getBusinessGoal().getTitle());
            }
            item.setTitle(goal.getTitle());
            item.setProgress(goal.getProgress());

            final Set<EdsGoalAssignees> assignees = goal.getGoalAssigneeses();

            Integer weight = 0;
            int k = 0;
            final GoalAssigneeItem[] assignItems = new GoalAssigneeItem[1];
            for (final EdsGoalAssignees goalAssignee : assignees) {
                if (parameter.getEmployeeId().equals(goalAssignee.getAssignee().getObjectID())) {
                    final GoalAssigneeItem assignItem = new GoalAssigneeItem();
                    assignItem.setObjectId(goalAssignee.getObjectID());
                    assignItem.setWeight(goalAssignee.getWeight());
                    assignItem.setId(goalAssignee.getAssignee().getObjectID());
                    assignItem.setName(goalAssignee.getName());
                    assignItem.setActual(goalAssignee.getActual());
                    assignItem.setTarget(goalAssignee.getTarget());
                    if (parameter.getValidityPeriodId() != null) {//to do
                        assignItem.setValidityPeriodId(parameter.getValidityPeriodId());
                    }
                    assignItems[k++] = assignItem;
                    weight = goalAssignee.getWeight().intValue();
                }
            }
            item.setWeight(weight);
            item.setGoalAssigneeItem(assignItems);
            results[i] = item;
            i++;
        }
        return results;
    }

    @Override
    public ListResult<HistoryItem> getAuditLogList(final ListingFilterParameter fp) {
//
//        final List<EdsAuditLog> logs = this.auditLogRepository.findAllByCompanyIdOrderByModificationDateDesc(ServerSecurityContext.getInstance().getCompanyId());
//
//        final Long count = this.auditLogRepository.countAllByCompanyId(ServerSecurityContext.getInstance().getCompanyId());
//
//        return new ListResult<>((ArrayList<HistoryItem>) logs.stream()
//                .map(EdsAuditLog::toRpc)
//                .collect(Collectors.toList()), count.intValue());
        return null;
    }

    @Override
    public HashMap<Double, String> getAssassmentRatings() {

        final List<EdsApprasialScoreType> appraisalRates = this.assessmentManager.getAppraisalScoreTypes();

        final HashMap<Double, String> asssasmentRatingsMap = new HashMap<>();
        if (appraisalRates != null && !appraisalRates.isEmpty()) {
            for (final EdsApprasialScoreType scoreType : appraisalRates) {
                String name = scoreType.getName();
                asssasmentRatingsMap.put(scoreType.getRate(), name);
            }
        }
        return asssasmentRatingsMap;
    }

    @Override
    public HashMap<String, String[]> getAssassmentGrades() {

        final List<EdsApprasialScoreType> appraisalRates = this.assessmentManager.getAppraisalScoreTypes();

        final HashMap<String, String[]> asssasmentRatingsMap = new HashMap<>();
        if (appraisalRates != null && !appraisalRates.isEmpty()) {
            for (final EdsApprasialScoreType scoreType : appraisalRates) {
                String rate = String.valueOf(scoreType.getRate());
                String name = scoreType.getName();
                asssasmentRatingsMap.put(scoreType.getGrade(), new String[]{rate, name});
            }
        }
        return asssasmentRatingsMap;
    }

    @Override
    public ListResult<GroupGoalITem> getGroupGoalList(final ListingFilterParameter filterParametrs) {
        final List<EdsGroupGoal> groupGoalITems = this.groupGoalManager.getList(filterParametrs);
        final Integer totalCount = this.groupGoalManager.getTotalCount(filterParametrs);

        final ArrayList<GroupGoalITem> result = new ArrayList<>();
        for (final EdsGroupGoal edsGroupGoal : groupGoalITems) {
            result.add(edsGroupGoal.getRPC());
        }
        return new ListResult<>(result, totalCount);
    }

    @Override
    public void createGroupGoals(final GroupGoalITem groupGoalITem) {
        final EdsGroupGoal edsGroupGoal;
        boolean isNew = true;
        if (groupGoalITem.getObjectId() != null) {
            isNew = false;
            edsGroupGoal = this.groupGoalManager.get(groupGoalITem.getObjectId());
        } else {
            edsGroupGoal = new EdsGroupGoal();
        }
        edsGroupGoal.setEmployee(this.userManager.get(groupGoalITem.getEmployee().getId()));
        if (groupGoalITem.getValidityPeriod() != null) {
            edsGroupGoal.setValidityPeriod(this.validityPeriodManager.get(groupGoalITem.getValidityPeriod().getId()));
        }
        edsGroupGoal.setFromDate(groupGoalITem.getFromDate().getNonConvertedDate());
        edsGroupGoal.setToDate(groupGoalITem.getToDate().getNonConvertedDate());

        final EdsReference goalCategory = this.referenceManager.findReference(EdsGoal._GOAL_CATEGORY, EdsGoal.PERSONAL_GOAL);
        final EdsReference submittedStatus = this.referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED);
        final EdsReference approveStatus = this.referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS);
        final EdsReference currentStatus = groupGoalITem.isSelfApprover() ? approveStatus : submittedStatus;


        final EdsUser user = this.userManager.getUser();
        final EdsAuditInfo info = edsGroupGoal.getAuditInfo();
        info.setModificationDate(new Date());
        info.setModifiedBy(user);
        if (isNew) {
            info.setCreationDate(new Date());
            info.setCreatedBy(user);
        }
        edsGroupGoal.setAuditInfo(info);

        this.groupGoalManager.createOrUpdate(edsGroupGoal);

        if (isOk(groupGoalITem.getApprovers())) {
            groupGoalITem.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirst = true;
            for (final ApproverItemMini approverItem : groupGoalITem.getApprovers()) {
                final EdsApprover _edsApprover = this.approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    this.approverManager.update(_edsApprover);
                    if (edsGroupGoal.getCurrentApprover() != null && isFirst) {
                        edsGroupGoal.getCurrentApprover().setStatus(currentStatus);
                        edsGroupGoal.setEntityStatus(submittedStatus);
                        isFirst = false;
                    } else if (edsGroupGoal.getCurrentApprover() != null) {
                        edsGroupGoal.getCurrentApprover().setStatus(submittedStatus);
                    }
                    if (!groupGoalITem.isSelfApprover()) {
                        edsGroupGoal.setEntityStatus(submittedStatus);
                    }
                    if (edsGroupGoal.isCurrentApproverRejected()) {
                        edsGroupGoal.setEntityStatus(edsGroupGoal.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                final EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(edsGroupGoal.getObjectID());
                edsApprover.setIs_default(false);

                if (isFirst) {
                    edsApprover.setStatus(currentStatus);
                    edsGroupGoal.setEntityStatus(submittedStatus);
                    isFirst = false;
                } else {
                    edsApprover.setStatus(submittedStatus);
                }
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    final EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                    edsApprover.setExactEmployee(user_);
                }
                edsApprover.setApproverRoles(new HashSet<>());
                edsApprover.setApproverEmployees(new HashSet<>());
                edsApprover.setDynamicQueries(new HashSet<>());
                this.approverManager.createOrUpdate(edsApprover);

                for (final EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                    edsApprover.getApproverRoles().add(roleapp);
                }

                for (final EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                    edsApprover.getApproverEmployees().add(ucerapp);
                }

                if (edsGroupGoal.getCurrentApprover() == null) {
                    edsGroupGoal.setCurrentApprover(edsApprover);
                }
                edsGroupGoal.getApprovers().add(edsApprover);
            }
        }

        for (final GoalItem goalItem : groupGoalITem.getGoalItems()) {
            goalItem.setStatusId(currentStatus.getObjectID());
            goalItem.setStatus(currentStatus.getName());
            goalItem.setValidityPeriodItem(groupGoalITem.getValidityPeriod());
            goalItem.setFromDate(groupGoalITem.getFromDate());
            goalItem.setToDate(groupGoalITem.getFromDate());
            goalItem.getGoalAssigneeItem()[0].setId(groupGoalITem.getEmployee().getId());
            goalItem.setGoalCategoryId(goalCategory.getObjectID());
            goalItem.setGoalCategory(goalCategory.getName());

            Integer goalID = this.saveGoalItem(goalItem);
            final EdsGoal goal = goalManager.get(goalID);
            goal.setGroupID(edsGroupGoal.getObjectID());
            edsGroupGoal.getGoals().add(goal);
            this.goalManager.update(goal);
        }

        if (isNew) {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                    BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsGroupGoal, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_GROUP_GOAL);
        } else {
            final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                    BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsGroupGoal, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_GROUP_GOAL);
        }

        final EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsGroupGoal, this.userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_GROUP_GOAL);
    }

    @Override
    public GroupGoalITem getGroupGoalData(final Integer objectId) {
        final EdsGroupGoal edsGroupGoal = this.groupGoalManager.get(objectId);
        final GroupGoalITem item = edsGroupGoal.getRPC();
        final ArrayList<GoalItem> goalItems = new ArrayList<>();
        if (edsGroupGoal.getGoals() != null) {
            for (final EdsGoal goal : edsGroupGoal.getGoals()) {
                goalItems.add(this.editGoal(goal.getObjectID(), Constants.PERSONAL_GOAL));
            }
        }
        item.setGoalItems(goalItems);
        return item;
    }

    @Override
    public SelectItem getEmployeeAsSelectItem(final Integer objectId) {
        final EdsEmployee employee = this.employeeManager.get(objectId);
        if (employee != null) {
            final String code = employee.getProfile() != null && !"".equals(employee.getProfile().getEmployeeCode()) ? employee.getProfile().getEmployeeCode() : "";
            return new SelectItem(employee.getObjectID(), (!"".equals(employee.getProfile().getEmployeeCode().replace("null", "").trim()) ? code + " - " : "") + employee.getFullName());
        }
        return null;
    }

    @Override
    public boolean deleteCertificate(final Integer objectId) {
        if (objectId != null) {
            final EdsCertificateOfEmployment employment = this.certificateOfEmploymentManager.get(objectId);
            if (employment != null) {
                employment.setDeleted(true);
                this.certificateOfEmploymentManager.createOrUpdate(employment);

                try {
                    solrManager.removeCertificate(employment.getObjectID(), SecurityContext.getCompanyID());
                } catch (IOException | SolrServerException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    public EdsEmployeeItemTableCF saveCustomTableFields(EdsEmployeeItemTableCF customfField, List<CompanyCustomFieldItem> customFieldItems) {
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
                customfField = new EdsEmployeeItemTableCF();
                employeeItemTableCFManager.create(customfField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customfField, customFieldItems);
            return customfField;
        }
        return null;
    }

    private EdsPositionCustomFields createPositionCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsPositionCustomFields positionCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                positionCustomFields = positionCFManager.get(customFieldItems.get(0).getObjectId());
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
                positionCustomFields = new EdsPositionCustomFields();
                positionCFManager.create(positionCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(positionCustomFields, customFieldItems);
            return positionCustomFields;
        }
        return null;
    }

    @Transactional
    @Override
    public void insertEmployeePresentTime(EdsShift shift) {
        attendanceHoursManager.deleteByShiftId(shift.getObjectID());
        LinkedHashMap<Integer, List<ShiftItems>> items = shiftItemManager.getShiftItemsByGroupId(shift.getObjectID());
        Integer lookUpType = shift.getLookupType() != null ? shift.getLookupType() : BRIGADA_ID;
        DateNonConvertable period = new DateNonConvertable(shift.getPeriod());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        LinkedHashMap<String, DateNonConvertable> datesMap = new LinkedHashMap<>();
        boolean isCustom = shift.getEndDate() != null;
        items.entrySet().iterator().next().getValue().forEach(i -> {
            Date date = null;
            try {
                date = isCustom ? format.parse(i.getKey()) : new Date(period.getDate().getYear(), period.getDate().getMonth(), Integer.parseInt(i.getKey()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            datesMap.put(i.getKey(), new DateNonConvertable(date));
        });
        for (Integer groupId : items.keySet()) {
            List<ShiftItems> shifts = items.get(groupId);
            List<Integer> employees = lookUpType.equals(BRIGADA_ID) ?
                    shiftTeamsManager.getEmployeeIdsByShiftAndGroupId(shift.getObjectID(), groupId) :
                    Collections.singletonList(groupId);
            for (ShiftItems sh : shifts) {
                DateNonConvertable date = datesMap.get(sh.getKey());
                if (sh.getTimeSlotId() != null) {
                    EdsShiftSettings timeslot = shiftSettingsManager.get(sh.getTimeSlotId());
                    Date start = (Date) date.getDate().clone();
                    start.setHours(timeslot.getStartTime() / 60);
                    start.setMinutes(timeslot.getStartTime() % 60);

                    Date end = (Date) date.getDate().clone();
                    if (timeslot.getStartTime() > timeslot.getEndTime()) {
                        end.setDate(end.getDate() + 1);
                    }
                    end.setHours(timeslot.getEndTime() / 60);
                    end.setMinutes(timeslot.getEndTime() % 60);
                    EmployeePresentItem item = new EmployeePresentItem(null, date, new DateNonConvertable(start), new DateNonConvertable(end), null, shift.getObjectID(), !lookUpType.equals(OVERTIME) ? timeslot.getObjectID() : null);
                    employees.forEach(e -> {
                        item.setEmployeeId(e);
                        commonService.saveAttendanceHour(item);
                    });
                } else if (lookUpType.equals(BRIGADA_ID)) {
                    EmployeePresentItem item = new EmployeePresentItem(null, date, date, date, null, shift.getObjectID(), null);
                    employees.forEach(e -> {
                        item.setEmployeeId(e);
                        commonService.saveAttendanceHour(item);
                    });
                }
            }
        }
    }

    @Override
    public void deleteTimeRecordsByShiftId(Integer shiftId) {
        attendanceHoursManager.deleteByShiftId(shiftId);
    }

    private String getOvertimePeriodCheckQuery(HashMap<Integer, ArrayList<String>> periodMap) {
        StringBuilder sbMain = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        int total = periodMap.size();

        for (Map.Entry<Integer, ArrayList<String>> entry : periodMap.entrySet()) {
            List<String> v = entry.getValue();
            sb.setLength(0);
            for (int i = 0; i < v.size(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append("'").append(v.get(i)).append("'");
            }
            sbMain.append("shi.groupid = ").append(entry.getKey())
                    .append(" AND shi.shift_settings_id IS NOT NULL AND shi.key IN (")
                    .append(sb).append(")");
            if (++counter < total) {
                sbMain.append(" OR ");
            }
        }
        return sbMain.toString();
    }


    public void updateEmployeeByRotation(EdsRotation rotation) {
        //review but don't touch
        Set<EdsRotationItemTable> itemTables = rotation.getItemTables();
        EdsUser user = userManager.getUser();
        ArrayList<CompanyCustomFieldItem> rotationCfs = this.commonService.getCompanyCustomFields(ViewName.RotationItemTable);
        ArrayList<CompanyCustomFieldItem> employeeCfs = this.commonService.getCompanyCustomFields(ViewName.Employee);
        for (EdsRotationItemTable itemTable : itemTables) {
            EdsEmployee employee = employeeManager.get(itemTable.getEmpId());
            Set<EdsEmployeeExperienceItemTable> experienceItemTables = employee.getExperienceItemTables();
            EdsEmployeeExperienceItemTable experience = new EdsEmployeeExperienceItemTable();
            HashMap<String, CompanyCustomFieldItem> employeeCfMap = CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(),
                            employeeCfs).stream()
                    .collect(Collectors.toMap(CompanyCustomFieldItem::getAliasName, x -> x, (k1, k2) -> k1, HashMap::new));
            List<CompanyCustomFieldItem> rotationCustomFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(), rotationCfs);

            for (CompanyCustomFieldItem rotationCustomFieldItem : rotationCustomFieldItems) {
                if (employeeCfMap.get(rotationCustomFieldItem.getAliasName()) != null && !ServerUtils.isNullOrEmpty(rotationCustomFieldItem.getFieldStringValue())) {
                    employee.addHistoryChange(employeeCfMap.get(rotationCustomFieldItem.getAliasName()).getFieldName(), employeeCfMap.get(rotationCustomFieldItem.getAliasName()).getFieldStringValue(), rotationCustomFieldItem.getFieldStringValue());
                    employeeCfMap.get(rotationCustomFieldItem.getAliasName()).setFieldStringValue(rotationCustomFieldItem.getFieldStringValue());
                    employeeCfMap.get(rotationCustomFieldItem.getAliasName()).setSelectedId(rotationCustomFieldItem.getSelectedId());
                    employeeCfMap.get(rotationCustomFieldItem.getAliasName()).setFieldDateNonConvertedValue(rotationCustomFieldItem.getFieldDateNonConvertedValue());
                }
                if (CustomFormConstants.QUALIFICATION.equals(rotationCustomFieldItem.getAliasName()) && rotationCustomFieldItem.getSelectedId() != null) {
                    employee.setQualification(referenceManager.get(rotationCustomFieldItem.getSelectedId()));
                }
            }
            employee.setCustomFields(this.saveEmployeeCustomFields(employee.getCustomFields(), new ArrayList<>(employeeCfMap.values())));
            EdsDepartment edsDepartment = null;
            if (itemTable.getNewDepID() != null) {
                edsDepartment = departmentManager.get(itemTable.getNewDepID());
                employeeDepartmentManager.deleteEmployeeDepartment(employee.getEmployeeDepartment(), rotation.getDate());
                EdsEmployeeDepartment newEmployeeDepartment = new EdsEmployeeDepartment();
                newEmployeeDepartment.setStartDate(rotation.getDate());
                newEmployeeDepartment.setTeam(edsDepartment);
                newEmployeeDepartment.setEmployee(employee);
                employeeDepartmentManager.create(newEmployeeDepartment);
                employee.setEmployeeTeam(newEmployeeDepartment);
                if (edsDepartment.getLeader() != null) {
                    employee.getProfile().setReportsTo(edsDepartment.getLeader());
                }
            }
            EdsPosition edsPosition = null;
            if (itemTable.getNewPosId() != null) {
                edsPosition = positionManager.get(itemTable.getNewPosId());
                employee.setPosition(edsPosition);
            }

            if (edsDepartment != null || edsPosition != null) {
                EdsCompanyPayrollSettings companySettingValue = companyPayrollSettingsManager.getCompanySettingValue(INDUSTRY_ID);
                if (companySettingValue != null) {
                    experience.setIndustryId(Integer.parseInt(companySettingValue.getValue()));
                }
                experience.setPositionId(edsPosition != null ? edsPosition.getObjectID() : (employee.getPosition() != null ? employee.getPosition().getObjectID() : null));
                experience.setHireDate(rotation.getDate());
                experience.setPosition(edsPosition != null ? edsPosition.getName() : (employee.getPosition() != null ? employee.getPosition().getName() : null));
                EdsEmployeeDepartment employeeDepartment = employee.getEmployeeDepartment();
                experience.setDepartmentId(edsDepartment != null ? edsDepartment.getObjectID() : (employeeDepartment != null && employeeDepartment.getTeam() != null ? employeeDepartment.getTeam().getObjectID() : null));
                experience.setDepartment(edsDepartment != null ? edsDepartment.getName() : (employeeDepartment != null && employeeDepartment.getTeam() != null ? employeeDepartment.getTeam().getName() : null));
                experience.setEdsEmployee(employee);
                employeeExperienceItemTableManager.create(experience);
                experienceItemTables.add(experience);
                employee.setExperienceItemTables(experienceItemTables);
            }

            employeeManager.update(employee);
            EdsBusinessEvent event = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employee, user);
            event.setEntityType(RelationItem.TYPE_EMPLOYEE);
            try {
                solrManager.addEmployeeToIndex(employee);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void convertPlacementItems(EdsGroupPlacement placement) {
        placement.getItemTables().forEach(itemTable -> {
            if (itemTable.getType().equals(CANDIDATE_ID)) {
                PlacementItem placementItem = new PlacementItem();
                placementItem.setCandidateID(itemTable.getCandidateId());
                placementItem.setDepartmentID(itemTable.getCurDepId());
                placementItem.setLocationID(itemTable.getLocationId());
                placementItem.setPositionID(itemTable.getPosId());
                placementItem.setCandidateType(Constants.SIMPLE_CANDIDATE);
                placementItem.setApprovers(placement.getMiniApproversRPC());
                placementItem.setDateOffed(itemTable.getEffectiveDate());
                placementItem.setNumberData(recruitmentService.generatePlacementNumber());
                placementItem.setStatusID(referenceManager.getByCode(PLACEMENT_STATUS_APPROVED).getObjectID());
                placementItem.setGroupPlacementId(placement.getObjectID());
                recruitmentService.savePlacement(placementItem, null);
            } else {
                updateGroupPlacementItems(itemTable);
            }
        });
    }


    public void updateGroupPlacementItems(EdsGroupPlacementItemTable itemTable) {
        ArrayList<CompanyCustomFieldItem> employeeCfs = this.commonService.getCompanyCustomFields(ViewName.Employee);
        EdsEmployee employee = employeeManager.get(itemTable.getEmpId());
        HashMap<String, CompanyCustomFieldItem> employeeCfMap = CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(),
                        employeeCfs).stream()
                .collect(Collectors.toMap(CompanyCustomFieldItem::getAliasName, x -> x, (k1, k2) -> k1, HashMap::new));
        employee.setCustomFields(this.saveEmployeeCustomFields(employee.getCustomFields(), new ArrayList<>(employeeCfMap.values())));
        if (itemTable.getDepartment() != null) {
            employeeDepartmentManager.deleteEmployeeDepartment(employee.getEmployeeDepartment());
            EdsEmployeeDepartment newEmployeeDepartment = new EdsEmployeeDepartment();
            newEmployeeDepartment.setStartDate(itemTable.getEffectiveDate());
            newEmployeeDepartment.setTeam(departmentManager.get(itemTable.getCurDepId()));
            newEmployeeDepartment.setEmployee(employee);
            employeeDepartmentManager.create(newEmployeeDepartment);
            employee.setEmployeeTeam(newEmployeeDepartment);
        }
        if (itemTable.getPosition() != null) {
            employee.setPosition(positionManager.get(itemTable.getPosId()));
        }
        employeeManager.update(employee);
        try {
            solrManager.addEmployeeToIndex(employee);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public EmployeeListItem[] getPositionEmployees(Integer positionId) {
        final List<EdsEmployee> employeeList = this.employeeManager.getPositionEmployees(positionId);
        final int size = employeeList != null ? employeeList.size() : 0;
        final EmployeeListItem[] empItems = new EmployeeListItem[size];
        int index = 0;
        for (final EdsEmployee employee : employeeList) {
            final EmployeeListItem empItem = new EmployeeListItem();
            empItem.setObjectID(employee.getObjectID());
            empItem.setFullName(employee.getFullName());
            empItem.setFirstName(employee.getFirstName());
            empItem.setLastName(employee.getLastName());
            empItem.setDepartment(employee.getTeam().getName());
            empItem.setStatus(this.referenceWfmMessageSource.localize(employee.getAccountStatus().getCode(), employee.getAccountStatus().getName()));
            empItems[index++] = empItem;
        }
        return empItems;

    }

    @Override
    public Integer saveEmployeeLeaveAllowance(ProfileItem item, EdsUser modifiedBy) {
        Integer employeeID = profileManager.getEmployeeByPinfl(String.valueOf(item.getPinfl()));
        if (employeeID == null) {
            return null;
        }
        log.info("save employee by ID: " + employeeID + " for period " + item.getStartDate() + " allowance: " + item.getAllowance() + "; left days: " + item.getLeftLeaveDays());
        Double totalTakenLeaveDays = null;
        EdsLabourPeriod period = labourPeriodManager.getByEmployeeIdAndStartDate(employeeID, item.getStartDate());
        if (period != null) {
            Double approvedDays = labourPeriodManager.getTotalTakenLeaveDaysByPeriodId(period.getObjectID(), true);
            totalTakenLeaveDays = approvedDays != null ? approvedDays : 0;
            double adjust = (item.getAllowance() - totalTakenLeaveDays) - item.getLeftLeaveDays();
            if (item.getAllowance() < totalTakenLeaveDays + adjust) {
                return null;
            }
            period.setAllowance(item.getAllowance());
            period.setOutOfSystemDays(adjust);
            period.setModifiedDate(new Date());
            period.setModifiedBy(modifiedBy);
            labourPeriodManager.createOrUpdate(period);
            return period.getObjectID();
        }
        return null;
    }

    @Override
    public ListResult<SubscriptionItem> getSubscriptionList(ListingFilterParameter fp) {
        return new ListResult<>();
    }

    @Override
    public void deleteSubscriptionItem(Integer id) {

    }

    @Override
    public ListResult<SubscriptionUsageItem> getUsageList(ListingFilterParameter fp) {
        return new ListResult<>();
    }

    @Override
    public void deleteUsageItem(Integer id) {

    }

    @Override
    public Boolean deleteBackupEmployeeById(Integer backupId) {
        EdsUser user = userManager.getUser();
        EdsBackupsEmployee backupsEmployee = backupsEmployeeManager.get(backupId);

        if (backupsEmployee == null) {
            return true;
        }
        //validate for existance of additional payment
        if (!validateBackupEmployee(backupId, null, true)) {
            return false;
        }

        backupsEmployee.setUpdater(user);
        backupsEmployee.setUpdatedDate(new Date());
        backupsEmployee.setDeleted(true);
        backupsEmployeeManager.update(backupsEmployee);
        baseEventsPostProcessor.registerEvent(BackupsEmployeeEventListenerImpl.TYPE, MyUpdateItem.DELETE, backupsEmployee, user);

        payrollServiceLocal.deleteAdditionalPaymentForBackupEmployee(backupId);
        recurrenceService.deleteRecurrences(backupsEmployee.getObjectID(), SchedulerConstant.RECURRING_ADDITIONAL_PAYMENT);
        return true;
    }

    @Override
    public ListResult<BackupsEmployeeObject> getBackupsEmployeeList(ListingFilterParameter fp) {
        List<EdsBackupsEmployee> list = backupsEmployeeManager.getAllItems(fp, commonService.getCompanyCustomFields(ViewName.BackupsEmployee));
        ArrayList<BackupsEmployeeObject> result = new ArrayList<>();
        if (list.size() > 0) {
            for (EdsBackupsEmployee item : list) {
                BackupsEmployeeObject object = item.toRpc();
                if (item.getBackupEmployees() != null) {
                    object.setBackups(item.getBackupEmployees().stream().map(EdsBackupEmployee::getEmployee).map(EdsEmployee::getFullName).collect(Collectors.joining(",")));
                }
                result.add(object);
            }
        }
        return new ListResult<>(result, list.size());
    }


    private void saveBackupEmployee(BackupsEmployeeObject objectData, EdsBackupsEmployee backupsEmployee) {
        objectData.getBackupsEmployees().forEach((value) -> {
            EdsBackupEmployee parentBackupEmployee = null;
            ApproverItemMini parentBackupEmployeeItem = value.getParentBackupEmployee();
            if (value.getParentBackupEmployee() != null && value.getParentBackupEmployee().getObjectID() != null) {
                parentBackupEmployee = backupEmployeeManager.get(value.getParentBackupEmployee().getObjectID());
            }
            if (parentBackupEmployee != null) {
                parentBackupEmployee.setBackupsEmployees(backupsEmployee);
                backupEmployeeManager.create(parentBackupEmployee);
            } else {
                parentBackupEmployee = new EdsBackupEmployee();
                if (backupsEmployee.getEmployee() != null) {
                    parentBackupEmployee.setStartDate(setStartTime(backupsEmployee.getEmployee().getObjectID(), parentBackupEmployeeItem.getFromBackupEmployeeDate().getNonConvertedDate()));
                    if (parentBackupEmployeeItem.getDueBackupEmployeeDate() != null) {
                        parentBackupEmployee.setDueDate(setEndTime(backupsEmployee.getEmployee().getObjectID(), parentBackupEmployeeItem.getDueBackupEmployeeDate().getNonConvertedDate()));
                    }
                }
                parentBackupEmployee.setEmployees(employeeManager.get(parentBackupEmployeeItem.getExactEmployee().getId()));
                parentBackupEmployee.setSickRequest(null);
                parentBackupEmployee.setParentId(null);
                parentBackupEmployee.setBackupsEmployees(backupsEmployee);
                backupEmployeeManager.create(parentBackupEmployee);

                final Integer parentBackupEmpId = parentBackupEmployee.getObjectID();
                value.getChildList().forEach((child -> {
                    EdsBackupEmployee childBackupEmployee = new EdsBackupEmployee();
                    childBackupEmployee.setStartDate(child.getFromBackupEmployeeDate().getNonConvertedDate());
                    if (child.getDueBackupEmployeeDate() != null) {
                        childBackupEmployee.setDueDate(child.getDueBackupEmployeeDate().getNonConvertedDate());
                    }
                    childBackupEmployee.setEmployees(employeeManager.get(child.getExactEmployee().getId()));
                    childBackupEmployee.setSickRequest(null);
                    childBackupEmployee.setParentId(parentBackupEmpId);
                    childBackupEmployee.setBackupsEmployees(backupsEmployee);
                    backupEmployeeManager.create(childBackupEmployee);
                }));
            }
        });
    }

    @Override
    public BackupsEmployeeObject getBackupsEmployee(Integer objectId) {

        EdsBackupsEmployee backupsEmployee = backupsEmployeeManager.get(objectId);
        BackupsEmployeeObject object = new BackupsEmployeeObject();
        if (backupsEmployee != null) {
            object = backupsEmployee.toRpc();
            if (object.getSelectedEmployee() != null) {
                object.setReasons(availabilityService.getReasons(object.getSelectedEmployee().getId()));
            }
            object.setCustomReasonId(backupsEmployee.getCustomReasonId());
            NumberData numberData = generateBackupsEmployeeCode();
            if (object.getCode() != null && object.getIntNumber() != null) {
                numberData.setNumberString(object.getCode());
                numberData.setIntNumber(object.getIntNumber());
            }
            object.setNumberData(numberData);
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.BackupsEmployee);
            object.setCustomFieldItems((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(backupsEmployee.getCustomFields(), customFieldsItems));

            List<EdsBackupEmployee> backupEmployeeList = backupsEmployee.getBackupEmployees();
            List<BackupEmployeeItem> backupEmployeeItemList = new ArrayList<>();
            ArrayList<ApproverItemMini> collectChilds = new ArrayList<>();
            BackupEmployeeItem backupEmployeeItem = new BackupEmployeeItem();
            if (backupEmployeeList != null && backupEmployeeList.size() > 0) {
                for (EdsBackupEmployee edsBackupEmployee : backupEmployeeList) {
                    Integer parentId = edsBackupEmployee.getParentId();
                    if (parentId != null) {

                        EdsBackupEmployee parentBackupEmployee = backupEmployeeManager.get(parentId);

                        ApproverItemMini parent = getApproverItemByBackupEmployee(parentBackupEmployee);

                        collectChilds.add(parent);
                        for (EdsBackupEmployee backupEmployee : backupEmployeeManager.getChildrensByParentId(parentId)) {
                            ApproverItemMini approverItemByBackupEmployee = getApproverItemByBackupEmployee(backupEmployee);
                            collectChilds.add(approverItemByBackupEmployee);
                        }
                        backupEmployeeItem.setChildList(collectChilds);
                        backupEmployeeItem.setParentBackupEmployee(parent);
                        backupEmployeeItemList.add(backupEmployeeItem);
                        break;
                    } else if (backupEmployeeList.size() == 1) {
                        EdsBackupEmployee onlyParent = backupEmployeeManager.get(edsBackupEmployee.getObjectID());
                        ApproverItemMini parentOnly = getApproverItemByBackupEmployee(onlyParent);
                        backupEmployeeItem.setParentBackupEmployee(parentOnly);
                        collectChilds.add(parentOnly);
                        backupEmployeeItem.setChildList(collectChilds);
                        backupEmployeeItemList.add(backupEmployeeItem);
                    }
                }
            }

            object.setBackupsEmployees(backupEmployeeItemList);

        } else {
            EdsUser user = userManager.getUser();
            if (user != null) {
                SelectItem selectedUser = user.getAsSelectItem();
                object.setSelectedEmployee(selectedUser);
                object.setReasons(availabilityService.getReasons(user.getObjectID()));
            }

        }
        object.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_BACKUPS_EMPLOYEE));
        object.setTemplates(getBackupEmployeePdfTemplates(PdfReferenceCodeNameEnum.BACKUP_EMPLOYEE.name()).getItems());
        return object;
    }

    private CustomFormItemPdfTemplateList getBackupEmployeePdfTemplates(String type) {
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

    private ApproverItemMini getApproverItemByBackupEmployee(EdsBackupEmployee backupEmployee) {
        ApproverItemMini child = new ApproverItemMini();
        child.setObjectID(backupEmployee.getObjectID());
        child.setVozlojeniya(true);
        child.setExactEmployee(backupEmployee.getEmployee().getAsSelectItem());
        child.setFromBackupEmployeeDate(new DateNonConvertable(backupEmployee.getStartDate()));
        child.setDueBackupEmployeeDate(backupEmployee.getDueDate() != null ? new DateNonConvertable(backupEmployee.getDueDate()) : null);
        return child;
    }

    @Override
    public Boolean updateBackupsEmployeeItemsAndStatus(BackupsEmployeeObject objectData) {

        EdsBackupsEmployee backupsEmployee = backupsEmployeeManager.get(objectData.getId());

        //validate for existance of additional payment
        if (!validateBackupEmployee(objectData.getId(), objectData.getDueDate(), false)) {
            return false;
        }

        EdsUser user = userManager.getUser();
        EdsReference referenceStatus = referenceManager.findReference(Constants.BACKUPS_EMPLOYEE_STATUS, objectData.getStatusCode());
        boolean statusChanged;
        if (backupsEmployee != null) {

            statusChanged = !backupsEmployee.getStatus().equals(objectData.getStatusCode());

            backupsEmployee.setOverallStatus(referenceStatus);

            backupsEmployee.updateStatus(referenceStatus);
            backupsEmployee.setUpdater(user);
            backupsEmployeeManager.createOrUpdate(backupsEmployee);

            backupEmployeeManager.deleteByBackupsEmployeeId(backupsEmployee.getObjectID());

            // Save Backup Employee Parent Child
            if (objectData.getBackupsEmployees() != null && objectData.getBackupsEmployees().size() > 0) {
                saveBackupEmployee(objectData, backupsEmployee);
            }

            if (!BACKUPS_EMPLOYEE_APPROVED.equals(objectData.getStatusCode()) && !BACKUPS_EMPLOYEE_DRAFT.equals(objectData.getStatusCode()) && !statusChanged) {
                baseEventsPostProcessor.registerEvent(BackupsEmployeeEventListenerImpl.TYPE, MyUpdateItem.EDIT, backupsEmployee, userManager.getUser());
            }

            EdsBusinessEvent workflowEvent = baseEventsPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), backupsEmployee, user);
            workflowEvent.setEntityType(RelationItem.TYPE_BACKUPS_EMPLOYEE);

            if (BACKUPS_EMPLOYEE_SUBMITTED.equals(backupsEmployee.getStatus()) && objectData.isApprover()) {
                try {
                    messageManager.sendBackupsEmployeeToApprover(backupsEmployee);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (BACKUPS_EMPLOYEE_APPROVED.equals(backupsEmployee.getStatus()) && objectData.isApprover()) {
                try {
                    messageManager.sendBackupsEmployeeToEmployee(null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (statusChanged) {
                baseEventsPostProcessor.registerEvent(BackupsEmployeeEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, backupsEmployee, userManager.getUser());
            }
            return true;
        }
        return false;
    }

    public NumberData generateBackupsEmployeeCode() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = backupsEmployeeManager.getBackupsEmployeeLastIntNumber();
        if (intNumber == null) {
            intNumber = 0;
        }
        if (settings != null && settings.getBackupsEmployeeNumberingFormat() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getBackupsEmployeeNumberingFormat(), settings.getDelimetrBackupsEmployeeNumberingFormat(), null, null, null, "");
            numberData.setDelimiter(settings.getDelimetrBackupsEmployeeNumberingFormat());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_BACKUPS_EMPLOYEE_PREFIX /*true*/);
        }
    }

    public Date setStartTime(Integer employeeID, Date startDate) {
        EdsEmployee employee = employeeManager.get(employeeID);
        Set<EdsTimeSlotItem> timeSlotItems = employee.getTimeSlot().getItems();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        timeSlotItems.forEach(item -> {
            if (dayOfWeek == item.getDay()) {
                calendar.set(Calendar.HOUR_OF_DAY, item.getStartTime() / 60);
                calendar.set(Calendar.MINUTE, item.getStartTime() % 60);
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, 9);
                calendar.set(Calendar.MINUTE, 0);
            }
        });
        return calendar.getTime();
    }

    public Date setEndTime(Integer employeeID, Date date) {
        EdsEmployee employee = employeeManager.get(employeeID);
        Set<EdsTimeSlotItem> timeSlotItems = employee.getTimeSlot().getItems();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        timeSlotItems.forEach(item -> {
            if (dayOfWeek == item.getDay()) {
                calendar.set(Calendar.HOUR_OF_DAY, item.getEndTime() / 60);
                calendar.set(Calendar.MINUTE, item.getEndTime() % 60);
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, 18);
                calendar.set(Calendar.MINUTE, 0);
            }
        });
        return calendar.getTime();
    }

    @Override
    public Integer saveBackupsEmployee(BackupsEmployeeObject objectData) {

        EdsBackupsEmployee object = null;

        if (objectData == null) {
            return -1;
        }

        if (objectData.getId() != null) {
            object = backupsEmployeeManager.get(objectData.getId());

            //validate for existance of additional payment
            if (!validateBackupEmployee(objectData.getId(), objectData.getDueDate(), false)) {
                return -1;
            }
        }

        if (object == null) {
            object = new EdsBackupsEmployee();
        }
        object.setDate(new Date());
        if (objectData.getSelectedEmployee() != null) {
            object.setEmployee(employeeManager.get(objectData.getSelectedEmployee().getId()));
        } else {
            object.setEmployee(null);
        }

        if (objectData.getDepartment() != null) {
            EdsDepartment department = departmentManager.get(objectData.getDepartment().getId());
            if (department != null) {
                object.setDepartment(department);
            }
        }
        if (objectData.getPosition() != null) {
            EdsPosition position = positionManager.get(objectData.getPosition().getId());
            if (position != null) {
                object.setPosition(position);
            }
        }
        object.setDescription(objectData.getDescription());
        object.setPercentage(objectData.getPercentage());
        object.setIsNeedToSign(objectData.getIsNeedSignature());
        if (objectData.getReasonsId() != null) {
            EdsLeaveReason leaveReason = leaveReasonManager.get(objectData.getReasonsId());
            if (leaveReason != null) {
                object.setLeaveReason(leaveReason);
                object.setCustomReasonId(null);
            } else {
                object.setCustomReasonId(objectData.getReasonsId());
                object.setLeaveReason(null);
            }
        } else {
            object.setCustomReasonId(null);
            object.setLeaveReason(null);
        }
        EdsUser user = userManager.getUser();

        if (objectData.getId() == null) {
            object.setCreator(user);
            object.setCreatedDate(new Date());
            object.setDeleted(false);
        }

        object.setUpdater(user);
        object.setUpdatedDate(new Date());
        object.setOverallStatus(referenceManager.getByCode(objectData.getStatusCode()));
        object.setCustomFields(createBackupsEmployeCF(objectData.getCustomFieldItems()));

        if (objectData.getNumberData() != null && !objectData.getNumberData().getNumberString().equals("")) {
            object.setIntNumber(objectData.getNumberData().getIntNumber());
            object.setBackupEmployeecode(objectData.getNumberData().getNumberString());
        }

        backupsEmployeeManager.createOrUpdate(object);

        EdsBackupsEmployee finalObject = object;


        backupEmployeeManager.deleteByBackupsEmployeeId(finalObject.getObjectID());

        if (objectData.getBackupsEmployees() != null && !objectData.getBackupsEmployees().isEmpty()) {
            saveBackupEmployee(objectData, finalObject);
        }

        EdsReference submittedStatus = referenceManager.findReference(Constants.BACKUPS_EMPLOYEE_STATUS, Constants.BACKUPS_EMPLOYEE_SUBMITTED);
        boolean statusChanged = finalObject.getOverallStatus() != null && !objectData.getStatusCode().equals(finalObject.getOverallStatus().getCode());

        if (!isOk(objectData.getApprovers())) {
            object.setEntityStatus(referenceManager.findReference(Constants.BACKUPS_EMPLOYEE_STATUS, objectData.getStatusCode()));
        }

        if (isOk(objectData.getApprovers())) {
            objectData.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : objectData.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (object.getCurrentApprover() != null && objectData.getStatusCode() != null && isFirstApprover) {
                        object.getCurrentApprover().setStatus(referenceManager.findReference(Constants.BACKUPS_EMPLOYEE_STATUS, objectData.getStatusCode()));
                        object.setEntityStatus(submittedStatus);
                        isFirstApprover = false;
                    } else if (object.getCurrentApprover() != null && objectData.getStatusCode() != null) {
                        object.getCurrentApprover().setStatus(submittedStatus);
                    }
                    if (objectData.getStatusCode() != null && !ROTATION_APPROVED.equals(objectData.getStatusCode())) {
                        object.setEntityStatus(referenceManager.findReference(Constants.BACKUPS_EMPLOYEE_STATUS, objectData.getStatusCode()));
                    }
                    if (object.isCurrentApproverRejected()) {
                        object.setEntityStatus(object.getCurrentApprover().getStatus());
                    }
                    continue;
                }

                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(object.getObjectID());
                edsApprover.setIs_default(false);

                if (objectData.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.BACKUPS_EMPLOYEE_STATUS, objectData.getStatusCode()));
                    if (Constants.ROTATION_DRAFT.equals(objectData.getStatusCode())) {
                        object.setEntityStatus(referenceManager.findReference(Constants.BACKUPS_EMPLOYEE_STATUS, objectData.getStatusCode()));
                    } else {
                        object.setEntityStatus(submittedStatus);
                    }
                    isFirstApprover = false;
                } else if (objectData.getStatusCode() != null) {
                    edsApprover.setStatus(submittedStatus);
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

                if (object.getCurrentApprover() == null) {
                    object.setCurrentApprover(edsApprover);
                }

                object.getApprovers().add(edsApprover);
            }
        }

        if (objectData.getId() == null) {

            baseEventsPostProcessor.registerEvent(BackupsEmployeeEventListenerImpl.TYPE, MyUpdateItem.ADD, object, userManager.getUser());

            if (objectData.getStatusCode().equals(Constants.BACKUPS_EMPLOYEE_DRAFT)) {
                baseEventsPostProcessor.registerEvent(BackupsEmployeeEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, object, userManager.getUser());
            }

            if (objectData.getStatusCode().equals(Constants.BACKUPS_EMPLOYEE_APPROVED)) {
                baseEventsPostProcessor.registerEvent(BackupsEmployeeEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, object, userManager.getUser());
            }

        } else if (!Constants.BACKUPS_EMPLOYEE_APPROVED.equals(objectData.getStatusCode()) && !Constants.BACKUPS_EMPLOYEE_DRAFT.equals(objectData.getStatusCode())) {
            baseEventsPostProcessor.registerEvent(BackupsEmployeeEventListenerImpl.TYPE, MyUpdateItem.EDIT, object, userManager.getUser());
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventsPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), object, user);
        workflowEvent.setEntityType(RelationItem.TYPE_BACKUPS_EMPLOYEE);

        if (statusChanged || objectData.getStatusCode().equals(BACKUPS_EMPLOYEE_SUBMITTED)) {
            baseEventsPostProcessor.registerEvent(BackupsEmployeeEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, object, userManager.getUser());
        }

        return object.getObjectID();
    }

    @Override
    public Integer departmentGoalAvialableWeight(Integer departmentId) {
        return goalManager.getDepartmentGoalAvailableWeight(departmentId);
    }

    @Override
    public DepartmentGoalEmployeeHistoryItem getDepartmentGoalLogData(Integer historyId) {
        EdsDepartmentGoalEmployeeMetricHistory history = employeeMetricHistoryManager.getEmployeeMetricHistoryById(historyId);
        if (history != null) return history.toRpc();
        return new DepartmentGoalEmployeeHistoryItem();
    }

    @Override
    public Boolean saveDepartmentGoalLogData(DepartmentGoalEmployeeHistoryItem item) {


        EdsGoalAssignees goalAssignee =
                goalAssigneesManager.getGoalAssigneeByDepartmentGoalIdAndEmployeeId(
                        item.getDepartmentGoalId(),
                        item.getEmployeeId()
                );

        if (goalAssignee == null) {
            return false;
        }

        EdsDepartmentGoalEmployeeMetricHistory history = new EdsDepartmentGoalEmployeeMetricHistory();
        history.setAssignee(userManager.getUserByUserID(item.getEmployeeId()));
        history.setDate(item.getDate());
        history.setActual(item.getActual());
        history.setComment(item.getComment());
        history.setGoalAssignees(goalAssignee);
        history.setCreationDate(new Date());

        employeeMetricHistoryManager.create(history);

        updateAssigneeTotal(goalAssignee, item.getEmployeeId());

        return true;
    }



    @Override
    public Boolean editDepartmentGoalLogData(DepartmentGoalEmployeeHistoryItem item) {

        if (item.getId() == null) {
            return false;
        }

        EdsDepartmentGoalEmployeeMetricHistory history =
                employeeMetricHistoryManager.get(item.getId());

        if (history == null) {
            return false;
        }

        // Entry date must fall within the goal's period (fromDate..toDate), both inclusive (date-only).
        EdsGoal goal = history.getGoalAssignees() != null ? history.getGoalAssignees().getGoal() : null;
        if (goal != null && goal.getFromDate() != null && goal.getToDate() != null && item.getDate() != null) {
            Date entry = item.getDate();
            Date from = goal.getFromDate();
            Date to = goal.getToDate();
            if (entry.before(from) || entry.after(to)) {
                return false;
            }
        }

        history.setDate(item.getDate());
        history.setActual(item.getActual());
        history.setComment(item.getComment());

        employeeMetricHistoryManager.update(history);

        updateAssigneeTotal(history.getGoalAssignees(), history.getAssignee().getObjectID());

        return true;
    }



    private void updateAssigneeTotal(EdsGoalAssignees goalAssignee, Integer employeeId) {

        Double total = employeeMetricHistoryManager
                .getActualTotalByGoalAssigneeIdAndEmployeeId(
                        goalAssignee.getObjectID(),
                        employeeId
                );

        goalAssignee.setActual(total);
        goalAssigneesManager.update(goalAssignee);
    }



    @Override
    public void deleteDepartmentGoalLogData(Integer id) {
        if (id == null) {
            return;
        }
        // Load the entry first (so we know which assignee it belongs to), soft-delete it,
        // then recompute that assignee's stored actual — otherwise the employee's actual
        // and the department total (sum of assignee actuals) stay stale after a delete.
        EdsDepartmentGoalEmployeeMetricHistory entry = employeeMetricHistoryManager.getEmployeeMetricHistoryById(id);
        employeeMetricHistoryManager.deleteEmployeeMetricHistoryById(id);
        if (entry != null && entry.getGoalAssignees() != null && entry.getAssignee() != null) {
            updateAssigneeTotal(entry.getGoalAssignees(), entry.getAssignee().getObjectID());
        }
    }


    @Override
    public DepartmentGoalChartSettingsItem getDepartmentGoalChartSettings(Integer goalId) {
        if (goalId == null) {
            return null;
        }
        EdsDepartmentGoalChartSettings settings = departmentGoalChartSettingsManager.getByGoalId(goalId);
        if (settings == null) {
            return null;
        }

        return settings.toDto();
    }

    @Override
    public Boolean saveDepartmentGoalChartSettings(Integer goalId, DepartmentGoalChartSettingsItem settingsItem) {
        if (goalId == null) {
            return false;
        }
        EdsDepartmentGoalChartSettings settings = departmentGoalChartSettingsManager.getByGoalId(goalId);
        boolean isNew = settings == null;
        if (isNew) {
            settings = new EdsDepartmentGoalChartSettings();
            settings.setGoalId(goalId);
        }
        settings.applyDto(settingsItem);
        if (isNew) {
            departmentGoalChartSettingsManager.create(settings);
        } else {
            departmentGoalChartSettingsManager.update(settings);
        }
        return true;
    }


    @Override
    public ListResult<DepartmentGoalEmployeeHistoryItem> getDepartmentGoalEmployeeMetricHistory(ListingFilterParameter filterParameter) {
        ListingFilterParameter filter = (filterParameter != null) ? filterParameter : new ListingFilterParameter();

        List<EdsDepartmentGoalEmployeeMetricHistory> entities = employeeMetricHistoryManager.getList(filter);
        Integer total = employeeMetricHistoryManager.getTotalCount(filter);

        ArrayList<DepartmentGoalEmployeeHistoryItem> resultList;
        if (entities != null && !entities.isEmpty()) {
            resultList = entities.stream()
                    .map(EdsDepartmentGoalEmployeeMetricHistory::toRpc)
                    .collect(Collectors.toCollection(ArrayList::new));
        } else {
            resultList = new ArrayList<>();
        }

        return new ListResult<>(resultList, total);
    }

    @Override
    public List<DepartmentGoalEmployeeHistoryItem> getDepartmentGoalChartData(Integer goalId) {
        // Projection (date, actual) only — no entity hydration, no assignee lazy-load (N+1),
        // no count query. The chart consumes just these two fields.
        List<Object[]> rows = employeeMetricHistoryManager.getChartDataForGoal(goalId);
        List<DepartmentGoalEmployeeHistoryItem> result = new ArrayList<>();
        if (rows != null) {
            for (Object[] row : rows) {
                DepartmentGoalEmployeeHistoryItem item = new DepartmentGoalEmployeeHistoryItem();
                item.setDate((Date) row[0]);
                item.setActual((Double) row[1]);
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public SelectItem[] getDepartmentGoalAssignedEmployees(Integer departmentGoalId) {
        List<EdsEmployee> assignees = goalAssigneesManager.getGoalAssignees(departmentGoalId);

        if (assignees == null) return new SelectItem[0];
        EdsUser user = userManager.getUser();
        Integer userId = user.getObjectID();

        return assignees.stream()
                .map(emp -> {
                    SelectItem item = new SelectItem(emp.getObjectID(), emp.getFullName());
                    item.setSelected(emp.getObjectID().equals(userId));
                    return item;
                })
                .toArray(SelectItem[]::new);

    }

    @Override
    public void updateCandidateStatusOnApproval(Integer candidateId, String status) {
        EdsCrmContact contact = crmContactManager.get(candidateId);
        if (status.equals(PLACEMENT_STATUS_APPROVED)) {
            contact.setCandidateStatus(referenceManager.getByCode("CANDIDATE_STATUS_HIRED"));
        } else if (status.equals(PLACEMENT_STATUS_REJECTED)) {
            contact.setCandidateStatus(referenceManager.getByCode("CANDIDATE_STATUS_LOST"));
        }
        crmContactManager.createOrUpdate(contact);
        try {
            contactSolrComponent.index(contact);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String hireDateValidation(RotationItem rotationItem) {
        DateNonConvertable rotationDate = rotationItem.getDate();
        StringBuilder notValidEmployees = new StringBuilder();

        for (RotationTableItem item : rotationItem.getRotationTableItems()) {
            EdsEmployee employee = employeeManager.get(item.getEmployee().getId());
            EdsEmployeeProfile profile = employee.getProfile();

            String fullName = (employee.getFullName() != null) ? employee.getFullName().trim() : "N/A";
            String employeeCode = (profile.getEmployeeCode() != null) ? profile.getEmployeeCode().trim() + " -> " : "";
            String employeeLabel = employeeCode + fullName;

            if (rotationDate.getDate() == null || employee.getStartDate() == null) {
                notValidEmployees.append(employeeLabel).append("</br>");
                continue;
            }

            if (employee.getStartDate().after(rotationDate.getDate())) {
                notValidEmployees.append(employeeLabel).append("</br>");
            }
        }

        return notValidEmployees.toString();
    }

    @Override
    public RotationTableItem getEmployeeDataForRotation(Integer employeeId) {
        EdsEmployee employee = employeeManager.get(employeeId);
        if (employee != null) {
            RotationTableItem item = new RotationTableItem();
            item.setEmployeeId(employeeId);
            item.setEmployee(employee.getAsSelectItem());
            EdsLocation location = employee.getLocation();
            EdsEmployeeDepartment employeeDepartment = employee.getEmployeeDepartment();
            EdsPosition employeePosition = employee.getPosition();
            if (location != null) {
                item.setCurrentLocation(location.getAsSelectItem());
            }
            if (employeeDepartment != null) {
                EdsDepartment team = employeeDepartment.getTeam();
                if (team != null) {
                    item.setCurrentDepartment(employeeDepartment.getTeam().getAsSelectItem());
                }
            }
            if (employeePosition != null) {
                item.setCurrentPosition(employeePosition.getAsSelectItem());
            }
            return item;
        }
        return null;
    }

    private EdsBackupsEmployeeCustomFields createBackupsEmployeCF(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsBackupsEmployeeCustomFields customFields;
            if (customFieldItems.get(0).getObjectId() != null) {

                customFields = backupsEmployeeCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null
                            || fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                customFields = new EdsBackupsEmployeeCustomFields();
                backupsEmployeeCFManager.create(customFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customFields, customFieldItems);
            return customFields;
        }
        return null;
    }

    private boolean validateBackupEmployee(Integer objectID, Date changedDueDate, boolean forDelete) {
        Integer year = null;
        Integer monthId = null;
        if (!forDelete) {
            if (changedDueDate != null) {
                Calendar period = Calendar.getInstance();
                period.setTime(changedDueDate);
                monthId = period.get(Calendar.MONTH);
                year = period.get(Calendar.YEAR);
            }
        }

        List<EdsPaymentDeduction> payslipItems = paymentDeductionManager.getBackupEmployeeAdditionalPaymentsUsedInPayslips(objectID, monthId, year);
        if (payslipItems != null && !payslipItems.isEmpty()) {
            return false;
        }
        payslipItems = additionalPaymentManager.getAdditionalPaymentByBackupsEmployeeId(objectID, monthId, year);
        return payslipItems == null || payslipItems.isEmpty();
    }

    @Override
    public PositionAiResponse fetchPositionAiData(PositionAiRequest positionAiRequest) {

        EdsReference position = referenceManager.getReference(positionAiRequest.getPositionReferenceId());
        EdsDepartment department = departmentManager.get(positionAiRequest.getDepartmentReferenceId());

        String pName = Optional.ofNullable(position)
                .map(EdsReference::getName)
                .orElse("");

        String pDescription = Optional.ofNullable(position)
                .map(EdsReference::getDescription)
                .orElse("");

        String dName = Optional.ofNullable(department)
                .map(EdsDepartment::getName)
                .orElse("");

        String dDescription =
                Optional.ofNullable(department)
                        .map(EdsDepartment::getDescription)
                        .orElse("")
                        + Optional.ofNullable(department)
                        .map(EdsDepartment::getShortDescription)
                        .orElse("");


        String token = EdsContextParams.getOpenAiToken();
        String realToken="";

        if (token == null || token.isBlank()) {
            realToken = SpringPropertiesUtil.getProperty("openai.api.key");
        } else {
            realToken = EncryptionUtils.decrypt(token);
        }

        PositionAiService aiService = new PositionAiService(realToken);

        PositionAiResponse aiResponse = aiService.generatePositionData(pName, pDescription, dName, dDescription);


        return aiResponse;
    }
}
