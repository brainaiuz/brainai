package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTodoListSettings;
import com.edatasite.workforce.core.domain.EdsUser;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 7/20/11
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TodoListSettingsManager extends Manager<EdsTodoListSettings> {

    EdsTodoListSettings getTodoListSettings(EdsUser user);

    EdsTodoListSettings getByUser(EdsUser user);
}
