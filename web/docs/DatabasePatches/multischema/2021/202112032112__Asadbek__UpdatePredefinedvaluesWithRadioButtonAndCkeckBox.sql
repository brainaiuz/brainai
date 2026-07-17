update "anv".companycustomfieldssettings SET predefinedvalues = REPLACE(predefinedvalues,',' , '-:-') where uitype='RadioButton';
update "anv".companycustomfieldssettings SET predefinedvalueswithsorting = REPLACE(predefinedvalueswithsorting,',' , '-:-') where uitype='RadioButton';


update "anv".companycustomfieldssettings SET predefinedvalues = REPLACE(predefinedvalues,',' , '-:-') where uitype='CheckBox';
update "anv".companycustomfieldssettings SET predefinedvalueswithsorting = REPLACE(predefinedvalueswithsorting,',' , '-:-') where uitype='CheckBox';


