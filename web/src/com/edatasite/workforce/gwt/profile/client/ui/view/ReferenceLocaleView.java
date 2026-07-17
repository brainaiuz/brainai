package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.form.FooteredCustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WrappedButton;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.List;

public class ReferenceLocaleView extends FooteredCustomForm implements FittedContent, Constants, Colapse {
    private TextBox english;
    private TextBox russian;
    private TextBox arabic;
    private TextBox uzbek;

    private Integer referenceId;
    private ReferenceLocale locale;

    public ReferenceLocaleView(Integer referenceId) {
        super("localeview", "Reference Locale");
        this.referenceId = referenceId;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void initialize() {
        english = new TextBox();
        russian = new TextBox();
        arabic = new TextBox();
        uzbek = new TextBox();

        addField(ENGLISH, new FormGroup("English", english));
        addField(RUSSIAN, new FormGroup("Русский", russian));
        addField(ARABICBOX, new FormGroup("عربى", arabic));
        addField(UZBEK, new FormGroup("O'zbek", uzbek));

        show();
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    protected List<Widget> getFooterLeftSideWidgets() {
        return null;
    }

    @Override
    protected List<Widget> getFooterRightSideWidgets() {
        WrappedButton saveButton = new WrappedButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(clickEvent -> save());

        List<Widget> result = new ArrayList<>();
        result.add(saveButton);

        return result;
    }

    private void save() {
        if (locale == null) {
            locale = new ReferenceLocale();
        }
        locale.setEnglish(english.getText());
        locale.setRussian(russian.getText());
        locale.setArabic(arabic.getText());
        locale.setUzbek(uzbek.getText());
        LoadingPanel.loading(true);
        AllInOneService.App.get().saveReferenceLocale(referenceId, locale, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void unused) {
                LoadingPanel.loading(false);
                closeTab();
            }
        });
    }

    @Override
    protected void addButtons() {

    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        AllInOneService.App.get().getReferenceLocaleByReferenceId(referenceId, new AsyncCallback<ReferenceLocale>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ReferenceLocale referenceLocale) {
                locale = referenceLocale;
                if (referenceLocale != null) {
                    english.setText(referenceLocale.getEnglish());
                    russian.setText(referenceLocale.getRussian());
                    arabic.setText(referenceLocale.getArabic());
                    uzbek.setText(referenceLocale.getUzbek());
                }
                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.REFERENCE_LOCALE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
