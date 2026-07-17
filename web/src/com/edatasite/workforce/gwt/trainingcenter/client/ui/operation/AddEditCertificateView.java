package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CRMLookUp;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 6:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddEditCertificateView extends CustomForm2 implements Colapse {

    private static final TCStrings tcStrings = TCStrings.App.get();

    private Integer objectID;
    private Numbering numberWidget;
    private CRMLookUp studentsLookup;
    private DataListBox certificateTypeListBox;

    private Image certificateTypeTemplateImage;
    private FlexTable itemsTable;
    private VerticalPanel itemsPanel;

    private NumberData numberData;

    private WfmButton2 saveButton;

    public AddEditCertificateView() {
        super("add", tcStrings.issueCertificate());
    }

    public AddEditCertificateView(Integer objectID) {
        super("edit", tcStrings.issueCertificate());
        this.objectID = objectID;
    }


    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {
        numberWidget = new Numbering();
        numberWidget.setStyleName(DEFAULT_WIDTH);
        numberWidget.getTxtPrefix().setWidth("30%");
        numberWidget.getTxtNumber().setWidth("70%");
        numberWidget.getLastTxt().setVisible(false);


        studentsLookup = new CRMLookUp(LookUpConstants.STUDENT);
        studentsLookup.addStyleName(DEFAULT_WIDTH);
        studentsLookup.setEnabled(false);

        certificateTypeListBox = new DataListBox();
        certificateTypeListBox.addStyleName(DEFAULT_WIDTH);

        certificateTypeTemplateImage = new Image();
        itemsTable = new FlexTable();
        itemsPanel = new VerticalPanel();
        itemsTable.setVisible(false);
        itemsTable.setWidget(0, 0, certificateTypeTemplateImage);
        itemsTable.setWidget(0, 1, itemsPanel);
        certificateTypeTemplateImage.setWidth("500px");
        itemsTable.setCellSpacing(10);
        itemsPanel.setSpacing(2);
        itemsTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        itemsTable.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);

        certificateTypeListBox.addValueChangeHandler(event -> {
            onCertificateTypeChange(certificateTypeListBox.getSelectedId());
            studentsLookup.setEnabled(certificateTypeListBox.isSomethingSelected());
        });

        addTitleField(CustomFormConstants.TITLE, tcStrings.issueCertificate());
        addField(CERTIFICATE.NUMBER, numberWidget, getTitle(wfmStrings.number(), true));
        addField(CERTIFICATE.STUDENT, studentsLookup, getTitle(wfmStrings.student(), true));
        addField(CERTIFICATE.CERTIFICATE_TYPE, certificateTypeListBox, getTitle(wfmStrings.certificateType(), true));
        addField(CERTIFICATE.ITEMS_TABLE, itemsTable, null);

        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void onCertificateTypeChange(Integer certificateTypeID) {
        itemsTable.setVisible(false);
        itemsPanel.clear();
        studentsLookup.clear();
        if (certificateTypeID != null) {
            TCService.App.get().getCertificateTypeTemplateData(certificateTypeID, new AsyncCallback<CertificateTypeData>() {
                @Override
                public void onFailure(Throwable caught) {
                }

                @Override
                public void onSuccess(CertificateTypeData result) {
                    if (result.getImageURL() != null && result.getFieldsCount() > 0) {
                        certificateTypeTemplateImage.setUrl(result.getImageURL());
                        for (int i = 0; i < result.getFieldsCount(); i++) {
                            itemsPanel.add(new CertificateItem());
                        }
                        itemsTable.setVisible(true);
                    }

                    if (objectID == null && result.getNumberData() != null) {
                        setNumberData(result.getNumberData());
                    }
                    studentsLookup.setEnabled(true);
                }
            });
        }
    }

    private void save() {
        if (!validate()) {
            return;
        }

        saveButton.setEnabled(false);

        CertificateData certificateData = new CertificateData();
        certificateData.setObjectID(objectID);
        certificateData.setStudentID(studentsLookup.getSelectedItemID());

        CertificateTypeData certificateTypeData = new CertificateTypeData();
        certificateTypeData.setObjectID(certificateTypeListBox.getSelectedId());
        if (numberData != null) {
            //numberData.setNumberString(number.getText());
            numberData = numberWidget.getNumberData(false);
            numberData.setFirstNumberString(numberWidget.getTxtPrefix().getText());
            certificateTypeData.setNumberData(numberData);
        }
        certificateData.setCertificateTypeData(certificateTypeData);

        if (itemsPanel != null && itemsPanel.getWidgetCount() > 0) {
            List<CertificateItemData> itemsList = new LinkedList<>();
            for (int i = 0; i < itemsPanel.getWidgetCount(); i++) {
                CertificateItem certificateItem = (CertificateItem) itemsPanel.getWidget(i);
                TextBox textBox = certificateItem.getTextBox();
                if (textBox.getText() != null && !"".equals(textBox.getText().trim())) {
                    CertificateItemData itemData = new CertificateItemData();
                    itemData.setSorder(i);
                    itemData.setValues(textBox.getText());
                    itemData.setColor(certificateItem.getColorsListBox().getSelectedId());
                    itemsList.add(itemData);
                }
            }
            certificateData.setItems(itemsList.toArray(new CertificateItemData[]{}));
        }

        TCService.App.get().saveCertificateData(certificateData, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
                saveButton.setEnabled(true);
            }

            @Override
            public void onSuccess(Integer result) {
                saveButton.setEnabled(true);
                Info.show("Certificate issued successfully", Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CERTIFICATE_SAVED, result, AddEditCertificateView.this);
                closeTab();
            }
        });
    }

    private boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(numberWidget.getTxtNumber())) {
            errors++;
        }
        if (!Validation.validateLookUpRequired(studentsLookup)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(certificateTypeListBox, new HTML(), "")) {
            errors++;
        }
        if (errors > 0) {
            WfmWindow.alert(wfmStrings.sureEnteredAllData());
            return false;
        }

        return true;
    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        TCService.App.get().getCertificateData(objectID, true, new AbstractAsyncCallback<CertificateData>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(CertificateData result) {
                LoadingPanel.loading(false);

                certificateTypeListBox.setItems(result.getCertificateTypes());

                if (result.getCertificateTypeData() != null) {
                    if (result.getCertificateTypeData().getObjectID() != null) {
                        certificateTypeListBox.setSelected(result.getCertificateTypeData().getObjectID());
                        loadCertificateTypeTemplateAndItems(result);
                    }
                    if (result.getCertificateTypeData().getNumberData() != null) {
                        setNumberData(result.getCertificateTypeData().getNumberData());
                    }
                }
            }
        });
    }

    private void setNumberData(NumberData result) {
        numberData = result;
        numberWidget.setNumberData(numberData);
    }

    private void loadCertificateTypeTemplateAndItems(CertificateData result) {
        CertificateTypeData typeData = result.getCertificateTypeData();
        CertificateItemData[] itemsData = result.getItems();

        if (typeData != null) {
            if (typeData.getImageURL() != null && typeData.getFieldsCount() > 0) {
                certificateTypeTemplateImage.setUrl(result.getCertificateTypeData().getImageURL());
                for (int i = 0; i < typeData.getFieldsCount(); i++) {
                    CertificateItem certificateItem = new CertificateItem();
                    TextBox textBox = certificateItem.getTextBox();
                    if (itemsData != null && itemsData.length > 0) {
                        for (CertificateItemData itemData : itemsData) {
                            if (itemData.getSorder().equals(i)) {
                                textBox.setText(itemData.getValues());
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

            studentsLookup.setEnabled(true);
            if (typeData.getStudents() != null) {
                studentsLookup.setItems(typeData.getStudents());
                if (result.getStudentID() != null) {
                    studentsLookup.setSelected(result.getStudentID());
                }
            }
        }
    }

    @Override
    protected void addButtons() {
        saveButton = addButton(wfmStrings.save(), (ClickHandler) event -> save());
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CERTIFICATE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
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
