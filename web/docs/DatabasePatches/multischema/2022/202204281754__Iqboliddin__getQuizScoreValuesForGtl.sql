create
or replace function getQuizScoreValues( fieldname varchar, answer varchar, entitycategoryname varchar)
returns varchar as $$
select (case when ccfs.quizformscorevalues = '' then null else ccfs.quizformscorevalues end)::json->>$2
FROM "anv".companycustomfieldssettings ccfs
where ccfs.fieldname = $1
  and ccfs.entitycategoryname = $3;
$$language
sql;