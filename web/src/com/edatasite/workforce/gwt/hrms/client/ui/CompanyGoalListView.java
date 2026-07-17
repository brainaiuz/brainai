package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.localization.HrmsMessages;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsAPIItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 23, 2009
 * Time: 5:10:45 PM
 */
public class CompanyGoalListView extends BaseListView implements CommandConstants, Constants {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final HrmsMessages hrmsMessages = HrmsMessages.App.get();
    private final HrmsServiceAsync hrmsService = HrmsService.App.get();

    public CompanyGoalListView() {
        super("companygoals");
        setDescription(property.getPlural(hrmsStrings.companyGoals()));
        if (hasPermissionToAdd()) {
            setAddNew("companygoal|add/add");
        }
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_COMPANY_GOALS);
    }

    private ListingPanel<GoalItem> list;

    @Override
    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.CompanyGoalListPanel, drawColumns(), getProvider(), getDesigner());

        list.setExcelListener(clickEvent -> {
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/downloadcompanygoalListExcel";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            list.callListExcel(excelURL, filterParametrs);
        });

        list.setPDFListener(clickEvent -> {
            String pdfURL;

            pdfURL = CommandConstants.PDF_URL + "/goalListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setStatusValues(COMPANY_GOAL);
            list.callListPDF(pdfURL, filterParametrs);
        });
        list.setCustomFieldsEditCellSaveChanges(new CellChange<GoalItem>() {
            @Override
            public void saveCell(GoalItem rowValue, String columnCodeName) {
                saveGoalEditCellValue(rowValue, columnCodeName);
            }
        });


        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_COMPANY_GOAL_ADD, CompanyGoalListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[7];
        int index = 0;

        columns[index] = new ColumnDefinitionConfig<GoalItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            public String getColumnStyle(Object object) {
                return null;
            }

            @Override
            public Anchor getCellValue(final GoalItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem goalSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-company-goal-small");
                goalSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("companygoal|summary/" + item.getObjectId(), item.getTitle()));
                if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_GOAL_SUMMARY)) {
                    actionItemCount++;
                    menuBar.addItem(goalSummary);
                }

                MenuPopItem goalEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                goalEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("companygoal|editcompanygoal/" + item.getObjectId(), item.getTitle()));
                actionItemCount++;
                if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_COMPANY_GOAL)) {
                    menuBar.addItem(goalEdit);
                }

                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                removeItem.setCommand(() -> {
                    WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    wfmMessageBox.setTitle(wfmStrings.warning());
                    wfmMessageBox.setMessage(wfmStrings.sureYouWantToDelete());
                    wfmMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            hrmsService.deleteCompanyGoal(item.getObjectId(), new AbstractAsyncCallback<Void>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void success(Void result) {
                                    LoadingPanel.loading(false);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GOAL_DELETE, result, CompanyGoalListView.this);
                                    Info.show(hrmsStrings.yourGoalHasBeenDeleted(), Info.Type.INFO);
                                    list.reloadPage();
                                }
                            });
                        }
                    });
                    wfmMessageBox.open();
                });
                if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_GOAL_REMOVE)) {
                    actionItemCount++;
                    menuBar.addItem(removeItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index++].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index] = new ColumnDefinitionConfig<GoalItem, SimpleLink>(wfmStrings.title(), GoalItem.COMPANY_GOAL_LIST_TITLE, 140) {

            public String getColumnStyle(Object object) {
                return null;
            }

            @Override
            public SimpleLink getCellValue(GoalItem item) {
                if (Utils.hasPermission(PermissionConstants.HRMS_COMPANY_GOAL_SUMMARY)) {
                    return getLink(item.getTitle(), "companygoal|summary/" + item.getObjectId(), item.getTitle());
                }
                return getLink(item.getTitle(), null);
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.description(), GoalItem.COMPANY_GOAL_LIST_DESCRIPTION, 150) {
            public String getColumnStyle(Object object) {
                return null;
            }

            @Override
            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                return item.getDescription();
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.outcome(), GoalItem.COMPANY_GOAL_LIST_OUTCOME, 150) {

            public String getColumnStyle(Object object) {
                return null;
            }

            @Override
            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                return item.getOutcome();
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.startDate(), GoalItem.COMPANY_GOAL_LIST_FROM_DATE, 100) {
            public String getColumnStyle(Object object) {
                return null;
            }

            @Override
            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                if (item.getFromDate() != null) {
                    return DateUtils.format(item.getFromDate()) + Utils.getHijriDate(item.getFromDate().getNonConvertedDate());
                }
                return "";
            }
        };
        columns[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.endDate(), GoalItem.COMPANY_GOAL_LIST_TO_DATE, 100) {

            public String getColumnStyle(Object object) {
                return null;
            }

            @Override
            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                if (item.getToDate() != null) {
                    return DateUtils.format(item.getToDate()) + Utils.getHijriDate(item.getToDate().getNonConvertedDate());
                }
                return "";
            }
        };
        columns[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.status(), GoalItem.COMPANY_GOAL_LIST_STATUS, 100) {

            public String getColumnStyle(Object object) {
                return null;
            }

            @Override
            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                return item.getStatus();
            }
        };
        columns[index].setMinimumColumnWidth(70);
        columns[index++].setShow(false);

        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.validityPeriod(), GoalItem.GOAL_LIST_VALIDITY_PERIOD, 100) {

            public String getColumnStyle(Object object) {
                return null;
            }

            @Override
            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                return item.getValidityPeriodItem() != null ? item.getValidityPeriodItem().getName() : "";
            }
        };
        columns[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[index].setColumnSortable(false);
        columns[index++].setMinimumColumnWidth(70);

        return columns;
    }

    private ListingRequestProvider<GoalItem> getProvider() {
        return (filterParametrs, callback) -> hrmsService.getCompanyGoalList(filterParametrs, new AsyncCallback<ListResult<GoalItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<GoalItem> goalList) {
                callback.onSuccess(goalList);
            }
        });
    }

    private GuideListingPanelDesign getDesigner() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? CompanyGoalListView.this::addNewCompanyGoal : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {
                    return addCompanyGoal();
                }
                return null;
            }

            private ActionButton addCompanyGoal() {
                ActionButton newCompanyGoalItem = getAddNewButton();

                newCompanyGoalItem.addClickHandler(event -> addNewCompanyGoal());
                newCompanyGoalItem.getElement().setId("companyGoal_id");
                return newCompanyGoalItem;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsMessages.currentlyThereAreNo(hrmsStrings.companyGoals().toLowerCase()) + " ");
                if (hasPermissionToAdd()) {
                    message.setTextBeforeLink(hrmsMessages.youCanStartRegisteringYour(hrmsStrings.companyGoals().toLowerCase()));
                    message.setHref("companygoal|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return (Utils.adminOrDirector() || Utils.hasRole(HR) || Utils.hasPermission(PermissionConstants.HRMS_EDIT_COMPANY_GOAL));//enable/disable editable option manage to employee custom fields
            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.HRMS_COMPANY_GOAL_CUSTOMIZE_BUTTON);
            }
        };
    }

    private SinksContainer addNewCompanyGoal() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("companygoal|add/add");
    }


    public String getIconStyle() {
        return "hrms company-list";
    }

    private void saveGoalEditCellValue(GoalItem rowValue, String columnCodeName) {
        HrmsAPIItem apiItem = new HrmsAPIItem();
        apiItem.setGoalItem(rowValue);
        apiItem.setColumnCodeName(columnCodeName);
        hrmsService.saveCompanyGoalEditCellValue(apiItem, new AbstractAsyncCallback<Void>() {
        });
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public String getPropertyCode() {
        return COMPANY_GOAL + GOAL;
    }

}
