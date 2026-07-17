package com.edatasite.workforce.gwt.crm.client.ui.view.widgets;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmHistoryList;
import com.google.gwt.user.client.ui.*;

/**
 * User: dilshod madrahimov
 * Date: 4/28/12
 * Time: 4:14 PM
 */
public class ContactUpdatesGrid extends Composite {

    private Integer contactID;
    private FlexTable historyTable;
    private ScrollPanel scrollPanel;
    private FlowPanel container = new FlowPanel();

    public ContactUpdatesGrid(Integer contactID) {
        super();
        this.contactID = contactID;
        viewShow();
        scrollPanel = new ScrollPanel(container);
        historyTable = new FlexTable();
        historyTable.setWidth("100%");
        container.setWidth("100%");
        scrollPanel.setWidth("100%");
        scrollPanel.setHeight("100%");
        initWidget(scrollPanel);
        setWidth("100%");
        draw();
    }

    public void viewShow() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_ADD, ContactUpdatesGrid.this, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_CAREER_ADD_EDIT, ContactUpdatesGrid.this, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_CAREER_DELETE, ContactUpdatesGrid.this, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NOTE_FOR_CRM_ADD, ContactUpdatesGrid.this, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NOTE_FOR_CRM_DELETE, ContactUpdatesGrid.this, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_TASK_ADD_EDIT, ContactUpdatesGrid.this, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, ContactUpdatesGrid.this, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_ACTIVITY_DELETED, ContactUpdatesGrid.this, (sender, args) -> draw());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MAIL_COMPOSE, ContactUpdatesGrid.this, (sender, args) -> draw());
    }

    private void draw() {
        CRMService.App.get().getCrmHistories(contactID, RelationItem.TYPE_CONTACT, new AbstractAsyncCallback<CrmHistoryList[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(CrmHistoryList[] contactHistoryLists) {
                historyTable.clear();
                getContactHistoryList(contactHistoryLists);
            }
        });

    }

    public Widget getContactUpdatesPanel() {
        viewShow();
        return historyTable;
    }

    private void getContactHistoryList(CrmHistoryList[] contactHistoryLists) {
        if (contactHistoryLists != null && contactHistoryLists.length > 0) {
            for (int i = 0; i < contactHistoryLists.length; i++) {
                CrmHistoryList historyList = contactHistoryLists[i];
                Image image = new Image();
                image.setWidth("29px");
                if (historyList.getUpdaterImageURL() != null) {
                    image.setUrl(historyList.getUpdaterImageURL());
                } else {
                    image = new Image();
                }
                Grid grid = new Grid(3, 1);
                grid.setCellPadding(2);
                grid.setCellSpacing(2);
                grid.setHTML(0, 0, "<b>" + historyList.getUpdater() + "</b>");
                grid.setHTML(1, 0, "<span style='color:#13649b;font-size:12px;width:100%;'>" + historyList.getMessage() + "</span>");
                grid.setHTML(2, 0, "<span style='color:gray;height:10px;padding-left:1px;'>" + DateUtils.formatInternal(historyList.getCreationTime()) + "</span>");

                FlexTable flexTable = new FlexTable();
                flexTable.getElement().setAttribute("style", "width:100%;border-bottom:1px dotted #CCCCCC;height:3px;padding-bottom:3px;");
                flexTable.setHTML(0, 0, "<div class=not-image-smalls>" + image + "</div>");
                flexTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
                flexTable.getFlexCellFormatter().setStyleName(0, 0, "paddingTop4");
                flexTable.setWidget(0, 1, grid);

                flexTable.getFlexCellFormatter().setWidth(0, 0, "7%");
                flexTable.getFlexCellFormatter().setWidth(0, 1, "93%");

                historyTable.setWidget(i, 0, flexTable);
                historyTable.getFlexCellFormatter().setVerticalAlignment(i, 0, HasVerticalAlignment.ALIGN_TOP);
            }
            container.add(historyTable);
        }
        if (contactHistoryLists.length > 5) {
            setHeight("295px");
        }
    }

}
