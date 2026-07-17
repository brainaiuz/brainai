package com.edatasite.workforce.gwt.project.server.app;

import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 25.06.12
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectCircularResolverService {
    HashSet<String> getProjectSpecificPermissions(Integer projectID);
}
