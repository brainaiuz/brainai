package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.view.RequestForQuoteLookUp;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PRODUCT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_QUOTE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_QUOTE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PRODUCT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PURCHASE_ORDER_SUMMARY;

/**
 * User: Hayot
 * Date: 8/11/11
 * Time: 3:49 PM
 */
public class AddTaggingView extends KpiModal {
    private final static AllInOneServiceAsync service = AllInOneService.App.get();
    final FlexTable table;
    private final FlexTable relationsTable;
    private ArrayList<SelectItem> relationals;
    private ArrayList<RelationItem> selectedRelations = new ArrayList<>();
    private Integer fromID;
    private String fromType;
    private String fromName;
    private final boolean isActionEditing;

    private LookUp lookUp;
    private LookUp projectLookUp;
    private CRMLookUp taskLookUp;
    private CRMLookUp issueLookUp;
    private CRMLookUp eventLookUp;
    private DatePicker eventDateUp;
    private List<SelectItem> customForms;


    public AddTaggingView(Integer fromID, String fromType, String fromName, String title, boolean isActionEditing) {
        setTitle(title);
        setWidth(575);
        setScrollable(true);
        table = new FlexTable();
        relationsTable = new FlexTable();
        setCloseButton(true);
        this.fromID = fromID;
        this.fromType = fromType;
        this.fromName = fromName;
        this.isActionEditing = isActionEditing;
        CommonService.App.get().getCustomForms(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                init();
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                super.onSuccess(result);
                customForms = result;
                init();
            }
        });
    }

    private void init() {
        Label linkTo = new Label(wfmStrings.linkTo());
        linkTo.setStyleName("customTitle");
        table.setWidget(0, 0, linkTo);
        table.getFlexCellFormatter().getElement(0, 0).getStyle().setPadding(10d, Style.Unit.PX);
        relationsTable.setStyleName("added-links");

        DataListBox sectionList = new DataListBox();
        sectionList.setItems(getRelational());
        sectionList.setChangeEvent(() -> {
            if (sectionList.isSomethingSelected()) {
                if (lookUp != null) {
                    lookUp.removeFromParent();
                    lookUp = null;
                }
                if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_TASK)) {
                    removeTaskRelatedWidgets();
                    taskSelectedInDropDown();
                } else if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_ISSUE)) {
                    removeTaskRelatedWidgets();
                    issueSelectedInDropDown();
                } else if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_EVENT)) {
                    eventSelectedInDropDown();
                } else {
                    removeTaskRelatedWidgets();
                    addLookUp(sectionList.getSelectedItem().getDescription(), sectionList.getSelectedItem().getName());
                }

            }
        });
        table.setWidget(1, 0, sectionList);
        table.setWidget(1, 1, new HTML());
        add(table);
        add(relationsTable);
        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.ensureDebugId("add_link_save_button");
        save.addClickHandler(event -> {
            if (!validate()) {
                return;
            }
            if (fromName != null && fromID != null) {
                RelationItem.setFromName(fromID, fromType, fromName, selectedRelations);
            }
            if (fromID != null && !isActionEditing) {
                LoadingPanel.loading(true);
                service.saveRelations(fromType, fromID, fromName, selectedRelations, true, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
                    @Override
                    public void failure(Throwable throwable) {
                        close();
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(ArrayList<RelationItem> selectItems) {
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_RELATION, selectItems, AddTaggingView.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_ADD, selectItems, AddTaggingView.this);
                        selectedRelations = selectItems;
                        close();
                        LoadingPanel.loading(false);
                    }
                });
            } else {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_RELATION, selectedRelations, AddTaggingView.this);
                close();
            }
        });
        addButton(save);
    }

    public static VerticalPanelDiv drawRelationTags(String fromType, Integer fromID, RelationItem... relations) {

        VerticalPanelDiv tagTable = new VerticalPanelDiv();
        tagTable.setWidth("100%");

        ArrayList<SelectItem> projectRelationItems = new ArrayList<>();
        ArrayList<SelectItem> contactRelationItems = new ArrayList<>();
        ArrayList<SelectItem> campaignRelationItems = new ArrayList<>();
        ArrayList<SelectItem> leadRelationItems = new ArrayList<>();
        ArrayList<SelectItem> candidateRelationItems = new ArrayList<>();
        ArrayList<SelectItem> opportunityRelationItems = new ArrayList<>();
        ArrayList<SelectItem> caseRelationItems = new ArrayList<>();
        ArrayList<SelectItem> taskRelationItems = new ArrayList<>();
        ArrayList<SelectItem> eventRelationItems = new ArrayList<>();
        ArrayList<SelectItem> crmAccountRelationItems = new ArrayList<>();
        ArrayList<SelectItem> clientCustomerRelationItems = new ArrayList<>();
        ArrayList<SelectItem> supplierRelationItems = new ArrayList<>();
        ArrayList<SelectItem> employeeRelationItems = new ArrayList<>();
        ArrayList<SelectItem> departmentRelationItems = new ArrayList<>();
        ArrayList<SelectItem> issueRelationItems = new ArrayList<>();
        ArrayList<SelectItem> saleQuoteItems = new ArrayList<>();
        ArrayList<SelectItem> saleOrderItems = new ArrayList<>();
        ArrayList<SelectItem> productItems = new ArrayList<>();
        ArrayList<SelectItem> bookingItems = new ArrayList<>();
        ArrayList<SelectItem> meetingItems = new ArrayList<>();
        ArrayList<SelectItem> purchaseOrders = new ArrayList<>();
        ArrayList<SelectItem> rfqItems = new ArrayList<>();
        ArrayList<SelectItem> studentItems = new ArrayList<>();
        if (relations != null) {
            for (final RelationItem relation : relations) {
                if (!relation.isRemove()) {
                    String relationType = null;
                    String relationToName = null;
                    Integer id = null;
                    if (fromID == null || (fromType.equals(relation.getFromType()) && fromID.equals(relation.getFromID()))) {
                        relationType = relation.getToType();
                        relationToName = relation.getToName();
                        id = relation.getToID();
                    } else if (fromID.equals(relation.getToID()) && fromType.equals(relation.getToType())) {
                        relationType = relation.getFromType();
                        relationToName = relation.getFromName();
                        id = relation.getFromID();
                    }

                    if (relationType != null && id != null) {
                        if (RelationItem.TYPE_PROJECT.equals(relationType)) {
                            SelectItem projectItem = new SelectItem();
                            projectItem.setId(id);
                            projectItem.setName(relationToName);
                            if (Utils.getPathName().contains("ProjectManagement.html")) {
                                projectItem.setDescription("project|summary/" + id);
                                projectItem.setNewItem(false);
                            } else {
                                projectItem.setDescription("ProjectManagement.html#" + "project|summary/" + id);
                                projectItem.setNewItem(true);
                            }
                            projectRelationItems.add(projectItem);
                        } else if (RelationItem.TYPE_CONTACT.equals(relationType)) {
                            SelectItem contactItem = new SelectItem();
                            contactItem.setId(id);
                            contactItem.setName(relationToName);
                            if (Utils.getPathName().contains("ProjectManagement.html") || Utils.getPathName().contains("Crm.html")) {
                                contactItem.setDescription("contact|summary/" + id);
                                contactItem.setNewItem(false);
                            } else {
                                contactItem.setDescription("Crm.html#" + "contact|summary/" + id);
                                contactItem.setNewItem(true);
                            }
                            contactRelationItems.add(contactItem);
                        } else if (RelationItem.TYPE_CAMPAIGN.equals(relationType)) {
                            SelectItem campaignItem = new SelectItem();
                            campaignItem.setId(id);
                            campaignItem.setName(relationToName);
                            if (Utils.getPathName().contains("Crm.html")) {
                                campaignItem.setDescription("campaign|summary/" + id);
                                campaignItem.setNewItem(false);
                            } else {
                                campaignItem.setDescription("Crm.html#" + "campaign|summary/" + id);
                                campaignItem.setNewItem(true);
                            }
                            campaignRelationItems.add(campaignItem);
                        } else if (RelationItem.TYPE_LEAD.equals(relationType)) {
                            SelectItem leadItem = new SelectItem();
                            leadItem.setId(id);
                            leadItem.setName(relationToName);
                            if (Utils.getPathName().contains("Crm.html")) {
                                leadItem.setDescription("lead|summary/" + id);
                                leadItem.setNewItem(false);
                            } else {
                                leadItem.setDescription("Crm.html#" + "lead|summary/" + id);
                                leadItem.setNewItem(true);
                            }
                            leadRelationItems.add(leadItem);
                        } else if (RelationItem.TYPE_CANDIDATE.equals(relationType) && relationToName != null) {
                            SelectItem candidateItem = new SelectItem();
                            candidateItem.setId(id);
                            candidateItem.setName(relationToName);
                            if (Utils.isHRMS()) {
                                candidateItem.setDescription("candidate|summary/" + id);
                                candidateItem.setNewItem(false);
                            } else {
                                candidateItem.setDescription("Hrms.html#" + "candidate|summary/" + id);
                                candidateItem.setNewItem(true);
                            }
                            candidateRelationItems.add(candidateItem);
                        } else if (RelationItem.TYPE_OPPORTUNITY.equals(relationType)) {
                            SelectItem opportunityItem = new SelectItem();
                            opportunityItem.setId(id);
                            opportunityItem.setName(relationToName);
                            if (Utils.getPathName().contains("Crm.html")) {
                                opportunityItem.setDescription("opportunity|summary/" + id);
                                opportunityItem.setNewItem(false);
                            } else {
                                opportunityItem.setDescription("Crm.html#" + "opportunity|summary/" + id);
                                opportunityItem.setNewItem(true);
                            }
                            opportunityRelationItems.add(opportunityItem);
                        } else if (RelationItem.TYPE_CASE.equals(relationType)) {
                            SelectItem caseItem = new SelectItem();
                            caseItem.setId(id);
                            caseItem.setName(relationToName);
                            if (Utils.getPathName().contains("Crm.html") || Utils.getPathName().contains("ProjectManagement.html")) {
                                caseItem.setDescription("case|summary/" + id);
                                caseItem.setNewItem(false);
                            } else {
                                caseItem.setDescription("Crm.html#" + "case|summary/" + id);
                                caseItem.setNewItem(true);
                            }
                            caseRelationItems.add(caseItem);
                        } else if (RelationItem.TYPE_TASK.equals(relationType)) {
                            SelectItem taskItem = new SelectItem();
                            taskItem.setId(id);
                            taskItem.setName(relationToName);
                            if (Utils.getPathName().contains("ProjectManagement.html")) {
                                taskItem.setDescription("task|summary/" + id);
                                taskItem.setNewItem(false);
                            } else if (Utils.getPathName().contains("Crm.html")) {
                                taskItem.setDescription("task|summary/" + id);
                                taskItem.setNewItem(false);
                            } else {
                                taskItem.setDescription("ProjectManagement.html#" + "task|summary/" + id);
                                taskItem.setNewItem(true);
                            }
                            taskRelationItems.add(taskItem);
                        } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
                            SelectItem crmAccountItem = new SelectItem();
                            crmAccountItem.setId(id);
                            crmAccountItem.setName(relationToName);
                            if (Utils.hasCrmRole()) {
                                if (Utils.getPathName().contains("Crm.html")) {
                                    crmAccountItem.setDescription("account|summary/" + id);
                                    crmAccountItem.setNewItem(false);
                                } else {
                                    crmAccountItem.setDescription("Crm.html#" + "account|summary/" + id);
                                    crmAccountItem.setNewItem(true);
                                }
                            }
                            crmAccountRelationItems.add(crmAccountItem);
                        } else if (RelationItem.TYPE_CLIENT.equals(relationType)) {
                            SelectItem clientCustomerItem = new SelectItem();
                            clientCustomerItem.setId(id);
                            clientCustomerItem.setName(relationToName);
                            if (Utils.getPathName().contains("ProjectManagement.html") || Utils.getPathName().contains("Accounting.html")) {
                                clientCustomerItem.setDescription("client|summary/" + id);
                                clientCustomerItem.setNewItem(false);
                            } else {
                                clientCustomerItem.setDescription("Crm.html#account|summary/" + id + "/false/Customer");
                                clientCustomerItem.setNewItem(true);
                            }
                            clientCustomerRelationItems.add(clientCustomerItem);
                        } else if (RelationItem.TYPE_SUPPLIER.equals(relationType)) {
                            SelectItem supplierItem = new SelectItem();
                            supplierItem.setId(id);
                            supplierItem.setName(relationToName);
                            if (Utils.getPathName().contains("Accounting.html")) {
                                supplierItem.setDescription("suppliersummary|summary/" + id);
                                supplierItem.setNewItem(false);
                            } else {
                                supplierItem.setDescription("Crm.html#account|summary/" + id + "/false/Supplier");
                                supplierItem.setNewItem(true);
                            }
                            supplierRelationItems.add(supplierItem);
                        } else if (RelationItem.TYPE_EMPLOYEE.equals(relationType)) {
                            SelectItem employeeItem = new SelectItem();
                            employeeItem.setId(id);
                            employeeItem.setName(relationToName);
                            if (Utils.getPathName().contains("ProjectManagement.html")) {
                                employeeItem.setDescription("employee|summary/" + id);
                                employeeItem.setNewItem(false);
                            } else if (Utils.getPathName().contains("Hrms.html")) {
                                employeeItem.setDescription("employeeProfile|employeeProfileView/" + id);
                                employeeItem.setNewItem(false);
                            } else {
                                employeeItem.setDescription("ProjectManagement.html#" + "employee|summary/" + id);
                                employeeItem.setNewItem(true);
                            }
                            employeeRelationItems.add(employeeItem);
                        } else if (RelationItem.TYPE_DEPARTMENT.equals(relationType)) {
                            SelectItem departmentItem = new SelectItem();
                            departmentItem.setId(id);
                            departmentItem.setName(relationToName);
                            if (Utils.getPathName().contains("ProjectManagement.html") || Utils.getPathName().contains("Hrms.html")) {
                                departmentItem.setDescription("department|summary/" + id);
                                departmentItem.setNewItem(false);
                            } else {
                                departmentItem.setDescription("ProjectManagement.html#" + "department|summary/" + id);
                                departmentItem.setNewItem(true);
                            }
                            departmentRelationItems.add(departmentItem);
                        } else if (RelationItem.TYPE_ISSUE.equals(relationType)) {
                            SelectItem issueItem = new SelectItem();
                            issueItem.setId(id);
                            issueItem.setName(relationToName);
                            if (Utils.getPathName().contains("ProjectManagement.html")) {
                                issueItem.setDescription("issue|summary/" + id);
                                issueItem.setNewItem(false);
                            } else {
                                issueItem.setDescription("ProjectManagement.html#" + "issue|summary/" + id);
                                issueItem.setNewItem(true);
                            }
                            issueRelationItems.add(issueItem);
                        } else if (RelationItem.TYPE_SALEQUOTE.equals(relationType)) {
                            SelectItem quoteItem = new SelectItem();
                            quoteItem.setId(id);
                            quoteItem.setName(relationToName);
                            if (Utils.hasRolesForAccounting()) {
                                if (Utils.getPathName().contains("Accounting.html")) {
                                    quoteItem.setDescription("salequote|summary/" + id);
                                    quoteItem.setNewItem(false);
                                } else {
                                    quoteItem.setDescription("Accounting.html#" + "salequote|summary/" + id);
                                    quoteItem.setNewItem(true);
                                }
                            }
                            saleQuoteItems.add(quoteItem);
                        } else if (RelationItem.TYPE_SALEORDER.equals(relationType)) {
                            SelectItem orderItem = new SelectItem();
                            orderItem.setId(id);
                            orderItem.setName(relationToName);
                            if (Utils.hasRolesForAccounting()) {
                                if (Utils.getPathName().contains("Accounting.html")) {
                                    orderItem.setDescription("saleorder|summary/" + id);
                                    orderItem.setNewItem(false);
                                } else {
                                    orderItem.setDescription("Accounting.html#" + "saleorder|summary/" + id);
                                    orderItem.setNewItem(true);
                                }
                            }
                            saleOrderItems.add(orderItem);
                        } else if (RelationItem.TYPE_PRODUCT.equals(relationType)) {
                            SelectItem productItem = new SelectItem();
                            productItem.setId(id);
                            productItem.setName(relationToName);
                            if (Utils.hasRolesForAccounting()) {
                                if (Utils.getPathName().contains("Accounting.html")) {
                                    productItem.setDescription("product|summary/" + id);
                                    productItem.setNewItem(false);
                                } else {
                                    productItem.setDescription("Accounting.html#" + "product|summary/" + id);
                                    productItem.setNewItem(true);
                                }
                            }
                            productItems.add(productItem);
                        } else if (RelationItem.TYPE_BOOKING.equals(relationType)) {
                            SelectItem bookingItem = new SelectItem();
                            bookingItem.setId(id);
                            bookingItem.setName(relationToName);
                            bookingItem.setDescription("bookingitemsreservation|summary/" + id);
                            bookingItem.setNewItem(false);

                            bookingItems.add(bookingItem);
                        } else if (RelationItem.TYPE_MEETING_MINUTES.equals(relationType)) {
                            SelectItem meetingItem = new SelectItem();
                            meetingItem.setId(id);
                            meetingItem.setName(relationToName);
                            meetingItem.setDescription("Hrms.html#" + "meetingMinutes|summary/" + id);
                            meetingItem.setNewItem(true);

                            meetingItems.add(meetingItem);
                        } else if (RelationItem.TYPE_PURCHASE_ORDER.equals(relationType)) {
                            SelectItem purchaseItem = new SelectItem();
                            purchaseItem.setId(id);
                            purchaseItem.setName(relationToName);
                            purchaseItem.setDescription("Accounting.html#" + Constants.PURCHASE_ORDER + "|summary/" + id);
                            purchaseItem.setNewItem(true);
                            purchaseOrders.add(purchaseItem);
                        } else if (RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(relationType)) {
                            SelectItem rfqItem = new SelectItem();
                            rfqItem.setId(id);
                            rfqItem.setName(relationToName);
                            if (Utils.hasRolesForAccounting()) {
                                if (Utils.getPathName().contains("Accounting.html")) {
                                    rfqItem.setDescription(Constants.REQUEST_FOR_QUOTE + "|summary/" + id);
                                    rfqItem.setNewItem(false);
                                } else {
                                    rfqItem.setDescription("Accounting.html#" + Constants.REQUEST_FOR_QUOTE + "|summary/" + id);
                                    rfqItem.setNewItem(true);
                                }
                            }
                            rfqItems.add(rfqItem);
                        } else if (RelationItem.TYPE_STUDENT.equals(relationType)) {
                            SelectItem studentItem = new SelectItem();
                            studentItem.setId(id);
                            studentItem.setName(relationToName);
                            studentItem.setDescription("TrainingCenter.html#" + "students|summary/" + id);
                            studentItem.setNewItem(true);
                            studentItems.add(studentItem);
                        } else { //Custom Forms Temporary Solution

                            String finalRelationType = relationType;
                            Integer finalId = id;
                            String finalRelationToName = relationToName;
                            CommonService.App.get().getCustomForms(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    super.onFailure(caught);
                                }

                                @Override
                                public void onSuccess(ArrayList<SelectItem> result) {
                                    super.onSuccess(result);
                                    if (result != null && result.size() > 0) {
                                        for (SelectItem item : result) {
                                            if (item.getName().equals(finalRelationType)) {
                                                SelectItem customFormItem = new SelectItem();
                                                customFormItem.setId(finalId);
                                                customFormItem.setName(finalRelationToName);
                                                if (Utils.isEnableAccountingModule()) {
                                                    customFormItem.setDescription("Accounting.html#" + Constants.ITEM_LIST + "|summary/" + finalId + "/" + item.getId() + "/" + item.getCode() + "/" + finalRelationType);
                                                } else {
                                                    customFormItem.setDescription("Crm.html#" + Constants.ITEM_LIST + "|summary/" + finalId + "/" + item.getId() + "/" + item.getCode() + "/" + finalRelationType);
                                                }
                                                customFormItem.setNewItem(true);
                                                ArrayList<SelectItem> customFormItems = new ArrayList<>();
                                                customFormItems.add(customFormItem);
                                                new RelationTags(tagTable, finalRelationType, customFormItems);
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        }
        new RelationTags(tagTable, Property.get(Constants.PROJECT, wfmStrings.project()), projectRelationItems);
        new RelationTags(tagTable, Property.get(Constants.Contacts, wfmStrings.contact()), contactRelationItems);
        new RelationTags(tagTable, wfmStrings.campaign(), campaignRelationItems);
        new RelationTags(tagTable, Property.get(Constants.LEADS, wfmStrings.lead()), leadRelationItems);
        new RelationTags(tagTable, wfmStrings.candidate(), candidateRelationItems);
        new RelationTags(tagTable, Property.get(Constants.Opportunities, wfmStrings.opportunity()), opportunityRelationItems);
        new RelationTags(tagTable, Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), caseRelationItems);
        new RelationTags(tagTable, Property.get(Constants.TASK, wfmStrings.task()), taskRelationItems);
        new RelationTags(tagTable, Property.get(Constants.EVENT_LIST, wfmStrings.event()), eventRelationItems);
        new RelationTags(tagTable, wfmStrings.crmAccount(), crmAccountRelationItems);
        new RelationTags(tagTable, wfmStrings.customer(), clientCustomerRelationItems);
        new RelationTags(tagTable, Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), supplierRelationItems);
        new RelationTags(tagTable, wfmStrings.employee(), employeeRelationItems);
        new RelationTags(tagTable, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), departmentRelationItems);
        new RelationTags(tagTable, Property.get(Constants.ISSUE, wfmStrings.issue()), issueRelationItems);
        new RelationTags(tagTable, wfmStrings.salesQuote(), saleQuoteItems);
        new RelationTags(tagTable, wfmStrings.saleorder(), saleOrderItems);
        new RelationTags(tagTable, wfmStrings.product(), productItems);
        new RelationTags(tagTable, wfmStrings.booking(), bookingItems);
        new RelationTags(tagTable, wfmStrings.meetingMinutes(), meetingItems);
        new RelationTags(tagTable, wfmStrings.purchaseorder(), purchaseOrders);
        new RelationTags(tagTable, wfmStrings.requestForQuote(), rfqItems);
        new RelationTags(tagTable, wfmStrings.student(), studentItems);

        return tagTable;
    }

    public boolean validate() {
        if (projectLookUp != null && taskLookUp != null) {
            if (projectLookUp.getSelectedItemID() != null && taskLookUp.getSelectedItem() == null) {
                taskLookUp.addStyleName("x-form-invalid");
                return false;
            }
        }
        if (projectLookUp != null && issueLookUp != null) {
            if (projectLookUp.getSelectedItemID() != null && issueLookUp.getSelectedItem() == null) {
                issueLookUp.addStyleName("x-form-invalid");
                return false;
            }
        }
        if (eventDateUp != null && eventLookUp != null) {
            if (eventDateUp.getDate() != null && eventLookUp.getSelectedItem() == null) {
                eventLookUp.addStyleName("x-form-invalid");
                return false;
            }
        }
        return true;
    }

    private void addLookUp(final String type, String formName) {
        if (lookUp == null) {
            if (type.equals(RelationItem.TYPE_REQUEST_FOR_QUOTE)) {
                lookUp = new RequestForQuoteLookUp();
            } else if (type.contains("_FORM")) {
                lookUp = new CRMLookUp(true, type);
            } else {
                lookUp = new CRMLookUp(type);
            }

            lookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                if (suggestionSelectionEvent.getSelectedItem() != null) {
                    SelectItem selected = lookUp.getSelectedItem();
                    selected.setDescription(type.contains("_FORM") ? formName : type);

                    compareToAdd(selected);
                    if (type.equals(RelationItem.TYPE_REQUEST_FOR_QUOTE) && selected.getEntityId() != null) {
                        compareToAdd(new SelectItem(selected.getEntityId(), selected.getCode(), RelationItem.TYPE_CLIENT));
                    }

                }
            });
        }
        table.setWidget(1, 1, lookUp);
        table.getFlexCellFormatter().getElement(1, 1).getStyle().setPadding(5.0, Style.Unit.PX);

    }

    private void taskSelectedInDropDown() {
        Label projectTitle = new Label(Property.get(Constants.PROJECT, wfmStrings.project()));
        Label taskTitle = new Label(Property.get(Constants.TASK, wfmStrings.task()));
        projectTitle.setStyleName("customTitle");
        taskTitle.setStyleName("customTitle");

        table.setWidget(0, 1, projectTitle);
        table.setWidget(0, 2, taskTitle);

        if (projectLookUp == null) {
            projectLookUp = new CRMLookUp(RelationItem.TYPE_PROJECT);
        }
        if (taskLookUp == null) {
            taskLookUp = new CRMLookUp(RelationItem.TYPE_TASK);
        }
        taskLookUp.setEnabled(false);

        projectLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                taskLookUp.clearAndClearItems();
                taskLookUp.refreshOracle(true);
                if (projectLookUp.getSelectedItem() != null) {
                    taskLookUp.setProjectID(projectLookUp.getSelectedItem().getId());
                    taskLookUp.setEnabled(true);
                }
            } else {
                taskLookUp.setProjectID(null);
                taskLookUp.setEnabled(false);
            }
        });

        taskLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                SelectItem selected = taskLookUp.getSelectedItem();
                selected.setDescription(RelationItem.TYPE_TASK);
                compareToAdd(selected);
            }
        });

        table.setWidget(1, 1, projectLookUp);
        table.setWidget(1, 2, taskLookUp);
        table.getFlexCellFormatter().getElement(1, 1).getStyle().setPadding(5.0, Style.Unit.PX);
    }

    private void issueSelectedInDropDown() {
        Label projectTitle = new Label(Property.get(Constants.PROJECT, wfmStrings.project()));
        Label issueTitle = new Label(Property.get(Constants.ISSUE, wfmStrings.issue()));
        projectTitle.setStyleName("customTitle");
        issueTitle.setStyleName("customTitle");

        table.setWidget(0, 1, projectTitle);
        table.setWidget(0, 2, issueTitle);

        if (projectLookUp == null) {
            projectLookUp = new CRMLookUp(RelationItem.TYPE_PROJECT);
        }
        if (issueLookUp == null) {
            issueLookUp = new CRMLookUp(RelationItem.TYPE_ISSUE);
        }
        issueLookUp.setEnabled(false);

        projectLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                issueLookUp.clearAndClearItems();
                issueLookUp.refreshOracle(true);
                if (projectLookUp.getSelectedItem() != null) {
                    issueLookUp.setProjectID(projectLookUp.getSelectedItem().getId());
                    issueLookUp.setEnabled(true);
                }
            } else {
                issueLookUp.setProjectID(null);
                issueLookUp.setEnabled(false);
            }
        });

        issueLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                SelectItem selected = issueLookUp.getSelectedItem();
                selected.setDescription(RelationItem.TYPE_ISSUE);
                compareToAdd(selected);
            }
        });

        table.setWidget(1, 1, projectLookUp);
        table.setWidget(1, 2, issueLookUp);
        table.getFlexCellFormatter().getElement(1, 1).getStyle().setPadding(5.0, Style.Unit.PX);
    }

    private void eventSelectedInDropDown() {
        Label dateTitle = new Label(wfmStrings.date());
        Label eventTitle = new Label(Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.events()));
        dateTitle.setStyleName("customTitle");
        eventTitle.setStyleName("customTitle");

        table.setWidget(0, 1, dateTitle);
        table.setWidget(0, 2, eventTitle);

        if (eventDateUp == null) {
            eventDateUp = new DatePicker();
            eventDateUp.setDate(null);
        }

        if (eventLookUp == null) {
            eventLookUp = new CRMLookUp(RelationItem.TYPE_EVENT);
        }
        eventLookUp.setEnabled(false);

        eventDateUp.addChangeHandler(event -> {
            if (eventDateUp.getDate() != null) {
                eventLookUp.clearAndClearItems();
                eventLookUp.refreshOracle(true);
                eventLookUp.setDate(eventDateUp.getDate());
                eventLookUp.setEnabled(true);
            } else {
                eventLookUp.setDate(null);
                eventLookUp.setEnabled(false);
            }
        });

        eventLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                SelectItem selected = eventLookUp.getSelectedItem();
                selected.setDescription(RelationItem.TYPE_EVENT);
                compareToAdd(selected);
            }
        });

        table.setWidget(1, 1, eventDateUp);
        table.setWidget(1, 2, eventLookUp);
        table.getFlexCellFormatter().getElement(1, 1).getStyle().setPadding(5.0, Style.Unit.PX);
    }

    private void removeTaskRelatedWidgets() {
        for (int i = 0; i <= 1; i++) {
            if (table.getCellCount(i) > 2) {
                for (int j = 1; j <= 2; j++) {
                    if (table.getWidget(i, j) != null) {
                        table.getWidget(i, j).removeFromParent();
                    }
                }
            }
        }
        projectLookUp = null;
        taskLookUp = null;
        issueLookUp = null;
        eventLookUp = null;
        eventDateUp = null;
    }

    private void compareToAdd(SelectItem selected) {
        RelationItem item = new RelationItem(null, selected.getId(), selected.getDescription(), selected.getName(), fromID, fromType, fromName);
        if (!(selectedRelations.contains(item) || (item.getFromType().equals(item.getToType()) && item.getToID().equals(item.getFromID())))) {
            selectedRelations.add(item);
            if (RelationItem.TYPE_CONTACT.equals(item.getToType()) || RelationItem.TYPE_LEAD.equals(item.getToType()) || RelationItem.TYPE_OPPORTUNITY.equals(item.getToType())) {
                service.getAdditionalRelations(item.getToID(), item.getToType(), item.getToName(), fromID, fromType, fromName, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(ArrayList<RelationItem> result) {
                        if (result != null && result.size() > 1) {
                            result.remove(0);
                            for (RelationItem item_ : result) {
                                if (!selectedRelations.contains(item_)) {
                                    selectedRelations.add(item_);
                                    addRelationTable(fromType, fromID, item_);
                                }
                            }
                        }
                    }
                });
            }
            addRelationTable(fromType, fromID, item);
        }
        if (selectedRelations.contains(item)) {
            RelationItem item_ = selectedRelations.get(selectedRelations.indexOf(item));
            if (item.equals(item_) && item_.getObjectID() != null && item_.isRemove()) {
                item_.setRemove(false);
                addRelationTable(fromType, fromID, item_);
            }
        }
    }

    private void addRelationTable(String fromType, Integer fromID, final RelationItem relation) {
        String relationType = null;
        String relationToName = null;
        Integer relationId = null;
        if (fromID == null || (fromType.equals(relation.getFromType()) && fromID.equals(relation.getFromID()))) {
            relationType = relation.getToType();
            relationToName = relation.getToName();
            relationId = relation.getToID();
        } else if (fromID.equals(relation.getToID()) && fromType.equals(relation.getToType())) {
            relationType = relation.getFromType();
            relationToName = relation.getFromName();
            relationId = relation.getFromID();
        }

        final FlexTable selectedTable = new FlexTable();
        selectedTable.setWidth("100%");
        selectedTable.getElement().getStyle().setTableLayout(Style.TableLayout.FIXED);
        selectedTable.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        HTMLPanel name = new HTMLPanel("span", getReadableRelationType(relationType));
        HTML desc = new HTML(relationToName);

        if (RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Accounting.html#requestforquote|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_TASK.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + finalRelationId);
                } else {
                    Utils.openURL("ProjectManagement.html#task|summary/" + finalRelationId);
                }
            });

        } else if (RelationItem.TYPE_ISSUE.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("issue|summary/" + finalRelationId);
                } else {
                    Utils.openURL("ProjectManagement.html#issue|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_EVENT.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.addClickHandler(click -> SinksContainerFactory.entryPoint.onHistoryChanged("event|summary/" + finalRelationId));
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
        } else if (RelationItem.TYPE_CONTACT.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");

            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html") || Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#contact|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_CAMPAIGN.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("campaign|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#campaign|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_LEAD.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("lead|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#lead|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("account|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#account|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_CLIENT.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("client|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#account|summary/" + finalRelationId + "/false/Customer");
                }
            });
        } else if (RelationItem.TYPE_SUPPLIER.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                Utils.openURL("Crm.html#account|summary/" + finalRelationId + "/false/Supplier");
            });
        } else if (RelationItem.TYPE_EMPLOYEE.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("employeeProfile|employeeProfileView/" + finalRelationId);
                } else {
                    Utils.openURL("Hrms.html#employeeProfile|employeeProfileView/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_DEPARTMENT.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html") || Utils.getPathName().contains("Hrms.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("department|summary/" + finalRelationId);
                } else {
                    Utils.openURL("ProjectManagement.html#department|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_OPPORTUNITY.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Crm.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#opportunity|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_CASE.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Crm.html") || Utils.getPathName().contains("ProjectManagement.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("case|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Crm.html#case|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_SALEQUOTE.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.isCRM() ? Utils.hasPermission(CRM_SALES_QUOTE_SUMMARY) : (Utils.hasPermission(ACCOUNTING_SALES_QUOTE_SUMMARY))) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("salequote|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#salequote|summary/" + finalRelationId);
                    }
                });
            }
        } else if (RelationItem.TYPE_SALEORDER.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.hasPermission(ACCOUNTING_SALES_ORDER_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("saleorder|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#saleorder|summary/" + finalRelationId);
                    }
                });
            }
        } else if (RelationItem.TYPE_PRODUCT.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PRODUCT_SUMMARY : ACCOUNTING_PRODUCT_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("product|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#product|summary/" + finalRelationId);
                    }
                });
            }
        } else if (RelationItem.TYPE_PURCHASE_ORDER.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.hasPermission(Utils.isLogistics() ? LOGISTICS_PURCHASE_ORDER_SUMMARY : ACCOUNTING_PURCHASE_ORDER_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#purchaseorder|summary/" + finalRelationId);
                    }
                });
            }

        } else if (RelationItem.TYPE_CANDIDATE.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Hrms.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("candidate|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Hrms.html#candidate|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_MEETING_MINUTES.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Hrms.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("meetingMinutes|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Hrms.html#meetingMinutes|summary/" + finalRelationId);
                }
            });
        } else if (RelationItem.TYPE_EMAIL_TRACKER.equals(relationType)) {
            Integer finalRelationId = relation.getFromID();
            String finalRelationToName1 = relationToName;
            CommonService.App.get().getEmailbyTrackerid(finalRelationId, new AbstractAsyncCallback<Email>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(Email result) {
                    super.success(result);
                    if (result != null) {
                        desc.setHTML(finalRelationToName1 != null ? "<a href=\"javascript:\">" + finalRelationToName1 + "</a>" : "");
                        desc.addClickHandler(click -> {
                            if (Utils.getPathName().contains("Crm.html")) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("email|summary/" + result.getObjectID());
                            } else {
                                Utils.openURL("MessageCenter.html#email|summary/" + result.getObjectID());
                            }
                        });
                    }
                }
            });
        } else {
            String finalRelationType = relationType;
            Integer finalRelationId = relationId;
            String finalRelationToName = relationToName;
            CommonService.App.get().getCustomForms(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                }

                @Override
                public void onSuccess(ArrayList<SelectItem> result) {
                    super.onSuccess(result);
                    if (result != null && result.size() > 0) {
                        for (SelectItem item : result) {
                            if (item.getName().equals(finalRelationType)) {
                                if (Utils.hasPermission(item.getCode() + "_SUMMARY_" + Utils.getCompanyID())) {
                                    desc.setHTML(finalRelationToName != null ? "<a href=\"javascript:\">" + finalRelationToName + "</a>" : "");
                                    desc.addClickHandler(click -> {
                                        SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|summary/" + finalRelationId + "/" + item.getId() + "/" + item.getCode() + "/" + finalRelationType);
                                    });
                                }
                            }
                        }
                    }
                }
            });
        }


        selectedTable.setWidget(0, 0, name);
        selectedTable.getFlexCellFormatter().getElement(0, 0).getStyle().setWidth(100d, Style.Unit.PX);
        selectedTable.getFlexCellFormatter().getElement(0, 0).getStyle().setVerticalAlign(Style.VerticalAlign.TOP);
        selectedTable.getFlexCellFormatter().getElement(0, 0).getStyle().setOverflow(Style.Overflow.HIDDEN);
        selectedTable.getFlexCellFormatter().getElement(0, 0).getStyle().setTextOverflow(Style.TextOverflow.ELLIPSIS);

        selectedTable.setWidget(0, 1, desc);
        Anchor remove = new Anchor(wfmStrings.delete());
        selectedTable.getFlexCellFormatter().getElement(0, 1).getStyle().setWidth(200d, Style.Unit.PX);
        remove.addClickHandler(event -> {
            selectedTable.removeAllRows();
            table.remove(selectedTable);
            selectedRelations.remove(relation);
            relation.setRemove(true);
            if (relation.getObjectID() != null) {   // O`chirilgan relationlarni bazadan o`chirib tashlash uchun kerak.
                selectedRelations.add(relation);
            }
        });
        selectedTable.setWidget(0, 2, remove);
        selectedTable.getFlexCellFormatter().getElement(0, 2).getStyle().setWidth(50d, Style.Unit.PX);
        int rowCount = relationsTable.getRowCount();
        relationsTable.setWidget(rowCount, 0, selectedTable);
    }

    private static String getTypeValue(String type) {
        String value = type;
        if (RelationItem.TYPE_CASE.equals(type)) {
            value = Property.get(Constants.CASE_LIST, wfmStrings.crmCase());
        } else if (RelationItem.TYPE_CONTACT.equals(type)) {
            value = Property.get(Constants.Contacts, wfmStrings.contact());
        } else if (RelationItem.TYPE_CAMPAIGN.equals(type)) {
            value = wfmStrings.campaign();
        } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(type)) {
            value = wfmStrings.crmAccount();
        } else if (RelationItem.TYPE_CLIENT.equals(type)) {
            value = wfmStrings.customer();
        } else if (RelationItem.TYPE_SUPPLIER.equals(type)) {
            value = Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier());
        } else if (RelationItem.TYPE_EMPLOYEE.equals(type)) {
            value = wfmStrings.employee();
        } else if (RelationItem.TYPE_DEPARTMENT.equals(type)) {
            value = Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
        } else if (RelationItem.TYPE_EVENT.equals(type)) {
            value = Property.get(Constants.EVENT_LIST, wfmStrings.event());
        } else if (RelationItem.TYPE_LEAD.equals(type)) {
            value = Property.get(Constants.LEADS, wfmStrings.lead());
        } else if (RelationItem.TYPE_CANDIDATE.equals(type)) {
            value = wfmStrings.candidate();
        } else if (RelationItem.TYPE_OPPORTUNITY.equals(type)) {
            value = Property.get(Constants.Opportunities, wfmStrings.opportunity());
        } else if (RelationItem.TYPE_TASK.equals(type)) {
            value = Property.get(Constants.TASK, wfmStrings.task());
        } else if (RelationItem.TYPE_ISSUE.equals(type)) {
            value = Property.get(Constants.ISSUE, wfmStrings.issue());
        } else if (RelationItem.TYPE_SALEQUOTE.equals(type)) {
            value = wfmStrings.salesQuote();
        } else if (RelationItem.TYPE_SALEORDER.equals(type)) {
            value = wfmStrings.saleorder();
        } else if (RelationItem.TYPE_PURCHASE_ORDER.equals(type)) {
            value = wfmStrings.purchaseorder();
        } else if (RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(type)) {
            value = wfmStrings.requestForQuote();
        } else if (RelationItem.TYPE_STUDENT.equals(type)) {
            value = wfmStrings.student();
        } else if (RelationItem.TYPE_EMAIL_TRACKER.equals(type)) {
            value = wfmStrings.email();
        }
        return value;
    }

    public ArrayList<RelationItem> getSelectedRelations() {
        return selectedRelations;
    }

    public void setSelectedRelations(String fromType, Integer fromID, RelationItem... relations) {
        ArrayList<RelationItem> selectedRelationsList = new ArrayList<>();
        if (relationsTable != null) {
            relationsTable.removeAllRows();
        }
        if (relations != null) {
            for (RelationItem relation : relations) {
                if (relation.isTrueLinkage(fromType, fromID) && relation.getToID() != null) {
                    addRelationTable(fromType, fromID, relation);
                    selectedRelationsList.add(relation);
                }
            }
        }
        this.selectedRelations = selectedRelationsList;
    }

    private SelectItem[] getRelational() {
        if (relationals != null && relationals.size() > 0) {
            return relationals.toArray(new SelectItem[]{});
        }
        relationals = new ArrayList<>();
        int count = 0;
        if (RelationItem.TYPE_BOOKING.equals(fromType)) {
            relationals.add(new SelectItem(++count, Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.events()), RelationItem.TYPE_EVENT));
        } else {
            if (Utils.isHRMS() && Utils.hasPermission(PermissionConstants.HRMS_RECRUITMENT)) {
                relationals.add(new SelectItem(++count, wfmStrings.candidate(), RelationItem.TYPE_CANDIDATE));
            }
            relationals.add(new SelectItem(++count, wfmStrings.campaigns(), RelationItem.TYPE_CAMPAIGN));
            relationals.add(new SelectItem(++count, Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), RelationItem.TYPE_CASE));
            relationals.add(new SelectItem(++count, wfmStrings.customers(), RelationItem.TYPE_CLIENT));
            relationals.add(new SelectItem(++count, Property.get(Constants.Contacts, wfmStrings.contact()), RelationItem.TYPE_CONTACT));
            if (Utils.isTrainingCenterEnabled()) {
                relationals.add(new SelectItem(++count, wfmStrings.course(), RelationItem.TYPE_COURCE_SCHEDULE));
            }
            relationals.add(new SelectItem(++count, wfmStrings.crmAccount(), RelationItem.TYPE_CRM_ACCOUNT));
            relationals.add(new SelectItem(++count, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), RelationItem.TYPE_DEPARTMENT));
            relationals.add(new SelectItem(++count, wfmStrings.employee(), RelationItem.TYPE_EMPLOYEE));
            relationals.add(new SelectItem(++count, Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.events()), RelationItem.TYPE_EVENT));
            relationals.add(new SelectItem(++count, Property.get(Constants.ISSUE, wfmStrings.issue()), RelationItem.TYPE_ISSUE));
            relationals.add(new SelectItem(++count, Property.get(Constants.LEADS, wfmStrings.lead()), RelationItem.TYPE_LEAD));
            relationals.add(new SelectItem(++count, Property.get(Constants.Opportunities, wfmStrings.opportunity()), RelationItem.TYPE_OPPORTUNITY));
            relationals.add(new SelectItem(++count, wfmStrings.product(), RelationItem.TYPE_PRODUCT));
            relationals.add(new SelectItem(++count, Property.get(Constants.PROJECT, wfmStrings.project()), RelationItem.TYPE_PROJECT));
            relationals.add(new SelectItem(++count, Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()), RelationItem.TYPE_PURCHASE_ORDER));
            relationals.add(new SelectItem(++count, Property.get(Constants.REQUEST_FOR_QUOTE, wfmStrings.requestForQuote()), RelationItem.TYPE_REQUEST_FOR_QUOTE));
            relationals.add(new SelectItem(++count, Property.get(Constants.SALE_ORDER_CODE, wfmStrings.saleorder()), RelationItem.TYPE_SALEORDER));
            relationals.add(new SelectItem(++count, Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()), RelationItem.TYPE_SALEQUOTE));
            if (Utils.isTrainingCenterEnabled()) {
                relationals.add(new SelectItem(++count, wfmStrings.student(), RelationItem.TYPE_STUDENT));
            }
            relationals.add(new SelectItem(++count, Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), RelationItem.TYPE_SUPPLIER));
            relationals.add(new SelectItem(++count, Property.get(Constants.TASK, wfmStrings.task()), RelationItem.TYPE_TASK));
        }

        if (customForms != null && customForms.size() > 0) {
            for (SelectItem customForm : customForms) {
                relationals.add(new SelectItem(++count, customForm.getName(), customForm.getCode()));
            }
        }
        return relationals.toArray(new SelectItem[]{});
    }

    public void setSelectedRelations(ArrayList<RelationItem> selectedRelations) {
        setSelectedRelations(fromType, fromID, selectedRelations);
    }

    public void setSelectedRelations(RelationItem... selectedRelations) {
        setSelectedRelations(fromType, fromID, selectedRelations);
    }

    public void setSelectedRelations(String fromType, Integer fromID, ArrayList<RelationItem> selectedRelations) {
        if (selectedRelations != null && selectedRelations.size() > 0) {
            setSelectedRelations(fromType, fromID, selectedRelations.toArray(new RelationItem[]{}));
        }
    }

    public static FlexTable drawTags(String fromType, Integer fromID, RelationItem... items) {
        final FlexTable table = new FlexTable();
        if (items != null && items.length > 0) {
            int row = 0;
            for (RelationItem item : items) {
                if (item != null && !item.isRemove()) {
                    String type = null;
                    String name = null;
                    Integer id = null;
                    if (fromID == null || (fromType.equals(item.getFromType()) && fromID.equals(item.getFromID()))) {
                        type = item.getToType();
                        name = item.getToName();
                        id = item.getToID();
                    } else if (fromID.equals(item.getToID()) && fromType.equals(item.getToType())) {
                        type = item.getFromType();
                        name = item.getFromName();
                        id = item.getFromID();
                    }
                    if (type != null && id != null) {
                        table.setWidget(row, 0, new HTML("<span class =\"customTitle\">" + getTypeValue(type) + ":</span>"));
                        table.getCellFormatter().setWidth(row, 0, "150px");
                        table.setWidget(row, 1, new Label(name));
                        table.getCellFormatter().setWidth(row, 1, "250px");
                        row++;
                    }
                }
            }
        }
        return table;
    }

    private String getReadableRelationType(String relationType) {
        if (relationType != null && !"".equals(relationType)) {
            if (RelationItem.TYPE_TASK.equals(relationType)) {
                return Property.get(Constants.TASK, wfmStrings.task());
            }
            if (RelationItem.TYPE_ISSUE.equals(relationType)) {
                return Property.get(Constants.ISSUE, wfmStrings.issue());
            }
            if (RelationItem.TYPE_EVENT.equals(relationType)) {
                return Property.get(Constants.EVENT_LIST, wfmStrings.event());
            }
            if (RelationItem.TYPE_CONTACT.equals(relationType)) {
                return Property.get(Constants.Contacts, wfmStrings.contact());
            }
            if (RelationItem.TYPE_CAMPAIGN.equals(relationType)) {
                return wfmStrings.campaign();
            }
            if (RelationItem.TYPE_LEAD.equals(relationType)) {
                return Property.get(Constants.LEADS, wfmStrings.lead());
            }
            if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
                return wfmStrings.crmAccount();
            }
            if (RelationItem.TYPE_CLIENT.equals(relationType)) {
                return wfmStrings.customer();
            }
            if (RelationItem.TYPE_SUPPLIER.equals(relationType)) {
                return Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier());
            }
            if (RelationItem.TYPE_EMPLOYEE.equals(relationType)) {
                return wfmStrings.employee();
            }
            if (RelationItem.TYPE_DEPARTMENT.equals(relationType)) {
                return Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department());
            }
            if (RelationItem.TYPE_OPPORTUNITY.equals(relationType)) {
                return Property.get(Constants.Opportunities, wfmStrings.opportunity());
            }
            if (RelationItem.TYPE_CASE.equals(relationType)) {
                return Property.get(Constants.CASE_LIST, wfmStrings.crmCase());
            }
            if (RelationItem.TYPE_EMAIL_TRACKER.equals(relationType)) {
                return wfmStrings.email();
            }
            if (RelationItem.TYPE_BOOKING.equals(relationType)) {
                return wfmStrings.booking();
            }
            if (RelationItem.TYPE_SALEQUOTE.equals(relationType)) {
                return wfmStrings.saleorder();
            }
            if (RelationItem.TYPE_SALEORDER.equals(relationType)) {
                return wfmStrings.saleorder();
            }
            if (RelationItem.TYPE_PRODUCT.equals(relationType)) {
                return wfmStrings.product();
            }
            if (RelationItem.TYPE_CANDIDATE.equals(relationType)) {
                return wfmStrings.candidate();
            }
            if (RelationItem.TYPE_COURCE_SCHEDULE.equals(relationType)) {
                return wfmStrings.course();
            }
            if (RelationItem.TYPE_EMAIL_FILTER.equals(relationType)) {
                return wfmStrings.emailFilters();
            }
            if (RelationItem.TYPE_PURCHASE_ORDER.equals(relationType)) {
                return wfmStrings.purchaseorder();
            }
            if (RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(relationType)) {
                return wfmStrings.requestForQuote();
            }
            if (RelationItem.TYPE_STUDENT.equals(relationType)) {
                return wfmStrings.student();
            }
        }
        return relationType;
    }

    /**
     * Deprecated use method getAddLinkButton(final AddTaggingView taggingShell, final String titleOfLink)
     */
    @SuppressWarnings("deprecated")
    public static SimpleLink getAddLink(final AddTaggingView taggingShell, final String titleOfLink,
                                        final String typeEvent, final Integer typeID) {
        final SimpleLink link = new SimpleLink(titleOfLink, SimpleLink.ADD_ICON);
        link.setStyleName("tabBarLinks btn-small btn--outline btn--white");
        link.ensureDebugId("addLinks");
        link.addClickHandler(clickEvent -> {
            link.removeStyleName(Constants.ERROR_FORM_STYLE);
            if (taggingShell != null && !taggingShell.isShowing()) {
                taggingShell.open();
            }
        });
        return link;
    }

    public static Anchor getAddLinkButton(final AddTaggingView taggingShell, final String titleOfLink) {
        Anchor link = new Anchor(titleOfLink);
        link.setStyleName("markPlus");
        link.addClickHandler(clickEvent -> {
            if (taggingShell != null && !taggingShell.isShowing()) {
                taggingShell.center();
            }
        });
        return link;
    }

    public Integer getFromID() {
        return fromID;
    }

    public void setFromID(Integer fromID) {
        this.fromID = fromID;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public static Widget drawTags(AddTaggingView taggingView) {
        return drawTags(taggingView.getFromType(), taggingView.getFromID(), taggingView.getSelectedRelations().toArray(new RelationItem[]{}));
    }

    public static Widget drawRelationTags(AddTaggingView taggingView) {
        return drawRelationTags(taggingView.getFromType(), taggingView.getFromID(), taggingView.getSelectedRelations().toArray(new RelationItem[]{}));
    }

    public static class RelationTags extends Composite {

        private HorizontalPanelDiv linkagePanel;
        private FlexTable tagPanel;
        private HTMLPanel titlePanel;
        private final ArrayList<SelectItem> relationItems;

        public RelationTags(VerticalPanelDiv parent, String title, ArrayList<SelectItem> relationItems) {
            this.relationItems = relationItems;
            initialize(parent, title);
        }

        private void initialize(VerticalPanelDiv parent, String title) {
            tagPanel = new FlexTable();
            tagPanel.setCellPadding(0);
            tagPanel.setCellSpacing(5);

            titlePanel = new HTMLPanel("span", title + ":&nbsp;&nbsp;&nbsp;");

            linkagePanel = new HorizontalPanelDiv();
            tagPanel.setWidget(0, 0, titlePanel);
            tagPanel.setWidget(0, 1, linkagePanel);
            tagPanel.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
            tagPanel.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);


            initWidget(tagPanel);

            drawing(parent);
        }

        private void drawing(VerticalPanelDiv parent) {
            for (int i = 0; i < relationItems.size(); i++) {
                SelectItem relationItem = relationItems.get(i);
                final boolean isRedirect = relationItem.isNewItem();
                final String action = relationItem.getDescription();

                SimpleLink link = new SimpleLink(relationItem.getName() == null || "".equals(relationItem.getName().trim()) ? wfmStrings.noname() : relationItem.getName());
                link.addClickHandler(event -> {
                    if (isRedirect) {
                        Utils.redirect(GWT.getHostPageBaseURL() + action);
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged(action, relationItem.getName(), relationItem.getName());
                        if (action != null && action.contains("bookingitemsreservation|")) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BOOKING_RELATION_OPENED, null, null);
                        }
                    }
                });
                if (i != relationItems.size() - 1) {
                    linkagePanel.add(link);
                    linkagePanel.add(new HTML(", "));
                } else {
                    linkagePanel.add(link);
                }
                linkagePanel.setHorizontalSpacing(3);
            }
            if (!relationItems.isEmpty() && relationItems.size() > 0) {
                parent.add(this);
                parent.setVerticalSpacing(3);
            }
        }
    }
}
