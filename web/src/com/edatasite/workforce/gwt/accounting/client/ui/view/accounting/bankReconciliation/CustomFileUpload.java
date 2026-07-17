package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankReconciliation;

import com.google.gwt.user.client.ui.FileUpload;

public class CustomFileUpload extends FileUpload {

    public CustomFileUpload() {
        super();
    }

    // Bir nechta fayl yuklashni qo'llab-quvvatlash
    public void setMultiple(boolean multiple) {
        if (multiple) {
            getElement().setAttribute("multiple", "multiple");
        } else {
            getElement().removeAttribute("multiple");
        }
    }
}
