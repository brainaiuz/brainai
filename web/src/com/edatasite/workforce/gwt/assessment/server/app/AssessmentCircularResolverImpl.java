package com.edatasite.workforce.gwt.assessment.server.app;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsSkill;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsAssessmentTemplate;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsGoalAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsGoalRating;
import com.edatasite.workforce.core.domain.assessment.EdsSkillRating;
import com.edatasite.workforce.core.domain.goal.EdsGoal;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentRatingsComments;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentSkills;
import com.edatasite.workforce.gwt.assessment.client.rpc.RatingComment;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElem;
import com.edatasite.workforce.gwt.assessment.client.rpc.SkillAssessmentElemsStruct;
import com.edatasite.workforce.gwt.assessment.server.struct.RatingCommentLists;
import com.edatasite.workforce.gwt.assessment.server.struct.SkillAsKey;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.AssessmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeAssessmentManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Admin
 * Date: 12.10.2008
 * Time: 19:40:38
 */

@SuppressWarnings({"ToArrayCallWithZeroLengthArrayArgument"})
@Transactional
@Service("assessmentCircularResolver")
public class AssessmentCircularResolverImpl implements AssessmentCircularResolver, Constants {

    @Autowired
    private AssessmentManager assessmentManager;
    @Autowired
    private EmployeeAssessmentManager employeeAssessmentManager;
    @Autowired
    @Qualifier("hrmsLocalizer")
    private WfmMessageSource hrmsLocalizer;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private UserManager userManager;

    /**
     * Retrieves the result of data for Reviewer and Employee in 360 degree assessment
     */
    public AssessmentSkills getAssessmentSkillsComments(Integer assessmentId) {
        AssessmentSkills assessmentSkills = new AssessmentSkills();
        EdsUser currentUser = assessmentManager.getUser();
        EdsAssessment assessment = assessmentManager.get(assessmentId);
        Set<EdsEmployeeAssessment> employeeAssessments = assessment.getEmployeeAssessments();
        EdsEmployeeAssessment keyEmployeeAssessment = assessment.getKeyEmployeeAssessment();
        EdsUser reviewer = keyEmployeeAssessment.getAssessment().getReviewer();
        boolean turn = currentUser.equals(reviewer);
        Map<SkillAsKey, RatingCommentLists> ratingsBySkill = new LinkedHashMap<>();
        for (EdsEmployeeAssessment emplAss : employeeAssessments) {
            if (emplAss.getCollaborator() == null) {
                for (EdsSkillRating skillRating : emplAss.getSkillAssessment().getRatings()) {
                    RatingCommentLists ratComm;
                    EdsSkill skill = skillRating.getSkill();
                    String skillName = hrmsLocalizer.localize(skill.getCode(), skill.getName());
                    String skillDescription = hrmsLocalizer.localize(skill.getDescriptionCode(), skill.getDescription());
                    SkillAsKey item = new SkillAsKey(skill.getObjectID(), skillName, skillDescription);
                    if (ratingsBySkill.get(item) == null) {
                        ratComm = new RatingCommentLists();
                    } else {
                        ratComm = ratingsBySkill.get(item);
                    }
                    ratingsBySkill.put(item, ratComm);
                }
            } else {
                for (EdsSkillRating skillRating : emplAss.getSkillAssessment().getRatings()) {
                    RatingCommentLists ratComm;
                    EdsSkill skill = skillRating.getSkill();
                    String skillName = hrmsLocalizer.localize(skill.getCode(), skill.getName());
                    String skillDescription = hrmsLocalizer.localize(skill.getDescriptionCode(), skill.getDescription());
                    SkillAsKey item = new SkillAsKey(skill.getObjectID(), skillName, skillDescription);
                    if (ratingsBySkill.get(item) == null) {
                        ratComm = new RatingCommentLists();
                    } else {
                        ratComm = ratingsBySkill.get(item);
                    }

                    RatingComment rc = new RatingComment();
                    rc.setComment(skillRating.getLastReviewerComment());
                    rc.setName(emplAss.getCollaborator().getName());
                    rc.setRateable(skillRating.getShowSlider());
                    rc.setAnonymous(emplAss.getAnonymous());
                    if (skillRating.getShowSlider() != null && skillRating.getShowSlider()) {
                        rc.setRating(skillRating.getRating());
                    }
                    rc.setStatus(emplAss.getStatus().getCode());
                    if (emplAss.isPeer()) {
                        ratComm.addPeer(rc);
                    } else if (emplAss.isManager()) {
                        ratComm.addManager(rc);
                    } else if (emplAss.isClient()) {
                        ratComm.addClient(rc);
                    }
                    ratingsBySkill.put(item, ratComm);
                }
            }
        }
        List<EdsSkillRating> keySkillRatings = keyEmployeeAssessment.getSkillAssessment().getRatings();
        for (SkillAsKey innerKey : ratingsBySkill.keySet()) {
            for (EdsSkillRating skillRating : keySkillRatings) {
                if (innerKey.skillId.equals(skillRating.getSkill().getObjectID())) {
                    innerKey.keySkillRatingId = skillRating.getObjectID();
                    innerKey.employeeComment = skillRating.getLastEmployeeComment();
                    innerKey.managerComment = skillRating.getLastReviewerComment();
                    innerKey.employeeRating = skillRating.getEmployeeRating();
                    innerKey.managerRating = skillRating.getRating();
                    innerKey.rateable = skillRating.getShowSlider();
                    innerKey.overalRate = skillRating.getSkillAssessment().getCalculatedAverageRate();
                    break;
                }
            }
        }
        assessmentSkills.setKeyEmployeeAssessmentId(keyEmployeeAssessment.getObjectID());
        assessmentSkills.setStatus(keyEmployeeAssessment.getStatus().getCode());
        assessmentSkills.setTurn(turn);
        assessmentSkills.setManagerName(reviewer.getName());
        assessmentSkills.setManagerID(reviewer.getObjectID());
        assessmentSkills.setCurrentUserManager(currentUser.equals(reviewer));
        EdsEmployee keyEmployeeAssessmentEmployee = keyEmployeeAssessment.getEmployee();
        assessmentSkills.setEmployeeName(keyEmployeeAssessmentEmployee.getName());
        assessmentSkills.setEmployeeId(keyEmployeeAssessmentEmployee.getObjectID());
        assessmentSkills.setInitiateDate(keyEmployeeAssessmentEmployee.getCompany().getCompanyDate());
        if (keyEmployeeAssessmentEmployee.getEmployeeTeam() != null && keyEmployeeAssessmentEmployee.getEmployeeTeam().getTeam() != null) {
            assessmentSkills.setTeam(keyEmployeeAssessmentEmployee.getEmployeeTeam().getTeam().getName());
        }
        AssessmentRatingsComments[] asRatingsComments = new AssessmentRatingsComments[ratingsBySkill.size()];

        Iterator<Map.Entry<SkillAsKey, RatingCommentLists>> iterator = ratingsBySkill.entrySet().iterator();
        int i = 0;
        double overalRate = 0d;
        int devide = 0;

        while (iterator.hasNext()) {
            asRatingsComments[i] = new AssessmentRatingsComments();
            Map.Entry<SkillAsKey, RatingCommentLists> entry = iterator.next();
            SkillAsKey key = entry.getKey();
            RatingCommentLists value = entry.getValue();
            asRatingsComments[i].setSkillName(key.skillName);
            asRatingsComments[i].setSkillID(key.skillId);
            asRatingsComments[i].setSkillDescription(key.skillDescription);
            asRatingsComments[i].setKeySkillRatingId(key.keySkillRatingId);
            asRatingsComments[i].setEmployeeComment(key.employeeComment);
            asRatingsComments[i].setManagerComment(key.managerComment);
            asRatingsComments[i].setManagerRating(key.managerRating);
            asRatingsComments[i].setEmployeeRating(key.employeeRating);
            asRatingsComments[i].setRateable(key.rateable);
            asRatingsComments[i].setPeers(value.peers.toArray(new RatingComment[]{}));
            asRatingsComments[i].setManagers(value.managers.toArray(new RatingComment[]{}));
            asRatingsComments[i].setClients(value.clients.toArray(new RatingComment[]{}));
            Double overal = calculatOveralAverage(asRatingsComments[i]);
            if (overal != null) {
                asRatingsComments[i].setCalculatedAverage(overal.floatValue());
                overalRate += overal;
                devide++;
            } else {
                asRatingsComments[i].setCalculatedAverage(0d);
            }

            i++;
        }
        if (devide > 0) {
            overalRate /= devide;
        }
        assessmentSkills.setCalculatedAverage(overalRate);
        assessmentSkills.setRatingsComments(asRatingsComments);
        return assessmentSkills;
    }

    public AssessmentSkills getAssessmentGoalsComments(Integer assessmentId) {
        AssessmentSkills assessmentGoals = new AssessmentSkills();
        EdsUser currentUser = assessmentManager.getUser();
        EdsAssessment assessment = assessmentManager.get(assessmentId);
        Set<EdsEmployeeAssessment> employeeAssessments = assessment.getEmployeeAssessments();
        EdsEmployeeAssessment keyEmployeeAssessment = assessment.getKeyEmployeeAssessment();
        if (keyEmployeeAssessment.getGoalAssessment() == null) {
            return new AssessmentSkills();
        }
        EdsUser reviewer = keyEmployeeAssessment.getAssessment().getReviewer();
        boolean turn = currentUser.equals(reviewer);
        Map<SkillAsKey, RatingCommentLists> ratingsByGoal = new LinkedHashMap<>();
        for (EdsEmployeeAssessment emplAss : employeeAssessments) {
            if (emplAss.getGoalAssessment() != null) {
                if (emplAss.getCollaborator() == null) {

                    for (EdsGoalRating goalRating : emplAss.getGoalAssessment().getRatings()) {
                        RatingCommentLists ratComm;
                        EdsGoal goal = goalRating.getGoal();
                        SkillAsKey item = new SkillAsKey(goal.getObjectID(), goal.getTitle(), goal.getDescription());

                        if (ratingsByGoal.get(item) == null) {
                            ratComm = new RatingCommentLists();
                        } else {
                            ratComm = ratingsByGoal.get(item);
                        }
                        ratingsByGoal.put(item, ratComm);
                    }
                } else {

                    for (EdsGoalRating goalRating : emplAss.getGoalAssessment().getRatings()) {
                        RatingCommentLists ratComm;
                        EdsGoal goal = goalRating.getGoal();
                        SkillAsKey item = new SkillAsKey(goal.getObjectID(), goal.getTitle(), goal.getDescription());
                        if (ratingsByGoal.get(item) == null) {
                            ratComm = new RatingCommentLists();
                        } else {
                            ratComm = ratingsByGoal.get(item);
                        }

                        RatingComment rc = new RatingComment();
                        rc.setComment(goalRating.getLastReviewerComment());
                        rc.setName(emplAss.getCollaborator().getName());
                        rc.setRateable(goalRating.getShowSlider());
                        rc.setAnonymous(emplAss.getAnonymous());
                        if (goalRating.getShowSlider() != null && goalRating.getShowSlider()) {
                            rc.setRating(goalRating.getRating());
                        }
                        rc.setStatus(emplAss.getStatus().getCode());
                        if (emplAss.isPeer()) {
                            ratComm.addPeer(rc);
                        } else if (emplAss.isManager()) {
                            ratComm.addManager(rc);
                        } else if (emplAss.isClient()) {
                            ratComm.addClient(rc);
                        }
                        ratingsByGoal.put(item, ratComm);
                    }
                }
            }
        }

        List<EdsGoalRating> keyGoalRatings = keyEmployeeAssessment.getGoalAssessment().getRatings();
        for (SkillAsKey innerKey : ratingsByGoal.keySet()) {
            for (EdsGoalRating goalRating : keyGoalRatings) {
                if (innerKey.skillId.equals(goalRating.getGoal().getObjectID())) {
                    innerKey.keySkillRatingId = goalRating.getObjectID();
                    innerKey.employeeComment = goalRating.getLastEmployeeComment();
                    innerKey.managerComment = goalRating.getLastReviewerComment();
                    //employee via reviewer comment history items
                    innerKey.employeeRating = goalRating.getEmployeeRating();
                    innerKey.managerRating = goalRating.getRating();
                    innerKey.rateable = goalRating.getShowSlider();
                    innerKey.overalRate = goalRating.getGoalAssessment().getCalculatedAverageRate();
                    break;
                }
            }
        }
        assessmentGoals.setKeyEmployeeAssessmentId(keyEmployeeAssessment.getObjectID());
        assessmentGoals.setStatus(keyEmployeeAssessment.getStatus().getCode());
        assessmentGoals.setTurn(turn);
        assessmentGoals.setManagerName(reviewer.getName());
        assessmentGoals.setManagerID(reviewer.getObjectID());
        assessmentGoals.setCurrentUserManager(currentUser.equals(reviewer));
        EdsEmployee keyEmployeeAssessmentEmployee = keyEmployeeAssessment.getEmployee();
        assessmentGoals.setEmployeeName(keyEmployeeAssessmentEmployee.getName());
        assessmentGoals.setEmployeeId(keyEmployeeAssessmentEmployee.getObjectID());
        assessmentGoals.setInitiateDate(keyEmployeeAssessmentEmployee.getCompany().getCompanyDate());
        if (keyEmployeeAssessmentEmployee.getEmployeeTeam() != null && keyEmployeeAssessmentEmployee.getEmployeeTeam().getTeam() != null) {
            assessmentGoals.setTeam(keyEmployeeAssessmentEmployee.getEmployeeTeam().getTeam().getName());
        }
        AssessmentRatingsComments[] asRatingsComments = new AssessmentRatingsComments[ratingsByGoal.size()];

        Iterator<Map.Entry<SkillAsKey, RatingCommentLists>> iterator = ratingsByGoal.entrySet().iterator();
        int i = 0;
        double overalRate = 0d;
        int divider = 0;

        while (iterator.hasNext()) {
            asRatingsComments[i] = new AssessmentRatingsComments();
            Map.Entry<SkillAsKey, RatingCommentLists> entry = iterator.next();
            SkillAsKey key = entry.getKey();
            RatingCommentLists value = entry.getValue();
            asRatingsComments[i].setSkillName(key.skillName);
            asRatingsComments[i].setSkillID(key.skillId);
            asRatingsComments[i].setSkillDescription(key.skillDescription);
            asRatingsComments[i].setKeySkillRatingId(key.keySkillRatingId);
            asRatingsComments[i].setEmployeeComment(key.employeeComment);
            asRatingsComments[i].setManagerComment(key.managerComment);
            asRatingsComments[i].setManagerRating(key.managerRating);
            asRatingsComments[i].setEmployeeRating(key.employeeRating);
            asRatingsComments[i].setRateable(key.rateable);
            asRatingsComments[i].setPeers(value.peers.toArray(new RatingComment[]{}));
            asRatingsComments[i].setManagers(value.managers.toArray(new RatingComment[]{}));
            asRatingsComments[i].setClients(value.clients.toArray(new RatingComment[]{}));
            Double overal = calculatOveralAverage(asRatingsComments[i]);
            if (overal != null) {
                asRatingsComments[i].setCalculatedAverage(overal.floatValue());
                overalRate += overal.floatValue();
                divider++;
            } else {
                asRatingsComments[i].setCalculatedAverage(0d);
            }

            i++;
        }
        if (divider > 0) {
            overalRate /= divider;
        }
        assessmentGoals.setCalculatedAverage(overalRate);
        assessmentGoals.setRatingsComments(asRatingsComments);
        return assessmentGoals;
    }

    private Double calculatOveralAverage(AssessmentRatingsComments ratingComment) {
        double overal = 0d;
        int count = 0;
        if (ratingComment.getPeers() != null) {
            RatingComment[] com = ratingComment.getPeers();
            for (RatingComment aCom : com) {
                if (aCom.getRating() != null && aCom.getRating() > 0) {
                    overal += aCom.getRating();
                    count++;
                }
            }
        }
        if (ratingComment.getManagers() != null) {
            RatingComment[] com = ratingComment.getManagers();
            for (RatingComment aCom : com) {
                if (aCom.getRating() != null && aCom.getRating() > 0) {
                    overal += aCom.getRating();
                    count++;
                }
            }
        }
        if (ratingComment.getClients() != null) {
            RatingComment[] com = ratingComment.getClients();
            for (RatingComment aCom : com) {
                if (aCom.getRating() != null && aCom.getRating() > 0) {
                    overal += aCom.getRating();
                    count++;
                }
            }
        }
        if (ratingComment.getManagerRating() != null && ratingComment.getManagerRating() > 0) {
            overal += ratingComment.getManagerRating();
            count++;
        }
        if (count > 0 && overal > 0) {
            return overal / count;
        }
        return null;
    }

    public SkillAssessmentElemsStruct getSkillAssessmentElemGroups(Integer employeeAssessmentId, Integer currentUserID, boolean hasReviewerSupervisor) {
        EdsUser edsUser = userManager.get(currentUserID);
        EdsEmployeeAssessment employeeAssessment = employeeAssessmentManager.get(employeeAssessmentId);
        EdsAssessment employeeAssessmentAssessment = employeeAssessment.getAssessment();
        boolean isWeightable = employeeAssessmentAssessment.isWeightable() != null && employeeAssessmentAssessment.isWeightable();
        List<EdsSkillRating> ratings = employeeAssessment.getSkillAssessment().getRatings();
        List<SkillAssessmentElem> skillAssessmentElems = new ArrayList<>();

        for (EdsSkillRating skillRating : ratings) {
            if (skillRating.getSkill() != null) {
                EdsSkill skill = skillRating.getSkill();
                String skillName = hrmsLocalizer.localize(skill.getCode(), skill.getName());
                String skillDescription = hrmsLocalizer.localize(skill.getDescriptionCode(), skill.getDescription());

                if (isWeightable) {
                    skillAssessmentElems.add(new SkillAssessmentElem(skillRating.getObjectID(), skillDescription,
                            skillName, skillRating.getRating(), skillRating.getEmployeeRating(),
                            skillRating.getWeight(),
                            skillRating.getLastEmployeeComment(), skillRating.getLastReviewerComment(),
                            skillRating.getRatingCommentItems(), skillRating.getSavedAsDraftComment(),
                            skillRating.getShowSlider(), skillRating.getSkill().getObjectID(),
                            skillRating.getEmployeeGrade(), skillRating.getManagerGrade()));
                } else {
                    skillAssessmentElems.add(new SkillAssessmentElem(skillRating.getObjectID(), skillDescription,
                            skillName, skillRating.getRating(), skillRating.getEmployeeRating(),
                            skillRating.getLastEmployeeComment(), skillRating.getLastReviewerComment(),
                            skillRating.getRatingCommentItems(), skillRating.getSavedAsDraftComment(),
                            skillRating.getShowSlider(), skillRating.getSkill().getObjectID(),
                            skillRating.getEmployeeGrade(), skillRating.getManagerGrade()));
                }
            }
        }

        SkillAssessmentElemsStruct elemStruct = new SkillAssessmentElemsStruct();
        if (employeeAssessmentAssessment.getValidityPeriod() != null) {
            elemStruct.setValidityPeriodId(employeeAssessmentAssessment.getValidityPeriod().getObjectID());
        }
        EdsUser initiator = employeeAssessmentAssessment.getInitiator();
        elemStruct.setCompanyName(initiator.getCompany().getName());
        elemStruct.setElems(skillAssessmentElems.toArray(new SkillAssessmentElem[]{}));
        elemStruct.setManagerPong(employeeAssessment.getManagerPong() != null ? employeeAssessment.getManagerPong() : 0);
        elemStruct.setEmployeePong(employeeAssessment.getEmployeePing() != null ? employeeAssessment.getEmployeePing() : 0);
        EdsEmployee employeeAssessmentEmployee = employeeAssessment.getEmployee();
        elemStruct.setEmployeeId(employeeAssessmentEmployee.getObjectID());
        elemStruct.setEmployeeStatus(employeeAssessmentEmployee.getAccountStatus().getCode());
        elemStruct.setWeightable(isWeightable);
        elemStruct.setGeneralComment(employeeAssessmentAssessment.getGeneralComment());
        elemStruct.setAssessmentDate(employeeAssessmentAssessment.getAssessmentDay());
        if (employeeAssessmentEmployee.getTeam() != null) {
            elemStruct.setDepartmentName(employeeAssessmentEmployee.getTeam().getName());
        }
        if (employeeAssessment.getSkillAssessment() != null) {
            if (isWeightable) {
                elemStruct.setCalculatedAverage(employeeAssessment.getSkillAssessment().
                        getCalculatedAverageRate(employeeAssessmentAssessment.isWeightable(), employeeAssessmentAssessment.getSkillsWeightPercent()) != null ? employeeAssessment.getSkillAssessment().
                        getCalculatedAverageRate(employeeAssessmentAssessment.isWeightable(), employeeAssessmentAssessment.getSkillsWeightPercent()) : 0);
                elemStruct.setSkillWeigthPercent(employeeAssessmentAssessment.getSkillsWeightPercent());
            } else {
                elemStruct.setCalculatedAverage(employeeAssessment.getSkillAssessment().getCalculatedAverageRate() != null ?
                        employeeAssessment.getSkillAssessment().getCalculatedAverageRate() : 0);
            }
        }
        EdsAssessmentTemplate assessmentTemplate = employeeAssessmentAssessment.getTemplate();
        if (assessmentTemplate != null) {
            elemStruct.setTemplateName(assessmentTemplate.getName());
        }
        elemStruct.setCurrentUserEmployee(edsUser.getObjectID().equals(employeeAssessmentEmployee.getObjectID()));
        elemStruct.setInitiator(initiator.getName());
        elemStruct.setInitiatorID(initiator.getObjectID());
        elemStruct.setCurrentUserInitiator(edsUser.equals(initiator));

        Integer supervisorID = (hasReviewerSupervisor && (employeeAssessmentEmployee.getProfile() != null && employeeAssessmentEmployee.getProfile().getReportsTo() != null)) ? employeeAssessmentEmployee.getProfile().getReportsTo().getObjectID() : 0;
        elemStruct.setCurrentUserSupervisor(edsUser.getObjectID().equals(supervisorID));
        elemStruct.setEmployeeName(employeeAssessmentEmployee.getName());
        elemStruct.setStatus(employeeAssessment.getStatus().getCode());
        EdsUser employeeAssessmentAssessmentLastUpdater = employeeAssessmentAssessment.getLastUpdater();
        if (employeeAssessmentAssessmentLastUpdater != null) {
            elemStruct.setLastUpdaterID(employeeAssessmentAssessmentLastUpdater.getObjectID());
            elemStruct.setCurrentUserLastUpdater(edsUser.equals(employeeAssessmentAssessmentLastUpdater));
            elemStruct.setLastUpdaterName(employeeAssessmentAssessmentLastUpdater.getName());
        }
        elemStruct.setAssessmentType(employeeAssessmentAssessment.getAssessmentType().getCode());
        EdsUser reviewer = employeeAssessmentAssessment.getReviewer();
        if (elemStruct.getAssessmentType().equals(ASSESSMENT_360)) {
            EdsUser collaborator = employeeAssessment.getCollaborator();
            if (collaborator == null) {
                elemStruct.setTurn(EMPLOYEE_TURN); //turn is for 360 employee and collaborator
                elemStruct.setReviewerName(reviewer.getName());
                elemStruct.setReviewerID(reviewer.getObjectID());
                elemStruct.setCurrentUserReviewer(edsUser.equals(reviewer));
            } else {
                elemStruct.setTurn(MANAGER_TURN);
                elemStruct.setReviewerName(collaborator.getName());
                elemStruct.setReviewerID(collaborator.getObjectID());
                elemStruct.setCurrentUserReviewer(edsUser.equals(collaborator));
                elemStruct.setAssessmentType(referenceWfmMessageSource.localizeRef(employeeAssessment.getType()));
            }
            if (employeeAssessment.getType() != null) {
                if (employeeAssessment.isClient()) {
                    elemStruct.setReviewerType(IS_CLIENT);
                } else if (employeeAssessment.isManager()) {
                    elemStruct.setReviewerType(IS_MANAGER);
                } else if (employeeAssessment.isPeer()) {
                    elemStruct.setReviewerType(IS_PEER);
                } else {
                    elemStruct.setReviewerType(IS_EMPLOYEE);
                }
            } else {
                elemStruct.setReviewerType(IS_EMPLOYEE);
            }
        } else {
            elemStruct.setReviewerName(reviewer.getName());
            elemStruct.setReviewerID(reviewer.getObjectID());
            elemStruct.setCurrentUserReviewer(edsUser.equals(reviewer));
        }
        return elemStruct;
    }

    public SkillAssessmentElemsStruct getGoalAssessmentElemGroups(Integer employeeAssessmentId, Integer currentUserID, boolean hasReviewerSupervisor) {
        EdsUser currentUser = userManager.get(currentUserID);
        EdsEmployeeAssessment employeeAssessment = employeeAssessmentManager.get(employeeAssessmentId);
        EdsAssessment employeeAssessmentAssessment = employeeAssessment.getAssessment();
        boolean isWeightable = employeeAssessmentAssessment.isWeightable() != null ? employeeAssessmentAssessment.isWeightable() : false;
        EdsGoalAssessment employeeAssessmentGoalAssessment = employeeAssessment.getGoalAssessment();
        if (employeeAssessmentGoalAssessment == null) {
            return new SkillAssessmentElemsStruct();
        }
        List<EdsGoalRating> ratings = employeeAssessmentGoalAssessment.getRatings();
        List<SkillAssessmentElem> skillAssessmentElems = new ArrayList<>();

        for (EdsGoalRating goalRating : ratings) {
            if (goalRating.getGoal() != null) {
                if (isWeightable) {
                    skillAssessmentElems.add(new SkillAssessmentElem(goalRating.getObjectID(), goalRating.getGoal().getDescription(),
                            goalRating.getGoal().getTitle(), goalRating.getRating(), goalRating.getEmployeeRating(), goalRating.getWeight(),
                            goalRating.getLastEmployeeComment(), goalRating.getLastReviewerComment(),
                            goalRating.getRatingCommentItems(), goalRating.getSavedAsDraftComment(),
                            goalRating.getShowSlider(), goalRating.getGoal().getObjectID(),
                            goalRating.getEmployeeGrade(), goalRating.getManagerGrade()));
                } else {
                    skillAssessmentElems.add(new SkillAssessmentElem(goalRating.getObjectID(), goalRating.getGoal().getDescription(),
                            goalRating.getGoal().getTitle(), goalRating.getRating(), goalRating.getEmployeeRating(),
                            goalRating.getLastEmployeeComment(), goalRating.getLastReviewerComment(),
                            goalRating.getRatingCommentItems(), goalRating.getSavedAsDraftComment(),
                            goalRating.getShowSlider(), goalRating.getGoal().getObjectID(),
                            goalRating.getEmployeeGrade(), goalRating.getManagerGrade()));
                }
            }
        }
        SkillAssessmentElemsStruct elemStruct = new SkillAssessmentElemsStruct();
        if (employeeAssessmentAssessment.getValidityPeriod() != null) {
            elemStruct.setValidityPeriodId(employeeAssessmentAssessment.getValidityPeriod().getObjectID());
        }
        EdsUser initiator = employeeAssessmentAssessment.getInitiator();
        elemStruct.setCompanyName(initiator.getCompany().getName());
        elemStruct.setElems(skillAssessmentElems.toArray(new SkillAssessmentElem[]{}));
        elemStruct.setManagerPong(employeeAssessment.getManagerPong() != null ? employeeAssessment.getManagerPong() : 0);
        elemStruct.setEmployeePong(employeeAssessment.getEmployeePing() != null ? employeeAssessment.getEmployeePing() : 0);
        EdsEmployee employeeAssessmentEmployee = employeeAssessment.getEmployee();
        elemStruct.setEmployeeId(employeeAssessmentEmployee.getObjectID());
        elemStruct.setWeightable(isWeightable);
        elemStruct.setGeneralComment(employeeAssessmentAssessment.getGeneralComment());
        elemStruct.setAssessmentDate(employeeAssessmentAssessment.getAssessmentDay());
        if (employeeAssessmentEmployee.getTeam() != null) {
            elemStruct.setDepartmentName(employeeAssessmentEmployee.getTeam().getName());
        }
        if (employeeAssessment.getSkillAssessment() != null) {
            if (isWeightable) {
                elemStruct.setCalculatedAverage(employeeAssessmentGoalAssessment.getCalculatedAverageRate(employeeAssessmentAssessment.isWeightable(), employeeAssessmentAssessment.getGoalsWeightPercent()) != null ?
                        employeeAssessmentGoalAssessment.getCalculatedAverageRate(employeeAssessmentAssessment.isWeightable(), employeeAssessmentAssessment.getGoalsWeightPercent()).floatValue() : 0);
                elemStruct.setGoalWeigthPercent(employeeAssessmentAssessment.getGoalsWeightPercent());
            } else {
                elemStruct.setCalculatedAverage(employeeAssessmentGoalAssessment.getCalculatedAverageRate() != null ?
                        employeeAssessmentGoalAssessment.getCalculatedAverageRate().floatValue() : 0);
            }
        }//
        if (employeeAssessmentAssessment.getTemplate() != null) {
            elemStruct.setTemplateName(employeeAssessmentAssessment.getTemplate().getName());
        }
        elemStruct.setCurrentUserEmployee(currentUser.getObjectID().equals(employeeAssessmentEmployee.getObjectID()));
        elemStruct.setInitiator(initiator.getName());
        elemStruct.setInitiatorID(initiator.getObjectID());
        elemStruct.setCurrentUserInitiator(currentUser.equals(initiator));
        Integer supervisorID = (hasReviewerSupervisor && (employeeAssessmentEmployee.getProfile() != null && employeeAssessmentEmployee.getProfile().getReportsTo() != null)) ? employeeAssessmentEmployee.getProfile().getReportsTo().getObjectID() : 0;
        elemStruct.setCurrentUserSupervisor(currentUser.getObjectID().equals(supervisorID));
        elemStruct.setEmployeeName(employeeAssessmentEmployee.getName());
        elemStruct.setStatus(employeeAssessment.getStatus().getCode());
        elemStruct.setAssessmentType(employeeAssessmentAssessment.getAssessmentType().getCode());
        EdsUser reviewer = employeeAssessmentAssessment.getReviewer();
        if (elemStruct.getAssessmentType().equals(ASSESSMENT_360)) {
            EdsUser collaborator = employeeAssessment.getCollaborator();
            if (collaborator == null) {

                elemStruct.setTurn(EMPLOYEE_TURN);    //turn is for 360 employee and collaborator
                elemStruct.setReviewerName(reviewer.getName());
                elemStruct.setReviewerID(reviewer.getObjectID());
                elemStruct.setCurrentUserReviewer(currentUser.equals(reviewer));
            } else {
                elemStruct.setTurn(MANAGER_TURN);
                elemStruct.setReviewerName(collaborator.getName());
                elemStruct.setReviewerID(collaborator.getObjectID());
                elemStruct.setCurrentUserReviewer(currentUser.equals(collaborator));
                elemStruct.setAssessmentType(referenceWfmMessageSource.localizeRef(employeeAssessment.getType()));
            }
            if (employeeAssessment.getType() != null) {
                if (employeeAssessment.isClient()) {
                    elemStruct.setReviewerType(IS_CLIENT);
                } else if (employeeAssessment.isManager()) {
                    elemStruct.setReviewerType(IS_MANAGER);
                } else if (employeeAssessment.isPeer()) {
                    elemStruct.setReviewerType(IS_PEER);
                } else {
                    elemStruct.setReviewerType(IS_EMPLOYEE);
                }
            } else {
                elemStruct.setReviewerType(IS_EMPLOYEE);
            }
        } else {
            elemStruct.setReviewerName(reviewer.getName());
            elemStruct.setReviewerID(reviewer.getObjectID());
            elemStruct.setCurrentUserReviewer(currentUser.equals(reviewer));
        }
        return elemStruct;
    }
}