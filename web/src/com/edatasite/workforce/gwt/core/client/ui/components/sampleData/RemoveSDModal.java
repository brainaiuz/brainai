package com.edatasite.workforce.gwt.core.client.ui.components.sampleData;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Paragraph;


public class RemoveSDModal extends KpiModal {

    private boolean fromWidget;
    private Command cmdRemoveData;

    public RemoveSDModal() {
        this(false);
    }

    public RemoveSDModal(boolean fromWidget) {
        super();
        this.fromWidget = fromWidget;
        addStyleName("modal--sample modal--sample-image");
        setDismissible(false);
//        addCloseHandler(ch -> RootPanel.getBodyElement().removeClassName("has-modal--sampledata"));
        initialize();
    }

    private void initialize() {
        modalHeader.clear();

        Heading desc = new Heading(HeadingSize.H5);
        String title = fromWidget ? "<p><b>" + Utils.getFirstName() + "</b></p>\n"
                : "<p><b>" + Utils.getFirstName() + "</b>, " + wfmStrings.removeSampleDataConfirmation() + "</p>\n";
        desc.getElement().setInnerHTML(title);
        modalHeader.add(desc);

        WfmButton2 btnCancel = new WfmButton2(fromWidget ? wfmStrings.close() : wfmStrings.removeLater(), WfmButton2.BTN_GREY);
        btnCancel.addClickHandler(ch -> close());

        WfmButton2 btnRemove = new WfmButton2(wfmStrings.removeData(), WfmButton2.BTN_PRIMARY);
        btnRemove.addClickHandler(ch -> {
            close();
            RemoveSDProgress removeSDProgress = new RemoveSDProgress();
            removeSDProgress.setWidth("690px");
            removeSDProgress.open();
            CommonService.App.get().resetCompany(new AsyncCallback<String>() {
                @Override
                public void onFailure(Throwable throwable) {
                    removeSDProgress.removeFromParent();
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(String session) {
                    if (session == null) {
                        removeSDProgress.removeFromParent();
                        Info.warn(wfmStrings.sorrySomethingWentWrong());
                    } else {
                        removeSDProgress.close();
                        Cookies.removeCookie(Constants.SESSION_ID_COOKIE);
                        Cookies.setCookie(Constants.SESSION_ID_COOKIE, session);
                        if (cmdRemoveData != null) {
                            cmdRemoveData.execute();
                        }
                    }
                }
            });
        });

        Div btnGroups = new Div("btns-group text-center");
        btnGroups.add(btnCancel);
        if (!fromWidget) {
            btnGroups.add(btnRemove);
        }

        modalHeader.add(btnGroups);

        modalContent.clear();
        Paragraph paragraph = new Paragraph(wfmStrings.changesWillBeLost());
        modalContent.add(paragraph);
        MaterialImage image = new MaterialImage("/mainStyles/new-ui/images/sample-dashboard.png");
        Div imageContainer = new Div("img-holder");
        imageContainer.add(image);

        modalContent.addStyleName("text-center");
        modalContent.add(imageContainer);

        modalFooter.removeFromParent();
    }

    @Override
    public void open() {
        super.open();
        RootPanel.getBodyElement().addClassName("has-modal--sampledata");
    }

    @Override
    public void close() {
        super.close();
        RootPanel.getBodyElement().removeClassName("has-modal--sampledata");
    }

    public void setCmdRemoveData(Command cmdRemoveData) {
        this.cmdRemoveData = cmdRemoveData;
    }
}
