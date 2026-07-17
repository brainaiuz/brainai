-- listpanel for Backups employee
delete
from listpanelguidesettings
where paneltype = 'BackupsEmployee';
INSERT INTO listpanelguidesettings (instancename, paneltype, phonenumber, wikiurl, youtubeurl)
VALUES ('backupsEmployee', 'BackupsEmployee', '+44 173 261 7967', 'https://www.kpi.com/blog/wiki_categories/hrms/', 'null');
