package com.edatasite.workforce.gwt.profile.client.ui.view.leaveRequestApproversView;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.libideas.validation.client.ValidationException;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Hayot
 * Date: 2/18/15 4:07 PM
 */
public class ApproversWidget extends Composite implements Constants {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();
    private final String type;
    private boolean isOnboarding = false;
    private Integer approveStatusID = null;
    private Integer rejectStatusID = null;
    private Integer startStatusID = null;
    private final String onboardingtype;
    private final String ROLE_LIST_BOX = "ROLE_LIST_BOX";
    private final String EMPLOYEE_LOOKUP = "EMPLOYEE_LOOKUP";
    private final String ROLE_RADIO_BUTTON = "ROLE_RADIO_BUTTON";
    private final String EMPLOYEE_RADIO_BUTTON = "EMPLOYEE_RADIO_BUTTON";
    private final String DYNAMIC_RADIO_BUTTON = "DYNAMIC_RADIO_BUTTON";
    private final String LOCATION_RADIO_BUTTON = "LOCATION_RADIO_BUTTON";

    private final String DYNAMIC_QUERY = "DYNAMIC_QUERY";
    private final String APPROVE_FOR_ALL = "APPROVE_FOR_ALL";
    private ArrayList<SelectItem> roles = new ArrayList<>();
    private DataListBox approveStatus;
    private DataListBox rejectStatus;
    private DataListBox startStatus;
    private final ArrayList<DataListBox> rolesListBoxTemp = new ArrayList<>();
    private final KpiCheckBox hierarchicalApproval;
    private FlexTable table;
    private final LinkedList<ApproverItem> approverTable = new LinkedList<>();
    private final VerticalPanel vp = new VerticalPanel();
    private MaterialLink adder;

    public ApproversWidget(String type, String onboardingtype, KpiCheckBox hierarchicalApproval) {
        this.type = type;
        this.onboardingtype = onboardingtype;
        this.hierarchicalApproval = hierarchicalApproval;
        isOnboarding = RelationItem.TYPE_EMPLOYEE_STEP.equals(onboardingtype);
        if (isOnboarding) {
            setupOnboardingStepTable();
        }
        setupTable();
        vp.setStyleName("approvers-table");
        vp.setWidth("100%");
        vp.add(table);
        vp.add(adder);
        //vp.add(saveButton);
        initDefaultDatas();
        initWidget(vp);
    }

    private void setupOnboardingStepTable() {
        FlexTable onboardingStatusTable = new FlexTable();
        onboardingStatusTable.addStyleName("onBoardingStepTable");
        approveStatus = new DataListBox();
        rejectStatus = new DataListBox();
        startStatus = new DataListBox();

        initOnboardingStatus();
        onboardingStatusTable.getElement().addClassName("td-padding-4");
        onboardingStatusTable.setText(0, 0, "Approve Status");/*Step*/
        onboardingStatusTable.setWidget(0, 1, approveStatus);
        onboardingStatusTable.setText(0, 2, "Reject Status");
        onboardingStatusTable.setWidget(0, 3, rejectStatus);
        onboardingStatusTable.setText(0, 4, "Start Status");
        onboardingStatusTable.setWidget(0, 5, startStatus);
        vp.add(onboardingStatusTable);
    }

    private void initOnboardingStatus() {
        AllInOneService.App.get().getEmployeeStepSatues(type, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SelectItem[] selectItems) {
                approveStatus.setItems(selectItems);
                rejectStatus.setItems(selectItems);
                startStatus.setItems(selectItems);
            }
        });
    }

    private void initDefaultDatas() {
        // get all roles from database.
        AllInOneService.App.get().getAllRoles(new AsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<SelectItem> selectItems) {
                roles = selectItems;
                if (roles != null && roles.size() > 0 && rolesListBoxTemp.size() > 0) {
                    //find all roles list box and add items to them.
                    for (DataListBox rolesListBox : rolesListBoxTemp) {
                        rolesListBox.setItems(roles.toArray(new SelectItem[]{}));
                    }
                }
                reinit();
            }
        });
    }

    private void reinit() {
        allInOneService.getApprovers(type, null, false, null, true, new AsyncCallback<ApprovalListResult>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ApprovalListResult result) {
                setOnboradingStatues(result.getList());
                redrawApprovers(result.getList());
                hierarchicalApproval.setValue(result.isHierarchicalApproval());
            }
        });
    }

    private void setOnboradingStatues(ArrayList<ApproverItem> result) {
        if (isOnboarding && result.size() > 0) {
            approveStatus.setSelected(result.get(0).getAppproveStatusId());
            rejectStatus.setSelected(result.get(0).getRejectStatusId());
            startStatus.setSelected(result.get(0).getStartStatusId());
        }
    }

    private Label getCustomizedLabel(String text) {
        Label label = new Label(text);
        label.addStyleName("bold-text-label");
        return label;
    }

    private void setupTable() {
        table = new FlexTable();
        table.getElement().addClassName("td-padding-4 approvers-table__data");
        table.setWidth("100%");
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setWidget(0, 0, getCustomizedLabel(wfmStrings.step()));/*Step*/
        table.setWidget(0, 1, getCustomizedLabel(wfmStrings.approvers()));
        table.setWidget(0, 2, getCustomizedLabel(wfmStrings.condition()));
        table.setWidget(0, 3, getCustomizedLabel(wfmStrings.action()));
        table.setText(0, 4, "");
        table.setText(0, 5, "");
        table.getFlexCellFormatter().addStyleName(0, 0, "thickBorderBottom");
        table.getFlexCellFormatter().addStyleName(0, 1, "thickBorderBottom");
        table.getFlexCellFormatter().addStyleName(0, 2, "thickBorderBottom");
        table.getFlexCellFormatter().addStyleName(0, 3, "thickBorderBottom");
        table.getFlexCellFormatter().addStyleName(0, 4, "thickBorderBottom");
        table.getFlexCellFormatter().addStyleName(0, 5, "thickBorderBottom");

        // Create a temp blank row:
        addNewRow(new ApproverItem(), 1);
        adder = new MaterialLink();
        adder.setStyleName("approvers-table__new-line");
        adder.addClickHandler(clickEvent -> addNewRow(new ApproverItem(), table.getRowCount()));
        Icon adderIcon = new Icon();
        adderIcon.setStyleName("ficon--plus");
        adder.add(adderIcon);
        adder.setText(settingsStrings.addNewStep());
//        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
//        saveButton.addStyleName("approvers-table__save-button");
//        saveButton.addClickHandler(clickEvent -> save());
    }

    public void removeRow(Integer index) {
        if (table.getRowCount() > 1) {
            table.removeRow(index);
            if (approverTable.get(index - 1) != null) {
               allInOneService.addDeletedApproverProcessHistory(type, approverTable.get(index - 1).getObjectID() , new AbstractAsyncCallback<Boolean>() {
                   @Override
                   public void failure(Throwable throwable) {
                       Info.warn(wfmStrings.errorOccurredSavingChanges());
                   }

                   @Override
                   public void success(Boolean result) {
                       if (result) {
                           Info.show(wfmStrings.messSuccessfulyyDeleted());
                       }
                   }});
                approverTable.remove(index - 1);

            }

        }
    }


    private void addNewRow(final ApproverItem item, final Integer index) {
        approverTable.add(item);
        final MultiTable roles_or_employees = new MultiTable(new MultiTableWidgets() {
            public WidgetsMap getWidgetsMaps() {
                return getRolesOrEmployeesWidgets(null, "ROLE", false);
            }

            public boolean isFilled() {
                return false;
            }
        });
        roles_or_employees.setStyleName("approvers-table__roles-or-employees");
        roles_or_employees.getLinksPanel().setStyleName("approvers-table__add-role-or-employees");

        VerticalPanel fourthColumnWidget = new VerticalPanel();
        fourthColumnWidget.setStyleName("approvers-table__approved-or-rejected");
        Label onapproval = new Label(settingsStrings.ifApproved());
        Label onrejection = new Label(settingsStrings.ifRejected());

        onapproval.addStyleName("onboardingLabel");
        onrejection.addStyleName("onboardingLabel");

        fourthColumnWidget.add(onapproval);
        fourthColumnWidget.add(onrejection);
        fourthColumnWidget.setSpacing(3);

        DataListBox ifApprovedAction = new DataListBox();
//       If approved da hozircha "Send to Next Approver", "Mark as approved" quyib quyuring
        List<SelectItem> ifApprovedActions = new ArrayList<>();
        ifApprovedActions.add(new SelectItem(ApproverItem.SEND_TO_NEXT_APPROVER, wfmStrings.sendToNextApprover()));
        ifApprovedActions.add(new SelectItem(ApproverItem.MARK_AS_APPROVED, wfmStrings.markAsApproved()));
        ifApprovedAction.setItems(ifApprovedActions.toArray(new SelectItem[]{}));
//        If rejected da hozircha "Send to previous approver", Send to creator, send to director quyib quyuring
        List<SelectItem> ifRejectedActions = new ArrayList<>();
        DataListBox ifRejectedAction = new DataListBox();
        //We have only two options for rejectaction of LeaveRequests
        if (RelationItem.TYPE_SICK_REQUEST.equalsIgnoreCase(onboardingtype)) {
            if (index > 1) {
                ifRejectedActions.add(new SelectItem(ApproverItem.SEND_TO_PREV_APPROVER, wfmStrings.sendToPreviousApprover()));
            }
            ifRejectedActions.add(new SelectItem(ApproverItem.MARK_AS_REJECTED, wfmStrings.markAsRejected()));
        } else {
            ifRejectedActions.add(new SelectItem(ApproverItem.SEND_TO_PREV_APPROVER, wfmStrings.sendToPreviousApprover()));
            ifRejectedActions.add(new SelectItem(ApproverItem.SEND_TO_CREATOR, wfmStrings.sendToCreator()));
            ifRejectedActions.add(new SelectItem(ApproverItem.SEND_TO_DIRECTORS, wfmStrings.sendToDirectors()));
            ifRejectedActions.add(new SelectItem(ApproverItem.MARK_AS_REJECTED, wfmStrings.markAsRejected()));
        }
        ifRejectedAction.setItems(ifRejectedActions.toArray(new SelectItem[]{}));

        VerticalPanel fivesColumnWidget = new VerticalPanel();
        fivesColumnWidget.setSpacing(3);
        fivesColumnWidget.add(ifApprovedAction);
        fivesColumnWidget.add(ifRejectedAction);
        fivesColumnWidget.setStyleName("approvers-table__action");

        MaterialPanel removePanel = new MaterialPanel();
        MaterialLink removeLink = new MaterialLink();
        removeLink.addClickHandler(clickEvent -> removeRow(table.getCellForEvent(clickEvent).getRowIndex()));
        Icon removeIcon = new Icon();
        removeIcon.setStyleName("ficon--trash approvers-table__remove-link");
        removeLink.add(removeIcon);
        removeLink.setText(wfmStrings.delete());
        removePanel.add(removeLink);

        table.setText(index, 0, index.toString());
        table.setWidget(index, 1, roles_or_employees);
        table.setWidget(index, 2, fourthColumnWidget);
        table.setWidget(index, 3, fivesColumnWidget);
        table.setWidget(index, 4, getWorkflowActions(item));
        table.setWidget(index, 5, removePanel);

        if (item != null && item.getObjectID() != null) {
            ifApprovedAction.setSelected(item.getOnApprovedAction());
            ifRejectedAction.setSelected(item.getOnRejectedAction());
            if ((item.getRoles() != null && item.getRoles().size() > 0) || (item.getEmployees() != null && item.getEmployees().size() > 0) ||
                    (item.getDynamicQueries() != null && item.getDynamicQueries().size() > 0)) {
                roles_or_employees.removeFirstRow();
                if (item.getRoles() != null && item.getRoles().size() > 0) {
                    for (SelectItem role : item.getRoles()) {
                        roles_or_employees.addWidgets(getRolesOrEmployeesWidgets(role, "ROLE", item.isLocation()));
                    }
                }
                if (item.getEmployees() != null && item.getEmployees().size() > 0) {
                    for (SelectItem employee : item.getEmployees()) {
                        roles_or_employees.addWidgets(getRolesOrEmployeesWidgets(employee, "EMPLOYEE", item.isLocation()));
                    }
                }
                if (item.getDynamicQueries() != null && item.getDynamicQueries().size() > 0) {
                    for (SelectItem query : item.getDynamicQueries()) {
                        roles_or_employees.addWidgets(getRolesOrEmployeesWidgets(query, "DYNAMIC", item.isLocation()));
                    }
                }
            }
        }

        table.getCellFormatter().setVerticalAlignment(index, 0, HasVerticalAlignment.ALIGN_MIDDLE);
        table.getCellFormatter().setVerticalAlignment(index, 1, HasVerticalAlignment.ALIGN_TOP);
        table.getCellFormatter().setVerticalAlignment(index, 2, HasVerticalAlignment.ALIGN_TOP);
        table.getCellFormatter().setVerticalAlignment(index, 3, HasVerticalAlignment.ALIGN_TOP);
        table.getCellFormatter().setVerticalAlignment(index, 4, HasVerticalAlignment.ALIGN_TOP);
        table.getCellFormatter().setVerticalAlignment(index, 5, HasVerticalAlignment.ALIGN_MIDDLE);
        table.getCellFormatter().setHorizontalAlignment(index, 0, HasHorizontalAlignment.ALIGN_CENTER);
        table.getCellFormatter().setHorizontalAlignment(index, 1, HasHorizontalAlignment.ALIGN_CENTER);
        table.getCellFormatter().setHorizontalAlignment(index, 2, HasHorizontalAlignment.ALIGN_RIGHT);
        table.getCellFormatter().setHorizontalAlignment(index, 3, HasHorizontalAlignment.ALIGN_LEFT);
        table.getCellFormatter().setHorizontalAlignment(index, 4, HasHorizontalAlignment.ALIGN_LEFT);
        table.getCellFormatter().setHorizontalAlignment(index, 5, HasHorizontalAlignment.ALIGN_RIGHT);
        if (index > 1) {
            table.getCellFormatter().getElement(index, 0).addClassName("topBorder");
            table.getCellFormatter().getElement(index, 1).addClassName("topBorder");
            table.getCellFormatter().getElement(index, 2).addClassName("topBorder");
            table.getCellFormatter().getElement(index, 3).addClassName("topBorder");
            table.getCellFormatter().getElement(index, 4).addClassName("topBorder");
            table.getCellFormatter().getElement(index, 5).addClassName("topBorder");
        }
    }

    private Widget getWorkflowActions(final ApproverItem item) {
        VerticalPanel vp = new VerticalPanel();
        vp.setStyleName("approvers-table__workflow");
        if (item.getOnApproveWorkflowRule() == null || item.getOnApproveWorkflowRule().getObjectID() == null) {
            vp.add(new Label(""));
            vp.add(new Label(""));
            return vp;
        }
        Anchor a = new Anchor(settingsStrings.workflowActions());
        a.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("workflow|summary/" + item.getOnApproveWorkflowRule().getObjectID() + "/" + item.getOnApproveWorkflowRule().getModule() + "/" + item.getOnApproveWorkflowRule().getExecutionCriteria()));
        vp.add(a);
        Anchor r = new Anchor(settingsStrings.workflowActions());
        if (item.getOnRejectWorkflowRule() != null) {
            r.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("workflow|summary/" + item.getOnRejectWorkflowRule().getObjectID() + "/" + item.getOnRejectWorkflowRule().getModule() + "/" + item.getOnRejectWorkflowRule().getExecutionCriteria()));
            vp.add(r);
        }
        return vp;
    }

    private WidgetsMap getRolesOrEmployeesWidgets(SelectItem selected, String type, Boolean isLocation) {
        WidgetsMap widgetsMap = new WidgetsMap();

        final DataListBox rolesListBox = new DataListBox();
        rolesListBox.ensureDebugId("roles");

        if (roles != null && roles.size() > 0) {
            //find all roles list box and add items to them.
            rolesListBox.setItems(roles.toArray(new SelectItem[]{}));
        } else {
            rolesListBoxTemp.add(rolesListBox);
        }

        final EmployeeLookUp employees = new EmployeeLookUp(true, false, false);
        employees.ensureDebugId("employees");

        TextArea2 queryWidget = new TextArea2();
        queryWidget.ensureDebugId("query");

        KpiCheckBox approveForAll = new KpiCheckBox(wfmStrings.approveForAll());
        if (selected != null && selected.isSelected()) {
            approveForAll.setValue(selected.isSelected());
        }

        final FlexTable flexTable = new FlexTable();
        String rnd = com.edatasite.workforce.gwt.core.client.UUID.uuid(5);
        final KpiRadioButton onlyRoles = new KpiRadioButton("roles_" + rnd, wfmStrings.role());
        onlyRoles.setValue(true);
        onlyRoles.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue()) {
                flexTable.setWidget(0, 0, rolesListBox);
            }
        });
        final KpiRadioButton onlyEmployees = new KpiRadioButton("roles_" + rnd, wfmStrings.employee());
        onlyEmployees.addStyleName("ml-2");
        onlyEmployees.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue()) {
                flexTable.setWidget(0, 0, employees);
            }
        });

        final KpiCheckBox location = new KpiCheckBox(wfmStrings.location());
        location.setValue(isLocation);
        location.addStyleName("ml-2");

        KpiRadioButton dynamicQuery = new KpiRadioButton("roles_" + rnd, wfmStrings.dynamicType());
        if (Utils.isSuperUser()) {
            dynamicQuery.setEnabled(true);
        } else {
            dynamicQuery.setEnabled(false);
            new KpiToolTip(dynamicQuery, settingsStrings.pleaseContactForMoreDetails());
        }
        dynamicQuery.setEnabled(Utils.isSuperUser());
        dynamicQuery.addValueChangeHandler(event -> {
            if (event.getValue()) {
                flexTable.setWidget(0, 0, queryWidget);
            }
        });
        widgetsMap.addWidgetToMap(ROLE_LIST_BOX, rolesListBox);
        widgetsMap.addWidgetToMap(EMPLOYEE_LOOKUP, employees);
        widgetsMap.addWidgetToMap(DYNAMIC_QUERY, queryWidget);
        widgetsMap.addWidgetToMap(APPROVE_FOR_ALL, approveForAll);
        widgetsMap.addWidgetToMap(ROLE_RADIO_BUTTON, onlyRoles);
        widgetsMap.addWidgetToMap(EMPLOYEE_RADIO_BUTTON, onlyEmployees);
        widgetsMap.addWidgetToMap(DYNAMIC_RADIO_BUTTON, dynamicQuery);
        widgetsMap.addWidgetToMap(LOCATION_RADIO_BUTTON, location);

        HorizontalPanel hp = new HorizontalPanel();
//        hp.add(new Label(wfmStrings.role()));
        hp.add(onlyRoles);
//        hp.add(new Label(wfmStrings.employee()));
        hp.add(onlyEmployees);
        hp.add(dynamicQuery);
        hp.add(location);

        flexTable.setWidget(0, 0, rolesListBox);
        flexTable.setWidget(0, 1, approveForAll);
        flexTable.setWidget(1, 0, hp);
        flexTable.getFlexCellFormatter().setColSpan(1, 0, 2);

        widgetsMap.addWidgets(flexTable);
        if (selected != null) {
            if (type.equals("ROLE")) {
                onlyRoles.setValue(true, true);
                rolesListBox.setSelected(selected);
            } else if (type.equals("EMPLOYEE")) {
                onlyEmployees.setValue(true, true);
                employees.setSelected(selected);
            } else {
                dynamicQuery.setValue(true, true);
                queryWidget.setText(selected.getName());
            }
        }
        return widgetsMap;
    }

    private void redrawApprovers(ArrayList<ApproverItem> approvers) {
        if (approvers != null && approvers.size() > 0) {
            clearAllRows();
            int index = 1;
            for (ApproverItem item : approvers) {
                addNewRow(item, index++);
            }
        }
    }

    private void clearAllRows() {
        int numRows = table.getRowCount();
        for (int i = 1; i < numRows; ++i) {
            table.removeRow(1);
        }
        approverTable.clear();
    }

    public void save() {
        LoadingPanel.loading(true);
        try {
            ArrayList<ApproverItem> items = prepareItemsToSave();
            ApprovalListResult approvalListResult = new ApprovalListResult(items);
            if (hierarchicalApproval.isVisible()) {
                approvalListResult.setHierarchicalApproval(hierarchicalApproval.getValue());
            }
            allInOneService.saveApprovers(type, onboardingtype, approveStatusID, rejectStatusID, startStatusID, approvalListResult, new AbstractAsyncCallback<Void>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.errorOccurredSavingChanges());
                }

                @Override
                public void success(Void result) {
                    LoadingPanel.loading(false);
                    Info.show(wfmMessages.hasBeenSavedSuccessfully(wfmStrings.approvalProcess()));
                    reinit();
                }
            });
        } catch (ValidationException e) {
            LoadingPanel.loading(false);
            Info.warn(wfmStrings.someErrorsOccurred());
        }
    }

    private ArrayList<ApproverItem> prepareItemsToSave() throws ValidationException {
        clearErrorStyle();
        ArrayList<ApproverItem> items = new ArrayList<>();
        prepareOnboardingStatusToSave();
        for (int row = 1; row < table.getRowCount(); row++) {
            ApproverItem item = approverTable.get(row - 1);
            // get every row
            // ignore first(0) column
            // second column(1) has approvers...
            prepareRoleEmployeeToSave(item, (MultiTable) table.getWidget(row, 1));
            // ignore third(2) column
            // get approved_action, rejectedAction they are on fourth column(3)
            prepareApprovedRejectedActionsToSave(item, (VerticalPanel) table.getWidget(row, 3));
            items.add(item);
        }
        return items;
    }

    private void prepareOnboardingStatusToSave() throws ValidationException {
        if (isOnboarding && table.getRowCount() > 0) {
            if (approveStatus.getSelectedItem() == null || rejectStatus.getSelectedItem() == null || startStatus.getSelectedItem() == null) {
                throw new ValidationException("Approve, Reject and Start statues are required");
            } else if (approveStatus.getSelectedItem().getId().equals(rejectStatus.getSelectedItem().getId())) {
                throw new ValidationException("Approve status and Reject status can not be the same");
            } else if (approveStatus.getSelectedItem().getId().equals(startStatus.getSelectedItem().getId())) {
                throw new ValidationException("Approve status and Start status can not be the same");
            } else if (rejectStatus.getSelectedItem().getId().equals(startStatus.getSelectedItem().getId())) {
                throw new ValidationException("Reject status and Start status can not be the same");
            } else {
                approveStatusID = approveStatus.getSelectedId();
                rejectStatusID = rejectStatus.getSelectedId();
                startStatusID = startStatus.getSelectedId();
            }
        }
    }

    private void prepareRoleEmployeeToSave(ApproverItem item, MultiTable widget) throws ValidationException {
        item.getRoles().clear();
        item.getEmployees().clear();
        item.getDynamicQueries().clear();
        for (HashMap<String, Widget> roleOrEmployee : widget.getWidgets()) {
            // we are in every row of roleOrEmployee multiTableWidget
            //ROLE_LIST_BOX, EMPLOYEE_LOOKUP, ROLE_RADIO_BUTTON, EMPLOYEE_RADIO_BUTTON
            boolean approveForAll = false;
            boolean isLocation;
            if (roleOrEmployee.get(APPROVE_FOR_ALL) != null) {
                approveForAll = ((KpiCheckBox) roleOrEmployee.get(APPROVE_FOR_ALL)).getValue();
            }
            if (roleOrEmployee.get(LOCATION_RADIO_BUTTON) != null) {
                isLocation = ((KpiCheckBox) roleOrEmployee.get(LOCATION_RADIO_BUTTON)).getValue();
                item.setLocation(isLocation);
            }
            if (((KpiRadioButton) roleOrEmployee.get(ROLE_RADIO_BUTTON)).getValue() && ((DataListBox) roleOrEmployee.get(ROLE_LIST_BOX)).getSelectedId() != null) {
                SelectItem roleItem = ((DataListBox) roleOrEmployee.get(ROLE_LIST_BOX)).getSelectedItem();
                roleItem.setSelected(approveForAll);
                item.getRoles().add(roleItem);
            }
            if (((KpiRadioButton) roleOrEmployee.get(EMPLOYEE_RADIO_BUTTON)).getValue() && ((EmployeeLookUp) roleOrEmployee.get(EMPLOYEE_LOOKUP)).getSelectedItemID() != null) {
                SelectItem employeeItem = ((EmployeeLookUp) roleOrEmployee.get(EMPLOYEE_LOOKUP)).getSelectedItem();
                employeeItem.setSelected(approveForAll);
                item.getEmployees().add(employeeItem);
            }
            if (((KpiRadioButton) roleOrEmployee.get(DYNAMIC_RADIO_BUTTON)).getValue() && ((TextArea2) roleOrEmployee.get(DYNAMIC_QUERY)).getText() != null) {
                SelectItem dynamicItem = new SelectItem(null, ((TextArea2) roleOrEmployee.get(DYNAMIC_QUERY)).getText());
                dynamicItem.setSelected(approveForAll);
                item.getDynamicQueries().add(dynamicItem);
            }
        }
        if (item.getRoles().size() == 0 && item.getEmployees().size() == 0 && item.getDynamicQueries().size() == 0) {
            throw new ValidationException("approver is required");
        }
    }

    private void prepareApprovedRejectedActionsToSave(ApproverItem item, VerticalPanel widget) throws ValidationException {
        DataListBox ifApprovedAction = (DataListBox) widget.getWidget(0);
        if (ifApprovedAction.getSelectedId() != null) {
            item.setOnApprovedAction(ifApprovedAction.getSelectedId());
        }
        DataListBox ifRejectedAction = (DataListBox) widget.getWidget(1);
        if (ifRejectedAction.getSelectedId() != null) {
            item.setOnRejectedAction(ifRejectedAction.getSelectedId());
        }
        if (item.getOnApprovedAction() == null || item.getOnRejectedAction() == null) {
            throw new ValidationException("approved/rejected action is required");
        }
    }

    private void clearErrorStyle() {
    }

}
