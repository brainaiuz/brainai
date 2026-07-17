package com.edatasite.workforce.rest.v3.release10.core.to;

public class QuizScoreDto {
    private String name;
    private Double score;
    private LocaleDto locale;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public LocaleDto getLocale() {
        return locale;
    }

    public void setLocale(LocaleDto locale) {
        this.locale = locale;
    }
}
