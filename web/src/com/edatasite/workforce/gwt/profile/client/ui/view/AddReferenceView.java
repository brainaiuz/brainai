package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: hayot
 * Date: 5/18/12
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddReferenceView extends CustomForm implements Colapse {
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();

    private Integer objectID;
    private String code;
    private TextBox name;
    private TextArea2 description;
    private ReferenceItem item;
    private ArrayList<ReferenceItem> children;
    private String parentCode;
    private ReferenceItemsTab tab;

    public AddReferenceView(Integer id) {
        super("", wfmStrings.editReference());
        this.objectID = id;
        this.code = null;
    }

    public AddReferenceView(String code) {
        super("", wfmStrings.editReference());
        this.code = code;
    }

    public AddReferenceView(Integer id, String parentCode) {
        super("", wfmStrings.editReference());
        this.objectID = id;
        this.parentCode = "null".equals(parentCode) ? null : parentCode;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.REFERENCE_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    private void initialize() {
        name = new TextBox();
        description = new TextArea2(wfmStrings.description());

        addTitleField(DETAILS, getTitle(wfmStrings.details()));
        addField(NAME, name, wfmStrings.name().concat(":"));
        addField(DESCRIPTION, description, null);

        show();
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, (ClickHandler) event -> save());
    }

    @Override
    protected void getDataToFillFields() {
        if (code != null) {
            service.getReferenceByCode(code, new AbstractAsyncCallback<ReferenceItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                }

                @Override
                public void onSuccess(ReferenceItem result) {
                    item = result == null ? new ReferenceItem() : result;
                    objectID = result == null ? null : result.getObjectID();
                    fillFieldsWithData();
                    getReferenceChildren();
                }
            });
        } else {
            service.getReference(objectID, new AbstractAsyncCallback<ReferenceItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                }

                @Override
                public void onSuccess(ReferenceItem result) {
                    item = result == null ? new ReferenceItem() : result;
                    fillFieldsWithData();
                    getReferenceChildren();
                }
            });
        }
    }

    private void getReferenceChildren() {
        service.getReferenceChildren(objectID, new AbstractAsyncCallback<ArrayList<ReferenceItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ArrayList<ReferenceItem> result) {
                children = result;
                fillChildrenFieldsWithData();
            }
        });
    }

    private void fillFieldsWithData() {
        name.setText(item.getName());
        name.setEnabled(objectID == null);
        description.setText(item.getTextDescription());
    }

    private void fillChildrenFieldsWithData() {
        tab = new ReferenceItemsTab(children);
        if (parentCode == null) {
            addField(REFERENCE_ITEMS, tab, null);
        }
    }

    private boolean validate() {
        int result = tab.resetValidation();
        if (result > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void prepareToSave() {
        item = item == null ? new ReferenceItem() : item;
        item.setParent(parentCode);
        item.setName(name.getText());
        item.setDescription(description.getText());
        children = tab.save(item);
    }

    private void save() {
        if (!validate()) {
            return;
        }
        enableButton(false);
        prepareToSave();
        service.saveReference(item, children, false, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
                enableButton(true);
                closeTab();
            }

            @Override
            public void onSuccess(Integer result) {
                if (result != null && result < 0) {
                    if (ReferenceItem.SHORT_NAME_EXISTS == result) {
                        Info.warn(settingsStrings.shortNameExists());
                    } else if (ReferenceItem.CODE_EXISTS == result) {
                        Info.show(wfmStrings.reference() + " " + wfmStrings.withTheSameNameAlreadyExist(), Info.Type.WARNING);
                    } else {
                        Info.show(settingsStrings.codeAlreadyExists(), Info.Type.WARNING);
                    }
                } else if (result != null && result > 0) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.changes()));
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_REFERENCE_ADD, null, AddReferenceView.this);
                    closeTab();
                }
                enableButton(true);
            }
        });
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
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
