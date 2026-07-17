package com.edatasite.workforce.gwt.client.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.log.KpiEntityType;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.crm.contact.EdsContactHistory;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.settings.EdsListPanelSettings;
import com.edatasite.workforce.core.solr.component.ContactSolrComponent;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.core.solr.document.CrmAccountSolrDoc;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelBBItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelPPItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.client.client.rpc.*;
import com.edatasite.workforce.gwt.client.client.rpc.supplier.SupplierList;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrClientRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCrmAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSupplierRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.*;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SubsidiariesCompanyManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.SolrTransactionManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ClientContactEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ClientEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.SolrEvent;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.CrmContactCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.*;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.invoice.client.rpc.MessageItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.TypeItem;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.BankDetailsDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.CustomerSupplierDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA. User: mansur Date: Jan 8, 2008 Time: 6:13:41 PM To
 * change this template use File | Settings | File Templates.
 */

@Transactional
@Service("clientService")
public class ClientServiceImpl implements ClientService, ClientServiceLocal, CustomerSupplierApiService, Constants, AccountingConstants {

    public static final String NA = "N/A";
    public static final BigDecimal ZERO = new BigDecimal("0.00");
    private static final Map<String, ComparatorFactory<EdsCrmAccount>> comparatorFactories = new HashMap<>();
    private static final Map<String, ComparatorFactory<EdsCrmAccount>> comparatorFactoriesSupplier = new HashMap<>();
    private static final Map<String, ComparatorFactory<CrmAccountItem>> comparatorFactoriesSupplierItem = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

    static {
        comparatorFactories.put(CODE_COLUMN, sortOrder -> new AbstractComparator<EdsCrmAccount>() {
            public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                return internalCompare(o1.getNumber(), o2.getNumber(), sortOrder);
            }
        });

        comparatorFactories.put(NAME_COLUMN, sortOrder -> new AbstractComparator<EdsCrmAccount>() {
            public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                return internalCompare(o1.getName(), o2.getName(), sortOrder);
            }
        });

        comparatorFactories.put(DESCRIPTION_COLUMN, sortOrder -> new AbstractComparator<EdsCrmAccount>() {
            public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                return internalCompare(o1.getDescription() != null ? o1.getDescription() : "",
                        o2.getDescription() != null ? o2.getDescription() : "", sortOrder);
            }
        });

        comparatorFactories.put(ADDRESS_COLUMN,
                sortOrder -> new AbstractComparator<EdsCrmAccount>() {
                    public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                        String ba1 = o1.getBillingAddress().getAddress();
                        String ba2 = o2.getBillingAddress().getAddress();
                        return internalCompare(ba1 != null ? ba1 : NA, ba2 != null
                                ? ba2
                                : NA, sortOrder);
                    }
                });

        comparatorFactories.put(COUNTRY_COLUMN,
                sortOrder -> new AbstractComparator<EdsCrmAccount>() {
                    public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                        EdsCountry bc1 = o1.getBillingAddress().getCountry();
                        EdsCountry bc2 = o2.getBillingAddress().getCountry();
                        return internalCompare(bc1 != null ? bc1.getName() : NA,
                                bc2 != null ? bc2.getName() : NA, sortOrder);
                    }
                });
        comparatorFactories.put(STATE_COLUMN,
                sortOrder -> new AbstractComparator<EdsCrmAccount>() {
                    public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                        EdsRegion br1 = o1.getBillingAddress().getState();
                        EdsRegion br2 = o2.getBillingAddress().getState();
                        return internalCompare(br1 != null ? br1.getName() : NA,
                                br2 != null ? br2.getName() : NA, sortOrder);
                    }
                });
        comparatorFactories.put(CONTACT_NAME_COLUMN,
                sortOrder -> new AbstractComparator<EdsCrmAccount>() {
                    public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
        comparatorFactories.put(CONTACT_PHONE_NUMBER_COLUMN,
                sortOrder -> new AbstractComparator<EdsCrmAccount>() {
                    public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
    }

    static {
        comparatorFactoriesSupplier.put(CODE_COLUMN, sortOrder -> new AbstractComparator<EdsCrmAccount>() {
            public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                return internalCompare(o1.getNumber(), o2.getNumber(), sortOrder);
            }
        });
        comparatorFactoriesSupplier.put(NAME_COLUMN, sortOrder -> new AbstractComparator<EdsCrmAccount>() {
            public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                return internalCompare(o1.getName(), o2.getName(), sortOrder);
            }
        });
        comparatorFactoriesSupplier.put(ADDRESS_COLUMN, sortOrder -> new AbstractComparator<EdsCrmAccount>() {
            public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                String ba1 = o1.getBillingAddress().getAddress();
                String ba2 = o2.getBillingAddress().getAddress();
                return internalCompare(ba1 != null ? ba1 : "", ba2 != null ? ba2 : "", sortOrder);
            }
        });

        comparatorFactoriesSupplier.put(COUNTRY_COLUMN, sortOrder -> new AbstractComparator<EdsCrmAccount>() {
            public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                EdsCountry bc1 = o1.getBillingAddress().getCountry();
                EdsCountry bc2 = o2.getBillingAddress().getCountry();
                return internalCompare(bc1 != null ? bc1.getName() : "", bc2 != null ? bc2.getName() : "", sortOrder);
            }
        });
        comparatorFactoriesSupplier.put(CONTACT_NAME_COLUMN, sortOrder -> new AbstractComparator<EdsCrmAccount>() {
            public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                EdsCrmContact c1 = o1.getPrimaryContact();
                EdsCrmContact c2 = o2.getPrimaryContact();
                String cName1 = (c1 != null ? c1.getName() : "");
                String cName2 = (c2 != null ? c2.getName() : "");
                return internalCompare(cName1, cName2, sortOrder);
            }
        });
        comparatorFactoriesSupplier.put(CONTACT_PHONE_NUMBER_COLUMN, sortOrder -> new AbstractComparator<EdsCrmAccount>() {
            public int compare(EdsCrmAccount o1, EdsCrmAccount o2) {
                EdsCrmContact c1 = o1.getPrimaryContact();
                EdsCrmContact c2 = o2.getPrimaryContact();
                String pn1 = (c1 != null ? c1.getPrimaryPhone() : "");
                String pn2 = (c2 != null ? c2.getPrimaryPhone() : "");
                return internalCompare(pn1, pn2, sortOrder);
            }
        });
    }

    static {
        comparatorFactoriesSupplierItem.put(CrmAccountItem.SUPPLIER_BALANCE,
                sortOrder -> new AbstractComparator<CrmAccountItem>() {
                    public int compare(CrmAccountItem o1, CrmAccountItem o2) {
                        return internalCompare(o1.getSupplierBalance(), o2.getSupplierBalance(), sortOrder);
                    }
                });
    }

    @Autowired
    private ClientManager clientManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private ClientContactManager clientContactManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private NoteHistoryManager noteHistoryManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private CrmContactItemParamsManager contactItemParamsManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ContactHistoryManager contactHistoryManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private SolrTransactionManager solrTransactionManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private FormPropertyManager formPropertyManager;
    @Autowired
    private CRMService crmService;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ListPanelSettingsManager listPanelSettingsManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private InvoiceTermsManager invoiceTermsManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private SubsidiariesCompanyManager subsidiariesCompanyManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private PriceLevelManager priceLevelManager;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    @Qualifier("countryLocalizer")
    private WfmMessageSource countryLocalizer;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;
    @Autowired
    private ContactSolrComponent contactSolrComponent;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CrmAccountItem editAccount(Integer objectId, String type) {
        return crmService.editAccount(objectId, type);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewClientList getNewClients(ListingFilterParameter lfp) {
        if (lfp == null) {
            lfp = new ListingFilterParameter();
        }
        ListingFilterParameter fp = lfp;
        EdsListPanelSettings panelSettings = listPanelSettingsManager.getUserListPanelSettings(ListPanelType.ClientListPanel.name(), null);
        ListPanelToolRpc panelTools;
        if (panelSettings != null) {
            panelTools = WfmJsonUtils.jsonDataConvertToListPanelToolsRpc(panelSettings.getSettingsJSONData());
            fp.setColumnsOfListing(panelTools.getColumnCodeName());
        } else {
            ArrayList<String> columnCodeName = CrmAccountItem.defaultClientColumnNames;
            panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(columnCodeName);
            fp.setColumnsOfListing(columnCodeName);
        }
        if (panelTools.isCustomFieldsShown()) {
            fp.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.CrmAccount));
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmAccount.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityType(KpiEntityType.CLIENT);
        ServerUtils.kpiLog(log, kpiLog, "Get clients list");
        return getClients(fp, lfp.asConfig());
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewClientList getClients(ListingFilterParameter filterParametrs, ListLoadConfig config) {
        Page<CrmAccountSolrDoc> crmAccountSolrDocPage = crmAccountSolrComponent.getClientList(filterParametrs, config);
        return getClientFromSolrResult(crmAccountSolrDocPage, filterParametrs);
    }

    @Override
    public ListResultTO<CustomerSupplierDto> getCustomerList(ListingFilterParameter fp) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CRM_ACCOUNT_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getSolrQueryForClient(fp, fp.asConfig()), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getClientFromSolrResult(resp);
    }

    @Override
    public CustomerSupplierDto getCustomerById(Integer objectId) {
        EdsCrmAccount customer = crmAccountManager.get(objectId);

        if (customer != null) {
            return wrapToCustomerSupplierDto(customer, true);
        }
        return null;
    }


    private SolrQuery getSolrQueryForClient(ListingFilterParameter fp, ListLoadConfig config) {
        FacetFilterRpc clientFacetFilter = fp.getFacetFilter();
        if (clientFacetFilter != null && !clientFacetFilter.isFilterChanges()) {
            clientFacetFilter = commonServiceLocal.getUserFacetFilter(clientFacetFilter);
        }
        EdsUser edsUser = crmContactManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        EdsReference customer = referenceManager.findReferenceForCrmAccount(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER);
        StringBuilder solrQuery = new StringBuilder();

        List<Integer> customerIDList = null;
        if (!ServerUtils.hasPermission(fp.isPM() ? PermissionConstants.PM_SEE_ALL_CUSTOMERS_LIST : PermissionConstants.ACCOUNTING_SEE_ALL_CUSTOMERS_LIST) && roleManager.hasRole(edsUser, PM) && !roleManager.hasEitherRoles(edsUser, ACCOUNTANT, DR, ADMIN)) {
            customerIDList = projectManager.getCustomerIDsByProjectManager(edsUser);
        } else if (roleManager.hasOnlyRoles(edsUser, CLIENT) && edsUser instanceof EdsClientContact) {
            if (((EdsClientContact) edsUser).getClientID() != null) {
                customerIDList = new ArrayList<>();
                customerIDList.add(((EdsClientContact) edsUser).getClientID());
                fp.setClientId(((EdsClientContact) edsUser).getClientID());
            }
        }
        solrQuery.append(QueryBuilderForSolr.getClientListSolrQuery(fp, clientFacetFilter, edsCompany, customer, customerIDList, edsUser, FacetContentType.ClientFacetFilter.getContentCode()[9]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(clientFacetFilter, edsCompany, SolrCrmAccountRepresenter.FIELD_CREATED_DATE, null, FacetContentType.ClientFacetFilter.getContentCode()[9]));

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(Math.max(config.getStart(), 0));
        query.setParam(CommonParams.ROWS, String.valueOf(config.getLimit()));

        if (!fp.isSearchButton()) {
            if (config.getSortField() != null && !"".equals(config.getSortField())) {
                boolean desc = false;
                if (Constants.DESC == config.getSortDir()) {
                    desc = true;
                }
                String solrSortField = SolrClientRepresenter.getSortingField(config.getSortField());
                if (solrSortField != null) {
                    query.setSort(solrSortField, getSolrOrder(desc));
                } else {
                    CustomFieldsUtils.setCustomFieldsSortableNameToSolr(config.getSortField(), desc, query, true);
                }
            } else {
                query.setSort(SolrClientRepresenter.FIELD_LAST_UPDATED_DATE, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

    /**
     * <h1>... This is method generated Supplier Solr Query ...</h1>
     * <br/>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last Updated {13:39 11/06/2011} ...</h3>
     *
     * @param fp
     * @param config
     * @return
     */
    private SolrQuery getSolrQueryForSupplier(ListingFilterParameter fp, ListLoadConfig config) {
        FacetFilterRpc supplierFilter = fp.getFacetFilter();
        if (supplierFilter != null && !supplierFilter.isFilterChanges()) {// first request get User default Filter tasks
            supplierFilter = commonServiceLocal.getUserFacetFilter(supplierFilter);
        }
        EdsUser edsUser = crmContactManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        EdsReference edsSupplier = referenceManager.findReferenceForCrmAccount(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER);
        StringBuilder solrQuery = new StringBuilder();

        String[] customFullAccessRoles = rolePermissionManager.getRolesByPermissionCode(LayoutRPC.LOGISTICS_SECTION.equals(fp.getModule())
                ? PermissionConstants.LOGISTICS_SUPPLIER_FULL_LIST_ACCESS
                : PermissionConstants.ACCOUNTING_SUPPLIER_FULL_LIST_ACCESS).toArray(new String[]{});
        solrQuery.append(QueryBuilderForSolr.getSupplierListSolrQuery(fp, supplierFilter, edsCompany, edsSupplier, edsUser, customFullAccessRoles, FacetContentType.SupplierFacetFilter.getContentCode()[8]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(supplierFilter, edsCompany, SolrCrmAccountRepresenter.FIELD_CREATED_DATE, null, FacetContentType.SupplierFacetFilter.getContentCode()[8]));

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery.toString());
        query.setStart(config.getStart());
        query.setParam(CommonParams.ROWS, String.valueOf(config.getLimit()));

        if (!fp.isSearchButton()) {
            if (config.getSortField() != null && !"".equals(config.getSortField())) {
                boolean desc = false;
                if (Constants.DESC == config.getSortDir()) {
                    desc = true;
                }
                String solrSortField = SolrClientRepresenter.getSortingField(config.getSortField());
                if (solrSortField != null) {
                    query.setSort(solrSortField, getSolrOrder(desc));
                } else {
                    CustomFieldsUtils.setCustomFieldsSortableNameToSolr(config.getSortField(), desc, query, true);
                }
            } else {
                query.setSort(SolrSupplierRepresenter.FIELD_LAST_UPDATED_DATE, SolrQuery.ORDER.desc);
            }
        }
        return query;
    }

    private NewClientList getClientFromSolrResult(Page<CrmAccountSolrDoc> crmAccountSolrDocPage, ListingFilterParameter fp) {
        ArrayList<CrmAccountItem> crmAccountItems = new ArrayList<>();
        int totalCount = 0;
        if (crmAccountSolrDocPage != null && crmAccountSolrDocPage.getContent() != null) {
            ListPanelToolRpc panelSettings = fp.getListPanelTool();
            totalCount = (int) crmAccountSolrDocPage.getTotalElements();
            List<CrmAccountSolrDoc> crmAccountSolrDocs = crmAccountSolrComponent.getDocumentsExistingInBase(crmAccountSolrDocPage.getContent());
            ArrayList<CompanyCustomFieldItem> cfResultForFiltering = commonServiceLocal.getCompanyCustomFieldsForFiltering(ViewName.CrmAccount);
            List<SelectItem> typesSelectItemMap = new ArrayList<>();
            List<EdsReference> types = referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE);
            for (EdsReference type : types) {
                String value = referenceWfmMessageSource.localize(type.getCode(), type.getName());
                typesSelectItemMap.add(new SelectItem(type.getObjectID(), value, type.getCode()));
            }
            Map<Integer, BigDecimal> balances = new HashMap<>();
            if (!crmAccountSolrDocs.isEmpty()) {
                String ids = crmAccountSolrDocs.stream().map(doc -> String.valueOf(doc.getCrmAccountId())).collect(Collectors.joining(","));
                balances = crmAccountManager.getClientBalanceByCustomerIds(ids);
            }
            Map<Integer, Date> crmAccountInvoiceExpireDate;
            if (panelSettings.getColumnCodeName().contains(CrmAccountItem.INVOICE_EXPIRE_DATE) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.SHOW_CRM_ACCOUNT_INVOICE_DETAILS)) {
                List<CrmAccountInvoiceTO> prioritizedInvoices = invoiceManager.getPrioritizedInvoices(crmAccountSolrDocs.stream().map(CrmAccountSolrDoc::getCrmAccountId).collect(Collectors.toList()));
                crmAccountInvoiceExpireDate = prioritizedInvoices.stream().filter(e -> e.getInvoiceExpireDate() != null).collect(Collectors.toMap(CrmAccountInvoiceTO::getCrmAccountId, CrmAccountInvoiceTO::getInvoiceExpireDate));
            } else {
                crmAccountInvoiceExpireDate = new HashMap<>();
            }
            for (CrmAccountSolrDoc doc : crmAccountSolrDocs) {
                CrmAccountItem item = new CrmAccountItem();
                item.setInvoiceExpireDate(new DateNonConvertable(crmAccountInvoiceExpireDate.get(doc.getCrmAccountId())));
                item.setInvoicePaidStatus(crmServiceLocal.invoicePaidStatus(item.getInvoiceExpireDate().getNonConvertedDate()));
                if (fp == null) {
                    fp = new ListingFilterParameter();
                }
                if (doc != null) {
                    boolean iDsOnly = fp.isIDsOnly();
                    boolean asSelectItem = fp.isAsSelectItem();
                    Integer id = doc.getCrmAccountId();
                    item.setObjectId(id);
                    if (asSelectItem || iDsOnly) {
                        if (!iDsOnly) {
                            item.setName(doc.getCrmAccountName());
                            item.setNumber(doc.getCrmAccountNumber());
                            if (doc.getOwnerName() != null) {
                                item.setOwnerNames(ServerUtils.collectionToCommaDelimitedString(doc.getOwnerName()));
                            }
                        }
                    } else {

                        if (doc.getOwnerName() != null) {
                            item.setOwnerNames(ServerUtils.collectionToCommaDelimitedString(doc.getOwnerName()));
                        }
                        Integer parentID = doc.getCrmAccountParentId();
                        if (parentID != null) {
                            CrmAccountItem parent = new CrmAccountItem();
                            parent.setObjectId(parentID);
                            parent.setName(doc.getCrmAccountParentName());
                            item.setParent(parent);
                        }
                        item.setName(doc.getCrmAccountName());
                        item.setNumber(doc.getCrmAccountNumber());
                        if (typesSelectItemMap.size() > 0) {
                            item.setAccountTypes(typesSelectItemMap.toArray(new SelectItem[]{}));
                            if (doc.getTypeIds() != null) {
                                if (doc.getTypeIds() instanceof ArrayList) {
                                    SelectItem.setSelected(item.getAccountTypes(), false, ((ArrayList<Integer>) doc.getTypeIds()).toArray(new Integer[]{}));
                                }
                            }
                        }
                        item.setBlocked(doc.getBlocked());
                        item.setIndustry(referenceWfmMessageSource.localize(doc.getIndustryCode(), doc.getIndustryName()));
                        item.setIndustryID(doc.getIndustryId());
                        item.setEmail(doc.getEmail());
                        item.setPhone(doc.getPhone());
                        item.setFax(doc.getFax());
                        item.setWebsite(doc.getWebsite());
                        item.setTermName(doc.getTermName());

                        Address billAddress = new Address();
                        billAddress.setPrimary(true);
                        billAddress.setObjectID(doc.getAdress1Id());
                        billAddress.setAddress(doc.getStreet());
                        billAddress.setAddressb(doc.getStreetb());
                        billAddress.setCity(doc.getCity());
                        billAddress.setCountryId(doc.getCountryId());
                        billAddress.setCountry(countryLocalizer.localize(doc.getCountryCode(), doc.getCountryName()));
                        billAddress.setState(doc.getStateName());
                        billAddress.setStateId(doc.getStateId());
                        billAddress.setZipCode(doc.getPostCode());
                        item.setBillAddresses(new Address[]{billAddress});

                        Address mailAddress = new Address();
                        mailAddress.setPrimary(true);
                        mailAddress.setObjectID(doc.getAdress2Id());
                        mailAddress.setAddress(doc.getStreet2());
                        mailAddress.setAddressb(doc.getStreet2b());
                        mailAddress.setCity(doc.getCity2());
                        mailAddress.setCountryId(doc.getCountryId2());
                        mailAddress.setCountry(countryLocalizer.localize(doc.getCountryCode2(), doc.getCountryName2()));
                        mailAddress.setState(doc.getStateName2());
                        mailAddress.setStateId(doc.getStateId2());
                        mailAddress.setZipCode(doc.getPostCode2());
                        item.setMailAddresses(new Address[]{mailAddress});

                        item.setCurrency(doc.getCurrencyName());
                        item.setCurrencyId(doc.getCurrencyId());
                        item.setVatNumber(doc.getVatNumber());
                        item.setTrn(doc.getTrnNumber());
                        item.setRegistrationNumber(doc.getRegistrationNumber());
                        item.setPaymentMethod(commonLocalizer.localize(doc.getPaymentMethodCode(), doc.getPaymentMethodName()));
                        item.setPaymentMethodId(doc.getPaymentMethodId());
                        item.setLastUpdatedDate(doc.getLastUpdateDate());
                        item.setCreatedDate(doc.getCreationDate());
                        item.setBankName(doc.getBankName());
                        item.setTaxName(doc.getTaxName());
                        item.setInTarget(doc.getInTarget());
                        item.setCreditLimit(BigDecimal.valueOf(doc.getCreditLimit()));
                        Date balanceDate = doc.getBalanceDate();
                        item.setBalanceDate(balanceDate != null ? new DateNonConvertable(balanceDate) : null);
                        item.setSaasuGUID(doc.getSaasuGuid());
                        item.setSaasuLastUpdatedDate(doc.getSaasuUpdatedDate());
                        item.setSaasuLastUpdatedUid(doc.getSaasuUpdatedUid());
                        if (doc.getContactId() != null) {
                            ContactListItem contact = new ContactListItem();
                            contact.setObjectId(doc.getContactId());
                            contact.setContactName(doc.getContactName());
                            contact.setPrimaryEmail(doc.getContactName());
                            item.setPrimaryContact(contact);
                        }

                        if (fp.isCustomFieldsShown() && fp.getListPanelTool() != null) {
                            item.setCustomFieldsMap(CustomFieldsUtils.getBaseSolrDocDynamicFields(doc, fp.getListPanelTool().getColumnCodeName()));
                        }
                        if (cfResultForFiltering != null && !cfResultForFiltering.isEmpty()) {
                            item.setCustomFieldsForFiltering(cfResultForFiltering);
                        }
                    }
                    item.setClientBalance((balances.get(id) != null ? balances.get(id) : BigDecimal.ZERO).doubleValue());
                    item.setSalesType(doc.getSalesTypeName());
                    item.setSalesTypeId(doc.getSalesTypeId());
                }

                crmAccountItems.add(item);
            }
            //sorting by client balance
            if (fp.getSortField() != null && CrmAccountItem.CLIENT_BALANCE.equals(fp.getSortField())) {
                if (fp.getSortDir() == 1) {
                    crmAccountItems.sort(Comparator.comparing(CrmAccountItem::getClientBalance).reversed());
                } else {
                    crmAccountItems.sort(Comparator.comparing(CrmAccountItem::getClientBalance));
                }
            }
        }
        return new NewClientList(crmAccountItems, totalCount);
    }

    private ListResultTO<CustomerSupplierDto> getClientFromSolrResult(QueryResponse resp) {
        int totalCount = (int) resp.getResults().getNumFound();
        List<Integer> Ids = resp.getResults().stream().map(doc -> SolrUtils.asInteger(doc, SolrClientRepresenter.FIELD_CRM_ACCOUNT_ID)).collect(Collectors.toList());
        List<EdsCrmAccount> crmAccountList = crmAccountManager.getCrmAccountsByIDs(Ids);
        ArrayList<CustomerSupplierDto> items = new ArrayList<>();
        crmAccountList
                .forEach(crmAccount -> items.add(wrapToCustomerSupplierDto(crmAccount, CustomFieldsUtils.setRPCCustomFieldItems(null, commonServiceLocal.getCompanyCustomFields(ViewName.CrmAccount)), true)));
        return new ListResultTO<>(totalCount, items);
    }

    public List<SolrDocument> getDocumentsExistingInBase(String core, SolrDocumentList results, String fieldObjectID, boolean isSupplier) {
        List<SolrDocument> documents = new ArrayList<>();
        Map<Integer, SolrDocument> mapDocuments = new HashMap<>();
        if (results != null && results.size() > 0) {
            for (SolrDocument doc : results) {
                documents.add(doc);
                mapDocuments.put((Integer) doc.getFieldValue(fieldObjectID), doc);
            }
        }
        List<Integer> objectIDsFromDatabase = null;
        if (SOLR_CRM_ACCOUNT_CORE.equals(core)) {
            objectIDsFromDatabase = crmAccountManager.getCrmAccountIDsByIDs(new ArrayList<>(mapDocuments.keySet()));
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

    private SolrQuery.ORDER getSolrOrder(boolean desc) {
        return desc ? SolrQuery.ORDER.desc : SolrQuery.ORDER.asc;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CrmAccountItem getClient(Integer objectID) {
        EdsCrmAccount client = clientManager.get(objectID);
        if (client != null) {
            CrmAccountItem clientItem = client.getRPC(null, false);

            List<EdsAddress> billAddrList = addressManager.getAddressesByEntityIdAndType(objectID, EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            List<EdsAddress> mailAddrList = addressManager.getAddressesByEntityIdAndType(objectID, EdsAddress.MAILING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            clientItem.setBillAddresses(client.getAddressData(billAddrList, client.getBillingAddress()));
            clientItem.setMailAddresses(client.getAddressData(mailAddrList, client.getMailingAddress()));

            clientItem.setEncryptedID(EncryptionHelper.encryptURL("client/" + client.getObjectID().toString()));

            //recalculate client/supplier balance
//            if (!client.getBalanceCalculated()) {
            clientItem.setClientBalance(crmAccountManager.getClientBalance(client.getObjectID()).doubleValue());
//
//                client.setBalanceCalculated(true);
//                crmAccountManager.update(client);
//            }
            clientItem.setCustomFields((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(client.getCustomFields(),
                    commonService.getCompanyCustomFields(ViewName.CrmAccount)));

            LinkedHashMap<String, FormProperty> fields = new LinkedHashMap<>();
            EdsFormProperty edsFormProperty = formPropertyManager.getByFormID(LayoutRPC.CLIENT_FORM);
            if (edsFormProperty != null) {
                Gson gson = new Gson();
                FormProperty[] formFields = gson.fromJson(edsFormProperty.getSettingsJSONData(), FormProperty[].class);
                for (FormProperty formProperty : formFields) {
                    if (formProperty != null) {
                        if (formProperty.getDefaultValue() != null && formProperty.getDefaultValue().length() == 0) {
                            formProperty.setDefaultValue(null);
                        }
                        if (formProperty.getRoleEdit() != null && formProperty.getRoleEdit().size() > 0) {
                            if (userManager.getUser().hasEitherRoles(formProperty.getRoleEdit().toArray(new Integer[]{}))) {
                                formProperty.setDisabled(false);
                            }
                        }
                        fields.put(formProperty.getCode(), formProperty);
                    }
                }
            }
            clientItem.setFormProperty(fields);
            return clientItem;
        } else {
            return null;
        }
    }

//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public CrmAccountItem getClientContactPhoneNumber(Integer crmAccountId) {
//        EdsCrmAccount client = clientManager.get(crmAccountId);
//        if (client == null || client.isDeleted()) {
//            return null;
//        }
//        CrmAccountItem clientItem = new CrmAccountItem();
//        if (client.getPrimaryContact() != null) {
//            ContactListItem contactItem = new ContactListItem();
//            contactItem.setObjectId(client.getPrimaryContact().getObjectID());
//            clientItem.setObjectId(client.getObjectID());
//            clientItem.setPhone(client.getPrimaryContact().getPrimaryPhone());
//            clientItem.setPrimaryContact(contactItem);
//        }
////        if (!client.getBalanceCalculated()) {
//        clientItem.setClientBalance(crmAccountManager.getClientBalance(client.getObjectID()).doubleValue());
////            client.setBalanceCalculated(true);
////            crmAccountManager.update(client);
////        }
//        clientItem.setAccountTypes(client.getAccountTypeList());
//        return clientItem;
//    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CrmAccountItem getSupplier(Integer objectID) {
        CrmAccountItem supplierItem = new CrmAccountItem();
        EdsCrmAccount supplier = crmAccountManager.get(objectID);
        if (supplier != null) {
            supplierItem = supplier.getRPC(supplierItem, false);

            List<EdsAddress> billAddrList = addressManager.getAddressesByEntityIdAndType(objectID, EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            List<EdsAddress> mailAddrList = addressManager.getAddressesByEntityIdAndType(objectID, EdsAddress.MAILING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            supplierItem.setBillAddresses(supplier.getAddressData(billAddrList, supplier.getBillingAddress()));
            supplierItem.setMailAddresses(supplier.getAddressData(mailAddrList, supplier.getMailingAddress()));

            EdsCompany company = clientManager.getUser().getCompany();
            if (supplier.getCurrency() != null) {
                supplierItem.setCurrency(supplier.getCurrency().getName());
            }
            if (supplier.getVatNumber() != null && !"".equals(supplier.getVatNumber())) {
                supplierItem.setVatNumber(supplier.getVatNumber());
            }
            if (supplier.getRegistrationNumber() != null && !"".equals(supplier.getRegistrationNumber())) {
                supplierItem.setRegistrationNumber(supplier.getRegistrationNumber());
            }
            if (supplier.getPaymentMethod() != null) {
                supplierItem.setPaymentMethod(commonLocalizer.localize(supplier.getPaymentMethod().getCode(), supplier.getPaymentMethod().getName()));
            }
//            if (!supplier.getBalanceCalculated()) {
            supplierItem.setSupplierBalance(crmAccountManager.getSupplierBalance(objectID).doubleValue());
//
//                supplier.setBalanceCalculated(true);
//                crmAccountManager.update(supplier);
//            }
            /*Bank Account Details*/
            supplierItem.setEncryptedID(EncryptionHelper.encryptURL("supplier/" + supplier.getObjectID().toString()));

            supplierItem.setCustomFields((ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(supplier.getCustomFields(),
                    commonService.getCompanyCustomFields(ViewName.CrmAccount)));
        } else {
            return null;
        }
        return supplierItem;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getClientCode() {
        return crmService.generateAccountNumber(CrmAccountItem.CUSTOMER);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CrmAccountItem getClientForEdit(Integer objectId) {
        return crmService.editAccount(objectId, EdsCrmAccount.CUSTOMER);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ContactListItem getPrimaryContact(Integer clientId) {
        EdsCrmContact contact = clientContactManager.getPrimaryClientContact(clientId);
        if (contact != null) {
            ContactListItem primaryContact = new ContactListItem();
            primaryContact.setObjectId(contact.getObjectID());
            primaryContact.getCrmAccount().setObjectId(clientId);
            primaryContact.setFirstName(contact.getFirstName());
            primaryContact.setLastName(contact.getLastName());
            primaryContact.setPrimaryPhone(contact.getPrimaryPhone());
            primaryContact.setPrimaryEmail(contact.getPrimaryEmail());
            return primaryContact;
        } else {
            return null;
        }
    }

    public Integer createClient(CrmAccountItem item, Integer userID) {
        return crmService.saveAccount(item, EdsCrmAccount.CUSTOMER, userID, false, false, false, true);
    }

    public ArrayList<Integer> deleteClientsOrSuppliers(ArrayList<Integer> objectIDs, boolean isClient, boolean deleteCrmContact) {
        objectIDs = new ArrayList<>(objectIDs);
        ArrayList<Integer> deletedIDs = new ArrayList<>();
        if (objectIDs.size() > 0) {
            for (Integer clientID : objectIDs) {
                Boolean deleted = isClient
                        ? deleteClient(clientID, deleteCrmContact, true)
                        : deleteSupplier(clientID, deleteCrmContact, true);
                if (deleted != null && deleted) {
                    deletedIDs.add(clientID);
                }
            }
            try {
                solrManager.removeCrmAccountByIds(deletedIDs.toArray(new Integer[0]));
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
        }
        objectIDs.removeAll(deletedIDs);
        return objectIDs;
    }

    public Boolean deleteClient(Integer clientID, boolean deleteCrmContact, boolean isBatchDelete) {
        EdsUser user = userManager.getUser();
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmAccount.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(clientID);
        kpiLog.setEntityType(KpiEntityType.CLIENT);
        ServerUtils.kpiLog(log, kpiLog, "Delete");
        if (clientID != null && !"".equals(clientID)) {
            EdsCrmAccount client = clientManager.get(clientID);
            if (client != null) {
                if (client.getAccountTypes() != null && client.getAccountTypes().size() > 0) {
                    boolean hasAnyRelation = false;
                    for (EdsReference r : client.getAccountTypes()) {
                        hasAnyRelation = false;
                        if (EdsCrmAccount.CUSTOMER.equals(r.getCode())) {
                            hasAnyRelation = allInOneServiceLocal.checkCrmAccountRelations(client.getObjectID(), EdsCrmAccount.CUSTOMER);
                        } else if (EdsCrmAccount.SUPPLIER.equals(r.getCode())) {
                            hasAnyRelation = allInOneServiceLocal.checkCrmAccountRelations(client.getObjectID(), EdsCrmAccount.SUPPLIER);
                        }
                        if (hasAnyRelation) {
                            break;
                        }
                    }
                    if (hasAnyRelation) {
                        return null;//Bu shart bajarilsa deme account ni delete qilolmidi chunki relation lari bor
                    }
                }
                Set<EdsCrmContact> contacts = client.getCrmContacts();
                if (deleteCrmContact) {
                    for (EdsCrmContact crmContact : contacts) {
                        deletedClientContact(user, crmContact, true, deleteCrmContact);
                    }
                }
                EdsCustomerTransaction transaction = transactionManager.getCustomerOpeningBalanceTransaction(clientID);
                if (transaction != null) {
                    transaction.setDeleted(true);
                    EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
                    financialSettingsManager.update(fs);
                }

                baseEventPostProcessor.registerEvent(ClientEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, client, user);
                boolean deleted = client.getAccountTypes().remove(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
                client.setDeleted(deleted);
                clientManager.update(client);
                if (!isBatchDelete && deleted) {
                    try {
                        solrManager.removeCrmAccountByIds(client.getObjectID());
                    } catch (SolrServerException | IOException e) {
                        e.printStackTrace();
                    }
                }
                return deleted;
            }
        }
        return false;
    }

    private void deletedClientContact(EdsUser user, EdsCrmContact contact, boolean isClient, boolean deleteCrmContact) {
        EdsClientContact clientContact = clientContactManager.getClientContactByCrmContact(contact.getObjectID());

        if (clientContact != null) {
            userManager.deleteUser(clientContact.getObjectID(), referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_INACTIVE));

            if (isClient) {
                baseEventPostProcessor.registerEvent(ClientContactEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, clientContact, user);
            }
        }
        if (deleteCrmContact) {
            Integer inactiveID = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_INACTIVE).getObjectID();
            crmContactManager.deleteContact(contact.getObjectID(), inactiveID);
            createContactHistory("Contact deleted", contact);
            baseEventPostProcessor.registerEvent(CrmContactCustomEventListenerImpl.TYPE, CrmContactCustomEventListenerImpl.EVENT_DELETE_CRM_CONTACT_FROM_SOLR, contact, crmContactManager.getUser());
        } else {
            contact.setContactType(EdsCrmContact.CRM_CONTACT);
            contact.setEntityContactID(null);
            if (isClient) {
                contact.getCategories().remove(contactCategoryManager.getDefaultCategoryByContactType(EdsCrmContact.CLIENT_CONTACT));
            } else {
                contact.getCategories().remove(contactCategoryManager.getDefaultCategoryByContactType(EdsCrmContact.SUPPLIER_CONTACT));
            }
            crmContactManager.update(contact);
            createContactHistory("Updated the contact", contact);
            baseEventPostProcessor.registerEvent(CrmContactCustomEventListenerImpl.TYPE, CrmContactCustomEventListenerImpl.EVENT_ADD_CRM_CONTACT_TO_SOLR, contact, crmContactManager.getUser());
        }
    }

    public Boolean deleteSupplier(Integer supplierID, boolean deleteCrmContact, boolean isBatchDelete) {
        EdsUser user = userManager.getUser();
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmAccount.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.DELETE);
        kpiLog.setEntityId(supplierID);
        kpiLog.setEntityType(KpiEntityType.SUPPLIER);
        ServerUtils.kpiLog(log, kpiLog, "Delete supplier");
        if (supplierID != null && !"".equals(supplierID)) {
            EdsCrmAccount supplier = crmAccountManager.get(supplierID);
            // delete supplier contacts
            if (supplier != null) {
                if (supplier.getAccountTypes() != null && supplier.getAccountTypes().size() > 0) {
                    boolean hasAnyRelation = false;
                    for (EdsReference r : supplier.getAccountTypes()) {
                        hasAnyRelation = false;
                        if (EdsCrmAccount.CUSTOMER.equals(r.getCode())) {
                            hasAnyRelation = allInOneServiceLocal.checkCrmAccountRelations(supplier.getObjectID(), EdsCrmAccount.CUSTOMER);
                        } else if (EdsCrmAccount.SUPPLIER.equals(r.getCode())) {
                            hasAnyRelation = allInOneServiceLocal.checkCrmAccountRelations(supplier.getObjectID(), EdsCrmAccount.SUPPLIER);
                        }
                        if (hasAnyRelation) {
                            break;
                        }
                    }
                    if (hasAnyRelation) {
                        return null; //Bu shart bajarilsa deme account ni delete qilolmidi chunki relation lari bor
                    }
                }
                if (deleteCrmContact) {
                    for (EdsCrmContact crmContact : supplier.getCrmContacts()) {
                        deletedClientContact(user, crmContact, false, deleteCrmContact);
                    }
                }
                EdsSupplierTransaction transaction = transactionManager.getSupplierOpeningBalanceTransaction(supplierID);
                if (transaction != null) {
                    transaction.setDeleted(true);
                    EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
                    financialSettingsManager.update(fs);
                }
                boolean deleted = supplier.getAccountTypes().remove(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER));
                supplier.setDeleted(deleted);
                crmAccountManager.update(supplier);
                if (!isBatchDelete && deleted) {
                    try {
                        solrManager.removeCrmAccountByIds(supplierID);
                    } catch (SolrServerException | IOException e) {
                        e.printStackTrace();
                    }
                }
                return deleted;
            }
        }
        return false;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientProjectList getProjects(Integer clientId, ListLoadConfig config) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setClientId(clientId);
        fp.setViewAsId(EdsRole.DR);
        List<EdsProject> projects = projectManager.list(fp);
        int totalCount = projects.size();
//        projects = ListUtils.getSublist(projects, config.getStart(), config.getLimit());
        ClientProjectListItem[] result = new ClientProjectListItem[totalCount];
        int i = 0;
        for (EdsProject project : projects) {
            if (project != null) {
                result[i] = new ClientProjectListItem();
                result[i].setObjectID(project.getObjectID());
                result[i].setName(project.getName());
                result[i].setDescription(project.getDescription() != null ? project.getDescription() : "");
                result[i].setManager(project.getManager() != null ? project.getManager().getFullName() : "");
                result[i].setStartDate(project.getStartDate() != null ? new Date(project.getStartDate().getTime()) : null);
                result[i].setEndDate(project.getEndDate() != null ? new Date(project.getEndDate().getTime()) : null);
                result[i].setTimeSpent(project.getTimeSpentHM() != null ? project.getTimeSpentHM() : "0");
                i++;
            }
        }
        return new ClientProjectList(result, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientContactList getContacts(Integer clientId, ListLoadConfig config) {
        List<EdsCrmContact> contacts = clientManager.getContacts(clientId);
        int totalCount = contacts.size();
        contacts = ListUtils.getSublist(contacts, config.getStart(), config.getLimit());
        ClientContactListItem[] result = new ClientContactListItem[totalCount];
        int i = 0;
        for (EdsCrmContact contact : contacts) {
            if (contact != null) {
                result[i] = new ClientContactListItem();
                result[i].setObjectID(contact.getObjectID());
                result[i].setFirstName(contact.getFirstName());
                result[i].setLastName(contact.getLastName());
                result[i].setEmail(contact.getPrimaryEmail() != null ? contact
                        .getPrimaryEmail() : "N/A");
                result[i].setJobType(contact.getJobFunction() != null ? contact
                        .getJobFunction() : "N/A");
                result[i].setActive(contact.isAccessEnabled());
                i++;
            }
        }
        return new ClientContactList(result, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientContactList getContacts(Integer clientId) {
        List<EdsCrmContact> contacts = clientManager.getContacts(clientId);
        int totalCount = contacts.size();
        ClientContactListItem[] result = new ClientContactListItem[totalCount];
        int i = 0;
        for (EdsCrmContact contact : contacts) {
            if (contact != null) {
                result[i] = new ClientContactListItem();
                result[i].setObjectID(contact.getObjectID());
                result[i].setFirstName(contact.getFirstName());
                result[i].setLastName(contact.getLastName());
                result[i].setEmail(contact.getPrimaryEmail() != null ? contact.getPrimaryEmail() : "N/A");
                result[i].setPhone(contact.getPrimaryPhone() != null ? contact.getPrimaryPhone() : "N/A");
                result[i].setJobType(contact.getJobFunction() != null ? contact.getJobFunction() : "N/A");
                result[i].setActive(contact.isAccessEnabled());
                result[i].setPosition(contact.getJobTitles() != null ? contact.getJobTitles() : "N/A");
                Address address = contact.getPrimaryAddressFromAll();
                if (address != null) {
                    result[i].setAddress(address.asString());
                }
                i++;
            }
        }
        return new ClientContactList(result, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountries() {
        return commonService.getCountries();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRegions() {
        return commonService.getRegions();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getRegions(Integer countryID) {
        return commonServiceLocal.getRegions(countryID);
    }

//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public Integer getCRMContactsCount(String[] contactsEmail) {
//
//        int result = 0;
//
//        EdsCompany company = clientManager.getUser().getCompany();
//        if ((contactsEmail.length != 0) && ("1".equals(company.getObjectID().toString()) ||
//                "5520".equals(clientManager.getUser().getCompany().getObjectID().toString()))) {
//            for (String email : contactsEmail) {
//                if (!email.equals("test@workforcetrack.com")) {
//                    EdsCrmContact crmContact = crmContactManager.getContactByPrimaryEmail(email);
//                    if (crmContact != null) {
//                        result++;
//                    }
//                }
//            }
//        } else {
//            for (String email : contactsEmail) {
//                EdsCrmContact crmContact = crmContactManager.getContactByPrimaryEmail(email);
//                if (crmContact != null) {
//                    result++;
//                }
//            }
//        }
//        return result;
//    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCurrencies() {
        return crmService.getCurrencies();
    }

    //-------------Supplier-----------------

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPaymentMethod() {
        return allInOneServiceLocal.getPaymentMethodList();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SupplierList getSuppliers(ListingFilterParameter lfp) {
        if (lfp == null) {
            lfp = new ListingFilterParameter();
        }
        ListingFilterParameter fp = lfp;
        EdsListPanelSettings panelSettings = listPanelSettingsManager.getUserListPanelSettings(ListPanelType.SupplierListPanel.name(), null);
        ListPanelToolRpc panelTools;
        if (panelSettings != null) {
            panelTools = WfmJsonUtils.jsonDataConvertToListPanelToolsRpc(panelSettings.getSettingsJSONData());
            fp.setColumnsOfListing(panelTools.getColumnCodeName());
        } else {
            ArrayList<String> columnCodeName = CrmAccountItem.defaultClientColumnNames;
            panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(columnCodeName);
            fp.setColumnsOfListing(columnCodeName);
        }
        if (panelTools.isCustomFieldsShown()) {
            fp.setCustomFieldsShown(panelTools.isCustomFieldsShown());
            panelTools.setListViewCustomFields(commonService.getCompanyCustomFieldsForListView(ViewName.CrmAccount));
        }
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CRM_ACCOUNT_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getSolrQueryForSupplier(fp, lfp.asConfig()), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsCrmAccount.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.LIST);
        kpiLog.setEntityType(KpiEntityType.SUPPLIER);
        ServerUtils.kpiLog(log, kpiLog, "Get suppliers list");
        return getSupplierFromSolrResult(resp, fp);
    }

    @Override
    public ListResultTO<CustomerSupplierDto> getSupplierList(ListingFilterParameter fp) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_CRM_ACCOUNT_CORE);
        QueryResponse resp = null;
        try {
            resp = server.query(getSolrQueryForSupplier(fp, fp.asConfig()), SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return getSupplierFromSolrResult(resp);
    }

    @Override
    public CustomerSupplierDto getSupplierById(Integer objectId) {
        EdsCrmAccount supplier = crmAccountManager.get(objectId);

        if (supplier != null) {
            return wrapToCustomerSupplierDto(supplier, false);
        }
        return null;
    }

    private SupplierList getSupplierFromSolrResult(QueryResponse resp, ListingFilterParameter fp) {
        ArrayList<CrmAccountItem> list = new ArrayList<>();
        int totalCount = 0;
        if (resp != null && resp.getResults() != null) {
            totalCount = (int) resp.getResults().getNumFound();
            List<SolrDocument> dataBaseLeadIDs = getDocumentsExistingInBase(SOLR_CRM_ACCOUNT_CORE, resp.getResults(), SolrClientRepresenter.FIELD_CRM_ACCOUNT_ID, true);
            ArrayList<CompanyCustomFieldItem> cfResultForFiltering = commonServiceLocal.getCompanyCustomFieldsForFiltering(ViewName.CrmAccount);
            List<SelectItem> typesSelectItemMap = new ArrayList<>();
            Map<Integer, BigDecimal> balances = new HashMap<>();
            List<EdsReference> types = referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE);
            for (EdsReference type : types) {
                String value = referenceWfmMessageSource.localize(type.getCode(), type.getName());
                typesSelectItemMap.add(new SelectItem(type.getObjectID(), value, type.getCode()));
            }
            if (!dataBaseLeadIDs.isEmpty()) {
                String ids = dataBaseLeadIDs.stream().map(doc -> String.valueOf(SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID))).collect(Collectors.joining(","));
                balances = crmAccountManager.getSupplierBalanceWithMap(ids);
            }
            for (SolrDocument doc : dataBaseLeadIDs) {
                CrmAccountItem item = new CrmAccountItem();
                if (fp == null) {
                    fp = new ListingFilterParameter();
                }
                if (doc != null) {
                    boolean iDsOnly = fp.isIDsOnly();
                    boolean asSelectItem = fp.isAsSelectItem();
                    Integer id = SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_ID);
                    if (asSelectItem || iDsOnly) {
                        item.setObjectId(id);
                        if (!iDsOnly) {
                            item.setName(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NAME));
                            item.setNumber(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NUMBER));
                            if (SolrUtils.asListString(doc, SolrCrmAccountRepresenter.FIELD_OWNER_NAME) != null) {
                                item.setOwnerNames(ServerUtils.collectionToCommaDelimitedString(SolrUtils.asListString(doc, SolrCrmAccountRepresenter.FIELD_OWNER_NAME)));
                            }
                        }
                    } else {
                        item.setObjectId(id);

                        if (SolrUtils.asListString(doc, SolrCrmAccountRepresenter.FIELD_OWNER_NAME) != null) {
                            item.setOwnerNames(ServerUtils.collectionToCommaDelimitedString(SolrUtils.asListString(doc, SolrCrmAccountRepresenter.FIELD_OWNER_NAME)));
                        }
                        Integer parentID = SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_ID);
                        if (parentID != null) {
                            CrmAccountItem parent = new CrmAccountItem();
                            parent.setObjectId(parentID);
                            parent.setName(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_PARENT_NAME));
                            item.setParent(parent);
                        }
                        item.setName(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NAME));
                        item.setNumber(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_CRM_ACCOUNT_NUMBER));
                        if (typesSelectItemMap.size() > 0) {
                            item.setAccountTypes(typesSelectItemMap.toArray(new SelectItem[]{}));
                            if (doc.getFieldValue(SolrCrmAccountRepresenter.FIELD_TYPE_IDS) != null) {
                                if (doc.getFieldValue(SolrCrmAccountRepresenter.FIELD_TYPE_IDS) instanceof ArrayList) {
                                    SelectItem.setSelected(item.getAccountTypes(), false, ((ArrayList<Integer>) doc.getFieldValue(SolrCrmAccountRepresenter.FIELD_TYPE_IDS)).toArray(new Integer[]{}));
                                } else if (doc.getFieldValue(SolrCrmAccountRepresenter.FIELD_TYPE_IDS) instanceof Integer) {
                                    SelectItem.setSelected(item.getAccountTypes(), false, (Integer) doc.getFieldValue(SolrCrmAccountRepresenter.FIELD_TYPE_IDS));
                                }
                            }
                        }
                        item.setBlocked(SolrUtils.asBoolean(doc, SolrCrmAccountRepresenter.FIELD_BLOCKED));
                        item.setIndustry(referenceWfmMessageSource.localize(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_INDUSTRY_CODE), SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_INDUSTRY_NAME)));
                        item.setIndustryID(SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_INDUSTRY_ID));
                        item.setEmail(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_EMAIL, ""));
                        item.setPhone(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_PHONE, "N/A"));
                        item.setFax(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_FAX, "N/A"));
                        item.setWebsite(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_WEBSITE, "N/A"));
                        item.setTermName(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_TERM_NAME));

                        Address billAddress = new Address();
                        billAddress.setPrimary(true);
                        billAddress.setObjectID(SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_ADRESS1_ID));
                        billAddress.setAddress(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_STREET));
                        billAddress.setAddressb(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_STREETB));
                        billAddress.setCity(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_CITY));
                        billAddress.setCountryId(SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_COUNTRY_ID));
                        billAddress.setCountry(countryLocalizer.localize(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_COUNTRY_CODE), SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_COUNTRY_NAME)));
                        billAddress.setState(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_STATE_NAME));
                        billAddress.setStateId(SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_STATE_ID));
                        billAddress.setZipCode(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_POST_CODE));
                        item.setBillAddresses(new Address[]{billAddress});

                        Address mailAddress = new Address();
                        mailAddress.setPrimary(true);
                        mailAddress.setObjectID(SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_ADRESS2_ID));
                        mailAddress.setAddress(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_STREET2));
                        mailAddress.setAddressb(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_STREET2B));
                        mailAddress.setCity(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_CITY2));
                        mailAddress.setCountryId(SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_COUNTRY_ID2));
                        mailAddress.setCountry(countryLocalizer.localize(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_COUNTRY_CODE2), SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_COUNTRY_NAME2)));
                        mailAddress.setState(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_STATE_NAME2));
                        mailAddress.setStateId(SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_STATE_ID2));
                        mailAddress.setZipCode(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_POST_CODE2));
                        item.setMailAddresses(new Address[]{mailAddress});

                        item.setCurrency(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_CURRENCY_NAME));
                        item.setCurrencyId(SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_CURRENCY_ID));
                        item.setVatNumber(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_VAT_NUMBER));
                        item.setTrn(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_TRN_NUMBER));
                        item.setRegistrationNumber(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_REGISTRATION_NUMBER));
                        item.setPaymentMethod(commonLocalizer.localize(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_PAYMENT_METHOD_CODE), SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_PAYMENT_METHOD_NAME)));
                        item.setPaymentMethodId(SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_PAYMENT_METHOD_ID));
                        item.setLastUpdatedDate(SolrUtils.asDate(doc, SolrCrmAccountRepresenter.FIELD_LAST_UPDATED_DATE));
                        item.setCreatedDate(SolrUtils.asDate(doc, SolrCrmAccountRepresenter.FIELD_CREATED_DATE));
                        item.setBankName(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_BANK_NAME));
                        item.setTaxName(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_TAX_NAME));
                        item.setInTarget(SolrUtils.asBoolean(doc, SolrCrmAccountRepresenter.FIELD_IN_TARGET));
                        item.setCreditLimit(SolrUtils.asBigDecimal(doc, SolrCrmAccountRepresenter.FIELD_CREDIT_LIMIT));
                        Date balanceDate = SolrUtils.asDate(doc, SolrCrmAccountRepresenter.FIELD_BALANCE_DATE);
                        Date supplierBalancedate = SolrUtils.asDate(doc, SolrCrmAccountRepresenter.FIELD_SUPPLIER_BALANCE_DATE);
                        item.setBalanceDate(balanceDate != null ? new DateNonConvertable(balanceDate) : null);
                        item.setSupplierBalanceDate(supplierBalancedate != null ? new DateNonConvertable(supplierBalancedate) : null);
                        item.setSaasuGUID(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_SAASU_GUID));
                        item.setSaasuLastUpdatedDate(SolrUtils.asDate(doc, SolrCrmAccountRepresenter.FIELD_SAASU_UPDATED_DATE));
                        item.setSaasuLastUpdatedUid(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_SAASU_UPDATED_UID));
                        if (SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_CONTACT_ID) != null) {
                            ContactListItem contact = new ContactListItem();
                            contact.setObjectId(SolrUtils.asInteger(doc, SolrCrmAccountRepresenter.FIELD_CONTACT_ID));
                            contact.setContactName(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_CONTACT_NAME));
                            contact.setPrimaryEmail(SolrUtils.asString(doc, SolrCrmAccountRepresenter.FIELD_CONTACT_EMAIL));
                            item.setPrimaryContact(contact);
                        }

                        if (fp.isCustomFieldsShown() && fp.getListPanelTool() != null) {
                            item.setCustomFieldsMap(CustomFieldsUtils.getInSolrCustomFields(doc, fp.getListPanelTool().getColumnCodeName()));
                        }
                        if (cfResultForFiltering != null && cfResultForFiltering.size() > 0) {
                            item.setCustomFieldsForFiltering(cfResultForFiltering);
                        }
                    }

                    item.setSupplierBalance((balances.get(id) != null ? balances.get(id) : BigDecimal.ZERO).doubleValue());
                }

                list.add(item);
            }
            //sorting by supplier balance
            if (fp.getSortField() != null && CrmAccountItem.SUPPLIER_BALANCE.equals(fp.getSortField())) {
                list.sort(comparatorFactoriesSupplierItem.get(fp.getSortField()).createComparator(fp.getSortDir()));
            }
        }
        return new SupplierList(list, totalCount);
    }

    private ListResultTO<CustomerSupplierDto> getSupplierFromSolrResult(QueryResponse resp) {
        int totalCount = (int) resp.getResults().getNumFound();
        List<Integer> Ids = resp.getResults().stream().map(doc -> SolrUtils.asInteger(doc, SolrClientRepresenter.FIELD_CRM_ACCOUNT_ID)).collect(Collectors.toList());
        List<EdsCrmAccount> crmAccountList = crmAccountManager.getCrmAccountsByIDs(Ids);
        ArrayList<CustomerSupplierDto> items = new ArrayList<>();
        crmAccountList
                .forEach(crmAccount -> {
                    CustomerSupplierDto dto = wrapToCustomerSupplierDto(crmAccount, CustomFieldsUtils.setRPCCustomFieldItems(null, commonServiceLocal.getCompanyCustomFields(ViewName.CrmAccount)), false);
                    BankDetailsDto bankDetails = new BankDetailsDto();
                    bankDetails.setBankName(crmAccount.getBankName());
                    bankDetails.setAccountName(crmAccount.getAccountName());
                    bankDetails.setAccountNumber(crmAccount.getAccountNo());
                    bankDetails.setSwiftCode(crmAccount.getSwiftCode());
                    bankDetails.setSortCode(crmAccount.getSortCode());
                    bankDetails.setIban(crmAccount.getIbanCode());
                    bankDetails.setBranch(crmAccount.getBranch());
                    bankDetails.setBankAddress(crmAccount.getBankAddress());
                    dto.setBankDetails(bankDetails);
                    items.add(dto);
                });
        return new ListResultTO<>(totalCount, items);
    }

    public Integer createSupplier(CrmAccountItem item, Integer userID) {
        return crmService.saveAccount(item, EdsCrmAccount.SUPPLIER, userID, false, false, false, true);
    }

//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public CrmAccountItem getSupplierForEdit(Integer objectId) {
//        return crmService.editAccount(objectId, EdsCrmAccount.SUPPLIER);
//    }
    //-------------End Supplier---------------

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getSupplierContacts(Integer supplierID) {
        EdsCrmAccount supplier = crmAccountManager.get(supplierID);
        Set<EdsCrmContact> supplierContacts = supplier.getCrmContacts();
        List<SelectItem> items = new ArrayList<>();
        if (supplierContacts != null && supplierContacts.size() > 0) {
            for (EdsCrmContact contact : supplierContacts) {
                items.add(new SelectItem(contact.getObjectID(), contact.getName()));
            }
            items.sort(Comparator.comparing(SelectItem::getName));
        }
        return items.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientContactList getSupplierContactLists(Integer supplierID) {
        EdsCrmAccount supplier = crmAccountManager.get(supplierID);
        Set<EdsCrmContact> supplierContacts = supplier.getCrmContacts();
        List<ClientContactListItem> result = new ArrayList<>();
        int i = 0;
        if (supplierContacts.size() > 0) {
            for (EdsCrmContact supplierContact : supplierContacts) {
                if (supplierContact != null && (supplierContact.isDeleted() == null || !supplierContact.isDeleted())) {
                    ClientContactListItem item = new ClientContactListItem();
                    item.setObjectID(supplierContact.getObjectID());
                    item.setFirstName(supplierContact.getFirstName());
                    item.setLastName(supplierContact.getLastName());
                    item.setEmail(supplierContact.getPrimaryEmail() != null ?
                            supplierContact.getPrimaryEmail() : "N/A");
                    item.setPhone(supplierContact.getPrimaryPhone() != null ?
                            supplierContact.getPrimaryPhone() : "N/A");
                    item.setJobType(supplierContact.getJobFunction() != null ?
                            supplierContact.getJobFunction() : "N/A");
                    item.setActive(supplierContact.isDeleted());
                    item.setPosition(supplierContact.getJobTitles() != null ?
                            supplierContact.getJobTitles() : "N/A");
                    Address address = supplierContact.getPrimaryAddressFromAll();
                    item.setAddress(address != null ? address.toString() : "");
                    result.add(item);
                }
            }
        }
        return new ClientContactList(result.toArray(new ClientContactListItem[]{}), result.size());
    }

    public void setNoteHistoryManager(NoteHistoryManager noteHistoryManager) {
        this.noteHistoryManager = noteHistoryManager;
    }

//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public HistoryListItem[] getClientSupplierNotes(Integer clientID, boolean isClient) {
//        EdsCrmAccount client = clientManager.get(clientID);
//        if (client != null) {
//            EdsNoteHistory[] clientNote = noteHistoryManager.getNoteList(new ListingFilterParameter()).toArray(new EdsNoteHistory[]{});
//            List<EdsNoteHistory> histrClientNotes = new LinkedList<>();
//            List<EdsNoteHistory> histrSupplierNotes = new LinkedList<>();
//            for (EdsNoteHistory noteHistr : clientNote) {
//                if ((EdsNoteHistory.CLIENT == noteHistr.getRelatedTo() && noteHistr.getRelatedId() != null) &&
//                        (noteHistr.getRelatedId().intValue() == client.getObjectID().intValue())) {
//                    histrClientNotes.add(noteHistr);
//                } else if ((EdsNoteHistory.SUPPLIER == noteHistr.getRelatedTo() && noteHistr.getRelatedId() != null) &&
//                        (noteHistr.getRelatedId().intValue() == client.getObjectID().intValue())) {
//                    histrSupplierNotes.add(noteHistr);
//                }
//            }
//            List<EdsNoteHistory> histrNotes = new LinkedList<>();
//            if (isClient) {
//                histrNotes.addAll(histrClientNotes);
//            } else {
//                histrNotes.addAll(histrSupplierNotes);
//            }
//            HistoryListItem[] clientSupplierNotes = new HistoryListItem[histrNotes.size()];
//            for (int i = 0; i < histrNotes.size(); i++) {
//                EdsNoteHistory notes = histrNotes.get(i);
//                HistoryListItem items = new HistoryListItem();
//                items.setObjectID(notes.getObjectID());
//                items.setEmployee(notes.getEmployee().getName());
//                items.setSubject(notes.getSubject());
//                items.setComment(notes.getComment());
//                items.setVisibility(notes.isVisibility());
//                items.setEventDate(notes.getEventDate() != null ? new Date(notes.getEventDate().getTime()) : null);
//                items.setEditable(employeeManager.getUser().equals(notes.getEmployee()));
//                NewsComment[] noteComments = getClientNoteComments(notes.getObjectID());
//                if (noteComments.length > 0) {
//                    items.setNotesComments(noteComments);
//                } else {
//                    items.setNotesComments(new NewsComment[0]);
//                }
//                clientSupplierNotes[i] = items;
//            }
//            return clientSupplierNotes;
//        }
//        return null;
//    }

//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public NewsComment[] getClientNoteComments(Integer noteID) {
//        return commonService.getNotecomments(noteID);
//    }

    public TypeItem createClientFromLead(Integer leadId, Integer userID) {
        if (leadId != null) {
            EdsCrmContact lead = crmContactManager.get(leadId);
            return createClientFromCrmAccount(lead.getCrmAccount().getObjectID(), userID, false);
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public TypeItem getOrCreateCrmAccountFromLead(Integer leadId) {
        EdsCrmContact lead = crmContactManager.get(leadId);
        TypeItem typeItem = null;
        if (lead.getCrmAccount() != null) {
            typeItem = createClientFromCrmAccount(lead.getCrmAccount().getObjectID(), null, false);
        }
        if (typeItem == null) {
            ContactListItem item = new ContactListItem();
            item.setObjectId(lead.getObjectID());
            ContactListItem contactItem = crmService.addAccountToContact(item, true);
            return new TypeItem(contactItem.getCrmAccount().getObjectId(), contactItem.getCrmAccount().getName(), contactItem.getCrmAccount().getNumber());
        }
        return typeItem;
    }

    @Transactional
    public TypeItem createClientFromCrmAccount(Integer accountID, Integer userID, Boolean visible) {
        //if client for given account does not exist we create it, otherwise return the existing client
        if (accountID != null) {
            EdsCrmAccount account = crmAccountManager.get(accountID);
            if (account != null) {
                if (visible) {
                    account.addAccountType(referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER));
                    crmServiceLocal.updateCrmAccountAndAddToSolr(account, false, userID != null
                            ? userManager.get(userID)
                            : crmAccountManager.getUser());
                }
                return new TypeItem(account.getObjectID(), account.getName(), account.getNumber());
            }
        }
        return null;
    }

//    public NewsComment saveClientNoteComments(NewsComment data) {
//        return commonService.saveNoteComment(data);
//    }

    public Integer createContact(ClientContact clientContact) {
        EdsCrmContact crmContact;
        if (clientContact.isClientContact()) {
            EdsCrmAccount client = clientManager.get(clientContact.getClientId());
            crmContact = changeCrmContact(clientContact, client, null);
        } else {
            EdsCrmAccount supplier = crmAccountManager.get(clientContact.getClientId());
            crmContact = changeCrmContact(clientContact, null, supplier);
        }

        return crmContact.getObjectID();
    }

    public FileResource[] getAttachments(Integer clientID) {
        return getClientAttachments(clientManager.get(clientID));
    }

    /*@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientInvoiceList getClientSalesQuotes(Integer clientID) {
        ListingFilterParameter fp = new ListingFilterParameter();
        ListLoadConfig config = new ListLoadConfig();
        config.setStart(0);
        config.setLimit(20);
        EdsCrmAccount c = clientManager.get(clientID);
        fp.setInvoiceClientId(c.getObjectID());
        List<EdsSaleQuote> quotes = quoteManager.getSaleQuoteList(fp, null);
        Integer totalCount = quoteManager.getSaleQuoteListCount(fp);

        return getList(quotes.toArray(new EdsSaleQuote[quotes.size()]), totalCount);
    }*/

    private FileResource[] getClientAttachments(EdsCrmAccount client) {
        List<FileResource> clientAttachments = attachmentUtilsManager.getAttachments(F_CLIENT, client.getObjectID(), client.getObjectID());
        return clientAttachments.toArray(new FileResource[]{});
    }

//    private ClientInvoiceList getList(EdsBaseInvoice[] baseInvoices, int totalCount) {
//        ClientInvoice[] result = new ClientInvoice[baseInvoices.length];
//        int i = 0;
//        for (EdsBaseInvoice baseInvoice : baseInvoices) {
//            boolean isInvoiceInstance = baseInvoice instanceof EdsSaleInvoice || baseInvoice instanceof EdsPurchaseInvoice;
//            if (isInvoiceInstance) {
//                setOverdue(baseInvoice);
//            }
//            result[i] = new ClientInvoice();
//            result[i].setId(baseInvoice.getObjectID());
//            result[i].setInvoiceNumber(baseInvoice.getNumber());
//            result[i].setInvoiceDate(baseInvoice.getInvoiceDate() != null
//                    ? new Date(baseInvoice.getInvoiceDate().getTime())
//                    : null);
//            result[i].setDueDate(baseInvoice.getDueDate() != null
//                    ? new Date(baseInvoice.getDueDate().getTime())
//                    : null);
//            result[i].setClientName(baseInvoice.getClientOrSupplier() != null
//                    ? baseInvoice.getClientOrSupplier().getName()
//                    : "N/A");
//            result[i].setStatus(baseInvoice.getStatus() != null
//                    ? referenceWfmMessageSource.localizeRef(baseInvoice.getStatus())
//                    : "N/A");
//            result[i].setTotal(baseInvoice.getTotal().doubleValue());
//
//            if (isInvoiceInstance) {
//                EdsInvoice invoice = (EdsInvoice) baseInvoice;
//                BigDecimal fullPayment = invoice.getFullPayments();
//
//                result[i].setPayments(fullPayment.divide((invoice.getExchangeRate().compareTo(ZERO) != 0
//                        ?
//                        invoice.getExchangeRate()
//                        : new BigDecimal("1.00")), 2, RoundingMode.HALF_UP).doubleValue());
//            }
//            i++;
//        }
//        return new ClientInvoiceList(result, totalCount);
//    }

//    private void setOverdue(EdsBaseInvoice baseInvoice) {
//        if (getCompanyDate().after(baseInvoice.getDueDate()) && !baseInvoice.getStatus().getCode().equals(PAID) &&
//                !baseInvoice.getStatus().getCode().equals(OVER_DUE)) {
//            baseInvoice.setStatus(referenceManager.findReference(INVOICE_STATUS, OVER_DUE));
//        }
//    }

//    private Date getCompanyDate() {
//        EdsUser user = invoiceManager.getUser();
//        Calendar companyTime = new GregorianCalendar(TimeZone.getTimeZone(user.getCompany().getCountryZone().getZone().getZoneID()));
//        return companyTime.getTime();
//    }

    /*Client Sales Invoices*/

//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public ClientInvoiceList getClientSalesInvoices(Integer clientID) {
//        ListingFilterParameter fp = new ListingFilterParameter();
//        ListLoadConfig config = new ListLoadConfig();
//        config.setStart(0);
//        config.setLimit(20);
//        EdsCrmAccount c = clientManager.get(clientID);
//        fp.setInvoiceClientId(c.getObjectID());
//        List<EdsBaseSaleInvoice> invoiceList = invoiceManager.getSaleInvoiceList(fp);
//        Integer totalCount = invoiceManager.getSaleInvoiceListCount(fp, false);
//        return getList(invoiceList.toArray(new EdsBaseInvoice[0]), totalCount);
//    }

    /*Supplier invoices*/

    /*@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientInvoiceList getSupplierPurchaseInvoices(Integer supplierID) {
        ListingFilterParameter fp = new ListingFilterParameter();

        EdsCrmAccount s = crmAccountManager.get(supplierID);
        fp.setInvoiceClientId(s.getObjectID());
        List<EdsPurchaseInvoice> invoiceList = invoiceManager.getPurchaseInvoiceList(fp, false);

        return createPurchaseInvoiceList(invoiceList);
    }*/

    /*private ClientInvoiceList createPurchaseInvoiceList(List<EdsPurchaseInvoice> invoices) {
        int totalCount = invoices.size();
        return getList(invoices.toArray(new EdsBaseInvoice[]{}), totalCount);
    }*/

    /*@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientInvoiceList getSupplierPurchaseOrders(Integer supplierID) {
        ListingFilterParameter fp = new ListingFilterParameter();
        EdsCrmAccount s = crmAccountManager.get(supplierID);
        fp.setInvoiceClientId(s.getObjectID());
        List<EdsPurchaseOrder> quotes = quoteManager.getPurchaseOrderList(fp, null);

        return getList(quotes.toArray(new EdsPurchaseOrder[quotes.size()]), quotes.size());
    }*/

    private EdsCrmContact changeCrmContact(ClientContact clientContact, EdsCrmAccount client, EdsCrmAccount supplier, boolean... importing) {
        EdsUser owner = ((client != null && client.getOwnerID() != null)
                ? userManager.get(client.getOwnerID())
                : clientContactManager.getUser());
        if (owner == null && client != null && client.getOwners() != null && client.getOwners().size() > 0) {
            owner = client.getOwners().get(0);
        }
        Integer crmContactID = clientContact.getObjectID();
        EdsCrmContact crmContact = null;
        if (crmContactID != null) {
            crmContact = crmContactManager.get(crmContactID);
        }
        if (crmContact == null) {
            crmContact = new EdsCrmContact();
        }
        if (client != null) {
            crmContact.setImportFileID(client.getImportFileID());
            crmContact.setEntityID(client.getEntityID());
            crmContact.setJobTitles(clientContact.getPosition());
        }
        crmContact.setCrmAccount(client != null ? client : supplier);
        return changeCrmContact(clientContact, crmContact, owner, importing);
    }

    private EdsCrmContact changeCrmContact(ClientContact clientContact, EdsCrmContact crmContact, EdsUser owner, boolean... importing) {
        crmContact.setImportFileID(clientContact.getImportFileID());
        crmContact.setFirstName(clientContact.getFirstName());
        crmContact.setLastName(clientContact.getLastName());
        if (clientContact.getPrimaryContact() != null) {
            crmContact.setPrimaryContact(clientContact.getPrimaryContact());

            if (clientContact.getPrimaryContact()) {
                EdsCrmAccount edsCrmAccount = crmContact.getCrmAccount();
                if (edsCrmAccount != null) {
                    for (EdsCrmContact contact : edsCrmAccount.getCrmContacts()) {
                        if (!clientContact.getObjectID().equals(contact.getObjectID())) {
                            contact.setPrimaryContact(Boolean.FALSE);
                        }
                    }
                }
            }
        }

        crmContact.setOwner(owner != null ? owner : clientManager.getUser());
        crmContact.setJobTitles(clientContact.getPosition());
        crmContact.setJobFunction(clientContact.getJobType());
        boolean addedToContactCategory = false;
        if (crmContact.getCrmAccount() != null) {
            if (crmContact.getCrmAccount().isClient() || crmContact.getCrmAccount().isSupplier() || crmContact.getCrmAccount().isVendor()) {
                crmContact.setContactType(crmContact.getCrmAccount().isClient()
                        ? EdsCrmContact.CLIENT_CONTACT
                        : EdsCrmContact.SUPPLIER_CONTACT);
                Set<Integer> contactTypes = new HashSet<>();
                if (crmContact.getCrmAccount().isClient()) {
                    contactTypes.add(EdsCrmContact.CLIENT_CONTACT);
                }
                if (crmContact.getCrmAccount().isSupplier() || crmContact.getCrmAccount().isVendor()) {
                    contactTypes.add(EdsCrmContact.SUPPLIER_CONTACT);
                }
                crmContact.addCategories(contactCategoryManager.getDefaultCategoriesByContactType(contactTypes.toArray(new Integer[]{})));
                addedToContactCategory = true;
            }
        }
        if (!addedToContactCategory) {
            crmContact.setContactType(EdsCrmContact.CRM_CONTACT);
            crmContact.addCategories(contactCategoryManager.getDefaultCategoryByContactType(crmContact.getContactType()));
        }
        //clientContact deleted bo'lib ketsa crmcontactList dagi contact CRMcontactga aylanadi...
        if (clientContact.getDeleted() != null && clientContact.getDeleted()) {
            crmContact.setContactType(EdsCrmContact.CRM_CONTACT);
            crmContact.setEntityContactID(null);
        }
        boolean isNewContact = crmContact.getObjectID() == null;

        crmContactManager.createOrUpdate(crmContact);
        if (isNewContact) {
            createContactHistory("Created the contact", crmContact);
        } else {
            createContactHistory("Updated the contact", crmContact);
        }
        //EMAILS
        //we must put newPrimaryEmail first then it becomes primary for crmContact too.
        EdsCrmContactItemParams email = EdsCrmContactItemParams.getPrimaryAsDomainObject(crmContact.getItemParams(EdsCrmContactItemParams.EMAIL));
        Integer index = 0;
        if (email != null) {
            index = crmContact.getItemParams().indexOf(email);
            crmContact.getItemParams().remove(email);
            email.setLastUpdateTime(new Date());
            email.setValue(clientContact.getEmail().toLowerCase());
        } else {
            email = new EdsCrmContactItemParams(EdsCrmContactItemParams.EMAIL);
            email.setContact(crmContact);
            email.setLastUpdateTime(new Date());
            email.setRelation(EdsCrmContactItemParams.HOME);
            email.setValue(clientContact.getEmail().toLowerCase());
        }
        contactItemParamsManager.createOrUpdate(email);
        crmContact.getItemParams().add(index, email);

        // set clientContact phones
        EdsCrmContactItemParams phone = EdsCrmContactItemParams.getPrimaryAsDomainObject(crmContact.getItemParams(EdsCrmContactItemParams.PHONE));
        if (phone != null) {
            crmContact.getItemParams().remove(phone);
            phone.setValue(clientContact.getPhone());
        } else {
            phone = new EdsCrmContactItemParams(EdsCrmContactItemParams.PHONE);
            phone.setContact(crmContact);
            phone.setLastUpdateTime(new Date());
            phone.setRelation(EdsCrmContactItemParams.HOME);
            phone.setValue(clientContact.getPhone());
        }
        contactItemParamsManager.createOrUpdate(phone);
        crmContact.getItemParams().add(0, phone);

        crmContact.setPrimaryEmail(clientContact.getEmail());
        crmContactManager.update(crmContact);
        if (importing == null || importing.length == 0 || !importing[0]) {
            try {
                if (isNewContact) {
                    solrTransactionManager.registerEvent(SolrEvent.CRM_CONTACT_ADD, crmContact, crmContact.getOwner().getCompany());
                }
                contactSolrComponent.index(crmContact);
            } catch (InterruptedException e) {
                baseEventPostProcessor.registerEvent(CrmContactCustomEventListenerImpl.TYPE, CrmContactCustomEventListenerImpl.EVENT_ADD_CRM_CONTACT_TO_SOLR, crmContact, crmContactManager.getUser());
            }
        }
        if (clientContact.isActive()) {
            if (!crmContact.isAccessEnabled()) {
                enableAccess(crmContact.getObjectID(), false);
            }
        } else {
            if (crmContact.isAccessEnabled()) {
                disableAccess(crmContact.getObjectID());
            }
        }
        return crmContact;
    }

    private void createContactHistory(String message, EdsCrmContact contact) {
        EdsContactHistory contactHistory = new EdsContactHistory();
        contactHistory.setCreationTime(new Date());
        contactHistory.setUpdater(userManager.getUser());
        contactHistory.setContact(contact);
        contactHistory.setMessage(message);
        contactHistoryManager.create(contactHistory);
    }

//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public SelectItem[] getSupplierAsSelectItem() {
//        ListingFilterParameter filterParametrs = new ListingFilterParameter();
//        // filterParametrs.setAccountType(EdsCrmAccount.SUPPLIER);
//        List<EdsCrmAccount> suppliers = crmAccountManager.getList(filterParametrs, null);
//
//        SelectItem[] result = new SelectItem[suppliers.size()];
//        int i = 0;
//        for (EdsCrmAccount supplier : suppliers) {
//            result[i] = new SelectItem(supplier.getObjectID(), supplier.getName());
//            i++;
//        }
//
//        return result;
//    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public PriceLevelItem[] getClientPriceLevels(Integer clientID) {
        EdsCrmAccount client = clientManager.get(clientID);
        List<PriceLevelItem> priceLevelItems = new ArrayList<>();

        if (client != null) {
            for (EdsPriceLevel priceLevel : priceLevelManager.getPriceLevels(null, clientID, false)) {
                PriceLevelItem priceLevelItem = new PriceLevelItem();
                priceLevelItem.setId(priceLevel.getObjectID());
                priceLevelItem.setName(priceLevel.getName());
                priceLevelItem.setType(priceLevel.getType());
                priceLevelItem.setPLCase(priceLevel.getPLCase());
                priceLevelItem.setPercent(priceLevel.getPercent());
                if (priceLevel.getCurrency() != null) {
                    priceLevelItem.setCurrency(new CurrencyItem(priceLevel.getCurrency().getObjectID(), priceLevel.getCurrency().getName(), priceLevel.getCurrency().getSymbol()));
                }

//                initPriceLevelPP(priceLevel, priceLevelItem);
                if (!priceLevel.isDeleted()) {
                    priceLevelItems.add(priceLevelItem);
                }
            }
            if (client.getClientType() != null) {
                List<EdsPriceLevel> clientTypePriceLevels = priceLevelManager.getPriceLevelsByClientType(client.getClientType().getObjectID(), client.getCurrency() != null ? client.getCurrency().getObjectID() : null, false);
                for (EdsPriceLevel priceLevel : clientTypePriceLevels) {
                    PriceLevelItem priceLevelItem = new PriceLevelItem();
                    priceLevelItem.setId(priceLevel.getObjectID());
                    priceLevelItem.setName(priceLevel.getName());
                    priceLevelItem.setType(priceLevel.getType());
                    priceLevelItem.setPLCase(priceLevel.getPLCase());
                    priceLevelItem.setPercent(priceLevel.getPercent());
                    if (priceLevel.getCurrency() != null) {
                        priceLevelItem.setCurrency(new CurrencyItem(priceLevel.getCurrency().getObjectID(), priceLevel.getCurrency().getName(), priceLevel.getCurrency().getSymbol()));
                    }
                    initPriceLevelBB(priceLevel, priceLevelItem);
                    priceLevelItems.add(priceLevelItem);
                }
            }

            if (priceLevelItems.size() > 0) {
                return priceLevelItems.toArray(new PriceLevelItem[]{});
            }
        }
        return new PriceLevelItem[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void initPriceLevelPP(EdsPriceLevel priceLevel, PriceLevelItem priceLevelItem) {
        if (priceLevel.getPriceLevelPPs() != null && priceLevel.getPriceLevelPPs().size() > 0) {
            List<PriceLevelPPItem> priceLevelPPItems = new ArrayList<>();
            for (EdsPriceLevelPP priceLevelPP : priceLevel.getPriceLevelPPs()) {
                PriceLevelPPItem priceLevelPPItem = new PriceLevelPPItem();
                if (priceLevelPP.getProduct() != null) {
                    priceLevelPPItem.setProductID(priceLevelPP.getProduct().getObjectID());
                    priceLevelPPItem.setStandarPrice(priceLevelPP.getProduct().getSellingPrice().doubleValue());
                }
                priceLevelPPItem.setCustomPrice(priceLevelPP.getCustomPrice());
                priceLevelPPItems.add(priceLevelPPItem);
            }

            priceLevelItem.setPriceLevelPPItems(priceLevelPPItems.toArray(new PriceLevelPPItem[]{}));
        }
    }

    private void initPriceLevelBB(EdsPriceLevel priceLevel, PriceLevelItem priceLevelItem) {
        if (priceLevel.getPriceLevelBBs() != null && priceLevel.getPriceLevelBBs().size() > 0) {
            List<PriceLevelBBItem> priceLevelBBItems = new ArrayList<>();
            for (EdsPriceLevelBB priceLevelBB : priceLevel.getPriceLevelBBs()) {
                PriceLevelBBItem priceLevelBBItem = new PriceLevelBBItem();
                priceLevelBBItem.setPercentage(priceLevelBB.getPercent());
                priceLevelBBItem.setBrand(priceLevelBB.getBrand().getAsSelectItem());
                priceLevelBBItem.setEffectType(priceLevelBB.getEffectType());
                priceLevelBBItems.add(priceLevelBBItem);
            }

            priceLevelItem.setPriceLevelBBItems(priceLevelBBItems.toArray(new PriceLevelBBItem[]{}));
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DiscountItem[] getClientDiscounts(Integer clientID) {
        EdsCrmAccount client = clientManager.get(clientID);
        List<DiscountItem> discountItems = new ArrayList<>();

        for (EdsDiscount discount : client.getDiscounts()) {
            discountItems.add(discount.getRPC());
        }
        return discountItems.toArray(new DiscountItem[]{});
    }

    @Override
    public Integer enableAccess(Integer contactID, Boolean fromSubscriptionForm) {
        return crmServiceLocal.enableAccess(contactID, fromSubscriptionForm);
    }

    public Integer disableAccess(Integer contactID) {
        return crmServiceLocal.disableAccess(contactID);
    }

//    @Override
//    public void createAccessEnabledContact(Integer clientID, ClientContact clientContact) {
//        EdsCrmAccount client = clientManager.get(clientID);
//        EdsCrmContact crmContact = changeCrmContact(clientContact, client, null);
//        crmServiceLocal.enableAccess(crmContact.getObjectID(), null);
//    }

    @Override
    public boolean isContactsExist(Integer objectID, String type) {
        return clientContactManager.isContactExist(objectID, type);
    }

//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public BillingData getContactAddress(Integer id, boolean isClient) {
//        EdsCrmAccount clientBase;
//        if (isClient) {
//            clientBase = clientManager.get(id);
//        } else {
//            clientBase = crmAccountManager.get(id);
//        }
//
//        BillingData data = new BillingData();
//        EdsAddress bAddr = clientBase.getBillingAddress();
//        if (clientBase.getCurrency() != null) {
//            data.setCurrencyId(clientBase.getCurrency().getObjectID());
//        }
//
//        EdsCompany company = clientManager.getUser().getCompany();
//        EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);
//        if (invoicingSettings != null && invoicingSettings.getAddressTemplate() != null && !"".equals(invoicingSettings.getAddressTemplate().trim())) {
//            Map<String, Object> values = new HashMap<>();
//            values.put("COMPANY_NAME", company.getName());
//            values.put("ADDRESS", bAddr.getAddress() != null ? bAddr.getAddress() : "");
//            values.put("CITY", bAddr.getCity() != null ? bAddr.getCity() : "");
//            values.put("COUNTRY", bAddr.getCountry() != null ? bAddr.getCountry().getName() : "");
//            values.put("STATE", bAddr.getState() != null ? bAddr.getState().getName() : "");
//            values.put("ZIPCODE", bAddr.getZipCode() != null ? bAddr.getZipCode() : "");
//            EdsTemplate template = new EdsTemplate(invoicingSettings.getAddressTemplate());
//            try {
//                data.setProcessedTemplate(template.processContent(values));
//            } catch (Exception e) {
//                e.printStackTrace();
//                return generateBillingData(bAddr, data);
//            }
//            return data;
//        } else {
//            return generateBillingData(bAddr, data);
//        }
//    }

    private BillingData generateBillingData(EdsAddress bAddr, BillingData data) {
        int nullCounter = 0;
        if (bAddr == null) {
            bAddr = new EdsAddress();
        }
        if (bAddr.getAddress() != null) {
            data.setAddress(bAddr.getAddress());
        } else {
            data.setAddress("");
            nullCounter++;
        }
        if (bAddr.getAddressb() != null) {
            data.setAddress2(bAddr.getAddressb());
        } else {
            data.setAddress2("");
            nullCounter++;
        }
        if (bAddr.getCity() != null) {
            data.setCity(bAddr.getCity());
        } else {
            data.setCity("");
            nullCounter++;
        }
        if (bAddr.getCountry() != null) {
            data.setCountry(new SelectItem(bAddr.getCountry().getObjectID(), bAddr.getCountry().getName()));
        } else {
            nullCounter++;
        }
        if (bAddr.getState() != null) {
            data.setState(new SelectItem(bAddr.getState().getObjectID(), bAddr.getState().getName()));
        } else {
            nullCounter++;
        }
        if (bAddr.getZipCode() != null) {
            data.setZipCode(bAddr.getZipCode());
        } else {
            data.setZipCode("");
            nullCounter++;
        }
        data.setNullInstance(nullCounter == 6);

        return data;
    }

    @Override
    public ClientSupplierAddressData getAddressData(Integer clientSupplierID, boolean isClient, Address.EntityType entityType) {
        ClientSupplierAddressData data = new ClientSupplierAddressData();
        data.setClientSupplierID(clientSupplierID);
        SelectItem[] billAddrItems, mailAddrItems;
        EdsCrmAccount crmAccount = crmAccountManager.get(clientSupplierID);
        Integer objectId = null;
        if (Address.EntityType.CrmAccount.equals(entityType) && crmAccount != null && crmAccount.getObjectID() != null) {
            objectId = crmAccount.getObjectID();
            if (crmAccount.getCurrency() != null) {
                data.setCurrencyID(crmAccount.getCurrency().getObjectID());
            }
        } else if (Address.EntityType.Company.equals(entityType)) {
            objectId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
        }
        List<EdsAddress> billAddresses = addressManager.getAddressesByEntityIdAndType(objectId, EdsAddress.BILLING_ADDRESS,
                Address.EntityType.Company.equals(entityType)
                        ? EdsAddress.ENTITY_TYPE_COMPANY
                        : EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
        List<EdsAddress> mailAddresses = addressManager.getAddressesByEntityIdAndType(objectId, EdsAddress.MAILING_ADDRESS,
                Address.EntityType.Company.equals(entityType)
                        ? EdsAddress.ENTITY_TYPE_COMPANY
                        : EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);

        int i = 0;
        billAddrItems = new SelectItem[billAddresses.size()];
        for (EdsAddress addr : billAddresses) {
            billAddrItems[i++] = new SelectItem(addr.getObjectID(),
                    ((addr.getName() != null && !"".equals(addr.getName().trim()))
                            ? addr.getName()
                            : "(no name)"), addr.getAddressDataAsHTML());
        }
        i = 0;
        mailAddrItems = new SelectItem[mailAddresses.size()];
        for (EdsAddress addr : mailAddresses) {
            mailAddrItems[i++] = new SelectItem(addr.getObjectID(),
                    ((addr.getName() != null && !"".equals(addr.getName().trim()))
                            ? addr.getName()
                            : "(no name)"), addr.getAddressDataAsHTML());
        }
        data.setBillAddresses(billAddrItems);
        data.setMailAddresses(mailAddrItems);
        if (crmAccount != null && crmAccount.getBillingAddress() != null) {
            data.setPrimaryBillAddressID(crmAccount.getBillingAddress().getObjectID());
        }
        if (crmAccount != null && crmAccount.getMailingAddress() != null) {
            data.setPrimaryMailAddressID(crmAccount.getMailingAddress().getObjectID());
        }
        return data;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Address editAddress(Integer addressID) {
        EdsAddress address = addressManager.get(addressID);
        return address.getRPC();
    }

    @Override
    public Integer saveAddress(Address data, Integer clientSupplierID, boolean isClient, boolean isBilling, Address.EntityType entityType) {
        EdsCountry country = (data.getCountryId() != null ? countryManager.get(data.getCountryId()) : null);
        EdsRegion state = (data.getStateId() != null ? regionManager.get(data.getStateId()) : null);
        EdsAddress address = (data.getObjectID() != null ? addressManager.get(data.getObjectID()) : new EdsAddress());
        if (clientSupplierID != null && Address.EntityType.CrmAccount.equals(entityType)) {
            EdsCrmAccount crmAccount = clientManager.get(clientSupplierID);
            address.setCrmAccount(crmAccount);
        } else if (Address.EntityType.Company.equals(entityType)) {
            Integer companyId = Integer.parseInt(ServerSecurityContext.getInstance().getCompanyId());
            address.setEntityID(companyId);
            address.setEntityType(EdsAddress.ENTITY_TYPE_COMPANY);
        }
        address.setRelationType(isBilling ? EdsAddress.BILLING_ADDRESS : EdsAddress.MAILING_ADDRESS);
        address.setAddressData(data, country, state);
        addressManager.createOrUpdate(address);
        return address.getObjectID();
    }

    @Override
    public CrmAccountBalance getCrmAccountBalanceReport(DateNonConvertable fromDate, DateNonConvertable toDate, ListingFilterParameter fp) {

        //when one of the gevin date ranges is null then return null result
        if (fromDate == null || toDate == null) {
            return null;
        }

        SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-d  HH:mm:ss");
        String formattedStartDate = dformat.format(fromDate.getNonConvertedDate());
        String formattedEndDate = dformat.format(toDate.getNonConvertedDate());

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsCurrency baseCurrency = financialSettings.getCurrency();
        boolean isMultiCurrencyCrmAccountBalance = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTIPLE_CURRENCY_CRM_ACCOUNT_BALANCE);
        EdsCrmAccount crmAccount = crmAccountManager.get(fp.getCrmAccountId());

        if (crmAccount == null) {
            return null;
        }
        CrmAccountBalance crmAccountBalance = new CrmAccountBalance();

        ArrayList<Integer> IdList = new ArrayList<>();
        if (fp.isShowSubAccountTransaction()) {
            buildChildIds(crmAccount, IdList);
        } else {
            IdList.add(fp.getCrmAccountId());
        }

        if (isMultiCurrencyCrmAccountBalance) {
            List<Integer> crmAccountUsedCurrencyIDList = transactionManager.getCrmAccountUsedCurrencies(IdList, fp.getAccountType());
            if (!crmAccountUsedCurrencyIDList.contains(baseCurrency.getObjectID())) {
                crmAccountUsedCurrencyIDList.add(financialSettings.getCurrency().getObjectID());
            }
            if (crmAccount.getCurrency() != null && !crmAccountUsedCurrencyIDList.contains(crmAccount.getCurrency().getObjectID())) {
                crmAccountUsedCurrencyIDList.add(crmAccount.getCurrency().getObjectID());
            }

            ArrayList<CrmAccountCurrencyBalance> balancesByCurrencyList = new ArrayList<>();
            int total = 0;
            for (Integer currencyID : crmAccountUsedCurrencyIDList) {
                if (fp.getCurrencyID() != null && !fp.getCurrencyID().equals(baseCurrency.getObjectID()) && !fp.getCurrencyID().equals(currencyID)) {
                    continue;
                }
                boolean isBaseCurrency = currencyID.equals(baseCurrency.getObjectID());
                BigDecimal beginningBalanceInBase = BigDecimal.ZERO, beginningBalance = BigDecimal.ZERO, endingBalanceinBase = BigDecimal.ZERO, endingBalance = BigDecimal.ZERO;
                LinkedHashMap<BigDecimal, BigDecimal> beginningBalanceMap = transactionManager.getCrmAccountEarlyBalance(IdList, formattedStartDate, fp.getAccountType(), currencyID, isBaseCurrency);
                for (BigDecimal key : beginningBalanceMap.keySet()) {
                    beginningBalanceInBase = key;
                    endingBalanceinBase = key;
                    beginningBalance = beginningBalanceMap.get(key);
                    endingBalance = beginningBalanceMap.get(key);

                }
                List<CrmAccountBalanceItem> items = transactionManager.getCrmAccountBalance(IdList, formattedStartDate, formattedEndDate, fp.getAccountType(), currencyID, isBaseCurrency, fp, null);
                total += transactionManager.getCrmAccountBalanceCount(IdList, formattedStartDate, formattedEndDate, fp.getAccountType(), currencyID, isBaseCurrency);
                crmAccountBalance.setTotalCount(total);

                if (fp.getStart() > 0) {
                    fp.setLimit(fp.getStart());
                    fp.setStart(0);
                    endingBalance = endingBalance.add(transactionManager.getCrmAccountPrevPageBalance(IdList, formattedStartDate, formattedEndDate, fp.getAccountType(), currencyID, isBaseCurrency, fp));
                }

                CrmAccountCurrencyBalance currencyBalance = createBalanceItem(beginningBalanceInBase, beginningBalance, endingBalanceinBase, endingBalance, items);
                fp.setStart(0);
                fp.setLimit(0);
                currencyBalance.setEndingBalance(currencyBalance.getEarlyBalance().add(transactionManager.getCrmAccountPrevPageBalance(IdList, formattedStartDate, formattedEndDate, fp.getAccountType(), currencyID, isBaseCurrency, fp)));

                currencyBalance.setBaseCurrency(baseCurrency.createCurrencyItem());
                currencyBalance.setCurrency(currencyManager.get(currencyID).createCurrencyItem());
                balancesByCurrencyList.add(currencyBalance);
            }

            crmAccountBalance.setCrmAccountItem(crmAccountManager.get(fp.getCrmAccountId()).getAsSelectItem());
            crmAccountBalance.setCurrencyBalances(balancesByCurrencyList);
            return crmAccountBalance;
        } else {
            Integer exchangeRateScale = financialSettingsManager.getFinancialSettings().getExchangeRateScale();
            Calendar prevBalanceDateCal = ServerUtils.convertDateIntoCalendar(fromDate != null ? fromDate.getNonConvertedDate() : new Date());
            prevBalanceDateCal.add(Calendar.DAY_OF_MONTH, -1);
            Date prevBalanceDate = ServerUtils.getStartDate(prevBalanceDateCal.getTime());

//            CurrencyListItem currencyListItem = currencyService.getCurrencyRateByDate(fp.getCurrencyID(), new DateNonConvertable(prevBalanceDate));
//            BigDecimal exchangeRate = BigDecimal.valueOf(currencyListItem.getExchangeRate()).setScale(exchangeRateScale, RoundingMode.HALF_UP);

            BigDecimal beginningBalanceInBase = BigDecimal.ZERO,beginningBalance = BigDecimal.ZERO, endingBalanceinBase = BigDecimal.ZERO, endingBalance = BigDecimal.ZERO;
            LinkedHashMap<BigDecimal, BigDecimal> beginningBalanceMap = transactionManager.getCrmAccountEarlyBalance(IdList, formattedStartDate, fp.getAccountType(), null, true);
            for (BigDecimal key : beginningBalanceMap.keySet()) {
                beginningBalanceInBase = key;
                endingBalanceinBase = key;
                beginningBalance = beginningBalanceMap.get(key);
                endingBalance = beginningBalanceMap.get(key);

            }
            List<CrmAccountBalanceItem> items = transactionManager.getCrmAccountBalance(IdList, formattedStartDate, formattedEndDate, fp.getAccountType(), null, true, fp, null);
            crmAccountBalance.setTotalCount(transactionManager.getCrmAccountBalanceCount(IdList, formattedStartDate, formattedEndDate, fp.getAccountType(), null, true));

            if (fp.getStart() > 0) {
                fp.setLimit(fp.getStart());
                fp.setStart(0);
                endingBalance = endingBalance.add(transactionManager.getCrmAccountPrevPageBalance(IdList, formattedStartDate, formattedEndDate, fp.getAccountType(), null, true, fp));
            }

//            beginningBalance = beginningBalance.multiply(exchangeRate.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
//            endingBalance = endingBalance.multiply(exchangeRate.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));


            CrmAccountCurrencyBalance currencyBalance = createBalanceItem(beginningBalanceInBase, beginningBalance, endingBalanceinBase, endingBalance, items);
            fp.setStart(0);
            fp.setLimit(0);
            BigDecimal prevPageBalance = transactionManager.getCrmAccountPrevPageBalance(IdList, formattedStartDate, formattedEndDate, fp.getAccountType(), null, true, fp);
//            prevPageBalance = prevPageBalance.multiply(exchangeRate.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP));
            currencyBalance.setEndingBalance(currencyBalance.getEarlyBalance().add(prevPageBalance));

            currencyBalance.setBaseCurrency(baseCurrency.createCurrencyItem());
            currencyBalance.setCurrency(fp.getCurrencyID() != null ? currencyManager.get(fp.getCurrencyID()).createCurrencyItem() : financialSettings.getCurrency().createCurrencyItem());
            ArrayList<CrmAccountCurrencyBalance> balancesByCurrencyList = new ArrayList<>();
            balancesByCurrencyList.add(currencyBalance);

            crmAccountBalance.setCrmAccountItem(crmAccountManager.get(fp.getCrmAccountId()).getAsSelectItem());
            crmAccountBalance.setCurrencyBalances(balancesByCurrencyList);
            return crmAccountBalance;
        }
    }

    private void buildChildIds(EdsCrmAccount parent, ArrayList<Integer> IdList) {
        if (parent.getChildList() != null && !parent.getChildList().isEmpty()) {
            IdList.add(parent.getObjectID());

            for (EdsCrmAccount child : parent.getChildList()) {
                buildChildIds(child, IdList);
            }
        } else {
            IdList.add(parent.getObjectID());
        }
    }

    private CrmAccountCurrencyBalance createBalanceItem(BigDecimal beginningBalanceInBase,BigDecimal beginningBalance, BigDecimal endingBalanceInBase, BigDecimal endingBalance, List<CrmAccountBalanceItem> items) {
        for (CrmAccountBalanceItem bi : items) {
            if ("EdsInvoiceTransaction".equals(bi.getTransactionType())) {
                bi.setTransactionType(INVOICE_TRANSACTION);
            } else if ("EdsInvoicePaymentTransaction".equals(bi.getTransactionType())) {
                bi.setTransactionType(INVOICEPAYMENT_TRANSACTION);
            } else if ("EdsCustomerTransaction".equals(bi.getTransactionType())) {
                bi.setTransactionType(CUSTOMER_TRANSACTION);
            } else if ("EdsSupplierTransaction".equals(bi.getTransactionType())) {
                bi.setTransactionType(SUPPLIER_TRANSACTION);
            } else if ("EdsCusSuppPaymentTransaction".equals(bi.getTransactionType())) {
                bi.setTransactionType(CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION);
            } else if ("EdsFixedAssetTransaction".equals(bi.getTransactionType())) {
                bi.setTransactionType(INVOICE_TRANSACTION);
            } else if ("EdsManualTransaction".equals(bi.getTransactionType())) {
                bi.setTransactionType(MANUAL_TRANSACTION);
            } else if ("EdsBankCheckTransaction".equals(bi.getTransactionType())) {
                bi.setTransactionType(BANK_CHECK_TRANSACTION);
            } else if ("EdsExpenseTransaction".equals(bi.getTransactionType())) {
                bi.setTransactionType(EXPENSE_TRANSACTION);
            } else if ("EdsExpensePaymentTransaction".equals(bi.getTransactionType())) {
                bi.setTransactionType(EXPENSEPAYMENT_TRANSACTION);
            }

            endingBalance = endingBalance.add(bi.getAmount());
            endingBalanceInBase = endingBalanceInBase.add(bi.getAmountInBase());
            bi.setBalance(endingBalance);
        }

        CrmAccountCurrencyBalance currencyBalance = new CrmAccountCurrencyBalance();
        currencyBalance.setItems(items.toArray(new CrmAccountBalanceItem[]{}));
        currencyBalance.setEarlyBalanceInBase(beginningBalanceInBase);
        currencyBalance.setEarlyBalance(beginningBalance);
        currencyBalance.setEndingBalanceInBase(endingBalanceInBase);
        currencyBalance.setEndingBalance(endingBalance);
        return currencyBalance;
    }

//    @Override
//    public void updateCrmAccountsAfterExportSaasu(Integer objectId, Date lastUpdateDate, String saasuLastUpdatedUid, Integer saasuGUID) {
//        EdsCrmAccount customerOrSupplier = crmAccountManager.get(objectId);
//        if (customerOrSupplier != null) {
//            if (saasuGUID != null) {
//                customerOrSupplier.setSaasuGUID(saasuGUID.toString());
//            }
//            customerOrSupplier.setSasuuLastUpdatedTime(lastUpdateDate);
//            customerOrSupplier.setSaasuLastUpdatedUid(saasuLastUpdatedUid);
//            crmAccountManager.update(customerOrSupplier);
//        }
//    }

    @Override
    public InvoiceTermsItem[] getInvoiceTermsForLookUp(ListingFilterParameter fp) {
        List<EdsInvoiceTerms> termsList = invoiceTermsManager.getInvoiceTerms(fp);
        InvoiceTermsItem[] termsItems = new InvoiceTermsItem[termsList.size()];
        int i = 0;
        for (EdsInvoiceTerms invTerm : termsList) {
            termsItems[i++] = invTerm.getAsRPC();
        }
        return termsItems;
    }

//    @Override
//    public CrmAccountItem getSupplierByQBSupplierID(String qbSupplierID) {
//        Integer supplierId = clientManager.getClientIdByQBCustomerId(qbSupplierID);
//
//        if (supplierId != null) {
//            return getSupplierForEdit(supplierId);
//        }
//        return null;
//    }

//    @Override
//    public String getSupplierCode() {
//        return crmService.generateAccountNumber(CrmAccountItem.SUPPLIER);
//    }

    @Override
    public SelectItem[] getSubsidiaries(ListingFilterParameter filterParametrs) {
        List<EdsSubsidiariesCompany> edsSubsidiariesCompanyList = subsidiariesCompanyManager.getSubsidiariesCompanies(filterParametrs);

        SelectItem[] subsidiariesItems = new SelectItem[edsSubsidiariesCompanyList.size() + (filterParametrs.isShowPA()
                ? 1
                : 0)];

        if (filterParametrs.isShowPA()) {
            subsidiariesItems[0] = new SelectItem(userManager.getUser().getCompany().getObjectID(), userManager.getUser().getCompany().getName());
        }

        int i = filterParametrs.isShowPA() ? 1 : 0;
        for (EdsSubsidiariesCompany edsSubsidiariesCompany : edsSubsidiariesCompanyList) {
            subsidiariesItems[i++] = new SelectItem(edsSubsidiariesCompany.getObjectID(), edsSubsidiariesCompany.getCompanyName(), edsSubsidiariesCompany.getCurrencyID().toString());
        }
        return subsidiariesItems;
    }

    public void sendCustomerBalanceEmail(MessageItem messageItem, DateNonConvertable fromDatNC, DateNonConvertable toDateNC) {
        messageManager.setCustomerBalanceEmail(messageItem, fromDatNC, toDateNC);
    }

    @Override
    public void blockAccount(Integer objectID, boolean blockOrUnblock) {
        EdsCrmAccount supplier = clientManager.get(objectID);

        if (supplier != null) {
            supplier.setBlocked(!blockOrUnblock);
            clientManager.update(supplier);
            try {
                crmAccountSolrComponent.index(supplier);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CrmAccountItem getCustomerQuickData(String accountType) {
        CrmAccountItem result = new CrmAccountItem();
        EdsUser edsUser = userManager.getUser();
        if (edsUser == null) {
            return result;
        }
        EdsUser user = (EdsUser) SecurityContext.getInstance().getUser();
        if (user != null) {
            result.setOwnerID(user.getObjectID());
            result.setOwnerName(user.getFullName() != null ? user.getFullName() : "");
        }
        result.setOwnerItems(crmServiceLocal.getOwnersListByPermission(PermissionConstants.CRM_LEAD_CONTACT_ASSIGNEE));
        result.setIndustries(ServerUtils.getAsSelectItem(referenceManager.listReferences("_COMPANY_WORKAREA"), ServerUtils.REFERENCE));
        if (EdsCrmAccount.CUSTOMER.equals(accountType)) {
            result.setTypeChecked(EdsCrmAccount.CUSTOMER, null);
            result.setBankAccounts(crmServiceLocal.getBankAccounts());
            EdsInvoicingSettings invoicingSettings = invoicingSettingsManager.getInvoiceSettings(edsUser.getCompany());
            if (invoicingSettings != null) {
                result.setBankAccountId(invoicingSettings.getBankAccountId());
            }
        } else if (EdsCrmAccount.SUPPLIER.equals(accountType)) {
            result.setTypeChecked(EdsCrmAccount.SUPPLIER, null);
        }
        ClientCurrency companyCurrency = crmServiceLocal.getClientCurrency();
        if (companyCurrency != null) {
            result.setCurrencies(companyCurrency.getItems());
            if (companyCurrency.getUserCurrencyId() != null) {
                result.setCurrencyId(companyCurrency.getUserCurrencyId());
            }
        }
        CurrencyItem baseCurrency = currencyService.getBaseCurrency();
        if (baseCurrency != null) {
            result.setBaseCurrencyID(baseCurrency.getId());
        }
        result.setAccountTypes(ServerUtils.getAsSelectItem(referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE), ServerUtils.REFERENCE));
        //default address information
        Integer countryID = clientManager.getUser().getCompany().getCountryZone().getCountry().getObjectID();
        Address billingAddress = new Address();
        Address mailingAddress = new Address();
        billingAddress.setCountryId(countryID);
        mailingAddress.setCountryId(countryID);
        billingAddress.setPrimary(true);
        mailingAddress.setPrimary(true);
        billingAddress.setName(commonLocalizer.localize(PdfLocalizationName.billingAddress, "Billing Address"));
        mailingAddress.setName(commonLocalizer.localize(PdfLocalizationName.mailingAddress, "Mailing Address"));
        result.setBillAddresses(new Address[]{billingAddress});
        result.setMailAddresses(new Address[]{mailingAddress});

        return result;
    }

    CustomerSupplierDto wrapToCustomerSupplierDto(EdsCrmAccount crmAccount, boolean customer) {
        return wrapToCustomerSupplierDto(crmAccount, null, customer);
    }

    CustomerSupplierDto wrapToCustomerSupplierDto(EdsCrmAccount crmAccount, ArrayList<CompanyCustomFieldItem> customFieldsItems, boolean customer) {
        CustomerSupplierDto dto = new CustomerSupplierDto();
        dto.setId(crmAccount.getObjectID());
        dto.setObjectKey(crmAccount.getObjectKey());
        dto.setName(crmAccount.getName());
        dto.setNumber(crmAccount.getNumber());
        dto.setEmail(crmAccount.getEmail());
        dto.setPhone(crmAccount.getPhone());
        dto.setWebsite(crmAccount.getWebsite());
        dto.setFax(crmAccount.getFax());

        if (crmAccount.getIndustry() != null) {
            dto.setIndustry(new ItemDto(crmAccount.getIndustry().getObjectID(), crmAccount.getIndustry().getName(), crmAccount.getIndustry().getCode()));
        }
        if (!CollectionUtils.isEmpty(crmAccount.getOwners())) {
            dto.setOwners(crmAccount.getOwners().stream().map(owner -> new ItemDto(owner.getObjectID(), owner.getFullName())).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(crmAccount.getBillingAddresses())) {
            dto.setBillingAddresses(crmAccount.getBillingAddresses().stream().map(addr -> ConvertUtils.toDto(addr.getRPC())).collect(Collectors.toList()));
        }
        if (!CollectionUtils.isEmpty(crmAccount.getMailingAddresses())) {
            dto.setShippingAddresses(crmAccount.getMailingAddresses().stream().map(addr -> ConvertUtils.toDto(addr.getRPC())).collect(Collectors.toList()));
        }

        if (customFieldsItems == null) {
            customFieldsItems = CustomFieldsUtils.setRPCCustomFieldItems(null, commonServiceLocal.getCompanyCustomFields(ViewName.CrmAccount));
        } else {
            customFieldsItems = new ArrayList<>(customFieldsItems);
        }
        customFieldsItems = CustomFieldsUtils.setRPCCustomFieldItems(crmAccount.getCustomFields(), customFieldsItems);
        dto.setCustomFields(customFieldsItems.stream().map(CustomFieldsUtils::getCustomFieldDto).filter(cf -> cf.getValue() != null).collect(Collectors.toList()));

        dto.setVatNumber(crmAccount.getVatNumber());

        if (crmAccount.getCurrency() != null) {
            dto.setCurrency(crmAccount.getCurrency().getName());
        }
        if (crmAccount.getVat() != null) {
            EdsVat vat = crmAccount.getVat();
            dto.setTax(new IdName(vat.getObjectID(), vat.getTaxNameAndRateAsString()));
        }
        if (crmAccount.getTerms() != null) {
            EdsInvoiceTerms edsTerm = crmAccount.getTerms();
            IdName termsDto = new IdName(edsTerm.getObjectID(), edsTerm.getName());
            termsDto.addProperty("days", edsTerm.getDays());
            dto.setTerms(termsDto);
        }
        if (crmAccount.getPaymentMethod() != null) {
            EdsPaymentMethod edsPaymentMethod = crmAccount.getPaymentMethod();
            dto.setPaymentMethod(new IdName(edsPaymentMethod.getObjectID(), edsPaymentMethod.getName()));
        }
        if (crmAccount.getBankAccount() != null) {
            EdsBankAccount bank = crmAccount.getBankAccount();
            dto.setBankAccount(new ItemDto(bank.getObjectID(), bank.getAccount().getName(), bank.getIbanCode()));
        }
        dto.setBalance(customer ? crmAccount.getBalanceAmount() : crmAccount.getSupplierBalance());
        dto.setBalanceAsOfDate(customer ? crmAccount.getBalanceDate() : crmAccount.getSupplierBalanceDate());
        dto.setCreditLimit(crmAccount.getCreditLimit());
        dto.setCreatedDate(crmAccount.getCreationTime());
        dto.setUpdatedDate(crmAccount.getLastUpdateTime());
        return dto;
    }
}
