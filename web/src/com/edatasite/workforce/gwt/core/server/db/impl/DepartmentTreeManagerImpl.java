package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsDepartmentTree;
import com.edatasite.workforce.gwt.core.client.rpc.ChartNode;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.DepartmentTreeManager;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dilsh0d on 18.03.16.
 */
@Repository("departmentTreeManager")
public class DepartmentTreeManagerImpl extends BaseManager<EdsDepartmentTree> implements DepartmentTreeManager {

    public DepartmentTreeManagerImpl() {
        super(EdsDepartmentTree.class);
    }

    @Override
    public void addChildTree(Integer newChildId, Integer parentId) {

        Query orderQuery = masterEntityManager.createNativeQuery(
                "SELECT COALESCE(MAX(sorder), -1) + 1 " +
                        "FROM " + getCompanyId() + ".team_tree " +
                        "WHERE parent_id = :parent_id AND depth = 1"
        ).setParameter("parent_id", parentId);
        int nextOrder = ((Number) orderQuery.getSingleResult()).intValue();

        Query query = masterEntityManager.createNativeQuery(
                        " INSERT INTO " + getCompanyId() + ".team_tree(parent_id, child_id, depth, sorder) " +
                                " SELECT p.parent_id, c.child_id, p.depth+c.depth+1, " +
                                "   CASE WHEN p.depth = 0 AND c.depth = 0 THEN :next_order ELSE 0 END " +
                                " FROM " + getCompanyId() + ".team_tree p, " + getCompanyId() + ".team_tree c " +
                                " WHERE p.child_id = :parent_id AND c.parent_id = :child_id"
                )
                .setParameter("child_id", newChildId)
                .setParameter("parent_id", parentId)
                .setParameter("next_order", nextOrder);

        query.executeUpdate();
    }

    @Override
    public void removeSubtreeFromParents(Integer team_id) {
        Query query = masterEntityManager.createNativeQuery(" DELETE FROM " + getCompanyId() + ".team_tree " +
                " WHERE parent_id not in (SELECT child_id FROM " + getCompanyId() + ".team_tree WHERE parent_id =:team_id) " +
                " and child_id IN (SELECT child_id FROM " + getCompanyId() + ".team_tree WHERE parent_id =:team_id)")
                .setParameter("team_id", team_id);
        query.executeUpdate();
    }

    @Override
    public Integer getParent(Integer childId) {
        Query query = slaveEntityManager.createNativeQuery("SELECT parent_id FROM " + getCompanyId() + ".team_tree" +
                " WHERE child_id =:child_id and depth != 0 ORDER BY depth ASC")
                .setParameter("child_id", childId)
                .setMaxResults(1);
        List<Integer> result = query.getResultList();
        if (result.isEmpty() || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public SelectItem getParentItem(Integer childId) {
        Query query = slaveEntityManager.createNativeQuery("SELECT tt.parent_id,t.name,t.externalGUID FROM " + getCompanyId() + ".team_tree tt" +
                " INNER JOIN " + getCompanyId() + ".team t on t.id=tt.parent_id" +
                " WHERE tt.child_id =:child_id and tt.depth != 0 ORDER BY tt.depth ASC")
                .setParameter("child_id", childId)
                .setMaxResults(1);

        List<Object[]> result = query.getResultList();
        if (result.isEmpty() || result.size() == 0) {
            return null;
        }
        SelectItem item = new SelectItem();
        item.setId((Integer) result.get(0)[0]);
        item.setName((String) result.get(0)[1]);
        item.setDescription((String) result.get(0)[2]);

        return item;
    }
    @Override
    public SelectItem getParentItemByChildId(Integer childId) {
        Query query = masterEntityManager.createNativeQuery("SELECT tt.parent_id,t.name,t.externalGUID, t.numberData FROM " + getCompanyId() + ".team_tree tt" +
                " INNER JOIN " + getCompanyId() + ".team t on t.id=tt.parent_id" +
                " WHERE tt.child_id =:child_id and tt.depth != 0 ORDER BY tt.depth ASC")
                .setParameter("child_id", childId)
                .setMaxResults(1);

        List<Object[]> result = query.getResultList();
        if (result.isEmpty() || result.size() == 0) {
            return null;
        }
        SelectItem item = new SelectItem();
        item.setId((Integer) result.get(0)[0]);
        item.setName((String) result.get(0)[1]);
        item.setDescription((String) result.get(0)[2]);
        item.setCode((String) result.get(0)[3]);

        return item;
    }

    @Override
    public List<Integer> getChildList(Integer parentId) {
        Query query = slaveEntityManager.createNativeQuery("select tt.child_id from " + getCompanyId() + ".team_tree tt " +
                        " inner join " + getCompanyId() + ".team t on t.id=tt.parent_id " +
                        " inner join " + getCompanyId() + ".team cht on cht.id=tt.child_id " +
                        " where t.isdeleted is not true " +
                        " and cht.isdeleted is not true " +
                        " and tt.parent_id =:parent_id " +
                        " and tt.depth != 0 " +
                        " order by tt.depth desc")
                .setParameter("parent_id", parentId);
        return query.getResultList();
    }

    @Override
    public List<Integer> getAllChildList(Integer parentId) {
        Query query = slaveEntityManager.createNativeQuery("select tt.child_id from " + getCompanyId() + ".team_tree tt " +
                        " inner join " + getCompanyId() + ".team t on t.id=tt.parent_id " +
                        " inner join " + getCompanyId() + ".team cht on cht.id=tt.child_id " +
                        " where t.isdeleted is not true " +
                        " and cht.isdeleted is not true " +
                        " and tt.parent_id =:parent_id " +
                        " order by tt.depth desc")
                .setParameter("parent_id", parentId);
        return query.getResultList();
    }

    @Override
    public List<Integer> getTreeTeamLeadersList(Integer childId) {
        Query query = slaveEntityManager.createNativeQuery("(SELECT t.leaderId FROM " + getCompanyId() + ".team_tree tt " +
                "inner join " + getCompanyId() + ".team t on t.id=tt.parent_id " +
                "                WHERE tt.child_id =" + childId + " and tt.depth != 0 ORDER BY tt.depth DESC) " +
                "UNION ALL " +
                "SELECT leaderId FROM " + getCompanyId() + ".team WHERE id=" + childId);
        Set<Integer> set = new LinkedHashSet<Integer>(query.getResultList());
        return new ArrayList<>(set);
    }

    /**
     * <b> Department nodes use closure table if you don't understand
     * my logic then please read closure table algorithm </b>
     * <i> technobytz.com/closure_table_store_hierarchical_data.html </i>
     * <br/>
     *
     * @return
     */
    public List<ChartNode> getTeamGraph(Integer parentId, Integer locationId) {

        String lang = ServerUtils.getUserLocale().getLanguage();
        String sql = "WITH RECURSIVE team_tree_show AS (" +
                "   SELECT tt.child_id, tt.parent_id,1 as depth, tt.child_id\\:\\:text as path, 0 as sorder " +
                "       FROM " + getCompanyId() + ".team_tree tt      " +
                "       WHERE tt.depth=0 AND tt.parent_id NOT IN (SELECT child_id FROM " + getCompanyId() + ".team_tree WHERE depth!=0 )" +
                "   UNION ALL" +
                "       SELECT t.child_id, tt.child_id, tt.depth+1, tt.path || ',' || t.child_id\\:\\:text, t.sorder " +
                "           FROM team_tree_show tt " +
                "           JOIN  " + getCompanyId() + ".team_tree t ON t.parent_id=tt.child_id" +
                "           WHERE t.depth!=0 AND t.depth=1" +
                ") " +
                "SELECT distinct t.id,";
        switch (lang) {
            case "en" -> sql += "COALESCE (rl.english, t.name),";
            case "ru" -> sql += "COALESCE (rl.russian, t.name),";
            case "uz" -> sql += "COALESCE (rl.uzbek, t.name),";
            case "ar" -> sql += "COALESCE (rl.arabic, t.name),";
            default -> sql += "t.name";
        }
        sql += "t.description,t.leaderId,tt.depth, tt.path, tt.sorder,t.leaderIsVacant,t.numberdata " +
                "   FROM " + getCompanyId() + ".team t" +
                " LEFT JOIN " + getCompanyId() + ".reference_locale rl ON rl.id = t.localeid" +
                "   INNER JOIN team_tree_show tt ON t.id = tt.child_id " +
                "   WHERE (t.isDeleted IS NULL OR t.isDeleted=FALSE) and t.active = true " +
                (locationId != null ? " and t.locationId  =  " + locationId : "") +
                (parentId != null ? " AND (t.id=" + parentId + "  or tt.path  ILIKE ('" + parentId + ",%') OR tt.path  ILIKE ('%," + parentId + ",%')) " : "") +
                "   ORDER BY tt.path ";

        Query query = slaveEntityManager.createNativeQuery(sql);
        return getTeamNodeList(query.getResultList(), parentId);
    }

    @Override
    public LinkedList<SelectItem> getDepartments(Integer parentId) {
        List<Object[]> ttids = findNative("select * from " +
                " ( select distinct tm.id teamId, tm.name teamName, t.sorder " +
                " from " + getCompanyId() + ".team_tree tt " +
                " left join " + getCompanyId() + ".team_tree uss on uss.depth!=0 and tt.parent_id=uss.child_id " +
                " JOIN " + getCompanyId() + ".team_tree t ON t.parent_id=tt.child_id and t.depth!=0 AND t.depth=1 " +
                " JOIN " + getCompanyId() + ".team tm ON t.child_id=tm.id " +
                "where t.parent_id= " + parentId + " and uss.id is null and tm.isdeleted = false ) pld " +
                " order by pld.sorder,pld.teamName ");
        LinkedList<SelectItem> result = new LinkedList<>();
        for (Object[] teamid : ttids) {
            result.add(new SelectItem((Integer) teamid[0], (String) teamid[1]));
        }
        return result;
    }

    @Override
    public List<EdsDepartmentTree> getDepartmentTreeByDepartmentID(Integer departmentId) {
        return find("select t from EdsDepartmentTree t where t.childId = " + departmentId);
    }

    @Override
    public List<Integer> getAncestorsAndChildren(Integer departmentId) {
        Query children = slaveEntityManager.createNativeQuery("select tt.child_id from " + getCompanyId() + ".team_tree tt " +
                " inner join " + getCompanyId() + ".team t on t.id=tt.parent_id " +
                " inner join " + getCompanyId() + ".team cht on cht.id=tt.child_id " +
                " where t.isdeleted is not true " +
                " and cht.isdeleted is not true " +
                " and tt.parent_id =:department_id " +
                " and tt.depth != 0 " +
                " order by tt.depth desc")
                .setParameter("department_id", departmentId);

        Query ancesters = slaveEntityManager.createNativeQuery("select tt.parent_id from " + getCompanyId() + ".team_tree tt " +
                        " inner join " + getCompanyId() + ".team t on t.id=tt.parent_id " +
                        " inner join " + getCompanyId() + ".team cht on cht.id=tt.child_id " +
                        " where t.isdeleted is not true " +
                        " and cht.isdeleted is not true " +
                        " and tt.child_id =:department_id " +
                        " and tt.depth != 0 " +
                        " order by tt.depth desc")
                .setParameter("department_id", departmentId);

        List<Integer> result = ancesters.getResultList();
        result.addAll(children.getResultList());
        return result;
    }

    private List<ChartNode> getTeamNodeList(List<Object[]> resultList, Integer parentId) {
        Map<Integer, ChartNode> nodeMap = new HashMap<>();
        List<ChartNode> teamNodes = new ArrayList<>();
        for (Object[] objects : resultList) {
            Integer id = (Integer) objects[0];
            String name = (String) objects[1];
            String desc = (String) objects[2];
            Integer leaderId = (Integer) objects[3];
            Integer depth = (Integer) objects[4];
            String path = ((String) objects[5]);
            Integer sorder = ((Integer) objects[6]);
            Boolean leaderIsVacant = objects[7] != null ? ((Boolean) objects[7]) : false;
            String code = objects[8] != null ? (String) objects[8] : null;
            String[] pathArr = null;
            if (path != null && !"".equals(path.trim())) {
                pathArr = path.split(",");
            }
            ChartNode node = new ChartNode(id, code != null ? code + "->" + name : name, desc, leaderId, depth, null, sorder);
//            if (leaderIsVacant != null && leaderIsVacant) {
//                node.setLeaderId(-1);
//            }
            if (pathArr.length > 1 && (parentId == null || !parentId.equals(id))) {
                ChartNode parentNode = nodeMap.get(Integer.valueOf(pathArr[pathArr.length - 2]));
                if (parentNode != null) {
                    node.setParent(parentNode);
                    parentNode.addChild(node);
                }
                ChartNode generalParent = nodeMap.get(Integer.valueOf(pathArr[0]));
                if (generalParent != null) {
                    generalParent.setDepth(generalParent.getDepth() + 1);
                }
            } else {
                teamNodes.add(node);
            }
            nodeMap.put(node.getId(), node);
        }
        teamNodes.sort((o1, o2) -> o2.getDepth().compareTo(o1.getDepth()));
        return teamNodes;
    }

    @Override
    public Integer getRootParentByLocation(Integer locationID) {
        String sql = "SELECT tt.parent_id FROM " + getCompanyId() + ".team_tree tt \n" +
                "inner join " + getCompanyId() + ".team t on t.id=tt.parent_id \n" +
                "inner join " + getCompanyId() + ".team cht on cht.id=tt.child_id \n" +
                "where t.isdeleted is not true and cht.isdeleted is not true \n" +
                "and t.locationid=" + locationID + " and tt.depth != 0 \n" +
                "order by tt.depth desc";
        return (Integer) findNativeSingle(sql);
    }

    @Override
    public List<Object[]> getFullSubtreeData(Integer rootId, Integer rootLocationId, Integer locationId) {

        String lang = ServerUtils.getUserLocale().getLanguage();

        String nameField;
        String descriptionField;
        String shortDescriptionField;

        String col;
        switch (lang) {
            case "en" -> col = "english";
            case "ru" -> col = "russian";
            case "uz" -> col = "uzbek";
            case "ar" -> col = "arabic";
            default -> col = null;
        }

        if (col != null) {
            nameField = "COALESCE(NULLIF(TRIM(rl." + col + "), ''), t.name)";
            descriptionField = "COALESCE(NULLIF(TRIM(drl." + col + "), ''), t.description)";
            shortDescriptionField = "COALESCE(NULLIF(TRIM(srl." + col + "), ''), t.short_description)";
        } else {
            nameField = "t.name";
            descriptionField = "t.description";
            shortDescriptionField = "t.short_description";
        }

        String baseSql = """
                WITH RECURSIVE team_tree_show AS (
                    SELECT
                        tt.child_id,
                        tt.parent_id,
                        1 AS depth,
                        CAST(tt.child_id AS TEXT) AS path,
                        0 AS sorder
                    FROM %1$s.team_tree tt
                    WHERE tt.depth = 0
                      AND tt.parent_id NOT IN (
                            SELECT child_id
                            FROM %1$s.team_tree
                            WHERE depth != 0
                      )
                
                    UNION ALL
                
                    SELECT
                        t.child_id,
                        tt.child_id,
                        tt.depth + 1,
                        tt.path || ',' || CAST(t.child_id AS TEXT),
                        t.sorder
                    FROM team_tree_show tt
                    JOIN %1$s.team_tree t
                        ON t.parent_id = tt.child_id
                    WHERE t.depth != 0
                      AND t.depth = 1
                )
                
                SELECT DISTINCT
                    t.id,
                    %2$s AS name,
                    %3$s AS description,
                    %4$s AS short_description,
                    t.leaderId,
                    tt.depth,
                    tt.path,
                    tt.sorder,
                    t.numberdata,
                    t.child_orientation,
                    t.locationId,
                    t.color
                FROM %1$s.team t
                LEFT JOIN %1$s.reference_locale rl
                    ON rl.id = t.localeid
                LEFT JOIN %1$s.reference_locale drl
                    ON drl.id = t.description_locale_id
                LEFT JOIN %1$s.reference_locale srl
                    ON srl.id = t.short_description_locale_id
                INNER JOIN team_tree_show tt
                    ON t.id = tt.child_id
                WHERE (t.isDeleted IS NULL OR t.isDeleted = FALSE)
                  AND t.active = TRUE
                """;

        String sql = String.format(baseSql, getCompanyId(), nameField, descriptionField, shortDescriptionField);

        if (rootId != null) {
            String rootFilter = String.format("""
                    AND (
                        t.id = %1$d
                        OR tt.path ILIKE ('%1$d,%%')
                        OR tt.path ILIKE ('%%,%1$d,%%')
                    )
                    """, rootId);
            sql += rootFilter;
        }

        if (locationId != null) {
            String locationFilter;
            if (rootLocationId != null) {
                locationFilter = String.format("""
                AND (
                    t.id = %1$d
                    OR t.locationId = %2$d
                    OR t.locationId = %3$d
                )
                """, rootId, locationId, rootLocationId);
            } else {
                locationFilter = String.format("""
                AND (
                    t.id = %1$d
                    OR t.locationId = %2$d
                )
                """, rootId, locationId);
            }
            sql += locationFilter;
        }



        sql += " ORDER BY tt.path";

        Query query = slaveEntityManager.createNativeQuery(sql);
        return query.getResultList();
    }


    @Override
    public List<EdsDepartmentTree> getDirectSubtreeByParent(Integer parentId) {
        String baseSql = """
                        SELECT tt.*
                        FROM   %1$s.team_tree tt
                        JOIN   %1$s.team t
                               ON t.id = tt.child_id
                        WHERE  tt.parent_id = :parentId
                          AND  tt.depth = 1
                          AND  (t.isDeleted = false OR t.isDeleted IS NULL)
                        ORDER BY tt.sorder, t.name;
                """;

        String sql = String.format(baseSql, getCompanyId());
        Query query = slaveEntityManager.createNativeQuery(sql, EdsDepartmentTree.class);
        query.setParameter("parentId", parentId);
        return query.getResultList();
    }

    @Override
    public List<Object[]> getDepartmentWithDirectChildren(Integer rootId) {

        if (rootId == null) {
            throw new IllegalArgumentException("rootId must not be null");
        }

        String lang = ServerUtils.getUserLocale().getLanguage();

        String sql = """
                WITH dept_and_children AS (
                    SELECT
                        tp.id                          AS id,
                        COALESCE(
                            NULLIF(TRIM(CASE :lang
                                WHEN 'en' THEN rlp.english
                                WHEN 'ru' THEN rlp.russian
                                WHEN 'uz' THEN rlp.uzbek
                                WHEN 'ar' THEN rlp.arabic
                            END), ''),
                            NULLIF(TRIM(rlp.english), ''),
                            NULLIF(TRIM(rlp.russian), ''),
                            NULLIF(TRIM(rlp.uzbek), ''),
                            NULLIF(TRIM(tp.name), '')
                        )                              AS name,
                        COALESCE(
                            NULLIF(TRIM(CASE :lang
                                WHEN 'en' THEN drlp.english
                                WHEN 'ru' THEN drlp.russian
                                WHEN 'uz' THEN drlp.uzbek
                                WHEN 'ar' THEN drlp.arabic
                            END), ''),
                            NULLIF(TRIM(drlp.english), ''),
                            NULLIF(TRIM(drlp.russian), ''),
                            NULLIF(TRIM(drlp.uzbek), ''),
                            NULLIF(TRIM(tp.description), '')
                        )                              AS description,
                        COALESCE(
                            NULLIF(TRIM(CASE :lang
                                WHEN 'en' THEN srlp.english
                                WHEN 'ru' THEN srlp.russian
                                WHEN 'uz' THEN srlp.uzbek
                                WHEN 'ar' THEN srlp.arabic
                            END), ''),
                            NULLIF(TRIM(srlp.english), ''),
                            NULLIF(TRIM(srlp.russian), ''),
                            NULLIF(TRIM(srlp.uzbek), ''),
                            NULLIF(TRIM(tp.short_description), '')
                        )                              AS short_description,
                        tp.leaderId                    AS leader,
                        0                              AS depth,
                        CAST(tp.id AS TEXT)            AS path,
                        0                              AS sorder,
                        tp.numberdata                  AS numberdata,
                        tp.child_orientation           AS child_orientation,
                        tp.locationId                  AS location,
                        tp.color                       AS color
                    FROM %1$s.team tp
                    LEFT JOIN %1$s.reference_locale rlp
                        ON rlp.id = tp.localeid
                    LEFT JOIN %1$s.reference_locale drlp
                        ON drlp.id = tp.description_locale_id
                    LEFT JOIN %1$s.reference_locale srlp
                        ON srlp.id = tp.short_description_locale_id
                    WHERE tp.id = :rootId
                      AND (tp.isDeleted IS NULL OR tp.isDeleted = FALSE)
                      AND tp.active = TRUE
                
                    UNION ALL
                
                    SELECT
                        tc.id                          AS id,
                        COALESCE(
                            NULLIF(TRIM(CASE :lang
                                WHEN 'en' THEN rlc.english
                                WHEN 'ru' THEN rlc.russian
                                WHEN 'uz' THEN rlc.uzbek
                                WHEN 'ar' THEN rlc.arabic
                            END), ''),
                            NULLIF(TRIM(rlc.english), ''),
                            NULLIF(TRIM(rlc.russian), ''),
                            NULLIF(TRIM(rlc.uzbek), ''),
                            NULLIF(TRIM(tc.name), '')
                        )                              AS name,
                        COALESCE(
                            NULLIF(TRIM(CASE :lang
                                WHEN 'en' THEN drlc.english
                                WHEN 'ru' THEN drlc.russian
                                WHEN 'uz' THEN drlc.uzbek
                                WHEN 'ar' THEN drlc.arabic
                            END), ''),
                            NULLIF(TRIM(drlc.english), ''),
                            NULLIF(TRIM(drlc.russian), ''),
                            NULLIF(TRIM(drlc.uzbek), ''),
                            NULLIF(TRIM(tc.description), '')
                        )                              AS description,
                        COALESCE(
                            NULLIF(TRIM(CASE :lang
                                WHEN 'en' THEN srlc.english
                                WHEN 'ru' THEN srlc.russian
                                WHEN 'uz' THEN srlc.uzbek
                                WHEN 'ar' THEN srlc.arabic
                            END), ''),
                            NULLIF(TRIM(srlc.english), ''),
                            NULLIF(TRIM(srlc.russian), ''),
                            NULLIF(TRIM(srlc.uzbek), ''),
                            NULLIF(TRIM(tc.short_description), '')
                        )                              AS short_description,
                        tp.leaderId                    AS leader,
                        1                              AS depth,
                        CAST(tp.id AS TEXT) || ',' ||
                        CAST(tc.id AS TEXT)            AS path,
                        tt.sorder                      AS sorder,
                        tc.numberdata                  AS numberdata,
                        tc.child_orientation           AS child_orientation,
                        tp.locationId                  AS location,
                        tp.color                       AS color
                    FROM %1$s.team_tree tt
                    JOIN %1$s.team tp
                        ON tp.id = tt.parent_id
                    JOIN %1$s.team tc
                        ON tc.id = tt.child_id
                    LEFT JOIN %1$s.reference_locale rlc
                        ON rlc.id = tc.localeid
                    LEFT JOIN %1$s.reference_locale drlc
                        ON drlc.id = tc.description_locale_id
                    LEFT JOIN %1$s.reference_locale srlc
                        ON srlc.id = tc.short_description_locale_id
                    WHERE tt.parent_id = :rootId
                      AND tt.depth = 1
                      AND (tc.isDeleted IS NULL OR tc.isDeleted = FALSE)
                      AND tc.active = TRUE
                )
                SELECT
                    id,
                    name,
                    description,
                    short_description,
                    leader,
                    depth,
                    path,
                    sorder,
                    numberdata,
                    child_orientation,
                    location,
                    color
                FROM dept_and_children
                ORDER BY depth, sorder, path
                """.formatted(getCompanyId());

        Query query = slaveEntityManager.createNativeQuery(sql);
        query.setParameter("rootId", rootId);
        query.setParameter("lang", lang);

        @SuppressWarnings("unchecked")
        List<Object[]> result = query.getResultList();
        return result;
    }


}
