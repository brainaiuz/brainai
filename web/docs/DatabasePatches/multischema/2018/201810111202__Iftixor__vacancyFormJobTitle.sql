

update "anv".modelfield set columntype='COL_1', forder=0 where form_id='VACANCY_FORM' and field_id='vacancyJobTitle';
update "anv".modelfield set columntype='COL_2', forder=4 where form_id='VACANCY_FORM' and field_id='proposedSalary';
update "anv".modelfield set columntype='COL_1', forder=1 where form_id='VACANCY_FORM' and field_id='vacancyDescription';
update "anv".modelfield set columntype='COL_1', forder=2 where form_id='VACANCY_FORM' and field_id='jobRequirement';


update "0".modelfield set columntype='COL_1', forder=0 where form_id='VACANCY_FORM' and field_id='vacancyJobTitle';
update "0".modelfield set columntype='COL_2', forder=4 where form_id='VACANCY_FORM' and field_id='proposedSalary';
update "0".modelfield set columntype='COL_1', forder=1 where form_id='VACANCY_FORM' and field_id='vacancyDescription';
update "0".modelfield set columntype='COL_1', forder=2 where form_id='VACANCY_FORM' and field_id='jobRequirement';
