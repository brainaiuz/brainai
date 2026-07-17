package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Tulqin
 * Date: 8/16/13
 * Time: 7:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndexCompanySolrCoresServlet implements HttpRequestHandler {
    private static final Logger log = LoggerFactory.getLogger(IndexCompanySolrCoresServlet.class);
    @Autowired
    BackendService backendService;
    SolrReindexRpc solrReindexRpc;


    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        String indexResult = null;
        try {
            solrReindexRpc = new SolrReindexRpc();
            Integer companyID = Integer.valueOf(request.getParameter("SchemaNumber"));
            String DbType = String.valueOf(request.getParameter("DbType"));

            if (companyID != null && !"".equals(companyID) && DbType != null && !"".equals(DbType)) {
                ServerSecurityContext.getInstance().setDatabase(DbType);
                solrReindexRpc.setCompanyId(companyID);
                indexResult = backendService.indexAllCoresOfSelectedCompany(solrReindexRpc);

//                PrintWriter writer = response.getWriter();
//                writer.write(indexResult + " CompanyID = " + companyID);
//                writer.close();

                response.getOutputStream().println(indexResult + " CompanyID = " + companyID);

            } else
                log.info("For automoving solr reindex, companyid and DataBase types must not be null");

        } catch (NumberFormatException e) {
            log.error(e.getMessage());
//            PrintWriter writer = response.getWriter();
//            writer.write("Solr index error!");
//            writer.close();

            response.getOutputStream().println("Solr index error!");
        }
    }
}
