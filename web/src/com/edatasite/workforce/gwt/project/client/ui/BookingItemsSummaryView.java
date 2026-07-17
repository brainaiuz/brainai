package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.BookingReservationItem;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.KpiDataGrid;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/19/12
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class BookingItemsSummaryView extends CustomForm2 implements Colapse, Constants {
    private static final ProjectServiceAsync projectService = ProjectService.App.get();

    private Integer objectId;
    private HTMLPanel htmlPanel;
    protected HashMap<String, Widget> widgetsMap;
    private KpiDataGrid<BookingReservationItem> dataGrid;
    private HTML bookingItemsStatus, location, name, productNumberWidget, category;
    private TextArea description;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public BookingItemsSummaryView(Integer id) {
        super("summary", wfmStrings.itemsSummary());
        objectId = id;
    }


    public BookingItemsSummaryView() {
    }

    public static final ProvidesKey<BookingReservationItem> KEY_PROVIDER = item -> item != null ? item.getObjectID() : null;

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.BookingItemsView, getFormID(),new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                BookingItemsSummaryView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {

        location = new HTML();
        location.addStyleName("form-control");

        name = new HTML();
        name.addStyleName("form-control");

        productNumberWidget = new HTML();
        productNumberWidget.addStyleName("form-control");

        category = new HTML();
        category.addStyleName("form-control");

        description = new TextArea();
        description.addStyleName("form-control");
        description.setStyleName("booking-item-description-edit");


        bookingItemsStatus = new HTML();
        bookingItemsStatus.addStyleName("form-control");

        dataGrid = new KpiDataGrid<>(KEY_PROVIDER);
        initTableColumn();

        addTitleField(INFORMATION, wfmStrings.information());
        if (formPropertyMap != null && formPropertyMap.get(LOCATION_FIELD) != null) {
            addField(LOCATION_FIELD, location, getTitle(formPropertyMap.get(LOCATION_FIELD).isChanged() ? formPropertyMap.get(LOCATION_FIELD).getTitle() : wfmStrings.location(), formPropertyMap.get(LOCATION_FIELD).isRequired()),false,
                    formPropertyMap.get(LOCATION_FIELD).isInformation());
            if (formPropertyMap.get(LOCATION_FIELD).isInformation()) {
                new KpiToolTip(location, formPropertyMap.get(LOCATION_FIELD).getInformationText());
            }
        } else {
            addField(LOCATION_FIELD, location, getTitle(wfmStrings.location(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(NAME) != null) {
            addField(NAME, name, getTitle(formPropertyMap.get(NAME).isChanged() ? formPropertyMap.get(NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(NAME).isRequired()),false,
                    formPropertyMap.get(NAME).isInformation());
            if (formPropertyMap.get(NAME).isInformation()) {
                new KpiToolTip(name, formPropertyMap.get(NAME).getInformationText());
            }
        } else {
            addField(NAME, name, getTitle(wfmStrings.name(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(DESCRIPTION) != null) {
            addField(DESCRIPTION, description, getTitle(formPropertyMap.get(DESCRIPTION).isChanged() ? formPropertyMap.get(DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(DESCRIPTION).isRequired()),false,
                    formPropertyMap.get(DESCRIPTION).isInformation());
            if (formPropertyMap.get(DESCRIPTION).isInformation()) {
                new KpiToolTip(description, formPropertyMap.get(DESCRIPTION).getInformationText());
            }
        } else {
            addField(DESCRIPTION, description, getTitle(wfmStrings.name(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            addField(CustomFormConstants.NUMBER, productNumberWidget, getTitle(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.NUMBER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.NUMBER).isInformation()) {
                new KpiToolTip(productNumberWidget, formPropertyMap.get(CustomFormConstants.NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.NUMBER, productNumberWidget, getTitle(wfmStrings.number(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CATEGORY) != null) {
            addField(CustomFormConstants.CATEGORY, category, getTitle(formPropertyMap.get(CustomFormConstants.CATEGORY).isChanged() ? formPropertyMap.get(CustomFormConstants.CATEGORY).getTitle() : wfmStrings.category(), formPropertyMap.get(CustomFormConstants.CATEGORY).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.CATEGORY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.CATEGORY).isInformation()) {
                new KpiToolTip(category, formPropertyMap.get(CustomFormConstants.CATEGORY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CATEGORY, category, getTitle(wfmStrings.category(), false));
        }
//        addField(LOCATION_FIELD, location, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location())));
//        addField(NAME, name, getTitle(wfmStrings.name()));
//        addField(DESCRIPTION, description, getTitle(wfmStrings.description()));
//        addField(CustomFormConstants.NUMBER, productNumberWidget, getTitle(wfmStrings.number()));
//        addField(CustomFormConstants.CATEGORY, category, getTitle(wfmStrings.category()));
        addField(RESERVATION_HISTORY, dataGrid, wfmStrings.reservationHistory());
        getCustomFieldUtil().drawCustomFields(this, objectId, true);
        show();

    }

    @Override
    protected void initPredefinedValues() {

    }


    @Override
    protected void getDataToFillFields() {
        drawForm(false);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_BOOKING_ITEMS_RESERVATION_SAVED, BookingItemsSummaryView.this, (sender, args) -> drawForm(true));

    }

    private void drawForm(boolean reload) {
        projectService.getBookingItemsData(objectId, new AbstractAsyncCallback<BookingItemsItem>() {
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

    private void fillFormWithData(BookingItemsItem bookingItems) {
        if (objectId != null) {
            location.setHTML(bookingItems.getLocation());
            name.setHTML(bookingItems.getItemName());
            description.setText(bookingItems.getDescription());
            productNumberWidget.setHTML(bookingItems.getItemNumber());
            category.setHTML(bookingItems.getCategory().getName());
            bookingItemsStatus = new HTML();
            bookingItemsStatus.addStyleName("form-control");
            addField(STATUS, bookingItemsStatus, wfmStrings.status());
            bookingItemsStatus.setHTML(bookingItems.getStatus());
            getCustomFieldUtil().fillCustomFieldsWithData(bookingItems.getCustomFieldItems(), true);
        }
        dataGrid.supplyProvider(bookingItems.getBookingReservationItemList());
        dataGrid.refresh();

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
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.reservations()), Info.Type.INFO);
                    dataGrid.getList().remove(index);
                    dataGrid.refresh();
                }
            });
        });
        dataGrid.addColumn(romeve, wfmStrings.delete());
        dataGrid.setColumnWidth(romeve, 15, com.google.gwt.dom.client.Style.Unit.PCT);

    }

    @Override
    protected void addButtons() {
        WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY, clickEvent -> {
            SinksContainerFactory.entryPoint.onHistoryChanged("bookingitems|edit/" + objectId);
        });
        addButton(editButton);
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
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }


    @Override
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
