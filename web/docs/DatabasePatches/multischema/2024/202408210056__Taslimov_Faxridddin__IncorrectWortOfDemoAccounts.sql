update "anv".companyCustomFieldsSettings set active=true where active is null;

update "anv".model set active=true where active is null;
update "anv".model set stepform=false where stepform is null;
update "anv".model set certificateform=false where certificateform is null;
update "anv".model set customform=false where customform is null;
update "anv".model set quizform=false where anonymousform is null;
update "anv".model set anonymousForm=false where anonymousform is null;

update "anv".modelfield set disableupdate=false where disableupdate is null;
update "anv".modelfield set fullwidth = false where fullwidth is null;
update "anv".modelfield set hide = false where hide is null;
update "anv".modelfield set hideincustomizeform=false where hideincustomizeform is null;
update "anv".modelfield set iscustomfield= false where iscustomfield is null;
update "anv".modelfield set isentityfield= false where isentityfield is null;
update "anv".modelfield set isworkflowattribute=false where isworkflowattribute is null;
update "anv".modelfield set mandatory= false where mandatory is null;
update "anv".modelfield set split = false where split is null;
update "anv".modelfield set systemdisable =false where systemdisable is null;
update "anv".modelfield set systemmandatory = false where systemmandatory is null;
update "anv".modelfield set usablebyworkflow = false where usablebyworkflow is null;
update "anv".modelfield set customizabletable =false where customizabletable is null;
update "anv".modelfield set deleted = false where deleted is null; 
