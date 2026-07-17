package com.edatasite.workforce.gwt.documents.client.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.enums.FileUploadType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.WestPanelHelp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelRowSelectionHandler;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ActionMenuItem;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextAreaCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.DeleteFolderCommand;
import com.edatasite.workforce.gwt.documents.client.DocUtils;
import com.edatasite.workforce.gwt.documents.client.DocumentImages;
import com.edatasite.workforce.gwt.documents.client.Folders;
import com.edatasite.workforce.gwt.documents.client.Groups;
import com.edatasite.workforce.gwt.documents.client.clipboard.Clipboard;
import com.edatasite.workforce.gwt.documents.client.commands.CopyCommand;
import com.edatasite.workforce.gwt.documents.client.commands.CutCommand;
import com.edatasite.workforce.gwt.documents.client.commands.DeleteCommand;
import com.edatasite.workforce.gwt.documents.client.commands.EmptyTrashCommand;
import com.edatasite.workforce.gwt.documents.client.commands.NewFolderCommand;
import com.edatasite.workforce.gwt.documents.client.commands.PasteCommand;
import com.edatasite.workforce.gwt.documents.client.commands.PropertiesCommand;
import com.edatasite.workforce.gwt.documents.client.commands.RenameFolderCommand;
import com.edatasite.workforce.gwt.documents.client.commands.RestoreTrashCommand;
import com.edatasite.workforce.gwt.documents.client.commands.ToTrashCommand;
import com.edatasite.workforce.gwt.documents.client.commands.UploadFileCommand;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.TrashResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsServiceAsync;
import com.edatasite.workforce.gwt.documents.client.table.FileActionMenu;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Icon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 14.05.2010
 * Time: 20:04:44
 */
public class DocumentsView extends View implements Constants, FittedContent {
    private ListingPanel<FileResource> docListPanel;
    private boolean isAnyFolderClicked = false;

    private String nickDebugId = "documents_viw_";

    private WfmButton2 reIndexButton;
    private Boolean enableReIndex = false;
    private FlowPanel wc = new FlowPanel();
    private final VerticalPanel vp = new VerticalPanel();

    public DocumentsView() {
        super("documentadd", wfmStrings.documents());
    }

    public DocumentsView(String name, String description) {
        super(name, description);
    }

    public DocumentsView(Integer folderId) {
        this();
        this.folderId = folderId;
    }

    public DocumentsView(int folderType, Integer entityId) {
        this();
        this.folderType = folderType;
        this.entityId = entityId;
    }

    public DocumentsView(String name, String folderName, Integer folderID) {
        this(name, folderName);
        this.folderId = folderID;
    }

    public DocumentsView(int folderType, Integer entityId, Integer folderId) {
        this();
        this.folderType = folderType;
        this.entityId = entityId;
        this.folderId = folderId;
        this.rootFolderId = folderId;
    }

    public DocumentsView(int folderType, Integer entityId, boolean isSystemItem, boolean isLoadAttachments) {
        this();
        this.folderType = folderType;
        this.entityId = entityId;
        this.isSystemItem = isSystemItem;
        this.isLoadAttachments = isLoadAttachments;
    }

    public DocumentsView(int folderType, Integer entityId, Integer folderId, boolean isSystemItem) {
        this();
        this.folderType = folderType;
        this.entityId = entityId;
        this.folderId = folderId;
        this.rootFolderId = folderId;
        this.isSystemItem = isSystemItem;
    }

    private DocumentsServiceAsync documentsService = DocumentsService.App.get();

    private boolean selectfolderbyvew = false;

    public boolean isSelectfolderbyvew() {
        return selectfolderbyvew;
    }

    public void setSelectfolderbyvew(boolean selectfolderbyvew) {
        this.selectfolderbyvew = selectfolderbyvew;
    }

    /**
     * A constant that denotes the completion of an IncrementalCommand.
     */
    public static final boolean DONE = false;

    private final DocumentImages.Images images = DocumentImages.get();
    /**
     * The panel that contains the various system messages.
     */

    /**
     * The single Documents instance.
     */
    private static DocumentsView singleton;

    /**
     * Gets the singleton Documents instance.
     *
     * @return the Documents object
     */
    public static DocumentsView get() {
        if (DocumentsView.singleton == null) {
            DocumentsView.singleton = new DocumentsView();
        }
        return DocumentsView.singleton;
    }

    public static void setSingleton(DocumentsView documentsView) {
        DocumentsView.singleton = documentsView;
    }

    /**
     * The Application Clipboard implementation;
     */
    private Clipboard clipboard = new Clipboard();

    private UserResource currentUserResource;

    /**
     * The group list widget.
     */
    private Groups groups;

    /**
     *
     */
    private KpiModal groupBox;

    /**
     * The widget that displays the tree of folders.
     */
    private Folders folders;

    /**
     * The currently selected item in the application, for use by the Edit menu
     * commands. Potential types are Folder, File, User and Group.
     */
    private Object currentSelection;

    private ActionButton uploadFile;
    /**
     * Upload File max size
     */
    private Integer maxSize;
    private Double companyUsedStorage;
    private double convertToMB = 1024;
    private Double companyMaxStorage = 10d;

    private Integer folderId;

    private Integer rootFolderId;
    private Integer folderType;
    private Integer entityId;
    private boolean isSystemItem = false;
    private boolean isLoadAttachments = false;

    private boolean isFileListShowing = false;

    private FolderResource folderResource;


    protected Widget onInitialize() {

        initializeWfpParams();

        if ((SinksContainerFactory.entryPoint.moduleSetting.isCustomise()) && folderId == null) {
            documentsService.getRootFolderID(new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void success(Integer rootFolderId) {
                    folderId = rootFolderId;
                    initInternal();
                }
            });
        } else if (entityId != null && folderType != null && folderId == null) {
            documentsService.getFolderID(folderType, entityId, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void success(Integer result) {
                    rootFolderId = result;
                    folderId = result;
                    initInternal();
                }
            });
        } else {
            initInternal();
        }

        //reload listing after doc. upload
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DOCUMENTS_UPLOAD_FILES, DocumentsView.this, (sender, args) -> docListPanel.reloadPage());
        return null;
    }

    private void initInternal() {

        docListPanel = new ListingPanel<>(ListPanelType.DocumentListPanel, getColumnConfig(), getRequestProvider(), getPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);

        docListPanel.addSelectionRowHandler(new ListingPanelRowSelectionHandler<FileResource>() {
            @Override
            public void onSelectedRows(HashSet<FileResource> selectedRows) {
                if (selectedRows.size() == 1) {
                    setCurrentSelection(selectedRows.iterator().next());
                } else if (selectedRows.size() > 1) {
                    setCurrentSelection(new ArrayList<>(selectedRows));
                } else {
                    //If none of the rows selected then set current parent folder as selected
                    if (docListPanel.getPagingScrollTable().getSelectedRowValues().isEmpty()) {
                        DnDTreeItem parentFolder = folders.getCurrentTreeItem();
                        if (parentFolder != null && parentFolder.getFolderResource() != null) {
                            folders.setCurrent(parentFolder);
                            setCurrentSelection(parentFolder.getFolderResource());
                        }
                    }
                }
            }
        });


        // Initialize the singleton before calling the constructors of the
        // various widgets that might call Documents.get().
        singleton = this;
        if (!isLoadAttachments) {
            folders = new Folders(folderId, isSystemItem);
            folders.clearSelection();
            if (folderType != null && entityId != null) {
                folders.setVisible(false);
            }
        }

        groups = new Groups();

        groupBox = new KpiModal();
        groupBox.drawCloseBtn(event -> {
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DOCUMENT_GROUPS_POPUP_CLOSED, null, null);
            groupBox.close();
        });
        groupBox.setWidth("650px");
        groupBox.add(groups);
        groupBox.addButton(groups.drawAndGetAddGroupButton());
        groupBox.ensureDebugId(nickDebugId + "groupBox");
        reIndexButton = new WfmButton2("Re Index");

        getDocumentsService().getCurrentUserResource(new AbstractAsyncCallback<UserResource>() {
            @Override
            public void success(UserResource user) {
                if (user != null) {
                    setCurrentUserResource(user);
                }
            }
        });

        getDocumentsService().getCompanyFileUploadMaxSize(new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable caught) {
                maxSize = null;
            }

            @Override
            public void success(Integer result) {
                maxSize = result;
            }
        });

        documentsService.isIndexedUploadDocument(new AbstractAsyncCallback<Boolean>() {
            @Override
            public void success(Boolean result) {
                enableReIndex = result;
                //enableReIndex
                getMessage();
            }
        });

        reIndexButton.addClickHandler(sender -> documentsService.copyUploadDocumentSize(1, new AbstractAsyncCallback<String>() {

            @Override
            public void failure(Throwable caught) {
                GWT.log("reIndexButton.Click = ", caught);
            }

            @Override
            public void success(String result) {
                reIndexButton.setText("Indexed");
            }
        }));

        add(docListPanel);
    }

    private void initializeWfpParams() {
        String strFolderID = getModuleParam("folderId");
        if (strFolderID != null && !strFolderID.isEmpty()) {
            this.folderId = Integer.valueOf(strFolderID);
        }
    }

    /**
     * <i>... Document View List Panel Design ...</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Create Date {16:18 18/07/2011} ...</i>
     *
     * @return
     */
    private ListingPanelDesign getPanelDesign() {

        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                uploadFile = getAddNewButton(ActionButton.Type.TOOLMENU);
                uploadFile.addClickHandler(event -> {
                    if (isLoadAttachments) {
                        documentsService.getFolderResource(folderId, new AbstractAsyncCallback<FolderResource>() {
                            @Override
                            public void failure(Throwable throwable) {
                                GWT.log("Get Folder", throwable);
                            }

                            @Override
                            public void success(FolderResource result) {
                                if (entityId != null) {
                                    result.setEntityId(entityId);
                                }
                                new UploadFileMenus(images, result);
                            }

                        });
                    } else {
                        new UploadFileMenus(images, null);
                    }
                });
                return uploadFile;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                final ActionButton more = new ActionButton(ActionButton.getMoreString(), ActionButton.Type.TOOLMENU);
                more.ensureDebugId("documents_more_button");
                //add new folder menu item
                more.addClickHandler(event -> {

                    //First of all if none of the rows selected then set current parent folder as selected
                    if (docListPanel.getPagingScrollTable().getSelectedRowValues().isEmpty()) {
                        DnDTreeItem parentFolder = folders.getCurrentTreeItem();
                        if (parentFolder != null && parentFolder.getFolderResource() != null) {
                            folders.setCurrent(parentFolder);
                            setCurrentSelection(parentFolder.getFolderResource());
                        }
                    }

                    ActionsMenus actionsMenus = new ActionsMenus(images);

                    if (!SinksContainerFactory.entryPoint.moduleSetting.isCustomise() && !isLoadAttachments) {
                        actionsMenus.showActionMenu(more.getAbsoluteLeft(), more.getAbsoluteTop() + 28);
                    }
                });
                if ((!isSystemItem || !isLoadAttachments) && (!SinksContainerFactory.entryPoint.moduleSetting.isCustomise() || Utils.hasRole(CLIENT))) {
                    return more;
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                if ((SinksContainerFactory.entryPoint.moduleSetting.isCustomise()) && !Utils.hasRole(CLIENT)) {

                    // upload file
                    final ListingActionMenu actionMenu = new ListingActionMenu();

                    DocumentsService.App.get().getEnableUploadTypes(new AbstractAsyncCallback<HashMap<String, Boolean>>() {
                        @Override
                        public void failure(Throwable throwable) {
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(HashMap<String, Boolean> result) {
                            createStorageItems(actionMenu, result);
                        }
                    });
                    // my google doc
                    actionMenu.addMenuItem("groups", new ActionMenuItem<FileResource>(wfmStrings.groups(), false) {
                        @Override
                        public boolean isDefaultShow() {
                            return true;
                        }

                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            return false;
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> {
                                groups.setVisible(true);
                                groups.updateCurrentlyShowingStats();
                                groupBox.center();
                            };
                        }
                    });

                    // rename doc file name
                    actionMenu.addMenuItem("renameFile", new ActionMenuItem<FileResource>(wfmStrings.renameFile(), false) {
                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canRename();
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new RenameFolderCommand(null).execute();
                        }
                    });

                    // rename doc file name
                    actionMenu.addMenuItem("copyFile", new ActionMenuItem<FileResource>(wfmStrings.copyFile(), false) {
                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canCopy();
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new CopyCommand(null).execute();
                        }
                    });

                    // rename doc file name
                    actionMenu.addMenuItem("cutFile", new ActionMenuItem<FileResource>(wfmStrings.cutFile(), false) {
                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canDelete();
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new CutCommand(null).execute();
                        }
                    });

                    // rename doc file name
                    actionMenu.addMenuItem("pasteFile", new ActionMenuItem<FileResource>(wfmStrings.pasteFile(), false) {
                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canPaste();
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new PasteCommand(null).execute();
                        }
                    });

                    // share doc
                    actionMenu.addMenuItem("shareFile", new ActionMenuItem<FileResource>(wfmStrings.shareFile(), false) {
                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canShare();
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new PropertiesCommand(null, 1).execute();
                        }
                    });

                    actionMenu.addMenuItem("moveToTrasheFile", new ActionMenuItem<FileResource>(wfmStrings.moveToTrash() + " " + wfmStrings.file(), false) {
                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rFowValues) {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canDelete();
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new ToTrashCommand(null).execute();
                        }
                    });

                    // download doc file
                    actionMenu.addMenuItem("downloadFile", new ActionMenuItem<FileResource>(wfmStrings.download(), false) {
                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            return true;
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            final FileResource item = getSingleSelection(rowValues);
                            return () -> {
                                String link = item.getDownloadUrl();
                                Window.open(link, "_blank", "");
                            };
                        }
                    });

                    // delete doc
                    actionMenu.addMenuItem("deleteFile", new ActionMenuItem<FileResource>(wfmStrings.delete(), false) {
                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && getCurrentSelection() instanceof FileResource && docUtils.canDeleteFile();
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new DeleteCommand(null).execute();
                        }
                    });

                    // propirties doc
                    actionMenu.addMenuItem("propirtiesFile", new ActionMenuItem<FileResource>(wfmStrings.properties(), false) {
                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            FileResource fileResource = getSingleSelection(rowValues);
                            return !fileResource.isDeleted();
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new PropertiesCommand(null, 0).execute();
                        }
                    });

                    // rename doc file name
                    actionMenu.addMenuItem("renameFolder", new ActionMenuItem<FileResource>("Rename Folder", false) {
                        @Override
                        public boolean isDefaultShow() {
                            return true;
                        }

                        @Override
                        public boolean isDefaultShowCriteria() {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canRename() && !(getCurrentSelection() instanceof FileResource);
                        }

                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            return false;
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new RenameFolderCommand(null).execute();
                        }
                    });

                    // rename doc file name
                    actionMenu.addMenuItem("copyFolder", new ActionMenuItem<FileResource>(wfmStrings.copyFolder(), false) {
                        @Override
                        public boolean isDefaultShow() {
                            return true;
                        }

                        @Override
                        public boolean isDefaultShowCriteria() {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canCopy() && !(getCurrentSelection() instanceof FileResource);
                        }

                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            return false;
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new CopyCommand(null).execute();
                        }
                    });

                    // rename doc file name
                    actionMenu.addMenuItem("cutFolder", new ActionMenuItem<FileResource>(wfmStrings.cut() + " " + wfmStrings.folder(), false) {
                        @Override
                        public boolean isDefaultShow() {
                            return true;
                        }

                        @Override
                        public boolean isDefaultShowCriteria() {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canDelete() && !(getCurrentSelection() instanceof FileResource);
                        }

                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            return false;
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new CutCommand(null).execute();
                        }
                    });
                    // paste doc Folder
                    actionMenu.addMenuItem("pasteFolder", new ActionMenuItem<FileResource>(wfmStrings.paste() + " " + wfmStrings.folder(), false) {
                        @Override
                        public boolean isDefaultShow() {
                            return true;
                        }

                        @Override
                        public boolean isDefaultShowCriteria() {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canPaste() && !(getCurrentSelection() instanceof FileResource);
                        }

                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canPaste();
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new PasteCommand(null).execute();
                        }
                    });
                    // share doc folder
                    actionMenu.addMenuItem("shareFolder", new ActionMenuItem<FileResource>(wfmStrings.share() + " " + wfmStrings.folder(), false) {
                        @Override
                        public boolean isDefaultShow() {
                            return true;
                        }

                        @Override
                        public boolean isDefaultShowCriteria() {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canShare() && !(getCurrentSelection() instanceof FileResource);
                        }

                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            return false;
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new PropertiesCommand(null, 1).execute();
                        }
                    });

                    actionMenu.addMenuItem("moveToTrashe", new ActionMenuItem<FileResource>(wfmStrings.moveToTrash() + " " + wfmStrings.folder(), false) {
                        @Override
                        public boolean isDefaultShow() {
                            return true;
                        }

                        @Override
                        public boolean isDefaultShowCriteria() {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canDelete() && !(getCurrentSelection() instanceof FileResource);
                        }

                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            return false;
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new ToTrashCommand(null).execute();
                        }
                    });

                    // delete doc
                    actionMenu.addMenuItem("deleteFolder", new ActionMenuItem<FileResource>("Delete Folder", false) {
                        @Override
                        public boolean isDefaultShow() {
                            return true;
                        }

                        @Override
                        public boolean isDefaultShowCriteria() {
                            DocUtils docUtils = new DocUtils(DocumentsView.this);
                            return docUtils.hasCurrenSelection() && docUtils.canDelete() && !(getCurrentSelection() instanceof FileResource);
                        }

                        @Override
                        public String getIconStyle() {
                            return "doc documents";
                        }

                        @Override
                        public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                            return false;
                        }

                        @Override
                        public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                            return () -> new DeleteFolderCommand(null).execute();
                        }
                    });

                    return actionMenu;
                } else {
                    return null;
                }
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noResultsFoundForTheProvidedSearchCriteria());
                emptyDataTable.initEmptyDataTable(message);
            }

        };
    }

    private void createStorageItems(ListingActionMenu actionMenu, HashMap<String, Boolean> result) {
        if (result.get(AMAZON) != null && result.get(AMAZON)) {
            // upload file
            actionMenu.addMenuItem("uploadFile", new ActionMenuItem<FileResource>(wfmStrings.uploadFile(), false) {
                @Override
                public boolean isDefaultShow() {
                    return true;
                }

                @Override
                public String getIconStyle() {
                    return "doc documents";
                }

                @Override
                public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                    DocUtils docUtils = new DocUtils(DocumentsView.this);
                    return docUtils.hasCurrenSelection() && !docUtils.isOthersShare() && docUtils.canUpload();
                }

                @Override
                public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                    return () -> new UploadFileCommand(null, FileUploadType.AMAZON).execute();
                }
            });
        }
        //if (!Utils.isMediaCom()) {
        // my google doc
        if (result.get(GOOGLE) != null && result.get(GOOGLE)) {
            actionMenu.addMenuItem("uploadGoogle", new ActionMenuItem<FileResource>(wfmStrings.uploadToMyGoogleDocs(), false) {
                @Override
                public boolean isDefaultShow() {
                    return true;
                }

                @Override
                public String getIconStyle() {
                    return "doc documents";
                }

                @Override
                public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                    DocUtils docUtils = new DocUtils(DocumentsView.this);
                    return docUtils.hasCurrenSelection() && !docUtils.isOthersShare() && docUtils.canUpload();
                }

                @Override
                public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                    return () -> new UploadFileCommand(null, FileUploadType.GOOGLE_DOCUMENTS).execute();
                }
            });
        }
        // my office doc
        if (result.get(OFFICE_365) != null && result.get(OFFICE_365)) {
            actionMenu.addMenuItem("uploadOffice", new ActionMenuItem<FileResource>(wfmStrings.uploadToOfficeDocuments(), false) {
                @Override
                public boolean isDefaultShow() {
                    return true;
                }

                @Override
                public String getIconStyle() {
                    return "doc documents";
                }

                @Override
                public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                    DocUtils docUtils = new DocUtils(DocumentsView.this);
                    return docUtils.hasCurrenSelection() && !docUtils.isOthersShare() && docUtils.canUpload();
                }

                @Override
                public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                    return () -> new UploadFileCommand(null, FileUploadType.OFFICE_DOCUMENTS).execute();
                }
            });
        }
        if (result.get(MINIO) != null && result.get(MINIO)) {
            // upload file
            actionMenu.addMenuItem("uploadFileToMinIO", new ActionMenuItem<FileResource>(wfmStrings.uploadFile(), false) {
                @Override
                public boolean isDefaultShow() {
                    return true;
                }

                @Override
                public String getIconStyle() {
                    return "doc documents";
                }

                @Override
                public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                    DocUtils docUtils = new DocUtils(DocumentsView.this);
                    return docUtils.hasCurrenSelection() && !docUtils.isOthersShare() && docUtils.canUpload();
                }

                @Override
                public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                    return () -> new UploadFileCommand(null, FileUploadType.MINIO).execute();
                }
            });
        }
        if (result.get(LOCAL) != null && result.get(LOCAL)) {
            // upload file
            actionMenu.addMenuItem("uploadFileToLocal", new ActionMenuItem<FileResource>(wfmStrings.uploadFile(), false) {
                @Override
                public boolean isDefaultShow() {
                    return true;
                }

                @Override
                public String getIconStyle() {
                    return "doc documents";
                }

                @Override
                public boolean isShowMenuItem(HashSet<FileResource> rowValues) {
                    DocUtils docUtils = new DocUtils(DocumentsView.this);
                    return docUtils.hasCurrenSelection() && !docUtils.isOthersShare() && docUtils.canUpload();
                }

                @Override
                public Command onClickMenuItem(HashSet<FileResource> rowValues) {
                    return () -> new UploadFileCommand(null, FileUploadType.LOCAL).execute();
                }
            });
        }
    }

    /**
     * <i>... Document View Request Provider ...</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Create Date {16:18 18/07/2011} ...</i>
     *
     * @return - requestProvider
     */
    private ListingRequestProvider<FileResource> getRequestProvider() {

        return new ListingRequestProvider<FileResource>() {
            @Override
            public void getRequest(ListingFilterParameter filterParametrs, ListingCallback<FileResource> callback) {

                boolean isAllFiles = false;
                if (get().getFolders() != null && get().getFolders().getCurrent() != null && ((DnDTreeItem) get().getFolders().getCurrent()).getAllFilesResource() != null) {
                    isAllFiles = true;
                }
                if (filterParametrs.isTrashResource() || filterParametrs.isSharedResource() || filterParametrs.isOtherSharedResource() || filterParametrs.isOtherResource()) {
                    isAnyFolderClicked = true;
                }

                if (folderId != null || isAnyFolderClicked || isAllFiles) {
                    filterParametrs.setFolderId(folderId);
                    if (isAllFiles) {
                        filterParametrs.setAllFilesResource(true);
                    }
                    isAnyFolderClicked = false;
                    if (isLoadAttachments) {
                        filterParametrs.setCrmEntityId(entityId);
                        filterParametrs.setSystemSubFolder(isLoadAttachments);
                    }
                    AbstractAsyncCallback<ListResult<FileResource>> asyncCallback = new AbstractAsyncCallback<ListResult<FileResource>>() {
                        @Override
                        public void failure(Throwable throwable) {
                            callback.onFailure(throwable);
                            isFileListShowing = false;
                        }

                        @Override
                        public void success(ListResult<FileResource> result) {
                            callback.onSuccess(result);
                            isFileListShowing = false;
                        }
                    };

                    DocumentsView.get().getDocumentsService().listFilesAndFolders(filterParametrs, asyncCallback);
                } else {
                    docListPanel.loading(false);
                }
            }
        };
    }

    /**
     * <i>... Document View Listing table columns ...</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Create Date {16:18 18/07/2011} ...</i>
     *
     * @return - columns
     */
    private ColumnDefinitionConfig[] getColumnConfig() {
        //boolean isShowActionColumn = true;

        //int _length = 9;
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[9];
        int col = 0;
        // action column
        //if (isShowActionColumn) {
        columns[col] = new ColumnDefinitionConfig<FileResource, FlowPanel>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public FlowPanel getCellValue(final FileResource item) {
                FlowPanel panel = new FlowPanel();
                if (item.isBackFolder()) {
                    return panel;
                }
                panel.setStyleName("marginLeft2");
                final Anchor arrow = new Anchor();
                arrow.setStyleName("action-listing ficon--more-horiz");
                arrow.addClickHandler(event -> {
                    //If Particular Item action were selected make sure selectionall is deselected
                    deselectAllRows();

                    if (item.isFolder()) {
                        if (folders.getCurrent() == null && folders.getCurrentTreeItem() != null) {
                            folders.setCurrent(folders.getCurrentTreeItem().getParentItem() != null ? folders.getCurrentTreeItem().getParentItem() : folders.getCurrentTreeItem());
                        }
                        folders.setCurrentFolder(item.getFolderResource(), arrow);
                    } else {
                        new FileActionMenu(item).showPopup(arrow.getAbsoluteLeft() + 15, arrow.getAbsoluteTop() + 2);
                    }
                });
                panel.add(arrow);
                return panel;
            }
        };

        columns[col].setColumnSortable(false);
        columns[col].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[col++].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        //}
        // doc name column
        columns[col] = new ColumnDefinitionConfig<FileResource, MaterialLink>(wfmStrings.name(), FileResource.NAME, 120) {
            @Override
            public MaterialLink getCellValue(final FileResource item) {
                Icon icon = getFileIcon(item);
                MaterialLink nameLink = new MaterialLink();
                nameLink.add(icon);
                nameLink.setText(item.getName());
                nameLink.setStyleName("elm_document-name");
                if (!item.isFolder()) {
                    nameLink.addClickHandler(event -> {
                        if (item.getUploadType() != null && item.getUploadType().equals(LOCAL) && item.getObjectId() == -1) { // item.getObjectID() == -1 <=====> THIS IS LOCAL FILE FROM HARD DRIVE DISC
                            String action = "/common/downloadLocalFile?file=" + item.getDescription();
                            Window.open(action, "_blank", "");
                        } else {
                            Utils.showImageOrDownloadFile(item, false);
                        }
                    });
                } else {
                    nameLink.addClickHandler(clickEvent -> {
                        if (item.getFolderResource() != null) {
                            folders.setCurrentTreeItem(item.getFolderResource(), true, item.isBackFolder());
                        }

                    });
                }

                return nameLink;
            }
        };
        columns[col++].setMinimumColumnWidth(100);
        // doc owner column
        columns[col] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.owner(), FileResource.OWNER, 100) {
            @Override
            public String getCellValue(FileResource item) {
                return (item.getOwner() != null) ? item.getOwner().getName() : "";
            }
        };
        columns[col++].setMinimumColumnWidth(90);
        // doc createby column
        columns[col] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.createdBy(), FileResource.CREATEBY, 100) {
            @Override
            public String getCellValue(FileResource item) {
                return item.getCreatedBy();
            }
        };
        columns[col++].setMinimumColumnWidth(80);
        // doc path column
        columns[col] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.path(), FileResource.PATH, 120) {
            @Override
            public String getCellValue(FileResource item) {
                return item.getPath();
            }
        };
        columns[col++].setMinimumColumnWidth(100);
        // doc size column
        columns[col] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.sizeField(), FileResource.SIZE, 80) {
            @Override
            public String getCellValue(FileResource item) {
                return item.getContentLength() != null ? item.getFileSizeAsString() : null;
            }
        };
        columns[col++].setMinimumColumnWidth(60);
        // doc date column
        columns[col] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.date(), FileResource.DATE, 90) {
            @Override
            public String getCellValue(FileResource item) {
                return (item.getModificationDate() != null) ? DateUtils.formatInternal(item.getModificationDate()) : "";
            }
        };
        columns[col++].setMinimumColumnWidth(80);
        // doc type column
        columns[col] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.type(), FileResource.TYPE, 130) {
            @Override
            public String getCellValue(FileResource item) {
                return (item.isFolder()) ? wfmStrings.folder() : DocUtils.getFileType(item.getContentType());
            }
        };
        columns[col++].setMinimumColumnWidth(120);
        //doc description

        final TextAreaCellEditor<String> descriptionCellEditor = new TextAreaCellEditor<String>(80) {
            @Override
            protected String getValue() {
                return getText();
            }

            @Override
            protected void setValue(String cellValue) {
                setText(cellValue);
            }
        };
        columns[col] = new ColumnDefinitionConfig<FileResource, String>(wfmStrings.description(), FileResource.DESCRIPTION, 130) {
            @Override
            public String getCellValue(FileResource item) {
                return (item.getDescription() == null) ? "" : item.getDescription();
            }

            @Override
            public void setCellValue(FileResource rowValue, String cellValue) {
                if (!rowValue.isFolder()) {
                    rowValue.setDescription(cellValue);
                    saveCellValue(rowValue);
                }
            }
        };
        columns[col].setCellEditor(descriptionCellEditor);
        columns[col].setCellChangesSave(new CellChange<FileResource>() {
            @Override
            public void saveCell(FileResource rowValue, String columnCodeName) {
                DocumentsService.App.get().saveFileDescription(rowValue.getBodyId(), rowValue.getDescription(), new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                    }

                    @Override
                    public void success(Void result) {
                    }
                });

                rowValue.getDescription();
            }
        });
        columns[col].setShow(false);
        columns[col].setMinimumColumnWidth(120);

        return columns;
    }

    public void deselectAllRows() {
        if (docListPanel != null) {
            docListPanel.getPagingScrollTable().getDataTable().deselectAllRows();
            docListPanel.getPagingScrollTable().getHeaderCheckBox().setValue(false);
        }
    }

    /**
     * Creates an HTML fragment that places an image & caption together, for use
     * in a group header.
     *
     * @param imageProto an image prototype for an image
     * @param caption    the group caption
     * @return the header HTML fragment
     */
    private String createHeaderHTML(AbstractImagePrototype imageProto, String caption) {
        return "<table class='caption' cellpadding='0' " + "cellspacing='0'>" + "<tr><td class='lcaption'>" +
                imageProto.getHTML() + "</td><td class='rcaption'><b style='white-space:nowrap'>&nbsp;" + caption + "</b></td></tr></table>";
    }

    public boolean isFileListShowing() {
        return isFileListShowing;
    }

    public void setFileListShowing(boolean fileListShowing) {
        isFileListShowing = fileListShowing;
    }

    /**
     * Make the user list visible.
     */
    public void showUserList() {
        docListPanel.reloadPage();
    }

    /**
     * Make the file list visible.
     */
    public void showFileList() {
        updateFileCache(true, true);
    }

    /**
     * Make the file list visible.
     *
     * @param reload
     */
    public void showFileList(boolean reload) {
        if (folders != null && folders.getCurrent() != null && folders.getCurrent().getUserObject() != null) {
            Object cachedObject = folders.getCurrent().getUserObject();
            if (cachedObject instanceof FolderResource) {
                FolderResource folder = (FolderResource) cachedObject;
            } else if (cachedObject instanceof TrashResource) {
                TrashResource folder = (TrashResource) cachedObject;
            }

        }
        if (reload) {
            docListPanel.reloadPage();
        }
    }

    public void refresh() {
        TreeItem selectedItem = DocumentsView.get().getFolders().getCurrent();
        updateFileCache(true, false);
        folders.clearSelection();
        folders.select(selectedItem);
    }

    /**
     * <i>... This update listing panel by selected tree type ...</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Create Date {20:28 18/07/2011} ...</i>
     *
     * @param clearSelection       - clearSelection
     * @param updateSelectedFolder - updateSelectedFolder
     */
    public void updateFileCache(boolean updateSelectedFolder, boolean clearSelection) {
        ListingFilterParameter filterP = docListPanel.getFilterParametrs();
        filterP.setFolderId(null);
        filterP.setUserID(null);
        filterP.setDeleted(false);
        filterP.setTrashResource(false);
        filterP.setSharedResource(false);
        filterP.setOtherResource(false);
        filterP.setAllFilesResource(false);
        filterP.setOtherSharedResource(false);
        filterP.setRootID(rootFolderId);
        if (isLoadAttachments) {
            filterP.setFolderId(folderId);
        } else {
            folderId = null;
            if (get().getFolders() != null && get().getFolders().getCurrent() != null && get().getFolders().isTrashItem(get().getFolders().getCurrent()) && !get().getFolders().getTrashItem().equals(get().getFolders().getCurrent())) {
                final DnDTreeItem folderItem = (DnDTreeItem) get().getFolders().getCurrent();
                filterP.setFolderId(folderItem.getFolderResource().getObjectId());
                folderId = folderItem.getFolderResource().getObjectId();
                filterP.setDeleted(true);
            } else if (get().getFolders() != null && get().getFolders().getCurrent() != null) {
                final DnDTreeItem folderItem = (DnDTreeItem) get().getFolders().getCurrent();
                if (folderItem.getFolderResource() != null) {
                    filterP.setFolderId(folderItem.getFolderResource().getObjectId());
                    folderId = folderItem.getFolderResource().getObjectId();
                } else if (folderItem.getTrashResource() != null) {
                    filterP.setTrashResource(true);
                    isAnyFolderClicked = true;
                } else if (folderItem.getSharedResource() != null) {
                    filterP.setSharedResource(true);
                    isAnyFolderClicked = true;
                } else if (folderItem.getOtherUserResource() != null) {
                    filterP.setOtherSharedResource(true);
                    isAnyFolderClicked = true;
                    filterP.setUserID(folderItem.getOtherUserResource().getObjectId());
                } else if (folderItem.getOthersResource() != null) {
                    filterP.setOtherResource(true);
                    isAnyFolderClicked = true;
                } else if (folderItem.getSystemResource() != null) {
                    filterP.setFolderId(folderItem.getSystemResource().getObjectId());
                    folderId = folderItem.getSystemResource().getObjectId();
                } else if (folderItem.getAllFilesResource() != null) {  //get all files
                    filterP.setAllFilesResource(true);
                    isAnyFolderClicked = true;
                }
            }
        }

        if (updateSelectedFolder) {
            if (folders != null) {
                folders.setCurrentTreeItem((DnDTreeItem) folders.getCurrent());
            }
            docListPanel.getFilterParametrs().setStart(0);
            docListPanel.getFilterParametrs().setLimit(10);
            docListPanel.gotoPageing(false);

            docListPanel.reloadPage();
        }
    }


    public FolderResource getFolderResource() {
        return folderResource;
    }

    public void setFolderResource(FolderResource folderResource) {
        this.folderResource = folderResource;
    }

    /**
     * Display the 'loading' indicator.
     */
    public void showLoadingIndicator() {
        LoadingPanel.loading(true);
    }

    /**
     * Hide the 'loading' indicator.
     */
    public void hideLoadingIndicator() {
        LoadingPanel.loading(false);
    }

    /**
     * A helper method that returns true if the user's list is currently visible
     * and false if it is hidden.
     *
     * @return true if the user list is visible
     */
    public boolean isUserListVisible() {
        return true;
    }

    /**
     * Display an error message.
     *
     * @param msg the message to display
     */
    public void displayError(String msg) {
        Info.show(msg, Info.Type.WARNING);
    }

    /**
     * Display a warning message.
     *
     * @param msg the message to display
     */
    public void displayWarning(String msg) {
        Info.show(msg, Info.Type.WARNING);
    }

    /**
     * Display an informational message.
     *
     * @param msg the message to display
     */
    public void displayInformation(String msg) {
        Info.show(msg, Info.Type.INFO);
    }

    /**
     * Retrieve the maxSize
     *
     * @return the maxSize
     */
    public Integer getMaxSize() {
        return maxSize;
    }

    /**
     * Retrieve the folders.
     *
     * @return the folders
     */
    public Folders getFolders() {
        return folders;
    }

    /**
     * Retrieve the currentSelection.
     *
     * @return the currentSelection
     */
    public Object getCurrentSelection() {
        return currentSelection;
    }

    /**
     * Modify the currentSelection.
     *
     * @param newCurrentSelection the currentSelection to set
     */
    public void setCurrentSelection(Object newCurrentSelection) {
        currentSelection = newCurrentSelection;
    }

    /**
     * Retrieve the groups.
     *
     * @return the groups
     */
    public Groups getGroups() {
        return groups;
    }

    /**
     * Retrieve the clipboard.
     *
     * @return the clipboard
     */
    public Clipboard getClipboard() {
        return clipboard;
    }

    public String getToken() {
        return "";
    }

    public DocumentsServiceAsync getDocumentsService() {
        return documentsService;
    }

    /**
     * Retrieve the currentUserResource.
     *
     * @return the currentUserResource
     */
    public UserResource getCurrentUserResource() {
        return currentUserResource;
    }

    /**
     * Modify the currentUserResource.
     *
     * @param newUser the new currentUserResource
     */
    public void setCurrentUserResource(UserResource newUser) {
        currentUserResource = newUser;
    }

    public static native void preventIESelection() /*-{
        $doc.body.onselectstart = function () {
            return false;
        };
    }-*/;

    public static native void enableIESelection() /*-{
        if ($doc.body.onselectstart != null)
            $doc.body.onselectstart = null;
    }-*/;

    /**
     * @return the absolute path of the API root URL
     */
    public String getApiPath() {
        return GWT.getModuleBaseURL();
    }

    public class ActionsMenus extends PopupPanel {
        public MenuBar contextMenu = new MenuBar(true);
        private final DocumentImages.Images images;
        private DocUtils docUtils = new DocUtils(DocumentsView.this);
        private String errorMsg;
        private int size = 0;

        public ActionsMenus(DocumentImages.Images images) {
            super(true);
            this.images = images;
            setAnimationEnabled(true);
            createMenu();
            this.setWidget(contextMenu);
            this.setModal(true);
        }


        public void showActionMenu(final int left, final int top) {
            this.setPopupPosition(left, top);
            this.show();
        }

        private void createMenu() {
            contextMenu.setAnimationEnabled(true);
            contextMenu.clearItems();

            if (docUtils.hasCurrenSelection() && !isSystemItem && (getCurrentSelection() instanceof FolderResource) && !docUtils.canEmptyTrash()) {
                contextMenu.addItem(createImageHtml(images.folderNew(), wfmStrings.createFolder()), true, new NewFolderCommand(this));
                size++;
            }
            GWT.log(" List: " + (getCurrentSelection() instanceof List));
            GWT.log(" Folder: " + (getCurrentSelection() instanceof FolderResource));
            GWT.log(" File:" + (getCurrentSelection() instanceof FileResource));
            GWT.log(" Can Restore/Empty trash:" + (docUtils.canRestore() || docUtils.canEmptyTrash()));

            if (docUtils.canRestore() || docUtils.canEmptyTrash()) {
                contextMenu.addItem(docUtils.createHtmlImage(wfmStrings.emptyTrash(), images.delete()), true, new EmptyTrashCommand(this));
            }

            if ((docUtils.canRestore() || docUtils.canEmptyTrash()) && (docUtils.hasCurrenSelection() &&
                    (getCurrentSelection() instanceof List) ||
                    ((getCurrentSelection() instanceof FileResource) && ((FileResource) getCurrentSelection()).isDeleted()))) {


                Object selection = DocumentsView.get().getCurrentSelection();
                String restoreLabel = "";
                if (selection instanceof FolderResource) {
                    restoreLabel = wfmStrings.restore() + " " + wfmStrings.folder();
                } else if (selection instanceof FileResource) {
                    restoreLabel = wfmStrings.restore() + " " + wfmStrings.file();
                } else if (selection instanceof List) {
                    restoreLabel = wfmStrings.restore() + " " + wfmStrings.filesSelected();
                }

                contextMenu.addItem(createImageHtml(images.versions(), restoreLabel), true, new RestoreTrashCommand(this));
//                contextMenu.addItem(createImageHtml(images.delete(), wfmStrings.emptyTrash()), true, new EmptyTrashCommand(this));

                size++;
            } else {

                Object selection = DocumentsView.get().getCurrentSelection();
                String deleteLabel = "";
                String propertiesLabel = "";
                String copyLabel = "";
                String cutLabel = "";
                String pasteLabel = "";
                String shareLabel = "";
                if (selection instanceof FolderResource || (selection instanceof FileResource && ((FileResource) selection).isFolder())) {
                    deleteLabel = wfmStrings.delete();
                    propertiesLabel = wfmStrings.folderProperties();
                    copyLabel = wfmStrings.copy();
                    cutLabel = wfmStrings.cut();
                    pasteLabel = wfmStrings.paste();
                    shareLabel = wfmStrings.share();
                } else if (selection instanceof FileResource) {
                    deleteLabel = wfmStrings.delete();
                    propertiesLabel = wfmStrings.properties();
                    copyLabel = wfmStrings.copy();
                    cutLabel = wfmStrings.cut();
                    pasteLabel = wfmStrings.paste();
                    shareLabel = wfmStrings.share();
                } else if (selection instanceof List) {
                    deleteLabel = wfmStrings.delete() + " " + wfmStrings.filesSelected();
                    propertiesLabel = wfmStrings.properties();
                    copyLabel = wfmStrings.copyFile();
                    cutLabel = wfmStrings.cutFile();
                    pasteLabel = wfmStrings.pasteFile();
                    shareLabel = wfmStrings.share() + " " + wfmStrings.filesSelected();
                }


                //Copy
                if (docUtils.hasCurrenSelection() && ((getCurrentSelection() instanceof List) ||
                        (getCurrentSelection() instanceof FileResource)) && docUtils.canCopy()) {
                    contextMenu.addItem(createImageHtml(images.copy(), copyLabel), true, new CopyCommand(this));
                    size++;
                }
                // cut doc file
                if (docUtils.hasCurrenSelection() && ((getCurrentSelection() instanceof List) ||
                        ((getCurrentSelection() instanceof FileResource) && docUtils.canDeleteFile()))) {
                    contextMenu.addItem(createImageHtml(images.copy(), cutLabel), true, new CutCommand(this));
                    size++;
                }

                // paste doc file
                if (docUtils.hasCurrenSelection() && ((getCurrentSelection() instanceof List) ||
                        (getCurrentSelection() instanceof FileResource)) && docUtils.canPaste()) {
                    contextMenu.addItem(createImageHtml(images.paste(), pasteLabel), true, new PasteCommand(this));
                    size++;
                }
                // share doc file
                if (docUtils.hasCurrenSelection() && ((getCurrentSelection() instanceof List) ||
                        ((getCurrentSelection() instanceof FileResource) && docUtils.canShare()))) {
                    contextMenu.addItem(createImageHtml(images.sharing(), shareLabel), true, new PropertiesCommand(this, 0));
                    size++;
                }

                // delete doc file
                if (docUtils.hasCurrenSelection() && ((getCurrentSelection() instanceof List) ||
                        ((getCurrentSelection() instanceof FileResource) && docUtils.canDeleteFile()))) {

                    contextMenu.addItem(createImageHtml(images.delete(), deleteLabel), true, new ToTrashCommand(this));
                    size++;
                }
                // property doc file
                if (docUtils.hasCurrenSelection() && ((getCurrentSelection() instanceof List) || (getCurrentSelection() instanceof FileResource))) {
                    contextMenu.addItem(createImageHtml(images.viewText(), propertiesLabel), true, new PropertiesCommand(this, 0));
                    size++;
                }
            }
            if (!docUtils.isAllFiles()) {
                // rename doc folder
                if (docUtils.hasCurrenSelection() && docUtils.canRename() && (getCurrentSelection() instanceof FolderResource)) {
                    contextMenu.addItem(createImageHtml(images.rename(), wfmStrings.rename() + " " + wfmStrings.folder()), true, new RenameFolderCommand(this));
                    size++;
                }
                // copy doc folder
                if (docUtils.hasCurrenSelection() && docUtils.canCopy() && (getCurrentSelection() instanceof FolderResource)) {
                    contextMenu.addItem(createImageHtml(images.copy(), wfmStrings.copyFolder()), true, new CopyCommand(this));
                    size++;
                }
                // cut doc folder
                if (docUtils.hasCurrenSelection() && docUtils.canDelete() && (getCurrentSelection() instanceof FolderResource)) {
                    contextMenu.addItem(createImageHtml(images.cut(), wfmStrings.cut() + " " + wfmStrings.folder()), true, new CutCommand(this));
                    size++;
                }
                // paste doc Folder
                if (docUtils.hasCurrenSelection() && docUtils.canPaste() && (getCurrentSelection() instanceof FolderResource)) {
                    contextMenu.addItem(createImageHtml(images.paste(), wfmStrings.paste() + " " + wfmStrings.folder()), true, new PasteCommand(this));
                    size++;
                }
                // share doc folder
                if (docUtils.hasCurrenSelection() && docUtils.canShare() && (getCurrentSelection() instanceof FolderResource)) {
                    contextMenu.addItem(createImageHtml(images.sharing(), wfmStrings.share() + " " + wfmStrings.folder()), true, new PropertiesCommand(this, 1));
                    size++;
                }
                // delete doc
                if (docUtils.hasCurrenSelection() && docUtils.canDelete() && (getCurrentSelection() instanceof FolderResource)) {
                    contextMenu.addItem(createImageHtml(images.delete(), wfmStrings.delete() + " " + wfmStrings.folder()), true, new ToTrashCommand(this)); //new DeleteFolderCommand(this));
                    size++;
                }
                //groups menu item
                if (size >= 0) {
                    contextMenu.addItem(createImageHtml(images.group(), wfmStrings.groups()), true, (Command) () -> {
                        groups.setVisible(true);
                        groups.updateCurrentlyShowingStats();
                        groupBox.open();
                        hide();
                    });
                    size++;
                }
            }
            if (size == 0) {
                errorMsg = wfmStrings.selectAnyItemToActivateBatchActions();
                addDisableItem();

            } else if (docUtils.isAllFiles()) {
                errorMsg = wfmStrings.allFileFileUploadNotAvailable();
                addDisableItem();
            }
        }

        private void addDisableItem() {
            Command disableCmd = this::hide;
            MenuItem disableItem = new MenuItem("<span style=\"color:#888888;\">&nbsp;" + errorMsg + "</span>", true, disableCmd);
            contextMenu.addItem(disableItem);
        }
    }

    private SelectItem getFolderSelectItem() {
        if (entityId != null && folderType != null) {
            if (F_TASK == folderType || F_PR_ISSUE == folderType) {
                return new SelectItem(entityId);
            }
        }
        return null;
    }

    public class UploadFileMenus {
        private final ContextMenu contextMenu = new ContextMenu();
        private final DocumentImages.Images images;
        private DocUtils docUtils;
        private int size = 0;
        private final FolderResource folderResource;
        private String errorMsg;

        UploadFileMenus(DocumentImages.Images images, FolderResource folderResource) {
            this.images = images;
            this.folderResource = folderResource;
            if (folderResource == null) {
                docUtils = new DocUtils(DocumentsView.this);
                errorMsg = wfmStrings.selectAnyItemToActivateFolderOfFileActions();
            } else {
                errorMsg = wfmStrings.youDontHavePermission();
            }
            createMenu();
        }

        private void createMenu() {
            size = 0;
            DocumentsService.App.get().getEnableUploadTypes(new AbstractAsyncCallback<HashMap<String, Boolean>>() {
                public void failure(Throwable throwable) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                public void success(HashMap<String, Boolean> result) {
                    uploadFile.setMenu(createStorageTypes(result).getMenuBar());
                }
            });
        }

        private ContextMenu createStorageTypes(HashMap<String, Boolean> result) {

            if (folderResource != null && folderResource.getPermission().isWrite()
                    || docUtils != null && docUtils.hasCurrenSelection() && !docUtils.isOthersShare() && docUtils.canUpload()) {

                String utitle = wfmStrings.uploadToWFTStorage();
                if (SinksContainerFactory.entryPoint.moduleSetting.isCustomise()) {
                    utitle = wfmStrings.uploadFile();
                }

                if (result.get(AMAZON) != null && result.get(AMAZON)) {
                    contextMenu.addMenuItem(createImageHtml(images.fileUploadAmazon(), utitle), true, (Command) () -> {
                        new UploadFileCommand(null, FileUploadType.AMAZON, getFolderSelectItem(), folderResource).execute();
                        hide();
                    });
                    size++;
                }
                if (result.get(MINIO) != null && result.get(MINIO)) {
                    contextMenu.addMenuItem(createImageHtml(images.fileUploadAmazon(), utitle), true, (Command) () -> {
                        new UploadFileCommand(null, FileUploadType.MINIO, getFolderSelectItem(), folderResource).execute();
                        hide();
                    });
                    size++;
                }
                if (result.get(LOCAL) != null && result.get(LOCAL)) {
                    contextMenu.addMenuItem(createImageHtml(images.upload(), utitle), true, (Command) () -> {
                        new UploadFileCommand(null, FileUploadType.LOCAL, getFolderSelectItem(), folderResource).execute();
                        hide();
                    });
                    size++;
                }
            }

            if (!SinksContainerFactory.entryPoint.moduleSetting.isCustomise()) {

                if (folderResource != null && folderResource.getPermission().isWrite()
                        || docUtils != null && docUtils.hasCurrenSelection() && !docUtils.isOthersShare() && docUtils.canUpload() && ((FolderResource) getCurrentSelection()).getFileType() != F_COMPANY_PUBLIC_ROOT
                        ) {

                    if (result.get(GOOGLE) != null && result.get(GOOGLE)) {
                        contextMenu.addMenuItem(createImageHtml(images.fileUploadGoogle(), wfmStrings.uploadToMyGoogleDocs()), true, (Command) () -> {
                            new UploadFileCommand(null, FileUploadType.GOOGLE_DOCUMENTS, getFolderSelectItem(), folderResource).execute();
                            hide();
                        });
                        size++;

                        contextMenu.addMenuItem(createImageHtml(images.fileUploadGoogle(), wfmStrings.linkToMyExistingGoogleDocs()), true, (Command) () -> {
                            new UploadFileCommand(null, FileUploadType.LINK_TO_GOOGLE_DOCUMENTS, getFolderSelectItem(), folderResource).execute();
                            hide();
                        });
                        size++;
                    }

                    if (result.get(OFFICE_365) != null && result.get(OFFICE_365)) {
                        contextMenu.addMenuItem(createImageHtml(images.fileUploadGoogle(), wfmStrings.uploadToOfficeDocuments()), true, (Command) () -> {
                            new UploadFileCommand(null, FileUploadType.OFFICE_DOCUMENTS, getFolderSelectItem(), folderResource).execute();
                            hide();
                        });
                        size++;

                        contextMenu.addMenuItem(createImageHtml(images.fileUploadGoogle(), wfmStrings.linkToMyExistingOfficeDocs()), true, (Command) () -> {
                            new UploadFileCommand(null, FileUploadType.LINK_TO_OFFICE_DOCUMENTS, getFolderSelectItem(), folderResource).execute();
                            hide();
                        });
                        size++;
                    }
                }
            }

            if (docUtils != null && docUtils.isAllFiles()) {
                errorMsg = wfmStrings.allFileFileUploadNotAvailable();
                documentsService.getRootFolderResource(new AbstractAsyncCallback<FolderResource>() {
                    @Override
                    public void failure(Throwable throwable) {
                    }

                    @Override
                    public void success(FolderResource result) {
                        folderId = result.getObjectId();
                        folders.select(folders.getRootItem());
                        new UploadFileMenus(images, result);
                    }
                });
            } else if (size == 0) {
                errorMsg = wfmStrings.youDontHavePermission();
                contextMenu.addMenuItem(errorMsg, true, (Command) () -> {
                    hide();
                });
                uploadFile.setMenu(contextMenu.getMenuBar());
            }
            return contextMenu;
        }
    }

    private void hide(){
        LoadingPanel.loading(false);
    }

    private String createImageHtml(ImageResource img, String text) {
        return "<span style='margin-left:4px;'>" + AbstractImagePrototype.create(img).getHTML() + "&nbsp;" + text + "</span>";
    }

    /**
     * Return the proper icon based on the MIME type of the file.
     *
     * @param file
     * @return the icon
     */
    /*private String getFileType(FileResource file) {
        String mimetype = file.getContentType();
        if (mimetype == null) {
            return "Document";
        }
        mimetype = mimetype.toLowerCase();
        if (mimetype.startsWith("application/pdf")) {
            return "PDF Document";
        } else if (mimetype.endsWith("excel") || mimetype.endsWith("spreadsheetml.sheet")) {
            return "Microsoft Office Excel Worksheet";
        } else if (mimetype.endsWith("msword") || mimetype.endsWith("wordprocessingml.document")) {
            return "Microsoft Office Word Document";
        } else if (mimetype.endsWith("powerpoint") || mimetype.endsWith("presentationml.presentation")) {
            return "Microsoft Office PowerPoint Presentation";
        } else if (mimetype.startsWith("application/zip") ||
                mimetype.startsWith("application/gzip") ||
                mimetype.startsWith("application/x-gzip") ||
                mimetype.startsWith("application/x-tar") ||
                mimetype.startsWith("application/x-gtar")) {
            return "Archive";
        } else if (mimetype.startsWith("text/html")) {
            return "Html";
        } else if (mimetype.startsWith("text/plain")) {
            return "txt";
        } else if (mimetype.startsWith("image/png")) {
            return "PNG File";
        } else if (mimetype.startsWith("image/jpeg")) {
            return "JPG File";
        } else if (mimetype.startsWith("image/")) {
            return "Image File";
        } else if (mimetype.startsWith("video/")) {
            return "Video Clip";
        } else if (mimetype.startsWith("audio/")) {
            return "Audio";
        }
        return "Document";
    }*/

    /**
     * Return the proper icon based on the MIME type of the file.
     *
     * @param file
     * @return the icon
     */
    private Icon getFileIcon(FileResource file) {
        if (file.isFolder()) {
            return getIconWithStyle("ficon--folder-bold");
        }
        String mimetype = file.getContentType();
        boolean shared = false;
        Folders folders = DocumentsView.get().getFolders();
        if (folders != null && folders.getCurrent() != null && folders.isOthersSharedItem(folders.getCurrent())) {
            DnDTreeItem otherUser = (DnDTreeItem) folders.getUserOfSharedItem(folders.getCurrent());
            if (otherUser == null) {
                shared = false;
            } else {
                String uname = otherUser.getOtherUserResource().getUsername();
                if (uname == null) {
                    uname = ((DnDTreeItem) folders.getSharesItem()).getOthersResource().getUsernameOfUri(otherUser.getOtherUserResource().getObjectId());
                }
                if (uname != null) {
                    shared = file.isShared();
                }
            }
        } else {
            shared = file.isShared();
        }
        if (mimetype == null) {
            return shared ? getIconWithStyle("ficon--file-plus ficon-shared")
                    : getIconWithStyle("ficon--file-plus");
        }
        mimetype = mimetype.toLowerCase();
        if (mimetype.startsWith("application/pdf")) {

            return shared ? getIconWithStyle("ficon--file-pdf ficon-shared")
                    : getIconWithStyle("ficon--file-pdf");
        } else if (mimetype.endsWith("excel") || mimetype.endsWith("spreadsheetml.sheet")) {

            return shared ? getIconWithStyle("ficon--file-excel ficon-shared")
                    : getIconWithStyle("ficon--file-excel");
        } else if (mimetype.endsWith("msword") || mimetype.endsWith("wordprocessingml.document")) {

            return shared ? getIconWithStyle("ficon--file-word ficon-shared")
                    : getIconWithStyle("ficon--file-word");
        } else if (mimetype.endsWith("powerpoint") || mimetype.endsWith("presentationml.presentation")) {

            return shared ? getIconWithStyle("ficon--file-pp ficon-shared")
                    : getIconWithStyle("ficon--file-pp");
        } else if (mimetype.startsWith("application/zip") ||
                mimetype.startsWith("application/gzip") ||
                mimetype.startsWith("application/x-gzip") ||
                mimetype.startsWith("application/x-tar") ||
                mimetype.startsWith("application/x-gtar")) {

            return shared ? getIconWithStyle("ficon--file-zip ficon-shared")
                    : getIconWithStyle("ficon--file-zip");
        } else if (mimetype.startsWith("text/html")) {

            return shared ? getIconWithStyle("ficon--file-html ficon-shared")
                    : getIconWithStyle("ficon--file-html");
        } else if (mimetype.startsWith("text/plain")) {

            return shared ? getIconWithStyle("ficon--file-txt ficon-shared")
                    : getIconWithStyle("ficon--file-txt");
        } else if (mimetype.startsWith("image/")) {

            return shared ? getIconWithStyle("ficon--file-img ficon-shared")
                    : getIconWithStyle("ficon--file-img");
        } else if (mimetype.startsWith("video/")) {

            return shared ? getIconWithStyle("ficon--file-video ficon-shared")
                    : getIconWithStyle("ficon--file-video");
        } else if (mimetype.startsWith("audio/")) {

            return shared ? getIconWithStyle("ficon--file-audio ficon-shared")
                    : getIconWithStyle("ficon--file-audio");
        }
        return shared ? getIconWithStyle("ficon--file-plus ficon-shared")
                : getIconWithStyle("ficon--file-plus");
    }

    private Icon getIconWithStyle(String styleName) {
        Icon icon = new Icon();
        icon.setStyleName(styleName);
        return icon;
    }

    @Override
    public String getIconStyle() {
        return "doc documents";
    }

    @Override
    public FlowPanel getHelpContainer() {

        DeferredCommand.addCommand(() -> {
            if (folders != null) {
                vp.add(folders);
                if (entityId == null && folderType == null) {
                    final WestPanelHelp westPanel = new WestPanelHelp(wfmStrings.helpMessage());
                    final StringBuilder sb = new StringBuilder();
                    sb.append("<span><b><br />").append(wfmStrings.rightClickOnFolderOrFileItemsForMoreOptions()).append("<br /></br /></b></span> ");


                    westPanel.addHtmlLine(new HTML(sb.toString()));
                    vp.add(westPanel);
                }
                return false;
            } else {
                return true;

            }
        });
        wc.add(vp);
        return wc;
    }

    public void getMessage() {
        documentsService.getStorageSize(new AsyncCallback<Double[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Double[] result) {
                companyMaxStorage = result[0];
                companyUsedStorage = result[1];
                String formatted = NumberFormat.getFormat("0.0000").format(companyUsedStorage);
                String formattedPercent = NumberFormat.getFormat("##0.00##").format((100 * companyUsedStorage) / (companyMaxStorage));
                if (Utils.hasRole(ADMIN) || Utils.hasRole(DR)) {
                    HTML line = new HTML("<br>");
                    vp.add(line);
                    HTML label = new HTML(" &nbsp; &nbsp;<span><b>" + formatted + " GB (" + formattedPercent + " %) of " + companyMaxStorage + " GB used </b></span> ");
                    vp.add(label);
                }
            }
        });
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
