package com.edatasite.workforce.gwt.core.client.form.panel;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.DynamicField2;
import com.edatasite.workforce.gwt.core.client.form.DynamicGridFormHelper;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Span;

import java.util.HashMap;

/**
 * User: Abror Abdukadirov
 * Date: 09.08.2019 18:22
 */
public class DynamicGridInactivePanel extends Composite {
    interface DynamicInactivePanelUiBinder extends UiBinder<Widget, DynamicGridInactivePanel> {
    }
    private static DynamicInactivePanelUiBinder ourUiBinder = GWT.create(DynamicInactivePanelUiBinder.class);
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private HashMap<String, DynamicGridItemPanel> gridItemMap = new HashMap<>();

    @UiField
    HTMLPanel wrapper;
    @UiField
    Span title;
    @UiField
    HTMLPanel mainPanel;

    private String sectionCode;
    private String elementId;
    private DynamicGridFormHelper formHelper;

    public DynamicGridInactivePanel(String section, DynamicGridFormHelper formHelper) {
        this.sectionCode = section;
        this.formHelper = formHelper;
        initWidget(ourUiBinder.createAndBindUi(this));
        this.initialize();
    }

    private void initialize() {
        this.title.setText(wfmStrings.unusedFields());
        this.elementId = DOM.createUniqueId();
        this.mainPanel.getElement().setId(this.elementId);
        this.wrapper.getElement().getStyle().setDisplay(Style.Display.NONE);
        this.initializeGrid(this.elementId, DynamicInactivePanelJavaScriptObject.createOptions());
    }

    public void addNewItem(DynamicGridItemPanel itemPanel, boolean autoPosition) {
        gridItemMap.put(itemPanel.getElementId(), itemPanel);

        DynamicJavaScriptItem scriptItem = DynamicJavaScriptItem.create();
        scriptItem.setX(itemPanel.getX());
        scriptItem.setY(itemPanel.getY());
        scriptItem.setWidth(itemPanel.getWidth());
        scriptItem.setMinWidth(itemPanel.getMinWidth());
        scriptItem.setHeight(itemPanel.getHeight());
        scriptItem.setMinHeight(itemPanel.getMinHeight());
        scriptItem.setFieldId(itemPanel.getFieldId());

        addItemNavite(mainPanel.getElement(),
                      itemPanel.getElement(),
                      itemPanel.getX(),
                      itemPanel.getY(),
                      itemPanel.getWidth(),
                      itemPanel.getHeight(),
                      autoPosition);

        if (hasInjected()) {
            mainPanel.add(itemPanel);
        } else {
            Timer timer = new Timer() {
                @Override
                public void run() {
                    mainPanel.add(itemPanel);
                }
            };
            timer.schedule(3000);
        }
    }

    private void addAndUpdateField(String itemId, String x, String y, String width) {
        DynamicGridItemPanel gridItem = this.formHelper.getItemById(itemId);

        DynamicField2 field = gridItem.getField();
        field.setActive(false);
        field.setSection(this.sectionCode);
        gridItem.setX(Integer.parseInt(x));
        gridItem.setY(Integer.parseInt(y));
        gridItem.setWidth(Integer.parseInt(width));
        this.addNewItem(gridItem, true);
    }

    private void removeDroppedItem(String itemId, Boolean hasInactive) {
        this.formHelper.removeDroppedItem(itemId, hasInactive);
    }

    public void commit() {
        commitNative(mainPanel.getElement());
    }

    public boolean hasInjected() {
        return hasInjectedNative(mainPanel.getElement());
    }

    public String getElementId() {
        return elementId;
    }

    public HashMap<String, DynamicGridItemPanel> getGridItemMap() {
        return gridItemMap;
    }

    private native boolean hasInjectedNative(Element mainPanel) /*-{
      var grid = $wnd.$(mainPanel).data('gridstack');
      return grid !== undefined;
    }-*/;

    private native void addItemNavite(Element mainPanel, Element item, int x, int y, int width, int height, boolean auto) /*-{
      var grid = $wnd.$(mainPanel).data('gridstack');
      if (grid) {
        grid.addWidget($wnd.$(item), x, y, width, height, auto);
      } else {
        setTimeout(function () {
          var grid = $wnd.$(mainPanel).data('gridstack');
          grid.addWidget($wnd.$(item), x, y, width, height, auto);
        }, 3000);
      }
    }-*/;

    private native void removeItemNative(Element mainPanel, Element item) /*-{
      var grid = $wnd.$(mainPanel).data('gridstack');

      grid.removeWidget($wnd.$(item), true);
    }-*/;

    private native void removeAllItemNative(Element mainPanel) /*-{
      var grid = $wnd.$(mainPanel).data('gridstack');

      grid.removeAll(true);
    }-*/;

    private native void commitNative(Element mainPanel) /*-{
      var grid = $wnd.$(mainPanel).data('gridstack');
      if (grid) {
        grid.commit();
      } else {
        setTimeout(function () {
          var grid = $wnd.$(mainPanel).data('gridstack');
          grid.commit();
        }, 3000);
      }
    }-*/;

    private native void initializeGrid(String elementId, DynamicInactivePanelJavaScriptObject options) /*-{
      var that = this;
      $wnd.$(function () {
        $wnd.$('#' + elementId).gridstack(options);

        var currGrid = $wnd.$('#' + elementId).data('gridstack');

        var currId = that.@com.edatasite.workforce.gwt.core.client.form.panel.DynamicGridInactivePanel::elementId;

        currGrid.container.droppable({
          accept: '.grid-stack .grid-stack-item',
          tolerance: 'pointer',
          drop: function(event, ui) {
            if (!ui.draggable) return;

            var panel = ui.draggable.parent();
            if (!panel.attr('id') || currId === panel.attr('id')) {
              return;
            }
            var newGrid = $wnd.$('#' + panel.attr('id')).data('gridstack');
            var cell = newGrid.getCellFromPixel(ui.draggable.position);
            if(typeof(ui.position) !== 'undefined' && ui.position != null){
              cell = newGrid.getCellFromPixel(ui.position);
            }
            var cellWidth = (ui.draggable[0].dataset.gsWidth || 2);
            if (currGrid.willItFit(cell.x, cell.y, +cellWidth, 1, true)) {

              newGrid.removeWidget(ui.draggable, false);

              var hasInactive = panel[0].classList.contains('grid-stack-inactive') > -1
              that.@com.edatasite.workforce.gwt.core.client.form.panel.DynamicGridInactivePanel::removeDroppedItem(Ljava/lang/String;Ljava/lang/Boolean;)(ui.draggable.attr('id'), hasInactive);

              that.@com.edatasite.workforce.gwt.core.client.form.panel.DynamicGridInactivePanel::addAndUpdateField(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(
                  ui.draggable.attr('id'), cell.x, cell.y, cellWidth);

              currGrid.resizable('#' + currId + ' .grid-stack-item', false);
            }
            else {
              alert('Not enough free space to add the field');
            }
          }
        });
      });
    }-*/;
}
