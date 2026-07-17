package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSkillGroup;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.SkillGroupManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("skillGroupManager")
public class SkillGroupManagerImpl extends BaseManager<EdsSkillGroup> implements SkillGroupManager {

    public SkillGroupManagerImpl() {
        super(EdsSkillGroup.class);
    }

    @Override
    public List<EdsSkillGroup> skillGroupList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        sql.append("select sg from EdsSkillGroup sg where ");
        if (fp.getSearchKey() != null && !fp.getSearchKey().trim().isEmpty()) {
            sql.append(" lower(sg.name) like lower('%")
                    .append(fp.getSearchKey())
                    .append("%') and ");
        }
        sql.append("( sg.deleted <> true or sg.deleted is null ) order by sg.id desc");
        return (List<EdsSkillGroup>) findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public EdsSkillGroup getByCode(String code) {
        return (EdsSkillGroup) findSingle(
                "select sg from EdsSkillGroup sg where sg.code = ? and ( sg.deleted <> true or sg.deleted is null )",
                code
        );
    }

    @Override
    public Long getCount() {
        return (Long) findSingle(
                "select count(sg.id) from EdsSkillGroup sg " +
                        "where ( sg.deleted <> true or sg.deleted is null )"
        );
    }

    @Override
    public EdsSkillGroup findByName(String name) {
        return (EdsSkillGroup) findSingle(
                "select sg from EdsSkillGroup sg " +
                        "where lower(sg.name) = lower(?) and ( sg.deleted <> true or sg.deleted is null )",
                name
        );
    }


    public EdsSkillGroup get(Integer id) {
        return super.get(id);
    }

}
