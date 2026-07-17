UPDATE accounttemplate SET key = cast(codestring as INT) WHERE codestring = '4905';
UPDATE "0".account SET key = cast(codestring as INT) WHERE key is null and codestring = '4905';
UPDATE "anv".account SET key = cast(codestring as INT) WHERE key is null and codestring = '4905';