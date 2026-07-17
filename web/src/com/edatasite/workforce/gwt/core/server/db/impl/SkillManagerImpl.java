package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployeeSkills;
import com.edatasite.workforce.core.domain.EdsProfileSkill;
import com.edatasite.workforce.core.domain.EdsSkill;
import com.edatasite.workforce.core.domain.EdsSkillGroup;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.SkillGroupManager;
import com.edatasite.workforce.gwt.core.server.db.SkillManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("unchecked")
@Repository("skillManager")
public class SkillManagerImpl extends BaseManager<EdsSkill> implements SkillManager {

    public SkillManagerImpl() {
        super(EdsSkill.class);
    }

    private SkillGroupManager skillGroupManager;

    @Autowired
    public void setSkillGroupManager(SkillGroupManager skillGroupManager) {
        this.skillGroupManager = skillGroupManager;
    }

    public List<EdsProfileSkill> getSkillListByUser(Integer profileID) {
        return find("SELECT ps FROM EdsProfileSkill ps where ps.profile=?", getUser().getEmployee().getProfile());
    }

    public List<EdsSkill> getSkillList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder();
        String order = " ct.name";

        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (SkillItem.COMPETENCY_NAME.equals(fp.getSortField())) {
                order = " ct.name";
            } else if (SkillItem.COMPETENCY_DESCRIPTION.equals(fp.getSortField())) {
                order = " ct.description";
            } else if (SkillItem.COMPETENCY_GROUP_NAME.equals(fp.getSortField())) {
                order = " ct.group.name";
            } else {
                order = " ct.name";
            }
        }
        sql.append("select ct ");
        sql.append(" from EdsSkill as ct ");
        sql.append(" where 1=1 ");
        sql.append(" and (ct.deleted is null or ct.deleted <> true) ");
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (");
            sql.append(" lower(ct.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(ct.description) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(ct.group.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") ");
        }
        sql.append(" order by ");
        sql.append(order);
        if (fp.isAscending()) {
            sql.append(" ASC ");
        } else {
            sql.append(" DESC ");
        }
        return (List<EdsSkill>) findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Transactional
    public List<EdsSkillGroup> skillGroupList() {
        List<EdsSkillGroup> skillGroups = find("FROM EdsSkillGroup sg where (sg.deleted is null or sg.deleted <> true) order by name");

        /**
         * If the first signed up company does not possess any skills, then we
         * will load all default  skills  from zero company and copy all skill
         * values and paste into newly created company's tables.This way helps
         * to avoid from ID conflicts between two schemas.
         */
        if (skillGroups.size() == 0) {
            skillGroups = findNative("SELECT sk.* FROM \"" + 0 + "\".skillgroup sk", EdsSkillGroup.class);

            for (EdsSkillGroup skillGroup : skillGroups) {
                EdsSkillGroup group = new EdsSkillGroup();
                group.setName(skillGroup.getName());
                group.setCode(skillGroup.getCode());
                skillGroupManager.create(group);

                List<EdsSkill> skills = findNative("SELECT s.* FROM \"" + 0 + "\".skill s WHERE s.groupid="
                        + skillGroup.getObjectID(), EdsSkill.class);
                for (EdsSkill skill : skills) {
                    EdsSkill newSkill = new EdsSkill();
                    newSkill.setName(skill.getName());
                    newSkill.setCode(skill.getCode());
                    newSkill.setDescription(skill.getDescription());
                    newSkill.setGroup(group);
                    create(newSkill);
                }
            }

            skillGroups = find("FROM EdsSkillGroup order by name ");
        }

        return skillGroups;
    }

    public List<EdsSkill> skillListBySkillGroupId(Integer skillGroupId) {
        return find("SELECT DISTINCT s FROM EdsSkill s WHERE s.group.objectID=? and (s.deleted is null or s.deleted <> true) order by s.name ", skillGroupId);
    }

    public EdsSkillGroup getSkillGroup(Integer skillGroupId) {
        return (EdsSkillGroup) findSingle("from EdsSkillGroup sg where sg.id=?", skillGroupId);
    }

    public List<EdsSkill> getSkillListByEmployeeByType(Integer employeeID, Integer type) {
        if (type != null) {
            return type == Constants.ASSESSMENT_SKILLS_SIMPLE ? getSkillListByEmployeeForSimple(employeeID) : getSkillListByEmployeeFor360(employeeID);
        } else {
            return null;
        }
    }

    public List<EdsSkill> getSkillListByEmployeeForSimple(Integer employeeID) {
        return (List<EdsSkill>) find("select employeeSkills.skill from EdsEmployeeSkills employeeSkills where employeeSkills.employee.objectID = ? and " +
                "(employeeSkills.skill.deleted is null or employeeSkills.skill.deleted <> true) and employeeSkills.deleted <> true and employeeSkills.type = ?", employeeID, EdsEmployeeSkills.Typer.SIMPLE);
    }

    public List<EdsSkill> getSkillListByEmployeeFor360(Integer employeeID) {
        return (List<EdsSkill>) find("select employeeSkills.skill from EdsEmployeeSkills employeeSkills where employeeSkills.employee.objectID = ? and " +
                "(employeeSkills.skill.deleted is null or employeeSkills.skill.deleted <> true) and employeeSkills.deleted <> true and employeeSkills.type = ?", employeeID, EdsEmployeeSkills.Typer.THT);
    }

    @Override
    public List<EdsSkill> getSkillListByGroupCodes(List<String> groupCodes) {
        return (List<EdsSkill>) find("select s from EdsSkill s join s.group g where (s.deleted is null or s.deleted is false) and g.code in ('" + ServerUtils.getAsCommoDelimited(groupCodes, "0", "','")+ "')");
    }

    @Override
    public EdsSkill getSkillByName(String name) {
        return (EdsSkill) findSingle("select s from EdsSkill s where (s.deleted is null OR s.deleted is false) and s.name = ?", name);
    }

    public Long getSkillsTotal(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT COUNT(ct.objectID) ");
        sql.append(" FROM EdsSkill as ct ");
        sql.append(" WHERE 1=1 ");
        sql.append(" AND (ct.deleted <> true OR ct.deleted is null) ");
        if (fp.getSqlSearchKey() != null) {
            sql.append(" AND (");
            sql.append(" lower(ct.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(ct.description) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(ct.group.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") ");
        }
        return (Long) findSingle(sql.toString());
    }

}
