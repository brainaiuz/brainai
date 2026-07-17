package com.edatasite.workforce.gwt.materialkanban.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.OpportunityPercentageStageModal;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoseDropController extends VerticalPanelDropController {

    private final KanbanBoard kanbanBoard;


    public LoseDropController(VerticalPanel dropTarget, KanbanBoard kanbanBoard) {
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


        String statusCode = "CLOSED_LOST";
        Object layoutData = context.selectedWidgets.get(0).asWidget().getLayoutData();
        Double[] d = (Double[]) layoutData;
        Integer itemId = d[0].intValue();


        CRMService.App.get().takeReference(statusCode, new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(SelectItem selectItem) {

                new OpportunityPercentageStageModal(selectItem, itemId, widgetNewIndex, null, null);

            }
        });


        context.selectedWidgets.get(0).asWidget().removeFromParent();


    }
}




