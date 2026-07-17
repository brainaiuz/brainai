package com.edatasite.workforce.rest.v3.release10.trainingcenter.service;

import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.edatasite.workforce.gwt.trainingcenter.server.TCServiceLocal;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseCreateDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.CourseDto;
import com.edatasite.workforce.rest.v3.release10.trainingcenter.dto.LocationPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class ApiCourseService {
    @Autowired
    private TCServiceLocal tcService;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;

    public ListResultTO<CourseDto> getCourses(ListingFilterParameter fp) {
        ListResult<CourseItem> item = tcService.getCourseList(fp);

        ArrayList<CourseDto> courseDtoList = item.getList().stream()
                .map(it -> new CourseDto(
                        it.getObjectID(),
                        it.getCourseName(),
                        it.getDuration() != null ? it.getDuration().toString() : "", // Handle null case
                        it.getValidity() != null ? it.getValidity().toString() : "", // Handle null case
                        it.getInstructors() != null
                                ? it.getInstructors().stream()
                                .map(in -> new ItemDto(in.getId(), in.getName()))
                                .toList()
                                : new ArrayList<>(),
                        it.getPricePerLocationStudent() != null
                                ? it.getPricePerLocationStudent().entrySet().stream()
                                .map(entry -> new ItemDto(
                                        entry.getKey(),
                                        entry.getValue() != null ? entry.getValue().toString() : "")) // Handle null value
                                .toList()
                                : new ArrayList<>()
                )).collect(Collectors.toCollection(ArrayList::new));
        return new ListResultTO<>(item.getTotal(), courseDtoList);
    }


    public CourseDto getById(Integer courseId) {
        CourseItem courseItem = tcService.getCourseItem(courseId);

        if (courseItem == null) {
            return null;
        }

        return new CourseDto(
                courseItem.getObjectID(),
                courseItem.getCourseName(),
                courseItem.getDuration() != null ? courseItem.getDuration().toString() : "",
                courseItem.getValidity() != null ? courseItem.getValidity().toString() : "",
                courseItem.getInstructors() != null
                        ? courseItem.getInstructors().stream()
                        .map(in -> new ItemDto(in.getId(), in.getName()))
                        .toList()
                        : new ArrayList<>(),
                courseItem.getPricePerLocationStudent() != null
                        ? courseItem.getPricePerLocationStudent().entrySet().stream()
                        .map(entry -> new ItemDto(
                                entry.getKey(),
                                entry.getValue() != null ? entry.getValue().toString() : ""))
                        .toList()
                        : new ArrayList<>()
        );
    }

    @Transactional
    public void createCourse(CourseCreateDto request) {
        CourseItem courseItem = new CourseItem();
        courseItem.setObjectID(request.getObjectID());
        courseItem.setSubject(new SelectItem(request.getSubjectId()));
        courseItem.setCourseName(request.getName());
        courseItem.setDuration(request.getDuration());
        courseItem.setNumberData(getNumberData(request));
        if (request.getLocationPrice() != null) {
            HashMap<Integer, BigDecimal> price = request.getLocationPrice().stream()
                    .filter(lp -> lp.getLocationId() != null)
                    .filter(lp -> lp.getPrice() != null)
                    .collect(Collectors.toMap(LocationPriceDto::getLocationId, LocationPriceDto::getPrice, (oldValue, newValue) -> newValue, HashMap::new));
            courseItem.setPricePerLocationStudent(price);

            HashMap<Integer, BigDecimal> stopFee = request.getLocationPrice().stream()
                    .filter(lp -> lp.getLocationId() != null)
                    .filter(lp -> lp.getStopFee() != null)
                    .collect(Collectors.toMap(LocationPriceDto::getLocationId, LocationPriceDto::getStopFee, (oldValue, newValue) -> newValue, HashMap::new));
            courseItem.setStopFeePerLocationStudent(stopFee);
        }
        courseItem.setValidity(request.getValidity());
        courseItem.setDescription(request.getDescription());
        courseItem.setOtherPreRequisites(request.getOtherPrerequisites());
        if (request.getInstructorIds() != null) {
            ArrayList<SelectItem> instructors = request.getInstructorIds().stream()
                    .map(SelectItem::new)
                    .collect(Collectors.toCollection(ArrayList::new));
            courseItem.setInstructors(instructors);
        }
        tcService.saveCourse(courseItem);
    }

    private NumberData getNumberData(CourseCreateDto request) {
        if (request.getNumber() != null) {
            return new NumberData(request.getNumber());
        }
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        Integer intNumber = tcService.getCourseLastIntNumber();
        if (settings == null || settings.getCourseNumberingFormat() == null) {
            return EdsNumberingSettings.getDefaultData(intNumber != null ? intNumber : 0, EdsNumberingSettings.DEF_COURSE_PREFIX);
        }
        return settings.parseNumberData(intNumber != null ? intNumber : 0, settings.getCourseNumberingFormat());
    }

    public Boolean deleteById(Integer courseId) {
        return courseId != null ? tcService.deleteCourse(courseId) : false;
    }
}
