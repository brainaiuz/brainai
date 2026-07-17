

update "anv".form_item_table_setting it set form_id = (select cf.form_id from "anv".custom_form cf where it.formid = cf.id)
