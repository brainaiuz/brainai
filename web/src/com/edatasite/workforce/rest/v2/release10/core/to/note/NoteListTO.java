package com.edatasite.workforce.rest.v2.release10.core.to.note;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 12/27/2017.
 */
public class NoteListTO extends ResponseData {

    private ArrayList<NoteTO> notes;


    public NoteListTO() {
    }

    public NoteListTO(ArrayList<NoteTO> notes) {
        this.notes = notes;
    }

    public ArrayList<NoteTO> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<NoteTO> notes) {
        this.notes = notes;
    }
}
