package com.edatasite.workforce.gwt.core.client.enums;
public enum GroupTranslation {
    ONE_OFFS("One-offs", "Разовые пользователи", "Bir martalik foydalanuvchilar"),
    CALENDAR_EDITORS("Calendar Editors", "Редакторы календаря", "Kalendar tahrirlovchilari"),
    CALENDAR_VIEWERS("Calendar Viewers", "Просмотр календаря", "Kalendar ko‘ruvchilari"),
    TIMESHEET_EDITORS("Timesheet Editors", "Редакторы табеля", "Ish vaqtini tahrirlovchilar"),
    ADMINISTRATORS("Administrators", "Администраторы", "Administratorlar"),
    MEMBERS("Members", "Участники", "A’zolar"),
    CLIENTS("Clients", "Клиенты", "Mijozlar"),
    DIRECTORS("Directors", "Директора", "Direktorlar"),
    PROJECT_MANAGERS("Project Managers", "Менеджеры проектов", "Loyiha menejerlari"),
    DEPARTMENT_LEADERS("Department Leaders", "Руководители отделов", "Bo‘lim rahbarlari"),
    ADMIN_LOCATIONS("Admin Locations", "Администраторы локаций", "Lokatsiya administratorlari"),
    HRS("Human Resources", "Отдел кадров", "Kadrlar bo‘limi"),
    ACCOUNTANTS("Accountants", "Бухгалтеры", "Buxgalterlar"),
    SALESMEN("Salesmen", "Продавцы", "Sotuvchilar"),
    CUSTOMER_SERVICE_REPRESENTATIVES("Customer Service Representatives", "Представители службы поддержки клиентов", "Mijozlarga xizmat ko‘rsatish vakillari"),
    SALESPERSONS("Salespersons", "Специалисты по продажам", "Savdo mutaxassislari");

    private final String en;
    private final String ru;
    private final String uz;

    GroupTranslation(String en, String ru, String uz) {
        this.en = en;
        this.ru = ru;
        this.uz = uz;
    }


    public static String getTranslatedName(String groupKey, String language) {
        if (groupKey == null) return null;

        try {
            GroupTranslation translation = GroupTranslation.valueOf(groupKey.toUpperCase());

             switch (language.toLowerCase()) {
                case "ru": return translation.ru;
                case "uz": return translation.uz;
                case "en":
                default: return translation.en;
            }
        } catch (IllegalArgumentException e) {
            return groupKey;
        }
    }
}