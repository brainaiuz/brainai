package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingMessages;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.invoice.client.rpc.RevisionHistoryItem;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/5/12
 * Time: 6:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class RevisionHistoryPanel extends VerticalPanel {
    private static final AccountingMessages accountingMessages = AccountingMessages.App.get();

    private static DateTimeFormat dateFormat = DateTimeFormat.getFormat(Constants.LONG_DATE_FORMAT_5);

    private RevisionHistoryItem[] revisionHistoryItems;

    private String viewName;

    public RevisionHistoryPanel(RevisionHistoryItem[] revisionHistoryItems, String viewName) {
        this.revisionHistoryItems = revisionHistoryItems;
        this.viewName = viewName;
        initialize();
    }

    private void initialize() {
        setWidth("100%");
        for (RevisionHistoryItem hi : revisionHistoryItems) {
            final Integer objectID = hi.getObjectID();
            if (hi.getUpdaterName() != null && hi.getNumber() != null && hi.getUpdatedDate() != null) {
                FlowPanel flowPanel = new FlowPanel();
                Label historyUpdatedLabel;
                if (hi.getObjectID().equals(revisionHistoryItems[revisionHistoryItems.length - 1].getObjectID())) {
                    historyUpdatedLabel = new Label(accountingMessages.historyCreated(hi.getUpdaterName()));
                } else {
                    historyUpdatedLabel = new Label(accountingMessages.historyUpdated(hi.getUpdaterName()));
                }
                Anchor historyLink = new Anchor(hi.getNumber());
                Label updatedOnLabel = new Label(accountingMessages.updatedOn(dateFormat.format(hi.getUpdatedDate())));

                historyLink.addClickHandler(event -> {
                    if (viewName != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(viewName + "|summary/" + objectID);
                    }
                });

                historyUpdatedLabel.getElement().getStyle().setFloat(Style.Float.LEFT);
                historyLink.getElement().getStyle().setFloat(Style.Float.LEFT);
                updatedOnLabel.getElement().getStyle().setFloat(Style.Float.LEFT);
                historyLink.getElement().getStyle().setPaddingLeft(3, Style.Unit.PX);
                historyLink.getElement().getStyle().setPaddingRight(3, Style.Unit.PX);

                flowPanel.add(historyUpdatedLabel);
                flowPanel.add(historyLink);
                flowPanel.add(updatedOnLabel);
                add(flowPanel);
            }
        }
    }
}
