package com.edatasite.workforce.gwt.messagecenter.client.container;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.EmailFolder;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.messagecenter.client.view.EmailListView;

import java.util.LinkedList;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 17, 2010
 * Time: 12:37:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class MessageCenterSinksContainer extends SinksContainer {

    private EmailAccountItem account;
    private Set<EmailFolder> folders;

    public MessageCenterSinksContainer(EmailAccountItem account, Set<EmailFolder> folders) {
        super("messagecenter_"+(account!=null ? account.getObjectID() : ""), (account!=null ? account.getEmail() : ""), null, NONE, -1, false);
        this.account = account;
        this.folders = folders;
        renderSinksContainer();
    }

    @Override
    protected void initViews() {
        if (folders != null && !folders.isEmpty()) {
            for (EmailFolder folder : folders) {
                addView(new EmailListView( "messagecenter_" + folder.getName(), folder.getName(), folder.getObjectID(), account));
            }
        } else {
            addView(new EmailListView("messagecenter", wfmStrings.emails(), null, account));
        }
        /*if (MessageCenter.emailFolders != null && MessageCenter.emailFolders.size() > 0) {
            for (EmailFolder folder : MessageCenter.emailFolders) {
                addView(new EmailListView("messagecenter_" + folder.getName(), folder.getName(), folder.getObjectID()));
            }
        } else {
            addView(new EmailListView("messagecenter", wfmStrings.emails(), null));
        }*/
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }
}
