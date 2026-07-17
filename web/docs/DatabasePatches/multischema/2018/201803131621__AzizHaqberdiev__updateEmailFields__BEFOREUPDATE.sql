update "anv".emails set unread = true where unread is null;
update "anv".emails set hasAttachment = false where hasAttachment is null;
update "anv".emails set fetched = true where fetched is null;
update "anv".emails set deleted = false where deleted is null;