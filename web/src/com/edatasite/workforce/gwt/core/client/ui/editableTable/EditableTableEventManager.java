package com.edatasite.workforce.gwt.core.client.ui.editableTable;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.DefaultGridEventManager;
import org.gwt.advanced.client.ui.widget.EditableGrid;
import org.gwt.advanced.client.ui.widget.GridPanel;
import org.gwt.advanced.client.ui.widget.cell.AbstractCell;
import org.gwt.advanced.client.ui.widget.cell.GridCell;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/22/12
 * Time: 1:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditableTableEventManager extends DefaultGridEventManager {
    /**
     * Creates an instance of the class and adds itself to the listeners list of the grid.
     *
     * @param panel is a grid panel.
     */

    private GridCell activeCell;

    private EditableTableListener listener;

    private boolean isLookUp;
    private boolean isTextArea;
    private boolean showRemoveCell;
    private boolean addOnClick = true;

    public EditableTableEventManager(GridPanel panel) {
        this(panel, true);
    }

    public EditableTableEventManager(GridPanel panel, boolean showRemoveCell, boolean addOnClick) {
        super(panel);
        this.showRemoveCell = showRemoveCell;
        this.addOnClick = addOnClick;
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SELECTION_LOOKUPCELL, new Widget(), (sender, args) -> isLookUp = false);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DRAG_END, new Widget(), (sender, args) -> activeCell = null);
    }

    public EditableTableEventManager(GridPanel panel, boolean showRemoveCell) {
        this(panel, showRemoveCell, true);
    }

    @Override
    public boolean dispatch(GridPanel panel, char keyCode, int modifiers) {
        if (!isLookUp) {
            if (KeyCodes.KEY_UP == keyCode && !isTextArea) {
                moveCursorUp();
                return true;
            } else if (KeyCodes.KEY_DOWN == keyCode && !isTextArea) {
                moveCursorDown();
                return true;
            } else if (KeyCodes.KEY_TAB == keyCode) {
                //activateCell();
                moveCursorRight();
                return true;
            } else if (KeyCodes.KEY_ENTER == keyCode && !isReadOnly() && !isTextArea) {
                activateCell();
                return false;
            }
        }
        return false;
    }


    /**
     * Moves the cursor down
     */
    @Override
    protected void moveCursorDown() {
        EditableGrid grid = getPanel().getGrid();
        int row = grid.getCurrentRow() + 1;
        if (row != grid.getRowCount() - 1) {
            int cell = grid.getCurrentColumn();
            GridCell gridCell = (GridCell) grid.getWidget(row, cell);
            if (activeCell != null) {
                grid.fireFinishEdit(activeCell, activeCell instanceof CustomCell ? activeCell.getValue() : activeCell.getNewValue());
                activeCell.displayActive(false);
            }
            checkForLookUp(gridCell);
            gridCell.displayActive(true);
            activeCell = gridCell;
            setCursor(row, cell, true);
        } else {
            if (listener != null) {
                listener.addRow();
            }
            int cell = grid.getCurrentColumn();
            GridCell gridCell = (GridCell) grid.getWidget(row, cell);
            if (activeCell != null) {
                grid.fireFinishEdit(activeCell, activeCell instanceof CustomCell ? activeCell.getValue() : activeCell.getNewValue());
                activeCell.displayActive(false);
            }
            checkForLookUp(gridCell);
            gridCell.displayActive(true);
            activeCell = gridCell;
            setCursor(row, cell, true);

        }
    }


    /**
     * Moves the cursor up
     */
    @Override
    protected void moveCursorUp() {
        EditableGrid grid = getPanel().getGrid();
        int row = grid.getCurrentRow() - 1;
        if (row >= 0) {
            int cell = grid.getCurrentColumn();
            GridCell gridCell = (GridCell) grid.getWidget(row, cell);
            if (activeCell != null) {
                grid.fireFinishEdit(activeCell, activeCell instanceof CustomCell ? activeCell.getValue() : activeCell.getNewValue());
                activeCell.displayActive(false);
            }
            checkForLookUp(gridCell);
            gridCell.displayActive(true);
            activeCell = gridCell;
            setCursor(row, cell, true);
        }
    }


    /**
     * Moves the cursor right
     */
    @Override
    protected void moveCursorRight() {
        EditableGrid grid = getPanel().getGrid();
        int row = grid.getCurrentRow();
        if (row == grid.getRowCount() - 1) {
            row--;
        }
        int cell = grid.getCurrentColumn() + 1;
        if (cell != grid.getModel().getColumns().length - 1) {
            GridCell gridCell = (GridCell) grid.getWidget(row, cell);
            if (activeCell != null) {
                grid.fireFinishEdit(activeCell, activeCell instanceof CustomCell ? activeCell.getValue() : activeCell.getNewValue());
                activeCell.displayActive(false);
            }
            checkForLookUp(gridCell);
            gridCell.displayActive(true);
            activeCell = gridCell;
            setCursor(row, cell, false);
        }
    }

    /**
     * Moves the cursor left
     */
    @Override
    protected void moveCursorLeft() {
        EditableGrid grid = getPanel().getGrid();
        int row = grid.getCurrentRow();
        if (row == grid.getRowCount() - 1) {
            row--;
        }
        int cell = grid.getCurrentColumn() - 1;
        GridCell gridCell = (GridCell) grid.getWidget(row, cell);
        if (activeCell != null) {
            grid.fireFinishEdit(activeCell, activeCell instanceof CustomCell ? activeCell.getValue() : activeCell.getNewValue());
            activeCell.displayActive(false);
        }
        checkForLookUp(gridCell);
        gridCell.displayActive(true);
        activeCell = gridCell;
        setCursor(row, cell, false);
    }

    @Override
    protected void moveByTab() {
        super.moveByTab();
    }

    /**
     * Activates the currently selected cell
     */
    @Override
    protected void activateCell() {
        EditableGrid grid = getPanel().getGrid();
        int row = grid.getCurrentRow();
        int cell = grid.getCurrentColumn();
        GridCell gridCell = (GridCell) grid.getWidget(row, cell);
        if (gridCell instanceof LookUpCell) {
            grid.fireFinishEdit(gridCell, gridCell.getNewValue());
        }
        if (gridCell instanceof CustomCell) {
            Widget widget = ((CustomCell) gridCell).InActive();
            ((CustomCell) gridCell).fireEvent();
            ((CustomCell) gridCell).prepare(widget);
        }
        activeCell = null;
    }


    /**
     * Sets the current position of the cursor or activates the selected cell.
     */
    @Override
    public void onClick(ClickEvent event) {
        int row = 0;
        EditableGrid grid = getPanel().getGrid();
        HTMLTable.Cell cellForEvent = grid.getCellForEvent(event);

        if (cellForEvent != null) {
            row = cellForEvent.getRowIndex();
            int cell = cellForEvent.getCellIndex();

            if (!showRemoveCell || cell != grid.getModel().getColumns().length - 1) {
                GridCell gridCell = (GridCell) grid.getWidget(row, cell);

                if (gridCell != null) {

                    if (activeCell != null && !gridCell.equals(activeCell)) {

                        if (!(activeCell instanceof LookUpCell)) {
                            grid.fireFinishEdit(activeCell, activeCell.getNewValue());
                        }
                        activeCell.displayActive(false);
                    } else {
                        if (Utils.isIE()) {
                            gridCell.setFocus(true);
                        }
                    }

                    if ((gridCell instanceof CustomCell) && !(((CustomCell) gridCell).getCustomWidget() instanceof DataListBox)) {
                        ((AbstractCell) gridCell).setActive(false);
                        ((CustomCell) gridCell).setLeft(cellForEvent.getElement().getAbsoluteLeft());
                        ((CustomCell) gridCell).setTop(cellForEvent.getElement().getAbsoluteTop());

                    }

                    checkForLookUp(gridCell);
                    activeCell = gridCell;
                    gridCell.displayActive(true);
                    setCursor(row, cell, false);
                    if (row == grid.getRowCount() - 1) {
                        if (listener != null && addOnClick) {
                            listener.addRow();
                        }
                        grid.setCurrentCell(row, cell);
                    }
                }
            } else {
                grid.setCurrentCell(row, cell);
                activeCell = null;
            }
        }
    }

    public void checkForLookUp(GridCell gridCell) {
        isLookUp = gridCell instanceof LookUpCell;

        isTextArea = false;
        if (gridCell instanceof CustomCell) {
            Widget customWidget = ((CustomCell) gridCell).getCustomWidget();
            if (customWidget != null && (customWidget instanceof TextArea2 || customWidget instanceof TextArea)) {
                isTextArea = true;
            }
        }
    }


    public void setListener(EditableTableListener listener) {
        this.listener = listener;
    }
}
