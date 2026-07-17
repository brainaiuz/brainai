package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationCFModal;
import com.edatasite.workforce.gwt.core.client.ui.LocalizationTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.MatrixTable;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OrganizeModuleView extends KpiSideNavBox implements Constants, Colapse {

    protected static final ProfileServiceAsync profileService = ProfileService.App.get();
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    protected TextBox singular;
    protected TextBox plural;
    protected TextBox shortname;
    protected DataListBox section;
    protected VerticalPanel sectionGroup;
    private FlowPanel panel;
    private KpiCheckBox convert;
    private MatrixTable convertForms;
    private Div convertFormPanel;
    ArrayList<SelectItem> convertFormItems = new ArrayList<>();
    private List<SelectItem> customForms;

    //    private KpiModal popup;
    protected WfmButton2 saveAndCloseButton;
    protected WfmButton2 closeButton;

    protected Integer objectID;
    protected String objectName;
    protected String module;
    private PropertyItem item;
    private LocalizationCFModal singularLocModel;
    private LocalizationCFModal pluralLocModel;
    private final Command fieldLocalizationSaveCommand = () -> {
        setLocalizedItemsToProperty();
    };

    public OrganizeModuleView(Integer objectID, String objectName, String module) {
        this.objectID = objectID;
        this.objectName = objectName;
        this.module = module;
        CommonService.App.get().getCustomForms(new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                initPopup();
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                super.onSuccess(result);
                customForms = result;
                getConvertItemsBySection();
            }
        });
    }

    private void initPopup() {
        setStyleName(getElement(), "quick-add", true);
        initInternal();

        loadData();
    }

    protected void initInternal() {
        panel = new FlowPanel();
        Heading header = new Heading(HeadingSize.H1);
        header.setText(wfmStrings.properties());
        addHeader(header);


        singular = new TextBox();
        plural = new TextBox();
        shortname = new TextBox();

        section = new DataListBox();
        section.setWithoutNullLabel(true);

        VerticalPanel singularPanel = new VerticalPanel();
        singularPanel.add(createLinkedTitle(settingsStrings.singular(), true));
        singularPanel.add(singular);

        VerticalPanel pluralPanel = new VerticalPanel();
        pluralPanel.add(createLinkedTitle(wfmStrings.plural(), false));
        pluralPanel.add(plural);

        VerticalPanel shortNamePanel = new VerticalPanel();
        Span shortName = new Span(wfmStrings.shortName());
        shortNamePanel.add(shortName);
        shortNamePanel.getElement().getStyle().setMarginTop(24, Style.Unit.PX);
        shortNamePanel.getElement().getStyle().setMarginBottom(16, Style.Unit.PX);
        shortNamePanel.add(shortname);

        sectionGroup = new VerticalPanel();
        sectionGroup.add(new Span(wfmStrings.section()));
        sectionGroup.add(section);
        sectionGroup.getElement().getStyle().setMarginTop(24, Style.Unit.PX);
        sectionGroup.getElement().getStyle().setMarginBottom(16, Style.Unit.PX);
        sectionGroup.setVisible(false);

        panel.add(singularPanel);
        panel.add(pluralPanel);
        panel.add(shortNamePanel);
        panel.add(sectionGroup);


        if (convertFormItems != null && convertFormItems.size() > 0) {
            convertFormPanel = new Div();
            convertFormPanel.setVisible(false);
            convert = new KpiCheckBox(wfmStrings.convert());
            convert.addClickHandler(click -> {
                convertFormPanel.setVisible(convert.getValue());
            });

            convertForms = new MatrixTable(3);
            convertFormPanel.add(convertForms);
            convertForms.addItems(CRMUtils.getSelectItemsAsCheckBoxMap(false, convertFormItems.toArray(new SelectItem[]{})), true);

            panel.add(convert);
            panel.add(convertFormPanel);
        }


        //init buttons
        saveAndCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        closeButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);

        saveAndCloseButton.addClickHandler(sender -> {
            saveAndCloseButton.setEnabled(false);
            save();
        });

        closeButton.addClickHandler(event -> remove());

        addBody(panel);
        addFooter(saveAndCloseButton);
        addFooter(closeButton);
        show();
    }

    private MaterialLink createLinkedTitle(String titleName, Boolean isSingular) {
        MaterialLink localeLink = new MaterialLink(titleName);
        localeLink.addStyleName("btn-small btn--default mb-1 mt-4");
        MaterialIcon plusIcon = new MaterialIcon();
        plusIcon.setStyleName("ficon--plus-circle");
        localeLink.add(plusIcon);
        localeLink.addClickHandler(event -> {
            if (isSingular) {
                singularLocModel = new LocalizationCFModal(item.getlName(), LocalizationTypeEnum.SYSTEM_FORM);
                singularLocModel.setFieldLocalizationSaveCommand(fieldLocalizationSaveCommand);
                singularLocModel.center();
            } else {
                pluralLocModel = new LocalizationCFModal(item.getlPlural(), LocalizationTypeEnum.SYSTEM_FORM);
                pluralLocModel.setFieldLocalizationSaveCommand(fieldLocalizationSaveCommand);
                pluralLocModel.center();
            }

        });
        return localeLink;
    }

    protected void loadData() {
        if (objectID != null) {
            profileService.getPropertyItem(objectID, module, new AbstractAsyncCallback<PropertyItem>() {
                @Override
                public void failure(Throwable throwable) {

                }

                @Override
                public void success(PropertyItem result) {
                    item = result;
                    if (result != null) {
                        singular.setText(result.getSingularMain());
                        plural.setText(result.getPluralMain());
                        shortname.setText(result.getShortcut());
                        section.setItems(getSectionsByModule());
                        if ((ModuleEnum.CRM.getCode().equals(module) || ModuleEnum.HRMS.getCode().equals(module)) || ModuleEnum.ACCOUNTING.getCode().equals(module)) {
                            sectionGroup.setVisible(true);
                            if (item.getContainer() != null) {
                                section.setSelected(item.getContainer());
                            } else {
                                if (getSectionsByModule() != null && getSectionsByModule().length > 0) {
                                    section.setSelected(getSectionsByModule()[0]);
                                }
                            }
                        }
                        if (item.getConvertItems() != null && item.getConvertItems().length > 0) {
                            convert.setValue(true);
                            convertFormPanel.setVisible(true);
                            for (ConvertItem convertItem : item.getConvertItems()) {
                                for (SelectItem item : convertFormItems) {
                                    if (convertItem != null && item != null && convertItem.getCode().equals(item.getDescription())) {
                                        item.setSelected(true);
                                        item.setEntityId(item.getEntityId());
                                        break;
                                    }
                                }
                            }
                            convertForms.clear();
                            convertForms.addItems(CRMUtils.getSelectItemsAsCheckBoxMap(false, convertFormItems.toArray(new SelectItem[]{})), true);
                        }
                    }
                }
            });
        }
    }

    private SelectItem[] getSectionsByModule() {
        if (item.getSections() != null && item.getSections().size() > 0) {
            List<SelectItem> sections = item.getSections();
            return sections.toArray(new SelectItem[]{});
        }
        return null;
    }

    private void setLocalizedItemsToProperty() {
        if (singularLocModel != null) {
            item.setlName(singularLocModel.getLocalization());
        }
        if (pluralLocModel != null) {
            item.setlPlural(pluralLocModel.getLocalization());
        }
    }

    protected void save() {
        if (validate()) {
            item.setSingular(singular.getValue());
            item.setPlural(plural.getValue());
            item.setShortcut(shortname.getValue());
            if (module != null) {
                item.setContainer(section.getSelectedItem());
            }
            setLocalizedItemsToProperty();
            if (convert != null && convert.getValue() && convertForms.getValuesMap() != null && convertForms.getValuesMap().size() > 0) {
                LinkedList<ConvertItem> convertItems = new LinkedList<>();
                SelectItem[] items = convertForms.getValuesMap().keySet().toArray(new SelectItem[]{});
                for (SelectItem item : items) {
                    if (item != null && item.isSelected()) {
                        ConvertItem convertItem = new ConvertItem();
                        convertItem.setCode(item.getDescription());
                        convertItem.setEntityId(item.getEntityId());
                        convertItem.setName(item.getName());
                        convertItems.add(convertItem);
                    }

                }
                item.setConvertItems(convertItems.toArray(new ConvertItem[]{}));
            } else {
                item.setConvertItems(null);
            }
            profileService.saveProperty(item, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    GWT.log(throwable.getMessage());
                    saveAndCloseButton.setEnabled(true);
                }

                @Override
                public void success(Integer result) {
                    LoadingPanel.loading(false);

                    remove();
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.changes()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ORGANIZE_MODULE_RELOAD_PAGE, result, null);
                }
            });
        } else {
            saveAndCloseButton.setEnabled(true);
        }
    }

    public boolean validate() {
        int errors = 0;

        plural.removeStyleName("x-form-invalid");
        if (!Validation.validateTextBoxRequired(plural)) {
            plural.addStyleName("x-form-invalid");
            errors++;
        }

        singular.removeStyleName("x-form-invalid");
        if (!Validation.validateTextBoxRequired(singular)) {
            singular.addStyleName("x-form-invalid");
            errors++;
        }

        shortname.removeStyleName("x-form-invalid");
        if (!Validation.validateTextBoxRequired(shortname)) {
            shortname.addStyleName("x-form-invalid");
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }

        return true;
    }

    private void getConvertItemsBySection() {
        if (Constants.Opportunities.equals(objectName)) {
            if (customForms != null && customForms.size() > 0) {
                for (SelectItem customForm : customForms) {
                    if (Utils.hasPermission(customForm.getCode() + "_ADD_" + Utils.getCompanyID())) {
                        SelectItem item = new SelectItem(null, customForm.getName(), customForm.getCode());
                        item.setEntityId(customForm.getEntityId());
                        convertFormItems.add(item);
                    }
                }
            }
        } else if (Constants.CASE_LIST.equals(objectName)) {
            if (customForms != null && customForms.size() > 0) {
                for (SelectItem customForm : customForms) {
                    if (Utils.hasPermission(customForm.getCode() + "_ADD_" + Utils.getCompanyID())) {
                        SelectItem item = new SelectItem(null, customForm.getName(), customForm.getCode());
                        item.setEntityId(customForm.getEntityId());
                        convertFormItems.add(item);
                    }
                }
            }
        } else if (Constants.REQUEST_FOR_QUOTE.equals(objectName)) {
            convertFormItems.add(new SelectItem(null, Property.get(Constants.Opportunities, wfmStrings.opportunity()), RelationItem.TYPE_OPPORTUNITY));
            convertFormItems.add(new SelectItem(null, Property.get(Constants.SALE_QUOTE, wfmStrings.salesQuote()), RelationItem.TYPE_SALEQUOTE));
            convertFormItems.add(new SelectItem(null, Property.get(Constants.SALE_ORDER_CODE, wfmStrings.saleorder()), RelationItem.TYPE_SALEORDER));
            convertFormItems.add(new SelectItem(null, Property.get(Constants.PURCHASE_ORDER, wfmStrings.purchaseorder()), RelationItem.TYPE_PURCHASE_ORDER));
            if (customForms != null && customForms.size() > 0) {
                for (SelectItem customForm : customForms) {
                    if (Utils.hasPermission(customForm.getCode() + "_ADD_" + Utils.getCompanyID())) {
                        SelectItem item = new SelectItem(null, customForm.getName(), customForm.getCode());
                        item.setEntityId(customForm.getEntityId());
                        convertFormItems.add(item);
                    }
                }
            }

        } else if (Constants.PURCHASE_ORDER.equals(objectName)) {
            convertFormItems.add(new SelectItem(null, Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), RelationItem.TYPE_CASE));
            if (customForms != null && customForms.size() > 0) {
                for (SelectItem customForm : customForms) {
                    if (Utils.hasPermission(customForm.getCode() + "_ADD_" + Utils.getCompanyID())) {
                        SelectItem item = new SelectItem(null, customForm.getName(), customForm.getCode());
                        item.setEntityId(customForm.getEntityId());
                        convertFormItems.add(item);
                    }
                }
            }
        } else if (Constants.PURCHASE_INVOICE.equals(objectName)) {
            convertFormItems.add(new SelectItem(null, Property.get(Constants.SALE_INVOICE, wfmStrings.salesInvoice()), RelationItem.TYPE_SALEINVOICE));
        } else if (Constants.SALE_ORDER_CODE.equals(objectName)) {
            convertFormItems.add(new SelectItem(null, Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), RelationItem.TYPE_CASE));
            if (customForms != null && customForms.size() > 0) {
                for (SelectItem customForm : customForms) {
                    if (Utils.hasPermission(customForm.getCode() + "_ADD_" + Utils.getCompanyID())) {
                        SelectItem item = new SelectItem(null, customForm.getName(), customForm.getCode());
                        item.setEntityId(customForm.getEntityId());
                        convertFormItems.add(item);
                    }
                }
            }
        } else if (Constants.SALE_QUOTE.equals(objectName)) {
            convertFormItems.add(new SelectItem(null, Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), RelationItem.TYPE_CASE));
            if (customForms != null && customForms.size() > 0) {
                for (SelectItem customForm : customForms) {
                    if (Utils.hasPermission(customForm.getCode() + "_ADD_" + Utils.getCompanyID())) {
                        SelectItem item = new SelectItem(null, customForm.getName(), customForm.getCode());
                        item.setEntityId(customForm.getEntityId());
                        convertFormItems.add(item);
                    }
                }
            }
        } else if (Constants.LOGACALL.equals(objectName)) {
            convertFormItems.add(new SelectItem(null, Property.get(Constants.TASK, wfmStrings.task()), RelationItem.TYPE_TASK));
            convertFormItems.add(new SelectItem(null, Property.get(Constants.LOGACALL, wfmStrings.call()), CrmConstants.CRM_EVENT_CALLOG));
            convertFormItems.add(new SelectItem(null, Property.get(Constants.EVENT_LIST, wfmStrings.event()), RelationItem.TYPE_EVENT));
            convertFormItems.add(new SelectItem(null, wfmStrings.sms(), RelationItem.TYPE_SMS));
            convertFormItems.add(new SelectItem(null, Property.get(Constants.SALE_ORDER_CODE, wfmStrings.saleorder()), RelationItem.TYPE_SALEORDER));
            convertFormItems.add(new SelectItem(null, Property.get(Constants.CASE_LIST, wfmStrings.crmCase()), RelationItem.TYPE_CASE));

        } else if ("leave_request_list".equals(objectName)) {
            convertFormItems.add(new SelectItem(null, Property.get(Constants.CERTIFICATES_LIST, wfmStrings.certificate()), RelationItem.TYPE_CERTIFICATE));
            if (customForms != null && customForms.size() > 0) {
                for (SelectItem customForm : customForms) {
                    if (Utils.hasPermission(customForm.getCode() + "_ADD_" + Utils.getCompanyID())) {
                        SelectItem item = new SelectItem(null, customForm.getName(), customForm.getCode());
                        item.setEntityId(customForm.getEntityId());
                        convertFormItems.add(item);
                    }
                }
            }

        } else if (Constants.CANDIDATE.equals(objectName)) {
            convertFormItems.add(new SelectItem(null, Property.get(Constants.PLACEMENT, wfmStrings.placement()), RelationItem.TYPE_PLACEMENT));
            if (customForms != null && customForms.size() > 0) {
                for (SelectItem customForm : customForms) {
                    if (Utils.hasPermission(customForm.getCode() + "_ADD_" + Utils.getCompanyID())) {
                        SelectItem item = new SelectItem(null, customForm.getName(), customForm.getCode());
                        item.setEntityId(customForm.getEntityId());
                        convertFormItems.add(item);
                    }
                }
            }

        }

        initPopup();
    }
}
