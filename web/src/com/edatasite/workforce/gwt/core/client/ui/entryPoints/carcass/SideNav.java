package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import com.edatasite.workforce.gwt.chart.client.rpc.KpiWidgetData;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.DashboardChartsRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.DashboardComponentItem;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMemberItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.shortcut.ShortcutItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PDFTemplateSelector;
import com.edatasite.workforce.gwt.documents.client.commands.NewFolderCommand;
import com.edatasite.workforce.gwt.documents.client.commands.PropertiesCommand;
import com.edatasite.workforce.gwt.documents.client.commands.UploadFileCommand;
import com.edatasite.workforce.gwt.documents.client.dnd.DnDTreeItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.view.DocumentsView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialCollapsible;
import gwt.material.design.client.ui.MaterialCollapsibleBody;
import gwt.material.design.client.ui.MaterialCollapsibleHeader;
import gwt.material.design.client.ui.MaterialCollapsibleItem;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Anchor;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.html.UnorderedList;

import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ADMIN;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DOCUMENTS_FOLDER_ALL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DOCUMENTS_FOLDER_MYFOLDERS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DOCUMENTS_FOLDER_OTHERS;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DOCUMENTS_FOLDER_PUBLIC;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DOCUMENTS_FOLDER_SHARED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DOCUMENTS_FOLDER_SYSTEM;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.DOCUMENTS_FOLDER_TRASH;

public class SideNav extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SideNavUiBinder ourUiBinder = GWT.create(SideNavUiBinder.class);
    @UiField
    MaterialCollapsible sideNavItems;
    @UiField
    Heading menuTitle;
    @UiField
    MaterialLink addNewMenu;
    @UiField
    MaterialDropDown addNewMenuList;
    @UiField
    Div navTitle;
    @UiField
    HTMLPanel content;
    @UiField
    Div companiesDiv;
    HashMap<String, MaterialLink> documentsMap = new HashMap<>();
    private MaterialDropDown company;
    private final HashMap<String, HashMap<String, MaterialLink>> shortcutsMap = new HashMap<>(); //shortcusts/views map by sinks container
    private final HashMap<Integer, HashMap<String, HTML>> emailFolders = new HashMap<>(); //shortcusts/views map by sinks container
    private final LinkedHashSet<SinksContainer> containers;
    private SinksContainer currentContainer;
    private SinksContainer attachedItem;
    private final Div main;

    public SideNav() {
        main = ourUiBinder.createAndBindUi(this);
        initWidget(main);
        containers = new LinkedHashSet();

        main.addMouseOutHandler(event -> {
            onSideNavLeftMenuMouseOut();
        });
        addNewMenu.add(new SvgIcon(SvgEnum.plus));
    }

    public SinksContainer getCurrentContainer() {
        return currentContainer;
    }

    public SinksContainer getDefaultContainer() {
        return currentContainer != null ? currentContainer : !containers.isEmpty() ? containers.iterator().next() : null;
    }

    public void addContainer(SinksContainer container) {

        if (!container.isDynamic()) {
            containers.add(container);

            MaterialCollapsibleHeader header = new MaterialCollapsibleHeader();
            MaterialCollapsibleBody body = new MaterialCollapsibleBody();
            UnorderedList ul = new UnorderedList();

            header.add(new MaterialLink(container.getDescription()));
            if (container.getIcon() != null) {
                header.add(container.getIcon());
            }
            HashMap<String, MaterialLink> map;
            if (shortcutsMap.containsKey(container.getName())) {
                map = shortcutsMap.get(container.getName());
            } else {
                map = new HashMap<>();
            }

            for (Map.Entry<String, ShortcutItem> itemEntry : container.getItemsByView().entrySet()) {
                ShortcutItem shortcut = itemEntry.getValue();

                MaterialLink link = new MaterialLink(shortcut.getDescription());
                link.setTitle(shortcut.getDescription());
                link.addClickHandler(e -> {
                    activateShortcut(container, shortcut);
                });
                ListItem li = new ListItem();
                li.add(link);

                if (shortcut.getAddNew() != null) {
                    li.add(createAddNewButtonForShortcut(shortcut));
                }
                ul.add(li);

                map.put(shortcut.getName(), link);
            }
            //We must reset this
            shortcutsMap.put(container.getName(), map);

            body.add(ul);

            MaterialCollapsibleItem item = new MaterialCollapsibleItem();
            String className = " main-dir__";
            if (container.getName() != null) {
                className += container.getName().replace(" ", "_").toLowerCase();
            }
            item.addStyleName(className);
            item.add(header);
            item.add(body);
            sideNavItems.add(item);
            String moduleClassName = getModuleClass();
            if (!"".equals(moduleClassName)) {
                sideNavItems.addStyleName(moduleClassName);
            }
            if (container.getItemsByView().values().size() == 1) {
                item.addClickHandler(e -> {
                    ul.getChildrenList().forEach(child -> {
                        container.getItemsByView().values().forEach(value -> {
                            activateShortcut(container, value);
                        });
                    });
                });
            }

            container.setWestMenuContainer(item);

            if (currentContainer == null) {
                currentContainer = container;
            }
        }
    }

    private static native String[][] printChartsPdf() /*-{
        return $wnd.printAllChartsToPdf();
    }-*/;

    public HashMap<Integer, HashMap<String, HTML>> getEmailFolders() {
        return emailFolders;
    }

    public void addToCompaniesDiv(Widget link, MaterialLink widget) {
        if (company == null) {
            company = new MaterialDropDown(link);
            company.addStyleName("prof-comp-list");
            if (Utils.isArabicCompany()) {
                company.addStyleName("prof-comp-list__arabic");
            }
            companiesDiv.add(company);
        }
        company.add(widget);
    }

    public void resetSideNavWidgetPlacement() {
        content.clear();
        content.add(navTitle);
        content.add(sideNavItems);
        sideNavItems.open(1);
    }

    public void loadSideNavContent(boolean value) {
        content.clear();
        LoadingPanel.loading(true, content);
    }

    public void replaceContent(Widget... widget) {
        content.clear();
        if (widget != null) {
            for (Widget widget_ : widget) {
                content.add(widget_);
            }
        }
    }

    private void activateShortcut(SinksContainer container, ShortcutItem shortcut) {
        //clear browser history for system container's shortcusts
        History.newItem("", false);
        MainLayout.get().clearDynamicTabsConfigs();

        currentContainer = container;
        container.reInit();
        shortcut.activate();
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_TAB, container, SideNav.this);
    }

    public void onSideNavLeftMenuMouseOut() {
        if (RootPanel.getBodyElement().hasClassName("left-menu-closed")) {
            // Reloading closes dropdown
            // Reload if dropDown.setHover(false)
            if (company != null) {
                company.reload();
            }
            addNewMenuList.reload();
        }
//        resetSideNavWidgetPlacement();
        MainLayout.get().setSideMenuHovered(false);
    }

    public void addMCContainer(Integer emailAccountId, SinksContainer container, boolean active, Long unreadCount, ClickHandler accountSettingsClickHandler) {

        if (!container.isDynamic()) {
            containers.add(container);

            MaterialCollapsibleHeader header = new MaterialCollapsibleHeader();
            MaterialCollapsibleBody body = new MaterialCollapsibleBody();
            UnorderedList ul = new UnorderedList();

            //////////////////////////
            Span settingsLink = new Span("");
            settingsLink.setStyleName("main-directories__sublink");
            settingsLink.setTooltip(wfmStrings.accountSettings());
            settingsLink.setTooltipPosition(Position.RIGHT);
            settingsLink.addClickHandler(accountSettingsClickHandler);
            addDashboardIcon(settingsLink);
            header.add(settingsLink);

            MaterialLink headerLink = new MaterialLink(container.getDescription());
            new MaterialTooltip(headerLink, container.getDescription()).setPosition(Position.TOP);
            header.add(headerLink);
            if (container.getIcon() != null) {
                header.add(container.getIcon());
            }

            UnorderedList ulheader = new UnorderedList();
            ListItem liHeader = new ListItem();
            if (active) {
                Icon i = new Icon();
                i.setClass("status-indicator");
                new MaterialTooltip(i, wfmStrings.active()).setPosition(Position.TOP);
                liHeader.add(i);
                liHeader.setClass("status--active");
            } else {
                Icon i = new Icon();
                i.setClass("status-indicator");
                new MaterialTooltip(i, wfmStrings.inactive()).setPosition(Position.TOP);
                liHeader.add(i);
                liHeader.setClass("status--inactive");
            }
            liHeader.add(settingsLink);
            liHeader.add(headerLink);
            ulheader.add(liHeader);
            header.add(ulheader);
            //////////////////
            HashMap<String, MaterialLink> map;
            if (shortcutsMap.containsKey(container.getName())) {
                map = shortcutsMap.get(container.getName());
            } else {
                map = new HashMap<>();
            }

            for (Map.Entry<String, ShortcutItem> itemEntry : container.getItemsByView().entrySet()) {
                ShortcutItem shortcut = itemEntry.getValue();

                String cnt = "";
                if (("messagecenter_INBOX").equalsIgnoreCase(shortcut.getName())) {
                    cnt = " - <b>(" + unreadCount + ")</b>";
                }
                MaterialLink link = new MaterialLink();
                HTML folder = new HTML(shortcut.getDescription() + cnt);
                //Keep reference to update count later (on clear event)
                if (("messagecenter_INBOX").equalsIgnoreCase(shortcut.getName())) {
                    HashMap<String, HTML> foldermetadata = new HashMap<>();
                    foldermetadata.put(shortcut.getDescription(), folder);
                    emailFolders.put(emailAccountId, foldermetadata);
                }

                link.add(folder);
                link.addClickHandler(e -> {
                    activateShortcut(container, shortcut);
                });
                ListItem li = new ListItem();
                li.add(link);
                if (shortcut.getAddNew() != null) {
                    li.add(createAddNewButtonForShortcut(shortcut));
                }
                ul.add(li);

                map.put(shortcut.getName(), link);
            }
            //We must reset this
            shortcutsMap.put(container.getName(), map);

            body.add(ul);

            MaterialCollapsibleItem item = new MaterialCollapsibleItem();
            String className = "main-dir__";
            if (container.getName() != null) {
                className += container.getName().replace(" ", "_").toLowerCase();
            }
            item.addStyleName(className);
            item.add(header);
            item.add(body);
            sideNavItems.add(item);

            container.setWestMenuContainer(item);

            if (currentContainer == null) {
                currentContainer = container;
            }
        }
    }

    private Anchor createAddNewButtonForShortcut(ShortcutItem shortcut) {
        if (shortcut.isCustomForm() && shortcut.getFormId() != null) {
            Anchor btn = new Anchor();
//        btn.getElement().setAttribute("href", "javascript;");
            Object addNew = shortcut.getAddNew();
            btn.addStyleName("btn--circle btn-small btn--success");
            btn.getElement().setInnerHTML("<svg class=\" icon--plus\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#plus\"></use></svg>");
            AllInOneService.App.get().checkCustomFormQuota(shortcut.getFormId(), false, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(Integer integer) {
                    if (integer == 0) {
                        if (addNew instanceof String) {
                            btn.addClickHandler(e -> {
                                e.preventDefault();
                                e.stopPropagation();

                                if (shortcut.isCustomForm()) {
                                    try {
                                        Integer customFormId = Integer.parseInt(shortcut.getName() != null && !shortcut.getName().isEmpty() ? shortcut.getName().substring("custom_form_".length()) : "-1");
                                        if (customFormId != -1) {
                                            SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + customFormId + "/" + shortcut.getFormId() + "/intro/" + shortcut.getDescription());
                                        } else {
                                            SinksContainerFactory.entryPoint.onHistoryChanged((String) addNew);
                                        }
                                    } catch (NumberFormatException ex) {
                                        SinksContainerFactory.entryPoint.onHistoryChanged((String) addNew);
                                    }
                                } else {
                                    SinksContainerFactory.entryPoint.onHistoryChanged((String) addNew);
                                }
                            });
                        } else if (addNew instanceof Command) {
                            btn.addClickHandler(event -> {
                                event.stopPropagation();
                                event.preventDefault();
                                ((Command) addNew).execute();
                            });
                        }
                    } else if (integer == -1) {
                        btn.addClickHandler(event -> Info.warn(wfmStrings.youDontHavePermission()));
                    } else if (integer == -2) {
                        btn.addClickHandler(event -> Info.warn(wfmStrings.dontHaveQuotaToAdd()));
                    }
                }
            });
            return btn;
        } else {
            Anchor btn = new Anchor();
//        btn.getElement().setAttribute("href", "javascript;");
            Object addNew = shortcut.getAddNew();
            btn.addStyleName("btn--circle btn-small btn--success");
            btn.getElement().setInnerHTML("<svg class=\" icon--plus\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#plus\"></use></svg>");
            if (addNew instanceof String) {
                btn.addClickHandler(e -> {
                    e.preventDefault();
                    e.stopPropagation();
                    SinksContainerFactory.entryPoint.onHistoryChanged((String) addNew);
                });
            } else if (addNew instanceof Command) {
                btn.addClickHandler(event -> {
                    event.stopPropagation();
                    event.preventDefault();
                    ((Command) addNew).execute();
                });
            }
            return btn;
        }
    }

    public void addDashboardContainer(SinksContainer container) {
        containers.add(container);

        MaterialCollapsibleHeader header = new MaterialCollapsibleHeader();
        header.add(new MaterialLink(container.getDescription()));

        MaterialCollapsibleBody body = new MaterialCollapsibleBody();
        //If i uncomment below line then we having issue with expanding default sinkscontainer
//      body.setDisplay(Display.BLOCK);
        UnorderedList ul = new UnorderedList();

        HashMap<String, MaterialLink> map;
        if (shortcutsMap.containsKey(container.getName())) {
            map = shortcutsMap.get(container.getName());
        } else {
            map = new HashMap<>();
        }

        for (Map.Entry<String, ShortcutItem> itemEntry : container.getItemsByView().entrySet()) {
            ShortcutItem shortcut = itemEntry.getValue();

            MaterialLink link = new MaterialLink(shortcut.getDescription());
            link.setTitle(shortcut.getDescription());
            link.addClickHandler(e -> activateShortcut(container, shortcut));

            Icon actionsIcon = new Icon();
            actionsIcon.setStyleName("action-listing ficon--more-horiz");

            MaterialLink actionsLink = new MaterialLink();
            actionsLink.setStyleName("btn btn--icon-text btn--text-dark");
            actionsLink.setId("documentsactionslink_" + shortcut.getName());
            actionsLink.add(actionsIcon);

            MaterialPanel mpanel = new MaterialPanel("dropdown-kit--arrow--below");
            mpanel.add(actionsLink);
            MaterialPanel sublinkMpanel = new MaterialPanel("main-directories__sublink");
            sublinkMpanel.add(mpanel);

            MaterialDropDown menuContainer = new MaterialDropDown(actionsLink);
            menuContainer.addStyleName("file-folder__context");
            menuContainer.setBelowOrigin(true);
            actionsLink.add(menuContainer);
            actionsLink.addClickHandler(event -> clickDashboardShortcutAction(shortcut, menuContainer, container));
            ListItem li = new ListItem();
            li.add(sublinkMpanel);
            li.add(link);
            ul.add(li);
            map.put(shortcut.getName(), link);

        }
        shortcutsMap.put(container.getName(), map);
        body.add(ul);

        MaterialCollapsibleItem item = new MaterialCollapsibleItem();
        item.setInitialClasses("main-dir__dashboard");
        if (container.getItemsByView().size() > 0) {
            item.add(header);
        }
        item.add(body);
        sideNavItems.add(item);
        body.getElement().getStyle().setDisplay(Style.Display.BLOCK); //Dashboard not visible with hisotry token
        container.setWestMenuContainer(item);

        if (currentContainer == null) {
            currentContainer = container;
        }
    }

    public void addDocumentsContainer(SinksContainer container) {
        containers.add(container);

        MaterialCollapsibleBody body = new MaterialCollapsibleBody();

        UnorderedList ul = new UnorderedList();
//        ul.setStyleName("main-directories__list--2");

        MaterialCollapsibleItem item = new MaterialCollapsibleItem();
        String className = "main-dir__";
        if (container.getName() != null) {
            className += container.getName().replace(" ", "_").toLowerCase();
        }
        item.addStyleName(className);
        body.add(ul);

        item.add(body);
        item.setActive(true);
        sideNavItems.add(item);

        container.setWestMenuContainer(item);

        LinkedList<SelectItem> folders = new LinkedList<>();
        if (!Utils.hasRole(Constants.CLIENT)) {
            folders.add(new SelectItem(0, DOCUMENTS_FOLDER_ALL, wfmStrings.all()));
            folders.add(new SelectItem(0, DOCUMENTS_FOLDER_SYSTEM, wfmStrings.sysTemFolder()));
            folders.add(new SelectItem(0, DOCUMENTS_FOLDER_MYFOLDERS, wfmStrings.myFolders()));
            folders.add(new SelectItem(0, DOCUMENTS_FOLDER_PUBLIC, wfmStrings.publicFolder()));
            folders.add(new SelectItem(0, DOCUMENTS_FOLDER_SHARED, wfmStrings.sharedByMe()));
            folders.add(new SelectItem(0, DOCUMENTS_FOLDER_TRASH, wfmStrings.trashBin()));
        } else {
            folders.add(new SelectItem(0, DOCUMENTS_FOLDER_MYFOLDERS, wfmStrings.myFolders()));
            folders.add(new SelectItem(0, DOCUMENTS_FOLDER_OTHERS, wfmStrings.sharedWithMe()));
        }


        for (SelectItem folder : folders) {

            MaterialLink viewNameLink = new MaterialLink(folder.getDescription());
            viewNameLink.addClickHandler(e ->
                    //Activate View (Folder)
                    selectDocumentView(container, folder)
            );

            ListItem li = new ListItem();
            Icon actionsIcon = new Icon();
            actionsIcon.setStyleName("action-listing ficon--more-horiz");

            MaterialLink actionsLink = new MaterialLink();
            actionsLink.setStyleName("btn btn--icon-text btn--text-dark");
            actionsLink.setId("documentsactionslink_" + folder.getName());
            actionsLink.add(actionsIcon);

            MaterialPanel mpanel = new MaterialPanel("dropdown-kit--arrow--below");
            mpanel.add(actionsLink);
            MaterialPanel sublinkMpanel = new MaterialPanel("main-directories__sublink");
            sublinkMpanel.add(mpanel);

            li.add(sublinkMpanel);
            li.add(viewNameLink);
            ul.add(li);


            MaterialDropDown menuContainer = new MaterialDropDown(actionsLink);
            menuContainer.addStyleName("file-folder__context");
            menuContainer.setBelowOrigin(true);
            actionsLink.add(menuContainer);

            actionsLink.addClickHandler(event -> clickDocumentsShorcutAction(folder, menuContainer));

            documentsMap.put(folder.getName(), viewNameLink);

        }

        container.getWestMenuContainer().getBody().makeActive(documentsMap.get(DOCUMENTS_FOLDER_MYFOLDERS).getParent());

        if (currentContainer == null) {
            currentContainer = container;
        }
    }

    private void clickDocumentsShorcutAction(SelectItem folder, MaterialDropDown menuContainer) {
        menuContainer.clear();

        if (DOCUMENTS_FOLDER_PUBLIC.equals(folder.getName()) || DOCUMENTS_FOLDER_MYFOLDERS.equals(folder.getName())) {
            MaterialLink createFolderLink = new MaterialLink(wfmStrings.createFolder());
            createFolderLink.addClickHandler(eventCreateFolder -> new NewFolderCommand(new PopupPanel()).execute());
            menuContainer.add(createFolderLink);

            MaterialLink folderPropertiesLink = new MaterialLink(wfmStrings.folderProperties());
            folderPropertiesLink.addClickHandler(eventFolderProperties -> new PropertiesCommand(new PopupPanel(), 0).execute());
            menuContainer.add(folderPropertiesLink);

            MaterialLink uploadFileLink = new MaterialLink(wfmStrings.uploadFile());
            uploadFileLink.addClickHandler(eventUpload -> new UploadFileCommand(new PopupPanel(), Utils.getFileUploadType()).execute());
            menuContainer.add(uploadFileLink);
        }

        MaterialLink refreshLink = new MaterialLink(wfmStrings.refresh());
        refreshLink.addClickHandler(eventRefresh -> {
            if (DocumentsView.get().getCurrentSelection() instanceof FileResource || DocumentsView.get().getCurrentSelection() instanceof List) {
                DocumentsView.get().showFileList(true);
            } else if (DocumentsView.get().getCurrentSelection() instanceof GroupMemberItem) {
            } else {
                DnDTreeItem selectedTreeItem = (DnDTreeItem) DocumentsView.get().getFolders().getCurrent();
                if (selectedTreeItem != null) {
                    DocumentsView.get().getFolders().updateFolder(selectedTreeItem);
                }
            }
        });
        menuContainer.add(refreshLink);
    }

    public void selectDocumentView(SinksContainer container, SelectItem folder) {
        DocumentsView documentsView = DocumentsView.get();
        if (documentsView != null) {
            documentsView.deselectAllRows();
        }

        currentContainer = container;
        MainLayout.get().clearDynamicTabsConfigs();
        container.reInit();

        documentsView = DocumentsView.get();
        if (documentsView != null && documentsView.getFolders() != null) {
            if (DOCUMENTS_FOLDER_ALL.equals(folder.getName())) {
                documentsView.getFolders().selectAll();
            } else if (DOCUMENTS_FOLDER_PUBLIC.equals(folder.getName())) {
                documentsView.getFolders().selectPublic();
            } else if (DOCUMENTS_FOLDER_SHARED.equals(folder.getName())) {
                documentsView.getFolders().selectSharedByMe();
            } else if (DOCUMENTS_FOLDER_OTHERS.equals(folder.getName())) {
                documentsView.getFolders().selectSharedWithOthers();
            } else if (DOCUMENTS_FOLDER_TRASH.equals(folder.getName())) {
                documentsView.getFolders().selectTrash();
            } else if (DOCUMENTS_FOLDER_MYFOLDERS.equals(folder.getName())) {
                documentsView.getFolders().selectMyFolders();
            } else if (DOCUMENTS_FOLDER_SYSTEM.equals(folder.getName())) {
                documentsView.getFolders().selectSystemFolders();
            }
        }
        MaterialLink viewNameLink = documentsMap.get(folder.getName());
        if (viewNameLink != null) {
            container.getWestMenuContainer().getBody().makeActive(viewNameLink.getParent());
        }
    }

    /**
     * Clear all containers
     * This one is used for getting started page
     * Ex. After account getting started or PM ... cleara all static/dynamic containers and redraw from scratch
     */
    public void clearContainers() {
        currentContainer = null;
        sideNavItems.clear();

        containers.forEach(c -> WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BEFORE_REMOVE_TAB, c, SideNav.this));

        containers.clear();
    }

    public void setSelection(SinksContainer container) {

        if (!isAttached()) {
            attachedItem = container;
            return;
        }

        if (!container.isDynamic()) {
            this.currentContainer = container;
        }

        ShortcutItem shortcut = container.getItemsByView().get(container.getPreparedView());

        makeActive(container, shortcut);
    }

    public void makeActive(SinksContainer container, ShortcutItem shortcut) {
        Map<String, MaterialLink> shortCutLinks = shortcutsMap.get(container.getName());

        if (shortcut == null) {
            shortcut = container.getItemsByView().values().iterator().next();
        }

        if (shortCutLinks != null && container.getWestMenuContainer() != null) {
            MaterialLink link = shortCutLinks.get(shortcut.getName());

            if (link != null) {
                container.reInit();

                shortcut.activate();
                container.getWestMenuContainer().getBody().makeActive(link.getParent());

                if (!container.isDynamic()) {
                    this.currentContainer = container;
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_TAB, container, SideNav.this);
                }
            }
        } else {
            container.reInit();
            shortcut.activate();
        }
    }


    public void disableAddNew() {
        addNewMenu.removeFromParent();
        addNewMenuList.removeFromParent();
    }

    public void addNewMenuItem(String title, String historyToken) {
        addNewMenuItem(title, historyToken, null);
    }

    public void addNewMenuItem(String title, String historyToken, Character accessKey) {
        addNewMenuItem(title, historyToken, accessKey, null);
    }

    public void addNewMenuItem(String title, String historyToken, Character accessKey, String Id) {
        MaterialLink link = new MaterialLink();
        link.add(new SvgIcon(SvgEnum.plus));
        link.setText(title);
        if (historyToken != null) {
            link.setHref(Utils.getPathName() + "#" + historyToken);
        }
        if (accessKey != null) {
            link.setAccessKey(accessKey);
        }
        if (Id != null && !Id.isEmpty()) {
            link.ensureDebugId(Id);
        }
        addNewMenuList.add(link);
    }

    public void addNewMenuItem(String title, ClickHandler clickHandler) {
        MaterialLink link = new MaterialLink();
        link.add(new SvgIcon(SvgEnum.plus));
        link.setText(title);
        link.addClickHandler(clickHandler);
        addNewMenuList.add(link);
    }

    public void setMenuTitle(String title) {
        menuTitle.setText(title);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (attachedItem == null) {
            attachedItem = containers.iterator().next();
        }
        setSelection(attachedItem);
        attachedItem = null;
        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ADD_TAB, getDefaultContainer(), SideNav.this);
    }

    public void clearSelection() {
        currentContainer = null;
        sideNavItems.closeAll();
    }

    public void openSecondContainer() {

//        if(sideNavItems.getWidgetCount()>1) {
        try {
            sideNavItems.open(1);
        } catch (Exception e) {

        }
//        }
    }

    private String getModuleClass() {
        if (Constants.MODULE_ACCOUNTING.equals(GWT.getModuleName())) {
            return "main-directories--accounting";
        } else if (Constants.MODULE_CRM.equalsIgnoreCase(GWT.getModuleName())) {
            return "main-directories--sales";
        } else if (Constants.MODULE_HRMS.equalsIgnoreCase(GWT.getModuleName())) {
            return "main-directories--humans";
        } else if (Constants.MODULE_PM.equalsIgnoreCase(GWT.getModuleName())) {
            return "main-directories--projects";
        } else if (Constants.MODULE_PAYROLL.equalsIgnoreCase(GWT.getModuleName())) {
            return "main-directories--payroll";
        } else if (Constants.MODULE_REPORTING.equalsIgnoreCase(GWT.getModuleName())) {
            return "main-directories--reports";
        } else if (Constants.MODULE_DOCUMENTS.equalsIgnoreCase(GWT.getModuleName())) {
            return "main-directories--docs";
        } else if (Constants.MODULE_TC.equalsIgnoreCase(GWT.getModuleName())) {
            return "main-directories--trainingCenter";
        }
        return "";
    }

    interface SideNavUiBinder extends UiBinder<Div, SideNav> {
    }

    private void clickDashboardShortcutAction(ShortcutItem shortcut, MaterialDropDown menuContainer, SinksContainer container) {
        if (menuContainer.getItems().isEmpty()) {
            menuContainer.clear();

            String[][] svgs = printChartsPdf();
            HashMap<String, String> charts = new HashMap<>();
            for (int i = 0; i < svgs[0].length; i++) {
                charts.put(svgs[0][i], svgs[1][i]);
            }
            if (Utils.hasRole(ADMIN) && !(Utils.getHostName().equals("app.kpi.com"))) {
                //PDF portrait
                MaterialLink pdfPortraitLink = new MaterialLink(wfmStrings.pdfPortrait());
                pdfPortraitLink.addClickHandler((e) -> {
                    new PDFTemplateSelector("DASHBOARD_CHARTS", new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(id, charts, shortcut.getDescription(), container, false);
                        }
                    });
                });
                menuContainer.add(pdfPortraitLink);

                //PDF landscape
                MaterialLink pdfLandscapeLink = new MaterialLink(wfmStrings.pdfLandscape());
                pdfLandscapeLink.addClickHandler((e) -> {
                    new PDFTemplateSelector("DASHBOARD_CHARTS", new ExtendedCommand() {
                        @Override
                        public void execute(Integer id) {
                            generatePDF(id, charts, shortcut.getDescription(), container, true);
                        }
                    });
                });
                menuContainer.add(pdfLandscapeLink);
            }

            //Customize button
            MaterialLink customizeLink = new MaterialLink(wfmStrings.customize());
            customizeLink.addClickHandler(event -> {
                View currentView = container.getWorkarea().getCurrentView();
                Integer viewId = shortcut.getViewId();

                if (currentView != null && viewId != null
                        && currentView.getName().equals(shortcut.getName())) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DASHBOARD_SETTINGS_CLICK, viewId, SideNav.this);
                }
            });
            menuContainer.add(customizeLink);
        } else {
            menuContainer.clear();
            menuContainer.setGwtDisplay(Style.Display.NONE);
            menuContainer.setStyle("border: none");
        }
    }

    private void addDashboardIcon(Span settingsLink) {
        settingsLink.getElement().setInnerHTML("<svg class=\"icon--equalizer\"><use href=\"mainStyles/new-ui/icons/sprite__panels.svg?v=" + Utils.getUploadVersion() + "#sliders\"></use></svg>");
    }

    private void generatePDF(Integer templateId, HashMap<String, String> charts, String desc, SinksContainer container, boolean isLandscape) {
        DashboardChartsRequestObject requestObject = new DashboardChartsRequestObject(isLandscape);
        HashMap<String, String> parameterIn = requestObject.getRequestParams();
        LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
        HashMap<Integer, KpiWidgetData> numericWidgetMap = new HashMap<>();
        HashMap<Integer, KpiWidgetData> tableWidgetMap = new HashMap<>();
        HashMap<Integer, KpiWidgetData> dataMap = new HashMap<>();
        HashMap<String, String> widgets = new HashMap<>();
        View view = container.getWorkarea().getCurrentView();
        Map<Integer, DashboardComponentItem> dashboards = view.getDashboards();
        if (view.getWidgetDataList() != null && view.getWidgetDataList().size() != 0) {
            for (KpiWidgetData data : view.getWidgetDataList()) {
                if (data != null && data.getChartDataTitle() != null) {
                    if (data.getTableData() == null) {
                        numericWidgetMap.put(data.getObjectId(), data);
                    } else if (data.getTableData() != null && data.getTableData().size() > 0) {
                        tableWidgetMap.put(data.getObjectId(), data);
                    }
                }
            }
        }
        HashMap<Integer, String> colors = new HashMap<>();
        int i = 0;
        for (Map.Entry<Integer, KpiWidgetData> duplicate : numericWidgetMap.entrySet()) {
            colors.put(i, duplicate.getValue().getChartDataTitleColor());
            i++;
        }

        for (Map.Entry<Integer, String> color : colors.entrySet()) {
            for (Map.Entry<Integer, KpiWidgetData> duplicate : numericWidgetMap.entrySet()) {
                if (color.getValue().equals(duplicate.getValue().getChartDataTitleColor())) {
                    dataMap.put(duplicate.getKey(), duplicate.getValue());
                }
            }
        }

        for (Map.Entry<Integer, KpiWidgetData> tableWidget : tableWidgetMap.entrySet()) {
            String mapValue = "";
            int j = 0;
            Double totalAmount = 0.0;
            for (SelectItem item : tableWidget.getValue().getTableData()) {
                HashMap<String, String> map = item.getValueMap();
                String data = "";
                if (j == 0) {
                    data = map.get("name") + "~" + map.get("description");
                } else {
                    String value = Utils.setTextInCenter(item.getTotalAmount());
                    data = map.get("name") + "~" + value;
                }
                mapValue = mapValue.concat("£").concat(data);
                if (item.getTotalAmount() != null) {
                    totalAmount += item.getTotalAmount();
                }
                j++;
            }
            mapValue = mapValue.concat("£").concat(wfmStrings.total()).concat("~") + Utils.setTextInCenter(totalAmount);
            widgets.put(tableWidget.getKey() + "@" + tableWidget.getValue().getChartDataTitle(), mapValue);
        }

        for (Map.Entry<Integer, KpiWidgetData> data : dataMap.entrySet()) {
            widgets.put(data.getKey() + "@" + data.getValue().getChartDataTitle() + "@" + data.getValue().getChartDataTitleColor(), String.valueOf(data.getValue().getCurrent().setScale(2, RoundingMode.UP)));
        }

        widgets.putAll(charts);

        for (Integer id : dashboards.keySet()) {
            if (id != null) {
                for (String keys : widgets.keySet()) {
                    String[] strings = keys.split("@");
                    Integer widgetId = Integer.valueOf(strings[0]);
                    if (id.equals(widgetId)) {
                        DashboardComponentItem componentItem = dashboards.get(id);
                        String keyMap;
                        if (strings.length == 3) {
                            keyMap = strings[1] + "@" + strings[2] + "@" + componentItem.getX() + "@" + componentItem.getY() + "@" + componentItem.getWidth() + "@" + componentItem.getHeight();
                        } else {
                            keyMap = strings[1] + "@" + componentItem.getX() + "@" + componentItem.getY() + "@" + componentItem.getWidth() + "@" + componentItem.getHeight();
                        }
                        parameters.put(keyMap, widgets.get(keys));
                        break;
                    }
                }
            }
        }

        parameters.putAll(parameterIn);
        if (templateId != null) {
            parameters.put("templateID", String.valueOf(templateId));
        }
        if (desc != null) {
            parameters.put("dashboardName", desc);
        }
        String pdfURL = CommandConstants.PDF_URL + "/dashBoardChartsPDFHandler";
        Utils.sendPDFOrExcelRequest(companiesDiv, pdfURL, parameters, "_blank");
    }
}
