package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsRfqRfpNote;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.RfqRfpNoteManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shohruh on 03-Feb-16.
 */
@Repository("rfqRfpNoteManager")
public class RfqRfpNoteManagerImpl extends BaseManager<EdsRfqRfpNote> implements RfqRfpNoteManager {
    public RfqRfpNoteManagerImpl() {
        super(EdsRfqRfpNote.class);
    }

    @Override
    public List<EdsRfqRfpNote> getRfpNotes(Integer objectID) {
        return find("select ih from EdsRfqRfpNote ih where ih.rfp.objectID=? order by ih.date", objectID);
    }

    @Override
    public List<EdsRfqRfpNote> getRfqNotes(Integer objectID) {
        return find("select ih from EdsRfqRfpNote ih where ih.rfq.objectID=? order by ih.date", objectID);
    }

    @Override
    public List<HistoryListItem> getRfpNotesAsHistoryListItem(Integer rfpId) {
        return mapToHistoryListItem(getRfpNotes(rfpId));
    }

    @Override
    public List<HistoryListItem> getRfqNotesAsHistoryListItem(Integer rfqId) {
        return mapToHistoryListItem(getRfqNotes(rfqId));
    }

    private List<HistoryListItem> mapToHistoryListItem(List<EdsRfqRfpNote> records) {
        List<HistoryListItem> recordItems = new ArrayList<>();
        for (EdsRfqRfpNote r : records) {
            HistoryListItem recordItem = new HistoryListItem();
            recordItem.setObjectID(r.getObjectID());
            if (r.isSuperUser()) {
                recordItem.setEmployee(Constants.defaultSupportName);
            } else {
                recordItem.setEmployee(r.getCommentator().getFullName());
            }
            recordItem.setComment(r.getComment());
            recordItem.setEventDate(r.getDate());
            recordItems.add(recordItem);
        }
        return recordItems;
    }

    @Override
    public void deleteRfqRfpNotes(Integer rfqRfpId, boolean isRfq) {
        if (isRfq) {
            update("delete from EdsRfqRfpNote where rfq.objectID = ?", rfqRfpId);
        } else {
            update("delete from EdsRfqRfpNote where rfp.objectID = ?", rfqRfpId);
        }
    }
}
