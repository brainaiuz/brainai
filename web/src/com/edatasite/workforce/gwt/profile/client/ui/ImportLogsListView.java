package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ImportLogItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Azazello on 2/5/16.
 */
public class ImportLogsListView extends BaseListView implements Constants {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final ProfileServiceAsync service = ProfileService.App.get();
    private ListingPanel<ImportLogItem> list;

    public ImportLogsListView() {
        super("importLogs", settingsStrings.importLogs());
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.ImportLogsListPanel, getColumnConfigs(), getListData(), getDesign());
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[11];
        //////////////////////////---------(1)----------
        columns[0] = new ColumnDefinitionConfig<ImportLogItem, String>(wfmStrings.type(), ImportLogItem.TYPE, 80) {
            @Override
            public String getCellValue(ImportLogItem item) {
                return item.getType() != null ? item.getType().getCode() : wfmStrings.notAvailable();
            }
        };
        columns[0].setMinimumColumnWidth(30);
        columns[0].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //////////////////////////---------(2)----------
        columns[1] = new ColumnDefinitionConfig<ImportLogItem, String>(wfmStrings.date(), ImportLogItem.DATE, 50) {
            @Override
            public String getCellValue(ImportLogItem item) {
                return DateUtils.formatInternal(item.getDate());
            }
        };
        columns[1].setMinimumColumnWidth(20);
        columns[1].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //////////////////////////---------(3)----------
        columns[2] = new ColumnDefinitionConfig<ImportLogItem, String>(wfmStrings.status(), ImportLogItem.STATUS, 50) {
            @Override
            public String getCellValue(ImportLogItem item) {
                return item.getStatus() != null ? item.getStatus().getCode() : wfmStrings.notAvailable();
            }
        };
        columns[2].setMinimumColumnWidth(20);
        columns[2].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //////////////////////////---------(4)----------
        columns[3] = new ColumnDefinitionConfig<ImportLogItem, Integer>(settingsStrings.requested(), ImportLogItem.REQUESTED, 40) {
            @Override
            public Integer getCellValue(ImportLogItem item) {
                return item.getRequestedRows();
            }
        };
        columns[3].setMinimumColumnWidth(20);
        columns[3].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[3].setColumnSortable(false);

        //////////////////////////---------(5)----------
        columns[4] = new ColumnDefinitionConfig<ImportLogItem, Integer>(settingsStrings.imported(), ImportLogItem.IMPORTED, 40) {
            @Override
            public Integer getCellValue(ImportLogItem item) {
                return item.getImportedRows();
            }
        };
        columns[4].setMinimumColumnWidth(20);
        columns[4].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[4].setColumnSortable(false);

        //////////////////////////---------(6)----------
        columns[5] = new ColumnDefinitionConfig<ImportLogItem, Integer>(wfmStrings.rejected(), ImportLogItem.REJECTED, 40) {
            @Override
            public Integer getCellValue(ImportLogItem item) {
                return item.getRejectedRows();
            }
        };
        columns[5].setMinimumColumnWidth(20);
        columns[5].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[5].setColumnSortable(false);

        //////////////////////////---------(7)----------
        columns[6] = new ColumnDefinitionConfig<ImportLogItem, Integer>(settingsStrings.skipped(), ImportLogItem.SKIPPED, 40) {
            @Override
            public Integer getCellValue(ImportLogItem item) {
                return item.getSkippedRows();
            }
        };
        columns[6].setMinimumColumnWidth(20);
        columns[6].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[6].setColumnSortable(false);

        //////////////////////////---------(8)----------
        columns[7] = new ColumnDefinitionConfig<ImportLogItem, Integer>(settingsStrings.overwritten(), ImportLogItem.OVERWRITTEN, 40) {
            @Override
            public Integer getCellValue(ImportLogItem item) {
                return item.getOverwrittenRows();
            }
        };
        columns[7].setMinimumColumnWidth(20);
        columns[7].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[7].setColumnSortable(false);

        //////////////////////////---------(9)----------
        columns[8] = new ColumnDefinitionConfig<ImportLogItem, HTML>(settingsStrings.importedFile(), ImportLogItem.IMPORT_FILE, 100) {
            @Override
            public HTML getCellValue(ImportLogItem item) {
                if (item.getImportFile() != null) {
                    return new HTML("<a href=" + item.getImportFile().getDescription() + ">" + item.getImportFile().getName() + "</a>");
                }
                return new HTML(wfmStrings.notAvailable());
            }
        };
        columns[8].setMinimumColumnWidth(40);
        columns[8].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[8].setColumnSortable(false);

        //////////////////////////---------(10)----------
        columns[9] = new ColumnDefinitionConfig<ImportLogItem, HTML>(settingsStrings.logFile(), ImportLogItem.LOG_FILE, 100) {
            @Override
            public HTML getCellValue(ImportLogItem item) {
                if (item.getLogFile() != null) {
                    return new HTML("<a href=" + item.getLogFile().getDescription() + ">" + item.getLogFile().getName() + "</a>");
                }
                return new HTML(wfmStrings.notAvailable());
            }
        };
        columns[9].setMinimumColumnWidth(40);
        columns[9].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[9].setColumnSortable(false);

        //////////////////////////---------(11)----------
        columns[10] = new ColumnDefinitionConfig<ImportLogItem, String>(wfmStrings.error(), ImportLogItem.ERROR, 50) {
            @Override
            public String getCellValue(ImportLogItem item) {
                return item.getErrorMessage();
            }
        };
        columns[10].setMinimumColumnWidth(20);
        columns[10].setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns[10].setColumnSortable(false);
        columns[10].setShow(false);

        return columns;
    }

    protected ListPanelType getPanelType() {
        return ListPanelType.EmailFilterListPanel;
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(settingsStrings.currentlyNoImportLogs());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }


    private ListingRequestProvider<ImportLogItem> getListData() {
        return (filterParametrs, callback) -> {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            service.getImportLogs(filterParametrs, new AbstractAsyncCallback<ListResult<ImportLogItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(ListResult<ImportLogItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    public String getIconStyle() {
        return "icon-logs";
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
