package com.edatasite.workforce.gwt.employee.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.GeneralPMNotesView;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeServiceAsync;
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
 * Date: 15.10.2009
 * Time: 15:55:14
 */
public class EmployeeNotesView extends GeneralPMNotesView {

    private final EmployeeServiceAsync employeeService = EmployeeService.App.get();
    private Integer employeeID;

    public EmployeeNotesView(Integer employeeID) {
        super("employeeNotes", wfmStrings.employeeNotes(), employeeID, false);
        this.employeeID = employeeID;
    }

    @Override
    public void getCommentsByTask(Integer relatedNoteId, final VerticalPanel commentPanel, final boolean isHideImage, final DisclosurePanel noteComments) {
        employeeService.getEmployeeNoteComments(relatedNoteId, new AbstractAsyncCallback<NewsComment[]>() {
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
        employeeService.getEmployeeNotes(employeeID, new AbstractAsyncCallback<HistoryListItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                throwable.getMessage();
            }

            @Override
            public void success(HistoryListItem[] historyListItems) {
                getNotes(historyListItems);
            }
        });
    }

    @Override
    public void relatedToID(HistoryListItem item) {
        item.setRelatedToId(4);
    }

    @Override
    public void saveData(NewsComment data, final Button addComment, final Integer objectID, final VerticalPanel addCommentPanel, final KpiModal kpiModal, final DisclosurePanel noteComments) {
        employeeService.saveEmployeeNoteComments(data, new AbstractAsyncCallback<NewsComment>() {
            @Override
            public void failure(Throwable caught) {
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
        employeeService.saveEmployeeNoteComments(comments, new AbstractAsyncCallback<NewsComment>() {
            @Override
            public void failure(Throwable caught) {
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

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}