package com.edatasite.workforce.rest.v3.release10.hrms.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.IdName;

public class LanguagesDto {
    private IdName language;
    private IdName level;

    public LanguagesDto() {
    }

    public LanguagesDto(IdName language, IdName level) {
        this.language = language;
        this.level = level;
    }

    public IdName getLanguage() {
        return language;
    }

    public void setLanguage(IdName language) {
        this.language = language;
    }

    public IdName getLevel() {
        return level;
    }

    public void setLevel(IdName level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LanguagesDto)) return false;

        LanguagesDto that = (LanguagesDto) o;

        if (getLanguage() != null ? !getLanguage().equals(that.getLanguage()) : that.getLanguage() != null)
            return false;
        if (getLevel() != null ? !getLevel().equals(that.getLevel()) : that.getLevel() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getLanguage() != null ? getLanguage().hashCode() : 0;
        result = 31 * result + (getLevel() != null ? getLevel().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LanguagesDto{" +
                "language=" + language +
                ", level=" + level +
                '}';
    }
}
