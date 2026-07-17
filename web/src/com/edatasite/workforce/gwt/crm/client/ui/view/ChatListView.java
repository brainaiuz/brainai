package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.contact.client.rpc.MessageTo;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.rpc.WhatsAppService;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketMessageObject;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTextArea;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.upload.MessengersAttachment;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.DL;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_WHATSAPP_MEDIA;

public class ChatListView extends BaseListView {
    private FlowPanel selectedChatUser;
    private FlowPanel bodyPanel;
    private HTML userNameLabel;
    private Image userIconImage;
    private DL userDetailsPanel;
    private FlowPanel chatInfoPanel;
    private FlowPanel userIconImageWrapper;
    private HTML nameLabel;
    private FlowPanel chatList;
    private Integer contactId;
    private LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<MessageTo>>> allMessagesByContact;
    private KpiTextArea messageInput;
    private String selectedUserId;
    private String type;
    private Anchor activeTabLink = null;
    private HashMap<Integer, ContactItem> contactInfoMap;
    private MessengersAttachment fileUpload;


    public ChatListView(Integer contactId, String type) {
        super("Chat"); // Assuming a constructor with title in BaseListView
        this.contactId = contactId;
        this.type = type;
        setDescription(wfmStrings.whatsApp());
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    protected Widget onInitialize() {
        getContactsInfoMap();
        getAllChatsWithMessages();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_WHATSAPP_ATTACHMENT_ATTACHED, this, (sender, args) -> {
            MessageTo message = new MessageTo();
            message.setContactId(selectedUserId);
            message.setContactType(type);
            message.setFiles((ArrayList<FileResource>) args);
            message.setMessageType("FILE");
            sendMessage(message);
        });
        return null;
    }

    private void initializeChatInterface() {
        // Main chat container
        FlowPanel chatContainer = new FlowPanel();
        chatContainer.setStyleName("fullChat"); // Set the CSS class

        // Chat List
        ScrollPanel chatListPanel = createChatListPanel(); // Use the created method
        chatContainer.add(chatListPanel);
        chatContainer.add(createChatWindowPanel(contactId));
        chatContainer.add(createChatInfoPanel(contactId));

        // Add the main container to the view
        this.add(chatContainer); // 'this' refers to BaseListView
    }

    private ScrollPanel createChatListPanel() {
        ScrollPanel chatListPanel = new ScrollPanel();
        chatListPanel.setStyleName("fullChat__list");

        // Create a container panel to hold all elements inside the scroll panel
        VerticalPanel containerPanel = new VerticalPanel();

        // Create a FlowPanel to mimic the <ul> tag for tabs
        FlowPanel tabsPanel = new FlowPanel();
        tabsPanel.setStyleName("tabs tabs--indicator");

        // Manually create each list item for tabs to mimic the <li> tag
        tabsPanel.add(createTabListItem("All chats"));
        tabsPanel.add(createTabListItem("My chats"));
        tabsPanel.add(createTabListItem("Unassigned chats"));

        containerPanel.add(tabsPanel); // Add tabs to the container panel

        // Search bar and other components...
        FlowPanel searchPanel = new FlowPanel();
        searchPanel.setStyleName("fullChat__search");

        FlowPanel comboBoxWrapper = new FlowPanel();
        comboBoxWrapper.setStyleName("simpleGwt-ComboBox");

        TextBox searchBox = new TextBox();
        searchBox.setStyleName("gwt-SuggestBox form-control");
        comboBoxWrapper.add(searchBox);

        searchBox.addKeyUpHandler(event -> {
            String searchText = searchBox.getText().trim().toLowerCase(); // Get the search text
            updateChatList(searchText); // Update the chat list based on the search text
        });
        // For caret and reset icon, you can use Labels or HTML elements

        // Caret icon (span)
        InlineHTML caretSpan = new InlineHTML("<span class=\"caret\" style=\"display: inline;\"></span>");
        comboBoxWrapper.add(caretSpan);

        // Reset icon (span)
        InlineHTML resetSpan = new InlineHTML("<span class=\"simpleGwt-ComboBox__reset close\" style=\"display: inline;\"></span>");
        comboBoxWrapper.add(resetSpan);

        searchPanel.add(comboBoxWrapper);

        containerPanel.add(searchPanel); // Add the search panel to the container panel

        // Add the chat list with chat users
        containerPanel.add(createChatList()); // Add the chat list

        chatListPanel.add(containerPanel);
        return chatListPanel;
    }

    private FlowPanel createChatList() {
        chatList = new FlowPanel();
        chatList.setStyleName("chatList");

        if (allMessagesByContact != null) {
            allMessagesByContact.forEach((contactId, messagesByDate) -> {
                String lastMessageDate = getLastKey(messagesByDate);
                MessageTo lastMessage = messagesByDate.get(lastMessageDate).get(messagesByDate.get(lastMessageDate).size()-1);
                chatList.add(createChatUser(contactInfoMap.get(contactId).getFullName(), lastMessage.getMessage(), messagesByDate.get(lastMessageDate).get(messagesByDate.get(lastMessageDate).size()-1).getCreatedDate(), "icon--check", 2, contactId.toString(),true));
            });
        }

        addClickHandlerToFlowPanel(chatList);

        return chatList;
    }

    private FlowPanel createChatUser(String name, String message, String time, String iconClass, int badgeCount, String contactId,boolean contactSelected) {
        FlowPanel chatUser = new FlowPanel();
        if (contactSelected && this.contactId.equals(Integer.valueOf(contactId))) {
            selectedChatUser = chatUser;
            chatUser.setStyleName("chatUser selected");
        } else {
            chatUser.setStyleName("chatUser");

        }
        chatUser.getElement().setAttribute("data-userId", contactId); // Set a unique identifier

        // Chat user icon
        FlowPanel iconPanel = new FlowPanel();
        iconPanel.setStyleName("chatUser__icon");
        Image userImage = new Image("https://placehold.co/200");
        iconPanel.add(userImage);
        chatUser.add(iconPanel);

        // Chat user text
        FlowPanel textPanel = new FlowPanel();
        textPanel.setStyleName("chatUser__txt");
        HTML userName = new HTML("<h5>" + name + "</h5>");
        HTML userMessage = new HTML("<span>" + message + "</span>");
        textPanel.add(userName);
        if (message != null && !message.isEmpty()){
            textPanel.add(userMessage);
        }
        chatUser.add(textPanel);

        // Chat user actions
        FlowPanel actionsPanel = new FlowPanel();
        actionsPanel.setStyleName("chatUser__act");

        FlowPanel timePanel = new FlowPanel();
        timePanel.setStyleName("chatUser__time");
        timePanel.getElement().setInnerText(time);
        actionsPanel.add(timePanel);

        FlowPanel infPanel = new FlowPanel();
        infPanel.setStyleName("chatUser__inf");

        HTML checkIcon = new HTML("<svg class=\"" + iconClass + "\"><use href=\"../../WebContent/mainStyles/new-ui/icons/sprite__panels.svg#" + iconClass + "\"></use></svg>");
//        HTML badge = new HTML("<div class=\"badge\">" + badgeCount + "</div>");

        infPanel.add(checkIcon);
//        infPanel.add(badge);
        actionsPanel.add(infPanel);

        chatUser.add(actionsPanel);

        return chatUser;
    }

    private Widget createTabListItem(String title) {
        FlowPanel listItem = new FlowPanel();
        listItem.setStyleName("tab");

        Anchor tabLink = new Anchor(title);
        tabLink.addClickHandler(event -> {
            if (activeTabLink != null) {
                activeTabLink.removeStyleName("active");
            }
            switch (tabLink.getText()){
                case "All chats":
                    chatList.clear();
                    if (allMessagesByContact != null) {
                        allMessagesByContact.forEach((contactId, messagesByDate) -> {
                            String lastMessageDate = getLastKey(messagesByDate);
                            MessageTo lastMessage = messagesByDate.get(lastMessageDate).get(messagesByDate.get(lastMessageDate).size()-1);
                            chatList.add(createChatUser(contactInfoMap.get(contactId).getFullName(), lastMessage.getMessage(), messagesByDate.get(lastMessageDate).get(messagesByDate.get(lastMessageDate).size()-1).getCreatedDate(), "icon--check", 2, contactId.toString(),false));
                        });
                        addClickHandlerToFlowPanel(chatList);
                    }
                    break;
                case "My chats" :
                    chatList.clear();
                    if (allMessagesByContact != null) {
                        LinkedHashMap<String, ArrayList<MessageTo>> map = allMessagesByContact.get(contactId);
                        String lastMessageDate = getLastKey(map);
                        MessageTo lastMessage = map.get(lastMessageDate).get(map.get(lastMessageDate).size()-1);
                        chatList.add(createChatUser(contactInfoMap.get(contactId).getFullName(), lastMessage.getMessage(), map.get(lastMessageDate).get(map.get(lastMessageDate).size()-1).getCreatedDate(), "icon--check", 2, contactId.toString(),true));
                    }
                    addClickHandlerToFlowPanel(chatList);
                    break;
                case "Unassigned chats":
                    chatList.clear();
                    break;
            }
            tabLink.addStyleName("active");
            activeTabLink = tabLink;
        });
        listItem.add(tabLink);

        return listItem;
    }

    private FlowPanel createChatWindowPanel(Integer contactId) {
        ContactItem contactItem = contactInfoMap.get(contactId);
        FlowPanel fullChatPanel = new FlowPanel();
        fullChatPanel.setStyleName("fullChat__chat");

        // Panel representing <div class="chat">
        FlowPanel chatPanel = new FlowPanel();
        chatPanel.setStyleName("chat");

        // Chat header
        FlowPanel headerPanel = new FlowPanel();
        headerPanel.setStyleName("chat__contact");

        FlowPanel contactBarPanel = new FlowPanel();
        contactBarPanel.setStyleName("contactBar");



        // Using HTML widgets to insert specific HTML tags
        nameLabel = new HTML("<h5 class='contactBar__name'>" + contactItem.getFullName() + "</h5>");
        HTML lastSeenLabel = new HTML("<sup class='contactBar__inf'>AI Reporting Engine</sup>");

        contactBarPanel.add(nameLabel);
        contactBarPanel.add(lastSeenLabel);

        headerPanel.add(contactBarPanel);

        // Chat body
        bodyPanel = new FlowPanel();
        bodyPanel.setStyleName("chat__body");


        updateMessagePanels(contactId);


        FlowPanel chatActPanel = new FlowPanel();
        chatActPanel.setStyleName("chat__act");

        FlowPanel chatActContainer = new FlowPanel();
        chatActContainer.setStyleName("chatAct");

        fileUpload = new MessengersAttachment(F_WHATSAPP_MEDIA,null,null,true);
        // Button with icon
        FlowPanel chatActButtonPanel = new FlowPanel();
        chatActButtonPanel.setStyleName("chatAct__act");

        Span imageUploadButton = new Span();
        imageUploadButton.add(new Image("../mainStyles/new-ui/icons/upload.svg"));
        imageUploadButton.setStyleName("btn btn--icon");


        // Button with icon
        FlowPanel chatActSendButtonPanel = new FlowPanel();
        chatActSendButtonPanel.setStyleName("chatAct__act");

        Span sendMessageButton = new Span();
        sendMessageButton.add(new Image("../mainStyles/new-ui/icons/user-plus.svg"));
        sendMessageButton.setStyleName("btn btn--icon");
        sendMessageClickHandler(sendMessageButton);


        FlowPanel chatActSendVoiceButtonPanel = new FlowPanel();
        chatActSendVoiceButtonPanel.setStyleName("chatAct__act");

        Span sendVoiceMessageButton = new Span();
        sendVoiceMessageButton.add(new Image("../mainStyles/new-ui/icons/microphone.svg"));
        sendVoiceMessageButton.setStyleName("btn btn--icon");


        FlowPanel chatActInputPanel = new FlowPanel();
        chatActInputPanel.setStyleName("chatAct__input");
        messageInput = new KpiTextArea();
        messageInput.setHeight("10px");
        messageInput.setStyleName("form-control");
        messageInput.getElement().setAttribute("placeholder", "Type your message...");
        messageInput.addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && !messageInput.getText().isEmpty()) {
                sendMessageFromTextArea();
                scrollToBottom();
            }
        });
        // Other buttons (for user-plus and microphone icons) similar to paperClipButton

        // Assembling the chat action area
        chatActButtonPanel.add(fileUpload);
        chatActInputPanel.add(messageInput);
        chatActSendButtonPanel.add(sendMessageButton);
        chatActSendVoiceButtonPanel.add(sendVoiceMessageButton);
        // Add other buttons to chatActContainer as needed

        chatActContainer.add(chatActButtonPanel);
        chatActContainer.add(chatActInputPanel);
        chatActContainer.add(chatActSendButtonPanel);
        chatActContainer.add(chatActSendVoiceButtonPanel);
        // Add other panels to chatActContainer as needed

        chatActPanel.add(chatActContainer);


        // Adding header and body to the chat panel
        chatPanel.add(headerPanel);
        chatPanel.add(bodyPanel);
        chatPanel.add(chatActPanel);

        // Adding chat panel to the fullChat panel
        fullChatPanel.add(chatPanel);

        return fullChatPanel;
    }

    private FlowPanel createChatInfoPanel(Integer contactId) {
        String contactFullName = contactInfoMap.get(contactId).getFullName();
        String phoneNumber = contactInfoMap.get(contactId).getPhone();

        chatInfoPanel = new FlowPanel();
        chatInfoPanel.setStyleName("fullChat__info");

        // Chat User Panel
        FlowPanel chatUserPanel = new FlowPanel();
        chatUserPanel.setStyleName("chatUser");

        userIconImageWrapper = new FlowPanel();
        userIconImageWrapper.setStyleName("chatUser__icon");
        userIconImage = new Image("https://placehold.co/200"); // Replace with actual image URL
        userIconImageWrapper.add(userIconImage);
        chatUserPanel.add(userIconImageWrapper);

        userNameLabel = new HTML("<h5>" + contactFullName + "</h5>");
        userNameLabel.setStyleName("chatUser__txt");
        chatUserPanel.add(userNameLabel);

        chatInfoPanel.add(chatUserPanel);

        // Chat User Info

        userDetailsPanel = new DL();
        userDetailsPanel.setStyleName("chatUserInfo");


        createDetailItemForInfo(userDetailsPanel, new SvgIcon(SvgEnum.phone), phoneNumber, "Telephone");
        createDetailItemForInfo(userDetailsPanel, new SvgIcon(SvgEnum.work), "Unknown", "Work Status");
        createDetailItemForInfo(userDetailsPanel, new SvgIcon(SvgEnum.at), "Unknown", "Username");
        chatInfoPanel.add(userDetailsPanel);
        return chatInfoPanel;
    }

    private void createDetailItemForInfo(DL parentPanel, SvgIcon icon, String mainText, String subText) {
        // Create the dt element
        Element dtElement = DOM.createElement("dt");

        // Create the Span element for the icon
        Span spanElement = new Span();

        // Create the Image element for the icon
        spanElement.add(icon);

        // Set class for styling
        spanElement.setStyleName("icon-class"); // Replace 'icon-class' with the appropriate class

        // Append the Span element to the dt element
        dtElement.appendChild(spanElement.getElement());

        // Append the dt element to the parent panel
        parentPanel.getElement().appendChild(dtElement);

        // Create and append the dd element
        Element ddElement = DOM.createElement("dd");
        ddElement.setInnerHTML("<h5>" + mainText + "</h5><sup>" + subText + "</sup>");
        parentPanel.getElement().appendChild(ddElement);
    }

    private void updateMessagePanels(Integer contactId) {
        LinkedHashMap<String, ArrayList<MessageTo>> messagesByDate = allMessagesByContact.get(contactId);
        if (messagesByDate != null && !messagesByDate.isEmpty()) {
            bodyPanel.clear();
            nameLabel.setHTML("<h5 class='contactBar__name'>" + contactInfoMap.get(contactId).getFullName() + "</h5>");


            messagesByDate.forEach((k, v) -> {
                GWT.log(k);
                FlowPanel messageTime = new FlowPanel();
                messageTime.setStyleName("msgTime");
                Label time = new Label(k);
                messageTime.add(time);
                bodyPanel.add(messageTime);
                for (MessageTo messageTo : v) {
                    Label messageLabelFrom = new Label(messageTo.getMessage());
                    messageLabelFrom.setStyleName("messageLabel");
                    FlowPanel messagePanelFrom = new FlowPanel();
                    messagePanelFrom.setStyleName(messageTo.isCompanyMessage() ? "msg msg--to" : "msg msg--from");
                    String sendDate = "<sub>" + messageTo.getCreatedDate() + "</sub>";
                    HTMLPanel subElement = new HTMLPanel(sendDate);

                    if (messageTo.getMessage() != null && !messageTo.getMessage().isEmpty()){
                        messagePanelFrom.add(messageLabelFrom);
                        messagePanelFrom.add(subElement);
                        bodyPanel.add(messagePanelFrom);
                    }

                }
            });

        }
    }

    public void updateChatInfoPanel(String userName, String userIconUrl, String phoneNumber, String jobTitle, String username) {
        // Update user name and icon
        userNameLabel.setHTML("<h5>" + userName + "</h5>");
        userIconImage.setUrl(userIconUrl);

        // Remove the old userDetailsPanel from its parent (if it's already added)
        if (userDetailsPanel.isAttached()) {
            userDetailsPanel.removeFromParent();
        }

        // Create a new userDetailsPanel
        userDetailsPanel = new DL();
        userDetailsPanel.setStyleName("chatUserInfo");

        // Add new details to the new userDetailsPanel
        createDetailItemForInfo(userDetailsPanel, new SvgIcon(SvgEnum.phone), phoneNumber, "Telephone");
        createDetailItemForInfo(userDetailsPanel, new SvgIcon(SvgEnum.work), "Unknown", "Work Status");
        createDetailItemForInfo(userDetailsPanel, new SvgIcon(SvgEnum.at), "Unknown", "Username");

        // Add the new userDetailsPanel to the chatInfoPanel (or its appropriate container)
        chatInfoPanel.add(userDetailsPanel);
    }

    private void initListenerToChat() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MESSAGE_RECEIVED, (sender, args) -> {
            WebSocketMessageObject messageDetails = JsonUtils.safeEval(args + "");
            MessageTo messageTo = new MessageTo();
            messageTo.setObjectId(messageDetails.getObjectId());
            messageTo.setMessage(messageDetails.getMessage());
            messageTo.setCreatedDate(messageDetails.getCreatedDate());
            messageTo.setContactFullName(messageDetails.getContactFullName());
            messageTo.setPhoneNumber(messageDetails.getPhoneNumber());

            if (selectedUserId != null && selectedUserId.equals(messageDetails.getContactId())) {
                // Panel for the contact is open, so add the message to the UI
                Label messageLabelFrom = new Label(messageDetails.getMessage());
                messageLabelFrom.setStyleName("messageLabel");
                FlowPanel messagePanelFrom = new FlowPanel();
                messagePanelFrom.setStyleName("msg msg--from");
                String sendDate = "<sub>" + messageDetails.getCreatedDate() + "</sub>";
                HTMLPanel subElement = new HTMLPanel(sendDate);
                messagePanelFrom.add(messageLabelFrom);
                messagePanelFrom.add(subElement);
                bodyPanel.add(messagePanelFrom);
            }

            putMessageToMap(messageDetails.getDate(), messageTo,Integer.valueOf(messageDetails.getContactId()));

            scrollToBottom();


        });
    }

    private void getAllChatsWithMessages() {
        WhatsAppService.App.get().getAllWhatsappMessages(contactId,type,new AsyncCallback<LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<MessageTo>>>>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<MessageTo>>> result) {
                selectedUserId = String.valueOf(contactId);
                allMessagesByContact = result;
                WhatsAppService.App.get().getContactDataMap(contactId,type,new AsyncCallback<HashMap<Integer, ContactItem>>() {
                    @Override
                    public void onFailure(Throwable caught) {

                    }

                    @Override
                    public void onSuccess(HashMap<Integer, ContactItem> result) {
                        contactInfoMap = result;
                        initializeChatInterface();
                        initListenerToChat();
                    }
                });

            }
        });
    }

    private void getContactsInfoMap(){

    }

    private void addClickHandlerToFlowPanel(FlowPanel chatList) {
        for (int i = 0; i < chatList.getWidgetCount(); i++) {
            FlowPanel chatUser = (FlowPanel) chatList.getWidget(i);
            chatUser.addDomHandler(event -> {

                FlowPanel clickedPanel = (FlowPanel) event.getSource();
                selectedUserId = clickedPanel.getElement().getAttribute("data-userId");

                // Update style of previously selected user
                if (selectedChatUser != null) {
                    selectedChatUser.removeStyleName("selected");
                }

                // Update style of the currently selected user
                selectedChatUser = (FlowPanel) event.getSource();
                selectedChatUser.setStyleName("chatUser selected");

                updateMessagePanels(new Integer(selectedUserId));
                updateChatInfoPanel(contactInfoMap.get(new Integer(selectedUserId)).getFullName(), "https://placehold.co/200",contactInfoMap.get(new Integer(selectedUserId)).getPhone(), "Unknown", "Unknown");
                scrollToBottom();
            }, ClickEvent.getType());
        }
    }

    private void sendMessageClickHandler(Span sendMessageButton) {
        sendMessageButton.addClickHandler(event -> {
            sendMessageFromTextArea();
            scrollToBottom();
        });

    }

    private void sendMessageFromTextArea() {
        if (!messageInput.getText().isEmpty()) {
            DateTimeFormat hourFormat = DateTimeFormat.getFormat("HH:mm");
            DateTimeFormat dateFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
            String hour = hourFormat.format(new Date());
            String date = dateFormat.format(new Date());

            Label messageLabelFrom = new Label(messageInput.getText());
            messageLabelFrom.setStyleName("messageLabel");
            FlowPanel messagePanelFrom = new FlowPanel();
            messagePanelFrom.setStyleName("msg msg--to");
            String sendDate = "<sub>" + hour + "</sub>";
            HTMLPanel subElement = new HTMLPanel(sendDate);

            messagePanelFrom.add(messageLabelFrom);
            messagePanelFrom.add(subElement);
            bodyPanel.add(messagePanelFrom);


            MessageTo message = new MessageTo();
            message.setContactId(selectedUserId);
            message.setMessage(messageInput.getText());
            message.setCompanyMessage(true);
            message.setContactType(type);
            message.setDate(date);
            message.setCreatedDate(hour);
            message.setMessageType("text");

            putMessageToMap(date,message,null);

            messageInput.setValue("");

            sendMessage(message);


        }
    }

    private void putMessageToMap(String date, MessageTo newMessage,Integer contactId) {
        LinkedHashMap<String, ArrayList<MessageTo>> messagesForContact = allMessagesByContact.get(contactId != null ? contactId : Integer.valueOf(selectedUserId));
        if (messagesForContact == null) {
            messagesForContact = new LinkedHashMap<>();
            allMessagesByContact.put(contactId != null ? contactId : Integer.valueOf(selectedUserId), messagesForContact);
        }
        ArrayList<MessageTo> messagesForDate = messagesForContact.get(date);
        if (messagesForDate == null) {
            messagesForDate = new ArrayList<>();
            messagesForContact.put(date, messagesForDate);
        }

        messagesForDate.add(newMessage);
    }

    private void scrollToBottom() {
        bodyPanel.getElement().setScrollTop(bodyPanel.getElement().getScrollHeight());
    }


    private String getLastKey(LinkedHashMap<String, ArrayList<MessageTo>> map) {
        String lastKey = null;
        for (String key : map.keySet()) {
            lastKey = key;
        }
        return lastKey;
    }

    private void sendMessage(MessageTo message){
        WhatsAppService.App.get().sendMessage(message, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {

            }

            @Override
            public void onSuccess(Boolean result) {

            }
        });
    }

    private void updateChatList(String searchText) {
        // Iterate through contactInfoMap and filter contacts based on search text
        chatList.clear();
        contactInfoMap.forEach((contactId, contactItem) -> {
            String fullName = contactItem.getFullName().toLowerCase();
            if (fullName.contains(searchText)) {
                // If the contact's full name contains the search text, add it to the chat list
                LinkedHashMap<String, ArrayList<MessageTo>> messagesByDate = allMessagesByContact.get(contactId);
                if (messagesByDate != null && !messagesByDate.isEmpty()) {
                    String lastMessageDate = getLastKey(messagesByDate);
                    MessageTo lastMessage = messagesByDate.get(lastMessageDate).get(messagesByDate.get(lastMessageDate).size() - 1);
                    chatList.add(createChatUser(contactInfoMap.get(contactId).getFullName(), lastMessage.getMessage(), messagesByDate.get(lastMessageDate).get(messagesByDate.get(lastMessageDate).size() - 1).getCreatedDate(), "icon--check", 2, contactId.toString(), true));
                }
            }
        });
        addClickHandlerToFlowPanel(chatList);
    }


}








