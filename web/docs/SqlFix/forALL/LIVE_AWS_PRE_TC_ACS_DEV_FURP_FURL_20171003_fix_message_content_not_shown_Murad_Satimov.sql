update "48079".mailmessage set content = messagehtml where messagehtml is not null and messagehtml <> ''
AND (content is null OR content = '');
update "anv".mailmessage set content = messagehtml where messagehtml is not null and messagehtml <> ''
AND (content is null OR content = '');