update "anv".mailMessage set isHtml = false where isHtml is null;
update "anv".mailMessage set deleted = false where deleted is null;
update "anv".leadmaillist set deleted = false where deleted is null;