package com.edatasite.workforce.gwt.assessment.client.ui.view.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.ui.html.Div;

/**
 * User: Ilhombek
 * Date: 12/26/12
 * Time: 5:55 PM
 */
public class GeneralCommentBuilder extends Composite {

    interface GeneralCommentBuilderUiBinder extends UiBinder<VerticalPanel, GeneralCommentBuilder> {
    }

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private TextArea2 generalCommentArea;

    @UiField
    HTMLPanel contentPanel;

    /**
     * Register comment builder constructor
     */
    public GeneralCommentBuilder() {
        GeneralCommentBuilderUiBinder ourUiBinder = GWT.create(GeneralCommentBuilderUiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));
        drawInitialize();
    }

    public String getText() {
        return generalCommentArea.getText();
    }

    public void setEnabled(boolean enabled) {
        this.generalCommentArea.setEnabled(enabled);
    }

    public void setText(String generalCommentText) {
        this.generalCommentArea.setText(generalCommentText);
    }

    private void drawInitialize() {
        //comment builder header message
        Div titleDiv = new Div("form-group__label");
        titleDiv.getElement().setInnerText(wfmStrings.comments());
        contentPanel.add(titleDiv);

        //comment builder text area
        generalCommentArea = new TextArea2(3000);
        generalCommentArea.getTextArea().getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        generalCommentArea.getTextArea().getElement().getStyle().setBorderColor("#000000");
        generalCommentArea.getTextArea().getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        generalCommentArea.setWidth(743);
        generalCommentArea.setHeight(70);
        contentPanel.add(generalCommentArea);
    }
}
