package com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 16.03.2009
 * Time: 18:35:35
 * To change this template use File | Settings | File Templates.
 */
public class Budgetsheet extends Composite {

    /*Styles*/
    private final String blueHeader = "blue-header";
    private final String blueBorder = "blue-border";
    private final String cellSettings = "cell-settings-1";
    private final String groupRowStyle = "blue-header"/*"group-row-style"*/;
    private final String textboxStyle = "textbox-style";
    private final String cellBackgroundOnClick = "cell-background-on-click";

    private final int height = 20;

    private FlexTable table;
    private HTMLTable.RowFormatter rowFormatter;
    private HTMLTable.CellFormatter cellFormatter;

    private Map columnsMap;
    private List items;
    private BudgetsheetColumn[] columns;

    private int rows = 0;

    public Budgetsheet(BudgetsheetColumn[] columns) {
        this.columns = columns;
        initialize();
    }

    private void initialize() {
        table = new FlexTable();
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setBorderWidth(1);
        table.addStyleName(blueBorder);

        rowFormatter = table.getRowFormatter();
        cellFormatter = table.getCellFormatter();

        columnsMap = new HashMap();
        items = new ArrayList();

        onRender();
    }

    private void onRender() {
        drawHeader();
        initWidget(table);
    }

    private void drawHeader() {
        for (int i = 0; i < columns.length; i++) {
            HTML html = new HTML("<b>" + columns[i].getText() + "</b>");
            html.setWidth(columns[i].getWidth() + "px");

            table.setWidget(rows, i, html);
            cellFormatter.setHeight(rows, i, "25px");
            cellFormatter.setAlignment(rows, i, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
            cellFormatter.addStyleName(rows, i, blueHeader);

            if (columns[i].getData() != null) {
                columnsMap.put(columns[i].getData(), columns[i]);
            }
        }
        rows++;
    }

    public void add(BudgetsheetItem item) {
        insert(getItemCount(), item);
    }

    public void add(BudgetsheetItem item, HasHorizontalAlignment.HorizontalAlignmentConstant horizontalAlignment) {
        insert(getItemCount(), item, horizontalAlignment);
    }

    public void add(String groupName, BudgetsheetItem[] items) {
        addGroup(groupName);
        for (BudgetsheetItem item : items) {
            insert(getItemCount(), item);
        }
    }

    public void addGroup(String groupName) {
        table.setHTML(rows, 0, "<b>" + groupName + "</b>");
        cellFormatter.setStyleName(rows, 0, cellSettings);
        for (int i = 0; i < columns.length; i++) {
            cellFormatter.addStyleName(rows, i, groupRowStyle);
        }
        cellFormatter.setHeight(rows, 0, height + "px");

        rows++;
    }

    public BudgetsheetItem getItem(int index) {
        return (BudgetsheetItem) items.get(index);
    }

    public List getItems() {
        return items;
    }

    public int getItemCount() {
        return items.size();
    }

    public void insert(int index, BudgetsheetItem item) {
        insert(index, item, HasAlignment.ALIGN_LEFT);
    }

    public void insert(int index, BudgetsheetItem item, HasHorizontalAlignment.HorizontalAlignmentConstant horizontalAlignment) {
        items.add(index, item);

        for (int i = 0; i < item.getValues().length; i++) {
            BudgetsheetObject object = item.getValue(i);

            cellFormatter.setHeight(rows, i, height + "px");
            cellFormatter.setStyleName(rows, i, cellSettings);

            if (object.getValue() instanceof Widget) {
                table.setWidget(rows, i, (Widget) object.getValue());
            } else {
                if (object.isEditable()) {
                    table.setWidget(rows, i, getEditableCell(rows, i, object));
                } else {
                    String html = String.valueOf(object.getValue());
                    table.setHTML(rows, i, !html.equals("null") && !html.equals("") ? html : "&nbsp;");
                    table.getCellFormatter().setHorizontalAlignment(rows, i, horizontalAlignment);
                    if (object.getItemStyle() != null && !object.getItemStyle().equals("")) {
                        cellFormatter.addStyleName(rows, i, object.getItemStyle());
                    }
                }
            }
        }
        rows++;
    }

    private HTML getEditableCell(final int row, final int column, final BudgetsheetObject object) {
        final HTML cell = new HTML(formatToDouble(String.valueOf(object.getValue())));
        cell.setWidth(columns[column].getWidth() + "px");
        cell.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        cell.addClickHandler(sender -> {
            cellFormatter.addStyleName(row, column, cellBackgroundOnClick);

            final PopupPanel popup = new PopupPanel(true, true);
            final TextBox textbox = getTextBox(column, popup, object.getKeyboardListener());
            textbox.setText(parseToString(cell.getText()));

            popup.setWidget(textbox);
            popup.setPopupPositionAndShow((offsetWidth, offsetHeight) -> popup.setPopupPosition(cell.getAbsoluteLeft(), cell.getAbsoluteTop()));

            popup.addPopupListener((sender1, autoClosed) -> {
                cell.setHTML(formatToDouble(textbox.getText()));
                cellFormatter.removeStyleName(row, column, cellBackgroundOnClick);
                object.getBudgetsheetProvider().save(parseToBigDecimal(cell.getText()), columns[column].getData());
            });

            textbox.setFocus(true);
        });

        return cell;
    }

    public BigDecimal parseToBigDecimal(String text) {
        return text != null && !text.equals("") ? AccountingUtils.get().parseToBigDecimal(text) : AccountingConstants.ZERO;
    }

    public String formatToDouble(String text) {
        return AccountingUtils.get().formatPrice(parseToBigDecimal(text));
    }

    public String parseToString(String text) {
        return AccountingUtils.get().formatPrice(parseToBigDecimal(text));
    }

    private TextBox getTextBox(int index, final PopupPanel popup, final BudgetsheetKeyboardListener keyboardListener) {
        final TextBox textbox = new TextBox();
        textbox.setWidth((columns[index].getWidth()) + "px");
        textbox.setStyleName(textboxStyle);
        textbox.setMaxLength(9);
        textbox.setAlignment(ValueBoxBase.TextAlignment.RIGHT);
        Validation.addNumericKeyboardListener(textbox, AccountingUtils.calculationScale);
        textbox.addKeyboardListener(new KeyboardListener() {

            public void onKeyDown(Widget sender, char keyCode, int modifiers) {

            }

            public void onKeyPress(Widget sender, char keyCode, int modifiers) {
                if (keyCode == KeyboardListener.KEY_ENTER || keyCode == KeyboardListener.KEY_ESCAPE) {
                    popup.hide();
                }
            }

            public void onKeyUp(Widget sender, char keyCode, int modifiers) {

            }
        });
		textbox.addClickHandler(clickEvent -> {
            if (textbox.getText() != null && !textbox.getText().isEmpty()) {
                try {
                    BigDecimal bigDecimal = new BigDecimal(textbox.getText());
                    if (bigDecimal.intValue() == 0) {
                        textbox.setText(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return textbox;
    }

    public FlexTable getTable() {
        return table;
    }

    public int getWidth() {
        return table.getOffsetWidth();
    }

    public void clear() {
        /*for (int i = 0; i < getItemCount(); i++) {
            items.remove(i);
        }*/
        items.clear();
        table.clear();
        rows = 1;
    }

    public BudgetsheetColumn getColumn(int index) {
        return index < columns.length ? columns[index] : null;
    }

    public BudgetsheetColumn getColumnByData(Object data) {
        return (BudgetsheetColumn) columnsMap.get(data);
    }

    public Object getColumnData(int index) {
        return getColumn(index).getData();
    }
}
