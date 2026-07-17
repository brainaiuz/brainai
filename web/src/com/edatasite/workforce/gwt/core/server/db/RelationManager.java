package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 8/12/11
 * Time: 5:32 AM
 * To change this template use File | Settings | File Templates.
 */
public interface RelationManager extends Manager<EdsRelation> {

    List<EdsRelation> getAllRelations(String type, Integer id);

    LinkedHashMap<Integer, List<EdsRelation>> getAllRelationsMapByObjectId(String type, List<Integer> ids);

    List<EdsRelation> getAllRelations(ListingFilterParameter filterParameter);

    List<EdsRelation> getAllRelations(String type, List<Integer> ids);

    List<EdsRelation> getAllFromRelations(String type, List<Integer> ids);

    List<EdsRelation> getAllToRelations(String type, List<Integer> ids);

    void deleteAllRelations(String type, Integer id);

    void updateName(String type, Integer id, String name);

    void mergeCrmAccountRelations(Integer objectID, String name, List<Integer> otherAccountIDs);

    void mergeRelationByType(String relationType, Integer objectID, String name, List<Integer> otherAccountIDs);

    List<Integer> getRelationIDsByType(Integer id, Integer entityID, String type, String relationType);

    List<Integer> getRelationIDsByType(Integer id, String type, String relationType);

    List<Integer> getRelationIDsByTypeAndIds(Integer id, String type, String relationType);

    List<Object[]> getRelationsByIdAndType(Integer id, String type, String relationType);

    List<EdsRelation> getByRelationItem(RelationItem relationItem, boolean exactRelation);

    void delete(Integer objectID);

    void changeTypesByType(String fromLead, ArrayList<Integer> leadIDsForRelations, String toType);

    Map<Integer, String> getAllRelationsForTimesheet(String type, String ids);

    List<Object[]> getCurrentInterviews();

    List<EdsRelation> getRelationsByRelationTypeToID(String toType, Integer toId);

    List<EdsRelation> getRelationsByRelationFromTypeToID(String fromType, Integer toId);

    List<EdsRelation> getRelationsByRelationFromTypeFromID(String fromType, Integer toId);

    void updateSolr(HashMap<String, ArrayList<Integer>> typeIDs) throws IOException, SolrServerException, InterruptedException;

    void deleteWorkflowRelatedRelations(Integer objectID, String type);

    void mergeCrmContactRelations(Integer objectID, String name, List<Integer> otherContactIDs);

    void mergeCrmContactInvoices(Integer otherContactObjectID, String fromType, Integer newContactID);

    List<Integer> getCustomFormForCurrentUser(Integer currentuserid, String formId);

}
