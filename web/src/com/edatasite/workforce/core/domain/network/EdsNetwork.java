package com.edatasite.workforce.core.domain.network;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsNews;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Apr 28, 2010
 * Time: 5:53:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "network")
public class EdsNetwork extends EdsObject {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id")
    private Set<EdsNetworkContact> contacts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private EdsUser creator;

    @Column(name = "description", length = 1000)
    private String description;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    /**
     * There are three types of networks:
     * 1) Public (Open Access): Anyone can join to the network without approval of the network creator.
     * 2) Public (Request to Join): Members can join to the network only through requesting to the network creator.
     * 3) Private (Invitation to Join): Invisible to all members, only network creator decides whom to invite to the network, whom not.
     */
    @Column(name = "type")
    private String type;

    @Column(name = "website")
    private String website;

    @Column(name = "isdeleted")
    private Boolean isDeleted = false;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "newsnetworkrelation",
            joinColumns = {@JoinColumn(name = "networkId")},
            inverseJoinColumns = {@JoinColumn(name = "newsId")}
    )
    private Set<EdsNews> news;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public Set<EdsNetworkContact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<EdsNetworkContact> contacts) {
        this.contacts = contacts;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Set<EdsNews> getNews() {
        return news;
    }

    public void setNews(Set<EdsNews> news) {
        this.news = news;
    }

    public Boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
