package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsFormula;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.FormulaManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 2011-07-19
 * Time: 8:03 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("formulaManager")
public class FormulaManagerImpl extends BaseManager<EdsFormula> implements FormulaManager {

    public FormulaManagerImpl() {
        super(EdsFormula.class);
    }
}
