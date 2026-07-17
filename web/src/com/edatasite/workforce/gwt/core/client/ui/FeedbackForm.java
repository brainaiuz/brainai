/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/22 4:48:37                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.CoreMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportItem;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportService;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.UploadForm;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Aug 22, 2009
 * Time: 6:30:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class FeedbackForm extends Composite implements Constants {

    private static final CoreMessages coreMessages = CoreMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();


    private static final String QUESTION = wfmStrings.askYourQuestionHere();
    private static final String ISSUE = wfmStrings.describeYourIssueHere();
    private static final String SUGGESTION = wfmStrings.provideYourSuggestionHere();
    private static final String OTHER = wfmStrings.typeYourMessageHere();

    private FlexTable content;
    private String requestADemo;
    private String demoRequestTitle;

    private WfmButton2 send;
    private WfmButton2 cancel;
    private TextArea2 message;
    private UploadForm uploadForm;

    private HTML titleType;

    private String currentUserViewSummary;

    private int rowCounter;

    private boolean isIfram;

    private DataListBox dwFeedBackType;
    private static Integer questionid = 1;
    private static Integer issueid = 2;
    private static Integer suggestionid = 3;
    private static Integer otherid = 4;
    private Integer feedbackType = questionid;

    private TextBox txtSubject;

    public FeedbackForm(String feedBackMessage) {
        this(false, feedBackMessage);
    }

    public FeedbackForm() {
        this(false, null);
    }

    public FeedbackForm(boolean _isIfram, String requestFrom) {
        if (requestFrom != null && "requestADemo".equals(requestFrom)) {
            this.requestADemo = requestFrom;
        }
        this.isIfram = _isIfram;
        content = new FlexTable();

        rowCounter = 0;
        titleType = new HTML(QUESTION);

        dwFeedBackType = new DataListBox();
        dwFeedBackType.setWidth("120px");
        dwFeedBackType.ensureDebugId("dwFeedBackType");
        dwFeedBackType.addValueChangeHandler(changeEvent -> onChangeFeedbackType());
        fillFeedbackTypeList();

        txtSubject = new TextBox();
        txtSubject.setWidth("200px");
        txtSubject.ensureDebugId("txtSubject");

        message = new TextArea2(10000);
        message.counter.setWidth("115px");
        send = new WfmButton2(wfmStrings.send());
        cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        uploadForm = new UploadForm(false, CommandConstants.FOR_BUG);
        fillTable();

        initWidget(content);
    }

    private void fillFeedbackTypeList() {
        if (dwFeedBackType == null)
            dwFeedBackType = new DataListBox();

        dwFeedBackType.clear();
        dwFeedBackType.setItems(feedback_types);

        //default selected value is QUESTION
        dwFeedBackType.setSelected(feedbackType);
    }

    public static final SelectItem[] feedback_types = new SelectItem[]{
            new SelectItem(questionid, wfmStrings.question()),
            new SelectItem(issueid, Property.get(Constants.ISSUE, wfmStrings.issue())),
            new SelectItem(suggestionid, wfmStrings.suggestion()),
            new SelectItem(otherid, wfmStrings.other())
    };

    private void onChangeFeedbackType() {
        if (dwFeedBackType.getSelectedId() != null)
            feedbackType = dwFeedBackType.getSelectedId();

        if (feedbackType == 1)
            titleType.setText(QUESTION);
        if (feedbackType == 2)
            titleType.setText(ISSUE);
        if (feedbackType == 3)
            titleType.setText(SUGGESTION);
        if (feedbackType == 4)
            titleType.setText(OTHER);
    }

    private void fillTable() {
        content.setWidth("100%");
        content.setCellSpacing(4);
        if (isIfram || (requestADemo != null && "requestADemo".equals(requestADemo))) {
            content.setWidth("555px");
        }
        message.setSize("645px", "120px");
        send.setWidth("80px");
        cancel.setWidth("80px");

        initListener();
        String titleTypeHeight = "20px";
        String titleTypeWidth = null;
        int colSpan = 3;
        if (!(requestADemo != null && "requestADemo".equals(requestADemo))) {
            content.setHTML(rowCounter, 0, wfmStrings.selectYourFeedbackType());
            content.getFlexCellFormatter().setWidth(rowCounter, 0, "200px");
            content.setWidget(rowCounter, 1, dwFeedBackType);
            rowCounter++;
        } else {
            content.setHTML(rowCounter, 0, "");
            content.setHTML(rowCounter, 1, "");
            content.setHTML(rowCounter, 2, "");
            content.setHTML(rowCounter, 3, "");
            content.setHTML(rowCounter, 4, "");
            rowCounter++;
            titleTypeHeight = "40px";
            titleTypeWidth = "535px";
            colSpan = 2;
        }

        content.setHTML(rowCounter, 0, wfmStrings.subject());
        content.setWidget(rowCounter, 1, txtSubject);
        rowCounter++;

        final VerticalPanel messagePanel = new VerticalPanel();
        if (titleTypeWidth != null) {
            messagePanel.setWidth(titleTypeWidth);
        } else {
            messagePanel.setWidth("100%");
        }
        messagePanel.add(titleType);
        titleType.setHeight(titleTypeHeight);
        if (titleTypeWidth != null) {
            titleType.setWidth(titleTypeWidth);
        }
        messagePanel.add(message);

        content.setWidget(rowCounter, 0, messagePanel);
        content.getFlexCellFormatter().setColSpan(rowCounter, 0, 5);
        rowCounter++;

        content.setWidget(rowCounter, 0, uploadForm);
        content.getFlexCellFormatter().setColSpan(rowCounter, 0, 5);
        rowCounter++;
        if (Utils.getPhone() != null && !"null".equals(Utils.getPhone())) {
            String callUs = coreMessages.callUs(Utils.getPhone());

            content.setHTML(rowCounter, 0, callUs);
            content.getFlexCellFormatter().setColSpan(rowCounter, 0, 2);
        }
        final HorizontalPanel buttonBar = new HorizontalPanel();
        buttonBar.setSpacing(5);
        if ((requestADemo != null && "requestADemo".equals(requestADemo))) {
            buttonBar.setWidth("50%");
        } else {
            buttonBar.setWidth("100%");
        }
        buttonBar.add(send);
        if (!isIfram) {
            buttonBar.add(cancel);
        }
        content.setWidget(rowCounter, 1, buttonBar);
        content.getFlexCellFormatter().setColSpan(rowCounter, 1, colSpan);
        content.getFlexCellFormatter().setHorizontalAlignment(rowCounter, 1, HasHorizontalAlignment.ALIGN_RIGHT);
    }

    private void initListener() {
        cancel.addClickHandler(clickEvent -> closeShell());
        send.addClickHandler(event -> {
            send.setEnabled(false);
            save();

        });
    }

    public void setType(String type) {
        if (type != null && "requestADemo".equals(type)) {
            titleType.setHTML(wfmStrings.requestADemoPart1() + Utils.getProductName() + wfmStrings.requestADemoPart2());
        } else {
            if (type != null || !"".equals(type)) {
                boolean done = false;
                if ("question".equals(type)) {
                    dwFeedBackType.setSelected(questionid);
                    titleType.setText(QUESTION);
                    done = true;
                } else if ("suggestion".equals(type)) {
                    dwFeedBackType.setSelected(suggestionid);
                    titleType.setText(SUGGESTION);
                    done = true;
                } else if ("issue".equals(type)) {
                    dwFeedBackType.setSelected(issueid);
                    titleType.setText(ISSUE);
                    done = true;
                } else if ("other".equals(type)) {
                    dwFeedBackType.setSelected(otherid);
                    titleType.setText(OTHER);
                    done = true;
                }
                if (!done) {
                    dwFeedBackType.setSelected(otherid);
                    titleType.setText(OTHER);
                    done = true;
                }
            } else {
                dwFeedBackType.setSelected(otherid);
                titleType.setText(OTHER);
            }
        }
    }

    /**
     * DEMO REQUEST TITLE getter
     *
     * @return
     */
    public String getDemoRequestTitle() {
        return demoRequestTitle;
    }

    /**
     * DEMO REQUEST TITLE setter
     *
     * @param demoRequestTitle
     */
    public void setDemoRequestTitle(String demoRequestTitle) {
        this.demoRequestTitle = demoRequestTitle;
    }

    public void setText(String title) {
        if (title != null && !"".equals(title)) {
            message.setText(title);
        }
    }

    String messageType;

    private void save() {
        if (validate()) {
            if (requestADemo != null && "requestADemo".equals(requestADemo)) {
                messageType = "<b>" + wfmStrings.demoRequestUpperCase() + (demoRequestTitle != null ? demoRequestTitle : "") + "</b>: " + message.getText();
            } else if (dwFeedBackType.getSelectedId() == issueid) {
                messageType = "<b>" + wfmStrings.bugUpperCase() + "</b>: " + message.getText();
            } else if (dwFeedBackType.getSelectedId() == suggestionid) {
                messageType = "<b>" + wfmStrings.feedBackSuggestionUpperCase() + "</b>: " + message.getText();
            } else if (dwFeedBackType.getSelectedId() == otherid) {
                messageType = "<b>" + wfmStrings.feedBackOtherUpperCase() + "</b>: " + message.getText();
            } else {
                messageType = "<b>" + wfmStrings.feedBackQuestionUpperCase() + "</b>: " + message.getText();
            }

            if (!uploadForm.isEmpty()) {
                LoadingPanel.loading(true);
            } else {
                LoadingPanel.loading(true);
            }
            final Timer timer = new Timer() {
                public void run() {
                    if (uploadForm.isFinished()) {
                        this.cancel();
                        registrBug(messageType, txtSubject.getText());
                    }
                }
            };
            timer.scheduleRepeating(1000);
        } else {
            send.setEnabled(true);
            Info.show(wfmStrings.messageBodyShouldNotBeBlank(), Info.Type.WARNING);
        }


    }

    private WfmMessageBox wfmMessageBox;

    private void registrBug(String message, String subject) {
        final BugReportItem bugReportItem = new BugReportItem();
        bugReportItem.setReportText(message);
        bugReportItem.setSubjectText(subject);
        bugReportItem.setReportSection(currentUserViewSummary);
        bugReportItem.setUserAgent(Utils.getUserAgent());

        final ArrayList att = new ArrayList();
        for (int i = 0; i < uploadForm.getUploadFiles().size(); i++) {
            UploadFile uploadFile = (UploadFile) uploadForm.getUploadFiles().get(i);
            if (uploadFile.getId() != null) {
                final FileItem fileItem = new FileItem();
                fileItem.setId(uploadFile.getId());
                att.add(fileItem);
            }
        }

        final FileItem[] fileItems = new FileItem[att.size()];
        for (int i = 0; i < att.size(); i++) {
            fileItems[i] = (FileItem) att.get(i);
        }
        bugReportItem.setAttachments(fileItems);

        BugReportService.App.get().sendBugReport(bugReportItem, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable arg0) {
                send.setEnabled(true);
                closeShell();
                LoadingPanel.loading(false);
                String errorMessage = wfmStrings.errorOnSendingFeedback();
                if (requestADemo != null && "requestADemo".equals(requestADemo)) {
                    errorMessage = wfmStrings.errorOnSendingDemoRequest();
                }
                wfmMessageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, errorMessage);
                wfmMessageBox.setTitle(wfmStrings.error());
                wfmMessageBox.open();
            }

            public void success(Void arg0) {
                LoadingPanel.loading(false);
                closeShell();
                String successfullyMessage = wfmStrings.sentSuccessfullyFeedback();
                if (requestADemo != null && "requestADemo".equals(requestADemo)) {
                    successfullyMessage = wfmStrings.sentSuccessfullyDemoRequest();
                }
                wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK, successfullyMessage);
                wfmMessageBox.setTitle(wfmStrings.information());
                wfmMessageBox.open();
            }
        });

    }


    private boolean validate() {
        boolean valid = true;
        if (message.getText() == null || "".equals(message.getText())) {
            valid = false;
        }
        return valid;
    }

    private void closeShell() {
        if (shell != null) {
            shell.close();
        }
    }

    private KpiModal shell;

    public void setShell(KpiModal shell) {
        this.shell = shell;
    }

    public void setCurrentUserViewSummary(String currentUserViewSummary) {
        this.currentUserViewSummary = currentUserViewSummary;
    }

    public interface Images extends ClientBundle {

        @Source("com/edatasite/workforce/gwt/core/resource/icons/icon-help.png")
        ImageResource help();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/icon-error.png")
        ImageResource error();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/icon-lightbulb.png")
        ImageResource lightpub();

        @Source("com/edatasite/workforce/gwt/core/resource/icons/comments.png")
        ImageResource addicon();

    }

    public final static Images images = (Images) GWT.create(Images.class);
}
