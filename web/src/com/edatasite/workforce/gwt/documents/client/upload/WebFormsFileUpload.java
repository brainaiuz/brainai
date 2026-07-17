package com.edatasite.workforce.gwt.documents.client.upload;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: Feb 16, 2011
 * Time: 3:01:10 PM
 */
public class WebFormsFileUpload extends GWTFileUpload implements CommandConstants, Constants {
    public WebFormsFileUpload() {
    }

    public WebFormsFileUpload(boolean withoutServerRadioButtons) {
        super(withoutServerRadioButtons);
    }

    public WebFormsFileUpload(UploadType uploadType, boolean... withoutRadioButtons) {
        super(uploadType, withoutRadioButtons);
    }
}
