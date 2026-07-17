package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.FeatureConstants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/4/11
 * Time: 9:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetRegisterListView extends BaseListView implements Constants, FeatureConstants, PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private ListingPanel list;
    private final String fixetAsset = "fixet_Asset";

    public FixedAssetRegisterListView() {
        super(FIXED_ASSETS);
        setDescription(property.getPlural(wfmStrings.fixedAssets()));
        if (Utils.hasPermission(ACCOUNTING_FIXED_ASSET_ADD)) {
            setAddNew("fixedasset|add/add");
        }
    }

    @Override
    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.FixedAssetRegisterListPanel, getColumns(), getListProvider(), getListDesign());

        list.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveFixedAssetCellValue((FixedAssetItem) rowValue, columnCodeName));

        list.setExcelListener(clickEvent -> {
            ListingFilterParameter fp = list.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            String ExcelURL = CommandConstants.COMMON_URL + "/downloadfixedAssetListExcelHandler";
            list.callListExcel(ExcelURL, fp);
        });
        list.setPDFListener(clickEvent -> {
            ListingFilterParameter fp = list.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            String pdfURL = CommandConstants.PDF_URL + "/fixedAssetListPDFHandler";
            list.callListPDF(pdfURL, fp);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FIXED_ASSET_SAVED, FixedAssetRegisterListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.PI_DELETE_FIXEDASSET_RELOAD, FixedAssetRegisterListView.this, (sender, args) -> list.reloadPage());

        add(list);
        return null;
    }

    private void saveFixedAssetCellValue(FixedAssetItem rowValue, String columnCodeName) {
        boolean hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(rowValue.getCreationDate().getNonConvertedDate()));
        if (!hasAccountingBeforeBlockDate) {
            FixedAssetService.App.get().saveFixedAssetCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            });
        } else {
            Info.warn(wfmStrings.youDontHavePermission());
            list.reloadPage();
        }
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(ACCOUNTING_FIXED_ASSET_ADD) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("fixedasset|add/add") : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return ChooseFilter.INVOICE_FILTER;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
                        int count = 8;
                        if (AccountingUtils.get().isEnableAccountingDepartmentRelation()) {
                            count++;
                        }
                        ArrayList<String> fields = new ArrayList<>(count);
                        fields.add(ListingChooseFilter.CATEGORY_ASSET);
//                        fields.add(ListingChooseFilter.ACCOUNT);
                        fields.add(ListingChooseFilter.FROM_DATE);
                        fields.add(ListingChooseFilter.TO_DATE);
                        fields.add(ListingChooseFilter.FROM_AMOUNT);
                        fields.add(ListingChooseFilter.TO_AMOUNT);
                        fields.add(ListingChooseFilter.LOCATION);
                        fields.add(ListingChooseFilter.CREATOR);
                        if (AccountingUtils.get().isEnableAccountingDepartmentRelation()) {
                            fields.add(ListingChooseFilter.DEPARTMENT_ASSET);
                        }
                        fields.add(ListingChooseFilter.CALCULATE_DEPRECIATION);
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.FixedAssetRegister;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = null;
                if (Utils.hasPermission(ACCOUNTING_FIXED_ASSET_ADD)) {
                    addNew = getAddNewButton();
                    addNew.ensureDebugId(fixetAsset + "addNewButton");
                    addNew.addClickHandler(e -> SinksContainerFactory.entryPoint.onHistoryChanged("fixedasset|add/add", wfmStrings.add() + " " + property.getSingular(wfmStrings.fixedAsset())));
                }
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarMoreActions() {
                ActionButton updateDepreciation = null;
                if (Utils.hasPermission(ACCOUNTING_FIXED_ASSET_DEPRECIATION_UPDATE)) {
                    updateDepreciation = new ActionButton(accountingStrings.updateDepreciation(), ActionButton.Type.BUTTON);
                    updateDepreciation.ensureDebugId(fixetAsset + "updateDepreciation");
                    updateDepreciation.addClickHandler((ClickEvent e) -> {
                        if (AccountingUtils.get().isEnableDepreciationDatePeriod()) {
                            new UpdateDepreciationPopup().center();
                        } else {
                            LoadingPanel.loading(true);
                            FixedAssetService.App.get().sendToUpdateDeprecationMQ(null, new AsyncCallback<Boolean>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Boolean hasInProgressDeprication) {
                                    LoadingPanel.loading(false);
                                    if (hasInProgressDeprication) {
                                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK);
                                        messageBox.setTitle(wfmStrings.information());
                                        messageBox.setMessage(wfmStrings.depricationisInProgressPleaseSeeImportLog(), wfmStrings.updateingAndNotification());
                                        messageBox.open();
                                    } else {
                                        Info.show(wfmStrings.updateingAndNotification(), Info.Type.INFO);
                                    }
                                }
                            });
                        }
                    });
                }

                return updateDepreciation;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }


            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(accountingStrings.currentlyYouHaveNotFixedAssets(), wfmStrings.fixedAssets()));
                if (Utils.hasPermission(ACCOUNTING_FIXED_ASSET_ADD)) {
                    message.setTextBeforeLink(property.getPlural(accountingStrings.youCanStartAddingFixedAssets(), wfmStrings.fixedAssets()));
                    message.setHref("fixedasset|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.ACCOUNTING_FIXED_ASSET_EDIT);
            }
        };
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<ColumnDefinitionConfig> columnList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<FixedAssetItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final FixedAssetItem item) {
                boolean hasAccountingBeforeBlockDate = (Utils.isBankingLocked() && DateUtils.getTransactionLockDate().after(item.getCreationDate().getNonConvertedDate()));

                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem summaryView = new MenuPopItem(wfmStrings.summaryView(), "icon-task-small");
                summaryView.ensureDebugId("summaryView");
                summaryView.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("fixedasset|summary/" + item.getObjectID(), item.getCode()));
                actionItemCount++;
                menuBar.addItem(summaryView);

                if (Utils.hasPermission(ACCOUNTING_FIXED_ASSET_EDIT) && !hasAccountingBeforeBlockDate && !item.getDisposed()) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    edit.ensureDebugId("edit");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("fixedasset|edit/" + item.getObjectID(), item.getCode()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }
                if (Utils.hasPermission(ACCOUNTING_FIXED_ASSET_DELETE) && !hasAccountingBeforeBlockDate) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    delete.ensureDebugId("delete");
                    delete.setCommand(() -> {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.YesNo);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                FixedAssetService.App.get().deleteFixedAsset(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                    }

                                    @Override
                                    public void success(Void aVoid) {
                                        LoadingPanel.loading(false);
                                        list.reloadPage();
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.FIXEDASSET_DELETE_PI_LIST_RELOAD, null, FixedAssetRegisterListView.this);
                                        removeDeletedTab("fixedasset" + item.getObjectID());
                                    }
                                });
                            }
                        });
                        messageBox.center();
                    });
                    actionItemCount++;
                    menuBar.addItem(delete);
                }

                if (!item.getDisposed()) {
                    MenuPopItem dispose = new MenuPopItem(wfmStrings.dispose(), "icon-remove-storefront");
                    dispose.ensureDebugId("dispose");
                    dispose.setCommand(() -> new FixedAssetDisposeDialogBox(item, () -> list.reloadPage()).open());
                    actionItemCount++;
                    menuBar.addItem(dispose);
                }

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, SimpleLink>(property.getShortForNumber(wfmStrings.number()), FixedAssetItem.CODE, 100) {

            @Override
            public SimpleLink getCellValue(FixedAssetItem item) {
                SimpleLink label = new SimpleLink(item.getCode() != null ? item.getCode() : "");
                if (item.getCode() != null) {
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("fixedasset|summary/" + item.getObjectID(), item.getCode(), item.getName()));
                }
                return label;

            }
        };
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(wfmStrings.name(), FixedAssetItem.NAME, 220) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.getName() != null ? item.getName() : "";
            }
        };
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(wfmStrings.owner(), FixedAssetItem.OWNER, 180) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.getOwner() != null ? item.getOwner().getName() : "N/A";
            }
        };
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(wfmStrings.purchaseDate(), FixedAssetItem.DATE, 100) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.getCreationDate() != null ? DateUtils.format(item.getCreationDate()) : "";
            }
        };
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(wfmStrings.cost(), FixedAssetItem.COST, 100) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.getCost() != null ? AccountingUtils.get().formatPrice(item.getCost()) : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, SimpleLink>(wfmStrings.category(), FixedAssetItem.CATEGORY, 170) {

            @Override
            public SimpleLink getCellValue(final FixedAssetItem item) {
                SimpleLink label = new SimpleLink(item.getAccount().getName() != null ? item.getAccount().getName() : "");
                if (item.getAccount().getName() != null) {
                    label.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("fixedasset|summary/" + item.getObjectID(), item.getCode()));
                }
                return label;
            }
        };
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(wfmStrings.residualValue(), FixedAssetItem.RESIDUALVALUE, 100) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.getResidualValue() != null ? AccountingUtils.get().formatPrice(item.getResidualValue()) : "";
            }
        };
        column.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        columnList.add(column);
        column.setShow(false);

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(wfmStrings.useFulLife(), FixedAssetItem.ASSETLIFE, 75) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.getUsefulLife() != null ? String.valueOf(item.getUsefulLife()) : "";
            }
        };
        column.setShow(false);
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(accountingStrings.accumulatedDepreciationAccount(), CustomFormConstants.FIXED_ASSET_ACCOUNT, 150) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.getFixedAssetAccount() != null ? item.getFixedAssetAccount().getName() != null ? item.getFixedAssetAccount().getName() : "" : "";
            }
        };
        column.setShow(false);
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(accountingStrings.depreciationExpenseAccount(), CustomFormConstants.EXPENSE_ACCOUNT, 150) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.getExpenseAccount() != null ? item.getExpenseAccount().getName() != null ? item.getExpenseAccount().getName() : "" : "";
            }
        };
        column.setShow(false);
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), FixedAssetItem.LOCATION, 150) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.getLocationName() != null ? item.getLocationName() : "";
            }
        };
        column.setShow(false);
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(wfmStrings.account(), FixedAssetItem.ACCOUNT, 150) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.getFinancedByAccount().getName() != null ? item.getFinancedByAccount().getName() : "";
            }
        };
        column.setShow(false);
        columnList.add(column);

        if (AccountingUtils.get().isEnableAccountingDepartmentRelation()) {
            column = new ColumnDefinitionConfig<FixedAssetItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), FixedAssetItem.DEPARTMENT, 150) {

                @Override
                public String getCellValue(FixedAssetItem item) {
                    return item.getDepartment() != null && item.getDepartment().getName() != null ? item.getDepartment().getName() : "";
                }
            };
            column.setShow(false);
            columnList.add(column);
        }

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(wfmStrings.calculateDepreciation(), FixedAssetItem.CALCULATE_DEPRECIATION, 150) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.isCalculateDepreciation() ? "Yes" : "No";
            }
        };
        column.setShow(false);
        columnList.add(column);

        column = new ColumnDefinitionConfig<FixedAssetItem, String>(wfmStrings.status(), FixedAssetItem.STATUS, 150) {

            @Override
            public String getCellValue(FixedAssetItem item) {
                return item.getDisposed() ? accountingStrings.disposed() : wfmStrings.active();
            }
        };
        column.setShow(false);
        columnList.add(column);

        return columnList.toArray(new ColumnDefinitionConfig[]{});
    }

    private ListingRequestProvider<FixedAssetItem> getListProvider() {
        return (filterParametrs, listingCallback) -> {
            getFilterRequestParam(filterParametrs);
            FixedAssetService.App.get().getFixedAssets(filterParametrs, new AsyncCallback<ListResult<FixedAssetItem>>() {


                @Override
                public void onFailure(Throwable throwable) {
                    listingCallback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<FixedAssetItem> fixedAssetItemListResult) {
                    listingCallback.onSuccess(fixedAssetItemListResult);//To change body of implemented methods use File | Settings | File Templates.
                }
            });
        };
    }

    @Override
    public String getIconStyle() {
        return "accountMark fixed-asset-reg-list";
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
    public String getPropertyCode() {
        return Constants.FIXED_ASSETS;
    }

    private HashMap<String, String> getFilterRequestParam(ListingFilterParameter filterParametrs) {
        if (filterParametrs != null) {
            filterParametrs.setFacetFilterJson(Utils.facetFilterRpcToJsonString(filterParametrs.getFacetFilter()));
            filterParametrs.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(filterParametrs.getListPanelTool()));
            if (filterParametrs.getStartDate() != null && filterParametrs.getEndDate() != null) {
                DateNonConvertable fromDate = new DateNonConvertable(DateUtil.resetTime(filterParametrs.getStartDate()));
                DateNonConvertable toDate = new DateNonConvertable(DateUtil.getDayLastTime(filterParametrs.getEndDate()));
                filterParametrs.setStartDateWithoutOffset(fromDate.getNonConvertedDate());
                filterParametrs.setEndDateWithoutOffset(toDate.getNonConvertedDate());
            }
            return filterParametrs.getRequestParams();
        }
        return null;
    }

}
