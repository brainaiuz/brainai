package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsIm;
import com.edatasite.workforce.core.domain.EdsProfileIm;

import java.util.List;

public interface ImManager extends Manager<EdsIm> {
    List<EdsIm> imList();

    List<EdsProfileIm> getImList();
}
