package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductCategoryItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
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
import gwt.material.design.client.ui.html.Span;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 3, 2010
 * Time: 8:03:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoriesListView extends BaseListView implements Constants, PermissionConstants {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private ListingPanel<ProductCategoryItem> list;
    private Integer storefrontID;
    private Integer parentID;
    private final boolean hasListAndAddPermission = Utils.hasPermission(ACCOUNTING_PRODUCT_CATEGORIES_LIST);

    public ProductCategoriesListView() {
        super("productCategoriesList", accountingStrings.productCategories());
        if (hasListAndAddPermission) {
            setAddNew("productcategory|add/add");
        }
    }

    public ProductCategoriesListView(final Integer parentID) {
        super("productCategoriesList", wfmStrings.categories());

        if (parentID != null) {
            this.parentID = parentID;
        }
        if (hasListAndAddPermission) {
            setAddNew("productcategory|add/add");
        }
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.ProductCategoryListPanel, getColumns_cfg(), getDataList(), getDesign());
        list.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveProductCategoryEditCellValue((ProductCategoryItem) rowValue, columnCodeName));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PRODUCTCATEGORY_SAVED, this, (sender, args) -> list.reloadPage());
        list.setPDFListener(clickEvent -> {
            final String pdfURL = CommandConstants.PDF_URL + "/productCategoriesListPDFHandler";
            list.callListPDF(pdfURL, list.getFilterParametrs());
        });
        list.setExcelListener(clickEvent -> {
            final String excelURL = CommandConstants.COMMON_URL + "/downloadProductCategoriesExcel";
            list.callListExcel(excelURL, list.getFilterParametrs());
        });
        add(list);
        return null;
    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-type-num-settings";//return "icon-settings-invoice";
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callBack) -> {
                            AccountingService.App.get().getProductCatecorFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    callBack.onFailure(throwable);
                                }

                                @Override
                                public void success(FacetFilterRpc result) {
                                    callBack.onSuccess(result);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }


            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = null;
                if (hasListAndAddPermission) {
                    addNew = getAddNewButton();
                    addNew.addClickHandler(clickEvent -> {
                        if (parentID != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("productcategory|add/add/" + parentID);
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("productcategory|add/add");
                        }
                    });
                }
                return addNew;
            }

            @Override
            public void initImportExportToolBarWidgets(final ExportImportOption exportOption, final MaterialDropDown menuContainer) {
                final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.PRODUCT_CATEGORIES, null);
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("importproductcategories|add/add/" + imp.getObjectId());
                    }
                });

                ImportFileActionLink link = new ImportFileActionLink();
                link.addClickHandler(ch -> imp.open());
                menuContainer.add(link);
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(final ListingEmptyDataInitializer emptyDataTable) {
                final DefaultNoItemsMessage message = new DefaultNoItemsMessage(accountingStrings.currentlyThereAreNoProductCategories());
                if (hasListAndAddPermission) {
                    message.setTextBeforeLink(accountingStrings.youCanStartAddingProductCategories());
                    message.setHref("productcategory|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return true;
            }
        };
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(2, wfmStrings.productCategory());

        contentConfigure.addContentConfigure(FacetContentType.ProductsCategoriesFacetFilter.getContentCode()[1], wfmStrings.parent(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return "parent";
            }

            @Override
            public String getSolrFacetFieldName() {
                return "parent";
            }
        });
        contentConfigure.setDatePeriodPanelEnabled(false);
        return contentConfigure;
    }


    private ListingRequestProvider<ProductCategoryItem> getDataList() {
        return (filterParametrs, callback) -> {
            initDataProvider(filterParametrs, callback, null);
        };
    }

    private void initDataProvider(ListingFilterParameter filterParametrs, ListingCallback<ProductCategoryItem> callback, Span container) {
        filterParametrs.setStorefrontID(storefrontID);
        filterParametrs.setParentID(parentID);
        AccountingService.App.get().getProductCategoriesList(filterParametrs, new AsyncCallback<ListResult<ProductCategoryItem>>() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess(ListResult<ProductCategoryItem> list) {
                if (callback != null) {
                    callback.onSuccess(list);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (list.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(list.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private ColumnDefinitionConfig[] getColumns_cfg() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[4];

        columns[0] = new ColumnDefinitionConfig<ProductCategoryItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(ProductCategoryItem item) {
                int actionItemCount = 0;
                final MenuBar menuBar = new MenuBar(true);

                final MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-employee-edit-profile");
                summary.getElement().setId("Product_categories_summary_button");
                summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("productcategoryview/" + item.getId(), item.getName()));
                actionItemCount++;
                menuBar.addItem(summary);

                MenuPopItem categoryEdit = new MenuPopItem(wfmStrings.edit(), "icon-employee-edit-profile");
                categoryEdit.getElement().setId("Product_categories_edit_button");
                categoryEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("productcategory|edit/" + item.getId(), item.getName()));
                actionItemCount++;
                menuBar.addItem(categoryEdit);

                MenuPopItem activeCategory = new MenuPopItem(item.isActive() ? wfmStrings.inactive() : wfmStrings.active(), "removeItemStyle-profile");
                activeCategory.setCommand(() -> {
                    WfmMessageBox inActiveMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    inActiveMessageBox.setTitle(wfmStrings.confirmationMessage());
                    inActiveMessageBox.setMessage(item.isActive() ? accountingStrings.doYouWantProductCategoryInactive() : accountingStrings.doYouWantProductCategoryActive());

                    inActiveMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            AccountingService.App.get().activeProductCategory(item.getId(), !item.isActive(), new AbstractAsyncCallback<Boolean>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    Info.show(accountingStrings.inActiveProductCategoryErrorMessage(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Boolean result) {
                                    Info.show(wfmMessages.changedSuccessfully(wfmStrings.productCategory()), Info.Type.INFO);
                                    list.reloadPage();
                                }
                            });
                        }
                    });
                    inActiveMessageBox.open();
                });
                menuBar.addItem(activeCategory);
                actionItemCount++;


                final MenuPopItem customFields = new MenuPopItem(wfmStrings.customFields(), "icon-custom-field");
                customFields.getElement().setId("Category_products_custom_field_button");
                customFields.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("productcategory|customfield/" + item.getId(), item.getName()));
                actionItemCount++;
                menuBar.addItem(customFields);

                if (Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR) || Utils.hasRole(Constants.ACCOUNTANT)) {
                    final MenuPopItem deleteCategory = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteCategory.getElement().setId("Category_products_delete_button");
                    deleteCategory.setCommand(() -> {
                        WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                AccountingService.App.get().deleteProductCategory(item.getId(), new AbstractAsyncCallback<Boolean>() {
                                    public void failure(final Throwable throwable) {
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(final Boolean deleted) {
                                        if (deleted) {
                                            //WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUPPLIER_DELETED, deleted, UnitMeasurementsListView.this);
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.category()), Info.Type.INFO);
                                            list.reloadPage();
                                        } else {
                                            Info.show(wfmStrings.categoryCouldNotBeDeleted(), Info.Type.INFO);
                                        }
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(deleteCategory);
                }

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<ProductCategoryItem, Widget>(wfmStrings.parent(), ProductCategoryItem.PARENT, 100) {
            @Override
            public Widget getCellValue(final ProductCategoryItem item) {
                final SimpleLink label = new SimpleLink(item.getParentCategoryName());
                label.addClickHandler((e) -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("productcategoryview/" + item.getParentCategoryID(), item.getParentCategoryName());
                });
                return label;
            }
        };
        columns[1].setMaximumColumnWidth(150);

        columns[2] = new ColumnDefinitionConfig<ProductCategoryItem, Widget>(wfmStrings.name(), ProductCategoryItem.NAME, 100) {
            @Override
            public Widget getCellValue(final ProductCategoryItem item) {
                final SimpleLink label = new SimpleLink(item.getName());
                label.addClickHandler((e) -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("productcategoryview/" + item.getId(), item.getName());
                });
                return label;
            }
        };
        columns[2].setMaximumColumnWidth(150);
        columns[3] = new ColumnDefinitionConfig<ProductCategoryItem, String>(wfmStrings.description(), ProductCategoryItem.DESCRIPTION, 400) {
            @Override
            public String getCellValue(final ProductCategoryItem item) {
                return item.getDescription();
            }
        };
        columns[3].setMaximumColumnWidth(200);

        columns[4] = new ColumnDefinitionConfig<ProductCategoryItem, String>(wfmStrings.status(), ProductCategoryItem.STATUS, 100) {
            @Override
            public String getCellValue(final ProductCategoryItem item) {
                return item.isActive() ? wfmStrings.active() : wfmStrings.inactive();
            }
        };
        columns[4].setMaximumColumnWidth(100);

        return columns;
    }

    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(final Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public void initStatistics(final Integer parentId, final Span container) {
        final ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        if (parentId != null) {
            initDataProvider(fp, null, container);
        }
    }

    private void saveProductCategoryEditCellValue(ProductCategoryItem rowValue, String columnCodeName) {
        AccountingService.App.get().saveProductCategoryCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
        });
    }
}
