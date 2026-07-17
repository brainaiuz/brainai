package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.backend.client.rpc.SchemaList;
import com.edatasite.workforce.gwt.backend.client.rpc.SchemaListItem;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Nov 4, 2010
 * Time: 3:58:20 PM
 */
public class SchemaListView extends BaseListView implements Constants {
    private final BackendServiceAsync backendService = BackendService.App.get();
    private final BackendStrings backendStrings = BackendStrings.App.get();
    private ListingPanel<SchemaListItem> listingTable;

    public SchemaListView() {
        super(SCHEMA_LIST, wfmStrings.companyManagement());
    }

    private ListingFilterParameter getFiterParametrs() {
        return null;
    }

    public String getIconStyle() {
        return "backend schemaListView";
    }

    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.ShemaListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());
        listingTable.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadSchemaListExcel";
            ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
            if (getFiterParametrs() != null && getFiterParametrs().getProjectId() != null) {
                filterParametrs.setProjectId(getFiterParametrs().getProjectId());
            }
            if (getFiterParametrs() != null && getFiterParametrs().getDepartmentId() != null) {
                filterParametrs.setDepartmentId((getFiterParametrs().getDepartmentId()));
            }
            listingTable.callListExcel(excelURL, filterParametrs);
        });

        listingTable.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/schemaListPDFHandler";
            ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
            if (getFiterParametrs() != null && getFiterParametrs().getProjectId() != null) {
                filterParametrs.setProjectId(getFiterParametrs().getProjectId());
            }
            if (getFiterParametrs() != null && getFiterParametrs().getDepartmentId() != null) {
                filterParametrs.setDepartmentId((getFiterParametrs().getDepartmentId()));
            }
            listingTable.callListPDF(pdfURL, filterParametrs);
        });

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SCHEMA_ADD, SchemaListView.this, (sender, args) -> listingTable.reloadPage());
        add(listingTable);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();
        // Action
        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<SchemaListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final SchemaListItem rowValue) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (rowValue != null) {
                    MenuPopItem dropSchema = new MenuPopItem(backendStrings.removeCompany(), "icon-task-small");
                    dropSchema.setCommand(() -> {
                        KpiModal modal = new KpiModal();
                        modal.setWidth(300);
                        modal.setTitle(backendStrings.removeCompany());
                        modal.setCloseButton(true);
                        TextBox textBox = new TextBox();
                        Validation.addNumericKeyboardListener(textBox);
                        modal.addWidget(textBox, backendStrings.companyID());
                        modal.addButton(new WfmButton2(wfmStrings.delete(), WfmButton2.BTN_PRIMARY, clickEvent -> {
                            if (!Validation.validateTextBoxRequired(textBox)) {
                                Info.warn(backendStrings.pleaseEnterCompanyID());
                                return;
                            } else if (!Integer.valueOf(textBox.getText()).equals(rowValue.getObjectID())) {
                                Info.warn(backendStrings.companyIDDoesntMatch());
                                return;
                            }
                            LoadingPanel.loading(true);
                            BackendService.App.get().removeCompany(rowValue.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.warn(backendStrings.schemaDeletionFailOnServerSide() + rowValue.getObjectID());
                                }

                                @Override
                                public void success(Boolean aBoolean) {
                                    LoadingPanel.loading(false);
                                    if (aBoolean != null && aBoolean) {
                                        modal.close();
                                        listingTable.reloadPage();
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), backendStrings.schema()) + rowValue.getObjectID());
                                    } else {
                                        Info.warn(wfmStrings.errorOccurredWhileDeleting() + rowValue.getObjectID());
                                    }
                                }
                            });
                        }));
                        modal.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(dropSchema);

                    MenuPopItem backupCompanyPublicData = new MenuPopItem(backendStrings.backupCompanyPublicData(), "icon-task-small");
                    backupCompanyPublicData.setCommand(() -> BackendService.App.get().getInsertPublicData(rowValue.getObjectID(), new AbstractAsyncCallback<String>() {
                        @Override
                        public void failure(Throwable throwable) {
                            Info.show(backendStrings.backupCompanyPublicDataFailServerSide() + " " + rowValue.getObjectID() + " " + wfmStrings.message() + ": " + throwable.getMessage(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(String query) {
                            showPublicData(rowValue.getObjectID() + " _ " + rowValue.getName(), query);
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(backupCompanyPublicData);

                    MenuPopItem backupSchema = new MenuPopItem(backendStrings.backupSchema(), "icon-task-small");
                    backupSchema.setCommand(() -> BackendService.App.get().backupSchema(rowValue.getObjectID(), new AbstractAsyncCallback<String>() {
                        @Override
                        public void failure(Throwable throwable) {
                            Info.show(backendStrings.backupSchemaFailServerSide() + " " + rowValue.getObjectID() + " " + wfmStrings.message() + ": " + throwable.getMessage(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(String query) {
                            Info.show(wfmStrings.done() + ":" + query, Info.Type.WARNING);
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(backupSchema);

                    MenuPopItem setUnderMaintenance = new MenuPopItem(backendStrings.setUnderMaintenace(), "icon-task-small");
                    setUnderMaintenance.setCommand(() -> BackendService.App.get().setCompanyUnderMaintenance(rowValue.getObjectID(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            Info.show(backendStrings.settingSchemaUnderMaintenanceFailedDue() + " " + throwable.getMessage(), Info.Type.WARNING);
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            Info.show(wfmStrings.done(), Info.Type.INFO);
                            listingTable.reloadPage();
                        }
                    }));
                    actionItemCount++;
                    menuBar.addItem(setUnderMaintenance);

                    MenuPopItem backupCompanyfiles = new MenuPopItem("Backup Company documents", "icon-task-small");
                    backupCompanyfiles.setCommand(() -> {
                        LoadingPanel.loading(true);
                        BackendService.App.get().backupCompanyDocuments(rowValue.getObjectID(), new AsyncCallback<String>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void onSuccess(String result) {
                                LoadingPanel.loading(false);
                                if (result != null) {
                                    KpiModal dialogBox = getInfoDialogBox(result);
                                    dialogBox.open();
                                } else {
                                    Info.show("No uploaded files found", Info.Type.WARNING);
                                }
                            }
                        });
                    });
                    actionItemCount++;
                    menuBar.addItem(backupCompanyfiles);

                    MenuPopItem backendManagementListView = new MenuPopItem(backendStrings.backendManagement(), "icon-fulldetails");
                    backendManagementListView.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("backendManagement|backendManagementView/" + rowValue.getObjectID() + "/" + rowValue.getName()));
                    actionItemCount++;
                    menuBar.addItem(backendManagementListView);

                    final MenuPopItem rebaseCompanyFiles = new MenuPopItem(backendStrings.companyFileTransfer(), "icon-solution-small");
                    rebaseCompanyFiles.setCommand(() -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("companyFileTransferView|fileTransferView/" + rowValue.getObjectID());
                    });
                    actionItemCount++;
                    menuBar.addItem(rebaseCompanyFiles);

                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);
        // Schema Name
        columnConfig = new ColumnDefinitionConfig<SchemaListItem, SimpleLink>(backendStrings.companyID(), SchemaListItem.NAME, 90) {

            @Override
            public SimpleLink getCellValue(SchemaListItem rowValue) {
                return new SimpleLink(rowValue.getObjectID() + "", "schema|summary/" + rowValue.getObjectID());
            }
        };
        columnConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(80);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<SchemaListItem, SimpleLink>(wfmStrings.companyName(), SchemaListItem.COMPANY_NAME, 70) {

            @Override
            public SimpleLink getCellValue(SchemaListItem rowValue) {
                if (rowValue != null && rowValue.getName() != null) {
                    return new SimpleLink(rowValue.getName(), "schema|summary/" + rowValue.getObjectID());
                } else {
                    return new SimpleLink("");
                }
            }
        };
        columnConfig.setMinimumColumnWidth(80);
        columnConfigs.add(columnConfig);
        // Schema Description
//        columnConfig = new ColumnDefinitionConfig<SchemaListItem, String>(wfmStrings.descriptionField(), SchemaListItem.DESCRIPTION, 120) {
//
//            @Override
//            public String getCellValue(SchemaListItem rowValue) {
//                return rowValue.getDescription();
//            }
//        };
//        columnConfig.setColumnSortable(false);
//        columnConfig.setMinimumColumnWidth(120);
//        columnConfigs.add(columnConfig);
        // Client Name
//        columnConfig = new ColumnDefinitionConfig<SchemaListItem, String>(backendStrings.databaseType(), SchemaListItem.FREE, 90) {
//
//            @Override
//            public String getCellValue(SchemaListItem rowValue) {
//                return rowValue.getFree() + "";
//            }
//        };
//        columnConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//        columnConfig.setColumnSortable(false);
//        columnConfig.setMinimumColumnWidth(100);

        columnConfig = new ColumnDefinitionConfig<SchemaListItem, String>(backendStrings.isMaintenance(), SchemaListItem.MAINTENANCE, 100) {
            @Override
            public String getCellValue(SchemaListItem rowValue) {
                return rowValue.getMaintenance();
            }
        };
        columnConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfig.setColumnSortable(false);
        columnConfigs.add(columnConfig);

        return columnConfigs.toArray(new ColumnDefinitionConfig[]{});
    }

    private void showPublicData(String title, String query) {
        KpiModal kpiModal = new KpiModal();
        kpiModal.setTitle(title);
        kpiModal.addStyleName("file--SchemaListView");
        kpiModal.setWidth(650);
        kpiModal.setScrollable(true);
        kpiModal.setCloseButton(true);
        TextArea textArea = new TextArea();
        textArea.setHeight("500px");
        textArea.setText(query);
        kpiModal.add(textArea);
        kpiModal.open();
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("schema|add/add"));
                return addNew;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(backendStrings.thereAreNoSchemas());
                emptyDataTable.initEmptyDataTable(message);

            }
        };
    }

    private ListingRequestProvider<SchemaListItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            backendService.getSchemas(filterParametrs, new AbstractAsyncCallback<SchemaList>() {
                @Override
                public void failure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void success(SchemaList result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private KpiModal getInfoDialogBox(String fileURL) {
        final KpiModal dialogBox = new KpiModal();
        dialogBox.setCloseButton(true);
        dialogBox.setTitle("Backup company files");
        dialogBox.setTitle("Backup company files");
        FlexTable panel = new FlexTable();
        panel.setWidget(0, 0, new HTML("Your company files archived successfully. You can download it by this link:\n"));
        panel.setWidget(1, 0, new HTML(fileURL));
        panel.setWidget(2, 0, new Button(wfmStrings.close(), (ClickHandler) clickEvent -> dialogBox.close()));
        panel.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
        panel.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
        panel.getFlexCellFormatter().setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_CENTER);
        dialogBox.add(panel);
        dialogBox.center();
        return dialogBox;
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
