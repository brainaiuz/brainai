package com.edatasite.workforce.gwt.crm.client;

import com.edatasite.workforce.gwt.contact.client.ui.AddContactView;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 17:33:02
 * To change this template use File | Settings | File Templates.
 */
public class ContactAddSinksContainer extends SinksContainer {

    public ContactAddSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        addView(new AddContactView(params));

//        String numberExp = "^\\d+$";
////        if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CONTACT)) {
//        boolean hasPermissiontoAdd = Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_CONTACT);
//        if (params != null && params.length > 1) {
//            if (params.length > 3) {
//                if (params[2] != null && params[2].matches(numberExp)) {
//                    AddContactView addContactView = new AddContactView(Integer.valueOf(params[2]), params[3]);
//                    addView(addContactView);
//                } else {
//                    if (hasPermissiontoAdd) {
//                        Integer objectId = null;
//                        AddContactView addContactView = new AddContactView(objectId, params[3]);
//                        addView(addContactView);
//                    }
//                }
//            } else if (params.length > 2) {
//                if (AddContactView.FOR_SEND_INVOICE_QUOTE.equals(params[2]) && hasPermissiontoAdd) {
//                    AddContactView addContactView = new AddContactView(null, params[1].equals("") ? null : Integer.valueOf(params[1]), params[2]);
//                    addView(addContactView);
//                } else if (AddContactView.FROM_OUTLOOK.equals(params[2])) {
//                    AddContactView addContactView = new AddContactView("", "", params[1]);
//                    addView(addContactView);
//                } else if (AddContactView.FROM_INCOMING_CALL.equals(params[2])) {
//                    AddContactView addContactView = new AddContactView(null, null, null, false);
//                    addView(addContactView);
//                    addContactView.setDefaultPhoneNumber(params[1]);
//                } else if (params[2] != null && COPY.equals(params[2])) {
//                    if (params[1] != null && params[1].matches(numberExp)) {
//                        AddContactView addContactView = new AddContactView(Integer.valueOf(params[1]), true);
//                        addView(addContactView);
//                    } else {
//                        if (hasPermissiontoAdd) {
//                            AddContactView addContactView = new AddContactView(null, true);
//                            addView(addContactView);
//                        }
//                    }
//                } else {
//                    if (hasPermissiontoAdd) {
//                        AddContactView addContactView = new AddContactView(null, null, params[2].equals("") ? null : Integer.valueOf(params[2]));
//                        addView(addContactView);
//                    }
//                }
//            } else {
//                if (hasPermissiontoAdd) {
//                    AddContactView addContactView = new AddContactView(null, params[1].equals("") ? null : Integer.valueOf(params[1]));
//                    addView(addContactView);
//                }
//            }
//        } else {
//            if (hasPermissiontoAdd) {
//                AddContactView addContactView = new AddContactView(null, null, null, false);
//                addView(addContactView);
//            }
//        }
//        }
    }
}
