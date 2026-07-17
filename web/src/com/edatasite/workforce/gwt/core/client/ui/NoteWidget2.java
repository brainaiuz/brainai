package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.jquery.client.api.JQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Hayot
 * Date: 4/3/12
 * Time: 12:11 PM
 */
public class NoteWidget2 extends SimplePanel implements Constants {
    protected VerticalPanel panel;
    protected MaterialRichEditorWithSuggestionsPopup textBox;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private int theLastNewID = 0;
    LinkedHashMap<Integer, NoteEntry> map = new LinkedHashMap<>();
    private Span tooltipWrapper;
    private String entityType;
    protected Integer entityID;
    private final static AllInOneServiceAsync allInOneService = AllInOneService.App.get();

    public NoteWidget2(Integer entityID, String entityType, HistoryListItem... notes) {
        this.entityID = entityID;
        this.entityType = entityType;
        drawInitialize();
        if (getElement() != null) {
            getElement().setAttribute("autocomplete", "off");
        }
    }

    private void drawInitialize() {
        init();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NOTE_ADD, NoteWidget2.this, (sender, args) -> {
            panel.clear();
            init();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NOTE_DELETE, NoteWidget2.this, (sender, args) -> {
            panel.clear();
            init();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_NOTE_EDIT, NoteWidget2.this, (sender, args) -> {
            panel.clear();
            init();
        });
    }

    private KpiRadioButton pub = new KpiRadioButton("visibility", wfmStrings.pub());
    private KpiRadioButton pri = new KpiRadioButton("visibility", wfmStrings.priv());
    private KpiRadioButton internal = new KpiRadioButton("visibility", wfmStrings.internal());

    private void init() {
        pub.ensureDebugId("pub-radioButton");
        internal.ensureDebugId("internal-radioButton");
        pri.ensureDebugId("pri-radioButton");
        if (panel == null || textBox == null) {
            clear();
            panel = new VerticalPanel();
            if (!Utils.hasOnlyRole(CLIENT)) {
                internal.setValue(Boolean.TRUE);
            } else {
                pub.setValue(Boolean.TRUE);
            }
            textBox = new MaterialRichEditorWithSuggestionsPopup();
            textBox.getMaterialRichEditor().addStyleName("getMaterialRichEditor");
            textBox.getMaterialRichEditor().setWidth("auto"); // override the property set in the file MaterialRichEditorWithSuggestionsPopup.java
            textBox.addStyleName("noteTextArea");
            textBox.ensureDebugId("note-textArea");
            if (MEETING_MINUTES.equals(entityType)) {
                textBox.addStyleName("size190_70");
            } else {
                textBox.addStyleName("size470_70");
            }
//            textBox.addKeyDownHandler(event -> {
//                Info.warn(textBox.getData());
//                if ((event.getNativeEvent().getKeyCode() == 10 || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) && event.getNativeEvent().getCtrlKey()) {
//                    if (textBox.getData() != null && !"".equals(textBox.getData().trim())) {
//                        if (editingKey != null) {
//                            HistoryListItem item = map.get(editingKey).getNote();
//                            item.setComment(textBox.getData());
//                            createNote(item, true);
//                        } else {
//                            createNote(new HistoryListItem(textBox.getData()), true);
//                        }
//                    }
//                    textBox.setData("");
//                }
//            });
            VerticalPanel vp = new VerticalPanel();
            vp.add(textBox);

            //Info
            tooltipWrapper = new Span();
            setTooltipClass();
            Window.addResizeHandler(e -> setTooltipClass());

            String publicAction = "<b>".concat(wfmStrings.pub()).concat("</b>").concat(" - ").concat(wfmStrings.publicInform());
            String internalAction = "<b>".concat(wfmStrings.internal()).concat("</b>").concat(" - ").concat(wfmStrings.internalInform());
            String privateAction = "<b>".concat(wfmStrings.priv()).concat("</b>").concat(" - ").concat(wfmStrings.privateInform());
            String infoText = publicAction.concat("<br>").concat(internalAction).concat("<br>").concat(privateAction);

            Icon iInfo = new Icon();
            iInfo.setClass("ficon--info");
            MaterialLink iconLink = new MaterialLink();
            iconLink.add(iInfo);
            String activation = "infoDropDown";
            iconLink.setActivates(activation);

            MaterialDropDown dropDown = new MaterialDropDown(activation);
            dropDown.addStyleName("dropdown-content dropdown-content-tooltip");
            dropDown.getElement().setInnerHTML(infoText);
            dropDown.setHover(true);

            tooltipWrapper.add(iconLink);
            tooltipWrapper.add(dropDown);

            WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
            save.getElement().removeClassName("optBtn2");
            save.ensureDebugId("notes-save");
            save.addClickHandler(clickEvent -> {
                if (textBox.getData() != null && !"".equals(textBox.getData().trim())) {
                    if (editingKey != null) {
                        HistoryListItem item = map.get(editingKey).getNote();
                        item.setComment(textBox.getData());
                        createNote(item, true);
                    } else {
                        createNote(new HistoryListItem(textBox.getData()), true);
                    }
                }
                textBox.setData("");
            });
            MaterialPanel buttonsPanel = new MaterialPanel("noteWidget__buttons");
            MaterialPanel radioButtonDiv = new MaterialPanel();
            MaterialPanel saveDiv = new MaterialPanel();
            if (!Utils.hasOnlyRole(CLIENT)) {
                radioButtonDiv.add(pub);
                radioButtonDiv.add(internal);
                radioButtonDiv.add(pri);
                radioButtonDiv.add(tooltipWrapper);
                saveDiv.add(save);

                //buttonsPanel.add(radioButtonDiv);
                buttonsPanel.add(saveDiv);
            } else {

                radioButtonDiv.add(pub);
                radioButtonDiv.add(pri);
                radioButtonDiv.add(tooltipWrapper);
                saveDiv.add(save);

//                buttonsPanel.add(radioButtonDiv);
                buttonsPanel.add(saveDiv);
            }
            vp.add(buttonsPanel);
            ScrollPanel sp = new ScrollPanel(panel);
            sp.addStyleName("notesPanel");
            sp.getElement().setAttribute("style", "max-height:200px; overflow-x: hidden; width: 100%");
            vp.add(sp);
            add(vp);
            setWidth("100%");
        }
        addStyleName("NoteWidget2");
        drawOldNotes();
    }

    private Boolean initVisibility() {
        if (pub.getValue()) {
            return Boolean.FALSE;
        }
        if (pri.getValue()) {
            return Boolean.TRUE;
        }
        return null;
    }

    public void drawOldNotes() {
        if (entityID != null && entityType != null) {
            allInOneService.getNotes(entityID, entityType, new AbstractAsyncCallback<ArrayList<HistoryListItem>>() {
                @Override
                public void failure(Throwable throwable) {

                }

                @Override
                public void success(ArrayList<HistoryListItem> result) {
                    if (result != null && result.size() > 0) {
                        for (HistoryListItem note : result) {
                            createNote(note, false);
                        }
                    }
                }
            });
        }
    }

    public void createNote(final HistoryListItem note, boolean saving) {
        Integer key = note.getObjectID();
        if (note.getObjectID() == null) {//this is new note...
            note.setObjectID(getNewKey());
            note.setEventDate(new Date());
            note.setEmployee(Utils.getUserFullName());
            key = note.getObjectID();
        }
        FlexTable table = null;
        if (editingKey != null) {
            table = (FlexTable) map.get(editingKey).getWidget();
            if (table != null) {
                table.setWidget(0, 0, drawContent(note));
                table.removeStyleName("noteTableHover");
            }
            editingKey = null;
        }
        if (table == null) {
            table = new FlexTable();
            table.getElement().addClassName("noteTable");
            table.setWidget(0, 0, drawContent(note));
            table.setWidget(1, 0, drawFooter(note));
            table.getCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LOCALE_END);
            panel.insert(table, 0);
        }
        map.put(key, new NoteEntry(note, table));
        textBox.setData("");
        if (saving && entityID != null && entityType != null) {
            note.setVisibility(initVisibility());
            if ((MEETING_MINUTES.equals(entityType) || VACANCY.equals(entityType) || PLACEMENT.equals(entityType) ||
                    PERSONAL_GOAL.equals(entityType) || DEPARTMENT_GOAL.equals(entityType) ||
                    PROJECT_GOAL.equals(entityType) || BUSINESS_GOAL.equals(entityType) ||
                    COMPANY_GOAL.equals(entityType) || PM_ISSUE.equals(entityType)) && entityID != null) {
                note.setObjectID(null);
                note.setRelatedId(entityID);
                if (PM_ISSUE.equals(entityType)) {
                    note.setRelatedToId(7);
                } else if (MEETING_MINUTES.equals(entityType)) {
                    note.setRelatedToId(8);
                } else if ((PERSONAL_GOAL.equals(entityType))) {
                    note.setRelatedToId(9);
                } else if ((DEPARTMENT_GOAL.equals(entityType))) {
                    note.setRelatedToId(10);
                } else if ((PROJECT_GOAL.equals(entityType))) {
                    note.setRelatedToId(11);
                } else if ((BUSINESS_GOAL.equals(entityType))) {
                    note.setRelatedToId(12);
                } else if ((COMPANY_GOAL.equals(entityType))) {
                    note.setRelatedToId(13);
                } else if ((VACANCY.equals(entityType))) {
                    note.setRelatedToId(14);
                } else if ((PLACEMENT.equals(entityType))) {
                    note.setRelatedToId(15);
                }

                note.setSubject(note.getComment());
                note.setEmployee(Utils.getUserFullName());
                final Integer finalKey = key;
                BugReportService.App.get().addNote(note, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void failure(Throwable throwable) {
                        removeNote(note);
                    }

                    @Override
                    public void success(Integer result) {
                        NoteEntry noteEntry = map.get(finalKey);
                        note.setObjectID(result);
                        noteEntry.setNote(note);
                        map.remove(finalKey);
                        map.put(result, noteEntry);
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTE_ADD, result, NoteWidget2.this);
                    }
                });

            } else {
                allInOneService.saveCrmNote(entityType, entityID, note, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        removeNote(note);
                    }

                    @Override
                    public void onSuccess(Integer result) {
                        NoteEntry entry = map.get(note.getObjectID());
                        note.setObjectID(result);
                        entry.setNote(note);
                        map.put(result, entry);
                        WfmUiEventsBus.fireWfmUiEvent(note.getObjectID() != null && !"".equals(note.getObjectID()) ? WfmUiEventType.ON_NOTE_EDIT : WfmUiEventType.ON_NOTE_ADD, result, NoteWidget2.this);
                    }
                });
            }
        }
    }

    protected Integer getNewKey() {
        return theLastNewID--;
    }

    protected FlowPanel drawFooter(final HistoryListItem note) {
        final FlowPanel f = new FlowPanel();
        f.addStyleName("notesFooter");
        final SimpleLink editLink = new SimpleLink(wfmStrings.edit(), null, "", "", "Note_edit_button");
        editLink.addClickHandler(event -> edit(note));
        editLink.addStyleName("noteRemove");
        final SimpleLink link = new SimpleLink(wfmStrings.delete(), null, "", "", "Note_delete_button");
        link.addClickHandler(event -> {
            LoadingPanel.loading(true);
            final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo,
                    wfmStrings.areYouSureYouWanttoDeleteThisNote(), new CloseHandler() {
                @Override
                public void onCancel() {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSubmit() {
                    LoadingPanel.loading(false);
                    removeNote(note);
                }
            });
            wfmMessageBox.setTitle(wfmStrings.confirmation());
            wfmMessageBox.center();
        });
        link.addStyleName("noteRemove");
        HTML employee = new HTML(note.getEmployee());
        employee.addStyleName("noteOwner");
        f.add(employee);
        HTML date = new HTML(DateUtils.formatInternal(note.getEventDate()));
        date.addStyleName("noteDate");
        f.add(date);
        if (Utils.isWebForm() || note.getEmployee().equals(Utils.getUserFullName())) { //userID
            Date toDay = new Date();
            if (note.getEventDate() != null && note.getEventDate().after(new Date(toDay.getYear(), toDay.getMonth(), toDay.getDate(), 0, 0, 0))) {
                f.add(editLink);
            }
            f.add(link);
        }
        return f;
    }

    protected Integer editingKey;

    private void edit(HistoryListItem note) {
        editingKey = note.getObjectID();
        textBox.setData(note.getComment());
//        textBox.getTextArea().setFocus(true);
        if (editingKey != null && map.containsKey(editingKey) && map.get(editingKey).getWidget() != null) {
            map.get(editingKey).getWidget().addStyleName("noteTableHover");
        }
    }

    protected void removeNote(HistoryListItem note) {
        if (note != null && map.get(note.getObjectID()) != null) {
            map.get(note.getObjectID()).remove();
            map.remove(note.getObjectID());
            if (!note.isNew()) {
                allInOneService.deleteNote(note.getObjectID(), entityType, new AbstractAsyncCallback<Void>() {
                    @Override
                    public void failure(Throwable throwable) {
                    }

                    @Override
                    public void success(Void result) {
//                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTE_DELETE, result, NoteWidget2.this);
                    }
                });
            }
        }
    }

    protected Widget drawContent(HistoryListItem note) {
        HTML html = new HTML(note.getComment(true));
//        html.setWidth("100%");
        html.getElement().addClassName("notesContent");
        return html;
    }

    public ArrayList<HistoryListItem> getNewNotesToSave() {
        if (textBox != null && textBox.getData() != null && !"".equals(textBox.getData().trim())) {
            createNote(new HistoryListItem(textBox.getData()), true);
        }
        ArrayList<HistoryListItem> newItems = new ArrayList<>();
        if (entityID == null) {
            if (map != null && map.size() > 0) {
                for (Map.Entry<Integer, NoteEntry> entry : map.entrySet()) {
                    if (entry.getKey() != null && entry.getKey() < 1) {
                        newItems.add(entry.getValue().getNote());
                    }
                }
            }
        }
        return newItems;
    }

    public void focus() {
//        textBox.getTextArea().setFocus(true);
    }

    protected class NoteEntry {
        private HistoryListItem note;
        private Widget widget;

        protected NoteEntry(HistoryListItem note, Widget widget) {
            this.note = note;
            this.widget = widget;
        }

        public HistoryListItem getNote() {
            return note;
        }

        public void setNote(HistoryListItem note) {
            this.note = note;
        }

        public Widget getWidget() {
            return widget;
        }

        public void setWidget(Widget widget) {
            this.widget = widget;
        }

        public void remove() {
            if (this.widget != null) {
                this.widget.removeFromParent();
            }
        }
    }

    public MaterialRichEditorWithSuggestionsPopup getTextBox() {
        return textBox;
    }

    private void setTooltipClass() {
        int frameWidth = JQuery.$(".frame__content__body.scroll-content").outerWidth();

        if (frameWidth < 960) {
            tooltipWrapper.setStyleName("dropdown-kit--arrow--right");
        } else {
            tooltipWrapper.setStyleName("dropdown-kit--arrow--left");
        }
    }
}