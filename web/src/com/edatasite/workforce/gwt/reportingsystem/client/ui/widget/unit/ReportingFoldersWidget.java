package com.edatasite.workforce.gwt.reportingsystem.client.ui.widget.unit;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEvent;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.FolderType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.FolderRpc;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Virus on 9/11/14.
 */
public class ReportingFoldersWidget extends Composite {

    interface ReportingFoldersUiBinder extends UiBinder<HTMLPanel, ReportingFoldersWidget> {
    }

    private Integer categoryId = 0;
    private static HashMap<String, Integer> moduleArray;
    private static ReportingFoldersUiBinder ourUiBinder = GWT.create(ReportingFoldersUiBinder.class);
    private final static WfmStrings wfmStrings = WfmStrings.App.get();

    protected ListDataProvider<FolderRpc> dataProvider = null;
    protected DataGrid<FolderRpc> cellTable = null;
    public static final ProvidesKey<FolderRpc> KEY_PROVIDER = new ProvidesKey<FolderRpc>() {
        public Object getKey(FolderRpc item) {
            return item == null ? null : item.getId();
        }
    };
    @UiField
    HTMLPanel folderList;
    /*@UiField
    ButtonElement addButton;*/
    private String searchKey;

    public ReportingFoldersWidget(String searchKey){
        initWidget(ourUiBinder.createAndBindUi(this));
        this.searchKey = searchKey;
        onInitialize();
    }

    public ReportingFoldersWidget(Integer categoryId) {
        initWidget(ourUiBinder.createAndBindUi(this));
        if (categoryId == -1){
            this.categoryId = null;
        }else {
            this.categoryId = categoryId;
        }
        searchKey = null;
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {

            }

            @Override
            public void onSuccess() {
                onInitialize();
            }
        });
//        initHandler(addButton);
    }
/*
    public void initHandler(ButtonElement button) {
        DOM.sinkEvents((Element) button.cast(), Event.ONCLICK);
        DOM.setEventListener((Element) button.cast(), new EventListener() {
            @Override
            public void onBrowserEvent(Event event) {
                AddEditReportingFolder folder = new AddEditReportingFolder(categoryId);
                folder.addCloseHandler(new CloseHandler<PopupPanel>() {
                    @Override
                    public void onClose(CloseEvent<PopupPanel> event) {
                        refresh();
                    }
                });
                folder.open();
            }
        });

    }*/

    public Widget onInitialize() {
        dataProvider = new ListDataProvider<>();
        cellTable = new DataGrid<>(30, KEY_PROVIDER);
        cellTable.setStyleName("table table-condensed table-bordered table-hover table_report table_report_sections valign_middle table_leftIndex");
        cellTable.setWidth("100%");
        cellTable.setHeight("397px");
        cellTable.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noResultsFoundForTheProvidedSearchCriteria(), "", null));
        addDataDisplay(cellTable);
        folderList.add(cellTable);
        initsializationStructure();
        initLoading(searchKey);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REPORTING_FOLDER_SAVED, ReportingFoldersWidget.this, new WfmUiEvent() {
            public void onWfmUiEvent(Widget sender, Object args) {
                refresh();
            }
        });

        return null;
    }

    private void initLoading(String searchKey) {
        LoadingPanel.loading(true);
        if (searchKey != null){
            loadingSearch(searchKey);
        }else{
            loading();
        }
    }

    private void loadingSearch(String searchKey) {
        LoadingPanel.loading(true);
        ReportingService.App.get().searchFolders(searchKey.toLowerCase(), new AbstractAsyncCallback<FolderRpc[]>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(FolderRpc[] result) {
                List<FolderRpc> list = dataProvider.getList();
                list.clear();
                Collections.addAll(list, result);
                LoadingPanel.loading(false);
            }
        });
        LoadingPanel.loading(false);
    }

    private void loading() {
        LoadingPanel.loading(true);
        ReportingService.App.get().getFolders(categoryId, new AbstractAsyncCallback<FolderRpc[]>() {
            @Override
            public void onSuccess(FolderRpc[] result) {
                List<FolderRpc> list = dataProvider.getList();
                moduleArray = new HashMap<>();
                list.clear();
                Collections.addAll(list, result);
                for (FolderRpc item : result) {
                    if (item.getCategoryId() != null){
                        moduleArray.put(item.getCategoryName(), item.getCategoryId());
                    }
                }
                LoadingPanel.loading(false);
            }

            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }
        });
        LoadingPanel.loading(false);
    }

    public void addDataDisplay(HasData<FolderRpc> display) {
        dataProvider.addDataDisplay(display);
    }

    private void initsializationStructure() {
        Column<FolderRpc, String> catagoryType = new Column<FolderRpc, String>(new TextCell()) {
            @Override
            public String getValue(FolderRpc folderRpc) {
                return folderRpc.getCategoryName();
            }
        };
        Column<FolderRpc, String> name = new Column<FolderRpc, String>(new TextCell()) {
            @Override
            public String getValue(FolderRpc folderRpc) {
                return folderRpc.getName();
            }
        };
        Column<FolderRpc, String> description = new Column<FolderRpc, String>(new TextCell()) {
            @Override
            public String getValue(FolderRpc folderRpc) {
                return folderRpc.getDescription();
            }
        };
        Column<FolderRpc, String> type = new Column<FolderRpc, String>(new TextCell()) {
            @Override
            public String getValue(FolderRpc folderRpc) {
                return folderRpc.getType();
            }
        };
        Column<FolderRpc, String> createdBy = new Column<FolderRpc, String>(new TextCell()) {
            @Override
            public String getValue(FolderRpc folderRpc) {
                return folderRpc.getCreatedBy();
            }
        };
        Column<FolderRpc, Date> createdDate = new Column<FolderRpc, Date>(new DateCell(DateUtils.getFormat())) {
            @Override
            public Date getValue(FolderRpc folderRpc) {
                return folderRpc.getCreatedDate();
            }
        };
        Column<FolderRpc, String> edit = new Column<FolderRpc, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(FolderRpc folderRpc) {
                return wfmStrings.edit();
            }

            @Override
            public void render(Cell.Context context, FolderRpc object, SafeHtmlBuilder sb) {
                if (!FolderType.System.name().equals(object.getType())) {
                    super.render(context, object, sb);
                }
            }
        };
        Column<FolderRpc, String> delete = new Column<FolderRpc, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(FolderRpc folderRpc) {
                return wfmStrings.delete();
            }

            @Override
            public void render(Cell.Context context, FolderRpc object, SafeHtmlBuilder sb) {
                if (!FolderType.System.name().equals(object.getType())) {
                    super.render(context, object, sb);
                }
            }
        };

        cellTable.addColumn(catagoryType, wfmStrings.apps());
        cellTable.addColumn(name, wfmStrings.folder());
        cellTable.addColumn(description, wfmStrings.description());
        cellTable.addColumn(type, wfmStrings.type());
        cellTable.addColumn(createdBy, wfmStrings.createdBy());
        cellTable.addColumn(createdDate, wfmStrings.createdDate());
        cellTable.setColumnWidth(catagoryType, "70px");
        cellTable.setColumnWidth(name, "70px");
        cellTable.setColumnWidth(description, "140px");
        cellTable.setColumnWidth(type, "70px");
        cellTable.setColumnWidth(createdBy, "80px");
        cellTable.setColumnWidth(createdDate, "70px");
        cellTable.addColumn(edit);
        cellTable.addColumn(delete);
        cellTable.setColumnWidth(edit, "50px");
        cellTable.setColumnWidth(delete, "50px");
//        edit.setFieldUpdater(new FieldUpdater<FolderRpc, String>() {
//            @Override
//            public void update(int index, FolderRpc object, String value) {
//                AddEditReportingFolder editFolder = new AddEditReportingFolder(object.getId(), categoryId);
//                editFolder.addCloseHandler(new CloseHandler<MaterialModal>() {
//                    @Override
//                    public void onClose(CloseEvent<MaterialModal> closeEvent) {
//                        refresh();
//                    }
//                });
//                editFolder.open();
//            }
//        });
        delete.setFieldUpdater(new FieldUpdater<FolderRpc, String>() {
            @Override
            public void update(int index, final FolderRpc object, String value) {

                final WfmMessageBox msg = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, wfmStrings.doYouReallyWantTodeleteThisFolder());
                msg.addCloseHandler(new com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        ReportingService.App.get().deleteFolder(object.getId(), new AbstractAsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                LoadingPanel.loading(false);
                                refresh();
                            }
                        });
                    }
                });
                msg.open();
            }
        });
    }

    public void refresh() {
        initLoading(searchKey);
    }

    public static SelectItem[] changeSelectItem(){
        ArrayList<SelectItem> list = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Integer> entry : moduleArray.entrySet()) {
            list.add(i++, new SelectItem(entry.getValue(), entry.getKey()));
        }
        return list.toArray(new SelectItem[list.size()]);
    }

    public void setSearchKey(String searchKey){
        this.searchKey = searchKey;
    }
}