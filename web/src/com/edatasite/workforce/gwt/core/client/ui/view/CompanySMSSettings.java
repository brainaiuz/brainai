//package com.edatasite.workforce.gwt.core.client.ui.view;
//
//import com.edatasite.workforce.gwt.core.client.form.CustomForm;
//import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
//import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
//import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
//import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
//import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
//import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSettings;
//import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
//import com.edatasite.workforce.gwt.profile.client.ui.EmailNotificationConstants;
//import com.edatasite.workforce.gwt.core.client.ui.SchemaLookUp;
//import com.edatasite.workforce.gwt.core.client.ui.billboard.BillboardPanel;
//import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
//import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.core.client.RunAsyncCallback;
//import com.google.gwt.event.dom.client.BlurEvent;
//import com.google.gwt.event.dom.client.BlurHandler;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.event.logical.shared.SelectionEvent;
//import com.google.gwt.event.logical.shared.SelectionHandler;
//import com.google.gwt.user.client.Window;
//import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.ui.*;
//
//
///**
// * Created by IntelliJ IDEA.
// * User: Hayot
// * Date: 20.08.2012
// * Time: 12:53:20
// * To change this template use File | Settings | File Templates.
// */
//
//public class CompanySMSSettings extends CustomForm implements EmailNotificationConstants {
//
//    private FlexTable companyTable;
//    private static final WfmStrings wfmStrings = WfmStrings.App.get();
//    private LookUp providerName;
//    private TextArea requirments;
//    private TextArea keyValues;
//    private DataListBox requestType;
//    private SchemaLookUp companyLookUp;
//    private WfmButton2 test;
//    private Integer objectID;
//    private SmsSettings item;
//
//
//    public CompanySMSSettings() {
//        super("companySMSSettings", "Company sms setting");
//    }
//
//    @Override
//    public FlowPanel getHelpContainer() {
//        return null;
//    }
//
//    @Override
//    public String getIconStyle() {
//        return "icon-settings-e-mail-notification";
//    }
//
//    @Override
//    protected Widget onInitialize() {
//        super.onInitialize();
//        initialize();
//        addFields();
//        return null;
//    }
//
//    @Override
//    protected String getWikiCode() {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    protected String getFormID() {
//        return LayoutRPC.SMS_SETTINGS_FORM;
//    }
//
//    @Override
//    protected String getFormType() {
//        return LayoutRPC.ADD;
//    }
//
//    @Override
//    protected void getDataToFillFields() {
//        requirments.setText("");
//        keyValues.setText("");
//        requestType.setSelectedNullLabel();
//        if (objectID != null) {
//            service.getSmsSetting(companyLookUp.getSelectedItemID(), objectID, new AbstractAsyncCallback<SmsSettings>() {
//                @Override
//                public void onFailure(Throwable throwable) {
//                    BillboardPanel.get().hide();
//                }
//
//                @Override
//                public void onSuccess(SmsSettings result) {
//                    item = result;
//                    requirments.setText("");
//                    keyValues.setText("");
//                    requestType.setSelectedNullLabel();
//                    if (result != null) {
//                        requirments.setText(result.getProviderRequirments());
//                        requestType.setSelectedByValue(result.getRequestType());
//                        keyValues.setText(result.getKeyValues());
//                    }
//                }
//
//            });
//        }
//    }
//
//    @Override
//    protected void addButtons() {
//        addButton(wfmStrings.save(), new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                save();
//            }
//        });
//    }
//
//    private void save() {
//        if (item == null) {
//            item = new SmsSettings();
//        }
//        if (item.getId() == null) {
//            item.setProviderName(providerName.getText());
//        }
//        item.setKeyValues(keyValues.getText());
//        item.setProviderRequirments(requirments.getText());
//        item.setRequestType(requestType.getSelectedItem(true) != null ? requestType.getSelectedItem(true).getName() : null);
//        service.saveSMSSettings(companyLookUp.getSelectedItemID(), item, new AbstractAsyncCallback<Void>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                super.onFailure(caught);
//            }
//
//            @Override
//            public void onSuccess(Void result) {
//                objectID = null;
//                item = null;
//                providerName.clear();
//                getDataToFillFields();
//            }
//        });
//    }
//
//    private void initialize() {
//        companyLookUp = new SchemaLookUp();
//        companyLookUp.getSuggestBox().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
//            @Override
//            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
//                providerName.clear();
//                getDataToFillFields();
//            }
//        });
//
//        companyLookUp.getSuggestBox().addBlurHandler(new BlurHandler() {
//            @Override
//            public void onBlur(BlurEvent blurEvent) {
//                providerName.clear();
//                getDataToFillFields();
//            }
//        });
//        providerName = new LookUp() {
//            @Override
//            protected void onItemDeleteInsertUpdate(int type) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            @Override
//            protected void lookUpService(final ListingFilterParameter filterParametrs) {
//                filterParametrs.setCompanyID(companyLookUp.getSelectedItemID());
//                service.getSmsSettings(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                        BillboardPanel.get().hide();
//                    }
//
//                    @Override
//                    public void onSuccess(SelectItem[] result) {
//                        setItems(filterParametrs.getSearchKey(), result);
//                        String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
//                        getSuggestBox().showSuggestions(searchKey);
//                        BillboardPanel.get().hide();
//                    }
//                });
//            }
//        };
//        providerName.getSuggestBox().addBlurHandler(new BlurHandler() {
//            @Override
//            public void onBlur(BlurEvent blurEvent) {
//                providerSelected();
//            }
//        });
//        providerName.getSuggestBox().addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
//            @Override
//            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
//                providerSelected();
//            }
//        });
//        requirments = new TextArea();
//        keyValues = new TextArea();
//        requestType = new DataListBox();
//        requestType.setItems(SmsSettings.REQUEST_TYPES);
//        test = new WfmButton2("Test");
//        test.addClickListener(new ClickListener() {
//            @Override
//            public void onClick(Widget sender) {
//                test();
//            }
//        });
//    }
//
//    private void test() {
//        service.test(objectID.toString(), new AsyncCallback<String>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//
//            @Override
//            public void onSuccess(String result) {
//                Window.alert(result);
//            }
//        });
//    }
//
//    private void providerSelected() {
//        objectID = null;
//        item = null;
//        objectID = providerName.getSelectedItemID();
//        getDataToFillFields();
//    }
//
//    private void addFields() {
//        addField(BACKEND.SCHEMA_LOOKUP, companyLookUp, wfmStrings.company());
//        addField(BACKEND.SMS.PROVIDER_NAME, providerName, wfmStrings.name());
//        addField(BACKEND.SMS.PROVIDER_REQUIRMENTS, requirments, wfmStrings.request());
//        addField(BACKEND.SMS.KEY_VALUES, keyValues, "Replacements");
//        addField(BACKEND.SMS.REQUEST_TYPE, requestType, wfmStrings.type());
//        addField(BACKEND.TEST_BUTTON, test, "Test");
//        show();
//    }
//
//    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
//        GWT.runAsync(new RunAsyncCallback() {
//
//            public void onFailure(Throwable caught) {
//                callback.onFailure(caught);
//            }
//
//            public void onSuccess() {
//                callback.onSuccess(onInitialize());
//            }
//        });
//    }
//}
