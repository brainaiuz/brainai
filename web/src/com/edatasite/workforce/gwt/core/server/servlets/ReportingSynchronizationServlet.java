package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUploadAmazonSettings;
import com.edatasite.workforce.core.domain.EdsUploadMinIOSettings;
import com.edatasite.workforce.core.domain.EdsUploadSettings;
import com.edatasite.workforce.core.domain.reporting.EdsReportTemplate;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.rpc.RpcMap;
import com.edatasite.workforce.utils.EdsContextParams;
import com.finnetlimited.reportservice.core.server.CoreServiceLocal;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Virus on 11/14/14.
 */
public class ReportingSynchronizationServlet extends HttpServlet implements Constants {

    private static Logger log = LoggerFactory.getLogger(ReportingSynchronizationServlet.class);

    @Override
    public void init(ServletConfig config) {
    }

    @Transactional
    @Override
    public void doPost(final HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String dataBase = req.getParameter("database");
        System.out.println("Begin Synchronization " + dataBase);
        ServerSecurityContext.getInstance().setDatabase(dataBase);

        final CoreServiceLocal coreServiceLocal = (CoreServiceLocal) ApplicationContextProvider.applicationContext.getBean("reportingCoreService");
        CompanyManager companyManager = (CompanyManager) ApplicationContextProvider.applicationContext.getBean("companyManager");
        LinkedHashMap<String, EdsReportTemplate> templateMap = new LinkedHashMap<>();

        HashMap map = new Gson().fromJson(new InputStreamReader(req.getInputStream()), HashMap.class);
        ArrayList<HashMap> templateList = (ArrayList<HashMap>) map.get("templateList");
        ArrayList<HashMap> reportList = (ArrayList<HashMap>) map.get("reportList");
        final ArrayList<EdsReport> edsReporList = new ArrayList<>();
        final HashMap<Integer, EdsUpload> uploadHashMap = new HashMap<>();
        final HashMap<Integer, EdsUploadSettings> uploadSettingsHashMap = new HashMap<>();

        for (HashMap rpcMap : templateList) {
            try {
                EdsReportTemplate edsReportTemplate = new EdsReportTemplate();
                RpcMap.set(rpcMap, edsReportTemplate);
//                edsReportTemplate.setTempReportTemplateCategory(edsReportTemplate.getReportTemplateCategory().getObjectID());
//                edsReportTemplate.setReportTemplateCategory(null);
//                edsReportTemplate.setCustomReportTemplates(null);
                templateMap.put(edsReportTemplate.getCode(), edsReportTemplate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (HashMap rpcMap : reportList) {
            EdsReport edsReport = new EdsReport();
            try {
                RpcMap.set(rpcMap, edsReport);
                if (rpcMap.get("childMap") != null && ((HashMap) rpcMap.get("childMap")).get("excelTemplate") != null) {

                    EdsUpload edsUpload = new EdsUpload();
                    EdsUploadSettings edsUploadSettings = MINIO.equals(EdsContextParams.getUploadType()) ? new EdsUploadMinIOSettings() : new EdsUploadAmazonSettings();
                    RpcMap.set((HashMap) ((HashMap) rpcMap.get("childMap")).get("excelTemplate"), edsUpload);
                    RpcMap.set((HashMap) ((HashMap) rpcMap.get("childMap")).get("uploadAmazonSettings"), edsUploadSettings);
                    uploadHashMap.put(edsReport.getExcelTemplateId(), edsUpload);
                    uploadSettingsHashMap.put(edsReport.getExcelTemplateId(), edsUploadSettings);
                }
//                System.out.println(edsReport);
                edsReporList.add(edsReport);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        final StringBuilder stringBuilder = new StringBuilder();
        try {
            System.out.println(WfmJpaTemplate.getDataBaseURL());
            coreServiceLocal.exportReportTemplates(templateMap);
            log.info("Imported Templates");
        } catch (Exception e) {
            log.error("Imported Templates", e);
        }
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (final String schema : companyManager.getExistingSchemas()) {
            executor.execute(() -> {
                try {
                    ServerSecurityContext.getInstance().setDatabase(dataBase);
                    coreServiceLocal.exportSavedReport(Integer.valueOf(schema), edsReporList, null, uploadHashMap, uploadSettingsHashMap, null, false);
                    log.info("<<<<<<<<<<<<<<<<<<<<<<DataBase=" + dataBase + " CompanyID=" + schema + " Imported Reports !>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                } catch (Exception e) {
                    stringBuilder.append("|   " + schema + "   ");
                    log.error("************************** DataBase=" + dataBase + " CompanyID=" + schema + " Export Report Migration failed! **********************");
                }
                Thread.yield();
            });
        }
        executor.shutdown();
        System.out.println("Completed proccess " + dataBase);
        resp.getWriter().println(stringBuilder.toString().isEmpty() ? ("Completed proccess " + dataBase) : ("ERROR " + dataBase + " " + stringBuilder.toString()));
    }
}
