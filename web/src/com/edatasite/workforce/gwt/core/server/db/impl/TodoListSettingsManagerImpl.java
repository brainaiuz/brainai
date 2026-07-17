package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTodoListSettings;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.TodoListSettingsManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 7/20/11
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("todoListSettingsManager")
public class TodoListSettingsManagerImpl extends BaseManager<EdsTodoListSettings> implements TodoListSettingsManager {

    public TodoListSettingsManagerImpl() {
        super(EdsTodoListSettings.class);
    }

    public EdsTodoListSettings getTodoListSettings(EdsUser user) {
        EdsTodoListSettings todoListSettings = (EdsTodoListSettings) findSingle("select ts from EdsTodoListSettings ts where ts.user = ?", user);
        if (todoListSettings == null || todoListSettings.isNew()) {
            todoListSettings = new EdsTodoListSettings();
            todoListSettings.setUser(user);
            create(todoListSettings);
        }
        return todoListSettings;
    }

    public EdsTodoListSettings getByUser(EdsUser user) {
        return (EdsTodoListSettings) findSingle("select ts from EdsTodoListSettings ts where ts.user = ?", user);
    }

}
