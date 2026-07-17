package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 15/03/12
 * Time: 16:27
 * To change this template use File | Settings | File Templates.
 */
public class SolrCoreCompanyView extends BaseListView implements Colapse {
    private final String coreName;
    private ListingPanel<SelectItem> companyListPanel;

    public SolrCoreCompanyView(String coreName) {
        super("solrCompanyList", "Solr Core Company List");
        this.coreName = coreName;
    }

    @Override
    protected Widget onInitialize() {
        companyListPanel = new ListingPanel<>(ListPanelType.SolrCoreCompanyListPanel, getColumns(), getRequest(), getPanelDesign());
        companyListPanel.getPdfVersion().setVisible(false);
        companyListPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadSolrCoreCompanyStatisticExcel";
            companyListPanel.callListExcel(excelURL, companyListPanel.getFilterParametrs());
        });
        companyListPanel.getFilterParametrs().setParams(coreName);
        add(companyListPanel);
        return null;
    }

    private ColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[5];
        int col = 0;
        columns[col] = new ColumnDefinitionConfig<SelectItem,Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final SelectItem selectItem) {
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile file--SolrCoreCompanyView");
                removeItem.setCommand(() -> {
                    WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    //message.setSize(300, 150);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            BackendService.App.get().deleteCompanyInSoreCore(coreName, selectItem.getId(), new AbstractAsyncCallback<Void>() {
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void success(Void result) {
                                    LoadingPanel.loading(false);
                                    Info.show("Company Solr Data Has Been Deleted", Info.Type.INFO);
                                    companyListPanel.reloadPage();
                                }
                            });
                        }
                    });
                    message.open();
                });
                if (!Boolean.parseBoolean(selectItem.getDescription())) {
//                    removeItem.setVisible(false); //https://prnt.sc/sap3j6
                }
                menuBar.addItem(removeItem);
                ToolItem toolItem = new ToolItem(1);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[col].setColumnSortable(false);
        columns[col].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[col++].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);

        columns[col] = new ColumnDefinitionConfig<SelectItem,String>("Company ID","companyId",60) {
            @Override
            public String getCellValue(SelectItem selectItem) {
                return selectItem.getId().toString();
            }
        };

        columns[col].setMinimumColumnWidth(40);
        columns[col].setColumnSortable(false);
        columns[col].addStyleAttribute("font-weight", "bold");
        columns[col++].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[col] = new ColumnDefinitionConfig<SelectItem,String>("Company Name","companyName",80) {
            @Override
            public String getCellValue(SelectItem selectItem) {
                return selectItem.getName();
            }
        };

        columns[col].setMinimumColumnWidth(60);
        columns[col++].setColumnSortable(false);

        columns[col] = new ColumnDefinitionConfig<SelectItem, String>("Number Docs", "numberDocs", 80) {

            @Override
            public String getCellValue(SelectItem selectItem) {
                return selectItem.getDescription();
            }
        };
        columns[col].addStyleAttribute("font-weight", "bold");
        columns[col++].setMinimumColumnWidth(60);

        columns[col] = new ColumnDefinitionConfig<SelectItem, String>("Company Status", "companyStatus", 80) {

            @Override
            public String getCellValue(SelectItem selectItem) {
                return selectItem.isSelected() ? "Deleted" : wfmStrings.active();
            }
        };
        columns[col].setHtml(true);
        columns[col].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[col].setMinimumColumnWidth(60);
        columns[col++].setColumnSortable(false);

        return columns;
    }

    private ListingRequestProvider<SelectItem> getRequest() {
        return (filterParametrs, callback) -> BackendService.App.get().getSolrCoreByCompanyList(coreName, filterParametrs, new AsyncCallback<ListResult<SelectItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<SelectItem> listResult) {
                callback.onSuccess(listResult);
            }
        });
    }

    private ListingPanelDesign getPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> RbacService.App.get().getByCompanySolrCoreFacetFilter(data, coreName, new AsyncCallback<FacetFilterRpc>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                callback.onFailure(throwable);
                            }

                            @Override
                            public void onSuccess(FacetFilterRpc facetFilterRpc) {
                                callback.onSuccess(facetFilterRpc);
                            }
                        });
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetConfigure();
                    }
                };
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
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Integer getTypeParentId() {
                return getSolrCoreId();
            }
        };
    }

    private Integer getSolrCoreId() {
        return coreName.hashCode();
    }

    private FacetContentConfigure getFacetConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(2, coreName.toUpperCase());
        contentConfigure.addContentConfigure("company", "Company", new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrTaskRepresenter.FIELD_COMPANY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrTaskRepresenter.FIELD_COMPANY_ID;
            }
        });
        return contentConfigure;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
