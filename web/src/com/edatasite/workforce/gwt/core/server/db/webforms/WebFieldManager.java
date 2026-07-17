package com.edatasite.workforce.gwt.core.server.db.webforms;

import com.edatasite.workforce.core.domain.webforms.EdsWebField;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 29, 2010
 * Time: 7:02:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WebFieldManager extends Manager<EdsWebField> {
    List<EdsWebField> getByWebFormID(Integer objectId);
}
