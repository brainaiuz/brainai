package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsBuildAssemblyNote;
import com.edatasite.workforce.gwt.core.server.db.BuildAssemblyNoteManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("buildAssemblyNoteManager")
public class BuildAssemblyNoteManagerImpl extends BaseManager<EdsBuildAssemblyNote> implements BuildAssemblyNoteManager {

    public BuildAssemblyNoteManagerImpl() {
        super(EdsBuildAssemblyNote.class);
    }

    @Override
    public List<EdsBuildAssemblyNote> getComments(Integer buildAssemblyId) {
        return find("select ban from EdsBuildAssemblyNote ban where ban.buildassembly.objectID = " + buildAssemblyId);
    }
}
