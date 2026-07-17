package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTaxComponent;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 09.06.2010
 * Time: 11:40:21
 * To change this template use File | Settings | File Templates.
 */
public interface TaxComponentManager extends Manager<EdsTaxComponent> {
    List<EdsTaxComponent> getTaxComponents(Integer taxID);

    void deleteTaxComponents(Integer taxID);
}
