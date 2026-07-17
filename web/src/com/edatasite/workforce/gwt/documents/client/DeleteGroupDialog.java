package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
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
 * The 'delete group' dialog box.
 */
public class DeleteGroupDialog extends WfmMessageBox {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    public DeleteGroupDialog() {
        super(IconEnum.QUESTION, Action.YesNo, true);
        // Use this opportunity to set the dialog's caption.
        setTitle(wfmStrings.delete() + " " + wfmStrings.group());
        final GroupMembersViewItem group = (GroupMembersViewItem) DocumentsView.get().getCurrentSelection();
        setMessage(wfmStrings.areSureYouWontToDelete() + " " + group.getGroupName() + " ?");
        addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                deleteGroup();
            }
        });
    }

    /**
     * Generate an RPC request to delete a group.
     */
    private void deleteGroup() {
        final TreeItem group = DocumentsView.get().getGroups().getCurrent();
        if (group == null) {
            DocumentsView.get().displayError(wfmStrings.noGroupWasSelected());
            return;
        }

        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().deleteGroup(((GroupMembersViewItem) group.getUserObject()).getGroupID(), new AbstractAsyncCallback() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (ObjectNotFoundException e) {
                    DocumentsView.get().displayError(wfmStrings.groupNotFound());
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(e.getMessage());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }

            @Override
            public void success(Object result) {
                LoadingPanel.loading(false);
//                super.success(result);
                DocumentsView.get().getGroups().updateGroups();
                DocumentsView.get().displayInformation(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.group()));
            }
        });

    }
}
