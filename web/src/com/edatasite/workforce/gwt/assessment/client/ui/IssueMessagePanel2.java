package com.edatasite.workforce.gwt.assessment.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import com.edatasite.workforce.gwt.issue.client.ui.IssueShell;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * User: Ilhombek
 * Date: 3/27/12
 * Time: 2:55 PM
 */
public class IssueMessagePanel2 extends Composite {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    private final PerformanceNoteItem[] noteItems;
    private final String string_employeeName;
    private boolean shellOpen;

    @UiField
    ImageElement iconMessage;
    @UiField
    HTML performanceNoteMessage;

    interface IssueMessagePanel2UiBinder extends UiBinder<HTMLPanel, IssueMessagePanel2> {
    }

    public IssueMessagePanel2(PerformanceNoteItem[] noteItems, String string_employeeName) {
        this.noteItems = noteItems;
        this.string_employeeName = string_employeeName;
        IssueMessagePanel2UiBinder ourUiBinder = GWT.create(IssueMessagePanel2UiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));
        drawInitialize();
    }

    private void drawInitialize() {
        String htmlString = "";
        if (noteItems.length > 1) {
            htmlString += projectStrings.thereAre() + " " + noteItems.length + " " + projectStrings.activePerformanceNotes() + " " + " " + wfmStrings.forLocalize() + " " + string_employeeName + "</b>";
        } else {
            htmlString += projectStrings.thereIs() + " " + noteItems.length + " " + projectStrings.activePerformanceNote() + " " + " " + wfmStrings.forLocalize() + " " + string_employeeName + "</b>";
        }

        performanceNoteMessage.setHTML(htmlString);
        performanceNoteMessage.addClickHandler(event -> {
            IssueShell issueShell = new IssueShell(noteItems);
            issueShell.addCloseHandler(popupPanelCloseEvent -> shellOpen = false);
            if (!shellOpen) {
                issueShell.open();
                shellOpen = true;
            }
        });
    }
}
