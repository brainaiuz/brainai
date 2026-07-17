package com.edatasite.workforce.gwt.core.client.ui.landing;

import com.edatasite.workforce.gwt.core.client.View;
import com.google.gwt.user.client.ui.*;

public abstract class LandingView extends View {

    private VerticalPanel centerPanel;
    private VerticalPanel rightPanel;
    private HorizontalPanel header;
    private HorizontalPanel descriptionPanel;
    private HorizontalPanel addNewPanel;
    private HorizontalPanel center;
    // private Label title;
    private Widget[] rightPanelItems;
    private int rightPanelIndex;
    private Widget[] centerPanelItems;
    private int centerPanelIndex;
    private FlexTable title;
    private HTML headerText;
    private String name;
    private String description;
    private String centerWidth;
    private String rightWidth;
    private HTML headerLabel;

    public LandingView() {
        doInit(null);
    }

    public LandingView(String name, String description) {
        super(name, description);
        doInit(description);
    }

    public LandingView(String name, String description, String centerWidth, String rightWidth) {
        super(name, description);
        this.centerWidth = centerWidth;
        this.rightWidth = rightWidth;
        doInit(description);
    }

    public void doInit(String name) {
        centerPanelItems = new Widget[1];
        centerPanelIndex = 0;
        rightPanelItems = new Widget[1];
        rightPanelIndex = 0;

        if (name != null) {
            title = new FlexTable();
            title.setWidth("100%");
            title.setStyleName("customTitle");
            title.setBorderWidth(0);
            HTMLTable.CellFormatter formatter = title.getCellFormatter();
            formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
            title.setHeight("20px");
            headerLabel = new HTML("<b>" + name + "</b>");
            title.setWidget(0, 0, headerLabel);
            title.setCellPadding(2);
            title.setCellSpacing(0);
        }

        header = new HorizontalPanel();
        header.setHeight("20px");
        header.setWidth("100%");
        //header.setSpacing(2);

        //header.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        descriptionPanel = new HorizontalPanel();
        descriptionPanel.setHeight("20px");
        descriptionPanel.setWidth("100%");
        descriptionPanel.setSpacing(2);
        descriptionPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
//		descriptionPanel.add(title);
        header.add(descriptionPanel);

        addNewPanel = new HorizontalPanel();
        addNewPanel.setHeight("20px");
        addNewPanel.setWidth("100%");
        addNewPanel.setSpacing(2);
        addNewPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        header.add(addNewPanel);
        add(header);

        centerPanel = new VerticalPanel();

        centerPanel.setWidth("100%");
        //centerPanel.setSpacing(2);
        centerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
        centerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

        rightPanel = new VerticalPanel();
        rightPanel.setWidth("100%");
        //rightPanel.setSpacing(2);

        rightPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        rightPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);

        center = new HorizontalPanel();
        center.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        center.setWidth("100%");

        center.add(centerPanel);
        if (centerWidth != null) {
            center.setCellWidth(centerPanel, centerWidth);
        } else {
            center.setCellWidth(centerPanel, "70%");
        }
        center.add(rightPanel);
        if (rightWidth != null) {
            center.setCellWidth(rightPanel, rightWidth);
        } else {
            center.setCellWidth(rightPanel, "30%");
        }

        add(center);
    }

    public void setHeaderLabel(String name) {
        headerLabel.setHTML("<b>" + name + "</b>");
    }

    public void setHeaderLabel(HTML name) {
        headerLabel = name;
    }

    public void clear() {
        for (int i = 0; i < rightPanelIndex; i++) {
            rightPanel.remove(rightPanelItems[i]);
        }
        for (int i = 0; i < centerPanelIndex; i++) {
            centerPanel.remove(centerPanelItems[i]);
        }

        centerPanelItems = new Widget[1];
        centerPanelIndex = 0;

        rightPanelItems = new Widget[1];
        rightPanelIndex = 0;
    }

    public void addCenterData(Widget widget) {
        centerPanel.add(widget);
        centerPanelItems[centerPanelIndex] = widget;
        Widget[] temp = new Widget[centerPanelItems.length + 1];
        System.arraycopy(centerPanelItems, 0, temp, 0, centerPanelItems.length);
        centerPanelItems = temp;
        centerPanelIndex++;

    }

    public void addRightData(Widget widget) {
        rightPanel.add(widget);
        rightPanelItems[rightPanelIndex] = widget;
        Widget[] temp = new Widget[rightPanelItems.length + 1];
        System.arraycopy(rightPanelItems, 0, temp, 0, rightPanelItems.length);
        rightPanelItems = temp;
        rightPanelIndex++;
    }

    public void addTopWidget(Widget widget) {
        addNewPanel.add(widget);
    }

    public void add(Widget widget) {
        super.add(widget);
    }

    protected VerticalPanel getCentralData() {
        return centerPanel;
    }

    public void setCenterWidth(String centerWidth) {
        this.centerWidth = centerWidth;
        center.setCellWidth(centerPanel, centerWidth);
    }

    public void setRightWidth(String rightWidth) {
        this.rightWidth = rightWidth;
        center.setCellWidth(rightPanel, rightWidth);
    }
}