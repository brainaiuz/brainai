----- ASK @Omonullo BEFORE APPLYING
----- ASK @Omonullo BEFORE APPLYING
----- ASK @Omonullo BEFORE APPLYING
----- ASK @Omonullo BEFORE APPLYING
----- ASK @Omonullo BEFORE APPLYING
----- ASK @Omonullo BEFORE APPLYING
----- ASK @Omonullo BEFORE APPLYING
UPDATE "59422".saleinvoice si
SET fourdigitnumber = (SELECT max(si2.fourdigitnumber)
                       FROM "59422".saleinvoice si2
                         JOIN "59422".invoice i2 ON i2.id = si2.id
                       WHERE i2.iscreditnote = TRUE
                             AND i2.creationdate > (SELECT
                                                      (CASE
                                                       WHEN (invs.numberingrestartenabled = TRUE) THEN (('2017-'||(invs.numberingrestartmonth+1)||'-'||invs.numberingrestartdate)::date)
                                                       ELSE '1900-01-01' END)
                                                    FROM "59422".invoicingsettings invs)
                       )
FROM "59422".invoice i

WHERE i.id = si.id AND i.iscreditnote = TRUE
      AND i.creationdate > (SELECT
                               (CASE
                                WHEN (invs.numberingrestartenabled = TRUE) THEN (('2017-'||(invs.numberingrestartmonth+1)||'-'||invs.numberingrestartdate)::date)
                                ELSE '1900-01-01' END)
                            FROM "59422".invoicingsettings invs)
