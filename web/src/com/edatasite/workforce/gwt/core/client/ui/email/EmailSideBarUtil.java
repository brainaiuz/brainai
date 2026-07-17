package com.edatasite.workforce.gwt.core.client.ui.email;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Anchor;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Dilshod Madrahimov on 06.07.15.
 */
public class EmailSideBarUtil {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final DateTimeFormat dateFormat = DateTimeFormat.getFormat("MMM dd, HH:MM");

    private static Integer itemCount = 5;

    public static native void emailAlert(Element elem) /*-{
        $wnd.$(elem).vibrate({
            speed: 30, // The time in ms between each rotation
            duration: 95000, // The whole "animation" duration, you can use "fast" and "slow"
            spread: 3 // The spread of the animation, beware of huge values (parkinson's style)
        });
    }-*/;

    public static ArrayList<MaterialPanel> generateEmailList(ArrayList<Email> items, Command command, Span totalSpan, Span emailGrandTotalCountSpan) {
        ArrayList<MaterialPanel> list = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            itemCount = items.size();
            items.forEach(item -> list.add(generateEmailContent(item, command, totalSpan, emailGrandTotalCountSpan)));
        } else {
            itemCount = 0;
        }
        return list;
    }

    private static MaterialPanel generateEmailContent(Email item, Command closeCommand, Span totalSpan, Span emailGrandTotalCountSpan) {

        MaterialPanel blockEmail = new MaterialPanel();
        blockEmail.addStyleName("alert-block alert-block--link alert-block--projects");

        MaterialPanel blockHeader = new MaterialPanel();
        blockHeader.setClass("alert-block__heading");
        blockEmail.add(blockHeader);

        Span blockCategory = new Span();
        blockCategory.setClass("alert-block__cat");
        blockHeader.add(blockCategory);

        SimpleLink userNameLink = new SimpleLink(item.getFromEmail());
        String userName = userNameLink.getText();
        userName = userName.replace("\"", "");
        String userNameParts[] = userName.split(" ");
        String firstName = userNameParts[0];
        String lastName = userNameParts.length > 1 ? userNameParts[1] : null;
        String initialName = "" + (!Utils.isNullOrEmpty(firstName) ? firstName.substring(0, 1) : "") + (!Utils.isNullOrEmpty(lastName) ? lastName.substring(0, 1) : "");

        Div blockCircle = new Div();
        blockCircle.setStyleName("alert-block__icon avatar");
        Div initialNameDiv = new Div();
        initialNameDiv.setStyleName("avatar__initials");
        initialNameDiv.getElement().setInnerText(initialName);
        blockCircle.add(initialNameDiv);
        blockCategory.add(blockCircle);

        Span categoryText = new Span();
        categoryText.setStyleName("alert-block__cat-text");

        Anchor emailLink = new Anchor();
        emailLink.getElement().setInnerText(userName);
        emailLink.addClickHandler(clickEvent -> {
            makeAlertAsRead(item, blockEmail, totalSpan);
            if (closeCommand != null) {
                closeCommand.execute();
            }
            SinksContainerFactory.entryPoint.onHistoryChanged("email|summary/" + item.getObjectID(), item.getSubject());
        });
        categoryText.add(emailLink);
        blockCategory.add(categoryText);

        Span blockDate = new Span();
        blockDate.setClass("alert-block__date");
        if (item.getReceivedDate() != null) {
            blockDate.setText(dateFormat.format(item.getReceivedDate()));
        }
        blockHeader.add(blockDate);

        MaterialLink closeLink = new MaterialLink();
        closeLink.setClass("alert-block__close btn-small btn--close");
        closeLink.addClickHandler(clickEvent -> {
            makeAlertAsRead(item, blockEmail, totalSpan);
            itemCount--;
            if (emailGrandTotalCountSpan != null && !"0".equals(emailGrandTotalCountSpan.getText())) {
                try {
                    //emailGrandTotalCountSpan.setText(String.valueOf(Integer.valueOf(emailGrandTotalCountSpan.getText()) - 1));
                    setEmailCount(Long.valueOf(Integer.valueOf(emailGrandTotalCountSpan.getText()) - 1), emailGrandTotalCountSpan);
                } catch (NumberFormatException e) {
                }
            }
            if (itemCount == 0) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_CHANGE_ENTITY, null, null);
            }
        });

        SvgIcon closeIcon = new SvgIcon(SvgEnum.xBold);
        Span closeText = new Span(wfmStrings.close());
        closeLink.add(closeIcon);
        closeLink.add(closeText);
        blockHeader.add(closeLink);

        MaterialPanel blockBody = new MaterialPanel();
        blockBody.setClass("alert-block__body");
        blockEmail.add(blockBody);

        Div blockTitle = new Div();
        blockTitle.setStyleName("alert-block__title");
        blockTitle.getElement().setInnerText(item.getSubject());
        blockTitle.addClickHandler(clickEvent -> {
            makeAlertAsRead(item, blockEmail, totalSpan);
            if (closeCommand != null) {
                closeCommand.execute();
            }
            SinksContainerFactory.entryPoint.onHistoryChanged("email|summary/" + item.getObjectID(), item.getSubject());
        });
        blockBody.add(blockTitle);

       /* Anchor blockNote = new Anchor();
        blockNote.setStyleName("alert-block__note");
        blockNote.setText(item.getSubject());
        blockNote.addClickHandler(clickEvent -> {
            makeAlertAsRead(item, blockEmail, totalSpan, closeCommand);
            if (closeCommand != null) {
                closeCommand.execute();
            }
            SinksContainerFactory.entryPoint.onHistoryChanged("email|summary/" + item.getObjectID(), item.getSubject());
        });
        blockBody.add(blockNote);*/

        Div blockAuthor = new Div();
        blockAuthor.setClass("alert-block__author");
        blockBody.add(blockAuthor);

        Anchor fromEmailLink = new Anchor();
        fromEmailLink.setClass("alert-block__title");
        fromEmailLink.getElement().setInnerText(getEmail(item.getFromEmail()));
        fromEmailLink.addClickHandler(c -> {
            makeAlertAsRead(item, blockEmail, totalSpan);
            if (closeCommand != null) {
                closeCommand.execute();
            }
            SinksContainerFactory.entryPoint.onHistoryChanged("email|summary/" + item.getObjectID(), item.getSubject());
        });
        blockAuthor.add(fromEmailLink);

        return blockEmail;
    }

    private static void makeAlertAsRead(Email item, MaterialPanel blockEmail, Span totalSpan) {
        blockEmail.addStyleName("ldt-power-off");

        Scheduler.get().scheduleFixedDelay(() -> {
            blockEmail.removeFromParent();
            return false;
        }, 500);

        if (totalSpan != null && !"0".equals(totalSpan.getText())) {
            try {
                totalSpan.setText(String.valueOf(Integer.valueOf(totalSpan.getText()) - 1));
            } catch (NumberFormatException e) {
            }
        }

        MessageCenterService.App.get().setEmailFlags(new ArrayList<>(Collections.singletonList(item.getObjectID())), null, Constants.FLAG_READ, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                super.failure(throwable);
            }

            @Override
            public void success(Void result) {
                super.success(result);
                if (Utils.isMC()) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_LIST_CHANGE, null, null);
                }
            }
        });
    }

    private static String getEmail(String fromEmail) {
        if (Utils.isNullOrEmpty(fromEmail)) {
            return "";
        }
        if (fromEmail.contains("<") && fromEmail.contains(">")) {
            return fromEmail.substring(fromEmail.lastIndexOf("<") + 1, fromEmail.lastIndexOf(">"));
        }
        return fromEmail;
    }

    private static void setEmailCount(Long totalCount, Span emailGrandTotalCountSpan) {
        if (totalCount == null || totalCount == 0L) {
            emailGrandTotalCountSpan.setText("");
            emailGrandTotalCountSpan.setVisible(false);
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
        emailGrandTotalCountSpan.setText(prefix + totalCount + suffix);
        emailGrandTotalCountSpan.setVisible(true);
    }
}
