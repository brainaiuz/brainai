package com.edatasite.workforce.gwt.core.client.ui.components.dashboard;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractInsertPanelDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.DragClientBundle;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.google.gwt.user.client.ui.*;

public class DashboardDropController extends AbstractInsertPanelDropController {

    /**
     * @param dropTarget the insert panel drop target
     * @see FlowPanelDropController#FlowPanelDropController(FlowPanel)
     */
    public DashboardDropController(FlowPanel dropTarget) {
        super(dropTarget);
    }

    @Override
    protected LocationWidgetComparator getLocationWidgetComparator() {
        return DOMUtil.isRtl((Panel)dropTarget) ? LocationWidgetComparator.BOTTOM_LEFT_COMPARATOR
                : LocationWidgetComparator.BOTTOM_RIGHT_COMPARATOR;
    }

    @Override
    protected Widget newPositioner(DragContext context) {
        // Use two widgets so that setPixelSize() consistently affects dimensions
        // excluding positioner border in quirks and strict modes
        SimplePanel outer = new SimplePanel();
        outer.addStyleName(DragClientBundle.INSTANCE.css().positioner());

        // place off screen for border calculation
        RootPanel.get().add(outer, -500, -500);

        // Ensure IE quirks mode returns valid outer.offsetHeight, and thus valid
        // DOMUtil.getVerticalBorders(outer)
        outer.setWidget(new Label("x"));

        int width = 0;
        int height = 0;
        for (Widget widget : context.selectedWidgets) {
            width = Math.max(width, widget.getOffsetWidth());
            height = Math.max(height, widget.getOffsetHeight());
        }

        SimplePanel inner = new SimplePanel();
        inner.setPixelSize(width - DOMUtil.getHorizontalBorders(outer), height
                - DOMUtil.getVerticalBorders(outer));

        outer.setWidget(inner);

        return outer;
    }
}
