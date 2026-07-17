package com.finnetlimited.reportservice.core.client.ui.panel;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * User: ${Dilsh0d}
 * Date: 24-Mar-2010
 * Time: 19:32:59
 */
public class DRSDragDropPanel extends VerticalPanel {

    private String name;
    private String title;
    private Label heading;

    public DRSDragDropPanel(String title, String name) {
        this.name = name;
        this.title = title;
        this.setStyleName("column");
        this.setHeight("120px");
        init();
    }

    private void init() {
        heading = new Label(title);
        heading.setStyleName("column-title");
        heading.setWordWrap(false);
        this.add(heading);
    }

    public String getName() {
        return name;
    }

    public Label getHeading() {
        return heading;
    }

}
