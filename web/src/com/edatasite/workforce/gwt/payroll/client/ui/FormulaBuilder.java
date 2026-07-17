package com.edatasite.workforce.gwt.payroll.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.CategoryRate;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.payroll.client.localization.PayrollStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.TextBox;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 2011-07-17
 * Time: 8:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class FormulaBuilder extends Composite {
    private static final PayrollStrings payrollStrings = GWT.create(PayrollStrings.class);
    private static final WfmStrings wfmStrings = GWT.create(WfmStrings.class);

    private class Storage {

        private Integer value;

        public Storage(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }

    private final String DEFAULT_HEIGHT = "35px";
    private final SelectItem MULTI_RANGE_RATE = new SelectItem(1, payrollStrings.multiRangeRate());
    private final SelectItem SIMPLE_RATE = new SelectItem(0, payrollStrings.simpleRate());

    private final ArrayList<TextBox> fixedAmounts = new ArrayList<>();
    private final FlexTable form = new FlexTable();
    private final ArrayList<TextBox> fromRates = new ArrayList<>();
    private final ArrayList<TextBox> percentages = new ArrayList<>();
    private final HashMap<Integer, Storage> rowCounter = new HashMap<>();
    private final FlexTable table = new FlexTable();
    private final ArrayList<TextBox> toRates = new ArrayList<>();
    private final DataListBox typeList = new DataListBox();

    public FormulaBuilder() {
        build();
    }

    public ArrayList<CategoryRate> getMultiRangeRates() {
        if (typeList.getSelectedItem().equals(MULTI_RANGE_RATE)) {
            ArrayList<CategoryRate> multiRangeRates = new ArrayList<>();

            for (int i = 0; i < fixedAmounts.size(); i++) {
                if (!fromRates.get(i).getText().equals("") && !toRates.get(i).getText().equals("")) {
                    CategoryRate multiRangeRate = new CategoryRate();
                    multiRangeRate.setFrom(BigDecimal.valueOf(Double.parseDouble(fromRates.get(i).getText())));
                    multiRangeRate.setTo(BigDecimal.valueOf(Double.parseDouble(toRates.get(i).getText())));

                    if (fixedAmounts.get(i).getText().equals("")) {
                        double percentage = Double.parseDouble(percentages.get(i).getText());
                        multiRangeRate.setPercentage(new BigDecimal(percentage));
                    } else {
                        multiRangeRate.setFixedAmount(BigDecimal.valueOf(Double.parseDouble(fixedAmounts.get(i).getText())));
                    }

                    multiRangeRates.add(multiRangeRate);
                }
            }

            return multiRangeRates;
        }

        return null;
    }

    public CategoryRate getSimpleRate() {
        if (typeList.getSelectedItem().equals(SIMPLE_RATE)) {
            CategoryRate simpleRate = new CategoryRate();
            if (fixedAmounts.get(0).getText().equals("") && !Utils.isNullOrEmpty(percentages.get(0).getText())) {
                simpleRate.setPercentage(BigDecimal.valueOf(Double.parseDouble(percentages.get(0).getText())));
            } else if (!Utils.isNullOrEmpty(fixedAmounts.get(0).getText())) {
                simpleRate.setFixedAmount(BigDecimal.valueOf(Double.parseDouble(fixedAmounts.get(0).getText())));
            }
            return simpleRate;
        }

        return null;
    }

    public void setLabelWidth(String width) {
        form.getCellFormatter().setWidth(0, 0, width);
    }

    public void setMultiRangeRates(ArrayList<CategoryRate> multiRangeRates) {
        typeList.setSelected(MULTI_RANGE_RATE);
        showMultiRangePart();

        int counter = 0;
        for (CategoryRate multiRangeRate : multiRangeRates) {
            if (counter + 1 < multiRangeRates.size()) {
                drawMultiRangePart(form.getRowCount(), false);
            }

            if (multiRangeRate.getFixedAmount() == null) {
                percentages.get(counter).setText(multiRangeRate.getPercentage().toString());
            } else {
                fixedAmounts.get(counter).setText(multiRangeRate.getFixedAmount().toString());
            }
            fromRates.get(counter).setText(multiRangeRate.getFrom() + "");
            toRates.get(counter).setText(multiRangeRate.getTo() + "");

            counter++;
        }
    }

    public void setSimpleRate(CategoryRate simpleRate) {
        typeList.setSelected(SIMPLE_RATE);
        if (simpleRate.getFixedAmount() == null) {
            percentages.get(0).setText(simpleRate.getPercentage().toString());
        } else {
            fixedAmounts.get(0).setText(simpleRate.getFixedAmount().toString());
        }
    }

    private void build() {
        typeList.setWithoutNullLabel(true);
        typeList.setItems(new SelectItem[]{SIMPLE_RATE, MULTI_RANGE_RATE});
        typeList.setSelected(SIMPLE_RATE);
        typeList.setChangeEvent(() -> {
            if (typeList.getSelectedItem().equals(SIMPLE_RATE)) {
                drawSimpleRatePart();
                table.getRowFormatter().setVisible(1, false);
            } else if (typeList.getSelectedItem().equals(MULTI_RANGE_RATE)) {
                showMultiRangePart();
            }
        });

        form.setCellPadding(0);
        form.setCellSpacing(0);
        form.setHTML(0, 0, getLabel(wfmStrings.type(), true));
        form.setWidget(0, 1, typeList);
        form.getCellFormatter().setHeight(0, 0, DEFAULT_HEIGHT);
        form.getFlexCellFormatter().setColSpan(0, 1, 5);
        drawSimpleRatePart();

        SimpleLink addNewRange = new SimpleLink(payrollStrings.addNewRange(), SimpleLink.ADD_ICON);
        addNewRange.addClickHandler(clickEvent -> drawMultiRangePart(form.getRowCount(), false));

        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setWidget(0, 0, form);
        table.setWidget(1, 0, addNewRange);
        table.getCellFormatter().setHeight(1, 0, DEFAULT_HEIGHT);
        table.getCellFormatter().getElement(1, 0).getStyle().setPaddingLeft(10, Style.Unit.PX);
        table.getRowFormatter().setVisible(1, false);

        initWidget(table);
    }

    private void clearLists() {
        fixedAmounts.clear();
        percentages.clear();
        fromRates.clear();
        toRates.clear();
    }

    private void drawMultiRangePart(final int row, boolean mandatory) {
        rowCounter.put(row, new Storage(row));

        String height = "15px", width = "35px";

        TextBox fixedAmount = new TextBox();
        TextBox percentage = new TextBox();
        TextBox from = new TextBox();
        TextBox to = new TextBox();

        fixedAmounts.add(fixedAmount);
        percentages.add(percentage);
        fromRates.add(from);
        toRates.add(to);

        percentage.setSize(width, height);
        valueController(percentage, fixedAmount);

        fixedAmount.setSize(width, height);
        valueController(fixedAmount, percentage);

        from.setSize(width, height);
        Validation.addNumericKeyboardListener(from);

        to.setSize(width, height);
        Validation.addNumericKeyboardListener(to);

        form.setHTML(row, 0, getLabel(payrollStrings.ratingInterval(), mandatory));
        form.setText(row, 1, "from");
        form.setWidget(row, 2, from);
        form.setText(row, 3, "to");
        form.setWidget(row, 4, to);

        if (!mandatory) {
            SimpleLink remove = new SimpleLink("delete", SimpleLink.REMOVE_ICON);
            remove.addClickHandler(clickEvent -> {
                Storage selectedRow = rowCounter.get(row);

                for (Integer keyRow : rowCounter.keySet()) {
                    Storage realRow = rowCounter.get(keyRow);
                    if (selectedRow.getValue() < realRow.getValue()) {
                        realRow.setValue(realRow.getValue() - 2);
                    }
                }

                form.removeRow(selectedRow.getValue());
                form.removeRow(selectedRow.getValue());

                rowCounter.remove(row);
            });
            form.setWidget(row, 5, remove);
        }

        form.setHTML(row + 1, 0, getLabel(payrollStrings.ratedBy(), mandatory));
        form.setText(row + 1, 1, wfmStrings.percentage() + ":");
        form.setWidget(row + 1, 2, percentage);
        form.setHTML(row + 1, 3, "% <b style='margin-left:5px; margin-right:5px;word-wrap:normal ;'>" + wfmStrings.or() + "</b>" + wfmStrings.fixedAmount());
        form.setWidget(row + 1, 4, fixedAmount);
        form.getCellFormatter().setHeight(row, 0, DEFAULT_HEIGHT);
        form.getCellFormatter().setWidth(row, 1, "32px");
        form.getCellFormatter().setWidth(row, 3, "20px");
        form.getCellFormatter().setHorizontalAlignment(row, 3, HasAlignment.ALIGN_CENTER);
        form.getCellFormatter().setWidth(row, 5, "60px");
        form.getCellFormatter().setHorizontalAlignment(row, 5, HasAlignment.ALIGN_CENTER);
        form.getCellFormatter().setHeight(row + 1, 0, DEFAULT_HEIGHT);
        form.getCellFormatter().setWidth(row + 1, 1, "62px");
        form.getCellFormatter().setWidth(row + 1, 3, "116px");
        form.getCellFormatter().setWidth(row + 1, 4, "45px");
        form.getRowFormatter().getElement(row).getStyle().setBackgroundColor("#EDEDED");
        form.getRowFormatter().getElement(row + 1).getStyle().setBackgroundColor("#EDEDED");
    }

    private void drawSimpleRatePart() {
        int rows = form.getRowCount();
        for (int i = rows - 1; i > 0; i--) {
            form.removeRow(i);
        }

        clearLists();

        String height = "15px", width = "35px";

        TextBox fixedAmount = new TextBox();
        TextBox percentage = new TextBox();

        fixedAmounts.add(fixedAmount);
        percentages.add(percentage);

        percentage.setSize(width, height);
        valueController(percentage, fixedAmount);

        fixedAmount.setSize(width, height);
        valueController(fixedAmount, percentage);

        form.setHTML(1, 0, getLabel(payrollStrings.ratedBy(), true));
        form.setText(1, 1, wfmStrings.percentage() + ":");
        form.setWidget(1, 2, percentage);
        form.setHTML(1, 3, "% <b style='margin-left:5px; margin-right:5px;word-wrap: normal; '>" + wfmStrings.or() + "</b>" + wfmStrings.fixedAmount());
        form.setWidget(1, 4, fixedAmount);
        form.getCellFormatter().setHeight(1, 0, DEFAULT_HEIGHT);
        form.getCellFormatter().setWidth(1, 1, "62px");
        //form.getCellFormatter().setWidth(1, 3, "116px");
    }

    private String getLabel(String text, boolean mandatory) {
        String required = mandatory ? "<font color='red'>*</font>" : "";
        return "<b class=customTitle style='padding-left: 11px;'>" + text + required + ":</b>";
    }

    private void showMultiRangePart() {
        form.removeRow(1);
        clearLists();
        drawMultiRangePart(1, true);
        table.getRowFormatter().setVisible(1, true);
    }

    private void valueController(TextBox textbox, final TextBox pairTextbox) {
        textbox.addKeyPressHandler(event -> {
            char key = event.getCharCode();
            if ((!Character.isDigit(key)) && (key != (char) KeyCodes.KEY_TAB)
                    && (key != (char) KeyCodes.KEY_BACKSPACE)
                    && (key != (char) KeyCodes.KEY_DELETE) && (key != (char) KeyCodes.KEY_ENTER)
                    && (key != (char) KeyCodes.KEY_HOME) && (key != (char) KeyCodes.KEY_END)
                    && (key != (char) KeyCodes.KEY_LEFT) && (key != (char) KeyCodes.KEY_UP)
                    && (key != (char) KeyCodes.KEY_RIGHT) && (key != (char) KeyCodes.KEY_DOWN)) {
                ((TextBox) event.getSource()).cancelKey();
            } else if (!pairTextbox.getText().equals("")) {
                pairTextbox.setText("");
            }
        });
    }

    public boolean validate() {
        int errors = 0;
        if (typeList.getSelectedItem().equals(MULTI_RANGE_RATE)) {
            for (int i = 0; i < fromRates.size(); i++) {
                if (!fromToValidate(fromRates.get(i), toRates.get(i))) {
                    errors++;
                } else {
                    if (i + 1 < fromRates.size()) {
                        Double toRate = Double.parseDouble(toRates.get(i).getText());
                        Double fromRate = Double.parseDouble(fromRates.get(i + 1).getText());
                        if (toRate >= fromRate) errors++;
                    }

                }
            }
            for (int i = 0; i < fixedAmounts.size(); i++) {
                if (numberValidate(fixedAmounts.get(i))) {
                    percentages.get(i).removeStyleName("x-form-invalid");
                } else if (numberValidate(percentages.get(i))) {
                    fixedAmounts.get(i).removeStyleName("x-form-invalid");
                } else {
                    errors++;
                }
            }
        } else if (typeList.getSelectedItem().equals(SIMPLE_RATE)) {
            for (int i = 0; i < fixedAmounts.size(); i++) {
                if (numberValidate(fixedAmounts.get(i))) {
                    percentages.get(i).removeStyleName("x-form-invalid");
                } else if (numberValidate(percentages.get(i))) {
                    fixedAmounts.get(i).removeStyleName("x-form-invalid");
                } else {
                    errors++;
                }
            }
        }

        return errors <= 0;
    }

    private boolean numberValidate(final TextBox textBox) {
        boolean flag = true;
        if (!textBox.getText().equals("")) {
            String text = textBox.getText();
            if (!text.matches("^[.0-9]{0,}$")) {
                textBox.setStyleName("x-form-invalid");
                flag = false;
            }
        } else {
            textBox.setStyleName("x-form-invalid");
            flag = false;
        }
        textBox.addKeyDownHandler(event -> textBox.removeStyleName("x-form-invalid"));

        return flag;
    }

    private boolean fromToValidate(final TextBox textBox, final TextBox textBox2) {
        boolean flag = numberValidate(textBox) & numberValidate(textBox2);
        if (flag) {
            double from = Double.parseDouble(textBox.getText());
            double to = Double.parseDouble(textBox2.getText());
            if (from > to) flag = false;
        }
        return flag;
    }
}
