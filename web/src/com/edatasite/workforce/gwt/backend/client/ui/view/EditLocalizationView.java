package com.edatasite.workforce.gwt.backend.client.ui.view;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendServiceAsync;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.form.LocalizationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LocalizationPermissionItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;


/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla Nigmatjonov
 * Date: 4/30/12
 * Time: 4:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditLocalizationView extends View {
    private static final BackendServiceAsync backendService = BackendService.App.get();
    private static final String WIDTH = "200px";

    private TextBox code;
    private TextBox defaultTitle;
    private TextBox en;
    private TextBox ru;
    private TextBox arabic;
    private TextBox turkish;
    private TextBox ger;
    private TextBox spa;
    private TextBox fr;
    private TextBox por;
    private TextBox neder;
    private TextBox ita;
    private TextBox thai;
    private TextBox desc;

    private WfmButton2 saveAndClose;
    private WfmButton2 cancel;

    private WfmForm.Field codeField;
    private WfmForm.Field defaultTitleField;
    private WfmForm.Field enField;
    private WfmForm.Field ruField;
    private WfmForm.Field arabicField;
    private WfmForm.Field turkishField;
    private WfmForm.Field gerField;
    private WfmForm.Field spaField;
    private WfmForm.Field frField;
    private WfmForm.Field porField;
    private WfmForm.Field nederField;
    private WfmForm.Field itaField;
    private WfmForm.Field thaiField;
    private WfmForm.Field descField;

    private WfmForm localForm;

    private final String editLocal = "edit_local_";
    private final Integer id;
    private String property;
    private LocalizationPermissionItem access;
    private LocalizationItem copyItem;


    public EditLocalizationView(Integer id, String property) {
        super("add", "Localization");
        this.id = id; //id null kemidi null ni urniga 0 keladi qachonki add ishlasa
        this.property = property;
    }

    public EditLocalizationView(Integer id) {
        super("edit", "Localization");
        this.id = id; //id null kemidi null ni urniga 0 keladi qachonki add ishlasa
    }

    @Override
    protected Widget onInitialize() {
        code = new TextBox();
        code.setWidth(WIDTH);

        defaultTitle = new TextBox();
        defaultTitle.setWidth(WIDTH);
        defaultTitle.setMaxLength(5000);

        en = new TextBox();
        en.setWidth(WIDTH);
        en.setMaxLength(5000);

        ru = new TextBox();
        ru.setWidth(WIDTH);
        ru.setMaxLength(5000);

        arabic = new TextBox();
        arabic.setWidth(WIDTH);
        arabic.setMaxLength(5000);

        turkish = new TextBox();
        turkish.setWidth(WIDTH);

       /* ger = new TextBox();
        ger.setWidth(WIDTH);*/

        spa = new TextBox();
        spa.setWidth(WIDTH);
        spa.setMaxLength(5000);

        fr = new TextBox();
        fr.setWidth(WIDTH);
        fr.setMaxLength(5000);

        por = new TextBox();
        por.setWidth(WIDTH);
        por.setMaxLength(5000);

        neder = new TextBox();
        neder.setWidth(WIDTH);
        neder.setMaxLength(5000);

        ita = new TextBox();
        ita.setWidth(WIDTH);
        ita.setMaxLength(5000);

        thai = new TextBox();
        thai.setWidth(WIDTH);
        thai.setMaxLength(5000);

        desc = new TextBox();
        desc.setWidth(WIDTH);
        desc.setMaxLength(10000);

        saveAndClose = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveAndClose.ensureDebugId(editLocal + "saveAndClose");
        saveAndClose.addClickHandler(clickEvent -> save());

        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancel.ensureDebugId(editLocal + "cancel");
        cancel.addClickHandler(clickEvent -> closeTab());

        FlexTable buttonPanel = new FlexTable();
        buttonPanel.setCellPadding(5);
        buttonPanel.setCellSpacing(5);
        buttonPanel.setWidget(0, 0, saveAndClose);
        buttonPanel.setWidget(0, 1, cancel);


        localForm = new WfmForm();
        localForm.setCellPadding(15);
        localForm.setCellSpacing(15);

        localForm.addTitleField("Add & Edit Localization");
        localForm.addHorizontalLine();

        code.ensureDebugId(editLocal + "code");
        codeField = localForm.addField(wfmStrings.code(), code, true);

        defaultTitle.ensureDebugId(editLocal + "default");
        defaultTitleField = localForm.addField(wfmStrings.default2(), defaultTitle, true);

        en.ensureDebugId(editLocal + "en");
        enField = localForm.addField("En", en);

        ru.ensureDebugId(editLocal + "ru");
        ruField = localForm.addField("Ru", ru);

        arabic.ensureDebugId(editLocal + "arabic");
        arabicField = localForm.addField("Arabic", arabic);

        turkish.ensureDebugId(editLocal + "turkish");
        turkishField = localForm.addField("Turkish", turkish);

        /*ger.ensureDebugId(editLocal + "ger");
        gerField = localForm.addField("Ger", ger);*/

        spa.ensureDebugId(editLocal + "spa");
        spaField = localForm.addField("Spa", spa);

        fr.ensureDebugId(editLocal + "fr");
        frField = localForm.addField("Fr", fr);

        por.ensureDebugId(editLocal + "por");
        porField = localForm.addField("Por", por);

        neder.ensureDebugId(editLocal + "neder");
        nederField = localForm.addField("Neder", neder);

        ita.ensureDebugId(editLocal + "ita");
        itaField = localForm.addField("Ita", ita);

        thai.ensureDebugId(editLocal + "thai");
        thaiField = localForm.addField("Thai", thai);

        desc.ensureDebugId(editLocal + "desc");
        descField = localForm.addField("Description", desc);

        VerticalPanel vPanel = new VerticalPanel();
        vPanel.setSpacing(5);
        vPanel.add(localForm);
        vPanel.add(buttonPanel);
        add(vPanel);
        rendrData();
        return null;
    }

    private void rendrData() {
        LoadingPanel.loading(true);
        backendService.getLocalization(id, new AsyncCallback<LocalizationItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(LocalizationItem result) {
                if (result != null) {
                    copyItem = result;
                    access = result.getLocalizationPermission();

                    code.setText(result.getCode());
                    if (property == null &&  !Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com")){
                        codeField.setVisible(false);
                    }
                    defaultTitle.setText(result.getDefaultText());

                    en.setText(result.getEn());
                    enField.setVisible(access.getEn());

                    ru.setText(result.getRu());
                    ruField.setVisible(access.getRu());

                    arabic.setText(result.getArabic());
                    arabicField.setVisible(access.getArabic());

                    turkish.setText(result.getTurkish());
                    turkishField.setVisible(access.getTurkish());

                   /* ger.setText(result.getGer());
                    gerField.setVisible(access.getGer());*/

                    spa.setText(result.getSpa());
                    spaField.setVisible(access.getSpa());

                    fr.setText(result.getFr());
                    frField.setVisible(access.getFr());

                    por.setText(result.getPor());
                    porField.setVisible(access.getPor());

                    neder.setText(result.getNeder());
                    nederField.setVisible(access.getNeder());

                    ita.setText(result.getIta());
                    itaField.setVisible(access.getIta());

                    thai.setText(result.getThai());
                    thaiField.setVisible(access.getThai());

                    desc.setText(result.getDescription());
                }
                LoadingPanel.loading(false);
            }
        });
    }

    private void save() {
        if (valadition()) {
            LoadingPanel.loading(true);
            saveAndClose.setEnabled(false);
            cancel.setEnabled(false);
            LocalizationItem item = new LocalizationItem();
            access = copyItem.getLocalizationPermission();
            item.setObjectID(id);
            item.setCode(access.getCode() || Utils.isLocalhostOrLochin("lochin.shodiev@workforcetrack.com") ? code.getText() : copyItem.getCode());
            item.setDefaultText(access.getDefaultText() ? defaultTitle.getText() : copyItem.getDefaultText());
            item.setEn(access.getEn() ? en.getText() : copyItem.getEn());
            item.setRu(access.getRu() ? ru.getText() : copyItem.getRu());
            item.setArabic(access.getArabic() ? arabic.getText() : copyItem.getArabic());
            item.setTurkish(access.getTurkish() ? turkish.getText() : copyItem.getTurkish());
            //item.setGer(access.getGer() ? ger.getText() : copyItem.getGer());
            item.setSpa(access.getSpa() ? spa.getText() : copyItem.getSpa());
            item.setFr(access.getFr() ? fr.getText() : copyItem.getFr());
            item.setPor(access.getPor() ? por.getText() : copyItem.getPor());
            item.setNeder(access.getNeder() ? neder.getText() : copyItem.getNeder());
            item.setIta(access.getIta() ? ita.getText() : copyItem.getIta());
            item.setThai(access.getThai() ? thai.getText() : copyItem.getThai());
            item.setDescription(desc.getText());

            item.setDefaultLastChanger(Utils.getUserFullName());
            item.setEnLastChanger(Utils.getUserFullName());
            item.setRuLastChanger(Utils.getUserFullName());
            item.setSpaLastChanger(Utils.getUserFullName());
            item.setItaLastChanger(Utils.getUserFullName());
            item.setTurLastChanger(Utils.getUserFullName());
            item.setPorLastChanger(Utils.getUserFullName());
            item.setNederLastChanger(Utils.getUserFullName());
            item.setArLastChanger(Utils.getUserFullName());
            item.setFrLastChanger(Utils.getUserFullName());
            item.setThaiLastChanger(Utils.getUserFullName());

            Date d = new Date();
            item.setDefaultLastUpdate(d);
            item.setEnLastUpdate(d);
            item.setRuLastUpdate(d);
            item.setSpaLastUpdate(d);
            item.setItaLastUpdate(d);
            item.setTurLastUpdate(d);
            item.setPorLastUpdate(d);
            item.setNederLastUpdate(d);
            item.setArLastUpdate(d);
            item.setFrLastUpdate(d);
            item.setThaiLastUpdate(d);
            if (id == 0) {
                item.setPropertyCode(property);
            }
            backendService.saveLocalization(item, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show("ERROR", Info.Type.WARNING);
                    saveAndClose.setEnabled(true);
                }

                @Override
                public void onSuccess(Boolean t) {
                    saveAndClose.setEnabled(true);
                    cancel.setEnabled(true);
                    LoadingPanel.loading(false);
                    if (t == null)  {
                        Info.show("This code is already exist.", Info.Type.WARNING);
                    }else{
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_LOCALIZATION_EDITED, t, EditLocalizationView.this);
                        closeTab();
                    }
                }
            });

        }
    }

    private boolean valadition() {
        int error = 0;
        localForm.cleanupErrors();
        if (!Validation.validateTextBoxRequired(code, codeField)) {
            error++;
        }
        if (!Validation.validateTextBoxRequired(defaultTitle, defaultTitleField)) {
            error++;
        }
        if (!Validation.validateLocalizationsDynamicValues(defaultTitle.getText(), arabic, arabicField)){
            error++;
        }
        if (!Validation.validateLocalizationsDynamicValues(defaultTitle.getText(), en, enField)){
            error++;
        }
        if (!Validation.validateLocalizationsDynamicValues(defaultTitle.getText(), ru, ruField)){
            error++;
        }
        if (!Validation.validateLocalizationsDynamicValues(defaultTitle.getText(), fr, frField)){
            error++;
        }
        if (!Validation.validateLocalizationsDynamicValues(defaultTitle.getText(), spa, spaField)){
            error++;
        }
        if (!Validation.validateLocalizationsDynamicValues(defaultTitle.getText(), turkish, turkishField)){
            error++;
        }
        if (!Validation.validateLocalizationsDynamicValues(defaultTitle.getText(), thai, thaiField)){
            error++;
        }
        if (!Validation.validateLocalizationsDynamicValues(defaultTitle.getText(), neder, nederField)){
            error++;
        }
        if (!Validation.validateLocalizationsDynamicValues(defaultTitle.getText(), por, porField)){
            error++;
        }
        if (!Validation.validateLocalizationsDynamicValues(defaultTitle.getText(), ita, itaField)){
            error++;
        }
        return error <= 0;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
