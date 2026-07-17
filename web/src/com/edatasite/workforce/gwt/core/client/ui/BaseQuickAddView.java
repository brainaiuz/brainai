package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddSettingsForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLabel;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseQuickAddView extends KpiSideNavBox implements FormHasCustomFieldInterface {
    private static final BaseQuickAddView.BaseQuickAddViewUiBinder ourUiBinder = GWT.create(BaseQuickAddView.BaseQuickAddViewUiBinder.class);
    private final QuickAddSettingsForm form;
    protected HashMap<String, Field> fields = new HashMap<>();
    private FormHasCustomField customFieldUtil;
    @UiField
    public HTMLPanel formPanel;
    @UiField
    FlowPanel fieldPanel;

    public BaseQuickAddView(QuickAddSettingsForm form) {
        super(0);
        ourUiBinder.createAndBindUi(this);
        this.form = form;
        CommonService.App.get().getCustomFieldsForQuickAdd(form.getViewName(), new AsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<CompanyCustomFieldItem> companyCustomFieldItems) {
                getCustomFieldUtil().setCompanyCustomFieldItems(companyCustomFieldItems);
                getCustomFieldUtil().drawCustomFields(null, null, BaseQuickAddView.this);
            }
        });
    }

    private void getColumns() {
        AllInOneService.App.get().getQuickAddColumns(form, new AsyncCallback<QuickAddColumnConfigs[]>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(QuickAddColumnConfigs[] columns) {
                for (QuickAddColumnConfigs configs : columns) {
                    Field field = fields.get(configs.getCode());

                    MaterialLabel title = new MaterialLabel(field.getTitle());
                    title.setMarginBottom(10);
                    fieldPanel.add(title);

                    field.getWidget().getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
                    fieldPanel.add(field.getWidget());
                }
                formPanel.add(fieldPanel);
                addBody(formPanel);
                show();
            }
        });
    }

    public void addField(String id, Widget widget, String title) {
        new Field(id, widget, title);
    }

    public void showFields() {
        getColumns();
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    interface BaseQuickAddViewUiBinder extends UiBinder<HTMLPanel, BaseQuickAddView> {
    }

    protected class Field {
        private String id;
        private String title;
        private Widget widget;

        private Field(String id, Widget widget, String title) {
            this.id = id;
            this.title = title;
            this.widget = widget;
            fields.put(id, this);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Widget getWidget() {
            return widget;
        }

        public void setWidget(Widget widget) {
            this.widget = widget;
        }
    }
}
