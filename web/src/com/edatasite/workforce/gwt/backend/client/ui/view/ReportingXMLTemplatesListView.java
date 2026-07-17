package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.CheckListBox;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 22.10.2011
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */

public class ReportingXMLTemplatesListView extends BaseListView {
    private static final int EDIT = 0;
    private static final int DELETE = 1;
    private static final int INSERT = 2;
    private static final int APPLYY_TO_MULTIDB = 3;

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final BackendStrings backendStrings = BackendStrings.App.get();
    private final CoreServiceAsync coreService = CoreService.App.get();
    private ListingPanel<ReportingListItem> listingTable;

    public ReportingXMLTemplatesListView(String name) {
        super("reportingXMLTemplateList", name);
    }

    @Override
    public String getIconStyle() {
        return "backend reportXmlTemListView";
    }

    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.ReportingXMLTemplatesListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        add(listingTable);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REPORT_TEMPLATE_ADD_EDIT, ReportingXMLTemplatesListView.this, (sender, args) -> listingTable.reloadPage());
        return null;
    }

    private void getActions(MenuBar menuBar, final ReportingListItem... template) {
        if (template != null && template.length == 1) {
            MenuPopItem editItem = new MenuPopItem(wfmStrings.edit());
            editItem.setCommand(() -> getCommand(EDIT, template));
            menuBar.addItem(editItem);
        }

        MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete());
        removeItem.setCommand(() -> {
            ReportingListItem[] items = template;
            if (items == null) {
                items = listingTable.getPagingScrollTable().getSelectedRowValues().toArray(new ReportingListItem[]{});
            }
            getCommand(DELETE, items);
        });
        menuBar.addItem(removeItem);

        MenuPopItem insertCommand = new MenuPopItem(wfmStrings.viewDetails());
        insertCommand.setCommand(() -> {
            ReportingListItem[] items = template;
            if (items == null) {
                items = listingTable.getPagingScrollTable().getSelectedRowValues().toArray(new ReportingListItem[]{});
            }
            getCommand(INSERT, items);
        });
        menuBar.addItem(insertCommand);

        MenuPopItem applyToAll = new MenuPopItem(wfmStrings.apply() + " " + wfmStrings.to() + " " + wfmStrings.all());
        applyToAll.setCommand(() -> {
            ReportingListItem[] items = template;
            if (items == null) {
                items = listingTable.getPagingScrollTable().getSelectedRowValues().toArray(new ReportingListItem[]{});
            }
            getCommand(APPLYY_TO_MULTIDB, items);
        });
        menuBar.addItem(applyToAll);
    }

    private void getCommand(int edit, final ReportingListItem... templates) {
        switch (edit) {
            case EDIT: {
                SinksContainerFactory.entryPoint.onHistoryChanged("reportxmltemplate|add/add/" + templates[0].getTemplateId());
            }
            break;
            case DELETE: {
                final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                //message.setSize(300, 150);
                message.setTitle(wfmStrings.delete());
                StringBuilder builder = new StringBuilder();
                builder.append(" ");
                for (ReportingListItem item : templates) {
                    builder.append(" ").append(item.getTemplateName()).append(",");
                }
                builder.delete(builder.length() - 1, builder.length());
                message.setMessage(wfmStrings.sureYouWantToDelete());
                message.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        final int[] n = {0};
                        for (final ReportingListItem template : templates) {
                            coreService.deleteReportingXMLTemplateFromCompany(template.getTemplateId(), null, new AbstractAsyncCallback<Void>() {
                                public void failure(Throwable throwable) {
                                    if (templates.length == ++n[0]) {
                                        LoadingPanel.loading(false);
                                    }
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                public void success(Void result) {
                                    if (templates.length == ++n[0]) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.messSuccessfulyyDeleted(), Info.Type.INFO);
                                        listingTable.reloadPage();
                                    }
                                }
                            });
                        }
                    }
                });
                message.open();
            }
            break;
            case INSERT: {
                Integer[] IDs = new Integer[templates.length];
                int i = -1;
                for (ReportingListItem item : templates) {
                    IDs[++i] = item.getTemplateId();
                }

                coreService.getInsertCommand(IDs, new AsyncCallback<String>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (Utils.isNullOrEmpty(s)) {
                            return;
                        }
                        KpiModal dialogBox = new KpiModal();
                        dialogBox.setCloseButton(true);
                        RichTextArea richTextBox = new RichTextArea();
                        richTextBox.setText(s);
                        richTextBox.setWidth("600px");
                        richTextBox.setHeight("500px");
                        dialogBox.add(richTextBox);
                        dialogBox.setSize("610px", "510px");
                        dialogBox.center();
                    }
                });
            }
            break;
            case APPLYY_TO_MULTIDB: {
                final Integer[] IDs = new Integer[templates.length];
                int i = -1;
                for (ReportingListItem item : templates) {
                    IDs[++i] = item.getTemplateId();
                }
                final KpiModal kpiModal = new KpiModal();
                Label label = new Label(wfmStrings.choose() + " " + wfmStrings.company());
                ArrayList<SelectItem> selectItems = new ArrayList<>();
                selectItems.add(new SelectItem(0, "AWS Free"));
                selectItems.add(new SelectItem(1, "AWS Paid"));
                selectItems.add(new SelectItem(2, "App Free"));
                selectItems.add(new SelectItem(3, "App Paid"));
                final CheckListBox companies = new CheckListBox(selectItems);

                Button save = new Button(wfmStrings.save());
                Button cancel = new Button(wfmStrings.cancel());

                kpiModal.add(label);
                kpiModal.add(companies);
                kpiModal.addButton(save);
                kpiModal.addButton(cancel);
                save.addClickHandler(clickEvent -> {
                    BackendService.Reporting.get().applyToMultiDBReportTemplate(IDs, companies.getSelectedItems(), new AsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                        }

                        @Override
                        public void onSuccess(Void s) {
                        }
                    });
                    kpiModal.close();
                });
                cancel.addClickHandler(clickEvent -> kpiModal.close());
                kpiModal.center();


            }
            break;
            default:
                break;
        }
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columnConfigs = new ColumnDefinitionConfig[6];
        // Action
        columnConfigs[0] = new ColumnDefinitionConfig<ReportingListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {

            @Override
            public Anchor getCellValue(final ReportingListItem rowValue) {
                MenuBar menuBar = new MenuBar(true);
                getActions(menuBar, rowValue);

                final ToolItem toolItem = new ToolItem(2);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfigs[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs[0].setColumnSortable(false);

        columnConfigs[1] = new ColumnDefinitionConfig<ReportingListItem, SimpleLink>(wfmStrings.name(), "templateName", 50) {
            @Override
            public SimpleLink getCellValue(ReportingListItem rowValue) {
                return new SimpleLink(rowValue.getTemplateName(), "reportxmltemplate|add/add/" + rowValue.getTemplateId());
            }
        };
        columnConfigs[1].setMaximumColumnWidth(200);
        columnConfigs[1].setColumnSortable(true);

        columnConfigs[2] = new ColumnDefinitionConfig<ReportingListItem, String>(wfmStrings.templateBody(), "templateBody", 100) {
            @Override
            public String getCellValue(ReportingListItem rowValue) {
                return rowValue.getTemplateBody();
            }
        };
        columnConfigs[2].setMaximumColumnWidth(250);

        columnConfigs[3] = new ColumnDefinitionConfig<ReportingListItem, String>(wfmStrings.templateType(), "templateType", 100) {
            @Override
            public String getCellValue(ReportingListItem rowValue) {
                return rowValue.getTemplateType();
            }
        };

        columnConfigs[4] = new ColumnDefinitionConfig<ReportingListItem, Boolean>(wfmStrings.isLibrary(), "isLibrary", 100) {
            @Override
            public Boolean getCellValue(ReportingListItem rowValue) {
                return rowValue.isLibrary();
            }
        };

        columnConfigs[5] = new ColumnDefinitionConfig<ReportingListItem, String>(wfmStrings.orderNumber(), "order", 40) {

            @Override
            public String getCellValue(ReportingListItem rowValue) {
                return "" + rowValue.getOrder();
            }

            @Override
            public void setCellValue(ReportingListItem rowValue, String cellValue) {
                rowValue.setOrder(Integer.valueOf(cellValue));
                saveCellValue(rowValue);
            }
        };
        initCellEdit(columnConfigs);
        return columnConfigs;
    }

    private void initCellEdit(ColumnDefinitionConfig[] columnConfigs) {
        TextBoxCellEditor<String> widget = new TextBoxCellEditor<String>() {
            @Override
            protected String getValue() {
                return getText();
            }

            @Override
            protected void setValue(String cellValue) {
                setText(cellValue);
            }
        };
        columnConfigs[5].setCellEditor(widget);
        columnConfigs[5].setCellChangesSave((CellChange<ReportingListItem>) (rowValue, columnCodeName) -> saveReportTemplateEditCellValue(rowValue));
    }

    private void saveReportTemplateEditCellValue(ReportingListItem rowValue) {
        CoreService.App.get().saveReportTemplate(rowValue, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void aVoid) {
                listingTable.reloadPage();
            }
        });
    }


    private ListingRequestProvider<ReportingListItem> getListingRequestProvider() {
        return (filterParameter, callback) -> {
            filterParameter.setLibrary(true);
            coreService.getReportingXMLTemplateList(filterParameter, new AbstractAsyncCallback<ListResult<ReportingListItem>>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(ListResult<ReportingListItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
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
                        return ListingChooseFilter.REPORTING_XML_TEMPLATES;
                    }
                };
            }

            @Override
            public HorizontalPanel initTopToolBarWidgets() {
                HorizontalPanel widgets = new HorizontalPanel();

                final ActionButton toolItem = new ActionButton(ActionButton.getMoreString(), ActionButton.Type.TOOLMENU);
                toolItem.addClickHandler(clickEvent -> {
                    MenuBar menuBar = new MenuBar(true);
                    menuBar.setAutoOpen(true);
                    toolItem.setMenu(menuBar);
                    getActions(menuBar, null);
                });
                widgets.add(toolItem);


                ActionButton addNew = new ActionButton(backendStrings.addReportTemplateCategory(), "", ActionButton.Type.BUTTON);
                addNew.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("reporttemplatecategory|add/add"));
                widgets.add(addNew);

                ActionButton addXmlTemplate = new ActionButton(backendStrings.addXMLTemplate(), "", ActionButton.Type.BUTTON);
                addXmlTemplate.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("reportxmltemplate|add/add"));
                widgets.add(addXmlTemplate);
                return widgets;
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

            }
        };
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
