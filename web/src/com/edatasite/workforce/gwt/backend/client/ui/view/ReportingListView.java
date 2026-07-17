package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov, Atabek Boboyev
 * Date: Mar 27, 2011
 * Time: 3:14:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportingListView extends BaseListView implements Constants {

    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private ListingPanel<ReportingListItem> listingTable;
    int pageStart = 0;
    private final String templateType;

    public ReportingListView(String templateType, String name, String description) {
        super(name, description);
        this.templateType = templateType;
    }

    public String getIconStyle() {
        switch (templateType) {
            case "XML":
                return "backend xmlRepListView";
            case "XLS":
                return "backend xlsRepListView";
            default:
                return null;
        }
    }

    public void init(Workarea workarea) {

    }

    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(null, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REPORT_TEMPLATE_ADD_EDIT, ReportingListView.this, (sender, args) -> listingTable.reloadPage());
        add(listingTable);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig["XLS".equals(templateType) ? 2 : 3];

        columns[0] = new ColumnDefinitionConfig<ReportingListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ReportingListItem rowValue) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if ("XLS".equals(templateType)) {
                    MenuPopItem menuItem = new MenuPopItem(wfmStrings.edit());
                    menuItem.setCommand(() -> {
                        if (listingTable.getFilterParametrs().getCompanyID() != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("reportingtemplate|add/" + rowValue.getReportId() + "/" + listingTable.getFilterParametrs().getCompanyID());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("reportingtemplate|add/" + rowValue.getReportId());
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(menuItem);
                    if (rowValue.getExceltemplateId() != null) {
                        MenuPopItem downloadItem = new MenuPopItem(wfmStrings.download() + " " + wfmStrings.excel());
                        downloadItem.setCommand(() -> BackendService.App.get().getLink(rowValue.getExceltemplateId(), new AsyncCallback<String>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                Info.show(wfmStrings.fileNotFound());
                            }

                            @Override
                            public void onSuccess(String s) {
                                if (!Utils.isNullOrEmpty(s)) {
                                    Utils.redirect(s);
                                }
                            }
                        }));
                        actionItemCount++;
                        menuBar.addItem(downloadItem);
                    }
                } else {
                    MenuPopItem roles = new MenuPopItem(backendStrings.roleEdit());
                    roles.setCommand(() -> roleSet(rowValue));
                    menuBar.addItem(roles);

                    MenuPopItem menuItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    menuItem.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        //message.setSize(300, 150);
                        message.setTitle(wfmStrings.delete());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                CoreService.App.get().deleteReportingXMLTemplateFromCompany(rowValue.getTemplateId(), listingTable.getFilterParametrs().getCompanyID(), new AbstractAsyncCallback<Void>() {
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.messSuccessfulyyDeleted(), Info.Type.INFO);
                                        listingTable.reloadPage();
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(menuItem);

                }
                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        if ("XLS".equals(templateType)) {
            columns[1] = new ColumnDefinitionConfig<ReportingListItem, String>(wfmStrings.reportName(), "reportName", 120) {
                @Override
                public String getCellValue(ReportingListItem rowValue) {
                    return rowValue.getReportName();
                }
            };
        } else {
            columns[1] = new ColumnDefinitionConfig<ReportingListItem, String>(wfmStrings.template(), "templateName", 120) {
                @Override
                public String getCellValue(ReportingListItem rowValue) {
                    return rowValue.getTemplateName();
                }
            };
            columns[1].setMaximumColumnWidth(200);

            columns[2] = new ColumnDefinitionConfig<ReportingListItem, String>(wfmStrings.templateType(), "templateType", 120) {
                @Override
                public String getCellValue(ReportingListItem rowValue) {
                    return rowValue.getTemplateType();
                }
            };
        }

        return columns;
    }

    private void roleSet(final ReportingListItem rowValue) {
        final KpiModal dialogBox = new KpiModal();
        dialogBox.setCloseButton(true);
        KpiCheckBox checkAll = null;
        final CheckListBox roleCheckListBox = new CheckListBox();
        checkAll = new KpiCheckBox(wfmStrings.all());
//        roleCheckListBox.setSize("300px", "400px");
        roleCheckListBox.addStyleName("roleCheckListBox"); //https://prnt.sc/sagfoh
//        dialogBox.setSize("310px", "410px");
        dialogBox.addStyleName("file--ReportingListVIew");
        Button save = new Button(wfmStrings.save());
        save.addStyleName("btn btn--primary");

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setStyleName("gwt-TabPanelBottom");
        verticalPanel.add(checkAll);
        verticalPanel.add(roleCheckListBox);

        dialogBox.add(verticalPanel);
        dialogBox.addButton(save);
        save.addClickHandler(clickEvent -> {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setCategoryID(null);
            filterParameter.setCompaines(new Integer[]{listingTable.getFilterParametrs().getCompanyID()});
            filterParameter.setObjectId(rowValue.getTemplateId());
            filterParameter.setName(rowValue.getTemplateName());
            filterParameter.setDescription(rowValue.getTemplateBody());
            filterParameter.setLibrary(rowValue.isLibrary());
            filterParameter.setSelected(true);
            filterParameter.setDeleted(false);
            filterParameter.setColumnsOfListing(new ArrayList<>());
            for (SelectItem selectItem : roleCheckListBox.getSelectedItems()) {
                filterParameter.getColumnsOfListing().add(selectItem.getDescription());
            }
            CoreService.App.get().saveOrUpdateReportTemplate(filterParameter, new AsyncCallback<Integer>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(Integer aVoid) {
                    dialogBox.close();
                    listingTable.reloadPage();
                }
            });
        });
        dialogBox.center();
        CoreService.App.get().getTemplateRoles(listingTable.getFilterParametrs().getCompanyID(), rowValue.getTemplateId(), new AsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> selectItems) {
                roleCheckListBox.init(selectItems);
            }
        });
        checkAll.addValueChangeHandler(b -> {
            for (SelectItem item : roleCheckListBox.getItems()) {
                roleCheckListBox.setSelected(item.getDescription(), b.getValue());
            }
        });
    }

    private ListingRequestProvider<ReportingListItem> getListingRequestProvider() {
        return (filterParameter, callback) -> {
            listingTable.getFilterParametrs().setFolderName(templateType);
            CoreService.App.get().getReportingTemplateList(filterParameter, new AbstractAsyncCallback<ListResult<ReportingListItem>>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(ListResult<ReportingListItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
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
                        return ListingChooseFilter.COMPANY_REPORTING_XML_TEMPLATES;
                    }
                };
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel topPanel = new HorizontalPanel();
                final SchemaLookUp schemaLookUp = new SchemaLookUp();
                schemaLookUp.getSuggestBox().setWidth("180px");
                schemaLookUp.setSelected(new SelectItem(Integer.valueOf(Utils.getEncryptedCompanyID()), Utils.getCompanyName()));
                listingTable.getFilterParametrs().setCompanyID(schemaLookUp.getSelectedItemID());
                schemaLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
                    if (schemaLookUp.getSelectedItemID() != null && schemaLookUp.getSelectedItemID() != 0 && schemaLookUp.getSelectedItemID() != -1) {
                        listingTable.getFilterParametrs().setCompanyID(schemaLookUp.getSelectedItemID());
                        listingTable.reloadPage();
                    }
                });
                topPanel.add(schemaLookUp);
                return topPanel;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
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
