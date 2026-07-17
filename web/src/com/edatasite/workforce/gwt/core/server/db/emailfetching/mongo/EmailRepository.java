package com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo;

import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends MongoRepository<EdsEmail, String>, EmailCustomRepository {

    @Query(value = "{ 'generatedGoogleID': ?0, 'companyId': ?1 }")
    EdsEmail findByGeneratedGoogleID(long generatedGoogleID, String companyId);

    @Query(value = "{ 'trackerId': ?0, 'companyId': ?1 }", fields = "{ 'messageId': 1 }")
    List<EdsEmail> findByTrackerId(Integer trackerId, String companyId);

    @Query(value = "{ 'trackerId': ?0, 'companyId': ?1 }")
    List<EdsEmail> findByTrackerIdAndCompanyId(Integer trackerId, String companyId);

    @Query(value = "{ 'messageId': ?0, 'emailSettingId': ?1, 'companyId': ?2, 'trackerId': {'$ne' : null} }")
    List<EdsEmail> findTopByMessageIdAndEmailSettingIdAndCompanyId(String messageId, Integer emailSettingsId, String companyId);

    @Query(value = "{ 'messageId': {'$in': [?0] }, 'emailSettingId': ?1, 'companyId': ?2, 'trackerId': {'$ne' : null} }", fields = "{ 'trackerId': 1 }")
    List<EdsEmail> findAllByMessageIdsAndEmailSettingId(String messageIds, Integer emailSettingsId, String companyId);

    @Query(value = "{ 'emailSettingId': ?0, 'companyId': ?1, 'folderType': 'INBOX', 'read': false, 'deleted': false }", count = true)
    Long getUnreadInboxMessageCount(Integer emailSettingId, String companyId);

    @Query(value = "{ 'emailSettingId': ?0, 'companyId': ?1, 'folderType': 'INBOX', 'read': false, 'deleted': false }")
    List<EdsEmail> getUnreadEmails(Integer emailSettingId, String companyId, Pageable pageable);

    @Query(value = "{ 'emailSettingId': ?0, 'companyId': ?1, 'folderId': ?2, 'deleted': false }")
    List<EdsEmail> getAllEmailsByFolderId(Integer emailSettingId, String companyId, Integer folderId);
}
