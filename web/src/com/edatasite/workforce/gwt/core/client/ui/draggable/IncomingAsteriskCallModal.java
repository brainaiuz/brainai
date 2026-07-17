package com.edatasite.workforce.gwt.core.client.ui.draggable;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SwitchvoxContactItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 27, 2020
 * Time: 3:55:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class IncomingAsteriskCallModal extends Composite {

    @UiTemplate("com.edatasite.workforce.gwt.core.client.ui.draggable.IncomingAsteriskCallModal.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, IncomingAsteriskCallModal> {
    }

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    @UiField
    HTMLPanel parentWrapper;
    @UiField
    WfmButton2 closeButton;
    @UiField
    HTML msg;

    private SwitchvoxContactItem item;

    public IncomingAsteriskCallModal(SwitchvoxContactItem item) {
        this.item = item;

        MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
        initWidget(uiBinder.createAndBindUi(this));

        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                destroy();
            }
        });

        WfmButton2 seeProfileButton = new WfmButton2(wfmStrings.seeProfile());

        if (item.getObjectId() == null || item.getObjectId() == 0) {
            seeProfileButton.setText(Property.get(Constants.Contacts, wfmStrings.addMess(), wfmStrings.contact()));
        }
        seeProfileButton.addClickHandler(event -> {

            if(WorkforceEntryPoint.asterisktCallPopups.get(item.getWorkPhone())!=null) {
                WorkforceEntryPoint.asterisktCallPopups.remove(item.getWorkPhone());
            }

////            String link = "contact|summary/" + item.getObjectId() + "/" + item.getAccountObjectId();
//            if (item.isLead()) {
//                link = "lead|summary/" + item.getObjectId();
//                if (item.getAccountObjectId() != null) {
//                    link = link + "/" + item.getAccountObjectId();
//                }
//            }
//            if (item.getObjectId() == null || item.getObjectId() == 0) {
//                link = "contact|add/add/" + item.getWorkPhone() + "/phone";
//            }

//            if (Utils.isCRM()) {
//                SinksContainerFactory.entryPoint.onHistoryChanged(link);
//            } else {
//                Utils.openURL("Crm.html#" + link);
//            }
        });

        String messageText = item.getName() + " <b>" + item.getWorkPhone() + "</b> ";
        String body = wfmStrings.youCuurentlyHaveCallFrom() + " " + messageText;
        msg.setHTML(body);

        if(item.getWorkPhone()!=null) {
            parentWrapper.getElement().setId(item.getWorkPhone());
        }

        RootPanel.get().add(this);
        RootPanel.getBodyElement().addClassName("has-modal-open");

        //Make this widget draggable
        Utils.makeDraggable(item.getWorkPhone());

    }

    private void destroy() {
        this.removeFromParent();
    }

}