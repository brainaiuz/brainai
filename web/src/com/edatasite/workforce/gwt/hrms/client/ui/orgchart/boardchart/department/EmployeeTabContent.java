package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.services.dto.EmployeeItem;
import com.edatasite.workforce.gwt.core.client.services.lookup.employee.EmployeeRestClient;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.GeneralEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.PositionLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.employee.EmployeeActionMenu;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.employee.EmployeePopup;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ERROR_FORM_STYLE;

public class EmployeeTabContent extends MaterialColumn {

    private final EmployeeRestClient employeeRestClient = new EmployeeRestClient();

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private final DepartmentNode department;

    private GeneralEmployeeLookUp employeeLookUp;
    private GeneralEmployeeLookUp vacantLookUp;

    private FlowPanel employeeList;
    private FlowPanel employeeHolder;

    private FlowPanel vacantList;
    private FlowPanel vacantHolder;

    private FlowPanel vacantBlock;
    private WfmButton2 addEmployeeBtn;
    private WfmButton2 addVacantBtn;

    private Map<Integer, EmployeeItem> departmentEmployees = new HashMap<>();
    private Map<Integer, EmployeeItem> departmentVacants = new HashMap<>();

    public EmployeeTabContent(DepartmentNode department) {
        this.department = department;
        add(buildRoot());
    }

    private FlowPanel buildRoot() {
        FlowPanel content = new FlowPanel();
        content.addStyleName("grid-row");

        FlowPanel positionHolder = buildPositionHolder();
        FlowPanel vacantHolder = buildVacantHolder();

        FlowPanel col1 = new FlowPanel();
        col1.addStyleName("col");
        FlowPanel col2 = new FlowPanel();
        col2.addStyleName("col");

        col1.add(positionHolder);
        col2.add(vacantHolder);
        content.add(col1);
        content.add(col2);

        getDepartmentEmployees();
        getDepartmentVacants();
        removeEmployeeListIfEmpty();
        return content;
    }

    private FlowPanel buildPositionHolder() {
        FlowPanel holder = card(); // .panel

        FlowPanel heading = new FlowPanel();
        heading.addStyleName("panel__heading");
        // info: bular hozir kerak emas
//        heading.add(titleLabel(wfmStrings.position()));
        heading.add(titleLabel(wfmStrings.employees()));

//        FlowPanel body = new FlowPanel();
//        body.addStyleName("panel__body");

//        PositionLookUp positionLookUp = createPositionLookUp();
//        body.add(positionLookUp);

//        addEmployeeBtn = createFullWidthAddButton();
//        body.add(addEmployeeBtn);

//        PopupPanel lookupPopup = createEmployeeLookupPopup(positionLookUp);
//        addEmployeeBtn.addClickHandler(click -> {
//            lookupPopup.getElement().getStyle().setWidth(addEmployeeBtn.getOffsetWidth(), Style.Unit.PX);
//            lookupPopup.showRelativeTo(addEmployeeBtn);
//        });

        holder.add(heading);
//        holder.add(body);
        employeeHolder = holder;
        employeeList = null;

        return holder;
    }

    private FlowPanel ensureEmployeeList(FlowPanel holder) {
        if (employeeList == null) {
            employeeList = new FlowPanel();
            employeeList.addStyleName("depEmployeeList");
            holder.add(employeeList);
        }
        return employeeList;
    }

    private void removeEmployeeListIfEmpty() {
        if (employeeList != null && employeeList.getWidgetCount() == 0) {
            employeeList.removeFromParent();
            employeeList = null;
        }
    }

    private FlowPanel ensureVacantList(FlowPanel holder) {
        if (vacantList == null) {
            vacantList = new FlowPanel();
            vacantList.addStyleName("depEmployeeList");
            holder.add(vacantList);
        }
        return vacantList;
    }

    private void removeVacantListIfEmpty() {
        if (vacantList != null && vacantList.getWidgetCount() == 0) {
            vacantList.removeFromParent();
            vacantList = null;
        }
    }


    private PositionLookUp createPositionLookUp() {
        PositionLookUp lookUp = new PositionLookUp(department.getId());
        lookUp.addStyleName("form-control");
        lookUp.getSuggestBox().addSelectionHandler(h -> {
            if (lookUp.getSelectedItem() == null) {
                this.employeeLookUp.clear();
                this.employeeLookUp.setEnabled(false);
                this.employeeLookUp.setPositionId(null);
            } else {
                this.employeeLookUp.setEnabled(true);
                this.employeeLookUp.setPositionId(lookUp.getSelectedItemID());
            }
        });
        lookUp.getSuggestBox().addValueChangeHandler(h -> {
            if (lookUp.getSelectedItem() == null) {
                this.employeeLookUp.clear();
                this.employeeLookUp.setEnabled(false);
                this.employeeLookUp.setPositionId(null);
            } else {
                this.employeeLookUp.setEnabled(true);
                this.employeeLookUp.setPositionId(lookUp.getSelectedItemID());
            }
        });
        return lookUp;
    }

    private WfmButton2 buildEmployeeBlock(PositionLookUp positionLookUp) {
        addEmployeeBtn = createFullWidthAddButton();

        PopupPanel lookupPopup = createEmployeeLookupPopup(positionLookUp);

        addEmployeeBtn.addClickHandler(click -> {
            lookupPopup.getElement().getStyle().setWidth(addEmployeeBtn.getOffsetWidth(), Style.Unit.PX);
            lookupPopup.showRelativeTo(addEmployeeBtn);
        });

        return addEmployeeBtn;
    }

    private WfmButton2 buildVacantBlock(GeneralEmployeeLookUp lookUp) {
        addVacantBtn = createFullWidthAddButton();

        addVacantBtn.addClickHandler(click -> {
            if (!Validation.validateLookUpRequired(lookUp)) {
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                return;
            }
            EmployeeItem employee = (EmployeeItem) lookUp.getSelectedItem();
            if (departmentVacants.containsKey(employee.getId())) {
                lookUp.getSuggestBox().addStyleName(ERROR_FORM_STYLE);
                lookUp.getSuggestBox().addSelectionHandler(event -> removeWidgetStyle(lookUp.getSuggestBox()));
                Info.show(lookUp.getSelectedItem().getName() + " " + wfmStrings.isAlreadySelected(), Info.Type.WARNING);
                return;
            }
            LoadingPanel.loading(true);
            employeeRestClient.addVacantToDepartment(employee.getId(), department.getId(), new AsyncCallback<ResultTO<List<EmployeeItem>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ResultTO<List<EmployeeItem>> result) {
                    LoadingPanel.loading(false);
                    insertEmployeeRow(true, employee);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADDED_TO_DEPARTMENT, result, EmployeeTabContent.this);
                }
            });
        });
        return addVacantBtn;
    }

    private PopupPanel createEmployeeLookupPopup(PositionLookUp positionLookUp) {
        PopupPanel popup = new PopupPanel(true);

        employeeLookUp = new GeneralEmployeeLookUp(null, positionLookUp.getSelectedItemID());
        employeeLookUp.setEnabled(false);
        employeeLookUp.getSuggestBox().addSelectionHandler(h -> {
            EmployeeItem employee = (EmployeeItem) employeeLookUp.getSelectedItem();
            if (departmentEmployees.containsKey(employee.getId())) {
                employeeLookUp.getSuggestBox().addStyleName(ERROR_FORM_STYLE);
                employeeLookUp.getSuggestBox().addSelectionHandler(event -> removeWidgetStyle(employeeLookUp.getSuggestBox()));
                Info.show(employeeLookUp.getSelectedItem().getName() + " " + wfmStrings.isAlreadySelected(), Info.Type.WARNING);
                return;
            }
            LoadingPanel.loading(true);
            employeeRestClient.addEmployeeToDepartment(employee.getId(), department.getId(), new AsyncCallback<ResultTO<List<EmployeeItem>>>() {

                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ResultTO<List<EmployeeItem>> result) {
                    LoadingPanel.loading(false);
                    insertEmployeeRow(false, employee);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADDED_TO_DEPARTMENT, result, EmployeeTabContent.this);
                }
            });
            popup.hide();
        });

        popup.add(employeeLookUp);
        return popup;
    }

    private void removeWidgetStyle(Widget widget) {
        if (!Utils.isNullOrEmpty(widget.getStyleName())) {
            widget.removeStyleName(ERROR_FORM_STYLE);
        }
    }

    private void insertEmployeeRow(boolean isVacant, EmployeeItem employee) {
        if (isVacant) {
            FlowPanel list = ensureVacantList(vacantHolder);
            list.add(buildEmployeeRow(employee, true));
            departmentVacants.put(employee.getId(), employee);
        } else {
            FlowPanel list = ensureEmployeeList(employeeHolder);
            list.add(buildEmployeeRow(employee, false));
            departmentEmployees.put(employee.getId(), employee);
        }
    }


    private void getDepartmentEmployees() {
        LoadingPanel.loading(true);
        employeeRestClient.getEmployeeListByDepartment(department.getId(), new AsyncCallback<ResultTO<List<EmployeeItem>>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<List<EmployeeItem>> result) {
                LoadingPanel.loading(false);
                if (employeeList != null) employeeList.clear();
                removeEmployeeListIfEmpty();
                List<EmployeeItem> empList = result.getData();
                if (!empList.isEmpty()) {
                    empList.forEach(e -> insertEmployeeRow(false, e));
                }
            }
        });
    }

    private void getDepartmentVacants() {
        LoadingPanel.loading(true);
        employeeRestClient.getVacantListByDepartment(department.getId(), new AsyncCallback<ResultTO<List<EmployeeItem>>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<List<EmployeeItem>> result) {
                LoadingPanel.loading(false);
                vacantBlock.clear();
                removeVacantListIfEmpty();

                List<EmployeeItem> empList = result.getData();
                if (!empList.isEmpty()) {
                    empList.forEach(e -> insertEmployeeRow(true, e));
                }
            }
        });
    }

    private FlowPanel buildVacantHolder() {
        FlowPanel holder = card(); // .panel

        FlowPanel heading = new FlowPanel();
        heading.addStyleName("panel__heading");
        heading.add(titleLabel(hrmsStrings.vacant()));

        FlowPanel body = new FlowPanel();
        body.addStyleName("panel__body");

        vacantLookUp = new GeneralEmployeeLookUp(null, null);
        vacantLookUp.addStyleName("form-control");
        body.add(vacantLookUp);

        addVacantBtn = createFullWidthAddButton();
        body.add(addVacantBtn);

        addVacantBtn.addClickHandler(click -> {
            if (!Validation.validateLookUpRequired(vacantLookUp)) {
                Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                return;
            }
            EmployeeItem employee = (EmployeeItem) vacantLookUp.getSelectedItem();
            if (departmentVacants.containsKey(employee.getId())) {
                vacantLookUp.getSuggestBox().addStyleName(ERROR_FORM_STYLE);
                vacantLookUp.getSuggestBox().addSelectionHandler(event -> removeWidgetStyle(vacantLookUp.getSuggestBox()));
                Info.show(vacantLookUp.getSelectedItem().getName() + " " + wfmStrings.isAlreadySelected(), Info.Type.WARNING);
                return;
            }

            addVacantBtn.setEnabled(false);
            LoadingPanel.loading(true);

            employeeRestClient.addVacantToDepartment(employee.getId(), department.getId(), new AsyncCallback<ResultTO<List<EmployeeItem>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    addVacantBtn.setEnabled(true);
                }

                @Override
                public void onSuccess(ResultTO<List<EmployeeItem>> result) {
                    LoadingPanel.loading(false);
                    addVacantBtn.setEnabled(true);
                    vacantLookUp.clear();
                    insertEmployeeRow(true, employee);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADDED_TO_DEPARTMENT, result, EmployeeTabContent.this);
                }
            });
        });

        vacantBlock = new FlowPanel();
        vacantBlock.addStyleName("depEmployeeList");

        holder.add(heading);
        holder.add(body);
        vacantHolder = holder; // запомнить куда добавлять список
        employeeList = null;     // пока не создаём

        return holder;
    }

    private FlowPanel buildEmployeeRow(EmployeeItem employee, boolean isVacant) {
        FlowPanel row = new FlowPanel();
        row.setStyleName("depEmployeeRow");

        String avatarUrl = employee.getImageUrl();
        FlowPanel avatar = buildAvatar(employee, avatarUrl);

        FlowPanel info = buildEmployeeInfo(employee);

        row.add(avatar);
        row.add(info);

        if (isVacant) {
            FlowPanel deleteAction = new FlowPanel();
            deleteAction.addStyleName("depEmployeeRow__act is-vacant-delete");

            SvgIcon deleteIcon = new SvgIcon(SvgEnum.trash2);
            deleteIcon.getElement().getStyle().setCursor(Style.Cursor.POINTER);

            deleteIcon.addDomHandler((ClickHandler) event -> {
                removeFromDepartment(employee, true);
            }, ClickEvent.getType());

            deleteAction.add(deleteIcon);
            row.add(deleteAction);
        } else {
            FlowPanel action = EmployeeActionMenu.createActionButton(
                    department.hasManager(),
                    employee.isLeader(),
                    new EmployeeActionMenu.ActionHandler() {
                        @Override
                        public void onAssignManager() {
                            assignManager(employee);
                        }

                        @Override
                        public void onUnAssignManager() {
                            unAssignManager(employee);
                        }
                    },
                    false, employee
            );
            row.add(action);
        }

        Anchor nameLink = (Anchor) info.getWidget(1);
        EmployeePopup popup = new EmployeePopup(employee, avatarUrl);
        addHoverPopupHandlers(nameLink, popup);

        return row;
    }

    private FlowPanel buildAvatar(EmployeeItem employee, String avatarUrl) {
        FlowPanel avatar = new FlowPanel();
        avatar.setStyleName("avatar");

        if (avatarUrl == null || avatarUrl.isEmpty()) {
            String[] empNameArr = employee.getName().trim().replace("  ", " ").split(" ");
            StringBuilder name = new StringBuilder();
            if (empNameArr[0] != null && !empNameArr[0].trim().isEmpty()) {
                name.append(empNameArr[0].charAt(0));
            }
            if (empNameArr[1] != null && !empNameArr[1].trim().isEmpty()) {
                name.append(empNameArr[1].charAt(0));
            }
            Span span = new Span(name.toString().toUpperCase());
            avatar.add(span);
        } else {
            MaterialImage avatarImage = new MaterialImage();
            avatarImage.setUrl(avatarUrl);
            avatarImage.setStyleName("avatar__img");
            avatar.add(avatarImage);
            avatar.getElement().setAttribute("style", "background-image: url(" + avatarUrl + ")");
        }
        return avatar;
    }

    private FlowPanel buildEmployeeInfo(EmployeeItem employee) {
        FlowPanel info = new FlowPanel();
        info.setStyleName("depEmployeeInf");

        // 1. Создаем панель позиции (она будет под индексом 0)
        FlowPanel positionPanel = new FlowPanel();
        positionPanel.setStyleName("depEmployeeInf__position");

        if (employee.getPosition() == null || employee.getPosition().trim().isEmpty()) {
            positionPanel.addStyleName("notAssigned");
            positionPanel.add(new Label(wfmStrings.position())); // Автоматически даст Position/Должность/Lavozim
        } else {
            positionPanel.add(new Label(employee.getPosition()));
        }

        // УДАЛИЛ блок с positionTextWrapper, так как он дублировал текст и ломал локализацию

        info.add(positionPanel); // Index 0

        // 2. Создаем ссылку с именем (она БУДЕТ под индексом 1, как и ожидает остальной код)
        Anchor nameLink = new Anchor(Optional.ofNullable(employee.getName()).orElse(wfmStrings.employee()));
        nameLink.setHref("javascript:void(0);");
        nameLink.setStyleName("depEmployeeInf__name");

        info.add(nameLink); // Index 1

        // 3. Добавляем лейбл менеджера в самый конец (Index 2)
        if (employee.isLeader()) {
            FlowPanel managerLabel = new FlowPanel();
            managerLabel.setStyleName("depEmployeeInf__label depEmployeeInf__label-manager");
            managerLabel.getElement().setInnerText(wfmStrings.manager());
            info.add(managerLabel); // Index 2
        }

        return info;
    }

    private FlowPanel card() {
        FlowPanel holder = new FlowPanel();
        holder.setStyleName("depTabs-panel panel panel--md");
        return holder;
    }

    private Widget titleLabel(String text) {
        Label title = new Label(text);
        title.addStyleName("panel__title");
        return title;
    }

    private WfmButton2 createFullWidthAddButton() {
        WfmButton2 button = new WfmButton2(wfmStrings.addEmployee(), WfmButton2.BTN_PRIMARY);
        button.insert(new SvgIcon(SvgEnum.plusCircle), 0);
        button.addStyleName("btn-block");

        button.getElement().getStyle().clearWidth();
        button.getElement().getStyle().clearMarginTop();
        button.getElement().getStyle().clearMarginBottom();

        return button;
    }


    private void addHoverPopupHandlers(final Widget trigger, final PopupPanel popup) {
        trigger.addDomHandler(e -> popup.showRelativeTo(trigger), MouseOverEvent.getType());

        trigger.addDomHandler(e -> {
            if (isMovingToPopup(e, popup)) return;
            popup.hide();
        }, MouseOutEvent.getType());

        popup.addDomHandler(e -> {
            // no-op: prevents premature hide when moving from trigger to popup
        }, MouseOverEvent.getType());

        popup.addDomHandler(e -> {
            if (isMovingToTrigger(e, trigger)) return;
            popup.hide();
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
        if (t == null) return null;
        if (Node.is(t)) {
            Node n = Node.as(t);
            if (Element.is(n)) return Element.as(n);
        }
        return null;
    }

    private void assignManager(EmployeeItem employee) {
        LoadingPanel.loading(true);
        employeeRestClient.assignManager(employee.getId(), department.getId(), new AsyncCallback<ResultTO<List<EmployeeItem>>>() {

            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<List<EmployeeItem>> result) {
                LoadingPanel.loading(false);
                department.setManager(true);
                if (employeeList != null) employeeList.clear();
                List<EmployeeItem> empList = result.getData();
                if (!empList.isEmpty()) {
                    empList.forEach(e -> insertEmployeeRow(false, e));
                }
            }
        });
    }

    private void unAssignManager(EmployeeItem employee) {
        LoadingPanel.loading(true);
        employeeRestClient.unAssignManager(employee.getId(), department.getId(), new AsyncCallback<ResultTO<List<EmployeeItem>>>() {

            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<List<EmployeeItem>> result) {
                LoadingPanel.loading(false);
                department.setManager(false);
                if (employeeList != null) employeeList.clear();
                List<EmployeeItem> empList = result.getData();
                if (!empList.isEmpty()) {
                    empList.forEach(e -> insertEmployeeRow(false, e));
                }
            }
        });
    }

    private void removeFromDepartment(EmployeeItem employee, boolean isVacant) {
        LoadingPanel.loading(true);
        employeeRestClient.removeFromDepartment(employee.getId(), department.getId(), isVacant, new AsyncCallback<ResultTO<List<EmployeeItem>>>() {

            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<List<EmployeeItem>> result) {
                LoadingPanel.loading(false);
                if (isVacant) {
                    if (vacantList != null) vacantList.clear();
                    departmentVacants.clear();
                } else {
                    if (employeeList != null) employeeList.clear();
                    departmentEmployees.clear();
                }
                List<EmployeeItem> empList = result.getData();
                if (!empList.isEmpty()) {
                    empList.forEach(e -> insertEmployeeRow(isVacant, e));
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADDED_TO_DEPARTMENT, result, EmployeeTabContent.this);
            }
        });
    }
}