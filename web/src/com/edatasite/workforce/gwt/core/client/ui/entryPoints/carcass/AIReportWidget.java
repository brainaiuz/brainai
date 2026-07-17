package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import com.edatasite.workforce.gwt.chart.client.charts.AbstractChart;
import com.edatasite.workforce.gwt.chart.client.enums.ChartTypeEnum;
import com.edatasite.workforce.gwt.chart.client.rpc.ChartData;
import com.edatasite.workforce.gwt.chart.client.utils.ChartUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.ReportingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AiReportService;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Position;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.IFrame;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.IS_PAID_COMPANY;

public class AIReportWidget extends Composite {
    private EmployeeProfileItem employeeProfileItem;

    public void setEmployeeProfileItem(EmployeeProfileItem employeeProfileItem) {
        this.employeeProfileItem = employeeProfileItem;
        initialize();
    }

    public long getClickCount() {
        return clickCount;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }

    interface AIReportWidgetUiBinder extends UiBinder<HTMLPanel, AIReportWidget> {
    }

    private static final AIReportWidgetUiBinder ourUiBinder = GWT.create(AIReportWidgetUiBinder.class);

    public static final ReportingStrings reportingStrings = ReportingStrings.App.get();
    protected static final WfmMessages wfmMessages = WfmMessages.App.get();
    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    // Generate Report video URL'lari
    private static final String generateReportVideoUrlUz = "https://www.youtube.com/embed/S1BoakEJEeY";
    private static final String generateReportVideoUrlRu = "https://www.youtube.com/embed/oH6A7hcYfnk";
    private static final String generateReportVideoUrl = "https://www.youtube.com/embed/xqFTe96OWPU";

    // Ask Question video URL'lari
    private static final String askAQuestionVideoUrlUz = "https://www.youtube.com/embed/uUDS9uBvIFk";
    private static final String askAQuestionVideoUrlRu = "https://www.youtube.com/embed/H1Y9HC5cmI0";
    private static final String askAQuestionVideoUrl = "https://www.youtube.com/embed/sRSqDctI8s8";

    private Widget lastVideoWidget;
    private Widget lastMessageWidget;
    private Widget onlyPaidSubscriptionMessage;
    private KpiModal videoModal;
    private Runnable onCloseListener;
    private String reportRpcUUID;
    private WfmButton2 likeBtn;
    private WfmButton2 dislikeBtn;
    private WfmButton2 submitBtn;
    private boolean reportGenerationEnabled = false;
    private long clickCount = 0;
    private Div wrapper = null;
    private Long chatId;
    private Integer chatHistoryLimit = 10;
    private boolean userLiked = false;
    Boolean isPaidCompany = Utils.getParam(IS_PAID_COMPANY) != null
            ? Boolean.valueOf(Utils.getParam(IS_PAID_COMPANY))
            : Boolean.FALSE;


    @UiField
    HTMLPanel msgTime;
    @UiField
    TextArea messageInput;
    @UiField
    FlowPanel messageInputContainer;
    @UiField
    FlowPanel bodyPanel;
    @UiField
    FlowPanel footerInfoPanel;
//    @UiField
//    DivElement recordButton;

    public String getReportRpcUUID() {
        return reportRpcUUID;
    }

    public void setReportRpcUUID(String reportRpcUUID) {
        this.reportRpcUUID = reportRpcUUID;
    }

    public void setOnCloseListener(Runnable listener) {
        this.onCloseListener = listener;
    }

    public AIReportWidget() {
        initWidget(ourUiBinder.createAndBindUi(this));
        exportAddUserMessageCallbackNative();
    }

    private void initialize() {
        // Initially setting userID for every session
        AiReportService.App.get().getOrCreateChatId(Utils.getUserID(), Utils.getCompanyID(), new AsyncCallback<Long>() {
            @Override
            public void onFailure(Throwable caught) {
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(Long result) {
                chatId = result;
            }
        });
        messageInputContainer.getElement().getStyle().setDisplay(Style.Display.NONE);
//        setMessageTime(new Date());
        typingFromAi(wfmMessages.helloFromKIA(Utils.getUserFullName()));
        addButtonsForWelcomeOptions();
        bindInputHandlers();
//        bindMicButtonHandler();
        setupFooterInfo();
    }
    //    private void setMessageTime(Date date) {
//        DateTimeFormat format = DateTimeFormat.getFormat("EEEE 'at' HH:mm");
//        String formatted = format.format(date);
//        msgTime.getElement().setInnerText(formatted);
//    }

    private void clearLastWidgets() {
        if (lastMessageWidget != null) {
            bodyPanel.remove(lastMessageWidget);
            lastMessageWidget = null;
        }
        if (lastVideoWidget != null) {
            bodyPanel.remove(lastVideoWidget);
            lastVideoWidget = null;
        }
        if (onlyPaidSubscriptionMessage != null) {
            bodyPanel.remove(onlyPaidSubscriptionMessage);
            onlyPaidSubscriptionMessage = null;
        }
    }

    private native void exportAddUserMessageCallbackNative() /*-{
        var self = this;
        $wnd.sendUserMessage = function(msgText) {
            self.@com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.AIReportWidget::addUserMessageByClicking(Ljava/lang/String;)(msgText);
        };
    }-*/;


    private void setupFooterInfo() {
        // Create horizontal container for footer text and icon
        FlowPanel container = new FlowPanel();
        container.setStyleName("chat__inf");

        // Set flex styles directly
        Style style = container.getElement().getStyle();
        style.setProperty("display", "flex");
        style.setProperty("alignItems", "center");
        style.setProperty("justifyContent", "center");
        style.setProperty("gap", "1px");

        // Footer label
        Label footerText = new Label(wfmMessages.poweredByAi());
        footerText.getElement().getStyle().setFontSize(12, Style.Unit.PX);
        footerText.getElement().getStyle().setColor("#6a6a6a");

        // Info icon with tooltip
        MaterialIcon infoIcon = new MaterialIcon();
        infoIcon.setTooltip(wfmMessages.aiTooltipInfo());
        infoIcon.setTooltipPosition(Position.TOP);
        infoIcon.setStyleName("ficon--info");
        infoIcon.getElement().getStyle().setColor("#6a6a6a");
        infoIcon.getElement().getStyle().setFontSize(12, Style.Unit.PX);

        // Compose
        footerInfoPanel.clear();
        footerInfoPanel.add(footerText);
        footerInfoPanel.add(infoIcon);
    }

//    private void bindMicButtonHandler() {
//        DOM.sinkEvents(recordButton, Event.ONCLICK);
//        DOM.setEventListener(recordButton, event -> {
//            event.preventDefault();
//            startVoiceRecording();
//        });
//    }
//
//    private native void startVoiceRecording() /*-{
//        $wnd.voiceRecorder.start();
//    }-*/;

    private void addButtonsForWelcomeOptions() {
        FlowPanel buttonGroup = new FlowPanel();
        buttonGroup.setStyleName("kia-buttons");

        // Generate Report Button
        Anchor generateBtn = new Anchor(wfmStrings.generateReport());
        generateBtn.setStyleName("btn btn--primary");
        generateBtn.addClickHandler(event -> {
            GWT.log("Paid or Free Company: ------------" + isPaidCompany + "------------");
            if ("apps.kpi.com".equals(Utils.getHostName()) && !isPaidCompany) {
                messageInputContainer.getElement().getStyle().setDisplay(Style.Display.NONE);
                clearLastWidgets();
                lastMessageWidget = typingFromAiReturnable(wfmMessages.reportPrompt());
                AiReportService.App.get().getAIVideoLinkMap(new AsyncCallback<Map<String,String>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        lastVideoWidget = null;
                    }

                    @Override
                    public void onSuccess(Map<String,String> result) {
                        if (result.get("reportVideo") != null && !result.get("reportVideo").isEmpty()) {
                            lastVideoWidget = addYoutubeVideoReturnable(result.get("reportVideo"));
                        }else {
                            lastVideoWidget = null;
                        }
                    }
                });
//                lastVideoWidget = addYoutubeVideoReturnable(getLocalizedGenerateReportVideoUrl());
                onlyPaidSubscriptionMessage = new HTML(onlyPaidSubscriptionContent());
                bodyPanel.add(onlyPaidSubscriptionMessage);
                scrollToBottom();
                return;
            }
            messageInputContainer.getElement().getStyle().clearDisplay();
            clearLastWidgets(); // oldingi xabar va video olib tashlanadi
            reportGenerationEnabled = true;
            lastMessageWidget = typingFromAiReturnable(wfmMessages.reportPrompt());
            AiReportService.App.get().getAIVideoLinkMap(new AsyncCallback<Map<String,String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    lastVideoWidget = null;
                }

                @Override
                public void onSuccess(Map<String,String> result) {
                    if (result.get("reportVideo") != null && !result.get("reportVideo").isEmpty()) {
                        lastVideoWidget = addYoutubeVideoReturnable(result.get("reportVideo"));
                    }else {
                        lastVideoWidget = null;
                    }
                }
            });
            messageInput.getElement().setAttribute("placeholder", wfmMessages.reportPlaceholder());
            // Resize textarea to fit content
            messageInput.addKeyUpHandler(keyUpEvent -> {
                resizeTextarea();
            });
        });

        // Ask FAQ Button
        Anchor faqBtn = new Anchor(wfmStrings.askQuestion());
        faqBtn.setStyleName("btn btn--darkgrey");
        faqBtn.addClickHandler(event -> {
            messageInputContainer.getElement().getStyle().clearDisplay();
            clearLastWidgets();
            reportGenerationEnabled = false;
            lastMessageWidget = typingFromAiReturnable(wfmMessages.placeholderExample());
//            lastVideoWidget = addYoutubeVideoReturnable(getLocalizedAskQuestionVideoUrl());
            AiReportService.App.get().getAIVideoLinkMap(new AsyncCallback<Map<String,String>>() {
                @Override
                public void onFailure(Throwable caught) {
                    lastVideoWidget = null;
                }

                @Override
                public void onSuccess(Map<String,String> result) {
                    if (result.get("wikiVideo") != null && !result.get("wikiVideo").isEmpty()) {
                        lastVideoWidget = addYoutubeVideoReturnable(result.get("wikiVideo"));
                    }else {
                        lastVideoWidget = null;
                    }
                }
            });

            messageInput.getElement().setAttribute("placeholder", wfmMessages.placeholderExample());
        });

        buttonGroup.add(generateBtn);
        buttonGroup.add(faqBtn);

        bodyPanel.add(buttonGroup);
    }

    private String getLocalizedGenerateReportVideoUrl() {
        String lang = Utils.getUserLanguage();
        if ("uz".equalsIgnoreCase(lang)) return generateReportVideoUrlUz;
        if ("ru".equalsIgnoreCase(lang)) return generateReportVideoUrlRu;
        return generateReportVideoUrl;
    }

    private String getLocalizedAskQuestionVideoUrl() {
        String lang = Utils.getUserLanguage();
        if ("uz".equalsIgnoreCase(lang)) return askAQuestionVideoUrlUz;
        if ("ru".equalsIgnoreCase(lang)) return askAQuestionVideoUrlRu;
        return askAQuestionVideoUrl;
    }
    //resize textarea method
    private void resizeTextarea() {
        Element textarea = messageInput.getElement();
        textarea.getStyle().setProperty("height", "auto");

        // Устанавливаем максимальную высоту как 40% от высоты окна
        int maxHeight = (int) (Window.getClientHeight() * 0.4);
        int scrollHeight = textarea.getScrollHeight();
        textarea.getStyle().setProperty("height", Math.min(scrollHeight, maxHeight) + "px");

        // Включаем скролл, если контент превышает лимит
        if (scrollHeight > maxHeight) {
            textarea.getStyle().setProperty("overflowY", "auto");
        } else {
            textarea.getStyle().setProperty("overflowY", "hidden");
        }
    }

    private Widget typingFromAiReturnable(String text) {
        HTMLPanel updated = new HTMLPanel(
                "<div class='msg-ava'>K.A.R.E.</div>" +
                        "<span>" + SafeHtmlUtils.htmlEscape(text) + "</span>" +
                        "<sub>" + formatTime(new Date()) +
                        "<svg class='icon--double-check'><use href='/mainStyles/new-ui/icons/sprite__panels.svg#double-check'></use></svg>" +
                        "</sub>"
        );
        updated.setStyleName("msg msg--from");
        bodyPanel.add(updated);
        scrollToBottom();
        return updated;
    }

    private Widget addYoutubeVideoReturnable(String videoUrl) {
        HTMLPanel videoWrapper = new HTMLPanel(
                "<div class='msg-ava'>K.A.R.E.</div>" +
                        "    <div class='video-cover msg-img'>" +
                        "      <iframe src='" + videoUrl + "' " +
                        "              frameborder='0' " +
                        "              allow='accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; fullscreen' " +
                        "              allowfullscreen>" +
                        "      </iframe>" +
                        "    </div>"
        );

        videoWrapper.setStyleName("msg msg--img msg--from");

        // Click handler modal ochish uchun
        videoWrapper.addDomHandler(event -> {
            event.preventDefault();
            showVideoModal(videoUrl);
        }, ClickEvent.getType());

        bodyPanel.add(videoWrapper);
        scrollToBottom();
        return videoWrapper;
    }

    private void showVideoModal(String youtubeUrl) {
        RootPanel.get().addStyleName("has-guide-modal");

        if (videoModal == null) {
            videoModal = new KpiModal(false);
            videoModal.setDismissible(true);

            // Close button
            gwt.material.design.client.ui.html.Anchor buttonClose = new gwt.material.design.client.ui.html.Anchor();
            buttonClose.addStyleName("btn-small btn--close btn--circle");
            SvgIcon iconClose = new SvgIcon(SvgEnum.x);
            buttonClose.add(iconClose);
            buttonClose.addClickHandler(e -> videoModal.close());
            videoModal.addToWrapper(buttonClose);

            // Modal open/close styling
            videoModal.addOpenHandler(event -> RootPanel.get().addStyleName("has-guide-modal"));
            videoModal.addCloseHandler(event -> RootPanel.get().removeStyleName("has-guide-modal"));

        } else {
            videoModal.clearContent();
        }
        // YouTube iframe
        IFrame iFrame = new IFrame();
        iFrame.getElement().setAttribute("src", youtubeUrl);
        iFrame.getElement().setAttribute("sandbox", "allow-forms allow-scripts allow-same-origin allow-presentation");
        iFrame.getElement().setAttribute("allowfullscreen", "allowfullscreen");
        iFrame.setWidth("100%");
        iFrame.setHeight("100%");
        videoModal.add(iFrame);

        videoModal.open();
    }

    private void bindInputHandlers() {
        messageInput.getElement().setAttribute("placeholder", wfmStrings.typeHere() + "...");
        messageInput.addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !messageInput.getText().trim().isEmpty()) {
                disableMessageInput(true);
                sendMessageFromInput();
                event.preventDefault(); // Enter bosilganda yangi qatordan saqlaydi
            }
        });
    }

    private void sendMessageFromInput() {

        String userText = messageInput.getText().trim();
        if (userText.isEmpty()) return;

        addUserMessage(userText); // Chatga chiqarish
        messageInput.setText(""); // Inputni tozalash

        HTMLPanel typingWrapper = createTypingWrapper();
        bodyPanel.add(typingWrapper);
        scrollToBottom();

        AiReportService.App.get().getAiResponse(userText, reportGenerationEnabled, chatId, new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                bodyPanel.remove(typingWrapper);
                disableMessageInput(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(String[] aiResponse) {
                if (aiResponse[1] != null) {
                    setReportRpcUUID(aiResponse[1]);
                    ChartMessagePanel panel = new ChartMessagePanel(getReportRpcUUID());

                    if (aiResponse[2] != null) {
                        panel.loadChartAtIndex(0,aiResponse[2]);
                    }else {
                        panel.loadChartAtIndex(0,null);
                    }
                    bodyPanel.add(panel.getWidget());
                }
                replaceTypingWithResponse(typingWrapper, aiResponse[0]);
                disableMessageInput(false);
            }
        });
    }

    private void disableMessageInput(boolean value) {
        messageInput.getElement().setPropertyBoolean("readOnly", value);
    }
    private boolean isDisableMessageInput() {
        return messageInput.getElement().getPropertyBoolean("readOnly");
    }

    private void addUserMessage(String text) {
        if (employeeProfileItem == null) {
            showInitialsAvatar("??", text);
            return;
        }
        String imageUrl = employeeProfileItem.getEmployeeImageUrl();
        GWT.log("--------------------"+imageUrl+"--------------------");
        if (imageUrl != null && !imageUrl.isEmpty()) {
            showImageAvatar(imageUrl, text);
        } else {
            String initials = employeeProfileItem.getFirstName().substring(0, 1).toUpperCase() + employeeProfileItem.getLastName().substring(0, 1).toUpperCase();
            showInitialsAvatar(initials, text);
        }
    }

    private void addUserMessageByClicking(String text) {

        if (isDisableMessageInput()){
            return;
        }
        if (employeeProfileItem == null) {
            showInitialsAvatar("??", text);
            return;
        }
        String imageUrl = employeeProfileItem.getEmployeeImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            showImageAvatar(imageUrl, text);
        } else {
            String initials = employeeProfileItem.getFirstName().substring(0, 1).toUpperCase() + employeeProfileItem.getLastName().substring(0, 1).toUpperCase();
            showInitialsAvatar(initials, text);
        }
        HTMLPanel typingWrapper = createTypingWrapper();
        bodyPanel.add(typingWrapper);
        scrollToBottom();
        disableMessageInput(true);
        AiReportService.App.get().getAiResponse(text, reportGenerationEnabled, chatId, new AsyncCallback<String[]>() {
            @Override
            public void onFailure(Throwable caught) {
                bodyPanel.remove(typingWrapper);
                disableMessageInput(false);
                Info.warn(wfmStrings.sorrySomethingWentWrong());
            }

            @Override
            public void onSuccess(String[] aiResponse) {
                if (aiResponse[1] != null) {
                    setReportRpcUUID(aiResponse[1]);
                    ChartMessagePanel panel = new ChartMessagePanel(getReportRpcUUID());

                    if (aiResponse[2] != null) {
                        panel.loadChartAtIndex(0,aiResponse[2]);
                    }else {
                        panel.loadChartAtIndex(0,null);
                    }
                    bodyPanel.add(panel.getWidget());
                }
                replaceTypingWithResponse(typingWrapper, aiResponse[0]);
                disableMessageInput(false);
            }
        });
    }

    private void showImageAvatar(String imageUrl, String text) {
        HTMLPanel msg = new HTMLPanel(
                "<div class='msg-ava'>" +
                        "<img src='" + imageUrl + "' class='avatar-img' />" +
                        "</div>" +
                        "<span>" + SafeHtmlUtils.htmlEscape(text) + "</span>" +
                        "<sub>" + formatTime(new Date()) +
                        "<svg class='icon--double-check'>" +
                        "<use href='/mainStyles/new-ui/icons/sprite__panels.svg#double-check'></use></svg>" +
                        "</sub>"
        );
        msg.setStyleName("msg msg--to");
        bodyPanel.add(msg);
        scrollToBottom();
    }

    private void showInitialsAvatar(String initials, String text) {
        HTMLPanel msg = new HTMLPanel(
                "<div class='msg-ava'>" + initials + "</div>" +
                        "<span>" + SafeHtmlUtils.htmlEscape(text) + "</span>" +
                        "<sub>" + formatTime(new Date()) +
                        "<svg class='icon--double-check'>" +
                        "<use href='/mainStyles/new-ui/icons/sprite__panels.svg#double-check'></use></svg>" +
                        "</sub>"
        );
        msg.setStyleName("msg msg--to");
        bodyPanel.add(msg);
        scrollToBottom();
    }

    private HTMLPanel createTypingWrapper() {
        HTMLPanel msg = new HTMLPanel(
                "<div class='msg-ava'>K.A.R.E.</div>" +
                        "<div class='msg msg--from'>" +
                        "<div class='message stark'>" +
                        "<div class='typing typing-1'></div>" +
                        "<div class='typing typing-2'></div>" +
                        "<div class='typing typing-3'></div>" +
                        "</div>" +
                        "</div>"
        );
        msg.setStyleName("msg msg--from");
        return msg;
    }

    private void replaceTypingWithResponse(HTMLPanel typingMsg, String aiText) {
        HTMLPanel updated = new HTMLPanel(
                "<div class='msg-ava'>K.A.R.E.</div>" +
                        "<span>" + aiText + "</span>" +
                        "<sub>" + formatTime(new Date()) +
                        "<svg class='icon--double-check'><use href='/mainStyles/new-ui/icons/sprite__panels.svg#double-check'></use></svg>" +
                        "</sub>"
        );
        updated.setStyleName("msg msg--from");

        // WfmButton2 tugmalarini yaratamiz
        likeBtn = new WfmButton2();
        likeBtn.setStyleName("btn btn--icon btn--white");
        likeBtn.getElement().setTitle("Thumbs up");

        // SVG ikonani qo‘shish
        likeBtn.getElement().setInnerHTML(
                "<svg class='icon--likeStroke voteIcon-free'><use href='/mainStyles/new-ui/icons/sprite__panels.svg#likeStroke'></use></svg>" +
                        "<svg class='icon--likeFill voteIcon-pushed'><use href='/mainStyles/new-ui/icons/sprite__panels.svg#likeFill'></use></svg>"
        );

        dislikeBtn = new WfmButton2();
        dislikeBtn.setStyleName("btn btn--icon btn--white");
        dislikeBtn.getElement().setTitle("Thumbs down");
        dislikeBtn.getElement().setInnerHTML(
                "<svg class='icon--disLikeStroke voteIcon-free'><use href='/mainStyles/new-ui/icons/sprite__panels.svg#disLikeStroke'></use></svg>" +
                        "<svg class='icon--disLikeFill voteIcon-pushed'><use href='/mainStyles/new-ui/icons/sprite__panels.svg#disLikeFill'></use></svg>"
        );

        // Vote box paneli
        FlowPanel voteBox = new FlowPanel();
        voteBox.setStyleName("voteBox");
        voteBox.add(likeBtn);
        voteBox.add(dislikeBtn);

        // DOMga qo‘shamiz
        int index = bodyPanel.getWidgetIndex(typingMsg);
        bodyPanel.remove(typingMsg);
        bodyPanel.insert(updated, index);
        bodyPanel.add(voteBox);

        // Click handlerlarni qo‘llaymiz
        addVoteClickHandlers(likeBtn, dislikeBtn);
        scrollToBottom();
    }

    private void addVoteClickHandlers(WfmButton2 likeBtn, WfmButton2 dislikeBtn) {
        likeBtn.setTitle("Thumbs up");
        dislikeBtn.setTitle("Thumbs down");

        likeBtn.addClickHandler(clickEvent -> {
            userLiked = true;
            likeBtn.addStyleName("active");
            dislikeBtn.removeStyleName("active");
            showFeedbackPopup(likeBtn);
        });

        dislikeBtn.addClickHandler(clickEvent -> {
            userLiked = false;
            dislikeBtn.addStyleName("active");
            likeBtn.removeStyleName("active");
            showFeedbackPopup(dislikeBtn);
        });
    }

    private void showFeedbackPopup(Widget sourceButton) {
        PopupPanel popup = new PopupPanel(true); // Auto-hide
        popup.setStyleName("feedbackPopup");
        popup.getElement().getStyle().setZIndex(9999);
        popup.getElement().getStyle().setBackgroundColor("#fff");
        popup.getElement().getStyle().setPadding(12, Style.Unit.PX);

        // Kontent
        FlowPanel content = new FlowPanel();
        content.setStyleName("panel");

        // X tugmasi
        HTML closeBtn = new HTML(
                "<div class='btn--icon popupClose' style='position: absolute; top: 8px; right: 8px; cursor: pointer;'>" +
                        "<svg class='icon--x'>" +
                        "<use href='/mainStyles/new-ui/icons/sprite__panels.svg#x'></use>" +
                        "</svg>" +
                        "</div>"
        );
        closeBtn.addClickHandler(e -> popup.hide());

        // Textarea
        TextArea feedbackText = new TextArea();
        feedbackText.setStyleName("form-control");
        feedbackText.getElement().setAttribute("placeholder", "Tell us more");
        feedbackText.setVisibleLines(2);
        feedbackText.getElement().getStyle().setMarginTop(24, Style.Unit.PX); // X dan pastga

        // Submit tugmasi
        submitBtn = new WfmButton2("Submit");
        submitBtn.setStyleName("btn btn--primary");
        submitBtn.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        submitBtn.addClickHandler(e -> {
            String feedback = feedbackText.getText().trim();
            if (chatId != null && chatId > 0) {
                AiReportService.App.get().getChatHistory(chatId, chatHistoryLimit, new AsyncCallback<List<Map<String, String>>>() {
                    @Override
                    public void onSuccess(List<Map<String, String>> history) {
                        StringBuilder full = new StringBuilder();

                        full.append("📝 *Feedback Summary*\n\n");
                        full.append("💬 Feedback:\n").append(feedbackText).append("\n\n");
                        full.append("🕒 Submitted at: ").append(DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n\n");

                        full.append("👤 User Email: ").append(Utils.getUserEmail()).append("\n");
                        full.append("👤 User ID: ").append(Utils.getUserID()).append("\n");
                        full.append("👤 User Full Name: ").append(Utils.getUserFullName()).append("\n\n");

                        full.append("🏢 Company ID: ").append(Utils.getCompanyID()).append("\n");
                        full.append("🏢 Company Name: ").append(Utils.getCompanyName()).append("\n\n");

                        full.append("Recent Chat History:\n");
                        for (Map<String, String> entry : history) {
                            String role = entry.get("role");
                            String content = entry.get("content");
                            full.append(role).append(": ").append(content).append("\n\n");
                        }

                        sendFeedback(full.toString(), feedback);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        Info.show("Chat history yuklab bo‘lmadi. Faqat feedback yuboriladi.", Info.Type.WARNING);
                        sendFeedback(feedback, "");
                    }
                });
            } else {
                sendFeedback(feedback, "");
            }
        });

        // Layout
        FlowPanel row = new FlowPanel();
        row.setStyleName("form-row");

        FlowPanel colText = new FlowPanel();
        colText.setStyleName("col");
        colText.add(feedbackText);

        FlowPanel colBtn = new FlowPanel();
        colBtn.setStyleName("col-auto");
        colBtn.add(submitBtn);

        row.add(colText);
        row.add(colBtn);

        content.add(closeBtn);
        content.add(row);

        popup.setWidget(content);

        // Joylashuv
        int popupWidth = 320;
        int buttonLeft = sourceButton.getAbsoluteLeft();
        int buttonTop = sourceButton.getAbsoluteTop();
        int buttonWidth = sourceButton.getOffsetWidth();
        int buttonHeight = sourceButton.getOffsetHeight();

        int popupLeft = buttonLeft + (buttonWidth / 2) - (popupWidth / 2);
        int popupTop = buttonTop + buttonHeight + 8;

        int windowWidth = Window.getClientWidth();
        int windowHeight = Window.getClientHeight();

        if (popupLeft + popupWidth > windowWidth) popupLeft = windowWidth - popupWidth - 10;
        if (popupLeft < 0) popupLeft = 10;
        if (popupTop + 200 > windowHeight) popupTop = windowHeight - 200 - 10;

        popup.setPopupPosition(popupLeft, popupTop);
        popup.show();
    }

    public void sendFeedback(String feedbackContent, String feedbackText) {
        Email newEmail = new Email();
        newEmail.setFromEmail(Utils.getUserEmail());

        String feedbackType = userLiked ? "Like" : "Dislike";

        newEmail.setSubject("ID: " + Utils.getCompanyID() + "; Like/Dislike: " + feedbackType + "; Feedback: " + feedbackText + ";");
        newEmail.setToEmail("support@kpi.com");
        newEmail.setContent(feedbackContent);
        MessageCenterService.App.get().sendMessage(newEmail, new AbstractAsyncCallback<Integer>() {
            public void success(Integer result) {
                LoadingPanel.loading(false);
                if (result != null) {
                    Info.show(wfmStrings.yourMessageHasBeenSent(), Info.Type.INFO);
                } else {
                    Info.show(wfmStrings.couldNotConnectToTheServer(), Info.Type.WARNING);
                }
            }

            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.unexpectedErrorOccuredWhileSending(), Info.Type.WARNING);
            }
        });
    }

    public Div getWrapper() {
        return wrapper;
    }

    private void typingFromAi(String text) {
        HTMLPanel updated = new HTMLPanel(
                "<div class='msg-ava'>K.A.R.E.</div>" +
                        "<span>" + SafeHtmlUtils.htmlEscape(text) + "</span>" +
                        "<sub>" + formatTime(new Date()) +
                        "<svg class='icon--double-check'><use href='/mainStyles/new-ui/icons/sprite__panels.svg#double-check'></use></svg>" +
                        "</sub>"
        );
        updated.setStyleName("msg msg--from");
        bodyPanel.add(updated);
        scrollToBottom();
    }

    private String formatTime(Date date) {
        return DateTimeFormat.getFormat("HH:mm").format(date);
    }

    private class ChartMessagePanel {

        private final String reportRpcUUID;

        Element prevBtn;
        Element nextBtn;
        FlowPanel footer;

        private int currentChartIndex = 0;
        private final int totalCharts = 8;

        private final HTMLPanel chartWrapper;
        private final FlowPanel msgImg;

        private final Label currentLabel;
        private final Label totalLabel;

        public ChartMessagePanel(String reportRpcUUID) {
            this.reportRpcUUID = reportRpcUUID;

            chartWrapper = new HTMLPanel("");
            chartWrapper.setStyleName("msg msg--img msg--from");

            // Avatar
            HTMLPanel avatar = new HTMLPanel("div", "K.A.R.E.");
            avatar.setStyleName("msg-ava");
            chartWrapper.add(avatar);

            // Chart konteyneri
            FlowPanel loadingPanel = new FlowPanel();
            loadingPanel.setWidth("440px");
            loadingPanel.setHeight("320px");

            msgImg = new FlowPanel();
            msgImg.setStyleName("msg-img");
            msgImg.add(loadingPanel);
            chartWrapper.add(msgImg);

            // Footer (pagination + tugmalar)
            footer = new FlowPanel();
            footer.setStyleName("msg-actFooter");

            // Paginationni to‘liq HTMLPanel bilan yaratamiz
            HTMLPanel pagination = new HTMLPanel(
                    "<ul class='pgFlipp'>" +
                            "<li class='pgFlipp__prev active'></li>" +
                            "<li class='pgFlipp__index'>" +
                            "<span class='pgFlipp__cur'>1</span>" +
                            "<span class='pgFlipp__ttl'>" + totalCharts + "</span>" +
                            "</li>" +
                            "<li class='pgFlipp__next active'></li>" +
                            "</ul>"
            );
            footer.add(pagination);

            // Label elementlarini yaratib, o‘rnini almashtirish uchun
            currentLabel = new Label("1");
            currentLabel.setStyleName("pgFlipp__cur");
            totalLabel = new Label(String.valueOf(totalCharts));
            totalLabel.setStyleName("pgFlipp__ttl");

            // Paginationdagi index span'larini Label bilan almashtirish uchun
            FlowPanel indexPanel = new FlowPanel();
            indexPanel.setStyleName("pgFlipp__index");
            indexPanel.add(currentLabel);
            indexPanel.add(new HTML()); // agar kerak bo‘lsa, bo‘sh element
            indexPanel.add(totalLabel);

            // Pagination ichidagi ul'ni olish
            Element ulElement = pagination.getElement().getFirstChildElement();

            // ulElement dan prev, next li elementlarini olish
            prevBtn = ulElement.getFirstChildElement();
            nextBtn = (Element) ulElement.getChild(ulElement.getChildCount() - 1);

            // prev va next tugmalari uchun click eventlarini o‘rnatamiz
            DOM.sinkEvents(prevBtn, Event.ONCLICK);
            DOM.setEventListener(prevBtn, evt -> {
                evt.preventDefault();
                if (currentChartIndex > 0) {
                    loadChartAtIndex(currentChartIndex - 1,null);
                } else {
                    loadChartAtIndex(totalCharts - 1, null); // siklik aylantirish
                }
            });

            DOM.sinkEvents(nextBtn, Event.ONCLICK);
            DOM.setEventListener(nextBtn, evt -> {
                evt.preventDefault();
                if (currentChartIndex < totalCharts - 1) {
                    loadChartAtIndex(currentChartIndex + 1, null);
                } else {
                    loadChartAtIndex(0, null); // siklik aylantirish
                }
            });

            // Pagination ichida indexPanel ni li o‘rniga qo‘shish uchun
            ulElement.replaceChild(indexPanel.getElement(), ulElement.getChild(1));

            // Icon buttonlar
            FlowPanel btnGroup = new FlowPanel();
            btnGroup.setStyleName("btn-group");

            // === CHART ICON BUTTON ===
//            Anchor chartBtn = new Anchor();
//            chartBtn.setHref("#");
//            chartBtn.setStyleName("btn btn--icon btn-small");
//            chartBtn.getElement().setInnerHTML(
//                    "<svg class='icon--'>" +
//                            "<use href='/mainStyles/new-ui/icons/sprite__panels.svg#verticalchart2'></use>" +
//                            "</svg>"
//            );
//            chartBtn.addClickHandler(event -> {
//                event.preventDefault();
//                Window.alert("Chart icon clicked!");
//            });

            // === EDIT ICON BUTTON ===
            Anchor editBtn = new Anchor();
            editBtn.setHref("#");
            editBtn.setStyleName("btn btn--icon btn-small");
            editBtn.getElement().setInnerHTML(
                    "<svg class='icon--user'>" +
                            "<use href='/mainStyles/new-ui/icons/sprite__panels.svg#edit'></use>" +
                            "</svg>"
            );
            editBtn.addClickHandler(event -> {
                setClickCount(getClickCount() + 1);
                if (Utils.getPathName().contains("Reporting.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("reporting|stepControl/" + getClickCount() + "/aireport/" + Utils.encrypt(getReportRpcUUID()));
                    onCloseListener.run();
                } else if (Utils.hasPermission(PermissionConstants.REPORTING_SYSTEM) || Utils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU)) {
                    String openReport = GWT.getHostPageBaseURL() + "Reporting.html#reporting|stepControl/" + getClickCount() + "/aireport/" + Utils.encrypt(getReportRpcUUID());
                    Utils.openURL(openReport);
                }
            });

            // === SAVE ICON BUTTON ===
//            Anchor saveBtn = new Anchor();
//            saveBtn.setHref("#");
//            saveBtn.setStyleName("btn btn--icon btn-small");
//            saveBtn.getElement().setInnerHTML(
//                    "<svg class='icon--user'>" +
//                            "<use href='/mainStyles/new-ui/icons/sprite__panels.svg#save'></use>" +
//                            "</svg>"
//            );
//            saveBtn.addClickHandler(event -> {
//                event.preventDefault();
//                Window.alert("Save icon clicked!");
//            });

//            btnGroup.add(chartBtn);
            btnGroup.add(editBtn);
//            btnGroup.add(saveBtn);
            footer.add(btnGroup);
        }

        public Widget getWidget() {
            return chartWrapper;
        }

        public String getReportRpcUUID() {
            return reportRpcUUID;
        }

        public void loadChartAtIndex(int index, String chartType) {

            currentChartIndex = index;

            ChartTypeEnum typeEnum;
            if (chartType == null) {
                typeEnum = AIReportWidget.this.getChartTypeByIndex(index);
            } else {
                typeEnum= ChartTypeEnum.valueOf(chartType);
                currentChartIndex = getChartIndexByType(typeEnum);
            }
            LoadingPanel.loading(true, chartWrapper);
            ReportingService.App.get().getReportChartDataForAi(getReportRpcUUID(), typeEnum, new AbstractAsyncCallback<ChartData>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false, chartWrapper);
                    Info.warn(wfmStrings.sorrySomethingWentWrong());
                }

                @Override
                public void onSuccess(ChartData chartData) {
                    LoadingPanel.loading(false, chartWrapper);
                    msgImg.clear();
                    if (chartData == null) {
                        HTMLPanel placeholder = noResultContent();
                        msgImg.add(placeholder);
                        Window.alert("Chart data is empty");
                        return;
                    }
                    AbstractChart chart = ChartUtils.generateChart(chartData);
                    if (chart.getSeries().length > 0){
                        // Footer'ga pagination qo‘shamiz
                        chart.setSize("440px", "320px");

                        chartWrapper.remove(footer);  // prevent duplicate footer
                        chartWrapper.add(footer);     // reattach footer below cha
                        msgImg.add(chart);
                    }else {
                        HTMLPanel placeholder = noResultContent();
                        msgImg.add(placeholder);
                    }
                    updatePaginationLabels();
                    scrollToBottom();
                }
            });
        }

        private void updatePaginationLabels() {
            currentLabel.setText(String.valueOf(currentChartIndex + 1));
            totalLabel.setText(String.valueOf(totalCharts));
            updatePaginationClasses();
        }

        private void updatePaginationClasses() {
            if (prevBtn != null) {
                prevBtn.setClassName(currentChartIndex == 0 ? "pgFlipp__prev disabled" : "pgFlipp__prev active");
            }

            if (nextBtn != null) {
                nextBtn.setClassName(currentChartIndex == totalCharts - 1 ? "pgFlipp__next disabled" : "pgFlipp__next active");
            }
        }

    }

    private void scrollToBottom() {
        bodyPanel.getElement().setScrollTop(bodyPanel.getElement().getScrollHeight());
    }

    private int getChartIndexByType(ChartTypeEnum chartType) {
        switch (chartType) {
            case VERTICAL_BAR_CHART:
                return 0;
            case HORIZONTAL_BAR_CHART:
                return 1;
            case LINE_CHART:
                return 2;
            case AREA_CHART:
                return 3;
            case PIE_CHART:
                return 4;
            case DONUT_CHART:
                return 5;
            case SEMI_CIRCLE_DONUT_CHART:
                return 6;
            case FUNNEL_CHART:
                return 7;
            default:
                throw new IllegalArgumentException("Unknown chart type: " + chartType);
        }
    }

    private ChartTypeEnum getChartTypeByIndex(int index) {
        ChartTypeEnum typeEnum = null;
        switch (index) {
            case 0:
                typeEnum = ChartTypeEnum.VERTICAL_BAR_CHART;
                break;
            case 1:
                typeEnum = ChartTypeEnum.HORIZONTAL_BAR_CHART;
                break;
            case 2:
                typeEnum = ChartTypeEnum.LINE_CHART;
                break;
            case 3:
                typeEnum = ChartTypeEnum.AREA_CHART;
                break;
            case 4:
                typeEnum = ChartTypeEnum.PIE_CHART;
                break;
            case 5:
                typeEnum = ChartTypeEnum.DONUT_CHART;
                break;
            case 6:
                typeEnum = ChartTypeEnum.SEMI_CIRCLE_DONUT_CHART;
                break;
            case 7:
                typeEnum = ChartTypeEnum.FUNNEL_CHART;
                break;
            default:
                throw new IllegalArgumentException("Unknown chart index: " + index);
        }
        return typeEnum;
    }

    public HTMLPanel noResultContent() {
        HTMLPanel panel = new HTMLPanel(
                "<div class='sign-noRes' style='width: 440px; height: 320px; display: flex; flex-direction: column; justify-content: center; align-items: center; text-align: center; font-family: Arial, sans-serif; color: #666;'>" +
                        "   <div class='sign-noRes__img'>" +
                        "       <img class='sign-noRes__img-img' src='mainStyles/new-ui/icons/noDataTempSvg.svg' alt='No Data Available' />" +
                        "       <div class='chart-no-data'>No Data Available</div>" +
                        "   </div>" +
                        "   <div>" +
                        "       <p><strong>No Results</strong></p>" +
                        "       <p>Results cannot be displayed with current filters or grouping options.</p>" +
                        "   </div>" +
                        "</div>"
        );

        panel.setStyleName("sign-noRes"); // kerakli style
        panel.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // bu yerda editBtn'dagi logikani takrorlaymiz
                setClickCount(getClickCount() + 1);
                if (Utils.getPathName().contains("Reporting.html")) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("reporting|stepControl/" + getClickCount() + "/aireport/" + Utils.encrypt(getReportRpcUUID()));
                    onCloseListener.run();
                } else if (Utils.hasPermission(PermissionConstants.REPORTING_SYSTEM) || Utils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU)) {
                    String openReport = GWT.getHostPageBaseURL() + "Reporting.html#reporting|stepControl/" + getClickCount() + "/aireport/" + Utils.encrypt(getReportRpcUUID());
                    Utils.openURL(openReport);
                }
            }
        }, ClickEvent.getType());

        return panel;
    }

    public String onlyPaidSubscriptionContent() {
        return "<div class='msg msg--img msg--from'>" +
                "  <div class='msg-ava'>K.A.R.E.</div>" +
                "  <div class='msg-img'>" +
                "    <div class='sign-noRes' style='" +
                "        width: 440px;" +
                "        height: 320px;" +
                "        display: flex;" +
                "        flex-direction: column;" +
                "        justify-content: center;" +
                "        align-items: center;" +
                "        text-align: center;" +
                "        font-family: Arial, sans-serif;" +
                "        color: #666;" +
                "                       '>" +
                "        <p><strong>" + wfmMessages.onlyForPaidSubscription() +
                "           </strong></p>" +
                "    </div>" +
                "  </div>" +
                "</div>";
    }
}
