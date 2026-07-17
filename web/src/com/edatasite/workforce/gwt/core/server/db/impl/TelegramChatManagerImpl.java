package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTelegramChat;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.TelegramConstants;
import com.edatasite.workforce.gwt.core.server.db.TelegramChatManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 25.05.2017 0:50
 */
@Repository("telegramChatManager")
public class TelegramChatManagerImpl extends BaseManager<EdsTelegramChat> implements TelegramChatManager {
    public TelegramChatManagerImpl() {
        super(EdsTelegramChat.class);
    }

    @Override
    public Integer getListCount(ListingFilterParameter fp) {
        boolean hasSearch = !StringUtils.isEmpty(fp.getSearchKey());
        String sqlQuery = "select count(ch.id) from EdsTelegramChat ch " +
                " where ch.deleted = false ";
        if (hasSearch) {
            sqlQuery += " and lower(ch.chatName) like :searchKey ";
        }
        TypedQuery<Long> query = slaveEntityManager.createQuery(sqlQuery, Long.class);
        if (hasSearch) {
            query = query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }
        List<Long> list = query.setMaxResults(1).getResultList();

        return list.isEmpty() ? 0 : list.get(0).intValue();
    }

    @Override
    public List<EdsTelegramChat> getList(ListingFilterParameter fp) {
        boolean hasSearch = !StringUtils.isEmpty(fp.getSearchKey());
        boolean hasSort = !StringUtils.isEmpty(fp.getSortField());
        String sqlQuery = "select ch from EdsTelegramChat ch " +
                " where ch.deleted = false ";
        if (fp.getAccessToken() != null && !fp.getAccessToken().isEmpty()) {
            sqlQuery += " and ch.telegramBotToken =:accessToken ";
        }

        if (hasSearch) {
            sqlQuery += " and lower(ch.chatName) like :searchKey ";
        }
        if (hasSort) {
            switch (fp.getSortField()) {
                case "chatName" -> sqlQuery += " order by ch.chatName ";
                case "chatType" -> sqlQuery += " order by ch.chatType ";
                case "creator" -> sqlQuery += " order by ch.creator.firstName||' '||ch.creator.lastName ";
                case "active" -> sqlQuery += " order by ch.active ";
                default -> sqlQuery += " order by ch.id ";
            }
            sqlQuery += fp.isAscending() ? "asc" : "desc";
        } else {
            sqlQuery += " order by ch.id desc ";
        }

        TypedQuery<EdsTelegramChat> query = slaveEntityManager.createQuery(sqlQuery, EdsTelegramChat.class)
                .setMaxResults(fp.getLimit())
                .setFirstResult(fp.getStart());
        if (hasSearch) {
            query = query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }

        if (fp.getAccessToken() != null && !fp.getAccessToken().isEmpty()) {
            query = query.setParameter("accessToken", fp.getAccessToken());
        }
        return query.getResultList();
    }

    @Override
    public EdsTelegramChat getByChatId(Long chatId) {
        return (EdsTelegramChat) findSingle("select bl from EdsTelegramChat bl where bl.chatId=?", chatId);
    }

    @Override
    public EdsTelegramChat getById(Integer id) {
        return (EdsTelegramChat) findSingle("select bl from EdsTelegramChat bl where bl.id=?", id);
    }

    @Override
    public EdsTelegramChat getByChatIdAndBotToken(Long chatId, String botToken) {
        return (EdsTelegramChat) findSingle("select bl from EdsTelegramChat bl where bl.chatId=? and bl.telegramBotToken=?", chatId, botToken);
    }

    @Override
    public List<EdsTelegramChat> getActiveChatsByType(String messageType) {
        StringBuilder sql = new StringBuilder();
        sql.append("select tch from EdsTelegramChat tch ");
        sql.append(" where tch.active is true ");
        if (TelegramConstants.SEND_CASE_CREATE.equals(messageType)) {
            sql.append(" and tch.sendCaseCreate is true ");
        }
        sql.append(" order by tch.id ");
        return find(sql.toString());
    }

    @Override
    public void deleteTelegramChatsRuleIds(Integer ruleId) {
        update("update EdsTelegramChat set edsTelegramReportingScheduleRule=null where edsTelegramReportingScheduleRule.objectId=?", ruleId);
    }
}
