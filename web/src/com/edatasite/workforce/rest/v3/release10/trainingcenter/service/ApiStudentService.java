package com.edatasite.workforce.rest.v3.release10.trainingcenter.service;

import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.StudentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ApiStudentService {
    private final TCService tcService;

    private static final Logger log = LoggerFactory.getLogger(ApiStudentService.class);

    public ApiStudentService(TCService tcService) {
        this.tcService = tcService;
    }

    public Integer create(StudentDto studentDto) {
        try {
            StudentItem studentItem = new StudentItem();
            studentItem.setCustomerID(studentDto.getCrmAccountId());
            studentItem.setContactID(studentDto.getContactId());
            studentItem.setActive(true);
            Integer studentId = tcService.saveGymStudentItem(studentItem);
            log.info("Student successfully created with ID: {}", studentId);
            return studentId;
        } catch (Exception e) {
            log.error("Error while creating student: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create student", e);
        }
    }
}
