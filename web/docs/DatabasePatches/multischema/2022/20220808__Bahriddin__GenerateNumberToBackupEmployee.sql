-- generate backups employee code BCE001

update "anv".pmnumberingsettings
set backupsEmployeeNumberingFormat         = 'prefixP:BCE/numbersP:0001/suffixP:false/',
    delimetrBackupsEmployeeNumberingFormat = ''
where id = 1;
