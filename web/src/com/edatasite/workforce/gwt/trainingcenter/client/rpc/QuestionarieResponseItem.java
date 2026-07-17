package com.edatasite.workforce.gwt.trainingcenter.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Abdullo
 * Date: 21.09.12
 * Time: 17:14
 */
public class QuestionarieResponseItem implements IsSerializable {
    private Integer id;
    private String answer;
    private Integer pointsEarnet;
    private Integer questionNumber;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getPointsEarnet() {
        return pointsEarnet;
    }

    public void setPointsEarnet(Integer pointsEarnet) {
        this.pointsEarnet = pointsEarnet;
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }
}
