
update "anv".folder set foldertype=72 where foldertype=71 and name = 'Request for Quote Main Attachments' and
 not exists (select * from "anv".folder where
				   name='Request for Quote Main Attachments' and foldertype=72)