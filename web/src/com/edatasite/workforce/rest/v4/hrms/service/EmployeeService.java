package com.edatasite.workforce.rest.v4.hrms.service;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.gwt.core.client.enums.Gender;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.services.dto.EmployeeItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    private final EmployeeManager employeeManager;
    private final EmployeeDepartmentManager employeeDepartmentManager;
    private final DepartmentManager departmentManager;
    private final PositionManager positionManager;
    private final UploadManager uploadManager;
    private final DepartmentService departmentService;

    public EmployeeService(EmployeeManager employeeManager, EmployeeDepartmentManager employeeDepartmentManager, DepartmentManager departmentManager, PositionManager positionManager, UploadManager uploadManager, DepartmentService departmentService) {
        this.employeeManager = employeeManager;
        this.employeeDepartmentManager = employeeDepartmentManager;
        this.departmentManager = departmentManager;
        this.positionManager = positionManager;
        this.uploadManager = uploadManager;
        this.departmentService = departmentService;
    }


    @Transactional(readOnly = true)
    public List<EmployeeItem> getEmployeesByDepartment(Integer departmentId) {
        if (departmentId == null) {
            log.error("Department ID should not be null");
            throw new RuntimeException("Department ID should not be null");
        }
        EdsDepartment department = Optional.of(departmentManager.get(departmentId)).orElseThrow(() -> {
            log.error("The department with ID {} is not found", departmentId);
            return new RuntimeException(String.format("The department with ID %1$s is not found", departmentId));
        });
        List<EdsEmployee> employees = employeeManager.getActiveTeamEmployees(departmentId);
        List<EmployeeItem> employeesAsItems = getEmployeesAsItems(employees, false);
        EdsEmployee leader = department.getLeader();
        if (leader != null) {
            for (EmployeeItem employee : employeesAsItems) {
                if (Objects.equals(employee.getId(), department.getLeader().getObjectID())) {
                    employee.setLeader(true);
                }
            }
        }
        return employeesAsItems;
    }

    @Transactional(readOnly = true)
    public List<EmployeeItem> getEmployeesByPosition(Integer positionId) {
        if (positionId == null) {
            log.error("PositionId ID should not be null");
            throw new RuntimeException("PositionId ID should not be null");
        }
        EdsPosition position = Optional.of(positionManager.get(positionId)).orElseThrow(() -> {
            log.error("The position with ID {} is not found", positionId);
            return new RuntimeException(String.format("The position with ID %1$s is not found", positionId));
        });

        List<EdsEmployee> employees = employeeManager.getPositionEmployees(positionId);
        List<EmployeeItem> employeesAsItems = getEmployeesAsItems(employees, false);
        EdsDepartment department = position.getDepartmentObject();
        if (department != null && department.getLeader() != null) {
            for (EmployeeItem employee : employeesAsItems) {
                if (Objects.equals(employee.getId(), department.getLeader().getObjectID())) {
                    employee.setLeader(true);
                }
            }
        }
        return employeesAsItems;
    }

    @Transactional(readOnly = true)
    public List<EmployeeItem> getAllEmployees() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setShowActive(true);
        fp.setEmployeeListForVacant(true);
        List<EdsEmployee> list = employeeManager.list(fp);
        return getEmployeesAsItems(list, null);
    }

    private List<EmployeeItem> getEmployeesAsItems(List<EdsEmployee> employees, Boolean vacant) {
        return employees.stream()
                .map(e -> {
                    EmployeeItem item = new EmployeeItem();
                    item.setId(e.getObjectID());
                    item.setName(e.getFullName());
                    item.setVacant(vacant);
                    EdsEmployeeProfile profile = e.getProfile();
                    if (profile != null) {
                        Gender gender = Gender.MALE.equals(e.getProfile().getGender()) ? Gender.MALE : Gender.FEMALE.equals(e.getProfile().getGender()) ? Gender.FEMALE : null;
                        item.setNumber(e.getProfile().getEmployeeCode());
                        item.setGender(gender);
                    }
                    item.setEmail(e.getEmail());
                    item.setPhoneNumber(e.getPrimaryPhone());
                    item.setTgNumber(e.getTgUserName() != null ? e.getTgUserName() : "");
                    if (e.getPhoto() != null) {
                        String url = uploadManager.getFileURL(e.getPhoto(), false);
                        item.setImageUrl(url);
                    }
                    if (e.getPosition() != null) {
                        EdsReferenceLocale locale = e.getPosition().getLocale();
                        String nameLocale = "";
                        if (locale != null) {
                            String lang = ServerUtils.getUserLocale().getLanguage();
                            switch (lang) {
                                case "uz" -> nameLocale = locale.getUzbek();
                                case "en" -> nameLocale = locale.getEnglish();
                                case "ar" -> nameLocale = locale.getArabic();
                                case "ru" -> nameLocale = locale.getRussian();
                            }
                        } else {
                            nameLocale = e.getPosition().getName();
                        }
                        item.setPosition(nameLocale);
                    }
                    return item;
                }).collect(Collectors.toList());
    }

    @Transactional
    public List<EmployeeItem> assignManager(Integer employeeId, Integer departmentId) {
        EdsEmployee employee = Optional.of(employeeManager.get(employeeId)).orElseThrow(() -> {
            log.error("=========== Employee {} is not found! ===========", employeeId);
            return new RuntimeException("=========== Employee is not found! ===========");
        });

        EdsDepartment department = Optional.of(departmentManager.get(departmentId)).orElseThrow(() -> {
            log.error("=========== Department {} is not found! ===========", departmentId);
            return new RuntimeException("=========== Department is not found! ===========");
        });

        department.setLeader(employee);
        departmentManager.update(department);

        List<EdsEmployee> employees = employeeManager.getActiveTeamEmployees(departmentId);
        List<EmployeeItem> employeesAsItems = getEmployeesAsItems(employees, false);
        if (employee != null) {
            for (EmployeeItem emp : employeesAsItems) {
                if (Objects.equals(emp.getId(), department.getLeader().getObjectID())) {
                    emp.setLeader(true);
                }
            }
        }
        return employeesAsItems;
    }

    @Transactional
    public List<EmployeeItem> unAssignManager(Integer employeeId, Integer departmentId) {
        EdsEmployee employee = Optional.of(employeeManager.get(employeeId)).orElseThrow(() -> {
            log.error("=========== Employee {} is not found! ===========", employeeId);
            return new RuntimeException("=========== Employee is not found! ===========");
        });

        EdsDepartment department = Optional.of(departmentManager.get(departmentId)).orElseThrow(() -> {
            log.error("=========== Department {} is not found! ===========", departmentId);
            return new RuntimeException("=========== Department is not found! ===========");
        });

        department.setLeader(null);
        departmentManager.update(department);

        List<EdsEmployee> employees = employeeManager.getActiveTeamEmployees(departmentId);
        return getEmployeesAsItems(employees, false);
    }

    @Transactional
    public List<EmployeeItem> removeFromDepartment(Integer employeeId, Integer departmentId, Boolean vacant) {
        EdsEmployee emp = Optional.of(employeeManager.get(employeeId)).orElseThrow(() -> {
            log.error("=========== Employee {} is not found! ===========", employeeId);
            return new RuntimeException("=========== Employee is not found! ===========");
        });

        EdsDepartment department = Optional.of(departmentManager.get(departmentId)).orElseThrow(() -> {
            log.error("=========== Department {} is not found! ===========", departmentId);
            return new RuntimeException("=========== Department is not found! ===========");
        });

        List<EdsEmployee> employees = new ArrayList<>();

        if (vacant) {
            List<EdsEmployee> vacants = department.getVacants();
            vacants.remove(emp);
            departmentManager.update(department);
            employees = departmentManager.getVacants(departmentId);
        } else {
            // INFO: Bu method ishlatilmaydi, hodimni departmentdan delete qilish ratatsiya orqalik bo'ladi
//            EdsEmployeeDepartment employeeDepartment = Optional.of(employeeDepartmentManager.getByEmployeeId(employeeId)).orElseThrow(() -> {
//                log.error("=========== Employee Department with employee id {} is not found! ===========", employeeId);
//                return new RuntimeException("=========== Employee Department with employee id is not found! ===========");
//            });
//
//            employeeDepartmentManager.deleteEmployeeDepartment(employeeDepartment);
//            employees = employeeManager.getActiveTeamEmployees(departmentId);
        }

        List<EmployeeItem> employeesAsItems = getEmployeesAsItems(employees, vacant);
        if (!vacant) {
            EdsEmployee leader = department.getLeader();
            if (leader != null) {
                for (EmployeeItem employee : employeesAsItems) {
                    if (Objects.equals(employee.getId(), department.getLeader().getObjectID())) {
                        employee.setLeader(true);
                    }
                }
            }
        }
        return employeesAsItems;
    }

    @Transactional
    public List<EmployeeItem> addEmployeeToDepartment(Integer employeeId, Integer departmentId) {
        Optional.of(employeeManager.get(employeeId)).orElseThrow(() -> {
            log.error("=========== Employee {} is not found! ===========", employeeId);
            return new RuntimeException("=========== Employee is not found! ===========");
        });
        EdsDepartment department = Optional.of(departmentManager.get(departmentId)).orElseThrow(() -> {
            log.error("=========== Department {} is not found! ===========", departmentId);
            return new RuntimeException("=========== Department is not found! ===========");
        });
        HashSet<Integer> employeeSet = new HashSet<>(employeeId);
        employeeSet.add(employeeId);
        departmentService.saveEmployeeDepartment(employeeSet, departmentId, true, true,false);
        List<EdsEmployee> employees = employeeManager.getActiveTeamEmployees(departmentId);
        List<EmployeeItem> employeesAsItems = getEmployeesAsItems(employees, false);
        EdsEmployee leader = department.getLeader();
        if (leader != null) {
            for (EmployeeItem employee : employeesAsItems) {
                if (Objects.equals(employee.getId(), department.getLeader().getObjectID())) {
                    employee.setLeader(true);
                }
            }
        }
        return employeesAsItems;
    }

    @Transactional
    public List<EmployeeItem> addVacantToDepartment(Integer employeeId, Integer departmentId) {
        EdsEmployee employee = Optional.of(employeeManager.get(employeeId)).orElseThrow(() -> {
            log.error("=========== Employee {} is not found! ===========", employeeId);
            return new RuntimeException("=========== Employee is not found! ===========");
        });

        EdsDepartment department = Optional.of(departmentManager.get(departmentId)).orElseThrow(() -> {
            log.error("=========== Department {} is not found! ===========", departmentId);
            return new RuntimeException("=========== Department is not found! ===========");
        });

        department.getVacants().add(employee);
        departmentManager.update(department);

        List<EdsEmployee> employees = departmentManager.getVacants(departmentId);
        return getEmployeesAsItems(employees, true);
    }

    @Transactional(readOnly = true)
    public List<EmployeeItem> getVacantsByDepartment(Integer departmentId) {
        Optional.of(departmentManager.get(departmentId)).orElseThrow(() -> {
            log.error("=========== Department {} is not found! ===========", departmentId);
            return new RuntimeException("=========== Department is not found! ===========");
        });

        List<EdsEmployee> employees = departmentManager.getVacants(departmentId);
        return getEmployeesAsItems(employees, true);
    }
}
