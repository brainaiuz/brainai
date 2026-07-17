package com.edatasite.workforce.gwt.crm.client.ui.view.widgets;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 11.04.12
 * Time: 12:27
 */

public class ContactStatusHistoryGrid extends AbstractDataGrid<ContactListItem> {
    protected Integer objectID;
    protected Integer contactType;
    private boolean isContactHistory;

    public ContactStatusHistoryGrid(Integer objectID, Integer contactType, boolean isContactHistory) {
        this();
        this.objectID = objectID;
        this.contactType = contactType;
        this.isContactHistory = isContactHistory;
        initialize();
    }

    protected ContactStatusHistoryGrid() {
        super();
    }

    @Override
    protected void addColums() {
        addColumn(new Column<ContactListItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ContactListItem item) {
                return () -> "<span>" + (item.getLeadStatus(true).getId() != null ? item.getLeadStatus(true).getName() : wfmStrings.notAvailable()) + "</span>";
            }
        }, wfmStrings.status());

        addColumn(new Column<ContactListItem, SafeHtml>(new SafeHtmlCell()) {

            @Override
            public SafeHtml getValue(final ContactListItem item) {
                return () -> "<span>" + (item.getCreatedDate() != null ? DateUtils.formatInternal(item.getCreatedDate()) : wfmStrings.notAvailable()) + "</span>";
            }
        }, wfmStrings.createdDate());

        addColumn(new Column<ContactListItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ContactListItem item) {
                return () -> "<span>" + (item.getUpdatedDate() != null ? DateUtils.formatInternal(item.getUpdatedDate()) : wfmStrings.notAvailable()) + "</span>";
            }
        }, wfmStrings.modifiedDate());

        addColumn(new Column<ContactListItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ContactListItem item) {
                return () -> "<span>" + (item.getOwnerId() != null ? item.getOwner() : wfmStrings.notAvailable()) + "</span>";
            }
        }, wfmStrings.modifiedBy());
    }

    @Override
    public void refresher() {
        ContactService.App.get().getStatusHistory(objectID, contactType, isContactHistory,  new AsyncCallback<ContactListItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                WfmWindow.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(ContactListItem[] result) {
                if (result != null && result.length > 0) {
                    supplyProvider(result);
                    reDrawItems();
                }
            }
        });
    }
}
