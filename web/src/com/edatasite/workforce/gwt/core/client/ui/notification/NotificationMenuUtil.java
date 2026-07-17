package com.edatasite.workforce.gwt.core.client.ui.notification;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.MessengerType;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.NotificationMsgService;
import com.edatasite.workforce.gwt.core.client.rpc.notification.NotificationItem;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Image;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

/**
 * Created by dilsh0d on 06.07.15.
 */
public class NotificationMenuUtil {


    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMM dd, HH:mm");

    public static native void notificationAlert(com.google.gwt.dom.client.Element elem) /*-{
        $wnd.$(elem).vibrate({
            speed: 30, // The time in ms between each rotation
            duration: 95000, // The whole "animation" duration, you can use "fast" and "slow"
            spread: 3 // The spread of the animation, beware of huge values (parkinson's style)
        });
    }-*/;

    private static Integer itemCount = 10;

    public static ArrayList<MaterialPanel> generateMenuList(ArrayList<NotificationItem> items, Command command, Span totalSpan, Span notificationGrandTotalCountSpan) {
        ArrayList<MaterialPanel> list = new ArrayList<>();

        if (items != null && !items.isEmpty()) {
            itemCount = items.size();
            items.forEach(item -> list.add(generateNotification(item, command, totalSpan, notificationGrandTotalCountSpan)));
        } else {
            itemCount = 0;
        }
        return list;
    }

    private static MaterialPanel generateNotification(NotificationItem item, Command command, Span totalSpan, Span notificationGrandTotalSpan) {
        MaterialPanel blockNotification = new MaterialPanel();
        blockNotification.addStyleName("alert-block");
        blockNotification.addClickHandler(c -> {
            if (command != null) {
                command.execute();
            }
            Window.open(GWT.getHostPageBaseURL() + item.getActionUrl().substring(1), "_self", "");
            makeAlertAsRead(blockNotification, item.getId());
        });

        MaterialPanel blockHeader = new MaterialPanel();
        blockHeader.setClass("alert-block__heading");
        blockNotification.add(blockHeader);

        Span blockCategory = new Span();
        blockCategory.setClass("alert-block__cat");
        blockHeader.add(blockCategory);

        Div bloclBrandFigure = new Div();
        bloclBrandFigure.setStyleName("alert-block__icon brandFigure");
        blockCategory.add(bloclBrandFigure);

        Span categoryText = new Span();
        categoryText.setStyleName("alert-block__cat-text");
        blockCategory.add(categoryText);

        Span blockDate = new Span();
        blockDate.setClass("alert-block__date");
        if (item.getDate() != null) {
            blockDate.setText(dateFormat.format(item.getDate()));
        }
        blockHeader.add(blockDate);

        MaterialLink closeLink = new MaterialLink();
        closeLink.setStyleName("alert-block__close btn-small btn--close");
        closeLink.addClickHandler(c -> {
            makeAlertAsRead(blockNotification, item.getId());
            itemCount--;
            if (totalSpan != null && !"0".equals(totalSpan.getText())) {
                try {
                    totalSpan.setText(String.valueOf(Integer.valueOf(totalSpan.getText()) - 1));
                } catch (NumberFormatException e) {
                }
            }

            if (notificationGrandTotalSpan != null && !"0".equals(notificationGrandTotalSpan.getText())) {
                try {
                    //notificationCountSpan.setText(String.valueOf(Integer.valueOf(notificationCountSpan.getText()) - 1));
                    setNotificaitonCount(Long.valueOf(Integer.valueOf(notificationGrandTotalSpan.getText()) - 1), notificationGrandTotalSpan);
                } catch (NumberFormatException e) {
                }
            }

            if (itemCount == 0) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTIFICATION_MSG_CHANGE_ENTITY, null, null);
            }
        });

        SvgIcon closeIcon = new SvgIcon(SvgEnum.xBold);
        Span closeText = new Span(wfmStrings.close());
        closeLink.add(closeIcon);
        closeLink.add(closeText);
        blockHeader.add(closeLink);

        if (ModuleEnum.ACCOUNTING.getCode().equals(item.getModuleCode())) {
            blockNotification.addStyleName("alert-block--accounts");
            categoryText.setText(wfmStrings.accounts());
            SvgIcon catIcon = new SvgIcon(SvgEnum.accounting);
            bloclBrandFigure.add(catIcon);
        } else if (ModuleEnum.HRMS.getCode().equals(item.getModuleCode())) {
            blockNotification.addStyleName("alert-block--humans");
            categoryText.setText(wfmStrings.hrms());
            SvgIcon catIcon = new SvgIcon(SvgEnum.user);
            bloclBrandFigure.add(catIcon);
            //TODO
        } else if (ModuleEnum.CRM.getCode().equals(item.getModuleCode())) {
            blockNotification.addStyleName("alert-block--sales");
            categoryText.setText(wfmStrings.crm());
            SvgIcon catIcon = new SvgIcon(SvgEnum.barChart);
            bloclBrandFigure.add(catIcon);
        } else if (ModuleEnum.REPORTING.getCode().equals(item.getModuleCode())) {
            blockNotification.addStyleName("alert-block--reports");
            categoryText.setText(wfmStrings.reports());
            SvgIcon catIcon = new SvgIcon(SvgEnum.flagBold);
            bloclBrandFigure.add(catIcon);
        } else if (ModuleEnum.PM.getCode().equals(item.getModuleCode())) {
            blockNotification.addStyleName("alert-block--projects");
            categoryText.setText(wfmStrings.projects());
            SvgIcon catIcon = new SvgIcon(SvgEnum.projects);
            bloclBrandFigure.add(catIcon);
        } else if (MessengerType.WHATSAPP.getCode().equals(item.getModuleCode())) {
//            blockNotification.addStyleName("alert-block--projects");
//            categoryText.setText(wfmStrings.projects());
//            SvgIcon catIcon = new SvgIcon(SvgEnum.whatsapp);
//            bloclBrandFigure.add(catIcon);
        } else if (ModuleEnum.PAYROLL.getCode().equals(item.getModuleCode())) {
            blockNotification.addStyleName("alert-block--payroll");
            categoryText.setText(wfmStrings.payroll());
            SvgIcon catIcon = new SvgIcon(SvgEnum.folderBold);
            bloclBrandFigure.add(catIcon);
        } else {
            blockNotification.addStyleName("alert-block--humans");
            categoryText.setText(wfmStrings.hrms());
            SvgIcon catIcon = new SvgIcon(SvgEnum.user);
            bloclBrandFigure.add(catIcon);
        }

        MaterialPanel blockBody = new MaterialPanel();
        blockBody.setClass("alert-block__body");
        blockNotification.add(blockBody);

        MaterialLink blockTitle = new MaterialLink();
        blockTitle.setClass("alert-block__note");
        blockTitle.setText(item.getName());
        blockBody.add(blockTitle);

        Div blockAuthor = new Div();
        blockAuthor.setClass("alert-block__author");
        blockBody.add(blockAuthor);

        FigureWidget figureWidget = new FigureWidget();
        figureWidget.setStyleName("figure-h");
        blockAuthor.add(figureWidget);

        if (!Utils.isNullOrEmpty(item.getActorUserImg())) {
            Icon figureIcon = new Icon();
            figureIcon.setStyleName("alert-block__icon figure-icon circle");
            figureWidget.add(figureIcon);

            Image userImage = new Image();
            userImage.removeStyleName("gwt-Image");
            userImage.setUrl(item.getActorUserImg());
            figureIcon.add(userImage);
        }

        FigCaption userName = new FigCaption();
        userName.setText(item.getUserInfo());
        figureWidget.add(userName);

        return blockNotification;
    }


    private static void setNotificaitonCount(Long totalCount, Span notificationGrandTotalCountSpan) {
        if (totalCount == null || totalCount == 0L) {
            notificationGrandTotalCountSpan.setText("");
            notificationGrandTotalCountSpan.setVisible(false);
            return;
        }

        String prefix = "";
        String suffix = "";
        if (totalCount >= 1000L) {
            totalCount /= 1000L;
            prefix = "+";
            suffix = "k";
        } else if (totalCount > 99L) {
            totalCount = 99L;
            prefix = "+";
        }

        totalCount = totalCount > 1000 ? totalCount / 1000L : totalCount > 100 ? 99 : totalCount;
        notificationGrandTotalCountSpan.setText(prefix + totalCount + suffix);
        notificationGrandTotalCountSpan.setVisible(true);
    }

    private static void makeAlertAsRead(MaterialPanel alertBlock, Integer objectId) {
        alertBlock.addStyleName("ldt-power-off");
        Scheduler.get().scheduleFixedDelay(() -> {
            alertBlock.removeFromParent();
            return false;
        }, 500);

        NotificationMsgService.App.get().updateClicked(objectId, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                super.failure(throwable);
            }

            @Override
            public void success(Void result) {
                super.success(result);

                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTIFICATION_MSG_CHANGE_ENTITY, null, null);
                if (Utils.isHRMS()) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTIFICATION_MSG_LIST_UPDATE, null, null);
                }
            }
        });
    }
}
