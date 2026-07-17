
update "anv".companycustomfieldssettings set predefinedvalues=replace(predefinedvalues, ',',';'),predefinedvalueswithsorting=replace(predefinedvalueswithsorting, ',',';') where  uitype='DropDown';
