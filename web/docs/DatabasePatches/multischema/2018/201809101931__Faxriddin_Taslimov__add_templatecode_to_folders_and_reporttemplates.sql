
update reporttemplate rt set categorycode =(select code from reporttemplatecategory c where rt.categoryid=c.id);

update "0".folders f set categorycode =(select code from reporttemplatecategory c where f.category_id=c.id);

update "anv".folders f set categorycode =(select code from reporttemplatecategory c where f.category_id=c.id);
