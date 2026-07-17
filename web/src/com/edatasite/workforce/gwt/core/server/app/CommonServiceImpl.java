package com.edatasite.workforce.gwt.core.server.app;

import au.com.bytecode.opencsv.CSVReader;
import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.components.ImageScaleDown;
import com.edatasite.workforce.core.domain.EdsAddViewSettings;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsAttendanceTerminal;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsContainer;
import com.edatasite.workforce.core.domain.EdsContainerItem;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCountryZone;
import com.edatasite.workforce.core.domain.EdsCustomFieldListener;
import com.edatasite.workforce.core.domain.EdsCustomFieldValidation;
import com.edatasite.workforce.core.domain.EdsCustomQuizFormScore;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmbassy;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsEntityType;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsFormProperty;
import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsLocalization;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.EdsMoreMenuSettings;
import com.edatasite.workforce.core.domain.EdsNoteComment;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsOnboardingStep;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRentalOrder;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsShift;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsSkill;
import com.edatasite.workforce.core.domain.EdsSubscriptionPayment;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.EdsTimeTrack;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.EdsUserFingerPrintDevice;
import com.edatasite.workforce.core.domain.EdsUserLastRequest;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.core.domain.EdsUserSettings;
import com.edatasite.workforce.core.domain.EdsUsersFingerPrint;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.EdsWFTPlagin;
import com.edatasite.workforce.core.domain.StaffInOut;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsItemBatch;
import com.edatasite.workforce.core.domain.accounting.EdsItemTableSettings;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsUnitMeasurement;
import com.edatasite.workforce.core.domain.analyzer.EdsSolrDbConsistency;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.approving.EdsApproverEmployees;
import com.edatasite.workforce.core.domain.approving.EdsApproverRoles;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFormCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFormNote;
import com.edatasite.workforce.core.domain.customfields.EdsCustomItemTableCF;
import com.edatasite.workforce.core.domain.customform.EdsCFItemTableSetting;
import com.edatasite.workforce.core.domain.customform.EdsCustomForm;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormAttributes;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormItems;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormLocalization;
import com.edatasite.workforce.core.domain.customform.EdsCustomFormSection;
import com.edatasite.workforce.core.domain.customform.EdsCustomItemTable;
import com.edatasite.workforce.core.domain.customform.EdsEmployeeCustomItemTable;
import com.edatasite.workforce.core.domain.customform.EdsModel;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.customform.EdsModelFieldCustom;
import com.edatasite.workforce.core.domain.customform.EdsOpportunityCustomItemTable;
import com.edatasite.workforce.core.domain.customform.EdsProjectCustomItemTable;
import com.edatasite.workforce.core.domain.dashboard.EdsDashboard;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.enums.DateTermsEnum;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsFacetFilter;
import com.edatasite.workforce.core.domain.rbac.facetfilter.EdsUserFilter;
import com.edatasite.workforce.core.domain.recruitment.EdsCandidateItemTable;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacementItemTable;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancyItemTable;
import com.edatasite.workforce.core.domain.settings.EdsForceShowGuidePanelSettings;
import com.edatasite.workforce.core.domain.settings.EdsKPIContactDetails;
import com.edatasite.workforce.core.domain.settings.EdsKanbanItemSettings;
import com.edatasite.workforce.core.domain.settings.EdsListPanelGuideSettings;
import com.edatasite.workforce.core.domain.settings.EdsListPanelSettings;
import com.edatasite.workforce.core.domain.settings.EdsOverdueInvoiceReminderSettings;
import com.edatasite.workforce.core.domain.settings.EdsQuickAddSettings;
import com.edatasite.workforce.core.domain.trainingcenter.EdsStudent;
import com.edatasite.workforce.core.solr.component.CustomFormItemSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeStepSolrComponent;
import com.edatasite.workforce.core.solr.component.VacancySolrComponent;
import com.edatasite.workforce.core.solr.document.CustomFormItemSolrDoc;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.NewLeaveRequest;
import com.edatasite.workforce.gwt.availability.client.rpc.TimeSlot;
import com.edatasite.workforce.gwt.client.client.rpc.ClientCurrency;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.core.client.enums.AttendanceHoursType;
import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.enums.UserSettingsTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomFormAttributeItem;
import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.form.formbuild.CustomFormItem;
import com.edatasite.workforce.gwt.core.client.form.formbuild.CustomFormRuleItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.AttendanceReportLogItem;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyAddress;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCFAndFormItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.ContactTypeForTwilio;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldSettingItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.DocumentsSearchItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeForTwilio;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeePresentItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.MyUpdateItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.OpportunityItemForTwilio;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SearchResultItem;
import com.edatasite.workforce.gwt.core.client.rpc.SearchResultItemList;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TwilioContactItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.SaveFilterSelectItems;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.placeofsupply.PlaceOfSupplyItem;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddSettingsForm;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCaseRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCustomFormConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrNewsRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSaleInvoiceRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceDeviceStatus;
import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceTerminal;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewAddFiledsCodeName;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelGuideSettingsRPC;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.AdvancedSearchRpc;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.ModuleOverallSearchRpc;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.ModuleSectionConstants;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.ModuleSectionRpc;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.OverallSearchRpc;
import com.edatasite.workforce.gwt.core.client.ui.search.rpc.SearchModuleType;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.server.app.social.zoom.ZoomService;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.AddViewSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.AttendanceHoursManager;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.AttendanceTerminalManager;
import com.edatasite.workforce.gwt.core.server.db.BenefitRequestManager;
import com.edatasite.workforce.gwt.core.server.db.CFCommitBoxNoteManager;
import com.edatasite.workforce.gwt.core.server.db.CFItemTableSettingmanager;
import com.edatasite.workforce.gwt.core.server.db.CandidateItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ContactCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.ContainerItemManager;
import com.edatasite.workforce.gwt.core.server.db.ContainerManager;
import com.edatasite.workforce.gwt.core.server.db.ContractManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormAttributeManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormCFManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormItemManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormLocalizationManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormNoteManager;
import com.edatasite.workforce.gwt.core.server.db.CustomFormSectionManager;
import com.edatasite.workforce.gwt.core.server.db.CustomItemTableCFManager;
import com.edatasite.workforce.gwt.core.server.db.CustomItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.CustomQuizFormManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.FormPropertyManager;
import com.edatasite.workforce.gwt.core.server.db.HostBasedSettingManager;
import com.edatasite.workforce.gwt.core.server.db.ImportFileManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.LocalizationManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.ModelManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.MoreMenuSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.NoteCommentManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.OnboardingStepManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PermissionManager;
import com.edatasite.workforce.gwt.core.server.db.PlacementItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.ShiftManager;
import com.edatasite.workforce.gwt.core.server.db.SkillManager;
import com.edatasite.workforce.gwt.core.server.db.StepEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.StudentManager;
import com.edatasite.workforce.gwt.core.server.db.SubscriptionPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotItemManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.TimeTrackManager;
import com.edatasite.workforce.gwt.core.server.db.TimeZoneManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintDeviceManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintmanager;
import com.edatasite.workforce.gwt.core.server.db.UserLastRequestManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.db.UserSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.WFTPlaginManager;
import com.edatasite.workforce.gwt.core.server.db.ZoomMeetingManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemBatchManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemTableSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.UnitMeasurementManager;
import com.edatasite.workforce.gwt.core.server.db.analyzer.SolrDbConsistencyManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.CrmCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.dashboard.DashboardManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.db.goal.GoalManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.AttachmentIndexRbacManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderRbacManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.FacetFilterManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.facetfilter.UserFilterManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ForceShowGuideSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.KPIContactDetailsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.KanbanItemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelGuideSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelSettingsDefaultManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.OverdueInvoiceReminderSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.QuickAddSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CustomFormItemsEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ImportCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ProjectCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.SyncGoogleContactsEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.ListPanelItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.UserRequestItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.rpc.FindEncodeInputStream;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.eml.EMLReader;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.usps.USPSAddress;
import com.edatasite.workforce.gwt.core.server.usps.USPSWebService;
import com.edatasite.workforce.gwt.core.server.utils.CacheConstants;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.core.server.utils.WfmJsonUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.exceptions.QuotaExceededException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.solr.SolrFolderRepresenter;
import com.edatasite.workforce.gwt.documents.server.GwtUploadServlet;
import com.edatasite.workforce.gwt.documents.server.app.DocumentItem;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.invoice.client.rpc.InvoiceList;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQData;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFQItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.invoice.server.app.ItemTableSettingsServiceLocal;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.server.app.MyAccountServiceLocal;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.profile.client.rpc.ConsolidationCompanyList;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.rest.v3.release10.settings.dto.UserSettingsDto;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import net.sf.mpxj.DateRange;
import net.sf.mpxj.Day;
import net.sf.mpxj.ProjectCalendar;
import net.sf.mpxj.ProjectCalendarHours;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.utility.DateUtility;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.Group;
import org.apache.solr.client.solrj.response.GroupCommand;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.GroupParams;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.persistence.EntityExistsException;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_CF_AUTO_NUMBER_RESET;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_ACCOUNT;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_EMPLOYEE_CONTACT;
import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * User: Employee
 * Date: Nov 3, 2009
 * Time: 5:06:47 PM
 */
@Transactional
@Service("commonService")
public class CommonServiceImpl implements CommonService, Constants, CommonServiceLocal {

    private static final Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);
    private static final String SPLIT_CHARACTER = "-:-";

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private GoalManager goalManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ClientService clientService;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private NoteCommentManager noteCommentManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private ReportingService reportingService;
    @Autowired
    private CRMService crmService;
    @Autowired
    private AttachmentIndexRbacManager attachmentIndexRbacManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource wfmMessageSource;
    @Autowired
    @Qualifier("myActivityLocalizer")
    private WfmMessageSource activityWfmMessageSource;
    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    private MyAccountServiceLocal myAccountServiceLocal;
    @Autowired
    @Qualifier("documentsService")
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private FolderRbacManager folderRbacManager;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private ExpenseReportManager reportManager;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ZoomMeetingManager zoomMeetingManager;
    @Autowired
    private ZoomService zoomService;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    @Qualifier("wftPlaginManager")
    private WFTPlaginManager plaginManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private SubscriptionPaymentManager subscriptionPaymentManager;
    @Autowired
    private TimeSlotItemManager timeSlotItemManager;
    @Autowired
    private BaseEventsPostProcessor baseEventsPostProcessor;
    @Autowired
    private ListPanelSettingsManager listPanelSettingsManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;
    @Autowired
    private ItemTableSettingsManager itemTableSettingsManager;
    @Autowired
    private ItemTableSettingsServiceLocal itemTableSettingsServiceLocal;
    @Autowired
    private CFItemTableSettingmanager cfItemTableSettingmanager;
    @Autowired
    private QuoteService quoteService;
    @Autowired
    private AccountingService accountingService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private PayrollService payrollService;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private AddViewSettingsManager addViewSettingsManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private MoreMenuSettingsManager moreMenuSettingsManager;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private FacetFilterManager facetFilterManager;
    @Autowired
    private StudentManager studentManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private SolrDbConsistencyManager solrDbConsistencyManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private LocalizationManager localizationManager;
    @Autowired
    private OverdueInvoiceReminderSettingsManager overdueInvoiceReminderSettingsManager;
    @Autowired
    private ListPanelGuideSettingsManager listPanelGuideSettingsManager;
    @Autowired
    private KPIContactDetailsManager kpiContactDetailsManager;
    @Autowired
    private ForceShowGuideSettingsManager forceShowGuideSettingsManager;
    @Autowired
    private HostBasedSettingManager hostBasedSettingManager;
    @Autowired
    private FormPropertyManager formPropertyManager;
    @Autowired
    @Qualifier("countryLocalizer")
    private WfmMessageSource countryLocalizer;

    @Autowired
    @Qualifier("regionLocalizer")
    private WfmMessageSource regionLocalizer;

    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    protected GroupManager groupManager;

    @Qualifier("companyCFSettingsManager")
    @Autowired
    private CompanyCustomFieldsManager companyCFManager;
    @Autowired
    private MessageManager messageManager;

    @Autowired
    private UserFilterManager userFilterManager;

    @Autowired
    private TimeZoneManager timeZoneManager;

    @Autowired
    private OnboardingStepManager onboardingStepManager;
    @Autowired
    private StepEmployeeManager stepEmployeeManager;
    @Autowired
    private SkillManager skillManager;
    @Autowired
    private ContractManager contractManager;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private TimeSlotManager timeSlotManager;

    @Autowired
    private TimeTrackManager timeTrackManager;
    @Autowired
    private UserFingerPrintmanager fingerPrintManager;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private UnitMeasurementManager unitMeasurementManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private RentalOrderManager rentalOrderManager;
    @Autowired
    private UserFingerPrintDeviceManager userFingerPrintDeviceManager;
    @Autowired
    private ProfileServiceLocal profileServiceLocal;
    @Autowired
    private UserSettingsManager userSettingsManager;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private PermissionManager permissionManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private ModelManager modelManager;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private CustomFormManager customFormManager;
    @Autowired
    private ContainerManager containerManager;
    @Autowired
    private ContainerItemManager containerItemManager;
    @Autowired
    private CustomFormItemManager customFormItemManager;
    @Autowired
    private CustomFormCFManager customFormCFManager;
    @Autowired
    private CustomItemTableCFManager customItemTableCFManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private CrmCustomFieldsManager crmCustomFieldsManager;
    @Autowired
    private CustomItemTableManager customItemTableManager;
    @Autowired
    private OpportunityItemTableManager opportunityItemTableManager;
    @Autowired
    private EmployeeItemTableManager employeeItemTableManager;
    @Autowired
    private PlacementItemTableManager placementItemTableManager;
    @Autowired
    private ProjectItemTableManager projectItemTableManager;

    @Autowired
    private CandidateItemTableManager candidateItemTableManager;
    @Autowired
    private VacancyItemTableManager vacancyItemTableManager;
    @Autowired
    private CustomFormNoteManager customFormNoteManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private CustomFormAttributeManager customFormAttributeManager;
    @Autowired
    private ApproverManager approverManager;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private ItemBatchManager itemBatchManager;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private CustomFormLocalizationManager customFormLocalizationManager;
    @Autowired
    private CustomFormSectionManager customFormSectionManager;
    @Autowired
    private CustomQuizFormManager customQuizFormManager;
    @Autowired
    private BenefitRequestManager benefitRequestManager;
    @Autowired
    private CFCommitBoxNoteManager cfCommitBoxNoteManager;
    @Autowired
    private DashboardManager dashboardManager;

    @Autowired
    @Qualifier("rabbitMQService")
    private RabbitMQService rabbitMQService;

    @Autowired
    private UserSessionManager userSessionManager;

    @Autowired
    private UserLastRequestManager userLastRequestManager;

    @Autowired
    private ListPanelSettingsDefaultManager listPanelSettingsDefaultManager;

    @Autowired
    private KanbanItemSettingsManager kanbanItemSettingsManager;
    @Autowired
    private AttendanceHoursManager attendanceHoursManager;
    @Autowired
    private ShiftManager shiftManager;
    @Autowired
    private QuickAddSettingsManager quickAddSettingsManager;

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    private CustomFormItemSolrComponent customFormItemSolrComponent;
    @Autowired
    private VacancySolrComponent vacancySolrComponent;
    @Autowired
    private EmployeeStepSolrComponent employeeStepSolrComponent;
    @Autowired
    private ImportFileManager importFileManager;
    @Autowired
    private ExecutorService executor;
    @Autowired
    private AttendanceTerminalManager attendanceTerminalManager;

    private SelectItem[] regionsCache;


    //-----------------------Methods --------------------------------------------

    @Transactional
    public void addMembers(Integer projectId, Integer[] members) {
        EdsProject project = this.projectManager.get(projectId);
        for (Integer member : members) {
            EdsEmployee employee = this.employeeManager.get(member);
            EdsEmployeeDepartment employeeDepartment = employee.getEmployeeTeam();
            if (employeeDepartment != null) {
                EdsProjectEmployee pe = new EdsProjectEmployee(employeeDepartment, project);
                pe.setStartDate(employeeDepartment.getEmployee().getCompany().getCompanyDate());
                this.projectEmployeeManager.create(pe);
            }
            this.baseEventPostProcessor.registerEvent(ProjectCustomEventListenerImpl.TYPE, ProjectCustomEventListenerImpl.EVENT_PROJECT_ADD_TO_SOLR, project, employee);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPositions() {
        List<EdsPosition> position = this.positionManager.list();
        SelectItem[] p = new SelectItem[position.size()];
        int i = 0;
        for (EdsPosition posit : position) {
            p[i] = new SelectItem();
            p[i].setId(posit.getObjectID());
            p[i].setName(posit.getName());
            i++;
        }
        return p;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getLanguages() {
        List<EdsReference> references = this.referenceManager.listReferences(Constants.LANGUAGES);

        if (references != null && !references.isEmpty()) {
            SelectItem[] languages = new SelectItem[references.size()];
            for (int i = 0; i < references.size(); i++) {
                languages[i] = new SelectItem(references.get(i).getObjectID(), references.get(i).getName());
            }

            return languages;
        }

        return new SelectItem[0];
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getReferences(ReferenceParentEnum parentCode) {
        List<EdsReference> references = this.referenceManager.listReferences(parentCode.name());
        ArrayList<SelectItem> result = new ArrayList<>();
        references.forEach(r -> result.add(new SelectItem(r.getObjectID(), r.getName(), r.getDescription())));
        return result.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getReferences(String parentCode) {
        List<EdsReference> references = this.referenceManager.listReferences(parentCode);
        ArrayList<SelectItem> result = new ArrayList<>();
        references.forEach(r -> result.add(new SelectItem(r.getObjectID(), r.getName(), r.getDescription())));
        return result.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getAddTaskStatusDrop() {
        List<EdsReference> statuses = this.referenceManager.listReferences(EdsTask.TASK_STATUS);
        EdsReference onHold = this.referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.ON_HOLD);
        EdsReference rejected = this.referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CANCELLED);
        statuses.remove(onHold);
        statuses.remove(rejected);
        return this.reference2SelectItem(statuses, EdsTask.NOT_STARTED);

    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] reference2SelectItem(List<EdsReference> references, String defaultSelection) {
        SelectItem[] selectItems = new SelectItem[references.size()];
        int i = 0;
        for (EdsReference status : references) {
            selectItems[i] = new SelectItem();
            selectItems[i].setId(status.getObjectID());
            selectItems[i].setDescription(status.getCode());
            String value = status.getName();
            if (defaultSelection != null) {
                if (defaultSelection.equals(status.getCode())) {
                    selectItems[i].setSelected(true);
                }
            }
            selectItems[i].setName(value);
            i++;
        }

        return selectItems;
    }

    public SelectItem[] getBenefitTypeList() {
        List<SelectItem> selectItemList = benefitRequestManager.getBenefitRequestTypeList();
        SelectItem[] selectItemArray = new SelectItem[selectItemList.size()];

        for (int i = 0; i < selectItemList.size(); i++) {
            selectItemArray[i] = selectItemList.get(i);
        }
        return selectItemArray;
    }

    public SelectItem[] getBenefitApprovers() {
        List<String> roleCodes = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.BENEFIT_REQUEST_APPROVER);
        if (roleCodes.isEmpty()) {
            roleCodes.add(EdsRole.ADMIN_CODE);
        }
        roleCodes.add(Constants.BMOFPR);
        EdsEmployee currentEmployee = employeeManager.get(userManager.getUser().getObjectID());
        //approvers list
        List<EdsEmployee> timeSheetApprovers = employeeManager.getApprovers(currentEmployee, roleCodes);

        SelectItem[] approvers = new SelectItem[timeSheetApprovers.size()];
        int i = 0;
        for (EdsUser manager : timeSheetApprovers) {
            approvers[i] = new SelectItem();
            approvers[i].setId(manager.getObjectID());
            approvers[i].setName(manager.getName());
            i++;
        }
        Arrays.sort(approvers, Comparator.comparing(SelectItem::getName));
        return approvers;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] referenceSelectItemWithPleaseSelect(List<EdsReference> statuses) {
        SelectItem[] selectItems = new SelectItem[statuses.size() + 1];
        selectItems[0] = new SelectItem();
        selectItems[0].setId(0);
        selectItems[0].setDescription("");
        selectItems[0].setName("Please select");
        int i = 1;
        for (EdsReference status : statuses) {
            selectItems[i] = new SelectItem();
            selectItems[i].setId(status.getObjectID());
            selectItems[i].setDescription(status.getCode());
            String value = this.wfmMessageSource.localize(status.getCode(), status.getName());
            selectItems[i].setName(value);
            i++;
        }


        return selectItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] convertReference2SelectItem(String referenceName, boolean useSortOrder, String defaultSelection) {
        return this.reference2SelectItem(this.referenceManager.listReferences(referenceName), defaultSelection);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] convertReference2SelectItem2(String referenceName) {
        return this.reference2SelectItem(this.referenceManager.listReferences(referenceName, true), null);
    }

    @Override
    public String getStudentImageURL(Integer studentID) {
        String url;
        if (studentID != null) {
            EdsStudent edsStudent = this.studentManager.get(studentID);
            if (edsStudent.getPhoto() != null) {
                url = this.getImageUrl(edsStudent.getPhoto().getObjectID());
            } else {
                url = null;
            }
        } else {
            url = null;
        }
        return url;
    }

    @Override
    public String getRentalProductImageURL(Integer rentalId) {
        String url;
        if (rentalId != null) {
            EdsItem edsItem = itemManager.get(rentalId);
            if (edsItem.getPhoto() != null) {
                url = getImageUrl(edsItem.getPhoto().getObjectID());
            } else {
                url = null;
            }
        } else {
            url = null;
        }
        return url;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getCompanyEmployeesWithTeams() {
        return this.getCompanyEmployeesWithTeams(null, false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getCompanyEmployeesWithTeams(Integer objectId, boolean isPayroll) {
        List<EdsEmployee> employees;
        if (isPayroll) {
            employees = this.employeeManager.getEmployeesForPayroll(this.employeeManager.getUser().getCompany());
        } else {
            employees = this.employeeManager.getEmployees(this.employeeManager.getUser().getCompany());
        }
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneeList = new LinkedHashMap<>();
        List<EdsEmployee> teamEmployees = null;
        if (objectId != null) {
            teamEmployees = this.employeeManager.getTeamEmployees(objectId);
        }
        KpiTreeInfo sItem;
        EdsDepartment department;
        boolean team;
        for (EdsEmployee employee : employees) {
            team = false;
            sItem = new KpiTreeInfo();
            sItem.setId(employee.getObjectID());
            if (employee.getProfile() != null && employee.getProfile().getEmployeeCode() != null) {
                sItem.setName(employee.getProfile().getEmployeeCode() + " - " + employee.getName());
            } else {
                sItem.setName(employee.getName());
            }
            if (teamEmployees != null) {
                for (EdsEmployee em : teamEmployees) {
                    if (em.getObjectID().equals(employee.getObjectID())) {
                        sItem.setSelected(true);
                        break;
                    }
                }
            }
            department = employee.getTeam();
            if (department != null) {
                sItem.setDepartmentId(department.getObjectID());
                sItem.setDepartmentName(department.getName());
                for (KpiTreeInfo s : assigneeList.keySet()) {
                    if (s.getId().equals(employee.getTeam().getObjectID())) {
                        team = true;
                        assigneeList.get(s).add(sItem);
                        break;
                    }
                }

                if (!team) {
                    KpiTreeInfo departmentInfo = new KpiTreeInfo(employee.getTeam().getObjectID(), employee.getTeam().getName());
                    ArrayList<KpiTreeInfo> list = new ArrayList<>();
                    list.add(sItem);
                    assigneeList.put(departmentInfo, list);
                }
            }
        }
        return assigneeList;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectItem[] getProjects(ListingFilterParameter filterParametrs, boolean crmActivityProjectNeeded, boolean withProjectNumber) {
        if (Constants.TASK.equals(filterParametrs.getCategory())) {
            EdsProject edsProject = null;
            if (filterParametrs.getProjectId() != null) {
                edsProject = this.projectManager.get(filterParametrs.getProjectId());
            } else if (crmActivityProjectNeeded) {
                edsProject = this.projectManager.getCrmProject();
            }
            if (edsProject == null) {
                return null;
            }
            return new ProjectItem[]{new ProjectItem(edsProject.getObjectID(), edsProject.getName(), edsProject.getBillable())};
        }
        EdsUser user = this.employeeManager.getUser();
        List<EdsProject> projects = this.projectManager.list(filterParametrs, user);
        ProjectItem[] result = new ProjectItem[projects.size()];
        int i = 0;
        for (EdsProject pr : projects) {
            result[i] = new ProjectItem(pr.getObjectID(), (withProjectNumber && pr.getNumber() != null ? pr.getNumber() + " - " : "") + pr.getName());
            result[i].setManager(user.equals(pr.getManager()) || pr.isUserBackupManager(user.getObjectID()));
            if (pr.getEndDate() != null) {
                result[i].setDescription(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(pr.getEndDate()));
            }
            if (crmActivityProjectNeeded && pr.isCrmActivityProject()) {
                result[i].setSelected(true);
            }
            i++;
        }
        Arrays.sort(result, (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectItem[] getProjects(boolean crmActivityProjectNeeded) {
        return this.getProjects(crmActivityProjectNeeded, false);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectItem[] getProjects(boolean crmActivityProjectNeeded, boolean withProjectNumber) {
        return this.getProjects(new ListingFilterParameter(), crmActivityProjectNeeded, withProjectNumber);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getDepartmentByName(String name) {
        List<EdsDepartment> departmentName = this.departmentManager.getDepartmentByName(name);
        return departmentName.size();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getDepartmentByCode(String code) {
        List<EdsDepartment> departmentcode = this.departmentManager.getDepartmentByName(code);
        return departmentcode.size();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getCurrentUserDepartmentID() {
        Integer teamID = null;
        EdsUser currentUser = this.userManager.getUser();
        if (currentUser != null && currentUser.isEmployee()) {
            EdsEmployee employee = currentUser.getEmployee();
            if (employee.getEmployeeTeam() != null && employee.getEmployeeTeam().getTeam() != null) {
                teamID = employee.getEmployeeTeam().getTeam().getObjectID();
            } else {
                teamID = currentUser.getCompany().getDefaultDepartment().getObjectID();
            }
        }
        return teamID;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem getEmployeeImageURL() {
        SelectItem photo = null;
        EdsUser user = this.userManager.getUser();
        if (user.getPhoto() != null) {
            photo = new SelectItem(user.getPhoto().getObjectID(), this.getImageUrl(user.getPhoto().getObjectID()));
        }
        return photo;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getEmployeeImageURL(Integer userId) {
        EdsUser user = userId != null ? this.userManager.get(userId) : this.userManager.getUser();

        if (user == null || user.getPhoto() == null) {
            return null;
        }
        return this.getImageUrl(user.getPhoto().getObjectID());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String[] getEmployeeNameWithImageURL(Integer userID) {
        String[] nameAndImageURL = new String[2];
        String url;
        EdsUser user = this.userManager.get(userID);
        if (user.getPhoto() != null) {
            url = this.getImageUrl(user.getPhoto().getObjectID());
        } else {
            url = null;
        }

        nameAndImageURL[0] = url;
        nameAndImageURL[1] = user.getFullName();
        return nameAndImageURL;
    }

    public Boolean removeUserImage(Integer userID) {
        EdsUser currentUser;
        if (userID == null) {
            currentUser = this.userManager.getUser();
        } else {
            currentUser = this.userManager.get(userID);
        }
        EdsUpload photo = currentUser.getPhoto();
        if (photo != null) {
            try {
                uploadManager.deleteFile(photo);
                uploadManager.delete(photo);
                currentUser.setPhoto(null);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public String getImageUrl(Integer id) {
        return this.getFileUrl(id, null, true);
    }

    @Override
    public String getFileUrl(Integer id) {
        return this.getFileUrl(id, null, true);
    }

    @Override
    public String getFileUrl(Integer fileId, String fileType, Boolean needHostForLocalFile) {
        String[] file = this.getFileUrl(fileId, fileType, needHostForLocalFile, false);
        if (file != null && file.length > 1 && file[0] != null) {
            return file[0];
        } else {
            return null;
        }
    }

    @Override
    public String[] getFileUrl(Integer fileId, String fileType, Boolean needHostForLocalFile, boolean withSize) {
        String[] result = new String[3];
        EdsUpload upload = (EdsUpload) this.uploadManager.get(fileId);
        if (withSize) {
            result[1] = upload.getWidth();
            result[2] = upload.getHeight();
        }

        result[0] = uploadManager.getFileURL(upload);

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewsComment[] getNotecomments(Integer objectId) {
        EdsNoteHistory noteHistory = this.noteHistoryManager.get(objectId);
        NewsComment[] comments = new NewsComment[0];
        if (noteHistory != null && noteHistory.getNoteComments() != null) {
            comments = new NewsComment[noteHistory.getNoteComments().size()];
            int i = 0;
            for (EdsNoteComment comment : noteHistory.getNoteComments()) {
                NewsComment newsComment = new NewsComment();
                newsComment.setNewsId(comment.getNote().getObjectID());
                newsComment.setCommentId(comment.getObjectID());
                newsComment.setComment(comment.getComment());
                newsComment.setDate(comment.getDate());
                newsComment.setUsername(comment.getUser().getName());
                if (comment.getUser().getPhoto() != null) {
                    newsComment.setEmployeeImageUrl(this.getImageUrl(comment.getUser().getPhoto().getObjectID()));
                }
                comments[i++] = newsComment;
            }
        }
        Arrays.sort(comments, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        return comments;
    }

    public NewsComment saveNoteComment(NewsComment data) {
        EdsNoteHistory noteHistory = this.noteHistoryManager.get(data.getNewsId());
        EdsNoteComment noteComment;
        if (data.getCommentId() != null) {
            noteComment = this.noteCommentManager.get(data.getCommentId());
            noteComment.setDate(data.getDate());
        } else {
            noteComment = new EdsNoteComment();
            noteComment.setDate(new Date());
        }

        noteComment.setComment(data.getComment());
        noteComment.setUser(this.noteCommentManager.getUser());
        noteComment.setNote(noteHistory);
        this.noteCommentManager.create(noteComment);
        if (data.getCommentId() != null) {
            //it's ok do nothing
        } else {
            data.setDate(new Date());
            data.setUsername(this.noteCommentManager.getUser().getName());
        }

        return data;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HistoryListItem[] getDepartmentNotes(Integer departmentID) {
        EdsDepartment department = this.departmentManager.get(departmentID);
        HistoryListItem[] departmentNotes;
        if (department != null) {
            EdsNoteHistory[] departmentNote = this.noteHistoryManager.getNoteList(new ListingFilterParameter()).toArray(new EdsNoteHistory[]{});
            List<EdsNoteHistory> histrNotes = new LinkedList<>();
            for (EdsNoteHistory noteHistr : departmentNote) {
                if ((EdsNoteHistory.DEPARTMENT == noteHistr.getRelatedTo() && noteHistr.getRelatedId() != null) &&
                        (noteHistr.getRelatedId().intValue() == department.getObjectID().intValue())) {
                    histrNotes.add(noteHistr);
                }
            }
            departmentNotes = new HistoryListItem[histrNotes.size()];
            for (int i = 0; i < histrNotes.size(); i++) {
                EdsNoteHistory notes = histrNotes.get(i);
                HistoryListItem items = new HistoryListItem();
                items.setObjectID(notes.getObjectID());
                items.setEmployee(notes.getEmployee().getName());
                items.setSubject(notes.getSubject());
                items.setComment(notes.getComment());
                items.setVisibility(notes.isVisibility());
                items.setEventDate(notes.getEventDate() != null ? new Date(notes.getEventDate().getTime()) : null);
                items.setEditable(this.employeeManager.getUser().equals(notes.getEmployee()));
                NewsComment[] noteComments = this.getNotecomments(notes.getObjectID());
                if (noteComments.length > 0) {
                    items.setNotesComments(noteComments);
                } else {
                    items.setNotesComments(new NewsComment[0]);
                }
                departmentNotes[i] = items;
            }
            return departmentNotes;
        }

        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<String, SelectItem[]> getCountriesAndRegions() {
        HashMap<String, SelectItem[]> map = new HashMap<>();
        ListingFilterParameter fp = new ListingFilterParameter();
        map.put("country", this.getCountries(fp, true));
        map.put("state", this.getRegions());
        map.put("embassy", this.getEmbasies());
        return map;
    }

    @Override
    public SelectItem[] getEmbasies() {
        List<EdsEmbassy> embasies;
        SelectItem[] result;
        int i = 0;
        Locale locale = this.regionLocalizer.initializeUserLocale();
        if (locale == null || locale.equals(Locale.ENGLISH) || locale.equals(Locale.UK)) {
            embasies = this.countryManager.listEmbasies();
            result = new SelectItem[embasies.size()];
            for (EdsEmbassy embassy : embasies) {
                result[i] = new SelectItem(embassy.getObjectID(), embassy.getName(), String.valueOf(embassy.getCountry().getObjectID()));
                i++;
            }
        } else {
            embasies = this.countryManager.listEmbasies();
            result = new SelectItem[embasies.size()];
            for (EdsEmbassy embassy : embasies) {
                result[i] = new SelectItem(embassy.getObjectID(), this.regionLocalizer.localize(embassy.getCode(), embassy.getName()), String.valueOf(embassy.getCountry().getObjectID()));
                i++;
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getTeamList() {
        EdsUser user = this.employeeManager.getUser();
        ListingFilterParameter fp = new ListingFilterParameter();
        if (fp.getViewAsId() == null) {
            EdsRole maximumRole = null;
            try {
                maximumRole = user.getRolesSortedByPattern().get(0);
            } catch (IndexOutOfBoundsException e) {
                CommonServiceImpl.log.info("******************** User Role check - Company: " + user.getCompany().getName() + " ID: " + user.getCompany().getObjectID());
            }
            if (maximumRole != null) {
                fp.setViewAsId(maximumRole.getObjectID());
            }
        }
        fp.setSortField(TeamListItem.NAME);
        fp.setSortDir(1);//Ascending order by name
        List<EdsDepartment> teamList = this.departmentManager.list(fp);
        SelectItem[] items = new SelectItem[teamList.size()];
        for (int k = 0; k < items.length; k++) {
            items[k] = new SelectItem();
            items[k].setId(teamList.get(k).getObjectID());
            items[k].setName(teamList.get(k).getName());
        }
        return items;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountries() {
        return this.getCountries(false);
    }

    @Override
    public void createCustomFieldFolder(Integer customFieldID) {
        this.documentsServiceLocal.createCustomFieldFolder(customFieldID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountries(boolean sorted) {
        ListingFilterParameter fp = new ListingFilterParameter();
        return this.getCountries(fp, sorted);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountries(ListingFilterParameter fp, boolean sorted) {
        List<EdsCountry> countries;
        SelectItem[] result;
        int i = 0;
        EdsUser edsUser = userManager.getUser();
        Locale locale = this.countryLocalizer.initializeUserLocale();
        fp.setLanguage(locale.getLanguage());
        countries = this.countryManager.list(fp);
        result = new SelectItem[countries.size()];
        boolean enLocale = locale == null || locale.equals(Locale.ENGLISH) || locale.equals(Locale.UK) || "en_gb".equals(locale.getLanguage());
        for (EdsCountry country : countries) {
            if (enLocale) {
                result[i] = new SelectItem(country.getObjectID(), country.getName());
            } else {
                result[i] = new SelectItem(country.getObjectID(), this.countryLocalizer.localize(country.getCode(), country.getName()));
            }
            result[i].setDescription(country.getCode());
            result[i].setCode(country.getCode());
            result[i].setNewItem(country.isActive());
            result[i].setCategory(country.getTelCode());
            if (edsUser != null && edsUser.getCompany() != null && edsUser.getCompany().getCountry() != null) {
                result[i].setSelectedId(edsUser.getCompany().getCountry().getObjectID());
            }
            i++;
        }

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRegions() {
        List<EdsRegion> regions;
        SelectItem[] result;
        int i = 0;
        Locale locale = this.regionLocalizer.initializeUserLocale();
        if (locale == null || locale.equals(Locale.ENGLISH) || locale.equals(Locale.UK)) {
            if (this.regionsCache == null) {
                regions = this.regionManager.list();
                result = new SelectItem[regions.size()];
                for (EdsRegion region : regions) {
                    result[i] = new SelectItem(region.getObjectID(), region.getName(), String.valueOf(region.getCountry().getObjectID()));
                    i++;
                }
                this.regionsCache = result;
            } else {
                result = this.regionsCache;
            }
        } else {
            regions = this.regionManager.list();
            result = new SelectItem[regions.size()];
            for (EdsRegion region : regions) {
                result[i] = new SelectItem(region.getObjectID(), this.regionLocalizer.localize(region.getCode(), region.getName()), String.valueOf(region.getCountry().getObjectID()));
                i++;
            }
        }
        return result;
    }

    public SelectItem[] getRegions(ListingFilterParameter fp) {
        List<EdsRegion> regions;
        SelectItem[] result;
        int i = 0;
        Locale locale = this.regionLocalizer.initializeUserLocale();
        if (locale == null || locale.equals(Locale.ENGLISH) || locale.equals(Locale.UK)) {
            regions = this.regionManager.list(fp);
            result = new SelectItem[regions.size()];
            for (EdsRegion region : regions) {
                result[i] = new SelectItem(region.getObjectID(), region.getName(), String.valueOf(region.getCountry().getObjectID()));
                i++;
            }
        } else {
            regions = this.regionManager.list(fp);
            result = new SelectItem[regions.size()];
            for (EdsRegion region : regions) {
                result[i] = new SelectItem(region.getObjectID(), this.regionLocalizer.localize(region.getCode(), region.getName()), String.valueOf(region.getCountry().getObjectID()));
                i++;
            }
        }
        return result;
    }

    public SelectItem[] getRegions(Integer countryID) {
        List<EdsRegion> regions = this.regionManager.listByCountry(countryID);
        SelectItem[] result = new SelectItem[regions.size()];
        int i = 0;
        Locale locale = this.regionLocalizer.initializeUserLocale();
        for (EdsRegion region : regions) {
            if (locale == null || locale.equals(Locale.ENGLISH) || locale.equals(Locale.UK)) {
                result[i] = new SelectItem(region.getObjectID(), region.getName());
            } else if (locale.getLanguage().equals("uz")) {
                result[i] = new SelectItem(region.getObjectID(), this.regionLocalizer.localize(region.getCode(), region.getUzName()));
            } else {
                result[i] = new SelectItem(region.getObjectID(), this.regionLocalizer.localize(region.getCode(), region.getName()));
            }
            i++;
        }
        return result;
    }

    @Override
    public SelectItem[] getRegions(Integer countryId, ListingFilterParameter filterParameter) {
        return this.regionManager.listRegionByCountry(countryId, filterParameter);
    }

    @Override
    public String saveCroppedImage(Integer id, int left, int top, int width, int height) {
        return this.saveCroppedImage(null, null, id, left, top, width, height);
    }

    @Override
    public FileResource saveItemCroppedImage(Integer imageID, Integer entityID, Integer entityType, int left, int top, int width, int height) {
        try {
            URL url = new URL(this.getImageUrl(imageID));
            EdsUpload upload = (EdsUpload) this.uploadManager.get(imageID);

            BufferedImage changeImg = ImageIO.read(url);
            BufferedImage cropped = changeImg.getSubimage(left, top, width, height);
            //login details dates
            ByteArrayOutputStream croppedStream = new ByteArrayOutputStream();
            ImageIO.write(cropped, "png", croppedStream);
            InputStream croppedInpStream = new ByteArrayInputStream(croppedStream.toByteArray());

            EdsFolder folder = this.folderManager.getFolder(Constants.F_SALE_QUOTE_ITEM, entityID);
            FolderResource tempFolder = this.getTempFolder();

            DocumentItem fileBody = new DocumentItem();
            fileBody.setInputStream(croppedInpStream);
            fileBody.setSize(croppedInpStream.available());
            fileBody.setContentType("image/png");
            fileBody.setName(upload.getOriginalName());
            fileBody.setFolderId(tempFolder.getObjectId());
            fileBody.setDownloadable(upload.isDownloadable());

            try {
                Integer entityId = folder != null ? folder.getEntityId() : null;
                FileResource file = this.documentsServiceLocal.createFile(fileBody, EdsContextParams.getUploadType(), tempFolder.getObjectId(), entityId);
                return file;
            } catch (DuplicateNameException | ObjectNotFoundException | QuotaExceededException |
                     InsufficientPermissionsException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public String saveCroppedImage(Integer entityId, String type, Integer id, int left, int top, int width, int height) {

        try {
            URL url = new URL(this.getImageUrl(id));

            BufferedImage changeImg = ImageIO.read(url);
            BufferedImage cropped = null;
            try {
                cropped = changeImg.getSubimage(left, top, width, height);
            } catch (RasterFormatException e) {
                cropped = changeImg;
            }
            //login details dates
            ByteArrayOutputStream croppedStream = new ByteArrayOutputStream();
            ImageIO.write(cropped, "png", croppedStream);
            InputStream croppedInpStream = new ByteArrayInputStream(croppedStream.toByteArray());

            if ((width > 200 || height > 220)) { //for employee profile
                ImageScaleDown imageScaleDown = new ImageScaleDown(croppedInpStream, "png");
                Object[] objects = imageScaleDown.getAdvancedImageScaleDownInputStream(200, 220);

                croppedInpStream = (InputStream) objects[0];
            }

            EdsAttachment attachment;
            if (id != null) {
                attachment = this.attachmentManager.get(id);
                if (attachment == null) {
                    attachment = new EdsAttachment();
                }
            } else {
                attachment = new EdsAttachment();
            }
            if (attachment.getObjectID() == null) {
                String contentType = url.openConnection().getContentType();
                attachment.setContentType(contentType);
                EdsReference uploadType = this.referenceManager.findReference(Constants._UPLOAD_TYPE, EdsContextParams.getUploadType());
                attachment.setType(uploadType);
            }
            long size = croppedInpStream.available();
            attachment.setInputStream(croppedInpStream);
            attachment.setSize(size);

            this.attachmentManager.create(attachment);

            if (entityId == null) {
                return this.getImageUrl(attachment.getObjectID());
            } else {
                if (LayoutRPC.HRMS_EMPLOYEE_FORM.equals(type)) {
                    return this.saveImageUrl(attachment.getObjectID(), entityId);
                } else if (LayoutRPC.CONTACT_FORM.equals(type)) {
                    return this.saveCrmContactImageUrl(attachment.getObjectID(), entityId);
                } else if (LayoutRPC.ACCOUNT_FORM.equals(type)) {
                    return this.saveCrmAccountLogoUrl(attachment.getObjectID(), entityId);
                } else if (LayoutRPC.STUDENT_FORM.equals(type)) {
                    return this.saveStudentImageUrl(attachment.getObjectID(), entityId);
                } else if (LayoutRPC.RENTAL_PRODUCT_FORM.equals(type)) {
                    return saveRentalProductImageUrl(attachment.getObjectID(), entityId);
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public String saveImageUrl(Integer id, Integer userID) {

        EdsUser user;
        if (userID != null) {
            user = this.userManager.get(userID);
        } else {
            user = this.userManager.getUser();
        }
        EdsUpload upload = (EdsUpload) this.uploadManager.get(id);

        String url = uploadManager.getFileURL(upload, false);

        user.setPhoto(upload);
        if (user.isClientContact()) {
            EdsClientContact client = this.clientContactManager.get(user.getObjectID());
            if (client != null && client.getCrmContact() != null) {
                client.getCrmContact().setPhoto(upload);
            }
        } else if (user.isEmployee()) {
            EdsEmployee employee = this.employeeManager.get(user.getObjectID());
            if (employee != null && employee.getProfile() != null && employee.getProfile().getContact() != null) {
                EdsCrmContact crmContact = employee.getProfile().getContact();
                crmContact.setPhoto(upload);
            }
        }
        return url;
    }

    @Override
    public String saveStudentImageUrl(Integer id, Integer studentId) {
        EdsStudent student = null;
        if (studentId != null) {
            student = this.studentManager.get(studentId);
        }
        EdsUpload upload = (EdsUpload) this.uploadManager.get(id);

        String url = uploadManager.getFileURL(upload);
        if (student != null) {
            student.setPhoto(upload);
        }
        return url;
    }

    private String saveRentalProductImageUrl(Integer id, Integer rentalId) {
        EdsItem item = null;
        if (rentalId != null) {
            item = itemManager.get(rentalId);
        }
        EdsUpload upload = (EdsUpload) uploadManager.get(id);

        String url = uploadManager.getFileURL(upload);
        if (item != null) {
            item.setPhoto(upload);
        }
        return url;
    }

    /**
     * Uplad Crm Contact Image
     *
     * @param id
     * @param crmContactID
     * @return
     */
    @Override
    public String saveCrmContactImageUrl(Integer id, Integer crmContactID) {
        EdsCrmContact crmContact = this.crmContactManager.get(crmContactID);
        EdsUpload upload = (EdsUpload) this.uploadManager.get(id);

        String url = uploadManager.getFileURL(upload);

        crmContact.setPhoto(upload);
        if (EdsCrmContact.EMPLOYEE_CONTACT.equals(crmContact.getContactType())) {
            EdsEmployeeProfile employeeProfile = this.profileManager.get(crmContact.getEntityContactID());
            if (employeeProfile != null) {
                EdsEmployee employee = this.employeeManager.getEmployeeByProfileID(employeeProfile.getObjectID());
                if (employee != null) {
                    employee.setPhoto(upload);
                }
            }
        } else if (EdsCrmContact.CLIENT_CONTACT.equals(crmContact.getContactType())) {
            EdsClientContact clientContact = this.clientContactManager.get(crmContact.getEntityContactID());
            if (clientContact != null) {
                clientContact.setPhoto(upload);
            }
        }
        return url;
    }

    //----------------------Search ---------------------------------
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SearchResultItemList searchByKeyword(DocumentsSearchItem searchItem) {
        EdsUser edsUser = this.referenceManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        ListingFilterParameter lfp = new ListingFilterParameter();
        lfp.setSearchKey(searchItem.getKeyword());
        lfp.setStart(searchItem.getStart());
        lfp.setLimit(searchItem.getLimit());
        FacetFilterRpc facetFilter = new FacetFilterRpc();
        facetFilter.setOverallSearch(true);
        if (searchItem.getSectionName() != -1 && !"".equals(searchItem.getKeyword()) && (searchItem.getKeyword() != null)) {
            if (ModuleSectionConstants.TASK == searchItem.getSectionName()) {
                try {
                    return this.getTaskOveralSearchData(edsUser, edsCompany, lfp, facetFilter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ModuleSectionConstants.PROJECT == searchItem.getSectionName()) {
                try {
                    return this.getProjectOverallSearch(edsUser, edsCompany, lfp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } /*else if (ModuleSectionConstants.CRM_CONTACT == searchItem.getSectionName()) {
                try {
                    return getCrmContactOverallSearch(edsUser, edsCompany, lfp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/ else if (ModuleSectionConstants.CRM_ACCOUNT == searchItem.getSectionName()) {
                try {
                    return this.getCrmAccountOverallSearch(edsCompany, lfp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ModuleSectionConstants.CRM_LEAD == searchItem.getSectionName()) {
                try {
                    return this.getCrmLeadOverallSearch(edsUser, lfp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } /*else if (ModuleSectionConstants.CRM_OPPORTUNITY == searchItem.getSectionName()) {
                try {
                    return getCrmOpportunityOverallSearch(edsUser, edsCompany, lfp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/ else if (ModuleSectionConstants.CRM_CASE == searchItem.getSectionName()) {
                try {
                    return this.getCrmCaseOverallSearch(edsCompany, lfp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ModuleSectionConstants.PURCHASE_ORDER == searchItem.getSectionName()) {
                try {
                    return this.getPurchaseOrderOverallSearch(edsUser, lfp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ModuleSectionConstants.SALE_INVOICE == searchItem.getSectionName()) {
                try {
                    return this.getSaleInvoiceOverallSearch(edsUser, lfp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ModuleSectionConstants.SALE_QUOTE == searchItem.getSectionName()) {
                try {
                    return this.getSaleQuoteOverallSearch(edsUser, lfp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ModuleSectionConstants.NEWS == searchItem.getSectionName()) {
                try {
                    return this.getNewsOverallSearch(edsUser, edsCompany, lfp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } /*else if (ModuleSectionConstants.EVENT == searchItem.getSectionName()) {
                try {
                    return getEventOverallSearch(lfp, edsUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/ else if (ModuleSectionConstants.DOCUMENTS == searchItem.getSectionName()) {
                try {
                    return this.documentsServiceLocal.getSearchResult(searchItem);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new SearchResultItemList(0, new SearchResultItem[0], 0);
    }

    /*private SearchResultItemList getCrmContactOverallSearch(EdsUser edsUser, EdsCompany edsCompany, ListingFilterParameter lfp) {

        String caegoryIdsForUserForSolr = contactCategoryManager.getCategoryIDsForUserForSOLR(null, edsUser, null, null);
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getContactListSolrQuery(lfp, null, edsCompany, caegoryIdsForUserForSolr, null));

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setFields(SolrContactRepresenter.FIELD_CONTACT_ID, SolrContactRepresenter.FIELD_CONTACT_NAME_COMPOSITE,
                SolrContactRepresenter.FIELD_PRIMARY_EMAIL, SolrContactRepresenter.FIELD_COUNTRY_NAME, SolrContactRepresenter.FIELD_TITLE);

        query.setSort(SolrContactRepresenter.FIELD_UPDATE_DATE, SolrQuery.ORDER.desc);

        QueryResponse resp = getSolrQueryResult(query, SOLR_CONTACT_CORE);
        int totalCount = (int) resp.getResults().getNumFound();
        int qTime = resp.getQTime();
        SearchResultItem[] foundItems = new SearchResultItem[resp.getResults().size()];
        int i = 0;
        for (SolrDocument relevantDoc : resp.getResults()) {
            foundItems[i] = new SearchResultItem();
            foundItems[i].setEntityID(SolrUtils.asInteger(relevantDoc, SolrContactRepresenter.FIELD_CONTACT_ID).toString());
            foundItems[i].setName(SolrUtils.asString(relevantDoc, SolrContactRepresenter.FIELD_CONTACT_NAME_COMPOSITE));
            foundItems[i].setDescription(SolrUtils.asString(relevantDoc, SolrContactRepresenter.FIELD_PRIMARY_EMAIL));
            foundItems[i].setTitleLink(EncryptionHelper.encryptURL("contact/" + foundItems[i].getEntityID()));
            foundItems[i].setPlainLink("contact/" + foundItems[i].getEntityID());
            foundItems[i++].setHighlits(getOveralSearchHighlits(relevantDoc,
                    new String[]{SolrContactRepresenter.FIELD_COUNTRY_NAME, SolrContactRepresenter.FIELD_TITLE},
                    new String[]{"Country Name", "Title"}));
        }
        return new SearchResultItemList(totalCount, foundItems, qTime);
    }*/

    private SearchResultItemList getCrmAccountOverallSearch(EdsCompany edsCompany, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getCrmAccountListSolrQuery(lfp, edsCompany, null, edsCompany != null ? edsCompany.getCreator() : null, null));
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setFields(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NAME,
                SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_NAME, SolrCrmAccountRepresenter.FIELD_EMAIL, SolrCrmAccountRepresenter.FIELD_COUNTRY_NAME);

        query.setSort(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NAME, SolrQuery.ORDER.asc);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_CRM_ACCOUNT_CORE);
        int totalCount = (int) resp.getResults().getNumFound();
        int qTime = resp.getQTime();
        int i = 0;
        SearchResultItem[] foundItems = new SearchResultItem[resp.getResults().size()];
        for (SolrDocument relevantDoc : resp.getResults()) {
            foundItems[i] = new SearchResultItem();
            foundItems[i].setEntityID(SolrUtils.asInteger(relevantDoc, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID).toString());
            foundItems[i].setName(SolrUtils.asString(relevantDoc, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NAME));
            foundItems[i].setDescription(SolrUtils.asString(relevantDoc, SolrCrmAccountRepresenter.FIELD_EMAIL));
            foundItems[i].setTitleLink(EncryptionHelper.encryptURL("account/" + foundItems[i].getEntityID()));
            foundItems[i].setPlainLink("account/" + foundItems[i].getEntityID());
            foundItems[i++].setHighlits(this.getOveralSearchHighlits(relevantDoc,
                    new String[]{SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_NAME, SolrCrmAccountRepresenter.FIELD_COUNTRY_NAME},
                    new String[]{"Account Parent Name", "Country Name"}));
        }
        return new SearchResultItemList(totalCount, foundItems, qTime);
    }

    private SearchResultItemList getCrmLeadOverallSearch(EdsUser edsUser, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getLeadListFacetFilterAssigneeQuery(edsUser.getCompany(), edsUser, lfp, null, null));
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setFields(SolrContactRepresenter.FIELD_CONTACT_ID, SolrContactRepresenter.FIELD_LEAD_NAME_COMPOSITE,
                SolrContactRepresenter.FIELD_JOB_TITLE, SolrContactRepresenter.FIELD_LEAD_ASSIGNEE, SolrContactRepresenter.FIELD_LEAD_BACKUP_ASSIGNEE,
                SolrContactRepresenter.FIELD_PRIMARY_EMAIL, SolrContactRepresenter.FIELD_COUNTRY_NAME);

        query.setSort(SolrContactRepresenter.FIELD_UPDATE_DATE, SolrQuery.ORDER.desc);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_CONTACT_CORE);
        int totalCount = (int) resp.getResults().getNumFound();
        int qTime = resp.getQTime();
        SearchResultItem[] foundItems = new SearchResultItem[resp.getResults().size()];
        int i = 0;
        for (SolrDocument relevantDoc : resp.getResults()) {
            foundItems[i] = new SearchResultItem();
            foundItems[i].setEntityID(SolrUtils.asInteger(relevantDoc, SolrContactRepresenter.FIELD_CONTACT_ID).toString());
            foundItems[i].setName(SolrUtils.asString(relevantDoc, SolrContactRepresenter.FIELD_LEAD_NAME_COMPOSITE));
            foundItems[i].setDescription(SolrUtils.asString(relevantDoc, SolrContactRepresenter.FIELD_PRIMARY_EMAIL));
            foundItems[i].setTitleLink(EncryptionHelper.encryptURL("lead/" + foundItems[i].getEntityID()));
            foundItems[i].setPlainLink("lead/" + foundItems[i].getEntityID());
            foundItems[i++].setHighlits(this.getOveralSearchHighlits(relevantDoc,
                    new String[]{SolrContactRepresenter.FIELD_JOB_TITLE, SolrContactRepresenter.FIELD_LEAD_ASSIGNEE,
                            SolrContactRepresenter.FIELD_LEAD_BACKUP_ASSIGNEE, SolrContactRepresenter.FIELD_COUNTRY_NAME},
                    new String[]{"Job Title", "Lead Assignee", "Lead Backup Assignee", "Country Name"}
            ));
        }
        return new SearchResultItemList(totalCount, foundItems, qTime);
    }

    /*private SearchResultItemList getCrmOpportunityOverallSearch(EdsUser edsUser, EdsCompany edsCompany, ListingFilterParameter lfp) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getOpportunityCoreSolrQuery(edsUser, null, lfp));

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setFields(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME,
                SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NAME, SolrOpportunityRepresenter.FIELD_ASSIGNEE_NAME, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_NAME);

        query.setSort(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID, SolrQuery.ORDER.desc);


        QueryResponse resp = getSolrQueryResult(query, SOLR_OPPORTUNITY_CORE);
        int totalCount = (int) resp.getResults().getNumFound();
        int qTime = resp.getQTime();
        int i = 0;
        SearchResultItem[] foundItems = new SearchResultItem[resp.getResults().size()];
        for (SolrDocument relevantDoc : resp.getResults()) {
            foundItems[i] = new SearchResultItem();
            foundItems[i].setEntityID(SolrUtils.asInteger(relevantDoc, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID).toString());
            foundItems[i].setName(SolrUtils.asString(relevantDoc, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_NAME));
            foundItems[i].setDescription(SolrUtils.asString(relevantDoc, SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_NAME));
            foundItems[i].setTitleLink(EncryptionHelper.encryptURL("opportunity/" + foundItems[i].getEntityID()));
            foundItems[i].setPlainLink("opportunity/" + foundItems[i].getEntityID());
            foundItems[i++].setHighlits(getOveralSearchHighlits(relevantDoc,
                    new String[]{SolrOpportunityRepresenter.FIELD_ASSIGNEE_NAME, SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_NAME},
                    new String[]{"Opprtunity Assignee", "Crm Account Name"}));
        }
        return new SearchResultItemList(totalCount, foundItems, qTime);
    }*/

    private SearchResultItemList getCrmCaseOverallSearch(EdsCompany edsCompany, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(// generate solr query
                this.getCrmCaseSolrQuery(lfp, edsCompany, null));
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setFields(SolrCaseRepresenter.CASE_ID, SolrCaseRepresenter.CASE_SUBJECT,
                SolrCaseRepresenter.CASE_EMAIL, SolrCaseRepresenter.CASE_ASSIGNEE,
                SolrCaseRepresenter.STATUS_NAME, SolrCaseRepresenter.PRIORITY_NAME, SolrCaseRepresenter.REPORTED_BY);
        query.setSort(SolrCaseRepresenter.LAST_UPDATE_DATE, SolrQuery.ORDER.desc);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_CASE_CORE);
        int totalCount = (int) resp.getResults().getNumFound();
        int qTime = resp.getQTime();
        int i = 0;
        SearchResultItem[] foundItems = new SearchResultItem[resp.getResults().size()];
        for (SolrDocument relevantDoc : resp.getResults()) {
            foundItems[i] = new SearchResultItem();
            foundItems[i].setEntityID(SolrUtils.asInteger(relevantDoc, SolrCaseRepresenter.CASE_ID).toString());
            foundItems[i].setName(SolrUtils.asString(relevantDoc, SolrCaseRepresenter.CASE_SUBJECT));
            foundItems[i].setTitleLink(EncryptionHelper.encryptURL("case/" + foundItems[i].getEntityID()));
            foundItems[i].setPlainLink("case/" + foundItems[i].getEntityID());
            foundItems[i++].setHighlits(this.getOveralSearchHighlits(relevantDoc,
                    new String[]{SolrCaseRepresenter.CASE_EMAIL, SolrCaseRepresenter.CASE_ASSIGNEE,
                            SolrCaseRepresenter.STATUS_NAME, SolrCaseRepresenter.PRIORITY_NAME, SolrCaseRepresenter.REPORTED_BY},
                    new String[]{"Case Email", "Case Assignee", "Status Name", "Priority Name", "Reported By"}
            ));
        }
        return new SearchResultItemList(totalCount, foundItems, qTime);
    }

    public String getCrmCaseSolrQuery(ListingFilterParameter fp, EdsCompany edsCompany, FacetFilterRpc facetFilter) {
        StringBuffer caseSolrQuery = new StringBuffer();
        caseSolrQuery.append(SolrCaseRepresenter.COMPANY_ID).append(":").append(edsCompany != null ? edsCompany.getObjectID() : SecurityContext.getCompanyID());
//        if (fp.getRelationID() == null || fp.getRelationType() == null) {
        boolean isRelation = false;
        if (fp.getCrmAccountId() != null && !"".equals(fp.getCrmAccountId())) {
            isRelation = true;
            caseSolrQuery.append(" AND ").append("(").append(SolrCaseRepresenter.ACCOUNT_ID).append(":").append(fp.getCrmAccountId());
        }
        if (fp.getCrmContactId() != null && !"".equals(fp.getCrmContactId())) {
            isRelation = true;
            caseSolrQuery.append(" AND ").append("(").append(SolrCaseRepresenter.RELEATED_TO_ID).append(":").append(fp.getCrmContactId());
        }
        if (fp.getCrmLeadId() != null && !"".equals(fp.getCrmLeadId())) {
            isRelation = true;
            caseSolrQuery.append(" AND ").append("(").append(SolrCaseRepresenter.LEAD_ID).append(":").append(fp.getCrmLeadId());
        }
        if (fp.getCrmOppartunityId() != null && !"".equals(fp.getCrmOppartunityId())) {
            isRelation = true;
            caseSolrQuery.append(" AND ").append("(").append(SolrCaseRepresenter.OPPORTUNITY_ID).append(":").append(fp.getCrmOppartunityId());
        }

        if (fp.getRelationID() != null && fp.getRelationType() != null && isRelation) {
            List<Integer> caseIds = this.relationManager.getRelationIDsByType(fp.getRelationID(), fp.getEntityID(), fp.getRelationType(), RelationItem.TYPE_CASE);
            if (caseIds != null && caseIds.size() > 0) {
                caseSolrQuery.append(" OR ").append(SolrCaseRepresenter.CASE_ID).append(":(").append(ServerUtils.getAsCommoDelimited(caseIds, "0", " ")).append(")");
            }
        }
        if (isRelation) {
            caseSolrQuery.append(" )");
        }

        if (fp.getWebFormID() != null && !"".equals(fp.getWebFormID())) {
            caseSolrQuery.append(" AND ").append(SolrCaseRepresenter.CASE_ORIGIN_ID).append(":").append(fp.getWebFormID());
        }
        if (fp.getObjectId() != null && !"".equals(fp.getObjectId())) {
            caseSolrQuery.append(" AND ").append("(").append(SolrCaseRepresenter.CASE_ID).append(":").append(fp.getObjectId());
        }

        if (fp.isLookUp()) {
            caseSolrQuery.append(" AND NOT ( ").append(SolrCaseRepresenter.STATUS_CODE).append(":").append(CaseItem.CASE_STATUS_CLOSED);
            caseSolrQuery.append(" OR ").append(SolrCaseRepresenter.STATUS_CODE).append(":").append(EdsCase.RESOLVED).append(" )");
        }
        if (fp.getObjectId() != null) {
            caseSolrQuery.append(")");
        }
        // ---- from kanban board ----
        if (Integer.valueOf(-1).equals(fp.getColumnMetadataId())) {
            caseSolrQuery.append(" AND -(").append(SolrCaseRepresenter.STATUS_ID).append(":").append("[* TO *]").append(")");
        } else if (fp.getColumnMetadataId() != null) {
            caseSolrQuery.append(" AND (").append(SolrCaseRepresenter.STATUS_ID).append(":").append(fp.getColumnMetadataId()).append(")");
        }
        // Set Search key
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            caseSolrQuery.append(" AND (").append(!fp.isLookUp() ? SolrCaseRepresenter.COMPOSITE : SolrCaseRepresenter.COMPOSITE_CASE_SUBJECT).append(":( ").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey(), fp.isLookUp())).append(" )");
            if (!fp.isLookUp()) {
                SolrSearchUtils searchUtils = new SolrSearchUtils();
                searchUtils.generateSearchQuery(caseSolrQuery, this.getCrmCaseSearchFields(), fp.getSearchKey());
            }
            caseSolrQuery.append(")");
        }
        if (facetFilter != null) {
            caseSolrQuery.append(" AND ").append(SolrCaseRepresenter.IN_TRASH).append(":").append(facetFilter.getCustomData().getOrDefault(CaseItem.IN_TRASH, "false"));
            if (facetFilter.getCustomData().containsKey(CaseItem.CASE_ATTACHMENT) && (Boolean.valueOf(facetFilter.getCustomData().get(CaseItem.CASE_ATTACHMENT)))) {
                caseSolrQuery.append(" AND ").append(SolrCaseRepresenter.HAS_ATTACHMENT).append(":").append(facetFilter.getCustomData().get(CaseItem.CASE_ATTACHMENT));
            }
            if (facetFilter.getStartDate() != null && facetFilter.getEndDate() != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String betweenDate = SolrCaseRepresenter.CREATE_DATE;
                if (facetFilter.getCustomData().containsKey(CaseItem.LAST_UPDATED_DATE) && (facetFilter.getCustomData().containsKey(CaseItem.LAST_UPDATED_DATE))) {
                    betweenDate = SolrCaseRepresenter.LAST_UPDATE_DATE;
                }
                caseSolrQuery.append(" AND (").append(betweenDate).append(":[ ").append(format.format(ServerUtils.getCompanyDate(facetFilter.getStartDate(), edsCompany)))
                        .append(" TO ").append(format.format(ServerUtils.getCompanyDate(facetFilter.getEndDate(), edsCompany))).append(" ]").append(")");
            }
        } else {
            caseSolrQuery.append(" AND ").append(SolrCaseRepresenter.IN_TRASH).append(":").append("false");
        }
        return caseSolrQuery.toString();
    }

    public Map<String, Double> getCrmCaseSearchFields() {
        Map<String, Double> fields = new HashMap<>();
        fields.put(SolrCaseRepresenter.COMPOSITE_CASE_SUBJECT, SolrSearchUtils.HIGH_PRIORITY);
        fields.put(SolrCaseRepresenter.FIELD_DYN_STRING_COMPOSITE, SolrSearchUtils.HIGH_PRIORITY);
        return fields;
    }

    private SearchResultItemList getPurchaseOrderOverallSearch(EdsUser edsUser, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(this.invoiceCircularResolver.getPurchaseOrderSolrQuery(lfp, edsUser, false));
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setFields(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID, SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER,
                SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME, SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, SolrSaleInvoiceRepresenter.FIELD_CURRENCY_NAME,
                SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE, SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT);
        query.setSort(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID, SolrQuery.ORDER.desc);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_PURCHASE_ORDER_CORE);
        int totalCount = (int) resp.getResults().getNumFound();
        int qTime = resp.getQTime();
        int i = 0;
        SearchResultItem[] foundItems = new SearchResultItem[resp.getResults().size()];
        for (SolrDocument relevantDoc : resp.getResults()) {
            foundItems[i] = new SearchResultItem();
            foundItems[i].setEntityID(SolrUtils.asInteger(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
            foundItems[i].setName(SolrUtils.asString(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER));
            foundItems[i].setDescription(SolrUtils.asString(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME));
            foundItems[i].setDateCreated(ServerUtils.dateFormat(SolrUtils.asDate(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE), Constants.SHORT_DATE_FORMAT_13));
            foundItems[i].setTitleLink(EncryptionHelper.encryptURL("purchaseorder/" + foundItems[i].getEntityID()));
            foundItems[i].setPlainLink("purchaseorder/" + foundItems[i].getEntityID());
            foundItems[i++].setHighlits(this.getOveralSearchHighlits(relevantDoc,
                    new String[]{SolrSaleInvoiceRepresenter.FIELD_CURRENCY_NAME, SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME,
                            SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT},
                    new String[]{"Currency Name", "Related Project Name", "Status Name", "Due Ammount"}
            ));
        }
        return new SearchResultItemList(totalCount, foundItems, qTime);
    }

    private SearchResultItemList getSaleInvoiceOverallSearch(EdsUser edsUser, ListingFilterParameter lfp) {
        SolrQuery query = new SolrQuery();
        query.setQuery(this.invoiceCircularResolver.getSaleInvoiceSolrQuery(lfp, edsUser, false, null));
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setFields(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID, SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER,
                SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME, SolrSaleInvoiceRepresenter.FIELD_CURRENCY_NAME, SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE,
                SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, SolrSaleInvoiceRepresenter.FIELD_PAID_AMOUNT, SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT);
        query.setSort(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID, SolrQuery.ORDER.desc);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_SALEINVOICE_CORE);
        int totalCount = (int) resp.getResults().getNumFound();
        int qTime = resp.getQTime();
        int i = 0;
        SearchResultItem[] foundItems = new SearchResultItem[resp.getResults().size()];
        for (SolrDocument relevantDoc : resp.getResults()) {
            foundItems[i] = new SearchResultItem();
            foundItems[i].setEntityID(SolrUtils.asInteger(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
            foundItems[i].setName(SolrUtils.asString(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER));
            foundItems[i].setDescription(SolrUtils.asString(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME));
            foundItems[i].setDateCreated(ServerUtils.dateFormat(SolrUtils.asDate(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE), Constants.SHORT_DATE_FORMAT_13));
            foundItems[i].setTitleLink(EncryptionHelper.encryptURL("saleinvoice/" + foundItems[i].getEntityID()));
            foundItems[i].setPlainLink("saleinvoice/" + foundItems[i].getEntityID());
            foundItems[i++].setHighlits(this.getOveralSearchHighlits(relevantDoc,
                    new String[]{SolrSaleInvoiceRepresenter.FIELD_CURRENCY_NAME, SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME,
                            SolrSaleInvoiceRepresenter.FIELD_PAID_AMOUNT, SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT},
                    new String[]{"Currency Name", "Status Name", "Paid Ammount", "Due Amount"}
            ));
        }

        return new SearchResultItemList(totalCount, foundItems, qTime);
    }

    private SearchResultItemList getSaleQuoteOverallSearch(EdsUser edsUser, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(this.invoiceCircularResolver.getSaleQuoteSolrQuery(lfp, edsUser, false, null));
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setFields(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID, SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER,
                SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME, SolrSaleInvoiceRepresenter.FIELD_CURRENCY_NAME, SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE,
                SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME, SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT);
        query.setSort(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID, SolrQuery.ORDER.desc);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_SALEQUOTE_CORE);
        int totalCount = (int) resp.getResults().getNumFound();
        int qTime = resp.getQTime();
        int i = 0;
        SearchResultItem[] foundItems = new SearchResultItem[resp.getResults().size()];
        for (SolrDocument relevantDoc : resp.getResults()) {
            foundItems[i] = new SearchResultItem();
            foundItems[i].setEntityID(SolrUtils.asInteger(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID).toString());
            foundItems[i].setName(SolrUtils.asString(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_INVOICE_NUMBER));
            foundItems[i].setDescription(SolrUtils.asString(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_CLIENT_NAME));
            foundItems[i].setDateCreated(ServerUtils.dateFormat(SolrUtils.asDate(relevantDoc, SolrSaleInvoiceRepresenter.FIELD_INVOICE_DATE), Constants.SHORT_DATE_FORMAT_13));
            foundItems[i].setTitleLink(EncryptionHelper.encryptURL("salequote/" + foundItems[i].getEntityID()));
            foundItems[i].setPlainLink("salequote/" + foundItems[i].getEntityID());
            foundItems[i++].setHighlits(this.getOveralSearchHighlits(relevantDoc,
                    new String[]{SolrSaleInvoiceRepresenter.FIELD_CURRENCY_NAME, SolrSaleInvoiceRepresenter.FIELD_STATUS_NAME,
                            SolrSaleInvoiceRepresenter.FIELD_RELATED_PROJECT_NAME, SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT},
                    new String[]{"Currency Name", "Related Project Name", "Status Name", "Due Amount"}
            ));
        }

        return new SearchResultItemList(totalCount, foundItems, qTime);
    }

    private SearchResultItemList getNewsOverallSearch(EdsUser edsUser, EdsCompany edsCompany, ListingFilterParameter lfp) {
        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getWorkspaceNewsListCore(lfp, edsUser, edsUser.getCompany()));
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setFields(SolrNewsRepresenter.FIELD_NEWS_ID, SolrNewsRepresenter.FIELD_SUBJECT,
                SolrNewsRepresenter.FIELD_FULL_TEXT, SolrNewsRepresenter.FIELD_USER, SolrNewsRepresenter.FIELD_DATE);
        query.setSort(SolrNewsRepresenter.FIELD_DATE, SolrQuery.ORDER.desc);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_NEWS_CORE);
        int totalCount = (int) resp.getResults().getNumFound();
        int qTime = resp.getQTime();
        int i = 0;
        SearchResultItem[] foundItems = new SearchResultItem[resp.getResults().size()];
        for (SolrDocument relevantDoc : resp.getResults()) {
            foundItems[i] = new SearchResultItem();
            foundItems[i].setEntityID(SolrUtils.asInteger(relevantDoc, SolrNewsRepresenter.FIELD_NEWS_ID).toString());
            foundItems[i].setName(SolrUtils.asString(relevantDoc, SolrNewsRepresenter.FIELD_SUBJECT));
            foundItems[i].setDescription(SolrUtils.asString(relevantDoc, SolrNewsRepresenter.FIELD_FULL_TEXT));
            foundItems[i].setDateCreated(ServerUtils.dateFormat(SolrUtils.asDate(relevantDoc, SolrNewsRepresenter.FIELD_DATE), Constants.SHORT_DATE_FORMAT_13));
            foundItems[i].setTitleLink(EncryptionHelper.encryptURL("news/" + foundItems[i].getEntityID()));
            foundItems[i].setPlainLink("news/" + foundItems[i].getEntityID());
            foundItems[i++].setHighlits(this.getOveralSearchHighlits(relevantDoc,
                    new String[]{SolrNewsRepresenter.FIELD_USER},
                    new String[]{"Posted By"}));
        }
        return new SearchResultItemList(totalCount, foundItems, qTime);
    }

    /*private SearchResultItemList getEventOverallSearch(ListingFilterParameter lfp, EdsUser edsUser) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getEventCoreSolrQuery(edsUser, null, lfp));

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setFields(SolrEventRepresenter.FIELD_EVENT_ID, SolrEventRepresenter.FIELD_SUBJECT,
                SolrEventRepresenter.FIELD_DESCRIPTION, SolrEventRepresenter.FIELD_OWNER_NAME, SolrEventRepresenter.FIELD_START_DATE);

        query.setSort(SolrEventRepresenter.FIELD_EVENT_ID, SolrQuery.ORDER.desc);

        QueryResponse resp = getSolrQueryResult(query, SOLR_EVENT_CORE);
        int totalCount = (int) resp.getResults().getNumFound();
        int qTime = resp.getQTime();
        int i = 0;
        SearchResultItem[] foundItems = new SearchResultItem[resp.getResults().size()];
        for (SolrDocument relevantDoc : resp.getResults()) {
            foundItems[i] = new SearchResultItem();
            foundItems[i].setEntityID(SolrUtils.asInteger(relevantDoc, SolrEventRepresenter.FIELD_EVENT_ID).toString());
            foundItems[i].setName(SolrUtils.asString(relevantDoc, SolrEventRepresenter.FIELD_SUBJECT));
            foundItems[i].setDescription(SolrUtils.asString(relevantDoc, SolrEventRepresenter.FIELD_DESCRIPTION));
            foundItems[i].setDateCreated((ServerUtils.dateFormat(SolrUtils.asDate(relevantDoc, SolrEventRepresenter.FIELD_START_DATE), ServerUtils.SHORT_DATE_FORMAT_13)));
            foundItems[i].setTitleLink(EncryptionHelper.encryptURL("event/" + foundItems[i].getEntityID()));
            foundItems[i].setPlainLink("event/" + foundItems[i].getEntityID());
            foundItems[i++].setHighlits(getOveralSearchHighlits(relevantDoc,
                    new String[]{SolrEventRepresenter.FIELD_OWNER_NAME},
                    new String[]{"Owner Name"}));
        }
        return new SearchResultItemList(totalCount, foundItems, qTime);
    }*/

    private SearchResultItemList getProjectOverallSearch(EdsUser edsUser, EdsCompany edsCompany, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getProjectSolrQuery(lfp, edsUser, edsCompany, edsUser.getRoleIds(), null));
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setFields(SolrProjectListRepresenter.FIELD_PROJECT_ID, SolrProjectListRepresenter.FIELD_PROJECT_NAME,
                SolrProjectListRepresenter.FIELD_PROJECT_DESCRIPTION, SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_NAME,
                SolrProjectListRepresenter.FIELD_PROJECT_BACKUP_MANAGER_NAME, SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_NAME,
                SolrProjectListRepresenter.FIELD_PROJECT_STATUS_NAME, SolrProjectListRepresenter.FIELD_USER_NAME, SolrProjectListRepresenter.FIELD_LAST_UPDATE_DATE);
        query.setSort(SolrProjectListRepresenter.FIELD_LAST_UPDATE_DATE, SolrQuery.ORDER.desc);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_PROJECT_CORE);
        Map<Integer, List<SolrDocument>> results = new HashMap<>();
        int totalCount = (int) resp.getResults().getNumFound();
        int qTime = resp.getQTime();
        // adding solr proposed results to map
        int i = 0;
        Map<Integer, Integer> projectOrder = new HashMap<>();
        Map<Integer, ArrayList<String>> projectAssignee = new HashMap<>();
        for (SolrDocument sDoc : resp.getResults()) {
            Integer projectID = (Integer) sDoc.getFieldValue(SolrProjectListRepresenter.FIELD_PROJECT_ID);
            List<SolrDocument> documents = results.get(projectID);
            if (documents == null) {
                documents = new ArrayList<>();
                results.put(projectID, documents);
                projectAssignee.put(projectID, new ArrayList<>());
            }
            documents.add(sDoc);
            projectOrder.put(i, projectID);
            i++;
        }
        int projectSize = projectOrder.keySet().size();
        SearchResultItem[] foundItems = new SearchResultItem[projectSize];
        for (int j = 0; j < projectSize; j++) {
            Integer projectId = projectOrder.get(j);
            List<SolrDocument> relatedEntries = results.get(projectId);
            SolrDocument relevantDoc = null;
            foundItems[j] = new SearchResultItem();
            for (SolrDocument doc : relatedEntries) {
                relevantDoc = doc;
                break;
            }
            foundItems[j].setEntityID(SolrUtils.asInteger(relevantDoc, SolrProjectListRepresenter.FIELD_PROJECT_ID).toString());
            foundItems[j].setName(SolrUtils.asString(relevantDoc, SolrProjectListRepresenter.FIELD_PROJECT_NAME));
            foundItems[j].setDescription(SolrUtils.asString(relevantDoc, SolrProjectListRepresenter.FIELD_PROJECT_DESCRIPTION));
            foundItems[j].setDateCreated(ServerUtils.dateFormat(SolrUtils.asDate(relevantDoc, SolrProjectListRepresenter.FIELD_LAST_UPDATE_DATE), Constants.SHORT_DATE_FORMAT_13));
            foundItems[j].setTitleLink(EncryptionHelper.encryptURL("project/" + foundItems[j].getEntityID()));
            foundItems[j].setPlainLink("project/" + foundItems[j].getEntityID());
            foundItems[j].setHighlits(this.getOveralSearchHighlits(relevantDoc, new String[]{SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_NAME,
                    SolrProjectListRepresenter.FIELD_PROJECT_BACKUP_MANAGER_NAME, SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_NAME,
                    SolrProjectListRepresenter.FIELD_PROJECT_STATUS_NAME}, new String[]{"Project Manager", "Backup Manager", "Client Name", "Status"}));
            foundItems[j].getHighlits().put("Assignees", this.getEntityAssignees(projectAssignee, lfp.getSearchKey(), projectId));


        }
        return new SearchResultItemList(totalCount, foundItems, qTime);
    }

    private SearchResultItemList getTaskOveralSearchData(EdsUser edsUser, EdsCompany edsCompany, ListingFilterParameter lfp, FacetFilterRpc facetFilter) {
        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getTaskCoreSolrQuery(edsUser, edsCompany, facetFilter, lfp, this.groupManager.getCompanyBuiltInGroup(EdsGroup.ADMINISTRATORS)));
        query.setStart(lfp.getStart());
        query.setRows(lfp.getLimit());
        query.setParam(GroupParams.GROUP, true);
        query.setParam(GroupParams.GROUP_TOTAL_COUNT, true);
        query.setParam(GroupParams.GROUP_FIELD, SolrTaskRepresenter.FIELD_TASK_ID);
        query.setFields(SolrTaskRepresenter.FIELD_TASK_ID,
                SolrTaskRepresenter.FIELD_TASK_STATUS, SolrTaskRepresenter.FIELD_TASK_PRIORITY,
                SolrTaskRepresenter.FIELD_TASK_NAME, SolrTaskRepresenter.FIELD_TASK_DESCRIPTION, SolrTaskRepresenter.FIELD_TASK_PROJECT_NAME,
                SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_NAME, SolrTaskRepresenter.FIELD_LAST_UPDATE_DATE, SolrTaskRepresenter.FIELD_RANK);
        query.setSort(SolrTaskRepresenter.FIELD_LAST_UPDATE_DATE, SolrQuery.ORDER.desc);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_TASK_CORE);
        GroupCommand groupCommand = resp.getGroupResponse().getValues().get(0);

        Map<Integer, List<SolrDocument>> results = new HashMap<>();
        List<Integer> taskIds = new ArrayList<>();
        int totalCount = groupCommand.getNGroups();
        int qTime = resp.getQTime();

        // adding solr proposed results to map
        int i = 0;
        Map<Integer, Integer> taskOrder = new HashMap<>();
        Map<Integer, ArrayList<String>> taskAssignee = new HashMap<>();
        for (Group group : groupCommand.getValues()) {
            SolrDocumentList solrDocList = group.getResult();
            SolrDocument solrDoc = solrDocList.get(0);
            Integer taskid = Integer.parseInt(SolrUtils.asString(solrDoc, SolrTaskRepresenter.FIELD_TASK_ID));
            results.put(taskid, group.getResult());
            taskIds.add(taskid);
        }

        int taskSize = taskOrder.keySet().size();
        SearchResultItem[] foundItems = new SearchResultItem[taskSize];
        if (taskIds.size() > 0) {
            for (int j = 0; j < taskSize; j++) {
                foundItems[j] = new SearchResultItem();
                Integer taskId = taskOrder.get(j);
                List<SolrDocument> relatedEntries = results.get(taskId);
                Integer rank = 0;
                SolrDocument relevantDoc = null;
                for (SolrDocument doc : relatedEntries) {
                    Integer currentRank = (Integer) doc.getFieldValue(SolrTaskRepresenter.FIELD_RANK);
                    if (rank < currentRank) {
                        relevantDoc = doc;
                        rank = currentRank;
                    }
                }
                foundItems[j].setEntityID(SolrUtils.asInteger(relevantDoc, SolrTaskRepresenter.FIELD_TASK_ID).toString());
                foundItems[j].setTitleLink(EncryptionHelper.encryptURL("task/" + foundItems[j].getEntityID()));
                foundItems[j].setPlainLink("task/" + foundItems[j].getEntityID());
                foundItems[j].setName(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_NAME));
                foundItems[j].setDescription(SolrUtils.asString(relevantDoc, SolrTaskRepresenter.FIELD_TASK_DESCRIPTION));
                foundItems[j].setDateCreated(ServerUtils.dateFormat(SolrUtils.asDate(relevantDoc, SolrTaskRepresenter.FIELD_LAST_UPDATE_DATE), Constants.SHORT_DATE_FORMAT_13));
                foundItems[j].setHighlits(this.getOveralSearchHighlits(relevantDoc,
                        new String[]{SolrTaskRepresenter.FIELD_TASK_STATUS, SolrTaskRepresenter.FIELD_TASK_PRIORITY, SolrTaskRepresenter.FIELD_TASK_PROJECT_NAME,
                                SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_NAME}
                        , new String[]{"Task Status", "Task Priority", "Project Name", "Client Name"}
                ));
                foundItems[j].getHighlits().put("Assignees", this.getEntityAssignees(taskAssignee, lfp.getSearchKey(), taskId));
            }
        }
        return new SearchResultItemList(totalCount, foundItems, qTime);
    }

    private String getEntityAssignees(Map<Integer, ArrayList<String>> assigneeMap, String searchKey, Integer entityID) {
        ArrayList<String> assigness = assigneeMap.get(entityID);
        String assigneesText = "";
        for (String assignee : assigness) {
            if (assignee != null && (assignee.contains(searchKey) || searchKey.contains(assignee))) {
                if (!"".equals(assigneesText)) {
                    assigneesText = "," + assigneesText;
                }
                assigneesText = assignee + assigneesText;
            } else if (assignee != null) {
                if (!"".equals(assigneesText)) {
                    assigneesText = assigneesText + ",";
                }
                assigneesText = assigneesText + assignee;
            }
        }
        return assigneesText;
    }

    private HashMap<String, String> getOveralSearchHighlits(SolrDocument relevantDoc, String[] solrCodes, String[] highlitTitle) {
        HashMap<String, String> highlits = new HashMap<>();
        int i = 0;
        for (String solrCodeName : solrCodes) {
            highlits.put(highlitTitle[i++], SolrUtils.asHighLitsString(relevantDoc, solrCodeName));
        }
        return highlits;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OverallSearchRpc searchByAllModule(AdvancedSearchRpc advancedSearchRpc) {
        OverallSearchRpc overallSearchRpc = new OverallSearchRpc();
        EdsUser edsUser = this.userManager.getUser();
        ListingFilterParameter lfp = new ListingFilterParameter();
        lfp.setSearchKey(advancedSearchRpc.getSearchKey());

        overallSearchRpc.getOverallSearchMap().put(SearchModuleType.PM, new ModuleOverallSearchRpc());
        overallSearchRpc.getOverallSearchMap().put(SearchModuleType.CRM, new ModuleOverallSearchRpc());
        overallSearchRpc.getOverallSearchMap().put(SearchModuleType.Accounting, new ModuleOverallSearchRpc());
        overallSearchRpc.getOverallSearchMap().put(SearchModuleType.Workspace, new ModuleOverallSearchRpc());
        overallSearchRpc.getOverallSearchMap().put(SearchModuleType.Documents, new ModuleOverallSearchRpc());
        if (!"".equals(advancedSearchRpc.getSearchKey()) && (advancedSearchRpc.getSearchKey() != null)) {
            // PM Task
            if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.PM)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.PM).getModuleSectionSearch().contains(ModuleSectionConstants.TASK))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.PM).getModuleOveralSearchMap().put(ModuleSectionConstants.TASK, this.getTaskOveralCount(edsUser, lfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Pm Project                  k
            if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.PM)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.PM).getModuleSectionSearch().contains(ModuleSectionConstants.PROJECT))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.PM).getModuleOveralSearchMap().put(ModuleSectionConstants.PROJECT, this.getProjectOveralCount(edsUser, lfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Crm contact
            /*if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.CRM)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.CRM).getModuleSectionSearch().contains(ModuleSectionConstants.CRM_CONTACT))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.CRM).getModuleOveralSearchMap().put(ModuleSectionConstants.CRM_CONTACT, getCrmContactOverallCount(edsUser, lfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
            // Crm Lead
            if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.CRM)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.CRM).getModuleSectionSearch().contains(ModuleSectionConstants.CRM_LEAD))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.CRM).getModuleOveralSearchMap().put(ModuleSectionConstants.CRM_LEAD, this.getCrmLeadOverallCount(edsUser, lfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Crm Account
            if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.CRM)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.CRM).getModuleSectionSearch().contains(ModuleSectionConstants.CRM_ACCOUNT))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.CRM).getModuleOveralSearchMap().put(ModuleSectionConstants.CRM_ACCOUNT, this.getCrmAccountOverallCount(edsUser, lfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            /*// Crm Opprtunity
            if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.CRM)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.CRM).getModuleSectionSearch().contains(ModuleSectionConstants.CRM_OPPORTUNITY))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.CRM).getModuleOveralSearchMap().put(ModuleSectionConstants.CRM_OPPORTUNITY, getCrmOpportunityOverallCount(lfp, edsUser));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
            // Crm Case
            if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.CRM)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.CRM).getModuleSectionSearch().contains(ModuleSectionConstants.CRM_CASE))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.CRM).getModuleOveralSearchMap().put(ModuleSectionConstants.CRM_CASE, this.getCrmCaseOverallCount(lfp, edsUser));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Accounting Purchase order
            if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.Accounting)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.Accounting).getModuleSectionSearch().contains(ModuleSectionConstants.PURCHASE_ORDER))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.Accounting).getModuleOveralSearchMap().put(ModuleSectionConstants.PURCHASE_ORDER, this.getPurchaseOrderOverallCount(edsUser, lfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Accounting Sale Invoice
            if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.Accounting)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.Accounting).getModuleSectionSearch().contains(ModuleSectionConstants.SALE_INVOICE))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.Accounting).getModuleOveralSearchMap().put(ModuleSectionConstants.SALE_INVOICE, this.getSaleInvoiceOverallCount(edsUser, lfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Accounting Sale Quote
            if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.Accounting)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.Accounting).getModuleSectionSearch().contains(ModuleSectionConstants.SALE_QUOTE))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.Accounting).getModuleOveralSearchMap().put(ModuleSectionConstants.SALE_QUOTE, this.getSaleQuoteOverallCount(edsUser, lfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Workspace News
            if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.Workspace)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.Workspace).getModuleSectionSearch().contains(ModuleSectionConstants.NEWS))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.Workspace).getModuleOveralSearchMap().put(ModuleSectionConstants.NEWS, this.getNewsOverallCount(edsUser, lfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // Workspace Event
            /*if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.Workspace)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.Workspace).getModuleSectionSearch().contains(ModuleSectionConstants.EVENT))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.Workspace).getModuleOveralSearchMap().put(ModuleSectionConstants.EVENT, getEventOverallCount(edsUser, lfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
            // Documents
            if ((advancedSearchRpc.getModuleSearchMap().containsKey(SearchModuleType.Documents)
                    && advancedSearchRpc.getModuleSearchMap().get(SearchModuleType.Documents).getModuleSectionSearch().contains(ModuleSectionConstants.DOCUMENTS))) {
                try {
                    overallSearchRpc.getOverallSearchMap().get(SearchModuleType.Documents).getModuleOveralSearchMap().put(ModuleSectionConstants.DOCUMENTS, this.getDocumentsOveralCount(edsUser, lfp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return overallSearchRpc;
    }

    /*private ModuleSectionRpc getCrmContactOverallCount(EdsUser edsUser, ListingFilterParameter lfp) {
        String caegoryIdsForUserForSolr = contactCategoryManager.getCategoryIDsForUserForSOLR(null, edsUser, null, null);
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getContactListSolrQuery(lfp, null, edsUser.getCompany(), caegoryIdsForUserForSolr, null));

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrContactRepresenter.FIELD_CONTACT_ID);

        QueryResponse resp = getSolrQueryResult(query, SOLR_CONTACT_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());


        return moduleSectionRpc;
    }*/

    private ModuleSectionRpc getCrmLeadOverallCount(EdsUser edsUser, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getLeadListFacetFilterAssigneeQuery(edsUser.getCompany(), edsUser, lfp, null, null));
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrContactRepresenter.FIELD_CONTACT_ID);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_CONTACT_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());

        return moduleSectionRpc;
    }

    private ModuleSectionRpc getCrmAccountOverallCount(EdsUser edsUser, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getCrmAccountListSolrQuery(lfp, edsUser.getCompany(), null, edsUser, null));
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_CRM_ACCOUNT_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());

        return moduleSectionRpc;
    }

    private ModuleSectionRpc getPurchaseOrderOverallCount(EdsUser edsUser, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(this.invoiceCircularResolver.getPurchaseOrderSolrQuery(lfp, edsUser, false));
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_PURCHASE_ORDER_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());

        return moduleSectionRpc;
    }

    private ModuleSectionRpc getSaleQuoteOverallCount(EdsUser edsUser, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(this.invoiceCircularResolver.getSaleQuoteSolrQuery(lfp, edsUser, false, null));
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_SALEQUOTE_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());

        return moduleSectionRpc;
    }

    private ModuleSectionRpc getSaleInvoiceOverallCount(EdsUser edsUser, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(this.invoiceCircularResolver.getSaleInvoiceSolrQuery(lfp, edsUser, false, null));
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrSaleInvoiceRepresenter.FIELD_SALEINVOICE_ID);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_SALEINVOICE_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());

        return moduleSectionRpc;
    }

    /*private ModuleSectionRpc getCrmOpportunityOverallCount(ListingFilterParameter lfp, EdsUser edsUser) {
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getOpportunityCoreSolrQuery(edsUser, null, lfp));

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID);

        QueryResponse resp = getSolrQueryResult(query, SOLR_OPPORTUNITY_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());

        return moduleSectionRpc;
    }*/

    private ModuleSectionRpc getCrmCaseOverallCount(ListingFilterParameter lfp, EdsUser edsUser) {

        SolrQuery query = new SolrQuery();
        query.setQuery(// generate solr query
                this.getCrmCaseSolrQuery(lfp, edsUser.getCompany(), null));
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrCaseRepresenter.CASE_ID);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_CASE_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());

        return moduleSectionRpc;
    }

    private ModuleSectionRpc getNewsOverallCount(EdsUser edsUser, ListingFilterParameter lfp) {
        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getWorkspaceNewsListCore(lfp, edsUser, edsUser.getCompany()));
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrNewsRepresenter.FIELD_NEWS_ID);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_NEWS_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());

        return moduleSectionRpc;
    }

    /*private ModuleSectionRpc getEventOverallCount(EdsUser edsUser, ListingFilterParameter lfp) {

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getEventCoreSolrQuery(null, null, lfp));

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrEventRepresenter.FIELD_EVENT_ID);

        QueryResponse resp = getSolrQueryResult(query, SOLR_EVENT_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());
        return moduleSectionRpc;
    }*/

    private ModuleSectionRpc getDocumentsOveralCount(EdsUser edsUser, ListingFilterParameter lfp) {
        Set<EdsGroup> membershipsGroups = edsUser.getMembershipGroups();
        if (lfp.getFolderType() != null) {
            EdsFolder folder = this.folderManager.getFolder(lfp.getFolderType(), lfp.getCrmEntityId());
            if (folder != null) {
                lfp.setFolderId(folder.getObjectID());
            }
        }

        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getDocumentsSolrCore(lfp, edsUser, edsUser.getCompany(), membershipsGroups));
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrFolderRepresenter.FIELD_FOLDER_ID);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_FOLDER_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());

        return moduleSectionRpc;
    }

    /**
     * <b>... Overall Search by Task Calculate task total and Qtime ...</b>
     * <br/>
     * <b>... Developer Dilshod.T ...</b>
     * <br/>
     * <b>... Created date 13:58 29/11/2011 ...</b>
     *
     * @param edsUser
     * @param lfp
     * @return
     */
    private ModuleSectionRpc getProjectOveralCount(EdsUser edsUser, ListingFilterParameter lfp) {

        SolrQuery query = new SolrQuery();
        query.setQuery(QueryBuilderForSolr.getProjectSolrQuery(lfp, edsUser, edsUser.getCompany(), edsUser.getRoleIds(), null));
        query.setStart(0);
        query.setRows(0);
        query.setFields(SolrProjectListRepresenter.FIELD_PROJECT_ID);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_PROJECT_CORE);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal((int) (resp.getResults().getNumFound()));
        moduleSectionRpc.setqTime(resp.getQTime());

        return moduleSectionRpc;  //To change body of created methods use File | Settings | File Templates.
    }

    /**
     * <b>... Overall Search by Task Calculate task total and Qtime ...</b>
     * <br/>
     * <b>... Developer Dilshod.T ...</b>
     * <br/>
     * <b>... Created date 13:58 29/11/2011 ...</b>
     *
     * @param edsUser
     * @param lfp
     * @return
     */
    private ModuleSectionRpc getTaskOveralCount(EdsUser edsUser, ListingFilterParameter lfp) {
        StringBuilder solrQuery = new StringBuilder();
        FacetFilterRpc facetFilter = new FacetFilterRpc();
        facetFilter.setOverallSearch(true);
        solrQuery.append(QueryBuilderForSolr.getTaskCoreSolrQuery(edsUser, edsUser.getCompany(), facetFilter, lfp, this.groupManager.getCompanyBuiltInGroup(EdsGroup.ADMINISTRATORS)));
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(0);
        query.setRows(0);
        query.setParam(GroupParams.GROUP, true);
        query.setParam(GroupParams.GROUP_TOTAL_COUNT, true);
        query.setParam(GroupParams.GROUP_FIELD, SolrTaskRepresenter.FIELD_TASK_ID);
        query.setFields(SolrTaskRepresenter.FIELD_TASK_ID);

        QueryResponse resp = this.getSolrQueryResult(query, Constants.SOLR_TASK_CORE);
        GroupCommand groupCommand = resp.getGroupResponse().getValues().get(0);
        ModuleSectionRpc moduleSectionRpc = new ModuleSectionRpc();
        moduleSectionRpc.setTotal(groupCommand.getNGroups());
        moduleSectionRpc.setqTime(resp.getQTime());

        return moduleSectionRpc;
    }

    private QueryResponse getSolrQueryResult(SolrQuery query, String core) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(core);
        QueryResponse resp = null;
        try {
            resp = server.query(query);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    @Override
    public UsagePlanItem usagePlanSaveAndGetId(UsagePlanItem usagePlan) {
        return this.myAccountServiceLocal.usagePlanSaveAndGet(usagePlan);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Double getSupportPackagePricePerHostPerPackage(String hostName, String supportPackageNAME) {
        BigDecimal supportPricePerHOSTPerPackage = this.globalAuthJdbcSpringManager.getSupportPricePerPackage(hostName, supportPackageNAME);
        return supportPricePerHOSTPerPackage.doubleValue();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Double getUserRatePerHOST(/*Integer userCount, */String hostName, String pricingPackageName) {
        hostName = hostName != null ? hostName : Constants.HOST_LIVE;
        BigDecimal userRatePerHOST = this.globalAuthJdbcSpringManager.getUserRatePerPackage(hostName, pricingPackageName);
        return userRatePerHOST.doubleValue();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<String, Double> getSupportPackagePrices(String hostName) {
        return this.globalAuthJdbcSpringManager.getSupportPackagePrices(hostName);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getFreeTrialDaysLeft(boolean isPaidCompany) {
        EdsUser user = this.usagePlanManager.getUser();
        EdsUsagePlan usagePlan = this.usagePlanManager.getCurrentUsagePlan(user.getCompany());
        EdsReference freeTrialPeriod = this.referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.FREE_TRIAL);
        if (isPaidCompany) {
            if (usagePlan != null) {
                if (!usagePlan.getPeriodType().getObjectID().equals(freeTrialPeriod.getObjectID())) {
                    Calendar cal1 = new GregorianCalendar();
                    cal1.setTime(new Date());
                    cal1.set(Calendar.HOUR, 0);
                    cal1.set(Calendar.MINUTE, 0);
                    cal1.set(Calendar.SECOND, 0);
                    cal1.set(Calendar.MILLISECOND, 0);
                    Calendar cal2 = new GregorianCalendar();
                    cal2.setTime(usagePlan.getEndDate());
                    cal2.set(Calendar.HOUR, 0);
                    cal2.set(Calendar.MINUTE, 0);
                    cal2.set(Calendar.SECOND, 0);
                    cal2.set(Calendar.MILLISECOND, 0);
                    int d = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 60 * 60 * 24));
                    if (d == 8) {
                        d = 7;
                    }
                    return d;
                }
            }
        } else {
            if (Integer.valueOf(3624).equals(user.getCompany().getObjectID()) || Integer.valueOf(1037).equals(user.getCompany().getObjectID()))//1037
            {
                return 0;
            }
            if (usagePlan != null) {
                if (usagePlan.getPeriodType().getObjectID().equals(freeTrialPeriod.getObjectID())) {
                    Calendar cal1 = new GregorianCalendar();
                    cal1.setTime(new Date());
                    cal1.set(Calendar.HOUR, 0);
                    cal1.set(Calendar.MINUTE, 0);
                    cal1.set(Calendar.SECOND, 0);
                    cal1.set(Calendar.MILLISECOND, 0);
                    Calendar cal2 = new GregorianCalendar();
                    cal2.setTime(usagePlan.getEndDate());
                    cal2.set(Calendar.HOUR, 0);
                    cal2.set(Calendar.MINUTE, 0);
                    cal2.set(Calendar.SECOND, 0);
                    cal2.set(Calendar.MILLISECOND, 0);
                    int d = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 60 * 60 * 24));
                    if (d == 8) {
                        d = 7;
                    }
                    return d;
                }
            }
        }

        return 0;
    }

    public void executeSubscriptionExpirationReport() {
        List<EdsCompany> companies = this.companyManager.getCompanies();
        List<String> schemas = this.companyManager.getExistingSchemas();

        for (EdsCompany company : companies) {

            if (company.hasSchema(schemas)) {
                executor.execute(() -> {
                    ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
                    ServerSecurityContext.getInstance().setDatabase(this.getCompaniesClusterType(company.getObjectID()));

                    CommonServiceImpl.log.info("Start account expire notification - (Company Name: " + company.getName() + ", id=" + company.getObjectID() + ")");
                    EdsUsagePlan usagePlan = this.usagePlanManager.getCurrentUsagePlan(company);
                    EdsReference freeTrialPeriod = this.referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.FREE_TRIAL);

                    if (usagePlan == null) {
                        EdsUsagePlan lastUsagePlan = this.usagePlanManager.getLastUsagePlan(company.getObjectID());
                        if (lastUsagePlan != null && !Boolean.TRUE.equals(lastUsagePlan.getExpiredNotificationSent())) {
                            try {
                                this.sendAccountExpiredEmailNotificationToAdmins(company, lastUsagePlan);
                            } catch (EdsTemplateException e) {
                                e.printStackTrace();
                            }
                            lastUsagePlan.setExpiredNotificationSent(true);
                            this.updateUsageplanInSeparateThread(lastUsagePlan);
                            company.setActiveForce(false);
                            this.companyManager.update(company);
                        } else {
                            CommonServiceImpl.log.info("Expired email already sent");
                        }
                    } else if (freeTrialPeriod != null && freeTrialPeriod.getObjectID().equals(usagePlan.getPeriodType().getObjectID())) {
                        try {
                            this.executeFreeTrialExpirationEmailNotification(company, usagePlan);
                        } catch (EdsTemplateException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            this.executeSubscriptionExpirationEmailNotification(company, usagePlan);
                        } catch (EdsTemplateException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    private void updateUsageplanInSeparateThread(EdsUsagePlan usagePlan) {
        javax.persistence.EntityManager em = this.usagePlanManager.getJpaTemplate().createHibernateEntityManager();
        Transaction tx = null;
        try (Session session = em.unwrap(Session.class)) {
            tx = session.getTransaction();
            tx.begin();

            session.update(usagePlan);
            session.flush();

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            em.close();
        }
    }

    private void executeFreeTrialExpirationEmailNotification(EdsCompany company, EdsUsagePlan usagePlan) throws EdsTemplateException {
        if (usagePlan != null) {
            Date freeTrialEndDate = usagePlan.getEndDate();
            Integer daysLeft = this.getDaysLeft(freeTrialEndDate);
            if (daysLeft == 7 || daysLeft == 1) {
                boolean notSentEmailNotification = false;
                if (daysLeft == 7 && !usagePlan.getSevenDays()) {
                    usagePlan.setSevenDays(true);
                    this.updateUsageplanInSeparateThread(usagePlan);
                    notSentEmailNotification = true;
                } else if (daysLeft == 1 && !usagePlan.getOneDays()) {
                    usagePlan.setOneDays(true);
                    this.updateUsageplanInSeparateThread(usagePlan);
                    notSentEmailNotification = true;
                }
                if (notSentEmailNotification) {
                    this.sendFreeTrialExpirationEmailNotificationToAdmins(company, freeTrialEndDate, daysLeft);
                }
            }
        }
    }

    private void sendFreeTrialExpirationEmailNotificationToAdmins(EdsCompany company, Date freeTrialEndDate, Integer daysLeft) throws EdsTemplateException {
        Integer roleID = EdsRole.ADMIN;
        List<EdsEmployee> companyAdmins = this.userManager.getUsersByROLE(company.getObjectID(), roleID);
        for (EdsEmployee admin : companyAdmins) {
            CommonServiceImpl.log.info("Start account expire notification - (Admin name: " + admin.getName() + ")");
            String subject = "Your free trial " + EdsContextParams.getProductName() + " subscription will end in " + daysLeft + " day(s)";
            this.messageManager.sendFreeTrialExpirationReportNotification(company, admin, daysLeft, freeTrialEndDate, subject);
            /*}*/
        }
    }

    private void executeSubscriptionExpirationEmailNotification(EdsCompany company, EdsUsagePlan usagePlan) throws EdsTemplateException {
        EdsCompanySystemSettings companySystemSettings = this.companySystemSettingsManager.findByCompanyID(company.getObjectID());
        Boolean payPalRecurring = companySystemSettings.getPayPalRecurring();//payPal recurring
        EdsSubscriptionPayment stripeRecurring = null;
        if (StringUtils.isNotBlank(usagePlan.getUnique_guid())) {
            stripeRecurring = this.subscriptionPaymentManager.getByUsageplanUID(usagePlan.getUnique_guid());
        }
        if (!payPalRecurring && usagePlan != null && usagePlan.getPaid() != null && usagePlan.getPaid() && (stripeRecurring == null || StringUtils.isBlank(stripeRecurring.getApiSubscrId()))) {
            Date usagePlanEndDate = usagePlan.getEndDate();
            Integer daysLeft = this.getDaysLeft(usagePlanEndDate);
            //daysLeft == 30 day (1 month), 14 day (2 week), 7 day (1 week), and 1 day
            if (daysLeft == 30 || daysLeft == 14 || daysLeft == 7 || daysLeft == 1) {
                boolean notSentEmailNotification = false;
                if (daysLeft == 30 && !usagePlan.getThirtyDays()) {
                    usagePlan.setThirtyDays(true);
                    this.updateUsageplanInSeparateThread(usagePlan);
                    notSentEmailNotification = true;
                } else if (daysLeft == 14 && !usagePlan.getFourTeenDays()) {
                    usagePlan.setFourTeenDays(true);
                    this.updateUsageplanInSeparateThread(usagePlan);
                    notSentEmailNotification = true;
                } else if (daysLeft == 7 && !usagePlan.getSevenDays()) {
                    usagePlan.setSevenDays(true);
                    this.updateUsageplanInSeparateThread(usagePlan);
                    notSentEmailNotification = true;
                } else if (daysLeft == 1 && !usagePlan.getOneDays()) {
                    usagePlan.setOneDays(true);
                    this.updateUsageplanInSeparateThread(usagePlan);
                    notSentEmailNotification = true;
                }
                if (notSentEmailNotification) {
                    this.sendAccountExpirationEmailNotificationToAdmins(company, usagePlanEndDate, daysLeft);
                }
            }
        }
    }

    private void sendAccountExpiredEmailNotificationToAdmins(EdsCompany company, EdsUsagePlan lastUsagePlan) throws EdsTemplateException {
        EdsCompanySystemSettings companySystemSettings = this.companySystemSettingsManager.findByCompanyID(company.getObjectID());
        Boolean payPalRecurring = companySystemSettings.getPayPalRecurring();//payPal recurring
        EdsSubscriptionPayment stripeRecurring = null;
        if (lastUsagePlan != null && StringUtils.isNotBlank(lastUsagePlan.getUnique_guid())) {
            stripeRecurring = this.subscriptionPaymentManager.getByUsageplanUID(lastUsagePlan.getUnique_guid());
        }
        if (!payPalRecurring && (stripeRecurring == null || StringUtils.isBlank(stripeRecurring.getApiSubscrId()))) {
            Integer roleID = EdsRole.ADMIN;
            List<EdsEmployee> companyAdmins = this.userManager.getUsersByROLE(company.getObjectID(), roleID);
            for (EdsEmployee admin : companyAdmins) {
                CommonServiceImpl.log.info("Start account expire notification - (Admin name: " + admin.getName() + ")");
                String subject = "Your " + EdsContextParams.getProductName() + " account subscription has expired!";
                this.messageManager.sendSubscriptionExpiredReportNotification(company, admin, subject);
                /*}*/
            }
        }
    }

    private void sendAccountExpirationEmailNotificationToAdmins(EdsCompany company, Date usagePlanEndDate, Integer daysLeft) throws EdsTemplateException {
        Integer roleID = EdsRole.ADMIN;
        List<EdsEmployee> companyAdmins = this.userManager.getUsersByROLE(company.getObjectID(), roleID);
        for (EdsEmployee admin : companyAdmins) {
            CommonServiceImpl.log.info("Start account expire notification - (Admin name: " + admin.getName() + ")");
            String subject = "Your " + EdsContextParams.getProductName() + " subscription will end in " + daysLeft + " day(s)";
            this.messageManager.sendSubscriptionExpirationReportNotification(company, admin, daysLeft, usagePlanEndDate, subject);
            /*}*/
        }
    }

    private Integer getDaysLeft(Date usagePlanEndDate) {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(new Date());
        cal1.set(Calendar.HOUR, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(usagePlanEndDate);
        cal2.set(Calendar.HOUR, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        return (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 60 * 60 * 24));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getEmployeePosition(Integer userID) {
        EdsUser user = userID != null ? this.userManager.get(userID) : this.userManager.getUser();
        String position = null;
        if (!user.isClientContact()) {
            EdsEmployee employee = user.getEmployee();
            position = employee.getPosition() != null ? employee.getPosition().getName() : "";
        }
        if (position != null) {
            return position;
        }
        return "";
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPurchaseInvoices() {
        ListLoadConfig config = new ListLoadConfig();
        config.setStart(0);
        config.setLimit(5000);
        InvoiceList list = this.invoiceCircularResolver.getPurchaseInvoiceData(new ListingFilterParameter());
        SelectItem[] selectItems = new SelectItem[list.getList().size()];
        int i = 0;
        for (NewInvoice invoice : list.getList()) {
            SelectItem item = new SelectItem();
            item.setId(invoice.getID());
            item.setName(invoice.getInvoiceNumber());
            selectItems[i] = item;
            i++;
        }
        return selectItems;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getExpenseReports() {
        List<EdsExpenseReport> list = this.reportManager.getEmployeeReports(new ListingFilterParameter(), false);
        SelectItem[] selectItems = new SelectItem[list.size()];
        int i = 0;
        for (EdsExpenseReport listItem : list) {
            SelectItem item = new SelectItem();
            item.setId(listItem.getObjectID());
            item.setName(listItem.getTitle());
            selectItems[i] = item;
            i++;
        }
        return selectItems;
    }

    @Override
    public void createProjectFolder(Integer projectId) {
        this.documentsServiceLocal.createProjectFolder(projectId);
    }

    @Override
    public void createExpensePaymentFolder(Integer paymentId) {
        this.documentsServiceLocal.createExpensePaymentFolder(paymentId);
    }

    @Override
    public void createMailMessageFolder(Integer mailMessageID) {
        this.documentsServiceLocal.createMailMessageFolder(mailMessageID);
    }

    @Override
    @Transactional
    public void indexCompanyFolders(SolrReindexRpc solrReindex) {
        CommonServiceImpl.log.trace(">>>Bigin Indexing company folders CompanyId:=" + solrReindex.getCompanyId());
        ServerSecurityContext.getInstance().setCompanyId(solrReindex.getCompanyId());
        Integer start = 0;
        while (start != -1) {
            start = this.documentsServiceLocal.indexCompanyFolders(solrReindex, start, 20);
            this.userManager.flushAndClear();
        }
        CommonServiceImpl.log.trace(">>>DONE index company folders CompanyId:=" + solrReindex.getCompanyId());
    }

    @Override
    @Transactional
    public void indexFiles(SolrReindexRpc solrReindex) {
        ServerSecurityContext.getInstance().setCompanyId(solrReindex.getCompanyId());
//        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(solrReindex.getCompanyId()));
        CommonServiceImpl.log.trace(">>>Bigin Indexing company files CompanyId:=" + solrReindex.getCompanyId());
        this.solrDbConsistencyManager.removeInconsistences(solrReindex.getCompanyId(), EdsSolrDbConsistency.FILE);
        this.solrDbConsistencyManager.flushAndClear();
        try {
            if (solrReindex.isAllReindex()) {
                this.solrManager.removeCompanyFiles(solrReindex.getCompanyId());
            }
        } catch (SolrServerException | IOException e) {
            log.error("Error File Index. Company ID : {} , Message : {} ", solrReindex.getCompanyId(), e.getMessage());
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;
        List<EdsFileHeader> fileHeaderList = fileHeaderManager.getCompanyFileForSolr(solrReindex, startat, limit);
        while (Objects.nonNull(fileHeaderList) && !fileHeaderList.isEmpty()) {
            folderRbacManager.indexFiles(fileHeaderList);
            folderRbacManager.flushAndClear();
            startat++;
            fileHeaderList = fileHeaderManager.getCompanyFileForSolr(solrReindex, (startat * limit), limit);
        }
        folderRbacManager.flushAndClear();
        CommonServiceImpl.log.trace(">>>DONE index company files CompanyId:=" + solrReindex.getCompanyId());
    }

    @Override
    @Transactional
    public void createSystemFolders(Integer companyId) {
        if (companyId != null) {
            this.documentsServiceLocal.createSystemFolders(companyId);
        } else {
            List<EdsCompany> companys = this.companyManager.getCompanies();
            List<String> schemas = this.companyManager.getExistingSchemas();
            for (EdsCompany company : companys) {
                if (company.hasSchema(schemas)) {
                    this.documentsServiceLocal.createSystemFolders(company.getObjectID());
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FolderResource getTempFolder() {
        EdsCompany company = this.folderManager.getUser().getCompany();
        return this.documentsServiceLocal.getTempFolderByCompany(company != null ? company.getObjectID() : null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FolderResource getTempFolderByCompanyID(String compID, String uID) {
        Integer companyID = null;
        Integer userID = null;
        try {
            companyID = Integer.valueOf(compID);
            userID = Integer.valueOf(uID);
        } catch (Exception ex) {
        }
        boolean companyIDset = false;
        if (companyID != null) {
            ServerSecurityContext.getInstance().setDatabase(this.globalAuthJdbcSpringManager.getCompanyDatabaseName(companyID));
            SecurityContext.setCompanyID(companyID);
            companyIDset = true;
        }
        if (userID != null) {
            ServerSecurityContext.getInstance().setStaticUserID(userID);
        }
        FolderResource folder = this.getTempFolderByCompany(companyID);
        if (companyIDset) {
            SecurityContext.removeCompanyID();
        }
        return folder;
    }

    private FolderResource getTempFolderByCompany(Integer companyID) {
        return this.documentsServiceLocal.getTempFolderByCompany(companyID);
    }

    @Override
    public void removeDocumentEntries(Integer userId) {
        this.documentsServiceLocal.removeDocumentEntries(userId);
    }

    @Override
    public void reIndexProjectDocument(Integer projectId) {
        this.documentsServiceLocal.reIndexProjectDocument(projectId);
    }

    @Override
    public void reIndexTaskDocument(Integer taskId) {
        this.documentsServiceLocal.reIndexTaskDocument(taskId);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public HashMap<String, SelectItem[]> getCSVColumns(Integer objectId) {
        EdsAttachment attachment = this.attachmentManager.get(objectId);
        FindEncodeInputStream inputStream = this.uploadManager.getFindEncodeInputStream(attachment);
        HashMap<String, SelectItem[]> map = new HashMap<>();
        if (attachment.getOriginalName().toLowerCase().contains(".xls") || attachment.getOriginalName().toLowerCase().contains(".xlsx")) {
            map.put(",", this.getExcelHeader(inputStream, 1).get(0));
            return map;
        }
        map.put(",", this.getCSVColumns(this.getByteArrayOfInputStream(inputStream), ',', 0).get(0));
        return map;

    }

    private ArrayList<SelectItem[]> getExcelHeader(FindEncodeInputStream inputStream, Integer needrowcount) {
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);
            ArrayList<SelectItem[]> headers = new ArrayList<>();
            Integer loopCount = sheet.getPhysicalNumberOfRows() > needrowcount ? needrowcount : sheet.getPhysicalNumberOfRows();
            for (int rowIndex = 0; rowIndex < loopCount; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                Iterator<Cell> cellIterator = row.cellIterator();
                List<SelectItem> items = new ArrayList<>();
                int i = 0;
                while (cellIterator.hasNext()) {
                    SelectItem item;
                    Cell cell = cellIterator.next();
                    switch (cell.getCellType()) {
                        case Cell.CELL_TYPE_BOOLEAN -> item = new SelectItem(i++, cell.getBooleanCellValue());
                        case Cell.CELL_TYPE_NUMERIC ->
                                item = new SelectItem(i++, String.valueOf(cell.getNumericCellValue()));
                        case Cell.CELL_TYPE_STRING -> item = new SelectItem(i++, cell.getStringCellValue());
                        default -> {
                            try {
                                item = new SelectItem(i++, cell.getStringCellValue());
                            } catch (Exception e) {
                                try {
                                    item = new SelectItem(i++, String.valueOf(cell.getNumericCellValue()));
                                } catch (Exception e1) {
                                    item = new SelectItem(i++, String.valueOf(cell.getBooleanCellValue()));
                                }
                            }
                        }
                    }
                    items.add(item);
                }
                headers.add(items.toArray(new SelectItem[]{}));
            }
            return headers;


        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private ArrayList<SelectItem[]> getCSVColumns(byte[] bytesOfIS, char defaultSeparator, int needRowCount) {
        ArrayList<SelectItem[]> csvColumns = new ArrayList<>();
        InputStream inputStream = new ByteArrayInputStream(bytesOfIS);
        InputStreamReader isr;
        char[] defaultSeparators = {',', ';', '\t'};
        isr = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        CSVReader reader = new CSVReader(isr, defaultSeparator);
        for (int j = 0; j <= needRowCount; j++) {
            String[] columns = null;
            try {
                columns = reader.readNext();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (columns == null) {
                break;
            }
            if (columns != null && columns.length == 1 && this.getNextSeparator(defaultSeparators, defaultSeparator) != -1) {
                if (columns[0].contains(String.valueOf(defaultSeparators[this.getNextSeparator(defaultSeparators, defaultSeparator)]))) {
                    return this.getCSVColumns(bytesOfIS, defaultSeparators[this.getNextSeparator(defaultSeparators, defaultSeparator)], needRowCount);
                }
            }
            List<SelectItem> items = new ArrayList<>();
            int i = 0;
            for (String s : columns) {
                if (s != null && !"".equals(s)) {
                    if (s.length() > 105) {
                        s = s.substring(0, 100) + "...";
                    }
                    items.add(new SelectItem(i++, s));
                } else {
                    items.add(new SelectItem(i++, "Blank"));
                }
            }
            csvColumns.add(items.toArray(new SelectItem[]{}));
        }
        return csvColumns;
    }

    private byte[] getByteArrayOfInputStream(InputStream is) {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            Charset charset = Charset.defaultCharset();
            if (is instanceof FindEncodeInputStream) {
                charset = ((FindEncodeInputStream) is).getCharset();
                is = ((FindEncodeInputStream) is).getIs();
            }
            try {
                InputStreamReader isReader = new InputStreamReader(is, charset);
                BufferedReader reader = new BufferedReader(isReader);
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString().getBytes();
        }
        return null;
    }

    private EmailTemplateItem getEMLItems(InputStream inputStream) {
        EmailTemplateItem templateItem = new EmailTemplateItem();

        EMLReader emlReader = new EMLReader(inputStream);

        templateItem.setDefault((emlReader.is_Is_Default() != null && "true".equals(emlReader.is_Is_Default())));
        templateItem.setSubject(emlReader.get_Subject() != null ? emlReader.get_Subject() : "");
        templateItem.setFromEmail(emlReader.get_From() != null ? emlReader.get_From() : "");
        templateItem.setMessageHTML(emlReader.get_HTMLBody() != null ? emlReader.get_HTMLBody() : "");
        templateItem.setTestEmail(emlReader.get_To() != null ? emlReader.get_To() : "");

        templateItem.setName(emlReader.get_Template_Name() != null ? emlReader.get_Template_Name() : "");
        templateItem.setFromUserName(emlReader.get_From_User_Name() != null ? emlReader.get_From_User_Name() : "");
        templateItem.setFromUserID((emlReader.get_From_User_Id() != null && !"".equals(emlReader.get_From_User_Id())) ?
                Integer.valueOf(emlReader.get_From_User_Id()) : Integer.valueOf(-1));
        templateItem.setCategoryId((emlReader.get_Category_Id() != null && !"".equals(emlReader.get_Category_Id())) ?
                Integer.valueOf(emlReader.get_Category_Id()) : Integer.valueOf(-3));
        templateItem.setCategoryName(emlReader.get_Category_Name() != null ? emlReader.get_Category_Name() : "");
        templateItem.setCompanyId((emlReader.get_Company_Id() != null && !"".equals(emlReader.get_Company_Id())) ?
                Integer.valueOf(emlReader.get_Company_Id()) : Integer.valueOf(-2));

        return templateItem;

    }

    /**
     * Save Zip file for attachment
     *
     * @param zipFile
     * @return attachment id
     * @throws Exception
     */
    public Integer saveZipFileForAttachment(File zipFile) throws Exception {
//        EdsUser user = userManager.getUser();
        InputStream source = new FileInputStream(zipFile);
        EdsAttachment attachment = new EdsAttachment();
        long size = zipFile.getTotalSpace();
        attachment.setInputStream(source);
        attachment.setSize(size);
        attachment.setOriginalName(zipFile.getName());//file original name
        attachment.setContentType("application/x-zip-compressed");//ZIP contentType (or "application/zip" -- but I think, not use this content type)
        EdsReference uploadType = this.referenceManager.findReference(Constants._UPLOAD_TYPE, EdsContextParams.getUploadType());
        attachment.setType(uploadType);//upload Type
        attachment.setFileType(Constants.ZIP_WITH_EML_FILE);//File Type
        this.attachmentManager.create(attachment);
        source.close();

        return attachment.getObjectID();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean findEMLFileInputZip(Integer uploadFileId) {
        File file = this.generateFile(uploadFileId);
        Enumeration entries;
        try (ZipFile zipFile = new ZipFile(file)) {

            int isEML = 0;
            int isDirectory = 0;
            entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.isDirectory()) {
                    isDirectory++;
                } else {
                    if (entry.getName().lastIndexOf(".eml") != -1) {
                        isEML++;
                    }
                }
            }
            if (isEML == (zipFile.size() - isDirectory)) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EmailTemplateItem[] getImportEMLFiles(Integer zipFileId) {
        File emlFile = this.generateFile(zipFileId);
        List<EmailTemplateItem> templateItems = new ArrayList<>();
        if (emlFile != null) {
            if (emlFile.getPath().lastIndexOf(".eml") != -1) {
                try {
                    InputStream fileStream = new FileInputStream(emlFile);
                    EmailTemplateItem emlItems = this.getEMLItems(fileStream);
                    templateItems.add(emlItems);
                    fileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Enumeration entries;
                try {
                    ZipFile zipFile = new ZipFile(emlFile);

                    entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = (ZipEntry) entries.nextElement();
                        if (!entry.isDirectory() && entry.getName().lastIndexOf(".eml") != -1) {
                            InputStream inputStream = zipFile.getInputStream(entry);
                            EmailTemplateItem templateItem = this.getEMLItems(inputStream);
                            templateItems.add(templateItem);
                            inputStream.close();
                        }
                    }
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Deleted " + emlFile.getName() + " file");
            emlFile.delete();
        }

        return templateItems.toArray(new EmailTemplateItem[]{});
    }

    private File generateFile(Integer zipFileId) {
        EdsUpload upload = (EdsUpload) this.uploadManager.get(zipFileId);
        InputStream inputStream = this.uploadManager.getInputStream(upload);

        File emlFile = null;
        try {
            emlFile = this.writeToCaceFile(upload, inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return emlFile;
    }

    private File writeToCaceFile(EdsUpload upload, InputStream uploadInputStream) throws IOException {
        File cacheFile;
        try {                               //Template directory -- java.io.tmpdir
            cacheFile = new File(System.getProperty("java.io.tmpdir") + File.separator/*"//"*/ + upload.getObjectID() + upload.getOriginalName());
            FileOutputStream writer = new FileOutputStream(cacheFile);
            int read;
            while ((read = uploadInputStream.read()) != -1) {
                writer.write(read);
            }
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException("Error occured during writing\n your file to the cache.");
        }
        return cacheFile;
    }

    private int getNextSeparator(char[] defaultSeparators, char defaultSeparator) {
        int i = 1;
        for (char separator : defaultSeparators) {
            if (separator == defaultSeparator) {
                return i;
            }
            i++;
        }
        return i < defaultSeparators.length ? i : -1;
    }

    @Transactional
    public String addGoogleSyncToQueue(String eventType) {
        this.baseEventsPostProcessor.registerEvent(SyncGoogleContactsEventListenerImpl.TYPE, eventType, this.employeeManager.getUser(), this.employeeManager.getUser());
        return null;
    }


    @Transactional
    public String addContactSyncToQueue(String eventType) {
        this.baseEventsPostProcessor.registerEvent(SyncGoogleContactsEventListenerImpl.TYPE, eventType, this.employeeManager.getUser(), this.employeeManager.getUser());
        return null;
    }

    @Override
    @Transactional
    public void activeCompany(ConsolidationCompanyList rowValue) {
        EdsCompany edsCompany = this.companyManager.get(rowValue.getCompanyId());
        edsCompany.setActive(rowValue.isStatus());
    }

    /**
     * Related WFT Plugin
     *
     * @param pluginName
     * @return pluginVersion
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getWFTPlugin(String pluginName) {
        ServerSecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        EdsWFTPlagin plugin = this.plaginManager.getPlugin(pluginName);
        return (plugin != null && plugin.getVersion() != null) ? plugin.getVersion() : "";
    }

    /**
     * Related Default description character limit
     *
     * @return - default description character limit
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getDefaultDescriptionCharacterLimit() {
        EdsUser user = this.userManager.getUser();
        int defaultDescriptionCharacterLimit = EdsCompanySystemSettings.DEFAULT_DESCRIPTION_CHARACTER_LIMIT;
        EdsCompanySystemSettings companySystemSettings = this.companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
        if (companySystemSettings != null && companySystemSettings.getDescriptionCharacterLimit() != null) {
            defaultDescriptionCharacterLimit = companySystemSettings.getDescriptionCharacterLimit();
        }
        return defaultDescriptionCharacterLimit;
    }

    /**
     * Related Company new dragable workspace enable option
     *
     * @return - enable/disable option
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean enableNewDragableWorkspace() {
        EdsUser user = this.userManager.getUser();
        boolean enableWorkspace = false;
        EdsCompanySystemSettings companySystemSettings = this.companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
        if (companySystemSettings != null && companySystemSettings.getShowDraggableWorkspace() != null) {
            enableWorkspace = companySystemSettings.getShowDraggableWorkspace();
        }
        return enableWorkspace;
    }

    /**
     * Related Company show google contact synchronize
     *
     * @return true or false
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean showContactSynchronize() {
        EdsUser user = this.userManager.getUser();
        boolean showContactSync = true;
        EdsCompanySystemSettings companySystemSettings = this.companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
        if (companySystemSettings != null && companySystemSettings.getShowGoogleContactSync() != null) {
            showContactSync = companySystemSettings.getShowGoogleContactSync();
        }
        return showContactSync;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListPanelToolRpc getUserListPanelSettings(ListPanelType type, String formId, Integer typeId, Integer stepID) {
        EdsListPanelSettings listPanelSettings = null;
        if (type != null || formId != null) {
            listPanelSettings = this.listPanelSettingsManager.getUserListPanelSettings(formId != null ? formId : type.name(), null);
            if (listPanelSettings == null) {
                listPanelSettings = this.listPanelSettingsManager.getDefaultListPanelSettings(formId != null ? formId : type.name());
            }
        }
        ListPanelToolRpc settings = new ListPanelToolRpc();

        if (listPanelSettings != null) {
            settings = WfmJsonUtils.jsonDataConvertToListPanelToolsRpc(listPanelSettings.getSettingsJSONData());
            settings.setType(type);
            settings.setFormID(formId);
            settings.setSortBy(listPanelSettings.getSortBy());
        }

        EdsListPanelGuideSettings edsListPanelGuideSettings = type != null ? this.listPanelGuideSettingsManager.getUserListPanelSettingsByType(type) : null;
        EdsKPIContactDetails contactDetails = null;
        if (this.userManager.getUser() != null && this.userManager.getUser().getCompany() != null) {
            contactDetails = this.kpiContactDetailsManager.getKPIContactDetailsByCountry(this.userManager.getUser().getCompany().getCountry());
        }
        ListPanelGuideSettingsRPC guideSettings = null;
        Boolean forceItToShow = type != null ? this.forceShowGuideSettingsManager.getForceItToShowByType(type) : null;
        if (edsListPanelGuideSettings != null) {
            guideSettings = edsListPanelGuideSettings.getRPC();
            if (contactDetails != null) {
                guideSettings.setDemoURL(contactDetails.getDemoURL());
                guideSettings.setPhoneNumber(contactDetails.getPhoneNumber());
            }
            // WhiteLabel overrides take priority over company contact details, and allow hiding the sections entirely.
            this.hostBasedSettingManager.applyWhiteLabelOverrides(guideSettings);
            if (forceItToShow != null) {
                guideSettings.setForceItToShow(false);
            }
            EdsProperty property = this.propertManager.findByCode(guideSettings.getInstanceName());
            guideSettings.setInstanceName(property != null ? property.getObjectName() : this.commonLocalizer.localize(guideSettings.getInstanceName()));
        }
        settings.setGuideSettings(guideSettings);
        if (type != null) {
            if (ViewName.Employee.equals(type.getViewName())) {
                boolean show = "EmployeeListPanel".equals(type.name()) ? ServerUtils.hasPermission(PermissionConstants.PM_SHOW_ADDITIONAL_INFORMATION) :
                        ServerUtils.hasPermission(PermissionConstants.HRMS_SHOW_ADDITIONAL_INFORMATION);
                settings.setListViewCustomFields(show ? this.getCompanyCustomFieldsForListView(type.getViewName()) : null);
            } else {
                if (typeId == null) {
                    if (ViewName.InventoryItemsView == type.getViewName() || ViewName.AssemblyItemsView == type.getViewName()) {
                        settings.setListViewCustomFields(this.getCompanyCustomFieldsForListView(ViewName.ProductServiceView));
                    } else {
                        if (ListPanelType.EventsListPanel.equals(type)) {
                            ArrayList<CompanyCustomFieldItem> companyCustomFieldItems = new ArrayList<>();
                            companyCustomFieldItems.addAll(this.getCompanyCustomFieldsForListView(type.getViewName()));
                            companyCustomFieldItems.addAll(this.getCompanyCustomFieldsForListView(ListPanelType.ActivityListPanel.getViewName()));
                            settings.setListViewCustomFields(companyCustomFieldItems);
                        } else if (ListPanelType.CertificatePanel.equals(type)) {
                            settings.setListViewCustomFields(getCompanyCustomFieldsForListView(ViewName.Certificates));
                        } else {
                            settings.setListViewCustomFields(this.getCompanyCustomFieldsForListView(type.getViewName()));
                        }
                    }
                } else {
                    String categoryName = "";
                    if (ViewName.CustomFormItems.equals(type.getViewName())) {
                        EdsCustomForm customForm = this.customFormManager.get(typeId);
                        EdsModel model = this.modelManager.get(customForm.getFormID());
                        categoryName = model != null ? model.getViewName() : null;
                    } else {
                        EdsOnboardingStep step = this.onboardingStepManager.get(typeId);
                        categoryName = step.getViewName();
                    }
                    settings.setListViewCustomFields(this.getCompanyCustomFieldsByCategoryForListView(type.getViewName(), categoryName));
                }
            }
            //Set kanban or list view state of user
            if (type.name().equals("TaskListPanel") || type.name().equals("CaseListPanel") || type.name().equals("LeadListPanel") || type.name().equals("OpportunitiesListPanel") || type.name().equals("CandidateListPanel")) {
                EdsUserSettings usersViewState = this.userSettingsManager.getUserSettingsValue(this.userSettingsManager.getUser(),
                        UserSettingsTypeEnum.ItemsDisplayOptions, type.getViewName() + "_viewstate");

                if (usersViewState != null && "kanban".equals(usersViewState.getValue())) {
                    settings.setViewstate("kanban");
                } else {
                    settings.setViewstate("list");
                }
            }
        }
        //End Set kanban or list view state of user
        return settings;
    }

    @Transactional
    public void unForceGuideListingPanelVisibility(ListPanelType panelType) {
        EdsForceShowGuidePanelSettings panelSettings = new EdsForceShowGuidePanelSettings();
        panelSettings.setForceItToShow(false);
        panelSettings.setPanelType(panelType.name());
        this.forceShowGuideSettingsManager.create(panelSettings);
    }

    @Transactional
    public UserSettingsDto getUserSettingsViwe(UserSettingsTypeEnum type, String value) {
        EdsUserSettings userSettingValue = this.userSettingsManager.getUserSettingsValue(this.userSettingsManager.getUser(), type, value);
        if (userSettingValue != null) {
            UserSettingsDto userSettingsDto = new UserSettingsDto(userSettingValue.getUser().getObjectID(), userSettingValue.getKey(), userSettingValue.getValue());
            return userSettingsDto;
        }
        return new UserSettingsDto(this.userSettingsManager.getUser().getObjectID(), "Task_viewstate", "list");
    }

    @Transactional
    public Boolean saveUserSettings(UserSettingsTypeEnum type, String key, String value) {

        EdsUserSettings userSettingValue = this.userSettingsManager.getUserSettingsValue(this.userSettingsManager.getUser(), type, key);

        if (userSettingValue == null) {
            userSettingValue = new EdsUserSettings();
            userSettingValue.setUser(this.userSettingsManager.getUser());
            userSettingValue.setKey(key);
            userSettingValue.setValue(value);
            userSettingValue.setType(type);
            this.userSettingsManager.create(userSettingValue);
        } else {
            userSettingValue.setValue(value);
            this.userSettingsManager.update(userSettingValue);
        }
        return Boolean.TRUE;
    }

    @Transactional
    public void saveListPanelSettings(ListPanelToolRpc settings) {
        EdsUser loggedUser = this.userManager.getUser();
        if (settings.isAppliedSettingsToAll()) {
            if (settings.getType() != null) {

                String type = settings.getFormID() != null ? settings.getFormID() : settings.getType().name();
                String settingsJson = WfmJsonUtils.listPanelToolsConvertToJsonData(settings);

                EdsListPanelSettings defaultPanelSettings = this.listPanelSettingsManager.getDefaultListPanelSettings(type);
                if (defaultPanelSettings == null) {
                    defaultPanelSettings = new EdsListPanelSettings();
                    defaultPanelSettings.setPanelType(type);
                    defaultPanelSettings.setParentID(settings.getStepID());
                    defaultPanelSettings.setDefaultSetting(true);
                }
                defaultPanelSettings.setSettingsJSONData(settingsJson);
                defaultPanelSettings.setSortBy(settings.getSortBy());
                this.listPanelSettingsManager.createOrUpdate(defaultPanelSettings);

                List<EdsUser> companyEmployees = this.userManager.getUsers();
                if (companyEmployees != null && companyEmployees.size() > 0) {
                    for (EdsUser user : companyEmployees) {
                        rabbitMQService.listPanelSettingsMQ(new ListPanelItemMQ(type, settingsJson, settings.getSortBy(), user.getObjectID(), settings.getStepID()));
                    }
                }
            }
        } else {
            this.saveListPanelSettingsForSingleUser(loggedUser, settings);
        }
    }


    @Transactional
    public void saveEnableWorkspaceWelcomePage(boolean isCheck) {
        EdsUser user = this.userManager.getUser();
        EdsCompanySystemSettings systemSettings = this.companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
        if (systemSettings != null) {
            systemSettings.setEnableWorkspaceWelcomePage(isCheck);
        }
    }

    public void saveRecurrenceJob(RecurrenceJobItem item) {
        Integer recurrenceId = this.recurrenceService.saveRecurrenceJob(item);

        if (recurrenceId != null && item.getJobType() == SchedulerConstant.OVERDUE_INVOICE_REMINDER) {
            this.overdueInvoiceReminderSettingsManager.deleteReminderSettingsByRecurrenceId(recurrenceId);
            if (item.getSelectedRoles() != null && item.getSelectedRoles().size() > 0) {
                for (SelectItem roleItem : item.getSelectedRoles()) {
                    EdsRole edsRole = this.roleManager.get(roleItem.getId());
                    EdsOverdueInvoiceReminderSettings reminderSettings = new EdsOverdueInvoiceReminderSettings();
                    reminderSettings.setRole(edsRole);
                    reminderSettings.setRecurrenceId(recurrenceId);
                    this.overdueInvoiceReminderSettingsManager.create(reminderSettings);
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public RecurrenceJobItem getJob(Integer jobType) {
        return this.recurrenceService.getJob(jobType);
    }

    public void deleteFile(Integer fileId) {
        try {
            this.documentsServiceLocal.deleteFile(fileId);
        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
            e.printStackTrace();
        }
    }

    public void deleteFiles(List<Integer> fileIds) {
        try {
            this.documentsServiceLocal.deleteFiles(fileIds);
        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
            e.printStackTrace();
        }
    }

    public void deleteAttachment(Integer attachmentId) {
        this.deleteAttachment(attachmentId, null);
    }

    public void deleteAttachment(Integer attachmentId, Integer companyId) {
        if (companyId != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyId);
        } else {
            EdsUser user = this.attachmentManager.getUser();
            Integer currentCompanyID = user.getCompany().getObjectID();
            ServerSecurityContext.getInstance().setCompanyId(currentCompanyID);
        }
        if (attachmentId != null) {
            EdsAttachment att = this.attachmentManager.get(attachmentId);
            if (att != null) {
                this.attachmentIndexRbacManager.removeIndex(att);
                this.attachmentManager.delete(att);
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsByCategory(ViewName viewName, String category) {
        List<EdsCompanyCustomFieldsSettings> companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsWithCategory(viewName.name(), category);
        if (companyCFs != null) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, viewName, false);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsByRelationship(ViewName viewName, Integer relationship, Integer limitCustomFields) {
        List<EdsCompanyCustomFieldsSettings> companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsByRelationship(viewName.name(), relationship, limitCustomFields);
        if (companyCFs != null) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, null, false);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CompanyCustomFieldItem> getCompanyCustomFields(ViewName viewName) {
        List<EdsCompanyCustomFieldsSettings> companyCFs;
        if (viewName == null)
            companyCFs = null;
        else
            companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsByEntityName(viewName.name());
        if (companyCFs != null && !companyCFs.isEmpty()) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, viewName, false);
        }
        return Lists.newArrayList();
    }

    @Override
    public HashMap<ArrayList<String>, ArrayList<String>> getCustomFieldByEntityCategory(String entityCategory, Integer companyId) {

        HashMap<ArrayList<String>, ArrayList<String>> customFields = new HashMap<>();

        String byPlural = propertManager.findByPlural(entityCategory, companyId);
        String objectName = "CUSTOM_VIEW_" + byPlural;
        String objectName2 = byPlural + "_FORM";
        ArrayList<String> customFieldsPdf = this.companyCFSettingsManager.getCompanyCustomFieldsByEntityCategory(objectName, companyId);
        ArrayList<String> itemTablePdf = this.cfItemTableSettingmanager.getNameByFormId(objectName2, companyId);

        customFields.put(customFieldsPdf, itemTablePdf);
        return customFields;
    }

    @Override
    public ArrayList<CompanyCustomFieldItem> getCustomFieldsForQuickAdd(ViewName viewName) {
        List<EdsCompanyCustomFieldsSettings> companyCFs = viewName != null ? this.companyCFSettingsManager.getCustomFieldsForQuickAdd(viewName.name()) : null;
        if (companyCFs != null && companyCFs.size() > 0) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, viewName, false);
        } else {
            return Lists.newArrayList();
        }
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CompanyCfAndPropertyItems getCompanyCustomFieldsAndFormProperties(ViewName viewName, String formID) {
        EdsUser user = this.attachmentManager.getUser();
        CompanyCfAndPropertyItems companyCfAndPropertyItems = new CompanyCfAndPropertyItems();
        List<EdsCompanyCustomFieldsSettings> companyCFs = viewName != null ? this.companyCFSettingsManager.getCompanyCustomFieldsByEntityName(viewName.name()) : null;
        if (companyCFs != null && !companyCFs.isEmpty()) {
            companyCfAndPropertyItems.setCompanyCustomFieldItems((ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, viewName, false));
        } else {
            companyCfAndPropertyItems.setCompanyCustomFieldItems(Lists.newArrayList());
        }

        if (formID == null && ViewName.Lead.equals(viewName)) {
            formID = LayoutRPC.LEAD_FORM;
        }

        if (formID != null) {
            LinkedHashMap<String, FormProperty> fields = new LinkedHashMap<>();
            EdsFormProperty edsFormProperty = this.formPropertyManager.getByFormID(formID);
            if (edsFormProperty != null) {
                Gson gson = new Gson();
                FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
                for (FormProperty formProperty : formFields) {
                    if (formProperty != null) {
                        if (formProperty.getDefaultValue() != null && formProperty.getDefaultValue().length() == 0) {
                            formProperty.setDefaultValue(null);
                        }
                        if (formProperty.getRoleEdit() != null && formProperty.getRoleEdit().size() > 0) {
                            if (user.hasEitherRoles(formProperty.getRoleEdit().toArray(new Integer[]{}))) {
                                formProperty.setDisabled(false);
                            }
                        }
                        fields.put(formProperty.getCode(), formProperty);
                    }
                }
            }
            companyCfAndPropertyItems.setFormPropertyMap(fields);
        }

        return companyCfAndPropertyItems;
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CompanyCfAndPropertyItems getCandidateFormCustomFieldsForQuestion() {
        CompanyCfAndPropertyItems candidateFields = new CompanyCfAndPropertyItems();
        List<EdsCompanyCustomFieldsSettings> candaiteCFs = this.companyCFSettingsManager.getCFByUiTypesForHrBot();
        if (candaiteCFs != null && !candaiteCFs.isEmpty()) {
            candidateFields.setCompanyCustomFieldItems((ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(candaiteCFs, ViewName.Candidate, false));
        } else {
            candidateFields.setCompanyCustomFieldItems(new ArrayList<>());
        }
        return candidateFields;
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CompanyCustomFieldItem> getCompanyAllCustomFields(ViewName viewName) {
        List<EdsCompanyCustomFieldsSettings> companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsByEntityName(viewName.name());

        if (companyCFs != null && companyCFs.size() > 0) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, viewName, true);
        }
        return Lists.newArrayList();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyAllDropDownCustomFiedsByEntityName(String entityName, Integer objectId, String costumFieldAliceName, boolean isCutomForm) {
        List<EdsCompanyCustomFieldsSettings> companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsByEntityName(entityName);
        if (companyCFs != null && companyCFs.size() == 0 && isCutomForm) {
            List<EdsModel> modelList = this.modelManager.getModelList(entityName);
            EdsModel model = modelList != null ? modelList.get(0) : null;
            if (model != null) {
                companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsWithCategory(ViewName.CustomFormItems.name(), model.getViewName());
            }
        }
        if (companyCFs != null && !companyCFs.isEmpty()) {
            companyCFs = companyCFs.stream()
                    .filter(item -> (Constants.UI_TYPE_DROPDOWN.equals(item.getUiType()))
                            && item.getPredefinedValues() != null).collect(Collectors.toList());
            if (companyCFs.size() > 0) {
                SelectItem[] selectItems = new SelectItem[companyCFs.size()];
                int i = 0;
                for (EdsCompanyCustomFieldsSettings item : companyCFs) {
                    if (costumFieldAliceName.equals(item.getColumnCode()))
                        continue;
                    selectItems[i++] = new SelectItem(item.getObjectID(), item.getFieldName(), item.getPredefinedValuesWithSorting());
                }
                return selectItems;
            }
        }
        return null;
    }

    @Override
    public LinkedHashMap<String, ArrayList<SelectItem>> getColumnsItems(ArrayList<String> columns) {
        LinkedHashMap<String, ArrayList<SelectItem>> coolumnWithItems = new LinkedHashMap<>();
        for (String column : columns) {
            coolumnWithItems.put(column, getReferenceByCode(column));
        }
        return coolumnWithItems;
    }


    public ArrayList<SelectItem> getReferenceByCode(String referenceCode) {
        ArrayList<SelectItem> names = new ArrayList<>();
        if (referenceCode != null && !referenceCode.equals("")) {
            List<String> nameList = referenceManager.getFieldNamesByCode(referenceCode);
            for (String name : nameList) {
                SelectItem selectItem = new SelectItem();
                selectItem.setName(name);
                names.add(selectItem);
            }
        }
        return names;
    }

    @Override
    public CompanyCFAndFormItems getCustomFormCfAndItem(ViewName viewName, Integer objectID, Integer fID, String formId, boolean isCopy, String lookUpType, Integer lookUpTypeId, String convertFormType, Integer convertFormId) {
        CompanyCFAndFormItems companyCFAndFormItems = new CompanyCFAndFormItems();
        companyCFAndFormItems.setCompanyCustomFieldItems(getCompanyCategoryCustomFields(fID));
        companyCFAndFormItems.setFormItems(getCustomFormItem(objectID, fID, formId, isCopy, lookUpType, lookUpTypeId, convertFormType, convertFormId));
        companyCFAndFormItems.setColumnConfigs(itemTableSettingsServiceLocal.getColumnConfigs(formId));
        companyCFAndFormItems.setAttributeItems(getCustomFormAttributes(formId));
        companyCFAndFormItems.setTableCustomFieldItem(getCompanyCustomFields(viewName));
        companyCFAndFormItems.setFormTimerItems(getCustomFormTimerItems(formId));
        companyCFAndFormItems.setCfItemTableSettings(getItemTableSettingsMap(formId));
        return companyCFAndFormItems;
    }

    @Override
    public FormItems getCustomFormTimerItems(String formId) {
        FormItems item = new FormItems();
        EdsCustomForm edsCustomForm = customFormManager.findByFormID(formId);
        if (edsCustomForm != null && edsCustomForm.getTimer() != null) {
            if (edsCustomForm.getTimer() != null) {
                item.setTimer(edsCustomForm.getTimer());
            }
            if (edsCustomForm.getWelcomeMessage() != null) {
                item.setWelcomeMessage(edsCustomForm.getWelcomeMessage());
            }
            if (edsCustomForm.getEndTimeMessage() != null) {
                item.setEndOfTimeMessage(edsCustomForm.getEndTimeMessage());
            }
            if (edsCustomForm.getAttempt() != null) {
                item.setAttempt(edsCustomForm.getAttempt());
            }
        }
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsByColumnCode(ViewName viewName, String columnCode) {
        List<EdsCompanyCustomFieldsSettings> companyCFs = this.companyCFSettingsManager.getCompanyCustomFields(viewName.name(), columnCode);
        if (companyCFs != null && companyCFs.size() > 0) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, viewName, false);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsForListView(ViewName viewName) {
        List<EdsCompanyCustomFieldsSettings> companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsForListView(viewName.name());
        if (companyCFs != null) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, null, false);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsByCategoryForListView(ViewName viewName, String category) {
        List<EdsCompanyCustomFieldsSettings> companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsByCategoryForListView(viewName.name(), category);
        if (companyCFs != null) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, null, false);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsForFiltering(ViewName viewName) {
        List<EdsCompanyCustomFieldsSettings> companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsForFiltering(viewName.name());
        if (companyCFs != null) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, null, false);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CompanyCustomFieldItem> getCompanyCustomFieldsForBaseInvoices(ViewName viewName) {
        List<EdsCompanyCustomFieldsSettings> companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsForBaseInvoices(viewName.name());
        if (companyCFs != null) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, null, false);
        }
        return null;
    }

    private List<CompanyCustomFieldItem> fillObjectFields(List<EdsCompanyCustomFieldsSettings> companyCFs, ViewName viewName, boolean allCFs) {
        List<CompanyCustomFieldItem> itemsList = new ArrayList<>();
        EdsUser user = this.userManager.getUser();
        EdsUserEmailSettings userSettings = null;
        if (user != null) {
            userSettings = userEmailSettingsManager.getUserSettings(user);
        }
        for (EdsCompanyCustomFieldsSettings setting : companyCFs) {
            ArrayList<String> rolecodes = new ArrayList<>();
            if (!setting.getAllowedRoles().isEmpty()) {
                rolecodes.addAll(setting.getAllowedRoleCodes());
                if (setting.getAllowedRoleCodes().contains(Constants.PM_CODE) && ViewName.Project.equals(viewName)) {
                    rolecodes.add(Constants.PMOFPR);
                    rolecodes.add(Constants.BMOFPR);
                }
            }
            if (user == null || user.hasEitherRoles(rolecodes.toArray(new String[]{})) || rolecodes.isEmpty() || allCFs) {
                CompanyCustomFieldItem fieldsItem = new CompanyCustomFieldItem();

                fieldsItem.setDisabled(setting.getDisabled());
                if (setting.getEditFieldRoles() != null && !setting.getEditFieldRoles().isEmpty()) {
                    fieldsItem.setRoleEdit(setting.getEditFieldRoles().stream().map(EdsRole::getObjectID).collect(Collectors.toCollection(ArrayList::new)));
                    if (user != null && user.hasEitherRoles(setting.getEditFieldRoles().toArray(new EdsRole[]{}))) {
                        fieldsItem.setDisabled(false);
                    }
                }
                fieldsItem.setAllowedRoles(setting.getAllowedRoleIds());
                fieldsItem.setObjectId(setting.getObjectID());
                fieldsItem.setEntityId(setting.getObjectID());
                fieldsItem.setColumnCode(setting.getColumnCode());
                fieldsItem.setDefaultName(setting.getFieldName());
                if (setting.getCustomFormlocalization() != null) {
                    fieldsItem.setLocalization(setting.getCustomFormlocalization().getRPC());
                    List<EdsCustomFormLocalization> items;
                    if (userSettings != null && userSettings.getInternationalization() != null) {
                        fieldsItem.setFieldName(setting.getFieldNameLocalization(userSettings.getInternationalization()));
                        fieldsItem.setUserLocale(userSettings.getInternationalization());
                        if (Constants.UI_TYPE_DROPDOWN.equals(setting.getUiType()) || Constants.UI_TYPE_RADIOBUTTON.equals(setting.getUiType()) || Constants.UI_TYPE_CHECKBOX.equals(setting.getUiType())) {
                            if (setting.getPredefinedValues() != null && setting.getPredefinedValues().length > 0) {
                                items = customFormLocalizationManager.getPredefinedValues(setting.getCustomFormlocalization().getObjectID());
                                if (items != null && !items.isEmpty()) {
                                    List<String> values = Arrays.stream(setting.getPredefinedValues()).toList();
                                    List<String> withoutSorting = new ArrayList<>();
                                    List<SelectItem> withSorting = new ArrayList<>();
                                    AtomicInteger count = new AtomicInteger(0);
                                    EdsUserEmailSettings finalUserSettings = userSettings;
                                    items.forEach(item -> {
                                        if (values.contains(item.getDefaultName())) {
                                            withoutSorting.add(item.getNameLocalization(finalUserSettings.getInternationalization()));
                                            withSorting.add(new SelectItem(count.getAndIncrement(), item.getNameLocalization(finalUserSettings.getInternationalization())));
                                        }
                                    });
                                    fieldsItem.setPredefinedValues(withoutSorting.toArray(new String[]{}));
                                    fieldsItem.setPredefinedValuesWithSorting(withSorting.toArray(new SelectItem[]{}));
                                } else {
                                    fieldsItem.setPredefinedValues(setting.getPredefinedValues());
                                    fieldsItem.setPredefinedValuesWithSorting(setting.getPredefinedValuesWithSorting());
                                }
                            }
                        } else if (setting.getPredefinedValues() != null && setting.getPredefinedValues().length > 0) {
                            fieldsItem.setPredefinedValues(setting.getPredefinedValues());
                            fieldsItem.setPredefinedValuesWithSorting(setting.getPredefinedValuesWithSorting());
                        }
                    } else {
                        fieldsItem.setFieldName(setting.getFieldName());
                        fieldsItem.setPredefinedValues(setting.getPredefinedValues());
                        fieldsItem.setPredefinedValuesWithSorting(setting.getPredefinedValuesWithSorting());
                    }

                } else {
                    fieldsItem.setFieldName(setting.getFieldName());
                    fieldsItem.setPredefinedValues(setting.getPredefinedValues());
                    fieldsItem.setPredefinedValuesWithSorting(setting.getPredefinedValuesWithSorting());
                }
                if (Constants.UI_TYPE_DROPDOWN.equals(setting.getUiType()) && setting.getRelationFieldId() != null && !ServerUtils.isNullOrEmpty(setting.getRelationFieldValues())) {
                    fieldsItem.setRelationItemsMap(getRelationFieldValuesWithLocale(setting.getRelationFieldValues(), setting.getRelationFieldId(), setting.getCustomFormlocalization()));
                }

                fieldsItem.setAliasName(setting.getAliasName());
                fieldsItem.setDataType(setting.getDataType());
                fieldsItem.setUiType(setting.getUiType());
                fieldsItem.setColumnWidth(setting.getColumnWidth());
                fieldsItem.setQuery(setting.getQuery());
                fieldsItem.setClickable(setting.isClickable());
                fieldsItem.setFacetable(setting.getFacetable());
                fieldsItem.setRequired(setting.getRequired());
                fieldsItem.setMinChar(setting.getMinChar());
                fieldsItem.setAddTab(setting.isAddTab());
                fieldsItem.setFileUploadFieldId(setting.getObjectID());
                fieldsItem.setEntityName(setting.getEntityName());
                fieldsItem.setEntityCategoryAlias(setting.getEntityCategoryAlias());
                fieldsItem.setEntityCategoryName(setting.getEntityCategoryName());
                fieldsItem.setLookUpTypeEnum(setting.getLookUpType());
                fieldsItem.setPrefix(setting.getPrefix());
                fieldsItem.setMinHeight(setting.getMinHeight());
                fieldsItem.setScale(setting.getScale());
                fieldsItem.setReferenceItem(setting.getReference());
                fieldsItem.setActive(setting.isActive());
                fieldsItem.setSystemField(setting.getDataType() != null && setting.getDataType().equals(Constants.SYSTEM));
                fieldsItem.setRelationFieldId(setting.getRelationFieldId());
                fieldsItem.setRelationFieldValues(setting.getRelationFieldValues());
                fieldsItem.setNumberMinValue(setting.getNumberMinValue());
                fieldsItem.setQuizFormScoreValues(setting.getQuizFormScoreValues());
                fieldsItem.setUseInPermission(setting.isUseInPermission());
                EdsEntityType enType = setting.getEntityType();
                if (enType != null) {
                    SelectItem si = enType.getAsSelectItem();
                    si.setReferenceCode(enType.getCode());
                    fieldsItem.setEntityType(si);
                } else {
                    fieldsItem.setEntityType(null);
                }
                if (setting.getCustomLogicField() != null) {
                    fieldsItem.setCustomLogicField(new SelectItem(setting.getCustomLogicField().getObjectID(), setting.getCustomLogicField().getFieldName(), setting.getCustomLogicField().getColumnCode()));
                    fieldsItem.setCustomLogicValue(setting.getCustomLogicValue());
                }

                if (Constants.UI_TYPE_ENTITY_DROPDOWN.equals(fieldsItem.getUiType())
                        || Constants.TYPE_ENTITY_LOOKUP.equals(fieldsItem.getUiType())
                        || Constants.TYPE_ENTITY_MULTI_LOOKUP.equals(fieldsItem.getUiType())) {
                    fieldsItem.setQueryItems(this.companyCFManager.getCustomFieldDataByQuery(SecurityContext.getCompanyID(), fieldsItem.getQuery()));
                }

                if (setting.getValidations() != null && setting.getValidations().size() > 0) {
                    CustomFieldSettingItem[] validations = new CustomFieldSettingItem[setting.getValidations().size()];
                    int index = 0;
                    for (EdsCustomFieldValidation validation : setting.getValidations()) {
                        validations[index] = new CustomFieldSettingItem();
                        validations[index].setObjectID(validation.getObjectID());
                        validations[index].setCustomFieldID(validation.getCustomfield().getObjectID());
                        validations[index].setCustomFieldName(validation.getCustomfield().getFieldName());
                        validations[index].setValidationCodeID(validation.getValidationCodeID());

                        if (validation.getJoinedField() != null) {
                            validations[index].setJoinedFieldID(validation.getJoinedField().getObjectID());
                            validations[index].setJoinedFieldName(validation.getJoinedField().getFieldName());
                            validations[index].setJoinedColumnCode(validation.getJoinedField().getColumnCode());
                            validations[index].setJoinedColumnUIType(validation.getJoinedField().getUiType());
                        }
                        validations[index].setRegex(validation.getRegexCode());

                        index++;
                    }

                    fieldsItem.setValidations(validations);
                }

                if (setting.getListeners() != null && !setting.getListeners().isEmpty()) {
                    CustomFieldSettingItem[] listeners = new CustomFieldSettingItem[setting.getListeners().size()];
                    int index = 0;
                    for (EdsCustomFieldListener listener : setting.getListeners()) {
                        listeners[index] = new CustomFieldSettingItem();
                        listeners[index].setObjectID(listener.getObjectID());
                        listeners[index].setCustomFieldID(listener.getCustomfield().getObjectID());
                        listeners[index].setCustomFieldName(listener.getCustomfield().getFieldName());
                        listeners[index].setCode(listener.getListenerCode());
                        listeners[index].setJoinedFieldID(listener.getJoinedfield().getObjectID());
                        listeners[index].setJoinedFieldName(listener.getJoinedfield().getFieldName());

                        index++;
                    }

                    fieldsItem.setListeners(listeners);
                }
                itemsList.add(fieldsItem);
            }
        }
        return itemsList;
    }

    /// // Iltimos bu methodd change qilmela ancha muammo bulishi mn relation Drop downlarda boshida ancha chigal kod yozilgan ekan
    private HashMap<String, ArrayList<SelectItem>> getRelationFieldValuesWithLocale(String relationFieldValues, Integer parentId, EdsCustomFormLocalization internationalization) {
        HashMap<String, HashSet<String>> map = new HashMap<>();
        String[] val = relationFieldValues.split(SPLIT_CHARACTER);
        for (String s : val) {
            String[] splittedVal = s.split("=");
            if (splittedVal != null && splittedVal.length > 1) {
                map.computeIfAbsent(splittedVal[1], k -> new HashSet<>());
                map.get(splittedVal[1]).add(splittedVal[0]);
            }
        }
        String lang = ServerUtils.getUserLocale().getLanguage();

        HashMap<String, ArrayList<SelectItem>> result = new HashMap<>();
        EdsCompanyCustomFieldsSettings settings = companyCFSettingsManager.get(parentId);
        if (settings == null) return result;
        map.forEach((key, value) -> {
            String keyName = null;
            for (EdsCustomFormLocalization parentLoc : settings.getCustomFormlocalization().getChildren()) {
                keyName = parentLoc.getNameValueByDefaultString(key, lang);
                if (keyName != null) {
                    break;
                }
            }
            ArrayList<SelectItem> childArray = new ArrayList<>();
            int i = 0;
            for (String strChild : value) {
                String childVal = null;
                for (EdsCustomFormLocalization childLoc : internationalization.getChildren()) {
                    childVal = childLoc.getNameValueByDefaultString(strChild, lang);
                    if (childVal != null) {
                        break;
                    }
                }
                childArray.add(new SelectItem(++i, childVal != null ? childVal : strChild));
            }
            result.put(keyName != null ? keyName : key, childArray);

        });
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListPanelToolRpc getCompanyAddViewFieldsPosition(Integer companyID, ViewAddFiledsCodeName viewAddFields) {
        return this.getCompanyAddViewFieldsPosition(companyID, viewAddFields, null, null);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListPanelToolRpc getCompanyAddViewFieldsPosition(Integer companyID, ViewAddFiledsCodeName viewAddFields, Integer relationship, Integer limitCustomFields) {
        if (companyID != null) {
            ServerSecurityContext.getInstance().setCompanyId(companyID);
        }
        ListPanelToolRpc panelTools = new ListPanelToolRpc();
        EdsAddViewSettings edsAddViewSettings = this.addViewSettingsManager.getAddViewSettings();
        if (edsAddViewSettings != null) {
            List<String> list = edsAddViewSettings.getAddViewFieldsPosition(viewAddFields);
            panelTools.setColumnCodeName(list != null ? new ArrayList<>(list) : null);
        }
        panelTools.setListViewCustomFields(this.getCompanyCustomFieldsByRelationship(viewAddFields.getViewName(), relationship, limitCustomFields));
        ServerSecurityContext.getInstance().removeCompanyId();
        return panelTools;
    }

    @Override
    @Transactional
    public void saveAddViewPosition(Integer companyID, ViewAddFiledsCodeName viewFieldsCode, ArrayList<String> onlyViewShowfieldCodeName) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsAddViewSettings edsAddViewSettings = this.addViewSettingsManager.getAddViewSettings();
        if (edsAddViewSettings == null) {
            edsAddViewSettings = new EdsAddViewSettings();
            this.addViewSettingsManager.create(edsAddViewSettings);
        }
        edsAddViewSettings.saveFieldParams(viewFieldsCode, onlyViewShowfieldCodeName);
        this.addViewSettingsManager.flush();
        ServerSecurityContext.getInstance().removeCompanyId();
    }

    public void saveListPanelSettingsForSingleUser(EdsUser user, ListPanelToolRpc settings) {
        if (settings.getType() == null) {
            return;
        }
        EdsListPanelSettings listPanelSettings = settings.getStepID() == null ? this.listPanelSettingsManager.getUserListPanelSettings(user, settings.getFormID() != null ? settings.getFormID() : settings.getType().name()) : this.listPanelSettingsManager.getUserListPanelSettings(user, settings.getFormID() != null ? settings.getFormID() : settings.getType().name(), settings.getStepID());
        if (listPanelSettings == null) {
            listPanelSettings = new EdsListPanelSettings();
            listPanelSettings.setUser(user);
            listPanelSettings.setPanelType(settings.getFormID() != null ? settings.getFormID() : settings.getType().name());
            listPanelSettings.setParentID(settings.getStepID());
        }
        listPanelSettings.setSettingsJSONData(WfmJsonUtils.listPanelToolsConvertToJsonData(settings));
        listPanelSettings.setSortBy(settings.getSortBy());
        this.listPanelSettingsManager.createOrUpdate(listPanelSettings);
    }

    public void changeLisPanelSettings(EdsListPanelSettings listPanelSettings, EdsUser user) {
        if ("SaleInvoiceListPanel".equals(listPanelSettings.getPanelType())) {
            this.myUpdateManager.registerListPanelSettingsEditUpdate(listPanelSettings, user, new Date());
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getMoreMenuSettings(String actionName) {
        EdsMoreMenuSettings moreMenuSettings = this.moreMenuSettingsManager.getMoreMenuSettings(actionName);
        if (moreMenuSettings != null) {
            return moreMenuSettings.getLinkName();
        }
        return null;
    }

    @Override
    @Transactional
    public SelectItem saveCRMContactCompanyLogo(Integer uploadId, Integer crmContactId) {
        SelectItem compPhoto = null;
        EdsCrmContact edsCrmContact = null;
        EdsUpload edsUpload = (EdsUpload) this.uploadManager.get(uploadId);
        if (crmContactId != null) {
            edsCrmContact = this.crmContactManager.get(crmContactId);
        } else {
            EdsEmployee edsEmployee = (EdsEmployee) this.companyManager.getUser();
            if (edsEmployee.getProfile() != null && edsEmployee.getProfile().getContact() != null) {
                edsCrmContact = edsEmployee.getProfile().getContact();
            }
        }

        String url = uploadManager.getFileURL(edsUpload);
        compPhoto = new SelectItem(edsUpload.getObjectID(), url);

        if (edsCrmContact != null) {
            edsCrmContact.setCompanyPhoto(edsUpload);
            this.crmContactManager.update(edsCrmContact, true);
        }
        return compPhoto;
    }

    @Override
    public SelectItem getCrmContactCompanyLogo() {
        SelectItem companyPhoto = null;
        EdsEmployee edsEmployee = (EdsEmployee) this.userManager.getUser();
        if (edsEmployee != null && edsEmployee.getProfile() != null && edsEmployee.getProfile().getContact() != null) {
            EdsCrmContact edsCrmContact = edsEmployee.getProfile().getContact();
            if (edsCrmContact.getCompanyPhoto() != null) {
                companyPhoto = new SelectItem(edsCrmContact.getCompanyPhoto().getObjectID(), this.getImageUrl(edsCrmContact.getCompanyPhoto().getObjectID()));
            }
        }
        return companyPhoto;
    }

    public TwilioContactItem getIncomingCallerDetails(String phoneNumber) {
        TwilioContactItem result = new TwilioContactItem();

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return result;
        }

        EdsCrmContact contact = this.crmContactManager.getByPhone(phoneNumber);
        if (contact != null) {
            if (contact.getContactType() != null && TYPE_EMPLOYEE_CONTACT == contact.getContactType()) {
                EdsEmployee employee = this.profileManager.getEmployeeByContactId(contact.getObjectID());
                if (employee != null) {
                    result.setObjectID(employee.getObjectID());
                }
            } else {
                result.setObjectID(contact.getObjectID());
            }
            result.setContactType(contact.getContactType());
            if (contact.getRelatedPhone(EdsCrmContactItemParams.MOBILE) != null) {
                result.setMobile(new ArrayList<>(Collections.singletonList(contact.getRelatedPhone(EdsCrmContactItemParams.MOBILE))));
            }
            result.setName(contact.getName());
            result.setOwner(contact.getOwner().getName());
            result.setOwnerId(contact.getOwner().getObjectID());
            result.setEmail(contact.getPrimaryEmail());
            ArrayList<ContactTypeForTwilio> otherFields = this.getOtherContactTypes(phoneNumber);
            result.setOtherTypes(otherFields);
            if (contact.getLeadStatus() != null) {
                result.setStatus(contact.getLeadStatus().getName());
            }
            if (contact.getCrmAccount() != null) {
                result.setCompany(contact.getCrmAccount().getName());
                result.setCompanyId(contact.getCrmAccount().getObjectID());
            }
            EdsOpportunity opportunity = this.opportunityManager.getOpportunityByContactId(contact.getObjectID());
            if (opportunity != null) {
                OpportunityItemForTwilio opportunityItemForTwilio = new OpportunityItemForTwilio();
                if (opportunity.getStage() != null) {
                    opportunityItemForTwilio.setStage(opportunity.getStage().getName());
                }
                if (opportunity.getName() != null) {
                    opportunityItemForTwilio.setName(opportunity.getName());
                }
                if (opportunity.getAmount() != null) {
                    opportunityItemForTwilio.setAmount(String.valueOf(opportunity.getAmount()));
                }
                if (opportunity.getAssignee() != null) {
                    opportunityItemForTwilio.setAssignee(opportunity.getAssignee().getName());
                }
                if (opportunity.getCurrency() != null) {
                    opportunityItemForTwilio.setCurrency(opportunity.getCurrency().getName());
                }
                result.setOpportunity(opportunityItemForTwilio);
            }
            if (contact.getVacancies() != null && !contact.getVacancies().isEmpty()) {
                result.setVacancy(contact.getVacancies().iterator().next().getName());
            }
            if (contact.getCandidateStatus() != null) {
                result.setStatus(contact.getCandidateStatus().getName());
            }
            EdsEmployee employee = this.profileManager.getEmployeeByContactId(contact.getObjectID());
            if (employee != null) {
                EmployeeForTwilio employeeForTwilio = new EmployeeForTwilio();
                EdsEmployee supervisor = this.employeeManager.getSupervisor(employee.getObjectID());
                if (supervisor != null) {
                    employeeForTwilio.setSupervisor(supervisor.getName());
                }
                if (employee.getEmployeeDepartment() != null) {
                    employeeForTwilio.setDepartment(employee.getEmployeeDepartment().getTeam().getName());
                }
                if (employee.getPosition() != null) {
                    employeeForTwilio.setPosition(employee.getPosition().getName());
                }
                if (employee.getEmail() != null) {
                    employeeForTwilio.setEmail(employee.getEmail());
                }
                result.setEmployee(employeeForTwilio);
            }
            return result;
        }
        EdsCrmAccount account = this.crmAccountManager.getByPhone(phoneNumber);
        if (account != null) {
            result.setObjectID(account.getObjectID());
//            result.setContactType(account.getTyp());
            if (StringUtils.isNotBlank(account.getPhone())) {
                result.setMobile(new ArrayList<>(Collections.singletonList(account.getPhone())));
            }
            result.setName(account.getName());
            result.setContactType(TYPE_ACCOUNT);
            if (account.getOwners() != null && !account.getOwners().isEmpty()) {
                result.setOwner(account.getOwners().get(0).getName());
                if (account.getOwners().get(0).getObjectID() != null) {
                    result.setOwnerId(account.getOwners().get(0).getObjectID());
                }
            }
            if (account.getEmail() != null) {
                result.setEmail(account.getEmail());
            }
            if (account.getIndustry() != null) {
                result.setAccountIndustry(account.getIndustry().getName());
            }
            return result;
        }
        return result;

    }

    public ArrayList<ContactTypeForTwilio> getOtherContactTypes(String phoneNumber) {
        ArrayList<ContactTypeForTwilio> otherFields = new ArrayList<>();
        for (EdsCrmContact otherField : this.crmContactManager.getAllByPhone(phoneNumber)) {
            ContactTypeForTwilio contactTypeForTwilio = new ContactTypeForTwilio();
            contactTypeForTwilio.setName(otherField.getName());
            contactTypeForTwilio.setId(otherField.getObjectID());
            contactTypeForTwilio.setContactType(otherField.getContactType());
            if (otherFields.stream().noneMatch(x -> x.getId().equals(contactTypeForTwilio.getId()))) {
                otherFields.add(contactTypeForTwilio);
            }
        }
        List<EdsCrmAccount> accounts = this.crmAccountManager.getAllByPhone(phoneNumber);
        if (accounts != null && accounts.size() > 0) {
            for (EdsCrmAccount account : accounts) {
                ContactTypeForTwilio contactTypeForTwilio = new ContactTypeForTwilio();
                contactTypeForTwilio.setName(account.getName());
                contactTypeForTwilio.setId(account.getObjectID());
                contactTypeForTwilio.setContactType(TYPE_ACCOUNT);
                if (otherFields.stream().noneMatch(x -> x.getId().equals(contactTypeForTwilio.getId()))) {
                    otherFields.add(contactTypeForTwilio);
                }
            }
        }
        return otherFields;
    }

    /**
     * <h1>... This is method delete facet filter saved data ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {18:22 21/06/2011} ...</h3>
     *
     * @param deleleFilterId
     */
    @Override
    @Transactional
    public void deleteFacetFilter(Integer deleleFilterId) {
        EdsFacetFilter edsFacetFilter = this.facetFilterManager.get(deleleFilterId);
        this.facetFilterManager.delete(edsFacetFilter);
    }

    /*@Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SaveFilterSelectItems getSavedFacetFilterLists(String type) {
        return getSavedFacetFilterList(ListPanelType.getByViewName(type), null);
    }*/

    /**
     * <h1>... This is method get user saved list facet filter ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>...Created date {18:31 21/06/2011} ...</h3>
     *
     * @param type
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SaveFilterSelectItems getSavedFacetFilterList(ListPanelType type, Integer typeId) {
        if (type == null) {
            return new SaveFilterSelectItems(null, null, null);
        }
        type.setId(typeId);
        List<Integer> userStarFilterListIds = this.userFilterManager.getUserFavourFacetFiltersID(type);
        List<EdsFacetFilter> facetFilterList = this.facetFilterManager.getUserFacetFilter(type);
        SelectItem[] items = new SelectItem[facetFilterList.size()];
        EdsFacetFilter facetFilter = this.facetFilterManager.getDefaultUserFacetFilter(type, null);
        EdsFacetFilter defaultFilter = this.facetFilterManager.getDefaultUserFacetFilter(type);
        Integer defaultFilterID = facetFilter != null ? facetFilter.getObjectID() : defaultFilter != null ? defaultFilter.getObjectID() : null;
        int k = 0;
        HashMap<Integer, Boolean> publicFilds = new HashMap<>();
        for (EdsFacetFilter edsFacetFilter : facetFilterList) {
            publicFilds.put(edsFacetFilter.getObjectID(), edsFacetFilter.isSystemFilter());
            items[k] = new SelectItem(edsFacetFilter.getObjectID(), edsFacetFilter.getName());
            if (edsFacetFilter.isSystemFilter()) {
                if (ListPanelType.GoodsDeliveredNoteListPanel.equals(type) && defaultFilterID == null && !ServerUtils.hasPermission(PermissionConstants.SAVE_FILTER)) {
                    defaultFilterID = edsFacetFilter.getObjectID();
                }
                items[k].setNewItem(true);// this attribute for system facet filter
                if (Constants.FACET_FILTER_DEFAULT_PARAM_ALL_TASKS.equals(edsFacetFilter.getName())) {
                    items[k].setName(this.commonLocalizer.localize(PdfLocalizationName.allTasks));
                } else if (Constants.FACET_FILTER_DEFAULT_PARAM_CRM_TASKS.equals(edsFacetFilter.getName())) {
                    items[k].setName(this.commonLocalizer.localize(PdfLocalizationName.crmTasks));
                }
            }
            if (userStarFilterListIds.contains(edsFacetFilter.getObjectID())) {
                items[k].setSelected(true);
            }
            k++;
        }
        return new SaveFilterSelectItems(defaultFilterID, publicFilds, items);
    }

    /**
     * <h1>... This is method facet filter checekd fileds to data base with type ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {19:19 21/06/2011} ...</h3>
     *
     * @param type
     * @return
     */

    @Override
    @Transactional
    public Integer saveFacetFilter(FacetFilterRpc facetFilter, ListPanelType type) {
        Integer objectID;
        type.setId(facetFilter.getTypeId());
        if (facetFilter.isSystemFilter()) { // system facet filter
            objectID = this.saveSystemFacetFilter(facetFilter, type);
        } else {
            objectID = this.createOrUpdateFacetFilter(facetFilter, type);
        }
        return objectID;
    }

    @Override
    @Transactional
    public Integer saveFacetFilter(HashMap<String, Object> paramMap) {
        FacetFilterRpc facetFilterRpc = (FacetFilterRpc) paramMap.get("facetFilterRpc");
        ListPanelType type = (ListPanelType) paramMap.get("type");
        return saveFacetFilter(facetFilterRpc, type);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public CompanyCustomFieldItem checkCFNameExists(String type, String category, String fieldName, String aliasName, Integer fieldID, boolean isItemTable, String itemTableOrFormType) {
        CompanyCustomFieldItem customField = new CompanyCustomFieldItem();
        boolean aliasNameExist = this.companyCFSettingsManager.isCustomAliasNameExists(type, category, aliasName, fieldID);
        if (isItemTable && !aliasNameExist) {
            String key = CacheConstants.ITEM_TABLE_SECTION + "_" + ItemTableEnum.valueOf(itemTableOrFormType).getTitle() + "_" + SecurityContext.getCompanyID();
            ColumnConfigs[] settingsJSONData = RedisClient.getKey(key, ColumnConfigs[].class);

            if (settingsJSONData == null || settingsJSONData != null && settingsJSONData.length == 0) {
                Gson gson = new Gson();
                EdsItemTableSettings its = itemTableSettingsManager.getSettingsBySection(ItemTableEnum.valueOf(itemTableOrFormType));
                if (its != null)
                    settingsJSONData = gson.fromJson(its.getSettingsJSONData(), ColumnConfigs[].class);
            }

            if (settingsJSONData != null && settingsJSONData.length > 0) {
                ColumnConfigs[] columns = settingsJSONData;
                if (columns != null) {
                    for (ColumnConfigs column : columns) {
                        if (column != null && column.getCompanyCustomFieldID() == null && aliasName.equals(column.getCode())) {
                            aliasNameExist = true;
                            break;
                        }
                    }
                }
            }
        } else if (!aliasNameExist) {
            String formId = switch (type) {
                case "Opportunity" -> LayoutRPC.OPPORTUNITY_FORM;
                case "RequestForQuote" -> LayoutRPC.REQUEST_FOR_QUOTE_FORM;
                case "SaleQuote" -> LayoutRPC.SALEQUOTE_FORM;
                case "CrmCase" -> LayoutRPC.CASE_FORM;
                case "SaleOrder" -> "SALEORDER_FORM";
                case "PurchaseOrder" -> LayoutRPC.PURCHASEORDER_FORM;
                default -> null;
            };
            if (formId != null) {
                EdsFormProperty formProperty = this.formPropertyManager.getByFormID(formId);

                if (formProperty != null) {
                    Gson gson = new Gson();
                    FormProperty[] fields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);
                    if (fields != null) {
                        for (FormProperty field : fields) {
                            if (field != null && aliasName.equals(field.getCode())) {
                                aliasNameExist = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        customField.setAliasNameExists(aliasNameExist);

        return customField;
    }

    /**
     * <h1>... This is method created User Facet Filter ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {20:49 21/06/2011} ...</h3>
     *
     * @param facetFilterRpc
     * @param type
     * @return
     */
    private Integer createOrUpdateFacetFilter(FacetFilterRpc facetFilterRpc, ListPanelType type) {
        EdsFacetFilter edsFacetFilter = new EdsFacetFilter();
        EdsUserFilter edsUserFilter = new EdsUserFilter();
        if (facetFilterRpc.getObjectID() != null) {
            edsFacetFilter = this.facetFilterManager.get(facetFilterRpc.getObjectID());
            edsUserFilter = this.userFilterManager.getByFacetFilterId(facetFilterRpc.getObjectID());
        } else {
            edsFacetFilter.setType(type.toString());
            this.facetFilterManager.create(edsFacetFilter);

            edsUserFilter.setFilter(edsFacetFilter);
            if (this.userManager.getUser() != null) {
                edsUserFilter.setUser(this.userManager.get(this.userManager.getUser().getObjectID()));
            }
            this.userFilterManager.create(edsUserFilter);
        }
        EdsUserFilter newEdsUserFilter = new EdsUserFilter();
        if (edsUserFilter == null || edsUserFilter.getUser() == null || !edsUserFilter.getUser().equals(this.userManager.getUser())) {
            newEdsUserFilter.setFilter(edsFacetFilter);
            if (this.userManager.getUser() != null) {
                newEdsUserFilter.setUser(this.userManager.get(this.userManager.getUser().getObjectID()));
            }
            newEdsUserFilter.setFavour(facetFilterRpc.isFavourFilter());
            this.userFilterManager.create(newEdsUserFilter);
            if (edsUserFilter == null) {
                edsUserFilter = newEdsUserFilter;
            }
        }
        if (ListPanelType.LeadListPanelOTF.equals(facetFilterRpc.getType()) || ListPanelType.OpportunityListPanelOTF.equals(facetFilterRpc.getType())
                || ListPanelType.TaskListPanelOTF.equals(facetFilterRpc.getType()) || ListPanelType.CrmAccountListPanelOTF.equals(facetFilterRpc.getType())
                || ListPanelType.ContactListPanelOTF.equals(facetFilterRpc.getType())) {
            edsUserFilter.setFavour(facetFilterRpc.isFavourFilter());
        }

        if (facetFilterRpc.isDefaultFilter() && !edsUserFilter.getIsDefault()) {
            EdsFacetFilter edsDefaultFacetFilter = this.facetFilterManager.getDefaultUserFacetFilter(type, null);
            if (edsDefaultFacetFilter != null && !edsDefaultFacetFilter.equals(edsFacetFilter)) {
                edsUserFilter.setIsDefault(true);
                EdsUserFilter edsDefaultUserFilter = this.userFilterManager.getByFacetFilterId(edsDefaultFacetFilter.getObjectID());
                if (edsDefaultUserFilter != null) {
                    edsDefaultUserFilter.setIsDefault(false);
                }
            } else {
                edsUserFilter.setIsDefault(true);
            }
        } else {
            edsUserFilter.setIsDefault(facetFilterRpc.isDefaultFilter());
        }
        if (!this.userManager.getUser().getRoleIds().contains(Constants.ADMIN)) {
            if (!edsFacetFilter.isSystemFilter()) {
                edsFacetFilter.setName(facetFilterRpc.getName());
                edsFacetFilter.setFacetFilter(facetFilterRpc);
                edsFacetFilter.setStartDate(facetFilterRpc.getStartDate());
                edsFacetFilter.setEndDate(facetFilterRpc.getEndDate());
                edsFacetFilter.setLess(facetFilterRpc.getLess());
                edsFacetFilter.setMore(facetFilterRpc.getMore());
                edsFacetFilter.setSystemFilter(false);
                edsFacetFilter.setDateTerm(DateTermsEnum.getById(facetFilterRpc.getDateTermId()));
            }
        } else {
            if (facetFilterRpc.isSystemFilter() && facetFilterRpc.isDefaultFilter()) {
                List<EdsFacetFilter> defaultFilters = facetFilterManager.getDefaultFacetFilters(facetFilterRpc.getType());
                for (EdsFacetFilter filter : defaultFilters) {
                    filter.getUserFilters().forEach(f -> f.setIsDefault(false));
                }
            }
            edsFacetFilter.setName(facetFilterRpc.getName());
            edsFacetFilter.setFacetFilter(facetFilterRpc);
            edsFacetFilter.setStartDate(facetFilterRpc.getStartDate());
            edsFacetFilter.setEndDate(facetFilterRpc.getEndDate());
            edsFacetFilter.setLess(facetFilterRpc.getLess());
            edsFacetFilter.setMore(facetFilterRpc.getMore());
            edsFacetFilter.setSystemFilter(facetFilterRpc.isPublicFilter());
            edsFacetFilter.setDateTerm(DateTermsEnum.getById(facetFilterRpc.getDateTermId()));
        }
        return edsFacetFilter.getObjectID();
    }

    /**
     * <h1>... This is method created System Facet Filter ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {20:49 21/06/2011} ...</h3>
     *
     * @param facetFilterRpc
     * @param type
     * @return
     */
    private Integer saveSystemFacetFilter(FacetFilterRpc facetFilterRpc, ListPanelType type) {
        EdsFacetFilter edsFacetFilter;
        if (facetFilterRpc.getObjectID() == null) {
            edsFacetFilter = new EdsFacetFilter();
            edsFacetFilter.setType(type.toString());
            edsFacetFilter.setSystemFilter(true);
            this.facetFilterManager.create(edsFacetFilter);
        } else {
            edsFacetFilter = this.facetFilterManager.get(facetFilterRpc.getObjectID());
        }
        edsFacetFilter.setFacetFilter(facetFilterRpc);
        return edsFacetFilter.getObjectID();
    }

    /**
     * <h1>... This is method get user saved facet filter data ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {16:58 1/06/2011} ...</h3>
     *
     * @param facetFilter
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getUserFacetFilter(FacetFilterRpc facetFilter) {
        EdsFacetFilter edsFacetFilter;
        EdsUser user = facetFilter.getUserID() != null ? this.userManager.get(facetFilter.getUserID()) : null;
        if (facetFilter.getObjectID() != null) {
            edsFacetFilter = this.facetFilterManager.getFacetFilter(facetFilter.getObjectID());
        } else {
            edsFacetFilter = this.facetFilterManager.getDefaultUserFacetFilter(facetFilter.getType(), user);
            if (edsFacetFilter == null) {
                edsFacetFilter = this.facetFilterManager.getDefaultUserFacetFilter(facetFilter.getType());
            }
        }
        HashMap<String, String> customData = facetFilter.getCustomData();
        ArrayList<String> showFacetCodeName = facetFilter.getShowFacetCodeName();
        HashMap<String, FacetSolrField> showSolrFieldMap = facetFilter.getShowSolrFieldMap();
        HashMap<String, FacetSolrField> hideSolrFieldMap = facetFilter.getHideSolrFieldMap();

        if (edsFacetFilter != null) {
            Set<String> keSet = new LinkedHashSet<>(facetFilter.getShowSolrFieldMap().keySet());
            keSet.addAll(facetFilter.getHideSolrFieldMap().keySet());
            facetFilter = edsFacetFilter.getFacetFilter(keSet);
            facetFilter.setType(ListPanelType.valueOf(edsFacetFilter.getType()));
        }
        facetFilter.setShowSolrFieldMap(showSolrFieldMap);
        facetFilter.setHideSolrFieldMap(hideSolrFieldMap);

        if (!facetFilter.isBuildShowFacetContent()) {
            facetFilter.setShowFacetCodeName(showFacetCodeName);
        }
        if (customData != null && customData.size() > 0) {
            facetFilter.setCustomData(customData);
        }
        return facetFilter;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FolderResource getFolderResource(int folderType, Integer entityId) {
        return this.documentsServiceLocal.getFolderResource(folderType, entityId);
    }

    @Override
    public void deleteFolder(Integer folderId) throws InsufficientPermissionsException, ObjectNotFoundException {
        this.documentsServiceLocal.deleteFolder(folderId);
    }

    @Override
    public String validateAddressByUSPS(Address address) {

        String stateCode = "";
        if (address.getStateId() != null) {
            EdsRegion state = this.regionManager.get(address.getStateId());
            String[] stateAlias = state.getAlias().split(";");
            if (stateAlias.length >= 1 && stateAlias[1] != null) {
                stateCode = stateAlias[1];
            }
        }

        USPSAddress uspsAddress = new USPSAddress(false);
        uspsAddress.setAddress1(address.getAddress() != null ? address.getAddress() : "");
        uspsAddress.setAddress2(address.getAddressb() != null ? address.getAddressb() : "");
        uspsAddress.setCity(address.getCity() != null ? address.getCity() : "");
        uspsAddress.setState(stateCode);
        uspsAddress.setZip(address.getZipCode() != null ? address.getZipCode() : "");
        uspsAddress.setZipPlus4("");

        USPSWebService uspsWebService = new USPSWebService("Verify",
                this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.USPS_IS_TEST_SERVER),
                this.genericSettingsManager.getValueByKey(GenericSettingsEnum.USPS_USER_ID),
                this.genericSettingsManager.getValueByKey(GenericSettingsEnum.USPS_USER_PASSWORD));

        String responseXML = uspsWebService.submitRequestAndGetResponse(uspsAddress.toXML());
        if (responseXML.contains("<Error>")) {
            int idx1 = responseXML.indexOf("<Description>") + 13;
            int idx2 = responseXML.indexOf("</Description>");
            return responseXML.substring(idx1, idx2);
        }

        return "SUCCESS";
    }

    @Override
    public String saveCrmAccountLogoUrl(Integer imageID, Integer objectId) {
        EdsCrmAccount crmAccount = this.crmAccountManager.get(objectId);
        EdsUpload upload = (EdsUpload) this.uploadManager.get(imageID);

        String url = uploadManager.getFileURL(upload);

        crmAccount.setLogo((EdsAttachment) upload);
        return url;
    }

    public ProjectCalendar createProjectCalendar(ProjectFile file, EdsTimeSlot defaultTimeSlot, String calendarName) {
        ProjectCalendar pc = file.addDefaultBaseCalendar();
        pc.setName(calendarName);
        pc.setWorkingDay(Day.SUNDAY, false);
        pc.setWorkingDay(Day.MONDAY, true);
        pc.setWorkingDay(Day.TUESDAY, true);
        pc.setWorkingDay(Day.WEDNESDAY, true);
        pc.setWorkingDay(Day.THURSDAY, true);
        pc.setWorkingDay(Day.FRIDAY, true);
        pc.setWorkingDay(Day.SATURDAY, false);

        for (EdsTimeSlotItem timeSlotItem : defaultTimeSlot.getItems()) {
            int startHour = timeSlotItem.getStartTime() / 60;
            int startMinute = timeSlotItem.getStartTime() % 60;
            int endHour = timeSlotItem.getEndTime() / 60;
            int endMinute = timeSlotItem.getEndTime() % 60;
            switch (timeSlotItem.getDay()) {
                case 0 -> {
                    if (startHour > 0 && endHour > 0) {
                        ProjectCalendarHours hours = pc.addCalendarHours(Day.SUNDAY);
                        hours.addRange(new DateRange(DateUtility.getTime(startHour, startMinute), DateUtility.getTime(endHour, endMinute)));
                    }
                    pc.setWorkingDay(Day.SUNDAY, true);
                }
                case 1 -> {
                    ProjectCalendarHours hours = pc.addCalendarHours(Day.MONDAY);
                    hours.addRange(new DateRange(DateUtility.getTime(startHour, startMinute), DateUtility.getTime(endHour, endMinute)));
                }
                case 2 -> {
                    ProjectCalendarHours hours = pc.addCalendarHours(Day.TUESDAY);
                    hours.addRange(new DateRange(DateUtility.getTime(startHour, startMinute), DateUtility.getTime(endHour, endMinute)));
                }
                case 3 -> {
                    ProjectCalendarHours hours = pc.addCalendarHours(Day.WEDNESDAY);
                    hours.addRange(new DateRange(DateUtility.getTime(startHour, startMinute), DateUtility.getTime(endHour, endMinute)));
                }
                case 4 -> {
                    ProjectCalendarHours hours = pc.addCalendarHours(Day.THURSDAY);
                    hours.addRange(new DateRange(DateUtility.getTime(startHour, startMinute), DateUtility.getTime(endHour, endMinute)));
                }
                case 5 -> {
                    ProjectCalendarHours hours = pc.addCalendarHours(Day.FRIDAY);
                    hours.addRange(new DateRange(DateUtility.getTime(startHour, startMinute), DateUtility.getTime(endHour, endMinute)));
                }
                case 6 -> {
                    if (startHour > 0 && endHour > 0) {
                        ProjectCalendarHours hours = pc.addCalendarHours(Day.SATURDAY);
                        hours.addRange(new DateRange(DateUtility.getTime(startHour, startMinute), DateUtility.getTime(endHour, endMinute)));
                    }
                    pc.setWorkingDay(Day.SATURDAY, true);
                }
            }
        }
        return pc;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimeSlot getCurrentEmployeeTimeSlot() {
        EdsUser currentUser = this.userManager.getUser();
        return this.getEmployeeTimeSlot(currentUser.getObjectID());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimeSlot getEmployeeTimeSlot(Integer employeeId) {
        return this.getEmployeeTimeSlot(employeeId, null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimeSlot getEmployeeTimeSlot(Integer employeeId, Date date) {
        EdsUser currentUser;
        if (employeeId != null) {
            currentUser = this.userManager.get(employeeId);
        } else {
            currentUser = this.userManager.getUser();
        }
        TimeSlot item = new TimeSlot();
        if (currentUser.isEmployee()) {
            EdsEmployee employee = currentUser.getEmployee();
            EdsTimeSlot timeSlot;
            EdsTimeSlotItem startOfWeek;
            date = date != null ? date : new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            if (employee.getTimeSlot() != null) {
                timeSlot = employee.getTimeSlot();
            } else {
                timeSlot = employee.getCompany().getDefaultTimeSlot();
            }
            EdsCompanySystemSettings companySystemSettings = this.companySystemSettingsManager.findByCompanyID(currentUser.getCompany().getObjectID());
            startOfWeek = companySystemSettings != null ? this.timeSlotItemManager.getTimeSlotItemByDay(companySystemSettings.getOverallDatePickerWeekStart(), timeSlot.getObjectID()) : null;
            for (EdsTimeSlotItem tsItem : timeSlot.getItems()) {
                if (date.getDay() == tsItem.getDay()) {
                    Integer startTime = tsItem.getStartTime() != 0 ? tsItem.getStartTime() : startOfWeek != null && startOfWeek.getStartTime() != 0 ? startOfWeek.getStartTime() : 570;
                    Integer endTime = tsItem.getEndTime() != 0 ? tsItem.getEndTime() : startOfWeek != null && startOfWeek.getEndTime() != 0 ? startOfWeek.getEndTime() : 1080;
                    String sHour = String.valueOf(startTime / 60);
                    if (sHour.length() == 1) {
                        sHour = "0" + sHour;
                    }
                    String sMin = String.valueOf(startTime % 60);
                    if (sMin.length() == 1) {
                        sMin = "0" + sMin;
                    }
                    String eHour = String.valueOf(endTime / 60);
                    if (eHour.length() == 1) {
                        eHour = "0" + eHour;
                    }
                    String eMin = String.valueOf(endTime % 60);
                    if (eMin.length() == 1) {
                        eMin = "0" + eMin;
                    }
                    item.setStartHour(sHour);
                    item.setStartMin(sMin);
                    item.setEndHour(eHour);
                    item.setEndMin(eMin);
                    break;
                }
            }
        } else {
            item.setStartHour("00");
            item.setStartMin("00");
            item.setEndHour("00");
            item.setEndMin("00");
        }
        return item;
    }

    @Override
    public void saveCompanyParent(Integer companyId, Integer parentCompanyId) {
        EdsCompany edsCompany = this.companyManager.get(companyId);
        edsCompany.setParentCompanyId(parentCompanyId);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public String getCompaniesClusterType(Integer companyId) {
        return this.globalAuthJdbcSpringManager.getCompanyClusterType(companyId);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Map<Integer, String> getCompaniesClusterType(String companyIds) {
        return this.globalAuthJdbcSpringManager.getCompaniesClusterType(companyIds);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Map<Integer, String> getSubsidiariesCompanyClusterType(Integer parentId) {
        return this.globalAuthJdbcSpringManager.getSubsidiariesCompanyClusterType(parentId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getBonusRecommendationApprovers(Integer employeeID) {
        EdsEmployee empl;
        EdsUser user = this.employeeManager.getUser();
        if (employeeID != null) {
            empl = this.employeeManager.get(employeeID);
        } else {
            empl = user.getEmployee();
        }
        //Bonus Recommendation approvers role codes
        List<String> roleCodes = this.rolePermissionManager.getRolesByPermissionCode(PermissionConstants.HRMS_EMPLOYEE_PREMIUM_RECOMMENDATIONS_APPROVERS);
        if (roleCodes.isEmpty()) {
            roleCodes.add(EdsRole.TL_CODE); //Department Leader
        }
        List<EdsEmployee> approvers = this.employeeManager.getApprovers(empl, roleCodes);

        Integer departmentLeaderID = null;
        if (empl.getEmployeeDepartment() != null && empl.getEmployeeDepartment().getTeam() != null && empl.getEmployeeDepartment().getTeam().getLeader() != null) {
            departmentLeaderID = empl.getEmployeeDepartment().getTeam().getLeader().getObjectID();
        }
        SelectItem[] result = new SelectItem[approvers.size()];
        int i = 0;
        for (EdsEmployee employee : approvers) {
            result[i] = new SelectItem(employee.getObjectID(), employee.getName());
            if (employee.getObjectID().equals(departmentLeaderID)) {
                result[i].setSelected(true);
            }
            i++;
        }
        return result;
    }

    public Boolean removeLocalizationItem(Integer id) {
        Boolean t = false;
        if (id != null) {
            EdsLocalization item = this.localizationManager.get(id);
            if (item != null) {
                item.setActive(false);
                this.localizationManager.update(item);
                t = true;
            }
        }
        return t;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public LinkedHashMap<String, LinkedHashMap<String, SelectItem>> getReportingTopMenu() {
        LinkedHashMap<String, LinkedHashMap<String, SelectItem>> map = this.reportingService.getReportingTopMenu();
        LinkedHashMap<String, LinkedHashMap<String, SelectItem>> repotingMenus = new LinkedHashMap<>();

        String menuName, localizeMenuName, splitMenuName;
        for (Map.Entry<String, LinkedHashMap<String, SelectItem>> item : map.entrySet()) {
            menuName = (String) item.getKey();
            splitMenuName = menuName.replace(" ", "").toUpperCase();
            localizeMenuName = this.commonLocalizer.localize(splitMenuName, menuName);
            repotingMenus.put(localizeMenuName, (LinkedHashMap<String, SelectItem>) item.getValue());
        }

        return repotingMenus;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ArrayList<CompanyCustomFieldItem> getCompanyStepCategoryCustomFields(Integer stepID) {
        ViewName viewName = ViewName.OnboardingStep;
        List<EdsCompanyCustomFieldsSettings> companyCFs = this.getEdsCompanyCustomFieldsSettingses(stepID, viewName.name());
        if (companyCFs != null) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, viewName, false);
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<EdsCompanyCustomFieldsSettings> getEdsCompanyCustomFieldsSettingses(Integer stepID, String viewName) {
        List<EdsCompanyCustomFieldsSettings> companyCFs = null;
        EdsOnboardingStep step;
        if (stepID != null) {
            step = this.onboardingStepManager.get(stepID);
            companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsWithCategory(viewName, step.getViewName());
        }
        return companyCFs;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ArrayList<SelectItem> getOnboardingStepdList(ListingFilterParameter fp) {
        List<EdsOnboardingStep> onboardingStepList = this.onboardingStepManager.getOnboardingStepList(fp);
        ArrayList<SelectItem> onboardingItems = new ArrayList<>();
        for (EdsOnboardingStep onboardingStep : onboardingStepList) {
            SelectItem selectItem = new SelectItem();
            selectItem.setName(onboardingStep.getName());
            selectItem.setDescription(onboardingStep.getViewName());
            onboardingItems.add(selectItem);
        }
        return onboardingItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void updateCompanyTimezones() {
        List<String> existingSchemas = this.companyManager.getExistingSchemas();
        for (String companyID : existingSchemas) {
            EdsCompany company = this.companyManager.get(Integer.valueOf(companyID));
            try {
                if (company != null) {
                    ServerSecurityContext.getInstance().setCompanyId(companyID);
                    if (company.hasSchema(existingSchemas)) {
                        List<EdsCountryZone> countryZones = new ArrayList<>();
                        if (company.getCountryZone() != null && company.getCountryZone().getObjectID() < 1000) {
                            countryZones = this.timeZoneManager.getCountryZones(company.getCountryZone().getCountry());
                            if (!countryZones.isEmpty()) {
                                for (EdsCountryZone countryZone : countryZones) {
                                    if (countryZone.getObjectID() >= 1000) {
                                        company.setCountryZone(countryZone);
                                        break;
                                    }
                                }
                            }
                        } else if (company.getCountryZone() == null) {
                            if (company.getCountryRegion() != null) {
                                countryZones = this.timeZoneManager.getCountryZones(company.getCountryRegion().getCountry());
                                if (!countryZones.isEmpty()) {
                                    for (EdsCountryZone countryZone : countryZones) {
                                        if (countryZone.getObjectID() >= 1000) {
                                            company.setCountryZone(countryZone);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        List<EdsUser> companyUsers = this.userManager.getCompanyUsers(company);
                        if (companyUsers != null) {
                            for (EdsUser user : companyUsers) {
                                if (user.getCountryZone() == null || user.getCountryZone() != null && user.getCountryZone().getObjectID() < 1000) {
                                    if (countryZones.isEmpty()) {
                                        countryZones = this.timeZoneManager.getCountryZones(company.getCountryZone().getCountry());
                                    }
                                    if (!countryZones.isEmpty()) {
                                        for (EdsCountryZone countryZone : countryZones) {
                                            if (countryZone.getObjectID() >= 1000) {
                                                user.setCountryZone(countryZone);
                                                this.userManager.update(user);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    this.userManager.flushAndClear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Companies timezonees successfully fixed");
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getPositionEmployees(Integer positionID) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> employeeList = new LinkedHashMap<>();
        List<EdsEmployee> employees;
        KpiTreeInfo key;
        boolean team;
        employees = this.employeeManager.getCompanyEmployees();
        ArrayList<Integer> employeePosition = null;
        if (positionID != null) {
            employeePosition = this.employeeManager.getEmployeePosition(positionID);
        }
        for (EdsEmployee employee : employees) {
            team = false;
            key = new KpiTreeInfo();
            key.setId(employee.getObjectID() != null ? employee.getObjectID() : 0);
            key.setEmployeeId(employee.getObjectID() != null ? employee.getObjectID() : 0);
            key.setName(employee.getName() != null ? employee.getName() : "");
            if (employee.getEmployeeDepartment() != null && employee.getTeam() != null && employee.getEmployeeDepartment().getObjectID() != null) {
                key.setDepartmentId(employee.getTeam().getObjectID());
                key.setDepartmentName(employee.getTeam().getName() != null ? employee.getTeam().getName() : "");
                key.setSelected(employeePosition != null && employeePosition.contains(employee.getObjectID()));

                for (KpiTreeInfo s : employeeList.keySet()) {
                    if (s.getId().equals(employee.getTeam().getObjectID())) {
                        team = true;
                        employeeList.get(s).add(key);
                        break;
                    }
                }

                if (!team) {
                    KpiTreeInfo department = new KpiTreeInfo(employee.getTeam().getObjectID(), employee.getTeam().getName());
                    ArrayList<KpiTreeInfo> list = new ArrayList<KpiTreeInfo>();
                    list.add(key);
                    employeeList.put(department, list);
                }
            }
        }
        return employeeList;
    }

    @Override
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getPositionDepartments(SelectItem position) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> departmentList = new LinkedHashMap<>();
        List<EdsDepartment> departments;
        KpiTreeInfo key;
        boolean team;
        departments = departmentManager.getCompanyDepartments(userManager.getUser().getCompany());
        for (EdsDepartment department : departments) {
            team = false;
            key = new KpiTreeInfo();
            key.setId(department.getObjectID() != null ? department.getObjectID() : 0);
            key.setEmployeeId(department.getObjectID() != null ? department.getObjectID() : 0);
            key.setName(department.getNumberData() != null ? department.getNumberData() + "->" + department.getName() : department.getName());
            if (department.getLocation() != null && department.getLocation().getObjectID() != null) {
                key.setDepartmentId(department.getLocation().getObjectID());
                key.setDepartmentName(department.getLocation().getCode() != null ? department.getLocation().getCode() + "->" + department.getLocation().getName() : department.getLocation().getName());
                key.setSelected(false);
                for (KpiTreeInfo s : departmentList.keySet()) {
                    if (s.getId().equals(department.getLocation().getObjectID())) {
                        team = true;
                        departmentList.get(s).add(key);
                        break;
                    }
                }
                if (!team) {
                    KpiTreeInfo departmentKey = new KpiTreeInfo(department.getLocation().getObjectID(), department.getLocation().getCode() != null ? department.getLocation().getCode() + "->" + department.getLocation().getName() : department.getLocation().getName());
                    ArrayList list = new ArrayList<KpiTreeInfo>();
                    list.add(key);
                    departmentList.put(departmentKey, list);
                }
            }
        }
        return departmentList;
    }

    @Override
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getPositionsForKpiTree(SelectItem positionItem) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> positionList = new LinkedHashMap<>();
        List<EdsPosition> positions;
        KpiTreeInfo key;
        boolean team;
        positions = positionManager.getPositionListByName(positionItem.getName());
        for (EdsPosition position : positions) {
            team = false;
            key = new KpiTreeInfo();
            key.setId(position.getObjectID() != null ? position.getObjectID() : 0);
            key.setEmployeeId(position.getObjectID() != null ? position.getObjectID() : 0);
            key.setName(position.getNumberData() != null ? position.getNumberData() + "->" + position.getName() : position.getName());
            if (position.getDepartmentObject() != null) {
                key.setPositionName(position.getDepartmentObject().getNumberData() != null ? position.getDepartmentObject().getNumberData() + "->" + position.getDepartmentObject().getName() : position.getDepartmentObject().getName());
            }
            if (position.getLocation() != null && position.getLocation().getObjectID() != null) {
                key.setDepartmentId(position.getLocation().getObjectID());
                key.setDepartmentName(position.getLocation().getCode() != null ? position.getLocation().getCode() + "->" + position.getLocation().getName() : position.getLocation().getName());
                key.setSelected(false);
                for (KpiTreeInfo s : positionList.keySet()) {
                    if (s.getId().equals(position.getLocation().getObjectID())) {
                        team = true;
                        positionList.get(s).add(key);
                        break;
                    }
                }
                if (!team) {
                    KpiTreeInfo departmentKey = new KpiTreeInfo(position.getLocation().getObjectID(), position.getLocation().getCode() != null ? position.getLocation().getCode() + "->" + position.getLocation().getName() : position.getLocation().getName());
                    ArrayList list = new ArrayList<KpiTreeInfo>();
                    list.add(key);
                    positionList.put(departmentKey, list);
                }
            }
        }
        return positionList;
    }

    @Override
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getLocationsForKpiTree() {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> locationList = new LinkedHashMap<>();
        List<EdsLocation> locations;
        KpiTreeInfo key;
        boolean team;
        locations = locationManager.getLocations(new ListingFilterParameter());
        ArrayList<Integer> employeePosition = null;
        for (EdsLocation department : locations) {
            team = false;
            key = new KpiTreeInfo();
            key.setId(department.getObjectID() != null ? department.getObjectID() : 0);
            key.setEmployeeId(department.getObjectID() != null ? department.getObjectID() : 0);
            key.setName(department.getCode() != null ? department.getCode() + "->" + department.getName() : department.getName());
            if (department.getParent() != null && department.getParent().getObjectID() != null) {
                key.setDepartmentId(department.getParent().getObjectID());
                key.setDepartmentName(department.getParent().getCode() != null ? department.getParent().getCode() + "->" + department.getParent().getName() : department.getParent().getName());
                key.setSelected(employeePosition != null && employeePosition.contains(department.getObjectID()));

                for (KpiTreeInfo s : locationList.keySet()) {
                    if (s.getId().equals(department.getParent().getObjectID())) {
                        team = true;
                        locationList.get(s).add(key);
                        break;
                    }
                }

                if (!team) {
                    KpiTreeInfo departmentKey = new KpiTreeInfo(department.getParent().getObjectID(), department.getParent().getCode() != null ? department.getParent().getCode() + "->" + department.getParent().getName() : department.getParent().getName());
                    ArrayList list = new ArrayList<KpiTreeInfo>();
                    list.add(key);
                    locationList.put(departmentKey, list);
                }
            }
        }
        return locationList;
    }

    @Override
    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getDepartmentsForKpiTree(SelectItem deparment) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> departmentList = new LinkedHashMap<>();
        List<EdsDepartment> departments;
        KpiTreeInfo key;
        boolean team;
        departments = departmentManager.getDepartmentListByName(deparment.getName());
        ArrayList<Integer> employeePosition = null;
        for (EdsDepartment department : departments) {
            team = false;
            key = new KpiTreeInfo();
            key.setId(department.getObjectID() != null ? department.getObjectID() : 0);
            key.setEmployeeId(department.getObjectID() != null ? department.getObjectID() : 0);
            key.setName(department.getNumberData() != null ? department.getNumberData() + "->" + department.getName() : department.getName());
            if (department.getLocation() != null && department.getLocation().getObjectID() != null) {
                key.setDepartmentId(department.getLocation().getObjectID());
                key.setDepartmentName(department.getLocation().getCode() != null ? department.getLocation().getCode() + "->" + department.getLocation().getName() : department.getLocation().getName());
                key.setSelected(employeePosition != null && employeePosition.contains(department.getObjectID()));

                for (KpiTreeInfo s : departmentList.keySet()) {
                    if (s.getId().equals(department.getLocation().getObjectID())) {
                        team = true;
                        departmentList.get(s).add(key);
                        break;
                    }
                }

                if (!team) {
                    KpiTreeInfo departmentKey = new KpiTreeInfo(department.getLocation().getObjectID(), department.getLocation().getCode() != null ? department.getLocation().getCode() + "->" + department.getLocation().getName() : department.getLocation().getName());
                    ArrayList list = new ArrayList<KpiTreeInfo>();
                    list.add(key);
                    departmentList.put(departmentKey, list);
                }
            }
        }
        return departmentList;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public String getTotalCharge(ProjectPosition po) {
        String formula = this.contractManager.getTotaLChargeFormula(po.getPriceTypeString());
        String numberOfWorker = "0";
        String unitPrice = "0";
        String unitQTY = "0";
        if (po.getNumberOfWorker() != null) {
            numberOfWorker = po.getNumberOfWorker().toString();
        }
        if (po.getUnitPrice() != null) {
            unitPrice = po.getUnitPrice().toString();
        }
        if (po.getUnitQTY() != null) {
            unitQTY = po.getUnitQTY().toString();
        }
        String totalcharceString = formula.replace("_numberofworks_", numberOfWorker).replace("_unitprice_", unitPrice).replace("_unitqty_", unitQTY);
        return this.contractManager.getTotaLCharge(totalcharceString);

    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public String getCalculateUnitPrice(ProjectPosition po) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStartDate(po.getContractStart() != null ? po.getContractStart().getNonConvertedDate() : null);
        fp.setEndDate(po.getContractEnd() != null ? po.getContractEnd().getNonConvertedDate() : null);
        Integer availableDays = this.attendanceRawDataManager.getWorkingDays(fp).size();
        List<EdsTimeSlot> list = this.timeSlotManager.getTimeslots(new ListingFilterParameter());
        Integer totalUnitPrice;
        Integer workingDayhours = 0;
        for (EdsTimeSlot timeslot : list) {
            if (timeslot.getObjectID().equals(1)) {
                workingDayhours = ServerUtils.getDailyAverageTimeslotMinutes(timeslot.getItems()) / 60;
                break;
            }
        }
        totalUnitPrice = availableDays * po.getNumberOfWorker();
        if (po.getPriceType().equals(0)) {
            totalUnitPrice = totalUnitPrice * workingDayhours;
        }
        return totalUnitPrice.toString();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public AttendanceReportLogItem getEmployeePresentTime(String empID, String dateItem) {
        AttendanceReportLogItem item = new AttendanceReportLogItem();
        EdsUser user = this.userManager.getUser();

        String companyDateFormatStr = user.getCompany().getCompanySettings().getShortDateFormat();
        String dateFormatStr = companyDateFormatStr != null && !companyDateFormatStr.isEmpty() ? companyDateFormatStr : "dd/MM/yyyy";
        SimpleDateFormat shortFormat = new SimpleDateFormat("dd.MM.yyyy");

        Integer employeeID = Integer.valueOf(empID);
        System.out.println(employeeID);
        SimpleDateFormat dateFormatTime = new SimpleDateFormat("dd/MM/yyyy");
        List<StaffInOut> result;

        Date date = null;
        String shortDate = dateItem;
        try {
            date = dateFormatTime.parse(dateItem);
            shortDate = shortFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String res = "";
        Calendar cal = Calendar.getInstance();
        boolean fingerprintEnabled = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FINGERPRINT_DEVICE_ENABLED);
        boolean fingerprintIsCustom = this.genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FOR_CUSTOM_FINGER_PRINT);
        if (fingerprintEnabled) {
            result = this.fingerPrintManager.getEmployeeTimeTrackInPeriod(employeeID, date, date, fingerprintIsCustom);
            if (result != null && result.size() > 0) {
                if (result.get(0).getStartDate() != null) {
                    cal.setTime(result.get(0).getStartDate());
                    res = res + cal.get(Calendar.HOUR_OF_DAY) + ":" + ((cal.get(Calendar.MINUTE) < 10) ? "0" + cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE));
                }
                res += ";";
                if (result.get(0).getEndDate() != null) {
                    cal.setTime(result.get(0).getEndDate());
                    res = res + cal.get(Calendar.HOUR_OF_DAY) + ":" + ((cal.get(Calendar.MINUTE) < 10) ? "0" + cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE));
                }
                item.setTimeslotId(result.get(0).getTimeSlotId());
            }
        } else {
            result = this.timeTrackManager.getEmployeeTimeTrackInPeriod(employeeID, date, date);
            if (result != null && result.size() > 0) {
                if (result.get(0).getStartDate() != null) {
                    cal.setTime(user.getUserDate(result.get(0).getStartDate()));
                    res = res + cal.get(Calendar.HOUR_OF_DAY) + ":" + ((cal.get(Calendar.MINUTE) < 10) ? "0" + cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE));
                }
                res += ";";
                if (result.get(0).getEndDate() != null) {
                    cal.setTime(user.getUserDate(result.get(0).getEndDate()));
                    res = res + cal.get(Calendar.HOUR_OF_DAY) + ":" + ((cal.get(Calendar.MINUTE) < 10) ? "0" + cal.get(Calendar.MINUTE) : cal.get(Calendar.MINUTE));
                }
                item.setTimeslotId(result.get(0).getTimeSlotId());
            }
        }
        item.setText(res);
        item.setDate(shortDate);
        return item;
    }

    @Override
    public Integer saveAttendanceHour(EmployeePresentItem item) {
        Integer employeeID = item.getEmployeeId();
        System.out.println("employeeID = " + employeeID);

        boolean timeAdded = false;
        if (item.getFrom() != null && item.getTo() != null) {
            Date from, to;
            if (item.getDateItem().getDate().after(item.getTo().getDate())) return Constants.ERROR;
            timeAdded = true;
            from = item.getFrom().getNonConvertedDate();
            to = item.getTo().getNonConvertedDate();
            EdsShift shift = item.getShiftId() != null ? shiftManager.get(item.getShiftId()) : null;
            if (shift == null || shift.getLookupType() == null || !shift.getLookupType().equals(OVERTIME)) {
                attendanceHoursManager.deleteEqualsStartDate(employeeID, from, AttendanceHoursType.MANUAL_OR_SHIFT);
            }
            attendanceHoursManager.insertEmployeeHours(employeeID, from, to, shift, item.getTimeSlotId());
        }


        EdsReference reference;
        if (item.getReasonID() != null) { //Adding Leave Request
            Date date = item.getDateItem().getNonConvertedDate();
            if (date != null) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(date);

                reference = this.referenceManager.get(item.getReasonID());

                EdsAttendanceRawData rawData = this.attendanceRawDataManager.getAttendanceRawDataByDate(date, employeeID);
                if (rawData != null && rawData.getTimeSlot() < 1) {
                    SelectItem[] reasons = this.leaveReasonManager.getAttendanceLRReasons(false);
                    for (SelectItem r : reasons) {
                        if (r.getId().equals(item.getReasonID())) {
                            return Constants.INFO;
                        }
                    }
                }

                TimeSlot timeSlot = this.getEmployeeTimeSlot(employeeID, date);
                NewLeaveRequest leaveRequest = new NewLeaveRequest();
                leaveRequest.setEmployee(employeeID);
                leaveRequest.setFrom(GenericSettingsEnum.ATTENDANCE_REPORT_BETA.name());
                leaveRequest.setReasonId(item.getReasonID());
                leaveRequest.setDescription("From attendance report");


                leaveRequest.setStartHour(Integer.parseInt(timeSlot.getStartHour()));
                leaveRequest.setStartMinut(Integer.parseInt(timeSlot.getStartMin()));
                leaveRequest.setEndHour(Integer.parseInt(timeSlot.getEndHour()));
                leaveRequest.setEndMinut(Integer.parseInt(timeSlot.getEndMin()));

                String startDString = String.valueOf(date.getTime());
                String endDString = String.valueOf(date.getTime());
                Long startD = this.getTimeAsLongFromString(startDString, timeSlot.getStartHour(), timeSlot.getStartMin());
                DateNonConvertable start = new DateNonConvertable(new Date(startD));
                Long endD = this.getTimeAsLongFromString(endDString, timeSlot.getEndHour(), timeSlot.getEndMin());
                DateNonConvertable end = new DateNonConvertable(new Date(endD));

                leaveRequest.setStartNonConverable(item.getFrom() != null ? item.getFrom() : start);
                leaveRequest.setEndNonConverable(item.getTo() != null ? item.getTo() : end);

                String hasAccess = this.availabilityService.hasAccessInsertRequest(employeeID, null, start, end, false);
                if (!Constants.TRUE.equals(hasAccess)) {
                    return Constants.VALIDATION;
                }
                if (start.getDate() != null) {
                    leaveRequest.setDate((String.valueOf(start.getDate().getTime())));
                }
                leaveRequest.setMonth(String.valueOf(calendar.get(Calendar.MONTH)));
                leaveRequest.setDay(String.valueOf(calendar.get(Calendar.DATE)));
                if (end.getDate() != null) {
                    leaveRequest.setDateE((String.valueOf(end.getDate().getTime())));
                }
                leaveRequest.setMonthE(String.valueOf(calendar.get(Calendar.MONTH)));
                leaveRequest.setDayE(String.valueOf(calendar.get(Calendar.DATE)));

                leaveRequest.setType("ST_PAID");
                if (reference != null && EdsSickRequest.LR_TYPE_UNAUTHORIZED_LEAVE.equals(reference.getCode())) {
                    leaveRequest.setType("NON_PAID");
                }
                leaveRequest.setTakeByMoney(false);
                this.availabilityService.createLeaveRequest(leaveRequest);
            }
        } else if (!timeAdded) {
            return Constants.WARNING;
        }
        return Constants.SUCCESS;
    }

    private Long getTimeAsLongFromString(String dateAsStr, String hourAsStr, String minutAsStr) {
        return Long.parseLong(dateAsStr) + (Long.parseLong(hourAsStr) * 60 + Long.parseLong(minutAsStr)) * 60 * 1000;
    }

    /**
     * Timesheet plugun
     *
     * @param employeeID
     * @param startTime
     * @param endTime
     * @param availableStatusID
     * @param fingerprintEnabled
     * @param isCustomFingerPrint
     */
    @Override
    public void saveEmployeePresentTimeFromAPI(Integer employeeID, Date startTime, Date endTime, Integer availableStatusID, boolean fingerprintEnabled, boolean isCustomFingerPrint) {
        List<StaffInOut> result = this.timeTrackManager.getEmployeeTimeTrackInPeriod(employeeID, startTime, endTime);

        if (result != null && result.size() > 0) {
            if (fingerprintEnabled) {
                EdsUsersFingerPrint edsTimeTrack = (EdsUsersFingerPrint) result.get(0);
                edsTimeTrack.setStartDate(startTime);
                edsTimeTrack.setEndDate(endTime);
                this.fingerPrintManager.createOrUpdate(edsTimeTrack);
            } else {
                EdsTimeTrack edsTimeTrack = (EdsTimeTrack) result.get(0);
                edsTimeTrack.setStartDate(startTime);
                edsTimeTrack.setEndDate(endTime);
                this.timeTrackManager.createOrUpdate(edsTimeTrack);
            }
        } else {
            if (fingerprintEnabled) {
                EdsUserFingerPrintDevice userFingerPrintDevice = this.userFingerPrintDeviceManager.getUserFingerPrintdByUserIdAndDeviceId(employeeID, null);
                this.fingerPrintManager.insertTimeTrack(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), employeeID, userFingerPrintDevice, startTime, endTime, availableStatusID, isCustomFingerPrint);
            } else {
                this.timeTrackManager.insertTimeTrack(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()), employeeID, startTime, endTime, availableStatusID, false);
            }
        }
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<ConsolidationCompanyList> getConsolidationCompanyList(Map<Integer, ConsolidationCompanyList> companyCosolidationMap, String ids, String dbName) {
        List<EdsCompany> edsCompanyList = this.companyManager.getCompaniesByIDs(ids);
        List<ConsolidationCompanyList> consolidationCompanyLists = new ArrayList<>();
        for (EdsCompany company : edsCompanyList) {
            ConsolidationCompanyList consolidationCompany = companyCosolidationMap.get(company.getObjectID());
            consolidationCompany.setDataBaseName(dbName);
            consolidationCompany.setCompanyId(company.getObjectID());
            consolidationCompany.setCompanyName(company.getName());
            consolidationCompany.setCountry(company.getCountryZone() != null ? company.getCountryZone().getCountry().getName() : "");
            EdsFinancialSettings financialSettings = this.financialSettingsManager.getSettingsByCompany(company.getObjectID());
            consolidationCompany.setBaseCurrency(financialSettings != null ? financialSettings.getCurrency().getName() : null);
            consolidationCompany.setStatus(company.getActive() != null ? company.getActive() : false);
            consolidationCompany.setAdminEmail(company.getEmail());
            consolidationCompanyLists.add(consolidationCompany);

        }
        return consolidationCompanyLists;
    }

    @Override
    @Transactional
    public Integer indexCompanyVacancy(SolrReindexRpc solrReindexRpc, Integer start, int limit) {
        List<EdsVacancy> vacancyList = this.vacancyManager.getVacancyListForSolr(solrReindexRpc, start, limit);
        if (vacancyList.size() == 0) {
            return -1;
        }
        try {
            vacancySolrComponent.indexes(vacancyList);
        } catch (SolrServerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("-----------------------------Indexed " + limit + " Vacancy-----------------------------");
        EdsVacancy edsVacancy = vacancyList.get(vacancyList.size() - 1);
        return edsVacancy.getObjectID();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public ListResult<SelectItem> getCountriesList(ListingFilterParameter fp) {
        fp.setActive(false);
        fp.setNewType(true);
        SelectItem[] result = this.getCountries(fp, true);
        ListingFilterParameter fp2 = new ListingFilterParameter();
        fp2.setActive(false);
        fp2.setSearchKey(fp.getSearchKey());
        Integer resultcount = this.countryManager.list(fp2).size();
        ArrayList<SelectItem> items = new ArrayList<>(result.length);
        for (SelectItem r : result) {
            SelectItem sitem = new SelectItem();
            sitem.setId(r.getId());
            sitem.setName(r.getName());
            sitem.setDescription(r.getDescription());
            sitem.setNewItem(r.isNewItem());
            items.add(sitem);
        }
        return new ListResult<>(items, resultcount);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SelectItem[] getVacancyTypeItems() {
        List<EdsReference> statuses = this.referenceManager.listReferences(EdsVacancy.VACANCY_TYPE);
        return this.reference2SelectItem(statuses, null);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public SelectItem[] getReligionItems() {
        List<EdsReference> statuses = this.referenceManager.listReferences(EdsVacancy.VACANCY_RELIGION);
        return this.reference2SelectItem(statuses, null);
    }

    @Override
    public SelectItem[] getReferenceItems(ListingFilterParameter fp) {
        if (fp.getCategory() != null && ModelField.SOURCE.CRM.CRM_PAYMENT_METHOD.equals(fp.getCategory())) {
            return this.allInOneServiceLocal.getPaymentMethodList();
        } else if (fp.getCategory() != null && ModelField.SOURCE.ACCOUNTING.ACCOUNTING_BANK_ACCOUNT.equals(fp.getCategory())) {
            return this.accountingService.getBankAccountItems();
        } else if (fp.getCategory() != null && CustomFormConstants.ACCOUNTS_RECEIVABLE_PAYABLE.equals(fp.getCategory())) {

            return this.accountingServiceLocal.getAccountsReceivablePayable(fp).toArray(new SelectItem[]{});
        } else if (fp.getCategory() != null && "CRM_ACCOUNT_CURRENCY".equals(fp.getCategory())) {

            ClientCurrency companyCurrency = this.crmServiceLocal.getClientCurrency();
            return companyCurrency != null ? companyCurrency.getItems() : null;
        } else if (fp.getCategory() != null && ModelField.SOURCE.ACCOUNTING.ACCOUNTING_CLIENT_INVOICE_TERM.equals(fp.getCategory())) {

            return this.clientService.getInvoiceTermsForLookUp(fp);
        } else if (fp.getCategory() != null && "CLIENT_VAT".equals(fp.getCategory())) {
            List<EdsVat> taxList = this.accountingServiceLocal.companyVatList(fp, null);
            return this.accountingServiceLocal.createCompanyTaxList(taxList).getTaxItems();
        } else if (fp.getCategory() != null && "PRODUCT_CATEGORY".equals(fp.getCategory())) {
            return this.accountingService.getCategoriesAsSelectItem();
        } else if (fp.getCategory() != null && "PRODUCT_BRAND".equals(fp.getCategory())) {
            return this.accountingService.getBrandsAsSelectItem();
        } else if (fp.getCategory() != null && "PRODUCT_DISCOUNT".equals(fp.getCategory())) {
            return this.discountService.getDiscountListAsSelectItem();
        } else if (fp.getCategory() != null && "PAYROLL_JOB_TITLE".equals(fp.getCategory())) {
            return this.payrollService.getJobTitles();
        } else {
            EdsReference parent = fp.getCategory() != null ? this.referenceManager.getByCode(fp.getCategory()) : null;
            if (parent != null) {
                fp.setParentID(parent.getObjectID());
            }
            fp.setLimit(20);
            List<EdsReference> list = this.referenceManager.listReferences(fp);
            return this.reference2SelectItem(list, null);
        }
    }

    public SelectItem[] getTaxTreatmentItems() {
        return ServerUtils.getAsSelectItem(this.crmServiceLocal.getTaxTreatments(), ServerUtils.REFERENCE);
    }

//    private void saveCountryEmbassyData(ReferenceItem item) {
//        if (item != null) {
//            EdsEmbassy embassy = item.getId() != null ? this.countryManager.getEmbassyById(item.getId()) : new EdsEmbassy();
//            embassy.setName(item.getName());
//            if (embassy.isNew()) {
//                embassy.setCode(item.getName().replaceAll("[^\\p{L}\\p{Nd}]|[\\p{InLatin-1Supplement}]+", "").toUpperCase());
//            }
//            embassy.setDescription(item.getDescription());
//            embassy.setDeleted(false);
//            embassy.setSorder(item.getOrder() == null ? 0 : item.getOrder());
//            embassy.setCountry(this.countryManager.get(item.getParentID()));
//            this.countryManager.createOrUpdateEmbassy(embassy);
//        }
//    }

    @Override
    @Transactional(readOnly = true)
    public Boolean checkProjectBillable(Integer projectId) {
        if (projectId != null) {
            EdsProject edsProject = this.projectManager.get(projectId);
            if (edsProject != null) {
                return edsProject.getBillable();
            }
        }
        return false;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public CompanyAddress getCompanyAddress() {

        Integer companyId = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        List<EdsAddress> billAddresses = this.addressManager.getAddressesByEntityIdAndType(companyId, EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_COMPANY);
        List<EdsAddress> mailAddresses = this.addressManager.getAddressesByEntityIdAndType(companyId, EdsAddress.MAILING_ADDRESS, EdsAddress.ENTITY_TYPE_COMPANY);

        SelectItem[] billAddrItems;
        SelectItem[] mailAddrItems;
        CompanyAddress data = new CompanyAddress();
        int i = 0;
        billAddrItems = new SelectItem[billAddresses.size()];
        for (EdsAddress addr : billAddresses) {
            billAddrItems[i++] = new SelectItem(addr.getObjectID(),
                    ((addr.getName() != null && !"".equals(addr.getName().trim())) ? addr.getName() : "(no name)"), addr.getAddressDataAsHTML(), addr.isPrimary());
        }
        i = 0;
        mailAddrItems = new SelectItem[mailAddresses.size()];
        for (EdsAddress addr : mailAddresses) {
            mailAddrItems[i++] = new SelectItem(addr.getObjectID(),
                    ((addr.getName() != null && !"".equals(addr.getName().trim())) ? addr.getName() : "(no name)"), addr.getAddressDataAsHTML(), addr.isPrimary());
        }
        data.setBillAddresses(billAddrItems);
        data.setMailAddresses(mailAddrItems);
        return data;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public SelectItem getCompanyDefaultCountry() {
        Integer companyId = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        List<EdsAddress> mailAddresses = this.addressManager.getAddressesByEntityIdAndType(companyId, EdsAddress.MAILING_ADDRESS, EdsAddress.ENTITY_TYPE_COMPANY);
        List<EdsAddress> billAddresses = this.addressManager.getAddressesByEntityIdAndType(companyId, EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_COMPANY);

        EdsCountry country = null;
        if (!mailAddresses.isEmpty()) {
            country = mailAddresses.get(0).getCountry();
        } else if (!billAddresses.isEmpty()) {
            country = billAddresses.get(0).getCountry();
        }

        if (country != null) {
            return new SelectItem(country.getObjectID(), country.getName());
        }
        return null;
    }

    @Override
    public CompanyCustomFieldItem getValidCustomFieldItem(Map.Entry<Integer, String> extraColumnEntry, Integer columnId, String columnValue, RejectedImportRecord[] rejectedRow, String fieldName) {

        CompanyCustomFieldItem customField = new CompanyCustomFieldItem();
        String[] extraColumnValues = extraColumnEntry.getValue().split(ImportFile.DELIMITR_BETWEEN_REPRESENTATION_ID);
        customField.setDataType(extraColumnValues[1]);
        customField.setColumnCode(extraColumnValues[2]);
        customField.setUiType(extraColumnValues.length > 4 ? extraColumnValues[4] : null);
        customField.setPredefinedValues(extraColumnValues.length > 5 ? extraColumnValues[5].split("-:-") : null);
        try {
            customField.setCustomFieldSettingID(Integer.parseInt(extraColumnValues[3]));
        } catch (NumberFormatException e) {
            System.out.print(e.getMessage());
        }

        if (CompanyCustomFieldItem.DATE.equals(customField.getDataType())) {
            SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_PATTERN);
            format.setLenient(false);
            try {
                format.parse(columnValue);

            } catch (ParseException e) {
                rejectedRow[columnId].setErrorComment(this.commonLocalizer.localize(PdfLocalizationName.invalidDateFormat));
            }
        } else if (CompanyCustomFieldItem.NUMBER.equals(customField.getDataType())) {
            if (StringUtils.isBlank(fieldName)) {
                fieldName = extraColumnValues[5]; //Number field has no predifined values
            }
            BigDecimal value = parseBigDecimal(columnValue);

            if (value == null) {
                rejectedRow[columnId].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.fieldShouldBeNumberFormat, fieldName));
            }
        }

        if (customField.getUiType() != null) {
            switch (customField.getUiType()) {
                case Constants.UI_TYPE_DROPDOWN, Constants.UI_TYPE_RADIOBUTTON -> {
                    if (customField.getPredefinedValues() != null && customField.getPredefinedValues().length > 0) {
                        List<String> list = Arrays.stream(customField.getPredefinedValues()).map(s -> s.trim().toLowerCase()).toList();

                        if (!list.contains(columnValue.trim().toLowerCase())) {
                            rejectedRow[columnId].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                        }
                    }
                }
                case Constants.UI_TYPE_CHECKBOX -> {
                    if (customField.getPredefinedValues() != null && customField.getPredefinedValues().length > 0) {
                        List<String> chosenValues = Arrays.stream(columnValue.split(Constants.MULTIVALUE_SEPARATOR)).map(s -> s.trim().toLowerCase()).collect(Collectors.toList());
                        List<String> predefinedValues = Arrays.stream(customField.getPredefinedValues()).map(s -> s.trim().toLowerCase()).collect(Collectors.toList());

                        ServerUtils.intersect(chosenValues, predefinedValues);

                        if (!chosenValues.isEmpty()) {
                            rejectedRow[columnId].setErrorComment(this.commonLocalizer.localizeWithParamAccounting(PdfLocalizationName.valueIsNotAvailable, columnValue));
                        } else {
                            CustomFieldsUtils.setValueByDataType(customField, columnValue.replace("|", "-:-"));
                            return customField;
                        }
                    }
                }
            }
        }

        if (StringUtils.isNotBlank(rejectedRow[columnId].getComment())) {
            return null;
        } else {
            CustomFieldsUtils.setValueByDataType(customField, columnValue);
            return customField;
        }
    }

    private BigDecimal parseBigDecimal(String columnValue) {
        try {
            return new BigDecimal(columnValue.trim().replace(",", ""));
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getInvoicesQuotesAsSelectItem(ListingFilterParameter fp) {
        SelectItem[] items;
        if (Constants.SALE_INVOICE.equals(fp.getInvoiceType())) {
            List<EdsBaseSaleInvoice> invoices = this.invoiceManager.getSaleInvoiceList(fp);
            items = new SelectItem[invoices.size()];
            int i = 0;
            for (EdsBaseSaleInvoice invoice : invoices) {
                items[i] = new SelectItem(invoice.getObjectID(), invoice.getNumber() + " -> " + invoice.getClient().getName());
                i++;
            }
            return items;
        } else if (Constants.PURCHASE_INVOICE.equals(fp.getInvoiceType())) {
            List<EdsPurchaseInvoice> invoices = this.invoiceManager.getPurchaseInvoiceList(fp, false);
            items = new SelectItem[invoices.size()];
            int i = 0;
            for (EdsPurchaseInvoice invoice : invoices) {
                items[i] = new SelectItem(invoice.getObjectID(), invoice.getNumber());
                i++;
            }
            return items;
        } else if (Constants.SALE_QUOTE.equals(fp.getInvoiceType()) || Constants.SALE_ORDER.equals(fp.getInvoiceType())) {
            fp.setParams(Constants.SALE_QUOTE);
            if (Constants.SALE_QUOTE.equals(fp.getInvoiceType())) {
                fp.setSearchByParent(true);
            }
            fp.setAllByFilter(true);
            List<EdsSaleQuote> quotes = this.quoteManager.getSaleQuoteList(fp, null);
            items = new SelectItem[quotes.size()];
            int i = 0;
            for (EdsSaleQuote quote : quotes) {
                items[i] = new SelectItem(quote.getObjectID(), quote.getNumber() + " -> " + quote.getClient().getName());
                i++;
            }
            return items;
        } else if (Constants.PURCHASE_ORDER.equals(fp.getInvoiceType())) {
            List<EdsPurchaseOrder> orders = this.quoteManager.getPurchaseOrderList(fp, null);
            items = new SelectItem[orders.size()];
            int i = 0;
            for (EdsPurchaseOrder order : orders) {
                items[i] = new SelectItem(order.getObjectID(), order.getNumber());
                i++;
            }
            return items;
        } else if (SALES_QUOTE_RENTAL_ORDER.equals(fp.getInvoiceType())) {
            fp.setParams(Constants.SALE_QUOTE);
            fp.setAllByFilter(true);
            List<EdsSaleQuote> quotes = this.quoteManager.getSaleQuoteList(fp, null);
            List<EdsRentalOrder> orders = this.rentalOrderManager.getRentalOrderList(fp);
            items = new SelectItem[quotes.size() + orders.size()];
            int i = 0;
            for (EdsSaleQuote quote : quotes) {
                items[i] = new SelectItem(quote.getObjectID(), quote.getNumber() + " -> " + quote.getClient().getName());
                i++;
            }
            for (EdsRentalOrder order : orders) {
                items[i] = new SelectItem(order.getObjectID(), order.getNumber() + " --> " + order.getCustomer().getName(), "rentalOrders");
                i++;
            }
            return items;
        }
        return null;
    }

    @Override
    public EmployeeProfileItem getEmployeeProfile() {
        EmployeeProfileItem result = new EmployeeProfileItem();

        EdsUser edsUser = this.userManager.getUser();
        if (edsUser == null) {
            return result;
        }

        result.setUserId(edsUser.getObjectID());
        result.setFirstName(edsUser.getFirstName() == null ? "N/A" : edsUser.getFirstName());
        result.setLastName(edsUser.getLastName() == null ? "N/A" : edsUser.getLastName());
        SelectItem photoItem = this.getEmployeeImageURL();
        if (photoItem != null) {
            result.setEmployeeImageUrl(photoItem.getName());
        }
        if (edsUser.isEmployee()) {
            EdsEmployee edsEmployee = edsUser.getEmployee();
            result.setPosition(edsEmployee.getPosition() != null ? edsEmployee.getPosition().getName() : "");
        } else {
            result.setClientContact(true);
        }
        return result;
    }

    @Override
    public HashMap<Integer, String[]> getExistingCustomFields(String entityName, String entityCategoryName, Integer objectID) {
        return this.profileServiceLocal.getExistingCustomFields(null, entityName, entityCategoryName, null, objectID);
    }

    @Override
    public CompanyCustomFieldItem getCustomFieldData(Integer objectID) {
        return this.profileServiceLocal.getCustomFieldData(objectID, null);
    }

    @Override
    public CompanyCustomFieldItem getCustomFieldByAlias(String entityName, String alias) {
        return this.profileServiceLocal.getCustomFieldByAlias(entityName, alias);
    }

    @Override
    public void saveCustomFields(CompanyCustomFieldItem item, boolean fromItemtable) {
        this.profileServiceLocal.saveCustomFields(null, item, fromItemtable);
    }

    @Override
    public void saveStaticFieldProperty(String formId, FormProperty formProperty) {
        if (formId != null && formProperty != null) {
            EdsFormProperty edsFormProperty = this.formPropertyManager.getByFormID(formId);

            if (edsFormProperty != null) {
                Gson gson = new Gson();
                FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
                if (formFields != null) {
                    for (FormProperty field : formFields) {
                        if (field != null && formProperty.getCode().equals(field.getCode())) {
                            this.setFormPropertyData(formProperty, field);
                            break;
                        }
                    }
                }
                edsFormProperty.setSettingsJSONData(gson.toJson(formFields));
                this.formPropertyManager.createOrUpdate(edsFormProperty);


                if (LayoutRPC.LOGACALL_FORM.equals(formId) || LayoutRPC.LOGACALL_FORM_VIEW.equals(formId)) {
                    EdsFormProperty callFormProperty = this.formPropertyManager.getByFormID(LayoutRPC.LOGACALL_FORM.equals(formId) ? LayoutRPC.LOGACALL_FORM_VIEW : LayoutRPC.LOGACALL_FORM);
                    if (callFormProperty != null) {

                        FormProperty[] calFormFields = gson.fromJson(callFormProperty.getSettingsJSONData(), FormProperty[].class);
                        if (calFormFields != null) {
                            for (FormProperty field : calFormFields) {
                                if (field != null && formProperty.getCode().equals(field.getCode())) {
                                    this.setFormPropertyData(formProperty, field);
                                    break;
                                }
                            }
                        }
                        callFormProperty.setSettingsJSONData(gson.toJson(calFormFields));
                        this.formPropertyManager.createOrUpdate(callFormProperty);
                    }
                }
                if (LayoutRPC.ACTIVITY_FORM.equals(formId) || LayoutRPC.ACTIVITY_VIEW_FORM.equals(formId)) {
                    EdsFormProperty callFormProperty = this.formPropertyManager.getByFormID(LayoutRPC.ACTIVITY_FORM.equals(formId) ? LayoutRPC.ACTIVITY_VIEW_FORM : LayoutRPC.ACTIVITY_FORM);
                    if (callFormProperty != null) {

                        FormProperty[] calFormFields = gson.fromJson(callFormProperty.getSettingsJSONData(), FormProperty[].class);
                        if (calFormFields != null) {
                            for (FormProperty field : calFormFields) {
                                if (field != null && formProperty.getCode().equals(field.getCode())) {
                                    this.setFormPropertyData(formProperty, field);
                                    break;
                                }
                            }
                        }
                        callFormProperty.setSettingsJSONData(gson.toJson(calFormFields));
                        this.formPropertyManager.createOrUpdate(callFormProperty);
                    }
                }
            }
        }
    }

    private void setFormPropertyData(FormProperty formProperty, FormProperty field) {
        if (!field.getTitle().equals(formProperty.getTitle())) {
            field.setChanged(true);
        }
        field.setTitle(formProperty.getTitle());
        field.setRequired(formProperty.isRequired());
        field.setDisabled(formProperty.isDisabled());
        field.setDefaultValue(formProperty.getDefaultValue() != null && formProperty.getDefaultValue().length() > 0 ? formProperty.getDefaultValue() : null);
        field.setSelectedId(formProperty.getSelectedId());
        field.setRoleEdit(formProperty.getRoleEdit());
        field.setSystemRequired(formProperty.isSystemRequired());
        field.setMinChar(formProperty.getMinChar());
        field.setInformation(formProperty.isInformation());
        field.setInformationText(formProperty.getInformationText());
        field.setApprovalRelated(formProperty.isApprovalRelated());
    }

    @Override
    public DateNonConvertable getFinancialYearStart() {
        EdsFinancialSettings fs = this.financialSettingsManager.getFinancialSettings();

        if (fs.getFinancialYearEnd() != null) {

            Calendar fYearStart = Calendar.getInstance();
            fYearStart.setTime(fs.getFinancialYearEnd());
            fYearStart.add(Calendar.DAY_OF_MONTH, +1);

            return new DateNonConvertable(fYearStart.getTime());
        }

        return null;
    }

    @Transactional(propagation = Propagation.NEVER)
    public String resetCompany() {
        return this.companyService.resetCompany();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CompanyCustomFieldItem getCompanyCustomFieldByEntityNameAndFieldName(ViewName viewName, String columnCode) {
        EdsCompanyCustomFieldsSettings companyCF = this.companyCFSettingsManager.getCompanyCustomFieldByEntityNameAndFieldName(viewName.name(), columnCode);
        if (companyCF != null) {
            ArrayList<EdsCompanyCustomFieldsSettings> list = new ArrayList<EdsCompanyCustomFieldsSettings>();
            list.add(companyCF);
            return ((ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(list, viewName, false)).get(0);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getSkillsForEmployeeAssignment() {
        String skillGroups = this.genericSettingsManager.getValueByKey(GenericSettingsEnum.DEFAULT_SKILL_GROUPS);
        if (skillGroups != null && !skillGroups.isEmpty()) {
            String[] groups = skillGroups.split(",");
            List<EdsSkill> skillList = this.skillManager.getSkillListByGroupCodes(Arrays.asList(groups));

            if (skillList != null && skillList.size() > 0) {
                SelectItem[] skills = new SelectItem[skillList.size()];
                for (int i = 0; i < skillList.size(); i++) {
                    skills[i] = new SelectItem(skillList.get(i).getObjectID(), skillList.get(i).getName());
                }

                return skills;
            }
        }
        return new SelectItem[0];
    }

    public String setSideNavBarPosition(String position) {
        EdsUserSettings us = this.userSettingsManager.getUserSettingsValue("sidenavpos");
        if (us == null) {
            us = new EdsUserSettings();
            us.setKey("sidenavpos");
            us.setUser(this.userManager.getUser());
        }
        us.setValue(position);
        this.userSettingsManager.createOrUpdate(us);
        return position;
    }

    public String loadSideNavPosition() {
        EdsUserSettings us = this.userSettingsManager.getUserSettingsValue("sidenavpos");
        String result = null;
        if (us != null) {
            result = us.getValue();
        }
        return result;
    }

    @Override
    public PlaceOfSupplyItem getPlaceOfSupply(String taxTreatmentType) {
        EdsCompany company = this.userManager.getUser().getCompany();

        if (ServerUtils.isUAECompany(company)) {
            return this.getPlaceOfSupplyForUAE(taxTreatmentType);
        } else if (ServerUtils.isKSACompany(company)) {
            return this.getPlaceOfSupplyForKSA(taxTreatmentType);
        }

        return null;
    }

    @Override
    public SelectItem getDefaultPlaceOfSupply() {
        EdsCompany company = this.userManager.getUser().getCompany();

        if (ServerUtils.isUAECompany(company)) {
            EdsAddress address = company.getBillingAddress();

            if (address != null && address.getState() != null) {
                EdsRegion state = address.getState();
                SelectItem placeOfSupply = new SelectItem(state.getObjectID(), state.getName(), String.valueOf(state.getCountry().getObjectID()), true);
                placeOfSupply.setCode(state.getCode());
                placeOfSupply.setCategory(Constants.PLACEOFSUPPLY_CATEGORY.REGION);
                return placeOfSupply;
            }
        } else if (ServerUtils.isKSACompany(company)) {
            EdsCountry country = company.getCountry();
            SelectItem placeOfSupply = new SelectItem(country.getObjectID(), country.getName(), country.getAlias(), true);
            placeOfSupply.setCode(country.getCode());
            placeOfSupply.setCategory(Constants.PLACEOFSUPPLY_CATEGORY.COUNTRY);
            return placeOfSupply;
        }
        return null;
    }

    @Override
    public String[] checkCFName(String entityName, String label, String uiType) {

        CommonServiceImpl.log.info("Check custom field name: " + label);

        EdsModel model = this.modelManager.getCustomFormModel(entityName);

        String categoryName = null;
        boolean exist;
        if (model != null && model.isCustomForm()) {
            entityName = ViewName.CustomFormItems.name();
            categoryName = model.getViewName();
            exist = this.companyCFSettingsManager.isCustomAliasNameExists(entityName, categoryName, label, null);
        } else if (model != null && model.isStepForm()) {
            entityName = ViewName.OnboardingStep.name();
            categoryName = model.getViewName();
            exist = this.companyCFSettingsManager.isCustomAliasNameExists(entityName, categoryName, label, null);
        } else {
            exist = this.companyCFSettingsManager.isCustomNameExists(entityName, label);

            String formId = switch (entityName) {
                case "Opportunity" -> LayoutRPC.OPPORTUNITY_FORM;
                case "RequestForQuote" -> LayoutRPC.REQUEST_FOR_QUOTE_FORM;
                case "SaleQuote" -> LayoutRPC.SALEQUOTE_FORM;
                case "CrmCase" -> LayoutRPC.CASE_FORM;
                case "SaleOrder" -> "SALEORDER_FORM";
                case "PurchaseOrder" -> LayoutRPC.PURCHASEORDER_FORM;
                default -> null;
            };
            if (formId != null) {
                EdsFormProperty formProperty = this.formPropertyManager.getByFormID(formId);

                if (formProperty != null) {
                    Gson gson = new Gson();
                    FormProperty[] fields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);
                    if (fields != null) {
                        for (FormProperty field : fields) {
                            if (field != null && label.equals(field.getCode())) {
                                exist = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (exist) {
            return null;
        }
        if (Constants.UI_TYPE_ITEM_TABLE.equals(uiType)) {
            EdsCFItemTableSetting tableSetting = this.cfItemTableSettingmanager.findByName(model.getFormID(), label);
            return tableSetting != null ? null : new String[]{uiType, label};
        }
        HashMap<Integer, String[]> map = this.profileServiceLocal.getExistingCustomFields(null, entityName, categoryName, null, null);
        return CustomFieldsUtils.getDataType(uiType, map);
    }

    private String getEntityNameByFormID(String formId) {
        EdsModel model = this.modelManager.get(formId, true);
        if (model == null) {
            return null;
        }
        return model.getViewName();
    }

    @Override
    public void deleteCustomField(String entityName, String columnCode) {
        EdsModel model = this.modelManager.getCustomFormModel(entityName);
        if (columnCode.startsWith(Constants.ITEM_TABLE) && entityName.startsWith(Constants.CUSTOM_VIEW) && model != null) { // custom item table

            List<EdsCompanyCustomFieldsSettings> cfS = this.companyCFManager.getCompanyCustomFieldsWithCategory(ViewName.CustomFormItemTable.name(), columnCode);
            for (EdsCompanyCustomFieldsSettings cf : cfS) {
                this.profileServiceLocal.deleteCustomField(cf.getObjectID(), null);
            }

            this.cfItemTableSettingmanager.deleteByUUID(columnCode);
            this.customItemTableManager.deleteByUUID(columnCode);

            EdsModelField field = this.modelFieldManager.getByFieldID(model.getFormID(), columnCode);
            this.modelFieldManager.delete(field);

        } else if (columnCode.startsWith(Constants.ITEM_TABLE) && model != null && LayoutRPC.OPPORTUNITY_FORM.equals(model.getFormID())) { // opportunity custom item table

            List<EdsCompanyCustomFieldsSettings> cfS = this.companyCFManager.getCompanyCustomFieldsWithCategory(ViewName.OpportunityItemTable.name(), columnCode);
            for (EdsCompanyCustomFieldsSettings cf : cfS) {
                this.profileServiceLocal.deleteCustomField(cf.getObjectID(), null);
            }

            this.cfItemTableSettingmanager.deleteByUUID(columnCode);
            this.opportunityItemTableManager.deleteByUUID(columnCode);

            EdsModelField field = this.modelFieldManager.getByFieldID(model.getFormID(), columnCode);
            this.modelFieldManager.delete(field);

        } else if (columnCode.startsWith(Constants.ITEM_TABLE) && model != null && LayoutRPC.HRMS_EMPLOYEE_FORM.equals(model.getFormID())) { // employee custom item table

            List<EdsCompanyCustomFieldsSettings> cfS = this.companyCFManager.getCompanyCustomFieldsWithCategory(ViewName.EmployeeItemTable.name(), columnCode);
            for (EdsCompanyCustomFieldsSettings cf : cfS) {
                this.profileServiceLocal.deleteCustomField(cf.getObjectID(), null);
            }

            this.cfItemTableSettingmanager.deleteByUUID(columnCode);
            this.employeeItemTableManager.deleteByUUID(columnCode);

            EdsModelField field = this.modelFieldManager.getByFieldID(model.getFormID(), columnCode);
            this.modelFieldManager.delete(field);

        } else if (columnCode.startsWith(Constants.ITEM_TABLE) && model != null && LayoutRPC.PLACEMENT_FORM.equals(model.getFormID())) {
            List<EdsCompanyCustomFieldsSettings> cfS = this.companyCFManager.getCompanyCustomFieldsWithCategory(ViewName.PlacementItemTable.name(), columnCode);
            for (EdsCompanyCustomFieldsSettings cf : cfS) {
                this.profileServiceLocal.deleteCustomField(cf.getObjectID(), null);
            }

            this.cfItemTableSettingmanager.deleteByUUID(columnCode);
            this.placementItemTableManager.deleteByUUID(columnCode);

            EdsModelField field = this.modelFieldManager.getByFieldID(model.getFormID(), columnCode);
            this.modelFieldManager.delete(field);
        } else if (columnCode.startsWith(Constants.ITEM_TABLE) && model != null && LayoutRPC.PROJECT_FORM.equals(model.getFormID())) {
            List<EdsCompanyCustomFieldsSettings> cfS = this.companyCFManager.getCompanyCustomFieldsWithCategory(ViewName.ProjectItemTable.name(), columnCode);
            for (EdsCompanyCustomFieldsSettings cf : cfS) {
                this.profileServiceLocal.deleteCustomField(cf.getObjectID(), null);
            }

            this.cfItemTableSettingmanager.deleteByUUID(columnCode);
            this.projectItemTableManager.deleteByUUID(columnCode);

            EdsModelField field = this.modelFieldManager.getByFieldID(model.getFormID(), columnCode);
            this.modelFieldManager.delete(field);
        } else if (columnCode.startsWith(Constants.ITEM_TABLE) && model != null && LayoutRPC.CANDIDATE_FORM.equals(model.getFormID())) {
            List<EdsCompanyCustomFieldsSettings> cfS = this.companyCFManager.getCompanyCustomFieldsWithCategory(ViewName.CandidateCustomItemTable.name(), columnCode);
            for (EdsCompanyCustomFieldsSettings cf : cfS) {
                this.profileServiceLocal.deleteCustomField(cf.getObjectID(), null);
            }

            this.cfItemTableSettingmanager.deleteByUUID(columnCode);
            this.candidateItemTableManager.deleteByUUID(columnCode);

            EdsModelField field = this.modelFieldManager.getByFieldID(model.getFormID(), columnCode);
            this.modelFieldManager.delete(field);
        } else {
            EdsCompanyCustomFieldsSettings settings = this.getCustomField(entityName, columnCode);
            if (settings == null) {
                return;
            }
            if (settings != null && settings.getObjectID() != null && Constants.UI_TYPE_COMMITBOX.equalsIgnoreCase(settings.getUiType())) {
                cfCommitBoxNoteManager.deleteNotesByCustomField(settings);
            }
            if (model != null && model.isCustomForm()) {

                this.profileServiceLocal.deleteCustomField(model.getFormID(), settings.getObjectID(), SecurityContext.getCompanyID());
                return;
            }
            this.profileServiceLocal.deleteCustomField(settings.getObjectID(), SecurityContext.getCompanyID());
        }
    }

    @Override
    public String saveCustomField(CustomizeFormItem field) {

        if (Constants.UI_TYPE_APPROVAL_PROCESS.equals(field.getUiType())) {
            return saveCustomFormAttribute(field);
        }
        if (Constants.UI_TYPE_ITEM_TABLE.equals(field.getUiType())) {

            EdsModel model = this.modelManager.getCustomFormModel(field.getEntityName());
            if (model == null) {
                throw new RuntimeException("Model not found");
            }
            EdsCustomForm form = this.customFormManager.findByFormID(model.getFormID());

            EdsCFItemTableSetting setting = new EdsCFItemTableSetting();
            setting.setName(field.getLabel());

            setting.setCustomForm(model.getFormID());

            this.cfItemTableSettingmanager.createOrUpdate(setting);

            Integer sorder = this.modelFieldManager.getMaxSortOrder(model.getFormID());
            if (sorder == null) {
                sorder = 0;
            }
            EdsModelFieldCustom modelFieldForCustomField = new EdsModelFieldCustom();
            modelFieldForCustomField.setForm_ID(model.getFormID());
            modelFieldForCustomField.setField_ID(setting.getUuid());
            modelFieldForCustomField.setCustomField(true);
            modelFieldForCustomField.setForder(++sorder);
            modelFieldForCustomField.setFsection(field.getSection());
            modelFieldForCustomField.setWidget(field.getUiType());
            modelFieldForCustomField.setType(field.getDataType());
            modelFieldForCustomField.setLabel(field.getLabel());
            modelFieldForCustomField.setColumnType(ColumnType.COL_1);
            modelFieldForCustomField.setGridWidth(12);

            EdsCustomFormLocalization localization = new EdsCustomFormLocalization();
            localization.setDefaultName(field.getLabel());
            localization.setEnglishName(field.getLabel());
            localization.setArabicName(field.getLabel());
            localization.setRussianName(field.getLabel());
            localization.setUzbekName(field.getLabel());
            localization.setFormId(field.getFormID());
            localization.setSection(field.getSection());
            localization.setType(Constants.FIELD);
            customFormLocalizationManager.create(localization);
            modelFieldForCustomField.setCustomFormLocalization(localization);

            this.modelFieldManager.createOrUpdate(modelFieldForCustomField);

            return "";
        }

        CompanyCustomFieldItem item = new CompanyCustomFieldItem();

        CommonServiceImpl.log.info("Creating custom field from dynamic form: " + field.getName());
        CommonServiceImpl.log.info("Viewname : " + field.getEntityName());

        if (field.getEntityName().startsWith(Constants.CUSTOM_VIEW)) {
            item.setEntityName(ViewName.CustomFormItems.name());
            item.setEntityCategoryName(field.getEntityName());
            item.setEntityCategoryAlias(field.getLabel());
        } else if (field.getEntityName().startsWith(Constants.ONBOARDING_STEP_FORM)) {
            item.setEntityName(ViewName.OnboardingStep.name());
            item.setEntityCategoryName(field.getEntityName());
            item.setEntityCategoryAlias(field.getLabel());
        } else {
            item.setEntityName(field.getEntityName());
        }
        item.setFieldName(field.getLabel());
        item.setAliasName(field.getLabel());
        item.setUiType(field.getUiType().equals(Constants.NUMBER) ? Constants.UI_TYPE_TEXTBOX : field.getUiType());
        item.setDataType(field.getDataType());
        item.setCreationDate(new Date());
        item.setColumnCode(field.getName());
        item.setSection(field.getSection());
        item.setForm(field.getFormID());
        this.profileServiceLocal.saveCustomFields(null, item, false);
        return "";
    }

    private String saveCustomFormAttribute(CustomizeFormItem field) {
        String message = checkCustomFormAttributeCount(field.getUiType(), field.getFormID());
        if (message != null) {
            return message;
        }
        EdsModel model = this.modelManager.getCustomFormModel(field.getEntityName());
        if (model == null) {
            throw new RuntimeException("Model not found");
        }
        EdsCustomForm edsForm = this.customFormManager.findByFormID(model.getFormID());
        EdsCustomFormAttributes edsAttributes = new EdsCustomFormAttributes();
        edsAttributes.setCustomForm(edsForm);
        edsAttributes.setFieldType(field.getUiType());
        edsAttributes.setLabel(field.getLabel());
        this.customFormAttributeManager.create(edsAttributes);

        Integer sorder = this.modelFieldManager.getMaxSortOrder(model.getFormID());
        if (sorder == null) {
            sorder = 0;
        }
        EdsModelFieldCustom modelFieldForCustomField = new EdsModelFieldCustom();
        modelFieldForCustomField.setForm_ID(model.getFormID());
        modelFieldForCustomField.setField_ID(edsAttributes.getFieldId());
        modelFieldForCustomField.setCustomField(true);
        modelFieldForCustomField.setForder(++sorder);
        modelFieldForCustomField.setFsection(field.getSection());
        modelFieldForCustomField.setWidget(field.getUiType());
        modelFieldForCustomField.setType(field.getDataType());
        modelFieldForCustomField.setLabel(field.getLabel());
        modelFieldForCustomField.setColumnType(ColumnType.COL_1);
        this.modelFieldManager.createOrUpdate(modelFieldForCustomField);
        return "";
    }

    @Override
    public Integer[] getCustomFieldsCount(String formID) {
        Integer[] countArray = getCFCountArrayByFormId(formID);
        if (countArray[0] == STRING_FIELD_LIMIT || countArray[1] == DOULE_FIELD_LIMIT || countArray[2] == FIELD_LIMIT) {
            profileServiceLocal.clearFromDbDeletedCustomFieldsByFormId(formID, this.getEntityNameByFormID(formID), true);
            return getCFCountArrayByFormId(formID);
        }
        return countArray;
    }

    private Integer[] getCFCountArrayByFormId(String formID) {
        String viewName = this.getEntityNameByFormID(formID);
        HashMap<Integer, String[]> map = this.getExistingCustomFields(viewName, null, null);
        return new Integer[]{map.get(0).length, map.get(1).length, map.get(2).length};
    }

    private EdsCompanyCustomFieldsSettings getCustomField(String entityName, String columnCode) {
        EdsModel model = this.modelManager.getCustomFormModel(entityName);

        EdsCompanyCustomFieldsSettings settings;

        if (model != null && model.isCustomForm()) {

            settings = this.companyCFManager.getCompanyCustomField(ViewName.CustomFormItems.name(), entityName, columnCode);

        } else if (model != null && model.isStepForm()) {
            settings = this.companyCFManager.getCompanyCustomField(ViewName.OnboardingStep.name(), entityName, columnCode);
        } else {
            settings = this.companyCFManager.getCompanyCustomField(entityName, columnCode);
        }
        return settings;
    }

    @Override
    public void updateCustomField(String entityName, String name, boolean mandatory) {
        EdsCompanyCustomFieldsSettings settings = this.getCustomField(entityName, name);
        if (settings != null) {
            settings.setRequired(mandatory);
        }

        List<EdsModel> modelList = this.modelManager.getModelList(entityName);
        if (CollectionUtils.isNotEmpty(modelList)) {
            String formID = modelList.get(0).getFormID();

            EdsModelField modelField = this.modelFieldManager.getByFieldID(formID, name);
            if (modelField == null) {
                return;
            }
            modelField.setMandatory(mandatory);
            this.modelFieldManager.update(modelField);
        }
    }

    @Override
    public CompanyCustomFieldItem getCustomFieldByEntityNameAndColumnCode(String entityCategoryName, String columnCode) {

        List<EdsModel> modelList = this.modelManager.getModelList(entityCategoryName);

        EdsCompanyCustomFieldsSettings settings;

        EdsModel model = modelList != null ? modelList.get(0) : null;

        if (model != null && model.isCustomForm()) {
            settings = this.companyCFSettingsManager.getCompanyCustomField(ViewName.CustomFormItems.name(), model.getViewName(), columnCode);
        } else if (model != null && model.isStepForm()) {
            settings = this.companyCFSettingsManager.getCompanyCustomField(ViewName.OnboardingStep.name(), model.getViewName(), columnCode);
        } else {
            settings = this.companyCFSettingsManager.getCompanyCustomField(entityCategoryName, columnCode);
        }


        if (settings == null && model != null && model.isCustomForm()) {
            settings = new EdsCompanyCustomFieldsSettings();
            EdsModelField modelField = this.modelFieldManager.getByFieldID(model.getFormID(), columnCode);
            if (modelField != null && modelField.getWidget().equals(UI_TYPE_APPROVAL_PROCESS)) {

                settings.setUiType(modelField.getWidget());
                settings.setFieldName(modelField.getLabel());
                settings.setAliasName(modelField.getLabel());
                settings.setRequired(modelField.isMandatory());
                settings.setDataType(modelField.getType());
                if (model != null && model.isCustomForm()) {
                    settings.setEntityName(ViewName.CustomFormItems.name());
                }
                settings.setEntityCategoryName(entityCategoryName);
                settings.setColumnCode(modelField.getField_ID());

                EdsAuditInfo info = new EdsAuditInfo();
                info.setCreationDate(new Date());
                info.setModificationDate(new Date());
                info.setModifiedBy((EdsUser) SecurityContext.getInstance().getUser());

                settings.setAuditInfo(info);
            }
            companyCFSettingsManager.create(settings);
        }

        CompanyCustomFieldItem item = settings != null ? settings.getRPC(null) : null;

        ArrayList<SelectItem> roleItems = new ArrayList<>();
        List<EdsRole> roles = this.roleManager.list();
        roles.add(roleManager.getByCode(ESS_USER_CODE));
        if (item != null) {
            item.setCustomForm(model != null && model.isCustomForm());
            boolean selected;
            for (EdsRole role : roles) {
                selected = item.getAllowedRoles().contains(role.getObjectID());
                roleItems.add(new SelectItem(role.getObjectID(), this.commonLocalizer.localize(role.getCode(), role.getName()), "", selected));
            }

            if (model != null && model.getFormID() != null) {
                EdsModelField modelField = this.modelFieldManager.getByFieldID(model.getFormID(), columnCode);

                if (modelField != null) {
                    item.setDefaultValue(modelField.getDefaultValue());
                }
            }
        }

        ArrayList<SelectItem> allRoles = new ArrayList<>();
        for (EdsRole role : roles) {
            allRoles.add(new SelectItem(role.getObjectID(), this.commonLocalizer.localize(role.getCode(), role.getName())));
        }
        item.setAllRoles(allRoles);
        item.setRoleList(roleItems);
        return item;
    }

    @Override
    public CustomFormItem getCustomForm(Integer objectId) {
        CustomFormItem item = new CustomFormItem();
        EdsProperty property = this.propertManager.get(objectId);

        if (property != null) {
            if (!property.getCustom()) {
                item.setContext(property.getModuleCode());
                item.setName(property.getSingular());
                item.setPlural(property.getPlural());
                item.setShortName(property.getShortcut());
                item.setPropertyID(property.getObjectID());
            } else {
                EdsCustomForm customForm = this.customFormManager.findByFormID(property.getFormID());
                if (customForm != null) {

                    item = customForm.toRpc(false);

                    if (customForm.getRoles() != null) {

                        customForm.getRoles().removeIf(role -> EdsRole.ADMIN_CODE.equals(role.getCode()) || EdsRole.CREATOR_CODE.equals(role.getCode()));

                        List<Integer> roleIDs = customForm.getRoles()
                                .stream()
                                .filter(edsRole -> edsRole != null && !edsRole.getDeleted())
                                .map(EdsRole::getObjectID)
                                .collect(Collectors.toList());

                        item.setSelectedRoleIds(roleIDs);
                    }
                    if (item.getlName() == null) {
                        EdsCustomFormLocalization lName = new EdsCustomFormLocalization();
                        lName.setDefaultName(customForm.getName());
                        lName.setEnglishName(customForm.getName());
                        lName.setArabicName(customForm.getName());
                        lName.setRussianName(customForm.getName());
                        lName.setUzbekName(customForm.getName());
                        customFormLocalizationManager.create(lName);

                        EdsCustomFormLocalization lPlural = new EdsCustomFormLocalization();
                        lPlural.setDefaultName(customForm.getName());
                        lPlural.setEnglishName(customForm.getName());
                        lPlural.setArabicName(customForm.getName());
                        lPlural.setRussianName(customForm.getName());
                        lPlural.setUzbekName(customForm.getName());
                        customFormLocalizationManager.create(lPlural);

                        EdsCustomFormLocalization lShort = new EdsCustomFormLocalization();
                        lShort.setDefaultName(customForm.getName());
                        lShort.setEnglishName(customForm.getName());
                        lShort.setArabicName(customForm.getName());
                        lShort.setRussianName(customForm.getName());
                        lShort.setUzbekName(customForm.getName());
                        customFormLocalizationManager.create(lShort);

                        item.setlName(lName.getRPC());
                        item.setlPlural(lPlural.getRPC());
                        item.setlShort(lShort.getRPC());

                        property.setLName(lName);
                        property.setlPlural(lPlural);
                        property.setlShort(lShort);
                        propertManager.update(property);
                    }
                }
            }
            item.setOldFormID(property.getFormID());
            if (property.getConvertItems() != null) {
                Gson gson = new Gson();
                item.setConvertItems(gson.fromJson(property.getConvertItems(), ConvertItem[].class));
            }

            EdsContainerItem containerItem = this.containerItemManager.getItem(property.getObjectID(), property.getModuleCode());
            if (containerItem != null && containerItem.getContainer() != null) {
                SelectItem selectItem = new SelectItem();
                selectItem.setId(containerItem.getContainer().getObjectID());
                selectItem.setName(containerItem.getContainer().isChanged() ? containerItem.getContainer().getDefaultName() : this.commonLocalizer.localize(containerItem.getContainer().getDefaultName()));
                selectItem.setOrderId(containerItem.getContainer().getSorder());
                item.setContainer(selectItem);
                item.setContainerItemId(containerItem.getObjectID());
            }
        }
        LinkedList<String> modules = new LinkedList<>();
        modules.add(ModuleEnum.CRM.getCode());
        modules.add(ModuleEnum.HRMS.getCode());
        modules.add(ModuleEnum.ACCOUNTING.getCode());
        modules.add(ModuleEnum.PAYROLL.getCode());
        modules.add(ModuleEnum.TRAINING_CENTER.getCode());
        modules.add(ModuleEnum.PM.getCode());

        LinkedHashMap<String, LinkedList<SelectItem>> items = new LinkedHashMap<>();
        for (String moduleCode : modules) {
            LinkedList<SelectItem> containerList = new LinkedList<>();
            List<EdsContainer> containers = this.containerManager.getContainerBySorder(moduleCode);
            if (containers != null && containers.size() > 0) {
                for (EdsContainer container : containers) {
                    SelectItem containerItem = new SelectItem();
                    containerItem.setId(container.getObjectID());
                    containerItem.setName(container.isChanged() ? container.getDefaultName() : this.commonLocalizer.localize(container.getDefaultName()));
                    containerItem.setOrderId(container.getSorder());
                    containerList.add(containerItem);
                }
            }
            items.put(moduleCode, containerList);
        }
        item.setSection(items);

        List<SelectItem> allRoles = this.allInOneServiceLocal.getAllRoles();
        allRoles.removeIf(role -> EdsRole.ADMIN_CODE.equals(role.getCode()) || EdsRole.CREATOR_CODE.equals(role.getCode()));
        item.setRoles(allRoles);
        return item;
    }

    @Override
    public ArrayList<SelectItem> getCustomForms() {
        ArrayList<SelectItem> items = new ArrayList<>();

        List<EdsCustomForm> customForms = this.customFormManager.getForms();
        if (customForms != null && customForms.size() > 0) {
            for (EdsCustomForm customForm : customForms) {
                SelectItem selectItem = new SelectItem();
                selectItem.setId(customForm.getObjectID());
                selectItem.setEntityId(customForm.getObjectID());
                selectItem.setName(customForm.getName());
                selectItem.setCode(customForm.getFormID());
                selectItem.setDescription(customForm.getFormID());

                items.add(selectItem);
            }
        }
        return items;
    }

    @Override
    public Email getEmailbyTrackerid(Integer trackerId) {

        EdsEmail email = this.emailRepository.findLastByTrackerId(trackerId);

        return email != null ? email.getRPC() : null;
    }

    @Override
    @Transactional
    public String saveCustomForm(CustomFormItem item) {
        String oldFormId = "";
        boolean isCopy = item.isCopy();
        if (item.getName() == null) {
            return null;
        }

        if (!item.isCustom()) {
            PropertyItem p = new PropertyItem();
            p.setId(item.getPropertyID());
            p.setDefaultName(item.getName());
            p.setPlural(item.getPlural());
            p.setSingular(item.getName());
            p.setShortcut(item.getShortName());
            this.profileServiceLocal.saveProperty(p);
            return "";
        }

        String NAME = ServerUtils.transliterate(item.getName()).replace(" ", "_").replaceAll("()", "").
                replace("'", "").toUpperCase();
        String FORM_NAME = NAME.toUpperCase() + "_FORM";

        boolean isNew = false;
        String customFormName = item.getName().replace("'", "");
        EdsCustomForm customForm = customFormManager.findByName(customFormName);
        if (customForm != null && (item.getObjectId() == null || !customForm.getObjectID().equals(item.getObjectId()))) {
            throw new EntityExistsException("Already exist");
        } else {
            if (item.getObjectId() == null) {
                EdsModel edsModel = this.modelManager.get(FORM_NAME);
                if (edsModel != null) {
                    throw new EntityExistsException("Already exist");
                }
                customForm = new EdsCustomForm();
            } else if (customForm == null) {
                if (isCopy) {
                    customForm = this.customFormManager.get(item.getObjectId()).cloneShallow();
                } else {
                    customForm = this.customFormManager.get(item.getObjectId());
                }
                oldFormId = customForm.getFormID();
            }
        }
        String module = "";
        if (item.getContext() != null && item.getContext().contains(",")) {
            module = item.getContext();
        } else {
            module = item.getModule() != null ? item.getModule().getCode() : "";
        }

        EdsUser user = this.userManager.getUser();
        EdsAuditInfo info = customForm.getAuditInfo();
        info.setModificationDate(new Date());
        info.setModifiedBy(user);
        if (customForm.isNew() || isCopy) {
            isNew = true;
            info.setCreationDate(new Date());
            info.setCreatedBy(user);
            customForm.setFormID(FORM_NAME);
        }
        customForm.setAuditInfo(info);
        customForm.setRoles(new ArrayList<>());

        this.customFormManager.createOrUpdate(customForm);

        customForm.setName(item.getName());
        customForm.setQuotaPerUser(item.getQuotaPerUser());
        customForm.setQuotaPerForm(item.getQuotaPerForm());
        if (item.getTimer() != null) {
            customForm.setTimer(item.getTimer()[0] + "," + item.getTimer()[1]);
        } else {
            customForm.setTimer(null);
        }
        customForm.setWelcomeMessage(item.getWelcomeMessage());
        customForm.setEndTimeMessage(item.getEndTimeMessage());

        customForm.setQuiz(item.isQuizForm());
        customForm.setAnonymous(item.isAnonymousForm());
        CustomFormRuleItem formRuleItem = item.getRuleItem();
        if (formRuleItem != null) {
            customForm.setConditionType(formRuleItem.getConditionType());
            customForm.setConditionValue(formRuleItem.getConditionValue());
            customForm.setRange(formRuleItem.getRange());
            customForm.setStartDate(formRuleItem.getStartDate());
            customForm.setEndDate(formRuleItem.getEndDate());
        } else {
            customForm.setConditionType(null);
            customForm.setConditionValue(null);
            customForm.setRange(null);
            customForm.setStartDate(null);
            customForm.setEndDate(null);
        }
        EdsProperty property = null;
        if (isCopy) {
            property = new EdsProperty();
        } else {
            property = customForm.getProperty();
        }
        if (property == null || property.getObjectID() == null && !isCopy) {
            if (item.getPropertyID() != null) {
                property = this.propertManager.get(item.getPropertyID());
            }
        }
        property.setDefaultName(item.getName());
        property.setSingular(item.getName());
        property.setPlural(item.getPlural());
        property.setShortcut(item.getShortName());
        property.setFormType(item.getType());
        property.setActive(true);

        property.setModuleCode(module);
        property.setFid(customForm.getObjectID());
        property.setCustom(Boolean.TRUE);
        if (isNew || isCopy) {
            property.setObjectName(NAME);
            property.setFormID(FORM_NAME);
        } else {
            property.setLastModifiedDate(new Date());
            property.setUserId(this.userManager.getUser().getObjectID());
        }

        Gson gson = new Gson();
        property.setConvertItems(gson.toJson(item.getConvertItems()));

        if (item.getlName() != null) {
            EdsCustomFormLocalization lName = customFormLocalizationManager.get(item.getlName().getId());
            lName.setEnglishName(item.getlName().getEnglishName());
            lName.setArabicName(item.getlName().getArabicName());
            lName.setRussianName(item.getlName().getRussianName());
            lName.setUzbekName(item.getlName().getUzbekName());
            customFormLocalizationManager.update(lName);
            property.setLName(lName);

            EdsCustomFormLocalization lPlural = customFormLocalizationManager.get(item.getlPlural().getId());
            lPlural.setEnglishName(item.getlPlural().getEnglishName());
            lPlural.setArabicName(item.getlPlural().getArabicName());
            lPlural.setRussianName(item.getlPlural().getRussianName());
            lPlural.setUzbekName(item.getlPlural().getUzbekName());
            customFormLocalizationManager.update(lPlural);
            property.setlPlural(lPlural);

            EdsCustomFormLocalization lShort = customFormLocalizationManager.get(item.getlShort().getId());
            lShort.setEnglishName(item.getlShort().getEnglishName());
            lShort.setArabicName(item.getlShort().getArabicName());
            lShort.setRussianName(item.getlShort().getRussianName());
            lShort.setUzbekName(item.getlShort().getUzbekName());
            customFormLocalizationManager.update(lShort);
            property.setlShort(lShort);
        }

        this.propertManager.createOrUpdate(property);
        customForm.setProperty(property);

        List<EdsRole> edsRoles = new ArrayList<>();
        edsRoles.add(this.roleManager.get(EdsRole.ADMIN));
        edsRoles.add(this.roleManager.getByCode(EdsRole.CREATOR_CODE));

        if (item.getSelectedRoleIds().size() > 0) {
            for (Integer roleId : item.getSelectedRoleIds()) {
                EdsRole edsRole = this.roleManager.get(roleId);
                if (edsRole != null) {
                    edsRoles.add(edsRole);
                }
            }
        }
        customForm.getRoles().clear();
        customForm.setRoles(edsRoles);

        if (item.getContainer() != null) {
            EdsContainer container = this.containerManager.get(item.getContainer().getId());

            EdsContainerItem containerItem = null;
            if (item.getContainerItemId() != null) {
                if (isCopy) {
                    containerItem = this.containerItemManager.get(item.getContainerItemId()).cloneShallow();
                } else {
                    containerItem = this.containerItemManager.get(item.getContainerItemId());
                }
            } else {
                containerItem = new EdsContainerItem();
            }
            if (containerItem != null && container != null) {
                Integer maxSorder = this.containerItemManager.getMaxSorderByContainer(module, container.getObjectID());

                String myModuleName = switch (module) {
                    case "accounting" -> "ACCOUNTING_MODULE";
                    case "hrms" -> "HRMS_MODULE";
                    case "pm" -> "PM";
                    case "crm" -> "CRM_MODULE";
                    case "documents" -> "DOCUMENT_MANAGEMENT";
                    case "payroll" -> "PAYROLL";
                    default -> "";
                };
                EdsModule mymodule = this.moduleManager.getModule(myModuleName);
                containerItem.setModule(mymodule);

                containerItem.setProperty(property);
                containerItem.setContainer(this.containerManager.get(item.getContainer().getId()));
                containerItem.setSorder(maxSorder != null ? maxSorder + 1 : 0);
                containerItem.setModuleCode(module);
                containerItem.setActive(true);

                this.containerItemManager.createOrUpdate(containerItem);
            }
        }

        if (isNew) {
            this.permissionManager.insertPermissionForCustomForm(FORM_NAME, customFormName, module, edsRoles);
            this.createWorkflowModule(NAME, customFormName, true);

        } else if (item.getOldFormID() != null) {
            List<String> defaultRoleList = new ArrayList<>();
            defaultRoleList.add(EdsRole.ADMIN_CODE);
            defaultRoleList.add(EdsRole.CREATOR_CODE);

            LinkedHashMap<String, List<String>> roleMap = new LinkedHashMap<>();
            LinkedList<String> permissions = new LinkedList<>();
            Integer companyId = user.getCompany().getObjectID();

            permissions.add(item.getOldFormID() + "_" + companyId);
            permissions.add(item.getOldFormID() + "_ADD_" + companyId);
            permissions.add(item.getOldFormID() + "_COPY_" + companyId);
            permissions.add(item.getOldFormID() + "_EDIT_" + companyId);
            permissions.add(item.getOldFormID() + "_DELETE_" + companyId);
            permissions.add(item.getOldFormID() + "_PDF_" + companyId);
            permissions.add(item.getOldFormID() + "_SUMMARY_" + companyId);
            permissions.add(item.getOldFormID() + "_FULL_LIST_" + companyId);
            permissions.add(item.getOldFormID() + "_SEE_OWN_" + companyId);
            permissions.add(item.getOldFormID() + "_SEE_BY_TYPE_" + companyId);
            permissions.add(item.getOldFormID() + "_ADD_LINKS_" + companyId);
            permissions.add(item.getOldFormID() + "_CUSTOMIZE_COLUMN_" + companyId);
            permissions.add(item.getOldFormID() + "_EXPORT_" + companyId);
            permissions.add(item.getOldFormID() + "_FILTER_" + companyId);

            for (String permission : permissions) {
                List<String> roleList = this.rolePermissionManager.getRolesByPermissionCode(permission);
                if (roleList != null && roleList.size() > 0) {
                    roleMap.put(permission, roleList);
                } else {
                    roleMap.put(permission, defaultRoleList);
                }
            }

            this.permissionManager.deletePermissionForCustomForm(item.getOldFormID());
            this.permissionManager.insertPermissionForOldCustomForm(item.getOldFormID(), customFormName, module, roleMap);
            this.createWorkflowModule(item.getOldFormID().replace("_FORM", ""), customFormName, true);
        }

        if (isCopy) {

            EdsModel model = null;
            if (modelManager.get(oldFormId) != null) {
                model = modelManager.get(oldFormId).cloneShallow();
            } else {
                model = new EdsModel();
            }
            model.setFormID(FORM_NAME);
            model.setTitle(item.getName());
            model.setViewName(Constants.CUSTOM_VIEW + NAME);
            model.setQuizForm(item.isQuizForm());
            model.setAnonymousForm(item.isAnonymousForm());
            this.modelManager.create(model);

            final List<EdsModelField> fields = this.modelFieldManager.getModelFields(oldFormId);
            final List<EdsCustomFormSection> sections = this.customFormSectionManager.getSections(oldFormId);

            sections.forEach(
                    section -> {
                        EdsCustomFormSection clonedSections = section.cloneShallow();
                        clonedSections.setForm_ID(FORM_NAME);

                        EdsCustomFormLocalization customFormLocalization = null;
                        if (clonedSections.getEdsCustomFormLocalization() != null) {
                            customFormLocalization = clonedSections.getEdsCustomFormLocalization().cloneShallow();
                        } else {
                            customFormLocalization = new EdsCustomFormLocalization();
                            customFormLocalization.setDefaultName(clonedSections.getLabel());
                            customFormLocalization.setEnglishName(clonedSections.getLabel());
                            customFormLocalization.setArabicName(clonedSections.getLabel());
                            customFormLocalization.setRussianName(clonedSections.getLabel());
                            customFormLocalization.setUzbekName(clonedSections.getLabel());
                            customFormLocalization.setFormId(FORM_NAME);
                            customFormLocalization.setSection(clonedSections.getSection());
                            customFormLocalization.setType(Constants.SECTION);
                        }
                        customFormLocalizationManager.create(customFormLocalization);
                        clonedSections.setEdsCustomFormLocalization(customFormLocalization);
                        customFormSectionManager.create(clonedSections);
                    });

            String finalOldFormId = oldFormId;
            fields.forEach(
                    field -> {
                        if (Constants.UI_TYPE_ITEM_TABLE.equals(field.getWidget())) {
                            String oldUUID = field.getField_ID();

                            EdsCFItemTableSetting setting = new EdsCFItemTableSetting();
                            setting.setName(field.getLabel());
                            setting.setCustomForm(FORM_NAME);
                            this.cfItemTableSettingmanager.createOrUpdate(setting);

                            EdsModelField clonedField = field.cloneShallow();
                            clonedField.setForm_ID(FORM_NAME);
                            clonedField.setField_ID(setting.getUuid());

                            EdsCustomFormLocalization clonedFieldLocalization = null;
                            if (clonedField.getCustomFormLocalization() != null) {
                                clonedFieldLocalization = clonedField.getCustomFormLocalization().cloneShallow();
                                clonedFieldLocalization.setFormId(FORM_NAME);
                            } else {
                                clonedFieldLocalization = new EdsCustomFormLocalization();
                                clonedFieldLocalization.setDefaultName(clonedField.getLabel());
                                clonedFieldLocalization.setEnglishName(clonedField.getLabel());
                                clonedFieldLocalization.setArabicName(clonedField.getLabel());
                                clonedFieldLocalization.setRussianName(clonedField.getLabel());
                                clonedFieldLocalization.setUzbekName(clonedField.getLabel());
                                clonedFieldLocalization.setFormId(FORM_NAME);
                                clonedFieldLocalization.setSection(clonedField.getSection());
                                clonedFieldLocalization.setType(Constants.FIELD);
                            }
                            customFormLocalizationManager.create(clonedFieldLocalization);
                            clonedField.setCustomFormLocalization(clonedFieldLocalization);
                            modelFieldManager.create(clonedField);

                            List<EdsCompanyCustomFieldsSettings> companyCustomFieldsSettings = companyCFSettingsManager.getCompanyCustomFieldsByCategoryForListView(CustomFieldSection.CustomFormItemTable.name(), field.getField_ID());
                            EdsCustomFormLocalization finalClonedFieldLocalization = clonedFieldLocalization;
                            companyCustomFieldsSettings.forEach(customfield -> {

                                EdsCompanyCustomFieldsSettings clonedCustomField = customfield.cloneShallow();
                                EdsCustomFormLocalization customFormLocalization = null;

                                if (clonedCustomField.getCustomFormlocalization() != null) {
                                    customFormLocalization = clonedCustomField.getCustomFormlocalization().cloneShallow();
                                } else {
                                    customFormLocalization = new EdsCustomFormLocalization();
                                    customFormLocalization.setDefaultName(clonedCustomField.getFieldName());
                                    customFormLocalization.setEnglishName(clonedCustomField.getFieldName());
                                    customFormLocalization.setArabicName(clonedCustomField.getFieldName());
                                    customFormLocalization.setRussianName(clonedCustomField.getFieldName());
                                    customFormLocalization.setUzbekName(clonedCustomField.getFieldName());

                                    customFormLocalization.setSection(field.getSection());
                                    customFormLocalization.setType(Constants.PREDEFINED);
                                }
                                customFormLocalization.setFormId(FORM_NAME);
                                customFormLocalization.setParent(finalClonedFieldLocalization);
                                customFormLocalizationManager.create(customFormLocalization);

                                if (Constants.UI_TYPE_DROPDOWN.equals(clonedCustomField.getUiType()) || Constants.UI_TYPE_CHECKBOX.equals(clonedCustomField.getUiType())
                                        || Constants.UI_TYPE_RADIOBUTTON.equals(clonedCustomField.getUiType())) {
                                    List<EdsCustomFormLocalization> listOfItems = customFormLocalizationManager.getPredefinedValues(clonedCustomField.getCustomFormlocalization().getObjectID());

                                    EdsCustomFormLocalization finalCustomFormLocalization = customFormLocalization;
                                    if (listOfItems.size() > 0) {
                                        listOfItems.forEach(predefined -> {
                                            EdsCustomFormLocalization predefinedValues = predefined.cloneShallow();
                                            predefinedValues.setParent(finalCustomFormLocalization);
                                            predefinedValues.setFormId(finalCustomFormLocalization.getFormId());
                                            customFormLocalizationManager.create(predefinedValues);
                                        });
                                    } else {
                                        if (clonedCustomField.getPredefinedValues() != null) {
                                            clonedCustomField.getPredefinedValues();
                                            for (String value : clonedCustomField.getPredefinedValues()) {
                                                EdsCustomFormLocalization source = new EdsCustomFormLocalization();
                                                source.setDefaultName(value);
                                                source.setEnglishName(value);
                                                source.setArabicName(value);
                                                source.setRussianName(value);
                                                source.setUzbekName(value);
                                                source.setType(Constants.ITEM_FIELD_PREDEFINED);
                                                source.setSection(field.getSection());
                                                source.setFormId(clonedField.getForm_ID());
                                                source.setParent(finalCustomFormLocalization);
                                                customFormLocalizationManager.create(source);
                                            }
                                        }
                                    }
                                }
                                if (customfield.getListeners() != null && customfield.getListeners().size() > 0) {
                                    clonedCustomField.getListeners().addAll(customfield.getListeners());
                                } else {
                                    clonedCustomField.setListeners(null);
                                }

                                if (customfield.getValidations() != null && customfield.getValidations().size() > 0) {
                                    clonedCustomField.getValidations().addAll(customfield.getValidations());
                                } else {
                                    clonedCustomField.setValidations(null);
                                }

                                if (customfield.getAllowedRoles() != null && customfield.getAllowedRoles().size() > 0) {
                                    clonedCustomField.getAllowedRoles().addAll(customfield.getAllowedRoles());
                                } else {
                                    clonedCustomField.setAllowedRoles(null);
                                }

                                if (customfield.getEditFieldRoles() != null && customfield.getEditFieldRoles().size() > 0) {
                                    clonedCustomField.getEditFieldRoles().addAll(customfield.getEditFieldRoles());
                                } else {
                                    clonedCustomField.setEditFieldRoles(null);
                                }

                                clonedCustomField.setEntityCategoryName(setting.getUuid());
                                clonedCustomField.setCustomFormlocalization(customFormLocalization);
                                companyCFSettingsManager.create(clonedCustomField);
                            });

                        } else {

                            EdsModelField clonedField = field.cloneShallow();
                            clonedField.setForm_ID(FORM_NAME);

                            EdsCustomFormLocalization customFormLocalization = null;
                            if (clonedField.getCustomFormLocalization() != null) {
                                customFormLocalization = clonedField.getCustomFormLocalization().cloneShallow();
                                customFormLocalization.setFormId(FORM_NAME);
                            } else {
                                customFormLocalization = new EdsCustomFormLocalization();
                                customFormLocalization.setDefaultName(clonedField.getLabel());
                                customFormLocalization.setEnglishName(clonedField.getLabel());
                                customFormLocalization.setArabicName(clonedField.getLabel());
                                customFormLocalization.setRussianName(clonedField.getLabel());
                                customFormLocalization.setUzbekName(clonedField.getLabel());
                                customFormLocalization.setFormId(FORM_NAME);
                                customFormLocalization.setSection(clonedField.getSection());
                                customFormLocalization.setType(Constants.FIELD);
                            }
                            customFormLocalizationManager.create(customFormLocalization);
                            clonedField.setCustomFormLocalization(customFormLocalization);
                            modelFieldManager.create(clonedField);

                            if (Constants.UI_TYPE_CHECKBOX.equals(field.getWidget()) || Constants.UI_TYPE_DROPDOWN.equals(field.getWidget())
                                    || Constants.UI_TYPE_RADIOBUTTON.equals(field.getWidget())) {
                                List<EdsCustomFormLocalization> listOfItems = customFormLocalizationManager.getPredefinedValues(clonedField.getCustomFormLocalization().getObjectID());
                                EdsCustomFormLocalization finalCustomFormLocalization = customFormLocalization;
                                if (listOfItems.size() > 0) {
                                    listOfItems.forEach(predefined -> {
                                        EdsCustomFormLocalization predefinedValues = predefined.cloneShallow();
                                        predefinedValues.setParent(finalCustomFormLocalization);
                                        predefinedValues.setFormId(FORM_NAME);
                                        customFormLocalizationManager.create(predefinedValues);
                                    });
                                } else if (field.getSource() != null && field.getSource().isEmpty()) {
                                    for (String value : field.getSource().split("-:-")) {
                                        EdsCustomFormLocalization source = new EdsCustomFormLocalization();
                                        source.setDefaultName(value);
                                        source.setEnglishName(value);
                                        source.setArabicName(value);
                                        source.setRussianName(value);
                                        source.setUzbekName(value);
                                        source.setType(Constants.PREDEFINED);
                                        source.setSection(field.getSection());
                                        source.setFormId(FORM_NAME);
                                        source.setParent(finalCustomFormLocalization);
                                        customFormLocalizationManager.create(source);
                                    }
                                }
                            }
                        }
                    });

            EdsModel oldModel = this.modelManager.get(oldFormId);
            List<EdsCompanyCustomFieldsSettings> companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsWithCategory(ViewName.CustomFormItems.name(), oldModel.getViewName());
            companyCFs.forEach(cCFs -> {
                EdsCompanyCustomFieldsSettings clonedCustomField = cCFs.cloneShallow();
                EdsCustomFormLocalization customFormLocalization = null;

                if (clonedCustomField.getCustomFormlocalization() != null) {
                    customFormLocalization = clonedCustomField.getCustomFormlocalization().cloneShallow();
                    customFormLocalization.setFormId(FORM_NAME);
                    customFormLocalizationManager.create(customFormLocalization);
                } else {
                    customFormLocalization = new EdsCustomFormLocalization();
                    customFormLocalization.setDefaultName(clonedCustomField.getFieldName());
                    customFormLocalization.setEnglishName(clonedCustomField.getFieldName());
                    customFormLocalization.setArabicName(clonedCustomField.getFieldName());
                    customFormLocalization.setRussianName(clonedCustomField.getFieldName());
                    customFormLocalization.setUzbekName(clonedCustomField.getFieldName());
                    customFormLocalization.setFormId(FORM_NAME);
                    customFormLocalization.setType(Constants.FIELD);
                    customFormLocalizationManager.create(customFormLocalization);
                }

                if (Constants.UI_TYPE_DROPDOWN.equals(clonedCustomField.getUiType())) {
                    List<EdsCustomFormLocalization> listOfItems = customFormLocalizationManager.getPredefinedValues(clonedCustomField.getCustomFormlocalization().getObjectID());

                    EdsCustomFormLocalization finalCustomFormLocalization = customFormLocalization;
                    if (listOfItems.size() > 0) {
                        listOfItems.forEach(predefined -> {
                            EdsCustomFormLocalization predefinedValues = predefined.cloneShallow();
                            predefinedValues.setParent(finalCustomFormLocalization);
                            predefinedValues.setFormId(finalCustomFormLocalization.getFormId());
                            customFormLocalizationManager.create(predefinedValues);
                        });
                    } else {
                        if (clonedCustomField.getPredefinedValues() != null) {
                            clonedCustomField.getPredefinedValues();
                            for (String value : clonedCustomField.getPredefinedValues()) {
                                EdsCustomFormLocalization source = new EdsCustomFormLocalization();
                                source.setDefaultName(value);
                                source.setEnglishName(value);
                                source.setArabicName(value);
                                source.setRussianName(value);
                                source.setUzbekName(value);
                                source.setType(Constants.PREDEFINED);
                                source.setFormId(FORM_NAME);
                                source.setParent(finalCustomFormLocalization);
                                customFormLocalizationManager.create(source);
                            }
                        }
                    }
                }

                if (cCFs.getListeners() != null && cCFs.getListeners().size() > 0) {
                    clonedCustomField.getListeners().addAll(cCFs.getListeners());
                } else {
                    clonedCustomField.setListeners(null);
                }

                if (cCFs.getValidations() != null && cCFs.getValidations().size() > 0) {
                    clonedCustomField.getValidations().addAll(cCFs.getValidations());
                } else {
                    clonedCustomField.setValidations(null);
                }

                if (cCFs.getAllowedRoles() != null && cCFs.getAllowedRoles().size() > 0) {
                    clonedCustomField.getAllowedRoles().addAll(cCFs.getAllowedRoles());
                } else {
                    clonedCustomField.setAllowedRoles(null);
                }

                if (cCFs.getEditFieldRoles() != null && cCFs.getEditFieldRoles().size() > 0) {
                    clonedCustomField.getEditFieldRoles().addAll(cCFs.getEditFieldRoles());
                } else {
                    clonedCustomField.setEditFieldRoles(null);
                }

                clonedCustomField.setEntityCategoryName(FORM_NAME);
                clonedCustomField.setCustomFormlocalization(customFormLocalization);
                companyCFSettingsManager.create(clonedCustomField);

            });
        }

        if ((isNew || this.modelManager.get(customForm.getFormID()) == null) && !item.isCopy()) {
            ModelForm model = new ModelForm();
            model.setFormID(FORM_NAME);
            model.setTitle(item.getName());
            model.setViewName(Constants.CUSTOM_VIEW + NAME);
            model.setActive(true);
            model.setCustomForm(true);
            model.setCustom(true);
            model.setQuizForm(item.isQuizForm());
            model.setAnonymousForm(item.isAnonymousForm());

            this.allInOneServiceLocal.saveModelForm(model);
        } else {
            EdsModel edsModel = this.modelManager.get(FORM_NAME);
            if (edsModel != null) {
                edsModel.setQuizForm(item.isQuizForm());
                edsModel.setAnonymousForm(item.isAnonymousForm());
                this.modelManager.createOrUpdate(edsModel);
            }
        }

        this.customFormManager.update(customForm);

        return FORM_NAME;
    }

    @Override
    public ListResult<FormItems> getCustomFormItems(ListingFilterParameter fp) {
        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCustomFormItems.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(CommonServiceImpl.log, kpiLog, "Get custom form item list");
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        if (panelTools == null) {
            panelTools = new ListPanelToolRpc();
            ArrayList<String> columnCodenames = new ArrayList<>();
            columnCodenames.add(FormItems.CREATER);
            columnCodenames.add(FormItems.CREATED_DATE);
            columnCodenames.add(FormItems.UPDATER);
            columnCodenames.add(FormItems.UPDATED_DATE);
            panelTools.setColumnCodeName(columnCodenames);
        }
        ArrayList<CompanyCustomFieldItem> customFieldItems = this.getCompanyCategoryCustomFields(fp.getParentID());
        if (panelTools.isCustomFieldsShown()) {
            fp.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(customFieldItems);
        }
        if (fp.getListPanelTool() == null) {
            fp.setListPanelTool(panelTools);
        }

        FacetFilterRpc facetFilter = fp.getFacetFilter();
        if (facetFilter != null && !facetFilter.isFilterChanges()) {
            facetFilter = this.getUserFacetFilter(facetFilter);
        }

        EdsUser edsUser = this.employeeManager.getUser();
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrCustomFormConst.FIELD_COMPANY_ID).append(":").append(edsUser.getCompany().getObjectID());
        solrQuery.append(" AND ");
        solrQuery.append(SolrCustomFormConst.FIELD_ITEM_ID).append(":").append(fp.getParentID());
        solrQuery.append(" AND ");
        solrQuery.append(SolrCustomFormConst.FIELD_DOC_TYPE).append(":").append(SolrCustomFormConst.CUSTOM_FORM_SOLR_DOC);

        List<String> customAccessRoles = this.rolePermissionManager.getRolesByPermissionCode(fp.getForm() + "_FULL_LIST_" + edsUser.getCompany().getObjectID());
        boolean hasCustomFullAccessToListing = !customAccessRoles.isEmpty() && edsUser.hasEitherRoles(customAccessRoles.toArray(new String[]{}));

        List<String> customSeeOwnAccessRoles = this.rolePermissionManager.getRolesByPermissionCode(fp.getForm() + "_SEE_OWN_" + edsUser.getCompany().getObjectID());
        boolean hasCustomSeeOwnAccessToListing = !customSeeOwnAccessRoles.isEmpty() && edsUser.hasEitherRoles(customSeeOwnAccessRoles.toArray(new String[]{}));

        List<String> customSeeByTypeRoles = this.rolePermissionManager.getRolesByPermissionCode(fp.getForm() + "_SEE_BY_TYPE_" + edsUser.getCompany().getObjectID());
        boolean hasCustomSeeByTypeAccessToListing = !customSeeByTypeRoles.isEmpty() && edsUser.hasEitherRoles(customSeeByTypeRoles.toArray(new String[]{}));

        String clientIDsStr = "";
        if (fp.getLookUpBy() != null && fp.getEntityID() != null && ("CRM_ACCOUNT".equals(fp.getLookUpBy()) || CustomFieldLookUpTypeEnum.CUSTOMER.equals(fp.getLookUpBy()) || CustomFieldLookUpTypeEnum.SUPPLIER.equals(fp.getLookUpBy()))) {
            EdsCrmAccount crmAccount = this.crmAccountManager.get(fp.getEntityID());
            hasCustomSeeOwnAccessToListing = hasCustomSeeOwnAccessToListing && crmAccount.getOwners().contains(edsUser);
        }

        if (hasCustomSeeOwnAccessToListing && !edsUser.hasRole(EdsRole.ADMIN_CODE)) {
            List<Integer> clientIDs = this.crmAccountManager.getAccountIDsByOwner(edsUser.getObjectID());
            if (clientIDs != null && !clientIDs.isEmpty()) {
                clientIDsStr = clientIDs.stream().map(clientID -> " " + clientID).collect(Collectors.joining());
            }
        }

        ArrayList<String> crmAccountColumnCodes = new ArrayList<>();
        if (!customFieldItems.isEmpty() && !clientIDsStr.trim().isEmpty()) {
            for (CompanyCustomFieldItem companyCustomFieldItem : customFieldItems) {
                if (companyCustomFieldItem != null && Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType())
                        && companyCustomFieldItem.isShowInListing()
                        && (CustomFieldLookUpTypeEnum.CUSTOMER.equals(companyCustomFieldItem.getLookUpTypeEnum())
                        || CustomFieldLookUpTypeEnum.SUPPLIER.equals(companyCustomFieldItem.getLookUpTypeEnum()))) {
                    crmAccountColumnCodes.add(companyCustomFieldItem.getColumnCode());
                }
            }
        }
        List<Integer> relatedIds = relationManager.getCustomFormForCurrentUser(userManager.getUser().getObjectID(), fp.getForm());

        if (!hasCustomFullAccessToListing) {
            if (!clientIDsStr.trim().isEmpty() && crmAccountColumnCodes != null && crmAccountColumnCodes.size() > 0) {
                solrQuery.append(" AND ( ").append(SolrCustomFormConst.FIELD_CREATOR_ID).append(":").append(edsUser.getObjectID());
                solrQuery.append(" OR ").append(SolrCustomFormConst.FIELD_CURRENT_APPROVER_ID).append(":").append(edsUser.getObjectID());
                for (String columnCode : crmAccountColumnCodes) {
                    solrQuery.append(" OR ").append(columnCode).append(":").append("(").append(clientIDsStr.trim()).append(") ");
                }
            } else {
                solrQuery.append(" AND ((").append(SolrCustomFormConst.FIELD_CREATOR_ID).append(":").append(edsUser.getObjectID()).append(") ");
                solrQuery.append(" OR (").append(SolrCustomFormConst.FIELD_CURRENT_APPROVER_ID).append(":").append(edsUser.getObjectID()).append(") ");
            }
            if (hasCustomSeeOwnAccessToListing && relatedIds != null && relatedIds.size() > 0) {
                for (Integer relatedId : relatedIds) {
                    solrQuery.append(" OR (").append(SolrCustomFormConst.FIELD_OBJECT_ID).append(":").append(relatedId).append(") ");
                }
            }
            List<String> seeRelatedFields = getSeeRelatedCompanyCategoryCustomFields(fp.getParentID());
            if (seeRelatedFields != null && !seeRelatedFields.isEmpty()) {
                for (String columnCode : seeRelatedFields) {
                    solrQuery.append(" OR (").append(columnCode).append(":\"").append(edsUser.getFullName()).append("\") ");
                }
            }

            Map<CustomFieldLookUpTypeEnum, List<String>> cfMap = customFieldItems.stream().filter(cf -> cf.isUseInPermission() != null && cf.isUseInPermission() &&
                            cf.getLookUpTypeEnum() != null && (cf.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.DEPARTMENT) ||
                            cf.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.LOCATION) || cf.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.POSITION)))
                    .collect(Collectors.groupingBy(cf -> cf.getLookUpTypeEnum(), Collectors.mapping(cf -> cf.getColumnCode(), Collectors.toList())));
            Integer locationId = edsUser.getLocation() != null ? edsUser.getLocation().getObjectID() : null;
            Integer positionId = edsUser.getEmployee().getPosition() != null ? edsUser.getEmployee().getPosition().getObjectID() : null;
            Integer departmentId = edsUser.getEmployee().getEmployeeDepartment() != null ? edsUser.getEmployee().getEmployeeDepartmentId() : null;
            if (hasCustomSeeByTypeAccessToListing && cfMap != null && !cfMap.isEmpty()) {
                for (CustomFieldLookUpTypeEnum type : cfMap.keySet()) {
                    if ((type.equals(CustomFieldLookUpTypeEnum.POSITION) && positionId != null) || (type.equals(CustomFieldLookUpTypeEnum.DEPARTMENT) && departmentId != null) ||
                            (type.equals(CustomFieldLookUpTypeEnum.LOCATION) && locationId != null)) {
                        List<String> columnCodes = cfMap.get(type);
                        if (columnCodes != null && !columnCodes.isEmpty()) {
                            for (String column : columnCodes) {
                                solrQuery.append(" OR (").append(column).append(":").append(type.equals(CustomFieldLookUpTypeEnum.POSITION) ? positionId :
                                        type.equals(CustomFieldLookUpTypeEnum.DEPARTMENT) ? departmentId : locationId).append(") ");
                            }
                        }
                    }
                }
            }
            solrQuery.append(") ");
        }

        ArrayList<String> columnCodes = new ArrayList<>();
        if (fp.getLookUpBy() != null && fp.getEntityID() != null) {
            if (!customFieldItems.isEmpty()) {
                for (CompanyCustomFieldItem companyCustomFieldItem : customFieldItems) {
                    if (companyCustomFieldItem.getLookUpTypeEnum() != null && companyCustomFieldItem.isAddTab()) {
                        if ("CRM_ACCOUNT".equals(fp.getLookUpBy()) && (CustomFieldLookUpTypeEnum.SUPPLIER.equals(companyCustomFieldItem.getLookUpTypeEnum()) || CustomFieldLookUpTypeEnum.CUSTOMER.equals(companyCustomFieldItem.getLookUpTypeEnum()))) {
                            columnCodes.add(companyCustomFieldItem.getColumnCode());
                        } else if (fp.getLookUpBy().equals(companyCustomFieldItem.getLookUpTypeEnum().name())) {
                            columnCodes.add(companyCustomFieldItem.getColumnCode());
                        }
                    }
                }
            }
            if (panelTools.getColumnCodeName().isEmpty()) {
                panelTools.setColumnCodeName(columnCodes);
            }


            if (!columnCodes.isEmpty()) {

                solrQuery.append(" AND +{!parent which=" + SolrCustomFormConst.FIELD_DOC_TYPE + ":" + SolrCustomFormConst.CUSTOM_FORM_SOLR_DOC + " v='+" +
                        "((" + SolrCustomFormConst.FIELD_CUSTOM_FIELD_KEY + ":" + columnCodes.get(0).toUpperCase() + " AND " + SolrCustomFormConst.FIELD_CUSTOM_FIELD_VALUE + ":" + "\"" + fp.getEntityID() + "\"").append(")");
                if (columnCodes.size() > 1) {
                    for (int i = 1; i < columnCodes.size(); i++) {
                        solrQuery.append(" OR (").append(SolrCustomFormConst.FIELD_CUSTOM_FIELD_KEY + ":" + columnCodes.get(i).toUpperCase() + " AND " + SolrCustomFormConst.FIELD_CUSTOM_FIELD_VALUE + ":" + "\"" + fp.getEntityID() + "\"").append(")");
                    }
                }

                solrQuery.append(") '} ");
            }

        }


        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            SolrSearchUtils searchUtils = new SolrSearchUtils();
            if (!StringUtils.isBlank(fp.getColumnCode())) {
                Map<String, Double> customMap = new HashMap<>();
                customMap.put(fp.getColumnCode(), SolrSearchUtils.HIGH_PRIORITY);
                customMap.putAll(QueryBuilderForSolr.getDynSearchFields());
                searchUtils.generateAndSearchQuery(solrQuery, customMap, fp.getSearchKey());
            } else {
                solrQuery.append(" AND (").append(SolrCustomFormConst.FIELD_COMPOSITE).append(":").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey()));
                searchUtils.generateSearchQueryForCustom(solrQuery, QueryBuilderForSolr.getDynSearchFields(), fp.getSearchKey());
                solrQuery.append(")");
            }

        }

        HashMap<String, FacetContentRpc> facetContentRpcHashMap = facetFilter != null && facetFilter.getFacetContentMap() != null ? facetFilter.getFacetContentMap() : new HashMap<>();
        customFieldItems.stream().filter(customFieldItem -> Constants.UI_TYPE_LOOKUP.equals(customFieldItem.getUiType())
                        && (CustomFieldLookUpTypeEnum.DEPARTMENT.equals(customFieldItem.getLookUpTypeEnum()) || CustomFieldLookUpTypeEnum.POSITION.equals(customFieldItem.getLookUpTypeEnum())))
                .peek(item -> {
                    FacetContentRpc facetContentRpc = facetContentRpcHashMap.get(item.getColumnCode());
                    if (facetContentRpc != null && facetContentRpc.getFacetItems() != null) {
                        Arrays.stream(facetContentRpc.getFacetItems()).peek(selectItem -> selectItem.setName(String.valueOf(selectItem.getId()))).collect(Collectors.toList());
                    }
                }).collect(Collectors.toList());
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNAForCF(facetFilter, edsUser.getCompany(), customFieldItems));
//        ListResult<FormItems> resultList = this.getCustomFormItemResponse(fp, solrQuery.toString());
//        setTotalScoresToQuizFormItems(fp.getForm(), resultList.getList());
        return this.getCustomFormItemResponse(fp, solrQuery.toString());
    }

    private void setTotalScoresToQuizFormItems(String formId, ArrayList<FormItems> customFormItemsList) {
        EdsCustomForm customForm = this.customFormManager.findByFormID(formId);
        if (!customForm.getQuiz()) {
            return;
        }
        EdsModel model = modelManager.get(formId);
        BigDecimal total = BigDecimal.ZERO;
        List<EdsCompanyCustomFieldsSettings> fields = this.companyCFSettingsManager.getCompanyCustomFieldsWithCategory(ViewName.CustomFormItems.name(), model.getViewName());
        for (EdsCompanyCustomFieldsSettings customField : fields) {
            Map<String, Double> map = null;
            if (UI_TYPE_CHECKBOX.equals(customField.getUiType()) || UI_TYPE_DROPDOWN.equals(customField.getUiType()) || UI_TYPE_RADIOBUTTON.equals(customField.getUiType())) {
                map = createMapFromQuizFormScoreValues(customField.getQuizFormScoreValues());
            }
            if (map != null) {
                for (Double value : map.values()) {
                    try {
                        total = total.add(new BigDecimal(value));
                    } catch (Exception ex) {
                        total = total.add(BigDecimal.ZERO);
                    }
                }
            }
        }

        for (FormItems formItem : customFormItemsList) {
            EdsCustomQuizFormScore score = customQuizFormManager.getQuizFormScore(formId, formItem.getObjectID());
            BigDecimal result = score.getTotalScore();
            StringBuilder str = new StringBuilder();
            BigDecimal percentage = BigDecimal.ZERO;
            str.append((int) result.doubleValue()).append("/").append(total).append("  ");
            if (!total.equals(BigDecimal.ZERO)) {
                percentage = result.divide(total, 2, RoundingMode.CEILING).multiply(new BigDecimal(100));
            }
            str.append("(").append(percentage);
            str.append("%)");
            formItem.setQuizResult(str.toString());
        }
    }


    @Override
    public ArrayList<SelectItem> getDynamicCustomForms(String lookUpType, boolean isCrmAccount) {
        ArrayList<SelectItem> items = new ArrayList<>();
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        List<EdsCompanyCustomFieldsSettings> companyCustomFields = this.companyCFManager.getCompanyCustomFieldsByLookUpType(lookUpType, isCrmAccount, true);
        if (!companyCustomFields.isEmpty()) {
            for (EdsCompanyCustomFieldsSettings companyCustomFieldsSettings : companyCustomFields) {
                String formId = companyCustomFieldsSettings.getEntityCategoryName().replaceFirst(Constants.CUSTOM_VIEW, "") + "_FORM";
                EdsCustomForm customForm = this.customFormManager.findByFormID(formId);
                if (ServerUtils.hasPermission(customForm.getFormID() + "_" + companyID)) {
                    SelectItem selectItem = new SelectItem();
                    selectItem.setId(customForm.getObjectID());
//                    selectItem.setName(customForm.getProperty().getObjectName());
                    selectItem.setDescription(customForm.getProperty().getShortcut());
                    selectItem.setCategory(customForm.getProperty().getPlural());
                    selectItem.setEntityId(customForm.getProperty().getFid());
                    selectItem.setCode(customForm.getFormID());
                    if (!items.contains(selectItem)) {
                        items.add(selectItem);
                    }
                }
            }
        }

        return items;
    }

    @Override
    public void saveCustomFormItemCellValue(FormItems rowValue, String columnCodeName) {

        EdsCustomFormItems customFormItems = this.customFormItemManager.get(rowValue.getObjectID());
        try {
            customFormItems.setLastChanges("");
            EdsCustomFormCustomFields customFields = customFormItems.getFormCustomFields();
            if (customFields == null) {
                customFields = new EdsCustomFormCustomFields();
                this.customFormCFManager.create(customFields);
                customFormItems.setFormCustomFields(customFields);
            }

            if (rowValue.getCustomFieldsMap() != null && rowValue.getCustomFieldsMap().size() > 0) {
                StringBuilder changesBuilder = new StringBuilder();
                for (String cit : rowValue.getCustomFieldsMap().keySet()) {
                    changesBuilder.append(customFormItems.getFormCustomFields() != null && CustomFieldsUtils.getObjectValue(customFormItems.getFormCustomFields(), cit) != null ? this.getChanges(CustomFieldsUtils.getObjectValue(customFormItems.getFormCustomFields(), cit), rowValue.getCustomFieldsMap().get(cit), cit) : (cit + ","));
                }
                String changes = changesBuilder.toString();
                if (!"".equals(changes)) {
                    customFormItems.addCustomFieldChanges(changes);
                }
            }

            customFormItems.getAuditInfo().setModificationDate(new Date());
            customFormItems.getAuditInfo().setModifiedBy(userManager.getUser());

            CustomFieldsUtils.setDomenObjectFieldChange(customFields, rowValue.getCustomFieldsMap(), columnCodeName);
            this.customFormItemManager.createOrUpdate(customFormItems);
            try {
                customFormItemSolrComponent.index(customFormItems);
            } catch (IOException | SolrServerException e) {
                e.printStackTrace();
            }
            EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, customFormItems, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_CUSTOM_FORM_ITEM);
        } catch (Exception e) {
            CommonServiceImpl.log.error("Custom Form Item List Edit Cell Column Code :" + columnCodeName, e);
        }
    }

    @Override
    public void deleteZoomCall(Integer zoomId, String url) {
        zoomMeetingManager.deleteMeetingById(zoomId);
        EdsEvent event = new EdsEvent();
        event.setDescription(url);
        zoomService.deleteMeeting(event);
    }

    @Override
    public SelectItem[] getCustomFormItemsForLookUp(String form_id) {


        EdsCompanyCustomFieldsSettings cfsItem = this.companyCFManager.getCompanyCustomFieldColumnCode("CustomFormItems", form_id, "AutoNumber");
        EdsCustomForm customForm = this.customFormManager.findByFormID(form_id);

        if (customForm != null) {
            if (cfsItem != null) {
                List<Object[]> customFormItems = this.customFormItemManager.getCustomFormItemsByFormId(customForm.getObjectID(), cfsItem);
                List<SelectItem> items = new ArrayList<>();
                for (int i = 0; i < customFormItems.size(); i++) {
                    SelectItem item = new SelectItem();
                    item.setId((Integer) customFormItems.get(i)[0]);

                    if (customFormItems.get(i)[1] != null) {
//                        autoNumber = codeFormat.format(((Double) (customFormItems.get(i)[1])).intValue());
                        item.setName((String) customFormItems.get(i)[1]);
                    }
                    items.add(item);
                }

                return items.toArray(new SelectItem[]{});
            } else {
                List<Integer> customFormItems = this.customFormItemManager.getCustomFormItemsByFormId(customForm.getObjectID());
                List<SelectItem> items = new ArrayList<>();
                for (int i = 0; i < customFormItems.size(); i++) {
                    SelectItem item = new SelectItem();
                    item.setId(customFormItems.get(i));
                    item.setName(customForm.getName() + ": " + customFormItems.get(i));
                    items.add(item);
                }

                return items.toArray(new SelectItem[]{});
            }
        }
        return null;
    }

    private ListResult<FormItems> getCustomFormItemResponse(ListingFilterParameter filterParameter, String solrQuery) {
        Page<CustomFormItemSolrDoc> customFormItemSolrDocs = customFormItemSolrComponent.getList(filterParameter, solrQuery);

        ArrayList<FormItems> itemList = new ArrayList<>();
        int totalNumber = 0;
        boolean isAnonymous = false;
        EdsCustomForm customForm = customFormManager.findByFormID(filterParameter.getValueMap().get("FORM"));
        if (customForm.isAnonymous()) {
            isAnonymous = true;
        }
        if (customFormItemSolrDocs == null) {
            return new ListResult<>(itemList, totalNumber);
        }
        ListPanelToolRpc panelSettings = filterParameter.getListPanelTool();
        totalNumber = (int) customFormItemSolrDocs.getTotalElements();
        for (CustomFormItemSolrDoc relevantDoc : customFormItemSolrDocs) {
            FormItems item = new FormItems();
            Integer id = relevantDoc.getObjectId();
            item.setObjectID(id);
            item.setAnonymous(isAnonymous);
            item.setCreator(relevantDoc.getCreatorName());
            item.setCreatedDate(relevantDoc.getCreatedDate());
            item.setUpdater(relevantDoc.getUpdaterName());
            item.setModifiedData(relevantDoc.getUpdatedDate());
            item.setFormID(relevantDoc.getFormId());
            item.setFormName(relevantDoc.getFormName());
            item.setStatus(relevantDoc.getStatusName());
            item.setCurrentApproverName(relevantDoc.getCurrentApproverName());
            item.setCustomFieldsMap(getLocaledCustomFiledMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(relevantDoc, panelSettings.getColumnCodeName()), panelSettings.getListViewCustomFields()));
            itemList.add(item);
        }
        return new ListResult<>(itemList, totalNumber);
    }

    private SolrQuery getCustomFormSolrQuery(ListingFilterParameter filterParameter, String solrQuery) {
        SolrQuery query = new SolrQuery();
        query.setFilterQueries(SolrEmployeeRepresenter.FIELD_COMPANY_ID + ":" + SecurityContext.getCompanyID());
        query.setQuery(solrQuery);
        query.setStart(filterParameter.getStart());
        query.setParam(CommonParams.ROWS, filterParameter.getLimit() > 0 ? String.valueOf(filterParameter.getLimit()) : "50");
        if (!filterParameter.isSearchButton()) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                boolean desc = !filterParameter.isAscending();
                SolrQuery.ORDER order = desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc;
                if (FormItems.UPDATED_DATE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCustomFormConst.FIELD_UPDATED_DATE, order);
                } else if (FormItems.CREATED_DATE.equals(filterParameter.getSortField())) {
                    query.setSort(SolrCustomFormConst.FIELD_CREATED_DATE, order);
                } else if (FormItems.UPDATER.equals(filterParameter.getSortField())) {
                    query.setSort("SORTABLE_" + SolrCustomFormConst.FIELD_UPDATER_NAME, order);
                } else if (FormItems.CREATER.equals(filterParameter.getSortField())) {
                    query.setSort("SORTABLE_" + SolrCustomFormConst.FIELD_CREATOR_NAME, order);
                } else if (FormItems.STATUS.equals(filterParameter.getSortField())) {
                    query.setSort("SORTABLE_" + SolrCustomFormConst.FIELD_STATUS_NAME, order);
                } else if (FormItems.APPROVER.equals(filterParameter.getSortField())) {
                    query.setSort("SORTABLE_" + SolrCustomFormConst.FIELD_CURRENT_APPROVER_NAME, order);
                } else {
                    CustomFieldsUtils.setCustomFieldsSortableNameToSolr(filterParameter.getSortField(), desc, query, true);
                }
            } else {
                query.setSort(SolrCustomFormConst.FIELD_CREATED_DATE, SolrQuery.ORDER.desc);
            }
        } else {
            query.setSort(SolrCustomFormConst.FIELD_UPDATED_DATE, SolrQuery.ORDER.desc);
        }
        return query;
    }

    @Override
    public Integer saveCustomFormItem(FormItems item) {

        EdsCustomForm customForm = this.customFormManager.findByFormID(item.getFormID());
        if (item.getAttempt() != null) {
            customForm.setAttempt(item.getAttempt());
        }
        EdsCustomFormItems edsItem = null;
        boolean isEdit = false;
        if (item.getObjectID() != null && !item.isCopy()) {
            isEdit = true;
            edsItem = this.customFormItemManager.get(item.getObjectID());
        }
        if (edsItem == null) {
            edsItem = new EdsCustomFormItems();
            edsItem.setCustomForm(customForm);
        }
        if (item.getTimerStartedAt() != null) {
            log.info("Testing started at: " + item.getTimerStartedAt());
        }
        if (item.getDurationTime() != null) {
            edsItem.setDurationTime(item.getDurationTime());
            log.info("It took to solve tests for user: " + item.getDurationTime());
        }
        edsItem.setLastChanges("");
        EdsUser user = this.userManager.getUser();
        EdsAuditInfo info = edsItem.getAuditInfo();
        info.setModificationDate(new Date());
        info.setModifiedBy(user);
        if (edsItem.isNew()) {
            info.setCreationDate(new Date());
            info.setCreatedBy(user);
        }
        edsItem.setAuditInfo(info);
        edsItem.setRelationId(item.getRelationId());
        edsItem.setRelationObjectKey(item.getRelationObjectKey());
        edsItem.setRelationType(item.getRelationType());

        if (item.getCustomFieldItems() != null && !item.getCustomFieldItems().isEmpty()) {
            StringBuilder changesBuilder = new StringBuilder();
            for (CompanyCustomFieldItem cit : item.getCustomFieldItems()) {
                changesBuilder.append(edsItem.getFormCustomFields() != null && CustomFieldsUtils.getObjectValue(edsItem.getFormCustomFields(), cit.getColumnCode()) != null ? this.getChanges(CustomFieldsUtils.getObjectValue(edsItem.getFormCustomFields(), cit.getColumnCode()), cit) : (""));
            }
            String changes = changesBuilder.toString();
            if (!"".equals(changes)) {
                edsItem.addCustomFieldChanges(changes);
            }
        }
        edsItem.setFormCustomFields(this.saveCustomFields(edsItem.getFormCustomFields(), item.getCustomFieldItems()));

        saveCustomQuizFormValues(customForm.getQuiz(), item.getCustomFieldItems(), item.getFormID(), edsItem);
        //save status for GTL when it doesnt have approvers
        if (StringUtils.isNotBlank(item.getStatusCode()) && !isOk(item.getApprovers())) {
            edsItem.setEntityStatus(referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, item.getStatusCode()));
        }

//        edsItem.getItemTables().clear();
        if (edsItem.getObjectID() != null) {
            this.customItemTableManager.deleteItems(edsItem.getObjectID());
        }

        for (Map.Entry<String, ArrayList<CustomTableRpc>> map : item.getTableItems().entrySet()) {
            List<CustomTableRpc> values = map.getValue();

            for (CustomTableRpc rpc : values) {
                EdsCustomItemTable customItemTable = new EdsCustomItemTable();
                customItemTable.setUuid(map.getKey());
                customItemTable.setSorder(rpc.getSorder());
                customItemTable.setName(rpc.getItemName());
                customItemTable.setDescription(rpc.getDescription());
                customItemTable.setCustomFields(this.saveCustomFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()));
                if (customItemTable.getCustomFields() != null) {
                    edsItem.addItemTable(customItemTable);
                }
            }
        }
        this.customFormItemManager.createOrUpdate(edsItem);
        saveCustomFormItemApprovers(edsItem, item);

        if (item.getRelationId() != null && item.getRelationType() != null) {
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_CUSTOM_FORM_ITEM, edsItem.getObjectID(), edsItem.getName(), new ArrayList<>(Collections.singletonList(new RelationItem(null, item.getRelationId(), item.getRelationType(), null, null, null, null))));
        }

        try {
            customFormItemSolrComponent.index(edsItem);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }

        KpiLog kpiLog = ServerSecurityContext.getInstance().getKpiLog();
        if (isEdit) {
            this.baseEventPostProcessor.registerEvent(CustomFormItemsEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsItem, user);

            EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsItem, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_CUSTOM_FORM_ITEM);

            kpiLog.setEntityName(EdsCustomFormItems.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(edsItem.getObjectID());
            ServerUtils.kpiLog(CommonServiceImpl.log, kpiLog, "Update custom form item");
        } else {
            this.baseEventPostProcessor.registerEvent(CustomFormItemsEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsItem, user);

            if (Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED.equals(item.getStatusCode())) {
                this.baseEventPostProcessor.registerEvent(CustomFormItemsEventListenerImpl.TYPE, CustomFormItemsEventListenerImpl.EVENT_CUSTOM_FROM_ITEM_SUBMITTED_TO_MANAGER, edsItem, user);
            }
            EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, edsItem, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_CUSTOM_FORM_ITEM);

            kpiLog.setEntityName(EdsCustomFormItems.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.ADD);
            kpiLog.setEntityId(edsItem.getObjectID());
            ServerUtils.kpiLog(CommonServiceImpl.log, kpiLog, "Add custom form item");
        }
        if (Constants.CUSTOM_FORM_ITEM_STATUS_APPROVED.equals(item.getStatusCode())) {
            this.baseEventPostProcessor.registerEvent(CustomFormItemsEventListenerImpl.TYPE, CustomFormItemsEventListenerImpl.EVENT_CUSTOM_FROM_ITEM_MANAGER_APPROVE, edsItem, this.userManager.getUser());
        } else if (Constants.CUSTOM_FORM_ITEM_STATUS_REJECTED.equals(item.getStatusCode())) {
            this.baseEventPostProcessor.registerEvent(CustomFormItemsEventListenerImpl.TYPE, CustomFormItemsEventListenerImpl.EVENT_CUSTOM_FROM_ITEM_MANAGER_REJECT, edsItem, this.userManager.getUser());
        }
        if (isOk(item.getApprovers()) && !Constants.CUSTOM_FORM_ITEM_STATUS_DRAFT.equals(item.getStatusCode())) {
            EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, WorkflowExecutionCriteriaEnum._WORKFLOW_ACTION_APPROVING.name(), edsItem, this.userManager.getUser());
            workflowEvent.setEntityType(RelationItem.TYPE_CUSTOM_FORM_ITEM);
        }
        return edsItem.getObjectID();
    }

    /*
     * saveCustomQuizFormValues()
     * If custom form was be quiz form this method
     * calculate total score of employee
     */
    private void saveCustomQuizFormValues(Boolean isQuiz, ArrayList<CompanyCustomFieldItem> customFieldItems, String formId, EdsCustomFormItems customFormItem) {
        if (isQuiz != null && isQuiz) {
            EdsCustomQuizFormScore formScore = customFormItem.getObjectID() != null ? Optional.ofNullable(customQuizFormManager.getQuizFormScore(formId, customFormItem.getObjectID())).orElse(new EdsCustomQuizFormScore()) : new EdsCustomQuizFormScore();
            formScore.setCustomFormId(formId);
            EdsModel model = modelManager.get(formId);
            formScore.setEmployee(userManager.getUser().getEmployee());
            BigDecimal total = BigDecimal.ZERO;
            boolean hasWrongAnswer = false;
            for (CompanyCustomFieldItem item : customFieldItems) {
                String entityCategoryName = item.getEntityCategoryName() != null ? item.getEntityCategoryName() : model.getViewName();
                EdsCompanyCustomFieldsSettings customFields = companyCFManager.getCompanyCustomField(ViewName.CustomFormItems.name(), entityCategoryName, item.getColumnCode());
                if (customFields != null) {
                    BigDecimal subTotal = BigDecimal.ZERO;
                    Map<String, Double> map = createMapFromQuizFormScoreValues(customFields.getQuizFormScoreValues());
                    if (UI_TYPE_CHECKBOX.equals(customFields.getUiType())) {
                        String[] strs = item.getFieldStringValue() != null ? item.getFieldStringValue().split("-:-") : new String[]{};
                        for (String value : strs) {
                            try {
                                BigDecimal val = BigDecimal.valueOf(map.get(value));
                                if (BigDecimal.ZERO.equals(val)) {
                                    hasWrongAnswer = true;
                                }
                                subTotal = subTotal.add(BigDecimal.valueOf(map.get(value)));
                            } catch (Exception ex) {
                                subTotal = subTotal.add(BigDecimal.ZERO);
                            }
                        }
                    } else if (UI_TYPE_DROPDOWN.equals(customFields.getUiType()) || UI_TYPE_RADIOBUTTON.equals(customFields.getUiType())) {
                        try {
                            subTotal = subTotal.add(BigDecimal.valueOf(map.get(item.getFieldStringValue())));
                        } catch (Exception ex) {
                            subTotal = subTotal.add(BigDecimal.ZERO);
                        }
                    }
                    if (!hasWrongAnswer) {
                        total = total.add(subTotal);
                    } else {
                        hasWrongAnswer = false;
                    }
                }
            }
            formScore.setTotalScore(total);
            formScore.setCreatedDate(new Date());
            formScore.setCustomFormItemId(customFormItem);
            this.customQuizFormManager.createOrUpdate(formScore);
        }
    }

    private Map<String, Double> createMapFromQuizFormScoreValues(String quizFormScoreValues) {
        Map<String, Double> splitedValues = new HashMap<>();
        if (quizFormScoreValues != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(quizFormScoreValues);
                for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
                    String key = it.next().toString();
                    splitedValues.put(key, jsonObject.getDouble(key));
                }
            } catch (Exception ex) {
                ex.getStackTrace();
            }
        } else {
            splitedValues = new HashMap<>();
        }
        return splitedValues;
    }

    private String getChanges(Object ob, CompanyCustomFieldItem item) {
        if (ob != null) {
            if (Constants.DATA_TYPE_TEXT.equals(item.getDataType())) {
                String text = (String) ob;
                return !text.equals(item.getFieldStringValue()) ? (item.getColumnCode() + ",") : "";
            } else if (Constants.DATA_TYPE_NUMBER.equals(item.getDataType())) {
                Double value = (Double) ob;
                return !value.equals(item.getFieldStringValue() != "" ? Double.valueOf(item.getFieldStringValue()) : 0) ? (item.getColumnCode() + ",") : "";
            } else if (Constants.DATA_TYPE_DATE.equals(item.getDataType())) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Date date = (Date) ob;
                String strDate = format.format(date);
                String strNewDate = "";
                if (item.getFieldDateNonConvertedValue() != null) {
                    strNewDate = format.format(item.getFieldDateNonConvertedValue().getNonConvertedDate());
                }
                return !strDate.equals(strNewDate) ? (item.getColumnCode() + ",") : "";
            }
        }
        return "";
    }

    private String getChanges(Object ob, Object value, String columnCodeName) {

        if (ob != null && columnCodeName != null) {
            if (columnCodeName.contains("date")) {
                Date date = (Date) ob;
                DateNonConvertable newValue = (DateNonConvertable) value;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                String dateStr = null;
                if (date != null) {
                    dateStr = format.format(date);
                }
                String newDateStr = null;
                if (newValue != null && newValue.getNonConvertedDate() != null) {
                    newDateStr = format.format(newValue.getNonConvertedDate());
                }
                return !dateStr.equals(newDateStr) ? (columnCodeName + ",") : "";
            } else {
                return !ob.equals(value) ? (columnCodeName + ",") : "";
            }
        }
        return "";
    }

    private void saveCustomFormItemApprovers(EdsCustomFormItems edsItem, FormItems item) {
        if (!isOk(item.getApprovers())) {
            return;
        }
        item.getApprovers().sort(Comparator.comparing(ApproverItemMini::getApproverOrder));
        boolean isFirstApprover = true;
        for (ApproverItemMini approverItem : item.getApprovers()) {
            EdsApprover _edsApprover = this.approverManager.get(approverItem.getClonedFrom());
            if (approverItem.getObjectID() != null) {
                if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                    EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                    _edsApprover.setExactEmployee(user_);
                }
                this.approverManager.update(_edsApprover);
                if (Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED.equals(item.getStatusCode()) && isFirstApprover) {
                    edsItem.setPrevApprover(null);
                    edsItem.setCurrentApprover(_edsApprover);
                    edsItem.getCurrentApprover().setStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, item.getStatusCode()));
                    edsItem.setEntityStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED));
                    isFirstApprover = false;
                } else if (edsItem.getCurrentApprover() != null && item.getStatusCode() != null && isFirstApprover) {
                    edsItem.getCurrentApprover().setStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, item.getStatusCode()));
                    edsItem.setEntityStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED));
                    isFirstApprover = false;
                } else if (edsItem.getCurrentApprover() != null && item.getStatusCode() != null) {
                    edsItem.getCurrentApprover().setStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED));
                }
                if (item.getStatusCode() != null && !Constants.CUSTOM_FORM_ITEM_STATUS_APPROVED.equals(item.getStatusCode())) {
                    edsItem.setEntityStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, item.getStatusCode()));
                }
                if (edsItem.isCurrentApproverRejected()) {
                    edsItem.setEntityStatus(edsItem.getCurrentApprover().getStatus());
                }
                continue;
            }

            EdsApprover edsApprover = _edsApprover.cloneShallow();
            edsApprover.setObjectID(null);
            edsApprover.setApproverHistory(new HashSet<>());
            edsApprover.setEntityID(edsItem.getObjectID());
            edsApprover.setIs_default(false);
            if (item.getStatusCode() != null && isFirstApprover && item.getApprovers().size() == 1) {
                edsApprover.setStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, item.getStatusCode()));
                edsItem.setEntityStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, item.getStatusCode()));
            } else if (item.getStatusCode() != null && isFirstApprover) {
                edsApprover.setStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, item.getStatusCode()));
                if (Constants.CUSTOM_FORM_ITEM_STATUS_DRAFT.equals(item.getStatusCode())) {
                    edsItem.setEntityStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, item.getStatusCode()));
                } else {
                    edsItem.setEntityStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED));
                }

                isFirstApprover = false;
            } else if (item.getStatusCode() != null) {
                edsApprover.setStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED));
                if (Constants.CUSTOM_FORM_ITEM_STATUS_DRAFT.equals(item.getStatusCode())) {
                    edsItem.setEntityStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, item.getStatusCode()));
                } else {
                    edsItem.setEntityStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED));
                }
            }
            if (approverItem.getExactEmployee() != null && approverItem.getExactEmployee().getId() != null) {
                EdsUser user_ = this.userManager.get(approverItem.getExactEmployee().getId());
                edsApprover.setExactEmployee(user_);
            }
            edsApprover.setApproverRoles(new HashSet<>());
            edsApprover.setApproverEmployees(new HashSet<>());
            edsApprover.setDynamicQueries(new HashSet<>());
            this.approverManager.createOrUpdate(edsApprover);

            for (EdsApproverRoles roleapp : _edsApprover.getApproverRoles()) {
                edsApprover.getApproverRoles().add(roleapp);
            }

            for (EdsApproverEmployees ucerapp : _edsApprover.getApproverEmployees()) {
                edsApprover.getApproverEmployees().add(ucerapp);
            }

            if (edsItem.getCurrentApprover() == null) {
                edsItem.setCurrentApprover(edsApprover);
            }
            edsItem.getApprovers().add(edsApprover);
        }
    }

    @Override
    public List<HistoryNote> getCustomFormItemHistoryNotes(Integer objectID, String formId) {
        if (objectID == null) {
            return null;
        }
        if (formId == null) {
            return null;
        }
        List<HistoryNote> result = new ArrayList<>();
        HistoryListItem[] asHistoryItems = this.getAsHistoryItems(this.customFormNoteManager.getCustomFormNotes(objectID));
        if (asHistoryItems != null) {
            result.addAll(Arrays.asList(asHistoryItems));
        }
        result.addAll(this.getCustomFormMyUpdates(objectID, formId));

        return result;
    }


    private List<MyUpdateItem> getCustomFormMyUpdates(Integer objectID, String formId) {

        EdsCustomForm customForm = this.customFormManager.findByFormID(formId);
        EdsCustomFormItems edsCustomFormItems = this.customFormItemManager.get(objectID);
        EdsProperty property = customForm.getProperty();
        String shortName = "";
        if (property != null) {
            shortName = property.getShortcut();
        }

        List<EdsMyUpdate> myUpdates = this.myUpdateManager.getUpdatesForCustomForm(objectID, MyUpdateTypeManager.CUSTOM_FORM_ITEM, formId);
        List<MyUpdateItem> updates = new ArrayList<>();
        for (EdsMyUpdate myUpdate : myUpdates) {
            MyUpdateItem item = new MyUpdateItem();
            item.setType(myUpdate.getEventType());
            item.setEventDate(myUpdate.getDate());

            EdsUser enducerUser = this.userManager.get(myUpdate.getInducerID());
            EdsUser user = this.userManager.get(myUpdate.getReceiver());

            item.setUserName(enducerUser.getName());
            StringBuilder message = new StringBuilder();

            String userName = user != null ? user.getFullName() : "";
            String managerName = "";
            switch (myUpdate.getTypeCode()) {
                case MyUpdateTypeManager.CUSTOM_FORM_ITEM_MANAGER_APPROVE,
                     MyUpdateTypeManager.CUSTOM_FORM_ITEM_MANAGER_REJECT -> managerName = enducerUser.getFullName();
                case MyUpdateTypeManager.CUSTOM_FORM_ITEM_SUBMITTED_TO_MANAGER ->
                        managerName = edsCustomFormItems.getCurrentApprover() != null &&
                                edsCustomFormItems.getCurrentApprover().getExactEmployee() != null
                                ? edsCustomFormItems.getCurrentApprover().getExactEmployee().getFullName()
                                : "";
                default -> {
                }
            }
            if (!(MyUpdateTypeManager.CUSTOM_FORM_ITEM_SUBMITTED_TO_MANAGER.equals(myUpdate.getTypeCode())
                    || MyUpdateTypeManager.CUSTOM_FORM_ITEM_MANAGER_APPROVE.equals(myUpdate.getTypeCode())
                    || MyUpdateTypeManager.CUSTOM_FORM_ITEM_MANAGER_REJECT.equals(myUpdate.getTypeCode()))) {
                if (this.userManager.getUser().getName().equals(item.getUserName())) {
                    message.append("You have ");
                } else {
                    message.append(item.getUserName()).append(" has ");
                }
            }
            switch (myUpdate.getTypeCode()) {
                case MyUpdateTypeManager.CUSTOM_FORM_ITEM_ADD -> {
                    item.setSubType(MyUpdateItem.ADD);
                    message.append("added ").append(shortName);
                }
                case MyUpdateTypeManager.CUSTOM_FORM_ITEM_EDIT -> {
                    item.setSubType(MyUpdateItem.EDIT);
                    message.append("edited ").append(shortName);
                }
                case MyUpdateTypeManager.CUSTOM_FORM_ITEM_DELETE -> {
                    item.setSubType(MyUpdateItem.DELETE);
                    message.append("deleted ").append(shortName);
                }
                case MyUpdateTypeManager.CUSTOM_FORM_ITEM_SUBMITTED_TO_MANAGER -> {
                    item.setSubType(MyUpdateItem.STATUS_SUBMITED);
                    message.append(this.activityWfmMessageSource.localizeWithParam(
                            MyUpdateTypeManager.HAS_SUBMIT_CUSTOM_FROM_ITEM_TO_MANAGER, userName, shortName, managerName)
                    );
                }
                case MyUpdateTypeManager.CUSTOM_FORM_ITEM_MANAGER_APPROVE -> {
                    item.setSubType(MyUpdateItem.STATUS_APPROVED);
                    message.append(this.activityWfmMessageSource.localizeWithParam(
                            MyUpdateTypeManager.MANAGER_APPROVED_CUSTOM_FROM_ITEM, managerName, shortName)
                    );
                }
                case MyUpdateTypeManager.CUSTOM_FORM_ITEM_MANAGER_REJECT -> {
                    item.setSubType(MyUpdateItem.STATUS_REJECT);
                    message.append(this.activityWfmMessageSource.localizeWithParam(
                            MyUpdateTypeManager.REJECTED_CUSTOM_FROM_ITEM, managerName, shortName)
                    );
                }
            }
            item.setMessage(message.toString());
            updates.add(item);
        }
        return updates;
    }


    private HistoryListItem[] getAsHistoryItems(List<EdsCustomFormNote> cfNotes) {
        if (cfNotes != null && cfNotes.size() > 0) {
            HistoryListItem[] hisItems = new HistoryListItem[cfNotes.size()];
            int i = 0;
            for (EdsCustomFormNote note : cfNotes) {
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
    public Integer createCustomFormItemNote(Integer transferID, HistoryListItem hisItem) {
        if (transferID != null && hisItem != null) {
            EdsCustomFormItems customFormItems = this.customFormItemManager.get(transferID);
            EdsCustomFormNote customFormNote = new EdsCustomFormNote();
            customFormNote.setCostomFormItem(customFormItems);
            customFormNote.setComment(hisItem.getComment());
            customFormNote.setDate(new Date());
            customFormNote.setCommentator(this.userManager.getUser());
            customFormNote.setSuperUser(ServerUtils.isSuperUser());
            this.customFormNoteManager.create(customFormNote);
            return customFormNote.getObjectID();
        }
        return null;
    }

    @Override
    public Boolean deleteCustomFormItemNote(Integer noteID) {
        if (noteID != null) {
            EdsCustomFormNote customFormNote = this.customFormNoteManager.get(noteID);
            this.customFormNoteManager.delete(customFormNote);
            return true;
        }
        return false;
    }

    @Override
    public void deleteCustomFormItem(Integer objectId) {

        EdsUser user = this.userManager.getUser();

        if (objectId != null) {
            EdsCustomFormItems customFormItem = this.customFormItemManager.get(objectId);
            if (customFormItem != null) {
                FormItems item = customFormItem.toRpc();
                customFormItem.setDeleted(true);

                this.relationManager.deleteAllRelations(item.getFormID(), customFormItem.getObjectID());

                this.customFormItemManager.update(customFormItem);

                this.baseEventPostProcessor.registerEvent(CustomFormItemsEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, customFormItem, user);


                EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE,
                        BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE,
                        customFormItem, this.userManager.getUser());
                workflowEvent.setEntityType(RelationItem.TYPE_CUSTOM_FORM_ITEM);

                try {
                    this.solrManager.removeCustomFormByIds(customFormItem.getObjectID());
                } catch (IOException | SolrServerException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void memorizedCustomFormItem(Integer objectId, String formId) {

        if (objectId != null) {
            EdsCustomForm edsCustomForm = customFormManager.findByFormID(formId);
            if (edsCustomForm != null) {
                edsCustomForm.setMemorizedItemId(objectId);
                customFormManager.createOrUpdate(edsCustomForm);
            }
        }

    }

    @Override
    public LinkedHashMap<String, FormProperty> getFormProperty(String formId) {

        LinkedHashMap<String, FormProperty> fields = new LinkedHashMap<>();
        EdsFormProperty edsFormProperty = this.formPropertyManager.getByFormID(formId);
        if (edsFormProperty != null) {
            Gson gson = new Gson();
            FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
            for (FormProperty formProperty : formFields) {
                if (formProperty != null) {
                    if (formProperty.getDefaultValue() != null && formProperty.getDefaultValue().length() == 0) {
                        formProperty.setDefaultValue(null);
                    }
                    if (formProperty.getRoleEdit() != null && formProperty.getRoleEdit().size() > 0) {
                        if (this.userManager.getUser().hasEitherRoles(formProperty.getRoleEdit().toArray(new Integer[]{}))) {
                            formProperty.setDisabled(false);
                        }
                    }
                    fields.put(formProperty.getCode(), formProperty);
                }
            }
        }

        return fields;
    }

    @Override
    public String checkCustomFormAttributeCount(String fieldType, String formID) {
        if (Constants.UI_TYPE_APPROVAL_PROCESS.equals(fieldType)) {
            if (formID != null) {
                List<EdsCustomFormAttributes> edsAttributes = this.customFormAttributeManager.getAttByFormIdAndFieldType(fieldType, formID);
                if (edsAttributes != null && edsAttributes.size() > 0) {
                    return "APPROVAL_PROCESS_LIMIT";
                }
            }
        }
        return null;
    }

    @Override
    public ArrayList<CustomFormAttributeItem> getCustomFormAttributes(String formId) {
        List<EdsCustomFormAttributes> edsAttributes = this.customFormAttributeManager.getAttributesByFormId(formId);
        return edsAttributes.stream().map(EdsCustomFormAttributes::toTO).collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void approveOrRejectCustomFormItem(Integer objectId, String statusCode) {
        EdsCustomFormItems edsCustomFormItem = this.customFormItemManager.get(objectId);
        edsCustomFormItem.setLastChanges("");
        EdsReference edsReference = this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, statusCode);

        if (!Constants.CUSTOM_FORM_ITEM_STATUS_APPROVED.equals(edsReference.getCode())) {
            edsCustomFormItem.setOverallStatus(edsReference);
        } else if (Constants.CUSTOM_FORM_ITEM_STATUS_APPROVED.equals(edsReference.getCode())
                && edsCustomFormItem.getOverallStatus() != null
                && Constants.CUSTOM_FORM_ITEM_STATUS_DRAFT.equals(edsCustomFormItem.getOverallStatus().getCode())) {
            edsCustomFormItem.setOverallStatus(this.referenceManager.findReference(Constants.CUSTOM_FORM_ITEM_STATUS, Constants.CUSTOM_FORM_ITEM_STATUS_SUBMITTED));
        }
        edsCustomFormItem.updateStatus(edsReference);
        this.customFormItemManager.update(edsCustomFormItem);

        EdsUser edsUser = this.userManager.getUser();
        if (Constants.CUSTOM_FORM_ITEM_STATUS_APPROVED.equals(edsReference.getCode())) {
            this.baseEventPostProcessor.registerEvent(CustomFormItemsEventListenerImpl.TYPE, CustomFormItemsEventListenerImpl.EVENT_CUSTOM_FROM_ITEM_MANAGER_APPROVE, edsCustomFormItem, edsUser);
        } else if (Constants.CUSTOM_FORM_ITEM_STATUS_REJECTED.equals(edsReference.getCode())) {
            this.baseEventPostProcessor.registerEvent(CustomFormItemsEventListenerImpl.TYPE, CustomFormItemsEventListenerImpl.EVENT_CUSTOM_FROM_ITEM_MANAGER_REJECT, edsCustomFormItem, edsUser);
        }
        this.allInOneServiceLocal.approvedOrRejected(RelationItem.TYPE_CUSTOM_FORM_ITEM, edsCustomFormItem.getObjectID(), null);

        try {
            customFormItemSolrComponent.index(edsCustomFormItem);
        } catch (IOException | SolrServerException | InterruptedException e) {
            e.printStackTrace();
        }
        EdsBusinessEvent workflowEvent = this.baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsCustomFormItem, edsUser);
        workflowEvent.setEntityType(RelationItem.TYPE_CUSTOM_FORM_ITEM);
    }

    @Override
    public Boolean isEnableApprovers(String formId) {
        return this.approverManager.isExistApproverByEntityTypeAndStepType(formId, RelationItem.TYPE_CUSTOM_FORM_ITEM);
    }

    @Override
    public String getDynamicImageUrl(Integer id) {
        String dynamicUrl = "";
        try {
            EdsUpload upload = (EdsUpload) this.uploadManager.get(id);
            if (upload != null) {
                File folder = new File(GwtUploadServlet.realPath + "fileview/");
                folder.mkdirs();
                File file = new File(folder.getAbsolutePath() + "/" + upload.getOriginalName());
                file.createNewFile();
                FileUtils.copyInputStreamToFile(this.uploadManager.getInputStream(upload), file);
                String url = getUrlName(upload.getOriginalName());
                dynamicUrl = EdsContextParams.getHost() + "/uploads/fileview/" + url;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dynamicUrl;
    }

    private String getUrlName(String originalName) {
        return originalName
                .replace("%", "%25")
                .replace(" ", "%20")
                .replace("~", "%7E")
                .replace("!", "%21")
                .replace("`", "%60")
                .replace("#", "%23")
                .replace(";", "%3B");
    }

    private EdsCustomItemTableCF saveCustomFields(EdsCustomItemTableCF
                                                          customfField, ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (customfField == null) {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && fieldItem.getFieldStringValue().length() > 0)
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || fieldItem.getItem() != null
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                customfField = new EdsCustomItemTableCF();
                this.customItemTableCFManager.create(customfField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customfField, customFieldItems);
            return customfField;
        }
        return null;
    }

    public EdsCustomFormCustomFields saveCustomFields(EdsCustomFormCustomFields
                                                              customfField, List<CompanyCustomFieldItem> customFieldItems) {
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
                customfField = new EdsCustomFormCustomFields();
                this.customFormCFManager.create(customfField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customfField, customFieldItems);
            return customfField;
        }
        return null;
    }

    @Override
    public FormItems getCustomFormItem(Integer objectID, Integer fID, String formId, boolean isCopy, String lookUpType,
                                       Integer lookUpTypeId, String formType, Integer convertFormId) {
        FormItems item = new FormItems();
        if (objectID == null && !isCopy && formType == null) {
            EdsCustomForm edsCustomForm = customFormManager.findByFormID(formId);
            if (edsCustomForm != null && edsCustomForm.getMemorizedItemId() != null) {
                objectID = edsCustomForm.getMemorizedItemId();
                isCopy = true;

                item.setTimer(edsCustomForm.getTimer());
                item.setWelcomeMessage(edsCustomForm.getWelcomeMessage());
                item.setEndOfTimeMessage(edsCustomForm.getEndTimeMessage());

            }
        }
        if (objectID != null) {
            EdsCustomFormItems edsItem = this.customFormItemManager.get(objectID);
            item = edsItem.toRpc();

            EdsCustomQuizFormScore score = customQuizFormManager.getQuizFormScore(item.getFormID(), item.getObjectID());
            item.setScore(score != null ? score.getTotalScore() : BigDecimal.ZERO);

            Set<EdsCustomItemTable> itemTables = edsItem.getItemTables();


            if (itemTables != null || !itemTables.isEmpty()) {
                HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

                for (EdsCustomItemTable itemTable : itemTables) {
                    CustomTableRpc rpc = itemTable.getRpc();

                    rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                            this.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, rpc.getUuid())));

                    map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                }
                item.setTableItems(map);
            }
//            Map<String, List<CustomTableRpc>> tableItems = item.getTableItems();
//
//
//            for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
//                tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
//            }

            item.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(edsItem.getFormCustomFields(),
                    this.getCompanyCategoryCustomFields(fID)));

            for (CompanyCustomFieldItem customFieldItem : item.getCustomFieldItems()) {
                if (Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(customFieldItem.getUiType())) {
                    if (StringUtils.isNotBlank(customFieldItem.getFieldStringValue())) {
                        List<FileResource> fileResources = this.documentsServiceLocal.getFileResources(Constants.F_CUSTOM_FIELD_ITEM, Double.valueOf(customFieldItem.getFieldStringValue()).intValue(), customFieldItem.getObjectId());
                        ArrayList<String> urls = new ArrayList<>();
                        for (FileResource fileResource : fileResources) {
                            urls.add(this.getImageUrl(fileResource.getBodyId()));
                        }
                        customFieldItem.setFileUrls(urls);
                    }
                } else if (Constants.UI_TYPE_PROFILE_IMAGE_WIDGET.equals(customFieldItem.getUiType())) {
                    ArrayList<String> urls = new ArrayList<>();
                    if (customFieldItem.getProfielImageId() != null) {
                        urls.add(this.getImageUrl(customFieldItem.getProfielImageId()));
                    }
                    customFieldItem.setFileUrls(urls);
                } else if (Constants.UI_TYPE_LOOKUP.equals(customFieldItem.getUiType()) && customFieldItem.getLookUpTypeEnum() != null) {
                    switch (customFieldItem.getLookUpTypeEnum()) {
                        case CASE -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsCase edsCase = this.caseManager.get(customFieldItem.getSelectedId());
                                if (edsCase != null && edsCase.getDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case CONTACT, LEAD -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsCrmContact edsCrmContact = this.crmContactManager.get(customFieldItem.getSelectedId());
                                if (edsCrmContact != null && edsCrmContact.isDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case CUSTOMER, SUPPLIER -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsCrmAccount edsCrmAccount = this.crmAccountManager.get(customFieldItem.getSelectedId());
                                if (edsCrmAccount != null && edsCrmAccount.isDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case EMPLOYEE -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsEmployee employee = this.employeeManager.get(customFieldItem.getSelectedId());
                                if (employee != null && employee.getDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case OPPORTUNITY -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsOpportunity edsOpportunity = this.opportunityManager.get(customFieldItem.getSelectedId());
                                if (edsOpportunity != null && edsOpportunity.getDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case PROJECT -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsProject edsProject = this.projectManager.get(customFieldItem.getSelectedId());
                                if (edsProject != null && edsProject.getDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case PRODUCT -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsItem edsItem1 = this.itemManager.get(customFieldItem.getSelectedId());
                                if (edsItem1 != null && edsItem1.getDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case PURCHASE_INVOICE, SALES_INVOICE -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsInvoice edsInvoice = this.invoiceManager.get(customFieldItem.getSelectedId());
                                if (edsInvoice != null && edsInvoice.isDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case PURCHASE_ORDER, SALES_QUOTE -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsQuote edsQuote = this.quoteManager.get(customFieldItem.getSelectedId());
                                if (edsQuote != null && edsQuote.isDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case TASK -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsTask edsTask = this.taskManager.get(customFieldItem.getSelectedId());
                                if (edsTask != null && edsTask.getDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case PERSONAL_GOAL, DEPARTMENT_GOAL, PROJECT_GOAL, BUSINESS_GOAL, COMPANY_GOAL -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsGoal edsGoal = this.goalManager.get(customFieldItem.getSelectedId());
                                if (edsGoal != null && edsGoal.isDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case VACANCY -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsVacancy edsVacancy = this.vacancyManager.get(customFieldItem.getSelectedId());
                                if (edsVacancy != null && edsVacancy.getDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case UNIT_MEASUREMENT -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsUnitMeasurement edsUnitMeasurement = this.unitMeasurementManager.get(customFieldItem.getSelectedId());
                                if (edsUnitMeasurement != null && edsUnitMeasurement.getDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case REFERENCE -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsReference edsReference = this.referenceManager.get(customFieldItem.getSelectedId());
                                if (edsReference != null && edsReference.isDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                        case CUSTOM_FORM -> {
                            if (customFieldItem.getSelectedId() != null) {
                                EdsCustomForm edsCustomForm = this.customFormManager.get(customFieldItem.getSelectedId());
                                if (edsCustomForm != null && edsCustomForm.getDeleted()) {
                                    customFieldItem.setDeleted(true);
                                }
                            }
                        }
                    }
                }
            }

            if (item.getCustomFieldItems() != null && !item.getCustomFieldItems().isEmpty()) {
                for (int i = 0; i < item.getCustomFieldItems().size(); i++) {
                    if (Constants.UI_TYPE_AUTONUMBER.equals(item.getCustomFieldItems().get(i).getUiType()) && item.getCustomFieldItems().get(i).getFieldStringValue() != null) {
                        if (isCopy) {
                            item.getCustomFieldItems().get(i).setFieldStringValue(this.getMaxValueOfAutoNumbering(item.getCustomFieldItems().get(i)));
                            item.setAutoNumber(this.getMaxValueOfAutoNumbering(item.getCustomFieldItems().get(i)));
                        } else {
                            item.setAutoNumber(item.getCustomFieldItems().get(i).getFieldStringValue());
                        }
                    }
                }
            }
            item.setRelations(EdsRelation.asRPCs(this.relationManager.getAllRelations(item.getFormID(), edsItem.getObjectID())));
            if (isCopy) {
                item.setCurrentApproverId(null);
                item.setCurrentApproverName(null);
            } else {
                item.setCurrentApproverSelectItem(edsItem.getCurrentApprover() != null && edsItem.getCurrentApprover().getExactEmployee() != null
                        ? edsItem.getCurrentApprover().getExactEmployee().getAsSelectItem()
                        : null);
            }
        }
        if (lookUpType != null && lookUpTypeId != null && (isCopy == (objectID != null))) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();
            if (objectID != null) {
                customFieldItems = item.getCustomFieldItems();
            } else {
                customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(null,
                        this.getCompanyCategoryCustomFields(fID));
            }

            boolean isClient = false;
            boolean isSupplier = false;
            String name = "";
            switch (lookUpType) {
                case "CASE" -> {
                    EdsCase edsCase = this.caseManager.get(lookUpTypeId);
                    name = edsCase.getSubject();
                }
                case "CONTACT", "LEAD" -> {
                    EdsCrmContact crmContact = this.crmContactManager.get(lookUpTypeId);
                    name = crmContact.getName();
                }
                case "CUSTOMER", "SUPPLIER", "CRM_ACCOUNT" -> {
                    EdsCrmAccount crmAccount = this.crmAccountManager.get(lookUpTypeId);
                    name = (crmAccount.getNumber() != null && !"".equals(crmAccount.getNumber().trim()) ? crmAccount.getNumber() + " -> " : "") + crmAccount.getName();
                    isClient = crmAccount.isClient();
                    isSupplier = crmAccount.isSupplier();
                }
                case "EMPLOYEE" -> {
                    EdsEmployee employee = this.employeeManager.get(lookUpTypeId);
                    name = employee.getFullName();
                }
                case "OPPORTUNITY" -> {
                    EdsOpportunity opportunity = this.opportunityManager.get(lookUpTypeId);
                    name = opportunity.getNumber();
                }
                case "PROJECT" -> {
                    EdsProject project = this.projectManager.get(lookUpTypeId);
                    name = (project.getNumber() != null && !"".equals(project.getNumber().trim()) ? project.getNumber() + " -> " : "") + project.getName();
                }
                case "PRODUCT" -> {
                    EdsItem product = this.itemManager.get(lookUpTypeId);
                    name = ((product.getProductNumber() != null && !"".equals(product.getProductNumber())) ? product.getProductNumber() + " -> " : "") + product.getName();
                }
                case "TASK" -> {
                    EdsTask task = this.taskManager.get(lookUpTypeId);
                    name = (task.getNumber() != null && !"".equals(task.getNumber().trim()) ? task.getNumber() + " -> " : "") + task.getName();
                }
                case "CUSTOM_FORM" -> {
                    EdsCustomForm form = this.customFormManager.get(lookUpTypeId);
                    name = form.getName();
                }
            }
            String finalName = name;
            boolean finalIsSupplier = isSupplier;
            boolean finalIsClient = isClient;
            customFieldItems.forEach(companyCustomFieldItem -> {
                if (Constants.UI_TYPE_LOOKUP.equals(companyCustomFieldItem.getUiType())) {
                    if ("CRM_ACCOUNT".equals(lookUpType)) {
                        if (finalIsSupplier && finalIsClient && (CustomFieldLookUpTypeEnum.SUPPLIER.equals(companyCustomFieldItem.getLookUpTypeEnum()) || CustomFieldLookUpTypeEnum.CUSTOMER.equals(companyCustomFieldItem.getLookUpTypeEnum()))) {
                            companyCustomFieldItem.setSelectedId(lookUpTypeId);
                            companyCustomFieldItem.setFieldStringValue(finalName);
                        } else if (finalIsSupplier && CustomFieldLookUpTypeEnum.SUPPLIER.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                            companyCustomFieldItem.setSelectedId(lookUpTypeId);
                            companyCustomFieldItem.setFieldStringValue(finalName);
                        } else if (finalIsClient && CustomFieldLookUpTypeEnum.CUSTOMER.equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                            companyCustomFieldItem.setSelectedId(lookUpTypeId);
                            companyCustomFieldItem.setFieldStringValue(finalName);
                        }
                    } else if (CustomFieldLookUpTypeEnum.get(lookUpType) != null && CustomFieldLookUpTypeEnum.get(lookUpType).equals(companyCustomFieldItem.getLookUpTypeEnum())) {
                        companyCustomFieldItem.setSelectedId(lookUpTypeId);
                        companyCustomFieldItem.setFieldStringValue(finalName);
                    }
                }
            });
            item.setCustomFieldItems(customFieldItems);

        }
        if (formId != null) {
            item.setHasApproval(this.approverManager.isExistApproverByEntityTypeAndStepType(formId, RelationItem.TYPE_CUSTOM_FORM_ITEM));
            item.setTemplates(this.getCustomFormItemPdfTemplates(AccountingConstants.CUSTOM_FORM_ITEM_VIEW, formId).getItems());
        }
        item.setCurrentUserId(this.userManager.getUser().getObjectID());


        if (formType != null && convertFormId != null) {
            ArrayList<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(null,
                    this.getCompanyCategoryCustomFields(fID));

            item.setFormName(this.customFormManager.findByFormID(formId) != null ? this.customFormManager.findByFormID(formId).getName() : null);

            if (customFieldItems != null && !customFieldItems.isEmpty()) {
                for (int i = 0; i < customFieldItems.size(); i++) {
                    if (Constants.UI_TYPE_AUTONUMBER.equals(customFieldItems.get(i).getUiType())) {
                        item.setAutoNumber(this.getMaxValueOfAutoNumbering(customFieldItems.get(i)));
                        break;
                    }
                }
            }

            if (RelationItem.TYPE_OPPORTUNITY.equals(formType)) {
                OpportunityListItem opportunityListItem = this.crmService.editOpportunity(convertFormId, null, null, null);
                item.setEntityName(opportunityListItem.getNumberData() != null ? opportunityListItem.getNumberData().getNumberString() : opportunityListItem.getOpportunityName());
                if (customFieldItems != null && !customFieldItems.isEmpty()) {
                    for (CompanyCustomFieldItem cfs : customFieldItems) {
                        if (cfs != null) {
                            if ("PROBABILITY".equals(cfs.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(cfs.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(cfs.getUiType()))) {
                                cfs.setFieldStringValue(opportunityListItem.getProbability() != null ? opportunityListItem.getProbability().doubleValue() : null);
                            }
                            if ("ASSIGNEE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(cfs.getLookUpTypeEnum()))) {
                                cfs.setSelectedId(opportunityListItem.getAssigneeId());
                                cfs.setFieldStringValue(opportunityListItem.getAssignee());
                            }
                            if ("BACKUP_ASSIGNEE".equals(cfs.getAliasName())) {
                                cfs.setFieldStringValue(opportunityListItem.getBackupAssignee());
                            }
                            if ("NUMBER".equals(cfs.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(cfs.getDataType())) {
                                cfs.setFieldStringValue(opportunityListItem.getNumberData() != null ? opportunityListItem.getNumberData().getNumberString() : null);
                            }
                            if ("NAME".equals(cfs.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(cfs.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(cfs.getUiType()))) {
                                cfs.setFieldStringValue(opportunityListItem.getOpportunityName());
                            }
                            if ("CUSTOMER".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.CUSTOMER.equals(cfs.getLookUpTypeEnum()))) {
                                cfs.setSelectedId(opportunityListItem.getAccountId());
                                cfs.setFieldStringValue(opportunityListItem.getAccount());
                            }
                            if ("CONTACT".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.CONTACT.equals(cfs.getLookUpTypeEnum()))) {
                                cfs.setSelectedId(opportunityListItem.getContactId());
                                cfs.setFieldStringValue(opportunityListItem.getContact());
                            }
                            if ("NEXT_STEP".equals(cfs.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(cfs.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(cfs.getUiType()))) {
                                cfs.setFieldStringValue(opportunityListItem.getNextStep());
                            }
                            if ("AMOUNT".equals(cfs.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(cfs.getDataType())) {
                                cfs.setFieldStringValue(opportunityListItem.getAmount());
                            }
                            if ("CURRENCY".equals(cfs.getAliasName()) && Constants.UI_TYPE_CURRENCY.equals(cfs.getUiType())) {
                                cfs.setFieldStringValue(opportunityListItem.getCurrency());
                                cfs.setSelectedId(opportunityListItem.getCurrencyId());
                            }
                            if ("CLOSING_DATE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_DATEPICKER.equals(cfs.getUiType()) || Constants.UI_TYPE_DATEPICKER_TIME.equals(cfs.getUiType()))) {
                                cfs.setFieldDateNonConvertedValue(new DateNonConvertable(opportunityListItem.getClosingDate()));
                            }

                            if (opportunityListItem.getCustomFields() != null && !opportunityListItem.getCustomFields().isEmpty()) {
                                for (CompanyCustomFieldItem oppCustomFields : opportunityListItem.getCustomFields()) {
                                    if (oppCustomFields != null && oppCustomFields.getAliasName().equals(cfs.getAliasName()) && oppCustomFields.getUiType().equals(cfs.getUiType())) {
                                        if (Constants.UI_TYPE_LOOKUP.equals(oppCustomFields.getUiType())) {
                                            if (cfs.getLookUpTypeEnum().equals(oppCustomFields.getLookUpTypeEnum())) {
                                                cfs.setFieldStringValue(oppCustomFields.getFieldStringValue());
                                                cfs.setSelectedId(oppCustomFields.getSelectedId());
                                                cfs.setItem(oppCustomFields.getItem());
                                            }
                                        } else {
                                            cfs.setFieldStringValue(oppCustomFields.getFieldStringValue());
                                            cfs.setSelectedId(oppCustomFields.getSelectedId());
                                            cfs.setItem(oppCustomFields.getItem());
                                            cfs.setFieldDateNonConvertedValue(oppCustomFields.getFieldDateNonConvertedValue());
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                List<EdsCFItemTableSetting> itsList = this.cfItemTableSettingmanager.findByFormId(formId);

                HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

                if (itsList != null || !itsList.isEmpty()) {

                    for (EdsCFItemTableSetting itemTableSettings : itsList) {
                        if (itemTableSettings != null) {
                            String uuid = itemTableSettings.getUuid();
                            if (uuid != null) {
                                if (opportunityListItem.getItems() != null) {
                                    opportunityListItem.getItems();
                                    for (OpportunityItem opportunityItem : opportunityListItem.getItems()) {
                                        CustomTableRpc rpc = new CustomTableRpc();
                                        rpc.setUuid(uuid);
                                        ArrayList<CompanyCustomFieldItem> itemCFs = CustomFieldsUtils.setRPCCustomFieldItems(null,
                                                this.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, uuid));
                                        if (opportunityItem != null) {
                                            for (CompanyCustomFieldItem itemCF : itemCFs) {
                                                if (itemCF != null) {
                                                    if ("PRODUCT".equals(itemCF.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT.equals(itemCF.getLookUpTypeEnum()) || Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType()))) {
                                                        if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType())) {
                                                            itemCF.setItem(new SelectItem(opportunityItem.getItemID(), opportunityItem.getItemName(), opportunityItem.getDescription()));
                                                        } else {
                                                            itemCF.setFieldStringValue(opportunityItem.getItemName());
                                                            itemCF.setSelectedId(opportunityItem.getItemID());
                                                        }
                                                    }
                                                    if ("DESCRIPTION".equals(itemCF.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(itemCF.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(itemCF.getUiType()))) {
                                                        itemCF.setFieldStringValue(opportunityItem.getDescription());
                                                    }

                                                    if ("QTY".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
                                                        itemCF.setFieldStringValue(opportunityItem.getQty() != null ? opportunityItem.getQty().toString() : null);
                                                    }
                                                    if ("MEASUREMENT".equals(itemCF.getAliasName()) && Constants.UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(itemCF.getLookUpTypeEnum())) {
                                                        itemCF.setFieldStringValue(opportunityItem.getUnitMeasurement() != null ? opportunityItem.getUnitMeasurement().getName() : null);
                                                        itemCF.setSelectedId(opportunityItem.getUnitMeasurement() != null ? opportunityItem.getUnitMeasurement().getId() : null);
                                                    }
                                                    if ("UNITPRICE".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
                                                        itemCF.setFieldStringValue(opportunityItem.getPrice() != null ? opportunityItem.getPrice().toString() : null);
                                                    }
                                                    if ("DISCOUNT_AMT".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
                                                        itemCF.setFieldStringValue(opportunityItem.getDiscountAmount() != null ? opportunityItem.getDiscountAmount().toString() : null);
                                                    }
                                                    if ("CLIENT".equals(itemCF.getAliasName()) && Constants.UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(itemCF.getLookUpTypeEnum())) {
                                                        itemCF.setSelectedId(opportunityItem.getSupplierID());
                                                        itemCF.setFieldStringValue(opportunityItem.getSupplierName());
                                                    }

                                                    if (opportunityListItem.getCustomFields() != null && opportunityListItem.getCustomFields().size() > 0) {
                                                        for (CompanyCustomFieldItem oppCustomFields : opportunityListItem.getCustomFields()) {
                                                            if (oppCustomFields != null && oppCustomFields.getAliasName().equals(itemCF.getAliasName()) && oppCustomFields.getUiType().equals(itemCF.getUiType())) {
                                                                if (Constants.UI_TYPE_LOOKUP.equals(oppCustomFields.getUiType())) {
                                                                    if (itemCF.getLookUpTypeEnum().equals(oppCustomFields.getLookUpTypeEnum())) {
                                                                        itemCF.setFieldStringValue(oppCustomFields.getFieldStringValue());
                                                                        itemCF.setSelectedId(oppCustomFields.getSelectedId());
                                                                        itemCF.setItem(oppCustomFields.getItem());
                                                                    }
                                                                } else {
                                                                    itemCF.setFieldStringValue(oppCustomFields.getFieldStringValue());
                                                                    itemCF.setSelectedId(oppCustomFields.getSelectedId());
                                                                    itemCF.setItem(oppCustomFields.getItem());
                                                                    itemCF.setFieldDateNonConvertedValue(oppCustomFields.getFieldDateNonConvertedValue());
                                                                }
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        rpc.setItemCustomFields(itemCFs);
                                        map.computeIfAbsent(itemTableSettings.getUuid(), x -> new ArrayList<>()).add(rpc);
                                    }
                                }
                            }
                        }
                    }
                }
                item.setTableItems(map);

            } else if (RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(formType)) {
                RFQData rfqData = this.quoteService.getRFQData(convertFormId, null);
                item.setEntityName(rfqData.getNumberData() != null ? rfqData.getNumberData().getNumberString() : convertFormId.toString());

                if (customFieldItems != null && !customFieldItems.isEmpty()) {
                    for (CompanyCustomFieldItem cfs : customFieldItems) {
                        if (cfs != null) {
                            if (rfqData.getCustomer() != null && "CUSTOMER".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.CUSTOMER.equals(cfs.getLookUpTypeEnum()))) {
                                cfs.setFieldStringValue(rfqData.getCustomer() != null ? rfqData.getCustomer().getName() : "");
                                cfs.setSelectedId(rfqData.getCustomer() != null ? rfqData.getCustomer().getId() : null);
                            }
                            if (rfqData.getDate() != null && "REQUEST_DATE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_DATEPICKER_TIME.equals(cfs.getUiType()) || Constants.UI_TYPE_DATEPICKER.equals(cfs.getUiType()))) {
                                cfs.setFieldDateNonConvertedValue(rfqData.getDate());
                            }
                            if (rfqData.getValidUntil() != null && "DUE_DATE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_DATEPICKER_TIME.equals(cfs.getUiType()) || Constants.UI_TYPE_DATEPICKER.equals(cfs.getUiType()))) {
                                cfs.setFieldDateNonConvertedValue(rfqData.getValidUntil());
                            }
                            if (rfqData.getNumberData() != null && "NUMBER".equals(cfs.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(cfs.getDataType())) {
                                cfs.setFieldStringValue(rfqData.getNumberData() != null ? rfqData.getNumberData().getNumberString() : null);
                            }
                            if (rfqData.getSqNumber() != null && "SQ_NUMBER".equals(cfs.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(cfs.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(cfs.getUiType()))) {
                                cfs.setFieldStringValue(rfqData.getSqNumber());
                            }
                            if (rfqData.getProject() != null && "PROJECT".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(cfs.getLookUpTypeEnum()))) {
                                cfs.setSelectedId(rfqData.getProject() != null ? rfqData.getProject().getId() : null);
                                cfs.setFieldStringValue(rfqData.getProject() != null ? rfqData.getProject().getName() : "");
                            }

                            if (rfqData.getCustomFieldList() != null && !rfqData.getCustomFieldList().isEmpty()) {
                                for (CompanyCustomFieldItem rfqCustomFields : rfqData.getCustomFieldList()) {
                                    if (rfqCustomFields != null && rfqCustomFields.getAliasName().equals(cfs.getAliasName()) && rfqCustomFields.getUiType().equals(cfs.getUiType())) {
                                        if (Constants.UI_TYPE_LOOKUP.equals(rfqCustomFields.getUiType())) {
                                            if (cfs.getLookUpTypeEnum().equals(rfqCustomFields.getLookUpTypeEnum())) {
                                                cfs.setFieldStringValue(rfqCustomFields.getFieldStringValue());
                                                cfs.setSelectedId(rfqCustomFields.getSelectedId());
                                                cfs.setItem(rfqCustomFields.getItem());
                                            }
                                        } else {
                                            cfs.setFieldStringValue(rfqCustomFields.getFieldStringValue());
                                            cfs.setSelectedId(rfqCustomFields.getSelectedId());
                                            cfs.setItem(rfqCustomFields.getItem());
                                            cfs.setFieldDateNonConvertedValue(rfqCustomFields.getFieldDateNonConvertedValue());
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                List<EdsCFItemTableSetting> itsList = this.cfItemTableSettingmanager.findByFormId(formId);

                HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

                if (itsList != null || !itsList.isEmpty()) {

                    for (EdsCFItemTableSetting itemTableSettings : itsList) {
                        if (itemTableSettings != null) {
                            String uuid = itemTableSettings.getUuid();
                            if (uuid != null) {
                                if (rfqData.getItems() != null && !rfqData.getItems().isEmpty()) {
                                    for (RFQItem rfqItem : rfqData.getItems()) {
                                        CustomTableRpc rpc = new CustomTableRpc();
                                        rpc.setUuid(uuid);
                                        ArrayList<CompanyCustomFieldItem> itemCFs = CustomFieldsUtils.setRPCCustomFieldItems(null,
                                                this.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, uuid));
                                        if (rfqItem != null) {
                                            for (CompanyCustomFieldItem itemCF : itemCFs) {
                                                if (itemCF != null) {
                                                    if (rfqItem.getProduct() != null && "PRODUCT".equals(itemCF.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT.equals(itemCF.getLookUpTypeEnum()) || Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType()))) {
                                                        if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType())) {
                                                            SelectItem selectItem = rfqItem.getProduct();
                                                            selectItem.setDescription(rfqItem.getDescription());
                                                            itemCF.setItem(selectItem);
                                                        } else {
                                                            itemCF.setFieldStringValue(rfqItem.getProduct().getName());
                                                            itemCF.setSelectedId(rfqItem.getProduct().getId());
                                                        }
                                                    }
                                                    if ("DESCRIPTION".equals(itemCF.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(itemCF.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(itemCF.getUiType()))) {
                                                        itemCF.setFieldStringValue(rfqItem.getDescription());
                                                    }

                                                    if (rfqItem.getQty() != null && "QTY".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
                                                        itemCF.setFieldStringValue(rfqItem.getQty() != null ? rfqItem.getQty().toString() : null);
                                                    }
                                                    if (rfqItem.getMeasurement() != null && "MEASUREMENT".equals(itemCF.getAliasName()) && Constants.UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(itemCF.getLookUpTypeEnum())) {
                                                        itemCF.setFieldStringValue(rfqItem.getMeasurement() != null ? rfqItem.getMeasurement().getName() : null);
                                                        itemCF.setSelectedId(rfqItem.getMeasurement() != null ? rfqItem.getMeasurement().getId() : null);
                                                    }
                                                    if (rfqItem.getUnitCost() != null && "UNITPRICE".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
                                                        itemCF.setFieldStringValue(rfqItem.getUnitCost() != null ? rfqItem.getUnitCost().toString() : null);
                                                    }
                                                    if (rfqItem.getSupplier() != null && "SUPPLIER".equals(itemCF.getAliasName()) && Constants.UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(itemCF.getLookUpTypeEnum())) {
                                                        itemCF.setSelectedId(rfqItem.getSupplier().getId());
                                                        itemCF.setFieldStringValue(rfqItem.getSupplier().getName());
                                                    }
                                                    if (rfqItem.getCommission() != null && "COMISSION".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
                                                        itemCF.setFieldStringValue(rfqItem.getCommission() != null ? rfqItem.getCommission().toString() : null);
                                                    }
                                                    if (rfqItem.getReMarks() != null && "REMARK".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
                                                        itemCF.setFieldStringValue(rfqItem.getReMarks());
                                                    }

                                                    if (rfqItem.getItemCustomFields() != null && !rfqItem.getItemCustomFields().isEmpty()) {
                                                        for (CompanyCustomFieldItem rfqCustomFields : rfqItem.getItemCustomFields()) {
                                                            if (rfqCustomFields != null && rfqCustomFields.getAliasName().equals(itemCF.getAliasName()) && rfqCustomFields.getUiType().equals(itemCF.getUiType())) {
                                                                if (Constants.UI_TYPE_LOOKUP.equals(rfqCustomFields.getUiType())) {
                                                                    if (itemCF.getLookUpTypeEnum().equals(rfqCustomFields.getLookUpTypeEnum())) {
                                                                        itemCF.setFieldStringValue(rfqCustomFields.getFieldStringValue());
                                                                        itemCF.setSelectedId(rfqCustomFields.getSelectedId());
                                                                        itemCF.setItem(rfqCustomFields.getItem());
                                                                    }
                                                                } else {
                                                                    itemCF.setFieldStringValue(rfqCustomFields.getFieldStringValue());
                                                                    itemCF.setSelectedId(rfqCustomFields.getSelectedId());
                                                                    itemCF.setItem(rfqCustomFields.getItem());
                                                                    itemCF.setFieldDateNonConvertedValue(rfqCustomFields.getFieldDateNonConvertedValue());
                                                                }
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        rpc.setItemCustomFields(itemCFs);
                                        map.computeIfAbsent(itemTableSettings.getUuid(), x -> new ArrayList<>()).add(rpc);
                                    }
                                }
                            }
                        }
                    }
                }
                item.setTableItems(map);
            } else if (RelationItem.TYPE_PURCHASE_ORDER.equals(formType) || RelationItem.TYPE_SALEQUOTE.equals(formType) || RelationItem.TYPE_SALEORDER.equals(formType)) {

                Params params = new Params();
                params.setObjectID(convertFormId);
                if (RelationItem.TYPE_PURCHASE_ORDER.equals(formType)) {
                    params.setType(Constants.PAYABLE);
                    params.setFormType(Constants.PURCHASE_ORDER);
                } else {
                    params.setType(Constants.RECEIVABLE);
                    params.setFormType(RelationItem.TYPE_SALEORDER.equals(formType) ? Constants.SALE_ORDER : Constants.SALE_QUOTE);

                }

                NewInvoice newInvoice = this.quoteService.getAllQuoteData(params);

                item.setEntityName(newInvoice.getInvoiceNumber() != null ? newInvoice.getInvoiceNumber() : convertFormId.toString());

                if (customFieldItems != null && !customFieldItems.isEmpty()) {
                    for (CompanyCustomFieldItem cfs : customFieldItems) {
                        if (cfs != null) {
                            this.convertQuoteFields(cfs, newInvoice, Constants.PAYABLE);
                        }
                    }
                }

                List<EdsCFItemTableSetting> itsList = this.cfItemTableSettingmanager.findByFormId(formId);

                HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

                if (itsList != null || !itsList.isEmpty()) {

                    for (EdsCFItemTableSetting itemTableSettings : itsList) {
                        if (itemTableSettings != null) {
                            String uuid = itemTableSettings.getUuid();
                            if (uuid != null) {
                                if (newInvoice.getItems() != null) {
                                    newInvoice.getItems();
                                    for (NewInvoiceItem newInvoiceItem : newInvoice.getItems()) {
                                        CustomTableRpc rpc = new CustomTableRpc();
                                        rpc.setUuid(uuid);
                                        ArrayList<CompanyCustomFieldItem> itemCFs = CustomFieldsUtils.setRPCCustomFieldItems(null,
                                                this.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, uuid));
                                        if (newInvoiceItem != null) {
                                            for (CompanyCustomFieldItem itemCF : itemCFs) {
                                                if (itemCF != null) {
                                                    this.convertQuoteTableItems(newInvoiceItem, itemCF);
                                                }
                                            }
                                        }
                                        rpc.setItemCustomFields(itemCFs);
                                        map.computeIfAbsent(itemTableSettings.getUuid(), x -> new ArrayList<>()).add(rpc);
                                    }
                                }
                            }
                        }
                    }
                }
                item.setTableItems(map);
            } else if (RelationItem.TYPE_CASE.equals(formType)) {
                CaseItem caseItem = this.crmService.editCase(convertFormId, null, null);
                item.setEntityName(caseItem.getCaseNumber() != null ? caseItem.getCaseNumber() : caseItem.getSubject());
                if (customFieldItems != null && !customFieldItems.isEmpty()) {
                    for (CompanyCustomFieldItem cfs : customFieldItems) {
                        if (cfs != null) {
                            if ("SUBJECT".equals(cfs.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(cfs.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(cfs.getUiType()))) {
                                cfs.setFieldStringValue(caseItem.getSubject());
                            }
                            if ("DESCRIPTION".equals(cfs.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(cfs.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(cfs.getUiType()))) {
                                cfs.setFieldStringValue(caseItem.getDescription());
                            }
                            if ("TYPE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(cfs.getLookUpTypeEnum())) && cfs.getReferenceItem() != null && EdsCase._CASE_TYPE.equals(cfs.getReferenceItem().getCode())) {
                                cfs.setSelectedId(caseItem.getTypeId());
                                cfs.setFieldStringValue(caseItem.getType());
                            }
                            if ("ORIGIN".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(cfs.getLookUpTypeEnum())) && cfs.getReferenceItem() != null && EdsCase._CASE_ORIGIN.equals(cfs.getReferenceItem().getCode())) {
                                cfs.setSelectedId(caseItem.getCaseOriginId());
                                cfs.setFieldStringValue(caseItem.getCaseOrigin());
                            }
                            if ("REASON".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(cfs.getLookUpTypeEnum())) && cfs.getReferenceItem() != null && EdsCase._CASE_REASON.equals(cfs.getReferenceItem().getCode())) {
                                cfs.setSelectedId(caseItem.getCaseReasonId());
                                cfs.setFieldStringValue(caseItem.getCaseReason());
                            }
                            if ("PRIORITY".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(cfs.getLookUpTypeEnum())) && cfs.getReferenceItem() != null && EdsCase._CASE_PRIORITY.equals(cfs.getReferenceItem().getCode())) {
                                cfs.setSelectedId(caseItem.getPriorityId());
                                cfs.setFieldStringValue(caseItem.getPriority());
                            }
                            if ("STATUS".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(cfs.getLookUpTypeEnum())) && cfs.getReferenceItem() != null && EdsCase._CASE_STATUS.equals(cfs.getReferenceItem().getCode())) {
                                if (caseItem.getStatus() != null) {
                                    cfs.setSelectedId(caseItem.getStatus().getId());
                                    cfs.setFieldStringValue(caseItem.getStatus().getName());
                                }
                            }
                            if ("INTERNAL_STATUS".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.REFERENCE.equals(cfs.getLookUpTypeEnum())) && cfs.getReferenceItem() != null && EdsCase._CASE_INTERNAL_STATUS.equals(cfs.getReferenceItem().getCode())) {
                                cfs.setSelectedId(caseItem.getInternalStatusId());
                                cfs.setFieldStringValue(caseItem.getInternalStatusName());
                            }
                            if ("ASSIGNEE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(cfs.getLookUpTypeEnum()))) {
                                cfs.setSelectedId(caseItem.getCaseAssigneeId());
                                cfs.setFieldStringValue(caseItem.getCaseAssigneeName());
                            }
                            if ("RESOLVER".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.EMPLOYEE.equals(cfs.getLookUpTypeEnum()))) {
                                cfs.setSelectedId(caseItem.getResolverId());
                                cfs.setFieldStringValue(caseItem.getResolverName());
                            }
                            if ("NOTE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(cfs.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(cfs.getUiType()))) {
                                cfs.setFieldStringValue(caseItem.getNotes() != null && caseItem.getNotes().size() > 0 ? caseItem.getNotes().get(0).getComment() : null);
                            }
                            if ("CASE_ID".equals(cfs.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(cfs.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(cfs.getUiType()))) {
                                cfs.setFieldStringValue(caseItem.getCaseNumber());
                            }
                            if ("INTERNAL_UPDATED_DATE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_DATEPICKER.equals(cfs.getUiType()) || Constants.UI_TYPE_DATEPICKER_TIME.equals(cfs.getUiType()))) {
                                cfs.setFieldDateNonConvertedValue(new DateNonConvertable(caseItem.getInternalUpdatedDate()));
                            }

                            if (caseItem.getCustomFields() != null && caseItem.getCustomFields().size() > 0) {
                                for (CompanyCustomFieldItem oppCustomFields : caseItem.getCustomFields()) {
                                    if (oppCustomFields != null && oppCustomFields.getAliasName().equals(cfs.getAliasName()) && oppCustomFields.getUiType().equals(cfs.getUiType())) {
                                        if (Constants.UI_TYPE_LOOKUP.equals(oppCustomFields.getUiType())) {
                                            if (cfs.getLookUpTypeEnum().equals(oppCustomFields.getLookUpTypeEnum())) {
                                                cfs.setFieldStringValue(oppCustomFields.getFieldStringValue());
                                                cfs.setSelectedId(oppCustomFields.getSelectedId());
                                                cfs.setItem(oppCustomFields.getItem());
                                            }
                                        } else {
                                            cfs.setFieldStringValue(oppCustomFields.getFieldStringValue());
                                            cfs.setSelectedId(oppCustomFields.getSelectedId());
                                            cfs.setItem(oppCustomFields.getItem());
                                            cfs.setFieldDateNonConvertedValue(oppCustomFields.getFieldDateNonConvertedValue());
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item.setCustomFieldItems(customFieldItems);
        }
        item.setCopy(isCopy);
        EdsCustomForm customForm = customFormManager.findByFormID(formId);
        item.setQuizForm(customForm != null && customForm.getQuiz() != null ? customForm.getQuiz() : false);
        return item;
    }

    private void convertQuoteTableItems(NewInvoiceItem newInvoiceItem, CompanyCustomFieldItem itemCF) {
        if (newInvoiceItem.getItemID() != null && "PRODUCT".equals(itemCF.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT.equals(itemCF.getLookUpTypeEnum()) || Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType()))) {
            if (Constants.UI_TYPE_ITEM_WITH_DESCRIPTION.equals(itemCF.getUiType())) {
                SelectItem selectItem = new SelectItem(newInvoiceItem.getItemID(), newInvoiceItem.getItemName());
                selectItem.setDescription(newInvoiceItem.getDescription());
                itemCF.setItem(selectItem);
            } else {
                itemCF.setFieldStringValue(newInvoiceItem.getItemName());
                itemCF.setSelectedId(newInvoiceItem.getItemID());
            }
        }
        if ("DESCRIPTION".equals(itemCF.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(itemCF.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(itemCF.getUiType()))) {
            itemCF.setFieldStringValue(newInvoiceItem.getDescription());
        }

        if (newInvoiceItem.getQuantity() != null && "QTY".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
            itemCF.setFieldStringValue(newInvoiceItem.getQuantity().toString());
        }
        if (newInvoiceItem.getProject() != null && "PROJECT".equals(itemCF.getAliasName()) && Constants.UI_TYPE_LOOKUP.equals(itemCF.getUiType()) && CustomFieldLookUpTypeEnum.PROJECT.equals(itemCF.getLookUpTypeEnum())) {
            itemCF.setFieldStringValue(newInvoiceItem.getProject() != null ? newInvoiceItem.getProject().getName() : null);
            itemCF.setSelectedId(newInvoiceItem.getProject() != null ? newInvoiceItem.getProject().getId() : null);
        }
        if (newInvoiceItem.getUnitPrice() != null && "UNITPRICE".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
            itemCF.setFieldStringValue(newInvoiceItem.getUnitPrice() != null ? newInvoiceItem.getUnitPrice().toString() : null);
        }
        if (newInvoiceItem.getDiscountAmount() != null && "DISCOUNT_AMT".equals(itemCF.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(itemCF.getDataType())) {
            itemCF.setFieldStringValue(newInvoiceItem.getDiscountAmount() != null ? newInvoiceItem.getDiscountAmount().toString() : null);
        }


        if (newInvoiceItem.getCustomFieldItems() != null && !newInvoiceItem.getCustomFieldItems().isEmpty()) {
            for (CompanyCustomFieldItem rfqCustomFields : newInvoiceItem.getCustomFieldItems()) {
                if (rfqCustomFields != null && rfqCustomFields.getAliasName().equals(itemCF.getAliasName()) && rfqCustomFields.getUiType().equals(itemCF.getUiType())) {
                    if (Constants.UI_TYPE_LOOKUP.equals(rfqCustomFields.getUiType())) {
                        if (itemCF.getLookUpTypeEnum().equals(rfqCustomFields.getLookUpTypeEnum())) {
                            itemCF.setFieldStringValue(rfqCustomFields.getFieldStringValue());
                            itemCF.setSelectedId(rfqCustomFields.getSelectedId());
                            itemCF.setItem(rfqCustomFields.getItem());
                        }
                    } else {
                        itemCF.setFieldStringValue(rfqCustomFields.getFieldStringValue());
                        itemCF.setSelectedId(rfqCustomFields.getSelectedId());
                        itemCF.setItem(rfqCustomFields.getItem());
                        itemCF.setFieldDateNonConvertedValue(rfqCustomFields.getFieldDateNonConvertedValue());
                    }
                    break;
                }
            }
        }
    }

    private void convertQuoteFields(CompanyCustomFieldItem cfs, NewInvoice newInvoice, String type) {
        if (newInvoice.getTypeItem() != null && "SUPPLIER".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(cfs.getLookUpTypeEnum()))) {
            cfs.setFieldStringValue(newInvoice.getTypeItem().getName());
            cfs.setSelectedId(newInvoice.getTypeItem().getId());
        }
        if (newInvoice.getTypeItem() != null && "CUSTOMER".equals(cfs.getAliasName()) && (Constants.UI_TYPE_LOOKUP.equals(cfs.getUiType()) && CustomFieldLookUpTypeEnum.CUSTOMER.equals(cfs.getLookUpTypeEnum()))) {
            cfs.setFieldStringValue(newInvoice.getTypeItem().getName());
            cfs.setSelectedId(newInvoice.getTypeItem().getId());
        }

        if (newInvoice.getInvoiceDate() != null && "DATE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_DATEPICKER_TIME.equals(cfs.getUiType()) || Constants.UI_TYPE_DATEPICKER.equals(cfs.getUiType()))) {
            cfs.setFieldDateNonConvertedValue(newInvoice.getInvoiceDate());
        }
        if (newInvoice.getDueDate() != null && "DUE_DATE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_DATEPICKER_TIME.equals(cfs.getUiType()) || Constants.UI_TYPE_DATEPICKER.equals(cfs.getUiType()))) {
            cfs.setFieldDateNonConvertedValue(newInvoice.getDueDate());
        }
        if (newInvoice.getInvoiceNumber() != null && "NUMBER".equals(cfs.getAliasName()) && Constants.DATA_TYPE_NUMBER.equals(cfs.getDataType())) {
            cfs.setFieldStringValue(newInvoice.getInvoiceNumber());
        }
        if (newInvoice.getReference() != null && "REFERENCE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_TEXTBOX.equals(cfs.getUiType()) || Constants.UI_TYPE_TEXTAREA.equals(cfs.getUiType()))) {
            cfs.setFieldStringValue(newInvoice.getReference());
        }
        if (newInvoice.getCancelDate() != null && "CANCEL_DATE".equals(cfs.getAliasName()) && (Constants.UI_TYPE_DATEPICKER_TIME.equals(cfs.getUiType()) || Constants.UI_TYPE_DATEPICKER.equals(cfs.getUiType()))) {
            cfs.setFieldDateNonConvertedValue(newInvoice.getCancelDate());
        }

        if (newInvoice.getCustomFieldItems() != null && newInvoice.getCustomFieldItems().size() > 0) {
            for (CompanyCustomFieldItem rfqCustomFields : newInvoice.getCustomFieldItems()) {
                if (rfqCustomFields != null && rfqCustomFields.getAliasName().equals(cfs.getAliasName()) && rfqCustomFields.getUiType().equals(cfs.getUiType())) {
                    if (Constants.UI_TYPE_LOOKUP.equals(rfqCustomFields.getUiType())) {
                        if (cfs.getLookUpTypeEnum().equals(rfqCustomFields.getLookUpTypeEnum())) {
                            cfs.setFieldStringValue(rfqCustomFields.getFieldStringValue());
                            cfs.setSelectedId(rfqCustomFields.getSelectedId());
                            cfs.setItem(rfqCustomFields.getItem());
                        }
                    } else {
                        cfs.setFieldStringValue(rfqCustomFields.getFieldStringValue());
                        cfs.setSelectedId(rfqCustomFields.getSelectedId());
                        cfs.setItem(rfqCustomFields.getItem());
                        cfs.setFieldDateNonConvertedValue(rfqCustomFields.getFieldDateNonConvertedValue());
                    }
                    break;
                }
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CustomFormItemPdfTemplateList getCustomFormItemPdfTemplates(String type, String formId) {
        List<EdsCompanyPdfTemplate> templates = this.companyPdfTemplateManager.getCompanyPDFTemplatesByTypeWithFormId(type, formId);
        SelectItem[] items = new SelectItem[templates.size()];
        int i = 0;
        Integer defaultTemplateID = null;
        for (EdsCompanyPdfTemplate t : templates) {
            items[i] = new SelectItem(t.getObjectID(), t.getName(), t.isDefaultTemplate(), null);
            if (t.isDefaultTemplate()) {
                defaultTemplateID = t.getObjectID();
            }
            i++;
        }
        return new CustomFormItemPdfTemplateList(items, defaultTemplateID);
    }

    @Override
    public void createWorkflowModule(String code, String name, boolean create) {
        EdsReference module = this.referenceManager.findReference(WorkflowRule._WORKFLOW_MODULE, WorkflowRule._WORKFLOW_MODULE + "_" + code);
        if (module == null) {
            if (create) {
                module = new EdsReference();
                module.setCode(WorkflowRule._WORKFLOW_MODULE + "_" + code);
                module.setName(name);
                module.setSorder(this.referenceManager.getLastSorder(WorkflowRule._WORKFLOW_MODULE) + 1);
                module.setParent(this.referenceManager.getByCode(WorkflowRule._WORKFLOW_MODULE));
                this.referenceManager.create(module);
            }
        } else if (!create) {
            String formID = Constants.ONBOARDING_STEP_FORM + code;
            EdsModel model = this.modelManager.getStepForm(formID);
            if (model != null) {
                this.modelManager.delete(model);
            }
            this.modelFieldManager.deleteFieldsByFormID(formID);
            module.setDeleted(true);
            this.referenceManager.update(module);
        }

        if (module != null) {
            module.setName(name);
        }
    }

    @Override
    public CustomTableRpc[] getCustomItemTable(Integer id, String uuid) {
        ArrayList<CustomTableRpc> items = new ArrayList<>();

        List<EdsCustomItemTable> itemTables = this.customItemTableManager.findByUuid(id, uuid);

        if (CollectionUtils.isNotEmpty(itemTables)) {
            itemTables.forEach(it -> {
                CustomTableRpc item = it.getRpc();

                item.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(),
                        this.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, item.getUuid())));
                items.add(item);
            });
        }
        return items.toArray(new CustomTableRpc[]{});
    }

    @Override
    public CustomTableRpc[] getOpportunityItemtable(Integer id, String uuid) {
        ArrayList<CustomTableRpc> items = new ArrayList<>();

        List<EdsOpportunityCustomItemTable> itemTables = this.opportunityItemTableManager.findByUuid(id, uuid);

        if (CollectionUtils.isNotEmpty(itemTables)) {
            itemTables.forEach(it -> {
                CustomTableRpc item = it.getRpc();

                item.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(),
                        this.getCompanyCustomFieldsByCategory(ViewName.OpportunityItemTable, item.getUuid())));
                items.add(item);
            });
        }
        return items.toArray(new CustomTableRpc[]{});
    }

    @Override
    public CustomTableRpc[] getEmployeeItemtable(Integer id, String uuid) {
        ArrayList<CustomTableRpc> items = new ArrayList<>();

        List<EdsEmployeeCustomItemTable> itemTables = this.employeeItemTableManager.findByUuid(id, uuid);

        if (CollectionUtils.isNotEmpty(itemTables)) {
            itemTables.forEach(it -> {
                CustomTableRpc item = it.getRpc();

                item.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(),
                        this.getCompanyCustomFieldsByCategory(ViewName.EmployeeItemTable, item.getUuid())));
                items.add(item);
            });
        }
        return items.toArray(new CustomTableRpc[]{});
    }

    @Override
    public CustomTableRpc[] getPlacementItemtable(Integer id, String uuid) {
        ArrayList<CustomTableRpc> items = new ArrayList<>();

        List<EdsPlacementItemTable> itemTables = this.placementItemTableManager.findByUuid(id, uuid);

        if (CollectionUtils.isNotEmpty(itemTables)) {
            itemTables.forEach(it -> {
                CustomTableRpc item = it.getRpc();

                item.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(),
                        this.getCompanyCustomFieldsByCategory(ViewName.PlacementItemTable, item.getUuid())));
                items.add(item);
            });
        }
        return items.toArray(new CustomTableRpc[]{});
    }

    @Override
    public CustomTableRpc[] getProjectItemtable(Integer id, String uuid) {
        ArrayList<CustomTableRpc> items = new ArrayList<>();

        List<EdsProjectCustomItemTable> itemTables = this.projectItemTableManager.findByUuid(id, uuid);

        if (CollectionUtils.isNotEmpty(itemTables)) {
            itemTables.forEach(it -> {
                CustomTableRpc item = it.getRpc();

                item.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(),
                        this.getCompanyCustomFieldsByCategory(ViewName.ProjectItemTable, item.getUuid())));
                items.add(item);
            });
        }
        return items.toArray(new CustomTableRpc[]{});
    }

    @Override
    public CustomTableRpc[] getCandidateItemTable(Integer id, String uuid) {
        ArrayList<CustomTableRpc> items = new ArrayList<>();
        List<EdsCandidateItemTable> itemTables = this.candidateItemTableManager.findByUuid(id, uuid);
        if (CollectionUtils.isNotEmpty(itemTables)) {
            itemTables.forEach(it -> {
                CustomTableRpc item = it.getRpc();

                item.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(),
                        this.getCompanyCustomFieldsByCategory(ViewName.CandidateCustomItemTable, item.getUuid())));
                items.add(item);
            });
        }
        return items.toArray(new CustomTableRpc[]{});
    }

    @Override
    public CustomTableRpc[] getVacancyItemtable(Integer id, String uuid) {
        ArrayList<CustomTableRpc> items = new ArrayList<>();

        List<EdsVacancyItemTable> itemTables = this.vacancyItemTableManager.findByUuid(id, uuid);

        if (CollectionUtils.isNotEmpty(itemTables)) {
            itemTables.forEach(it -> {
                CustomTableRpc item = it.getRpc();

                item.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(),
                        this.getCompanyCustomFieldsByCategory(ViewName.VacancyItemTable, item.getUuid())));
                items.add(item);
            });
        }
        return items.toArray(new CustomTableRpc[]{});
    }

    @Override
    public ArrayList<CompanyCustomFieldItem> getCompanyCategoryCustomFields(Integer fid) {

        EdsCustomForm customForm;
        if (fid != null) {
            customForm = this.customFormManager.get(fid);
        } else {
            return null;
        }

        EdsModel model = this.modelManager.get(customForm.getFormID());
        if (model == null) {
            return null;
        }

        List<EdsCompanyCustomFieldsSettings> companyCFs = this.companyCFSettingsManager.getCompanyCustomFieldsWithCategory(ViewName.CustomFormItems.name(), model.getViewName());
        if (companyCFs != null) {
            return (ArrayList<CompanyCustomFieldItem>) this.fillObjectFields(companyCFs, ViewName.CustomFormItems, false);
        }
        return null;
    }

    private List<String> getSeeRelatedCompanyCategoryCustomFields(Integer fid) {

        EdsCustomForm customForm = this.customFormManager.get(fid);

        EdsModel model = this.modelManager.get(customForm.getFormID());
        if (model == null) {
            return null;
        }

        return this.companyCFSettingsManager.getSeeRelatedCompanyCustomFields(ViewName.CustomFormItems.name(), model.getViewName());
    }

    @Override
    public void indexCustomFormItems(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        int startat = 0;
        int limit = HIBERNATE_CHUNK_SIZE;

        List<EdsCustomFormItems> list = this.customFormItemManager.list(fp, startat, limit);
        while (!list.isEmpty()) {
            try {
                customFormItemSolrComponent.indexConcurrently(list);
            } catch (SolrServerException | IOException | InterruptedException e) {
                log.error("Error Custom Form Index. Company ID : {} , Message : {} ", ServerSecurityContext.getInstance().getCompanyId(), e.getMessage());
            }
            if (startat > 0 && startat % 2 == 0) {
                this.customFormItemManager.flushAndClear();
            }
            customFormItemManager.flushAndClear();
            startat++;
            list = this.customFormItemManager.list(fp, (startat * limit), limit);
        }
        customFormItemManager.flushAndClear();
    }

    @Override
    public ArrayList<SelectItem[]> getCSVColumns(Integer attachmentid, Integer needrowcount) {
        EdsAttachment attachment = attachmentManager.get(attachmentid);
        FindEncodeInputStream inputStream = uploadManager.getFindEncodeInputStream(attachment);
        if (attachment.getOriginalName().toLowerCase().contains(".xls") || attachment.getOriginalName().toLowerCase().contains(".xlsx")) {
            return getExcelHeader(inputStream, needrowcount);
        }
        return this.getCSVColumns(this.getByteArrayOfInputStream(inputStream), ',', needrowcount);
    }

    @Override
    public String getMaxValueOfAutoNumbering(CompanyCustomFieldItem customFieldItem) {
        ArrayList<String> result = (ArrayList<String>) this.companyCFManager.getValuesOfAutoNumberingByTableName(customFieldItem, genericSettingsManager.isSettingsEnabled(ENABLE_CF_AUTO_NUMBER_RESET));
        if (result == null) {
            return CustomFieldsUtils.formatAutoNumber(customFieldItem.getPrefix(), 1);
        }
        int maxValue = 0;
        for (String number : result) {
            if (StringUtils.isNotBlank(number)) {
                maxValue++;
            }
        }
        return CustomFieldsUtils.formatAutoNumber(customFieldItem.getPrefix(), maxValue + 1);

    }

    @Override
    public SelectItem[] getSerialNumbers(ListingFilterParameter fp) {
        List<EdsItemBatch> result = this.itemBatchManager.getList(fp);
        HashSet<String> serials = new HashSet<>();
        for (EdsItemBatch item : result) {
            serials.add(item.getSerial());
        }
        List<SelectItem> items = new ArrayList<>();
        int i = 1;
        for (String item : serials) {
            items.add(new SelectItem(i++, item));
        }
        return items.toArray(new SelectItem[]{});
    }

    @Override
    public SelectItem[] getBatchType(ListingFilterParameter fp) {
        List<EdsItemBatch> result = this.itemBatchManager.getList(fp);
        List<SelectItem> items = new ArrayList<>();
        HashSet<String> types = new HashSet<>();
        for (EdsItemBatch item : result) {
            types.add(item.getBatchType());
        }
        int i = 1;
        for (String type : types) {
            items.add(new SelectItem(i++, type));
        }
        return items.toArray(new SelectItem[]{});
    }

    @Override
    public SelectItem[] getBatchWarehouse(ListingFilterParameter fp) {
        List<EdsItemBatch> result = this.itemBatchManager.getList(fp);

        List<SelectItem> items = new ArrayList<>();
        HashMap<Integer, String> warehouses = new HashMap<>();
        for (EdsItemBatch item : result) {
            warehouses.put(item.getWarehouse().getObjectID(), item.getWarehouse().getName());
        }
        for (Map.Entry<Integer, String> warehouse : warehouses.entrySet()) {
            SelectItem selectItem = new SelectItem(warehouse.getKey(), warehouse.getValue());
            items.add(selectItem);
        }
        return items.toArray(new SelectItem[]{});
    }

    private PlaceOfSupplyItem getPlaceOfSupplyForUAE(String treatmentType) {
        PlaceOfSupplyItem item = new PlaceOfSupplyItem();
        boolean isDomesticTaxTreatment = Constants.VAT_REGISTERED.equals(treatmentType) || Constants.NON_VAT_REGISTERED.equals(treatmentType)
                || Constants.VAT_REGISTERED_DESIGNATED_ZONE.equals(treatmentType) || Constants.NON_VAT_REGISTERED_DESIGNATED_ZONE.equals(treatmentType);

        boolean isGccTaxTreatment = Constants.GCC_VAT_REGISTERED.equals(treatmentType) || Constants.GCC_NON_VAT_REGISTERED.equals(treatmentType);

        if (isDomesticTaxTreatment) {
            SelectItem[] regions = ServerUtils.getAsSelectItem(this.regionManager.listByCountry(this.userManager.getUser().getCompany().getCountry().getObjectID()), ServerUtils.EDS_REGION);
            for (SelectItem region : regions) {
                region.setCategory(Constants.PLACEOFSUPPLY_CATEGORY.REGION);
            }
            item.setStates(regions);

            return item;
        } else if (isGccTaxTreatment) {
            SelectItem[] regions = ServerUtils.getAsSelectItem(this.regionManager.listByCountry(this.userManager.getUser().getCompany().getCountry().getObjectID()), ServerUtils.EDS_REGION);
            for (SelectItem region : regions) {
                region.setCategory(Constants.PLACEOFSUPPLY_CATEGORY.REGION);
            }
            item.setStates(regions);

            SelectItem[] countries = ServerUtils.getAsSelectItem(this.countryManager.getCountryByCodeIn(Constants.GCC_COUNTRIES), ServerUtils.EDS_COUNTRY);
            for (SelectItem country : countries) {
                country.setCategory(Constants.PLACEOFSUPPLY_CATEGORY.COUNTRY);
            }
            item.setCountries(countries);

            return item;
        }
        return null;
    }

    private PlaceOfSupplyItem getPlaceOfSupplyForKSA(String treatmentType) {
        PlaceOfSupplyItem item = new PlaceOfSupplyItem();
        SelectItem[] countries = ServerUtils.getAsSelectItem(this.countryManager.getCountryByCodeIn(Constants.GCC_COUNTRIES), ServerUtils.EDS_COUNTRY);
        for (SelectItem country : countries) {
            country.setCategory(Constants.PLACEOFSUPPLY_CATEGORY.COUNTRY);
        }
        item.setCountries(countries);
        return item;
    }

    @Override
    public CompanyCustomFieldItem getCompanyCustomFieldById(Integer objectId) {
        EdsCompanyCustomFieldsSettings edsCompanyCustomFieldsSettings = this.companyCFSettingsManager.get(objectId);
        return edsCompanyCustomFieldsSettings.getRPC(null);
    }

    @Override
    public HashMap<String, Object> getLocaledCustomFiledMap(HashMap<String, Object> map, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems == null)
            customFieldItems = new ArrayList<>();

        for (CompanyCustomFieldItem customFieldItem : customFieldItems) {
            if (CustomFieldLookUpTypeEnum.POSITION.equals(customFieldItem.getLookUpTypeEnum())) {
                if (Constants.UI_TYPE_LOOKUP.equals(customFieldItem.getUiType())) {
                    try {
                        Integer positionId = Integer.parseInt((String) map.get(customFieldItem.getColumnCode()));
                        map.put(customFieldItem.getColumnCode(), positionManager.get(positionId).getName());
                    } catch (NumberFormatException e) {
                        log.info("Position id is not parse successfully");
                    }
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
                    try {
                        Gson gson = new Gson();
                        List<SelectItem> multiLookUpItems = gson.fromJson((String) map.get(customFieldItem.getColumnCode()), new TypeToken<List<SelectItem>>() {
                        }.getType());
                        if (multiLookUpItems != null && multiLookUpItems.size() > 0) {
                            multiLookUpItems.stream().peek(selectItem -> selectItem.setName(positionManager.get(selectItem.getId()).getName())).collect(Collectors.toList());
                        }
                        map.put(customFieldItem.getColumnCode(), gson.toJson(multiLookUpItems));
                    } catch (Exception ex) {
                        ex.getStackTrace();
                    }
                }
            } else if (CustomFieldLookUpTypeEnum.DEPARTMENT.equals(customFieldItem.getLookUpTypeEnum())) {
                if (Constants.UI_TYPE_LOOKUP.equals(customFieldItem.getUiType())) {
                    try {
                        Integer departmentId = Integer.parseInt((String) map.get(customFieldItem.getColumnCode()));
                        map.put(customFieldItem.getColumnCode(), departmentManager.get(departmentId).getName());
                    } catch (NumberFormatException e) {
                        log.info("Department id is not parse successfully");
                    }
                } else if (Constants.UI_TYPE_MULTI_LOOKUP.equals(customFieldItem.getUiType())) {
                    try {
                        Gson gson = new Gson();
                        List<SelectItem> multiLookUpItems = gson.fromJson((String) map.get(customFieldItem.getColumnCode()), new TypeToken<List<SelectItem>>() {
                        }.getType());
                        if (multiLookUpItems != null && multiLookUpItems.size() > 0) {
                            multiLookUpItems.stream().peek(selectItem -> selectItem.setName(departmentManager.get(selectItem.getId()).getName())).collect(Collectors.toList());
                        }
                        map.put(customFieldItem.getColumnCode(), gson.toJson(multiLookUpItems));
                    } catch (Exception ex) {
                        ex.getStackTrace();
                    }
                }
            }
        }
        return map;
    }

    @Override
    public void getFacetFilterWithLocale
            (HashMap<String, FacetContentRpc> map, List<CompanyCustomFieldItem> customFieldItems) {
        if (map == null)
            return;
        if (customFieldItems == null)
            customFieldItems = new ArrayList();

        SelectItem[] selectItems;
        if (map.get("department") != null && map.get("department").getFacetItems() != null) {
            selectItems = getLocaledSelectItemsToDepartment(departmentManager, map.get("department").getFacetItems());
            map.get("department").setFacetItems(selectItems);
        }
        if (map.get("location") != null && map.get("location").getFacetItems() != null) {
            selectItems = getSelectedItemsToLocation(map.get("location").getFacetItems());
            map.get("location").setFacetItems(selectItems);
        }
        if (map.get("positionType") != null && map.get("positionType").getFacetItems() != null) {
            selectItems = getLocaledSelectItems(referenceManager, map.get("positionType").getFacetItems(), Boolean.FALSE);
            map.get("positionType").setFacetItems(selectItems);
        }
        if (map.get("position") != null && map.get("position").getFacetItems() != null) {
            selectItems = getLocaledSelectItems(positionManager, map.get("position").getFacetItems(), Boolean.FALSE);
            map.get("position").setFacetItems(selectItems);
        }
        if (map.get("status") != null && map.get("status").getFacetItems() != null) {
            selectItems = getLocaledSelectItems(referenceManager, map.get("status").getFacetItems(), Boolean.FALSE);
            Set<SelectItem> set = new HashSet<>();
            for (int i = 0; i < selectItems.length; i++) {
                int isSingle = 0;
                for (int j = i + 1; j < selectItems.length; j++) {
                    if (selectItems[i].getId().equals(selectItems[j].getId())) {
                        isSingle++;
                        selectItems[i].setTotalCount(selectItems[i].getTotalCount() + selectItems[j].getTotalCount());
                        selectItems[i].setDescription(commonLocalizer.localize(selectItems[i].getName().toLowerCase(), selectItems[i].getName().toLowerCase()) + " (<b> " + selectItems[i].getTotalCount() + " </b>)");
                        selectItems[i].setName(commonLocalizer.localize(selectItems[i].getName().toLowerCase(), selectItems[i].getName()));
                        set.add(selectItems[i]);
                    }
                }
                if (isSingle == 0) {
                    if ("true".equals(selectItems[i].getName()) || "false".equals(selectItems[i].getName())) {
                        selectItems[i].setDescription(accountingLocalizer.localize(selectItems[i].getName().equals("true") ? "active" : "inactive") + " (<b> " + selectItems[i].getTotalCount() + " </b>)");
                    } else {
                        selectItems[i].setDescription(commonLocalizer.localize(selectItems[i].getName().toLowerCase(), selectItems[i].getName()) + " (<b> " + selectItems[i].getTotalCount() + " </b>)");
                    }
                    selectItems[i].setName(commonLocalizer.localize(selectItems[i].getName().toLowerCase(), selectItems[i].getName()));
                    set.add(selectItems[i]);
                }
            }
            map.get("status").setFacetItems(set.toArray(new SelectItem[0]));
        }

        for (CompanyCustomFieldItem item : customFieldItems) {
            FacetContentRpc contentRpc = map.get(item.getColumnCode());
            if (contentRpc == null
                    || contentRpc.getFacetItems() == null
                    || !Constants.UI_TYPE_LOOKUP.equals(item.getUiType())) {
                continue;
            }
            if (CustomFieldLookUpTypeEnum.DEPARTMENT.equals(item.getLookUpTypeEnum())) {
                selectItems = getLocaledSelectItems(departmentManager, contentRpc.getFacetItems(), Boolean.TRUE);
                contentRpc.setFacetItems(selectItems);
            }
            if (CustomFieldLookUpTypeEnum.POSITION.equals(item.getLookUpTypeEnum())) {
                selectItems = getLocaledSelectItems(positionManager, contentRpc.getFacetItems(), Boolean.TRUE);
                contentRpc.setFacetItems(selectItems);
            }
        }
        /////// Todo: in the filter did not see custom fields  ozgina hatliklar bor customFormlarni filter UI da va Employee Listing Filter Ui da
    }

    @Override
    public void saveListPanelMq(ListPanelItemMQ message) {
        EdsUser userByUserID = userManager.getUserByUserID(message.getUserId());
        if (userByUserID != null) {
            EdsListPanelSettings listPanelSettings = listPanelSettingsManager.getUserListPanelSettings(userByUserID, message.getPanelType());
            if (listPanelSettings == null) {
                listPanelSettings = new EdsListPanelSettings();
                listPanelSettings.setUser(userByUserID);
                listPanelSettings.setPanelType(message.getPanelType());
                listPanelSettings.setParentID(message.getParentId());
            }
            listPanelSettings.setSettingsJSONData(message.getSettingsJSONData());
            listPanelSettings.setSortBy(message.getSortBy());
            listPanelSettingsManager.createOrUpdate(listPanelSettings);
        }
    }

    @Override
    public void saveuserRequestTrackingMq(UserRequestItemMQ message) {
        EdsUser user = userManager.getUserByUserID(message.getUserId());
        EdsUserSession userSession = userSessionManager.getUserSession(message.getSessionId());
        if (user != null) {
            if ((userSession != null && !userSession.isSuperUser()) && user.isEmployee()) {
                EdsUserLastRequest userLastRequest = new EdsUserLastRequest();
                userLastRequest.setEmployee(user);
                userLastRequest.setMethodName(message.getMethodName());
                userLastRequest.setRequestDate(message.getRequestDate());
                userLastRequestManager.create(userLastRequest);
            }
        }
    }

    @Override
    public List<String> getCFsColumnCodeByUiTypes(ViewName viewName, List<String> uiTypes) {
        List<EdsCompanyCustomFieldsSettings> customFieldsSettings = companyCFSettingsManager.getCFByUiTypes(viewName.name(), uiTypes, null);
        return customFieldsSettings.stream().map(EdsCompanyCustomFieldsSettings::getColumnCode).collect(Collectors.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public EdsImportFile saveImportFile(ImportFile importFile, EdsUser employee) {
        EdsImportFile edsImportFile = new EdsImportFile(importFile);
        edsImportFile.setOwner(employee);
        StringBuilder nextSteps = new StringBuilder();
        nextSteps.append(ImportCustomEventListenerImpl.EVENT_IMPORT_CRMACCOUNT).append(":").append(ImportCustomEventListenerImpl.EVENT_IMPORT_CONTACT);
        edsImportFile.setNextSteps(nextSteps.toString());
        edsImportFile.setStatus(ImportStatusEnum.IN_PROCESS);
        importFileManager.createOrUpdate(edsImportFile);
        importFileManager.flushAndClear();
        return edsImportFile;
    }

    private SelectItem[] getLocaledSelectItems(Manager manager, SelectItem[] selectItems, boolean isCustomField) {
        for (int i = 0; i < selectItems.length; i++) {
            Integer selectItemId = -1;
            if (isCustomField) {
                try {
                    selectItemId = Integer.parseInt(selectItems[i].getName());
                } catch (NumberFormatException ex) {
                    ex.getStackTrace();
                }
            } else {
                selectItemId = selectItems[i].getId();
            }
            if (selectItemId == -1 || manager.get(selectItemId) == null)
                continue;

            String localeName = manager.get(selectItemId).getName();

            selectItems[i].setName(localeName);
            selectItems[i].setId(selectItemId);
            selectItems[i].setDescription(localeName + " (<b> " + selectItems[i].getTotalCount() + " </b>)");
        }
        return selectItems;
    }

    private SelectItem[] getSelectedItemsToLocation(SelectItem[] selectItems) {
        for (int i = 0; i < selectItems.length; i++) {
            Integer selectItemId = -1;
            selectItemId = selectItems[i].getId();

            EdsLocation location = locationManager.get(selectItemId);

            if (selectItemId == -1 || location == null)
                continue;

            String localeName = location.getName();
            String code = location.getCode() != null ? location.getCode() + "->" : "";


            selectItems[i].setName(code + localeName);
            selectItems[i].setId(selectItemId);
            selectItems[i].setDescription(code + localeName + " (<b> " + selectItems[i].getTotalCount() + " </b>)");
        }
        return selectItems;
    }

    private SelectItem[] getLocaledSelectItemsToDepartment(DepartmentManager manager, SelectItem[] selectItems) {
        for (int i = 0; i < selectItems.length; i++) {
            Integer selectItemId = -1;
            selectItemId = selectItems[i].getId();

            EdsDepartment department = manager.get(selectItemId);

            if (selectItemId == -1 || department == null)
                continue;

            String localeName = department.getName();
            String code = department.getNumberData() != null ? department.getNumberData() + "->" : "";


            selectItems[i].setName(code + localeName);
            selectItems[i].setId(selectItemId);
            selectItems[i].setDescription(code + localeName + " (<b> " + selectItems[i].getTotalCount() + " </b>)");
        }
        return selectItems;
    }

    @Override
    public SelectItem[] getBankAccountCurrencyList(ListingFilterParameter fp) {
        List<EdsBankAccount> accounts = accountingManager.getBankAccountList(fp);
        List<SelectItem> items = new ArrayList<>();
        int i = 0;
        for (EdsBankAccount account : accounts) {
            if (account.getAccount() != null && account.getAccount().getCurrency() != null) {
                SelectItem item = new SelectItem(account.getAccount().getCurrency().getObjectID(), account.getAccount().getCurrency().getName());
                if (!items.contains(item)) {
                    items.add(item);
                }
            }
        }
        return items.toArray(new SelectItem[]{});
    }

    @Override
    public CustomFormLocalization getCFLocalization(Integer id, LocalizationTypeEnum type) {
        CustomFormLocalization localization = null;
        switch (type) {
            case FIELD -> {
                EdsCompanyCustomFieldsSettings customFieldsSettings = companyCFManager.get(id);
                if (customFieldsSettings != null && customFieldsSettings.getCustomFormlocalization() != null) {
                    if ((customFieldsSettings.getCustomFormlocalization().getChildren() == null || customFieldsSettings.getCustomFormlocalization().getChildren().isEmpty()) && (customFieldsSettings.getPredefinedValues() != null && customFieldsSettings.getPredefinedValues().length > 0) &&
                            (Constants.UI_TYPE_CHECKBOX.equals(customFieldsSettings.getUiType()) || Constants.UI_TYPE_DROPDOWN.equals(customFieldsSettings.getUiType()) || Constants.UI_TYPE_RADIOBUTTON.equals(customFieldsSettings.getUiType()))) {
                        for (String value : customFieldsSettings.getPredefinedValues()) {
                            EdsCustomFormLocalization predefinedFields = new EdsCustomFormLocalization();
                            predefinedFields.setDefaultName(value);
                            predefinedFields.setEnglishName(value);
                            predefinedFields.setArabicName(value);
                            predefinedFields.setRussianName(value);
                            predefinedFields.setUzbekName(value);
                            predefinedFields.setParent(customFieldsSettings.getCustomFormlocalization());
                            customFormLocalizationManager.create(predefinedFields);
                        }
                    }
                    localization = customFieldsSettings.getCustomFormlocalization().getRPC();
                } else if (customFieldsSettings != null) {
                    EdsCustomFormLocalization customFormLocalization = new EdsCustomFormLocalization();
                    customFormLocalization.setDefaultName(customFieldsSettings.getFieldName());
                    customFormLocalization.setEnglishName(customFieldsSettings.getFieldName());
                    customFormLocalization.setArabicName(customFieldsSettings.getFieldName());
                    customFormLocalization.setRussianName(customFieldsSettings.getFieldName());
                    customFormLocalization.setUzbekName(customFieldsSettings.getFieldName());
                    customFormLocalizationManager.create(customFormLocalization);

                    if ((customFieldsSettings.getPredefinedValues() != null && customFieldsSettings.getPredefinedValues().length > 0) &&
                            (Constants.UI_TYPE_CHECKBOX.equals(customFieldsSettings.getUiType()) || Constants.UI_TYPE_DROPDOWN.equals(customFieldsSettings.getUiType()) || Constants.UI_TYPE_RADIOBUTTON.equals(customFieldsSettings.getUiType()))) {
                        for (String value : customFieldsSettings.getPredefinedValues()) {
                            EdsCustomFormLocalization predefinedFields = new EdsCustomFormLocalization();
                            predefinedFields.setDefaultName(value);
                            predefinedFields.setEnglishName(value);
                            predefinedFields.setArabicName(value);
                            predefinedFields.setRussianName(value);
                            predefinedFields.setUzbekName(value);
                            predefinedFields.setParent(customFormLocalization);
                            customFormLocalizationManager.create(predefinedFields);
                        }
                    }
                    customFieldsSettings.setCustomFormlocalization(customFormLocalization);
                    companyCFSettingsManager.update(customFieldsSettings);
                    localization = customFormLocalization.getRPC();
                }
            }
            case SECTION -> {
                EdsCustomFormSection customFormSection = customFormSectionManager.get(id);
                if (customFormSection != null && customFormSection.getEdsCustomFormLocalization() != null) {
                    localization = customFormSection.getEdsCustomFormLocalization().getRPC();
                } else {
                    EdsCustomFormLocalization customFormLocalization = new EdsCustomFormLocalization();
                    customFormLocalization.setDefaultName(customFormSection.getLabel());
                    customFormLocalization.setEnglishName(customFormSection.getLabel());
                    customFormLocalization.setArabicName(customFormSection.getLabel());
                    customFormLocalization.setRussianName(customFormSection.getLabel());
                    customFormLocalization.setUzbekName(customFormSection.getLabel());
                    customFormLocalizationManager.create(customFormLocalization);
                    customFormSection.setEdsCustomFormLocalization(customFormLocalization);
                    customFormSectionManager.update(customFormSection);
                    localization = customFormLocalization.getRPC();
                }
            }
            case DASHBOARD -> {
                EdsDashboard dashboard = dashboardManager.get(id);
                if (dashboard != null && dashboard.getLocalization() != null) {
                    localization = dashboard.getLocalization().getRPC();
                } else {
                    EdsCustomFormLocalization cLocalization = new EdsCustomFormLocalization();
                    cLocalization.setDefaultName(dashboard.getName());
                    cLocalization.setEnglishName(dashboard.getName());
                    cLocalization.setArabicName(dashboard.getName());
                    cLocalization.setRussianName(dashboard.getName());
                    cLocalization.setUzbekName(dashboard.getName());
                    customFormLocalizationManager.create(cLocalization);
                    dashboard.setLocalization(cLocalization);
                    dashboardManager.update(dashboard);
                    localization = cLocalization.getRPC();
                }
            }
            case CONTAINER -> {
                EdsContainer container = containerManager.get(id);
                if (container != null && container.getLocalization() != null) {
                    localization = container.getLocalization().getRPC();
                } else {
                    EdsCustomFormLocalization cLocalization = new EdsCustomFormLocalization();
                    cLocalization.setDefaultName(container.getDefaultName());
                    cLocalization.setEnglishName(container.getDefaultName());
                    cLocalization.setArabicName(container.getDefaultName());
                    cLocalization.setRussianName(container.getDefaultName());
                    cLocalization.setUzbekName(container.getDefaultName());
                    customFormLocalizationManager.create(cLocalization);
                    container.setLocalization(cLocalization);
                    containerManager.update(container);
                    localization = cLocalization.getRPC();
                }
            }
            case DASHBOARD_COMPONENT, DASHBOARD_SUFFIX_COMPONENT, DASHBOARD_DIFFERENCE_COMPONENT,
                 DASHBOARD_COMPARISON_COMPONENT -> {
                EdsCustomFormLocalization customFormLocalization = null;
                if (id != null) {
                    customFormLocalization = customFormLocalizationManager.get(id);
                } else {
                    customFormLocalization = new EdsCustomFormLocalization();
                    customFormLocalizationManager.create(customFormLocalization);
                }
                localization = customFormLocalization.getRPC();
            }
        }
        return localization;
    }

    @Override
    public ArrayList<String> getItemTableCustomForms(String itemTable, Integer companyId) {
        ArrayList<String> companyCustomFieldsByEntityCategory = itemTable != null ? this.companyCFSettingsManager.getCompanyCustomFieldsByEntityNative(itemTable, companyId) : null;
        return companyCustomFieldsByEntityCategory;
    }

    @Override
    public Boolean customFormIsQuizForm(String formId) {
        EdsCustomForm customForm = this.customFormManager.findByFormID(formId);
        return customForm != null && customForm.getQuiz();
    }

    @Override
    public KanbanItemColumnConfigs[] getKanbanColumnConfigs(KanbanItemSettingEnum settingEnum) {
        EdsKanbanItemSettings kanbanItemSettings = kanbanItemSettingsManager.getSettingsByCode(settingEnum.getCode());

        KanbanItemColumnConfigs[] columnConfigs = null;
        if (kanbanItemSettings != null && StringUtils.isNotBlank(kanbanItemSettings.getSettingsJSONData())) {
            Gson gson = new Gson();
            columnConfigs = gson.fromJson(kanbanItemSettings.getSettingsJSONData(), KanbanItemColumnConfigs[].class);
        }
        if (columnConfigs == null) {
            columnConfigs = new KanbanItemColumnConfigs[]{};
        }

        for (KanbanItemColumnConfigs item : columnConfigs) {
            item.setLocalizationName(commonLocalizer.localize(item.getLocalizationCode(), item.getTitle()));
        }

        return columnConfigs;
    }

    @Override
    public Integer saveKanbanItemSettings(KanbanItemSettingEnum settingEnum, KanbanItemColumnConfigs[] columnConfigs) throws ObjectNotFoundException {
        if (settingEnum == null) {
            throw new ObjectNotFoundException("Object not found");
        }
        EdsKanbanItemSettings kanbanItemSettings = kanbanItemSettingsManager.getSettingsByCode(settingEnum.getCode());
        if (kanbanItemSettings == null) {
            kanbanItemSettings = new EdsKanbanItemSettings();
            kanbanItemSettings.setCode(settingEnum.getCode());
            kanbanItemSettings.setName(settingEnum.getName());
        }
        Gson gson = new Gson();
        kanbanItemSettings.setSettingsJSONData(gson.toJson(columnConfigs));

        kanbanItemSettingsManager.createOrUpdate(kanbanItemSettings);
        return kanbanItemSettings.getObjectID();
    }

    public SelectItem[] getRelatedFieldsBySectionName(String sectionCode) {
        if (KanbanItemSettingEnum.TASK_ITEM_SETTINGS.getCode().equals(sectionCode)) {
            return getTaskViewFields();
        } else if (KanbanItemSettingEnum.OPPORTUNITY_ITEM_SETTINGS.getCode().equals(sectionCode)) {
            return getOpportunityViewFields();
        } else if (KanbanItemSettingEnum.CASE_ITEM_SETTINGS.getCode().equals(sectionCode)) {
            return getCaseViewFields();
        } else if (KanbanItemSettingEnum.LEAD_ITEM_SETTINGS.getCode().equals(sectionCode)) {
            return getLeadViewFields();
        } else if (KanbanItemSettingEnum.CANDIDATE_ITEM_SETTINGS.getCode().equals(sectionCode)) {
            return getCandidateViewFields();
        } else {
            return new SelectItem[]{};
        }
    }

    private SelectItem[] getCaseViewFields() {
        int index = 1;
        List<SelectItem> caseFields = new ArrayList<>();
        caseFields.add(new SelectItem(index++, commonLocalizer.localize("number"), KanbanItemSettingEnum.CASE_NUMBER.getCode()));
        caseFields.add(new SelectItem(index++, commonLocalizer.localize("subject"), KanbanItemSettingEnum.CASE_SUBJECT.getCode()));
        caseFields.add(new SelectItem(index++, commonLocalizer.localize("reporter"), KanbanItemSettingEnum.CASE_REPORTER.getCode()));
        caseFields.add(new SelectItem(index++, commonLocalizer.localize("assignee"), KanbanItemSettingEnum.CASE_ASSIGNE_NAME.getCode()));
        caseFields.addAll(getCompanyCFAsSelectItemForKanbanItemSettings(ViewName.CrmCase, index));
        return caseFields.toArray(new SelectItem[]{});
    }

    private SelectItem[] getLeadViewFields() {
        int index = 1;
        List<SelectItem> leadFields = new ArrayList<>();
        leadFields.add(new SelectItem(index++, commonLocalizer.localize("name"), KanbanItemSettingEnum.LEAD_NAME.getCode()));
        leadFields.add(new SelectItem(index++, commonLocalizer.localize("assignee"), KanbanItemSettingEnum.LEAD_ASSIGNE_NAME.getCode()));
        leadFields.add(new SelectItem(index++, commonLocalizer.localize("account"), KanbanItemSettingEnum.LEAD_INFO.getCode()));
        leadFields.addAll(getCompanyCFAsSelectItemForKanbanItemSettings(ViewName.Lead, index));
        return leadFields.toArray(new SelectItem[]{});
    }

    private SelectItem[] getCandidateViewFields() {
        int index = 1;
        List<SelectItem> candidateFields = new ArrayList<>();
        candidateFields.add(new SelectItem(index++, commonLocalizer.localize("name"), KanbanItemSettingEnum.CANDIDATE_NAME.getCode()));
        candidateFields.add(new SelectItem(index++, commonLocalizer.localize("leadAssignee"), KanbanItemSettingEnum.CANDIDATE_LEAD_ASSIGNEE.getCode()));
        candidateFields.add(new SelectItem(index++, commonLocalizer.localize("position"), KanbanItemSettingEnum.CANDIDATE_POSITION.getCode()));
        candidateFields.add(new SelectItem(index++, commonLocalizer.localize("department"), KanbanItemSettingEnum.CANDIDATE_DEPARTMENT.getCode()));
        candidateFields.add(new SelectItem(index++, commonLocalizer.localize("location"), KanbanItemSettingEnum.CANDIDATE_DEPARTMENT.getCode()));
//        candidateFields.add(new SelectItem(index++, commonLocalizer.localize("lead"), KanbanItemSettingEnum.CANDIDATE_LEAD_NAME.getCode()));
        candidateFields.addAll(getCompanyCFAsSelectItemForKanbanItemSettings(ViewName.Candidate, index));
        return candidateFields.toArray(new SelectItem[]{});
    }

    private SelectItem[] getOpportunityViewFields() {
        int index = 1;
        List<SelectItem> opportunityFields = new ArrayList<>();
        opportunityFields.add(new SelectItem(index++, commonLocalizer.localize("name"), KanbanItemSettingEnum.OPPORTUNITY_NAME.getCode()));
        opportunityFields.add(new SelectItem(index++, commonLocalizer.localize("amount"), KanbanItemSettingEnum.OPPORTUNITY_AMOUT.getCode()));
        opportunityFields.add(new SelectItem(index++, commonLocalizer.localize("assignee"), KanbanItemSettingEnum.OPPORTUNITY_ASSIGNE_NAME.getCode()));
        opportunityFields.add(new SelectItem(index++, commonLocalizer.localize("closeDate"), KanbanItemSettingEnum.OPPORTUNITY_CLOSEDATE.getCode()));
        opportunityFields.add(new SelectItem(index++, commonLocalizer.localize("account"), KanbanItemSettingEnum.OPPORTUNITY_INFO.getCode()));
        opportunityFields.add(new SelectItem(index++, commonLocalizer.localize("backupAssignee"), KanbanItemSettingEnum.OPPORTUNITY_BACKUP_ASSIGNE_NAME.getCode()));
        opportunityFields.add(new SelectItem(index++, commonLocalizer.localize("contact"), KanbanItemSettingEnum.OPPORTUNITY_CONTACT.getCode()));
        opportunityFields.add(new SelectItem(index++, commonLocalizer.localize("phone"), KanbanItemSettingEnum.OPPORTUNITY_CONTACT_PHONE.getCode()));
        opportunityFields.addAll(getCompanyCFAsSelectItemForKanbanItemSettings(ViewName.Opportunity, index));
        return opportunityFields.toArray(new SelectItem[]{});
    }

    private SelectItem[] getTaskViewFields() {
        int index = 1;
        List<SelectItem> taskFields = new ArrayList<>();
        taskFields.add(new SelectItem(index++, commonLocalizer.localize("name"), KanbanItemSettingEnum.TASK_NAME.getCode()));
        taskFields.add(new SelectItem(index++, commonLocalizer.localize("code"), KanbanItemSettingEnum.TASK_CODE.getCode()));
        taskFields.add(new SelectItem(index++, commonLocalizer.localize("startDate"), KanbanItemSettingEnum.TASK_START_DATE.getCode()));
        taskFields.add(new SelectItem(index++, commonLocalizer.localize("endDate"), KanbanItemSettingEnum.TASK_END_DATE.getCode()));
        taskFields.add(new SelectItem(index++, commonLocalizer.localize("assignee"), KanbanItemSettingEnum.TASK_ASSIGNEE_EMPLOYEE.getCode()));
        taskFields.add(new SelectItem(index++, commonLocalizer.localize("priority"), KanbanItemSettingEnum.TASK_PRIORITY.getCode()));
        taskFields.add(new SelectItem(index++, commonLocalizer.localize("customerName"), KanbanItemSettingEnum.TASK_CUSTOMER_NAME.getCode()));
        taskFields.add(new SelectItem(index++, commonLocalizer.localize("projectName"), KanbanItemSettingEnum.TASK_PROJECTNAME.getCode()));
        taskFields.add(new SelectItem(index++, commonLocalizer.localize("description"), KanbanItemSettingEnum.TASK_DESCRIPTION.getCode()));
        taskFields.addAll(getCompanyCFAsSelectItemForKanbanItemSettings(ViewName.Task, index));
        return taskFields.toArray(new SelectItem[]{});
    }

    private List<SelectItem> getCompanyCFAsSelectItemForKanbanItemSettings(ViewName sectionViewName, int index) {
        List<SelectItem> newList = new ArrayList<>();
        List<EdsCompanyCustomFieldsSettings> companyCustomFieldsSettings = companyCFSettingsManager.getCFByUiTypes(sectionViewName.name(),
                Arrays.asList(UI_TYPE_TEXTBOX, UI_TYPE_DATEPICKER, UI_TYPE_DROPDOWN, UI_TYPE_TEXTBOX_EMAIL, UI_TYPE_LOOKUP, UI_TYPE_DATEPICKER_TIME), null);

        String userLocale = ServerUtils.getUserLocale().getLanguage();
        for (EdsCompanyCustomFieldsSettings item : companyCustomFieldsSettings) {
            String fieldName = item.getFieldNameLocalization(userLocale);
            newList.add(new SelectItem(index++, (fieldName != null ? fieldName : item.getFieldName()), item.getColumnCode()));
        }
        return newList;
    }

    public HashMap<String, KanbanItemColumnConfigs[]> getKanbanItemFieldsAsMap() {
        List<EdsKanbanItemSettings> allKanbanItemSettings = kanbanItemSettingsManager.findAll();
        HashMap<String, KanbanItemColumnConfigs[]> kanbanItemField = new HashMap<>();
        if (allKanbanItemSettings != null && allKanbanItemSettings.size() > 0) {
            allKanbanItemSettings.forEach(item -> {
                Gson gson = new Gson();
                KanbanItemColumnConfigs[] columnConfigs = gson.fromJson(item.getSettingsJSONData(), KanbanItemColumnConfigs[].class);
                kanbanItemField.put(item.getCode(), columnConfigs);
            });
        }
        return kanbanItemField;
    }

    public QuickAddColumnConfigs[] getQuickAddColumnConfigs(QuickAddSettingsForm form) {
        EdsQuickAddSettings settings = quickAddSettingsManager.getByForm(form);

        QuickAddColumnConfigs[] columnConfigs = null;
        if (settings != null && StringUtils.isNotBlank(settings.getSettingsJSONData())) {
            Gson gson = new Gson();
            columnConfigs = gson.fromJson(settings.getSettingsJSONData(), QuickAddColumnConfigs[].class);
        }
        if (columnConfigs == null) {
            columnConfigs = new QuickAddColumnConfigs[]{};
        }

        return columnConfigs;
    }

    public Integer saveQuickAddSettings(QuickAddSettingsForm form, QuickAddColumnConfigs[] columnConfigs) throws ObjectNotFoundException {
        if (form == null) {
            throw new ObjectNotFoundException("Object not found");
        }
        EdsQuickAddSettings settings = quickAddSettingsManager.getByForm(form);
        if (settings == null) {
            settings = new EdsQuickAddSettings();
            settings.setForm(form.toString());
        }
        Gson gson = new Gson();
        settings.setSettingsJSONData(gson.toJson(columnConfigs));

        quickAddSettingsManager.createOrUpdate(settings);
        return settings.getObjectID();
    }

    @Override
    public HashMap<String, SelectItem> getItemTableSettingsMap(String formId) {
        List<EdsCFItemTableSetting> cfItemTable = cfItemTableSettingmanager.findByFormId(formId);
        return cfItemTable.stream()
                .collect(Collectors.toMap(
                        EdsCFItemTableSetting::getUuid,
                        cfI -> {
                            SelectItem cfItem = new SelectItem();
                            cfItem.setName(cfI.getName());
                            cfItem.setId(cfI.getObjectID());
                            cfItem.setItemTableEntity(cfI.getEntity());
                            cfItem.setItemTableEntityId(cfI.getEntityId());
                            cfItem.setItemTableRelation(cfI.getRelationField());
                            cfItem.setItemTableRelationId(cfI.getRelationFieldId());
                            cfItem.setItemTableUuid(cfI.getUuid());
                            return cfItem;
                        },
                        (existing, replacement) -> replacement,
                        HashMap::new
                ));
    }


    @Override
    public HashMap<String, ArrayList<CustomTableRpc>> getItemTableValues(String formId, ArrayList<SelectItem> relationInfo) {
        HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();
        HashMap<Integer, ArrayList<CompanyCustomFieldItem>> cfs = new HashMap<>();
        HashMap<Integer, ArrayList<CompanyCustomFieldItem>> cfsValues = new HashMap<>();
        HashMap<Integer, ArrayList<SelectItem>> systemFieldValues = new HashMap<>();

        for (SelectItem relation : relationInfo) {
            ArrayList<SelectItem> items = new ArrayList<>();

            if (relation.getItemTableEntity() != null && relation.getItemTableRelation() != null) {
                processSelectedItems(relation, cfs, cfsValues, items, systemFieldValues);
            }

            processSystemAndCustomFieldItemsAndMap(relation, cfs, cfsValues, items, map, systemFieldValues);
        }

        return map;
    }

    @Override
    public SelectItem getUserLocation() {
        EdsUser user = userManager.getUser();
        if (user != null) {
            EdsLocation location = user.getLocation();
            if (location != null) {
                return new SelectItem(location.getObjectID(), location.getName());
            }
        }
        return null;
    }

    private void processSelectedItems(SelectItem item, HashMap<Integer, ArrayList<CompanyCustomFieldItem>> cfs,
                                      HashMap<Integer, ArrayList<CompanyCustomFieldItem>> cfsValues,
                                      ArrayList<SelectItem> items, HashMap<Integer, ArrayList<SelectItem>> systemFieldValues) {
        switch (item.getItemTableEntity()) {
            case EdsCFItemTableSetting.DEPARTMENT:
                processDepartmentItems(item, cfs, items, systemFieldValues);
                break;
            case EdsCFItemTableSetting.PROJECT:
                processProjectItems(item, cfs, items, systemFieldValues);
                break;
            case EdsCFItemTableSetting.LOCATION:
                processLocationItems(item, cfs, cfsValues, items, systemFieldValues);
                break;
        }
    }

    public void processDepartmentItems(SelectItem item, HashMap<Integer, ArrayList<CompanyCustomFieldItem>> cfs,
                                       ArrayList<SelectItem> items, HashMap<Integer, ArrayList<SelectItem>> systemFieldValues) {

        if (item.getItemTableRelation().equals(EdsCFItemTableSetting.EMPLOYEE)) {
            List<EdsModelField> modelFields = modelFieldManager.getModelFields(LayoutRPC.HRMS_EMPLOYEE_FORM);
            employeeManager.getEmployeesByDepartment(item.getId(), true).forEach(o -> {
                convertSystemFields(LayoutRPC.HRMS_EMPLOYEE_FORM, o, systemFieldValues, modelFields);
                cfs.put(o.getObjectID(), CustomFieldsUtils.setRPCCustomFieldItems(o.getCustomFields(), this.getCompanyCustomFields(ViewName.Employee)));
                items.add(new SelectItem(o.getObjectID(), o.getFormmattedName()));
            });
        } else if (item.getItemTableRelation().equals(EdsCFItemTableSetting.POSITION)) {
            List<EdsModelField> modelFields = modelFieldManager.getModelFields(LayoutRPC.POSITION_FORM);
            positionManager.getPositionListByDepartment(item.getId()).forEach(o -> {
                convertSystemFields(LayoutRPC.POSITION_FORM, o, systemFieldValues, modelFields);
                cfs.put(o.getObjectID(), CustomFieldsUtils.setRPCCustomFieldItems(o.getCustomFields(), this.getCompanyCustomFields(ViewName.Positions)));
                items.add(new SelectItem(o.getObjectID(), o.getName()));
            });
        }

    }


    private void processProjectItems(SelectItem item, HashMap<Integer, ArrayList<CompanyCustomFieldItem>> cfs,
                                     ArrayList<SelectItem> items, HashMap<Integer, ArrayList<SelectItem>> systemFieldValues) {
        if (item.getItemTableRelation().equals(EdsCFItemTableSetting.EMPLOYEE)) {
            List<EdsModelField> modelFields = modelFieldManager.getModelFields(LayoutRPC.HRMS_EMPLOYEE_FORM);
            projectManager.getEmployeesObjectByProject(item.getId()).forEach(o ->
                    {
                        convertSystemFields(LayoutRPC.HRMS_EMPLOYEE_FORM, o, systemFieldValues, modelFields);
                        cfs.put(o.getObjectID(), CustomFieldsUtils.setRPCCustomFieldItems(o.getCustomFields(), this.getCompanyCustomFields(ViewName.Employee)));
                        items.add(new SelectItem(o.getObjectID(), o.getName()));
                    }
            );

        }
    }

    private void processLocationItems(SelectItem item, HashMap<Integer, ArrayList<CompanyCustomFieldItem>> cfs,
                                      HashMap<Integer, ArrayList<CompanyCustomFieldItem>> cfsValues,
                                      ArrayList<SelectItem> items, HashMap<Integer, ArrayList<SelectItem>> systemFieldValues) {
        if (item.getItemTableRelation().equals(EdsCFItemTableSetting.POSITION)) {
            List<EdsModelField> modelFields = modelFieldManager.getModelFields(LayoutRPC.POSITION_FORM);
            positionManager.getPostionsByLocation(item.getId()).forEach(o ->
                    {
                        convertSystemFields(LayoutRPC.POSITION_FORM, o, systemFieldValues, modelFields);
                        cfs.put(o.getObjectID(), CustomFieldsUtils.setRPCCustomFieldItems(o.getCustomFields(), this.getCompanyCustomFields(ViewName.Positions)));
                        items.add(new SelectItem(o.getObjectID(), o.getName()));

                    }
            );
        } else if (item.getItemTableRelation().equals(EdsCFItemTableSetting.EMPLOYEE)) {
            List<EdsModelField> modelFields = modelFieldManager.getModelFields(LayoutRPC.HRMS_EMPLOYEE_FORM);
            employeeManager.getEmployeesByLocation(item.getId()).forEach(o ->
                    {
                        convertSystemFields(LayoutRPC.HRMS_EMPLOYEE_FORM, o, systemFieldValues, modelFields);
                        cfs.put(o.getObjectID(), CustomFieldsUtils.setRPCCustomFieldItems(o.getCustomFields(), this.getCompanyCustomFields(ViewName.Employee)));
                        items.add(new SelectItem(o.getObjectID(), o.getFormmattedName()));
                    }
            );

        } else if (item.getItemTableRelation().equals(EdsCFItemTableSetting.DEPARTMENT)) {
            List<EdsModelField> modelFields = modelFieldManager.getModelFields(LayoutRPC.DEPARTMENT_FORM);
            departmentManager.getDepartmentByLocationID(item.getId()).forEach(o ->
            {
                convertSystemFields(LayoutRPC.DEPARTMENT_FORM, o, systemFieldValues, modelFields);
                cfs.put(o.getObjectID(), CustomFieldsUtils.setRPCCustomFieldItems(o.getCustomFields(), this.getCompanyCustomFields(ViewName.Department)));

                items.add(new SelectItem(o.getObjectID(), o.getName()));
            });
        }
    }

    private void processSystemAndCustomFieldItemsAndMap(SelectItem item, HashMap<Integer, ArrayList<CompanyCustomFieldItem>> cfs,
                                                        HashMap<Integer, ArrayList<CompanyCustomFieldItem>> cfsValues,
                                                        ArrayList<SelectItem> items, HashMap<String, ArrayList<CustomTableRpc>> map, HashMap<Integer, ArrayList<SelectItem>> systemFieldValues) {
        ArrayList<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(null, this.getCompanyCustomFieldsByCategory(ViewName.CustomFormItemTable, item.getItemTableUuid()));
        HashMap<String, CompanyCustomFieldItem> cfMap = customFieldItems.stream()
                .collect(Collectors.toMap(CompanyCustomFieldItem::getAliasName, Function.identity(), (k1, k2) -> k1, HashMap::new));

        cfs.forEach((k, v) -> {
            ArrayList<CompanyCustomFieldItem> cfItems = new ArrayList<>();
            v.forEach(cfItem -> {
                CompanyCustomFieldItem candidateCustomField = cfMap.get(cfItem.getAliasName());
                if (candidateCustomField != null && !ServerUtils.isNullOrEmpty(cfItem.getFieldStringValue())) {
                    CompanyCustomFieldItem newCustomFieldItem = candidateCustomField.cloneObject();
                    newCustomFieldItem.setFieldStringValue(cfItem.getFieldStringValue());
                    newCustomFieldItem.setSelectedId(cfItem.getSelectedId());
                    newCustomFieldItem.setFieldDateNonConvertedValue(cfItem.getFieldDateNonConvertedValue());
                    cfItems.add(newCustomFieldItem);
                }
            });
            cfsValues.put(k, cfItems);
        });
        systemFieldValues.forEach((k, v) -> {
            final ArrayList<CompanyCustomFieldItem>[] customFieldItems1 = new ArrayList[]{cfsValues.get(k)};
            v.forEach(sysItem -> {
                CompanyCustomFieldItem candidateSystemField = cfMap.get(sysItem.getCode());
                if (candidateSystemField != null && !ServerUtils.isNullOrEmpty(sysItem.getName())) {
                    CompanyCustomFieldItem newSystemFieldItem = candidateSystemField.cloneObject();
                    newSystemFieldItem.setFieldStringValue(sysItem.getName());
                    newSystemFieldItem.setSelectedId(sysItem.getId());
                    if (customFieldItems1[0] == null) {
                        customFieldItems1[0] = new ArrayList<>();
                    }
                    customFieldItems1[0].add(newSystemFieldItem);
                }
            });
            cfsValues.put(k, customFieldItems1[0]);
        });
        AtomicInteger i = new AtomicInteger();
        customFieldItems.forEach(o -> {
            if (o.getLookUpTypeEnum() != null && o.getLookUpTypeEnum().name().equals(item.getItemTableRelation())) {
                if (items.size() == 0) {
                    map.computeIfAbsent(item.getItemTableUuid(), k -> new ArrayList<>(Collections.singletonList(new CustomTableRpc())));
                }
                for (SelectItem selectItem : items) {
                    CustomTableRpc rpc = new CustomTableRpc();
                    rpc.setUuid(item.getItemTableUuid());
                    ArrayList<CompanyCustomFieldItem> itemsToAdd = new ArrayList<>();
                    CompanyCustomFieldItem companyCustomFieldItem = customFieldItems.get(i.get());
                    CompanyCustomFieldItem companyCustomFieldItemNew = companyCustomFieldItem.cloneObject();
                    companyCustomFieldItemNew.setSelectedId(selectItem.getId());
                    companyCustomFieldItemNew.setFieldStringValue(selectItem.getName());
                    itemsToAdd.add(companyCustomFieldItemNew);
                    if (cfsValues.get(selectItem.getId()) != null) {
                        itemsToAdd.addAll(cfsValues.get(selectItem.getId()));
                    }
                    rpc.setItemCustomFields(itemsToAdd);
                    map.computeIfAbsent(item.getItemTableUuid(), x -> new ArrayList<>()).add(rpc);
                }
            }
            i.getAndIncrement();
        });
    }

    private void convertSystemFields(String formId, EdsObject object, HashMap<Integer, ArrayList<SelectItem>> systemFieldsMap, List<EdsModelField> modelFields) {

        ArrayList<SelectItem> systemFields = new ArrayList<>();

        switch (formId) {
            case LayoutRPC.HRMS_EMPLOYEE_FORM:
                EdsEmployee employee = (EdsEmployee) object;
                modelFields.forEach(modelField -> {
                    SelectItem item = new SelectItem();
                    item.setName(employee.getStringValue(modelField.getField_ID()));
                    item.setId(employee.getObjectID());
                    item.setCode(modelField.getField_ID());
                    systemFields.add(item);
                });
                break;
            case LayoutRPC.POSITION_FORM:
                EdsPosition position = (EdsPosition) object;
                modelFields.forEach(modelField -> {
                    SelectItem item = new SelectItem();
                    item.setName(position.getStringValue(modelField.getField_ID()));
                    item.setId(position.getObjectID());
                    item.setCode(modelField.getField_ID());
                    systemFields.add(item);
                });
                break;
            case LayoutRPC.DEPARTMENT_FORM:
                EdsDepartment department = (EdsDepartment) object;
                modelFields.forEach(modelField -> {
                    SelectItem item = new SelectItem();
                    item.setName(department.getStringValue(modelField.getField_ID()));
                    item.setId(department.getObjectID());
                    item.setCode(modelField.getField_ID());
                    systemFields.add(item);
                });
                break;
        }
        systemFieldsMap.put(object.getObjectID(), systemFields);
    }

    public ArrayList<AttendanceTerminal> getAttendanceTerminals() {
        return getAttendanceTerminals(null);
    }

    @Override
    public ArrayList<AttendanceTerminal> getAttendanceTerminals(String searchKey) {
        List<EdsAttendanceTerminal> terminals = attendanceTerminalManager.getAll(searchKey);
        ArrayList<AttendanceTerminal> list = new ArrayList<>();
        for (EdsAttendanceTerminal terminal : terminals) {
            AttendanceTerminal attendanceTerminal = new AttendanceTerminal();
            attendanceTerminal.setId(terminal.getObjectID());
            attendanceTerminal.setLocationId(terminal.getLocationId());
            attendanceTerminal.setCompanyUniqueID(terminal.getCompanyUniqueID());
            attendanceTerminal.setCompanyBranchName(terminal.getCompanyBranchName());
            attendanceTerminal.setDynamicStatus(terminal.getDynamicStatus());
            attendanceTerminal.setExternalId(terminal.getExternalId());
            list.add(attendanceTerminal);
        }
        return list;
    }

    @Override
    public AttendanceTerminal getAttendanceTerminal(Integer id) {
        EdsAttendanceTerminal terminal = attendanceTerminalManager.get(id);
        if (terminal == null) {
            return null;
        }
        AttendanceTerminal attendanceTerminal = new AttendanceTerminal();
        attendanceTerminal.setId(terminal.getObjectID());
        attendanceTerminal.setLocationId(terminal.getLocationId());
        if (terminal.getLocationId() != null) {
            EdsLocation location = locationManager.get(terminal.getLocationId());
            attendanceTerminal.setLocation(location != null ? location.getAsSelectItem() : null);
        }
        attendanceTerminal.setCompanyUniqueID(terminal.getCompanyUniqueID());
        attendanceTerminal.setCompanyBranchName(terminal.getCompanyBranchName());
        attendanceTerminal.setDynamicStatus(terminal.getDynamicStatus());
        attendanceTerminal.setExternalId(terminal.getExternalId());
        return attendanceTerminal;
    }

    @Override
    @Transactional
    public Integer saveAttendanceTerminal(AttendanceTerminal attendanceTerminal) {
        EdsAttendanceTerminal edsAttendanceTerminal = Optional.ofNullable(attendanceTerminalManager.get(attendanceTerminal.getId()))
                .orElse(new EdsAttendanceTerminal());
        if (edsAttendanceTerminal.getObjectID() != null && edsAttendanceTerminal.getExternalId() != null) {
            throw new IllegalStateException("Cannot modify a system-managed terminal");
        }
        edsAttendanceTerminal.setCompanyBranchName(attendanceTerminal.getCompanyBranchName());
        edsAttendanceTerminal.setCompanyUniqueID(attendanceTerminal.getCompanyUniqueID());
        edsAttendanceTerminal.setDynamicStatus(attendanceTerminal.getDynamicStatus());
        edsAttendanceTerminal.setLocationId(attendanceTerminal.getLocationId());
        attendanceTerminalManager.createOrUpdate(edsAttendanceTerminal);
        return 0;
    }

    @Override
    @Transactional
    public void deleteAttendanceTerminal(Integer id) {
        EdsAttendanceTerminal terminal = attendanceTerminalManager.get(id);
        if (terminal != null && terminal.getExternalId() != null) {
            throw new IllegalStateException("Cannot delete a system-managed terminal");
        }
        attendanceTerminalManager.delete(terminal);
    }

    @Override
    public ArrayList<AttendanceDeviceStatus> fetchAttendanceTerminalStatus() {
        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("sessionId", SecurityContext.getInstance().getSessionId());
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

            ResponseEntity<ArrayList<AttendanceDeviceStatus>> devices = restTemplate.exchange(
                    "https://attendance.kpi.com/api/v1/device/my",
                    HttpMethod.GET,
                    new HttpEntity<>(null, headers),
                    new ParameterizedTypeReference<>() {
                    }
            );

            return devices.getBody() != null ? devices.getBody() : new ArrayList<>();
        } catch (RestClientException e) {
            return new ArrayList<>();
        }
    }
}

