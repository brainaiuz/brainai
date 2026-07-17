insert into "anv".reference (code, name, sorder, parentid)
values ('_WORKFLOW_MODULE_PLACEMENT', 'Placement', (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')),
        (select id from "anv".reference where code = '_WORKFLOW_MODULE'));
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'PLACEMENT_FORM'
  and field_id = 'placementcandidate';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'PLACEMENT_FORM'
  and field_id = 'placementDateOffer';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'PLACEMENT_FORM'
  and field_id = 'placementDepartment';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'PLACEMENT_FORM'
  and field_id = 'placementLocation';
update "anv".modelfield
set usablebyworkflow = true
where form_id = 'PLACEMENT_FORM'
  and field_id = 'placementPosition';