package com.edatasite.workforce.gwt.core.client.ui.view.payslip;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SalaryHistoryService;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
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

public class SalaryHistoryListView extends BaseListView implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private ListingPanel<SalaryHistory> listingPanel;
    private final Integer entityID;

    public SalaryHistoryListView(Integer entityID) {
        super("salaryhistory", wfmStrings.salaryHistories());
        this.entityID = entityID;
        if (Utils.hasRole(PM) || Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(HR)) {
            setAddNew(() -> new EmployeeSalaryView(entityID));
        }
    }

    @Override
    protected Widget onInitialize() {
        listingPanel = new ListingPanel<>(getPanelType(), getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALARY_ADD, SalaryHistoryListView.this, (sender, args) -> listingPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SALARY_DELETE, SalaryHistoryListView.this, (sender, args) -> listingPanel.reloadPage());
        add(listingPanel);
        return super.onInitialize();
    }

    private ListPanelType getPanelType() {
        return ListPanelType.EmployeeSalaryHistoryList;
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
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = null;
                if (Utils.hasPermission(PermissionConstants.ADD_SALARY_HISTORY)) {
                    addNew = getAddNewButton();
                    addNew.addClickHandler(c -> new EmployeeSalaryView(entityID));
                }

                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noUpdatesText());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<SalaryHistory> getListingRequestProvider() {
        return (filterParameter, callback) -> {
            filterParameter.setEntityID(entityID);
            SalaryHistoryService.App.get().list(filterParameter, new AbstractAsyncCallback<ListResult<SalaryHistory>>() {

                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<SalaryHistory> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[4];
        columns[0] = new ColumnDefinitionConfig<SalaryHistory, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final SalaryHistory item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.setScheduledCommand(() -> new EmployeeSalaryView(entityID, item.getId()));
                edit.getElement().setId("salary_edit_button");
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                delete.getElement().setId("salary_delete_button");
                delete.setScheduledCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(wfmStrings.sureYouWantToDelete() + "&nbsp <font color='#15428B'><b>\"" + item.getSalary() + "\"</b></font> ?");
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            SalaryHistoryService.App.get().delete(item.getId(), new AsyncCallback<Boolean>() {
                                public void onFailure(Throwable caught) {
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void onSuccess(Boolean deleted) {
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SALARY_DELETE, null, null);
                                    Info.show(wfmStrings.messSuccessfulyyDeleted(), Info.Type.INFO);
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                menuBar.addItem(delete);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);
        columns[1] = new ColumnDefinitionConfig<SalaryHistory, String>(wfmStrings.basicSalary(), "salary", 200) {

            @Override
            public String getCellValue(SalaryHistory item) {
                return item.getSalary() != null ? Utils.getCalculationNumberFormat().format(item.getSalary()) : wfmStrings.na();
            }
        };
        columns[1].setMinimumColumnWidth(180);
        columns[2] = new ColumnDefinitionConfig<SalaryHistory, String>(wfmStrings.effectiveDate(), "effectiveDate", 100) {

            @Override
            public String getCellValue(SalaryHistory item) {
                return item.getEffectiveDate() != null ? DateUtils.format(item.getEffectiveDate().getNonConvertedDate()) : wfmStrings.na();
            }
        };
        columns[2].setMinimumColumnWidth(80);

        columns[3] = new ColumnDefinitionConfig<SalaryHistory, String>(wfmStrings.related(), "related", 100) {

            @Override
            public String getCellValue(SalaryHistory item) {
                return item.getRelationType() != null ? item.getRelationType() : wfmStrings.na();
            }
        };
        columns[3].setMinimumColumnWidth(80);
        return columns;
    }

    @Override
    public String getIconStyle() {
        return null;
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
