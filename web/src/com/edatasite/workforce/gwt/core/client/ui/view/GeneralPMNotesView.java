package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.panel.VerticalPanelDiv;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * User: Ilhombek
 * Date: 30.11.2010
 * Time: 14:01:40
 */
public abstract class GeneralPMNotesView extends View {

    public static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SelectPanel assigneesPanel;
    private final TableColumn[] columns = new TableColumn[2];
    private FlexTable drawPanel;
    private HorizontalPanel horizontalPanel;
    private KpiEditor notesArea;
    private String notesValue;
    private boolean isSelectedEditorArea = false;
    private boolean isOnlyProjectAndTask = false;
    private final boolean isOnlyProject;
    private final Integer objectId;
    private VerticalPanel panel;
    private WfmButton2 saveButton;
    private TextBox subject;
    private VerticalPanelDiv verticalPanelDiv;
    private DataListBox visibilityListBox;
    private KpiCheckBox withAllTaskNotes;
    private final String COMMENTS_S_STRING = wfmStrings.comments();

    public GeneralPMNotesView(String name, String description, Integer objectId, boolean isOnlyProjectAndTask) {
        this(name, description, objectId, isOnlyProjectAndTask, false);
    }

    public GeneralPMNotesView(String name, String description, Integer objectId, boolean isOnlyProjectAndTask, boolean isOnlyProject) {
        super(name, description);
        this.objectId = objectId;
        this.isOnlyProjectAndTask = isOnlyProjectAndTask;
        this.isOnlyProject = isOnlyProject;
    }

    public abstract void getCommentsByTask(Integer relatedNoteId, final VerticalPanel commentPanel, final boolean isHideImage, final DisclosurePanel noteComments);

    public abstract void getRelatedNotes(boolean b);

    public void getProjectMembers() {
        //
    }

    public abstract void relatedToID(HistoryListItem item);

    public abstract void saveData(NewsComment data, final Button addComment, final Integer objectID, final VerticalPanel addCommentPanel, final KpiModal kpiModal, final DisclosurePanel noteComments);

    public abstract void updateNoteComment(final NewsComment comments, final TextArea2 dialogBoxTextArea, final FlexTable commentTable);

    public void setCommentCount(final DisclosurePanel noteComments, Integer countComment) {
        if (countComment > 0) {
            noteComments.getHeaderTextAccessor().setText(COMMENTS_S_STRING + "(" + countComment + ")");
        } else {
            noteComments.getHeaderTextAccessor().setText(COMMENTS_S_STRING);
        }
    }

    public void getNotes(HistoryListItem[] relatedNotes) {
        panel.clear();
        for (final HistoryListItem notes : relatedNotes) {
            DisclosurePanelImages disimage = GWT.create(MyDisclosurePanelImages.class);
            final DisclosurePanel noteComments = new DisclosurePanel(disimage, COMMENTS_S_STRING, false);
            final VerticalPanel commentPanel = new VerticalPanel();
            final VerticalPanel addCommentPanel = new VerticalPanel();
            HTML addTaskComment = new HTML("<a href='javascript:;' style='color:#1f4f8f'><b class=customTitle>" + wfmStrings.addYourComment() + "</b></a>");
            addTaskComment.ensureDebugId("addYourComment");
            DOM.setStyleAttribute(addTaskComment.getElement(), "color", "#1f4f8f");
            addTaskComment.addClickHandler(event -> getShowCommentPopup(notes.getObjectID(), addCommentPanel, noteComments));
            HorizontalPanel pane = new HorizontalPanel();
            pane.add(addTaskComment);
            commentPanel.insert(pane, 0);
            commentPanel.insert(addCommentPanel, 1);
            getCommentsByTask(notes.getObjectID(), addCommentPanel, true, noteComments);
            noteComments.add(commentPanel);
            String hour = String.valueOf(notes.getEventDate().getHours());
            if (notes.getEventDate().getHours() < 10) {
                hour = "0" + hour;
            }
            String minut = String.valueOf(notes.getEventDate().getMinutes());
            if (notes.getEventDate().getMinutes() < 10) {
                minut = "0" + minut;
            }
            FlexTable table = new FlexTable();
            String date = notes.getEventDate() != null ? DateUtils.format(notes.getEventDate()) + "      [" + hour + ":" + minut + "]" : "";
            HTML userName;
            if (Utils.userSettings.get(Constants.USER_FULLNAME).equals(notes.getEmployee())) {
                userName = new HTML("<b class=blueTitle>" + notes.getEmployee() + "</b>" + " " + wfmStrings.on() + " ");
            } else {
                userName = new HTML("<b class=redTitle>" + notes.getEmployee() + "</b>" + " " + wfmStrings.on() + " ");
            }

            HTML subjectHtml = new HTML(notes.getSubject().equals("") || notes.getSubject() == null ? "" : "<b>" + wfmStrings.subject() + " </b>" + notes.getSubject());
            HTML visib = new HTML("<b>-" + (notes.isVisibility() == null ? wfmStrings.internal() : notes.isVisibility() ? wfmStrings.priv() : wfmStrings.pub()) + "</b>");
            HorizontalPanel horz;
            horz = new HorizontalPanel();
            horz.add(userName);
            horz.add(new HTML(date));
            horz.add(visib);
            horz.setSpacing(5);

            table.setWidget(0, 0, horz);

            horz = new HorizontalPanel();
            horz.add(subjectHtml);
            horz.setSpacing(5);
            table.setWidget(1, 0, horz);

            horz = new HorizontalPanel();
            horz.add(new HTML(notes.getComment()));
            horz.setSpacing(5);
            table.setWidget(2, 0, horz);

            boolean isProjectTasksNote = withAllTaskNotes != null && withAllTaskNotes.getValue() &&
                    notes.getRelatedToName() != null && !"".equals(notes.getRelatedToName()) &&
                    notes.getRelatedName() != null && !"".equals(notes.getRelatedName()) &&
                    notes.getRelatedToId() == 2;//2 == TASK related

            if (!isProjectTasksNote) {
                horz = getNoteEditDeleteLinks(notes.getObjectID(), notes.getSubject(), notes.getComment(), notes.isVisibility(), notes.isEditable());
                horz.setSpacing(5);
                table.setWidget(3, 0, horz);
            }

            horz = new HorizontalPanel();
            horz.add(noteComments);
            horz.setSpacing(5);
            table.setWidget(4, 0, horz);

            if (isProjectTasksNote) {
                HTML relatedToName = new HTML(wfmStrings.relatedTo() + ": " + notes.getRelatedToName() + " - ");
                SimpleLink relatedLink = new SimpleLink("<b>" + notes.getRelatedName() + "</b>");
                relatedLink.addClickHandler(clickEvent -> {
//                        Window.open(GWT.getHostPageBaseURL() + notes.getSectionLink() + notes.getRelatedToLink(), "_blank", "");
                    SinksContainerFactory.entryPoint.onHistoryChanged(notes.getRelatedToLink());
                });
                HorizontalPanelDiv relatedToPanelDiv = new HorizontalPanelDiv();
                relatedToPanelDiv.add(3, relatedToName, relatedLink);
                horz = new HorizontalPanel();
                horz.add(relatedToPanelDiv);
                horz.setSpacing(5);
                table.setWidget(5, 0, horz);
            }

//            panel.insert(table, 0);
//            panel.insert(new HTML("<div class=line></div>"), 1);
            panel.add(table);
            panel.add(new HTML("<div class=line></div>"));
        }
    }

    @Override
    protected Widget onInitialize() {
        initialize();
        draw();
        return null;
    }

    private void deleteNoteComment(NewsComment comments, final SimpleLink deleteLink, final DisclosurePanel noteComments) {
        BugReportService.App.get().deleteNoteComment(comments.getCommentId(), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                String str = noteComments.getHeaderTextAccessor().getText().substring(COMMENTS_S_STRING.length() + 1, noteComments.getHeaderTextAccessor().getText().length() - 1);
                Integer countComment = Integer.parseInt(str) - 1;
                setCommentCount(noteComments, countComment);
                deleteLink.getParent().getParent().removeFromParent();
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.notes()), Info.Type.INFO);
            }
        });
    }

    private void deleteNoteWithComment(Integer noteId, final HorizontalPanel horz) {
        BugReportService.App.get().deleteNote(noteId, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                horz.removeFromParent();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTE_DELETE, result, GeneralPMNotesView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.note()), Info.Type.INFO);
            }
        });
    }

    private HorizontalPanel getNoteEditDeleteLinks(final Integer noteId, final String noteSubject, final String noteDescription, final Boolean isPublic, final boolean isEditable) { //isPublic da null xam keladi, null value bu Internal Note ligini bildiradi
        final HorizontalPanel horzPanel = new HorizontalPanel();
        final SimpleLink editLink = new SimpleLink(wfmStrings.edit().toLowerCase(), null, "", "task-comment-link", "editLink");
        final SimpleLink deleteLink = new SimpleLink(wfmStrings.delete().toLowerCase(), null, "", "task-comment-link", "deleteLink");
        editLink.getElement().getStyle().setMarginRight(3, Style.Unit.PX);
        deleteLink.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);

        editLink.addClickHandler(event -> createNoteEditDeletePopup(noteId, noteSubject, noteDescription, isPublic));

        deleteLink.addClickHandler(event -> confirmAndDeleteNote(noteId, horzPanel));

        final FlowPanel flowPanel = new FlowPanel();
        flowPanel.add(editLink);
        flowPanel.add(deleteLink);
        if (isEditable || Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR)) {
            horzPanel.add(flowPanel);
        }
        return horzPanel;
    }

    private void confirmAndDeleteNote(final Integer noteId, final HorizontalPanel horz) {
        final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo,
                                                              wfmStrings.areYouSureYouWanttoDeleteThisNote(), new CloseHandler() {
            @Override
            public void onSubmit() {
                deleteNoteWithComment(noteId, horz);
            }
        });
        wfmMessageBox.setTitle(wfmStrings.confirmation());
        wfmMessageBox.open();
    }

    public void getNoteComments(final NewsComment comments, final VerticalPanel commentPanel, final DisclosurePanel noteComments, boolean isHideImage) {
        final FlexTable commentTable = new FlexTable();
        commentTable.setCellSpacing(5);
        Image image = new Image();
        image.setWidth("68px");
        image.setHeight("87px");
        if (comments.getEmployeeImageUrl() != null) {
            image.setUrl(comments.getEmployeeImageUrl());
        } else {
            image = null;
        }
        final HTML commentHeader = new HTML("<b>" + (comments.getUsername() != null ? comments.getUsername() : "") + "</b> " +
                (comments.getDate() != null ? DateUtils.formatInternal(comments.getDate()) : ""));
        commentTable.setWidget(0, 0, commentHeader);
        commentHeader.setWordWrap(false);
        commentTable.getCellFormatter().setWordWrap(0, 0, false);

        if (isHideImage) {
            commentTable.setWidget(1, 0, image);
        } else {
            commentTable.setWidget(1, 0, new HTML());
        }
        final SimpleLink editLink = new SimpleLink(wfmStrings.edit().toLowerCase(), null, "", null, "task-comment-link");
        final SimpleLink deleteLink = new SimpleLink(wfmStrings.delete().toLowerCase(), null, "", null, "task-comment-link");
        editLink.getElement().getStyle().setMarginRight(3, Style.Unit.PX);
        deleteLink.getElement().getStyle().setMarginLeft(3, Style.Unit.PX);

        editLink.addClickHandler(event -> createCommentEditDeletePopup(comments, commentTable));

        deleteLink.addClickHandler(event -> {
            final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo,
                    wfmStrings.sureYouWantToDelete(), new CloseHandler() {
                @Override
                public void onSubmit() {
                    deleteNoteComment(comments, deleteLink, noteComments);
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.open();
        });

        final FlowPanel flowPanel = new FlowPanel();
        flowPanel.add(editLink);
        flowPanel.add(deleteLink);
        if (((comments.getUsername() != null && comments.getUsername().equals(Utils.getUserFullName())) || Utils.hasRole(Constants.ADMIN) || Utils.hasRole(Constants.DR))) {
            commentTable.setWidget(2, 0, flowPanel);
        }
        final HTML comment = new HTML(comments.getComment() != null ? comments.getComment() : "");
        comment.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        commentTable.setWidget(1, 1, comment);
        commentTable.getColumnFormatter().setWidth(0, "70px");
        commentTable.getFlexCellFormatter().setColSpan(0, 0, 2);
        commentPanel.add(commentTable);
    }

    private void draw() {
        saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId("issue_note_saveButton");
        saveButton.addClickHandler(clickEvent -> {
            saveButton.setEnabled(false);
            Boolean visibility = null;
            if (visibilityListBox.getItemText(visibilityListBox.getSelectedIndex()).equals(wfmStrings.pub())) {
                visibility = false;
            } else if (visibilityListBox.getItemText(visibilityListBox.getSelectedIndex()).equals(wfmStrings.priv())) {
                visibility = true;
            }
            save(null, null, visibility, null, false);
        });
        drawPanel.setWidget(1, 0, saveButton);
        if (isOnlyProject) {
            drawPanel.getFlexCellFormatter().setColSpan(1, 0, 2);
        }
        getRelatedNotes(withAllTaskNotes != null && withAllTaskNotes.getValue());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NOTE_ADD, GeneralPMNotesView.this, (sender, args) -> getRelatedNotes(withAllTaskNotes != null && withAllTaskNotes.getValue()));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NOTE_DELETE, GeneralPMNotesView.this, (sender, args) -> getRelatedNotes(withAllTaskNotes != null && withAllTaskNotes.getValue()));
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NOTE_EDIT, GeneralPMNotesView.this, (sender, args) -> getRelatedNotes(withAllTaskNotes != null && withAllTaskNotes.getValue()));
    }

    private void clearAndSetAssignees() {
        if (isOnlyProjectAndTask) {
            if (assigneesPanel != null) {
                assigneesPanel.clearTreeView();
                getProjectMembers();
            }
        }
    }

    private void createCommentEditDeletePopup(final NewsComment comments, final FlexTable commentTable) {
        final KpiModal dialogBox = new KpiModal();
        dialogBox.setTitle((wfmStrings.edit() + " " + wfmStrings.comment()));
        dialogBox.setPixelSize(200, 170);

        final TextArea2 dialogBoxTextArea = new TextArea2(3000);
        dialogBoxTextArea.setPixelSize(180, 130);
        dialogBoxTextArea.setText(comments.getComment());
        FlexTable vertDialogBox = new FlexTable();
        vertDialogBox.setStyleName("workforce");
        vertDialogBox.setWidget(0, 0, dialogBoxTextArea);

        Button saveButton = new Button(wfmStrings.save());
        saveButton.ensureDebugId("issue_note_saveButton");
        saveButton.addClickHandler(event -> {
            comments.setComment(dialogBoxTextArea.getText());
            dialogBox.close();
            updateNoteComment(comments, dialogBoxTextArea, commentTable);

        });
        vertDialogBox.setWidget(1, 0, saveButton);
        vertDialogBox.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_CENTER);
        Button closeButton = new Button(wfmStrings.close());
        closeButton.addClickHandler(event -> {
            dialogBox.close();
            dialogBoxTextArea.setText("");
        });
        vertDialogBox.setWidget(1, 1, closeButton);
        vertDialogBox.getFlexCellFormatter().setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_CENTER);

        vertDialogBox.getFlexCellFormatter().setColSpan(0, 0, 2);

        dialogBox.add(vertDialogBox);
        dialogBox.open();
    }

    private void generateVisibilityItems(final DataListBox listBox, final Boolean isPublic) {
        BugReportService.App.get().isEmployee(new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(Boolean val) {
                boolean isEmployee = val;
                if (isEmployee) {
                    listBox.setItems(new SelectItem[]{
                            new SelectItem(1, wfmStrings.pub()),
                            new SelectItem(2, wfmStrings.internal()),
                            new SelectItem(3, wfmStrings.priv())
                    });
                } else {
                    listBox.setItems(new SelectItem[]{
                            new SelectItem(1, wfmStrings.pub()),
                            new SelectItem(3, wfmStrings.priv())
                    });
                }
                if (isPublic == null && isEmployee) {
                    listBox.setSelected(2);
                } else {
                    if (isPublic) {
                        listBox.setSelected(3);
                    } else {
                        listBox.setSelected(1);
                    }
                }
            }
        });
    }

    private void createNoteEditDeletePopup(final Integer noteId, String noteSubject, String noteDescription, Boolean isPublic) {  //isPublic da null xam keladi, null value bu Internal Note ligini bildiradi
        final KpiModal dialogBox = new KpiModal();
        dialogBox.setTitle((wfmStrings.edit() + " " + wfmStrings.note()));
        dialogBox.setPixelSize(470, 220);

        final TextBox subjectBox = new TextBox();
        subjectBox.setWidth("180px");
        subjectBox.setText(noteSubject);

        final DataListBox visibilityBox = new DataListBox();
        visibilityBox.setWidth("100px");
        generateVisibilityItems(visibilityBox, isPublic);
        final KpiEditor dialogBoxTextArea = new KpiEditor();//3000
        dialogBoxTextArea.setWidth("450px");
        dialogBoxTextArea.setData(noteDescription);
        FlexTable editNoteTable = new FlexTable();
        editNoteTable.setStyleName("workforce");
        editNoteTable.setHTML(0, 0, wfmStrings.subject());
        editNoteTable.setWidget(0, 1, subjectBox);

        editNoteTable.setHTML(0, 2, wfmStrings.visibility());
        editNoteTable.setWidget(0, 3, visibilityBox);
        editNoteTable.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
        editNoteTable.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_MIDDLE);
        editNoteTable.getFlexCellFormatter().setVerticalAlignment(0, 2, HasVerticalAlignment.ALIGN_MIDDLE);
        editNoteTable.getFlexCellFormatter().setVerticalAlignment(0, 3, HasVerticalAlignment.ALIGN_MIDDLE);

        editNoteTable.setWidget(1, 0, dialogBoxTextArea);
        editNoteTable.getFlexCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);

        createNotesTextValue(dialogBoxTextArea);
        Button saveButton = new Button(wfmStrings.save());
        saveButton.ensureDebugId("issue_note_saveButton");
        saveButton.addClickHandler(event -> {
            Boolean isVisible = null;
            if (visibilityBox.getItemText(visibilityBox.getSelectedIndex()).equals(wfmStrings.pub())) {
                isVisible = false;
            } else if (visibilityBox.getItemText(visibilityBox.getSelectedIndex()).equals(wfmStrings.priv())) {
                isVisible = true;
            }
            save(noteId, subjectBox.getText(), isVisible, isSelectedEditorArea ? notesValue : dialogBoxTextArea.getData(), true);
            dialogBox.close();
//                updateNoteComment(comments, dialogBoxTextArea, commentTable);

        });
        editNoteTable.setHTML(2, 0, "&nbsp;");
        editNoteTable.setWidget(2, 1, saveButton);
        editNoteTable.getFlexCellFormatter().setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_CENTER);
        Button closeButton = new Button(wfmStrings.close());
        closeButton.ensureDebugId("close");
        closeButton.addClickHandler(event -> {
            dialogBox.close();
            subjectBox.setText("");
            dialogBoxTextArea.setData("");
        });
        editNoteTable.setWidget(2, 2, closeButton);
        editNoteTable.getFlexCellFormatter().setHorizontalAlignment(2, 2, HasHorizontalAlignment.ALIGN_CENTER);
        editNoteTable.setHTML(2, 3, "&nbsp;");

        editNoteTable.getFlexCellFormatter().setColSpan(1, 0, 4);

        dialogBox.add(editNoteTable);
        dialogBox.open();
    }

    private void createNotesTextValue(final KpiEditor editor) {
        editor.addClickHandler(clickEvent -> {
            isSelectedEditorArea = true;
            notesValue = editor.getData();
        });

    }

    private void initialize() {
        panel = new VerticalPanel();
        horizontalPanel = new HorizontalPanel();

        subject = new TextBox();
        subject.setWidth("180px");
        HTML subjectHtml = new HTML(wfmStrings.subject());

        notesArea = new KpiEditor(true);//3000
        notesArea.setWidth("450px");


        columns[0] = new TableColumn(wfmStrings.employee(), wfmStrings.employee());
        columns[1] = new TableColumn(wfmStrings.delete(), wfmStrings.action());
        assigneesPanel = new SelectPanel(columns);
        assigneesPanel.setTreePanelWidth(220);
        assigneesPanel.getTreeSelect().getElement().getStyle().setHeight(215, Style.Unit.PX);
        assigneesPanel.setHeight(180);
        assigneesPanel.setTableWidth(100);
        assigneesPanel.setTableHeight(100);
        assigneesPanel.getTable().setVisible(false);
        assigneesPanel.hideAvailablityCheckBox();

        drawPanel = new FlexTable();
        drawPanel.setCellSpacing(15);
        drawPanel.setWidth("100%");
        drawPanel.setWidget(2, 0, panel);
        if (isOnlyProject) {
            drawPanel.getFlexCellFormatter().setColSpan(2, 0, 2);
        }

        visibilityListBox = new DataListBox();
        visibilityListBox.setWidth("100px");
        generateVisibilityItems(visibilityListBox, null);
        visibilityListBox.addValueChangeHandler(event -> {
            if (isOnlyProjectAndTask) {
                if (visibilityListBox.getItemText(visibilityListBox.getSelectedIndex()).equals(wfmStrings.priv())) {
                    verticalPanelDiv.setVisible(false);
                    assigneesPanel.checkAllItems(false);
                } else {
                    verticalPanelDiv.setVisible(true);
                }
            }
        });
        HTML visHtml = new HTML(wfmStrings.visibility());

        horizontalPanel.add(subjectHtml);
        horizontalPanel.add(subject);
        horizontalPanel.add(visHtml);
        horizontalPanel.add(visibilityListBox);
        horizontalPanel.setSpacing(10);
        FlexTable flexTable = new FlexTable();
        flexTable.setWidget(0, 0, horizontalPanel);
        flexTable.setWidget(1, 0, notesArea);
        if (isOnlyProjectAndTask) {
            flexTable.setStyleName("workforce");
            verticalPanelDiv = new VerticalPanelDiv();
            verticalPanelDiv.add(2, new HTML(wfmStrings.sendNotification()), assigneesPanel);
            flexTable.setWidget(1, 1, verticalPanelDiv);
            flexTable.getFlexCellFormatter().getElement(1, 1).getStyle().setPaddingLeft(10, Style.Unit.PX);
            flexTable.getFlexCellFormatter().getElement(1, 1).getStyle().setPaddingBottom(5, Style.Unit.PX);
            getProjectMembers();//
        }

        drawPanel.setWidget(0, 0, flexTable);
        if (isOnlyProject) {
            withAllTaskNotes = new KpiCheckBox(wfmStrings.includeTaskNotes(), true);
            withAllTaskNotes.addValueChangeHandler(event -> {
                //register 'with all tasks notes' logic
                getRelatedNotes(event.getValue());
            });
            drawPanel.setWidget(0, 1, withAllTaskNotes);
            drawPanel.getFlexCellFormatter().getElement(0, 1).getStyle().setPaddingTop(11, Style.Unit.PX);
            drawPanel.getFlexCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_RIGHT);
            drawPanel.getFlexCellFormatter().setVerticalAlignment(0, 1, HasVerticalAlignment.ALIGN_TOP);
        }
        add(drawPanel);
    }

    private void getShowCommentPopup(final Integer objectID, final VerticalPanel addCommentPanel, final DisclosurePanel noteComments) {
        final KpiModal kpiModal = new KpiModal();
        kpiModal.setTitle(wfmStrings.addComment());
        final TextArea2 comment = new TextArea2(3000);
        comment.setWidth("400px");
        comment.setHeight("132px");
        kpiModal.add(comment);

        final Button addComment = new Button(wfmStrings.addComment());
        addComment.ensureDebugId("addComment");
        Button cancel = new Button(wfmStrings.cancel());
        cancel.ensureDebugId("cancel");
        addComment.addClickHandler(clickEvent -> {
            addComment.setEnabled(false);
            NewsComment data = new NewsComment();
            data.setComment(comment.getText());
            data.setNewsId(objectID);
            saveData(data, addComment, objectID, addCommentPanel, kpiModal, noteComments);
        });
        cancel.addClickHandler(clickEvent -> kpiModal.close());
        HorizontalPanel buttons = new HorizontalPanel();
        buttons.setStyleName("workforce");
        buttons.add(new Label());
        buttons.add(addComment);
        buttons.add(cancel);
        buttons.setCellWidth(buttons.getWidget(0), "194px");
        buttons.setSpacing(10);
        kpiModal.add(buttons);
        kpiModal.setSize("420px", "210px");
        kpiModal.open();
    }

    private void save(Integer noteId, String subjectText, Boolean isVisibility, String notes, final boolean isUpdate) {
        if (!isUpdate) {
            if (!validate()) {
                saveButton.setEnabled(true);
                return;
            }
        }
        HistoryListItem item = new HistoryListItem();
        item.setRelatedId(objectId);
        if (noteId != null) {
            item.setObjectID(noteId);
        }
        item.setSubject(subjectText != null ? subjectText : subject.getText());
        item.setComment(notes != null ? notes : notesArea.getData());
        relatedToID(item);
        item.setVisibility(isVisibility);
        if (assigneesPanel != null && assigneesPanel.getTreeSelect() != null &&
                assigneesPanel.getTreeSelect().getCheckedItems() != null && assigneesPanel.getTreeSelect().getCheckedItems().length > 0) {

            WfmTreeItem[] checkedItems = assigneesPanel.getTreeSelect().getCheckedItems();
            ArrayList<IdTime> assignees = new ArrayList<>();
            for (WfmTreeItem wfmTreeItem : checkedItems) {
                if (Constants.IS_EMPLOYEE.equals(wfmTreeItem.getDescription())) {
                    IdTime employee = new IdTime(wfmTreeItem.getId(), 0);
                    assignees.add(employee);
                } else if (Constants.IS_CLIENT.equals(wfmTreeItem.getDescription())) {
                    IdTime clientContact = new IdTime(wfmTreeItem.getId(), 1);
                    assignees.add(clientContact);
                }
            }
            item.setProjectEmployees(assignees.toArray(new IdTime[]{}));
        }
        LoadingPanel.loading(true);
        BugReportService.App.get().addNote(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable caught) {
                caught.getMessage();
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
                clearAndSetAssignees();
            }

            @Override
            public void success(final Integer result) {
                LoadingPanel.loading(false);
                saveButton.setEnabled(true);
                clearAndSetAssignees();
                if (isUpdate) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTE_EDIT, result, GeneralPMNotesView.this);
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.note()), Info.Type.INFO);
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTE_ADD, result, GeneralPMNotesView.this);

                    subject.setText("");
                    notesArea.setData("");
                    visibilityListBox.setSelected(2);
                    if (isOnlyProjectAndTask) {
                        verticalPanelDiv.setVisible(true);
                    }
                }
            }
        });
    }

    private boolean validate() {
        if (notesArea.getData() == null || "".equals(notesArea.getData())) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }
}