package com.edatasite.workforce.gwt.core.server.db.impl.webforms;

import com.edatasite.workforce.core.domain.webforms.EdsWebField;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.webforms.WebFieldManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jul 29, 2010
 * Time: 7:05:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("webFieldManager")
public class WebFieldManagerImpl extends BaseManager<EdsWebField> implements WebFieldManager {
    public WebFieldManagerImpl() {
        super(EdsWebField.class);
    }

    @Override
    public List<EdsWebField> getByWebFormID(Integer objectId) {
        return (List<EdsWebField>) find("select field from EdsWebField field where field.webForm.objectID = " + objectId);
    }
}
