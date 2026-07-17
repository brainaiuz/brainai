package com.edatasite.workforce.core.solr.facet;

import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.GroupParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.SOLR_TASK_CORE;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 17:29.
 */
@Component
public class SolrFacetFilterComponent {

    @Autowired
    private SolrTemplate solrTemplate;

    public QueryResponse getFacetFilter(String solrCore, String solrQuery, FacetFilterRpc facetFilterData, Class _clazz) {
        return getFacetFilter(solrCore, solrQuery, facetFilterData, _clazz, -1);
    }


    public QueryResponse getFacetFilter(String solrCore, String solrQuery, FacetFilterRpc facetFilterData, Class _clazz, int facetLimit) {

        SolrClient server = WfmJpaTemplate.getSolrServerForCore(solrCore);
        SolrQuery query = new SolrQuery();
        query.setQuery(solrQuery);

        for (String key : facetFilterData.getShowSolrFieldMap().keySet()) {
            query.addFacetField(facetFilterData.getShowSolrFieldMap().get(key).getSolrFacetFieldName());
        }
        query.setFacetMinCount(1);
        query.setFacet(true);

        if (Constants.SOLR_TASK_CORE.equals(solrCore)) {
            query.set(GroupParams.GROUP, true);
            query.set(GroupParams.GROUP_TRUNCATE, true);
            query.set(GroupParams.GROUP_MAIN, true);
            query.set(GroupParams.GROUP_FIELD, SolrTaskRepresenter.FIELD_TASK_ID);
        }

        if (facetLimit != -1) {
            query.setFacetLimit(facetLimit);
        } else {
            query.setFacetLimit(WfmJpaTemplate.SOLR_FACET_LIMIT);
        }

        query.setFacetMissing(true);

        QueryResponse resp = null;
        try {
            resp = server.query(query, SolrRequest.METHOD.POST);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return resp;
    }
}
