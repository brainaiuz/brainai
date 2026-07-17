package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class OrgPositionCard extends Composite {

    interface Binder extends UiBinder<Widget, OrgPositionCard> {
    }

    private static final Binder uiBinder = GWT.create(Binder.class);

    @UiField
    Label title;
    @UiField
    Label owner;
    @UiField
    HTML description;
    @UiField
    HTML metrics;

    private final OrgPositionDTO pos;

    public OrgPositionCard(OrgPositionDTO pos) {
        this.pos = pos;
        initWidget(uiBinder.createAndBindUi(this));
        bind();
        getElement().addClassName("orgPosition-" + pos.getColorKey());
    }

    private void bind() {
        title.setText(pos.getTitle());
        owner.setText(pos.getOwnerName());
        description.setHTML(SafeHtmlUtils.fromString(
                nullToEmpty(pos.getDescription())).asString());
        metrics.setHTML(SafeHtmlUtils.fromString(
                nullToEmpty(pos.getMetrics())).asString());
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
