package com.edatasite.workforce.gwt.documents.client;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
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
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: User
 * Date: 12.07.12
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public class DocumentsDocsSelectPopup extends Div implements Constants {

    private final int limit = 30;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final String searchDefaultText = wfmStrings.searchFile();
    private final TreeSelectIcons icons = GWT.create(TreeSelectIcons.class);
    private WfmButton2 selectButton;
    private FileUploadDialog uploadDialog;
    private TextBox searchTextBox;
    private FolderWidget previousFolder;
    private FolderWrap foldersWrap;
    private CheckBoxList checkBoxList;
    private Span mS;
    private Label fileCountLabel = new Label();
    private HashMap<String, FileResource> selectedFiles = new HashMap<>();
    private ArrayList<FileResource> selectedFilesList = new ArrayList<>();
    private HashMap<CustomCheckBox, FileResource> allFiles = new HashMap<>();
    private HashMap<Integer, CustomCheckBox> allFilesMap = new HashMap<>();
    private String folderType;
    private int index = 0;
    private int offset = 0;
    private int filesCount = 0;
    private static String FOLDER_ID = "folderId";
    private static String FOLDER_TYPE = "folderType";
    private static String SYSTEM_TYPE = "sysTemFolder";
    private static String ALL_FILE_TYPE = "allFiles";
    private static String MY_FOLDER_TYPE = "myFolder";
    private static String SHARED_WITH_ME_TYPE = "sharedWithMe";
    private static String SHARED_BY_ME_TYPE = "sharedByMe";


    public DocumentsDocsSelectPopup(FileUploadDialog uploadDialog) {
        super("kpi-upload__list");
        this.uploadDialog = uploadDialog;
        onInitialize();
    }

    public void onInitialize() {
        Div listHeader = new Div("kpi-upload__list-header");
        Div searchForm = new Div("searchForm");
        add(listHeader);
        listHeader.add(searchForm);
        searchBox();
        searchForm.add(searchTextBox);
//        MaterialWidget em = new MaterialWidget(Document.get().createElement("em"));
//        em.addStyleName("searchForm__btn");
//        em.addClickHandler(getSearchIconClickHandler());
//        searchForm.add(em);

        checkBoxList = new CheckBoxList();
        add(checkBoxList);
        Div more = new Div("kpi-upload__list-more");
        mS = new Span(wfmStrings.loadMore());
        more.add(mS);
        mS.addClickHandler(clickEvent -> {
            checkBoxList.setNoFileText(wfmStrings.noFilesInSelectedFolder());
            if (filesCount - offset >= 0) {
                offset += limit;
                FolderWidget selectedItem = foldersWrap.getSelectedItem();
                if (selectedItem != null) {
                    addFileToTable(selectedItem.getFolder(), selectedItem.getFolderType());
                }
//                if (filesCount - offset < limit) {
//                    mS.setVisible(false);
//                }
            }
        });
        add(more);
        foldersWrap = new FolderWrap(new FolderSelection() {
            @Override
            public void execute(FolderWidget folder) {
                searchTextBox.setText(searchDefaultText);
                if (previousFolder == null) {
                    previousFolder = folder;
                }
                final FolderWidget currentFolder = folder;
                if (currentFolder != null) {
                    offset = 0;
                    currentFolder.select();
                    if (!previousFolder.equals(currentFolder)) {
                        currentFolder.select();
                    }
                    FolderResource folderResource = currentFolder.getFolder();
                    addFileToTable(folderResource, currentFolder.getFolderType());
                    previousFolder = currentFolder;
                }
            }
        },
                new FolderOpen() {
                    @Override
                    public void execute(FolderWidget parent) {
                        FolderWidget item = parent;
                        item.select();
                        if (!previousFolder.equals(item)) {
                            previousFolder.deselect();
                        }
                        previousFolder = item;
                    }
                });


        fileCountLabel.addStyleName("mod_text--center");

        selectButton = new WfmButton2(wfmStrings.select(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (!selectedFiles.isEmpty()) {
                if (uploadDialog != null) {
                    for (FileResource resource : selectedFiles.values()) {
                        uploadDialog.putIntoKpiFiles(resource.getBodyId(), resource);
                        FileWidget fileWidget = new FileWidget(resource.getBodyId() + "", resource.getName());
                        Icon clearImage = new Icon();
                        clearImage.setStyleName(WfmButton2.ICON_TRASH);
                        clearImage.getElement().getStyle().setCursor(Style.Cursor.POINTER);
                        clearImage.getElement().getStyle().setMarginLeft(5d, Style.Unit.PX);
                        fileWidget.setRemoveCommand(() -> {
                            uploadDialog.removeFromKpiFiles(resource.getBodyId());
                            uploadDialog.removeFromUploadContent(fileWidget);
                        });
                        uploadDialog.addToUploadContent(fileWidget);
                        fileWidget.setCompleted(101);
                        uploadDialog.runCommand();
                    }
                    uploadDialog.setVisibleToGoogleDocsPanel();
                    DocumentsDocsSelectPopup.this.removeFromParent();
                    uploadDialog.uploadState();
                } else {
                    Collections.addAll(selectedFilesList, selectedFiles.values().toArray(new FileResource[]{}));
                }
            } else {
                Info.show(wfmStrings.pleaseSelectDocumentsToLink(), Info.Type.WARNING);
            }
//            close();
        });
        getDocumentFolders();
        addSelectButton();
    }

    private void addSelectButton() {
        FormGroup group = new FormGroup(selectButton);
        group.addStyleName("mb-0");
        selectButton.addStyleName("mr-2");
        WfmButton2 close = new WfmButton2(wfmStrings.close(), (event) -> {
            DocumentsDocsSelectPopup.this.removeFromParent();
            uploadDialog.uploadState();
        });
        group.addToContent(close);
        uploadDialog.uploadWrapper.add(group);
    }

    private ClickHandler getSearchIconClickHandler() {
        return (event -> {
            checkBoxList.setNoFileVisible(false);
            for (int i = 0; i < checkBoxList.getWidgetCount(); i++) {
                checkBoxList.getWidget(i).setVisible(false);
            }
            LoadingPanel.loading(true, checkBoxList.getSelf());
            FolderWidget selectedItem = foldersWrap.getSelectedItem();
            Integer folderID = selectedItem != null ? selectedItem.getFolder() != null ? selectedItem.getFolder().getObjectId() : null : null;
            DocumentsView.get().getDocumentsService().searchDocument(folderID, searchTextBox.getText(), new AbstractAsyncCallback<ArrayList<FileResource>>() {
                @Override
                public void failure(Throwable throwable) {
                    checkBoxList.setNoFileText(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
                    checkBoxList.setNoFileVisible(true);
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(ArrayList<FileResource> result) {
                    LoadingPanel.loading(false);
                    if (!result.isEmpty()) {
                        for (FileResource resource : result) {
                            if (!allFilesMap.containsKey(resource.getBodyId())) {
                                final CustomCheckBox checkBox = new CustomCheckBox(resource.getBodyId(), resource.getName());
                                checkBox.getElement().getStyle().setMarginLeft(3d, Style.Unit.PX);
                                String widgetId = "ch_" + index;
                                checkBox.getElement().setId(widgetId);
                                allFiles.put(checkBox, resource);
                                allFilesMap.put(resource.getBodyId(), checkBox);
                                checkBox.addValueChangeHandler(booleanValueChangeEvent -> {
                                    String id = checkBox.getElement().getId();
                                    if (checkBox.getValue()) {
                                        if (!selectedFiles.containsKey(id)) {
                                            selectedFiles.put(id, allFiles.get(checkBox));
                                        }
                                    } else {
                                        selectedFiles.remove(id);
                                    }
                                });
                                checkBoxList.add(checkBox);
                                index++;
                            } else {
                                allFilesMap.get(resource.getBodyId()).setVisible(true);
                            }
                        }
                    } else {
                        checkBoxList.setNoFileText(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
                        checkBoxList.setNoFileVisible(true);
                    }
                }
            });
        });
    }

    private void getDocumentFolders() {
        LoadingPanel.loading(true, foldersWrap);
        DocumentsService.App.get().getAllMainFolders(new AsyncCallback<HashMap<String, FolderResource>>() {
            @Override
            public void onFailure(Throwable throwable) {
                checkBoxList.setNoFileText(wfmStrings.noFilesInSelectedFolder());
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(HashMap<String, FolderResource> folderResources) {
                LoadingPanel.loading(false);
                FolderResource allFilesFolder = new FolderResource();
                allFilesFolder.setObjectId(0);
                allFilesFolder.setName(wfmStrings.allFiles());

                FolderWidget folderWidget = new FolderWidget(allFilesFolder, ALL_FILE_TYPE, false);

                foldersWrap.add(folderWidget);
                foldersWrap.setSelectedItem(allFilesFolder);
                previousFolder = folderWidget;


                for (Map.Entry<String, FolderResource> entrySet : folderResources.entrySet()) {
                    FolderResource folder = entrySet.getValue();
                    folderType = entrySet.getKey();
                    index++;
                    FolderWidget folderItem;
                    if (!folder.getSubfolders().isEmpty()) {
                        folderItem = new FolderWidget(folder, folderType, true);
                        addSubFoldersToTree(folder, folderItem);
                    } else {
                        folderItem = new FolderWidget(folder, folderType, false);
                    }
                    foldersWrap.add(folderItem);
                    FolderWidget subFolderItem;
                    if (!folder.getSubfolders().isEmpty()) {
                        subFolderItem = new FolderWidget(folder, folderType, true);
                        addSubFoldersToTree(folder, subFolderItem);
                    } else {
                        subFolderItem = new FolderWidget(folder, folderType, false);
                    }

                }

                addFileToTable(null, ALL_FILE_TYPE);
            }
        });
    }

    private void getSystemSubfolders(Integer id, final FolderWidget item) {
        DocumentsView.get().getDocumentsService().getSystemSubFolders(id, new AbstractAsyncCallback<ArrayList<FolderResource>>() {
            @Override
            public void success(ArrayList<FolderResource> result) {
                for (FolderResource folder : result) {
                    FolderWidget subFolderItem;
                    if (!folder.getSubfolders().isEmpty()) {
                        subFolderItem = new FolderWidget(folder, SYSTEM_TYPE, true);
                        addSubFoldersToTree(folder, subFolderItem);
                    } else {
                        subFolderItem = new FolderWidget(folder, SYSTEM_TYPE, false);
                    }
                    item.addFolder(subFolderItem);
                }
            }
        });
    }

    private void getShareWithMeSubfolders(Integer id, final FolderWidget item) {
        DocumentsView.get().getDocumentsService().getSubFolders(id, new AbstractAsyncCallback<FolderResource>() {
            @Override
            public void success(FolderResource result) {
                for (FolderResource folder : result.getFolders()) {
                    FolderWidget subFolderItem;
                    if (!folder.getSubfolders().isEmpty()) {
                        subFolderItem = new FolderWidget(folder, SHARED_WITH_ME_TYPE, true);
                        addSubFoldersToTree(folder, subFolderItem);
                    } else {
                        subFolderItem = new FolderWidget(folder, SHARED_WITH_ME_TYPE, false);
                    }
                    item.addFolder(subFolderItem);

                }
            }
        });
    }

    private void addFileToTable(FolderResource folder, String folderType) {

        ListingFilterParameter filterParametrs = new ListingFilterParameter();
        filterParametrs.setFolderId(null);
        filterParametrs.setLimit(limit);
        filterParametrs.setStart(offset);
        filterParametrs.setUserID(null);
        filterParametrs.setDeleted(false);
        filterParametrs.setTrashResource(false);
        filterParametrs.setSharedResource(false);
        filterParametrs.setOtherResource(false);
        filterParametrs.setAllFilesResource(false);
        filterParametrs.setOtherSharedResource(false);

        if (MY_FOLDER_TYPE.equals(folderType) || SHARED_WITH_ME_TYPE.equals(folderType) || SHARED_BY_ME_TYPE.equals(folderType)) {
            filterParametrs.setFolderId(folder.getObjectId());
            filterParametrs.setFolderName(folderType);
        } else if (SYSTEM_TYPE.equals(folderType)) {
            filterParametrs.setFolderId(folder.getObjectId());
            filterParametrs.setSystemSubFolder(true);
        } else if (ALL_FILE_TYPE.equals(folderType)) {
            filterParametrs.setAllFilesResource(true);
        }
        checkBoxList.setNoFileVisible(false);
        LoadingPanel.loading(true, checkBoxList.getSelf());
        DocumentsView.get().getDocumentsService().listFilesAndFoldersForPopup(filterParametrs, new AbstractAsyncCallback<ListResult<FileResource>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                checkBoxList.setNoFileVisible(false);
            }

            @Override
            public void success(ListResult<FileResource> resultList) {
                LoadingPanel.loading(false);
                List<FileResource> result = resultList.getList();
                filesCount = resultList.getTotal();
                if (filesCount == 0) {
                    fileCountLabel.setText("0-0 of 0");
                } else {
                    fileCountLabel.setText((offset + 1) + "-" + (result.size() == limit ? (offset + limit) : offset + result.size()) + " of " + filesCount);
                }
                if (filesCount - offset <= 0 || filesCount - offset <= limit) {
                    mS.setVisible(false);
                } else {
                    mS.setVisible(true);
                }
//                for (int i = 0; i < checkBoxList.getWidgetCount(); i++) {
                checkBoxList.clear();
//                }
                if (result != null && !result.isEmpty()) {
                    for (FileResource resource : result) {
                        final CustomCheckBox checkBox = new CustomCheckBox(resource.getBodyId(), resource.getName());
                        if (!allFilesMap.containsKey(resource.getBodyId())) {
                            checkBox.getElement().getStyle().setMarginLeft(3d, Style.Unit.PX);
                            String widgetId = "ch_" + index;
                            checkBox.getElement().setId(widgetId);
                            allFiles.put(checkBox, resource);
                            allFilesMap.put(resource.getBodyId(), checkBox);
                            checkBox.addValueChangeHandler(booleanValueChangeEvent -> {
                                String id1 = checkBox.getElement().getId();
                                if (checkBox.getValue()) {
                                    if (!selectedFiles.containsKey(id1)) {
                                        selectedFiles.put(id1, allFiles.get(checkBox));
                                    }
                                } else {
                                    selectedFiles.remove(id1);
                                }
                            });
                            checkBoxList.add(checkBox);
                            index++;
                        } else {
                            checkBoxList.add(checkBox);
                        }
                    }
                } else {
                    checkBoxList.setNoFileVisible(true);
                    checkBoxList.setNoFileText(wfmStrings.noFilesInSelectedFolder());
                }
            }
        });

    }

    private void addSubFoldersToTree(FolderResource folder, FolderWidget folderItem) {
        for (FolderResource subFolder : folder.getSubfolders()) {
            FolderWidget subFolderItem;
            index++;
            if (!subFolder.getSubfolders().isEmpty()) {
                subFolderItem = new FolderWidget(subFolder, folderType, true);
                addSubFoldersToTree(subFolder, subFolderItem);
            } else {
                subFolderItem = new FolderWidget(subFolder, folderType, false);
            }
            folderItem.addFolder(subFolderItem);
        }
    }

    private void searchBox() {
        searchTextBox = new TextBox();
        searchTextBox.setTitle(searchDefaultText);
        searchTextBox.setText(searchDefaultText);
        searchTextBox.addStyleName("search-textbox");
        searchTextBox.addFocusHandler(event -> {
            if ((searchDefaultText).trim().equals(searchTextBox.getText().trim())) {
                searchTextBox.setText("");
                searchTextBox.removeStyleName("search-textbox");
            }
        });
        searchTextBox.addBlurHandler(event -> {
            if ("".equals(searchTextBox.getText())) {
                searchTextBox.setText(searchDefaultText);
                searchTextBox.addStyleName("search-textbox");
            }
        });
        searchTextBox.addKeyUpHandler(event -> {
            boolean match = false;
            checkBoxList.setNoFileVisible(false);
            for (int i = 0; i < checkBoxList.getWidgetCount(); i++) {
                checkBoxList.getWidget(i).setVisible(false);
            }
            if (searchTextBox.getText() != null && !"".equals(searchTextBox.getText())) {
                for (CustomCheckBox checkBox : allFiles.keySet()) {
                    FileResource resource = allFiles.get(checkBox);
                    if (resource.getName().toLowerCase().contains(searchTextBox.getText().toLowerCase())) {
                        checkBox.setVisible(true);
                        match = true;
                    }
                }
            } else {
                for (CustomCheckBox checkBox : allFiles.keySet()) {
                    checkBox.setVisible(true);
                    match = true;
                }
            }
            checkBoxList.setNoFileVisible(!match);
        });


        MaterialPanel searchPanel = new MaterialPanel("wg_lang-select__search");
        searchPanel.add(searchTextBox);
    }

    public void addFoldersWrapperTo(Div div) {
        div.add(foldersWrap);
    }

}

