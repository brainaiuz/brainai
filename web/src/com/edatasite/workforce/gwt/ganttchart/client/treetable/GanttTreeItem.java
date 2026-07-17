package com.edatasite.workforce.gwt.ganttchart.client.treetable;

import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.ganttchart.client.GanttChartImageBundle;
import com.edatasite.workforce.gwt.ganttchart.client.GanttContextMenu;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasFocus;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 16, 2011
 * Time: 10:22:44 AM
 * To change this template use File | Settings | File Templates.
 */

public class GanttTreeItem extends Widget implements HasHTML {

	public GanttChartImageBundle images = GWT.create(GanttChartImageBundle.class);

	private Vector children = new Vector();

	private Element itemTable, contentElem, imgElem;

	private boolean open;

	private GanttTreeItem parentItem;

	private boolean selected;

	private Object userObject;

	private GanttChartTree table;

	private int row;

	private Widget widget;
	private Label[] labels;

	public GanttTreeItem() {
		setElement(DOM.createDiv());
		itemTable = DOM.createTable();
		contentElem = DOM.createDiv();
//        contentElem.setAttribute("font-size", "15px");
		imgElem = DOM.createImg();

        Element tr = DOM.createTR();
		Element tdImg = DOM.createTD(), tdContent = DOM.createTD();
		DOM.appendChild(itemTable, tr);
		DOM.appendChild(tr, tdImg);
		DOM.appendChild(tr, tdContent);
		DOM.setStyleAttribute(tdImg, "verticalAlign", "middle");
		DOM.setStyleAttribute(tdContent, "verticalAlign", "middle");
		DOM.setStyleAttribute(tdContent, "textAlign", "center");
//		DOM.setStyleAttribute(tr, "fontSize", "15px");

		DOM.appendChild(getElement(), itemTable);
		DOM.appendChild(tdImg, imgElem);
		DOM.appendChild(tdContent, contentElem);

		DOM.setAttribute(getElement(), "position", "relative");
		DOM.setStyleAttribute(contentElem, "display", "inline");
		DOM.setStyleAttribute(contentElem, "cursor", "pointer");
		DOM.setStyleAttribute(getElement(), "whiteSpace", "nowrap");
		DOM.setAttribute(itemTable, "whiteSpace", "nowrap");
		setStyleName(contentElem, "gwt-TreeItem", true);
	}

	public GanttTreeItem(Object userObj, int columnsCount) {
		this();
		setUserObject(userObj);
        sinkEvents(Event.ONCLICK | Event.ONCONTEXTMENU);
		this.labels = new Label[columnsCount];
	}

    public void onBrowserEvent(Event event) {
        int eventType = DOM.eventGetType(event);
        switch (eventType) {
            case Event.ONCLICK: {
				if (!DOM.compare(DOM.eventGetTarget(event), imgElem)) {
					TaskSingleItem task = (TaskSingleItem) getUserObject();
					if (task.isWorkstream()) {
						SinksContainerFactory.entryPoint.onHistoryChanged("workstream|summary/" + task.getObjectID());
					} else {
						SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + task.getObjectID());
					}
				}
				break;
			}
            case Event.ONCONTEXTMENU: {
                GanttContextMenu menu = new GanttContextMenu(table.getGanttChart(), this);
                event.preventDefault();
                event.stopPropagation();
                menu.getContextMenu().setPopupPosition(event.getClientX(), event.getClientY());
                menu.getContextMenu().show();
                break;
            }
        }
    }

	public GanttTreeItem addItem(Object userObj, int columnsCount) {
		GanttTreeItem ret = new GanttTreeItem(userObj, columnsCount);
		addItem(ret);
		return ret;
	}

	/**
	 * Adds another item as a child to this one.
	 *
	 * @param item
	 *            the item to be added
	 */
	public void addItem(GanttTreeItem item) {
		addItem(item, false);
	}

    public void addItem(GanttTreeItem item, boolean addchilds) {
		if ((item.getParentItem() != null) || (item.getTreeTable() != null)) {
			item.remove();
		}
		item.setTreeTable(table);
		item.setParentItem(this);
		children.add(item);
		int d = item.getDepth();
		if (d != 0) {
			DOM.setStyleAttribute(item.getElement(), "marginLeft", (d * 16) + "px");
		}
		if (table != null) {
            int addRow = getRow() + getChildCount();
            if (addchilds) {
                if (getChildCount() > 0) {
                    int lastChildRow = this.getTreeTable().getLastChildRow(this) + 1;
                    addRow = lastChildRow < addRow ? addRow : lastChildRow;
                }
            }
            table.insertItem(item, addRow);
			table.updateRowCache();
			table.updateVisibility(item);
		}

		if (children.size() == 1) {
			updateState();
		}
	}

	public int getRow() {
		return row;
	}

	public void setRow(int r) {
		row = r;
	}

	/**
	 * Returns the depth of this item. Depth of root child is 0.
	 *
	 * @return
	 */
	public int getDepth() {
		if (parentItem == null) {
			return 0;
		}
		return parentItem.getDepth() + 1;
	}

	/**
	 * Returns the count of all descendents; includes this item in the count.
	 *
	 * @return
	 */
	public int getDescendentCount() {
		int d = 1;
		for (int i = getChildCount() - 1; i >= 0; i--) {
			d += getChild(i).getDescendentCount();
		}
		return d;
	}

	public int getDescendentCount1() {
		int d = 0;
		for (int i = getChildCount() - 1; i >= 0; i--) {
			d++;
			if (getChild(i).isOpen()) {
				d += getChild(i).getDescendentCount1();
			}
		}
		return d;
	}

	/**
	 * Gets the child at the specified index.
	 *
	 * @param index
	 *            the index to be retrieved
	 * @return the item at that index
	 */

	public GanttTreeItem getChild(int index) {
		if ((index < 0) || (index >= children.size())) {
			return null;
		}

		return (GanttTreeItem) children.get(index);
	}

	/**
	 * Gets the number of children contained in this item.
	 *
	 * @return this item's child count.
	 */

	public int getChildCount() {
		return children.size();
	}

	/**
	 * Gets the index of the specified child item.
	 *
	 * @param child
	 *            the child item to be found
	 * @return the child's index, or <code>-1</code> if none is found
	 */

	public int getChildIndex(GanttTreeItem child) {
		return children.indexOf(child);
	}

	public String getHTML() {
		return DOM.getInnerHTML(contentElem);
	}

	/**
	 * Gets this item's parent.
	 *
	 * @return the parent item
	 */
	public GanttTreeItem getParentItem() {
		return parentItem;
	}

	/**
	 * Gets whether this item's children are displayed.
	 *
	 * @return <code>true</code> if the item is open
	 */
	public boolean getState() {
		return open;
	}

	public boolean isOpen() {
		return getState();
	}

	public String getText() {
		return DOM.getInnerText(contentElem);
	}

	/**
	 * Gets the tree that contains this item.
	 *
	 * @return the containing tree
	 */
	public GanttChartTree getTreeTable() {
		return table;
	}

	/**
	 * Gets the user-defined object associated with this item.
	 *
	 * @return the item's user-defined object
	 */
	public Object getUserObject() {
		return userObject;
	}

	/**
	 * Gets the <code>Widget</code> associated with this tree item.
	 *
	 * @return the widget
	 */
	public Widget getWidget() {
		return widget;
	}

	public boolean isSelected() {
		return selected;
	}

	/**
	 * Removes this item from its tree.
	 */
	public void remove() {
		if (parentItem != null) {
			parentItem.removeItem(this);
		} else if (table != null) {
			table.removeItem(this);
		}
	}

	/**
	 * Removes one of this item's children.
	 *
	 * @param item the item to be removed
	 */

	public void removeItem(GanttTreeItem item) {
		if (!children.contains(item)) {
			return;
		}
		// Update Item state.
		item.setTreeTable(null);
		item.setParentItem(null);

		children.remove(item);
		if (table != null) {
			table.removeItemFromTable(item);
		}

		if (children.size() == 0) {
			updateState();
		}
	}

	/**
	 * Removes all of this item's children.
	 */
	public void removeItems() {
		while (getChildCount() > 0) {
			removeItem(getChild(0));
		}
	}

	public void setHTML(String html) {
		DOM.setInnerHTML(contentElem, html);
	}

	public void setSelected(boolean selected) {
		if (this.selected == selected) {
			return;
		}
		this.selected = selected;
		setStyleName(contentElem, "gwt-TreeItem-selected", selected);
	}

	/**
	 * Sets whether this item's children are displayed.
	 *
	 * @param open
	 *            whether the item is open
	 */
	public void setState(boolean open) {
		setState(open, true);
	}

	/**
	 * Sets whether this item's children are displayed.
	 *
	 * @param open
	 *            whether the item is open
	 * @param fireEvents
	 *            <code>true</code> to allow open/close events to be fired
	 */
	public void setState(boolean open, boolean fireEvents) {
		if (open && children.size() == 0) {
			return;
		}

		this.open = open;
		if (open) {
			table.showChildren(this);
		} else {
			table.hideChildren(this);
		}
		updateState();

		if (fireEvents) {
			table.fireStateChanged(this);
		}
	}

	public void setText(String text) {
		DOM.setInnerText(contentElem, text);
	}

	/**
	 * Sets the user-defined object associated with this item.
	 *
	 * @param userObj
	 *            the item's user-defined object
	 */
	public void setUserObject(Object userObj) {
		userObject = userObj;
	}

	/**
	 * Sets the current widget. Any existing child widget will be removed.
	 *
	 * @param widget  Widget to set
	 */
	public void setWidget(Widget w) {
		if (widget != null) {
			DOM.removeChild(contentElem, widget.getElement());
		}
		if (w != null) {
			widget = w;
			DOM.setInnerText(contentElem, null);
			DOM.appendChild(contentElem, w.getElement());
		}
	}

	/**
	 * Returns the widget, if any, that should be focused on if this GanttTreeItem is
	 * selected.
	 *
	 * @return widget to be focused.
	 */
	protected HasFocus getFocusableWidget() {
		Widget widget = getWidget();
		if (widget instanceof HasFocus) {
			return (HasFocus) widget;
		} else {
			return null;
		}
	}

	void addTreeItems(List accum) {
        for (Object aChildren : children) {
            GanttTreeItem item = (GanttTreeItem) aChildren;
            accum.add(item);
            item.addTreeItems(accum);
        }
	}

	public Vector getChildren() {
		return children;
	}

	Element getContentElem() {
		return contentElem;
	}

	int getContentHeight() {
		return DOM.getIntAttribute(itemTable, "offsetHeight");
	}

	Element getImageElement() {
		return imgElem;
	}

	int getTreeTop() {
		GanttTreeItem item = this;
		int ret = 0;

		while (item != null) {
			ret += DOM.getIntAttribute(item.getElement(), "offsetTop");
			item = item.getParentItem();
		}

		return ret;
	}

	void setParentItem(GanttTreeItem parent) {
		this.parentItem = parent;
	}

	void setTreeTable(GanttChartTree table) {
		if (this.table == table) {
			return;
		}

		if (this.table != null) {
			if (this.table.getSelectedItem() == this) {
				this.table.setSelectedItem(null);
			}
		}
		this.table = table;
        for (Object aChildren : children) {
            ((GanttTreeItem) aChildren).setTreeTable(table);
        }
		updateState();
	}

	void updateState() {
		if (children.size() == 0) {
			DOM.setAttribute(imgElem, "src", images.treeWhite().getURL());
			return;
		}
		if (open) {
			DOM.setAttribute(imgElem, "src", images.treeOpen().getURL());
		} else {
			DOM.setAttribute(imgElem, "src", images.treeClosed().getURL());
		}
	}

	void updateStateRecursive() {
		updateState();
        for (Object aChildren : children) {
            ((GanttTreeItem) aChildren).updateStateRecursive();
        }
	}

	public Label[] getLabels() {
		return labels;
	}
}

