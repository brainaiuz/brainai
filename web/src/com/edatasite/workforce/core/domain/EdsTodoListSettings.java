package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 7/20/11
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "todoListSettings")
public class EdsTodoListSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;//todoListSettings ID;

    @Column(name = "isShowDetails")
    private Boolean isShowDetails = true;//todoList details shown/hidden option;

    @Column(name = "isShowTodoList")
    private Boolean isShowTodoList = false;//in workspace todoList shown/hidden option;

    @Column(name = "theme")
    private String todoListTheme;//Color schema for To Do List;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;//join to user ID

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getShowDetails() {
        return isShowDetails;
    }

    public void setShowDetails(Boolean showDetails) {
        isShowDetails = showDetails;
    }

    public Boolean getShowTodoList() {
        return isShowTodoList;
    }

    public void setShowTodoList(Boolean showTodoList) {
        isShowTodoList = showTodoList;
    }

    public String getTodoListTheme() {
        return todoListTheme;
    }

    public void setTodoListTheme(String todoListTheme) {
        this.todoListTheme = todoListTheme;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }
}
