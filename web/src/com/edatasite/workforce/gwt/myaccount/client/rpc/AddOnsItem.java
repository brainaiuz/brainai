package com.edatasite.workforce.gwt.myaccount.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Dilshod Madrahimov
 * Date: 05.09.2018
 * Time: 13:07:36
 */
public class AddOnsItem implements IsSerializable {


    private SelectItem onlineTraining;
    private SelectItem initialSetup;
    private SelectItem dedicatedAccountManager;
    private SelectItem customPdfTemplate;
    private SelectItem extraStorage;
    private SelectItem dedicatedDeveloper;

    public SelectItem getOnlineTraining() {
        return onlineTraining;
    }

    public void setOnlineTraining(SelectItem onlineTraining) {
        this.onlineTraining = onlineTraining;
    }

    public SelectItem getInitialSetup() {
        return initialSetup;
    }

    public void setInitialSetup(SelectItem initialSetup) {
        this.initialSetup = initialSetup;
    }

    public SelectItem getExtraStorage() {
        return extraStorage;
    }

    public void setExtraStorage(SelectItem extraStorage) {
        this.extraStorage = extraStorage;
    }

    public SelectItem getCustomPdfTemplate() {
        return customPdfTemplate;
    }

    public void setCustomPdfTemplate(SelectItem customPdfTemplate) {
        this.customPdfTemplate = customPdfTemplate;
    }

    public SelectItem getDedicatedDeveloper() {
        return dedicatedDeveloper;
    }

    public void setDedicatedDeveloper(SelectItem dedicatedDeveloper) {
        this.dedicatedDeveloper = dedicatedDeveloper;
    }

    public SelectItem getDedicatedAccountManager() {
        return dedicatedAccountManager;
    }

    public void setDedicatedAccountManager(SelectItem dedicatedAccountManager) {
        this.dedicatedAccountManager = dedicatedAccountManager;
    }
}