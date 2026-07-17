package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ColorWidget;
import com.google.gwt.user.client.Command;
import gwt.material.design.client.ui.html.Div;

public class GradientColorWidget extends Div {
    private GRow gradientColorTab;
    private ColorWidget colorWidget;
    private String selectedColor;
    private Command changeColorCommand;

    public GradientColorWidget(Command changeColorCommand) {
        super();
        this.changeColorCommand = changeColorCommand;
        initialize();
    }

//    public GradientColorWidget(Command changeColorCommand,String color) {
//        this.selectedColor = color;
//        this.changeColorCommand = changeColorCommand;
//        initialize();
//    }

    public void initialize() {
        if (!Utils.isNullOrEmpty(selectedColor)) {
            colorWidget = new ColorWidget(selectedColor);
        } else {
            colorWidget = new ColorWidget();
        }
        colorWidget.getElement().setAttribute("style", "width: 400px !important;");
        colorWidget.setChangeHandler(() -> {
            this.selectedColor = colorWidget.getColor();
            changeColorTabWidget(selectedColor);
//            if (changeColorCommand != null) {
//                changeColorCommand.execute();
//            }
        });
        gradientColorTab = new GRow();
        gradientColorTab.setWidth("100%");
        gradientColorTab.setHeight("30px");
        gradientColorTab.setMargin(8.0);
        changeColorTabWidget(Utils.isNullOrEmpty(selectedColor) ? "#2C74DB" : selectedColor);
        Div mainDiv = new Div("color-widget");
        mainDiv.getElement().setAttribute("style", "width: 390px !important;");
        mainDiv.add(colorWidget);
        mainDiv.add(gradientColorTab);
        add(mainDiv);
    }

    private void changeColorTabWidget(String selectedColor) {
        gradientColorTab.clear();
        double defaultWidth = 1.0 / 12;
        for (int i = 0; i < 10; i++) {
            GColumn li = new GColumn();
            li.setStyle("background-color:" + selectedColor + "; margin : 0.5px; border-radius:5px");
            li.setOpacity(1.0 - defaultWidth * (i + 1));
            gradientColorTab.add(li);
        }
    }

    public String getSelectedColor() {
        return colorWidget.getColor();
    }

    public void setSelectedColor(String color) {
        if (Utils.isNullOrEmpty(color)) {
            color = "#2C74DB";
        }
        colorWidget.setColor(color);
        changeColorTabWidget(color);
    }

    public void setChangeColorCommand(Command changeColorCommand) {
        this.changeColorCommand = changeColorCommand;
    }
}
