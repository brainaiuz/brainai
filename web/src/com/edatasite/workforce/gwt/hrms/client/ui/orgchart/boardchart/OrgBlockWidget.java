package com.edatasite.workforce.gwt.hrms.client.ui.orgchart.boardchart;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class OrgBlockWidget extends Composite {

    interface Binder extends UiBinder<Widget, OrgBlockWidget> {
    }

    private static final Binder uiBinder = GWT.create(Binder.class);

    @UiField
    FlowPanel root;
    @UiField
    FlowPanel header;
    @UiField
    Label title;
    @UiField
    Label subtitle;
    @UiField
    Label owner;
    @UiField
    HTML description;
    @UiField
    HTML metrics;
    @UiField
    FlowPanel positionsGrid;

    private final OrgBlockDTO block;

    public OrgBlockWidget(OrgBlockDTO block) {
        this.block = block;
        initWidget(uiBinder.createAndBindUi(this));
        bind();
    }

    private void bind() {
        title.setText(block.getTitle());
        subtitle.setText(block.getSubtitle());
        owner.setText(block.getOwnerName());

        description.setHTML(SafeHtmlUtils.fromString(
                nullToEmpty(block.getDescription())).asString());
        metrics.setHTML(SafeHtmlUtils.fromString(
                nullToEmpty(block.getMetrics())).asString());

        root.addStyleName("orgBlock-" + block.getColorKey());

        if (block.isLargeHeader()) {
            root.addStyleName("orgBlock-large");
        }

        positionsGrid.clear();
        if (block.getPositions() != null && !block.getPositions().isEmpty()) {
            for (OrgPositionDTO pos : block.getPositions()) {
                positionsGrid.add(new OrgPositionCard(pos));
            }
        } else {
            positionsGrid.setVisible(false);
        }
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
