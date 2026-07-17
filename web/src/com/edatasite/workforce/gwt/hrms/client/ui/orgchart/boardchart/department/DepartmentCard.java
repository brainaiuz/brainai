package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ChildOrientation;
import com.edatasite.workforce.gwt.core.client.enums.DepartmentActionType;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.services.dto.EmployeeItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.dialogs.AddSubDepartment;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.dialogs.HorizontalMovementDialog;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department.dialogs.MoveInHierarchyDialog;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.employee.EmployeePopup;
import com.edatasite.workforce.gwt.team.client.rpc.request.CreateDepartmentReq;
import com.edatasite.workforce.gwt.team.client.services.client.impl.DepartmentRestClient;
import com.edatasite.workforce.gwt.team.client.ui.view.DepartmentRemovePopup;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEPARTMENT_GOAL;

public class DepartmentCard extends Composite {

    interface Binder extends UiBinder<Widget, DepartmentCard> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    DepartmentRestClient departmentRestClient = new DepartmentRestClient();

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    @UiField
    FlowPanel cardPanel;
    @UiField
    FlowPanel childrenPanel;
    @UiField
    HTML departmentName;
    @UiField
    FocusPanel headerActions;
    @UiField
    FlowPanel employeesSection;
    @UiField
    FlowPanel employeesList;
    @UiField
    FlowPanel shortDescriptionSection;
    @UiField
    HTML shortDescription;
    @UiField
    Label shortDescriptionLabel;
    @UiField
    FlowPanel descriptionSection;
    @UiField
    HTML description;
    @UiField
    Label descriptionLabel;
    @UiField
    FlowPanel metricsSection;
    @UiField
    FlowPanel metricsList;
    @UiField
    Label metricsLabel;


    private final List<SelectItem> depSelectItemList = new ArrayList<>();
    private final TreeUpdateHandler treeUpdateHandler;
    private DepartmentNode currentDep;
    private DepartmentNode rootDep;
    private boolean root;

    private DepartmentActionsPopup actionsPopup;

    public DepartmentCard(DepartmentNode rootDep, DepartmentNode current, boolean root, TreeUpdateHandler handler) {
        this.rootDep = rootDep;
        this.currentDep = current;
        this.root = root;
        this.treeUpdateHandler = handler;

        initWidget(binder.createAndBindUi(this));

        if (root) {
            addStyleName("depRoot");
        }
        metricsLabel.setText(hrmsStrings.metrics());
        descriptionLabel.setText(hrmsStrings.workActiviteis());
        shortDescriptionLabel.setText(hrmsStrings.keyDeliverable());

        bindData();

        actionsPopup = new DepartmentActionsPopup(current, (n, action) -> {
            handleAction(action, n);
        });

        headerActions.add(new SvgIcon(SvgEnum.settings));
        headerActions.addClickHandler(event -> {
            event.preventDefault();
            event.stopPropagation();
            toggleActionsPopup();
        });

        renderEmployees();
        renderMetrics();
        renderChildren();
        buildSelectItem(rootDep, depSelectItemList);
        cardPanel.addDomHandler(e -> {
            viewDepartment(currentDep, "EMPLOYEE");
        }, ClickEvent.getType());
        actionsPopup.addCloseHandler(e -> headerActions.removeStyleName("active"));
    }

    private void bindData() {
        if (currentDep.getColor() != null && !currentDep.getColor().isEmpty()) {
            cardPanel.getElement().setAttribute("style", "background-color: " + currentDep.getColor());
        }
        departmentName.setHTML(SafeHtmlUtils.fromString(Optional.ofNullable(currentDep.getName()).orElse("")).asString());

        String shortDesc = Optional.ofNullable(currentDep.getShortDescription()).orElse("");
        shortDescription.setHTML(SafeHtmlUtils.fromString(shortDesc).asString());
        shortDescriptionSection.setVisible(!shortDesc.isEmpty());

        String fullDesc = Optional.ofNullable(currentDep.getDescription()).orElse("");
        description.setHTML(SafeHtmlUtils.fromString(fullDesc).asString());
        descriptionSection.setVisible(!fullDesc.isEmpty());
    }

    private void renderEmployees() {
        employeesList.clear();
        if (currentDep.getEmployees() == null || currentDep.getEmployees().isEmpty()) {
            employeesSection.setVisible(false);
            return;
        }
        employeesSection.setVisible(true);
        for (EmployeeItem emp : currentDep.getEmployees()) {
            employeesList.add(buildEmployeeRow(emp));
        }
    }

    private FlowPanel buildEmployeeRow(EmployeeItem emp) {
        FlowPanel row = new FlowPanel();
        row.setStyleName("depEmployeeRow");

        FlowPanel avatar = new FlowPanel();
        avatar.setStyleName("avatar");
        String avatarUrl = emp.getImageUrl();
        if (avatarUrl == null || avatarUrl.isEmpty()) {
            String[] empNameArr = emp.getName().trim().replace("  ", " ").split(" ");
            StringBuilder name = new StringBuilder();
            if (empNameArr.length > 0 && empNameArr[0] != null && !empNameArr[0].isEmpty())
                name.append(empNameArr[0].charAt(0));
            if (empNameArr.length > 1 && empNameArr[1] != null && !empNameArr[1].isEmpty())
                name.append(empNameArr[1].charAt(0));
            avatar.add(new Span(name.toString().toUpperCase()));
        } else {
            MaterialImage avatarImage = new MaterialImage();
            avatarImage.setUrl(avatarUrl);
            avatarImage.setStyleName("avatar__img");
            avatar.add(avatarImage);
            avatar.getElement().setAttribute("style", "background-image: url(" + avatarUrl + ")");
        }

        FlowPanel info = new FlowPanel();
        info.setStyleName("depEmployeeInf");

        FlowPanel positionPanel = new FlowPanel();
        positionPanel.setStyleName("depEmployeeInf__position");

        // ПРОВЕРКА: Если должность пустая или null
        if (emp.getPosition() == null || emp.getPosition().trim().isEmpty()) {
            // Добавляем класс модификатор
            positionPanel.addStyleName("notAssigned");
            // Устанавливаем дефолтное значение из локализации (Position, Должность или Lavozim)
            positionPanel.add(new Label(wfmStrings.position()));
        } else {
            // Если должность есть — выводим её
            positionPanel.add(new Label(emp.getPosition()));
        }

        if (emp.isLeader()) {
            FlowPanel managerLabel = new FlowPanel();
            managerLabel.setStyleName("depEmployeeInf__label depEmployeeInf__label-manager");
            managerLabel.getElement().setInnerText("Manager");
            positionPanel.add(managerLabel);
        }

        Anchor nameLink = new Anchor(Optional.ofNullable(emp.getName()).orElse(wfmStrings.employee()));
        nameLink.setHref("javascript:void(0);");
        nameLink.setStyleName("depEmployeeInf__name");

        info.add(positionPanel);
        info.add(nameLink);
        row.add(avatar);
        row.add(info);

        EmployeePopup popup = new EmployeePopup(emp, avatarUrl);
        addPopupHandlers(nameLink, popup);
        return row;
    }

    private void addPopupHandlers(final Widget trigger, final PopupPanel popup) {
        trigger.addDomHandler(e -> popup.showRelativeTo(trigger), MouseOverEvent.getType());
        trigger.addDomHandler(e -> {
            if (!isMovingToPopup(e, popup)) popup.hide();
        }, MouseOutEvent.getType());
        popup.addDomHandler(e -> {
        }, MouseOverEvent.getType());
        popup.addDomHandler(e -> {
            if (!isMovingToTrigger(e, trigger)) popup.hide();
        }, MouseOutEvent.getType());
    }

    private boolean isMovingToPopup(MouseOutEvent e, PopupPanel popup) {
        Element related = asElement(e.getRelatedTarget());
        return related != null && popup.getElement().isOrHasChild(related);
    }

    private boolean isMovingToTrigger(MouseOutEvent e, Widget trigger) {
        Element related = asElement(e.getRelatedTarget());
        return related != null && trigger.getElement().isOrHasChild(related);
    }

    private Element asElement(EventTarget t) {
        if (t == null || !Node.is(t)) return null;
        Node n = Node.as(t);
        return Element.is(n) ? Element.as(n) : null;
    }

    private void renderMetrics() {
        metricsList.clear();
        if (currentDep.getMetrics() == null || currentDep.getMetrics().isEmpty()) {
            metricsSection.setVisible(false);
            return;
        }
        metricsSection.setVisible(true);
        for (SelectItem m : currentDep.getMetrics()) {
            Anchor metric = new Anchor("* " + Optional.ofNullable(m.getName()).orElse(""));
            metric.setStyleName("depMetricItem");
            metric.addDomHandler(e -> {
                e.preventDefault();
                e.stopPropagation();
                if (Utils.hasPermission(PermissionConstants.HRMS_DEPARTMENT_GOAL_SUMMARY)) {
                    summaryGoal(m.getId());
                } else {
                    Info.warn(wfmStrings.youDontHavePermission());
                }
            }, ClickEvent.getType());
            metricsList.add(metric);
        }
    }

    private void toggleActionsPopup() {
        if (actionsPopup == null) return;
        if (actionsPopup.isShowing()) {
            actionsPopup.hide();
            headerActions.removeStyleName("active");
        } else {
            headerActions.addStyleName("active");
            actionsPopup.showRelativeTo(headerActions);
        }
    }

    private void handleAction(DepartmentActionType action, DepartmentNode currentNode) {
        switch (action) {
            case MOVE_IN_HIERARCHY:
                onMoveInHierarchy(currentNode);
                break;
            case MOVE_HORIZONTAL:
                onSortHorizontally(currentNode, headerActions);
                break;
            case ADD_SUB_DEPARTMENT:
                onAddSubDepartment(currentNode);
                break;
            case SORT_SUB_DEPARTMENTS:
                onSortSubDepartment(currentNode, headerActions);
                break;
            case OPEN_DEPARTMENT:
                viewDepartment(currentNode, "EMPLOYEE");
                break;
            case DELETE_DEPARTMENT:
                deleteDepartment(currentNode);
                break;
        }
    }

    private void onMoveInHierarchy(DepartmentNode currentNode) {
        MoveInHierarchyDialog shell = new MoveInHierarchyDialog(
                rootDep,
                currentNode,
                (newParentDep, current, inheritColor) -> moveDepartment(current, newParentDep, inheritColor)
        );
        shell.open();
    }

    private void onSortHorizontally(DepartmentNode currentNode, Widget trigger) {
        DepartmentNode parentNode = findParentOf(rootDep, currentNode);
        if (parentNode == null) return;

        HorizontalMovementDialog dialog = new HorizontalMovementDialog(
                parentNode,
                parentNode,
                newOrder -> {
                    parentNode.setChildren(newOrder);
                    saveNewOrder(parentNode);
                },
                HorizontalMovementDialog.HorizontalDialogMode.MOVE_HORIZONTAL,
                currentNode.getId()
        );
        dialog.open();
    }

    private void onSortSubDepartment(DepartmentNode currentNode, Widget trigger) {
        if (!currentNode.hasChildren()) return;
        HorizontalMovementDialog dialog = new HorizontalMovementDialog(
                currentNode, currentNode,
                newOrder -> {
                    currentNode.setChildren(newOrder);
                    saveNewOrder(currentNode);
                },
                HorizontalMovementDialog.HorizontalDialogMode.SORT_SUB_FUNCTIONS,
                null
        );
        dialog.open();
    }

    private void onAddSubDepartment(DepartmentNode currentNode) {
        AddSubDepartment addDepartmentModal = new AddSubDepartment(currentNode, depSelectItemList.toArray(new SelectItem[]{}), req -> saveNewDepartment(req));
        addDepartmentModal.open();
    }

    private void saveNewDepartment(CreateDepartmentReq req) {
        LoadingPanel.loading(true);
        departmentRestClient.createDepartment(req, new AsyncCallback<ResultTO<DepartmentNode>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<DepartmentNode> result) {
                LoadingPanel.loading(false);
                if (treeUpdateHandler != null) treeUpdateHandler.onTreeUpdated(result.getData());
            }
        });
    }

    private void viewDepartment(DepartmentNode current, String activeTab) {
        DepartmentNavBox navBox = new DepartmentNavBox(current, activeTab);
        navBox.open();
    }

    private void summaryGoal(Integer goalId) {
        StringBuilder history = new StringBuilder("goal|summary/");
        history.append(goalId).append("/").append(DEPARTMENT_GOAL);
        SinksContainerFactory.entryPoint.onHistoryChanged(history.toString());
    }

    private void deleteDepartment(DepartmentNode current) {
        if (current.isRoot()) {
            Info.show(Property.get(Constants.DEPARTMENT_LIST, "You cannot delete default %s!", wfmStrings.department()), Info.Type.WARNING);
        } else {
            int empCount = current.getEmployees() != null ? current.getEmployees().size() : 0;
            new DepartmentRemovePopup(current.getId(), empCount, null).selectitionListener(null);
        }
    }

    private void moveDepartment(DepartmentNode current, DepartmentNode newParent, boolean inheritColor) {
        if (current == null || newParent == null || (current.getId() != null && current.getId().equals(newParent.getId())))
            return;

        DepartmentNode oldParent = findParentOf(rootDep, current);
        if (oldParent != null && oldParent.getChildren() != null) {
            oldParent.getChildren().removeIf(child -> child == current || (child.getId() != null && current.getId() != null && child.getId().equals(current.getId())));
        }

        if (newParent.getChildren() == null) newParent.setChildren(new ArrayList<>());
        newParent.getChildren().add(current);
        if (newParent.getId() != null) current.setParentId(newParent.getId());

        LoadingPanel.loading(true);
        departmentRestClient.moveDepartment(current.getId(), newParent.getId(), inheritColor, new AsyncCallback<ResultTO<DepartmentNode>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<DepartmentNode> result) {
                LoadingPanel.loading(false);
                rootDep = result.getData();
                currentDep = result.getData();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ORG_BOARD_SETTINGS_UPDATED, result, DepartmentCard.this);
            }
        });
    }

    private DepartmentNode findParentOf(DepartmentNode root, DepartmentNode target) {
        if (root == null || target == null || root.getChildren() == null) return null;
        for (DepartmentNode child : root.getChildren()) {
            if (child == target || (child.getId() != null && target.getId() != null && child.getId().equals(target.getId())))
                return root;
            DepartmentNode found = findParentOf(child, target);
            if (found != null) return found;
        }
        return null;
    }

    private void saveNewOrder(DepartmentNode parent) {
        LoadingPanel.loading(true);
        departmentRestClient.updateSubDepartmentOrder(parent, new AsyncCallback<ResultTO<DepartmentNode>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<DepartmentNode> result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ORG_BOARD_SETTINGS_UPDATED, result, DepartmentCard.this);
            }
        });
    }

    private void renderChildren() {
        childrenPanel.clear();
        if (!currentDep.hasChildren()) {
            childrenPanel.setVisible(false);
            return;
        }
        childrenPanel.setVisible(true);
        childrenPanel.removeStyleName("children-horizontal");
        childrenPanel.removeStyleName("children-vertical");
        childrenPanel.addStyleName(currentDep.getChildOrientation() == ChildOrientation.HORIZONTAL ? "children-horizontal" : "children-vertical");

        for (DepartmentNode child : currentDep.getChildren()) {
            childrenPanel.add(new DepartmentCard(rootDep, child, false, treeUpdateHandler));
        }
    }

    public void refresh() {
        bindData();
        renderEmployees();
        renderMetrics();
        renderChildren();
    }

    private void buildSelectItem(DepartmentNode node, List<SelectItem> depSelectItemList) {
        depSelectItemList.add(new SelectItem(node.getId(), Optional.ofNullable(node.getName()).orElse("—")));
        if (node.hasChildren()) {
            for (DepartmentNode child : node.getChildren()) buildSelectItem(child, depSelectItemList);
        }
    }

    public interface TreeUpdateHandler {
        void onTreeUpdated(DepartmentNode newRoot);
    }
}