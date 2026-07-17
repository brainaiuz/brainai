package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.employee;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.services.dto.EmployeeItem;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class EmployeeActionMenu {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    public static FlowPanel createActionButton(boolean hasManager, boolean isManager, ActionHandler handler, boolean isVacant, EmployeeItem employee) {
        FlowPanel action = new FlowPanel();
        action.setStyleName("ficon--more-horiz");

        PopupPanel popup = new PopupPanel(true, false);
        popup.setStyleName("emplActionsPopup");
        popup.add(buildMenu(hasManager, isManager, handler, popup, isVacant, employee));

        action.addDomHandler((ClickHandler) event -> {
            event.preventDefault();
            event.stopPropagation();

            if (popup.isShowing()) {
                popup.hide();
            } else {
                popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                    final int textboxAbsolTop = action.getAbsoluteTop();

                    public void setPosition(int offsetWidth, int offsetHeight) {
                        int totalWidth = action.getAbsoluteLeft() + popup.getOffsetWidth();
                        if (offsetHeight + action.getOffsetHeight() < Window.getClientHeight() - textboxAbsolTop) {
                            if (totalWidth > Window.getClientWidth()) {
                                popup.setPopupPosition(action.getAbsoluteLeft() - (totalWidth - Window.getClientWidth()), textboxAbsolTop + action.getOffsetHeight());
                            } else {
                                popup.setPopupPosition((action.getAbsoluteLeft() - popup.getOffsetWidth() + action.getOffsetWidth()), textboxAbsolTop + action.getOffsetHeight());
                            }
                        } else {
                            if (totalWidth > Window.getClientWidth()) {
                                popup.setPopupPosition(action.getAbsoluteLeft() - (totalWidth - Window.getClientWidth()), textboxAbsolTop - offsetHeight);
                            } else {
                                popup.setPopupPosition(action.getAbsoluteLeft(), textboxAbsolTop - offsetHeight);
                            }
                        }
                    }
                });
            }
        }, ClickEvent.getType());

        action.getElement().setTabIndex(0);
        action.addDomHandler((KeyDownHandler) e -> {
            if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER || e.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
                e.preventDefault();
                if (popup.isShowing()) popup.hide();
                else popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
                    final int textboxAbsolTop = action.getAbsoluteTop();

                    public void setPosition(int offsetWidth, int offsetHeight) {
                        int totalWidth = action.getAbsoluteLeft() + popup.getOffsetWidth();
                        if (offsetHeight + action.getOffsetHeight() < Window.getClientHeight() - textboxAbsolTop) {
                            if (totalWidth > Window.getClientWidth()) {
                                popup.setPopupPosition(action.getAbsoluteLeft() - (totalWidth - Window.getClientWidth()), textboxAbsolTop + action.getOffsetHeight());
                            } else {
                                popup.setPopupPosition(action.getAbsoluteLeft(), textboxAbsolTop + action.getOffsetHeight());
                            }
                        } else {
                            if (totalWidth > Window.getClientWidth()) {
                                popup.setPopupPosition(action.getAbsoluteLeft() - (totalWidth - Window.getClientWidth()), textboxAbsolTop - offsetHeight);
                            } else {
                                popup.setPopupPosition(action.getAbsoluteLeft(), textboxAbsolTop - offsetHeight);
                            }
                        }
                    }
                });
            }
        }, KeyDownEvent.getType());

        return action;
    }

    private static FlowPanel buildMenu(boolean hasManager, boolean isManager, ActionHandler handler, PopupPanel popup, boolean isVacant, EmployeeItem employee) {
        FlowPanel menu = new FlowPanel();
        menu.addStyleName("emplActionsPopup-list");

        if (!isVacant) {
            if (hasManager) {
                if (isManager) {
                    menu.add(menuItem(new SvgIcon(SvgEnum.user), hrmsStrings.unassigne(), () -> {
                        popup.hide();
                        handler.onUnAssignManager();
                    }, false));
                }
            } else {
                menu.add(menuItem(new SvgIcon(SvgEnum.userCheck), hrmsStrings.assignManager(), () -> {
                    popup.hide();
                    handler.onAssignManager();
                }, false));
            }
        }

        FlowPanel redirect = new FlowPanel();
        redirect.addStyleName("emplActionsPopup__item btn");
        redirect.add(new SvgIcon(SvgEnum.link));
        redirect.add(new Label(wfmStrings.rotation()));
        redirect.addDomHandler((ClickHandler) e -> {
            e.preventDefault();
            e.stopPropagation();

            if (popup != null) {
                popup.hide();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SIDE_NAV_CLOSED, null, null);
            }

            if (Utils.hasPermission(PermissionConstants.HRMS_ROTATION_ADD)) {
                SinksContainerFactory.entryPoint.onHistoryChanged("rotation|add/employee/" + employee.getId());
            } else {
                Info.show(wfmStrings.youDontHavePermission());
            }
        }, ClickEvent.getType());

        menu.add(redirect);

        return menu;
    }

    private static Widget menuItem(SvgIcon icon, String text, Runnable action, boolean isDanger) {
        FlowPanel item = new FlowPanel();
        item.addStyleName("emplActionsPopup__item btn");
        if (isDanger) {
            item.addStyleName("emplActionsPopup__item--danger");
        }

        item.add(icon);
        item.add(new Label(text));

        item.addDomHandler((ClickHandler) e -> {
            e.preventDefault();
            e.stopPropagation();
            action.run();
        }, ClickEvent.getType());

        return item;
    }

    public interface ActionHandler {
        void onAssignManager();

        void onUnAssignManager();
    }
}
