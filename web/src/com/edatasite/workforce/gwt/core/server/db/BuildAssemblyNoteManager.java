package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBuildAssemblyNote;

import java.util.List;

public interface BuildAssemblyNoteManager extends Manager<EdsBuildAssemblyNote> {

    List<EdsBuildAssemblyNote> getComments(Integer buildAssemblyId);

}
