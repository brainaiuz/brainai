package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Italic;

/**
 * Created by Anvar Akramov on 12/14/17.
 */
public class ProductFile extends MaterialPanel {

    private ProductPicture productPicture;
    Italic iconClose = new Italic("");
    Div progressIndicator = new Div("btn-upload__indicator active");
    private Image image;
    private KpiRadioButton radioButton;
    private boolean done = false;
    /**
     * <div class="btn-uploaded-img btn-uploaded--has-control">
     * <div class="btn-uploaded__control">
     * <div class="control control--checkbox">
     * <input type="checkbox" checked="checked"/>
     * <span class="control__indicator"></span>
     * </div>
     * </div>
     * <img src="http://via.placeholder.com/350x150"/>
     * <i class="close"></i>
     * </div>
     **/
    public ProductFile(ProductPicture fileResource) {
        super();
        setClass("btn-uploaded-img btn-uploaded--has-control");
        Div uControl = new Div("btn-uploaded__control");
//        Div radioDiv = new Div("control control--radio");
        radioButton = new KpiRadioButton("productPicture");
        image = new Image();
//        image.setWidth("17px");
//        image.setHeight("17px");
//        radioDiv.add(radioButton);
        uControl.add(radioButton);
        add(progressIndicator);
        if (fileResource != null) {
            setFile(fileResource);
        } else {
            progressIndicator.setVisible(true);
        }
        iconClose.setClass("close");
        add(uControl);
        add(image);
        add(iconClose);
    }

    public ProductPicture getPicture() {
        return productPicture;
    }

    public void setFile(ProductPicture productPicture) {
        this.productPicture = productPicture;
        radioButton.setValue(productPicture.isDefaultPicture());
        image.setUrl(productPicture.getUrl());
//        image.setWidth("90px");
        image.setHeight("48px");
        progressIndicator.removeFromParent();
    }

    public void addCloseHandler(ClickHandler clickHandler) {
        iconClose.addClickHandler(clickHandler);
    }

    public void addDefulatRadioButtonHandler(ClickHandler clickHandler) {
        radioButton.addClickHandler(clickHandler);
    }

    public boolean isSelectedAsDefault() {
        return radioButton.getValue();
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDone() {
        return done;
    }
}
