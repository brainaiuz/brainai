update "311555".reference_locale set russian = 'Внештатное' where russian = 'Внештатное кол-во' and  id = (select localeid from "311555".reference where code = 'TYPE_EXTERNAL');
