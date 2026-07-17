package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.ArrayList;

/**
 * Validity response classni ishlatishdan maqsad ichma ich validationlardan olib o'tish
 *
 * Idea is to have single point of data to say if data is valid or not.
 */
public class ValidityResponse {
    private boolean valid = true;
    private ArrayList<String> errorMessage;

    public boolean isValid() {
        return valid;
    }

    private ArrayList<String> getErrorMessage() {
        if (errorMessage == null) {
            errorMessage = new ArrayList<>();
        }
        return errorMessage;
    }

    public void addErrorMessage(String errorMessage_) {
        getErrorMessage().add(errorMessage_);
        valid = false;
    }

    public String[] getErrorMessages() {
        return getErrorMessage().toArray(new String[]{});
    }
}
