package com.edatasite.workforce.gwt.invoice.client.ui.view.itemtablesettings;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldQuickAdd;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldSection;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.html.TD;
import gwt.material.design.client.ui.html.Table;
import gwt.material.design.client.ui.html.TableRow;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Created by Normurod on 3/22/2017.
 */
public class ItemTableCustomFieldsView extends Composite {

    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private KpiDataGrid<CompanyCustomFieldItem> dataGrid;
    private ListDataProvider<CompanyCustomFieldItem> dataProvider;

    private final ProvidesKey<CompanyCustomFieldItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectId();

    private final CustomFieldSection section;
    private ColumnSettigns columnSettigns;
    private WfmButton2 btnAddNew, btnConfigColumns;
    private final MaterialPanel pnlContainer;
    private String uuid;
    private BiConsumer<String, CustomFieldSection> consumer;
    private Integer totalColumnWidth;
    private Table footerTable;
    private TD tdTotal;
    private Span spanTotal;

    public ItemTableCustomFieldsView(CustomFieldSection section, String uuid) {
        this.uuid = uuid;
        this.section = section;

        pnlContainer = new MaterialPanel("margin-top");
        initWidget(pnlContainer);
    }

    public ItemTableCustomFieldsView(CustomFieldSection section) {
        this.section = section;

        pnlContainer = new MaterialPanel("margin-top");
        initWidget(pnlContainer);

        onInitialize();
    }

    public void clear() {
        pnlContainer.clear();
    }

    public Widget onInitialize() {
        dataProvider = new ListDataProvider<>();
        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        dataGrid.addStyleName("cellBasedWidget-mod");
        dataGrid.setPageSize(200);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("200px");
        dataGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(settingsStrings.thereAreNoFieldsYet(), "", null));
        dataProvider.addDataDisplay(dataGrid);

        btnAddNew = new WfmButton2(wfmStrings.add() + " " + wfmStrings.customField(), WfmButton2.BTN_PRIMARY);
        btnAddNew.getElement().setId("add_custom_field_button");
        btnAddNew.addClickHandler(clickEvent -> new CustomFieldQuickAdd(section, null, uuid, null, () -> {
            loadData();
            fireInTheHole();
        }));

        btnConfigColumns = new WfmButton2("", "btn btn--default btn--icon", "ficon--equalizer");
        btnConfigColumns.getElement().setId("product_table_setting_column_config_button");
        btnConfigColumns.addClickHandler(ch -> columnSettigns.show(uuid));

        Div pnlBox = new Div("panel-box panel-box--right");
        Div pnlBoxItem = new Div("panel-box__item");
        pnlBoxItem.add(btnAddNew);

        Div pnlBoxItem2 = new Div("panel-box__item");
        pnlBoxItem2.add(btnConfigColumns);
        new KpiToolTip(btnConfigColumns, wfmStrings.columnsConfig());
        pnlBoxItem2.add(btnConfigColumns);

        pnlBox.add(pnlBoxItem);
        pnlBox.add(pnlBoxItem2);
        pnlContainer.add(pnlBox);
        pnlContainer.add(dataGrid);

        initTableColumns();
        if (CustomFieldSection.CustomFormItemTable.equals(section)) {
            initFooterTable();
        }

        loadData();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DELETE_ABSTRACTADDCUSTOMFIELDSVIEW, ItemTableCustomFieldsView.this, (sender, args) -> reLoad());

        return null;
    }

    private void initFooterTable() {
        footerTable = new Table();
        footerTable.setWidth("100%");

        TableRow tr = new TableRow();
        footerTable.add(tr);

        //fieldName
        TD td = new TD();
        td.setWidth("20%");
        tr.add(td);

        //aliasName
        td = new TD();
        td.setWidth("20%");
        tr.add(td);

        //uiType
        td = new TD();
        td.setWidth("20%");
        tr.add(td);

        //Width
        tdTotal = new TD();
        tdTotal.setWidth("10%");
        tr.add(tdTotal);

        spanTotal = new Span();
        spanTotal.addStyleName("total_column_width");
        tdTotal.add(spanTotal);

        //dataType
        td = new TD();
        td.setWidth("10%");
        tr.add(td);

        //required
        td = new TD();
        td.setWidth("10%");
        tr.add(td);

        //action
        td = new TD();
        td.setWidth("10%");
        tr.add(td);

        pnlContainer.add(footerTable);
    }

    private void fireInTheHole() {
        if (this.uuid != null && uuid.trim().length() > 0) {
            consumer.accept(uuid, section);
        } else {
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LOAD_ITEM_TABLE_COLUMN_CONFIGS, section, ItemTableCustomFieldsView.this);
        }
    }

    private void initTableColumns() {
        Column<CompanyCustomFieldItem, String> fieldName = new Column<CompanyCustomFieldItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final CompanyCustomFieldItem object) {
                return object.getFieldName();
            }
        };
        fieldName.setFieldUpdater((i, object, s) -> new CustomFieldQuickAdd(section, null, uuid, object.getObjectId(), () -> {
            loadData();
            fireInTheHole();
        }));

        dataGrid.addColumn(fieldName, wfmStrings.fieldName());
        dataGrid.setColumnWidth(fieldName, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<CompanyCustomFieldItem, String> aliasName = new Column<CompanyCustomFieldItem, String>(new TextCell()) {
            @Override
            public String getValue(CompanyCustomFieldItem object) {
                return object.getAliasName();
            }
        };
        dataGrid.addColumn(aliasName, wfmStrings.aliasName());
        dataGrid.setColumnWidth(aliasName, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<CompanyCustomFieldItem, String> uiType = new Column<CompanyCustomFieldItem, String>(new TextCell()) {
            @Override
            public String getValue(CompanyCustomFieldItem object) {
                return object.getUiType();
            }
        };
        dataGrid.addColumn(uiType, settingsStrings.uiType());
        dataGrid.setColumnWidth(uiType, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        if (CustomFieldSection.CustomFormItemTable.equals(section)) {
            TextInputCell textInputCell = new TextInputCell();
            textInputCell.setWidth("50px");
            Column<CompanyCustomFieldItem, String> columnWidth = new Column<CompanyCustomFieldItem, String>(textInputCell) {
                @Override
                public String getValue(CompanyCustomFieldItem item) {
                    return item.getColumnWidth() == null ? "" : ("" + item.getColumnWidth());
                }
            };
            dataGrid.addColumn(columnWidth, "Witdh(%)");
            dataGrid.setColumnWidth(columnWidth, 10, Style.Unit.PCT);

            columnWidth.setFieldUpdater((i, companyCustomFieldItem, value) -> {
                Integer width = Optional.ofNullable(companyCustomFieldItem.getColumnWidth()).orElse(0);
                try {
                    if (value != null && !"".equals(value)) {
                        width = Integer.valueOf(value);
                    }
                    companyCustomFieldItem.setColumnWidth(width);
                    autoSavePerObject(companyCustomFieldItem);
                } catch (NumberFormatException ex) {
                    companyCustomFieldItem.setColumnWidth(width);
                    autoSavePerObject(companyCustomFieldItem);
                }
            });
        }

        Column<CompanyCustomFieldItem, String> dataType = new Column<CompanyCustomFieldItem, String>(new TextCell()) {
            @Override
            public String getValue(CompanyCustomFieldItem object) {
                return object.getDataType();
            }
        };
        dataGrid.addColumn(dataType, wfmStrings.dataType());
        dataGrid.setColumnWidth(dataType, 10, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<CompanyCustomFieldItem, String> required = new Column<CompanyCustomFieldItem, String>(new TextCell()) {
            @Override
            public String getValue(CompanyCustomFieldItem object) {
                return object.isRequired() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        dataGrid.addColumn(required, wfmStrings.required());
        dataGrid.setColumnWidth(required, 10, com.google.gwt.dom.client.Style.Unit.PCT);

        Column<CompanyCustomFieldItem, String> action = new Column<CompanyCustomFieldItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final CompanyCustomFieldItem object) {
                return wfmStrings.delete();
            }
        };
        action.setFieldUpdater((index, object, value) -> {
            final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            messageBox.setTitle(wfmStrings.confirmation());
            messageBox.setMessage(settingsStrings.areYouSureWantRemoveCustomField());
            messageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    ProfileService.App.get().deleteCustomField(object.getObjectId(), null, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            throwable.printStackTrace();
                        }

                        @Override
                        public void success(Void aVoid) {
                            LoadingPanel.loading(false);
                            if (CustomFieldSection.CustomFormItemTable.equals(section)) {
                                calculateColumnWithTotal();
                            }
                            Scheduler.get().scheduleFixedDelay(() -> {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DELETE_ABSTRACTADDCUSTOMFIELDSVIEW, null, null);
                                return false;
                            }, 100);

                        }
                    });
                }
            });
            messageBox.open();
        });
        dataGrid.addColumn(action, wfmStrings.action());
        dataGrid.setColumnWidth(action, 10, Style.Unit.PCT);
    }


    private void autoSavePerObject(CompanyCustomFieldItem item) {
        LoadingPanel.loading(true);
        ProfileService.App.get().saveCustomFormCustomFieldSettings(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                if (CustomFieldSection.CustomFormItemTable.equals(section)) {
                    calculateColumnWithTotal();
                }
            }
        });
    }

    private void calculateColumnWithTotal() {
        List<CompanyCustomFieldItem> listItems = dataProvider.getList();
        totalColumnWidth = listItems.stream().filter(item -> item.getColumnWidth() != null).mapToInt(CompanyCustomFieldItem::getColumnWidth).sum();

        spanTotal.setText(totalColumnWidth + "%");
        if (totalColumnWidth == 0) {
            spanTotal.setText("");
        } else if (totalColumnWidth == 100) {
            spanTotal.getElement().setAttribute("style", "color:green;padding-left:15px;");
        } else if (totalColumnWidth > 100) {
            spanTotal.getElement().setAttribute("style", "color:red;padding-left:15px;");
            Info.warn("Column total width can not be more than 100%");
        } else if (totalColumnWidth < 100) {
            spanTotal.getElement().setAttribute("style", "color:red;padding-left:15px;");
        }
    }

    public void reLoad() {
        loadData();
        fireInTheHole();
    }

    private void loadData() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setEntityName("'" + section.name() + "'");
        fp.setLimit(dataGrid.getPageSize());
        fp.setCategory(this.uuid);
        ProfileService.App.get().getCustomFields(fp, new AbstractAsyncCallback<ListResult<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ListResult<CompanyCustomFieldItem> customfields) {
                LoadingPanel.loading(false);
                dataProvider.getList().clear();
                dataProvider.setList(customfields.getList());

                if (customfields.getList() != null && !customfields.getList().isEmpty()) {
                    dataGrid.setHeight((customfields.getList().size() + 1) * 40 + 50 + "px");
                } else {
                    dataGrid.setHeight("200px");
                }
                dataProvider.refresh();
                if (CustomFieldSection.CustomFormItemTable.equals(section)) {
                    calculateColumnWithTotal();
                }
            }
        });
    }

    public void setColumnSettigns(ColumnSettigns columnSettigns) {
        this.columnSettigns = columnSettigns;
    }

    public void refresh() {

        if (dataProvider.getList() != null && !dataProvider.getList().isEmpty()) {
            dataGrid.setHeight((dataProvider.getList().size() + 1) * 40 + 50 + "px");
        } else {
            dataGrid.setHeight("200px");
        }
        dataProvider.refresh();
    }

    public CustomFieldSection getSection() {
        return section;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setConsumer(BiConsumer<String, CustomFieldSection> consumer) {
        this.consumer = consumer;
    }
}
