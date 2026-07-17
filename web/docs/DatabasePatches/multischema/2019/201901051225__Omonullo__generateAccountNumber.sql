create or replace function "anv".generateaccountnumber(startnumberrange integer, endnumberrange integer) returns text
  language plpgsql
as
$$
DECLARE accountNumber text; i integer;
BEGIN
  FOR i in startNumberRange .. endNumberRange LOOP
    accountNumber = ''||i;
    IF NOT EXISTS (SELECT * FROM  "anv".account WHERE accountcode = accountNumber) THEN
      return accountNumber;
    END IF;
  END LOOP;
  return null;
END
$$;
alter function "anv".generateaccountnumber(integer, integer) owner to wfmtest;


create or replace function "0".generateaccountnumber(startnumberrange integer, endnumberrange integer) returns text
  language plpgsql
as
$$
DECLARE accountNumber text; i integer;
BEGIN
  FOR i in startNumberRange .. endNumberRange LOOP
    accountNumber = ''||i;
    IF NOT EXISTS (SELECT * FROM  "0".account WHERE accountcode = accountNumber) THEN
      return accountNumber;
    END IF;
  END LOOP;
  return null;
END
$$;
alter function "0".generateaccountnumber(integer, integer) owner to wfmtest;


