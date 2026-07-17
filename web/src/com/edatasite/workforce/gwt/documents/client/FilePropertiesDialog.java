package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Set;

/**
 * The 'File properties' dialog box implementation.
 *
 * @author Sherali
 */
public class FilePropertiesDialog extends AbstractPropertiesDialog {
    final PermissionsList permList;

    private final TextBox name = new TextBox();

    final FileResource file;

    /**
     * The widget's constructor.
     */
    public FilePropertiesDialog() {

        // Set the dialog's caption.
//        setText("File properties");

        file = (FileResource) DocumentsView.get().getCurrentSelection();
        permList = new PermissionsList(file.getPermissions(), file.getOwner(), file, false);

        // Outer contains inner and buttons.
        final VerticalPanel outer = new VerticalPanel();
        final FocusPanel focusPanel = new FocusPanel(outer);
        // Inner contains generalPanel and permPanel.
        inner = new DecoratedTabPanel();
        inner.setAnimationEnabled(true);
        inner.addStyleName("decorated-tab--panel");
        final VerticalPanel generalPanel = new VerticalPanel();
        final VerticalPanel permPanel = new VerticalPanel();
        final HorizontalPanel permButtons = new HorizontalPanel();
        final HorizontalPanel pathPanel = new HorizontalPanel();

        inner.add(generalPanel, wfmStrings.properties());
        inner.add(permPanel, wfmStrings.share());
        setWidth(550);
        inner.selectTab(0);

        final FlexTable generalTable = new FlexTable();
        generalTable.setText(0, 0, wfmStrings.name());
        generalTable.setText(1, 0, wfmStrings.folder());
        generalTable.setText(2, 0, wfmStrings.owner());
        generalTable.setText(3, 0, wfmStrings.modifiedDate());
        name.setText(file.getName());
        name.setMaxLength(255);
        generalTable.setWidget(0, 1, name);
        if (file.getFolderName() != null) {
            generalTable.setText(1, 1, file.getFolderName());
        } else {
            generalTable.setText(1, 1, "-");
        }
        generalTable.setText(2, 1, file.getOwner().getName());
        final DateTimeFormat formatter = DateTimeFormat.getFormat("d/M/yyyy h:mm a");
        generalTable.setText(3, 1, formatter.format(file.getModificationDate()));

        generalTable.addStyleName("file--FilePropertiesDialog");
        generalTable.getFlexCellFormatter().setStyleName(0, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(1, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(2, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(3, 0, "props-labels");
        generalTable.setCellSpacing(4);

        // Create the 'OK' button, along with a listener that hides the dialog
        // when the button is clicked.
        final WfmButton2 ok = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_PRIMARY, event -> {
            if (!validateFileName()) {
                return;
            }
            updateFile();
            closeDialog();
        });
//        buttons.setCellHorizontalAlignment(ok, HasHorizontalAlignment.ALIGN_CENTER);
        // Create the 'Cancel' button, along with a listener that hides the
        // dialog when the button is clicked.
        final WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), event -> closeDialog());

        generalPanel.add(generalTable);

        generalPanel.setSpacing(4);

        final WfmButton2 add = new WfmButton2(wfmStrings.addGroup(), event -> {
            PermissionsAddDialog dlg = new PermissionsAddDialog(null, permList, false);
            dlg.center();
        });

        final WfmButton2 addUser = new WfmButton2(wfmStrings.addUser(), event -> {
            PermissionsAddDialog dlg = new PermissionsAddDialog(null, permList, true);
            dlg.center();
        });
        final WfmButton2 addClient = new WfmButton2(Property.get(Constants.CLIENT_LIST, wfmStrings.addMess(), wfmStrings.customer()), event -> {
            PermissionsAddToClientDialog dlg = new PermissionsAddToClientDialog(null, permList, CrmConstants.CUSTOMER);
            dlg.center();
        });
        final WfmButton2 addSupplier = new WfmButton2(Property.get(Constants.SUPPLIER_LIST, wfmStrings.addMess(), wfmStrings.supplier()), event -> {
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

        Div scrollPanel = new Div();
        scrollPanel.setStyleName("scroll-box");
        scrollPanel.add(permList);
        permPanel.add(scrollPanel);
        permPanel.add(permButtons);

        TextBox path = new TextBox();
        path.setWidth("100%");
        path.addClickHandler(event -> {
            DocumentsView.enableIESelection();
            ((TextBox) event.getSource()).selectAll();
            DocumentsView.preventIESelection();
        });
        path.setText(file.getUri());
        path.setTitle("Use this URI for sharing this file with the world (crtl-C/cmd-C to copy to system clipboard)");
        path.setWidth("100%");
        path.setReadOnly(true);
        pathPanel.setWidth("100%");
        pathPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        pathPanel.add(new Label("Sharing URI"));
        pathPanel.setSpacing(8);
        pathPanel.addStyleName("doc-TabPanelBottom");
        pathPanel.add(path);
        pathPanel.setVisible(file.isReadForAll());
        permPanel.add(pathPanel);

        outer.add(inner);
        outer.addStyleName("doc-TabPanelBottom");
        focusPanel.setFocus(true);
        add(outer);
        addButton(cancel);
        addButton(ok);

        addStyleName("no-border file--FilePropertiesDialog");

        super.center();
    }

    private void updateFile() {
        permList.updatePermissionsAccordingToInput();
        Set<PermissionHolder> perms = permList.getPermissions();
        ArrayList<PermissionHolder> permissionHolders = new ArrayList<>();
        permissionHolders.addAll(perms);
        String newFileName = null;
        if (!file.getName().equals(name.getText())) {
            newFileName = name.getText();
        }

        DocumentsView.get().getDocumentsService().updateFile(file.getObjectId(), newFileName, false, permissionHolders, new AbstractAsyncCallback() {
            @Override
            public void success(Object result) {
                DocumentsView.get().updateFileCache(true, false /* do not clear selected file*/);
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

    /**
     * Accepts any change and updates the file
     */
    @Override
    protected void accept() {

    }

    private boolean validateFileName() {
        if (!Utils.isValidFolderName(name.getText())) {
            DocumentsView.get().displayError(wfmStrings.youCantUseFileName());
            return false;
        }
        return true;
    }

}
