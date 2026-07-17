package com.edatasite.workforce.gwt.core.client.ui.dialogBox;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialDialog;
import gwt.material.design.client.ui.MaterialDialogContent;
import gwt.material.design.client.ui.MaterialDialogFooter;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

/**
 * User: Jamshid Asatillayev
 * Date: 17-Sep-2010
 * Time: 23:02:21
 */
public class KpiModal extends MaterialDialog {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected static final ProjectStrings projectStrings = ProjectStrings.App.get();
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    private ScrollPanel scrollPanel;            //container scroll
    private Span modalTitle;                    //title
    private Span loader;
    protected MaterialPanel modalHeader;
    protected MaterialDialogContent modalContent; //container
    protected MaterialDialogFooter modalFooter;  //footer

    private WfmButton2 closeBtn;          //closeBtn
    private boolean isScrollable = false, hasCloseButton = true;
    private Div wrapper = null;

    private boolean headerFooter = true;

    public KpiModal(boolean headerFooter) {
        super();
        this.headerFooter = headerFooter;
        drawModal();
    }

    public KpiModal() {
        super();
        drawModal();
    }

    private void drawModal() {
        modalTitle = new Span();
        modalContent = new MaterialDialogContent();
        modalFooter = new MaterialDialogFooter();
//        modalFooter.addStyleName("dropdown-split--top"); // it looks like unusible
        modalHeader = new MaterialPanel();
        modalHeader.setStyleName("modal-header");
        modalHeader.add(modalTitle);
        loader = new Span();
        loader.setStyleName("");// will be css here
        modalHeader.add(loader);
        loader.setVisible(false);
        wrapper = new Div("modal-wrapper convert-lead form-compressed");
        if (headerFooter) {
            wrapper.add(modalHeader);
        }
        wrapper.add(modalContent);
        if (headerFooter) {
            wrapper.add(modalFooter);
        }

        super.add(wrapper);

        overlayCloseHandler();

        try {
            Utils.hideDropDownLookUp();
        } catch (Exception ignored) {
        }
    }

    public void setTitle(String title) {
        modalTitle.setText((!Utils.isNullOrEmpty(title) ? title : wfmStrings.confirmation()));
    }

    public void setTitleWithLoader(String title, boolean showLoader) {
        modalTitle.setText((!Utils.isNullOrEmpty(title) ? title : wfmStrings.confirmation()));
        loader.setVisible(showLoader);
    }

    @Override
    public void open() {
        RootPanel.get().add(this);
        RootPanel.getBodyElement().addClassName("has-modal-open");
        super.open();
    }

    @Override
    public void close() {
        super.close();
        RootPanel.getBodyElement().removeClassName("has-modal-open");
        removeModalFromParent();
    }

    private void drawCloseBtn() {
        if (closeBtn == null) {
            closeBtn = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
            closeBtn.addClickHandler(event -> {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DOCUMENT_GROUPS_POPUP_CLOSED, null, null);
                close();
            });
            modalFooter.add(closeBtn);
        }
    }

    public void drawCloseBtn(ClickHandler clickHandler) {
        this.hasCloseButton = true;
        if (closeBtn == null) {
            closeBtn = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
            closeBtn.addClickHandler(clickHandler);
            modalFooter.add(closeBtn);
        }
    }

    public void center() {
        if (!isShowing()) {
            RootPanel.get().add(this);

            super.open();
        }
    }

    public boolean isShowing() {
        return Display.BLOCK.getCssName().equals(getElement().getStyle().getDisplay());
    }

    public void setCloseButton(boolean hasClose) {
        this.hasCloseButton = hasClose;
        if (this.hasCloseButton) {
            drawCloseBtn();
        }
    }

    @Override
    public void add(Widget widget) {
        modalContent.add(widget);
    }

    public void addWidget(Widget widget, String label) {
        add(new FormGroup(label, widget));
    }

    public void addButton(Widget w) {
        modalFooter.add(w);
    }

    public void clearContent() {
        modalContent.clear();
    }

    public void clearFooter() {
        modalFooter.clear();
    }

    public void clearAndAdd(Widget w) {
        clearContent();
        add(w);
    }

    public void removeCloseBtn() {
        if (getFooter().getWidgetIndex(closeBtn) != -1) {
            getFooter().remove(closeBtn);
        }
    }

    public void removeModalFromParent() {
        this.removeFromParent();
    }

    public void setHeight(int height) {
        setHeight(height + "px");
        if (isScrollable) {
            scrollPanel.setHeight(height + "px");
        }
    }

    public void setWidth(int width) {
        setWidth(width + "px");
        if (isScrollable) {
            scrollPanel.setWidth(width + "px");
        }
    }

    public void setWidthAndHeight(int widht, int height) {
        setWidth(widht);
        setHeight(height);
    }

    public void setWidth(String width) {
        wrapper.setWidth(width);
    }

    public void setSize(int width, int height) {
        setHeight(height);
        setWidth(width);
        if (isScrollable) {
            scrollPanel.setSize(width + "px", height + "px");
        }
    }

    public void overlayCloseHandler() {
        addCloseHandler(closeEvent -> removeModalFromParent());
    }

    public ScrollPanel getScrollPanel() {
        return scrollPanel;
    }

    public MaterialDialogContent getContent() {
        return modalContent;
    }

    public MaterialDialogFooter getFooter() {
        return modalFooter;
    }

    /**
     * To make DialogBox scrollable,first set its necessary width and/or height!
     *
     * @param isScrollable - for making scrollable
     */
    public void setScrollable(boolean isScrollable) {
        scrollPanel = new ScrollPanel(modalContent);
        this.isScrollable = isScrollable;
        if (isScrollable) {
            if (getInDuration() > 0) {
                scrollPanel.setHeight(getInDuration() + "px");
            }
            if (getOutDuration() > 0) {
                scrollPanel.setWidth(getOutDuration() + "px");
            }
            if (getInDuration() <= 0 && getOutDuration() <= 0) {
                throw new IllegalStateException("To make DialogBox scrollable,first set its necessary width and/or height!");
            } else {
                wrapper.remove(modalContent);
                wrapper.insert(scrollPanel, wrapper.getWidgetIndex(getFooter()));
            }
        } else {
            int scrollIndex = wrapper.getWidgetIndex(scrollPanel);
            wrapper.remove(scrollPanel);
            wrapper.insert(modalContent, scrollIndex);
        }
    }

    public void addToWrapper(Widget widget) {
        if (widget == null) {
            return;
        }
        wrapper.add(widget);
    }

    public Div getWrapper() {
        return wrapper;
    }

    public MaterialPanel getModalHeader() {
        return modalHeader;
    }
}
