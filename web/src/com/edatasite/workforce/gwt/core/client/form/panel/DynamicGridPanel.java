package com.edatasite.workforce.gwt.core.client.form.panel;

import com.edatasite.workforce.gwt.core.client.form.DynamicField2;
import com.edatasite.workforce.gwt.core.client.form.DynamicGridFormHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

/**
 * User: Abror Abdukadirov
 * Date: 09.08.2019 17:52
 */
public class DynamicGridPanel extends Composite {
    interface DynamicPanelUiBinder extends UiBinder<Widget, DynamicGridPanel> {
    }
    private static DynamicPanelUiBinder ourUiBinder = GWT.create(DynamicPanelUiBinder.class);
    private HashMap<String, DynamicGridItemPanel> gridItemMap = new HashMap<>();

    @UiField
    HTMLPanel mainPanel;

    private JavaScriptObject gridObject;
    private String elementId;
    private String sectionCode;
    private DynamicGridFormHelper formHelper;

    public DynamicGridPanel(String sectionCode, DynamicGridFormHelper formHelper) {
        this.sectionCode = sectionCode;
        this.formHelper = formHelper;
        initWidget(ourUiBinder.createAndBindUi(this));
        this.elementId = DOM.createUniqueId();
        mainPanel.getElement().setId(this.elementId);
        this.initialize(this.elementId, DynamicPanelJavaScriptObject.createOptions());
    }

    public void addNewItem(DynamicGridItemPanel itemPanel, boolean autoPosition) {
        gridItemMap.put(itemPanel.getElementId(), itemPanel);

        itemPanel.setAddedCommand(() -> this.formHelper.itemAdded(itemPanel));

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

    public void removeAllItem() {
        gridItemMap.clear();

        removeAllItemNative(mainPanel.getElement());

        commitNative(mainPanel.getElement());
    }

    private void addAndUpdateField(String itemId, String x, String y, String width) {
        DynamicGridItemPanel gridItem = this.formHelper.getItemById(itemId);

        DynamicField2 field = gridItem.getField();

        if (!field.isActive()) {
            field.setActive(true);
        }
        field.setSection(this.sectionCode);
        gridItem.setX(Integer.parseInt(x));
        gridItem.setY(Integer.parseInt(y));
        gridItem.setWidth(Integer.parseInt(width));
        this.addNewItem(gridItem, true);
    }

    private void removeDroppedItem(String itemId, Boolean hasInactive) {
        this.formHelper.removeDroppedItem(itemId, hasInactive);
    }

    public void removeItem(DynamicGridItemPanel itemPanel) {
        gridItemMap.remove(itemPanel.getElementId());

        removeItemNative(mainPanel.getElement(), itemPanel.getElement());

        commitNative(mainPanel.getElement());
    }

    private void setGridObject(JavaScriptObject gridObject) {
        this.gridObject = gridObject;
    }

    private native void initialize(String elementId, DynamicPanelJavaScriptObject options) /*-{
      var that = this;
      $wnd.$(function () {
        $wnd.$('#' + elementId).gridstack(options);

        var currGrid = $wnd.$('#' + elementId).data('gridstack');

        that.@com.edatasite.workforce.gwt.core.client.form.panel.DynamicGridPanel::setGridObject(Lcom/google/gwt/core/client/JavaScriptObject;)(currGrid);

        var currId = that.@com.edatasite.workforce.gwt.core.client.form.panel.DynamicGridPanel::elementId;

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

            var hasInactive = panel[0].classList.contains('grid-stack-inactive') > -1

            var cell = newGrid.getCellFromPixel(ui.draggable.position);
            if(typeof(ui.position) !== 'undefined' && ui.position != null){
              cell = newGrid.getCellFromPixel(ui.position);
            }
            var cellWidth = (ui.draggable[0].dataset.gsWidth || 2);
            if (currGrid.willItFit(cell.x, cell.y, +cellWidth, 1, true)) {

              newGrid.removeWidget(ui.draggable, false);

              that.@com.edatasite.workforce.gwt.core.client.form.panel.DynamicGridPanel::removeDroppedItem(Ljava/lang/String;Ljava/lang/Boolean;)(ui.draggable.attr('id'), hasInactive);

              that.@com.edatasite.workforce.gwt.core.client.form.panel.DynamicGridPanel::addAndUpdateField(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(ui.draggable.attr('id'), cell.x, cell.y, cellWidth);

              if (hasInactive) {
                currGrid.resizable('#' + currId + ' .grid-stack-item', true);
              }
            }
            else {
              alert('Not enough free space to add the field');
            }
          }
        });
      });
    }-*/;

    public void commit() {
        commitNative(mainPanel.getElement());
    }

    public boolean hasInjected() {
        return hasInjectedNative(mainPanel.getElement());
    }

    public boolean hasWillItFit() {
        return hasWillItFitNative(mainPanel.getElement());
    }

    private native boolean hasInjectedNative(Element mainPanel) /*-{
      var grid = $wnd.$(mainPanel).data('gridstack');
      return grid !== undefined;
    }-*/;

    private native boolean hasWillItFitNative(Element mainPanel) /*-{
      var grid = $wnd.$(mainPanel).data('gridstack');
      return grid.willItFit(0, 0, 3, 1, true);
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

    public HashMap<String, DynamicGridItemPanel> getGridItemMap() {
        return gridItemMap;
    }

    public HTMLPanel getMainPanel() {
        return mainPanel;
    }
}
