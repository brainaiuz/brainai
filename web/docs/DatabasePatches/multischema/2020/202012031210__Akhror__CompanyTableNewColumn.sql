update company c set localecode = (select languagecode from locale l where l.id = c.localeid);
alter table company drop column localeid;