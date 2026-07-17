package com.edatasite.workforce.gwt.core.server.db.impl.benefit;

import com.edatasite.workforce.core.domain.EdsBenefit;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.benefit.BenefitManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.profile.client.rpc.BenefitItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Aziz on 11.09.14.
 */
@Repository("BenefitManager")
public class BenefitManagerImpl extends BaseManager<EdsBenefit> implements BenefitManager {
    public BenefitManagerImpl() {
        super(EdsBenefit.class);
    }

//    public static final String _BENEFIT_TYPE = "_BENEFIT_TYPE";
//    public static final String _BENEFIT_QTYTYPE = "_BENEFIT_QTYTYPE";
//    public static final String CASH = "CASH";
//    public static final String NON_CASH = "NON_CASH";
//    public static final String CASH_AND_NON_CASH = "CASH_AND_NON_CASH";

    @Override
    public List<EdsBenefit> getBenefitList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder("select b from EdsBenefit b ");
        sql.append("LEFT JOIN b.type type ");
        sql.append("LEFT JOIN b.qtytype qtytype ");
        sql.append("LEFT JOIN b.currency currency ");
        sql.append("WHERE (b.deleted is null or b.deleted is not true) ");

        if (StringUtils.isNotBlank(fp.getSearchKey())) {
            sql.append(" AND (lower(b.name) like '").append(fp.getSqlSearchKey()).append("') ");
        }
        if (fp.isActive()) {
            sql.append(" AND b.isActive is true ");
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (fp.setPlannedDue()) {
            sql.append(" AND (b.expireDate is null OR to_char(b.expireDate,'yyyy-MM-dd') >= '").append(format.format(ServerUtils.getEndDate(new Date()))).append("')");
        } else if (fp.getNonConvertibleStartDate() != null) {
            sql.append(" AND (b.expireDate is null OR to_char(b.expireDate,'yyyy-MM-dd') >= '").append(format.format(ServerUtils.getEndDate(fp.getNonConvertibleStartDate()))).append("')");
        }

        sql.append(" order by ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (BenefitItem.NAME.equals(fp.getSortField())) {
                sql.append("b.name");
            } else if (BenefitItem.TYPE.equals(fp.getSortField())) {
                sql.append("type.name");
            } else if (BenefitItem.CURRENCY.equals(fp.getSortField())) {
                sql.append("currency.name");
            } else if (BenefitItem.EXPIRE_DATE.equals(fp.getSortField())) {
                sql.append("b.expireDate");
            } else if (BenefitItem.QTYTYPE.equals(fp.getSortField())) {
                sql.append("qtytype.name");
            } else if (BenefitItem.TRANSFERRABLE.equals(fp.getSortField())) {
                sql.append("b.transferrable");
            } else {
                sql.append(" b.name");
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" asc");
                } else {
                    sql.append(" desc");
                }
            } else {
                sql.append(" desc");
            }
        } else {
            sql.append(" b.lastUpdateTime desc ");
        }
        if (fp.getLimit() == 0) {
            return find(sql.toString());
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getBenefitTotalCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(b.objectID) FROM EdsBenefit b ");
        sql.append("WHERE (b.deleted is null or b.deleted is not true) ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(b.name) like '").append(fp.getSqlSearchKey()).append("') ");
        }
        if (fp.isActive()) {
            sql.append(" AND b.isActive is true ");
        }

        if (fp.setPlannedDue()) {
            sql.append(" AND (b.expireDate is null OR date(b.expireDate) > date('" + new Date() + "')) ");
        }
        Long count = (Long) findSingle(sql.toString());
        return count != null ? count.intValue() : 0;
    }

    @Override
    public boolean hasBenefitRequest(Integer benefitID) {
        StringBuilder sql = new StringBuilder("select count(br.id) from ").append(getCompanyId()).append(".benefitRequest br ");
        sql.append("left join ").append(getCompanyId()).append(".benefit b on (br.benefit_id = b.id) ");
        sql.append("where b.id = ").append(benefitID);
        sql.append(" and (br.deleted<>true or br.deleted is null) ");
        BigInteger total = (BigInteger) findNativeSingle(sql.toString());
        return total.intValue() > 0;
    }
}
