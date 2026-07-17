
delete from "anv".model where formid='VACANCY_FORM';
insert into "anv".model (formid, title, viewname, active) values('VACANCY_FORM', 'Vacancy Form', 'Vacancy', true);