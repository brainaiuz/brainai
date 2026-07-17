package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.view.GeneralPMNotesView;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * User: Ilhombek
 * Date: 14.10.2009
 * Time: 23:59:54
 */
public class ProjectNotesView extends GeneralPMNotesView implements Constants {

    private static final ProjectStrings projecStrings = ProjectStrings.App.get();
    private final Integer projectID;

    public ProjectNotesView(Integer projectID) {
        super("projectNotes", projecStrings.projectNotes(), projectID, true, true);
        this.projectID = projectID;
    }

    @Override
    public void getCommentsByTask(Integer relatedNoteId, final VerticalPanel commentPanel, final boolean isHideImage, final DisclosurePanel noteComments) {
        ProjectService.App.get().getProjectNoteComments(relatedNoteId, new AbstractAsyncCallback<NewsComment[]>() {
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
    public void getRelatedNotes(boolean withAllTaskNotes) {
        ProjectService.App.get().getProjectNotes(projectID, null, withAllTaskNotes, new AbstractAsyncCallback<HistoryListItem[]>() {
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

    public void getProjectMembers() {
        TaskService.App.get().getProjectAssigneesWithPositions(projectID, new AbstractAsyncCallback<HashMap<Integer, LinkedList<WfmTreeItem>>>() {
            @Override
            public void success(HashMap<Integer, LinkedList<WfmTreeItem>> result) {
                assigneesPanel.clearTreeView();
                LinkedList<WfmTreeItem> employees = result.get(0);
                LinkedList<WfmTreeItem> clientContacts = result.get(1);
                boolean allVisible = employees.size() != 0 || clientContacts.size() != 0;
                TreeSelect.setTickAllVisible(allVisible);

                final WfmTreeItem projectEmployeeItem = new WfmTreeItem(projectID, wfmStrings.members());
                final WfmTreeItem projectClientItem = new WfmTreeItem(projectID, Property.getPluralWithObjectCodeWithReplace(Constants.Contacts, wfmStrings.clientContact(), wfmStrings.contact()));


                if (employees.size() != 0) {
                    assigneesPanel.addTreeItem(projectEmployeeItem, employees);
                }
                if ((clientContacts.size() != 0)) {
                    assigneesPanel.addTreeItem(projectClientItem, clientContacts);
                }
                if (allVisible) {
                    assigneesPanel.expandTreeView();
                }
            }
        });
    }

    @Override
    public void relatedToID(HistoryListItem item) {
        item.setRelatedToId(1);
    }

    @Override
    public void saveData(NewsComment data, final Button addComment, final Integer objectID, final VerticalPanel addCommentPanel, final KpiModal kpiModal, final DisclosurePanel noteComments) {
        ProjectService.App.get().saveProjectNoteComments(data, new AbstractAsyncCallback<NewsComment>() {
            public void failure(Throwable caught) {
                addComment.setEnabled(true);
            }

            public void success(NewsComment result) {
                getCommentsByTask(objectID, addCommentPanel, true, noteComments);
                kpiModal.close();
                addComment.setEnabled(true);
            }
        });
    }

    @Override
    public void updateNoteComment(final NewsComment comments, final TextArea2 dialogBoxTextArea, final FlexTable commentTable) {
        ProjectService.App.get().saveProjectNoteComments(comments, new AbstractAsyncCallback<NewsComment>() {
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

    @Override
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