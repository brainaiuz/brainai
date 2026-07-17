package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPensionProvider;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PensionProviderManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 10, 2009
 * Time: 7:34:51 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("pensionProviderManager")
public class PensionProviderManagerImpl extends BaseManager<EdsPensionProvider> implements PensionProviderManager {
    public PensionProviderManagerImpl() {
        super(EdsPensionProvider.class);
    }

    public List<EdsPensionProvider> getCompanyPensionProviders() {
        return find("select pp from EdsPensionProvider pp where " + ServerUtils.checkForDeleted("pp.deleted"));
    }

    public Integer getPensionProviderSize() {
        return find("select pp from EdsPensionProvider pp where " + ServerUtils.checkForDeleted("pp.deleted")).size();
    }

    @Override
    public List<EdsPensionProvider> getPensionProviders(ListingFilterParameter lfp) {
        return findInterval("select pp from EdsPensionProvider pp  where " + ServerUtils.checkForDeleted("pp.deleted"), lfp.getStart(), lfp.getLimit());
    }
}
