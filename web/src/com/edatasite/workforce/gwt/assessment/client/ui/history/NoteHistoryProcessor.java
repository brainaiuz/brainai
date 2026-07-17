package com.edatasite.workforce.gwt.assessment.client.ui.history;

import com.edatasite.workforce.gwt.assessment.client.NoteAddSinksContainer;
import com.edatasite.workforce.gwt.assessment.client.NoteSinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

/**
 * User: Sherzod
 * Date: May 14, 2009
 * Time: 5:24:14 PM
 */
public class NoteHistoryProcessor implements HistoryProcessor {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    //must be ---> strings.length<=3
    public SinksContainer process(String containerName, String[] strings) {
        return new NoteSinksContainer(containerName + strings[0], hrmsStrings.performanceNoteView(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new NoteAddSinksContainer("performancenoteadd", hrmsStrings.addNewNote());
    }
}
