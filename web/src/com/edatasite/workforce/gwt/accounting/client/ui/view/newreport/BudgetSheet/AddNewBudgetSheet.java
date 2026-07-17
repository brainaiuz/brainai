package com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandlerAdapter;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.ColumnManagementBudgetManager;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.CollapsiblePanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Heading;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AddNewBudgetSheet extends KpiSideNavBox {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private final FlowPanel panel;
    private TextBox name;
    private DataListBox type;
    private DataListBox groupBy;
    private TextBox scale;
    private KpiSwitcher defaultBudget;
    private final Integer objectID;
    private final SelectItem budgetManager;
    private BudgetManagerItem budgetsheetItem = new BudgetManagerItem();
    private AbsolutePanel boundaryPanel;
    private VerticalPanel showVerticalPanel;
    private PickupDragController showColumnDragController;
    private final MaterialPanel container;
    private LinkedList<BudgetColumn> columns;
    private List<SelectItem> customForms;

    public AddNewBudgetSheet(SelectItem budgetManager) {
        super();
        panel = new FlowPanel();
        container = new MaterialPanel("drag-tiles");
        this.budgetManager = budgetManager;
        this.objectID = budgetManager != null ? budgetManager.getId() : null;
        Heading header = new Heading(HeadingSize.H1);
        header.setText(budgetManager != null ? budgetManager.getName() : "");
        addHeader(header);
        CommonService.App.get().getCustomForms(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                init();
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                super.onSuccess(result);
                customForms = result;
                init();
            }
        });
    }


    public AddNewBudgetSheet() {
        this(null);
    }


    private void init() {
        initialize();
        if (objectID != null) {
            AccountingService.App.get().getBudgetManagerData(objectID, new AbstractAsyncCallback<BudgetManagerItem>() {
                @Override
                public void failure(Throwable throwable) {
                    super.failure(throwable);
                }

                @Override
                public void success(BudgetManagerItem result) {
                    super.success(result);
                    budgetsheetItem = result;
                    columns = result.getColumns();
                    setFormData();
                    drawColumns();
                }
            });
        }
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BUDGET_SHEET_RELOAD_COLUMNS, AddNewBudgetSheet.this, (sender, args) -> {
            BudgetColumn budgetColumn = (BudgetColumn) args;

            boolean ifExist = false;
            if (columns != null) {
                for (int i = 0; i < columns.size(); i++) {
                    if (budgetColumn.getCode().equals(columns.get(i).getCode())) {
                        columns.set(i, budgetColumn);
                        ifExist = true;
                        break;
                    }
                }
            } else {
                columns = new LinkedList<>();
            }
            if (!ifExist) {
                columns.add(budgetColumn);
            }
            container.clear();
            drawColumns();
        });
    }

    private void initialize() {
        setStyleName(getElement(), "quick-add", true);

        Heading header = new Heading(HeadingSize.H1);
        header.setText(Property.get("budgetsheetView", wfmStrings.budgetManager()));

        name = new TextBox();

        type = new DataListBox();
        type.setItems(getTypes());
        type.addValueChangeHandler(value -> {
            if (type.getSelectedItem() != null) {
                getCustomfieldsByType();
            }
        });

        groupBy = new DataListBox();

        scale = new TextBox();
        Validation.addNumericKeyboardListener(scale);
        scale.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        scale.setText("0");

        defaultBudget = new KpiSwitcher();

        GRow row = new GRow();
        row.add(new GColumn(GColumnEnum.COL_12, new FormGroup(wfmStrings.name(), name)));
        GRow rowType = new GRow();
        rowType.add(new GColumn(GColumnEnum.COL_12, new FormGroup(wfmStrings.type(), type)));

        GRow rowgroupBy = new GRow();
        rowgroupBy.add(new GColumn(GColumnEnum.COL_12, new FormGroup(wfmStrings.groupBy(), groupBy)));
        GRow decimalRow = new GRow();
        FormGroup scaleGroup = new FormGroup(wfmStrings.decimalPlaces(), scale);
        new KpiToolTip(scaleGroup, wfmStrings.numberOfDigitsToTheRight());
        decimalRow.add(new GColumn(GColumnEnum.COL_8, scaleGroup));
        decimalRow.add(new GColumn(GColumnEnum.COL_4, new FormGroup(wfmStrings.default2(), defaultBudget)));
        panel.add(row);
        panel.add(rowType);
        panel.add(rowgroupBy);
        panel.add(decimalRow);

        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        saveButton.addClickHandler(click -> save());


        addBody(panel);
        addFooter(saveButton);

        if (objectID != null) {
            WfmButton2 deleteButton = new WfmButton2(wfmStrings.delete(), WfmButton2.BTN_WHITE_OUTLINE);

            deleteButton.addClickHandler(click -> removeBudgetManager());

            addFooter(deleteButton);
        }
        show();
    }


    private void setFormData() {
        if (budgetsheetItem != null) {
            if (!Utils.isNullOrEmpty(budgetsheetItem.getName())) {
                name.setText(budgetsheetItem.getName());
            }
            if (!Utils.isNullOrEmpty(budgetsheetItem.getType())) {
                type.setSelectedByDescription(budgetsheetItem.getType());
                type.setEnabled(false);

                groupBy.setItems(getGroupByTypes(budgetsheetItem.getGroupsByType()));
                if (!Utils.isNullOrEmpty(budgetsheetItem.getGroupBy())) {
                    groupBy.setSelectedByDescription(budgetsheetItem.getGroupBy());
                    groupBy.setEnabled(false);
                }
            }
            if (budgetsheetItem.getScale() != null) {
                scale.setText(budgetsheetItem.getScale().toString());
            }
            defaultBudget.setValue(budgetsheetItem.isDefaultBudget());
        }
    }

    private void removeBudgetManager() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.confirmation());
        messageBox.setMessage(wfmStrings.messAreDelete() + "?");
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                AccountingService.App.get().deleteBudgetManager(objectID, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        throwable.printStackTrace();
                    }

                    @Override
                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        remove();
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.budget()), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CHANGE_BUDGET_MANAGERS, null, AddNewBudgetSheet.this);
                    }
                });
            }
        });
        messageBox.open();
    }


    private SelectItem[] getTypes() {
        List<SelectItem> types = new ArrayList<>();
        types.add(new SelectItem(1, wfmStrings.product(), Constants.PRODUCTS));
        types.add(new SelectItem(2, wfmStrings.employee(), Constants.EMPLOYEES));
        types.add(new SelectItem(3, wfmStrings.customer(), Constants.CUSTOMER));
        types.add(new SelectItem(4, wfmStrings.chartOfAccounts(), Constants.CHART_OF_ACCOUNT));
        types.add(new SelectItem(5, wfmStrings.opportunity(), Constants.OPPORTUNITY));
        types.add(new SelectItem(6, wfmStrings.purchaseorder(), Constants.PURCHASE_ORDER));
        if (customForms != null && !customForms.isEmpty()) {
            types.addAll(customForms);
        }
        return types.toArray(new SelectItem[]{});
    }

    private void getCustomfieldsByType() {
        AccountingService.App.get().getCustomfieldsByType(type.getSelectedItem().getDescription(), new AbstractAsyncCallback<List<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                groupBy.setItems(getGroupByTypes(null));
            }

            @Override
            public void success(List<SelectItem> result) {
                super.success(result);
                groupBy.setItems(getGroupByTypes(result));
            }
        });
    }

    private SelectItem[] getGroupByTypes(List<SelectItem> cfs) {
        List<SelectItem> types = new ArrayList<>();
        if (type.getSelectedItem() != null && type.getSelectedItem().getDescription().equals(Constants.CUSTOMER)) {
            types.add(new SelectItem(1, wfmStrings.country(), CustomFormConstants.COUNTRY));
            types.add(new SelectItem(2, Property.get(Constants.CLIENT_LIST, wfmStrings.clientType(), wfmStrings.customer()), CustomFormConstants.CLIENT_TYPE));
            types.add(new SelectItem(3, wfmStrings.owners(), CustomFormConstants.CRM_ACCOUNT_OWNER));
            types.addAll(cfs);
        } else if (type.getSelectedItem() != null && type.getSelectedItem().getDescription().equals(Constants.PRODUCTS)) {
            types.add(new SelectItem(1, wfmStrings.category(), CustomFormConstants.CATEGORY));
            types.add(new SelectItem(2, wfmStrings.brand(), CustomFormConstants.BRAND));
            types.addAll(cfs);
        } else if (type.getSelectedItem() != null && type.getSelectedItem().getDescription().equals(Constants.OPPORTUNITY)) {
            types.add(new SelectItem(1, wfmStrings.assignee(), CustomFormConstants.ASSIGNEE));
            types.add(new SelectItem(2, wfmStrings.backupAssignee(), CustomFormConstants.BACKUP_ASSIGNEE));
            types.add(new SelectItem(3, wfmStrings.stage(), CustomFormConstants.CRM_OPPORTUNITY_STAGE));
            types.add(new SelectItem(4, wfmStrings.product(), Constants.PRODUCTS));
            types.addAll(cfs);
        } else if (type.getSelectedItem() != null && type.getSelectedItem().getDescription().equals(Constants.PURCHASE_ORDER)) {
            types.add(new SelectItem(1, wfmStrings.product(), Constants.PRODUCTS));
            types.addAll(cfs);
        } else if (type.getSelectedItem() != null && type.getSelectedItem().getDescription().contains("_FORM")) {
            types.addAll(cfs);
        }
        return types.toArray(new SelectItem[]{});
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (!Validation.validateDataListBoxRequired(type)) {
            errors++;
        }
        if (type.getSelectedItem() != null && type.getSelectedItem().getDescription().equals(Constants.CUSTOMER)) {
            if (!Validation.validateDataListBoxRequired(groupBy)) {
                errors++;
            }
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    public void save() {
        if (!validate()) {
            return;
        }
        LoadingPanel.loading(true, panel);
        String oldBudgetName = budgetsheetItem.getName();
        budgetsheetItem.setName(name.getText());
        budgetsheetItem.setType(type.getSelectedItem().getDescription());
        if (groupBy.getSelectedItem() != null) {
            budgetsheetItem.setGroupBy(groupBy.getSelectedItem().getDescription());
        }
        if (!Utils.isNullOrEmpty(scale.getText())) {
            budgetsheetItem.setScale(Integer.valueOf(scale.getText()));
        }
        budgetsheetItem.setDefaultBudget(defaultBudget.getValue());


        LinkedList<BudgetColumn> list = new LinkedList<>();
        if (showVerticalPanel != null && showVerticalPanel.getWidgetCount() > 0) {
            for (int i = 0; i < showVerticalPanel.getWidgetCount(); i++) {
                BudgetColumn item = (BudgetColumn) showVerticalPanel.getWidget(i).getLayoutData();
                list.add(item);
            }
        }
        budgetsheetItem.setColumns(list);

        AccountingService.App.get().saveBudgetManager(budgetsheetItem, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void success(Integer result) {
                super.success(result);
                LoadingPanel.loading(false, panel);
                remove();
                if (objectID != null && name.getText().equals(oldBudgetName)) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BUDGET_SHEET_UPDATE, null, AddNewBudgetSheet.this);
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CHANGE_BUDGET_MANAGERS, null, AddNewBudgetSheet.this);
                }
            }
        });
    }

    private void drawColumns() {
        // dragable container
        boundaryPanel = new AbsolutePanel();

        // initialize vertical panel to hold our columns
        showVerticalPanel = new VerticalPanel();

        boundaryPanel.add(showVerticalPanel);

        // initialize our column drag controller
        showColumnDragController = new PickupDragController(boundaryPanel, false);
        showColumnDragController.setBehaviorMultipleSelection(false);

        // initialize our column drop controller
        VerticalPanelDropController columnDropController = new VerticalPanelDropController(showVerticalPanel);
        showColumnDragController.registerDropController(columnDropController);
        showColumnDragController.addDragHandler(new DragHandlerAdapter() {
            @Override
            public void onDragEnd(DragEndEvent event) {
            }
        });

        CollapsiblePanel collapsiblePanel = new CollapsiblePanel();
        collapsiblePanel.addWidget(boundaryPanel);
        collapsiblePanel.setActive(true);

        createNewColumnPanel(showVerticalPanel);
        container.add(collapsiblePanel);
        panel.add(container);

        LinkedList<GColumn> columns = new LinkedList<>();
        GColumn customizeText = new GColumn(GColumnEnum.COL_8, new HTML(wfmStrings.columns()));
        columns.add(customizeText);

        ActionButton button = new ActionButton("", "btn btn--new btn--circle");
//        button.getElement().setAttribute("style", "height:2rem");
        button.add(new SvgIcon(SvgEnum.plus));
        button.addClickHandler(click -> {
            new ColumnManagementBudgetManager(budgetManager);
        });
        columns.add(new GColumn(GColumnEnum.COL_2, button));
        collapsiblePanel.setCustomizeHeaderBudget(columns);

    }

    private void createNewColumnPanel(VerticalPanel showVerticalPanel) {
        if (columns != null && columns.size() > 0) {

            for (BudgetColumn budgetColumn : columns) {
                MaterialLink options = new MaterialLink();
                options.setActivates("field-settings");
                options.addStyleName("btn—icon");
                SvgIcon svgIcon = new SvgIcon("moreBold");
                options.add(svgIcon);

                MaterialDropDown menuContainer = new MaterialDropDown("field-settings");
                menuContainer.setBelowOrigin(true);
                options.add(menuContainer);

                MaterialLink properties = new MaterialLink(wfmStrings.properties());
                properties.addClickHandler(click -> {
                    new ColumnManagementBudgetManager(budgetColumn.getCode(), budgetManager);
                });
                menuContainer.add(properties);

                MaterialLink delete = new MaterialLink(wfmStrings.delete());
                delete.addClickHandler(click -> {
                    deleteColumn(budgetColumn.getCode());
                });
                menuContainer.add(delete);

                MaterialPanel pnlColumn = new MaterialPanel("drag-tile state-on");
                MaterialPanel pnlGrip = new MaterialPanel("drag-tile__grip");

                HTML columnTitle = new HTML(budgetColumn.getName());
                columnTitle.setStyleName("drag-tile__text");

                MaterialPanel pnlAction = new MaterialPanel("drag-tile__actions");
                pnlAction.add(options);


                pnlColumn.add(pnlGrip);
                pnlColumn.add(columnTitle);
                pnlColumn.add(pnlAction);
                pnlColumn.setLayoutData(budgetColumn);

                showColumnDragController.makeDraggable(pnlColumn, pnlGrip);
                showVerticalPanel.add(pnlColumn);
            }
        }
    }

    private void deleteColumn(String columnCode) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.confirmation());
        messageBox.setMessage(wfmStrings.messAreDelete() + "?");
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                AccountingService.App.get().deleteBudgetManagerColumn(objectID, columnCode, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        throwable.printStackTrace();
                    }

                    @Override
                    public void success(Void result) {
                        LoadingPanel.loading(false);
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.budget()), Info.Type.INFO);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CHANGE_BUDGET_MANAGERS, null, AddNewBudgetSheet.this);
                        LinkedList<BudgetColumn> budgetColumns = new LinkedList<>();
                        for (BudgetColumn budgetCol : columns) {
                            if (!columnCode.equals(budgetCol.getCode())) {
                                budgetColumns.add(budgetCol);
                            }
                        }
                        columns = budgetColumns;
                        container.clear();
                        drawColumns();
                    }
                });
            }
        });
        messageBox.open();
    }
}