package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateData;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateItemData;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateTypeData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 9:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class CertificateSummaryView extends CustomForm2 implements Colapse {
    private static final TCStrings tcStrings = TCStrings.App.get();

    private Integer objectID;
    private FlexTable itemsTable;
    private Image certificateTypeTemplateImage;
    private VerticalPanel itemsPanel;
    private HTML numberWidget, student, certificateType;

    public CertificateSummaryView(Integer objectID) {
        super("summary", tcStrings.certificateView());
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {

        itemsTable = new FlexTable();
        certificateTypeTemplateImage = new Image();
        itemsPanel = new VerticalPanel();
        itemsTable.setVisible(false);
        itemsTable.setWidget(0, 0, certificateTypeTemplateImage);
        itemsTable.setWidget(0, 1, itemsPanel);
        certificateTypeTemplateImage.setWidth("500px");
        itemsTable.setCellSpacing(10);
        itemsPanel.setSpacing(2);
        itemsTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        itemsTable.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

        numberWidget = new HTML();
        student = new HTML();
        certificateType = new HTML();

        addTitleField(CustomFormConstants.TITLE, tcStrings.issueCertificate());
        addField(CustomFormConstants.NUMBER, numberWidget, getTitle(wfmStrings.number()));
        addField(CERTIFICATE.STUDENT, student, getTitle(wfmStrings.student()));
        addField(CERTIFICATE.CERTIFICATE_TYPE, certificateType, getTitle(wfmStrings.certificateType()));
        addField(CERTIFICATE.ITEMS_TABLE, itemsTable, null);
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        TCService.App.get().getCertificateData(objectID, false, new AsyncCallback<CertificateData>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CertificateData result) {
                LoadingPanel.loading(false);
                addItemsTable(result);
            }
        });
    }

    private void addItemsTable(CertificateData result) {


        numberWidget.setHTML(result.getCertificateTypeData().getNumberData().getNumberString());
        student.setHTML(result.getStudent());
        certificateType.setHTML(result.getCertificateTypeData().getName());

        CertificateTypeData typeData = result.getCertificateTypeData();
        CertificateItemData[] itemsData = result.getItems();
        if (typeData != null && typeData.getImageURL() != null && typeData.getFieldsCount() > 0) {
            certificateTypeTemplateImage.setUrl(result.getCertificateTypeData().getImageURL());
            for (int i = 0; i < typeData.getFieldsCount(); i++) {
                CertificateItem certificateItem = new CertificateItem();
                if (itemsData != null && itemsData.length > 0) {
                    for (CertificateItemData itemData : itemsData) {
                        if (itemData.getSorder().equals(i)) {
                            certificateItem.getTextBox().setText(itemData.getValues());
                            if (itemData.getColor() != null) {
                                certificateItem.getColorsListBox().setSelected(itemData.getColor());
                            }
                        }
                    }
                }
                itemsPanel.add(certificateItem);
            }
            itemsTable.setVisible(true);
        }
    }

    @Override
    protected void addButtons() {
        addButton(tcStrings.generateIDCard(), (ClickHandler) event -> generateIDCardPDF());
    }

    public void generateIDCardPDF() {
        RequestObject requestObject = new RequestObject(objectID);
        String pdfURL = CommandConstants.PDF_URL + "/generateIDCard";
        HashMap<String, String> parametrs = requestObject.getRequestParams();
        Utils.sendPDFOrExcelRequest(panel, pdfURL, parametrs, "_blank");
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CERTIFICATE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
    }

    public class CertificateItem extends HorizontalPanel {
        private TextBox textBox;
        private DataListBox colorsListBox;

        public CertificateItem() {
            initialize();
        }

        private void initialize() {
            textBox = new TextBox();
            textBox.setWidth("200px");
            colorsListBox = new DataListBox();
            colorsListBox.setWidth("100px");
            colorsListBox.setWithoutNullLabel(true);
            colorsListBox.setItems(TCConstants.COLORS);
            colorsListBox.setSelected(0);
            colorsListBox.setMarginBottom(1);

            add(textBox);
            add(colorsListBox);
            setSpacing(2);
        }

        public TextBox getTextBox() {
            return textBox;
        }

        public void setTextBox(TextBox textBox) {
            this.textBox = textBox;
        }

        public DataListBox getColorsListBox() {
            return colorsListBox;
        }

        public void setColorsListBox(DataListBox colorsListBox) {
            this.colorsListBox = colorsListBox;
        }
    }
    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
