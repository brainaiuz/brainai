package com.edatasite.workforce.gwt.ganttchart.client.treetable;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.ganttchart.client.Constants;
import com.edatasite.workforce.gwt.ganttchart.client.GanttChart;
import com.edatasite.workforce.gwt.ganttchart.client.TaskWidget;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.KeyboardListenerCollection;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 16, 2011
 * Time: 10:24:39 AM
 * To change this template use File | Settings | File Templates.
 */

public class GanttChartTree extends FlexTable {

    private GanttChart ganttChart;
    private GanttTreeItem curSelection;
    private String imageBase = GWT.getModuleBaseURL();
    private KeyboardListenerCollection keyboardListeners;
    private GanttChartTreeListenerCollection listeners;
    private MouseListenerCollection mouseListeners = null;
    public final GanttTreeItem root;
    private GanttChartTreeRenderer renderer;
    private int lastEventType;

    public class Renderer {
        public void renderRow(GanttChartTree tree, GanttTreeItem item, int row) {

        }
    }

    public GanttChartTree(GanttChart ganttChart) {
        this.ganttChart = ganttChart;
        sinkEvents(Event.MOUSEEVENTS | Event.KEYEVENTS);

        root = new GanttTreeItem() {
            public void addItem(GanttTreeItem item) {
                // If this element already belongs to a tree or tree item, remove it.
                if ((item.getParentItem() != null) || (item.getTreeTable() != null)) {
                    item.remove();
                }
                item.setTreeTable(this.getTreeTable());

                // Explicitly set top-level items' parents to null.
                item.setParentItem(null);
                getChildren().add(item);
            }

            public void removeItem(GanttTreeItem item) {
                if (!getChildren().contains(item)) {
                    return;
                }
                // Update Item state.
                item.setTreeTable(null);
                item.setParentItem(null);
                getChildren().remove(item);
            }
        };
        root.setTreeTable(this);
    }

    public void addItem(GanttTreeItem item) {
        root.addItem(item);

        // Adds the item to the proper row
        insertItem(item, getRowCount());
        updateRowCache();
        updateVisibility(item);
    }

    public void insertItem(GanttTreeItem item, int r) {
        // inserts this item into the tree
        insertRow(r);
        setWidget(r, getTreeColumn(), item);
        item.setRow(r);
        render(item);
        Vector chlds = item.getChildren();
        for (Object chld1 : chlds) {
            GanttTreeItem chld = (GanttTreeItem) chld1;
            insertItem(chld, r + 1);
        }

        GanttTreeItem p = item.getParentItem();
        if (p != null) {
            if (!p.isOpen()) {
                setVisible(false, item.getRow());
                setChildrenVisible(item, false);
            }
        }
    }

    public void removeItem(GanttTreeItem item) {
        root.removeItem(item);
        removeItemFromTable(item);
    }

    void removeItemFromTable(GanttTreeItem item) {
        int r = item.getRow();
        int rs = item.getDescendentCount();
        for (int i = 0; i < rs; i++) {
            removeRow(r);
        }
        updateRowCache();
    }

    public void removeItems() {
        while (getItemCount() > 0) {
            removeItem(getItem(0));
        }
    }

    public void updateRowCache() {
        updateRowCache(root, -1);
    }

    int updateRowCache(GanttTreeItem item, int r) {
        item.setRow(r);

        Vector chlds = item.getChildren();
        for (Object chld1 : chlds) {
            GanttTreeItem chld = (GanttTreeItem) chld1;
            r++;
            r = updateRowCache(chld, r);
        }

        return r;
    }

    protected int getTreeColumn() {
        return 0;
    }

    public void addKeyboardListener(KeyboardListener listener) {
        if (keyboardListeners == null) {
            keyboardListeners = new KeyboardListenerCollection();
        }
        keyboardListeners.add(listener);
    }

    public void addMouseListener(MouseListener listener) {
        if (mouseListeners == null) {
            mouseListeners = new MouseListenerCollection();
        }
        mouseListeners.add(listener);
    }

    public void addTreeTableListener(GanttChartTreeListener listener) {
        if (listeners == null) {
            listeners = new GanttChartTreeListenerCollection();
        }
        listeners.add(listener);
    }

    /**
     * Clears all tree items from the current tree.
     */
    public void clear() {
        int size = root.getChildCount();
        for (int i = size - 1; i >= 0; i--) {
            root.getChild(i).remove();
        }
    }

    /**
     * Ensures that the currently-selected item is visible, opening its parents
     * and scrolling the tree as necessary.
     */
    public void ensureSelectedItemVisible() {
        if (curSelection == null) {
            return;
        }

        GanttTreeItem parent = curSelection.getParentItem();
        while (parent != null) {
            parent.setState(true);
            parent = parent.getParentItem();
        }
    }

    public String getImageBase() {
        return imageBase;
    }

    public GanttTreeItem getItem(int index) {
        return root.getChild(index);
    }

    public int getItemCount() {
        return root.getChildCount();
    }

    public GanttTreeItem getSelectedItem() {
        return curSelection;
    }

    public void onBrowserEvent(Event event) {
        int eventType = DOM.eventGetType(event);
        switch (eventType) {
            case Event.ONMOUSEDOWN: {
                if (mouseListeners != null) {
                    mouseListeners.fireMouseEvent(this, event);
                }
                elementClicked(root, DOM.eventGetTarget(event));
                break;
            }

            case Event.ONMOUSEUP: {
                if (mouseListeners != null) {
                    mouseListeners.fireMouseEvent(this, event);
                }
                break;
            }

            case Event.ONMOUSEMOVE: {
                if (mouseListeners != null) {
                    mouseListeners.fireMouseEvent(this, event);
                }
                break;
            }

            case Event.ONMOUSEOVER: {
                if (mouseListeners != null) {
                    mouseListeners.fireMouseEvent(this, event);
                }
                break;
            }

            case Event.ONMOUSEOUT: {
                if (mouseListeners != null) {
                    mouseListeners.fireMouseEvent(this, event);
                }
                break;
            }

            case Event.ONKEYDOWN:
                // If nothing's selected, select the first item.
                if (curSelection == null) {
                    if (root.getChildCount() > 0) {
                        onSelection(root.getChild(0), true);
                    }
                    super.onBrowserEvent(event);
                    return;
                }

                if (lastEventType == Event.ONKEYDOWN) {
                    return;
                }

                // Handle keyboard events
                switch (DOM.eventGetKeyCode(event)) {
                    case KeyboardListener.KEY_UP: {
                        moveSelectionUp(curSelection);
                        DOM.eventPreventDefault(event);
                        break;
                    }
                    case KeyboardListener.KEY_DOWN: {
                        moveSelectionDown(curSelection, true);
                        DOM.eventPreventDefault(event);
                        break;
                    }
                    case KeyboardListener.KEY_LEFT: {
                        if (curSelection.getState()) {
                            curSelection.setState(false);
                        }
                        DOM.eventPreventDefault(event);
                        break;
                    }
                    case KeyboardListener.KEY_RIGHT: {
                        if (!curSelection.getState()) {
                            curSelection.setState(true);
                        }
                        DOM.eventPreventDefault(event);
                        break;
                    }
                }

                // Intentional fallthrough.
            case Event.ONKEYUP:
                if (eventType == Event.ONKEYUP) {
                    // If we got here because of a key tab, then we need to make
                    // sure the
                    // current tree item is selected.
                    if (DOM.eventGetKeyCode(event) == KeyboardListener.KEY_TAB) {
                        Vector chain = new Vector();
                        collectElementChain(chain, getElement(), DOM.eventGetTarget(event));
                        GanttTreeItem item = findItemByChain(chain, 0, root);
                        if (item != getSelectedItem()) {
                            setSelectedItem(item, true);
                        }
                    }
                }

                // Intentional fallthrough.
            case Event.ONKEYPRESS: {
                if (keyboardListeners != null) {
                    keyboardListeners.fireKeyboardEvent(this, event);
                }
                break;
            }
        }

        // We must call SynthesizedWidget's implementation for all other events.
        super.onBrowserEvent(event);
        lastEventType = eventType;
    }

    public void removeKeyboardListener(KeyboardListener listener) {
        if (keyboardListeners != null) {
            keyboardListeners.remove(listener);
        }
    }

    public void removeTreeTableListener(GanttChartTreeListener listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public void setImageBase(String baseUrl) {
        imageBase = baseUrl;
        root.updateStateRecursive();
    }

    public void setSelectedItem(GanttTreeItem item) {
        setSelectedItem(item, true);
    }

    public void setSelectedItem(GanttTreeItem item, boolean fireEvents) {
        if (item == null) {
            if (curSelection == null) {
                return;
            }
            curSelection.setSelected(false);
            curSelection = null;
            return;
        }

        onSelection(item, fireEvents);
    }

    public Iterator treeItemIterator() {
        List accum = new ArrayList();
        root.addTreeItems(accum);
        return accum.iterator();
    }

    protected void onLoad() {
        root.updateStateRecursive();

        renderTable();
        updateVisibility();
    }

    void fireStateChanged(GanttTreeItem item) {
        if (listeners != null) {
            listeners.fireItemStateChanged(item);
        }
    }

    /**
     * Collects parents going up the element tree, terminated at the tree root.
     */
    private void collectElementChain(Vector chain, Element hRoot, Element hElem) {
        if ((hElem == null) || DOM.compare(hElem, hRoot)) {
            return;
        }

        collectElementChain(chain, hRoot, DOM.getParent(hElem));
        chain.add(hElem);
    }

    public boolean elementClicked(GanttTreeItem root, Element hElem) {
        Vector chain = new Vector();
        collectElementChain(chain, getElement(), hElem);

        GanttTreeItem item = findItemByChain(chain, 0, root);
        if (item != null) {
            if (DOM.compare(item.getImageElement(), hElem)) {
                LoadingPanel.loading(true);
                Integer id = ((TaskSingleItem) item.getUserObject()).getObjectID();
                HashMap<Integer, ArrayList<TaskSingleItem>> workstreams = ganttChart.workstreamTasks;
                ArrayList<TaskSingleItem> taskItems = workstreams.get(id);
                if (taskItems != null && !taskItems.isEmpty()) {
                    for (TaskSingleItem taskItem : taskItems) {
                        if (!item.isOpen()) {
                            GanttTreeItem nextTreeItem = ganttChart.taskItems.get(taskItem.isWorkstream() ? "w" + taskItem.getObjectID() : "t" + taskItem.getObjectID());
                            recursivelyAttachTaskWidgets(taskItem, nextTreeItem);
                        } else {
                            recursivelyRemoveTaskWidgets(taskItem);
                        }
                    }
                }
                moveTasksAfterThisTask(item, item.getTreeTable());

                item.setState(!item.getState(), true);
                ArrayList<TaskSingleItem> tasks = new ArrayList<>();
                for (TaskWidget widget : ganttChart.taskWidgets.values()) {
                    if (widget.getTask().getPredecessorTasks() != null && widget.getTask().getPredecessorTasks().length > 0) {
                        tasks.add(widget.getTask());
                    }
                }
                ganttChart.reDrawDependencies(tasks);
                LoadingPanel.loading(false);
                return true;
            } else if (DOM.isOrHasChild(item.getElement(), hElem)) {
                onSelection(item, true);
                LoadingPanel.loading(false);
                return true;
            }
            LoadingPanel.loading(false);
        }

        return false;
    }

    private void moveTasksAfterThisTask(GanttTreeItem item, GanttChartTree treeTable) {
        moveTasksAfterThisTask(item, treeTable, false);
    }

    private void moveTasksAfterThisTask(GanttTreeItem item, GanttChartTree treeTable, boolean forRemove) {
        // shu indexdan keyingi widgetlarni surish
        int childCount = item.getDescendentCount1();
        int height = childCount * (TaskWidget.cellSize + 1);
        if (item.isOpen()) {
            height = (-1) * height;
        }
        GanttTreeItem nextNonChild = this.getNextNonChild(item);
        while (nextNonChild != null) {
            TaskSingleItem taskItem = (TaskSingleItem) nextNonChild.getUserObject();
            int treeTableTop = this.getAbsoluteTop() + 1;
            if (!"".equals(nextNonChild.getUserObject())) {
                expandCollapseChilds(nextNonChild, height);
            }
            String prefix = taskItem.isWorkstream() ? "w_" : "t_";
            TaskWidget taskWidget = ganttChart.taskWidgets.get(prefix + taskItem.getObjectID());
            if (taskWidget != null) {
                taskWidget.setTop(taskWidget.getTop() + (forRemove ? (-1) * Math.abs(height) : height));
                ganttChart.taskWidgets.remove(prefix + taskItem.getObjectID());
                ganttChart.taskWidgets.put(prefix + taskItem.getObjectID(), taskWidget);
            }
            nextNonChild = this.getNextNonChild(nextNonChild);
        }
    }

    private void recursivelyAttachTaskWidgets(TaskSingleItem taskItem, GanttTreeItem item) {
        String prefix = taskItem.isWorkstream() ? "w_" : "t_";
        int top = ganttChart.taskWidgets.get(prefix + taskItem.getObjectID()).getTop();
        int index = (top / TaskWidget.cellSize);
        TaskWidget rectangle = ganttChart.attachTaskWidget(index, taskItem);
        ganttChart.taskWidgets.remove(prefix + taskItem.getObjectID());
        ganttChart.taskWidgets.put(prefix + taskItem.getObjectID(), rectangle);
        if (item != null && item.isOpen()) {
            ArrayList<TaskSingleItem> wsTasks = ganttChart.workstreamTasks.get(taskItem.getObjectID());
            if (wsTasks != null && !wsTasks.isEmpty()) {
                for (TaskSingleItem item1 : wsTasks) {
                    GanttTreeItem treeItem = ganttChart.taskItems.get(taskItem.isWorkstream() ? "w" + item1.getObjectID() : "t" + item1.getObjectID());
                    recursivelyAttachTaskWidgets(item1, treeItem);
                }
            }
        }
    }

    private void recursivelyRemoveTaskWidgets(TaskSingleItem taskItem) {
        HashMap<String, TaskWidget> taskWidgets = ganttChart.taskWidgets;
        String prefix = taskItem.isWorkstream() ? "w_" : "t_";
        if (taskWidgets.containsKey(prefix + taskItem.getObjectID())) {
            Element elementById = DOM.getElementById("t_" + taskItem.getObjectID().toString());
            if (elementById != null) {
                elementById.removeFromParent();
            }
            ArrayList<TaskSingleItem> workstreamTasks = ganttChart.workstreamTasks.get(taskItem.getObjectID());
            if (workstreamTasks != null && !workstreamTasks.isEmpty()) {
                for (TaskSingleItem item : workstreamTasks) {
                    recursivelyRemoveTaskWidgets(item);
                }
            }
        }
    }

    private void expandCollapseChilds(GanttTreeItem nextNonChild, int difference) {
        Vector childCount = nextNonChild.getChildren();
        if (childCount != null && !childCount.isEmpty()) {
            for (GanttTreeItem item : (GanttTreeItem[]) childCount.toArray(new GanttTreeItem[]{})) {
                TaskSingleItem taskItem = (TaskSingleItem) item.getUserObject();
                String prefix = taskItem.isWorkstream() ? "w_" : "t_";
                TaskWidget taskWidget = ganttChart.taskWidgets.get(taskItem.getObjectID());
                Element userObject = DOM.getElementById(prefix + taskItem.getObjectID().toString());
                if (taskWidget != null) {
                    int absoluteTop = taskWidget.getTop() + difference;
                    taskWidget.setTop(absoluteTop);
                    ganttChart.taskWidgets.remove(prefix + taskItem.getObjectID());
                    ganttChart.taskWidgets.put(prefix + taskItem.getObjectID(), taskWidget);
                }
                if (item.getChildCount() > 0) {
                    expandCollapseChilds(item, difference);
                }
            }
        }
    }

    private GanttTreeItem findDeepestOpenChild(GanttTreeItem item) {
        if (!item.getState()) {
            return item;
        }
        return findDeepestOpenChild(item.getChild(item.getChildCount() - 1));
    }

    private GanttTreeItem findItemByChain(Vector chain, int idx, GanttTreeItem root) {
        if (idx == chain.size()) {
            return root;
        }

        for (Object aChain : chain) {
            Element elem = (Element) aChain;
            String n = getNodeName(elem);
            if ("div".equalsIgnoreCase(n)) {
                return findItemByElement(root, elem);
            }
        }

        return null;
    }

    private GanttTreeItem findItemByElement(GanttTreeItem item, Element elem) {
        if (DOM.compare(item.getElement(), elem)) {
            return item;
        }
        for (int i = 0, n = item.getChildCount(); i < n; ++i) {
            GanttTreeItem child = item.getChild(i);
            child = findItemByElement(child, elem);
            if (child != null) {
                return child;
            }
        }
        return null;
    }

    private native String getNodeName(Element elem) /*-{
        return elem.nodeName;
    }-*/;

    public void moveSelectionDown(GanttTreeItem sel, boolean dig) {
        if (sel == root) {
            return;
        }
        GanttTreeItem parent = sel.getParentItem();
        if (parent == null) {
            parent = root;
        }
        int idx = parent.getChildIndex(sel);

        if (!dig || !sel.getState()) {
            if (idx < parent.getChildCount() + 1) {
                ganttChart.ganttChartTree.removeItem(sel);
                ganttChart.ganttChartTree.insertItem(sel, idx + 2);
                onSelection(sel, true);
                root.getChildren().add(idx + 1, sel);
                updateRowCache();
                updateVisibility(sel);
            }
        } else if (sel.getChildCount() > 0) {
            onSelection(sel.getChild(0), true);
        }
    }

    public void moveSelectionUp(GanttTreeItem sel) {
        GanttTreeItem parent = sel.getParentItem();
        if (parent == null) {
            parent = root;
        }
        int idx = parent.getChildIndex(sel);

        if (idx > 0) {
            GanttTreeItem sibling = parent.getChild(idx - 1);
            onSelection(findDeepestOpenChild(sibling), true);
            if (idx > 1) {
                ganttChart.ganttChartTree.removeItem(sel);
                ganttChart.ganttChartTree.insertItem(sel, idx);
                root.getChildren().add(idx - 1, sel);
                updateRowCache();
                updateVisibility(sel);
            }
        } else {
            onSelection(parent, true);
        }
    }

    public void shiftToRight(GanttTreeItem treeItem) {
        if (treeItem.getRow() > 1) {
            GanttTreeItem parent = ganttChart.ganttChartTree.getPreviousNonChild(treeItem);
            if (parent != null) {
                ganttChart.ganttChartTree.root.getChildren().remove(treeItem);
                parent.addItem(treeItem, true);
                SelectItem[] predecessorTasks = ((TaskSingleItem) treeItem.getUserObject()).getPredecessorTasks();
                ArrayList<SelectItem> preds = new ArrayList<>();
                Collections.addAll(preds, predecessorTasks);
                TaskSingleItem item = (TaskSingleItem) parent.getUserObject();
                preds.add(new SelectItem(item.getObjectID(), item.getName()));
                ((TaskSingleItem) treeItem.getUserObject()).setPredecessorTasks(preds.toArray(new SelectItem[]{}));
                parent.setState(true, true);
            } else ganttChart.ganttChartTree.root.addItem(treeItem);
            ArrayList<TaskSingleItem> items = new ArrayList<>();
            if (!ganttChart.workstreamTasks.containsKey(((TaskSingleItem) treeItem.getParentItem().getUserObject()).getObjectID())) {
                items.add((TaskSingleItem) treeItem.getUserObject());
            } else {
                items = ganttChart.workstreamTasks.get(((TaskSingleItem) treeItem.getParentItem().getUserObject()).getObjectID());
                items.add((TaskSingleItem) treeItem.getUserObject());
            }
            ganttChart.workstreamTasks.put(((TaskSingleItem) treeItem.getParentItem().getUserObject()).getObjectID(), items);
        }
    }

    public void shiftToLeft(GanttTreeItem treeItem) {
        if (treeItem.getRow() > 1) {
            if (treeItem.isOpen()) {
                treeItem.setState(false, true);
            }
            Integer row = treeItem.getRow();
            int d = treeItem.getDepth() - 1;
            GanttTreeItem parent = treeItem.getParentItem();
            treeItem.remove();
            ArrayList<SelectItem> preds = new ArrayList<>();
            Collections.addAll(preds, ((TaskSingleItem) treeItem.getUserObject()).getPredecessorTasks());
            preds.remove(((TaskSingleItem) parent.getUserObject()).getObjectID());
            ((TaskSingleItem) treeItem.getUserObject()).setPredecessorTasks(preds.toArray(new SelectItem[]{}));
            if (parent.getParentItem() != null) {
                parent.getParentItem().addItem(treeItem);
            } else {
                parent.getTreeTable().insertItem(treeItem, parent.getTreeTable().getNextNonChild(parent).getRow());
                parent.getTreeTable().root.getChildren().add(parent.getRow(), treeItem);
            }
            if (d >= 0) {
                DOM.setStyleAttribute(treeItem.getElement(), "marginLeft", (d * 16) + "px");
            }
            parent.getTreeTable().updateRowCache();
            parent.getTreeTable().updateVisibility(treeItem);
            if (treeItem.getChildCount() == 1) {
                treeItem.updateState();
            }
        }
    }

    public void deleteTask(GanttTreeItem treeItem) {
        /*recursivelyRemoveTaskWidgets((TaskSingleItem)treeItem.getUserObject());
        ArrayList<TaskSingleItem> taskItems = ganttChart.workstreamTasks.get(((TaskSingleItem)treeItem.getUserObject()).getObjectID());
        GanttChartTree treeTable = treeItem.getTreeTable();
        moveTasksAfterThisTask(treeItem, treeTable, true);
        if(taskItems != null && !taskItems.isEmpty()) {
            for (TaskSingleItem item : taskItems) {
                recursivelyRemoveTaskWidgets(item);
            }
        }*/
        treeItem.remove();
        TaskSingleItem item = (TaskSingleItem) treeItem.getUserObject();
        if (item.isWorkstream()) {
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKSTREAM_DELETED, null, GanttChartTree.this);
        } else {
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TASK_DELETE, null, GanttChartTree.this);
        }
    }

    private void onSelection(GanttTreeItem item, boolean fireEvents) {
        if (item == root) {
            return;
        }

        if (curSelection != null) {
            curSelection.setSelected(false);
            ganttChart.ganttChartTree.getRowFormatter().removeStyleName(curSelection.getRow(), "selectedRowStyle");
        }

        curSelection = item;

        if (curSelection != null) {
            curSelection.setSelected(true);
            if (curSelection.getRow() <= root.getChildCount()) {
                ganttChart.ganttChartTree.getRowFormatter().setStyleName(curSelection.getRow(), "selectedRowStyle");
            }
            if (fireEvents && (listeners != null)) {
                listeners.fireItemSelected(curSelection);
            }
        }
    }

    private native boolean shouldTreeDelegateFocusToElement(Element elem) /*-{
        var focus = ((elem.nodeName == "SELECT") || (elem.nodeName == "INPUT") || (elem.nodeName == "CHECKBOX"));
        return focus;
    }-*/;

    public void updateVisibility() {
        for (int i = 0, s = root.getChildCount(); i < s; i++) {
            GanttTreeItem item = root.getChild(i);
            updateVisibility(item);
        }
    }

    public void updateVisibility(GanttTreeItem item) {
        if (item.isOpen()) {
            showChildren(item);
        } else {
            hideChildren(item);
        }
    }

    void setVisible(boolean visible, int row) {
        UIObject.setVisible(getRowFormatter().getElement(row), visible);
    }

    protected void setVisible(boolean visible, int row, int count) {
        for (int r = row, s = row + count; r < s; r++) {
            setVisible(visible, r);
        }
    }

    public void showChildren(GanttTreeItem item) {
        for (int i = 0, s = item.getChildCount(); i < s; i++) {
            GanttTreeItem child = item.getChild(i);
            setVisible(true, child.getRow());

            if (child.isOpen()) {
                showChildren(child);
            }
        }
    }

    public void hideChildren(GanttTreeItem item) {
        setChildrenVisible(item, false);
    }


    public void setChildrenVisible(GanttTreeItem item, boolean visible) {
        if (item.getChildCount() == 0) {
            return;
        }
        int row = item.getRow() + 1;
        int lastChildRow = getLastChildRow(item);
        int count = lastChildRow - row + 1;
        setVisible(visible, row, count);
    }

    protected GanttTreeItem getNextSibling(GanttTreeItem item) {
        GanttTreeItem p = item.getParentItem();
        if (p == null) {
            int idx = root.getChildIndex(item) + 1;
            if (idx < root.getChildCount()) {
                // Gets the next sibling
                return root.getChild(idx);
            }
        } else {
            int idx = p.getChildIndex(item) + 1;
            if (idx < p.getChildCount()) {
                // Gets the next sibling
                return p.getChild(idx);
            }
        }
        return null;
    }

    protected GanttTreeItem getPreviousSibling(GanttTreeItem item) {
        GanttTreeItem p = item.getParentItem();
        if (p == null) {
            int idx = root.getChildIndex(item) - 1;
            if (idx >= 0) {
                // Gets the previous sibling
                return root.getChild(idx);
            }
        } else {
            int idx = p.getChildIndex(item) - 1;
            if (idx >= 0) {
                // Gets the previous sibling
                return p.getChild(idx);
            }
        }
        return null;
    }

    public GanttTreeItem getNextNonChild(GanttTreeItem item) {
        GanttTreeItem next = getNextSibling(item);
        if (next != null) {
            return next;
        }
        GanttTreeItem p = item.getParentItem();
        if (p != null) {
            return getNextNonChild(p);
        } else {
            return null;
        }
    }

    public GanttTreeItem getPreviousNonChild(GanttTreeItem item) {
        GanttTreeItem previousSibling = getPreviousSibling(item);
        if (previousSibling != null) {
            return previousSibling;
        }
        GanttTreeItem p = item.getParentItem();
        if (p != null) {
            return getPreviousNonChild(p);
        } else {
            return null;
        }
    }

    public int getLastChildRow(GanttTreeItem item) {
        GanttTreeItem next = getNextNonChild(item);
        if (next != null) {
            return next.getRow() - 1;
        }

        return getRowCount() - 1;
    }

    public void renderTable() {
        render(root);
    }

    /**
     * Renders TreeItems recursively.
     *
     * @param item
     */
    public void render(GanttTreeItem item) {
        getRenderer().renderTreeItem(this, item, item.getRow());
        if (item.getParentItem() != null) {
            updateVisibility(item.getParentItem());
        }

        for (int i = 0, s = item.getChildCount(); i < s; i++) {
            GanttTreeItem child = item.getChild(i);
            render(child);
        }
    }

    public GanttChartTreeRenderer getRenderer() {
        if (renderer == null) {
            renderer = new DefaultRendererGanttChart();
        }
        return renderer;
    }

    public void setRenderer(GanttChartTreeRenderer renderer) {
        this.renderer = renderer;
    }

    class DefaultRendererGanttChart implements GanttChartTreeRenderer {
        public void renderTreeItem(GanttChartTree table, GanttTreeItem item, int row) {
            Object obj = item.getUserObject();
            if (obj instanceof Widget) {
                item.setWidget((Widget) obj);
            } else if (obj instanceof TaskSingleItem) {
                TaskSingleItem task = (TaskSingleItem) obj;
                if (task != null) {
                    if (ganttChart.isHasEditAccess()) {
                        String action = "task|summary/" + task.getObjectID().toString();
                        Anchor summaryLink = new Anchor(task.getName(), true);
                        summaryLink.setTitle(task.getName());
                        if (task.isWorkstream()) {
                            action = "workstream|summary/" + task.getObjectID().toString();
                            summaryLink = new Anchor(task.getName());
                            summaryLink.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
                            summaryLink.setTitle(task.getName());
                        }
                        final String finalAction = action;
                        summaryLink.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged(finalAction));
                        item.setWidget(summaryLink);
//						DOM.setAttribute(item.getImageElement(), "src", item.images.treeClosed().getURL());
                    } else {
                        item.setHTML(task.getName());
                    }

                    FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
                    formatter.setWidth(row, 0, "15%");
                    setStyleToTreeCell(formatter, row, 0);
                    formatter.getElement(row, 0).getStyle().setProperty("maxWidth", "170px");
                    formatter.getElement(row, 0).getStyle().setProperty("minWidth", "170px");

                    int index = 1;
                    if (ganttChart.getColumnNames() == null || ganttChart.getColumnNames().isEmpty()) {
                        ganttChart.setColumnNames("");
                    }
                    String[] hiddenColumns = ganttChart.getColumnNames().split(",");
                    for (String column : hiddenColumns) {
                        column = column.replace(" ", "");
                        if (TaskListItem.START_DATE.equals(column)) {
                            index = drawColumn(table, row, task, DateUtils.format(task.getStartDate()), formatter, index, "8%", "80px");
                        } else if (TaskListItem.END_DATE.equals(column)) {
                            index = drawColumn(table, row, task, DateUtils.format(task.getEndDate()), formatter, index, "8%", "80px");
                        } else if (TaskListItem.COMPLETE.equals(column)) {
                            Float percent = Float.valueOf("0.00");
                            if (task.getPercent() != null) {
                                percent = task.getPercent();
                            }
                            index = drawColumn(table, row, task, Constants.defaultNumberFormat.format(percent) + "%", formatter, index, "4%", "45px");
                        } else if (TaskListItem.ASSIGNED_TO.equals(column)) {
                            index = drawColumn(table, row, task, getAssignToNames(task.getInvolvedMembers()), formatter, index, "10%", "130px");
                        } else if (TaskListItem.OVERALL_STATUS_NAME.equals(column)) {
                            index = drawColumn(table, row, task, task.getStatusName(), formatter, index, "8%", "90px");
                        } else if (TaskListItem.PRIORITY_NAME.equals(column)) {
                            index = drawColumn(table, row, task, task.getPriorityName(), formatter, index, "6%", "70px");
                        } else if (TaskListItem.ESTIMATED.equals(column)) {
                            index = drawColumn(table, row, task, Utils.formatMinutes(task.getEstimatedTime()), formatter, index, "6%", "60px");
                        } else if (TaskListItem.ACTUAL_TIME.equals(column)) {
                            index = drawColumn(table, row, task, Utils.formatMinutes(task.getActualTime()), formatter, index, "7%", "60px");
                        } else if (TaskListItem.ACTUAL_START_DATE.equals(column)) {
                            index = drawColumn(table, row, task, DateUtils.format(task.getActualStartDate()), formatter, index, "7%", "70px");
                        } else if (TaskListItem.ACTUAL_END_DATE.equals(column)) {
                            index = drawColumn(table, row, task, DateUtils.format(task.getActualEndDate()), formatter, index, "7%", "70px");
                        } else if (TaskListItem.BILLABLE.equals(column)) {
                            index = drawColumn(table, row, task, !task.isWorkstream() ? (task.getBillable() ? ganttChart.wfmStrings.yes() : ganttChart.wfmStrings.no()) : "", formatter, index, "4%", "45px");
                        }
                    }
                }
            }
        }

        private int drawColumn(GanttChartTree table, int row, TaskSingleItem task, String text, FlexCellFormatter formatter, int index, String width, String maxMinWidth) {
            Label label = new Label(text);
            label.getElement().setId("r" + row + "c" + index + "_" + task.getObjectID());
            table.setWidget(row, index, label);
            formatter.setWidth(row, index, width);
            setStyleToTreeCell(formatter, row, index);
            formatter.getElement(row, index).getStyle().setProperty("maxWidth", maxMinWidth);
            formatter.getElement(row, index).getStyle().setProperty("minWidth", maxMinWidth);
            formatter.setHorizontalAlignment(row, index++, HasHorizontalAlignment.ALIGN_CENTER);
            return index;
        }
    }

    private String getAssignToNames(TaskInvolvedMember[] assignedTo) {
        if (assignedTo != null) {
            StringBuilder names = new StringBuilder();
            if (assignedTo.length > 0) {
                for (TaskInvolvedMember employeeTask : assignedTo) {
                    if (!names.toString().equals("")) {
                        names.append(",");
                    }
                    names.append(employeeTask.getEmployee());
                }
                return names.toString();
            } else {
                return "";
            }
        }
        return "";
    }

    private String getBold(String s) {
        return "<b>" + s + "</b>";
    }

    private void setStyleToTreeCell(FlexTable.FlexCellFormatter formatter, int row, int col) {
        formatter.setStyleName(row, col, "leftPanelRows");
//        formatter.getElement(row, col).getStyle().setFontSize(15d, Style.Unit.PX);
    }

    public void setWidget(int row, int column, Widget widget) {
        if (column != getTreeColumn()) {
            super.setWidget(row, column, widget);
        } else {
//            if (widget instanceof GanttTreeItem) {
            super.setWidget(row, column, widget);
//            } else {
//                throw new RuntimeException("Cannot add non-GanttTreeItem to tree column");
//            }
        }
    }

    public void setText(int row, int column, String text) {
        if (column != getTreeColumn()) {
            super.setText(row, column, text);
        } else {
            throw new RuntimeException("Cannot add non-GanttTreeItem to tree column");
        }
    }

    public void setHTML(int row, int column, String text) {
        if (column != getTreeColumn()) {
            super.setHTML(row, column, text);
        } else {
            throw new RuntimeException("Cannot add non-GanttTreeItem to tree column");
        }
    }

    public GanttChart getGanttChart() {
        return ganttChart;
    }

	/*public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }*/
}

