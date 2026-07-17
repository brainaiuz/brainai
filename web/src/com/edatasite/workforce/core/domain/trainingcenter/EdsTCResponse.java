package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 14.09.12
 * Time: 18:40
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "tcresponse")
public class EdsTCResponse extends EdsObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer objectID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sudentQuestionaireID")
	private EdsTCStudentQuestionaire sudentQuestionaire;

	private Integer questionNumber;

	private String answer;

	private Integer pointsEarnet;

	@Override
	public Integer getObjectID() {
		return objectID;
	}

	public EdsTCStudentQuestionaire getSudentQuestionaire() {
		return sudentQuestionaire;
	}

	public void setSudentQuestionaire(EdsTCStudentQuestionaire sudentQuestionaire) {
		this.sudentQuestionaire = sudentQuestionaire;
	}

	public Integer getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
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
}
