package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages;

import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.type.ComponentType;
import com.fasterxml.jackson.annotation.JsonInclude;



/**
 * The type Body component, to send template messages
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BodyComponent extends Component<BodyComponent> {


    /**
     * Instantiates a new Body component, to send template messages
     */
    public BodyComponent() {
        super(ComponentType.BODY);
    }


}
