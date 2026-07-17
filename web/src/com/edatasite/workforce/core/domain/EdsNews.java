package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.network.EdsNetwork;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.news.client.rpc.NewsSolr;
import com.edatasite.workforce.gwt.news.client.rpc.UserSolr;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 5:00:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "news")
public class EdsNews extends EdsObject implements ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String subject;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "newscategoryrelation",
            joinColumns = {@JoinColumn(name = "newsId")},
            inverseJoinColumns = {@JoinColumn(name = "categoryId")}
    )
    private List<EdsNewsCategory> categories;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "newssupplierrelation",
            joinColumns = {@JoinColumn(name = "newsId")},
            inverseJoinColumns = {@JoinColumn(name = "supplierId")}
    )
    private Set<EdsCrmAccount> suppliers;

    @Column(name = "shortdescription", length = 10000)
    private String shortDescription;

    private Date date;

    @Column(name = "fulltext")
    @Type(type = "text")
    private String fullText;

    private String seo;

    private Boolean isTop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image")
    private EdsAttachment image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file")
    private EdsAttachment file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    private String owner;

    private Boolean isBlog;

    @Column(name = "isgeneralnews")
    private Boolean isGeneralNews = false;

    @Column(name = "isdisclosedname")
    private Boolean isDisclosedName;

    private Boolean pressRelease;

    private Boolean postRelease;

    private Boolean networkNews;

    private Boolean toAllNetworks;

    private Boolean showHomePage;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "newsnetworkrelation",
            joinColumns = {@JoinColumn(name = "newsId")},
            inverseJoinColumns = {@JoinColumn(name = "networkId")}
    )
    private Set<EdsNetwork> networks;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "visibility")
    private Boolean visibility = false;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "newsid")
    private Set<EdsNewsComment> views = new HashSet<>();

    @Column(name = "creationTime")
    private Date creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationID")
    private EdsLocation location;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;


    private String imageUrl;

    private Boolean isAnonym;

    private Boolean isPressRelease;

    private Boolean isNews;

    private Boolean isTopNews;

    private Boolean isFeatures;

    private Boolean isOpinion;

    private Boolean isWhitePaper;

    private Boolean isSponsoredArticle;

    private Boolean isEventArchive;

    private String author;

    public Boolean isTopNews() {
        return isTopNews;
    }

    public void setTopNews(Boolean topNews) {
        isTopNews = topNews;
    }

    public Boolean getEventArchive() {
        return isEventArchive;
    }

    public void setEventArchive(Boolean eventArchive) {
        isEventArchive = eventArchive;
    }

    public Boolean getSponsoredArticle() {
        return isSponsoredArticle;
    }

    public void setSponsoredArticle(Boolean sponsoredArticle) {
        isSponsoredArticle = sponsoredArticle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean isOpinion() {
        return isOpinion;
    }

    public void setIsOpinion(Boolean opinion) {
        isOpinion = opinion;
    }

    public Boolean isWhitePaper() {
        return isWhitePaper;
    }

    public void setIsWhitePaper(Boolean whitePaper) {
        isWhitePaper = whitePaper;
    }

    public Boolean isPressRelease() {
        return isPressRelease;
    }

    public void setIsPressRelease(Boolean pressRelease) {
        isPressRelease = pressRelease;
    }

    public Boolean isNews() {
        return isNews;
    }

    public void setNews(Boolean news) {
        isNews = news;
    }

    public Boolean isFeatures() {
        return isFeatures;
    }

    public void setFeatures(Boolean features) {
        isFeatures = features;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<EdsNewsCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<EdsNewsCategory> categories) {
        this.categories = categories;
    }

    public Set<EdsCrmAccount> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Set<EdsCrmAccount> suppliers) {
        this.suppliers = suppliers;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getSeo() {
        return seo;
    }

    public void setSeo(String seo) {
        this.seo = seo;
    }

    public Boolean getTop() {
        return isTop != null ? isTop : false;
    }

    public void setTop(Boolean top) {
        isTop = top != null ? top : false;
    }

    public EdsAttachment getImage() {
        return image;
    }

    public void setImage(EdsAttachment image) {
        this.image = image;
    }

    public EdsAttachment getFile() {
        return file;
    }

    public void setFile(EdsAttachment file) {
        this.file = file;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getBlog() {
        return isBlog;
    }

    public void setBlog(Boolean blog) {
        isBlog = blog;
    }

    public Boolean getDisclosedName() {
        return isDisclosedName;
    }

    public void setDisclosedName(Boolean disclosedName) {
        isDisclosedName = disclosedName;
    }

    public Boolean getPressRelease() {
        return pressRelease;
    }

    public void setPressRelease(Boolean pressRelease) {
        this.pressRelease = pressRelease;
    }

    public Boolean getPostRelease() {
        return postRelease;
    }

    public void setPostRelease(Boolean postRelease) {
        this.postRelease = postRelease;
    }

    public Boolean getNetworkNews() {
        return networkNews != null ? networkNews : true;
    }

    public void setNetworkNews(Boolean networkNews) {
        this.networkNews = networkNews != null ? networkNews : true;
    }

    public Boolean getToAllNetworks() {
        return toAllNetworks != null ? toAllNetworks : false;
    }

    public void setToAllNetworks(Boolean toAllNetworks) {
        this.toAllNetworks = toAllNetworks != null ? toAllNetworks : false;
    }

    public Set<EdsNetwork> getNetworks() {
        return networks;
    }

    public void setNetworks(Set<EdsNetwork> networks) {
        this.networks = networks;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public Set<EdsNewsComment> getViews() {
        return views;
    }

    public void setViews(Set<EdsNewsComment> views) {
        this.views = views;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getAnonym() {
        return isAnonym != null ? isAnonym : false;
    }

    public void setAnonym(Boolean anonym) {
        isAnonym = anonym != null ? anonym : false;
    }

    public Boolean isGeneralNews() {
        return isGeneralNews;
    }

    public void setGeneralNews(Boolean generalNews) {
        isGeneralNews = generalNews;
    }

    public Boolean getShowHomePage() {
        return showHomePage;
    }

    public void setShowHomePage(Boolean showHomePage) {
        this.showHomePage = showHomePage;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public void setUpdater(EdsUser user) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Date getCreationTime() {
        return creationTime;
    }

    @Override
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public void setCreator(EdsUser value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public NewsSolr getRPC() {
        NewsSolr rpc = new NewsSolr();
        rpc.setId(getObjectID());
        rpc.setSubject(getSubject());
        rpc.setFullText(getFullText());
        rpc.setDate(getDate());
        rpc.setCreationTime(getCreationTime());
        rpc.setAnonym(getAnonym());
        Optional.ofNullable(getUser())
                .map(u -> {
                    UserSolr si = new UserSolr();
                    si.setId(u.getObjectID());
                    si.setFullname(u.getFullName());
                    Optional.ofNullable(u.getCompany())
                            .map(EdsCompany::getObjectID)
                            .ifPresent(si::setCompanyId);
                    return si;
                })
                .ifPresent(rpc::setUser);
        rpc.setVisible(getVisibility());
        rpc.setGeneralNews(isGeneralNews());
        rpc.setBlog(getBlog());
        Optional.ofNullable(getLocation())
                .map(l -> new SelectItem(l.getObjectID(), l.getName()))
                .ifPresent(rpc::setLocation);
        rpc.setOwnerName(getOwner());
        rpc.setDeleted(getDeleted());
        List<SelectItem> categories = Optional.ofNullable(getCategories()).orElse(List.of())
                .stream()
                .map(c -> new SelectItem(c.getObjectID(), c.getName()))
                .toList();
        rpc.setCategories(categories);
        rpc.setViewCount(getViews() != null ? getViews().size() : 0);
        return rpc;
    }
}
