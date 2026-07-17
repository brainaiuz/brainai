package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.AbstractPropertiesDialog;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * User: abror
 * Date: 11/23/15 12:41 PM
 */
public class EmployeeDocFilePropertiesDialog extends AbstractPropertiesDialog {

    private final TextBox name = new TextBox();
    private final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private FileResource file;

    public EmployeeDocFilePropertiesDialog(FileResource fileItem) {

        if (fileItem != null) {
            file = fileItem;
            final VerticalPanel outer = new VerticalPanel();
            inner = new DecoratedTabPanel();
            inner.setAnimationEnabled(true);
            final VerticalPanel generalPanel = new VerticalPanel();
            inner.add(generalPanel, wfmStrings.properties());
            setWidth(350);
            inner.selectTab(0);
            final FlexTable generalTable = new FlexTable();
            generalTable.setText(0, 0, wfmStrings.name());
            generalTable.setText(1, 0, wfmStrings.folder());
            generalTable.setText(2, 0, wfmStrings.owner());
            generalTable.setText(3, 0, wfmStrings.modifiedDate());
            name.setText(file.getName());
            name.setMaxLength(300);
            generalTable.setWidget(0, 1, name);
            if (file.getFolderName() != null) {
                if (file.getFolderName().equals("Employee Profile Attachments"))
                    generalTable.setText(1, 1, hrmsStrings.employeeProfileAttach());
                else generalTable.setText(1, 1, file.getFolderName());
            } else {
                generalTable.setText(1, 1, "-");
            }
            generalTable.setText(2, 1, file.getOwner() != null ? file.getOwner().getName() : file.getOwnerName());
            final DateTimeFormat formatter = DateTimeFormat.getFormat("d/M/yyyy h:mm a");
            generalTable.setText(3, 1, file.getModificationDate() != null ? formatter.format(file.getModificationDate()) : "");
            generalTable.addStyleName("file--EmployeeDocFileProperties props-table");
            generalTable.getFlexCellFormatter().setStyleName(0, 0, "props-labels");
            generalTable.getFlexCellFormatter().setStyleName(1, 0, "props-labels");
            generalTable.getFlexCellFormatter().setStyleName(2, 0, "props-labels");
            generalTable.getFlexCellFormatter().setStyleName(3, 0, "props-labels");
            generalTable.setCellSpacing(4);
            final WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY, event -> {
                if (!validateFileName()) {
                    return;
                }
                updateFile();
                closeDialog();
            });
            final WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), event -> closeDialog());

            generalPanel.add(generalTable);

            generalPanel.setSpacing(4);
            outer.add(inner);
            outer.addStyleName("doc-TabPanelBottom");

            super.addButton(cancel);
            super.addButton(ok);

            super.center();
            add(outer);
        }
    }

    private void updateFile() {
        String newFileName = null;
        if (!file.getName().equals(name.getText())) {
            newFileName = name.getText();
        }

        DocumentsService.App.get().updateFile(file.getObjectId(), newFileName, false, null, new AbstractAsyncCallback<Void>() {
            @Override
            public void success(Void v) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMPLOYEE_FILES_UPLOADED, v, EmployeeDocFilePropertiesDialog.this);
//                EmployeeDocumentsListView getClass = new EmployeeDocumentsListView();
//                getClass.reloadPage();
//                DocumentsView.get().updateFileCache(true, false /* do not clear selected file*/);
            }

            @Override
            public void failure(Throwable throwable) {
                try {
                    throw throwable;
                } catch (InsufficientPermissionsException e) {
                    DocumentsView.get().displayError(e.getMessage());
                } catch (ObjectNotFoundException e) {
                    DocumentsView.get().displayError(wfmStrings.resursNotFoundAndNoShare());
                } catch (DuplicateNameException e) {
                    DocumentsView.get().displayError(wfmStrings.file() + " " + wfmStrings.withTheSameNameAlreadyExist());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }

    private boolean validateFileName() {
        if (!Utils.isValidFolderName(name.getText())) {
            DocumentsView.get().displayError(wfmStrings.youCantUseFileName());
            return false;
        }
        return true;
    }

    @Override
    protected void accept() {

    }
}
