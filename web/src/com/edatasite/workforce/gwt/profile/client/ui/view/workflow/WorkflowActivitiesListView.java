package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * User: abror
 * Date: 10/23/15 3:58 PM
 */
public class WorkflowActivitiesListView extends BaseListView implements Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();

    public static final ProfileServiceAsync profileService = ProfileService.App.get();
    public static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();

    private ListingPanel<WorkflowRule> list;
    private ListingFilterParameter filterParametr;

    public WorkflowActivitiesListView() {
        super(WORKFLOW_ACTIVITIES_LIST, Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, settingsStrings.upcomingactivities(), wfmStrings.activities()));
    }

    public void refresh() {
        filterParametr.setStart(0);
        list.reloadPage();
    }

    protected Widget onInitialize() {

        list = new ListingPanel<>(ListPanelType.WorkflowActivitiesListPanel, getColumnConfigs(), getListData(), getDisagn());
        WfmUiEventsBus.addWfmUiListener(WorkflowActivitiesListView.this, (sender, args) -> refresh(), WfmUiEventType.ON_WORKFLOW_ACTIVITIES_UPDATE, WfmUiEventType.ON_WORKFLOW_ACTIVITIES_DELETE, WfmUiEventType.ON_WORKFLOW_ACTIVITIES_ADD);

        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        final ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column = null;

        column = new ColumnDefinitionConfig<WorkflowRule, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final WorkflowRule item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                MenuPopItem remove = new MenuPopItem(wfmStrings.stop(), "icon-remove");
                remove.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(settingsStrings.messAreStopRecurrence()
                                          + " " + item.getName() + " " + wfmStrings.recurrence());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            profileService.stopUpcomingRecurrence(item.getRecurrenceID(), new AbstractAsyncCallback<Void>() {
                                @Override
                                public void failure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                                }

                                @Override
                                public void success(Void result) {
                                    LoadingPanel.loading(false);
                                    Info.show(settingsStrings.successfullyStoped(), Info.Type.INFO);
                                    list.reloadPage();
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
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, Widget>(settingsStrings.entityName(), CustomFormConstants.WORKFLOW_FORM.RULE_ENTITY_NAME, 180) {
            @Override
            public Widget getCellValue(final WorkflowRule item) {
                Label label = new Label(item.getEntityName() != null ? item.getEntityName() : "n/a");
                label.addClickHandler(clickEvent -> {
                    Window.open(GWT.getHostPageBaseURL() + item.getEntityLink(), "_blank", "");
                });
                return label;
            }
        };
        column.setMinimumColumnWidth(40);
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, HTML>(wfmStrings.name(), CustomFormConstants.WORKFLOW_FORM.NAME, 180) {
            @Override
            public HTML getCellValue(final WorkflowRule item) {
                if (WorkflowRule.WORKFLOW_SMS_ALERT.equals(item.getActivitiesType())) {
                    SimpleLink link = new SimpleLink(item.getName() != null ? item.getName() : wfmStrings.notAvailable());
                    link.addClickHandler(clickEvent -> new WorkflowSMSAlertView(item.getEntityId(), item.getObjectID()));
                    return link;
                } else if (WorkflowRule.WORKFLOW_ALERT.equals(item.getActivitiesType())) {
                    return getLink(item.getName(), "workflowalert|summary/" + item.getEntityId() + "/" + (item.getObjectID() != null ? item.getObjectID() : ""));
                } else if (WorkflowRule.WORKFLOW_EVENT.equals(item.getActivitiesType())) {
                    return getLink(item.getName(), "workflowevent|summary/" + item.getEntityId() + "/" + (item.getObjectID() != null ? item.getObjectID() : "") + "/" + (item.isCallLog() ? "call" : ""));
                } else if (WorkflowRule.WORKFLOW_TASK.equals(item.getActivitiesType())) {
                    return getLink(item.getName(), "taskedit|edittask/" + item.getEntityId() + "/" + Constants.WORKFLOW);
                } else if (WorkflowRule.WORKFLOW_ONBOARDING_STEP.equals(item.getActivitiesType())) {
                    SimpleLink link = new SimpleLink(item.getName() != null ? item.getName() : wfmStrings.notAvailable());
                    link.addClickHandler(clickEvent -> new WorkflowEmployeeStepView(item.getEntityId(), item.getObjectID()));
                    return link;
                } else {
                    return new HTML(item.getName() != null ? item.getName() : wfmStrings.notAvailable());
                }
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, String>(wfmStrings.type(), WorkflowRule.TYPE, 30) {
            @Override
            public String getCellValue(WorkflowRule item) {
                return localizeType(item.getActivitiesType());
            }
        };
        column.setMinimumColumnWidth(20);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, String>(settingsStrings.executionDate(), WorkflowRule.EXECUTION_DATE, 60) {
            @Override
            public String getCellValue(WorkflowRule item) {
                return item.getExecutionDate() != null ? DateUtils.formatInternal(item.getExecutionDate()) : "";
            }
        };
        column.setMinimumColumnWidth(30);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, String>(wfmStrings.executionCriteria(), WorkflowRule.EXECUTION_CRITERIA, 50) {
            @Override
            public String getCellValue(WorkflowRule item) {
                return item.getExecutionCriteria().name();
            }
        };
        column.setMinimumColumnWidth(30);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, String>(wfmStrings.apps(), CustomFormConstants.WORKFLOW_FORM.MODULE, 50) {
            @Override
            public String getCellValue(WorkflowRule item) {
                return localize(item.getModule());
            }
        };
        column.setMinimumColumnWidth(30);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, String>(wfmStrings.createdBy(), CustomFormConstants.WORKFLOW_FORM.CREATOR, 70) {
            @Override
            public String getCellValue(WorkflowRule item) {
                return item.getCreator() != null ? item.getCreator() : "";
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<WorkflowRule, String>(wfmStrings.ruleName(), WorkflowRule.RULE_NAME, 70) {
            @Override
            public String getCellValue(WorkflowRule item) {
                return item.getActivitiesRuleName() != null ? item.getActivitiesRuleName() : "";
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    public static String localizeType(String activitiesType) {
        if (activitiesType == null) {
            activitiesType = wfmStrings.notAvailable();
        } else if (WorkflowRule.WORKFLOW_ALERT.equals(activitiesType)) {
            return wfmStrings.email();
        } else if (WorkflowRule.WORKFLOW_EVENT.equals(activitiesType)) {
            return Property.get(Constants.EVENT_LIST, wfmStrings.event());
        } else if (WorkflowRule.WORKFLOW_TASK.equals(activitiesType)) {
            return wfmStrings.task();
        } else if (WorkflowRule.WORKFLOW_SMS_ALERT.equals(activitiesType)) {
            return wfmStrings.sms();
        } else if (WorkflowRule.WORKFLOW_ONBOARDING_STEP.equals(activitiesType)) {
            return wfmStrings.onboardingStep();
        } else {
            return wfmStrings.notAvailable();
        }
        return activitiesType;
    }

    public static String localize(String module) {
        if (module == null) {
            return wfmStrings.notAvailable();
        }
        if (module.equals(WorkflowRule._WORKFLOW_MODULE)) {
            return "Module";
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_LEAD)) {
            return Property.get(Constants.LEADS, wfmStrings.lead());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_CONTACT)) {
            return Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.crmContact(), wfmStrings.contacts());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_CASE)) {
            return Property.get(Constants.CASE_LIST, wfmStrings.crmCase());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_ACTIVITY)) {
            return Property.get(Constants.EVENT_LIST, wfmStrings.event());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_LOGACALL)) {
            return Property.get(Constants.LOGACALL, wfmStrings.logCall());
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_SCHEDULED_COURSE)) {
            return wfmStrings.courseSchedule();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_CS_STUDENT)) {
            return settingsStrings.csStudent();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE)) {
            return wfmStrings.employee();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_SALEQUOTE)) {
            return wfmStrings.salesQuote();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_PURCHASE)) {
            return wfmStrings.requestForPurchase();
        } else if (module.equals(WorkflowRule._WORKFLOW_MODULE_PURCHASEORDER)) {
            return wfmStrings.purchaseorder();
        } else if (module.contains(WorkflowRule._WORKFLOW_MODULE)) {
            String name = module.replaceAll(WorkflowRule._WORKFLOW_MODULE + "_", "");
            return name.replace("_", " ").trim();
        }
        return module;
    }

    private ListingRequestProvider<WorkflowRule> getListData() {
        return (filterParametrs, callback) -> {
            if (filterParametr == null) {
                filterParametr = new ListingFilterParameter();
            }
            filterParametrs.setStart(filterParametr.getStart());
            filterParametr = filterParametrs;
            filterParametr.setSearchKey(filterParametrs.getSearchKey());
            filterParametr.setStart(filterParametrs.getStart());
            filterParametr.setLimit(filterParametrs.getLimit());
            filterParametr.setSearchType(1);
//            profileService.getWorkflowActivitiesList(filterParametr, new AsyncCallback<ListResult<WorkflowRule>>() {
//                @Override
//                public void onFailure(Throwable throwable) {
//                    callback.onFailure(throwable);
//                }
//
//                @Override
//                public void onSuccess(ListResult<WorkflowRule> result) {
//                    callback.onSuccess(result);
//                }
//            });
            allInOneService.getWorkflowActivitiesList(filterParametr, new AsyncCallback<ListResult<WorkflowRule>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<WorkflowRule> result) {
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
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyDonotHaveAny(Property.getPluralWithObjectCode(Constants.EVENT_LIST, wfmStrings.activities())));
                emptyDataTable.initEmptyDataTable(message);

            }
        };
    }

    @Override
    public String getIconStyle() {
        return "event event-list";
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
}
