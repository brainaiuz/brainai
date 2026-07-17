package com.edatasite.workforce.gwt.core.server.db.impl.talentprofile;

import com.edatasite.workforce.core.domain.EdsAward;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeSkills;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsTalentProfileCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.TalentProfileCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.talentprofile.AwardManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileEnum;
import com.edatasite.workforce.gwt.hrms.client.rpc.TalentProfileListItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Dec 2, 2009
 * Time: 11:59:00 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("awardManager")
public class AwardManagerImpl extends BaseManager<EdsAward> implements AwardManager {

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private TalentProfileCFManager talentProfileCFManager;
    @Autowired
    private CrmContactManager crmContactManager;

    public AwardManagerImpl() {
        super(EdsAward.class);
    }


    @Override
    public ListResult<TalentProfileListItem> getTalentProfileList(ListingFilterParameter fp) {

        EdsEmployee employee = null;
        EdsCrmContact edsCrmContact = null;
        if (fp.getContactID() != null) {
            edsCrmContact = crmContactManager.get(fp.getContactID());
        } else {
            if (fp.getEmployeeId() != null) {
                employee = employeeManager.get(fp.getEmployeeId());
            }
            if (employee == null) {
                employee = employeeManager.getUser().getEmployee();
            }
            fp.setEmployeeId(employee.getObjectID());
        }


        //Execute total count
        StringBuilder sql = new StringBuilder();
        sql.append("select count(t.id) from (");
        sql.append(getBaseSql(fp));
        sql.append(") t");
        BigInteger totalCount = (BigInteger) findNativeSingle(sql.toString());

        //Execute list
        sql = new StringBuilder();
        sql.append("select t.* from (");
        sql.append(getBaseSql(fp));
        sql.append(") t ");
        sql.append(" order by ");

        String sortOrder = fp.isAscending() ? "" : " desc ";
        if (StringUtils.isNotBlank(fp.getSortField())) {
            if (TalentProfileListItem.NAME.equals(fp.getSortField())) {
                sql.append("t.name");
            } else if (TalentProfileListItem.TYPE.equals(fp.getSortField())) {
                sql.append("t.country");
            } else if (TalentProfileListItem.START_DATE.equals(fp.getSortField())) {
                sql.append("t.startDate");
            } else if (TalentProfileListItem.END_DATE.equals(fp.getSortField())) {
                sql.append("t.endDate");
            } else if (TalentProfileListItem.COUNTRY.equals(fp.getSortField())) {
                sql.append("t.country");
            } else {
                sql.append(" t.lastUpdateDate");
            }
            sql.append(sortOrder);
        } else {
            sql.append(" t.lastUpdateDate desc");
        }

        sql.append(" limit ").append(fp.getLimit());
        sql.append(" offset ").append(fp.getStart());

        ListPanelToolRpc panelSettings = fp.getListPanelTool();
        List<Object[]> objects = (List<Object[]>) findNative(sql.toString());
        ArrayList<TalentProfileListItem> result = new ArrayList<>();

        for (Object[] obj : objects) {
            TalentProfileListItem item = new TalentProfileListItem();
            item.setObjectID((Integer) obj[0]);
            item.setName((String) obj[1]);
            if (obj[2] != null) {
                item.setStartDate(new DateNonConvertable((Date) obj[2]));
            }
            if (obj[3] != null) {
                item.setEndDate(new DateNonConvertable((Date) obj[3]));
            }
            item.setDegree(new ReferenceItem(null, (String) obj[4]));
            item.setType(TalentProfileEnum.valueOf((String) obj[5]));
            item.setCountry((String) obj[6]);
            if (panelSettings != null && obj[8] != null) {
                EdsTalentProfileCustomFields cf = talentProfileCFManager.get((Integer) obj[8]);
                if (cf != null) {
                    HashMap<String, Object> map = CustomFieldsUtils.getRPCCustomFields(cf, panelSettings.getColumnCodeName());
                    item.setCustomFieldItems(commonServiceLocal.getLocaledCustomFiledMap(map, panelSettings.getListViewCustomFields()));
                }
            }

            result.add(item);
        }

        return new ListResult<>(result, totalCount.intValue());
    }

    private StringBuilder getBaseSql(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();

        //AWARDS
        sql.append("select a.id, a.name, a.issueDate startDate, a.expireDate endDate, null as degree,  'AWARD' as type, c.name as country,a.lastUpdateDate, null \n");
        sql.append(" from ").append(getCompanyId()).append(".award a \n");
        sql.append(" left join ").append(getPublic()).append(".country c on a.countryid = c.id \n");
        sql.append(" where (a.deleted is null or a.deleted is not true) \n");
        sql.append(" and a.employeeId=").append(fp.getEmployeeId());

        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            sql.append(" and (LOWER(a.name) LIKE '%").append(fp.getSearchKey().toLowerCase()).append("%' ");
            sql.append(" or LOWER(c.name) LIKE '%").append(fp.getSearchKey().toLowerCase()).append("%') ");
        }
        sql.append(" \n UNION ALL \n");

        //EDUCATION
        sql.append("select e.id, e.school as name, e.startDate startDate, e.endDate endDate, r.name as degree, 'EDUCATION' as type, c.name as country,e.lastUpdateDate, cf.id  \n");
        sql.append(" from ").append(getCompanyId()).append(".education e \n");
        sql.append(" left join ").append(getPublic()).append(".country c on e.countryid = c.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".reference r on r.id = e.degreeid \n");
        sql.append(" left join ").append(getCompanyId()).append(".talentprofilecustomfields cf on e.customfieldsid = cf.id \n");
        sql.append(" where (e.deleted is null or e.deleted is not true) \n");
        sql.append(" and (e.employeeId=").append(fp.getEmployeeId());
        sql.append(" or e.candidateid=").append(fp.getContactID()).append(" )");

        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            sql.append(" and (LOWER(e.school) LIKE '%").append(fp.getSearchKey().toLowerCase()).append("%' ");
            sql.append(" or LOWER(c.name) LIKE '%").append(fp.getSearchKey().toLowerCase()).append("%') ");
        }

        sql.append("\n UNION ALL \n");

        //COMPETENCY
        sql.append("select s.id, s.name, null startDate, null endDate, null as degree, 'COMPETENCY' as type, '' as country,s.lastUpdateDate, null \n");
        sql.append(" from ").append(getCompanyId()).append(".skill s \n");
        sql.append(" left join ").append(getCompanyId()).append(".employeeSkills es on s.id = es.skillId \n");
        sql.append(" left join ").append(getCompanyId()).append(".employee emp on emp.id = es.employeeid \n");
        sql.append(" where (s.deleted is null or s.deleted is not true) \n");
        sql.append(" and (es.deleted is null or es.deleted is not true) \n");
        sql.append(" and es.type = ").append(EdsEmployeeSkills.Typer.SIMPLE.ordinal());
        sql.append(" and es.employeeId=").append(fp.getEmployeeId());

        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            sql.append(" and LOWER(s.name) LIKE '%").append(fp.getSearchKey().toLowerCase()).append("%' ");
        }
        return sql;
    }

    @Override
    public List<Object[]> getTalentProfileData(Integer employeeId) {
        if (employeeId == null) {
            return null;
        }

        //Execute list
        String sql = "select t.* from (" +
                getUnionData(employeeId) +
                ") t " +
                " order by t.lastUpdateDate desc";

        List<Object[]> objects = (List<Object[]>) findNative(sql);
        return objects;
    }

    private StringBuilder getUnionData(Integer employeeId) {
        StringBuilder sql = new StringBuilder();

        //AWARDS
        sql.append("select a.id, a.name, a.issueDate startDate, a.expireDate endDate, 'AWARD' as type, c.name as country,a.lastUpdateDate,a.description as description, '' as degree, '' as study \n");
        sql.append(" from ").append(getCompanyId()).append(".award a \n");
        sql.append(" left join ").append(getPublic()).append(".country c on a.countryid = c.id \n");
        sql.append(" where (a.deleted is null or a.deleted is not true) \n");
        sql.append(" and a.employeeId=").append(employeeId);

        sql.append(" \n UNION ALL \n");

        //EDUCATION
        sql.append("select e.id, e.school as name, e.startDate startDate, e.endDate endDate, 'EDUCATION' as type, c.name as country,e.lastUpdateDate,e.comment as description, r.name as degree, e.fieldofstudy as study \n");
        sql.append(" from ").append(getCompanyId()).append(".education e \n");
        sql.append(" left join ").append(getPublic()).append(".country c on e.countryid = c.id \n");
        sql.append(" left join ").append(getCompanyId()).append(".Reference r on r.id = e.degreeid\n");
        sql.append(" where (e.deleted is null or e.deleted is not true) \n");
        sql.append(" and e.employeeId=").append(employeeId);

        sql.append("\n UNION ALL \n");

        //COMPETENCY
        sql.append("select s.id, s.name, null startDate, null endDate, 'COMPETENCY' as type, '' as country,s.lastUpdateDate,s.description as description, '' as degree, '' as study \n");
        sql.append(" from ").append(getCompanyId()).append(".skill s \n");
        sql.append(" left join ").append(getCompanyId()).append(".employeeSkills es on s.id = es.skillId \n");
        sql.append(" left join ").append(getCompanyId()).append(".employee emp on emp.id = es.employeeid \n");
        sql.append(" where (s.deleted is null or s.deleted is not true) \n");
        sql.append(" and (es.deleted is null or es.deleted is not true) \n");
        sql.append(" and es.type = ").append(EdsEmployeeSkills.Typer.SIMPLE.ordinal());
        sql.append(" and es.employeeId=").append(employeeId);

        return sql;
    }

}
