--AWS da urildi !!!!
---+1-809,+1-829
UPDATE  country set telcode='+1-809' WHERE code='DO';
INSERT INTO country(alias, code, name, telcode, currencyid,isactive)
 VALUES ('Dominican Republic;DO;','DO','Dominican Republic','+1-829',(SELECT currencyid from country WHERE code='DO'),true);


--- +1-787,+1-939
update country set telcode='+1-787' where code='PR';
INSERT INTO country(alias, code, name, telcode, currencyid, isactive)
VALUES ('Puerto Rico;PR;','PR','Puerto Rico','+1-939',(SELECT currencyid from country WHERE code='PR'),true);


--- +1-684, +684
update country set telcode='+1-684' where code='AS';
INSERT INTO country(alias, code, name, telcode, currencyid, isactive)
VALUES ('American Samoa;AS;','AS','American Samoa','+684',(SELECT currencyid from country WHERE code='AS'),true);
