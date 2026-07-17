package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.ColumnConfig;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.EditableTable;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.lookup.LookUp2;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ReferenceLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomCellTextBox;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollService;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;

import static com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC.WAGE_RATE_FORM;

public class AddWageRateView extends CustomForm implements Constants, FittedContent, Colapse {
    private static final SettingStrings settingStrings = SettingStrings.App.get();
    private static final PayrollStrings payrollStrings = PayrollStrings.App.get();
    private DatePicker effectiveDate;
    private LookUp2 zone;
    private EditableTable items;
    private final Integer id;

    public AddWageRateView(Integer id) {
        super("addWageRate", settingStrings.addWageRate());
        this.id = id;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        registerFields();
        return null;
    }

    private void registerFields() {
        effectiveDate = new DatePicker();

        zone = new LookUp2();
        PayrollService.App.get().getPayrollZones(new ListingFilterParameter(), new AsyncCallback<ListResult<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(ListResult<SelectItem> result) {
                if (result != null && result.getList() != null) {
                    zone.setItems(result.getList().toArray(new SelectItem[0]));
                }
            }
        });

        items = new EditableTable(getColumns(), true, true);
        items.setDraggable(true);
        items.ensureDebugId("Rotation_item_table");
        items.setWidth("100%");
        items.setListener(new EditableTableListener() {
            @Override
            public void addRow() {
                items.addRow(getWidgets(null));
            }

            @Override
            public void removeRow() {
                Window.alert(((CustomCellTextBox) items.getColumnById(items.getGrid().getCurrentRow(), ItemTableConstants.ID)).getText());
            }
        });


        for (int i = 0; i < 3; i++) {
            items.addRow(getWidgets(null));
        }

        addField(EFFECTIVE_DATE, effectiveDate, getTitle(wfmStrings.effectiveDate(), true));
        addField(ZONE, zone, getTitle(payrollStrings.payrollZones(), true));
        addField(WAGE_RATE, items, null);
        show();
    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        LoadingPanel.loading(true);
        ProfileService.App.get().saveWageRate(getValues(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Void unused) {
                enableButton(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.messSuccessfullySaved());
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WAGE_RATE_ADD, null, AddWageRateView.this);
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();

        errors += markAsError(EFFECTIVE_DATE, effectiveDate, !Validation.validateDate(effectiveDate));
        errors += markAsError(ZONE, zone, !Validation.validateLookUpRequired(zone));

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private SelectItem getValues() {
        SelectItem item = new SelectItem();
        item.setId(id);
        item.setDate(effectiveDate.getDateAsNonConvertable());
        item.setEntityId(zone.getSelectedItemID());
        item.setRelatedItems(getTableItems());

        return item;
    }

    private ColumnConfig[] getColumns() {
        ArrayList<ColumnConfig> columns = new ArrayList<>();

        ColumnConfig position = new ColumnConfig(LookUpCell.class, ItemTableConstants.POSITION, wfmStrings.position(), 250, true, Constants.LEFT_ALIGN_CELL);
        position.setPixel(true);
        position.setForceWidthInPercent(false);
        columns.add(position);

        ColumnConfig rate = new ColumnConfig(CustomCell.class, ItemTableConstants.RATE, wfmStrings.rate(), 250, true, Constants.RIGHT_ALIGN_CELL);
        rate.setPixel(true);
        rate.setForceWidthInPercent(false);
        columns.add(rate);
        return columns.toArray(new ColumnConfig[]{});
    }

    private Widget[] getWidgets(SelectItem item) {
        ArrayList<Widget> widgets = new ArrayList<>();

        ReferenceLookUp position = new ReferenceLookUp(POSITION_TITLES);
        if (item != null && item.getEntityId() != null) {
            position.setSelected(new SelectItem(item.getEntityId(), item.getCategory()));
        }
        widgets.add(position);

        CustomCellTextBox rate = new CustomCellTextBox(true);
        Validation.addNumericKeyboardListener(rate, 2);
        if (item != null && item.getQtyAmount() != null) {
            rate.setText(item.getQtyAmount().toString());
        }
        widgets.add(rate);
        return widgets.toArray(new Widget[]{});
    }

    public SelectItem[] getTableItems() {
        ArrayList<SelectItem> tableItems = new ArrayList<>();
        for (int i = 0; i < items.getGrid().getRowCount(); i++) {
            SelectItem result = new SelectItem();
            ReferenceLookUp position = (ReferenceLookUp) items.getColumnById(i, ItemTableConstants.POSITION);
            if (position.getSelectedItem() == null) {
                continue;
            }
            if (position != null) {
                result.setEntityId(position.getSelectedItemID());
            }

            CustomCellTextBox rate = (CustomCellTextBox) items.getColumnById(i, ItemTableConstants.RATE);
            if (rate != null) {
                result.setQtyAmount(Utils.parseToBigDecimal(rate.getDisplayValue()));
            }
            tableItems.add(result);
        }
        return tableItems.toArray(new SelectItem[]{});
    }

    @Override
    public String getIconStyle() {
        return "icon-edit";
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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

    @Override
    protected void addButtons() {
        addButton(new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> save()));
    }

    @Override
    protected void getDataToFillFields() {
        if (id != null) {
            ProfileService.App.get().getWageRate(id, new AsyncCallback<SelectItem>() {
                @Override
                public void onFailure(Throwable caught) {

                }

                @Override
                public void onSuccess(SelectItem result) {
                    effectiveDate.setDate(result.getDate().getNonConvertedDate());
                    zone.setSelected(new SelectItem(result.getEntityId(), result.getCategory()));

                    if (result.getRelatedItems() != null) {
                        items.removeAllRows();
                        Arrays.stream(result.getRelatedItems()).forEach(i -> items.addRow(getWidgets(i)));
                        for (int i = result.getRelatedItems().length; i < 3; i++) {
                            items.addRow(getWidgets(null));
                        }
                    }
                }
            });
        }
    }

    @Override
    protected String getFormID() {
        return WAGE_RATE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }
}
