package com.edatasite.workforce.gwt.materialkanban.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

public class WonDropController extends VerticalPanelDropController {

    private final KanbanBoard kanbanBoard;


    public WonDropController(VerticalPanel dropTarget, KanbanBoard kanbanBoard) {

        super(dropTarget);
        this.kanbanBoard = kanbanBoard;
    }

    @Override
    public void onDrop(DragContext context) {
        super.onDrop(context);

        VerticalPanel targetVerticalPanel = (VerticalPanel) dropTarget;
        Object targetColumn = targetVerticalPanel.getLayoutData();
        Object prevLayoutData = null;
        Object afterLayoutData = null;

        //get widget new index inside VerticalPanel
        Integer widgetNewIndex = dropTarget.getWidgetIndex(context.selectedWidgets.get(0).asWidget());

        //if widget is not first item in column then get previous item
        if (widgetNewIndex > 0) {
            prevLayoutData = dropTarget.getWidget(widgetNewIndex - 1).getLayoutData();
        }

        //if there is next column after the item then get next item
        if (dropTarget.getWidgetCount() > (widgetNewIndex + 2)) {
            afterLayoutData = dropTarget.getWidget(widgetNewIndex + 2).getLayoutData();
        }

        String statusCode = "CLOSED_WON";
        toServer(statusCode, context.selectedWidgets.get(0).asWidget().getLayoutData(), widgetNewIndex);


        context.selectedWidgets.get(0).asWidget().removeFromParent();


    }


    private void toServer(String statusCode, Object layoutData1, int widgetIndex) {
        Double[] d = (Double[]) layoutData1;
        Integer itemId = d[0].intValue();

        CRMService.App.get().changeOpportunityKanbanOrder(statusCode, itemId, widgetIndex, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Integer integer) {


            }
        });
    }

}
