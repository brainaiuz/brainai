INSERT INTO defaultlayout_new_ui (active, formid, layout, title, addform, editform, viewform, forclient, validations, webform, importform, sectionid, sections)
  SELECT active, formid, layout, title, addform, editform, viewform, forclient, validations, webform, importform, sectionid, sections FROM defaultlayout;
