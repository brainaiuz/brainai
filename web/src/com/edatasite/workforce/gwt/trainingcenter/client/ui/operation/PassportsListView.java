package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.passport.PassportData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 10/06/14
 * Time: 17:30
 * To change this template use File | Settings | File Templates.
 */
public class PassportsListView extends BaseListView implements TCConstants {
    private static final TCStrings tcStrings = TCStrings.App.get();
    private static final WfmMessages wfmMessages = WfmMessages.App.get();
    private ListingPanel<PassportData> listingPanel;

    public PassportsListView() {
        super(TC_PASSPORT);
        setDescription(property.getPlural(tcStrings.hsePassports()));
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.PassportListPanel, getColumns(), getListingRequestProvider(), getListPanelDesign());
        add(listingPanel);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PASSPORT_SAVED, PassportsListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PASSPORT_DELETE, PassportsListView.this, (sender, args) -> listingPanel.reloadPage());
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        CustomColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[7];
        columns[0] = new ColumnDefinitionConfig<PassportData, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final PassportData rowValue) {
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
                //View passport
                final MenuPopItem passportSummary = new MenuPopItem(tcStrings.passportView(), "icon-CertificateData-view");
                passportSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_PASSPORT + "|summary/" + rowValue.getObjectID()));
                menuItemCount++;
                menuBar.addItem(passportSummary);
                //Edit CertificateData
                final MenuPopItem passportEdit = new MenuPopItem(tcStrings.editPassport(), "icon-employee-edit-profile");
                passportEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_PASSPORT + "|edit/" + rowValue.getObjectID()));
                menuItemCount++;
                menuBar.addItem(passportEdit);
                passportEdit.setVisible(Utils.hasPermission(PermissionConstants.TC_PASSPORT_EDIT));

                //Delete CertificateData
                final MenuPopItem deletePassport = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                deletePassport.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.INFO, Action.YesNo, true);
                    //message.setSize(300, 150);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            TCService.App.get().deletePassport(rowValue.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Boolean result) {
                                    if (result) {
                                        LoadingPanel.loading(false);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), tcStrings.passport()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PASSPORT_DELETE, result, PassportsListView.this);
                                    }
                                }
                            });
                        }
                    });
                    message.open();
                });
                menuItemCount++;
                menuBar.addItem(deletePassport);
                deletePassport.setVisible(Utils.hasPermission(PermissionConstants.TC_PASSPORT_DELETE));

                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();  //return action menu items
            }
        };
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<PassportData, SimpleLink>(wfmStrings.number(), PassportData.NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(PassportData rowValue) {
                return new SimpleLink((rowValue.getNumberString() != null ? rowValue.getNumberString() : "") + rowValue.getNumber(), TC_PASSPORT + "|summary/" + rowValue.getObjectID());
            }
        };
        columns[2] = new ColumnDefinitionConfig<PassportData, SimpleLink>(wfmStrings.student(), PassportData.STUDENT, 200) {
            @Override
            public SimpleLink getCellValue(PassportData rowValue) {
                return new SimpleLink(rowValue.getStudent(), TC_PASSPORT + "|summary/" + rowValue.getObjectID());
            }
        };
        columns[3] = new ColumnDefinitionConfig<PassportData, String>(wfmStrings.type(), PassportData.TYPE, 100) {
            @Override
            public String getCellValue(PassportData rowValue) {
                return rowValue.getType();
            }
        };
        columns[4] = new ColumnDefinitionConfig<PassportData, String>(wfmStrings.status(), PassportData.STATUS, 100) {
            @Override
            public String getCellValue(PassportData rowValue) {
                return rowValue.getStatus();
            }
        };
        columns[4].setColumnSortable(false);
        columns[5] = new ColumnDefinitionConfig<PassportData, String>(wfmStrings.level(), PassportData.LEVEL, 100) {
            @Override
            public String getCellValue(PassportData rowValue) {
                return rowValue.getLevel();
            }
        };
        columns[5].setColumnSortable(false);
        columns[6] = new ColumnDefinitionConfig<PassportData, String>(wfmStrings.issuedDate(), PassportData.CREATION_DATE, 100) {
            @Override
            public String getCellValue(PassportData rowValue) {
                return rowValue.getCreationDate() != null ? DateUtils.format(rowValue.getCreationDate()) : "";
            }
        };
        return columns;
    }

    private ListingPanelDesign getListPanelDesign() {
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
                if (Utils.hasPermission(PermissionConstants.TC_PASSPORT_ISSUE)) {
                    ActionButton addNewPassport = getAddNewButton();
                    addNewPassport.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged(TC_PASSPORT + "|add/add"));
                    return addNewPassport;
                }
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(tcStrings.hsePassports()));
                message.setHref(TC_PASSPORT + "|add/add");
                message.setTextBeforeLink(tcStrings.noPassportsLink());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<PassportData> getListingRequestProvider() {
        return (filterParameter, callback) -> {
            TCService.App.get().getPassportsList(filterParameter, new AbstractAsyncCallback<ListResult<PassportData>>() {
                @Override
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void success(ListResult<PassportData> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    @Override
    public String getIconStyle() {
        return "bgMark certificate-icon";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return "passport";
    }
}
