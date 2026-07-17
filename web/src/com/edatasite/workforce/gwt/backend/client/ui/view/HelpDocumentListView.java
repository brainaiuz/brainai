package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.form.HelpDocumentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Dilshod Madrahimov
 * Date: 2/27/13
 * Time: 2:51 PM
 */
public class HelpDocumentListView extends BaseListView implements Constants {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private ListingPanel<HelpDocumentItem> listing;
    private boolean isFromPartnerBackend = false;

    public HelpDocumentListView() {
        super(HELP_DOCUMENT + "View", backendStrings.helpDocuments());
    }

    public HelpDocumentListView(boolean isFromPartnerBackend) {
        super(HELP_DOCUMENT + "View", backendStrings.helpDocuments());
        this.isFromPartnerBackend = isFromPartnerBackend;
    }

    public String getIconStyle() {
        return "doc documents";
    }


    protected Widget onInitialize() {
        listing = new ListingPanel<>(ListPanelType.HelpDocumentListPanel, getColumns(), getListData(), getDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_HELP_DOCUMENT_FORM_ADD, HelpDocumentListView.this, (sender, args) -> listing.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_HELP_DOCUMENT_DELETE, HelpDocumentListView.this, (sender, args) -> listing.reloadPage());
        add(listing);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();
        //action column
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<HelpDocumentItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final HelpDocumentItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(HELP_DOCUMENT + "|add/add/" + item.getObjectID()));
                actionItemCount++;
                menuBar.addItem(edit);
                MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                delete.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    //messageBox.setSize(300, 150);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(backendStrings.areSureYouWontToDeleteDocument());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            BackendService.App.get().deleteHelpDocument(item.getObjectID(), new AsyncCallback<Boolean>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Boolean result) {
                                    if (result) {
                                        LoadingPanel.loading(false);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), backendStrings.document()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_HELP_DOCUMENT_DELETE, result, HelpDocumentListView.this);
                                    }
                                }
                            });
                        }
                    });
                    messageBox.open();
                });

                actionItemCount++;
                menuBar.addItem(delete);

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsConfigList.add(column);

        // #title column
        column = new ColumnDefinitionConfig<HelpDocumentItem, String>(wfmStrings.title(), "title", 150) {
            @Override
            public String getCellValue(HelpDocumentItem item) {
                return item.getTitle();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);
        // section column
        column = new ColumnDefinitionConfig<HelpDocumentItem, String>(wfmStrings.section(), "section", 80) {
            @Override
            public String getCellValue(HelpDocumentItem item) {
                return item.getSection();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(false);
        columnsConfigList.add(column);
        // section column
        column = new ColumnDefinitionConfig<HelpDocumentItem, String>(wfmStrings.form(), "form", 100) {
            @Override
            public String getCellValue(HelpDocumentItem item) {
                return item.getForm();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);
        // host column
        column = new ColumnDefinitionConfig<HelpDocumentItem, String>(backendStrings.host(), "hostName", 100) {
            @Override
            public String getCellValue(HelpDocumentItem item) {
                return item.getHostName();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(false);
        columnsConfigList.add(column);
        // #link column
        column = new ColumnDefinitionConfig<HelpDocumentItem, String>(wfmStrings.link(), "link", 150) {
            @Override
            public String getCellValue(HelpDocumentItem item) {
                return item.getLink();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(false);
        columnsConfigList.add(column);

        return columnsConfigList.toArray(new ColumnDefinitionConfig[columnsConfigList.size()]);
    }

    private ListingRequestProvider<HelpDocumentItem> getListData() {

        return (fp, callback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            BackendService.App.get().getHelpDocumentList(fp, new AbstractAsyncCallback<ListResult<HelpDocumentItem>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(ListResult<HelpDocumentItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(HELP_DOCUMENT + "|add/add"));
                return addNew;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(backendStrings.messCurrentlyHelpDocument());
                message.setHref(HELP_DOCUMENT + "|add/add");
                message.setTextBeforeLink(backendStrings.messYouHelpDocumentClicking());
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowCustomiseButton() {
                return false;
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };
    }

    @Override
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
