package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.ui.clickablePanel.ClickablePanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDialogContent;

import java.util.ArrayList;
import java.util.List;

public class UploadFile extends Composite implements CommandConstants, Constants, Clearable {


    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private List files;
    private List tables;
    private Element inner;
    private VerticalPanel panel;
    private WfmFormPanel form;
    private FileUpload fileUpload;
    private HorizontalPanel hp;
    private ProgressBar progressBar;
    private Image statusImg;
    private Button bt;
    private Button cancel;
    private KpiModal shell;
    private TextArea description;
    private TextArea description2;
    private SimpleLink authorizationLink;

    private Command uploadFinnished;
    private Command removeIfPressedESC;
    private Timer timer_check_submit;
    private Timer timer;

    private Integer id;
    private String baseStyle;
    private String uploadStyle;
    private String fromSection;

    private final int _DELAY_CHECK_SUBMIT = 1000;
    private int _DELAY_PROGRESS = 1000;
    public boolean isUploaded = true;
    public boolean isDone;
    private boolean showPopup;
    private boolean isValid;
    private boolean isCheked;
    private boolean d = true;

    public UploadFile(boolean showPopup) {
        this(showPopup, null);
    }

    public UploadFile(boolean showPopup, String uploadStyle) {
        this(showPopup, uploadStyle, null);
    }

    public UploadFile(boolean showPopup, String uploadStyle, String fromSection) {
        this.showPopup = showPopup;
        this.uploadStyle = uploadStyle;
        this.fromSection = fromSection;
        initialize();
    }

    public Command getRemoveIfPressedESC() {
        return removeIfPressedESC;
    }

    public void setRemoveIfPressedESC(Command removeIfPressedESC) {
        this.removeIfPressedESC = removeIfPressedESC;
    }

    public Command getUploadFinnished() {
        return uploadFinnished;
    }

    public void setUploadFinnished(Command uploadFinnished) {
        this.uploadFinnished = uploadFinnished;
    }

    public void setSpacing(int spacing) {
        panel.setSpacing(spacing);
    }

    public void clear() {
        files.clear();
        for (Object table : tables) {
            panel.remove((Widget) table);
        }
        tables.clear();
    }

    public void onUploadFile(ChangeHandler handler) {
        fileUpload.addChangeHandler(handler);
    }

    public String getDescr() {
        return description.getText();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private void initialize() {
        panel = new VerticalPanel();
        files = new ArrayList();
        tables = new ArrayList();
        baseStyle = "my-list";
        addFileField(new FlexTable());
        checkIsValidForGoogle(false);
        form = new WfmFormPanel("/CreateAttachment");
        if (fromSection != null) {
            form.setParameter(FROM_SECTION, fromSection);
        }
        form.setWidget(panel);
        form.addSubmitHandler(event -> isUploaded = false);
        form.addSubmitCompleteHandler(event -> {
            isDone = true;
            id = form.getObjectID();
            if (id != null) {
                setUploadDoneFileLink(100, wfmStrings.successfullyUploaded());
            }
            if (form.isSuccess()) {
                isUploaded = true;
            } else {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, true);
                //messageBox.setSize(350, 150);
                messageBox.setTitle(wfmStrings.error());
                if (form.getErrorString() == null) {
                    messageBox.setMessage(wfmStrings.errorOccurredDuringUploading());
                    setUploadErrorFileLink(0, wfmStrings.errorOccurredDuringUploading());
                } else {
                    messageBox.setMessage(form.getErrorString());
                    setUploadErrorFileLink(0, wfmStrings.error());
                }
                messageBox.open();
            }

            if (uploadFinnished != null) {
                uploadFinnished.execute();
            }
        });

        timer_check_submit = new Timer() {
            public void run() {
                if (!isEmpty()) {
                    cancel();
                    if (!showPopup) {
                        form.setParameter("hostId", "0");
                        form.submit();
                        uploadProgress();
                    } else {
                        shell.open();
                    }
                }
            }
        };
        timer_check_submit.scheduleRepeating(_DELAY_CHECK_SUBMIT);
        initWidget(form);
    }

    private void addFileField(FlexTable table) {
        isCheked = false;
        fileUpload = new FileUpload();
        if (uploadStyle != null && !"".equals(uploadStyle)) {
            fileUpload.addStyleName(uploadStyle);
        }
        files.add(fileUpload);
        fileUpload.setName(ATTACHMENT_PARAM_BASE + Integer.toString(files.size() - 1));

        description = new TextArea();
        description.setSize("345", "80px");
        description.setText(ADD_DESCRIPTION);
        description.setName(DESCRIPTION_PARAM_NAME);
        description2 = new TextArea();
        description2.setText(ADD_DESCRIPTION);
        description2.setName(DESCRIPTION_PARAM_NAME);
        description.addKeyPressHandler(event -> {
            if (description.getText().equals(ADD_DESCRIPTION)) {
                description.setText("");
            }
        });
        description.addClickHandler(sender -> {
            if (description.getText().equals(ADD_DESCRIPTION)) {
                description.setText("");
            }
        });

        shell = new KpiModal();
        shell.addCloseHandler(popupPanelCloseEvent -> {
            if (d) {
                if (removeIfPressedESC != null) {
                    removeIfPressedESC.execute();
                }
            }
        });

        shell.setSize("350px", "250px");
        shell.setTitle(wfmStrings.chooseFileStorage());

        final TextBox textBox = new TextBox();
        textBox.setVisible(false);
        textBox.setName(UPLOAD_TYPE_PARAM_NAME);
        textBox.setText(Utils.getUploadTypeParam());

        final RadioButton toGoogle = new KpiRadioButton("upload", wfmStrings.uploadToGoogleDocuments(), true);

        HTML googleIcon = new HTML();
        googleIcon.setSize("16px", "16px");
        googleIcon.setStyleName("google-icon");

        authorizationLink = new SimpleLink(wfmStrings.authorizationGoogleDocuments());
        authorizationLink.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
        authorizationLink.setVisible(false);
        authorizationLink.setWidth("200px");
        authorizationLink.addClickHandler(sender -> {
            authorizationLink.setVisible(false);
            new GoogleAuthorizationPanel(GOOGLE_DOCUMENTS, true);
        });

        toGoogle.addClickHandler(sender -> {
            isCheked = true;
            checkIsValidForGoogle(false);
            if (FAIL.equals(Cookies.getCookie(GOOGLE_DOCS_COOKIE))) {
                authorizationLink.setVisible(true);
            }
            textBox.setText(GOOGLE_DOCS_PARAM_NAME);
        });

        String uploadText = wfmStrings.uploadToWorkforcetrackS3Storage() + " "+Utils.getProductName() + " "+ wfmStrings.uploadToWorkforcetrackS3Storage1();

        RadioButton toWorkforce = new KpiRadioButton("upload", uploadText, true);
        toWorkforce.setValue(true);
        toWorkforce.addClickHandler(sender -> {
            isCheked = false;
            textBox.setText(Utils.getUploadTypeParam());
            authorizationLink.setVisible(false);
        });

        HTML wftIcon = new HTML();
        wftIcon.setSize("16px", "16px");
        wftIcon.setStyleName("wft-icon");

        ClickablePanel googlePanel = new ClickablePanel();
        googlePanel.addHorizontally(new HTML("&nbsp;&nbsp;"));
        googlePanel.addHorizontally(googleIcon);
        googlePanel.addHorizontally(new HTML("&nbsp;"));
        googlePanel.addHorizontally(toGoogle);

        ClickablePanel wftPanel = new ClickablePanel();
        wftPanel.addHorizontally(new HTML("&nbsp;&nbsp;"));
        wftPanel.addHorizontally(wftIcon);
        wftPanel.addHorizontally(new HTML("&nbsp;"));
        wftPanel.addHorizontally(toWorkforce);

        final MaterialDialogContent shellContainer = shell.getContent();
        shellContainer.add(new HTML("&nbsp;"));
        shellContainer.add(googlePanel);
        shellContainer.add(authorizationLink);
        shellContainer.add(wftPanel);
        shellContainer.add(new HTML("<b class=customTitle>&nbsp;&nbsp;&nbsp;" + wfmStrings.description() + "</b>"));
        shellContainer.add(description);
        bt = new Button(wfmStrings.upload());
        bt.addClickHandler(clickEvent -> {
//                if (Utils.isFromWorkforcetrack() && toGoogle.getValue()) {
            if (toGoogle.getValue()) {
                checkIsValidForGoogle(true);
            } else {
                upload();
            }
        });
        cancel = new Button(wfmStrings.cancel());
        cancel.addClickHandler(clickEvent -> shell.close());

        shell.addButton(bt);
        shell.addButton(cancel);
        table.setWidget(0, 0, fileUpload);
        table.setWidget(1, 2, textBox);
        table.setWidget(2, 0, description2);
        description2.setVisible(false);

        panel.add(table);
        tables.add(table);
    }

    private void checkIsValidForGoogle(final boolean upload) {
        bt.setEnabled(false);
        LoginService.App.get().isValid_User_For_Google_Gocs(new AbstractAsyncCallback<Boolean>() {
            public void failure(Throwable throwable) {
                bt.setEnabled(true);
            }

            public void success(Boolean result) {
                bt.setEnabled(true);
                isValid = result;
                if (isValid) {
                    Cookies.setCookie(GOOGLE_DOCS_COOKIE, CommandConstants.SUCCESS);
                    if (upload) {
                        upload();
                    }
                } else {
                    if (isCheked && upload) {
                        authorizationLink.setVisible(true);
                        Info.show(wfmStrings.youNeedAuthorizeToGoogleAccount(), Info.Type.WARNING);
                    }
                    Cookies.setCookie(GOOGLE_DOCS_COOKIE, FAIL);
                }
            }
        });
    }

    public void upload() {
        if (d) {
            d = false;
            shell.close();
            description2.setText(description.getText());
            form.submit();
            uploadProgress();
        }
    }

    protected void onRender() {
        inner = DOM.createDiv();

        inner.addClassName(baseStyle + "-inner");
        setElement(DOM.createDiv());
        DOM.appendChild(getElement(), inner);
        setStyleName(baseStyle + "-flat");

        DOM.appendChild(inner, panel.getElement());
    }

    public boolean validate() {
        return !isEmpty() && isDone;
    }

    public boolean isEmpty() {
        for (Object file : files) {
            FileUpload upload = (FileUpload) file;
            if (upload.getFilename() != null && !upload.getFilename().equals("")) {
                return false;
            }
        }

        return true;
    }

    public void clearSelected() {
        clear();
    }

    public void setUploadDoneFileLink(int percent, String msg) {
        progressBar.setProgress(percent);
        statusImg.setUrl("mainStyles/icons/accept.png");
        statusImg.setTitle(msg);

    }

    public void setUploadErrorFileLink(int percent, String msg) {
        timer.cancel();
        progressBar.setProgress(percent);
        statusImg.setUrl("mainStyles/icons/error.gif");
        statusImg.setTitle(msg);
    }

    public void uploadProgress() {
        FlexTable ft = (FlexTable) panel.getWidget(0);
        FileUpload file = (FileUpload) ft.getWidget(0, 0);
        if (file.getFilename() != null && !file.getFilename().equals("")) {
            progressBar = new ProgressBar(20);
            timer = new Timer() {
                public void run() {
                    int progress = progressBar.getProgress() + 4;
                    if (progress > 90) {
                        cancel();
                    }
                    if (progress > 50) {
                        _DELAY_PROGRESS = 2000;
                    }
                    progressBar.setProgress(progress);
                }
            };

            timer.scheduleRepeating(_DELAY_PROGRESS);
            hp = new HorizontalPanel();
            String ss = file.getFilename();
            hp.add(new Label(ss.substring(ss.lastIndexOf("\\") + 1)));
            hp.add(progressBar);
            statusImg = new Image("mainStyles/loading-animation.gif");
            hp.add(statusImg);
            ft.setWidget(0, 0, hp);
        }
    }
}

class ProgressBar extends VerticalPanel {

    private long startTime = System.currentTimeMillis();

    /**
     * The number of bar elements to show
     */
    private int elements = 20;

    /**
     * Current progress (as a percentage)
     */
    private int progress = 0;

    /**
     * This is the frame around the progress bar
     */
    private FlexTable barFrame = new FlexTable();

    /**
     * This is the grid used to show the elements
     */
    private Grid elementGrid;

    /**
     * This is the current text label below the progress bar
     */
    private Label remainLabel = new Label();

    /**
     * This is the current text label above the progress bar
     */
    private Label textLabel = new Label();

    /**
     * internal flags for options
     */

    /**
     * Base constructor for this widget
     *
     * @param elements The number of elements (bars) to show on the progress bar
     */
    public ProgressBar(int elements) {
        // Set element count
        this.elements = elements;

        // Styling
        remainLabel.setStyleName("progressbar-remaining");
        textLabel.setStyleName("progressbar-text");

        // Initialize the progress elements
        elementGrid = new Grid(1, elements);
        elementGrid.setStyleName("progressbar-inner");
        elementGrid.setCellPadding(0);
        elementGrid.setCellSpacing(0);

        for (int loop = 0; loop < elements; loop++) {
            Grid elm = new Grid(1, 1);
            //elm.setHTML(0, 0, "&nbsp;");
            elm.setHTML(0, 0, "");
            elm.setStyleName("progressbar-blankbar");
            elm.addStyleName("progressbar-bar");
            elementGrid.setWidget(0, loop, elm);
        }

        // Create the container around the elements
        Grid containerGrid = new Grid(1, 1);
        containerGrid.setCellPadding(0);
        containerGrid.setCellSpacing(0);
        containerGrid.setWidget(0, 0, elementGrid);
        containerGrid.setStyleName("progressbar-outer");

        // Set up the surrounding flex table based on the options
        barFrame.setWidget(0, 0, containerGrid);
        barFrame.setWidth("100%");

        // Add the frame to the panel
        add(barFrame);

        // Initialize progress bar
        setProgress(0);
    }

    /**
     * Set the current progress as a percentage
     *
     * @param percentage Set current percentage for the progress bar
     */
    public void setProgress(int percentage) {
        // Make sure we are error-tolerant
        if (percentage > 100) {
            percentage = 100;
        }
        if (percentage < 0) {
            percentage = 0;
        }

        // Set the internal variable
        progress = percentage;

        // Update the elements in the progress grid to
        // reflect the status
        int completed = elements * percentage / 100;
        for (int loop = 0; loop < elements; loop++) {
            Grid elm = (Grid) elementGrid.getWidget(0, loop);
            if (loop < completed) {
                elm.setStyleName("progressbar-fullbar");
                elm.addStyleName("progressbar-bar");
            } else {
                elm.setStyleName("progressbar-blankbar");
                elm.addStyleName("progressbar-bar");
            }
        }

        if (percentage > 0) {
            // Calculate the new time remaining
            long soFar = (System.currentTimeMillis() - startTime) / 1000;
            long remaining = soFar * (100 - percentage) / percentage;
            // Select the best UOM
            if (remaining > 120) {
                remaining = remaining / 60;
                if (remaining > 120) {
                    remaining = remaining / 60;
                }
            }
        } else {
            // If progress is 0, reset the start time
            startTime = System.currentTimeMillis();
        }
    }

    /**
     * Get the current progress as a percentage
     *
     * @return Current percentage for the progress bar
     */
    public int getProgress() {
        return (progress);
    }

    /**
     * Get the text displayed above the progress bar
     *
     * @return the text
     */
    public String getText() {
        return this.textLabel.getText();
    }

    /**
     * Set the text displayed above the progress bar
     *
     * @param text the text to set
     */
    public void setText(String text) {
        this.textLabel.setText(text);
    }

}
