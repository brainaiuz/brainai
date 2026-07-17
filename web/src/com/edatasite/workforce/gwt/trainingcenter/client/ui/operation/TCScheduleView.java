package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.TCHtmlTemplates;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCScheduleData;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCScheduleItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/6/12
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class TCScheduleView extends CustomForm2 implements FittedContent {

    public static TCStrings tcStrings = TCStrings.App.get();

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private CrmAccountLookUp customerLookUp;
    private FormGroup startDateWidget;
    private FormGroup endDateWidget;
    private FormGroup customerWidget;

    private KpiDataGrid<TCScheduleItem> itemsGrid;
    private ListDataProvider<TCScheduleItem> itemsDataProvider;

    private TCScheduleData scheduleData;

    public static final ProvidesKey<TCScheduleItem> KEY_PROVIDER_ITEM = item -> item == null ? null : item.getObjectID();

    public TCScheduleView() {
        super("scheduleinvoice");
        setDescription(property.getPlural(tcStrings.consolidatedInvoice()));
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        initTopPanel();
        initItemsGrid();

        return null;
    }

    @Override
    protected void addButtons() {
        WfmButton2 updateButton = new WfmButton2("Update", WfmButton2.BTN_PRIMARY, event -> loadData());

        WfmButton2 saveButton = new WfmButton2("Schedule", WfmButton2.BTN_PRIMARY, event -> save());


        addButton(updateButton);
        addButton(saveButton);
    }

    @Override
    protected void getDataToFillFields() {

    }

    @Override
    protected String getFormID() {
        return "SCHEDULE_INVOICE_FORM";
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected void registerFields() {

    }

    @Override
    protected void initPredefinedValues() {

    }

    private void initTopPanel() {

        startDatePicker = new DatePicker(true);
        startDateWidget = new FormGroup("Period", startDatePicker);
        startDateWidget.setMarginRight(10);

        endDatePicker = new DatePicker(true);
        endDateWidget = new FormGroup("End", endDatePicker);
        endDateWidget.setMarginRight(10);

        customerLookUp = new CrmAccountLookUp(CrmAccountItem.CUSTOMER, false);
        customerWidget = new FormGroup("Customer", customerLookUp);


        HorizontalPanel dataPanel = new HorizontalPanel();
        dataPanel.addStyleName("form-group__content");
        dataPanel.add(startDateWidget);
        dataPanel.add(endDateWidget);
        dataPanel.add(customerWidget);


        MaterialPanel mainPanel = new MaterialPanel();
        mainPanel.addStyleName("content-box content-box--white");
        mainPanel.add(new HTML(TCHtmlTemplates.getInstance().invoiceGenerationInfo()));
        mainPanel.add(dataPanel);
//        mainPanel.add(createFooter());
        add(mainPanel);
        addWidgetsToForm();
        show();
//        add(createFooter());
    }

    private void addWidgetsToForm() {
        addTitleField(INFORMATION, wfmStrings.information());
        //1.1
        addField(SCHEDULE_INVOICE.PERIOD, startDatePicker, getTitle(Property.get("PERIOD", wfmStrings.period()), true));
        //2.1
        addField(SCHEDULE_INVOICE.END,endDatePicker, Property.get("END", wfmStrings.end()), true);
        addField(SCHEDULE_INVOICE.CUSTOMER,customerLookUp, getTitle(Property.get("CUSTOMER", wfmStrings.customer()), true));


    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return TCScheduleView.this.getFooterRightSideWidgets();
            }
        });
    }


    private List<Widget> getFooterRightSideWidgets() {
        WfmButton2 updateButton = new WfmButton2("Update", WfmButton2.BTN_PRIMARY, event -> loadData());

        WfmButton2 saveButton = new WfmButton2("Schedule", WfmButton2.BTN_PRIMARY, event -> save());

        List<Widget> list = new ArrayList<>();
        Div div = new Div();
        div.add(updateButton);
        list.add(saveButton);
        list.add(div);
        return list;
    }

    private void initItemsGrid() {
        itemsGrid = new KpiDataGrid<>(KEY_PROVIDER_ITEM);
        itemsGrid.setVisible(false);
        itemsGrid.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.noDataAvailable(), null, null));
        itemsGrid.setSize("100%", "350px");

        // Invoice Number
        itemsGrid.addColumn(new Column<TCScheduleItem, String>(new TextCell()) {
            @Override
            public String getValue(TCScheduleItem item) {
                return String.valueOf(item.getNumber());
            }
        }, wfmStrings.number());
        itemsGrid.setColumnWidth(itemsGrid.getColumn(0), 50, Style.Unit.PCT);

        // Invoice Total
        itemsGrid.addColumn(new Column<TCScheduleItem, String>(new TextCell()) {
            @Override
            public String getValue(TCScheduleItem item) {
                return Utils.formatDouble(item.getAmount().doubleValue());
            }
        }, wfmStrings.total());
        itemsGrid.setColumnWidth(itemsGrid.getColumn(1), 50, Style.Unit.PCT);

        itemsDataProvider = new ListDataProvider<>();
        itemsDataProvider.addDataDisplay(itemsGrid);

        VerticalPanel itemsPanel = new VerticalPanel();
        itemsPanel.add(itemsGrid);
        itemsPanel.setSpacing(10);

        add(itemsPanel);
    }

    private void loadData() {
        if (!validate()) {
            return;
        }

        LoadingPanel.loading(true);

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setCrmAccountId(customerLookUp.getSelectedItemID());

        TCService.App.get().getTCScheduleData(Utils.getStartDateNC(startDatePicker.getDate()), Utils.getEndDateNC(endDatePicker.getDate()), filterParameter, new AsyncCallback<TCScheduleData>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(TCScheduleData result) {
                appyFilterParametersToObject(result);
                applyItemsToTable(result);

                LoadingPanel.loading(false);
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateDate(startDatePicker)) {
            errors++;
        }
        if (!Validation.validateDate(endDatePicker)) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(customerLookUp)) {
            errors++;
        }
        return errors <= 0;
    }

    private void appyFilterParametersToObject(TCScheduleData result) {
        scheduleData = result;
        scheduleData.setStartDate(startDatePicker.getDate() != null ? new DateNonConvertable(startDatePicker.getDate()) : null);
        scheduleData.setEndDate(endDatePicker.getDate() != null ? new DateNonConvertable(endDatePicker.getDate()) : null);
        scheduleData.setCustomerID(customerLookUp.getSelectedItemID());
    }

    private void applyItemsToTable(TCScheduleData result) {
        itemsDataProvider.getList().clear();
        TCScheduleItem[] items = result.getItems();
        for (TCScheduleItem item : items) {
            itemsDataProvider.getList().add(item);
        }
        itemsDataProvider.refresh();

        itemsGrid.setVisible(items != null && items.length > 0);
    }

    private void save() {
        if (scheduleData == null || scheduleData.getItems() == null || scheduleData.getItems().length == 0) {
            WfmWindow.alert("There are no items to schedule. Please update first to load data.");
            return;
        }

        TCService.App.get().saveTCScheduleData(scheduleData, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                WfmWindow.error("Error occured while scheduling consolidated invoice");
            }

            @Override
            public void onSuccess(Integer result) {
                WfmWindow.info("Consolidated invoice has been scheduled on a background process. Once completed, you should receive the consolidated invoice on your email.");
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
    @Override
    public String getPropertyCode() {
        return "scheduleinvoice";
    }
}
