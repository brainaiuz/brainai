package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Feb 11, 2010
 * Time: 8:47:51 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "chatroom")
public class EdsChatRoom extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "expert")
    private Boolean expert = false;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category")
    private Integer category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;// this is creator user or expert user

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taskid")
    private EdsTask task;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roomPhotoId")
    private EdsUpload roomPhoto;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroomid")
    private Set<EdsChatRoomAssignee> chatRoomAssignee;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isExpert() {
        return expert;
    }

    public void setExpert(Boolean expert) {
        this.expert = expert;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsCompany getCompany() {
        return getUser().getCompany();
    }

    public EdsTask getTask() {
        return task;
    }

    public void setTask(EdsTask task) {
        this.task = task;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsUpload getRoomPhoto() {
        return roomPhoto;
    }

    public void setRoomPhoto(EdsUpload roomPhoto) {
        this.roomPhoto = roomPhoto;
    }

    public Set<EdsChatRoomAssignee> getChatRoomAssignee() {
        Set<EdsChatRoomAssignee> removeChatRoomAssigne = new HashSet<>();
        for (EdsChatRoomAssignee roomAssignee : chatRoomAssignee) {
            if (roomAssignee.getUser().getDeleted()) {
                removeChatRoomAssigne.add(roomAssignee);
            }
        }
        chatRoomAssignee.removeAll(removeChatRoomAssigne);
        return chatRoomAssignee;
    }

    public void setChatRoomAssignee(Set<EdsChatRoomAssignee> chatRoomAssignee) {
        this.chatRoomAssignee = chatRoomAssignee;
    }
}
