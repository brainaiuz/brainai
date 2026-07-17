package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelectIcons;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.gwtupload.FileWidget;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 4/18/12
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */

public class StorageDocsSelectPopup extends Div implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final TreeSelectIcons icons = GWT.create(TreeSelectIcons.class);
    private WfmButton2 selectButton;
    private FileUploadDialog uploadDialog;
    private String storageType;
    private TextBox searchTextBox;
    private FolderWidget previousFolder;
    private Label noFilesWidget = new Label(wfmStrings.noFilesInSelectedFolder());
    private Label dataLoading = new Label(wfmStrings.dataLoading());
    private FolderWrap foldersWrap;
    private HashMap<String, CustomCheckBox> checkBoxes = new HashMap<>();
    private HashMap<String, FileResource> selectedFiles = new HashMap<>();
    private HashMap<CustomCheckBox, FileResource> allFiles = new HashMap<>();
    private HashMap<String, List<FileResource>> folderFiles = new HashMap<>();
    private int index = 0;
    private int folderID = 0;
    private CheckBoxList checkBoxList;

    public StorageDocsSelectPopup(FileUploadDialog uploadDialog, String storageType) {
        this.uploadDialog = uploadDialog;
        this.storageType = storageType;
        onInitialize();
    }

    public void onInitialize() {
        Div listHeader = new Div("kpi-upload__list-header");
        Div searchForm = new Div();
        add(listHeader);
        listHeader.add(searchForm);
        searchBox();
        searchForm.add(searchTextBox);

        checkBoxList = new CheckBoxList();
        add(checkBoxList);

        foldersWrap = new FolderWrap(folderWidget -> {
            if (previousFolder == null) {
                previousFolder = folderWidget;
            }
            folderWidget.select();
            if (!previousFolder.equals(folderWidget)) {
                previousFolder.deselect();
            }
            FolderResource folder = folderWidget.getFolder();
            List<FileResource> fileResources = folderFiles.get(folder.getDriveFolderId());

            clearTable();
            if (fileResources != null && !fileResources.isEmpty()) {
                for (FileResource resource : fileResources) {
                    addFileToTable(resource);
                }
            } else if (fileResources != null && fileResources.isEmpty()) {
//                table.add(noFilesWidget); //TODO restore
                noFilesWidget.setVisible(true);
                noFilesWidget.getElement().getStyle().setMarginTop(90d, Style.Unit.PX);
                noFilesWidget.getElement().getStyle().setMarginLeft(80d, Style.Unit.PX);
            } else {
                loadFileResources(folder.getDriveFolderId());
            }
            previousFolder = folderWidget;
        },
                new FolderOpen() {
                    @Override
                    public void execute(FolderWidget parent) {
                        loadSubfolders(parent);
                    }
                });
        uploadDialog.setStorageType(storageType);

        noFilesWidget.getElement().getStyle().setMarginTop(20, Style.Unit.PCT);
        noFilesWidget.getElement().getStyle().setMarginLeft(15, Style.Unit.PCT);

        selectButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, (ClickHandler) clickEvent -> {
            if (!selectedFiles.isEmpty()) {
                int i = 1;
                for (FileResource resource : selectedFiles.values()) {
                    if (!uploadDialog.getGoogleOrOfficeFiles().containsValue(resource)) {
                        uploadDialog.getGoogleOrOfficeFiles().put(i, resource);
                    }
                    final FileWidget fileWidget = new FileWidget(resource.getBodyId() + "", resource.getName());
                    final int finalI = i;
                    fileWidget.setRemoveCommand(() -> {
                        uploadDialog.getGoogleOrOfficeFiles().remove(finalI);
                        uploadDialog.removeFromUploadContent(fileWidget);
                    });
                    fileWidget.setCompleted(101);
                    uploadDialog.addToUploadContent(fileWidget);
                    uploadDialog.runCommand();
                    i++;
                }
                uploadDialog.setVisibleToGoogleDocsPanel();
            } else {
                Info.show(wfmStrings.pleaseSelectDocumentsToLink(), Info.Type.WARNING);
            }
            StorageDocsSelectPopup.this.removeFromParent();
            uploadDialog.uploadState();
        });

        setTitle("Data loading is in progress. Please wait...");
        LoadingPanel.loading(true, StorageDocsSelectPopup.this);
        DocumentsService.App.get().getUsersAllGoogleDocumentsAndFolders(storageType, new AsyncCallback<FolderResource[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false, StorageDocsSelectPopup.this);
                setTitle(GOOGLE.equals(storageType) ? wfmStrings.selectGoogleDocs() : wfmStrings.selectOfficeDocs());
            }

            @Override
            public void onSuccess(FolderResource[] folderResources) {
                LoadingPanel.loading(false, StorageDocsSelectPopup.this);
                setTitle(GOOGLE.equals(storageType) ? wfmStrings.selectGoogleDocs() : wfmStrings.selectOfficeDocs());

                if (folderResources != null) {
                    boolean isFirstFolder = true;

                    for (FolderResource folder : folderResources) {
                        FolderWidget folderWidget = new FolderWidget(folder, !folder.getSubfolders().isEmpty());
                        foldersWrap.add(folderWidget);
                        if (isFirstFolder) {
                            isFirstFolder = false;
                            previousFolder = folderWidget;
                            folderWidget.select();
                            loadFileResources(folder.getDriveFolderId());
                        }

                        if (!folder.getSubfolders().isEmpty()) {
                            addSubFoldersToTree(folder, folderWidget);

                        }/* else if (!"all_files".equals(folder.getDriveFolderId())) {
                            folderItem.addTextItem(wfmStrings.loading());
                        }*/
                    }
                }
            }
        });

        addSelectButton();
    }

    public void addFoldersWrapperTo(Div div) {
        div.add(foldersWrap);
    }

    private void addSelectButton() {
        FormGroup group = new FormGroup(selectButton);
        group.addStyleName("mb-0");
        selectButton.addStyleName("mr-2");
        WfmButton2 close = new WfmButton2(wfmStrings.close(), (event) -> {
            StorageDocsSelectPopup.this.removeFromParent();
            uploadDialog.uploadState();
        });
        group.addToContent(close);
        uploadDialog.uploadWrapper.clear();
        uploadDialog.uploadWrapper.add(group);
    }

    private void loadSubfolders(final FolderWidget folderWidget) {
        final FolderResource folder = folderWidget.getFolder();
        DocumentsService.App.get().getGoogleSubFolders(folder.getDriveFolderId(), storageType, new AsyncCallback<FolderResource[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(FolderResource[] folderResources) {
//                folderWidget.clear();

                if (folderResources != null && folderResources.length > 0) {
                    folder.setSubfolders((ArrayList<FolderResource>) Arrays.asList(folderResources));
                    addSubFoldersToTree(folder, folderWidget);
                }
            }
        });
    }

    private void loadFileResources(final String folderId) {
//        table.add(dataLoading); //TODO restore
        dataLoading.getElement().getStyle().setMarginTop(50, Style.Unit.PCT);
        dataLoading.getElement().getStyle().setMarginLeft(20, Style.Unit.PCT);

        DocumentsService.App.get().getGoogleFiles(!"all_files".equals(folderId) ? folderId : null, storageType, new AsyncCallback<ArrayList<FileResource>>() {
            @Override
            public void onFailure(Throwable throwable) {
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<FileResource> fileResources) {
                clearTable();

                folderFiles.put(folderId, fileResources);

                if (!fileResources.isEmpty()) {
                    for (FileResource fileResource : fileResources) {
                        addFileToTable(fileResource);
                    }
                } else {
//                    checkBoxList.add(noFilesWidget); //TODO restore
                    noFilesWidget.getElement().getStyle().setMarginTop(90d, Style.Unit.PX);
                    noFilesWidget.getElement().getStyle().setMarginLeft(80d, Style.Unit.PX);
                }
            }
        });
    }

    private void addFileToTable(FileResource resource) {
        final CustomCheckBox checkBox = new CustomCheckBox(resource.getBodyId(), resource.getName());
        checkBox.setFileResource(resource);
        checkBox.addValueChangeHandler(booleanValueChangeEvent -> {
            FileResource resource1 = checkBox.getFileResource();

            if (checkBox.getValue()) {
                if (!selectedFiles.containsKey(resource1.getGoogleOrOffice365Id())) {
                    selectedFiles.put(resource1.getGoogleOrOffice365Id(), resource1);
                }
            } else {
                selectedFiles.remove(resource1.getGoogleOrOffice365Id());
            }
        });
        checkBoxes.put(resource.getGoogleOrOffice365Id(), checkBox);
        checkBoxList.add(checkBox);
    }

    private void addSubFoldersToTree(FolderResource folder, FolderWidget folderWidget) {
        for (FolderResource subFolder : folder.getSubfolders()) {
            FolderWidget folder_ = new FolderWidget(subFolder, null, !folder.getSubfolders().isEmpty());
            folderWidget.add(folder_);
            if (!folder.getSubfolders().isEmpty()) {
                addSubFoldersToTree(subFolder, folder_);
            }
        }
    }

    private void searchBox() {
        searchTextBox = new TextBox();
        final String searchDefaultText = wfmStrings.searchFile();
        searchTextBox.setTitle(searchDefaultText);
        searchTextBox.setPlaceHolder(searchDefaultText);
        searchTextBox.addStyleName("search-textbox");
        searchTextBox.addFocusHandler(event -> {
            if ((searchDefaultText).trim().equals(searchTextBox.getText().trim())) {
                searchTextBox.setText("");
                searchTextBox.removeStyleName("search-textbox");
            }
        });
        searchTextBox.addBlurHandler(event -> {
            if ("".equals(searchTextBox.getText())) {
                searchTextBox.setPlaceHolder(searchDefaultText);
                searchTextBox.addStyleName("search-textbox");
            }
        });
        searchTextBox.addKeyUpHandler(event -> {
            for (int i = 0; i < checkBoxList.getWidgetCount(); i++) {
                checkBoxList.getWidget(i).setVisible(false);
            }
            if (searchTextBox.getText() != null && !"".equals(searchTextBox.getText())) {
                for (int i = 0; i < checkBoxList.getWidgetCount(); i++) {
                    CustomCheckBox checkBox = checkBoxList.getWidget(i);
                    FileResource resource = checkBox.getFileResource();

                    if (resource.getName().toLowerCase().contains(searchTextBox.getText().toLowerCase())) {
                        checkBox.setVisible(true);
                    }
                }
            } else {
                for (int i = 0; i < checkBoxList.getWidgetCount(); i++) {
                    checkBoxList.getWidget(i).setVisible(true);
                }
            }
        });

    }

    private void clearTable() {
        checkBoxList.clear();
        checkBoxes.clear();
        selectedFiles.clear();
    }

    private Tree.Resources getTreeImageResources() {
        return new Tree.Resources() {
            public ImageResource treeClosed() {
                return DocumentImages.get().getTreeClosed();
            }

            public ImageResource treeLeaf() {
                return DocumentImages.get().groups();
            }

            public ImageResource treeOpen() {
                return DocumentImages.get().getTreeOpen();
            }
        };
    }
}
