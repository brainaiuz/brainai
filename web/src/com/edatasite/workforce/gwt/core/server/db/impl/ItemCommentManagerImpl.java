package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsItemComment;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.ItemCommentManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("itemCommentManager")
public class ItemCommentManagerImpl extends BaseManager<EdsItemComment> implements ItemCommentManager, Constants, AccountingConstants {

    public ItemCommentManagerImpl() {
        super(EdsItemComment.class);
    }

    public List<EdsItemComment> getComments(Integer productId) {
        return find("from EdsItemComment where item.objectID=? order by date", productId);
    }

}