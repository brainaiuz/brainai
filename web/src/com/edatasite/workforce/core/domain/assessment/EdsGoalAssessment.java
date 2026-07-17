package com.edatasite.workforce.core.domain.assessment;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 19.06.2007
 * Time: 10:10:10
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "goalAssessment")
public class EdsGoalAssessment extends EdsObject {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer id) {
        this.objectID = id;
    }

    private Integer averageType = 1; // 1 - Computed average, 2 - Custom average
    private Integer customAverage;
    private Integer computedAverage;  // Transient field not saved into database!
    private String comments;
    private Boolean completed = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessmentId")
    private EdsEmployeeAssessment employeeAssessment;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "goalRatingId")
    private List<EdsGoalRating> ratings = new ArrayList<>();

    public EdsEmployeeAssessment getAssessment() {
        return employeeAssessment;
    }

    public void setAssessment(EdsEmployeeAssessment employeeAssessment) {
        this.employeeAssessment = employeeAssessment;
        if (!this.equals(employeeAssessment.getSkillAssessment())) {
            employeeAssessment.setGoalAssessment(this);
        }
    }

    public Integer getAverageType() {
        return averageType;
    }

    public void setAverageType(Integer averageType) {
        this.averageType = averageType;
    }

    public Integer getCustomAverage() {
        return customAverage;
    }

    public void setCustomAverage(Integer customAverage) {
        this.customAverage = customAverage;
    }

    public Integer getComputedAverage() {
        return computedAverage;
    }

    public void setComputedAverage(Integer computedAverage) {
        this.computedAverage = computedAverage;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }


    public List<EdsGoalRating> getRatings() {
        return ratings;
    }

    public void setRatings(List<EdsGoalRating> ratings) {
        this.ratings = ratings;
    }

    public void addGoalRating(EdsGoalRating goalRating) {
        goalRating.setGoalAssessment(this);
        getRatings().add(goalRating);
    }

    public Integer getAverage() {
        if (getRatings().isEmpty()) {
            return null;
        }
        int result = 0;
        int divider = 0;
        for (EdsGoalRating rating : getRatings()) {
            if (rating.getShowSlider() != null && !rating.getShowSlider()) {
                continue;
            }
            result += (rating.getRating() * rating.getWeight());
            divider += rating.getWeight();
        }
        return result / divider;
    }

    public Integer getAverageRate() {
        if (getRatings().isEmpty()) {
            return null;
        }
        int result = 0;
        int divider = 0;
        for (EdsGoalRating rating : getRatings()) {
            if (rating.getShowSlider() != null && !rating.getShowSlider()) {
                continue;
            }
            if (rating.getRating() != null && rating.getWeight() != null) {
                result += (rating.getRating() * rating.getWeight());
                divider += rating.getWeight();
            }
        }
        return divider != 0 ? result / divider : null;
    }

    public Double getCalculatedAverageRate() {
        if (getRatings().isEmpty()) {
            return null;
        }
        Double result = 0d;
        int divider = 0;
        for (EdsGoalRating rating : getRatings()) {
            if (rating.getShowSlider() != null && !rating.getShowSlider()) {
                continue;
            }
            if (rating.getRating() != null) {
                result += (rating.getRating());
                divider++;
            }
        }
        return result / divider;
    }

    public Double getCalculatedAverageRate(boolean isWeightable, int goalPercent) {
        if (!isWeightable) {
            return getCalculatedAverageRate();
        }
        if (getRatings().isEmpty()) {
            return null;
        }
        Double result = 0d;
        for (EdsGoalRating rating : getRatings()) {
            if (rating.getShowSlider() != null && !rating.getShowSlider()) {
                continue;
            }
            if (rating.getRating() != null && rating.getWeight() != null) {
                result += (rating.getRating().floatValue() * (rating.getWeight().floatValue() / 100f));
//                divider += rating.getWeight();
            }
        }
        return (result * goalPercent / 100);
    }

}
