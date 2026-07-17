update "0_template".companyCustomFieldsSettings set active=true where active is null;

update "0_template".model set active=true where active is null;
update "0_template".model set stepform=false where stepform is null;
update "0_template".model set certificateform=false where certificateform is null;
update "0_template".model set customform=false where customform is null;
update "0_template".model set quizform=false where anonymousform is null;
update "0_template".model set anonymousForm=false where anonymousform is null;

update "0_template".modelfield set disableupdate=false where disableupdate is null;
update "0_template".modelfield set fullwidth = false where fullwidth is null;
update "0_template".modelfield set hide = false where hide is null;
update "0_template".modelfield set hideincustomizeform=false where hideincustomizeform is null;
update "0_template".modelfield set iscustomfield= false where iscustomfield is null;
update "0_template".modelfield set isentityfield= false where isentityfield is null;
update "0_template".modelfield set isworkflowattribute=false where isworkflowattribute is null;
update "0_template".modelfield set mandatory= false where mandatory is null;
update "0_template".modelfield set split = false where split is null;
update "0_template".modelfield set systemdisable =false where systemdisable is null;
update "0_template".modelfield set systemmandatory = false where systemmandatory is null;
update "0_template".modelfield set usablebyworkflow = false where usablebyworkflow is null;
update "0_template".modelfield set customizabletable =false where customizabletable is null;
update "0_template".modelfield set deleted = false where deleted is null; 
