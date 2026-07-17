package com.edatasite.workforce.gwt.core.client.ui.components.sampleData;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;

public class RemoveSDInfo extends KpiModal {

    public RemoveSDInfo() {
        super();
        addStyleName("modal--sample modal-sample-intro modal--sample-image");
        addCloseHandler(ch -> RootPanel.getBodyElement().removeClassName("has-modal--sampledata"));
        initialize();
    }

    private void initialize() {
        modalHeader.clear();

        Heading header = new Heading(HeadingSize.H5);

        String title = "<b>" + Utils.getFirstName() + "</b>, " + wfmMessages.removeSampleData1(Utils.getProductName());
        header.getElement().setInnerHTML(title);
        modalHeader.add(header);

        Div content = new Div();
        content.getElement().setInnerHTML(
                "<p><small>"+wfmMessages.removeSampleData2(Utils.getProductName())+"</small></p>\n" +
                "<p><small>"+wfmMessages.removeSampleData3(Utils.getProductName())+"</small></p>\n" +
                "<p><small>"+wfmMessages.removeSampleData4(Utils.getProductName(),Utils.getProductName())+"</small></p>\n" +
                "<p><small>"+wfmMessages.removeSampleData5()+"</small></p>");

        WfmButton2 btnCancel = new WfmButton2(wfmStrings.ok(), WfmButton2.BTN_GREY);
        btnCancel.addClickHandler(ch -> {
            animateModalClose(getWrapper().getElement(), MainLayout.get().getRemoveSampleLink().getElement());
            close();
        });

        Div btnGroups = new Div("btns-group text-center");
        btnGroups.add(btnCancel);

        modalContent.clear();

        MaterialImage image = new MaterialImage("/mainStyles/new-ui/images/sample-dashboard.png");
        Div imageContainer = new Div("img-holder");
        imageContainer.add(image);

        modalContent.addStyleName("text-center");
        modalContent.add(content);
        modalContent.add(btnGroups);
        modalContent.add(imageContainer);

        modalFooter.removeFromParent();
    }

    private native void animateModalClose(Element wrapper, Element button) /*-{
        $wnd.modal_animation({source: wrapper, target: button, endColorSelector: button});
    }-*/;

    @Override
    public void open() {
        super.open();
        RootPanel.getBodyElement().addClassName("has-modal--sampledata");
    }

    @Override
    public void close() {
        super.close();
        RootPanel.getBodyElement().removeClassName("has-modal--sampledata");
    }
}