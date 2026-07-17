package com.edatasite.workforce.gwt.core.client.ui;


import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Jan 8, 2008 Time: 5:10:33 PM To
 * change this template use File | Settings | File Templates.
 */

public abstract class QuickViewPanel extends FlowPanel {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private QuickViewStrategy helper;
    private List links = new ArrayList();
    private ListingPanel listingPanel;

    public QuickViewPanel() {
        hidePreview();
    }

    protected void hidePreview() {
        clear();
        clearLinks();
        DockPanel dockPanel = new DockPanel();
        dockPanel.setSize("100%", "100%");
        dockPanel.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
        dockPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        Label label = new Label(wfmStrings.selectItemFromListAbove());
        dockPanel.add(label, DockPanel.CENTER);
        add(dockPanel);
        setSize("100%", "100%");
        /*add(new Label("No preview available"));*/
//        layout(true);
    }

    public void preview(Object o) {
        if (o == null) {
            hidePreview();
        }
        if (!doPreview(o)) {
            hidePreview();
        }
        if (getHelper() != null) {
            getHelper().drawLinks(getLinks());
        }
    }

    private void drawLinks(boolean force) {
        if (getHelper() != null) {
            getHelper().drawLinks(getLinks());
        }
    }


    protected abstract boolean doPreview(Object o);

    public void setLinksSetter(QuickViewStrategy helper) {
        this.setHelper(helper);
    }

    public List getLinks() {
        return links;
    }

    public void setLinks(List links) {
        this.links.clear();
        this.links = links;
    }

    public void setLinks(List links, boolean force) {
        this.links.clear();
        this.links = links;
        drawLinks(force);

    }

    public void setHelper(QuickViewStrategy helper) {
        this.helper = helper;
    }

    public QuickViewStrategy getHelper() {
        return helper;
    }

    public ListingPanel getParentListPanel() {
        return listingPanel;
    }

    public void setParentListPanel(ListingPanel parentListPanel) {
        this.listingPanel = parentListPanel;
    }

    public void clearLinks() {
        getLinks().clear();
    }

}
