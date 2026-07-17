package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.project.client.rpc.ContractListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;

/**
 * User: Faxriddin Taslimov Date: 26.08.2015
 */

public class ContractListView extends BaseListView implements Constants {

    private final ProjectServiceAsync projectService = ProjectService.App.get();

    private ListingPanel<ContractListItem> listingTable;
    protected ContextMenu actions;
    protected ActionButton actionButton;

    protected String relationType;
    protected Integer relationID;

    public ContractListView() {
        this(null, null);
    }

    public ContractListView(String relationType, Integer relationID) {
        super(CONTRACT_LIST);
        setDescription(property.getPlural(wfmStrings.contracts()));
        if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_ADD_EDIT)) {
            setAddNew("contract|add/add");
        }
        this.relationType = relationType;
        this.relationID = relationID;
    }

    public FlowPanel getHelpContainer() {
        if (helpPanel == null) {
            helpPanel = HelpPanelGenerator.getHelpPanel(PermissionConstants.PM_CONTEXT, PermissionConstants.PM_CONTRACT_LIST);
        }
        return helpPanel;
    }


    @Override
    public String getIconStyle() {
        return "bgMark icon-project";
    }


    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.ContractListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign());
        listingTable.setPDFListener(clickEvent -> {
            if (listingTable.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL;
            pdfURL = CommandConstants.PDF_URL + "/contractListPDFHandler";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            listingTable.callListPDF(pdfURL, fp);
        });
        listingTable.setExcelListener(clickEvent -> {
            if (listingTable.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL = CommandConstants.COMMON_URL + "/downloadContractListExcel";
            ListingFilterParameter fp = listingTable.getFilterParametrs();
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            listingTable.callListExcel(excelURL, fp);
        });
        add(listingTable);
        registrationEvents();
        listingTable.reloadPage();
        return null;
    }

    private void registrationEvents() {

        int ON_CONTRACT_ADD = WfmUiEventType.ON_CONTRACT_ADD, ON_PROJECT_ADD = WfmUiEventType.ON_PROJECT_ADD, ON_CONTRACT_EDIT = WfmUiEventType.ON_CONTRACT_EDIT,
                ON_CONTRACT_DELETE = WfmUiEventType.ON_CONTRACT_DELETE, ON_PROJECT_DELETE = WfmUiEventType.ON_PROJECT_DELETE;

        WfmUiEventsBus.addWfmUiListener(ON_CONTRACT_ADD, ContractListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(ON_PROJECT_ADD, ContractListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(ON_CONTRACT_EDIT, ContractListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(ON_CONTRACT_DELETE, ContractListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(ON_PROJECT_DELETE, ContractListView.this, (sender, args) -> listingTable.reloadPage());
    }

    private int actionItemCount;

    private ColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();
        // Action
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<ContractListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(ContractListItem rowValue) {
                actionItemCount = 0;
                MenuBar actions = new MenuBar(true);
                MenuPopItem contractSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-project-small");
                contractSummary.ensureDebugId("summaryView");

                contractSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("contract|summary/" + rowValue.getObjectId(), rowValue.getNumber(), rowValue.getClient()));
                if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_SUMMARY)) {
                    actionItemCount++;
                    actions.addItem(contractSummary);
                }

                MenuPopItem contractEdit = new MenuPopItem(wfmStrings.edit(), "icon-project-edit-small");
                contractEdit.ensureDebugId("editProject");
                contractEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("contract|edit/" + rowValue.getObjectId(), rowValue.getNumber(), rowValue.getClient()));

                if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_ADD_EDIT)) {
                    actionItemCount++;
                    actions.addItem(contractEdit);
                }

                MenuPopItem contractConvertToProject = new MenuPopItem(wfmStrings.convertTo() + " " + Property.get(Constants.PROJECT, wfmStrings.project()).toLowerCase(), "msProject-icon");
                contractConvertToProject.ensureDebugId("convertToProject");
                contractConvertToProject.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("project|add/add/contract/" + rowValue.getObjectId()));
                if (Utils.hasPermission(PermissionConstants.PM_PROJECT_ADD) && rowValue.getProject() == null) {
                    actionItemCount++;
                    actions.addItem(contractConvertToProject);
                }

                MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                removeItem.ensureDebugId("delete");
                removeItem.setCommand(() -> {
                    WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(wfmStrings.sureYouWantToDelete());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            projectService.deleteContract(rowValue.getObjectId(), new AbstractAsyncCallback<Void>() {
                                public void failure(Throwable throwable) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void success(Void result) {
                                    LoadingPanel.loading(false);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTRACT_DELETE, result, ContractListView.this);
                                    Info.show(wfmStrings.yourContractHasBeenDeleted(), Info.Type.INFO);
                                }
                            });
                        }
                    });
                    message.open();
                });

                if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_DELETE)) {
                    actionItemCount++;
                    actions.addItem(removeItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(actions);

                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        column.setFooterName("");
        columnConfigs.add(column);

        // Contract Number
        column = new ColumnDefinitionConfig<ContractListItem, SimpleLink>(wfmStrings.number(), ContractListItem.NUMBER, 50) {
            @Override
            public SimpleLink getCellValue(ContractListItem rowValue) {
                return new SimpleLink(rowValue.getNumber(), "contract|summary/" + rowValue.getObjectId(), rowValue.getClient(), rowValue.getNumber());
            }
        };
        column.setMinimumColumnWidth(45);
        column.setFooterName("");
        columnConfigs.add(column);

        // Client Name
        column = new ColumnDefinitionConfig<ContractListItem, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), ContractListItem.CLIENT, 100) {
            @Override
            public String getCellValue(ContractListItem rowValue) {
                return rowValue.getClient();
            }
        };
        column.setMinimumColumnWidth(95);
        column.setFooterName("");
        columnConfigs.add(column);

        // Project
        column = new ColumnDefinitionConfig<ContractListItem, String>(Property.get(Constants.PROJECT, wfmStrings.project()), ContractListItem.PROJECT, 120) {
            @Override
            public String getCellValue(ContractListItem rowValue) {
                return rowValue.getProject();
            }
        };
        column.setMinimumColumnWidth(115);
        column.setFooterName("");
        columnConfigs.add(column);
        // LastNoteComment
        column = new ColumnDefinitionConfig<ContractListItem, String>(wfmStrings.notes(), ContractListItem.LAST_NOTE_COMMENT, 100) {
            @Override
            public String getCellValue(ContractListItem rowValue) {
                String result = rowValue.getLastNoteComment() == null ? "" : rowValue.getLastNoteComment();
                return result.substring(0, Math.min(result.length(), 300));
            }
        };
        columnConfigs.add(column);

        // Contract Registration Date
        column = new ColumnDefinitionConfig<ContractListItem, String>(wfmStrings.dateOfRegistration(), ContractListItem.CONTRACT_REGISTRATION_DATE, 80) {
            @Override
            public String getCellValue(ContractListItem rowValue) {
                return rowValue.getCreationTime() != null ? DateUtils.format(rowValue.getCreationTime()) : null;
            }
        };
        column.setMinimumColumnWidth(70);
        column.setFooterName("");
        column.setShow(false);
        columnConfigs.add(column);

        // Contract Begin Date
        column = new ColumnDefinitionConfig<ContractListItem, String>(wfmStrings.contractStart(), ContractListItem.CONTRACT_START_DATE, 80) {
            @Override
            public String getCellValue(ContractListItem rowValue) {
                return rowValue.getContractBeginDate() != null ? DateUtils.format(rowValue.getContractBeginDate()) : null;
            }
        };
        column.setMinimumColumnWidth(70);
        column.setFooterName("");
        column.setShow(false);
        columnConfigs.add(column);

        // Contract End Date
        column = new ColumnDefinitionConfig<ContractListItem, String>(wfmStrings.contractEnd(), ContractListItem.CONTRACT_END_DATE, 80) {
            @Override
            public String getCellValue(ContractListItem rowValue) {
                return rowValue.getContractEndDate() != null ? DateUtils.format(rowValue.getContractEndDate()) : null;
            }
        };
        column.setMinimumColumnWidth(70);
        column.setFooterName("");
        column.setShow(false);
        columnConfigs.add(column);

        return columnConfigs.toArray(new ColumnDefinitionConfig[columnConfigs.size()]);
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
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

                    @Override
                    public long initSimpleFilterType() {
                        return ListingChooseFilter.CONTRACT_LIST;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return addContract();
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption option, MaterialDropDown menuContainer) {
                option.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noContractsTextAdmin());
                if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_ADD_EDIT)) {
                    message.setHref("contract|add/add");
                    message.setTextBeforeLink(wfmStrings.noContractLink());
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.PM_CONTRACT_ADD_EDIT);
            }

        };
    }

    private ActionButton addContract() {
        if (Utils.hasPermission(PermissionConstants.PM_CONTRACT_ADD_EDIT)) {
            ActionButton newContractItem = getAddNewButton();
            newContractItem.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("contract|add/add"));
            return newContractItem;
        } else {
            return null;
        }
    }

    private ListingRequestProvider<ContractListItem> getListingRequestProvider() {

        return (filterParametrs, callback) -> {
            filterParametrs.setRelationType(relationType);
            filterParametrs.setRelationID(relationID);
            projectService.getContractList(filterParametrs, new AbstractAsyncCallback<ListResult<ContractListItem>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(ListResult<ContractListItem> contractList) {
                    callback.onSuccess(contractList);
                }
            });
        };
    }

    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return CONTRACT_LIST;
    }
}
