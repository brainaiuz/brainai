 CREATE OR REPLACE FUNCTION "anv".connectby(
    integer,
    integer,
    integer,
    text,
    integer)
  RETURNS SETOF record AS
'$libdir/tablefunc', 'connectby_text'
  LANGUAGE c STABLE STRICT
  COST 1
  ROWS 1000;
ALTER FUNCTION "anv".connectby(text, text, text, text, integer)
  OWNER TO postgres;
