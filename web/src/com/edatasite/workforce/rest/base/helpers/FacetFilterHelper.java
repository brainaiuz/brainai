package com.edatasite.workforce.rest.base.helpers;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.rest.base.to.FacetFilterItemTO;
import com.edatasite.workforce.rest.base.to.FacetFilterTO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Umidbek on 31.01.2015.
 */
public class FacetFilterHelper {
    /**
     * Facet Content Type value / Lite Facet Filter fields relations
     */

    final static HashMap<FacetContentType, ArrayList<String>> FACET_CODE_NAMES;

    /**
     * List panel / Facet content types relations
     */

    final static HashMap<ListPanelType, FacetContentType> LIST_PANEL_FACET_CONTENT;

    static {
        FACET_CODE_NAMES = new HashMap<FacetContentType, ArrayList<String>>() {{
            for (final FacetContentType type : FacetContentType.values()) {
                this.put(type, new ArrayList<>(Arrays.asList(type.getContentCode())));
            }
        }};

        LIST_PANEL_FACET_CONTENT = new HashMap<ListPanelType, FacetContentType>() {{
            this.put(ListPanelType.TaskListPanel, FacetContentType.TaskFacetFilter);
            this.put(ListPanelType.ProjectListPanel, FacetContentType.ProjectFacetFilter);
            this.put(ListPanelType.EmployeeListPanel, FacetContentType.ContactFacetFilter);
            this.put(ListPanelType.CaseListPanel, FacetContentType.CaseFacetFilter);
            this.put(ListPanelType.ContactListPanel, FacetContentType.ContactFacetFilter);
            this.put(ListPanelType.LeadListPanel, FacetContentType.LeadFacetFilter);
            this.put(ListPanelType.LeadKanbanPanel, FacetContentType.LeadFacetFilter);
            this.put(ListPanelType.ClientListPanel, FacetContentType.ClientFacetFilter);
            this.put(ListPanelType.OpportunitiesListPanel, FacetContentType.OpportunityFacetFilter);
            this.put(ListPanelType.CrmAccountListPanel, FacetContentType.CrmAccountFacetFilter);
            this.put(ListPanelType.SaleInvoiceListPanel, FacetContentType.SaleInvoiceFacetFilter);
            this.put(ListPanelType.PurchaseInvoicePanel, FacetContentType.PurchaseInvoiceFacetFilter);
            this.put(ListPanelType.SaleQuoteListPanel, FacetContentType.SaleQuoteFacetFilter);
            this.put(ListPanelType.ScheduledCourseListPanel, FacetContentType.CourseScheduleFaceFilter);
            this.put(ListPanelType.SaleOrderListPanel, FacetContentType.SaleOrderFacetFilter);
            this.put(ListPanelType.PurchaseOrderListPanel, FacetContentType.PurchaseOrderFacetFilter);
            this.put(ListPanelType.ExpenceReportListPanel, FacetContentType.ExpenseReportsClaimsFacetFilter);
            this.put(ListPanelType.ProductServiceListPanel, FacetContentType.ProductsServicesFacetFilter);
            this.put(ListPanelType.CustomFormItemsPanel, FacetContentType.CustomFormItemFacetFilter);
            this.put(ListPanelType.LeaveRequestApprove, FacetContentType.LeaveFacetFilter);
            this.put(ListPanelType.PositionsPanel, FacetContentType.PositionFilter);
            this.put(ListPanelType.DepartmentListPanel, FacetContentType.DepartmentFilter);
        }};
    }

    private static FacetFilterRpc getTaskFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.TaskListPanel,
                FacetFilterUtils.getTaskSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.TaskFacetFilter)
        );
    }

    private static FacetFilterRpc getProjectFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.ProjectListPanel,
                FacetFilterUtils.getProjectSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.ProjectFacetFilter)
        );
    }

    private static FacetFilterRpc getCaseFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.CaseListPanel,
                FacetFilterUtils.getCaseSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.CaseFacetFilter)
        );
    }

    private static FacetFilterRpc getContactFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.ContactListPanel,
                FacetFilterUtils.getContactSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.ContactFacetFilter)
        );
    }

    public static FacetFilterRpc getLeadFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.LeadListPanel,
                FacetFilterUtils.getLeadSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.LeadFacetFilter)
        );
    }

    public static FacetFilterRpc getLeadKanbanFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.LeadKanbanPanel,
                FacetFilterUtils.getLeadSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.LeadFacetFilter)
        );
    }

    public static FacetFilterRpc getAdditionalPaymentFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.AdditionalPayment,
                FacetFilterUtils.getAdditionalPaymentSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.AdditionalPaymentFacetFilter)
        );
    }

    public static FacetFilterRpc getSaleQuoteFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.SaleQuoteListPanel,
                FacetFilterUtils.getSaleQuoteSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.SaleQuoteFacetFilter)
        );
    }

    public static FacetFilterRpc getSaleOrderFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.SaleOrderListPanel,
                FacetFilterUtils.getSaleQuoteSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.SaleOrderFacetFilter)
        );
    }

    public static FacetFilterRpc getExpenseReportFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.ExpenceReportListPanel,
                FacetFilterUtils.getExpenseReportSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.ExpenseReportsClaimsFacetFilter)
        );
    }

    public static FacetFilterRpc getOpportunityFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.OpportunitiesListPanel,
                FacetFilterUtils.getOpportunitySolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.OpportunityFacetFilter)
        );
    }

    public static FacetFilterRpc getCourseScheduleFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.ScheduledCourseListPanel,
                FacetFilterUtils.getScheduleSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.CourseScheduleFaceFilter)
        );
    }

    public static FacetFilterRpc getCrmAccountFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.CrmAccountListPanel,
                FacetFilterUtils.getCrmAccountSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.CrmAccountFacetFilter)
        );
    }


    private static FacetFilterRpc getClientFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.ClientListPanel,
                FacetFilterUtils.getClientSolrFields(),
                FACET_CODE_NAMES.get(FacetContentType.ClientFacetFilter)
        );
    }

    private static FacetFilterRpc getSaleInvoiceFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.SaleInvoiceListPanel,
                FacetFilterUtils.getSaleInvoiceFields(),
                FACET_CODE_NAMES.get(FacetContentType.SaleInvoiceFacetFilter)
        );
    }

    private static FacetFilterRpc getPurchaseInvoiceFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.PurchaseInvoicePanel,
                FacetFilterUtils.getPurchaseInvoiceFields(),
                FACET_CODE_NAMES.get(FacetContentType.PurchaseInvoiceFacetFilter)
        );
    }

    private static FacetFilterRpc getPurchaseOrderFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.PurchaseOrderListPanel,
                FacetFilterUtils.getPurchaseOrderFields(),
                FACET_CODE_NAMES.get(FacetContentType.PurchaseOrderFacetFilter)
        );
    }

    private static FacetFilterRpc getProductServiceFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.ProductServiceListPanel,
                FacetFilterUtils.getProductServiceFields(),
                FACET_CODE_NAMES.get(FacetContentType.ProductsServicesFacetFilter)
        );
    }

    private static FacetFilterRpc getLeaveRequestFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.LeaveRequestApprove,
                FacetFilterUtils.getLeaveRequestFields(),
                FACET_CODE_NAMES.get(FacetContentType.LeaveFacetFilter)
        );
    }

    private static FacetFilterRpc getCustomFormFilterPrototype() {
        return new FacetFilterRpc(
                ListPanelType.CustomFormItemsPanel,
                FacetFilterUtils.getCustomFormFields(),
                FACET_CODE_NAMES.get(FacetContentType.CustomFormItemFacetFilter)
        );
    }

    /**
     * Public getter methods
     */

    public static FacetFilterRpc getFilterPrototype(ListPanelType listPanelType) {
        return switch (listPanelType) {
            case TaskListPanel -> getTaskFilterPrototype();
            case ProjectListPanel -> getProjectFilterPrototype();
            case CaseListPanel -> getCaseFilterPrototype();
            case ContactListPanel -> getContactFilterPrototype();
            case LeadListPanel -> getLeadFilterPrototype();
            case LeadKanbanPanel -> getLeadKanbanFilterPrototype();
            case ClientListPanel -> getClientFilterPrototype();
            case AdditionalPayment -> getAdditionalPaymentFilterPrototype();
            case SaleQuoteListPanel -> getSaleQuoteFilterPrototype();
            case SaleOrderListPanel -> getSaleOrderFilterPrototype();
            case ExpenceReportListPanel -> getExpenseReportFilterPrototype();
            case OpportunitiesListPanel -> getOpportunityFilterPrototype();
            case CrmAccountListPanel -> getCrmAccountFilterPrototype();
            case SaleInvoiceListPanel -> getSaleInvoiceFilterPrototype();
            case PurchaseInvoicePanel -> getPurchaseInvoiceFilterPrototype();
            case PurchaseOrderListPanel -> getPurchaseOrderFilterPrototype();
            case ProductServiceListPanel -> getProductServiceFilterPrototype();
            case LeaveRequestApprove -> getLeaveRequestFilterPrototype();
            case ScheduledCourseListPanel -> getCourseScheduleFilterPrototype();
            case CustomFormItemsPanel -> getCustomFormFilterPrototype();
            default -> null;
        };

    }

    public static FacetContentType getFacetContentType(ListPanelType listPanelType) {
        return LIST_PANEL_FACET_CONTENT.get(listPanelType);
    }

    public static ArrayList<String> getFacetCodeNames(ListPanelType listPanelType) {
        return FACET_CODE_NAMES.get(getFacetContentType(listPanelType));
    }

    public static ArrayList<String> getFacetCodeNames(FacetContentType facetContentType) {
        return FACET_CODE_NAMES.get(facetContentType);
    }


    public static FacetFilterRpc createFacetFilter(Integer filterID, ListPanelType listPanelType) {
        FacetFilterRpc facetFilter = getFilterPrototype(listPanelType);

        if (facetFilter == null) {
            facetFilter = new FacetFilterRpc();
        }

        facetFilter.setObjectID(filterID);

        return facetFilter;
    }

    static FacetFilterRpc fillFacetFilter(FacetFilterRpc facetFilter, HttpServletRequest servletRequest, ListPanelType listPanelType) {
        FacetContentType facetContentType = getFacetContentType(listPanelType);
        List<String> queryParams = getFacetCodeNames(facetContentType);

        if (facetFilter == null) {
            facetFilter = getFilterPrototype(listPanelType);
        }

        if (facetFilter == null) {
            return null;
        }

        facetFilter.setFilterChanges(true);

        if (queryParams != null) {
            for (String param : queryParams) {
                fillFacetContentMap(facetFilter, param, servletRequest.getParameter(param));
            }
        }

        return facetFilter;
    }

    private static void fillFacetContentMap(FacetFilterRpc facetFilter, String key, String chunkParams) {
        if (chunkParams == null) {
            return;
        }

        FacetSolrField solrField = facetFilter.getShowSolrFieldMap().get(key);

        List<SelectItem> selectItemList;

        if (facetFilter.getFacetContentMap().get(key).getFacetItems() == null) {
            selectItemList = new ArrayList<>();
        } else {
            selectItemList = new ArrayList<>(Arrays.asList(facetFilter.getFacetContentMap().get(key).getFacetItems()));
        }

        String[] values = chunkParams.split(",");

        for (String value : values) {
            if (solrField.isConditionItemId()) {
                selectItemList.add(new SelectItem(Integer.valueOf(value)));
            } else {
                selectItemList.add(new SelectItem(null, value));
            }
        }

        facetFilter.getFacetContentMap().get(key).setFacetItems(selectItemList.toArray(new SelectItem[0]));
    }

    public static FacetFilterRpc assign(FacetFilterTO filter, FacetFilterRpc facetFilter) {
        HashMap<String, ArrayList<FacetFilterItemTO>> content = filter.getFacetContent();

        facetFilter.setObjectID(filter.getId());

        if (filter.getName() != null) {
            facetFilter.setName(filter.getName().trim());
        }

        if (filter.getIsDefault() != null) {
            facetFilter.setDefaultFilter(filter.getIsDefault());
        }

        if (filter.getIsPublic() != null) {
            facetFilter.setPublicFilter(filter.getIsPublic());
        }

        if (content != null) {
            for (String key : content.keySet()) {
                if (!facetFilter.getFacetContentMap().containsKey(key)) {
                    continue;
                }

                FacetContentRpc facetContent = facetFilter.getFacetContentMap().get(key);
                FacetSolrField facetSolrField = facetFilter.getShowSolrFieldMap().get(key);

                ArrayList<FacetFilterItemTO> facetItems = content.get(key);
                List<SelectItem> selectItems = new ArrayList<>();

                for (FacetFilterItemTO facetItem : facetItems) {
                    if (facetSolrField.isConditionItemId()) {
                        selectItems.add(new SelectItem(Integer.valueOf(facetItem.getKey())));
                    } else {
                        selectItems.add(new SelectItem(null, facetItem.getKey()));
                    }
                }

                facetContent.setFacetItems(selectItems.toArray(new SelectItem[0]));
            }
        }

        return facetFilter;
    }

    public static FacetFilterTO mapFilter(FacetFilterRpc facetFilter) {
        List<String> queryParams = getFacetCodeNames(facetFilter.getType());
        HashMap<String, ArrayList<FacetFilterItemTO>> facetContent = new HashMap<>();

        FacetFilterTO facetFilterTO = new FacetFilterTO();
        facetFilterTO.setFacetContent(facetContent);

        facetFilterTO.setId(facetFilter.getObjectID());
        facetFilterTO.setName(facetFilter.getName());
        facetFilterTO.setIsDefault(facetFilter.isDefaultFilter());

        for (String queryParam : queryParams) {
            FacetContentRpc content = facetFilter.getFacetContentMap().get(queryParam);
            FacetSolrField solrField = facetFilter.getShowSolrFieldMap().get(queryParam);

            if (content == null || content.getSavedItems().isEmpty()) {
                continue;
            }

            ArrayList<FacetFilterItemTO> items = new ArrayList<>();

            for (SelectItem item : content.getFacetItems()) {
                String key;

                if (content.getSavedItems().containsKey(item.getId())) {
                    if (solrField.isConditionItemId()) {
                        key = String.valueOf(item.getId());
                    } else {
                        key = item.getName();
                    }

                    items.add(new FacetFilterItemTO(key, item.getName(), item.getDescription()));
                }
            }

            facetContent.put(queryParam, items);
        }

        return facetFilterTO;
    }

}
