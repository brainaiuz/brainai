package com.edatasite.workforce.gwt.accounting.client.ui.view.widgets;

import com.edatasite.workforce.gwt.accounting.client.rpc.BarcodeGeneratorService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BarcodeItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.ProductNumbering;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;

/**
 * Created by Shohruh on 07 Feb 2017.
 */
public class BarcodeWidget extends Composite implements Constants{
    private BarcodeWidgetInterface barcodeInterface;
    private BarcodeItem barcodeItem;

    private Image image;
    private String text;

    public interface BarcodeWidgetInterface {
        ProductNumbering getNumberWidget();
        NewProduct getProduct();
    }

    public BarcodeWidget(BarcodeWidgetInterface barcodeInterface) {
        this.barcodeInterface = barcodeInterface;
        initialize();
        generateBarCode();
    }

    private void drawUI() {
        image.setUrl(barcodeItem.getBarcodeUrl());
        image.setWidth(barcodeItem.getWidth());
    }

    private void initialize() {
        image = new Image();
        initWidget(image);
        if (barcodeInterface.getNumberWidget() != null) {
            barcodeInterface.getNumberWidget().getTxtNumber().addBlurHandler(blurEvent -> generateBarCode());
        }
    }

    private void generateBarCode() {
        String text = getPrefixText() + getNumberText();
        if (text == null || "".equals(text)) {
            text = barcodeInterface.getProduct().getNumberData().getNumberString();
        }
        if (!text.equals(this.text)) {
            this.text = text;
        } else {
            return;
        }
        BarcodeItem item = new BarcodeItem();
        item.setProductId(barcodeInterface.getProduct().getObjectId());
        item.setBarcodeNumber(text);
        BarcodeGeneratorService.App.get().generateBarcode(item, new AbstractAsyncCallback<BarcodeItem>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(BarcodeItem result) {
                if (result == null) {
                    changeStyle("x-form-invalid");
                    return;
                } else {
                    changeStyle("");
                }
                barcodeItem = result;
                drawUI();
                setNumberText(result.getBarcodeNumber());
            }
        });
    }

    private void changeStyle(String style) {
        if (barcodeInterface.getNumberWidget() != null) {
            barcodeInterface.getNumberWidget().getTxtNumber().setStyleName("");
        }
    }

    private String getNumberText() {
        return barcodeInterface.getNumberWidget() != null ? barcodeInterface.getNumberWidget().getTxtNumber().getText() : "";
    }

    private void setNumberText(String formattedText) {
        String text = getNumberText() + getChecksum(formattedText);
        if (barcodeInterface.getNumberWidget() != null) {
            barcodeInterface.getNumberWidget().getTxtNumber().setText(text);
        }
    }

    private String getPrefixText() {
        return barcodeInterface.getNumberWidget() != null ? barcodeInterface.getNumberWidget().getTxtPrefix().getText() : "";
    }

    public Integer getIntNumber() {
        String text = getNumberText();
        return Integer.valueOf(text.substring(0, text.length() - getChecksum(text).length()));
    }

    public String getChecksum() {
        return getChecksum(getNumberText());
    }

    private String getChecksum(String text) {
        String checksum = "";
        if(text.length() > 0 && barcodeItem != null &&
                (BarcodeType.EAN8.equals(barcodeItem.getType()) || BarcodeType.EAN13.equals(barcodeItem.getType())
                        || BarcodeType.EAN128.equals(barcodeItem.getType()) || BarcodeType.UPCA.equals(barcodeItem.getType()) || BarcodeType.UPCE.equals(barcodeItem.getType()))) {
            checksum = text.substring(text.length()-1);
        }
        return checksum;
    }

    public Integer getBarcodeId() {
        return barcodeItem != null ? barcodeItem.getUploadId() : barcodeInterface.getProduct().getBarcodeID();
    }
}
