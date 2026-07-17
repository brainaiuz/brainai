package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * User: Abdulaziz
 * Date: May 19, 2010
 * Time: 12:08:05 PM
 */
public interface RbacService extends RemoteService {

    SelectItem[] getCompanyGroups();

//    SelectItem[] getGroupOrUserList(boolean group);

//    GroupMembersViewItem getGroupMembers(Integer groupID);

//    TaskPolicyViewItem getCompanyTaskPolicy();

//    TaskPolicyItem[] getCompanyIndirectTaskPolicy();

//    TaskPolicyItem[] getCompanyDirectTaskPolicy();

//    void saveTaskPolicy(TaskPolicyItem item);

//    void deleteTaskPolice(Integer taskPolicyId);

//    void deleteGroup(Integer groupID);

//    void deleteGroupTrustee(Integer trusteeId, Integer trusteeType, Integer groupId);

//    ProjectMember[] getGroupMembersList(Integer groupID);

    Integer saveGroup(GroupMembersViewItem item) throws DuplicateNameException;

    FacetFilterRpc getTaskFacetFilterData(FacetFilterRpc taskFacet, boolean fromMobile);

    FacetFilterRpc getOpportunityFacetFilterData(FacetFilterRpc taskFacet);

    FacetFilterRpc getExpenseReportClaimsFacetFilterData(FacetFilterRpc taskFacet, String reporterName, boolean isLookup);

    FacetFilterRpc getEventFacetFilterData(FacetFilterRpc eventFacet, Integer eventType, Integer createdFrom);

    FacetFilterRpc getPurchaseInvoiceFacetFilterData(FacetFilterRpc purchaseFacet);

    ArrayList<GroupMembersViewItem> getCompanyGroupsWithMembers();

    FacetFilterRpc getBenefitRequestsFacetFilterData(FacetFilterRpc benefitRequestsData);

    FacetFilterRpc getCRMFacetFilterData(String type, FacetFilterRpc data);

    FacetFilterRpc getAccountingFacetFilterData(String type, FacetFilterRpc data);

    FacetFilterRpc getSaleInvoiceFacetFilterData(FacetFilterRpc data);

    FacetFilterRpc getSaleQuoteFacetFilterData(FacetFilterRpc data);

    FacetFilterRpc getSaleOrderFacetFilterData(FacetFilterRpc data);

    FacetFilterRpc getGdnGrnFacetFilterData(FacetFilterRpc data, boolean isGdn);

    FacetFilterRpc getCertificateFacetFilterData(FacetFilterRpc data);

    FacetFilterRpc getPositionFacetFilterData(FacetFilterRpc data);
    FacetFilterRpc getDepartmentFacetFilterData(FacetFilterRpc data);

    FacetFilterRpc getPurchaseOrderFacetFilterData(FacetFilterRpc data);

    FacetFilterRpc getProductsServicesFacetFilterData(FacetFilterRpc data);

    FacetFilterRpc getCourseBookingFacetFilterData(FacetFilterRpc data);

    FacetFilterRpc getCourseScheduleFacetFilterData(FacetFilterRpc data);

//    void fixInconsistenciesInDb(Integer companyID);

//    void fixInconsistenciesInSolr(Integer companyID);

    void fixInconsistencesInSolrAndDB(Integer companyID);

    void fixInconsistencesInSolrAndDBForAllCompanies();

    FacetFilterRpc getProjectFacetFilterData(FacetFilterRpc projectFacet);

//    FacetFilterRpc getNewsFacetFilterRpc(FacetFilterRpc newsFilter, boolean isBlog);

    ArrayList<Integer> getGroupMembersListForTree(Integer groupID);

    FacetFilterRpc getCaseFacetFilterData(FacetFilterRpc caseData);

    FacetFilterRpc getByCompanySolrCoreFacetFilter(FacetFilterRpc data, String solrCoreName);

    FacetFilterRpc getDocumentFacetFilterData(FacetFilterRpc grantProfileFacet, String typeCode);

    FacetFilterRpc getEmployeeFacetFilterData(FacetFilterRpc employeeFacet);

    FacetFilterRpc getEmployeeAnnualBalanceFacetFilterData(FacetFilterRpc employeeFacet);

    FacetFilterRpc getLeaveFacetFilterData(FacetFilterRpc filterRpc);

    FacetFilterRpc getEmployeeStepFacetFilterData(ListingFilterParameter fp, FacetFilterRpc employeeStepFacet);

    FacetFilterRpc getCustomFormItemFacetFilterData(ListingFilterParameter fp, FacetFilterRpc filterRpc);

    FacetFilterRpc getSinglePayrunFacetFilterData(FacetFilterRpc singlePayrunFacet);

    FacetFilterRpc getGroupPayrunFacetFilterData(FacetFilterRpc groupPayrunFacet);

    FacetFilterRpc getCashAdvanceFacetFilterData(FacetFilterRpc groupPayrunFacet);

    FacetFilterRpc getAdditionalPaymentFacetFilterData(FacetFilterRpc additionalPaymentFacet);

    FacetFilterRpc getVacancyFacetFilterData(FacetFilterRpc data);

    FacetFilterRpc getChartOfAccountFacetFilterData(FacetFilterRpc facetFilterRpc);

    FacetFilterRpc getRFQFacetFilterData(FacetFilterRpc data);

    class App {
        public static RbacServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/rbac");
            return (RbacServiceAsync) target;
        }
    }

}
