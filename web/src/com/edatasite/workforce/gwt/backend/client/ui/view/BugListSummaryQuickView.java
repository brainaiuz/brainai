package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BugsPerEmployeesListItem;
import com.edatasite.workforce.gwt.core.client.ui.QuickViewPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionLabel;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 03.06.2009
 * Time: 23:15:29
 * To change this template use File | Settings | File Templates.
 */
public class BugListSummaryQuickView extends QuickViewPanel {

    private static final BackendStrings backendStrings = BackendStrings.App.get();

    public BugListSummaryQuickView() {
        super();
    }

    protected boolean doPreview(Object o) {

        if (!(o instanceof BugsPerEmployeesListItem)) {
            return false;
        }
        clear();
        final BugsPerEmployeesListItem item = (BugsPerEmployeesListItem) o;

        PreviewSectionField field;
        PreviewSectionLabel label;
        final PreviewSectionContainer container = new PreviewSectionContainer();

        if (item != null) {
            label = new PreviewSectionLabel(wfmStrings.employeeSummary(), "");

            SimpleLink bugViewLink = new SimpleLink("<b>" + item.getStatusNew().toString() + "</b>");
            bugViewLink.addClickHandler(sender -> SinksContainerFactory.entryPoint.onHistoryChanged("bugListSummary|buglistview/" + item.getObjectID() + "/" + item.getNewStatusName()));
            field = new PreviewSectionField();
            field.addField(wfmStrings.employee(), item.getEmployee());
            field.addField(wfmStrings.total(), item.getTotal().toString());
            field.addField(wfmStrings.New(), bugViewLink);
            field.addField(backendStrings.resolved(), item.getResolved().toString());
            field.addField(backendStrings.underInvestigation(), item.getUnderInvest().toString());
            field.addField(wfmStrings.inProgress(), item.getInProgress().toString());
            field.addField(backendStrings.ignored(), item.getIgnored().toString());
            field.addField(wfmStrings.done(), item.getDone().toString());

            container.addSection(label, field);
        }
        add(container);
        return true;
    }
}
