package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 5:23:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "newscategory")
public class EdsNewsCategory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "newscategoryrelation",
            joinColumns = {@JoinColumn(name = "categoryId")},
            inverseJoinColumns = {@JoinColumn(name = "newsId")}
    )
    private List<EdsNews> news;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsNewsCategory parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Where(clause = "deleted = 'false'")
    private List<EdsNewsCategory> children;

    private Boolean deleted = false;

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

    public List<EdsNews> getNews() {
        return news;
    }

    public void setNews(List<EdsNews> news) {
        this.news = news;
    }

    public EdsNewsCategory getParent() {
        return parent;
    }

    public void setParent(EdsNewsCategory parent) {
        this.parent = parent;
    }

    public List<EdsNewsCategory> getChildren() {
        return children;
    }

    public void setChildren(List<EdsNewsCategory> children) {
        this.children = children;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
