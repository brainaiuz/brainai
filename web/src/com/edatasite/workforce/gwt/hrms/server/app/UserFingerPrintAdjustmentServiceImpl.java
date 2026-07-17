package com.edatasite.workforce.gwt.hrms.server.app;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserFingerPrintDevice;
import com.edatasite.workforce.core.domain.EdsUsersFingerPrint;
import com.edatasite.workforce.core.domain.EdsUsersFingerPrintAdjustment;
import com.edatasite.workforce.core.domain.enums.FingerprintSource;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.availability.client.rpc.UserFingerPrintDeviceItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserFingerPrintAdjustment;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintDeviceManager;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintmanager;
import com.edatasite.workforce.gwt.core.server.rpc.FingerPrintUserItem;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rpc.DocumentsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.UserFingerPrintAdjustmentService;
import com.edatasite.workforce.rest.v3.release10.core.to.AttendanceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_EMPLOYEE_ATTENDANCE;

@Service("userFingerPrintAdjustmentService")
public class UserFingerPrintAdjustmentServiceImpl implements UserFingerPrintAdjustmentService, UserFingerPrintAdjustmentServiceLocal {
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final UserFingerPrintAdjustmentManager userFingerPrintAdjustmentManager;
    private final UserFingerPrintDeviceManager userFingerPrintDeviceManager;
    private final GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    private final UserFingerPrintmanager userFingerPrintmanager;
    private final DocumentsService documentsService;

    public UserFingerPrintAdjustmentServiceImpl(UserFingerPrintAdjustmentManager userFingerPrintAdjustmentManager,
                                                UserFingerPrintDeviceManager userFingerPrintDeviceManager,
                                                GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager,
                                                UserFingerPrintmanager userFingerPrintmanager,
                                                DocumentsService documentsService) {
        this.userFingerPrintAdjustmentManager = userFingerPrintAdjustmentManager;
        this.userFingerPrintDeviceManager = userFingerPrintDeviceManager;
        this.globalAuthJdbcSpringManager = globalAuthJdbcSpringManager;
        this.userFingerPrintmanager = userFingerPrintmanager;
        this.documentsService = documentsService;
    }

    @Override
    @Transactional
    public void create(UserFingerPrintAdjustment item) {
        EdsUsersFingerPrint edsUsersFingerPrint = new EdsUsersFingerPrint();
        edsUsersFingerPrint.setStartDate(item.getStartDate().getNonConvertedDate());
        edsUsersFingerPrint.setStatusString(item.getStatusString());
        edsUsersFingerPrint.setDeviceUUID(item.getDeviceUUID());
        edsUsersFingerPrint.setFingerprintId(item.getFingerprintId());
        edsUsersFingerPrint.setTimeSlotId(item.getTimeSlotId());
        edsUsersFingerPrint.setDeviceName(item.getDeviceName());

        Integer currentUserId = userFingerPrintAdjustmentManager.getUser().getObjectID();
        EdsUsersFingerPrintAdjustment edsUsersFingerPrintAdjustment = new EdsUsersFingerPrintAdjustment();
        edsUsersFingerPrintAdjustment.setCreatedBy(currentUserId);
        edsUsersFingerPrintAdjustment.setUpdatedBy(currentUserId);
        edsUsersFingerPrintAdjustment.setUpdatedAt(LocalDateTime.now());
        edsUsersFingerPrintAdjustment.setDescription(item.getDescription());
        edsUsersFingerPrintAdjustment.setFingerprint(edsUsersFingerPrint);
        edsUsersFingerPrintAdjustment.setSource(FingerprintSource.from(item.getSource()));
        userFingerPrintAdjustmentManager.create(edsUsersFingerPrintAdjustment);
    }

    @Override
    public List<UserFingerPrintDeviceItem> getFingerprintDevices(Integer userId) {
        List<EdsUserFingerPrintDevice> fingerPrintDevices = userFingerPrintDeviceManager.getAllByUserIdAndDeviceIds(userId);
        return fingerPrintDevices.stream()
                .map(fpd -> {
                    UserFingerPrintDeviceItem item = new UserFingerPrintDeviceItem();
                    item.setUserId(fpd.getUserId());
                    item.setDeviceId(fpd.getDeviceId());
                    item.setFingerPrintId(fpd.getFingerprintId());
                    return item;
                }).toList();
    }

    @Override
    public List<CompanyDomain> getFingerprintSetup() {
        return globalAuthJdbcSpringManager.getFingerprintSetup(SecurityContext.getCompanyID());
    }

    @Override
    public UserFingerPrintDeviceItem getFingerprintSetupByUserIdAndDeviceId(Integer userId, String deviceId) {
        EdsUserFingerPrintDevice fpd = userFingerPrintDeviceManager.getUserFingerPrintdByUserIdAndDeviceId(userId, deviceId);
        if (fpd == null) return null;
        UserFingerPrintDeviceItem item = new UserFingerPrintDeviceItem();
        item.setUserId(fpd.getUserId());
        item.setDeviceId(fpd.getDeviceId());
        item.setFingerPrintId(fpd.getFingerprintId());
        return item;
    }

    @Override
    public List<UserFingerPrintDeviceItem> getAllByDeviceIdAndFingerprintId(String deviceUuid, String fingerprintId) {
        return userFingerPrintDeviceManager.getAllByDeviceIdAndFingerprintId(deviceUuid, fingerprintId).stream()
                .map(fpd -> {
                    UserFingerPrintDeviceItem item = new UserFingerPrintDeviceItem();
                    item.setUserId(fpd.getUserId());
                    item.setDeviceId(fpd.getDeviceId());
                    item.setFingerPrintId(fpd.getFingerprintId());
                    return item;
                })
                .toList();
    }

    @Override
    public List<UserFingerPrintDeviceItem> getAllByDeviceId(String deviceId) {
        return userFingerPrintDeviceManager.getAllByDeviceId(deviceId).stream()
                .map(fpd -> {
                    UserFingerPrintDeviceItem item = new UserFingerPrintDeviceItem();
                    item.setUserId(fpd.getUserId());
                    item.setDeviceId(fpd.getDeviceId());
                    item.setFingerPrintId(fpd.getFingerprintId());
                    item.setUserName(fpd.getUser().getFirstName() + " " + fpd.getUser().getLastName());
                    return item;
                })
                .toList();
    }

    @Override
    public AttendanceDto getByAttendanceId(Integer attendanceId) {
        EdsUsersFingerPrint edsUsersFingerPrint = userFingerPrintmanager.get(attendanceId);
        if (edsUsersFingerPrint == null) return null;
        EdsUser currentUser = userFingerPrintDeviceManager.getUser();
        if (!currentUser.hasRole("ADMIN")
                && userFingerPrintDeviceManager.getAllByDeviceIdAndFingerprintId(edsUsersFingerPrint.getDeviceUUID(), edsUsersFingerPrint.getFingerprintId()).stream()
                .map(EdsUserFingerPrintDevice::getUserId)
                .noneMatch(currentUser.getObjectID()::equals)) {
            return null;
        }
        AttendanceDto attendanceDto = new AttendanceDto();
        attendanceDto.setDeviceUid(edsUsersFingerPrint.getDeviceUUID());
        attendanceDto.setDeviceName(edsUsersFingerPrint.getDeviceName());
        FingerPrintUserItem fingerPrintUserItem = new FingerPrintUserItem( );
        fingerPrintUserItem.setUserId(edsUsersFingerPrint.getFingerprintId());
        fingerPrintUserItem.setStatus(edsUsersFingerPrint.getStatusString());
        fingerPrintUserItem.setLogTime(SIMPLE_DATE_FORMAT.format(edsUsersFingerPrint.getStartDate()));
        EdsUsersFingerPrintAdjustment adjustment = userFingerPrintAdjustmentManager.getByFingerprint(attendanceId);
        if (adjustment != null) {
            ArrayList<FileResource> fileResources = documentsService.getFileResources(F_EMPLOYEE_ATTENDANCE, adjustment.getObjectID(), adjustment.getObjectID());
            fingerPrintUserItem.setSnapshotLinks(fileResources.stream().map(FileResource::getDownloadUrl).toList());
            fingerPrintUserItem.setNote(adjustment.getDescription());
            fingerPrintUserItem.setLatitude(adjustment.getLatitude());
            fingerPrintUserItem.setLongitude(adjustment.getLongitude());
            fingerPrintUserItem.setSource(adjustment.getSource());
            attendanceDto.setProjectId(adjustment.getProject() != null ? adjustment.getProject().getObjectID() : null);
            attendanceDto.setProjectName(adjustment.getProject() != null ? adjustment.getProject().getName() : null);
            attendanceDto.setTaskId(adjustment.getTask() != null ? adjustment.getTask().getObjectID() : null);
            attendanceDto.setTaskName(adjustment.getTask() != null ? adjustment.getTask().getName() : null);
        }
        attendanceDto.setData(List.of(fingerPrintUserItem));
        return attendanceDto;
    }
}
