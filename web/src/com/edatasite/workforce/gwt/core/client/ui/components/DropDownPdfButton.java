package com.edatasite.workforce.gwt.core.client.ui.components;

import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;

public class DropDownPdfButton extends ExportPdfButton {

    public DropDownPdfButton(PdfRequestInterface pdfRequestInterface) {
        super(pdfRequestInterface);
        addStyleName("java-wrap");
    }

    @Override
    protected MaterialLink getPdfLink() {
        MaterialLink pdfLink = super.getPdfLink();
        pdfLink.setText(wfmStrings.pdf());
        return pdfLink;
    }

    @Override
    protected MaterialDropDown createDropDown(MaterialLink ieLink) {
        MaterialDropDown dropDown = super.createDropDown(ieLink);
        dropDown.setHover(true);
        return dropDown;
    }
}
