package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBackupsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BackupsEmployeeManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.BACKUP_EMPLOYEE_SEE_ALL;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.BACKUP_EMPLOYEE_SEE_BY_TYPE;

@Repository("backupsEmployeeManager")
public class BackupsEmployeeManagerImpl extends BaseManager<EdsBackupsEmployee> implements BackupsEmployeeManager {

    public BackupsEmployeeManagerImpl() {
        super(EdsBackupsEmployee.class);
    }

    @Override
    public List<EdsBackupsEmployee> getAllItems(ListingFilterParameter fp, List<CompanyCustomFieldItem> customFieldItems) {
        if (fp == null) fp = new ListingFilterParameter();
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append("select be.* from ").append(getCompanyId()).append(".backups_employee be left join ")
                .append(getCompanyId()).append(".backups_employee_customfields cf on be.customfieldsid = cf.id left join")
                .append(getCompanyId()).append(".myuser us on be.employee_id = us.id where ").append(ServerUtils.checkForDeleted("be.deleted"));
        if (!ServerUtils.hasPermission(BACKUP_EMPLOYEE_SEE_ALL)) {
            EdsUser edsUser = getUser();
            solrQuery.append(" and (be.creatorId =").append(edsUser.getObjectID()).append(" or be.employee_id =").append(edsUser.getObjectID());
            if (ServerUtils.hasPermission(BACKUP_EMPLOYEE_SEE_BY_TYPE) && customFieldItems != null && !customFieldItems.isEmpty()) {
                Map<CustomFieldLookUpTypeEnum, List<String>> cfMap = customFieldItems.stream().filter(cf -> cf.isUseInPermission() != null && cf.isUseInPermission() &&
                                cf.getLookUpTypeEnum() != null && (cf.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.DEPARTMENT) ||
                                cf.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.LOCATION) || cf.getLookUpTypeEnum().equals(CustomFieldLookUpTypeEnum.POSITION)))
                        .collect(Collectors.groupingBy(CompanyCustomFieldItem::getLookUpTypeEnum, Collectors.mapping(CompanyCustomFieldItem::getColumnCode, Collectors.toList())));
                Integer locationId = edsUser.getLocation() != null ? edsUser.getLocation().getObjectID() : null;
                Integer positionId = edsUser.getEmployee().getPosition() != null ? edsUser.getEmployee().getPosition().getObjectID() : null;
                Integer departmentId = edsUser.getEmployee().getEmployeeDepartment() != null && edsUser.getEmployee().getEmployeeDepartment().getTeam() != null ? edsUser.getEmployee().getEmployeeDepartment().getTeam().getObjectID() : null;
                if (cfMap != null && !cfMap.isEmpty()) {
                    for (CustomFieldLookUpTypeEnum type : cfMap.keySet()) {
                        if ((type.equals(CustomFieldLookUpTypeEnum.POSITION) && positionId != null) || (type.equals(CustomFieldLookUpTypeEnum.DEPARTMENT) && departmentId != null) ||
                                (type.equals(CustomFieldLookUpTypeEnum.LOCATION) && locationId != null)) {
                            List<String> columnCodes = cfMap.get(type);
                            if (columnCodes != null && !columnCodes.isEmpty()) {
                                for (String column : columnCodes) {
                                    solrQuery.append(" OR cast(cast(cf.jsonentities as json) ->> '").append(column.toLowerCase()).append("' as numeric)=").append(type.equals(CustomFieldLookUpTypeEnum.POSITION) ? positionId :
                                            type.equals(CustomFieldLookUpTypeEnum.DEPARTMENT) ? departmentId : locationId);
                                }
                            }
                        }
                    }
                }
            }
            solrQuery.append(")");
        }
        if (fp.getSqlSearchKey() != null && !"".equals(fp.getSqlSearchKey()) && !" ".equals(fp.getSqlSearchKey())) {
            solrQuery.append(" and (lower(us.firstname) like '").append(fp.getSqlSearchKey()).append("' ")
                    .append(" or  lower(us.lastName) like '").append(fp.getSqlSearchKey()).append("'")
                    .append(" or  lower(be.backup_employee_code) like '").append(fp.getSqlSearchKey()).append("'")
                    .append(" or  (lower(us.firstname) || '' ||  lower(us.lastName)) like '").append(fp.getSqlSearchKey()).append("')");
        }
        solrQuery.append(" order by be.id desc");
        return findNative(solrQuery.toString(), EdsBackupsEmployee.class);
    }

    @Override
    public Integer countBackupEmployee() {
        Long totalItems = (Long) findSingle("select count(*) from EdsBackupsEmployee be  where (be.deleted is null OR be.deleted <> true)");
        return totalItems.intValue();
    }

    @Override
    public Integer getBackupsEmployeeLastIntNumber() {
        return (Integer) findSingle("select bce.intNumber from EdsBackupsEmployee bce where (bce.deleted = false or bce.deleted is null) and bce.intNumber is not null order by bce.intNumber desc");
    }
}
