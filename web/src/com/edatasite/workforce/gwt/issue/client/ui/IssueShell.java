package com.edatasite.workforce.gwt.issue.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * User: Sherzod
 * Date: May 26, 2009
 * Time: 7:22:51 PM
 */
public class IssueShell extends KpiModal {
    private final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final PerformanceNoteItem[] noteItems;

    public IssueShell(PerformanceNoteItem[] noteItems) {
        this.noteItems = noteItems;
        loadContent();
    }

    private void loadContent() {
        WfmForm summary = new WfmForm(new String[]{"30%", "70%"});
        for (int i = 0; i < noteItems.length; i++) {
            PerformanceNoteItem noteItem = noteItems[i];
            final Integer objectId = noteItem.getObjectID();
            summary.addField(hrmsStrings.noteName(), new Label(noteItem.getName()));
            summary.addField(hrmsStrings.noteDescription(), new Label(noteItem.getDescription()));
            summary.addField(hrmsStrings.noteStatus(), new Label(noteItem.getStatusName()));
            SimpleLink viewDetails = new SimpleLink(wfmStrings.viewDetails());
            FlexTable panel = new FlexTable();

            panel.setWidget(0, 0, viewDetails);
            viewDetails.addClickHandler(sender -> {
                closeShell();
                SinksContainerFactory.entryPoint.onHistoryChanged("performancenote|summary/" + objectId);
            });
            panel.setWidget(0, 1, viewDetails);
            panel.getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER);
            summary.addField(wfmStrings.resolverOwner(), panel);
            if (i < noteItems.length - 1) {
                summary.addHorizontalLineForAppraisals();
            }
        }
        WfmButton2 closeButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        closeButton.addClickHandler(sender -> closeShell());
        modalFooter.add(closeButton);


//        scroll panel
        ScrollPanel scrollPanel = new ScrollPanel();
        scrollPanel.setSize("350px", "400px");
        scrollPanel.add(summary);

        add(scrollPanel);

        setSize(400, 400);
    }

    private void closeShell() {
        close();
    }
}