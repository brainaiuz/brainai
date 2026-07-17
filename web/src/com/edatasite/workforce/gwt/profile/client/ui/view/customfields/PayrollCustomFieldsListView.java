package com.edatasite.workforce.gwt.profile.client.ui.view.customfields;

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
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
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
 * Created by Ilhom Lutfullaev on 17.10.2017.
 */
public class PayrollCustomFieldsListView extends CustomFieldsListView {

    public PayrollCustomFieldsListView() {
        super("payrollcustomfields", settingsStrings.payrollCustomFields());
    }

    @Override
    public String getIconStyle() {
        return "icon-accounting-custim";
    }

    protected ColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<CompanyCustomFieldItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CompanyCustomFieldItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "edit-icon");
                edit.ensureDebugId("Payroll_Custom_field_edit");
                edit.setCommand(() -> {
                    if (listing.getFilterParametrs().getCompanyID() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|editcustomfield/" + item.getObjectId() + "/" + title + "/" + listing.getFilterParametrs().getCompanyID());
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|editcustomfield/" + item.getObjectId() + "/" + title);
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
                delete.ensureDebugId("Payroll_custom_fiels_delete");
                delete.setCommand(() -> {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
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

                ToolItem toolItem = new ToolItem(actionItemCount);
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
                return getAsLink(item.getFieldName(), "customFieldManagement|editcustomfield/" + item.getObjectId() + "/" + title);
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

        return columnsConfigList.toArray(new ColumnDefinitionConfig[]{});
    }

    protected ListingRequestProvider<CompanyCustomFieldItem> getListData() {
        return (filterParameter, callback) -> {
            filterParameter.setEntityName(getPayrollCFEntityNames());
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
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton newLocation = getAddNewButton();

                newLocation.addClickHandler(baseEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("customFieldManagement|add/add/" + CustomFieldArea.PAYROLL));

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

            @Override
            public boolean isShowResetButton() {
                return false;
            }
        };

    }

    private String getPayrollCFEntityNames() {
        String enames = "";
        enames += "'" + ViewName.SinglePayrun.name() + "'";
        enames += ",'" + ViewName.CashAdvanceList.name() + "'";
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
