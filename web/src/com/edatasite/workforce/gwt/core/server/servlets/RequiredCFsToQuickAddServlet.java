package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddSettingsForm;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class RequiredCFsToQuickAddServlet implements HttpRequestHandler {
    @Autowired
    private CompanyCustomFieldsManager companyCustomFieldsManager;
    @Autowired
    private ProfileServiceLocal profileServiceLocal;

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String schemaType = request.getParameter("schema");
        if (schemaType.equals("free")) {
            ServerSecurityContext.getInstance().setDatabase(TenantContextHolder.FREE_DB);
        } else if (schemaType.equals("paid")) {
            ServerSecurityContext.getInstance().setDatabase(TenantContextHolder.PAID_DB);
        }
        CompanyManager companyManager = (CompanyManager) ApplicationContextProvider.applicationContext.getBean("companyManager");
        List<EdsCompany> companies = companyManager.getCompanies();
        for (EdsCompany company : companies) {
            ServerSecurityContext.getInstance().setCompanyId(company.getObjectID());
            List<EdsCompanyCustomFieldsSettings> cfs = companyCustomFieldsManager.getCustomFieldsForQuickAdd(ViewName.Task.name());
            if (cfs != null && !cfs.isEmpty()) {
                for (EdsCompanyCustomFieldsSettings cf : cfs) {
                    profileServiceLocal.addOrRemoveCFFromQuickAdd(QuickAddSettingsForm.TASK, cf.getFieldName(), cf.getColumnCode(), true);
                }
            }
        }
    }
}
