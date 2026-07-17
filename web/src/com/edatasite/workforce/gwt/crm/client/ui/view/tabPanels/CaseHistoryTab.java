/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/29 8:56:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmHistoryList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Apr 27, 2010
 * Time: 6:17:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class CaseHistoryTab extends Composite {

    private FlexTable historyTable;
    private ScrollPanel scrollPanel;
    private final CRMServiceAsync crmService = CRMService.App.get();
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private Integer caseID;
    FlowPanel container = new FlowPanel();

    public CaseHistoryTab(Integer objectID) {
        super();
        this.caseID = objectID;
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASE_ADD, CaseHistoryTab.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASE_REPLY_TO_REPORTER, CaseHistoryTab.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASE_FORWARDED, CaseHistoryTab.this, (sender, args) -> refresh());
        scrollPanel = new ScrollPanel(container);
        historyTable = new FlexTable();
        historyTable.addStyleName("caseHistoryLog");
        container.setWidth("100%");
        scrollPanel.setWidth("100%");
        scrollPanel.setHeight("100%");
        scrollPanel.setHorizontalScrollPosition(0);
        initWidget(scrollPanel);
        setWidth("100%");
        refresh();
    }

    public void refresh() {
        crmService.getCrmHistories(caseID, RelationItem.TYPE_CASE, new AbstractAsyncCallback<CrmHistoryList[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(CrmHistoryList[] result) {
                historyTable.clear();
                getHistoryList(result);
            }
        });
    }

    private void getHistoryList(CrmHistoryList[] historyLists) {
        if (historyLists != null && historyLists.length > 0) {
            for (int i = 0; i < historyLists.length; i++) {
                Image userImage = new Image();
                userImage.setWidth("29px");
                if (historyLists[i].getUpdaterImageURL() != null) {
                    userImage.setUrl(historyLists[i].getUpdaterImageURL());
                } /*else {
                    userImage = new Image(imageBundle.getNoPhotoSmall());
                }*/

                Grid grid = new Grid(3, 1);
                grid.addStyleName("caseHistoryLog-text");
                grid.setWidget(0, 0, new HTML("<span class=caseHistoryLog-status>" + historyLists[i].getMessage() + "</span>"));
                grid.setWidget(1, 0, new HTML("&nbsp; by &nbsp; <span class=caseHistoryLog-user>" + historyLists[i].getUpdater() + "</span"));
                grid.setHTML(2, 0, "<span class=caseHistoryLog-date>" + DateUtils.formatInternal(historyLists[i].getCreationTime()) + "</span>");

                FlexTable flexTable = new FlexTable();
                if (i != historyLists.length - 1) {
//                    flexTable.getElement().setAttribute("style", "width:100%; border-bottom:1px dotted #CCCCCC;height:3px;padding-bottom:3px;");
                    flexTable.setStyleName("caseHistoryLog-length1");
                }
                flexTable.setHTML(0, 0, "<div class=img-holder>" + userImage + "</div>");
                flexTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
                flexTable.setWidget(0, 1, grid);
                flexTable.getFlexCellFormatter().setWidth(0, 0, "7%");
                flexTable.getFlexCellFormatter().setWidth(0, 1, "93%");
                flexTable.addStyleName("caseHistoryLog-item");
                flexTable.getFlexCellFormatter().setStyleName(0, 0, "caseHistoryLog-item__logo");
                flexTable.getFlexCellFormatter().setStyleName(0, 1, "caseHistoryLog-item__text");

                historyTable.setWidget(i, 0, flexTable);
                historyTable.getFlexCellFormatter().setVerticalAlignment(i, 0, HasVerticalAlignment.ALIGN_TOP);
            }
            container.add(historyTable);
        } else {
            getEmptyPanel(Property.get(Constants.CASE_LIST, crmStrings.noCaseHistory(), wfmStrings.caseID()), null, null);
        }
        if (historyLists.length > 4) {
//            setHeight("330px");
        }
    }

    public void getEmptyPanel(String message, String textBeforeLink, final String link) {
        historyTable.clear();
        final VerticalPanel vpanel = new VerticalPanel();
        vpanel.setSize("100%", "100%");
        final HorizontalPanel centerPanel = new HorizontalPanel();
        final HorizontalPanel horz = new HorizontalPanel();
        final HTML noNotes = new HTML(message);
        horz.add(noNotes);
        if (textBeforeLink != null) {
            final SimpleLink mylink = new SimpleLink(textBeforeLink);
            horz.add(mylink);
            mylink.addClickHandler(event -> {
                if (link != null) {
                    SinksContainerFactory.entryPoint.onHistoryChanged(link);
                }
            });
        }
        vpanel.add(horz);
        vpanel.setCellHorizontalAlignment(horz, HasHorizontalAlignment.ALIGN_CENTER);
        vpanel.setCellVerticalAlignment(horz, HasVerticalAlignment.ALIGN_MIDDLE);
        DOM.setStyleAttribute(this.getElement(), "overflow", "hidden");
        container.clear();
        container.add(vpanel);
    }
}