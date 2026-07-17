package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmFormPanel;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;

/**
 * Created by Dilshod Madrahimov on 8/1/15 1:17 PM
 */
public class ProductSerailsImportPopup extends ProductSerialsPopup {

    public static final String SAMPLE_SERIAL_NUMBERS = "https://s3.amazonaws.com/workforcetrack/000000000000/public/Product_serail_numbers.csv";

    private FileUpload fileUpload;
    private WfmFormPanel uploadPanel;
    private Button uploadBtn;
    private Button cancelBtn;

    public ProductSerailsImportPopup() {
        super();
        initialize();
    }

    private void initialize() {
        fileUpload = new FileUpload();
        fileUpload.setName(CommandConstants.ATTACHMENT_PARAM_BASE + 0);
        uploadPanel = new WfmFormPanel("/ProductSerailsHandler");
        uploadPanel.addFormHandler(new FormHandler() {
            public void onSubmit(FormSubmitEvent event) {

            }

            public void onSubmitComplete(FormSubmitCompleteEvent event) {
                LoadingPanel.loading(false);
                close();
                if (uploadPanel.getErrorString() == null && uploadPanel.isSuccess()) {
                    Info.show(wfmStrings.uploaded(), Info.Type.INFO);
                } else {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            }
        });

        uploadPanel.setWidget(fileUpload);
        Anchor simpleLink = new Anchor(wfmStrings.downloadSample(), SAMPLE_SERIAL_NUMBERS);
        simpleLink.setTarget("_blank");

        uploadBtn = new Button(wfmStrings.upload());
        cancelBtn = new Button(wfmStrings.cancel());

        uploadBtn.addClickHandler(clickEvent -> {
            if (fileUpload.getFilename() != null && !"".equals(fileUpload.getFilename())) {
                uploadPanel.setParameter("url", "ProductSerailsHandler");
                uploadPanel.submit();
            }
            LoadingPanel.loading(true);
        });
        cancelBtn.addClickHandler(clickEvent -> close());
        setTitle("Import Serial Numbers");
        setSize(240, 120);
        getContent().add(uploadPanel);
        getContent().add(simpleLink);
        addButton(uploadBtn);
        addButton(cancelBtn);
        open();
    }
}
