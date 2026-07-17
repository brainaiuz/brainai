package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/25/12
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class OnboardingPeriodListView extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsServiceAsync hrmsService = HrmsService.App.get();
    private ListingPanel<OnboardingItem> listingTable;

    public OnboardingPeriodListView() {
        super(ONBOARDING_PERIOD, wfmStrings.onboardingPeriod());
        if (hasPermissionAdd()) {
            setAddNew(ONBOARDING_PERIOD + "|add/add");
        }

    }

    private boolean hasPermissionAdd() {
        return Utils.hasPermission(HRMS_ONBOARDING_ADD);
    }

    protected Widget onInitialize() {
        listingTable = new GuideListingPanel(ListPanelType.OnboardingPeriodListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ONBOARDING_PERIOD_ADD_EDIT, OnboardingPeriodListView.this, (sender, args) -> listingTable.reloadPage());
        add(listingTable);
        listingTable.reloadPage();
        return null;
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionAdd() ? OnboardingPeriodListView.this::addNewOnboardingPeriod : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

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
                if (hasPermissionAdd()) {
                    ActionButton newProjectItem = getAddNewButton();

                    newProjectItem.addClickHandler(event -> addNewOnboardingPeriod());
                    return newProjectItem;
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.cerrentlyPeriods());
                message.setTextBeforeLink(wfmStrings.addPeriodsByClicking());
                message.setHref(ONBOARDING_PERIOD + "|add/add");
                emptyDataTable.initEmptyDataTable(message);
            }

        };
    }

    private SinksContainer addNewOnboardingPeriod() {
        return SinksContainerFactory.entryPoint.onHistoryChanged(ONBOARDING_PERIOD + "|add/add");
    }

    private ListingRequestProvider<OnboardingItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            //     filterParametrs.setEmployeeId(employeeId);
            hrmsService.getOnboardingPeriodList(filterParametrs, new AbstractAsyncCallback<ListResult<OnboardingItem>>() {


                @Override
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void success(ListResult<OnboardingItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columnConfigs = new ColumnDefinitionConfig[6];
        columnConfigs[0] = new ColumnDefinitionConfig<OnboardingItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final OnboardingItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
                //Edit  item
                if (Utils.hasPermission(HRMS_ONBOARDING_EDIT)) {
                    final MenuPopItem onboardingPeriodEdit = new MenuPopItem(wfmStrings.edit(), "");
                    onboardingPeriodEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(ONBOARDING_PERIOD + "|add/add/" + rowValue.getPeriodId(), rowValue.getPeriodName()));
                    menuItemCount++;
                    menuBar.addItem(onboardingPeriodEdit);
                }
                //Delete item
                if (Utils.hasPermission(HRMS_ONBOARDING_DELETE)) {
                    final MenuPopItem deleteOnboardingPeriod = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteOnboardingPeriod.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                hrmsService.onboardingPeriodDelete(rowValue.getPeriodId(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        listingTable.reloadPage();
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.onboardingPeriod()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ONBOARDING_PERIOD_DELETE, result, OnboardingPeriodListView.this);
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    menuItemCount++;
                    menuBar.addItem(deleteOnboardingPeriod);
                }
                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();  //return action menu items
            }
        };
        columnConfigs[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs[0].setColumnSortable(false);
        columnConfigs[1] = new ColumnDefinitionConfig<OnboardingItem, SimpleLink>(wfmStrings.name(), OnboardingItem.ONBOARDING_PERIOD_NAME, 200) {
            @Override
            public SimpleLink getCellValue(OnboardingItem rowValue) {
                return getLink(rowValue.getPeriodName(), ONBOARDING_PERIOD + "|add/add/" + rowValue.getPeriodId(), rowValue.getPeriodName());
            }
        };
        columnConfigs[1].setMinimumColumnWidth(50);
        columnConfigs[2] = new ColumnDefinitionConfig<OnboardingItem, String>(wfmStrings.description(), OnboardingItem.ONBOARDING_PERIOD_DESCRIPTION, 200) {
            @Override
            public String getCellValue(OnboardingItem rowValue) {
                return rowValue.getPeriodDescription();
            }
        };
        columnConfigs[2].setMinimumColumnWidth(50);
        columnConfigs[3] = new ColumnDefinitionConfig<OnboardingItem, Integer>(wfmStrings.period(), OnboardingItem.ONBOARDING_PERIOD_RELIATIVE_START, 200) {
            @Override
            public Integer getCellValue(OnboardingItem rowValue) {
                return rowValue.getPeriodRelativeStart();
            }
        };
        columnConfigs[3].setMinimumColumnWidth(50);
        columnConfigs[4] = new ColumnDefinitionConfig<OnboardingItem, Integer>(wfmStrings.duration(), OnboardingItem.ONBOARDING_PERIOD_DURATION, 200) {
            @Override
            public Integer getCellValue(OnboardingItem rowValue) {
                return rowValue.getDuration();
            }
        };
        columnConfigs[4].setMinimumColumnWidth(50);
        columnConfigs[5] = new ColumnDefinitionConfig<OnboardingItem, String>(wfmStrings.active(), OnboardingItem.ONBOARDING_PERIOD_ACTIVE, 200) {
            @Override
            public String getCellValue(OnboardingItem rowValue) {
                return rowValue.getPeriodActive() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfigs[5].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs[5].setMinimumColumnWidth(50);

        return columnConfigs;
    }

    public String getIconStyle() {
        return "onboardingPeriod onboardingPeriod-list";
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
