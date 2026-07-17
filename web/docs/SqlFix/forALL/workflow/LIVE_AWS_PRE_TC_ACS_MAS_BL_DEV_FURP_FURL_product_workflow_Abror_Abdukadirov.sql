
delete from "anv".reference  where code = '_WORKFLOW_MODULE_PRODUCT';
insert into "anv".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('_WORKFLOW_MODULE_PRODUCT', false, true, 'Product', true, (select max(sorder) from "anv".reference where parentid = (select id from "anv".reference where code = '_WORKFLOW_MODULE')), (select id from "anv".reference where code='_WORKFLOW_MODULE'), true);

delete from "0".reference  where code = '_WORKFLOW_MODULE_PRODUCT';
insert into "0".reference(code, deleted, isremovable, name, shared, sorder, parentid, isactive)
							values('_WORKFLOW_MODULE_PRODUCT', false, true, 'Product', true, (select max(sorder) from "0".reference where parentid = (select id from "0".reference where code = '_WORKFLOW_MODULE')), (select id from "0".reference where code='_WORKFLOW_MODULE'), true);


delete from "model"  where formid = 'PRODUCT_FORM';
delete from "modelfield"  where form_id = 'PRODUCT_FORM';

insert into model(active, formid, title, viewname) values(true, 'PRODUCT_FORM', 'Product Form', 'Product');

insert into modelfield(form_ID, 	field_ID, 	 						sorder, widget , 				source, 													    usableByWorkflow, 	  type, 		disableUpdate, isWorkflowAttribute) values
											('PRODUCT_FORM', 'NAME',        					 1,			'TextBox', 	    null,  														    true,								  'Text',	  false,          false),
											('PRODUCT_FORM', 'NUMBER',								 2, 		'TextBox', 		  null,                                 true, 							  'Text',	  true,           false),
											('PRODUCT_FORM', 'CATEGORY',							 3, 		'DropDown', 		'ACCOUNTING@PRODUCT_CATEGORY',        true, 							  'Text',	  true,           false),
											('PRODUCT_FORM', 'BRAND',					         4,     'DropDown', 		'ACCOUNTING@PRODUCT_BRAND',           true, 							  'Text',	  true,           false),
											('PRODUCT_FORM', 'DESCRIPTION',					   5,     'TextBox', 		  null,                                 true, 							  'Text',	  false,          false),
											('PRODUCT_FORM', 'COST_PRICE',					   6,     'TextBox', 		  null,                                 true, 							  'Number',	  false,          false),
											('PRODUCT_FORM', 'SELLING_PRICE',					 6,     'TextBox', 		null,                                 true, 							  'Number',	  false,          false),
											('PRODUCT_FORM', 'COGS_ACCOUNT',					 6,     'DropDown', 		'ACCOUNTING@COGS_ACCOUNT',           true, 							  'Text',	  false,          false),
											('PRODUCT_FORM', 'ASSET_ACCOUNT',					 6,     'DropDown', 		'ACCOUNTING@ASSET_ACCOUNT',           true, 							  'Text',	  false,          false);
