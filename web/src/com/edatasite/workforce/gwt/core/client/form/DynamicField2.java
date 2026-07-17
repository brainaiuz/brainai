package com.edatasite.workforce.gwt.core.client.form;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_CURRENCY;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UI_TYPE_ITEM_TABLE;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.ACCOUNTING_PRODUCT_TABLE_SETTINGS;

/**
 * Created by Hurshid on 2/17/2018.
 */
public class DynamicField2 extends MaterialPanel {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();

    private CustomizeFormItem field;
    private boolean active;
    private String section;
    private ColumnType column;
    private MaterialPanel dragActions;
    private Command inactiveCommand;
    private Command addedCommand;
    private WfmButton2 button;
    private String fieldName;
    private final String formID;
    private boolean isQuizForm;

    DynamicField2(CustomizeFormItem field, String formID, boolean active) {
        this.field = field;
        this.formID = formID;
        this.active = active;
        isQuizCustomForm();
        init();
    }

    private void isQuizCustomForm() {
        LoadingPanel.loading(true);
        CommonService.App.get().customFormIsQuizForm(formID, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
                isQuizForm = false;
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                LoadingPanel.loading(false);
                isQuizForm = aBoolean;
            }
        });
    }

    public void setActive(boolean active) {
        field.setActive(active);
        this.active = active;

        String inactiveClass = "drag-tile--inactive";
        if (isActive()) {
            removeStyleName(inactiveClass);
        } else {
            if (!getStyleName().contains(inactiveClass)) {
                addStyleName(inactiveClass);
            }
        }
        if (this.dragActions != null) {
            this.dragActions.setVisible(active);
        }
    }

    public boolean isActive() {
        return active;
    }

    private void init() {
        setStyleName("drag-tile");
        if (field.getId() == null) {
            addStyleName("drag-tile--edit");
        }
        setActive(this.active);

        MaterialPanel textPanel = new MaterialPanel("drag-tile__text drag-tile-field");

        textPanel.add(new HTML(field.getLabel() != null ? field.getLabel() : ""));

        MaterialPanel dragTileGrip = null;
        dragActions = new MaterialPanel("drag-tile__actions");

        if (field.getId() == null) {
            TextBox textBox = new TextBox();
            textBox.setMaxLength(1000);
            textBox.addKeyDownHandler(keyDownEvent -> {
                if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    checkCustomFieldName(textBox.getText());
                }
            });
            textBox.setPlaceHolder(wfmStrings.enterName());
            textPanel.clear();
            textPanel.add(textBox);

            button = new WfmButton2("", "btn--icon");
            SvgIcon check = new SvgIcon("check");
            button.add(check);
            button.addClickHandler(click -> checkCustomFieldName(textBox.getText()));

            dragActions.add(button);
        } else {
            dragTileGrip = new MaterialPanel("drag-tile__grip");
            add(dragTileGrip);

            MaterialLink options = createOptions(field.isCustomField());
            dragActions.add(options);
        }
        add(textPanel);

        if (!this.isActive()) {
            dragActions.setVisible(false);
        }

        WfmButton2 inactiveButton = new WfmButton2("", "btn--icon");
        SvgIcon x = new SvgIcon("x");
        inactiveButton.add(x);
        inactiveButton.addClickHandler(event -> {
            if (inactiveCommand == null || field.getId() == null) {
                this.removeFromParent();
            } else {
                inactiveCommand.execute();
            }
        });

        dragActions.add(inactiveButton);
        add(dragActions);
    }

    private void checkCustomFieldName(String name) {
        button.setEnabled(false);
        if (name == null || name.trim().length() == 0) {
            button.setEnabled(true);
            Info.show(wfmStrings.pleaseEnterValue(), Info.Type.WARNING);
            return;
        }
        fieldName = name.length() > 1000 ? name.substring(0, 1000) : name;
        CommonService.App.get().checkCFName(field.getEntityName(), fieldName.trim(), field.getUiType(), new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                button.setEnabled(true);
            }

            @Override
            public void onSuccess(String[] result) {
                button.setEnabled(true);
                if (result == null) {
                    Info.show(wfmStrings.customFieldNameExists(), Info.Type.WARNING);
                } else if (result[0] == null || result[0].length() == 0) {
                    Info.show(wfmStrings.customFieldLimitExceeded(), Info.Type.WARNING);
                    DynamicField2.this.removeFromParent();
                } else {
                    field.setLabel(fieldName);
                    field.setDataType(result[0]);
                    field.setName(result[1]);
                    field.setId(0);
                    field.setFormID(formID);

                    saveCustomField(field);

                    clear();
                    init();

                    if (addedCommand != null) {
                        addedCommand.execute();
                    }
                }
            }
        });
    }

    private void saveCustomField(CustomizeFormItem field) {

        CommonService.App.get().saveCustomField(field, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                Info.warn("Something went wrong!");
            }

            @Override
            public void onSuccess(String o) {
                if ("APPROVAL_PROCESS_LIMIT".equals(o)) {
                    Info.warn(wfmStrings.customFieldLimitExceeded());
                }
            }
        });
    }

    private MaterialLink createOptions(boolean customField) {
        MaterialLink options = new MaterialLink();
        options.setActivates("field-settings");
        options.addStyleName("btn—icon");
        SvgIcon svgIcon = new SvgIcon("moreBold");
        options.add(svgIcon);

        MaterialDropDown menuContainer = new MaterialDropDown("field-settings");
        menuContainer.setBelowOrigin(true);
        options.add(menuContainer);

        if (customField) {
            MaterialLink properties = new MaterialLink(wfmStrings.properties());

            if (UI_TYPE_ITEM_TABLE.equals(field != null ? field.getUiType() : "") && Utils.hasPermission(ACCOUNTING_PRODUCT_TABLE_SETTINGS)) {
                properties.addClickHandler(click -> {
                    //Utils.redirect("Settings.html#customizationSettings|itemtablesettings/" + formID + "/" + field.getLabel());
                    SinksContainerFactory.entryPoint.onHistoryChanged("customizationSettings|itemtablesettingsdraggable/" + formID + "/" + getSection() + "/" + field.getLabel(), field.getLabel(), field.getLabel());
//                    Utils.redirect("Settings.html#customizationSettings|itemtablesettingsdraggable/" + formID + "/" + field.getLabel());
                });
                menuContainer.add(properties);
            } else {
                properties.addClickHandler(click -> {
                    final FieldProperty property = new FieldProperty(field.getEntityName(), field.getName(), field.isCustomField(), isQuizForm);
                    property.setCommand((fieldItem) -> {
                        CompanyCustomFieldItem f = (CompanyCustomFieldItem) fieldItem;
                        field.setLabel(f.getFieldName());
                        field.setSystemMandatory(f.isRequired());
                        clear();
                        init();
                    });
                    property.initizalize();
                });
//                if (!UI_TYPE_COMMITBOX.equalsIgnoreCase(field.getUiType()))
                menuContainer.add(properties);
            }

            if (!(UI_TYPE_ITEM_TABLE.equals(field.getUiType()) || UI_TYPE_CURRENCY.equals(field.getUiType()))) {
                MaterialLink required = new MaterialLink(field.isSystemMandatory() ? wfmStrings.markAsNotRequired() : wfmStrings.markAsRequired());
                required.addClickHandler(click -> updateCustomField(!field.isSystemMandatory()));
                menuContainer.add(required);
            }

            MaterialLink delete = new MaterialLink(wfmStrings.delete());
            delete.addClickHandler(click -> {
                if (!field.isCustomField()) {
                    return;
                }
                if (field == null || field.getId() < 1) {
                    this.removeFromParent();
                    return;
                }
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.confirmation());
                messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        CommonService.App.get().deleteCustomField(field.getEntityName(), field.getName(), new AbstractAsyncCallback<Void>() {
                            @Override
                            public void failure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                throwable.printStackTrace();
                            }

                            @Override
                            public void success(Void result) {
                                field.setDeleted(true);
                                LoadingPanel.loading(false);
                                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.customField()), Info.Type.INFO);
                                DynamicField2.this.removeFromParent();
                            }
                        });
                    }
                });
                messageBox.open();
            });
            menuContainer.add(delete);
        }

        MaterialLink deactivate = new MaterialLink(wfmStrings.deactivate());
        deactivate.addClickHandler(click -> {
            if (inactiveCommand != null) {
                inactiveCommand.execute();
            } else {
                this.removeFromParent();
            }
        });
        menuContainer.add(deactivate);
        return options;
    }

    private void updateCustomField(boolean mandatory) {
        CommonService.App.get().updateCustomField(field.getEntityName(), field.getName(), mandatory, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Void aVoid) {
                field.setSystemMandatory(mandatory);
                clear();
                init();
            }
        });
    }

    public String getSection() {
        if (section == null) {
            section = field.getSection();
        }
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public ColumnType getColumn() {
        if (column == null) {
            column = field.getColumnType();
        }
        return column;
    }

    public void setColumn(ColumnType column) {
        this.column = column;
    }

    public CustomizeFormItem getField() {
        return field;
    }

    public void setField(CustomizeFormItem field) {
        this.field = field;
    }

    void setInactiveCommand(Command inactiveCommand) {
        this.inactiveCommand = inactiveCommand;
    }

    public void setAddedCommand(Command addedCommand) {
        this.addedCommand = addedCommand;
    }
}
