package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.note.NoteDetailsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskDetailInfoTO;

import java.util.ArrayList;

public class DetailInfoTO extends ResponseData {
    private ItemDetailTO item;
    private TaskDetailInfoTO task;
    private ArrayList<NoteDetailsTO> note;

    public ItemDetailTO getItem() {
        return item;
    }

    public void setItem(ItemDetailTO item) {
        this.item = item;
    }

    public TaskDetailInfoTO getTask() {
        return task;
    }

    public void setTask(TaskDetailInfoTO task) {
        this.task = task;
    }

    public ArrayList<NoteDetailsTO> getNote() {
        return note;
    }

    public void setNote(ArrayList<NoteDetailsTO> note) {
        this.note = note;
    }
}
