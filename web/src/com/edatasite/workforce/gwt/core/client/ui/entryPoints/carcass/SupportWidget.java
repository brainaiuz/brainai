package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SendFeedbackForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.components.TawkLiveChat;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

/**
 * SupportWidget provides a UI component that allows users to access support functionalities,
 * including AI Chat, Live Chat, and Feedback. It integrates with TawkLiveChat and native
 * AI chat functionality to enhance the user support experience.
 */
public class SupportWidget extends Div {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    // Panel and button for triggering the support options popup
    private final Div helpPanel;
    private final MaterialLink helpButton;

    // Containers for the feedback form and its side navigation box
    private KpiSideNavBox sendFeedbackBox;
    private SendFeedbackForm sendFeedbackForm;

    // Popup panel that contains all support options
    private final PopupPanel supportPopup;
    private final Div popupContent;
    private final boolean isBrainUz = Utils.isBrain()  ;

    // Links for AI Chat, Live Chat, and Feedback
    MaterialLink aiChatLink;
    MaterialLink liveChatLink;
    MaterialLink feedBackLink;

    // temporarry icons
    Image chatClsoe = new Image("../mainStyles/new-ui/icons/chat/chat-close.svg");
    Image chatOpen = new Image("../mainStyles/new-ui/icons/chat/chat-open.svg");

    /**
     * Constructor initializes the support widget components and sets up event handlers.
     */
    public SupportWidget() {
        super("frame__help");

        // Initialize the help panel and its toggle button
        helpPanel = new Div("helpChat");
        helpButton = new MaterialLink();
        helpButton.addStyleName("helpChat__btn btn--circle dropdown-button");

        // Add an icon (message square) to the help button
        SvgIcon arrow = new SvgIcon(SvgEnum.messageSquare);
        helpButton.add(chatOpen);
        helpPanel.add(helpButton);

        // Initialize the support popup
        supportPopup = new PopupPanel(true);
        supportPopup.setStyleName("gwt-PopupPanel", false);
        supportPopup.addStyleName("helpChatFooter__wrapper");

        // Set up the popup content container
        popupContent = new Div("helpChatFooter");
        supportPopup.setWidget(popupContent);

        // Add the help panel to the widget
        add(helpPanel);

        // Initialize components and event handlers
        initHelpPanel();
    }

    /**
     * Initializes the help panel components and binds global event handlers.
     */
    private void initHelpPanel() {
        // Close any active live chat on initialization
        TawkLiveChat.closeChat();
        popupContent.clear();

        // Initialize support options
        initAiChat();
        initLiveChat();
        initFeedBack();

        // When the popup is closed, ensure all chat widgets are closed
        supportPopup.addCloseHandler(event -> {
            closeAll();
            helpButton.remove(chatClsoe);
            helpButton.add(chatOpen);
        });

        // Toggle the support options dropdown on help button click
        helpButton.addClickHandler(event -> toggleDropdown());

        // Close all support widgets when the window is about to close
        Window.addWindowClosingHandler(closingEvent -> closeAll());
    }

    /**
     * Toggles the support options dropdown and manages chat widget visibility.
     */
    private void toggleDropdown() {
        // If the AI Chat iframe is ready, toggle its visibility based on current state
        if (!isAiChatClosed()) {
            if (isChatIframeReady()) {
                toggleAiChatJs(true);
                positionDropdown();
                helpButton.remove(chatOpen);
                helpButton.add(chatClsoe);
            }
        }else {
            supportPopup.setVisible(false);
            supportPopup.hide();
            helpButton.remove(chatClsoe);
            helpButton.add(chatOpen);
        }

        // Remove the active style from the live chat link
        liveChatLink.removeStyleName("active");
        aiChatLink.addStyleName("active");
    }

    /**
     * Positions the support popup relative to the help button.
     */
    private void positionDropdown() {
        supportPopup.setPopupPositionAndShow((offsetWidth, offsetHeight) -> {
            int left = helpButton.getAbsoluteLeft();
            int top = helpButton.getAbsoluteTop() + helpButton.getOffsetHeight();

            // Adjust the top position if the popup would overflow the window
            if (top + offsetHeight > Window.getClientHeight()) {
                top = helpButton.getAbsoluteTop() - offsetHeight;
            }
            supportPopup.setPopupPosition(left, top);
        });
    }

    /**
     * Initializes the AI Chat support option.
     */
    private void initAiChat() {
        aiChatLink = new MaterialLink();
        aiChatLink.addStyleName("helpChat-div--message");

        // Create container for the AI Chat icon
        Span signsSpan = new Span();
        signsSpan.addStyleName("helpChat-div__signs");

        // SVG icon
        SvgIcon iFeedback = new SvgIcon(SvgEnum.messageSquare);
        SvgIcon aiChatIcon = new SvgIcon(SvgEnum.aiChat);
        signsSpan.add(aiChatIcon);

        // Create the text label for AI Chat
        Span chatText = new Span(wfmStrings.aiHelper());
        chatText.addStyleName("helpChat-div__text");

        aiChatLink.add(signsSpan);
        aiChatLink.add(chatText);

        // On click, activate AI Chat and close live chat if open
        aiChatLink.addClickHandler(event -> {
            event.preventDefault();
            event.stopPropagation();
            aiChatLink.addStyleName("active");
            liveChatLink.removeStyleName("active");

            if (TawkLiveChat.isLiveChatOpen()) {
                TawkLiveChat.closeChat();
            }
            if (isChatIframeReady()) {
                toggleAiChatJs(true);
            }
        });
        popupContent.add(aiChatLink);
    }


    /**
     * Initializes the Feedback support option and its corresponding form.
     */
    public void initFeedBack() {
        feedBackLink = new MaterialLink();
        feedBackLink.addStyleName("helpChat-div--message");

        // Create container for the feedback icon
        Span signsSpan = new Span();
        signsSpan.addStyleName("helpChat-div__signs");

        SvgIcon feedBackIcon = new SvgIcon(SvgEnum.feedback);
        signsSpan.add(feedBackIcon);

        // Create text label for feedback
        Span feedBackText = new Span(wfmStrings.sendFeedback());
        feedBackText.addStyleName("helpChat-div__text");

        feedBackLink.add(signsSpan);
        feedBackLink.add(feedBackText);
        feedBackLink.addClickHandler(event -> sendFeedbackForm.getTypes());

        // Open the feedback form on click
        feedBackLink.addClickHandler(clickEvent -> {
            closeAll();
            supportPopup.hide();
            onSendfeedBack(null);
        });
        popupContent.add(feedBackLink);

        // Initialize the feedback form components
        initSendFeedbackForm();
    }

    /**
     * Initializes the Live Chat support option.
     */
    private void initLiveChat() {
        liveChatLink = new MaterialLink();
        liveChatLink.addStyleName("helpChat-div--liveChat");

        // Create container for the live chat icon
        Span signsSpan = new Span();
        signsSpan.addStyleName("helpChat-div__signs");

        SvgIcon liveChatIcon = new SvgIcon(SvgEnum.messageSquare);
        signsSpan.add(liveChatIcon);

        // Create text label for live chat
        Span chatText = new Span(wfmStrings.liveChat());
        chatText.addStyleName("helpChat-div__text");

        liveChatLink.add(signsSpan);
        liveChatLink.add(chatText);

        // Initialize TawkLiveChat with required parameters
        new TawkLiveChat(Utils.getTawToSiteId(), Utils.getCompanyName(), Utils.getUserFullName(), Utils.getUserEmail());

        // Setup click handler based on user roles and configurations
        if (!Utils.hasRole(Constants.CLIENT)) {
            if (Utils.getTawToSiteId() != null) {
                liveChatLink.addClickHandler(event -> {
                    event.preventDefault();
                    event.stopPropagation();
                    closeAIChat();
                    aiChatLink.removeStyleName("active");
                    liveChatLink.addStyleName("active");
                    if (!TawkLiveChat.isLiveChatOpen()) {
                        TawkLiveChat.toggleChat();
                    }
                });
            } else {
                liveChatLink.addClickHandler(event ->
                        Utils.openURL(GWT.getHostPageBaseURL() + "LiveChat.html")
                );
            }
        }
        popupContent.add(liveChatLink);
    }

    /**
     * Initializes the feedback form and its associated side navigation box.
     */
    private void initSendFeedbackForm() {
        sendFeedbackBox = new KpiSideNavBox(false);
        sendFeedbackBox.setClass("quick-add");
        sendFeedbackBox.addHeader(new HTML("<H1>" + wfmStrings.sendFeedback() + "</H1>"));

        // Save button for the feedback form
        WfmButton2 saveButton = new WfmButton2(wfmStrings.send(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(ch -> {
            if (sendFeedbackForm.validate()) {
                saveButton.setEnabled(false);
                LoadingPanel.loading(true);

                if (isBrainUz) {
                    CRMService.App.get().saveFeedBack(sendFeedbackForm.getBugReportItem(), new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            saveButton.setEnabled(true);
                            sendFeedbackBox.close();
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.errorOnSendingFeedback(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            saveButton.setEnabled(true);
                            sendFeedbackForm.clearForm();
                            sendFeedbackBox.hide();
                            Info.show(wfmStrings.sentSuccessfullyFeedback(), Info.Type.INFO);
                        }
                    });
                } else {
                    BugReportService.App.get().sendBugReport(sendFeedbackForm.getBugReportItem(), new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            saveButton.setEnabled(true);
                            sendFeedbackBox.close();
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.errorOnSendingFeedback(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            saveButton.setEnabled(true);
                            sendFeedbackForm.clearForm();
                            sendFeedbackBox.hide();
                            Info.show(wfmStrings.sentSuccessfullyFeedback(), Info.Type.INFO);
                        }
                    });
                }
            } else{
                    Info.show(wfmStrings.messageBodyShouldNotBeBlank(), Info.Type.WARNING);
                }
        });

        // Close button for the feedback form (if needed)
        WfmButton2 closeButton = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_DEFAULT);
        closeButton.addClickHandler(ch -> {
            sendFeedbackBox.hide();
        });
        sendFeedbackForm = new SendFeedbackForm();
        sendFeedbackBox.addBody(sendFeedbackForm);
        if (isBrainUz){
            sendFeedbackForm.getAttachments().setVisible(false);
            sendFeedbackForm.getAnonymousFeedbackCheckBox().setVisible(true);
        }else {
            sendFeedbackForm.getAnonymousFeedbackCheckBox().setVisible(false);
        }
        sendFeedbackBox.addFooter(saveButton);
    }

    /**
     * Opens the feedback form and sets the appropriate context.
     *
     * @param from Optional string indicating the origin of the feedback request.
     */
    public void onSendfeedBack(String from) {
        sendFeedbackForm.setCurrentUserViewSummary(MainLayout.get().getCurrentContainer().getName());
        sendFeedbackBox.open();
        sendFeedbackBox.addOpenedHandler(event -> sendFeedbackForm.setFrom(from));
    }

    // Close AI chat widget
    private void closeAIChat() {
        if (isChatIframeReady()) {
            toggleAiChatJs(false);
        }
    }

    // Close all chat widgets AI/live
    private void closeAll() {
        closeAIChat();
        TawkLiveChat.closeChat();
    }


    /**
     * Native method to check if the chat iframe exists and is ready.
     */
    private native boolean isChatIframeReady() /*-{
        return !!$wnd.document.getElementById("kpiChat-wrapper");
    }-*/;

    /**
     * Native method to post toggle message directly to the current window.
     */
    private native void toggleAiChatJs(boolean isVisible) /*-{
        var message = JSON.stringify({
            type: "showChat",
            payload: {isVisible: isVisible}
        });
        // Post to the current window where the listener is attached
        $wnd.postMessage(message, "*");
    }-*/;

    /**
     * Native method to check if the AI chat widget is closed.
     */
    private static native boolean isAiChatClosed() /*-{
        return $wnd.isKpiChatOpen ? $wnd.isKpiChatOpen() : false;
    }-*/;

    public native void setAiChatToken(String token, String hostname) /*-{
        if ($wnd.setToken){
            $wnd.setToken(token);
        }else {
            console.log("Set Token function not found");
        }

        if ($wnd.setDomain){
            $wnd.setDomain(hostname);
        }else {
            console.log("Set Domain function not found");
        }

    }-*/;

    public void initPremiumSupport() {
        // Create the main link element
        MaterialLink premiumSupportLink = new MaterialLink();
        premiumSupportLink.addStyleName("helpChat-div--feedback");

        // Container for the icon
        Span signsSpan = new Span();
        signsSpan.addStyleName("helpChat-div__signs");

        // SVG icon
        SvgIcon iFeedback = new SvgIcon(SvgEnum.messageSquare);
        signsSpan.add(iFeedback);

        // Text element
        Span feedBackText = new Span(wfmStrings.premiumSupport());
        feedBackText.addStyleName("helpChat-div__text");
        feedBackText.getElement().getStyle().setWhiteSpace(Style.WhiteSpace.NOWRAP);

        // Add elements to the link
        premiumSupportLink.add(signsSpan);
        premiumSupportLink.add(feedBackText);

        // Add the link to the dropdown
//        nativeDropdown.add(premiumSupportLink);

        // Click event to open the pricing page
        premiumSupportLink.addClickHandler(clickEvent ->
                Utils.openURLCurrentTab("/Myaccount.html#usageplan|allPricingView/default/0/1/ps")
        );
    }

}
