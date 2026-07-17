package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.*;
import java.util.function.Consumer;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

public class CustomFieldsBar extends Composite {
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private static final String open = "custom-form__asside--open";
    private static final String close = "custom-form__asside--close";
    private static final Map<String, List<String>> map = new HashMap<>();
    private Div fieldsDiv;
    private final MaterialPanel panel;
    private Consumer<SelectItem> selectFunction;
    private Integer[] count;

    CustomFieldsBar(String formID) {
        this.formID = formID;
        panel = new MaterialPanel("custom-form__asside");
        initialize();
        initWidget(panel);
    }

    private final String formID;

    static {
        map.put(DATA_TYPE_TEXT, Arrays.asList(
                UI_TYPE_TEXTBOX,
                UI_TYPE_TEXTBOX_EMAIL,
                UI_TYPE_URL,
                UI_TYPE_TEXTAREA,
                UI_TYPE_HTML_TEXTAREA,
                UI_TYPE_DROPDOWN,
                UI_TYPE_ENTITY_DROPDOWN,
                UI_TYPE_CHECKBOX,
                UI_TYPE_RADIOBUTTON,
                UI_TYPE_LOOKUP,
                UI_TYPE_CURRENCY,
                UI_TYPE_MULTI_LOOKUP,
                UI_TYPE_AUTONUMBER,
                TYPE_ENTITY_LOOKUP,
                TYPE_ENTITY_MULTI_LOOKUP,
                UI_TYPE_RADIOBUTTON,
                UI_TYPE_COMMITBOX

        ));
        map.put(DATA_TYPE_DATE, Arrays.asList(UI_TYPE_DATEPICKER, UI_TYPE_DATEPICKER_TIME));
        map.put(DATA_TYPE_PROFILE_IMAGE, Collections.singletonList(UI_TYPE_PROFILE_IMAGE_WIDGET));
        map.put(DATA_TYPE_FILE_UPLOAD, Arrays.asList(UI_TYPE_FILE_UPLOAD_ITEM, UI_TYPE_FILE_UPLOAD_WIDGET));
        map.put(DATA_TYPE_NUMBER, Arrays.asList(NUMBER, UI_TYPE_PERCENTAGE));
    }

    private void createField(String uiType, String name, SvgEnum iconEnum) {

        Div field = new Div("new-field");
        field.addClickHandler(click -> selectField(uiType, name));
        field.getElement().getStyle().setCursor(Style.Cursor.POINTER);

        Div icon = new Div("new-field__icon");
        SvgIcon svgIcon = new SvgIcon(iconEnum);
        icon.add(svgIcon);
        field.add(icon);

        Span txt = new Span(name);
        txt.setTitle(name);
        field.add(txt);

        Div actions = new Div("new-field__actions");
        WfmButton2 button = new WfmButton2("", "btn btn-small btn--new btn--circle");
        button.add(new SvgIcon(SvgEnum.plus));

//        button.addClickHandler(click -> selectField(uiType, name));

        if (uiType.equalsIgnoreCase(CF_COMING_SOON)) {
            field.setTooltip(wfmStrings.comingsoon());
        }

        actions.add(button);
        field.add(actions);
        fieldsDiv.add(field);
    }

    private void initialize() {
        Div wrapper = new Div("new-fields__wrapper");

        MaterialLink closeLink = new MaterialLink();
        SvgIcon icon = new SvgIcon(SvgEnum.x);
        closeLink.add(icon);

        closeLink.addStyleName("new-fields-close");
        closeLink.addClickHandler(click -> this.hide());
        wrapper.add(closeLink);

        panel.add(wrapper);
        Heading h3 = new Heading(HeadingSize.H3);
//        h3.setText("New fields");
        h3.setText(wfmStrings.newFields());
        h3.setClass("new-fields__title");
        wrapper.add(h3);

//        fieldsDiv = new Div(wfmStrings.newFields());
        fieldsDiv = new Div("new-fields");
        wrapper.add(fieldsDiv);

        createField(UI_TYPE_TEXTBOX, wfmStrings.textBox(), SvgEnum.singleLine);
        createField(UI_TYPE_TEXTAREA, wfmStrings.textArea(), SvgEnum.multiline);
        createField(UI_TYPE_DROPDOWN, wfmStrings.dropDown(), SvgEnum.pickList);
        createField(UI_TYPE_RADIOBUTTON, wfmStrings.radioButton(), SvgEnum.radiobutton);
        createField(UI_TYPE_CHECKBOX, wfmStrings.checkBoxes(), SvgEnum.check);
        createField(UI_TYPE_DATEPICKER, wfmStrings.date(), SvgEnum.calendar);
        createField(UI_TYPE_DATEPICKER_TIME, wfmStrings.dateOrTime(), SvgEnum.dateTime);
        createField(UI_TYPE_LOOKUP, wfmStrings.lookUp(), SvgEnum.search);
        createField(UI_TYPE_MULTI_LOOKUP, wfmStrings.multiSelectLookUp(), SvgEnum.multiSelectLookup);
        createField(UI_TYPE_PROFILE_IMAGE_WIDGET, wfmStrings.profilePicture(), SvgEnum.user);
        createField(UI_TYPE_FILE_UPLOAD_ITEM, wfmStrings.fileUpload(), SvgEnum.upload);
        createField(CF_COMING_SOON, wfmStrings.formula(), SvgEnum.formula);
        createField(NUMBER, wfmStrings.numerical(), SvgEnum.number);
        createField(UI_TYPE_TEXTBOX_EMAIL, wfmStrings.email(), SvgEnum.mail);
        createField(UI_TYPE_PERCENTAGE, wfmStrings.percentage(), SvgEnum.percent);
        createField(UI_TYPE_URL, wfmStrings.urlname(), SvgEnum.link);
        createField(UI_TYPE_ITEM_TABLE, wfmStrings.itemTable(), SvgEnum.list);
        createField(UI_TYPE_HTML_TEXTAREA, wfmStrings.textArea(), SvgEnum.multiline);
        createField(TYPE_ENTITY_LOOKUP, wfmStrings.entityLookUp(), SvgEnum.multiSelect);
        createField(TYPE_ENTITY_MULTI_LOOKUP, wfmStrings.entityMultiLookUp(), SvgEnum.multiSelect);
        createField(UI_TYPE_AUTONUMBER, wfmStrings.autoNumber(), SvgEnum.autoNumber);
//        createField(UI_TYPE_COMMITBOX, wfmStrings.commentBox(), SvgEnum.multiline);

        AllInOneService.App.get().isDynamicForm(formID, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                if (aBoolean) {
                    createField(UI_TYPE_APPROVAL_PROCESS, wfmStrings.approvalProcess(), SvgEnum.multiSelect);
                    if (Utils.isEnableAccountingModule()) {
                        createField(UI_TYPE_CURRENCY, wfmStrings.currency(), SvgEnum.dollar);
                    }
                    createField(UI_TYPE_COMMITBOX, wfmStrings.commentBox(), SvgEnum.multiline);
                }
            }
        });
        updateCustomFieldsCount();
    }

    public void updateCustomFieldsCount() {
        CommonService.App.get().getCustomFieldsCount(formID, new AsyncCallback<Integer[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Integer[] integers) {
                count = integers;
                GWT.log("text fields count " + integers[0] + " number fields count " + integers[1] + " dauble fields count " + integers[2]);
            }
        });
    }

    private boolean checkCustomFieldCount(String uiType) {

        if (map.get(DATA_TYPE_TEXT).contains(uiType)) {
            if (count[0] < STRING_FIELD_LIMIT) {
                return true;
            }
        }
        if (map.get(DATA_TYPE_NUMBER).contains(uiType) || map.get(DATA_TYPE_FILE_UPLOAD).contains(uiType) || map.get(DATA_TYPE_PROFILE_IMAGE).contains(uiType)) {
            if (count[1] < DOULE_FIELD_LIMIT) {
                return true;
            }
        }
        if (map.get(DATA_TYPE_DATE).contains(uiType)) {
            return count[2] < FIELD_LIMIT;
        }
        return false;
    }


    private void selectField(String uiType, String name) {
        GWT.log(uiType);
        if (uiType.equals(CF_COMING_SOON)) {
            return;
        }
        if (UI_TYPE_APPROVAL_PROCESS.equals(uiType)) {
            checkCustomFormAttributeCount(uiType, new SelectItem(0, name, uiType));
            return;
        }
        if (!UI_TYPE_ITEM_TABLE.equals(uiType)) {
            if (!checkCustomFieldCount(uiType)) {
                Info.warn(wfmStrings.customFieldLimitExceeded());
                return;
            }
        }
        if (selectFunction != null) {
            selectFunction.accept(new SelectItem(0, name, uiType));
        }
        this.hide();
    }

    private void checkCustomFormAttributeCount(String uiType, SelectItem item) {
        LoadingPanel.loading(true);
        CommonService.App.get().checkCustomFormAttributeCount(uiType, formID, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.warn("Something went wrong!");
            }

            @Override
            public void onSuccess(String o) {
                LoadingPanel.loading(false);
                if ("APPROVAL_PROCESS_LIMIT".equals(o)) {
                    Info.warn(wfmStrings.customFieldLimitExceeded());
                } else {
                    if (selectFunction != null) {
                        selectFunction.accept(item);
                    }
                    hide();
                }
            }
        });
    }

    public void show(Consumer<SelectItem> selectFunction) {
        DOM.getParent(panel.getElement()).removeClassName(close);
        DOM.getParent(panel.getElement()).addClassName(open);
        this.selectFunction = selectFunction;
    }

    public void hide() {
        DOM.getParent(panel.getElement()).removeClassName(open);
        DOM.getParent(panel.getElement()).addClassName(close);
    }
}
