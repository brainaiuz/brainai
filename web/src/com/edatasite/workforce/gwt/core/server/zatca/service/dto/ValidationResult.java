package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import java.util.List;

public class ValidationResult {
    List<ValidationMessage> infoMessages;
    List<ValidationMessage> warningMessages;
    List<ValidationMessage> errorMessages;
    String status;
}
