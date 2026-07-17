package com.edatasite.workforce.gwt.core.client.ui.dialogBox;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 27.09.2010
 * Time: 20:18:21
 */
public class WfmMessageBox extends KpiModal {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final HTML center = new HTML();
    private String btnMessage1;
    private String btnMessage2;
    private Action buttonStyle;
    private IconEnum iconEnum;
    private String message;
    private String pressedBtnName;
    public VerticalPanel contentPanel;
    private CloseHandler closeHandler;
    private String path;
    private WfmButton2 okButton;


    public WfmMessageBox(IconEnum iconEnum, boolean isModal) {
        this(iconEnum, null);
    }

    /**
     * The widget's constructor
     *
     * @param iconEnum        the iconEnum to display
     * @param buttonStyle the buttons to display
     */
    public WfmMessageBox(IconEnum iconEnum, Action buttonStyle) {
        this(iconEnum, buttonStyle, null, null);
    }

    public WfmMessageBox(IconEnum iconEnum, Action buttonStyle, boolean isModal) {
        this(iconEnum, buttonStyle, null, null);
    }

    /**
     * The widget's constructor
     *
     * @param iconEnum        the iconEnum to display
     * @param buttonStyle the buttons to display
     * @param message     the message to display
     */
    public WfmMessageBox(IconEnum iconEnum, Action buttonStyle, String message) {
        this(iconEnum, buttonStyle, message, null);
    }

    /**
     * The widget's constructor
     *
     * @param iconEnum        the iconEnum to display
     * @param buttonStyle the buttons to display
     * @param message     the message to display
     * @param closeHandel
     */
    public WfmMessageBox(IconEnum iconEnum, Action buttonStyle, String message, CloseHandler closeHandel) {
        this(iconEnum, buttonStyle, message, null, closeHandel);
    }

    /**
     * The widget's constructor
     *
     * @param iconEnum           the iconEnum to display
     * @param buttonStyle    the buttons to display
     * @param message        the message to display
     * @param buttonMessage1 the message to buttonMessage one
     * @param closeHandler
     */
    public WfmMessageBox(IconEnum iconEnum, Action buttonStyle, String message, String buttonMessage1, CloseHandler closeHandler) {
        this(iconEnum, buttonStyle, message, buttonMessage1, null, closeHandler);
    }

    /**
     * The widget's constructor
     *
     * @param iconEnum           the iconEnum to display
     * @param buttonStyle    the buttons to display
     * @param message        the message to display
     * @param buttonMessage1 the message to buttonMessage one
     * @param buttonMessage2 the message to buttonMessage two
     * @param closeHandler
     */
    public WfmMessageBox(IconEnum iconEnum, Action buttonStyle, String message, String buttonMessage1, String buttonMessage2, CloseHandler closeHandler) {
        super();
        this.iconEnum = iconEnum;
        this.buttonStyle = buttonStyle;
        this.message = message;
        this.btnMessage1 = buttonMessage1;
        this.btnMessage2 = buttonMessage2;
        this.closeHandler = closeHandler;
        generate();
    }

    /**
     * This is <<Builder Pattern>> and it's used while creating new instance.
     * And used after constructor.
     *
     * @param closeHandler
     * @return this
     */
    public WfmMessageBox addCloseHandler(CloseHandler closeHandler) {
        this.closeHandler = closeHandler;
        return this;
    }

    private void generate() {
        if (closeHandler == null) {
            closeHandler = new CloseHandler() {
                @Override
                public void onCancel() {

                }

                @Override
                public void onSubmit() {

                }
            };
        }
        // Create a VerticalPanel to contain the label and the buttons
        contentPanel = new VerticalPanel();
        contentPanel.addStyleName("WfmMessageBox__contentPanel");
//        contentPanel.setWidth("100%");

        // Create center message
        setIcon(iconEnum);
        if (buttonStyle != null) {
            switch (buttonStyle) {
                case OK:
                    getOkButton();
                    break;
                case OkCancel:
                    getOkCancelOrYesNoButton(true);
                    break;
                case YesNo:
                    getOkCancelOrYesNoButton(false);
                    break;
                case YesNoCancel:
                    getYesNoCancelButton();
                    break;
                case YesRefresh:
                    getYesRefreshButton();
                    break;
            }
        }
        if (!Action.OK.equals(buttonStyle)) {
            setCloseButton(false);
            removeCloseBtn();
        }
        addStyleName("WfmMessageBox");
        setMessage(message);
        center.addStyleName("wfmMessageBox__msg-wrapper");
        contentPanel.add(center);
        add(contentPanel);

//        setWidth(400);
    }

    private void getYesRefreshButton() {
            String btnTitle = btnMessage1 != null && !"".equals(btnMessage1) ? btnMessage1 : wfmStrings.yes();
            okButton = new WfmButton2(btnTitle, WfmButton2.BTN_PRIMARY);
            okButton.addClickHandler(event -> {
                close();
                getCloseHandler().onSubmit();
            });
            okButton.ensureDebugId("yes_button");
            addButton(okButton);
    }

    public void setIcon(IconEnum iconEnum) {

    }

    public void setMessage(String message) {
        center.setHTML("<table class='WfmMessageBox-msg'><tr><td>" + (path == null ? "" : path) +
                "</td><td>" + message + "</td></tr></table>");
    }

    public void setMessageCenter(String message) {
        center.setHTML("<table class='WfmMessageBox-msg'><tr><td>" + (path == null ? "" : path) +
                "</td><td align='center' >" + message + "</td></tr></table>");
    }

    public void setMessage(String message, String message2) {
        center.setHTML("<table class='WfmMessageBox-msg'><tr><td>" + (path == null ? "" : path) +
                "</td><td align='center'><b>" + message + "</b> <br/>" + message2 + " </td></tr></table>");
    }

    private void getOkButton() {

        String btnTitle = btnMessage1 != null && !"".equals(btnMessage1) ? btnMessage1 : "Ok";
        okButton = new WfmButton2(btnTitle, WfmButton2.BTN_PRIMARY);
        okButton.addClickHandler(event -> {
            pressedBtnName = "YES";
            close();
            getCloseHandler().onSubmit();
        });
        okButton.ensureDebugId("ok_button");
        addButton(okButton);
    }

    public CloseHandler getCloseHandler() {
        return closeHandler != null ? closeHandler : new CloseHandler() {
            @Override
            public void onCancel() {
                super.onCancel();    //To change body of overridden methods use File | Settings | File Templates.
            }

            @Override
            public void onSubmit() {
                super.onSubmit();    //To change body of overridden methods use File | Settings | File Templates.
            }
        };
    }

    private void getOkCancelOrYesNoButton(boolean isOkCancel) {
        String btnTitle = null;
        if (btnMessage2 != null && !"".equals(btnMessage2)) {
            btnTitle = btnMessage2;
        } else if (isOkCancel) {
            btnTitle = wfmStrings.cancel();
        } else {
            btnTitle = wfmStrings.no();
        }
        WfmButton2 cancelButton = new WfmButton2(btnTitle, WfmButton2.BTN_DEFAULT);
        cancelButton.addClickHandler(event -> {
            pressedBtnName = "NO";
            close();
            getCloseHandler().onCancel();
        });
        cancelButton.ensureDebugId("no_button");
        cancelButton.addStyleName("file--WfmMessageBox");
        addButton(cancelButton);

        if (btnMessage1 != null && !"".equals(btnMessage1)) {
            btnTitle = btnMessage1;
        } else if (isOkCancel) {
            btnTitle = wfmStrings.ok();
        } else {
            btnTitle = wfmStrings.yes();
        }
        WfmButton2 okButton = new WfmButton2(btnTitle, WfmButton2.BTN_PRIMARY);
        okButton.ensureDebugId("ok_button");
        okButton.addClickHandler(event -> {
            pressedBtnName = "YES";
            close();
            getCloseHandler().onSubmit();
        });
        addButton(okButton);
    }

    private void getYesNoCancelButton() {
        String btnTitle = btnMessage1 != null && !"".equals(btnMessage1) ? btnMessage1 : (wfmStrings.yes());
        WfmButton2 yesButton = new WfmButton2(btnTitle, WfmButton2.BTN_PRIMARY);
        yesButton.addClickHandler(event -> {
            pressedBtnName = "YES";
            close();
            getCloseHandler().onSubmit();
        });
        yesButton.ensureDebugId("yesButton");
        addButton(yesButton);

        btnTitle = ((btnMessage2 != null && !"".equals(btnMessage2)) ? btnMessage2 : wfmStrings.no());
        WfmButton2 noButton = new WfmButton2(btnTitle, WfmButton2.BTN_DEFAULT);
        noButton.addClickHandler(event -> {
            pressedBtnName = "NO";
            close();
            getCloseHandler().onSubmit();
        });
        noButton.ensureDebugId("noButton");
        addButton(noButton);

        WfmButton2 cancelButton = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancelButton.addClickHandler(event -> {
            pressedBtnName = "CANCEL";
            close();
            getCloseHandler().onCancel();
        });
        cancelButton.ensureDebugId("no_button");
        addButton(cancelButton);
    }

    public String getPressedButtonName() {
        return pressedBtnName;
    }

    @Override
    public void open() {
        super.open();
        if (okButton != null) {
            okButton.getElement().focus();
        }
    }

    public void setContent(Widget w) {
        contentPanel.insert(w, contentPanel.getWidgetCount());
    }

    public void replaceWidget(Widget w) {
        contentPanel.remove(center);
        contentPanel.insert(w, contentPanel.getWidgetCount());
    }
}
