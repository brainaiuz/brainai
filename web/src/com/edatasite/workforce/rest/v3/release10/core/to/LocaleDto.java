package com.edatasite.workforce.rest.v3.release10.core.to;

public class LocaleDto {
    private String russian;
    private String english;
    private String uzbek;
    private String arabian;

    public LocaleDto() {
    }

    public LocaleDto(String russian, String english, String uzbek, String arabian) {
        this.russian = russian;
        this.english = english;
        this.uzbek = uzbek;
        this.arabian = arabian;
    }

    public String getRussian() {
        return russian;
    }

    public void setRussian(String russian) {
        this.russian = russian;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getUzbek() {
        return uzbek;
    }

    public void setUzbek(String uzbek) {
        this.uzbek = uzbek;
    }

    public String getArabian() {
        return arabian;
    }

    public void setArabian(String arabian) {
        this.arabian = arabian;
    }
}
