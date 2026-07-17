package com.edatasite.workforce.gwt.client.client.ui.view;

import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 9, 2011
 * Time: 2:19:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClientSupplierRemovePopup {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final Integer objectID;
    private final String itemName;
    private final String type;
    private final Command listener;

    public ClientSupplierRemovePopup(Integer objectID, String itemName, String type, Command listener) {
        this.objectID = objectID;
        this.itemName = itemName;
        this.type = type;
        this.listener = listener;
        initialize();
    }

    private void initialize() {
        ClientService.App.get().isContactsExist(objectID, type, new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void success(Boolean isContactExists) {
                showDialog(isContactExists);
            }
        });
    }

    private void showDialog(boolean isContactExists) {
        final KpiModal messageBox = new KpiModal();
        messageBox.setTitle(wfmStrings.warning());

        VerticalPanel messagePanel = new VerticalPanel();
        HTML question = new HTML(wfmStrings.sureYouWantToDelete());
        question.setStyleName("mod_text--center margin-bottom");
        messagePanel.add(question);

        final KpiCheckBox removeContact = new KpiCheckBox(Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.  removeFromContactsToo(), wfmStrings.contacts()));

        if (isContactExists) {
            HorizontalPanelDiv panel = new HorizontalPanelDiv();
            panel.setStyleName("mod_text--center");
//            panel.add(removeContact);
//            Label emptyLabel = new Label();
//            panel.add(emptyLabel);
//            panel.setCellWidth(emptyLabel, "10px");
//            panel.add(new Label(wfmStrings.removeFromContactsToo()));

            panel.add(removeContact);
            messagePanel.add(panel);
        }
        messagePanel.setSpacing(10);

        WfmButton2 yes = new WfmButton2(wfmStrings.yes(), WfmButton2.BTN_PRIMARY);
        yes.addClickHandler(clickEvent -> {
            messageBox.close();
            LoadingPanel.loading(true);

            if (Constants.CLIENT_STR.equals(type)) {
                ClientService.App.get().deleteClient(objectID, removeContact.getValue(), false, new AbstractAsyncCallback<Boolean>() {
                    public void failure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        showFailureInfo();
                    }

                    public void success(Boolean deleted) {
                        LoadingPanel.loading(false);

                        if (deleted != null){
                            if (deleted) {
                                showSuccessInfo();
                                listener.execute();
                            } else {
                                showFailureInfo();
                            }
                        } else{
                            showAccessDenyInfo();
                        }
                    }
                });
            } else {
                ClientService.App.get().deleteSupplier(objectID, removeContact.getValue(), false, new AbstractAsyncCallback<Boolean>() {
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        showFailureInfo();
                    }

                    public void onSuccess(Boolean deleted) {
                        LoadingPanel.loading(false);

                        if (deleted != null) {
                            if (deleted) {
                                showSuccessInfo();
                                listener.execute();
                            } else {
                                showFailureInfo();
                            }
                        } else {
                            showAccessDenyInfo();
                        }
                    }
                });
            }
        });
        WfmButton2 no = new WfmButton2(wfmStrings.no(), WfmButton2.BTN_DEFAULT/*, WfmButton2.ICON_CLOSE*/);
        no.addClickHandler(clickEvent -> messageBox.close());

        messageBox.addButton(no);
        messageBox.addButton(yes);
//        HorizontalPanel buttonPanel = new HorizontalPanel();
//        buttonPanel.add(yes);
//        Label emptyLabel = new Label();
//        buttonPanel.add(emptyLabel);
//        buttonPanel.setCellWidth(emptyLabel, "10px");
//        buttonPanel.add(no);

        FlexTable table = new FlexTable();
//        table.setWidget(0, 0, AbstractImagePrototype.create(icons.warning()).createImage());
        table.setWidget(0, 0, messagePanel);
        //table.setWidget(1, 1, buttonPanel);
        table.setCellSpacing(10);
//        table.getFlexCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_RIGHT);
        messageBox.add(table);
        messageBox.setWidth("400px");
        messageBox.open();
    }

    private void showFailureInfo() {
        if (Constants.CLIENT_STR.equals(type)) {
            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
        } else {
            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
        }
    }

    private void showAccessDenyInfo() {
        if (Constants.CLIENT_STR.equals(type)) {
            Info.show(Property.get(Constants.CLIENT_LIST, wfmStrings.accessDenyOnDeleting(), wfmStrings.customer()), Info.Type.WARNING);
        } else {
            Info.show(Property.get(Constants.SUPPLIER_LIST, wfmStrings.accessDenyOnDeleting(), wfmStrings.supplier()), Info.Type.WARNING);
        }
    }

    private void showSuccessInfo() {
        if (Constants.CLIENT_STR.equals(type)) {
            Info.show(Property.get(Constants.CLIENT_LIST, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.customer()), Info.Type.INFO);
        } else {
            Info.show(Property.get(Constants.SUPPLIER_LIST, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.supplier()), Info.Type.INFO);
        }
    }
}
