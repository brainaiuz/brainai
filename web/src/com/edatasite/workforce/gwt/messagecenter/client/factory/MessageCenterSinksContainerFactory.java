package com.edatasite.workforce.gwt.messagecenter.client.factory;


import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.EmailFolder;
import com.edatasite.workforce.gwt.core.client.ui.emailAccount.EmailAccountQuickAdd;
import com.edatasite.workforce.gwt.core.client.ui.emailAccount.EmailAccountQuickEdit;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.googlecalendar.client.history.GoogleCalendarHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.container.MessageCenterSinksContainer;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailComposeHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.history.EmailHistoryProcessor;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterServiceAsync;
import com.edatasite.workforce.gwt.messagecenter.client.view.MessageCenter;

import java.util.HashSet;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 07.03.12
 * Time: 15:54
 * To change this template use File | Settings | File Templates.
 */

public class MessageCenterSinksContainerFactory extends SinksContainerFactory {
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    public static final WfmMessages wfmMessages = WfmMessages.App.get();
    public static final MessageCenterServiceAsync service = MessageCenterService.App.get();

    public MessageCenterSinksContainerFactory(WorkforceEntryPoint entryPoint) {
        super(entryPoint);
    }

    public void initDefaultContainers() {

        if (MessageCenter.emailAccounts != null && !MessageCenter.emailAccounts.isEmpty()) {

            //Default Email Account
            for(Map.Entry<EmailAccountItem, HashSet<EmailFolder>> entry : MessageCenter.emailAccounts.entrySet()) {
                if(entry.getKey().isDefaultEmail()) {
                    MessageCenterSinksContainer container = new MessageCenterSinksContainer(entry.getKey(), entry.getValue());
                    container.setPreparedView("messagecenter_" + MCFolderType.INBOX.name());
                    putContainer(container);
                    MainLayout.get().getSideNavBar().addMCContainer(entry.getKey().getObjectID(), container, entry.getKey().isActive(), entry.getKey().getUnreadCount(), clickEvent -> new EmailAccountQuickEdit(entry.getKey().getObjectID()));
                }
            }
            //Not Default Email Accounts
            for(Map.Entry<EmailAccountItem, HashSet<EmailFolder>> entry : MessageCenter.emailAccounts.entrySet()) {
                if(!entry.getKey().isDefaultEmail()) {
                    MessageCenterSinksContainer container = new MessageCenterSinksContainer(entry.getKey(), entry.getValue());
                    container.setPreparedView("messagecenter_" + MCFolderType.INBOX.name());
                    putContainer(container);
                    MainLayout.get().getSideNavBar().addMCContainer(entry.getKey().getObjectID(), container, entry.getKey().isActive(), entry.getKey().getUnreadCount(), clickEvent -> {
                        MessageCenter.emailAccount = entry.getKey();
                        new EmailAccountQuickEdit(entry.getKey().getObjectID());
                    });
                }

            }
        } else {
            MessageCenterSinksContainer container = new MessageCenterSinksContainer(null, null);
            putContainer(container);
            MainLayout.get().getSideNavBar().addMCContainer(0, container, false, 0L, clickEvent -> new EmailAccountQuickAdd());
        }
    }

    public void registerProcessors() {
        registerHistoryProcessor("email", new EmailHistoryProcessor());
        registerHistoryProcessor("emailcompose", new EmailComposeHistoryProcessor());
        registerHistoryProcessor("calendar", new GoogleCalendarHistoryProcessor());
    }

    @Override
    public void registerMenuItems() {
        addNewMenuItem(wfmStrings.newAccount(), clickEvent -> new EmailAccountQuickAdd());
    }
}
