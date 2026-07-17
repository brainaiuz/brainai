package com.edatasite.workforce.gwt.messagecenter.client.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.EmailFolder;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterServiceAsync;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialLabel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Azazello on 12/10/15.
 */
public class ManageFoldersModal extends KpiModal implements Constants {
    private static final MessageCenterServiceAsync mcService = MessageCenterService.App.get();
    private CaptionPanel treeContent;
    private Integer emailAccountID;
    private Integer trashFolderID;
    private Integer sentFolderID;
    private ArrayList<EmailFolder> treeFolders = new ArrayList<>();
    private Tree tree;
    private DataListBox trashFolder;
    private DataListBox sentFolder;
    private WfmButton2 refreshButton;

    public ManageFoldersModal(Integer emailAccountID) {
        this.emailAccountID = emailAccountID;
        setWidth(500);
        setTitle(wfmStrings.chooseFoldersToFetch());
        init();
        getFolders(false);
        open();
    }

    private void init() {
        FlexTable table = new FlexTable();
        table.setCellPadding(10);
        table.setCellSpacing(10);

        ScrollPanel treePanel = new ScrollPanel();
        treeContent = new CaptionPanel(wfmStrings.folders());
        treeContent.add(treePanel);
        treeContent.addStyleName("legend");
        tree = new Tree();
        treePanel.add(tree);
        treePanel.setHeight("200px");

        HTMLPanel panel = new HTMLPanel("");
        panel.setStyleName("panel");

        HTMLPanel sentPanel = new HTMLPanel("");
        sentPanel.setStyleName("form-group");
        MaterialLabel sentLabel = new MaterialLabel(wfmStrings.defaultSent());
        sentLabel.setStyleName("form-group__label");
        sentFolder = new DataListBox();
        sentPanel.add(sentLabel);
        sentPanel.add(sentFolder);

        HTMLPanel trashPanel = new HTMLPanel("");
        trashPanel.setStyleName("form-group");
        MaterialLabel trashLabel = new MaterialLabel(wfmStrings.defaultTrash());
        trashLabel.setStyleName("form-group__label");
        trashFolder = new DataListBox();
        trashPanel.add(trashLabel);
        trashPanel.add(trashFolder);

        panel.add(sentPanel);
        panel.add(trashPanel);

        table.setWidget(0, 0, treeContent);
        table.setWidget(0, 1, panel);
        add(table);

        refreshButton = new WfmButton2(wfmStrings.refreshFolders(), WfmButton2.BTN_SECONDARY, clickEvent -> getFolders(true));
        addButton(refreshButton);
        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save()));
    }

    private void getFolders(boolean refresh) {
        LoadingPanel.loading(true, ManageFoldersModal.this);
        refreshButton.setEnabled(!refresh);
        mcService.getEmailAccountFolders(emailAccountID, refresh, new AbstractAsyncCallback<ArrayList<EmailFolder>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, ManageFoldersModal.this);
            }

            @Override
            public void onSuccess(ArrayList<EmailFolder> result) {
                LoadingPanel.loading(false, ManageFoldersModal.this);
                if (result != null) {
                    tree.clear();
                    sentFolder.clear();
                    trashFolder.clear();
                    fillFields(result);
                } else if (refresh) {
                    Info.show(wfmStrings.couldNotConnectToTheServer(), Info.Type.WARNING);
                }
            }
        });
    }

    private void fillFields(ArrayList<EmailFolder> folders) {
        fillTree(folders, null);
        ArrayList<SelectItem> items = new ArrayList<>();
        for (EmailFolder folder : folders) {
            items.add(new SelectItem(folder.getObjectID(), folder.getName()));
            if (MCFolderType.TRASH.equals(folder.getType())) {
                trashFolderID = folder.getObjectID();
            }
            if (MCFolderType.SENT.equals(folder.getType())) {
                sentFolderID = folder.getObjectID();
            }
        }
        trashFolder.setItems(items.toArray(new SelectItem[]{}));
        if (trashFolderID != null) {
            trashFolder.setSelected(trashFolderID);
        }
        sentFolder.setItems(items.toArray(new SelectItem[]{}));
        if (sentFolderID != null) {
            sentFolder.setSelected(sentFolderID);
        }
    }

    private void fillTree(ArrayList<EmailFolder> folders, EmailFolderTreeItem parent) {
        for (EmailFolder f : folders) {
            if (!treeFolders.contains(f)) {
                if (parent == null && f.getParentID() == null) {
                    EmailFolderTreeItem item = new EmailFolderTreeItem(f, tree, parent);
                    tree.addItem(item);
                    treeFolders.add(f);
                    fillTree(folders, item);
                } else if (parent != null && parent.getItem().getObjectID().equals(f.getParentID())) {
                    EmailFolderTreeItem item = new EmailFolderTreeItem(f, tree, parent);
                    treeFolders.add(f);
                    parent.addItem(item);
                    fillTree(folders, item);
                }
            }
        }
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateDataListBoxRequired(trashFolder)) {
            errors++;
        }
        if (!Validation.validateDataListBoxRequired(sentFolder)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void getSelectedIDs(ArrayList<Integer> fetchableFolders, List<EmailFolderTreeItem> childs) {
        for (EmailFolderTreeItem treeItem : childs) {
            if (treeItem.getCheckBox().getValue()) {
                fetchableFolders.add(treeItem.getItem().getObjectID());
            }
            if (treeItem.getChildren() != null) {
                getSelectedIDs(fetchableFolders, treeItem.getChildren());
            }
        }
    }

    private void save() {
        if (!validate()) {
            return;
        }
        ArrayList<Integer> fetchableFolders = new ArrayList<>();
        for (int i = 0; i < tree.getItemCount(); i++) {
            EmailFolderTreeItem treeItem = (EmailFolderTreeItem) tree.getItem(i);
            if (treeItem.getCheckBox().getValue()) {
                fetchableFolders.add(treeItem.getItem().getObjectID());
            }
            if (treeItem.getChildren() != null) {
                getSelectedIDs(fetchableFolders, treeItem.getChildren());
            }
        }
        LoadingPanel.loading(true);
        mcService.saveFetchableFolders(fetchableFolders, trashFolder.getSelectedId(), sentFolder.getSelectedId(), emailAccountID, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                close();
                Info.show(wfmStrings.messFoldersSettedSucc(), Info.Type.INFO);
                Utils.openURLCurrentTab(Utils.getHostURL() + "MessageCenter.html");
            }
        });
    }
}
