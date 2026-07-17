package com.edatasite.workforce.gwt.core.client.ui.customfields;

import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmCustomFieldsForm;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_TEXTBOX_EMAIL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_URL;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 09-Nov-2010
 * Time: 18:56:25
 */
public class CustomFieldBuilder {

    private HashMap<String, AbstractCustomField> customFieldMap;

    private WfmCustomFieldsForm wfmForm;
    private ArrayList<String> showFieldCodeName;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private String localeCode = null;
    private Map<String, List<CompanyCustomFieldItem>> customLogicFieldMap = new HashMap<>();
    private ArrayList<String> ignoreValidationList = new ArrayList<>();

    // Custom Fields Form

    public CustomFieldBuilder(ViewAddFiledsCodeName addViewName, WfmCustomFieldsForm wfmForm, final CustomFiledsOnLoad customFiledOnLoad, Integer limitCustomFields) {
        this(addViewName, wfmForm, customFiledOnLoad, true, null, null, limitCustomFields);
    }

    public CustomFieldBuilder(ViewAddFiledsCodeName addViewName, WfmCustomFieldsForm wfmForm, final CustomFiledsOnLoad customFiledOnLoad, String localeCode) {
        this(addViewName, wfmForm, customFiledOnLoad, true, null, localeCode, null);
    }

    // Custom Fields Form

    public CustomFieldBuilder(ViewAddFiledsCodeName addViewName, WfmCustomFieldsForm wfmForm, final CustomFiledsOnLoad customFiledOnLoad, final boolean isUsesShowField) {
        this(addViewName, wfmForm, customFiledOnLoad, isUsesShowField, null, null, null);
    }
    // Custom Fields Form

    public CustomFieldBuilder(ViewAddFiledsCodeName addViewName, WfmCustomFieldsForm wfmForm, final CustomFiledsOnLoad customFiledOnLoad, final boolean isUsesShowField, final Integer relationship, String localeCode, Integer limitCustomFields) {
        this.wfmForm = wfmForm;
        this.localeCode = localeCode;
        LoadingPanel.loading(true);
        CommonService.App.get().getCompanyAddViewFieldsPosition(null, addViewName, relationship, limitCustomFields, new AsyncCallback<ListPanelToolRpc>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ListPanelToolRpc result) {
                LoadingPanel.loading(false);
                setCustomFields(result.getListViewCustomFields());
                if (isUsesShowField) {
                    showFieldCodeName = result.getColumnCodeName();
                }
                if (customFiledOnLoad != null) {
                    customFiledOnLoad.onLoad(result.getColumnCodeName());
                }
                drawCustomFields();
                if (customFiledOnLoad != null) {
                    customFiledOnLoad.setCustomFieldValues();
                }
            }
        });
    }

    public CustomFieldBuilder(WfmCustomFieldsForm wfmForm, ArrayList<CompanyCustomFieldItem> categoryCustomFields) {
        this.wfmForm = wfmForm;

        if (categoryCustomFields != null && categoryCustomFields.size() > 0) {
            setCustomFields(categoryCustomFields);
            drawCustomFields();
            setValues(categoryCustomFields);
        }
    }

    public CustomFieldBuilder(ArrayList<CompanyCustomFieldItem> customFields) {
        setCustomFields(customFields);
    }

    /**
     * Custom fields set object id null
     *
     * @param customFields
     */
    private void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        for (CompanyCustomFieldItem fieldItem : customFields) {
            fieldItem.setObjectId(null);
        }
        this.customFields = customFields;
    }

    /**
     * Drawing user custom fields
     */
    private void drawCustomFields() {
        customFieldMap = new HashMap<>();
        if (customFields != null) {
            customLogicFieldMap = customFields
                    .stream()
                    .filter(cf -> cf.getCustomLogicField() != null)
                    .collect(Collectors.groupingBy(
                            cf -> cf.getCustomLogicField().getDescription(),
                            Collectors.toList()
                    ));
            for (CompanyCustomFieldItem filedItem : customFields) {
                AbstractCustomField customFieldUI = localeCode != null ? CustomFieldFactory.getCustomWidgetWithLocale(filedItem, wfmForm, showFieldCodeName, getCustomWidgetStyle(), localeCode) :
                        CustomFieldFactory.getCustomWidget(filedItem, wfmForm, showFieldCodeName, getCustomWidgetStyle());
                if (isNonConvertedDate() && customFieldUI instanceof DatePickerCustomField) {
                    ((DatePickerCustomField) customFieldUI).setDateNonConvertable(true);
                }
                customFieldMap.put(filedItem.getColumnCode(), customFieldUI);
                if (filedItem.getCustomLogicField() != null) {
                    ignoreValidationList.add(filedItem.getColumnCode());
                }
                if (customFieldUI instanceof DropDownCustomField) {
                    ((DataListBox) customFieldUI.getCustomFieldWidget()).addValueChangeHandler(event -> {
                        if (customLogicFieldMap.get(filedItem.getColumnCode()) != null) {
                            List<CompanyCustomFieldItem> companyCustomFieldItemsList = customLogicFieldMap.get(filedItem.getColumnCode()) != null ? customLogicFieldMap.get(filedItem.getColumnCode()) : null;
                            if (companyCustomFieldItemsList != null && !companyCustomFieldItemsList.isEmpty()) {
                                for (CompanyCustomFieldItem cfItem : companyCustomFieldItemsList) {
                                    AbstractCustomField customField = customFieldMap.get(cfItem.getColumnCode());
                                    if (((DropDownCustomField) customFieldUI).getSelectedItem().getName().equals(cfItem.getCustomLogicValue())) {
                                        ignoreValidationList.remove(cfItem.getColumnCode());
                                    } else {
                                        ignoreValidationList.add(cfItem.getColumnCode());
                                    }
                                    customField.getCustomWidget().setVisible(cfItem.getCustomLogicValue().equals(((DropDownCustomField) customFieldUI).getSelectedItem().getName()));
                                    customField.getLabel().setVisible(cfItem.getCustomLogicValue().equals(((DropDownCustomField) customFieldUI).getSelectedItem().getName()));
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    protected String getCustomWidgetStyle() {
        return null;
    }

    protected boolean isNonConvertedDate() {
        return false;
    }

    /**
     * This is method uses for in edit set save custom fields data
     *
     * @param customFieldItems
     */
    public void setValues(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        for (CompanyCustomFieldItem customItems : customFieldItems) {
            for (HashMap.Entry<String, AbstractCustomField> entry : customFieldMap.entrySet()) {
                AbstractCustomField customFieldUI = (AbstractCustomField) entry.getValue();
                if (customFieldUI.getValue().equals(customItems)) {
                    customFieldUI.setValue(customItems);
                }
            }
        }
    }

    /**
     * This is method uses for returns user wrote date in custom fields
     *
     * @return
     */
    public ArrayList<CompanyCustomFieldItem> getValues() {
        ArrayList<CompanyCustomFieldItem> customFieldList = new ArrayList<>();
        if (customFieldMap != null) {
            for (AbstractCustomField abstractCustomField : customFieldMap.values()) {
                customFieldList.add(abstractCustomField.getValue());
            }
        }
        return customFieldList;
    }

    // Generate Custom fields Widgets

    public void generateFieldsWidgets(ArrayList<String> showFieldCodeName) {
        customFieldMap = new HashMap<>();
        if (customFields != null && showFieldCodeName != null) {
            for (CompanyCustomFieldItem filedItem : customFields) {
                if (showFieldCodeName.contains(filedItem.getColumnCode())) {
                    AbstractCustomField customFieldUI = CustomFieldFactory.getCustomWidget(filedItem, showFieldCodeName);
                    customFieldMap.put(filedItem.getColumnCode(), customFieldUI);
                }
            }
        }
    }
    // Get By fieldCode Name

    public Widget getCustomWidget(String fieldCodeName) {
        return customFieldMap.get(fieldCodeName).getCustomFieldWidget();
    }

    /**
     * This is method uses for returns user wrote date in custom fields
     *
     * @return
     */
    public ArrayList<CompanyCustomFieldItem> getWidgetValues() {
        ArrayList<CompanyCustomFieldItem> customFieldList = new ArrayList<>();
        for (AbstractCustomField abstractCustomField : customFieldMap.values()) {
            customFieldList.add(abstractCustomField.getWidgetValue());
        }
        return customFieldList;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public boolean validateRequiredFields() {
        int errors = 0;
        for (AbstractCustomField cf : customFieldMap.values()) {
            if (cf.isRequiredField && !cf.validateField() && !ignoreValidationList.contains(cf.getColumnCode())) {
                errors++;
            } else if (!cf.isRequiredField && (cf.customFieldItem.getUiType().equals(UI_TYPE_TEXTBOX_EMAIL) || cf.customFieldItem.getUiType().equals(UI_TYPE_URL)) && !cf.validateField()) {
                errors++;
            }
        }
        return errors <= 0;
    }

    public void setReload(ArrayList<CompanyCustomFieldItem> customFields) {

        if (wfmForm != null && wfmForm.getFields() != null && wfmForm.getFields().size() > 0) {
            wfmForm.getFields().clear();
            wfmForm.getContainer().clear();
        }

        setCustomFields(customFields);
        drawCustomFields();
        setValues(customFields);
    }
}
