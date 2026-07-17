package com.edatasite.workforce.gwt.core.client.ui.addLinkSideNavBox;


import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContractLookUp;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ShiftLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.RequestForPurchaseLookUp;
import com.edatasite.workforce.gwt.core.client.ui.view.RequestForQuoteLookUp;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_CUSTOMER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PREPAYMENT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PRODUCT_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PRODUCT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_ORDER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PURCHASE_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_REQUEST_FOR_QUOTE_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_ORDER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_QUOTE_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SALES_QUOTE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_SUPPLIER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ADD_NEW_CASE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ADD_NEW_LEAD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_ACCOUNT_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_ADD_NEW_CAMPAIGN;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_ADD_NEW_CONTACT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_ADD_NEW_OPPORTUNITIES;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_PURCHASE_INVOICE_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_PURCHASE_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_PURCHASE_ORDER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_PURCHASE_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_REQUEST_FOR_QUOTE_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_ORDER_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_QUOTE_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_SALES_QUOTE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_TASKS_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ADD_NEW_DEPARTMENT;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ADD_NEW_EMPLOYEE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_SHIFT_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PRODUCT_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PURCHASE_INVOICE_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.LOGISTICS_PURCHASE_ORDER_SUMMARY;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PM_CONTRACT_LIST;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PM_ISSUE_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PM_PROJECT_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.PM_TASKS_ADD;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.REQUEST_FOR_PURCHASES;


public class AddLinkSideNavBox extends KpiSideNavBox {

    private ArrayList<RelationItem> relationItemsList = new ArrayList<>();

    private DataListBox sectionList;
    private ArrayList<SelectItem> relationals;
    private List<SelectItem> customForms;
    private LookUp lookUp;
    private LookUp projectLookUp;
    private CRMLookUp taskLookUp;
    private CRMLookUp issueLookUp;
    private CRMLookUp eventLookUp;
    private DatePicker eventDateUp;
    private final FlexTable relationsTable;
    private final MaterialPanel linkToPanel;
    private final MaterialPanel callRelatedPanel;
    private final FlowPanel panel;
    private final Integer fromID;
    private final String fromType;
    private final String fromName;
    private final boolean isActionEditing;


    public AddLinkSideNavBox(Integer fromID, String fromType, String fromName, boolean isActionEditing) {
        super(550);
        this.fromID = fromID;
        this.fromType = fromType;
        this.fromName = fromName;
        this.isActionEditing = isActionEditing;
        panel = new FlowPanel();
        linkToPanel = new MaterialPanel();
        callRelatedPanel = new MaterialPanel();
        relationsTable = new FlexTable();
        relationsTable.addStyleName("addLinksResults");
        CommonService.App.get().getCustomForms(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);

                initForm();
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                super.onSuccess(result);
                customForms = result;
                initForm();
            }
        });
    }

    private void initForm() {
        setStyleName(getElement(), "quick-add file--AddLinkSideNavBox", true);
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.addLinks());
        addHeader(header);

        sectionList = new DataListBox();
        sectionList.setItems(getRelational());

        sectionList.setChangeEvent(() -> {
            HTML addNew = new HTML();

            if (checkConvertProperty(sectionList.getSelectedItem().getDescription()) && ((fromType.contains("_FORM") &&
                    (RelationItem.TYPE_OPPORTUNITY.equals(sectionList.getSelectedItem().getDescription()) ||
                            RelationItem.TYPE_SALEORDER.equals(sectionList.getSelectedItem().getDescription()) ||
                            RelationItem.TYPE_SALEQUOTE.equals(sectionList.getSelectedItem().getDescription()) ||
                            RelationItem.TYPE_PURCHASE_INVOICE.equals(sectionList.getSelectedItem().getDescription()) ||
                            RelationItem.TYPE_PURCHASE_ORDER.equals(sectionList.getSelectedItem().getDescription()))) ||
                    (fromType.equals(RelationItem.TYPE_REQUEST_FOR_QUOTE) &&
                            (RelationItem.TYPE_OPPORTUNITY.equals(sectionList.getSelectedItem().getDescription()) ||
                                    RelationItem.TYPE_SALEQUOTE.equals(sectionList.getSelectedItem().getDescription()) ||
                                    RelationItem.TYPE_PURCHASE_ORDER.equals(sectionList.getSelectedItem().getDescription()) ||
                                    sectionList.getSelectedItem().getDescription().contains("_FORM") ||
                                    RelationItem.TYPE_SALEORDER.equals(sectionList.getSelectedItem().getDescription()))) ||
                    (fromType.equals(RelationItem.TYPE_OPPORTUNITY) &&
                            (sectionList.getSelectedItem().getDescription().contains("_FORM"))) ||
                    (fromType.equals(RelationItem.TYPE_CASE) &&
                            (sectionList.getSelectedItem().getDescription().contains("_FORM"))) ||
                    (fromType.equals(RelationItem.TYPE_PURCHASE_ORDER) &&
                            (RelationItem.TYPE_CASE.equals(sectionList.getSelectedItem().getDescription()) ||
                                    sectionList.getSelectedItem().getDescription().contains("_FORM"))) ||
                    (fromType.equals(RelationItem.TYPE_SALEQUOTE) &&
                            (RelationItem.TYPE_CASE.equals(sectionList.getSelectedItem().getDescription()) ||
                                    sectionList.getSelectedItem().getDescription().contains("_FORM"))) ||
                    (fromType.equals(RelationItem.TYPE_SALEORDER) &&
                            (RelationItem.TYPE_CASE.equals(sectionList.getSelectedItem().getDescription()) ||
                                    sectionList.getSelectedItem().getDescription().contains("_FORM"))))) {
                addNew.setHTML("<a href=\"javascript:\">" + wfmStrings.convert() + "</a>");
                addNew.addClickHandler(addNewCl -> addNewForm(sectionList.getSelectedItem().getDescription()));
            } else {
                addNew.setHTML("");
            }
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
                    if (lookUp == null) {
                        if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_REQUEST_FOR_QUOTE)) {
                            lookUp = new RequestForQuoteLookUp();
                        }
                        if (sectionList.getSelectedItem().getDescription().equals(RelationItem.REQUEST_FOR_PURCHASE)) {
                            lookUp = new RequestForPurchaseLookUp();
                        } else if (sectionList.getSelectedItem().getDescription().contains("_FORM")) {
                            lookUp = new CRMLookUp(true, sectionList.getSelectedItem().getDescription());
                        } else if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_SHIFT)) {
                            lookUp = new ShiftLookUp(null);
                        } else if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_PROJECT)) {
                            lookUp = new CRMLookUp(sectionList.getSelectedItem().getDescription().toUpperCase());
                        } else if (sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_CONTRACT)) {
                            lookUp = new ContractLookUp();
                        } else {
                            lookUp = new CRMLookUp(sectionList.getSelectedItem().getDescription());
                        }

                        lookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                            if (suggestionSelectionEvent.getSelectedItem() != null) {
                                SelectItem selected = lookUp.getSelectedItem();
                                selected.setDescription(sectionList.getSelectedItem().getDescription().contains("_FORM") ? sectionList.getSelectedItem().getName() : sectionList.getSelectedItem().getDescription());
                                compareToAdd(selected);
                                if ((sectionList.getSelectedItem().getDescription().equals(RelationItem.TYPE_REQUEST_FOR_QUOTE) || sectionList.getSelectedItem().getDescription().equals(RelationItem.REQUEST_FOR_PURCHASE)) && selected.getEntityId() != null) {
                                    compareToAdd(new SelectItem(selected.getEntityId(), selected.getCode(), RelationItem.TYPE_CLIENT));
                                }
                            }
                        });
                    }

                    AdvancedInputGroup addNewAccountLink = new AdvancedInputGroup(null, lookUp, addNew, true, true);
                    GRow row = new GRow();
                    row.add(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.relatedTo(), sectionList)));
                    if (addNew != null && addNew.getHTML() != null && addNew.getHTML().length() > 0) {
                        row.add(new GColumn(GColumnEnum.COL_6, new FormGroup("&nbsp;", addNewAccountLink)));
                    } else {
                        row.add(new GColumn(GColumnEnum.COL_6, new FormGroup("&nbsp;", lookUp)));
                    }
                    callRelatedPanel.clear();
                    callRelatedPanel.add(row);
                }
            }
        });
        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_6, new FormGroup(wfmStrings.relatedTo(), sectionList)));
        TextBox text = new TextBox();
        text.setEnabled(false);
        row.add(new GColumn(GColumnEnum.COL_6, new FormGroup("&nbsp;", text)));
        callRelatedPanel.add(row);

        panel.add(callRelatedPanel);

        linkToPanel.addStyleName("addLinksResults__wrapper");
        panel.add(linkToPanel);
        addBody(panel);
//        show();
    }

    private SelectItem[] getRelational() {
        if (relationals != null && relationals.size() > 0) {
            return relationals.toArray(new SelectItem[]{});
        }
        relationals = new ArrayList<>();
        int count = 0;
        if (Utils.hasPermission(CRM_ACCOUNT_ADD)) {
            relationals.add(new SelectItem(++count, wfmStrings.crmAccount(), RelationItem.TYPE_CRM_ACCOUNT));
        }
        if (Utils.hasPermission(CRM_ADD_NEW_CAMPAIGN)) {
            relationals.add(new SelectItem(++count, wfmStrings.campaign(), RelationItem.TYPE_CAMPAIGN));
        }
        if (Utils.isHRMS() && Utils.hasPermission(PermissionConstants.HRMS_RECRUITMENT)) {
            relationals.add(new SelectItem(++count, wfmStrings.candidate(), RelationItem.TYPE_CANDIDATE));
        }
        if (Utils.hasPermission(ADD_NEW_CASE)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), RelationItem.TYPE_CASE));
        }
        if (Utils.hasPermission(CRM_ADD_NEW_CONTACT)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.Contacts, wfmStrings.contact()), RelationItem.TYPE_CONTACT));
        }
        if (Utils.hasPermission(PM_CONTRACT_LIST)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.CONTRACT_LIST, wfmStrings.contract()), RelationItem.TYPE_CONTRACT));
        }
        if (Utils.isTrainingCenterEnabled()) {
            relationals.add(new SelectItem(++count, wfmStrings.course(), RelationItem.TYPE_COURCE_SCHEDULE));
        }
        if (Utils.hasPermission(ACCOUNTING_CUSTOMER_ADD)) {
            relationals.add(new SelectItem(++count, wfmStrings.customer(), RelationItem.TYPE_CLIENT));
        }
        if (Utils.hasPermission(HRMS_ADD_NEW_DEPARTMENT)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), RelationItem.TYPE_DEPARTMENT));
        }
        if (Utils.hasPermission(HRMS_ADD_NEW_EMPLOYEE)) {
            relationals.add(new SelectItem(++count, wfmStrings.employee(), RelationItem.TYPE_EMPLOYEE));
        }
        if (Utils.hasPermission(HRMS_SHIFT_ADD)) {
            relationals.add(new SelectItem(++count, wfmStrings.shift(), RelationItem.TYPE_SHIFT));
        }
        if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_EVENT)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.EVENT_LIST, wfmStrings.event()), RelationItem.TYPE_EVENT));
        }
        if (Utils.hasPermission(PM_ISSUE_ADD)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.ISSUE, wfmStrings.issue()), RelationItem.TYPE_ISSUE));
        }
        if (Utils.hasPermission(ADD_NEW_LEAD)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.LEADS, wfmStrings.lead()), RelationItem.TYPE_LEAD));
        }
        if (Utils.hasPermission(CRM_ADD_NEW_OPPORTUNITIES)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.Opportunities, wfmStrings.opportunity()), RelationItem.TYPE_OPPORTUNITY));
        }
        if (Utils.hasPermission(ACCOUNTING_PRODUCT_ADD)) {
            relationals.add(new SelectItem(++count, wfmStrings.product(), RelationItem.TYPE_PRODUCT));
        }
        if (Utils.hasPermission(PM_PROJECT_ADD)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.PROJECT, wfmStrings.project()), RelationItem.TYPE_PROJECT));
        }
        if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_PURCHASE_ORDER_ADD)) : Utils.hasPermission(ACCOUNTING_PURCHASE_ORDER_ADD)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()), RelationItem.TYPE_PURCHASE_ORDER));
        }
        if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_PURCHASE_INVOICE_ADD)) : Utils.hasPermission(ACCOUNTING_PURCHASE_INVOICE_ADD)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.PURCHASE_INVOICE, wfmStrings.purchaseinvoice()), RelationItem.TYPE_PURCHASE_INVOICE));
        }
        if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_REQUEST_FOR_QUOTE_ADD)) : Utils.hasPermission(ACCOUNTING_REQUEST_FOR_QUOTE_ADD)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.REQUEST_FOR_QUOTE, wfmStrings.requestForQuote()), RelationItem.TYPE_REQUEST_FOR_QUOTE));
        }
        if (Utils.isAccountingSetup() && Utils.hasModuleEnabled(REQUEST_FOR_PURCHASES)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.REQUEST_FOR_PURCHASE, wfmStrings.requestForPurchase()), RelationItem.REQUEST_FOR_PURCHASE));
        }
        if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_SALES_ORDER_ADD)) : Utils.hasPermission(ACCOUNTING_SALES_ORDER_ADD)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.SALE_ORDER_CODE, wfmStrings.saleorder()), RelationItem.TYPE_SALEORDER));
        }
        if (Utils.isCRM() ? (Utils.isAccountingSetup() && Utils.hasPermission(CRM_SALES_QUOTE_ADD)) : Utils.hasPermission(ACCOUNTING_SALES_QUOTE_ADD)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()), RelationItem.TYPE_SALEQUOTE));
        }
        if (Utils.isTrainingCenterEnabled()) {
            relationals.add(new SelectItem(++count, wfmStrings.student(), RelationItem.TYPE_STUDENT));
        }
        if (Utils.hasPermission(ACCOUNTING_SUPPLIER_ADD)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.SUPPLIER_LIST, wfmStrings.supplier()), RelationItem.TYPE_SUPPLIER));
        }
        if (Utils.isCRM() ? Utils.hasPermission(CRM_TASKS_ADD) : Utils.hasPermission(PM_TASKS_ADD)) {
            relationals.add(new SelectItem(++count, Property.get(Constants.TASK, wfmStrings.task()), RelationItem.TYPE_TASK));
        }

        if (customForms != null && customForms.size() > 0) {
            for (SelectItem customForm : customForms) {
                if (Utils.hasPermission(customForm.getCode() + "_ADD_" + Utils.getCompanyID())) {
                    relationals.add(new SelectItem(++count, customForm.getName(), customForm.getCode()));
                }
            }
        }
        return relationals.toArray(new SelectItem[]{});
    }

    private void compareToAdd(SelectItem selected) {
        linkToPanel.setVisible(true);
        RelationItem item = new RelationItem(null, selected.getId(), selected.getDescription(), selected.getName(), fromID, fromType, fromName);

        if (validateRelationItem(item)) {
            addRelationTable(fromType, fromID, item);
            getRelationByType(item);
        }

        if (relationItemsList.contains(item)) {
            RelationItem item_ = relationItemsList.get(relationItemsList.indexOf(item));
            if (item.equals(item_) && item_.getObjectID() != null && item_.isRemove()) {
                item_.setRemove(false);
                addRelationTable(fromType, fromID, item_);
                save();
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

        HTML name = new HTML(getReadableRelationType(relationType));
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
        } else if (RelationItem.REQUEST_FOR_PURCHASE.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Accounting.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("requestforpurchase|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Accounting.html#requestforpurchase|summary/" + finalRelationId);
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
        } else if (RelationItem.TYPE_EVENT.equals(relationType) || CrmConstants.CRM_EVENT_CALLOG.equals(relationType)) {
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
            if (Utils.isCRM() ? Utils.hasPermission(PermissionConstants.CRM_SALES_ORDER_SUMMARY) : Utils.hasPermission(ACCOUNTING_SALES_ORDER_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("saleorder|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#saleorder|summary/" + finalRelationId);
                    }
                });
            }
        } else if (RelationItem.TYPE_SUPPLIER_PREPAYMENTS.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.hasPermission(ACCOUNTING_PREPAYMENT_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment|summary/" + finalRelationId + "/supplierCredit");
                    } else {
                        Utils.openURL("Accounting.html#invoicepayment|summary/" + finalRelationId + "/supplierCredit");
                    }
                });
            }
        } else if (RelationItem.TYPE_CUSTOMER_PREPAYMENTS.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.hasPermission(ACCOUNTING_PREPAYMENT_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("invoicepayment|summary/" + finalRelationId + "/prepayment");
                    } else {
                        Utils.openURL("Accounting.html#invoicepayment|summary/" + finalRelationId + "/prepayment");
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
        } else if (RelationItem.TYPE_PROJECT.equals(relationType)) {
            Integer finalRelationId = relationId;

            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("ProjectManagement.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("project|summary/" + finalRelationId);
                } else {
                    Utils.openURL("ProjectManagement.html#project|summary/" + finalRelationId);
                }
            });

        } else if (RelationItem.TYPE_PURCHASE_ORDER.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.hasPermission(Utils.isCRM() ? CRM_PURCHASE_ORDER_SUMMARY : Utils.isLogistics() ? LOGISTICS_PURCHASE_ORDER_SUMMARY : ACCOUNTING_PURCHASE_ORDER_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#purchaseorder|summary/" + finalRelationId);
                    }
                });
            }

        } else if (RelationItem.TYPE_PURCHASE_INVOICE.equals(relationType)) {
            Integer finalRelationId = relationId;
            if (Utils.hasPermission(Utils.isCRM() ? CRM_PURCHASE_INVOICE_SUMMARY : Utils.isLogistics() ? LOGISTICS_PURCHASE_INVOICE_SUMMARY : ACCOUNTING_PURCHASE_INVOICE_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#purchaseinvoice|summary/" + finalRelationId);
                    }
                });
            }

        } else if (RelationItem.TYPE_SALEINVOICE.equals(relationType)) {
            Integer finalRelationId = relationId;

            if (Utils.isCRM() ? Utils.hasPermission(PermissionConstants.CRM_SALES_INVOICE_SUMMARY) : Utils.hasPermission(ACCOUNTING_SALES_INVOICE_SUMMARY)) {
                desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
                desc.addClickHandler(click -> {
                    if (Utils.getPathName().contains("Accounting.html")) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("saleinvoice|summary/" + finalRelationId);
                    } else {
                        Utils.openURL("Accounting.html#saleinvoice|summary/" + finalRelationId);
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
        } else if (RelationItem.TYPE_SHIFT.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Hrms.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("shift|add/summary/" + finalRelationId);
                } else {
                    Utils.openURL("Hrms.html#shift|add/summary/" + finalRelationId);
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
        } else if (RelationItem.TYPE_VACANCY.equals(relationType)) {
            Integer finalRelationId = relationId;
            desc.setHTML(relationToName != null ? "<a href=\"javascript:\">" + relationToName + "</a>" : "");
            desc.addClickHandler(click -> {
                if (Utils.getPathName().contains("Hrms.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|summary/" + finalRelationId);
                } else {
                    Utils.openURL("Hrms.html#vacancy|summary/" + finalRelationId);
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
                            if ((item.getCode().equals(finalRelationType) || item.getName().equals(finalRelationType)) && Utils.hasPermission(item.getCode() + "_SUMMARY_" + Utils.getCompanyID())) {
                                name.setHTML(item.getName());
                                desc.setHTML(finalRelationToName != null ? "<a href=\"javascript:\">" + finalRelationToName + "</a>" : "");
                                desc.addClickHandler(click -> {
                                    SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|summary/" + finalRelationId + "/" + item.getId() + "/" + item.getCode() + "/" + finalRelationType);
                                });
                            }
                        }
                    }
                }
            });
        }


        DynamicTable table = new DynamicTable(getColumns(), false);
        table.setStyleName("RelatedTo-selection"); //https://prnt.sc/rq1ghy

        Icon removeIcon = new Icon();
        removeIcon.addStyleName("btn--icon");
        SvgIcon trashIcon = new SvgIcon(SvgEnum.trash2);
        removeIcon.add(trashIcon);


        removeIcon.addClickHandler(event -> {
            table.removeAllRows();
            relationItemsList.remove(relation);
            relation.setRemove(true);
            if (relation.getObjectID() != null) {   // O`chirilgan relationlarni bazadan o`chirib tashlash uchun kerak.
                relationItemsList.add(relation);
            }

            if (relationItemsList != null && relationItemsList.size() == 0) {
                linkToPanel.setVisible(false);
            }
            save();
        });

        LinkedHashMap<String, Widget> itemWidgetsMap = new LinkedHashMap<>();
        itemWidgetsMap.put("relatedTo", name);

        itemWidgetsMap.put("item", desc);
        itemWidgetsMap.put("remove", removeIcon);

        table.addRow(1, itemWidgetsMap.values().toArray(new Widget[]{}));

        int rowCount = relationsTable.getRowCount();
        relationsTable.setWidget(rowCount, 0, table);
        linkToPanel.add(relationsTable);

    }

    protected DynamicTableColumn[] getColumns() {
        ArrayList<DynamicTableColumn> headers = new ArrayList<>();
        headers.add(new DynamicTableColumn("", "relatedTo", 150, false));
        headers.add(new DynamicTableColumn("", "item", 350, false));
        headers.add(new DynamicTableColumn("", "delete", 50, false));
        return headers.toArray(new DynamicTableColumn[]{});
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
            if (CrmConstants.CRM_EVENT_CALLOG.equals(relationType)) {
                return Property.get(Constants.LOGACALL, wfmStrings.logCall());
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
                return wfmStrings.customers();
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
                return Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote());
            }
            if (RelationItem.TYPE_SALEORDER.equals(relationType)) {
                return Property.get(Constants.SALE_ORDER_CODE, wfmStrings.saleorder());
            }
            if (RelationItem.TYPE_SALEINVOICE.equals(relationType)) {
                return Property.get(Constants.SALE_INVOICE, wfmStrings.salesInvoice());
            }
            if (RelationItem.TYPE_PRODUCT.equals(relationType)) {
                return wfmStrings.product();
            }
            if (RelationItem.TYPE_PROJECT.equals(relationType)) {
                return Property.get(Constants.PROJECT, wfmStrings.project());
            }
            if (RelationItem.TYPE_CANDIDATE.equals(relationType)) {
                return wfmStrings.candidate();
            }
            if (RelationItem.TYPE_SHIFT.equals(relationType)) {
                return wfmStrings.shift();
            }
            if (RelationItem.TYPE_COURCE_SCHEDULE.equals(relationType)) {
                return wfmStrings.course();
            }
            if (RelationItem.TYPE_EMAIL_FILTER.equals(relationType)) {
                return wfmStrings.emailFilters();
            }
            if (RelationItem.TYPE_PURCHASE_ORDER.equals(relationType)) {
                return Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder());
            }
            if (RelationItem.TYPE_PURCHASE_INVOICE.equals(relationType)) {
                return Property.get(Constants.PURCHASE_INVOICE, wfmStrings.purchaseinvoice());
            }
            if (RelationItem.TYPE_REQUEST_FOR_QUOTE.equals(relationType)) {
                return Property.get(Constants.REQUEST_FOR_QUOTE, wfmStrings.requestForQuote());
            }
            if (RelationItem.REQUEST_FOR_PURCHASE.equals(relationType)) {
                return Property.get(Constants.REQUEST_FOR_PURCHASE, wfmStrings.requestForPurchase());
            }
            if (RelationItem.TYPE_STUDENT.equals(relationType)) {
                return wfmStrings.student();
            }
            if (RelationItem.TYPE_CUSTOMER_PREPAYMENTS.equals(relationType)) {
                return wfmStrings.prepayments();
            }
            if (RelationItem.TYPE_SUPPLIER_PREPAYMENTS.equals(relationType)) {
                return wfmStrings.supplierCredits();
            }
        }
        return relationType;
    }

    private void removeTaskRelatedWidgets() {
        projectLookUp = null;
        taskLookUp = null;
        issueLookUp = null;
        eventLookUp = null;
        eventDateUp = null;
    }

    private void taskSelectedInDropDown() {
        if (projectLookUp == null) {
            projectLookUp = new CRMLookUp(RelationItem.TYPE_PROJECT.toUpperCase());
        }
        if (taskLookUp == null) {
            taskLookUp = new CRMLookUp(RelationItem.TYPE_TASK.toUpperCase());
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
                selected.setDescription(RelationItem.TYPE_TASK.toUpperCase());
                compareToAdd(selected);
            }
        });

        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(wfmStrings.relatedTo(), sectionList)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(Property.get(Constants.TASK, wfmStrings.task()), taskLookUp)));
        callRelatedPanel.clear();
        callRelatedPanel.add(row);
    }

    private void issueSelectedInDropDown() {

        if (projectLookUp == null) {
            projectLookUp = new CRMLookUp(RelationItem.TYPE_PROJECT.toUpperCase());
        }
        if (issueLookUp == null) {
            issueLookUp = new CRMLookUp(RelationItem.TYPE_ISSUE.toUpperCase());
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

        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(wfmStrings.relatedTo(), sectionList)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(Property.get(Constants.PROJECT, wfmStrings.project()), projectLookUp)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(Property.get(Constants.ISSUE, wfmStrings.issue()), issueLookUp)));
        callRelatedPanel.clear();
        callRelatedPanel.add(row);
    }

    private void eventSelectedInDropDown() {

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

        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(wfmStrings.relatedTo(), sectionList)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(wfmStrings.date(), eventDateUp)));
        row.add(new GColumn(GColumnEnum.COL_4, new FormGroup(Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.events()), eventLookUp)));
        callRelatedPanel.clear();
        callRelatedPanel.add(row);
    }

    public ArrayList<RelationItem> getSelectedRelations() {
        return relationItemsList;
    }

    public void setSelectedRelations(List<RelationItem> relations, boolean isNew) {
        if (relationsTable != null) {
            relationsTable.removeAllRows();
        }
        if (relations != null && relations.size() > 0) {
            for (RelationItem relationItem : relations) {
                if (relationItem != null && relationItem.getToID() != null) {
                    this.relationItemsList.add(relationItem);
                    addRelationTable(fromType, fromID, relationItem);
                    if (isNew) {
                        getRelationByType(relationItem);
                    }
                }
            }
        }
    }

    public void setSelectedRelations(String fromType, Integer fromID, List<RelationItem> relations) {
        if (relationsTable != null) {
            relationsTable.removeAllRows();
        }
        if (relations != null && relations.size() > 0) {
            for (RelationItem relation : relations) {
                if (relation != null && relation.getToID() != null) {
                    this.relationItemsList.add(relation);
                    addRelationTable(fromType, fromID, relation);
                    getRelationByType(relation);
                }
            }
        }
    }

    private void save() {
        if (fromName != null && fromID != null) {
            RelationItem.setFromName(fromID, fromType, fromName, relationItemsList);
        }
        if (fromID != null && !isActionEditing) {
            LoadingPanel.loading(true);
            AllInOneService.App.get().saveRelations(fromType, fromID, fromName, relationItemsList, true, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    remove();
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(ArrayList<RelationItem> selectItems) {
                    relationItemsList = selectItems;
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    private boolean validateRelationItem(RelationItem relationItem) {
        boolean haveRelationItem = false;

        for (RelationItem item : relationItemsList) {

            if (item.getToType().equals(relationItem.getToType()) && item.getToID().equals(relationItem.getToID())) {
                haveRelationItem = true;
                break;
            }
        }
        if (!haveRelationItem) {
            relationItemsList.add(relationItem);
            return true;
        }
        return false;
    }

    private void getRelationByType(RelationItem relationItem) {

        if (relationItem.getToType() != null && relationItem.getToID() != null) {
            AllInOneService.App.get().getAdditionalRelations(relationItem.getToID(), relationItem.getToType(), relationItem.getToName(), fromID, fromType, fromName, new AbstractAsyncCallback<ArrayList<RelationItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                }

                @Override
                public void onSuccess(ArrayList<RelationItem> result) {
                    if (result != null && result.size() > 1) {
                        result.remove(0);
                        for (RelationItem item_ : result) {
                            if (validateRelationItem(item_)) {
                                addRelationTable(fromType, fromID, item_);
                            }
                        }
                    }
                    save();
                }
            });
        }
    }

    public void addItem(RelationItem relationItem) {
        if (relationItem != null && relationItem.getToID() != null) {
            if (validateRelationItem(relationItem)) {
                addRelationTable(fromType, fromID, relationItem);
            }
        }
    }

    private void addNewForm(String section) {

        if (RelationItem.TYPE_OPPORTUNITY.equals(section)) {
            if (Utils.getPathName().contains("Crm.html") || Utils.getPathName().contains("Accounting.html")) {
                SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/CONVERT/" + fromType + "/" + fromID);
                remove();
            } else {
                Utils.openURL("Crm.html#opportunity|add/add/CONVERT/" + fromType + "/" + fromID);
            }
        } else if (RelationItem.TYPE_SALEQUOTE.equals(section)) {
            if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                SinksContainerFactory.entryPoint.onHistoryChanged("salequote|edit/CONVERT/" + fromType + "/" + fromID);
                remove();
            } else {
                Utils.openURL("Accounting.html#salequote|edit/CONVERT/" + fromType + "/" + fromID);
            }
        } else if (RelationItem.TYPE_SALEORDER.equals(section)) {
            if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                SinksContainerFactory.entryPoint.onHistoryChanged("saleorder|edit/CONVERT/" + fromType + "/" + fromID);
                remove();
            } else {
                Utils.openURL("Accounting.html#saleorder|edit/CONVERT/" + fromType + "/" + fromID);
            }
        } else if (RelationItem.TYPE_PURCHASE_ORDER.equals(section)) {
            if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                SinksContainerFactory.entryPoint.onHistoryChanged("purchaseorder|edit/CONVERT/" + fromType + "/" + fromID);
                remove();
            } else {
                Utils.openURL("Accounting.html#purchaseorder|edit/CONVERT/" + fromType + "/" + fromID);
            }
        } else if (RelationItem.TYPE_PURCHASE_INVOICE.equals(section)) {
            if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                SinksContainerFactory.entryPoint.onHistoryChanged("purchaseinvoice|edit/CONVERT/" + fromType + "/" + fromID);
                remove();
            } else {
                Utils.openURL("Accounting.html#purchaseinvoice|edit/CONVERT/" + fromType + "/" + fromID);
            }
        } else if (RelationItem.TYPE_CASE.equals(section)) {
            if (Utils.getPathName().contains("Accounting.html") || Utils.getPathName().contains("Crm.html")) {
                SinksContainerFactory.entryPoint.onHistoryChanged("case|add/add/CONVERT/" + fromType + "/" + fromID);
                remove();
            } else {
                Utils.openURL("Crm.html#case|add/add/CONVERT/" + fromType + "/" + fromID);
            }
        } else if (RelationItem.TYPE_VACANCY.equals(section)) {
            if (Utils.getPathName().contains("Hrms.html") || Utils.getPathName().contains("Crm.html")) {
                SinksContainerFactory.entryPoint.onHistoryChanged("vacancy|add/add/CONVERT/" + fromType + "/" + fromID);
                remove();
            } else {
                Utils.openURL("Hrms.html#vacancy|add/add/CONVERT/" + fromType + "/" + fromID);
            }
        } else if (section.contains("_FORM")) {

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
                            if ((item.getCode().equals(section) || item.getName().equals(section))) {
                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + item.getEntityId() + "/" + item.getCode() + "/CONVERT/" + fromType + "/" + fromID);
                                remove();
                            }
                        }
                    }
                }
            });
        }
    }

    private boolean checkConvertProperty(String section) {
        boolean havePermission = false;
        String propertyCode = null;
        switch (fromType) {
            case RelationItem.TYPE_OPPORTUNITY:
                propertyCode = Constants.Opportunities;
                break;
            case RelationItem.TYPE_REQUEST_FOR_QUOTE:
                propertyCode = Constants.REQUEST_FOR_QUOTE;
                break;
            case RelationItem.REQUEST_FOR_PURCHASE:
                propertyCode = Constants.REQUEST_FOR_PURCHASE;
                break;
            case RelationItem.TYPE_SALEQUOTE:
                propertyCode = Constants.SALE_QUOTE;
                break;
            case RelationItem.TYPE_SALEORDER:
                propertyCode = Constants.SALE_ORDER_CODE;
                break;
            case RelationItem.TYPE_PURCHASE_ORDER:
                propertyCode = Constants.PURCHASE_ORDER;
                break;
            case RelationItem.TYPE_PURCHASE_INVOICE:
                propertyCode = Constants.PURCHASE_INVOICE;
                break;
            case RelationItem.TYPE_CASE:
                propertyCode = Constants.CASE_LIST;
                break;
            case RelationItem.TYPE_VACANCY:
                propertyCode = Constants.VACANCY;
                break;
        }

        if (fromType.contains("_FORM")) {
            propertyCode = fromType != null && fromType.length() > 5 ? fromType.substring(0, fromType.length() - 5) : null;
        }
        if (propertyCode != null) {
            PropertyItem propertyItem = Utils.getProperTy(propertyCode);
            if (propertyItem != null && propertyItem.getConvertItems() != null) {
                for (ConvertItem convertItem : propertyItem.getConvertItems()) {
                    if (section.equals(convertItem.getCode())) {
                        havePermission = true;
                        break;
                    }
                }
            }
        }
        return havePermission;
    }
}

