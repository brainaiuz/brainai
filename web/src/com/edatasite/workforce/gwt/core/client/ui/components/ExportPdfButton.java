package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.HashMap;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.BTN_DEFAULT_OUTLINE;

public class ExportPdfButton extends MaterialPanel {
    protected WfmStrings wfmStrings = WfmStrings.App.get();
    private PdfRequestInterface pdfRequestInterface;

    public ExportPdfButton(PdfRequestInterface pdfRequestInterface) {
        super();
        this.pdfRequestInterface = pdfRequestInterface;
//        addStyleName("dropdown-kit--arrow--below");
        createPdfButton();
    }

    protected void createPdfButton() {

        MaterialLink pdf = new MaterialLink(wfmStrings.pdfVersion());
        MaterialSplitButton ieLink = new MaterialSplitButton(pdf, BTN_DEFAULT_OUTLINE);
        ieLink.addStyleName("dropdown-split--top");

        add(ieLink);

        if (pdfRequestInterface.isLandscapeOptionEnabled()) {
            pdf.addClickHandler(e -> {
                sendPdfExportRequest(pdfRequestInterface.getParameters());
            });
            MaterialLink landscapeLink = new MaterialLink(wfmStrings.landscape());
            ieLink.addItem(landscapeLink);
            landscapeLink.addClickHandler(e -> {
                HashMap<String, String> parameter = pdfRequestInterface.getParameters();
                parameter.put("IS_LANDSCAPE", "true");
                sendPdfExportRequest(parameter);
            });
        } else {
            addClickHandler(e -> {
                sendPdfExportRequest(pdfRequestInterface.getParameters());
            });
        }
    }

    protected MaterialDropDown createDropDown(MaterialLink ieLink) {
        MaterialDropDown dropDown = new MaterialDropDown(ieLink);
        dropDown.setBelowOrigin(true);
        return dropDown;
    }

    protected MaterialLink getPdfLink() {
        MaterialLink ieLink = new MaterialLink();
        ieLink.addStyleName("btn btn--icon btn--white");
        MaterialIcon icon = new MaterialIcon();
        icon.setStyleName("ficon--file-pdf material-icons");
        ieLink.add(icon);
        return ieLink;
    }

    private void sendPdfExportRequest(HashMap<String, String> parameter) {
        Utils.sendPDFOrExcelRequest(this, CommandConstants.PDF_URL + pdfRequestInterface.getUrl(), parameter, "_blank");
    }

    public interface PdfRequestInterface {
        String getUrl();

        boolean isLandscapeOptionEnabled();

        HashMap<String, String> getParameters();
    }
}
