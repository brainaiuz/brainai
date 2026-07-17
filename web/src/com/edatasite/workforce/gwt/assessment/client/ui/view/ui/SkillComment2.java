package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * User: Ilhombek
 * Date: 3/19/12
 * Time: 9:13 AM
 */
public class SkillComment2 extends Composite {

    private String commenter;
    private String comment;
    private String commenterTeam;
    private boolean isFromShift = false;
    @UiField
    HTMLPanel content;


    interface SkillComment2UiBinder extends UiBinder<HTMLPanel, SkillComment2> {
    }

    /**
     * Create constructor with params
     *
     * @param commenter     - commenter name
     * @param commenterTeam - commenter department name
     * @param comment       - given comment
     */
    public SkillComment2(String commenter, String commenterTeam, String comment) {
        SkillComment2UiBinder ourUiBinder = GWT.create(SkillComment2UiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));
        this.commenter = commenter;
        this.commenterTeam = commenterTeam;
        this.comment = comment;
        this.isFromShift = isFromShift;
        drawInitialize();
    }

    private void drawInitialize() {
        HTML fullHTML = new HTML();
        //initialize
        String name = "";
        if (commenter != null && !isFromShift) {
            name += "<b>" + commenter + "</b>";
        }
        if (commenterTeam != null && isFromShift) {
            name += "<b> (" + commenterTeam + "):</b> ";
        }
        if (comment != null) {
            name += formatString(comment);
        }
        fullHTML.setHTML(name);
        content.add(fullHTML);
    }

    private String formatString(String text) {
        return text != null && !"".equals(text) ? text.replace("\r\n", "<br/>").replace("\n", "<br/>") : text;
    }
}
