package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import gwt.material.design.addins.client.carousel.MaterialCarousel;
import gwt.material.design.addins.client.carousel.constants.CarouselType;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.html.Br;
import gwt.material.design.client.ui.html.Span;

public class ImageViewerSlidePopup extends KpiModal {

    interface ImageViewerSlidePopupUiBinder extends UiBinder<HTMLPanel, ImageViewerSlidePopup> {
    }

    private static ImageViewerSlidePopup.ImageViewerSlidePopupUiBinder ourUiBinder = GWT.create(ImageViewerSlidePopup.ImageViewerSlidePopupUiBinder.class);

    @UiField
    MaterialCarousel carouselBody;

    public ImageViewerSlidePopup(FileResource fileResource, FileResource[] fileResources) {
        super();
        this.getModalHeader().removeFromParent();
        this.addStyleName("has-material-carousel");
        setWidth("800px");
        add(ourUiBinder.createAndBindUi(this));
        addButton(new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT, clickEvent -> close()));

        carouselBody.add(createImageSlide(fileResource));
        carouselBody.setAutoplay(false);
        carouselBody.setInfinite(true);
        carouselBody.setType(CarouselType.IMAGE);
        for (FileResource file : fileResources) {
            if (Utils.isImage(file) && !fileResource.getBodyId().equals(file.getBodyId())) {
                carouselBody.add(createImageSlide(file));
            }
        }
        getFooter().removeFromParent();

    }

    private HTMLPanel createImageSlide(FileResource fileResource) {
        HTMLPanel htmlPanel = new HTMLPanel("");
        Image image = new Image(fileResource.getDownloadUrl());
        image.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        image.addClickHandler(event -> Window.open(fileResource.getDownloadUrl(), "_blank", ""));

        HTMLPanel materialLabels = new HTMLPanel("");
        materialLabels.setStyleName("material-labels");

        HTMLPanel materialLabelsCenter = new HTMLPanel("");
        materialLabelsCenter.setStyleName("material-labels__center");

        materialLabelsCenter.add(createLabel(wfmStrings.name(), fileResource.getName()));
        materialLabelsCenter.add(createLabel(wfmStrings.createdDate(), DateUtils.format(fileResource.getCreationDate())));
        materialLabelsCenter.add(createLabel(wfmStrings.fileSize(), fileResource.getFileSizeAsString()));

        materialLabels.add(materialLabelsCenter);
        htmlPanel.add(image);
        htmlPanel.add(new Br());
        htmlPanel.add(materialLabels);
        return htmlPanel;
    }

    private MaterialLabel createLabel(String title, String value) {
        MaterialLabel label = new MaterialLabel();
        Span span = new Span(title + ": ");

        label.add(span);
        label.add(new HTML(value));
        return label;
    }
}
