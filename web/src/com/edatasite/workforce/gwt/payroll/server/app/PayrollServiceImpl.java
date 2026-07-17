package com.edatasite.workforce.gwt.payroll.server.app;

import com.edatasite.shared.components.PasswordGenerator;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.shared.utils.EdsCalendarUtils;
import com.edatasite.workforce.core.domain.EdsAnnualLeaveAllowance;
import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsBackupsEmployee;
import com.edatasite.workforce.core.domain.EdsBenefitRequest;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCountryZone;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsItemCustomFields;
import com.edatasite.workforce.core.domain.EdsLeaveReason;
import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.core.domain.EdsPerformanceNote;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsExpensePaymentTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsTransactionItem;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsAdditionalPaymentCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCashAdvanceCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsOvertimeCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsPayrollCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.payrolluk.EdsATSJobSalary;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPaymentNote;
import com.edatasite.workforce.core.domain.payrolluk.EdsAdditionalPaymentTransaction;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvancePayTransaction;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvanceTransaction;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsDailyRateSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsEmployeePayrollSettingsTemplate;
import com.edatasite.workforce.core.domain.payrolluk.EdsEosCalculation;
import com.edatasite.workforce.core.domain.payrolluk.EdsFormula;
import com.edatasite.workforce.core.domain.payrolluk.EdsMultiCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsMultiRangeRate;
import com.edatasite.workforce.core.domain.payrolluk.EdsOvertimeObject;
import com.edatasite.workforce.core.domain.payrolluk.EdsOvertimeObjectData;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollBatch;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollGlobalSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollGlobalSettingsItem;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollPaymentItem;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollPaymentTransaction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrunPayment;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrunPaymentItem;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrunPaymentTransaction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipEmployeeBonus;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipPayments;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTable;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.payrolluk.EdsPensionProvider;
import com.edatasite.workforce.core.domain.payrolluk.EdsPensionScheme;
import com.edatasite.workforce.core.domain.payrolluk.EdsRecurringPayDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsRuleEosSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsSalaryHistory;
import com.edatasite.workforce.core.domain.payrolluk.EdsSickLeaveSettings;
import com.edatasite.workforce.core.domain.payrolluk.EdsSimpleRate;
import com.edatasite.workforce.core.domain.payrolluk.EdsSinglePayrunTransaction;
import com.edatasite.workforce.core.domain.payrolluk.EmployeePayrollSettingsHistory;
import com.edatasite.workforce.core.domain.payrolluk.EndOfServiceSettings;
import com.edatasite.workforce.core.domain.payrolluk.P11;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.settings.EdsPayrollZone;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowRule;
import com.edatasite.workforce.core.service.SessionContextService;
import com.edatasite.workforce.core.solr.component.AdditionalPaymentSolrComponent;
import com.edatasite.workforce.core.solr.component.CashAdvanceSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeSolrComponent;
import com.edatasite.workforce.core.solr.component.ExpenseReportClaimsSolrComponent;
import com.edatasite.workforce.core.solr.component.GroupPayrunSolrComponent;
import com.edatasite.workforce.core.solr.component.SinglePayrunSolrComponent;
import com.edatasite.workforce.core.solr.document.AdditionalPaymentSolrDoc;
import com.edatasite.workforce.core.solr.document.CashAdvanceSolrDoc;
import com.edatasite.workforce.core.solr.document.GroupPayrunSolrDoc;
import com.edatasite.workforce.core.solr.document.SinglePayrunSolrDoc;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.availability.client.rpc.StatisticsLeaveRequest;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.EmailHostException;
import com.edatasite.workforce.gwt.core.client.Exceptions.EmployeeCodeExistsException;
import com.edatasite.workforce.gwt.core.client.Exceptions.NoAccessUserLimitException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UsernameAlreadyExistsException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UsersLimitExceededException;
import com.edatasite.workforce.gwt.core.client.PayslipItemFilter;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.LRSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PayslipTableRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.UserBankAccountData;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.BackupEmployeeItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableSettingService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.CategoryRate;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.DailyOvertimeData;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployeeSalary;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettings;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettingsObject;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EndOfServiceRules;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.MonthlyOvertimeData;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.MonthlyOvertimeDataWithRates;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCashAdvanceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSinglePayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.calendardatepicker.CalendarUtil;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.view.AdditionalPaymentItemCategory;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvancePayment;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.client.ui.view.EmployeeDataDetail;
import com.edatasite.workforce.gwt.core.client.ui.view.LeavePaymentItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentCalculationDetail;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollPdfTemplateList;
import com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance.MultiCashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.recurring.PayType;
import com.edatasite.workforce.gwt.core.client.ui.view.recurring.RecurringPayDeductItem;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.RejectedImportRecord;
import com.edatasite.workforce.gwt.core.server.app.SalaryHistoryLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.controllers.EmailAddressValidator;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.AnnualLeaveAllowanceManager;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.AttendanceHoursManager;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.BackupsEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.BankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.BenefitRequestManager;
import com.edatasite.workforce.gwt.core.server.db.ClientManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentTreeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeBenefitAllowanceManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ExpensePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.LabourPeriodManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.MonthlyTimesheetManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.P11Manager;
import com.edatasite.workforce.gwt.core.server.db.PaymentMethodManager;
import com.edatasite.workforce.gwt.core.server.db.PerformanceNoteManager;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestDurationManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.UserBankAccountManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.WorkflowAlertManager;
import com.edatasite.workforce.gwt.core.server.db.WorkflowRuleManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.AdditionalPaymentCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.CashAdvanceCFManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.ItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.AttendanceRawDataManagerImpl;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.AdditionalPaymentNoteManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CashAdvanceManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.DailyRateSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EmployeePayrollSettingsTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EndOfServiceGratuityManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EndOfServiceManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.EndOfServiceRuleManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.FormulaManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.MultiCashAdvanceManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.OvertimeCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.OvertimeManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.OvertimeObjectDataManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PaymentDeductionManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollBatchManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollGlobalSettingsItemManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollGlobalSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollPaymentItemManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrunPaymentItemManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrunPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipPaymentsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PensionProviderManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PensionSchemeManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.RecurringPayDeductionManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.SalaryHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.SickLeaveSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.SimpleRateManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.PayrollZoneManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EmployeeEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll.AdditionalPaymentEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll.CashAdvanceEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll.EndOfServiceSettingsEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll.GroupPayrunEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll.OvertimeEventListinerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll.PensionSchemeEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.payroll.SinglePayrunEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.rpc.GroupPayrunItems;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeePayrollSettingsObject;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.server.app.ImportFileServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.CashAdvanceReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.CashAdvanceReportItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.CategoryObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.EndOfServiceData;
import com.edatasite.workforce.gwt.payroll.client.rpc.EoSCalculationData;
import com.edatasite.workforce.gwt.payroll.client.rpc.EosReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.GroupPayrunData;
import com.edatasite.workforce.gwt.payroll.client.rpc.NiTaxChangesListItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.OvertimeObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.OvertimeObjectData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrolTableItemListResult;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollAmountsTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollBatchData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollGlobalSettingsData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollPaymentItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollSettings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollTotalTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrunPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrunPaymentItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipFilter;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionContributionData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionProviderData;
import com.edatasite.workforce.gwt.payroll.client.rpc.PensionSchemeData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryDetailedReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.SalaryReportItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.SaveResultTO;
import com.edatasite.workforce.gwt.payroll.client.rpc.SickLeaveSettings;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.WpsReportData;
import com.edatasite.workforce.gwt.payroll.client.rpc.WpsReportItem;
import com.edatasite.workforce.gwt.payroll.client.ui.view.dailyRateCalculation.DailyRateSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.ui.PayrollConstants;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.hibernate.Session;
import org.jaxen.util.SingletonList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.ZERO;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.EMPLOYER_SETTINGS.DEFAULT_ADDITIONAL_TYPE_SETTINGS;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.LR_TYPE_SICK_LEAVE;
import static com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants.WPS_NUMBER;
import static com.edatasite.workforce.gwt.core.client.ui.Frequency.MONTHLY;
import static com.edatasite.workforce.gwt.core.client.ui.Frequency.WEEKLY;
import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;
import static com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ImportCustomEventListenerImpl.EVENT_IMPORT_PAYMENT;

@SuppressWarnings({"ToArrayCallWithZeroLengthArrayArgument"})
@Transactional
@Service("payrollService")
public class PayrollServiceImpl implements PayrollService, PayrollServiceLocal, Constants {

    private final static Logger log = LoggerFactory.getLogger(PayrollServiceImpl.class);

    private static final BigDecimal MIN_THRESHOLD = new BigDecimal("0.001"); // threshold for workingDays

    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private PaymentMethodManager paymentMethodManager;
    @Autowired
    private P11Manager p11Manager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private PayrollCategoryManager categoryManager;
    @Autowired
    private PaymentDeductionManager paymentDeductionManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmployeePayrollSettingsManager employeePayrollSettingsManager;
    @Autowired
    private EmployeePayrollSettingsTemplateManager employeePayrollSettingsTemplateManager;
    @Autowired
    private EmployeePayrollSettingsHistoryManager employeePayrollSettingsHistoryManager;
    @Autowired
    private CompanyPayrollSettingsManager companyPayrollSettingsManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private PensionSchemeManager pensionSchemeManager;
    @Autowired
    private PensionProviderManager pensionProviderManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    @Qualifier("availabilityService")
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UserBankAccountManager userBankAccountManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private BaseEventsPostProcessor baseEventsPostProcessor;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private PayrollGlobalSettingsManager payrollGlobalSettingsManager;
    @Autowired
    private PayrollBatchManager payrollBatchManager;
    @Autowired
    private PayrollGlobalSettingsItemManager payrollGlobalSettingsItemManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private BankAccountManager bankAccountManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    @Qualifier("payslipPDFHandler")
    private IPostPDFHandler payslipPDFHandler;
    @Autowired
    private PayslipTableManager payslipTableManager;
    @Autowired
    private PayslipTableItemManager payslipTableItemManager;
    @Autowired
    private PayslipPaymentsManager payslipPaymentsManager;
    @Autowired
    private FormulaManager formulaManager;
    @Autowired
    private SimpleRateManager simpleRateManager;
    @Autowired
    private TimeSheetManager timesheetManager;
    @Autowired
    private ExpensePaymentManager expensePaymentManager;
    @Autowired
    private ExpenseServiceLocal expenseServiceLocal;
    @Autowired
    private CommonServiceLocal commonService;
    @Autowired
    private ItemTableSettingService itemTableSettingService;
    @Autowired
    private EndOfServiceManager endOfServiceManager;
    @Autowired
    private EndOfServiceRuleManager endOfServiceRuleManager;
    @Autowired
    private EndOfServiceGratuityManager endOfServiceGratuityManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private CashAdvanceManager cashAdvanceManager;
    @Autowired
    private MultiCashAdvanceManager multiCashAdvanceManager;
    @Autowired
    private BenefitRequestManager benefitRequestManager;
    @Autowired
    private MonthlyTimesheetManager monthlyTimesheetManager;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    private EmployeeBenefitAllowanceManager employeeBenefitAllowanceManager;
    @Autowired
    private AnnualLeaveAllowanceManager annualLeaveAllowanceManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private WorkflowRuleManager workflowRuleManager;
    @Autowired
    private WorkflowAlertManager workflowAlertManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private AdditionalPaymentManager additionalPaymentManager;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private ClientManager clientManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private ReportService reportService;
    @Autowired
    private SickLeaveSettingsManager sickLeaveSettingsManager;
    @Autowired
    private DailyRateSettingsManager dailyRateSettingsManager;
    @Autowired
    private SickRequestDurationManager sickRequestDurationManager;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private PayrollCustomFieldsManager payrollCFManager;
    @Autowired
    private ImportFileServiceLocal importFileServiceLocal;
    @Autowired
    private PayrollAsyncService payrollAsyncService;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private ItemCFManager itemCFManager;
    @Autowired
    private PayrunPaymentManager payrunPaymentManager;
    @Autowired
    private PayrunPaymentItemManager payrunPaymentItemManager;
    @Autowired
    private PayrollPaymentManager payrollPaymentManager;
    @Autowired
    private PayrollPaymentItemManager payrollPaymentItemManager;
    @Autowired
    private MyUpdateTypeManager myUpdateTypeManager;
    @Autowired
    private AdditionalPaymentNoteManager noteManager;
    @Autowired
    private CashAdvanceCFManager cashAdvanceCFManager;
    @Autowired
    private PerformanceNoteManager performanceNoteManager;
    @Autowired
    private SessionContextService sessionContextService;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private AdditionalPaymentCFManager additionalPaymentCFManager;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;
    @Autowired
    private SinglePayrunSolrComponent singlePayrunSolrComponent;
    @Autowired
    private CashAdvanceSolrComponent cashAdvanceSolrComponent;
    @Autowired
    private GroupPayrunSolrComponent groupPayrunSolrComponent;
    @Autowired
    private AdditionalPaymentSolrComponent additionalPaymentSolrComponent;
    @Autowired
    private ExpenseReportClaimsSolrComponent expenseReportClaimsSolrComponent;
    @Autowired
    private RecurringPayDeductionManager recurringPayDeductionManager;
    @Autowired
    private OvertimeManager overtimeManager;
    @Autowired
    private DepartmentTreeManager departmentTreeManager;
    @Autowired
    private OvertimeObjectDataManager objectDataManager;
    @Autowired
    private OvertimeCustomFieldsManager overtimeCFManager;
    @Autowired
    private LabourPeriodManager labourPeriodManager;
    @Autowired
    private SalaryHistoryManager salaryHistoryManager;
    @Autowired
    private AttendanceHoursManager attendanceHoursManager;
    @Autowired
    private ShiftManager shiftManager;
    @Autowired
    private CustomFormItemManager customFormItemManager;
    @Autowired
    private BackupsEmployeeManager backupsEmployeeManager;
    @Autowired
    private PayrollZoneManager payrollZoneManager;
    @Autowired
    private SalaryHistoryLocal salaryHistoryLocal;
    @Autowired
    private ExecutorService executor;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PayrollSettings getEmployeeDetailsAndPayrollSettings(Integer employeeId, Date date) {
        PayrollSettings payrollSettings = employeeServiceLocal.getEmployeeDetailsAndPayrollSettings(employeeId, date);
        if (payrollSettings.getStatusId() == null) {
            payrollSettings.setStatusId(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_PENDING).getObjectID());
        }

        String enabledMultiCurrency = getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL);
        payrollSettings.setEnabledMultiCurrency("true".equals(enabledMultiCurrency));
        payrollSettings.setPayMethods(allInOneService.getPaymentMethodList());
        payrollSettings.setCountries(commonService.getCountries());
        return payrollSettings;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRoles() {
        List<EdsRole> roles = getUserRolesByPattern(roleManager.list());
        SelectItem[] r = new SelectItem[roles.size()];
        int i = 0;
        for (EdsRole rol : roles) {
            if (!rol.getObjectID().equals(EdsRole.CLIENT)) {
                r[i] = new SelectItem();
                r[i].setId(rol.getObjectID());
                r[i].setName(commonLocalizer.localize(rol.getCode(), rol.getName()));
                i++;
            }
        }
        SelectItem[] ss = new SelectItem[i];
        for (int j = 0; j < i; j++) {
            if (r[j] != null) {
                ss[j] = r[j];
            }
        }
        return ss;
    }

    private List<EdsRole> getUserRolesByPattern(List<EdsRole> roles) {
        Integer[] sortRoleByPattern = new Integer[]{EdsRole.ADMIN, EdsRole.DR, EdsRole.HR, EdsRole.ACCOUNTANT, EdsRole.ADMIN_LOCATION,
                EdsRole.SALESMAN, EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE, EdsRole.CUSTOMER_SERVICE_MANAGER, EdsRole.SALESPERSON, EdsRole.TL, EdsRole.PM,
                EdsRole.MEM, EdsRole.CALENDAR_EDITOR, EdsRole.CALENDAR_VIEWER, EdsRole.CLIENT, EdsRole.TIMESHEET_EDITOR, EdsRole.GUEST, EdsRole.INSTRUCTOR};
        List<EdsRole> userRoles = new ArrayList<>();
        if (userManager.getUser() != null && userManager.getUser().getCompany() != null && userManager.getUser().getCompany().getObjectID() != null) {
            //add EXPERT ROLE to COO and ATM
            if (userManager.getUser().getCompany().getObjectID().equals(5377) || userManager.getUser().getCompany().getObjectID().equals(8934)) {
                sortRoleByPattern = new Integer[]{EdsRole.ADMIN, EdsRole.DR, EdsRole.HR, EdsRole.ACCOUNTANT, EdsRole.ADMIN_LOCATION,
                        EdsRole.SALESMAN, EdsRole.CUSTOMER_SERVICE_REPRESENTATIVE, EdsRole.SALESPERSON, EdsRole.TL, EdsRole.PM,
                        EdsRole.MEM, EdsRole.CALENDAR_EDITOR, EdsRole.CALENDAR_VIEWER, EdsRole.CLIENT, EdsRole.CHAT_EXPERT, EdsRole.TIMESHEET_EDITOR, EdsRole.GUEST, EdsRole.INSTRUCTOR};
            }
        }
        EdsModule trainingCenter = moduleManager.getModuleByCode(PermissionConstants.TRAINING_CENTER);
        for (Integer aSortRoleByPattern : sortRoleByPattern) {
            EdsRole rol;

            if (EdsRole.INSTRUCTOR.equals(aSortRoleByPattern)) {
                rol = roleManager.getByCode(INSTRUCTOR_CODE);
            } else {
                rol = roleManager.get(aSortRoleByPattern);
            }

            if (roles.contains(rol)) {
                if (INSTRUCTOR_CODE.equals(rol.getCode())) {
                    if (trainingCenter != null) {
                        userRoles.add(rol);
                    }
                } else {
                    userRoles.add(rol);
                }
            }
        }
        for (EdsRole role : roles) {
            if (!userRoles.contains(role) && (role.getDeleted() == null || !role.getDeleted()) && (role.getSystem() == null || !role.getSystem())) {
                userRoles.add(role);
            }
        }
        return userRoles;
    }

    @Override
    @Transactional
    public Integer createCategory(CategoryObject category) {
        EdsPayrollCategory dbcategory = new EdsPayrollCategory();
        dbcategory.setCode(category.getCode());
        dbcategory.setName(category.getName());
        dbcategory.setNiable(category.getNiable());
        dbcategory.setTaxable(category.getTaxable());
        dbcategory.setType(category.getType());
        dbcategory.setAdvancePayment(category.isAdvancePayment());
        dbcategory.setArabic(category.isArabic());
        dbcategory.setDebitToAccountID(category.getDebitToAccountID());
        dbcategory.setCreditToAccountID(category.getCreditToAccountID());
        categoryManager.create(dbcategory);

        return dbcategory.getObjectID();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getCompanyPayrollSettings(String key) {
        final EdsCompanyPayrollSettings settings = companyPayrollSettingsManager.getCompanySettingValue(key);
        return settings != null ? settings.getValue() : null;
    }

    @Override
    public GroupPayrunData getGroupPayrunSettings() {
        final GroupPayrunData result = new GroupPayrunData();

        result.setEnabledMultiCurrency("true".equals(this.getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL)));
        result.setSendNotification("true".equals(this.getCompanyPayrollSettings(BY_DEFAULT_EMAIL_NOTIFICATION)));
        final EdsCompany company = userManager.getUser().getCompany();
        String code;
        try {
            code = company.getCountryZone().getCountry().getCode();
        } catch (NullPointerException e) {
            code = "";
        }
        EdsPensionScheme pensionScheme = pensionSchemeManager.getPensionSchema(code);

        if (pensionScheme != null) {
            result.setCalculatePension(true);
            result.setPensionType(pensionScheme.getDeductionType());
            result.setCompanyPensionType(pensionScheme.getEmployerDeductionType());
            result.setPensionValue(pensionScheme.getDeductionValue());
            result.setNonLocalPensionValue(pensionScheme.getNonLocalDeductionValue());
            result.setCompanyPensionValue(pensionScheme.getEmployerDeductionValue());
            result.setCompanyNonLocalPensionValue(pensionScheme.getEmployerNonLocalDeductionValue());
            result.setPensionValueType(pensionScheme.getDeductFrom());
            result.setEmpMaxTaxableAmount(pensionScheme.getEmpMaxTaxableAmount());
            result.setCompMaxtaxableAmount(pensionScheme.getCompMaxTaxableAmount());

            for (EdsPayrollCategory category : pensionScheme.getCategories()) {
                result.getPensionAllowances().add(category.createPaymentDeductionSelectItem());
            }
        }
        return result;
    }

    private String getCompanyPayrollSettings(String key, String defaultVal) {
        String val = getCompanyPayrollSettings(key);
        return val != null && !val.isEmpty() ? val : defaultVal;
    }

    private String getEmployeeSettingValue(Integer empId, String key, String... defaultVal) {
        EdsEmployeePayrollSettings settings = employeePayrollSettingsManager.getEmployeeSettingValue(empId, key);
        return settings != null && !settings.getValue().isEmpty() ? settings.getValue() : defaultVal.length > 0 ? defaultVal[0] : null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmployerSettings getCompanyPayrollSettings() {
        EmployerSettings result = new EmployerSettings();
        final List<EdsCompanyPayrollSettings> list = companyPayrollSettingsManager.getCompanySettings(companyManager.getUser().getCompany().getObjectID());
        List<KeyValueStruct> listItems = new ArrayList<>();
        ArrayList<String> keys = new ArrayList<>();
        for (EdsCompanyPayrollSettings cps : list) {
            if (cps.getKey().equals(BANK_ACCOUNT_ID) && cps.getValue() != null && !"".equals(cps.getValue())) {
                listItems.add(new KeyValueStruct(cps.getKey(), cps.getValue()));
                EdsBankAccount bankAccount = bankAccountManager.get(Integer.parseInt(cps.getValue()));
                listItems.add(new KeyValueStruct(BANK_ACCOUNT_NAME, bankAccount.getAccount().getName()));
            } else if (cps.getKey().equals(EXPENSE_PAID_ACCOUNT) && cps.getValue() != null && !"".equals(cps.getValue())) {
                listItems.add(new KeyValueStruct(cps.getKey(), cps.getValue()));
                EdsAccount expenseAccount = accountingManager.get(Integer.parseInt(cps.getValue()));
                listItems.add(new KeyValueStruct(EXPENSE_PAID_ACCOUNT_NAME, expenseAccount.getName()));
            } else if (NUMBER_OF_EMPLOYEE_ID.equals(cps.getKey()) && StringUtils.isNotBlank(cps.getValue())) {
                EdsReference numberOfEmployees = referenceManager.get(Integer.parseInt(cps.getValue()));
                listItems.add(new KeyValueStruct(cps.getKey(), numberOfEmployees.getObjectID(), numberOfEmployees.getName()));
            } else if (INDUSTRY_ID.equals(cps.getKey()) && StringUtils.isNotBlank(cps.getValue())) {
                EdsReference industry = referenceManager.get(Integer.parseInt(cps.getValue()));
                if (industry != null) {
                    listItems.add(new KeyValueStruct(cps.getKey(), industry.getObjectID(), industry.getName()));
                }
            } else if (cps.getKey().equals(COUNTRY_ID) && cps.getValue() != null && !"".equals(cps.getValue())) {
                listItems.add(new KeyValueStruct(cps.getKey(), cps.getValue()));
                EdsCountry country = countryManager.get(Integer.parseInt(cps.getValue()));
                listItems.add(new KeyValueStruct(COUNTRY_NAME, country.getName()));
            } else if (cps.getKey().equals(LEAVE_MONEY_TYPE_CATEGORY) && cps.getValue() != null && !cps.getValue().isEmpty()) {
                EdsPayrollCategory cat = categoryManager.get(Integer.parseInt(cps.getValue()));
                result.setLeaveMoneyTypeCategory(cat.createPaymentDeductionSelectItem());
            } else if (cps.getKey().equals(WEBSITE) && cps.getValue() != null && !cps.getValue().isEmpty()) {
                listItems.add(new KeyValueStruct(WEBSITE, cps.getValue()));
            } else if (!cps.getKey().equals(DEDUCT_ALLOWANCES) && !cps.getKey().equals(TIMESHEET_HOURS_ALLOWANCES) && !cps.getKey().equals(LEAVE_DAILY_ALLOWANCES) && !cps.getKey().equals(LEAVE_MONEY_ALLOWANCES) && !cps.getKey().equals(ADDITIONAL_PAYMENT_ALLOWANCES)) {
                listItems.add(new KeyValueStruct(cps.getKey(), cps.getValue()));
                keys.add(cps.getKey());
            } else if (cps.getKey().equals(DEFAULT_ADDITIONAL_TYPE) && cps.getValue() != null && !cps.getValue().isEmpty()) {
                listItems.add(new KeyValueStruct(cps.getKey(), cps.getValue()));
                keys.add(cps.getKey());
            } else if (cps.getKey().equals(DEFAULT_ADDITIONAL_TYPE_SETTINGS) && cps.getValue() != null && !cps.getValue().isEmpty()) {
                listItems.add(new KeyValueStruct(cps.getKey(), cps.getValue()));
                keys.add(cps.getKey());
            } else {
                if (cps.getValue() != null && !"".equals(cps.getValue())) {
                    EdsPayrollCategory cat;
                    List<PaymentDeductionSelectItem> allowances = result.getAllowances(cps.getKey());
                    String[] categories = cps.getValue().split(";");
                    for (String category : categories) {
                        cat = categoryManager.get(Integer.parseInt(category));
                        if (cat != null) {
                            allowances.add(cat.createPaymentDeductionSelectItem());
                        }
                    }
                }
            }
        }
        if (!keys.contains(ADDRESS1) && !keys.contains(ADDRESS2)) {
            EdsCompany company = companyManager.getUser().getCompany();
            listItems.add(new KeyValueStruct(ADDRESS1, company.getAddress1() != null ? company.getAddress1() : ""));
            listItems.add(new KeyValueStruct(ADDRESS2, company.getAddress2() != null ? company.getAddress2() : ""));
        }
        result.setSettings(listItems.toArray(new KeyValueStruct[]{}));
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmployerSettingsObject getCompanyPayrollSettingsForGroupPayrunPDF() {
        EmployerSettingsObject result = new EmployerSettingsObject();

        String bankAccount = getCompanyPayrollSettings(BANK_ACCOUNT_ID);
        if (bankAccount != null && !"".equals(bankAccount)) {
            EdsBankAccount edsBankAccount = bankAccountManager.get(Integer.parseInt(bankAccount));
            if (edsBankAccount != null) {
                if (edsBankAccount.getAccount() != null) {
                    result.setBankName(edsBankAccount.getAccount().getName());
                }
                result.setBankAddress(edsBankAccount.getBankAddress());
                result.setAccountName(edsBankAccount.getAccauntName());
                result.setAccountNumber(edsBankAccount.getAccountNumber());
                result.setSwiftCode(edsBankAccount.getSwiftCode());
                result.setiBANCode(edsBankAccount.getIbanCode());
            }
        }
        result.setCompanyCode(getCompanyPayrollSettings(COMPANY_CODE));
        result.setReferenceNumber(getCompanyPayrollSettings(PAYE_REF_NUMBER));

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CategoryObject[] getCompanyCategories(boolean isArabic) {
        final List<EdsPayrollCategory> categories = categoryManager.list(isArabic);
        final boolean isUKPayroll = isCountryUK();
        List<CategoryObject> result = new LinkedList<>();
        for (EdsPayrollCategory category : categories) {
            if (!isUKPayroll && (Constants.EMPLOYEE_NI.equals(category.getCode()) || Constants.EMPLOYER_NI.equals(category.getCode()) || Constants.INCOME_TAX.equals(category.getCode()))) {
                continue;
            }
            result.add(categoryForTransfer(category));
        }
        return result.toArray(new CategoryObject[]{});
    }

    public Boolean checkUsingCategory(Integer id) {
        List<EdsPaymentDeduction> list = paymentDeductionManager.getPaymentDeductionByCatogoryID(id);
        return list.size() <= 0;
    }

    public void deleteCategory(Integer id) {
        List<EdsPaymentDeduction> list = paymentDeductionManager.getPaymentDeductionByCatogoryID(id);
        if (list.size() == 0) {
            categoryManager.deleteCategories(id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<CategoryObject> getCompanyCategories(ListingFilterParameter fp) {
        List<EdsPayrollCategory> categories = categoryManager.list(fp);
        int totalCount = categories.size();
        ListLoadConfig config = fp.asConfig();
        if (config != null) {
            categories = ListUtils.getSublist(categories, config.getStart(), config.getLimit());
        }
        ArrayList<CategoryObject> result2 = new ArrayList<>();
        for (EdsPayrollCategory c : categories) {
            result2.add(categoryForTransfer(c));
        }
        return new ListResult<>(result2, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CategoryObject categoryForTransfer(EdsPayrollCategory category) {
        CategoryObject object = new CategoryObject();
        EdsAccount debitAccount;
        EdsAccount creditAccount;
        object.setId(category.getObjectID());
        object.setCode(category.getCode());
        if (STATUTORY_SICK_PAY.equals(category.getCode()) || STATUTORY_MATERNITY_PAY.equals(category.getCode()) || STATUTORY_PATERNITY_PAY.equals(category.getCode()) ||
                STATUTORY_ADOPTION_PAY.equals(category.getCode()) || STATUTORY_PATERNITY_PAY_ADOPT.equals(category.getCode())) {
            object.setName(category.getName() + "(" + category.getCode() + ")");
        } else {
            object.setName(category.getName());
        }
        object.setType(category.getType());
        if (category.getDebitToAccountID() != null) {
            object.setDebitToAccountID(category.getDebitToAccountID());
            debitAccount = accountingManager.get(category.getDebitToAccountID());
            object.setDebitToAccount(debitAccount != null ? debitAccount.createAccountItem() : null);
        }
        if (category.getCreditToAccountID() != null) {
            object.setCreditToAccountID(category.getCreditToAccountID());
            creditAccount = accountingManager.get(category.getCreditToAccountID());
            object.setCreditToAccount(creditAccount != null ? creditAccount.createAccountItem() : null);
        }
        object.setTaxable(category.getTaxable());
        object.setNiable(category.getNiable());
        object.setPensionable(category.getPensionable());
        object.setAdvancePayment(category.isAdvancePayment());
        return object;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<PayrollGlobalSettingsData> getPaymentDections(ListingFilterParameter fp) {
        PayrollGlobalSettingsData item;
        Integer totalCount = payrollGlobalSettingsManager.getSettingsSize();
        List<EdsPayrollGlobalSettings> items = payrollGlobalSettingsManager.getSettingsList(fp);
        ArrayList<PayrollGlobalSettingsData> list = new ArrayList<>();
        for (EdsPayrollGlobalSettings pitem : items) {
            item = new PayrollGlobalSettingsData();
            item.setObjectID(pitem.getObjectID());
            item.setName(pitem.getName());
            item.setSettingsType(pitem.getSettingsType());
            item.setRateType(pitem.getRateType());
            item.setLastUpdate(pitem.getLastUpdate());
            list.add(item);
        }
        return new ListResult<>(list, totalCount);
    }

    public void savePaymentDeductionSettings(PayrollGlobalSettingsData pds) {
        int BATCH_SIZE = 20;
        EdsPayrollGlobalSettings payrollGlobalSettings;
        if (pds.getObjectID() != null) {
            payrollGlobalSettings = payrollGlobalSettingsManager.get(pds.getObjectID());
            if (payrollGlobalSettings != null) {
                payrollGlobalSettingsItemManager.deleteItemsBySettingId(payrollGlobalSettings.getObjectID());
            }
        } else {
            payrollGlobalSettings = new EdsPayrollGlobalSettings();
        }

        payrollGlobalSettings.setName(pds.getName());
        payrollGlobalSettings.setCategoryType(pds.getCategoryType());
        payrollGlobalSettings.setSettingsType(pds.getSettingsType());
        payrollGlobalSettings.setRateType(pds.getRateType());
        payrollGlobalSettings.setPayrollBatch(pds.getBatchItem() != null ? payrollBatchManager.get(pds.getBatchItem().getId()) : null);
        List<EdsEmployee> employees = new ArrayList<>();
        for (Integer empId : pds.getSelectedEmployeeIds()) {
            employees.add(employeeManager.get(empId));
        }
        payrollGlobalSettings.setEmployees(employees);
        if (pds.getSalaryCategory() != null) {
            payrollGlobalSettings.setSalaryCategoryId(pds.getSalaryCategory().getId());
        }
        payrollGlobalSettings.setSalary(pds.getSalary());
        payrollGlobalSettings.setCurrency(pds.getCurrency() != null ? currencyManager.getCurrency(pds.getCurrency().getId()) : null);
        payrollGlobalSettings.setLastUpdate(new Date());
        payrollGlobalSettingsManager.createOrUpdate(payrollGlobalSettings);
        payrollGlobalSettingsManager.flush();

        List<Integer> employeeIds = employees.stream().map(EdsEmployee::getObjectID).collect(Collectors.toList());
        Map<String, PaymentDeductionObject> paymentDeductionObjectMap = paymentDeductionManager.getRecurringPaymentDeductionByCategoryMap(employeeIds);
        String[] settingsKeys = {
                RATE_TYPE,
                SALARY,
                REGULAR_OVERTIME_RATE,
                REGULAR_OVERTIME_RATE_TYPE,
                WEEKEND_OVERTIME_RATE,
                WEEKEND_OVERTIME_RATE_TYPE,
                HOLIDAY_OVERTIME_RATE,
                HOLIDAY_OVERTIME_RATE_TYPE
        };
        Table<Integer, String, EdsEmployeePayrollSettings> employeeSettingsMap = employeePayrollSettingsManager.getEmployeesPayrollSettingsTable(employeeIds, settingsKeys);

        EdsPayrollCategory regularOvertimeCategory = categoryManager.getCategoryByCode(REGULAR_OVERTIME);
        EdsPayrollCategory weekendOvertimeCategory = categoryManager.getCategoryByCode(WEEKEND_OVERTIME);
        EdsPayrollCategory holidayOvertimeCategory = categoryManager.getCategoryByCode(HOLIDAY_OVERTIME);

        Integer payrollGlobalSettingsId = payrollGlobalSettings.getObjectID();
        for (PaymentDeductionObject paymentDeductionObject : pds.getPayments()) {
            Integer settingItemId = savePayrollGlobalSettingsItem(payrollGlobalSettingsId, paymentDeductionObject);
            paymentDeductionObject.setSettingItemId(settingItemId);
        }
        for (PaymentDeductionObject paymentDeductionObject : pds.getDeductions()) {
            Integer settingItemId = savePayrollGlobalSettingsItem(payrollGlobalSettingsId, paymentDeductionObject);
            paymentDeductionObject.setSettingItemId(settingItemId);
        }
        for (PaymentDeductionObject paymentDeductionObject : pds.getEmployerContributions()) {
            Integer settingItemId = savePayrollGlobalSettingsItem(payrollGlobalSettingsId, paymentDeductionObject);
            paymentDeductionObject.setSettingItemId(settingItemId);
        }
        for (PaymentDeductionObject paymentDeductionObject : pds.getTaxes()) {
            Integer settingItemId = savePayrollGlobalSettingsItem(payrollGlobalSettingsId, paymentDeductionObject);
            paymentDeductionObject.setSettingItemId(settingItemId);
        }

        if (pds.getRegularOvertimeRate() != null && pds.getRegularOvertimeRateType() != null) {
            saveOvertimeRateGlobalSettings(payrollGlobalSettingsId,
                    regularOvertimeCategory.getObjectID(),
                    pds.getRegularOvertimeRate(),
                    pds.getRegularOvertimeRateType());
        }
        if (pds.getWeekendOvertimeRate() != null && pds.getWeekendOvertimeRateType() != null) {
            saveOvertimeRateGlobalSettings(payrollGlobalSettingsId,
                    weekendOvertimeCategory.getObjectID(),
                    pds.getWeekendOvertimeRate(),
                    pds.getWeekendOvertimeRateType());
        }
        if (pds.getHolidayOvertimeRate() != null && pds.getHolidayOvertimeRateType() != null) {
            saveOvertimeRateGlobalSettings(payrollGlobalSettingsId,
                    holidayOvertimeCategory.getObjectID(),
                    pds.getHolidayOvertimeRate(),
                    pds.getHolidayOvertimeRateType());
        }

        int i = 0;
        for (EdsEmployee employee : employees) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                employeeManager.flush();
                employeeManager.clear();
            }
            if (payrollGlobalSettings.getCurrency() != null) {
                employee.getPayrollBatches().removeIf(batch -> !Objects.equals(batch.getCurrency(), payrollGlobalSettings.getCurrency()));
                employee.setSalaryCurrency(payrollGlobalSettings.getCurrency());
                if (employee.getProfile() != null && payrollGlobalSettings.getSalary() != null) {
                    employee.getProfile().setSalaryAmount(Double.valueOf(payrollGlobalSettings.getSalary().toString()));
                }
            }
            for (PaymentDeductionObject paymentDeductionObject : pds.getPayments()) {
                String key = employee.getObjectID() + "_" + paymentDeductionObject.getCategoryItem().getId();
                savePayrollGlobalSettingsItemCategory(employee.getObjectID(), paymentDeductionObject, paymentDeductionObjectMap.get(key));
            }
            for (PaymentDeductionObject paymentDeductionObject : pds.getDeductions()) {
                String key = employee.getObjectID() + "_" + paymentDeductionObject.getCategoryItem().getId();
                savePayrollGlobalSettingsItemCategory(employee.getObjectID(), paymentDeductionObject, paymentDeductionObjectMap.get(key));
            }
            for (PaymentDeductionObject paymentDeductionObject : pds.getEmployerContributions()) {
                String key = employee.getObjectID() + "_" + paymentDeductionObject.getCategoryItem().getId();
                savePayrollGlobalSettingsItemCategory(employee.getObjectID(), paymentDeductionObject, paymentDeductionObjectMap.get(key));
            }
            for (PaymentDeductionObject paymentDeductionObject : pds.getTaxes()) {
                String key = employee.getObjectID() + "_" + paymentDeductionObject.getCategoryItem().getId();
                savePayrollGlobalSettingsItemCategory(employee.getObjectID(), paymentDeductionObject, paymentDeductionObjectMap.get(key));
            }
            if (payrollGlobalSettings.getSalaryCategoryId() != null && payrollGlobalSettings.getSalary() != null) {
                EdsEmployeePayrollSettings salary = employeeSettingsMap.get(employee.getObjectID(), SALARY);
                EdsEmployeePayrollSettings salaryCategory = employeeSettingsMap.get(employee.getObjectID(), SALARY_CATEGORY);
                EdsEmployeePayrollSettings rateType = employeeSettingsMap.get(employee.getObjectID(), RATE_TYPE);

                if (salary != null) {
                    if (payrollGlobalSettings.getSalary() != null && !payrollGlobalSettings.getSalary().toString().equals(salary)) {
                        salary.setValue(payrollGlobalSettings.getSalary().toString());
                        employeePayrollSettingsManager.update(salary);
                    }
                } else if (payrollGlobalSettings.getSalary() != null) {
                    salary = new EdsEmployeePayrollSettings();
                    salary.setEmployeeId(employee.getObjectID());
                    salary.setKey(SALARY);
                    salary.setValue(payrollGlobalSettings.getSalary().toString());
                    employeePayrollSettingsManager.create(salary);
                }
                if (salaryCategory != null) {
                    if (payrollGlobalSettings.getSalaryCategoryId() != null && !payrollGlobalSettings.getSalaryCategoryId().equals(Integer.valueOf(salaryCategory.getValue()))) {
                        salaryCategory.setValue(payrollGlobalSettings.getSalaryCategoryId().toString());
                        employeePayrollSettingsManager.update(salaryCategory);
                    }
                } else if (payrollGlobalSettings.getSalaryCategoryId() != null) {
                    salaryCategory = new EdsEmployeePayrollSettings();
                    salaryCategory.setEmployeeId(employee.getObjectID());
                    salaryCategory.setKey(SALARY_CATEGORY);
                    salaryCategory.setValue(payrollGlobalSettings.getSalaryCategoryId().toString());
                    employeePayrollSettingsManager.create(salaryCategory);
                }
                if (rateType != null) {
                    if (payrollGlobalSettings.getRateType() != null && !payrollGlobalSettings.getRateType().equals(rateType.getValue())) {
                        rateType.setValue(payrollGlobalSettings.getRateType());
                        employeePayrollSettingsManager.update(rateType);
                    }
                } else {
                    rateType = new EdsEmployeePayrollSettings();
                    rateType.setEmployeeId(employee.getObjectID());
                    rateType.setKey(RATE_TYPE);
                    rateType.setValue(payrollGlobalSettings.getRateType());
                    employeePayrollSettingsManager.create(rateType);
                }
            }

            if (pds.getRegularOvertimeRate() != null && pds.getRegularOvertimeRateType() != null) {
                saveOvertimeRateEmployeeSettings(payrollGlobalSettings.getObjectID(),
                        employee.getObjectID(),
                        regularOvertimeCategory.getObjectID(),
                        employeeSettingsMap,
                        pds.getRegularOvertimeRate(),
                        pds.getRegularOvertimeRateType(),
                        REGULAR_OVERTIME_RATE,
                        REGULAR_OVERTIME_RATE_TYPE);
            }
            if (pds.getWeekendOvertimeRate() != null && pds.getWeekendOvertimeRateType() != null) {
                saveOvertimeRateEmployeeSettings(payrollGlobalSettings.getObjectID(),
                        employee.getObjectID(),
                        weekendOvertimeCategory.getObjectID(),
                        employeeSettingsMap,
                        pds.getWeekendOvertimeRate(),
                        pds.getWeekendOvertimeRateType(),
                        WEEKEND_OVERTIME_RATE,
                        WEEKEND_OVERTIME_RATE_TYPE);
            }
            if (pds.getHolidayOvertimeRate() != null && pds.getHolidayOvertimeRateType() != null) {
                saveOvertimeRateEmployeeSettings(payrollGlobalSettings.getObjectID(),
                        employee.getObjectID(),
                        holidayOvertimeCategory.getObjectID(),
                        employeeSettingsMap,
                        pds.getHolidayOvertimeRate(),
                        pds.getHolidayOvertimeRateType(),
                        HOLIDAY_OVERTIME_RATE,
                        HOLIDAY_OVERTIME_RATE_TYPE);
            }
        }

        if (payrollGlobalSettings.getCurrency() != null) {
            try {
                employeeSolrComponent.indexes(employees);
            } catch (SolrServerException | IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Integer savePayrollGlobalSettingsItem(Integer payrollGlobalSettingsId, PaymentDeductionObject paymentDeductionObject) {
        EdsPayrollGlobalSettingsItem settingsItem = new EdsPayrollGlobalSettingsItem();
        settingsItem.setPayrollGlobalSettingsId(payrollGlobalSettingsId);
        settingsItem.setCategoryId(paymentDeductionObject.getCategoryItem().getId());
        settingsItem.setPayType(paymentDeductionObject.getType());
        settingsItem.setAmount(paymentDeductionObject.getType() == 0 ? paymentDeductionObject.getPaymentAmount() : paymentDeductionObject.getPercentage());
        settingsItem.setPaymentType(paymentDeductionObject.getPaymentType() != null ? paymentDeductionObject.getPaymentType() : EPPaymentType.RECURRING);
        settingsItem.setFromAllAllowances(paymentDeductionObject.isFromAllAllowances());
        if (paymentDeductionObject.getLinkedCategories() != null && paymentDeductionObject.getLinkedCategories().size() > 0) {
            EdsPayrollCategory category;
            for (final PaymentDeductionObject linkedCategory : paymentDeductionObject.getLinkedCategories()) {
                category = this.categoryManager.get(linkedCategory.getCategoryItem().getId());
                if (category != null) {
                    settingsItem.getLinkedCategories().add(category);
                }
            }
        }
        payrollGlobalSettingsItemManager.createOrUpdate(settingsItem);
        return settingsItem.getObjectID();
    }

    private void savePayrollGlobalSettingsItemCategory(Integer employeeId, PaymentDeductionObject paymentDeductionObject, PaymentDeductionObject existingPaymentDeductionObject) {
        EdsPaymentDeduction paymentDeduction;
        if (existingPaymentDeductionObject != null) {
            paymentDeduction = paymentDeductionManager.get(existingPaymentDeductionObject.getId());
        } else {
            paymentDeduction = new EdsPaymentDeduction();
            paymentDeduction.setEmployeeId(employeeId);
            paymentDeduction.setCategoryId(paymentDeductionObject.getCategoryItem().getId());
        }
        paymentDeduction.setPayrollGlobalSettingsItemId(paymentDeductionObject.getSettingItemId());
        paymentDeduction.setPaymentAmount(paymentDeductionObject.getPaymentAmount());
        paymentDeduction.setPayType(paymentDeductionObject.getType());
        paymentDeduction.setPercentage(paymentDeductionObject.getPercentage());
        paymentDeduction.setPaymentType(paymentDeductionObject.getPaymentType() != null ? paymentDeductionObject.getPaymentType() : EPPaymentType.RECURRING);
        paymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentDeductionObject.getPaymentType()));
        paymentDeduction.setFromAllAllowances(paymentDeductionObject.isFromAllAllowances());
        paymentDeduction.getLinkedCategories().clear();
        if (paymentDeductionObject.getLinkedCategories() != null && paymentDeductionObject.getLinkedCategories().size() > 0) {
            EdsPayrollCategory category;
            for (final PaymentDeductionObject linkedCategory : paymentDeductionObject.getLinkedCategories()) {
                category = this.categoryManager.get(linkedCategory.getCategoryItem().getId());
                if (category != null) {
                    paymentDeduction.getLinkedCategories().add(category);
                }
            }
        }
        paymentDeductionManager.createOrUpdate(paymentDeduction);
    }

    private void saveOvertimeRateGlobalSettings(Integer payrollGlobalSettingsId,
                                                Integer categoryId,
                                                BigDecimal pdRate,
                                                String pdRateType) {
        EdsPayrollGlobalSettingsItem settingsItem = new EdsPayrollGlobalSettingsItem();
        settingsItem.setPayrollGlobalSettingsId(payrollGlobalSettingsId);
        settingsItem.setCategoryId(categoryId);
        settingsItem.setPayType(FIXED.equals(pdRateType) ? 0 : 1);//fixed=0; percentage=1;
        settingsItem.setAmount(pdRate);
        payrollGlobalSettingsItemManager.createOrUpdate(settingsItem);
    }

    private void saveOvertimeRateEmployeeSettings(Integer payrollGlobalSettingsId, Integer employeeId, Integer categoryId,
                                                  Table<Integer, String, EdsEmployeePayrollSettings> employeeSettingsMap,
                                                  BigDecimal pdRate, String pdRateType,
                                                  String rate, String rateType) {
        EdsEmployeePayrollSettings regularOvertimeRate = employeeSettingsMap.get(employeeId, rate);
        EdsEmployeePayrollSettings regularOvertimeRateType = employeeSettingsMap.get(employeeId, rateType);
        if (regularOvertimeRate != null) {
            regularOvertimeRate.setValue(pdRate.toString());
            employeePayrollSettingsManager.update(regularOvertimeRate);
        } else {
            regularOvertimeRate = new EdsEmployeePayrollSettings();
            regularOvertimeRate.setEmployeeId(employeeId);
            regularOvertimeRate.setKey(rate);
            regularOvertimeRate.setValue(pdRate.toString());
            employeePayrollSettingsManager.create(regularOvertimeRate);
        }
        if (regularOvertimeRateType != null) {
            regularOvertimeRateType.setValue(pdRateType);
            employeePayrollSettingsManager.update(regularOvertimeRateType);
        } else {
            regularOvertimeRateType = new EdsEmployeePayrollSettings();
            regularOvertimeRateType.setEmployeeId(employeeId);
            regularOvertimeRateType.setKey(rateType);
            regularOvertimeRateType.setValue(pdRateType);
            employeePayrollSettingsManager.create(regularOvertimeRateType);
        }
    }

    /////////////////////PayslipView "Preview" logic related////////////////////////////////////////

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmployeePayrollSettingsObject getEmployeePayrollSettings(Integer employeeID, String key) {
        final EdsEmployeePayrollSettings setting = employeePayrollSettingsManager.getEmployeeSettingValue(employeeID, key);
        final EmployeePayrollSettingsObject settingObject = new EmployeePayrollSettingsObject();
        settingObject.setObjectID(setting.getObjectID());
        settingObject.setEmployeeID(setting.getEmployee().getObjectID());
        settingObject.setKey(setting.getKey());
        settingObject.setValue(setting.getValue());
        return settingObject;
    }

    /**
     * Creates, updates employee settings
     *
     * @param employee        employee whois setting is to be updated
     * @param payrollSettings settings list to be updated for an employee
     */
    public Integer addNewStarter(NewEmployee employee, HashMap<String, String> payrollSettings) throws UsernameAlreadyExistsException, EmailHostException, UsersLimitExceededException, NoAccessUserLimitException {
        Integer employeeID;
        boolean isNewEmployee = false;
        if (employee.getObjectID() == null) {
            isNewEmployee = true;
            employeeID = employeeService.createEmployee(employee, false);
            if (employeeID == -1) {
                throw new UsernameAlreadyExistsException();
            }
            if (employeeID == -3) {
                throw new EmailHostException();
            }
            if (employeeID == -11) {
                throw new UsersLimitExceededException();
            }
            if (employeeID == -12) {
                throw new NoAccessUserLimitException();
            }
        } else {
            employeeID = employee.getObjectID();
        }
        EdsEmployee edsEmployee = employeeManager.get(employeeID);
        Set<EdsRole> roles = new HashSet<>();
        if (employee.getRoleId() != null) {
            for (int i = 0; i < employee.getRoleId().length; i++) {
                roles.add(roleManager.get(employee.getRoleId()[i]));
                edsEmployee.setRoles(roles);
            }
        }
        if (employee.isEssUser()) {
            roles.clear();
            roles.add(roleManager.getByCode(ESS_USER_CODE));
            edsEmployee.setRoles(roles);
        }

        if (employee.hasAccess()) {
            String password = userManager.findActiveAndNonFederateLoginUsers(EdsContextParams.getHostname(), edsEmployee.getUserName());
            if (password == null) {
                edsEmployee.setRandom(ServerUtils.randomstring());
                PasswordGenerator pg = new PasswordGenerator(6);
                password = pg.generateAsString();
                employee.setPassword(password);
                edsEmployee.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_PENDING));
            } else {
                //if isn't sent activation link to employee it must be active
                edsEmployee.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
            }
            edsEmployee.setPassword(password);
        } else {
            edsEmployee.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_NO_ACCCESS));
        }

        if (employee.getBirthDate() != null && edsEmployee.getProfile() != null && edsEmployee.getProfile().getContact() != null) {
            edsEmployee.getProfile().getContact().setDateOfBirth(employee.getBirthDate().getNonConvertedDate());
            crmContactManager.update(edsEmployee.getProfile().getContact(), true);
        }
        if (employee.getStartDate() != null) {
            edsEmployee.setStartDate(employee.getStartDate().getNonConvertedDate());
        }

        if (!isNewEmployee) {
            if (employee.getEmail() != null) {
                edsEmployee.setEmail(employee.getEmail());
            }
            if (employee.getFname() != null) {
                edsEmployee.setFirstName(employee.getFname());
            }
            if (employee.getLname() != null) {
                edsEmployee.setLastName(employee.getLname());
            }
        }

        boolean enabledMultiCurrency = "true".equals(getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL));

        if (employee.getSalaryCurrency() != null) {
            edsEmployee.setSalaryCurrency(currencyManager.get(employee.getSalaryCurrency().getId()));
            edsEmployee.getPayrollBatches().removeIf(batch -> !Objects.equals(batch.getCurrency(), edsEmployee.getSalaryCurrency()));
        } else if (!enabledMultiCurrency) {
            EdsCurrency baseCurrency = financialSettingsManager.getFinancialSettings().getCurrency();
            if (baseCurrency != null) {
                edsEmployee.setSalaryCurrency(baseCurrency);
                edsEmployee.getPayrollBatches().removeIf(batch -> !Objects.equals(batch.getCurrency(), edsEmployee.getSalaryCurrency()));
            }
        }

        if (employee.getCitizenship() != null) {
            edsEmployee.setCitizenship(countryManager.get(employee.getCitizenship().getId()));
        } else {
            edsEmployee.setCitizenship(null);
        }
        edsEmployee.setEndDate(employee.getResignationDate() != null ? employee.getResignationDate().getNonConvertedDate() : null);
        if (employee.getEmpCode() != null && edsEmployee.getProfile() != null) {
            edsEmployee.getProfile().setEmployeeCode(employee.getEmpCode());
        }
        if (employee.getNumberData() != null && edsEmployee.getProfile() != null) {
            edsEmployee.getProfile().setIntNumber(employee.getNumberData().getIntNumber());
            edsEmployee.getProfile().setSavedNumberFormula(employee.getNumberData().getSavedNumberFormula());
        }
        edsEmployee.setPrevEndDate(employee.getPrevEndDate() != null ? employee.getPrevEndDate().getNonConvertedDate() : null);
        edsEmployee.setStartDateForOnlyPayroll(employee.getStartDateForOnlyPayroll() != null ? employee.getStartDateForOnlyPayroll().getNonConvertedDate() : null);
        if (employee.getPayMethod() != null && employee.getPayMethod().getId() != null) {
            edsEmployee.setPayMethod(paymentMethodManager.get(employee.getPayMethod().getId()));
        }
        edsEmployee.setPaymentMethod(employee.getPaymentMethod());
        edsEmployee.setSalaryMode(employee.getSalaryMode());
        edsEmployee.setFingerprintDeviceUuids(employee.getFingerprintDeviceUuids());
        EdsPaymentDeduction newPaymentDeduction;

        if (!isNewEmployee) {
            if (employee.getPayments() != null && employee.getPayments().size() > 0) {
                for (PaymentDeductionObject paymentOrDeductionItem : employee.getPayments()) {
                    if (paymentOrDeductionItem.getId() != null) {
                        newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                    } else {
                        newPaymentDeduction = new EdsPaymentDeduction();
                    }
                    newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem().getId());
                    newPaymentDeduction.setEmployeeId(edsEmployee.getObjectID());
                    newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                    newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                    newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                    newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                    newPaymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentOrDeductionItem.getPaymentType()));
                    newPaymentDeduction.setPaymentType(paymentOrDeductionItem.getPaymentType() != null ? paymentOrDeductionItem.getPaymentType() : EPPaymentType.RECURRING);
                    paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                }
            }

            if (employee.getDeductions() != null && employee.getDeductions().size() > 0) {
                for (PaymentDeductionObject paymentOrDeductionItem : employee.getDeductions()) {
                    if (paymentOrDeductionItem.getId() != null) {
                        newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                        paymentDeductionManager.get(paymentOrDeductionItem.getId()).getLinkedCategories().clear();
                        paymentDeductionManager.flush();
                    } else {
                        newPaymentDeduction = new EdsPaymentDeduction();
                    }
                    newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem().getId());
                    newPaymentDeduction.setEmployeeId(edsEmployee.getObjectID());
                    newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                    newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                    newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                    newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                    newPaymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentOrDeductionItem.getPaymentType()));
                    newPaymentDeduction.setPaymentType(paymentOrDeductionItem.getPaymentType() != null ? paymentOrDeductionItem.getPaymentType() : EPPaymentType.RECURRING);
                    newPaymentDeduction.setFromAllAllowances(paymentOrDeductionItem.isFromAllAllowances());
                    newPaymentDeduction.getLinkedCategories().clear();
                    paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                    if (paymentOrDeductionItem.getLinkedCategories() != null && paymentOrDeductionItem.getLinkedCategories().size() > 0) {
                        EdsPayrollCategory category;
                        for (PaymentDeductionObject linkedCategory : paymentOrDeductionItem.getLinkedCategories()) {
                            category = categoryManager.get(linkedCategory.getCategoryItem().getId());
                            if (category != null) {
                                category.addPaymentDeduction(newPaymentDeduction);
                            }
                        }
                    }
                }
            }

            if (employee.getTaxes() != null && employee.getTaxes().size() > 0) {
                for (PaymentDeductionObject paymentOrDeductionItem : employee.getTaxes()) {
                    if (paymentOrDeductionItem.getId() != null) {
                        newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                        paymentDeductionManager.get(paymentOrDeductionItem.getId()).getLinkedCategories().clear();
                        paymentDeductionManager.flush();
                    } else {
                        newPaymentDeduction = new EdsPaymentDeduction();
                    }
                    newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem().getId());
                    newPaymentDeduction.setEmployeeId(edsEmployee.getObjectID());
                    newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                    newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                    newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                    newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                    newPaymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentOrDeductionItem.getPaymentType()));
                    newPaymentDeduction.setPaymentType(paymentOrDeductionItem.getPaymentType() != null ? paymentOrDeductionItem.getPaymentType() : EPPaymentType.RECURRING);
                    newPaymentDeduction.setFromAllAllowances(paymentOrDeductionItem.isFromAllAllowances());
                    newPaymentDeduction.getLinkedCategories().clear();
                    paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                    if (paymentOrDeductionItem.getLinkedCategories() != null && paymentOrDeductionItem.getLinkedCategories().size() > 0) {
                        EdsPayrollCategory category;
                        for (PaymentDeductionObject linkedCategory : paymentOrDeductionItem.getLinkedCategories()) {
                            category = categoryManager.get(linkedCategory.getCategoryItem().getId());
                            if (category != null) {
                                category.addPaymentDeduction(newPaymentDeduction);
                            }
                        }
                    }
                }
            }

            if (employee.getLoans() != null && employee.getLoans().size() > 0) {
                for (PaymentDeductionObject paymentOrDeductionItem : employee.getLoans()) {
                    if (paymentOrDeductionItem.getId() != null) {
                        newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                    } else {
                        newPaymentDeduction = new EdsPaymentDeduction();
                    }
                    newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem().getId());
                    newPaymentDeduction.setEmployeeId(edsEmployee.getObjectID());
                    if (paymentOrDeductionItem.getPercentage() != null && paymentOrDeductionItem.getPercentage().compareTo(BigDecimal.ZERO) != 0) {
                        newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                    }
                    newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                    newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                    newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                    newPaymentDeduction.setStartDate(paymentOrDeductionItem.getStarttDate().getNonConvertedDate());
                    newPaymentDeduction.setTotalAmount(paymentOrDeductionItem.getTotalAmount());
                    newPaymentDeduction.setRecurring(true);
                    paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                }
            }

            if (employee.getEmployerContributions() != null && employee.getEmployerContributions().size() > 0) {
                for (PaymentDeductionObject paymentOrDeductionItem : employee.getEmployerContributions()) {
                    if (paymentOrDeductionItem.getId() != null) {
                        newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                    } else {
                        newPaymentDeduction = new EdsPaymentDeduction();
                    }
                    newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem().getId());
                    newPaymentDeduction.setEmployeeId(edsEmployee.getObjectID());
                    newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                    newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                    newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                    newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                    newPaymentDeduction.setRecurring(!EPPaymentType.ADDITIONAL.equals(paymentOrDeductionItem.getPaymentType()));
                    newPaymentDeduction.setPaymentType(paymentOrDeductionItem.getPaymentType() != null ? paymentOrDeductionItem.getPaymentType() : EPPaymentType.RECURRING);
                    newPaymentDeduction.getLinkedCategories().clear();
                    paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                    if (paymentOrDeductionItem.getLinkedCategories() != null && paymentOrDeductionItem.getLinkedCategories().size() > 0) {
                        EdsPayrollCategory category;
                        for (PaymentDeductionObject linkedCategory : paymentOrDeductionItem.getLinkedCategories()) {
                            category = categoryManager.get(linkedCategory.getCategoryItem().getId());
                            if (category != null) {
                                category.addPaymentDeduction(newPaymentDeduction);
                            }
                        }
                    }
                }
            }

        }

        if (employee.getDeletedCategories() != null && employee.getDeletedCategories().size() > 0) {
            for (Integer id : employee.getDeletedCategories()) {
                paymentDeductionManager.deletePaymentOrDeduction(id);
            }
        }

        if (employee.getInactiveCategories() != null && employee.getInactiveCategories().size() > 0) {
            EdsPaymentDeduction paymentDeduction;
            for (Integer id : employee.getInactiveCategories()) {
                paymentDeduction = paymentDeductionManager.get(id);
                if (paymentDeduction != null) {
                    paymentDeduction.setRecurring(false);
                }
            }
        }


        /* UPDATE PAYROLL SETTINGS */
        for (Map.Entry<String, String> payrollSetting : payrollSettings.entrySet()) {
            if (payrollSetting.getValue() == null) {
                continue;
            }

            EdsEmployeePayrollSettings eps = employeePayrollSettingsManager.getEmployeeSettingValue(employeeID, payrollSetting.getKey());

            if (payrollSetting.getValue().equals(PayrollConstants.EMPTY_VALUE)) { //Delete if empty
                if (eps != null) {
                    employeePayrollSettingsManager.delete(eps);
                }
                continue;
            }

            if (eps != null && !payrollSetting.getValue().equals(eps.getValue())) {
                /*if(NI_NUMBER.equals(eps.getKey())){
                    createHistory(EmployeePayrollSettingsHistory.NINO_CHANGED,EmployeePayrollSettingsHistory.USER_MODIFIED,payrollSetting.getValue(),eps);
                }else */
                if (NI_TABLE_LETTER.equals(eps.getKey())) {
                    createHistory(EmployeePayrollSettingsHistory.NICATEGORY_CHANGED, EmployeePayrollSettingsHistory.USER_MODIFIED, payrollSetting.getValue(), eps);
                } else if (TAX_CODE.equals(eps.getKey())) {
                    createHistory(EmployeePayrollSettingsHistory.TAXCODE_CHANGED, EmployeePayrollSettingsHistory.USER_MODIFIED, payrollSetting.getValue(), eps);
                }
            } else if (eps == null) {
                /*if(NI_NUMBER.equals(eps.getKey())){
                    createHistory(EmployeePayrollSettingsHistory.NINO_CHANGED,EmployeePayrollSettingsHistory.SYSTEM_CREATED,payrollSetting.getValue(),eps);
                } else */
                if (NI_TABLE_LETTER.equals(payrollSetting.getKey())) {
                    createHistory(EmployeePayrollSettingsHistory.NICATEGORY_CHANGED, EmployeePayrollSettingsHistory.SYSTEM_CREATED, payrollSetting.getValue(), eps);
                } else if (TAX_CODE.equals(payrollSetting.getKey())) {
                    createHistory(EmployeePayrollSettingsHistory.TAXCODE_CHANGED, EmployeePayrollSettingsHistory.SYSTEM_CREATED, payrollSetting.getValue(), eps);
                }
            }


            if (eps == null) {
                eps = new EdsEmployeePayrollSettings();
            }
            eps.setEmployeeId(edsEmployee.getObjectID());
            eps.setKey(payrollSetting.getKey());
            if (payrollSetting.getKey().equals(SALARY)) {
                eps.setValue(new BigDecimal(payrollSetting.getValue()).setScale(5, RoundingMode.HALF_UP).toString());
            } else {
                eps.setValue(payrollSetting.getValue());
            }
            employeePayrollSettingsManager.createOrUpdate(eps);
        }
        EdsUser user = userManager.getUser();
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, employee.getObjectID() == null ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsEmployee, user);
        workflowEvent.setEntityType(RelationItem.TYPE_EMPLOYEE);
        if (payrollSettings.get(EMPLOYEE_BONUS_TYPE) != null && !"".equals(payrollSettings.get(EMPLOYEE_BONUS_TYPE))) {
            EdsPayslipEmployeeBonus payslipEmployeeBonus = simpleRateManager.getEmployeeBonus(edsEmployee.getObjectID());
            if (payslipEmployeeBonus == null) {
                payslipEmployeeBonus = new EdsPayslipEmployeeBonus();
                payslipEmployeeBonus.setEmployee(edsEmployee);
            }
            BigDecimal bonusAmount = new BigDecimal(payrollSettings.get(EMPLOYEE_BONUS_VALUE));
            if (PERCENTAGE.equals(payrollSettings.get(EMPLOYEE_BONUS_TYPE))) {
                payslipEmployeeBonus.setPercentage(bonusAmount);
            } else {
                payslipEmployeeBonus.setFixedAmount(bonusAmount);
            }
            simpleRateManager.createOrUpdate(payslipEmployeeBonus);
        }
        if (isNewEmployee && payrollSettings.get(SALARY) != null) {
            SalaryHistory salaryHistory = new SalaryHistory();
            salaryHistory.setEmployeeId(edsEmployee.getObjectID());
            salaryHistory.setSalary(new BigDecimal(payrollSettings.get(SALARY)));
            salaryHistory.setEffectiveDate(new DateNonConvertable(employee.getStartDate() != null ? employee.getStartDate().getNonConvertedDate() : new Date()));
            salaryHistory.setRelationId(edsEmployee.getObjectID());
            salaryHistory.setRelationType(EdsSalaryHistory.TYPE_PROFILE);
            salaryHistoryLocal.save(salaryHistory);
        }

        EdsBusinessEvent edsBusinessEvent = baseEventPostProcessor.registerEvent(EmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsEmployee, user);
        edsBusinessEvent.setSolrIndexed(true);
        try {
            employeeSolrComponent.index(edsEmployee);
        } catch (SolrServerException | InterruptedException e) {
            edsBusinessEvent.setSolrIndexed(false);
            log.error("SAVE EMPLOYEE ERROR:" + e.getMessage(), e);
        } catch (IOException e) {
            edsBusinessEvent.setSolrIndexed(false);
            log.error("SAVE EMPLOYEE ERROR2:" + e.getMessage(), e);
        }
        return employeeID;
    }

    private void createHistory(String status, String methodOfChange, String newValue, EdsEmployeePayrollSettings eps) {
        EmployeePayrollSettingsHistory epsh = new EmployeePayrollSettingsHistory();
        if (eps != null) {
            epsh.setEmployeePayrollSettings(eps);
            epsh.setOldValue(eps.getValue());
        }
        epsh.setNewValue(newValue);
        epsh.setDate(new Date());
        epsh.setStatus(referenceManager.findReference(EmployeePayrollSettingsHistory.HISTORY_STATUS, status));
        epsh.setMethodOfChange(referenceManager.findReference(EmployeePayrollSettingsHistory.METHOD_OF_CHANGE, methodOfChange));
        employeePayrollSettingsHistoryManager.create(epsh);
    }

    private void createTransactionForExpencePayment(EdsExpensePayment payment, Integer paymentType) {
        EdsUser user = accountingManager.getUser();
        EdsExpensePaymentTransaction transaction = new EdsExpensePaymentTransaction();//get the transaction for this invoice
        transaction.setTargetTransaction(transactionManager.getTransactionByExpense(payment.getExpenseReport()));
        transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID() + 1);
        transaction.setExpensePayment(payment);
        transaction.setJournalDate(payment.getPaymentDate());
        transaction.setPostedDate(user.getCompany().getCompanyDate());
        transaction.setPostedBy(user);
        transaction.setName("Payment: " + (isOk(payment.getExpenseReport().getCurrentApprover())
                && isOk(payment.getExpenseReport().getCurrentApprover().getExactEmployee())
                && payment.getExpenseReport().getCurrentApprover().getExactEmployee().getName() != null
                ? payment.getExpenseReport().getCurrentApprover().getExactEmployee().getName() : ""));
        transaction.setReference(payment.getReference());


        boolean isEnabledMultiCurrency = "true".equals(getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL));

        BigDecimal paymentExchangeRate = payment.getExchangeRate() != null && payment.getExchangeRate().compareTo(ZERO) != 0 ? payment.getExchangeRate() : BigDecimal.ONE;
        BigDecimal expenseReportExchangeRate = payment.getExpenseReport().getExchangeRate().compareTo(ZERO) != 0 ? payment.getExpenseReport().getExchangeRate() : BigDecimal.ONE;

        BigDecimal paymentAtCurrentRate = payment.getAmount().divide(paymentExchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        BigDecimal paymentAtExpenseReportRate = payment.getExpenseReport().getTotal().subtract(payment.getExpenseReport().getPaidTotal(false)).divide(expenseReportExchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        BigDecimal diff = paymentAtCurrentRate.subtract(paymentAtExpenseReportRate);
        EdsTransactionItem transactionItem;
        if (diff.compareTo(BigDecimal.ZERO) != 0) {
            transactionItem = new EdsTransactionItem();
            transactionItem.setAccount(accountingManager.getAccountByKey(EdsAccount.EXCHANGE_VARIANCE));
            if (diff.compareTo(BigDecimal.ZERO) > 0) {
                transactionItem.setDebit(diff.abs());
            } else {
                transactionItem.setCredit(diff.abs());
            }
            transaction.addTransactionItem(transactionItem);
        }

        transactionItem = new EdsTransactionItem();
        transactionItem.setAccount(accountingManager.getAccountByKey(EdsAccount.UNPAID_EXPENSE_CLAIMS));
        transactionItem.setDebit(paymentAtExpenseReportRate);
        if (isEnabledMultiCurrency) {
            transactionItem.setForeignDebit(payment.getAmount());
        }
        transaction.addTransactionItem(transactionItem);
        transactionItem = new EdsTransactionItem();
        transactionItem.setCredit(paymentAtCurrentRate);
        if (PAYMENT_TYPE.equals(paymentType)) {
            transactionItem.setAccount(payment.getAccount());
        } else {
            transactionItem.setAccount(accountingManager.getAccountByKey(EdsAccount.PREPAYMENT));
        }
        if (isEnabledMultiCurrency) {
            transactionItem.setForeignCredit(payment.getAmount());
        }
        transaction.addTransactionItem(transactionItem);
        transactionManager.create(transaction);
    }

    public Integer saveCompanyPayrollSettings(EmployerSettings payrollSettings) {
        EdsCompanyPayrollSettings cps;
        if (payrollSettings.getSettings() != null) {
            for (KeyValueStruct setting : payrollSettings.getSettings()) {
                if (setting == null || setting.getValue() == null)
                    continue;
                cps = companyPayrollSettingsManager.getCompanySettingValue(setting.getKey());
                if (cps == null) {
                    cps = new EdsCompanyPayrollSettings();
                }
                cps.setKey(setting.getKey());
                cps.setValue(setting.getValue());
                companyPayrollSettingsManager.createOrUpdate(cps);
            }
        }
        if (payrollSettings.getLeaveMoneyTypeCategory() != null && payrollSettings.getLeaveMoneyTypeCategory().getId() != null) {
            cps = companyPayrollSettingsManager.getCompanySettingValue(LEAVE_MONEY_TYPE_CATEGORY);
            if (cps == null) {
                cps = new EdsCompanyPayrollSettings();
            }
            cps.setKey(LEAVE_MONEY_TYPE_CATEGORY);
            cps.setValue(payrollSettings.getLeaveMoneyTypeCategory().getId().toString());
            companyPayrollSettingsManager.createOrUpdate(cps);
        }
        for (Map.Entry<String, ArrayList<PaymentDeductionSelectItem>> entry : payrollSettings.getAllowancesMap().entrySet()) {
            StringBuilder categories = new StringBuilder();
            if (entry.getValue() != null && entry.getValue().size() > 0) {
                Integer index = 0;
                for (PaymentDeductionSelectItem allowance : entry.getValue()) {
                    if (!index.equals(0)) {
                        categories.append(";").append(allowance.getId());
                    } else {
                        categories.append(allowance.getId());
                    }
                    index++;
                }
            }
            cps = companyPayrollSettingsManager.getCompanySettingValue(entry.getKey());
            if (cps == null) {
                cps = new EdsCompanyPayrollSettings();
            }
            cps.setKey(entry.getKey());
            cps.setValue(categories.toString());
            companyPayrollSettingsManager.createOrUpdate(cps);
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String calculateNITableLetter(HashMap<Integer, Boolean> params) {
        return String.valueOf(PayrollUtils.calculateNITableLetter(params));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPensionProviders() {
        List<EdsPensionProvider> providers = pensionProviderManager.getCompanyPensionProviders();
        SelectItem[] items = new SelectItem[providers.size()];
        int i = 0;
        for (EdsPensionProvider p : providers) {
            items[i] = new SelectItem(p.getObjectID(), p.getName());
            i++;
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPensionSchemes() {
        List<EdsPensionScheme> schemes = pensionSchemeManager.getCompanyPensionSchemes();
        SelectItem[] items = new SelectItem[schemes.size()];
        int i = 0;
        for (EdsPensionScheme ps : schemes) {
            if (ps.getType() != null) {
                items[i] = new SelectItem(ps.getObjectID(), ps.getName() + "(" + ps.getType().getName() + ")", ps.getType().getName());
            } else {
                items[i] = new SelectItem(ps.getObjectID(), ps.getName());
            }
            i++;
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<PensionSchemeData> getPensionSchemeList(ListingFilterParameter filterParametrs) {
        Integer countryID = null;
        List<EdsPensionScheme> pensionSchemeList = pensionSchemeManager.getPensionSchemes(filterParametrs);
        ArrayList<PensionSchemeData> pensionSchemeDataList = new ArrayList<>();
        PensionSchemeData pensionSchemeData;
        //limit 20
        int titalCount = pensionSchemeList.size();
        ListLoadConfig conf = filterParametrs.asConfig();
        if (conf != null) {
            pensionSchemeList = ListUtils.getSublist(pensionSchemeList, conf.getStart(), conf.getLimit());
        }
        //get country symbol
        String countrySID = getCompanyPayrollSettings(COUNTRY_ID);
        if (countrySID != null && !countrySID.isEmpty()) {
            countryID = Integer.parseInt(countrySID);
        }
        String symbolChecked = "";
        if (countryID != null) {
            EdsCountry country = countryManager.get(countryID);
            if (country != null && country.getCurrency() != null && country.getCurrency().getSymbol() != null) {
                symbolChecked = country.getCurrency().getSymbol();
            }
        }

        //set data to list result
        for (EdsPensionScheme ps : pensionSchemeList) {
            String employeeContribution;
            String employerContribution;
            if (ps.getDeductionType() != 1) {
                employeeContribution = symbolChecked + " " + ps.getDeductionValue();
            } else {
                employeeContribution = ps.getDeductionValue() + "%";
            }
            if (ps.getEmployerDeductionType() != 1) {
                employerContribution = symbolChecked + " " + ps.getEmployerDeductionValue();
            } else {
                employerContribution = ps.getEmployerDeductionValue() + "%";
            }
            pensionSchemeData = new PensionSchemeData();
            pensionSchemeData.setObjectId(ps.getObjectID());
            pensionSchemeData.setSchemeName(ps.getName());
            pensionSchemeData.setSchemeTypeName(ps.getType().getName());
            pensionSchemeData.setEmployeeContribution(employeeContribution);
            pensionSchemeData.setDeductFrom(ps.getDeductFrom());
            pensionSchemeData.setAllowTaxRelief(ps.getAllowTaxRelief());
            pensionSchemeData.setReduceByBasicRateTax(ps.getReduceByBasicRateTax());
            pensionSchemeData.setWagesInsufficient(ps.getWagesInsufficient());
            pensionSchemeData.setEmpMaxTaxableAmount(ps.getEmpMaxTaxableAmount());
            pensionSchemeData.setCompMaxTaxableAmount(ps.getCompMaxTaxableAmount());
            pensionSchemeData.setEmployerContribution(employerContribution);
            pensionSchemeDataList.add(pensionSchemeData);
        }
        return new ListResult<>(pensionSchemeDataList, titalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PensionSchemeData getPensionSchemeById(Integer id) {
        EdsCompany company = userManager.getUser().getCompany();
        EdsPensionScheme ps = pensionSchemeManager.getPensionSchema(company.getCountryZone() != null && company.getCountryZone().getCountry() != null ? company.getCountryZone().getCountry().getCode() : "");
        if (ps != null) {
            PensionSchemeData pensionSchemeData = new PensionSchemeData();

            pensionSchemeData.setObjectId(ps.getObjectID());
            pensionSchemeData.setAllowTaxRelief(ps.getAllowTaxRelief());
            pensionSchemeData.setDeductFrom(ps.getDeductFrom());
            pensionSchemeData.setDeductionType(ps.getDeductionType());
            pensionSchemeData.setDeductionValue(ps.getDeductionValue());
            pensionSchemeData.setNonLocalDeductionValue(ps.getNonLocalDeductionValue());
            pensionSchemeData.setEmployerDeductionType(ps.getEmployerDeductionType());
            pensionSchemeData.setEmployerDeductionValue(ps.getEmployerDeductionValue());
            pensionSchemeData.setEmployerNonLocalDeductionValue(ps.getEmployerNonLocalDeductionValue());
            pensionSchemeData.setEmployerSapPayment(ps.getEmployerSapPayment());
            pensionSchemeData.setEmployerSmpPayment(ps.getEmployerSmpPayment());
            pensionSchemeData.setEmployerSppPayment(ps.getEmployerSppPayment());
            pensionSchemeData.setEmployerSspPayment(ps.getEmployerSspPayment());
            pensionSchemeData.setOtherAcRef(ps.getOtherAcRef());
            if (ps.getProvider() != null) {
                pensionSchemeData.setProviderId(ps.getProvider().getObjectID());
                pensionSchemeData.setProviderName(ps.getProvider().getName());
            }
            if (ps.getCategories() != null && ps.getCategories().size() > 0) {
                for (EdsPayrollCategory category : ps.getCategories()) {
                    pensionSchemeData.getAllowances().add(category.createPaymentDeductionSelectItem());
                }
            }
            pensionSchemeData.setReduceByBasicRateTax(ps.getReduceByBasicRateTax());
            pensionSchemeData.setSchemeName(ps.getName());
            if (ps.getType() != null) {
                pensionSchemeData.setSchemeType(ps.getType().getObjectID());
                pensionSchemeData.setSchemeTypeName(ps.getType().getName());
            }
            pensionSchemeData.setWagesInsufficient(ps.getWagesInsufficient());
            pensionSchemeData.setSspPayment(ps.getSspPayment());
            pensionSchemeData.setSmpPayment(ps.getSmpPayment());
            pensionSchemeData.setSapPayment(ps.getSapPayment());
            pensionSchemeData.setSppPayment(ps.getSppPayment());
            pensionSchemeData.setEmpMaxTaxableAmount(ps.getEmpMaxTaxableAmount());
            pensionSchemeData.setCompMaxTaxableAmount(ps.getCompMaxTaxableAmount());

            return pensionSchemeData;
        }
        return null;
    }

    public void deletePensionScheme(Integer id) {
        pensionSchemeManager.deletePensionScheme(id);
    }

    public void savePensionScheme(PensionSchemeData schemeData, Boolean update) {
        EdsPensionScheme scheme;
        EdsPayrollCategory category;
        if (schemeData.getObjectId() != null) {
            scheme = pensionSchemeManager.get(schemeData.getObjectId());
            categoryManager.deleteReferenceBySchemaID(schemeData.getObjectId());
        } else {
            scheme = new EdsPensionScheme();
        }
        scheme.setName(schemeData.getSchemeName());
        if (schemeData.getProviderId() != null) {
            scheme.setProvider(pensionProviderManager.get(schemeData.getProviderId()));
        }
        if (schemeData.getSchemeType() != null) {
            scheme.setType(referenceManager.get(schemeData.getSchemeType()));
        }

        scheme.setOtherAcRef(schemeData.getOtherAcRef());

        scheme.setDeductionType(schemeData.getDeductionType());
        scheme.setDeductionValue(schemeData.getDeductionValue());
        scheme.setNonLocalDeductionValue(schemeData.getNonLocalDeductionValue());
        scheme.setDeductFrom(schemeData.getDeductFrom());
        scheme.setAllowTaxRelief(schemeData.getAllowTaxRelief());
        scheme.setReduceByBasicRateTax(schemeData.getReduceByBasicRateTax());
        scheme.setSspPayment(schemeData.getSspPayment());
        scheme.setSmpPayment(schemeData.getSmpPayment());
        scheme.setSapPayment(schemeData.getSapPayment());
        scheme.setSppPayment(schemeData.getSppPayment());

        scheme.setEmployerDeductionType(schemeData.getEmployerDeductionType());
        scheme.setEmployerDeductionValue(schemeData.getEmployerDeductionValue());
        scheme.setEmployerNonLocalDeductionValue(schemeData.getEmployerNonLocalDeductionValue());
        scheme.setEmployerSspPayment(schemeData.getEmployerSspPayment());
        scheme.setEmployerSmpPayment(schemeData.getEmployerSmpPayment());
        scheme.setEmployerSapPayment(schemeData.getEmployerSapPayment());
        scheme.setEmployerSppPayment(schemeData.getEmployerSppPayment());

        scheme.setWagesInsufficient(schemeData.getWagesInsufficient());
        scheme.setEmpMaxTaxableAmount(schemeData.getEmpMaxTaxableAmount());
        scheme.setCompMaxTaxableAmount(schemeData.getCompMaxTaxableAmount());

        pensionSchemeManager.createOrUpdate(scheme);
        if (schemeData.getObjectId() == null) {
            baseEventsPostProcessor.registerEvent(PensionSchemeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, scheme, userManager.getUser());
        } else {
            baseEventsPostProcessor.registerEvent(PensionSchemeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, scheme, userManager.getUser());
        }
        if (schemeData.getAllowances().size() > 0) {
            for (PaymentDeductionSelectItem item : schemeData.getAllowances()) {
                category = categoryManager.get(item.getId());
                if (category != null) {
                    category.setPensionScheme(scheme);
                }
            }
        }
    }

    public void savePensionProvider(PensionProviderData providerData) {
        EdsPensionProvider provider;
        if (providerData.getObjectID() != null) {
            provider = pensionProviderManager.get(providerData.getObjectID());
        } else {
            provider = new EdsPensionProvider();
        }
        provider.setName(providerData.getProviderName());
        provider.setProviderAccountRef(providerData.getProviderAccountRef());
        provider.setProviderOtherRef(providerData.getProviderOtherRef());
        provider.setProviderAddress(providerData.getProviderAddress());
        provider.setProviderTownCity(providerData.getProviderTownCity());
        provider.setProviderCounty(providerData.getProviderCounty());
        provider.setProviderPostCode(providerData.getProviderPostCode());
        if (providerData.getProviderCountry() != null) {
            provider.setProviderCountry(countryManager.get(providerData.getProviderCountry().getId()));
        }
        provider.setProviderTelNo(providerData.getProviderTelNo());
        provider.setProviderFaxNo(providerData.getProviderFaxNo());
        provider.setProviderEmail(providerData.getProviderEmail());
        provider.setProviderCPName(providerData.getProviderCPName());
        provider.setProviderCPMobile(providerData.getProviderCPMobile());
        provider.setLastPayment(providerData.getLastPayment());
        provider.setNextPayment(providerData.getNextPayment());

        provider.setBankName(providerData.getBankName());
        provider.setBranchName(providerData.getBranchName());
        provider.setBankAddress(providerData.getBankAddress());
        provider.setBankTownCity(providerData.getBankTownCity());
        provider.setBankCounty(providerData.getBankCounty());
        provider.setBankPostCode(providerData.getBankPostCode());
        if (providerData.getBankCountry() != null) {
            provider.setBankCountry(countryManager.get(providerData.getBankCountry().getId()));
        }
        provider.setBankCPName(providerData.getBankCPName());
        provider.setBankTelNo(providerData.getBankTelNo());
        provider.setBankFaxNo(providerData.getBankFaxNo());
        provider.setBankEmail(providerData.getBankEmail());
        provider.setSortCode(providerData.getSortCode());
        provider.setAccountNo(providerData.getAccountNo());
        provider.setNameShownOnAccount(providerData.getNameShownOnAccount());
        provider.setBankAccountRef(providerData.getBankAccountRef());
        provider.setBankOtherRefNo(providerData.getBankOtherRefNo());

        pensionProviderManager.createOrUpdate(provider);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<NiTaxChangesListItem> getNiTaxChanges(ListingFilterParameter fp) {
        List<EmployeePayrollSettingsHistory> epshList = employeePayrollSettingsHistoryManager.getCompanyEmployeePayrollSettingsHistory(fp);
        ArrayList<NiTaxChangesListItem> niTaxChangesListItems = new ArrayList<>();
        Integer totalCount = epshList.size();
        for (EmployeePayrollSettingsHistory epsh : epshList) {
            NiTaxChangesListItem items = new NiTaxChangesListItem();
            items.setObjectId(epsh.getObjectID());
            items.setEmployeeName(epsh.getEmployeePayrollSettings() != null ? epsh.getEmployeePayrollSettings().getEmployee().getFullName() : "");
            items.setOldCode(epsh.getOldValue());
            items.setNewCode(epsh.getNewValue());
            if (epsh.getDate() != null) {
                items.setDate(new Date(epsh.getDate().getTime()));
            }
            if (epsh.getMethodOfChange() != null) {
                items.setMethodOfChange(epsh.getMethodOfChange().getName());
            }
            niTaxChangesListItems.add(items);
        }

        return new ListResult<>(niTaxChangesListItems, totalCount);
    }

    public void createBankAccount(UserBankAccountData bankAccount) {
        //user bank account details;
        EdsUser user = userManager.get(bankAccount.getObjectID());
        EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(user);
        if (userBankAccount == null) {
            userBankAccount = new EdsUserBankAccount();
            userBankAccount.setUser(user);
        }
        userBankAccount.setBankName(bankAccount.getBankName());
        userBankAccount.setBankAddress(bankAccount.getBankAddress());
        userBankAccount.setAccountNumber(bankAccount.getAccountNumber());
        userBankAccount.setAccountName(bankAccount.getAccountName());
        userBankAccount.setSwiftCode(bankAccount.getSwiftCode());
        userBankAccount.setSortCode(bankAccount.getSortCode());
        userBankAccount.setIbanCode(bankAccount.getIbanCode());
        userBankAccount.setAgentID(bankAccount.getAgentID());

        if (userBankAccount.getObjectID() != null) {
            userBankAccountManager.update(userBankAccount);
        } else {
            userBankAccountManager.create(userBankAccount);
        }
    }

    private BigDecimal calculatePayslipEmployeeBonus(Integer employeeID, BigDecimal baseAmount) {
        BigDecimal amountForSet = BigDecimal.ZERO;
        EdsSimpleRate payslipEmployeeBonus = simpleRateManager.getEmployeeBonus(employeeID);
        if (payslipEmployeeBonus != null) {
            if (payslipEmployeeBonus.getPercentage() != null) {
                amountForSet = baseAmount.multiply(payslipEmployeeBonus.getPercentage()).divide(new BigDecimal(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
            } else if (payslipEmployeeBonus.getFixedAmount() != null) {
                amountForSet = payslipEmployeeBonus.getFixedAmount();
            }
        }
        return amountForSet.setScale(ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<String, Date> calculateDatesByWeekOrMonthNumber(int weekOrMonthNumber, int frequencyID, Date selectedDate) {
        final HashMap<String, Date> dates = new HashMap<>();
        final Date startDate = calculatePeriodStartDate(weekOrMonthNumber, frequencyID, selectedDate);
        dates.put(WEEKORMONTH_START_DATE, startDate);
        dates.put(WEEKORMONTH_END_DATE, calculatePeriodEndDate(weekOrMonthNumber, frequencyID, startDate));

        return dates;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Date calculatePeriodStartDate(int weekOrMonthNumber, int frequencyID, Date selectedDate) {
        final Calendar startDate = isCountryUK() ? PayrollUtils.getBeginningOfTaxYear(null) : PayrollUtils.getBeginningOfTaxYearForNonUK(selectedDate);
        final Frequency frequency = Frequency.getByID(frequencyID);
        if (WEEKLY.getCycle() == frequency.getCycle()) {
            startDate.set(Calendar.DAY_OF_YEAR, startDate.get(Calendar.DAY_OF_YEAR) + (weekOrMonthNumber - 1) * 7);
        } else if (MONTHLY.getId() == frequencyID) {
            startDate.set(Calendar.MONTH, startDate.get(Calendar.MONTH) + weekOrMonthNumber - 1);
        }
        return startDate.getTime();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Date calculatePeriodEndDate(int weekOrMonthNumber, int frequencyID, Date date) {
        final Frequency frequency = Frequency.getByID(frequencyID);
        if (frequency.getNumberOfCyclesInYear() < weekOrMonthNumber) {
            return isCountryUK() ? PayrollUtils.getEndOfTaxYear(null).getTime() : PayrollUtils.getEndOfTaxYearForNonUK(date).getTime();
        }
        final Calendar endDate = Calendar.getInstance();
        endDate.setTime(date != null ? date : calculatePeriodStartDate(weekOrMonthNumber, frequencyID, date));
        if (frequency.getCycle() == WEEKLY.getCycle()) {
            endDate.set(Calendar.DATE, endDate.get(Calendar.DATE) + 7);
        } else if (MONTHLY == frequency) {
            endDate.set(Calendar.MONTH, endDate.get(Calendar.MONTH) + 1);
        }
        endDate.set(Calendar.DATE, endDate.get(Calendar.DATE) - 1);
        return endDate.getTime();
    }

    public Integer savePaymentDeductionCategory(CategoryObject category) {

        if (categoryManager.isCategoryCodeExists(category.getType(), category.getCode(), category.getId())) {
            return -1;
        }

        EdsPayrollCategory edsCategory;
        if (category.getId() != null) {
            edsCategory = categoryManager.get(category.getId());
        } else {
            edsCategory = new EdsPayrollCategory();
        }

        edsCategory.setName(category.getName());
        edsCategory.setCode(category.getCode());
        edsCategory.setType(category.getType());
        edsCategory.setNiable(category.getNiable());
        edsCategory.setTaxable(category.getTaxable());
        edsCategory.setExcludeInCustomDeductions(category.getExcludeInCustomDeductions());
        edsCategory.setExcludeSickLeave(category.isExcludeSickLeave());
        edsCategory.setExcludeAnnualLeave(category.isExcludeAnnualLeave());
        edsCategory.setNonMoneyType(category.isNonMoneyType());
        edsCategory.setPensionable(category.getPensionable());
        edsCategory.setAdvancePayment(category.isAdvancePayment());
        edsCategory.setDebitToAccountID(category.getDebitToAccountID());
        edsCategory.setCreditToAccountID(category.getCreditToAccountID());
        edsCategory.setArabic(category.isArabic());
        edsCategory.setFormula(saveAndGetFormula(category));
        edsCategory.setCashAdvance(category.isCashAdvance());
        edsCategory.setSystemCode(category.getSystemCode());

        if (Boolean.TRUE.equals(category.getDefaultCategory())) {
            categoryManager.resetDefaultCategory();
            edsCategory.setDefaultCategory(category.getDefaultCategory());
        }

        if (category.getLocaleItem() != null){
            EdsReferenceLocale locale = allInOneServiceLocal.saveEntityLocale(category.getLocaleItem());
            edsCategory.setLocale(locale);
        }

        categoryManager.createOrUpdate(edsCategory);

        return edsCategory.getObjectID();
    }

    private EdsFormula saveAndGetFormula(CategoryObject category) {
        EdsFormula formula = new EdsFormula();

        if (category.getSimpleRate() != null) {
            EdsSimpleRate simpleRate = new EdsSimpleRate();
            simpleRate.setPercentage(category.getSimpleRate().getPercentage());
            simpleRate.setFixedAmount(category.getSimpleRate().getFixedAmount());

            simpleRateManager.create(simpleRate);

            formula.setSimpleRate(simpleRate);
        } else {
            for (CategoryRate rate : category.getMultiRangeRates()) {
                EdsMultiRangeRate multiRangeRate = new EdsMultiRangeRate();
                multiRangeRate.setPercentage(rate.getPercentage());
                multiRangeRate.setFixedAmount(rate.getFixedAmount());
                multiRangeRate.setFrom(rate.getFrom());
                multiRangeRate.setTo(rate.getTo());
                multiRangeRate.setFormula(formula);

                formula.getMultiRangeRates().add(multiRangeRate);
            }
            formula.setType(EdsFormula.MULTI_RANGE_RATE);
        }

        formulaManager.create(formula);

        return formula;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CategoryObject getPaymentDeductionCategory(Integer objectID) {
        CategoryObject co = new CategoryObject();
        if (objectID != null) {
            EdsPayrollCategory category = categoryManager.get(objectID);

            co.setId(category.getObjectID());
            co.setType(category.getType());
            co.setCode(category.getCode());
            co.setName(category.getRealName());
            co.setTaxable(category.getTaxable());
            co.setNiable(category.getNiable());
            co.setPensionable(category.getPensionable());
            co.setAdvancePayment(category.isAdvancePayment());
            co.setDefaultCategory(category.getDefaultCategory());
            co.setExcludeSickLeave(category.isExcludeSickLeave());
            co.setExcludeAnnualLeave(category.isExcludeAnnualLeave());
            co.setExcludeInCustomDeductions(category.getExcludeInCustomDeductions());
            co.setNonMoneyType(category.isNonMoneyType());
            co.setSystemCode(category.getSystemCode());
            if (category.getLocale() != null) {
                co.setLocaleItem(category.getLocale().toRPC());
            }
            if (category.isCashAdvance()) {
                EdsAccount debitAccount;
                EdsAccount prepaymentAccount = accountingManager.getAccountByKey(EdsAccount.PREPAYMENT);
                if (category.getDebitToAccountID() != null) {
                    co.setDebitToAccountID(category.getDebitToAccountID());
                    debitAccount = accountingManager.get(category.getDebitToAccountID());
                    if (debitAccount != null) {
                        co.setDebitToAccount(debitAccount.createAccountItem());
                    }
                }
                if (category.getCreditToAccountID() != null) {
                    if (prepaymentAccount != null && prepaymentAccount.getObjectID().equals(category.getCreditToAccountID())) {
                        co.setCreditToAccountID(category.getCreditToAccountID());
                        co.setCreditToAccount(prepaymentAccount.createAccountItem());
                    } else {
                        EdsAccount creditToAccount = null;
                        if (category.getCreditToAccountID() != null) {
                            creditToAccount = accountingManager.get(category.getCreditToAccountID());
                        }
                        co.setCreditToAccountID(category.getCreditToAccountID());
                        co.setCreditToAccount(creditToAccount != null ? creditToAccount.createAccountItem() : null);
                    }
                } else {
                    if (prepaymentAccount != null) {
                        co.setCreditToAccountID(prepaymentAccount.getObjectID());
                        co.setCreditToAccount(prepaymentAccount.createAccountItem());
                    }
                }
                co.setCashAdvance(true);
            } else {
                EdsAccount creditAccount = null;
                EdsAccount debitAccount = null;
                if (category.getDebitToAccountID() != null) {
                    debitAccount = accountingManager.get(category.getDebitToAccountID());
                }
                co.setDebitToAccountID(category.getDebitToAccountID());
                co.setDebitToAccount(debitAccount != null ? debitAccount.createAccountItem() : null);

                if (category.getCreditToAccountID() != null) {
                    creditAccount = accountingManager.get(category.getCreditToAccountID());
                }
                co.setCreditToAccountID(category.getCreditToAccountID());
                co.setCreditToAccount(creditAccount != null ? creditAccount.createAccountItem() : null);
            }

            if (category.getFormula() != null) {
                EdsSimpleRate simpleRate = category.getFormula().getSimpleRate();
                if (simpleRate == null) {
                    List<EdsMultiRangeRate> multiRangeRates = category.getFormula().getMultiRangeRates();
                    for (EdsMultiRangeRate multiRangeRate : multiRangeRates) {
                        CategoryRate multiRangeCatRate = new CategoryRate();
                        if (multiRangeRate.getPercentage() == null) {
                            multiRangeCatRate.setFixedAmount(multiRangeRate.getFixedAmount());
                        } else {
                            multiRangeCatRate.setPercentage(multiRangeRate.getPercentage());
                        }
                        multiRangeCatRate.setFrom(multiRangeRate.getFrom());
                        multiRangeCatRate.setTo(multiRangeRate.getTo());

                        co.addMultiRangeRate(multiRangeCatRate);
                    }
                } else {
                    CategoryRate simpleCatRate = new CategoryRate();
                    if (simpleRate.getPercentage() == null) {
                        simpleCatRate.setFixedAmount(simpleRate.getFixedAmount());
                    } else {
                        simpleCatRate.setPercentage(simpleRate.getPercentage());
                    }
                    co.setSimpleRate(simpleCatRate);
                }
            }
        }
        EdsAccount prepaymentAccount = accountingManager.getAccountByKey(EdsAccount.PREPAYMENT);
        if (prepaymentAccount != null) {
            co.setDefaultAccountForCashLoans(prepaymentAccount.createAccountItem());
        }
        co.setUk(isCountryUK());
        return co;
    }

    public Integer rollback(Integer frequency, Integer period, Integer taxYear, Integer employeeID) {
        List<P11> payslips = p11Manager.getPayslipsForRollback(frequency, period, taxYear, employeeID, employeeID == null ? employeeManager.getUser().getCompany().getObjectID() : null);
        if (payslips == null || payslips.isEmpty()) {
            return 0;
        }
        int payslipSize = payslips.size();
        for (P11 payslip : payslips) {
            Integer payslipID = payslip.getObjectID();
            transactionManager.deleteTransactionsByPayslip(payslipID);
            timesheetManager.deletePayslipIDsFromTimeSheet(payslipID, employeeID);
            payslipPaymentsManager.deleteByPayslipID(payslipID);
            payslip.setDeleted(true);
            p11Manager.update(payslip);
        }
        return payslipSize;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyTaxYears(Integer companyID) {
        Integer minTaxYear = p11Manager.getMinTaxYear();
        final int maxTaxTear = PayrollUtils.getTaxYearEnd(null);
        if (minTaxYear == null) {
            minTaxYear = maxTaxTear;
        }
        final int size = maxTaxTear - minTaxYear + 1;
        final SelectItem[] years = new SelectItem[size];
        for (int year = maxTaxTear; year >= minTaxYear; year--) {
            years[maxTaxTear - year] = new SelectItem(year, (year - 1) + "-" + year);
        }
        return years;
    }

    public PaymentDeductionSelectItem getCategoryObject(String code) {
        final EdsPayrollCategory cat = categoryManager.getCategoryByCode(code);
        return cat.createPaymentDeductionSelectItem();
    }

    public PaymentDeductionObject getPaymentDeductionObject(String code, BigDecimal amount) {
        return new PaymentDeductionObject(getCategoryObject(code), amount);
    }

    public void setAmount(String code, BigDecimal amount, HashMap<String, PaymentDeductionObject> map) {
        if (map.containsKey(code)) {
            map.get(code).setPaymentAmount(amount);
        } else {
            map.put(code, getPaymentDeductionObject(code, amount));
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean isCountryUK() {
        String cps = getCompanyPayrollSettings(COUNTRY_ID);
        return "45".equals(cps); //Default is NON UK // 45 -> UK Country ID;
    }

    private boolean isNotSaudiCompany() {
        String cps = getCompanyPayrollSettings(COUNTRY_ID);
        return !"187".equals(cps);
    }

    private List<EdsExpenseReport> getUnpaidExpenseReports(ListingFilterParameter lfp) {
        EdsReference statusApproved = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_APPROVED);
        EdsReference statusPartiallyPaid = referenceManager.findReference(EXPENSE_STATUS, PARTIALLY_PAID);
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(lfp.getEmployeeId());
        boolean enabledMultiCurrency = "true".equals(getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL));

        //Avoid expenses with foreign currencies
        EdsEmployee employee = employeeManager.get(lfp.getEmployeeId());
        EdsCurrency currency = employee != null ? employee.getSalaryCurrency() : null;
        if (currency != null) {
            fp.setCurrencyID(currency.getObjectID());
        } else if (enabledMultiCurrency) {
            EdsCurrency baseCurrency = financialSettingsManager.getFinancialSettings() != null ? financialSettingsManager.getFinancialSettings().getCurrency() : null;
            Integer baseCurrencyId = baseCurrency != null ? baseCurrency.getObjectID() : null;
            fp.setCurrencyID(baseCurrencyId);
        } else {
            fp.setCurrencyID(lfp.getBaseCurrencyID());
        }

        fp.setStatusIDs(new Integer[]{statusApproved.getObjectID(), statusPartiallyPaid.getObjectID()});
        fp.setBaseCurrencyID(lfp.getBaseCurrencyID());
        fp.setEndDate(DateUtil.getDayLastTime(lfp.getEndDate()));
        return expenseReportManager.getUnpaidExpenseClaimsForPayslip(fp);
    }

    @Override
    public PaymentDeductionSelectItem[] getCategoriesForLookUp(ListingFilterParameter filterParametrs) {
        return categoryManager.getCategoriesForLookUp(filterParametrs);
    }

    @Override
    public PaymentDeductionSelectItem getCategoryById(Integer categoryId) {
        return categoryManager.get(categoryId).createPaymentDeductionSelectItem();
    }

    @Override
    public ListResult<GroupPayrunData> getPayslipTableList(ListingFilterParameter fp) {

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEmployee.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get GroupPayrun list (from solr)");
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        FacetFilterRpc groupPayrunFacetFilter = fp.getFacetFilter();
        if (groupPayrunFacetFilter != null && !groupPayrunFacetFilter.isFilterChanges()) {
            groupPayrunFacetFilter = commonServiceLocal.getUserFacetFilter(groupPayrunFacetFilter);
        }
        if (groupPayrunFacetFilter != null) {
            if (groupPayrunFacetFilter.getSearchKey() != null && !"".equals(groupPayrunFacetFilter.getSearchKey())) {
                fp.setSearchKey(groupPayrunFacetFilter.getSearchKey());
            }
            fp.setFacetFilter(groupPayrunFacetFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();

        String solrQuery = QueryBuilderForSolr.getGroupPayrunSolrQuery(fp) +
                generatePermissionQuery(PermissionConstants.PAYROLL_GROUP_PAYRUN_LIST) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(groupPayrunFacetFilter, edsCompany, null, null);
        return getGroupPayrunResponse(fp, solrQuery);
    }

    private ListResult<GroupPayrunData> getGroupPayrunResponse(ListingFilterParameter fp, String solrQuery) {
        Page<GroupPayrunSolrDoc> groupPayrunSolrDocs = groupPayrunSolrComponent.getList(fp, solrQuery);
        return getGroupPayrunFromSolrResult(groupPayrunSolrDocs, fp);
    }

    private ListResult<GroupPayrunData> getGroupPayrunFromSolrResult(Page<GroupPayrunSolrDoc> groupPayrunSolrDocs, ListingFilterParameter filterParametrs) {
        ArrayList<GroupPayrunData> groupPayrunItems = new ArrayList<>();
        int totalNumber = 0;
        if (groupPayrunSolrDocs != null && groupPayrunSolrDocs.getContent() != null) {
            totalNumber = (int) groupPayrunSolrDocs.getTotalElements();

            for (GroupPayrunSolrDoc relevantDoc : groupPayrunSolrDocs.getContent()) {
                GroupPayrunData tableItem = new GroupPayrunData();
                tableItem.setObjectID(relevantDoc.getGroupPayrunId());
                tableItem.setMonthID(relevantDoc.getMonthId());
                if (relevantDoc.getYear() != null && relevantDoc.getYear() > 0) {
                    tableItem.setMonth(relevantDoc.getMonthName() + ", " + relevantDoc.getYear().toString());
                } else {
                    tableItem.setMonth(relevantDoc.getMonthName());
                }
                Integer statusId = relevantDoc.getStatusId();
                if (statusId != null) {
                    EdsReference status = referenceManager.get(statusId);
                    if (status != null) {
                        tableItem.setStatus(status.getName() != null ? status.getName() : relevantDoc.getStatusName() != null ? relevantDoc.getStatusName() : "");
                        tableItem.setStatusCode(status.getCode());
                    }
                }

                tableItem.setCreator(new SelectItem(relevantDoc.getPreparerId(), relevantDoc.getPreparerName()));
                tableItem.setApprover(new SelectItem(relevantDoc.getApproverId(), relevantDoc.getApproverName()));
                tableItem.setPayrollBatchItem(new SelectItem(relevantDoc.getPayrollBatchId(), relevantDoc.getPayrollBatchName()));
                tableItem.setTotalAmount(BigDecimal.valueOf(relevantDoc.getTotal()));
                tableItem.setTotalInBase(BigDecimal.valueOf(relevantDoc.getTotalInBase()));
                tableItem.setCurrencyName(relevantDoc.getForeignCurrencyName());
                tableItem.setPayMethodName(relevantDoc.getPaymentMethodName());
                if (relevantDoc.getProcessDate() != null) {
                    tableItem.setProcessDate(new DateNonConvertable(relevantDoc.getProcessDate()));
                }
                tableItem.setProjectItem(new SelectItem(relevantDoc.getProjectId(), relevantDoc.getProjectName()));
                tableItem.setLocationItem(new SelectItem(relevantDoc.getLocationId(), relevantDoc.getLocationName()));
                tableItem.setBasicSalary(Objects.nonNull(relevantDoc.getBasicSalary()) ? BigDecimal.valueOf(relevantDoc.getBasicSalary()) : BigDecimal.valueOf(0));
                tableItem.setAllowance(Objects.nonNull(relevantDoc.getAllowance()) ? BigDecimal.valueOf(relevantDoc.getAllowance()) : BigDecimal.valueOf(0));
                tableItem.setPension(Objects.nonNull(relevantDoc.getPension()) ? BigDecimal.valueOf(relevantDoc.getPension()) : BigDecimal.valueOf(0));
                tableItem.setDeduction(Objects.nonNull(relevantDoc.getDeduction()) ? BigDecimal.valueOf(relevantDoc.getDeduction()) : BigDecimal.valueOf(0));
                tableItem.setExpense(Objects.nonNull(relevantDoc.getExpense()) ? BigDecimal.valueOf(relevantDoc.getExpense()) : BigDecimal.valueOf(0));
                groupPayrunItems.add(tableItem);
            }
        }
        return new ListResult<>(groupPayrunItems, totalNumber);
    }

    @Override
    public GroupPayrunData getPayslipTable(PayslipFilter filter) {
        GroupPayrunData result;
        ListingFilterParameter lfp;
        List<SinglePayrunItem> singlePayrunItems;
        EdsCompany company = userManager.getUser().getCompany();
        EdsCurrency baseCurrency = financialSettingsManager.getFinancialSettings().getCurrency();

        result = new GroupPayrunData();
        result.setObjectID(filter.isFromExisting() ? null : filter.getObjectID());
        result.setDoubleApprovedEnabled("true".equals(getCompanyPayrollSettings(ENABLED_DOUBLE_APPROVER_PAYRUN)));
        result.setDoubleConfirmationEnabled("true".equals(getCompanyPayrollSettings(DOUBLE_CONFIRMATION)));
        result.setSendNotification("true".equals(getCompanyPayrollSettings(BY_DEFAULT_EMAIL_NOTIFICATION)));
        result.setEnabledMultiCurrency("true".equals(getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL)));

        boolean isEmployeeCodeInteger = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER);

        List<String> validateList;
        if (filter.getObjectID() != null) {
            singlePayrunItems = new ArrayList<>();
            EdsPayslipTable payslipTable = payslipTableManager.get(filter.getObjectID());
            result.setMonthID(payslipTable.getMonthID());
            result.setYear(payslipTable.getYear());
            result.setMonth(payslipTable.getMonth());
            result.setFrequency(payslipTable.getFrequency());

            result.setCurrency(payslipTable.getCurrency() != null ? payslipTable.getCurrency().createCurrencyItem() : null);
            result.setExchangeRate(payslipTable.getExchangeRate());

            result.setProcessDate(payslipTable.getProcessDate() != null ? new DateNonConvertable(payslipTable.getProcessDate()) :
                    new DateNonConvertable(new Date(payslipTable.getYear() - 1900, payslipTable.getMonthID(), DateUtil.getDateInMonth(payslipTable.getYear(), payslipTable.getMonthID()))));

            if (payslipTable.getPayrollBatch() != null) {
                result.setPayrollBatchItem(payslipTable.getPayrollBatch().getAsSelectItem());
            } else if (payslipTable.getProject() != null) {
                result.setProjectItem(payslipTable.getProject().getAsSelectItem());
            } else if (payslipTable.getLocation() != null) {
                result.setLocationItem(payslipTable.getLocation().getAsSelectItem());
            } else if (!"true".equals(this.getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL))) {
                result.setPayrollBatchItem(new SelectItem(0, commonLocalizer.localize("allEmployees", "All Employees")));
            }
            if (payslipTable.getProject() != null) {
                result.setProjectItem(payslipTable.getProject().getAsSelectItem());
            }
            if (payslipTable.getApprover() != null) {
                if (payslipTable.getApprover().getPosition() != null) {
                    result.setApprover(new SelectItem(payslipTable.getApprover().getObjectID(), payslipTable.getApprover().getFullName(), payslipTable.getApprover().getPosition().getName()));
                } else {
                    if (payslipTable.getApprover().getProfile() != null && payslipTable.getApprover().getProfile().getEmployeeCode() != null) {
                        result.setApprover(new SelectItem(payslipTable.getApprover().getObjectID(), payslipTable.getApprover().getProfile().getEmployeeCode() + " - " + payslipTable.getApprover().getFullName()));
                    } else {
                        result.setApprover(new SelectItem(payslipTable.getApprover().getObjectID(), payslipTable.getApprover().getFullName()));
                    }
                }
            }

            if (payslipTable.getPreparer() != null) {
                if (payslipTable.getPreparer().getPosition() != null) {
                    result.setCreator(new SelectItem(payslipTable.getPreparer().getObjectID(), payslipTable.getPreparer().getFullName(), payslipTable.getPreparer().getPosition().getName()));
                } else {
                    result.setCreator(new SelectItem(payslipTable.getPreparer().getObjectID(), payslipTable.getPreparer().getFullName()));
                }
            }

            result.setTotalAmount(payslipTable.getTotalAmount());
            result.setTotalInBase(payslipTable.getTotalInBase());
            result.setCreatedDate(new DateNonConvertable(payslipTable.getCreationDate()));
            result.setApproveDate(new DateNonConvertable(payslipTable.getApprovedDate()));
            result.setFromTaxi(payslipTable.isFromTaxi());
            if (payslipTable.getPaymentMethod() != null) {
                result.setPayMethod(payslipTable.getPaymentMethod().getAsSelectItem());
            }
            List<EdsPaymentDeduction> categories;
            HashSet<PaymentDeductionSelectItem> payments = new HashSet<>();
            HashSet<PaymentDeductionSelectItem> deductions = new HashSet<>();
            HashSet<PaymentDeductionSelectItem> taxes = new HashSet<>();
            HashSet<PaymentDeductionSelectItem> employerContributions = new HashSet<>();
            Map<Integer, BigDecimal> map = null;
            boolean empInBase = result.getCurrency() == null || result.getCurrency().getId().equals(baseCurrency.getObjectID());
            boolean expInBase;
            EdsPayrollCategory expenseCategory = categoryManager.getCategoryByCode(EXPENSE_REPORT);
            PaymentDeductionSelectItem expenseCategoryItem = expenseCategory.createPaymentDeductionSelectItem();

            if (filter.isFromExcelHandler()) {
                map = payslipTableItemManager.getRecurringCategoriesTotalByItems(filter.getObjectID());
            }

            for (EdsPayslipTableItem item : payslipTable.getPayslipTableItems()) {
                EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(userManager.get(item.getEmployee().getObjectID()));
                SinglePayrunItem pItem = item.getRPC(userBankAccount);
                if (item.getEmployee() != null && item.getEmployee().getProfile() != null
                        && !"".equals(item.getEmployee().getProfile().getEmployeeCode())) {

                    if (filter.isEmpCodeAdjoined() && item.getEmployee().getProfile() != null && item.getEmployee().getProfile().getEmployeeCode() != null) {
                        pItem.setEmployee(item.getEmployee().getProfile().getEmployeeCode()
                                .concat(" -> ")
                                .concat(item.getEmployee().getFullName()));
                    }
                    if (isEmployeeCodeInteger && StringUtils.isNotEmpty(pItem.getEmployeeCode())) {
                        try {
                            pItem.setEmployeeNumber(Long.parseLong(pItem.getEmployeeCode().replaceAll("\\D", "")));
                        } catch (NumberFormatException ignored) {
                        }
                    }
                }
                if (filter.isFromExcelHandler() && item.getEmployee() != null) {
                    pItem.setFirstName(item.getEmployee().getFirstName());
                    pItem.setLastName(item.getEmployee().getLastName());
                }
                pItem.setSalary(item.getBasicSalary());
                pItem.setApproved(item.getApproved());
                categories = payslipTableItemManager.getItemCategories(item.getObjectID());
                if (categories != null && categories.size() > 0) {
                    for (EdsPaymentDeduction paymentDeduction : categories) {
                        PaymentDeductionObject object = paymentDeduction.getRPC();
                        object.setPaymentAmount(payslipPaymentsManager.getPaymentAmount(paymentDeduction.getObjectID(), item.getObjectID()));
                        if (object.isPaymentCategory()) {
                            pItem.getPaymentCategories().add(object);
                            payments.add(object.getCategoryItem());
                        } else if (object.isTaxCategory()) {
                            pItem.getTaxCategories().add(object);
                            taxes.add(object.getCategoryItem());
                        } else if (object.isEmployerContributionCategory()) {
                            pItem.getEmployerContributionCategories().add(object);
                            employerContributions.add(object.getCategoryItem());
                        } else if (object.isDeductionCategory()) {
                            if (LEAVE_DEDUCTIONS.equals(object.getCategoryItem().getCode()) && object.getLeaveDaysCount() != null) {
                                pItem.setNonPaidLeaveDays(pItem.getNonPaidLeaveDays() + object.getLeaveDaysCount().intValue());
                            }
                            pItem.getDeductionCategories().add(object);
                            deductions.add(object.getCategoryItem());
                        } else if (object.isMaterialAidCategory()) {
                            payments.add(object.getCategoryItem());
                        }
                    }
                }
                if (pItem.getAdditionalPay() != null && BigDecimal.ZERO.compareTo(pItem.getAdditionalPay()) < 0) {
                    EdsPayrollCategory bonusCategory = categoryManager.getCategoryByCode(BONUS);
                    if (bonusCategory != null) {
                        PaymentDeductionObject bonus = new PaymentDeductionObject();
                        bonus.setCategoryItem(bonusCategory.createPaymentDeductionSelectItem());
                        bonus.setPaymentAmount(pItem.getAdditionalPay());
                        pItem.getPaymentCategories().add(bonus);
                        payments.add(bonus.getCategoryItem());
                    }
                }
                lfp = new ListingFilterParameter();
                lfp.setEmployeeId(pItem.getEmployeeID());
                lfp.setEndDate(filter.getToDate() != null ? filter.getToDate().getNonConvertedDate() : pItem.getToDate().getNonConvertedDate());
                lfp.setBaseCurrencyID(baseCurrency.getObjectID());

                BigDecimal amount = BigDecimal.ZERO;
                List<ExpenseData> expenses = new LinkedList<>();
                String expensePaidFromAccount = getCompanyPayrollSettings(EXPENSE_PAID_ACCOUNT);
                EdsAccount paidFromAccount = expensePaidFromAccount != null && !expensePaidFromAccount.isEmpty() ? accountingManager.get(Integer.valueOf(expensePaidFromAccount)) : null;
                List<EdsExpenseReport> linkedExpenses = expenseReportManager.getPayslipTableItemRelatedExpenseClaims(pItem.getObjectID());
                for (EdsExpenseReport exp : linkedExpenses) {
                    expInBase = empInBase || exp.getCurrency() == null || exp.getCurrency().getObjectID().equals(lfp.getBaseCurrencyID());
                    ExpenseData expData;
                    double totalExp = expInBase ? exp.getBaseTotal().doubleValue() : exp.getTotal().doubleValue();
                    if (PARTIALLY_PAID.equals(exp.getStatus().getCode())) {
                        double paid = exp.getPaidTotal(expInBase).doubleValue();
                        totalExp -= paid;
                    } else if (EXPENSE_PAID.equals(exp.getStatus().getCode())) {
                        totalExp = exp.getPaidTotalByPayslip(pItem.getObjectID(), expInBase).doubleValue();
                    }
                    if (exp.getAccount() != null) {
                        expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), totalExp, expInBase, exp.getAccount().getObjectID(), exp.getAccount().getName());
                    } else {
                        if (paidFromAccount == null) {
                            expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), totalExp, expInBase, null, "");
                        } else {
                            expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), totalExp, expInBase, paidFromAccount.getObjectID(), paidFromAccount.getName());
                        }
                    }
                    if (expData.isInBaseCurrency() && result.getExchangeRate() != null) {
                        expData.setAmount(expData.getAmount() * result.getExchangeRate().doubleValue());
                        expData.setInBaseCurrency(false);
                    }
                    expenses.add(expData);
                    amount = amount.add(BigDecimal.valueOf(expData.getAmount()));
                }
                if (expenses.size() > 0) {
                    expenses.sort((o1, o2) -> o2.getObjectID().compareTo(o1.getObjectID()));

                    PaymentDeductionObject expensePayment = new PaymentDeductionObject();
                    expensePayment.setPaymentAmount(amount);
                    expensePayment.setExpenses(expenses.toArray(new ExpenseData[]{}));
                    expensePayment.setCategoryItem(expenseCategoryItem);
                    pItem.setEmployeeExpenses(expensePayment);
                }

                if (filter.isFromExcelHandler()) {
                    if (item.getEmployee().getPosition() != null) {
                        pItem.setPosition(item.getEmployee().getPosition().getName());
                    }
                    if (item.getEmployee().getProfile() != null) {
                        pItem.setNationality(item.getEmployee().getProfile().getNationality());
                    }
                    pItem.setStartDate(item.getEmployee().getStartDate());
                    pItem.setRegularOvertimeRate(Double.valueOf(getEmployeeSettingValue(pItem.getEmployeeID(), REGULAR_OVERTIME_RATE, "0.0").replace(",", "")));
                    pItem.setWeekendOvertimeRate(Double.valueOf(getEmployeeSettingValue(pItem.getEmployeeID(), WEEKEND_OVERTIME_RATE, "0.0").replace(",", "")));
                    pItem.setHolidayOvertimeRate(Double.valueOf(getEmployeeSettingValue(pItem.getEmployeeID(), HOLIDAY_OVERTIME_RATE, "0.0").replace(",", "")));

                    pItem.setRecurringsTotal(map.getOrDefault(pItem.getObjectID(), BigDecimal.ZERO));
                    pItem.setWpsNumber(getEmployeeSettingValue(item.getEmployee().getObjectID(), WPS_NUMBER, ""));
                }
                if (item.getEmployee().getProfile() != null && item.getEmployee().getProfile().getCountry() != null) {
                    pItem.setCalculatePension(true);
                    if (company.getCountryZone() != null) {
                        pItem.setLocalEmployee(company.getCountryZone().getCountry().equals(item.getEmployee().getProfile().getCountry()));
                    }
                }
                singlePayrunItems.add(pItem);
            }
            result.setAllPaymentCategories(payments);
            result.setAllDeductionCategories(deductions);
            result.setAllTaxCategories(taxes);
            result.setAllEmployerContributionCategories(employerContributions);
            // At this moment ordering is possible only through this way
            if (isEmployeeCodeInteger) {
                singlePayrunItems.sort(Comparator.comparing(SinglePayrunItem::getSortEmployeeNumber));
            } else {
                singlePayrunItems.sort(Comparator.comparing(SinglePayrunItem::getEmployee));
            }
            result.setTableItems(singlePayrunItems.toArray(new SinglePayrunItem[]{}));
            if (filter.isFromExcelHandler()) {
                result.setCompanyWpsNumber(getCompanyPayrollSettings(WPS_NO, ""));
                result.setCompanyBankAccountCode(getCompanyBankCodeOfCompanyPayrollSettings());
            }
        } else {
            singlePayrunItems = new ArrayList<>();
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setForChanging(filter.isFromGroupTaxi());
            filterParameter.setPayrollBatchID(filter.getPayrollBatchID());
            filterParameter.setStartDate(filter.getFromDate() != null && filter.getFromDate().getNonConvertedDate() != null ? filter.getFromDate().getNonConvertedDate() : null);
            filterParameter.setEndDate(filter.getToDate() != null && filter.getToDate().getNonConvertedDate() != null ? filter.getToDate().getNonConvertedDate() : null);
            filterParameter.setSortField(filter.getSortField());
            filterParameter.setEmployeeIDs(ServerUtils.getAsCommoDelimited(filter.getAvoidEmployees(), "0", ","));
            filterParameter.setProjectId(filter.getProjectId());

            if (filter.getPayrollBatchID() != null && filter.getPayrollBatchID() > 0) {
                EdsCurrency currency = payrollBatchManager.get(filter.getPayrollBatchID()).getCurrency();
                if (currency != null)
                    result.setCurrency(currency.createCurrencyItem());
            }

            List<GroupPayrunItems> employees = payslipTableManager.getEmployeeDataForGroupPayrun(filterParameter);
            ArrayList<Integer> employeeIds = employees.stream().map(GroupPayrunItems::getId).collect(Collectors.toCollection(ArrayList::new));

            Map<String, PaymentDeductionSelectItem> categoryMap = categoryManager.getCategoryItemMapByCodes(LEAVE_DEDUCTIONS,
                    LEAVE_ENCHASHMENT,
                    BENEFIT_PAYMENT,
                    EXPENSE_REPORT,
                    REGULAR_OVERTIME,
                    WEEKEND_OVERTIME,
                    HOLIDAY_OVERTIME,
                    ADDITIONAL_PAYMENT,
                    ABSENCE_DEDUCTIONS,
                    BONUS);

            boolean isLeaveSettingsCalculationEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SICK_LEAVE_SETTINGS_CALCULATION);
            Map<Integer, Double[]> spentMinutes = Maps.newHashMapWithExpectedSize(employeeIds.size());
            Map<Integer, Integer> annualLeaveAllowanceMap = Maps.newHashMapWithExpectedSize(employeeIds.size());

            Map<String, String> settingsMap = companyPayrollSettingsManager.getCompanyPayrollSettingsMap(NON_PAID_LEAVE_DAYS_IMPACT,
                    LEAVE_DAYS_IMPACT,
                    DAILY_RATE_BY_EMPLOYER_SETTINGS,
                    ENABLED_LEAVE_DEDUCTIONS,
                    ENABLED_LEAVE_PAYMENTS,
                    NUMBER_OF_WORK_DAYS,
                    LEAVE_MONEY_TYPE_CATEGORY,
                    DEDUCT_TYPE,
                    DEDUCT_ALLOWANCES,
                    LEAVE_DAILY_PAYMENT_TYPE,
                    LEAVE_DAILY_ALLOWANCES,
                    LEAVE_MONEY_PAYMENT_TYPE,
                    LEAVE_MONEY_ALLOWANCES);
            EdsPayrollCategory leaveMTCategory = categoryManager.getCategoryByCode(LEAVE_SALARY);
            PaymentDeductionSelectItem leaveMTCategoryItem = leaveMTCategory != null ? leaveMTCategory.createPaymentDeductionSelectItem() : null;

            List<PaymentDeductionObject> leaveDeductionLinkedCategories = loadLeaveSettings(settingsMap.get(DEDUCT_ALLOWANCES));
            List<PaymentDeductionObject> leaveDailyTypeLinkedCategories = loadLeaveSettings(settingsMap.get(LEAVE_DAILY_ALLOWANCES));
            List<PaymentDeductionObject> leaveMoneyTypeLinkedCategories = loadLeaveSettings(settingsMap.get(LEAVE_MONEY_ALLOWANCES));

            if (isLeaveSettingsCalculationEnabled && filter.getYear() != null && filter.getMonth() != null) {

                Integer approvedStatusId = referenceManager.findReferenceId(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED);

                annualLeaveAllowanceMap.putAll(annualLeaveAllowanceManager.getLastYearMinutesMapByYearAndReasonAndEmployee(filter.getYear(),
                        EdsSickRequest.LR_TYPE_ANNUAL_LEAVE,
                        employeeIds));
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setYear(filter.getYear());
                fp.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
                fp.setStatusID(approvedStatusId);
                fp.setAnnualLeave(true);
                fp.setObjectIDs(employeeIds);

                spentMinutes.putAll(sickRequestDurationManager.getAllowanceSpentByEmployees(fp));
            }

            PayslipFilter payslipFilter = new PayslipFilter();
            payslipFilter.setFromDate(filter.getFromDate());
            payslipFilter.setToDate(filter.getToDate());
            Multimap<Integer, PaymentDeductionObject> paymentDeductionsMap = paymentDeductionManager.getEmployeesPaymentDeductionMap(employeeIds, payslipFilter);
            String[] settingsKeys = {RATE_TYPE,
                    SALARY,
                    REGULAR_OVERTIME_RATE,
                    REGULAR_OVERTIME_RATE_TYPE,
                    WEEKEND_OVERTIME_RATE,
                    WEEKEND_OVERTIME_RATE_TYPE,
                    HOLIDAY_OVERTIME_RATE,
                    HOLIDAY_OVERTIME_RATE_TYPE,
                    PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS,
                    PayrollConstants.MATERIAL_AID_TYPE_FUNERAL,
                    PayrollConstants.MATERIAL_AID_TYPE_GIFT
            };
            Table<Integer, String, String> employeeSettingsMap = employeePayrollSettingsManager.getEmployeesPayrollSettingMap(employeeIds, settingsKeys);

            ListingFilterParameter lp = new ListingFilterParameter();
            lp.setObjectIDs(employeeIds);
            lp.setStartDate(filter.getFromDate().getNonConvertedDate());
            lp.setEndDate(filter.getToDate().getNonConvertedDate());
            HashMap<Integer, List<SalaryHistory>> salaryHistoryMap = salaryHistoryManager.getEmployeeSalaryHistoryMap(lp);

            Integer baseCurrencyId = baseCurrency != null ? baseCurrency.getObjectID() : null;
            boolean hasCountry = company != null && company.getCountryZone() != null && company.getCountryZone().getCountry() != null;
            Integer countryId = hasCountry ? company.getCountryZone().getCountry().getObjectID() : null;
            String countryCode = hasCountry ? company.getCountryZone().getCountry().getCode() : "";

            //Loop through all employees in group
            for (GroupPayrunItems emp : employees) {
                Integer employeeID = emp.getId();
                validateList = payslipTableItemManager.getPayedMonthList(null, emp.getId());
                if (filter != null && filter.getPeriodChecker() != null && validateList != null && validateList.size() > 0) {
                    if (validateList.contains(filter.getPeriodChecker())) {
                        continue;
                    }
                }
                PayslipItemFilter itemFilter = PayslipItemFilter.fromPayslipFilter(filter);
                itemFilter.setEmployeeID(employeeID);
                itemFilter.setEmployeeCodeInteger(isEmployeeCodeInteger);

                itemFilter.setBaseCurrencyId(baseCurrencyId);
                itemFilter.setCountryId(countryId);
                itemFilter.setCountryCode(countryCode);

                itemFilter.setSpentMinutes(spentMinutes.get(employeeID));
                itemFilter.setCategoryMap(categoryMap);
                itemFilter.setCompanyPayrollSettingsMap(settingsMap);

                itemFilter.setLeaveSettingsCalculationEnabled(isLeaveSettingsCalculationEnabled);
                itemFilter.setLeaveMTCategoryItem(leaveMTCategoryItem);
                itemFilter.setLeaveDailyTypeLinkedCategories(leaveDailyTypeLinkedCategories);
                itemFilter.setLeaveDeductionLinkedCategories(leaveDeductionLinkedCategories);
                itemFilter.setLeaveMoneyTypeLinkedCategories(leaveMoneyTypeLinkedCategories);
                itemFilter.setSalaryHistories(salaryHistoryMap.get(employeeID));
                itemFilter.setEmployeeSettingsMap(employeeSettingsMap.row(employeeID));
                itemFilter.setLastYearMinutes(annualLeaveAllowanceMap.get(employeeID));
                itemFilter.setPaymentDeductions(((ArrayListMultimap<Integer, PaymentDeductionObject>) paymentDeductionsMap).get(employeeID));

                SinglePayrunItem item = generateSinglePayrun(itemFilter);
                singlePayrunItems.add(item);
            }

            if (isEmployeeCodeInteger) {
                singlePayrunItems.sort(Comparator.comparing(SinglePayrunItem::getSortEmployeeNumber));
            }
            result.setTableItems(singlePayrunItems.toArray(new SinglePayrunItem[]{}));
        }

        EdsPensionScheme pensionScheme = pensionSchemeManager.getPensionSchema(company.getCountryZone() != null && company.getCountryZone().getCountry() != null ? company.getCountryZone().getCountry().getCode() : "");
        if (pensionScheme != null) {
            result.setPensionType(pensionScheme.getDeductionType());
            result.setCompanyPensionType(pensionScheme.getEmployerDeductionType());
            result.setPensionValue(pensionScheme.getDeductionValue());
            result.setNonLocalPensionValue(pensionScheme.getNonLocalDeductionValue());
            result.setCompanyPensionValue(pensionScheme.getEmployerDeductionValue());
            result.setCompanyNonLocalPensionValue(pensionScheme.getEmployerNonLocalDeductionValue());
            result.setPensionValueType(pensionScheme.getDeductFrom());
            result.setEmpMaxTaxableAmount(pensionScheme.getEmpMaxTaxableAmount());
            result.setCompMaxtaxableAmount(pensionScheme.getCompMaxTaxableAmount());
            if (pensionScheme.getCategories() != null && pensionScheme.getCategories().size() > 0) {
                for (EdsPayrollCategory category : pensionScheme.getCategories()) {
                    result.getPensionAllowances().add(category.createPaymentDeductionSelectItem());
                }
            }
        }

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        if (fs != null && fs.getCurrency() != null) {
            result.setCurrencyName(fs.getCurrency().getName());
        }
        result.setAtsCustomization(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ATS_PAYROLL_CUSTOMIZATION));

        return result;
    }

    private void calculateDataForCustomAnnualLeaveCustomisation(Integer employeeId,
                                                                ListingFilterParameter lfp,
                                                                List<PaymentDeductionObject> paymentCategories,
                                                                SinglePayrunItem item,
                                                                BigDecimal leavePaymentsTotal,
                                                                Double[] spentMinutes,
                                                                Integer lastYearMinutes,
                                                                Integer month,
                                                                Integer year) {
        EdsSickLeaveSettings sklSettings = sickLeaveSettingsManager.getOne();
        BigDecimal leaveAllowances = BigDecimal.ZERO;
        EdsDailyRateSettings edrs = dailyRateSettingsManager.getOne();
        Integer daysCount = null;
        BigDecimal monthCount = BigDecimal.ONE;
        EdsEmployee employee = employeeManager.get(employeeId);
        DailyRateSettings dayrs = edrs != null ? edrs.getRPC() : null;
        if (sklSettings != null && dayrs != null && employee.getStartDate() != null) {
            PaymentDeductionObject leavePayment = new PaymentDeductionObject();
            leavePayment.setCategoryItem(sklSettings.getSickLeaveCategory().createPaymentDeductionSelectItem());
            for (EdsPayrollCategory category : sklSettings.getCategories()) {
                PaymentDeductionObject pdo = new PaymentDeductionObject();
                pdo.setCategoryItem(category.createPaymentDeductionSelectItem());
                leavePayment.getLinkedCategories().add(pdo);
            }
            BigDecimal leaveCalcPercent = BigDecimal.ZERO;
            ListingFilterParameter sickLeaveFilter = getNonPaidLeaveFilter(lfp);
            List<EdsSickRequest> yearSickRequests = sickRequestManager.getNonPaidLeaveRequests(sickLeaveFilter);
            for (EdsSickRequest sickRequest : yearSickRequests) {
                int leaveDays = new BigDecimal(availabilityServiceLocal.getLeaveRequestStats(sickRequest)[0]).intValue();
                int fullPaidDays = 0;
                int halfPaidDays = 0;
                if (getMonthsDifference(employee.getStartDate(), sickRequest.getStartDate()) >= sklSettings.getMinPeriodOfService()) {
                    if (leaveDays > sklSettings.getFullyPaidLeaveDays() && leaveDays <= sklSettings.getHalfPaidLeaveDays()) {
                        leaveCalcPercent = new BigDecimal("0.5");
                        halfPaidDays = leaveDays - sklSettings.getFullyPaidLeaveDays();
                    }
                    if (leaveDays > 0 && halfPaidDays <= sklSettings.getFullyPaidLeaveDays()) {
                        fullPaidDays = leaveDays - halfPaidDays;
                    }
                }
                if (DailyRateSettings.TYPE_CALENDAR.equals(dayrs.getDailyRateType())) {
                    daysCount = ServerUtils.getMonthDaysCountInYear(sickRequest.getStartDate().getMonth(), sickRequest.getStartDate().getYear());
                } else if (DailyRateSettings.TYPE_FORMULA.equals(dayrs.getDailyRateType())) {
                    daysCount = attendanceRawDataManager.getWorkingDays(sickLeaveFilter, AttendanceRawDataManagerImpl.WORKING_DATES).size();
                    monthCount = new BigDecimal("12.0");
                } else if (DailyRateSettings.TYPE_EMPLOYER_SETTINGS.equals(dayrs.getDailyRateType())) {
                    daysCount = edrs.getWorkDaysInMonth();
                }
                if (daysCount != null) {
                    leaveAllowances = getAllowanceTotal(leavePayment.getLinkedCategories(), paymentCategories);
                    BigDecimal fullPaymentAmount = item.getBasicSalary().add(leaveAllowances).multiply(new BigDecimal(fullPaidDays)).multiply(monthCount).divide(new BigDecimal(daysCount), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    BigDecimal halfPaymentAmount = item.getBasicSalary().add(leaveAllowances).multiply(new BigDecimal(leaveDays)).multiply(leaveCalcPercent).multiply(monthCount).divide(new BigDecimal(daysCount), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    BigDecimal totalPayment = fullPaymentAmount.add(halfPaymentAmount);
                    if (totalPayment != null && totalPayment.compareTo(BigDecimal.ZERO) != 0) {
                        leavePayment.getSickRequestids().add(sickRequest.getObjectID());
                    }
                    if (leavePayment.getPaymentAmount() != null) {
                        leavePayment.setPaymentAmount(leavePayment.getPaymentAmount().add(totalPayment));
                    } else {
                        leavePayment.setPaymentAmount(totalPayment);
                    }
                }
            }
            if (leavePayment.getPaymentAmount() != null && leavePayment.getPaymentAmount().compareTo(BigDecimal.ZERO) != 0) {
                paymentCategories.add(leavePayment);
                leavePaymentsTotal = leavePaymentsTotal.add(leavePayment.getPaymentAmount());
            }
        }
        int lastYearLeftMinutes = 0;
        if (spentMinutes != null && lastYearMinutes != null) {
            double lastSpent = spentMinutes != null && spentMinutes[3] != null ? spentMinutes[3] : 0.0;
            lastYearLeftMinutes = lastYearMinutes - (int) lastSpent;
        }
        EdsPayrollCategory prevAllowanceCategory = categoryManager.getCategoryByCode(SICK_LEAVE_PAYMENT);
        PaymentDeductionSelectItem prevAllowanceCategItem = prevAllowanceCategory != null ? prevAllowanceCategory.createPaymentDeductionSelectItem() : null;

        LRSettingsItem lrSettingsItem = profileService.getLrSettingsItem();
        boolean toPay = lrSettingsItem != null && lrSettingsItem.getPayremainingallowance() && lrSettingsItem.getUsageDeadline() != null && new Date(lrSettingsItem.getUsageDeadline()).getMonth() > month;
        if (dayrs != null && toPay && lastYearLeftMinutes > 0 && prevAllowanceCategItem != null) {
            PaymentDeductionObject prevLeavePayment = new PaymentDeductionObject();
            prevLeavePayment.setCategoryItem(prevAllowanceCategItem);
            if (DailyRateSettings.TYPE_CALENDAR.equals(dayrs.getDailyRateType())) {
                daysCount = CalendarUtil.getMonthDaysCount(month, year);
            } else if (DailyRateSettings.TYPE_FORMULA.equals(dayrs.getDailyRateType())) {
                ListingFilterParameter leaveFilter = getNonPaidLeaveFilter(lfp);
                leaveFilter.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
                daysCount = attendanceRawDataManager.getWorkingDays(leaveFilter, AttendanceRawDataManagerImpl.WORKING_DATES).size();
                monthCount = new BigDecimal("12.0");
            } else if (DailyRateSettings.TYPE_EMPLOYER_SETTINGS.equals(dayrs.getDailyRateType())) {
                daysCount = edrs.getWorkDaysInMonth();
            }
            Integer averageMinutesForDay = ServerUtils.getDailyAverageTimeslotMinutes(employee.getTimeSlot().getItems());

            int days = new BigDecimal(lastYearLeftMinutes).intValue() / averageMinutesForDay;
            BigDecimal annualLeavePaymentAmount = item.getBasicSalary().add(leaveAllowances).multiply(new BigDecimal(days)).multiply(monthCount).divide(new BigDecimal(daysCount), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            if (annualLeavePaymentAmount.compareTo(BigDecimal.ZERO) > 0) {
                prevLeavePayment.setPaymentAmount(annualLeavePaymentAmount);
                leavePaymentsTotal = leavePaymentsTotal.add(annualLeavePaymentAmount);
                LeavePaymentItem leaveItem = new LeavePaymentItem(year, days);
                leaveItem.setLeaveMinutes(new BigDecimal(lastYearLeftMinutes).intValue());
                prevLeavePayment.setLeavePaymentItem(leaveItem);
                paymentCategories.add(prevLeavePayment);
            }
        }
    }

    private BigDecimal loadRecurringPaymentObjectData(BigDecimal salary, BigDecimal numberOfWorkDays, PaymentDeductionObject object, List<MonthlyOvertimeData> overtimeData, BigDecimal allowanceRatio) {
        BigDecimal payAmount = object.getPaymentAmount();

        if (object.getCategoryItem().getCode().equals(HOUSING_ALLOWANCE) && overtimeData != null && !overtimeData.isEmpty()) {
            BigDecimal housingTotal = BigDecimal.ZERO;
            BigDecimal dailyRate = BigDecimal.ZERO;
            try {
                dailyRate = payAmount.divide(numberOfWorkDays, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            } catch (ArithmeticException ignored) {
            }

            for (MonthlyOvertimeData data : overtimeData) {
                if (data.getAccomodationPayDays() != null && data.getAccomodationPayDays().compareTo(BigDecimal.ZERO) > 0) {
                    housingTotal = housingTotal.add(dailyRate.multiply(data.getAccomodationPayDays()));
                }
            }
            object.setPaymentAmount(housingTotal);
        } else if (object.getCategoryItem().getCode().equals(FOOD_ALLOWANCE) && overtimeData != null && !overtimeData.isEmpty()) {
            BigDecimal foodTotal = BigDecimal.ZERO;
            BigDecimal dailyRate = BigDecimal.ZERO;
            try {
                dailyRate = payAmount.divide(numberOfWorkDays, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            } catch (ArithmeticException ignored) {
            }

            for (MonthlyOvertimeData data : overtimeData) {
                if (data.getAccomodationPayDays() != null && data.getAccomodationPayDays().compareTo(BigDecimal.ZERO) > 0) {
                    foodTotal = foodTotal.add(dailyRate.multiply(data.getFoodPayDays()));
                }
            }
            object.setPaymentAmount(foodTotal);
        } else if (object.getPaymentAmount() != null && !object.isAdditionalPayment()) {
            object.setPaymentAmount(object.getPaymentAmount().multiply(allowanceRatio));
        }
        if (object.getPaymentAmount() == null && object.getPercentage() != null) {
            BigDecimal paymentAmount = salary.multiply(object.getPercentage());
            object.setAmount(paymentAmount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
            object.setPaymentAmount(paymentAmount.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
        }
        return object.getCategoryItem() != null && object.getCategoryItem().isNonMoneyType() ? BigDecimal.ZERO : object.getPaymentAmount();
    }

    private boolean loadLoanData(PaymentDeductionObject object) {
        BigDecimal payTotal = payslipPaymentsManager.getPayedAmountByCategory(object.getId());
        if (object.getTotalAmount() != null && payTotal != null) {
            object.setRemainingAmount(object.getTotalAmount().subtract(payTotal));
            if (BigDecimal.ZERO.compareTo(object.getRemainingAmount()) >= 0) {
                object.setPaymentAmount(BigDecimal.ZERO);
                return true;
            } else if (object.getPaymentAmount().compareTo(object.getRemainingAmount()) > 0) {
                object.setPaymentAmount(object.getRemainingAmount());
            }
        }
        return false;
    }

    private PaymentDeductionObject getFixedHrmsOTPayment(PaymentDeductionSelectItem categoryItem, BigDecimal numberOfWorkDay, BigDecimal salary, BigDecimal hours, String rate, String rateType) {
        PaymentDeductionObject overtimePayment = null;
        if (categoryItem != null && rate != null && !rate.isEmpty()) {
            overtimePayment = new PaymentDeductionObject();
            overtimePayment.setCategoryItem(categoryItem);
            BigDecimal overtimeRate = new BigDecimal(rate.replace(",", ""));
            if (PERCENTAGE.equals(rateType)) {
                overtimeRate = overtimeRate.multiply(salary).divide(numberOfWorkDay.multiply(BigDecimal.valueOf(8.00 * 100.00)), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            }
            overtimePayment.setPaymentAmount(overtimeRate.multiply(hours));
        }
        return overtimePayment != null && overtimePayment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0 ? overtimePayment : null;
    }

    private PaymentDeductionObject calculateAbsenceOrOvertimeByTimeslot(PaymentDeductionSelectItem categoryItem, List<PaymentDeductionObject> paymentCategories, BigDecimal salary, BigDecimal absenceHours, BigDecimal timesllotHours, String relativeRate, String rateType) {
        List<PaymentDeductionObject> leaveDailyTypeLinkedCategories = new ArrayList<>();
        Integer calculationtype = loadLeaveSettings(TIMESHEET_HOURS_CALCUTATION_TYPE, TIMESHEET_HOURS_ALLOWANCES, leaveDailyTypeLinkedCategories);
        if (leaveDailyTypeLinkedCategories.size() > 0 || calculationtype.equals(2)) {
            salary = salary.add(getTotalPayments(leaveDailyTypeLinkedCategories, paymentCategories, calculationtype));
        }

        PaymentDeductionObject overtimePayment = null;
        if (categoryItem != null) {
            overtimePayment = new PaymentDeductionObject();
            overtimePayment.setCategoryItem(categoryItem);
            BigDecimal rate = BigDecimal.ONE;
            if (StringUtils.isNotEmpty(relativeRate)) {
                rate = new BigDecimal(relativeRate.replace(",", ""));
            }
            if (PERCENTAGE.equals(rateType) && !Objects.equals(BigDecimal.ZERO, timesllotHours)) {
                overtimePayment.setPaymentAmount(rate.multiply(salary).multiply(absenceHours).divide(timesllotHours.multiply(new BigDecimal(100)), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
            } else {
                overtimePayment.setPaymentAmount(rate.multiply(absenceHours));
            }
        }
        return overtimePayment != null && overtimePayment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0 ? overtimePayment : null;
    }

    private PaymentDeductionObject getFixedOTPayment(SinglePayrunItem item, BigDecimal numberOfWorkDay, PaymentDeductionSelectItem categoryItem, String rate, String rateType, List<MonthlyOvertimeData> overtimeData, String overtimeType) {
        PaymentDeductionObject overtimePayment = null;
        if (categoryItem != null && rate != null && !rate.isEmpty() && !overtimeData.isEmpty()) {
            overtimePayment = new PaymentDeductionObject();
            overtimePayment.setCategoryItem(categoryItem);

            BigDecimal overtimeTotal = BigDecimal.ZERO;
            for (MonthlyOvertimeData data : overtimeData) {
                BigDecimal overtimeRate = new BigDecimal(rate.replace(",", ""));

                BigDecimal basSalary = item.getBasicSalary();
                if (data.getPositionSalary() != null && data.getPositionSalary().compareTo(item.getBasicSalary()) > 0) {
                    basSalary = data.getPositionSalary();
                }

                if (PERCENTAGE.equals(rateType)) {
                    overtimeRate = overtimeRate.multiply(basSalary).divide(numberOfWorkDay.multiply(BigDecimal.valueOf(8.00 * 100)), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                }

                if (REGULAR_OVERTIME.equals(overtimeType)) {
                    overtimeTotal = overtimeTotal.add(overtimeRate.multiply(data.getRegularOvertimeHours()));
                } else if (WEEKEND_OVERTIME.equals(overtimeType)) {
                    overtimeTotal = overtimeTotal.add(overtimeRate.multiply(data.getWeeklyOvertimeHours()));
                } else if (HOLIDAY_OVERTIME.equals(overtimeType)) {
                    overtimeTotal = overtimeTotal.add(overtimeRate.multiply(data.getHolidayOvertimeHours()));
                }
            }

            overtimePayment.setPaymentAmount(overtimeTotal);
        }
        return overtimePayment != null && overtimePayment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0 ? overtimePayment : null;
    }

    private PaymentDeductionObject getAdditionalPaymentData(SinglePayrunItem item, List<MonthlyOvertimeData> overtimeData, PaymentDeductionSelectItem additionalPaymentCategoryItem) {
        PaymentDeductionObject salaryDifferencePayment = null;
        BigDecimal basicSalary = item.getBasicSalary();
        if (additionalPaymentCategoryItem != null && overtimeData != null && !overtimeData.isEmpty() /*&&
                overtimeData.getPositionSalary() != null && overtimeData.getPositionSalary().compareTo(basicSalary) > 0*/) {
            salaryDifferencePayment = new PaymentDeductionObject();
            salaryDifferencePayment.setCategoryItem(additionalPaymentCategoryItem);

            BigDecimal totalAdditional = BigDecimal.ZERO;
            for (MonthlyOvertimeData data : overtimeData) {
                if (data.getPositionSalary() != null && data.getPositionSalary().compareTo(basicSalary) > 0) {
                    BigDecimal dailyRate = data.getPositionSalary().subtract(basicSalary).divide(item.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    totalAdditional = totalAdditional.add(dailyRate.multiply(data.getTotalWorkedDays()));
                }
            }

            //BigDecimal dailyRate = overtimeData.getPositionSalary().subtract(basicSalary).divide(item.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            salaryDifferencePayment.setPaymentAmount(totalAdditional);
        }
        return salaryDifferencePayment != null && salaryDifferencePayment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0 ? salaryDifferencePayment : null;
    }

    private PaymentDeductionObject getExpensePaymentData(ListingFilterParameter lfp, PaymentDeductionSelectItem expenseCategoryItem) {
        BigDecimal amount = BigDecimal.ZERO;
        PaymentDeductionObject expensePayment = null;
        List<ExpenseData> expenses = new LinkedList<>();
        List<EdsExpenseReport> unpaidExpenses = getUnpaidExpenseReports(lfp);
        String expensePaidFromAccount = getCompanyPayrollSettings(EXPENSE_PAID_ACCOUNT, "");
        boolean empInBase = lfp.getCurrencyID() == null || lfp.getCurrencyID().equals(lfp.getBaseCurrencyID());
        boolean expInBase;
        EdsAccount paidFromAccount = !expensePaidFromAccount.isEmpty() ? accountingManager.get(Integer.valueOf(expensePaidFromAccount)) : null;
        if (lfp.getObjectId() != null) {
            List<EdsExpenseReport> linkedExpenses = expenseReportManager.getPayslipTableItemRelatedExpenseClaims(lfp.getObjectId());
            for (EdsExpenseReport exp : linkedExpenses) {
                expInBase = empInBase || exp.getCurrency() == null || exp.getCurrency().getObjectID().equals(lfp.getBaseCurrencyID());
                double total = expInBase ? exp.getBaseTotal().doubleValue() : exp.getTotal().doubleValue();
                double paid = exp.getPaidTotal(expInBase).doubleValue();
                total -= paid;
                if (exp.getAccount() != null) {
                    expenses.add(new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, exp.getAccount().getObjectID(), exp.getAccount().getName()));
                } else {
                    if (paidFromAccount == null) {
                        expenses.add(new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, null, ""));
                    } else {
                        expenses.add(new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, paidFromAccount.getObjectID(), paidFromAccount.getName()));
                    }
                }
                amount = amount.add(exp.getBaseTotal() != null ? exp.getBaseTotal() : BigDecimal.ZERO);
            }
        }
        for (EdsExpenseReport exp : unpaidExpenses) {
            expInBase = exp.getCurrency() == null || exp.getCurrency().getObjectID().equals(lfp.getBaseCurrencyID());
            double total = !expInBase && "true".equals(this.getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL)) ? exp.getTotal().doubleValue() : exp.getBaseTotal().doubleValue();
            double paid = exp.getPaidTotal(expInBase).doubleValue();
            total -= paid;
            if (exp.getAccount() != null) {
                expenses.add(new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, exp.getAccount().getObjectID(), exp.getAccount().getName()));
            } else {
                if (paidFromAccount == null) {
                    expenses.add(new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, null, ""));
                } else {
                    expenses.add(new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, paidFromAccount.getObjectID(), paidFromAccount.getName()));
                }
            }
            amount = amount.add(BigDecimal.valueOf(total));
        }
        if (expenses.size() > 0) {
            expenses.sort(Comparator.comparingInt(ExpenseData::getObjectID).reversed());

            expensePayment = new PaymentDeductionObject();
            expensePayment.setCategoryItem(expenseCategoryItem);
            expensePayment.setPaymentAmount(amount);
            expensePayment.setExpenses(expenses.toArray(new ExpenseData[]{}));
        }
        return expensePayment;
    }

    private BigDecimal getAllowanceTotal(List<PaymentDeductionObject> linkedCategories, List<PaymentDeductionObject> paymentCategories) {
        BigDecimal result = BigDecimal.ZERO;
        for (PaymentDeductionObject paymentObject : paymentCategories) {
            for (PaymentDeductionObject item : linkedCategories) {
                if (item.getCategoryItem().getId().equals(paymentObject.getCategoryItem().getId()) && paymentObject.getPaymentAmount() != null) {
                    result = result.add(paymentObject.getPaymentAmount());
                    break;
                }
            }
        }
        return result;
    }

    private BigDecimal getTotalPayments(List<PaymentDeductionObject> linkedCategories, List<PaymentDeductionObject> paymentCategories, Integer calculationtype) {
        BigDecimal result = BigDecimal.ZERO;
        for (PaymentDeductionObject paymentObject : paymentCategories) {
            if (calculationtype.equals(2)) {
                result = result.add(paymentObject.getPaymentAmount() != null ? paymentObject.getPaymentAmount() : BigDecimal.ZERO);
            } else {
                for (PaymentDeductionObject item : linkedCategories) {
                    if (item.getCategoryItem().getId().equals(paymentObject.getCategoryItem().getId())) {
                        result = result.add(paymentObject.getPaymentAmount());
                        break;
                    }
                }
            }
        }
        return result;
    }

    private void registerEmployeeExpenses(PaymentDeductionObject employeeExpenses, EdsPayslipTableItem payslipTableItem) {
        EdsExpenseReport expenseReport;
        expenseReportManager.updateExpensesByPayslipTableID(payslipTableItem.getObjectID());
        if (employeeExpenses != null && employeeExpenses.getExpenses() != null) {
            employeeExpenses.getExpenses();
            for (ExpenseData exp : employeeExpenses.getExpenses()) {
                expenseReport = expenseReportManager.get(exp.getObjectID());
                if (expenseReport != null) {
                    expenseReport.setPayslipTableItemID(payslipTableItem.getObjectID());
                    expenseReport.setPaymentType(exp.getPaymentType());
                    expenseReport.setAccount(accountingManager.get(exp.getAccountID()));
                    expenseReport.setLastUpdateTime(new Date());
                }
            }
        }
    }

    private void registerPaymentDeductionCategories(SinglePayrunItem item, EdsPayslipTableItem payslipTableItem) {
        EdsPayslipPayments payslipPayment;
        EdsPaymentDeduction newPaymentOrDeduction;
        List<EdsPayslipPayments> paymentsForDelete = payslipPaymentsManager.getByPayslipItemID(payslipTableItem.getObjectID());
//        payslipPaymentsManager.deleteByPayslipItemID(payslipTableItem.getObjectID());
        for (PaymentDeductionObject paymentObject : item.getPaymentCategories()) {
            if (paymentObject.getId() != null) {
                payslipPayment = payslipPaymentsManager.getPayslipPayment(paymentObject.getId(), payslipTableItem.getObjectID());
                if (payslipPayment == null) {
                    payslipPayment = new EdsPayslipPayments();
                }
                payslipPayment.setPaymentDeductionID(paymentObject.getId());
                payslipPayment.setPaymentTotal(paymentObject.getPaymentAmount());
                newPaymentOrDeduction = paymentDeductionManager.get(paymentObject.getId());
                newPaymentOrDeduction.setRemarks(paymentObject.getRemarks());
                paymentDeductionManager.createOrUpdate(newPaymentOrDeduction);
            } else {
                newPaymentOrDeduction = new EdsPaymentDeduction();
                newPaymentOrDeduction.setCategory(categoryManager.get(paymentObject.getCategoryItem().getId()));
                newPaymentOrDeduction.setEmployee(payslipTableItem.getEmployee());
                newPaymentOrDeduction.setPaymentAmount(paymentObject.getPaymentAmount());
                newPaymentOrDeduction.setSalaryObject(paymentObject.isSalaryObject());
                if (paymentObject.getLeaveDaysCount() != null) {
                    newPaymentOrDeduction.setLeaveDaysCount(paymentObject.getLeaveDaysCount());
                }
                newPaymentOrDeduction.setRemarks(paymentObject.getRemarks());
                if (paymentObject.getStarttDate() != null) {
                    newPaymentOrDeduction.setStartDate(paymentObject.getStarttDate().getNonConvertedDate());
                }
                if (paymentObject.getEnddDate() != null) {
                    newPaymentOrDeduction.setEndDate(paymentObject.getEnddDate().getNonConvertedDate());
                }
                paymentDeductionManager.create(newPaymentOrDeduction);
                payslipPayment = new EdsPayslipPayments();
                payslipPayment.setPaymentDeductionID(newPaymentOrDeduction.getObjectID());
                payslipPayment.setPaymentTotal(paymentObject.getPaymentAmount());
            }
            payslipPayment.setPayslipItemID(payslipTableItem.getObjectID());
            payslipPayment.setForwardedPayment(paymentObject.getCategoryItem().getCode().contains(REMAINING_PREV_MONTH));
            payslipPaymentsManager.createOrUpdate(payslipPayment);
            paymentsForDelete.remove(payslipPayment);
            if (paymentObject.getSickRequestids() != null && paymentObject.getSickRequestids().size() > 0) {
                for (Integer sickReqId : paymentObject.getSickRequestids()) {
                    EdsSickRequest sickRequest = sickRequestManager.get(sickReqId);
                    sickRequest.setPaymentDeduction(newPaymentOrDeduction);
                    sickRequestManager.update(sickRequest);
                }
            }
            if (paymentObject.getLeavePaymentItem() != null) {
                newPaymentOrDeduction.setLeaveMinutes(paymentObject.getLeavePaymentItem().getLeaveMinutes());
                newPaymentOrDeduction.setLeavePaymentYear(paymentObject.getLeavePaymentItem().getLeavePaymentYear());
                newPaymentOrDeduction.setLeaveDays(paymentObject.getLeavePaymentItem().getLeaveDays());
            }
        }

        for (PaymentDeductionObject deductionObject : item.getDeductionCategories()) {
            if (deductionObject.getId() != null) {
                payslipPayment = payslipPaymentsManager.getPayslipPayment(deductionObject.getId(), payslipTableItem.getObjectID());
                if (payslipPayment == null) {
                    payslipPayment = new EdsPayslipPayments();
                }
                payslipPayment.setPaymentDeductionID(deductionObject.getId());
                payslipPayment.setPaymentTotal(deductionObject.getPaymentAmount());
                payslipPayment.setPayslipItemID(payslipTableItem.getObjectID());
                payslipPayment.setForwardedPayment(deductionObject.getCategoryItem().getCode().contains(REMAINING_PREV_MONTH));
                payslipPaymentsManager.createOrUpdate(payslipPayment);
                paymentsForDelete.remove(payslipPayment);

                newPaymentOrDeduction = paymentDeductionManager.get(deductionObject.getId());
                newPaymentOrDeduction.setRemarks(deductionObject.getRemarks());
                if (newPaymentOrDeduction.getCashAdvanceID() != null) {
                    EdsCashAdvance cashAdvance = cashAdvanceManager.get(newPaymentOrDeduction.getCashAdvanceID());
                    checkCashAdvanceForFullyPaid(deductionObject.getId(), cashAdvance.getObjectID());
                }
                paymentDeductionManager.createOrUpdate(newPaymentOrDeduction);
            } else {
                newPaymentOrDeduction = new EdsPaymentDeduction();
                newPaymentOrDeduction.setCategory(categoryManager.get(deductionObject.getCategoryItem().getId()));
                newPaymentOrDeduction.setEmployee(payslipTableItem.getEmployee());
                newPaymentOrDeduction.setPaymentAmount(deductionObject.getPaymentAmount());
                if (deductionObject.getLeaveDaysCount() != null) {
                    newPaymentOrDeduction.setLeaveDaysCount(deductionObject.getLeaveDaysCount());
                }
                newPaymentOrDeduction.setRemarks(deductionObject.getRemarks());
                paymentDeductionManager.create(newPaymentOrDeduction);
                payslipPayment = new EdsPayslipPayments();
                payslipPayment.setPaymentDeductionID(newPaymentOrDeduction.getObjectID());
                payslipPayment.setPaymentTotal(deductionObject.getPaymentAmount());
                payslipPayment.setPayslipItemID(payslipTableItem.getObjectID());
                payslipPayment.setForwardedPayment(deductionObject.getCategoryItem().getCode().contains(REMAINING_PREV_MONTH));
                payslipPaymentsManager.createOrUpdate(payslipPayment);
                paymentsForDelete.remove(payslipPayment);
            }
        }

        for (PaymentDeductionObject deductionObject : item.getTaxCategories()) {
            if (deductionObject.getId() != null) {
                payslipPayment = payslipPaymentsManager.getPayslipPayment(deductionObject.getId(), payslipTableItem.getObjectID());
                if (payslipPayment == null) {
                    payslipPayment = new EdsPayslipPayments();
                }
                payslipPayment.setPaymentDeductionID(deductionObject.getId());
                payslipPayment.setPaymentTotal(deductionObject.getPaymentAmount());
                payslipPayment.setPayslipItemID(payslipTableItem.getObjectID());
                payslipPayment.setForwardedPayment(deductionObject.getCategoryItem().getCode().contains(REMAINING_PREV_MONTH));
                payslipPaymentsManager.createOrUpdate(payslipPayment);
                paymentsForDelete.remove(payslipPayment);

                newPaymentOrDeduction = paymentDeductionManager.get(deductionObject.getId());
                newPaymentOrDeduction.setRemarks(deductionObject.getRemarks());
                if (newPaymentOrDeduction.getCashAdvanceID() != null) {
                    EdsCashAdvance cashAdvance = cashAdvanceManager.get(newPaymentOrDeduction.getCashAdvanceID());
                    checkCashAdvanceForFullyPaid(deductionObject.getId(), cashAdvance.getObjectID());
                }
                paymentDeductionManager.createOrUpdate(newPaymentOrDeduction);
            } else {
                newPaymentOrDeduction = new EdsPaymentDeduction();
                newPaymentOrDeduction.setCategory(categoryManager.get(deductionObject.getCategoryItem().getId()));
                newPaymentOrDeduction.setEmployee(payslipTableItem.getEmployee());
                newPaymentOrDeduction.setPaymentAmount(deductionObject.getPaymentAmount());
                if (deductionObject.getLeaveDaysCount() != null) {
                    newPaymentOrDeduction.setLeaveDaysCount(deductionObject.getLeaveDaysCount());
                }
                newPaymentOrDeduction.setRemarks(deductionObject.getRemarks());
                paymentDeductionManager.create(newPaymentOrDeduction);
                payslipPayment = new EdsPayslipPayments();
                payslipPayment.setPaymentDeductionID(newPaymentOrDeduction.getObjectID());
                payslipPayment.setPaymentTotal(deductionObject.getPaymentAmount());
                payslipPayment.setPayslipItemID(payslipTableItem.getObjectID());
                payslipPayment.setForwardedPayment(deductionObject.getCategoryItem().getCode().contains(REMAINING_PREV_MONTH));
                payslipPaymentsManager.createOrUpdate(payslipPayment);
                paymentsForDelete.remove(payslipPayment);
            }
        }

        for (PaymentDeductionObject deductionObject : item.getEmployerContributionCategories()) {
            if (deductionObject.getId() != null) {
                payslipPayment = payslipPaymentsManager.getPayslipPayment(deductionObject.getId(), payslipTableItem.getObjectID());
                if (payslipPayment == null) {
                    payslipPayment = new EdsPayslipPayments();
                }
                payslipPayment.setPaymentDeductionID(deductionObject.getId());
                payslipPayment.setPaymentTotal(deductionObject.getPaymentAmount());
                payslipPayment.setPayslipItemID(payslipTableItem.getObjectID());
                payslipPayment.setForwardedPayment(deductionObject.getCategoryItem().getCode().contains(REMAINING_PREV_MONTH));
                payslipPaymentsManager.createOrUpdate(payslipPayment);
                paymentsForDelete.remove(payslipPayment);

                newPaymentOrDeduction = paymentDeductionManager.get(deductionObject.getId());
                newPaymentOrDeduction.setRemarks(deductionObject.getRemarks());
                if (newPaymentOrDeduction.getCashAdvanceID() != null) {
                    EdsCashAdvance cashAdvance = cashAdvanceManager.get(newPaymentOrDeduction.getCashAdvanceID());
                    checkCashAdvanceForFullyPaid(deductionObject.getId(), cashAdvance.getObjectID());
                }
                paymentDeductionManager.createOrUpdate(newPaymentOrDeduction);
            } else {
                newPaymentOrDeduction = new EdsPaymentDeduction();
                newPaymentOrDeduction.setCategory(categoryManager.get(deductionObject.getCategoryItem().getId()));
                newPaymentOrDeduction.setEmployee(payslipTableItem.getEmployee());
                newPaymentOrDeduction.setPaymentAmount(deductionObject.getPaymentAmount());
                if (deductionObject.getLeaveDaysCount() != null) {
                    newPaymentOrDeduction.setLeaveDaysCount(deductionObject.getLeaveDaysCount());
                }
                newPaymentOrDeduction.setRemarks(deductionObject.getRemarks());
                paymentDeductionManager.create(newPaymentOrDeduction);
                payslipPayment = new EdsPayslipPayments();
                payslipPayment.setPaymentDeductionID(newPaymentOrDeduction.getObjectID());
                payslipPayment.setPaymentTotal(deductionObject.getPaymentAmount());
                payslipPayment.setPayslipItemID(payslipTableItem.getObjectID());
                payslipPayment.setForwardedPayment(deductionObject.getCategoryItem().getCode().contains(REMAINING_PREV_MONTH));
                payslipPaymentsManager.createOrUpdate(payslipPayment);
                paymentsForDelete.remove(payslipPayment);
            }
        }

        for (EdsPayslipPayments deletedPayment : paymentsForDelete) {
            payslipPaymentsManager.delete(deletedPayment);

            EdsPaymentDeduction paymentOrDeduction = paymentDeductionManager.get(deletedPayment.getPaymentDeductionID());
            if (paymentOrDeduction.getCashAdvanceID() != null) {
                EdsCashAdvance cashAdvance = cashAdvanceManager.get(paymentOrDeduction.getCashAdvanceID());
                checkCashAdvanceForFullyPaid(paymentOrDeduction.getObjectID(), cashAdvance.getObjectID());
            }
            paymentDeductionManager.createOrUpdate(paymentOrDeduction);
        }
    }

    private void createTransactionForPayslipTable(List<EdsPayslipTableItem> payslipTableItems) {
        for (EdsPayslipTableItem payslipTableItem : payslipTableItems) {
            if (payslipTableItem.isApproved() && !payslipTableItem.isTransacted()) {
                createTransactionForSinglePayrun(payslipTableItem);
            }
        }
    }

    public void createTransactionForSinglePayrun(EdsPayslipTableItem payslipTableItem) {
        BigDecimal paymentAmount;
        Set<EdsTransactionItem> transactionItems = new HashSet<>();

        boolean enabledMultiCurrency = "true".equals(getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL));
        BigDecimal exchangeRate = enabledMultiCurrency && payslipTableItem.getExchangeRate() != null ? payslipTableItem.getExchangeRate() : BigDecimal.ONE;
        List<EdsPaymentDeduction> categories = payslipPaymentsManager.getCategoriesForTransaction(payslipTableItem.getObjectID());
        for (EdsPaymentDeduction payment : categories) {
            EdsPayrollCategory category = payment.getCategory();
            if (category != null && REGULAR_OVERTIME.equals(category.getCode())) {
                createTransactionForOvertimes(payslipTableItem, transactionItems, exchangeRate, REGULAR_OVERTIME_CATEGORY_ID, payment);
            } else if (category != null && WEEKEND_OVERTIME.equals(category.getCode())) {
                createTransactionForOvertimes(payslipTableItem, transactionItems, exchangeRate, WEEKEND_OVERTIME_CATEGORY_ID, payment);
            } else if (category != null && HOLIDAY_OVERTIME.equals(category.getCode())) {
                createTransactionForOvertimes(payslipTableItem, transactionItems, exchangeRate, HOLIDAY_OVERTIME_CATEGORY_ID, payment);
            } else if (category != null && category.getDebitToAccountID() != null && category.getCreditToAccountID() != null) {
                EdsAccount debitAccount = accountingManager.get(category.getDebitToAccountID());
                EdsAccount creditAccount = accountingManager.get(category.getCreditToAccountID());
                if (debitAccount != null && creditAccount != null) {
                    paymentAmount = payslipPaymentsManager.getPaymentAmount(payment.getObjectID(), payslipTableItem.getObjectID());
                    EdsTransactionItem debitItem = new EdsTransactionItem();
                    debitItem.setAccount(debitAccount);
                    debitItem.setDebit(paymentAmount.divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));

                    EdsTransactionItem creditItem = new EdsTransactionItem();
                    creditItem.setAccount(creditAccount);
                    creditItem.setCredit(paymentAmount.divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                    if (payslipTableItem.getEmployee() != null && payslipTableItem.getEmployee().getEmployeeDepartment() != null) {
                        debitItem.setDepartment(payslipTableItem.getEmployee().getEmployeeDepartment().getTeam());
                        creditItem.setDepartment(payslipTableItem.getEmployee().getEmployeeDepartment().getTeam());
                    }

                    transactionItems.add(debitItem);
                    transactionItems.add(creditItem);
                }
                if (category.isCashAdvance() && payment.getCashAdvanceID() != null) {
                    checkCashAdvanceForFullyPaid(payment.getObjectID(), payment.getCashAdvanceID());
                }
            }
        }

        EdsPayrollCategory employeePensionCategory = categoryManager.getCategoryByCode(EMPLOYEE_PENSION_DEDUCTION);
        EdsPayrollCategory employerPensionCategory = categoryManager.getCategoryByCode(EMPLOYER_PENSION_DEDUCTION);

        if (payslipTableItem.getPensionAmount() != null && payslipTableItem.getPensionAmount().compareTo(BigDecimal.ZERO) != 0 &&
                employeePensionCategory != null && employeePensionCategory.getDebitToAccountID() != null && employeePensionCategory.getCreditToAccountID() != null) {
            EdsAccount debitAccount = accountingManager.get(employeePensionCategory.getDebitToAccountID());
            EdsAccount creditAccount = accountingManager.get(employeePensionCategory.getCreditToAccountID());
            if (debitAccount != null && creditAccount != null) {
                EdsTransactionItem debitItem = new EdsTransactionItem();
                debitItem.setType(EMPLOYEE_CONTRIBUTION);
                debitItem.setAccount(debitAccount);
                debitItem.setDebit(payslipTableItem.getPensionAmount().divide(exchangeRate, EdsFinancialSettings.SYSTEM_CALCULATION_SCALE, RoundingMode.HALF_UP));
                transactionItems.add(debitItem);

                EdsTransactionItem creditItem = new EdsTransactionItem();
                creditItem.setType(EMPLOYEE_CONTRIBUTION);
                creditItem.setAccount(creditAccount);
                creditItem.setCredit(payslipTableItem.getPensionAmount().divide(exchangeRate, EdsFinancialSettings.SYSTEM_CALCULATION_SCALE, RoundingMode.HALF_UP));
                transactionItems.add(creditItem);
            }

        }
        if (payslipTableItem.getCompanyPensionAmount() != null && payslipTableItem.getCompanyPensionAmount().compareTo(BigDecimal.ZERO) != 0 &&
                employerPensionCategory != null && employerPensionCategory.getDebitToAccountID() != null && employerPensionCategory.getCreditToAccountID() != null) {
            EdsAccount debitAccount = accountingManager.get(employerPensionCategory.getDebitToAccountID());
            EdsAccount creditAccount = accountingManager.get(employerPensionCategory.getCreditToAccountID());
            if (debitAccount != null && creditAccount != null) {
                EdsTransactionItem debitItem = new EdsTransactionItem();
                debitItem.setType(EMPLOYEER_CONTRIBUTION);
                debitItem.setAccount(debitAccount);
                debitItem.setDebit(payslipTableItem.getCompanyPensionAmount().divide(exchangeRate, EdsFinancialSettings.SYSTEM_CALCULATION_SCALE, RoundingMode.HALF_UP));
                transactionItems.add(debitItem);

                EdsTransactionItem creditItem = new EdsTransactionItem();
                creditItem.setType(EMPLOYEER_CONTRIBUTION);
                creditItem.setAccount(creditAccount);
                creditItem.setCredit(payslipTableItem.getCompanyPensionAmount().divide(exchangeRate, EdsFinancialSettings.SYSTEM_CALCULATION_SCALE, RoundingMode.HALF_UP));
                transactionItems.add(creditItem);
            }
        }

        List<EdsExpenseReport> linkedExpenses = expenseReportManager.getPayslipTableItemRelatedExpenseClaims(payslipTableItem.getObjectID());
        if (linkedExpenses != null && linkedExpenses.size() > 0) {
            EdsReference exp_paid = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_PAID);
            for (EdsExpenseReport expenseReport : linkedExpenses) {
                expenseReport.setEntityStatus(exp_paid);
                expenseReportManager.update(expenseReport);

                boolean empInExpense = Objects.equals(expenseReport.getCurrency(), payslipTableItem.getCurrency());
                EdsExpensePayment expensePayment = new EdsExpensePayment();
                expensePayment.setExpenseReport(expenseReport);
                expensePayment.setAccount(expenseReport.getAccount());
                expensePayment.setAmount(expenseReport.getTotal().subtract(expenseReport.getPaidTotal(false)));
                expensePayment.setPaymentDate(payslipTableItem.getToDate());
                expensePayment.setReference(null);
                expensePayment.setUser(referenceManager.getUser());
                expensePayment.setPayslipTableItem(payslipTableItem);
                if (empInExpense) {
                    expensePayment.setExchangeRate(exchangeRate);
                } else {
                    expensePayment.setExchangeRate(expenseReport.getExchangeRate());
                }
                expensePaymentManager.create(expensePayment);
                createTransactionForExpencePayment(expensePayment, expenseReport.getPaymentType());
                expenseReport.getPayments().add(expensePayment);
            }
            try {
                expenseReportClaimsSolrComponent.indexes(linkedExpenses);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (transactionItems.size() > 0) {
            EdsSinglePayrunTransaction transaction = new EdsSinglePayrunTransaction();
            EdsUser user = transactionManager.getUser();
            transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID(user.getCompany()) + 1);
            transaction.setJournalDate(payslipTableItem.getProcessDate() != null ? payslipTableItem.getProcessDate() : payslipTableItem.getToDate());
            transaction.setName(payslipTableItem.getEmployee().getFullName() + (payslipTableItem.isFromEndOfService() ? " End Of Service" : (" Payrun For " + payslipTableItem.getMonth())));
            transaction.setPostedBy(user);
            transaction.setPostedDate(user.getCompany().getCompanyDate());

            transaction.setTransactionItems(transactionItems);
            transaction.setPayrun(payslipTableItem);
            transactionManager.create(transaction);

            payslipTableItem.setTransacted(true);
            payslipTableItemManager.update(payslipTableItem);
        }
    }

    private void createTransactionForOvertimes(EdsPayslipTableItem payslipTableItem, Set<EdsTransactionItem> transactionItems, BigDecimal exchangeRate, String overtimeCategoryCode, EdsPaymentDeduction payment) {
        BigDecimal paymentAmount;
        EdsEmployeePayrollSettings eps = employeePayrollSettingsManager.getEmployeeSettingValue(payslipTableItem.getEmployee().getObjectID(), overtimeCategoryCode);
        if (eps != null) {
            EdsPayrollCategory overtimeCategory = categoryManager.get(Integer.valueOf(eps.getValue()));
            EdsAccount debitAccount = accountingManager.get(overtimeCategory.getDebitToAccountID());
            EdsAccount creditAccount = accountingManager.get(overtimeCategory.getCreditToAccountID());
            if (debitAccount != null && creditAccount != null) {
                paymentAmount = payslipPaymentsManager.getPaymentAmount(payment.getObjectID(), payslipTableItem.getObjectID());
                EdsTransactionItem debitItem = new EdsTransactionItem();
                debitItem.setAccount(debitAccount);
                debitItem.setDebit(paymentAmount.divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));

                EdsTransactionItem creditItem = new EdsTransactionItem();
                creditItem.setAccount(creditAccount);
                creditItem.setCredit(paymentAmount.divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                if (payslipTableItem.getEmployee() != null && payslipTableItem.getEmployee().getEmployeeDepartment() != null) {
                    debitItem.setDepartment(payslipTableItem.getEmployee().getEmployeeDepartment().getTeam());
                    creditItem.setDepartment(payslipTableItem.getEmployee().getEmployeeDepartment().getTeam());
                }

                transactionItems.add(debitItem);
                transactionItems.add(creditItem);
            }
        }
    }

    private void checkCashAdvanceForFullyPaid(Integer paymentDeductionId, Integer cashAdvanceID) {
        EdsCashAdvance cashAdvance = cashAdvanceManager.get(cashAdvanceID);

        BigDecimal payTotal = payslipPaymentsManager.getPayedAmountByCategory(paymentDeductionId);
        if (payTotal != null && payTotal.compareTo(BigDecimal.ZERO) > 0) {
            if (payTotal.compareTo(cashAdvance.getTotalAmount()) >= 0) {
                cashAdvance.setOverallStatus(referenceManager.getByCode(PAID));
            } else {
                cashAdvance.setOverallStatus(referenceManager.getByCode(PARTIALLY_PAID));
            }
        } else {
            if (cashAdvance.getStatus() != null && (cashAdvance.getStatus().getCode().equals(PAID) || PARTIALLY_PAID.equals(cashAdvance.getStatus().getCode()))) {
                EdsCashAdvanceTransaction transaction = transactionManager.getTransactionByCashAdvance(cashAdvance.getObjectID());
                if (transaction != null) {
                    cashAdvance.setOverallStatus(referenceManager.getByCode(POSTED));
                } else {
                    cashAdvance.setOverallStatus(referenceManager.getByCode(APPROVED));
                }
                cashAdvanceManager.update(cashAdvance);
            }
        }
        addCashAdvanceToSolr(cashAdvance);
    }

    @Override
    public void sendPayslipNotification(Integer payslipTableID) {
        try {
            messageManager.sendPayslipToEmployees(payslipTableID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletePayslipTable(Integer objectID) {
        EdsPayslipTable payslipTable = payslipTableManager.get(objectID);
        payslipTable.setDeleted(true);
        transactionManager.deleteTransactionsByPayslipTable(objectID);
        for (EdsPayslipTableItem item : payslipTable.getPayslipTableItems()) {
            List<EdsPaymentDeduction> linkedCashAdvances = paymentDeductionManager.getSinglePayrunCashAdvanceDeductions(item.getObjectID());
            payslipPaymentsManager.deleteByPayslipItemID(item.getObjectID());
            checkExpensesForSinglePayrunDelete(item.getObjectID());

            transactionManager.deleteTransactionsByPayrun(item.getObjectID());
            item.setDeleted(true);
            item.setLastUpdateTime(new Date());
            payslipTableItemManager.update(item);
            checkCashAdvancesForSinglePayrunDelete(linkedCashAdvances);
            baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, item, userManager.getUser());
        }

        if (!CollectionUtils.isEmpty(payslipTable.getPayments())) {
            payslipTable.getPayments().forEach(payment -> deletePayrunPayment(payment.getObjectID()));
        }

        baseEventsPostProcessor.registerEvent(GroupPayrunEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, payslipTable, userManager.getUser());
        payslipTable.setLastUpdateTime(new Date());
        EdsUser user = userManager.getUser();
        if (user != null) {
            payslipTable.setUpdator(user);
        }
        payslipTableManager.update(payslipTable);
        removeSinglePayrunFromSolr(payslipTable.getPayslipTableItems().toArray(new EdsPayslipTableItem[]{}));
        removeGroupPayrunFromSolr(payslipTable);
    }

    private void removeSinglePayrunFromSolr(EdsPayslipTableItem... edsPayslipTableItems) {
        List<Integer> ids = new ArrayList<>();
        for (EdsPayslipTableItem item : edsPayslipTableItems) {
            ids.add(item.getObjectID());
        }
        try {
            solrManager.removeSinglePayrunByIds(ids.toArray(new Integer[]{}));
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    private void removeGroupPayrunFromSolr(EdsPayslipTable payslipTable) {
        try {
            solrManager.removeGroupPayrunByIds(payslipTable.getObjectID());
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    private void removeCashAdvanceFromSolr(EdsCashAdvance cashAdvance) {
        try {
            solrManager.removeCashAdvanceByIds(cashAdvance.getObjectID());
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addGroupPayrunToSolr(EdsPayslipTable... payslipTable) {
        try {
            groupPayrunSolrComponent.indexes(Arrays.asList(payslipTable));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void addCashAdvanceToSolr(EdsCashAdvance... cashAdvances) {
        try {
            cashAdvanceSolrComponent.indexes(Arrays.asList(cashAdvances));

            WebSocketServerObject message = new WebSocketServerObject();
            message.setEventType(WfmUiEventType.ON_CASH_SAVED);
            if (SecurityContext.getInstance().getUser() != null) {
                Integer userId = ((EdsUser) SecurityContext.getInstance().getUser()).getObjectID();
                message.setUserId(userId);
                rabbitMQService.sendWebPushNotification(message);
            }
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addAdditionalPaymentToSolr(Integer objectID) {
        EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(objectID);
        if (additionalPayment == null) {
            return;
        }
        try {
//            solrManager.addAdditionalPaymentToIndex(additionalPayment);
            additionalPaymentSolrComponent.index(additionalPayment);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void addAdditionalPaymentToSolr(EdsAdditionalPayment... payments) {
        try {
            additionalPaymentSolrComponent.indexes(Arrays.asList(payments));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeAdditionalPaymentFromSolr(EdsAdditionalPayment payment) {
        try {
            solrManager.removeAdditionalPaymentByIds(payment.getObjectID());
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EndOfServiceData saveEndOfServiceSettings(EndOfServiceData settings) {
        EndOfServiceSettings eosSettings;
        EdsPayrollCategory category;
        if (settings.getObjectID() != null) {
            eosSettings = endOfServiceManager.get(settings.getObjectID());
            if (isNotSaudiCompany()) {
                endOfServiceRuleManager.deleteRuleSettings(settings.getObjectID());
                categoryManager.deleteReferenceByEndOfServiceSettings(settings.getObjectID());
                endOfServiceRuleManager.flush();
            }
        } else {
            eosSettings = new EndOfServiceSettings();
        }
        eosSettings.setCountryCode(settings.getCountryCode());
        eosSettings.setPayFrom(settings.getPayType());
        eosSettings.setIncludeLeaveAllowances(settings.isIncludeLeaveAllowances());
        eosSettings.setIncludeBenefitPayments(settings.isIncludeBenefitPayments());
        eosSettings.setUseMonthPayment(settings.isUseMonthPayment());
        eosSettings.setFromAllAllowances(settings.isAllAllowanceFromLastPayment());
        eosSettings.setFromLastPayment(settings.isFromLastPayment());
        endOfServiceManager.createOrUpdate(eosSettings);
        if (isNotSaudiCompany()) {
            for (EndOfServiceRules rule : settings.getRules()) {
                if (rule != null) {
                    EdsRuleEosSettings ruleEos = new EdsRuleEosSettings();
                    ruleEos.setDays(rule.getDays());
                    if (rule.getMonths() != null) {
                        ruleEos.setMonths(rule.getMonths());
                    }
                    ruleEos.setReasonCode(rule.getReasonCode());
                    ruleEos.setRule(rule.getRule());
                    ruleEos.setRuleType(rule.getRuleType());
                    ruleEos.setRuleCode(rule.getRuleCode());
                    ruleEos.setSettings(eosSettings);
                    endOfServiceRuleManager.create(ruleEos);
                }
            }
        }
        settings.setObjectID(eosSettings.getObjectID());
        if (settings.getAllowances().size() > 0) {
            for (PaymentDeductionSelectItem item : settings.getAllowances()) {
                category = categoryManager.get(item.getId());
                if (category != null) {
                    category.setEndOfServiceSettings(eosSettings);
                }
            }
        }
        baseEventsPostProcessor.registerEvent(EndOfServiceSettingsEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, eosSettings, userManager.getUser());
        return settings;
    }

    @Override
    public EndOfServiceData getEndOfServiceSettings(String countryCode) {
        EndOfServiceSettings settings = endOfServiceManager.getEndOfServiceSettings(countryCode);
        EndOfServiceData result = new EndOfServiceData();
        if (settings != null) {
            result.setCountryCode(settings.getCountryCode());
            result.setObjectID(settings.getObjectID());
            EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
            if (fs != null) {
                EdsCurrency currency = fs.getCurrency();
                if (currency != null) {
                    result.setCurrencyCode(currency.getSymbol() != null ? currency.getSymbol() : currency.getName());
                }
            }
            List<EndOfServiceRules> rules = new ArrayList<>();
            for (EdsRuleEosSettings rule : settings.getRuleSettings()) {
                EndOfServiceRules ruleS = rule.getRPC();
                ruleS.setUseMonthPayment(settings.isUseMonthPayment());
                rules.add(ruleS);
            }
            result.setRules(rules.toArray(new EndOfServiceRules[]{}));
            result.setIncludeLeaveAllowances(settings.isIncludeLeaveAllowances());
            result.setIncludeBenefitPayments(settings.isIncludeBenefitPayments());
            result.setUseMonthPayment(settings.isUseMonthPayment());
            result.setAllAllowanceFromLastPayment(settings.isFromAllAllowances());
            result.setFromLastPayment(settings.isFromLastPayment());
            result.setPayType(settings.getPayFrom());
            if (settings.getCategories() != null && settings.getCategories().size() > 0) {
                for (EdsPayrollCategory category : settings.getCategories()) {
                    result.getAllowances().add(category.createPaymentDeductionSelectItem());
                }
            }
        }
        //set numbering
        BankTransferNumberData transferNumberData = new BankTransferNumberData();
        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        Integer fourDigitNumber = endOfServiceGratuityManager.getLastIntNumber();
        String format = null;
        if (numberingSettings != null) {
            format = numberingSettings.getEndOfServiceNumberingFormat();
        }
        if (format != null) {
            parseNumber(format, transferNumberData, fourDigitNumber);
        } else {
            String prefix = EdsNumberingSettings.DEF_CASH_ADVANCE_PREFIX;
            NumberData numberData = EdsNumberingSettings.getDefaultData(fourDigitNumber, prefix);
            String[] numberParts = numberData.getNumberFormat().split("_");
            transferNumberData.setPrefix(numberParts[0]);
            transferNumberData.setFourDigitNumber(String.valueOf(numberParts[1]));
            transferNumberData.setWithDate(numberParts[1].split("-").length == 2);
        }
        result.setNumberData(transferNumberData);
        return result;
    }

    @Override
    public ListResult<EoSCalculationData> getEndOfServiceGratuityList(ListingFilterParameter filterParameter) {
        EoSCalculationData calculationData;
        ArrayList<EoSCalculationData> calculationDataList = new ArrayList<>();
        List<EdsEosCalculation> list = endOfServiceGratuityManager.getEosCalculationList(filterParameter);
        Integer totalCount = endOfServiceGratuityManager.getEosCalculationCount();
        for (EdsEosCalculation data : list) {
            calculationData = new EoSCalculationData();
            calculationData.setObjectID(data.getObjectID());
            if (data.getCreator() != null) {
                calculationData.setCreator(new SelectItem(data.getCreator().getObjectID(), data.getCreator().getName()));
            }
            if (data.getEmployee() != null) {
                EdsEmployee edsEmployee = data.getEmployee();
                calculationData.setEmployee(new SelectItem(edsEmployee.getObjectID(), edsEmployee.getName()));
                calculationData.setEmployeeSalaryCurrency(edsEmployee.getSalaryCurrency() != null ? edsEmployee.getSalaryCurrency().getName() : null);
                if (edsEmployee.getProfile() != null) {
                    calculationData.setEmployeeCode(edsEmployee.getProfile().getEmployeeCode() != null ? edsEmployee.getProfile().getEmployeeCode() : "");
                }
            }
            calculationData.setDate(new DateNonConvertable(data.getCreationDate()));
            calculationData.setHireDate(new DateNonConvertable(data.getHireDate()));
            calculationData.setResignationDate(new DateNonConvertable(data.getResignationDate()));
            calculationData.setReasonCode(data.getReasonCode());
            calculationData.setPaymentNumber(data.getPaymentNumber());
            calculationData.setTotalWorkedDays(data.getTotalWorkedDays());
            calculationData.setEosAmount(data.getTotalAmount());
            calculationData.setCurrency(data.getCurrency() != null ? data.getCurrency().createCurrencyItem() : null);
            calculationDataList.add(calculationData);
        }
        return new ListResult<>(calculationDataList, totalCount);
    }

    @Override
    @Transactional
    public EoSCalculationData getEmployeeEosData(PayslipItemFilter filter, String countryCode) {
        EoSCalculationData result = new EoSCalculationData();

        if (filter == null) {
            return result;
        }
        EndOfServiceSettings endOfServiceSettings = endOfServiceManager.getEndOfServiceSettings(countryCode);
        if (endOfServiceSettings == null) {
            endOfServiceSettings = new EndOfServiceSettings();
            endOfServiceSettings.setCountryCode(countryCode);
            endOfServiceManager.create(endOfServiceSettings);
        }
        ListingFilterParameter lfp = new ListingFilterParameter();
        lfp.setEmployeeId(filter.getEmployeeID());
        lfp.setType(NONE);
        List<EdsSickRequest> nonPaidSickRequests = sickRequestManager.getNonPaidLeaveRequests(lfp);
        int totalDays = 0;
        EdsEmployee employee = employeeManager.get(filter.getEmployeeID());
        String enabledMultiCurrency = getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL);
        //set numbering
        BankTransferNumberData transferNumberData = new BankTransferNumberData();
        getEndOfServiceNumberData(transferNumberData);
        result.setPaymentNumber(transferNumberData.getTransferNumber());
        result.setNumberData(transferNumberData);
        boolean isDailyRateByEmployerSettings = "true".equals(getCompanyPayrollSettings(DAILY_RATE_BY_EMPLOYER_SETTINGS)) || "BY_STATIC_DAY".equals(getCompanyPayrollSettings(DAILY_RATE_BY_EMPLOYER_SETTINGS));
        if (isDailyRateByEmployerSettings) {
            result.setNumberOfWorkDay(Double.parseDouble(getCompanyPayrollSettings(NUMBER_OF_WORK_DAYS, DEFAULT_NUMBER_OF_WORK_DAYS.toString())));
        }
        result.setEnabledMultiCurrency("true".equals(enabledMultiCurrency));
        result.setCurrency(employee.getSalaryCurrency() != null ? employee.getSalaryCurrency().createCurrencyItem() : null);
        if (employee != null) {
            result.setHireDate(new DateNonConvertable(employee.getStartDate()));
            result.setResignationDate(new DateNonConvertable(employee.getEndDate()));
            if (employee.getStartDate() != null && employee.getEndDate() != null) {
                totalDays += DateUtil.countDays(employee.getStartDate(), employee.getEndDate());
            }

        }
        for (EdsSickRequest request : nonPaidSickRequests) {
            if (!request.isTakeByMoney() && request.getStartDate() != null && request.getEndDate() != null) {
                totalDays -= countLeaveRequestDays(request).intValue();
            }
        }
        result.setTotalWorkedDays(totalDays);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        int currentYear = Integer.parseInt(dateFormat.format(new Date()));
        EdsEmployeePayrollSettings employeePayrollSettings = employeePayrollSettingsManager.getEmployeeSettingValue(filter.getEmployeeID(), SALARY);
        if (employeePayrollSettings != null) {
            result.setBasicSalary(new BigDecimal(employeePayrollSettings.getValue()));
        }
        EdsEmployeePayrollSettings employeeContractStartDate = employeePayrollSettingsManager.getEmployeeSettingValue(filter.getEmployeeID(), CONTRACT_START_DATE);
        EdsEmployeePayrollSettings employeeContractEndDate = employeePayrollSettingsManager.getEmployeeSettingValue(filter.getEmployeeID(), CONTRACT_END_DATE);
        if (employeeContractStartDate != null) {
            if (employeeContractStartDate.getValue() != null && !employeeContractStartDate.getValue().isEmpty()) {
                if (employeeContractEndDate != null && employeeContractEndDate.getValue() != null && !employeeContractEndDate.getValue().isEmpty()) {
                    result.setEmployeeContractType(1);
                } else {
                    result.setEmployeeContractType(0);
                }
            } else {
                result.setEmployeeContractType(0);
            }
        } else {
            result.setEmployeeContractType(0);
        }

        if (endOfServiceSettings.isIncludeLeaveAllowances()) {
            List<EdsAnnualLeaveAllowance> leaveAllowanceList = annualLeaveAllowanceManager.getLeaveAllowance(currentYear, Collections.singletonList(filter.getEmployeeID()));
            lfp.setYear(currentYear);
            double payDays = 0.00;
            for (EdsAnnualLeaveAllowance annualLeaveAllowance : leaveAllowanceList) {
                lfp.setReasonID(annualLeaveAllowance.getReason().getObjectID());
                Double[] stats = attendanceRawDataManager.getLeaveRequestMinutes(lfp);
                double usedDays = stats[DAYS_FROM_ANNUAL] != null ? stats[DAYS_FROM_ANNUAL] : 0;
                int annualAllowanceMinutes = annualLeaveAllowance.getAnnualAllowanceMinutes();
                double annualAllowanceDays = (double) annualAllowanceMinutes / ServerUtils.getDailyAverageTimeslotMinutes(employee.getTimeSlot().getItems());
                if (usedDays == 0) {
                    payDays += annualAllowanceDays;
                } else if (annualAllowanceDays >= usedDays) {
                    payDays += annualAllowanceDays - usedDays;
                }
            }
            result.setLeftLeaveDays(BigDecimal.valueOf(payDays));
            BigDecimal dailyRate = result.getBasicSalary().divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP);
            result.setLeaveAllowanceTotal(dailyRate.multiply(result.getLeftLeaveDays()));
        } else {
            result.setLeaveAllowanceTotal(BigDecimal.ZERO);
        }

        if (endOfServiceSettings.isIncludeBenefitPayments()) {
            BigDecimal benefitTotal = employeeBenefitAllowanceManager.getBenefitPaymentForEndOfServiceCalculation(currentYear, filter.getEmployeeID());
            result.setBenefitPaymentTotal(benefitTotal);
        } else {
            result.setBenefitPaymentTotal(BigDecimal.ZERO);
        }


        if (endOfServiceSettings.isFromAllAllowances()) {
            List<Object[]> payslipItemData = payslipPaymentsManager.getEosDataFromPaylipTableItem(filter.getEmployeeID());
            BigDecimal lastPaymentsTotal = BigDecimal.ZERO;
            if (payslipItemData.size() > 0) {
                Object[] newData = payslipItemData.get(0);
                Date newPayslipItemDate = (Date) newData[1];
                if (newPayslipItemDate != null && newData[0] != null) {
                    lastPaymentsTotal = payslipTableItemManager.getPayslipItemPaymentsTotal((Integer) newData[0], "Payment");
                }

            }
            result.setLastPaymentsTotal(lastPaymentsTotal);
        } else if (endOfServiceSettings.getCategories() != null && endOfServiceSettings.getCategories().size() > 0) {
            StringBuilder categories = new StringBuilder();
            String categoryString;
            for (EdsPayrollCategory category : endOfServiceSettings.getCategories()) {
                categories.append("'").append(category.getCode()).append("',");
            }
            categoryString = categories.substring(0, categories.toString().length() - 1);
            if (endOfServiceSettings.isFromLastPayment()) {
                List<Object[]> payslipItemData = payslipPaymentsManager.getEosDataFromPaylipTableItemByCategory(filter.getEmployeeID(), categoryString);
                BigDecimal lastPaymentsTotal = BigDecimal.ZERO;
                if (payslipItemData.size() > 0) {
                    Object[] newData = payslipItemData.get(0);
                    Date newPayslipItemDate = (Date) newData[1];
                    if (newPayslipItemDate != null && newData[0] != null) {
                        lastPaymentsTotal = payslipPaymentsManager.getPaymentAmount((Integer) newData[1], (Integer) newData[0]);
                    }
                }
                result.setLastPaymentsTotal(lastPaymentsTotal);
            } else {
                result.setLastPaymentsTotal(paymentDeductionManager.getTotalPaymentByCategories(filter.getEmployeeID(), categoryString));
            }
        } else {
            result.setLastPaymentsTotal(BigDecimal.ZERO);
        }
        if (result.getResignationDate() != null) {
            final Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTimeInMillis(result.getResignationDate().getDateLong());
            startCalendar.set(Calendar.DAY_OF_MONTH, 1);

            final Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTimeInMillis(result.getResignationDate().getDateLong());

            final Integer resignationYear = endCalendar.get(Calendar.YEAR);

            filter.setDaysOfMonth(startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            if (employee != null) {
                filter.setEmployeeID(employee.getObjectID());
                filter.setEmployeeName(employee.getFullName());
            }
            filter.setFromDate(new DateNonConvertable(startCalendar.getTime()));
            filter.setToDate(result.getResignationDate());
            filter.setPeriodChecker(endCalendar.get(Calendar.MONTH) + "," + resignationYear);
            filter.setFromChangeHandler(true);
            filter.setYear(resignationYear);
            filter.setMonth(endCalendar.get(Calendar.MONTH));
            final SinglePayrunItem singlePayrunData = this.getSinglePayrunData(filter);

            if (singlePayrunData.getObjectID() == null) {
                singlePayrunData.setMonthID(filter.getMonth());
                singlePayrunData.setMonth(new DateFormatSymbols().getMonths()[filter.getMonth()]);
                singlePayrunData.setYear(filter.getYear());
                singlePayrunData.setFromDate(filter.getFromDate());
                singlePayrunData.setToDate(filter.getToDate());
                singlePayrunData.setProcessDate(filter.getToDate());
            }
            result.setPayrunItem(singlePayrunData);
        }
        return result;
    }

    private void getEndOfServiceNumberData(BankTransferNumberData transferNumberData) {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer fourDigitNumber = endOfServiceGratuityManager.getLastIntNumber();
        String format = null;
        if (settings != null) {
            format = settings.getEndOfServiceNumberingFormat();
        }
        if (format != null) {
            parseNumber(format, transferNumberData, fourDigitNumber);
        } else {
            String prefix = EdsNumberingSettings.DEF_END_OF_SERVICE;
            NumberData numberData = EdsNumberingSettings.getDefaultData(fourDigitNumber, prefix);
            String[] numberParts = numberData.getNumberFormat().split("_");
            transferNumberData.setPrefix(numberParts[0]);
            transferNumberData.setFourDigitNumber(String.valueOf(numberParts[1]));
            transferNumberData.setWithDate(numberParts[1].split("-").length == 2);
        }
    }

    @Override
    public Boolean saveEosCalculationData(EoSCalculationData data) {
        if (data == null || data.getDate() == null || data.getEmployee() == null) {
            return false;
        }
        final EdsEosCalculation calculation = new EdsEosCalculation();
        if (data.getDate() != null) {
            calculation.setCreationDate(data.getDate().getNonConvertedDate());
        }
        if (data.getHireDate() != null) {
            calculation.setHireDate(data.getHireDate().getNonConvertedDate());
        }
        if (data.getResignationDate() != null) {
            calculation.setResignationDate(data.getResignationDate().getNonConvertedDate());
        }
        if (data.getCreator() != null && data.getCreator().getId() != null) {
            EdsEmployee creator = employeeManager.get(data.getCreator().getId());
            calculation.setCreator(creator);
        }
        if (data.getEmployee() != null && data.getEmployee().getId() != null) {
            EdsEmployee employee = employeeManager.get(data.getEmployee().getId());
            calculation.setEmployee(employee);
        }
        calculation.setPaymentNumber(data.getPaymentNumber());
        if (data.getNumberData() != null) {
            calculation.setFourDigitNumber(Integer.parseInt(data.getNumberData().getFourDigitNumber()));
        }
        calculation.setReasonCode(data.getReasonCode());
        calculation.setTotalWorkedDays(data.getTotalWorkedDays());
        calculation.setTotalAmount(data.getEosAmount());
        calculation.setCurrency(data.getCurrency() != null ? currencyManager.get(data.getCurrency().getId()) : null);
        calculation.setExchangeRate(data.getExchangeRate());
        endOfServiceGratuityManager.create(calculation);
        this.createSinglePayrunForCalculation(calculation);

        if (data.getPayrunItem() != null) {
            this.saveSinglePayrun(data.getPayrunItem());
        }
        return true;
    }

    @Override
    public SelectItem[] getJobTitles() {
        List<EdsATSJobSalary> jobSalaryList = employeePayrollSettingsManager.getJobTitles();
        SelectItem[] res = new SelectItem[jobSalaryList.size()];
        int i = 0;
        for (EdsATSJobSalary jobSalary : jobSalaryList) {
            res[i++] = new SelectItem(jobSalary.getObjectID(), jobSalary.getJobTitle(), jobSalary.getSalaryAmount());
        }
        return res;
    }

    public CashAdvanceItem getCashAdvancedItem(ListingFilterParameter filterParameter) {
        CashAdvanceItem result = new CashAdvanceItem();
        Integer objectID = filterParameter.getObjectId();
        if (objectID != null) {
            EdsCashAdvance cashAdvance = cashAdvanceManager.get(objectID);
            if (cashAdvance != null && !cashAdvance.getDeleted()) {
                result = cashAdvance.getRPC();
                String doubleConfirmation = getCompanyPayrollSettings(DOUBLE_CONFIRMATION);
                result.setDoubleConfirmationEnabled("true".equals(doubleConfirmation));
                if (result.getOverAllStatus() != null && !APPROVED.equalsIgnoreCase(result.getOverAllStatus()) && cashAdvance.getCurrentApprover() != null && cashAdvance.getCurrentApprover().getExactEmployee() != null && cashAdvance.getCurrentApprover().getExactEmployee().getObjectID().equals(userManager.getUser().getObjectID())) {
                    result.setCanApprove(true);
                }
                result.setPdfTemplateList(getPayrollPdfTemplates(PdfReferenceCodeNameEnum.CASH_ADVANCE.name()));
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setObjectId(objectID);
                ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.CashAdvanceList);
                result.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(cashAdvance.getCustomFields(), customFieldsItems));
                BigDecimal appliedAmount = cashAdvanceManager.getCashAdvanceAppliedAmount(fp);
                appliedAmount = appliedAmount != null ? appliedAmount : BigDecimal.ZERO;
                result.setRemainingAmount(cashAdvance.getTotalAmount().subtract(appliedAmount));
                result.setUsedInPayslip(cashAdvanceManager.isCashAdvanceUsedInPayslip(objectID));
            }
        } else {
            EdsUser user = userManager.getUser();
            EdsEmployee employee = user.getEmployee();
            result.setEmployee(new SelectItem(employee.getObjectID(),
                    employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null && !"".equals(employee.getProfile().getEmployeeCode())
                            ? employee.getProfile().getEmployeeCode().concat(" -> ").concat(employee.getFullName()) : employee.getFullName()
            ));
            result.setDriverNumber(employee.getDriverNumber() != null ?
                    new SelectItem(employee.getObjectID(), employee.getDriverNumber().toString().concat(" -> ").concat(employee.getFullName()), String.valueOf(employee.getDriverNumber())) : null);
            BankTransferNumberData btnd = generateCashAdvanceNumberFormat();
            result.setBankTransferNumberData(btnd);
            result.setNumber(btnd.getTransferNumber());
            result.setIntNumber(Integer.parseInt(btnd.getFourDigitNumber()));
        }
        String enabledMultiCurrency = getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL);
        result.setEnabledMultiCurrency("true".equals(enabledMultiCurrency));
        result.setCurrentUserId(userManager.getUser().getObjectID());
        result.setPaymentMethods(allInOneService.getPaymentMethodList());
        result.setTemplates(getCashAdvancePdfTempletes(PdfReferenceCodeNameEnum.CASH_ADVANCE.name()).getItems());
        return result;
    }

    private CustomFormItemPdfTemplateList getCashAdvancePdfTempletes(String type) {
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

    public BankTransferNumberData generateCashAdvanceNumberFormat() {
        BankTransferNumberData transferNumberData = new BankTransferNumberData();
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer fourDigitNumber = cashAdvanceManager.getCashAdvanceIntNumber();
        String format = null;
        if (settings != null) {
            format = settings.getCashAdvanceNumberFormat();
        }
        if (format != null) {
            parseNumber(format, transferNumberData, fourDigitNumber);
        } else {
            String prefix = EdsNumberingSettings.DEF_CASH_ADVANCE_PREFIX;
            NumberData numberData = EdsNumberingSettings.getDefaultData(fourDigitNumber, prefix);
            String[] numberParts = numberData.getNumberFormat().split("_");
            transferNumberData.setPrefix(numberParts[0]);
            transferNumberData.setFourDigitNumber(String.valueOf(numberParts[1]));
            transferNumberData.setWithDate(numberParts[1].split("-").length == 2);
        }
        return transferNumberData;
    }

    public BankTransferNumberData generateCashAdvanceNumberFormat(Integer fourDigitNumber) {
        BankTransferNumberData transferNumberData = new BankTransferNumberData();
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        String format = null;
        if (settings != null) {
            format = settings.getCashAdvanceNumberFormat();
        }
        if (format != null) {
            parseNumber(format, transferNumberData, fourDigitNumber);
        } else {
            String prefix = EdsNumberingSettings.DEF_CASH_ADVANCE_PREFIX;
            NumberData numberData = EdsNumberingSettings.getDefaultData(fourDigitNumber, prefix);
            String[] numberParts = numberData.getNumberFormat().split("_");
            transferNumberData.setPrefix(numberParts[0]);
            transferNumberData.setFourDigitNumber(String.valueOf(numberParts[1]));
            transferNumberData.setWithDate(numberParts[1].split("-").length == 2);
        }
        return transferNumberData;
    }

    private void parseNumber(String numberFormat, BankTransferNumberData numberData, Integer fourDigitNumber) {
        String[] mainPartNumbers = numberFormat.split("_");  // e.g CP_0001-05/2015 or CR_0001-05/2015
        String[] datePartNumbers = mainPartNumbers[1].split("-");  // e.g 0001-05/2015 or 0001-05/2015

        numberData.setPrefix(mainPartNumbers[0]);
        numberData.setWithDate(datePartNumbers.length == 2);

        String lastFourNumber = datePartNumbers[0];

        DecimalFormat format = new DecimalFormat("0000");
        numberData.setFourDigitNumber(fourDigitNumber != null ? format.format(fourDigitNumber + 1) : lastFourNumber);
        if (numberData.isWithDate()) {
            numberData.setDate(ServerUtils.getBankTransferDateNumber(new Date()));
        }
    }

    @Override
    public ListResult<CashAdvanceItem> getCashAdvanceList(ListingFilterParameter fp) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEmployee.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get CashAdvance list (from solr)");
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        FacetFilterRpc cashAdvanceFacetFilter = fp.getFacetFilter();
        if (cashAdvanceFacetFilter != null && !cashAdvanceFacetFilter.isFilterChanges()) {
            cashAdvanceFacetFilter = commonServiceLocal.getUserFacetFilter(cashAdvanceFacetFilter);
        }
        if (cashAdvanceFacetFilter != null) {
            if (cashAdvanceFacetFilter.getSearchKey() != null && !"".equals(cashAdvanceFacetFilter.getSearchKey())) {
                fp.setSearchKey(cashAdvanceFacetFilter.getSearchKey());
            }
            fp.setStartDate(cashAdvanceFacetFilter.getStartDate());
            fp.setEndDate(cashAdvanceFacetFilter.getEndDate());
            fp.setFacetFilter(cashAdvanceFacetFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();

        String solrQuery = QueryBuilderForSolr.getCashAdvanceSolrQuery(fp, edsUser) +
                SolrFacetUtils.generateForPricesFacet(cashAdvanceFacetFilter, FacetContentType.CashAdvanceFacetFilter.getContentCode()[2]) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(cashAdvanceFacetFilter, edsCompany,
                        SolrCashAdvanceRepresenter.FIELD_REQUEST_DATE,
                        SolrCashAdvanceRepresenter.FIELD_REQUEST_DATE, FacetContentType.CashAdvanceFacetFilter.getContentCode()[2]);

        return getCashAdvanceResponse(fp, solrQuery);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public TestRPC saveCashAdvance(CashAdvanceItem cashAdvanceItem) {
        TestRPC result = new TestRPC();
        if (cashAdvanceItem.getNumber() != null && cashAdvanceManager.numberExists(cashAdvanceItem.getNumber(), cashAdvanceItem.getObjectID())) {
            result.setMessage(CashAdvanceItem.NUMBER_EXISTS);
            return result;
        }
        EdsCashAdvance cashAdvance = cashAdvanceItem.getObjectID() != null ? cashAdvanceManager.get(cashAdvanceItem.getObjectID()) : null;
        String cashAdvanceOldStatus = null;
        if (cashAdvance != null) {
            cashAdvanceOldStatus = cashAdvance.getStatus() != null ? cashAdvance.getStatus().getCode() : null;
            if (APPROVED.equals(cashAdvance.getOverallStatus().getCode())) {
                ListingFilterParameter lfp = new ListingFilterParameter();
                lfp.setObjectId(cashAdvanceItem.getObjectID());
                BigDecimal remainingAmount = cashAdvanceManager.getCashAdvanceAppliedAmount(lfp);
                if (remainingAmount != null && remainingAmount.compareTo(cashAdvanceItem.getTotalAmount()) > 0) {
                    result.setMessage(CashAdvanceItem.NOT_SUFFICIENT_AMOUNT);
                    return result;
                }
            } else if (POSTED.equals(cashAdvance.getOverallStatus().getCode())) {
                ListingFilterParameter lfp = new ListingFilterParameter();
                lfp.setObjectId(cashAdvanceItem.getObjectID());
                BigDecimal remainingAmount = transactionManager.getCashAdvanceTransctionPaidAmount(lfp);
                if (remainingAmount != null && remainingAmount.compareTo(cashAdvanceItem.getTotalAmount()) > 0) {
                    result.setMessage(CashAdvanceItem.NOT_SUFFICIENT_AMOUNT);
                    return result;
                }
            }
            Integer pdoId = paymentDeductionManager.getPaymentDeductionIdByCashAdvance(cashAdvance.getObjectID());
            if (pdoId != null) {
                checkCashAdvanceForFullyPaid(pdoId, cashAdvance.getObjectID());
            }
        } else {
            cashAdvance = new EdsCashAdvance();
        }

        if (cashAdvanceItem.getEmployee() != null) {
            cashAdvance.setEmployee(employeeManager.get(cashAdvanceItem.getEmployee().getId()));
        }

        if (cashAdvanceItem.getCategoryItem() != null) {
            cashAdvance.setCategory(categoryManager.get(cashAdvanceItem.getCategoryItem().getId()));
        }
        if (cashAdvanceItem.getObjectID() == null) {
            if (cashAdvanceItem.getCreationDate() != null) {
                cashAdvance.setCreationDate(cashAdvanceItem.getCreationDate().getNonConvertedDate());
            } else {
                cashAdvance.setCreationDate(new Date());
            }
        }
        if (cashAdvanceItem.getApprovedDate() != null) {
            cashAdvance.setApprovedDate(cashAdvanceItem.getApprovedDate().getNonConvertedDate());
        }
        if (cashAdvanceItem.getTransactionDate() != null) {
            cashAdvance.setTransactionDate(cashAdvanceItem.getTransactionDate().getNonConvertedDate());
        }
        if (cashAdvanceItem.getPaidFromAccount() != null) {
            EdsAccount account = accountingManager.get(cashAdvanceItem.getPaidFromAccount().getId());
            cashAdvance.setAccount(account);
        }
        if (cashAdvanceItem.getCashAdvanceAccount() != null) {
            EdsAccount cashAccount = accountingManager.get(cashAdvanceItem.getCashAdvanceAccount().getId());
            cashAdvance.setCashAccount(cashAccount);
        }
        if (cashAdvanceItem.getCurrency() != null) {
            cashAdvance.setCurrency(currencyManager.get(cashAdvanceItem.getCurrency().getId()));
        }
        if (cashAdvanceItem.getIntNumber() != null) {
            cashAdvance.setIntNumber(cashAdvanceItem.getIntNumber());
        }

        EdsCashAdvanceCustomFields customFields = createCashAdvanceCustomFields(cashAdvanceItem.getCustomFieldItems());
        cashAdvance.setCustomFields(customFields);
        cashAdvance.setNumber(cashAdvanceItem.getNumber());
        cashAdvance.setReference(cashAdvanceItem.getReference());
        cashAdvance.setType(cashAdvanceItem.getType());
        cashAdvance.setTotalAmount(cashAdvanceItem.getTotalAmount());
        cashAdvance.setTotalInBase(cashAdvanceItem.getTotalInBaseAmount());
        cashAdvance.setExchangeRate(cashAdvanceItem.getExchangeRate());
        if (cashAdvanceItem.getDate() != null) {
            cashAdvance.setRequestDate(cashAdvanceItem.getDate().getNonConvertedDate());
        }
        cashAdvance.setLastUpdateTime(new Date());
        cashAdvance.setPurpose(cashAdvanceItem.getPurpose());
        cashAdvance.setPercent(cashAdvanceItem.getPercent());
        cashAdvance.setPaymentAmount(cashAdvanceItem.getPaymentAmount());
        if (cashAdvanceItem.getPaymentMethod() != null && cashAdvanceItem.getPaymentMethod().getId() != null) {
            cashAdvance.setPaymentMethod(paymentMethodManager.get(cashAdvanceItem.getPaymentMethod().getId()));
        }
        if (cashAdvanceItem.getMultiCashAdvanceId() != null) {
            cashAdvance.setMultiCashAdvance(multiCashAdvanceManager.get(cashAdvanceItem.getMultiCashAdvanceId()));
            cashAdvance.setBasicSalary(cashAdvanceItem.getBasicSalary());
            cashAdvance.setPercentage(cashAdvanceItem.getPercentage());
        }
        cashAdvanceManager.createOrUpdate(cashAdvance);

        boolean statusChanged = cashAdvance.getOverallStatus() != null && cashAdvanceItem.getStatus() != null && cashAdvanceItem.getStatus().getCode() != null && !cashAdvanceItem.getStatus().getCode().equals(cashAdvance.getOverallStatus().getCode());

        if (cashAdvanceItem.getAttachments() != null && cashAdvanceItem.getAttachments().length > 0) {
            saveCashAdvanceAttachments(cashAdvanceItem.getAttachments(), cashAdvance);
        }

        if (isOk(cashAdvanceItem.getApprovers())) {
            if (Constants.DRAFT.equals(cashAdvanceOldStatus)) {
                // it was difficult to merge expense approvers, so just deleting old records
                approverManager.deletedAprovers(RelationItem.TYPE_CASH_ADVANCE, cashAdvance.getObjectID());
                //delete prev/current approvers
                cashAdvance.setCurrentApprover(null);
                cashAdvance.setPrevApprover(null);
            }
            EdsReference status = (cashAdvanceItem.getStatus() != null && cashAdvanceItem.getStatus().getCode() != null) ? referenceManager.getByCode(cashAdvanceItem.getStatus().getCode()) : null;
            //if cash advance action is "save & close", status will be null.
            cashAdvanceItem.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            for (ApproverItemMini approverItem : cashAdvanceItem.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null && !_edsApprover.getDeleted()) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (cashAdvance.getCurrentApprover() != null && status != null) {
                        cashAdvance.getCurrentApprover().setStatus(status);
                    }
                    //force approve
                    if (cashAdvanceItem.isApproveForAll() && status != null && APPROVED.equals(status.getCode())) {
                        cashAdvance.setOverallStatus(status);
                    }
                    if (status != null && !APPROVED.equals(status.getCode())) {
                        cashAdvance.setOverallStatus(status);
                    }
                    if (cashAdvance.isCurrentApproverRejected()) {
                        cashAdvance.setOverallStatus(cashAdvance.getCurrentApprover().getStatus());
                    }
                    continue;
                }

                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(cashAdvance.getObjectID());
                edsApprover.setIs_default(false);
                edsApprover.setDeleted(false);
                if (status != null) {
                    edsApprover.setStatus(status);
                    cashAdvance.setOverallStatus(status);
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

                if (cashAdvance.getCurrentApprover() == null) {
                    cashAdvance.setCurrentApprover(edsApprover);
                }
                cashAdvance.getApprovers().add(edsApprover);
            }
            //update after new approvers set
            cashAdvanceManager.update(cashAdvance);
        } else {
            if (cashAdvanceItem.getStatus() != null) {
                cashAdvance.setOverallStatus(referenceManager.getByCode(cashAdvanceItem.getStatus().getCode()));
            }
        }
        addCashAdvanceToSolr(cashAdvance);
        if (cashAdvanceItem.getObjectID() == null) {
            baseEventsPostProcessor.registerEvent(CashAdvanceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, cashAdvance, userManager.getUser());

            EdsBusinessEvent workflowRule = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, cashAdvance, userManager.getUser());
            workflowRule.setEntityType(RelationItem.TYPE_CASH_ADVANCE);
        } else if (!APPROVED.equals(cashAdvanceItem.getOverAllStatus())) {
            baseEventsPostProcessor.registerEvent(CashAdvanceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, cashAdvance, userManager.getUser());

            EdsBusinessEvent workflowRule = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, cashAdvance, userManager.getUser());
            workflowRule.setEntityType(RelationItem.TYPE_CASH_ADVANCE);
        }
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), cashAdvance, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_CASH_ADVANCE);
        if (cashAdvanceItem.getStatus() != null && SUBMITTED_TO_MANAGER.equals(cashAdvanceItem.getStatus().getCode())) {
            EdsCurrency currency = null;
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            if (financialSettings != null) {
                currency = financialSettings.getCurrency();
            }
            boolean hasAlerts = false;
            List<EdsWorkflowRule> rules = workflowRuleManager.getByModuleAndActions(WorkflowRule._WORKFLOW_MODULE_CASH_ADVANCE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT);
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
                    messageManager.sendCashAdvanceRequestToApprover(cashAdvance, currency);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (cashAdvanceItem.getStatus() != null && REJECTED.equals(cashAdvanceItem.getStatus().getCode())) {
            boolean hasAlerts = false;
            if (cashAdvance.getCurrentApprover() != null && cashAdvance.getCurrentApprover().getOnRejectedWorkflow() != null) {
                EdsWorkflowRule rule = cashAdvance.getCurrentApprover().getOnRejectedWorkflow();
                if (rule != null) {
                    hasAlerts = workflowAlertManager.hasAlertsByRoleID(rule.getObjectID());
                }
            }
            if (!hasAlerts) {
                try {
                    messageManager.sendCashAdvanceRejectMessageToEmployee(cashAdvance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (cashAdvanceItem.getStatus() != null && POSTED.equals(cashAdvanceItem.getStatus().getCode())) {
            approvedActionForCashAdvance(cashAdvance.getObjectID(), true);
        }
        if (statusChanged) {
            baseEventsPostProcessor.registerEvent(CashAdvanceEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, cashAdvance, userManager.getUser());
        }
        result.setId(cashAdvance.getObjectID());
        return result;
    }

    private EdsCashAdvanceCustomFields createCashAdvanceCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsCashAdvanceCustomFields cashAdvanceCustomFields = null;
            if (customFieldItems.get(0).getObjectId() != null) {
                cashAdvanceCustomFields = cashAdvanceCFManager.get(customFieldItems.get(0).getObjectId());
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
                cashAdvanceCustomFields = new EdsCashAdvanceCustomFields();
                cashAdvanceCFManager.create(cashAdvanceCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(cashAdvanceCustomFields, customFieldItems);
            return cashAdvanceCustomFields;
        }
        return null;
    }

    private void saveCashAdvanceAttachments(FileItem[] attachments, EdsCashAdvance cashAdvance) {
        attachmentUtilsManager.saveAttachments(F_CASH_ADVANCE, cashAdvance.getObjectID(), cashAdvance.getObjectID(), attachments);
    }

    public void approvedActionForCashAdvance(Integer cashAdvanceId, boolean createTransaction) {
        EdsCashAdvance cashAdvance = cashAdvanceManager.get(cashAdvanceId);
        if (createTransaction) {
            String disablePayrollTransactions = getCompanyPayrollSettings(DISABLE_PAYROLL_TRANSACTIONS);
            if (disablePayrollTransactions == null || "false".equals(disablePayrollTransactions)) {
                EdsPaymentDeduction paymentDeduction = paymentDeductionManager.getDeductionOrLoanByCashAdvanceID(cashAdvance.getObjectID());
                if (paymentDeduction == null) {
                    paymentDeduction = new EdsPaymentDeduction();
                }
                paymentDeduction.setEmployeeId(cashAdvance.getEmployee().getObjectID());
                if (cashAdvance.getCategory() != null) {
                    paymentDeduction.setCategoryId(cashAdvance.getCategory().getObjectID());
                } else {
                    paymentDeduction.setCategoryId(categoryManager.getCategoryByCode(CASH_ADVANCE).getObjectID());
                }
                paymentDeduction.setCashAdvanceID(cashAdvance.getObjectID());
                if ("Deduction".equals(cashAdvance.getType())) {
                    paymentDeduction.setPaymentAmount(cashAdvance.getTotalAmount());
                } else {
                    paymentDeduction.setStartDate(cashAdvance.getTransactionDate());
                    paymentDeduction.setTotalAmount(cashAdvance.getTotalAmount());
                    paymentDeduction.setPaymentAmount(cashAdvance.getPaymentAmount());
                }
                paymentDeduction.setRecurring(true);
                paymentDeductionManager.createOrUpdate(paymentDeduction);
                createTransactionForCashAdvance(cashAdvance);
            }
        } else {
            EdsPaymentDeduction paymentDeduction = paymentDeductionManager.getDeductionOrLoanByCashAdvanceID(cashAdvance.getObjectID());
            if (paymentDeduction == null) {
                paymentDeduction = new EdsPaymentDeduction();
            }
            paymentDeduction.setEmployeeId(cashAdvance.getEmployee().getObjectID());
            if (cashAdvance.getCategory() != null) {
                paymentDeduction.setCategoryId(cashAdvance.getCategory().getObjectID());
            } else {
                paymentDeduction.setCategoryId(categoryManager.getCategoryByCode(CASH_ADVANCE).getObjectID());
            }
            paymentDeduction.setCashAdvanceID(cashAdvance.getObjectID());
            if ("Deduction".equals(cashAdvance.getType())) {
                paymentDeduction.setPaymentAmount(cashAdvance.getTotalAmount());
            } else {
                paymentDeduction.setStartDate(cashAdvance.getTransactionDate());
                paymentDeduction.setTotalAmount(cashAdvance.getTotalAmount());
                paymentDeduction.setPaymentAmount(cashAdvance.getPaymentAmount());
            }
            paymentDeduction.setRecurring(true);
            paymentDeductionManager.create(paymentDeduction);
        }
        addCashAdvanceToSolr(cashAdvance);
    }

    private void createTransactionForCashAdvance(EdsCashAdvance cashAdvance) {
        if (transactionManager.getTransactionByCashAdvance(cashAdvance.getObjectID()) == null) {
            EdsUser user = transactionManager.getUser();
            BigDecimal exchangeRate = cashAdvance.getExchangeRate() != null ? cashAdvance.getExchangeRate() : BigDecimal.ONE;
            EdsCashAdvanceTransaction cashAdvanceTransaction = new EdsCashAdvanceTransaction();
            cashAdvanceTransaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID() + 1);
            cashAdvanceTransaction.setCashAdvance(cashAdvance);
            cashAdvanceTransaction.setJournalDate(cashAdvance.getTransactionDate());
            cashAdvanceTransaction.setName("Cash Advance for " + cashAdvance.getEmployee().getFullName());
            cashAdvanceTransaction.setPostedBy(user);
            cashAdvanceTransaction.setPostedDate(user.getCompany().getCompanyDate());

            EdsTransactionItem debitItem = new EdsTransactionItem();
            debitItem.setAccount(cashAdvance.getCashAccount());
            debitItem.setDebit(cashAdvance.getTotalAmount().divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
            cashAdvanceTransaction.addTransactionItem(debitItem);

            EdsTransactionItem creditItem = new EdsTransactionItem();
            creditItem.setAccount(cashAdvance.getAccount());
            creditItem.setCredit(cashAdvance.getTotalAmount().divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
            cashAdvanceTransaction.addTransactionItem(creditItem);

            transactionManager.create(cashAdvanceTransaction);
        }
    }

    @Override
    public EosReportData getEmployeeEosDataList(ListingFilterParameter lfp) {
        EoSCalculationData data;
        List<EdsEmployee> employeeList;
        EndOfServiceSettings settings = endOfServiceManager.getEndOfServiceSettings(lfp.getCountryCode());
        if (settings == null) {     //DEFAULT settings
            settings = new EndOfServiceSettings();
            settings.setPayFrom(0);
            settings.setIncludeLeaveAllowances(true);
            settings.setIncludeBenefitPayments(true);
            settings.setFromLastPayment(false);
            settings.setFromAllAllowances(false);
        }
        ArrayList<EoSCalculationData> result = new ArrayList<>();
        lfp.setResignedEmployeesIncluded(true);
        employeeList = employeeManager.empListForEOS(lfp);
        int totalCount = employeeList.size();
        if (lfp.getLimit() > 0) {
            employeeList = ListUtils.getSublist(employeeList, lfp.getStart(), lfp.getLimit());
        }
        boolean isDailyRateByEmployerSettings = "true".equals(getCompanyPayrollSettings(DAILY_RATE_BY_EMPLOYER_SETTINGS)) || "BY_STATIC_DAY".equals(getCompanyPayrollSettings(DAILY_RATE_BY_EMPLOYER_SETTINGS));
        Double numberOfWorkDay = null;
        if (isDailyRateByEmployerSettings) {
            numberOfWorkDay = Double.parseDouble(getCompanyPayrollSettings(NUMBER_OF_WORK_DAYS, DEFAULT_NUMBER_OF_WORK_DAYS.toString()));
        }
        int totalDays;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        int currentYear = Integer.parseInt(dateFormat.format(new Date()));
        for (EdsEmployee employee : employeeList) {
            totalDays = 0;
            data = new EoSCalculationData();

            EdsEmployeePayrollSettings employeePayrollSettings = employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), SALARY);
            EdsEmployeePayrollSettings employeeContractStartDate = employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), CONTRACT_START_DATE);
            EdsEmployeePayrollSettings employeeContractEndDate = employeePayrollSettingsManager.getEmployeeSettingValue(employee.getObjectID(), CONTRACT_END_DATE);
            if (employeeContractStartDate != null) {
                if (employeeContractStartDate.getValue() != null && !employeeContractStartDate.getValue().isEmpty()) {
                    if (employeeContractEndDate != null && employeeContractEndDate.getValue() != null && !employeeContractEndDate.getValue().isEmpty()) {
                        data.setEmployeeContractType(1);
                    } else {
                        data.setEmployeeContractType(0);
                    }
                } else {
                    data.setEmployeeContractType(0);
                }
            } else {
                data.setEmployeeContractType(0);
            }
            data.setHireDate(new DateNonConvertable(employee.getStartDate()));
            data.setResignationDate(employee.getEndDate() != null ? new DateNonConvertable(employee.getEndDate()) : null);
            data.setEmployee(new SelectItem(employee.getObjectID(), employee.getFullName(), employee.getProfile() != null ? employee.getProfile().getEmployeeCode() : null));
            data.setEmployeeSalaryCurrency(employee.getSalaryCurrency() != null ? employee.getSalaryCurrency().getName() : null);
            data.setBasicSalary(new BigDecimal(employeePayrollSettings.getValue()));
            if (employee.getEndDate() == null) {
                totalDays += DateUtil.countDays(employee.getStartDate(), ServerUtils.parseFilterParameterDate(lfp.getStartDateNC()));
            } else {
                totalDays += DateUtil.countDays(employee.getStartDate(), employee.getEndDate());
            }
            lfp.setEmployeeId(employee.getObjectID());
            lfp.setType(NONE);
            List<EdsSickRequest> nonPaidSickRequests = sickRequestManager.getNonPaidLeaveRequests(lfp);
            for (EdsSickRequest request : nonPaidSickRequests) {
                if (!request.isTakeByMoney() && request.getStartDate() != null && request.getEndDate() != null) {
                    totalDays -= countLeaveRequestDays(request).intValue();//DateUtil.countDays(request.getStartDate(), request.getEndDate());
                }
            }
            data.setNumberOfWorkDay(numberOfWorkDay);
            data.setTotalWorkedDays(totalDays);
            if (settings.isIncludeLeaveAllowances()) {
                List<EdsAnnualLeaveAllowance> leaveAllowanceList = annualLeaveAllowanceManager.getLeaveAllowance(currentYear, Collections.singletonList(employee.getObjectID()));
                ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
                listingFilterParameter.setYear(currentYear);
                listingFilterParameter.setEmployeeId(employee.getObjectID());
                double payDays = 0.00;
                for (EdsAnnualLeaveAllowance annualLeaveAllowance : leaveAllowanceList) {
                    listingFilterParameter.setReasonID(annualLeaveAllowance.getReason().getObjectID());
                    Double[] stats = attendanceRawDataManager.getLeaveRequestMinutes(lfp);
                    double usedDays = stats[DAYS_FROM_ANNUAL] != null ? stats[DAYS_FROM_ANNUAL] : 0;
                    int annualAllowanceMinutes = annualLeaveAllowance.getAnnualAllowanceMinutes();
                    double annualAllowanceDays = (double) annualAllowanceMinutes / ServerUtils.getDailyAverageTimeslotMinutes(employee.getTimeSlot().getItems());
                    if (usedDays == 0) {
                        payDays += annualAllowanceDays;
                    } else if (annualAllowanceDays >= usedDays) {
                        payDays += annualAllowanceDays - usedDays;
                    }
                }
                data.setLeftLeaveDays(BigDecimal.valueOf(payDays));
                BigDecimal dailyRate = data.getBasicSalary().divide(BigDecimal.valueOf(30), 2, RoundingMode.HALF_UP);
                data.setLeaveAllowanceTotal(dailyRate.multiply(data.getLeftLeaveDays()));
            } else {
                data.setLeaveAllowanceTotal(BigDecimal.ZERO);
            }
            if (settings.isIncludeBenefitPayments()) {
                BigDecimal benefitTotal = employeeBenefitAllowanceManager.getBenefitPaymentForEndOfServiceCalculation(ServerUtils.getYear(new Date()), employee.getObjectID());
                data.setBenefitPaymentTotal(benefitTotal);
            } else {
                data.setBenefitPaymentTotal(BigDecimal.ZERO);
            }
            if (settings.isFromAllAllowances()) {
                List<Object[]> payslipItemData = payslipPaymentsManager.getEosDataFromPaylipTableItem(employee.getObjectID());
                BigDecimal lastPaymentsTotal = BigDecimal.ZERO;
                if (payslipItemData.size() > 0) {
                    Object[] newData = payslipItemData.get(0);
                    Date newPayslipItemDate = (Date) newData[1];
                    if (newPayslipItemDate != null && newData[0] != null) {
                        lastPaymentsTotal = payslipTableItemManager.getPayslipItemPaymentsTotal((Integer) newData[0], "Payment");
                    }

                }
                data.setLastPaymentsTotal(lastPaymentsTotal);
            } else if (settings.getCategories() != null && settings.getCategories().size() > 0) {
                StringBuilder categories = new StringBuilder();
                String categoryString;
                for (EdsPayrollCategory category : settings.getCategories()) {
                    categories.append("'").append(category.getCode()).append("',");
                }
                categoryString = categories.substring(0, categories.toString().length() - 1);
                if (settings.isFromLastPayment()) {
                    List<Object[]> payslipItemData = payslipPaymentsManager.getEosDataFromPaylipTableItemByCategory(employee.getObjectID(), categoryString);
                    BigDecimal lastPaymentsTotal = BigDecimal.ZERO;
                    if (payslipItemData.size() > 0) {
                        Object[] newData = payslipItemData.get(0);
                        Date newPayslipItemDate = (Date) newData[1];
                        if (newPayslipItemDate != null && newData[0] != null) {
                            lastPaymentsTotal = payslipPaymentsManager.getPaymentAmount((Integer) newData[1], (Integer) newData[0]);
                        }
                    }
                    data.setLastPaymentsTotal(lastPaymentsTotal);
                } else {
                    data.setLastPaymentsTotal(paymentDeductionManager.getTotalPaymentByCategories(employee.getObjectID(), categoryString));
                }
            } else {
                data.setLastPaymentsTotal(BigDecimal.ZERO);
            }
            result.add(data);

        }
        return new EosReportData(result, totalCount);
    }

    // DOES NOT RETURN NULL
    private String getCompanyBankCodeOfCompanyPayrollSettings() {
        String bankCode = "";
        String bankAccountId_ = getCompanyPayrollSettings(BANK_ACCOUNT_ID, "0");
        String integerPattern = "^\\d+$";
        if (bankAccountId_.matches(integerPattern)) {
            int bankAccountId = Integer.parseInt(bankAccountId_);
            if (bankAccountId > 0) {
                EdsBankAccount bankAccount = bankAccountManager.get(bankAccountId);
                if (bankAccount != null) {
                    String agentId = bankAccount.getAgentID();
                    bankCode = agentId != null ? agentId : "";
                }
            }
        }
        return bankCode;
    }

    @Override
    public WpsReportData getWpsReportData(ListingFilterParameter lfp) {
        ArrayList<WpsReportItem> items = payslipTableManager.getWpsReportItems(lfp);
        WpsReportData wpsReportData = new WpsReportData(items, items.size());
        if (lfp.isFromExcelPDF()) {
            wpsReportData.setCompanyWpsNumber(getCompanyPayrollSettings(WPS_NO, ""));
            wpsReportData.setCompanyBankCode(getCompanyBankCodeOfCompanyPayrollSettings());
            if (lfp.getObjectId() != null) {
                EdsPayslipTable payslipTable = payslipTableManager.get(lfp.getObjectId());
                wpsReportData.setMonth(payslipTable.getMonth());
                wpsReportData.setMonthId(payslipTable.getMonthID() + 1);
                wpsReportData.setYear(payslipTable.getYear());
            } else {
                wpsReportData.setMonth(lfp.getMonthName());
                wpsReportData.setMonthId(lfp.getSelectedMonth() + 1);
                wpsReportData.setYear(lfp.getYear());
            }
        }
        return wpsReportData;
    }

    @Override
    public HashMap<SelectItem, SelectItem[]> getYearMonthsForWps() {
        return payslipTableItemManager.getYearMonthsForWps();
    }

    @Override
    public boolean deleteCashAdvance(Integer objectID) {
        EdsPaymentDeduction linkedDeductionOrLoan;
        EdsCashAdvance cashAdvance = cashAdvanceManager.get(objectID);
        if (cashAdvance != null) {
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setObjectId(objectID);
            List<EdsPayslipPayments> payments = payslipPaymentsManager.getCashAdvancePayments(filter);
            if (payments != null && !payments.isEmpty()) {
                return false;
            }

            EdsCashAdvanceTransaction cashAdvanceTransaction = transactionManager.getTransactionByCashAdvance(objectID);
            if (cashAdvanceTransaction != null) {
                cashAdvanceTransaction.setDeleted(true);
                transactionManager.update(cashAdvanceTransaction);
            }
            approverManager.deletedAprovers(RelationItem.TYPE_CASH_ADVANCE, cashAdvance.getObjectID());

            baseEventsPostProcessor.registerEvent(CashAdvanceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, cashAdvance, userManager.getUser());

            EdsBusinessEvent workflowRule = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, cashAdvance, userManager.getUser());
            workflowRule.setEntityType(RelationItem.TYPE_CASH_ADVANCE);
            cashAdvance.setDeleted(true);
            cashAdvance.setLastUpdateTime(new Date());
            linkedDeductionOrLoan = paymentDeductionManager.getDeductionOrLoanByCashAdvanceID(objectID);
            if (linkedDeductionOrLoan != null) {
                linkedDeductionOrLoan.setDeleted(true);
                paymentDeductionManager.update(linkedDeductionOrLoan);
            }
            removeCashAdvanceFromSolr(cashAdvance);
        }
        return true;
    }

    @Override
    public void deleteEndOfServiceGratuity(Integer objectID) {
        P11 payslip;
        EdsPayslipTableItem singlePayrun;
        EdsEosCalculation eosCalculation = endOfServiceGratuityManager.get(objectID);
        if (eosCalculation != null) {
            eosCalculation.setDeleted(true);
            payslip = eosCalculation.getPayslip();
            singlePayrun = eosCalculation.getSinglePayrun();
            if (payslip != null) {
                payslip.setDeleted(true);
                transactionManager.deleteTransactionsByPayslip(payslip.getObjectID());
                p11Manager.update(payslip);
            }
            if (singlePayrun != null) {
                singlePayrun.setDeleted(true);
                singlePayrun.setLastUpdateTime(new Date());
                transactionManager.deleteTransactionsByPayrun(singlePayrun.getObjectID());
                removeSinglePayrunFromSolr(singlePayrun);
            }
            endOfServiceGratuityManager.update(eosCalculation);
        }
    }

    @Override
    public EoSCalculationData getEndOfServiceGratuity(Integer objectID) {
        EoSCalculationData result = new EoSCalculationData();
        EdsEosCalculation eosCalculation = endOfServiceGratuityManager.get(objectID);
        if (eosCalculation != null) {
            result.setObjectID(objectID);
            result.setTotalWorkedDays(eosCalculation.getTotalWorkedDays());
            result.setHireDate(new DateNonConvertable(eosCalculation.getHireDate()));
            result.setResignationDate(new DateNonConvertable(eosCalculation.getResignationDate()));
            result.setEosAmount(eosCalculation.getTotalAmount());
            result.setReasonCode(eosCalculation.getReasonCode());
            result.setDate(new DateNonConvertable(eosCalculation.getCreationDate()));
            if (eosCalculation.getEmployee() != null) {
                result.setEmployee(new SelectItem(eosCalculation.getEmployee().getObjectID(), eosCalculation.getEmployee().getFullName()));
                result.setEmployeeSalaryCurrency(eosCalculation.getEmployee().getSalaryCurrency() != null ? eosCalculation.getEmployee().getSalaryCurrency().getName() : null);
            }
            //set numbering
            BankTransferNumberData transferNumberData = new BankTransferNumberData();
            getEndOfServiceNumberData(transferNumberData);
            result.setPaymentNumber(transferNumberData.getTransferNumber());
        }
        return result;
    }

    @Override
    public void deleteSinglePayrun(Integer objectID, Integer employeeID) {
        EdsPayslipTableItem singlePayrun = payslipTableItemManager.get(objectID);
        if (singlePayrun == null) {
            return;
        }
        if (singlePayrun.getPayslipTable() != null) {
            boolean deleteGroupPayrun = payslipTableItemManager.isLastItemInGroupPayrun(singlePayrun.getPayslipTable().getObjectID());
            if (deleteGroupPayrun) {
                EdsPayslipTable payslipTable = singlePayrun.getPayslipTable();
                payslipTable.setDeleted(true);
                transactionManager.deleteTransactionsByPayslipTable(payslipTable.getObjectID());
                if (!CollectionUtils.isEmpty(payslipTable.getPayments())) {
                    payslipTable.getPayments().forEach(payment -> deletePayrunPayment(payment.getObjectID()));
                }

                payslipTable.setLastUpdateTime(new Date());
                EdsUser user = userManager.getUser();
                if (user != null) {
                    payslipTable.setUpdator(user);
                }
                payslipTableManager.update(payslipTable);
                removeGroupPayrunFromSolr(payslipTable);
            }
        }
        transactionManager.deleteTransactionsByPayrun(objectID);

        if (!CollectionUtils.isEmpty(singlePayrun.getPaymentItems())) {
            singlePayrun.getPaymentItems().forEach(paymentItem -> deletePayrunPaymentItem(paymentItem.getObjectID()));
        }

        List<EdsPaymentDeduction> linkedCashAdvances = paymentDeductionManager.getSinglePayrunCashAdvanceDeductions(objectID);
        payslipPaymentsManager.deleteByPayslipItemID(objectID);
        checkExpensesForSinglePayrunDelete(objectID);
        checkCashAdvancesForSinglePayrunDelete(linkedCashAdvances);
        baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, singlePayrun, userManager.getUser());
        singlePayrun.setDeleted(true);
        singlePayrun.setLastUpdateTime(new Date());
        payslipTableItemManager.update(singlePayrun);
        removeSinglePayrunFromSolr(singlePayrun);
    }

    private void checkCashAdvancesForSinglePayrunDelete(List<EdsPaymentDeduction> linkedCashAdvances) {

        for (EdsPaymentDeduction paymentDeduction : linkedCashAdvances) {
            paymentDeduction.setRecurring(true);
            if (paymentDeduction.getCashAdvanceID() != null) {
                checkCashAdvanceForFullyPaid(paymentDeduction.getObjectID(), paymentDeduction.getCashAdvanceID());
            }
            paymentDeductionManager.update(paymentDeduction);
        }
    }

    private void checkExpensesForSinglePayrunDelete(Integer objectID) {
        List<EdsExpenseReport> linkedExpenses = expenseReportManager.getPayslipTableItemRelatedExpenseClaims(objectID);
        EdsReference paidStatus = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_PAID);
        EdsReference approvedStatus = referenceManager.findReference(EXPENSE_STATUS, EXPENSE_APPROVED);
        EdsReference partiallyPaid = referenceManager.findReference(EXPENSE_STATUS, PARTIALLY_PAID);
        for (EdsExpenseReport expenseReport : linkedExpenses) {
            expenseReport.setPayslipTableItemID(null);
            expenseReport.setEntityStatus(approvedStatus);
            expenseReport.setAccount(null);
            List<EdsExpensePayment> expensePayments = expenseReport.getPayments();
            if (expensePayments != null && expensePayments.size() > 0) {
                for (EdsExpensePayment ep : expensePayments) {
                    if (ep.getPayslipTableItem() != null && ep.getPayslipTableItem().getObjectID().equals(objectID)) {
                        transactionManager.deleteExpensePaymentTransaction(ep);
                        ep.setDeleted(true);
                        expensePaymentManager.update(ep);
                    }
                }
                if (expenseReport.getPayments() != null && expenseReport.getPayments().size() > 0) {
                    BigDecimal paidTotal = getPaidTotal(expenseReport);
                    double dueAmount = expenseReport.getTotal().doubleValue() - paidTotal.doubleValue();
                    if (BigDecimal.valueOf(dueAmount).setScale(5, RoundingMode.HALF_UP).doubleValue() <= 0.01) {
                        expenseReport.setEntityStatus(paidStatus);
                    } else {
                        expenseReport.setEntityStatus(partiallyPaid);
                    }
                }
            }
            try {
                expenseReportClaimsSolrComponent.index(expenseReport);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            expenseReportManager.update(expenseReport);
        }
    }

    private BigDecimal getPaidTotal(EdsExpenseReport ep) {
        List<EdsExpensePayment> payments = expensePaymentManager.getPayments(ep);
        BigDecimal totalPaid = ZERO;
        for (EdsExpensePayment p : payments) {
            totalPaid = totalPaid.add(p.getAmount());
        }
        return totalPaid.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public PayrollGlobalSettingsData getPaymentDeductionSettingsData(Integer objectID) {
        PayrollGlobalSettingsData result = null;
        PaymentDeductionObject resultItem;
        String enabledMultiCurrency = getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL);
        EdsPayrollGlobalSettings settings = payrollGlobalSettingsManager.get(objectID);
        if (settings != null) {
            result = new PayrollGlobalSettingsData();
            result.setObjectID(objectID);
            result.setName(settings.getName());
            result.setCategoryType(settings.getCategoryType());
            result.setSettingsType(settings.getSettingsType());
            result.setBatchItem(settings.getPayrollBatch() != null ? settings.getPayrollBatch().asSelectItem() : null);
            if (settings.getEmployees() != null) {
                List<Integer> employeeIds = new ArrayList<>();
                for (EdsEmployee employee : settings.getEmployees()) {
                    employeeIds.add(employee.getObjectID());
                }
                result.setSelectedEmployeeIds(employeeIds.toArray(new Integer[employeeIds.size()]));
            }
            result.setRateType(settings.getRateType());
            result.setSalary(settings.getSalary());
            result.setEnabledMultiCurrency("true".equals(enabledMultiCurrency));
            result.setCurrencies(currencyService.getCurrencies(true));
            result.setCurrency(settings.getCurrency() != null ? settings.getCurrency().createCurrencyItem() : null);
            if (settings.getSalaryCategoryId() != null) {
                EdsPayrollCategory salaryCategory = categoryManager.get(settings.getSalaryCategoryId());
                if (salaryCategory != null) {
                    result.setSalaryCategory(salaryCategory.createPaymentDeductionSelectItem());
                }
            }
            if (settings.getItems() != null) {
                for (EdsPayrollGlobalSettingsItem item : settings.getItems()) {
                    switch (item.getCategory().getCode()) {
                        case REGULAR_OVERTIME -> {
                            result.setRegularOvertimeRate(item.getAmount());
                            result.setRegularOvertimeRateType(item.getPayType() != null && item.getPayType() == 1 ? PERCENTAGE : FIXED);
                        }
                        case WEEKEND_OVERTIME -> {
                            result.setWeekendOvertimeRate(item.getAmount());
                            result.setWeekendOvertimeRateType(item.getPayType() != null && item.getPayType() == 1 ? PERCENTAGE : FIXED);
                        }
                        case HOLIDAY_OVERTIME -> {
                            result.setHolidayOvertimeRate(item.getAmount());
                            result.setHolidayOvertimeRateType(item.getPayType() != null && item.getPayType() == 1 ? PERCENTAGE : FIXED);
                        }
                        default -> {
                            resultItem = new PaymentDeductionObject();
                            resultItem.setId(item.getObjectID());
                            resultItem.setType(item.getPayType());
                            resultItem.setPaymentType(item.getPaymentType());
                            resultItem.setFromAllAllowances(item.getFromAllAllowances());
                            if (resultItem.getType() == 0) {
                                resultItem.setPaymentAmount(item.getAmount());
                            } else {
                                resultItem.setPercentage(item.getAmount());
                            }
                            if (item.getCategory() != null) {
                                resultItem.setCategoryItem(item.getCategory().createPaymentDeductionSelectItem());
                            }
                            if (item.getLinkedCategories() != null && item.getLinkedCategories().size() > 0) {
                                PaymentDeductionObject linkedObject;
                                for (final EdsPayrollCategory linkedCategory : item.getLinkedCategories()) {
                                    linkedObject = new PaymentDeductionObject();
                                    linkedObject.setCategoryItem(linkedCategory.createPaymentDeductionSelectItem());
                                    resultItem.getLinkedCategories().add(linkedObject);
                                }
                            }
                            if (item.getCategory().getType() != null && item.getCategory().getType().equals(EdsPayrollCategory.PAYMENT)) {
                                result.getPayments().add(resultItem);
                            } else if (item.getCategory().getType().equals(EdsPayrollCategory.DEDUCTION)) {
                                result.getDeductions().add(resultItem);
                            } else if (item.getCategory().getType().equals(EdsPayrollCategory.TAX)) {
                                result.getTaxes().add(resultItem);
                            } else if (item.getCategory().getType().equals(EdsPayrollCategory.EMPLOYER_CONTRIBUTION)) {
                                result.getEmployerContributions().add(resultItem);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public ArrayList<PensionContributionData> getPensionContributionByFilter(Integer month, Integer year) {
        return payslipTableItemManager.getPensionContributions(month, year);
    }

    @Override
    public void deletePaymentDeductionSettings(Integer settingsID) {
        EdsPayrollGlobalSettings settings = payrollGlobalSettingsManager.get(settingsID);
        if (settings != null) {
            settings.setDeleted(true);
            if (settings.getItems() != null) {
                for (EdsPayrollGlobalSettingsItem item : settings.getItems()) {
                    if (item.getCategories() != null) {
                        for (EdsPaymentDeduction paymentDeduction : item.getCategories()) {
                            paymentDeduction.setRecurring(false);
                            paymentDeductionManager.update(paymentDeduction);
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Integer saveSinglePayrun(SinglePayrunItem data) {
        EdsPayslipTableItem singlePayrun;
        if (data.getObjectID() == null) {
            singlePayrun = new EdsPayslipTableItem();
            singlePayrun.setPreparer(employeeManager.get(data.getCreator().getId()));
            singlePayrun.setCreationDate(data.getCreationDate().getNonConvertedDate());
        } else {
            singlePayrun = payslipTableItemManager.get(data.getObjectID());
        }
        EdsPayslipTable payslipTable = singlePayrun.getPayslipTable();
        singlePayrun.setEmployee(employeeManager.get(data.getEmployeeID()));
        singlePayrun.setFromDate(data.getFromDate() != null ? data.getFromDate().getNonConvertedDate() : null);
        singlePayrun.setToDate(data.getToDate() != null ? data.getToDate().getNonConvertedDate() : null);
        singlePayrun.setProcessDate(data.getProcessDate() != null ? data.getProcessDate().getNonConvertedDate() : null);
        singlePayrun.setDaysWorked(data.getDaysWorked());
        singlePayrun.setBasicSalary(data.getBasicSalary());
        singlePayrun.setDailyRate(data.getDailyRate());
        singlePayrun.setActualMonthPay(data.getActualMonthPay());
        singlePayrun.setAllowance(data.getAllowance());
        singlePayrun.setAdditionalPay(data.getAdditionalPay());
        singlePayrun.setDeduction(data.getDeduction());
        singlePayrun.setTax(data.getTax());
        singlePayrun.setEmployerContribution(data.getEmployerContribution());
        singlePayrun.setExpense(data.getExpense());
        singlePayrun.setDescription(data.getDescription());
        if (data.getProjectItem() != null) {
            singlePayrun.setProject(projectManager.get(data.getProjectItem().getId()));
        }
        if (data.getPayMethodId() != null) {
            EdsPaymentMethod edsPaymentMethod = paymentMethodManager.get(data.getPayMethodId());
            if (edsPaymentMethod != null) {
                singlePayrun.setPaymentMethod(edsPaymentMethod);
            }
        }

        if (payslipTable != null) {
            if (singlePayrun.getTotal() != null && data.getTotal() != null) {
                BigDecimal difference = singlePayrun.getTotal().subtract(data.getTotal());
                payslipTable.setTotalAmount(payslipTable.getTotalAmount().subtract(difference));
            }
            if (singlePayrun.getTotalInBase() != null && data.getTotalInBase() != null) {
                BigDecimal differenceInBase = singlePayrun.getTotalInBase().subtract(data.getTotalInBase());
                payslipTable.setTotalInBase(payslipTable.getTotalInBase().subtract(differenceInBase));
            }
            payslipTableManager.createOrUpdate(payslipTable);
        }
        singlePayrun.setTotal(data.getTotal());
        singlePayrun.setTotalInBase(data.getTotalInBase());


        singlePayrun.setExchangeRate(data.getExchangeRate());
        singlePayrun.setPensionRate(data.getPensionRate());
        singlePayrun.setPensionValueType(data.getPensionValueType());
        singlePayrun.setPensionAmount(data.getPensionAmount());
        singlePayrun.setCompanyPensionAmount(data.getCompanyPensionAmount());
        singlePayrun.setCompanyPensionRate(data.getCompanyPensionRate());
        singlePayrun.setCompanyNonLocalPensionRate(data.getCompanyNonLocalPensionRate());
        singlePayrun.setCompanyPensionType(data.getCompanyPensionType());
        singlePayrun.setNonLocalPensionRate(data.getNonLocalPensionRate());
        singlePayrun.setPaymentPolicy(data.getPaymentPolicy());
        singlePayrun.setPdfTemplateID(data.getPdfTemplateID());
        singlePayrun.setCurrency(data.getCurrency() != null ? currencyManager.get(data.getCurrency().getId()) : null);
        if (data.getStatus() != null) {
            singlePayrun.setStatus(referenceManager.findReference(PAYRUN_STATUS, data.getStatus()));
        }
        singlePayrun.setApprover(employeeManager.get(data.getApprover().getId()));
        if (data.getApprover2() != null) {
            singlePayrun.setApprover2(employeeManager.get(data.getApprover2().getId()));
        }
        if (data.getStatus() != null && PAYRUN_STATUS_APPROVED.equals(data.getStatus())) {
            singlePayrun.setApprovedDate(data.getApprovedDate().getNonConvertedDate());
            if (data.getTotal().compareTo(BigDecimal.ZERO) == 0) {
                singlePayrun.setStatus(referenceManager.findReference(PAYRUN_STATUS, PAYRUN_STATUS_PAID));
            }
        }
        singlePayrun.setMonthID(data.getMonthID());
        singlePayrun.setMonth(data.getMonth());
        singlePayrun.setYear(data.getYear());
        singlePayrun.setFrequency(data.getFrequency());
        singlePayrun.setLastUpdateTime(new Date());

        singlePayrun.setCustomFields(createCustomFields(data.getCustomFieldItems()));

        payslipTableItemManager.createOrUpdate(singlePayrun);
        registerPaymentDeductionCategories(data, singlePayrun);
        registerEmployeeExpenses(data.getEmployeeExpenses(), singlePayrun);

        if (data.getObjectID() == null) {
            baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, singlePayrun, userManager.getUser());
        } else {
            baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, singlePayrun, userManager.getUser());
        }

        if (data.getStatus() != null && PAYRUN_STATUS_SUBMITTED.equals(data.getStatus())) {
            try {
                messageManager.sendSinglePayrunToManager(singlePayrun);
                baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, singlePayrun, userManager.getUser());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String disablePayrollTransactions = getCompanyPayrollSettings(DISABLE_PAYROLL_TRANSACTIONS);

        if (data.getStatus() != null && PAYRUN_STATUS_APPROVED.equals(data.getStatus())) {
            if (disablePayrollTransactions == null || "false".equals(disablePayrollTransactions)) {
                createTransactionForSinglePayrun(singlePayrun);
            }
            if (data.sendNotification()) {
                try {
                    messageManager.sendSinglePayrunToEmployee(singlePayrun);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        addSinglePayrunToSolr(singlePayrun);
        if (payslipTable != null) {
            addGroupPayrunToSolr(payslipTable);
        }
        return singlePayrun.getObjectID();
    }

    @Transactional
    public EdsPayrollCustomFields createCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsPayrollCustomFields customFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                customFields = payrollCFManager.get(customFieldItems.get(0).getObjectId());
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
                customFields = new EdsPayrollCustomFields();
                payrollCFManager.create(customFields);
            }
            CustomFieldsUtils.setAccountingDomainObjectCustomFields(customFields, customFieldItems);
            return customFields;
        }
        return null;
    }

    private void addSinglePayrunToSolr(EdsPayslipTableItem... singlePayruns) {
        try {
            singlePayrunSolrComponent.indexes(Arrays.asList(singlePayruns));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void addSinglePayrunToSolr(Integer payslipTableId) {
        if (payslipTableId == null) {
            return;
        }
        EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(payslipTableId);

        try {
            singlePayrunSolrComponent.index(payslipTableItem);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void approveSinglePayrun(SinglePayrunItem data) {
        EdsPayslipTableItem singlePayrun = payslipTableItemManager.get(data.getObjectID());
        boolean isSecondApproverEnabled = singlePayrun.getApprover2() != null;
        boolean isFirstApproved = false;
        singlePayrun.setProcessDate(data.getProcessDate() != null ? data.getProcessDate().getNonConvertedDate() : null);
        singlePayrun.setApprovedDate(data.getApprovedDate().getNonConvertedDate());

        EdsReference status = referenceManager.findReference(PAYRUN_STATUS, data.getStatus());
        EdsReference paidStatus = referenceManager.findReference(PAYRUN_STATUS, PAYRUN_STATUS_PAID);
        if (isSecondApproverEnabled) {
            isFirstApproved = singlePayrun.getStatus() != null && PAYRUN_STATUS_APPROVED.equals(singlePayrun.getStatus().getCode());
            if (isFirstApproved) {
                if (singlePayrun.getTotal().compareTo(BigDecimal.ZERO) == 0) {
                    singlePayrun.setStatus2(paidStatus);
                } else {
                    singlePayrun.setStatus2(status);
                }
            } else {
                if (singlePayrun.getTotal().compareTo(BigDecimal.ZERO) == 0) {
                    singlePayrun.setStatus(paidStatus);
                } else {
                    singlePayrun.setStatus(status);
                }
            }
        } else {
            if (singlePayrun.getTotal().compareTo(BigDecimal.ZERO) == 0) {
                singlePayrun.setStatus(paidStatus);
            } else {
                singlePayrun.setStatus(status);
            }
        }
        singlePayrun.setLastUpdateTime(new Date());

        if (isSecondApproverEnabled) {
            if (isFirstApproved && PAYRUN_STATUS_APPROVED.equals(data.getStatus2())) {
                createTransactionForSinglePayrun(singlePayrun);
                if (data.sendNotification()) {
                    try {
                        messageManager.sendSinglePayrunToEmployee(singlePayrun);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            createTransactionForSinglePayrun(singlePayrun);
            if (data.sendNotification()) {
                try {
                    messageManager.sendSinglePayrunToEmployee(singlePayrun);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        addSinglePayrunToSolr(singlePayrun);
        baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, singlePayrun, userManager.getUser());
    }

    private ListingFilterParameter getNonPaidLeaveFilter(ListingFilterParameter lfp) {
        ListingFilterParameter sickLeaveFilter = new ListingFilterParameter();
        sickLeaveFilter.setStartDate(DateUtil.getYearFirstDay(lfp.getStartDate()));
        sickLeaveFilter.setEndDate(DateUtil.getYearLastDay(lfp.getStartDate()));
        sickLeaveFilter.setEmployeeId(lfp.getEmployeeId());
        sickLeaveFilter.setExcludedType(LEAVE_REQUEST_TYPE);
        sickLeaveFilter.setReasonCode(LR_TYPE_SICK_LEAVE);
        return sickLeaveFilter;
    }

    @Override
    public Boolean hasPaymentItems(Integer singlePayrunID) {
        EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(singlePayrunID);
        if (payslipTableItem.getStatus() != null && PAYRUN_STATUS_PAID.equals(payslipTableItem.getStatus().getCode())
                && payslipTableItem.getPaymentItems() != null && !CollectionUtils.isEmpty(payslipTableItem.getPaymentItems())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    };


    @Override
    public SinglePayrunItem getSinglePayrunData(PayslipItemFilter filter) {
        SinglePayrunItem result;
        BigDecimal numberOfWorkDay;
        ListingFilterParameter lfp;
        EdsCompany company = userManager.getUser().getCompany();

        result = new SinglePayrunItem();
        if (filter.getEmployeeID() != null) {
            result.setPayedPayslipDataList(payslipTableItemManager.getPayedMonthList(filter.getObjectID(), filter.getEmployeeID()));
        }
        if (filter.getPeriodChecker() != null && result.getPayedPayslipDataList() != null && result.getPayedPayslipDataList().contains(filter.getPeriodChecker())) {
            result.setReturnMessage(DUPLICATE);
            return result;
        }
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        EdsCurrency baseCurrency = fs.getCurrency();
        Map<String, String> payrollSettings = companyPayrollSettingsManager.getCompanyPayrollSettingsMap(DAILY_RATE_BY_EMPLOYER_SETTINGS, DAILY_RATE_BY_EMPLOYER_SETTINGS, NUMBER_OF_WORK_DAYS,
                EXPENSE_PAID_ACCOUNT, PAYMENT_POLICY, ENABLED_DOUBLE_APPROVER_PAYRUN, DOUBLE_CONFIRMATION, BY_DEFAULT_EMAIL_NOTIFICATION, MULTI_CURRENCY_FOR_PAYROLL);

        if (filter.isFromChangeHandler()) {
            EdsEmployee employee = employeeManager.get(filter.getEmployeeID());
            if (employee.getAccountStatus() != null && EMPLOYEE_STATUS_RESIGNED.equals(employee.getAccountStatus().getCode())
                    && employee.getEndDate() != null) {
                if (DateUtil.compare(filter.getFromDate().getNonConvertedDate(), employee.getEndDate())) {
                    result.setReturnMessage(EMPLOYEE_STATUS_RESIGNED);
                    return result;
                } else if (DateUtil.compare(filter.getToDate().getNonConvertedDate(), employee.getEndDate())) {
                    filter.setToDate(new DateNonConvertable(employee.getEndDate()));
                }
            }

            ArrayList<Integer> employeeIds = new ArrayList<>(Collections.singletonList(filter.getEmployeeID()));
            boolean isEmployeeCodeInteger = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER);
            Map<String, PaymentDeductionSelectItem> categoryMap = categoryManager.getCategoryItemMapByCodes(LEAVE_DEDUCTIONS,
                    LEAVE_ENCHASHMENT,
                    BENEFIT_PAYMENT,
                    EXPENSE_REPORT,
                    REGULAR_OVERTIME,
                    WEEKEND_OVERTIME,
                    HOLIDAY_OVERTIME,
                    ADDITIONAL_PAYMENT,
                    ABSENCE_DEDUCTIONS,
                    BONUS);

            Map<String, String> settingsMap = companyPayrollSettingsManager.getCompanyPayrollSettingsMap(NON_PAID_LEAVE_DAYS_IMPACT,
                    LEAVE_DAYS_IMPACT,
                    DAILY_RATE_BY_EMPLOYER_SETTINGS,
                    ENABLED_LEAVE_DEDUCTIONS,
                    ENABLED_LEAVE_PAYMENTS,
                    NUMBER_OF_WORK_DAYS,
                    LEAVE_MONEY_TYPE_CATEGORY,
                    DEDUCT_TYPE,
                    DEDUCT_ALLOWANCES,
                    LEAVE_DAILY_PAYMENT_TYPE,
                    LEAVE_DAILY_ALLOWANCES,
                    LEAVE_MONEY_PAYMENT_TYPE,
                    LEAVE_MONEY_ALLOWANCES);
            EdsPayrollCategory leaveMTCategory = categoryManager.getCategoryByCode(LEAVE_SALARY);
            PaymentDeductionSelectItem leaveMTCategoryItem = leaveMTCategory != null ? leaveMTCategory.createPaymentDeductionSelectItem() : null;

            List<PaymentDeductionObject> leaveDeductionLinkedCategories = loadLeaveSettings(settingsMap.get(DEDUCT_ALLOWANCES));
            List<PaymentDeductionObject> leaveDailyTypeLinkedCategories = loadLeaveSettings(settingsMap.get(LEAVE_DAILY_ALLOWANCES));
            List<PaymentDeductionObject> leaveMoneyTypeLinkedCategories = loadLeaveSettings(settingsMap.get(LEAVE_MONEY_ALLOWANCES));

            boolean isLeaveSettingsCalculationEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SICK_LEAVE_SETTINGS_CALCULATION);
            Map<Integer, Double[]> spentMinutes = Maps.newHashMapWithExpectedSize(employeeIds.size());
            Map<Integer, Integer> annualLeaveAllowanceMap = Maps.newHashMapWithExpectedSize(employeeIds.size());

            if (isLeaveSettingsCalculationEnabled && filter.getYear() != null && filter.getMonth() != null) {

                Integer approvedStatusId = referenceManager.findReferenceId(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED);

                annualLeaveAllowanceMap.putAll(annualLeaveAllowanceManager.getLastYearMinutesMapByYearAndReasonAndEmployee(filter.getYear(),
                        EdsSickRequest.LR_TYPE_ANNUAL_LEAVE,
                        employeeIds));
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setYear(filter.getYear());
                fp.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
                fp.setStatusID(approvedStatusId);
                fp.setAnnualLeave(true);
                fp.setObjectIDs(employeeIds);

                spentMinutes.putAll(sickRequestDurationManager.getAllowanceSpentByEmployees(fp));
            }
            PayslipFilter payslipFilter = new PayslipFilter();
            payslipFilter.setFromDate(filter.getFromDate());
            payslipFilter.setToDate(filter.getToDate());
            Multimap<Integer, PaymentDeductionObject> paymentDeductionsMap = paymentDeductionManager.getEmployeesPaymentDeductionMap(employeeIds, payslipFilter);
            String[] settingsKeys = {RATE_TYPE,
                    SALARY,
                    REGULAR_OVERTIME_RATE,
                    REGULAR_OVERTIME_RATE_TYPE,
                    WEEKEND_OVERTIME_RATE,
                    WEEKEND_OVERTIME_RATE_TYPE,
                    HOLIDAY_OVERTIME_RATE,
                    HOLIDAY_OVERTIME_RATE_TYPE,
                    PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS,
                    PayrollConstants.MATERIAL_AID_TYPE_FUNERAL,
                    PayrollConstants.MATERIAL_AID_TYPE_GIFT
            };
            Table<Integer, String, String> employeeSettingsMap = employeePayrollSettingsManager.getEmployeesPayrollSettingMap(employeeIds, settingsKeys);

            ListingFilterParameter lp = new ListingFilterParameter();
            lp.setObjectIDs(employeeIds);
            lp.setStartDate(filter.getFromDate().getNonConvertedDate());
            lp.setEndDate(filter.getToDate().getNonConvertedDate());
            HashMap<Integer, List<SalaryHistory>> salaryHistoryMap = salaryHistoryManager.getEmployeeSalaryHistoryMap(lp);

            Integer baseCurrencyId = baseCurrency != null ? baseCurrency.getObjectID() : null;
            boolean hasCountry = company != null && company.getCountryZone() != null && company.getCountryZone().getCountry() != null;
            Integer countryId = hasCountry ? company.getCountryZone().getCountry().getObjectID() : null;
            String countryCode = hasCountry ? company.getCountryZone().getCountry().getCode() : "";

            filter.setEmployeeCodeInteger(isEmployeeCodeInteger);

            filter.setBaseCurrencyId(baseCurrencyId);
            filter.setCountryId(countryId);
            filter.setCountryCode(countryCode);

            filter.setSpentMinutes(spentMinutes.get(filter.getEmployeeID()));
            filter.setCategoryMap(categoryMap);
            filter.setCompanyPayrollSettingsMap(settingsMap);

            filter.setLeaveSettingsCalculationEnabled(isLeaveSettingsCalculationEnabled);
            filter.setLeaveMTCategoryItem(leaveMTCategoryItem);
            filter.setLeaveDailyTypeLinkedCategories(leaveDailyTypeLinkedCategories);
            filter.setLeaveDeductionLinkedCategories(leaveDeductionLinkedCategories);
            filter.setLeaveMoneyTypeLinkedCategories(leaveMoneyTypeLinkedCategories);

            filter.setSalaryHistories(salaryHistoryMap.get(filter.getEmployeeID()));
            filter.setEmployeeSettingsMap(employeeSettingsMap.row(filter.getEmployeeID()));
            filter.setLastYearMinutes(annualLeaveAllowanceMap.get(filter.getEmployeeID()));
            filter.setPaymentDeductions(((ArrayListMultimap<Integer, PaymentDeductionObject>) paymentDeductionsMap).get(filter.getEmployeeID()));

            result = generateSinglePayrun(filter);

            ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.Employee);
            customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(employee.getCustomFields(), customFieldItems);
            for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
                if (!CompanyCustomFieldItem.DATE.equals(customFieldItem.getDataType())) {
                    result.addCustomField(customFieldItem.getFieldName(), customFieldItem.getFieldStringValue());
                }
            }
        } else if (filter.getObjectID() != null) {
            result = payslipTableItemManager.getSinglePayrunTO(filter.getObjectID());
            boolean isDailyRateByEmployerSettings = "true".equals(payrollSettings.getOrDefault(DAILY_RATE_BY_EMPLOYER_SETTINGS, "false")) || "BY_STATIC_DAY".equals(payrollSettings.getOrDefault(DAILY_RATE_BY_EMPLOYER_SETTINGS, "false"));

            EdsEmployeePayrollSettingsTemplate edsTemplate = employeePayrollSettingsTemplateManager.getEmployeeAssignedTemplate(result.getEmployeeID());
            if (edsTemplate != null && edsTemplate.getStatus() != null) {
                result.setEmployeeTemplateStatus(edsTemplate.getStatus().getName());
            }
            EdsEmployeePayrollSettings salary = employeePayrollSettingsManager.getEmployeeSettingValue(result.getEmployeeID(), SALARY);
            result.setSalary(result.getBasicSalary() != null ? result.getBasicSalary() : salary != null ? BigDecimal.valueOf(Double.parseDouble(salary.getValue())) : BigDecimal.ZERO);
            result.setPayMethods(allInOneService.getPaymentMethodList());
            List<EdsPaymentDeduction> categoryList = payslipTableItemManager.getItemCategories(filter.getObjectID());
            numberOfWorkDay = new BigDecimal(payrollSettings.getOrDefault(NUMBER_OF_WORK_DAYS, DEFAULT_NUMBER_OF_WORK_DAYS.toString()));
            numberOfWorkDay = isDailyRateByEmployerSettings ? numberOfWorkDay : BigDecimal.valueOf(DateUtil.getDateInMonth(result.getYear(), result.getMonthID()));
            result.setNumberOfWorkDay(numberOfWorkDay);
            if (categoryList != null && categoryList.size() > 0) {
                String categoryIds = categoryList.stream().map(c -> c.getObjectID().toString()).collect(Collectors.joining(","));
                Map<Integer, BigDecimal> amounts = payslipPaymentsManager.getPaymentAmounts(categoryIds, result.getObjectID());
                for (EdsPaymentDeduction paymentDeduction : categoryList) {
                    PaymentDeductionObject object = paymentDeduction.getRPC();
                    object.setPaymentAmount(amounts.get(paymentDeduction.getObjectID()));

                    if (object.getCategoryItem() != null && LEAVE_DEDUCTIONS.equals(object.getCategoryItem().getCode())) {
                        List<PaymentDeductionObject> leaveDeductionLinkedCategories = new ArrayList<>();
                        Integer leaveDeductType = loadLeaveSettings(DEDUCT_TYPE, DEDUCT_ALLOWANCES, leaveDeductionLinkedCategories);
                        if (leaveDeductType == 1) {
                            object.setLinkedCategories(leaveDeductionLinkedCategories);
                        }
                        object.setNumberOfWorkDays(numberOfWorkDay);
                        object.setLeaveType(leaveDeductType);
                    }
                    if (object.isPaymentCategory()) {
                        result.getPaymentCategories().add(object);
                    } else if (object.isTaxCategory()) {
                        result.getTaxCategories().add(object);
                    } else if (object.isEmployerContributionCategory()) {
                        result.getEmployerContributionCategories().add(object);
                    } else if (object.isDeductionCategory()) {
                        result.getDeductionCategories().add(object);
                    } else if (object.isMaterialAidCategory()) {
                        result.getPaymentCategories().add(object);
                    }
                }
            }

            lfp = new ListingFilterParameter();
            lfp.setEmployeeId(result.getEmployeeID());
            lfp.setEndDate(filter.getToDate() != null ? filter.getToDate().getNonConvertedDate() : result.getToDate().getNonConvertedDate());
            lfp.setBaseCurrencyID(baseCurrency != null ? baseCurrency.getObjectID() : null);

            BigDecimal amount = BigDecimal.ZERO;
            List<ExpenseData> expenses = new LinkedList<>();
            String expensePaidFromAccount = payrollSettings.get(EXPENSE_PAID_ACCOUNT);
            EdsAccount paidFromAccount = expensePaidFromAccount != null && !expensePaidFromAccount.isEmpty() ? accountingManager.get(Integer.valueOf(expensePaidFromAccount)) : null;
            boolean empInBase = result.getCurrency() == null || result.getCurrency().getId().equals(baseCurrency.getObjectID());
            boolean expInBase;
            List<EdsExpenseReport> linkedExpenses = expenseReportManager.getPayslipTableItemRelatedExpenseClaims(result.getObjectID());
            for (EdsExpenseReport exp : linkedExpenses) {
                expInBase = empInBase || exp.getCurrency() == null || exp.getCurrency().getObjectID().equals(lfp.getBaseCurrencyID());
                ExpenseData expData;
                double total = expInBase ? exp.getBaseTotal().doubleValue() : exp.getTotal().doubleValue();
                if (PARTIALLY_PAID.equals(exp.getStatus().getCode())) {
                    double paid = exp.getPaidTotal(expInBase).doubleValue();
                    total -= paid;
                } else if (EXPENSE_PAID.equals(exp.getStatus().getCode())) {
                    total = exp.getPaidTotalByPayslip(filter.getObjectID(), expInBase).doubleValue();
                }
                if (exp.getAccount() != null) {
                    expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, exp.getAccount().getObjectID(), exp.getAccount().getName(), exp.getPaymentType());
                } else {
                    if (paidFromAccount == null) {
                        expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, null, "", exp.getPaymentType());
                    } else {
                        expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, paidFromAccount.getObjectID(), paidFromAccount.getName(), exp.getPaymentType());
                    }
                }
                if (expData.isInBaseCurrency() && result.getExchangeRate() != null) {
                    expData.setAmount(expData.getAmount() * result.getExchangeRate().doubleValue());
                    expData.setInBaseCurrency(false);
                }
                expenses.add(expData);
                amount = amount.add(BigDecimal.valueOf(expData.getAmount()));
            }

            if (expenses.size() > 0) {
                expenses.sort(Comparator.comparingInt(ExpenseData::getObjectID).reversed());

                EdsPayrollCategory category = categoryManager.getCategoryByCode(EXPENSE_REPORT);
                PaymentDeductionObject expensePayment = new PaymentDeductionObject();
                expensePayment.setPaymentAmount(amount);
                expensePayment.setExpenses(expenses.toArray(new ExpenseData[]{}));
                expensePayment.setCategoryItem(category.createPaymentDeductionSelectItem());
                result.setEmployeeExpenses(expensePayment);

            }

//            if (payslipTableItem.getEmployee().getProfile() != null && payslipTableItem.getEmployee().getProfile().getCountry() != null) {
//                result.setCalculatePension(true);
//                if (company.getCountryZone() != null) {
//                    result.setLocalEmployee(company.getCountryZone().getCountry().equals(payslipTableItem.getEmployee().getProfile().getCountry()));
//                }
//            }
            result.setPdfTemplateList(getPayrollPdfTemplates(PdfReferenceCodeNameEnum.SINGLE_PAYRUN.name()));
//            List<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.Employee);
//            customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(payslipTableItem.getEmployee().getCustomFields(), customFieldItems);
//            for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
//                if (!CompanyCustomFieldItem.DATE.equals(customFieldItem.getDataType())) {
//                    result.addCustomField(customFieldItem.getFieldName(), customFieldItem.getFieldStringValue());
//                }
//            }

            EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(filter.getObjectID());
            //Get payrun custom fields
            EdsPayrollCustomFields customFields = payslipTableItem.getCustomFields();
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.SinglePayrun);
            result.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(customFields, customFieldsItems));

            if (payslipTableItem.getPaymentItems() != null && !CollectionUtils.isEmpty(payslipTableItem.getPaymentItems())) {
                result.setPaymentItems(payslipTableItem.getPaymentItems().stream().map(EdsPayrunPaymentItem::toRPC).collect(Collectors.toCollection(ArrayList::new)));
            }
            if (payslipTableItem != null) {
                EdsSinglePayrunTransaction transaction = transactionManager.getTransactionByPayrun(payslipTableItem.getObjectID());
                if (transaction != null) {
                    result.setJournalId(transaction.getJournalId());
                }
            }
        } else {
            result.setMonthID(filter.getMonth());
            result.setYear(filter.getYear());
            result.setFromDate(filter.getFromDate());
            result.setToDate(filter.getToDate());
            result.setProcessDate(filter.getToDate());
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.SinglePayrun);
            result.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(null, customFieldsItems));
        }

        loadPensionCategories(result, company.getCountryZone());

        result.setExpense(BigDecimal.ZERO);

        if (fs != null && fs.getCurrency() != null) {
            result.setCurrencyName(fs.getCurrency().getName());
        }

        String paymentPolicy = payrollSettings.get(PAYMENT_POLICY);
        if (filter.getObjectID() == null) {
            result.setPaymentPolicy(paymentPolicy != null ? paymentPolicy : "");
            result.setPdfTemplateList(getPayrollPdfTemplates(PdfReferenceCodeNameEnum.SINGLE_PAYRUN.name()));
        }

        result.setAtsCustomization(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ATS_PAYROLL_CUSTOMIZATION));
        result.setDoubleApprovedEnabled("true".equals(payrollSettings.getOrDefault(ENABLED_DOUBLE_APPROVER_PAYRUN, "false")));
        result.setDoubleConfirmationEnabled("true".equals(payrollSettings.getOrDefault(DOUBLE_CONFIRMATION, "false")));
        result.setSendNotification("true".equals(payrollSettings.getOrDefault(BY_DEFAULT_EMAIL_NOTIFICATION, "false")));
        result.setEnabledMultiCurrency("true".equals(payrollSettings.getOrDefault(MULTI_CURRENCY_FOR_PAYROLL, "false")));
        EdsModule edsModule = moduleManager.getModuleByCode(PermissionConstants.ACCOUNTING_MODULE);
        if (edsModule != null) {
            result.setEnabledAccounting(true);
        }
        ArrayList<PaymentDeductionObject> nonZeroBalancedCategories = new ArrayList<>();
        for (PaymentDeductionObject pdo : result.getPaymentCategories()) {
            if (pdo.getPaymentAmount() != null && pdo.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                nonZeroBalancedCategories.add(pdo);
            }
        }
        result.setPaymentCategories(nonZeroBalancedCategories);

        ArrayList<PaymentDeductionObject> nonZeroBalancedDeductions = new ArrayList<>();
        for (PaymentDeductionObject pdo : result.getDeductionCategories()) {
            if (pdo.getPaymentAmount() != null && pdo.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                nonZeroBalancedDeductions.add(pdo);
            }
        }
        result.setDeductionCategories(nonZeroBalancedDeductions);

        ArrayList<PaymentDeductionObject> nonZeroBalancedTaxes = new ArrayList<>();
        for (PaymentDeductionObject pdo : result.getTaxCategories()) {
            if (pdo.getPaymentAmount() != null && pdo.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                nonZeroBalancedTaxes.add(pdo);
            }
        }
        result.setTaxCategories(nonZeroBalancedTaxes);
        if (result.getProjectId() != null) {
            EdsProject project = projectManager.get(result.getProjectId());
            if (project != null) {
                result.setProjectItem(new SelectItem(project.getObjectID(), project.getName()));
            }
        }
        ArrayList<PaymentDeductionObject> nonZeroBalancedEmployerContributions = new ArrayList<>();
        for (PaymentDeductionObject pdo : result.getEmployerContributionCategories()) {
            if (pdo.getPaymentAmount() != null && pdo.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                nonZeroBalancedEmployerContributions.add(pdo);
            }
        }
        result.setEmployerContributionCategories(nonZeroBalancedEmployerContributions);
        return result;
    }

    private BigDecimal calculatePastMonthUnpaidSalary(SinglePayrunItem result, ListingFilterParameter parentFilter, Boolean isDailyRateByEmployerSettings, BigDecimal numberOfWorkDay) {
        if (true) {
            //TODO hozircha previouse month salary hisoblanmiydigan bulib turibti
            return BigDecimal.ZERO;
        }
        Calendar calendar = Calendar.getInstance();
        int monthId = Optional.ofNullable(parentFilter.getMonthId()).orElse(calendar.get(Calendar.MONTH));
        int year = Optional.ofNullable(parentFilter.getYear()).orElse(calendar.get(Calendar.YEAR));
        BigDecimal fullDifference = BigDecimal.ZERO;
//        tempLeaveSickPaidDays.clear();
        ArrayList<BigDecimal> monthTotalList = new ArrayList<>();
        ArrayList<BigDecimal> fromPreviouseMonthList = new ArrayList<>();
        for (int y = 1; y <= 2; y++) {
            if (y == 1) {
                year = monthId > 1 ? year : year - 1;
                monthId = monthId > 1 ? monthId - 2 : monthId + 10;
            } else {
                year = monthId < 11 ? year : year + 1;
                monthId = monthId < 11 ? monthId + 1 : 0;
            }

            final Calendar previousMonth = Calendar.getInstance();
            previousMonth.set(Calendar.MONTH, monthId);
            previousMonth.set(Calendar.YEAR, year);
            previousMonth.set(Calendar.DAY_OF_MONTH, 1);

            final EdsPayslipTableItem firstPreviousTableItem = this.payslipTableItemManager.getEmployeePayslipTable(parentFilter.getEmployeeId(), monthId, year);
            final BigDecimal actualNumberOfWorkDays = isDailyRateByEmployerSettings ? numberOfWorkDay : new BigDecimal(DateUtil.getDateInMonth(year, monthId));

            PayslipItemFilter payslipItemFilter = new PayslipItemFilter();
            payslipItemFilter.setEmployeeID(parentFilter.getEmployeeId());
            payslipItemFilter.setMonth(monthId);
            payslipItemFilter.setYear(year);
            payslipItemFilter.setFromChangeHandler(true);
            payslipItemFilter.setFromNexMonth(true);
            payslipItemFilter.setDaysOfMonth(actualNumberOfWorkDays.intValue());
            payslipItemFilter.setFromDate(new DateNonConvertable(DateUtil.getMonthFirstDay(previousMonth.getTime())));
            payslipItemFilter.setToDate(new DateNonConvertable(DateUtil.getMonthLastDate(DateUtil.resetTime(previousMonth.getTime()))));
            SinglePayrunItem previouseMonthNewResult = getSinglePayrunData(payslipItemFilter);
            BigDecimal previouseMonthNewTotal = BigDecimal.ZERO;
            for (PaymentDeductionObject paymentObject : previouseMonthNewResult.getPaymentCategories()) {
                previouseMonthNewTotal = previouseMonthNewTotal.add(paymentObject.getPaymentAmount());
            }
            for (PaymentDeductionObject deductionObject : previouseMonthNewResult.getDeductionCategories()) {
                previouseMonthNewTotal = previouseMonthNewTotal.subtract(deductionObject.getPaymentAmount());
            }

            for (PaymentDeductionObject deductionObject : previouseMonthNewResult.getTaxCategories()) {
                previouseMonthNewTotal = previouseMonthNewTotal.subtract(deductionObject.getPaymentAmount());
            }

            BigDecimal previouseMonthOldTotal = BigDecimal.ZERO;
            BigDecimal fromPreviouseMonth = BigDecimal.ZERO;
            if (firstPreviousTableItem != null) {
                List<EdsPaymentDeduction> itemCategories = this.payslipTableItemManager.getItemCategories(firstPreviousTableItem.getObjectID(), true);
                previouseMonthOldTotal = firstPreviousTableItem.getTotal();
                for (EdsPaymentDeduction pd : itemCategories) {
                    BigDecimal paymentAmount = "Deduction".equals(pd.getCategory().getType()) || "Tax".equals(pd.getCategory().getType()) ? pd.getTotalPayment().multiply(BigDecimal.valueOf(-1)) : pd.getTotalPayment();
                    previouseMonthOldTotal = previouseMonthOldTotal.subtract(paymentAmount);
                    if (REMAINING_PREV_MONTH_PAYMENT_ONE.equals(pd.getCategory().getCode()) || REMAINING_PREV_MONTH_DEDUCTION_ONE.equals(pd.getCategory().getCode())) {
                        fromPreviouseMonth = fromPreviouseMonth.add(paymentAmount);
                    }
                }
            }

            monthTotalList.add(previouseMonthNewTotal.subtract(previouseMonthOldTotal));
            fromPreviouseMonthList.add(fromPreviouseMonth);
        }

        for (int j = 0; j <= 1; j++) {
            BigDecimal montTotal = monthTotalList.get(j);
            String prevMonthPaymentCode = REMAINING_PREV_MONTH_PAYMENT_ONE;
            String prevMonthDeductCode = REMAINING_PREV_MONTH_DEDUCTION_ONE;
            if (j == 0) {
                prevMonthPaymentCode = REMAINING_PREV_MONTH_PAYMENT_TWO;
                prevMonthDeductCode = REMAINING_PREV_MONTH_DEDUCTION_TWO;
                montTotal = montTotal.subtract(fromPreviouseMonthList.get(j + 1));
            }

            if (montTotal.setScale(2, RoundingMode.HALF_UP).compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }

            fullDifference = fullDifference.add(montTotal);

            boolean isPayment = montTotal.compareTo(BigDecimal.ZERO) > 0;
            EdsPayrollCategory rpmPaymentCategory;
            if (isPayment) {
                rpmPaymentCategory = this.categoryManager.getCategoryByCode(prevMonthPaymentCode);
            } else {
                rpmPaymentCategory = this.categoryManager.getCategoryByCode(prevMonthDeductCode);
            }

            PaymentDeductionObject remainingPrevMonthPayment = new PaymentDeductionObject();
            remainingPrevMonthPayment.setPaymentAmount(montTotal.abs());
            remainingPrevMonthPayment.setPaymentCategory(isPayment);
            if (rpmPaymentCategory != null) {
                remainingPrevMonthPayment.setCategoryItem(rpmPaymentCategory.createPaymentDeductionSelectItem());
                if (isPayment) {
                    result.getPaymentCategories().add(remainingPrevMonthPayment);
                } else {
                    result.getDeductionCategories().add(remainingPrevMonthPayment);
                }
            }
        }
        return fullDifference;
    }

    private String getWorkingTimeAsString(BigDecimal workingTime) {
        if (workingTime != null) {
            int seconds = workingTime.multiply(new BigDecimal(60)).multiply(new BigDecimal(60)).intValue();
            int hours = seconds / 60 / 60;
            int minutes = (seconds - hours * 60 * 60) / 60;
//            seconds = seconds - 60 * (hours * 60 + minutes);
            return String.format("%sh, %sm"/*, %ss*/, hours, minutes/*, seconds*/);
        }
        return "";
    }

    public SinglePayrunItem getSinglePayrunItemPaymentDeductionCategories(Integer objectID) {
        if (objectID != null) {
            List<EdsPaymentDeduction> categories;
            EdsCurrency baseCurrency = financialSettingsManager.getFinancialSettings().getCurrency();
            EdsPayslipTableItem item = payslipTableItemManager.get(objectID);
            boolean empInBase = true; //is Currency in Base?
            EdsPayrollCategory expenseCategory = categoryManager.getCategoryByCode(EXPENSE_REPORT);
            PaymentDeductionSelectItem expenseCategoryItem = expenseCategory.createPaymentDeductionSelectItem();
            EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(userManager.get(item.getEmployee().getObjectID()));
            SinglePayrunItem pItem = item.getRPC(userBankAccount);
            EdsCompany company = userManager.getUser().getCompany();
            pItem.setEmployee(pItem.getEmployee() != null ? pItem.getEmployee() : "");
            pItem.setEmployee(item.getEmployee().getProfile() != null && item.getEmployee().getProfile().getEmployeeCode() != null && !"".equals(item.getEmployee().getProfile().getEmployeeCode())
                    ? item.getEmployee().getProfile().getEmployeeCode().concat(" -> ").concat(item.getEmployee().getFullName()) : item.getEmployee().getFullName());
            EdsEmployeePayrollSettings salary = employeePayrollSettingsManager.getEmployeeSettingValue(pItem.getEmployeeID(), SALARY);
            pItem.setSalary(pItem.getBasicSalary() != null ? pItem.getBasicSalary() : salary != null ? BigDecimal.valueOf(Double.parseDouble(salary.getValue())) : BigDecimal.ZERO);
            pItem.setApproved(item.getApproved());
            categories = payslipTableItemManager.getItemCategories(item.getObjectID());
            if (categories != null && categories.size() > 0) {
                for (EdsPaymentDeduction paymentDeduction : categories) {
                    PaymentDeductionObject object = paymentDeduction.getRPC();
                    object.setPaymentAmount(payslipPaymentsManager.getPaymentAmount(paymentDeduction.getObjectID(), item.getObjectID()));
                    if (object.isPaymentCategory()) {
                        pItem.getPaymentCategories().add(object);
                    } else if (object.isTaxCategory()) {
                        pItem.getTaxCategories().add(object);
                    } else if (object.isEmployerContributionCategory()) {
                        pItem.getEmployerContributionCategories().add(object);
                    } else if (object.isDeductionCategory()) {
                        if (LEAVE_DEDUCTIONS.equals(object.getCategoryItem().getCode()) && object.getLeaveDaysCount() != null) {
                            pItem.setNonPaidLeaveDays(pItem.getNonPaidLeaveDays() + object.getLeaveDaysCount().intValue());
                        }
                        pItem.getDeductionCategories().add(object);
                    } else if (object.isMaterialAidCategory()) {
                        pItem.getPaymentCategories().add(object);
                    }
                }
            }
            if (pItem.getAdditionalPay() != null && BigDecimal.ZERO.compareTo(pItem.getAdditionalPay()) < 0) {
                EdsPayrollCategory bonusCategory = categoryManager.getCategoryByCode(BONUS);
                if (bonusCategory != null) {
                    PaymentDeductionObject bonus = new PaymentDeductionObject();
                    bonus.setCategoryItem(bonusCategory.createPaymentDeductionSelectItem());
                    bonus.setPaymentAmount(pItem.getAdditionalPay());
                    pItem.getPaymentCategories().add(bonus);
                }
            }
            ListingFilterParameter lfp = new ListingFilterParameter();
            lfp.setEmployeeId(pItem.getEmployeeID());
            lfp.setEndDate(pItem.getToDate().getNonConvertedDate());
            lfp.setBaseCurrencyID(baseCurrency.getObjectID());

            BigDecimal amount = BigDecimal.ZERO;
            List<ExpenseData> expenses = new LinkedList<>();
            List<EdsExpenseReport> unpaidExpenses = getUnpaidExpenseReports(lfp);
            List<EdsExpenseReport> linkedExpenses = expenseReportManager.getPayslipTableItemRelatedExpenseClaims(pItem.getObjectID());
            String expensePaidFromAccount = getCompanyPayrollSettings(EXPENSE_PAID_ACCOUNT);
            EdsAccount paidFromAccount = expensePaidFromAccount != null && !expensePaidFromAccount.isEmpty() ? accountingManager.get(Integer.valueOf(expensePaidFromAccount)) : null;

            for (EdsExpenseReport exp : unpaidExpenses) {
                double totalExp = empInBase ? exp.getBaseTotal().doubleValue() : exp.getTotal().doubleValue();
                double paid = exp.getPaidTotal(empInBase).doubleValue();
                totalExp -= paid;
                if (exp.getAccount() != null) {
                    expenses.add(new ExpenseData(exp.getObjectID(), exp.getTitle(), totalExp, empInBase, exp.getAccount().getObjectID(), exp.getAccount().getName()));
                } else {
                    if (paidFromAccount == null) {
                        expenses.add(new ExpenseData(exp.getObjectID(), exp.getTitle(), totalExp, empInBase, null, ""));
                    } else {
                        expenses.add(new ExpenseData(exp.getObjectID(), exp.getTitle(), totalExp, empInBase, paidFromAccount.getObjectID(), paidFromAccount.getName()));
                    }
                }
                amount = amount.add(BigDecimal.valueOf(totalExp));
            }
            for (EdsExpenseReport exp : linkedExpenses) {
                ExpenseData expData;
                double totalExp = empInBase ? exp.getBaseTotal().doubleValue() : exp.getTotal().doubleValue();
                if (PARTIALLY_PAID.equals(exp.getStatus().getCode())) {
                    double paid = exp.getPaidTotal(empInBase).doubleValue();
                    totalExp -= paid;
                } else if (EXPENSE_PAID.equals(exp.getStatus().getCode())) {
                    totalExp = exp.getPaidTotalByPayslip(item.getPayslipTable().getObjectID(), empInBase).doubleValue();
                }
                if (exp.getAccount() != null) {
                    expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), totalExp, empInBase, exp.getAccount().getObjectID(), exp.getAccount().getName());
                } else {
                    if (paidFromAccount == null) {
                        expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), totalExp, empInBase, null, "");
                    } else {
                        expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), totalExp, empInBase, paidFromAccount.getObjectID(), paidFromAccount.getName());
                    }
                }
                if (expData.isInBaseCurrency() && item.getPayslipTable().getExchangeRate() != null) {
                    expData.setAmount(expData.getAmount() * item.getPayslipTable().getExchangeRate().doubleValue());
                    expData.setInBaseCurrency(false);
                }
                expenses.add(expData);
                amount = amount.add(BigDecimal.valueOf(expData.getAmount()));
            }
            if (expenses.size() > 0) {
                expenses.sort((o1, o2) -> o2.getObjectID().compareTo(o1.getObjectID()));

                PaymentDeductionObject expensePayment = new PaymentDeductionObject();
                expensePayment.setPaymentAmount(amount);
                expensePayment.setExpenses(expenses.toArray(new ExpenseData[]{}));
                expensePayment.setCategoryItem(expenseCategoryItem);
                pItem.setEmployeeExpenses(expensePayment);
            }
            if (item.getEmployee().getProfile() != null && item.getEmployee().getProfile().getCountry() != null) {
                pItem.setCalculatePension(true);

                if (company.getCountryZone() != null) {
                    pItem.setLocalEmployee(company.getCountryZone().getCountry().equals(item.getEmployee().getProfile().getCountry()));
                }
            }
            return pItem;
        }
        return null;
    }

    private BigDecimal getWorkedDayTotal(List<MonthlyOvertimeData> overtimeData) {
        BigDecimal totalWorkedDay = BigDecimal.ZERO;

        if (overtimeData != null && !overtimeData.isEmpty()) {
            for (MonthlyOvertimeData data : overtimeData) {
                totalWorkedDay = totalWorkedDay.add(data.getTotalWorkedDays());
            }
        }

        return totalWorkedDay.compareTo(BigDecimal.ZERO) > 0 ? totalWorkedDay : BigDecimal.ZERO;
    }

    private void loadPensionCategories(SinglePayrunItem item, EdsCountryZone countryZone) {
        EdsPensionScheme pensionScheme = pensionSchemeManager.getPensionSchema(countryZone != null && countryZone.getCountry() != null ? countryZone.getCountry().getCode() : "");
        if (pensionScheme != null) {
            item.setPensionType(pensionScheme.getDeductionType());
            item.setCompanyPensionType(pensionScheme.getEmployerDeductionType());
            item.setPensionRate(pensionScheme.getDeductionValue());
            item.setNonLocalPensionRate(pensionScheme.getNonLocalDeductionValue());
            item.setCompanyPensionRate(pensionScheme.getEmployerDeductionValue());
            item.setCompanyNonLocalPensionRate(pensionScheme.getEmployerNonLocalDeductionValue());
            item.setPensionValueType(pensionScheme.getDeductFrom());
            item.setEmpMaxTaxableAmount(pensionScheme.getEmpMaxTaxableAmount());
            item.setCompMaxTaxableAmount(pensionScheme.getCompMaxTaxableAmount());
            if (pensionScheme.getCategories() != null && pensionScheme.getCategories().size() > 0) {
                for (EdsPayrollCategory category : pensionScheme.getCategories()) {
                    item.getPensionAllowances().add(category.createPaymentDeductionSelectItem());
                }
            }
        }
    }

    private BigDecimal loadLinkedCategoriesData(List<PaymentDeductionObject> relatedCategories, List<PaymentDeductionObject> paymentCategories, BigDecimal paymentsTotal, BigDecimal salary, BigDecimal allowanceRatio) {
        BigDecimal total = BigDecimal.ZERO;
        for (PaymentDeductionObject object : relatedCategories) {
            if (object.isFromAllAllowances() && object.getPercentage() != null) {
                object.setPaymentAmount(salary.add(paymentsTotal).multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
                total = total.add(object.getPaymentAmount());
            } else if (object.getLinkedCategories().size() > 0 && object.getPercentage() != null) {
                BigDecimal allowanceTotal = getAllowanceTotal(object.getLinkedCategories(), paymentCategories);
                object.setPaymentAmount(salary.add(allowanceTotal).multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP).multiply(allowanceRatio));
                total = total.add(object.getPaymentAmount());
            }
        }
        return total;
    }

    private BigDecimal loadLinkedTaxCategoriesData(SinglePayrunItem item, BigDecimal paymentsTotal, BigDecimal[] nonTaxableDeductionsTotal) {
        BigDecimal total = BigDecimal.ZERO;
        for (PaymentDeductionObject object : item.getTaxCategories()) {
            if (object.getPercentage() != null) {
                if (object.isFromAllAllowances()) {
                    object.setPaymentAmount(item.getBasicSalary().add(paymentsTotal).subtract(nonTaxableDeductionsTotal[0]).multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
                    total = total.add(object.getPaymentAmount());
                } else if (!object.getLinkedCategories().isEmpty()) {
                    BigDecimal allowanceTotal = getAllowanceTotal(object.getLinkedCategories(), item.getPaymentCategories());
                    object.setPaymentAmount(item.getBasicSalary().add(allowanceTotal).subtract(nonTaxableDeductionsTotal[0]).multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
                    total = total.add(object.getPaymentAmount());
                }
            }
        }
        return total;
    }

    private BigDecimal loadLinkedDeductionCategoriesData(SinglePayrunItem item, BigDecimal paymentsTotal, BigDecimal[] nonTaxableDeductionsTotal) {
        BigDecimal total = BigDecimal.ZERO;
        for (PaymentDeductionObject object : item.getDeductionCategories()) {
            BigDecimal deductionTax = object.getTax() != null ? object.getTax() : BigDecimal.ZERO;
            if (object.getPercentage() != null) {
                boolean flag = false;
                if (object.isFromAllAllowances()) {
                    BigDecimal amount = item.getBasicSalary().add(paymentsTotal).multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                    object.setPaymentAmount(amount.subtract(deductionTax));
                    flag = true;
                } else if (!object.getLinkedCategories().isEmpty()) {
                    BigDecimal allowanceTotal = getAllowanceTotal(object.getLinkedCategories(), item.getPaymentCategories());
                    BigDecimal amount = item.getBasicSalary().add(allowanceTotal).multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                    object.setPaymentAmount(amount.subtract(deductionTax));
                    flag = true;
                }

                if (flag) {
                    //recurring deduction logic
                    if (object.getRemainingAmount() != null && object.getPaymentAmount().compareTo(object.getRemainingAmount()) > 0) {
                        object.setPaymentAmount(object.getRemainingAmount());
                    }

                    total = total.add(object.getPaymentAmount());
                    if (object.getPaymentAmount() != null && !object.getCategoryItem().getTaxable()) {
                        nonTaxableDeductionsTotal[0] = nonTaxableDeductionsTotal[0].add(object.getPaymentAmount());
                    }
                }
            }
        }
        return total;
    }

    private BigDecimal loadLinkedEmployerContributionCategoriesData(SinglePayrunItem item, BigDecimal paymentsTotal) {
        BigDecimal total = BigDecimal.ZERO;
        for (PaymentDeductionObject object : item.getEmployerContributionCategories()) {
            if (object.getPercentage() != null) {
                if (object.isFromAllAllowances()) {
                    object.setPaymentAmount(item.getBasicSalary().add(paymentsTotal).multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
                    total = total.add(object.getPaymentAmount());
                } else if (!object.getLinkedCategories().isEmpty()) {
                    BigDecimal allowanceTotal = getAllowanceTotal(object.getLinkedCategories(), item.getPaymentCategories());
                    object.setPaymentAmount(item.getBasicSalary().add(allowanceTotal).multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
                    total = total.add(object.getPaymentAmount());
                }
            }
        }
        return total;
    }

    private BigDecimal getProfileMeOTPayments(MonthlyOvertimeDataWithRates overtimeDataWithRates, PaymentDeductionSelectItem regularOTCategoryItem, PaymentDeductionSelectItem weekendOTCategoryItem, PaymentDeductionSelectItem holidayOTCategoryItem, List<PaymentDeductionObject> payments) {
        BigDecimal overtimeTotal = BigDecimal.ZERO;
        if (overtimeDataWithRates != null) {
            PaymentDeductionObject overtimePayment;
            overtimePayment = getProfileMEOTPayment(regularOTCategoryItem, overtimeDataWithRates.getOvertimeRate(), overtimeDataWithRates.getOvertimeHours());
            if (overtimePayment != null) {
                overtimePayment.setRemarks(getWorkingTimeAsString(overtimeDataWithRates.getOvertimeHours()));
                payments.add(overtimePayment);
                overtimeTotal = overtimeTotal.add(overtimePayment.getPaymentAmount());
            }
            overtimePayment = getProfileMEOTPayment(weekendOTCategoryItem, overtimeDataWithRates.getWeekendOvertimeRate(), overtimeDataWithRates.getWeekendOvertimeHours());
            if (overtimePayment != null) {
                overtimePayment.setRemarks(getWorkingTimeAsString(overtimeDataWithRates.getWeekendOvertimeHours()));
                payments.add(overtimePayment);
                overtimeTotal = overtimeTotal.add(overtimePayment.getPaymentAmount());
            }
            overtimePayment = getProfileMEOTPayment(holidayOTCategoryItem, overtimeDataWithRates.getHolidayOvertimeRate(), overtimeDataWithRates.getHolidayOvertimeHours());
            if (overtimePayment != null) {
                overtimePayment.setRemarks(getWorkingTimeAsString(overtimeDataWithRates.getHolidayOvertimeHours()));
                payments.add(overtimePayment);
                overtimeTotal = overtimeTotal.add(overtimePayment.getPaymentAmount());
            }
        }
        return overtimeTotal;
    }

    private PaymentDeductionObject getProfileMEOTPayment(PaymentDeductionSelectItem categoryItem, BigDecimal overtimeRate, BigDecimal hours) {
        PaymentDeductionObject overtimePayment = null;
        if (categoryItem != null && hours != null && hours.compareTo(BigDecimal.ZERO) > 0 && overtimeRate != null) {
            overtimePayment = new PaymentDeductionObject();
            overtimePayment.setCategoryItem(categoryItem);
            overtimePayment.setPaymentAmount(overtimeRate.multiply(hours));
        }
        return overtimePayment != null && overtimePayment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0 ? overtimePayment : null;
    }

    private BigDecimal loadBenefitPaymentData(SinglePayrunItem result,
                                              ListingFilterParameter lfp,
                                              Integer baseCurrencyId,
                                              PaymentDeductionSelectItem benefitCategoryItem) {
        final List<EdsBenefitRequest> benefitRequests = benefitRequestManager.getBenefitRequestForPayment(lfp);

        if (benefitRequests.isEmpty()) {
            return BigDecimal.ZERO;
        }
        Double amount = 0d;
        for (EdsBenefitRequest benefitRequest : benefitRequests) {
            Double reqestedAmount = benefitRequest.getRequestedQuantity();
            if (!baseCurrencyId.equals(benefitRequest.getBenefit().getCurrency().getObjectID())) {
                amount += reqestedAmount / expenseServiceLocal.getExchRate(benefitRequest.getBenefit().getCurrency().getName());
            } else {
                amount += reqestedAmount;
            }
        }
        result.getPaymentCategories().add(new PaymentDeductionObject(benefitCategoryItem, BigDecimal.valueOf(amount)));

        return BigDecimal.valueOf(amount);
    }

    private BigDecimal deductPaymentsByNonPaidLeaveDays(SinglePayrunItem result, PaymentDeductionObject leaveDeduction) {
        BigDecimal paymentsDiff = BigDecimal.ZERO, diff;
        BigDecimal deductingRatio = leaveDeduction.getLeaveDaysCount().divide(result.getNumberOfWorkDay(), 6, RoundingMode.HALF_UP);
        for (PaymentDeductionObject obj : result.getPaymentCategories()) {
            boolean isDeducted = false;
            for (PaymentDeductionObject linkedObj : leaveDeduction.getLinkedCategories()) {
                if (linkedObj.getCategoryItem().getId().equals(obj.getCategoryItem().getId()) && obj.getPaymentAmount() != null) {
                    isDeducted = true;
                    break;
                }
            }
            if (!isDeducted && !obj.isAdditionalPayment()) {
                diff = obj.getPaymentAmount().multiply(deductingRatio);
                obj.setPaymentAmount(obj.getPaymentAmount().subtract(diff));
                paymentsDiff = paymentsDiff.subtract(diff);
            }
        }
        return paymentsDiff;
    }

    private BigDecimal deductLastMonthPaidLeaves(SinglePayrunItem result, ListingFilterParameter lfp, PaymentDeductionObject leaveDPayment, PaymentDeductionObject leaveMPayment) {
        BigDecimal dailyRate, paymentsDiff = BigDecimal.ZERO, diff;
        List<EdsSickRequest> lastMonthPaidLeaves = sickRequestManager.getNonPaidLeaveRequests(lfp);
        if (lastMonthPaidLeaves != null && lastMonthPaidLeaves.size() > 0) {
            HashMap<Date, LeaveBalanceCalculationItem> map = new LinkedHashMap<>();

            for (EdsSickRequest sickRequest : lastMonthPaidLeaves) {
                if (sickRequest.getStartDate() != null && sickRequest.getEndDate() != null) {
                    if (DateUtil.compare(lfp.getStartDate(), sickRequest.getStartDate())) {
                        sickRequest.setStartDate(lfp.getStartDate());
                    }
                    generateLRDays(sickRequest, lfp.getEndDate(), map, false);
                }
            }

            BigDecimal deductingRatio = BigDecimal.valueOf(map.get(DateUtil.getMonthFirstDay(lfp.getStartDate())).getLrDays()).divide(result.getNumberOfWorkDay(), 6, RoundingMode.HALF_UP);
            BigDecimal basicSalary = salaryHistoryManager.getEmployeeLastSalaryHistory(result.getEmployeeID(), result.getFromDate().getNonConvertedDate());
            BigDecimal allowanceTotal;

            if (lfp.isDailyRateByEmployerSettings()) {
                dailyRate = basicSalary.divide(result.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                setDailyRateToMap(map, dailyRate);

                if (leaveDPayment.getLeaveType() == 1) {
                    allowanceTotal = getAllowanceTotal(leaveDPayment.getLinkedCategories(), result.getPaymentCategories());
                    dailyRate = allowanceTotal.add(basicSalary).divide(result.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    setDailyRateToMap(map, dailyRate, true);
                }
                if (leaveMPayment.getLeaveType() == 1) {
                    allowanceTotal = getAllowanceTotal(leaveMPayment.getLinkedCategories(), result.getPaymentCategories());
                    dailyRate = allowanceTotal.add(basicSalary).divide(result.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    setDailyRateToMap(map, dailyRate, false);
                }
            } else {
                initializeDailyRateByMonth(map, basicSalary);
                if (leaveDPayment.getLeaveType() == 1) {
                    allowanceTotal = getAllowanceTotal(leaveDPayment.getLinkedCategories(), result.getPaymentCategories());
                    initializeDailyRateByMonth(map, allowanceTotal.add(basicSalary), true);
                }
                if (leaveMPayment.getLeaveType() == 1) {
                    allowanceTotal = getAllowanceTotal(leaveMPayment.getLinkedCategories(), result.getPaymentCategories());
                    initializeDailyRateByMonth(map, allowanceTotal.add(basicSalary), false);
                }
            }

            BigDecimal remainder = BigDecimal.ZERO;
//            result.setBasicSalary(result.getBasicSalary().subtract(result.getSalary().multiply(deductingRatio)));
            for (PaymentDeductionObject obj : result.getPaymentCategories()) {
                boolean isDeducted = false;
                if (leaveDPayment.getCategoryItem() != null && leaveDPayment.getCategoryItem().getId().equals(obj.getCategoryItem().getId()) || leaveMPayment != null && leaveMPayment.getCategoryItem() != null && leaveMPayment.getCategoryItem().getId().equals(obj.getCategoryItem().getId())) {
                    continue;
                }
                for (PaymentDeductionObject linkedObj : leaveDPayment.getLinkedCategories()) {
                    if (linkedObj.getCategoryItem().getId().equals(obj.getCategoryItem().getId()) && obj.getPaymentAmount() != null) {
                        diff = obj.getPaymentAmount().multiply(deductingRatio);
                        obj.setPaymentAmount(obj.getPaymentAmount().subtract(diff));
                        paymentsDiff = paymentsDiff.add(diff);
                        remainder = remainder.add(obj.getPaymentAmount().subtract(obj.getPaymentAmount().setScale(2, RoundingMode.HALF_UP)));
                        isDeducted = true;
                        break;
                    }
                }
                if (!isDeducted && lfp.isLeaveDaysImpact()) {
                    diff = obj.getPaymentAmount().multiply(deductingRatio);
                    obj.setPaymentAmount(obj.getPaymentAmount().subtract(diff));
                    paymentsDiff = paymentsDiff.add(diff);
                    remainder = remainder.add(obj.getPaymentAmount().subtract(obj.getPaymentAmount().setScale(2, RoundingMode.HALF_UP)));
                }
            }

            if (leaveMPayment.getCategoryItem().getId().equals(leaveDPayment.getCategoryItem().getId())) {
                leaveDPayment.setPaymentAmount(getAllLRAmount(map).add(remainder));
                if (leaveDPayment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0)
                    result.getPaymentCategories().add(leaveDPayment);

                paymentsDiff = paymentsDiff.add(leaveDPayment.getPaymentAmount());
            } else {
                leaveDPayment.setPaymentAmount(getAllLRAmount(map, true).add(remainder));
                leaveMPayment.setPaymentAmount(getAllLRAmount(map, false));
                if (leaveDPayment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0)
                    result.getPaymentCategories().add(leaveDPayment);
                if (leaveMPayment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0)
                    result.getPaymentCategories().add(leaveMPayment);

                paymentsDiff = paymentsDiff.add(leaveDPayment.getPaymentAmount()).add(leaveMPayment.getPaymentAmount());
            }
        }
        return paymentsDiff;
    }

    private BigDecimal loadCurrentMonthPaidLeavesData(SinglePayrunItem result, ListingFilterParameter lfp, PaymentDeductionObject leaveDPayment, PaymentDeductionObject leaveMPayment, MonthlyOvertimeDataWithRates overtimeDataWithRates) {
        List<EdsSickRequest> currentMonthPaidLeaves = sickRequestManager.getNonPaidLeaveRequests(lfp);
        BigDecimal dailyRate, paymentsDiff = BigDecimal.ZERO, diff;

        leaveDPayment.setPaymentAmount(BigDecimal.ZERO);
        leaveMPayment.setPaymentAmount(BigDecimal.ZERO);
        if (currentMonthPaidLeaves != null && currentMonthPaidLeaves.size() > 0 && leaveDPayment.getCategoryItem() != null) {
            Map<Date, LeaveBalanceCalculationItem> map = new LinkedHashMap<>();

            for (EdsSickRequest sickRequest : currentMonthPaidLeaves) {
                if (sickRequest.getStartDate() != null && sickRequest.getEndDate() != null) {
                    if (sickRequest.isTakeByMoney()) {
                        map = generateLRDays(sickRequest, lfp.getEndDate(), map, true);
                    } else {
                        map = generateLRDays(sickRequest, lfp.getEndDate(), map, false);
                    }
                }
            }

            LeaveBalanceCalculationItem cmLRBalance = map.values().iterator().next(); //Current month leave balance
            leaveDPayment.setLeaveDaysCount(BigDecimal.valueOf(cmLRBalance.getLrDays() + cmLRBalance.getLrTakeMoneyDays()));
            BigDecimal basicSalary = salaryHistoryManager.getEmployeeLastSalaryHistory(result.getEmployeeID(), result.getFromDate().getNonConvertedDate());
            BigDecimal allowanceTotal;

            if (overtimeDataWithRates != null && overtimeDataWithRates.getRateType() != null && overtimeDataWithRates.getRate() != null) {
                dailyRate = overtimeDataWithRates.getRate();

                if (overtimeDataWithRates.getRateType() == 0 && cmLRBalance.getLrDays() > 0) {
                    dailyRate = dailyRate.multiply(BigDecimal.valueOf(cmLRBalance.getLrHours()).divide(BigDecimal.valueOf(cmLRBalance.getLrDays()), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
                }
                setDailyRateToMap(map, dailyRate);
            } else if (lfp.isDailyRateByEmployerSettings()) {
                dailyRate = basicSalary.divide(result.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                setDailyRateToMap(map, dailyRate);

                if (leaveDPayment.getLeaveType() == 1) {
                    allowanceTotal = getAllowanceTotal(leaveDPayment.getLinkedCategories(), result.getPaymentCategories());
                    dailyRate = allowanceTotal.add(basicSalary).divide(result.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    setDailyRateToMap(map, dailyRate, true);
                }
                if (leaveMPayment.getLeaveType() == 1) {
                    allowanceTotal = getAllowanceTotal(leaveMPayment.getLinkedCategories(), result.getPaymentCategories());
                    dailyRate = allowanceTotal.add(basicSalary).divide(result.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    setDailyRateToMap(map, dailyRate, false);
                }
            } else {
                final boolean isCalculationByTimeslot = "BY_TIMESLOT".equals(getCompanyPayrollSettings(DAILY_RATE_BY_EMPLOYER_SETTINGS));
                if (isCalculationByTimeslot) {
                    initializeDailyRateByMonth(map, basicSalary, result.getNumberOfWorkDay());
                } else {
                    initializeDailyRateByMonth(map, basicSalary);
                }
                if (leaveDPayment.getLeaveType() == 1) {
                    allowanceTotal = getAllowanceTotal(leaveDPayment.getLinkedCategories(), result.getPaymentCategories());

                    if (isCalculationByTimeslot) {
                        initializeDailyRateByMonth(map, allowanceTotal.add(basicSalary), result.getNumberOfWorkDay(), true);
                    } else {
                        initializeDailyRateByMonth(map, allowanceTotal.add(basicSalary), true);
                    }
                }
                if (leaveMPayment.getLeaveType() == 1) {
                    allowanceTotal = getAllowanceTotal(leaveMPayment.getLinkedCategories(), result.getPaymentCategories());
                    initializeDailyRateByMonth(map, allowanceTotal.add(basicSalary), false);
                }
            }

            BigDecimal remainder = BigDecimal.ZERO;
//            result.setBasicSalary(result.getBasicSalary().subtract(BigDecimal.valueOf(cmLRBalance.getLrDays()).multiply(result.getSalary()).divide(result.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)));
            for (PaymentDeductionObject object : result.getPaymentCategories()) {
                boolean isDeducted = false;
                for (PaymentDeductionObject catObj : leaveDPayment.getLinkedCategories()) {
                    if (catObj.getCategoryItem().getId().equals(object.getCategoryItem().getId()) && object.getPaymentAmount() != null) {
                        diff = BigDecimal.valueOf(cmLRBalance.getLrDays()).multiply(object.getPaymentAmount()).divide(result.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                        object.setPaymentAmount(object.getPaymentAmount().subtract(diff));
                        paymentsDiff = paymentsDiff.subtract(diff);
                        remainder = remainder.add(object.getPaymentAmount().subtract(object.getPaymentAmount().setScale(2, RoundingMode.HALF_UP)));
                        isDeducted = true;
                        break;
                    }
                }
                if (!isDeducted && lfp.isLeaveDaysImpact() && !object.isAdditionalPayment()) {
                    diff = BigDecimal.valueOf(cmLRBalance.getLrDays()).multiply(object.getPaymentAmount()).divide(result.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    object.setPaymentAmount(object.getPaymentAmount().subtract(diff));
                    paymentsDiff = paymentsDiff.subtract(diff);
                    remainder = remainder.add(object.getPaymentAmount().subtract(object.getPaymentAmount().setScale(2, RoundingMode.HALF_UP)));
                }
            }

            if (leaveMPayment.getCategoryItem().getId().equals(leaveDPayment.getCategoryItem().getId())) {
                leaveDPayment.setPaymentAmount(getAllLRAmount(map).add(remainder));
                if (leaveDPayment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0)
                    result.getPaymentCategories().add(leaveDPayment);

                paymentsDiff = paymentsDiff.add(leaveDPayment.getPaymentAmount());
            } else {
                leaveDPayment.setPaymentAmount(getAllLRAmount(map, true).add(remainder));
                leaveMPayment.setPaymentAmount(getAllLRAmount(map, false));
                if (leaveDPayment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0)
                    result.getPaymentCategories().add(leaveDPayment);
                if (leaveMPayment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0)
                    result.getPaymentCategories().add(leaveMPayment);

                paymentsDiff = paymentsDiff.add(leaveDPayment.getPaymentAmount()).add(leaveMPayment.getPaymentAmount());
            }
        }
        return paymentsDiff;
    }

    private void loadLeaveDeductionData(SinglePayrunItem result, ListingFilterParameter lfp, PaymentDeductionObject leaveDeduction) {
        List<EdsSickRequest> nonPaidSickRequests = sickRequestManager.getNonPaidLeaveRequests(lfp);
        Double leaveDaysCount = 0.0;
        for (EdsSickRequest request : nonPaidSickRequests) {
            if (request.getStartDate() != null && request.getEndDate() != null) {
                request.setStartDate(lfp.getStartDate() == null || DateUtil.compare(request.getStartDate(), lfp.getStartDate()) ? request.getStartDate() : lfp.getStartDate());
                request.setEndDate(lfp.getEndDate() == null || DateUtil.compare(DateUtil.getDayLastTime(lfp.getEndDate()), request.getEndDate()) ? request.getEndDate() : DateUtil.getDayLastTime(lfp.getEndDate()));
                leaveDaysCount += countLeaveRequestDays(request);
            }
        }
        leaveDeduction.setLeaveDaysCount(BigDecimal.valueOf(leaveDaysCount));
        BigDecimal allowanceTotal = leaveDeduction.getLeaveType() == 2 ? lfp.getPaymentsTotal() : getAllowanceTotal(leaveDeduction.getLinkedCategories(), result.getPaymentCategories());
        BigDecimal dailyRate = result.getSalary().add(allowanceTotal).divide(leaveDeduction.getNumberOfWorkDays(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        leaveDeduction.setPaymentAmount(dailyRate.multiply(leaveDeduction.getLeaveDaysCount()).setScale(ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));

        if (leaveDaysCount > 0) {
            result.getDeductionCategories().add(leaveDeduction);
        }
    }

    private List<PaymentDeductionObject> loadLeaveSettings(String leaveAllowances) {
        if (StringUtil.isEmpty(leaveAllowances)) {
            return Collections.emptyList();
        }
        final String[] array = leaveAllowances.split(";");

        if (array.length == 0) {
            return Collections.emptyList();
        }
        final List<Integer> idList = Lists.newArrayList(array).stream().map(Integer::parseInt).collect(Collectors.toList());
        final List<EdsPayrollCategory> list = categoryManager.get(idList);

        return list.stream()
                .map(pd -> new PaymentDeductionObject(pd.createPaymentDeductionSelectItem()))
                .collect(Collectors.toList());
    }

    private Integer loadLeaveSettings(String leaveTypeCode, String leaveAllowancesCode, List<PaymentDeductionObject> linkedCategories) {
        Integer leaveType = Integer.parseInt(getCompanyPayrollSettings(leaveTypeCode, "0"));

        String leaveAllowances = getCompanyPayrollSettings(leaveAllowancesCode);
        if (leaveAllowances != null && !leaveAllowances.isEmpty()) {
            EdsPayrollCategory cat;
            String[] categories = leaveAllowances.split(";");
            PaymentDeductionObject linkedObject;
            for (String category : categories) {
                cat = categoryManager.get(Integer.parseInt(category));
                if (cat != null) {
                    linkedObject = new PaymentDeductionObject();
                    linkedObject.setCategoryItem(cat.createPaymentDeductionSelectItem());
                    linkedCategories.add(linkedObject);
                }
            }
        }
        return leaveType;
    }

    private void loadBasicSalaryWithOvertimeData(SinglePayrunItem result, MonthlyOvertimeDataWithRates overtimeDataWithRates) {
        if (overtimeDataWithRates != null) {
            if (overtimeDataWithRates.getRateType() != null && overtimeDataWithRates.getRate() != null) {
                if (overtimeDataWithRates.getRateType() == 0) {
                    if (overtimeDataWithRates.getPlannedHours() != null && overtimeDataWithRates.getPlannedHours().compareTo(BigDecimal.ZERO) > 0) {
                        result.setBasicSalary(overtimeDataWithRates.getRate().multiply(overtimeDataWithRates.getWorkedHours().divide(overtimeDataWithRates.getPlannedHours(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP))
                                .setScale(ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
                    } else {
                        result.setBasicSalary(BigDecimal.ZERO);
                    }
                } else {
                    if (overtimeDataWithRates.getDaysOfPresence() != null && overtimeDataWithRates.getDaysOfPresence() > 0) {
                        result.setBasicSalary(overtimeDataWithRates.getRate().multiply(BigDecimal.valueOf(overtimeDataWithRates.getDaysOfPresence())));
                    } else {
                        result.setBasicSalary(BigDecimal.ZERO);
                    }
                }
            } else {
                result.setBasicSalary(BigDecimal.ZERO);
                overtimeDataWithRates = null;
            }
        }
    }

    @Transactional
    public Double countLeaveRequestDays(EdsSickRequest request, boolean... isHours) {
        EntityManager em = sickRequestManager.getJpaTemplate().getHibernateEntityManager();
        try (Session session = em.unwrap(Session.class)) {
            session.evict(request);
        } catch (Exception ignored) {
        } finally {
            em.close();
        }
        return Double.parseDouble(availabilityServiceLocal.getLeaveRequestStats(request)[isHours.length > 0 && isHours[0] ? 1 : 0]);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PayrollPdfTemplateList getPayrollPdfTemplates(String type) {
        List<EdsCompanyPdfTemplate> templates = companyPdfTemplateManager.getCompanyPDFTemplatesByType(type);
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
        return new PayrollPdfTemplateList(items, defaultTemplateID);
    }

    @Override
    public ListResult<SinglePayrunItem> getSinglePayrunList(ListingFilterParameter fp) {

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEmployee.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get SinglePayrun list (from solr)");
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        FacetFilterRpc singlePayrunFacetFilter = fp.getFacetFilter();
        if (singlePayrunFacetFilter != null && !singlePayrunFacetFilter.isFilterChanges()) {
            singlePayrunFacetFilter = commonServiceLocal.getUserFacetFilter(singlePayrunFacetFilter);
        }
        if (singlePayrunFacetFilter != null) {
            if (singlePayrunFacetFilter.getSearchKey() != null && !"".equals(singlePayrunFacetFilter.getSearchKey())) {
                fp.setSearchKey(singlePayrunFacetFilter.getSearchKey());
            }
            fp.setStartDate(singlePayrunFacetFilter.getStartDate());
            fp.setEndDate(singlePayrunFacetFilter.getEndDate());
            fp.setFacetFilter(singlePayrunFacetFilter);
        }
        fp.setCheckNumber(employeeManager.isIntegerEmployeeCodeEnabled());
        EdsUser edsUser = employeeManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();

        String solrQuery = QueryBuilderForSolr.getSinglePayrunSolrQuery(fp) +
                (fp.getEmployeeId() == null ? generatePermissionQuery(PermissionConstants.PAYROLL_PAYSLIP_LIST) : "") +
                SolrFacetUtils.generateForPricesFacet(singlePayrunFacetFilter, FacetContentType.SinglePayrunFacetFilter.getContentCode()[3]) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(singlePayrunFacetFilter, edsCompany,
                        SolrSinglePayrunRepresenter.FIELD_FROM_DATE,
                        SolrSinglePayrunRepresenter.FIELD_TO_DATE, FacetContentType.SinglePayrunFacetFilter.getContentCode()[3]);
        return getSinglePayrunResponse(fp, solrQuery);
    }

    public String generatePermissionQuery(String view) {
        EdsUser user = payrollBatchManager.getUser();

        if (roleManager.hasRole(user, EdsRole.DR) || roleManager.hasRole(user, EdsRole.ADMIN) || roleManager.hasRole(user, EdsRole.HR) || roleManager.hasRole(user, EdsRole.ACCOUNTANT)
                || (PermissionConstants.PAYROLL_CASH_ADVANCE_LIST.equals(view) && ServerUtils.hasPermission(PermissionConstants.PAYROLL_CASH_ADVANCE_FULL_ACCESS))
                || (PermissionConstants.PAYROLL_GROUP_PAYRUN_LIST.equals(view) && ServerUtils.hasPermission(PermissionConstants.PAYROLL_GROUP_PAYRUN_FULL_ACCESS))
                || (PermissionConstants.PAYROLL_PAYSLIP_LIST.equals(view) && ServerUtils.hasPermission(PermissionConstants.PAYROLL_SINGLE_PAYRUN_FULL_ACCESS))
                || (PermissionConstants.PAYROLL_EMPLOYEES_LIST.equals(view) && ServerUtils.hasPermission(PermissionConstants.PAYROLL_EMPLOYEES_FULL_ACCESS))
        ) {
            return "";
        }

        List<EdsPayrollBatch> groups = payrollBatchManager.getManagerPayrollGroups(user.getObjectID());

        StringBuilder query = new StringBuilder();

        if (groups != null && !groups.isEmpty()) {
            StringBuilder whereClause = new StringBuilder();

            for (EdsPayrollBatch g : groups) {
                if (whereClause.length() > 0) {
                    whereClause.append(" OR ");
                }
                whereClause.append(g.getObjectID());
            }
            query.append(" AND ").append(SolrSinglePayrunRepresenter.FIELD_PAYROLL_BATCH_ID).append(":(").append(whereClause).append(") ");
        } else {
            if (PermissionConstants.PAYROLL_PAYSLIP_LIST.equals(view)) {
                query.append(" AND ").append(SolrSinglePayrunRepresenter.FIELD_EMPLOYEE_ID).append(":").append(user.getObjectID());
            } else {
                query.append(" AND ").append(SolrSinglePayrunRepresenter.FIELD_PAYROLL_BATCH_ID).append(":0 "); //managers can not see employees that assigned other groups
            }
        }
        return query.toString();
    }

    private ListResult<SinglePayrunItem> getSinglePayrunResponse(ListingFilterParameter fp, String solrQuery) {
        Page<SinglePayrunSolrDoc> singlePayrunSolrDocs = singlePayrunSolrComponent.getList(fp, solrQuery);
        return getSinglePayrunFromSolrResult(singlePayrunSolrDocs, fp);
    }

    private ListResult<CashAdvanceItem> getCashAdvanceResponse(ListingFilterParameter fp, String solrQuery) {
        ListPanelToolRpc panelSettings = fp.getListPanelTool();
        Page<CashAdvanceSolrDoc> cashAdvanceSolrDocPage = cashAdvanceSolrComponent.getList(fp, solrQuery);
        return getCashAdvanceFromSolrResult(cashAdvanceSolrDocPage, panelSettings);
    }

    private ListResult<AdditionalPayment> getAdditionalPaymentResponse(ListingFilterParameter fp, String solrQuery) {
        Page<AdditionalPaymentSolrDoc> additionalPaymentSolrDocs = additionalPaymentSolrComponent.getList(fp, solrQuery);
        return getAdditionalPaymentFromSolrResult(additionalPaymentSolrDocs, fp);
    }

    private ListResult<SinglePayrunItem> getSinglePayrunFromSolrResult(Page<SinglePayrunSolrDoc> singlePayrunSolrDocs, ListingFilterParameter filterParametrs) {
        ArrayList<SinglePayrunItem> itemList = new ArrayList<>();
        int totalNumber = 0;
        ListPanelToolRpc panelSettings = filterParametrs.getListPanelTool();
        if (singlePayrunSolrDocs != null && singlePayrunSolrDocs.getContent() != null) {
            totalNumber = (int) singlePayrunSolrDocs.getTotalElements();
            boolean isApproved = approverManager.isExistApproverByEntityType(RelationItem.TYPE_ADDITIONAL_PAYMENT);

            for (SinglePayrunSolrDoc relevantDoc : singlePayrunSolrDocs.getContent()) {
                SinglePayrunItem item = new SinglePayrunItem();
                Integer singlePayrunID = relevantDoc.getSinglePayrunId();
                item.setObjectID(singlePayrunID);
                item.setEmployee(relevantDoc.getEmployeeName());
                item.setEmployeeCode(relevantDoc.getEmployeeCode());
                item.setDriverID(relevantDoc.getDriverId());
                Integer employeeId = relevantDoc.getEmployeeId();
                item.setEmployeeID(employeeId);
//                item.setProjects(getEmployeeProjects(employeeId));
                Integer statusId = relevantDoc.getStatusId();
                if (statusId != null) {
                    EdsReference status = referenceManager.get(statusId);
                    item.setStatusID(statusId);
                    item.setStatus(status.getName());
                    item.setStatusCode(status.getCode());
                }

                item.setPdfTemplateID(relevantDoc.getPdfTemplateId() != null ? Integer.parseInt(relevantDoc.getPdfTemplateId()) : null);
                item.setMonthID(relevantDoc.getMonthId());
                item.setMonth(relevantDoc.getMonthName());
                item.setYear(relevantDoc.getYear());
                item.setFromDate(new DateNonConvertable(relevantDoc.getFromDate()));
                item.setToDate(new DateNonConvertable(relevantDoc.getToDate()));
                item.setCreator(new SelectItem(relevantDoc.getPreparerId(), relevantDoc.getPreparerName()));
                item.setApprover(new SelectItem(relevantDoc.getApproverId(), relevantDoc.getApproverName()));
                item.setApproved(relevantDoc.getApproved());
                item.setTotal(relevantDoc.getTotal() != null ? BigDecimal.valueOf(Double.parseDouble(relevantDoc.getTotal())) : null);
                item.setCurrency(new CurrencyItem(relevantDoc.getCompanyId(), relevantDoc.getCurrencyName(), ""));
                item.setProcessDate(new DateNonConvertable(relevantDoc.getProcessDate()));
                item.setPayMethodName(relevantDoc.getPaymentMethod() != null ? relevantDoc.getPaymentMethod().toString() : null);
                if (panelSettings != null) {
                    item.setCustomFieldMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(relevantDoc, panelSettings.getColumnCodeName()));
                }
                item.setApproved(isApproved);
                itemList.add(item);
            }
        }
        return new ListResult<>(itemList, totalNumber);
    }

    private ListResult<CashAdvanceItem> getCashAdvanceFromSolrResult(Page<CashAdvanceSolrDoc> cashAdvanceSolrDocPage, ListPanelToolRpc panelSettings) {
        ArrayList<CashAdvanceItem> itemList = new ArrayList<>();
        int totalNumber = 0;
        if (cashAdvanceSolrDocPage != null && cashAdvanceSolrDocPage.getContent() != null && cashAdvanceSolrDocPage.getContent().size() > 0) {
            totalNumber = (int) cashAdvanceSolrDocPage.getTotalElements();
            for (CashAdvanceSolrDoc doc : cashAdvanceSolrDocPage.getContent()) {
                CashAdvanceItem item = new CashAdvanceItem();
                item.setObjectID(doc.getCashAdvanceId());
                if (doc.getApproverId() != null) {
                    item.setApprover(new SelectItem(doc.getApproverId(), doc.getApproverName()));
                }
                String employeeCode = doc.getEmployeeCode();
                String employeeName = doc.getEmployeeName();
                String employeeCodeName = employeeCode != null && !"".equals(employeeCode) ? employeeCode + " -> " + employeeName : employeeName;
                item.setEmployee(new SelectItem(doc.getEmployeeId(), employeeCodeName));
                item.setEmployeeCode(employeeCode != null ? employeeCode : "");
                item.setEmployeeName(employeeName);
                if (doc.getRequestDate() != null) {
                    item.setDate(new DateNonConvertable(doc.getRequestDate()));
                }
                if (doc.getApprovedDate() != null) {
                    item.setApprovedDate(new DateNonConvertable(doc.getApprovedDate()));
                }
                item.setPercent(doc.getPercent());
                item.setPaymentAmount(BigDecimal.valueOf(doc.getPaymentAmount()));
                item.setTotalAmount(BigDecimal.valueOf(doc.getTotalAmount()));
                item.setType(doc.getType());
                if (doc.getPaymentMethodId() != null) {
                    SelectItem paymentMethod = new SelectItem();
                    paymentMethod.setId(doc.getPaymentMethodId());
                    paymentMethod.setName(doc.getPaymentMethodName());
                    paymentMethod.setCode(doc.getPaymentMethodCode());
                    item.setPaymentMethod(paymentMethod);
                }
                item.setPurpose(doc.getPurpose());
                //If status is null, set empty object to avoid checking null pointer
                SelectItem status = new SelectItem();
                status.setId(doc.getStatusId());
                status.setName(doc.getStatusName() != null ? commonLocalizer.localize(doc.getStatusName().toLowerCase(), doc.getStatusName()) : "");
                status.setCode(doc.getStatusCode());
                item.setStatus(status);

                if (doc.getCurrencyId() != null) {
                    CurrencyItem currencyItem = new CurrencyItem();
                    currencyItem.setId(doc.getCurrencyId());
                    currencyItem.setName(doc.getCurrencyName());
                    item.setCurrency(currencyItem);
                }
                item.setDriverId(doc.getDriverId());
                item.setNumber(doc.getNumber());
                item.setRemainingAmount(BigDecimal.valueOf(doc.getRemainingAmount()));

                Integer prevApproverID = doc.getPreviousApproverId();
                if (prevApproverID != null) {
                    ApproverItemMini prevApprover = new ApproverItemMini();
                    prevApprover.setObjectID(prevApproverID);
                    prevApprover.setExactEmployee(SolrUtils.asSelectItem(doc.getPreviousApproverExactEmployeeId(), doc.getPreviousApproverExactEmployeeName()));
                    ReferenceItem prevApproverStatus = new ReferenceItem();
                    prevApproverStatus.setId(doc.getPreviousApproverStatusId());
                    prevApproverStatus.setCode(doc.getPreviousApproverStatusCode());
                    prevApprover.setStatus(prevApproverStatus);

                    item.setPrevApprover(prevApprover);
                }

                Integer currentApproverID = doc.getCurrentApproverId();
                if (currentApproverID != null) {
                    ApproverItemMini currentApprover = new ApproverItemMini();
                    currentApprover.setObjectID(currentApproverID);
                    currentApprover.setExactEmployee(SolrUtils.asSelectItem(doc.getCurrentApproverExactEmployeeId(), doc.getCurrentApproverExactEmployeeName()));
                    ReferenceItem currentApproverStatus = new ReferenceItem();
                    currentApproverStatus.setId(doc.getCurrentApproverStatusId());
                    currentApproverStatus.setCode(doc.getCurrentApproverStatusCode());
                    currentApprover.setStatus(currentApproverStatus);

                    item.setCurrentApprover(currentApprover);
                }

                if (doc.getOverallStatusId() != null) {
                    ReferenceItem overallStatus = new ReferenceItem();
                    overallStatus.setId(doc.getOverallStatusId());
                    overallStatus.setName(doc.getOverallStatusCode());
                    item.setOverallStatus(overallStatus);
                }
                if (panelSettings != null) {
                    HashMap<String, Object> map = CustomFieldsUtils.getBaseSolrDocDynamicFields(doc, panelSettings.getColumnCodeName());
                    item.setCustomFieldValuesItems(commonServiceLocal.getLocaledCustomFiledMap(map, panelSettings.getListViewCustomFields()));
                }
                itemList.add(item);
            }
        }
        return new ListResult<>(itemList, totalNumber);
    }

    private ListResult<AdditionalPayment> getAdditionalPaymentFromSolrResult(Page<AdditionalPaymentSolrDoc> additionalPaymentSolrDocs, ListingFilterParameter fp) {
        ArrayList<AdditionalPayment> itemList = new ArrayList<>();
        int totalNumber = 0;
        if (additionalPaymentSolrDocs != null && additionalPaymentSolrDocs.getContent() != null) {
            totalNumber = (int) additionalPaymentSolrDocs.getTotalElements();
            for (AdditionalPaymentSolrDoc relevantDoc : additionalPaymentSolrDocs.getContent()) {
                AdditionalPayment item = new AdditionalPayment();
                Integer additionalPaymentID = relevantDoc.getAdditionalPaymentId();
                item.setObjectID(additionalPaymentID);
                item.setReference(relevantDoc.getReference());
                String c = relevantDoc.getStatusName() == null ? null : relevantDoc.getStatusName().toLowerCase();
                item.setStatus(commonLocalizer.localize(c, relevantDoc.getStatusName()));
                item.setStatusCode(relevantDoc.getStatusCode());
                item.setType(relevantDoc.getType());
                item.setEntityType(relevantDoc.getEntityType());
                item.setPaymentType(relevantDoc.getPaymentType());
                item.setPayrollBatch(new SelectItem(relevantDoc.getPayrollGroupId(), relevantDoc.getPayrollGroupName()));
                item.setDepartment(new SelectItem(relevantDoc.getPayrollDepartmentId(), relevantDoc.getPayrollDepartmentName()));
                item.setPdfTemplateID(relevantDoc.getPdfTemplateId());
                item.setMonth(ServerUtils.convertMonthToInterfaceLanguage(relevantDoc.getMonthName()));
                item.setYear(relevantDoc.getYear());
                item.setDate(new DateNonConvertable(relevantDoc.getCreationDate()));
                item.setCreator(new SelectItem(relevantDoc.getCreatorId(), relevantDoc.getCreatorName()));
                if (relevantDoc.getApproverId() != null) {
                    item.setApprover(new SelectItem(relevantDoc.getApproverId(), relevantDoc.getApproverName()));
                }
                item.setTotal(BigDecimal.valueOf(relevantDoc.getTotalAmount()));
                String categoryType = relevantDoc.getPaymentCategory();
                item.setCategory(new SelectItem(categoryType));
                item.setCategoryType(commonLocalizer.localize(relevantDoc.getPaymentCategory().toLowerCase(), relevantDoc.getPaymentCategory()));
                item.setUpdatedTime(new DateNonConvertable(relevantDoc.getLastUpdate()));
                item.setUpdater(new SelectItem(relevantDoc.getUpdaterId(), relevantDoc.getUpdaterName()));

                String categoryLookUpName = relevantDoc.getCategoryLookupName();
                String departmentName = relevantDoc.getPayrollDepartmentName();
                if (Objects.equals(categoryLookUpName, departmentName)) {
                    Integer departmentId = relevantDoc.getPayrollDepartmentId();
                    EdsReferenceLocale edsDepartmentLocale = departmentManager.getDeparmentLocalization(departmentId);
                    String locale = ServerUtils.getUserLocale().getLanguage();
                    if (edsDepartmentLocale != null) {
                        if ("ru".equals(locale)) {
                            categoryLookUpName = edsDepartmentLocale.getRussian();
                        } else if ("uz".equals(locale)) {
                            categoryLookUpName = edsDepartmentLocale.getUzbek();
                        } else if ("en".equals(locale)) {
                            categoryLookUpName = edsDepartmentLocale.getEnglish();
                        } else {
                            categoryLookUpName = edsDepartmentLocale.getArabic();
                        }
                    } else {
                        categoryLookUpName = "";
                    }
                    item.setCategoryLookUp(categoryLookUpName);
                } else if (Objects.equals(categoryLookUpName, "All Employees")) {
                    item.setCategoryLookUp(commonLocalizer.localize("allEmployees", "All Employees"));
                } else {
                    item.setCategoryLookUp(relevantDoc.getCategoryLookupName());
                }
                if (fp.getListPanelTool() != null) {
                    item.setCustomFieldsMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(relevantDoc, fp.getListPanelTool().getColumnCodeName()));
                }
                itemList.add(item);
            }
        }
        return new ListResult<>(itemList, totalNumber);
    }

    @Override
    public PayslipTableRequestObject getSinglePayrunPdfData(Integer singlePayrunID) {
        BigDecimal amount, totalPayToDate, totalPayToDateForOldPayslips;
        PayslipTableRequestObject result = null;

        EdsPayslipTableItem item = payslipTableItemManager.get(singlePayrunID);
        if (item != null) {
            totalPayToDate = payslipTableItemManager.getTotalPayToDate(item.getEmployee().getObjectID(), item.getToDate(), item.getYear());
            totalPayToDate = totalPayToDate != null ? totalPayToDate : BigDecimal.ZERO;
            totalPayToDateForOldPayslips = p11Manager.getTotalPayToDate(item.getEmployee().getObjectID(), item.getToDate(), item.getYear());
            totalPayToDateForOldPayslips = totalPayToDateForOldPayslips != null ? totalPayToDateForOldPayslips : BigDecimal.ZERO;
            totalPayToDate = totalPayToDate.add(totalPayToDateForOldPayslips);
            result = new PayslipTableRequestObject();
            if (item.getEmployee() != null) {
                result.setEmployeeId(item.getEmployee().getObjectID());
                result.setEmployeeName(item.getEmployee().getFullName());
                result.setEmployeeCode(item.getEmployee().getProfile().getEmployeeCode());
                result.setDriverNumber(item.getEmployee().getDriverNumber());
                result.setEmployeeDepartment(item.getEmployee().getEmployeeDepartment() != null && item.getEmployee().getEmployeeDepartment().getTeam() != null ? item.getEmployee().getEmployeeDepartment().getTeam().getName() : "");
                result.setEmployeePosition(item.getEmployee().getPosition() != null ? item.getEmployee().getPosition().getName() : "");
                result.setEmployeeLocation(item.getEmployee().getLocation() != null ? item.getEmployee().getLocation().getName() : "");
                result.setEmployeeHireDate(item.getEmployee().getStartDate());
                result.setResignationDate(item.getEmployee().getEndDate());
                EdsUserBankAccount userBankAccount = userBankAccountManager.getUserBankAccountByUser(userManager.get(item.getEmployee().getObjectID()));
                if (userBankAccount != null) {
                    result.setiBanCode(userBankAccount.getIbanCode() != null ? userBankAccount.getIbanCode() : "");
                    result.setBankAccountNumber(userBankAccount.getAccountNumber());
                }
                result.setWorkDays(BigDecimal.valueOf(availabilityServiceLocal.getEmployeeWorkDaysCountInMonth(item.getEmployee().getObjectID(), item.getFromDate(), item.getToDate())));
                ArrayList<CompanyCustomFieldItem> customFieldItems = commonService.getCompanyCustomFields(ViewName.Employee);
                customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(item.getEmployee().getCustomFields(), customFieldItems);
                for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
                    if (!CompanyCustomFieldItem.DATE.equals(customFieldItem.getDataType())) {
                        result.addCustomField(customFieldItem.getFieldName(), customFieldItem.getFieldStringValue());
                    }
                }
                String rateType = getEmployeeSettingValue(item.getEmployee().getObjectID(), RATE_TYPE, "");
                BigDecimal salary = item.getBasicSalary() != null ? item.getBasicSalary() : new BigDecimal(getEmployeeSettingValue(item.getEmployee().getObjectID(), SALARY, "0.0"));
                result.setEmployeePaymentType(rateType);
                if (FIXED_HRMS_OVERTIME_RATE.equals(result.getEmployeePaymentType())) {
                    ListingFilterParameter lfp = new ListingFilterParameter();
                    lfp.setEmployeeId(result.getEmployeeId());
                    lfp.setStartDate(item.getFromDate());
                    lfp.setEndDate(item.getToDate());
                    List<DailyOvertimeData> overTimeData = attendanceRawDataManager.getDailyOvertimeData(lfp);
                    for (DailyOvertimeData data : overTimeData) {
                        result.getDailyOvertimeData().put(data.getDate(), data);
                    }
                } else if (FIXED_TIMESHEET_OVERTIME_RATE.equals(result.getEmployeePaymentType())) {
                    ListingFilterParameter lfp = new ListingFilterParameter();
                    lfp.setEmployeeId(result.getEmployeeId());
                    lfp.setStartDate(item.getFromDate());
                    MonthlyOvertimeDataWithRates overtimeDataWithRates = monthlyTimesheetManager.getMonthlyTimesheetDataWithOvertimeRatesForPayroll(lfp);
                    if (overtimeDataWithRates != null) {
                        if (overtimeDataWithRates.getRateType() != null && overtimeDataWithRates.getRate() != null) {
                            result.setOvertimeDataWithRates(overtimeDataWithRates);
                        }
                    }
                } else if (FIXED_OVERTIME_RATE.equals(result.getEmployeePaymentType())) {
                    ListingFilterParameter lfp = new ListingFilterParameter();
                    lfp.setEmployeeId(result.getEmployeeId());
                    lfp.setStartDate(item.getFromDate());
                    MonthlyOvertimeDataWithRates overtimeDataWithRates = monthlyTimesheetManager.getMonthlyTimesheetDataWithOvertimeRatesForPayroll(lfp);
                    overtimeDataWithRates.setRate(salary);
                    overtimeDataWithRates.setOvertimeRate(new BigDecimal(getEmployeeSettingValue(item.getEmployee().getObjectID(), REGULAR_OVERTIME_RATE, "0.0")));
                    overtimeDataWithRates.setWeekendOvertimeRate(new BigDecimal(getEmployeeSettingValue(item.getEmployee().getObjectID(), WEEKEND_OVERTIME_RATE, "0.0")));
                    overtimeDataWithRates.setHolidayOvertimeRate(new BigDecimal(getEmployeeSettingValue(item.getEmployee().getObjectID(), HOLIDAY_OVERTIME_RATE, "0.0")));
                    if (overtimeDataWithRates != null) {
                        result.setOvertimeDataWithRates(overtimeDataWithRates);
                    }
                } else {
                    MonthlyOvertimeDataWithRates overtimeDataWithRates = new MonthlyOvertimeDataWithRates();
                    overtimeDataWithRates.setRate(salary);
                    result.setOvertimeDataWithRates(overtimeDataWithRates);
                }
            }
            if (item.getPaymentMethod() != null && item.getPaymentMethod().getName() != null) {
                result.setPayMethod(item.getPaymentMethod().getName());
            }
            result.setCreator(item.getPreparer() != null ? item.getPreparer().getName() : "");
            result.setCreatedDate(item.getCreationDate());
            result.setApprover(item.getApprover() != null ? item.getApprover().getName() : "");
            result.setApproveDate(item.getApprovedDate());
            result.setProcessDate(item.getProcessDate());
            result.setMonth(item.getMonth());
            result.setMonthId(item.getMonthID());
            result.setYear(item.getYear());
            result.setWorkedDays(item.getDaysWorked() != null ? item.getDaysWorked().toString() : "");
            result.setBasicSalary(item.getBasicSalary());
            result.setDailyRate(item.getDailyRate());
            result.setActualMonthPay(item.getActualMonthPay());
            result.setAllowance(item.getAllowance());
            result.setAdditionalPay(item.getAdditionalPay());
            result.setDeduction(item.getDeduction());
            result.setTax(item.getTax());
            result.setExpense(item.getExpense());
            result.setPaymentPolicy(item.getPaymentPolicy());
            result.setTotal(item.getTotal());
            result.setPensionAmount(item.getPensionAmount());
            result.setFromDate(item.getFromDate());
            result.setToDate(item.getToDate());
            result.setRejectionNote(item.getRejectionNote());
            result.setDescription(item.getDescription());
            result.setTotalPayToDate(totalPayToDate);
            result.setTotalInBase(item.getTotalInBase());
            result.setCurrency(item.getCurrency() != null ? item.getCurrency().getName() : null);
            result.setExchangeRate(item.getExchangeRate());
            result.setMonthlyCollection(item.getCollection());
            result.setSpentFlueAmount(item.getUsedPetrol());
            result.setMonthlySalik(item.getMonthlySalik());
            result.setObjectID(item.getObjectID());

            List<EdsPaymentDeduction> categories = payslipTableItemManager.getItemCategories(item.getObjectID());
            LinkedList<PaymentDeductionObject> payments = new LinkedList<>();
            LinkedList<PaymentDeductionObject> deductions = new LinkedList<>();
            BigDecimal allowanceAndBasicSalaryTotal = BigDecimal.ZERO;
            if (categories != null && categories.size() > 0) {
                for (EdsPaymentDeduction paymentDeduction : categories) {
                    PaymentDeductionObject object = paymentDeduction.getRPC();
                    amount = payslipPaymentsManager.getPaymentAmount(paymentDeduction.getObjectID(), item.getObjectID());
                    object.setTotalAmount(amount);
                    object.setLeaveDaysCount(paymentDeduction.getLeaveDaysCount());
                    if (object.isPaymentCategory()) {
                        payments.add(object);
                        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PAYSLIP_FROM_TIMESHEET) && ((object.getType() != null && object.getType() == 0) || object.isSalaryObject())) {
                            allowanceAndBasicSalaryTotal = allowanceAndBasicSalaryTotal.add(object.getPaymentAmount());
                        }
                    } else {
                        deductions.add(object);
                    }
                }
            }
            result.setAllPaymentCategories(payments);
            result.setAllDeductionCategories(deductions);
            result.setAllowanceAndBasicSalaryTotal(allowanceAndBasicSalaryTotal);

            EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
            EdsCurrency baseCurrency = fs.getCurrency();
            BigDecimal expAmount = BigDecimal.ZERO;
            List<ExpenseData> expenses = new LinkedList<>();
            String expensePaidFromAccount = getCompanyPayrollSettings(EXPENSE_PAID_ACCOUNT);
            EdsAccount paidFromAccount = expensePaidFromAccount != null && !expensePaidFromAccount.isEmpty() ? accountingManager.get(Integer.valueOf(expensePaidFromAccount)) : null;
            boolean empInBase = item.getCurrency() == null || item.getCurrency().getObjectID().equals(baseCurrency.getObjectID());
            boolean expInBase;
            List<EdsExpenseReport> linkedExpenses = expenseReportManager.getPayslipTableItemRelatedExpenseClaims(item.getObjectID());
            for (EdsExpenseReport exp : linkedExpenses) {
                expInBase = empInBase || exp.getCurrency() == null || exp.getCurrency().getObjectID().equals(baseCurrency.getObjectID());
                ExpenseData expData;
                double total = expInBase ? exp.getBaseTotal().doubleValue() : exp.getTotal().doubleValue();
                if (PARTIALLY_PAID.equals(exp.getStatus().getCode())) {
                    double paid = exp.getPaidTotal(expInBase).doubleValue();
                    total -= paid;
                } else if (EXPENSE_PAID.equals(exp.getStatus().getCode())) {
                    total = exp.getPaidTotalByPayslip(item.getObjectID(), expInBase).doubleValue();
                }
                if (exp.getAccount() != null) {
                    expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, exp.getAccount().getObjectID(), exp.getAccount().getName(), exp.getPaymentType());
                } else {
                    if (paidFromAccount == null) {
                        expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, null, "", exp.getPaymentType());
                    } else {
                        expData = new ExpenseData(exp.getObjectID(), exp.getTitle(), total, expInBase, paidFromAccount.getObjectID(), paidFromAccount.getName(), exp.getPaymentType());
                    }
                }
                if (expData.isInBaseCurrency() && item.getExchangeRate() != null) {
                    expData.setAmount(expData.getAmount() * item.getExchangeRate().doubleValue());
                    expData.setInBaseCurrency(false);
                }
                expenses.add(expData);
                expAmount = expAmount.add(BigDecimal.valueOf(expData.getAmount()));
            }
            if (expenses.size() > 0) {
                expenses.sort(Comparator.comparingInt(ExpenseData::getObjectID).reversed());

                PaymentDeductionObject expensePayment = new PaymentDeductionObject();
                expensePayment.setPaymentAmount(expAmount);
                expensePayment.setExpenses((expenses.toArray(new ExpenseData[]{})));
                result.setEmployeeExpenses(expensePayment);
            }
        }
        return result;
    }

    @Override
    public void deletePensionProvider(Integer pensionProviderID) {
        EdsPensionProvider pensionProvider = pensionProviderManager.get(pensionProviderID);
        if (pensionProvider != null) {
            pensionProvider.setDeleted(true);
        }
    }

    @Override
    public ListResult<PensionProviderData> getPensionProviders(ListingFilterParameter filterParameter) {
        PensionProviderData data;
        Integer totalCount = pensionProviderManager.getPensionProviderSize();
        List<EdsPensionProvider> pensionProviders = pensionProviderManager.getPensionProviders(filterParameter);
        ArrayList<PensionProviderData> ppData = new ArrayList<>();
        for (EdsPensionProvider pensionProvider : pensionProviders) {
            data = new PensionProviderData();
            data.setObjectID(pensionProvider.getObjectID());
            data.setProviderName(pensionProvider.getName());
            data.setProviderAddress(pensionProvider.getProviderAddress());
            if (pensionProvider.getProviderCountry() != null) {
                data.setProviderCounty(pensionProvider.getProviderCountry().getName());
            }
            data.setProviderEmail(pensionProvider.getProviderEmail());
            data.setProviderTelNo(pensionProvider.getProviderTelNo());
            data.setProviderCPName(pensionProvider.getProviderCPName());
            ppData.add(data);
        }

        return new ListResult<>(ppData, totalCount);
    }

    @Override
    public PensionProviderData getPensionProvider(Integer objectID) {
        PensionProviderData result = new PensionProviderData();
        EdsPensionProvider pensionProvider = pensionProviderManager.get(objectID);
        if (pensionProvider != null) {
            result.setObjectID(pensionProvider.getObjectID());
            result.setProviderName(pensionProvider.getName());
            result.setProviderAccountRef(pensionProvider.getProviderAccountRef());
            result.setProviderOtherRef(pensionProvider.getProviderOtherRef());
            result.setProviderAddress(pensionProvider.getProviderAddress());
            result.setProviderTownCity(pensionProvider.getProviderTownCity());
            result.setProviderPostCode(pensionProvider.getProviderPostCode());
            result.setProviderTelNo(pensionProvider.getProviderTelNo());
            result.setProviderFaxNo(pensionProvider.getProviderFaxNo());
            result.setProviderEmail(pensionProvider.getProviderEmail());
            result.setProviderCPName(pensionProvider.getProviderCPName());
            result.setProviderCPMobile(pensionProvider.getProviderCPMobile());
            result.setBankName(pensionProvider.getBankName());
            result.setBranchName(pensionProvider.getBranchName());
            result.setBankAddress(pensionProvider.getBankAddress());
            result.setBankTownCity(pensionProvider.getBankTownCity());
            result.setBankCPName(pensionProvider.getBankCPName());
            result.setBankTelNo(pensionProvider.getBankTelNo());
            result.setBankFaxNo(pensionProvider.getBankFaxNo());
            result.setBankEmail(pensionProvider.getBankEmail());
            result.setSortCode(pensionProvider.getSortCode());
            result.setAccountNo(pensionProvider.getAccountNo());
            result.setNameShownOnAccount(pensionProvider.getNameShownOnAccount());
            result.setBankAccountRef(pensionProvider.getBankAccountRef());

            if (pensionProvider.getProviderCountry() != null) {
                result.setProviderCountry(new SelectItem(pensionProvider.getProviderCountry().getObjectID(), pensionProvider.getProviderCountry().getName()));
            }

            if (pensionProvider.getBankCountry() != null) {
                result.setBankCountry(new SelectItem(pensionProvider.getBankCountry().getObjectID(), pensionProvider.getBankCountry().getName()));
            }
        }
        return result;
    }

    private void createSinglePayrunForCalculation(EdsEosCalculation data) {
        EdsPayslipTableItem singlePayrun = new EdsPayslipTableItem();
        singlePayrun.setEmployee(data.getEmployee());
        singlePayrun.setPreparer(data.getCreator());
        singlePayrun.setCreationDate(data.getCreationDate());
        singlePayrun.setFromDate(DateUtil.getMonthFirstDay(data.getCreationDate()));
        singlePayrun.setToDate(DateUtil.getMonthLastDate(data.getCreationDate()));
        singlePayrun.setFromEndOfService(true);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(data.getCreationDate());
        singlePayrun.setMonthID(calendar.get(Calendar.MONTH));
        singlePayrun.setYear(calendar.get(Calendar.YEAR));
        singlePayrun.setMonth(new SimpleDateFormat("MMMM").format(data.getCreationDate()));
        singlePayrun.setStatus(referenceManager.findReference(PAYRUN_STATUS, PAYRUN_STATUS_DRAFT));
        singlePayrun.setTotal(data.getTotalAmount());
        if (data.getExchangeRate() != null) {
            singlePayrun.setExchangeRate(data.getExchangeRate());
            if (data.getTotalAmount() != null) {
                singlePayrun.setTotalInBase(data.getTotalAmount().divide(data.getExchangeRate(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
            }
        }
        singlePayrun.setCurrency(data.getCurrency());
        singlePayrun.setLastUpdateTime(new Date());
        payslipTableItemManager.createOrUpdate(singlePayrun);
        addSinglePayrunToSolr(singlePayrun);
        EdsPayrollCategory category = categoryManager.getCategoryByCode(END_OF_SERVICE);
        if (category != null) {
            EdsPaymentDeduction newPayment = new EdsPaymentDeduction();
            newPayment.setCategoryId(category.getObjectID());
            newPayment.setPaymentAmount(data.getTotalAmount());
            newPayment.setEmployeeId(data.getEmployee().getObjectID());
            newPayment.setPaymentDate(data.getCreationDate());
            paymentDeductionManager.create(newPayment);
            EdsPayslipPayments payslipPayments = new EdsPayslipPayments();
            payslipPayments.setPaymentDeductionID(newPayment.getObjectID());
            payslipPayments.setPayslipItemID(singlePayrun.getObjectID());
            payslipPayments.setPaymentTotal(data.getTotalAmount());
            payslipPaymentsManager.create(payslipPayments);
        }

        data.setSinglePayrun(singlePayrun);
        endOfServiceGratuityManager.update(data);
    }

    @Override
    public ArrayList<MyUpdateItem> getSinglePayrunUpdates(Integer objectID) {
        List<EdsMyUpdate> myUpdates = myUpdateManager.getUpdatesForAffectedID(objectID, MyUpdateTypeManager.SINGLE_PAYRUN);
        ArrayList<MyUpdateItem> updates = new ArrayList<>();
        for (EdsMyUpdate myUpdate : myUpdates) {
            MyUpdateItem item = new MyUpdateItem();
            item.setType(myUpdate.getEventType());
            item.setSubType(myUpdate.getTypeCode());
            item.setEventDate(myUpdate.getDate());
            item.setUserName(userManager.get(myUpdate.getInducerID()).getName());
            StringBuilder message = new StringBuilder();
            if (userManager.getUser().getName().equals(item.getUserName())) {
                message.append("You have ");
            } else {
                message.append(item.getUserName()).append(" has ");
            }
            switch (item.getSubType()) {
                case MyUpdateTypeManager.SINGLE_PAYRUN_ADD -> message.append(SUB_TYPE_ADDED);
                case MyUpdateTypeManager.SINGLE_PAYRUN_EDIT -> message.append(SUB_TYPE_EDITED);
                case MyUpdateTypeManager.SINGLE_PAYRUN_DELETE -> message.append(SUB_TYPE_DELETED);
                case MyUpdateTypeManager.SINGLE_PAYRUN_SUBMIT -> message.append(SUB_TYPE_SUBMITTED);
                case MyUpdateTypeManager.SINGLE_PAYRUN_APPROVE -> message.append(SUB_TYPE_APPROVED);
                case MyUpdateTypeManager.SINGLE_PAYRUN_REJECT -> message.append(SUB_TYPE_DECLINED);
            }
            item.setMessage(message.toString());
            updates.add(item);
        }
        return updates;
    }

    @Override
    public ArrayList<MyUpdateItem> getGroupPayrunUpdates(Integer objectID) {
        List<EdsMyUpdate> myUpdates = myUpdateManager.getUpdatesForAffectedID(objectID, MyUpdateTypeManager.GROUP_PAYRUN);
        ArrayList<MyUpdateItem> updates = new ArrayList<>();
        for (EdsMyUpdate myUpdate : myUpdates) {
            MyUpdateItem item = new MyUpdateItem();
            item.setType(myUpdate.getEventType());
            item.setSubType(myUpdate.getTypeCode());
            item.setEventDate(myUpdate.getDate());
            item.setUserName(userManager.get(myUpdate.getInducerID()).getName());
            StringBuilder message = new StringBuilder();
            if (userManager.getUser().getName().equals(item.getUserName())) {
                message.append("You have ");
            } else {
                message.append(item.getUserName()).append(" has ");
            }
            switch (item.getSubType()) {
                case MyUpdateTypeManager.GROUP_PAYRUN_ADD -> message.append("added");
                case MyUpdateTypeManager.GROUP_PAYRUN_EDIT -> message.append("edited");
                case MyUpdateTypeManager.GROUP_PAYRUN_DELETE -> message.append("deleted");
                case MyUpdateTypeManager.GROUP_PAYRUN_SUBMIT -> message.append("submitted");
                case MyUpdateTypeManager.GROUP_PAYRUN_APPROVE -> message.append("approved");
                case MyUpdateTypeManager.GROUP_PAYRUN_REJECT -> message.append("declined");
            }
            item.setMessage(message.toString());
            updates.add(item);
        }
        return updates;
    }

    @Override
    public ArrayList<MyUpdateItem> getCashAdvanceUpdates(Integer objectID) {
        List<EdsMyUpdate> myUpdates = myUpdateManager.getUpdatesForAffectedID(objectID, MyUpdateTypeManager.CASH_ADVANCE);
        ArrayList<MyUpdateItem> updates = new ArrayList<>();
        for (EdsMyUpdate myUpdate : myUpdates) {
            MyUpdateItem item = new MyUpdateItem();
            item.setType(myUpdate.getEventType());
            item.setSubType(myUpdate.getTypeCode());
            item.setEventDate(myUpdate.getDate());
            item.setUserName(userManager.get(myUpdate.getInducerID()).getName());
            StringBuilder message = new StringBuilder();
            if (userManager.getUser().getName().equals(item.getUserName())) {
                message.append("You have ");
            } else {
                message.append(item.getUserName()).append(" has ");
            }
            switch (item.getSubType()) {
                case MyUpdateTypeManager.CASH_ADVANCE_ADD -> message.append("added");
                case MyUpdateTypeManager.CASH_ADVANCE_EDIT -> message.append("edited");
                case MyUpdateTypeManager.CASH_ADVANCE_DELETE -> message.append("deleted");
                case MyUpdateTypeManager.CASH_ADVANCE_SUBMIT -> message.append("submitted");
                case MyUpdateTypeManager.CASH_ADVANCE_APPROVE -> message.append("approved");
                case MyUpdateTypeManager.CASH_ADVANCE_REJECT -> message.append("declined");
            }
            item.setMessage(message.toString());
            updates.add(item);
        }
        return updates;
    }

    /**
     * Payment for Payslip
     */

    public PayrunPayment initPayrunPayment(ListingFilterParameter fp) {
        final PayrunPayment result = new PayrunPayment();

        result.setTotalItems(0);
        result.setItems(new ArrayList<>());
        if (fp.getGroupPayrunID() == null) {
            return result;
        }
        final EdsPayslipTable payslipTable = payslipTableManager.get(fp.getGroupPayrunID());

        if (payslipTable == null) {
            return result;
        }
        final Integer count = payslipTableItemManager.getCountByFilter(fp);
        final ArrayList<PayrunPaymentItem> paymentItems = Lists.newArrayListWithExpectedSize(fp.getLimit());

        result.setTotalItems(count);
        if (count <= 0) {
            return result;
        }
        PayrollTotalTO payrollTotalTO = payslipTableItemManager.getTotalAmountGroupId(payslipTable.getObjectID());
        result.setAmount(payrollTotalTO.getTotalApprovedAmount());

        payslipTableItemManager.getListByFilter(fp).forEach(item -> initPayrunPaymentItem(item).ifPresent(paymentItems::add));
        result.setItems(paymentItems);
        return result;
    }

    public PayrunPaymentItem initPayrunPaymentItem(Integer singlePayrunID) {
        PayrunPaymentItem result = new PayrunPaymentItem();
        if (singlePayrunID == null) {
            return result;
        }

        EdsPayslipTableItem singlePayrun = payslipTableItemManager.get(singlePayrunID);
        if (singlePayrun == null) {
            return result;
        }

        Optional<PayrunPaymentItem> itemOptional = initPayrunPaymentItem(singlePayrun);
        if (itemOptional.isPresent()) {
            result = itemOptional.get();
        }

        return result;
    }

    private Optional<PayrunPaymentItem> initPayrunPaymentItem(EdsPayslipTableItem item) {
        if (item.getEmployee() == null) {
            return Optional.empty();
        }
        PayrunPaymentItem paymentItem = new PayrunPaymentItem();
        paymentItem.setEmployeeID(item.getEmployee().getObjectID());
        paymentItem.setEmployee(item.getEmployee().getFullName());
        paymentItem.setSinglePayrunID(item.getObjectID());
        paymentItem.setCurrency(item.getCurrency() != null ? item.getCurrency().createCurrencyItem() : null);
        paymentItem.setExchangeRate(item.getExchangeRate());
        if (item.getEmployee().getProfile() != null && !StringUtil.isEmpty(item.getEmployee().getProfile().getEmployeeCode())) {
            paymentItem.setEmployee(item.getEmployee().getProfile().getEmployeeCode().concat(" -> ").concat(item.getEmployee().getFullName()));
        }

        paymentItem.setDueDate(new DateNonConvertable(item.getToDate()));
        BigDecimal paymentAmount = payrunPaymentItemManager.getTotalPaymentBySinglePayrunId(item.getObjectID());
        List<EdsPayrunPaymentItem> existingPaymentItems = payrunPaymentItemManager.getPayrunPaymentItems(item.getObjectID());
        BigDecimal dueAmount = item.getTotal().subtract(paymentAmount);

        //check if it is already paid
        if (dueAmount.compareTo(BigDecimal.ZERO) <= 0 && existingPaymentItems != null && !CollectionUtils.isEmpty(existingPaymentItems)) {
            return Optional.empty();
        }
        paymentItem.setDueAmount(dueAmount);

        return Optional.of(paymentItem);
    }

    @Transactional
    public SaveResultTO<Integer> createPayrunPayment(PayrunPayment payment) {
        SaveResultTO<Integer> result = new SaveResultTO<>();
        if (payment == null) {
            return result.setMessage("Incorrect incoming data");
        }
        if (payment.getGroupPayrunID() == null) {
            return result.setMessage("Incorrect payrun data");
        }
        if (payment.getPaymentDate() == null) {
            return result.setMessage("Please, select the payment date");
        }

        EdsPayslipTable payslipTable = payslipTableManager.get(payment.getGroupPayrunID());

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setGroupPayrunID(payment.getGroupPayrunID());
        fp.setLimit(payment.getTotalItems());
        PayrunPayment rawPayment = initPayrunPayment(fp);

        EdsPayrunPayment edsPayment = new EdsPayrunPayment();

        edsPayment.setBankAccountID(payment.getPaidFromAccountID());
        edsPayment.setPaidToAccountID(payment.getPaidToAccountID());
        edsPayment.setDetails(payment.getDetails());
        edsPayment.setPaymentDate(payment.getPaymentDate().getNonConvertedDate());
        edsPayment.setPayslipTable(payslipTable);

        edsPayment.setPaymentDate(payment.getPaymentDate().getNonConvertedDate());

        edsPayment.setCurrency(payment.getCurrency() != null ? currencyManager.get(payment.getCurrency().getId()) : null);
        edsPayment.setExchangeRate(payment.getExchangeRate());

        payrunPaymentManager.create(edsPayment);

        HashMap<Integer, PayrunPaymentItem> changedItems = payment.getChangedItems();
        HashMap<Integer, Boolean> deletedItems = payment.getDeletedItems();

        BigDecimal paymentTotal = BigDecimal.ZERO;
        Set<EdsPayrunPaymentItem> paymentItems = new HashSet<>();
        for (PayrunPaymentItem paymentItem : rawPayment.getItems()) {
            if (deletedItems != null && deletedItems.containsKey(paymentItem.getEmployeeID())) {
                continue;
            } else if (changedItems != null && changedItems.containsKey(paymentItem.getEmployeeID())) {
                paymentItem = changedItems.get(paymentItem.getEmployeeID());
            }

            EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(paymentItem.getSinglePayrunID());
            if (payslipTableItem == null) {
                continue;
            }

            if (paymentItem.getPaidFromAccountID() == null) {
                paymentItem.setPaidFromAccountID(payment.getPaidFromAccountID());
            }
            if (paymentItem.getPaidToAccountID() == null) {
                paymentItem.setPaidToAccountID(payment.getPaidToAccountID());
            }
            if (paymentItem.getPaymentAmount() == null) {
                paymentItem.setPaymentAmount(paymentItem.getDueAmount());
            }
            paymentItem.setPaymentDate(payment.getPaymentDate());

            paymentItem.setCurrency(payment.getCurrency());
            paymentItem.setExchangeRate(payment.getExchangeRate());

            EdsPayrunPaymentItem edsPaymentItem = createPayrunPaymentItem(paymentItem, edsPayment, payslipTableItem);

            paymentTotal = paymentTotal.add(edsPaymentItem.getPaymentAmount());
            paymentItems.add(edsPaymentItem);
        }

        edsPayment.setItems(paymentItems);
        edsPayment.setAmount(paymentTotal);
        edsPayment.setAmountInBase(paymentTotal.divide(Optional.ofNullable(edsPayment.getExchangeRate()).orElse(BigDecimal.ONE), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        payrunPaymentManager.update(edsPayment);

        payrunPaymentManager.flush();

        if (!Objects.equals("true", getCompanyPayrollSettings(DISABLE_PAYROLL_TRANSACTIONS))) {
            createTransactionForPayrunPayment(edsPayment);
        }

        updatePayslipTableItemStatusAfterPayment(payslipTable.getObjectID());

        return result.setResult(edsPayment.getObjectID());
    }

    @Transactional
    public SaveResultTO<Integer> createPayrunPaymentItem(PayrunPaymentItem paymentItem) {
        SaveResultTO<Integer> result = new SaveResultTO<>();
        if (paymentItem == null) {
            return result.setMessage("Incorrect incoming data");
        }

        EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(paymentItem.getSinglePayrunID());
        if (payslipTableItem == null) {
            return result.setMessage("Incorrect payrun data");
        }

        EdsPayrunPaymentItem edsPaymentItem = createPayrunPaymentItem(paymentItem, null, payslipTableItem);

        if (!Objects.equals("true", getCompanyPayrollSettings(DISABLE_PAYROLL_TRANSACTIONS))) {
            createTransactionForSinglePayrunPayment(edsPaymentItem);
        }

        updateSinglePayrunStatusAfterPayment(payslipTableItem.getObjectID());

        return result.setResult(edsPaymentItem.getObjectID());
    }

    private EdsPayrunPaymentItem createPayrunPaymentItem(PayrunPaymentItem paymentItem, EdsPayrunPayment edsPayment, EdsPayslipTableItem payslipTableItem) {
        EdsPayrunPaymentItem edsPaymentItem = new EdsPayrunPaymentItem();
        edsPaymentItem.setPayrunPayment(edsPayment);
        edsPaymentItem.setEmployee(employeeManager.get(paymentItem.getEmployeeID()));
        edsPaymentItem.setPayslipTableItem(payslipTableItem);

        edsPaymentItem.setPaidFromAccountID(paymentItem.getPaidFromAccountID());
        edsPaymentItem.setPaidToAccountID(paymentItem.getPaidToAccountID());

        edsPaymentItem.setBankAccount(paymentItem.getBankAccount());
        edsPaymentItem.setReference(paymentItem.getReference());
        edsPaymentItem.setDetails(paymentItem.getDetails());

        edsPaymentItem.setPaymentDate(paymentItem.getPaymentDate().getNonConvertedDate());

        edsPaymentItem.setCurrency(paymentItem.getCurrency() != null ? currencyManager.get(paymentItem.getCurrency().getId()) : null);
        edsPaymentItem.setExchangeRate(paymentItem.getExchangeRate());

        edsPaymentItem.setPaymentAmount(paymentItem.getPaymentAmount());

        payrunPaymentItemManager.create(edsPaymentItem);

        return edsPaymentItem;
    }

    private void createTransactionForPayrunPayment(EdsPayrunPayment edsPayment) {
        for (EdsPayrunPaymentItem paymentItem : edsPayment.getItems()) {
            createTransactionForSinglePayrunPayment(paymentItem);
        }
    }

    public void createTransactionForSinglePayrunPayment(EdsPayrunPaymentItem paymentItem) {
        EdsUser user = transactionManager.getUser();

        EdsPayslipTableItem payslipTableItem = paymentItem.getPayslipTableItem();
        boolean enabledMultiCurrency = "true".equals(getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL));
        BigDecimal exchangeRate = enabledMultiCurrency && paymentItem.getExchangeRate() != null ? paymentItem.getExchangeRate() : BigDecimal.ONE;

        EdsPayrunPaymentTransaction transaction = transactionManager.getTransactionByPayrunPayment(paymentItem.getObjectID());
        if (transaction == null) {
            transaction = new EdsPayrunPaymentTransaction();
            transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID() + 1);
        } else {
            transactionManager.deleteTransactionItems(transaction.getObjectID());
        }
        transaction.setPaymentItem(paymentItem);
        transaction.setJournalDate(paymentItem.getPaymentDate() != null ? paymentItem.getPaymentDate() : payslipTableItem.getToDate());
        transaction.setName(paymentItem.getEmployee().getFullName() + " Payrun Payment For " + payslipTableItem.getMonth());
        transaction.setPostedDate(user.getUserDate());
        transaction.setPostedBy(user);
        transaction.setReference(paymentItem.getReference());
        transactionManager.createOrUpdate(transaction);

        List<EdsTransactionItem> transactionItemList = new ArrayList<>();
        EdsTransactionItem creditItem = new EdsTransactionItem();
        creditItem.setAccount(accountingManager.get(paymentItem.getPaidFromAccountID()));
        creditItem.setCredit(paymentItem.getPaymentAmount().divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        transactionItemList.add(creditItem);

        EdsTransactionItem debitItem = new EdsTransactionItem();
        debitItem.setAccount(accountingManager.get(paymentItem.getPaidToAccountID()));
        debitItem.setDebit(paymentItem.getPaymentAmount().divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        transactionItemList.add(debitItem);

        if (!transactionItemList.isEmpty()) {
            for (EdsTransactionItem ti : transactionItemList) {
                ti.setTransaction(transaction);
            }
        }
        transaction.getTransactionItems().addAll(transactionItemList);

        transactionManager.createOrUpdate(transaction);
    }

    public void updatePayslipTableItemStatusAfterPayment(Integer payslipID) {
        EdsPayslipTable payslipTable = payslipTableManager.get(payslipID);
        if (payslipTable == null) {
            return;
        }

        List<EdsPayslipTableItem> changedItems = new ArrayList<>();
        for (EdsPayslipTableItem payslipTableItem : payslipTable.getPayslipTableItems()) {
            payslipTableItem = updateSinglePayrunStatusAfterPaymentInternal(payslipTableItem);

            baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, payslipTableItem, userManager.getUser());
            changedItems.add(payslipTableItem);
        }
        addSinglePayrunToSolr(changedItems.toArray(new EdsPayslipTableItem[]{}));
        payslipTableItemManager.flush();

        updatePayslipTableStatusAfterPayment(payslipID);
    }

    public void updateSinglePayrunStatusAfterPayment(Integer singlePayrunID) {
        EdsPayslipTableItem payslipTableItem = payslipTableItemManager.get(singlePayrunID);
        if (payslipTableItem == null) {
            return;
        }

        payslipTableItem = updateSinglePayrunStatusAfterPaymentInternal(payslipTableItem);

        baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, payslipTableItem, userManager.getUser());
        addSinglePayrunToSolr(payslipTableItem);

        if (payslipTableItem.getPayslipTable() != null) {
            updatePayslipTableStatusAfterPayment(payslipTableItem.getPayslipTable().getObjectID());
        }
    }

    private EdsPayslipTableItem updateSinglePayrunStatusAfterPaymentInternal(EdsPayslipTableItem singlePayrun) {
        EdsReference partialPaidStatus = referenceManager.findReference(PAYRUN_STATUS, Constants.PAYRUN_STATUS_PARTIAL_PAID);
        EdsReference paidStatus = referenceManager.findReference(PAYRUN_STATUS, Constants.PAYRUN_STATUS_PAID);
        EdsReference approvedStatus = referenceManager.findReference(PAYRUN_STATUS, Constants.PAYRUN_STATUS_APPROVED);

        List<EdsPayrunPaymentItem> paymentItems = payrunPaymentItemManager.getPayrunPaymentItems(singlePayrun.getObjectID());
        BigDecimal paymentTotal = payrunPaymentItemManager.getTotalPaymentBySinglePayrunId(singlePayrun.getObjectID());
        if (paymentItems == null || CollectionUtils.isEmpty(paymentItems)) {
            singlePayrun.setStatus(approvedStatus);
            singlePayrun.setStatus2(approvedStatus);
        } else if (paymentTotal.compareTo(singlePayrun.getTotal()) < 0) {
            singlePayrun.setStatus(partialPaidStatus);
            singlePayrun.setStatus2(partialPaidStatus);
        } else {
            singlePayrun.setStatus(paidStatus);
            singlePayrun.setStatus2(paidStatus);
        }
        singlePayrun.setLastUpdateTime(new Date());
        payslipTableItemManager.update(singlePayrun);

        return singlePayrun;
    }

    private void updatePayslipTableStatusAfterPayment(Integer payslipID) {
        EdsPayslipTable payslipTable = payslipTableManager.get(payslipID);
        if (payslipTable == null) {
            return;
        }

        EdsReference partialPaidStatus = referenceManager.findReference(PAYRUN_STATUS, Constants.PAYRUN_STATUS_PARTIAL_PAID);
        EdsReference paidStatus = referenceManager.findReference(PAYRUN_STATUS, Constants.PAYRUN_STATUS_PAID);
        EdsReference approvedStatus = referenceManager.findReference(PAYRUN_STATUS, Constants.PAYRUN_STATUS_APPROVED);

        List<EdsPayrunPayment> payments = payrunPaymentManager.getPayrunPayments(payslipID);
        BigDecimal paymentTotal = payrunPaymentManager.getTotalPaymentByPayrunId(payslipID);
        if (payments == null || CollectionUtils.isEmpty(payments)) {
            payslipTable.setStatus(approvedStatus);
            payslipTable.setStatus2(approvedStatus);
        } else if (paymentTotal.compareTo(payslipTable.getTotalAmount()) < 0) {
            payslipTable.setStatus(partialPaidStatus);
            payslipTable.setStatus2(partialPaidStatus);
        } else {
            payslipTable.setStatus(paidStatus);
            payslipTable.setStatus2(paidStatus);
        }
        payslipTableManager.update(payslipTable);
        addGroupPayrunToSolr(payslipTable);
        baseEventsPostProcessor.registerEvent(GroupPayrunEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, payslipTable, userManager.getUser());
    }

    public ListResult<PayrunPayment> getPayrunPaymentList(ListingFilterParameter fp) {
        ListResult<PayrunPayment> listResult = new ListResult<>();
        listResult.setTotal(0);
        if (fp == null) {
            return listResult;
        }
        List<EdsPayrunPayment> payments = payrunPaymentManager.getPayrunPayments(fp.getGroupPayrunID());
        ArrayList<PayrunPayment> items = new ArrayList<>();
        if (payments != null && !CollectionUtils.isEmpty(payments)) {
            for (EdsPayrunPayment payment : payments) {
                items.add(payment.toSimpleRPC());
            }
        }
        listResult.setList(items);
        listResult.setTotal(items.size());
        return listResult;
    }

    public PayrunPayment getPayrunPayment(ListingFilterParameter fp) {
        PayrunPayment result = new PayrunPayment();
        result.setTotalItems(0);
        result.setItems(new ArrayList<>());

        EdsPayrunPayment payment = payrunPaymentManager.get(fp.getObjectId());
        if (payment == null) {
            return result;
        }
        result = payment.toSimpleRPC();

        EdsAccount paidFromAccount = accountingManager.get(result.getPaidFromAccountID());
        EdsAccount paidToAccount = accountingManager.get(result.getPaidToAccountID());

        result.setPaidFromAccount(paidFromAccount.createAccountItem());
        result.setPaidToAccount(paidToAccount.createAccountItem());

        List<EdsPayrunPaymentItem> items = payrunPaymentItemManager.getListByFilter(fp);

        ArrayList<PayrunPaymentItem> paymentItems = new ArrayList<>();
        for (EdsPayrunPaymentItem item : items) {
            PayrunPaymentItem paymentItem = item.toRPC();

            if (paymentItem.getPaidFromAccountID() != null) {
                EdsAccount paidFromAccount1 = accountingManager.get(paymentItem.getPaidFromAccountID());
                paymentItem.setPaidFromAccount(paidFromAccount1.createAccountItem());
            }
            if (paymentItem.getPaidToAccountID() != null) {
                EdsAccount paidToAccount1 = accountingManager.get(paymentItem.getPaidToAccountID());
                paymentItem.setPaidToAccount(paidToAccount1.createAccountItem());
            }

            paymentItems.add(paymentItem);

        }
        result.setItems(paymentItems);
        result.setTotalItems(paymentItems.size());

        return result;
    }

    public PayrunPaymentItem getPayrunPaymentItem(Integer paymentId) {
        PayrunPaymentItem result = new PayrunPaymentItem();
        if (paymentId == null) {
            return result;
        }

        EdsPayrunPaymentItem paymentItem = payrunPaymentItemManager.get(paymentId);
        if (paymentItem == null) {
            return result;
        }
        result = paymentItem.toRPC();

        if (paymentItem.getPaidFromAccountID() != null) {
            result.setPaidFromAccountID(paymentItem.getPaidFromAccountID());
            EdsAccount paidFromAccount = accountingManager.get(paymentItem.getPaidFromAccountID());
            result.setPaidFromAccount(paidFromAccount.createAccountItem());
        }
        if (paymentItem.getPaidToAccountID() != null) {
            result.setPaidToAccountID(paymentItem.getPaidToAccountID());
            EdsAccount paidToAccount = accountingManager.get(paymentItem.getPaidToAccountID());
            result.setPaidToAccount(paidToAccount.createAccountItem());
        }

        return result;
    }

    public Boolean deletePayrunPayment(Integer objectID) {
        EdsPayrunPayment payment = payrunPaymentManager.get(objectID);
        if (payment == null) {
            return Boolean.TRUE;
        }

        for (EdsPayrunPaymentItem paymentItem : payment.getItems()) {
            paymentItem.setDeleted(true);
            payrunPaymentItemManager.update(paymentItem);

            transactionManager.deleteTransactionsByPayrunPayment(paymentItem.getObjectID());
        }
        payment.setDeleted(true);
        payrunPaymentManager.update(payment);


        if (payment.getPayslipTable() != null) {
            updatePayslipTableItemStatusAfterPayment(payment.getPayslipTable().getObjectID());
        }
        return Boolean.TRUE;
    }

    public Boolean deletePayrunPaymentItem(Integer objectID) {
        EdsPayrunPaymentItem paymentItem = payrunPaymentItemManager.get(objectID);
        if (paymentItem == null) {
            return Boolean.TRUE;
        }

        EdsReference partialPaidStatus = referenceManager.findReference(PAYRUN_STATUS, Constants.PAYRUN_STATUS_PARTIAL_PAID);
        EdsReference approvedStatus = referenceManager.findReference(PAYRUN_STATUS, Constants.PAYRUN_STATUS_APPROVED);

        EdsPayrunPayment payment = paymentItem.getPayrunPayment();
        if (payment != null) {
            boolean deletePayment = payrunPaymentItemManager.isLastItemInPayrunPayment(payment.getObjectID());
            if (deletePayment) {
                payment.setDeleted(true);
            } else {
                payment.setAmount(payment.getAmount().subtract(paymentItem.getPaymentAmount()));
                payment.setAmountInBase(payment.getAmount().divide(Optional.ofNullable(payment.getExchangeRate()).orElse(BigDecimal.ONE), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
            }
            payrunPaymentManager.update(payment);
        }

        paymentItem.setDeleted(true);
        payrunPaymentItemManager.update(paymentItem);

        transactionManager.deleteTransactionsByPayrunPayment(objectID);

        EdsPayslipTableItem payslipTableItem = paymentItem.getPayslipTableItem();
        if (payslipTableItem != null) {
            List<EdsPayrunPaymentItem> paymentItems = payrunPaymentItemManager.getPayrunPaymentItems(payslipTableItem.getObjectID());
            BigDecimal paymentTotal = payrunPaymentItemManager.getTotalPaymentBySinglePayrunId(payslipTableItem.getObjectID());
            if (paymentItems != null && !CollectionUtils.isEmpty(paymentItems) && paymentTotal.compareTo(payslipTableItem.getTotal()) < 0) {
                payslipTableItem.setStatus(partialPaidStatus);
                payslipTableItem.setStatus2(partialPaidStatus);
            } else {
                payslipTableItem.setStatus(approvedStatus);
                payslipTableItem.setStatus2(approvedStatus);
            }
            payslipTableItemManager.update(payslipTableItem);
            baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, payslipTableItem, userManager.getUser());
            addSinglePayrunToSolr(payslipTableItem.getObjectID());

            if (payslipTableItem.getPayslipTable() != null) {
                updatePayslipTableStatusAfterPayment(payslipTableItem.getPayslipTable().getObjectID());
            }
        }
        return Boolean.TRUE;
    }

    /**
     * End of Payment for Payslip
     * */

    /**
     * Payment for Additional payments
     */

    public PayrollPayment initPayrollPayment(ListingFilterParameter fp) {
        final PayrollPayment result = new PayrollPayment();

        result.setTotalItems(0);
        result.setItems(new ArrayList<>());
        if (fp.getGroupPayrunID() == null) {
            return result;
        }
        final EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(fp.getGroupPayrunID());

        if (additionalPayment == null) {
            return result;
        }
        if (additionalPayment.getShowInPaySlip()) {
            return result;
        }
        final Integer count = paymentDeductionManager.getAdditionalPaymentCountByFilter(fp);
        final ArrayList<PayrollPaymentItem> paymentItems = Lists.newArrayListWithExpectedSize(fp.getLimit());

        result.setTotalItems(count);
        if (count <= 0) {
            return result;
        }
        result.setAmount(additionalPayment.getTotal());

        paymentDeductionManager.getAdditionalPaymentItemListByFilter(fp).forEach(item -> initPayrollPaymentItem(item).ifPresent(paymentItems::add));
        result.setItems(paymentItems);
        return result;
    }

    public PayrollPaymentItem initPayrollPaymentItem(Integer paymentDeductionId) {
        PayrollPaymentItem result = new PayrollPaymentItem();
        if (paymentDeductionId == null) {
            return result;
        }

        EdsPaymentDeduction item = paymentDeductionManager.get(paymentDeductionId);
        if (item == null) {
            return result;
        }

        Optional<PayrollPaymentItem> itemOptional = initPayrollPaymentItem(item);
        if (itemOptional.isPresent()) {
            result = itemOptional.get();
        }

        return result;
    }

    private Optional<PayrollPaymentItem> initPayrollPaymentItem(EdsPaymentDeduction item) {
        if (item.getEmployee() == null) {
            return Optional.empty();
        }
        PayrollPaymentItem paymentItem = new PayrollPaymentItem();
        paymentItem.setEmployeeID(item.getEmployee().getObjectID());
        paymentItem.setEmployee(item.getEmployee().getFullName());
        paymentItem.setAdditionalPaymentItemID(item.getObjectID());
//        paymentItem.setCurrency(item.getCurrency() != null ? item.getCurrency().createCurrencyItem() : null);
        paymentItem.setExchangeRate(BigDecimal.ONE);
        if (item.getEmployee().getProfile() != null && !StringUtil.isEmpty(item.getEmployee().getProfile().getEmployeeCode())) {
            paymentItem.setEmployee(item.getEmployee().getProfile().getEmployeeCode().concat(" -> ").concat(item.getEmployee().getFullName()));
        }

        paymentItem.setDueDate(new DateNonConvertable(item.getAdditionalPaymentDate()));
        BigDecimal paymentAmount = payrollPaymentItemManager.getTotalPaymentBySingleAddPaymentId(item.getObjectID());
        List<EdsPayrollPaymentItem> existingPaymentItems = payrollPaymentItemManager.getPayrollPaymentItems(item.getObjectID());
        BigDecimal dueAmount = item.getCategory().isNonMoneyType() ? BigDecimal.ZERO : item.getTotalAmount().subtract(paymentAmount);

        //check if it is already paid
        if (dueAmount.compareTo(BigDecimal.ZERO) <= 0 && existingPaymentItems != null && !CollectionUtils.isEmpty(existingPaymentItems)) {
            return Optional.empty();
        }
        paymentItem.setDueAmount(dueAmount);

        return Optional.of(paymentItem);
    }

    @Transactional
    public SaveResultTO<Integer> createPayrollPayment(PayrollPayment payment) {
        SaveResultTO<Integer> result = new SaveResultTO<>();
        if (payment == null) {
            return result.setMessage("Incorrect incoming data");
        }
        if (payment.getAdditionalPaymentID() == null) {
            return result.setMessage("Incorrect additional payment data");
        }
        if (payment.getPaymentDate() == null) {
            return result.setMessage("Please, select the payment date");
        }

        EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(payment.getAdditionalPaymentID());

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setGroupPayrunID(payment.getAdditionalPaymentID());
        fp.setLimit(10);
        PayrollPayment rawPayment = initPayrollPayment(fp);

        EdsPayrollPayment edsPayment = new EdsPayrollPayment();

        edsPayment.setBankAccountID(payment.getPaidFromAccountID());
        edsPayment.setPaidToAccountID(payment.getPaidToAccountID());
        edsPayment.setDetails(payment.getDetails());
        edsPayment.setPaymentDate(payment.getPaymentDate().getNonConvertedDate());
        edsPayment.setAdditionalPayment(additionalPayment);

        edsPayment.setPaymentDate(payment.getPaymentDate().getNonConvertedDate());

        edsPayment.setCurrency(payment.getCurrency() != null ? currencyManager.get(payment.getCurrency().getId()) : null);
        edsPayment.setExchangeRate(payment.getExchangeRate());

        payrollPaymentManager.create(edsPayment);

        HashMap<Integer, PayrollPaymentItem> changedItems = payment.getChangedItems();
        HashMap<Integer, Boolean> deletedItems = payment.getDeletedItems();

        BigDecimal paymentTotal = BigDecimal.ZERO;
        Set<EdsPayrollPaymentItem> paymentItems = new HashSet<>();
        for (PayrollPaymentItem paymentItem : rawPayment.getItems()) {
            if (deletedItems != null && deletedItems.containsKey(paymentItem.getEmployeeID())) {
                continue;
            } else if (changedItems != null && changedItems.containsKey(paymentItem.getEmployeeID())) {
                paymentItem = changedItems.get(paymentItem.getEmployeeID());
            }

            EdsPaymentDeduction paymentDeduction = paymentDeductionManager.get(paymentItem.getAdditionalPaymentItemID());
            if (paymentDeduction == null) {
                continue;
            }

            if (paymentItem.getPaidFromAccountID() == null) {
                paymentItem.setPaidFromAccountID(payment.getPaidFromAccountID());
            }
            if (paymentItem.getPaidToAccountID() == null) {
                paymentItem.setPaidToAccountID(payment.getPaidToAccountID());
            }
            if (paymentItem.getPaymentAmount() == null) {
                paymentItem.setPaymentAmount(paymentItem.getDueAmount());
            }
            paymentItem.setPaymentDate(payment.getPaymentDate());

            paymentItem.setCurrency(payment.getCurrency());
            paymentItem.setExchangeRate(payment.getExchangeRate());

            EdsPayrollPaymentItem edsPaymentItem = createPayrollPaymentItem(paymentItem, edsPayment, paymentDeduction);

            paymentTotal = paymentTotal.add(edsPaymentItem.getPaymentAmount());
            paymentItems.add(edsPaymentItem);
        }

        edsPayment.setItems(paymentItems);
        edsPayment.setAmount(paymentTotal);
        edsPayment.setAmountInBase(paymentTotal.divide(Optional.ofNullable(edsPayment.getExchangeRate()).orElse(BigDecimal.ONE), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        payrollPaymentManager.update(edsPayment);

        if (!Objects.equals("true", getCompanyPayrollSettings(DISABLE_PAYROLL_TRANSACTIONS))) {
            createTransactionForPayrollPayment(edsPayment);
        }
        payrollPaymentManager.flush();

        updateAdditionalPaymentItemsStatusAfterPayment(additionalPayment.getObjectID());

        return result.setResult(edsPayment.getObjectID());
    }

    @Transactional
    public SaveResultTO<Integer> createPayrollPaymentItem(PayrollPaymentItem paymentItem) {
        SaveResultTO<Integer> result = new SaveResultTO<>();
        if (paymentItem == null) {
            return result.setMessage("Incorrect incoming data");
        }

        EdsPaymentDeduction paymentDeduction = paymentDeductionManager.get(paymentItem.getAdditionalPaymentItemID());
        if (paymentDeduction == null) {
            return result.setMessage("Incorrect payment data");
        }

        EdsPayrollPaymentItem edsPaymentItem = createPayrollPaymentItem(paymentItem, null, paymentDeduction);

        if (!Objects.equals("true", getCompanyPayrollSettings(DISABLE_PAYROLL_TRANSACTIONS))) {
            createTransactionForSinglePayrollPayment(edsPaymentItem);
        }

        updateAdditionalPaymentItemStatusAfterPayment(paymentDeduction.getObjectID());

        return result.setResult(edsPaymentItem.getObjectID());
    }

    private EdsPayrollPaymentItem createPayrollPaymentItem(PayrollPaymentItem paymentItem, EdsPayrollPayment edsPayment, EdsPaymentDeduction paymentDeduction) {
        EdsPayrollPaymentItem edsPaymentItem = new EdsPayrollPaymentItem();
        edsPaymentItem.setPayrollPayment(edsPayment);
        edsPaymentItem.setEmployee(employeeManager.get(paymentItem.getEmployeeID()));
        edsPaymentItem.setPaymentDeduction(paymentDeduction);

        edsPaymentItem.setPaidFromAccountID(paymentItem.getPaidFromAccountID());
        edsPaymentItem.setPaidToAccountID(paymentItem.getPaidToAccountID());

        edsPaymentItem.setBankAccount(paymentItem.getBankAccount());
        edsPaymentItem.setReference(paymentItem.getReference());
        edsPaymentItem.setDetails(paymentItem.getDetails());

        edsPaymentItem.setPaymentDate(paymentItem.getPaymentDate().getNonConvertedDate());

        edsPaymentItem.setCurrency(paymentItem.getCurrency() != null ? currencyManager.get(paymentItem.getCurrency().getId()) : null);
        edsPaymentItem.setExchangeRate(paymentItem.getExchangeRate());

        edsPaymentItem.setPaymentAmount(paymentItem.getPaymentAmount());

        payrollPaymentItemManager.create(edsPaymentItem);

        return edsPaymentItem;
    }

    private void createTransactionForPayrollPayment(EdsPayrollPayment edsPayment) {
        for (EdsPayrollPaymentItem paymentItem : edsPayment.getItems()) {
            createTransactionForSinglePayrollPayment(paymentItem);
        }
    }

    public void createTransactionForSinglePayrollPayment(EdsPayrollPaymentItem paymentItem) {
        EdsUser user = transactionManager.getUser();

        EdsPaymentDeduction paymentDeduction = paymentItem.getPaymentDeduction();
        boolean enabledMultiCurrency = "true".equals(getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL));
        BigDecimal exchangeRate = enabledMultiCurrency && paymentItem.getExchangeRate() != null ? paymentItem.getExchangeRate() : BigDecimal.ONE;

        EdsPayrollPaymentTransaction transaction = transactionManager.getTransactionByPayrollPayment(paymentItem.getObjectID());
        if (transaction == null) {
            transaction = new EdsPayrollPaymentTransaction();
            transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID() + 1);
        } else {
            transactionManager.deleteTransactionItems(transaction.getObjectID());
        }
        transaction.setPaymentItem(paymentItem);
        transaction.setJournalDate(paymentItem.getPaymentDate() != null ? paymentItem.getPaymentDate() : paymentDeduction.getAdditionalPaymentDate());
        transaction.setName("Additional Payroll Payment: " + paymentItem.getEmployee().getFullName());
        transaction.setPostedDate(user.getUserDate());
        transaction.setPostedBy(user);
        transaction.setReference(paymentItem.getReference());
        transactionManager.createOrUpdate(transaction);

        List<EdsTransactionItem> transactionItemList = new ArrayList<>();
        EdsTransactionItem creditItem = new EdsTransactionItem();
        creditItem.setAccount(accountingManager.get(paymentItem.getPaidFromAccountID()));
        creditItem.setCredit(paymentItem.getPaymentAmount().divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        transactionItemList.add(creditItem);

        EdsTransactionItem debitItem = new EdsTransactionItem();
        debitItem.setAccount(accountingManager.get(paymentItem.getPaidToAccountID()));
        debitItem.setDebit(paymentItem.getPaymentAmount().divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        transactionItemList.add(debitItem);

        if (!transactionItemList.isEmpty()) {
            for (EdsTransactionItem ti : transactionItemList) {
                ti.setTransaction(transaction);
            }
        }
        transaction.getTransactionItems().addAll(transactionItemList);

        transactionManager.createOrUpdate(transaction);
    }

    public void updateAdditionalPaymentItemsStatusAfterPayment(Integer additionalPaymentID) {
        EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(additionalPaymentID);
        if (additionalPayment == null) {
            return;
        }

        for (EdsPaymentDeduction paymentDeduction : additionalPayment.getItems()) {
            updateAdditionalPaymentItemStatusAfterPaymentInternal(paymentDeduction);
        }
        baseEventsPostProcessor.registerEvent(AdditionalPaymentEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, additionalPayment, userManager.getUser());

        updateAdditionalPaymentStatusAfterPayment(additionalPaymentID);
    }

    public void updateAdditionalPaymentItemStatusAfterPayment(Integer paymentDeductionID) {
        EdsPaymentDeduction paymentDeduction = paymentDeductionManager.get(paymentDeductionID);
        if (paymentDeduction == null) {
            return;
        }

        paymentDeduction = updateAdditionalPaymentItemStatusAfterPaymentInternal(paymentDeduction);

        if (paymentDeduction.getAdditionalPayment() != null) {
            updateAdditionalPaymentStatusAfterPayment(paymentDeduction.getAdditionalPayment().getObjectID());
        }
    }

    private EdsPaymentDeduction updateAdditionalPaymentItemStatusAfterPaymentInternal(EdsPaymentDeduction paymentDeduction) {
        EdsReference partialPaidStatus = referenceManager.findReference(PAYMENT_STATUS, Constants.PAYMENT_STATUS_PARTIAL_PAID);
        EdsReference paidStatus = referenceManager.findReference(PAYMENT_STATUS, Constants.PAYMENT_STATUS_PAID);
        EdsReference approvedStatus = referenceManager.findReference(PAYMENT_STATUS, Constants.PAYMENT_STATUS_APPROVED);

        List<EdsPayrollPaymentItem> paymentItems = payrollPaymentItemManager.getPayrollPaymentItems(paymentDeduction.getObjectID());
        BigDecimal paymentTotal = payrollPaymentItemManager.getTotalPaymentBySingleAddPaymentId(paymentDeduction.getObjectID());
        if (paymentItems == null || CollectionUtils.isEmpty(paymentItems)) {
            paymentDeduction.setStatus(approvedStatus);
        } else if (paymentTotal.compareTo(paymentDeduction.getPaymentAmount()) < 0) {
            paymentDeduction.setStatus(partialPaidStatus);
        } else {
            paymentDeduction.setStatus(paidStatus);
        }
        paymentDeductionManager.update(paymentDeduction);

        return paymentDeduction;
    }

    private void updateAdditionalPaymentStatusAfterPayment(Integer additionalPaymentID) {
        EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(additionalPaymentID);
        if (additionalPayment == null) {
            return;
        }

        EdsReference partialPaidStatus = referenceManager.findReference(PAYMENT_STATUS, Constants.PAYMENT_STATUS_PARTIAL_PAID);
        EdsReference paidStatus = referenceManager.findReference(PAYMENT_STATUS, Constants.PAYMENT_STATUS_PAID);
        EdsReference approvedStatus = referenceManager.findReference(PAYMENT_STATUS, Constants.PAYMENT_STATUS_APPROVED);

        List<EdsPayrollPayment> payments = payrollPaymentManager.getPayrollPayments(additionalPaymentID);
        BigDecimal paymentTotal = payrollPaymentManager.getTotalPaymentByAdditionalPaymentId(additionalPaymentID);
        if (payments == null || CollectionUtils.isEmpty(payments)) {
            additionalPayment.setEntityStatus(approvedStatus);
        } else if (paymentTotal.compareTo(additionalPayment.getTotal()) < 0) {
            additionalPayment.setEntityStatus(partialPaidStatus);
        } else {
            additionalPayment.setEntityStatus(paidStatus);
        }
        additionalPaymentManager.update(additionalPayment);
        addAdditionalPaymentToSolr(additionalPayment);
        baseEventsPostProcessor.registerEvent(AdditionalPaymentEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, additionalPayment, userManager.getUser());
    }

    public ListResult<PayrollPayment> getPayrollPaymentList(ListingFilterParameter fp) {
        ListResult<PayrollPayment> listResult = new ListResult<>();
        listResult.setTotal(0);
        if (fp == null) {
            return listResult;
        }
        List<EdsPayrollPayment> payments = payrollPaymentManager.getPayrollPayments(fp.getGroupPayrunID());
        ArrayList<PayrollPayment> items = new ArrayList<>();
        if (payments != null && !CollectionUtils.isEmpty(payments)) {
            for (EdsPayrollPayment payment : payments) {
                items.add(payment.toSimpleRPC());
            }
        }
        listResult.setList(items);
        listResult.setTotal(items.size());
        return listResult;
    }

    public PayrollPayment getPayrollPayment(ListingFilterParameter fp) {
        PayrollPayment result = new PayrollPayment();
        result.setTotalItems(0);
        result.setItems(new ArrayList<>());

        EdsPayrollPayment payment = payrollPaymentManager.get(fp.getObjectId());
        if (payment == null) {
            return result;
        }
        result = payment.toSimpleRPC();

        EdsAccount paidFromAccount = accountingManager.get(result.getPaidFromAccountID());
        EdsAccount paidToAccount = accountingManager.get(result.getPaidToAccountID());

        result.setPaidFromAccount(paidFromAccount.createAccountItem());
        result.setPaidToAccount(paidToAccount.createAccountItem());

        List<EdsPayrollPaymentItem> items = payrollPaymentItemManager.getListByFilter(fp);

        ArrayList<PayrollPaymentItem> paymentItems = new ArrayList<>();
        for (EdsPayrollPaymentItem item : items) {
            PayrollPaymentItem paymentItem = item.toRPC();

            if (paymentItem.getPaidFromAccountID() != null) {
                EdsAccount paidFromAccount1 = accountingManager.get(paymentItem.getPaidFromAccountID());
                paymentItem.setPaidFromAccount(paidFromAccount1.createAccountItem());
            }
            if (paymentItem.getPaidToAccountID() != null) {
                EdsAccount paidToAccount1 = accountingManager.get(paymentItem.getPaidToAccountID());
                paymentItem.setPaidToAccount(paidToAccount1.createAccountItem());
            }

            paymentItems.add(paymentItem);

        }
        result.setItems(paymentItems);
        result.setTotalItems(paymentItems.size());

        return result;
    }

    public PayrollPaymentItem getPayrollPaymentItem(Integer paymentId) {
        PayrollPaymentItem result = new PayrollPaymentItem();
        if (paymentId == null) {
            return result;
        }

        EdsPayrollPaymentItem paymentItem = payrollPaymentItemManager.get(paymentId);
        if (paymentItem == null) {
            return result;
        }
        result = paymentItem.toRPC();

        if (paymentItem.getPaidFromAccountID() != null) {
            result.setPaidFromAccountID(paymentItem.getPaidFromAccountID());
            EdsAccount paidFromAccount = accountingManager.get(paymentItem.getPaidFromAccountID());
            result.setPaidFromAccount(paidFromAccount.createAccountItem());
        }
        if (paymentItem.getPaidToAccountID() != null) {
            result.setPaidToAccountID(paymentItem.getPaidToAccountID());
            EdsAccount paidToAccount = accountingManager.get(paymentItem.getPaidToAccountID());
            result.setPaidToAccount(paidToAccount.createAccountItem());
        }

        return result;
    }

    public Boolean deletePayrollPayment(Integer objectID) {
        EdsPayrollPayment payment = payrollPaymentManager.get(objectID);
        if (payment == null) {
            return Boolean.TRUE;
        }

        for (EdsPayrollPaymentItem paymentItem : payment.getItems()) {
            paymentItem.setDeleted(true);
            payrollPaymentItemManager.update(paymentItem);

            transactionManager.deleteTransactionsByPayrollPayment(paymentItem.getObjectID());
        }
        payment.setDeleted(true);
        payrollPaymentManager.update(payment);


        if (payment.getAdditionalPayment() != null) {
            updatePayslipTableItemStatusAfterPayment(payment.getAdditionalPayment().getObjectID());
            updateAdditionalPaymentItemsStatusAfterPayment(payment.getAdditionalPayment().getObjectID());
        }
        return Boolean.TRUE;
    }

    public Boolean deletePayrollPaymentItem(Integer objectID) {
        EdsPayrollPaymentItem paymentItem = payrollPaymentItemManager.get(objectID);
        if (paymentItem == null) {
            return Boolean.TRUE;
        }

        EdsReference partialPaidStatus = referenceManager.findReference(PAYMENT_STATUS, Constants.PAYMENT_STATUS_PARTIAL_PAID);
        EdsReference approvedStatus = referenceManager.findReference(PAYMENT_STATUS, Constants.PAYMENT_STATUS_APPROVED);

        EdsPayrollPayment payment = paymentItem.getPayrollPayment();
        if (payment != null) {
            boolean deletePayment = payrollPaymentItemManager.isLastItemInPayrollPayment(payment.getObjectID());
            if (deletePayment) {
                payment.setDeleted(true);
            } else {
                payment.setAmount(payment.getAmount().subtract(paymentItem.getPaymentAmount()));
                payment.setAmountInBase(payment.getAmount().divide(Optional.ofNullable(payment.getExchangeRate()).orElse(BigDecimal.ONE), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
            }
            payrollPaymentManager.update(payment);
        }

        paymentItem.setDeleted(true);
        payrollPaymentItemManager.update(paymentItem);

        transactionManager.deleteTransactionsByPayrollPayment(objectID);

        EdsPaymentDeduction paymentDeduction = paymentItem.getPaymentDeduction();
        if (paymentDeduction != null) {
            List<EdsPayrollPaymentItem> paymentItems = payrollPaymentItemManager.getPayrollPaymentItems(paymentDeduction.getObjectID());
            BigDecimal paymentTotal = payrollPaymentItemManager.getTotalPaymentBySingleAddPaymentId(paymentDeduction.getObjectID());
            if (paymentItems != null && !CollectionUtils.isEmpty(paymentItems) && paymentTotal.compareTo(paymentDeduction.getPaymentAmount()) < 0) {
                paymentDeduction.setStatus(partialPaidStatus);
            } else {
                paymentDeduction.setStatus(approvedStatus);
            }
            paymentDeductionManager.update(paymentDeduction);

            if (paymentDeduction.getAdditionalPayment() != null) {
                updateAdditionalPaymentStatusAfterPayment(paymentDeduction.getAdditionalPayment().getObjectID());
            }
        }
        return Boolean.TRUE;
    }

    /**
     * End of Payment for Additional payment
     */

    @Override
    public List<HistoryNote> loadPaymentNotes(Integer objectId) {
        if (objectId == null) {
            return new ArrayList<>();
        }

        List<HistoryNote> result = new ArrayList<>();
        HistoryListItem[] asHistoryItems = getAsHistoryItems(noteManager.getAdditionalPaymentNote(additionalPaymentManager.get(objectId)));
        if (asHistoryItems != null) {
            result.addAll(Arrays.asList(asHistoryItems));
        }
        result.addAll(getAllHistory(objectId, ADDITIONAL_PAYMENT));
        return result;
    }

    private HistoryListItem[] getAsHistoryItems(List<EdsAdditionalPaymentNote> notes) {
        if (notes != null && notes.size() > 0) {
            HistoryListItem[] hisItems = new HistoryListItem[notes.size()];
            int i = 0;
            for (EdsAdditionalPaymentNote note : notes) {
                hisItems[i] = new HistoryListItem();
                hisItems[i].setObjectID(note.getObjectID());
                hisItems[i].setComment(note.getComment());
                if (note.isSuperUser()) {
                    hisItems[i].setEmployee(Constants.defaultSupportName);
                } else {
                    hisItems[i].setEmployee(note.getCommentator().getFullName());
                }
                hisItems[i].setEmployeeID(note.getCommentator().getObjectID());
                hisItems[i].setEventDate(note.getDate());
                i++;
            }
            return hisItems;
        }
        return null;
    }

    @Override
    public Integer createPaymentHistoryNote(Integer objectId, HistoryListItem hisItem) {
        EdsAdditionalPayment payment = null;
        if (objectId != null) {
            payment = additionalPaymentManager.get(objectId);
        }
        if (hisItem.getObjectID() == null) {
            EdsAdditionalPaymentNote note = new EdsAdditionalPaymentNote();
            note.setComment(hisItem.getComment());
            note.setCommentator(additionalPaymentManager.getUser());
            note.setPayment(payment);
            note.setDate(new Date());
            note.setSuperUser(ServerUtils.isSuperUser());
            noteManager.create(note);
            return note.getObjectID();
        }
        return null;
    }


    public List<MyUpdateItem> getAllHistory(Integer objectID, String viewType) {

        List<EdsMyUpdate> myUpdates = new ArrayList<>();
        String relationType = null;

        if (Constants.ADDITIONAL_PAYMENT.equals(viewType)) {
            myUpdates = myUpdateManager.getUpdatesForAffectedID(objectID, MyUpdateTypeManager.ADDITIONAL_PAYMENT);
            relationType = RelationItem.TYPE_ADDITIONAL_PAYMENT;
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

    @Override
    public AdditionalPayment getAdditionalPaymentData(ListingFilterParameter fp) {
        AdditionalPayment result = new AdditionalPayment();
        if (fp.getObjectId() != null) {
            if (fp.getEmployeeId() != null) {
                List<EdsPaymentDeduction> items = additionalPaymentManager.getAdditionalPaymentItemList(fp);
                for (EdsPaymentDeduction item : items) {
                    if (item != null && item.getAdditionalPayment() != null) {
                        EdsAdditionalPayment additionalPayment = item.getAdditionalPayment();
                        result = item.getAdditionalPayment().getRPC(false);
                        String[] empIds = result.getEmployeeIds() != null ? result.getEmployeeIds().split(",") : new String[]{};
                        EdsEmployee edsEmployee = item.getEmployee();
                        if (empIds.length == 1) {
                            edsEmployee = employeeManager.get(Integer.parseInt(empIds[0]));
                        }
                        if (edsEmployee != null) {
                            if (edsEmployee.getPosition() != null) {
                                result.setEmployeePosition(edsEmployee.getPosition().getName());
                            }
                            if (edsEmployee.getPayMethod() != null) {
                                result.setEmployeePayMethod(edsEmployee.getPayMethod().getName());
                            }
                        }
                        if (result.isBasicPlusAllowance()) {
                            result.setAllowancePaymentCategories(getPaymentCategoriesBySelectedId(item.getAdditionalPayment().getAllowanceCategoriyIds()));
                        }
                        result.setTotal(item.getPaymentAmount());
                        PaymentDeductionObject itemRPC = item.getRPC();
                        if (itemRPC.getEmployee() != null) {
                            ListingFilterParameter filterParams = new ListingFilterParameter();
                            filterParams.setMonthId(additionalPayment.getMonthID());
                            filterParams.setYear(additionalPayment.getYear());
                            filterParams.setEmployeeId(itemRPC.getEmployee().getId());
                            itemRPC.setCountIncident(getIncidentCountByDynamicId(filterParams, itemRPC.getEmployee().getId()));
                        }
                        result.addItem(itemRPC);
                    }
                }
            } else {
                EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(fp.getObjectId());
                if (additionalPayment != null && !additionalPayment.getDeleted()) {

                    ArrayList<PaymentDeductionSelectItem> categorySelectItems = getPaymentCategoriesBySelectedId(additionalPayment.getAllowanceCategoriyIds());
                    result = additionalPayment.getRPC();
                    String[] empIds = result.getEmployeeIds() != null ? result.getEmployeeIds().split(",") : new String[]{};
                    if (empIds.length == 1) {
                        EdsEmployee edsEmployee = employeeManager.get(Integer.parseInt(empIds[0]));
                        if (edsEmployee != null) {
                            result.setEmployee(edsEmployee.getAsSelectItem());
                            result.getEmployee().setDescription(edsEmployee.getProfile() != null ? edsEmployee.getProfile().getEmployeeCode() : null);
                            EmployeeDataDetail employeeDataDetail = new EmployeeDataDetail();
                            employeeDataDetail.setPostion(edsEmployee.getPosition() != null ? edsEmployee.getPosition().getName() : "");
                            employeeDataDetail.setDepartment(edsEmployee.getTeam() != null ? edsEmployee.getTeam().getName() : "");
                            employeeDataDetail.setLocation(edsEmployee.getLocation() != null ? edsEmployee.getLocation().getName() : "");
                            employeeDataDetail.setEmploymentMode(edsEmployee.getProfile() != null &&
                                    edsEmployee.getProfile().getEmploymentMode() != null ? edsEmployee.getProfile().getEmploymentMode().getName() : "");
                            final HashMap<String, String> payrollSettings = this.employeeServiceLocal.getEmployeePayrollSettings(edsEmployee.getObjectID());
                            final String salaryValue = payrollSettings.get(Constants.SALARY);
                            if (salaryValue != null && !"".equals(salaryValue)) {
                                employeeDataDetail.setBasicSalary(Double.parseDouble(salaryValue));
                            } else {
                                employeeDataDetail.setBasicSalary(0d);
                            }
                            result.setEmployeeDataDetail(employeeDataDetail);
                            for (EdsPaymentDeduction edsPaymentDeduction : edsEmployee.getCategories()) {
                                if (edsPaymentDeduction.getPayType() != null && edsPaymentDeduction.getPayType().equals(1)) {
                                    employeeDataDetail.setAllowance(edsPaymentDeduction.getPercentage() != null ? String.valueOf(edsPaymentDeduction.getPercentage()) : "");
                                }
                            }
                        }
                    }
                    result.setApproverSaved(approverManager.isExistApproverByEntityTypeAndEntityId(RelationItem.TYPE_ADDITIONAL_PAYMENT, additionalPayment.getObjectID()));
                    Gson gson = new Gson();
                    if (additionalPayment.getCalculationDetails() != null) {
                        PaymentCalculationDetail[] details = gson.fromJson(additionalPayment.getCalculationDetails(), PaymentCalculationDetail[].class);
                        result.setCalculationDetails(new ArrayList<>(Arrays.asList(details)));
                    }


                    if (additionalPayment.getFromId() != null && additionalPayment.getFromType() != null) {
                        result.setFromId(additionalPayment.getFromId());
                        result.setFromType(additionalPayment.getFromType());
                        EdsCustomFormItems customFormItems = customFormItems = customFormItemManager.get(additionalPayment.getFromId());
                        switch (additionalPayment.getFromType()) {
                            case END_OF_SERVICE ->
                                    result.setFromObject(new SelectItem(customFormItems.getObjectID(), "MATERIALJNAYA_POMOSCHJ_FORM", customFormItems.getFormCustomFields().getStringValue8(), customFormItems.getFormCustomFields().getStringValue8()));
                            case OVERTIME -> {
                                EdsOvertimeObject overtime = overtimeManager.get(additionalPayment.getFromId());
                                result.setFromObject(new SelectItem(overtime.getObjectID(), overtime.getOvertimeType(), overtime.getOvertimeCode(), overtime.getOvertimeCode()));
                            }
                            case BACKUPS_EMPLOYEE -> {
                                EdsBackupsEmployee backupEmployee = backupsEmployeeManager.get(additionalPayment.getFromId());
                                result.setFromObject(new SelectItem(backupEmployee.getObjectID(), backupEmployee.getName(), backupEmployee.getBackupEmployeecode(), backupEmployee.getBackupEmployeecode()));
                            }
                            case "MATERIALJNAYA_POMOSCHJ._FORM" ->
                                    result.setFromObject(new SelectItem(customFormItems.getObjectID(), "MATERIALJNAYA_POMOSCHJ._FORM", customFormItems.getFormCustomFields().getStringValue2(), customFormItems.getFormCustomFields().getStringValue2()));
                        }

                    }

                    if (additionalPayment.getLeaveRequestId() != null) {
                        result.setLeaveRequestId(additionalPayment.getLeaveRequestId());
                        EdsSickRequest sickRequest = sickRequestManager.get(additionalPayment.getLeaveRequestId());

                        if (sickRequest != null && sickRequest.getNumberData() != null) {
                            result.setFromObject(new SelectItem(sickRequest.getObjectID(),
                                    sickRequest.getNumberData(),
                                    Utils.formatDate(sickRequest.getStartDate(), userManager.getUser().getCompany()) + " - " + Utils.formatDate(sickRequest.getEndDate(), userManager.getUser().getCompany()), sickRequest.getNumberData()));
                        } else {
                            result.setFromObject(new SelectItem(0,
                                    "Удалено",
                                    "n/a"));
                        }
                    }

                    List<PaymentDeductionObject> items = new ArrayList<>();
                    fp.setGroupPayrunID(fp.getObjectId());
                    Integer itemCount = paymentDeductionManager.getAdditionalPaymentCountByFilter(fp);
                    result.setTotalItems(itemCount);
                    List<EdsPaymentDeduction> paymentItems = paymentDeductionManager.getAdditionalPaymentItemListByFilter(fp);

                    fp.setNewType(true);
                    Integer itemNonMoneyTypeCount = paymentDeductionManager.getAdditionalPaymentCountByFilter(fp);
                    Calendar calendar = Calendar.getInstance();
                    if (fp.getMonthId() == null) {
                        fp.setMonthId(additionalPayment.getMonthID());
                    }
                    if (fp.getYear() == null) {
                        fp.setYear(additionalPayment.getYear());
                    }
                    calendar.set(Calendar.MONTH, fp.getMonthId());
                    calendar.set(Calendar.YEAR, fp.getYear());
                    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                    Date date = calendar.getTime();

                    List<Integer> employeeIds = paymentItems.stream().map(EdsPaymentDeduction::getEmployeeId).collect(Collectors.toList());

                    String[] settingsKeys = {
                            PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS,
                            PayrollConstants.MATERIAL_AID_TYPE_FUNERAL,
                            PayrollConstants.MATERIAL_AID_TYPE_GIFT
                    };
                    Table<Integer, String, String> employeeSettingsMap = employeePayrollSettingsManager.getEmployeesPayrollSettingMap(employeeIds, settingsKeys);

                    if (!CollectionUtils.isEmpty(paymentItems)) {
                        ArrayList<CompanyCustomFieldItem> cfs = commonService.getCompanyCustomFields(ViewName.AdditionalPaymentItem);
                        for (EdsPaymentDeduction item : paymentItems) {
                            PaymentDeductionObject paymentDeductionObject = item.getRPC();
                            paymentDeductionObject.setItemCustomFields((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), cfs));
                            paymentDeductionObject.setCountIncident(0);
                            if (paymentDeductionObject.getEmployee() != null) {
                                ListingFilterParameter filterParams = new ListingFilterParameter();
                                filterParams.setMonthId(additionalPayment.getMonthID());
                                filterParams.setYear(additionalPayment.getYear());
                                filterParams.setEmployeeId(paymentDeductionObject.getEmployee().getId());
                                paymentDeductionObject.setCountIncident(getIncidentCountByDynamicId(filterParams, paymentDeductionObject.getEmployee().getId()));
                            }

                            if (!StringUtils.isEmpty(item.getTaxCategoryList())) {
                                List<PaymentDeductionObject> taxCategories = new ArrayList<>();
                                AdditionalPaymentItemCategory[] additionalPaymentItemCategories = gson.fromJson(item.getTaxCategoryList(), AdditionalPaymentItemCategory[].class);
                                if (additionalPaymentItemCategories != null && additionalPaymentItemCategories.length > 0) {
                                    for (int i = 0; i < additionalPaymentItemCategories.length; i++) {
                                        PaymentDeductionObject object = new PaymentDeductionObject();
                                        object.setId(additionalPaymentItemCategories[i].getId());
                                        object.setCategoryItem(additionalPaymentItemCategories[i].getCategoryItem());
                                        object.setPercentage(additionalPaymentItemCategories[i].getPercentage());
                                        object.setAmount(additionalPaymentItemCategories[i].getAmount());
                                        object.setType(additionalPaymentItemCategories[i].getType());
                                        taxCategories.add(object);
                                    }
                                }
                                paymentDeductionObject.setTaxCategories(taxCategories);
                            }
                            if (!StringUtils.isEmpty(item.getEmployerContributionCategoryList())) {
                                List<PaymentDeductionObject> employerContributions = new ArrayList<>();
                                AdditionalPaymentItemCategory[] additionalPaymentItemCategories = gson.fromJson(item.getEmployerContributionCategoryList(), AdditionalPaymentItemCategory[].class);
                                if (additionalPaymentItemCategories != null && additionalPaymentItemCategories.length > 0) {
                                    for (int i = 0; i < additionalPaymentItemCategories.length; i++) {
                                        PaymentDeductionObject object = new PaymentDeductionObject();
                                        object.setId(additionalPaymentItemCategories[i].getId());
                                        object.setCategoryItem(additionalPaymentItemCategories[i].getCategoryItem());
                                        object.setPercentage(additionalPaymentItemCategories[i].getPercentage());
                                        object.setAmount(additionalPaymentItemCategories[i].getAmount());
                                        object.setType(additionalPaymentItemCategories[i].getType());
                                        employerContributions.add(object);
                                    }
                                }
                                paymentDeductionObject.setEmployerContributionCategories(employerContributions);
                            }
                            if (!StringUtils.isEmpty(item.getCustomDeductionCategoryList())) {
                                List<PaymentDeductionObject> customDeductions = new ArrayList<>();
                                AdditionalPaymentItemCategory[] additionalPaymentItemCategories = gson.fromJson(item.getCustomDeductionCategoryList(), AdditionalPaymentItemCategory[].class);
                                if (additionalPaymentItemCategories != null && additionalPaymentItemCategories.length > 0) {
                                    for (int i = 0; i < additionalPaymentItemCategories.length; i++) {
                                        PaymentDeductionObject object = new PaymentDeductionObject();
                                        object.setId(additionalPaymentItemCategories[i].getId());
                                        object.setCategoryItem(additionalPaymentItemCategories[i].getCategoryItem());
                                        object.setPercentage(additionalPaymentItemCategories[i].getPercentage());
                                        object.setAmount(additionalPaymentItemCategories[i].getAmount());
                                        object.setType(additionalPaymentItemCategories[i].getType());
                                        customDeductions.add(object);
                                    }
                                }
                                paymentDeductionObject.setDeductionCategories(customDeductions);
                            }
                            if (paymentDeductionObject.getEmployee() != null && paymentDeductionObject.getEmployee().getId() != null) {
                                collectEmployeeCategories(employeeManager.get(paymentDeductionObject.getEmployee().getId()), paymentDeductionObject);
                            }

//                            paymentDeductionObject.getLgotaBalanceMap().put(PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS, calculateMaterialAidBalance(item.getEmployee(), employeeSettingsMap.row(item.getEmployeeId()), PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS, mrotValue, date));
//                            paymentDeductionObject.getLgotaBalanceMap().put(PayrollConstants.MATERIAL_AID_TYPE_FUNERAL, calculateMaterialAidBalance(item.getEmployee(), employeeSettingsMap.row(item.getEmployeeId()), PayrollConstants.MATERIAL_AID_TYPE_FUNERAL, mrotValue, date));
//                            paymentDeductionObject.getLgotaBalanceMap().put(PayrollConstants.MATERIAL_AID_TYPE_GIFT, calculateMaterialAidBalance(item.getEmployee(), employeeSettingsMap.row(item.getEmployeeId()), PayrollConstants.MATERIAL_AID_TYPE_GIFT, mrotValue, date));

                            items.add(paymentDeductionObject);
                        }
                    }
                    result.setMakePayment(!itemCount.equals(itemNonMoneyTypeCount));
                    result.setItems(items);
                    result.setAllowancePaymentCategories(categorySelectItems);
                    result.setCustomFields((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(additionalPayment.getCustomFields(), commonService.getCompanyCustomFields(ViewName.AdditionalPayment)));

                    if (additionalPayment.getPayments() != null && !CollectionUtils.isEmpty(additionalPayment.getPayments())) {
                        result.setPayments(additionalPayment.getPayments().stream().map(EdsPayrollPayment::toSimpleRPC).collect(Collectors.toList()));
                    }

                    BigDecimal totalSingleOnlyPayments = payrollPaymentItemManager.getTotalSinglePaymentsByAdditionalPaymentId(additionalPayment.getObjectID());
                    if (totalSingleOnlyPayments.compareTo(BigDecimal.ZERO) > 0) {
                        List<PayrollPayment> payments = Optional.ofNullable(result.getPayments()).orElse(new ArrayList<>());
                        PayrollPayment singlePayment = new PayrollPayment();
                        singlePayment.setAmount(totalSingleOnlyPayments);
                        payments.add(singlePayment);
                    }
                }
            }
            result.setDefaultCategory(result.getDefaultPayrollCategoryId() != null ? categoryManager.get(result.getDefaultPayrollCategoryId()).createPaymentDeductionSelectItem() : null);
        } else {
            EdsPayrollCategory payrollCategory = categoryManager.getDefaultCategory();
            if (payrollCategory != null) {
                result.setDefaultCategory(payrollCategory.createPaymentDeductionSelectItem());
            }
        }
        SelectItem[] departmentList = allInOneService.getPayrollDepartmentForLookUp(fp);
        if (departmentList != null) {
            result.setDepartmentList(departmentList);
        }
        result.setPdfTemplateList(getPayrollPdfTemplates(PdfReferenceCodeNameEnum.ADDITIONAL_PAYMENT.name()));
        List<CompanyCustomFieldItem> itemCFs = commonService.getCompanyAllCustomFields(ViewName.AdditionalPaymentItem);
        if (!CollectionUtils.isEmpty(itemCFs)) {
            result.setItemCustomFields((ArrayList<CompanyCustomFieldItem>) itemCFs);
        }
        result.setColumnConfigs(itemTableSettingService.getColumnConfigs(ItemTableEnum.ADDITIONAL_PAYMENT_ITEM));
        result.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_ADDITIONAL_PAYMENT));

        return result;
    }

    private ArrayList<PaymentDeductionSelectItem> getPaymentCategoriesBySelectedId(String allowanceCategoriyIds) {
        ArrayList<PaymentDeductionSelectItem> categories = new ArrayList<>();
        if (allowanceCategoriyIds == null || allowanceCategoriyIds.isEmpty())
            return categories;
        String[] ids = allowanceCategoriyIds.split(",");
        for (String id : ids) {
            try {
                Integer cId = Integer.parseInt(id);
                EdsPayrollCategory item = categoryManager.get(cId);
                if (item != null) {
                    categories.add(item.createPaymentDeductionSelectItem());
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        return categories;
    }

    @Override
    public void saveAdditionalPayment(AdditionalPayment data) {
        saveAdditionalPayment(data, false);
    }

    @Transactional
    public Integer saveAdditionalPayment(AdditionalPayment data, boolean forImport) {
        Integer additionalPaymentId = createAdditionalPayment(data, forImport);
        data.setObjectID(additionalPaymentId);
        if (!forImport && !data.isFromView()) {
            EdsAdditionalPayment edsAdditionalPayment = additionalPaymentManager.get(additionalPaymentId);
            String statusCode = edsAdditionalPayment.getStatus();
            EdsReference pendingStatus = referenceManager.getByCode(Constants.PAYRUN_STATUS_PENDING);
            edsAdditionalPayment.setEntityStatus(pendingStatus);
            additionalPaymentManager.update(edsAdditionalPayment);
            addAdditionalPaymentToSolr(edsAdditionalPayment);
            createAdditionalPaymentItems(data, statusCode);
        }
        return additionalPaymentId;
    }

    @Transactional
    public Integer createAdditionalPayment(AdditionalPayment data, boolean forImport) {
        boolean checkAfterProcessing = false;
        EdsAdditionalPayment payment = additionalPaymentManager.get(data.getObjectID());
        EdsCurrency currency = null;
        if (data.getCurrency() != null && data.getCurrency().getId() != null && data.getCurrency().getId() > 0) {
            currency = currencyManager.get(data.getCurrency().getId());
        }
        EdsUser user = userManager.getUser();
        if (payment == null) {
            checkAfterProcessing = true;
            payment = new EdsAdditionalPayment();
        } else if (PAYMENT_STATUS_DRAFT.equals(data.getOldStatusCode())) {
            checkAfterProcessing = true;
        }
        if (!data.isFromView()) {
            if (data.getObjectID() == null) {
                payment.setCreator(employeeManager.get(user.getObjectID()));
            }
            payment.setUpdater(user);
            payment.setReference(data.getReference());
            payment.setCreationDate(payment.getCreationDate() == null ? new Date() : payment.getCreationDate());
            payment.setLastUpdateTime(new Date());
            payment.setMonth(ServerUtils.getMonthFromCompatibleIndex(data.getMonthID()));
            payment.setMonthID(data.getMonthID());
            payment.setYear(data.getYear());
            payment.setType(data.getType());
            if (data.getTotal() != null && data.getTotal().compareTo(BigDecimal.ZERO) > 0) {
                payment.setTotal(data.getTotal());
            }
            if (data.getEntityType() != null) {
                payment.setEntityType(data.getEntityType());
            }
            payment.setCurrency(currency);
            payment.setShowInPaySlip(data.isShowInPayslip());
            payment.setDeleted(forImport);
            payment.setCategoryType(data.getCategoryType());
            payment.setPdfTemplateID(data.getPdfTemplateID());
            payment.setDefaultDate(data.getDefaultDate() != null ? data.getDefaultDate().getNonConvertedDate().getTime() : null);
            payment.setFixedAmount(data.getFixedAmount());
            payment.setPaymentType(data.getPaymentType());
            payment.setPercentage(data.getPercentage());
            payment.setLeaveRequestId(data.getLeaveRequestId());
            payment.setBackupsEmployeeId(data.getBackupsEmployeeId());
            payment.setDefaultPayrollCategoryId(data.getDefaultPayrollCategoryId());
            StringBuilder builder = new StringBuilder();
            if (data.isBasicPlusAllowance() && data.getAllowancePaymentCategories() != null && data.getAllowancePaymentCategories().size() > 0) {
                for (PaymentDeductionSelectItem item : data.getAllowancePaymentCategories()) {
                    builder.append(item.getId()).append(",");
                }
            }
            payment.setBasicAndAllowance(data.isBasicPlusAllowance());
            payment.setAllowanceCategoriyIds(builder.toString());
            if (data.getPayrollBatch() != null) {
                EdsPayrollBatch payrollBatch = payrollBatchManager.get(data.getPayrollBatch().getId());
                payment.setPayrollBatch(payrollBatch);
            }
            if (data.getEmployee() != null) {
                payment.setEmployeeIds(String.valueOf(data.getEmployee().getId()));
            } else if (data.getEmployeeIds() != null && !data.getEmployeeIds().isEmpty()) {
                payment.setEmployeeIds(data.getEmployeeIds());
            }
            if (data.getDepartment() != null) {
                payment.setDepartment(departmentManager.get(data.getDepartment().getId()));
            }
            if (data.getLocation() != null) {
                payment.setLocation(locationManager.get(data.getLocation().getId()));
            }
            if (data.getFromId() != null) {
                payment.setFromId(data.getFromId());
            }
            if (data.getFromType() != null) {
                payment.setFromType(data.getFromType());
            }

            if (data.getSupervisor() != null) {
                payment.setSupervisor(employeeManager.get(data.getSupervisor().getId()));
            }
            if (!CollectionUtils.isEmpty(data.getCalculationDetails())) {
                Gson gson = new Gson();
                payment.setCalculationDetails(gson.toJson(data.getCalculationDetails().toArray(new PaymentCalculationDetail[]{})));
            }
            if (!CollectionUtils.isEmpty(data.getCustomFields())) {
                payment.setCustomFields(createAdditionalPaymentCustomFields(payment.getCustomFields(), data.getCustomFields()));
            }

            additionalPaymentManager.createOrUpdate(payment);
            if (data != null && data.getHistoryList() != null) {
                data.getHistoryList();
                for (HistoryListItem historyListItem : data.getHistoryList()) {
                    EdsAdditionalPaymentNote paymentNote = noteManager.get(historyListItem.getObjectID());
                    paymentNote.setPayment(payment);
                    noteManager.createOrUpdate(paymentNote);
                }
            }
            if (payment.getObjectID() != null && data.getAttachments() != null && data.getAttachments().length > 0) {
                attachmentUtilsManager.saveAttachments(F_ADDITIONAL_PAYMENT, payment.getObjectID(), payment.getObjectID(), data.getAttachments());
            }
        }


        boolean statusChanged = payment.getOverallStatus() != null && !data.getStatusCode().equals(payment.getOverallStatus().getCode());

        if (!isOk(data.getApprovers())) {
            payment.setEntityStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, data.getStatusCode()));
        }

        if (isOk(data.getApprovers())) {
            data.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : data.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (payment.getCurrentApprover() != null && data.getStatusCode() != null && isFirstApprover) {
                        payment.getCurrentApprover().setStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, data.getStatusCode()));
                        payment.setEntityStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, Constants.PAYMENT_STATUS_SUBMITTED));
                        isFirstApprover = false;
                    } else if (payment.getCurrentApprover() != null && data.getStatusCode() != null) {
                        payment.getCurrentApprover().setStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, Constants.PAYMENT_STATUS_SUBMITTED));
                    }
                    if (data.getStatusCode() != null && !PAYMENT_STATUS_APPROVED.equals(data.getStatusCode())) {
                        payment.setEntityStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, data.getStatusCode()));
                    }
                    if (payment.isCurrentApproverRejected()) {
                        payment.setEntityStatus(payment.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(payment.getObjectID());
                edsApprover.setIs_default(false);

                if (data.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, data.getStatusCode()));
                    if (Constants.PAYMENT_STATUS_DRAFT.equals(data.getStatusCode())) {
                        payment.setEntityStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, data.getStatusCode()));
                    } else {
                        payment.setEntityStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, PAYMENT_STATUS_SUBMITTED));
                    }
                    isFirstApprover = false;
                } else if (data.getStatusCode() != null) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, PAYMENT_STATUS_SUBMITTED));
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

                if (payment.getCurrentApprover() == null) {
                    payment.setCurrentApprover(edsApprover);
                }
                payment.getApprovers().add(edsApprover);
            }
        }

        if (!forImport) {
            addAdditionalPaymentToSolr(payment);
        }
        if (data.getObjectID() == null) {
            baseEventsPostProcessor.registerEvent(AdditionalPaymentEventListenerImpl.TYPE, MyUpdateItem.ADD, payment, userManager.getUser());
            if (data.getStatusCode().equals(PAYMENT_STATUS_DRAFT)) {
                baseEventsPostProcessor.registerEvent(AdditionalPaymentEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, payment, userManager.getUser());
            }
            if (data.getStatusCode().equals(PAYMENT_STATUS_APPROVED)) {
                baseEventsPostProcessor.registerEvent(AdditionalPaymentEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, payment, userManager.getUser());
            }
        } else if (!PAYMENT_STATUS_APPROVED.equals(data.getStatusCode()) && !PAYMENT_STATUS_DRAFT.equals(data.getStatusCode())) {
            baseEventsPostProcessor.registerEvent(AdditionalPaymentEventListenerImpl.TYPE, MyUpdateItem.EDIT, payment, userManager.getUser());
        }


        if (!checkAfterProcessing) {
            /* Run workflow approval process */
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), payment, user);
            workflowEvent.setEntityType(RelationItem.TYPE_ADDITIONAL_PAYMENT);

            if (PAYMENT_STATUS_APPROVED.equals(payment.getStatus())) {
                payment.setApprovedDate(new Date());
                if (!payment.getShowInPaySlip()) {
                    additionalPaymentTransaction(payment);
                }
            }
        }
        if (PAYMENT_STATUS_SUBMITTED.equals(payment.getStatus())) {
            boolean hasAlerts = false;
            List<EdsWorkflowRule> rules = workflowRuleManager.getByModuleAndActions(WorkflowRule._WORKFLOW_MODULE_ADDITIONAL_PAYMENT, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT);
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
                    messageManager.sendAdditionalPaymentToApprover(payment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (PAYMENT_STATUS_APPROVED.equals(payment.getStatus())) {
            try {
                for (EdsPaymentDeduction item : payment.getItems()) {
                    messageManager.sendAdditionalPaymentToEmployee(item.getEmployee(), payment);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statusChanged || data.getStatusCode().equals(PAYMENT_STATUS_SUBMITTED)) {
            baseEventsPostProcessor.registerEvent(AdditionalPaymentEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, payment, userManager.getUser());
        }

        return payment.getObjectID();
    }

    private EdsAdditionalPaymentCustomFields createAdditionalPaymentCustomFields(EdsAdditionalPaymentCustomFields additionalPaymentCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (additionalPaymentCustomFields == null) {
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
                additionalPaymentCustomFields = new EdsAdditionalPaymentCustomFields();
                additionalPaymentCFManager.create(additionalPaymentCustomFields);
            }
            CustomFieldsUtils.setAccountingDomainObjectCustomFields(additionalPaymentCustomFields, customFieldItems);
            return additionalPaymentCustomFields;
        }
        return null;
    }

    @Override
    public void updatePaymentItemsAndStatus(AdditionalPayment data) {
        EdsAdditionalPayment payment = additionalPaymentManager.get(data.getObjectID());
        EdsUser user = userManager.getUser();
        //updating items
        if (!CollectionUtils.isEmpty(data.getItems())) {
            createAdditionalPaymentItems(data, data.getStatusCode());
        }

        //updating status
        EdsReference referenceStatus = referenceManager.findReference(Constants.PAYMENT_STATUS, data.getStatusCode());
        if (!PAYMENT_STATUS_APPROVED.equals(data.getStatusCode())) {
            payment.setOverallStatus(referenceStatus);
        } else if (PAYMENT_STATUS_APPROVED.equals(data.getStatusCode()) && payment.getOverallStatus() != null && PAYMENT_STATUS_DRAFT.equals(payment.getOverallStatus().getCode())) {
            payment.setOverallStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, Constants.PAYMENT_STATUS_SUBMITTED));
        }
        payment.updateStatus(referenceStatus);
        addAdditionalPaymentToSolr(payment);
        additionalPaymentManager.update(payment);

        if (!PAYMENT_STATUS_APPROVED.equals(data.getStatusCode()) && !PAYMENT_STATUS_DRAFT.equals(data.getStatusCode())) {
            baseEventsPostProcessor.registerEvent(AdditionalPaymentEventListenerImpl.TYPE, MyUpdateItem.EDIT, payment, userManager.getUser());
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), payment, user);
        workflowEvent.setEntityType(RelationItem.TYPE_ADDITIONAL_PAYMENT);

        if (PAYMENT_STATUS_APPROVED.equals(payment.getStatus())) {
            payment.setApprovedDate(new Date());
            if (!payment.getShowInPaySlip()) {
                additionalPaymentTransaction(payment);
            }
        }
        if (PAYMENT_STATUS_SUBMITTED.equals(payment.getStatus())) {
            boolean hasAlerts = false;
            List<EdsWorkflowRule> rules = workflowRuleManager.getByModuleAndActions(WorkflowRule._WORKFLOW_MODULE_ADDITIONAL_PAYMENT, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT);
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
                    messageManager.sendAdditionalPaymentToApprover(payment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (PAYMENT_STATUS_APPROVED.equals(payment.getStatus())) {
            try {
                for (EdsPaymentDeduction item : payment.getItems()) {
                    messageManager.sendAdditionalPaymentToEmployee(item.getEmployee(), payment);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (data.getStatusCode().equals(PAYMENT_STATUS_SUBMITTED)) {
            baseEventsPostProcessor.registerEvent(AdditionalPaymentEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, payment, userManager.getUser());
        }

    }

    @Transactional
    public void createAdditionalPaymentItems(AdditionalPayment data, String statusCode) {
        final String database = ServerSecurityContext.getInstance().getDatabase();
        final String companyId = ServerSecurityContext.getInstance().getCompanyId();
        final Integer userId = userManager.getUser().getObjectID();

        executor.execute(() -> {
            ServerSecurityContext.getInstance().setDatabase(database);
            ServerSecurityContext.getInstance().setCompanyId(companyId);
            ServerSecurityContext.getInstance().setStaticUserID(userId);

            ArrayList<Callable<Integer>> futureCall = new ArrayList<>();

            EdsAdditionalPayment edsAdditionalPayment = additionalPaymentManager.get(data.getObjectID());
            ListingFilterParameter filter = new ListingFilterParameter();
            filter.setLimit(0);
            filter.setStart(null);
            filter.setSearchKey(null);

            if (!StringUtils.isEmpty(data.getOldStatusCode())) {
                filter.setGroupPayrunID(edsAdditionalPayment.getObjectID());
                List<EdsPaymentDeduction> paymentItems = paymentDeductionManager.getAdditionalPaymentItemListByFilter(filter);
                if (CollectionUtils.isEmpty(paymentItems)) {
                    return;
                }

                if (!CollectionUtils.isEmpty(paymentItems)) {
                    for (EdsPaymentDeduction edsPaymentDeduction : paymentItems) {
                        String key = edsPaymentDeduction.getEmployee().getObjectID() + "_" + edsPaymentDeduction.getObjectID();
                        if (!CollectionUtils.isEmpty(data.getDeletedItems()) && data.getDeletedItems().get(key) != null) {

                            futureCall.add(() -> {
                                ServerSecurityContext.getInstance().setDatabase(database);
                                ServerSecurityContext.getInstance().setCompanyId(companyId);
                                ServerSecurityContext.getInstance().setStaticUserID(userId);

                                payrollAsyncService.getInNewTransaction(() -> {
                                    deleteLineItems(edsPaymentDeduction.getObjectID());
                                    return null;
                                });

                                BigDecimal total = edsPaymentDeduction.getTotalAmount() != null ? edsPaymentDeduction.getTotalAmount() : BigDecimal.ZERO;
                                total = total.subtract(edsPaymentDeduction.getTotalAmount());
                                edsAdditionalPayment.setTotal(total);
                                additionalPaymentManager.update(edsAdditionalPayment);
//                            paymentDeductionManager.flush();
                                return null;
                            });
                        } else if (!CollectionUtils.isEmpty(data.getChangedItems()) && edsPaymentDeduction.getEmployee() != null && data.getChangedItems().get(key) != null) {
                            PaymentDeductionObject paymentDeductionObject = data.getChangedItems().get(key);
                            paymentDeductionObject.setCashAdvanceID(data.getObjectID());
                            futureCall.add(() -> {
                                ServerSecurityContext.getInstance().setDatabase(database);
                                ServerSecurityContext.getInstance().setCompanyId(companyId);
                                ServerSecurityContext.getInstance().setStaticUserID(userId);

                                Integer objectId = paymentDeductionObject.getPaymentAmount() != null ? payrollAsyncService.createPaymentDeduction(paymentDeductionObject) : null;
                                return objectId;
                            });
                        } else {
                            PaymentDeductionObject paymentDeductionObject = createOrUpdateAdditionaPaymentItem(data, edsPaymentDeduction.getObjectID(), null);
                            paymentDeductionObject.setCashAdvanceID(data.getObjectID());
                            futureCall.add(() -> {
                                ServerSecurityContext.getInstance().setDatabase(database);
                                ServerSecurityContext.getInstance().setCompanyId(companyId);
                                ServerSecurityContext.getInstance().setStaticUserID(userId);

                                Integer objectId = paymentDeductionObject.getPaymentAmount() != null ? payrollAsyncService.createPaymentDeduction(paymentDeductionObject) : null;
                                return objectId;
                            });
                        }
                    }
                }
            } else if (StringUtils.isEmpty(data.getOldStatusCode())) {
                ListingFilterParameter filterParameter = data.getFilterParameter();
                filterParameter.setStart(null);
                filterParameter.setLimit(0);
                filterParameter.setSearchKey(null);
                List<EdsEmployee> employees = new ArrayList<>();

                Calendar calendar = Calendar.getInstance();
                if (filterParameter.getMonthId() == null) {
                    filterParameter.setMonthId(calendar.get(Calendar.MONTH));
                }
                if (filterParameter.getYear() == null) {
                    filterParameter.setYear(calendar.get(Calendar.YEAR));
                }

                if (filterParameter.getEmployeeIDs() != null || filterParameter.getDepartmentId() != null || filterParameter.getLocationId() != null || filterParameter.getSupervisorId() != null || filterParameter.getObjectId() != null) {
                    employees = employeeManager.getEmployeesByFilter(filterParameter);
                }

                if (!CollectionUtils.isEmpty(employees)) {
                    for (EdsEmployee edsEmployee : employees) {
                        if (edsEmployee != null) {
                            if (!CollectionUtils.isEmpty(data.getDeletedItems()) && data.getDeletedItems().get(edsEmployee.getObjectID().toString()) != null) {
                                continue;
                            } else if (!CollectionUtils.isEmpty(data.getChangedItems()) && data.getChangedItems().get(edsEmployee.getObjectID().toString()) != null) {
                                PaymentDeductionObject item = data.getChangedItems().get(edsEmployee.getObjectID().toString());
                                item.setCashAdvanceID(data.getObjectID());
                                futureCall.add(() -> {
                                    ServerSecurityContext.getInstance().setDatabase(database);
                                    ServerSecurityContext.getInstance().setCompanyId(companyId);
                                    ServerSecurityContext.getInstance().setStaticUserID(userId);

                                    Integer objectId = item.getPaymentAmount() != null ? payrollAsyncService.createPaymentDeduction(item) : null;
                                    return objectId;
                                });
                            } else {
                                PaymentDeductionObject item = createOrUpdateAdditionaPaymentItem(data, null, edsEmployee);
                                item.setCashAdvanceID(data.getObjectID());
                                item.setEmployee(edsEmployee.getAsSelectItem());
                                futureCall.add(() -> {
                                    ServerSecurityContext.getInstance().setDatabase(database);
                                    ServerSecurityContext.getInstance().setCompanyId(companyId);
                                    ServerSecurityContext.getInstance().setStaticUserID(userId);

                                    Integer objectId = item.getPaymentAmount() != null ? payrollAsyncService.createPaymentDeduction(item) : null;
                                    return objectId;
                                });
                            }
                        }
                    }
                }
            }

            ArrayList<Integer> paymentDeductionIds = new ArrayList<>();
            try {
                for (Future<Integer> future : executor.invokeAll(futureCall)) {
                    if (future.get() != null) {
                        paymentDeductionIds.add(future.get());
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            payrollAsyncService.getInNewTransaction(() -> {
                batchChangeAdditionalPaymentStatus(data.getObjectID(), statusCode);
                return null;
            });
        });
    }


    @Transactional
    public Integer createPaymentDeduction(PaymentDeductionObject item) {
        EdsAdditionalPayment edsAdditionalPayment = additionalPaymentManager.get(item.getCashAdvanceID());
        EdsPaymentDeduction paymentDeduction = paymentDeductionManager.get(item.getId());
        EdsEmployee edsEmployee = employeeManager.get(item.getEmployee() != null && item.getEmployee().getId() != null ? item.getEmployee().getId() : null);

        if (edsEmployee != null) {
            if (paymentDeduction == null) {
                paymentDeduction = new EdsPaymentDeduction();
            }

            paymentDeduction.setEmployee(employeeManager.get(item.getEmployee().getId()));
            paymentDeduction.setEmployeeId(item.getEmployee().getId());
            if (item.getCategoryItem() != null && item.getCategoryItem().getId() != null) {
                paymentDeduction.setCategory(categoryManager.get(item.getCategoryItem().getId()));
                paymentDeduction.setCategoryId(item.getCategoryItem().getId());
            }
            paymentDeduction.setAdditionalPayment(edsAdditionalPayment);
            paymentDeduction.setPaymentAmount(item.getPaymentAmount());
            if (item.getBasicSalaryPartAmount() != null) {
                paymentDeduction.setBasicSalaryPartAmount(item.getBasicSalaryPartAmount());
            }
            paymentDeduction.setPaymentDate(item.getPaymentDate());
            paymentDeduction.setTax(item.getTax());
            paymentDeduction.setEmployerContribution(item.getEmployerContribution());
            paymentDeduction.setDeduction(item.getDeduction());
            paymentDeduction.setCommission(item.getCommission());
            paymentDeduction.setStartDate(item.getStarttDate() != null ? item.getStarttDate().getNonConvertedDate() : null);
            paymentDeduction.setEndDate(item.getEnddDate() != null ? item.getEnddDate().getNonConvertedDate() : null);
            if (item.getAdditionalPaymentDate() != null) {
                paymentDeduction.setAdditionalPaymentDate(item.getAdditionalPaymentDate().getNonConvertedDate());
            }
            paymentDeduction.setBasicSalaryAmount(item.getEmployeeBasicSalary());
            paymentDeduction.setPercentage(item.getPercentage());
            paymentDeduction.setBasicAndadditionalAmount(item.getBasicPlusAllowance());

            paymentDeduction.setTotalAmount(item.getTotalAmount());
            ArrayList<CompanyCustomFieldItem> apItemCustomFields = item.getItemCustomFields();

            List<CompanyCustomFieldItem> apAllItemCustomFields = commonService.getCompanyAllCustomFields(ViewName.AdditionalPaymentItem);
            if (apAllItemCustomFields != null && !apAllItemCustomFields.isEmpty()) {
                for (CompanyCustomFieldItem companyCustomFieldItem : apAllItemCustomFields) {
                    if (apItemCustomFields != null && !apItemCustomFields.contains(companyCustomFieldItem)) {
                        apItemCustomFields.add(companyCustomFieldItem);
                    }
                }
            }

            if (apItemCustomFields != null && apItemCustomFields.size() > 0) {
                for (CompanyCustomFieldItem additionalPaymentCF : apItemCustomFields) {
                    for (CompanyCustomFieldItem apCF : item.getItemCustomFields()) {
                        if (additionalPaymentCF.getDataType().equals(apCF.getDataType())
                                && additionalPaymentCF.getUiType().equals(apCF.getUiType())
                                && additionalPaymentCF.getAliasName().equals(apCF.getAliasName())
                                && (additionalPaymentCF.getFieldStringValue() == null || (additionalPaymentCF.getFieldStringValue() != null && additionalPaymentCF.getFieldStringValue().length() == 0))) {
                            additionalPaymentCF.setPredefinedValues(apCF.getPredefinedValues());
                            additionalPaymentCF.setPredefinedValuesWithSorting(apCF.getPredefinedValuesWithSorting());
                            additionalPaymentCF.setQuery(apCF.getQuery());
                            additionalPaymentCF.setQueryItems(apCF.getQueryItems());
                            additionalPaymentCF.setFieldStringValue(apCF.getFieldStringValue());
                            additionalPaymentCF.setFieldDateNonConvertedValue(apCF.getFieldDateNonConvertedValue());
                            additionalPaymentCF.setAttachments(apCF.getAttachments());
                            additionalPaymentCF.setLookUpTypeEnum(apCF.getLookUpTypeEnum());
                            additionalPaymentCF.setSelectedId(apCF.getSelectedId());
                            additionalPaymentCF.setDefaultValue(apCF.getDefaultValue());
                            additionalPaymentCF.setPrefix(apCF.getPrefix());
                            additionalPaymentCF.setItem(apCF.getItem());
                            additionalPaymentCF.setSelectItems(apCF.getSelectItems());
                        }
                    }
                }
            }

            paymentDeduction.setCustomFields(saveItemCustomFields(paymentDeduction.getCustomFields(), item.getItemCustomFields()));
            Gson gson = new Gson();
            if (!CollectionUtils.isEmpty(item.getTaxCategories())) {
                List<AdditionalPaymentItemCategory> taxCategories = new ArrayList<>();

                for (PaymentDeductionObject paymentDeductionObject : item.getTaxCategories()) {
                    AdditionalPaymentItemCategory additionalPaymentItemCategory = new AdditionalPaymentItemCategory();
                    additionalPaymentItemCategory.setId(paymentDeductionObject.getId());
                    additionalPaymentItemCategory.setAmount(paymentDeductionObject.getAmount());
                    additionalPaymentItemCategory.setCategoryItem(paymentDeductionObject.getCategoryItem());
                    additionalPaymentItemCategory.setType(paymentDeductionObject.getType());
                    additionalPaymentItemCategory.setPercentage(paymentDeductionObject.getPercentage());
                    taxCategories.add(additionalPaymentItemCategory);
                }
                paymentDeduction.setTaxCategoryList(gson.toJson(taxCategories.toArray(new AdditionalPaymentItemCategory[]{})));
            }
            if (!CollectionUtils.isEmpty(item.getEmployerContributionCategories())) {
                List<AdditionalPaymentItemCategory> employerContributions = new ArrayList<>();

                for (PaymentDeductionObject paymentDeductionObject : item.getEmployerContributionCategories()) {
                    AdditionalPaymentItemCategory additionalPaymentItemCategory = new AdditionalPaymentItemCategory();
                    additionalPaymentItemCategory.setId(paymentDeductionObject.getId());
                    additionalPaymentItemCategory.setAmount(paymentDeductionObject.getAmount());
                    additionalPaymentItemCategory.setCategoryItem(paymentDeductionObject.getCategoryItem());
                    additionalPaymentItemCategory.setType(paymentDeductionObject.getType());
                    additionalPaymentItemCategory.setPercentage(paymentDeductionObject.getPercentage());
                    employerContributions.add(additionalPaymentItemCategory);
                }
                paymentDeduction.setEmployerContributionCategoryList(gson.toJson(employerContributions.toArray(new AdditionalPaymentItemCategory[]{})));
            }
            if (!CollectionUtils.isEmpty(item.getDeductionCategories())) {
                List<AdditionalPaymentItemCategory> customDeductions = new ArrayList<>();

                for (PaymentDeductionObject paymentDeductionObject : item.getDeductionCategories()) {
                    AdditionalPaymentItemCategory additionalPaymentItemCategory = new AdditionalPaymentItemCategory();
                    additionalPaymentItemCategory.setId(paymentDeductionObject.getId());
                    additionalPaymentItemCategory.setAmount(paymentDeductionObject.getAmount());
                    additionalPaymentItemCategory.setCategoryItem(paymentDeductionObject.getCategoryItem());
                    additionalPaymentItemCategory.setType(paymentDeductionObject.getType());
                    additionalPaymentItemCategory.setPercentage(paymentDeductionObject.getPercentage());
                    customDeductions.add(additionalPaymentItemCategory);
                }
                paymentDeduction.setCustomDeductionCategoryList(gson.toJson(customDeductions.toArray(new AdditionalPaymentItemCategory[]{})));
            }

            paymentDeductionManager.createOrUpdate(paymentDeduction);

            return paymentDeduction.getObjectID();
        }
        return null;
    }


    private EdsItemCustomFields saveItemCustomFields(EdsItemCustomFields edsItemCustomFields, List<CompanyCustomFieldItem> customFieldItems) {
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
                edsItemCustomFields = new EdsItemCustomFields();
                itemCFManager.create(edsItemCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsItemCustomFields, customFieldItems);
            return edsItemCustomFields;
        }
        return null;
    }

    @Transactional
    public void additionalPaymentTransaction(EdsAdditionalPayment payment) {
        for (EdsPaymentDeduction deduction : payment.getItems()) {
            if (deduction.getCategory() != null && deduction.getCategory().getDebitToAccountID() != null && deduction.getCategory().getCreditToAccountID() != null) {

                EdsAccount debitAccount = accountingManager.get(deduction.getCategory().getDebitToAccountID());
                EdsAccount creditAccount = accountingManager.get(deduction.getCategory().getCreditToAccountID());


                EdsAdditionalPaymentTransaction transaction = new EdsAdditionalPaymentTransaction();
                EdsUser user = transactionManager.getUser();
                transaction.setJournalId(transactionManager.getCompanyLastTransactionOrderID(user.getCompany()) + 1);
                transaction.setJournalDate(deduction.getAdditionalPaymentDate() != null ? deduction.getAdditionalPaymentDate() : new Date());
                transaction.setName("Additional Payment: " + deduction.getEmployee().getFullName());
                transaction.setPostedBy(user);
                transaction.setPostedDate(user.getCompany().getCompanyDate());

                transaction.setAdditionalPayment(deduction);

                if (debitAccount != null && creditAccount != null) {

                    EdsTransactionItem debitItem = new EdsTransactionItem();
                    debitItem.setAccount(debitAccount);
                    debitItem.setDebit(deduction.getPaymentAmount() != null ? deduction.getPaymentAmount() : BigDecimal.ZERO);
                    transaction.addTransactionItem(debitItem);

                    EdsTransactionItem creditItem = new EdsTransactionItem();
                    creditItem.setAccount(creditAccount);
                    creditItem.setCredit(deduction.getPaymentAmount() != null ? deduction.getPaymentAmount() : BigDecimal.ZERO);
                    transaction.addTransactionItem(creditItem);
                }
                transactionManager.create(transaction);
            }
        }

    }

    @Override
    public Integer deleteAdditionalPayment(Integer objectID) {
        List<EdsPaymentDeduction> payslipItems = paymentDeductionManager.getPayslipAdditionalPayments(objectID);
        if (payslipItems != null && !payslipItems.isEmpty()) {
            return -1;
        }
        EdsAdditionalPayment payment = additionalPaymentManager.get(objectID);
        if (payment != null) {
            payment.setLastUpdateTime(new Date());
            payment.setUpdater(userManager.getUser());
            payment.setDeleted(true);
            baseEventsPostProcessor.registerEvent(AdditionalPaymentEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, payment, userManager.getUser());

            for (EdsPaymentDeduction item : payment.getItems()) {
                transactionManager.deleteAdditionalPaymentTransaction(item.getObjectID());
                item.setDeleted(true);
            }
            if (!CollectionUtils.isEmpty(payment.getPayments())) {
                payment.getPayments().forEach(paymentObject -> deletePayrollPayment(paymentObject.getObjectID()));
            }
            additionalPaymentManager.update(payment);
            removeAdditionalPaymentFromSolr(payment);
        }
        return 0;
    }

    @Override
    public Integer deleteAdditionalPaymentItem(Integer objectID, Integer employeeID) {
        List<EdsPaymentDeduction> payslipItems = paymentDeductionManager.getPayslipAdditionalPayments(objectID);
        if (payslipItems != null && !payslipItems.isEmpty()) {
            return -1;
        }
        EdsAdditionalPayment payment = additionalPaymentManager.get(objectID);
        payment.setLastUpdateTime(new Date());
        payment.setUpdater(userManager.getUser());
        boolean empty = true;
        BigDecimal itemTotal = BigDecimal.ZERO;
        for (EdsPaymentDeduction item : payment.getItems()) {
            if (item.getEmployee().getObjectID().equals(employeeID)) {
                item.setDeleted(true);
                itemTotal = item.getPaymentAmount();

                if (!CollectionUtils.isEmpty(item.getPaymentItems())) {
                    item.getPaymentItems().forEach(paymentItem -> deletePayrollPaymentItem(paymentItem.getObjectID()));
                }
            } else {
                empty = false;
            }
        }
        if (empty) {
            payment.setDeleted(true);
            removeAdditionalPaymentFromSolr(payment);
        } else {
            payment.setTotal(payment.getTotal().subtract(itemTotal));
            addAdditionalPaymentToSolr(payment);
        }
        additionalPaymentManager.update(payment);
        return 0;
    }

    @Override
    public Boolean isExistSuchAdditionalPaymentByCategory(ListingFilterParameter fp) {
        return additionalPaymentManager.isExistAdditionalPaymentByCategory(fp).size() > 0;
    }

    @Override
    public ListResult<RecurringPayDeductItem> getRecurringPaymentDeductionList(ListingFilterParameter fp) {
        List<EdsRecurringPayDeduction> list = recurringPayDeductionManager.getAllItems(fp);
        Integer total = recurringPayDeductionManager.getTotalCount();
        ArrayList<RecurringPayDeductItem> result = new ArrayList<>();
        if (list.size() > 0) {
            for (EdsRecurringPayDeduction item : list) {
                result.add(item.toRpc(true));
            }
        }
        return new ListResult<>(result, total);
    }

    public RecurringPayDeductItem getRecurringPayDeduction(Integer objectId) {
        RecurringPayDeductItem result = new RecurringPayDeductItem();
        EdsRecurringPayDeduction edsObject = recurringPayDeductionManager.get(objectId);
        if (edsObject != null) {
            result = edsObject.toRpc(false);
        }
        result.setCurrentUserId(userManager.getUser().getObjectID());
        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public TestRPC saveRecurringPaymentDeduction(RecurringPayDeductItem pdItem) {
        TestRPC result = new TestRPC();

        EdsRecurringPayDeduction recurringPayDeduction = null;
        String recurringPayDeductOldStatus = null;
        if (pdItem.getObjectID() != null) {
            recurringPayDeduction = recurringPayDeductionManager.get(pdItem.getObjectID());
            recurringPayDeductOldStatus = recurringPayDeduction.getStatus() != null ? recurringPayDeduction.getStatus().getCode() : null;

        } else {
            recurringPayDeduction = new EdsRecurringPayDeduction();
            recurringPayDeduction.setCreationDate(new Date());
        }
        recurringPayDeduction.setLastUpdateTime(new Date());
        recurringPayDeduction.setPayType(pdItem.getPayType());

        if (pdItem.getEmployee() != null) {
            recurringPayDeduction.setEmployee(employeeManager.get(pdItem.getEmployee().getId()));
        }
        if (pdItem.getCategoryItem() != null) {
            recurringPayDeduction.setCategory(categoryManager.get(pdItem.getCategoryItem().getId()));
        }
        if (pdItem.getFromDate() != null) {
            recurringPayDeduction.setFromDate(pdItem.getFromDate().getNonConvertedDate());
        }
        if (pdItem.getToDate() != null) {
            recurringPayDeduction.setToDate(pdItem.getToDate().getNonConvertedDate());
        }
        if (pdItem.getType() != null) {
            recurringPayDeduction.setType(pdItem.getType());
        }
        if (pdItem.getApprovedDate() != null) {
            recurringPayDeduction.setApprovedDate(pdItem.getApprovedDate().getNonConvertedDate());
        }
        if (pdItem.getCurrency() != null) {
            recurringPayDeduction.setCurrency(currencyManager.get(pdItem.getCurrency().getId()));
        }
        if (pdItem.getPaymentAmount() != null) {
            recurringPayDeduction.setPaymentAmount(pdItem.getPaymentAmount());
        }
        if (pdItem.getPercentage() != null) {
            recurringPayDeduction.setPercentage(pdItem.getPercentage());
        }
        if (pdItem.getTotalLimit() != null) {
            recurringPayDeduction.setTotalLimit(pdItem.getTotalLimit());
        }

        recurringPayDeduction.getLinkedCategories().clear();
        if (pdItem.isFromAllAllowances()) {
            recurringPayDeduction.setFromAllAllowances(pdItem.isFromAllAllowances());
        } else if (pdItem.getLinkedCategories() != null && pdItem.getLinkedCategories().size() > 0) {
            EdsPayrollCategory category;
            for (final PaymentDeductionSelectItem linkedCategory : pdItem.getLinkedCategories()) {
                category = this.categoryManager.get(linkedCategory.getId());
                if (category != null) {
                    recurringPayDeduction.getLinkedCategories().add(category);
                }
            }
        }

        recurringPayDeductionManager.createOrUpdate(recurringPayDeduction);


        if (!isOk(pdItem.getApprovers())) {
            if (pdItem.getStatus() != null) {
                recurringPayDeduction.setEntityStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, pdItem.getStatus().getCode()));
            }
        }
        if (isOk(pdItem.getApprovers())) {
            EdsReference status = (pdItem.getStatus() != null && pdItem.getStatus().getCode() != null) ? referenceManager.getByCode(pdItem.getStatus().getCode()) : null;
            pdItem.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : pdItem.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (recurringPayDeduction.getCurrentApprover() != null && status != null && isFirstApprover) {
                        recurringPayDeduction.getCurrentApprover().setStatus(status);
                        recurringPayDeduction.setEntityStatus(referenceManager.getByCode(Constants.SUBMITTED_TO_MANAGER));
                        isFirstApprover = false;
                    } else if (recurringPayDeduction.getCurrentApprover() != null && status != null) {
                        recurringPayDeduction.getCurrentApprover().setStatus(referenceManager.getByCode(Constants.SUBMITTED_TO_MANAGER));
                    }
                    if (status != null && !APPROVED.equals(status.getCode())) {
                        recurringPayDeduction.setEntityStatus(status);
                    }
                    if (recurringPayDeduction.isCurrentApproverRejected()) {
                        recurringPayDeduction.setEntityStatus(recurringPayDeduction.getCurrentApprover().getStatus());
                    }
                    continue;
                }
                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(recurringPayDeduction.getObjectID());
                edsApprover.setIs_default(false);

                if (status != null && isFirstApprover) {
                    edsApprover.setStatus(status);
                    if (Constants.DRAFT.equals(status.getCode())) {
                        recurringPayDeduction.setEntityStatus(referenceManager.getByCode(Constants.DRAFT));
                    } else {
                        recurringPayDeduction.setEntityStatus(referenceManager.getByCode(Constants.SUBMITTED_TO_MANAGER));
                    }
                    isFirstApprover = false;
                } else if (status != null) {
                    edsApprover.setStatus(referenceManager.getByCode(Constants.SUBMITTED_TO_MANAGER));
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

                if (recurringPayDeduction.getCurrentApprover() == null) {
                    recurringPayDeduction.setCurrentApprover(edsApprover);
                }
                recurringPayDeduction.getApprovers().add(edsApprover);
            }
        }

        recurringPayDeductionManager.update(recurringPayDeduction);
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), recurringPayDeduction, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_RECURRING_PAY_DEDUCTION);

        if (pdItem.getStatus() != null && POSTED.equals(pdItem.getStatus().getCode())) {
            approvedActionForRecurringPayDeduction(recurringPayDeduction.getObjectID());
        }

        result.setId(recurringPayDeduction.getObjectID());
        return result;
    }

    @Override
    public Boolean deleteRecurringPaymentDeduction(Integer objectID) {
        EdsRecurringPayDeduction object = recurringPayDeductionManager.get(objectID);
        EdsPaymentDeduction paymentDeduction = paymentDeductionManager.getByRecurringPayDeductionID(object.getObjectID());

        if (paymentDeduction != null) {
            Boolean isUsed = payslipPaymentsManager.checkPaymentDeductionForUsed(paymentDeduction.getObjectID());
            if (isUsed) {
                return false;
            }

            paymentDeduction.setDeleted(true);
            paymentDeductionManager.update(paymentDeduction);
        }
        object.setDeleted(true);
        object.setLastUpdateTime(new Date());
        recurringPayDeductionManager.update(object);
        return true;
    }

    @Override
    public SalaryDetailedReportData getSalaryDetailedReportData(ListingFilterParameter lfp) {
        return payslipTableItemManager.getSalaryDetailedReportItems(lfp);
    }

    @Override
    public AdditionalPayment getAdditionalPaymentItemsData(ListingFilterParameter fp) {
        AdditionalPayment result = new AdditionalPayment();
        if (fp.getObjectId() != null) {
            EdsAdditionalPayment additionalPayment = additionalPaymentManager.get(fp.getObjectId());
            if (additionalPayment != null && !additionalPayment.getDeleted()) {
                Gson gson = new Gson();
                List<PaymentDeductionObject> items = new ArrayList<>();
                fp.setGroupPayrunID(fp.getObjectId());
                Integer itemCount = paymentDeductionManager.getAdditionalPaymentCountByFilter(fp);
                result.setTotalItems(itemCount);
                HashMap<Integer, BigDecimal> salaryMap = new HashMap<>();

                List<EdsPaymentDeduction> paymentItems = paymentDeductionManager.getAdditionalPaymentItemListByFilter(fp);
                if (!CollectionUtils.isEmpty(paymentItems)) {
                    Calendar calendar = Calendar.getInstance();
                    if (fp.getMonthId() == null) {
                        fp.setMonthId(additionalPayment.getMonthID());
                    }
                    if (fp.getYear() == null) {
                        fp.setYear(additionalPayment.getYear());
                    }
                    List<Integer> employeeIds = paymentItems.stream().map(EdsPaymentDeduction::getEmployeeId).collect(Collectors.toList());

                    if (!fp.isSummaryView()) {
                        if (fp.isCalculateByLastMonth()) {
                            //get last month
                            calendar.set(Calendar.MONTH, fp.getMonthId());
                            calendar.set(Calendar.YEAR, fp.getYear());
                            calendar.add(Calendar.MONTH, -1);

                            ListingFilterParameter filterParameter = new ListingFilterParameter();
                            fp.setEmployeeIDs(fp.getEmployeeIDs());
                            fp.setMonthId(calendar.get(Calendar.MONTH));
                            fp.setYear(calendar.get(Calendar.YEAR));

                            ArrayList<Integer> categoryIds = new ArrayList<>();
                            if (filterParameter.isBasicPlusAllowancePaymentType() && !CollectionUtils.isEmpty(filterParameter.getPaymentCategories())) {
                                categoryIds = filterParameter.getPaymentCategories().stream().map(SelectItem::getId).collect(Collectors.toCollection(ArrayList::new));
                            }
                            fp.setObjectIDs(categoryIds);
                            salaryMap = payslipPaymentsManager.getEmployeeSalaryForPeriod(fp);
                        } else {
                            calendar.set(Calendar.MONTH, fp.getMonthId());
                            calendar.set(Calendar.YEAR, fp.getYear());
                            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                            salaryMap = salaryHistoryManager.getEmployeeSalaryMap(employeeIds, calendar.getTime());
                        }
                    }

                    calendar.set(Calendar.MONTH, fp.getMonthId());
                    calendar.set(Calendar.YEAR, fp.getYear());
                    calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
                    Date date = calendar.getTime();

                    String[] settingsKeys = {
                            PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS,
                            PayrollConstants.MATERIAL_AID_TYPE_FUNERAL,
                            PayrollConstants.MATERIAL_AID_TYPE_GIFT
                    };
                    Table<Integer, String, String> employeeSettingsMap = employeePayrollSettingsManager.getEmployeesPayrollSettingMap(employeeIds, settingsKeys);
                    if (additionalPayment.getLeaveRequestId() != null) {
                        result.setLeaveRequestId(additionalPayment.getLeaveRequestId());
                    }

                    for (EdsPaymentDeduction item : paymentItems) {
                        PaymentDeductionObject paymentDeductionObject = item.getRPC();
                        paymentDeductionObject.setItemCustomFields((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(item.getCustomFields(), commonService.getCompanyCustomFields(ViewName.AdditionalPaymentItem)));
                        paymentDeductionObject.setCountIncident(0);
                        if (paymentDeductionObject.getEmployee() != null) {
                            EdsEmployee edsEmployee = employeeManager.get(paymentDeductionObject.getEmployee().getId());
                            ListingFilterParameter filterParams = new ListingFilterParameter();
                            filterParams.setMonthId(fp.getMonthId());
                            filterParams.setYear(fp.getYear());
                            filterParams.setEmployeeId(paymentDeductionObject.getEmployee().getId());
                            paymentDeductionObject.setCountIncident(getIncidentCountByDynamicId(filterParams, paymentDeductionObject.getEmployee().getId()));

                            if (!fp.isSummaryView()) {
                                BigDecimal salary = salaryMap.get(paymentDeductionObject.getEmployee().getId());
                                paymentDeductionObject.setEmployeeBasicSalary(salary == null ? BigDecimal.ZERO : salary);

                                if (fp.isCalculateByLastMonth()) {
                                    paymentDeductionObject.setBasicPlusAllowance(paymentDeductionObject.getEmployeeBasicSalary());
                                } else if (fp.isBasicPlusAllowancePaymentType()) {
                                    calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
                                    BigDecimal totalAmount = getTotalAllowancesByEmployee(edsEmployee, fp.getPaymentCategories(), calendar.getTime(), paymentDeductionObject.getEmployeeBasicSalary());
                                    paymentDeductionObject.setBasicPlusAllowance(totalAmount);
                                }

                                BigDecimal empMode = BigDecimal.ONE;
                                if (edsEmployee != null && edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmploymentMode() != null) {

                                    if ("FULL_TIME".equals(edsEmployee.getProfile().getEmploymentMode().getCode())) {
                                        empMode = BigDecimal.ONE;
                                    } else if ("075_TIME".equals(edsEmployee.getProfile().getEmploymentMode().getCode())) {
                                        empMode = BigDecimal.valueOf(0.75);
                                    } else if ("PART_TIME".equals(edsEmployee.getProfile().getEmploymentMode().getCode())) {
                                        empMode = BigDecimal.valueOf(0.50);
                                    } else if ("QUARTER_TIME".equals(edsEmployee.getProfile().getEmploymentMode().getCode())) {
                                        empMode = BigDecimal.valueOf(0.25);
                                    }
                                }
                                paymentDeductionObject.setEmpMode(empMode);
                            }
                        }

                        if (!StringUtils.isEmpty(item.getTaxCategoryList())) {
                            List<PaymentDeductionObject> taxCategories = new ArrayList<>();
                            AdditionalPaymentItemCategory[] additionalPaymentItemCategories = gson.fromJson(item.getTaxCategoryList(), AdditionalPaymentItemCategory[].class);
                            if (additionalPaymentItemCategories != null) {
                                for (AdditionalPaymentItemCategory additionalPaymentItemCategory : additionalPaymentItemCategories) {
                                    PaymentDeductionObject object = new PaymentDeductionObject();
                                    object.setId(additionalPaymentItemCategory.getId());
                                    object.setCategoryItem(additionalPaymentItemCategory.getCategoryItem());
                                    object.setPercentage(additionalPaymentItemCategory.getPercentage());
                                    object.setAmount(additionalPaymentItemCategory.getAmount());
                                    object.setType(additionalPaymentItemCategory.getType());
                                    taxCategories.add(object);
                                }
                            }
                            paymentDeductionObject.setTaxCategories(taxCategories);
                        }
                        if (!StringUtils.isEmpty(item.getEmployerContributionCategoryList())) {
                            List<PaymentDeductionObject> employerContributions = new ArrayList<>();
                            AdditionalPaymentItemCategory[] additionalPaymentItemCategories = gson.fromJson(item.getEmployerContributionCategoryList(), AdditionalPaymentItemCategory[].class);
                            if (additionalPaymentItemCategories != null) {
                                for (AdditionalPaymentItemCategory additionalPaymentItemCategory : additionalPaymentItemCategories) {
                                    PaymentDeductionObject object = new PaymentDeductionObject();
                                    object.setId(additionalPaymentItemCategory.getId());
                                    object.setCategoryItem(additionalPaymentItemCategory.getCategoryItem());
                                    object.setPercentage(additionalPaymentItemCategory.getPercentage());
                                    object.setAmount(additionalPaymentItemCategory.getAmount());
                                    object.setType(additionalPaymentItemCategory.getType());
                                    employerContributions.add(object);
                                }
                            }
                            paymentDeductionObject.setEmployerContributionCategories(employerContributions);
                        }
                        if (!StringUtils.isEmpty(item.getCustomDeductionCategoryList())) {
                            List<PaymentDeductionObject> customDeductions = new ArrayList<>();
                            AdditionalPaymentItemCategory[] additionalPaymentItemCategories = gson.fromJson(item.getCustomDeductionCategoryList(), AdditionalPaymentItemCategory[].class);
                            if (additionalPaymentItemCategories != null) {
                                for (AdditionalPaymentItemCategory additionalPaymentItemCategory : additionalPaymentItemCategories) {
                                    PaymentDeductionObject object = new PaymentDeductionObject();
                                    object.setId(additionalPaymentItemCategory.getId());
                                    object.setCategoryItem(additionalPaymentItemCategory.getCategoryItem());
                                    object.setPercentage(additionalPaymentItemCategory.getPercentage());
                                    object.setAmount(additionalPaymentItemCategory.getAmount());
                                    object.setType(additionalPaymentItemCategory.getType());
                                    customDeductions.add(object);
                                }
                            }
                            paymentDeductionObject.setDeductionCategories(customDeductions);
                        }
                        if (paymentDeductionObject.getEmployee() != null && paymentDeductionObject.getEmployee().getId() != null) {
                            collectEmployeeCategories(employeeManager.get(paymentDeductionObject.getEmployee().getId()), paymentDeductionObject);
                        }

//                        paymentDeductionObject.getLgotaBalanceMap().put(PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS, calculateMaterialAidBalance(item.getEmployee(), employeeSettingsMap.row(item.getEmployeeId()), PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS, null, date));
//                        paymentDeductionObject.getLgotaBalanceMap().put(PayrollConstants.MATERIAL_AID_TYPE_FUNERAL, calculateMaterialAidBalance(item.getEmployee(), employeeSettingsMap.row(item.getEmployeeId()), PayrollConstants.MATERIAL_AID_TYPE_FUNERAL, null, date));
//                        paymentDeductionObject.getLgotaBalanceMap().put(PayrollConstants.MATERIAL_AID_TYPE_GIFT, calculateMaterialAidBalance(item.getEmployee(), employeeSettingsMap.row(item.getEmployeeId()), PayrollConstants.MATERIAL_AID_TYPE_GIFT, null, date));
//TODO will discuss this later

                        items.add(paymentDeductionObject);
                    }
                }
                result.setItems(items);
            }
        }
        return result;
    }

    private void collectEmployeeCategories(EdsEmployee edsEmployee, PaymentDeductionObject item) {
        List<PaymentDeductionObject> taxCategories = new ArrayList<>();
        List<PaymentDeductionObject> employerContributions = new ArrayList<>();
        List<PaymentDeductionObject> deductionCategories = new ArrayList<>();
        List<EdsPaymentDeduction> paymentDeductions = edsEmployee.getCategories();
        if (paymentDeductions != null && paymentDeductions.size() > 0) {
            PaymentDeductionObject object;
            for (EdsPaymentDeduction paymentDeduction : paymentDeductions) {
                object = paymentDeduction.getRPC();
                if (object.isTaxCategory() || object.isEmployerContributionCategory() || object.isDeductionCategory()) {
                    List<EdsPayrollCategory> linkedCategoryList = categoryManager.getCategoryLinkedCategories(paymentDeduction.getObjectID());
                    if (!CollectionUtils.isEmpty(linkedCategoryList)) {
                        ArrayList<PaymentDeductionObject> categoryList = new ArrayList<>();
                        for (EdsPayrollCategory edsPayrollCategory : linkedCategoryList) {
                            PaymentDeductionObject linkedObject = new PaymentDeductionObject();
                            linkedObject.setCategoryItem(new PaymentDeductionSelectItem(edsPayrollCategory.getObjectID(), edsPayrollCategory.getName(), edsPayrollCategory.getCode(), edsPayrollCategory.getType()));
                            categoryList.add(linkedObject);
                        }
                        object.setLinkedCategories(categoryList);
                    }
                    if (object.isTaxCategory()) {
                        taxCategories.add(object);
                    } else if (object.isEmployerContributionCategory()) {
                        employerContributions.add(object);
                    } else if (object.isDeductionCategory()) {
                        deductionCategories.add(object);
                    }
                }
            }
        }
        item.setAllTaxCategories(taxCategories);
        item.setAllEmployerContributionCategories(employerContributions);
        item.setAllDeductionCategories(deductionCategories);
    }

    @Override
    public ListResult<OvertimeObject> getOvertimeObjectList(ListingFilterParameter filterParametrs) {
        List<EdsOvertimeObject> list = overtimeManager.getAllItems(filterParametrs);
        Integer totalItem = overtimeManager.getTotalUndeletedItemCount();
        ArrayList<OvertimeObject> result = new ArrayList<>();
        if (list.size() > 0) {
            for (EdsOvertimeObject item : list) {
                result.add(item.toRpc(false));
            }
        }
        return new ListResult<>(result, totalItem);
    }

    @Override
    public AdditionalPayment createAdditionalPaymentFromOvertime(Integer overtimeItemId) {
        EdsOvertimeObject overtimeObject = overtimeManager.get(overtimeItemId);
        if (overtimeObject == null) {
            return null;
        }
        AdditionalPayment payment = new AdditionalPayment();
        Date selectedDate = overtimeObject.getDate();
        EdsPayrollCategory payrollCategory = overtimeObject.getCategory();

        payment.setPaymentType("FIXED_AMOUNT");
        if (selectedDate != null) {
            DateNonConvertable nonConvertableDate = new DateNonConvertable(selectedDate);
            LocalDate currentDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            payment.setYear(currentDate.getYear());
            payment.setMonth(currentDate.getMonth().getDisplayName(TextStyle.FULL, ServerUtils.getUserLocale()));
            payment.setMonthID(currentDate.getMonth().getValue() - 1);
            payment.setStatus("PAYMENT_DRAFT");
            payment.setStatusCode(PAYMENT_STATUS_DRAFT);
            payment.setDefaultDate(nonConvertableDate);
            if (overtimeObject.getCreator() != null) {
                StringBuilder paymentReference = new StringBuilder();
                paymentReference.append(payrollCategory != null ? payrollCategory.getName() : " ").append(" ");
                if (overtimeObject.getSelectedEmployee() != null) {
                    paymentReference.append(overtimeObject.getSelectedEmployee().getFullName()).append(" ");
                } else if (overtimeObject.getPayrollBatch() != null) {
                    paymentReference.append(overtimeObject.getPayrollBatch().getName()).append(" ");
                } else if (overtimeObject.getSelectedDepartment() != null) {
                    paymentReference.append(overtimeObject.getSelectedDepartment().getName()).append(" ");
                }
                paymentReference.append(ServerUtils.getDateShortFormat(nonConvertableDate.getDate()));
                payment.setReference(paymentReference.toString());
            }
        }
        payment.setBasicPlusAllowance(false);
        payment.setApprovedDate(new DateNonConvertable());
        payment.setStatus("PAYMENT_DRAFT");
        payment.setStatusCode(PAYMENT_STATUS_DRAFT);
        payment.setOverallStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, PAYMENT_STATUS_DRAFT).getRPC());

        if (EdsOvertimeObject.OVERTIME_EMPLOYEE_TYPE.equals(overtimeObject.getOvertimeType())) {
            payment.setEmployee(overtimeObject.getSelectedEmployee() != null ? overtimeObject.getSelectedEmployee().getAsSelectItem() : null);
        } else if (EdsOvertimeObject.OVERTIME_DEPARTMENT_TYPE.equals(overtimeObject.getOvertimeType())) {
            payment.setDepartment(overtimeObject.getSelectedDepartment() != null ? overtimeObject.getSelectedDepartment().getAsSelectItem() : null);
        } else if (EdsOvertimeObject.OVERTIME_GROUP_EMPLOYEE_TYPE.equals(overtimeObject.getOvertimeType())) {
            payment.setPayrollBatch(overtimeObject.getPayrollBatch() != null ? overtimeObject.getPayrollBatch().getAsSelectItem() : null);
        }
        if (overtimeObject.getCategory() != null) {
            EdsPayrollCategory category = categoryManager.get(overtimeObject.getCategory().getObjectID());
            if (category != null) {
                PaymentDeductionSelectItem selectItem = category.createPaymentDeductionSelectItem();
                payment.setDefaultCategory(selectItem);
                payment.setDefaultPayrollCategoryId(selectItem.getId());
            }
        }
        payment.setShowInPayslip(true);
        String ids = overtimeObject.getLineItems().stream().map(t -> String.valueOf(t.getEmployee().getObjectID())).collect(Collectors.joining(","));
        HashMap<Integer, BigDecimal> salaryMap = employeePayrollSettingsManager.getEmployeeSalaryMap(ids);

        List<PaymentDeductionObject> items = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (EdsOvertimeObjectData objectData : overtimeObject.getLineItems()) {
            PaymentDeductionObject item = new PaymentDeductionObject();
            EdsEmployee emp = objectData.getEmployee();
            item.setEmployee(emp.getAsSelectItem());
            item.setPaymentDate(objectData.getDate());
            item.setAdditionalPaymentDate(new DateNonConvertable(objectData.getDate()));
            item.setCategoryItem(objectData.getCategory() != null ? objectData.getCategory().createPaymentDeductionSelectItem() : null);
            BigDecimal result = calculateAmoutByEmployeeOvertime(objectData, salaryMap.get(emp.getObjectID()));
            item.setPaymentAmount(result);
            totalAmount = totalAmount.add(result);
            items.add(item);
        }
        payment.setItems(items);
        payment.setTotal(totalAmount);
        payment.setFromId(overtimeObject.getObjectID());
        payment.setFromType(OVERTIME);
        createPaymentFromOvertimeItem(payment);
        return null;
    }

    @Override
    public void deleteOvertimeItemById(Integer objectId) throws ObjectNotFoundException {
        EdsOvertimeObject overtimeObject = overtimeManager.get(objectId);
        if (overtimeObject == null) {
            throw new ObjectNotFoundException();
        }
        overtimeObject.setDeleted(true);
        overtimeManager.update(overtimeObject);
    }

    private BigDecimal calculateAmoutByEmployeeOvertime(EdsOvertimeObjectData objectData, BigDecimal basicSalary) {
        if (objectData == null || objectData.getEmployee() == null) return BigDecimal.ONE;
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(EdsCalendarUtils.getStartOfMonth(objectData.getDate()).getTime());
        fp.setEndDate(EdsCalendarUtils.getEndOfMonth(objectData.getDate()).getTime());
        fp.setEmployeeId(objectData.getEmployee().getObjectID());

        int availableDays = attendanceRawDataManager.getWorkingDays(fp).size();
        if (availableDays == 0) return BigDecimal.ZERO;
        int dailyWorkingHours = 8;
        BigDecimal grandTotalPayment = basicSalary;
        if (grandTotalPayment == null) {
            grandTotalPayment = BigDecimal.ZERO;
        }
        List<EdsPaymentDeduction> paymentCategories = paymentDeductionManager.getPaymentsByEffectiveDate(objectData.getEmployee().getObjectID(), objectData.getDate());
        for (EdsPaymentDeduction deduction : paymentCategories) {
            if ((deduction.getPayType() == null || deduction.getPayType() == 0) && deduction.getPaymentAmount() != null) {
                grandTotalPayment = grandTotalPayment.add(deduction.getPaymentAmount());
            } else {
                grandTotalPayment = grandTotalPayment.add((basicSalary.multiply(deduction.getPercentage()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
            }
        }
        BigDecimal devisor = new BigDecimal(availableDays * dailyWorkingHours).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = grandTotalPayment.divide(devisor, 2, RoundingMode.HALF_UP);
        BigDecimal overtimeHours = objectData.getOvertimeHours() != null ? objectData.getOvertimeHours() : BigDecimal.ZERO;
        return total.multiply(overtimeHours.multiply(BigDecimal.valueOf(2)));
    }

    private void createPaymentFromOvertimeItem(AdditionalPayment payment) {
        Integer additionalPaymentId = createAdditionalPayment(payment, false);
        payment.setObjectID(additionalPaymentId);
        for (PaymentDeductionObject item : payment.getItems()) {
            item.setCashAdvanceID(payment.getObjectID());
            payrollAsyncService.createPaymentDeduction(item);
        }
    }

    @Override
    public List<SelectItem> getOvertimeEmployees(ListingFilterParameter filterParameter) {
        List<SelectItem> employees = new ArrayList<>();
        Integer departmentId = filterParameter.getDepartmentId();
        if (departmentId != null) {
            if (filterParameter.isApplyForSubDepartment()) {
                List<Integer> childList = departmentTreeManager.getAllChildList(departmentId);
                if (childList != null && childList.size() > 0) {
                    childList.forEach(ch -> {
                        List<EdsEmployee> teamEmployees = employeeDepartmentManager.getTeamEmployees2(ch);
                        if (teamEmployees != null && teamEmployees.size() > 0) {
                            teamEmployees.forEach(e -> employees.add(e.getAsSelectItem()));
                        }
                    });
                }
            } else {
                List<EdsEmployeeDepartment> edsEmployeeDepartments = employeeDepartmentManager.getTeamEmployees(filterParameter.getDepartmentId());
                for (EdsEmployeeDepartment emp : edsEmployeeDepartments) {
                    employees.add(emp.getEmployee().getAsSelectItem());
                }
            }
        } else if (filterParameter.getObjectId() != null) {
            List<EdsEmployee> employeeList = employeeManager.getEmployeeByPayrollBatch(filterParameter.getObjectId());
            for (EdsEmployee emp : employeeList) {
                employees.add(emp.getAsSelectItem());
            }
        }
        return employees;
    }

    @Override
    public NumberData generateOvertimeCode() {
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = overtimeManager.getOvertimeLastIntNumber();
        if (intNumber == null) {
            intNumber = 0;
        }
        if (settings != null && settings.getOvertimeNumberingFormat() != null) {
            NumberData numberData = settings.parseNumberDataForALL(intNumber, settings.getOvertimeNumberingFormat(), settings.getDelimetrOvertimeNumbering(), null, null, null, "");
            numberData.setDelimiter(settings.getDelimetrOvertimeNumbering());
            return numberData;
        } else {
            return EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_OVRTIME_PREFIX /*true*/);
        }
    }

    @Override
    public Integer saveOvertimeItem(OvertimeObject overtimeObject) {
        EdsOvertimeObject object = null;
        if (overtimeObject == null) {
            return -1;
        }
        if (overtimeObject.getId() != null) {
            object = overtimeManager.get(overtimeObject.getId());
        }
        if (object == null) {
            object = new EdsOvertimeObject();
        }
        object.setDate(overtimeObject.getDate().getDate());
        object.setObjectID(overtimeObject.getId());
        if (overtimeObject.getCategory() != null) {
            object.setCategory(categoryManager.get(overtimeObject.getCategory().getId()));
        }
        if (overtimeObject.getSelectedDepartment() != null) {
            object.setSelectedDepartment(departmentManager.get(overtimeObject.getSelectedDepartment().getId()));
        } else if (overtimeObject.getSelectedEmployee() != null) {
            object.setSelectedEmployee(employeeManager.get(overtimeObject.getSelectedEmployee().getId()));
        } else if (overtimeObject.getPayrollBatch() != null) {
            EdsPayrollBatch payrollBatch = payrollBatchManager.get(overtimeObject.getPayrollBatch().getId());
            object.setPayrollBatch(payrollBatch);
        }
        EdsUser user = userManager.getUser();
        if (overtimeObject.getId() == null) {
            object.setCreator(user);
            object.setCreatedDate(new Date());
            object.setUpdater(user);
            object.setUpdatedDate(new Date());
        } else {
            object.setUpdater(user);
            object.setUpdatedDate(new Date());
        }
        object.setCustomFields(createOvertimeCustomFields(overtimeObject.getCustomFieldItems()));
        object.setOvertimeType(overtimeObject.getOvertimeType());
        object.setDefaultHours(overtimeObject.getDefaultHours());
        if (overtimeObject.getNumberData() != null) {
            object.setIntNumber(overtimeObject.getNumberData().getIntNumber());
            object.setOvertimeCode(overtimeObject.getNumberData().getNumberString());
        }
        overtimeManager.createOrUpdate(object);
        object.setLineItems(new HashSet<>());
        for (OvertimeObjectData dataItem : overtimeObject.getItems()) {
            EdsOvertimeObjectData objectData = objectDataManager.get(dataItem.getId());
            if (objectData == null) {
                objectData = new EdsOvertimeObjectData();
            }
            objectData.setDate(dataItem.getDate().getDate());
            objectData.setOvertimeHours(dataItem.getOvertimeHours());
            objectData.setObjectID(dataItem.getId());
            objectData.setCategory(categoryManager.get(dataItem.getCategory().getId()));

            objectData.setEmployee(employeeManager.get(dataItem.getEmployee().getId()));
            objectData.setOvertimeObject(object);

            objectDataManager.createOrUpdate(objectData);
            object.getLineItems().add(objectData);
        }

        boolean statusChanged = object.getOverallStatus() != null && !overtimeObject.getStatusCode().equals(object.getOverallStatus().getCode());

        if (!isOk(overtimeObject.getApprovers())) {
            object.setEntityStatus(referenceManager.findReference(Constants.OVERTIME_STATUS, overtimeObject.getStatusCode()));
        }

        if (isOk(overtimeObject.getApprovers())) {
            overtimeObject.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            boolean isFirstApprover = true;
            for (ApproverItemMini approverItem : overtimeObject.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (object.getCurrentApprover() != null && overtimeObject.getStatusCode() != null && isFirstApprover) {
                        object.getCurrentApprover().setStatus(referenceManager.findReference(Constants.OVERTIME_STATUS, overtimeObject.getStatusCode()));
                        object.setEntityStatus(referenceManager.findReference(Constants.OVERTIME_STATUS, Constants.OVERTIME_SUBMITTED));
                        isFirstApprover = false;
                    } else if (object.getCurrentApprover() != null && overtimeObject.getStatusCode() != null) {
                        object.getCurrentApprover().setStatus(referenceManager.findReference(Constants.OVERTIME_STATUS, Constants.OVERTIME_SUBMITTED));
                    }
                    if (overtimeObject.getStatusCode() != null && !OVERTIME_APPROVED.equals(overtimeObject.getStatusCode())) {
                        object.setEntityStatus(referenceManager.findReference(Constants.OVERTIME_STATUS, overtimeObject.getStatusCode()));
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

                if (overtimeObject.getStatusCode() != null && isFirstApprover) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.OVERTIME_STATUS, overtimeObject.getStatusCode()));
                    if (Constants.OVERTIME_DRAFT.equals(overtimeObject.getStatusCode())) {
                        object.setEntityStatus(referenceManager.findReference(Constants.OVERTIME_STATUS, overtimeObject.getStatusCode()));
                    } else {
                        object.setEntityStatus(referenceManager.findReference(Constants.OVERTIME_STATUS, OVERTIME_SUBMITTED));
                    }
                    isFirstApprover = false;
                } else if (overtimeObject.getStatusCode() != null) {
                    edsApprover.setStatus(referenceManager.findReference(Constants.OVERTIME_STATUS, OVERTIME_SUBMITTED));
                }
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                    edsApprover.setExactEmployee(user_);
                }
                edsApprover.setApproverRoles(new HashSet<>());
                edsApprover.setApproverEmployees(new HashSet<>());
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

        if (overtimeObject.getId() == null) {
            baseEventsPostProcessor.registerEvent(OvertimeEventListinerImpl.TYPE, MyUpdateItem.ADD, object, userManager.getUser());
            if (overtimeObject.getStatusCode().equals(Constants.OVERTIME_DRAFT)) {
                baseEventsPostProcessor.registerEvent(OvertimeEventListinerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, object, userManager.getUser());
            }
            if (overtimeObject.getStatusCode().equals(Constants.OVERTIME_APPROVED)) {
                baseEventsPostProcessor.registerEvent(OvertimeEventListinerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, object, userManager.getUser());
            }
        } else if (!Constants.OVERTIME_APPROVED.equals(overtimeObject.getStatusCode()) && !Constants.OVERTIME_DRAFT.equals(overtimeObject.getStatusCode())) {
            baseEventsPostProcessor.registerEvent(OvertimeEventListinerImpl.TYPE, MyUpdateItem.EDIT, object, userManager.getUser());
        }
        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), object, user);
        workflowEvent.setEntityType(RelationItem.TYPE_OVERTIME);

        if (OVERTIME_SUBMITTED.equals(object.getStatus())) {
            boolean hasAlerts = false;
            List<EdsWorkflowRule> rules = workflowRuleManager.getByModuleAndActions(WorkflowRule._WORKFLOW_MODULE_OVERTIME, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT);
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
//                    messageManager.sendOvertimeToApprover(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (OVERTIME_APPROVED.equals(object.getStatus())) {
            try {
                for (EdsOvertimeObjectData item : object.getLineItems()) {
//                    messageManager.sendOvertimeToEmployee(item.getEmployee(), object);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statusChanged || overtimeObject.getStatusCode().equals(OVERTIME_SUBMITTED)) {
            baseEventsPostProcessor.registerEvent(OvertimeEventListinerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, object, userManager.getUser());
        }
        return object.getObjectID();
    }

    @Override
    public OvertimeObject getOvertimeObject(Integer objectId, Boolean isEditForm) {
        EdsOvertimeObject overtimeObject = overtimeManager.get(objectId);
        OvertimeObject object = new OvertimeObject();
        if (overtimeObject != null) {
            object = overtimeObject.toRpc(true);
            if (isEditForm) {
                NumberData numberData = generateOvertimeCode();
                if (object.getCode() != null && object.getIntNumber() != null) {
                    numberData.setNumberString(object.getCode());
                    numberData.setIntNumber(object.getIntNumber());
                }
                object.setNumberData(numberData);
            }
            ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Overtime);
            object.setCustomFieldItems((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(overtimeObject.getCustomFields(), customFieldsItems));
        }
        object.setApprover(approverManager.isExistApproverByEntityType(RelationItem.TYPE_OVERTIME));

        return object;
    }

    private EdsOvertimeCustomFields createOvertimeCustomFields(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsOvertimeCustomFields customFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                customFields = overtimeCFManager.get(customFieldItems.get(0).getObjectId());
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
                customFields = new EdsOvertimeCustomFields();
                overtimeCFManager.create(customFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customFields, customFieldItems);
            return customFields;
        }
        return null;
    }

    @Override
    public void updateOvertimeItemsAndStatus(OvertimeObject data) {
        EdsOvertimeObject overtimeObject = overtimeManager.get(data.getId());
        EdsUser user = userManager.getUser();
        //updating status
        EdsReference referenceStatus = referenceManager.findReference(Constants.OVERTIME_STATUS, data.getStatusCode());
        if (!OVERTIME_APPROVED.equals(data.getStatusCode())) {
            overtimeObject.setOverallStatus(referenceStatus);
        } else if (OVERTIME_APPROVED.equals(data.getStatusCode()) && overtimeObject.getOverallStatus() != null && OVERTIME_DRAFT.equals(overtimeObject.getOverallStatus().getCode())) {
            overtimeObject.setOverallStatus(referenceManager.findReference(Constants.OVERTIME_STATUS, Constants.OVERTIME_SUBMITTED));
        }
        overtimeObject.updateStatus(referenceStatus);
        overtimeManager.createOrUpdate(overtimeObject);

        if (!OVERTIME_APPROVED.equals(data.getStatusCode()) && !OVERTIME_DRAFT.equals(data.getStatusCode())) {
            baseEventsPostProcessor.registerEvent(OvertimeEventListinerImpl.TYPE, MyUpdateItem.EDIT, overtimeObject, userManager.getUser());
        }

        /* Run workflow approval process */
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), overtimeObject, user);
        workflowEvent.setEntityType(RelationItem.TYPE_OVERTIME);

        if (OVERTIME_SUBMITTED.equals(overtimeObject.getStatus())) {
            boolean hasAlerts = false;
            List<EdsWorkflowRule> rules = workflowRuleManager.getByModuleAndActions(WorkflowRule._WORKFLOW_MODULE_OVERTIME, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT);
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
//                    messageManager.sendOvertimeToApprover(overtimeObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (OVERTIME_APPROVED.equals(overtimeObject.getStatus())) {
            try {
                for (EdsOvertimeObjectData item : overtimeObject.getLineItems()) {
//                    messageManager.sendOvertimeToEmployee(item.getEmployee(), overtimeObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (data.getStatusCode().equals(OVERTIME_SUBMITTED)) {
            baseEventsPostProcessor.registerEvent(OvertimeEventListinerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, overtimeObject, userManager.getUser());
        }
//        if (OVERTIME_APPROVED.equals(overtimeObject.getStatus()) && overtimeObject.getCurrentApprover().getObjectID().equals(overtimeObject.getLastApprover().getObjectID())) {
//            createAdditionalPaymentFromOvertime(overtimeObject.getObjectID());
//        }
    }

    public BigDecimal calculateMaterialAidBalance(EdsEmployee employee, Map<String, String> employeeSettingsMap, String categoryCode, BigDecimal mrotValue, Date date) {
        Integer employeeId = employee.getObjectID();
        if (employeeSettingsMap == null || employeeSettingsMap.isEmpty()) {
            String[] settingsKeys = {categoryCode};
            employeeSettingsMap = employeePayrollSettingsManager.getEmployeesPayrollSettingMap(Collections.singletonList(employeeId), settingsKeys).row(employeeId);
        }

        final Calendar fromBeginningOfTheYear = isCountryUK() ? PayrollUtils.getBeginningOfTaxYear(date) : PayrollUtils.getBeginningOfTaxYearForNonUK(date);
        final Calendar toEndOfTheYear = isCountryUK() ? PayrollUtils.getEndOfTaxYear(date) : PayrollUtils.getEndOfTaxYearForNonUK(date);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);


        BigDecimal categoryLimit = BigDecimal.ZERO;
        if (PayrollConstants.MATERIAL_AID_TYPE_FUNERAL.equals(categoryCode)) {
            categoryLimit = new BigDecimal("4.22").setScale(5, RoundingMode.HALF_UP);
        } else if (PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS.equals(categoryCode)) {
            categoryLimit = new BigDecimal("4.22").setScale(5, RoundingMode.HALF_UP);
        } else if (PayrollConstants.MATERIAL_AID_TYPE_GIFT.equals(categoryCode)) {
            categoryLimit = new BigDecimal("2.11").setScale(5, RoundingMode.HALF_UP);
        }
//        categoryLimit = categoryLimit.multiply(mrotValue); //TODO related to MROT

        //subtract if employee has material aid balance
        Date hireDate = employee.getStartDate();
        Date payslipDate = date;
        if (hireDate != null && ServerUtils.isSameYear(hireDate, payslipDate)) {
            String limitBalanceString = employeeSettingsMap.get(categoryCode);
            if (limitBalanceString != null && !limitBalanceString.isEmpty()) {
                BigDecimal limitInitialBalance = new BigDecimal(limitBalanceString.replaceAll(",", ""));
                categoryLimit = categoryLimit.subtract(limitInitialBalance);
            }
        }

        BigDecimal totalPaidMaterialAid = paymentDeductionManager.getPayslipMaterialAidTotalPayments(fromBeginningOfTheYear.getTime(), toEndOfTheYear.getTime(), employeeId, categoryCode);
        totalPaidMaterialAid = totalPaidMaterialAid.add(additionalPaymentManager.getAddPaymentMaterialAidTotalPayments(calendar.get(Calendar.YEAR), employeeId, categoryCode));

        BigDecimal materialAidBalance = categoryLimit.subtract(totalPaidMaterialAid);

        return materialAidBalance.compareTo(BigDecimal.ZERO) > 0 ? materialAidBalance : BigDecimal.ZERO;
    }


    public ListResult<AdditionalPayment> getAdditionalPaymentList(ListingFilterParameter fp) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsEmployee.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get Additional Payment list (from solr)");
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        FacetFilterRpc paymentFacetFilter = fp.getFacetFilter();
        if (paymentFacetFilter != null && !paymentFacetFilter.isFilterChanges()) {
            paymentFacetFilter = commonServiceLocal.getUserFacetFilter(paymentFacetFilter);
        }
        if (paymentFacetFilter != null) {
            if (paymentFacetFilter.getSearchKey() != null && !"".equals(paymentFacetFilter.getSearchKey())) {
                fp.setSearchKey(paymentFacetFilter.getSearchKey());
            }
            fp.setStartDate(paymentFacetFilter.getStartDate());
            fp.setEndDate(paymentFacetFilter.getEndDate());
            fp.setFacetFilter(paymentFacetFilter);
        }
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        if (panelTools == null) {
            ArrayList<String> columnCodeName = OpportunityListItem.defaultColumnNames;
            panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(columnCodeName);
            fp.setColumnsOfListing(columnCodeName);
        }
        if (panelTools.isCustomFieldsShown()) {
            fp.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.AdditionalPayment));
        }
        EdsUser edsUser = employeeManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        EdsEmployee employee = edsUser.getEmployee();

        if (edsUser.hasRole(EdsRole.TL_CODE)) {
            boolean multiDepartmentEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_MULTI_DEPARTMENT_LEADER);
            if (multiDepartmentEnabled) {
                List<EdsDepartment> departments = departmentManager.getTeamsByEmployeeId(edsUser.getObjectID());
                if (departments != null) {
                    List<Integer> departmentIDs = departments.stream().map(EdsDepartment::getObjectID).toList();
                    fp.setDepartmentIds(ServerUtils.getAsCommoDelimited(departmentIDs, "0", " "));
                }
            } else {
                EdsDepartment edsDepartment = departmentManager.getDepartmentByLeader(edsUser);
                if (edsDepartment != null) {
                    fp.setDepartmentId(edsDepartment.getObjectID());
                }
            }
        }
        boolean isSeeAllPermission = ServerUtils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SEE_ALL);
        if (!isSeeAllPermission && !edsUser.hasRole(EdsRole.ADMIN_CODE)) {
            ArrayList<Integer> entityIDs = approverManager.getEntityIDs(RelationItem.TYPE_ADDITIONAL_PAYMENT, employee.getObjectID());
            fp.setObjectIDs(entityIDs);
        }

        String solrQuery = QueryBuilderForSolr.getAdditionalPaymentSolrQuery(fp, edsUser) +
                SolrFacetUtils.generateForPricesFacet(paymentFacetFilter, FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[4]) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(paymentFacetFilter, edsCompany,
                        SolrSinglePayrunRepresenter.FIELD_FROM_DATE,
                        SolrSinglePayrunRepresenter.FIELD_TO_DATE, FacetContentType.AdditionalPaymentFacetFilter.getContentCode()[4]);
        return getAdditionalPaymentResponse(fp, solrQuery);
    }

    public ListResult<AdditionalPayment> getAdditionalPaymentItemList(ListingFilterParameter fp) {
        ArrayList<AdditionalPayment> result = new ArrayList<>();
        EdsUser user = userManager.getUser();
        boolean isSeeAllPermission = ServerUtils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SEE_ALL);
        if (!isSeeAllPermission && ServerUtils.hasPermission(PermissionConstants.PAYROLL_ADDITIONAL_PAYMENT_SEE_OWN)) {
            if (user.hasEitherRoles(EdsRole.TL_CODE)) {
                EdsDepartment department = departmentManager.getDepartmentByLeader(user);
                if (department != null) {
                    fp.setDepartmentId(department.getObjectID());
                    fp.setEmployeeId(null);
                }
            } else {
                if (user.getEmployee() != null) {
                    fp.setEmployeeId(user.getEmployee().getObjectID());
                }
            }
        }
        List<EdsPaymentDeduction> items = additionalPaymentManager.getAdditionalPaymentItemList(fp);
        for (EdsPaymentDeduction item : items) {
            AdditionalPayment payment = item.getAdditionalPayment().getRPC(false);
            payment.setStatus(commonLocalizer.localize(payment.getStatus().toLowerCase(), payment.getStatus()));
            payment.setMonth(ServerUtils.convertMonthToInterfaceLanguage(payment.getMonth()));
            EdsEmployee edsEmployee = item.getEmployee();
            if (edsEmployee != null) {
                payment.setEmployee(edsEmployee.getAsSelectItem());
                if (edsEmployee.getProfile() != null) {
                    payment.setEmployeeCode(edsEmployee.getProfile().getEmployeeCode());
                }
            }
            payment.setTotal(item.getPaymentAmount());
            payment.addItem(item.getRPC());
            result.add(payment);
        }
        return new ListResult<>(result, result.size());
    }

    public Integer getIncidentCountByDynamicId(ListingFilterParameter filterParameter, Integer employeeId) {
        List<EdsPerformanceNote> incidentList = performanceNoteManager.getIncidentListByEmployee(filterParameter, employeeId);
        if (incidentList != null && incidentList.size() > 0) {
            return incidentList.size();
        }
        return 0;
    }

    public AdditionalPayment getEmployeesForAdditionalPayment(ListingFilterParameter filterParameter, HashMap<Integer, PaymentDeductionObject> existingItems) {
        AdditionalPayment result = new AdditionalPayment();
        List<EdsEmployee> employees = new ArrayList<>();
        List<PaymentDeductionObject> items = new ArrayList<>();
        boolean isEnableWithMiddle = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_FULLNAME_WITH_MIDDLENAME);
        HashMap<Integer, BigDecimal> salaryMap = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        if (filterParameter.getMonthId() == null) {
            filterParameter.setMonthId(calendar.get(Calendar.MONTH));
        }
        if (filterParameter.getYear() == null) {
            filterParameter.setYear(calendar.get(Calendar.YEAR));
        }

        Integer itemCount = 0;

        if (filterParameter.getEmployeeIDs() != null || filterParameter.getDepartmentId() != null || filterParameter.getLocationId() != null || filterParameter.getSupervisorId() != null || filterParameter.getObjectId() != null) {
            itemCount = employeeManager.getEmployeesCountByFilter(filterParameter);
            employees = employeeManager.getEmployeesByFilter(filterParameter);
        }
        List<Integer> employeeIds = employees.stream().map(EdsEmployee::getObjectID).collect(Collectors.toList());
        filterParameter.setEmployeeIDs(ServerUtils.getAsCommoDelimited(employeeIds, "0"));

        String[] settingsKeys = {
                PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS,
                PayrollConstants.MATERIAL_AID_TYPE_FUNERAL,
                PayrollConstants.MATERIAL_AID_TYPE_GIFT
        };
        Table<Integer, String, String> employeeSettingsMap = employeePayrollSettingsManager.getEmployeesPayrollSettingMap(employeeIds, settingsKeys);

        if (filterParameter.isCalculateByLastMonth()) {
            //get last month
            calendar.set(Calendar.MONTH, filterParameter.getMonthId());
            calendar.set(Calendar.YEAR, filterParameter.getYear());
            calendar.add(Calendar.MONTH, -1);

            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setEmployeeIDs(filterParameter.getEmployeeIDs());
            fp.setMonthId(calendar.get(Calendar.MONTH));
            fp.setYear(calendar.get(Calendar.YEAR));

            ArrayList<Integer> categoryIds = new ArrayList<>();
            if (filterParameter.isBasicPlusAllowancePaymentType() && !CollectionUtils.isEmpty(filterParameter.getPaymentCategories())) {
                categoryIds = filterParameter.getPaymentCategories().stream().map(SelectItem::getId).collect(Collectors.toCollection(ArrayList::new));
            }
            fp.setObjectIDs(categoryIds);
            salaryMap = payslipPaymentsManager.getEmployeeSalaryForPeriod(fp);
        } else {
            calendar.set(Calendar.MONTH, filterParameter.getMonthId());
            calendar.set(Calendar.YEAR, filterParameter.getYear());
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
            salaryMap = salaryHistoryManager.getEmployeeSalaryMap(employeeIds, calendar.getTime());
        }

        calendar.set(Calendar.MONTH, filterParameter.getMonthId());
        calendar.set(Calendar.YEAR, filterParameter.getYear());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

        log.info("Employee Size :" + employees.size());
        int i = 0;
        for (EdsEmployee employee : employees) {
            i++;
            log.info(" Current employee element : " + i);
            if (existingItems != null && existingItems.containsKey(employee.getObjectID())) {
                items.add(existingItems.get(employee.getObjectID()));
            } else {
                items.add(getEmployeeForAdditionalPayment(filterParameter, employee, salaryMap.get(employee.getObjectID()), employeeSettingsMap, calendar.getTime(), isEnableWithMiddle));
            }
        }
        log.info("All Employee Finished :" + employees.size());
        result.setItems(items);
        result.setTotalItems(itemCount);
        return result;
    }

    private PaymentDeductionObject getEmployeeForAdditionalPayment(ListingFilterParameter filterParameter, EdsEmployee edsEmployee, BigDecimal salary,
                                                                   Table<Integer, String, String> employeeSettingsMap,
                                                                   Date date, boolean isEnableWithMiddle) {
        PaymentDeductionObject item = new PaymentDeductionObject();
        SelectItem employeeItem = new SelectItem();
        employeeItem.setId(edsEmployee.getObjectID());
        employeeItem.setName(isEnableWithMiddle ? edsEmployee.getFormmattedName() : edsEmployee.getFullName());

        if (filterParameter.getLimit() > 0) {
            Integer incidentId = getIncidentCountByDynamicId(filterParameter, edsEmployee.getObjectID());
            item.setCountIncident(incidentId);
        }

        if (edsEmployee.getProfile() != null) {
            EdsEmployeeProfile edsEmployeeProfile = edsEmployee.getProfile();
            employeeItem.setDescription(edsEmployeeProfile.getEmployeeCode());
            item.setEmployeeBasicSalary(salary == null ? BigDecimal.ZERO : salary);
            if (filterParameter.isCalculateByLastMonth()) {
                item.setBasicPlusAllowance(item.getEmployeeBasicSalary());
            } else if (filterParameter.isBasicPlusAllowancePaymentType()) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, filterParameter.getMonthId());
                calendar.set(Calendar.YEAR, filterParameter.getYear());
                calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
                BigDecimal totalAmount = getTotalAllowancesByEmployee(edsEmployee, filterParameter.getPaymentCategories(), calendar.getTime(), item.getEmployeeBasicSalary());
                item.setBasicPlusAllowance(totalAmount);
            }
            BigDecimal empMode = BigDecimal.ONE;
            if (edsEmployeeProfile.getEmploymentMode() != null) {

                if ("FULL_TIME".equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                    empMode = BigDecimal.ONE;
                } else if ("075_TIME".equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                    empMode = BigDecimal.valueOf(0.75);
                } else if ("PART_TIME".equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                    empMode = BigDecimal.valueOf(0.50);
                } else if ("QUARTER_TIME".equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                    empMode = BigDecimal.valueOf(0.25);
                }
            }
            item.setEmpMode(empMode);
        }

//        item.getLgotaBalanceMap().put(PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS, calculateMaterialAidBalance(edsEmployee, employeeSettingsMap.row(edsEmployee.getObjectID()), PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS, null, date)); //TODO related to MROT
//        item.getLgotaBalanceMap().put(PayrollConstants.MATERIAL_AID_TYPE_FUNERAL, calculateMaterialAidBalance(edsEmployee, employeeSettingsMap.row(edsEmployee.getObjectID()), PayrollConstants.MATERIAL_AID_TYPE_FUNERAL, null, date));
//        item.getLgotaBalanceMap().put(PayrollConstants.MATERIAL_AID_TYPE_GIFT, calculateMaterialAidBalance(edsEmployee, employeeSettingsMap.row(edsEmployee.getObjectID()), PayrollConstants.MATERIAL_AID_TYPE_GIFT, null, date));
//TODO will discuss this later
        collectEmployeeCategories(edsEmployee, item);
        item.setEmployee(employeeItem);
        return item;
    }

    private boolean checkEmployeeForResigned(EdsEmployee employee, ListingFilterParameter filterParameter) {
        if (employee.getAccountStatus() != null &&
                EMPLOYEE_STATUS_RESIGNED.equals(employee.getAccountStatus().getCode())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            if (filterParameter.getMonthId() != null) {
                calendar.set(Calendar.MONTH, filterParameter.getMonthId());
            }
            if (filterParameter.getYear() != null) {
                calendar.set(Calendar.YEAR, filterParameter.getYear());
            }
            return calendar.getTime().compareTo(employee.getEndDate()) > 0;
        }
        return false;
    }

    @Override
    public ListResult<MultiCashAdvanceItem> getMultiCashAdvanceList(ListingFilterParameter fp) {
        List<EdsMultiCashAdvance> result = multiCashAdvanceManager.getMultiCashAdvanceList(fp);
        Integer total = multiCashAdvanceManager.getMultiCashAdvanceCount(fp);

        ListPanelToolRpc panelTools = fp.getListPanelTool();
        ArrayList<MultiCashAdvanceItem> list = new ArrayList<>();
        for (EdsMultiCashAdvance edsMultiCashAdvance : result) {
            MultiCashAdvanceItem multiCashAdvanceItem = new MultiCashAdvanceItem();
            multiCashAdvanceItem.setObjectID(edsMultiCashAdvance.getObjectID());
            multiCashAdvanceItem.setNumber(edsMultiCashAdvance.getNumber());
            multiCashAdvanceItem.setType(edsMultiCashAdvance.getType());
            multiCashAdvanceItem.setDate(new DateNonConvertable(edsMultiCashAdvance.getRequestDate()));
            multiCashAdvanceItem.setStatus(edsMultiCashAdvance.getStatus().getAsSelectItem());
            if (edsMultiCashAdvance.getCurrentApprover() != null && edsMultiCashAdvance.getCurrentApprover().getExactEmployee() != null) {
                if (edsMultiCashAdvance.getCurrentApprover().getExactEmployee().isEmployee()) {
                    EdsEmployee edsEmployee = edsMultiCashAdvance.getCurrentApprover().getExactEmployee().getEmployee();
                    if (edsEmployee != null && edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null) {
                        multiCashAdvanceItem.setApprover(new SelectItem(edsEmployee.getObjectID(), edsEmployee.getProfile().getEmployeeCode() + " - " + edsEmployee.getFullName()));
                    } else {
                        multiCashAdvanceItem.setApprover(edsMultiCashAdvance.getCurrentApprover().getExactEmployee().getAsSelectItem());
                    }
                } else {
                    multiCashAdvanceItem.setApprover(edsMultiCashAdvance.getCurrentApprover().getExactEmployee().getAsSelectItem());
                }
            }
            multiCashAdvanceItem.setTotalAmount(edsMultiCashAdvance.getTotalAmount());
//            multiCashAdvanceItem.setRemainingAmount(edsMultiCashAdvance.getTotalAmount());

            list.add(multiCashAdvanceItem);
        }
        return new ListResult<>(list, total);
    }

    public ArrayList<PaymentDeductionObject> getEmployeesForMultiCashAdvance(ListingFilterParameter filterParameter) {
        List<EdsEmployee> employees;
        ArrayList<PaymentDeductionObject> items = new ArrayList<>();
        boolean isEnableWithMiddle = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_FULLNAME_WITH_MIDDLENAME);
        HashMap<Integer, BigDecimal> salaryMap = employeePayrollSettingsManager.getEmployeeSalaryMap(null);
        if (filterParameter.getEmployeeId() != null) {
            EdsEmployee edsEmployee = employeeManager.get(filterParameter.getEmployeeId());
            if (edsEmployee != null) {
                PaymentDeductionObject item = new PaymentDeductionObject();

                SelectItem employeeItem = new SelectItem();
                employeeItem.setId(edsEmployee.getObjectID());
                employeeItem.setName(isEnableWithMiddle ? edsEmployee.getFormmattedName() : edsEmployee.getFullName());
                if (edsEmployee.getProfile() != null) {
                    employeeItem.setDescription(edsEmployee.getProfile().getEmployeeCode());
                    BigDecimal bs = salaryMap.get(filterParameter.getEmployeeId());
                    item.setEmployeeBasicSalary(bs == null ? BigDecimal.ZERO : bs);
                    if (filterParameter.isBasicPlusAllowancePaymentType()) {
                        BigDecimal totalAmount = getTotalAllowancesByEmployee(edsEmployee, filterParameter.getPaymentCategories(), item.getPaymentDate(), item.getEmployeeBasicSalary());
                        item.setBasicPlusAllowance(totalAmount);
                    }
                }
                BigDecimal empMode = BigDecimal.ONE;
                EdsEmployeeProfile edsEmployeeProfile = edsEmployee.getProfile();
                if (edsEmployeeProfile != null && edsEmployeeProfile.getEmploymentMode() != null) {
                    if (FULL_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                        empMode = BigDecimal.ONE;
                    } else if (S_075_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                        empMode = BigDecimal.valueOf(0.75);
                    } else if (PART_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                        empMode = BigDecimal.valueOf(0.50);
                    } else if (QUARTER_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                        empMode = BigDecimal.valueOf(0.25);
                    }
                }
                item.setEmpMode(empMode);
                item.setEmployee(employeeItem);
                items.add(item);
            }
        } else if (filterParameter.getDepartmentId() != null) {
            List<EdsEmployeeDepartment> edsEmployeeDepartments = employeeDepartmentManager.getTeamEmployees(filterParameter.getDepartmentId());

            for (EdsEmployeeDepartment employeeDepartment : edsEmployeeDepartments) {
                PaymentDeductionObject item = new PaymentDeductionObject();
                SelectItem employeeItem = new SelectItem();
                if (employeeDepartment.getEmployee() != null) {
                    employeeItem.setId(employeeDepartment.getEmployee().getObjectID());
                    employeeItem.setName(isEnableWithMiddle ? employeeDepartment.getEmployee().getFormmattedName() : employeeDepartment.getEmployee().getFullName());
                    if (employeeDepartment.getEmployee().getProfile() != null) {
                        EdsEmployeeProfile edsEmployeeProfile = employeeDepartment.getEmployee().getProfile();
                        employeeItem.setDescription(edsEmployeeProfile.getEmployeeCode());
                        BigDecimal bs = salaryMap.get(employeeDepartment.getEmployee().getObjectID());
                        item.setEmployeeBasicSalary(bs == null ? BigDecimal.ZERO : bs);
                        if (filterParameter.isBasicPlusAllowancePaymentType()) {
                            BigDecimal totalAmount = getTotalAllowancesByEmployee(employeeDepartment.getEmployee(), filterParameter.getPaymentCategories(), item.getPaymentDate(), item.getEmployeeBasicSalary());
                            item.setBasicPlusAllowance(totalAmount);
                        }
                        BigDecimal empMode = BigDecimal.ONE;
                        if (edsEmployeeProfile != null && edsEmployeeProfile.getEmploymentMode() != null) {
                            if (FULL_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                                empMode = BigDecimal.ONE;
                            } else if (S_075_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                                empMode = BigDecimal.valueOf(0.75);
                            } else if (PART_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                                empMode = BigDecimal.valueOf(0.50);
                            } else if (QUARTER_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                                empMode = BigDecimal.valueOf(0.25);
                            }
                        }
                        item.setEmpMode(empMode);
                    }
                    item.setEmployee(employeeItem);
                    items.add(item);
                }
            }
        } else if (filterParameter.getLocationId() != null) {
            List<EdsEmployee> edsLocationEmployees = locationManager.getLocationEmployee(filterParameter.getLocationId());
            for (EdsEmployee employeeLocation : edsLocationEmployees) {
                PaymentDeductionObject item = new PaymentDeductionObject();
                SelectItem employeeItem = new SelectItem();

                employeeItem.setId(employeeLocation.getObjectID());
                employeeItem.setName(isEnableWithMiddle ? employeeLocation.getFormmattedName() : employeeLocation.getFullName());
                if (employeeLocation.getProfile() != null) {
                    employeeItem.setDescription(employeeLocation.getProfile().getEmployeeCode());
                    BigDecimal bs = salaryMap.get(employeeLocation.getEmployee().getObjectID());
                    item.setEmployeeBasicSalary(bs == null ? BigDecimal.ZERO : bs);
                    if (filterParameter.isBasicPlusAllowancePaymentType()) {
                        BigDecimal totalAmount = getTotalAllowancesByEmployee(employeeLocation, filterParameter.getPaymentCategories(), item.getPaymentDate(), item.getEmployeeBasicSalary());
                        item.setBasicPlusAllowance(totalAmount);
                    }
                }
                BigDecimal empMode = BigDecimal.ONE;
                if (employeeLocation.getProfile() != null && employeeLocation.getProfile().getEmploymentMode() != null) {
                    EdsEmployeeProfile edsEmployeeProfile = employeeLocation.getProfile();

                    if (FULL_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                        empMode = BigDecimal.ONE;
                    } else if (S_075_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                        empMode = BigDecimal.valueOf(0.75);
                    } else if (PART_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                        empMode = BigDecimal.valueOf(0.50);
                    } else if (QUARTER_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                        empMode = BigDecimal.valueOf(0.25);
                    }
                }
                item.setEmpMode(empMode);
                item.setEmployee(employeeItem);
                items.add(item);
            }
        } else {
            if (filterParameter.getObjectId() != null && filterParameter.getObjectId().equals(0)) {
                employees = employeeManager.list(filterParameter);
            } else {
                employees = employeeManager.getEmployeeByPayrollBatch(filterParameter.getObjectId());
            }

            for (EdsEmployee edsEmployee : employees) {
                if (edsEmployee != null) {
                    PaymentDeductionObject item = new PaymentDeductionObject();
                    SelectItem employeeItem = new SelectItem();
                    employeeItem.setId(edsEmployee.getObjectID());
                    employeeItem.setName(isEnableWithMiddle ? edsEmployee.getFormmattedName() : edsEmployee.getFullName());
                    if (edsEmployee.getProfile() != null) {
                        employeeItem.setDescription(edsEmployee.getProfile().getEmployeeCode());
                        BigDecimal bs = salaryMap.get(edsEmployee.getEmployee().getObjectID());
                        item.setEmployeeBasicSalary(bs == null ? BigDecimal.ZERO : bs);
                        if (filterParameter.isBasicPlusAllowancePaymentType()) {
                            BigDecimal totalAmount = getTotalAllowancesByEmployee(edsEmployee, filterParameter.getPaymentCategories(), item.getPaymentDate(), item.getEmployeeBasicSalary());
                            item.setBasicPlusAllowance(totalAmount);
                        }

                        BigDecimal empMode = BigDecimal.ONE;
                        if (edsEmployee.getProfile().getEmploymentMode() != null) {
                            EdsEmployeeProfile edsEmployeeProfile = edsEmployee.getProfile();

                            if (FULL_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                                empMode = BigDecimal.ONE;
                            } else if (S_075_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                                empMode = BigDecimal.valueOf(0.75);
                            } else if (PART_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                                empMode = BigDecimal.valueOf(0.50);
                            } else if (QUARTER_TIME.equals(edsEmployeeProfile.getEmploymentMode().getCode())) {
                                empMode = BigDecimal.valueOf(0.25);
                            }
                        }
                        item.setEmpMode(empMode);
                    }
                    item.setEmployee(employeeItem);
                    items.add(item);
                }
            }
        }

        if (items.size() > 0) {
            items.sort(Comparator.comparing(o -> o.getEmployee().getName()));
            if (!ServerUtils.isNullOrEmpty(filterParameter.getSearchKey())) {
                items = (ArrayList<PaymentDeductionObject>) items.stream().filter(emp -> emp.getEmployee().getName().toLowerCase().contains(filterParameter.getSearchKey().toLowerCase())).collect(Collectors.toList());
            }
        }
        return items;
    }

    @Override
    public MultiCashAdvanceItem getMultiCashAdvanceData(Integer objectdID) {
        MultiCashAdvanceItem multiCashAdvanceItem = new MultiCashAdvanceItem();
        BankTransferNumberData numberData = generateMultiCashAdvanceNumber();
        multiCashAdvanceItem.setNumberData(numberData);
        multiCashAdvanceItem.setNumber(numberData.getTransferNumber());
        multiCashAdvanceItem.setPaymentMethods(allInOneService.getPaymentMethodList());
        String doubleConfirmation = getCompanyPayrollSettings(DOUBLE_CONFIRMATION);
        multiCashAdvanceItem.setDoubleConfirmationEnabled("true".equals(doubleConfirmation));
        if (objectdID != null) {
            EdsMultiCashAdvance edsMultiCashAdvance = multiCashAdvanceManager.get(objectdID);
            multiCashAdvanceItem.setNumber(edsMultiCashAdvance.getNumber());
            multiCashAdvanceItem.setType(edsMultiCashAdvance.getType());
            if ("group".equals(edsMultiCashAdvance.getType()) && edsMultiCashAdvance.getPayrollBatch() != null) {
                multiCashAdvanceItem.setEmployee(edsMultiCashAdvance.getPayrollBatch().asSelectItem());
            } else if ("department".equals(multiCashAdvanceItem.getType()) && edsMultiCashAdvance.getDepartment() != null) {
                multiCashAdvanceItem.setEmployee(edsMultiCashAdvance.getDepartment().getAsSelectItem());
            } else if ("location".equals(multiCashAdvanceItem.getType()) && edsMultiCashAdvance.getLocation() != null) {
                multiCashAdvanceItem.setEmployee(edsMultiCashAdvance.getLocation().getAsSelectItem());
            } else if ("employee".equals(multiCashAdvanceItem.getType()) && edsMultiCashAdvance.getEmployee() != null) {
                multiCashAdvanceItem.setEmployee(edsMultiCashAdvance.getEmployee().getAsSelectItem());
            }
            if (edsMultiCashAdvance.getCategory() != null) {
                multiCashAdvanceItem.setCategoryItem(edsMultiCashAdvance.getCategory().getAsSelectItem());
            }
            if (edsMultiCashAdvance.getPaymentMethod() != null) {
                multiCashAdvanceItem.setPaymentMethod(edsMultiCashAdvance.getPaymentMethod().getAsSelectItem());
            }
            multiCashAdvanceItem.setAmountType(edsMultiCashAdvance.getAmountType());
            multiCashAdvanceItem.setDate(new DateNonConvertable(edsMultiCashAdvance.getRequestDate()));
            multiCashAdvanceItem.setStatus(edsMultiCashAdvance.getStatus().getAsSelectItem());
            if (edsMultiCashAdvance.getCurrentApprover() != null && edsMultiCashAdvance.getCurrentApprover().getExactEmployee() != null) {
                if (edsMultiCashAdvance.getCurrentApprover().getExactEmployee().isEmployee()) {
                    EdsEmployee edsEmployee = edsMultiCashAdvance.getCurrentApprover().getExactEmployee().getEmployee();
                    if (edsEmployee != null && edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null) {
                        multiCashAdvanceItem.setApprover(new SelectItem(edsEmployee.getObjectID(), edsEmployee.getProfile().getEmployeeCode() + " - " + edsEmployee.getFullName()));
                    } else {
                        multiCashAdvanceItem.setApprover(edsMultiCashAdvance.getCurrentApprover().getExactEmployee().getAsSelectItem());
                    }
                } else {
                    multiCashAdvanceItem.setApprover(edsMultiCashAdvance.getCurrentApprover().getExactEmployee().getAsSelectItem());
                }
            }
            List<EdsCashAdvance> cashAdvanceList = cashAdvanceManager.getListByMultiCashAdvance(edsMultiCashAdvance.getObjectID());
            if (!CollectionUtils.isEmpty(cashAdvanceList)) {
                multiCashAdvanceItem.setCashAdvanceItems(cashAdvanceList.stream().map(EdsCashAdvance::getRPC).collect(Collectors.toList()));
            }
        }
        return multiCashAdvanceItem;
    }

    @Override
    @Transactional
    public TestRPC saveMultiCashAdvance(MultiCashAdvanceItem multiCashAdvanceItem, boolean fromView) {

        TestRPC result = new TestRPC();
        if (multiCashAdvanceItem.getNumber() != null && multiCashAdvanceManager.numberExists(multiCashAdvanceItem.getNumber(), multiCashAdvanceItem.getObjectID())) {
            result.setMessage(CashAdvanceItem.NUMBER_EXISTS);
            return result;
        }
        Integer objectId = createMultiCashAdvance(multiCashAdvanceItem, fromView);
        multiCashAdvanceItem.setObjectID(objectId);
        createMultiCashAdvanceItems(multiCashAdvanceItem, fromView);
        result.setId(objectId);
        return result;
    }

    @Transactional
    public Integer createMultiCashAdvance(MultiCashAdvanceItem multiCashAdvanceItem, boolean fromView) {
        EdsMultiCashAdvance edsMultiCashAdvance = multiCashAdvanceItem.getObjectID() != null ? multiCashAdvanceManager.get(multiCashAdvanceItem.getObjectID()) : new EdsMultiCashAdvance();

        if (edsMultiCashAdvance == null) {
            edsMultiCashAdvance = new EdsMultiCashAdvance();
        }

        String cashAdvanceOldStatus = edsMultiCashAdvance.getStatus() != null ? edsMultiCashAdvance.getStatus().getCode() : null;
        if (!fromView) {
            edsMultiCashAdvance.setIntNumber(multiCashAdvanceItem.getIntNumber());
            edsMultiCashAdvance.setNumber(multiCashAdvanceItem.getNumber());
            edsMultiCashAdvance.setType(multiCashAdvanceItem.getType());
            edsMultiCashAdvance.setAmountType(multiCashAdvanceItem.getAmountType());
            if ("group".equals(multiCashAdvanceItem.getType())) {
                edsMultiCashAdvance.setPayrollBatch(payrollBatchManager.get(multiCashAdvanceItem.getEmployee().getId()));
            } else if ("department".equals(multiCashAdvanceItem.getType())) {
                edsMultiCashAdvance.setDepartment(departmentManager.get(multiCashAdvanceItem.getEmployee().getId()));
            } else if ("location".equals(multiCashAdvanceItem.getType())) {
                edsMultiCashAdvance.setLocation(locationManager.get(multiCashAdvanceItem.getEmployee().getId()));
            } else if ("employee".equals(multiCashAdvanceItem.getType())) {
                edsMultiCashAdvance.setEmployee(employeeManager.get(multiCashAdvanceItem.getEmployee().getId()));
            }

            if (multiCashAdvanceItem.getCategoryItem() != null) {
                edsMultiCashAdvance.setCategory(categoryManager.get(multiCashAdvanceItem.getCategoryItem().getId()));
            }

            if (multiCashAdvanceItem.getObjectID() == null) {
                if (multiCashAdvanceItem.getCreationDate() != null) {
                    edsMultiCashAdvance.setCreationDate(multiCashAdvanceItem.getCreationDate().getNonConvertedDate());
                } else {
                    edsMultiCashAdvance.setCreationDate(new Date());
                }
            }

            if (multiCashAdvanceItem.getDate() != null) {
                edsMultiCashAdvance.setRequestDate(multiCashAdvanceItem.getDate().getNonConvertedDate());
            }

            if (multiCashAdvanceItem.getApprovedDate() != null) {
                edsMultiCashAdvance.setApprovedDate(multiCashAdvanceItem.getApprovedDate().getNonConvertedDate());
            }

            if (multiCashAdvanceItem.getIntNumber() != null) {
                edsMultiCashAdvance.setIntNumber(multiCashAdvanceItem.getIntNumber());
            }
            edsMultiCashAdvance.setNumber(multiCashAdvanceItem.getNumber());

            edsMultiCashAdvance.setLastUpdateTime(new Date());

            if (multiCashAdvanceItem.getPaymentMethod() != null && multiCashAdvanceItem.getPaymentMethod().getId() != null) {
                edsMultiCashAdvance.setPaymentMethod(paymentMethodManager.get(multiCashAdvanceItem.getPaymentMethod().getId()));
            }
            multiCashAdvanceManager.createOrUpdate(edsMultiCashAdvance);
        }

        if (isOk(multiCashAdvanceItem.getApprovers())) {
            if (Constants.DRAFT.equals(cashAdvanceOldStatus)) {
                // it was difficult to merge expense approvers, so just deleting old records
                approverManager.deletedAprovers(RelationItem.TYPE_CASH_ADVANCE, edsMultiCashAdvance.getObjectID());
                //delete prev/current approvers
                edsMultiCashAdvance.setCurrentApprover(null);
                edsMultiCashAdvance.setPrevApprover(null);
            }
            EdsReference status = (multiCashAdvanceItem.getStatus() != null && multiCashAdvanceItem.getStatus().getCode() != null) ? referenceManager.getByCode(multiCashAdvanceItem.getStatus().getCode()) : null;
            //if cash advance action is "save & close", status will be null.
            multiCashAdvanceItem.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            for (ApproverItemMini approverItem : multiCashAdvanceItem.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null && !_edsApprover.getDeleted()) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (edsMultiCashAdvance.getCurrentApprover() != null && status != null) {
                        edsMultiCashAdvance.getCurrentApprover().setStatus(status);
                    }

                    if (status != null && !APPROVED.equals(status.getCode())) {
                        edsMultiCashAdvance.setOverallStatus(status);
                    }
                    if (edsMultiCashAdvance.isCurrentApproverRejected()) {
                        edsMultiCashAdvance.setOverallStatus(edsMultiCashAdvance.getCurrentApprover().getStatus());
                    }
                    continue;
                }

                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(edsMultiCashAdvance.getObjectID());
                edsApprover.setIs_default(false);
                edsApprover.setDeleted(false);
                if (status != null) {
                    edsApprover.setStatus(status);
                    edsMultiCashAdvance.setOverallStatus(status);
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

                if (edsMultiCashAdvance.getCurrentApprover() == null) {
                    edsMultiCashAdvance.setCurrentApprover(edsApprover);
                }
                edsMultiCashAdvance.getApprovers().add(edsApprover);
            }
            //update after new approvers set
            multiCashAdvanceManager.update(edsMultiCashAdvance);
        } else if (multiCashAdvanceItem.getStatus() != null) {
            edsMultiCashAdvance.setOverallStatus(referenceManager.getByCode(multiCashAdvanceItem.getStatus().getCode()));
        }

        if (!fromView) {
            List<Integer> removedCashAdvances = new ArrayList<>();
            List<EdsCashAdvance> cashAdvanceList = cashAdvanceManager.getListByMultiCashAdvance(edsMultiCashAdvance.getObjectID());
            if (CollectionUtils.isEmpty(cashAdvanceList) && !CollectionUtils.isEmpty(multiCashAdvanceItem.getCashAdvanceItems())) {
                for (EdsCashAdvance edsCashAdvance : cashAdvanceList) {
                    boolean contains = false;
                    for (CashAdvanceItem cashAdvanceItem : multiCashAdvanceItem.getCashAdvanceItems()) {
                        if (edsCashAdvance.getObjectID().equals(cashAdvanceItem.getObjectID()) || cashAdvanceItem.getEmployee() != null && edsCashAdvance.getEmployee().getObjectID().equals(cashAdvanceItem.getEmployee().getId())) {
                            cashAdvanceItem.setObjectID(edsCashAdvance.getObjectID());
                            contains = true;
                        }
                    }
                    if (!contains) {
                        ListingFilterParameter filter = new ListingFilterParameter();
                        filter.setObjectId(edsCashAdvance.getObjectID());
                        List<EdsPayslipPayments> payments = payslipPaymentsManager.getCashAdvancePayments(filter);
                        if (payments != null && !payments.isEmpty()) {
                            edsCashAdvance.setMultiCashAdvance(null);
                            cashAdvanceManager.createOrUpdate(edsCashAdvance);
                        } else {
                            removedCashAdvances.add(edsCashAdvance.getObjectID());
                        }
                    }
                }
            }
            if (!CollectionUtils.isEmpty(removedCashAdvances)) {
                for (Integer id : removedCashAdvances) {
                    deleteCashAdvance(id);
                }
            }
        }
        cashAdvanceManager.flush();
        return edsMultiCashAdvance.getObjectID();
    }

    @Transactional
    public void createMultiCashAdvanceItems(MultiCashAdvanceItem multiCashAdvanceItem, boolean fromView) {
        final String database = ServerSecurityContext.getInstance().getDatabase();
        final String companyId = ServerSecurityContext.getInstance().getCompanyId();
        final Integer userId = userManager.getUser().getObjectID();
        Integer[] fourDigitNumber = {null};

        executor.execute(() -> {
            ServerSecurityContext.getInstance().setDatabase(database);
            ServerSecurityContext.getInstance().setCompanyId(companyId);
            ServerSecurityContext.getInstance().setStaticUserID(userId);

            ArrayList<Callable<Integer>> futureCall = new ArrayList<>();

            if (!CollectionUtils.isEmpty(multiCashAdvanceItem.getCashAdvanceItems())) {
                for (CashAdvanceItem cashAdvanceItem : multiCashAdvanceItem.getCashAdvanceItems()) {
                    if (!fromView) {
                        BankTransferNumberData numberData = fourDigitNumber[0] == null ? generateCashAdvanceNumberFormat() : generateCashAdvanceNumberFormat(fourDigitNumber[0]);
                        if (fourDigitNumber[0] == null) {
                            fourDigitNumber[0] = Integer.valueOf(numberData.getFourDigitNumber());
                            fourDigitNumber[0]--;
                        }
                        fourDigitNumber[0]++;
                        cashAdvanceItem.setNumber(numberData.getTransferNumber());
                        cashAdvanceItem.setIntNumber(Integer.parseInt(numberData.getFourDigitNumber()));
                        cashAdvanceItem.setIntNumber(Integer.parseInt(numberData.getFourDigitNumber()));
                        cashAdvanceItem.setMultiCashAdvanceId(multiCashAdvanceItem.getObjectID());
                        if (multiCashAdvanceItem.getPaymentTerms() != null && multiCashAdvanceItem.getPaymentTerms().getId() == 1 && multiCashAdvanceItem.getPaymentTermsAmount() != null) {
                            cashAdvanceItem.setPercent(multiCashAdvanceItem.getPaymentTermsAmount().doubleValue());
                        }
                        if (cashAdvanceItem.getCategoryItem() == null && multiCashAdvanceItem.getCategoryItem() != null) {
                            PaymentDeductionSelectItem paymentDeductionSelectItem = new PaymentDeductionSelectItem();
                            paymentDeductionSelectItem.setId(multiCashAdvanceItem.getCategoryItem().getId());
                            paymentDeductionSelectItem.setName(multiCashAdvanceItem.getCategoryItem().getName());
                            cashAdvanceItem.setCategoryItem(paymentDeductionSelectItem);
                        }
                    } else if (!Constants.POSTED.equals(multiCashAdvanceItem.getStatus().getCode())) {
                        cashAdvanceItem.setApprovers(multiCashAdvanceItem.getApprovers());
                        cashAdvanceItem.setApprovedDate(multiCashAdvanceItem.getApprovedDate());
                    }
                    boolean needCreate = true;
                    if (cashAdvanceItem.getObjectID() != null && Constants.DRAFT.equals(cashAdvanceItem.getStatus().getCode())) {
                        EdsCashAdvance edsCashAdvance = cashAdvanceManager.get(cashAdvanceItem.getObjectID());

                        if (edsCashAdvance.getOverallStatus() != null && !Constants.DRAFT.equals(edsCashAdvance.getOverallStatus().getCode())) {
                            needCreate = false;
                        }

                    }
                    if (fromView && cashAdvanceItem.getObjectID() != null) {
                        EdsCashAdvance edsCashAdvance = cashAdvanceManager.get(cashAdvanceItem.getObjectID());

                        if (edsCashAdvance.getOverallStatus() != null && Constants.APPROVED.equals(edsCashAdvance.getOverallStatus().getCode()) && !Constants.POSTED.equals(cashAdvanceItem.getStatus().getCode())) {
                            needCreate = false;
                        }
                    }
                    if (needCreate) {
                        futureCall.add(() -> {
                            ServerSecurityContext.getInstance().setDatabase(database);
                            ServerSecurityContext.getInstance().setCompanyId(companyId);
                            ServerSecurityContext.getInstance().setStaticUserID(userId);

                            Integer objectId = payrollAsyncService.createCashAdvance(cashAdvanceItem);
                            return objectId;
                        });
                    }
                }
            }

            ArrayList<Integer> singleCashAdvanceIds = new ArrayList<>();
            try {
                for (Future<Integer> future : executor.invokeAll(futureCall)) {
                    singleCashAdvanceIds.add(future.get());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    @Transactional
    public Integer createCashAdvance(CashAdvanceItem cashAdvanceItem) {

        if (cashAdvanceItem.getNumber() != null && cashAdvanceManager.numberExists(cashAdvanceItem.getNumber(), cashAdvanceItem.getObjectID())) {
            BankTransferNumberData newNumberData = generateCashAdvanceNumberFormat();
            cashAdvanceItem.setNumber(newNumberData.getTransferNumber());
            cashAdvanceItem.setIntNumber(Integer.parseInt(newNumberData.getFourDigitNumber()));
            cashAdvanceItem.setIntNumber(Integer.parseInt(newNumberData.getFourDigitNumber()));
        }
        EdsCashAdvance cashAdvance = cashAdvanceItem.getObjectID() != null ? cashAdvanceManager.get(cashAdvanceItem.getObjectID()) : null;
        String cashAdvanceOldStatus = null;
        if (cashAdvance != null) {
            cashAdvanceOldStatus = cashAdvance.getStatus() != null ? cashAdvance.getStatus().getCode() : null;

            Integer pdoId = paymentDeductionManager.getPaymentDeductionIdByCashAdvance(cashAdvance.getObjectID());
            if (pdoId != null) {
                checkCashAdvanceForFullyPaid(pdoId, cashAdvance.getObjectID());
            }
        } else {
            cashAdvance = new EdsCashAdvance();
        }

        if (cashAdvanceItem.getEmployee() != null) {
            cashAdvance.setEmployee(employeeManager.get(cashAdvanceItem.getEmployee().getId()));
        }

        if (cashAdvanceItem.getCategoryItem() != null) {
            cashAdvance.setCategory(categoryManager.get(cashAdvanceItem.getCategoryItem().getId()));
        }
        if (cashAdvanceItem.getObjectID() == null) {
            if (cashAdvanceItem.getCreationDate() != null) {
                cashAdvance.setCreationDate(cashAdvanceItem.getCreationDate().getNonConvertedDate());
            } else {
                cashAdvance.setCreationDate(new Date());
            }
        }
        if (cashAdvanceItem.getApprovedDate() != null) {
            cashAdvance.setApprovedDate(cashAdvanceItem.getApprovedDate().getNonConvertedDate());
        }
        if (cashAdvanceItem.getTransactionDate() != null) {
            cashAdvance.setTransactionDate(cashAdvanceItem.getTransactionDate().getNonConvertedDate());
        }
        if (cashAdvanceItem.getPaidFromAccount() != null) {
            EdsAccount account = accountingManager.get(cashAdvanceItem.getPaidFromAccount().getId());
            cashAdvance.setAccount(account);
        }
        if (cashAdvanceItem.getCashAdvanceAccount() != null) {
            EdsAccount cashAccount = accountingManager.get(cashAdvanceItem.getCashAdvanceAccount().getId());
            cashAdvance.setCashAccount(cashAccount);
        }
        if (cashAdvanceItem.getCurrency() != null) {
            cashAdvance.setCurrency(currencyManager.get(cashAdvanceItem.getCurrency().getId()));
        }
        if (cashAdvanceItem.getIntNumber() != null) {
            cashAdvance.setIntNumber(cashAdvanceItem.getIntNumber());
        }
        if (cashAdvanceItem.getNumber() != null) {
            cashAdvance.setNumber(cashAdvanceItem.getNumber());
        }
        if (cashAdvanceItem.getReference() != null) {
            cashAdvance.setReference(cashAdvanceItem.getReference());
        }
        if (cashAdvanceItem.getType() != null) {
            cashAdvance.setType(cashAdvanceItem.getType());
        }
        if (cashAdvanceItem.getTotalAmount() != null) {
            cashAdvance.setTotalAmount(cashAdvanceItem.getTotalAmount());
        }
        if (cashAdvanceItem.getTotalInBaseAmount() != null) {
            cashAdvance.setTotalInBase(cashAdvanceItem.getTotalInBaseAmount());
        }
        if (cashAdvanceItem.getExchangeRate() != null) {
            cashAdvance.setExchangeRate(cashAdvanceItem.getExchangeRate());
        }
        if (cashAdvanceItem.getDate() != null) {
            cashAdvance.setRequestDate(cashAdvanceItem.getDate().getNonConvertedDate());
        }
        cashAdvance.setLastUpdateTime(new Date());
        if (cashAdvanceItem.getPurpose() != null) {
            cashAdvance.setPurpose(cashAdvanceItem.getPurpose());
        }
        if (cashAdvanceItem.getPercent() != null) {
            cashAdvance.setPercent(cashAdvanceItem.getPercent());
        }
        if (cashAdvanceItem.getPaymentAmount() != null) {
            cashAdvance.setPaymentAmount(cashAdvanceItem.getPaymentAmount());
        }
        if (cashAdvanceItem.getPaymentMethod() != null && cashAdvanceItem.getPaymentMethod().getId() != null) {
            cashAdvance.setPaymentMethod(paymentMethodManager.get(cashAdvanceItem.getPaymentMethod().getId()));
        }
        if (cashAdvanceItem.getMultiCashAdvanceId() != null) {
            cashAdvance.setMultiCashAdvance(multiCashAdvanceManager.get(cashAdvanceItem.getMultiCashAdvanceId()));
            cashAdvance.setBasicSalary(cashAdvanceItem.getBasicSalary());
            cashAdvance.setPercentage(cashAdvanceItem.getPercentage());
        }
        cashAdvanceManager.createOrUpdate(cashAdvance);

        boolean statusChanged = cashAdvance.getOverallStatus() != null && cashAdvanceItem.getStatus() != null && cashAdvanceItem.getStatus().getCode() != null && !cashAdvanceItem.getStatus().getCode().equals(cashAdvance.getOverallStatus().getCode());

        if (cashAdvanceItem.getAttachments() != null && cashAdvanceItem.getAttachments().length > 0) {
            saveCashAdvanceAttachments(cashAdvanceItem.getAttachments(), cashAdvance);
        }

        if (isOk(cashAdvanceItem.getApprovers())) {
            if (Constants.DRAFT.equals(cashAdvanceOldStatus)) {
                // it was difficult to merge expense approvers, so just deleting old records
                approverManager.deletedAprovers(RelationItem.TYPE_CASH_ADVANCE, cashAdvance.getObjectID());
                //delete prev/current approvers
                cashAdvance.setCurrentApprover(null);
                cashAdvance.setPrevApprover(null);
            }
            EdsReference status = (cashAdvanceItem.getStatus() != null && cashAdvanceItem.getStatus().getCode() != null) ? referenceManager.getByCode(cashAdvanceItem.getStatus().getCode()) : null;
            //if cash advance action is "save & close", status will be null.
            cashAdvanceItem.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
            for (ApproverItemMini approverItem : cashAdvanceItem.getApprovers()) {
                EdsApprover _edsApprover = approverManager.get(approverItem.getClonedFrom());
                if (approverItem.getObjectID() != null && !_edsApprover.getDeleted()) {
                    if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                        EdsUser user_ = userManager.get(approverItem.getExactEmployee().getId());
                        _edsApprover.setExactEmployee(user_);
                    }
                    approverManager.update(_edsApprover);
                    if (cashAdvance.getCurrentApprover() != null && status != null) {
                        cashAdvance.getCurrentApprover().setStatus(status);
                    }
                    //force approve
                    if (cashAdvanceItem.isApproveForAll() && status != null && APPROVED.equals(status.getCode())) {
                        cashAdvance.setOverallStatus(status);
                    }
                    if (status != null && !APPROVED.equals(status.getCode())) {
                        cashAdvance.setOverallStatus(status);
                    }
                    if (cashAdvance.isCurrentApproverRejected()) {
                        cashAdvance.setOverallStatus(cashAdvance.getCurrentApprover().getStatus());
                    }
                    continue;
                }

                EdsApprover edsApprover = _edsApprover.cloneShallow();
                edsApprover.setObjectID(null);
                edsApprover.setApproverHistory(new HashSet<>());
                edsApprover.setEntityID(cashAdvance.getObjectID());
                edsApprover.setIs_default(false);
                edsApprover.setDeleted(false);
                if (status != null) {
                    edsApprover.setStatus(status);
                    cashAdvance.setOverallStatus(status);
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

                if (cashAdvance.getCurrentApprover() == null) {
                    cashAdvance.setCurrentApprover(edsApprover);
                }
                cashAdvance.getApprovers().add(edsApprover);
            }
            //update after new approvers set
            cashAdvanceManager.update(cashAdvance);
        } else {
            if (cashAdvanceItem.getStatus() != null) {
                cashAdvance.setOverallStatus(referenceManager.getByCode(cashAdvanceItem.getStatus().getCode()));
            }
        }
        cashAdvanceManager.update(cashAdvance);
        addCashAdvanceToSolr(cashAdvance);
        if (cashAdvanceItem.getObjectID() == null) {
            baseEventsPostProcessor.registerEvent(CashAdvanceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, cashAdvance, userManager.getUser());

            EdsBusinessEvent workflowRule = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, cashAdvance, userManager.getUser());
            workflowRule.setEntityType(RelationItem.TYPE_CASH_ADVANCE);
        } else if (!APPROVED.equals(cashAdvanceItem.getOverAllStatus())) {
            baseEventsPostProcessor.registerEvent(CashAdvanceEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, cashAdvance, userManager.getUser());

            EdsBusinessEvent workflowRule = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, cashAdvance, userManager.getUser());
            workflowRule.setEntityType(RelationItem.TYPE_CASH_ADVANCE);
        }
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), cashAdvance, userManager.getUser());
        workflowEvent.setEntityType(RelationItem.TYPE_CASH_ADVANCE);
        if (cashAdvanceItem.getStatus() != null && SUBMITTED_TO_MANAGER.equals(cashAdvanceItem.getStatus().getCode())) {
            EdsCurrency currency = null;
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
            if (financialSettings != null) {
                currency = financialSettings.getCurrency();
            }
            boolean hasAlerts = false;
            List<EdsWorkflowRule> rules = workflowRuleManager.getByModuleAndActions(WorkflowRule._WORKFLOW_MODULE_CASH_ADVANCE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE, WorkflowExecutionCriteriaEnum._WORKFLOW_EXECUTION_CRITERIA_CREATE_EDIT);
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
                    messageManager.sendCashAdvanceRequestToApprover(cashAdvance, currency);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (cashAdvanceItem.getStatus() != null && REJECTED.equals(cashAdvanceItem.getStatus().getCode())) {
            boolean hasAlerts = false;
            if (cashAdvance.getCurrentApprover() != null && cashAdvance.getCurrentApprover().getOnRejectedWorkflow() != null) {
                EdsWorkflowRule rule = cashAdvance.getCurrentApprover().getOnRejectedWorkflow();
                if (rule != null) {
                    hasAlerts = workflowAlertManager.hasAlertsByRoleID(rule.getObjectID());
                }
            }
            if (!hasAlerts) {
                try {
                    messageManager.sendCashAdvanceRejectMessageToEmployee(cashAdvance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (cashAdvanceItem.getStatus() != null && POSTED.equals(cashAdvanceItem.getStatus().getCode())) {
            approvedActionForCashAdvance(cashAdvance.getObjectID(), true);
        }
        if (statusChanged) {
            baseEventsPostProcessor.registerEvent(CashAdvanceEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, cashAdvance, userManager.getUser());
        }
        return cashAdvance.getObjectID();
    }

    @Override
    public Boolean deleteMultiCashAdvance(Integer objectID) {
        int nonRemovedAdvances = 0;
        List<EdsCashAdvance> cashAdvanceList = cashAdvanceManager.getListByMultiCashAdvance(objectID);
        if (!CollectionUtils.isEmpty(cashAdvanceList)) {
            for (EdsCashAdvance edsCashAdvance : cashAdvanceList) {
                if (!deleteCashAdvance(edsCashAdvance.getObjectID())) {
                    nonRemovedAdvances++;
                }
            }
        }
        if (nonRemovedAdvances == 0) {
            EdsMultiCashAdvance edsMultiCashAdvance = multiCashAdvanceManager.get(objectID);
            edsMultiCashAdvance.setDeleted(true);
            multiCashAdvanceManager.createOrUpdate(edsMultiCashAdvance);
        }
        return nonRemovedAdvances == 0;
    }

    private BankTransferNumberData generateMultiCashAdvanceNumber() {

        BankTransferNumberData transferNumberData = new BankTransferNumberData();
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer fourDigitNumber = multiCashAdvanceManager.getCashAdvanceIntNumber(); //todo multiCash Advance
        String format = null;
        if (settings != null) {
            format = settings.getMultiCashAdvanceNumberFormat();
        }
        if (format != null) {
            parseNumber(format, transferNumberData, fourDigitNumber);
        } else {
            String prefix = EdsNumberingSettings.DEF_MULTI_CASH_ADVANCE_PREFIX;
            NumberData numberData = EdsNumberingSettings.getDefaultData(fourDigitNumber, prefix);
            String[] numberParts = numberData.getNumberFormat().split("_");
            transferNumberData.setPrefix(numberParts[0]);
            transferNumberData.setFourDigitNumber(String.valueOf(numberParts[1]));
            transferNumberData.setWithDate(numberParts[1].split("-").length == 2);
        }
        return transferNumberData;
    }

    private BigDecimal getTotalAllowancesByEmployee(EdsEmployee employee, List<PaymentDeductionSelectItem> paymentCategories, Date paymentDate, BigDecimal basicSalary) {
        BigDecimal grandTotalPayment = basicSalary;
        if (grandTotalPayment == null) {
            grandTotalPayment = BigDecimal.ZERO;
        }
        List<Integer> selectedCatogoryIds = paymentCategories.stream().map(PaymentDeductionSelectItem::getId).collect(Collectors.toList());

        if (employee.getCategories() != null && employee.getCategories().size() > 0) {
            for (EdsPaymentDeduction deduction : employee.getCategories()) {
                if (paymentDate != null && (deduction.getStartDate() != null && deduction.getStartDate().after(paymentDate) || deduction.getEndDate() != null && deduction.getEndDate().before(paymentDate))) {
                    continue;
                }
                if (selectedCatogoryIds.size() > 0 && selectedCatogoryIds.contains(deduction.getCategory().getObjectID())) {
                    if ((deduction.getPayType() == null || deduction.getPayType() == 0) && deduction.getPaymentAmount() != null) {
                        grandTotalPayment = grandTotalPayment.add(deduction.getPaymentAmount());
                    } else {
                        grandTotalPayment = grandTotalPayment.add((basicSalary.multiply(deduction.getPercentage()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)));
                    }
                }
            }
        }

        return grandTotalPayment;
    }

    @Override
    public CurrencyItem getPayrollBatchCurrency(ListingFilterParameter filterParameter) {
        CurrencyItem currency = invoiceCircularResolver.getBaseCurrency();
        if (filterParameter.getObjectId() != null && !filterParameter.getObjectId().equals(0)) {
            EdsPayrollBatch payrollBatch = payrollBatchManager.get(filterParameter.getObjectId());
            if (payrollBatch != null && payrollBatch.getCurrency() != null) {
                currency = payrollBatch.getCurrency().createCurrencyItem();
            }
        }
        return currency;
    }

    public String addImportToQueue(AdditionalPayment data, ImportFile importFile) {
        //Create AdditionalPayment first
        Integer paymentId = saveAdditionalPayment(data, true);
        //Set created AdditionalPayment id to associate all rows with it
        importFile.setPaymentID(paymentId);
        //add to queue
        return importFileServiceLocal.addImportToQueue(importFile);
    }

    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployeesForPaymentDeductionSettings
            (ListingFilterParameter lfp) {
        List<EdsEmployee> employees = employeeManager.list(lfp);
        List<EdsEmployee> employeesForEdit = new ArrayList<>();
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneeList = new LinkedHashMap<>();

        if (lfp.getObjectId() != null) {
            employeesForEdit = payrollGlobalSettingsManager.get(lfp.getObjectId()).getEmployees();
            employees.removeAll(employeesForEdit);
        }

        EdsObject category;
        boolean teamOrPosition;
        KpiTreeInfo sItem;

        ArrayList<KpiTreeInfo> naList = new ArrayList<>();

        for (EdsEmployee employee : employeesForEdit) {
            teamOrPosition = false;
            sItem = new KpiTreeInfo();
            sItem.setId(employee.getObjectID());
            if (employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null) {
                sItem.setName(employee.getProfile().getEmployeeCode() + " - " + employee.getName());
                sItem.setMatchSortString(employee.getProfile().getEmployeeCode());
            } else {
                sItem.setName(employee.getName());
            }
            sItem.setSelected(true);
            if (lfp.getType() == 1) { //by Position
                category = employee.getPosition();
            }
            if (lfp.getType() == 3) { //by Position
                category = employee.getLocation();
            } else { //by Department
                category = employee.getTeam();
            }
            if (category != null) {
                sItem.setDepartmentId(category.getObjectID());
                sItem.setDepartmentName(category.getName());
                for (KpiTreeInfo s : assigneeList.keySet()) {
                    if (s.getId().equals(category.getObjectID())) {
                        teamOrPosition = true;
                        assigneeList.get(s).add(sItem);
                        break;
                    }
                }

                if (!teamOrPosition) {
                    KpiTreeInfo departmentInfo = new KpiTreeInfo(category.getObjectID(), category.getName());
                    ArrayList<KpiTreeInfo> list = new ArrayList<>();
                    list.add(sItem);
                    assigneeList.put(departmentInfo, list);
                }
            } else {
                sItem.setDepartmentId(0);
                sItem.setDepartmentName("n/a");
                naList.add(sItem);
            }
        }

        for (EdsEmployee employee : employees) {
            teamOrPosition = false;
            sItem = new KpiTreeInfo();
            sItem.setId(employee.getObjectID());
            if (employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null) {
                sItem.setName(employee.getProfile().getEmployeeCode() + " - " + employee.getName());
                sItem.setMatchSortString(employee.getProfile().getEmployeeCode());
            } else {
                sItem.setName(employee.getName());
            }
            if (lfp.getType() == 1) { //by Position
                category = employee.getPosition();
            }
            if (lfp.getType() == 3) { //by Location
                category = employee.getLocation();
            } else { //by Department
                category = employee.getTeam();
            }
            if (category != null) {
                sItem.setDepartmentId(category.getObjectID());
                sItem.setDepartmentName(category.getName());
                for (KpiTreeInfo s : assigneeList.keySet()) {
                    if (s.getId().equals(category.getObjectID())) {
                        teamOrPosition = true;
                        assigneeList.get(s).add(sItem);
                        break;
                    }
                }

                if (!teamOrPosition) {
                    KpiTreeInfo departmentInfo = new KpiTreeInfo(category.getObjectID(), category.getName());
                    ArrayList<KpiTreeInfo> list = new ArrayList<>();
                    list.add(sItem);
                    assigneeList.put(departmentInfo, list);
                }
            } else {
                sItem.setDepartmentId(0);
                sItem.setDepartmentName("n/a");
                naList.add(sItem);
            }
        }
        if (naList.size() > 0) {
            KpiTreeInfo departmentInfo = new KpiTreeInfo(0, "n/a");
            assigneeList.put(departmentInfo, naList);
        }
        return assigneeList;
    }

    @Override
    public Integer saveEmployeePayrollSettingsTemplate(NewEmployee
                                                               employee, HashMap<String, String> payrollSettings) throws
            UsernameAlreadyExistsException, EmailHostException, EmployeeCodeExistsException {
        EdsEmployeePayrollSettingsTemplate empl;
        EdsUser loggedUser = userManager.getUser();
        if (employee.getObjectID() == null && employee.getEmployeeTemplateID() == null) {
            if (employee.hasAccess() && !(employee.getEmail() == null || "".equals(employee.getEmail()))) {
                System.out.println(" -----------------  " + employee.getEmail() + "------------------");
                if (checkUserName(employee.getEmail(), loggedUser.getCompany().getObjectID()) == -1) {
                    throw new UsernameAlreadyExistsException();
                }
                if (!EmailAddressValidator.checkHost(employee.getEmail())) {
                    throw new EmailHostException();
                }
            }

            if ((employee.getEmpCode() != null && !"".equals(employee.getEmpCode())) && profileManager.isEmployeeCodeExists(employee.getEmpCode(), null)) {
                throw new EmployeeCodeExistsException();
            }
        }

        if (employee.getEmployeeTemplateID() != null) {
            empl = employeePayrollSettingsTemplateManager.get(employee.getEmployeeTemplateID());
        } else {
            empl = new EdsEmployeePayrollSettingsTemplate();
        }

        if (employee.getObjectID() != null) {
            empl.setEmployeeID(employee.getObjectID());
        }

        EdsEmployee edsEmployee = employeeManager.get(employee.getObjectID());
        if (edsEmployee != null) {
            if (employee.getPayMethod() != null) {
                EdsPaymentMethod edsPaymentMethod = paymentMethodManager.get(employee.getPayMethod().getId());
                if (edsPaymentMethod != null) {
                    edsEmployee.setPayMethod(edsPaymentMethod);
                }
            }
            edsEmployee.setPaymentMethod(employee.getPaymentMethod());
            employeeManager.update(edsEmployee);
        }

        if (employee.getEmail() != null && !"".equals(employee.getEmail())) {
            empl.setEmail(employee.getEmail());
        } else {
            String email = employee.getFname() + "." + employee.getLname() + "_test@workforcetrack.com";
            empl.setEmail(email);
        }
        empl.setFirstName(employee.getFname());
        empl.setLastName(employee.getLname());
        empl.setPaymentMethod(employee.getPaymentMethod());
        empl.setSalaryCurrency(employee.getSalaryCurrency() != null ? currencyManager.get(employee.getSalaryCurrency().getId()) : null);
        empl.setCitizenship(employee.getCitizenship() != null ? countryManager.get(employee.getCitizenship().getId()) : null);
        empl.setStartDate(employee.getStartDate() != null ? employee.getStartDate().getNonConvertedDate() : null);
        empl.setEndDate(employee.getEndDate() != null ? employee.getEndDate().getNonConvertedDate() : null);
        empl.setDobDate(employee.getBirthDate() != null ? employee.getBirthDate().getNonConvertedDate() : null);
        empl.setGender(employee.getGender());
        empl.setEmployeeCode(employee.getEmpCode());
        empl.setSender(loggedUser);
        if (employee.getApprover() != null && employee.getApprover().getId() != null) {
            EdsEmployee approver = employeeManager.get(employee.getApprover().getId());
            if (approver != null) {
                empl.setApprover(approver);
            }
        }

        if (payrollSettings != null && payrollSettings.size() > 0) {
            StringBuilder settings = new StringBuilder();
            for (Map.Entry<String, String> payrollSetting : payrollSettings.entrySet()) {
                settings.append(payrollSetting.getKey()).append(":").append(payrollSetting.getValue()).append(";");
            }
            empl.setPayrollSettings(settings.toString());
        }

        if (employee.getDeletedCategories() != null && employee.getDeletedCategories().size() > 0) {
            StringBuilder deletedCategories = new StringBuilder();
            for (Integer deletedCategoryId : employee.getDeletedCategories()) {
                deletedCategories.append(deletedCategoryId).append(";");
            }
            empl.setDeletedCategories(deletedCategories.toString());
        }

        if (employee.getInactiveCategories() != null && employee.getInactiveCategories().size() > 0) {
            StringBuilder inactiveCategories = new StringBuilder();
            for (Integer inactiveCategoryId : employee.getInactiveCategories()) {
                inactiveCategories.append(inactiveCategoryId).append(";");
            }
            empl.setInactiveCategories(inactiveCategories.toString());
        }

        empl.setStatus(referenceManager.getByCode(SUBMITTED_TO_MANAGER));

        employeePayrollSettingsTemplateManager.createOrUpdate(empl);

        HashSet<Integer> oldCategories = new HashSet<>();
        for (EdsPaymentDeduction pd : empl.getCategories()) {
            oldCategories.add(pd.getObjectID());
        }

        EdsPaymentDeduction newPaymentDeduction;
        if (employee.getPayments() != null && employee.getPayments().size() > 0) {
            for (PaymentDeductionObject paymentOrDeductionItem : employee.getPayments()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                    if (newPaymentDeduction.getEmployee() != null) {
                        newPaymentDeduction = new EdsPaymentDeduction();
                        newPaymentDeduction.setParentIdForTemplate(paymentOrDeductionItem.getId());
                    }
                    oldCategories.remove(newPaymentDeduction.getObjectID());
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem().getId());
                newPaymentDeduction.setEmployeeTemplate(empl);
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                newPaymentDeduction.setRecurring(true);
                paymentDeductionManager.createOrUpdate(newPaymentDeduction);
            }
        }

        if (employee.getDeductions() != null && employee.getDeductions().size() > 0) {
            for (PaymentDeductionObject paymentOrDeductionItem : employee.getDeductions()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                    if (newPaymentDeduction.getEmployee() != null) {
                        newPaymentDeduction = new EdsPaymentDeduction();
                        newPaymentDeduction.setParentIdForTemplate(paymentOrDeductionItem.getId());
                    }
                    paymentDeductionManager.get(paymentOrDeductionItem.getId()).getLinkedCategories().clear();
                    paymentDeductionManager.flush();
                    oldCategories.remove(newPaymentDeduction.getObjectID());
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem().getId());
                newPaymentDeduction.setEmployeeTemplate(empl);
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                newPaymentDeduction.setRecurring(true);
                newPaymentDeduction.setFromAllAllowances(paymentOrDeductionItem.isFromAllAllowances());
                newPaymentDeduction.getLinkedCategories().clear();
                paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                if (paymentOrDeductionItem.getLinkedCategories() != null && paymentOrDeductionItem.getLinkedCategories().size() > 0) {
                    EdsPayrollCategory category;
                    for (PaymentDeductionObject linkedCategory : paymentOrDeductionItem.getLinkedCategories()) {
                        category = categoryManager.get(linkedCategory.getCategoryItem().getId());
                        if (category != null) {
                            category.addPaymentDeduction(newPaymentDeduction);
                        }
                    }
                }
            }
        }

        if (employee.getTaxes() != null && employee.getTaxes().size() > 0) {
            for (PaymentDeductionObject paymentOrDeductionItem : employee.getTaxes()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                    if (newPaymentDeduction.getEmployee() != null) {
                        newPaymentDeduction = new EdsPaymentDeduction();
                        newPaymentDeduction.setParentIdForTemplate(paymentOrDeductionItem.getId());
                    }
                    paymentDeductionManager.get(paymentOrDeductionItem.getId()).getLinkedCategories().clear();
                    paymentDeductionManager.flush();
                    oldCategories.remove(newPaymentDeduction.getObjectID());
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem().getId());
                newPaymentDeduction.setEmployeeTemplate(empl);
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                newPaymentDeduction.setRecurring(true);
                newPaymentDeduction.setFromAllAllowances(paymentOrDeductionItem.isFromAllAllowances());
                newPaymentDeduction.getLinkedCategories().clear();
                paymentDeductionManager.createOrUpdate(newPaymentDeduction);
                if (paymentOrDeductionItem.getLinkedCategories() != null && paymentOrDeductionItem.getLinkedCategories().size() > 0) {
                    EdsPayrollCategory category;
                    for (PaymentDeductionObject linkedCategory : paymentOrDeductionItem.getLinkedCategories()) {
                        category = categoryManager.get(linkedCategory.getCategoryItem().getId());
                        if (category != null) {
                            category.addPaymentDeduction(newPaymentDeduction);
                        }
                    }
                }
            }
        }

        if (employee.getLoans() != null && employee.getLoans().size() > 0) {
            for (PaymentDeductionObject paymentOrDeductionItem : employee.getLoans()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                    if (newPaymentDeduction.getEmployee() != null) {
                        newPaymentDeduction = new EdsPaymentDeduction();
                        newPaymentDeduction.setParentIdForTemplate(paymentOrDeductionItem.getId());
                    }
                    oldCategories.remove(newPaymentDeduction.getObjectID());
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem().getId());
                newPaymentDeduction.setEmployeeTemplate(empl);
                if (paymentOrDeductionItem.getPercentage() != null && paymentOrDeductionItem.getPercentage().compareTo(BigDecimal.ZERO) != 0) {
                    newPaymentDeduction.setPercentage(paymentOrDeductionItem.getPercentage());
                }
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setStartDate(paymentOrDeductionItem.getStarttDate().getNonConvertedDate());
                newPaymentDeduction.setTotalAmount(paymentOrDeductionItem.getTotalAmount());
                newPaymentDeduction.setRecurring(true);
                paymentDeductionManager.createOrUpdate(newPaymentDeduction);
            }
        }

        for (Integer pdID : oldCategories) {
            paymentDeductionManager.deletePaymentOrDeduction(pdID);
        }
        paymentDeductionManager.flushAndClear();

        try {
            messageManager.sendEmployeeTemplateToApprover(empl);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return empl.getObjectID();
    }

    @Override
    public ListResult<NewEmployee> getEmployeeTemplateList(ListingFilterParameter fp) {
        NewEmployee employeeTemplate;
        ArrayList<NewEmployee> items = new ArrayList<>();
        List<EdsEmployeePayrollSettingsTemplate> employeeTemplates = employeePayrollSettingsTemplateManager.getEmployeeTemplateList(fp);
        Integer totalCount = employeePayrollSettingsTemplateManager.getEmployeeTemplateCount();
        if (employeeTemplates != null) {
            for (EdsEmployeePayrollSettingsTemplate template : employeeTemplates) {
                employeeTemplate = new NewEmployee();
                employeeTemplate.setObjectID(template.getEmployeeID());
                employeeTemplate.setEmployeeTemplateID(template.getObjectID());
                employeeTemplate.setFname(template.getFirstName());
                employeeTemplate.setLname(template.getLastName());
                employeeTemplate.setEmail(template.getEmail());
                employeeTemplate.setRejectionNote(template.getRejectionNote());
                if (template.getSender() != null) {
                    employeeTemplate.setSender(new SelectItem(template.getSender().getObjectID(), template.getSender().getFullName()));
                }
                if (template.getStatus() != null) {
                    employeeTemplate.setStatus(template.getStatus().getName());
                }
                items.add(employeeTemplate);
            }
        }
        return new ListResult<>(items, totalCount);
    }

    @Override
    public PayrollSettings getEmployeeTemplate(Integer employeeTemplateID) {
        PayrollSettings payrollSettings = new PayrollSettings();
        payrollSettings.setCurrencies(currencyService.getCurrencies(true));
        payrollSettings.setEnabledMultiCurrency("true".equals(getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL)));
        if (employeeTemplateID != null) {
            EdsEmployeePayrollSettingsTemplate template = employeePayrollSettingsTemplateManager.get(employeeTemplateID);
            if (template != null) {
                payrollSettings.setEmployeeId(template.getEmployeeID());
                payrollSettings.setEmployeeFirstName(template.getFirstName());
                payrollSettings.setEmployeeLastName(template.getLastName());
                payrollSettings.setEmployeeEmail(template.getEmail());
                payrollSettings.setStartDate(template.getStartDate() != null ? new DateNonConvertable(template.getStartDate()) : null);
                payrollSettings.setResignationDate(template.getEndDate() != null ? new DateNonConvertable(template.getEndDate()) : null);
                payrollSettings.setPayMethods(allInOneService.getPaymentMethodList());
                EdsEmployee edsEmployee = employeeManager.get(template.getEmployeeID());
                if (edsEmployee.getPayMethod() != null) {
                    payrollSettings.setPayMethod(edsEmployee.getPayMethod().getAsSelectItem());
                    payrollSettings.setPaymentMethod(edsEmployee.getPaymentMethod());
                }
                if (template.getDobDate() != null) {
                    payrollSettings.setDob(new DateNonConvertable(template.getDobDate()));
                }
                if (template.getPayrollSettings() != null && !"".equals(template.getPayrollSettings())) {
                    for (String pair : template.getPayrollSettings().split(";")) {
                        String[] keyValue = pair.split(":");
                        payrollSettings.getPayrollSettings().put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
                    }
                }
                if (payrollSettings.getPayrollSettings().containsKey(SALARY_CATEGORY) && !"".equals(payrollSettings.getPayrollSettings().get(SALARY_CATEGORY))) {
                    EdsPayrollCategory salaryCategory = categoryManager.get(Integer.parseInt(payrollSettings.getPayrollSettings().get(SALARY_CATEGORY)));
                    if (salaryCategory != null) {
                        payrollSettings.setSalaryCategory(salaryCategory.createPaymentDeductionSelectItem());
                    }
                } else {
                    EdsPayrollCategory salaryCategory = categoryManager.getCategoryByCode(BASIC_SALARY);
                    if (salaryCategory != null) {
                        payrollSettings.setSalaryCategory(salaryCategory.createPaymentDeductionSelectItem());
                    }
                }
                if (template.getSalaryCurrency() != null) {
                    payrollSettings.setSalaryCurrency(template.getSalaryCurrency().createCurrencyItem());
                }
                EdsCountry citizenship = template.getCitizenship();
                if (citizenship != null) {
                    payrollSettings.setCitizenship(citizenship.getAsSelectItem());
                }
                List<EdsPaymentDeduction> categories = template.getCategories();
                if (categories != null && categories.size() > 0) {
                    PaymentDeductionObject object;
                    for (EdsPaymentDeduction category : categories) {
                        object = category.getRPC();
                        object.setUsed(payslipPaymentsManager.checkPaymentDeductionForUsed(category.getObjectID()));
                        if (category.getLinkedCategories() != null && category.getLinkedCategories().size() > 0) {
                            PaymentDeductionObject linkedObject;
                            for (EdsPayrollCategory linkedCategory : category.getLinkedCategories()) {
                                linkedObject = new PaymentDeductionObject();
                                linkedObject.setCategoryItem(linkedCategory.createPaymentDeductionSelectItem());
                                object.getLinkedCategories().add(linkedObject);
                            }
                        }
                        if (object.isPaymentCategory()) {
                            payrollSettings.getPaymentCategories().add(object);
                        } else if (object.isLoan()) {
                            if (!category.isFullPayed()) {
                                object.setRemainingAmount(category.getRemainingAmount());
                                payrollSettings.getLoanCategories().add(object);
                            }
                        } else if (object.isDeductionCategory()) {
                            payrollSettings.getDeductionCategories().add(object);
                        } else if (object.isTaxCategory()) {
                            payrollSettings.getTaxCategories().add(object);
                        } else if (object.isEmployerContributionCategory()) {
                            payrollSettings.getEmployerContributions().add(object);
                        }
                    }
                }
            }
        }
        payrollSettings.setCountries(commonService.getCountries());
        return payrollSettings;
    }

    private NewEmployee getNewEmployeeDataFromTemplate(EdsEmployeePayrollSettingsTemplate template) {
        NewEmployee newEmployee = new NewEmployee();
        newEmployee.setFname(template.getFirstName());
        newEmployee.setLname(template.getLastName());
        newEmployee.setEmail(template.getEmail());
        newEmployee.setStartDate(template.getStartDate() != null ? new DateNonConvertable(template.getStartDate()) : null);
        newEmployee.setResignationDate(template.getEndDate() != null ? new DateNonConvertable(template.getEndDate()) : null);
        newEmployee.setEmployeeTemplateID(template.getObjectID());
        newEmployee.setBirthDate(template.getDobDate() != null ? new DateNonConvertable(template.getDobDate()) : null);

        List<EdsPaymentDeduction> categories = template.getCategories();
        if (categories != null && categories.size() > 0) {
            PaymentDeductionObject object;
            for (EdsPaymentDeduction category : categories) {
                object = category.getRPC();
                object.setUsed(payslipPaymentsManager.checkPaymentDeductionForUsed(category.getObjectID()));
                if (category.getLinkedCategories() != null && category.getLinkedCategories().size() > 0) {
                    PaymentDeductionObject linkedObject;
                    for (EdsPayrollCategory linkedCategory : category.getLinkedCategories()) {
                        linkedObject = new PaymentDeductionObject();
                        linkedObject.setCategoryItem(linkedCategory.createPaymentDeductionSelectItem());
                        object.getLinkedCategories().add(linkedObject);
                    }
                }
                if (object.isPaymentCategory()) {
                    newEmployee.getPayments().add(object);
                } else if (object.isLoan()) {
                    if (!category.isFullPayed()) {
                        object.setRemainingAmount(category.getRemainingAmount());
                        newEmployee.getLoans().add(object);
                    }
                } else {
                    newEmployee.getDeductions().add(object);
                }
            }
        }
        return newEmployee;
    }

    @Override
    public void updateEmployeeTemplateStatus(Integer employeeTemplateID, String status, String rejectionNote) {
        if (employeeTemplateID == null) {
            return;
        }
        final EdsEmployeePayrollSettingsTemplate template = employeePayrollSettingsTemplateManager.get(employeeTemplateID);

        if (template == null) {
            return;
        }
        template.setStatus(referenceManager.getByCode(status));
        if (status.equals(APPROVED)) {
            this.saveEmployeeSettingsFromEmployeeTemplateData(template);
        } else {
            template.setRejectionNote(rejectionNote);
        }
    }

    @Override
    public void deleteEmployeeTemplate(Integer employeeTemplateID) {
        final EdsEmployeePayrollSettingsTemplate template = employeePayrollSettingsTemplateManager.get(employeeTemplateID);

        if (template != null) {
            template.setDeleted(true);
        }
    }

    @Override
    public Date getFinancialYearDate() {
        final EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();

        if (fs != null) {
            return fs.getFinancialYearEnd();
        }
        return null;
    }

    @Override
    public CashAdvanceReportData getCashAdvanceReportData(ListingFilterParameter lfp) {
        final ArrayList<CashAdvanceReportItem> resultList = Lists.newArrayListWithCapacity(lfp.getLimit());
        final Integer count = cashAdvanceManager.getCashAdvanceReportItemsCount(lfp);

        if (count > 0) {
            resultList.addAll(cashAdvanceManager.getCashAdvanceReportItems(lfp));
        }
        return new CashAdvanceReportData(resultList, count);
    }

    @Override
    public SalaryReportData getSalarReportData(ListingFilterParameter lfp) {
        final ArrayList<SalaryReportItem> resultList = Lists.newArrayListWithExpectedSize(lfp.getLimit());
        final List<SalaryReportItem> list = payslipTableItemManager.getSalaryReportItems(lfp);

        if (lfp.getLimit() > 0) {
            resultList.addAll(ListUtils.getSublist(list, lfp.getStart(), lfp.getLimit()));
        }
        return new SalaryReportData(resultList, list.size());
    }

    private void saveEmployeeSettingsFromEmployeeTemplateData(EdsEmployeePayrollSettingsTemplate template) {
        EdsEmployee employee;
        Integer employeeID;
        if (template == null) {
            return;
        }

        if (template.getEmployeeID() != null) {
            employee = employeeManager.get(template.getEmployeeID());
            employeeID = template.getEmployeeID();
        } else {
            employeeID = employeeService.createEmployee(getNewEmployeeDataFromTemplate(template), false);
            employee = employeeManager.get(employeeID);
        }
        if (template.getDobDate() != null && employee.getProfile() != null && employee.getProfile().getContact() != null) {
            employee.getProfile().getContact().setDateOfBirth(template.getDobDate());
            crmContactManager.update(employee.getProfile().getContact(), true);
        }
        if (template.getStartDate() != null) {
            employee.setStartDate(template.getStartDate());
        }
        if (template.getEmail() != null) {
            employee.setEmail(template.getEmail());
        }
        if (template.getFirstName() != null) {
            employee.setFirstName(template.getFirstName());
        }
        if (template.getLastName() != null) {
            employee.setLastName(template.getLastName());
        }
        employee.setEndDate(template.getEndDate());
        if (template.getEmployeeCode() != null && employee.getProfile() != null) {
            employee.getProfile().setEmployeeCode(template.getEmployeeCode());
        }
        final Set<Integer> oldCategories = employee.getCategories()
                .stream()
                .map(EdsPaymentDeduction::getObjectID)
                .collect(Collectors.toSet());

        for (EdsPaymentDeduction category : template.getCategories()) {
            if (category.getParentIdForTemplate() != null) {
                final EdsPaymentDeduction employeeCategory = paymentDeductionManager.get(category.getParentIdForTemplate());

                if (employeeCategory == null) {
                    continue;
                }
                if (category.getCategory() != null) {
                    employeeCategory.setCategoryId(category.getCategory().getObjectID());
                }
                employeeCategory.setPaymentAmount(category.getPaymentAmount());
                employeeCategory.setPayType(category.getPayType());
                employeeCategory.setPercentage(category.getPercentage());
                employeeCategory.setFromAllAllowances(category.isFromAllAllowances());
                oldCategories.remove(employeeCategory.getObjectID());
                continue;
            }
            final EdsPaymentDeduction newPaymentDeduction = new EdsPaymentDeduction();

            newPaymentDeduction.setCategoryId(category.getCategory().getObjectID());
            newPaymentDeduction.setEmployeeId(employeeID);
            newPaymentDeduction.setPaymentAmount(category.getPaymentAmount());
            newPaymentDeduction.setPaymentDate(category.getPaymentDate());
            newPaymentDeduction.setPayType(category.getPayType());
            newPaymentDeduction.setPercentage(category.getPercentage());
            newPaymentDeduction.setRecurring(true);
            newPaymentDeduction.setFromAllAllowances(category.isFromAllAllowances());
            paymentDeductionManager.createOrUpdate(newPaymentDeduction);
        }
        final Map<String, String> payrollSettings = Maps.newHashMap();

        for (String pair : Optional.ofNullable(template.getPayrollSettings()).orElse("").split(";")) {
            final String[] keyValue = pair.split(":");

            payrollSettings.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : "");
        }
        for (Map.Entry<String, String> payrollSetting : payrollSettings.entrySet()) {
            if (payrollSetting.getValue() == null) {
                continue;
            }

            EdsEmployeePayrollSettings eps = employeePayrollSettingsManager.getEmployeeSettingValue(employeeID, payrollSetting.getKey());

            if (eps != null && !payrollSetting.getValue().equals(eps.getValue())) {
                if (NI_TABLE_LETTER.equals(eps.getKey())) {
                    createHistory(EmployeePayrollSettingsHistory.NICATEGORY_CHANGED, EmployeePayrollSettingsHistory.USER_MODIFIED, payrollSetting.getValue(), eps);
                } else if (TAX_CODE.equals(eps.getKey())) {
                    createHistory(EmployeePayrollSettingsHistory.TAXCODE_CHANGED, EmployeePayrollSettingsHistory.USER_MODIFIED, payrollSetting.getValue(), eps);
                }
            } else if (eps == null) {
                if (NI_TABLE_LETTER.equals(payrollSetting.getKey())) {
                    createHistory(EmployeePayrollSettingsHistory.NICATEGORY_CHANGED, EmployeePayrollSettingsHistory.SYSTEM_CREATED, payrollSetting.getValue(), eps);
                } else if (TAX_CODE.equals(payrollSetting.getKey())) {
                    createHistory(EmployeePayrollSettingsHistory.TAXCODE_CHANGED, EmployeePayrollSettingsHistory.SYSTEM_CREATED, payrollSetting.getValue(), eps);
                }
            }
            if (eps == null) {
                eps = new EdsEmployeePayrollSettings();
            }
            eps.setEmployeeId(employee.getObjectID());
            eps.setKey(payrollSetting.getKey());
            eps.setValue(payrollSetting.getValue());
            employeePayrollSettingsManager.createOrUpdate(eps);
        }
        for (Integer categoryID : oldCategories) {
            paymentDeductionManager.deletePaymentOrDeduction(categoryID);
        }
    }

    private int checkUserName(String userName, Integer companyID) {
        try {
            // usernames by default all lowercased
            if (userManager.findUser(userName.toLowerCase(), companyID) != null) {
                return -1;
            }
        } catch (Exception ignored) {
        }
        return 0;
    }

    @Override
    public void savePayrollBatch(PayrollBatchData data) {
        EdsPayrollBatch payrollBatch = null;

        if (data.getObjectID() != null) {
            payrollBatch = payrollBatchManager.get(data.getObjectID());
        }
        if (payrollBatch == null) {
            payrollBatch = new EdsPayrollBatch();
        }
        payrollBatch.setName(data.getName());
        payrollBatch.setDescription(data.getDescription());
        payrollBatch.setType(data.getType());
        payrollBatch.setCurrency(data.getCurrency() != null ? currencyManager.get(data.getCurrency().getId()) : null);
        if (data.getClient() != null) {
            EdsCrmAccount client = clientManager.get(data.getClient().getId());
            payrollBatch.setClient(client);
        }
        if (data.getProject() != null) {
            EdsProject project = projectManager.get(data.getProject().getId());
            payrollBatch.setProject(project);
        }
        this.payrollBatchManager.createOrUpdate(payrollBatch);

        if (data.getSelectedEmployeeIds() != null) {
            this.saveGroupEmployees(data.getSelectedEmployeeIds(), payrollBatch.getObjectID(), true);
        }
        payrollBatch.getManagers().clear();
        if (data.getManagers() != null) {
            for (SelectItem manager : data.getManagers()) {
                payrollBatch.getManagers().add(employeeManager.get(manager.getId()));
            }
        }
    }

    @Override
    public void deletePayrollBatch(Integer payrollBatchId) {
        payrollBatchManager.removeEmployeesReferencebyBatch(payrollBatchId);
        final EdsPayrollBatch payrollBatch = payrollBatchManager.get(payrollBatchId);

        if (payrollBatch != null) {
            payrollBatch.setDeleted(true);
            payrollBatchManager.update(payrollBatch);
        }
    }

    @Override
    public PayrollBatchData getPayrollBatchData(Integer objectID) {
        PayrollBatchData result = null;
        EdsPayrollBatch payrollBatch = payrollBatchManager.get(objectID);
        if (payrollBatch != null) {
            result = new PayrollBatchData();
            result.setName(payrollBatch.getName());
            result.setDescription(payrollBatch.getDescription());
            result.setType(payrollBatch.getType());
            result.setCurrencies(currencyService.getCurrencies(true));
            result.setCurrency(payrollBatch.getCurrency() != null ? payrollBatch.getCurrency().createCurrencyItem() : null);

            if (payrollBatch.getClient() != null) {
                result.setClient(payrollBatch.getClient().getAsSelectItem());
            }
            if (payrollBatch.getProject() != null) {
                result.setProject(payrollBatch.getProject().getAsSelectItem());
            }

            if (payrollBatch.getManagers() != null && !payrollBatch.getManagers().isEmpty()) {
                ArrayList<SelectItem> managers = new ArrayList<>();
                for (EdsEmployee manager : payrollBatch.getManagers()) {
                    managers.add(new SelectItem(manager.getObjectID(), manager.getFullName()));
                }
                result.setManagers(managers.toArray(new SelectItem[]{}));
            }
        }
        String enabledMultiCurrency = getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL);
        result.setEnabledMultiCurrency("true".equals(enabledMultiCurrency));
        return result;
    }

    @Override
    public ListResult<PayrollBatchData> getPayrollBatches(final ListingFilterParameter lfp) {
        final Integer totalCount = payrollBatchManager.getTotalCount();
        final List<PayrollBatchData> list = Lists.newArrayListWithExpectedSize(lfp.getLimit());

        if (totalCount > 0) {
            final List<EdsPayrollBatch> items = payrollBatchManager.getPayrollBatchList(lfp);
            final Map<Integer, Integer> batchemps = payrollBatchManager.getPayrollBatchEmployeeAmount();

            for (EdsPayrollBatch batch : items) {
                final PayrollBatchData item = new PayrollBatchData();

                item.setObjectID(batch.getObjectID());
                item.setName(batch.getName());
                item.setDescription(batch.getDescription());
                item.setType(batch.getType());
                item.setCurrency(batch.getCurrency() != null ? batch.getCurrency().createCurrencyItem() : null);
                if (batchemps.containsKey(batch.getObjectID())) {
                    item.setEmployeesAmount(batchemps.get(batch.getObjectID()));
                }
                if (batch.getManagers() != null && !batch.getManagers().isEmpty()) {
                    ArrayList<SelectItem> managers = new ArrayList<>();
                    for (EdsEmployee manager : batch.getManagers()) {
                        managers.add(new SelectItem(manager.getObjectID(), manager.getFullName()));
                    }
                    item.setManagers(managers.toArray(new SelectItem[]{}));
                }
                list.add(item);
            }
            if ("assignedEmployees".equals(lfp.getSortField())) {
                list.sort(lfp.isAscending()
                        ? Comparator.comparingInt(PayrollBatchData::getEmployeesAmount)
                        : Comparator.comparingInt(PayrollBatchData::getEmployeesAmount).reversed());
            }
        }
        return new ListResult<>(Lists.newArrayList(list), totalCount);
    }

    @Override
    public ArrayList<SelectItem> getPayrollBatchesForLookUp(ListingFilterParameter lfp) {
        ArrayList<SelectItem> itemList = payrollBatchManager.getPayrollBatchesForLookUp(lfp);
        EdsUser edsUser = userManager.getUser();
        if ((roleManager.hasRole(edsUser, EdsRole.DR) ||
                roleManager.hasRole(edsUser, EdsRole.ADMIN) ||
                roleManager.hasRole(edsUser, EdsRole.HR) ||
                ServerUtils.hasPermission(PermissionConstants.PAYROLL_GROUP_FULL_ACCESS))) {
            itemList.add(new SelectItem(0, commonLocalizer.localize("allEmployees", "All Employees")));
        }
        return itemList;
    }

    @Override
    public SelectItem[] getDriversForLookUp(ListingFilterParameter filterParameter) {
        return employeeManager.getDriverListAsSelectItems(filterParameter).toArray(new SelectItem[]{});
    }

    private Map<Date, LeaveBalanceCalculationItem> generateLRDays(EdsSickRequest sickRequest,
                                                                  Date periodEnd,
                                                                  Map<Date, LeaveBalanceCalculationItem> map,
                                                                  boolean isTakeMoney) {
        Date startDate = (Date) sickRequest.getStartDate().clone();
        Date endDate = (Date) sickRequest.getEndDate().clone();
        Date date2 = DateUtil.getDayLastTime(periodEnd);
        Date currentMonth = DateUtil.getMonthFirstDay(startDate);

        LeaveBalanceCalculationItem calItem;
        while (DateUtil.compare(endDate, date2)) {
            if (map.get(currentMonth) != null) {
                calItem = map.get(currentMonth);
            } else {
                calItem = new LeaveBalanceCalculationItem();
            }
            sickRequest.setStartDate(startDate);
            sickRequest.setEndDate(DateUtil.getDayLastTime(date2));

            if (isTakeMoney) {
                calItem.setLrTakeMoneyDays(calItem.getLrTakeMoneyDays() + countLeaveRequestDays(sickRequest));
            } else {
                calItem.setLrDays(calItem.getLrDays() + countLeaveRequestDays(sickRequest));
            }

            calItem.setLrHours(calItem.getLrHours() + countLeaveRequestDays(sickRequest, true));
            map.put(currentMonth, calItem);

            if (DateUtil.compare(DateUtil.getMonthLastDate(date2), date2)) {
                startDate = DateUtil.addTime(DateUtil.getDayLastTime(date2), 0, 0, 1);
                date2 = DateUtil.getMonthLastDate(date2);
                currentMonth = startDate;
                continue;
            }

            startDate = DateUtil.getMonthFirstDay(DateUtil.addMonths(startDate, 1));
            date2 = DateUtil.getMonthLastDate(startDate);
            currentMonth = startDate;
        }

        if (map.get(currentMonth) != null) {
            calItem = map.get(currentMonth);
        } else {
            calItem = new LeaveBalanceCalculationItem();
        }
//        calItem.setLrTakeFromMoney(isTakeMoney);
        sickRequest.setStartDate(startDate);
        sickRequest.setEndDate(endDate);

        if (isTakeMoney) {
            calItem.setLrTakeMoneyDays(calItem.getLrTakeMoneyDays() + countLeaveRequestDays(sickRequest));
//            calItem.setLrDays(calItem.getLrTakeMoneyDays());
        } else {
            calItem.setLrDays(calItem.getLrDays() + countLeaveRequestDays(sickRequest));
        }
        calItem.setLrHours(calItem.getLrHours() + countLeaveRequestDays(sickRequest, true));
        map.put(currentMonth, calItem);

        return map.entrySet().stream()
                .filter(entry -> entry.getKey().equals(DateUtil.getMonthFirstDay(periodEnd)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void setDailyRateToMap(Map<Date, LeaveBalanceCalculationItem> map, BigDecimal dailyRate, boolean...
            isDailyType) {
        if (map == null) {
            return;
        }
        for (LeaveBalanceCalculationItem item : map.values()) {
            if (isDailyType.length > 0 && isDailyType[0]) {
                item.setDailyRate(dailyRate);
            } else if (isDailyType.length > 0) {
                item.setMoneyRate(dailyRate);
            } else {
                item.setDailyRate(dailyRate);
                item.setMoneyRate(dailyRate);
            }
        }
    }

    private BigDecimal getAllLRAmount(Map<Date, LeaveBalanceCalculationItem> map, boolean... isDailyType) {
        BigDecimal amountByDay = BigDecimal.ZERO;
        BigDecimal amountByMoney = BigDecimal.ZERO;

        for (LeaveBalanceCalculationItem calItem : map.values()) {
            amountByDay = amountByDay.add(calItem.getDailyRate().multiply(BigDecimal.valueOf(calItem.getLrDays())));
            amountByMoney = amountByMoney.add(calItem.getMoneyRate().multiply(BigDecimal.valueOf(calItem.getLrTakeMoneyDays())));
        }

        if (isDailyType.length > 0 && isDailyType[0]) {
            return amountByDay;
        } else if (isDailyType.length > 0) {
            return amountByMoney;
        } else {
            return amountByDay.add(amountByMoney);
        }
    }

    private void initializeDailyRateByMonth(Map<Date, LeaveBalanceCalculationItem> map, BigDecimal basicSalary,
                                            boolean... isDailyType) {
        if (map == null) {
            return;
        }
        for (Date key : map.keySet()) {
            LeaveBalanceCalculationItem calItem = map.get(key);

            BigDecimal dailyRate = basicSalary.divide(BigDecimal.valueOf(DateUtil.getDateInMonth(key.getYear(),
                            key.getMonth())),
                    ServerUtils.getSystemCalculationScale(),
                    RoundingMode.HALF_UP);
            if (isDailyType.length > 0 && isDailyType[0]) {
                calItem.setDailyRate(dailyRate);
            } else if (isDailyType.length > 0) {
                calItem.setMoneyRate(dailyRate);
            } else {
                calItem.setDailyRate(dailyRate);
                calItem.setMoneyRate(dailyRate);
            }
        }
    }

    private void initializeDailyRateByMonth(Map<Date, LeaveBalanceCalculationItem> map, BigDecimal
            basicSalary, BigDecimal workedDays, boolean... isDailyType) {
        if (map == null) {
            return;
        }
        for (Date key : map.keySet()) {
            LeaveBalanceCalculationItem calItem = map.get(key);

            BigDecimal dailyRate = basicSalary.divide(workedDays != null ? workedDays : BigDecimal.valueOf(DateUtil.getDateInMonth(key.getYear(),
                            key.getMonth())),
                    ServerUtils.getSystemCalculationScale(),
                    RoundingMode.HALF_UP);
            if (isDailyType.length > 0 && isDailyType[0]) {
                calItem.setDailyRate(dailyRate);
            } else if (isDailyType.length > 0) {
                calItem.setMoneyRate(dailyRate);
            } else {
                calItem.setDailyRate(dailyRate);
                calItem.setMoneyRate(dailyRate);
            }
        }
    }

    @Override
    @Transactional
    public TestRPC saveCashAdvancePayment(CashAdvancePayment payment) {
        final TestRPC result = new TestRPC();
        final Date date = new Date();
        final EdsPayslipPayments pp = new EdsPayslipPayments();

        pp.setPaymentDeductionID(this.paymentDeductionManager.getPaymentDeductionIdByCashAdvance(payment.getCashadvanceId()));
        pp.setPaymentTotal(payment.getPaymentAmount());
        pp.setExchangeRate(payment.getExchangeRate());
        pp.setAccount(this.accountingManager.get(payment.getAccountId()));
        pp.setReference(payment.getReference());
        pp.setPaymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate().getDate() : null);
        pp.setCreationDate(date);
        pp.setLasUpdated(date);
        final EdsCashAdvance cashAdvance = cashAdvanceManager.get(payment.getCashadvanceId());

        pp.setCashAdvance(cashAdvance);
        pp.setCurrency(cashAdvance.getCurrency());
        pp.setUser(this.payslipPaymentsManager.getUser());
        this.payslipPaymentsManager.create(pp);
        cashAdvance.getPayments().add(pp);
        this.createCashAdvancePaymentTransaction(pp.getObjectID());
        this.checkCashAdvanceForFullyPaid(pp.getPaymentDeductionID(), cashAdvance.getObjectID());

        result.setId(pp.getObjectID());
        result.setRemainingAmount(cashAdvance.getTotalAmount().subtract(payment.getPaymentAmount()));
        return result;
    }

    private void createCashAdvancePaymentTransaction(Integer payslipPaymentId) {
        final boolean isEnabledMultiCurrency = "true".equals(getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL));
        final EdsPayslipPayments pp = payslipPaymentsManager.get(payslipPaymentId);
        final EdsCashAdvance cashAdvance = pp.getCashAdvance();
        final BigDecimal exchangeRate = Optional.ofNullable(pp.getExchangeRate()).orElse(BigDecimal.ONE);
        final EdsUser user = transactionManager.getUser();
        final EdsCashAdvancePayTransaction capTrans = new EdsCashAdvancePayTransaction();

        capTrans.setCashAdvancePayment(pp);
        capTrans.setJournalId(transactionManager.getCompanyLastTransactionOrderID(user.getCompany()) + 1);
        capTrans.setJournalDate(pp.getPaymentDate());
        capTrans.setPostedDate(user.getUserDate());
        capTrans.setName("Cash Advance Payment Transaction: " + cashAdvance.getNumber() + "(" + cashAdvance.getObjectID() + ")");
        EdsTransactionItem captiDebit = new EdsTransactionItem();
        captiDebit.setAccount(pp.getAccount());
        captiDebit.setDebit(pp.getPaymentTotal().divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        if (isEnabledMultiCurrency) {
            captiDebit.setForeignDebit(pp.getPaymentTotal());
        }
        capTrans.addTransactionItem(captiDebit);
        final EdsTransactionItem captiCredit = new EdsTransactionItem();

        captiCredit.setAccount(cashAdvance.getCashAccount());
        captiCredit.setCredit(pp.getPaymentTotal().divide(exchangeRate, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        if (isEnabledMultiCurrency) {
            captiCredit.setForeignCredit(pp.getPaymentTotal());
        }
        capTrans.addTransactionItem(captiCredit);
        this.transactionManager.create(capTrans);
    }

    @Override
    public TestRPC deleteCashAdvancePayment(Integer cashAdvanceId, Integer paymentId) {
        final TestRPC result = new TestRPC();
        final EdsCashAdvancePayTransaction transaction = transactionManager.getCashAdvancePayTransactionByPayslipPayment(payslipPaymentsManager.get(paymentId));

        if (transaction != null) {
            transaction.setCashAdvancePayment(null);
            transaction.setDeleted(true);
            transactionManager.update(transaction);
        }
        final EdsPayslipPayments epp = payslipPaymentsManager.get(paymentId);

        if (epp != null) {
            payslipPaymentsManager.delete(epp);
        }
        this.checkCashAdvanceForFullyPaid(paymentDeductionManager.getPaymentDeductionIdByCashAdvance(cashAdvanceId), cashAdvanceId);

        this.addCashAdvanceToSolr(cashAdvanceManager.get(cashAdvanceId));
        return result;
    }

    @Override
    public HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> getEmployeesMap(ListingFilterParameter fp, String formType) {
        return reportService.getEmployeesMap(fp, formType);
    }

    @Override
    public void saveGroupEmployees(HashSet<Integer> members, Integer objectID, boolean isChecked) {
        if (isChecked) {
            payrollBatchManager.addEmployeePayrolBatch(objectID, members);
        } else {
            payrollBatchManager.removeEmployeesFromGroup(objectID, members);
        }
        List<EdsEmployee> edsEmployees = employeeManager.getEmployeesByIds(ServerUtils.getAsCommoDelimited(new ArrayList<>(members), "", ","));
        try {
            employeeSolrComponent.indexes(edsEmployees);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public TestRPC saveSickLeaveSettings(SickLeaveSettings sickLeaveSettings) {
        TestRPC result = new TestRPC();
        EdsSickLeaveSettings edsSLSettings = sickLeaveSettingsManager.getOne();
        if (edsSLSettings == null) {
            edsSLSettings = new EdsSickLeaveSettings();
        }
        edsSLSettings.setFullyPaidLeaveDays(sickLeaveSettings.getFullyPaidLeaveDays());
        edsSLSettings.setHalfPaidLeaveDays(sickLeaveSettings.getHalfPaidLeaveDays());
        edsSLSettings.setUnPaidLeaveDays(sickLeaveSettings.getUnPaidLeaveDays());
        edsSLSettings.setMinPeriodOfService(sickLeaveSettings.getMinPeriodOfService());
        edsSLSettings.setSickLeaveCategory(categoryManager.get(sickLeaveSettings.getSickLeaveCategory().getId()));
        sickLeaveSettingsManager.createOrUpdate(edsSLSettings);

        categoryManager.deleteSickeLeaveSettingsRefernce();
        EdsPayrollCategory category;
        if (sickLeaveSettings.getAllowances().size() > 0) {
            for (PaymentDeductionSelectItem item : sickLeaveSettings.getAllowances()) {
                category = categoryManager.get(item.getId());
                if (category != null) {
                    category.setSickLeaveSettings(edsSLSettings);
                }
            }
        }
        result.setId(edsSLSettings.getObjectID());
        return result;
    }

    @Override
    public SickLeaveSettings getSickLeaveSettings() {
        final EdsSickLeaveSettings edsls = sickLeaveSettingsManager.getOne();

        if (edsls == null) {
            return null;
        }
        return edsls.getRPC();
    }

    @Override
    public TestRPC saveDailyRateSettings(DailyRateSettings settings) {
        TestRPC result = new TestRPC();
        EdsDailyRateSettings edrs = dailyRateSettingsManager.getOne();
        if (edrs == null) {
            edrs = new EdsDailyRateSettings();
        }

        edrs.setDailyRateType(settings.getDailyRateType());
        if (DailyRateSettings.TYPE_FORMULA.equals(settings.getDailyRateType())) {
            edrs.setExcludeDayOffs(settings.isExcludeDayOffs());
            edrs.setExcludeHoliday(settings.isExcludeHoliday());
            edrs.setWorkDaysInMonth(null);
        } else if (DailyRateSettings.TYPE_EMPLOYER_SETTINGS.equals(settings.getDailyRateType())) {
            edrs.setWorkDaysInMonth(settings.getWorkDaysInMonth());
            edrs.setExcludeDayOffs(false);
            edrs.setExcludeHoliday(false);
        } else if (DailyRateSettings.TYPE_CALENDAR.equals(settings.getDailyRateType())) {
            edrs.setExcludeDayOffs(false);
            edrs.setExcludeHoliday(false);
            edrs.setWorkDaysInMonth(null);
        }
        dailyRateSettingsManager.createOrUpdate(edrs);
        return result;
    }

    public DailyRateSettings getDailyRateSettings() {
        EdsDailyRateSettings edrs = dailyRateSettingsManager.getOne();
        if (edrs == null) {
            edrs = new EdsDailyRateSettings();
        }
        return edrs.getRPC();
    }

    private int getMonthsDifference(Date startDate, Date endDate) {
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(DateUtil.resetTime(startDate));
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(DateUtil.getDayLastTime(endDate));

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        return diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
    }

    @Override
    public PayrolTableItemListResult getPayslipItems(PayslipFilter filter) {
        PayrolTableItemListResult result = new PayrolTableItemListResult();
        result.setTotal(0);
        result.setList(new ArrayList<>());
        if (filter == null || filter.getFromDate() == null || filter.getToDate() == null || (filter.getPayrollBatchID() == null && filter.getProjectId() == null && filter.getLocationId() == null)) {
            return result;
        }
        if (filter.isEnabledMultiCurrency()) {
            EdsCurrency currency = financialSettingsManager.getFinancialSettings().getCurrency();

            if (filter.getPayrollBatchID() != null) {
                EdsPayrollBatch payrollBatch = payrollBatchManager.get(filter.getPayrollBatchID());

                if (payrollBatch != null && payrollBatch.getCurrency() != null) {
                    currency = payrollBatch.getCurrency();
                }
            }
            result.setCurrency(currency.createCurrencyItem());
        }
        Integer itemCount = payslipTableManager.getEmployeeDataForGroupPayrunCount(filter);

        result.setTotal(itemCount);
        if (itemCount <= 0) {
            return result;
        }
        List<SinglePayrunItem> singlePayrunItems = Lists.newArrayListWithExpectedSize(filter.getLimit(itemCount));
        ArrayList<Integer> employeeIds = new ArrayList<>(payslipTableManager.getEmployeeListDataForGroupPayrun(filter));

        if (employeeIds.isEmpty()) {
            return result;
        }

        EdsCurrency baseCurrency = financialSettingsManager.getFinancialSettings().getCurrency();
        boolean isEmployeeCodeInteger = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER);
        Map<String, PaymentDeductionSelectItem> categoryMap = categoryManager.getCategoryItemMapByCodes(LEAVE_DEDUCTIONS,
                LEAVE_ENCHASHMENT,
                BENEFIT_PAYMENT,
                EXPENSE_REPORT,
                REGULAR_OVERTIME,
                WEEKEND_OVERTIME,
                HOLIDAY_OVERTIME,
                ADDITIONAL_PAYMENT,
                ABSENCE_DEDUCTIONS,
                BONUS);

        Map<String, String> settingsMap = companyPayrollSettingsManager.getCompanyPayrollSettingsMap(NON_PAID_LEAVE_DAYS_IMPACT,
                LEAVE_DAYS_IMPACT,
                DAILY_RATE_BY_EMPLOYER_SETTINGS,
                ENABLED_LEAVE_DEDUCTIONS,
                ENABLED_LEAVE_PAYMENTS,
                NUMBER_OF_WORK_DAYS,
                LEAVE_MONEY_TYPE_CATEGORY,
                DEDUCT_TYPE,
                DEDUCT_ALLOWANCES,
                LEAVE_DAILY_PAYMENT_TYPE,
                LEAVE_DAILY_ALLOWANCES,
                LEAVE_MONEY_PAYMENT_TYPE,
                LEAVE_MONEY_ALLOWANCES);
        EdsPayrollCategory leaveMTCategory = categoryManager.getCategoryByCode(LEAVE_SALARY);
        PaymentDeductionSelectItem leaveMTCategoryItem = leaveMTCategory != null ? leaveMTCategory.createPaymentDeductionSelectItem() : null;

        List<PaymentDeductionObject> leaveDeductionLinkedCategories = loadLeaveSettings(settingsMap.get(DEDUCT_ALLOWANCES));
        List<PaymentDeductionObject> leaveDailyTypeLinkedCategories = loadLeaveSettings(settingsMap.get(LEAVE_DAILY_ALLOWANCES));
        List<PaymentDeductionObject> leaveMoneyTypeLinkedCategories = loadLeaveSettings(settingsMap.get(LEAVE_MONEY_ALLOWANCES));

        boolean isLeaveSettingsCalculationEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SICK_LEAVE_SETTINGS_CALCULATION);
        Map<Integer, Double[]> spentMinutes = Maps.newHashMapWithExpectedSize(employeeIds.size());
        Map<Integer, Integer> annualLeaveAllowanceMap = Maps.newHashMapWithExpectedSize(employeeIds.size());

        if (isLeaveSettingsCalculationEnabled && filter.getYear() != null && filter.getMonth() != null) {

            Integer approvedStatusId = referenceManager.findReferenceId(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED);

            annualLeaveAllowanceMap.putAll(annualLeaveAllowanceManager.getLastYearMinutesMapByYearAndReasonAndEmployee(filter.getYear(),
                    EdsSickRequest.LR_TYPE_ANNUAL_LEAVE,
                    employeeIds));
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setYear(filter.getYear());
            fp.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
            fp.setStatusID(approvedStatusId);
            fp.setAnnualLeave(true);
            fp.setObjectIDs(employeeIds);

            spentMinutes.putAll(sickRequestDurationManager.getAllowanceSpentByEmployees(fp));
        }
        EdsCompany company = userManager.getUser().getCompany();
        Multimap<Integer, PaymentDeductionObject> paymentDeductionsMap = paymentDeductionManager.getEmployeesPaymentDeductionMap(employeeIds, filter);
        String[] settingsKeys = {RATE_TYPE,
                SALARY,
                REGULAR_OVERTIME_RATE,
                REGULAR_OVERTIME_RATE_TYPE,
                WEEKEND_OVERTIME_RATE,
                WEEKEND_OVERTIME_RATE_TYPE,
                HOLIDAY_OVERTIME_RATE,
                HOLIDAY_OVERTIME_RATE_TYPE,
                PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS,
                PayrollConstants.MATERIAL_AID_TYPE_FUNERAL,
                PayrollConstants.MATERIAL_AID_TYPE_GIFT
        };
        Table<Integer, String, String> employeeSettingsMap = employeePayrollSettingsManager.getEmployeesPayrollSettingMap(employeeIds, settingsKeys);

//        BigDecimal mrotValue = commonServiceLocal.getMrotValueByDate(filter.getToDate().getNonConvertedDate(), true); //TODO related to MROT

        ListingFilterParameter lp = new ListingFilterParameter();
        lp.setObjectIDs(employeeIds);
        lp.setStartDate(filter.getFromDate().getNonConvertedDate());
        lp.setEndDate(filter.getToDate().getNonConvertedDate());
        HashMap<Integer, List<SalaryHistory>> salaryHistoryMap = salaryHistoryManager.getEmployeeSalaryHistoryMap(lp);

        Integer baseCurrencyId = baseCurrency != null ? baseCurrency.getObjectID() : null;
        boolean hasCountry = company != null && company.getCountryZone() != null && company.getCountryZone().getCountry() != null;
        Integer countryId = hasCountry ? company.getCountryZone().getCountry().getObjectID() : null;
        String countryCode = hasCountry ? company.getCountryZone().getCountry().getCode() : "";

        List<Callable<SinglePayrunItem>> futureCall = Lists.newArrayList();

        final String databaseName = ServerSecurityContext.getInstance().getDatabase();
        final String companyIdText = ServerSecurityContext.getInstance().getCompanyId();
        final Integer userId = userManager.getUser().getObjectID();

        for (Integer employeeId : employeeIds) {
            futureCall.add(() -> {
                ServerSecurityContext.getInstance().setCompanyId(companyIdText);
                ServerSecurityContext.getInstance().setDatabase(databaseName);
                ServerSecurityContext.getInstance().setStaticUserID(userId);

                PayslipItemFilter itemFilter = PayslipItemFilter.fromPayslipFilter(filter);
                itemFilter.setEmployeeID(employeeId);
                itemFilter.setEmployeeCodeInteger(isEmployeeCodeInteger);

                itemFilter.setBaseCurrencyId(baseCurrencyId);
                itemFilter.setCountryId(countryId);
                itemFilter.setCountryCode(countryCode);
//                itemFilter.setMrotValue(mrotValue); //TODO related to MROT

                itemFilter.setSpentMinutes(spentMinutes.get(employeeId));
                itemFilter.setCategoryMap(categoryMap);
                itemFilter.setCompanyPayrollSettingsMap(settingsMap);

                itemFilter.setLeaveSettingsCalculationEnabled(isLeaveSettingsCalculationEnabled);
                itemFilter.setLeaveMTCategoryItem(leaveMTCategoryItem);
                itemFilter.setLeaveDailyTypeLinkedCategories(leaveDailyTypeLinkedCategories);
                itemFilter.setLeaveDeductionLinkedCategories(leaveDeductionLinkedCategories);
                itemFilter.setLeaveMoneyTypeLinkedCategories(leaveMoneyTypeLinkedCategories);
                itemFilter.setSalaryHistories(salaryHistoryMap.get(employeeId));
                itemFilter.setEmployeeSettingsMap(employeeSettingsMap.row(employeeId));
                itemFilter.setLastYearMinutes(annualLeaveAllowanceMap.get(employeeId));
                itemFilter.setPaymentDeductions(((ArrayListMultimap<Integer, PaymentDeductionObject>) paymentDeductionsMap).get(employeeId));

                SinglePayrunItem item = generateSinglePayrun(itemFilter);
                ArrayList<PaymentDeductionObject> nonZeroBalancedCategories = new ArrayList<>();
                for (PaymentDeductionObject pdo : item.getPaymentCategories()) {
                    if (pdo.getPaymentAmount() != null && pdo.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                        nonZeroBalancedCategories.add(pdo);
                    }
                }
                item.setPaymentCategories(nonZeroBalancedCategories);

                ArrayList<PaymentDeductionObject> nonZeroBalancedDeductions = new ArrayList<>();
                for (PaymentDeductionObject pdo : item.getDeductionCategories()) {
                    if (pdo.getPaymentAmount() != null && pdo.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                        nonZeroBalancedDeductions.add(pdo);
                    }
                }
                item.setDeductionCategories(nonZeroBalancedDeductions);

                ArrayList<PaymentDeductionObject> nonZeroBalancedTaxes = new ArrayList<>();
                for (PaymentDeductionObject pdo : item.getTaxCategories()) {
                    if (pdo.getPaymentAmount() != null && pdo.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                        nonZeroBalancedTaxes.add(pdo);
                    }
                }
                item.setTaxCategories(nonZeroBalancedTaxes);

                ArrayList<PaymentDeductionObject> nonZeroBalancedEmployerContributions = new ArrayList<>();
                for (PaymentDeductionObject pdo : item.getEmployerContributionCategories()) {
                    if (pdo.getPaymentAmount() != null && pdo.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                        nonZeroBalancedEmployerContributions.add(pdo);
                    }
                }
                item.setEmployerContributionCategories(nonZeroBalancedEmployerContributions);
                return item;
            });
        }
        try {
            for (Future<SinglePayrunItem> future : executor.invokeAll(futureCall)) {
                SinglePayrunItem payrunItem = future.get();
                if (payrunItem == null) {
                    continue;
                }
                singlePayrunItems.add(payrunItem);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        if (filter.getLimit() != null && isEmployeeCodeInteger) {
            singlePayrunItems.sort(Comparator.comparing(SinglePayrunItem::getSortEmployeeNumber));
        }
        result.setList(new ArrayList<>(singlePayrunItems));
        if (singlePayrunItems.isEmpty()) {
            result.setTotal(0);
        }
        return result;
    }

    @Override
    @Transactional
    public SaveResultTO<Integer> createPayslipTable(final GroupPayrunData item, final PayslipFilter filter) {
        final SaveResultTO<Integer> result = new SaveResultTO<>();

        if (item == null || filter == null) {
            return result.setMessage("Incorrect incoming data");
        }
        if (item.getPayrollBatchItem() == null && item.getProjectItem() == null && item.getLocationItem() == null) {
            return result.setMessage("Please, choose project or batch or location!");
        }
        if (item.getMonthID() == null) {
            return result.setMessage("Please, choose month!");
        }
        if (item.getYear() == null) {
            return result.setMessage("Please, choose year!");
        }
        Integer payslipTableId = createGroupPayrun(item);
        createPayslipTableItems(item, filter, payslipTableId);

        if (item.getStatus() != null && PAYRUN_STATUS_SUBMITTED.equals(item.getStatus())) {
            EdsPayslipTable payslipTable = payslipTableManager.get(payslipTableId);
            try {
                messageManager.sendPayslipToManager(payslipTable);
                baseEventsPostProcessor.registerEvent(GroupPayrunEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, payslipTable, userManager.getUser());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (PAYRUN_STATUS_APPROVED.equals(item.getStatus()) && item.isSendNotification()) {
            try {
                messageManager.sendPayslipToEmployees(payslipTableId, item.isSendNotification());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.setResult(1);
    }

    @Transactional
    public Integer createGroupPayrun(GroupPayrunData groupPayrunData) {
        groupPayrunData.setCreatedDate(new DateNonConvertable());
        BigDecimal exchangeRate = BigDecimal.ONE;

        if (groupPayrunData.getPayrollBatchItem() != null) {
            final EdsPayrollBatch edsPayrollBatch = payrollBatchManager.get(groupPayrunData.getPayrollBatchItem().getId());

            if (edsPayrollBatch != null) {
                EdsCurrency currency = edsPayrollBatch.getCurrency();
                if (groupPayrunData.getProcessDate() == null) {
                    groupPayrunData.setProcessDate(new DateNonConvertable());
                }
                if (currency != null) {
                    final CurrencyListItem currencyRateByDate = currencyService.getCurrencyRateByDate(currency.getObjectID(), groupPayrunData.getProcessDate());
                    if (currencyRateByDate != null) {
                        exchangeRate = BigDecimal.valueOf(currencyRateByDate.getExchangeRate());
                    }
                } else {
                    currency = financialSettingsManager.getFinancialSettings().getCurrency();
                }
                groupPayrunData.setCurrency(currency.createCurrencyItem());
            }
        }
        groupPayrunData.setExchangeRate(exchangeRate);
        EdsPayslipTable payslipTable;
        if (groupPayrunData.getObjectID() == null) {
            payslipTable = new EdsPayslipTable();
            payslipTable.setPreparer(employeeManager.get(groupPayrunData.getCreator().getId()));
            payslipTable.setCreationDate(groupPayrunData.getCreatedDate().getNonConvertedDate());
        } else {
            payslipTable = payslipTableManager.get(groupPayrunData.getObjectID());
        }
        payslipTable.setFrequency(groupPayrunData.getFrequency());
        payslipTable.setMonthID(groupPayrunData.getMonthID());
        payslipTable.setYear(groupPayrunData.getYear());
        payslipTable.setMonth(groupPayrunData.getMonth());
        payslipTable.setFromTaxi(groupPayrunData.isFromTaxi());
        payslipTable.setProcessDate(groupPayrunData.getProcessDate() != null ? groupPayrunData.getProcessDate().getNonConvertedDate() : null);
        if (groupPayrunData.getPayrollBatchItem() != null) {
            payslipTable.setPayrollBatch(payrollBatchManager.get(groupPayrunData.getPayrollBatchItem().getId()));
        }
        if (groupPayrunData.getProjectItem() != null) {
            payslipTable.setProject(projectManager.get(groupPayrunData.getProjectItem().getId()));
        }
        if (groupPayrunData.getLocationItem() != null) {
            payslipTable.setLocation(locationManager.get(groupPayrunData.getLocationItem().getId()));
        }
        payslipTable.setStatus(referenceManager.findReference(Constants.PAYRUN_STATUS, Constants.PAYRUN_STATUS_PROCESSING));
        if (groupPayrunData.getApprover() != null) {
            payslipTable.setApprover(employeeManager.get(groupPayrunData.getApprover().getId()));
        }
        if (groupPayrunData.getApprover2() != null) {
            payslipTable.setApprover2(employeeManager.get(groupPayrunData.getApprover2().getId()));
        }
        if (groupPayrunData.getStatus() != null && Constants.PAYRUN_STATUS_APPROVED.equals(groupPayrunData.getStatus())) {
            payslipTable.setApprovedDate(groupPayrunData.getApproveDate().getNonConvertedDate());
        }

        payslipTable.setCurrency(groupPayrunData.getCurrency() != null ? currencyManager.get(groupPayrunData.getCurrency().getId()) : null);
        payslipTable.setExchangeRate(groupPayrunData.getExchangeRate());
        payslipTable.setLastUpdateTime(new Date());
        EdsUser user = userManager.getUser();
        if (user!=null){
            payslipTable.setUpdator(user);
        }
        if (groupPayrunData.getPayMethod() != null) {
            payslipTable.setPaymentMethod(paymentMethodManager.get(groupPayrunData.getPayMethod().getId()));
        }

        PayrollTotalTO totalTO = payslipTableItemManager.getTotalAmountGroupId(payslipTable.getObjectID());
        if (totalTO != null && totalTO.getTotalAmount() != null) {
            payslipTable.setTotalAmount(totalTO.getTotalAmount());
            payslipTable.setTotalInBase(totalTO.getTotalAmount().divide(Optional.ofNullable(payslipTable.getExchangeRate()).orElse(BigDecimal.ONE), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        }

        PayrollAmountsTO amountsTO = payslipTableItemManager.getTotalsByGroupId(payslipTable.getObjectID());
        if (amountsTO != null && amountsTO.getBasicSalary() != null) {
            payslipTable.setBasicSalary(amountsTO.getBasicSalary());
            payslipTable.setAllowance(amountsTO.getAllowance());
            payslipTable.setPension(amountsTO.getPension());
            payslipTable.setDeduction(amountsTO.getDeduction());
            payslipTable.setTax(amountsTO.getTax());
            payslipTable.setEmployerContribution(amountsTO.getEmployerContribution());
            payslipTable.setExpense(amountsTO.getExpense());
        }
        payslipTableManager.createOrUpdate(payslipTable);

        addGroupPayrunToSolr(payslipTable);
        if (groupPayrunData.getObjectID() == null) {
            baseEventsPostProcessor.registerEvent(GroupPayrunEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, payslipTable, userManager.getUser());
        } else {
            baseEventsPostProcessor.registerEvent(GroupPayrunEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, payslipTable, userManager.getUser());
        }
        return payslipTable.getObjectID();
    }

    @Override
    public PayrollTotalTO deleteSinglePayrun(Integer id) {
        final EdsPayslipTableItem singlePayrun = payslipTableItemManager.get(id);

        if (singlePayrun == null || singlePayrun.getPayslipTable() == null) {
            return new PayrollTotalTO();
        }
        EdsPayslipTable payslipTable = singlePayrun.getPayslipTable();

        deleteSinglePayrun(singlePayrun.getObjectID(), null);
        return savePayslipTableTotal(payslipTable);
    }

    private PayrollTotalTO savePayslipTableTotal(EdsPayslipTable payslipTable) {
        if (payslipTable == null || payslipTable.isDeleted()) {
            return new PayrollTotalTO();
        }
        final PayrollTotalTO totalTO = payslipTableItemManager.getTotalAmountGroupId(payslipTable.getObjectID());

        if (totalTO != null && totalTO.getTotalAmount() != null) {
            payslipTable.setTotalAmount(totalTO.getTotalAmount());
            payslipTable.setTotalInBase(totalTO.getTotalAmount().divide(Optional.ofNullable(payslipTable.getExchangeRate()).orElse(BigDecimal.ONE), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        }

        PayrollAmountsTO amountsTO = payslipTableItemManager.getTotalsByGroupId(payslipTable.getObjectID());
        if (amountsTO != null && amountsTO.getBasicSalary() != null) {
            payslipTable.setBasicSalary(amountsTO.getBasicSalary());
            payslipTable.setAllowance(amountsTO.getAllowance());
            payslipTable.setPension(amountsTO.getPension());
            payslipTable.setDeduction(amountsTO.getDeduction());
            payslipTable.setTax(amountsTO.getTax());
            payslipTable.setEmployerContribution(amountsTO.getEmployerContribution());
            payslipTable.setExpense(amountsTO.getExpense());

            totalTO.setBasicSalary(amountsTO.getBasicSalary());
            totalTO.setAllowance(amountsTO.getAllowance());
            totalTO.setPension(amountsTO.getPension());
            totalTO.setDeduction(amountsTO.getDeduction());
            totalTO.setTax(amountsTO.getTax());
            totalTO.setEmployerContribution(amountsTO.getEmployerContribution());
            totalTO.setExpense(amountsTO.getExpense());
        }

        payslipTableManager.update(payslipTable);
        payslipTableManager.flushAndClear();
        addGroupPayrunToSolr(payslipTable);
        return totalTO;
    }

    @Override
    public PayrolTableItemListResult getPayslipTableItemsList(ListingFilterParameter fp) {
        final PayrolTableItemListResult result = new PayrolTableItemListResult();

        result.setTotal(0);
        result.setList(new ArrayList<>());
        if (fp.getGroupPayrunID() == null) {
            return result;
        }
        final EdsPayslipTable payslipTable = this.payslipTableManager.get(fp.getGroupPayrunID());

        if (payslipTable == null) {
            return result;
        }
        final Integer count = this.payslipTableItemManager.getCountByFilter(fp);
        final List<SinglePayrunItem> resulList = Lists.newArrayListWithExpectedSize(fp.getLimit());

        result.setTotal(count);
        if (count <= 0) {
            return result;
        }
        final EdsPayrollCategory expenseCategory = categoryManager.getCategoryByCode(EXPENSE_REPORT);
        PaymentDeductionSelectItem expenseCategoryItem = null;

        if (expenseCategory != null) {
            expenseCategoryItem = expenseCategory.createPaymentDeductionSelectItem();
        }
        final EdsCompany company = this.userManager.getUser().getCompany();
        final EdsCurrency baseCurrency = this.financialSettingsManager.getFinancialSettings().getCurrency();
        final boolean isEmployeeCodeInteger = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER);
        final boolean empInBase = payslipTable.getCurrency() == null || payslipTable.getCurrency().getObjectID().equals(baseCurrency.getObjectID());
        final List<EdsPayslipTableItem> items = this.payslipTableItemManager.getListByFilter(fp);
        final String expensePaidFromAccount = this.getCompanyPayrollSettings(EXPENSE_PAID_ACCOUNT);
        final EdsAccount paidFromAccount = !StringUtil.isEmpty(expensePaidFromAccount) ? accountingManager.get(Integer.valueOf(expensePaidFromAccount)) : null;

        result.setTotalTO(payslipTableItemManager.getTotalAmountGroupId(payslipTable.getObjectID()));
        for (EdsPayslipTableItem item : items) {
            if (item.getEmployee() == null) {
                continue;
            }
            SinglePayrunItem pItem = item.getRPC(null);
            if (item.getEmployee().getProfile() != null && !StringUtil.isEmpty(item.getEmployee().getProfile().getEmployeeCode())) {
                pItem.setEmployee(item.getEmployee().getProfile().getEmployeeCode().concat(" -> ").concat(item.getEmployee().getFullName()));

                if (isEmployeeCodeInteger && StringUtils.isNotEmpty(pItem.getEmployeeCode())) {
                    try {
                        pItem.setEmployeeNumber(Long.parseLong(pItem.getEmployeeCode().replaceAll("\\D", "")));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
            pItem.setSalary(item.getBasicSalary());
            pItem.setEditable(item.getStatus() == null || (!Constants.PAYRUN_STATUS_SUBMITTED.equals(item.getStatus().getCode()) && !Constants.PAYRUN_STATUS_APPROVED.equals(item.getStatus().getCode())
                    && !Constants.PAYRUN_STATUS_PARTIAL_PAID.equals(item.getStatus().getCode()) && !Constants.PAYRUN_STATUS_PAID.equals(item.getStatus().getCode())));

            final List<EdsPaymentDeduction> categories = payslipTableItemManager.getItemCategories(item.getObjectID());

            for (EdsPaymentDeduction paymentDeduction : categories) {
                PaymentDeductionObject object = paymentDeduction.getRPC();
                object.setPaymentAmount(this.payslipPaymentsManager.getPaymentAmount(paymentDeduction.getObjectID(), item.getObjectID()));
                if (object.isPaymentCategory()) {
                    pItem.getPaymentCategories().add(object);
                } else if (object.isTaxCategory()) {
                    pItem.getTaxCategories().add(object);
                } else if (object.isEmployerContributionCategory()) {
                    pItem.getEmployerContributionCategories().add(object);
                } else if (object.isDeductionCategory()) {
                    if (LEAVE_DEDUCTIONS.equals(object.getCategoryItem().getCode()) && object.getLeaveDaysCount() != null) {
                        pItem.setNonPaidLeaveDays(pItem.getNonPaidLeaveDays() + object.getLeaveDaysCount().intValue());
                    }
                    pItem.getDeductionCategories().add(object);
                }
            }
            if (pItem.getAdditionalPay() != null && pItem.getAdditionalPay().compareTo(BigDecimal.ZERO) > 0) {
                final EdsPayrollCategory bonusCategory = this.categoryManager.getCategoryByCode(BONUS);

                if (bonusCategory != null) {
                    PaymentDeductionObject bonus = new PaymentDeductionObject();
                    bonus.setCategoryItem(bonusCategory.createPaymentDeductionSelectItem());
                    bonus.setPaymentAmount(pItem.getAdditionalPay());
                    pItem.getPaymentCategories().add(bonus);
                }
            }
            BigDecimal amount = BigDecimal.ZERO;
            final List<ExpenseData> expenses = Lists.newLinkedList();
            final List<EdsExpenseReport> linkedExpenses = expenseReportManager.getPayslipTableItemRelatedExpenseClaims(pItem.getObjectID());

            for (EdsExpenseReport exp : linkedExpenses) {
                final boolean expInBase = empInBase || exp.getCurrency() == null || exp.getCurrency().getObjectID().equals(baseCurrency.getObjectID());
                ExpenseData expData;
                double totalExp = expInBase ? exp.getBaseTotal().doubleValue() : exp.getTotal().doubleValue();
                if (PARTIALLY_PAID.equals(exp.getStatus().getCode())) {
                    double paid = exp.getPaidTotal(expInBase).doubleValue();
                    totalExp -= paid;
                } else if (EXPENSE_PAID.equals(exp.getStatus().getCode())) {
                    totalExp = exp.getPaidTotalByPayslip(pItem.getObjectID(), expInBase).doubleValue();
                }
                if (exp.getAccount() != null) {
                    expData = new ExpenseData(exp.getObjectID(),
                            exp.getTitle(),
                            totalExp,
                            expInBase,
                            exp.getAccount().getObjectID(),
                            exp.getAccount().getName());
                } else {
                    if (paidFromAccount == null) {
                        expData = new ExpenseData(exp.getObjectID(),
                                exp.getTitle(),
                                totalExp,
                                expInBase,
                                null,
                                "");
                    } else {
                        expData = new ExpenseData(exp.getObjectID(),
                                exp.getTitle(),
                                totalExp,
                                expInBase,
                                paidFromAccount.getObjectID(),
                                paidFromAccount.getName());
                    }
                }
                if (expData.isInBaseCurrency() && payslipTable.getExchangeRate() != null) {
                    expData.setAmount(expData.getAmount() * payslipTable.getExchangeRate().doubleValue());
                    expData.setInBaseCurrency(false);
                }
                expenses.add(expData);
                amount = amount.add(BigDecimal.valueOf(expData.getAmount()));
            }
            if (!expenses.isEmpty()) {
                expenses.sort((o1, o2) -> o2.getObjectID().compareTo(o1.getObjectID()));
                final PaymentDeductionObject expensePayment = new PaymentDeductionObject();

                expensePayment.setPaymentAmount(amount);
                expensePayment.setExpenses(expenses.toArray(new ExpenseData[]{}));
                expensePayment.setCategoryItem(expenseCategoryItem);
                pItem.setEmployeeExpenses(expensePayment);
            }

            if (item.getEmployee().getProfile() != null && item.getEmployee().getProfile().getCountry() != null) {
                pItem.setCalculatePension(true);
                if (company.getCountryZone() != null) {
                    pItem.setLocalEmployee(company.getCountryZone()
                            .getCountry()
                            .equals(item.getEmployee()
                                    .getProfile()
                                    .getCountry()));
                }
            }
            resulList.add(pItem);
        }
        result.setList(Lists.newArrayList(resulList));
        PayrollAmountsTO amounts = payslipTableItemManager.getTotalsByGroupId(payslipTable.getObjectID());
        result.setBasicSalary(amounts.getBasicSalary());
        result.setAllowance(amounts.getAllowance());
        result.setPensionAmount(amounts.getPension());
        result.setDeduction(amounts.getDeduction());
        result.setTax(amounts.getTax());
        result.setEmployeeContribution(amounts.getEmployerContribution());
        result.setEmployeeExpenses(amounts.getExpense());
        return result;
    }

    @Override
    public GroupPayrunData getPayslipTableSimple(PayslipFilter filter) {
        final EdsCompany company = userManager.getUser().getCompany();
        final GroupPayrunData result = new GroupPayrunData();

        result.setObjectID(filter.getObjectID());
        final Map<String, String> payrollSettingsMap = companyPayrollSettingsManager.getCompanyPayrollSettingsMap(ENABLED_DOUBLE_APPROVER_PAYRUN,
                DOUBLE_CONFIRMATION,
                BY_DEFAULT_EMAIL_NOTIFICATION,
                MULTI_CURRENCY_FOR_PAYROLL);

        result.setDoubleApprovedEnabled("true".equals(payrollSettingsMap.get(ENABLED_DOUBLE_APPROVER_PAYRUN)));
        result.setDoubleConfirmationEnabled("true".equals(payrollSettingsMap.get(DOUBLE_CONFIRMATION)));
        result.setSendNotification("true".equals(payrollSettingsMap.get(BY_DEFAULT_EMAIL_NOTIFICATION)));
        result.setEnabledMultiCurrency("true".equals(payrollSettingsMap.get(MULTI_CURRENCY_FOR_PAYROLL)));
        final EdsPayslipTable payslipTable = payslipTableManager.get(filter.getObjectID());

        if (payslipTable == null) {
            return result;
        }
//        savePayslipTableTotal(payslipTable);
        result.setMonthID(payslipTable.getMonthID());
        result.setYear(payslipTable.getYear());
        result.setMonth(payslipTable.getMonth());
        result.setFrequency(payslipTable.getFrequency());
        result.setExchangeRate(payslipTable.getExchangeRate());
        result.setTotalAmount(payslipTable.getTotalAmount());
        result.setTotalInBase(payslipTable.getTotalInBase());
        result.setCreatedDate(new DateNonConvertable(payslipTable.getCreationDate()));
        result.setApproveDate(new DateNonConvertable(payslipTable.getApprovedDate()));
        result.setFromTaxi(payslipTable.isFromTaxi());
        if (payslipTable.getStatus() != null) {
            result.setStatus(payslipTable.getStatus().getName());
            result.setStatusCode(payslipTable.getStatus().getCode());
        }

        if (payslipTable.getPaymentMethod() != null) {
            result.setPayMethod(payslipTable.getPaymentMethod().getAsSelectItem());
        }

        if (payslipTable.getCurrency() != null) {
            result.setCurrency(payslipTable.getCurrency().createCurrencyItem());
        }
        if (payslipTable.getProcessDate() != null) {
            result.setProcessDate(new DateNonConvertable(payslipTable.getProcessDate()));
        } else {
            result.setProcessDate(new DateNonConvertable(new Date(payslipTable.getYear() - 1900,
                    payslipTable.getMonthID(),
                    DateUtil.getDateInMonth(payslipTable.getYear(), payslipTable.getMonthID()))));
        }
        if (payslipTable.getPayrollBatch() != null) {
            result.setPayrollBatchItem(payslipTable.getPayrollBatch().getAsSelectItem());
        } else if (payslipTable.getProject() != null) {
            result.setProjectItem(payslipTable.getProject().getAsSelectItem());
        } else if (payslipTable.getLocation() != null) {
            result.setLocationItem(payslipTable.getLocation().getAsSelectItem());
        } else if (!"true".equals(this.getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL))) {
            result.setPayrollBatchItem(new SelectItem(0, commonLocalizer.localize("allEmployees", "All employees")));
        }
        if (payslipTable.getApprover() != null) {
            if (payslipTable.getApprover().getPosition() != null) {
                result.setApprover(new SelectItem(payslipTable.getApprover().getObjectID(),
                        payslipTable.getApprover().getFullName(),
                        payslipTable.getApprover().getPosition().getName()));
            } else {
                if (payslipTable.getApprover().getProfile() != null && payslipTable.getApprover().getProfile().getEmployeeCode() != null) {
                    result.setApprover(new SelectItem(payslipTable.getApprover().getObjectID(),
                            payslipTable.getApprover().getProfile().getEmployeeCode() + " - " +
                                    payslipTable.getApprover().getFullName()));
                } else {
                    result.setApprover(new SelectItem(payslipTable.getApprover().getObjectID(),
                            payslipTable.getApprover().getFullName()));
                }
            }
        }
        if (payslipTable.getStatus() != null && Constants.PAYRUN_STATUS_PENDING.equals(payslipTable.getStatus().getCode())) {
            result.setPendingItemIds(payslipTableItemManager.getPendingItems(payslipTable.getObjectID()));
        }
        if (payslipTable.getPreparer() != null) {
            if (payslipTable.getPreparer().getPosition() != null) {
                result.setCreator(new SelectItem(payslipTable.getPreparer().getObjectID(), payslipTable.getPreparer().getFullName(), payslipTable.getPreparer().getPosition().getName()));
            } else {
                result.setCreator(new SelectItem(payslipTable.getPreparer().getObjectID(), payslipTable.getPreparer().getFullName()));
            }
        }
        if (payslipTable.getPaymentMethod() != null) {
            result.setPayMethod(payslipTable.getPaymentMethod().getAsSelectItem());
        }
        final String countryCode = company.getCountryZone() != null &&
                company.getCountryZone().getCountry() != null
                ? company.getCountryZone().getCountry().getCode()
                : "";
        final EdsPensionScheme pensionScheme = pensionSchemeManager.getPensionSchema(countryCode);

        if (pensionScheme != null) {
            result.setPensionType(pensionScheme.getDeductionType());
            result.setCompanyPensionType(pensionScheme.getEmployerDeductionType());
            result.setPensionValue(pensionScheme.getDeductionValue());
            result.setNonLocalPensionValue(pensionScheme.getNonLocalDeductionValue());
            result.setCompanyPensionValue(pensionScheme.getEmployerDeductionValue());
            result.setCompanyNonLocalPensionValue(pensionScheme.getEmployerNonLocalDeductionValue());
            result.setPensionValueType(pensionScheme.getDeductFrom());
            result.setEmpMaxTaxableAmount(pensionScheme.getEmpMaxTaxableAmount());
            result.setCompMaxtaxableAmount(pensionScheme.getCompMaxTaxableAmount());
            if (pensionScheme.getCategories() != null && pensionScheme.getCategories().size() > 0) {
                for (EdsPayrollCategory category : pensionScheme.getCategories()) {
                    result.getPensionAllowances().add(category.createPaymentDeductionSelectItem());
                }
            }
        }
        final EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();

        if (fs != null && fs.getCurrency() != null) {
            result.setCurrencyName(fs.getCurrency().getName());
        }
        result.setAtsCustomization(genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ATS_PAYROLL_CUSTOMIZATION));
        final List<EdsPaymentMethod> paymentMethods = this.paymentMethodManager.list();

        for (EdsPaymentMethod method : paymentMethods) {
            result.getPaymentMethods().add(new SelectItem(method.getObjectID(), commonLocalizer.localize(method.getCode(), method.getName())));
        }

        if (payslipTable.getPayments() != null && !CollectionUtils.isEmpty(payslipTable.getPayments())) {
            result.setPayments(payslipTable.getPayments().stream().map(EdsPayrunPayment::toSimpleRPC).collect(Collectors.toCollection(ArrayList::new)));
        }

        BigDecimal totalSingleOnlyPayments = payrunPaymentItemManager.getTotalSinglePaymentsByGroupPayrunId(payslipTable.getObjectID());
        if (totalSingleOnlyPayments.compareTo(BigDecimal.ZERO) > 0) {
            List<PayrunPayment> payments = Optional.ofNullable(result.getPayments()).orElse(new ArrayList<>());
            PayrunPayment singlePayment = new PayrunPayment();
            singlePayment.setAmount(totalSingleOnlyPayments);
            payments.add(singlePayment);
        }

        return result;
    }

    @Override
    public PayrollTotalTO batchChangePayrollGroupStatus(Integer id, String status) {
        if (id == null || StringUtil.isEmpty(status)) {
            return new PayrollTotalTO();
        }
        final EdsPayslipTable payslipTable = payslipTableManager.get(id);
        return batchChangePayrollGroupStatus(payslipTable, status);
    }


    @Transactional
    public PayrollTotalTO batchChangePayrollGroupStatus(EdsPayslipTable payslipTable, String status) {
        if (payslipTable == null || StringUtil.isEmpty(status)) {
            return new PayrollTotalTO();
        }

        if (payslipTable == null || payslipTable.isDeleted()) {
            return new PayrollTotalTO();
        }

        EdsReference payslipStatus = referenceManager.findReference(PAYRUN_STATUS, status);
        EdsReference pendingStatus = referenceManager.findReference(PAYRUN_STATUS, Constants.PAYRUN_STATUS_PENDING);
        EdsReference paidStatus = referenceManager.findReference(PAYRUN_STATUS, Constants.PAYRUN_STATUS_PAID);

        final boolean isApprovedOrSubmitted = Objects.equals(payslipStatus.getCode(), PAYRUN_STATUS_SUBMITTED) || Objects.equals(payslipStatus.getCode(), PAYRUN_STATUS_APPROVED);

        if (isApprovedOrSubmitted) {
            payslipTable.setApprovedDate(new Date());
        }
        boolean pendingItemExists = false;
        boolean allPaid = true;

        for (EdsPayslipTableItem payslipTableItem : payslipTable.getPayslipTableItems()) {
            payslipTableItem.setApprovalOrRejectionStatus(payslipStatus);
            payslipTableItem.setProcessDate(payslipTable.getProcessDate());
            payslipTableItem.setProcessDate(payslipTable.getProcessDate());
            if (isApprovedOrSubmitted) {
                if (payslipTableItem.getTotal().compareTo(BigDecimal.ZERO) < 0) {
                    payslipTableItem.setStatus(pendingStatus);
                    payslipTableItem.setStatus2(pendingStatus);
                    pendingItemExists = true;
                } else if (Objects.equals(payslipStatus.getCode(), PAYRUN_STATUS_APPROVED) && payslipTableItem.getTotal().compareTo(BigDecimal.ZERO) == 0) {
                    payslipTableItem.setStatus(paidStatus);
                    payslipTableItem.setStatus2(paidStatus);
                    payslipTableItem.setApprovedDate(payslipTable.getApprovedDate());
                } else {
                    payslipTableItem.setStatus(payslipStatus);
                    payslipTableItem.setStatus2(payslipStatus);
                    payslipTableItem.setApprovedDate(payslipTable.getApprovedDate());
                    allPaid = false;
                }
            }
            payslipTableItem.setLastUpdateTime(new Date());
            payslipTableItemManager.createOrUpdate(payslipTableItem);
            baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, payslipTableItem, userManager.getUser());
        }
        addSinglePayrunToSolr(payslipTable.getPayslipTableItems().toArray(new EdsPayslipTableItem[]{}));

        if (pendingItemExists) {
            payslipTable.setStatus(pendingStatus);
        } else if (Objects.equals(payslipStatus.getCode(), PAYRUN_STATUS_APPROVED) && allPaid) {
            payslipTable.setStatus(paidStatus);
        } else {
            payslipTable.setStatus(payslipStatus);
        }
        payslipTableManager.flush();

        if (!Objects.equals("true", getCompanyPayrollSettings(DISABLE_PAYROLL_TRANSACTIONS))) {
            createTransactionForPayslipTable(payslipTable.getPayslipTableItems());
        }

        baseEventsPostProcessor.registerEvent(GroupPayrunEventListenerImpl.TYPE, EdsMyUpdate.STATUS_CHANGE, payslipTable, userManager.getUser());
        savePayslipTableTotal(payslipTable);
        payrollAsyncService.applyGroupPayrunTotal(payslipTable.getObjectID());
        return new PayrollTotalTO();
    }

    @Override
    public SinglePayrunItem updateSinglePayrollItem(SinglePayrunItem item, Boolean dateChange) {
        if (item == null) {
            return new SinglePayrunItem();
        }
        EdsPayslipTableItem payslipTableItem = this.payslipTableItemManager.get(item.getObjectID());

        if (payslipTableItem == null ||
                Optional.ofNullable(payslipTableItem.isDeleted()).orElse(false) ||
                payslipTableItem.getPayslipTable() == null ||
                payslipTableItem.getPayslipTable().isDeleted()) {
            return new SinglePayrunItem();
        }
        if (Optional.ofNullable(dateChange).orElse(false) && item.getToDate() != null && item.getFromDate() != null) {
            final EdsEmployee employee = payslipTableItem.getEmployee();
            final Date endDate = employee.getEndDate() == null || employee.getEndDate().after(item.getToDate().getNonConvertedDate()) ?
                    item.getToDate().getNonConvertedDate() :
                    employee.getEndDate();

            int workedDaysWithoutOffset = DateUtil.countDays(DateUtil.getMonthFirstDay(item.getFromDate().getNonConvertedDate()), endDate);

            boolean isDailyRateByEmployerSettings = "true".equals(getCompanyPayrollSettings(DAILY_RATE_BY_EMPLOYER_SETTINGS)) || "BY_STATIC_DAY".equals(getCompanyPayrollSettings(DAILY_RATE_BY_EMPLOYER_SETTINGS));

            BigDecimal numberOfWorkDays = isDailyRateByEmployerSettings ?
                    new BigDecimal(getCompanyPayrollSettings(NUMBER_OF_WORK_DAYS, DEFAULT_NUMBER_OF_WORK_DAYS.toString())) :
                    BigDecimal.valueOf(DateUtil.getDateInMonth(item.getYear(), item.getMonthID()));

            final int monthOffset = DateUtil.countDays(DateUtil.getMonthFirstDay(item.getFromDate().getNonConvertedDate()), item.getFromDate().getNonConvertedDate()) - 1;//start_date - 1

            workedDaysWithoutOffset = Math.min(numberOfWorkDays.intValue(), workedDaysWithoutOffset);
            int selectedPeriod = workedDaysWithoutOffset - monthOffset;
            BigDecimal allowanceRatio;
            if (selectedPeriod < numberOfWorkDays.intValue()) {
                allowanceRatio = BigDecimal.valueOf(selectedPeriod).divide(numberOfWorkDays, 10, RoundingMode.HALF_UP);
                final BigDecimal actualBasicSalary = item.getSalary().multiply(allowanceRatio);

                item.setToDate(new DateNonConvertable(endDate));
                item.setTotal(item.getTotal().subtract(item.getBasicSalary()).add(actualBasicSalary));
                item.setTotalInBase(item.getTotal().multiply(Optional.ofNullable(item.getExchangeRate()).orElse(BigDecimal.ONE)));
                item.setBasicSalary(actualBasicSalary);
            }
        }
        EdsPayslipTable payslipTable = payslipTableItem.getPayslipTable();
        payslipTableItem.setEmployee(employeeManager.get(item.getEmployeeID()));
        payslipTableItem.setDriverID(item.getDriverID());
        payslipTableItem.setFromDate(item.getFromDate() != null ? item.getFromDate().getNonConvertedDate() : null);
        payslipTableItem.setToDate(item.getToDate() != null ? item.getToDate().getNonConvertedDate() : null);
        payslipTableItem.setProcessDate(item.getProcessDate() != null ? item.getProcessDate().getNonConvertedDate() : null);
        payslipTableItem.setDaysWorked(item.getDaysWorked());
        payslipTableItem.setCollection(item.getMonthlyCollection());
        payslipTableItem.setCommision(item.getComission());
        payslipTableItem.setUsedPetrol(item.getSpentFlueAmount());
        payslipTableItem.setMonthlySalik(item.getMonthlySalik());
        payslipTableItem.setBasicSalary(item.getBasicSalary());
        payslipTableItem.setDailyRate(item.getDailyRate());
        payslipTableItem.setActualMonthPay(item.getActualMonthPay());
        payslipTableItem.setAllowance(item.getAllowance());
        payslipTableItem.setAdditionalPay(item.getAdditionalPay());
        payslipTableItem.setDeduction(item.getDeduction());
        payslipTableItem.setTax(item.getTax());
        payslipTableItem.setEmployerContribution(item.getEmployerContribution());
        payslipTableItem.setExpense(item.getExpense());
        payslipTableItem.setDescription(item.getDescription());
        payslipTableItem.setTotal(item.getTotal());
        payslipTableItem.setTotalInBase(item.getTotalInBase());
        payslipTableItem.setCurrency(payslipTable.getCurrency());
        payslipTableItem.setExchangeRate(payslipTable.getExchangeRate());
        payslipTableItem.setPensionRate(item.getPensionRate());
        payslipTableItem.setPensionValueType(item.getPensionValueType());
        payslipTableItem.setPensionAmount(item.getPensionAmount());
        payslipTableItem.setCompanyPensionAmount(item.getCompanyPensionAmount());
        payslipTableItem.setCompanyPensionRate(item.getCompanyPensionRate());
        payslipTableItem.setCompanyNonLocalPensionRate(item.getCompanyNonLocalPensionRate());
        payslipTableItem.setCompanyPensionType(item.getCompanyPensionType());
        payslipTableItem.setNonLocalPensionRate(item.getNonLocalPensionRate());
        payslipTableItem.setStatus(payslipTable.getStatus());
        payslipTableItem.setStatus2(payslipTable.getStatus2());
        payslipTableItem.setApprover(payslipTable.getApprover());
        payslipTableItem.setApprover2(payslipTable.getApprover2());
        payslipTableItem.setPreparer(payslipTable.getPreparer());
        payslipTableItem.setMonthID(item.getMonthID() != null ? item.getMonthID() : payslipTable.getMonthID());
        payslipTableItem.setMonth(item.getMonth() != null ? item.getMonth() : payslipTable.getMonth());
        payslipTableItem.setYear(item.getYear() != null ? item.getYear() : payslipTable.getYear());
        payslipTableItem.setFrequency(payslipTable.getFrequency());
        payslipTableItem.setCreationDate(payslipTable.getCreationDate());
        payslipTableItem.setApprovedDate(payslipTable.getApprovedDate());
        payslipTableItem.setGrossPay(item.getGross());
        payslipTableItem.setLastUpdateTime(new Date());
        this.payslipTableItemManager.createOrUpdate(payslipTableItem);

        this.baseEventsPostProcessor.registerEvent(SinglePayrunEventListenerImpl.TYPE,
                BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT,
                payslipTableItem,
                userManager.getUser());

        final String salarySettings = getEmployeeSettingValue(item.getEmployeeID(), SALARY_CATEGORY, "");
        EdsPayrollCategory basicSalayCategory;
        if (salarySettings != null && !salarySettings.isEmpty()) {
            basicSalayCategory = categoryManager.get(Integer.parseInt(salarySettings));
        } else {
            basicSalayCategory = categoryManager.getCategoryByCode(BASIC_SALARY);
        }
        item.getPaymentCategories().removeIf(PaymentDeductionObject::isSalaryObject);

        final PaymentDeductionObject basicSalary = new PaymentDeductionObject();
        basicSalary.setPaymentAmount(item.getBasicSalary());
        basicSalary.setCategoryItem(basicSalayCategory.createPaymentDeductionSelectItem());
        basicSalary.setSalaryObject(true);
        item.getPaymentCategories().add(basicSalary);

        registerPaymentDeductionCategories(item, payslipTableItem);
        registerEmployeeExpenses(item.getEmployeeExpenses(), payslipTableItem);
        addSinglePayrunToSolr(payslipTableItem);
        item.setTotalTO(savePayslipTableTotal(payslipTable));
        return item;
    }

    @Transactional
    public void createPayslipTableItemsForImport(GroupPayrunData item, Integer payslipTableId, ArrayList<SinglePayrunItem> singlePayrunList) {
        final String database = ServerSecurityContext.getInstance().getDatabase();
        final String companyId = ServerSecurityContext.getInstance().getCompanyId();
        final Integer userId = userManager.getUser().getObjectID();

        ArrayList<Callable<Integer>> futureCall = new ArrayList<>();

        if (item.getObjectID() == null) {

            ListResult<SinglePayrunItem> itemsToSave = null;
            if (singlePayrunList != null && !singlePayrunList.isEmpty()) {
                PayrolTableItemListResult result = new PayrolTableItemListResult();
                result.setTotal(singlePayrunList.size());
                result.setList(singlePayrunList);
                itemsToSave = result;
            }
            if (itemsToSave.getTotal() <= 0 || itemsToSave.getList() == null) {
                return;
            }
            HashMap<Integer, SinglePayrunItem> changedItems = item.getChangedItems();
            HashMap<Integer, Boolean> deletedItems = item.getDeletedItems();

            for (SinglePayrunItem singlePayrunItem : itemsToSave.getList()) {
                if (deletedItems != null && deletedItems.containsKey(singlePayrunItem.getEmployeeID())) {
                    continue;
                } else if (changedItems != null && changedItems.containsKey(singlePayrunItem.getEmployeeID())) {
                    singlePayrunItem = changedItems.get(singlePayrunItem.getEmployeeID());
                }
                SinglePayrunItem finalSinglePayrunItem = singlePayrunItem;
                futureCall.add(() -> {
                    ServerSecurityContext.getInstance().setDatabase(database);
                    ServerSecurityContext.getInstance().setCompanyId(companyId);
                    ServerSecurityContext.getInstance().setStaticUserID(userId);
                    finalSinglePayrunItem.setGroupPayrunID(payslipTableId);
                    finalSinglePayrunItem.setStatus(item.getStatus());
                    Integer objectId = payrollAsyncService.saveSinglePayrunItem(finalSinglePayrunItem, (payrunItem, edsPayslipItem) -> {
                        registerPaymentDeductionCategories(payrunItem, edsPayslipItem);
                        registerEmployeeExpenses(payrunItem.getEmployeeExpenses(), edsPayslipItem);
                    });
                    addSinglePayrunToSolr(objectId);
                    return objectId;
                });
            }
        }

        ArrayList<Integer> singlePayrunIds = new ArrayList<>();
        try {
            for (Future<Integer> future : executor.invokeAll(futureCall)) {
                singlePayrunIds.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("", e);
        }

        payrollAsyncService.getInNewTransaction(() -> {
            batchChangePayrollGroupStatus(payslipTableId, item.getStatus());
            return null;
        });

    }

    private SinglePayrunItem generateSinglePayrun(PayslipItemFilter filter) {
        final Integer employeeId = filter.getEmployeeID();
        final EdsEmployee employee = this.employeeManager.get(employeeId);
        final EdsCompany company = employee.getCompany();
        /////////////////////////////////////////////////
        final PaymentDeductionSelectItem leaveDeductionCategoryItem = filter.getCategory(LEAVE_DEDUCTIONS);
        final PaymentDeductionSelectItem benefitCategoryItem = filter.getCategory(BENEFIT_PAYMENT);
        final PaymentDeductionSelectItem expenseCategoryItem = filter.getCategory(EXPENSE_REPORT);
        final PaymentDeductionSelectItem regularOTCategoryItem = filter.getCategory(REGULAR_OVERTIME);
        final PaymentDeductionSelectItem weekendOTCategoryItem = filter.getCategory(WEEKEND_OVERTIME);
        final PaymentDeductionSelectItem holidayOTCategoryItem = filter.getCategory(HOLIDAY_OVERTIME);
        final PaymentDeductionSelectItem absenceDeductionItem = filter.getCategory(ABSENCE_DEDUCTIONS);
        final PaymentDeductionSelectItem additionalPaymentCategoryItem = filter.getCategory(ADDITIONAL_PAYMENT);
        final PaymentDeductionSelectItem bonusCategoryItem = filter.getCategory(BONUS);
        final PaymentDeductionSelectItem leaveDTCategoryItem = filter.getCategory(LEAVE_ENCHASHMENT);

        final List<PaymentDeductionObject> paymentDeductions = filter.getPaymentDeductions();
        final Map<String, String> employeeSettingsMap = Optional.ofNullable(filter.getEmployeeSettingsMap()).orElse(new HashMap<>());

        final boolean isNonPaidLeaveDaysImpact = "true".equals(filter.getCompanyPayrollSettingsOrDefault(NON_PAID_LEAVE_DAYS_IMPACT, "true"));
        final boolean isPaidLeaveDaysImpact = "true".equals(filter.getCompanyPayrollSettingsOrDefault(LEAVE_DAYS_IMPACT, "true"));
        final boolean isDailyRateByEmployerSettings = "true".equals(filter.getCompanyPayrollSettings(DAILY_RATE_BY_EMPLOYER_SETTINGS)) || "BY_STATIC_DAY".equals(filter.getCompanyPayrollSettings(DAILY_RATE_BY_EMPLOYER_SETTINGS));
        final boolean isCalculationByTimeslot = "BY_TIMESLOT".equals(filter.getCompanyPayrollSettings(DAILY_RATE_BY_EMPLOYER_SETTINGS));
        final boolean enabledLeaveDeductions = "true".equals(filter.getCompanyPayrollSettings(ENABLED_LEAVE_DEDUCTIONS));
        final boolean enabledLeavePayments = "true".equals(filter.getCompanyPayrollSettingsOrDefault(ENABLED_LEAVE_PAYMENTS, "true"));
        final boolean salaryModeByAttendanceReport = Constants.BY_ATTENDANCE_REPORT.equals(employee.getSalaryMode());
        final int leaveDeductType = Integer.parseInt(filter.getCompanyPayrollSettingsOrDefault(DEDUCT_TYPE, "0"));
        final int leaveDailyType = Integer.parseInt(filter.getCompanyPayrollSettingsOrDefault(LEAVE_DAILY_PAYMENT_TYPE, "0"));
        final int leaveMoneyType = Integer.parseInt(filter.getCompanyPayrollSettingsOrDefault(LEAVE_MONEY_PAYMENT_TYPE, "0"));
        /////////////////////////////////////////////////
        BigDecimal paymentsTotal = BigDecimal.ZERO;
        BigDecimal deductionsTotal = BigDecimal.ZERO;
        BigDecimal taxTotal = BigDecimal.ZERO;
        BigDecimal employerContributionTotal = BigDecimal.ZERO;
        BigDecimal materialAidTotal = BigDecimal.ZERO;
        BigDecimal overtimeTotal = BigDecimal.ZERO;
        BigDecimal expensesTotal = BigDecimal.ZERO;
        BigDecimal customPaymentsTotal = BigDecimal.ZERO;

        Map<String, BigDecimal> materialAidMap = new HashMap<>();
        Map<String, BigDecimal> calculatedMaterialAidMap = new HashMap<>();

        PaymentDeductionObject leaveDeduction;
        PaymentDeductionObject leaveDPayment;
        PaymentDeductionObject leaveMPayment;

        final SinglePayrunItem item = new SinglePayrunItem();
        final List<PaymentDeductionObject> paymentCategories = item.getPaymentCategories();
        final List<PaymentDeductionObject> deductionCategories = item.getDeductionCategories();
        final List<PaymentDeductionObject> taxCategories = item.getTaxCategories();
        final List<PaymentDeductionObject> employerContributionCategories = item.getEmployerContributionCategories();
        final List<PaymentDeductionObject> materialAidCategories = new ArrayList<>();
        final List<PaymentDeductionObject> customDeductionCategories = new ArrayList<>();

        item.setEmployeeID(employeeId);
        if (employee.getCitizenship() != null) {
            item.setCalculatePension(true);
            if (filter.getCountryId() != null) {
                item.setLocalEmployee(filter.getCountryId().equals(employee.getCitizenship().getObjectID()));
            }
        }
        final String employeeCode = employee.getProfile() != null && !StringUtil.isEmpty(employee.getProfile().getEmployeeCode()) ? employee.getProfile().getEmployeeCode() : "";

        boolean isEnableWithMiddle = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_FULLNAME_WITH_MIDDLENAME);
        if (filter.isEmpCodeAdjoined() && !StringUtil.isEmpty(employeeCode)) {
            item.setEmployee(employeeCode.concat(" -> ").concat(!isEnableWithMiddle ? employee.getFullName() : employee.getFormmattedName()));
        } else {
            item.setEmployee(!isEnableWithMiddle ? employee.getFullName() : employee.getFormmattedName());
        }
        item.setEmployeeCode(employeeCode);

        if (filter.isEmployeeCodeInteger() && StringUtils.isNotEmpty(employeeCode)) {
            try {
                item.setEmployeeNumber(Long.parseLong(employeeCode.replaceAll("\\D", "")));
            } catch (NumberFormatException ignored) {
            }
        }

        item.setFromDate(filter.getFromDate());
        item.setToDate(filter.getToDate());
        item.setEditable(true);

        final String rateType = employeeSettingsMap.get(RATE_TYPE);
        String regularOTRate = employeeSettingsMap.get(REGULAR_OVERTIME_RATE) != null ? employeeSettingsMap.get(REGULAR_OVERTIME_RATE) : "";
        String regularOTRateType = employeeSettingsMap.get(REGULAR_OVERTIME_RATE_TYPE) != null ? employeeSettingsMap.get(REGULAR_OVERTIME_RATE_TYPE) : "";
        String weekendOTRate = employeeSettingsMap.get(WEEKEND_OVERTIME_RATE) != null ? employeeSettingsMap.get(WEEKEND_OVERTIME_RATE) : "";
        String weekendOTRateType = employeeSettingsMap.get(WEEKEND_OVERTIME_RATE_TYPE) != null ? employeeSettingsMap.get(WEEKEND_OVERTIME_RATE_TYPE) : "";
        String holidayOTRate = employeeSettingsMap.get(HOLIDAY_OVERTIME_RATE) != null ? employeeSettingsMap.get(HOLIDAY_OVERTIME_RATE) : "";
        String holidayOTRateType = employeeSettingsMap.get(HOLIDAY_OVERTIME_RATE_TYPE) != null ? employeeSettingsMap.get(HOLIDAY_OVERTIME_RATE_TYPE) : "";

        ListingFilterParameter lfp = new ListingFilterParameter();
        lfp.setStartDate(filter.getFromDate().getNonConvertedDate());
        lfp.setEndDate(filter.getToDate().getNonConvertedDate());
        lfp.setEmployeeId(employeeId);

        BigDecimal numberOfWorkDay = BigDecimal.ZERO;
        Map<Date, BigDecimal> workHoursMap = new HashMap<>();
        if (isDailyRateByEmployerSettings) {
            numberOfWorkDay = new BigDecimal(getCompanyPayrollSettings(NUMBER_OF_WORK_DAYS, DEFAULT_NUMBER_OF_WORK_DAYS.toString()));
        } else if (isCalculationByTimeslot) {
            if (salaryModeByAttendanceReport) {
                workHoursMap = attendanceHoursManager.getAttendanceHours(lfp);
                BigDecimal workHours = shiftManager.getShiftHours(lfp.getStartDate(), employeeId);
                workHours = workHours != null ? workHours : BigDecimal.valueOf(168);
                numberOfWorkDay = workHours.divide(BigDecimal.valueOf(8), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            } else {
                workHoursMap = attendanceRawDataManager.getWorkingHours(lfp, AttendanceRawDataManagerImpl.WORKING_DATES);
                numberOfWorkDay = new BigDecimal(workHoursMap.size());
            }
        } else {
            numberOfWorkDay = BigDecimal.valueOf(filter.getDaysOfMonth());
        }
        item.setNumberOfWorkDay(numberOfWorkDay);

        if (employee.getStartDate() != null && employee.getStartDate().after(filter.getFromDate().getNonConvertedDate())) {
            item.setFromDate(new DateNonConvertable(employee.getStartDate()));
            lfp.setStartDate(employee.getStartDate());
            filter.setFromDate(new DateNonConvertable(employee.getStartDate()));
        } else {
            item.setFromDate(filter.getFromDate());
        }

        if (employee.getEndDate() != null && employee.getEndDate().before(filter.getToDate().getNonConvertedDate())) {
            item.setToDate(new DateNonConvertable(employee.getEndDate()));
            lfp.setEndDate(employee.getEndDate());
            filter.setToDate(new DateNonConvertable(employee.getEndDate()));
        } else {
            item.setToDate(filter.getToDate());
        }

        // Start Calculate worked days
        BigDecimal workedDays = BigDecimal.ZERO;
        if (isCalculationByTimeslot) {
            Set<Date> leaveDays = getLeaveRequestDaysByPeriod(employeeId, lfp.getStartDate(), lfp.getEndDate(), employee.getSalaryMode(), AttendanceRawDataManagerImpl.WORKING_DATES);
            BigDecimal workedHours = PayrollUtils.getNumberOfHours(lfp.getStartDate(), lfp.getEndDate(), workHoursMap, leaveDays);
            workedDays = workedHours.divide(BigDecimal.valueOf(8), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        } else {
            workedDays = BigDecimal.valueOf(ServerUtils.countDays(lfp.getStartDate(), lfp.getEndDate()) + 1);
            workedDays = item.getNumberOfWorkDay().min(workedDays);
        }
        item.setDaysWorked(workedDays);
        // End Calculate worked days

        // Allowance ratio
        BigDecimal allowanceRatio = BigDecimal.ONE;
        if (item.getDaysWorked().compareTo(item.getNumberOfWorkDay()) < 0) {
            try {
                allowanceRatio = item.getDaysWorked().divide(item.getNumberOfWorkDay(), 2, RoundingMode.HALF_UP);
            } catch (ArithmeticException e) {
                allowanceRatio = BigDecimal.ZERO;
            }
        }

        List<EmployeeSalary> employeeSalaries = new ArrayList<>();
        BigDecimal salary = calculateSalary(employeeId, filter, item.getNumberOfWorkDay(), workHoursMap, isCalculationByTimeslot, employeeSalaries, employee.getSalaryMode(), enabledLeavePayments);

        item.setBasicSalary(salary.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
        item.setCurrency(employee.getSalaryCurrency() != null ? employee.getSalaryCurrency().createCurrencyItem() : null);

        MonthlyOvertimeDataWithRates overtimeDataWithRates = null;
        List<MonthlyOvertimeData> overtimeData = null;
        if (FIXED_TIMESHEET_OVERTIME_RATE.equals(rateType) || FIXED_ATTENDANCE_REPORT_OVERTIME_RATE.equals(rateType)) {
            String overtimeCalculationType = getCompanyPayrollSettings(OVERTIME_RATE_BY_EMPLOYER_SETTINGS);
            overtimeDataWithRates = new MonthlyOvertimeDataWithRates();
            overtimeDataWithRates.setRateType(0);
            BigDecimal workedHours = BigDecimal.valueOf(0, ServerUtils.getCalculationScale());
            BigDecimal overtimeHours = BigDecimal.valueOf(0, ServerUtils.getCalculationScale());
            BigDecimal weekendOvertimeHours = BigDecimal.valueOf(0, ServerUtils.getCalculationScale());
            BigDecimal holidayOvertimeHours = BigDecimal.valueOf(0, ServerUtils.getCalculationScale());
            BigDecimal plannedHours = BigDecimal.valueOf(0, ServerUtils.getCalculationScale());
            boolean isBasedOnTimesheet = FIXED_TIMESHEET_OVERTIME_RATE.equals(rateType);
            BigDecimal timeslotHours = timesheetManager.getTimeslotHours(lfp);
            lfp.setBasedOnTimesheet(isBasedOnTimesheet);
            if ("WEEKLY".equals(overtimeCalculationType)) {
                List<Object[]> weeklyHours = timesheetManager.getTimesheetForWeeklyRate(lfp);
                if (weeklyHours != null && weeklyHours.get(0) != null) {
                    workedHours = (isBasedOnTimesheet ? (BigDecimal) weeklyHours.get(0)[0] : BigDecimal.valueOf((Double) weeklyHours.get(0)[0])).divide(new BigDecimal(60), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                    overtimeHours = (isBasedOnTimesheet ? (BigDecimal) weeklyHours.get(0)[1] : BigDecimal.valueOf((Double) weeklyHours.get(0)[1])).divide(new BigDecimal(60), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                    holidayOvertimeHours = (isBasedOnTimesheet ? (BigDecimal) weeklyHours.get(0)[2] : BigDecimal.valueOf((Double) weeklyHours.get(0)[2])).divide(new BigDecimal(60), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                    weekendOvertimeHours = (isBasedOnTimesheet ? (BigDecimal) weeklyHours.get(0)[3] : BigDecimal.valueOf((Double) weeklyHours.get(0)[3])).divide(new BigDecimal(60), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                    plannedHours = timeslotHours.divide(new BigDecimal(60), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                }
            } else {
                List<EdsAttendanceRawData> workedHoursForPayroll = timesheetManager.getHoursForPayrun(lfp);
                for (EdsAttendanceRawData rawData : workedHoursForPayroll) {
                    if (rawData.getHoliday()) {
                        holidayOvertimeHours = holidayOvertimeHours.add(BigDecimal.valueOf(rawData.getTimeSheet()));
                    } else if (rawData.getDayOff()) {
                        weekendOvertimeHours = weekendOvertimeHours.add(BigDecimal.valueOf(rawData.getTimeSheet()));
                    } else if (rawData.getLeave() > 0) {
                        weekendOvertimeHours = weekendOvertimeHours.add(BigDecimal.valueOf(rawData.getTimeSheet()));
                        workedHours = workedHours.add(BigDecimal.valueOf(rawData.getLeave()));
                    } else {
                        if ("MONTHLY".equals(overtimeCalculationType)) {
                            workedHours = workedHours.add(BigDecimal.valueOf(rawData.getTimeSheet()));
                        } else {
                            if (rawData.getTimeSheet() > rawData.getTimeSlot()) {
                                workedHours = workedHours.add(BigDecimal.valueOf(rawData.getTimeSlot()));
                                overtimeHours = overtimeHours.add(BigDecimal.valueOf(rawData.getTimeSheet() - rawData.getTimeSlot()));
                            } else {
                                workedHours = workedHours.add(BigDecimal.valueOf(rawData.getTimeSheet()));
                            }
                        }
                    }
                }
                plannedHours = timeslotHours;
                if ("MONTHLY".equals(overtimeCalculationType) && workedHours.compareTo(plannedHours) > 0) {
                    overtimeHours = workedHours.subtract(plannedHours);
                    workedHours = plannedHours;
                }
            }
            overtimeDataWithRates.setRate(item.getBasicSalary());
            overtimeDataWithRates.setPlannedHours(plannedHours.divide(BigDecimal.valueOf(60), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
            overtimeDataWithRates.setWorkedHours(workedHours.divide(BigDecimal.valueOf(60), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));

            overtimeDataWithRates.setHolidayOvertimeHours(holidayOvertimeHours.divide(BigDecimal.valueOf(60), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
            if (!holidayOTRate.isEmpty()) {
                BigDecimal holidayRate = new BigDecimal(holidayOTRate);
                overtimeDataWithRates.setHolidayOvertimeRate(holidayOTRateType.equals(PERCENTAGE) ? overtimeDataWithRates.getRate().multiply(holidayRate).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP) : holidayRate);
            }
            overtimeDataWithRates.setWeekendOvertimeHours(weekendOvertimeHours.divide(BigDecimal.valueOf(60), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
            if (!weekendOTRate.isEmpty()) {
                BigDecimal weekendRate = new BigDecimal(weekendOTRate);
                overtimeDataWithRates.setWeekendOvertimeRate(weekendOTRateType.equals(PERCENTAGE) ? overtimeDataWithRates.getRate().multiply(weekendRate).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP) : weekendRate);
            }
            overtimeDataWithRates.setOvertimeHours(overtimeHours.divide(BigDecimal.valueOf(60), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
            if (!regularOTRate.isEmpty()) {
                BigDecimal overtimeRate = new BigDecimal(regularOTRate);
                overtimeDataWithRates.setOvertimeRate(regularOTRateType.equals(PERCENTAGE) ? overtimeDataWithRates.getRate().multiply(overtimeRate).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP) : overtimeRate);
            }
            loadBasicSalaryWithOvertimeData(item, overtimeDataWithRates);
        } else if (TIMESHEET_ONLY_RATE.equals(rateType)) {
            BigDecimal timesheetSalary = timesheetManager.getApprovedTimeSpentInterval(employeeId,
                    item.getFromDate().getNonConvertedDate(),
                    item.getToDate().getNonConvertedDate(), filter.getProjectId());
            item.setBasicSalary(timesheetSalary);
        } else {
            overtimeData = monthlyTimesheetManager.getMonthlyTimesheetDataForPayroll(lfp);
        }
        item.setSalary(item.getBasicSalary());

        if (paymentDeductions != null && !paymentDeductions.isEmpty()) {
            BigDecimal[] nonTaxableDeductionsTotal = new BigDecimal[1];
            nonTaxableDeductionsTotal[0] = BigDecimal.ZERO;
            for (PaymentDeductionObject object : paymentDeductions) {
                object.setLoan(object.isDeductionCategory() && object.getStarttDate() != null && object.getEnddDate() == null);
                if (object.isLoan() && loadLoanData(object)) {
                    continue;
                }
                if (object.isPaymentCategory()) {
                    if (Integer.valueOf(3).equals(object.getType())) {
                        PaymentDeductionObject paymentObject = new PaymentDeductionObject();
                        paymentObject.setEmployee(object.getEmployee());
                        paymentObject.setCategoryItem(object.getCategoryItem());
                        paymentObject.setPaymentAmount(filter.getMrotValue().multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                        paymentObject.setPercentage(object.getPercentage());
                        paymentObject.setStarttDate(object.getStarttDate());
                        paymentObject.setEnddDate(object.getEnddDate());

                        ListingFilterParameter internalLfp = new ListingFilterParameter();
                        internalLfp.setEmployeeId(employeeId);
                        internalLfp.setStartDate(lfp.getStartDate());
                        internalLfp.setEndDate(lfp.getEndDate());
                        BigDecimal internalAllowanceRatio = BigDecimal.ONE;
                        if (object.getStarttDate() != null || object.getEnddDate() != null) {
                            if (object.getStarttDate() != null && lfp.getStartDate().before(object.getStarttDate().getNonConvertedDate())) {
                                internalLfp.setStartDate(object.getStarttDate().getNonConvertedDate());
                            }
                            if (object.getEnddDate() != null && lfp.getEndDate().after(object.getEnddDate().getNonConvertedDate())) {
                                internalLfp.setEndDate(object.getEnddDate().getNonConvertedDate());
                            }
                            // Start Calculate worked days
                            BigDecimal internalWorkedDays = BigDecimal.ZERO;
                            if (isCalculationByTimeslot) {
                                BigDecimal internalWorkedHours = PayrollUtils.getNumberOfHours(internalLfp.getStartDate(), internalLfp.getEndDate(), workHoursMap, null);
                                internalWorkedDays = internalWorkedHours.divide(BigDecimal.valueOf(8), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                            } else {
                                internalWorkedDays = BigDecimal.valueOf(ServerUtils.countDays(internalLfp.getStartDate(), internalLfp.getEndDate()));
                                internalWorkedDays = item.getNumberOfWorkDay().min(internalWorkedDays);
                            }
                            // End Calculate worked days

                            // Allowance ratio
                            if (internalWorkedDays.compareTo(item.getNumberOfWorkDay()) < 0) {
                                try {
                                    internalAllowanceRatio = internalWorkedDays.divide(item.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                                } catch (ArithmeticException e) {
                                    internalAllowanceRatio = BigDecimal.ZERO;
                                }
                            }
                            //End of Allowance Ratio
                        }
                        BigDecimal paymentAmount = this.loadRecurringPaymentObjectData(BigDecimal.ZERO, item.getNumberOfWorkDay(), paymentObject, overtimeData, internalAllowanceRatio);
                        paymentsTotal = paymentsTotal.add(paymentAmount);
                        if (paymentObject.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                            paymentObject.setRemarks(Utils.formatDate(internalLfp.getStartDate(), company) + " - " + Utils.formatDate(internalLfp.getEndDate(), company));
                            paymentCategories.add(paymentObject);
                        }
                        if (paymentObject.getCategoryItem() != null && !paymentObject.getCategoryItem().isExcludeInCustomDeductions()) {
                            customPaymentsTotal = customPaymentsTotal.add(paymentAmount);
                        }
                    } else {
                        for (EmployeeSalary employeeSalary : employeeSalaries) {
                            PaymentDeductionObject paymentObject = new PaymentDeductionObject();
                            paymentObject.setEmployee(object.getEmployee());
                            paymentObject.setCategoryItem(object.getCategoryItem());
                            paymentObject.setPaymentAmount(object.getPaymentAmount());
                            paymentObject.setPercentage(object.getPercentage());
                            paymentObject.setStarttDate(object.getStarttDate());
                            paymentObject.setEnddDate(object.getEnddDate());

                            ListingFilterParameter internalLfp = new ListingFilterParameter();
                            internalLfp.setEmployeeId(employeeId);
                            internalLfp.setStartDate(employeeSalary.getFromDate());
                            internalLfp.setEndDate(employeeSalary.getToDate());
                            BigDecimal internalAllowanceRatio = BigDecimal.ONE;
                            BigDecimal calculatedSalaryForPeriod = employeeSalary.calculateSalary(item.getNumberOfWorkDay());
                            if (object.getStarttDate() == null) {
                                object.setStarttDate(new DateNonConvertable(internalLfp.getStartDate()));
                            }
                            if (object.getEnddDate() == null) {
                                object.setEnddDate(new DateNonConvertable(internalLfp.getEndDate()));
                            }
                            if (object.getStarttDate() != null || object.getEnddDate() != null) {
                                if (object.getStarttDate() != null && internalLfp.getStartDate().before(object.getStarttDate().getNonConvertedDate())) {
                                    internalLfp.setStartDate(object.getStarttDate().getNonConvertedDate());
                                }
                                if (object.getEnddDate() != null && internalLfp.getEndDate().after(object.getEnddDate().getNonConvertedDate())) {
                                    internalLfp.setEndDate(object.getEnddDate().getNonConvertedDate());
                                }
                                // Start Calculate worked days
                                BigDecimal internalWorkedDays = BigDecimal.ZERO;
                                if (isCalculationByTimeslot) {
                                    Set<Date> internalLeaveDays = getLeaveRequestDaysByPeriod(employeeId, internalLfp.getStartDate(), internalLfp.getEndDate(), employee.getSalaryMode(), AttendanceRawDataManagerImpl.WORKING_DATES);
                                    BigDecimal internalWorkedHours = PayrollUtils.getNumberOfHours(internalLfp.getStartDate(), internalLfp.getEndDate(), workHoursMap, internalLeaveDays);
                                    internalWorkedDays = internalWorkedHours.divide(BigDecimal.valueOf(8), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                                } else {
                                    internalWorkedDays = BigDecimal.valueOf(ServerUtils.countDays(internalLfp.getStartDate(), internalLfp.getEndDate()) + 1);
                                    internalWorkedDays = item.getNumberOfWorkDay().min(internalWorkedDays);
                                }
                                // End Calculate worked days

                                // Allowance ratio
                                if (internalWorkedDays.compareTo(item.getNumberOfWorkDay()) < 0) {
                                    try {
                                        internalAllowanceRatio = internalWorkedDays.divide(item.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                                    } catch (ArithmeticException e) {
                                        internalAllowanceRatio = BigDecimal.ZERO;
                                    }
                                }
                                //End of Allowance Ratio
                                calculatedSalaryForPeriod = employeeSalary.calculateSalary(internalWorkedDays, item.getNumberOfWorkDay());
                            }
                            BigDecimal paymentAmount = this.loadRecurringPaymentObjectData(calculatedSalaryForPeriod, item.getNumberOfWorkDay(), paymentObject, overtimeData, internalAllowanceRatio);
                            paymentsTotal = paymentsTotal.add(paymentAmount);
                            if (paymentObject.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
                                if (internalLfp.getStartDate() != null && internalLfp.getEndDate() != null) {
                                    paymentObject.setRemarks(Utils.formatDate(internalLfp.getStartDate(), company) + " - " + Utils.formatDate(internalLfp.getEndDate(), company));
                                }
                                paymentCategories.add(paymentObject);
                            }
                            if (paymentObject.getCategoryItem() != null && !paymentObject.getCategoryItem().isExcludeInCustomDeductions()) {
                                customPaymentsTotal = customPaymentsTotal.add(paymentAmount);
                            }
                        }
                    }
                } else if (object.isTaxCategory()) {
                    taxCategories.add(object);
                    collectLinkedCategories(object);
                    if (object.getPaymentAmount() != null) {
                        object.setPaymentAmount(object.getPaymentAmount().multiply(allowanceRatio));
                        taxTotal = taxTotal.add(object.getPaymentAmount());
                    } else if (object.getPercentage() != null &&
                            !object.isFromAllAllowances() &&
                            object.getLinkedCategories().isEmpty()) {
                        BigDecimal amount = item.getBasicSalary().multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                        object.setAmount(amount);
                        object.setPaymentAmount(amount);
                        taxTotal = taxTotal.add(object.getPaymentAmount());
                    }
                } else if (object.isEmployerContributionCategory()) {
                    employerContributionCategories.add(object);
                    if (object.getPaymentAmount() != null) {
                        object.setPaymentAmount(object.getPaymentAmount().multiply(allowanceRatio));
                        employerContributionTotal = employerContributionTotal.add(object.getPaymentAmount());
                    } else if (object.getPercentage() != null &&
                            !object.isFromAllAllowances() &&
                            object.getLinkedCategories().isEmpty()) {
                        BigDecimal amount = item.getBasicSalary().multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                        object.setAmount(amount);
                        object.setPaymentAmount(amount);
                        employerContributionTotal = employerContributionTotal.add(object.getPaymentAmount());
                    }
                } else if (object.isDeductionCategory()) {
                    BigDecimal deductionTax = object.getTax() != null ? object.getTax() : BigDecimal.ZERO;
                    BigDecimal deduction = object.getDeduction() != null ? object.getDeduction() : BigDecimal.ZERO;
                    if (object.getPaymentAmount() != null && (object.getType() == null ||
                            !object.getType().equals(PayrollConstants.LINKED_TYPE_MINIMUM_WAGE))) {//not minimum wage type
                        object.setPaymentAmount(object.getPaymentAmount().subtract(deduction).subtract(deductionTax));
                        deductionsTotal = deductionsTotal.add(object.getPaymentAmount());
                    } else if (object.getPercentage() != null &&
                            !object.isFromAllAllowances() &&
                            object.getLinkedCategories().isEmpty() &&
                            !object.getType().equals(PayrollConstants.LINKED_TYPE_PERCENTAGE_OF_BASIC_AND_ALLOWANCE_AFTER_TAX)) {
                        BigDecimal baseAmount = item.getBasicSalary();
                        //minimum wage
                        if (object.getType().equals(PayrollConstants.LINKED_TYPE_MINIMUM_WAGE)) {
                            baseAmount = filter.getMrotValue();
                        }
                        BigDecimal amount = baseAmount.multiply(object.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                        object.setAmount(amount.subtract(deductionTax));
                        object.setPaymentAmount(amount.subtract(deductionTax));
                        deductionsTotal = deductionsTotal.add(object.getPaymentAmount());
                    }
                    if (object.getPercentage() != null && object.getType().equals(PayrollConstants.LINKED_TYPE_PERCENTAGE_OF_BASIC_AND_ALLOWANCE_AFTER_TAX)) {
                        customDeductionCategories.add(object);
                    } else {
                        deductionCategories.add(object);
                    }
                    if (object.getPaymentAmount() != null && !object.getCategoryItem().getTaxable()) {
                        nonTaxableDeductionsTotal[0] = nonTaxableDeductionsTotal[0].add(object.getPaymentAmount());
                    }
                } else if (object.isMaterialAidCategory()) {
                    materialAidCategories.add(object);
                    if (object.getCategoryItem() != null) {
                        PaymentDeductionSelectItem materialAidCategory = object.getCategoryItem();

                        BigDecimal value = materialAidMap.getOrDefault(materialAidCategory.getSystemCode(), BigDecimal.ZERO);
                        value = value.add(object.getPaymentAmount());
                        materialAidMap.put(materialAidCategory.getSystemCode(), value);
                        if (!materialAidCategory.isNonMoneyType()) {
                            materialAidTotal = materialAidTotal.add(object.getPaymentAmount());
                        }
                    }
                }
            }

            //custom deductions logic
            for (PaymentDeductionObject customDeduction : customDeductionCategories) {
                BigDecimal internalAllowanceRatio = BigDecimal.ONE;
                ListingFilterParameter internalLfp = new ListingFilterParameter();
                internalLfp.setEmployeeId(employeeId);
                internalLfp.setStartDate(lfp.getStartDate());
                internalLfp.setEndDate(lfp.getEndDate());
                if (customDeduction.getStarttDate() != null && internalLfp.getStartDate().before(customDeduction.getStarttDate().getNonConvertedDate())
                        || customDeduction.getEnddDate() != null && internalLfp.getEndDate().after(customDeduction.getEnddDate().getNonConvertedDate())) {

                    if (customDeduction.getStarttDate() != null && internalLfp.getStartDate().before(customDeduction.getStarttDate().getNonConvertedDate())) {
                        internalLfp.setStartDate(customDeduction.getStarttDate().getNonConvertedDate());
                    }
                    if (customDeduction.getEnddDate() != null && internalLfp.getEndDate().after(customDeduction.getEnddDate().getNonConvertedDate())) {
                        internalLfp.setEndDate(customDeduction.getEnddDate().getNonConvertedDate());
                    }
                    // Start Calculate worked days
                    int internalWorkedDays = 0;
                    if (isCalculationByTimeslot) {
                        internalWorkedDays = attendanceRawDataManager.getWorkingDays(internalLfp, AttendanceRawDataManagerImpl.WORKING_DATES).size();
                    } else {
                        internalWorkedDays = ServerUtils.countDays(internalLfp.getStartDate(), internalLfp.getEndDate());
                        internalWorkedDays = Math.min(item.getNumberOfWorkDay().intValue(), internalWorkedDays);
                    }
                    // End Calculate worked days

                    // Allowance ratio
                    if (internalWorkedDays < item.getNumberOfWorkDay().intValue()) {
                        try {
                            internalAllowanceRatio = BigDecimal.valueOf(internalWorkedDays).divide(item.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                        } catch (ArithmeticException e) {
                            internalAllowanceRatio = BigDecimal.ZERO;
                        }
                    }
                    //End of Allowance Ratio
                }

                customDeduction.setPaymentAmount(item.getBasicSalary().add(customPaymentsTotal).subtract(taxTotal)
                        .multiply(customDeduction.getPercentage()).multiply(internalAllowanceRatio)
                        .divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
                customDeduction.setRemarks(Utils.formatDate(internalLfp.getStartDate(), company) + " - " + Utils.formatDate(internalLfp.getEndDate(), company));
                deductionCategories.add(customDeduction);
            }
            deductionsTotal = deductionsTotal.add(loadLinkedDeductionCategoriesData(item, paymentsTotal, nonTaxableDeductionsTotal));
            employerContributionTotal = employerContributionTotal.add(loadLinkedEmployerContributionCategoriesData(item, paymentsTotal));
            taxTotal = taxTotal.add(loadLinkedTaxCategoriesData(item, paymentsTotal, nonTaxableDeductionsTotal));

            // Non money type
            if (!CollectionUtils.isEmpty(item.getPaymentCategories()) && !CollectionUtils.isEmpty(item.getTaxCategories())) {
                for (PaymentDeductionObject payDeduction : item.getPaymentCategories()) {
                    if (payDeduction.getCategoryItem() != null && payDeduction.getCategoryItem().isNonMoneyType()) {
                        Integer categoryId = payDeduction.getCategoryItem().getId();
                        for (PaymentDeductionObject taxPayDeduction : item.getTaxCategories()) {
                            if (taxPayDeduction.getCategoryItem() != null && taxPayDeduction.getPercentage() != null) {
                                if (categoryId.equals(taxPayDeduction.getCategoryItem().getId())) {
                                    BigDecimal payAmount = payDeduction.getPaymentAmount() != null ? payDeduction.getPaymentAmount() : BigDecimal.ZERO;
                                    BigDecimal amount = payAmount.multiply(taxPayDeduction.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                                    taxPayDeduction.setPaymentAmount(taxPayDeduction.getPaymentAmount().add(amount));
                                    taxTotal = taxTotal.add(amount);
                                } else if (!CollectionUtils.isEmpty(taxPayDeduction.getLinkedCategories())) {
                                    for (PaymentDeductionObject taxLinkedCategory : taxPayDeduction.getLinkedCategories()) {
                                        if (taxLinkedCategory.getCategoryItem() != null && categoryId.equals(taxLinkedCategory.getCategoryItem().getId())) {
                                            BigDecimal payAmount = payDeduction.getPaymentAmount() != null ? payDeduction.getPaymentAmount() : BigDecimal.ZERO;
                                            BigDecimal amount = payAmount.multiply(taxPayDeduction.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                                            taxPayDeduction.setPaymentAmount(taxPayDeduction.getPaymentAmount().add(amount));
                                            taxTotal = taxTotal.add(amount);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // compute tax for over beneficial limit of financial aid categories (per employee)
            if (BigDecimal.ZERO.compareTo(materialAidTotal) < 0 && !taxCategories.isEmpty()) {
                //get the first tax category with percentage
                for (Map.Entry<String, BigDecimal> entry : materialAidMap.entrySet()) {
                    String systemCode = entry.getKey();
                    BigDecimal paidAmount = entry.getValue();
                    BigDecimal currentMrotValue = Optional.ofNullable(filter.getMrotValue()).orElse(BigDecimal.ZERO);

                    BigDecimal materialAidBalance = calculateMaterialAidBalance(employee, employeeSettingsMap, systemCode, currentMrotValue, filter.getToDate().getNonConvertedDate());
                    BigDecimal taxableAmount = paidAmount.subtract(materialAidBalance);

                    if (taxableAmount.compareTo(BigDecimal.ZERO) > 0) {
                        calculatedMaterialAidMap.put(systemCode, taxableAmount);
                    }
                }

                for (PaymentDeductionObject taxCategory : taxCategories) {
                    if (taxCategory.getPercentage() != null) {
                        for (Map.Entry<String, BigDecimal> entry : calculatedMaterialAidMap.entrySet()) {
                            BigDecimal taxAmount = entry.getValue().multiply(taxCategory.getPercentage()).divide(BigDecimal.valueOf(100), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                            taxCategory.setPaymentAmount(taxCategory.getPaymentAmount().add(taxAmount));
                            taxTotal = taxTotal.add(taxAmount);
                        }
                    }
                }
                paymentCategories.addAll(materialAidCategories);
                paymentsTotal = paymentsTotal.add(materialAidTotal);
            }
        }

        if (enabledLeaveDeductions && leaveDeductionCategoryItem != null && !TIMESHEET_ONLY_RATE.equals(rateType)) {
            leaveDeduction = new PaymentDeductionObject();
            leaveDeduction.setCategoryItem(leaveDeductionCategoryItem);
            leaveDeduction.setNumberOfWorkDays(item.getNumberOfWorkDay());
            leaveDeduction.setLeaveType(leaveDeductType);
            if (leaveDeductType == 1) {
                leaveDeduction.setLinkedCategories(filter.getLeaveDeductionLinkedCategories());
            }
            lfp.setType(NONE);
            lfp.setPaymentsTotal(paymentsTotal);

            this.loadLeaveDeductionData(item, lfp, leaveDeduction);
            deductionsTotal = deductionsTotal.add(leaveDeduction.getPaymentAmount());

            if (isNonPaidLeaveDaysImpact) {
                BigDecimal diff = this.deductPaymentsByNonPaidLeaveDays(item, leaveDeduction);
                paymentsTotal = paymentsTotal.add(diff);
            }
        }

        if (enabledLeavePayments && leaveDTCategoryItem != null && !TIMESHEET_ONLY_RATE.equals(rateType)) {
            leaveDPayment = new PaymentDeductionObject();
            leaveDPayment.setCategoryItem(leaveDTCategoryItem);
            leaveDPayment.setLeaveType(leaveDailyType);
            if (leaveDailyType == 1) {
                leaveDPayment.setLinkedCategories(filter.getLeaveDailyTypeLinkedCategories());
            }
            leaveMPayment = new PaymentDeductionObject();
            leaveMPayment.setCategoryItem(filter.getLeaveMTCategoryItem());
            leaveMPayment.setLeaveType(leaveMoneyType);

            if (leaveMoneyType == 1) {
                leaveMPayment.setLinkedCategories(filter.getLeaveMoneyTypeLinkedCategories());
            }
            lfp.setType(null);
            lfp.setLeaveDaysImpact(isPaidLeaveDaysImpact);
            BigDecimal diff = !filter.isLeaveSettingsCalculationEnabled() ? this.loadCurrentMonthPaidLeavesData(item, lfp, leaveDPayment, leaveMPayment, overtimeDataWithRates) : BigDecimal.ZERO;
            paymentsTotal = paymentsTotal.add(diff);

            lfp.setActualDue(true);
            diff = this.deductLastMonthPaidLeaves(item, lfp, leaveDPayment, leaveMPayment);
            paymentsTotal = item.getPaymentCategories().stream().map(PaymentDeductionObject::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        if (benefitCategoryItem != null) {
            BigDecimal diff = this.loadBenefitPaymentData(item, lfp, filter.getBaseCurrencyId(), benefitCategoryItem);
            paymentsTotal = paymentsTotal.add(diff);
        }

        item.setEmployeeExpenses(this.getExpensePaymentData(lfp, expenseCategoryItem));
        if (item.getEmployeeExpenses() != null) {
            expensesTotal = item.getEmployeeExpenses().getPaymentAmount();
        }

        if (FIXED_TIMESHEET_OVERTIME_RATE.equals(rateType) || FIXED_ATTENDANCE_REPORT_OVERTIME_RATE.equals(rateType)) {
            overtimeTotal = getProfileMeOTPayments(overtimeDataWithRates,
                    regularOTCategoryItem,
                    weekendOTCategoryItem,
                    holidayOTCategoryItem,
                    paymentCategories);
        } else if (FIXED_OVERTIME_RATE.equals(rateType)) {
            PaymentDeductionObject additionalPaymentObject = getAdditionalPaymentData(item, overtimeData, additionalPaymentCategoryItem);
            if (additionalPaymentObject != null) {
                paymentCategories.add(additionalPaymentObject);
                paymentsTotal = paymentsTotal.add(additionalPaymentObject.getPaymentAmount());
            }

            if (overtimeData != null) {
                PaymentDeductionObject overtimePayment = getFixedOTPayment(item,
                        numberOfWorkDay,
                        regularOTCategoryItem,
                        employeeSettingsMap.get(REGULAR_OVERTIME_RATE),
                        employeeSettingsMap.get(REGULAR_OVERTIME_RATE_TYPE),
                        overtimeData, REGULAR_OVERTIME);
                if (overtimePayment != null) {
                    paymentCategories.add(overtimePayment);
                    overtimeTotal = overtimeTotal.add(overtimePayment.getPaymentAmount());
                }
                overtimePayment = getFixedOTPayment(item,
                        numberOfWorkDay,
                        weekendOTCategoryItem,
                        employeeSettingsMap.get(WEEKEND_OVERTIME_RATE),
                        employeeSettingsMap.get(WEEKEND_OVERTIME_RATE_TYPE),
                        overtimeData,
                        WEEKEND_OVERTIME);
                if (overtimePayment != null) {
                    paymentCategories.add(overtimePayment);
                    overtimeTotal = overtimeTotal.add(overtimePayment.getPaymentAmount());
                }
                overtimePayment = getFixedOTPayment(item,
                        numberOfWorkDay,
                        holidayOTCategoryItem,
                        employeeSettingsMap.get(HOLIDAY_OVERTIME_RATE),
                        employeeSettingsMap.get(HOLIDAY_OVERTIME_RATE_TYPE),
                        overtimeData,
                        HOLIDAY_OVERTIME);
                if (overtimePayment != null) {
                    paymentCategories.add(overtimePayment);
                    overtimeTotal = overtimeTotal.add(overtimePayment.getPaymentAmount());
                }

                overtimeTotal = overtimeTotal.add(calculatePastMonthUnpaidSalary(item, lfp, isDailyRateByEmployerSettings, numberOfWorkDay));

                BigDecimal totalOvertime = getWorkedDayTotal(overtimeData);
                if (totalOvertime.compareTo(item.getNumberOfWorkDay()) < 0) {
                    item.setBasicSalary(item.getBasicSalary().add(item.getBasicSalary()
                            .multiply(totalOvertime)
                            .divide(item.getNumberOfWorkDay(), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)));
                }
            }
        } else if (FIXED_HRMS_OVERTIME_RATE.equals(rateType)) {
            List<DailyOvertimeData> dailyOverTimeData = attendanceRawDataManager.getDailyOvertimeData(lfp);

            if (dailyOverTimeData != null && !dailyOverTimeData.isEmpty()) {
                BigDecimal regularOTHours = BigDecimal.ZERO;
                BigDecimal holidayOTHours = BigDecimal.ZERO;
                BigDecimal weekendOTHours = BigDecimal.ZERO;
                BigDecimal absenceHours = BigDecimal.ZERO;
                BigDecimal timeslotHours = BigDecimal.ZERO;
                for (DailyOvertimeData dailyData : dailyOverTimeData) {
                    if (dailyData.getOvertimeHour() != null) {
                        if (dailyData.isHoliday()) {
                            holidayOTHours = holidayOTHours.add(dailyData.getOvertimeHour());
                        } else if (dailyData.isDayOff()) {
                            weekendOTHours = weekendOTHours.add(dailyData.getOvertimeHour());
                        } else {
                            regularOTHours = regularOTHours.add(dailyData.getOvertimeHour());
                        }
                    }

                    if (dailyData.getAbsenceHour() != null) {
                        absenceHours = absenceHours.add(dailyData.getAbsenceHour());
                    }
                    if (dailyData.getTimeslotHour() != null) {
                        timeslotHours = timeslotHours.add(dailyData.getTimeslotHour());
                    }
                }

                List<PaymentDeductionObject> userDefaultPaymentCategories = new ArrayList<>(paymentCategories);

                PaymentDeductionObject overtimeOrAbsencePaymentDeduction =
                        isCalculationByTimeslot ? calculateAbsenceOrOvertimeByTimeslot(regularOTCategoryItem, userDefaultPaymentCategories, item.getBasicSalary(), regularOTHours, timeslotHours, employeeSettingsMap.get(REGULAR_OVERTIME_RATE), employeeSettingsMap.get(REGULAR_OVERTIME_RATE_TYPE))
                                : getFixedHrmsOTPayment(regularOTCategoryItem, numberOfWorkDay, item.getBasicSalary().add(paymentsTotal), regularOTHours, employeeSettingsMap.get(REGULAR_OVERTIME_RATE), employeeSettingsMap.get(REGULAR_OVERTIME_RATE_TYPE));
                if (overtimeOrAbsencePaymentDeduction != null) {
                    paymentCategories.add(overtimeOrAbsencePaymentDeduction);
                    overtimeTotal = overtimeTotal.add(overtimeOrAbsencePaymentDeduction.getPaymentAmount());
                }
                overtimeOrAbsencePaymentDeduction =
                        isCalculationByTimeslot ? calculateAbsenceOrOvertimeByTimeslot(holidayOTCategoryItem, userDefaultPaymentCategories, item.getBasicSalary(), weekendOTHours, timeslotHours, employeeSettingsMap.get(WEEKEND_OVERTIME_RATE), employeeSettingsMap.get(WEEKEND_OVERTIME_RATE_TYPE))
                                : getFixedHrmsOTPayment(weekendOTCategoryItem, numberOfWorkDay, item.getBasicSalary().add(paymentsTotal), weekendOTHours, employeeSettingsMap.get(WEEKEND_OVERTIME_RATE), employeeSettingsMap.get(WEEKEND_OVERTIME_RATE_TYPE));
                if (overtimeOrAbsencePaymentDeduction != null) {
                    paymentCategories.add(overtimeOrAbsencePaymentDeduction);
                    overtimeTotal = overtimeTotal.add(overtimeOrAbsencePaymentDeduction.getPaymentAmount());
                }
                overtimeOrAbsencePaymentDeduction =
                        isCalculationByTimeslot ? calculateAbsenceOrOvertimeByTimeslot(holidayOTCategoryItem, userDefaultPaymentCategories, item.getBasicSalary(), holidayOTHours, timeslotHours, employeeSettingsMap.get(HOLIDAY_OVERTIME_RATE), employeeSettingsMap.get(HOLIDAY_OVERTIME_RATE_TYPE))
                                : getFixedHrmsOTPayment(holidayOTCategoryItem, numberOfWorkDay, item.getBasicSalary().add(paymentsTotal), holidayOTHours, employeeSettingsMap.get(HOLIDAY_OVERTIME_RATE), employeeSettingsMap.get(HOLIDAY_OVERTIME_RATE_TYPE));
                if (overtimeOrAbsencePaymentDeduction != null) {
                    paymentCategories.add(overtimeOrAbsencePaymentDeduction);
                    overtimeTotal = overtimeTotal.add(overtimeOrAbsencePaymentDeduction.getPaymentAmount());
                }

                overtimeOrAbsencePaymentDeduction = calculateAbsenceOrOvertimeByTimeslot(absenceDeductionItem, userDefaultPaymentCategories, item.getBasicSalary(), absenceHours, timeslotHours, "100", PERCENTAGE);
                if (overtimeOrAbsencePaymentDeduction != null) {
                    deductionCategories.add(overtimeOrAbsencePaymentDeduction);
                    deductionsTotal = deductionsTotal.add(overtimeOrAbsencePaymentDeduction.getPaymentAmount());
                }
            }
        }
        BigDecimal leavePaymentsTotal = BigDecimal.ZERO;

        if (filter.isLeaveSettingsCalculationEnabled() && !TIMESHEET_ONLY_RATE.equals(rateType)) {
            calculateDataForCustomAnnualLeaveCustomisation(employeeId,
                    lfp,
                    paymentCategories,
                    item,
                    leavePaymentsTotal,
                    filter.getSpentMinutes(),
                    filter.getLastYearMinutes(),
                    filter.getMonth(),
                    filter.getYear());
        }
        BigDecimal additionalPay = calculatePayslipEmployeeBonus(employeeId, item.getBasicSalary());

        item.setAdditionalPay(additionalPay);
        if (Optional.ofNullable(item.getAdditionalPay()).orElse(BigDecimal.ZERO).compareTo(BigDecimal.ZERO) > 0 && bonusCategoryItem != null) {
            PaymentDeductionObject bonus = new PaymentDeductionObject();
            bonus.setCategoryItem(bonusCategoryItem);
            bonus.setPaymentAmount(item.getAdditionalPay());
            paymentCategories.add(bonus);
            paymentsTotal = paymentsTotal.add(item.getAdditionalPay());
        }
        BigDecimal pensionAmount = BigDecimal.ZERO;
        if (filter.isCalculatePension()) {
            pensionAmount = loadPensionCategoriesAndGetAmount(item, filter.getCountryCode());
        }
        item.setExpense(BigDecimal.ZERO);
        item.setComission(BigDecimal.ZERO);
        item.setMonthlyCollection(BigDecimal.ZERO);
        item.setSpentFlueAmount(BigDecimal.ZERO);

        paymentsTotal = paymentsTotal.add(overtimeTotal).add(leavePaymentsTotal);

        //basic salary category
        EdsPayrollCategory basicSalayCategory;
        String basicSalarySettings = getEmployeeSettingValue(employee.getObjectID(), SALARY_CATEGORY);
        if (basicSalarySettings != null) {
            basicSalayCategory = categoryManager.get(Integer.parseInt(basicSalarySettings));
        } else {
            basicSalayCategory = categoryManager.getCategoryByCode(BASIC_SALARY);
        }

        for (EmployeeSalary employeeSalary : employeeSalaries) {
            PaymentDeductionObject basicSalary = new PaymentDeductionObject();
            basicSalary.setPaymentAmount(employeeSalary.calculateSalary(item.getNumberOfWorkDay()));
            basicSalary.setCategoryItem(basicSalayCategory.createPaymentDeductionSelectItem());
            if (employeeSalary.getFromDate() != null && employeeSalary.getToDate() != null) {
                basicSalary.setRemarks(Utils.formatDate(employeeSalary.getFromDate(), company) + " - " + Utils.formatDate(employeeSalary.getToDate(), company));
            }
            basicSalary.setSalaryObject(true);
            item.getPaymentCategories().add(basicSalary);
        }
        BigDecimal total = item.getBasicSalary();
        total = total.add(paymentsTotal);
        item.setGross(item.getBasicSalary().add(paymentsTotal));
        total = total.add(expensesTotal);//expenses
        total = total.subtract(deductionsTotal);
        total = total.subtract(taxTotal);
        total = total.subtract(pensionAmount);

        item.setActualMonthPay(item.getSalary());
        item.setAllowance(paymentsTotal);
      if (item.getPensionType() != null) {
            if (item.isLocalEmployee()) {
                if (item.getPensionRate() != null && BigDecimal.ZERO.compareTo(item.getPensionRate()) < 0) {
                    if (item.getPensionAllowances() != null && !item.getPensionAllowances().isEmpty()) {
                        item.setPensionAmount(this.getPensionAmount(item, item.getPensionRate()));
                    } else {
                        item.setPensionAmount(item.getBasicSalary()
                                .multiply(item.getPensionRate())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    }
                }
                if (item.getCompanyPensionRate() != null && BigDecimal.ZERO.compareTo(item.getCompanyPensionRate()) < 0) {
                    if (item.getPensionAllowances() != null && !item.getPensionAllowances().isEmpty()) {
                        item.setCompanyPensionAmount(this.getPensionAmount(item, item.getCompanyPensionRate()));
                    } else {
                        item.setCompanyPensionAmount(item.getBasicSalary()
                                .multiply(item.getCompanyPensionRate())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

                    }
                }
            } else {
                if (item.getNonLocalPensionRate() != null && BigDecimal.ZERO.compareTo(item.getNonLocalPensionRate()) < 0) {
                    if (item.getPensionAllowances() != null && !item.getPensionAllowances().isEmpty()) {
                        item.setPensionAmount(this.getPensionAmount(item, item.getNonLocalPensionRate()));
                    } else {
                        item.setPensionAmount(item.getBasicSalary().multiply(item.getNonLocalPensionRate())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    }
                }
                if (item.getCompanyNonLocalPensionRate() != null && BigDecimal.ZERO.compareTo(item.getCompanyNonLocalPensionRate()) < 0) {
                    if (item.getPensionAllowances() != null && !item.getPensionAllowances().isEmpty()) {
                        item.setCompanyPensionAmount(this.getPensionAmount(item, item.getCompanyNonLocalPensionRate()));

                    } else {
                        item.setCompanyPensionAmount(item.getBasicSalary()
                                .multiply(item.getCompanyNonLocalPensionRate())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    }
                }
            }
        }
        item.setDeduction(deductionsTotal);
        item.setEmployerContribution(employerContributionTotal);
        item.setTax(taxTotal);
        item.setTotal(total);
        return item;
    }

    private BigDecimal loadPensionCategoriesAndGetAmount(final SinglePayrunItem item, final String countryCode) {
        final EdsPensionScheme pensionScheme = pensionSchemeManager.getPensionSchema(countryCode != null ? countryCode : "");
        if (pensionScheme == null) {
            return BigDecimal.ZERO;
        }
        item.setPensionType(pensionScheme.getDeductionType());
        item.setCompanyPensionType(pensionScheme.getEmployerDeductionType());
        item.setPensionRate(pensionScheme.getDeductionValue());
        item.setNonLocalPensionRate(pensionScheme.getNonLocalDeductionValue());
        item.setCompanyPensionRate(pensionScheme.getEmployerDeductionValue());
        item.setCompanyNonLocalPensionRate(pensionScheme.getEmployerNonLocalDeductionValue());
        item.setPensionValueType(pensionScheme.getDeductFrom());
        item.setEmpMaxTaxableAmount(pensionScheme.getEmpMaxTaxableAmount());
        item.setCompMaxTaxableAmount(pensionScheme.getCompMaxTaxableAmount());
        if (pensionScheme.getCategories() != null && pensionScheme.getCategories().size() > 0) {
            for (EdsPayrollCategory category : pensionScheme.getCategories()) {
                item.getPensionAllowances().add(category.createPaymentDeductionSelectItem());
            }
        }
        if (!item.isCalculatePension()) {
            return BigDecimal.ZERO;
        }

        if (item.getPensionType() != null && item.getPensionType() == 0) {
            if (item.isLocalEmployee() && item.getPensionRate() != null) {
                item.setPensionAmount(item.getPensionRate());
                item.setCompanyPensionAmount(item.getCompanyPensionRate());
            } else if (!item.isLocalEmployee() && item.getNonLocalPensionRate() != null) {
                item.setPensionAmount(item.getNonLocalPensionRate());
                item.setCompanyPensionAmount(item.getCompanyNonLocalPensionRate());
            }
        } else if (item.getPensionType() != null) {
            if (item.isLocalEmployee()) {
                if (item.getPensionRate() != null && BigDecimal.ZERO.compareTo(item.getPensionRate()) < 0) {
                    if (item.getPensionAllowances() != null && !item.getPensionAllowances().isEmpty()) {
                        item.setPensionAmount(this.getPensionAmount(item, item.getPensionRate()));
                    } else {
                        item.setPensionAmount(item.getBasicSalary()
                                .multiply(item.getPensionRate())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    }
                }
                if (item.getCompanyPensionRate() != null && BigDecimal.ZERO.compareTo(item.getCompanyPensionRate()) < 0) {
                    if (item.getPensionAllowances() != null && item.getPensionAllowances().size() > 0) {
                        item.setCompanyPensionAmount(this.getPensionAmount(item, item.getCompanyPensionRate()));
                    } else {
                        item.setCompanyPensionAmount(item.getBasicSalary()
                                .multiply(item.getCompanyPensionRate())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));

                    }
                }
            } else {
                if (item.getNonLocalPensionRate() != null && BigDecimal.ZERO.compareTo(item.getNonLocalPensionRate()) < 0) {
                    if (item.getPensionAllowances() != null && !item.getPensionAllowances().isEmpty()) {
                        item.setPensionAmount(this.getPensionAmount(item, item.getNonLocalPensionRate()));
                    } else {
                        item.setPensionAmount(item.getBasicSalary().multiply(item.getNonLocalPensionRate())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    }
                }
                if (item.getCompanyNonLocalPensionRate() != null && BigDecimal.ZERO.compareTo(item.getCompanyNonLocalPensionRate()) < 0) {
                    if (item.getPensionAllowances() != null && !item.getPensionAllowances().isEmpty()) {
                        item.setCompanyPensionAmount(this.getPensionAmount(item, item.getCompanyNonLocalPensionRate()));

                    } else {
                        item.setCompanyPensionAmount(item.getBasicSalary()
                                .multiply(item.getCompanyNonLocalPensionRate())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    }
                }
            }
        }
        return Optional.ofNullable(item.getPensionAmount()).orElse(BigDecimal.ZERO);
    }

    private BigDecimal getPensionAmount(SinglePayrunItem item, BigDecimal pensionRate) {
        final BigDecimal maxTaxableAmount = item.getCompMaxTaxableAmount();
        final Set<Integer> categoryIds = item.getPensionAllowances()
                .stream()
                .map(PaymentDeductionSelectItem::getId)
                .collect(Collectors.toSet());
        BigDecimal allowanceTotal = BigDecimal.ZERO;
        for (PaymentDeductionObject deductionObject : item.getPaymentCategories()) {
            if (!categoryIds.contains(deductionObject.getCategoryItem().getId())) {
                continue;
            }
            if (deductionObject.getType() == null || deductionObject.getType() == 0 || deductionObject.isLoan()) {
                allowanceTotal = allowanceTotal.add(deductionObject.getPaymentAmount());
            } else if (deductionObject.getPercentage() != null) {
                if (deductionObject.getPaymentAmount() != null) {
                    allowanceTotal = allowanceTotal.add(deductionObject.getPaymentAmount());
                } else {
                    allowanceTotal = allowanceTotal.add(item.getBasicSalary().multiply(deductionObject.getPercentage())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                }
            }
        }
        allowanceTotal = allowanceTotal.add(item.getBasicSalary());
        if (maxTaxableAmount.compareTo(BigDecimal.ZERO) > 0 && allowanceTotal.compareTo(maxTaxableAmount) >= 0) {
            allowanceTotal = maxTaxableAmount;
        }
        return allowanceTotal.multiply(pensionRate).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean saveSingleParunCellValue(SinglePayrunItem rowValue, String columnCodeName) {
        EdsPayslipTableItem singplePayrun = payslipTableItemManager.get(rowValue.getObjectID());
        try {
            EdsPayrollCustomFields payrunCF = singplePayrun.getCustomFields();
            if (payrunCF == null) {
                payrunCF = new EdsPayrollCustomFields();
                payrollCFManager.create(payrunCF);
                singplePayrun.setCustomFields(payrunCF);
            }
            CustomFieldsUtils.setDomenObjectFieldChange(payrunCF, rowValue.getCustomFieldMap(), columnCodeName);

            addSinglePayrunToSolr(singplePayrun);

            return true;
        } catch (Exception e) {
            log.error("Single Payrun List Edit Cell Column Code :" + columnCodeName, e);
            return false;
        }
    }


    @Override
    @Transactional
    public ArrayList<RejectedImportRecord[]> importGroupPayrun(ImportFile importFile, List<String[]> data) {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;
        Map<Integer, PaymentDeductionSelectItem> headerItem = new HashMap<>();

        boolean hasHeader = importFile.isHasHeader();
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_PATTERN);
        formatter.setLenient(false);

        Date processDate = null;
        Integer year = null;
        Integer monthID = null;
        ArrayList<SinglePayrunItem> singlePayrunList = new ArrayList<>();
        for (String[] row : data) {
            rejectedRow = new RejectedImportRecord[row.length];
            boolean isValid = true;

            boolean rowIsEmpty = true;
            int rowID = 0;
            for (String str : row) {
                rejectedRow[rowID++] = new RejectedImportRecord(str);
                if (rowIsEmpty && StringUtils.isNotBlank(str)) {
                    rowIsEmpty = false;
                }
            }
            if (hasHeader) {
                rejectedRecords.add(rejectedRow);
                int headerRow = 0;
                for (String str : row) {
                    if (headerRow >= 4) {
                        if (!StringUtils.isBlank(str)) {
                            EdsPayrollCategory edsPayrollCategory = categoryManager.getCategoryByCode(str);
                            if (edsPayrollCategory != null) {
                                PaymentDeductionSelectItem category = edsPayrollCategory.createPaymentDeductionSelectItem();
                                headerItem.put(headerRow, category);
                            }
                        }
                    }
                    headerRow++;
                }

                hasHeader = false;
                continue;
            }
            if (rowIsEmpty) {
                continue;
            }

            EdsEmployee edsEmployee = null;
            SinglePayrunItem payrunItem = new SinglePayrunItem();
            BigDecimal paymentAmount = BigDecimal.ZERO;
            ArrayList<PaymentDeductionObject> paymentCategories = new ArrayList<>();
            BigDecimal deductionAmount = BigDecimal.ZERO;
            ArrayList<PaymentDeductionObject> deductionCategories = new ArrayList<>();
            BigDecimal taxAmount = BigDecimal.ZERO;
            ArrayList<PaymentDeductionObject> taxCategories = new ArrayList<>();
            BigDecimal employerContributionAmount = BigDecimal.ZERO;
            ArrayList<PaymentDeductionObject> employerContributionCategories = new ArrayList<>();
            int columnID = 0;
            for (String columnValue : row) {
                if (columnID == 0) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[columnID].getData()));
                        isValid = false;
                    } else {
                        edsEmployee = employeeManager.getEmployeeByNumber(columnValue.trim());
                    }
                    if (edsEmployee == null || !isValid) {
                        if (edsEmployee == null) {
                            rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.employeeNotFound, rejectedRecords.get(0)[columnID].getData()));
                        }
                        break;
                    }
                    payrunItem.setEmployeeID(edsEmployee.getObjectID());
                    payrunItem.setEmployee(edsEmployee.getFullName());
                    payrunItem.setEmployeeCode(edsEmployee.getProfile() != null ? edsEmployee.getProfile().getEmployeeCode() : "");
                } else if (columnID == 1) {

                } else if (columnID == 2) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[columnID].getData()));
                        isValid = false;
                    } else {
                        try {
                            processDate = formatter.parse(columnValue);
                        } catch (ParseException e) {
                            rejectedRow[columnID].setErrorComment(this.commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
                            isValid = false;
                        }
                        payrunItem.setProcessDate(new DateNonConvertable(processDate));


                    }
                } else if (columnID == 3) {
                    BigDecimal basicSalary = null;
                    if (!StringUtils.isBlank(columnValue)) {
                        try {
                            basicSalary = new BigDecimal(columnValue.trim().replace(",", ""));
                        } catch (Exception e) {
                            basicSalary = null;
                        }
                    }
                    payrunItem.setBasicSalary(basicSalary);
                } else {
                    if (headerItem.get(columnID) != null && edsEmployee != null) {
                        PaymentDeductionSelectItem category = headerItem.get(columnID);
                        PaymentDeductionObject paymentDeductionObject = new PaymentDeductionObject();
                        paymentDeductionObject.setEmployee(edsEmployee.getAsSelectItem());
                        paymentDeductionObject.setCategoryItem(category);

                        if (payrunItem.getProcessDate() != null) {
                            paymentDeductionObject.setStarttDate(payrunItem.getProcessDate());
                            paymentDeductionObject.setEnddDate(payrunItem.getProcessDate());
                        }

                        BigDecimal amount = BigDecimal.ZERO;
                        if (!StringUtils.isBlank(columnValue)) {
                            try {
                                amount = new BigDecimal(columnValue.trim().replace(",", ""));
                            } catch (Exception e) {
                                amount = BigDecimal.ZERO;
                            }
                        }
                        paymentDeductionObject.setPaymentAmount(amount);

                        if ("Deduction".equals(category.getType())) {
                            deductionCategories.add(paymentDeductionObject);
                            deductionAmount = deductionAmount.add(amount);
                        } else if ("Tax".equals(category.getType())) {
                            taxCategories.add(paymentDeductionObject);
                            taxAmount = taxAmount.add(amount);
                        } else if ("EmployerContribution".equals(category.getType())) {
                            employerContributionCategories.add(paymentDeductionObject);
                            employerContributionAmount = employerContributionAmount.add(amount);
                        } else if ("Payment".equals(category.getType())) {
                            paymentCategories.add(paymentDeductionObject);
                            paymentAmount = paymentAmount.add(amount);
                        }
                    }
                }
                columnID++;
            }

            hasHeader = false;
            if (edsEmployee != null && payrunItem.getProcessDate() != null) {
                payrunItem.setPaymentCategories(paymentCategories);
                payrunItem.setDeductionCategories(deductionCategories);
                payrunItem.setTaxCategories(taxCategories);
                payrunItem.setEmployerContributionCategories(employerContributionCategories);
                payrunItem.setAllowance(paymentAmount);
                payrunItem.setDeduction(deductionAmount);
                payrunItem.setTax(taxAmount);
                payrunItem.setEmployerContribution(employerContributionAmount);
                BigDecimal basicSalary = payrunItem.getBasicSalary() != null ? payrunItem.getBasicSalary() : BigDecimal.ZERO;
                payrunItem.setTotal(basicSalary.add(payrunItem.getAllowance()).subtract(payrunItem.getDeduction()).subtract(payrunItem.getTax()));

                final Calendar processDateCalendar = Calendar.getInstance();
                processDateCalendar.setTimeInMillis(payrunItem.getProcessDate().getDateLong());

                if (year == null) {
                    year = processDateCalendar.get(Calendar.YEAR);
                }
                if (monthID == null) {
                    monthID = processDateCalendar.get(Calendar.MONTH);
                }
                payrunItem.setYear(processDateCalendar.get(Calendar.YEAR));
                payrunItem.setMonthID(processDateCalendar.get(Calendar.MONTH));
                int daysCount = ServerUtils.getMonthDaysCountInYear(monthID, year);
                payrunItem.setFromDate(new DateNonConvertable(new Date(year - 1900, monthID, 1)));
                payrunItem.setToDate(new DateNonConvertable(new Date(year - 1900, monthID, daysCount)));
                payrunItem.setDaysWorked(BigDecimal.valueOf(daysCount));
                singlePayrunList.add(payrunItem);
            }
        }

        GroupPayrunData groupPayrunData = new GroupPayrunData();
        groupPayrunData.setProcessDate(new DateNonConvertable(processDate));
        groupPayrunData.setMonthID(monthID);
        groupPayrunData.setYear(year);
        groupPayrunData.setStatus(Constants.PAYRUN_STATUS_DRAFT);

        String enabledMultiCurrency = getCompanyPayrollSettings(MULTI_CURRENCY_FOR_PAYROLL);
        boolean allEmployees = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_ALL_EMPLOYEES);
        if ((!"true".equals(enabledMultiCurrency)) && !allEmployees) {
            groupPayrunData.setPayrollBatchItem(new SelectItem(0, commonLocalizer.localize("allEmployees", "All Employees")));
            groupPayrunData.setProjectItem(null);
        }
        groupPayrunData.setCreator(userManager.getUser().getAsSelectItem());
        groupPayrunData.setFrequency(1);
        groupPayrunData.setCurrency(currencyService.getBaseCurrency());
        groupPayrunData.setExchangeRate(BigDecimal.ONE);
        Integer payslipTableId = createGroupPayrun(groupPayrunData);

        createPayslipTableItemsForImport(groupPayrunData, payslipTableId, singlePayrunList);
        return rejectedRecords;
    }

    @Override
    public ArrayList<RejectedImportRecord[]> importPaymentDeduction(ImportFile importFile, List<String[]>
            data, String type) {
        ArrayList<RejectedImportRecord[]> rejectedRecords = new ArrayList<>();
        RejectedImportRecord[] rejectedRow;

        Integer fieldCategoryName = importFile.getColumnID(ImportField.PaymentDeductionFields.FIELD_CATEGORY_NAME);
        Integer fieldCategoryCode = importFile.getColumnID(ImportField.PaymentDeductionFields.FIELD_CATEGORY_CODE);
        Integer fieldDebitToAccount = importFile.getColumnID(ImportField.PaymentDeductionFields.FIELD_DEBIT_TO_ACCOUNT);
        Integer fieldCreditToAccount = importFile.getColumnID(ImportField.PaymentDeductionFields.FIELD_CREDIT_TO_ACCOUNT);
        Integer fieldUseIn = importFile.getColumnID(ImportField.PaymentDeductionFields.FIELD_USE_IN);

        boolean hasHeader = importFile.isHasHeader();
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_PATTERN);
        formatter.setLenient(false);

        int impRows = 0;
        int ignoredRows = 0;
        int overwrittenRows = 0;
        int skippedRows = 0;
        for (String[] row : data) {
            rejectedRow = new RejectedImportRecord[row.length];
            boolean isValid = true;

            boolean rowIsEmpty = true;
            int rowID = 0;
            for (String str : row) {
                rejectedRow[rowID++] = new RejectedImportRecord(str);
                if (rowIsEmpty && StringUtils.isNotBlank(str)) {
                    rowIsEmpty = false;
                }
            }

            if (hasHeader) {
                rejectedRecords.add(rejectedRow);
                hasHeader = false;
                continue;
            }

            CategoryObject categoryObject = new CategoryObject();
            int columnID = 0;
            for (String columnValue : row) {
                if (columnID == fieldCategoryName) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[columnID].getData()));
                        isValid = false;
                    } else {
                        categoryObject.setName(columnValue);
                    }
                } else if (columnID == fieldCategoryCode) {
                    if (StringUtils.isBlank(columnValue)) {
                        rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueCannotBeEmpty, rejectedRecords.get(0)[columnID].getData()));
                        isValid = false;
                    } else {
                        categoryObject.setCode(columnValue.trim());
                    }
                } else if (columnID == fieldDebitToAccount) {
                    if (StringUtils.isNotBlank(columnValue)) {
                        EdsAccount account = accountingManager.getAccountByCode(columnValue);
                        if (account == null) {
                            rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.debitToAccountNotFound, rejectedRecords.get(0)[columnID].getData()));
                            isValid = false;
                        } else {
                            categoryObject.setDebitToAccountID(account.getObjectID());
                        }
                    }
                } else if (columnID == fieldCreditToAccount) {
                    if (StringUtils.isNotBlank(columnValue)) {
                        EdsAccount account = accountingManager.getAccountByCode(columnValue);
                        if (account == null) {
                            rejectedRow[columnID].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.creditToAccountNotFound, rejectedRecords.get(0)[columnID].getData()));
                            isValid = false;
                        } else {
                            categoryObject.setCreditToAccountID(account.getObjectID());
                        }
                    }
                } else if (columnID == fieldUseIn) {
                    if (StringUtils.isNotBlank(columnValue)) {
                        if (type.equals(EVENT_IMPORT_PAYMENT)) {
                            categoryObject.setDefaultCategory("YES".equalsIgnoreCase(columnValue));
                        } else {
                            categoryObject.setCashAdvance("YES".equalsIgnoreCase(columnValue));
                        }
                    }
                }

                columnID++;
            }
            categoryObject.setAdvancePayment(false);
            categoryObject.setEditable(true);
            CategoryRate simpleRate = new CategoryRate();
            simpleRate.setFixedAmount(BigDecimal.ZERO);
            categoryObject.setSimpleRate(simpleRate);
            categoryObject.setType(type.equals(EVENT_IMPORT_PAYMENT) ? "Payment" : "Deduction");
            if (isValid) {
                Integer id = savePaymentDeductionCategory(categoryObject);
                if (id == -1) {
                    if (importFile.isMerge()) {
                        PaymentDeductionSelectItem selectItem = getCategoryObject(categoryObject.getCode());
                        categoryObject.setId(selectItem.getId());
                        savePaymentDeductionCategory(categoryObject);
                        overwrittenRows++;
                        importFile.setClonedColumns(overwrittenRows);
                    } else {
                        skippedRows++;
                        importFile.setSkippedColumns(skippedRows);
                    }
                } else {
                    impRows++;
                    importFile.setImportedColumns(impRows);
                }
            } else {
                rejectedRecords.add(rejectedRow);
                ignoredRows++;
                importFile.setIgnoredColumns(ignoredRows);
            }
        }


        return rejectedRecords;
    }

    @Override
    public BigDecimal getPredefinedValueOfCategory(Integer employeeId, Integer categoryId) {
        EdsPaymentDeduction predefinedPaymentDeduction = paymentDeductionManager.getPredefinedPaymentDeduction(employeeId, categoryId, EPPaymentType.ADDITIONAL);
        return predefinedPaymentDeduction != null ? predefinedPaymentDeduction.getPaymentAmount() : null;
    }

    public void approvedActionForRecurringPayDeduction(Integer objectID) {
        log.info("PayrollService.approvedActionForRecurringPayDeduction(" + objectID + ")");
        EdsRecurringPayDeduction recurringPayDeduction = recurringPayDeductionManager.get(objectID);

        Date oldEffectiveDate = null;
        EdsPaymentDeduction paymentDeduction = paymentDeductionManager.getByRecurringPayDeductionID(recurringPayDeduction.getObjectID());
        if (paymentDeduction == null) {
            paymentDeduction = new EdsPaymentDeduction();
        } else {
            oldEffectiveDate = paymentDeduction.getStartDate();
        }
        if (recurringPayDeduction.getEmployee() != null) {
            paymentDeduction.setEmployeeId(recurringPayDeduction.getEmployee().getObjectID());
        }
        if (recurringPayDeduction.getCategory() != null) {
            paymentDeduction.setCategoryId(recurringPayDeduction.getCategory().getObjectID());
        }
        paymentDeduction.setRecurringPayDeductionID(recurringPayDeduction.getObjectID());

        paymentDeduction.setStartDate(recurringPayDeduction.getFromDate());
//        if (PayrollConstants.LINKED_TYPE_MINIMUM_WAGE.equals(recurringPayDeduction.getType())) {
//            BigDecimal minimumWage = BigDecimal.ZERO;
//            try {
////                minimumWage = getMinimumWageValueByDate(recurringPayDeduction.getFromDate(), true);
//            } catch (Exception e) {
//                log.error("MinimumWage: ", e);
//            }
//            paymentDeduction.setPaymentAmount(minimumWage.multiply(recurringPayDeduction.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
//            paymentDeduction.setPercentage(recurringPayDeduction.getPercentage());
//        } else {
        paymentDeduction.setPaymentAmount(recurringPayDeduction.getPaymentAmount());
        paymentDeduction.setPercentage(recurringPayDeduction.getPercentage());
//        }
        paymentDeduction.setPayType(recurringPayDeduction.getType());
        paymentDeduction.setRecurring(true);

        if (recurringPayDeduction.getTotalLimit() != null) {
            paymentDeduction.setTotalAmount(recurringPayDeduction.getTotalLimit());
        } else if (recurringPayDeduction.getToDate() != null) {
            paymentDeduction.setEndDate(recurringPayDeduction.getToDate());
        }

        if (PayType.DEDUCTION.equals(recurringPayDeduction.getPayType())) {
            //linked categories
            if (recurringPayDeduction.isFromAllAllowances()) {
                paymentDeduction.setFromAllAllowances(recurringPayDeduction.isFromAllAllowances());
            } else if (recurringPayDeduction.getLinkedCategories() != null && recurringPayDeduction.getLinkedCategories().size() > 0) {
                paymentDeduction.getLinkedCategories().addAll(recurringPayDeduction.getLinkedCategories());
            }

            /*if (recurringPayDeduction.getTotalLimit() != null) {
                paymentDeduction.setTotalAmount(recurringPayDeduction.getTotalLimit());
            } else {
                paymentDeduction.setEndDate(recurringPayDeduction.getToDate());
            }*/
        } else {
            //check for existing payment category for this employee, and update its effective date
            if (recurringPayDeduction.getFromDate() != null) {
                EdsPaymentDeduction previousPayDeductionBeforeUpdate = paymentDeductionManager.getPreviousPaymentDeductionByEffectiveDate(recurringPayDeduction.getEmployee().getObjectID(), recurringPayDeduction.getCategory().getObjectID(), oldEffectiveDate);
                if (previousPayDeductionBeforeUpdate != null) {
                    previousPayDeductionBeforeUpdate.setEndDate(null);
                    paymentDeductionManager.update(previousPayDeductionBeforeUpdate);
                }

                //After update
                EdsPaymentDeduction previousPayDeductionAfterUpdate = paymentDeductionManager.getPreviousPaymentDeductionByEffectiveDate(recurringPayDeduction.getEmployee().getObjectID(), recurringPayDeduction.getCategory().getObjectID(), recurringPayDeduction.getFromDate());
                if (previousPayDeductionAfterUpdate != null) {
                    previousPayDeductionAfterUpdate.setEndDate(DateUtil.minusDays(recurringPayDeduction.getFromDate(), 1));
                    paymentDeductionManager.update(previousPayDeductionAfterUpdate);
                }

                EdsPaymentDeduction nextPayDeduction = paymentDeductionManager.getNextPaymentDeductionByEffectiveDate(recurringPayDeduction.getEmployee().getObjectID(), recurringPayDeduction.getCategory().getObjectID(), recurringPayDeduction.getFromDate());
                if (nextPayDeduction != null && nextPayDeduction.getStartDate() != null) {
                    paymentDeduction.setEndDate(DateUtil.minusDays(nextPayDeduction.getStartDate(), 1));
                }
            }

        }
        paymentDeductionManager.createOrUpdate(paymentDeduction);

        recurringPayDeduction.setPaymentDeduction(paymentDeduction);
        recurringPayDeductionManager.update(recurringPayDeduction);
    }

    @Transactional
    public Integer createCashAdvanceByLeaveRequest(Integer leaveRequestId) {
        BigDecimal cashAdvanceAmount = BigDecimal.ZERO;
        StatisticsLeaveRequest leaveRequest = availabilityServiceLocal.getLeaveRequest(leaveRequestId);
        Date startDate = leaveRequest.getStartDDate().getNonConvertedDate();
        Date endDate = leaveRequest.getEndDDate().getNonConvertedDate();

        Date recallDate = leaveRequest.getRecallDDate().getNonConvertedDate();
        Integer employeeId = leaveRequest.getEmployeeId();
        EdsEmployee edsEmployee = employeeManager.get(employeeId);
        if (recallDate != null && startDate != null && endDate != null) {
            int periodCount = 12;
            if (edsEmployee.getStartDate() != null) {
                long monthCount = monthsBetween(edsEmployee.getStartDate(), Calendar.getInstance().getTime(), false);
                if (monthCount < 12) {
                    periodCount = (int) monthCount;
                }
            }

            EdsPayrollCategory annualLeaveCategory = categoryManager.getCategoryByCode(VACATION_PAY);
            Calendar startPaysCalendar = Calendar.getInstance();
            startPaysCalendar.setTime(startDate);

            EdsPayslipTableItem edsPayslipTableItem = payslipTableItemManager.getEmployeePayslipTable(employeeId, startPaysCalendar.get(Calendar.MONTH), startPaysCalendar.get(Calendar.YEAR));

            List<EdsPaymentDeduction> paymentDeductions = null;

            if (edsPayslipTableItem != null && (edsPayslipTableItem.isDeleted() == null || !edsPayslipTableItem.isDeleted()) && edsPayslipTableItem.getStatus() != null
                    && (Constants.PAYRUN_STATUS_APPROVED.equals(edsPayslipTableItem.getStatus().getCode())
                    || Constants.PAYRUN_STATUS_PARTIAL_PAID.equals(edsPayslipTableItem.getStatus().getCode())
                    || Constants.PAYRUN_STATUS_PAID.equals(edsPayslipTableItem.getStatus().getCode()))) {
                paymentDeductions = payslipTableItemManager.getItemCategoriesByCategoryID(edsPayslipTableItem.getObjectID(), annualLeaveCategory.getObjectID());
            }
            if (startDate.getYear() == endDate.getYear() && startDate.getMonth() == endDate.getMonth()) {
                cashAdvanceAmount = calculateAmountCashAdvance(employeeId, periodCount, recallDate, endDate, paymentDeductions);
            } else {
                BigDecimal paymentsTotal = BigDecimal.ZERO;
                long monthCount = monthsBetween(recallDate, endDate, true);
                Date startLVDate = recallDate;
                for (int i = 0; i < monthCount; i++) {
                    Date lastDayMonth = ServerUtils.getMonthEndDate(startLVDate);

                    paymentsTotal = paymentsTotal.add(calculateAmountCashAdvance(employeeId, periodCount, startLVDate, lastDayMonth, paymentDeductions));
                    Calendar startCalendar = Calendar.getInstance();
                    startCalendar.setTime(lastDayMonth);
                    startCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    startLVDate = ServerUtils.getMonthStartDate(startCalendar.getTime());
                }
                cashAdvanceAmount = paymentsTotal.add(calculateAmountCashAdvance(employeeId, periodCount, startLVDate, endDate, paymentDeductions));

            }

            if (cashAdvanceAmount.compareTo(BigDecimal.ZERO) > 0) {
                CashAdvanceItem cashAdvanceItem = new CashAdvanceItem();
                cashAdvanceItem.setType("Loan");
                cashAdvanceItem.setDate(new DateNonConvertable(recallDate));
                cashAdvanceItem.setCreationDate(new DateNonConvertable(new Date()));
                cashAdvanceItem.setEmployee(edsEmployee.getAsSelectItem());
                cashAdvanceItem.setTotalAmount(cashAdvanceAmount);
                cashAdvanceItem.setPaymentAmount(cashAdvanceAmount);
                cashAdvanceItem.setStatus(new SelectItem(Constants.APPROVED));
                BankTransferNumberData btnd = generateCashAdvanceNumberFormat();
                cashAdvanceItem.setNumber(btnd.getTransferNumber());
                cashAdvanceItem.setIntNumber(Integer.parseInt(btnd.getFourDigitNumber()));

                EdsPayrollCategory category = categoryManager.getCategoryByCode("Аванс");
                cashAdvanceItem.setCategoryItem(category != null ? category.createPaymentDeductionSelectItem() : null);
                cashAdvanceItem.setApprovedDate(new DateNonConvertable(new Date()));
                cashAdvanceItem.setLeaveRequestId(leaveRequestId);

                createCashAdvance(cashAdvanceItem);
            }
        }

        return null;
    }

    private BigDecimal calculateAmountCashAdvance(Integer employeeId, int periodCount, Date startDate, Date endDate, List<EdsPaymentDeduction> paymentDeductions) {
        BigDecimal cashAdvanceAmount = BigDecimal.ZERO;
        EdsLeaveReason reason = leaveReasonManager.findByCode(CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setIncludeDayOff(reason != null ? reason.getIncludeDayOffs() : false);
        fp.setReasonCode(CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
        fp.setEmployeeId(employeeId);
        fp.setAllDay(false);
        double dayCount = Double.parseDouble(availabilityServiceLocal.getLeaveDaysCount(fp, new DateNonConvertable(startDate), new DateNonConvertable(endDate)));
        if (dayCount > 0) {
            if (!CollectionUtils.isEmpty(paymentDeductions)) {
                for (EdsPaymentDeduction paymentDeduction : paymentDeductions) {
                    if (paymentDeduction.getStartDate() != null && paymentDeduction.getEndDate() != null && (startDate.equals(paymentDeduction.getStartDate()) || startDate.after(paymentDeduction.getStartDate())) && startDate.before(paymentDeduction.getEndDate())) {
                        double allLeavedayCount = Double.parseDouble(availabilityServiceLocal.getLeaveDaysCount(fp, new DateNonConvertable(paymentDeduction.getStartDate()), new DateNonConvertable(paymentDeduction.getEndDate())));
                        BigDecimal paymentAmount = paymentDeduction.getPaymentAmount() != null ? paymentDeduction.getPaymentAmount() : BigDecimal.ZERO;
                        cashAdvanceAmount = paymentAmount.divide(new BigDecimal(allLeavedayCount), 5, RoundingMode.HALF_UP).multiply(new BigDecimal(dayCount)).setScale(2, RoundingMode.HALF_UP);
                        break;
                    }
                }
            } else {
                BigDecimal totalPay = BigDecimal.ZERO;
                PayrollSettings payrollSettings = employeeServiceLocal.getEmployeeDetailsAndPayrollSettings(employeeId, new Date());
                BigDecimal basicSalaryAmount = payrollSettings.getPayrollSettings() != null && payrollSettings.getPayrollSettings().get(SALARY) != null ? new BigDecimal(payrollSettings.getPayrollSettings().get(SALARY)) : BigDecimal.ZERO;
                Calendar period = Calendar.getInstance();
                period.setTime(startDate);
                Integer monthId = period.get(Calendar.MONTH);
                Integer year = period.get(Calendar.YEAR);
                BigDecimal totalAllowance = payslipTableItemManager.getEmployeeAllowanceByPeriod(employeeId, monthId, year, CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
                if (totalAllowance.compareTo(BigDecimal.ZERO) > 0) {
                    totalPay = totalPay.add(totalAllowance);
                }
                BigDecimal totalAddPayment = additionalPaymentManager.getEmployeeAddPaymentByPeriod(employeeId, monthId, year, CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
                if (totalAddPayment.compareTo(BigDecimal.ZERO) > 0) {
                    totalPay = totalPay.add(totalAddPayment);
                }

                BigDecimal averageMonthlyAmount = totalPay.divide(new BigDecimal(periodCount), 5, RoundingMode.HALF_UP);
                BigDecimal dailyPayAmountAmount = averageMonthlyAmount.add(basicSalaryAmount).divide(new BigDecimal("25.4"), 5, RoundingMode.HALF_UP);

                cashAdvanceAmount = dailyPayAmountAmount.multiply(new BigDecimal(dayCount)).setScale(2, RoundingMode.HALF_UP);
            }
        }
        return cashAdvanceAmount;
    }

    @Override
    public void createAdditionalPaymentForLeaveRequestBackups(Integer sickRequestId) {
        EdsSickRequest sickRequest = sickRequestManager.get(sickRequestId);
        List<BackupEmployeeItem> list = availabilityServiceLocal.getBackupEmployeesForLeaveRequest(sickRequestId);
        if (list == null) {
            return;
        }

        EdsPayrollCategory category = categoryManager.getCategoryByCode("107");//ВОЗЛОЖЕНИЯ
        if (category == null) {
            category = categoryManager.getDefaultCategory();
        }
        if (category == null) {
            return;
        }

        String rateSettings = getCompanyPayrollSettings(DAILY_RATE_BY_EMPLOYER_SETTINGS);
        final boolean isDailyRateByEmployerSettings = "true".equals(rateSettings) || "BY_STATIC_DAY".equals(rateSettings);
        final boolean isCalculationByTimeslot = "BY_TIMESLOT".equals(rateSettings);

        BigDecimal numberOfWorkDay = BigDecimal.ZERO;
        List<Date> workingDays = null;
        if (isDailyRateByEmployerSettings) {
            numberOfWorkDay = new BigDecimal(getCompanyPayrollSettings(NUMBER_OF_WORK_DAYS, DEFAULT_NUMBER_OF_WORK_DAYS.toString()));
        } else if (isCalculationByTimeslot) {
            ListingFilterParameter lfp = new ListingFilterParameter();
            lfp.setStartDate(DateUtil.getMonthFirstDay(sickRequest.getStartDate()));
            lfp.setEndDate(DateUtil.getMonthLastDate(sickRequest.getStartDate()));
            lfp.setEmployeeId(sickRequest.getEmployee().getObjectID());
            lfp.setDailyRateByEmployerSettings(isDailyRateByEmployerSettings);
            workingDays = attendanceRawDataManager.getWorkingDays(lfp);
            numberOfWorkDay = new BigDecimal(workingDays.size());
        } else {
            final Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTimeInMillis(sickRequest.getStartDate().getTime());
            numberOfWorkDay = BigDecimal.valueOf(startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }

        EdsEmployeePayrollSettings employeePayrollSettings = employeePayrollSettingsManager.getEmployeeSettingValue(sickRequest.getEmployee().getObjectID(), Constants.SALARY);
        BigDecimal salary = BigDecimal.ZERO;
        if (employeePayrollSettings != null) {
            salary = new BigDecimal(employeePayrollSettings.getValue());
        }

        String employeeName = sickRequest.getEmployee().getFullName();

        for (BackupEmployeeItem backupEmployeeItem : list) {
            if (backupEmployeeItem.getDutyPercentage().compareTo(BigDecimal.ZERO) > 0) {
                AdditionalPayment payment = new AdditionalPayment();
                payment.setShowInPayslip(true);

                Date startDate = backupEmployeeItem.getParentBackupEmployee().getFromBackupEmployeeDate().getNonConvertedDate();
                LocalDate currentDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                payment.setYear(currentDate.getYear());
                payment.setMonth(currentDate.getMonth().getDisplayName(TextStyle.FULL, ServerUtils.getUserLocale()));
                payment.setMonthID(currentDate.getMonth().getValue() - 1);

                payment.setReference(sickRequest.getEmployee().getFullName() + " " + category.getName() + " -> " + currentDate.getMonth().getDisplayName(TextStyle.FULL, ServerUtils.getUserLocale()) + " " + currentDate.getYear() + " -> " + employeeName);

                PaymentDeductionSelectItem selectItem = category.createPaymentDeductionSelectItem();
                payment.setDefaultCategory(selectItem);
                payment.setDefaultPayrollCategoryId(selectItem.getId());

                payment.setApprovedDate(new DateNonConvertable());
                payment.setStatus("APPROVED");
                payment.setStatusCode(PAYMENT_STATUS_APPROVED);
                payment.setOverallStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, PAYMENT_STATUS_APPROVED).getRPC());

                List<PaymentDeductionObject> items = new ArrayList<>();
                BigDecimal totalAmount = BigDecimal.ZERO;
                for (ApproverItemMini child : backupEmployeeItem.getChildList()) {
                    PaymentDeductionObject item = new PaymentDeductionObject();
                    item.setEmployee(child.getExactEmployee());
                    item.setPaymentDate(child.getFromBackupEmployeeDate().getNonConvertedDate());
                    item.setAdditionalPaymentDate(child.getFromBackupEmployeeDate());
                    item.setCategoryItem(category.createPaymentDeductionSelectItem());
                    item.setPaymentAmount(calculatePaymentForBackupEmployee(child, salary, backupEmployeeItem.getDutyPercentage(), numberOfWorkDay, rateSettings));
                    totalAmount = totalAmount.add(item.getPaymentAmount());
                    items.add(item);
                }

                payment.setItems(items);
                payment.setTotal(totalAmount);
                createPaymentFromOvertimeItem(payment);
            }
        }
    }

    @Transactional
    public void createAdditionalPaymentByLeaveRequest(Integer leaveRequestId) {
        if (CollectionUtils.isEmpty(additionalPaymentManager.getAdditionalPaymentByLeaveRequestId(leaveRequestId))) {
            EdsReference type = referenceManager.findReference(EdsSickRequest._SICK_TYPE, EdsSickRequest.PAID);
            StatisticsLeaveRequest leaveRequest = availabilityServiceLocal.getLeaveRequest(leaveRequestId);
            DateNonConvertable startDate = leaveRequest.getStartDDate();
            DateNonConvertable endDate = leaveRequest.getEndDDate();
            ArrayList<PaymentCalculationDetail> calculationDetails = new ArrayList<>();
            if (type != null && type.getObjectID().equals(leaveRequest.getTypeId())) {
                Integer employeeId = leaveRequest.getEmployeeId();
                EdsEmployee employee = employeeManager.get(employeeId);

                AdditionalPayment payment = new AdditionalPayment();
                payment.setShowInPayslip(false);
                payment.setType(Constants.ADDITIONAL_PAYMENT_TYPE);
                payment.setCategoryType(PayrollConstants.CATEGORY_PAYMENT);
                payment.setEmployeeIds(employeeId.toString());
                payment.setLeaveRequestId(leaveRequestId);

                LocalDate currentDate = startDate.getNonConvertedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                payment.setYear(currentDate.getYear());
                payment.setMonth(currentDate.getMonth().getDisplayName(TextStyle.FULL, ServerUtils.getUserLocale()));
                payment.setMonthID(currentDate.getMonth().getValue() - 1);
                payment.setApprovedDate(new DateNonConvertable());
                payment.setDefaultDate(startDate);
                payment.setStatus("PAYMENT_DRAFT");
                payment.setStatusCode(PAYMENT_STATUS_DRAFT);
                payment.setOverallStatus(referenceManager.findReference(Constants.PAYMENT_STATUS, PAYMENT_STATUS_DRAFT).getRPC());

                List<PaymentDeductionObject> items = new ArrayList<>();
                PaymentDeductionObject item = new PaymentDeductionObject();
                item.setEmployee(employee.getAsSelectItem());
                item.setPaymentDate(startDate.getNonConvertedDate());
                item.setAdditionalPaymentDate(startDate);
                items.add(item);
                payment.setItems(items);

                //leave payments
                int periodCount = 12;
                if (employee.getStartDate() != null) {
                    long monthCount = monthsBetween(employee.getStartDate(), Calendar.getInstance().getTime(), false);
                    if (monthCount < 12) {
                        periodCount = (int) monthCount;
                    }
                }

                String leaveReqCode = leaveRequest.getNumberData() != null ? leaveRequest.getNumberData().getNumberString() : null;
                StringBuilder reference = new StringBuilder();
                BigDecimal paymentsTotal = BigDecimal.ZERO;
                BigDecimal totalByMoney = BigDecimal.ZERO;
                BigDecimal paymentsTotalBasicSalary = BigDecimal.ZERO;

                if (startDate != null && endDate != null) {
                    if (CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(leaveRequest.getReasonCode()) || "СОЦИАЛЬНЫЙ_ОТПУСК_-_ОТПУСК_В_СВЯЗИ_С_ОБУЧЕНИЕМ".equals(leaveRequest.getReasonCode())) {
                        boolean isAnnual = CustomFormConstants.LR_TYPE_ANNUAL_LEAVE.equals(leaveRequest.getReasonCode());

                        EdsPayrollCategory annualLeaveCategory = categoryManager.getCategoryByCode(VACATION_PAY);
                        EdsPayrollCategory studyLeaveCategory = categoryManager.getCategoryByCode(STUDY_LEAVE_PAY);

                        if (isAnnual && annualLeaveCategory != null || !isAnnual && studyLeaveCategory != null) {
                            if (startDate.getNonConvertedDate().getYear() == endDate.getNonConvertedDate().getYear() && startDate.getNonConvertedDate().getMonth() == endDate.getNonConvertedDate().getMonth()) {
                                BigDecimal[] amounts = paymentCategoryByLeaveRequest(calculationDetails, employeeId, isAnnual, reference, periodCount, leaveReqCode, startDate.getNonConvertedDate(), endDate.getNonConvertedDate());
                                paymentsTotal = paymentsTotal.add(amounts[0]);
                                paymentsTotalBasicSalary = paymentsTotalBasicSalary.add(amounts[1]);
                            } else {
                                long monthCount = monthsBetween(startDate.getNonConvertedDate(), endDate.getNonConvertedDate(), true);
                                Date startLVDate = startDate.getNonConvertedDate();
                                for (int i = 0; i < monthCount; i++) {
                                    Date lastDayMonth = ServerUtils.getMonthEndDate(startLVDate);
                                    BigDecimal[] amounts = paymentCategoryByLeaveRequest(calculationDetails, employeeId, isAnnual, reference, periodCount, leaveReqCode, startLVDate, lastDayMonth);
                                    paymentsTotal = paymentsTotal.add(amounts[0]);
                                    paymentsTotalBasicSalary = paymentsTotalBasicSalary.add(amounts[1]);
                                    Calendar startCalendar = Calendar.getInstance();
                                    startCalendar.setTime(lastDayMonth);
                                    startCalendar.add(Calendar.DAY_OF_MONTH, 1);
                                    startLVDate = ServerUtils.getMonthStartDate(startCalendar.getTime());
                                }
                                BigDecimal[] amounts = paymentCategoryByLeaveRequest(calculationDetails, employeeId, isAnnual, reference, periodCount, leaveReqCode, startLVDate, endDate.getNonConvertedDate());
                                paymentsTotal = paymentsTotal.add(amounts[0]);
                                paymentsTotalBasicSalary = paymentsTotalBasicSalary.add(amounts[1]);

                            }

                            if (isAnnual) {
                                List<Object[]> dayTypesByPeriod = labourPeriodManager.getDayTypesByPeriod(leaveRequestId);
                                for (Object[] object : dayTypesByPeriod) {
                                    if (object[1] != null && object[2] != null && Constants.MONEY.equals(object[1])) {
                                        totalByMoney = totalByMoney.add(paymentCategoryByLeaveRequestMoneyType(calculationDetails, employeeId, reference, periodCount, leaveRequest, ((BigDecimal) object[2]).intValue()));
                                    }
                                }
                            }

                            PaymentDeductionSelectItem selectItem = isAnnual ? annualLeaveCategory.createPaymentDeductionSelectItem() : studyLeaveCategory.createPaymentDeductionSelectItem();
                            payment.setDefaultCategory(selectItem);
                            payment.setDefaultPayrollCategoryId(selectItem.getId());
                            item.setCategoryItem(selectItem);
                        }
                    } else if (CustomFormConstants.LR_TYPE_SICK_LEAVE.equals(leaveRequest.getReasonCode())) {
                        Integer percentage = leaveRequest.getSalaryPercentage() != null ? leaveRequest.getSalaryPercentage() : 100;
                        EdsPayrollCategory sickLeaveCategory = categoryManager.getCategoryByCode(SICK_LEAVE_PAYMENT_CODE);
                        if (sickLeaveCategory != null) {
                            BigDecimal basicSalaryAmount = salaryHistoryManager.getEmployeeLastSalaryHistory(employeeId, startDate.getNonConvertedDate());
                            if (startDate.getNonConvertedDate().getYear() == endDate.getNonConvertedDate().getYear() && startDate.getNonConvertedDate().getMonth() == endDate.getNonConvertedDate().getMonth()) {
                                paymentsTotal = paymentsTotal.add(sickLeavePayment(calculationDetails, employee, basicSalaryAmount, periodCount, leaveReqCode, startDate.getNonConvertedDate(), endDate.getNonConvertedDate(), percentage, reference));
                            } else {
                                long monthCount = monthsBetween(startDate.getNonConvertedDate(), endDate.getNonConvertedDate(), true);
                                Date startLVDate = startDate.getNonConvertedDate();
                                for (int i = 0; i < monthCount; i++) {
                                    Date lastDayMonth = ServerUtils.getMonthEndDate(startLVDate);
                                    paymentsTotal = paymentsTotal.add(sickLeavePayment(calculationDetails, employee, basicSalaryAmount, periodCount, leaveReqCode, startLVDate, lastDayMonth, percentage, reference));
                                    Calendar startCalendar = Calendar.getInstance();
                                    startCalendar.setTime(lastDayMonth);
                                    startCalendar.add(Calendar.DAY_OF_MONTH, 1);
                                    startLVDate = ServerUtils.getMonthStartDate(startCalendar.getTime());
                                }
                                paymentsTotal = paymentsTotal.add(sickLeavePayment(calculationDetails, employee, basicSalaryAmount, periodCount, leaveReqCode, startLVDate, endDate.getNonConvertedDate(), percentage, reference));
                            }

                            PaymentDeductionSelectItem selectItem = sickLeaveCategory.createPaymentDeductionSelectItem();
                            payment.setDefaultCategory(selectItem);
                            payment.setDefaultPayrollCategoryId(selectItem.getId());
                            item.setCategoryItem(selectItem);
                        }
                    } else if ("ОТПУСК_-_РОЖДЕНИЕ_РЕБЕНКА".equals(leaveRequest.getReasonCode())) {
                        BigDecimal maternityLeaveDayCount = leaveRequest.getMaternityLeaveDayCount() != null ? leaveRequest.getMaternityLeaveDayCount() : BigDecimal.ZERO;
                        EdsPayrollCategory maternityLeaveCategory = categoryManager.getCategoryByCode(MATERNITY_LEAVE_PAYMENT_CODE);
                        if (maternityLeaveCategory != null) {
                            BigDecimal basicSalaryAmount = salaryHistoryManager.getEmployeeLastSalaryHistory(employeeId, startDate.getNonConvertedDate());
                            if (basicSalaryAmount == null) {
                                basicSalaryAmount = BigDecimal.ZERO;
                            }
                            paymentsTotal = paymentsTotal.add(maternityLeavePayment(calculationDetails, employee, basicSalaryAmount, periodCount, leaveReqCode, startDate.getNonConvertedDate(), endDate.getNonConvertedDate(), maternityLeaveDayCount, reference));

                            PaymentDeductionSelectItem selectItem = maternityLeaveCategory.createPaymentDeductionSelectItem();
                            payment.setDefaultCategory(selectItem);
                            payment.setDefaultPayrollCategoryId(selectItem.getId());
                            item.setCategoryItem(selectItem);
                        }
                    }


                    if (leaveRequest.getReason() != null && employee != null) {
                        payment.setReference(employee.getName() + "  " + leaveRequest.getReason());
                    } else {
                        payment.setReference(reference.toString());
                    }
                    payment.setTotal(paymentsTotal.add(totalByMoney));
                    payment.setPaymentType("FIXED_AMOUNT");
                    payment.setFixedAmount(paymentsTotal);
                    item.setPaymentAmount(paymentsTotal);
                    item.setBasicSalaryPartAmount(paymentsTotalBasicSalary);
                    item.setTotalAmount(paymentsTotal);


                    if (totalByMoney.compareTo(BigDecimal.ZERO) > 0) {
                        EdsPayrollCategory takeByMoney = categoryManager.getCategoryByCode(TAKE_BY_MONEY_PAY);
                        PaymentDeductionObject itemByMoney = new PaymentDeductionObject();
                        itemByMoney.setEmployee(employee.getAsSelectItem());
                        itemByMoney.setPaymentDate(startDate.getNonConvertedDate());
                        itemByMoney.setAdditionalPaymentDate(startDate);
                        itemByMoney.setPaymentAmount(totalByMoney);
                        itemByMoney.setTotalAmount(totalByMoney);
                        itemByMoney.setCategoryItem(takeByMoney != null ? takeByMoney.createPaymentDeductionSelectItem() : null);
                        items.add(itemByMoney);
                    }

                    if (payment.getTotal() != null && payment.getTotal().compareTo(BigDecimal.ZERO) > 0) {
                        payment.setFromView(false);
                        payment.setCalculationDetails(calculationDetails);
                        saveSingleAdditionalPayment(payment);
                    }
                }
            }
        }
    }

    public void saveSingleAdditionalPayment(AdditionalPayment data) {
        Integer additionalPaymentId = createAdditionalPayment(data, false);
        data.setObjectID(additionalPaymentId);
        for (PaymentDeductionObject item : data.getItems()) {
            item.setCashAdvanceID(additionalPaymentId);
            createPaymentDeduction(item);
        }
    }

    private BigDecimal sickLeavePayment(ArrayList<PaymentCalculationDetail> calculationDetails, EdsEmployee employee, BigDecimal bSalary, int periodCount, String leaveRequestCode, Date startDate, Date endDate, Integer percentage, StringBuilder reference) {
        EdsLeaveReason reason = leaveReasonManager.findByCode(LR_TYPE_SICK_LEAVE);

        BigDecimal totalPay = BigDecimal.ZERO;
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setIncludeDayOff(reason != null ? reason.getIncludeDayOffs() : false);
        fp.setReasonCode(LR_TYPE_SICK_LEAVE);
        fp.setEmployeeId(employee.getObjectID());
        fp.setAllDay(false);
        Double dayCount = Double.valueOf(availabilityServiceLocal.getLeaveDaysCount(fp, new DateNonConvertable(startDate), new DateNonConvertable(endDate)));
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (dayCount > 0) {
            BigDecimal basicSalaryAmount = bSalary != null ? bSalary : BigDecimal.ZERO;
            BigDecimal nadbavka = BigDecimal.ZERO;
            EdsPayrollCategory nadbavkaCategory = categoryManager.getCategoryByCode("105");
            if (nadbavkaCategory != null && !CollectionUtils.isEmpty(employee.getCategories())) {
                for (EdsPaymentDeduction edsPaymentDeduction : employee.getCategories()) {
                    if (edsPaymentDeduction != null && edsPaymentDeduction.getCategory() != null && edsPaymentDeduction.getCategory().getObjectID().equals(nadbavkaCategory.getObjectID())) {
                        if (edsPaymentDeduction.getPayType() == 0 && edsPaymentDeduction.getPaymentAmount() != null) {
                            nadbavka = edsPaymentDeduction.getPaymentAmount();
                        } else if (edsPaymentDeduction.getPayType() == 1 && edsPaymentDeduction.getPercentage() != null) {
                            nadbavka = basicSalaryAmount.multiply(edsPaymentDeduction.getPercentage()).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
                        }
                    }
                }
            }

            Calendar period = Calendar.getInstance();
            period.setTime(startDate);
            Integer monthId = period.get(Calendar.MONTH);
            Integer year = period.get(Calendar.YEAR);
            BigDecimal totalAllowance = payslipTableItemManager.getEmployeeAllowanceByPeriod(employee.getObjectID(), monthId, year, LR_TYPE_SICK_LEAVE);
            if (totalAllowance.compareTo(BigDecimal.ZERO) > 0) {
                totalPay = totalPay.add(totalAllowance);
            }
            BigDecimal totalAddPayment = additionalPaymentManager.getEmployeeAddPaymentByPeriod(employee.getObjectID(), monthId, year, LR_TYPE_SICK_LEAVE);
            if (totalAddPayment.compareTo(BigDecimal.ZERO) > 0) {
                totalPay = totalPay.add(totalAddPayment);
            }

            BigDecimal averageMonthlyAmount = totalPay.divide(new BigDecimal(periodCount), 5, RoundingMode.HALF_UP);
            Double monthDays = Double.valueOf(availabilityServiceLocal.getLeaveDaysCount(fp, new DateNonConvertable(ServerUtils.getMonthStartDate(startDate)), new DateNonConvertable(ServerUtils.getMonthEndDate(startDate))));
            BigDecimal monthlyAmount = basicSalaryAmount.add(nadbavka).add(averageMonthlyAmount);
            BigDecimal dailyPayAmountAmount = monthlyAmount.divide(new BigDecimal(monthDays), 5, RoundingMode.HALF_UP);

            BigDecimal amount = dailyPayAmountAmount.multiply(new BigDecimal(dayCount));
            totalAmount = amount.multiply(new BigDecimal(percentage)).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);

            String remarks = "";
            if (leaveRequestCode != null) {
                remarks += leaveRequestCode + "-> "; //!!! bu "->" belgini o'zgartirishda PaymentCalculationSideNavBox classda split qilib ishlatilgan shu yerga e'tibor berish kerak
            }
            remarks += ServerUtils.getDateAsString(startDate, true) + " - " + ServerUtils.getDateAsString(endDate, true) + " (" + dayCount + ") \n";
            reference.append(remarks);

            String formula = "(basicSalary + nadbavka + (totalPay/hirePeriod))/monthDays * sickRequestDays * (percentage/100)";
            String calculation = "(" + basicSalaryAmount.setScale(2, RoundingMode.HALF_UP) + " + " + nadbavka.setScale(2, RoundingMode.HALF_UP) + " + (" + totalPay.setScale(2, RoundingMode.HALF_UP) + ")/" + periodCount + "))/" + monthDays + " * " + dayCount + " * (" + percentage + "/100)";

            PaymentCalculationDetail paymentCalculationDetail = new PaymentCalculationDetail();
            paymentCalculationDetail.setName(remarks);
            paymentCalculationDetail.setFormula(formula);
            paymentCalculationDetail.setCalculation(calculation);
            paymentCalculationDetail.setAmount(amount);
            calculationDetails.add(paymentCalculationDetail);
        }

        return totalAmount;
    }

    private BigDecimal maternityLeavePayment(ArrayList<PaymentCalculationDetail> calculationDetails, EdsEmployee employee, BigDecimal bSalary, int periodCount, String leaveRequestCode, Date startDate, Date endDate, BigDecimal maternityDayCount, StringBuilder reference) {
        EdsLeaveReason reason = leaveReasonManager.findByCode("ОТПУСК_-_РОЖДЕНИЕ_РЕБЕНКА");
        BigDecimal totalPay = BigDecimal.ZERO;
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setIncludeDayOff(reason != null ? reason.getIncludeDayOffs() : false);
        fp.setReasonCode(LR_TYPE_SICK_LEAVE);
        fp.setEmployeeId(employee.getObjectID());
        fp.setAllDay(false);
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (maternityDayCount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal basicSalaryAmount = bSalary != null ? bSalary : BigDecimal.ZERO;
            BigDecimal nadbavka = BigDecimal.ZERO;
            EdsPayrollCategory nadbavkaCategory = categoryManager.getCategoryByCode("105");
            if (nadbavkaCategory != null && !CollectionUtils.isEmpty(employee.getCategories())) {
                for (EdsPaymentDeduction edsPaymentDeduction : employee.getCategories()) {
                    if (edsPaymentDeduction != null && edsPaymentDeduction.getCategory() != null && edsPaymentDeduction.getCategory().getObjectID().equals(nadbavkaCategory.getObjectID())) {
                        if (edsPaymentDeduction.getPayType() == 0 && edsPaymentDeduction.getPaymentAmount() != null) {
                            nadbavka = edsPaymentDeduction.getPaymentAmount();
                        } else if (edsPaymentDeduction.getPayType() == 1 && edsPaymentDeduction.getPercentage() != null) {
                            nadbavka = basicSalaryAmount.multiply(edsPaymentDeduction.getPercentage()).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
                        }
                    }
                }
            }

            Calendar period = Calendar.getInstance();
            period.setTime(startDate);
            Integer monthId = period.get(Calendar.MONTH);
            Integer year = period.get(Calendar.YEAR);
            BigDecimal totalAllowance = payslipTableItemManager.getEmployeeAllowanceByPeriod(employee.getObjectID(), monthId, year, LR_TYPE_SICK_LEAVE);
            if (totalAllowance.compareTo(BigDecimal.ZERO) > 0) {
                totalPay = totalPay.add(totalAllowance);
            }
            BigDecimal totalAddPayment = additionalPaymentManager.getEmployeeAddPaymentByPeriod(employee.getObjectID(), monthId, year, LR_TYPE_SICK_LEAVE);
            if (totalAddPayment.compareTo(BigDecimal.ZERO) > 0) {
                totalPay = totalPay.add(totalAddPayment);
            }

            BigDecimal averageMonthlyAmount = totalPay.divide(new BigDecimal(periodCount), 5, RoundingMode.HALF_UP);
//            Double monthDays = Double.valueOf(availabilityServiceLocal.getLeaveDaysCount(fp, new DateNonConvertable(ServerUtils.getMonthStartDate(startDate)), new DateNonConvertable(ServerUtils.getMonthEndDate(startDate))));
            Double monthDays = 20d;
            BigDecimal monthlyAmount = basicSalaryAmount.add(nadbavka).add(averageMonthlyAmount);
            BigDecimal dailyPayAmountAmount = monthlyAmount.divide(new BigDecimal(monthDays), 5, RoundingMode.HALF_UP);

            totalAmount = dailyPayAmountAmount.multiply(maternityDayCount).setScale(2, RoundingMode.HALF_UP);

            String remarks = "";
            if (leaveRequestCode != null) {
                remarks += leaveRequestCode + "-> ";  //!!! bu "->" belgini o'zgartirishda PaymentCalculationSideNavBox classda split qilib ishlatilgan shu yerga e'tibor berish kerak
            }
            remarks += ServerUtils.getDateAsString(startDate, true) + " - " + ServerUtils.getDateAsString(endDate, true) + " (" + maternityDayCount + ") \n";
            reference.append(remarks);


            String formula = "(basicSalary + nadbavka + (totalPay/hirePeriod))/monthDays * maternityDays";
            String calculation = "(" + basicSalaryAmount.setScale(2, RoundingMode.HALF_UP) + " + " + nadbavka.setScale(2, RoundingMode.HALF_UP) + " + (" + totalPay.setScale(2, RoundingMode.HALF_UP) + ")/" + periodCount + "))/" + monthDays + " * " + maternityDayCount;

            PaymentCalculationDetail paymentCalculationDetail = new PaymentCalculationDetail();
            paymentCalculationDetail.setName(remarks);
            paymentCalculationDetail.setFormula(formula);
            paymentCalculationDetail.setCalculation(calculation);
            paymentCalculationDetail.setAmount(totalAmount);
            calculationDetails.add(paymentCalculationDetail);
        }

        return totalAmount;
    }

    private BigDecimal paymentCategoryByLeaveRequestMoneyType(ArrayList<PaymentCalculationDetail> calculationDetails, Integer employeeId, StringBuilder reference, int periodCount, StatisticsLeaveRequest leaveRequest, Integer dayCount) {
        EdsLeaveReason reason = leaveReasonManager.findByCode(CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);

        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal totalPay = BigDecimal.ZERO;
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setIncludeDayOff(reason != null ? reason.getIncludeDayOffs() : false);
        fp.setReasonCode(CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
        fp.setEmployeeId(employeeId);
        fp.setAllDay(false);

        if (dayCount > 0) {
            Calendar period = Calendar.getInstance();
            period.setTime(leaveRequest.getStartDDate().getNonConvertedDate());
            int monthId = period.get(Calendar.MONTH);
            int year = period.get(Calendar.YEAR);
            BigDecimal totalAllowance = payslipTableItemManager.getEmployeeAllowanceByPeriod(employeeId, monthId, year, CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
            if (totalAllowance.compareTo(BigDecimal.ZERO) > 0) {
                totalPay = totalPay.add(totalAllowance);
            }
            BigDecimal totalAddPayment = additionalPaymentManager.getEmployeeAddPaymentByPeriod(employeeId, monthId, year, CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
            if (totalAddPayment.compareTo(BigDecimal.ZERO) > 0) {
                totalPay = totalPay.add(totalAddPayment);
            }
            BigDecimal basicSalaryAmount = salaryHistoryManager.getEmployeeLastSalaryHistory(employeeId, leaveRequest.getStartDDate().getNonConvertedDate());
            if (basicSalaryAmount == null) {
                basicSalaryAmount = BigDecimal.ZERO;
            }
            BigDecimal averageMonthlyAmount = totalPay.divide(new BigDecimal(periodCount), 5, RoundingMode.HALF_UP);
            BigDecimal dailyPayAmountAmount = averageMonthlyAmount.add(basicSalaryAmount).divide(new BigDecimal("25.4"), 5, RoundingMode.HALF_UP);

            amount = dailyPayAmountAmount.multiply(new BigDecimal(dayCount)).setScale(2, RoundingMode.HALF_UP);
            String remarks = commonLocalizer.localize("takeLeaveBy", "Take Leave By") + ": " + commonLocalizer.localize("money", "Money") + " (" + dayCount + ")" + "\n";
            reference.append(remarks);

            String formula = "((totalPay/hirePeriod) + basicSalary)/25.4 * leaveRequestDays";
            String calculation = "((" + totalPay.setScale(2, RoundingMode.HALF_UP) + "/" + periodCount + ") + " + basicSalaryAmount.setScale(2, RoundingMode.HALF_UP) + ")/25.4 * " + dayCount;

            PaymentCalculationDetail paymentCalculationDetail = new PaymentCalculationDetail();
            paymentCalculationDetail.setName(remarks);
            paymentCalculationDetail.setFormula(formula);
            paymentCalculationDetail.setCalculation(calculation);
            paymentCalculationDetail.setAmount(amount);
            calculationDetails.add(paymentCalculationDetail);
        }
        return amount;
    }

    private BigDecimal[] paymentCategoryByLeaveRequest(List<PaymentCalculationDetail> calculationDetails, Integer employeeId, boolean isAnnual, StringBuilder reference, int periodCount, String leaveRequestCode, Date startDate, Date endDate) {
        EdsLeaveReason reason = leaveReasonManager.findByCode(isAnnual ? CustomFormConstants.LR_TYPE_ANNUAL_LEAVE : "СОЦИАЛЬНЫЙ_ОТПУСК_-_ОТПУСК_В_СВЯЗИ_С_ОБУЧЕНИЕМ");

        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal basicSalaryPay = BigDecimal.ZERO;
        BigDecimal basicSalaryAmount = salaryHistoryManager.getEmployeeLastSalaryHistory(employeeId, startDate);
        if (basicSalaryAmount == null) {
            basicSalaryAmount = BigDecimal.ZERO;
        }
        BigDecimal totalPay = BigDecimal.ZERO;
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(startDate);
        fp.setEndDate(endDate);
        fp.setIncludeDayOff(reason != null ? reason.getIncludeDayOffs() : false);
        fp.setReasonCode(isAnnual ? CustomFormConstants.LR_TYPE_ANNUAL_LEAVE : "СОЦИАЛЬНЫЙ_ОТПУСК_-_ОТПУСК_В_СВЯЗИ_С_ОБУЧЕНИЕМ");
        fp.setEmployeeId(employeeId);
        fp.setAllDay(false);
        Double dayCount = availabilityServiceLocal.getLeaveDays(fp, new DateNonConvertable(startDate), new DateNonConvertable(endDate));
        if (dayCount > 0) {
            Calendar period = Calendar.getInstance();
            period.setTime(startDate);
            Integer monthId = period.get(Calendar.MONTH);
            Integer year = period.get(Calendar.YEAR);
            BigDecimal totalAllowance = payslipTableItemManager.getEmployeeAllowanceByPeriod(employeeId, monthId, year, CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
            if (totalAllowance.compareTo(BigDecimal.ZERO) > 0) {
                totalPay = totalPay.add(totalAllowance);
            }
            BigDecimal totalAddPayment = additionalPaymentManager.getEmployeeAddPaymentByPeriod(employeeId, monthId, year, CustomFormConstants.LR_TYPE_ANNUAL_LEAVE);
            if (totalAddPayment.compareTo(BigDecimal.ZERO) > 0) {
                totalPay = totalPay.add(totalAddPayment);
            }

            BigDecimal averageMonthlyAmount = totalPay.divide(new BigDecimal(periodCount), 5, RoundingMode.HALF_UP);
            BigDecimal dailyPayAmountAmount = averageMonthlyAmount.add(basicSalaryAmount).divide(new BigDecimal("25.4"), 5, RoundingMode.HALF_UP);

            amount = dailyPayAmountAmount.multiply(new BigDecimal(dayCount)).setScale(2, RoundingMode.HALF_UP);
            basicSalaryPay = basicSalaryAmount.divide(new BigDecimal("25.4"), 5, RoundingMode.HALF_UP).multiply(new BigDecimal(dayCount)).setScale(2, RoundingMode.HALF_UP);

            String remarks = "";
            if (leaveRequestCode != null) {
                remarks += leaveRequestCode + "-> "; //!!! bu "->" belgini o'zgartirishda PaymentCalculationSideNavBox classda split qilib ishlatilgan shu yerga e'tibor berish kerak
            }
            remarks += ServerUtils.getDateAsString(startDate, true) + " - " + ServerUtils.getDateAsString(endDate, true) + " (" + dayCount + ")" + "\n";
            reference.append(remarks);

            String formula = "((totalPay/hirePeriod) + basicSalary)/25.4 * leaveRequestDays";
            String calculation = "((" + totalPay.setScale(2, RoundingMode.HALF_UP) + "/" + periodCount + ") + " + basicSalaryAmount.setScale(2, RoundingMode.HALF_UP) + ")/25.4 * " + dayCount;

            PaymentCalculationDetail paymentCalculationDetail = new PaymentCalculationDetail();
            paymentCalculationDetail.setName(remarks);
            paymentCalculationDetail.setFormula(formula);
            paymentCalculationDetail.setCalculation(calculation);
            paymentCalculationDetail.setAmount(amount);
            calculationDetails.add(paymentCalculationDetail);
        }
        BigDecimal[] amounts = new BigDecimal[2];
        amounts[0] = amount;
        amounts[1] = isAnnual ? basicSalaryPay : BigDecimal.ZERO;
        return amounts;
    }

    public static int monthsBetween(Date d1, Date d2, boolean withoutDay) {
        if (d2 == null || d1 == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d1);
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        int nMonth1 = 12 * calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH);
        calendar.setTime(d2);
        int day2 = calendar.get(Calendar.DAY_OF_MONTH);
        int nMonth2 = 12 * calendar.get(Calendar.YEAR) + calendar.get(Calendar.MONTH);
        if (!withoutDay && day2 - day1 > 0) {
            nMonth2 += 1;
        }
        return java.lang.Math.abs(nMonth2 - nMonth1);
    }

    private BigDecimal calculatePaymentForBackupEmployee(ApproverItemMini backupEmployee, BigDecimal leaveEmployeeSalary, BigDecimal dutyPercentage, BigDecimal numberOfWorkDays, String rateSettings) {
        final boolean isDailyRateByEmployerSettings = "true".equals(rateSettings) || "BY_STATIC_DAY".equals(rateSettings);
        final boolean isCalculationByTimeslot = "BY_TIMESLOT".equals(rateSettings);

        Date startDate = backupEmployee.getFromBackupEmployeeDate().getNonConvertedDate();
        Date toDate = backupEmployee.getDueBackupEmployeeDate().getNonConvertedDate();

        ListingFilterParameter lfp = new ListingFilterParameter();
        lfp.setStartDate(backupEmployee.getFromBackupEmployeeDate().getNonConvertedDate());
        lfp.setEndDate(backupEmployee.getDueBackupEmployeeDate().getNonConvertedDate());
        lfp.setEmployeeId(backupEmployee.getExactEmployee().getId());
        lfp.setDailyRateByEmployerSettings(isDailyRateByEmployerSettings);

        int workedDays;
        if (isCalculationByTimeslot) {
            workedDays = attendanceRawDataManager.getWorkingDays(lfp).size();
        } else {
            workedDays = ServerUtils.countDays(startDate, toDate);
        }
        BigDecimal ratio = BigDecimal.ONE;
        ratio = BigDecimal.valueOf(workedDays).divide(numberOfWorkDays, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        return leaveEmployeeSalary.multiply(ratio).multiply(dutyPercentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
    }

    @Transactional
    public Integer deleteAdditionalPaymentForBackupEmployee(Integer backupEmployeeId) {
        List<EdsAdditionalPayment> payments = additionalPaymentManager.getAdditionalPaymentByBackupsEmployeeId(backupEmployeeId);

        int deleted = 0;
        if (payments != null && !payments.isEmpty()) {
            for (EdsAdditionalPayment payment : payments) {
                deleted += deleteAdditionalPayment(payment.getObjectID());
            }
        }
        return deleted;
    }

    private PaymentDeductionObject createOrUpdateAdditionaPaymentItem(AdditionalPayment data, Integer paymentID, EdsEmployee edsEmployee) {
        PaymentDeductionObject paymentItem = new PaymentDeductionObject();
        BigDecimal basicSalary = null;
        BigDecimal empMode = BigDecimal.ONE;
        boolean isBasicPaymentType = "BASIC_SALARY".equals(data.getPaymentType());
        boolean isAllowenceType = "BASIC_SALARY_ALLOWANCE".equals(data.getPaymentType());
        boolean isMinimumWageType = "MINIMUM_WAGE".equals(data.getPaymentType());
        boolean isEnableWithMiddle = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_FULLNAME_WITH_MIDDLENAME);

        HashMap<String, BigDecimal> lgotaBalanceMap = new HashMap<>();
        List<PaymentDeductionObject> taxCategories = new ArrayList<>();
        List<PaymentDeductionObject> allTaxCategories = new ArrayList<>();
        List<PaymentDeductionObject> allEmployerContributionCategories = new ArrayList<>();
        List<PaymentDeductionObject> employerContributionCategories = new ArrayList<>();
        List<PaymentDeductionObject> deductionCategories = new ArrayList<>();
        List<PaymentDeductionObject> allDeductionCategories = new ArrayList<>();

        ListingFilterParameter filterParameter = data.getFilterParameter();
        if (paymentID != null) {
            EdsPaymentDeduction edsPaymentDeduction = paymentDeductionManager.get(paymentID);
            paymentItem = edsPaymentDeduction.getRPC();
            paymentItem.setItemCustomFields((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(edsPaymentDeduction.getCustomFields(), commonService.getCompanyCustomFields(ViewName.AdditionalPaymentItem)));
            paymentItem.setCountIncident(0);
            edsEmployee = edsPaymentDeduction.getEmployee();
        }


        Calendar calendar = Calendar.getInstance();
        if (filterParameter.getMonthId() == null) {
            filterParameter.setMonthId(calendar.get(Calendar.MONTH));
        }
        if (filterParameter.getYear() == null) {
            filterParameter.setYear(calendar.get(Calendar.YEAR));
        }
        calendar.set(Calendar.MONTH, filterParameter.getMonthId());
        calendar.set(Calendar.YEAR, filterParameter.getYear());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

        List<Integer> employeeIds = new SingletonList(edsEmployee.getObjectID());
        HashMap<Integer, BigDecimal> salaryMap = null;
        String empCode = edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmploymentMode() != null ? edsEmployee.getProfile().getEmploymentMode().getCode() : null;
        if (empCode != null) {
            if ("FULL_TIME".equals(empCode)) {
                empMode = BigDecimal.ONE;
            } else if ("075_TIME".equals(empCode)) {
                empMode = BigDecimal.valueOf(0.75);
            } else if ("PART_TIME".equals(empCode)) {
                empMode = BigDecimal.valueOf(0.50);
            } else if ("QUARTER_TIME".equals(empCode)) {
                empMode = BigDecimal.valueOf(0.25);
            }
        }

        filterParameter.setEmployeeIDs(ServerUtils.getAsCommoDelimited(employeeIds, "0"));

        String[] settingsKeys = {
                PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS,
                PayrollConstants.MATERIAL_AID_TYPE_FUNERAL,
                PayrollConstants.MATERIAL_AID_TYPE_GIFT
        };
        Table<Integer, String, String> employeeSettingsMap = employeePayrollSettingsManager.getEmployeesPayrollSettingMap(employeeIds, settingsKeys);

        if (filterParameter.isCalculateByLastMonth()) {
            //get last month
            calendar.set(Calendar.MONTH, filterParameter.getMonthId());
            calendar.set(Calendar.YEAR, filterParameter.getYear());
            calendar.add(Calendar.MONTH, -1);

            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setEmployeeIDs(filterParameter.getEmployeeIDs());
            fp.setMonthId(calendar.get(Calendar.MONTH));
            fp.setYear(calendar.get(Calendar.YEAR));

            ArrayList<Integer> categoryIds = new ArrayList<>();
            if (filterParameter.isBasicPlusAllowancePaymentType() && !CollectionUtils.isEmpty(filterParameter.getPaymentCategories())) {
                categoryIds = filterParameter.getPaymentCategories().stream().map(SelectItem::getId).collect(Collectors.toCollection(ArrayList::new));
            }
            fp.setObjectIDs(categoryIds);
            salaryMap = payslipPaymentsManager.getEmployeeSalaryForPeriod(fp);
        } else {
            calendar.set(Calendar.MONTH, filterParameter.getMonthId());
            calendar.set(Calendar.YEAR, filterParameter.getYear());
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
            salaryMap = salaryHistoryManager.getEmployeeSalaryMap(employeeIds, calendar.getTime());
        }

        calendar.set(Calendar.MONTH, filterParameter.getMonthId());
        calendar.set(Calendar.YEAR, filterParameter.getYear());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

        SelectItem employeeItem = new SelectItem();
        employeeItem.setId(edsEmployee.getObjectID());
        employeeItem.setName(isEnableWithMiddle ? edsEmployee.getFormmattedName() : edsEmployee.getFullName());
        employeeItem.setDescription(empCode);

        EdsEmployeePayrollSettings settings = employeePayrollSettingsManager.getEmployeeSettingValue(edsEmployee.getObjectID(), SALARY);
        BigDecimal bs = settings != null && settings.getValue() != null ? new BigDecimal(settings.getValue()) : BigDecimal.ZERO;
        basicSalary = bs != null ? bs : BigDecimal.ZERO;
        if (filterParameter.isBasicPlusAllowancePaymentType()) {
            calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
            basicSalary = getTotalAllowancesByEmployee(edsEmployee, filterParameter.getPaymentCategories(), calendar.getTime(), basicSalary);
        }

//        lgotaBalanceMap.put(PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS, calculateMaterialAidBalance(edsEmployee, employeeSettingsMap.row(edsEmployee.getObjectID()), PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS, null, calendar.getTime())); //TODO related to MROT
//        lgotaBalanceMap.put(PayrollConstants.MATERIAL_AID_TYPE_FUNERAL, calculateMaterialAidBalance(edsEmployee, employeeSettingsMap.row(edsEmployee.getObjectID()), PayrollConstants.MATERIAL_AID_TYPE_FUNERAL, null, calendar.getTime()));
//        lgotaBalanceMap.put(PayrollConstants.MATERIAL_AID_TYPE_GIFT, calculateMaterialAidBalance(edsEmployee, employeeSettingsMap.row(edsEmployee.getObjectID()), PayrollConstants.MATERIAL_AID_TYPE_GIFT, null, calendar.getTime()));
//TODO will discuss this later
        List<EdsPaymentDeduction> categories = edsEmployee.getCategories();
        if (categories != null && categories.size() > 0) {
            PaymentDeductionObject object;
            for (EdsPaymentDeduction category : categories) {
                object = category.getRPC();
                if (object.isTaxCategory() || object.isEmployerContributionCategory() || object.isDeductionCategory()) {
                    List<EdsPayrollCategory> linkedCategoryList = categoryManager.getCategoryLinkedCategories(category.getObjectID());
                    if (!CollectionUtils.isEmpty(linkedCategoryList)) {
                        ArrayList<PaymentDeductionObject> categoryList = new ArrayList<>();
                        for (EdsPayrollCategory edsPayrollCategory : linkedCategoryList) {
                            PaymentDeductionObject linkedObject = new PaymentDeductionObject();
                            linkedObject.setCategoryItem(new PaymentDeductionSelectItem(edsPayrollCategory.getObjectID(), edsPayrollCategory.getName(), edsPayrollCategory.getCode(), edsPayrollCategory.getType()));
                            categoryList.add(linkedObject);
                        }
                        object.setLinkedCategories(categoryList);
                    }
                    if (object.isTaxCategory()) {
                        allTaxCategories.add(object);
                    } else if (object.isEmployerContributionCategory()) {
                        allEmployerContributionCategories.add(object);
                    } else if (object.isDeductionCategory()) {
                        allDeductionCategories.add(object);
                    }
                }
            }
        }

        if (data.getDefaultDate() != null) {
            paymentItem.setAdditionalPaymentDate(data.getDefaultDate());
        }

        if (paymentID == null) {
            if (isBasicPaymentType || isAllowenceType) {
                BigDecimal percentage = data.getPercentage();

                if (isAllowenceType) {
                    paymentItem.setBasicPlusAllowance(basicSalary);
                } else if (isBasicPaymentType) {
                    paymentItem.setEmployeeBasicSalary(basicSalary);
                }
                paymentItem.setPercentage(percentage);

                BigDecimal payAmount = BigDecimal.ZERO;
                if (basicSalary != null && percentage != null && basicSalary.compareTo(BigDecimal.ZERO) > 0 && percentage.compareTo(BigDecimal.ZERO) > 0) {
                    payAmount = basicSalary.multiply(percentage.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                }
                paymentItem.setPaymentAmount(payAmount);
            } else {
                paymentItem.setBasicPlusAllowance(BigDecimal.ZERO);
                paymentItem.setEmployeeBasicSalary(BigDecimal.ZERO);
                paymentItem.setPercentage(BigDecimal.ZERO);

                BigDecimal payAmountValue = data.getFixedAmount();
                if (!(isBasicPaymentType || isAllowenceType)) {
                    payAmountValue = payAmountValue.multiply(empMode).setScale(2, RoundingMode.HALF_UP);
                }
                paymentItem.setPaymentAmount(payAmountValue);
            }
        }
        EdsPayrollCategory edsPayrollCategory = paymentItem.getCategoryItem() != null ? categoryManager.get(paymentItem.getCategoryItem().getId()) : categoryManager.get(data.getDefaultPayrollCategoryId());
        paymentItem.setCategoryItem(edsPayrollCategory.createPaymentDeductionSelectItem());
        BigDecimal taxableAmount = paymentItem.getPaymentAmount();
        BigDecimal taxTotal = BigDecimal.ZERO;
        BigDecimal employerContributionTotal = BigDecimal.ZERO;
        BigDecimal deductionTotal = BigDecimal.ZERO;
        if (!data.isShowInPayslip()) {
            if (PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS.equals(edsPayrollCategory.getSystemCode()) ||
                    PayrollConstants.MATERIAL_AID_TYPE_FUNERAL.equals(edsPayrollCategory.getSystemCode()) ||
                    PayrollConstants.MATERIAL_AID_TYPE_GIFT.equals(edsPayrollCategory.getSystemCode())) {
                BigDecimal balance = lgotaBalanceMap.getOrDefault(edsPayrollCategory.getSystemCode(), BigDecimal.ZERO);
                taxableAmount = taxableAmount.subtract(balance);
                taxableAmount = taxableAmount.compareTo(BigDecimal.ZERO) > 0 ? taxableAmount : BigDecimal.ZERO;
            }

            if (!CollectionUtils.isEmpty(allTaxCategories) && taxableAmount.compareTo(BigDecimal.ZERO) > 0) {
                for (PaymentDeductionObject taxCategory : allTaxCategories) {
                    boolean findCategory = false;
                    if (taxCategory.isFromAllAllowances()) {
                        findCategory = true;
                    } else if (taxCategory != null && !taxCategory.isSalaryObject() && taxCategory.getType() != null && taxCategory.getType().equals(2) && taxCategory.getLinkedCategories() != null && taxCategory.getLinkedCategories().size() > 0) {
                        for (PaymentDeductionObject taxAllowanceCategory : taxCategory.getLinkedCategories()) {
                            if (taxAllowanceCategory != null && taxAllowanceCategory.getCategoryItem() != null && edsPayrollCategory.getObjectID().equals(taxAllowanceCategory.getCategoryItem().getId())) {
                                findCategory = true;
                                break;
                            }
                        }
                    }
                    if (findCategory) {
                        taxCategories.add(taxCategory);
                        BigDecimal taxAmount = taxableAmount.multiply(taxCategory.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                        taxTotal = taxTotal.add(taxAmount);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(allEmployerContributionCategories)) {
                for (PaymentDeductionObject empContCategory : allEmployerContributionCategories) {
                    boolean findCategory = false;
                    if (empContCategory.isFromAllAllowances()) {
                        findCategory = true;
                    } else if (empContCategory != null && !empContCategory.isSalaryObject() && empContCategory.getType() != null && empContCategory.getType().equals(2) && empContCategory.getLinkedCategories() != null && empContCategory.getLinkedCategories().size() > 0) {
                        for (PaymentDeductionObject empContAllowanceCategory : empContCategory.getLinkedCategories()) {
                            if (empContAllowanceCategory != null && empContAllowanceCategory.getCategoryItem() != null && edsPayrollCategory.getObjectID().equals(empContAllowanceCategory.getCategoryItem().getId())) {
                                findCategory = true;
                            }
                        }
                    }
                    if (findCategory) {
                        employerContributionCategories.add(empContCategory);
                        BigDecimal empContrAmount = taxableAmount.multiply(empContCategory.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                        employerContributionTotal = employerContributionTotal.add(empContrAmount);
                    }
                }
            }

            if (!CollectionUtils.isEmpty(allDeductionCategories)) {
                for (PaymentDeductionObject deductionCategory : allDeductionCategories) {
                    if (paymentItem.getAdditionalPaymentDate() != null &&
                            (deductionCategory.getStarttDate() == null || !paymentItem.getAdditionalPaymentDate().getNonConvertedDate().before(deductionCategory.getStarttDate().getNonConvertedDate())) &&
                            (deductionCategory.getEnddDate() == null || !paymentItem.getAdditionalPaymentDate().getNonConvertedDate().after(deductionCategory.getEnddDate().getNonConvertedDate()))
                    ) {
                        boolean findCategory = false;
                        BigDecimal deductableAmount = taxableAmount;

                        if (Integer.valueOf(4).equals(deductionCategory.getType()) && !edsPayrollCategory.getExcludeInCustomDeductions()) {
                            deductableAmount = taxableAmount.subtract(taxTotal);
                            findCategory = true;
                        } else if (Integer.valueOf(2).equals(deductionCategory.getType()) && deductionCategory.getLinkedCategories() != null && deductionCategory.getLinkedCategories().size() > 0) {
                            for (PaymentDeductionObject deductionAllowanceCategory : deductionCategory.getLinkedCategories()) {
                                if (deductionAllowanceCategory != null && deductionAllowanceCategory.getCategoryItem() != null && edsPayrollCategory.getObjectID().equals(deductionAllowanceCategory.getCategoryItem().getId())) {
                                    findCategory = true;
                                }
                            }
                        } else if (deductionCategory.isFromAllAllowances()) {
                            findCategory = true;
                        }

                        if (findCategory) {
                            deductionCategories.add(deductionCategory);
                            BigDecimal deductionAmount = deductableAmount.multiply(deductionCategory.getPercentage()).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            deductionTotal = deductionTotal.add(deductionAmount);
                        }
                    }
                }
            }
        }
        paymentItem.setTax(taxTotal);
        paymentItem.setTaxCategories(taxCategories);
        paymentItem.setEmployerContributionCategories(employerContributionCategories);
        paymentItem.setEmployerContribution(employerContributionTotal);
        paymentItem.setDeduction(deductionTotal);
        paymentItem.setDeductionCategories(deductionCategories);
        BigDecimal totalValue = paymentItem.getPaymentAmount().subtract(taxTotal).subtract(deductionTotal).setScale(2, RoundingMode.HALF_UP);
        if (!data.isShowInPayslip() && edsPayrollCategory.isNonMoneyType()) {
            totalValue = BigDecimal.ZERO;
        }
        paymentItem.setTotalAmount(totalValue);
        int monthDayCount = ServerUtils.getMonthDaysCountInYear(data.getMonthID(), data.getYear());
        paymentItem.setStarttDate(new DateNonConvertable(new Date(data.getYear() - 1900, data.getMonthID(), 1)));
        paymentItem.setEnddDate(new DateNonConvertable(new Date(data.getYear() - 1900, data.getMonthID(), monthDayCount)));

        return paymentItem;
    }

    @Transactional
    public void batchChangeAdditionalPaymentStatus(Integer id, String statusCode) {
        if (id != null && !StringUtil.isEmpty(statusCode)) {
            EdsAdditionalPayment edsAdditionalPayment = additionalPaymentManager.get(id);
            EdsReference status = referenceManager.getByCode(statusCode);
            edsAdditionalPayment.setEntityStatus(status);

            List<Object> totalAmount = paymentDeductionManager.getAdditionalPaymentTotalAmount(id);
            BigDecimal total = BigDecimal.ZERO;
            if (!CollectionUtils.isEmpty(totalAmount)) {
                total = (BigDecimal) totalAmount.get(0);
            }
            edsAdditionalPayment.setTotal(total);
            additionalPaymentManager.update(edsAdditionalPayment);
            allInOneServiceLocal.approvedOrRejected(RelationItem.TYPE_ADDITIONAL_PAYMENT, id, null);
        }
    }

    @Transactional
    public Boolean deleteLineItems(Integer itemId) {
        if (itemId != null) {
            paymentDeductionManager.deletePaymentOrDeduction(itemId);
            return true;
        }
        return false;
    }

    private BigDecimal calculateSalary(Integer employeeID, PayslipItemFilter filter, BigDecimal numberOfWorkDay,
                                       Map<Date, BigDecimal> workingHours, boolean isCalculationByTimeslot, List<EmployeeSalary> employeeSalaries, String employeeSalaryMode, boolean enabledLeavePayments) {
        BigDecimal salary = BigDecimal.ZERO;
        List<SalaryHistory> salaryHistories = filter.getSalaryHistories();

        // Check for null or exact zero
        if (numberOfWorkDay == null || BigDecimal.ZERO.equals(numberOfWorkDay)) {
            return BigDecimal.ZERO;
        }
        // Check if the denominator is below the threshold
        if (numberOfWorkDay.abs().compareTo(MIN_THRESHOLD) < 0) {
            // Handle small denominator (return default value, throw exception, etc.)
            return BigDecimal.ZERO;
        }

        Date fromDate = filter.getFromDate().getNonConvertedDate();
        BigDecimal effectiveSalary = salaryHistoryManager.getEmployeeLastSalaryHistory(employeeID, fromDate);
        if (effectiveSalary == null) {
            effectiveSalary = BigDecimal.ZERO;
        }

        if (salaryHistories != null && !CollectionUtils.isEmpty(salaryHistories)) {
            salaryHistories.sort(Comparator.comparing(SalaryHistory::getNonConvertableEffectiveDate));

            for (SalaryHistory history : salaryHistories) {
                Date toDate = history.getEffectiveDate().getNonConvertedDate();
                toDate = ServerUtils.addDays(toDate, -1);
                long diff = ServerUtils.countDays(fromDate, toDate);
                BigDecimal realWorkedDays = BigDecimal.ZERO;
                if (diff > 0) {
                    Set<Date> leaveDays = getLeaveRequestDaysByPeriod(employeeID, fromDate, toDate, employeeSalaryMode, isCalculationByTimeslot ? AttendanceRawDataManagerImpl.WORKING_DATES : AttendanceRawDataManagerImpl.ALL_DATES);
                    int leaveDayCount = leaveDays != null ? leaveDays.size() : 0;
                    if (isCalculationByTimeslot) {
                        BigDecimal workedHours = PayrollUtils.getNumberOfHours(fromDate, toDate, workingHours, leaveDays);
                        realWorkedDays = workedHours.divide(BigDecimal.valueOf(8), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    } else {
                        realWorkedDays = (diff - leaveDayCount) > 0 ? BigDecimal.valueOf(diff - leaveDayCount) : BigDecimal.ZERO;
                    }
                    if (realWorkedDays.compareTo(BigDecimal.ZERO) > 0) {
                        salary = salary.add(effectiveSalary.multiply(realWorkedDays));
                        EmployeeSalary employeeSalary = new EmployeeSalary(effectiveSalary, realWorkedDays);
                        employeeSalary.setFromDate(fromDate);
                        employeeSalary.setToDate(toDate);
                        employeeSalaries.add(employeeSalary);
                    }
                }
                effectiveSalary = history.getSalary();
                fromDate = history.getEffectiveDate().getNonConvertedDate().after(filter.getFromDate().getNonConvertedDate()) ? history.getEffectiveDate().getNonConvertedDate() : filter.getFromDate().getNonConvertedDate();
            }
        }

        Date toDate = filter.getToDate().getNonConvertedDate();
        long diff = ServerUtils.countDays(fromDate, toDate) + 1;//include last day
        BigDecimal realWorkedDays = BigDecimal.ZERO;
        if (diff > 0) {
            Set<Date> leaveDays = getLeaveRequestDaysByPeriod(employeeID, fromDate, toDate, employeeSalaryMode, isCalculationByTimeslot ? AttendanceRawDataManagerImpl.WORKING_DATES : AttendanceRawDataManagerImpl.ALL_DATES);
            int leaveDayCount = leaveDays != null && enabledLeavePayments ? leaveDays.size() : 0;
            if (isCalculationByTimeslot) {
                BigDecimal workedHours = PayrollUtils.getNumberOfHours(fromDate, toDate, workingHours, leaveDays);
                realWorkedDays = workedHours.divide(BigDecimal.valueOf(8), ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
            } else {
                realWorkedDays = (diff - leaveDayCount) > 0 ? BigDecimal.valueOf(diff - leaveDayCount) : BigDecimal.ZERO;
            }
            if (realWorkedDays.compareTo(BigDecimal.ZERO) > 0) {
                salary = salary.add(effectiveSalary.multiply(realWorkedDays));
            }
            EmployeeSalary employeeSalary = new EmployeeSalary(effectiveSalary, realWorkedDays);
            employeeSalary.setFromDate(fromDate);
            employeeSalary.setToDate(toDate);
            employeeSalaries.add(employeeSalary);
        }

        return salary.divide(numberOfWorkDay, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
    }

    private Set<Date> getLeaveRequestDaysByPeriod(Integer employeeId, Date fromDate, Date toDate, String salaryMode, String datesType) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEmployeeId(employeeId);
        fp.setStatusCode(EdsSickRequest.APPROVED);
        fp.setStartDate(fromDate);
        fp.setEndDate(toDate);
        fp.setExcludedType(salaryMode);

        return attendanceRawDataManager.getLeaveDays(fp, datesType);
    }

    private void collectLinkedCategories(PaymentDeductionObject object) {
        List<PaymentDeductionObject> linkedCategories = !CollectionUtils.isEmpty(object.getLinkedCategories()) ? new ArrayList<>() : object.getLinkedCategories();
        Map<Integer, PaymentDeductionObject> allLinkedCategories = new HashMap<>();
        if (!CollectionUtils.isEmpty(linkedCategories)) {
            for (PaymentDeductionObject linkedCategory : linkedCategories) {
                if (linkedCategory.getCategoryItem() != null && allLinkedCategories.get(linkedCategory.getCategoryItem().getId()) != null) {
                    allLinkedCategories.put(linkedCategory.getCategoryItem().getId(), linkedCategory);
                }
            }
        }
        EdsPaymentDeduction edsPaymentDeduction = paymentDeductionManager.get(object.getId());
        if (edsPaymentDeduction.getPayrollGlobalSettingsItem() != null && !CollectionUtils.isEmpty(edsPaymentDeduction.getPayrollGlobalSettingsItem().getLinkedCategories())) {
            for (EdsPayrollCategory linkedCategory : edsPaymentDeduction.getPayrollGlobalSettingsItem().getLinkedCategories()) {
                if (allLinkedCategories.get(linkedCategory.getObjectID()) == null) {
                    PaymentDeductionObject linkedObject = new PaymentDeductionObject();
                    linkedObject.setCategoryItem(linkedCategory.createPaymentDeductionSelectItem());
                    allLinkedCategories.put(linkedCategory.getObjectID(), linkedObject);
                    linkedCategories.add(linkedObject);
                }
            }
        }
        if (linkedCategories.size() > 0) {
            object.setLinkedCategories(linkedCategories);
        }
    }

    @Transactional
    public void createPayslipTableItems(GroupPayrunData item, PayslipFilter filter, Integer payslipTableId) {
        final String database = ServerSecurityContext.getInstance().getDatabase();
        final String companyId = ServerSecurityContext.getInstance().getCompanyId();
        final Integer userId = userManager.getUser().getObjectID();

        if (item.getObjectID() == null) {
            filter.setLimit(null);
            filter.setStart(null);
            filter.setSearchKey(null);
            if (item.getDeletedItems() != null && !item.getDeletedItems().isEmpty()) {
                filter.setAvoidEmployees(new ArrayList<>(item.getDeletedItems().keySet()));
            }
            if (filter == null || filter.getFromDate() == null || filter.getToDate() == null || (filter.getPayrollBatchID() == null && filter.getProjectId() == null && filter.getLocationId() == null)) {
                return;
            }
            if (filter.isEnabledMultiCurrency()) {
                EdsCurrency currency = financialSettingsManager.getFinancialSettings().getCurrency();

                if (filter.getPayrollBatchID() != null) {
                    EdsPayrollBatch payrollBatch = payrollBatchManager.get(filter.getPayrollBatchID());

                    if (payrollBatch != null && payrollBatch.getCurrency() != null) {
                        currency = payrollBatch.getCurrency();
                    }
                }
            }
            Integer itemCount = payslipTableManager.getEmployeeDataForGroupPayrunCount(filter);

            if (itemCount <= 0) {
                return;
            }
            ArrayList<Integer> employeeIds = new ArrayList<>(payslipTableManager.getEmployeeListDataForGroupPayrun(filter));

            if (employeeIds.isEmpty()) {
                return;
            }

            EdsCurrency baseCurrency = financialSettingsManager.getFinancialSettings().getCurrency();
            boolean isEmployeeCodeInteger = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER);
            Map<String, PaymentDeductionSelectItem> categoryMap = categoryManager.getCategoryItemMapByCodes(LEAVE_DEDUCTIONS,
                    LEAVE_ENCHASHMENT,
                    BENEFIT_PAYMENT,
                    EXPENSE_REPORT,
                    REGULAR_OVERTIME,
                    WEEKEND_OVERTIME,
                    HOLIDAY_OVERTIME,
                    ADDITIONAL_PAYMENT,
                    ABSENCE_DEDUCTIONS,
                    BONUS);

            Map<String, String> settingsMap = companyPayrollSettingsManager.getCompanyPayrollSettingsMap(NON_PAID_LEAVE_DAYS_IMPACT,
                    LEAVE_DAYS_IMPACT,
                    DAILY_RATE_BY_EMPLOYER_SETTINGS,
                    ENABLED_LEAVE_DEDUCTIONS,
                    ENABLED_LEAVE_PAYMENTS,
                    NUMBER_OF_WORK_DAYS,
                    LEAVE_MONEY_TYPE_CATEGORY,
                    DEDUCT_TYPE,
                    DEDUCT_ALLOWANCES,
                    LEAVE_DAILY_PAYMENT_TYPE,
                    LEAVE_DAILY_ALLOWANCES,
                    LEAVE_MONEY_PAYMENT_TYPE,
                    LEAVE_MONEY_ALLOWANCES);
            EdsPayrollCategory leaveMTCategory = categoryManager.getCategoryByCode(LEAVE_SALARY);
            PaymentDeductionSelectItem leaveMTCategoryItem = leaveMTCategory != null ? leaveMTCategory.createPaymentDeductionSelectItem() : null;

            List<PaymentDeductionObject> leaveDeductionLinkedCategories = loadLeaveSettings(settingsMap.get(DEDUCT_ALLOWANCES));
            List<PaymentDeductionObject> leaveDailyTypeLinkedCategories = loadLeaveSettings(settingsMap.get(LEAVE_DAILY_ALLOWANCES));
            List<PaymentDeductionObject> leaveMoneyTypeLinkedCategories = loadLeaveSettings(settingsMap.get(LEAVE_MONEY_ALLOWANCES));

            boolean isLeaveSettingsCalculationEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SICK_LEAVE_SETTINGS_CALCULATION);
            Map<Integer, Double[]> spentMinutes = Maps.newHashMapWithExpectedSize(employeeIds.size());
            Map<Integer, Integer> annualLeaveAllowanceMap = Maps.newHashMapWithExpectedSize(employeeIds.size());

            if (isLeaveSettingsCalculationEnabled && filter.getYear() != null && filter.getMonth() != null) {

                Integer approvedStatusId = referenceManager.findReferenceId(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED);

                annualLeaveAllowanceMap.putAll(annualLeaveAllowanceManager.getLastYearMinutesMapByYearAndReasonAndEmployee(filter.getYear(),
                        EdsSickRequest.LR_TYPE_ANNUAL_LEAVE,
                        employeeIds));
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setYear(filter.getYear());
                fp.setReasonCode(EdsSickRequest.LR_TYPE_ANNUAL_LEAVE);
                fp.setStatusID(approvedStatusId);
                fp.setAnnualLeave(true);
                fp.setObjectIDs(employeeIds);

                spentMinutes.putAll(sickRequestDurationManager.getAllowanceSpentByEmployees(fp));
            }
            EdsCompany company = userManager.getUser().getCompany();
            Multimap<Integer, PaymentDeductionObject> paymentDeductionsMap = paymentDeductionManager.getEmployeesPaymentDeductionMap(employeeIds, filter);
            String[] settingsKeys = {RATE_TYPE,
                    SALARY,
                    REGULAR_OVERTIME_RATE,
                    REGULAR_OVERTIME_RATE_TYPE,
                    WEEKEND_OVERTIME_RATE,
                    WEEKEND_OVERTIME_RATE_TYPE,
                    HOLIDAY_OVERTIME_RATE,
                    HOLIDAY_OVERTIME_RATE_TYPE,
                    PayrollConstants.MATERIAL_AID_TYPE_FAMILY_AFFAIRS,
                    PayrollConstants.MATERIAL_AID_TYPE_FUNERAL,
                    PayrollConstants.MATERIAL_AID_TYPE_GIFT
            };
            Table<Integer, String, String> employeeSettingsMap = employeePayrollSettingsManager.getEmployeesPayrollSettingMap(employeeIds, settingsKeys);

//            BigDecimal mrotValue = commonServiceLocal.getMrotValueByDate(filter.getToDate().getNonConvertedDate(), true); //TODO related to MROT

            ListingFilterParameter lp = new ListingFilterParameter();
            lp.setObjectIDs(employeeIds);
            lp.setStartDate(filter.getFromDate().getNonConvertedDate());
            lp.setEndDate(filter.getToDate().getNonConvertedDate());
            HashMap<Integer, List<SalaryHistory>> salaryHistoryMap = salaryHistoryManager.getEmployeeSalaryHistoryMap(lp);

            Integer baseCurrencyId = baseCurrency != null ? baseCurrency.getObjectID() : null;
            boolean hasCountry = company != null && company.getCountryZone() != null && company.getCountryZone().getCountry() != null;
            Integer countryId = hasCountry ? company.getCountryZone().getCountry().getObjectID() : null;
            String countryCode = hasCountry ? company.getCountryZone().getCountry().getCode() : "";

            HashMap<Integer, SinglePayrunItem> changedItems = item.getChangedItems();

            payslipTableManager.updatePayslipTableTotalItems(employeeIds.size(), payslipTableId);
            payslipTableManager.flushAndClear();

            AtomicInteger index = new AtomicInteger();
            for (Integer employeeId : employeeIds) {
                executor.execute(() -> {
                    ServerSecurityContext.getInstance().setDatabase(database);
                    ServerSecurityContext.getInstance().setCompanyId(companyId);
                    ServerSecurityContext.getInstance().setStaticUserID(userId);

                    SinglePayrunItem singlePayrunItem = null;
                    if (changedItems != null && changedItems.containsKey(employeeId)) {
                        singlePayrunItem = changedItems.remove(employeeId);
                    } else {
                        PayslipItemFilter itemFilter = PayslipItemFilter.fromPayslipFilter(filter);
                        itemFilter.setEmployeeID(employeeId);
                        itemFilter.setEmployeeCodeInteger(isEmployeeCodeInteger);

                        itemFilter.setPayslipTableId(payslipTableId);
                        itemFilter.setStatus(item.getStatus());

                        itemFilter.setBaseCurrencyId(baseCurrencyId);
                        itemFilter.setCountryId(countryId);
                        itemFilter.setCountryCode(countryCode);
//                        itemFilter.setMrotValue(mrotValue); //TODO related to MROT

                        itemFilter.setSpentMinutes(spentMinutes.get(employeeId));
                        itemFilter.setCategoryMap(categoryMap);
                        itemFilter.setCompanyPayrollSettingsMap(settingsMap);

                        itemFilter.setLeaveSettingsCalculationEnabled(isLeaveSettingsCalculationEnabled);
                        itemFilter.setLeaveMTCategoryItem(leaveMTCategoryItem);
                        itemFilter.setLeaveDailyTypeLinkedCategories(leaveDailyTypeLinkedCategories);
                        itemFilter.setLeaveDeductionLinkedCategories(leaveDeductionLinkedCategories);
                        itemFilter.setLeaveMoneyTypeLinkedCategories(leaveMoneyTypeLinkedCategories);
                        itemFilter.setSalaryHistories(salaryHistoryMap.get(employeeId));
                        itemFilter.setEmployeeSettingsMap(employeeSettingsMap.row(employeeId));
                        itemFilter.setLastYearMinutes(annualLeaveAllowanceMap.get(employeeId));
                        itemFilter.setPaymentDeductions(((ArrayListMultimap<Integer, PaymentDeductionObject>) paymentDeductionsMap).get(employeeId));
                        singlePayrunItem = generateSinglePayrun(itemFilter);
                    }
                    singlePayrunItem.setGroupPayrunID(payslipTableId);
                    singlePayrunItem.setStatus(item.getStatus());

                    if (item.getProjectItem() != null) {
                        singlePayrunItem.setProjectItem(item.getProjectItem());
                    }

                    Integer objectId = payrollAsyncService.saveSinglePayrunItem(singlePayrunItem, (payrunItem, edsPayslipItem) -> {
                        registerPaymentDeductionCategories(payrunItem, edsPayslipItem);
                        registerEmployeeExpenses(payrunItem.getEmployeeExpenses(), edsPayslipItem);
                    });
                    addSinglePayrunToSolr(objectId);
                    index.getAndIncrement();
                    if (index.get() == employeeIds.size()) {
                        payrollAsyncService.getInNewTransaction(() -> {
                            batchChangePayrollGroupStatus(payslipTableId, item.getStatus());
                            return null;
                        });
                    }
                });
            }
        } else {
            batchChangePayrollGroupStatus(payslipTableId, item.getStatus());
        }
    }

    @Transactional
    public void createSinglePayrun(PayslipItemFilter itemFilter) {
        EdsPayslipTable payslipTable = payslipTableManager.get(itemFilter.getPayslipTableId());
        ServerSecurityContext.getInstance().setStaticUserID(payslipTable.getPreparer() != null ? payslipTable.getPreparer().getObjectID() : null);
        try {
            SinglePayrunItem singlePayrunItem = generateSinglePayrun(itemFilter);
            singlePayrunItem.setGroupPayrunID(itemFilter.getPayslipTableId());
            singlePayrunItem.setStatus(itemFilter.getStatus());
            Integer objectId = payrollAsyncService.saveSinglePayrunItem(singlePayrunItem, (payrunItem, edsPayslipItem) -> {
                registerPaymentDeductionCategories(payrunItem, edsPayslipItem);
                registerEmployeeExpenses(payrunItem.getEmployeeExpenses(), edsPayslipItem);
            });
            addSinglePayrunToSolr(objectId);

            payslipTable.setSuccessItems(payslipTable.getSuccessItems() + 1);
            payslipTableManager.update(payslipTable);

            if (payslipTable.getTotalItems() <= payslipTable.getSuccessItems() + payslipTable.getFailedItems()) {
//                payrollAsyncService.getInNewTransaction(() -> {
                batchChangePayrollGroupStatus(payslipTable, itemFilter.getStatus());
//                    return null;
//                });
//                payrollAsyncService.applyGroupPayrunTotal(itemFilter.getPayslipTableId());
            }
        } catch (Exception e) {
            payslipTable.setFailedItems(payslipTable.getFailedItems() + 1);
            payslipTableManager.update(payslipTable);

            if (payslipTable.getTotalItems() <= payslipTable.getSuccessItems() + payslipTable.getFailedItems()) {
//                payrollAsyncService.getInNewTransaction(() -> {
                batchChangePayrollGroupStatus(payslipTable, itemFilter.getStatus());
//                    return null;
//                });
//                payrollAsyncService.applyGroupPayrunTotal(itemFilter.getPayslipTableId());
            }
        }
        SecurityContext.getInstance().setStaticUserID(null);
    }

    static class LeaveBalanceCalculationItem {
        private Double lrDays = 0d;
        private Double lrTakeMoneyDays = 0d;
        private Double lrHours = 0d;
        private BigDecimal dailyRate = BigDecimal.ZERO;
        private BigDecimal moneyRate = BigDecimal.ZERO;

        LeaveBalanceCalculationItem() {
        }

        public Double getLrDays() {
            return lrDays;
        }

        public void setLrDays(Double lrDays) {
            this.lrDays = lrDays;
        }

        public Double getLrHours() {
            return lrHours;
        }

        public void setLrHours(Double lrHours) {
            this.lrHours = lrHours;
        }

        public BigDecimal getDailyRate() {
            return dailyRate;
        }

        public void setDailyRate(BigDecimal dailyRate) {
            this.dailyRate = dailyRate;
        }

        public Double getLrTakeMoneyDays() {
            return lrTakeMoneyDays;
        }

        public void setLrTakeMoneyDays(Double lrTakeMoneyDays) {
            this.lrTakeMoneyDays = lrTakeMoneyDays;
        }

        public BigDecimal getMoneyRate() {
            return moneyRate;
        }

        public void setMoneyRate(BigDecimal moneyRate) {
            this.moneyRate = moneyRate;
        }

    }

    @Override
    public ListResult<SelectItem> getPayrollZones(ListingFilterParameter fp) {
        List<EdsPayrollZone> zones = Optional.ofNullable(payrollZoneManager.findZones()).orElse(new ArrayList<>());
        return new ListResult<>(zones.stream().map(EdsPayrollZone::getAsSelectItem).collect(Collectors.toCollection(ArrayList::new)), zones.size());
    }

    @Transactional
    @Override
    public Boolean saveIndustrySettings(String value) {
        final EdsCompanyPayrollSettings settings = companyPayrollSettingsManager.getCompanySettingValue("INDUSTRY_ID");
        settings.setValue(value);
        companyPayrollSettingsManager.update(settings);
        return true;
    }
}
