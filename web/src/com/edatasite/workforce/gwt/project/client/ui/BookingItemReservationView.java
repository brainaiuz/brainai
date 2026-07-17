package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.BookingReservationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 6/18/12
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */

public class BookingItemReservationView extends CustomForm2 implements HasLinksInterface, Colapse, Constants {

    private final Integer objectID;

    private HTML reservedByLabel, bookingItemsCategoryLabel, bookingItemsLabel, startDateLabel, endDateLabel, linksLabel;
    private HasLinks linkingUtil;
    private FooterInformer link;
    private HTMLPanel htmlPanel;
    private BookingReservationItem item;
    private final AtomicBoolean firstClick = new AtomicBoolean(true);
    private HashMap<String, Widget> widgetsMap;

    public BookingItemReservationView(Integer objectID) {
        super("summary", "Reservation Summary");
        this.objectID = objectID;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }


    @Override
    protected void registerFields() {
        reservedByLabel = new HTML();
        bookingItemsCategoryLabel = new HTML();
        bookingItemsLabel = new HTML();
        startDateLabel = new HTML();
        endDateLabel = new HTML();

        addTitleField(INFORMATION, wfmStrings.information());
        addField(RESERVED_BY, reservedByLabel, wfmStrings.reservedBy());
        addField(CATEGORY, bookingItemsCategoryLabel, wfmStrings.category());
        addField(ITEMS, bookingItemsLabel, wfmStrings.item());
        addField(START_DATE, startDateLabel, wfmStrings.startDate());
        addField(END_DATE, endDateLabel, wfmStrings.endDate());
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected void getDataToFillFields() {
        ProjectService.App.get().getBookingItemReservation(objectID, new AbstractAsyncCallback<BookingReservationItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(BookingReservationItem bookingItems) {
                LoadingPanel.loading(false);
                item = bookingItems;
                drawForm();
            }
        });
    }


    @Override
    protected void addButtons() {
        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        if (objectID != null) {
            footer.addToLeftSide(link);
        }
    }

    private void drawForm() {
        reservedByLabel.setHTML(item.getSelectedReservedById().getName());
        bookingItemsCategoryLabel.setHTML(item.getSelectedCategoryId().getName());
        bookingItemsLabel.setHTML(item.getBookingItemName());
        startDateLabel.setHTML(DateUtils.formatInternal(item.getFromDate()));
        endDateLabel.setHTML(DateUtils.formatInternal(item.getToDate()));
        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                ArrayList<RelationItem> relationItems = new ArrayList<>();
                getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });
        link.setBadgeCount(item.getRelations().size());

    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BOOKING_ITEM_RESERVATION_VIEW;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    public String getIconStyle() {
        return null;
    }

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(BookingItemReservationView.this) {
                @Override
                protected boolean isActionEditing() {
                    return true;
                }

                @Override
                public Integer getRelationID() {
                    return objectID;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_BOOKING;
                }

                @Override
                public String getRelationName() {
                    return item != null ? item.getBookingItemName() : null;
                }

            };
        }
        return linkingUtil;
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
}
