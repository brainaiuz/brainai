package com.edatasite.workforce.gwt.note.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 31-Aug-2009
 * Time: 14:32:06
 * To change this template use File | Settings | File Templates.
 */
public interface NoteService extends RemoteService {

    ListResult<HistoryListItem> noteList(ListingFilterParameter filterParametrs);

    void deleteNote(Integer id);

    SelectItem[] getNoteDates(ListingFilterParameter fp);

    SelectItem[] getNoteUsers(ListingFilterParameter fp);

    SelectItem[] getNoteRelation(ListingFilterParameter fp);

    HistoryListItem getNote(Integer objectId);

    HashMap<WfmTreeItem, LinkedList<WfmTreeItem>> getEmployees();

    class App {
        public static NoteServiceAsync get() {
            ServiceDefTarget target = GWT.create(NoteService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/note");
            return (NoteServiceAsync) target;
        }
    }
}