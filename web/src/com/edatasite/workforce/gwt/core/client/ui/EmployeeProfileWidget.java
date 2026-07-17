package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.HelpDocumentItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.WorkforceEntryPoint;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.ChooseCompanyWidget;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.upload.ImageUploadDialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.FigCaption;
import gwt.material.design.client.ui.html.FigureWidget;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.jquery.client.api.JQuery;

import java.util.ArrayList;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.ADMIN;

/**
 * User: Abror Abdukadirov
 * Date: 16.01.2018 17:01
 */
public class EmployeeProfileWidget extends Div {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final EmployeeProfileWidgetUiBinder ourUiBinder = GWT.create(EmployeeProfileWidgetUiBinder.class);
    @UiField
    Span name;
    @UiField
    Span position;
    @UiField
    MaterialPanel profileImagePanel;
    private Span unavailablePhoto;
    private MaterialLink uploadImage;
    private Integer employeeID;
    private Div cardOpts;
    String section = PermissionConstants.USERMENU_CONTEXT;
    String view = PermissionConstants.WIKI;

    //    private Command onBlur;
    public EmployeeProfileWidget() {
        super("profile-card");
        add(ourUiBinder.createAndBindUi(this));
        initWidgets();
        initCardOpts();
        getData();
    }

    private void initFoucser() {
        getElement().getStyle().setOutlineColor(null);
        getElement().getStyle().setOutlineWidth(0, Style.Unit.PX);
        getElement().setTabIndex(0);
        JQuery.$(this).click();
        addBlurHandler(e -> {
//            if (onBlur != null) {
//                onBlur.execute();
//            }
        });

    }

//    public void setOnBlurCommand(Command onBlur) {
//        this.onBlur = onBlur;
//    }

    @Override
    protected void onAttach() {
        super.onAttach();
        setFocus(true);
    }

    private void initWidgets() {
        cardOpts = new Div("profile-card__opts");
        add(cardOpts);
        add(new ChooseCompanyWidget());
        Div footer = new Div("profile-card__footer");
       AllInOneService.App.get().getDownloadAppLinks(new AsyncCallback<SelectItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                footer.getElement().setInnerHTML("<a class=\"vendor-btn--google\" href=\"https://apps.apple.com/uz/app/kpi-mobile/id6468964283\"\n" +
                        "   target=\"_blank\"></a>\n" +
                        "<a class=\"vendor-btn--ios\" href=\"https://play.google.com/store/apps/details?id=com.kpi_superapp&pcampaignid=web_share\"\n" +
                        "   target=\"_blank\"></a>");
            }

            @Override
            public void onSuccess(SelectItem s) {
                if (s == null) {
                    // App links are disabled for this host in the WhiteLabel settings.
                    return;
                }
                String ios = s.getCode() != null ? s.getCode() : "https://apps.apple.com/uz/app/kpi-mobile/id6468964283";
                String android = s.getName() != null ? s.getName() : "https://play.google.com/store/apps/details?id=com.kpi_superapp&pcampaignid=web_share";
                footer.getElement().setInnerHTML("<a class=\"vendor-btn--google\" href=\""+ios+"\"\n" +
                        "   target=\"_blank\"></a>\n" +
                        "<a class=\"vendor-btn--ios\" href=\""+android+"\"\n" +
                        "   target=\"_blank\"></a>");            }
        });

        add(footer);
        initFoucser();
    }

    private void initCardOpts() {
        FigureWidget userFig = createCardFigure(SvgEnum.user, wfmStrings.contactprofile());
        userFig.addClickHandler(e -> Utils.openURL(GWT.getHostPageBaseURL() + "Settings.html#profileSettings"));
        cardOpts.add(userFig);

        if (Utils.hasRole(ADMIN)) {
            FigureWidget myAccFig = createCardFigure(SvgEnum.creditCard, wfmStrings.myAccount());
            myAccFig.addClickHandler(e -> Utils.redirect(GWT.getHostPageBaseURL() + "Myaccount.html"));
            cardOpts.add(myAccFig);
        }

        KpiModal dialogBox = new KpiModal(false);
        dialogBox.getContent().getElement().getStyle().setPadding(0d, Style.Unit.PX);
        dialogBox.setWidth("825px");
        ReferSomeonePopup referSomeonePopup = new ReferSomeonePopup();
        referSomeonePopup.setCloseCommand(param -> dialogBox.close());
        dialogBox.add(referSomeonePopup);

        if (Utils.hasPermission(PermissionConstants.SETTINGS_SEND_ADS_FORM)) {
            FigureWidget referSomeone = createCardFigure(SvgEnum.userPlus, wfmStrings.referSomeone());
            referSomeone.addClickHandler(e -> dialogBox.open());
            cardOpts.add(referSomeone);
        }

        // WhiteLabel switch: the Wiki entry is hidden together with the main page Wiki section.
        AllInOneService.App.get().isShowWiki(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                addWikiAndLogoutFigures(true);
            }

            @Override
            public void onSuccess(Boolean showWiki) {
                addWikiAndLogoutFigures(showWiki == null || showWiki);
            }
        });
    }

    private void addWikiAndLogoutFigures(boolean showWiki) {
        if (showWiki && Utils.hasGenericAccess(GenericSettingsEnum.UPDATE_WIKI)) {
            AllInOneService.App.get().getHelpDocumentBySectionView(section, view, new AsyncCallback<ArrayList<HelpDocumentItem>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ArrayList<HelpDocumentItem> helpDocumentItems) {
                    if (helpDocumentItems != null && PermissionConstants.USERMENU_CONTEXT.equals(helpDocumentItems.get(0).getSection())) {
                        for (HelpDocumentItem helpDocumentItem : helpDocumentItems) {
                            String ItemName = helpDocumentItem.getTitle() != null ? helpDocumentItem.getTitle() : wfmStrings.wiki();
                            String wikiUrl = helpDocumentItem.getLink() != null ? helpDocumentItem.getLink() : "https://www.kpi.com/wiki";
                            FigureWidget wikiFig = createCardFigure(SvgEnum.lifeBuoy, ItemName);
                            wikiFig.addClickHandler(e -> Utils.openURL(wikiUrl));
                            cardOpts.add(wikiFig);
                        }
                    } else {
                        FigureWidget wikiFig = createCardFigure(SvgEnum.lifeBuoy, wfmStrings.wiki());
                        wikiFig.addClickHandler(e -> Utils.openURL("https://www.kpi.com/wiki"));
                        cardOpts.add(wikiFig);
                    }

                    FigureWidget logoutFig = createCardFigure(SvgEnum.logOut, wfmStrings.logOut());
                    logoutFig.addClickHandler(e -> WorkforceEntryPoint.onLogOut());
                    cardOpts.add(logoutFig);

                }
            });
        } else {
            if (showWiki && !Utils.isBrain()) {
                FigureWidget wikiFig = createCardFigure(SvgEnum.lifeBuoy, wfmStrings.wiki());
                wikiFig.addClickHandler(e -> Utils.openURL("https://www.kpi.com/wiki"));
                cardOpts.add(wikiFig);
            }

                FigureWidget logoutFig = createCardFigure(SvgEnum.logOut, wfmStrings.logOut());
                logoutFig.addClickHandler(e -> WorkforceEntryPoint.onLogOut());
                cardOpts.add(logoutFig);
        }

    }

    private FigureWidget createCardFigure(SvgEnum iconType, String caption) {
        FigureWidget fig = new FigureWidget();
        SvgIcon icon = new SvgIcon(iconType);
        FigCaption uCaption = new FigCaption();
        uCaption.setText(caption);
        fig.add(icon);
        fig.add(uCaption);
        return fig;
    }

    private void getData() {
        CommonService.App.get().getEmployeeProfile(new AsyncCallback<EmployeeProfileItem>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(EmployeeProfileItem item) {
                employeeID = item.getUserId();
                setData(item);
            }
        });
    }

    private void setData(EmployeeProfileItem item) {
        String fullName = item.getFirstName() + " " + item.getLastName();
        boolean canChange = Utils.hasPermission(Utils.isPM() ? PermissionConstants.PM_EMPLOYEE_EDIT : PermissionConstants.HRMS_EDIT_PROFILE) ||
                (employeeID != null && Utils.getUserID().equals(employeeID) && Utils.hasPermission(Utils.isPM() ? PermissionConstants.PM_EMPLOYEE_EDIT_OWN_PROFILE : PermissionConstants.HRMS_EDIT_OWN_PROFILE));

        if (item.getEmployeeImageUrl() != null && !"".equals(item.getEmployeeImageUrl())) {
            initProfileImage(item.getEmployeeImageUrl(), canChange);
        } else {
            unavailablePhoto = new Span(Utils.getUserInitialName());
            unavailablePhoto.setClass("user-profile-img__initials");

            Div wrapper = new Div("user-profile-img profile-card-img");
            wrapper.add(unavailablePhoto);
            profileImagePanel.add(wrapper);

            Icon icon = new Icon();
            icon.setStyleName("ficon--camera");

            if (canChange) {
                uploadImage = new MaterialLink();
                uploadImage.setClass("profile-card-img__btn");
                uploadImage.add(icon);
                uploadImage.setText(wfmStrings.change());
                uploadImage.addClickHandler(event -> {
                    ImageUploadDialog uploadDialog = new ImageUploadDialog(item.getUserId(), LayoutRPC.HRMS_EMPLOYEE_FORM);
                    uploadDialog.open();
                });
                profileImagePanel.add(uploadImage);
            }
        }

        name.setText(fullName);
        if (item.isClientContact()) {
            position.setText(wfmStrings.customer());
        } else {
            position.setText((item.getPosition() != null && !"N/A".equals(item.getPosition())) ? item.getPosition() : "");
        }


        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_USER_OWN_IMAGE_UPLOAD_ADD, EmployeeProfileWidget.this, (sender, args) -> {
            if (args != null && sender instanceof ImageUploadDialog) {
                String resultUrl = (String) args;
                initProfileImage(resultUrl, canChange);
            }
        });
    }

    private void initProfileImage(String profileImgUrl, boolean canChange) {
        profileImagePanel.clear();

        profileImagePanel.add(new HTML("<div class=\"user-profile-img profile-card-img\" style=\"background-image:url('" + profileImgUrl + "');\"> <img src=\"" + profileImgUrl + "\"> </div>"));

        Icon icon = new Icon();
        icon.setStyleName("ficon--camera");

        if (canChange) {
            uploadImage = new MaterialLink();
            uploadImage.setClass("profile-card-img__btn");
            uploadImage.add(icon);
            uploadImage.setText(wfmStrings.change());
            uploadImage.addClickHandler(event -> {
                ImageUploadDialog uploadDialog = new ImageUploadDialog(employeeID, LayoutRPC.HRMS_EMPLOYEE_FORM);
                uploadDialog.open();
            });
            profileImagePanel.add(uploadImage);
        }
    }

    interface EmployeeProfileWidgetUiBinder extends UiBinder<Div, EmployeeProfileWidget> {
    }
}