package com.edatasite.workforce.gwt.core.client.ui.dialogBox;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

/**
 * Created by Shohruh on 14-Jan-16.
 */
public class WfmInputBox extends KpiModal {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final HTML center = new HTML();
    private String buttonMessage1;
    private String buttonMessage2;
    private Action buttonStyle;
    private IconEnum iconEnum;
    private String message;
    private String pressedBtnName;
    private TextBox textBox;
    public VerticalPanel contentPanel;
    private AbstractAsyncCallback<String> callbackHandler;
    private String path;
    private Button okButton;
    private HorizontalPanelDiv inputPanel;
    private HorizontalPanelDiv buttons;
    private String[] values;
    private String input;


    public WfmInputBox(IconEnum iconEnum, boolean isModal) {
        this(iconEnum, null);
    }

    /**
     * The widget's constructor
     *
     * @param iconEnum        the iconEnum to display
     * @param buttonStyle the buttons to display
     */
    public WfmInputBox(IconEnum iconEnum, Action buttonStyle) {
        this(iconEnum, buttonStyle, null, null, null);
    }

    public WfmInputBox(IconEnum iconEnum, Action buttonStyle, boolean isModal) {
        this(iconEnum, buttonStyle, null, null, null);
    }

    /**
     * The widget's constructor
     *
     * @param iconEnum        the iconEnum to display
     * @param buttonStyle the buttons to display
     * @param message     the message to display
     */
    public WfmInputBox(IconEnum iconEnum, Action buttonStyle, String message) {
        this(iconEnum, buttonStyle, message, null, null);
    }

    /**
     * The widget's constructor
     *
     * @param iconEnum        the iconEnum to display
     * @param buttonStyle the buttons to display
     * @param message     the message to display
     * @param values      valid input values
     * @param callbackHandler
     */
    public WfmInputBox(IconEnum iconEnum, Action buttonStyle, String message, String[] values, AbstractAsyncCallback<String> callbackHandler) {
        this(iconEnum, buttonStyle, message, values, null, callbackHandler);
    }

    /**
     * The widget's constructor
     *
     * @param iconEnum           the iconEnum to display
     * @param buttonStyle    the buttons to display
     * @param message        the message to display
     * @param values         valid input values
     * @param buttonMessage1 the message to buttonMessage one
     * @param callbackHandler
     */
    public WfmInputBox(IconEnum iconEnum, Action buttonStyle, String message, String[] values, String buttonMessage1, AbstractAsyncCallback<String> callbackHandler) {
        this(iconEnum, buttonStyle, message, values, buttonMessage1, null, callbackHandler);
    }

    /**
     * The widget's constructor
     *
     * @param iconEnum           the iconEnum to display
     * @param buttonStyle    the buttons to display
     * @param message        the message to display
     * @param values         valid input values
     * @param buttonMessage1 the message to buttonMessage one
     * @param buttonMessage2 the message to buttonMessage two
     * @param callbackHandler
     */
    public WfmInputBox(IconEnum iconEnum, Action buttonStyle, String message, String[] values, String buttonMessage1, String buttonMessage2, AbstractAsyncCallback<String> callbackHandler) {
        super();
        this.iconEnum = iconEnum;
        this.buttonStyle = buttonStyle;
        this.message = message;
        this.values = values;
        this.buttonMessage1 = buttonMessage1;
        this.buttonMessage2 = buttonMessage2;
        this.callbackHandler = callbackHandler;
        generate();
    }

    /**
     * This is <<Builder Pattern>> and it's used while creating new instance.
     * And used after constructor.
     *
     * @param callbackHandler
     * @return this
     */
    public WfmInputBox addCallbackHandler(AbstractAsyncCallback<String> callbackHandler) {
        this.callbackHandler = callbackHandler;
        return this;
    }

    private void generate() {
        if (callbackHandler == null) {
            callbackHandler = new AbstractAsyncCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    super.onSuccess(result);
                }

                @Override
                public void onFailure(Throwable caught) {
                    super.onFailure(caught);
                }
            };
        }
        // Create a VerticalPanel to contain the label and the buttons
        contentPanel = new VerticalPanel();
//        contentPanel.setStyleName("workforce");
        contentPanel.setWidth("100%");

        // Create center message
        setIcon(iconEnum);
        setMessage(message);
        inputPanel = getInputPanel();
        if (buttonStyle != null) {
            switch (buttonStyle) {
                case OK:
                    buttons = getOkButton();
                    break;
                case OkCancel:
                    buttons = getOkCancelOrYesNoButton(true);
                    break;
                case YesNo:
                    buttons = getOkCancelOrYesNoButton(false);
                    break;
                case YesNoCancel:
                    buttons = getYesNoCancelButton();
                    break;
            }
        }
        center.setStyleName("wfmMessageBox");
        contentPanel.add(center);
        if (inputPanel != null) {
            contentPanel.add(inputPanel);
        }
        if (buttons != null) {
            contentPanel.add(buttons);
        }
        contentPanel.setCellHorizontalAlignment(center, HasHorizontalAlignment.ALIGN_CENTER);
        if (inputPanel != null) {
            contentPanel.setCellHorizontalAlignment(inputPanel, HasHorizontalAlignment.ALIGN_CENTER);
        }
        if (buttons != null) {
            contentPanel.setCellHorizontalAlignment(buttons, HasHorizontalAlignment.ALIGN_CENTER);
        }
        contentPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        clearAndAdd(contentPanel);
        setSize(300, 150);
    }

    private HorizontalPanelDiv getInputPanel() {
        HorizontalPanelDiv inputPanel = new HorizontalPanelDiv();
        inputPanel.setStyleName("wfmMessageBox");
        textBox = new TextBox();
        textBox.ensureDebugId("input_textbox");
        inputPanel.add(textBox);
        inputPanel.setHorizontalSpacing(18);
        inputPanel.setMarginBottom(10);
        return inputPanel;
    }

    public void setIcon(IconEnum iconEnum) {

    }

    public void setMessage(String message) {
        center.setHTML("<table align='center' cellspacing='10' cellpadding='0' width='100%'><tr><td rowspan='2'>" + (path == null ? "" : path) +
                "</td><td style='padding:9px;'>" + message + "</td></tr></table>");
    }

    private HorizontalPanelDiv getOkButton() {
        final HorizontalPanelDiv buttonPanel = new HorizontalPanelDiv(true);
        buttonPanel.setStyleName("wfmMessageBox");
        okButton = new Button(((buttonMessage1 != null && !"".equals(buttonMessage1)) ?
                buttonMessage1 : "Ok"), (ClickHandler) event -> {
                    pressedBtnName = "YES";
                    if (validateInput()) {
                        close();
                        getCallbackHandler().onSuccess(input);
                    }
                });
        okButton.ensureDebugId("ok_button");
        okButton.setWidth("62px");
        okButton.setHeight("23px");
        buttonPanel.add(okButton);
        buttonPanel.setHorizontalSpacing(18);
        return buttonPanel;
    }

    public AbstractAsyncCallback<String> getCallbackHandler() {
        return callbackHandler != null ? callbackHandler : new AbstractAsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
            }

            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }
        };
    }

    private HorizontalPanelDiv getOkCancelOrYesNoButton(boolean isOkCancel) {
        final HorizontalPanelDiv buttonPanel = new HorizontalPanelDiv(true);
        buttonPanel.setStyleName("wfmMessageBox");
        final Button okButton = new Button(((buttonMessage1 != null && !"".equals(buttonMessage1)) ? buttonMessage1 :
                (isOkCancel ? wfmStrings.ok() : wfmStrings.yes())), (ClickHandler) event -> {
                    pressedBtnName = "YES";
                    if (validateInput()) {
                        close();
                        getCallbackHandler().onSuccess(input);
                    }
                });
        okButton.ensureDebugId("ok_button");
        buttonPanel.add(okButton);
        final Button cancelButton = new Button(((buttonMessage2 != null && !"".equals(buttonMessage2)) ? buttonMessage2 :
                (isOkCancel ? wfmStrings.cancel() : wfmStrings.no())), (ClickHandler) event -> {
                    pressedBtnName = "NO";
                    close();
                    getCallbackHandler().onSuccess(null);
                });
        cancelButton.ensureDebugId("no_button");
        cancelButton.addStyleName("file--WfmInputBox");
        buttonPanel.add(cancelButton);
        buttonPanel.setHorizontalSpacing(18);
        buttonPanel.setMarginBottom(8);
        return buttonPanel;
    }

    private HorizontalPanelDiv getYesNoCancelButton() {
        final HorizontalPanelDiv buttonPanel = new HorizontalPanelDiv(true);
        buttonPanel.setStyleName("wfmMessageBox");
        final Button yesButton = new Button((buttonMessage1 != null && !"".equals(buttonMessage1)) ?
                buttonMessage1 : (wfmStrings.yes()), (ClickHandler) event -> {
                    pressedBtnName = "YES";
                    input = textBox.getText();
                    if (validateInput()) {
                        close();
                        getCallbackHandler().onSuccess(input);
                    }
                });
        yesButton.ensureDebugId("yesButton");
        buttonPanel.add(yesButton);

        final Button noButton = new Button(((buttonMessage2 != null && !"".equals(buttonMessage2)) ?
                buttonMessage2 : wfmStrings.no()), (ClickHandler) event -> {
                    pressedBtnName = "NO";
                    close();
                    getCallbackHandler().onSuccess("");
                });
        noButton.ensureDebugId("noButton");
        buttonPanel.add(noButton);

        final Button cancelButton = new Button(wfmStrings.cancel(), (ClickHandler) event -> {
            close();
            getCallbackHandler().onSuccess(null);
        });
        cancelButton.ensureDebugId("no_button");
        buttonPanel.add(cancelButton);
        buttonPanel.setHorizontalSpacing(18);
        return buttonPanel;
    }

    public String getPressedButtonName() {
        return pressedBtnName;
    }

    private boolean validateInput () {
        input = textBox.getText();
        if (values != null && values.length > 0) {
            for (String s : values) {
                if (s != null && s.equalsIgnoreCase(input)) {
                    return true;
                }
            }
            textBox.setStyleName(Constants.ERROR_FORM_STYLE);
            return false;
        }
        return true;
    }

    @Override
    public void open() {
        super.open();
        if (okButton != null) {
            okButton.getElement().focus();
        }
    }

    public void setContent(Widget w) {
        remove(center);
        contentPanel.insert(w, buttons != null ? contentPanel.getWidgetIndex(buttons) : 1);
    }

    public HorizontalPanelDiv getButtonsPanel() {
        return buttons;
    }
}