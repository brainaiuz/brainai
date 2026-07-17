package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;


/**
 * Command to empty trash bin.
 *
 * @author Sherali
 */
public class EmptyTrashCommand implements Command {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private PopupPanel containerPanel;

    public EmptyTrashCommand(PopupPanel _containerPanel) {
        containerPanel = _containerPanel;
    }

    public void execute() {
        containerPanel.hide();
        LoadingPanel.loading(true);
        DocumentsView.get().getDocumentsService().emptyTrash(new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                LoadingPanel.loading(false);
                DocumentsView.get().getFolders().selectTrash();
                DocumentsView.get().getFolders().update(DocumentsView.get().getFolders().getTrashItem());
                DocumentsView.get().showFileList(true);
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                try {
                    throw throwable;
                } catch (ObjectNotFoundException e) {
                    DocumentsView.get().displayError("Resource does not exist");
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHavePermission());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }

}
