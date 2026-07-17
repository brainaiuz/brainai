-- create, insert, drop

-- PUBLIC Scheme
-- CREATE TABLE IF NOT EXISTS fake_table(id integer);
-- INSERT INTO fake_table(id) VALUES(1),(2),(3),(4),(5);
-- DROP TABLE IF EXISTS fake_table;
--=========================================================


-- "0"
-- CREATE TABLE IF NOT EXISTS "0".fake_table(id integer);
-- INSERT INTO "0".fake_table(id) VALUES(1),(2),(3),(4),(5);
-- DROP TABLE IF EXISTS "0".fake_table;
--INSERT INTO "0".fake_table(id) VALUES(1),(2),(3),(4),(5); -- Raises error
--=========================================================


-- PRIVATE Schemes
-- CREATE TABLE IF NOT EXISTS "anv".fake_table(id integer);
-- INSERT INTO "anv".fake_table(id) VALUES(1),(2),(3),(4),(5);
-- DROP TABLE IF EXISTS "anv".fake_table;
-- INSERT INTO "anv".fake_table(id) VALUES(1),(2),(3),(4),(5); -- Raises error

--=========================================================
