package com.edatasite.workforce.gwt.materialkanban.client;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.VerticalPanelDropController;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by Anvar Akramov on 9/6/17.
 */
public class KanbanDropController<T> extends VerticalPanelDropController {

    private KanbanDataLoader kanbanDataLoader;
//    private Integer widgetIndex;
    private KanbanBoard kanbanBoard;
    //private KanbanVerticalPanel sourceColumnTotal;
    private boolean movestarted = false;

    public KanbanDropController(VerticalPanel dropTarget, KanbanDataLoader kanbanDataLoader, KanbanBoard kanbanBoard) {
        super(dropTarget);
        this.kanbanDataLoader = kanbanDataLoader;
        this.kanbanBoard = kanbanBoard;
    }

    @Override
    public void onDrop(DragContext context) {
        super.onDrop(context);
//        MaterialToast.fireToast("onDrop:");
        movestarted = false;
        Double amount = 0d;
        for (Widget selectedWidget : context.selectedWidgets) {

            KanbanVerticalPanel targetVerticalPanel = (KanbanVerticalPanel) dropTarget;
            Object targetColumn = targetVerticalPanel.getLayoutData();

            if (kanbanDataLoader != null && (!targetColumn.equals(kanbanBoard.getFromColumnMetadata())
                    || dropTarget.getWidgetIndex(selectedWidget) != kanbanBoard.getWidgetIndex())) {

                Object prevLayoutData = null;
                Object afterLayoutData = null;
                //get widget new index inside VerticalPanel
                Integer widgetNewIndex = dropTarget.getWidgetIndex(selectedWidget);
                //if widget is not first item in column then get previous item
                if (widgetNewIndex > 0) {
                    prevLayoutData = dropTarget.getWidget(widgetNewIndex - 1).getLayoutData();
                }
                //if there is next column after the item then get next item
                if (dropTarget.getWidgetCount() > (widgetNewIndex + 2)) {
                    afterLayoutData = dropTarget.getWidget(widgetNewIndex + 2).getLayoutData();
                }

                SelectItem fromColumnMetadata = null;
                if(kanbanBoard.getFromColumnMetadata()!=null) {
                    fromColumnMetadata = ((SelectItem) (kanbanBoard.getFromColumnMetadata())) ;
                }
                //save changes on serverside
                SelectItem finalFromMetadata = fromColumnMetadata;
                KanbanVerticalPanel fromColumn = kanbanBoard.getFromColumn();

                kanbanDataLoader.onDropKanbanItem(kanbanBoard.getFromColumnMetadata(), targetColumn, selectedWidget.getLayoutData(),
                        widgetNewIndex, prevLayoutData, afterLayoutData, kanbanBoard, () -> {
                            if (fromColumn != null && fromColumn.getWidgetCount() < kanbanBoard.getPageSize() + 1 && fromColumn.getTotalCount() > fromColumn.getWidgetCount() + 1) {
                                if (finalFromMetadata != null) {
                                    kanbanBoard.reloadColumn(finalFromMetadata.getId());
                                }
                            }
                        });

                //Update Total Count of columns
//                MaterialToast.fireToast("onDrop 1 :" + targetVerticalPanel.getTotalCount());
                targetVerticalPanel.addTotalCount(1);

                if (selectedWidget.getLayoutData() != null && selectedWidget.getLayoutData() instanceof Number[]) {
                    amount = kanbanDataLoader.getAmount((Number[]) selectedWidget.getLayoutData());
                    if (amount > 0) {
                        targetVerticalPanel.getDraggableColumn().setTotalAmount(targetVerticalPanel.getDraggableColumn().getTotalAmount() + amount);
                    }
                }

            } else {
                Widget parent = selectedWidget.getParent();
                if (parent != null && ((KanbanVerticalPanel) parent) != null) {
                    ((KanbanVerticalPanel) parent).addTotalCount(1);
//                    MaterialToast.fireToast("onDrop 2 :" + ((KanbanVerticalPanel) parent).getTotalCount());
                }
            }
        }
        if(kanbanBoard.getFromColumn()!=null) {
            kanbanBoard.getFromColumn().addTotalCount(-1);
            if(amount>0) {
                kanbanBoard.getFromColumn().getDraggableColumn().setTotalAmount(kanbanBoard.getFromColumn().getDraggableColumn().getTotalAmount()-amount);
//                Window.alert("" + kanbanBoard.getFromColumn().getDraggableColumn().getTotalAmount() );
            }
            kanbanBoard.setFromColumn(null);
        }
        kanbanBoard.setFromColumnMetadata(null);
    }

    /*private void addTotalCount(MaterialLabel targetColumnTotal, int n) {
        if (targetColumnTotal != null && !targetColumnTotal.getText().equals("")) {
            String total = targetColumnTotal.getText();
            //if total is inside breakets
            if(total.contains("(") && total.contains(")")) {
                total = total.substring(1, total.length() - 1);
                targetColumnTotal.setText("(" + (Integer.valueOf(total) + n) + ")");
            } else {
                targetColumnTotal.setText( (Integer.valueOf(total) + n) + "");
            }
        }
    }*/

    /*@Override
    public void onEnter(DragContext context) {

        MaterialToast.fireToast("OnEnter:");

        for (Widget widget : context.selectedWidgets) {
            Widget parent = widget.getParent();
            if (parent != null && parent.getLayoutData() != null && !movestarted) {
                movestarted = true;
                kanbanBoard.setFromColumnMetadata(parent.getLayoutData());
                kanbanBoard.setFromColumn(((KanbanVerticalPanel) parent));
//                sourceColumnTotal = ((KanbanVerticalPanel) parent);
                //Substruct 1 from column totalcount
//                MaterialToast.fireToast("onEnter :" + (sourceColumnTotal.getTotalCount()));

//                sourceColumnTotal.addTotalCount(-1);
                widgetIndex = ((InsertPanel) parent).getWidgetIndex(widget);
            } else {
//                MaterialToast.fireToast("movestarted:" + movestarted + " "+(parent!=null) + " " + (parent.getLayoutData()!=null) );
            }
        }

        super.onEnter(context);
    }*/

    /*@Override
    public void onLeave(DragContext context) {
        super.onLeave(context);

        for (Widget widget : context.selectedWidgets) {
            Widget parent = widget.getParent();
            if (parent != null && parent.getLayoutData() != null*//* && !movestarted*//*) {
//                movestarted = true;
//                kanbanBoard.setFromColumnMetadata(parent.getLayoutData());
                sourceColumnTotal = ((KanbanVerticalPanel) parent);
                //Substruct 1 from column totalcount
                MaterialToast.fireToast("onLeave :" + (sourceColumnTotal.getTotalCount()));

//                sourceColumnTotal.addTotalCount(-1);
//                widgetIndex = ((InsertPanel) parent).getWidgetIndex(widget);
            } else {
//                MaterialToast.fireToast("movestarted:" + movestarted + " "+(parent!=null) + " " + (parent.getLayoutData()!=null) );
            }
        }
    }*/

    /*@Override
    public void onLeave(DragContext context) {
        super.onLeave(context);
        for (Widget widget : context.selectedWidgets) {
            Widget parent = widget.getParent();
            if (parent != null && parent.getLayoutData() != null) {
                kanbanBoard.setFromColumnMetadata(parent.getLayoutData());
                sourceColumnTotal = ((KanbanVerticalPanel) parent);
                //Substruct 1 from column totalcount
                MaterialToast.fireToast("onLeave :" + (sourceColumnTotal.getTotalCount()));

                sourceColumnTotal.addTotalCount(-1);
                widgetIndex = ((InsertPanel) parent).getWidgetIndex(widget);
            }
        }
    }*/

    /*@Override
    public void onMove(DragContext context) {
        super.onMove(context);
        //        WidgetLocation dropTargetLocation = new WidgetLocation(dropTarget, null);
        context.dropController.getDropTarget().getWidget(0).getElement().scrollIntoView();
    }*/

    /*@Override
    protected Widget newPositioner(DragContext context) {
        return super.newPositioner(context);
    }*/
}
