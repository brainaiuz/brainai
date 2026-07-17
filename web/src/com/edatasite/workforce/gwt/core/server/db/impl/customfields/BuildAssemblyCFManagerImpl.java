package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsBuildAssemblyCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.BuildAssemblyCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("buildAssemblyCFManager")
public class BuildAssemblyCFManagerImpl extends BaseManager<EdsBuildAssemblyCustomFields> implements BuildAssemblyCFManager {
    public BuildAssemblyCFManagerImpl() {
        super(EdsBuildAssemblyCustomFields.class);
    }

}

