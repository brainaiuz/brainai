package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BrandItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
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
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 17, 2010
 * Time: 3:18:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrandsListView extends BaseListView implements Constants, PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private ListingPanel<BrandItem> list;
    private final boolean hasListAndAddPermission = Utils.hasPermission(ACCOUNTING_BRANDS_LIST);

    public BrandsListView() {
        super("brandsList", accountingStrings.brands());
        if (hasListAndAddPermission) {
            setAddNew("brand|add/add");
        }
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.BrendsListPanel, drawColumns(), getListingRequestProvider(), getListingPanelDesign());
        list.getXlsVersion().setVisible(false);

        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/brandsListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            list.callListPDF(pdfURL, filterParametrs);
        });

        /*list.setExcelListener(new SelectionListener(){
            public void widgetSelected(BaseEvent be) {
                String excelURL = CommandConstants.COMMON_URL + "/downloadUnitMeasurementsListExcel";
                ListingFilterParameter filterParametrs = list.getFilterParametrs();
                list.callListExcel(excelURL,filterParametrs);
            }
        });*/


        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BRAND_SAVED, BrandsListView.this, (sender, args) -> list.reloadPage());

        add(list);
        list.reloadPage();
        return null;
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;/*new ListingFacetFilter() {
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
                        return ListingChooseFilter.TASK;
                    }
                };*/
            }

//            @Override
//            public void initTopToolBarWidgets(HorizontalPanel topPanel) {
//                if ((Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN))) {
//                    ToolItem newLocation = new ToolItem(Style.PUSH);
//                    newLocation.setText(inventoryStrings.addBrand());
//                    newLocation.setIconStyle("icon-accounting-suppliers");
//                    newLocation.addSelectionListener(new SelectionListener() {
//                        public void widgetSelected(BaseEvent baseEvent) {
//                            SinksContainerFactory.entryPoint.onHistoryChanged("brand|add/add");
//                        }
//                    });
//                    topPanel.add(newLocation);
//                }

//            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasListAndAddPermission) {
                    final ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("brand|add/add"));
                    return addNew;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.BRAND, null);
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("importbrand|add/add/" + imp.getObjectId());
                    }
                });

                ImportFileActionLink link = new ImportFileActionLink();
                link.addClickHandler(ch -> imp.open());
                menuContainer.add(link);
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyThereAreNoBrands());
                if (hasListAndAddPermission) {
                    message.setTextBeforeLink(accountingStrings.youCanStartAddingBrands());
                    message.setHref("brand|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<BrandItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> AccountingService.App.get().getBrandsList(filterParametrs, new AbstractAsyncCallback<ListResult<BrandItem>>() {
            public void failure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void success(ListResult<BrandItem> list) {
                callback.onSuccess(list);
            }
        });
    }

    private ColumnDefinitionConfig[] drawColumns() {

        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[4];
        columns[0] = new ColumnDefinitionConfig<BrandItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final BrandItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem brandEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                brandEdit.getElement().setId("Brand_edit_button");
                brandEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("brand|edit/" + item.getId(), item.getName()));
                actionItemCount++;
                menuBar.addItem(brandEdit);

                /*MenuItem pdfVersion = new MenuItem(Style.PUSH);
                pdfVersion.setIconStyle("icon-pdf-profile");
                pdfVersion.setText("Export to PDF");
                pdfVersion.addSelectionListener(new SelectionListener() {
                    public void widgetSelected(BaseEvent be) {
                        String pdfURL = CommandConstants.PDF_URL + "/unitMeasurementsViewPDFHandler";
                        RequestObject requestObject = new RequestObject(item.getObjectID());
                        list.callItemPDF(pdfURL, requestObject);
                    }
                });
                actions.add(pdfVersion);*/
                MenuPopItem deleteBrand = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                deleteBrand.getElement().setId("Brand_delete_button");
                deleteBrand.setCommand(() -> {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            AccountingService.App.get().deleteBrand(item.getId(), new AsyncCallback<Boolean>() {
                                public void onFailure(Throwable throwable) {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    GWT.log(throwable.getMessage());
                                }

                                public void onSuccess(Boolean deleted) {
                                    if (deleted == null) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    } else if (deleted) {
                                        Info.show(property.getPlural(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.brand()), Info.Type.INFO);
                                        list.reloadPage();
                                    } else {
                                        Info.show(accountingStrings.youCannotDeleteBrand(), Info.Type.WARNING);
                                    }
                                }
                            });
                        }
                    });
                    message.open();
                });
                actionItemCount++;
                if (Utils.hasRole(ADMIN)) {
                    menuBar.addItem(deleteBrand);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<BrandItem, String>(wfmStrings.name(), BrandItem.NAME, 100) {

            @Override
            public String getCellValue(BrandItem item) {
                return item.getName();
            }
        };

        columns[2] = new ColumnDefinitionConfig<BrandItem, String>(wfmStrings.description(), BrandItem.DESCRIPTION, 100) {

            @Override
            public String getCellValue(BrandItem item) {
                return item.getDescription();
            }
        };

        columns[3] = new ColumnDefinitionConfig<BrandItem, String>(accountingStrings.parentBrand(), BrandItem.PARENT, 100) {

            @Override
            public String getCellValue(BrandItem item) {
                return item.getParentBrandName();
            }
        };

        return columns;
    }

    public String getIconStyle() {
        return "accountMark ac-type-num-settings";//return "icon-settings-invoice";
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
