package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsProfileIm;

import java.util.List;

public interface ProfileImManager extends Manager<EdsProfileIm> {
    List<EdsProfileIm> getImList();

    List<EdsProfileIm> accountListByImId(Integer imId);
}
