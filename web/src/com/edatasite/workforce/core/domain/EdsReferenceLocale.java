package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;

import javax.persistence.*;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "reference_locale")
public class EdsReferenceLocale extends EdsObject {
    @Override
    public Integer getObjectID() {
        return objectID;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(length = 1000)
    private String english;

    @Column(length = 1000)
    private String russian;

    @Column(length = 1000)
    private String arabic;

    @Column(length = 1000)
    private String uzbek;

    public ReferenceLocale toRPC() {
        ReferenceLocale referenceLocale = new ReferenceLocale();

        referenceLocale.setObjectId(getObjectID());
        referenceLocale.setEnglish(getEnglish());
        referenceLocale.setRussian(getRussian());
        referenceLocale.setArabic(getArabic());
        referenceLocale.setUzbek(getUzbek());

        return referenceLocale;
    }

    public String getLocaleByCode(String code) {
        return switch (code) {
            case "en" -> getEnglish();
            case "ru" -> getRussian();
            case "ar" -> getArabic();
            case "uz" -> getUzbek();
            default -> null;
        };
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getRussian() {
        return russian;
    }

    public void setRussian(String russian) {
        this.russian = russian;
    }

    public String getArabic() {
        return arabic;
    }

    public void setArabic(String arabic) {
        this.arabic = arabic;
    }

    public String getUzbek() {
        return uzbek;
    }

    public void setUzbek(String uzbek) {
        this.uzbek = uzbek;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdsReferenceLocale)) return false;
        if (!super.equals(o)) return false;

        EdsReferenceLocale that = (EdsReferenceLocale) o;

        if (getObjectID() != null ? !getObjectID().equals(that.getObjectID()) : that.getObjectID() != null)
            return false;
        if (getEnglish() != null ? !getEnglish().equals(that.getEnglish()) : that.getEnglish() != null) return false;
        if (getRussian() != null ? !getRussian().equals(that.getRussian()) : that.getRussian() != null) return false;
        if (getArabic() != null ? !getArabic().equals(that.getArabic()) : that.getArabic() != null) return false;
        if (getUzbek() != null ? !getUzbek().equals(that.getUzbek()) : that.getUzbek() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getObjectID() != null ? getObjectID().hashCode() : 0);
        result = 31 * result + (getEnglish() != null ? getEnglish().hashCode() : 0);
        result = 31 * result + (getRussian() != null ? getRussian().hashCode() : 0);
        result = 31 * result + (getArabic() != null ? getArabic().hashCode() : 0);
        result = 31 * result + (getUzbek() != null ? getUzbek().hashCode() : 0);
        return result;
    }
}
