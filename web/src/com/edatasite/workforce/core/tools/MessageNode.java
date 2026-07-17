package com.edatasite.workforce.core.tools;

import java.io.Serial;
import java.io.Serializable;
/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Dec 16, 2010
 * Time: 6:33:16 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * define an extended BodypartBean class to store additional data
 */
public final class MessageNode implements Serializable {
    @Serial
    private static final long serialVersionUID = 750866110886999439L;

    BodypartBean aNode = null;

    int level = 0;

    public MessageNode(BodypartBean aNode, int level) {
        this.aNode = aNode;
        this.level = level;
    }

    public BodypartBean getBodypartNode() {
        return aNode;
    }

    public int getLevel() {
        return level;
    }
}
