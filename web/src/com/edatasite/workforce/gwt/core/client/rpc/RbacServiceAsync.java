package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * User: Abdulaziz
 * Date: May 19, 2010
 * Time: 12:08:26 PM
 */
public interface RbacServiceAsync {
    void getCompanyGroups(AsyncCallback<SelectItem[]> async);

//    public void getGroupMembers(Integer groupID, AsyncCallback<GroupMembersViewItem> async);

//    public void getCompanyTaskPolicy(AsyncCallback<TaskPolicyViewItem> async);

//    void getCompanyIndirectTaskPolicy(AsyncCallback<TaskPolicyItem[]> callback);

//    void getCompanyDirectTaskPolicy(AsyncCallback<TaskPolicyItem[]> callback);

//    void saveTaskPolicy(TaskPolicyItem item, AsyncCallback<Void> callback);

//    void deleteTaskPolice(Integer taskPolicyId, AsyncCallback<Void> callback);

//    void deleteGroup(Integer groupID, AsyncCallback<Void> callback);

//    void getGroupOrUserList(boolean group, AsyncCallback<SelectItem[]> callback);

//    void deleteGroupTrustee(Integer trusteeId, Integer trusteeType, Integer groupId, AsyncCallback<Void> callback);

//    void getGroupMembersList(Integer groupID, AsyncCallback<ProjectMember[]> callback);

    void saveGroup(GroupMembersViewItem item, AsyncCallback<Integer> callback);

    void getTaskFacetFilterData(FacetFilterRpc taskFacet, boolean fromMobile, AsyncCallback<FacetFilterRpc> callback);

    void getOpportunityFacetFilterData(FacetFilterRpc opportunityFacet, AsyncCallback<FacetFilterRpc> callback);

    void getExpenseReportClaimsFacetFilterData(FacetFilterRpc reportFacet, String reporterName, boolean isLookup, AsyncCallback<FacetFilterRpc> callback);

    void getEventFacetFilterData(FacetFilterRpc eventFacet, Integer eventType, Integer createdFrom, AsyncCallback<FacetFilterRpc> callback);

    void getPurchaseInvoiceFacetFilterData(FacetFilterRpc purchaseFacet, AsyncCallback<FacetFilterRpc> callback);

    void getCompanyGroupsWithMembers(AsyncCallback<ArrayList<GroupMembersViewItem>> callback);

    void getCRMFacetFilterData(String type, FacetFilterRpc data, AsyncCallback<FacetFilterRpc> callback);

    void getAccountingFacetFilterData(String type, FacetFilterRpc data, AsyncCallback<FacetFilterRpc> callback);

    void getSaleInvoiceFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> callback);

    void getRFQFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> callback);

    void getSaleQuoteFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> callback);

    void getSaleOrderFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> callback);

    void getGdnGrnFacetFilterData(FacetFilterRpc data, boolean isGdn, AsyncCallback<FacetFilterRpc> callback);

    void getPurchaseOrderFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> callback);

    void getProductsServicesFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> callback);

//    void fixInconsistenciesInDb(Integer companyID, AsyncCallback<Void> callback);

//    void fixInconsistenciesInSolr(Integer companyID, AsyncCallback<Void> callback);

    void fixInconsistencesInSolrAndDB(Integer companyID, AsyncCallback<Void> callback);

    void fixInconsistencesInSolrAndDBForAllCompanies(AsyncCallback<Void> callback);

    void getProjectFacetFilterData(FacetFilterRpc projectFacet, AsyncCallback<FacetFilterRpc> callback);

//    void getNewsFacetFilterRpc(FacetFilterRpc newsFilter, boolean isBlog, AsyncCallback<FacetFilterRpc> callback);

    void getGroupMembersListForTree(Integer groupID, AsyncCallback<ArrayList<Integer>> callback);

    void getCaseFacetFilterData(FacetFilterRpc caseData, AsyncCallback<FacetFilterRpc> callback);

    void getByCompanySolrCoreFacetFilter(FacetFilterRpc data, String solrCoreName, AsyncCallback<FacetFilterRpc> callback);

    void getCourseBookingFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> async);

    void getCourseScheduleFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> async);

    void getDocumentFacetFilterData(FacetFilterRpc profileData, String typeCode, AsyncCallback<FacetFilterRpc> callback);

    void getBenefitRequestsFacetFilterData(FacetFilterRpc facetFilterBenefitRequests, AsyncCallback<FacetFilterRpc> async);

    void getEmployeeFacetFilterData(FacetFilterRpc employee, AsyncCallback<FacetFilterRpc> callback);

    void getEmployeeAnnualBalanceFacetFilterData(FacetFilterRpc employee, AsyncCallback<FacetFilterRpc> callback);

    void getLeaveFacetFilterData(FacetFilterRpc leave, AsyncCallback<FacetFilterRpc> callback);

    void getEmployeeStepFacetFilterData(ListingFilterParameter fp, FacetFilterRpc employeeStep, AsyncCallback<FacetFilterRpc> callback);

    void getCustomFormItemFacetFilterData(ListingFilterParameter fp, FacetFilterRpc facetFilterRpc, AsyncCallback<FacetFilterRpc> callback);

    void getSinglePayrunFacetFilterData(FacetFilterRpc singlePayrun, AsyncCallback<FacetFilterRpc> callback);

    void getGroupPayrunFacetFilterData(FacetFilterRpc groupPayrun, AsyncCallback<FacetFilterRpc> callback);

    void getCashAdvanceFacetFilterData(FacetFilterRpc groupPayrun, AsyncCallback<FacetFilterRpc> callback);

    void getAdditionalPaymentFacetFilterData(FacetFilterRpc additionalPaymentFacet, AsyncCallback<FacetFilterRpc> callback);

    void getVacancyFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> callback);

    void getChartOfAccountFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> callback);

    void getCertificateFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> async);

    void getPositionFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> async);

    void getDepartmentFacetFilterData(FacetFilterRpc data, AsyncCallback<FacetFilterRpc> async);
}
