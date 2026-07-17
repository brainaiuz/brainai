package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class FolderPropertiesDialog extends KpiModal {

    protected final DocumentImages.Images images = DocumentImages.get();

    final Div pnlPermissionListContainer;

    /**
     * The widget that holds the folderName of the folder.
     */
    private final TextBox folderName = new TextBox();

    /**
     * A flag that denotes whether the dialog will be used to create or modify a
     * folder.
     */
    private final boolean create;

    final FolderResource folder;

    final TabPanel inner;
    PermissionsList permList;

    /**
     * The widget's constructor.
     *
     * @param _create true if the dialog is displayed for creating a new
     *                sub-folder of the selected folder, false if it is displayed
     *                for modifying the selected folder
     */
    public FolderPropertiesDialog(boolean _create) {

        // Enable IE selection for the dialog (must disable it upon closing it)
        DocumentsView.enableIESelection();

        create = _create;
        DnDTreeItem folderItem = (DnDTreeItem) DocumentsView.get().getFolders().getCurrent();
        folder = folderItem.getFolderResource();

        if (!create) {
            folderName.setEnabled(false);
        }
        VerticalPanel outer = new VerticalPanel();
        inner = new DecoratedTabPanel();
        inner.setAnimationEnabled(true);
        inner.addStyleName("decorated-tab--panel");
        VerticalPanel generalPanel = new VerticalPanel();
        VerticalPanel permPanel = new VerticalPanel();
        FlexTable buttons = new FlexTable();
        HorizontalPanel permButtons = new HorizontalPanel();
        inner.add(generalPanel, create ? wfmStrings.createFolder() : wfmStrings.folderProperties());
        if (!create) {
            inner.add(permPanel, wfmStrings.shareFile());
        }
        inner.selectTab(0);
        setWidth(250);
        final FlexTable generalTable = new FlexTable();
        generalTable.setText(0, 0, wfmStrings.name());
        generalTable.setText(1, 0, wfmStrings.parent());
        generalTable.setText(2, 0, wfmStrings.createdBy());
        generalTable.setText(3, 0, wfmStrings.modifiedDate());
        folderName.setText(create ? "" : folder.getName());
        folderName.setMaxLength(255);
        generalTable.setWidget(0, 1, folderName);
        if (create) {
            generalTable.setText(1, 1, folder.getName());
        } else if (folder.getParentName() == null) {
            generalTable.setText(1, 1, "-");
        } else {
            generalTable.setText(1, 1, folder.getParentName());
        }
        generalTable.setText(2, 1, folder.getOwner().getName());
        DateTimeFormat formatter = DateTimeFormat.getFormat("d/M/yyyy h:mm a");
        if (folder.getModificationDate() != null) {
            generalTable.setText(3, 1, formatter.format(folder.getModificationDate()));
        }
        generalTable.addStyleName("file--FolderPropertiesDialog props-table");
        generalTable.getFlexCellFormatter().setStyleName(0, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(1, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(2, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(3, 0, "props-labels");
        generalTable.setCellSpacing(4);

        String okLabel;
        if (create) {
            okLabel = wfmStrings.create();
        } else {
            okLabel = wfmStrings.update();
        }
        WfmButton2 ok = new WfmButton2(okLabel, WfmButton2.BTN_PRIMARY, event -> {
            if (!validateFolderName()) {
                return;
            }
            createOrUpdateFolder();

            closeDialog();
        });
        buttons.setWidget(0, 0, ok);
        buttons.getFlexCellFormatter().getElement(0, 0).getStyle().setTextAlign(Style.TextAlign.CENTER);
        final WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, event -> closeDialog());
        buttons.setWidget(0, 1, cancel);
        buttons.getFlexCellFormatter().getElement(0, 1).getStyle().setTextAlign(Style.TextAlign.CENTER);
        buttons.addStyleName("doc-TabPanelBottom");

        WfmButton2 add = new WfmButton2(wfmStrings.addGroup(), event -> {
            PermissionsAddDialog dlg = new PermissionsAddDialog(null, permList, false);
            dlg.center();
        });

        WfmButton2 addUser = new WfmButton2(wfmStrings.addUser(), event -> {
            PermissionsAddDialog dlg = new PermissionsAddDialog(null, permList, true);
            dlg.center();
        });
        WfmButton2 addClient = new WfmButton2(Property.get(Constants.CLIENT_LIST, wfmStrings.addMess(), wfmStrings.customer()), event -> {
            PermissionsAddToClientDialog dlg = new PermissionsAddToClientDialog(null, permList, CrmConstants.CUSTOMER);
            dlg.center();
        });
        WfmButton2 addSupplier = new WfmButton2(Property.get(Constants.SUPPLIER_LIST, wfmStrings.addMess(), wfmStrings.supplier()), event -> {
            PermissionsAddToClientDialog dlg = new PermissionsAddToClientDialog(null, permList, CrmConstants.SUPPLIER);
            dlg.center();
        });
        if (!Utils.hasRole(Constants.CLIENT)) {
            permButtons.add(add);
            permButtons.setCellHorizontalAlignment(add, HasHorizontalAlignment.ALIGN_CENTER);

            permButtons.add(addUser);
            permButtons.setCellHorizontalAlignment(addUser, HasHorizontalAlignment.ALIGN_CENTER);

            permButtons.add(addClient);
            permButtons.setCellHorizontalAlignment(addClient, HasHorizontalAlignment.ALIGN_CENTER);

            permButtons.add(addSupplier);
            permButtons.setCellHorizontalAlignment(addSupplier, HasHorizontalAlignment.ALIGN_CENTER);
        }
        permButtons.setCellHorizontalAlignment(cancel, HasHorizontalAlignment.ALIGN_CENTER);
        permButtons.setSpacing(8);
        permButtons.addStyleName("doc-TabPanelBottom");

        generalPanel.add(generalTable);

        pnlPermissionListContainer = new Div();
        pnlPermissionListContainer.setStyleName("scroll-box");

        permPanel.add(pnlPermissionListContainer);
        permPanel.add(permButtons);
        outer.add(inner);
        outer.add(buttons);
        outer.setCellHeight(buttons, "50px");
        outer.setCellVerticalAlignment(buttons, HasVerticalAlignment.ALIGN_MIDDLE);
        outer.setCellHorizontalAlignment(buttons, HasHorizontalAlignment.ALIGN_CENTER);
        outer.addStyleName("doc-TabPanelBottom");

        add(outer);
        addButton(cancel);
        addButton(ok);

        setWidth(550);

        if (create) {
            folderName.setFocus(true);
        } else {
            ok.setFocus(true);
        }
        loadFolderPermissions(folder.getObjectId());
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

    /**
     * Generate an RPC request to create a new folder.
     */
    private void createFolder() {
        DocumentsView.get().getDocumentsService().createFolder(folder.getObjectId(), folderName.getText(), new AbstractAsyncCallback<FolderResource>() {
            @Override
            public void success(FolderResource result) {
//                    DocumentsView.get().getFolders().updateFolder((DnDTreeItem) DocumentsView.get().getFolders().getCurrent(), result);
                DnDTreeItem folderItem = (DnDTreeItem) DocumentsView.get().getFolders().getCurrent();
                if (!folderItem.getState()) {
                    final DnDTreeItem item = new DnDTreeItem(DocUtils.imageItemHTML(images.folderYellow(), result.getName()), false, folderItem.getPopupTree(), true);
                    folderItem.addItem(item);
                    folderItem.removeStyleName("removeTreeItemPlus");
                }
                DocumentsView.get().getFolders().updateFolder((DnDTreeItem) DocumentsView.get().getFolders().getCurrent());
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DOCUMENTS_UPLOAD_FILES, null, FolderPropertiesDialog.this);
            }

            @Override
            public void failure(Throwable throwable) {
                try {
                    throw throwable;
                } catch (InsufficientPermissionsException | DuplicateNameException e) {
                    DocumentsView.get().displayError(wfmStrings.youDontHaveTheNecessaryPermissionsAndSameNaneAlreadyExist());
                } catch (ObjectNotFoundException e) {
                    DocumentsView.get().displayError(wfmStrings.resursNotFoundAndNoShare());
                } catch (Throwable e) {
                    // last resort  a very unexpected exception
                }
            }
        });
    }

    /**
     * Upon closing the dialog by clicking OK or pressing ENTER this method does
     * the actual work of modifying folder properties or creating a new Folder
     * depending on the value of the create field
     */
    private void createOrUpdateFolder() {
        if (create) {
            createFolder();
        } else {
            updateFolder();
        }

    }

    private void loadFolderPermissions(Integer folderId) {
        LoadingPanel.loading(true, pnlPermissionListContainer);
        DocumentsService.App.get().getFolderPermissions(folderId, new AbstractAsyncCallback<HashSet<PermissionHolder>>() {
            @Override
            public void success(HashSet<PermissionHolder> permssions) {
                LoadingPanel.loading(false);
                permList = new PermissionsList(permssions, folder.getOwner(), folder, false);
                pnlPermissionListContainer.clear();
                pnlPermissionListContainer.add(permList);
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }
        });
    }

    private void updateFolder() {
        if (!validateFolderName()) {
            return;
        }
        permList.updatePermissionsAccordingToInput();
        Set<PermissionHolder> perms = permList.getPermissions();
        ArrayList<PermissionHolder> permissionHolders = new ArrayList<>();
        permissionHolders.addAll(perms);
        DocumentsView.get().getDocumentsService().updateFolder(folder, permissionHolders, new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                DocumentsView.get().getFolders().updateFolder((DnDTreeItem) DocumentsView.get().getFolders().getCurrent());
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

    public void selectTab(int _tab) {
        inner.selectTab(_tab);
    }

    private boolean validateFolderName() {
        if (!Utils.isValidFolderName(folderName.getText())) {
            DocumentsView.get().displayError(wfmStrings.youCantUseFolderName());
            return false;
        }
        return true;
    }
}
