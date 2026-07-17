package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWebHookResponse;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.WebHookResponseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User : Akhror
 * Date : 17.01.2022
 */
@Repository("webHookResponseManager")
public class WebHookResponseManagerImpl extends BaseManager<EdsWebHookResponse> implements WebHookResponseManager {
    public WebHookResponseManagerImpl() {
        super(EdsWebHookResponse.class);
    }

    @Override
    public List<EdsWebHookResponse> list(ListingFilterParameter fp) {
        if (fp.getSearchKey() == null) {
            return findInterval("from EdsWebHookResponse w where w.webHook.objectID = ? order by w.createdDate desc", fp.getStart(), fp.getLimit(), fp.getRelationID());
        }
        String searchQuery = """
                from EdsWebHookResponse w
                where w.webHook.objectID = ?
                    and (w.body like '%""" + fp.getSearchKey() + "%' or w.response like '%" + fp.getSearchKey() + """
                %')
                order by w.createdDate desc""";
        return findInterval(searchQuery, fp.getStart(), fp.getLimit(), fp.getRelationID());
    }

    @Override
    public List<EdsWebHookResponse> listByType(Integer typeId, String type) {
        return find("from EdsWebHookResponse w where w.typeId = ? and w.type= ? order by w.createdDate desc", typeId, type);
    }

    @Override
    public Integer getUnDeletedItemCount(Integer id) {
        Long total = (Long) findSingle("select count(*) from EdsWebHookResponse w where w.webHook.objectID = ? ",id);
        return total.intValue();
    }
}
