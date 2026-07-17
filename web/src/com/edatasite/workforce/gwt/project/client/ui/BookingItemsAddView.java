package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.BookingReservationItem;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.rpc.BookingItemsItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/18/12
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemsAddView extends CustomForm2 implements Constants, Colapse {


    private static final ProjectServiceAsync projectService = ProjectService.App.get();

    private Integer objectID;
    private DataListBox location;
    private TextBox name;
    private DataListBox category;
    private TextArea description;
    private WfmButton2 saveButton;
    private TextBox itemNubmer;
    private Numbering productNumberWidget;
    private NumberData numberData;
    private HTML bookingItemsStatus;
    private HTMLPanel htmlPanel;
    private HashMap<String, Widget> widgetsMap;
    private BookingItemsItem bookingItems = new BookingItemsItem();
    private KpiDataGrid<BookingReservationItem> dataGrid;
    FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;


    public BookingItemsAddView(String[] params) {
        super("bookingitemsadd", wfmStrings.addItem());
    }

    public BookingItemsAddView(Integer objectID, String[] params) {
        super("edit", wfmStrings.editItem());
        this.objectID = objectID;
    }

    public static final ProvidesKey<BookingReservationItem> KEY_PROVIDER = item -> item != null ? item.getObjectID() : null;


    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.BookingItemsView,getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                BookingItemsAddView.super.onInitialize();
            }
        });
        return null;
    }


    @Override
    protected void registerFields() {
        location = new DataListBox();
        location.addStyleName(DEFAULT_WIDTH);

        name = new TextBox();
        name.addStyleName(DEFAULT_WIDTH);

        productNumberWidget = new Numbering();
        productNumberWidget.addStyleName(DEFAULT_WIDTH);

        category = new DataListBox();
        category.addStyleName(DEFAULT_WIDTH);

        description = new TextArea();
        description.addStyleName(DEFAULT_WIDTH);
        description.setStyleName("booking-item-description-edit");

        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
//        dataGrid.setSize("540px", "342px");
        initTableColumn();

        addTitleField(INFORMATION, wfmStrings.information());
        if (formPropertyMap != null && formPropertyMap.get(LOCATION_FIELD) != null) {
            addField(LOCATION_FIELD, location, getTitle(formPropertyMap.get(LOCATION_FIELD).isChanged() ? formPropertyMap.get(LOCATION_FIELD).getTitle() : wfmStrings.location(), formPropertyMap.get(LOCATION_FIELD).isRequired()),false,
                    formPropertyMap.get(LOCATION_FIELD).isInformation());
            location.setEnabled(!formPropertyMap.get(LOCATION_FIELD).isDisabled());
            if (formPropertyMap.get(LOCATION_FIELD).isInformation()) {
                new KpiToolTip(location, formPropertyMap.get(LOCATION_FIELD).getInformationText());
            }
        } else {
            addField(LOCATION_FIELD, location, getTitle(wfmStrings.location(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(NAME) != null) {
            addField(NAME, name, getTitle(formPropertyMap.get(NAME).isChanged() ? formPropertyMap.get(NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(NAME).isRequired()),false,
                    formPropertyMap.get(NAME).isInformation());
            name.setEnabled(!formPropertyMap.get(NAME).isDisabled());
            if (formPropertyMap.get(NAME).isInformation()) {
                new KpiToolTip(name, formPropertyMap.get(NAME).getInformationText());
            }
        } else {
            addField(NAME, name, getTitle(wfmStrings.name(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null) {
            addField(DESCRIPTION, description, getTitle(formPropertyMap.get(DESCRIPTION).isChanged() ? formPropertyMap.get(DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(DESCRIPTION).isRequired()),false,
                    formPropertyMap.get(DESCRIPTION).isInformation());
            description.setEnabled(!formPropertyMap.get(DESCRIPTION).isDisabled());
            if (formPropertyMap.get(DESCRIPTION).isInformation()) {
                new KpiToolTip(description, formPropertyMap.get(DESCRIPTION).getInformationText());
            }
        } else {
            addField(DESCRIPTION, description, getTitle(wfmStrings.name(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, productNumberWidget, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NUMBER).isInformation());
            productNumberWidget.setEnabled(!formPropertyMap.get(CustomFormConstants.NUMBER).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.NUMBER).isInformation()) {
                new KpiToolTip(productNumberWidget, formPropertyMap.get(CustomFormConstants.NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NUMBER, productNumberWidget, getTitle(wfmStrings.number(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CATEGORY) != null) {
            addField(CustomFormConstants.CATEGORY, category, getTitle(formPropertyMap.get(CustomFormConstants.CATEGORY).isChanged() ? formPropertyMap.get(CustomFormConstants.CATEGORY).getTitle() : wfmStrings.category(), formPropertyMap.get(CustomFormConstants.CATEGORY).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.CATEGORY).isInformation());
            category.setEnabled(!formPropertyMap.get(CustomFormConstants.CATEGORY).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.CATEGORY).isInformation()) {
                new KpiToolTip(category, formPropertyMap.get(CustomFormConstants.CATEGORY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CATEGORY, category, getTitle(wfmStrings.category(), false));
        }
//        addField(LOCATION_FIELD, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), true));
//        addField(NAME, name, getTitle(wfmStrings.name(), true));
//        addField(DESCRIPTION, description, getTitle(wfmStrings.description()));
//        addField(CustomFormConstants.NUMBER, productNumberWidget, getTitle(wfmStrings.number()));
//        addField(CustomFormConstants.CATEGORY, category, getTitle(wfmStrings.category(), true));

        addField(RESERVATION_HISTORY, dataGrid, wfmStrings.reservationHistory());
        getCustomFieldUtil().drawCustomFields(this, objectID, false);
        show();
    }


    @Override
    protected void getDataToFillFields() {
        drawForm(false);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BOOKING_ITEMS_RESERVATION_SAVED, BookingItemsAddView.this, (sender, args) -> drawForm(true));
    }


    private void drawForm(boolean reload) {
        projectService.getBookingItemsData(objectID, new AbstractAsyncCallback<BookingItemsItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(BookingItemsItem bookingItems) {
                LoadingPanel.loading(false);
                if (reload) {
                    dataGrid.supplyProvider(bookingItems.getBookingReservationItemList());
                    dataGrid.refresh();
                }
                if (!reload) {
                    fillFormWithData(bookingItems);
                }
            }
        });
    }

    @Override
    protected void initPredefinedValues() {

    }


    private void initTableColumn() {
        //Reserved By
        Column<BookingReservationItem, String> reservedBy = new Column<BookingReservationItem, String>(new TextCell()) {
            @Override
            public String getValue(BookingReservationItem object) {
                return object.getSelectedReservedById() != null ? object.getSelectedReservedById().getName() : wfmStrings.notAvailable();
            }
        };
        dataGrid.addColumn(reservedBy, wfmStrings.reservedBy());
        dataGrid.setColumnWidth(reservedBy, 25, com.google.gwt.dom.client.Style.Unit.PCT);

        //from
        Column<BookingReservationItem, String> fromdate = new Column<BookingReservationItem, String>(new TextCell()) {
            @Override
            public String getValue(BookingReservationItem object) {
                return object.getFromDate() != null ? DateUtils.formatInternalShort1(object.getFromDate()) : wfmStrings.notAvailable();
            }
        };
        dataGrid.addColumn(fromdate, wfmStrings.from());
        dataGrid.setColumnWidth(fromdate, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //to
        Column<BookingReservationItem, String> toDate = new Column<BookingReservationItem, String>(new TextCell()) {
            @Override
            public String getValue(BookingReservationItem object) {
                return object.getToDate() != null ? DateUtils.formatInternalShort1(object.getToDate()) : wfmStrings.notAvailable();
            }
        };
        dataGrid.addColumn(toDate, wfmStrings.to());
        dataGrid.setColumnWidth(toDate, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //Edit
        final Column<BookingReservationItem, String> edit = new Column<BookingReservationItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final BookingReservationItem object) {
                return wfmStrings.edit();
            }
        };
        edit.setFieldUpdater((index, object, value) -> SinksContainerFactory.entryPoint.onHistoryChanged("bookingitemsreservation|edit/" + object.getObjectID()));
        dataGrid.addColumn(edit, wfmStrings.edit());
        dataGrid.setColumnWidth(edit, 10, com.google.gwt.dom.client.Style.Unit.PCT);

        //View
        final Column<BookingReservationItem, String> view = new Column<BookingReservationItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final BookingReservationItem object) {
                return wfmStrings.summaryView();
            }
        };
        view.setFieldUpdater((index, object, value) -> SinksContainerFactory.entryPoint.onHistoryChanged("bookingitemsreservation|summary/" + object.getObjectID()));
        dataGrid.addColumn(view, wfmStrings.summaryView());
        dataGrid.setColumnWidth(view, 10, com.google.gwt.dom.client.Style.Unit.PCT);

        //Remove Action
        final Column<BookingReservationItem, String> romeve = new Column<BookingReservationItem, String>(new SimpleLinkCell()) {

            @Override
            public String getValue(final BookingReservationItem object) {
                return wfmStrings.delete();
            }
        };
        romeve.setFieldUpdater((index, object, value) -> {
            final Integer reservationRemoveLink = object.getObjectID();

            String message = "deleted";
            projectService.deleteReservation(reservationRemoveLink, new AsyncCallback<Void>() {

                @Override
                public void onFailure(Throwable caught) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }

                @Override
                public void onSuccess(Void result) {
                    Info.show(wfmStrings.yourTaskHasBeenReservationItem(), Info.Type.INFO);
                    dataGrid.getList().remove(index);
                    dataGrid.refresh();
                }
            });
        });
        dataGrid.addColumn(romeve, wfmStrings.delete());
        dataGrid.setColumnWidth(romeve, 15, com.google.gwt.dom.client.Style.Unit.PCT);

    }

    private void fillFormWithData(BookingItemsItem bookingItems) {
        category.setItems(bookingItems.getCategories());
        category.setSelected(bookingItems.getCategory());
        location.setItems(bookingItems.getLocations());
        location.setSelected(bookingItems.getLocationID());

        if (objectID != null) {
            name.setText(bookingItems.getItemName());
            description.setText(bookingItems.getDescription());
            bookingItemsStatus = new HTML();
            bookingItemsStatus.addStyleName("form-control");
            addField(STATUS, bookingItemsStatus, wfmStrings.status());
            bookingItemsStatus.setHTML(bookingItems.getStatus());
        }
        if (bookingItems.getObjectID() != null) {
            itemNubmer = new TextBox();
            itemNubmer.addStyleName(DEFAULT_WIDTH);
            itemNubmer.setValue(bookingItems.getItemNumber());
            if (bookingItems.getItemNumber() != null && !bookingItems.getItemNumber().equals("")) {
                productNumberWidget.setNumberData(bookingItems.getNumberData());
            }
        } else {
            if (bookingItems.getObjectID() == null) {
                generateBookingItemNumber();
            }
        }
        getCustomFieldUtil().fillCustomFieldsWithData(bookingItems.getCustomFieldItems());
        dataGrid.supplyProvider(bookingItems.getBookingReservationItemList());
        dataGrid.refresh();
    }


    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(name)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(category, new HTML(), wfmStrings.pleaseSelect())) {
            errors++;
        }
        String fromSection = GWT.getModuleName();
        if (fromSection != null && "trainingcenter".equals(fromSection)) {
            if (!Validation.validateListBoxRequired(location, new HTML(), wfmStrings.pleaseSelect())) {
                errors++;
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null && formPropertyMap.get(DESCRIPTION).isRequired()) {
            errors += markAsError(DESCRIPTION, description, !Validation.validateTextAreaRequired(description));
        }




        errors += getCustomFieldUtil().validateCustomFields();
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
        }
        return errors == 0;
    }

    private BookingItemsItem getBookingItemObject() {
        bookingItems.setObjectID(objectID);
        bookingItems.setItemName(name.getText());
        bookingItems.setCategory(category.getSelectedItem());
        bookingItems.setDescription(description.getText());
        if (itemNubmer != null && itemNubmer.getText() != null) {
            bookingItems.setItemNumber(itemNubmer.getText());
        } else {
            bookingItems.setItemNumber(productNumberWidget.getNumberData(false).getNumberString());
        }
        bookingItems.setNumberData(productNumberWidget.getNumberData(false));
        bookingItems.setLocationID(location.getSelectedId());
        bookingItems.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        return bookingItems;
    }

    private void save(BookingItemsItem item) {
        LoadingPanel.loading(true);
        projectService.saveBookingItem(item, new AbstractAsyncCallback<Integer>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            public void success(Integer result) {
                LoadingPanel.loading(false);
                saveButtonEnable(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.item()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BOOKING_ITEMS_SAVED, null, null);
                closeTab();
            }
        });
    }

    @Override
    protected void addButtons() {
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            if (validate()) {
                bookingItems = getBookingItemObject();
                save(bookingItems);

            }
        });
        saveButton.ensureDebugId("saveButton");
        addButton(saveButton);
    }

    private void saveButtonEnable(boolean enabled) {
        saveButton.setEnabled(enabled);
    }

    private void generateBookingItemNumber() {
        projectService.generateBookingItemNumber(new AbstractAsyncCallback<NumberData>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(NumberData result) {
                numberData = result;
                productNumberWidget.setNumberData(numberData);
            }
        });
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BOOKING_ITEMS_ADD_VIEW;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
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


    @Override
    public String getIconStyle() {
        return null;
    }
}
