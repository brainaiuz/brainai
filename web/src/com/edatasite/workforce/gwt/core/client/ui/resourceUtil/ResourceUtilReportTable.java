package com.edatasite.workforce.gwt.core.client.ui.resourceUtil;

import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.EmployeeResourceUtilItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ResourceUtilItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 5/16/12
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceUtilReportTable extends Composite implements ResourceUtilReportConstants {

    private int currentDay;
    private int monthMaxDay = 0;
    private String monthName;
    private MonthDay monthDay;
    private Date date;
    private ResourceUtilReportTableData resourceUtilReportData;
    private ResourceUtilItem resourceUtilItem;
    private ResourceUtilizationView resourceUtilizationView;

    private Element headerTableElement;
    private Element contentTableElement;

    @UiField
    HTMLPanel header;
    @UiField
    HTMLPanel content;

    interface ResourceUtilReportTableUiBinder extends UiBinder<HTMLPanel, ResourceUtilReportTable> {
    }

    public ResourceUtilReportTable(ResourceUtilizationView widgets, MonthDay monthDay, Date date) {
        this.resourceUtilizationView = widgets;
        this.monthDay = monthDay;
        this.date = date;
        ResourceUtilReportTableUiBinder ourUiBinder = GWT.create(ResourceUtilReportTableUiBinder.class);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public void addChildToParent(Element parent, Element child) {
        DOM.appendChild(parent, child);
    }

    public void insertChildToParent(Element parent, Element child, int index) {
        DOM.insertChild(parent, child, index);
    }

    public void generateTable() {
        resourceUtilReportData = new ResourceUtilReportTableData(this, getMonthMaxDay(), getDate(), resourceUtilItem.getMonth_holiday_INT());
        drawInitialize();
        generateEmployeeResourceReport(false);
    }

    public void generateExpandCollapsRows(boolean enableExpand) {
        headerTableElement.removeFromParent();
        contentTableElement.removeFromParent();
        drawInitialize();
        generateEmployeeResourceReport(enableExpand);
    }

    public void generateTOP(Element monthNameElement) {
        //create top(first) tHead element
        Element tHeadElement = DOM.createTHead();
        addChildToHeaderTable(tHeadElement);

        //register selected/current month days TR
        Element monthTRElement = DOM.createTR();
        addChildToParent(tHeadElement, monthTRElement);

        Element monthNameTHElement = DOM.createTH();
        monthNameTHElement.addClassName("firstColCell");
        addChildToParent(monthTRElement, monthNameTHElement);
        addChildToParent(monthNameTHElement, monthNameElement);
        //register month days elements
        generateMonthDays(monthTRElement, true);
    }

    public void generateBottom() {
        //create bottom(last) tFoot element
        Element tFoot = DOM.createTFoot();
        addChildToContentTable(tFoot);
        //register TR element
        Element trElement = DOM.createTR();
        addChildToParent(tFoot, trElement);
        //register TD element
        Element tdElement = DOM.createTD();
        addChildToParent(trElement, tdElement);
        //register month days elements
        generateMonthDays(trElement, false);
    }

    public void removeResourceUtilTable() {
        //remove table option
        Element headerTable = DOM.getChild(header.getElement(), 0);
        if (headerTable != null) {
            DOM.removeChild(header.getElement(), headerTable);
        }
        Element contentTable = DOM.getChild(content.getElement(), 0);
        if (contentTable != null) {
            DOM.removeChild(content.getElement(), contentTable);
        }
    }

    public ResourceUtilReportTableData getResourceUtilReportData() {
        return resourceUtilReportData;
    }

    public ResourceUtilizationView getResourceUtilizationView() {
        return resourceUtilizationView;
    }

    public MonthDay getMonthDay() {
        return monthDay;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public int getMonthMaxDay() {
        return monthMaxDay;
    }

    public void setMonthMaxDay(int monthMaxDay) {
        this.monthMaxDay = monthMaxDay;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void setResourceUtilItem(ResourceUtilItem resourceUtilItem) {
        this.resourceUtilItem = resourceUtilItem;
    }

    private void addChildToContentTable(Element child) {
        DOM.appendChild(contentTableElement, child);
    }

    private void addChildToHeaderTable(Element child) {
        DOM.appendChild(headerTableElement, child);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private void drawInitialize() {
        //register
        headerTableElement = DOM.createTable();
        headerTableElement.addClassName(CLASS_RESOURCE_UTIL_T);
        DOM.setElementProperty(headerTableElement, "cellSpacing", "0");
        DOM.setElementProperty(headerTableElement, "cellPadding", "0");
        DOM.appendChild(header.getElement(), headerTableElement);
        if (getResourceUtilizationView().isFromProjectSummary()) {
            RootPanel.getBodyElement().addClassName("has-rsrcUtlzLayout__filters");
        }

        contentTableElement = DOM.createTable();
        contentTableElement.setId(CLASS_RESOURCE_UTILREPORT_BIG_Table);
        contentTableElement.addClassName(CLASS_RESOURCE_UTIL_T);
        DOM.setElementProperty(contentTableElement, "cellSpacing", "0");
        DOM.setElementProperty(contentTableElement, "cellPadding", "0");
        DOM.appendChild(content.getElement(), contentTableElement);
    }

    private void generateEmployeeResourceReport(boolean enableExpand) {
        EmployeeResourceUtilItem[] employeeResourceUtilItems = resourceUtilItem.getEmployeeResourceUtilItems();
        for (EmployeeResourceUtilItem employeeResourceUtilItem : employeeResourceUtilItems) {
            Element innerElementTBody = DOM.createTBody();
            addChildToContentTable(innerElementTBody);
            resourceUtilReportData.generateEmployeeReport(innerElementTBody, employeeResourceUtilItem, enableExpand);
        }
    }

    private void generateMonthDays(Element parentTRElement, boolean isTop) {
        resourceUtilReportData.generateMonthDays(parentTRElement, getCurrentDay(), isTop);
    }
}
