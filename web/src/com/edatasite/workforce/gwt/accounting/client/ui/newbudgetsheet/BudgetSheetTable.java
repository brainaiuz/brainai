package com.edatasite.workforce.gwt.accounting.client.ui.newbudgetsheet;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet.BudgetsheetColumn;
import com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet.BudgetsheetItem;
import com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet.BudgetsheetKeyboardListener;
import com.edatasite.workforce.gwt.accounting.client.ui.budgetsheet.BudgetsheetObject;
import com.edatasite.workforce.gwt.accounting.client.ui.view.newreport.BudgetSheet.NewBudgetSheet;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;
import org.gwt.advanced.client.ui.widget.AdvancedFlexTable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by admin on 05.09.2014.
 */
public class BudgetSheetTable extends Composite implements Constants {

    /*Styles*/
    private final String headRowStyle = "heading_row";
    private final String inputPlaceStyle = "gwt_input_place";
    private final String textboxStyle = "textbox-style";
    private final String cellBackgroundOnClick = "cell-background-on-click";

    private final int height = 20;

    private AdvancedFlexTable table;
    private HTMLTable.RowFormatter rowFormatter;
    private HTMLTable.CellFormatter cellFormatter;

    private Map<Object, BudgetsheetColumn> columnsMap;
    private List<BudgetsheetItem> items;
    private BudgetsheetColumn[] columns;
    private NewBudgetSheet newBudgetSheet;
    private String styleIcon;
    private int sortDirection;

    private int rows = 0;

    public BudgetSheetTable(NewBudgetSheet newBudgetSheet, BudgetsheetColumn[] columns, String styleIcon, int sortDirection) {
        this.columns = columns;
        this.newBudgetSheet = newBudgetSheet;
        this.styleIcon = styleIcon;
        this.sortDirection = sortDirection;
        initialize();
    }

    private void initialize() {
        table = new AdvancedFlexTable();
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setBorderWidth(1);
        table.addStyleName("table table_report table_report_sections");

        rowFormatter = table.getRowFormatter();
        cellFormatter = table.getCellFormatter();

        columnsMap = new HashMap<>();
        items = new ArrayList<>();

        onRender();
    }

    private void onRender() {
        drawHeader();
        initWidget(table);
    }

    private void drawHeader() {

        for (int i = 0; i < columns.length; i++) {

            final HTML html = new HTML((i == 0 ? "<a href=\"#\" ui:field=\"codeText\" style=\"color:#fff;\">" + columns[i].getText() + "</a>" : columns[i].getText()) +
                    (i == 0 ? "<a style=\"margin-left:4px\" class=" + (styleIcon != null ? styleIcon : "fonticon-sort-alpha-asc") + "  href=\"#\"</a>" : ""));
            if (i == 0) {
                html.addClickHandler(clickEvent -> {
                    if (sortDirection == ASC) {
                        newBudgetSheet.refresh(DESC, "fonticon-sort-alpha-desc");
                    } else {
                        newBudgetSheet.refresh(ASC, "fonticon-sort-alpha-asc");
                    }
                });
            }
            html.setStyleName("cell_holder frame_affix_top");
            if (!columns[i].getStyle().isEmpty()) {
                html.addStyleName(columns[i].getStyle());
            }
            html.getElement().getStyle().setProperty("minWidth", columns[i].getWidth() + "px");

            table.setHeaderWidget(i, html);
            if (columns[i].getData() != null) {
                columnsMap.put(columns[i].getData(), columns[i]);
            }
        }
        table.getTHeadElement().addClassName("point_affix_top");
        table.getTHeadElement().addClassName("text-nowrap");
    }

    public void add(BudgetsheetItem item, String rowStyle) {
        insert(getItemCount(), item, rowStyle);
    }

    public void add(String groupName, BudgetsheetItem[] items) {
        addGroup(groupName);
        for (BudgetsheetItem item : items) {
            insert(getItemCount(), item, item.isHasChild() ? headRowStyle : null);
        }
    }

    public void addGroup(String groupName) {
        table.setHTML(rows, 0, groupName);
        Element tbody = DOM.getChild(table.getElement(), 2);
        tbody.addClassName("category_set");
        Element tr = DOM.getChild(tbody, rows);
        tr.addClassName(headRowStyle);
        tr.getStyle().setProperty("borderBottom", "1px solid #f5f7f9");
        Element td = DOM.getChild(tr, 0);
        td.setAttribute("colspan", String.valueOf(columns.length));
        cellFormatter.setHeight(rows, 0, height + "px");

        rows++;
    }

    public BudgetsheetItem getItem(int index) {
        return items.get(index);
    }

    public List<BudgetsheetItem> getItems() {
        return items;
    }

    public int getItemCount() {
        return items.size();
    }

    public void insert(int index, BudgetsheetItem item, String rowStyle) {
        items.add(index, item);

        for (int i = 0; i < item.getValues().length; i++) {
            BudgetsheetObject object = item.getValue(i);

            cellFormatter.setHeight(rows, i, height + "px");

            if (object.getValue() instanceof Widget) {
                table.setWidget(rows, i, (Widget) object.getValue());
            } else {
                if (object.isEditable()) {
                    table.setWidget(rows, i, getEditableCell(rows, i, object));
                } else {
                    String html = String.valueOf(object.getValue());
                    table.setHTML(rows, i, !html.equals("null") && !html.equals("") ? html : "&nbsp;");
                    if (object.getItemStyle() != null && !object.getItemStyle().equals("")) {
                        cellFormatter.addStyleName(rows, i, object.getItemStyle());
                    }
                }
            }
        }
        Element tr = DOM.getChild(DOM.getChild(table.getElement(), 2), rows);
        tr.getStyle().setProperty("borderBottom", "1px solid #f5f7f9");
        if (tr != null) {
            if (item.getLevel() != null) {
                tr.getFirstChildElement().getStyle().setProperty("paddingLeft", (item.getLevel() + 4) + "rem");
            }
            if (rowStyle != null && !Objects.equals(rowStyle, "")) {
                tr.addClassName(rowStyle);
            }
        }
        rows++;
    }

    private HTML getEditableCell(final int row, final int column, final BudgetsheetObject object) {
        final HTML cell = new HTML(formatToDouble(String.valueOf(object.getValue())));
        cell.addStyleName(RIGHT_ALIGN_CELL);
        cell.addStyleName(inputPlaceStyle);
        cell.addClickHandler(sender -> {
            cellFormatter.addStyleName(row, column, cellBackgroundOnClick);

            final PopupPanel popup = new PopupPanel(true, true);
            final TextBox textbox = getTextBox(column, popup, object.getKeyboardListener());
            textbox.setWidth((cell.getElement().getClientWidth()) + "px");
            if (parseToBigDecimal(cell.getText()).compareTo(BigDecimal.ZERO) > 0 || parseToBigDecimal(cell.getText()).compareTo(BigDecimal.ZERO) < 0) {
                textbox.setText(parseToString(cell.getText()));
            } else {
                textbox.setText("");
            }
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
        if (text != null && text.toCharArray().length > 1
                && text.toCharArray()[0] == '('
                && text.toCharArray()[text.toCharArray().length - 1] == ')') {
            return AccountingUtils.get().parseToBigDecimal(text.substring(1, text.toCharArray().length - 1)).negate();
        }
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
        textbox.setMaxLength(12);
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
        return columnsMap.get(data);
    }

    public Object getColumnData(int index) {
        return getColumn(index).getData();
    }
}
