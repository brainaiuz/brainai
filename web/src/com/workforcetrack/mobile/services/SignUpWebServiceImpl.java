package com.workforcetrack.mobile.services;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.signup.client.rpc.CreatedCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.SignUpService;
import com.workforcetrack.mobile.rpc.base.WebServiceConstants;
import com.workforcetrack.mobile.rpc.contact.MCountryList;
import com.workforcetrack.mobile.rpc.signup.MCreatedCompany;
import com.workforcetrack.mobile.rpc.signup.MNewCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/12/11
 * Time: 11:30 AM
 */
@Service("signUpWebService")
public class SignUpWebServiceImpl implements SignUpWebService {

    @Autowired
    private SignUpService signUpService;
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private CommonServiceLocal commonServiceLocal;

    @Override
    public MCreatedCompany createCompany(MNewCompany mNewCompany) {
        if (mNewCompany == null || mNewCompany.getAdminEmail() == null || "".equals(mNewCompany.getAdminEmail())
                || mNewCompany.getName() == null || "".equals(mNewCompany.getName())
                || mNewCompany.getAdminFName() == null || "".equals(mNewCompany.getAdminFName())
                || mNewCompany.getAdminLName() == null || "".equals(mNewCompany.getAdminLName())
                || mNewCompany.getCountryID() == null || "".equals(mNewCompany.getCountryID()) || mNewCompany.getCountryID() == 0) {
            return null;
        }

        MCreatedCompany mCreatedCompany = null;
        NewCompany newCompany = new NewCompany();

        Boolean convertResult = MNewCompany.convert(mNewCompany, newCompany, true);
        if (convertResult != null && convertResult) {
            SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
            newCompany.setHost(mNewCompany.getHost() != null && !"".equals(mNewCompany.getHost()) ? mNewCompany.getHost() : WebServiceConstants.HOST_LIVE_KPI);
            newCompany.setLocale(Locale.ENGLISH.getLanguage());
            CreatedCompany comID = signUpService.createCompany(newCompany);
            if (comID != null && comID.getCompanyId() != null) {
                SecurityContext.getInstance().setDatabase(Constants.DATABASE_PAID);
                crmServiceLocal.createLeadFromSignUpper(NewCompany.toString(newCompany));
            }
            SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
            mCreatedCompany = new MCreatedCompany(comID);
        }

        return mCreatedCompany;

    }

    @Override
    public MCountryList getCountries() {
        SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        SelectItem[] countries = signUpService.getCountries();
        return new MCountryList(countries);
    }

    public String getWFTPlugin(String pluginName) {
        SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        return commonServiceLocal.getWFTPlugin(pluginName);
    }

}
