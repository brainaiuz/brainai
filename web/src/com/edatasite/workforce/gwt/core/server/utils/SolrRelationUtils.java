package com.edatasite.workforce.gwt.core.server.utils;

import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.solr.document.BaseSolrDoc;
import com.edatasite.workforce.core.solr.document.RelationBaseSolrDoc;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: dilsh0d
 * Date: 19/03/12
 * Time: 16:03
 */
public class SolrRelationUtils {

    public static String[] relationTypes = new String[]{
            EdsRelation.TYPE_CASE,
            EdsRelation.TYPE_OPPORTUNITY,
            EdsRelation.TYPE_CRM_ACCOUNT,
            EdsRelation.TYPE_LEAD,
            EdsRelation.TYPE_CONTACT,
            EdsRelation.TYPE_EVENT,
            EdsRelation.TYPE_CANDIDATE,
            EdsRelation.TYPE_TASK,
            EdsRelation.TYPE_PROJECT,
            EdsRelation.TYPE_EMAIL_TRACKER,
            EdsRelation.TYPE_ISSUE,
            EdsRelation.TYPE_CLIENT,
            EdsRelation.TYPE_SUPPLIER,
            EdsRelation.TYPE_EMPLOYEE,
            EdsRelation.TYPE_DEPARTMENT
    };

    public static void addToSolrRelations(SolrInputDocument doc, List<EdsRelation> edsRelationList, String relationType) {
        for (EdsRelation edsRelation : edsRelationList) {
            if (relationType.equals(edsRelation.getFromType()) && edsRelation.getToID() != null && edsRelation.getToName() != null && edsRelation.getToType() != null) {
                if (getRelationIdSolrFieldName(edsRelation.getToType()) != null) {
                    doc.addField(getRelationIdSolrFieldName(edsRelation.getToType()), edsRelation.getToID());
                    doc.addField(getRelationNameSolrFieldName(edsRelation.getToType()), edsRelation.getToName());
                    doc.addField(getRelationIdNameSolrFieldName(edsRelation.getToType()), edsRelation.getToID() + SolrEventRepresenter.SPLIT + edsRelation.getToName());
                }
            } else if (relationType.equals(edsRelation.getToType()) && edsRelation.getFromID() != null && edsRelation.getFromName() != null && edsRelation.getFromType() != null) {
                if (getRelationIdSolrFieldName(edsRelation.getFromType()) != null) {
                    doc.addField(getRelationIdSolrFieldName(edsRelation.getFromType()), edsRelation.getFromID());
                    doc.addField(getRelationNameSolrFieldName(edsRelation.getFromType()), edsRelation.getFromName());
                    doc.addField(getRelationIdNameSolrFieldName(edsRelation.getFromType()), edsRelation.getFromID() + SolrEventRepresenter.SPLIT + edsRelation.getFromName());
                }
            }
        }
    }

    public static void addToRelationBaseSolrDoc(RelationBaseSolrDoc doc, List<EdsRelation> edsRelationList, String relationType) {
        for (EdsRelation edsRelation : edsRelationList) {
            if (relationType.equals(edsRelation.getFromType()) && edsRelation.getToID() != null && edsRelation.getToName() != null && edsRelation.getToType() != null) {
                if (getRelationFieldName(edsRelation.getToType()) != null) {
                    doc.getRelatedIdDynamic().computeIfAbsent(getRelationFieldName(edsRelation.getToType()), value -> new ArrayList<>()).add(edsRelation.getToID());
                    doc.getRelatedNameDynamic().computeIfAbsent(getRelationFieldName(edsRelation.getToType()), value -> new ArrayList<>()).add(edsRelation.getToName());
                    doc.getRelatedIdNameDynamic().computeIfAbsent(getRelationFieldName(edsRelation.getToType()), value -> new ArrayList<>()).add(edsRelation.getToID() + SolrEventRepresenter.SPLIT + edsRelation.getToName());
                }
            } else if (relationType.equals(edsRelation.getToType()) && edsRelation.getFromID() != null && edsRelation.getFromName() != null && edsRelation.getFromType() != null) {
                if (getRelationFieldName(edsRelation.getFromType()) != null) {
                    doc.getRelatedIdDynamic().computeIfAbsent(getRelationFieldName(edsRelation.getFromType()), value -> new ArrayList<>()).add(edsRelation.getFromID());
                    doc.getRelatedNameDynamic().computeIfAbsent(getRelationFieldName(edsRelation.getFromType()), value -> new ArrayList<>()).add(edsRelation.getFromName());
                    doc.getRelatedIdNameDynamic().computeIfAbsent(getRelationFieldName(edsRelation.getFromType()), value -> new ArrayList<>()).add(edsRelation.getFromID() + SolrEventRepresenter.SPLIT + edsRelation.getFromName());
                }
            }
        }
    }

    private static String getRelationFieldName(String relationType) {
        if (EdsRelation.TYPE_CASE.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_CASE;
        } else if (EdsRelation.TYPE_OPPORTUNITY.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_OPPORTUNITY;
        } else if (EdsRelation.TYPE_CRM_ACCOUNT.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_CRM_ACCOUNT;
        } else if (EdsRelation.TYPE_LEAD.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_LEAD;
        } else if (EdsRelation.TYPE_CONTACT.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_CONTACT;
        } else if (EdsRelation.TYPE_CANDIDATE.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_CANDIDATE;
        } else if (EdsRelation.TYPE_EVENT.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_EVENT;
        } else if (EdsRelation.TYPE_TASK.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_TASK;
        } else if (EdsRelation.TYPE_PROJECT.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_PROJECT;
        } else if (EdsRelation.TYPE_EMAIL_TRACKER.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_EMAIL_TRACKER;
        } else if (EdsRelation.TYPE_ISSUE.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_ISSUE;
        } else if (EdsRelation.TYPE_CLIENT.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_CLIENT;
        } else if (EdsRelation.TYPE_SUPPLIER.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_SUPPLIER;
        } else if (EdsRelation.TYPE_EMPLOYEE.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_EMPLOYEE;
        } else if (EdsRelation.TYPE_DEPARTMENT.equalsIgnoreCase(relationType)) {
            return EdsRelation.TYPE_DEPARTMENT;
        }
        return null;
    }

    private static String getRelationIdSolrFieldName(String relationType) {
        if (EdsRelation.TYPE_CASE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_CASE;
        } else if (EdsRelation.TYPE_OPPORTUNITY.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_OPPORTUNITY;
        } else if (EdsRelation.TYPE_CRM_ACCOUNT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_CRM_ACCOUNT;
        } else if (EdsRelation.TYPE_LEAD.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_LEAD;
        } else if (EdsRelation.TYPE_CONTACT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_CONTACT;
        } else if (EdsRelation.TYPE_CANDIDATE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_CANDIDATE;
        } else if (EdsRelation.TYPE_EVENT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_EVENT;
        } else if (EdsRelation.TYPE_TASK.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_TASK;
        } else if (EdsRelation.TYPE_PROJECT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_PROJECT;
        } else if (EdsRelation.TYPE_EMAIL_TRACKER.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_EMAIL_TRACKER;
        } else if (EdsRelation.TYPE_ISSUE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_ISSUE;
        } else if (EdsRelation.TYPE_CLIENT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_CLIENT;
        } else if (EdsRelation.TYPE_SUPPLIER.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_SUPPLIER;
        } else if (EdsRelation.TYPE_EMPLOYEE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_EMPLOYEE;
        } else if (EdsRelation.TYPE_DEPARTMENT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + EdsRelation.TYPE_DEPARTMENT;
        }
        return null;
    }

    private static String getRelationNameSolrFieldName(String relationType) {
        if (EdsRelation.TYPE_CASE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_CASE;
        } else if (EdsRelation.TYPE_OPPORTUNITY.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_OPPORTUNITY;
        } else if (EdsRelation.TYPE_CRM_ACCOUNT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_CRM_ACCOUNT;
        } else if (EdsRelation.TYPE_LEAD.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_LEAD;
        } else if (EdsRelation.TYPE_CONTACT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_CONTACT;
        } else if (EdsRelation.TYPE_CANDIDATE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_CANDIDATE;
        } else if (EdsRelation.TYPE_EVENT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_EVENT;
        } else if (EdsRelation.TYPE_TASK.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_TASK;
        } else if (EdsRelation.TYPE_PROJECT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_PROJECT;
        } else if (EdsRelation.TYPE_EMAIL_TRACKER.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_EMAIL_TRACKER;
        } else if (EdsRelation.TYPE_ISSUE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_ISSUE;
        } else if (EdsRelation.TYPE_CLIENT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_CLIENT;
        } else if (EdsRelation.TYPE_SUPPLIER.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_SUPPLIER;
        } else if (EdsRelation.TYPE_EMPLOYEE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_EMPLOYEE;
        } else if (EdsRelation.TYPE_DEPARTMENT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_NAME + EdsRelation.TYPE_DEPARTMENT;
        }
        return null;
    }

    private static String getRelationIdNameSolrFieldName(String relationType) {
        if (EdsRelation.TYPE_CASE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_CASE;
        } else if (EdsRelation.TYPE_OPPORTUNITY.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_OPPORTUNITY;
        } else if (EdsRelation.TYPE_CRM_ACCOUNT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_CRM_ACCOUNT;
        } else if (EdsRelation.TYPE_LEAD.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_LEAD;
        } else if (EdsRelation.TYPE_CONTACT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_CONTACT;
        } else if (EdsRelation.TYPE_CANDIDATE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_CANDIDATE;
        } else if (EdsRelation.TYPE_EVENT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_EVENT;
        } else if (EdsRelation.TYPE_TASK.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_TASK;
        } else if (EdsRelation.TYPE_PROJECT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_PROJECT;
        } else if (EdsRelation.TYPE_EMAIL_TRACKER.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_EMAIL_TRACKER;
        } else if (EdsRelation.TYPE_ISSUE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_ISSUE;
        } else if (EdsRelation.TYPE_CLIENT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_CLIENT;
        } else if (EdsRelation.TYPE_SUPPLIER.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_SUPPLIER;
        } else if (EdsRelation.TYPE_EMPLOYEE.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_EMPLOYEE;
        } else if (EdsRelation.TYPE_DEPARTMENT.equalsIgnoreCase(relationType)) {
            return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + EdsRelation.TYPE_DEPARTMENT;
        }
        return null;
    }

    public static HashMap<String, String> getSolrRelationValue(SolrDocument doc, String relationType) {
        HashMap<String, String> relationValueMap = new HashMap<>();
        for (String typeName : relationTypes) {
            if (!typeName.equals(relationType)) {
                relationValueMap.put(typeName, ServerUtils.asListToString(SolrUtils.asListString(doc, getRelationNameSolrFieldName(typeName))));
            }
        }
        return relationValueMap;
    }

    public static HashMap<String, String> getRelationBaseSolrDocValue(RelationBaseSolrDoc doc, String relationType) {
        HashMap<String, String> relationValueMap = new HashMap<>();
        for (String typeName : relationTypes) {
            if (!typeName.equals(relationType) && doc.getRelatedNameDynamic().get(getRelationNameSolrFieldName(typeName)) != null) {
                relationValueMap.put(typeName, doc.getRelatedNameDynamic().get(getRelationNameSolrFieldName(typeName)).stream().collect(Collectors.joining(", ")));
            }
        }
        return relationValueMap;
    }

    public static HashMap<String, String> getBaseSolrDocValue(BaseSolrDoc doc, String relationType) {
        HashMap<String, String> relationValueMap = new HashMap<>();
        for (String typeName : relationTypes) {
            if (!typeName.equals(relationType)) {
                relationValueMap.put(typeName, doc.getStringValueDynamic().get(getRelationNameSolrFieldName(typeName)));
            }
        }
        return relationValueMap;
    }
}