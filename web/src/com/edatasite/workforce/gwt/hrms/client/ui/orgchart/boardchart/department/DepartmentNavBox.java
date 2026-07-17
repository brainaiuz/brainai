package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.department;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ChildOrientation;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.ResultTO;
import com.edatasite.workforce.gwt.core.client.services.dto.DepartmentNode;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiViewButton;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.OrgChartColorSchema;
import com.edatasite.workforce.gwt.team.client.services.client.impl.DepartmentRestClient;
import com.google.gwt.dom.client.*;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.html.UnorderedList;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.SETTINGS_CUSTOMIZATION_REFERENCE;

public class DepartmentNavBox extends KpiSideNavBox {

    private final DepartmentRestClient departmentRestClient = new DepartmentRestClient();
    private DepartmentNode selectedNode;
    private com.google.gwt.user.client.Timer hideTimer;
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public DepartmentNavBox(DepartmentNode node, String activeTab) {
        super(true);
        getElement().removeClassName("user-menu-settings");
        getElement().addClassName("side-nav--orgChart");
        getElement().getStyle().setWidth(65, Style.Unit.PCT);

        LoadingPanel.loading(true);
        departmentRestClient.getDepartment(node.getId(), new AsyncCallback<ResultTO<DepartmentNode>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<DepartmentNode> result) {
                LoadingPanel.loading(false);
                selectedNode = result.getData();

                getContentHeader().removeFromParent();
                addHeaderContainer(getHeaderContent(selectedNode));

                DepartmentTabsNav departmentTabsNav = new DepartmentTabsNav(selectedNode);
                departmentTabsNav.activateTab(activeTab);

                addHeaderContainer(departmentTabsNav.getTabsWidget());
                addBody(departmentTabsNav.getPanesWidget());
            }
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SIDE_NAV_CLOSED, DepartmentNavBox.this, (sender, args) -> close());
    }


    private FlowPanel getHeaderContent(DepartmentNode selectedNode) {
        FlowPanel root = new FlowPanel();
        root.setStyleName("depCardFunc-heading");

        FlowPanel headerRow = new FlowPanel();
        headerRow.addStyleName("depCardFunc-title");

        SimplePanel circle = new SimplePanel();
        circle.setStyleName("colorPickItem active");

        String backgroundColor = selectedNode.getColor() != null ? selectedNode.getColor() : "#DDFDD7";
        circle.getElement().getStyle().setBackgroundColor(backgroundColor);

        OrgChartColorSchema color = new OrgChartColorSchema(true);
        color.setSelectHandler((depColor, changeColor) -> {
            circle.getElement().getStyle().setBackgroundColor(depColor);
            selectedNode.setColor(depColor);
            selectedNode.setChangeSubDepColor(changeColor);
            updateDepartment(selectedNode);
        });
        PopupPanel colorPopup = new PopupPanel();
        colorPopup.addStyleName("colorPickItemPopup");
        colorPopup.add(color);

        addPopupHandlers(circle, colorPopup);

        Label departmentName = new Label();
        departmentName.addStyleName("form-control");
        departmentName.addStyleName("depCardFunc-titleControl");
        departmentName.setText(selectedNode.getName());

        if (Utils.hasPermission(SETTINGS_CUSTOMIZATION_REFERENCE)) {
            departmentName.addClickHandler(h -> Utils.openURL("Settings.html#reference|edit/" + "DEPARTMENT_TITLES"));
        }

        headerRow.add(circle);
        headerRow.add(createOrientationPanel());
        headerRow.add(departmentName);

        FlowPanel shortDescriptionRow = new FlowPanel();
        shortDescriptionRow.addStyleName("depCardFunc-descr");
        shortDescriptionRow.addStyleName("depCardFunc-descr--1");

        FlowPanel productPanel = new FlowPanel();
        productPanel.addStyleName("form-control");
        productPanel.addStyleName("depCardFunc-titleControl");

        String productId = Document.get().createUniqueId();
        productPanel.getElement().setId(productId);

        Label shortDescriptionLabel = new Label(hrmsStrings.keyDeliverable());

        shortDescriptionLabel.addStyleName("depCardFunc-descrTitle");
        shortDescriptionLabel.getElement().setAttribute("for", productId);

        updateEmptyState(productPanel, selectedNode.getShortDescription());

        Span productValue = new Span(selectedNode.getShortDescription());
        productValue.setStyleName("depCardFunc-descr2Value");
        productPanel.add(productValue);

        KpiViewButton showAllProduct = new KpiViewButton(hrmsStrings.showMore(), SvgEnum.chevronDown);
        showAllProduct.setTextMinWidthByLongest(hrmsStrings.showMore(), hrmsStrings.showLess());
        showAllProduct.addStyleName("depCardFunc-descr2Btn");

        applyCollapsedStyle(productPanel, showAllProduct);

        showAllProduct.addDomHandler(event -> {
            event.preventDefault();
            event.stopPropagation();
            toggle(showAllProduct, productPanel);
        }, ClickEvent.getType());

        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_DEPARTMENT)) {
            productValue.setCursor(Style.Cursor.POINTER);
            productPanel.addDomHandler(event -> goToDepEditForm(selectedNode.getId(), selectedNode.getNumberData()), ClickEvent.getType());
        }

        shortDescriptionRow.add(shortDescriptionLabel);
        shortDescriptionRow.add(productPanel);
        shortDescriptionRow.add(showAllProduct);

        FlowPanel fullDescriptionRow = new FlowPanel();
        fullDescriptionRow.addStyleName("depCardFunc-descr");
        fullDescriptionRow.addStyleName("depCardFunc-descr--2");

        FlowPanel descriptionPanel = new FlowPanel();
        descriptionPanel.addStyleName("form-control");
        descriptionPanel.addStyleName("depCardFunc-titleControl");

        String worksId = Document.get().createUniqueId();
        descriptionPanel.getElement().setId(worksId);

        Label fullDescriptionLabel = new Label(hrmsStrings.workActiviteis());
        fullDescriptionLabel.addStyleName("depCardFunc-descrTitle");
        fullDescriptionLabel.getElement().setAttribute("for", worksId);

        updateEmptyState(descriptionPanel, selectedNode.getDescription());

        Span description = new Span(selectedNode.getDescription());
        description.setStyleName("depCardFunc-descr2Value");
        descriptionPanel.add(description);

        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_DEPARTMENT)) {
            description.setCursor(Style.Cursor.POINTER);
            descriptionPanel.addDomHandler(event -> goToDepEditForm(selectedNode.getId(), selectedNode.getNumberData()), ClickEvent.getType());
        }

        KpiViewButton showAll = new KpiViewButton(hrmsStrings.showMore(), SvgEnum.chevronDown);
        showAll.setTextMinWidthByLongest(hrmsStrings.showMore(), hrmsStrings.showLess());
        showAll.addStyleName("depCardFunc-descr2Btn");

        applyCollapsedStyle(descriptionPanel, showAll);

        showAll.addDomHandler(event -> {
            event.preventDefault();
            event.stopPropagation();
            toggle(showAll, descriptionPanel);
        }, ClickEvent.getType());

        fullDescriptionRow.add(fullDescriptionLabel);
        fullDescriptionRow.add(descriptionPanel);
        fullDescriptionRow.add(showAll);


        root.add(headerRow);
        root.add(shortDescriptionRow);
        root.add(fullDescriptionRow);

        return root;
    }

    private void goToDepEditForm(Integer id, String numberData) {
        SinksContainerFactory.entryPoint.onHistoryChanged("department|edit/" + id, numberData);
        close();
    }

    private void updateDepartment(DepartmentNode node) {
        LoadingPanel.loading(true);
        departmentRestClient.updateDepartment(node, new AsyncCallback<ResultTO<DepartmentNode>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ResultTO<DepartmentNode> result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_ADDED_TO_DEPARTMENT, result, DepartmentNavBox.this);
            }
        });
    }

    private void addPopupHandlers(final Widget trigger, final PopupPanel popup) {

        // 1. Показываем попап и отменяем таймер закрытия, если он тикал
        trigger.addDomHandler(e -> {
            if (hideTimer != null) hideTimer.cancel();
            popup.showRelativeTo(trigger);
        }, MouseOverEvent.getType());

        // 2. Уходим с триггера — запускаем таймер на 300мс
        trigger.addDomHandler(e -> {
            // Если мышь ушла на сам попап, ничего не делаем
            if (isMovingToPopup(e, popup)) return;

            startHideTimer(popup);
        }, MouseOutEvent.getType());

        // 3. Если мышь зашла на попап — отменяем закрытие
        popup.addDomHandler(e -> {
            if (hideTimer != null) hideTimer.cancel();
        }, MouseOverEvent.getType());

        // 4. Уходим с попапа — запускаем таймер
        popup.addDomHandler(e -> {
            // Если мышь вернулась на триггер, ничего не делаем
            if (isMovingToTrigger(e, trigger)) return;

            startHideTimer(popup);
        }, MouseOutEvent.getType());
    }

    // Вспомогательный метод для запуска таймера
    private void startHideTimer(final PopupPanel popup) {
        if (hideTimer != null) hideTimer.cancel();

        hideTimer = new com.google.gwt.user.client.Timer() {
            @Override
            public void run() {
                popup.hide();
            }
        };
        hideTimer.schedule(300); // 300 миллисекунд задержки
    }

    private boolean isMovingToPopup(MouseOutEvent e, PopupPanel popup) {
        Element related = asElement(e.getRelatedTarget());
        if (related == null) return false;
        return popup.getElement().isOrHasChild(related);
    }

    private boolean isMovingToTrigger(MouseOutEvent e, Widget trigger) {
        Element related = asElement(e.getRelatedTarget());
        if (related == null) return false;
        return trigger.getElement().isOrHasChild(related);
    }

    private Element asElement(EventTarget t) {
        if (t == null) return null;
        if (Node.is(t)) {
            Node n = Node.as(t);
            if (Element.is(n)) return Element.as(n);
        }
        return null;
    }

    private void applyCollapsedStyle(FlowPanel descriptionPanel, KpiViewButton showAll) {
        descriptionPanel.removeStyleName("is-expanded");
        descriptionPanel.addStyleName("is-collapsed");

        // Force CSS for single-line truncation
        descriptionPanel.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        descriptionPanel.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NOWRAP);
        descriptionPanel.getElement().getStyle().setProperty("textOverflow", "ellipsis");

        showAll.removeStyleName("active");
    }

    private void applyExpandedStyle(FlowPanel descriptionPanel, KpiViewButton showAll) {
        descriptionPanel.removeStyleName("is-collapsed");
        descriptionPanel.addStyleName("is-expanded");

        // Force CSS to allow text wrapping
        descriptionPanel.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NORMAL);
        descriptionPanel.getElement().getStyle().setProperty("wordWrap", "break-word");
        descriptionPanel.getElement().getStyle().setOverflow(Style.Overflow.VISIBLE);

        showAll.addStyleName("active");
    }


    private void toggle(KpiViewButton showAll, FlowPanel descriptionPanel) {
        boolean isExpanded = showAll.isActive();
        if (!isExpanded) {
            showAll.setActive(true);
            showAll.setText(hrmsStrings.showLess());
            applyExpandedStyle(descriptionPanel, showAll);
        } else {
            showAll.setActive(false);
            showAll.setText(hrmsStrings.showMore());
            applyCollapsedStyle(descriptionPanel, showAll);
        }
    }


    private native int getScrollHeight(Element e) /*-{
        return e.scrollHeight || 0;
    }-*/;

    private FlowPanel createOrientationPanel() {
        FlowPanel orientationPanel = new FlowPanel();
        orientationPanel.setStyleName("dirModeTabs");

        UnorderedList orientationList = new UnorderedList();
        orientationList.setStyleName("tabs");

        ListItem vertical = new ListItem();
        vertical.setStyleName("tab dirModeTabs__tab");

        MaterialLink verticalLink = new MaterialLink();
        verticalLink.add(new SvgIcon(SvgEnum.stackY));

        vertical.add(verticalLink);

        ListItem horizontal = new ListItem();
        horizontal.setStyleName("tab dirModeTabs__tab");

        MaterialLink horizontalLink = new MaterialLink();
        horizontalLink.add(new SvgIcon(SvgEnum.stackX));
        horizontal.add(horizontalLink);

        verticalLink.addClickHandler(e -> {
            verticalLink.setStyleName("active");
            horizontalLink.removeStyleName("active");
            selectedNode.setChildOrientation(ChildOrientation.VERTICAL);
            updateDepartment(selectedNode);
        });

        horizontalLink.addClickHandler(e -> {
            verticalLink.removeStyleName("active");
            horizontalLink.setStyleName("active");
            selectedNode.setChildOrientation(ChildOrientation.HORIZONTAL);
            updateDepartment(selectedNode);
        });

        if (ChildOrientation.VERTICAL.equals(selectedNode.getChildOrientation())) {
            verticalLink.setStyleName("active");
        } else {
            horizontalLink.setStyleName("active");
        }

        ListItem indicator = new ListItem();
        indicator.setStyleName("indicator");

        orientationList.add(vertical);
        orientationList.add(horizontal);
        orientationList.add(indicator);
        orientationPanel.add(orientationList);

        return orientationPanel;
    }

    private void updateEmptyState(Widget widget, String value) {
        boolean isEmpty = (value == null || value.trim().isEmpty());

        if (isEmpty) {
            widget.addStyleName("is-empty");
        } else {
            widget.removeStyleName("is-empty");
        }
    }

}
