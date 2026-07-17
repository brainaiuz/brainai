package com.edatasite.workforce.gwt.messagecenter.client.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.EmailFolder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialTooltip;
import gwt.material.design.client.ui.html.Span;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Azazello
 * Date: Jan 31, 2018
 * Time: 11:50:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmailListView extends BaseListView implements Constants {
    private final MessageCenterServiceAsync messageCenterService = MessageCenterService.App.get();

    private ListingPanel<Email> listingTable;
    private HashSet<Email> selectedItems = new HashSet<>();
    private Integer folderID;
    private Integer relationID;
    private String relationType;
    private String relationName;
    private String relatedEmail;
    private Integer trackerID;
    private ContextMenu actions;
    private ContextMenu actionsEmpty;
    private FlexTable additionalPanel;
    private CheckBox listOnlyRelated;

    public EmailListView(String name, String description, Integer folderID, EmailAccountItem emailAccountItem) {
        super(name, description);
        this.folderID = folderID;
    }

    public EmailListView(String relationType, Integer relationID) {
        super("messagecenter", wfmStrings.emails());
        this.relationType = relationType;
        this.relationID = relationID;
    }

    public EmailListView(String relationType, Integer relationID, Integer trackerID) {
        super("messagecenter", wfmStrings.emails());
        this.relationType = relationType;
        this.relationID = relationID;
        this.trackerID = trackerID;
    }

    @Override
    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.MessageCenter, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        listingTable.addSelectionRowHandler(selectedRows -> {
            selectedItems = selectedRows;
            if (selectedItems != null && selectedItems.size() > 0) {
                if (more != null) {
                    more.setVisible(true);
                }
            } else {
                if (more != null) {
                    more.setVisible(false);
                }
            }
        });

        additionalPanel = new FlexTable();
        additionalPanel.getElement().getStyle().setPaddingLeft(20d, Style.Unit.PX);

        if (showRelatedEmails()) {
            listOnlyRelated = new KpiCheckBox("All related emails");
            listOnlyRelated.ensureDebugId("listOnlyRelated");
            messageCenterService.getRelatedEmail(relationType, relationID, new AbstractAsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(String result) {
                    relatedEmail = result;
                    if (result != null) {
                        additionalPanel.setWidget(0, 2, listOnlyRelated);
                        listOnlyRelated.addValueChangeHandler(booleanValueChangeEvent -> {
                            listingTable.getFilterParametrs().setLookUpBy(booleanValueChangeEvent.getValue() ? Constants.BY_EMAIL : null);
                            listingTable.getFilterParametrs().setEmail(result);
                            listingTable.getFilterParametrs().setShowActive(booleanValueChangeEvent.getValue());
                            listingTable.reloadPage();
                        });
                    }
                }
            });
            AllInOneService.App.get().getRelationName(relationID, relationType, new AbstractAsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(String result) {
                    relationName = result;
                }
            });
        }

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMAIL_LIST_CHANGE, EmailListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_EMAILS_CLEARED, EmailListView.this, (sender, args) -> {
            listingTable.reloadPage();
            MessageCenterService.App.get().getUserFetchableEmailFolders(new AbstractAsyncCallback<HashMap<EmailAccountItem, HashSet<EmailFolder>>>() {
                @Override
                public void onFailure(Throwable caught) {
                    GWT.log("Couldnt update count of unread messages.");
                }

                @Override
                public void onSuccess(HashMap<EmailAccountItem, HashSet<EmailFolder>> result) {

                    HashMap<Integer, HashMap<String, HTML>> emailFolders = MainLayout.get().getSideNavBar().getEmailFolders();
                    if (emailFolders != null) {
                        for (EmailAccountItem emailAccountItem : result.keySet()) {
                            if (emailFolders.get(emailAccountItem.getObjectID()) != null) {
                                HashMap<String, HTML> folder = emailFolders.get(emailAccountItem.getObjectID());
                                if (folder != null && folder.size() > 0) {
                                    for (Map.Entry<String, HTML> f : folder.entrySet()) {
                                        //Update shorcut text
                                        f.getValue().setHTML(f.getKey() + " - <b>(" + emailAccountItem.getUnreadCount() + ")</b>");
                                    }
                                }
                            }
                        }
                    }

                }
            });
        });

        add(listingTable);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();
        //Actions
        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<Email, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH / 2) {
            @Override
            public Anchor getCellValue(final Email rowValue) {
                final MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);

                MenuPopItem markAsRead = new MenuPopItem(rowValue.isSeen() ? wfmStrings.markAsUnread() : wfmStrings.markAsRead(), "", () -> setEmailFlags(new ArrayList<>(Collections.singletonList(rowValue.getObjectID())), rowValue.isSeen() ? FLAG_UNREAD : FLAG_READ));
                markAsRead.ensureDebugId("mark_as_read_unread");
                menuBar.addItem(markAsRead);

                MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "", () -> deleteSelection(rowValue));
                delete.ensureDebugId("delete");
                menuBar.addItem(delete);

                ToolItem toolItem = new ToolItem(3);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH / 2);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH / 2);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);
        //Subject
        columnConfig = new ColumnDefinitionConfig<Email, SimpleLink>(wfmStrings.subject(), Email.SUBJECT, 120) {
            public SimpleLink getCellValue(final Email rowValue) {
                return getEmailLink(rowValue.getObjectID(), rowValue.getSubject(), rowValue.isSeen(), rowValue.getSubject());
            }
        };
        columnConfig.setMinimumColumnWidth(120);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);
        //From
        columnConfig = new ColumnDefinitionConfig<Email, SimpleLink>(wfmStrings.fromN(), Email.FROM_EMAIL, 80) {
            public SimpleLink getCellValue(final Email rowValue) {
                return getEmailLink(rowValue.getObjectID(), rowValue.getFromEmail(), rowValue.isSeen(), rowValue.getSubject());
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);
        //To
        columnConfig = new ColumnDefinitionConfig<Email, SimpleLink>(wfmStrings.toN(), Email.TO_EMAIL, 80) {
            @Override
            public SimpleLink getCellValue(final Email rowValue) {
                return getEmailLink(rowValue.getObjectID(), rowValue.getToEmails(), rowValue.isSeen(), rowValue.getSubject());
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);
        if (folderID == null) {
            //Folder
            columnConfig = new ColumnDefinitionConfig<Email, SimpleLink>(wfmStrings.folder(), Email.FOLDER, 40) {
                @Override
                public SimpleLink getCellValue(final Email rowValue) {
                    return getEmailLink(rowValue.getObjectID(), rowValue.getFolderName(), rowValue.isSeen(), rowValue.getSubject());
                }
            };
            columnConfig.setMinimumColumnWidth(30);
            columnConfig.setColumnSortable(false);
            columnConfigs.add(columnConfig);
        }
        //Created Date
        columnConfig = new ColumnDefinitionConfig<Email, SimpleLink>(wfmStrings.createdDate(), Email.CREATED_DATE, 40) {
            public SimpleLink getCellValue(final Email rowValue) {
                return getEmailLink(rowValue.getObjectID(), rowValue.getDate() != null ? DateUtils.formatInternal(rowValue.getDate()) : wfmStrings.notAvailable(), rowValue.isSeen(), rowValue.getSubject());
            }
        };
        columnConfig.setMinimumColumnWidth(30);
        columnConfigs.add(columnConfig);
        //Fetched Date
        columnConfig = new ColumnDefinitionConfig<Email, SimpleLink>(wfmStrings.receivedDate(), Email.FETCHED_DATE, 40) {
            public SimpleLink getCellValue(final Email rowValue) {
                return getEmailLink(rowValue.getObjectID(), rowValue.getReceivedDate() != null ? DateUtils.formatInternal(rowValue.getReceivedDate()) : wfmStrings.notAvailable(), rowValue.isSeen(), rowValue.getSubject());
            }
        };
        columnConfig.setMinimumColumnWidth(30);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        return columnConfigs.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private SimpleLink getEmailLink(String emailID, String text, boolean seen, String tabName) {
        text = Optional.ofNullable(text).orElse(wfmStrings.notAvailable());
        tabName = Optional.ofNullable(tabName).orElse(wfmStrings.notAvailable());
        return new SimpleLink(seen ? text : "<b>" + text + "</b>", "email|summary/" + emailID, tabName, tabName);
    }

    private ListingRequestProvider<Email> getListingRequestProvider() {
        return (filterParametrs, callback) -> initMessageList(filterParametrs, callback, null);
    }

    private void initMessageList(ListingFilterParameter filterParametrs, ListingCallback<Email> callback, Span container) {
        if (folderID != null || (relationID != null && relationType != null)) {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            filterParametrs.setRelationID(relationID);
            filterParametrs.setRelationType(relationType);
            filterParametrs.setEmailFolderID(folderID);
            messageCenterService.getEmails(filterParametrs, new AbstractAsyncCallback<ListResult<Email>>() {
                @Override
                public void failure(Throwable throwable) {
                    if (callback != null) {
                        callback.onFailure(throwable);
                    }
                }

                @Override
                public void success(ListResult<Email> listResult) {
                    if (callback != null) {
                        callback.onSuccess(listResult);
                    }
                    statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                    if (statisticShortcut != null) {
                        if (listResult.getTotal() != null && listResult.getTotal() > 0) {
                            statisticShortcut.setText(countFormat(listResult.getTotal()));
                            statisticShortcut.setClass("tab-label");
                        } else {
                            statisticShortcut.setText("");
                            statisticShortcut.removeStyleName("tab-label");
                        }
                    }
                }
            });
        } else if (callback != null) {
            callback.onSuccess(new ListResult<>(new ArrayList<>(), 0));
        }
    }

    private boolean showRelatedEmails() {
        return relationID != null && relationType != null;
    }

    private ActionButton more;

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (relationID != null && relationType != null) {
                    ActionButton addNew = getAddNewButton();
                    addNew.ensureDebugId("inbox_compose");
                    if (relationType.equals(RelationItem.TYPE_CASE)) {
                        addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + relationID + "/" + Boolean.FALSE + "/" + Boolean.FALSE + "/" + RelationItem.TYPE_CASE + "/" + trackerID + "/" + relationName + "/" + Boolean.FALSE + "/" + Boolean.FALSE));
                    } else {
                        addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + relatedEmail + "/" + relationType + "/" + relationID + "/" + relationName));
                    }
                    return addNew;
                } else if (folderID != null) {
                    ActionButton addNew = getAddNewButton();
                    addNew.ensureDebugId("inbox_compose");
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/"));
                    return addNew;
                }
                return null;
            }
            @Override
            public ActionButton initTopToolBarMore() {
                more = new ActionButton(ActionButton.getMoreString(), "left", ActionButton.Type.TOOLMENU);
                more.ensureDebugId("email_list_more");
                more.setVisible(false);
                more.addClickHandler(clickEvent -> {
                    MenuBar menu = getMoreActions();
                    menu.setAutoOpen(true);
                    more.setMenu(menu);
                });
                return more;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(folderID != null ? wfmStrings.currentlyYouDoNotHaveAnyEmails() : wfmStrings.currentlyYouDoNotHaveAnyEmailAccounts());
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public ActionButton initializationReloadButton() {

                ActionButton fetch = new ActionButton("", "btn btn--icon");
                fetch.getElement().setId("reload_button");
                new MaterialTooltip(fetch, wfmStrings.fetchEmails()).setPosition(Position.TOP);

                fetch.addClickHandler(clickEvent -> {
//                    fetch.setEnabled(false);
                    messageCenterService.manuallyFetchEmails(folderID, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable throwable) {
//                            fetch.setEnabled(true);
                            //listingTable.reloadPage();
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_LIST_CHANGE, null, EmailListView.this);
                        }

                        @Override
                        public void success(Void result) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_LIST_CHANGE, result, EmailListView.this);
                        }
                    });
                    Info.show(wfmStrings.fetchProcessOngoing());
                });
                fetch.add(new SvgIcon(SvgEnum.rotateCw));
                return fetch;

            }
        };
    }

    private MenuBar getMoreActions() {
        if (selectedItems != null && selectedItems.size() > 0) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(false);
                actions.addMenuItem(wfmStrings.markAsRead(), null, true, () -> {
                    ArrayList<String> emailIDs = selectedItems.stream().map(Email::getObjectID).collect(Collectors.toCollection(ArrayList::new));
                    setEmailFlags(emailIDs, FLAG_READ);
                });
                actions.addMenuItem(wfmStrings.markAsUnread(), null, true, () -> {
                    ArrayList<String> emailIDs = selectedItems.stream().map(Email::getObjectID).collect(Collectors.toCollection(ArrayList::new));
                    setEmailFlags(emailIDs, FLAG_UNREAD);
                });
                actions.addMenuItem(wfmStrings.delete(), null, true, () -> {
                    ArrayList<String> emailIDs = selectedItems.stream().map(Email::getObjectID).collect(Collectors.toCollection(ArrayList::new));
                    deleteSelections(emailIDs);
                });
            }
            return actions.getMenuBar();
        } else {
            if (actionsEmpty == null) {
                actionsEmpty = new ContextMenu();
                actionsEmpty.getMenuBar().setAutoOpen(false);
                actionsEmpty.addMenuItem(wfmStrings.selectAnyItemToActivateBatchActions(), null, true, null);
            }
            return actionsEmpty.getMenuBar();
        }
    }

    private void setEmailFlags(ArrayList<String> emailIDs, String flag) {
        LoadingPanel.loading(true);
        messageCenterService.setEmailFlags(emailIDs, folderID, flag, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_LIST_CHANGE, result, EmailListView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_CHANGE_ENTITY, result, EmailListView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAILS_CLEARED, null, null);

            }
        });
    }

    private void deleteSelection(Email item) {
        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        message.setTitle(wfmStrings.warning());
        message.setMessage(wfmStrings.sureYouWantToDelete());
        message.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                messageCenterService.setEmailFlags(new ArrayList<>(Collections.singletonList(item.getObjectID())), folderID, FLAG_DELETED, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        //listingTable.reloadPage();
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_LIST_CHANGE, result, EmailListView.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_CHANGE_ENTITY, result, EmailListView.this);
                        Info.show(wfmStrings.messSuccessfulyyDeleted(), Info.Type.INFO);
                    }
                });
            }
        });
        message.open();
    }

    private void deleteSelections(ArrayList<String> emailIDs) {
        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        message.setTitle(wfmStrings.warning());
        message.setMessage(wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords());
        message.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                messageCenterService.setEmailFlags(emailIDs, folderID, FLAG_DELETED, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_LIST_CHANGE, result, EmailListView.this);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_EMAIL_CHANGE_ENTITY, result, EmailListView.this);
                        Info.show(wfmStrings.messSuccessfulyyDeleted(), Info.Type.INFO);
                    }
                });
            }
        });
        message.open();
    }

    @Override
    public String getIconStyle() {
        return "mails mail-list";
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

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        initMessageList(fp, null, container);
    }
}
