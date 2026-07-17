package com.edatasite.workforce.gwt.profile.client.ui.view.customfields;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationCFModal;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldArea;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.ui.view.CustomizableSystemFieldSideNav;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 5/28/12
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountingCustomFieldsListView extends CustomFieldsListView {
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();

    public AccountingCustomFieldsListView() {
        super("accountingcustomfields", settingsStrings.accountingCustomFields());
    }

    @Override
    public String getIconStyle() {
        return "icon-accounting-custim";
    }

    protected ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<CompanyCustomFieldItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CompanyCustomFieldItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "edit-icon");
                edit.ensureDebugId("Accounting_custom_field_edit");
                edit.setCommand(() -> {
                    if (item.isSystemField()) {
                        new CustomizableSystemFieldSideNav(item.getObjectId());
                    } else {
                        if (listing.getFilterParametrs().getCompanyID() != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|editcustomfield/" + item.getObjectId() + "/" + title + "/" + listing.getFilterParametrs().getCompanyID());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|editcustomfield/" + item.getObjectId() + "/" + title);
                        }
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

                if (!item.isSystemField()) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    delete.ensureDebugId("Accounting_custom_field_delete");
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
                                    public void success(Void aVoid) {
                                        LoadingPanel.loading(false);
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
                } else {
                    MenuPopItem active = new MenuPopItem(item.isActive() ? wfmStrings.deactivate() : wfmStrings.activate(), "edit-icon");
                    active.setCommand(() -> {
                        item.setActive(!item.isActive());
                        ProfileService.App.get().saveCustomFields(null, item, false, new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                            }

                            @Override
                            public void onSuccess(Void unused) {
                                refresh();
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.customField()));
                            }
                        });
                    });
                    actionItemCount++;
                    menuBar.addItem(active);
                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsConfigList.add(column);


        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(settingsStrings.entityName(), "entityname", 150) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return CustomFieldSection.getBySectionName(item.getEntityName()) != null ? CustomFieldSection.getBySectionName(item.getEntityName()).getTitle() : "";
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(settingsStrings.relationName(), "relationname", 150) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return item.getRelationshipName();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, SimpleLink>(wfmStrings.fieldName(), "fieldname", 150) {
            @Override
            public SimpleLink getCellValue(CompanyCustomFieldItem item) {
                if (item.isSystemField()) {
                    SimpleLink simpleLink = new SimpleLink(item.getFieldName());
                    simpleLink.addClickHandler(clickEvent -> {
                        new CustomizableSystemFieldSideNav(item.getObjectId());
                    });
                    return simpleLink;
                } else {
                    return getAsLink(item.getFieldName(), "customFieldManagement|editcustomfield/" + item.getObjectId() + "/" + title);
                }
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(wfmStrings.aliasName(), "aliasname", 150) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return item.getAliasName();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(settingsStrings.uiType(), "uitype", 100) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return item.getUiType();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(wfmStrings.dataType(), "datatype", 100) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return item.getDataType();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(false);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<CompanyCustomFieldItem, String>(wfmStrings.status(), "item-status", 100) {
            @Override
            public String getCellValue(CompanyCustomFieldItem item) {
                return item.isActive() ? wfmStrings.active() : wfmStrings.inactive();
            }
        };
        column.setMinimumColumnWidth(100);
        column.setColumnSortable(false);
        columnsConfigList.add(column);
        return columnsConfigList.toArray(new ColumnDefinitionConfig[]{});
    }

    protected ListingRequestProvider<CompanyCustomFieldItem> getListData() {
        return (filterParameter, callback) -> {
            filterParameter.setEntityName(getAccountingCFEntityNames());
            profileService.getCustomFields(filterParameter, new AbstractAsyncCallback<ListResult<CompanyCustomFieldItem>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

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
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton newLocation = getAddNewButton();

                newLocation.addClickHandler(baseEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|add/add/" + CustomFieldArea.ACCOUNTING));

                return newLocation;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, false);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
            }
        };

    }

    private String getAccountingCFEntityNames() {
        String enames = "";
        enames += "'" + ViewName.SaleInvoice.name() + "'";
        enames += ",'" + ViewName.PurchaseInvoice.name() + "'";
        enames += ",'" + ViewName.SaleQuote.name() + "'";
        enames += ",'" + ViewName.SaleOrder.name() + "'";
        enames += ",'" + ViewName.PurchaseOrder.name() + "'";
        enames += ",'" + ViewName.ExpenceReportView.name() + "'";
        enames += ",'" + ViewName.ProductCategory.name() + "'";
        enames += ",'" + ViewName.ProductCategoryStoreFront.name() + "'";
        enames += ",'" + ViewName.ProductServiceView.name() + "'";
        enames += ",'" + ViewName.FixedAsset.name() + "'";
        enames += ",'" + ViewName.BatchInvoicePaymentView.name() + "'";
        enames += ",'" + ViewName.BatchPayBillView.name() + "'";
        enames += ",'" + ViewName.BankAccounts.name() + "'";
        enames += ",'" + ViewName.BankTransferList.name() + "'";
        enames += ",'" + ViewName.RequestForQuote.name() + "'";
        enames += ",'" + ViewName.RequestForPurchase.name() + "'";
        enames += ",'" + ViewName.ExpenceReportViewSystem.name() + "'";
        enames += ",'" + ViewName.SaleInvoiceSystem.name() + "'";
        enames += ",'" + ViewName.PurchaseInvoiceSystem.name() + "'";
        enames += ",'" + ViewName.PurchaseOrderSystem.name() + "'";
        enames += ",'" + ViewName.BatchPayBillViewSystem.name() + "'";
        enames += ",'" + ViewName.SaleOrderSystem.name() + "'";
        enames += ",'" + ViewName.SaleQuoteSystem.name() + "'";
        enames += ",'" + ViewName.RentalProductsView.name() + "'";
        enames += ",'" + ViewName.RentalOrdersView.name() + "'";
        return enames;
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
