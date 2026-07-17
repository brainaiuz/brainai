package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsProfileSkill;
import com.edatasite.workforce.core.domain.EdsSkill;
import com.edatasite.workforce.core.domain.EdsSkillGroup;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

public interface SkillManager extends Manager<EdsSkill> {

    List<EdsProfileSkill> getSkillListByUser(Integer profileID);

    List<EdsSkill> getSkillList(ListingFilterParameter fp);

    Long getSkillsTotal (ListingFilterParameter fp);

    List<EdsSkillGroup> skillGroupList();

    List<EdsSkill> skillListBySkillGroupId(Integer skillGroupId);

    EdsSkillGroup getSkillGroup(Integer skillGroupId);

    List<EdsSkill> getSkillListByEmployeeByType(Integer employeeID, Integer type);

    List<EdsSkill> getSkillListByEmployeeForSimple(Integer employeeID);

    List<EdsSkill> getSkillListByEmployeeFor360(Integer employeeID);

    List<EdsSkill> getSkillListByGroupCodes(List<String> groupCodes);

    EdsSkill getSkillByName(String name);
}
