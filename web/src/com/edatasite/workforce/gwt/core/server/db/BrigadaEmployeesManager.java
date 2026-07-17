package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBrigada;
import com.edatasite.workforce.core.domain.EdsBrigadaEmployee;
import com.edatasite.workforce.core.domain.EdsEmployee;

import java.util.HashMap;
import java.util.List;

public interface BrigadaEmployeesManager extends Manager<EdsBrigadaEmployee> {

    List<EdsBrigadaEmployee> getBrigadaEmployees(EdsBrigada project);

    List<Integer> getBrigadaEmployees(Integer brigadaId);

    List<Integer> getBrigadaEmployees(String brigadaIds);

    void deleteBrigadaInPE(EdsBrigada project);

    HashMap<Integer, EdsBrigadaEmployee> getBrigadaEmployeesAsMap(EdsBrigada project);

    EdsBrigadaEmployee getBrigadaEmployee(EdsEmployee employee, EdsBrigada edsProject);
}
