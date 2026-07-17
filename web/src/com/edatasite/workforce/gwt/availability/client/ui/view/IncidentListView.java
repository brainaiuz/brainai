package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

/**
 * User: Sherzod
 * Date: Jul 18, 2009
 * Time: 12:22:27 AM
 */
public class IncidentListView extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private Integer employeeID;
    private ListingPanel<PerformanceNoteItem> list;
    private boolean all = false;

    public IncidentListView() {
        super("incidentList");
        setDescription(property.getPlural(wfmStrings.incidents()));
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_INCIDENT)) {
            setAddNew("incident|add/add");
        }
    }

    public IncidentListView(boolean all) {
        super("incidentList");
        setDescription(property.getPlural(wfmStrings.incidents()));
        this.all = all;
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_INCIDENT)) {
            setAddNew("incident|add/add");
        }
    }

    public IncidentListView(Integer employeeID) {
        super("incidentList");
        setDescription(property.getPlural(wfmStrings.incidents()));
        this.employeeID = employeeID;
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_INCIDENT)) {
            setAddNew("incident|add/add");
        }
    }

    @Override
    public String getIconStyle() {
        return "hrms incident-list";
    }

    @Override
    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.IncientListPanel, drawColumns(), getProvider(), getDesigner());
        list.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadIncidentListExcel";
            ListingFilterParameter fp = list.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            list.callListExcel(excelURL, list.getFilterParametrs());
        });
        list.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/incidentListPDFHandler";
            ListingFilterParameter fp = list.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            list.callListPDF(pdfURL, fp);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INCIDENT_ADD, IncidentListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_INCIDENT_DELETE, IncidentListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[8];
        int index = 0;
        //incident action
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final PerformanceNoteItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                //incident summary
                if (Utils.hasPermission(PermissionConstants.HRMS_SUMMARY_INCIDENT)) {
                    MenuPopItem incidentSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-incident-small");
                    incidentSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("incident|summary/" + item.getObjectID(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(incidentSummary);
                }
                //incident edit
                if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_INCIDENT)) {
                    MenuPopItem editIncident = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    editIncident.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("incident|edit/" + item.getObjectID(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(editIncident);
                }
                //incident delete
                if (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_INCIDENT)) {
                    MenuPopItem deleteNote = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteNote.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        //messageBox.setSize(300, 150);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                HrmsService.App.get().deletePerformanceNote(item.getObjectID(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INCIDENT_DELETE, result, IncidentListView.this);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.incident()), Info.Type.INFO);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(deleteNote);
                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index++].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        //incident name
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, SimpleLink>(wfmStrings.name(), PerformanceNoteItem.NAME, 120) {
            @Override
            public SimpleLink getCellValue(PerformanceNoteItem item) {
                return getLink(item.getName(), "incident|summary/" + item.getObjectID(), item.getName());
            }
        };
        columns[index++].setMinimumColumnWidth(100);
        //incident related to
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, String>(wfmStrings.relatedTo(), PerformanceNoteItem.RELATED_TO, 120) {
            @Override
            public String getCellValue(PerformanceNoteItem item) {
                return item.getRelatedToName() != null ? item.getRelatedToName() : "";
            }
        };
        columns[index].setColumnSortable(true);
        columns[index++].setMinimumColumnWidth(100);

        //incident reported by
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, String>(wfmStrings.reportedBy(), PerformanceNoteItem.REPORTED_BY, 120) {
            @Override
            public String getCellValue(PerformanceNoteItem item) {
                return item.getReportedByName();
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        //incident period
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, String>(wfmStrings.period(), PerformanceNoteItem.PERIOD, 110) {
            @Override
            public String getCellValue(PerformanceNoteItem item) {
                return DateUtils.format(item.getStartDate()) + " - " + DateUtils.format(item.getEndDate());
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        //incident priority
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, String>(wfmStrings.priority(), PerformanceNoteItem.PRIORITY, 100) {
            @Override
            public String getCellValue(PerformanceNoteItem item) {
                return item.getPriorityName();
            }
        };
        columns[index++].setMinimumColumnWidth(90);

        //incident resolver/owner
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, String>(wfmStrings.resolverOwner(), PerformanceNoteItem.RESOLVER, 120) {
            @Override
            public String getCellValue(PerformanceNoteItem item) {
                return item.getResolverName();
            }
        };
        columns[index++].setMinimumColumnWidth(100);

        //incident status
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, String>(wfmStrings.status(), PerformanceNoteItem.STATUS, 100) {
            @Override
            public String getCellValue(PerformanceNoteItem item) {
                return item.getStatusName();
            }
        };
        columns[index++].setMinimumColumnWidth(90);
        return columns;
    }

    private GuideListingPanelDesign getDesigner() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_INCIDENT)) {
                    if (employeeID != null) {
                        return () -> SinksContainerFactory.entryPoint.onHistoryChanged("incident|add/add/" + employeeID);
                    } else {
                        return () -> SinksContainerFactory.entryPoint.onHistoryChanged("incident|add/add");
                    }
                }
                return null;
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
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_INCIDENT)) {
                    ActionButton newIncidentItem = getAddNewButton();
                    newIncidentItem.addClickHandler(event -> {
                        String historyToken = "incident|add/add";
                        if (employeeID != null) {
                            historyToken = historyToken + "/" + employeeID;
                        }
                        SinksContainerFactory.entryPoint.onHistoryChanged(historyToken);
                    });
                    return newIncidentItem;
                }
                return null;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.currentlyYouDoNotHaveAnyIncidents());
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_INCIDENT)) {
                    message.setTextBeforeLink(hrmsStrings.pleaseAddIncidentByClicking() + " ");
                    String hrefLink = "incident|add/add";
                    if (employeeID != null) {
                        hrefLink = hrefLink + "/" + employeeID;
                    }
                    message.setHref(hrefLink);
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };

    }

    private ListingRequestProvider<PerformanceNoteItem> getProvider() {
        return (fp, callback) -> loadIncidents(fp, callback, null);
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadIncidents(new ListingFilterParameter(), null, container);
    }

    private void loadIncidents(ListingFilterParameter fp, ListingCallback callback, Span container) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setEmployeeId(employeeID);
        fp.setIncident(true);
        fp.setAllByFilter(all);
        HrmsService.App.get().getPerformanceNoteList(fp, new AbstractAsyncCallback<ListResult<PerformanceNoteItem>>() {
            @Override
            public void failure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void success(ListResult<PerformanceNoteItem> result) {
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
        return "incidentList";
    }
}