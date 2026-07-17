package com.edatasite.workforce.gwt.core.client.ui.components.groupBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Abror Abdukadirov
 * Date: 07.02.2018 16:52
 */
public class SectionBoxPanel extends Composite {
    interface SectionBoxPanelUiBinder extends UiBinder<Widget, SectionBoxPanel> {
    }

    private static SectionBoxPanelUiBinder ourUiBinder = GWT.create(SectionBoxPanelUiBinder.class);

    @UiField
    HTMLPanel contentPanel;

    public SectionBoxPanel() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public GBox drawNewGroupBox() {
        GBox gBox = new GBox(new GBoxRow());
        contentPanel.add(gBox);
        return gBox;
    }

    public Widget addGroupBoxItem(Widget groupBoxItem) {
        return this.addGroupBoxItem(0, groupBoxItem);
    }

    //if several GBox
    //get GBox by index
    public Widget addGroupBoxItem(int groupBoxIndex, Widget groupBoxItem) {
        GBox groupBox = (GBox) this.contentPanel.getWidget(groupBoxIndex);
        GBoxRow groupBoxRow = (GBoxRow) groupBox.getWidget(0);
        groupBoxRow.add(groupBoxItem);
        return groupBoxItem;
    }

    public GBoxItem addGroupBoxItem(String label, Widget widget) {
        return this.addGroupBoxItem(0, label, widget);
    }

    public GBoxItem addGroupBoxItem(int groupBoxIndex, String label, Widget widget) {
        GBox groupBox = (GBox) this.contentPanel.getWidget(groupBoxIndex);
        GBoxRow groupBoxRow = (GBoxRow) groupBox.getWidget(0);
        GBoxItem groupBoxItem = new GBoxItem();
        if (label != null) {
            groupBoxItem.setLabel(label);
        }
        groupBoxItem.setComponent(widget);
        groupBoxRow.add(groupBoxItem);
        return groupBoxItem;
    }

    public GBoxItem addGroupBoxItem(int groupBoxIndex, String label, Widget labelWidget, Widget widget) {
        GBox groupBox = (GBox) this.contentPanel.getWidget(groupBoxIndex);
        GBoxRow groupBoxRow = (GBoxRow) groupBox.getWidget(0);
        GBoxItem groupBoxItem = new GBoxItem();
        if (label != null) {
            groupBoxItem.setLabel(label);
            groupBoxItem.getLabel().add(labelWidget);
        }
        groupBoxItem.setComponent(widget);
        groupBoxRow.add(groupBoxItem);
        return groupBoxItem;
    }

    @Override
    protected void onAttach() {
        RootPanel.get().addStyleName("has-reporting-filters-panel fitted-content");
        super.onAttach();
    }

    @Override
    protected void onDetach() {
        RootPanel.get().removeStyleName("has-reporting-filters-panel");
        super.onDetach();
    }

    public HTMLPanel getContentPanel() {
        return contentPanel;
    }
}