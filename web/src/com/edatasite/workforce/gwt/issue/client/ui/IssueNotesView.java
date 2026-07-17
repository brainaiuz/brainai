package com.edatasite.workforce.gwt.issue.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.GeneralPMNotesView;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueService;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Ilhombek
 * Date: 12/16/11
 * Time: 7:31 PM
 */
public class IssueNotesView extends GeneralPMNotesView implements Constants {

    private Integer int_issueID;
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    public IssueNotesView(Integer int_issueID) {
        super("issueNotes", projectStrings.issueNotes(), int_issueID, false);
        this.int_issueID = int_issueID;
    }

    @Override
    public void getCommentsByTask(Integer relatedNoteId, final VerticalPanel commentPanel, final boolean isHideImage, final DisclosurePanel noteComments) {
        IssueService.App.get().getIssueNoteComments(relatedNoteId, new AbstractAsyncCallback<NewsComment[]>() {
            @Override
            public void success(NewsComment[] result) {
                commentPanel.clear();
                if (result != null && result.length > 0) {
                    for (NewsComment aResult : result) {
                        getNoteComments(aResult, commentPanel, noteComments, isHideImage);
                    }
                    setCommentCount(noteComments, result.length);
                }
            }
        });
    }

    @Override
    public String getIconStyle() {
        return "bgMark pm-notes";
    }

    @Override
    public void getRelatedNotes(boolean withAllTaskNotes) {//withAllTaskNotes -> don't use here(only used project notes)
        IssueService.App.get().getIssueNotes(int_issueID, new AbstractAsyncCallback<HistoryListItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                throwable.getMessage();
            }

            @Override
            public void success(HistoryListItem[] result) {
                getNotes(result);
            }
        });
    }

    @Override
    public void relatedToID(HistoryListItem item) {
        item.setRelatedToId(7);
    }

    @Override
    public void saveData(NewsComment data, final Button addComment, final Integer objectID, final VerticalPanel addCommentPanel, final KpiModal kpiModal, final DisclosurePanel noteComments) {
        IssueService.App.get().saveIssueNoteComments(data, new AbstractAsyncCallback<NewsComment>() {
            @Override
            public void failure(Throwable throwable) {
                addComment.setEnabled(true);
            }

            @Override
            public void success(NewsComment result) {
                getCommentsByTask(objectID, addCommentPanel, true, noteComments);
                kpiModal.close();
                addComment.setEnabled(true);
            }
        });
    }

    @Override
    public void updateNoteComment(final NewsComment comments, final TextArea2 dialogBoxTextArea, final FlexTable commentTable) {
        IssueService.App.get().saveIssueNoteComments(comments, new AbstractAsyncCallback<NewsComment>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(NewsComment result) {
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.comment()), Info.Type.INFO);

                comments.setComment(dialogBoxTextArea.getText());
                commentTable.setWidget(1, 1, new Label(comments.getComment()));

                dialogBoxTextArea.setText("");
            }
        });
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}