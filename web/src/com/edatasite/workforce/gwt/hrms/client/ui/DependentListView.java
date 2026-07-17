package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

/**
 * User: unni
 * Date: Oct 21, 2009
 * Time: 9:51:17 PM
 */
public class DependentListView extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private ListingPanel<DependentItem> listingTable;
    private final Integer int_employeeID;
    private boolean isFromCandidate;

    public DependentListView(Integer int_employeeID,boolean isFromCandidate) {
        super(DEPENDENT_LIST);
        setDescription(property.getPlural(hrmsStrings.dependents()));
        this.int_employeeID = int_employeeID;
        this.isFromCandidate = isFromCandidate;
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_DEPENDENT)) {
            setAddNew("dependent|add/add");
        }
    }

    @Override
    public String getIconStyle() {
        return "hrms team-list";
    }

    @Override
    protected Widget onInitialize() {
        listingTable = new GuideListingPanel(ListPanelType.DependentListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());

        listingTable.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveDependantEditCellValue((DependentItem) rowValue, columnCodeName));

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DEPENDENT_ADD_EDIT, DependentListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DEPENDENT_DELETE, DependentListView.this, (sender, args) -> listingTable.reloadPage());
        add(listingTable);
        return null;
    }

    private void saveDependantEditCellValue(DependentItem rowValue, String columnCodeName) {
        HrmsService.App.get().saveDependantEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
        });
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {

        final ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        //action
        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<DependentItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final DependentItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                //dependent summary
                if (Utils.hasPermission(PermissionConstants.HRMS_DEPENDENT_SUMMARY)) {
                    MenuPopItem dependentSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-salary-grade-small");
                    dependentSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("dependent|summary/" + item.getObjectId(), item.getFirstName(), item.getLastName()));
                    actionItemCount++;
                    menuBar.addItem(dependentSummary);
                }
                //edit dependent
                if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_DEPENDENT)) {
                    MenuPopItem dependentEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    dependentEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("dependent|edit/" + item.getObjectId()+"/true", item.getFirstName(), item.getLastName()));
                    actionItemCount++;
                    menuBar.addItem(dependentEdit);
                }
                //delete dependent
                if (Utils.hasPermission(PermissionConstants.HRMS_DEPENDENT_REMOVE)) {
                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                HrmsService.App.get().deleteDependent(item.getObjectId(), new AbstractAsyncCallback<Void>() {
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DEPENDENT_DELETE, result, DependentListView.this);
                                        Info.show(hrmsStrings.DependentHasBeenDeleted(), Info.Type.INFO);
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    menuBar.addItem(removeItem);
                }

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //dependent first name
        columnConfig = new ColumnDefinitionConfig<DependentItem, SimpleLink>(wfmStrings.firstName(), DependentItem.FIRSTNAME, 200) {
            @Override
            public SimpleLink getCellValue(DependentItem rowValue) {
                return getLink(rowValue.getFirstName(), "dependent|summary/" + rowValue.getObjectId(), rowValue.getFirstName(), rowValue.getLastName());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columns.add(columnConfig);

        //dependent last name
        columnConfig = new ColumnDefinitionConfig<DependentItem, SimpleLink>(wfmStrings.lastName(), DependentItem.LASTNAME, 200) {
            @Override
            public SimpleLink getCellValue(DependentItem rowValue) {
                return getLink(rowValue.getLastName(), "dependent|summary/" + rowValue.getObjectId(), rowValue.getFirstName(), rowValue.getLastName());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columns.add(columnConfig);

        //dependent relationship
        columnConfig = new ColumnDefinitionConfig<DependentItem, String>(wfmStrings.relationship(), DependentItem.RELATIONSHIP, 200) {
            @Override
            public String getCellValue(DependentItem rowValue) {
                return rowValue.getRelationship();
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columns.add(columnConfig);

        //dependent phone 1
        columnConfig = new ColumnDefinitionConfig<DependentItem, String>(wfmStrings.phone(), DependentItem.PHONE1, 200) {
            @Override
            public String getCellValue(DependentItem rowValue) {
                return new PhoneNumber(rowValue.getPhone1()).toString();
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columns.add(columnConfig);

        //dependent phone 2
        columnConfig = new ColumnDefinitionConfig<DependentItem, String>(wfmStrings.phone()+" 2", DependentItem.PHONE2, 200) {
            @Override
            public String getCellValue(DependentItem rowValue) {
                return new PhoneNumber(rowValue.getPhone2()).toString();
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columns.add(columnConfig);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                if (isFromCandidate) {
                    return Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_DEPENDENT) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("dependent|add/add/" + int_employeeID+"/true") : null;
                } else {
                    return Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_DEPENDENT) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged("dependent|add/add/" + int_employeeID) : null;
                }            }

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
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_DEPENDENT)) {
                    ActionButton newProjectItem = getAddNewButton();
                    newProjectItem.addClickHandler(event -> {
                        if (isFromCandidate) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("dependent|add/add/" + int_employeeID+"/true");
                        } else {
                            if (int_employeeID != null) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("dependent|add/add/" + int_employeeID);
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged("dependent|add/add");
                            }
                        }
                    });
                    return newProjectItem;
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.cerrentlyDependents());
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_DEPENDENT)) {
                    message.setTextBeforeLink(hrmsStrings.beforeLinkMessage());
                    message.setHref("dependent|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.HRMS_EDIT_DEPENDENT);
            }
        };
    }

    private ListingRequestProvider<DependentItem> getListingRequestProvider() {
        return (fp, callback) -> loadDependents(fp, callback, null);
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadDependents(new ListingFilterParameter(), null, container);
    }

    private void loadDependents(ListingFilterParameter fp, ListingCallback callback, Span container) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        if (isFromCandidate) {
            fp.setFromCandidate(isFromCandidate);
            fp.setContactID(int_employeeID);
        } else {
            fp.setEmployeeId(int_employeeID);
        }
        HrmsService.App.get().getDependentsList(fp, new AbstractAsyncCallback<ListResult<DependentItem>>() {
            @Override
            public void failure(Throwable caught) {
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            @Override
            public void success(ListResult<DependentItem> result) {
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
        return DEPENDENT_LIST;
    }
}