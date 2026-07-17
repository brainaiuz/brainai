package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSkillGroup;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Manager interface for EdsSkillGroup entities.
 * Includes methods for listing, counting, and searching skill groups.
 */
public interface SkillGroupManager extends Manager<EdsSkillGroup> {

    /**
     * Returns a list of skill groups based on filter parameters.
     */
    List<EdsSkillGroup> skillGroupList(ListingFilterParameter fp);

    /**
     * Returns a skill group by its unique code.
     */
    EdsSkillGroup getByCode(String code);

    /**
     * Returns total count of skill groups.
     */
    Long getCount();

    /**
     * Finds a skill group by name, ignoring deleted items.
     * @param name the name to search for
     * @return the skill group with the given name, or null if not found
     */
    EdsSkillGroup findByName(String name);
}
