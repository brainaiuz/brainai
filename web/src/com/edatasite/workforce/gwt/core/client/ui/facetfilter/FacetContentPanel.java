package com.edatasite.workforce.gwt.core.client.ui.facetfilter;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: dilsh0d
 * Date: 20/03/12
 * Time: 19:16
 * To change this template use File | Settings | File Templates.
 */
public class FacetContentPanel extends FocusPanel {

    private boolean selectedIndex = false;
    private String facetCodeName;
    private MaterialIcon close;
    private Image warning;
    private HTML captionLabel;
    private KpiCheckBox capCheck;
    private FacetList facetList;
    private FacetSettingRpc setting;
    private ArrayList<HandlerRegistration> eventHandleList = new ArrayList<>();

    public FacetContentPanel(String captionName, FlexTable datePeriodContent, String facetCodeName) {
        this.facetCodeName = facetCodeName;

        MaterialPanel pnlHeader = new MaterialPanel("panel__heading");
        captionLabel = new HTML(captionName);
        captionLabel.setStyleName("panel__title");
        pnlHeader.add(captionLabel);

        MaterialPanel pnlBody = new MaterialPanel("panel__body");
        pnlBody.add(datePeriodContent);

        MaterialPanel facetContent = new MaterialPanel("panel panel--facet-filters");
        facetContent.add(pnlHeader);
        facetContent.add(pnlBody);
        add(facetContent);
    }


    public FacetContentPanel(String facetCodeName, String captionName) {
        this.facetCodeName = facetCodeName;
        initialization(captionName);
    }

    private void initialization(String captionName) {
        capCheck = new KpiCheckBox();

        captionLabel = new HTML(captionName);
        captionLabel.setStyleName("panel__title");

        close = new MaterialIcon();
        close.setClass("close");

        warning = new Image("/mainStyles/icons/exclamation.png");
        warning.addClickHandler(clickEvent -> Info.show("This column has more than 250 unique items. Only first 250 unique items are displayed.", Info.Type.WARNING));
        showWarningImage(false);

        MaterialPanel pnlHeader = new MaterialPanel("panel__heading");
        pnlHeader.add(capCheck);
        pnlHeader.add(captionLabel);
        pnlHeader.add(close);
        pnlHeader.add(warning);

        facetList = new FacetList();
        facetList.setStyleName("panel__body check-list");

        MaterialPanel facetContent = new MaterialPanel();
        facetContent.addStyleName("panel panel--facet-filters");
        facetContent.add(pnlHeader);
        facetContent.add(facetList);
        add(facetContent);
    }

    public void showWarningImage(boolean show) {
        warning.getElement().getStyle().setDisplay(show ? Style.Display.INLINE : Style.Display.NONE);
        warning.getElement().getStyle().setVisibility(show ? Style.Visibility.VISIBLE : Style.Visibility.HIDDEN);
    }

    public String getFacetCodeName() {
        return facetCodeName;
    }

    public HTML getCaptionLabel() {
        return captionLabel;
    }

    public KpiCheckBox getCapCheck() {
        return capCheck;
    }

    public FacetList getFacetList() {
        return facetList;
    }

    public boolean isSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(boolean value) {
        this.selectedIndex = value;
    }

    public void setSetting(FacetSettingRpc setting) {
        this.setting = setting;
    }

    public FacetSettingRpc getSetting() {
        return setting;
    }

    public void setPanelPosition(int row, int cell) {
        setting.setRow(row);
        setting.setCell(cell);
    }

    public void addCloseHandler(ClickHandler clickHandler) {
        eventHandleList.add(close.addClickHandler(clickHandler));
    }

    public void addCapCheckBoxClickHandler(ClickHandler clickHandler) {
        eventHandleList.add(capCheck.addClickHandler(clickHandler));
    }

    public void removeAllListeners() {
        for (HandlerRegistration handlerRegistration : eventHandleList) {
            handlerRegistration.removeHandler();
        }
        eventHandleList.clear();
        facetList.clearClickEvents();
    }
}
