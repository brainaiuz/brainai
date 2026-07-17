package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 14.09.12
 * Time: 18:08
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "tcquestionaire")
public class EdsTCQuestionaire extends EdsObject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer objectID;

	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeid")
	private EdsReference type;

	private Integer totalPoints;

	public Integer getObjectID() {
		return objectID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EdsReference getType() {
		return type;
	}

	public void setType(EdsReference type) {
		this.type = type;
	}

	public Integer getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}
}
