package com.edatasite.workforce.gwt.hrms.client.ui.talentprofile;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
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
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileEnum;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileListItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileService;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileServiceAsync;
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
 * Created by Dilsh0d Madrahimov on 11/6/2018.
 */
public class TalentProfileListView extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final TalentProfileServiceAsync talentProfileService = TalentProfileService.App.get();
    private ListingPanel<TalentProfileListItem> listingTable;

    private final Integer employeeID;

    private final boolean isFromCandidate;

    public TalentProfileListView(Integer employeeID,boolean isFromCandidate) {
        super(TALENT_PROFILE);
        setDescription(property.getPlural(hrmsStrings.educationDetails()));
        this.employeeID = employeeID;
        this.isFromCandidate = isFromCandidate;
        if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_ADD)) {
            setAddNew(TalentProfileEnum.EDUCATION.name().toLowerCase().concat("|add/add/"));
        }
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        listingTable = new GuideListingPanel(ListPanelType.TalentProfileListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TALENT_PROFILE_CHANGE, TalentProfileListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_COMPETENCY, TalentProfileListView.this, (sender, args) -> listingTable.reloadPage());
        add(listingTable);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {

        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        //action
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<TalentProfileListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final TalentProfileListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_VIEW) && !TalentProfileEnum.COMPETENCY.equals(item.getType())) {
                    MenuPopItem viewItem = new MenuPopItem(wfmStrings.summaryView());
                    viewItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(item.getType().name().toLowerCase().concat("|summary/" + item.getObjectID())));
                    actionItemCount++;
                    menuBar.addItem(viewItem);
                }

                if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_EDIT)) {
                    MenuPopItem editItem = new MenuPopItem(wfmStrings.edit());
                    editItem.setCommand(() -> {
                        if (TalentProfileEnum.COMPETENCY.equals(item.getType())) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("editskill//" + item.getObjectID());
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged(item.getType().name().toLowerCase().concat("|edit/" + item.getObjectID()));
                        }
                    });
                    actionItemCount++;
                    menuBar.addItem(editItem);
                }

                if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_DELETE)) {
                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                talentProfileService.deleteTalentProfileItem(item.getObjectID(), item.getType(), new AbstractAsyncCallback<Boolean>() {
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Boolean result) {
                                        LoadingPanel.loading(false);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TALENT_PROFILE_CHANGE, result, TalentProfileListView.this);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), hrmsStrings.talentProfile()), Info.Type.INFO);
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    menuBar.addItem(removeItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<TalentProfileListItem, SimpleLink>(wfmStrings.name(), TalentProfileListItem.NAME, 100) {
            @Override
            public SimpleLink getCellValue(TalentProfileListItem rowValue) {
                return getLink(rowValue.getName(), (TalentProfileEnum.COMPETENCY.equals(rowValue.getType()) ? "#" : rowValue.getType().name().toLowerCase().concat("|summary/" + rowValue.getObjectID())));
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<TalentProfileListItem, String>(wfmStrings.type(), TalentProfileListItem.TYPE, 100) {
            @Override
            public String getCellValue(TalentProfileListItem rowValue) {
                if (TalentProfileEnum.EDUCATION.equals(rowValue.getType())) {
                    return wfmStrings.education();
                }
                if (TalentProfileEnum.COMPETENCY.equals(rowValue.getType())) {
                    return wfmStrings.competency();
                }
                return "";
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<TalentProfileListItem, String>(wfmStrings.degree(), TalentProfileListItem.DEGREE, 100) {
            @Override
            public String getCellValue(TalentProfileListItem rowValue) {
                if (rowValue.getDegree() != null)
                    return rowValue.getDegree().getName();
                return null;
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<TalentProfileListItem, String>(wfmStrings.startDate(), TalentProfileListItem.START_DATE, 80) {
            @Override
            public String getCellValue(TalentProfileListItem rowValue) {
                return DateUtils.format(rowValue.getStartDate());
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<TalentProfileListItem, String>(wfmStrings.endDate(), TalentProfileListItem.END_DATE, 80) {
            @Override
            public String getCellValue(TalentProfileListItem rowValue) {
                return DateUtils.format(rowValue.getEndDate());
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<TalentProfileListItem, String>(wfmStrings.country(), TalentProfileListItem.COUNTRY, 80) {
            @Override
            public String getCellValue(TalentProfileListItem rowValue) {
                return rowValue.getCountry();
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);


        return columns.toArray(new CustomColumnDefinitionConfig[columns.size()]);
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {

            @Override
            public Command getAddNewItemCommand() {
                if (isFromCandidate) {
                    return Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_ADD) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged(TalentProfileEnum.EDUCATION.name().toLowerCase().concat("|add/add/") + employeeID + "/true") : null;
                } else {
                    return Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_ADD) ? () -> SinksContainerFactory.entryPoint.onHistoryChanged(TalentProfileEnum.EDUCATION.name().toLowerCase().concat("|add/add/") + employeeID + "/false") : null;
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
                if (Utils.hasPermission(PermissionConstants.HRMS_TALENT_PROFILE_ADD)) {
                    ActionButton addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                    addNew.addClickHandler(clickEvent -> addNew.setMenu(getActions()));
                    return addNew;
                }
                return null;
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

    private MenuBar getActions() {
        ContextMenu contextMenu = new ContextMenu();
        if (isFromCandidate) {
            contextMenu.addMenuItem(hrmsStrings.addEducation(), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged(TalentProfileEnum.EDUCATION.name().toLowerCase().concat("|add/add/") + employeeID + "/true"));
        } else {
            contextMenu.addMenuItem(hrmsStrings.addEducation(), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged(TalentProfileEnum.EDUCATION.name().toLowerCase().concat("|add/add/") + employeeID + "/false"));
        }//        contextMenu.addMenuItem(hrmsStrings.addAward(), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged(TalentProfileEnum.AWARD.name().toLowerCase().concat("|add/add/") + employeeID));
        contextMenu.addMenuItem(hrmsStrings.addSkill(), true, () -> SinksContainerFactory.entryPoint.onHistoryChanged("addSkill/" + employeeID));

        return contextMenu.getMenuBar();
    }

    private ListingRequestProvider<TalentProfileListItem> getListingRequestProvider() {
        return (fp, callback) -> {
            loadTalentProfiles(fp, callback, null);
        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadTalentProfiles(new ListingFilterParameter(), null, container);
    }

    private void loadTalentProfiles(ListingFilterParameter filterParameter, ListingCallback callback, Span container) {
        if (filterParameter == null) {
            filterParameter = new ListingFilterParameter();
        }
        if (isFromCandidate) {
            filterParameter.setContactID(employeeID);
        } else {
            filterParameter.setEmployeeId(employeeID);
        }
        talentProfileService.getTalentProfileList(filterParameter, new AbstractAsyncCallback<ListResult<TalentProfileListItem>>() {
            @Override
            public void failure(Throwable caught) {
                if (callback != null) {
                    callback.onFailure(caught);
                }
            }

            @Override
            public void success(ListResult<TalentProfileListItem> result) {
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
        return TALENT_PROFILE;
    }
}
