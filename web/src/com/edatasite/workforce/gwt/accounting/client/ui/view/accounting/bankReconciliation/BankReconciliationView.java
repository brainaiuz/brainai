package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankReconciliation;

import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.reconciliation.ImportSettingsWidget;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.reconciliation.ReviewWidget;
import com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.reconciliation.UploadWidget;
import com.edatasite.workforce.gwt.core.client.View;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class BankReconciliationView extends View {

    interface BankReconciliationViewUiBinder extends UiBinder<Widget, BankReconciliationView> {}
    private static final BankReconciliationViewUiBinder uiBinder = GWT.create(BankReconciliationViewUiBinder.class);

    @UiField
    HTMLPanel contentPanel;
    @UiField
    Button nextButton;
    @UiField
    Button prevButton;

    private int currentStep = 1;
    private UploadWidget fileUploadView;
    private ImportSettingsWidget importSettingsView;
    private ReviewWidget reviewView;

    public BankReconciliationView() {
        // UI ni yuklash
        uiBinder.createAndBindUi(this);
        initSteps();
        loadStep(1);

        nextButton.addClickHandler(event -> {
            if (currentStep < 3) {
                currentStep++;
                loadStep(currentStep);
            }
        });

        prevButton.addClickHandler(event -> {
            if (currentStep > 1) {
                currentStep--;
                loadStep(currentStep);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {

    }

    private void initSteps() {
        fileUploadView = new UploadWidget(); // 1-step view
        importSettingsView = new ImportSettingsWidget(null); // 2-step view
        reviewView = new ReviewWidget(null); // 3-step view
    }

    private void loadStep(int step) {
        contentPanel.clear(); // Yangi stepga o'tishda content panelni tozalash

        switch (step) {
            case 1:
                contentPanel.add(fileUploadView); // 1-step view qo'shish
                prevButton.setVisible(false); // Oldingi tugma ko'rinmas
                nextButton.setVisible(true); // Keyingi tugma ko'rinadi
                break;
            case 2:
                contentPanel.add(importSettingsView); // 2-step view qo'shish
                prevButton.setVisible(true); // Oldingi tugma ko'rinadi
                nextButton.setVisible(true); // Keyingi tugma ko'rinadi
                break;
            case 3:
                contentPanel.add(reviewView); // 3-step view qo'shish
                prevButton.setVisible(true); // Oldingi tugma ko'rinadi
                nextButton.setVisible(false); // Keyingi tugma ko'rinmaydi
                break;
        }
    }
}
