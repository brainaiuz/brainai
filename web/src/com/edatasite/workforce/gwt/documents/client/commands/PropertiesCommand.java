package com.edatasite.workforce.gwt.documents.client.commands;

import com.edatasite.workforce.gwt.documents.client.FilePropertiesDialog;
import com.edatasite.workforce.gwt.documents.client.FilesPropertiesDialog;
import com.edatasite.workforce.gwt.documents.client.FolderPropertiesDialog;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.PopupPanel;

import java.util.ArrayList;
import java.util.List;

/**
 * The command that displays the appropriate Properties dialog, according to the
 * selected object in the application.
 *
 * @author Sherali
 */
public class PropertiesCommand implements Command {

    private PopupPanel containerPanel;

    private int tabToShow = 0;

    /**
     * @param _containerPanel
     * @param _tab            the tab to switch to
     */
    public PropertiesCommand(PopupPanel _containerPanel, int _tab) {
        containerPanel = _containerPanel;
        tabToShow = _tab;
    }

    public void execute() {
        if (containerPanel != null) {
            containerPanel.hide();
        }
        if (DocumentsView.get().getCurrentSelection() instanceof FolderResource || DocumentsView.get().getCurrentSelection() instanceof FileResource) {
            initialize();
        } else if (DocumentsView.get().getCurrentSelection() instanceof List) {
            List<FileResource> res = (List<FileResource>) DocumentsView.get().getCurrentSelection();

            ArrayList<FileResource> resources = new ArrayList<>();
            for (FileResource f : res) {
                if (f.getPermission() != null && f.getPermission().isModifyACL()) {
                    resources.add(f);
                }
            }
            FilesPropertiesDialog dlg = new FilesPropertiesDialog(resources);
            dlg.selectTab(tabToShow);
            dlg.center();
        }
    }

    private void initialize() {
        displayProperties();
    }

    /**
     * Display the appropriate Properties dialog, according to the selected
     * object in the application.
     */
    void displayProperties() {
        Object selection = DocumentsView.get().getCurrentSelection();
        if (selection instanceof FolderResource || (selection instanceof FileResource && ((FileResource) selection).isFolder()) ) {
            FolderPropertiesDialog dlg = new FolderPropertiesDialog(false);
            dlg.selectTab(tabToShow);
            dlg.center();
        } else if (selection instanceof FileResource) {
            FilePropertiesDialog dlg = new FilePropertiesDialog();
            dlg.selectTab(tabToShow);
            dlg.center();
        }
    }
}
