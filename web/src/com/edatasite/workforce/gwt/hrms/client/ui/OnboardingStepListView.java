package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.*;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/25/12
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class OnboardingStepListView extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsServiceAsync hrmsService = HrmsService.App.get();
    private ListingPanel<OnboardingItem> listingTable;
    private HashSet selectedItems = new HashSet();
    private boolean fromBackend;
    private SchemaLookUp schemaLookUp;
    private SchemaLookUp copySchemaLookup;
    private Integer schemaID;

    public OnboardingStepListView() {
        super(ONBOARDING_STEP, wfmStrings.onboardingStep());
        if (hasPermissionAdd()) {
            setAddNew(ONBOARDING_STEP + "|add/add");
        }
    }

    private boolean hasPermissionAdd() {
        return !fromBackend && Utils.hasPermission(HRMS_ONBOARDING_STEP_ADD);
    }

    public OnboardingStepListView(boolean fromBackend) {
        this();
        this.fromBackend = fromBackend;
        if (hasPermissionAdd()) {
            setAddNew(ONBOARDING_STEP + "|add/add");
        }
    }

    protected Widget onInitialize() {
        listingTable = fromBackend ? new GuideListingPanel(ListPanelType.OnboardingStepListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX)
                : new GuideListingPanel(ListPanelType.OnboardingStepListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());

        listingTable.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveOnboardingStepCellValue((OnboardingItem) rowValue, columnCodeName));

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ONBOARDING_STEP_ADD_EDIT, OnboardingStepListView.this, (sender, args) -> listingTable.reloadPage());
        if (fromBackend) {
            listingTable.addSelectionRowHandler(selectedRows -> selectedItems = selectedRows);
        }
        add(listingTable);
        return null;
    }

    private void saveOnboardingStepCellValue(OnboardingItem rowValue, String columnCodeName) {
        hrmsService.saveOnboardingStepEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
        });
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionAdd() ? OnboardingStepListView.this::addNewOnboardingStep : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }


            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionAdd()) {
                    ActionButton addNewStep = getAddNewButton();
                    addNewStep.addClickHandler(event -> addNewOnboardingStep());
                    return addNewStep;
                }
                return null;
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                if (fromBackend) {
                    HorizontalPanel topPanel = new HorizontalPanel();
                    topPanel.setSpacing(5);
                    topPanel.add(new HTML(wfmStrings.company() + ": "));
                    getSchemaLookUp(topPanel);

                    ActionButton item = new ActionButton(wfmStrings.copy(), ActionButton.Type.BUTTON);
                    item.addClickHandler(clickEvent -> {
                        if (selectedItems.size() == 0) {
                            Info.show(wfmMessages.pleaseChooseOneOrMoreItemsToCopy(), Info.Type.INFO);
                        } else if (schemaLookUp.getSelectedItemID() == null || schemaLookUp.getSelectedItemID() < 1) {
                            Info.show(wfmMessages.pleaseChooseCompanyToCopyItemsFrom(), Info.Type.INFO);
                        } else {
                            openCopyPopup(getIDsOnly(selectedItems));
                        }
                    });
                    topPanel.add(item);

                    return topPanel;
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.cerrentlySteps());
                if (!fromBackend) {
                    message.setTextBeforeLink(wfmStrings.addStepsByClicking());
                    message.setHref(ONBOARDING_STEP + "|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.HRMS_ONBOARDING_STEP_EDIT);
            }
        };
    }

    private SinksContainer addNewOnboardingStep() {
        return SinksContainerFactory.entryPoint.onHistoryChanged(ONBOARDING_STEP + "|add/add");
    }

    public static ArrayList<Integer> getIDsOnly(Set<OnboardingItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (OnboardingItem item : selectedItems) {
            ids.add(item.getStepId());
        }
        return ids;
    }


    private void openCopyPopup(final ArrayList<Integer> objectIDs) {
        final KpiModal popup = new KpiModal();
        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(5);
        copySchemaLookup = new SchemaLookUp();
        copySchemaLookup.getFilterParametrs().setDiscludedSchemaID(schemaLookUp.getSelectedItemID());
        verticalPanel.add(copySchemaLookup);
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        WfmButton2 copy = new WfmButton2(wfmStrings.copy());
        copy.addClickHandler(clickEvent -> {
            if (copySchemaLookup.getSelectedItemID() != null && copySchemaLookup.getSelectedItemID() > 0) {
                LoadingPanel.loading(true);
                hrmsService.copyOnboardingSteps(objectIDs, schemaLookUp.getSelectedItemID(), copySchemaLookup.getSelectedItemID(), new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                        popup.close();
                        Info.show(wfmMessages.errorOccuredWhileCopyingSteps(), Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        LoadingPanel.loading(false);
                        popup.close();
                        Info.show((objectIDs.size() - result) + " " + wfmMessages.onboardingStepsHasBeenCopiedSucc(), Info.Type.INFO);
                    }
                });
            } else {
                Info.show(wfmMessages.pleaseChooseCompanyToCopyItemsTo(), Info.Type.INFO);
            }
        });
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancel.addClickHandler(clickEvent -> popup.close());
        horizontalPanel.setSpacing(5);
        horizontalPanel.add(copy);
        horizontalPanel.add(cancel);
        verticalPanel.add(horizontalPanel);
        verticalPanel.setCellHorizontalAlignment(horizontalPanel, HasHorizontalAlignment.ALIGN_CENTER);
        popup.add(verticalPanel);
        popup.open();
    }

    private void getSchemaLookUp(HorizontalPanel topPanel) {
        schemaLookUp = new SchemaLookUp();
        topPanel.add(schemaLookUp);
        schemaLookUp.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (suggestionSelectionEvent.getSelectedItem() != null) {
                schemaID = schemaLookUp.getOracle().getItemID(suggestionSelectionEvent.getSelectedItem().getDisplayString());
                listingTable.reloadPage();
            }
        });
        schemaLookUp.getSuggestBox().addBlurHandler(blurEvent -> {
            if (schemaLookUp.getOracle().getItemID(schemaLookUp.getText()) != null) {
                schemaID = schemaLookUp.getOracle().getItemID(schemaLookUp.getText());
                listingTable.reloadPage();
            }
        });
    }

    private ListingRequestProvider<OnboardingItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            if (fromBackend) {
                if (schemaID != null) {
                    filterParametrs.setCompanyID(schemaID);
                    getListingRequest(callback, filterParametrs);
                }
            } else {
                getListingRequest(callback, filterParametrs);
            }
        };
    }

    private void getListingRequest(final ListingCallback<OnboardingItem> callback, ListingFilterParameter filterParametrs) {
        hrmsService.getOnboardingStepdList(filterParametrs, new AbstractAsyncCallback<ListResult<OnboardingItem>>() {
            @Override
            public void failure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void success(ListResult<OnboardingItem> result) {
                callback.onSuccess(result);
            }
        });
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columnConfigs = new ColumnDefinitionConfig[5];
        columnConfigs[0] = new ColumnDefinitionConfig<OnboardingItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final OnboardingItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                int menuItemCount = 0;
                if (!fromBackend) {
                    //Edit  item
                    if (Utils.hasPermission(HRMS_ONBOARDING_STEP_EDIT)) {
                        final MenuPopItem onboardingStepEdit = new MenuPopItem(wfmStrings.edit(), "");
                        onboardingStepEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(ONBOARDING_STEP + "|add/add/" + rowValue.getStepId(), rowValue.getStepName()));
                        menuItemCount++;
                        menuBar.addItem(onboardingStepEdit);
                    }
                    //Delete item
                    if (Utils.hasPermission(HRMS_ONBOARDING_STEP_DELETE)) {
                        MenuPopItem deleteOnboardingStep = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                        deleteOnboardingStep.setCommand(() -> {
                            final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                            message.setTitle(wfmStrings.warning());
                            message.setMessage(wfmStrings.sureYouWantToDelete());
                            message.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    LoadingPanel.loading(true);
                                    hrmsService.onboardingStepDelete(rowValue.getStepId(), new AbstractAsyncCallback<Void>() {
                                        @Override
                                        public void failure(Throwable throwable) {
                                            LoadingPanel.loading(false);
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        @Override
                                        public void success(Void result) {
                                            LoadingPanel.loading(false);
                                            listingTable.reloadPage();
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.onboarding()), Info.Type.INFO);
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ONBOARDING_STEP_DELETE, rowValue, OnboardingStepListView.this);
                                        }
                                    });
                                }
                            });
                            message.open();
                        });
                        menuItemCount++;
                        menuBar.addItem(deleteOnboardingStep);
                    }
                } else {
                    final MenuPopItem copy = new MenuPopItem(wfmStrings.copy(), "");
                    copy.setCommand(() -> {
                        ArrayList<Integer> objectIDs = new ArrayList<>();
                        objectIDs.add(rowValue.getStepId());
                        openCopyPopup(objectIDs);
                    });
                    menuItemCount++;
                    menuBar.addItem(copy);
                }

                ToolItem toolItem = new ToolItem(menuItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfigs[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH + 20);
        columnConfigs[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH + 20);

        columnConfigs[1] = new ColumnDefinitionConfig<OnboardingItem, SimpleLink>(wfmStrings.name(), OnboardingItem.ONBOARDING_STEP_NAME, 200) {
            @Override
            public SimpleLink getCellValue(OnboardingItem rowValue) {
                return getLink(rowValue.getStepName(), ONBOARDING_STEP + "|add/add/" + rowValue.getStepId(), rowValue.getStepName());
            }
        };
        columnConfigs[1].setMinimumColumnWidth(50);
        columnConfigs[2] = new ColumnDefinitionConfig<OnboardingItem, String>(wfmStrings.description(), OnboardingItem.ONBOARDING_STEP_DESCRIPTION, 200) {
            @Override
            public String getCellValue(OnboardingItem rowValue) {
                return rowValue.getStepDescription() != null ? rowValue.getStepDescription() : wfmStrings.notAvailable();
            }
        };
        columnConfigs[2].setMinimumColumnWidth(50);
        columnConfigs[3] = new ColumnDefinitionConfig<OnboardingItem, String>(wfmStrings.period(), OnboardingItem.ONBOARDING_PERIOD_NAME, 200) {
            @Override
            public String getCellValue(OnboardingItem rowValue) {
                return rowValue.getPeriodName() != null ? rowValue.getPeriodName() : wfmStrings.notAvailable();
            }
        };
        columnConfigs[3].setMinimumColumnWidth(50);
        columnConfigs[4] = new ColumnDefinitionConfig<OnboardingItem, String>(wfmStrings.previousStep(), OnboardingItem.ONBOARDING_PERIOD_PARENT_STEP, 200) {
            @Override
            public String getCellValue(OnboardingItem rowValue) {
                return rowValue.getParentName() != null ? rowValue.getParentName() : wfmStrings.notAvailable();
            }
        };
        columnConfigs[4].setMinimumColumnWidth(50);

        return columnConfigs;
    }

    public String getIconStyle() {
        return "onboardingStep onboardingStep-list";
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
