package com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages;

import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.type.ComponentType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class HeaderComponent extends Component<HeaderComponent> {
    /**
     * Instantiates a new Component.
     */
    public HeaderComponent() {
        super(ComponentType.HEADER);
    }


}
