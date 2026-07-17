package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart.goal;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class GoalActionMenu {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    public static FlowPanel createActionButton(ActionHandler handler) {
        FlowPanel action = new FlowPanel();
        action.setStyleName("btn-small btn--circle ficon--more-horiz");

        PopupPanel popup = new PopupPanel(true, false);
        popup.setStyleName("emplActionsPopup");
        popup.add(buildMenu(handler, popup));

        action.addDomHandler((ClickHandler) event -> {
            event.preventDefault();
            event.stopPropagation();

            if (popup.isShowing()) {
                popup.hide();
            } else {
                popup.showRelativeTo(action);
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

    private static FlowPanel buildMenu(ActionHandler handler, PopupPanel popup) {
        FlowPanel menu = new FlowPanel();
        menu.addStyleName("emplActionsPopup-list");


        // Edit Goal
        menu.add(menuItem(new SvgIcon(SvgEnum.editPen), wfmStrings.edit(), () -> {
            popup.hide();
            handler.onGoalEdit();
        }, false)); // isDanger = false

        // Delete Goal
        menu.add(menuItem(new SvgIcon(SvgEnum.trash2), wfmStrings.delete(), () -> {
            popup.hide();
            handler.onGoalDelete();
        }, true)); // isDanger = true

        return menu;
    }

    private static Widget menuItem(SvgIcon icon, String text, Runnable action, boolean isDanger) {
        FlowPanel item = new FlowPanel();
        item.getElement().setAttribute("role", "menuitem");
        //item.addStyleName("emplActionsPopup__item");

        FlowPanel btn = new FlowPanel();
        item.addStyleName("emplActionsPopup__item btn");

        if (isDanger) {
            item.addStyleName("emplActionsPopup__item--danger");
        }

        item.add(icon);
        item.add(new Label(text));

        // Кликаем по обертке или кнопке — неважно, вешаем на item для надежности области клика
        item.addDomHandler((ClickHandler) e -> {
            e.preventDefault();
            e.stopPropagation();
            action.run();
        }, ClickEvent.getType());

        // Вкладываем кнопку в айтем
        //item.add(btn);

        return item;
    }

    public interface ActionHandler {
        void onGoalDelete();

        void onGoalEdit();
    }

}
