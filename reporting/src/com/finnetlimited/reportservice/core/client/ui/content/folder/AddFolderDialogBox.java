package com.finnetlimited.reportservice.core.client.ui.content.folder;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.FolderType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.ui.loading.DRSLoadingPanel;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: Power
 * Date: 3/12/12
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddFolderDialogBox extends KpiModal {

    private TextBox textbox;
    private DataListBox listTypes;
    private Button button;

    private FlexTable folderFlexTable;
    private String folderName;
    private String folderType;
    private int folderId;
    private Command command;
    private boolean isEditMode;


    public AddFolderDialogBox() {
        super();
        getContent().setStyleName("addfolder-content");
        setWidth("240px");
        renderContent();
    }

    public void setFolderName(String foldername) {
        folderName = foldername;
    }

    public void setFolderId(int folderid) {
        folderId = folderid;
    }

    public void setFolderType(String foldertype) {
        folderType = foldertype;
    }

    public void setFolderListingDialogBox(FolderListingDialogBox fldb) {
        // folderListingDialogBox = fldb;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    private void renderContent() {

        textbox = new TextBox();

        listTypes = new DataListBox();
        listTypes.addListItem(new SelectItem(0, FolderType.Public.name(), FolderType.Public.name()));
        listTypes.addListItem(new SelectItem(1, FolderType.Private.name(), FolderType.Private.name()));

        button = new Button(wfmStrings.save(), (ClickListener) sender -> {
            if ("".equals(textbox.getText().trim())) {
                final WfmMessageBox msg = new WfmMessageBox(IconEnum.ERROR, Action.OK, "Please enter a folder name");
                msg.center();
                return;
            }

            if (isEditMode) {
                updateFolder();
            } else {
                saveFolder();
            }
        });
        button.getElement().setAttribute("style", "float:right;");

        folderFlexTable = new FlexTable();
        folderFlexTable.setStyleName("inner-ftable");
        folderFlexTable.setHTML(0, 0, "<strong> " + wfmStrings.folderName() + " <span style='color:red'> * </span> </strong>");
        folderFlexTable.setWidget(0, 1, textbox);

        folderFlexTable.setHTML(1, 0, "<strong> " + wfmStrings.folderType() + " </strong>");
        folderFlexTable.setWidget(1, 1, listTypes);

        folderFlexTable.setHTML(2, 0, "&nbsp;");
        folderFlexTable.setWidget(2, 1, button);

        add(folderFlexTable);
        center();

    }

    public void show(boolean iseditmode) {
        textbox.setText(folderName);
        listTypes.setSelectedByValue(folderType);
        isEditMode = iseditmode;
        open();
    }

    private void saveFolder() {
        FolderRpc folder = new FolderRpc();
        folder.setName(textbox.getText());
        folder.setType(listTypes.getSelectedItem().getName());

        DRSLoadingPanel.show();
        CoreService.App.get().saveFolder(folder, new AsyncCallback<Boolean>() {
            public void onFailure(Throwable throwable) {
                final WfmMessageBox msg = new WfmMessageBox(IconEnum.ERROR, Action.OK, "The folder has been existed in our database. You should enter another a folder name");
                msg.center();
            }

            public void onSuccess(Boolean result) {
                DRSLoadingPanel.hide();
                if (result) {
                    if (command != null) {
                        command.execute();
                    }
                    close();
                } else {
                    final WfmMessageBox msg = new WfmMessageBox(IconEnum.ERROR, Action.OK, "The folder has been existed in our database. You should enter another a folder name");
                    msg.center();
                }
            }
        });
    }

    private void updateFolder() {
        FolderRpc folder = new FolderRpc();
        folder.setId(folderId);
        folder.setName(textbox.getText());
        folder.setType(listTypes.getSelectedItem().getName());

        DRSLoadingPanel.show();
        CoreService.App.get().updateFolder(folder, new AsyncCallback<Boolean>() {
            public void onFailure(Throwable throwable) {

            }

            public void onSuccess(Boolean result) {
                DRSLoadingPanel.hide();
                if (result) {
                    if (command != null) {
                        command.execute();
                    }
                    close();
                } else {
                    final WfmMessageBox msg = new WfmMessageBox(IconEnum.ERROR, Action.OK, "The folder has been existed in our database. You should enter another a folder name");
                    msg.center();
                }
            }

        });
    }


}
