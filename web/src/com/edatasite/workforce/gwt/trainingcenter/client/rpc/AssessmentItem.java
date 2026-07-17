package com.edatasite.workforce.gwt.trainingcenter.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: 18.09.12
 * Time: 17:39
 */
public class AssessmentItem implements IsSerializable {

    public static String NAME = "name";
    public static String ACTION = "action";
    public static String TOTAL_POINTS = "totalPoints";
    public static String TYPE = "type";

    private Integer objectId;
    private Integer studentQuestionaireId;
    private String name;
    private String totalPoints;
    private ArrayList<QuestionarieResponseItem> responseItems;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(String totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getStudentQuestionaireId() {
        return studentQuestionaireId;
    }

    public void setStudentQuestionaireId(Integer studentQuestionaireId) {
        this.studentQuestionaireId = studentQuestionaireId;
    }

    public ArrayList<QuestionarieResponseItem> getResponseItems() {
        return responseItems;
    }

    public void setResponseItems(ArrayList<QuestionarieResponseItem> responseItems) {
        this.responseItems = responseItems;
    }
}
