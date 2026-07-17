package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.localization.BackendStrings;
import com.edatasite.workforce.gwt.backend.client.rpc.BugsPerEmployeesListItem;
import com.edatasite.workforce.gwt.core.client.ui.QuickViewPanel;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionContainer;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionField;
import com.edatasite.workforce.gwt.core.client.ui.preview.PreviewSectionLabel;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 05.06.2009
 * Time: 18:02:50
 * To change this template use File | Settings | File Templates.
 */
public class BugListPerSectionQuickView extends QuickViewPanel {
    private static final BackendStrings backendStrings = BackendStrings.App.get();

    public BugListPerSectionQuickView() {
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
            label = new PreviewSectionLabel(backendStrings.sectionSummary(), "");
            field = new PreviewSectionField();
            field.addField(wfmStrings.section(), item.getSection());
            field.addField(wfmStrings.total(), item.getTotal().toString());
            field.addField(wfmStrings.New(), item.getStatusNew().toString());
            field.addField(backendStrings.resolved(), item.getResolved().toString());
            field.addField(backendStrings.underInvestigation(), item.getUnderInvest().toString());
            field.addField(wfmStrings.inProgress(), item.getInProgress().toString());
            field.addField(backendStrings.ignored(), item.getIgnored().toString());
            field.addField(wfmStrings.done(), item.getDone().toString());

            container.addSection(label, field);
        }

        add(container);
//        setScrollEnabled(true);
//        layout(true);
        return true;
    }
}
