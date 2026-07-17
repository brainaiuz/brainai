package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AiImageResult implements Serializable {

    private boolean aiAvailable = true;
    private String errorMessage;
    private List<ProductPicture> generatedPictures = new ArrayList<>();

    public AiImageResult() {
    }

    public boolean isAiAvailable() {
        return aiAvailable;
    }

    public void setAiAvailable(boolean aiAvailable) {
        this.aiAvailable = aiAvailable;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<ProductPicture> getGeneratedPictures() {
        return generatedPictures;
    }

    public void setGeneratedPictures(List<ProductPicture> generatedPictures) {
        this.generatedPictures = generatedPictures;
    }
}
