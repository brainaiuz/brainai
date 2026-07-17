package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.analyzer.EdsSolrDbConsistency;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.network.EdsNetwork;
import com.edatasite.workforce.core.domain.network.EdsNetworkContact;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.core.solr.component.*;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.*;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.analyzer.SolrDbConsistencyManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TrusteeManager;
import com.edatasite.workforce.gwt.core.server.rpc.QueryBuilderForSolr;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrSearchUtils;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.rpc.solr.SolrFolderRepresenter;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.edatasite.workforce.gwt.payroll.server.app.PayrollServiceLocal;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.GroupParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: Abdulaziz
 * Date: May 19, 2010
 * Time: 12:06:55 PM
 */
@Transactional
@Service("rbacService")
public class RbacServiceImpl implements RbacService, Constants {

    @Autowired
    private GroupManager groupManager;
    @Autowired
    private BenefitRequestManager benefitRequestManager;
    @Autowired
    private TrusteeManager trusteeManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private ContactCategoryManager contactCategoryManager;
    @Autowired
    private NewsManager newsManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    @Qualifier("backendService")
    private BackendServiceLocal backendServiceLocal;
    @Autowired
    private SolrDbConsistencyManager solrDbConsistencyManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private ItemStockManager itemStockManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    @Qualifier("payrollService")
    private PayrollServiceLocal payrollService;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private ExpenseServiceLocal expenseServiceLocal;
    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private PositionManager positionManager;
    @Autowired
    @Qualifier("hrmsService")
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ContactSolrComponent contactSolrComponent;
    @Autowired
    private CaseSolrComponent caseSolrComponent;
    @Autowired
    private EventSolrComponent eventSolrComponent;
    @Autowired
    private VacancySolrComponent vacancySolrComponent;
    @Autowired
    private EmployeeStepSolrComponent employeeStepSolrComponent;
    @Autowired
    private EmployeeSolrComponent employeeSolrComponent;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;
    @Autowired
    private SinglePayrunSolrComponent singlePayrunSolrComponent;
    @Autowired
    private CashAdvanceSolrComponent cashAdvanceSolrComponent;
    @Autowired
    private GroupPayrunSolrComponent groupPayrunSolrComponent;
    @Autowired
    private AdditionalPaymentSolrComponent additionalPaymentSolrComponent;
    @Autowired
    private ChartOfAccountSolrComponent chartOfAccountSolrComponent;
    @Autowired
    private ShippingDataSolrComponent shippingDataSolrComponent;
    @Autowired
    private SaleInvoiceSolrComponent saleInvoiceSolrComponent;
    @Autowired
    private PurchaseInvoiceSolrComponent purchaseInvoiceSolrComponent;
    @Autowired
    private ProductsServicesSolrComponent productsServicesSolrComponent;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;
    @Autowired
    private SaleQuoteSolrComponent saleQuoteSolrComponent;
    @Autowired
    private PurchaseOrderSolrComponent purchaseOrderSolrComponent;
    @Autowired
    private ExpenseReportClaimsSolrComponent expenseReportClaimsSolrComponent;
    @Autowired
    private TaskSolrComponent taskSolrComponent;
    @Autowired
    private RequestForQuoteSolrComponent requestForQuoteSolrComponent;
    @Autowired
    private CertificateSolrComponent certificateSolrComponent;
    @Autowired
    private PositionSolrComponent positionSolrComponent;
    @Autowired
    private DepartmentSolrComponent departmentSolrComponent;

    public SelectItem[] getCompanyGroups() {
        List<EdsGroup> groups = groupManager.getCompanyGroups();
        List<SelectItem> items = new ArrayList<>();
        for (EdsGroup group : groups) {
            SelectItem sItem = new SelectItem(group.getObjectID(), commonLocalizer.localize(group.getConstantName(), group.getName()), group.getDescription());
            items.add(sItem);
        }
        return items.toArray(new SelectItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public GroupMembersViewItem getGroupMembers(Integer groupID) {
        GroupMembersViewItem item = new GroupMembersViewItem();
        EdsGroup group = groupManager.get(groupID);
        item.setDefault(group.getEntryType() == EdsObject.DEFAULT);
        item.setGroupConstantName(group.getConstantName());
        item.setGroupDescription(group.getDescription());
        item.setGroupName(commonLocalizer.localize(group.getConstantName(), group.getName()));

        item.setGroupID(group.getObjectID());
        item.setGroupEntryType(group.getEntryType());
        item.setType(group.getType());

        if (group.getOwner() != null && groupManager.getUser().getObjectID().equals(group.getOwner().getTrusteeID())) {
            item.setCanChange(true);
        }
        ArrayList<GroupMemberItem> members = new ArrayList<>();
        Map<String, GroupMemberItem> groupMembers = new HashMap<>();
        for (EdsTrustee member : group.getMembers()) {
            GroupMemberItem mItem = new GroupMemberItem();
            mItem.setType(group.getType());
            mItem.setTrusteeID(member.getTrusteeID());
            mItem.setTrusteeType(member.getType().getObjectID());
            mItem.setTrusteeDescription(member.getType().getDescription());
            if (EdsTrusteeType.USER.equals(member.getType().getObjectID())) {
                EdsUser user = userManager.get(mItem.getTrusteeID());
                mItem.setTrusteeName(user.getName());
            } else if (EdsTrusteeType.GROUP.equals(member.getType().getObjectID())) {
                EdsGroup tGroup = groupManager.get(member.getTrusteeID());
                mItem.setTrusteeName(tGroup.getName());
            }
            groupMembers.put(mItem.getTrusteeName(), mItem);
        }

        SortedSet<String> sortedMembers = new TreeSet<>(groupMembers.keySet());
        for (String s : sortedMembers) {
            members.add(groupMembers.get(s));
        }
        item.setMembers(members.toArray(new GroupMemberItem[]{}));
        return item;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Deprecated
    public ProjectMember[] getGroupMembersList(Integer groupID) {
        List<EdsEmployee> teamEmployees = employeeManager.getCompanyEmployees();
        GroupMembersViewItem group = getGroupMembers(groupID);
        ProjectMember[] members = new ProjectMember[teamEmployees.size()];
        int i = 0;
        for (ProjectMember member : members) {
            member = new ProjectMember();
            member.setId(teamEmployees.get(i).getObjectID());
            member.setName(teamEmployees.get(i).getName());
            if (teamEmployees.get(i).getEmployeeTeam() != null) {
                if (teamEmployees.get(i).getEmployeeTeam().getTeam() != null) {
                    member.setDepartmentId(teamEmployees.get(i).getEmployeeTeam().getTeam().getObjectID());
                    member.setTeamName(teamEmployees.get(i).getEmployeeTeam().getTeam().getName());
                }
            }

            for (int j = 0;
                 j < group.getMembers().length;
                 j++) {
                if (teamEmployees.get(i).getObjectID().equals(group.getMembers()[j].getTrusteeID())) {
                    member.setCheck(true);
                }
            }
            members[i] = member;
            i++;
        }
        return members;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<Integer> getGroupMembersListForTree(Integer groupID) {
        ArrayList<Integer> employeeIDs = new ArrayList<>();
        GroupMembersViewItem group = getGroupMembers(groupID);
        for (GroupMemberItem groupMemberItem : group.getMembers()) {
            employeeIDs.add(groupMemberItem.getTrusteeID());
        }
        return employeeIDs;
    }

    /**
     * <h1>... THIS IS METHOD FILL CASE FACET FILTER DATA ...</h1>
     * <br/>
     * <h2>... WRITE BY DEVELOPER {DILSHOD.T} ...</h2>
     * <br/>
     * <h3>... CREATED DATE {18:10 04/05/2011} ...</h3>
     * <br/>
     *
     * @param caseFacetData
     * @return Case Facet Filter Data
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getCaseFacetFilterData(FacetFilterRpc caseFacetData) {
        if (!caseFacetData.isFilterChanges()) {
            caseFacetData = commonServiceLocal.getUserFacetFilter(caseFacetData);
        }
        EdsCompany company = companyManager.getUser().getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(caseFacetData.getSearchKey());

        String solrQuery = this.commonServiceLocal.getCrmCaseSolrQuery(fp, company, caseFacetData) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(caseFacetData, company, null, null);

        QueryResponse response = getSolrResponse(Constants.SOLR_CASE_CORE, solrQuery, SolrCaseRepresenter.CASE_ID, caseFacetData, true);
        return SolrFacetUtils.fillFacetFilterDataWithNA(response, caseFacetData);
    }

    @Override
    public FacetFilterRpc getByCompanySolrCoreFacetFilter(FacetFilterRpc facetFilter, String solrCoreName) {
        if (!facetFilter.isFilterChanges()) {
            facetFilter = commonServiceLocal.getUserFacetFilter(facetFilter);
        }

        StringBuilder solrQuery = new StringBuilder();
        if (facetFilter.getSearchKey() != null && !"".equals(facetFilter.getSearchKey())) {
            solrQuery.append(SolrTaskRepresenter.FIELD_COMPANY_ID).append(":").append(facetFilter.getSearchKey());
        } else {
            solrQuery.append("*:*");
        }
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQuery(facetFilter, null, null, null));

        QueryResponse response = getSolrResponse(solrCoreName, solrQuery.toString(), null, facetFilter, false, 600);
        SolrFacetUtils.fillFacetFilterData(response, facetFilter);
        SelectItem[] facetItems = facetFilter.getFacetContentMap().get("company").getFacetItems();
        String companyIds = ServerUtils.getSelectItemIdAsCommaDelimeted(facetItems);
        List<EdsCompany> edsCompanyList = companyManager.getCompaniesByIDs(companyIds);
        Map<Integer, EdsCompany> companyMap = new HashMap<>();
        for (EdsCompany edsCompany : edsCompanyList) {
            companyMap.put(edsCompany.getObjectID(), edsCompany);
        }
        for (SelectItem item : facetItems) {
            if (companyMap.containsKey(item.getId())) {
                item.setDescription(companyMap.get(item.getId()).getName() + item.getDescription());
            }
        }
        return facetFilter;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getOpportunityFacetFilterData(FacetFilterRpc opportunityFacetFilter) {
        if (!opportunityFacetFilter.isFilterChanges()) {
            opportunityFacetFilter = commonServiceLocal.getUserFacetFilter(opportunityFacetFilter);
        }
        EdsUser edsUser = userManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();

        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(opportunityFacetFilter.getSearchKey());
        if (opportunityFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_TYPE) != null) {
            if (opportunityFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_ID) != null) {
                String relationType = opportunityFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_TYPE);
                Integer relationID = Integer.parseInt(opportunityFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_ID));
                if (RelationItem.TYPE_CONTACT.equals(relationType)) {
                    fp.setContactID(relationID);
                } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
                    fp.setCrmAccountId(relationID);
                } else if (RelationItem.TYPE_CAMPAIGN.equals(relationType)) {
                    fp.setCampaignID(relationID);
                }
            }
        }

        String[] fields = opportunityFacetFilter.getSolrFieldMapCodeList(FacetContentType.OpportunityFacetFilter.getContentCode()[4]);

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(crmServiceLocal.getOpportunityCoreSolrQuery(edsUser, fp));
        solrQuery.append(SolrFacetUtils.generateSaleInvoiceDuePaidAmountFacet(
                opportunityFacetFilter,
                FacetContentType.OpportunityFacetFilter.getContentCode()[4]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(
                opportunityFacetFilter,
                edsCompany,
                SolrOpportunityRepresenter.FIELD_CLOSING_DATE,
                SolrOpportunityRepresenter.FIELD_CLOSING_DATE,
                FacetContentType.OpportunityFacetFilter.getContentCode()[4]
        ));

        QueryResponse resp = getSolrResponse(Constants.SOLR_OPPORTUNITY_CORE, solrQuery.toString(), SolrOpportunityRepresenter.FIELD_OPPORTUNITY_ID, opportunityFacetFilter, true);
        SolrFacetUtils.fillFacetFilterDataWithNA(resp, opportunityFacetFilter,
                fields
        );
        if (opportunityFacetFilter.getFacetContentMap().containsKey(FacetContentType.OpportunityFacetFilter.getContentCode()[4])) {
            getOpportunityFacetResultFromSolr(resp, opportunityFacetFilter);
        }
        if (opportunityFacetFilter.getFacetContentMap().containsKey(FacetContentType.OpportunityFacetFilter.getContentCode()[0])) {
            for (SelectItem facetItem : opportunityFacetFilter.getFacetContentMap().get(FacetContentType.OpportunityFacetFilter.getContentCode()[0]).getFacetItems()) {
                StringBuilder statusAmountSum = new StringBuilder(solrQuery);
                if (facetItem.getId() == -1) {
                    statusAmountSum.append(" AND (-").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":[* TO *] AND *:*)");
                } else {
                    statusAmountSum.append(" AND ").append(SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID).append(":").append(facetItem.getId());
                }
                facetItem.setTotalAmount(getOpportunityStatusSum(statusAmountSum.toString()));
            }
        }
        return opportunityFacetFilter;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getExpenseReportClaimsFacetFilterData(FacetFilterRpc reportsFacetFilter, String reporterName, boolean isLookup) {
        return expenseReportClaimsSolrComponent.getExpenseReportClaimsFacetFilterData(reportsFacetFilter, reporterName, isLookup);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getEventFacetFilterData(FacetFilterRpc eventFacetFilter, Integer eventType, Integer createdFrom) {
        return eventSolrComponent.getEventFacetFilterData(eventFacetFilter, eventType, createdFrom);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getPurchaseInvoiceFacetFilterData(FacetFilterRpc purchaseFacetFilter) {
        return purchaseInvoiceSolrComponent.getPurchaseInvoiceFacetFilterData(purchaseFacetFilter);
    }

    public Integer saveGroup(GroupMembersViewItem item) throws DuplicateNameException {
        EdsUser user = userManager.getUser();
        EdsGroup edsGroup;
        if (item.getGroupID() != null) {
            edsGroup = groupManager.get(item.getGroupID());
            if (item.getGroupEntryType() != null && item.getGroupEntryType() != Constants.BUILT_IN) {
                edsGroup.setEntryType(EdsObject.CUSTOM);
                edsGroup.setConstantName(item.getGroupName());
            }
            edsGroup.setDescription(item.getGroupDescription());
            edsGroup.setName(item.getGroupName());
            Set<EdsTrustee> edsTrusteeList = edsGroup.getMembers();
            Set<EdsTrustee> removedTrustees = new HashSet<>();
            for (EdsTrustee edsTrustee : edsTrusteeList) {
                boolean isDelete = true;
                for (int i = 0;
                     i < item.getMembers().length;
                     i++) {
                    if (edsTrustee.getTrusteeID().equals(item.getMembers()[i].getTrusteeID())) {
                        isDelete = false;
                        break;
                    }
                }
                if (isDelete) {
                    removedTrustees.add(edsTrustee);
                }
            }
            edsGroup.getMembers().removeAll(removedTrustees);
            for (EdsTrustee tr : removedTrustees) {
                if (EdsTrusteeType.USER.equals(tr.getType().getObjectID())) {
                    EdsUser tUser = userManager.get(tr.getTrusteeID());
                    tUser.getMembershipGroups().remove(edsGroup);
                }
            }
            for (GroupMemberItem member : item.getMembers()) {
                EdsUser edsUser = userManager.get(member.getTrusteeID());
                EdsTrustee trustee = trusteeManager.getTrustee(edsUser);
                edsGroup.getMembers().add(trustee);
                if (EdsTrusteeType.USER.equals(trustee.getType().getObjectID())) {
                    EdsUser tUser = userManager.get(trustee.getTrusteeID());
                    tUser.getMembershipGroups().add(edsGroup);
                }
            }
            groupManager.update(edsGroup);
        } else {
            if (groupManager.existsGroup(user.getObjectID(), item.getGroupName())) {
                throw new DuplicateNameException("A group with the name '" + item.getGroupName() + "' already exists");
            }
            edsGroup = new EdsGroup();
            edsGroup.setEntryType(EdsObject.CUSTOM);
            edsGroup.setConstantName(item.getGroupName());
            edsGroup.setDescription(item.getGroupDescription());
            edsGroup.setName(item.getGroupName());
            edsGroup.setOwner(trusteeManager.getTrustee(userManager.getUser()));
            edsGroup.setType(item.getType());
            for (GroupMemberItem member : item.getMembers()) {
                EdsUser edsUser = userManager.get(member.getTrusteeID());
                EdsTrustee trustee = trusteeManager.getTrustee(edsUser);
                edsGroup.getMembers().add(trustee);
                if (EdsTrusteeType.USER.equals(trustee.getType().getObjectID())) {
                    EdsUser tUser = userManager.get(trustee.getTrusteeID());
                    tUser.getMembershipGroups().add(edsGroup);
                }
            }
            groupManager.create(edsGroup);
        }

        return edsGroup.getObjectID();
    }

    /**
     * <h1>... This is method generated Task Facet Filter ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last update date {20:56 01/06/2011} ...</h3>
     *
     * @param taskFacetFilter
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getTaskFacetFilterData(FacetFilterRpc taskFacetFilter, boolean fromMobile) {
        if (!taskFacetFilter.isFilterChanges()) {
            taskFacetFilter = commonServiceLocal.getUserFacetFilter(taskFacetFilter);
        }
        EdsUser edsUser = userManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();

        if (edsUser.hasRole(Constants.SUPPLIER) && !edsUser.getRoleIds().contains(EdsRole.CLIENT)) {
            QueryBuilderForSolr.supplierRelationForTaskList(taskFacetFilter, edsUser);
        }

        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter fp = new ListingFilterParameter();
        fp.setFromMobile(fromMobile);
        fp.setSearchKey(taskFacetFilter.getSearchKey());
        if (taskFacetFilter.getCustomDataValue(FacetFilterCutomField.PROJECTID) != null) {
            fp.setProjectId(Integer.valueOf(taskFacetFilter.getCustomDataValue(FacetFilterCutomField.PROJECTID)));
        }
        if (taskFacetFilter.getCustomDataValue(FacetFilterCutomField.DEPARTMENTID) != null) {
            fp.setDepartmentId(Integer.valueOf(taskFacetFilter.getCustomDataValue(FacetFilterCutomField.DEPARTMENTID)));
        }

        if (taskFacetFilter.getCustomDataValue(FacetFilterCutomField.DEPARTMENTID) != null) {
            fp.setDepartmentId(Integer.valueOf(taskFacetFilter.getCustomDataValue(FacetFilterCutomField.DEPARTMENTID)));
        }
        if (taskFacetFilter.getCustomDataValue(FacetFilterCutomField.ISCRMTASKLIST) != null) {
            try {
                fp.setCrmTaskList(Boolean.valueOf(taskFacetFilter.getCustomDataValue(FacetFilterCutomField.ISCRMTASKLIST)));
            } catch (Exception e) {
                fp.setCrmTaskList(false);
            }
        }
        fp.setRelationType(taskFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_TYPE));
        try {
            fp.setRelationID(taskFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_ID) != null ? Integer.valueOf(taskFacetFilter.getCustomDataValue(FacetFilterCutomField.RELATION_ID)) : null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        if (fp.isCrmTaskList()) {
            if (taskFacetFilter.getFacetContentMap().containsKey(FacetContentType.TaskFacetFilter.getContentCode()[0])) {
                FacetContentRpc projectFacetContentRpc = taskFacetFilter.getFacetContentMap().get(FacetContentType.TaskFacetFilter.getContentCode()[0]);
                if (projectFacetContentRpc != null && (projectFacetContentRpc.getFacetItems() == null || projectFacetContentRpc.getFacetItems().length == 0)) {
                    EdsProject crmProject = projectManager.getCrmProject();
                    if (crmProject != null) {
                        SelectItem projectSelectItem = new SelectItem(crmProject.getObjectID());
                        projectSelectItem.setSelected(true);
                        projectFacetContentRpc.setFacetItems(new SelectItem[]{projectSelectItem});
                    }
                }
            }
        }

        String[] fields = null;
        boolean isAssigneeStatusShow = taskFacetFilter.getFacetContentMap().containsKey(FacetContentType.TaskFacetFilter.getContentCode()[5])
                && taskFacetFilter.getFacetContentMap().get(FacetContentType.TaskFacetFilter.getContentCode()[5]).getFacetItems().length == 1;
        if (isAssigneeStatusShow) {
            fields = taskFacetFilter.getSolrFieldMapCodeList(FacetContentType.TaskFacetFilter.getContentCode()[5]);
        } else {
            fields = taskFacetFilter.getSolrFieldMapCodeList(FacetContentType.TaskFacetFilter.getContentCode()[5], FacetContentType.TaskFacetFilter.getContentCode()[18]);
        }

        String solrQuery = QueryBuilderForSolr.getTaskCoreSolrQuery(edsUser, edsCompany, taskFacetFilter, fp, groupManager.getCompanyBuiltInGroup(EdsGroup.ADMINISTRATORS)) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(taskFacetFilter, edsCompany, SolrTaskRepresenter.FIELD_START_DATE, SolrTaskRepresenter.FIELD_DUE_DATE,
                        FacetContentType.TaskFacetFilter.getContentCode()[5]) +
                QueryBuilderForSolr.getTaskFacetFilterAssigneesQuery(taskFacetFilter, edsUser);

        SolrClient server = WfmJpaTemplate.getSolrServerForCore(Constants.SOLR_TASK_CORE);
        QueryResponse resp = null;
        resp = getSolrResponse(Constants.SOLR_TASK_CORE, solrQuery, "", taskFacetFilter, true);

        if (taskFacetFilter.getFacetContentMap().containsKey(FacetContentType.TaskFacetFilter.getContentCode()[5])) {
            getTaskFacetFilterAssignees(server, taskFacetFilter, edsUser, edsUser.getMembershipGroups(), edsCompany, fields);
        }

        return SolrFacetUtils.fillFacetFilterDataWithNA(resp, taskFacetFilter, fields);
    }

    /**
     * <h1>... This is method generated Task Facet Filter Assignee Field ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {14:02 04/06/2011} ...</h3>
     *
     * @param server
     * @param taskFacetFilter
     * @param edsUser
     * @param membershipsGroups
     * @param edsCompany
     * @return
     */
    private FacetFilterRpc getTaskFacetFilterAssignees(SolrClient server, FacetFilterRpc taskFacetFilter, EdsUser edsUser, Set<EdsGroup> membershipsGroups, EdsCompany edsCompany, String... codeList) {
        String solrQuery = QueryBuilderForSolr.getTaskAssigneeCoreSolrQuery(edsUser, edsCompany, taskFacetFilter, membershipsGroups) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(taskFacetFilter, edsCompany, SolrTaskRepresenter.FIELD_START_DATE, SolrTaskRepresenter.FIELD_DUE_DATE,
                        FacetContentType.TaskFacetFilter.getContentCode()[5]) +
                QueryBuilderForSolr.getTaskFacetFilterAssigneesQuery(taskFacetFilter, edsUser);

        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);
        query.addFacetField(SolrTaskRepresenter.FIELD_USER_ID_NAME);
        query.setFacetMinCount(1);
        query.setFacet(true);
        query.setFacetMissing(true);
        query.setFacetLimit(WfmJpaTemplate.SOLR_FACET_LIMIT);
        QueryResponse resp = null;

        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        SolrFacetUtils.fillFacetFilterDataWithNA(resp, taskFacetFilter, FacetContentType.TaskFacetFilter.getContentCode()[5]);
        return taskFacetFilter;
    }

    /**
     * <h1>... Lead Facet Filter Assignee Content read in solr and fill Rpc object ...</h1>
     * <br/>
     * <h2>... Write by developer {Hayot.R} ...</h2>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last Updated {19:26 11/06/2011} ...</h3>
     *
     * @param resp                  QueryResponse
     * @param crmAccountFacetFilter FacetFilterRpc
     * @return
     */
    private FacetFilterRpc getCrmAccountFacetResultFromSolr(QueryResponse resp, FacetFilterRpc crmAccountFacetFilter) {
        String dotKey = FacetContentType.CrmAccountFacetFilter.getContentCode()[8];
        /*List<EdsReference> types = referenceManager.listReferences(EdsCrmAccount._CRM_ACCOUNT_TYPE);
        List<SelectItem> selectItems = new ArrayList<>();
        for (EdsReference type : types) {
            selectItems.add(new SelectItem(type.getObjectID(), type.getObjectID() + "@" + type.getName(), type.getName()));
        }*/

        Map<String, String> additionalInformation = getCompanyStatusMap();

        crmAccountFacetFilter.getFacetContentMap().get(dotKey).setFacetItems(
                getFacetItemsByIdOnly(resp.getFacetField(SolrCrmAccountRepresenter.FIELD_BLOCKED),
                        additionalInformation, true)
        );
        return crmAccountFacetFilter;
    }

    private Map<String, String> getCompanyStatusMap() {
        Map<String, String> additionalInformation = new HashMap<>();
        additionalInformation.put("false", "Qualified");
        additionalInformation.put(String.valueOf("_false".hashCode()), "Qualified");
        additionalInformation.put(String.valueOf("_Qualified".hashCode()), "Qualified");
        additionalInformation.put("true", "Unqualified");
        additionalInformation.put(String.valueOf("_true".hashCode()), "Unqualified");
        additionalInformation.put(String.valueOf("_Unqualified".hashCode()), "Unqualified");
        return additionalInformation;
    }

    /**
     * <h1>... Lead Facet Filter Assignee Content read in solr and fill Rpc object ...</h1>
     * <br/>
     * <h2>... Write by developer {Hayot.R} ...</h2>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last Updated {15:02 11/06/2011} ...</h3>
     *
     * @param resp            QueryResponse
     * @param leadFacetFilter FacetFilterRpc
     * @return
     */
    private FacetFilterRpc getLeadFacetAssigneeResultFromSolr(QueryResponse resp, FacetFilterRpc leadFacetFilter) {
        String assigneeKey = FacetContentType.LeadFacetFilter.getContentCode()[5];
        if (leadFacetFilter.getFacetContentMap().containsKey(assigneeKey)) {
            FacetField assigneeFacet = resp.getFacetField(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID_NAME);
            FacetField bassigneeFacet = resp.getFacetField(SolrContactRepresenter.FIELD_LEAD_BACKUP_ASSIGNEE_ID_NAME);
            assigneeFacet = mergeFacetFields(SolrContactRepresenter.FIELD_LEAD_ASSIGNEE_ID_NAME, assigneeFacet, bassigneeFacet);
            resp.getFacetFields().add(/*4, */assigneeFacet);// merged assignee facet field set
            SolrFacetUtils.fillFacetFilterDataWithNA(resp, leadFacetFilter, assigneeKey);
        }
        return leadFacetFilter;
    }

    private FacetField mergeFacetFields(String facedFieldName, FacetField... facetFields) {
        Map<String, Integer> fields = new TreeMap<>();
        if (facetFields != null && facetFields.length > 0) {
            boolean isNullSet = false;//faqat birinchisini nullarini hisoblaymiz tamom boshqalarini tashlavoramiz xolos...
            for (FacetField facetField : facetFields) {
                if (facetField != null) {
                    for (FacetField.Count count : facetField.getValues()) {
                        if (count.getName() != null) {
                            if (fields.containsKey(count.getName())) {
                                fields.put(count.getName(), Integer.valueOf(String.valueOf(fields.get(count.getName()).longValue() + count.getCount())));
                            } else {
                                fields.put(count.getName(), Integer.valueOf(String.valueOf(count.getCount())));
                            }
                        } else if (!isNullSet) {
                            fields.put("N/A", Integer.valueOf(String.valueOf(count.getCount())));
                            isNullSet = true;
                        }
                    }
                }
            }
        }
        FacetField returning = new FacetField(facedFieldName);
        for (Map.Entry<String, Integer> entry : fields.entrySet()) {
            returning.add("N/A".equals(entry.getKey()) ? null : entry.getKey(), entry.getValue());
        }
        return returning;
    }

    /**
     * <h1>... This is methed uses Client and Supplier List Facet Filter ...</h1>
     * <br/>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last Updated {13:52 11/06/2011} ...</h3>
     *
     * @param type
     * @param facetFilterRpc
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getAccountingFacetFilterData(String type, FacetFilterRpc facetFilterRpc) {
        if (LookUpConstants.CLIENT.equals(type)) {
            return getClientFacetFilterData(facetFilterRpc);
        } else if (CrmConstants.SUPPLIER.equals(type)) {
            return getSupplierFacetFilterData(facetFilterRpc);
        }
        return null;
    }

    /**
     * <h1>... This is methed generated Client List Facet Filter Solr Query ...</h1>
     * <br/>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last Updated {13:50 11/06/2011} ...</h3>
     *
     * @param clientFacetFilter
     * @return
     */
    public FacetFilterRpc getClientFacetFilterData(FacetFilterRpc clientFacetFilter) {
        if (!clientFacetFilter.isFilterChanges()) {
            clientFacetFilter = commonServiceLocal.getUserFacetFilter(clientFacetFilter);
        }
        EdsUser edsUser = userManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        EdsReference edsCustomer = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER);
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(clientFacetFilter.getSearchKey());
        StringBuilder solrQuery = new StringBuilder();

        List<Integer> customerIDList = null;
        if (roleManager.hasRole(edsUser, Constants.PM) && !roleManager.hasEitherRoles(edsUser, Constants.ACCOUNTANT, Constants.DR, Constants.ADMIN)) {
            customerIDList = projectManager.getCustomerIDsByProjectManager(edsUser);
        }
        solrQuery.append(QueryBuilderForSolr.getClientListSolrQuery(fp, clientFacetFilter, edsCompany, edsCustomer, customerIDList, edsUser, FacetContentType.ClientFacetFilter.getContentCode()[9]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(clientFacetFilter, edsCompany, SolrCrmAccountRepresenter.FIELD_CREATED_DATE, null, FacetContentType.ClientFacetFilter.getContentCode()[9]));

        QueryResponse resp = getSolrResponse(Constants.SOLR_CRM_ACCOUNT_CORE, solrQuery.toString(), SolrClientRepresenter.FIELD_CLIENT_ID, clientFacetFilter, true);
        SolrFacetUtils.fillFacetFilterDataWithNA(resp, clientFacetFilter);

        if (clientFacetFilter.getFacetContentMap().containsKey(FacetContentType.ClientFacetFilter.getContentCode()[9])) {
            FacetField amountFacet = resp.getFacetField(SolrCrmAccountRepresenter.FIELD_IN_TARGET);
            if (amountFacet != null && amountFacet.getValues() != null) {
                int yes = 0, no = 0;
                for (FacetField.Count count : amountFacet.getValues()) {
                    if ("true".equalsIgnoreCase(count.getName())) {
                        yes += count.getCount();
                    } else {
                        no += count.getCount();
                    }
                }
                SelectItem[] amount = new SelectItem[2];
                amount[0] = new SelectItem("true".hashCode(), "true");
                amount[0].setDescription("Yes  ( <b>" + yes + "</b> )");

                amount[1] = new SelectItem("false".hashCode(), "false");
                amount[1].setDescription("No ( <b>" + no + "</b> )");

                clientFacetFilter.getFacetContentMap().get(FacetContentType.ClientFacetFilter.getContentCode()[9]).setFacetItems(amount);
            } else {
                clientFacetFilter.getFacetContentMap().get(FacetContentType.ClientFacetFilter.getContentCode()[9]).setFacetItems(new SelectItem[0]);
            }
        }
        return clientFacetFilter;
    }

    /**
     * <h1>... This is methed generated Supplier List Facet Filter Solr Query ...</h1>
     * <br/>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last Updated {13:50 11/06/2011} ...</h3>
     *
     * @param supplierFacetFilter
     * @return
     */
    public FacetFilterRpc getSupplierFacetFilterData(FacetFilterRpc supplierFacetFilter) {
        if (!supplierFacetFilter.isFilterChanges()) {
            supplierFacetFilter = commonServiceLocal.getUserFacetFilter(supplierFacetFilter);
        }
        EdsUser edsUser = userManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(supplierFacetFilter.getSearchKey());
        EdsReference edsSupplier = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.SUPPLIER);

        StringBuilder solrQuery = new StringBuilder();

        String[] customFullAccessRoles = rolePermissionManager.getRolesByPermissionCode(LayoutRPC.LOGISTICS_SECTION.equals(supplierFacetFilter.getName()) ? PermissionConstants.LOGISTICS_SUPPLIER_FULL_LIST_ACCESS : PermissionConstants.ACCOUNTING_SUPPLIER_FULL_LIST_ACCESS).toArray(new String[]{});
        solrQuery.append(QueryBuilderForSolr.getSupplierListSolrQuery(fp, supplierFacetFilter, edsCompany, edsSupplier, edsUser, customFullAccessRoles, FacetContentType.SupplierFacetFilter.getContentCode()[8]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(supplierFacetFilter, edsCompany, SolrCrmAccountRepresenter.FIELD_CREATED_DATE, null, FacetContentType.SupplierFacetFilter.getContentCode()[8]));

        QueryResponse resp = getSolrResponse(Constants.SOLR_CRM_ACCOUNT_CORE, solrQuery.toString(), SolrSupplierRepresenter.FIELD_SUPPLIER_ID, supplierFacetFilter, true);
        SolrFacetUtils.fillFacetFilterDataWithNA(resp, supplierFacetFilter);

        if (supplierFacetFilter.getFacetContentMap().containsKey(FacetContentType.SupplierFacetFilter.getContentCode()[8])) {
            FacetField amountFacet = resp.getFacetField(SolrCrmAccountRepresenter.FIELD_IN_TARGET);
            if (amountFacet != null && amountFacet.getValues() != null) {
                int yes = 0, no = 0;
                for (FacetField.Count count : amountFacet.getValues()) {
                    if ("true".equalsIgnoreCase(count.getName())) {
                        yes += count.getCount();
                    } else {
                        no += count.getCount();
                    }
                }
                SelectItem[] amount = new SelectItem[2];
                amount[0] = new SelectItem("true".hashCode(), "true");
                amount[0].setDescription("Yes  ( <b>" + yes + "</b> )");

                amount[1] = new SelectItem("false".hashCode(), "false");
                amount[1].setDescription("No ( <b>" + no + "</b> )");

                supplierFacetFilter.getFacetContentMap().get(FacetContentType.SupplierFacetFilter.getContentCode()[8]).setFacetItems(amount);
            } else {
                supplierFacetFilter.getFacetContentMap().get(FacetContentType.SupplierFacetFilter.getContentCode()[8]).setFacetItems(new SelectItem[0]);
            }
        }
        return supplierFacetFilter;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getCRMFacetFilterData(String type, FacetFilterRpc facetFilterRpc) {
        if (LookUpConstants.CRM_ACCOUNT.equals(type)) {
            return crmAccountSolrComponent.getCrmAccountFacetFilterData(facetFilterRpc);
        } else if (LookUpConstants.CRM_CONTACT.equals(type)) {
            return contactSolrComponent.getContactFacetFilterData(facetFilterRpc);
        } else if (LookUpConstants.CRM_LEAD.equals(type)) {
            return contactSolrComponent.getLeadFacetFilterData(facetFilterRpc);
        } else if (LookUpConstants.CANDIDATE.equals(type)) {
            return contactSolrComponent.getCandidateFacetFilterData(facetFilterRpc);
        } else if (CrmConstants.CRM_CASE.equals(type)) {
            return caseSolrComponent.getCaseFacetFilterData(facetFilterRpc);
        }
        return null;
    }

    private SelectItem[] getFacetItemsByIdOnly(FacetField facetField, Map<String, String> additionalInformation, boolean forceToChangeUiName) {
        if (facetField != null && facetField.getValues() != null) {
            ArrayList<SelectItem> parents = new ArrayList<>();
            SelectItem nAItem = null;
            for (FacetField.Count count : facetField.getValues()) {
                if (count.getName() == null) {
                    if (count.getCount() > 0) {
                        nAItem = new SelectItem();
                        nAItem.setName("N/A");
                        nAItem.setDescription("N/A ( <b>" + count.getCount() + "</b> )");
                        nAItem.setId(-1);
                        nAItem.setTotalCount(count.getCount());
                    }
                } else {
                    SelectItem sItem = null;
                    Integer id = null;
                    if (count.getName().matches(Constants.REGEX_INTEGER)) {
                        try {
                            id = Integer.parseInt(count.getName());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    String name = null;
                    if (id != null) {
                        if (additionalInformation.containsKey(id.toString())) {
                            sItem = new SelectItem(id, additionalInformation.get(id.toString()));
                        }
                    } else {
                        sItem = new SelectItem(("_" + count.getName()).hashCode(), count.getName());
                        if (additionalInformation.containsKey(count.getName().toLowerCase())) {
                            name = additionalInformation.get(count.getName().toLowerCase());
                            sItem.setName(name);
                        }
                    }
                    if (sItem != null) {
                        sItem.setTotalCount(count.getCount());
                        sItem.setDescription((forceToChangeUiName && name != null ? name : sItem.getName()) + " ( <b>" + count.getCount() + "</b> )");
                        parents.add(sItem);
                    }
                }
            }
            SelectItem[] items = parents.toArray(new SelectItem[]{});
            items = ServerUtils.sortSelectItem(items);
            if (nAItem != null) {
                SelectItem[] nItems = items;
                items = new SelectItem[nItems.length + 1];
                int n = 0;
                for (SelectItem item : nItems) {
                    items[n] = item;
                    n++;
                }
                items[n] = nAItem;
            }

            return items;
        } else {
            return new SelectItem[0];
        }
    }

    private FacetFilterRpc applyNonConvertedFilterPeriod(FacetFilterRpc facet) {
        HashMap<String, String> customData = facet.getCustomData();
        if (customData.get(Constants.STARTDATE_NC) != null) {
            facet.setStartDate(ServerUtils.parseFilterParameterDate(customData.get(Constants.STARTDATE_NC)));
        }
        if (customData.get(Constants.ENDDATE_NC) != null) {
            facet.setEndDate(ServerUtils.parseFilterParameterDate(customData.get(Constants.ENDDATE_NC)));
        }
        return facet;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getSaleInvoiceFacetFilterData(FacetFilterRpc invoiceFacet) {
        return saleInvoiceSolrComponent.getSaleInvoiceFacetFilterData(invoiceFacet);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getSaleQuoteFacetFilterData(FacetFilterRpc quoteFacet) {
        return saleQuoteSolrComponent.getSaleQuoteFacetFilterData(quoteFacet);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getSaleOrderFacetFilterData(FacetFilterRpc quoteFacet) {
        return saleQuoteSolrComponent.getSaleOrderFacetFilterData(quoteFacet);
    }

    @Override
    public FacetFilterRpc getGdnGrnFacetFilterData(FacetFilterRpc facetFilterRpc, boolean isGdn) {
        return shippingDataSolrComponent.getGdnGrnFacetFilterData(facetFilterRpc, isGdn);
    }

    @Override
    public FacetFilterRpc getCertificateFacetFilterData(FacetFilterRpc facetFilterRpc) {
        return certificateSolrComponent.getCertificateFilterData(facetFilterRpc);
    }

    @Override
    public FacetFilterRpc getPositionFacetFilterData(FacetFilterRpc facetFilterRpc) {
        return positionSolrComponent.getPositionFacetFilterData(facetFilterRpc);
    }

    @Override
    public FacetFilterRpc getDepartmentFacetFilterData(FacetFilterRpc facetFilterRpc) {
        return departmentSolrComponent.getDepartmentFacetFilterData(facetFilterRpc);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getPurchaseOrderFacetFilterData(FacetFilterRpc orderFacet) {
        return purchaseOrderSolrComponent.getPurchaseOrderFacetFilterData(orderFacet);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getProductsServicesFacetFilterData(FacetFilterRpc productsFacet) {
        return productsServicesSolrComponent.getProductServicesFacetFilterData(productsFacet);
    }

    @Override
    public FacetFilterRpc getCourseBookingFacetFilterData(FacetFilterRpc data) {
        if (!data.isFilterChanges()) {
            data = commonServiceLocal.getUserFacetFilter(data);
        }

        StringBuilder solrQuery = new StringBuilder();
        EdsUser user = employeeManager.getUser();
        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(data.getSearchKey());

        solrQuery.append(QueryBuilderForSolr.getCourseBookingCoreSolrQuery(fp));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(data, user.getCompany(), null, null, null));

        QueryResponse resp = getSolrResponse(Constants.SOLR_COURSE_BOOKING_CORE, solrQuery.toString(), SolrCourseBookingRepresenter.FIELD_COURSE_BOOKING_ID, data, true);
        SolrFacetUtils.fillFacetFilterDataWithNA(resp, data);
        return data;
    }

    @Override
    public FacetFilterRpc getCourseScheduleFacetFilterData(FacetFilterRpc data) {
        if (!data.isFilterChanges()) {
            data = commonServiceLocal.getUserFacetFilter(data);
        }

        StringBuilder solrQuery = new StringBuilder();
        EdsUser user = employeeManager.getUser();
        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(data.getSearchKey());

        solrQuery.append(QueryBuilderForSolr.getCourseScheduleCoreSolrQuery(fp));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(data, user.getCompany(), null, null, null));

        QueryResponse resp = getSolrResponse(Constants.SOLR_COURSE_SCHEDULE_CORE, solrQuery.toString(), SolrCourseScheduleRepresenter.FIELD_COURSE_SCHEDULE_ID, data, true);
        SolrFacetUtils.fillFacetFilterDataWithNA(resp, data);
        return data;
    }
    private FacetFilterRpc getSaleQuoteFacetResultFromSolr(QueryResponse resp, FacetFilterRpc quoteFacet) {
        int num = 0;
        FacetField amountFacet = resp.getFacetField(SolrSaleInvoiceRepresenter.FIELD_DUE_AMOUNT);
        if (amountFacet != null && amountFacet.getValues() != null) {
            num = 0;
            int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (FacetField.Count count : amountFacet.getValues()) {
                if (count.getName() != null) {
                    Double total = Double.parseDouble(count.getName());
                    if (total < 100) {
                        lessThan100 += count.getCount();
                    } else if (100 <= total && total <= 1000) {
                        from100To1000 += count.getCount();
                    } else if (1001 <= total && total <= 10000) {
                        from1000To10000 += count.getCount();
                    } else if (10001 <= total && total <= 50000) {
                        from10000To50000 += count.getCount();
                    } else {
                        moreThan50000 += count.getCount();
                    }
                }
            }
            SelectItem[] amount = new SelectItem[5];
            amount[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            amount[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00  ( <b>" + lessThan100 + "</b> )");

            amount[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            amount[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

            amount[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            amount[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

            amount[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            amount[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

            amount[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            amount[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

            quoteFacet.getFacetContentMap().get(FacetContentType.SaleQuoteFacetFilter.getContentCode()[2]).setFacetItems(amount);
        } else {
            quoteFacet.getFacetContentMap().get(FacetContentType.SaleQuoteFacetFilter.getContentCode()[2]).setFacetItems(new SelectItem[0]);
        }
        return quoteFacet;
    }

    private FacetFilterRpc getPurchaseInvoiceFacetResultFromSolr(QueryResponse resp, FacetFilterRpc purchaseFacet) {
        int num = 0;
        FacetField amountFacet = resp.getFacetField(SolrPurchaseInvoiceRepresenter.FIELD_DUE_AMOUNT);
        if (amountFacet != null && amountFacet.getValues() != null) {
            num = 0;
            int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (FacetField.Count count : amountFacet.getValues()) {
                if (count.getName() != null) {
                    Double total = Double.parseDouble(count.getName());
                    if (total < 100) {
                        lessThan100 += count.getCount();
                    } else if (100 <= total && total <= 1000) {
                        from100To1000 += count.getCount();
                    } else if (1001 <= total && total <= 10000) {
                        from1000To10000 += count.getCount();
                    } else if (10001 <= total && total <= 50000) {
                        from10000To50000 += count.getCount();
                    } else {
                        moreThan50000 += count.getCount();
                    }
                }
            }
            SelectItem[] amount = new SelectItem[5];
            amount[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            amount[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00  ( <b>" + lessThan100 + "</b> )");

            amount[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            amount[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

            amount[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            amount[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

            amount[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            amount[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

            amount[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            amount[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

            purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4]).setFacetItems(amount);
        } else {
            purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[4]).setFacetItems(new SelectItem[0]);
        }

        FacetField paidAmountFacet = resp.getFacetField(SolrPurchaseInvoiceRepresenter.FIELD_PAID_AMOUNT);
        if (paidAmountFacet != null && paidAmountFacet.getValues() != null) {
            num = 0;
            int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (FacetField.Count count : paidAmountFacet.getValues()) {
                if (count.getName() != null) {
                    Double total = Double.parseDouble(count.getName());
                    if (total < 100) {
                        lessThan100 += count.getCount();
                    } else if (100 <= total && total <= 1000) {
                        from100To1000 += count.getCount();
                    } else if (1001 <= total && total <= 10000) {
                        from1000To10000 += count.getCount();
                    } else if (10001 <= total && total <= 50000) {
                        from10000To50000 += count.getCount();
                    } else {
                        moreThan50000 += count.getCount();
                    }
                }
            }
            SelectItem[] paidAmount = new SelectItem[5];
            paidAmount[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            paidAmount[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00 ( <b>" + lessThan100 + "</b> )");

            paidAmount[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            paidAmount[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

            paidAmount[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            paidAmount[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

            paidAmount[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            paidAmount[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

            paidAmount[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            paidAmount[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

            purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[5]).setFacetItems(paidAmount);
        } else {
            purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[5]).setFacetItems(new SelectItem[0]);
        }
        if (purchaseFacet.getFacetContentMap().containsKey(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6])) {
            FacetField creditNoteFacet = resp.getFacetField(SolrPurchaseInvoiceRepresenter.FIELD_IS_CREDIT_NOTE);
            if (creditNoteFacet != null && creditNoteFacet.getValues() != null) {
                int yes = 0, no = 0;
                for (FacetField.Count count : creditNoteFacet.getValues()) {
                    if ("true".equalsIgnoreCase(count.getName())) {
                        yes += count.getCount();
                    } else {
                        no += count.getCount();
                    }
                }
                SelectItem[] type = new SelectItem[2];
                type[0] = new SelectItem("true".hashCode(), "true");
                type[0].setDescription("Debit Notes  ( <b>" + yes + "</b> )");

                type[1] = new SelectItem("false".hashCode(), "false");
                type[1].setDescription("Purchase Invoices ( <b>" + no + "</b> )");

                purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]).setFacetItems(type);
            } else {
                if (purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]) != null) {
                    purchaseFacet.getFacetContentMap().get(FacetContentType.PurchaseInvoiceFacetFilter.getContentCode()[6]).setFacetItems(new SelectItem[0]);
                }
            }
        }
        return purchaseFacet;
    }

    private FacetFilterRpc getOpportunityFacetResultFromSolr(QueryResponse resp, FacetFilterRpc opportunityFacet) {
        int num = 0;
        FacetField amountFacet = resp.getFacetField(SolrOpportunityRepresenter.FIELD_AMOUNT);
        if (amountFacet != null && amountFacet.getValues() != null) {
            num = 0;
            int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (FacetField.Count count : amountFacet.getValues()) {
                if (count.getName() != null) {
                    Double total = Double.parseDouble(count.getName());
                    if (total < 100) {
                        lessThan100 += count.getCount();
                    } else if (100 <= total && total <= 1000) {
                        from100To1000 += count.getCount();
                    } else if (1001 <= total && total <= 10000) {
                        from1000To10000 += count.getCount();
                    } else if (10001 <= total && total <= 50000) {
                        from10000To50000 += count.getCount();
                    } else {
                        moreThan50000 += count.getCount();
                    }
                }
            }
            SelectItem[] amount = new SelectItem[5];
            amount[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            amount[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00  ( <b>" + lessThan100 + "</b> )");
            amount[0].setTotalCount((long) lessThan100);

            amount[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            amount[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");
            amount[1].setTotalCount((long) from100To1000);

            amount[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            amount[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");
            amount[2].setTotalCount((long) from1000To10000);

            amount[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            amount[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");
            amount[3].setTotalCount((long) from10000To50000);

            amount[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            amount[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");
            amount[4].setTotalCount((long) moreThan50000);

            opportunityFacet.getFacetContentMap().get(FacetContentType.OpportunityFacetFilter.getContentCode()[4]).setFacetItems(amount);
        } else {
            opportunityFacet.getFacetContentMap().get(FacetContentType.OpportunityFacetFilter.getContentCode()[4]).setFacetItems(new SelectItem[0]);
        }

        return opportunityFacet;
    }

    private FacetFilterRpc getExpenseFacetResultFromSolr(QueryResponse resp, FacetFilterRpc expenseFacet) {
        int num = 0;
        FacetField amountFacet = resp.getFacetField(SolrExpenseReportRepresenter.FIELD_ORIGINAL_AMOUNT);
        if (amountFacet != null && amountFacet.getValues() != null) {
            num = 0;
            int lessThan100 = 0, from100To1000 = 0, from1000To10000 = 0, from10000To50000 = 0, moreThan50000 = 0;
            for (FacetField.Count count : amountFacet.getValues()) {
                if (count.getName() != null) {
                    Double total = Double.parseDouble(count.getName());
                    if (total < 100) {
                        lessThan100 += count.getCount();
                    } else if (100 <= total && total <= 1000) {
                        from100To1000 += count.getCount();
                    } else if (1001 <= total && total <= 10000) {
                        from1000To10000 += count.getCount();
                    } else if (10001 <= total && total <= 50000) {
                        from10000To50000 += count.getCount();
                    } else {
                        moreThan50000 += count.getCount();
                    }
                }
            }
            SelectItem[] amount = new SelectItem[5];
            amount[0] = new SelectItem("[ * TO 99 ]".hashCode(), "[ * TO 99 ]");
            amount[0].setDescription(commonLocalizer.localize("lessThan", "Less than") + " 100.00  ( <b>" + lessThan100 + "</b> )");

            amount[1] = new SelectItem("[100 TO 1000]".hashCode(), "[100 TO 1000]");
            amount[1].setDescription("100.00 - 1,000.00 ( <b>" + from100To1000 + "</b> )");

            amount[2] = new SelectItem("[1001 TO 10000]".hashCode(), "[1001 TO 10000]");
            amount[2].setDescription("1,001.00 - 10,000.00 ( <b>" + from1000To10000 + "</b> )");

            amount[3] = new SelectItem("[10001 TO 50000]".hashCode(), "[10001 TO 50000]");
            amount[3].setDescription("10,001.00 - 50,000.00 ( <b>" + from10000To50000 + "</b> )");

            amount[4] = new SelectItem("[50001 TO *]".hashCode(), "[50001 TO *]");
            amount[4].setDescription(commonLocalizer.localize("moreThan", "More than") + " 50,000.00 ( <b>" + moreThan50000 + "</b> )");

            expenseFacet.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6]).setFacetItems(amount);
        } else {
            expenseFacet.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[6]).setFacetItems(new SelectItem[0]);
        }

        FacetField expanse = resp.getFacetField(SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE);
        if (expanse != null && expanse.getValues() != null) {
            int yes = 0, no = 0;
            for (FacetField.Count count : expanse.getValues()) {
                if ("false".equalsIgnoreCase(count.getName())) {
                    yes += count.getCount();
                } else {
                    no += count.getCount();
                }
            }
            SelectItem[] type = new SelectItem[2];
            type[0] = new SelectItem("false".hashCode(), "false");
            type[0].setDescription("Employee Expenses  ( <b>" + yes + "</b> )");

            type[1] = new SelectItem("true".hashCode(), "true");
            type[1].setDescription("Company Expenses ( <b>" + no + "</b> )");

            expenseFacet.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[9]).setFacetItems(type);
        } else {
            if (expenseFacet.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[9]) != null) {
                expenseFacet.getFacetContentMap().get(FacetContentType.ExpenseReportsClaimsFacetFilter.getContentCode()[9]).setFacetItems(new SelectItem[0]);
            }
        }
        return expenseFacet;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<GroupMembersViewItem> getCompanyGroupsWithMembers() {
        EdsUser user = userManager.getUser();
        List<EdsGroup> groups = groupManager.getUserGroups(user.getObjectID());
        ArrayList<GroupMembersViewItem> groupMembersViewItemList = new ArrayList<>();
        for (EdsGroup group : groups) {
            groupMembersViewItemList.add(getGroupMembers(group.getObjectID()));
        }
        return groupMembersViewItemList;
    }

    @Override
    public FacetFilterRpc getBenefitRequestsFacetFilterData(FacetFilterRpc benefitRequestsData) {
        if (!benefitRequestsData.isFilterChanges()) {
            benefitRequestsData = commonServiceLocal.getUserFacetFilter(benefitRequestsData);
        }
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(benefitRequestsData.getSearchKey());
        fp.setViewType(benefitRequestsData.getName());

      return null;

    }

    @Transactional
    public void fixInconsistenciesInDb(Integer companyID) {
        System.out.println("Fixing DB - > SOLR inconsistences started for companyID = " + companyID);

        Integer start = 0;
        // first iteratively will fix task inconsistencies in DB
        try {
            while (start != -1) {
                start = backendServiceLocal.fixTaskInconsistenciesInDb(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix DB - > SOLR inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixInconsistenciesInSolr(Integer companyID) {
        System.out.println("Fixing SOLR - > DB inconsistences started for companyID = " + companyID);
        Integer start = 0;
        // first iteratively will fix task inconsistencies in Solr
        try {
            while (start != -1) {
                start = backendServiceLocal.fixTaskInconsistenciesInSolr(companyID, start);
            }
        } catch (Exception ex) {
            System.out.println("Failed fix SOLR - > DB inconsistence for companyID = " + companyID);
        }
    }

    @Transactional
    public void fixInconsistencesInSolrAndDB(Integer companyID) {
        ServerSecurityContext.getInstance().setCompanyId(companyID.toString());
        fixInconsistenciesInSolr(companyID);
        fixInconsistenciesInDb(companyID);
    }

    @Transactional
    public void fixInconsistencesInSolrAndDBForAllCompanies() {
        List<Integer> companys = solrDbConsistencyManager.getComapanyIDsWithInconsistenct();
        List<String> schemas = companyManager.getExistingSchemas();
        for (Integer companyid : companys) {
            if (schemas.contains(companyid.toString())) {
                fixInconsistencesInSolrAndDB(companyid);
            } else {
                solrDbConsistencyManager.removeInconsistences(companyid, EdsSolrDbConsistency.TASK);
            }
        }
    }

    /**
     * <h1>... This is method generated Project List FacetFilter data ...</h1>
     * <br/>
     * <h2>... Write by {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {20:09 13/06/2011} ...</h3>
     *
     * @param projectFacetFilter
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getProjectFacetFilterData(FacetFilterRpc projectFacetFilter) {
        return projectSolrComponent.getProjectFacetFilterData(projectFacetFilter);
    }

    /**
     * <h1>... News List Facet Filter generated method ...</h1>
     * <br/>
     * <h2>... Write by developer {Shershod.Suyarqulov} ...</h2>
     * <h2>... Changed by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Last Updated {15:51 13/06/2011}  ...</h3>
     *
     * @param newsFacetFilter
     * @param isBlog
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getNewsFacetFilterRpc(FacetFilterRpc newsFacetFilter, boolean isBlog) {
        if (!newsFacetFilter.isFilterChanges()) {
            newsFacetFilter = commonServiceLocal.getUserFacetFilter(newsFacetFilter);
        }
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter lfp = new ListingFilterParameter();
        lfp.setSearchKey(newsFacetFilter.getSearchKey());
        String solrQuery = QueryBuilderForSolr.getNewsListSolrQuery(lfp, edsCompany, isBlog) +
                SolrFacetUtils.generatedFacetFilterSolrQuery(newsFacetFilter, edsCompany, SolrNewsRepresenter.FIELD_DATE, null);

        QueryResponse resp = getSolrResponse(Constants.SOLR_NEWS_CORE, solrQuery, SolrNewsRepresenter.FIELD_NEWS_ID, newsFacetFilter);

        // do not show news that can't be viewed by user
        int z = 0;
        int i = 0;
        for (SolrDocument doc : resp.getResults()) {
            boolean canView = false;
            EdsNews news = newsManager.get(SolrUtils.asInteger(doc, SolrNewsRepresenter.FIELD_NEWS_ID));
            if (news != null && news.getNetworks() != null && news.getNetworks().size() != 0) {
                for (EdsNetwork network : news.getNetworks()) {
                    if (network.getType().equals("Private (Invitation to Join)")) {
                        z++;
                    }
                }
                if (z == news.getNetworks().size()) {
                    for (EdsNetwork network : news.getNetworks()) {
                        if (network.getCreator() != null && network.getCreator().getObjectID().equals(userManager.getUser().getObjectID())) {
                            canView = true;
                            break;
                        }
                        for (EdsNetworkContact contact : network.getContacts()) {
                            if (contact.getUserContactID().equals(userManager.getUser().getObjectID())) {
                                if (!contact.isPending()) {
                                    canView = true;
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    canView = true;
                }
            } else {
                canView = true;
            }
            if (!canView) {
                //decreasing count
                String ownerId = (String) resp.getResults().get(i).get(SolrNewsRepresenter.FIELD_NEWS_OWNER);
                for (int b = 0;
                     b < resp.getFacetField(SolrNewsRepresenter.FIELD_NEWS_OWNER).getValues().size();
                     b++) {
                    if (resp.getFacetField(SolrNewsRepresenter.FIELD_NEWS_OWNER).getValues().get(b).getName().equals(ownerId)) {
                        long count = resp.getFacetField(SolrNewsRepresenter.FIELD_NEWS_OWNER).getValues().get(b).getCount();
                        resp.getFacetField(SolrNewsRepresenter.FIELD_NEWS_OWNER).getValues().get(b).setCount(count - 1);
                    }
                }
                resp.getResults().remove(i);

            }
            i++;
        }
        return SolrFacetUtils.fillFacetFilterData(resp, newsFacetFilter);
    }

    /**
     * <h1>... This is method generated Facet Filter Solr Response  ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {17:15 16/03/2012} ...</h3>
     *
     * @param solrCore
     * @param solrQuery
     * @param collapseFieldId
     * @param facetFilterData
     * @return
     */
    private QueryResponse getSolrResponse(String solrCore, String solrQuery, String collapseFieldId, FacetFilterRpc facetFilterData) {
        return getSolrResponse(solrCore, solrQuery, collapseFieldId, facetFilterData, false, -1);
    }

    /**
     * <h1>... This is method generated Facet Filter Solr Response  ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {21:53 10/06/2011} ...</h3>
     *
     * @param solrCore
     * @param solrQuery
     * @param collapseFieldId
     * @param facetFilterData
     * @return
     */
    private QueryResponse getSolrResponse(String solrCore, String solrQuery, String collapseFieldId, FacetFilterRpc facetFilterData, boolean isMissing) {
        return getSolrResponse(solrCore, solrQuery, collapseFieldId, facetFilterData, isMissing, -1);
    }

    /**
     * <h1>... This is method generated Facet Filter Solr Response  ...</h1>
     * <br/>
     * <h2>... Write by developer {Dilshod.T} ...</h2>
     * <br/>
     * <h3>... Created date {16:40 08/06/2011} ...</h3>
     *
     * @param solrCore
     * @param solrQuery
     * @param collapseFieldId
     * @param facetFilterData
     * @param isMissing
     * @return QueryResponse
     */
    private QueryResponse getSolrResponse(String solrCore, String solrQuery, String collapseFieldId, FacetFilterRpc facetFilterData, boolean isMissing, int facetLimit) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(solrCore);
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);

        for (String key : facetFilterData.getShowSolrFieldMap().keySet()) {
            query.addFacetField(facetFilterData.getShowSolrFieldMap().get(key).getSolrFacetFieldName());
        }
        query.setFacetMinCount(1);
        query.setFacet(true);

        if (Constants.SOLR_TASK_CORE.equals(solrCore)) {
            query.set(GroupParams.GROUP, true);
            query.set(GroupParams.GROUP_TRUNCATE, true);
            query.set(GroupParams.GROUP_MAIN, true);
            query.set(GroupParams.GROUP_FIELD, SolrTaskRepresenter.FIELD_TASK_ID);
        }

        if (facetLimit != -1) {
            query.setFacetLimit(facetLimit);
        } else {
            query.setFacetLimit(WfmJpaTemplate.SOLR_FACET_LIMIT);
        }

        if (isMissing) {
            query.setFacetMissing(true);
        }

        QueryResponse resp = null;
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public Double getOpportunityStatusSum(String solrQuery/*, String collapseFieldId, FacetFilterRpc facetFilterData, boolean isMissing, int facetLimit*/) {
        SolrClient server = WfmJpaTemplate.getSolrServerForCore(Constants.SOLR_OPPORTUNITY_CORE);
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);

        query.setFacetMinCount(1);
        query.setFacet(true);

        query.addGetFieldStatistics(SolrOpportunityRepresenter.FIELD_AMOUNT_BASE_CURRENCY);

        QueryResponse statusAmountSumResp = null;
        try {
            statusAmountSumResp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        if (statusAmountSumResp != null && statusAmountSumResp.getFieldStatsInfo() != null && statusAmountSumResp.getFieldStatsInfo().containsKey(SolrOpportunityRepresenter.FIELD_AMOUNT_BASE_CURRENCY)) {
            return (Double) (statusAmountSumResp.getFieldStatsInfo().get(SolrOpportunityRepresenter.FIELD_AMOUNT_BASE_CURRENCY).getSum());
        } else {
            return null;
        }

//        return statusAmountSumResp;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getDocumentFacetFilterData(FacetFilterRpc documentFacetData, String typeCode) {
        if (!documentFacetData.isFilterChanges()) {
            documentFacetData = commonServiceLocal.getUserFacetFilter(documentFacetData);
        }
        EdsCompany company = companyManager.getUser().getCompany();
        Set<EdsGroup> membershipsGroups = companyManager.getUser().getMembershipGroups();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(documentFacetData.getSearchKey());
        fp.setCrmEntityId(documentFacetData.getObjectID());
        fp.setModule(LayoutRPC.HRMS_SECTION);
        fp.setFolderType(documentFacetData.getTypeId());
        if (documentFacetData.getTypeId() != null) {
            EdsFolder folder = folderManager.getFolderByFolderType(documentFacetData.getTypeId());
            if (folder != null) {
                fp.setFolderId(folder.getObjectID());
            }
        }
        if (typeCode != null && !"".equals(typeCode)) {
            fp.setViewType(typeCode);
            EdsReference employeeDocType = referenceManager.findReference(EdsFileHeader._DOCUMENT_TYPES, typeCode);
            if (employeeDocType != null) {
                fp.setType(employeeDocType.getObjectID());
            }
        }
        String solrQuery = QueryBuilderForSolr.getDocumentsSolrCore(fp, companyManager.getUser(), company, membershipsGroups) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(documentFacetData, company, SolrFolderRepresenter.FIELD_DATE_CREATION, SolrFolderRepresenter.FIELD_DATE_CREATION);

        QueryResponse response = getSolrResponse(Constants.SOLR_FOLDER_CORE, solrQuery, SolrFolderRepresenter.FIELD_FOLDER_ID, documentFacetData, true);
        return SolrFacetUtils.fillFacetFilterDataWithNA(response, documentFacetData);
    }

    @Override
    public FacetFilterRpc getEmployeeFacetFilterData(FacetFilterRpc employeeFacet) {
        return employeeSolrComponent.getEmployeeFacetFilterData(employeeFacet);
    }

    @Override
    public FacetFilterRpc getEmployeeAnnualBalanceFacetFilterData(FacetFilterRpc employeeFacet) {
        EdsUser user = userManager.getUser();
        if (!employeeFacet.isFilterChanges()) {
            employeeFacet = commonServiceLocal.getUserFacetFilter(employeeFacet);
        }
        EdsCompany company = companyManager.getUser().getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(employeeFacet.getSearchKey());
        fp.setViewType(employeeFacet.getName());
        fp.setModule(PermissionConstants.HRMS_CONTEXT);

        boolean showAllEmployees = ServerUtils.hasPermission(PermissionConstants.SHOW_ALL_EMPLOYEE_LIST);

        List<Integer> departmentList = Lists.newArrayList();
        if (!showAllEmployees && ServerUtils.hasPermission(PermissionConstants.SHOW_DEPARTMENT_EMPLOYEE_LIST)) {
            List<EdsDepartment> edsDepartments = departmentManager.getTeamsByEmployeeId(user.getObjectID());
            departmentList.addAll(edsDepartments.stream().map(EdsDepartment::getObjectID).toList());
        }

        String solrQuery = QueryBuilderForSolr.getEmployeeSolrQuery(fp, user, departmentList) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(employeeFacet, company, SolrEmployeeRepresenter.FIELD_CREATED_DATE, SolrEmployeeRepresenter.FIELD_CREATED_DATE);

        QueryResponse response = getSolrResponse(Constants.SOLR_EMPLOYEE_CORE, solrQuery, SolrEmployeeRepresenter.FIELD_EMPLOYEE_ID, employeeFacet, true);
        return SolrFacetUtils.fillFacetFilterDataWithNA(response, employeeFacet);
    }

    private void getSelectItemsWithLocaleName(FacetFilterRpc facetFilterRpc) {
        if (facetFilterRpc.getFacetContentMap().get("department") == null ||
                facetFilterRpc.getFacetContentMap().get("department").getFacetItems() == null)
            return;

        SelectItem[] items = facetFilterRpc.getFacetContentMap().get("department").getFacetItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getId() == -1) continue;
            String departmentLocaleName = departmentManager.get(items[i].getId()).getName();
            items[i].setName(departmentLocaleName);
            items[i].setDescription(departmentLocaleName + " (<b> " + items[i].getTotalCount() + " </b>)");
        }
        if (facetFilterRpc.getFacetContentMap().get("position") == null ||
                facetFilterRpc.getFacetContentMap().get("position").getFacetItems() == null)
            return;
        SelectItem[] postionItems = facetFilterRpc.getFacetContentMap().get("position").getFacetItems();
        for (int i = 0; i < postionItems.length; i++) {
            if (postionItems[i].getId() == -1) continue;
            String positionLocaleName = positionManager.get(postionItems[i].getId()).getName();
            postionItems[i].setName(positionLocaleName);
            postionItems[i].setDescription(positionLocaleName + " (<b> " + postionItems[i].getTotalCount() + " </b>)");
        }
    }

    @Override
    public FacetFilterRpc getLeaveFacetFilterData(FacetFilterRpc facetFilterRpc) {
        EdsUser user = userManager.getUser();
        if (!facetFilterRpc.isFilterChanges()) {
            facetFilterRpc = commonServiceLocal.getUserFacetFilter(facetFilterRpc);
        }
        EdsCompany company = companyManager.getUser().getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(facetFilterRpc.getSearchKey());
        fp.setViewType(facetFilterRpc.getName());

        String solrQuery = availabilityServiceLocal.getLeaveRequestSolrQuery(fp, user) +
                SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(facetFilterRpc, company, SolrLeaveRequestConst.FIELD_START_DATE, SolrLeaveRequestConst.FIELD_END_DATE);

        QueryResponse response = getSolrResponse(Constants.SOLR_LEAVE_REQUEST_CORE, solrQuery, SolrLeaveRequestConst.FIELD_EMPLOYEE_ID, facetFilterRpc, true);
        return SolrFacetUtils.fillFacetFilterDataWithNA(response, facetFilterRpc);
    }

    @Override
    public FacetFilterRpc getEmployeeStepFacetFilterData(ListingFilterParameter fp, FacetFilterRpc employeeStepFacet) {
        return employeeStepSolrComponent.getEmployeeStepFacetFilterData(fp, employeeStepFacet);
    }

    @Override
    public FacetFilterRpc getCustomFormItemFacetFilterData(ListingFilterParameter fp, FacetFilterRpc facet) {
        if (!facet.isFilterChanges()) {
            facet = commonServiceLocal.getUserFacetFilter(facet);
        }
        EdsUser user = companyManager.getUser();
        fp = fp == null ? new ListingFilterParameter() : fp;
        fp.setSearchKey(facet.getSearchKey());
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(SolrCustomFormConst.FIELD_COMPANY_ID).append(":").append(SecurityContext.getCompanyID());
        solrQuery.append(" AND ").append(SolrCustomFormConst.FIELD_ITEM_ID).append(":").append(fp.getParentID());
        solrQuery.append(" AND ").append(SolrCustomFormConst.FIELD_DOC_TYPE).append(":").append(SolrCustomFormConst.CUSTOM_FORM_SOLR_DOC);


        List<String> customAccessRoles = this.rolePermissionManager.getRolesByPermissionCode(fp.getForm() + "_FULL_LIST_" + user.getCompany().getObjectID());
        boolean hasCustomFullAccessToListing = customAccessRoles.size() > 0 && user.hasEitherRoles(customAccessRoles.toArray(new String[]{}));

        List<String> customSeeOwnAccessRoles = this.rolePermissionManager.getRolesByPermissionCode(fp.getForm() + "_SEE_OWN_" + user.getCompany().getObjectID());
        boolean hasCustomSeeOwnAccessToListing = customSeeOwnAccessRoles.size() > 0 && user.hasEitherRoles(customSeeOwnAccessRoles.toArray(new String[]{}));

        String clientIDsStr = "";
        if (fp.getLookUpBy() != null && fp.getEntityID() != null && ("CRM_ACCOUNT".equals(fp.getLookUpBy()) || CustomFieldLookUpTypeEnum.CUSTOMER.equals(fp.getLookUpBy()) || CustomFieldLookUpTypeEnum.SUPPLIER.equals(fp.getLookUpBy()))) {
            EdsCrmAccount crmAccount = this.crmAccountManager.get(fp.getEntityID());
            hasCustomSeeOwnAccessToListing = hasCustomSeeOwnAccessToListing && crmAccount.getOwners().contains(user);
        }

        if (hasCustomSeeOwnAccessToListing && !user.hasRole(EdsRole.ADMIN_CODE)) {
            List<Integer> clientIDs = this.crmAccountManager.getAccountIDsByOwner(user.getObjectID());
            if (clientIDs != null && clientIDs.size() > 0) {
                clientIDsStr = clientIDs.stream().map(clientID -> " " + clientID).collect(Collectors.joining());
            }
        }

        ArrayList<String> crmAccountColumnCodes = new ArrayList<>();
        ArrayList<CompanyCustomFieldItem> customFieldItems = this.commonServiceLocal.getCompanyCategoryCustomFields(fp.getParentID());
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

        if (!hasCustomFullAccessToListing) {
            if (!clientIDsStr.trim().isEmpty() && crmAccountColumnCodes != null && crmAccountColumnCodes.size() > 0) {
                solrQuery.append(" AND ( ").append(SolrCustomFormConst.FIELD_CREATOR_ID).append(":").append(user.getObjectID());
                solrQuery.append(" OR ").append(SolrCustomFormConst.FIELD_CURRENT_APPROVER_ID).append(":").append(user.getObjectID());
                for (String columnCode : crmAccountColumnCodes) {
                    solrQuery.append(" OR ").append(columnCode.toUpperCase()).append(":").append("(").append(clientIDsStr.trim()).append(") ");
                }
                solrQuery.append(" )");
            } else {
                solrQuery.append(" AND ((").append(SolrCustomFormConst.FIELD_CREATOR_ID).append(":").append(user.getObjectID()).append(") ");
                solrQuery.append(" OR (").append(SolrCustomFormConst.FIELD_CURRENT_APPROVER_ID).append(":").append(user.getObjectID()).append(")) ");
            }
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
            solrQuery.append(" AND (").append(SolrCustomFormConst.FIELD_COMPOSITE).append(":").append(SolrSearchUtils.normalaizeKeyword(fp.getSearchKey()));
            SolrSearchUtils searchUtils = new SolrSearchUtils();
            searchUtils.generateSearchQueryForCustom(solrQuery, QueryBuilderForSolr.getDynSearchFields(), fp.getSearchKey());
            solrQuery.append(")");
        }
        HashMap<String, FacetContentRpc> facetContentRpcHashMap = facet != null && facet.getFacetContentMap() != null ? facet.getFacetContentMap() : new HashMap<>();
        customFieldItems.stream().filter(customFieldItem -> Constants.UI_TYPE_LOOKUP.equals(customFieldItem.getUiType())
                && (CustomFieldLookUpTypeEnum.DEPARTMENT.equals(customFieldItem.getLookUpTypeEnum()) || CustomFieldLookUpTypeEnum.POSITION.equals(customFieldItem.getLookUpTypeEnum())))
                .peek(item -> {
                    FacetContentRpc facetContentRpc = facetContentRpcHashMap.get(item.getColumnCode());
                    if (facetContentRpc != null && facetContentRpc.getFacetItems() != null) {
                        Arrays.stream(facetContentRpc.getFacetItems()).peek(selectItem -> selectItem.setName(String.valueOf(selectItem.getId()))).collect(Collectors.toList());
                    }
                }).collect(Collectors.toList());
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(facet, user.getCompany(), SolrCustomFormConst.FIELD_CREATED_DATE, SolrCustomFormConst.FIELD_UPDATED_DATE));

        QueryResponse response = getSolrResponse(Constants.SOLR_CUSTOM_FORM_ITEM_CORE, solrQuery.toString(), SolrCustomFormConst.FIELD_ITEM_ID, facet, true);
        FacetFilterRpc facetFilterRpc = SolrFacetUtils.fillFacetFilterDataWithNA(response, facet, fp);
        commonServiceLocal.getFacetFilterWithLocale(facetFilterRpc.getFacetContentMap(), customFieldItems);
        return facetFilterRpc;
    }

    private void getLocaleForCustomFormFilter(FacetFilterRpc facetFilterRpc, ArrayList<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems == null || customFieldItems.size() <= 0 || facetFilterRpc == null) {
            return;
        }

        for (CompanyCustomFieldItem fieldItem : customFieldItems) {
            FacetContentRpc facetContentRpc = facetFilterRpc.getFacetContentMap().get(fieldItem.getColumnCode());
            if (facetContentRpc == null) continue;
            List<SelectItem> selectItems = Arrays.asList(facetContentRpc.getFacetItems());
            if (selectItems != null && selectItems.size() > 0) {
                if (CustomFieldLookUpTypeEnum.DEPARTMENT.equals(fieldItem.getLookUpTypeEnum()) && Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                    selectItems.stream().peek(selectItem -> {
                        try {
                            Integer departmentId = Integer.parseInt(selectItem.getName());
                            String departmentLocaleName = departmentManager.get(departmentId).getName();
                            selectItem.setId(departmentId);
                            selectItem.setName(departmentLocaleName);
                            selectItem.setDescription(departmentLocaleName + " (<b> " + selectItem.getTotalCount() + " </b>)");
                        } catch (NumberFormatException ex) {
                            ex.getStackTrace();
                        }
                    }).collect(Collectors.toList());
                }
                if (CustomFieldLookUpTypeEnum.POSITION.equals(fieldItem.getLookUpTypeEnum()) && Constants.UI_TYPE_LOOKUP.equals(fieldItem.getUiType())) {
                    selectItems.stream().peek(selectItem -> {
                        try {
                            Integer positionId = Integer.parseInt(selectItem.getName());
                            String positionLocaleName = positionManager.get(positionId).getName();
                            selectItem.setId(positionId);
                            selectItem.setName(positionLocaleName);
                            selectItem.setDescription(positionLocaleName + " (<b> " + selectItem.getTotalCount() + " </b>)");
                        } catch (NumberFormatException ex) {
                            ex.getStackTrace();
                        }
                    }).collect(Collectors.toList());
                }
            }
        }
    }

    @Override
    public FacetFilterRpc getSinglePayrunFacetFilterData(FacetFilterRpc singlePayrunFacet) {
        if (!singlePayrunFacet.isFilterChanges()) {
            singlePayrunFacet = commonServiceLocal.getUserFacetFilter(singlePayrunFacet);
        }
        EdsCompany company = companyManager.getUser().getCompany();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setSearchKey(singlePayrunFacet.getSearchKey());
        fp.setStartDate(singlePayrunFacet.getStartDate());
        fp.setEndDate(singlePayrunFacet.getEndDate());
        fp.setEmployeeId(singlePayrunFacet.getUserID());

        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(QueryBuilderForSolr.getSinglePayrunSolrQuery(fp));
        solrQuery.append(fp.getEmployeeId() == null ? payrollService.generatePermissionQuery(PermissionConstants.PAYROLL_PAYSLIP_LIST) : "");
        solrQuery.append(SolrFacetUtils.generateForPricesFacet(singlePayrunFacet, FacetContentType.SinglePayrunFacetFilter.getContentCode()[3]));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQueryWithNA(singlePayrunFacet, company,
                SolrSinglePayrunRepresenter.FIELD_FROM_DATE,
                SolrSinglePayrunRepresenter.FIELD_TO_DATE,
                FacetContentType.SinglePayrunFacetFilter.getContentCode()[3]));

        QueryResponse response = getSolrResponse(SOLR_SINGLE_PAYRUN_CORE, String.valueOf(solrQuery), SolrLeaveRequestConst.FIELD_EMPLOYEE_ID, singlePayrunFacet, true);
        SolrFacetUtils.fillFacetFilterDataWithNA(response, singlePayrunFacet);
        if (singlePayrunFacet.getFacetContentMap().containsKey(FacetContentType.SinglePayrunFacetFilter.getContentCode()[3])) {
            singlePayrunSolrComponent.getSinglePayrunFacetResultFromSolr(response.getResults(), singlePayrunFacet);
        }
        return singlePayrunFacet;
    }

    @Override
    public FacetFilterRpc getGroupPayrunFacetFilterData(FacetFilterRpc groupPayrunFacet) {
        return groupPayrunSolrComponent.getGroupPayrunFacetFilterData(groupPayrunFacet);
    }

    @Override
    public FacetFilterRpc getCashAdvanceFacetFilterData(FacetFilterRpc cashAdvanceFacet) {

        return cashAdvanceSolrComponent.getCashAdvanceFacetFilterData(cashAdvanceFacet);
    }

    @Override
    public FacetFilterRpc getAdditionalPaymentFacetFilterData(FacetFilterRpc additionalPaymentFacet) {
        return additionalPaymentSolrComponent.getAdditionalPaymentFacetFilterData(additionalPaymentFacet);
    }

    @Override
    public FacetFilterRpc getVacancyFacetFilterData(FacetFilterRpc data) {
        return vacancySolrComponent.getVacancyFacetFilterData(data);
    }

    @Override
    public FacetFilterRpc getChartOfAccountFacetFilterData(FacetFilterRpc facetFilterRpc) {
        return chartOfAccountSolrComponent.getChartOfAccountFacetFilterData(facetFilterRpc);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FacetFilterRpc getRFQFacetFilterData(FacetFilterRpc rfqFacet) {
        return requestForQuoteSolrComponent.getRFQFacetFilterData(rfqFacet);
    }
}
