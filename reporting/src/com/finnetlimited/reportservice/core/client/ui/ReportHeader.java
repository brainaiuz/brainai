package com.finnetlimited.reportservice.core.client.ui;

import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.finnetlimited.reportservice.core.client.ui.popup.GoogleMarketPlaceUsersView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * User: ${Dilsh0d}
 * Date: 10-Mar-2010
 * Time: 16:48:10
 * <p/>
 * <br/> This is page template Header page
 */
public class ReportHeader extends Composite {

    interface HeaderBinder extends UiBinder<Widget, ReportHeader> {
    }

    public static final HeaderBinder header = GWT.create(HeaderBinder.class);
    @UiField
    HTMLPanel companyName;
    @UiField
    HTMLPanel userName;
    @UiField
    HTMLPanel manageUsersPipe;
    @UiField
    Anchor manageUsers;
    @UiField
    Anchor logout;
    @UiField
    Anchor report;
    @UiField
    Anchor dashboard;
    @UiField
    HTMLPanel companyLogo;

    @UiHandler("manageUsers")
    public void manageUsersClicked(ClickEvent event) {
        new GoogleMarketPlaceUsersView(true);
    }


    public ReportHeader() {
        initWidget(header.createAndBindUi(this));
        getUserData();
    }

    private void getUserData() {
        CoreService.App.get().getUserNameAndCompanyName(new AsyncCallback<String[]>() {
            public void onFailure(Throwable caught) {
            }

            public void onSuccess(String[] result) {
                Image logo = new Image();
                logo.setStyleName("maxLogoHeight");
                final String workforceLogo = "workforcelogo2.png";//workforce logo;
                logo.setUrl(result[0] != null && !"".equals(result[0]) ? result[0] : workforceLogo);//company logo
                DOM.appendChild(companyLogo.getElement(), logo.getElement());
                HTML html;
                html = new HTML(result[1]);//user name
                DOM.appendChild(userName.getElement(), html.getElement());
                html = new HTML(result[2]);//company name
                DOM.appendChild(companyName.getElement(), html.getElement());
                if (result[3] == null) {
                    manageUsersPipe.setVisible(false);
                    manageUsers.setVisible(false);
                }
            }
        });
    }
}
