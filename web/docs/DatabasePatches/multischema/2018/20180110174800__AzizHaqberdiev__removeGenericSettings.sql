delete from "anv".genericSettings where key = 'SELECT_ALL_ITEMS_FROM_LISTING';
delete from "0".genericSettings where key = 'SELECT_ALL_ITEMS_FROM_LISTING';

delete from "anv".myModule where code = 'EMAIL_MARKETING';
delete from "0".myModule where code = 'EMAIL_MARKETING';

update "anv".mailList set deleted = false where deleted is null;