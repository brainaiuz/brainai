package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface PositionManager extends Manager<EdsPosition> {
    List<EdsPosition> list();

    List<EdsPosition> listCompanyPositions();

    EdsPosition getPositionForEdit(Integer posId);

    List getPositionList(ListingFilterParameter fp);

    ArrayList<EdsPosition> getPostionsByLocation(Integer locationId);

    SelectItem[] getPositionListAsSelectItem(ListingFilterParameter fp);

    void deletePosition(EdsPosition position);

    Integer getPositionListCount(ListingFilterParameter filterParametrs);

    EdsPosition getByName(String name);

    List<EdsPosition> getPositionListByStatusID(Integer status, ListingFilterParameter fp);

    EdsPosition getPositionByName(String name, Integer objectID);

    EdsPosition getPositionByCode(String code, Integer objectID);

    boolean isPositionNumberExist(String numberString, Integer objectID);

    Integer getPositionLastIntNumber();

    Integer getPositionsSizeByNameForValidation(String positionName, Integer locationId, Integer objectId);

    Map<Integer, Integer> getPositionHeadCountMap();

    List<EdsPosition> getPositionsForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<Integer> getPositionIdsByIds(String ids);

    List<EdsPosition> getPositionWithDistinctName();

    List<Integer> getPositionIdsWithLimit(Integer start, Integer limit);

    List<EdsPosition> getPositionListByName(String name);

    ArrayList<EdsPosition> getPositionListByDepartment(Integer departmentId);


    ArrayList<SelectItem> getReferenceRelatedPositions(Integer referenceId);

    List<Integer> getCompanyDeletedPositionsForSolr(SolrReindexRpc solrReindex);
}
