package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.core.client.*;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

/**
 * User: Sherzod
 * Date: Jun 17, 2009
 * Time: 8:02:46 PM
 */
public class PerformanceNoteListView extends BaseListView implements Constants {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private ListingPanel<PerformanceNoteItem> list;

    public PerformanceNoteListView() {
        super("noteList");
        setDescription(Property.getPluralWithObjectCode("performanceNote", wfmStrings.performanceNotes()));
        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PERFORMANCE_NOTE)) {
            setAddNew("performancenote|add/add");
        }
    }

    @Override
    public String getIconStyle() {
        return "assessment note-list";
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.PerformanceNoteListPanel, getColumnsConfig(), getProvider(), getDesign());

        list.setPDFListener(clickEvent -> {
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/noteListPDFHandler";
            ListingFilterParameter filterParameters = list.getFilterParametrs();
            filterParameters.setPropertyCode(getPropertyCode());
            list.callListPDF(pdfURL, filterParameters);
        });

        list.setExcelListener(clickEvent -> {
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/downloadNoteListExcel";
            ListingFilterParameter filterParameters = list.getFilterParametrs();
            filterParameters.setPropertyCode(getPropertyCode());
            list.callListExcel(excelURL, filterParameters);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PERFORMANCE_NOTE_ADD, PerformanceNoteListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PERFORMANCE_NOTE_DELETE, PerformanceNoteListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnsConfig() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[7];
        int index = 0;
        //performance note action
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final PerformanceNoteItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                //performance note summary
                if (Utils.hasPermission(PermissionConstants.HRMS_PERFORMANCE_NOTE_SUMMERY)) {
                    MenuPopItem noteSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-notes-small");
                    noteSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("performancenote|summary/" + item.getObjectID(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(noteSummary);
                }
                //performance note edit
                if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_PERFORMANCE_NOTE)) {
                    MenuPopItem editNote = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    editNote.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("performancenote|edit/" + item.getObjectID(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(editNote);
                }
                //performance note delete
                if (Utils.hasPermission(PermissionConstants.HRMS_REMOVE_PERFORMANCE_NOTE)) {
                    MenuPopItem deleteNote = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deleteNote.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.confirmation());
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
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PERFORMANCE_NOTE_DELETE, result, PerformanceNoteListView.this);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.performanceNote()), Info.Type.INFO);
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
        //performance note name
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, SimpleLink>(wfmStrings.name(), PerformanceNoteItem.NAME, 140) {
            @Override
            public SimpleLink getCellValue(PerformanceNoteItem item) {
                return getLink(item.getName(), "performancenote|summary/" + item.getObjectID(), item.getName());
            }
        };
        columns[index++].setMinimumColumnWidth(100);
        //performance note related to
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, String>(wfmStrings.relatedTo(), PerformanceNoteItem.RELATED_TO, 100) {
            @Override
            public String getCellValue(PerformanceNoteItem item) {
                return item.getRelatedToName() != null ? item.getRelatedToName() : "";
            }
        };
        columns[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[index].setColumnSortable(true);
        columns[index++].setMinimumColumnWidth(80);
        //performance note period
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, String>(wfmStrings.period(), PerformanceNoteItem.PERIOD, 110) {
            @Override
            public String getCellValue(PerformanceNoteItem item) {
                return DateUtils.format(item.getStartDate()) + " - " + DateUtils.format(item.getEndDate());
            }
        };
        columns[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[index++].setMinimumColumnWidth(100);
        //performance note status
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, String>(wfmStrings.status(), PerformanceNoteItem.STATUS, 100) {
            @Override
            public String getCellValue(PerformanceNoteItem item) {
                return item.getStatusName();
            }
        };
        columns[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);

        columns[index++].setMinimumColumnWidth(90);
        //performance note reported by
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, String>(wfmStrings.reportedBy(), PerformanceNoteItem.REPORTED_BY, 120) {
            @Override
            public String getCellValue(PerformanceNoteItem item) {
                return item.getReportedByName();
            }
        };
        columns[index].setColumnSortable(true);
        columns[index++].setMinimumColumnWidth(100);
        //performance note resolver/owner
        columns[index] = new ColumnDefinitionConfig<PerformanceNoteItem, String>(wfmStrings.resolverOwner(), PerformanceNoteItem.RESOLVER, 120) {
            @Override
            public String getCellValue(PerformanceNoteItem item) {
                return item.getResolverName();
            }
        };
        columns[index].setColumnSortable(true);
        columns[index++].setMinimumColumnWidth(100);
        return columns;
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PERFORMANCE_NOTE)) {
                    ActionButton newNoteItem = getAddNewButton();
                    newNoteItem.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("performancenote|add/add"));
                    return newNoteItem;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.noPerformanceNotes());
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PERFORMANCE_NOTE)) {
                    message.setTextBeforeLink(hrmsStrings.addPerformanceNote() + " ");
                    message.setHref("performancenote|add/add");
                }
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<PerformanceNoteItem> getProvider() {
        return (fp, callback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            HrmsService.App.get().getPerformanceNoteList(fp, new AbstractAsyncCallback<ListResult<PerformanceNoteItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void success(ListResult<PerformanceNoteItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

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
        return "performanceNote";
    }
}
