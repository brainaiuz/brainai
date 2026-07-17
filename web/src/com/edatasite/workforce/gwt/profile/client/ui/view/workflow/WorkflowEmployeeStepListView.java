package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
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
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Azazello on 8/6/15.
 */
public class WorkflowEmployeeStepListView extends BaseListView implements Constants {
    private static final ProfileServiceAsync profileService = ProfileService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final WfmMessages messages = WfmMessages.App.get();
    private ListingPanel<EmployeeStepItem> list;
    private HashSet<EmployeeStepItem> selectedItems = new HashSet<>();
    private final Integer workflowID;
    private final DataListBox chooseType = new DataListBox();

    public WorkflowEmployeeStepListView(Integer workflowID) {
        super("workflowEmployeeStep", wfmStrings.onboardingStep());
        this.workflowID = workflowID;
    }

    protected Widget onInitialize() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setAllByFilter(true);
        profileService.getApproverModules(fp, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
                initList();
            }

            @Override
            public void success(SelectItem[] result) {
                chooseType.setItems(result);
                initList();
            }
        });
        return null;
    }

    private void initList() {
        list = new ListingPanel<>(ListPanelType.WorkflowEmployeeStepListPanel, getColumnConfigs(), getListData(), getDisagn(), getSelectionPolicy());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WORKFLOW_EMPLOYEE_STEP_UPDATE, WorkflowEmployeeStepListView.this, (sender, args) -> list.reloadPage());
        list.addSelectionRowHandler(selectedRows -> selectedItems = (HashSet<EmployeeStepItem>) selectedRows);
        add(list);
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[3];
        Integer index = -1;
        //////////////////////////---------(0)----------
        columns[++index] = new ColumnDefinitionConfig<EmployeeStepItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final EmployeeStepItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(EMPLOYEE_STEP + "|add/add/" + item.getObjectID() + "/" + item.getStepID() + "/" + item.getFormID() + "/" + item.getStepName() + "/" + workflowID));
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem removeCampaign = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                removeCampaign.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(messages.sureYouWantToDelete("", wfmStrings.onboardingStep()));
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            profileService.deleteWorkflowStep(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void onSuccess(Void result) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmMessages.workflowStepDeleted(), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_EMPLOYEE_STEP_UPDATE, result, WorkflowEmployeeStepListView.this);
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                actionItemCount++;
                menuBar.addItem(removeCampaign);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        //////////////////////////---------(1)----------
        columns[++index] = new ColumnDefinitionConfig<EmployeeStepItem, SimpleLink>(wfmStrings.onboardingStep(), "stage", 100) {
            @Override
            public SimpleLink getCellValue(final EmployeeStepItem item) {
                SimpleLink link = new SimpleLink(item.getStepName() != null ? item.getStepName() : wfmStrings.notAvailable());
                link.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged(EMPLOYEE_STEP + "|add/add/" + item.getObjectID() + "/" + item.getStepID() + "/" + item.getFormID() + "/" + item.getStepName() + "/" + workflowID));
                return link;
            }
        };
        columns[index].setMinimumColumnWidth(40);

        //////////////////////////---------(2)----------
        columns[++index] = new ColumnDefinitionConfig<EmployeeStepItem, String>(wfmStrings.status(), "status", 100) {
            @Override
            public String getCellValue(final EmployeeStepItem item) {
                return item.getStatusName() != null ? item.getStatusName() : wfmStrings.notAvailable();
            }
        };
        columns[index].setMinimumColumnWidth(40);

        return columns;
    }

    private ListingRequestProvider<EmployeeStepItem> getListData() {
        return (filterParametr, callback) -> {
            if (filterParametr == null) {
                filterParametr = new ListingFilterParameter();
            }
            filterParametr.setWorkflowID(workflowID);
            profileService.listWorkflowSteps(filterParametr, new AbstractAsyncCallback<ListResult<EmployeeStepItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(ListResult<EmployeeStepItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                MenuBar menu = new MenuBar(true);
                for (final SelectItem item : chooseType.getItems()) {
                    MenuPopItem addNewEmployeeStep = new MenuPopItem(item.getName());
                    addNewEmployeeStep.ensureDebugId("Workflow_" + item.getDescription() + "_add");
                    addNewEmployeeStep.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(EMPLOYEE_STEP + "|add/add/" + item.getId() + "/" + item.getDescription() + "/" + item.getName() + "/" + workflowID));
                    menu.addItem(addNewEmployeeStep);
                }
                addNew.setMenu(menu);
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(messages.currentlyDonotHaveAny("workflow set for Onboarding Step."));
                /*message.setHref(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent clickEvent) {
                        new WorkflowEmployeeStepView(null, workflowID);
                    }
                });
                message.setTextBeforeLink(messages.addingByClicking(""));*/
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(messages.pleaseSelectOneRow(wfmStrings.onboardingStep()), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = messages.sureYouWantToDelete("", selectedItems.size() + " " + wfmStrings.onboardingStep() + "s");
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                final ArrayList<Integer> ids = EmployeeStepItem.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    profileService.deleteWorkflowSteps(ids, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Void result) {
                            LoadingPanel.loading(false);
                            Info.show(wfmMessages.workflowStepDeleted(), Info.Type.INFO);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_EMPLOYEE_STEP_UPDATE, result, WorkflowEmployeeStepListView.this);
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
        return null;
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
