package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("PropertyManager")
public class PropertyManagerImpl extends BaseManager<EdsProperty> implements PropertManager {

    public PropertyManagerImpl() {
        super(EdsProperty.class);
    }

    @Override
    public List<EdsProperty> findByModuleCode(String moduleName) {
        return slaveEntityManager.createQuery("select p from EdsProperty p where p.active<>false " +
                "and (p.isCustom is false or (p.isCustom is true and p.moduleCode='" + moduleName + "'))", EdsProperty.class).getResultList();
    }

    @Override
    public List<EdsProperty> findByModuleCodeFromBackend(String moduleName) {
        return slaveEntityManager.createQuery("select p from EdsProperty p where p.active<>false " +
                "and p.formID is not null and p.moduleCode='" + moduleName + "'", EdsProperty.class).getResultList();
    }

    @Override
    public EdsProperty zeroSchemaProperty(String objectName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append("\"0\"").append(".property ");
        sql.append(" where  objectName='").append(objectName).append("'");
        return (EdsProperty) findNativeSingle(sql.toString(), EdsProperty.class);
    }

    @Override
    public String findByPlural(String pluralName, Integer companyId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select p.objectName from ").append("\"").append(companyId).append("\".").append("property p ");
        sql.append(" where p.plural ='").append(pluralName).append("'");
//        return (EdsProperty) findNativeSingle("select p from EdsProperty p where p.plural =:plural", preparing(new Entry("plural", pluralName)));
        return (String) findNativeSingle(sql.toString());
    }

    @Override
    public EdsProperty findByCode(String instanceName) {
        return (EdsProperty) findSingleByNamedParams("select p from EdsProperty p where p.objectName =:objectName and p.active is true",
                preparing(new Entry("objectName", instanceName)));
    }

    @Override
    public Integer parentCountByModuleCode(String moduleName) {
        StringBuilder sql = new StringBuilder();
        sql.append("select max(parent) from ").append(getCompanyId()).append(".property ");
        sql.append(" where moduleCode ='").append(moduleName).append("'");

        return (Integer) findNativeSingle(sql.toString());
    }


    @Override
    public List<EdsProperty> getListingByParent(Integer parent, String moduleName) {
        return slaveEntityManager.createQuery("select p from EdsProperty p " +
                " where p.parent =" + parent + " and p.moduleCode='" + moduleName + "' and p.listing is true" +
                " and ( p.child is null and (p.objectName is not null or p.formID is not null) or p.child is not null) " +
                " order by sorder", EdsProperty.class).getResultList();
    }


    @Override
    public List<EdsProperty> list(ListingFilterParameter fp) {
        return findInterval(getSqlForListing(fp, false), fp.getStart(), fp.getLimit());
    }

    @Override
    public int count(ListingFilterParameter filterParameter) {
        Long count = (Long) findSingle(getSqlForListing(filterParameter, true));
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

    private String getSqlForListing(ListingFilterParameter filterParameter, boolean forCount) {
        StringBuilder sql = new StringBuilder("select ").append(forCount ? "count(p.objectID)" : "p").append(" from EdsProperty p left join p.user u where 1=1 ");

        if (StringUtils.isNotBlank(filterParameter.getModule())) {
            sql.append(" and lower(p.moduleCode) LIKE lower('%" + filterParameter.getModule() + "%') ");
        }
        if (!StringUtils.isEmpty(filterParameter.getSqlSearchKey())) {
            sql.append(" and (");
            sql.append(" lower(p.defaultName) like '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(" or lower(p.singular) like '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(" or lower(p.plural) like '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(")");
        }
        if (!forCount) {
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                if (PropertyItem.DEFAULT_NAME.equals(filterParameter.getSortField())) {
                    sql.append("ORDER BY p.defaultName ");
                } else if (PropertyItem.CUSTOM_NAME.equals(filterParameter.getSortField())) {
                    sql.append("ORDER BY p.singular ");
                } else if (PropertyItem.LAST_MODIFIED.equals(filterParameter.getSortField())) {
                    sql.append("ORDER BY p.lastModifiedDate ");
                } else if (PropertyItem.MODIFIER.equals(filterParameter.getSortField())) {
                    sql.append("ORDER BY u.firstName, u.lastName ");
                } else if (PropertyItem.SINGULAR.equals(filterParameter.getSortField())) {
                    sql.append("ORDER BY p.singular ");
                } else if (PropertyItem.PLURAL.equals(filterParameter.getSortField())) {
                    sql.append("ORDER BY p.plural ");
                } else if (PropertyItem.SHORT_NAME.equals(filterParameter.getSortField())) {
                    sql.append("ORDER BY p.shortcut ");
                }
                if (!filterParameter.isAscending()) {
                    sql.append(" DESC ");
                }
            } else {
                sql.append("ORDER BY p.defaultName DESC ");
            }
        }
        return sql.toString();
    }
}
