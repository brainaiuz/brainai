package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPensionScheme;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PensionSchemeManager;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 10, 2009
 * Time: 7:08:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("pensionSchemeManager")
public class PensionSchemeManagerImpl extends BaseManager<EdsPensionScheme> implements PensionSchemeManager {

    public PensionSchemeManagerImpl() {
        super(EdsPensionScheme.class);
    }

    public List<EdsPensionScheme> getCompanyPensionSchemes() {
        return find("select ps from EdsPensionScheme ps WHERE ps.deleted<>true or ps.deleted is null"/*, getUser().getCompany()*/);
    }

    public List<EdsPensionScheme> getPensionSchemes(ListingFilterParameter fp) {
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT ps FROM EdsPensionScheme ps WHERE ps.deleted<>true or ps.deleted is null ");
        if (fp.getSqlSearchKey() != null) {
            sql.append("and (lower(ps.name) like '" + fp.getSqlSearchKey() + "'");
            sql.append(" or lower(ps.type.name) like '" + fp.getSqlSearchKey() + "')");
        }
        if (fp.getSortField() != null && fp.getSortField() != "") {
            sql.append("ORDER BY ");
            if (fp.getSortField().equals("name")) {
                sql.append("ps.name ");
            }
            if (fp.getSortField().equals("type")) {
                sql.append("ps.type.name ");
            }
            if (fp.getSortField().equals("employeeContribution")) {
                sql.append("ps.deductionValue ");
            }
            if (fp.getSortField().equals("deductionFrom")) {
                sql.append("ps.deductFrom ");
            }
            if (fp.getSortField().equals("allowTaxRelief")) {
                sql.append("ps.allowTaxRelief ");
            }
            if (fp.getSortField().equals("reduceByBRT")) {
                sql.append("ps.reduceByBasicRateTax ");
            }
            if (fp.getSortField().equals("wagesInsufficient")) {
                sql.append("ps.wagesInsufficient ");
            }
            if (fp.getSortField().equals("employerContribution")) {
                sql.append("ps.employerDeductionValue ");
            }

            if (!fp.isAscending()) {
                sql.append("desc");
            }
        } else {
            sql.append("ORDER BY  ps.name asc");
        }
        return find(sql.toString());
    }

    @Override
    public EdsPensionScheme getPensionSchema(String countryCode) {
        EdsPensionScheme schema = (EdsPensionScheme) findNativeSingle("select ps.* from " + getCompanyId() + ".pensionscheme ps where ps.countrycode is null order by id  desc ", EdsPensionScheme.class);
        if (schema == null && !countryCode.isEmpty()) {
            schema = (EdsPensionScheme) findNativeSingle("select ps.* from " + getCompanyId() + ".pensionscheme ps  where ps.countrycode='" + countryCode + "' order by id  desc ", EdsPensionScheme.class);
        }
        return schema;
    }

    public void deletePensionScheme(Integer id) {
        update("update EdsPensionScheme ps set ps.deleted=true where ps.objectID=" + id);
    }

    public boolean isDeductedFromGrossPay(String pensionSchemes) {
        if (pensionSchemes != null && !"".equals(pensionSchemes.trim()) && pensionSchemes.split(",").length > 0) {
            return ((BigInteger) findNativeSingle("SELECT count(*) FROM " + getCompanyId() + ".pensionscheme WHERE deleted<>true or deleted is null and  id IN (" + pensionSchemes + ") AND deductFrom = " + DEDUCT_FROM__GROSS_PAY + " ")).intValue() > 0;
        }
        return false;
    }

    public boolean isDeductedFromNetPay(String pensionSchemes) {
        if (pensionSchemes != null && !"".equals(pensionSchemes.trim()) && pensionSchemes.split(",").length > 0) {
            return ((Number) findSingle("SELECT count(ps) FROM EdsPensionScheme ps WHERE deleted<>true or deleted is null and ps.objectID IN (" + pensionSchemes + ") AND ps.deductFrom = " + DEDUCT_FROM_NET_PAY + " ")).intValue() > 0;
        }
        return false;
    }

    public final static int DEDUCT_FROM__GROSS_PAY = 0;
    public final static int DEDUCT_FROM_NET_PAY = 1;
}
