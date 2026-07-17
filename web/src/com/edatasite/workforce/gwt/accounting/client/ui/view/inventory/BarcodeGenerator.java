package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialLink;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/21/11
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class BarcodeGenerator implements AccountingConstants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accuntingStrings = AccountingStrings.App.get();

    private TextBox barcodeTxtBox;
    private DataListBox QRCodeSize;
    private MaterialLink barcodeDownload;

    private final String barcodeGenerator = "barcode_generator_";
    private final String type; // barcode or qrcode

    private MaterialLink barcodeGenerate;
    private Image imgBarcodeImage;

    public TextBox getBarcodeTxtBox() {
        return barcodeTxtBox;
    }

    public void setBarcodeTxtBox(TextBox barcodeTxtBox) {
        this.barcodeTxtBox = barcodeTxtBox;
    }

    public BarcodeGenerator() {
        this(QRCODE);
    }

    public BarcodeGenerator(String type) {
        this.type = type;
        initialize();
    }

    private void initialize() {
        barcodeTxtBox = new TextBox();
        barcodeTxtBox.ensureDebugId(barcodeGenerator + "barcodeTxtBox");
        barcodeTxtBox.setWidth("100%");
        barcodeTxtBox.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        barcodeTxtBox.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        barcodeTxtBox.getElement().getStyle().setBorderColor("#e1e5ec");
        if (BARCODE.equals(type)) {
            Validation.addNumericKeyboardListener(barcodeTxtBox);
        }

        QRCodeSize = new DataListBox();
        QRCodeSize.ensureDebugId(barcodeGenerator + "QRCodeSize");
        QRCodeSize.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        QRCodeSize.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        QRCodeSize.getElement().getStyle().setBorderColor("#e1e5ec");
        QRCodeSize.setItems(QR_CODE_SIZES);
        QRCodeSize.setSelected(SMALL_SIZE);
        QRCodeSize.setWidth("100%");

        barcodeDownload = new MaterialLink();
        barcodeDownload.ensureDebugId(barcodeGenerator + "barcodeDownload");
        barcodeDownload.setStyleName(WfmButton2.BTN_SUCCESS);
        barcodeDownload.setTarget("blank");

        imgBarcodeImage = new Image();
        imgBarcodeImage.ensureDebugId(barcodeGenerator + "imgBarcodeImage");
//        imgBarcodeImage.setUrl("/ps_templates/image/barcode.png");

        barcodeGenerate = new MaterialLink(wfmStrings.generate());
        barcodeGenerate.ensureDebugId(barcodeGenerator + "barcodeGenerate");
        barcodeGenerate.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
        barcodeGenerate.getElement().getStyle().setBorderColor("#ced5db");
        barcodeGenerate.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
        barcodeGenerate.setStyleName(WfmButton2.BTN_PRIMARY);
        barcodeGenerate.addClickHandler(event -> {
            if (!Validation.validateTextBoxRequired(barcodeTxtBox)) {
                return;
            }
            if (barcodeTxtBox.getText().length() < 2) {
                Info.show(accuntingStrings.youShouldEnterAtLeast2charactersToGenerateBarcode(), Info.Type.WARNING);
                return;
            }
            int barcodeSize;
            switch (QRCodeSize.getSelectedId()) {
                case SMALL_SIZE:
                    barcodeSize = SMALL;
                    break;
                case MEDIUM_SIZE:
                    barcodeSize = MEDIUM;
                    break;
                case LARGE_SIZE:
                    barcodeSize = LARGE;
                    break;
                default:
                    barcodeSize = SMALL;
            }
            generateBarCode(barcodeTxtBox.getText(), barcodeSize);
        });
        imgBarcodeImage.setVisible(false);
        barcodeDownload.setVisible(false);
    }

    public void generateBarCode(String barCodeText, int barcodeSize) {
        if (type.equals(QRCODE)) {
            imgBarcodeImage.setUrl("https://chart.googleapis.com/chart?chs=" + barcodeSize + "x" + barcodeSize + "&cht=qr&chl=" + barCodeText);
            barcodeDownload.setHref("https://chart.googleapis.com/chart?chs=150x150&cht=qr&chl=" + barCodeText);
        } else {
            imgBarcodeImage.setUrl("https://mobiledemand-barcode.azurewebsites.net/barcode/image?content=" + barCodeText + "&size=50&symbology=CODE_128&format=png&text=false");
            barcodeDownload.setHref("https://mobiledemand-barcode.azurewebsites.net/barcode/image?content=" + barCodeText + "&size=50&symbology=CODE_128&format=png&text=false");
        }
        barcodeDownload.setText(accuntingStrings.openInNewWindow());
        imgBarcodeImage.setVisible(true);
        barcodeDownload.setVisible(true);
    }

    public FlowPanel createWidget() {
        return createWidget(true);
    }

    public FlowPanel createWidget(boolean generateButton) {
        FlowPanel panel = new FlowPanel();

        if (generateButton) {
            GRow formRow = new GRow();
            GColumn barcodeCol = new GColumn(GColumnEnum.COL_8);
            barcodeCol.add(barcodeTxtBox);
            formRow.add(barcodeCol);

            GColumn generateCol = new GColumn(GColumnEnum.COL_4);
            generateCol.add(barcodeGenerate);
            formRow.add(generateCol);
            panel.add(formRow);
        }

        GRow imgRow = new GRow();
        imgRow.setPadding(10);
        imgRow.add(imgBarcodeImage);

        GRow downloadRow = new GRow();
        GColumn downloadCol = new GColumn(GColumnEnum.COL_6);
        downloadCol.add(barcodeDownload);
        downloadRow.add(downloadCol);

        panel.add(imgRow);
        panel.add(downloadRow);

        return panel;
    }

    public FlexTable createImageWidget() {
        FlexTable table = new FlexTable();
        table.setWidget(0, 0, imgBarcodeImage);
        table.setWidget(1, 0, barcodeDownload);
        return table;
    }

    public void setData(String barcode, Integer qrCodeSizeID) {
        barcodeTxtBox.setText(barcode);
        if (barcode != null && barcode.length() > 1) {
            QRCodeSize.setSelected(qrCodeSizeID == null ? SMALL_SIZE : qrCodeSizeID);
            int barcodeSize;
            switch (QRCodeSize.getSelectedId()) {
                case SMALL_SIZE:
                    barcodeSize = SMALL;
                    break;
                case MEDIUM_SIZE:
                    barcodeSize = MEDIUM;
                    break;
                case LARGE_SIZE:
                    barcodeSize = LARGE;
                    break;
                default:
                    barcodeSize = SMALL;
                    break;
            }
            generateBarCode(barcode, barcodeSize);
        } else {
            QRCodeSize.setSelected(SMALL_SIZE);
        }
    }

    public String getBarcodeText() {
        return barcodeTxtBox.getText();
    }

    public Integer getQRCodeSizeID() {
        if (barcodeTxtBox.getText().length() >= 2)
            return QRCodeSize.getSelectedId();
        return null;
    }

    public void setEnabled(boolean enabled) {
        barcodeTxtBox.setEnabled(enabled);
        QRCodeSize.setEnabled(enabled);
        barcodeGenerate.setVisible(enabled);
        barcodeDownload.setEnabled(enabled);
    }

}
