package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.ProfileMessages;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowEmployee;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Azazello on 4/27/16.
 */
public class WorkflowEmployeeListView extends BaseListView implements Constants {
    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final ProfileMessages profileMessages = ProfileMessages.App.get();
    private ListingPanel<WorkflowEmployee> list;
    private Set<WorkflowEmployee> selectedItems = new HashSet<>();
    private final Integer workflowID;

    public WorkflowEmployeeListView(Integer workflowID) {
        super("convertToEmployee", settingsStrings.convertToEmployee());
        this.workflowID = workflowID;
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.WorkflowEmployeeListPanel, getColumnConfigs(), getListData(), getDisagn(), getSelectionPolicy());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKFLOW_EMPLOYEE_MODIFICATION, WorkflowEmployeeListView.this, (sender, args) -> list.reloadPage());
        list.addSelectionRowHandler(selectedRows -> selectedItems = (HashSet<WorkflowEmployee>) selectedRows);
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[2];
        Integer index = -1;
        //////////////////////////---------(0)----------
        columns[++index] = new ColumnDefinitionConfig<WorkflowEmployee, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final WorkflowEmployee item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowEmployee|add/add/" + item.getWorkflowID() + "/" + item.getObjectID()));
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem remove = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                remove.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(wfmMessages.sureYouWantToDelete("", settingsStrings.convertToEmployee()));
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            ArrayList<Integer> idS = new ArrayList<>();
                            idS.add(item.getObjectID());
                            profileService.deleteWorkflowEmployees(idS, new AbstractAsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    LoadingPanel.loading(false);
                                    Info.show(profileMessages.workflowEmployeeDeleted(), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_EMPLOYEE_MODIFICATION, result, WorkflowEmployeeListView.this);
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                actionItemCount++;
                menuBar.addItem(remove);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        //////////////////////////---------(1)----------
        columns[++index] = new ColumnDefinitionConfig<WorkflowEmployee, String>(wfmStrings.name(), "name", 100) {
            @Override
            public String getCellValue(final WorkflowEmployee item) {
                return settingsStrings.convertToEmployee() + "_" + String.valueOf(item.getObjectID());
            }
        };
        columns[index].setMinimumColumnWidth(40);
        columns[index].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        return columns;
    }

    private ListingRequestProvider<WorkflowEmployee> getListData() {
        return (filterParametr, callback) -> {
            loadEmployeeList(filterParametr, callback, null);
        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        loadEmployeeList(fp, null, container);
    }

    private void loadEmployeeList(ListingFilterParameter filterParametr, ListingCallback callback, Span container) {
        if (filterParametr == null) {
            filterParametr = new ListingFilterParameter();
        }
        filterParametr.setWorkflowID(workflowID);
        profileService.getWorkflowEmployeeList(filterParametr, new AbstractAsyncCallback<ListResult<WorkflowEmployee>>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(ListResult<WorkflowEmployee> result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(result.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("workflowEmployee|add/add/" + workflowID));
                return addNew;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                return getRemoveMoreButton(clickEvent -> deleteSelection());
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(settingsStrings.convertToEmployee()));
                message.setHref("workflowEmployee|add/add/" + workflowID);
                message.setTextBeforeLink(wfmMessages.addingByClicking(settingsStrings.convertToEmployee()));
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow(settingsStrings.convertToEmployee()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = wfmMessages.sureYouWantToDelete("", selectedItems.size() + " " + settingsStrings.convertToEmployee() + "s");
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                final ArrayList<Integer> ids = WorkflowEmployee.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    profileService.deleteWorkflowEmployees(ids, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Void result) {
                            LoadingPanel.loading(false);
                            Info.show(profileMessages.workflowEmployeeDeleted(), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_EMPLOYEE_MODIFICATION, result, WorkflowEmployeeListView.this);
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    private SelectionGrid.SelectionPolicy getSelectionPolicy() {
        return SelectionGrid.SelectionPolicy.CHECKBOX;
    }

    @Override
    public String getIconStyle() {
        return "contact contact-list";
    }

    @Override
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
