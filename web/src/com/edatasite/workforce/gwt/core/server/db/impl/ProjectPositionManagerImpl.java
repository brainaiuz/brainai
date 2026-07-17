package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsProjectPosition;
import com.edatasite.workforce.gwt.core.server.db.ProjectPositionManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Normurod on 8/5/15.
 */
@Repository("projectPositionManager")
public class ProjectPositionManagerImpl extends BaseManager<EdsProjectPosition> implements ProjectPositionManager {
    public ProjectPositionManagerImpl() {
        super(EdsProjectPosition.class);
    }

    @Override
    public List<EdsProjectPosition> getProjectPositions(Integer projectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ps FROM EdsProjectPosition ps join ps.project p WHERE (ps.deleted is null OR ps.deleted is false) \n");
        sql.append("AND p.objectID = ").append(projectID).append("\n");
        return find(sql.toString());
    }

    @Override
    public List<EdsProjectEmployee> getProjectPositionEmployees(Integer projectID, Integer positionID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT pe FROM EdsProjectEmployee pe join pe.project p join pe.position ps WHERE (pe.isDeleted is null OR pe.isDeleted is false) \n");
        sql.append("AND p.objectID = ").append(projectID).append("\n");
        sql.append("AND ps.objectID = ").append(positionID).append("\n");
        return find(sql.toString());
    }

    @Override
    public EdsProjectPosition getProjectPosition(Integer projectID, Integer positionID) {
        if (projectID == null || positionID == null) {
            return null;
        }
        return (EdsProjectPosition)findSingle("select pp from EdsProjectPosition pp join pp.project p join pp.position ps where (pp.deleted is false or pp.deleted is null) and p.objectID = ? and ps.objectID = ?", projectID, positionID);
    }

    @Override
    public Date getProjectPositionLastDate(Integer contractID){
        if (contractID == null) {
            return null;
        }
        String sql = "select MIN(contractEndDate) from EdsProjectPosition where contractid = ?";
        return (Date)findSingle(sql, contractID);
    }
}
