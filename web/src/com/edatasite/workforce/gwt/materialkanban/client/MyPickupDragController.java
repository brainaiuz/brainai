package com.edatasite.workforce.gwt.materialkanban.client;

import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialToast;

/**
 * Created by Anvar Akramov on 4/16/18.
 */
public class MyPickupDragController extends PickupDragController {

    KanbanBoard kanbanBoard;

    public MyPickupDragController(AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel, KanbanBoard kanbanBoard) {
        super(boundaryPanel, allowDroppingOnBoundaryPanel);
        this.kanbanBoard = kanbanBoard;
    }

    /*public MyPickupDragController(AbsolutePanel boundaryPanel, boolean allowDroppingOnBoundaryPanel) {
        super(boundaryPanel, allowDroppingOnBoundaryPanel);
    }*/

    @Override
    public void dragMove() {

        context.selectedWidgets.get(0).getElement().scrollIntoView();
//        MaterialToast.fireToast(context.selectedWidgets.get(0).getElement().getId());
//        scrollSmoothly(context.selectedWidgets.get(0).getElement().getId());

        super.dragMove();
    }

    @Override
    public void dragStart() {


            RootPanel.getBodyElement().addClassName("has-dnd-drag");

        Widget parent = context.selectedWidgets.get(0).getParent();
        if (parent != null && parent.getLayoutData() != null) {

            kanbanBoard.setFromColumnMetadata(parent.getLayoutData());
            kanbanBoard.setFromColumn(((KanbanVerticalPanel) parent));
            kanbanBoard.setWidgetIndex( ((InsertPanel) parent).getWidgetIndex(context.selectedWidgets.get(0)) );
        } else {
            MaterialToast.fireToast((parent != null) + " " + (parent.getLayoutData() != null));
        }
        super.dragStart();
    }

    /*public static native void scrollSmoothly(String elementId) *//*-{
//        $wnd.jQuery('#'+elementId).scrollIntoView(250, "easeOutExpo");//
        $wnd.jQuery('#'+elementId).scrollintoview({ duration: 50, direction: "x" });//
//        $wnd.jQuery('#'+elementId).scrollanimate({}, "slow");
    }-*//*;*/

    @Override
    public void dragEnd() {
        RootPanel.getBodyElement().removeClassName("has-dnd-drag");
        super.dragEnd();
    }
}
