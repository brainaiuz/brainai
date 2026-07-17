package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsReferenceColor;

/**
 * User: Dilsh0d Madrahimov
 * Date: Jan 14, 2008 Time: 3:58:03 PM
 */

public interface ReferenceColorManager extends Manager<EdsReferenceColor> {

    EdsReferenceColor getByHex(String colorHex);
}
