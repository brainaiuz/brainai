package com.edatasite.workforce.gwt.profile.client.ui.view.customfields;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;

/**
 * User: Normurod Buriev
 * Date: 7/22/11
 * Time: 11:22 AM
 */
public class CustomFieldsListView extends BaseListView {

    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final ProfileServiceAsync profileService = ProfileService.App.get();
    protected String title;
    protected ListingPanel<CompanyCustomFieldItem> listing;
    protected LocalizationCFModal localizationCFModal;

    public CustomFieldsListView() {
        super("customfields", settingsStrings.customFieldsManager());
    }

    public CustomFieldsListView(String name, String description) {
        super(name, description);
        this.title = name;
    }

    @Override
    public String getIconStyle() {
        return "accountMark icon-custom-field";
    }

    @Override
    protected Widget onInitialize() {
        listing = new ListingPanel<>(getPanelType(), getColumns(), getListData(), getDisagn());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CUSTOM_FIELD_ADD, CustomFieldsListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DELETE_ABSTRACTADDCUSTOMFIELDSVIEW, CustomFieldsListView.this, (sender, args) -> refresh());
        add(listing);
        return null;
    }

    protected void refresh() {
        listing.reloadPage();
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.CustomFieldsListPanel;
    }

    protected ColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();
        //action
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<CompanyCustomFieldItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CompanyCustomFieldItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit-subscriptions");
                edit.ensureDebugId("edit");
                edit.setCommand(() -> {
                    if (listing.getFilterParametrs().getCompanyID() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|editcustomfield/" + item.getObjectId() + "/" + title + "/" + listing.getFilterParametrs().getCompanyID(), item.getFieldName());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|editcustomfield/" + item.getObjectId() + "/" + title, item.getFieldName());
                    }
                });
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem localization = new MenuPopItem(wfmStrings.localization());
                localization.ensureDebugId("localization-button");
                localization.setCommand(() -> {
                    localizationCFModal = new LocalizationCFModal(item.getObjectId(), LocalizationTypeEnum.FIELD);
                    localizationCFModal.center();
                });
                actionItemCount++;
                menuBar.addItem(localization);

                MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                delete.ensureDebugId("delete");
                delete.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.confirmation());
                    messageBox.setMessage(settingsStrings.areYouSureWantRemoveCustomField());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            profileService.deleteCustomField(item.getObjectId(), listing.getFilterParametrs().getCompanyID(), new AbstractAsyncCallback<Void>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    throwable.printStackTrace();
                                }

                                @Override
                                public void success(Void result) {
                                    LoadingPanel.loading(false);
                                    Info.show(settingsStrings.yourCustomFieldHasBeenDeleted(), Info.Type.INFO);
                                    Timer timer = new Timer() {
                                        @Override
                                        public void run() {
                                            refresh();
                                        }
                                    };
                                    timer.schedule(1000);
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                actionItemCount++;
                menuBar.addItem(delete);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsConfigList.add(column);
        //entity name
        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(settingsStrings.entityName(), "entityname", 150) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return CustomFieldSection.getBySectionName(item.getEntityName())!=null?CustomFieldSection.getBySectionName(item.getEntityName()).getTitle():"";
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);
        //field name
        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, SimpleLink>(wfmStrings.fieldName(), "fieldname", 150) {
            @Override
            public SimpleLink getCellValue(CompanyCustomFieldItem item) {
                return getAsLink(item.getFieldName(),"customFieldManagement|editcustomfield/" + item.getObjectId() + "/" + title);
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);
        //alias name
        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(wfmStrings.aliasName(), "aliasname", 150) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return item.getAliasName();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);
        //ui type
        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(settingsStrings.uiType(), "uitype", 100) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return item.getUiType();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);
        //data type
        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(wfmStrings.dataType(), "datatype", 100) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return item.getDataType();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(false);
        columnsConfigList.add(column);
        //created date
        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(wfmStrings.createdDate(), "creationDate", 80) {
            @Override
            public String getCellValue(CompanyCustomFieldItem rowValue) {
                return rowValue.getCreationDate() != null ? DateUtils.formatInternal(rowValue.getCreationDate()) : "";
            }
        };
        column.setMinimumColumnWidth(70);
        column.setColumnSortable(false);
        columnsConfigList.add(column);
        //created by
        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(wfmStrings.createdBy(), "createdBy", 80) {
            @Override
            public String getCellValue(CompanyCustomFieldItem rowValue) {
                return rowValue.getCreatedBy() != null ? rowValue.getCreatedBy() : "";
            }
        };
        column.setMinimumColumnWidth(70);
        column.setColumnSortable(false);
        columnsConfigList.add(column);
        //last updated date
        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(wfmStrings.modifiedDate(), "lastUpdatedDate", 80) {
            @Override
            public String getCellValue(CompanyCustomFieldItem rowValue) {
                return rowValue.getLastUpdatedDate() != null ? DateUtils.formatInternal(rowValue.getLastUpdatedDate()) : "";
            }
        };
        column.setMinimumColumnWidth(70);
        column.setColumnSortable(false);
        columnsConfigList.add(column);
        //last updated by
        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(wfmStrings.modifiedBy(), "lastUpdatedBy", 80) {
            @Override
            public String getCellValue(CompanyCustomFieldItem rowValue) {
                return rowValue.getLastUpdatedBy() != null ? rowValue.getLastUpdatedBy() : "";
            }
        };
        column.setMinimumColumnWidth(70);
        column.setColumnSortable(false);
        columnsConfigList.add(column);

        return columnsConfigList.toArray(new ColumnDefinitionConfig[]{});
    }

    protected ListingRequestProvider<CompanyCustomFieldItem> getListData() {
        return (filterParameter, callback) -> {
            profileService.getCustomFields(filterParameter, new AbstractAsyncCallback<ListResult<CompanyCustomFieldItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<CompanyCustomFieldItem> customfields) {
                    callback.onSuccess(customfields);
                }
            });
        };
    }

    protected ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton newProjectItem = getAddNewButton();
                newProjectItem.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|add/add/null"));
                return newProjectItem;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
            }

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };

    }

    protected void initEditAction() {
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    protected SimpleLink getAsLink(String name, final String linkHref) {
        if (name == null) {
            name = "";
        }
        SimpleLink link = new SimpleLink(name);
        String finalName = name;
        link.addClickHandler(event -> {
            if (listing.getFilterParametrs().getCompanyID() != null){
                SinksContainerFactory.entryPoint.onHistoryChanged(linkHref + "/" + listing.getFilterParametrs().getCompanyID());
            }
            else {
                SinksContainerFactory.entryPoint.onHistoryChanged(linkHref, finalName);
            }
        });
        return link;
    }

}
