package com.edatasite.workforce.gwt.profile.client.ui.view.pdf;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfFooteHederAttributeEnum;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfFooterHeaderContentItem;
import com.edatasite.workforce.gwt.profile.client.rpc.PdfHeaderFooterItem;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import gwt.material.design.client.ui.MaterialPanel;

public class PdfFooterHeaderItemWidget extends Composite {
    private final static WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private final String nickDebugId = "add_pdf_footer_header_widget";
    private PdfHeaderFooterItem item;
    private String position;
    private HTML attribute;
    private KpiEditor content;
    private DataListBox attributeList;
    private MaterialPanel descriptionPanel;
    private Boolean isEnable;
    private Integer imageFileId;

    public PdfFooterHeaderItemWidget(String position, Boolean isEnable) {
        this.position = position;
        this.isEnable = isEnable;
        onInitialize();
    }

    private void onInitialize() {

        descriptionPanel = new MaterialPanel();
        MaterialPanel secondRow = new MaterialPanel("grid-row");
        secondRow.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        MaterialPanel contentPanel = new MaterialPanel("col-12");

        content = new KpiEditor(false, true, true);
        content.ensureDebugId(this.nickDebugId + "_content_" + position);
        contentPanel.add(content);
        contentPanel.setHeight("200px");
        secondRow.add(contentPanel);

        descriptionPanel.add(createFirstRow());
        descriptionPanel.add(secondRow);
        initWidget(descriptionPanel);
    }

    private MaterialPanel createFirstRow() {
        MaterialPanel firstRow = new MaterialPanel("grid-row");
        MaterialPanel dropdownPanel = new MaterialPanel("col-6");
        MaterialPanel attributePanel = new MaterialPanel("col-6");
        firstRow.add(dropdownPanel);
        firstRow.add(attributePanel);

        attributeList = new DataListBox();
        attributeList.setItems(getAttributesList());
        attributeList.addValueChangeHandler(changeEvent -> {
            setAttributeValue(changeEvent.getValue());
        });
        dropdownPanel.add(new FormGroup(getNameByPosition(), attributeList));

        attribute = new HTML(wfmStrings.noAttributesSelected());
        attribute.addClickHandler(handler -> {
            if (attributeList.getSelectedItem() != null) {
                Utils.copyToClipBoard(attribute.getHTML());
            }
        });
        attribute.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        attribute.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        attribute.getElement().getStyle().setCursor(Style.Cursor.POINTER);
        attributePanel.add(attribute);

        return firstRow;
    }

    private void setAttributeValue(SelectItem value) {
        PdfFooteHederAttributeEnum attributeValue = null;
        if (value != null && value.getDescription() != null) {
            attributeValue = PdfFooteHederAttributeEnum.valueOf(value.getDescription());
        }
        if (attributeValue != null) {
            attribute.setHTML(attributeValue.getCode());
        } else {
            attribute.setHTML(wfmStrings.noAttributesSelected());
        }
    }


    private SelectItem[] getAttributesList() {

        return new SelectItem[]{
                new SelectItem(1, wfmStrings.companyName(), PdfFooteHederAttributeEnum.COMPANY_NAME.getAttributeName()),
                new SelectItem(2, wfmStrings.companyLogo(), PdfFooteHederAttributeEnum.COMPANY_LOGO.getAttributeName()),
                new SelectItem(3, settingsStrings.companyMainAddress(), PdfFooteHederAttributeEnum.COMPANY_MAIN_ADDRESS.getAttributeName()),
                new SelectItem(4, wfmStrings.website(), PdfFooteHederAttributeEnum.COMPANY_WEBSITE.getAttributeName()),
                new SelectItem(5, settingsStrings.poweredBy(), PdfFooteHederAttributeEnum.POWERED_BY.getAttributeName()),
                new SelectItem(6, settingsStrings.pagination(), PdfFooteHederAttributeEnum.PAGINATION.getAttributeName()),
                new SelectItem(7, settingsStrings.qrCode(), PdfFooteHederAttributeEnum.QR_CODE.getAttributeName()),
                new SelectItem(8, wfmStrings.phone(), PdfFooteHederAttributeEnum.PHONE_NUMBER.getAttributeName()),
                new SelectItem(9, wfmStrings.email(), PdfFooteHederAttributeEnum.EMAIL_ID.getAttributeName()),
                new SelectItem(10, wfmStrings.fax(), PdfFooteHederAttributeEnum.FAX_NUM.getAttributeName()),
                new SelectItem(11, wfmStrings.location() + wfmStrings.address(), PdfFooteHederAttributeEnum.USER_LOCATION_ADRESS.getAttributeName()),
                new SelectItem(12, wfmStrings.location() + wfmStrings.email(), PdfFooteHederAttributeEnum.USER_LOCATION_EMAIL.getAttributeName()),
                new SelectItem(13, wfmStrings.location() + wfmStrings.phone(), PdfFooteHederAttributeEnum.USER_LOCATION_PHONE.getAttributeName()),
                new SelectItem(14, wfmStrings.location() + wfmStrings.postCode(), PdfFooteHederAttributeEnum.USER_LOCATION_ZIP_CODE.getAttributeName()),
//                new SelectItem(15, wfmStrings.imageUpload(), PdfFooteHederAttributeEnum.IMAGE_UPLOAD.getAttributeName())
        };
    }

    private String getNameByPosition() {
        String result = position.substring(7);
        switch (result) {
            case "LEFT":
                return wfmStrings.left();
            case "CENTER":
                return wfmStrings.center();
            case "RIGHT":
                return wfmStrings.right();
        }
        return position;
    }

    public KpiEditor getContent() {
        return content;
    }

    public PdfFooterHeaderContentItem getItem() {
        PdfFooterHeaderContentItem contentItem = new PdfFooterHeaderContentItem(position, Utils.encrypt(content.getData()), isEnable);
        return contentItem;
    }

    public void setItem(PdfFooterHeaderContentItem item) {
        position = item.getPosition();
        isEnable = item.getEnable();
        content.setData(item.getContent());
    }

    public Boolean getEnable() {
        return isEnable;
    }

    public void setEnable(Boolean enable) {
        isEnable = enable;
    }
}
