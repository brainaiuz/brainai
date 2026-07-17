package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 09.11.2008
 * Time: 17:38:10
 * To change this template use File | Settings | File Templates.
 */
public interface WorkStreamManager extends Manager<EdsWorkStream>/*, SearchHelper*/ {

    List<EdsWorkStream> listByProjectId(Integer projectId);

    List<EdsWorkStream> findOrphanWorkstreams(Integer projectId);

    List<EdsWorkStream> findOrphanWorkstreamsByAlphabitic(Integer projectId);

    List<EdsWorkStream> findOrphanWorkstreams(ListingFilterParameter filterParameter);

    List<EdsWorkStream> findOrphanWorkstreams(Integer projectId, Date from, Date to, String sortBy);

    List<EdsWorkStream> findOrphanWorkstreams(Integer projectId, Date from, Date to, String sortBy, Integer start, Integer limit);

    List<Integer> getWorkStreamsSomeParent(Integer parentID);

    List<EdsWorkStream> listByProjectIds(String projectIds);

    List<EdsWorkStream> getOrderByWorkStream(ListingFilterParameter filterParameter);

    Object getWSPercent(Integer objectID);

    Date getWSStartDateByTask(Integer objectID);

    Date getWSEndDAteByTask(Integer objectID);

    EdsProject selectCompilitedStatus(Integer projectID);

    Integer getWorkSreamLastIntNumber(Integer projectID, boolean unique);

    String getSavedNumberformat(Integer objectID);

	Integer getEmployeeAssignedTasksCount(Integer workStreamID, Integer employeeID);
}
