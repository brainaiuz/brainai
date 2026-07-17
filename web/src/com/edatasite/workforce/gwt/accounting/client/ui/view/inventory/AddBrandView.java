package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.BrandItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.fileUpload.image.KpiImageUploadForm;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 17, 2010
 * Time: 3:52:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddBrandView extends CustomForm2 implements Constants, Colapse {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private TextBox nameTextBox;
    private TextArea2 descriptionTextArea;
    private DataListBox parentBrand;

    private ExtendedCommand providerCommand;
    private Command closeCommand;
    private final String addBrandView = "add_brand_view_";

    private KpiImageUploadForm imageUploadForm;
    private KpiModal box;
    private WfmButton2 saveButton;
    private FormHasCustomField customFieldUtil;
    private LinkedHashMap<String, FormProperty> formPropertyMap;


    private Integer objectID;

    public AddBrandView() {
        super("brandadd", accountingStrings.addBrand());
    }

    public AddBrandView(Integer objectID) {
        super("edit", accountingStrings.editBrand());
        this.objectID = objectID;
    }

    public AddBrandView(ExtendedCommand providerCommand, Command closeCommand, KpiModal box) {
        this.providerCommand = providerCommand;
        this.closeCommand = closeCommand;
        this.box = box;

    }

    @Override
    public String getIconStyle() {
        return "accountMark ac-edit";  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        AccountingService.App.get().getBrand(objectID, new AsyncCallback<BrandItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                GWT.log(throwable.getMessage());
            }

            @Override
            public void onSuccess(BrandItem brandItem) {
                LoadingPanel.loading(false);
                setData(brandItem);
                if (objectID == null) {
                    setDefaultValuesByFormProperty();
                }
            }
        });
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue() != null) {
            nameTextBox.setText(formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue() != null) {
            descriptionTextArea.setText(formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PARENT) != null && formPropertyMap.get(CustomFormConstants.PARENT).getDefaultValue() != null) {
            parentBrand.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PARENT).getSelectedId(), formPropertyMap.get(CustomFormConstants.PARENT).getDefaultValue()));
        }
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.BRAND_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected void addButtons() {
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        saveButton.ensureDebugId("brand-saveButton");
        if (box != null) {
            footer.removeFromParent();
            box.addButton(saveButton);
        } else {
            addButton(saveButton);
        }
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Brand, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                AddBrandView.super.onInitialize();
                initialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    public void initialize() {
        if (providerCommand != null) {
        }

        nameTextBox = new TextBox();
        nameTextBox.addStyleName(DEFAULT_WIDTH);
        nameTextBox.ensureDebugId(addBrandView + "nameTextBox");

        imageUploadForm = new KpiImageUploadForm(200, 200);

        descriptionTextArea = new TextArea2();
        descriptionTextArea.addStyleName(MAX_DEFAULT_WIDTH);
        descriptionTextArea.setHeight("100px");
        descriptionTextArea.ensureDebugId(addBrandView + "descriptionTextArea");

        parentBrand = new DataListBox();
        parentBrand.addStyleName(DEFAULT_WIDTH);
        parentBrand.ensureDebugId(addBrandView + "parentBrand");

        addTitleField(TITLE, objectID != null ? accountingStrings.editBrand() : accountingStrings.addBrand());
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null) {
            addField(CustomFormConstants.NAME, nameTextBox, getTitle(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), formPropertyMap.get(CustomFormConstants.NAME).isRequired()));
            nameTextBox.setEnabled(!formPropertyMap.get(CustomFormConstants.NAME).isDisabled());
        } else {
            addField(NAME, nameTextBox, getTitle(wfmStrings.name(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, descriptionTextArea, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()));
            descriptionTextArea.setEnabled(!formPropertyMap.get(CustomFormConstants.DESCRIPTION).isDisabled());
        } else {
            addField(DESCRIPTION, descriptionTextArea, getTitle(wfmStrings.description()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PARENT) != null) {
            addField(CustomFormConstants.PARENT, parentBrand, getTitle(formPropertyMap.get(CustomFormConstants.PARENT).isChanged() ? formPropertyMap.get(CustomFormConstants.PARENT).getTitle() : wfmStrings.parent(), formPropertyMap.get(CustomFormConstants.PARENT).isRequired()));
            parentBrand.setEnabled(!formPropertyMap.get(CustomFormConstants.PARENT).isDisabled());
        } else {
            addField(PARENT, parentBrand, wfmStrings.parent());
        }

        addField(Website.IMAGE_PANEL, imageUploadForm, wfmStrings.uploadImage());
        getCustomFieldUtil().drawCustomFields(this, objectID);


        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void setData(BrandItem item) {
        nameTextBox.setValue(item.getName());
        descriptionTextArea.setText(item.getDescription());
        descriptionTextArea.setText(item.getDescription());
        parentBrand.setItems(item.getParents());
        if (item.getParentBrandID() != null) {
            parentBrand.setSelected(item.getParentBrandID());
        }
        imageUploadForm.addImage(item.getImageID());
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields());
    }

    private void save() {
        saveButton.setEnabled(false);
        if (validateBrand()) {
            BrandItem brandItem = getBrandItem();
            AccountingService.App.get().checkIfBrandExists(brandItem, new AsyncCallback<TestRPC>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(TestRPC testRPC) {
                    if (testRPC.exists()) {
                        Info.warn(accountingStrings.brandWithThisNameAlreadyExists());
                        saveButton.setEnabled(true);
                    } else {
                        AccountingService.App.get().saveBrand(brandItem, new AbstractAsyncCallback<Integer>() {
                            public void failure(Throwable caught) {
                                //To change body of implemented methods use File | Settings | File Templates.
                            }

                            public void success(Integer result) {
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.brand()), Info.Type.INFO);
                                if (closeCommand != null) {
                                    providerCommand.execute(result);
                                    closeCommand.execute();
                                } else {
                                    closeTab(null);
                                }
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_BRAND_SAVED, null, null);
                            }
                        });
                    }
                }
            });
        } else {
            saveButton.setEnabled(true);
        }
    }

    private boolean validateBrand() {
        int errors = 0;
        clearErrorStyle();

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).isRequired()) {
            errors += markAsError(nameTextBox, !Validation.validateTextBoxRequired(nameTextBox));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()) {
            errors += markAsError(descriptionTextArea, !Validation.validateTextAreaRequired(descriptionTextArea));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PARENT) != null && formPropertyMap.get(CustomFormConstants.PARENT).isRequired()) {
            errors += markAsError(parentBrand, !Validation.validateListBoxRequired(parentBrand));
        }

        errors += getCustomFieldUtil().validateCustomFields();
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private BrandItem getBrandItem() {
        BrandItem brandItem = new BrandItem();
        brandItem.setId(objectID);
        brandItem.setName(nameTextBox.getText());
        brandItem.setDescription(descriptionTextArea.getText());
        if (parentBrand.getSelectedId() != null) {
            brandItem.setParentBrandID(parentBrand.getSelectedId());
        }
        brandItem.setImageID(imageUploadForm.getImageId());
        brandItem.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());

        return brandItem;
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
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
