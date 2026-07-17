package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 09-Nov-2010
 * Time: 17:37:28
 */
public class DropDownCustomField extends AbstractCustomField {

    private SimpleLink refreshAction;
    private DataListBox dropDownList;
    private Map<String, ArrayList<SelectItem>> valuesMap;

    public DropDownCustomField(CompanyCustomFieldItem customFieldItem, List<String> fieldCodeName) {
        super(customFieldItem, fieldCodeName);
    }

    public DropDownCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle);
    }

    public DropDownCustomField(CompanyCustomFieldItem customFieldItem, WfmCustomFieldsForm wfmForm, List<String> fieldCodeName, String customWidgetStyle, String localeCode) {
        super(customFieldItem, wfmForm, fieldCodeName, customWidgetStyle, localeCode);
    }

    @Override
    public void initilazation() {
        FlexTable container = new FlexTable();
        dropDownList = new DataListBox();
        refreshAction = new SimpleLink(SimpleLink.REFRESH_ICON);
        refreshAction.setVisible(Constants.UI_TYPE_ENTITY_DROPDOWN.equals(customFieldItem.getUiType()));
        valuesMap = new HashMap<>();
        if (CUSTOM_WIDGET_STYLE != null) {
            dropDownList.setStyleName(CUSTOM_WIDGET_STYLE);
        } else {
            dropDownList.addStyleName(DEFAULT_WIDTH);
        }
        if (customFieldItem.getPredefinedValues() != null) {
            parseDropDownValues(customFieldItem.getPredefinedValues());
            if (localeCode != null) {
                dropDownList.setItems(getValueListByLocale(localeCode).toArray(new SelectItem[]{}));
            } else {
                dropDownList.setItems(getValueListByLocale("en").toArray(new SelectItem[]{}));
            }
        }
        container.setWidget(0, 0, dropDownList);
        container.setWidget(0, 1, refreshAction);
        addField(customFieldItem.getFieldName(), container);
        setValue(customFieldItem);

        refreshAction.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                LoadingPanel.loading(true);
                ProfileService.App.get().getCustomFieldDataByQuery(customFieldItem.getCompanyId(), customFieldItem.getQuery(), new AbstractAsyncCallback<SelectItem[]>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(final SelectItem[] items) {
                        if (items != null) {
                            customFieldItem.setPredefinedValues(Arrays.stream(items).map(SelectItem::getName).collect(Collectors.toList()).toArray(new String[items.length]));
                        }
                        customFieldItem.setObjectId(customFieldItem.getEntityId());
                        ProfileService.App.get().saveCustomFields(customFieldItem.getCompanyId(), customFieldItem, false, new AbstractAsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                super.onFailure(caught);
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void onSuccess(Void result) {
                                LoadingPanel.loading(false);
                                dropDownList.clear();
                                dropDownList.setItems(items);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CUSTOM_FIELD_ADD, null, null);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void setValue(CompanyCustomFieldItem customFields) {
        this.customFieldItem.setObjectId(customFields.getObjectId());
        if (customFields.getFieldStringValue() != null && !"".equals(customFields.getFieldStringValue())) {
            if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
                Double dv = Double.valueOf(customFields.getFieldStringValue());
                for (SelectItem item : dropDownList.getItems()) {
                    if (dv.equals(Double.valueOf(item.getName()))) {
                        dropDownList.setSelected(item.getId());
                        break;
                    }
                }
            } else {
                dropDownList.setSelected(customFields.getFieldStringValue().hashCode());
            }
        }
    }

    @Override
    public CompanyCustomFieldItem getValue() {
        if (dropDownList.getSelectedItem() != null) {
            customFieldItem.setFieldStringValue(dropDownList.getSelectedItem().getName());
        } else {
            customFieldItem.setFieldStringValue("");
        }
        return customFieldItem;
    }

    @Override
    public Widget getCustomFieldWidget() {
        if (dropDownList == null) {
            dropDownList = new DataListBox();
            dropDownList.setWidth(FIELD_WIDTH);
            if (customFieldItem.getPredefinedValues() != null) {
                String[] splitName = customFieldItem.getPredefinedValues();
                SelectItem[] items = new SelectItem[splitName.length];
                for (int i = 0; i < splitName.length; i++) {
                    items[i] = new SelectItem(splitName[i].hashCode(), splitName[i]);
                }
                dropDownList.setItems(items);
            }
        }
        return dropDownList;
    }

    @Override
    public CompanyCustomFieldItem getWidgetValue() {
        CompanyCustomFieldItem customFieldItem = clone();
        if (dropDownList.getSelectedItem() != null) {
            customFieldItem.setFieldStringValue(dropDownList.getSelectedItem().getName());
        }
        return customFieldItem;
    }

    @Override
    public boolean validateField() {
        return !isRequiredField || Validation.validateListBoxRequired(dropDownList, customWfmFormField, wfmStrings.pleaseSelect());
    }

    private void parseDropDownValues(String[] predefinedValues) {
        for (String value : predefinedValues) {
            getValueListByLocale("en").add(new SelectItem(value.hashCode(), value));
        }
    }

    private ArrayList<SelectItem> getValueListByLocale(String locale) {
        valuesMap.computeIfAbsent(locale, k -> new ArrayList<>());
        return valuesMap.get(locale);
    }

    public SelectItem getSelectedItem() {
        return dropDownList.getSelectedItem();
    }

}
