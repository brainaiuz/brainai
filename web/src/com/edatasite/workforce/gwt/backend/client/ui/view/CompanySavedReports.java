package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 5/1/12
 * Time: 1:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompanySavedReports extends BaseListView {
    private static final BackendStrings backendStrings = BackendStrings.App.get();
    private ListingPanel<SelectListRpc> listingPanel;
    private SchemaLookUp schemaLookUp;
    private final TableColumn[] columns = new TableColumn[2];
    private final MaterialPanel gridRow = new MaterialPanel("grid-row");
    private final MaterialPanel col = new MaterialPanel("col-12");
    private SelectPanel sharedTree;

    public CompanySavedReports() {
        super("companySavedReports", backendStrings.companySavedReports());
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(ListPanelType.CompanySavedReportPanel, getColumnConfig(), getRequestDataProvider(), getDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        add(listingPanel);
        initializeTree();
        return null;
    }

    private void initializeTree() {
        columns[0] = new TableColumn("company", wfmStrings.company(), 150);
        columns[1] = new TableColumn("delete", wfmStrings.action(), 50);
        sharedTree = new SelectPanel(columns);
        sharedTree.setSearchText(wfmStrings.searchCompany());
        sharedTree.hideAvailablityCheckBox();
        LoadingPanel.loading(true);
        CoreService.App.get().getCompanies(null, new AsyncCallback<ArrayList<TeamEmployees>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(ArrayList<TeamEmployees> companyList) {
                TreeSelect.setTickAllVisible(companyList.size() != 0);
                for (TeamEmployees teamEmployee : companyList) {
                    sharedTree.addTreeItem(teamEmployee.getTeam(), teamEmployee.getMembers());
                }
                for (int i = 0; i < sharedTree.getTree().getItemCount(); i++) {
                    NTreeSelectItem parent = (NTreeSelectItem) sharedTree.getTree().getItem(i);
                    for (int j = 0; j < parent.getChildCount(); j++) {
                        NTreeSelectItem child = (NTreeSelectItem) parent.getChild(j);
                        for (WfmTreeItem company : companyList.get(0).getMembers()) {
                            if (child.getItem().getId().equals(company.getId()) && company.isChecked()) {
                                child.setChecked(true);
                                sharedTree.onTreeItemSelection(child, null);
                                break;
                            }
                        }
                    }
                }
                sharedTree.expandTreeView();
                LoadingPanel.loading(false);
            }
        });
        gridRow.add(col);
        col.add(sharedTree);
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<SelectListRpc, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(SelectListRpc rowValue) {
                MenuBar menuBar = new MenuBar(true);
                getTopActions(menuBar, rowValue);

                final ToolItem toolItem = new ToolItem(0);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<SelectListRpc, String>(wfmStrings.name(), "name", 100) {
            @Override
            public String getCellValue(SelectListRpc rowValue) {
                return rowValue.getName();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<SelectListRpc, String>(wfmStrings.folder(), "folder", 100) {
            @Override
            public String getCellValue(SelectListRpc rowValue) {
                return rowValue.getFolder();
            }

            @Override
            public void setCellValue(SelectListRpc rowValue, String cellValue) {
                rowValue.setFolder(cellValue);
                saveCellValue(rowValue);
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<SelectListRpc, String>(wfmStrings.template(), "viewName", 100) {
            @Override
            public String getCellValue(SelectListRpc rowValue) {
                return rowValue.getDescription();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<SelectListRpc, Boolean>(wfmStrings.isLibrary(), "isLibrary", 40) {
            @Override
            public Boolean getCellValue(SelectListRpc rowValue) {
                return rowValue.isLibrary();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<SelectListRpc, Boolean>("Fake Report", "fakeReport", 40) {
            @Override
            public Boolean getCellValue(SelectListRpc rowValue) {
                return rowValue.isFakeReport();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<SelectListRpc, Boolean>(" - " + wfmStrings.synchronize(), "synchronization", 40) {
            @Override
            public Boolean getCellValue(SelectListRpc rowValue) {
                return rowValue.isSynchronization();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        initCellEdit(columns);
        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private void initCellEdit(ArrayList<ColumnDefinitionConfig> columns) {
        final DropDownCellEditor<String> folderCellEditor = new DropDownCellEditor<String>(150) {
            @Override
            protected String getValue() {
                return getListBox().getSelectedItem().getDescription();
            }

            @Override
            protected void setValue(String cellValue) {
                getListBox().setSelectedByValue(cellValue);
            }
        };
        folderCellEditor.getListBox().setWithoutNullLabel(true);
        columns.get(2).setCellEditor(folderCellEditor);
        columns.get(2).setCellChangesSave((rowValue, columnCodeName) -> ReportingService.App.get().changeFolderOfReport(schemaLookUp.getSelectedItemID(), ((SelectListRpc) rowValue).getId(), folderCellEditor.getSelectItem().getName(), new AbstractAsyncCallback<Void>() {
        }));
        ReportingService.App.get().getFolders(null, new AsyncCallback<FolderRpc[]>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(FolderRpc[] result) {
                ArrayList<SelectItem> items = new ArrayList<>();
                for (FolderRpc rpc : result) {
                    items.add(new SelectItem(rpc.getId(), rpc.getCategoryName() + " -> " + rpc.getName(), rpc.getName()));
                }
                folderCellEditor.getListBox().setItems(items.toArray(new SelectItem[items.size()]));
            }
        });
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                ActionButton items = new ActionButton(ActionButton.getMoreString(), "", ActionButton.Type.TOOLMENU);
                items.ensureDebugId("Company_Saved_report_button_id");
                items.addClickHandler(clickEvent -> {
                    if (schemaLookUp.getSelectedItemID() != null) {
                        MenuBar menu = new MenuBar(true);
                        menu.setAutoOpen(true);
                        getTopActions(menu, null);
                        items.setMenu(menu);
                    } else {
                        Info.warn(wfmStrings.pleaseChooseYourCompany());
                    }
                });
                return items;
            }

            @Override
            public Widget getAddAdditionalPanel() {
                MaterialPanel panel = new MaterialPanel();
                schemaLookUp = new SchemaLookUp();
                schemaLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> listingPanel.reloadPage());
                panel.add(schemaLookUp);
                return panel;
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

    private void getTopActions(MenuBar menu, final SelectListRpc item) {
        final MenuPopItem customAddHtml = new MenuPopItem(backendStrings.customHTML());
        customAddHtml.setCommand(
                () -> {
                    if (item!=null) {
                        addOrUpdateCustomHtml(item.getId());
                    } else {
                        Info.warn(wfmStrings.selectAnyItemToActivateBatchActions());
                    }
                }
        );
        menu.addItem(customAddHtml);
        final MenuPopItem export = new MenuPopItem(wfmStrings.export());
        export.setCommand(() -> {
            if (getSelectionItems(item) != null && getSelectionItems(item).length > 0) {
                exportReport(getSelectionItems(item), getSelectionCodeItems(item));
            } else {
                Info.warn(wfmStrings.selectAnyItemToActivateBatchActions());
            }
        });
        menu.addItem(export);

        final MenuPopItem viewDetails = new MenuPopItem(wfmStrings.viewDetails());
        viewDetails.setCommand(() -> {
            if (getSelectionItems(item) != null && getSelectionItems(item).length > 0) {
                viewReportDetails(getSelectionItems(item), getSelectionCodeItems(item));
            } else {
                Info.warn(wfmStrings.selectAnyItemToActivateBatchActions());
            }
        });
        menu.addItem(viewDetails);

        if (item != null) {
            final MenuPopItem edit = new MenuPopItem(wfmStrings.edit());
            edit.setCommand(() -> editReport(item.getId()));
            menu.addItem(edit);
        }

        final MenuPopItem setPermission = new MenuPopItem(wfmStrings.apply() + " " + wfmStrings.permission());
        setPermission.setCommand(() -> {
            if (getSelectionItems(item) != null && getSelectionItems(item).length > 0) {
                applyPermissionForReport(getSelectionItems(item), getSelectionCodeItems(item));
            } else {
                Info.warn(wfmStrings.selectAnyItemToActivateBatchActions());
            }
        });
        menu.addItem(setPermission);

        if (Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com") || Utils.isLocalhostOrLochin("taslimov.fahriddin@finnetlimited.com") || !Utils.getHostURL().contains(Constants.HOST_LIVE)) {
            final MenuPopItem delete = new MenuPopItem(wfmStrings.delete());
            delete.setCommand(() -> {
                if (getSelectionItems(item) != null && getSelectionItems(item).length > 0) {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.YesNo, false);
                    messageBox.setMessage(wfmStrings.payAttantionForImportantPage() + " " + wfmStrings.sureYouWantToDelete());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            listingPanel.getFilterParametrs().setCompanyID(schemaLookUp.getSelectedItemID());
                            listingPanel.getFilterParametrs().setCategories(getSelectionItems(item));
                            listingPanel.getFilterParametrs().setStatusCodes(getSelectionCodeItems(item));
                            LoadingPanel.loading(true);
                            CoreService.App.get().deleteReportsByCompany(listingPanel.getFilterParametrs(), new AsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                                }

                                @Override
                                public void onSuccess(Void aVoid) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.success());
                                    messageBox.close();
                                    listingPanel.reloadPage();
                                }
                            });
                        }
                    });
                    messageBox.open();
                } else {
                    Info.warn(wfmStrings.selectAnyItemToActivateBatchActions());
                }
            });
            menu.addItem(delete);
        }
    }

    public void addOrUpdateCustomHtml(Integer id){
        LoadingPanel.loading(false);

        KpiModal dialogBox = new KpiModal();
        dialogBox.setWidth(500);
        TextArea textArea = new TextArea();
        textArea.setCharacterWidth(99000);
        textArea.setHeight("400px");
        CoreService.App.get().getCustomHtmlCodeByReportId(id, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(String htmlCode) {
                if(htmlCode!=null) {
                    textArea.setValue(htmlCode);
                }else {
                    CoreService.App.get().getDefaultHtmlCode(new AsyncCallback<String>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.warn(wfmStrings.sorrySomethingWentWrong());
                        }

                        @Override
                        public void onSuccess(String s) {
                            textArea.setText(s);
                        }
                    });
                }
            }
        });
        dialogBox.add(textArea);
        dialogBox.addButton(new WfmButton2(wfmStrings.close(), clickEvent -> dialogBox.close()));
        dialogBox.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            LoadingPanel.loading(true);
            if (Utils.isNullOrEmpty(textArea.getText())) {
                Info.warn(wfmStrings.sureEnteredAllData());
                return;
            }

            CoreService.App.get().createOrUpdateCustomHtml(textArea.getText(),id, new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }


                @Override
                public void onSuccess(String aVoid) {
                    LoadingPanel.loading(false);
                    Info.show(aVoid);
                    dialogBox.close();
                }
            });
        }));
        dialogBox.open();
    }
    private void exportReport(Integer[] selectionItems, String[] selectionCodeItems) {
        KpiModal modal = new KpiModal();
        modal.setWidth(750);
        modal.add(gridRow);
        modal.addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> modal.close()));
        modal.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            LoadingPanel.loading(true);
            listingPanel.getFilterParametrs().setCompanyID(schemaLookUp.getSelectedItemID());
            listingPanel.getFilterParametrs().setCategories(selectionItems);
            listingPanel.getFilterParametrs().setStatusCodes(selectionCodeItems);
            listingPanel.getFilterParametrs().setCompaines(sharedTree.getSelectedItems());
            BackendService.Reporting.get().exportSavedReports(listingPanel.getFilterParametrs(), new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(String result) {
                    LoadingPanel.loading(false);
                    if (Utils.isNullOrEmpty(result)) {
                        modal.close();
                        Info.show(wfmStrings.success());
                    } else {
                        Info.warn(result);
                    }
                }
            });
        }));
        modal.open();
    }

    private void applyPermissionForReport(Integer[] selectionItems, String[] selectionCodeItems) {
        KpiModal modal = new KpiModal();
        modal.setWidth(750);
        modal.add(gridRow);
        modal.addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> modal.close()));
        modal.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            LoadingPanel.loading(true);
            listingPanel.getFilterParametrs().setCompanyID(schemaLookUp.getSelectedItemID());
            listingPanel.getFilterParametrs().setCategories(selectionItems);
            listingPanel.getFilterParametrs().setStatusCodes(selectionCodeItems);
            listingPanel.getFilterParametrs().setCompaines(sharedTree.getSelectedItems());
            BackendService.Reporting.get().setPermissionForSavedReports(listingPanel.getFilterParametrs(), new AsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(Void result) {
                    LoadingPanel.loading(false);
                    modal.close();
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), backendStrings.companySavedReports()));
                }
            });
        }));
        modal.open();
    }

    private void viewReportDetails(Integer[] selectionItems, String[] selectionCodeItems) {
        LoadingPanel.loading(true);
        listingPanel.getFilterParametrs().setCompanyID(schemaLookUp.getSelectedItemID());
        listingPanel.getFilterParametrs().setCategories(selectionItems);
        listingPanel.getFilterParametrs().setStatusCodes(selectionCodeItems);
        CoreService.App.get().getSavedReportInsertQuery(listingPanel.getFilterParametrs(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(String s) {
                LoadingPanel.loading(false);
                KpiModal dialogBox = new KpiModal();
                dialogBox.setWidth(500);
                TextArea textArea = new TextArea();
                textArea.setHeight("200px");
                textArea.setText(!Utils.isNullOrEmpty(s) ? s : "No report query found");
                dialogBox.add(textArea);
                dialogBox.addButton(new WfmButton2(wfmStrings.close(), clickEvent -> dialogBox.close()));
                dialogBox.open();
            }
        });
    }

    private void editReport(Integer objectID) {
        listingPanel.getFilterParametrs().setCompanyID(schemaLookUp.getSelectedItemID());
        listingPanel.getFilterParametrs().setObjectId(objectID);
        LoadingPanel.loading(true);
        CoreService.App.get().getSavedReportUpdateCommand(listingPanel.getFilterParametrs(), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(String s) {
                LoadingPanel.loading(false);
                if (Utils.isNullOrEmpty(s)) {
                    Info.warn("No report query found");
                } else {
                    KpiModal dialogBox = new KpiModal();
                    dialogBox.setWidth(500);
                    TextArea textArea = new TextArea();
                    textArea.setCharacterWidth(10000);
                    textArea.setHeight("400px");
                    textArea.setText(s);
                    dialogBox.add(textArea);
                    dialogBox.addButton(new WfmButton2(wfmStrings.close(), clickEvent -> dialogBox.close()));
                    dialogBox.addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
                        LoadingPanel.loading(true);
                        if (Utils.isNullOrEmpty(textArea.getText())) {
                            Info.warn(wfmStrings.sureEnteredAllData());
                            return;
                        }
                        listingPanel.getFilterParametrs().setParams(textArea.getText());
                        CoreService.App.get().updateSavedReport(listingPanel.getFilterParametrs(), new AsyncCallback<String>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                Info.warn(wfmStrings.sorrySomethingWentWrong());
                            }

                            @Override
                            public void onSuccess(String aVoid) {
                                LoadingPanel.loading(false);
                                Info.warn(aVoid);
                                dialogBox.close();
                            }
                        });
                    }));
                    dialogBox.open();
                }
            }
        });
    }

    private ListingRequestProvider<SelectListRpc> getRequestDataProvider() {
        return (filterParametrs, callback) -> {
            if (schemaLookUp.getSelectedItem() != null) {
                filterParametrs.setCompanyID(schemaLookUp.getSelectedItem().getId());
                CoreService.App.get().getCompanyReportList(filterParametrs, new AsyncCallback<ListResult<SelectListRpc>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void onSuccess(ListResult<SelectListRpc> result) {
                        if (callback != null) {
                            callback.onSuccess(result);
                        }
                    }
                });
            } else if(callback != null) {
                callback.onSuccess(new ListResult<>());
            }
        };
    }

    private Integer[] getSelectionItems(SelectListRpc rpc) {
        Integer[] objectIDs = null;
        if (rpc == null) {
            SelectListRpc[] SelectListRpcs = listingPanel.getPagingScrollTable().getSelectedRowValues().toArray(new SelectListRpc[]{});
            objectIDs = new Integer[SelectListRpcs.length];
            int i = -1;
            for (SelectListRpc SelectListRpc : SelectListRpcs) {
                objectIDs[++i] = SelectListRpc.getId();
            }
        } else {
            objectIDs = new Integer[]{rpc.getId()};
        }
        return objectIDs;
    }

    private String[] getSelectionCodeItems(SelectListRpc rpc) {
        String[] codeItems = null;
        if (rpc == null) {
            SelectListRpc[] SelectListRpcs = listingPanel.getPagingScrollTable().getSelectedRowValues().toArray(new SelectListRpc[]{});
            codeItems = new String[SelectListRpcs.length];
            int i = -1;
            for (SelectListRpc SelectListRpc : SelectListRpcs) {
                codeItems[++i] = SelectListRpc.getCode();
            }
        } else {
            codeItems = new String[]{rpc.getCode()};
        }
        return codeItems;
    }

    @Override
    public String getIconStyle() {
        return "backend reportListView";
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
}