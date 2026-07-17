package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ShippingMethodManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 16, 2010
 * Time: 5:30:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("shippingMethodManager")
public class ShippingMethodManagerImpl extends BaseManager<EdsShippingMethod> implements ShippingMethodManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;


    public ShippingMethodManagerImpl() {
        super(EdsShippingMethod.class);
    }


    public List<EdsShippingMethod> getShippingMethodsByCompanyID(ListingFilterParameter fp) {

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT s ");
        getShippingMethodMyCompanyCount(fp, sql);
        if (fp != null && fp.getSortField() != null) {
            String ascOrDesc = (fp.getSortDir() == 2 ? " desc" : "");
            if ("name".equals(fp.getSortField())) {
                sql.append(" order by s.name" + ascOrDesc);
            } else if ("description".equals(fp.getSortField())) {
                sql.append(" order by s.description" + ascOrDesc);
            } else if ("price".equals(fp.getSortField())) {
                sql.append(" order by s.price" + ascOrDesc);
            }
        } else {
            sql.append(" order by s.objectID desc");
        }
        return find(sql.toString());
    }

    @Override
    public Boolean hasShippingMethod() {
        List<Integer> list = find(" SELECT s.id FROM EdsShippingMethod s where " + ServerUtils.checkForDeleted("s.deleted"));
        return !list.isEmpty();
    }

    private void getShippingMethodMyCompanyCount(ListingFilterParameter fp, StringBuffer sql) {
        sql.append(" FROM EdsShippingMethod s where " + ServerUtils.checkForDeleted("s.deleted"));
        if (fp != null && fp.getSqlSearchKey() != null) {

            sql.append(" and (lower(s.name) like '" + fp.getSqlSearchKey() + "' OR ");
            sql.append(" lower(s.description) like '" + fp.getSqlSearchKey() + "') ");
        }
    }

    @Override
    public Integer listCount(ListingFilterParameter filterParametrs) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select count(s) ");
        getShippingMethodMyCompanyCount(filterParametrs, sql);
        return Integer.parseInt(findSingle(sql.toString()).toString());
    }

    @Override
    public List<EdsShippingMethod> getShippingMethodsByCustomer(ListingFilterParameter filterParameter) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("(select * from  \"" + schema + "\".shippingmethod shm ");
        sql.append("left join \"" + schema + "\".shippingmethodrelation shmr on shmr.shipping_method_id=shm.id ");
        sql.append("where ").append(ServerUtils.checkForDeleted("shm.deleted"));
        if (filterParameter != null && filterParameter.getClientId() != null) {
            sql.append(" and (shmr.customer_id = '" + filterParameter.getClientId() + "' or shmr.customer_id is null) ");
        } else {
            sql.append(" and shmr.customer_id is null ");
        }
        sql.append(" order by shm.id desc)");

        return findNative(sql.toString(), EdsShippingMethod.class);
    }

    public EdsShippingMethod getShippingMethodByName(String name) {
        return (EdsShippingMethod) findSingle("SELECT shm FROM EdsShippingMethod shm where " + ServerUtils.checkForDeleted("shm.deleted") + " and shm.name = '" + name + "'");
    }
}
