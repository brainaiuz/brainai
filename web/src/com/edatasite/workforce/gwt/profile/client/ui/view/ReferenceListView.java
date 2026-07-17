package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 7/22/11
 * Time: 11:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReferenceListView extends BaseListView {
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();
    protected ListingPanel<ReferenceItem> listing;
    private String parentCode = null;

    public ReferenceListView() {
        super("referenceList", wfmStrings.referencces());
    }

    public ReferenceListView(String parentCode) {
        super("referenceList", wfmStrings.leaveReasons());
        this.parentCode = parentCode;
    }

    protected Widget onInitialize() {
        listing = new ListingPanel<>(ListPanelType.ReferenceListPanel, getColumns(), getListData(), getDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REFERENCE_ADD, ReferenceListView.this, (sender, args) -> listing.reloadPage());
        add(listing);
        return null;
    }

    protected ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<ReferenceItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ReferenceItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);


                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.getElement().setId("edit");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("reference|edit/" + item.getObjectID() + "/" + parentCode, item.getName(), item.getName()));
                actionItemCount++;
                menuBar.addItem(edit);


                if (item.isCanDelete() && Utils.hasPermission(PermissionConstants.REFERENCE_DELETE)) {
                    MenuPopItem deletePage = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deletePage.getElement().setId("Leave_reason_delete_button");
                    deletePage.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage(wfmMessages.sureYouWantToDelete(item.getName(), "?"));
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                allInOneService.deleteReference(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.warn(wfmStrings.youCannotDeleteThisItem());
                                    }

                                    @Override
                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        listing.reloadPage();
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(deletePage);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<ReferenceItem, SimpleLink>(wfmStrings.name(), ReferenceItem.NAME, 100) {
            @Override
            public SimpleLink getCellValue(ReferenceItem item) {
                SimpleLink link = new SimpleLink(item.getName());
                link.addClickHandler(click -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("reference|edit/" + item.getObjectID() + "/" + parentCode, item.getName(), item.getName());
                });
                return link;
            }
        };
        column.setMinimumColumnWidth(50);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<ReferenceItem, String>(wfmStrings.description(), ReferenceItem.DESCRIPTION, 150) {
            @Override
            public String getCellValue(ReferenceItem item) {
                return item.getTextDescription();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<ReferenceItem, String>(wfmStrings.code(), ReferenceItem.CODE, 100) {
            @Override
            public String getCellValue(ReferenceItem item) {
                return item.getCode();
            }
        };
        column.setMinimumColumnWidth(50);
        columnsConfigList.add(column);

        if (Constants.LEAVE_REQUEST_TYPE.equals(parentCode)) {
            column = new ColumnDefinitionConfig<ReferenceItem, String>(wfmStrings.active(), "active", 50) {
                @Override
                public String getCellValue(ReferenceItem item) {
                    return item.isActive() ? wfmStrings.yes() : wfmStrings.no();
                }
            };
            column.setMinimumColumnWidth(30);
            column.setColumnSortable(false);
            columnsConfigList.add(column);

            column = new ColumnDefinitionConfig<ReferenceItem, String>(wfmStrings.shortName(), "shortName", 50) {
                @Override
                public String getCellValue(ReferenceItem item) {
                    return item.getShortName() != null ? item.getShortName() : wfmStrings.notAvailable();
                }
            };
            column.setMinimumColumnWidth(30);
            columnsConfigList.add(column);

            column = new ColumnDefinitionConfig<ReferenceItem, String>(settingsStrings.quickAddLR(), "quickAddLR", 50) {
                @Override
                public String getCellValue(ReferenceItem item) {
                    return item.isAttendanceLR() ? wfmStrings.yes() : wfmStrings.no();
                }
            };
            column.setMinimumColumnWidth(30);
            columnsConfigList.add(column);
        }

        return columnsConfigList.toArray(new ColumnDefinitionConfig[]{});
    }

    protected ListingRequestProvider<ReferenceItem> getListData() {
        return (filterParameter, callback) -> {
            filterParameter.setColumnCode(parentCode);
            allInOneService.getDeletableReferences(filterParameter, new AbstractAsyncCallback<ListResult<ReferenceItem>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(ListResult<ReferenceItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    protected ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {

                ActionButton newProjectItem = getAddNewButton();
                newProjectItem.addClickHandler(event -> {
                    if (parentCode != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("reference|edit/" + parentCode);
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("reference|referenceadd/add");
                    }
                });
                return newProjectItem;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
            }

            @Override
            public boolean isShowCustomiseButton() {
                return true;
            }

            @Override
            public boolean isShowResetButton() {
                return true;
            }
        };

    }

    @Override
    public String getIconStyle() {
        return null;
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
