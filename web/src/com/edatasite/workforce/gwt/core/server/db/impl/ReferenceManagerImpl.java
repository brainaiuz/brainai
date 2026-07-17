package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Jan 14, 2008 Time: 3:59:21 PM To
 * change this template use File | Settings | File Templates.
 */

@Repository("referenceManager")
public class ReferenceManagerImpl extends BaseManager<EdsReference> implements ReferenceManager, Constants {

    public ReferenceManagerImpl() {
        super(EdsReference.class);
    }

    @Override
    public void deleteChildren(EdsReference reference) {
        if (reference != null && reference.getParent() == null) {
            updateNative("update " + getCompanyId() + ".reference set deleted  = true where parentid = " + reference.getObjectID());
        }
    }

    @Override
    public SelectItem[] getAsSelectItems(String parentCode) {
        List<EdsReference> references = parentCode != null && !"".equals(parentCode) ? find("select r from EdsReference r where r.deleted<>true and r.parent.code=?", parentCode) : null;
        ArrayList<SelectItem> result = new ArrayList<>();
        if (references != null && references.size() > 0) {
            for (EdsReference reference : references) {
                result.add(reference.getRPC());
            }
        }
        return result.toArray(new SelectItem[]{});
    }

    @Override
    public Integer getLastSorder(String parentCode) {
        return (Integer) findSingle("select max(sorder) from EdsReference where deleted is not true and parent.code=?", parentCode);
    }

    @Override
    public EdsReference getReferenceByParentCode(String parentCode) {
        return (EdsReference) findSingle("select r from EdsReference r where r.deleted is not true and r.parent.code=? order by r.sorder", parentCode);
    }

    @Override
    public void copyStepStatuses(Integer oldParentID, Integer newParentID, Integer fromCompanyID, Integer toCompanyID) {
        if (fromCompanyID != null && toCompanyID != null && oldParentID != null) {
            List<EdsReference> statuses = (List<EdsReference>) findNative("select * from \"" + fromCompanyID + "\".reference where (deleted is null or deleted is false) " +
                    "and parentid = " + oldParentID, EdsReference.class);
            if (statuses != null && statuses.size() > 0) {
                int i = 0;
                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO \"").append(toCompanyID).append("\".reference (code, name, description, sorder, parentid, isSystemReference, isRemovable) VALUES ");
                for (EdsReference ref : statuses) {
                    sql.append("(").append(ref.getCode() != null ? "'" + ref.getCode() + "'" : null).append(",");
                    sql.append(ref.getName() != null ? "'" + ref.getName() + "'" : null).append(",");
                    sql.append(ref.getDescription() != null ? "'" + ref.getDescription() + "'" : null).append(",").append(ref.getSorder()).append(",").append(newParentID).append(",true,false)");
                    i++;
                    if (i < statuses.size()) {
                        sql.append(", ");
                    }
                }
                updateNative(sql.toString());
            }
        }
    }

    @Override
    public EdsReference getByCode(String code) {
        return (EdsReference) findSingle("select r from EdsReference r where r.deleted<>true and r.code=?", code);
    }

    @Override
    public List<EdsReference> listReferences(ListingFilterParameter filterParameter) {
        return findInterval(getSqlForListing(filterParameter, false), filterParameter.getStart(), filterParameter.getLimit());
    }

    @Override
    public int countReferences(ListingFilterParameter filterParameter) {
        Long count = (Long) findSingle(getSqlForListing(filterParameter, true));
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

    private String getSqlForListing(ListingFilterParameter filterParameter, boolean forCount) {
        StringBuilder sql = new StringBuilder("SELECT ").append(forCount ? "COUNT(r.objectID)" : "r").append(" FROM EdsReference  r LEFT JOIN r.locale loc WHERE r.deleted IS NOT TRUE AND ");
        if (filterParameter.getParentID() != null) {
            sql.append(" r.parent.objectID = ").append(filterParameter.getParentID());
        } else {
            sql.append("r.parent IS NULL");
        }
        if (!StringUtils.isEmpty(filterParameter.getExcludedType())) {
            sql.append(" AND r.code!='").append(filterParameter.getExcludedType()).append("' ");
        }
        if (!StringUtils.isEmpty(filterParameter.getSqlSearchKey())) {
            sql.append(" AND (");
            sql.append(" LOWER(r.name) LIKE '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(" OR LOWER(r.description) LIKE '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(" OR LOWER(r.code) LIKE '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(" OR LOWER(loc.russian) LIKE '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(" OR LOWER(loc.uzbek) LIKE '").append(filterParameter.getSqlSearchKey()).append("'");
            sql.append(" OR LOWER(loc.english) LIKE '").append(filterParameter.getSqlSearchKey()).append("')");
        }
        sql.append(" AND r.shared is true ");
        if (!forCount) {
            sql.append(" ORDER BY ").append(!StringUtils.isEmpty(filterParameter.getSortField()) ? " r." + filterParameter.getSortField() : " r.name ");
            sql.append(!filterParameter.isAscending() ? " DESC " : " ASC");
        }
        return sql.toString();
    }

    /**
     * gets the references....
     *
     * @param parentCode
     */
    public List<EdsReference> listReferences(String parentCode) {
        return listReferences(parentCode, false);
    }

    @Override
    public List<EdsReference> listReferencesByLimit(String parentCode, int limit) {
        return (List<EdsReference>) findLimited("select r from EdsReference r where r.deleted<>true and r.parent.code=?", limit, parentCode);
    }

    /**
     * gets the references....
     *
     * @param parentCode        - parent code
     * @param isSystemReference - is system reference -- if TRUE --> only selected system references, if FALSE --> select all references (system references with custom references)
     * @return - list references
     */
    public List<EdsReference> listReferences(String parentCode, boolean isSystemReference) {
        if (StringUtils.isEmpty(ServerSecurityContext.getInstance().getCompanyId()) || getUser() == null) {
            return Collections.emptyList();
        }
        String order = " order by ";
        if (!StringUtils.isEmpty(parentCode)) {
            order += " r.sorder asc ";
        } else {
            order += " r.id asc ";
        }
        String systemRef = "";
        if (isSystemReference) {
            systemRef += " and " + " r.isSystemReference<>false ";
        }
        String checkForActive = "";
        if (EdsSickRequest._LEAVE_REQUEST_TYPE.equals(parentCode) || EdsTask.TASK_STATUS.equals(parentCode)) {
            checkForActive += " and r.isActive<>false ";
        }

        List<EdsReference> list = find("select r from EdsReference r  where " + ServerUtils.checkForDeleted("r.deleted") + " and r.parent.code=? " + systemRef + checkForActive + order, parentCode);

        return new ArrayList<>(list);
    }

    public Map<String, List<ReferenceItem>> listReferences(List<String> parentCodes, boolean isSystemReference) {
        if (parentCodes == null || parentCodes.isEmpty()) {
            return Collections.emptyMap();
        }
        if (StringUtils.isEmpty(ServerSecurityContext.getInstance().getCompanyId()) || getUser() == null) {
            return Collections.emptyMap();
        }

        List<String> codes = parentCodes.stream()
                .filter(c -> !StringUtils.isEmpty(c))
                .distinct()
                .toList();

        if (codes.isEmpty()) {
            return Collections.emptyMap();
        }

        String hql =
                "select r.parent.code, r.objectID, r.name, r.description, r.cssStyle, r.antonym, " +
                        "       r.code, r.requiredComment, r.relative, r.isCustomButton, r.buttonLocation, r.isActive " +
                        "from EdsReference r " +
                        "where " + ServerUtils.checkForDeleted("r.deleted") +
                        "  and r.parent.code in (:parentCodes) " +
                        (isSystemReference ? " and r.isSystemReference<>false " : "") +
                        "order by r.parent.code asc, r.sorder asc, r.id asc";
        Map<String, Object> params = new HashMap<>();
        params.put("parentCodes", codes);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = (List<Object[]>) findByNamedParams(hql, params);

        Map<String, List<ReferenceItem>> result = new HashMap<>();
        for (Object[] row : rows) {
            String parentCode = (String) row[0];
            Integer id = (Integer) row[1];
            String name = (String) row[2];
            String desc = (String) row[3];
            String cssStyle = (String) row[4];
            String antonym = (String) row[5];
            String code = (String) row[6];
            Boolean selected = (Boolean) row[7];
            String relative = (String) row[8];
            Boolean customBtn = (Boolean) row[9];
            String btnLoc = (String) row[10];
            Boolean isActive = (Boolean) row[11];

            if (requiresActiveOnly(parentCode) && Boolean.FALSE.equals(isActive)) continue;

            ReferenceItem item = new ReferenceItem(id, name, desc, cssStyle, antonym);
            item.setCode(code);
            item.setSelected(Boolean.TRUE.equals(selected));
            item.setRelative(relative);
            item.setCustomButton(Boolean.TRUE.equals(customBtn));
            item.setButtonLocation(btnLoc);

            result.computeIfAbsent(parentCode, k -> new ArrayList<>()).add(item);
        }

        return result;
    }

    private static boolean requiresActiveOnly(String parentCode) {
        return EdsSickRequest._LEAVE_REQUEST_TYPE.equals(parentCode)
                || EdsTask.TASK_STATUS.equals(parentCode);
    }


    public EdsReference findReference(String parentCode, String code) {
        if (ServerSecurityContext.getInstance().getCompanyId() == null) {
            ServerSecurityContext.getInstance().setCompanyId("0");
        }
        return (EdsReference) findSingle("select r from EdsReference r where r.deleted is not true and r.parent.code=? and r.code=?", parentCode, code);
    }

    public EdsReference findReferenceForCrmAccount(String parentCode, String code) {
        if (ServerSecurityContext.getInstance().getCompanyId() == null) {
            ServerSecurityContext.getInstance().setCompanyId("0");
        }
        return (EdsReference) findSingle("select r from EdsReference r where r.parent.code=? and r.code=?", parentCode, code);
    }

    public EdsReference findByParentCodeAndName(String parentCode, String name) {
        if (ServerSecurityContext.getInstance().getCompanyId() == null) {
            ServerSecurityContext.getInstance().setCompanyId("0");
        }
        if (StringUtils.isBlank(name)) {
            return null;
        }
        return (EdsReference) findSingle("select r from EdsReference r where r.deleted is not true and r.parent.code=? and lower(trim(r.name))=?", parentCode, name.trim().toLowerCase());
    }

    public EdsReference findReferenceByDescription(String parentCode, String description) {
        if (ServerSecurityContext.getInstance().getCompanyId() == null) {
            ServerSecurityContext.getInstance().setCompanyId("0");
        }
        return (EdsReference) findSingle("select r from EdsReference r where r.parent.code=? and r.description=?", parentCode, description);
    }

    public EdsReference getReference(Integer objectID) {
        return (EdsReference) findSingle("select r from EdsReference r where r.deleted<>true and r.objectID=?", objectID);
    }

    @Override
    public EdsReference getOriginal(Integer objectID) {
        return super.get(objectID);
    }

    @Override
    public EdsReference get(Integer objectID) {
        EdsReference edsReference = super.get(objectID);
        return (edsReference == null || edsReference.isDeleted()) ? null : edsReference;
    }

    public List<EdsReference> getTimeSheetEntryStatuses() {
        return listReferences(_TIME_SHEET_ENTRY_STATUS);
    }

    public List<EdsReference> getIssueStatuses(boolean... isResolver) {
        if (isResolver != null && isResolver.length > 0) {
            if (isResolver[0]) {
                return listReferences(_ISSUE_STATUS);
            } else {
                return find("select r from EdsReference r where r.deleted<>true and r.parent.code=? and r.code<>?", _ISSUE_STATUS, _ISSUE_STATUS_RESOLVED);
            }
        } else {
            return listReferences(_ISSUE_STATUS);
        }
    }

    public EdsReference findReferenceByCode(String code) {
        return (EdsReference) findSingle("select r from EdsReference r where r.deleted<>true and r.code=? ", code);
    }

    @Override
    public List<EdsReference> getParents() {
        return find("select r from EdsReference r where r.deleted<>true and r.parent is null");
    }

    @Override
    public EdsReference getByName(String name) {
        return (EdsReference) findSingle("select r from EdsReference r where (r.deleted is not null and r.deleted<>true) and lower(r.name) = ?", name.toLowerCase());
    }

    @Override
    public Integer findReferenceId(String parentCode, String code) {
        if (ServerSecurityContext.getInstance().getCompanyId() == null) {
            ServerSecurityContext.getInstance().setCompanyId("0");
        }
        if (StringUtil.isEmpty(parentCode) || StringUtils.isEmpty(code)) {
            return null;
        }
        final String sql = "select r.objectID from EdsReference r " +
                "    where r.parent.code= :parentCode " +
                "        and r.code = :code" +
                "        and (r.deleted is null or r.deleted <> true) ";
        final List<Integer> list = this.slaveEntityManager.createQuery(sql)
                .setParameter("parentCode", parentCode)
                .setParameter("code", code)
                .setMaxResults(1)
                .getResultList();

        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<String> getFieldNamesByCode(String code) {
        return find("select r.name from EdsReference r\n" +
                "where r.parent.code=? and r.deleted is not true and r.isActive is true ", code);
    }

    @Override
    public Map<Integer, EdsReference> getRefernceByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyMap();

        Map<String, Object> params = new HashMap<>();
        params.put("ids", ids);

        List<EdsReference> references = findByNamedParams("SELECT r FROM EdsReference r WHERE r.deleted<>true AND r.objectID IN (:ids)", params);
        return references.stream()
                .collect(Collectors.toMap(EdsReference::getObjectID, Function.identity()));
    }

    @Override
    public Set<EdsReference> getReferenceSetByParentCode(String parentCode) {
        return (Set<EdsReference>) find("SELECT * FROM EdsReference WHERE parentid = (SELECT id FROM EdsReference WHERE code = '" + parentCode + "') ");
    }
}
