package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 14.09.12
 * Time: 18:14
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "tcstudentquestionaire")
public class EdsTCStudentQuestionaire extends EdsObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer objectID;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "courseScheduleID")
	private EdsCourseSchedule courseSchedule;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "studentID")
	private EdsStudent student;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "questionaireID")
	private EdsTCQuestionaire questionaire;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private EdsReference questionaireStatus;

	private Integer totalPointEarnet = 0;

	@Override
	public Integer getObjectID() {
		return objectID;
	}

	public EdsCourseSchedule getCourseSchedule() {
		return courseSchedule;
	}

	public void setCourseSchedule(EdsCourseSchedule courseSchedule) {
		this.courseSchedule = courseSchedule;
	}

	public EdsStudent getStudent() {
		return student;
	}

	public void setStudent(EdsStudent student) {
		this.student = student;
	}

	public EdsTCQuestionaire getQuestionaire() {
		return questionaire;
	}

	public void setQuestionaire(EdsTCQuestionaire questionaire) {
		this.questionaire = questionaire;
	}

	public EdsReference getQuestionaireStatus() {
		return questionaireStatus;
	}

	public void setQuestionaireStatus(EdsReference questionaireStatus) {
		this.questionaireStatus = questionaireStatus;
	}

	public Integer getTotalPointEarnet() {
		return totalPointEarnet;
	}

	public void setTotalPointEarnet(Integer totalPointEarnet) {
		this.totalPointEarnet = totalPointEarnet;
	}
}
