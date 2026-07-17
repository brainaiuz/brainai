package com.edatasite.workforce.gwt.core.server.db.rbac;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsProjectIndexRbac;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Oct 10, 2009
 * Time: 9:38:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectIndexRbacManager extends Manager<EdsProjectIndexRbac> {
    EdsProjectIndexRbac createProjectIndex(EdsProject project, EdsUser user, int permissions);

    EdsProjectIndexRbac updateProjectIndex(EdsProject project, EdsUser user, int permissions);

    EdsProjectIndexRbac getProjectIndex(EdsProject project, EdsUser user);

    void removeProjectIndex(EdsProject project);

    void indexProject(EdsProject project);

    List<EdsProject> list(ListingFilterParameter fp);

    List<EdsProjectIndexRbac> getCompanyProjectIndex();

    List<EdsProjectIndexRbac> getProjectIndexes(EdsProject project);

    List<EdsProjectIndexRbac> getProjectIndexesById(EdsUser user, String projectIds);

    List<EdsProjectIndexRbac> getProjectIndexesByIdForAdminAndDir(String projectIds);

    List<Integer> getProjectOwners(EdsProject project);

    void removeCompanyRelatedRbacEntries();
}
