package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.ui.TreeItem;


/**
 * @author Sherali
 */
public class DeleteUserDialog extends WfmMessageBox {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    /**
     * The widget's constructor.
     */
    public DeleteUserDialog() {
        // Use this opportunity to set the dialog's caption.
        super(IconEnum.QUESTION, Action.YesNo, true);
        setTitle(wfmStrings.delete() + " " + wfmStrings.user());
        final GroupMemberItem group = (GroupMemberItem) DocumentsView.get().getCurrentSelection();

        setMessage(wfmStrings.areSureYouWontToRemoveUser() + " " + group.getTrusteeName() + " ?");
        addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                deleteUser();
            }
        });
    }

    /**
     * Generate an RPC request to delete a group.
     */
    private void deleteUser() {
        final TreeItem user = DocumentsView.get().getGroups().getCurrent();
        final TreeItem group = user.getParentItem();
        if (group == null) {
            DocumentsView.get().displayError(wfmStrings.noUserWasSElected());
            return;
        }

        final GroupMembersViewItem groupResource = (GroupMembersViewItem) group.getUserObject();
        final GroupMemberItem memberR = (GroupMemberItem) user.getUserObject();
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().removeMemberFromGroup(groupResource.getGroupID(), memberR.getTrusteeID(), new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                LoadingPanel.loading(false);
                DocumentsView.get().getGroups().updateGroups();
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (ObjectNotFoundException e) {
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });

    }
}

