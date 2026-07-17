package com.edatasite.workforce.gwt.messagecenter.client.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.factory.PermissionDenyContainerFactory;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.EmailFolder;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.messagecenter.client.factory.MessageCenterSinksContainerFactory;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 07.03.12
 * Time: 16:32
 * To change this template use File | Settings | File Templates.
 */

public class MessageCenter extends WorkforceEntryPoint {
    public static EmailAccountItem emailAccount;
    public static Map<EmailAccountItem, HashSet<EmailFolder>> emailAccounts = new HashMap<>();

    public interface MessageCenterResources extends ClientBundle {
        @CssResource.NotStrict
        @Source("com/edatasite/workforce/gwt/messagecenter/client/css/MessageCenter.css")
        CssResource messageCenter();
    }

    public static MessageCenterResources resources = GWT.create(MessageCenterResources.class);

    @Override
    protected void loadEmailFolders() {
        MessageCenterService.App.get().getUserFetchableEmailFolders(new AbstractAsyncCallback<HashMap<EmailAccountItem, HashSet<EmailFolder>>>() {
            @Override
            public void onFailure(Throwable caught) {
                initDefaultUserSettings();
            }

            @Override
            public void onSuccess(HashMap<EmailAccountItem, HashSet<EmailFolder>> result) {

                emailAccounts = result;
                for (EmailAccountItem key : result.keySet()) {
                    if (key.isDefaultEmail()) {
                        emailAccount = key;
                    }
                }
                /*
                for (EmailAccountItem key : result.keySet()) {
                    emailAccount = key;
                    emailFolders.clear();
                    emailFolders.addAll(result.get(key));
                }*/
                initDefaultUserSettings();
            }
        });
    }

    public void initSinksContainerFactory() {
            if (Utils.hasPermission(PermissionConstants.CRM_MESSAGE_CENTER)) {
                containerFactory = new MessageCenterSinksContainerFactory(this);
                resources.messageCenter().ensureInjected();
            } else {
                containerFactory = new PermissionDenyContainerFactory(this);
                String section = Utils.getFirstAvailableSectionName();
                if (section != null) {
                    Utils.redirect(GWT.getHostPageBaseURL() + section);
                }
            }


    }
}
