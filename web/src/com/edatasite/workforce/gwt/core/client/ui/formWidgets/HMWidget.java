package com.edatasite.workforce.gwt.core.client.ui.formWidgets;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by Normurod on 12/14/2016.
 */
public class HMWidget extends Composite {

    private TextBox hoursBox;
    private TextBox minutesBox;
    private HorizontalPanelDiv pnlHours;

    private String HH = "HH";
    private String MM = "MM";

    public HMWidget() {
        hoursBox = new TextBox();
        hoursBox.setText(HH);
        hoursBox.getElement().getStyle().setTextAlign(Style.TextAlign.RIGHT);
        hoursBox.setWidth("59px");
        Validation.addNumericKeyboardListener(hoursBox);

        hoursBox.getElement().getStyle().setColor("rgba(0, 0, 0, 0.26)");
        hoursBox.addFocusHandler(event -> {
            if (HH.equals(hoursBox.getValue())) {
                hoursBox.setText("");
                hoursBox.getElement().getStyle().setColor("#000");
            }
        });
        hoursBox.addBlurHandler(event -> {
            if ("".equals(hoursBox.getValue())) {
                hoursBox.getElement().getStyle().setColor("rgba(0, 0, 0, 0.26)");
                hoursBox.setText(HH);
            }
        });

        minutesBox = new TextBox();
        minutesBox.setText(MM);
        minutesBox.setWidth("59px");
        Validation.addNumericKeyboardListener(minutesBox);

        minutesBox.getElement().getStyle().setColor("rgba(0, 0, 0, 0.26)");
        minutesBox.addFocusHandler(event -> {
            if (MM.equals(minutesBox.getValue())) {
                minutesBox.setText("");
                minutesBox.getElement().getStyle().setColor("#000");
            }
        });
        minutesBox.addBlurHandler(event -> {
            if ("".equals(minutesBox.getValue())) {
                minutesBox.getElement().getStyle().setColor("rgba(0, 0, 0, 0.26)");
                minutesBox.setText(MM);
            }
        });

        pnlHours = new HorizontalPanelDiv(5, hoursBox, new HTML(":"), minutesBox);
        initWidget(pnlHours);
    }

    public Integer getValueAsMinutes() {
        Integer minutes = 0;

        if (!hoursBox.getText().isEmpty() && !HH.equals(hoursBox.getText())) {
            minutes = Integer.parseInt(hoursBox.getText()) * 60;
        }
        if (!minutesBox.getText().isEmpty() && !MM.equals(minutesBox.getText())) {
            minutes += Integer.parseInt(minutesBox.getText());
        }

        return minutes;
    }

    public void setValueAsMinutes(Integer minutes) {
        if (minutes == null) {
            hoursBox.setText(HH);
            minutesBox.setText(MM);
        } else {
            hoursBox.setText(String.valueOf(minutes / 60));
            minutesBox.setText(String.valueOf(minutes % 60));
        }
    }
}
