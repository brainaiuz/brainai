package com.edatasite.workforce.gwt.contact.server.app;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsEntity;
import com.edatasite.workforce.core.domain.EdsFormProperty;
import com.edatasite.workforce.core.domain.EdsItemCustomFields;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsServerContacts;
import com.edatasite.workforce.core.domain.EdsSpokenLanguages;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.EdsGoogleWFTGroups;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCareer;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactCategory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContactCustomItemTable;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContactDetails;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContactItemTableCF;
import com.edatasite.workforce.core.domain.crm.contact.EdsDeviceCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customfields.EdsEmployeeCustomFields;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailTracker;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.recruitment.EdsCandidateItemTable;
import com.edatasite.workforce.core.domain.recruitment.EdsCandidateItemTableCF;
import com.edatasite.workforce.core.domain.recruitment.EdsPlacement;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.settings.EdsRestHook;
import com.edatasite.workforce.core.domain.webforms.EdsWebForm;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.core.solr.component.EmployeeSolrComponent;
import com.edatasite.workforce.core.solr.document.ContactSolrDoc;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.contact.client.rpc.CommonItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.contact.client.rpc.GoogleGroupsSetting;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.server.ContactCategoryServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryList;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrContactRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CampaignManager;
import com.edatasite.workforce.gwt.core.server.db.CandidateItemTableCFManager;
import com.edatasite.workforce.gwt.core.server.db.CandidateItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.CaptchaManager;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyPdfTemplateManager;
import com.edatasite.workforce.gwt.core.server.db.ContactCareerManager;
import com.edatasite.workforce.gwt.core.server.db.ContactCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactItemParamsManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EntityManager;
import com.edatasite.workforce.gwt.core.server.db.FormPropertyManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleContactsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.LocationManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.PlacementManager;
import com.edatasite.workforce.gwt.core.server.db.PositionManager;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.SpokenLanguagesManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.VacancyManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BrandManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.UnitMeasurementManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.CrmCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.ItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailTrackerManager;
import com.edatasite.workforce.gwt.core.server.db.eventdispatcher.BusinessEventDispatcherManager;
import com.edatasite.workforce.gwt.core.server.db.googlegroups.GoogleGroupsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.CrmContactItemTableCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.CrmContactItemTableManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PaymentDeductionManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayrollCategoryManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.RestHookManager;
import com.edatasite.workforce.gwt.core.server.db.webforms.WebFormManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.SolrTransactionManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.CrmLeadEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WorkflowActionDetectedEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.CrmContactCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.SyncGoogleContactsEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365BaseList;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365ContactFolder;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365ContactService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.ContactCareerItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.googlecontacts.client.rpc.GoogleContactsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.app.RecruitmentServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.rest.base.enums.ContactParamEnum;
import com.edatasite.workforce.rest.base.to.AddressTO;
import com.edatasite.workforce.rest.base.to.ContactParamTO;
import com.edatasite.workforce.rest.base.to.ContactTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.EmailTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CountriesListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactAddressAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.CrmAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.EntityContactAddressTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.AddZapierContactTO;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactGroupEntry;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
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
import java.math.RoundingMode;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.EMPLOYEE_PROFILE;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.KANBAN_ORDER_GAP;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_EMPLOYEE_CONTACT;
import static com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants.TYPE_LEAD_CONTACT;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Nov 3, 2010
 * Time: 5:06:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
@Service("contactService")
public class ContactServiceImpl implements ContactService, ContactServiceLocal, Constants {
    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);
    @Autowired
    private CaptchaManager captchaManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CampaignManager campaignManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private CrmContactItemParamsManager contactItemParamsManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private CRMService crmService;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private EmailTrackerManager emailTrackerManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private SolrTransactionManager solrTransactionManager;
    @Autowired
    private GoogleContactsService googleContactsService;
    @Autowired
    private GoogleContactsManager googleContactsManager;
    @Autowired
    private BusinessEventDispatcherManager businessEventDispatcherManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private VacancyManager vacancyManager;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private ContactCareerManager contactCareerManager;
    @Autowired
    private WebFormManager webFormManager;
    @Autowired
    private CrmCustomFieldsManager crmCustomFieldsManager;
    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;
    @Autowired
    private MassMailService massMailService;
    @Autowired
    private GoogleGroupsManager googleGroupsManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private AttachmentManager attachmentsManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("countryLocalizer")
    private WfmMessageSource countryLocalizer;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    @Qualifier("hrmsService")
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private RecruitmentService recruitmentService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private PaymentDeductionManager paymentDeductionManager;
    @Autowired
    private PayrollCategoryManager categoryManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private Office365AuthService office365AuthService;
    @Autowired
    private Office365ContactService office365ContactService;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private SpokenLanguagesManager spokenLanguagesManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ContactCategoryServiceLocal contactCategoryServiceLocal;
    @Autowired
    private RestHookManager restHookManager;
    @Autowired
    private FormPropertyManager formPropertyManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    protected UnitMeasurementManager unitMeasurementManager;
    @Autowired
    private CrmContactItemTableCFManager crmContactItemCFManager;
    @Autowired
    private CrmContactItemTableManager crmContactItemTableManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ProductCategoryManager productCategoryManager;
    @Autowired
    private BrandManager brandManager;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ProductService productService;
    @Autowired
    private ItemCFManager itemCFManager;
    @Autowired
    private TelegramChatService telegramChatService;
    @Autowired
    @Qualifier("invoiceService")
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private PlacementManager placementManager;
    @Autowired
    private CompanyPdfTemplateManager companyPdfTemplateManager;
    @Autowired
    private RecruitmentServiceLocal recruitmentServiceLocal;
    @Autowired
    private CandidateItemTableManager candidateItemTableManager;
    @Autowired
    private CandidateItemTableCFManager candidateItemTableCFManager;
    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private PositionManager positionManager;
    RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private TimeSlotManager timeSlotManager;

    @Override
    @Transactional
    public Integer enableAccess(Integer contactID, boolean enable) {
        return enable ? clientService.enableAccess(contactID, null) : clientService.disableAccess(contactID);
    }

    @Override
    public Integer saveCandidate(ContactListItem item) {
        return recruitmentService.saveCandidate(item);
    }

    public Integer updateProfile(ProfileItem editProfile) {
        return hrmsServiceLocal.updateProfile(editProfile);
    }

    public ProfileItem editProfile(Integer objectID) {
        return hrmsServiceLocal.editProfile(objectID);
    }

    public ProfileItem editProfile(Integer objectID, String from, boolean isView) {
        return editProfile(objectID, from, isView, null, null, null);
    }

    public ProfileItem editProfile(Integer objectID, String from, boolean isView, Integer placementId, String fromType, Integer convertedFormId) {
        return hrmsServiceLocal.editProfile(objectID, from, isView, placementId, fromType, convertedFormId);
    }

    public ProfileItem getProfile(Integer objectID) {
        return hrmsServiceLocal.getProfile(objectID);
    }

    @Override
    public ProfileItem getEployeePdfTemplateList(Integer objectID) {
        ProfileItem profileItem = new ProfileItem();
        PdfTemplateItemList pdfTemplateItemList = invoiceServiceLocal.getCompanyPdfTemplates(EMPLOYEE_PROFILE);
        EdsCompany company = userManager.getUser().getCompany();
        if (!company.getCountry().getCode().equals("UZ")) {
            pdfTemplateItemList.setItems(Arrays.stream(pdfTemplateItemList.getItems())
                    .filter(i -> !(i.getName().equals("Объективка") || i.getName().equals("Obyektiv"))).toArray(SelectItem[]::new));
            profileItem.setTemplates(pdfTemplateItemList.getItems());
        } else {
            profileItem.setTemplates(pdfTemplateItemList.getItems());
        }

        return profileItem;
    }

    @Override
    public Boolean canDeleteCandidate(ArrayList<Integer> ids) {
        Boolean result = Boolean.TRUE;
        for (Integer id : ids) {
            EdsPlacement placement = placementManager.getPlacementByCandidateId(id);
            result &= (placement == null);
        }
        return result;
    }

/*@Override
    @Transactional
    public ArrayList<Integer> changeCategory(Integer categoryId, ArrayList<Integer> iDs, int action) {
        if (iDs != null && iDs.size() > 0) {
            List<EdsCrmContact> contacts = crmContactManager.getSharedOrOwnedContactsByIDs(iDs);
            if (contacts.size() > 0) {
                EdsContactCategory category = contactCategoryManager.get(categoryId);
                if (category != null) {
                    for (EdsCrmContact contact : contacts) {
                        if (contact != null) {
                            if (action == ContactCategoryListItem.MOVE) {
                                contact.getCategories().clear();
                            }
                            contact.addCategories(category);
                        }
                    }
                }
                try {
                    solrManager.addContactToIndex(contacts.toArray(new EdsCrmContact[]{}));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            iDs.removeAll(EdsCrmContact.getObjectIDs(contacts));
        }
        return iDs;
    }*/

    @Override
    @Transactional
    public void saveContactEditCellValue(ContactListItem rowValue, String columnCodeName) {
        if (rowValue == null || rowValue.getObjectId() == null) {
            return;
        }
        EdsCrmContact edsCrmContact = crmContactManager.get(rowValue.getObjectId());
        if (edsCrmContact == null) {
            return;
        }
        if (StringUtils.isBlank(columnCodeName)) {
            return;
        }

        edsCrmContact.clear();

        switch (columnCodeName) {
            case ContactListItem.COUNTRY, ContactListItem.STATE, ContactListItem.STREET, ContactListItem.STREET2, ContactListItem.CITY, ContactListItem.POST_CODE -> {
                Address address = edsCrmContact.getPrimaryAddressFromAll();
                Integer addressID = address != null ? address.getObjectID() : null;
                EdsAddress primaryAddress = addressID != null ? addressManager.get(addressID) : null;
                if (primaryAddress == null) {
                    primaryAddress = new EdsAddress();
                    primaryAddress.setRelationType(EdsCrmContactItemParams.WORK);
                    primaryAddress.setContact(edsCrmContact);
                    edsCrmContact.getAddresses().add(primaryAddress);
                }
                if (ContactListItem.COUNTRY.equals(columnCodeName)) {
                    primaryAddress.setCountry(countryManager.get(rowValue.getPrimaryAddress().getCountryId()));
                    primaryAddress.setState(null);
                } else {
                    if (ContactListItem.STATE.equals(columnCodeName) && primaryAddress.getCountry() != null) {
                        EdsRegion state = regionManager.getRegionByName(rowValue.getPrimaryAddress().getState());
                        if (state.getCountry().equals(primaryAddress.getCountry())) {
                            primaryAddress.setState(state);
                        }
                    } else {
                        if (ContactListItem.STREET.equals(columnCodeName)) {
                            primaryAddress.setAddress(rowValue.getPrimaryAddress().getAddress());
                        } else if (ContactListItem.CITY.equals(columnCodeName)) {
                            primaryAddress.setCity(rowValue.getPrimaryAddress().getCity());
                        } else if (ContactListItem.STREET2.equals(columnCodeName)) {
                            primaryAddress.setAddressb(rowValue.getPrimaryAddress().getAddressb());
                        } else if (ContactListItem.POST_CODE.equals(columnCodeName)) {
                            primaryAddress.setZipCode(rowValue.getPrimaryAddress().getZipCode());
                        }
                    }
                }
            }
            case ContactListItem.DATE_OF_BIRTH ->
                    edsCrmContact.setDateOfBirth(rowValue.getBirthDate().getNonConvertedDate());
            case ContactListItem.EMAIL -> {
                String primaryEmailName = edsCrmContact.getPrimaryEmail() == null ? edsCrmContact.getPrimaryEmailFromAll() : edsCrmContact.getPrimaryEmail();
                if (primaryEmailName == null) {
                    EdsCrmContactItemParams param = new EdsCrmContactItemParams();
                    param.setValue(rowValue.getPrimaryEmail());
                    param.setParam(EdsCrmContactItemParams.EMAIL);
                    param.setRelation(EdsCrmContactItemParams.WORK);
                    edsCrmContact.getItemParams().add(param);
                } else {
                    for (EdsCrmContactItemParams item : edsCrmContact.getItemParams(EdsCrmContactItemParams.EMAIL)) {
                        if (primaryEmailName.equalsIgnoreCase(item.getValue())) {
                            item.setValue(rowValue.getPrimaryEmail());
                            break;
                        }
                    }
                }
                edsCrmContact.setPrimaryEmail(rowValue.getPrimaryEmail());
            }
            case ContactListItem.CRM_ACCOUNT -> {
                EdsCrmAccount edsCrmAccount = crmAccountManager.get(rowValue.getCrmAccount().isNew() ? crmService.getOrCreateCrmAccount(rowValue.getCrmAccount().getName()) : rowValue.getCrmAccount().getObjectId());
                edsCrmContact.setCrmAccount(edsCrmAccount);
                if (edsCrmAccount != null) {
                    edsCrmContact.setEntityID(edsCrmAccount.getEntityID());
                }
            }
            case ContactListItem.CAMPAIGN -> {
                if (rowValue.getCampaignId() != null) {
                    EdsCampaign campaign = campaignManager.get(rowValue.getCampaignId());
                    edsCrmContact.setCampaign(campaign);
                } else if (StringUtils.isNotBlank(rowValue.getCampaign())) {
                    EdsCampaign campaign = campaignManager.getCampaignByName(rowValue.getCampaign());
                    edsCrmContact.setCampaign(campaign);
                }
            }
            case ContactListItem.REPORTS_TO -> edsCrmContact.setReportsTo(rowValue.getReportsTo());
            case ContactListItem.JOB_TITLE -> edsCrmContact.setJobTitles(rowValue.getJobTitle());
            case ContactListItem.DEPARTMENT -> edsCrmContact.setDepartment(rowValue.getDepartment());
            case ContactListItem.POSITION -> edsCrmContact.setPosition(rowValue.getPosition());
            case ContactListItem.LEAD_STATUS -> {
                recruitmentServiceLocal.insertCandidateStatusHistory(edsCrmContact, referenceManager.get(rowValue.getLeadStatus(true).getId()), rowValue.getLeadStatus(true).getCategory());
                edsCrmContact.setLeadStatus(rowValue.getLeadStatus(true).getId() == null ? null : referenceManager.get(rowValue.getLeadStatus(true).getId()));
                if (rowValue.isCandidate()) {
                    String statusCode = rowValue.getLeadStatus(true).getCode();
                    if (EdsCrmContact.CANDIDATE_STATUS_REJECTED.equals(statusCode) || EdsCrmContact.CANDIDATE_STATUS_UNQUALIFIED.equals(statusCode) || EdsCrmContact.CANDIDATE_STATUS_HIRED.equals(statusCode)) {
                        edsCrmContact.setShortList(false);
                    }
                    if (EdsCrmContact.CANDIDATE_STATUS_SHORTLIST.equals(statusCode)) {
                        edsCrmContact.setShortList(true);
                    }
                }
            }
            case ContactListItem.LEAD_SOURCE ->
                    edsCrmContact.setLeadSource(rowValue.getLeadSourceID() == null ? null : referenceManager.get(rowValue.getLeadSourceID()));
            case ContactListItem.LEAD_RATING ->
                    edsCrmContact.setLeadRating(rowValue.getLeadRatingID() == null ? null : referenceManager.get(rowValue.getLeadRatingID()));
            case ContactListItem.LEAD_ASSIGNEE ->
                    edsCrmContact.setLeadAssignee(rowValue.getLeadAssigneeID() == null ? null : employeeManager.get(rowValue.getLeadAssigneeID()));
            case ContactListItem.LEAD_BACKUP_ASSIGNEE ->
                    edsCrmContact.setLeadBackupAssignee(rowValue.getLeadBackupAssigneeID() == null ? null : employeeManager.get(rowValue.getLeadBackupAssigneeID()));
            case ContactListItem.OWNER ->
                    edsCrmContact.setOwner(rowValue.getOwnerId() == null ? null : employeeManager.get(rowValue.getOwnerId()));
            case ContactListItem.EMAIL_ALLOWED -> edsCrmContact.setEmailOptOut(rowValue.isEmailOptOut());
            case ContactListItem.CANDIDATE_SKILLS -> edsCrmContact.setSkills(rowValue.getSkills());
            default -> {
                EdsCrmCustomFields edsCrmCustomFields = edsCrmContact.getCustomFields();
                if (edsCrmCustomFields == null) {
                    edsCrmCustomFields = new EdsCrmCustomFields();
                    crmCustomFieldsManager.create(edsCrmCustomFields);
                    edsCrmContact.setCustomFields(edsCrmCustomFields);
                }
                Object ob = CustomFieldsUtils.getObjectValue(edsCrmCustomFields, columnCodeName);
                if (ob != null) {
                    if (ob instanceof String text) {
                        if (!text.equals(rowValue.getCustomFieldsMap().get(columnCodeName))) {
                            edsCrmContact.addChange(columnCodeName);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(rowValue.getCustomFieldsMap().get(columnCodeName))) {
                            edsCrmContact.addChange(columnCodeName);
                        }
                    } else if (ob instanceof Date date) {
                        if (!date.equals(rowValue.getCustomFieldsMap().get(columnCodeName))) {
                            edsCrmContact.addChange(columnCodeName);
                        }
                    }
                } else {
                    edsCrmContact.addChange(columnCodeName);
                }
                CustomFieldsUtils.setDomenObjectFieldChange(edsCrmCustomFields, rowValue.getCustomFieldsMap(), columnCodeName);
            }
        }
        try {
            crmContactManager.update(edsCrmContact);
            if (edsCrmContact.isPropertiesChanged()) {
                crmContactManager.createHistory(edsCrmContact);
            }
            contactSolrComponent.index(edsCrmContact);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsCrmContact, userManager.getUser());
        workflowEvent.setEntityType(edsCrmContact.is(EdsCrmContact.LEAD_CONTACT) ? RelationItem.TYPE_LEAD : (edsCrmContact.is(EdsCrmContact.CANDIDATE) ? RelationItem.TYPE_CANDIDATE : RelationItem.TYPE_CONTACT));
    }

    @Transactional
    public void updateContactsWithNoCategories() {
        Integer companyID = null;
        try {
            companyID = Integer.parseInt(SecurityContext.getInstance().getCompanyId());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (companyID != null) {
            List<EdsContactCategory> categories = contactCategoryManager.getDefaultCategoriesWithoutPrivateCategories();
            Map<Integer, EdsContactCategory> categoryMap = new HashMap<>();
            EdsContactCategory crmContacts = null;
            for (EdsContactCategory category : categories) {
                if (category != null && category.getConstantName() != null) {
                    if (category.getConstantName().contains("CRM")) {
                        categoryMap.put(EdsCrmContact.CRM_CONTACT, category);
                        crmContacts = category;
                    } else if (category.getConstantName().contains("Client")) {
                        categoryMap.put(EdsCrmContact.CLIENT_CONTACT, category);
                    } else if (category.getConstantName().contains("Supplier")) {
                        categoryMap.put(EdsCrmContact.SUPPLIER_CONTACT, category);
                    } else if (category.getConstantName().contains("Employee")) {
                        categoryMap.put(EdsCrmContact.EMPLOYEE_CONTACT, category);
                    }
                }
            }
            EdsUser user = employeeManager.getAdministrators().get(0);
            int startat = 0;
            int limit = 500;
            List<EdsCrmContact> contacts = crmContactManager.getCompanyContacts(companyID, startat, limit);
            while (!contacts.isEmpty()) {
                if (!contacts.isEmpty()) {
                    for (EdsCrmContact contact : contacts) {
                        if (contact != null && (contact.getCategories() == null || contact.getCategories().isEmpty())) {
                            contact.addCategories(categoryMap.get(contact.getContactType()));
                            if (contact.getCategories().isEmpty()) {
                                contact.addCategories(crmContacts);
                            }
                            try {
                                contactSolrComponent.index(contact);
                            } catch (InterruptedException e) {
                                baseEventPostProcessor.registerEvent(CrmContactCustomEventListenerImpl.TYPE, CrmContactCustomEventListenerImpl.EVENT_ADD_CRM_CONTACT_TO_SOLR, contact, user);
                            }
                        }
                    }
                } else {
                    break;
                }
                crmContactManager.flushAndClear();
                startat = contacts.get(contacts.size() - 1).getObjectID();
                contacts = crmContactManager.getCompanyContacts(companyID, startat, limit);
            }
            crmContactManager.flushAndClear();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactList getContactsByIDsFromDB(ListingFilterParameter fp, List<Integer> lessObjectIDs) {
        System.out.print(new Date() + " <---> ");
        List<ContactListItem> contactItems = new ArrayList<>();
        if (lessObjectIDs != null && lessObjectIDs.size() > 0) {
            List<EdsCrmContact> edsContacts = crmContactManager.getContactsByIDs(lessObjectIDs);
            if (edsContacts != null && edsContacts.size() > 0) {
                fp.setBriefly(false);
                for (EdsCrmContact edsContact : edsContacts) {
                    if (edsContact != null) {
                        contactItems.add(edsContact.getRPC(fp));
                    }
                }
            }
        }
        System.out.println(new Date());
        return new ContactList(contactItems.toArray(new ContactListItem[]{}), contactItems.size());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactList getContactsByIDsFromDBForExport(ListingFilterParameter fp, List<Integer> lessObjectIDs) {
        System.out.print(new Date() + " <---> ");
        List<ContactListItem> contactItems = new ArrayList<>();
        if (lessObjectIDs != null && lessObjectIDs.size() > 0) {
            contactItems = crmContactManager.getContactRPCsByIDsForCSVExport(lessObjectIDs);
            HashMap<Integer, HashMap<Integer, HashMap<Integer, ArrayList<String>>>> itemParams = contactItemParamsManager.getItemParamsByContactIDs(lessObjectIDs);
            Map<Integer, Map<Integer, ArrayList<Address>>> addresses = addressManager.getAddressesByContactIDs(lessObjectIDs);
            if (contactItems != null && contactItems.size() > 0) {
                for (ContactListItem item : contactItems) {
                    item.setItemParams(itemParams.get(item.getObjectId()));
                    if (addresses.containsKey(item.getObjectId())) {
                        ArrayList<Address> addressesList = new ArrayList<>();
                        for (Map.Entry<Integer, ArrayList<Address>> entry : addresses.get(item.getObjectId()).entrySet()) {
                            if (entry.getValue() != null && entry.getValue().size() > 0) {
                                addressesList.addAll(entry.getValue());
                            }
                        }
                        item.setAddresses(addressesList);
                    }
                }
            }
        }
        System.out.println(new Date());
        return new ContactList(contactItems.toArray(new ContactListItem[]{}), contactItems.size());
    }

    /*@Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResultTO<ContactTO> getContactListForAPI(ListingFilterParameter filterParameter) {
        ListResult<ContactListItem> result;
        if (filterParameter.getRelationID() != null && RelationItem.TYPE_CRM_ACCOUNT.equals(filterParameter.getRelationType())) {
            filterParameter.setEntityID(filterParameter.getRelationID());
        } else if (filterParameter.getRelationID() != null && RelationItem.TYPE_CAMPAIGN.equals(filterParameter.getRelationType())) {
            filterParameter.setCampaignID(filterParameter.getRelationID());
        }
        if (CrmConstants.TYPE_LEAD_CONTACT == filterParameter.getContactType()) {
            result = crmService.getNewLeads(filterParameter);
        } else {
            result = getNewContactList(filterParameter);
        }
        ArrayList<ContactTO> contactTOList = new ArrayList<>(result.getList().size());
        for (ContactListItem contactItem : result.getList()) {
            contactTOList.add(new ContactTO(contactItem, true));
        }

        return new ListResultTO<>(result.getTotal(), contactTOList);
    }*/

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<ContactListItem> getNewContactList(ListingFilterParameter filterParameter) {
//        System.out.println("START:::::" + new Date());
        ListLoadConfig config = new ListLoadConfig();
        config.setSortField(filterParameter.getSortField());
        config.setStart(filterParameter.getStart());
        config.setLimit(filterParameter.getLimit());
        config.setSortDir(filterParameter.isAscending() ? 1 : 2);
        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        if (panelTools == null) {
            ArrayList<String> columnCodeName = ContactListItem.defaultContactColumnNames;
            panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(columnCodeName);
        }
        filterParameter.setColumnsOfListing(panelTools.getColumnCodeName());
        if (panelTools.isCustomFieldsShown()) {
            filterParameter.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.Contact));
        }
        //Get Contacts list from solr
        ContactList contactList = getContactList(filterParameter, config);

        ListResult<ContactListItem> newContactList = new ListResult<>();
        newContactList.setTotal(contactList.getTotalCount());
        ArrayList<ContactListItem> newContactListItem = new ArrayList<>(Arrays.asList(contactList.getContactListItems()));
        newContactList.setList(newContactListItem);
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get contact list");
//        System.out.println("END:::::" + new Date());
        return newContactList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactList getContactList(ListingFilterParameter fp, ListLoadConfig config) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        ServerUtils.kpiLog(log, kpiLog, "Get contact list");
        EdsUser user;
        if (fp != null && fp.getUserID() != null) {
            user = userManager.get(fp.getUserID());
        } else {
            user = crmContactManager.getUser();
        }
        //if All Items must be retrieved
        if (fp != null && fp.isAllByFilter() != null && fp.isAllByFilter()) {
            int start = 0;
            int limit = config != null && config.getLimit() != 20 && config.getLimit() > 0 ? config.getLimit() : 200;
            int totalLength = 1;
            ArrayList<ContactListItem> contactListItems = new ArrayList<>();
            while (totalLength > start) {
                ListLoadConfig config1 = new ListLoadConfig();
                config1.setStart(start);
                config1.setLimit(limit);
                ContactList contactList = getContactList(fp, config1, user);
                totalLength = contactList.getTotalCount();
                contactListItems.addAll(Arrays.asList(contactList.getContactListItems()));
                start = start + limit;
            }
            return new ContactList(contactListItems, totalLength);
        } else {
            //If not all items requested then retrieve limited
            return getContactList(fp, config, user);
        }
    }

    private ContactList getContactList(ListingFilterParameter fp, ListLoadConfig config, EdsUser user) {
        Page<ContactSolrDoc> contactSolrDocPage = contactSolrComponent.getList(fp, config, user);
        return getContactFromSolrResult(contactSolrDocPage, fp);
    }

    private ContactList getContactFromSolrResult(Page<ContactSolrDoc> contactSolrDocPage, ListingFilterParameter fp) {
        ArrayList<ContactListItem> contactListItems = new ArrayList<>();
        EdsUser user = null;
        if (fp != null && fp.getUserID() != null) {
            user = userManager.get(fp.getUserID());
        }
        if (!contactSolrDocPage.isEmpty()) {
            // adding solr proposed results to map
            boolean validateUserGoogle = false;
            try {
                if (user != null) {
                    validateUserGoogle = googleContactsManager.validateUser(user);
                } else {
                    validateUserGoogle = validateUserGoogle();
                }
            } catch (Exception e) {
                log.error("Contact list error", e);
            }

            boolean validateUserOffice = false;
            try {
                if (user != null) {
                    validateUserOffice = googleContactsManager.validateOfficeUser(user);
                } else {
                    validateUserOffice = validateUserOffice();
                }
            } catch (Exception e) {
                log.error("Contact list error", e);
            }
            boolean isCooOrATM = ((Integer) 5377).equals(SecurityContext.getCompanyID()) || ((Integer) 8934).equals(SecurityContext.getCompanyID());
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            fp.setFromCoo(isCooOrATM);
            List<ContactSolrDoc> dataBaseContactIDs = null;
//            System.out.println("Checking existence in DB:::::" + new Date());
            if (!fp.isFromOutlook()) {
                dataBaseContactIDs = contactSolrComponent.getDocumentsExistingInBase(contactSolrDocPage.getContent(), RelationItem.TYPE_CONTACT);
            } else {
                dataBaseContactIDs = contactSolrComponent.getDocumentsExistingInBase2(contactSolrDocPage.getContent());
            }
//            System.out.println("Needed things Start:::::" + new Date());
            Map<Integer, EdsContactCategory> categories = null;
            if (!fp.isAsSelectItem() && (fp.getColumnsOfListing() == null || fp.getColumnsOfListing().isEmpty() || fp.getColumnsOfListing().contains(ContactListItem.CATEGORIES))) {
                List<EdsContactCategory> categoriesList = contactCategoryManager.getAllCategories(user != null ? user.getObjectID() : contactCategoryManager.getUser().getObjectID());
                categoriesList.addAll(contactCategoryManager.getSharedCategories(user != null ? user.getObjectID() : contactCategoryManager.getUser().getObjectID(), false));
                categories = EdsContactCategory.asMap(categoriesList);
            }
            Map<Integer, EdsCountry> countries = null;
            if (!fp.isAsSelectItem()) {
                countries = ServerUtils.getListAsMapIntegerAndValue(countryManager.list());
            }
            Map<Integer, Boolean> isNotActiveContacts = null;
            if (fp.getAccountID() != null) {
                isNotActiveContacts = clientContactManager.getMapIdAndIsActive(SolrUtils.getValuesFromField(SolrContactRepresenter.FIELD_CONTACT_ID, ((fp.isAsSelectItem() ? contactSolrDocPage.getContent() : dataBaseContactIDs))));
            }
//            System.out.println("TO:RPC:::::" + new Date());
            for (ContactSolrDoc contactSolrDoc : (fp.isAsSelectItem() ? contactSolrDocPage.getContent() : dataBaseContactIDs)) {
                contactListItems.add(getRPCFromContactSolrDoc(contactSolrDoc, fp, categories, countries, validateUserGoogle, validateUserOffice, isNotActiveContacts, null, null));
            }
        }
        return new ContactList(contactListItems.toArray(new ContactListItem[]{}), (int) contactSolrDocPage.getTotalElements());
    }

    @Override
    public void saveContactSyncSettings(String type, String storageType) {
        EdsUser user = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        if (userSettings == null) {
            userSettings = new EdsUserEmailSettings();
            userSettings.setUser(user);
        }
        if (OFFICE_365.equals(storageType)) {
            userSettings.setOfficeContactSyncType(type);
        } else {
            userSettings.setContactSyncType(type);
        }
        userEmailSettingsManager.createOrUpdate(userSettings);

    }

    @Override
    public CrmAccountItem getContactAccount(Integer contactID) {
        EdsCrmContact crmContact = crmContactManager.get(contactID);
        if (crmContact == null || crmContact.getCrmAccount() == null) {
            return null;
        }
        EdsCrmAccount crmAccount = crmContact.getCrmAccount();
        CrmAccountItem crmAccountItem = new CrmAccountItem();
        crmAccountItem.setObjectId(crmAccount.getObjectID());
        crmAccountItem.setName(crmAccount.getName());
        return crmAccountItem;
    }

    @Override
    public SelectItem getContactAccountSelect(Integer contactID) {
        EdsCrmContact crmContact = crmContactManager.get(contactID);
        if (crmContact == null || crmContact.getCrmAccount() == null) {
            return null;
        }
        EdsCrmAccount crmAccount = crmContact.getCrmAccount();
        SelectItem selectItem = new SelectItem();
        selectItem.setId(crmAccount.getObjectID());
        selectItem.setName(crmAccount.getName());
        selectItem.setReferenceCode(RelationItem.TYPE_CRM_ACCOUNT);
        return selectItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem getRPCFromContactSolrDoc(ContactSolrDoc contactSolrDoc, ListingFilterParameter fp, Map<Integer, EdsContactCategory> categories,
                                                    Map<Integer, EdsCountry> countries, boolean validateUserGoogle, boolean validateUserOffice,
                                                    Map<Integer, Boolean> forActivationLink, Map<Integer, Integer> hasPlacement, HashMap<Integer, EdsCrmAccount> crmAccountMap) {
        ContactListItem rpc = new ContactListItem();
        EdsUser user = userManager.getUser();
        boolean briefly = fp != null && fp.isBriefly();
        boolean iDsOnly = fp != null && fp.isIDsOnly();
        boolean asSelectItem = fp != null && fp.isAsSelectItem();
        if (asSelectItem || iDsOnly) {// asselectItemga boshqa narsalar qo'shilmasin... xato tashlaydi... P.S. CSVEXPORT
            rpc.setObjectId(contactSolrDoc.getContactId());
            if (!iDsOnly) {
                rpc.setFirstName(contactSolrDoc.getFirstName());
                rpc.setLastName(contactSolrDoc.getLastName());
                rpc.setRefIndNumber(contactSolrDoc.getRefIndNumber());
                rpc.setOwnerId(contactSolrDoc.getOwnerId());
            }
            return rpc;
        } else {
            if (!briefly) {
                rpc = getContact(crmContactManager.get(contactSolrDoc.getContactId()), fp);
            } else {
                rpc.setObjectId(contactSolrDoc.getContactId());
                rpc.setFirstName(contactSolrDoc.getFirstName());
                rpc.setLastName(contactSolrDoc.getLastName());
                rpc.setRefIndNumber(contactSolrDoc.getRefIndNumber());
                rpc.setContactName(SolrUtils.asString(contactSolrDoc.getContactName(), ""));
                rpc.setPrimaryContact(contactSolrDoc.getPrimaryContact());
                rpc.setTitle(SolrUtils.asString(contactSolrDoc.getTitle(), ""));
                rpc.setJobTitle(SolrUtils.asString(contactSolrDoc.getJobTitle(), ""));
                rpc.setPrimaryEmail(SolrUtils.asString(contactSolrDoc.getPrimaryEmail(), ""));
                rpc.setPrimaryPhone(SolrUtils.asString(contactSolrDoc.getPrimaryPhone(), "N/A"));
                rpc.getExtension().add(SolrUtils.asString(contactSolrDoc.getExtension(), "N/A"));
                rpc.getHomeFax().add(SolrUtils.asString(contactSolrDoc.getFax(), "N/A"));
                rpc.getMobile().add(SolrUtils.asString(contactSolrDoc.getMobile(), "N/A"));
                rpc.getWorkPhone().add(SolrUtils.asString(contactSolrDoc.getWorkPhone(), "N/A"));
                rpc.getHomeWebSite().add(SolrUtils.asString(contactSolrDoc.getWebsite(), "N/A"));
                rpc.setContactType(contactSolrDoc.getContactType());
                rpc.setUpdatedDate(contactSolrDoc.getUpdateDate());
                rpc.setCreatedDate(contactSolrDoc.getCreationDate());
                rpc.setHasToken(validateUserGoogle);
                rpc.setHasOfficeToken(validateUserOffice);
                rpc.setGoogleId(contactSolrDoc.getGoogleId());
                rpc.setDepartment(contactSolrDoc.getDepartment());
                rpc.setNumberData(new NumberData(contactSolrDoc.getNumber(), -1));
                if (contactSolrDoc.getDateOfBirth() != null) {
                    rpc.setBirthDate(new DateNonConvertable(contactSolrDoc.getDateOfBirth()));
                }
                rpc.setReportsTo(contactSolrDoc.getReportsTo());
                rpc.setReportsToId(contactSolrDoc.getReportsToId());
                rpc.setEmailOptOut(contactSolrDoc.getEmailAllowed());
                if (categories != null && categories.size() > 0 && contactSolrDoc.getCategoryId() != null) {
                    rpc.setCategoryNames(ServerUtils.getAsCommoDelimited(EdsContactCategory.getNames(fp.isFromCoo(), categories, contactSolrDoc.getCategoryId()), "N/A"));
                }
                rpc.setOwner(SolrUtils.asString(contactSolrDoc.getOwnerName(), ""));
                rpc.setOwnerId(contactSolrDoc.getOwnerId());
                rpc.setCreator(SolrUtils.asString(contactSolrDoc.getCreatorName(), ""));
                rpc.setUpdater(SolrUtils.asString(contactSolrDoc.getUpdaterName(), ""));
                if (contactSolrDoc.getAccountId() != null) {
                    CrmAccountItem crmAccountItem = new CrmAccountItem();
                    crmAccountItem.setObjectId(contactSolrDoc.getAccountId());
                    crmAccountItem.setName(contactSolrDoc.getAccountName());
                    crmAccountItem.setNumber(contactSolrDoc.getAccountNumber());
                    crmAccountItem.setIndustry(contactSolrDoc.getAccountIndustry());
                    crmAccountItem.setIndustryID(contactSolrDoc.getAccountIndustryId());
                    if (contactSolrDoc.getAccountId() != null) {
                        SelectItem[] accountTypes = crmAccountMap != null && crmAccountMap.size() > 0 ? crmAccountMap.get(crmAccountItem.getObjectId()) != null ? crmAccountMap.get(crmAccountItem.getObjectId()).getAccountTypeList() : null : crmAccountManager.get(crmAccountItem.getObjectId()).getAccountTypeList();
                        if (accountTypes != null && accountTypes.length > 0) {
                            crmAccountItem.setAccountTypes(accountTypes);
                        }
                    }
                    rpc.setCrmAccount(crmAccountItem);
                }
                rpc.setAccessEnabled(contactSolrDoc.getAccessEnabled());
                rpc.setClientContactId(contactSolrDoc.getClientContactId());
                if (rpc.isCandidate()) {
                    rpc.setCandidateStatus(SolrUtils.asReferenceItem(contactSolrDoc.getStatusId(), contactSolrDoc.getStatusCode()));
                    if (rpc.getLeadStatus(true) != null) {
                        rpc.getLeadStatus(true).setName(referenceWfmMessageSource.localize(contactSolrDoc.getStatusCode(), contactSolrDoc.getStatus()));
                    }
                    SelectItem candidateSourceItem = new SelectItem(contactSolrDoc.getLeadSourceId(), contactSolrDoc.getLeadSource());
                    if (contactSolrDoc.getLeadSourceCode() != null) {
                        String localedNameOfSource = referenceWfmMessageSource.localize(contactSolrDoc.getLeadSourceCode(), contactSolrDoc.getLeadSource());
                        candidateSourceItem.setName(localedNameOfSource);
                    }
                    if (rpc.getCandidateStatus() != null && rpc.getCandidateStatus().getId() != null && rpc.getCandidateStatus().getId() > 0) {
                        EdsReference status = referenceManager.get(rpc.getCandidateStatus().getId());
                        if (status != null) {
                            boolean draggable = false;
                            if (status.getAllowedRoles().isEmpty() || !status.getAllowedRoles().isEmpty() && user.hasEitherRoles(status.getAllowedRoles().toArray(new EdsRole[]{}))) {
                                rpc.setDraggable(true);
                                draggable = true;
                            }

                            if (status.getViewOnlyRoles() != null && !status.getViewOnlyRoles().isEmpty()
                                    && !draggable && user.hasEitherRoles(status.getViewOnlyRoles().toArray(new EdsRole[]{}))) {
                                rpc.setDraggable(false);
                            }

                            if (status.getOppEditBtnRole() == null || (status.getOppEditBtnRole() != null && status.getOppEditBtnRole().isEmpty()) || user == null || (user != null && user.hasEitherRoles(status.getOppEditBtnRole().toArray(new EdsRole[]{})))) {
                                rpc.setAllowEdit(true);
                            }
                        }
                    }

                    rpc.setCandidateSource(candidateSourceItem);
                    rpc.setSkills(contactSolrDoc.getCandidateSkills());
                    rpc.setProjectItem(SolrUtils.asSelectItem(contactSolrDoc.getCandidateProjectId(), contactSolrDoc.getCandidateProject()));
                    if (hasPlacement != null && hasPlacement.containsKey(rpc.getObjectId())) {
                        rpc.setHasPlacement(hasPlacement.get(rpc.getObjectId()) != null);
                        rpc.setPlacementId(hasPlacement.get(rpc.getObjectId()));
                    }
                } else if (rpc.isLeadContact()) {
                    rpc.setLeadAssignee(contactSolrDoc.getAssignee());
                    rpc.setLeadAssigneeID(contactSolrDoc.getAssigneeId());
                    rpc.setLeadBackupAssignee(contactSolrDoc.getBackupAssignee());
                    rpc.setLeadBackupAssigneeID(contactSolrDoc.getBackupAssigneeId());
                    rpc.setLeadStatus(SolrUtils.asReferenceItem(contactSolrDoc.getStatusId(), contactSolrDoc.getStatusCode()));
                    if (rpc.getLeadStatus(true) != null && rpc.getLeadStatus(true).getId() != null && rpc.getLeadStatus(true).getId() > 0) {
                        EdsReference status = referenceManager.get(rpc.getLeadStatus(true).getId());
                        if (status != null) {
//                            rpc.getLeadStatus(true).setName(referenceWfmMessageSource.localize(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_LEAD_STATUS_CODE), SolrUtils.asString(doc, SolrContactRepresenter.FIELD_LEAD_STATUS)));
                            rpc.getLeadStatus(true).setName(referenceWfmMessageSource.localize(status.getCode(), status.getName()));
                        }
                    }
                    rpc.setLeadSource(referenceWfmMessageSource.localize(contactSolrDoc.getLeadSourceCode(), contactSolrDoc.getLeadSource()));
                    rpc.setLeadSourceID(contactSolrDoc.getLeadSourceId());
                    rpc.setOtherLeadSource(contactSolrDoc.getLeadSourceOther());
                    rpc.setLeadRating(referenceWfmMessageSource.localize(contactSolrDoc.getRatingCode(), contactSolrDoc.getRating()));
                    rpc.setLeadRatingID(contactSolrDoc.getRatingId());
                }
                if (contactSolrDoc.getCountryName() != null || contactSolrDoc.getStateName() != null || contactSolrDoc.getStreet() != null || contactSolrDoc.getCity() != null || contactSolrDoc.getPostCode() != null) {
                    Address address = new Address();
                    address.setCountryId(contactSolrDoc.getCountryId());
                    address.setStateId(contactSolrDoc.getStateId());
                    if (countries != null && rpc.isLeadContact() && address.getCountryId() != null) {
                        EdsCountry country = countries.get(address.getCountryId());
                        boolean timeZoneFound = false;
                        if (address.getStateId() != null) {
                            EdsRegion state = regionManager.get(address.getStateId());
                            if (state != null && state.getCountry() != null && country != null && state.getCountry().getObjectID().equals(country.getObjectID()) && state.getTimeZone() != null) {
                                address.setCountry(countryLocalizer.localize(country.getCode(), country.getName()) + regionManager.getStateTimeZoneAndPhoneCode(country, state));
                                timeZoneFound = true;
                            }
                        }
                        if (!timeZoneFound && country != null) {
                            address.setCountry(countryLocalizer.localize(country.getCode(), country.getName()) + countryManager.getCountryTimeZoneAndPhoneCode(country));
                        }
                    } else {
                        address.setCountry(countryLocalizer.localize(contactSolrDoc.getCountryCode(), contactSolrDoc.getCountryName()));
                    }
                    address.setState(contactSolrDoc.getStateName());
                    address.setStateId(contactSolrDoc.getStateId());
                    address.setAddress(contactSolrDoc.getStreet());
                    address.setAddressb(contactSolrDoc.getStreet2());
                    address.setCity(contactSolrDoc.getCity());
                    address.setZipCode(contactSolrDoc.getPostCode());
                    address.setLongitude(contactSolrDoc.getLongitude());
                    address.setLatitude(contactSolrDoc.getLatitude());
                    rpc.setPrimaryAddress(address);
                } else {
                    rpc.setPrimaryAddress(null);
                }
                if (contactSolrDoc.getCampaignId() != null) {
                    rpc.setCampaign(contactSolrDoc.getCampaignName());
                    rpc.setCampaignId(contactSolrDoc.getCampaignId());
                }
                if (fp.isWithImage()) {
                    EdsCrmContact contact = crmContactManager.get(rpc.getObjectId());
                    if (contact != null && contact.getPhoto() != null) {
                        rpc.setContactImageUrl(getImageUrl(contact.getPhoto().getObjectID()));
                        rpc.setContactImageID(contact.getPhoto().getObjectID());
                    }
                    //Only mobile asked contact account's logo
                    if (fp.isFromMobile() && rpc.getCrmAccount().getObjectId() != null) {
                        EdsCrmAccount edsCrmAccount = crmAccountManager.get(rpc.getCrmAccount().getObjectId());
                        if (edsCrmAccount != null && edsCrmAccount.getLogo() != null) {
                            rpc.getCrmAccount().setLogoUrl(getImageUrl(edsCrmAccount.getLogo().getObjectID()));
                        }
                    }
                }
                if (forActivationLink != null && forActivationLink.containsKey(rpc.getObjectId())) {
                    if (!forActivationLink.get(rpc.getObjectId())) {
                        rpc.setAccessEnabled(true);
                    }//
                    rpc.setActive(forActivationLink.get(rpc.getObjectId()));
                    rpc.setClientContact(true);
                    EdsClientContact contact = clientContactManager.getClientContactByCrmContact(rpc.getObjectId());
                    rpc.setAccessStatus(contact.getAccountStatus().getName());

                }
                if (contactSolrDoc.getVacancyId() != null && contactSolrDoc.getVacancyId().size() > 0) {
                    ArrayList<SelectItem> vacancies = new ArrayList<>();
                    Collections.addAll(vacancies, ServerUtils.asListToSelectItem(contactSolrDoc.getVacancyId(), contactSolrDoc.getVacancyName()));
                    rpc.setVacancies(vacancies);
                }
            }
            if (fp.isCustomFieldsShown()) {
                rpc.setCustomFieldsMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(contactSolrDoc, fp.getColumnsOfListing()));
            }
            if (fp.getColumnsOfListing() != null && fp.getColumnsOfListing().contains(ContactListItem.MAILING_LIST)) {
                rpc.setMailingLists(ServerUtils.asListToString(contactSolrDoc.getMailListName()));
            }
            return rpc;
        }
    }

    public SolrQuery getSolrQueryForContact(ListingFilterParameter fp, ListLoadConfig config, EdsUser user) {
        return crmServiceLocal.getSolrQueryForContact(fp, config, user);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem getContact(Integer objectId, Boolean fromMobile) {
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        kpiLog.setEntityId(objectId);
        ServerUtils.kpiLog(log, kpiLog, "Contact view");
        EdsCrmContact contact = new EdsCrmContact();
        if (objectId != null) {
            contact = crmContactManager.get(objectId);
        }
        ListingFilterParameter fp = new ListingFilterParameter(false);
        fp.setFromMobile(fromMobile != null && fromMobile);
        return getContact(contact, fp);
    }

    private void wrapSpokenLanguages(EdsCrmContact contact, ContactListItem item) {
        ArrayList<EdsSpokenLanguages> spokenLanguages = spokenLanguagesManager.getListByRelation(contact.getObjectID(), EdsSpokenLanguages.TYPE_CANDIDATE);
        if (spokenLanguages != null) {
            ArrayList<SpokenLanguageItem> languageItems = new ArrayList<>(spokenLanguages.size());
            spokenLanguages.forEach(sl -> languageItems.add(new SpokenLanguageItem(sl.getLanguage() != null ? new SelectItem(sl.getLanguage().getObjectID(), referenceWfmMessageSource.localize(sl.getLanguage().getCode(), sl.getLanguage().getName())) : null, sl.getLevel() != null ? new SelectItem(sl.getLevel().getObjectID(), referenceWfmMessageSource.localize(sl.getLevel().getCode(), sl.getLevel().getName())) : null)));
            item.setSpokingLanguages(languageItems);
        }
    }


    public ContactListItem[] getStatusHistory(Integer objectID, Integer contactType, boolean isContactHistory) {
        EdsCrmContact contact = crmContactManager.get(objectID);
        if (contact != null && (contact.is(contactType) || isContactHistory)) {
            List<ContactListItem> histories = new ArrayList<>();
            if (contact.getConvertedDate() != null && contact.getAuditInfo() != null) {
                ContactListItem item = new ContactListItem();
                if (contact.getAuditInfo().getModifiedBy() != null) {
                    item.setOwner(contact.getAuditInfo().isSuperUser() ? defaultSupportName : contact.getAuditInfo().getModifiedBy().getFullName());
                    item.setOwnerId(contact.getAuditInfo().getModifiedBy().getObjectID());
                }
                item.setUpdatedDate(contact.getConvertedDate());
                item.setCreatedDate(contact.getHistoricalParent() != null ? contact.getHistoricalParent().getAuditInfo().getCreationDate() : contact.getAuditInfo().getCreationDate());
                item.setLeadStatus(new ReferenceItem(1));
                item.getLeadStatus(true).setName(commonLocalizer.localize(PdfLocalizationName.converted));
                histories.add(item);
            }
            for (EdsCrmContact history : contact.getHistories()) {
                ContactListItem historyItem = new ContactListItem();
                if (history != null) {
                    if (history.getAuditInfo() != null) {
                        if (history.getAuditInfo().getModifiedBy() != null) {
                            historyItem.setOwner(history.getAuditInfo().isSuperUser() ? defaultSupportName : history.getAuditInfo().getModifiedBy().getFullName());
                            historyItem.setOwnerId(history.getAuditInfo().getModifiedBy().getObjectID());
                        }
                        historyItem.setUpdatedDate(history.getAuditInfo().getModificationDate());
                        historyItem.setCreatedDate(history.getHistoricalParent().getAuditInfo().getCreationDate());
                        if (history.getLeadStatus() != null) {
                            historyItem.setLeadStatus(history.getLeadStatus().getRPC());
                            historyItem.getLeadStatus(true).setName(referenceWfmMessageSource.localizeRef(history.getLeadStatus()));
                        }
                    }
                }
                histories.add(historyItem);
            }
            return histories.toArray(new ContactListItem[]{});
        }
        return null;
    }

    @Transactional
    public ContactListItem getContact(EdsCrmContact crmContact, ListingFilterParameter fp) {
        long startedAt = System.currentTimeMillis();
        ContactListItem item = new ContactListItem();
        EdsCrmContact contact;
        if (crmContact != null) {
            contact = crmContact;
            item = contact.getRPC(fp, item);
            if (crmContact.is(EdsCrmContact.CANDIDATE)) {
                item.setLeadAssignees(crmServiceLocal.getOwnersListByPermission(PermissionConstants.HRMS_SHOW_IN_CANDIDATE_OWNER));
                Set<EdsCandidateItemTable> itemTables = new HashSet<>();
                if (contact != null) {
                    item.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_PLACEMENT, contact.getObjectID())));
                    itemTables = contact.getCandidateItemTables();
                }

                Map<String, List<CustomTableRpc>> map = new HashMap<>();

                if (itemTables != null && itemTables.size() > 0) {

                    for (EdsCandidateItemTable itemTable : itemTables) {
                        CustomTableRpc rpc = itemTable.getRpc();

                        rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                                commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.CandidateCustomItemTable, rpc.getUuid())));

                        map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                    }
                    item.setCandidateCustomTableItems(map);
                }
            } else {
                item.setLeadAssignees(crmServiceLocal.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE));
            }
            item.setRelationships(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact.CONTACT_RELATION), ServerUtils.REFERENCE));
            item.setMartialStatusList(this.commonServiceLocal.convertReference2SelectItem(EdsEmployeeProfile.MARTIAL_STATUS, false, null));
            item.setContactImAddress(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact._IM_ADDRESSES), ServerUtils.REFERENCE));
            if (crmContact.is(EdsCrmContact.CANDIDATE)) {
                item.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_CANDIDATE, crmContact.getObjectID())));
            }
            if (item.isLeadContact() && contact.getLeadSource() != null) {
                item.setLeadSource(referenceWfmMessageSource.localize(contact.getLeadSource().getCode(), contact.getLeadSource().getName()));
            }
            if (item.isLeadContact() && contact.getLeadStatus() != null) {
                item.getLeadStatus(true).setName(referenceWfmMessageSource.localize(contact.getLeadStatus().getCode(), contact.getLeadStatus().getName()));
            }
            EdsDepartment candidateDepartment = crmContact.getCandidateDepartment();
            EdsPosition candidatePosition = crmContact.getCandidatePosition();
            if (candidateDepartment != null) {
                item.setDepartmentItem(new SelectItem(candidateDepartment.getObjectID(), candidateDepartment.getName()));
            }
            if (candidatePosition != null) {
                item.setPositionItem(new SelectItem(candidatePosition.getObjectID(), candidatePosition.getName()));
            }

            EdsTimeSlot timeSlot = crmContact.getTimeSlot();
            if (timeSlot != null) {
                item.setTimeSlotItem(new SelectItem(timeSlot.getObjectID(), timeSlot.getName()));
            }
            item.setPassportNumber(contact.getPassportNumber());

//            if (!ServerUtils.isNullOrEmpty(item.getTitle())) {
//                String title = item.getTitle().replace(".", "");
//                item.setTitle(referenceWfmMessageSource.localize(StringUtils.upperCase(title)));
//            }
            if (item.getTitleId() != null) {
                EdsReference title = referenceManager.get(item.getTitleId());
                item.setTitle(title.getName());
            }
            Map<Integer, SelectItem> map = SelectItem.asMap(item.getRelationships());
            if (item.getSelectedRelationships() != null && item.getSelectedRelationships().size() > 0) {
                for (final SelectItem relation : item.getSelectedRelationships()) {
                    if (map.containsKey(relation.getId()) && map.get(relation.getId()) != null) {
                        relation.setName(map.get(relation.getId()).getName());
                    }
                }
            }
            Map<Integer, SelectItem> contactIMAddressmap = SelectItem.asMap(item.getContactImAddress());
            if (item.getSelectedContactImAddress() != null && item.getSelectedContactImAddress().size() > 0) {
                for (final SelectItem imAddress : item.getSelectedContactImAddress()) {
                    if (contactIMAddressmap.containsKey(imAddress.getId()) && contactIMAddressmap.get(imAddress.getId()) != null) {
                        imAddress.setName(contactIMAddressmap.get(imAddress.getId()).getName());
                    }
                }
            }
            if (item.isLeadContact() && contact.getPrimaryAddressFromAll() != null && contact.getPrimaryAddressFromAll().getCountryId() != null) {
                Address primaryAddress = item.getPrimaryAddress(true);
                EdsCountry country = countryManager.get(contact.getPrimaryAddressFromAll().getCountryId());
                primaryAddress.setCountry(countryLocalizer.localize(country.getCode(), country.getName()) + countryManager.getCountryTimeZoneAndPhoneCode(country));
                item.setPrimaryAddress(primaryAddress);
            }
            item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(contact.getCustomFields(), commonService.getCompanyCustomFields(item.isLeadContact() ? ViewName.Lead : (item.isCandidate() ? ViewName.Candidate : ViewName.Contact))));
            if (fp != null && !fp.isForCSVonly() && !fp.isFromMobile()) {
                item.setDepartments(getContactSelectItems(CONTACT_DEPARTMENTS));
                item.getCrmAccount().setIndustries(getContactSelectItems(_COMPANY_WORKAREA));
                item.getCrmAccount().setAccountTypes(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE), ServerUtils.REFERENCE));
                if (item.getCrmAccount() != null) {
                    item.setSupervisors(crmService.getContactsByAccount(item.getCrmAccount().getObjectId(), item.getObjectId()));
                }
                if (contact.getCrmAccount() != null) {
                    item.setCrmAccount(contact.getCrmAccount().getRPC(item.getCrmAccount(), false));
                    if (item.getCrmAccount().getIndustry() != null) {
                        item.getCrmAccount().setIndustry(referenceWfmMessageSource.localize(item.getCrmAccount().getIndustryCode(), item.getCrmAccount().getIndustry()));
                    }
                }
                item.setPermissionForEntireUser(getContactPermission(contact.getObjectID()));
                if (crmContact.getAssets() != null) {
                    item.setAssets(crmContact.getAssets());
                } else {
                    item.setAssets(" ");
                }
                if (contact.getPhoto() != null) {
                    item.setContactImageUrl(getImageUrl(contact.getPhoto().getObjectID()));
                    item.setContactImageID(contact.getPhoto().getObjectID());
                }
                item.setAccessEnabled(contact.isAccessEnabled());
                item.setPrimaryContact(contact.getPrimaryContact());
                ListingFilterParameter filterParametrs = fp;
                filterParametrs.setRelationID(contact.getObjectID());
                if (contact.is(EdsCrmContact.LEAD_CONTACT)) {
                    filterParametrs.setLeadID(contact.getObjectID());
                    filterParametrs.setRelationType(RelationItem.TYPE_LEAD);
                } else {
                    filterParametrs.setRelationType(RelationItem.TYPE_CONTACT);
                    filterParametrs.setContactID(contact.getObjectID());
                }
                HistoryList historyList = crmServiceLocal.getCrmNoteHistory(filterParametrs);
                if (historyList != null) {
                    item.setHistory(historyList);
                    item.setAllHistory(historyList);
                }
                try {
                    item.setHasToken(validateUserGoogle());
                } catch (Exception e) {
                    item.setHasToken(false);
                }
                try {
                    item.setHasOfficeToken(validateUserOffice());
                } catch (Exception e) {
                    item.setHasOfficeToken(false);
                }
                filterParametrs.setEntityID(null);
                if (crmContact.getBackgroundInformation() != null) {
                    item.setBackgroundInformation(crmContact.getBackgroundInformation());
                }
                if (crmContact.getCompanyPhoto() != null) {
                    item.setCompanyPhotoUrl(getImageUrl(crmContact.getCompanyPhoto().getObjectID()));
                }

                if (crmContact.getDisclaimer() != null) {
                    item.setDisclaimer(crmContact.getDisclaimer());
                }
                Set<EdsVacancy> vacancies = crmContact.getVacancies();
                if (vacancies != null && vacancies.size() > 0) {
                    ArrayList<SelectItem> vacanciesList = new ArrayList<>();
                    ArrayList<VacancyItem> vacancyitems = new ArrayList<>();
                    for (EdsVacancy vacancy : vacancies) {
                        SelectItem asSelectItem = vacancy.getAsSelectItem();
                        asSelectItem.setSelected(true);
                        vacanciesList.add(asSelectItem);

                        VacancyItem vacancyItem = new VacancyItem();
                        vacancyItem.setObjectID(vacancy.getObjectID());
                        vacancyItem.setJobTitle(vacancy.getJobTitle());
                        vacancyItem.setReferenceLocale(vacancy.getLocale() != null ? vacancy.getLocale().toRPC() : null);
                        vacancyItem.setDepartment(vacancy.getDepartment() != null ? new ReferenceItem(vacancy.getDepartment().getObjectID(), vacancy.getDepartment().getName(), null,
                                vacancy.getDepartment().getLocale() != null ? vacancy.getDepartment().getLocale().toRPC() : null) : null);
                        vacancyItem.setLocation(vacancy.getLocation() != null ? new ReferenceItem(vacancy.getLocation().getObjectID(), vacancy.getLocation().getName(), null,
                                vacancy.getLocation().getLocale() != null ? vacancy.getLocation().getLocale().toRPC() : null) : null);
                        vacancyItem.setPositions(vacancy.getPosition() != null ? Collections.singletonList(new ReferenceItem(vacancy.getPosition().getObjectID(), vacancy.getPosition().getName(), null,
                                vacancy.getPosition().getLocale() != null ? vacancy.getPosition().getLocale().toRPC() : null)).toArray(new ReferenceItem[]{}) : null);
                        vacancyitems.add(vacancyItem);
                    }
                    item.setVacancies(vacanciesList);
                    item.setVacancyItems(vacancyitems);
                }

                wrapSpokenLanguages(contact, item);
            }

            if (crmContact.getCrmContactItems().size() > 0) {
                OpportunityItem[] items = new OpportunityItem[crmContact.getCrmContactItems().size()];
                ArrayList<OpportunityItem> listItems = new ArrayList<>();
                int index = 0;
                for (EdsCrmContactItem crmContactItem : crmContact.getCrmContactItems()) {
                    items[index] = new OpportunityItem();
                    items[index].setItemID(crmContactItem.getItem() != null ? crmContactItem.getItem().getObjectID() : null);
                    items[index].setItemName(crmContactItem.getItem() != null ? crmContactItem.getItem().getName() : crmContactItem.getItemName());
                    items[index].setItemNumber(crmContactItem.getItem() != null ? crmContactItem.getItem().getProductNumber() : "");
                    items[index].setQty(crmContactItem.getQty());
                    items[index].setDescription(crmContactItem.getDescription());
                    items[index].setPrice(crmContactItem.getPrice());
                    if (crmContactItem.getUnitMeasurement() != null) {
                        items[index].setUnitMeasurement(crmContactItem.getUnitMeasurement().getAsSelectItem());
                    }
                    if (crmContactItem.getCategory() != null) {
                        items[index].setProductCategory(new SelectItem(crmContactItem.getCategory().getObjectID(), crmContactItem.getCategory().getName()));
                    }
                    if (crmContactItem.getBrand() != null) {
                        items[index].setProductBrand(new SelectItem(crmContactItem.getBrand().getObjectID(), crmContactItem.getBrand().getName()));
                    }
                    items[index].setSupplierID(crmContactItem.getSupplierID());
                    items[index].setSupplierName(crmContactItem.getSupplierName());

                    ArrayList<CompanyCustomFieldItem> itemCustomFields = new ArrayList<>();

                    for (CompanyCustomFieldItem customFieldItem : commonService.getCompanyCustomFields(ViewName.OpportunitySubItem)) {
                        itemCustomFields.add(customFieldItem.cloneObject());
                    }
                    items[index].setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(crmContactItem.getCustomFields(), itemCustomFields));
                    listItems.add(items[index]);
                    index++;
                }
                item.setItems(listItems.toArray(new OpportunityItem[0]));

                if (crmContact != null) {
                    Set<EdsCrmContactCustomItemTable> itemTables = crmContact.getItemTables();

                    HashMap<String, ArrayList<CustomTableRpc>> map1 = new HashMap<>();

                    if (itemTables != null || itemTables.size() > 0) {

                        for (EdsCrmContactCustomItemTable itemTable : itemTables) {
                            CustomTableRpc rpc = itemTable.getRpc();

                            rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                                    commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.OpportunityItemTable, rpc.getUuid())));

                            map1.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                        }
                        item.setCustomTableItems(map1);
                    }
                    Map<String, ArrayList<CustomTableRpc>> tableItems = item.getCustomTableItems();


                    for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                        tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
                    }
                }
            }
            if (crmContact.getAuditInfo() != null) {
                item.setCreator(crmContact.getAuditInfo().getCreatedBy() != null ? crmContact.getAuditInfo().getCreatedBy().getFullName() : "");
                item.setUpdater(crmContact.getAuditInfo().getModifiedBy() != null ? crmContact.getAuditInfo().getModifiedBy().getFullName() : "");
            }
            Map<Integer, ArrayList<String>> telegramChats = contact.getParams(EdsCrmContactItemParams.TELEGRAM_CHATS);
            if (telegramChats.size() > 0) {
                ArrayList<SelectItem> chats = new ArrayList<>();
                for (Integer botId : telegramChats.keySet()) {
                    TelegramSettingsItem bot = telegramChatService.getTelegramSettingsItem(botId);
                    TelegramChatListItem chat = telegramChatService.getChat(Integer.valueOf(telegramChats.get(botId).get(0)));
                    chats.add(new SelectItem(botId, bot.getBotName(), chat.getChatName()));
                }
                item.setTelegramChats(chats);
            }
            List<EdsPaymentDeduction> list = paymentDeductionManager.getEmpployeePaymentDeductions(contact.getObjectID(), true);
            if (list != null && list.size() > 0) {
                ArrayList<PaymentDeductionObject> objectList = new ArrayList<>();
                for (EdsPaymentDeduction deduction : list) {
                    PaymentDeductionObject deductionObject = new PaymentDeductionObject();
                    deductionObject.setCategoryItem(deduction.getRPC().getCategoryItem());
                    deductionObject.setCategoryname(deduction.getCategory() != null ? deduction.getCategory().getName() : "");
                    deductionObject.setType(deduction.getPayType());
                    deductionObject.setPaymentAmount(deduction.getPaymentAmount());
                    objectList.add(deductionObject);
                }
                item.setAllowanceCategories(objectList);
            }
        }
        item.setTemplates(getCandidatePdfTempeletes(PdfReferenceCodeNameEnum.CANDIDATE_FORM.name()).getItems());
        System.out.println("TOOK: " + (System.currentTimeMillis() - startedAt) + " ms to generate ITEM");
        return item;
    }

    private CustomFormItemPdfTemplateList getCandidatePdfTempeletes(String type) {
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

    public SelectItem[] getContactSelectItems(String parentCode) {
        List<EdsReference> getReferences = referenceManager.listReferences(parentCode);
        SelectItem[] selectItems;
        if (CONTACT_DEPARTMENTS.equals(parentCode) || _COMPANY_WORKAREA.equals(parentCode) || INDUSTRIES.equals(parentCode) || NUMBER_OF_EMPLOYEES.equals(parentCode) || CONTACT_ORGANIZATION_TYPES.equals(parentCode) || _TITLE.equals(parentCode)) {
            List<EdsReference> references = getReferences.stream()
                    .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparing(EdsReference::getName))),
                            ArrayList::new));
            selectItems = new SelectItem[references.size()];
            int i = 0;
            for (EdsReference reference : references) {
                selectItems[i] = new SelectItem(reference.getObjectID(), referenceWfmMessageSource.localize(reference.getCode(), reference.getName()), reference.getCode());
                i++;
            }
            Arrays.sort(selectItems, comparing(SelectItem::getName));
        } else {
            selectItems = new SelectItem[getReferences.size()];
            int i = 0;
            for (EdsReference reference : getReferences) {
                selectItems[i] = new SelectItem(reference.getObjectID(), referenceWfmMessageSource.localize(reference.getCode(), reference.getName()));
                i++;
            }
        }
        return selectItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PermissionHolder getContactPermission(Integer contactID) {
        return crmContactManager.getPermission(contactID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactCareerItem[] getContactCareers(Integer contactID) {
        List<EdsContactCareer> contactCareers = contactCareerManager.getContactCareers(contactID);

        ContactCareerItem[] careerItems = new ContactCareerItem[contactCareers.size()];
        int i = 0;
        for (EdsContactCareer career : contactCareers) {
            careerItems[i] = getContactCareer(career);
            i++;
        }
        if (careerItems.length > 0) {
            return careerItems;
        }
        return new ContactCareerItem[0];
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactCareerItem getContactCareer(Integer careerID) {
        EdsContactCareer career = contactCareerManager.get(careerID != null ? careerID : 0);
        return getContactCareer(career);
    }

    private ContactCareerItem getContactCareer(EdsContactCareer career) {
        ContactCareerItem careerItem = new ContactCareerItem();
        if (career != null) {
            careerItem.setCareerID(career.getObjectID());
        }
        if (career != null && career.getCity() != null) {
            careerItem.setCity(career.getCity());
        }
        if (career != null && career.getCompanyName() != null) {
            careerItem.setCompanyName(career.getCompanyName());
        }
        if (career != null && career.getCountry() != null) {
            careerItem.setCountryID(career.getCountry().getObjectID());
            careerItem.setCountryName(career.getCountry().getName());
        } else {
//            careerItem.setCountryID(companyManager.getUser().getCompany().getCountryZone().getCountry().getObjectID());
        }
        careerItem.setCountries(ServerUtils.sortSelectItem(commonService.getCountries()));
        careerItem.setCurrentYear(career != null && career.getCurrentYear() != null ? career.getCurrentYear() : false);
        if (career != null && career.getIndustry() != null) {
            careerItem.setIndustryID(career.getIndustry().getObjectID());
            careerItem.setIndustryName(referenceWfmMessageSource.localizeRef(career.getIndustry()));
        }
        careerItem.setIndustries(ServerUtils.getAsSelectItem(referenceManager.listReferences(_COMPANY_WORKAREA), ServerUtils.REFERENCE));
        if (career != null && career.getContact() != null) {
            careerItem.setContactID(career.getContact().getObjectID());
        }
        if (career != null && career.getJobTitle() != null) {
            careerItem.setJobTitle(career.getJobTitle());
        }
        if (career != null && career.getFromYear() != null) {
            careerItem.setFromYear(career.getFromYear());
        }
        if (career != null && career.getToYear() != null) {
            careerItem.setToYear(career.getToYear());
        }
        return careerItem;
    }

    @Transactional
    public Integer saveContactCareer(ContactCareerItem careerItem) {
        EdsContactCareer contactCareer;
        if (careerItem.getCareerID() != null) {
            contactCareer = contactCareerManager.get(careerItem.getCareerID());
        } else {
            contactCareer = new EdsContactCareer();
        }
        contactCareer.setCompanyName(careerItem.getCompanyName());
        contactCareer.setJobTitle(careerItem.getJobTitle());
        contactCareer.setCity(careerItem.getCity());
        EdsCrmContact contact = crmContactManager.get(careerItem.getContactID());
        contactCareer.setContact(contact);
        if (careerItem.getCountryID() != null) {
            EdsCountry country = countryManager.get(careerItem.getCountryID());
            contactCareer.setCountry(country);
        }
        if (careerItem.getIndustryID() != null) {
            EdsReference industry = referenceManager.get(careerItem.getIndustryID());
            contactCareer.setIndustry(industry);
        }
        if (careerItem.getFromYear() != null) {
            contactCareer.setFromYear(careerItem.getFromYear());
        }
        if (careerItem.getToYear() != null) {
            contactCareer.setToYear(careerItem.getToYear());
        }
        contactCareer.setCurrentYear(careerItem.isCurrentYear());

        if (contactCareer.getObjectID() != null) {
            contactCareerManager.update(contactCareer);
            crmServiceLocal.createContactHistory("Updated career information - \"" + (contactCareer.getJobTitle() != null ? contactCareer.getJobTitle() : "") + " " + contactCareer.getCompanyName() + "\"", contact);
        } else {
            contactCareerManager.create(contactCareer);
            crmServiceLocal.createContactHistory("Added career information - \"" + (contactCareer.getJobTitle() != null ? contactCareer.getJobTitle() : "") + " " + contactCareer.getCompanyName() + "\"", contact);
        }
        return contactCareer.getObjectID();
    }

    @Transactional
    public void deleteContactCareer(Integer careerID) {
        EdsContactCareer career = contactCareerManager.get(careerID);
        career.setDeleted(true);
        crmServiceLocal.createContactHistory("Deleted career information - \"" + career.getJobTitle() + " " + career.getCompanyName() + "\"", career.getContact());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem getContactForSync(EdsCrmContact crmContact, boolean briefly) {
        ContactListItem item = new ContactListItem();
        EdsCrmContact contact = new EdsCrmContact();
        if (crmContact != null) {
            contact = crmContact;

            item = contact.getRPC(new ListingFilterParameter(briefly), item);

            if (contact.getPhoto() != null) {
                item.setContactImageUrl(getImageUrl(contact.getPhoto().getObjectID()));
            }
            item.setHasToken(true);
            item.setHasOfficeToken(true);
        }
        return item;
    }

    @Transactional
    public HashMap<ContactListItem, Integer> saveMultipleContacts(ArrayList<ContactListItem> contacts, boolean runWebhook) {
        HashMap<ContactListItem, Integer> returning = new HashMap<>();
        if (!contacts.isEmpty()) {
            for (ContactListItem item : contacts) {
                if (item != null) {
                    Integer result = saveContact(item, null, runWebhook);
                    if (result != null && result < 0) {
                        returning.put(item, result);
                    }
                }
            }
        }
        return returning;
    }
    public Integer getEntityIdByLeadId(Integer leadId){
        return crmContactManager.getByLeadID(leadId);
    };


    @Transactional
    public Integer saveContact(ContactListItem item, ArrayList<Integer> mailingList, boolean runWebhook) {
        EdsUser user = null;
        if (crmContactManager.getUser() != null && crmContactManager.getUser().getObjectID() != null) {
            user = userManager.get(crmContactManager.getUser().getObjectID());
        }
        return saveContact(item, mailingList, user, false, runWebhook);
    }

    @Transactional
    public SelectItem saveContact(ContactListItem item, ArrayList<Integer> mailingList) {
        EdsUser user = null;
        if (crmContactManager.getUser() != null && crmContactManager.getUser().getObjectID() != null) {
            user = userManager.get(crmContactManager.getUser().getObjectID());
        }
        Integer contactId = saveContact(item, mailingList, user, false, true);
        EdsCrmContact edsCrmContact = crmContactManager.get(contactId);
        SelectItem selectItem = new SelectItem(contactId);
        if (edsCrmContact != null && edsCrmContact.getCrmAccount() != null) {
            selectItem.setEntityId(edsCrmContact.getCrmAccount().getObjectID());
            selectItem.setDescription(edsCrmContact.getCrmAccount().getName());
        }
        return selectItem;
    }

    @Transactional
    public Integer saveContact(ContactListItem item, ArrayList<Integer> mailingList, EdsUser user, boolean savingFromCrmAccount, boolean runWebhook) {
        String requestedFrom = "";
        boolean isNewAccount;
        requestedFrom = item.getCreatedFrom();
        if (item.isCheckForDuplicates()) {
            List<EdsCrmContact> contacts = crmContactManager.getDuplicates(item.getObjectId(), item.getFirstName(), item.getLastName(), item.getPrimaryEmail(), item.getPrimaryPhone(), item.isLeadContact());
            if (contacts != null && contacts.size() > 0) {
                for (EdsCrmContact contact : contacts) {
                    if (item.getPrimaryEmail() != null && !"".equals(item.getPrimaryEmail()) && item.getPrimaryEmail().equals(contact.getPrimaryEmail()) && (item.isEmployeeContact() || TYPE_EMPLOYEE_CONTACT == (contact.getContactType()) || (contact.getCrmAccount() != null && contact.getCrmAccount().isClient()) || TYPE_LEAD_CONTACT == contact.getContactType())) {
                        return -2;
                    }
                }
                for (EdsCrmContact contact : contacts) {
                    if (item.getPrimaryPhone() != null && !"".equals(item.getPrimaryPhone()) && item.getPrimaryPhone().equals(contact.getPrimaryPhone()) && (item.isEmployeeContact() || TYPE_EMPLOYEE_CONTACT == (contact.getContactType()) || (contact.getCrmAccount() != null && contact.getCrmAccount().isClient()) || TYPE_LEAD_CONTACT == contact.getContactType())) {
                        return -3;
                    }
                }
                if (item.getWebFormID() == null && !item.isNameNotUnique()) {//we dont need to check for names if the contact is from webform.
                    for (EdsCrmContact contact : contacts) {
                        if (contact.getFirstName() != null && item.getFirstName() != null && !"".equals(item.getFirstName().trim()) && contact.getFirstName().equals(item.getFirstName())
                                && contact.getLastName() != null && item.getLastName() != null && !"".equals(item.getLastName().trim()) && contact.getLastName().equals(item.getLastName())) {
                            return -1;
                        }
                    }
                }
            }
        }
        if (item.getWebFormID() != null && item.getObjectId() == null) {
            System.out.println(item.getAntibot());
            if (!captchaManager.validateCaptcha(item.getAntibot())) {
                return Constants.ANTIBOT_ERROR;
            }
        }
        if (user == null) {
            user = crmContactManager.getUser();
        }
        if (user == null && item.getOwnerId() != null) {
            user = userManager.get(item.getOwnerId());
        }
        EdsCrmContact contact = null;
        if (item.getObjectId() != null) {
            contact = crmContactManager.get(item.getObjectId());
        } else {
            if (item.getEntityID() != null) {
                contact = EdsCrmContact.getPreferedItem(crmContactManager.getContactByEntityID(item.getEntityID()), item.getPrimaryEmail());
            }
        }
        if (contact == null) {
            contact = new EdsCrmContact();
            contact.setCreator(user);
            if (ContactListItem.REQUEST_FROM_CONTACT_SYNC.equals(requestedFrom)) {
                EdsContactCategory privateCategory = contactCategoryManager.getPrivateCategory(ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_CONTACT_LIST)).get(0);
                contact.addCategories(privateCategory);
            }
        }
        contact.setVersion(contact.getVersion() + 1);
        contact.clear();
        EdsCrmContactDetails contactDetails = null;
        if (contact.getCrmContactDetails() == null) {
            contactDetails = new EdsCrmContactDetails();
            contact.setCrmContactDetails(contactDetails);
        } else {
            contactDetails = contact.getCrmContactDetails();
        }

        if (ContactListItem.REQUEST_FROM_CONTACT_SYNC.equals(requestedFrom)) {
            //contactDetails.setGooglenote(item.getNote() != null ? item.getNote() : "");//we decided with Munir not to sync google note
        } else {
            if (item.getWebFormID() != null) {
                EdsWebForm webForm = webFormManager.get(item.getWebFormID());
                contact.setLeadWebForm(webForm);
                if (user == null && webForm != null) {
                    user = userManager.get(webForm.getOwner().getObjectID());
                }
            }

            contactDetails.setBackgroundInformation(item.getBackgroundInformation());
            contactDetails.setDisclaimer(item.getDisclaimer());
            contact.setAssets(item.getAssets());
            EdsAttachment companyPhoto = null;
            if (item.getCompanyPhotoId() != null) {
                companyPhoto = attachmentsManager.get(item.getCompanyPhotoId());
            }
            contact.setCompanyPhoto(companyPhoto);
            EdsAttachment photo = null;
            if (item.getPhotoId() != null && contact.getPhoto() == null) {
                photo = attachmentsManager.get(item.getPhotoId());
            } else if (contact.getPhoto() != null) {
                photo = attachmentsManager.get(contact.getPhoto().getObjectID());
            }
            contact.setPhoto(photo);
            contact.setReportsTo(item.getReportsTo());
            contact.setReportsToId(item.getReportsToId());
        }
        contact.setOwner(item.getOwnerId() != null ? userManager.get(item.getOwnerId()) : user);
        contact.setContactType(item.getContactType());
        if (item.getSyncID() > 0) {
            contact.setSyncID(item.getSyncID());
        }
        contact.setSaasuGUID(item.getSaasuUID());
        //Personal Information
        contact.setFirstName(item.getFirstName());
        if (item.isPrimaryContact() && !contact.getPrimaryContact() && (contact.getCrmAccount() != null || item.getCrmAccount().getObjectId() != null)) {
            unCheckExistingPrimaryContact(contact.getCrmAccount() != null ? contact.getCrmAccount().getObjectID() : item.getCrmAccount().getObjectId());
        }
        contact.setRefIndNumber(item.getRefIndNumber());
        if (!item.isPrimaryContact() && item.getCrmAccount() != null) {
            boolean isPrimary = item.getCrmAccount().getObjectId() == null || !crmContactManager.hasContactsByCrmAccount(item.getCrmAccount().getObjectId());
            contact.setPrimaryContact(isPrimary);
        } else {
            contact.setPrimaryContact(item.isPrimaryContact());
        }

        if (contact.isPrimaryContact()) {
            EdsCrmAccount edsCrmAccount = contact.getCrmAccount();
            if (edsCrmAccount != null) {
                for (EdsCrmContact edsCrmContact : edsCrmAccount.getCrmContacts()) {
                    if (!edsCrmContact.getObjectID().equals(contact.getObjectID())) {
                        edsCrmContact.setPrimaryContact(Boolean.FALSE);
                    }
                }
            }
        }
        contact.setLastName(item.getLastName());
        contact.setMiddleName(item.getMiddleName());
        contact.setGender(item.getGender());
        if (item.getMartialStatusId() != null) {
            contact.setMartialStatus(this.referenceManager.get(item.getMartialStatusId()));

        }
        contact.setOtherName(item.getOtherName());
        contact.setShowAccountAddress(item.getShowAccountAddress());
        boolean employeeChanged = !Objects.deepEquals(contact.getDateOfBirth(), item.getBirthDate() != null ? item.getBirthDate().getNonConvertedDate() : null) && contact.is(EdsCrmContact.EMPLOYEE_CONTACT);
        contact.setDateOfBirth(item.getBirthDate() != null ? item.getBirthDate().getNonConvertedDate() : null);
        contact.setGoogleId(StringUtils.isNotBlank(item.getGoogleId()) ? item.getGoogleId() : null);
        contact.setJobFunction(StringUtils.isNotBlank(item.getJobFunction()) ? item.getJobFunction() : null);
        contact.setJobTitles(StringUtils.isNotBlank(item.getJobTitle()) ? item.getJobTitle() : null);
        contact.setDepartment(StringUtils.isNotBlank(item.getDepartment()) ? item.getDepartment() : null);

        if (item.getObjectId() == null && item.getContactType() == null) {
            item.setContactType(ContactListItem.CRM_CONTACT);
        }
        contact.setEntityID(item.getEntityID());
        //contact.setTitle(item.getTitle());
        if (item.getTitleId() != null) {
            EdsReference reference = referenceManager.get(item.getTitleId());
            if (reference != null) {
                contact.setTitleRef(reference);
                if (!"other".equalsIgnoreCase(reference.getName())) {
                    contact.setTitle(reference.getName());
                } else {
                    contact.setTitle(item.getTitle());
                }
            } else {
                contact.setTitle(item.getTitle());
            }
        } else {
            contact.setTitleRef(null);
            contact.setTitle("");
        }
        if (!(item.getSyncID() > 0)) {
            if (item.getCampaignId() != null) {
                contact.setCampaign(campaignManager.get(item.getCampaignId()));
            } else {
                contact.setCampaign(null);
            }
        }
        contact.setEmailOptOut(item.isEmailOptOut());
        contact.setPrimaryPhone(StringUtils.isNotBlank(item.getPrimaryPhone()) ? item.getPrimaryPhone() : null);
        contact.setPrimaryEmail(StringUtils.isNotBlank(item.getPrimaryEmail()) ? item.getPrimaryEmail() : null);

        //SET DEVICE CONTACT SYNC FIELDS
        if (!WebServiceUtils.isEmptyOrNull(item.getDeviceContactID(), item.getDeviceID())) {
            contact.setDeleted(false);
            EdsDeviceCrmContact deviceCrmContact = contact.getDeviceCrmContact(item.getDeviceID(), true);
            if (deviceCrmContact == null) {
                deviceCrmContact = new EdsDeviceCrmContact();
                deviceCrmContact.setStatus(ContactListItem.SYNCED);
                deviceCrmContact.setDeviceID(item.getDeviceID());
                deviceCrmContact.setDeviceContactID(item.getDeviceContactID());
                contact.getDeviceCrmContacts().add(deviceCrmContact);
            } else {
                deviceCrmContact.setStatus(ContactListItem.SYNCED);
                deviceCrmContact.setDeviceID(item.getDeviceID());
                deviceCrmContact.setDeviceContactID(item.getDeviceContactID());
            }
        } else {
            contact.setDeviceCrmContactsStatusIfExist(ContactListItem.UPDATED);
        }

        if (item.getObjectId() == null && item.getWebFormID() != null) {
            EdsWebForm webForm = webFormManager.get(item.getWebFormID());
            if (webForm != null && webForm.getWebFormSource() != null) {
                contact.setLeadSource(webForm.getWebFormSource());
            }
            contact.setLeadWebForm(webForm);
        }
        boolean isHrms = false;
        boolean newCreated = crmContactManager.createOrUpdate(contact);
        if (item.isCandidate()) {
            if (item.getNumberData() != null) {
                contact.setNumber(item.getNumberData().getNumberString());
                contact.setNumberInteger(item.getNumberData().getIntNumber());
            }
            if (item.getProjectItem() != null && item.getProjectItem().getId() != null) {
                contact.setCandidateProject(projectManager.get(item.getProjectItem().getId()));
            } else {
                contact.setCandidateProject(null);
            }
            Map<String, List<CustomTableRpc>> customTableItems = item.getCandidateCustomTableItems();

            for (Map.Entry<String, List<CustomTableRpc>> map : customTableItems.entrySet()) {
                List<CustomTableRpc> values = map.getValue();
                if (contact.getObjectID() != null) {
                    for (CustomTableRpc customTableRpc : values) {
                        List<EdsCandidateItemTable> oldValuesPlacement = candidateItemTableManager.findByUuid(contact.getObjectID(), customTableRpc.getUuid());

                        if (oldValuesPlacement != null && oldValuesPlacement.size() > 0) {
                            for (EdsCandidateItemTable itemTable : oldValuesPlacement) {
                                candidateItemTableManager.delete(itemTable);
                            }
                        }
                    }
                }

                for (CustomTableRpc rpc : values) {
                    EdsCandidateItemTable customItemTable = new EdsCandidateItemTable();
                    customItemTable.setUuid(map.getKey());
                    customItemTable.setName(rpc.getItemName());
                    customItemTable.setDescription(rpc.getDescription());
                    customItemTable.setCustomFields(saveCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()));
                    customItemTable.setCandidate(contact);
                    if (saveCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()) != null) {
                        candidateItemTableManager.createOrUpdate(customItemTable);
                    }
                }
            }
            if (contact.isNew()) {
                if (contact.getAuditInfo() == null) {
                    EdsAuditInfo auditInfo = new EdsAuditInfo();
                    auditInfo.setSuperUser(ServerUtils.isSuperUser());
                    contact.setAuditInfo(auditInfo);
                }
                contact.getAuditInfo().setCreationDate(item.getCreatedDate());
            }
            if (!contact.isNew() || contact.getLeadWebForm() == null) {
                contact.setLeadSource(item.getCandidateSource() != null && item.getCandidateSource().getId() != null ? referenceManager.get(item.getCandidateSource().getId()) : null);
            }
            if ((newCreated && item.getCandidateStatus() != null)) {
                recruitmentServiceLocal.insertCandidateStatusHistory(contact, referenceManager.get(item.getCandidateStatus().getId()), null);
            } else if (item.getCandidateStatus() != null && contact.getLeadStatus() != null && contact.getLeadStatus().getCode() != null && !contact.getLeadStatus().getCode().equals(item.getCandidateStatus().getCode())) {
                recruitmentServiceLocal.insertCandidateStatusHistory(contact, referenceManager.get(item.getCandidateStatus().getId()), item.getCandidateStatus().getCategory());
            }
            contact.setLeadStatus(item.getCandidateStatus() != null && item.getCandidateStatus().getId() != null ? referenceManager.get(item.getCandidateStatus().getId()) : null);
            String candidateStatusCode = contact.getLeadStatus() != null ? contact.getLeadStatus().getCode() : null;
            if (EdsCrmContact.CANDIDATE_STATUS_SHORTLIST.equals(candidateStatusCode)) {
                contact.setShortList(true);
            } else {
                if (EdsCrmContact.CANDIDATE_STATUS_HIRED.equals(candidateStatusCode) || EdsCrmContact.CANDIDATE_STATUS_REJECTED.equals(candidateStatusCode) || EdsCrmContact.CANDIDATE_STATUS_UNQUALIFIED.equals(candidateStatusCode)) {
                    contact.setShortList(false);
                }
            }


            if (item.getDepartmentItem() != null) {
                contact.setCandidateDepartment(departmentManager.get(item.getDepartmentItem().getId()));
            }
            if (item.getPositionItem() != null) {
                contact.setCandidatePosition(positionManager.get(item.getPositionItem().getId()));
            }

            if (item.getTimeSlotItem() != null) {
                contact.setTimeSlot(timeSlotManager.get(item.getTimeSlotItem().getId()));
            }


            contact.setWorkExperience(item.getWorkExperience());
            contact.setWorkExperienceMonthOrYear(item.getWorkExperienceMonthOrYear());
            contact.setCurrentEmployer(item.getCurrentEmployer());
            contact.setExpectedSalary(item.getExpectedSalary());
            contact.setStartSalary(item.getStartSalary());
            contact.setPassportNumber(item.getPassportNumber());
            contact.setSkills(item.getSkills());
            contact.setPrefferedLocation(item.getPreferredLocation() != null && item.getPreferredLocation().getId() != null ? locationManager.get(item.getPreferredLocation().getId()) : null);
            //save candidate vacancies
            saveCandidateVacancies(item.getVacancies(), contact);
        } else {
            if (item.isLeadContact()) {
                if (item.getLeadAssigneeID() != null) {
                    contact.setLeadAssignee(employeeManager.get(item.getLeadAssigneeID()));
                }
                if (item.getLeadBackupAssigneeID() != null && !item.getLeadBackupAssigneeID().equals(item.getLeadAssigneeID())) {
                    contact.setLeadBackupAssignee(employeeManager.get(item.getLeadBackupAssigneeID()));
                }
                if (item.getLeadSourceID() != null) {
                    contact.setLeadSource(referenceManager.get(item.getLeadSourceID()));
                } else {
                    if (item.getWebFormID() != null) {
                        EdsWebForm webForm = webFormManager.get(item.getWebFormID());
                        if (webForm != null && webForm.getWebFormSource() != null) {
                            contact.setLeadSource(webForm.getWebFormSource());
                        }
                    }
                }
            } else {
                contact.setCategories(null);
                if (item.getSelectedCategories() != null) {
                    for (SelectItem selectedCategory : item.getSelectedCategories()) {
                        if (selectedCategory != null && selectedCategory.getId() != null) {
                            contact.addCategories(contactCategoryManager.get(selectedCategory.getId()));
                        }
                    }
                }
                if (item.getCrmAccount() != null
                        && item.getCrmAccount().getAccountTypes() != null) {
                    item.getCrmAccount().getAccountTypes();
                    for (SelectItem r : item.getCrmAccount().getAccountTypes()) {
                        if (r.isSelected()) {
                            EdsContactCategory category;
                            if (EdsCrmAccount.CUSTOMER.equals(r.getReferenceCode())) {
                                category = contactCategoryManager.getDefaultCategoryByContactType(EdsCrmContact.CLIENT_CONTACT);
                                contact.addCategories(category);
                            } else if (EdsCrmAccount.SUPPLIER.equals(r.getReferenceCode())) {
                                category = contactCategoryManager.getDefaultCategoryByContactType(EdsCrmContact.SUPPLIER_CONTACT);
                                contact.addCategories(category);
                            }
                        }
                    }
                }
            }
            contact.setOtherLeadSource(item.getOtherLeadSource());
            if (item.getLeadStatus(true).getId() != null) {
                contact.setLeadStatus(referenceManager.get(item.getLeadStatus(true).getId()));
            }
            if (item.getLeadRatingID() != null) {
                contact.setLeadRating(referenceManager.get(item.getLeadRatingID()));
            }
            if (item.getObjectId() != null) {
                if (!item.isEmailOptOut()) {
                    contact.setBouncedcount(0);
                }
            }

            if (item instanceof ProfileItem) {
                isHrms = WORKFORCETRACK.equals(((ProfileItem) item).getFrom());
            }
            if (item.getTrackerIDSet() != null && !isHrms) {
                Set<Integer> trackerIdSet = item.getTrackerIDSet();
                Set<EdsEmailTracker> emailTrackers = new HashSet<>();
                for (Integer id : trackerIdSet) {
                    emailTrackers.add(emailTrackerManager.get(id));
                }
                contact.setTrackerIdSet(emailTrackers);
            }

        }
        if (contact.getCategories().isEmpty()) {
            contact.addCategories(contactCategoryManager.getDefaultCategoryByContactType(contact.getContactType()));
        }
        if (contact.getEntityID() == null) {
            createEntity(contact);
        }
        if (contactDetails.isNew()) {
            crmContactManager.persist(contactDetails);
        }
        //set kanbanboard order if its null
        if (contact.getKanbanorder() == null) {
            Long minKanbanOrderInStatus = crmContactManager.getMinKanbanOrder(CrmConstants.TYPE_LEAD_CONTACT, contact.getLeadStatus() != null ? contact.getLeadStatus().getObjectID() : null);
            if (minKanbanOrderInStatus == null) {
                minKanbanOrderInStatus = KANBAN_ORDER_GAP;
                contact.setKanbanorder(minKanbanOrderInStatus);
            } else {
                contact.setKanbanorder(minKanbanOrderInStatus - KANBAN_ORDER_GAP);
            }

        }

        item.setCreatedDate(contact.getCreationDate());
        item.setUpdatedDate(contact.getModificationDate());
        item.setObjectId(contact.getObjectID());
        ArrayList<Integer> newList = new ArrayList<>();
        //ArrayList<EdsSpokenLanguages> languages = spokenLanguagesManager.getListByRelation(contact.getObjectID(), EdsSpokenLanguages.TYPE_CANDIDATE);
        //Create or update existing languages
        if (item.getSpokingLanguages() != null && item.getSpokingLanguages().size() > 0) {
            for (SpokenLanguageItem languageItem : item.getSpokingLanguages()) {
                if (languageItem.getLanguage() != null && languageItem.getLanguage().getId() != null && languageItem.getLevel() != null && languageItem.getLevel().getId() != null) {
                    EdsSpokenLanguages language = spokenLanguagesManager.getByRelation(contact.getObjectID(), EdsSpokenLanguages.TYPE_CANDIDATE, languageItem.getLanguage().getId());
                    if (language == null) {
                        language = new EdsSpokenLanguages();
                        language.setEntityType(EdsSpokenLanguages.TYPE_CANDIDATE);
                        language.setEntityId(contact.getObjectID());
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
            spokenLanguagesManager.removedLanguages(contact.getObjectID(), EdsSpokenLanguages.TYPE_CANDIDATE, newList);
        }
        if (item.getContactType() != null) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
            kpiLog.setEntityId(contact.getObjectID());
            if (item.getContactType() == CrmConstants.TYPE_LEAD_CONTACT) {
                kpiLog.setEntityType(CrmConstants.CRM_LEAD);
                if (newCreated) {
                    kpiLog.setActionType(KpiLog.ActionType.ADD);
                    ServerUtils.kpiLog(log, kpiLog, "Added new lead");
                } else {
                    kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                    ServerUtils.kpiLog(log, kpiLog, "Updated lead");
                }
            } else if (item.getContactType() == CrmConstants.TYPE_CRM_CONTACT) {
                kpiLog.setEntityType(CrmConstants.CRM_CONTACT);
                if (newCreated) {
                    kpiLog.setActionType(KpiLog.ActionType.ADD);
                    ServerUtils.kpiLog(log, kpiLog, "Created new contact");
                } else {
                    kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                    ServerUtils.kpiLog(log, kpiLog, "Updated contact");
                }
            } else if (item.getContactType() == CrmConstants.TYPE_CANDIDATE) {
                kpiLog.setEntityType(CrmConstants.CANDIDATE);
                if (newCreated) {
                    kpiLog.setActionType(KpiLog.ActionType.ADD);
                    ServerUtils.kpiLog(log, kpiLog, "Created new candidate");
                } else {
                    kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                    ServerUtils.kpiLog(log, kpiLog, "Updated candidate");
                }
            }
        }
        crmServiceLocal.updateAddresses(item.getAddresses().toArray(new Address[]{}), contact, null, false, isHrms);

        EdsPaymentDeduction newPaymentDeduction;
        if (item.isCandidate() && item.getAllowanceCategories().size() > 0) {
            for (PaymentDeductionObject paymentOrDeductionItem : item.getAllowanceCategories()) {
                if (paymentOrDeductionItem.getId() != null) {
                    newPaymentDeduction = paymentDeductionManager.get(paymentOrDeductionItem.getId());
                } else {
                    newPaymentDeduction = new EdsPaymentDeduction();
                }
                newPaymentDeduction.setCategoryId(paymentOrDeductionItem.getCategoryItem() != null ? paymentOrDeductionItem.getCategoryItem().getId() : null);
                newPaymentDeduction.setCandidate(contact);
                newPaymentDeduction.setPaymentAmount(paymentOrDeductionItem.getPaymentAmount());
                newPaymentDeduction.setPaymentDate(paymentOrDeductionItem.getPaymentDate());
                newPaymentDeduction.setPayType(paymentOrDeductionItem.getType());
                newPaymentDeduction.setRecurring(true);
                paymentDeductionManager.createOrUpdate(newPaymentDeduction);
            }
        }
        if (ContactListItem.REQUEST_FROM_CONTACT_SYNC.equals(requestedFrom) && contact.getObjectID() != null) {
            List<EdsCrmContactItemParams> relations = contactItemParamsManager.getContactParams(contact.getObjectID(), 6);
            ArrayList<SelectItem> relationsList = new ArrayList<>();
            for (EdsCrmContactItemParams param : relations) {
                SelectItem rel = new SelectItem();
                rel.setId(param.getRelation());
                rel.setDescription(param.getValue());
                rel.setSelected(true);
                relationsList.add(rel);
            }
            item.setSelectedRelationships(relationsList);
        }
        HashMap<Integer, HashMap<Integer, ArrayList<String>>> itemParams = ContactListItem.getAllItemParamsAsMap(item);
        // we must delete existing items to save new ones

        if (contact.getObjectID() != null) {
            contactItemParamsManager.deleteAllContactItemParams(contact.getObjectID());
        }
        if (!item.isFromAPI()) {
            createItemParams(contact, itemParams, false);
        }
        String accountChangedHistory = null;
        if (!item.getCrmAccount().isNew() || item.getCrmAccount().getName() != null) {
            if (contact.getObjectID() != null && contact.getCrmAccount() != null && !contact.getCrmAccount().getObjectID().equals(item.getCrmAccount().getObjectId())) {
                accountChangedHistory = "Account/Company changed from \"" + contact.getCrmAccount().getName() + "\"";
                ContactCareerItem careerItem = new ContactCareerItem();
                careerItem.setContactID(contact.getObjectID());
                careerItem.setCompanyName(contact.getCrmAccount().getName());
                careerItem.setJobTitle(contact.getJobTitles());
                careerItem.setIndustryID(contact.getCrmAccount().getIndustry() != null ? contact.getCrmAccount().getIndustry().getObjectID() : null);
                careerItem.setToYear(new Date());
                careerItem.setCurrentYear(false);
                if (contact.getCrmAccount().getBillingAddress() != null) {
                    if (contact.getCrmAccount().getBillingAddress().getCountry() != null) {
                        careerItem.setCountryID(contact.getCrmAccount().getBillingAddress().getCountry().getObjectID());
                    }
                    careerItem.setCity(contact.getCrmAccount().getBillingAddress().getCity());
                }
                saveContactCareer(careerItem);
            }
            contact.setCrmAccount(null);
            if (savingFromCrmAccount) {
                if (!item.getCrmAccount().isNew()) {        //+++++++++++++++++++++++shu erda breakpoint bor edi
                    contact.setCrmAccount(crmAccountManager.get(item.getCrmAccount().getObjectId()));
                }
            } else {
                //CREATE CRMACCOUNT
                isNewAccount = createOrSetCrmAccountToContact(contact, item.getCrmAccount(), false);

                if (isNewAccount && contact.getAddresses() != null && contact.getAddresses().size() > 0) {
                    if (contact.getAddresses().get(0) != null) {
                        EdsAddress a = contact.getAddresses().get(0);
                        Address address = new Address();
                        address.setAddress(a.getAddress());
                        address.setAddressb(a.getAddressb());
                        address.setCity(a.getCity());
                        address.setCountryId(a.getCountry() != null ? a.getCountry().getObjectID() : null);
                        address.setStateId(a.getState() != null ? a.getState().getObjectID() : null);
                        address.setZipCode(a.getZipCode());
                        boolean t = false;
                        if ("".equals(address.getName()) || address.getName() == null) {
                            address.setName("Billing Address");
                            t = true;
                        }
                        crmServiceLocal.updateAddresses(new Address[]{address}, contact.getCrmAccount(), EdsAddress.BILLING_ADDRESS, false);
                        if (t) {
                            address.setName("Shipping Address");
                        }
                        crmServiceLocal.updateAddresses(new Address[]{address}, contact.getCrmAccount(), EdsAddress.MAILING_ADDRESS, false);
                        crmAccountManager.update(contact.getCrmAccount(), true);
                    }
                }
            }
            if (contact.getCrmAccount() != null && accountChangedHistory != null) {
                accountChangedHistory += " to \"" + contact.getCrmAccount().getName() + "\"";
            }
        }

        saveLeadItems(contact, item.getItems());

        if (!ContactListItem.REQUEST_FROM_CONTACT_SYNC.equals(requestedFrom)) {
            if (item.getNote() != null && !"".equals(item.getNote())) {
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setContactID(contact.getObjectID());
                fp.setRelationID(contact.getObjectID());
                fp.setRelationType(ContactListItem.getRelationTypeByContactType(contact.getContactType()));
                crmService.saveCrmNote(fp, item.getNote());
            }
            if (item.isAddingFromWebForms()) {
                ArrayList<CompanyCustomFieldItem> customFields = item.getCustomFields();
                ArrayList<CompanyCustomFieldItem> newCustomFields = new ArrayList<>();
                if (customFields != null && customFields.size() > 0) {
                    for (CompanyCustomFieldItem customFieldItem : customFields) {
                        EdsCompanyCustomFieldsSettings edsCompanyCustomFieldSetting = companyCFSettingsManager.get(customFieldItem.getObjectId());
                        if (edsCompanyCustomFieldSetting != null) {
                            CompanyCustomFieldItem newCustomFieldItem = edsCompanyCustomFieldSetting.getRPC(null);
                            newCustomFieldItem.setObjectId(null);
                            newCustomFieldItem.setFieldStringValue(customFieldItem.getFieldStringValue());
                            newCustomFieldItem.setFieldDateNonConvertedValue(customFieldItem.getFieldDateNonConvertedValue());
                            newCustomFields.add(newCustomFieldItem);
                        }
                    }
                    item.setCustomFields(newCustomFields);
                } else {
                    item.setCustomFields(null);
                }
            }
            if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
                StringBuilder changesBuilder = new StringBuilder();
                for (CompanyCustomFieldItem cit : item.getCustomFields()) {
                    changesBuilder.append(contact.getCustomFields() != null && CustomFieldsUtils.getObjectValue(contact.getCustomFields(), cit.getColumnCode()) != null ? getChanges(CustomFieldsUtils.getObjectValue(contact.getCustomFields(), cit.getColumnCode()), cit) : (cit.getColumnCode() + ","));
                }
                String changes = changesBuilder.toString();
                if (!"".equals(changes)) {
                    contact.addCustomFieldChanges(changes);
                }
            }
            //SAVE CUSTOM FIELDS
            contact.setCustomFields(saveCustomFields(contact.getCustomFields(), item.getCustomFields()));

            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setContactID(contact.getObjectID());
            if (mailingList != null) {// if you want delete saved mailing list @{mailingList} should be empty array
                massMailService.updateCrmEntityMailLists(fp, mailingList);
            }

            if (item.getAttachments() != null && item.getAttachments().length > 0 && !isHrms) {
                saveContactAttachments(item.getAttachments(), contact);
            }
            if (newCreated) {
                crmServiceLocal.createContactHistory("Created the " + (item.isLeadContact() ? "lead" : "contact"), contact.getObjectID(), user.getObjectID());
            } else {
                if (!item.isEmailOptOut()) {
                    EdsCrmContact edsCrmContact = crmContactManager.get(item.getObjectId());
                    if (edsCrmContact != null) {
                        edsCrmContact.setBouncedcount(0);
                    }
                }
                crmServiceLocal.createContactHistory(accountChangedHistory != null ? accountChangedHistory : "Updated the " + (item.isLeadContact() ? "lead" : "contact"), contact);
            }
            crmContactManager.update(contact);
            if (contact.getObjectID() != null && contact.getEntityContactID() != null) {
                EdsUser clientContact = clientContactManager.getClientByCrmContactAndByUserId(contact.getObjectID(), contact.getEntityContactID());
                if (clientContact != null) {
                    clientContact.setFirstName(item.getFirstName());
                    clientContact.setLastName(item.getLastName());
                    userManager.update(clientContact);
                }
            }
            if (contact.is(EdsCrmContact.EMPLOYEE_CONTACT)) {
                changeEmployeeDetails(contact, employeeChanged);
            } else if (contact.is(EdsCrmContact.STUDENT_CONTACT)) {
                changeStudentDetails(contact);
            }
            updateEdsCrmContactAndIndex(contact, newCreated, user);
        } else {
            crmContactManager.update(contact);
        }
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, newCreated ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : (contact.isDeleted() != null && contact.isDeleted() ? BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT), contact, user);
        workflowEvent.setEntityType(contact.is(EdsCrmContact.LEAD_CONTACT) ? RelationItem.TYPE_LEAD : (contact.is(EdsCrmContact.CANDIDATE) ? RelationItem.TYPE_CANDIDATE : RelationItem.TYPE_CONTACT));
        if (contact.is(EdsCrmContact.LEAD_CONTACT) || contact.is(EdsCrmContact.CRM_CONTACT)) {
            baseEventPostProcessor.registerEvent(CrmLeadEventListenerImpl.TYPE, newCreated ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, contact, user);
        }
        crmServiceLocal.saveCrmNotes(CrmConstants.CRM_CONTACT, contact.getObjectID(), item.getNotes());

        //Rest Hooks for Zapier Integration

        if (runWebhook) {
            try {
                List<EdsRestHook> webhooks = restHookManager.getByEventName(newCreated ? "contact.create" : "contact.update");
                if (!webhooks.isEmpty()) {
                    for (EdsRestHook webhook : webhooks) {
                        try {
                            if (!"https://hooks.zapier.com/fake-subscription-url".equalsIgnoreCase(webhook.getTargetUrl())) {
                                log.info("Triggering webhook {}: {}", webhook.getEventName(), webhook.getTargetUrl());

                                HttpHeaders httpHeaders = new HttpHeaders();
                                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                                HttpEntity<AddZapierContactTO> httpRequest = new HttpEntity<>(convertToContact(contact), httpHeaders);

                                String resp = restTemplate.postForObject(webhook.getTargetUrl(), httpRequest, String.class);
                                log.info("ZAPIER WEBHOOK: {}", resp);
                            }
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                }
            } catch (Exception e1) {
                log.error("", e1);
            }
        }

        for (Map.Entry<String, ArrayList<CustomTableRpc>> map : item.getCustomTableItems().entrySet()) {
            List<CustomTableRpc> values = map.getValue();

            for (CustomTableRpc rpc : values) {
                EdsCrmContactCustomItemTable customItemTable = new EdsCrmContactCustomItemTable();
                customItemTable.setUuid(map.getKey());
                customItemTable.setName(rpc.getItemName());
                customItemTable.setDescription(rpc.getDescription());
                customItemTable.setCustomFields(saveCustomTableFields(customItemTable.getCustomFields(), rpc.getItemCustomFields()));
                customItemTable.setCrmContact(contact);
                crmContactItemTableManager.createOrUpdate(customItemTable);

            }
        }

        List<EdsOpportunity> opportunities = opportunityManager.getOpportunityByCrmContactID(contact.getObjectID());
        if (opportunities != null && opportunities.size() > 0) {
            for (EdsOpportunity opportunity : opportunities) {
                try {
                    solrManager.addOpportunityToIndex(opportunity);
                } catch (SolrServerException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (item.isFromOpportunityQuickAdd() && item.getOpportunity() != null && item.getOpportunity().getObjectId() != null) {
            EdsOpportunity opportunity = opportunityManager.get(item.getOpportunity().getObjectId());
            if (contact != null) {
                opportunity.setCrmContact(contact);
                if (contact.getEntityID() != null) {
                    opportunity.setEntityID(contact.getEntityID());
                }
            }
            if (contact != null && contact.getCrmAccount() != null) {
                opportunity.setCrmAccount(contact.getCrmAccount());
                if (contact.getCrmAccount().getEntityID() != null) {
                    opportunity.setEntityID(contact.getCrmAccount().getEntityID());
                }
            }

        }
        return contact.getObjectID();
    }

    private void saveLeadItems(EdsCrmContact crmContact, OpportunityItem[] items) {
        crmContactManager.deleteItems(crmContact.getObjectID());

        if (items != null) {
            for (OpportunityItem item : items) {
                EdsCrmContactItem crmContactItem = new EdsCrmContactItem();
                crmContactItem.setCrmContact(crmContact);
                if (item.getItemID() != null) {
                    crmContactItem.setItem(itemManager.get(item.getItemID()));
                }

                crmContactItem.setItemName(item.getItemName());
                crmContactItem.setDescription(item.getDescription());
                crmContactItem.setQty(item.getQty());
                crmContactItem.setPrice(item.getPrice());
                if (item.getUnitMeasurement() != null && item.getUnitMeasurement().getId() != null) {
                    crmContactItem.setUnitMeasurement(unitMeasurementManager.get(item.getUnitMeasurement().getId()));
                }
                crmContactItem.setSupplierID(item.getSupplierID());
                if (item.getSupplierName() != null) {
                    crmContactItem.setSupplierName(item.getSupplierName());
                }

                if (item.getProductCategory() != null) {
                    crmContactItem.setCategory(productCategoryManager.get(item.getProductCategory().getId()));
                }
                if (item.getProductBrand() != null) {
                    crmContactItem.setBrand(brandManager.get(item.getProductBrand().getId()));
                }
                if (item.getTaxItem() != null && item.getTaxItem().getId() != null) {
                    crmContactItem.setVat(vatManager.get(item.getTaxItem().getId()));
                }
                crmContactItem.setTaxAmount(item.getTaxAmount());
                if (item.getNet() == null && item.getSubTotal() == null && item.getQty() != null &&
                        item.getQty().compareTo(BigDecimal.ZERO) != 0 && item.getPrice() != null && item.getPrice().compareTo(BigDecimal.ZERO) != 0) {
                    crmContactItem.setNet(item.getQty().multiply(item.getPrice()).setScale(8, RoundingMode.HALF_UP));
                    crmContactItem.setSubTotal(item.getQty().multiply(item.getPrice()).setScale(8, RoundingMode.HALF_UP));
                } else {
                    crmContactItem.setNet(item.getNet());
                    crmContactItem.setSubTotal(item.getSubTotal());
                }
                if (crmContactItem.getItem() != null && productService.getProductBaseData(crmContactItem.getItem().getObjectID()) != null) {
                    ArrayList<CompanyCustomFieldItem> productCustomFields = productService.getProductBaseData(crmContactItem.getItem().getObjectID(), true).getProductCustomFieldItems();

                    if (productCustomFields != null && productCustomFields.size() > 0) {
                        setValueStaticFieldFromCFByAliasName(crmContactItem, productCustomFields);


                        ArrayList<CompanyCustomFieldItem> leadItemCustomFields = item.getItemCustomFields();

                        ArrayList<CompanyCustomFieldItem> leadAllItemCustomFields = commonService.getCompanyAllCustomFields(ViewName.Lead);
                        if (leadAllItemCustomFields != null && !leadAllItemCustomFields.isEmpty()) {
                            for (CompanyCustomFieldItem companyCustomFieldItem : leadAllItemCustomFields) {
                                if (leadItemCustomFields != null && !leadItemCustomFields.contains(companyCustomFieldItem)) {
                                    leadItemCustomFields.add(companyCustomFieldItem);
                                }
                            }
                        }

                        if (leadItemCustomFields != null && leadItemCustomFields.size() > 0) {
                            for (CompanyCustomFieldItem leadCF : leadItemCustomFields) {
                                for (CompanyCustomFieldItem productCF : productCustomFields) {
                                    if (leadCF.getDataType().equals(productCF.getDataType())
                                            && leadCF.getUiType().equals(productCF.getUiType())
                                            && leadCF.getAliasName().equals(productCF.getAliasName())
                                            && (leadCF.getFieldStringValue() == null || (leadCF.getFieldStringValue() != null && leadCF.getFieldStringValue().length() == 0))) {
                                        leadCF.setPredefinedValues(productCF.getPredefinedValues());
                                        leadCF.setPredefinedValuesWithSorting(productCF.getPredefinedValuesWithSorting());
                                        leadCF.setQuery(productCF.getQuery());
                                        leadCF.setQueryItems(productCF.getQueryItems());
                                        leadCF.setFieldStringValue(productCF.getFieldStringValue());
                                        leadCF.setFieldDateNonConvertedValue(productCF.getFieldDateNonConvertedValue());
                                        leadCF.setAttachments(productCF.getAttachments());
                                        leadCF.setLookUpTypeEnum(productCF.getLookUpTypeEnum());
                                        leadCF.setSelectedId(productCF.getSelectedId());
                                        leadCF.setDefaultValue(productCF.getDefaultValue());
                                        leadCF.setPrefix(productCF.getPrefix());
                                        leadCF.setItem(productCF.getItem());
                                        leadCF.setSelectItems(productCF.getSelectItems());
                                    }
                                }
                            }
                        }
                    }
                }

                crmContactItem.setCustomFields(saveItemCustomFields(crmContactItem.getCustomFields(), item.getItemCustomFields()));
                crmContact.getCrmContactItems().add(crmContactItem);
            }
        }
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

    private void setValueStaticFieldFromCFByAliasName(EdsCrmContactItem crmContactItem, ArrayList<CompanyCustomFieldItem> productCustomFieldItems) {
        for (CompanyCustomFieldItem productCFItem : productCustomFieldItems) {
            if (productCFItem != null && productCFItem.getAliasName() != null) {
                switch (productCFItem.getAliasName()) {
                    case ItemTableConstants.DESCRIPTION -> {
                        if ((crmContactItem.getDescription() == null || crmContactItem.getDescription() != null && crmContactItem.getDescription().length() == 0) &&
                                productCFItem.getFieldStringValue() != null && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType()))) {
                            crmContactItem.setDescription(productCFItem.getFieldStringValue());
                        }
                    }
                    case ItemTableConstants.QTY -> {
                        if (crmContactItem.getQty() == null &&
                                productCFItem.getFieldStringValue() != null && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            crmContactItem.setQty(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.MEASUREMENT -> {
                        if (crmContactItem.getUnitMeasurement() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.UNIT_MEASUREMENT.equals(productCFItem.getLookUpTypeEnum())) {
                            crmContactItem.setUnitMeasurement(unitMeasurementManager.get(productCFItem.getSelectedId()));
                        }
                    }
                    case ItemTableConstants.UNITPRICE -> {
                        if (crmContactItem.getPrice() == null &&
                                productCFItem.getFieldStringValue() != null && (UI_TYPE_TEXTAREA.equals(productCFItem.getUiType()) || UI_TYPE_TEXTBOX.equals(productCFItem.getUiType())) && DATA_TYPE_NUMBER.equals(productCFItem.getDataType())) {
                            crmContactItem.setPrice(new BigDecimal(productCFItem.getFieldStringValue()));
                        }
                    }
                    case ItemTableConstants.SUPPLIER -> {
                        if (crmContactItem.getSupplierID() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.SUPPLIER.equals(productCFItem.getLookUpTypeEnum())) {
                            crmContactItem.setSupplierID(productCFItem.getSelectedId());
                            crmContactItem.setSupplierName(productCFItem.getFieldStringValue());
                        }
                    }
                    case ItemTableConstants.CATEGORY -> {
                        if (crmContactItem.getCategory() == null &&
                                productCFItem.getSelectedId() != null && UI_TYPE_LOOKUP.equals(productCFItem.getUiType()) && CustomFieldLookUpTypeEnum.PRODUCT_CATEGORY.equals(productCFItem.getLookUpTypeEnum())) {
                            crmContactItem.setCategory(productCategoryManager.get(productCFItem.getSelectedId()));
                        }
                    }
                }
            }
        }
    }

    public EdsCrmContactItemTableCF saveCustomTableFields(EdsCrmContactItemTableCF customfField, List<CompanyCustomFieldItem> customFieldItems) {
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
                customfField = new EdsCrmContactItemTableCF();
                crmContactItemCFManager.create(customfField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customfField, customFieldItems);
            return customfField;
        }
        return null;
    }

    @Transactional
    public Integer saveOffice365Contact(ContactListItem item, EdsUser user) {
        boolean isNewAccount;
        String requestedFrom = item.getCreatedFrom();

        if (user == null) {
            user = crmContactManager.getUser();
        }
        if (user == null && item.getOwnerId() != null) {
            user = userManager.get(item.getOwnerId());
        }
        EdsCrmContact contact = null;
        if (item.getObjectId() != null) {
            contact = crmContactManager.get(item.getObjectId());
        } else {
            if (item.getEntityID() != null) {
                contact = EdsCrmContact.getPreferedItem(crmContactManager.getContactByEntityID(item.getEntityID()), item.getPrimaryEmail());
            }
        }
        if (contact == null) {
            contact = new EdsCrmContact();
            contact.setCreator(user);
            if (ContactListItem.REQUEST_FROM_CONTACT_SYNC.equals(requestedFrom)) {
                EdsContactCategory privateCategory = contactCategoryManager.getPrivateCategory(ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_CONTACT_LIST)).get(0);
                contact.addCategories(privateCategory);
            }
        }
        contact.setVersion(contact.getVersion() + 1);
        contact.clear();
        EdsCrmContactDetails contactDetails = null;
        if (contact.getCrmContactDetails() == null) {
            contactDetails = new EdsCrmContactDetails();
            contact.setCrmContactDetails(contactDetails);
        } else {
            contactDetails = contact.getCrmContactDetails();
        }

        contact.setOwner(item.getOwnerId() != null ? userManager.get(item.getOwnerId()) : user);
        contact.setContactType(item.getContactType());
        if (item.getSyncID() > 0) {
            contact.setSyncID(item.getSyncID());
        }

        //Personal Information
        contact.setFirstName(item.getFirstName());
        /*if (item.isPrimaryContact() && !contact.getPrimaryContact() && (contact.getCrmAccount() != null || item.getCrmAccount().getObjectId() != null)) {
            unCheckExistingPrimaryContact(contact.getCrmAccount() != null ? contact.getCrmAccount().getObjectID() : item.getCrmAccount().getObjectId());
        }*/
        if (!item.isPrimaryContact() && item.getCrmAccount() != null) {
            boolean isPrimary = item.getCrmAccount().getObjectId() == null || !crmContactManager.hasContactsByCrmAccount(item.getCrmAccount().getObjectId());
            contact.setPrimaryContact(isPrimary);
        } else {
            contact.setPrimaryContact(item.isPrimaryContact());
        }
        contact.setLastName(item.getLastName());
        contact.setMiddleName(item.getMiddleName());
        contact.setOtherName(item.getOtherName());
        //@TODO do we need this
        contact.setDateOfBirth(item.getBirthDate() != null ? item.getBirthDate().getNonConvertedDate() : null);
        contact.setGoogleId(StringUtils.isNotBlank(item.getGoogleId()) ? item.getGoogleId() : null);
        contact.setDepartment(StringUtils.isNotBlank(item.getDepartment()) ? item.getDepartment() : null);

        if (item.getObjectId() == null && item.getContactType() == null) {
            item.setContactType(ContactListItem.CRM_CONTACT);
        }

        if (item.getTitleId() != null) {
            EdsReference reference = referenceManager.get(item.getTitleId());
            if (reference != null) {
                contact.setTitleRef(reference);
                if (!"other".equalsIgnoreCase(reference.getName())) {
                    contact.setTitle(reference.getName());
                } else {
                    contact.setTitle(item.getTitle());
                }
            } else {
                contact.setTitle(item.getTitle());
            }
        } else {
            contact.setTitleRef(null);
            contact.setTitle(StringUtils.isNotBlank(item.getTitle()) ? item.getTitle() : "");
        }
        if (StringUtils.isNotBlank(item.getJobTitle())) {
            contact.setJobTitles(item.getJobTitle());
        }

        contact.setPrimaryPhone(StringUtils.isNotBlank(item.getPrimaryPhone()) ? item.getPrimaryPhone() : null);
        contact.setPrimaryEmail(StringUtils.isNotBlank(item.getPrimaryEmail()) ? item.getPrimaryEmail() : null);

        contact.setCategories(null);
        if (item.getSelectedCategories() != null) {
            for (SelectItem selectedCategory : item.getSelectedCategories()) {
                if (selectedCategory != null && selectedCategory.getId() != null) {
                    contact.addCategories(contactCategoryManager.get(selectedCategory.getId()));
                }
            }
        }
        //@TODO do we need this since its false
        if (item.getCrmAccount() != null
                && item.getCrmAccount().getAccountTypes() != null) {
            item.getCrmAccount().getAccountTypes();
            for (SelectItem r : item.getCrmAccount().getAccountTypes()) {
                if (r.isSelected()) {
                    EdsContactCategory category;
                    if (EdsCrmAccount.CUSTOMER.equals(r.getReferenceCode())) {
                        category = contactCategoryManager.getDefaultCategoryByContactType(EdsCrmContact.CLIENT_CONTACT);
                        contact.addCategories(category);
                    } else if (EdsCrmAccount.SUPPLIER.equals(r.getReferenceCode())) {
                        category = contactCategoryManager.getDefaultCategoryByContactType(EdsCrmContact.SUPPLIER_CONTACT);
                        contact.addCategories(category);
                    }
                }
            }
        }

        if (item.getObjectId() != null) {
            if (!item.isEmailOptOut()) {
                contact.setBouncedcount(0);
            }
        }
        //SET DEFAULT CATEGORY IF CATEGORY IS EMPTY
        if (contact.getCategories().isEmpty()) {
            contact.addCategories(contactCategoryManager.getDefaultCategoryByContactType(contact.getContactType()));
        }
        if (contact.getEntityID() == null) {
            createEntity(contact);
        }
        if (contactDetails.isNew()) {
            crmContactManager.persist(contactDetails);
        }

        boolean newCreated = crmContactManager.createOrUpdate(contact);

        if (item.getContactType() != null) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
            kpiLog.setEntityId(contact.getObjectID());
            if (item.getContactType() == CrmConstants.TYPE_LEAD_CONTACT) {
                kpiLog.setEntityType(CrmConstants.CRM_LEAD);
                if (newCreated) {
                    kpiLog.setActionType(KpiLog.ActionType.ADD);
                    ServerUtils.kpiLog(log, kpiLog, "Added new lead");
                } else {
                    kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                    ServerUtils.kpiLog(log, kpiLog, "Updated lead");
                }
            } else if (item.getContactType() == CrmConstants.TYPE_CRM_CONTACT) {
                kpiLog.setEntityType(CrmConstants.CRM_CONTACT);
                if (newCreated) {
                    kpiLog.setActionType(KpiLog.ActionType.ADD);
                    ServerUtils.kpiLog(log, kpiLog, "Created new contact");
                } else {
                    kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                    ServerUtils.kpiLog(log, kpiLog, "Updated contact");
                }
            } else if (item.getContactType() == CrmConstants.TYPE_CANDIDATE) {
                kpiLog.setEntityType(CrmConstants.CANDIDATE);
                if (newCreated) {
                    kpiLog.setActionType(KpiLog.ActionType.ADD);
                    ServerUtils.kpiLog(log, kpiLog, "Created new candidate");
                } else {
                    kpiLog.setActionType(KpiLog.ActionType.UPDATE);
                    ServerUtils.kpiLog(log, kpiLog, "Updated candidate");
                }
            }
        }
        crmServiceLocal.updateAddresses(item.getAddresses().toArray(new Address[]{}), contact, null, false, false);

        if (ContactListItem.REQUEST_FROM_CONTACT_SYNC.equals(requestedFrom) && contact.getObjectID() != null) {
            List<EdsCrmContactItemParams> relations = contactItemParamsManager.getContactParams(contact.getObjectID(), 6);
            ArrayList<SelectItem> relationsList = new ArrayList<>();
            for (EdsCrmContactItemParams param : relations) {
                SelectItem rel = new SelectItem();
                rel.setId(param.getRelation());
                rel.setDescription(param.getValue());
                rel.setSelected(true);
                relationsList.add(rel);
            }
            item.setSelectedRelationships(relationsList);
        }
        //if (!(item.getSyncID() > 0)) {
        HashMap<Integer, HashMap<Integer, ArrayList<String>>> itemParams = ContactListItem.getAllItemParamsAsMap(item);
        // we must delete existing items to save new ones

        if (contact.getObjectID() != null) {
            contactItemParamsManager.deleteAllContactItemParams(contact.getObjectID());
        }
        if (!item.isFromAPI()) {
            createItemParams(contact, itemParams, false);
        }
        String accountChangedHistory = null;
        if (!item.getCrmAccount().isNew() || item.getCrmAccount().getName() != null) {
            if (contact.getObjectID() != null && contact.getCrmAccount() != null && !contact.getCrmAccount().getObjectID().equals(item.getCrmAccount().getObjectId())) {
                accountChangedHistory = "Account/Company changed from \"" + contact.getCrmAccount().getName() + "\"";
                ContactCareerItem careerItem = new ContactCareerItem();
                careerItem.setContactID(contact.getObjectID());
                careerItem.setCompanyName(contact.getCrmAccount().getName());
                careerItem.setJobTitle(contact.getJobTitles());
                careerItem.setIndustryID(contact.getCrmAccount().getIndustry() != null ? contact.getCrmAccount().getIndustry().getObjectID() : null);
                careerItem.setToYear(new Date());
                careerItem.setCurrentYear(false);
                if (contact.getCrmAccount().getBillingAddress() != null) {
                    if (contact.getCrmAccount().getBillingAddress().getCountry() != null) {
                        careerItem.setCountryID(contact.getCrmAccount().getBillingAddress().getCountry().getObjectID());
                    }
                    careerItem.setCity(contact.getCrmAccount().getBillingAddress().getCity());
                }
                saveContactCareer(careerItem);
            }
            contact.setCrmAccount(null);

            isNewAccount = createOrSetCrmAccountToContact(contact, item.getCrmAccount(), false);
            if (isNewAccount && contact.getAddresses() != null && !contact.getAddresses().isEmpty()) {
                if (contact.getAddresses().get(0) != null) {
                    EdsAddress a = contact.getAddresses().get(0);
                    Address address = new Address();
                    address.setAddress(a.getAddress());
                    address.setAddressb(a.getAddressb());
                    address.setCity(a.getCity());
                    address.setCountryId(a.getCountry() != null ? a.getCountry().getObjectID() : null);
                    address.setStateId(a.getState() != null ? a.getState().getObjectID() : null);
                    address.setZipCode(a.getZipCode());
                    boolean t = false;
                    if ("".equals(address.getName()) || address.getName() == null) {
                        address.setName("Billing Address");
                        t = true;
                    }
                    crmServiceLocal.updateAddresses(new Address[]{address}, contact.getCrmAccount(), EdsAddress.BILLING_ADDRESS, false);
                    if (t) {
                        address.setName("Shipping Address");
                    }
                    crmServiceLocal.updateAddresses(new Address[]{address}, contact.getCrmAccount(), EdsAddress.MAILING_ADDRESS, false);
                    crmAccountManager.update(contact.getCrmAccount(), true);
                }
            }

            if (contact.getCrmAccount() != null && accountChangedHistory != null) {
                accountChangedHistory += " to \"" + contact.getCrmAccount().getName() + "\"";
            }
        }

        crmContactManager.update(contact);

        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, newCreated ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : (contact.isDeleted() != null && contact.isDeleted() ? BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT), contact, user);
        workflowEvent.setEntityType(contact.is(EdsCrmContact.LEAD_CONTACT) ? RelationItem.TYPE_LEAD : (contact.is(EdsCrmContact.CANDIDATE) ? RelationItem.TYPE_CANDIDATE : RelationItem.TYPE_CONTACT));
        if (contact.is(EdsCrmContact.LEAD_CONTACT) || contact.is(EdsCrmContact.CRM_CONTACT)) {
            baseEventPostProcessor.registerEvent(CrmLeadEventListenerImpl.TYPE, newCreated ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, contact, user);
        }
//        crmServiceLocal.saveCrmNotes(CrmConstants.CRM_CONTACT, contact.getObjectID(), item.getNotes());
        return contact.getObjectID();
    }

    private String getChanges(Object ob, CompanyCustomFieldItem item) {
        if (ob != null) {
            if (DATA_TYPE_TEXT.equals(item.getDataType())) {
                String text = (String) ob;
                return !text.equals(item.getFieldStringValue()) ? (item.getColumnCode() + ",") : "";
            } else if (DATA_TYPE_NUMBER.equals(item.getDataType())) {
                String s = String.valueOf(((Double) ob).intValue());
                return !s.equals(item.getFieldStringValue()) ? (item.getColumnCode() + ",") : "";
            } else if (DATA_TYPE_DATE.equals(item.getDataType())) {
                Date date = (Date) ob;
                return !date.equals(item.getFieldDateNonConvertedValue() != null ? item.getFieldDateNonConvertedValue().getNonConvertedDate() : null) ? (item.getColumnCode() + ",") : "";
            }
        }
        return "";
    }

    /**
     * Register candidate vacancies
     *
     * @param itemVacancies - candidate vacancies
     * @param candidate     - candidate
     */
    public void saveCandidateVacancies(ArrayList<SelectItem> itemVacancies, EdsCrmContact candidate) {
        if (itemVacancies != null && itemVacancies.size() > 0) {
            Set<EdsVacancy> oldVacancies = (Set<EdsVacancy>) new HashSet<>(candidate.getVacancies()).clone();

            Set<EdsVacancy> newVacancies = new HashSet<>();

            for (SelectItem vacancy : itemVacancies) {
                EdsVacancy edsVacancy = vacancyManager.get(vacancy.getId());
                if (edsVacancy != null) {
                    newVacancies.add(edsVacancy);

                    if (candidate.isNew() || !oldVacancies.contains(edsVacancy)) {
                        EdsReference inProgressStatus = referenceManager.findReference(EdsVacancy.VACANCY_STATUSES, EdsVacancy.VS_IN_PROGRESS);
                        if (inProgressStatus != null) {
                            edsVacancy.setStatus(inProgressStatus);
                        }
                    }
                }
            }
            ArrayList<EdsVacancy> list1 = new ArrayList<>(newVacancies);
            ArrayList<EdsVacancy> list2 = new ArrayList<>(candidate.getVacancies());
            ServerUtils.intersect(list1, list2);

            candidate.getVacancies().addAll(list1);
            list2.forEach(candidate.getVacancies()::remove);
        }
    }

    public void unCheckExistingPrimaryContact(Integer accountId) {
        EdsCrmContact clientContact = clientContactManager.getPrimaryClientContact(accountId);
        if (clientContact != null) {
            clientContact.setPrimaryContact(false);
            try {
                crmContactManager.update(clientContact);
                contactSolrComponent.index(clientContact);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Transactional
    public void updateEdsCrmContactAndIndex(EdsCrmContact contact, boolean newCreated, EdsUser user) {
        if (contact != null) {
            crmContactManager.update(contact);
            if (!newCreated) {
                crmContactManager.createHistory(contact);
            }
            contact = crmContactManager.get(contact.getObjectID());
            try {
                contactSolrComponent.index(contact);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (newCreated) {
                EdsCompany company = null;

                if (user == null) {
                    company = companyManager.get(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
                } else {
                    company = user.getCompany();
                }
            }
        }
    }

    private boolean createOrSetCrmAccountToContact(EdsCrmContact contact, CrmAccountItem crmAccount, Boolean isFromSignUp) {
        boolean isNewAccount = false;
        if (contact != null) {
            EdsCrmAccount account = null;
            if (!crmAccount.isNew()) {
                account = crmAccountManager.get(crmAccount.getObjectId());
                if (account != null) {
                    long start = System.currentTimeMillis();
                    CrmAccountItem item = crmServiceLocal.getAccount(account, false);
                    log.info("crmServiceLocal.getAccount >>>>>>>" + (System.currentTimeMillis() - start));
                    if (item != null) {
                        if (crmAccount.getIndustryID() != null) {
                            item.setIndustryID(crmAccount.getIndustryID());
                        }
                        if (crmAccount.getAccountTypes() != null && crmAccount.getAccountTypes().length > 0) {
                            item.setAccountTypes(crmAccount.getAccountTypes());
                        }
                        item.setFromSignUp(isFromSignUp != null && isFromSignUp);
                        if (item.getPhone() == null) {
                            item.setPhone(crmAccount.getPhone());
                        }
                        if (item.getEmail() == null) {
                            item.setEmail(crmAccount.getEmail());
                        }
                        if (item.getFax() == null) {
                            item.setFax(crmAccount.getFax());
                        }
                    }
                    Integer accountID = crmService.saveAccount(item, null, null, false, false, false, false);
                    account = crmAccountManager.get(accountID);
                    contact.setCrmAccount(account);
                }
            } else {
                if (StringUtils.isNotBlank(crmAccount.getName())) {
                    if (!DEFAULT_COMPANY_NAME.equals(crmAccount.getName()) || crmAccount.getCompanyId() == null) {
                        account = crmAccountManager.getCrmAccountByName(crmAccount.getName(), null);
                    }
                    if (account == null) {
                        isNewAccount = true;
                        if (crmAccount.getCompanyId() != null) {
                            crmAccount.setName(crmAccount.getName() + "_" + crmAccount.getCompanyId());
                        }
                        crmAccount.setPhone(crmAccount.getPhone() == null ? contact.getPrimaryPhone() : crmAccount.getPhone());
                        crmAccount.setEmail(crmAccount.getEmail() == null ? contact.getPrimaryEmail() : crmAccount.getEmail());
                        crmAccount.setFromSignUp(isFromSignUp != null && isFromSignUp);
                        EdsFormProperty formProperty = formPropertyManager.getByFormID(LayoutRPC.ACCOUNT_FORM);

                        if (formProperty != null) {
                            Gson gson = new Gson();
                            FormProperty[] formFields = gson.fromJson(formProperty.getSettingsJSONData(), FormProperty[].class);
                            if (formFields != null) {
                                for (FormProperty field : formFields) {
                                    if (field.getCode().equals(CustomFormConstants.CURRENCY) && field.getSelectedId() != null) {
                                        crmAccount.setCurrency(field.getDefaultValue());
                                        crmAccount.setCurrencyId(field.getSelectedId());
                                    }
                                }
                            }
                        }
                        account = crmAccountManager.get(crmService.saveAccount(crmAccount, null, null, false, false, false, false));

                        if (account == null) {
                            account = crmAccountManager.getCrmAccountByName(crmAccount.getName(), null);
                        }
                    }
                    if (crmAccount.getCompanyId() != null) {
                        account.setSignupCompanyId(crmAccount.getCompanyId());
                    }
                    contact.setCrmAccount(account);
                    crmAccount.setObjectId(account.getObjectID());
                    if (account != null) {
                        /*if (account.getOwner() == null) {
                            account.setOwner(contact.getOwner());
                        }*/
                        if (account.getOwners() == null || account.getOwners().isEmpty()) {
                            account.setOwners(Collections.singletonList(contact.getOwner()));
                        }
                        //get necessary contact info for the account
                        if (contact.getItemParams() != null && contact.getItemParams().size() > 0) {
                            String homeFax = null;
                            for (EdsCrmContactItemParams param : contact.getItemParams()) {
                                //the following finds work phone
                                if (param.getParam() == CONTACT_PHONES && param.getRelation() == G_WORK) {
                                    account.setPhone(param.getValue());
                                }
                                //the following finds work fax
                                if (param.getParam() == CONTACT_PHONES && param.getRelation() == G_WORK_FAX) {
                                    account.setFax(param.getValue());
                                }
                                //the following finds work email
                                if (param.getParam() == CONTACT_EMAILS && param.getRelation() == G_WORK) {
                                    account.setEmail(param.getValue());
                                }
                                //the following finds home fax
                                if (param.getParam() == CONTACT_PHONES && param.getRelation() == G_HOME_FAX) {
                                    homeFax = param.getValue();
                                }
                            }
                            if ((account.getPhone() == null || account.getPhone().equals("") && (contact.getPrimaryPhone() != null && !contact.getPrimaryPhone().equals("")))) {
                                account.setPhone(contact.getPrimaryPhone());
                            }
                            if ((account.getEmail() == null || account.getEmail().equals("") && (contact.getPrimaryEmail() != null && !contact.getPrimaryEmail().equals("")))) {
                                account.setEmail(contact.getPrimaryEmail());
                            }
                            if ((account.getFax() == null || account.getFax().equals("") && (homeFax != null))) {
                                account.setFax(homeFax);
                            }
                        }
                    }
                }
            }
            if (account != null) {
                crmAccountManager.update(account);
                try {
                    crmAccountSolrComponent.index(account);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return isNewAccount;
    }

    @Transactional
    public EdsCrmCustomFields saveCustomFields(EdsCrmCustomFields edsCrmCustomField, List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            if (edsCrmCustomField == null) {
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
                edsCrmCustomField = new EdsCrmCustomFields();
                crmCustomFieldsManager.create(edsCrmCustomField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsCrmCustomField, customFieldItems);
            return edsCrmCustomField;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCrmAccounts() {
        List<EdsCrmAccount> accounts = crmAccountManager.getList(new ListingFilterParameter(), null);
        SelectItem[] selectItems = new SelectItem[accounts.size()];
        int i = 0;
        for (EdsCrmAccount account : accounts) {
            selectItems[i] = new SelectItem(account.getObjectID(), account.getName());
            i++;
        }
        return selectItems;
    }


    private void changeEmployeeDetails(EdsCrmContact contact, boolean changed) {
        if (contact != null && contact.getEntityContactID() != null) {
            EdsEmployee employee = employeeManager.getEmployeeByProfileID(contact.getEntityContactID());
            if (employee != null) {
                String oldEmail = employee.getEmail();
                if (!Objects.deepEquals(employee.getFirstName(), contact.getFirstName())) {
                    employee.setFirstName(contact.getFirstName());
                    changed = true;
                }
                if (!Objects.deepEquals(employee.getLastName(), contact.getLastName())) {
                    employee.setLastName(contact.getLastName());
                    changed = true;
                }
                if (!Objects.deepEquals(employee.getMiddleName(), contact.getMiddleName())) {
                    employee.setMiddleName(contact.getMiddleName());
                    changed = true;
                }
                if (!Objects.deepEquals(employee.getEmail(), contact.getPrimaryEmail()) && contact.getPrimaryEmail() != null && !"".equals(contact.getPrimaryEmail())) {
                    employee.setEmail(contact.getPrimaryEmail());
                    changed = true;
                }
                if (changed) {
                    employeeManager.createOrUpdate(employee);
                    try {
                        employeeSolrComponent.index(employee);
                    } catch (SolrServerException e) {
                        log.error("SAVE EMPLOYEE ERROR:" + e.getMessage(), e);
                    } catch (IOException e) {
                        log.error("SAVE EMPLOYEE ERROR2:" + e.getMessage(), e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (!Objects.equals(oldEmail, employee.getEmail())) {
                    userManager.saveUserAuthenticationData(employee, SecurityContext.getCompanyID(), false, false, false);
                }
            }
        }
    }

    private void changeStudentDetails(EdsCrmContact contact) {

    }

    private void createItemParams(final EdsCrmContact contact, HashMap<Integer, HashMap<Integer, ArrayList<String>>> itemParams, boolean isFromAPI) {
        if (contact.getItemParams() != null && !isFromAPI) {
            contact.getItemParams().clear();
        }
        if (itemParams.size() > 0) {
            for (Map.Entry<Integer, HashMap<Integer, ArrayList<String>>> entry : itemParams.entrySet()) {
                Integer param = entry.getKey();
                boolean primaryFound = false;
                for (Map.Entry<Integer, ArrayList<String>> entry_ : entry.getValue().entrySet()) {
                    Integer relation = entry_.getKey();
                    List<String> values = entry_.getValue();
                    if (values != null && values.size() > 0) {
                        for (String value : values) {
                            if (value != null && !"".equals(value.trim())) {
                                createItemParam(contact, param, relation, value);
                                if ((param.equals(EdsCrmContactItemParams.EMAIL) && value.equals(contact.getPrimaryEmail())) || (param.equals(EdsCrmContactItemParams.PHONE) && value.equals(contact.getPrimaryPhone()))) {
                                    primaryFound = true;
                                }
                            }
                        }
                    }
                }
                if (!primaryFound) {
                    if (param.equals(EdsCrmContactItemParams.EMAIL)) {
                        contact.setPrimaryEmail(contact.getPrimaryEmailFromAll());
                    } else {
                        if (param.equals(EdsCrmContactItemParams.PHONE)) {
                            contact.setPrimaryPhone(contact.getPrimaryPhoneFromAll());
                        }
                    }
                } else {
                    primaryFound = false;
                }
            }
        }
    }

    private void createAddressItem(final EdsCrmContact contact, Integer relation, Address address, String requestedFrom) {
        EdsAddress edsAddress = new EdsAddress();
        edsAddress.setContact(contact);
        edsAddress.setRelationType(relation);
        if (address.getAddress() != null) {
            if (ContactListItem.REQUEST_FROM_CONTACT_SYNC.equals(requestedFrom)) {
                edsAddress.setAddress(address.getAddress().split("\n")[0]);
            } else {
                edsAddress.setAddress(address.getAddress());
            }
        }
        if (address.getName() != null) {
            edsAddress.setName(address.getName());
        }
        edsAddress.setAddressb(address.getAddressb());
        edsAddress.setPrimary(address.isPrimary());
        if (address.getCity() != null) {
            edsAddress.setCity(address.getCity());
        }
        if (address.getCountryId() != null) {
            EdsCountry country = countryManager.get(address.getCountryId());
            edsAddress.setCountry(country);
        }

        // ---------------------------------------------------------------------------------------------------------
        /* This checking need for import contacts from google.
           In imported google contacts has only country and state name, hasn't country or state id
           In WFT contact add/edit forms don't set country and state name; set only country or state id
        */
        if (address.getCountry() != null) {
            EdsCountry country = countryManager.getCountryByName(address.getCountry());
            if (country != null) {
                edsAddress.setCountry(country);
            }
        }
        if (address.getState() != null) {
            EdsRegion region = regionManager.getRegionByName(address.getState());
            if (region != null) {
                edsAddress.setState(region);
            }
        }
        // ---------------------------------------------------------------------------------------------------------
        if (address.getStateId() != null) {
            EdsRegion region = regionManager.get(address.getStateId());
            edsAddress.setState(region);
        }
        if (address.getZipCode() != null) {
            edsAddress.setZipCode(address.getZipCode());
        }
        addressManager.create(edsAddress);
        contact.getAddresses().add(edsAddress);
    }

    private EdsCrmContactItemParams createItemParam(final EdsCrmContact contact, int param, int relation, String value) {
        if (value != null && !"".equals(value)) {
            EdsCrmContactItemParams params = new EdsCrmContactItemParams();
            params.setContact(contact);
            params.setLastUpdateTime(new Date());
            params.setParam(param);
            params.setRelation(relation);
            params.setValue(value);
            contactItemParamsManager.create(params);
            contact.getItemParams().add(params);
            return params;
        }
        return null;
    }

    @Transactional
    public Integer saveContactDetailsOfEmployee(final EdsEmployee employee, ContactListItem contactListItem, Boolean isFromSignUp, Address... addresses) {
        EdsUser user = userManager.getUser(); // if user is null then user equals to employee
        if (user == null) {
            user = employee;
        }
//        EdsCompany company = SecurityContext.getInstance().getCompanyId() != null ? companyManager.get(Integer.parseInt(SecurityContext.getInstance().getCompanyId())) : (user != null ? user.getCompany() : null);
        SecurityContext.getInstance().setStaticUserID(user != null ? user.getObjectID() : null);
        EdsCrmContact contact = null;
        if (employee.getProfile() != null) {
            contact = employee.getProfile().getContact();
        } else {
            EdsEmployeeProfile profile = new EdsEmployeeProfile();
            profile.setEmployee(employee);
            profileManager.create(profile);
            employee.setProfile(profile);
            contact = createCrmContactForProfile(profile, contactListItem.getCrmAccount().getAccountName(), isFromSignUp);
            profile.setContact(contact);
            profileManager.update(profile);
        }
        if (contact == null) {
            contact = createCrmContactForProfile(employee.getProfile(), contactListItem.getCrmAccount().getAccountName(), isFromSignUp);
            employee.getProfile().setContact(contact);
            profileManager.update(employee.getProfile());
        }
        //contact never gets null... after these ifs... :)
        if (employee.getProfile() != null) {
            contact.setEntityContactID(employee.getProfile().getObjectID());
        }
        contact.setFirstName(employee.getFirstName());
        contact.setLastName(employee.getLastName());
        contact.setMiddleName(employee.getMiddleName());
        contact.getAuditInfo().setModificationDate(employee.getLastUpdateTime());
        contact.setDateOfBirth(contactListItem.getBirthDate() != null ? contactListItem.getBirthDate().getNonConvertedDate() : null);

        updatePrimaryContactItemParams(contact, EdsCrmContactItemParams.EMAIL, EdsCrmContactItemParams.HOME, employee.getEmail());
        if (contactListItem.getHomePhone().size() > 0) {
            updatePrimaryContactItemParams(contact, EdsCrmContactItemParams.PHONE, EdsCrmContactItemParams.HOME, contactListItem.getHomePhone().get(0));
        }
        if (contactListItem.getWorkPhone().size() > 0) {
            updatePrimaryContactItemParams(contact, EdsCrmContactItemParams.PHONE, EdsCrmContactItemParams.WORK, contactListItem.getWorkPhone().get(0));
        }
        if (contactListItem.getMobile().size() > 0) {
            updatePrimaryContactItemParams(contact, EdsCrmContactItemParams.PHONE, EdsCrmContactItemParams.MOBILE, contactListItem.getMobile().get(0));
        }
        if (contactListItem.getAddresses().size() > 0) {
            Address address = contactListItem.getAddresses().get(0);
            if (address != null) {
                if (employee.getLocation() != null) {
                    if (address.getCity() == null) {
                        address.setCity(employee.getLocation().getCity());
                    }
                    if (employee.getLocation().getCountry() != null && address.getCountryId() == null) {
                        address.setCountryId(employee.getLocation().getCountry().getObjectID());
                    }
                    if (address.getStateId() == null && employee.getLocation().getState() != null) {
                        address.setStateId(employee.getLocation().getState().getObjectID());
                    }
                }
            }
            updatePrimaryContactAddress(contact, address, EdsAddress.HOME);
        }
        contact.setPrimaryEmail(contact.getPrimaryEmailFromAll());
        contact.setPrimaryPhone(contact.getPrimaryPhoneFromAll());
        contact.addCategories(contactCategoryManager.getDefaultCategoryByContactType(contact.getContactType()));
        try {
            contactSolrComponent.index(contact);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return contact.getObjectID();
    }

    @Transactional
    public void updatePrimaryContactAddress(final EdsCrmContact contact, Address address, int relation) {
        EdsAddress edsCrmContactItemAddresses = EdsAddress.getFirstAddress(contact.getAddresses(), true, null, relation);
        int index = contact.getAddresses().indexOf(edsCrmContactItemAddresses);
        if (index > -1) {
            contact.getAddresses().remove(index);
        }
        if (edsCrmContactItemAddresses == null) {
            edsCrmContactItemAddresses = new EdsAddress();
            edsCrmContactItemAddresses.setRelationType(relation);
            edsCrmContactItemAddresses.setContact(contact);
        }
        edsCrmContactItemAddresses.setAddress(address.getAddress());
        edsCrmContactItemAddresses.setCity(address.getCity());
        if (address.getCountryId() != null) {
            edsCrmContactItemAddresses.setCountry(countryManager.get(address.getCountryId()));
        }
        if (address.getStateId() != null) {
            edsCrmContactItemAddresses.setState(regionManager.get(address.getStateId()));
        }
        edsCrmContactItemAddresses.setZipCode(address.getZipCode());
        addressManager.createOrUpdate(edsCrmContactItemAddresses);
        contact.getAddresses().add(index > -1 ? index : 0, edsCrmContactItemAddresses);
    }

    @Transactional
    public void updatePrimaryContactItemParams(final EdsCrmContact contact, int param, int relation, String value) {
        EdsCrmContactItemParams itemParam = EdsCrmContactItemParams.getFirstItemParam(contact.getItemParams(param), false, relation);
        if (itemParam == null) {
            createItemParam(contact, param, relation, value);
        } else {
            itemParam.setValue(value);
            contactItemParamsManager.createOrUpdate(itemParam);
        }
    }

    @Transactional
    public EdsCrmContact createCrmContactForProfile(EdsEmployeeProfile profile, String crmAccountName, Boolean isFromSignUp) {
        EdsUser user = userManager.getUser();
        if (user == null) {
            user = employeeManager.getEmployeeByProfileID(profile.getObjectID());
        }
        EdsCrmContact contact = new EdsCrmContact();
        contact.setContactType(EdsCrmContact.EMPLOYEE_CONTACT);
        contact.setEntityContactID(profile.getObjectID());
        contact.setOwner(user);
        CrmAccountItem account = new CrmAccountItem();
        EdsEmployee employee = employeeManager.getEmployeeByProfileID(profile.getObjectID());
        account.setName(crmAccountName != null ? crmAccountName : employee != null && employee.getCompany() != null ? employee.getCompany().getName() : "");
        createOrSetCrmAccountToContact(contact, account, isFromSignUp);
        createEntity(contact);
        boolean isNewContact = crmContactManager.createOrUpdate(contact);
        if (isNewContact) {
            crmServiceLocal.createContactHistory("Created the contact", contact);
        } else {
            crmServiceLocal.createContactHistory("Updated the contact", contact);
        }
        return contact;
    }

    @Transactional
    public void createEntity(final Object object) {
        EdsEntity entity = new EdsEntity();
        if (object instanceof EdsCrmAccount) {
            entityManager.create(entity);
            ((EdsCrmAccount) object).setEntityID(entity.getObjectID());
        }
        if (object instanceof EdsCrmContact) {
            entityManager.create(entity);
            ((EdsCrmContact) object).setEntityID(entity.getObjectID());
        }
        if (object instanceof EdsCase) {
            entityManager.create(entity);
            ((EdsCase) object).setEntityID(entity.getObjectID());
        }
    }

    private void saveContactAttachments(FileItem[] attachments, EdsCrmContact contact) {
        attachmentUtilsManager.saveAttachments(contact.is(EdsCrmContact.LEAD_CONTACT) ? F_LEAD : (contact.is(EdsCrmContact.CANDIDATE) ? F_CANDIDATE : F_CRM_CONTACT), contact.getObjectID(), contact.getObjectID(), attachments);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean validateUserGoogle() throws Exception {
        return googleContactsService.validateCurrentUser();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean validateUserOffice() throws Exception {
        return googleContactsService.validateCurrentOfficeUser();
    }

    /**
     * @param allWFTContacts - if true then get all WFT contacts, else get only different contacts : in WFT hasn't, has in google contacts
     * @return WF Contact Items array
     * @throws Exception
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem[] getWFTContacts(boolean allWFTContacts) throws Exception {
        ListingFilterParameter fp = new ListingFilterParameter();
        List<EdsCrmContact> contactList = crmContactManager.list(fp);
        ContactListItem[] wftContacts = new ContactListItem[contactList.size()];
        int k = 0;
        if (contactList.size() > 0) {
            for (EdsCrmContact contact : contactList) {
                wftContacts[k++] = getContact(contact, new ListingFilterParameter(true));
            }
        }
        if (allWFTContacts) {
            return wftContacts;
        } else {
            EdsUser user = googleContactsManager.getUser();
            ContactsService myService = googleContactsManager.getLoggedService(user);
            List<ContactEntry> googleContacts = null;
            if (myService != null) {
                googleContacts = googleContactsManager.getContactFeed(myService).getEntries();
            }
            List<ContactListItem> wftContactItems = new ArrayList<>();
            googleContactsManager.checkingContactsExistsInGoogle(wftContacts, googleContacts, wftContactItems, user, true);
            return wftContactItems.toArray(new ContactListItem[]{});
        }
    }

    @Transactional
    public void exportToGoogleContact(ContactListItem[] items, boolean forExport) throws Exception {
        try {
            googleContactsManager.exportWFTContactsToGoogleContacts(items, forExport);
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            throw new GeneralSecurityException(ex.getMessage());
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            throw new AuthenticationException(ex.getMessage());
        } catch (ServiceException ex) {
            ex.printStackTrace();
            throw new ServiceException(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        }
    }

    @Transactional
    public void recurringSyncContactsWithGoogle(Integer employeeId) {
        EdsUser user = employeeManager.get(employeeId);
        if (user != null && user.getCompany().getActive() && !user.getDeleted()) {
            Boolean checkUserToken = false;
            try {
                checkUserToken = googleContactsManager.validateUser(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (checkUserToken) {
                ListingFilterParameter fp = new ListingFilterParameter();
                List<EdsCrmContact> contactList = crmContactManager.list(fp, user);
                ContactListItem[] contactResult = new ContactListItem[contactList.size()];
                int k = 0;
                for (EdsCrmContact account : contactList) {
                    ListingFilterParameter fp_ = new ListingFilterParameter(true);
                    fp_.setForCSVonly(true);
                    contactResult[k++] = getContact(account, fp_);
                }
                try {
                    synchronizeGoogleContacts(contactResult, user);
                } catch (GeneralSecurityException | ServiceException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Transactional
    public void synchronizeContactsWithGoogle() throws Exception {
        if (validateUserGoogle()) {
            ListingFilterParameter fp = new ListingFilterParameter();
            List<EdsCrmContact> contactList = crmContactManager.list(fp);
            ContactListItem[] contactResult = new ContactListItem[contactList.size()];
            int k = 0;
            for (EdsCrmContact account : contactList) {
                //System.out.println( "--------------------------Before CRMServiceImpl.getContactForSync(account, true) :"+(new Date()).toString() );
                contactResult[k++] = getContactForSync(account, true);
                //System.out.println( "--------------------------After CRMServiceImpl.getContactForSync(account, true) :"+(new Date()).toString() );
            }
            synchronizeGoogleContacts(contactResult);
        }
    }

    @Transactional
    public String synchronizeContactsWithGoogleInBackground(String storageType) throws Exception {
        String eventType = SyncGoogleContactsEventListenerImpl.EVENT_SYNC_CONTACT;
        if (OFFICE_365.equals(storageType)) {
            eventType = SyncGoogleContactsEventListenerImpl.EVENT_SYNC_CONTACT_OFFICE;
        }
        List<EdsBusinessEvent> events = businessEventDispatcherManager.getUserSyntGoogleContactEvents(eventType, employeeManager.getUser().getObjectID());

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsServerContacts.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.SYNCHRONIZE);
        ServerUtils.kpiLog(log, kpiLog, "Synchronize contacts with api server ");

        if (events != null && !events.isEmpty()) {
            return "Running";
        }
        boolean istrue = false;
        if (GOOGLE.equals(storageType) && validateUserGoogle()) {
            istrue = true;
        } else if (OFFICE_365.equals(storageType) && validateUserOffice()) {
            istrue = true;
        }
        if (istrue) {
            commonServiceLocal.addGoogleSyncToQueue(eventType);
        }
        return "Success";
    }

    //    @Transactional
    public String synchronizeContactsInBackground(String storageType) throws Exception {
        String eventType = SyncGoogleContactsEventListenerImpl.EVENT_SYNC_CONTACT;

        if (OFFICE_365.equals(storageType)) {
            eventType = SyncGoogleContactsEventListenerImpl.EVENT_SYNC_CONTACT_OFFICE;
        }
        List<EdsBusinessEvent> events = businessEventDispatcherManager.getUserSyntGoogleContactEvents(eventType, employeeManager.getUser().getObjectID());

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsServerContacts.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.SYNCHRONIZE);
        ServerUtils.kpiLog(log, kpiLog, "Synchronize contacts with api server ");

        if (events != null && events.isEmpty()) {
            return "Running";
        }
        boolean istrue = false;
        if (GOOGLE.equals(storageType) && validateUserGoogle()) {
            istrue = true;
        } else if (OFFICE_365.equals(storageType) && validateUserOffice()) {
            istrue = true;
        }
        if (istrue) {
            commonServiceLocal.addContactSyncToQueue(eventType);
        }
        return "Success";
    }

    /**
     * This method used for synchronise google & WFT contacts;
     * 1) get all google contacts, then check for has this contact in WFT contacts : if not contains then import,
     * if contain, merge this contact with WFT contact;
     * 2) get all WFT contacts, then export this contacts to google;
     *
     * @param items
     * @return
     */
    private void synchronizeGoogleContacts(ContactListItem[] items) throws GeneralSecurityException, IOException, ServiceException {
        EdsUser user = googleContactsManager.getUser();
        synchronizeGoogleContacts(items, user);
    }

    private void synchronizeGoogleContacts(ContactListItem[] wftContacts, EdsUser user) throws GeneralSecurityException, IOException, ServiceException {
        ContactsService myService = googleContactsManager.getLoggedService(user);
        if (myService != null) {
            List<ContactEntry> googleContacts = googleContactsManager.getContactFeed(myService).getEntries();
            ContactListItem[] googleContactItems = googleContactsManager.getGoogleContactItems(googleContacts, user);
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
            String type = "";
            if (userSettings.getContactSyncType() != null) {
                type = userSettings.getContactSyncType();
            }
            if (googleContactItems != null && googleContactItems.length > 0) {
                //importGoogleContacts(googleContactItems, user, false);
                if (!KPIMASTER.equals(type)) {
                    importNewGoogleContacts(wftContacts, googleContactItems, user, false);
                }
            }
            if (wftContacts != null && wftContacts.length > 0) {
                //Get
                if (!SERVERMASTER.equals(type)) {
                    googleContactsManager.exportWFTContactsToGoogleContacts(googleContacts, wftContacts, user, false);
                }

            }
        }
    }

    @Transactional
    public void importGoogleContacts(ContactListItem[] items, boolean fromImportView) {
        EdsUser user = crmContactManager.getUser();
        importGoogleContacts(items, user, fromImportView);
    }

    public void importGoogleContacts(ContactListItem[] googleContactItems, EdsUser user, boolean fromImportView) {
        // get WFT contacts from database
        ListingFilterParameter fp = new ListingFilterParameter();
        List<EdsCrmContact> contactList = crmContactManager.list(fp, user);
        ContactListItem[] wftContactItems = new ContactListItem[contactList.size()];
        for (
                int k = 0;
                k < contactList.size();
                k++) {
            wftContactItems[k] = getContact(contactList.get(k), new ListingFilterParameter(true));
        }

        Map<Integer, ContactListItem> differentContacts = getDifferentContacts(googleContactItems, wftContactItems, user, fromImportView);
        Collection<ContactListItem> collection = differentContacts.values();
        ContactListItem[] newContactItems = new ContactListItem[collection.size()];
        if (differentContacts.size() > 0) {
            newContactItems = collection.toArray(new ContactListItem[]{});
        }

        if (wftContactItems.length == 0) {
            newContactItems = googleContactItems;
        }
        for (ContactListItem newContactItem : newContactItems) {
            newContactItem.setOwnerId(user.getObjectID());
            saveContact(newContactItem, null, user, false, true);
        }
    }

    public void importNewGoogleContacts(ContactListItem[] wftContactItems, ContactListItem[] googleContactItems, EdsUser user, boolean fromImportView) {

        Map<Integer, ContactListItem> differentContacts = getDifferentContacts(googleContactItems, wftContactItems, user, fromImportView);
        Collection<ContactListItem> collection = differentContacts.values();
        ContactListItem[] newContactItems = new ContactListItem[collection.size()];
        if (differentContacts.size() > 0) {
            newContactItems = collection.toArray(new ContactListItem[]{});
        }

        if (wftContactItems.length == 0) {
            newContactItems = googleContactItems;
        }
        for (ContactListItem newContactItem : newContactItems) {
            newContactItem.setOwnerId(user.getObjectID());
            saveContact(newContactItem, null, user, false, true);
        }
    }

    private Map<Integer, ContactListItem> getDifferentContacts(ContactListItem[] googleContactItems, ContactListItem[] wftContactItems, EdsUser user, boolean fromImportView) {
        Map<Integer, ContactListItem> wftContacts2 = new HashMap<>(); // contacts map by hashCode contacts full name
        Map<String, ContactListItem> wftContacts1 = new HashMap<>();   // contacts map by contact googleeId
        Map<Integer, ContactListItem> result = new HashMap<>();
        if (wftContactItems != null) {
            for (ContactListItem wftContactItem : wftContactItems) {
                // put contact to contacts map by googleId
                if (wftContactItem.getGoogleId() != null && !"".equals(wftContactItem.getGoogleId())) {
                    wftContacts1.put(wftContactItem.getGoogleId(), wftContactItem);
                }
                // put contact to contacts map by contacts fullName hashCode
                StringBuilder contactInfo = new StringBuilder();
                if (wftContactItem.getFirstName() != null && !"".equals(wftContactItem.getFirstName())) {
                    contactInfo.append(wftContactItem.getFirstName().replace(" ", ""));
                }
                if (wftContactItem.getLastName() != null && !"".equals(wftContactItem.getLastName())) {
                    contactInfo.append(wftContactItem.getLastName().replace(" ", ""));
                }
                if (wftContactItem.getPrimaryEmail() != null && !"".equals(wftContactItem.getPrimaryEmail())) {
                    contactInfo.append(wftContactItem.getPrimaryEmail().replace(" ", ""));
                }
                if (contactInfo != null && !"".contentEquals(contactInfo)) {
                    Integer contactHash = contactInfo.toString().hashCode();
                    if (!wftContacts2.containsValue(wftContactItem)) {
                        wftContacts2.put(contactHash, wftContactItem);
                    }
                }
            }
        }
        if (googleContactItems != null && googleContactItems.length > 0) {
            for (
                    int i = 0;
                    i < googleContactItems.length;
                    i++) {
                ContactListItem contactListItem = wftContacts1.get(googleContactItems[i].getGoogleId());
                if (!wftContacts1.containsKey(googleContactItems[i].getGoogleId())) {
                    boolean hasInResult = false;
                    StringBuilder info = new StringBuilder();
                    if (googleContactItems[i].getContactName() != null && !"".equals(googleContactItems[i].getContactName())) {
                        info.append(googleContactItems[i].getContactName().replace(" ", ""));
                    }
                    if (googleContactItems[i].getPrimaryEmail() != null && !"".equals(googleContactItems[i].getPrimaryEmail())) {
                        info.append(googleContactItems[i].getPrimaryEmail().replace(" ", ""));
                    }
                    if (!"".contentEquals(info)) {
                        Integer hash = info.toString().hashCode();
                        // if google contact not contain in WFT Contacts
                        if (!wftContacts2.containsKey(hash)) {
                            result.put(hash, googleContactItems[i]);
                            hasInResult = true;
                        } else {
                            // if google contact contain in WFT Contacts
                            hasInResult = true;
                            if (wftContacts2.get(hash).getOwnerId() != null && wftContacts2.get(hash).getOwnerId().equals(user.getObjectID())) {
                                if (!fromImportView) {
//                                    googleContactItems[i].setVisible(wftContacts2.get(hash).isVisible());
                                    if (googleContactItems[i].getUpdatedDate() != null && wftContacts2.get(hash).getUpdatedDate() != null &&
                                            googleContactItems[i].getUpdatedDate().getTime() > wftContacts2.get(hash).getUpdatedDate().getTime()) {
                                        googleContactItems[i].setObjectId(wftContacts2.get(hash).getObjectId());
                                        result.put(hash, googleContactItems[i]);
                                    }
                                }
                            }
                        }
                    }
                    if (!hasInResult) {
                        result.put(i, googleContactItems[i]);
                    }
                } else {
                    if (contactListItem.getOwnerId() != null && contactListItem.getOwnerId().equals(user.getObjectID())) {
                        if (!fromImportView) {
//                            googleContactItems[i].setVisible(contactListItem.isVisible());
                            if (googleContactItems[i].getUpdatedDate() != null && contactListItem.getUpdatedDate() != null &&
                                    googleContactItems[i].getUpdatedDate().getTime() > contactListItem.getUpdatedDate().getTime()) {
                                googleContactItems[i].setObjectId(contactListItem.getObjectId());
                                result.put(i, googleContactItems[i]);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Transactional
    public ArrayList<Integer> deleteContacts(ArrayList<Integer> contactIDs, Integer ownerID, boolean deleteFromGoogle) {
        ArrayList<Integer> contactIDs2 = new ArrayList<>(contactIDs);
        EdsUser user = ownerID != null ? userManager.get(ownerID) : googleContactsManager.getUser();
        List<Integer> deleteds = googleContactsManager.deleteContact(contactIDs2, user.getObjectID());
        if (deleteFromGoogle) {
            deleteContactFromGoogle(deleteds);
            deleteContactFromOffice(deleteds);
        }
        contactIDs2.removeAll(deleteds);
        if (contactIDs.size() == 1) {
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsCrmContact.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.DELETE);
            kpiLog.setEntityId(contactIDs.get(0));
            ServerUtils.kpiLog(log, kpiLog, "Contact deleted");
        }
        return contactIDs2;
    }

    private void deleteContactFromGoogle(List<Integer> contactIDs) {
        EdsUser user = crmContactManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        if (userSettings.getContactSyncType() != null && SERVERMASTER.equals(userSettings.getContactSyncType())) {
            return;
        }

        ContactsService myService = null;
        try {
            myService = googleContactsManager.getLoggedService(user);
        } catch (AuthenticationException | IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        List<ContactEntry> googleContacts = null;
        if (myService != null) {
            try {
                googleContacts = googleContactsManager.getContactFeed(myService).getEntries();
            } catch (IOException | ServiceException e) {
                e.printStackTrace();
            }
            Map<String, ContactEntry> googleContactsMap = new HashMap<>();
            ContactEntry contactEntry = null;
            if (googleContacts != null && !googleContacts.isEmpty()) {
                for (ContactEntry entry : googleContacts) {
                    googleContactsMap.put(entry.getId(), entry);
                    StringBuilder info = new StringBuilder();
                    if (entry.getTitle() != null && StringUtils.isNotBlank(entry.getTitle().getPlainText())) {
                        info.append(entry.getTitle().getPlainText().replace(" ", ""));
                    }
                    if (entry.getEmailAddresses() != null && !entry.getEmailAddresses().isEmpty() && StringUtils.isNotBlank(entry.getEmailAddresses().get(0).getAddress())) {
                        info.append(entry.getEmailAddresses().get(0).getAddress());
                    }
                    if (StringUtils.isNotBlank(info.toString())) {
                        Integer hash = info.toString().hashCode();
                        if (!googleContactsMap.containsKey(hash.toString())) {
                            googleContactsMap.put(hash.toString(), entry);
                        }
                    }
                }
            }
            for (Integer contactID : contactIDs) {
                EdsCrmContact contact = crmContactManager.get(contactID);
                if (StringUtils.isNotBlank(contact.getGoogleId())) {
                    if (googleContactsMap.containsKey(contact.getGoogleId())) {
                        contactEntry = googleContactsMap.get(contact.getGoogleId());
                    }
                } else {
                    StringBuilder fullName = new StringBuilder();
                    if (contact.getFirstName() != null && !"".equals(contact.getFirstName())) {
                        fullName.append(contact.getFirstName().trim());
                    }
                    if (contact.getLastName() != null && !"".equals(contact.getLastName())) {
                        fullName.append(contact.getLastName().trim());
                    }
                    if (contact.getPrimaryEmail() != null && !"".equals(contact.getPrimaryEmail())) {
                        fullName.append(contact.getPrimaryEmail().trim());
                    }
                    if (!"".contentEquals(fullName)) {
                        Integer hash = fullName.toString().replace(" ", "").hashCode();
                        if (googleContactsMap.containsKey(hash.toString())) {
                            contactEntry = googleContactsMap.get(hash.toString());
                        }
                    }
                }
                if (contactEntry != null) {
                    try {
                        contactEntry.delete();
                    } catch (IOException | ServiceException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void deleteContactFromOffice(List<Integer> contactIDs) {
        EdsUser user = crmContactManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        if (userSettings.getContactSyncType() != null && SERVERMASTER.equals(userSettings.getContactSyncType())) {
            return;
        }

        Office365AccessTokenDTO tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), OFFICE_365);

        for (Integer contactID : contactIDs) {
            EdsCrmContact contact = crmContactManager.get(contactID);
            if (StringUtils.isNotBlank(contact.getGoogleId())) {
                office365ContactService.deleteAContact(tokenDTO, contact.getGoogleId());
            }

        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem[] getGoogleContacts(boolean allGoogleContacts) throws Exception {
        if (allGoogleContacts) {
            return googleContactsManager.getGoogleContactItems();
        } else {
            ListingFilterParameter fp = new ListingFilterParameter();
            List<EdsCrmContact> contactList = crmContactManager.list(fp);
            ContactListItem[] wftContacts = new ContactListItem[contactList.size()];
            int k = 0;
            if (!contactList.isEmpty()) {
                for (EdsCrmContact contact : contactList) {
                    wftContacts[k++] = getContact(contact, new ListingFilterParameter(true));
                }
            }
            Map<Integer, ContactListItem> contactItemsMap = getDifferentContacts(googleContactsManager.getGoogleContactItems(), wftContacts, crmContactManager.getUser(), true);
            List<ContactListItem> entryList = new ArrayList<>(contactItemsMap.values());
            return entryList.toArray(new ContactListItem[]{});
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem editContact(int contactType, Integer objectId, Integer accountId, Integer webFormID, boolean forMobile) {
        ContactListItem item = new ContactListItem();
        item.setContactType(contactType);
        //dropdowns begin
        if (webFormID != null) {
            EdsWebForm webform = webFormManager.get(webFormID);
            SecurityContext.getInstance().setStaticUserID(webform.getOwner() != null ? webform.getOwner().getObjectID() : 1);
            accountId = null;
        }

        EdsUser user = userManager.getUser();

        item.setContactImAddress(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact._IM_ADDRESSES), ServerUtils.REFERENCE));

        item.setCountries(commonService.getCountries());
        item.setStates(commonService.getRegions());
        item.setMartialStatusList(this.commonServiceLocal.convertReference2SelectItem(EdsEmployeeProfile.MARTIAL_STATUS, false, null));


        item.setRelationships(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact.CONTACT_RELATION), ServerUtils.REFERENCE));
        ArrayList<ContactCategoryListItem> categoriesList = contactCategoryServiceLocal.getContactCategories();
        TreeSelectItem[] categories = ContactCategoryListItem.getAsTreeSelectItem(categoriesList);
        item.setCategories(forMobile ? TreeSelectItem.withoutTreeCapability(new ArrayList<>(Arrays.asList(categories != null ? categories : new TreeSelectItem[0]))).toArray(new TreeSelectItem[]{}) : categories);
        item.setContactCategoryListItems(categoriesList.toArray(new ContactCategoryListItem[]{}));
        item.setDepartments(getContactSelectItems(CONTACT_DEPARTMENTS));
        if (contactType == ContactListItem.CANDIDATE) {
            item.setLeadAssignees(crmServiceLocal.getOwnersListByPermission(PermissionConstants.HRMS_SHOW_IN_CANDIDATE_OWNER));
            Map<String, List<CustomTableRpc>> map = new HashMap<>();
            EdsCrmContact candidate = crmContactManager.get(objectId);
            if (candidate != null) {
                Set<EdsCandidateItemTable> itemTables = candidate.getCandidateItemTables();
                if (itemTables != null && itemTables.size() > 0) {

                    for (EdsCandidateItemTable itemTable : itemTables) {
                        CustomTableRpc rpc = itemTable.getRpc();

                        rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                                commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.CandidateCustomItemTable, rpc.getUuid())));

                        map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                    }
                    item.setCandidateCustomTableItems(map);
                }
                EdsDepartment candidateDepartment = candidate.getCandidateDepartment();
                EdsPosition candidatePosition = candidate.getCandidatePosition();
                EdsTimeSlot timeSlot = candidate.getTimeSlot();
                if (candidateDepartment != null) {
                    item.setDepartmentItem(new SelectItem(candidateDepartment.getObjectID(), candidateDepartment.getName()));
                }
                if (candidatePosition != null) {
                    item.setPositionItem(new SelectItem(candidatePosition.getObjectID(), candidatePosition.getName()));
                }

                if (timeSlot != null) {
                    item.setTimeSlotItem(new SelectItem(timeSlot.getObjectID(), timeSlot.getName()));
                }
                item.setPassportNumber(candidate.getPassportNumber());

            }

        } else {
            item.setLeadAssignees(crmServiceLocal.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE));
        }
        if (contactType == ContactListItem.LEAD_CONTACT) {
            List<EdsReference> leadSourceList = referenceManager.listReferences(EdsCrmContact._LEAD_SOURCE);
            ArrayList<EdsReference> listOfObject = new ArrayList<>(leadSourceList);
            item.setLeadSources(ServerUtils.getAsSelectItem(listOfObject, ServerUtils.REFERENCE));
//            item.setLeadStatuses(result);
            item.setLeadStatuses(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_STATUS), ServerUtils.REFERENCE));
            for (SelectItem reference : item.getLeadStatuses()) {
                if (objectId == null && EdsCrmContact.ATTEMPTED_TO_CONTACT.equals(reference.getCode())) {
                    item.setLeadStatus( new SelectItem(reference.getId(), reference.getName()));
                }
            }
            item.setLeadRatings(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact._LEAD_RATING), ServerUtils.REFERENCE));
            if (objectId == null) {
                item.setLeadAssigneeID(user.getObjectID());
            }
        } else {
            if (contactType == ContactListItem.CANDIDATE) {
                if (objectId == null) {
                    item.setCurrentEmployer(user != null ? user.getName() : null);
                }
                item.setLocations(locationManager.getLocationsAsSelectItems(new ListingFilterParameter()));
                item.setCandidateSources(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact._CANDIDATE_SOURCE), ServerUtils.REFERENCE));
                item.setCandidateStatuses(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact._CANDIDATE_STATUS), ServerUtils.REFERENCE));

                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setBriefly(false);
                fp.setProjectId(objectId != null ? crmContactManager.getProjectIDByContact(objectId) : null);
                fp.setCrmContactId(objectId);
                List<EdsVacancy> list = vacancyManager.list(fp);
                if (list != null && list.size() > 0) {
                    ArrayList<SelectItem> vacanciesList = new ArrayList<>();
                    for (EdsVacancy vacancy : list) {
                        vacanciesList.add(vacancy.getAsSelectItem());
                    }
                    item.setVacancies(vacanciesList);
                }
                if (objectId != null) {
                    item.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_CANDIDATE, objectId)));
                }
            }
        }
        if (objectId == null && webFormID != null) {
            EdsWebForm webform = webFormManager.get(webFormID);
            if (webform != null && webform.getWebFormSource() != null) {
                ReferenceItem sourceItem = webform.getWebFormSource().getRPC();
                if (contactType == ContactListItem.CANDIDATE) {
                    item.setCandidateSource(sourceItem);
                } else {
                    item.setLeadSourceID(sourceItem.getObjectID());
                    item.setLeadSource(sourceItem.getName());
                }
            }
        }

        EdsCrmContact contact = new EdsCrmContact();
        if (item.getContactType() == null) {
            item.setContactType(ContactListItem.CRM_CONTACT);
        }
        if (objectId != null) {
            contact = crmContactManager.get(objectId);
            item = contact.getRPC(new ListingFilterParameter(false), item);
            item.setCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(contact.getCustomFields(), commonService.getCompanyCustomFields(item.isLeadContact() ? ViewName.Lead : (item.isCandidate() ? ViewName.Candidate : ViewName.Contact))));
            EdsAttachment photo = null;
            if (contact.getPhoto() != null) {
                item.setContactImageUrl(getImageUrl(contact.getPhoto().getObjectID()));
            }
            if (contact != null) {
                Set<EdsCrmContactCustomItemTable> itemTables = contact.getItemTables();

                HashMap<String, ArrayList<CustomTableRpc>> map = new HashMap<>();

                if (itemTables != null || itemTables.size() > 0) {

                    for (EdsCrmContactCustomItemTable itemTable : itemTables) {
                        CustomTableRpc rpc = itemTable.getRpc();

                        rpc.setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(itemTable.getCustomFields(),
                                commonServiceLocal.getCompanyCustomFieldsByCategory(ViewName.LeadItem, rpc.getUuid())));

                        map.computeIfAbsent(itemTable.getUuid(), x -> new ArrayList<>()).add(rpc);
                    }
                    item.setCustomTableItems(map);
                }
                Map<String, ArrayList<CustomTableRpc>> tableItems = item.getCustomTableItems();


                for (List<CustomTableRpc> tableRpcs : tableItems.values()) {
                    tableRpcs.sort(Comparator.comparing(CustomTableRpc::getId));
                }

                if (contact.getCrmContactItems().size() > 0) {
                    OpportunityItem[] items = new OpportunityItem[contact.getCrmContactItems().size()];
                    ArrayList<OpportunityItem> listItems = new ArrayList<>();
                    int index = 0;
                    for (EdsCrmContactItem it : contact.getCrmContactItems()) {
                        items[index] = new OpportunityItem();
                        items[index].setId(it.getObjectID());
                        items[index].setItemID(it.getObjectID() != null ? it.getItem().getObjectID() : null);
                        items[index].setItemName(it.getItem() != null ? it.getItem().getName() : it.getItemName());
                        items[index].setItemNumber(it.getItem() != null ? it.getItem().getProductNumber() : "");
                        items[index].setQty(it.getQty());
                        items[index].setDescription(it.getDescription());
                        items[index].setPrice(it.getPrice());
                        if (it.getVat() != null) {
                            items[index].setTaxItem(it.getVat().createTaxItem());
                            items[index].setTaxAmount(it.getItemCalculatedTaxAmount());
                        }

                        items[index].setNet(it.getNet());
                        items[index].setSubTotal(it.getSubTotal());

                        if (it.getUnitMeasurement() != null) {
                            items[index].setUnitMeasurement(it.getUnitMeasurement().getAsSelectItem());
                        }

                        if (it.getCategory() != null) {
                            items[index].setProductCategory(new SelectItem(it.getCategory().getObjectID(), it.getCategory().getName()));
                        }

                        if (it.getBrand() != null) {
                            items[index].setProductBrand(new SelectItem(it.getBrand().getObjectID(), it.getBrand().getName()));
                        }

                        items[index].setSupplierID(it.getSupplierID());
                        items[index].setSupplierName(it.getSupplierName());

                        ArrayList<CompanyCustomFieldItem> itemCustomFields = new ArrayList<>();

                        for (CompanyCustomFieldItem customFieldItem : commonService.getCompanyAllCustomFields(ViewName.LeadItem)) {
                            itemCustomFields.add(customFieldItem.cloneObject());
                        }

                        items[index].setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(it.getCustomFields(),
                                itemCustomFields));
                        listItems.add(items[index]);
                        index++;
                    }
                    item.setItems(listItems.toArray(new OpportunityItem[0]));
                }

                Map<Integer, ArrayList<String>> telegramChats = contact.getParams(EdsCrmContactItemParams.TELEGRAM_CHATS);
                if (telegramChats.size() > 0) {
                    ArrayList<SelectItem> chats = new ArrayList<>();
                    for (Integer botId : telegramChats.keySet()) {
                        TelegramSettingsItem bot = telegramChatService.getTelegramSettingsItem(botId);
                        TelegramChatListItem chat = telegramChatService.getChat(Integer.valueOf(telegramChats.get(botId).get(0)));
                        chats.add(new SelectItem(botId, chat.getObjectId(), chat.getChatName(), bot.getToken()));
                    }
                    item.setTelegramChats(chats);
                }
            }
        } else {
            item.setOwner(user.getName());
            item.setOwnerId(user.getObjectID());
            ArrayList<Address> addresses = new ArrayList<>();
            Integer countryID = user.getCompany().getCountryZone().getCountry().getObjectID();
            Address address = new Address();
            address.setCountryId(countryID);
            address.setPrimary(true);
            address.setName(commonLocalizer.localize(PdfLocalizationName.billingAddress, "Billing Address"));
            addresses.add(address);
            item.setAddresses(addresses);
            if (contactType == ContactListItem.CANDIDATE) {
                EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                Integer intNumber = crmContactManager.getCandidateLastNumber();
                if (settings != null && settings.getCandidateNumberingFormat() != null) {
                    item.setNumberData(settings.parseNumberData(intNumber != null ? intNumber : 1, settings.getCandidateNumberingFormat()));
                } else {
                    item.setNumberData(EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_CANDIDATE_PREFIX));
                }
            }
        }
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        if (settings != null && settings.getCandidateNumberingFormat() != null && item.getNumberData() != null) {
            item.getNumberData().setNumberFormat(settings.getCandidateNumberingFormat());
        } else {
            if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER)) {
                item.setNumberData(EdsNumberingSettings.getDefaultData(item.getNumberData() != null && item.getNumberData().getIntNumber() != null ? item.getNumberData().getIntNumber() - 1 : 1, EdsNumberingSettings.DEF_CANDIDATE_PREFIX));
            }
        }
        item.getCrmAccount().setIndustries(getContactSelectItems(_COMPANY_WORKAREA));

        SelectItem[] contactTitles = getContactSelectItems(_TITLE);
        Locale userLocale = ServerSecurityContext.getInstance().getUserLocale();
        ArrayList<SelectItem> titleList = new ArrayList<>();
        for (SelectItem title : contactTitles) {
            if (userLocale != null && "uz".equalsIgnoreCase(userLocale.toString()) && !"MRS".equalsIgnoreCase(title.getDescription()) && !"MS".equalsIgnoreCase(title.getDescription())) {
                titleList.add(title);
            }
        }
        if (userLocale != null && "uz".equalsIgnoreCase(userLocale.toString())) {
            item.getCrmAccount().setTitle(titleList.toArray(new SelectItem[0]));
        } else {
            item.getCrmAccount().setTitle(contactTitles);
        }

        item.getCrmAccount().setAccountTypes(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE), ServerUtils.REFERENCE));
        if (contact.getCrmAccount() != null) {
            item.setCrmAccount(contact.getCrmAccount().getRPC(item.getCrmAccount(), false));
        } else {
            if (accountId != null) {
                EdsCrmAccount account = crmAccountManager.get(accountId);
                if (account != null) {
                    item.setCrmAccount(account.getRPC(item.getCrmAccount(), false));
                }
            }
        }
        if (item.getCrmAccount() != null) {
            item.setSupervisors(crmService.getContactsByAccount(item.getCrmAccount().getObjectId(), item.getObjectId()));
        }
        if (!contact.isNew()) {
            wrapSpokenLanguages(contact, item);
        }
        return item;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getAccountTypes() {
        return ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE), ServerUtils.REFERENCE);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRelationships() {
        return ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmContact.CONTACT_RELATION), ServerUtils.REFERENCE);
    }

    @Override
    public List<AddressTO> getAddresses(Integer contactId) {
        List<EdsAddress> addressList = addressManager.getContactAddresses(contactId);
        if (addressList != null && !addressList.isEmpty()) {
            List<AddressTO> addressTOList = new ArrayList<>(addressList.size());
            for (EdsAddress address : addressList) {
                AddressTO addressTO = new AddressTO();
                addressTO.setName(address.getName());
                addressTO.setIsPrimary(address.isPrimary());
                addressTO.setType(ContactParamEnum.getParamAsSelectItemTO(address.getRelationType()));
                addressTO.setAddress1(address.getAddress());
                addressTO.setAddress2(address.getAddressb());
                if (address.getCountry() != null) {
                    addressTO.setCountry(new SelectItemTO(address.getCountry().getObjectID(), address.getCountry().getName(), address.getCountry().getCode(), ""));
                }
                if (address.getState() != null) {
                    addressTO.setState(new SelectItemTO(address.getState().getObjectID(), address.getState().getName()));
                }
                addressTO.setCity(address.getCity());
                addressTO.setPostCode(address.getZipCode());
                addressTOList.add(addressTO);
            }
            return addressTOList;
        }
        return null;
    }

    /**
     * API
     *
     * @param contactId
     * @return ContactTO
     */
    public ContactTO getContactParams(Integer contactId) {

        List<EdsCrmContactItemParams> contactParams = contactItemParamsManager.getContactParams(contactId);
        ContactTO contactTO = new ContactTO();

        List<ContactParamTO> phones = new ArrayList<>();
        List<ContactParamTO> emails = new ArrayList<>();
        List<ContactParamTO> imAddresses = new ArrayList<>();
        List<ContactParamTO> webAddresses = new ArrayList<>();

        if (contactParams != null && contactParams.size() > 0) {
            for (EdsCrmContactItemParams param : contactParams) {

                ContactParamTO paramTO = new ContactParamTO();
                paramTO.setId(param.getObjectID());
                paramTO.setName(param.getValue());
                paramTO.setType(ContactParamEnum.getParamAsSelectItemTO(param.getRelation()));

                if (EdsCrmContactItemParams.PHONE == param.getParam()) {
                    paramTO.setIsPrimary(param.getValue().equals(param.getContact().getPrimaryPhone()));
                    phones.add(paramTO);
                } else if (EdsCrmContactItemParams.EMAIL == param.getParam()) {
                    paramTO.setIsPrimary(param.getValue().equals(param.getContact().getPrimaryEmail()));
                    emails.add(paramTO);
                } else if (EdsCrmContactItemParams.IMADDRESS == param.getParam()) {
                    imAddresses.add(paramTO);
                } else if (EdsCrmContactItemParams.WEBSITE == param.getParam()) {
                    webAddresses.add(paramTO);
                }
            }
        }

        contactTO.setPhones(phones);
        contactTO.setEmails(emails);
        contactTO.setImAddresses(imAddresses);
        contactTO.setWebAddresses(webAddresses);

        return contactTO;
    }

    /**
     * API
     *
     * @param contactId
     * @param paramType
     * @return
     */
    public List<SelectItemTO> getContactParamsAsSelectItemTO(Integer contactId, Integer paramType) {
        List<EdsCrmContactItemParams> crmContactItemParams = contactItemParamsManager.getContactParams(contactId, paramType);
        List<SelectItemTO> selectItemTOs = new ArrayList<>();
        if (crmContactItemParams != null && crmContactItemParams.size() > 0) {
            for (EdsCrmContactItemParams param : crmContactItemParams) {
                SelectItemTO itemTO = new SelectItemTO();
                itemTO.setId(param.getObjectID());
                itemTO.setName(param.getValue());
                if (Constants.CONTACT_RELATIONSHIPS == paramType) {
                    EdsReference reference = referenceManager.get(param.getRelation());
                    if (reference != null) {
                        itemTO.setCode(reference.getCode());
                        itemTO.setDescription(reference.getDescription());
                    }
                }
                selectItemTOs.add(itemTO);
            }
        }
        return selectItemTOs;
    }

    /**
     * API
     *
     * @param contactId
     * @param paramType
     * @return
     */
    @Override
    public List<ContactParamTO> getContactParams(Integer contactId, Integer paramType) {
        List<EdsCrmContactItemParams> crmContactItemParams = null;
        if (paramType != null) {
            crmContactItemParams = contactItemParamsManager.getContactParams(contactId, paramType);
        } else {
            crmContactItemParams = contactItemParamsManager.getContactParams(contactId);
        }
        List<ContactParamTO> paramTOs = new ArrayList<>();
        if (crmContactItemParams != null && crmContactItemParams.size() > 0) {
            for (EdsCrmContactItemParams param : crmContactItemParams) {
                ContactParamTO paramTO = new ContactParamTO();
                paramTO.setId(param.getObjectID());
                paramTO.setName(param.getValue());
                if (paramType != null && Constants.CONTACT_EMAILS == paramType) {
                    paramTO.setIsPrimary(param.getValue().equals(param.getContact().getPrimaryEmail()));
                }
                if (paramType != null && Constants.CONTACT_PHONES == paramType) {
                    paramTO.setIsPrimary(param.getValue().equals(param.getContact().getPrimaryPhone()));
                }
                paramTO.setType(ContactParamEnum.getParamAsSelectItemTO(param.getRelation()));
                paramTOs.add(paramTO);
            }
        }
        return paramTOs;
    }

    /**
     * API
     *
     * @param item
     * @return
     */
    @Transactional
    public List<ContactParamTO> saveContactParams(ContactListItem item, Integer paramType) {
        if (item != null && item.getObjectId() != null) {
            HashMap<Integer, HashMap<Integer, ArrayList<String>>> itemParams = ContactListItem.getAllItemParamsAsMap(item);
            contactItemParamsManager.deleteContactItemParams(item.getObjectId(), paramType);

            EdsCrmContact contact = crmContactManager.get(item.getObjectId());
            if (item.getPrimaryEmail() != null) {
                contact.setPrimaryEmail(item.getPrimaryEmail());
            }
            if (item.getPrimaryPhone() != null) {
                contact.setPrimaryPhone(item.getPrimaryPhone());
            }
            createItemParams(contact, itemParams, true);
            crmContactManager.update(contact);
            EdsUser user = crmContactManager.getUser();
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, contact, user);
            workflowEvent.setEntityType(contact.is(EdsCrmContact.LEAD_CONTACT) ? RelationItem.TYPE_LEAD : (contact.is(EdsCrmContact.CANDIDATE) ? RelationItem.TYPE_CANDIDATE : RelationItem.TYPE_CONTACT));
            baseEventPostProcessor.registerEvent(CrmLeadEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, contact, user);

            return getContactParams(item.getObjectId(), paramType);
        }
        return new ArrayList<>(1);
    }

    /**
     * API
     *
     * @param item
     * @return
     */
    @Transactional
    public List<SelectItemTO> saveContactParamsAsSelectItemTO(ContactListItem item, Integer paramType) {
        if (item != null && item.getObjectId() != null) {
            HashMap<Integer, HashMap<Integer, ArrayList<String>>> itemParams = ContactListItem.getAllItemParamsAsMap(item);
            contactItemParamsManager.deleteContactItemParams(item.getObjectId(), paramType);

            EdsCrmContact contact = crmContactManager.get(item.getObjectId());

            createItemParams(contact, itemParams, true);
            crmContactManager.update(contact);
            EdsUser user = crmContactManager.getUser();
            EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, contact, user);
            workflowEvent.setEntityType(contact.is(EdsCrmContact.LEAD_CONTACT) ? RelationItem.TYPE_LEAD : (contact.is(EdsCrmContact.CANDIDATE) ? RelationItem.TYPE_CANDIDATE : RelationItem.TYPE_CONTACT));
            baseEventPostProcessor.registerEvent(CrmLeadEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, contact, user);

            return getContactParamsAsSelectItemTO(item.getObjectId(), paramType);
        }
        return new ArrayList<>(1);
    }

    @Override
    @Transactional
    public void deleteContactParam(Integer objectID) {
        EdsCrmContactItemParams crmContactItemParams = contactItemParamsManager.get(objectID);
        contactItemParamsManager.delete(crmContactItemParams);
    }

    /*@Override
    public List<Integer> getContactCategoryIDs(Integer contactID) {
        return contactCategoryManager.getContactCategoryIDs(contactID);
    }*/

    @Override
    public void copyEmployeeCustomFields(EdsEmployeeCustomFields employeeCustomFields, EdsCrmContact contact) {
        if (employeeCustomFields != null) {
            List<EdsCompanyCustomFieldsSettings> employeeCustomFieldsSettings = companyCFSettingsManager.getCompanyCustomFieldsByEntityName(CFEMPLOYEE);
            List<EdsCompanyCustomFieldsSettings> contactCustomFieldsSettings = companyCFSettingsManager.getCompanyCustomFieldsByEntityName(CFCONTACT);
            EdsCrmCustomFields contactCustomFields = contact.getCustomFields() == null ? new EdsCrmCustomFields() : contact.getCustomFields();
            for (EdsCompanyCustomFieldsSettings employeeCustomFieldsSetting : employeeCustomFieldsSettings) {
                for (EdsCompanyCustomFieldsSettings customFieldSetting : contactCustomFieldsSettings) {
                    CompanyCustomFieldItem companyCustomFieldItem = new CompanyCustomFieldItem();
                    if (employeeCustomFieldsSetting.getAliasName() != null && employeeCustomFieldsSetting.getAliasName().equals(customFieldSetting.getAliasName())) {
                        if (employeeCustomFieldsSetting.getDataType().equals(customFieldSetting.getDataType())) {
                            companyCustomFieldItem.setColumnCode(customFieldSetting.getColumnCode());
                            if (DATA_TYPE_DATE.equals(employeeCustomFieldsSetting.getDataType())) {
                                companyCustomFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable((Date) employeeCustomFields.getValueByCode(employeeCustomFieldsSetting.getDataType(), employeeCustomFieldsSetting.getColumnCode())));
                                CustomFieldsUtils.setDateCustomFields(contactCustomFields, companyCustomFieldItem);
                            } else if (DATA_TYPE_NUMBER.equals(employeeCustomFieldsSetting.getDataType())) {
                                companyCustomFieldItem.setFieldStringValue((Double) employeeCustomFields.getValueByCode(employeeCustomFieldsSetting.getDataType(), employeeCustomFieldsSetting.getColumnCode()));
                                CustomFieldsUtils.setDoubleCustomFields(contactCustomFields, companyCustomFieldItem);
                            } else {
                                companyCustomFieldItem.setFieldStringValue((String) employeeCustomFields.getValueByCode(employeeCustomFieldsSetting.getDataType(), employeeCustomFieldsSetting.getColumnCode()));
                                CustomFieldsUtils.setStringCustomFields(contactCustomFields, companyCustomFieldItem);
                            }
                        }
                    }
                }
            }
            crmCustomFieldsManager.createOrUpdate(contactCustomFields);
            contact.setCustomFields(contactCustomFields);
            crmContactManager.update(contact, true);
        }
    }

    public String getImageUrl(Integer id) {
        return uploadManager.getFileURL(id);
    }

    /*@Transactional
    public void createSystemContactCategories(String from) {
        Integer companyID = null;
        try {
            companyID = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (companyID != null) {
            System.out.println("COMPANY_ID:>>>>>>>> : " + companyID);

            EdsCompany company = companyManager.get(companyID);
            List<EdsEmployee> admins = employeeManager.getAdministrators();
            EdsUser companyCreator = null;
            if (admins != null && admins.size() > 0) {
                companyCreator = admins.get(0);
            }
            if (companyCreator != null) {
                //CRM category
                ContactCategoryListItem crmCategory = new ContactCategoryListItem();
                crmCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                crmCategory.setName(crmLocalizer.localize(PdfLocalizationName.salesContacts));
                crmCategory.setDescription(crmLocalizer.localize(PdfLocalizationName.crmContact));
                crmCategory.setCategoryType(EdsContactCategory.CRM_CONTACT_CATEGORY);
                saveContactCategory(crmCategory, companyCreator, from);

                //Client category
                ContactCategoryListItem clientCategory = new ContactCategoryListItem();
                clientCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                clientCategory.setName(crmLocalizer.localize(PdfLocalizationName.customerContacts));
                clientCategory.setDescription(crmLocalizer.localize(PdfLocalizationName.clientContact));
                clientCategory.setCategoryType(EdsContactCategory.CLIENT_CONTACT_CATEGORY);
                saveContactCategory(clientCategory, companyCreator, from);

                //Supplier category
                ContactCategoryListItem supplierCategory = new ContactCategoryListItem();
                supplierCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                supplierCategory.setName(crmLocalizer.localize(PdfLocalizationName.supplierContact));
                supplierCategory.setDescription(crmLocalizer.localize(PdfLocalizationName.supplierContact));
                supplierCategory.setCategoryType(EdsContactCategory.SUPPLIER_CONTACT_CATEGORY);
                saveContactCategory(supplierCategory, companyCreator, from);

                //Employee category
                ContactCategoryListItem employeeCategory = new ContactCategoryListItem();
                employeeCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                employeeCategory.setName(crmLocalizer.localize(PdfLocalizationName.employeeContact));
                employeeCategory.setDescription(crmLocalizer.localize(PdfLocalizationName.employeeContact));
                employeeCategory.setCategoryType(EdsContactCategory.EMPLOYEE_CONTACT_CATEGORY);
                saveContactCategory(employeeCategory, companyCreator, from);

                //Private category
                ContactCategoryListItem privateCategory = new ContactCategoryListItem();
                privateCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                privateCategory.setName(crmLocalizer.localize(PdfLocalizationName.privateContacts));
                privateCategory.setDescription(crmLocalizer.localize(PdfLocalizationName.myPrivateContacts));
                privateCategory.setCategoryType(EdsContactCategory.PRIVATE_CONTACT_CATEGORY);
                saveContactCategory(privateCategory, companyCreator, from);

                //Private category
                ContactCategoryListItem leadCategory = new ContactCategoryListItem();
                leadCategory.setType(EdsContactCategory.SYSTEM_BUILTIN);
                leadCategory.setName(crmLocalizer.localize(PdfLocalizationName.leadContacts));
                leadCategory.setDescription(crmLocalizer.localize(PdfLocalizationName.leadContacts));
                leadCategory.setCategoryType(EdsContactCategory.LEAD_CONTACT_CATEGORY);
                leadCategory.setDoNotShow(true);
                saveContactCategory(leadCategory, companyCreator, from);
            }
        }
    }*/

    ////CONTACT CATEGORY....

    /*@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<ContactCategoryListItem> getContactCategories() {
        ArrayList<ContactCategoryListItem> categoryListItems = crmContactManager.getContactCategories();
        int index = 0;
        for (ContactCategoryListItem item : categoryListItems) {
            String categoryName = item.getName().replace(" ", "_");
            String localizeCategoryName = commonLocalizer.localize(categoryName, item.getName());
            categoryListItems.get(index++).setName(localizeCategoryName);
        }
        return categoryListItems;
    }*/

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem getContactDataForImport(String leadOrCandidatePermissionCode) {
        ContactListItem result = new ContactListItem();
        ArrayList<ContactCategoryListItem> categories = contactCategoryManager.getContactCategories();
        if (categories != null && categories.size() > 0) {
            result.setCategories(ContactCategoryListItem.getAsTreeSelectItem(categories));
        }
        result.setLeadAssignees(crmServiceLocal.getOwnersListByPermission(leadOrCandidatePermissionCode));
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<ContactCategoryListItem> getGoogleContactGroups(String storageType) {
        if (storageType.equals(OFFICE_365)) {
            return getOffice365ContactCategoryListItems();
        } else {
            return getGoogleContactCategoryListItems();
        }
    }

    private ArrayList<ContactCategoryListItem> getOffice365ContactCategoryListItems() {

        ArrayList<ContactCategoryListItem> office365ContactFoldersList = new ArrayList<>();

        Office365AccessTokenDTO tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), OFFICE_365);

        if (tokenDTO != null) {
            Office365BaseList<Office365ContactFolder> office365ContactFolders = office365ContactService.getContactFolderCollection(tokenDTO);
            for (Office365ContactFolder entry : office365ContactFolders) {
                ContactCategoryListItem folderItem = new ContactCategoryListItem();
                folderItem.setObjectID((entry.getId().split("/")[entry.getId().split("/").length - 1].hashCode()));
                String folderDisplayName = entry.getDisplayName();
                folderItem.setName(folderDisplayName);
                folderItem.setDescription(entry.getId());
                office365ContactFoldersList.add(folderItem);
            }
            return office365ContactFoldersList;
        }
        return null;
    }

    private ArrayList<ContactCategoryListItem> getGoogleContactCategoryListItems() {
        ArrayList<ContactCategoryListItem> googleGroupsList = new ArrayList<>();
        EdsUser user = userManager.getUser();
        try {
            ContactsService myService = googleContactsManager.getLoggedService(user);
            if (myService != null) {
                List<ContactGroupEntry> contactGroups = googleContactsManager.getContactGroupFeed(myService).getEntries();
                for (ContactGroupEntry entry : contactGroups) {
                    ContactCategoryListItem gItem = new ContactCategoryListItem();
                    gItem.setObjectID((entry.getId().split("/")[entry.getId().split("/").length - 1].hashCode()));
                    String groupFullName = entry.getTitle().getPlainText();
                    if (entry.getSystemGroup() != null) {
                        gItem.setName(groupFullName.substring(groupFullName.indexOf(":") + 2));//remove prefix "System Group: " asked by Munir
                    } else {
                        gItem.setName(groupFullName);
                    }
                    gItem.setDescription(String.valueOf(((entry.getId().split("/")[entry.getId().split("/").length - 1].hashCode()))));
                    googleGroupsList.add(gItem);
                }
                return googleGroupsList;
            }


        } catch (GeneralSecurityException | ServiceException | IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Transactional
    public void saveGoogleGroupsSettings(String storageType, GoogleGroupsSetting[] settings) {
        EdsUser user = userManager.getUser();
        List<EdsGoogleWFTGroups> edsSettings = googleGroupsManager.getGroupSettings(user, storageType.equals(OFFICE_365));
        for (EdsGoogleWFTGroups setting : edsSettings) {
            setting.setDeleted(true);
            googleGroupsManager.update(setting);
        }

        for (GoogleGroupsSetting setting : settings) {
            EdsGoogleWFTGroups groupSetting = new EdsGoogleWFTGroups();
            groupSetting.setGoogleGroupID(setting.getGoogleGroupID());
            groupSetting.setWftGroupID(setting.getWftGroupID());
            groupSetting.setUser(user);
            groupSetting.setOfficeGroup(storageType.equals(OFFICE_365));
            googleGroupsManager.create(groupSetting);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<GoogleGroupsSetting> getUserSettings(String storageType) {
        EdsUser user = userManager.getUser();
        ArrayList<GoogleGroupsSetting> settings = new ArrayList<>();
        List<EdsGoogleWFTGroups> edsSettings = googleGroupsManager.getGroupSettings(user, storageType.equals(OFFICE_365));
        for (EdsGoogleWFTGroups setting : edsSettings) {
            GoogleGroupsSetting set = new GoogleGroupsSetting();
            set.setGoogleGroupID(setting.getGoogleGroupID());
            set.setId(setting.getObjectID());
            set.setUserID(setting.getUser().getObjectID());
            set.setWftGroupID(setting.getWftGroupID());
            set.setIsOffice365Group(storageType.equals(OFFICE_365));
            settings.add(set);
        }
        return settings;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean hasContactCategorySettings(String serverType) {
        EdsUser user = userManager.getUser();
        Long count = googleGroupsManager.getGroupSettingsCount(user, serverType.equals(OFFICE_365));
        return count > 0;
    }


    /**
     * Find how many candidates join to per vacancies
     *
     * @return list of CommonItem object
     */
    @Override
    public ArrayList<CommonItem> getCandidatePerVacancyChartData() {
        return (ArrayList<CommonItem>) vacancyManager.getCandidatePerVacancyChartData();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCandidatesAsSelectItem(ListLoadConfig config, ListingFilterParameter filterParametrs) {
        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }
        Integer companyID = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        List<SelectItem> result = new ArrayList<>();
        if (companyID != null) {
            int startat = 0;
            int limit = 500000;
            if (config != null && config.getLimit() > 0) {
                limit = config.getLimit();
            }

            List<EdsCrmContact> candidates = crmContactManager.getCompanyCandidates(userManager.getUser().getCompany().getObjectID(), startat, limit);
            for (EdsCrmContact candidate : candidates) {
                result.add(candidate.getAsSelectItem());
            }
        }
        return result.toArray(new SelectItem[]{});
    }

    @Override
    public ArrayList<SelectItem> getProjectVacancyItem(Integer objectID, Integer projectID) {
        return recruitmentService.getProjectVacancyItem(objectID, projectID);
    }

    private static final HashSet<String> countryCodes = new HashSet<>();

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem getKanbanLeadFromSolrDoc(ContactSolrDoc contactSolrDoc) {
        ContactListItem rpc = new ContactListItem();
        rpc.setObjectId(contactSolrDoc.getContactId());
        rpc.setKanbanorder(contactSolrDoc.getLeadKanbanOrder());
        rpc.setFirstName(contactSolrDoc.getFirstName());
        rpc.setLastName(contactSolrDoc.getLastName());
        rpc.setPrimaryEmail(contactSolrDoc.getPrimaryEmail());
        rpc.setPrimaryPhone(contactSolrDoc.getPrimaryPhone());
        rpc.setCreatedDate(contactSolrDoc.getCreationDate());
        if (contactSolrDoc.getAccountId() != null) {
            rpc.getCrmAccount().setObjectId(contactSolrDoc.getAccountId());
            rpc.getCrmAccount().setName(contactSolrDoc.getAccountName());
            rpc.getCrmAccount().setNumber(contactSolrDoc.getAccountNumber());
            rpc.getCrmAccount().setIndustry(contactSolrDoc.getAccountIndustry());
            rpc.getCrmAccount().setIndustryID(contactSolrDoc.getAccountIndustryId());
        }
        Integer assigneeId = contactSolrDoc.getAssigneeId();
        if (assigneeId != null && assigneeId > 0) {

            rpc.setLeadAssigneeID(assigneeId);
            rpc.setLeadAssignee(contactSolrDoc.getAssignee());
        }

        List<Integer> taskIDs = relationManager.getRelationIDsByType(rpc.getObjectId(), RelationItem.TYPE_LEAD, RelationItem.TYPE_TASK);
        if (taskIDs != null && !taskIDs.isEmpty()) {

            boolean hasLeadOverdueTasks = taskManager.getOverdueTasksByIDs(taskIDs).size() > 0;

            if (hasLeadOverdueTasks) {
                rpc.setStatus("OVERDUE");
            } else {
                boolean hasLeadTasks = taskManager.getTasksByIDs(taskIDs).size() > 0;
                rpc.setStatus(hasLeadTasks ? "AVAILABLE" : "NO_TASKS");
            }
        } else {
            rpc.setStatus("NO_TASKS");
        }

        return rpc;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem getKanbanLeadFromSolrDoc(SolrDocument doc) {
        ContactListItem rpc = new ContactListItem();
        rpc.setObjectId(SolrUtils.asInteger(doc, SolrContactRepresenter.FIELD_CONTACT_ID));
        rpc.setKanbanorder(SolrUtils.asLong(doc, SolrContactRepresenter.FIELD_LEAD_KANBAN_ORDER));
        rpc.setFirstName(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_FIRST_NAME));
        rpc.setLastName(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_LAST_NAME));
        rpc.setPrimaryEmail(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_PRIMARY_EMAIL, ""));
        rpc.setPrimaryPhone(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_PRIMARY_PHONE, "N/A"));
        rpc.setCreatedDate(SolrUtils.asDate(doc, SolrContactRepresenter.FIELD_CREATION_DATE));
        if (doc.getFieldValue(SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID) != null) {
            rpc.getCrmAccount().setObjectId(SolrUtils.asInteger(doc, SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID));
            rpc.getCrmAccount().setName(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_CRM_ACCOUNT_NAME));
            rpc.getCrmAccount().setNumber(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_CRM_ACCOUNT_NUMBER));
            rpc.getCrmAccount().setIndustry(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_CRM_ACCOUNT_INDUSTRY));
            rpc.getCrmAccount().setIndustryID(SolrUtils.asInteger(doc, SolrContactRepresenter.FIELD_CRM_ACCOUNT_INDUSTRY_ID));
        }
        Integer assigneeId = SolrUtils.asInteger(doc, SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID);
        if (assigneeId != null && assigneeId > 0) {

            rpc.setLeadAssigneeID(assigneeId);
            rpc.setLeadAssignee(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_LEAD_ASSIGNEE));
        }

        List<Integer> taskIDs = relationManager.getRelationIDsByType(rpc.getObjectId(), RelationItem.TYPE_LEAD, RelationItem.TYPE_TASK);
        if (taskIDs != null && !taskIDs.isEmpty()) {

            boolean hasLeadOverdueTasks = taskManager.getOverdueTasksByIDs(taskIDs).size() > 0;

            if (hasLeadOverdueTasks) {
                rpc.setStatus("OVERDUE");
            } else {
                boolean hasLeadTasks = taskManager.getTasksByIDs(taskIDs).size() > 0;
                rpc.setStatus(hasLeadTasks ? "AVAILABLE" : "NO_TASKS");
            }
        } else {
            rpc.setStatus("NO_TASKS");
        }

        return rpc;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer saveCompanyContactsToSpeadsheedFile(String fileName, Integer companyId, Sheet sh, Integer rowCount) {
        int startat = 0;
        List<EdsCrmContact> contacts = crmContactManager.getContactListByCompanyId(companyId, startat);
        EdsCompany company = companyManager.getCompany(companyId);
        if (company != null) {
            EdsUsagePlan plan = usagePlanManager.getCurrentUsagePlan(company);
            Map<String, String> map = new HashMap<>();
            map.put("company_name", company.getName());
            map.put("company_id", company.getObjectID().toString());
            map.put("subscription", plan.getStatus().getName());
            while (contacts.size() > 0) {
                try {
                    rowCount = addtoSheet(sh, rowCount, fileName, contacts, map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                companyManager.flushAndClear();
                contacts = crmContactManager.getContactListByCompanyId(companyId, contacts.get(contacts.size() - 1).getObjectID());
            }
        }
        return rowCount;
    }

    private Integer addtoSheet(Sheet sh, Integer rowCount, String fileName, List<EdsCrmContact> contacts, Map<String, String> additionalData) {
        Map<Integer, String> rolesMap = crmContactManager.getContactIdAndRolesMap(ServerUtils.getAsCommoDelimited(EdsObject.getObjectIDs(contacts), "0"));
        for (EdsCrmContact contact : contacts) {
            Row row = sh.createRow(++rowCount);
            int columnNumber = 0;
            row.getCell(columnNumber++).setCellValue(additionalData.get("subscription"));
            row.getCell(columnNumber++).setCellValue(additionalData.get("company_id"));
            row.getCell(columnNumber++).setCellValue(additionalData.get("company_name"));
            EdsAddress address = contact.getEdsPrimaryAddressFromAll();
            // Title
            row.getCell(columnNumber++).setCellValue(contact.getTitle());
            // First Name
            row.getCell(columnNumber++).setCellValue(contact.getFirstName());
            // Second Name
            row.getCell(columnNumber++).setCellValue(contact.getLastName());
            // E-mail- Primary
            row.getCell(columnNumber++).setCellValue(contact.getPrimaryEmail());
            // E-mail
            row.getCell(columnNumber++).setCellValue(contact.getEmails().size() > 0 ? contact.getEmails().get(0) : null);
            // Phone
            row.getCell(columnNumber++).setCellValue(contact.getPrimaryPhone());
            // Job Title
            row.getCell(columnNumber++).setCellValue(contact.getJobTitles());
            // Department
            row.getCell(columnNumber++).setCellValue(contact.getDepartment());
            // Type
            row.getCell(columnNumber++).setCellValue(contact.getHumanReadableContactType());
            // Country
            row.getCell(columnNumber++).setCellValue(address != null ? address.getCountryName() : "");
            // State
            row.getCell(columnNumber++).setCellValue(address != null ? address.getStateName() : "");
            // City
            row.getCell(columnNumber++).setCellValue(address != null ? address.getCity() : "");
            // Role
            if (contact.is(CrmConstants.TYPE_EMPLOYEE_CONTACT)) {
                row.getCell(columnNumber++).setCellValue(rolesMap.getOrDefault(contact.getObjectID(), ""));
            } else {
                row.getCell(columnNumber++).setCellValue("");
            }
        }
        return rowCount;
    }

    public ArrayList<PhoneTO> convertToPhoneTO(EdsCrmContact contact) {
        ArrayList<PhoneTO> phones = new ArrayList<>();

        if (contact != null) {

            List<EdsCrmContactItemParams> contactItemParams = contact.getItemParams(EdsCrmContactItemParams.PHONE);
            String primaryPhone = contact.getPrimaryPhone() != null ? contact.getPrimaryPhone() : "";
            if (contactItemParams != null) {
                for (EdsCrmContactItemParams itemParam : contactItemParams) {
                    if (StringUtils.isNotBlank(itemParam.getValue())) {
                        String phoneNumber = itemParam.getValue();
                        if (phoneNumber.toLowerCase().startsWith("+")) {
                            for (String phoneCode : getPhoneCountryCodes()) {
                                if (StringUtils.isNotBlank(phoneCode) && phoneNumber.startsWith(phoneCode)) {
                                    phoneNumber = phoneNumber.replace(phoneCode, "");
                                    if (StringUtils.isBlank(phoneNumber)) {
                                        break;
                                    }
                                    PhoneTO phone = new PhoneTO();
                                    phone.setPhone_number(phoneNumber);
                                    phone.setCountry_code(phoneCode);
                                    phone.setType(ContactParamEnum.getParamAsSelectItemTO(itemParam.getRelation()) != null ? ContactParamEnum.getParamAsSelectItemTO(itemParam.getRelation()).getName() : null);
                                    phone.addProperty("primary",primaryPhone.equals(phoneCode+phoneNumber));
                                    phones.add(phone);
                                    break;
                                }
                            }
                        } else {
                            PhoneTO phone = new PhoneTO();
                            phone.setPhone_number(phoneNumber);
                            phones.add(phone);
                        }
                    }
                }
            }
        }
        return phones;
    }

    public ArrayList<String> convertContactEmails(EdsCrmContact contact) {
        ArrayList<String> emails = new ArrayList<>();
        if (contact != null) {
            List<EdsCrmContactItemParams> contactItemParams = contact.getItemParams(EdsCrmContactItemParams.EMAIL);
            for (EdsCrmContactItemParams contactItemParam : contactItemParams) {
                if (StringUtils.isNotBlank(contactItemParam.getValue())) {
                    emails.add(contactItemParam.getValue());
                }
            }
        }
        return emails;
    }

  public ArrayList<EmailTO> convertContactEmailsWithPrimary(EdsCrmContact contact) {
        ArrayList<EmailTO> emails = new ArrayList<>();
        if (contact != null) {
            String primaryEmail= contact.getPrimaryEmail() != null ? contact.getPrimaryEmail() : "";
            List<EdsCrmContactItemParams> contactItemParams = contact.getItemParams(EdsCrmContactItemParams.EMAIL);
            for (EdsCrmContactItemParams contactItemParam : contactItemParams) {
                if (StringUtils.isNotBlank(contactItemParam.getValue())) {
                    EmailTO emailTO= new EmailTO();
                    emailTO.setEmail(contactItemParam.getValue());
                    emailTO.addProperty("primary",primaryEmail.equals(contactItemParam.getValue()));
                    emails.add(emailTO);
                }
            }
        }
        return emails;
    }

    public CrmAccountTO convertCompany(EdsCrmAccount company) {
        if (company != null) {
            CrmAccountTO crmAccount = new CrmAccountTO();
            crmAccount.setItem_id(company.getObjectID());
            crmAccount.setName(company.getName());
            if (company.getLogo() != null) {
                crmAccount.setAvatar_image(commonServiceLocal.getImageUrl(company.getLogo().getObjectID()));
            }
            return crmAccount;
        }
        return null;
    }

    @Override
    public ArrayList<EntityContactAddressTO> convertAddresses(EdsCrmContact contact) {
        //Get Address
        ArrayList<EntityContactAddressTO> entityAddresses = new ArrayList<>();
        if (contact != null) {
            List<EdsAddress> addresses = contact.getAddresses();
            if (addresses != null) {
                addresses.forEach(address -> {
                    EntityContactAddressTO entityAddress = new EntityContactAddressTO();
                    entityAddress.setItem_id(address.getObjectID());
                    if (ContactParamEnum.HOME.getId().equals(address.getRelationType())) {
                        entityAddress.setType(ContactParamEnum.HOME.getCode());
                    } else if (ContactParamEnum.WORK.getId().equals(address.getRelationType())) {
                        entityAddress.setType(ContactParamEnum.WORK.getCode());
                    } else {
                        entityAddress.setType(ContactParamEnum.OTHER.getCode());
                    }
                    if (StringUtils.isNotBlank(address.getName())) {
                        entityAddress.setName(address.getName());
                    }
                    if (StringUtils.isNotBlank(address.getAddress())) {
                        entityAddress.setLine_1(address.getAddress());
                    }
                    if (StringUtils.isNotBlank(address.getAddressb())) {
                        entityAddress.setLine_2(address.getAddressb());
                    }
                    if (StringUtils.isNotBlank(address.getCity())) {
                        entityAddress.setCity(address.getCity());
                    }
                    if (StringUtils.isNotBlank(address.getZipCode())) {
                        entityAddress.setPost_code(address.getZipCode());
                    }
                    entityAddress.setIs_primary(address.isPrimary());
                    if (address.getCountry() != null) {
                        CountriesListTO country = new CountriesListTO();
                        country.setId(address.getCountry().getObjectID());
                        country.setTitle(address.getCountry().getName());
                        country.setCountry_code(address.getCountry().getCode());
                        country.setHas_states(address.getCountry().getStates() != null && address.getCountry().getStates().size() > 0);

                        entityAddress.setCountry(country);
                    }
                    if (address.getState() != null) {
                        entityAddress.setState(new CategoryTO(address.getState().getObjectID(), address.getState().getName()));
                    }
                    entityAddresses.add(entityAddress);
                });
            }
        }
        return entityAddresses;
    }

    @Override
    public AddZapierContactTO convertToContact(EdsCrmContact contact) {
        if (contact != null) {
            AddZapierContactTO contactItem = new AddZapierContactTO();
                    /*if (StringUtils.isNotBlank(contactListItem.getName())) {
                        contactItem.setName(contactListItem.getName());
                    }*/
            if (StringUtils.isNotBlank(contact.getFirstName())) {
                contactItem.setFirst_name(contact.getFirstName());
            }
            if (StringUtils.isNotBlank(contact.getLastName())) {
                contactItem.setLast_name(contact.getLastName());
            }
            contactItem.setId(contact.getObjectID());
            //Phones
            contactItem.setPhone_number(contact.getPrimaryPhone());

            //Emails

            contactItem.setEmail(contact.getPrimaryEmail());
            //Company
            contactItem.setCompany(convertCompany(contact.getCrmAccount()));
            if (contact.getCrmAccount() != null) {
                contactItem.setCompany_name(contact.getCrmAccount().getName());
            }
            //Addresses
            HashMap<Integer, ContactAddressAddTO> entityAddresses = new HashMap<>();
            List<EdsAddress> addresses = contact.getAddresses();
            if (addresses != null) {
                addresses.forEach(address -> {
                    EntityContactAddressTO entityAddress = new EntityContactAddressTO();
                    entityAddress.setItem_id(address.getObjectID());
                    if (ContactParamEnum.HOME.getId().equals(address.getRelationType())) {
                        entityAddress.setType(ContactParamEnum.HOME.getCode());
                    } else if (ContactParamEnum.WORK.getId().equals(address.getRelationType())) {
                        entityAddress.setType(ContactParamEnum.WORK.getCode());
                    } else {
                        entityAddress.setType(ContactParamEnum.OTHER.getCode());
                    }
                    if (StringUtils.isNotBlank(address.getName())) {
                        entityAddress.setName(address.getName());
                    }
                    if (StringUtils.isNotBlank(address.getAddress())) {
                        entityAddress.setLine_1(address.getAddress());
                    }
                    if (StringUtils.isNotBlank(address.getAddressb())) {
                        entityAddress.setLine_2(address.getAddressb());
                    }
                    if (StringUtils.isNotBlank(address.getCity())) {
                        entityAddress.setCity(address.getCity());
                    }
                    if (StringUtils.isNotBlank(address.getZipCode())) {
                        entityAddress.setPost_code(address.getZipCode());
                    }
                    entityAddress.setIs_primary(address.isPrimary());
                    if (address.getCountry() != null) {
                        CountriesListTO country = new CountriesListTO();
                        country.setId(address.getCountry().getObjectID());
                        country.setTitle(/*"United States of America".equalsIgnoreCase(address.getCountry().getName()) ? "United States" :*/ address.getCountry().getName());
                        country.setCountry_code(address.getCountry().getCode());
                        country.setHas_states(address.getCountry().getStates() != null && !address.getCountry().getStates().isEmpty());

                        entityAddress.setCountry(country);
                    }
                    if (address.getState() != null) {
                        entityAddress.setState(new CategoryTO(address.getState().getObjectID(), address.getState().getName(), address.getState().getCode()));
                    }
                    entityAddresses.put(entityAddress.getItem_id(), entityAddress);
                });
            }

            contactItem.setAddresses((new ArrayList<>(entityAddresses.values())));
            return contactItem;
        }
        return null;
    }

    public com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO convertToContactTO(EdsCrmContact edsCrmContact) {
        com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO contactBaseInfo = new com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO();

        contactBaseInfo.setItem_id(edsCrmContact.getObjectID());
        contactBaseInfo.setName(edsCrmContact.getName());

        if (StringUtils.isNotBlank(edsCrmContact.getFirstName())) {
            contactBaseInfo.setFirst_name(edsCrmContact.getFirstName());
        }
        if (StringUtils.isNotBlank(edsCrmContact.getLastName())) {
            contactBaseInfo.setLast_name(edsCrmContact.getLastName());
        }

        if (StringUtils.isNotBlank(edsCrmContact.getJobTitles())) {
            contactBaseInfo.setJobTitle(edsCrmContact.getJobTitles());
        }
        if (StringUtils.isNotBlank(edsCrmContact.getTitle())) {
            contactBaseInfo.setTitleName(edsCrmContact.getTitle());
        }


        if (edsCrmContact.getPhoto() != null) {
            contactBaseInfo.setAvatar_image(commonServiceLocal.getImageUrl(edsCrmContact.getPhoto().getObjectID()));
        }
        ContactsTO contactsTO = new ContactsTO();

        //Base Info Phones
        contactsTO.setPhones(convertToPhoneTO(edsCrmContact));

        //Base Info Emails
        ArrayList<String> emails = convertContactEmails(edsCrmContact);
        contactsTO.setEmails(emails);

        ArrayList<EmailTO> emailTo = convertContactEmailsWithPrimary(edsCrmContact);
        contactsTO.setEmailTo(emailTo);

        contactBaseInfo.setContacts(contactsTO);

        //Contact's Company
        contactBaseInfo.setCompany(convertCompany(edsCrmContact.getCrmAccount()));
        //Addresses
        contactBaseInfo.setEntityAddresses(convertAddresses(edsCrmContact));

        return contactBaseInfo;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem getKanbanCandidateFromSolrDoc(SolrDocument doc, EdsUser user) {
        ContactListItem rpc = new ContactListItem();

        rpc.setObjectId(SolrUtils.asInteger(doc, SolrContactRepresenter.FIELD_CONTACT_ID));
        rpc.setKanbanorder(SolrUtils.asLong(doc, SolrContactRepresenter.FIELD_CANDIDATE_KANBAN_ORDER));
        rpc.setFirstName(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_FIRST_NAME));
        rpc.setLastName(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_LAST_NAME));
        rpc.setPrimaryEmail(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_PRIMARY_EMAIL, ""));
        rpc.setPrimaryPhone(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_PRIMARY_PHONE, "N/A"));
        rpc.setCreatedDate(SolrUtils.asDate(doc, SolrContactRepresenter.FIELD_CREATION_DATE));
        rpc.setPosition(SolrUtils.asString(doc,SolrContactRepresenter.FIELD_CANDIDATE_POSITION));
        rpc.setDepartment(SolrUtils.asString(doc,SolrContactRepresenter.FIELD_CANDIDATE_DEPARTMENT));
        rpc.setCandidateLocation(SolrUtils.asString(doc,SolrContactRepresenter.FIELD_PREFERRED_LOCATION));
        String statusCode = SolrUtils.asString(doc, SolrContactRepresenter.FIELD_LEAD_STATUS_CODE);
        EdsReference status = referenceManager.getByCode(statusCode);
        ReferenceItem statusItem = status.getRPC();
        statusItem.setAllowEdit(canEdit(status));
        statusItem.setDraggable(isDragable(status));
        rpc.setCandidateStatus(statusItem);
        rpc.setDraggable(isDragable(status));

        if (doc.getFieldValue(SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID) != null) {
            rpc.getCrmAccount().setObjectId(SolrUtils.asInteger(doc, SolrContactRepresenter.FIELD_CRM_ACCOUNT_ID));
            rpc.getCrmAccount().setName(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_CRM_ACCOUNT_NAME));
            rpc.getCrmAccount().setNumber(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_CRM_ACCOUNT_NUMBER));
            rpc.getCrmAccount().setIndustry(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_CRM_ACCOUNT_INDUSTRY));
            rpc.getCrmAccount().setIndustryID(SolrUtils.asInteger(doc, SolrContactRepresenter.FIELD_CRM_ACCOUNT_INDUSTRY_ID));
        }

        if (SolrUtils.asListInteger(doc, SolrContactRepresenter.FIELD_VACANCY_ID) != null) {
            ArrayList<SelectItem> vacancies = new ArrayList<>();
            Collections.addAll(vacancies, ServerUtils.asListToSelectItem(SolrUtils.asListInteger(doc, SolrContactRepresenter.FIELD_VACANCY_ID), SolrUtils.asListString(doc, SolrContactRepresenter.FIELD_VACANCY_NAME)));
            rpc.setVacancies(vacancies);
        }

        Integer assigneeId = SolrUtils.asInteger(doc, SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID);
        if (assigneeId != null && assigneeId > 0) {

            rpc.setLeadAssigneeID(assigneeId);
            rpc.setLeadAssignee(SolrUtils.asString(doc, SolrContactRepresenter.FIELD_LEAD_ASSIGNEE));
        }

        List<Integer> taskIDs = relationManager.getRelationIDsByType(rpc.getObjectId(), RelationItem.TYPE_CANDIDATE, RelationItem.TYPE_TASK);
        if (taskIDs != null && !taskIDs.isEmpty()) {

            boolean hasLeadOverdueTasks = taskManager.getOverdueTasksByIDs(taskIDs).size() > 0;

            if (hasLeadOverdueTasks) {
                rpc.setStatus("OVERDUE");
            } else {
                boolean hasLeadTasks = taskManager.getTasksByIDs(taskIDs).size() > 0;
                rpc.setStatus(hasLeadTasks ? "AVAILABLE" : "NO_TASKS");
            }
        } else {
            rpc.setStatus("NO_TASKS");
        }

        return rpc;
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

    protected HashSet<String> getPhoneCountryCodes() {
        if (countryCodes.isEmpty()) {
            List<EdsCountry> countryList = countryManager.list(new ListingFilterParameter());
            countryList.forEach(edsCountry -> countryCodes.add(edsCountry.getTelCode()));
            return countryCodes;
        }
        return countryCodes;
    }

    public EdsCandidateItemTableCF saveCustomTableFields(EdsCandidateItemTableCF customfField, List<CompanyCustomFieldItem> customFieldItems) {
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
                customfField = new EdsCandidateItemTableCF();
                candidateItemTableCFManager.create(customfField);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(customfField, customFieldItems);
            return customfField;
        }
        return null;
    }

    @Override
    public void deleteLead(Integer id) {
        Integer inactiveID = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_INACTIVE).getObjectID();
        crmContactManager.deleteContact(id, inactiveID);
    }
}
