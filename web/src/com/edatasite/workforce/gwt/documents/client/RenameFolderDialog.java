package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;

import static com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2.BTN_PRIMARY;


/**
 * Created by IntelliJ IDEA.
 * User: Sher
 * Date: 03.08.2010
 * Time: 15:24:41
 */
public class RenameFolderDialog extends KpiModal {

    /**
     * The widget that holds the folderName of the folder.
     */
    private final TextBox folderName = new TextBox();

    final FolderResource folder;


    /**
     * The widget's constructor.
     */
    public RenameFolderDialog() {
        // Enable IE selection for the dialog (must disable it upon closing it)
        DocumentsView.enableIESelection();

        final DnDTreeItem folderItem = (DnDTreeItem) DocumentsView.get().getFolders().getCurrent();
        folder = folderItem.getFolderResource();

        // Use this opportunity to set the dialog's caption.
        setTitle("Rename folder");
        setWidth(400);
        // Outer contains inner and buttons
        final VerticalPanel outer = new VerticalPanel();
        final HorizontalPanel buttons = new HorizontalPanel();

        final FlexTable generalTable = new FlexTable();
        generalTable.setCellPadding(5);
        generalTable.setText(0, 0, wfmStrings.name());
        generalTable.setText(1, 0, wfmStrings.parent());
        generalTable.setText(2, 0, wfmStrings.createdBy());
        generalTable.setText(3, 0, wfmStrings.modifiedDate());
        folderName.setText(folder.getName());
        folderName.setMaxLength(255);
        generalTable.setWidget(0, 1, folderName);
        generalTable.setText(1, 1, folder.getName());
        generalTable.setText(2, 1, folder.getOwner().getName());
        DateTimeFormat formatter = DateTimeFormat.getFormat("d/M/yyyy h:mm a");
        if (folder.getModificationDate() != null) {
            generalTable.setText(3, 1, formatter.format(folder.getModificationDate()));
        }
        generalTable.addStyleName("file--RenameFolderDialog");
        generalTable.getFlexCellFormatter().setStyleName(0, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(1, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(2, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(3, 0, "props-labels");
//        generalTable.getFlexCellFormatter().setStyleName(0, 1, "props-values");
//        generalTable.getFlexCellFormatter().setStyleName(1, 1, "props-values");
//        generalTable.getFlexCellFormatter().setStyleName(2, 1, "props-values");
//        generalTable.getFlexCellFormatter().setStyleName(3, 1, "props-values");


        final WfmButton2 ok = new WfmButton2(wfmStrings.update(), BTN_PRIMARY, event -> {
            if (!validateFolderName()) {
                return;
            }
            updateFolder();

            closeDialog();
        });
        buttons.add(ok);
        buttons.setCellHorizontalAlignment(ok, HasHorizontalAlignment.ALIGN_CENTER);
        buttons.setCellVerticalAlignment(ok, HasVerticalAlignment.ALIGN_BOTTOM);
        buttons.setCellHeight(ok, "50px");
        // Create the 'Cancel' button, along with a listener that hides the
        // dialog
        // when the button is clicked.
        final WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), event -> closeDialog());
        buttons.add(cancel);
        buttons.setCellHorizontalAlignment(cancel, HasHorizontalAlignment.ALIGN_CENTER);
        buttons.setCellVerticalAlignment(cancel, HasVerticalAlignment.ALIGN_BOTTOM);
        buttons.setSpacing(8);
        buttons.setCellHeight(cancel, "50px");
        buttons.addStyleName("doc-TabPanelBottom");

        outer.add(generalTable);
        outer.add(buttons);
        outer.setCellHorizontalAlignment(buttons, HasHorizontalAlignment.ALIGN_CENTER);
        outer.addStyleName("doc-TabPanelBottom");

        add(outer);
    }

    @Override
    public void center() {
        super.center();
        folderName.setFocus(true);
    }


    /**
     * Enables IE selection prevention and hides the dialog
     * (we disable the prevention on creation of the dialog)
     */
    public void closeDialog() {
        DocumentsView.preventIESelection();
        close();
    }

    private void updateFolder() {
        if (!validateFolderName()) {
            return;
        }
        if (!folder.getName().equals(folderName.getText())) {
            folder.setName(folderName.getText());
        }

        DocumentsView.get().getDocumentsService().updateFolder(folder, null, new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                DocumentsView.get().getFolders().updateFolder((DnDTreeItem) DocumentsView.get().getFolders().getCurrent());
                //T3834 fix
                DocumentsView.get().getFolders().setCurrent(DocumentsView.get().getFolders().getCurrent().getParentItem());
                DocumentsView.get().showFileList(true);
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
                    DocumentsView.get().displayError(wfmStrings.youDontHaveTheNecessaryPermissionsAndSameNaneAlreadyExist());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }

    private boolean validateFolderName() {
        if (!Utils.isValidFolderName(folderName.getText())) {
            DocumentsView.get().displayError(wfmStrings.youCantUseFolderName());
            return false;
        }
        return true;
    }
}
