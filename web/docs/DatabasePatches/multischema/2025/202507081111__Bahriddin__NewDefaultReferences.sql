-- Delete existing parents and their children
DELETE
FROM "anv".reference
WHERE parentid IN
      (SELECT id FROM "anv".reference WHERE code IN ('CITIZENSHIP', '_CANDIDATE_HARD_SKILLS','_CANDIDATE_QUESTIONS', '_YES', '_NO'));

DELETE
FROM "anv".reference
WHERE code IN ('CITIZENSHIP', '_CANDIDATE_HARD_SKILLS', '_YES_NO_QUESTION');

-- Insert Candidate Citizenship parent
INSERT INTO "anv".reference (code, isactive, issystemreference, name, shared, sorder, parentid)
VALUES ('CITIZENSHIP', true, true, 'Citizenship', true, 0, null);

-- Insert child for Candidate Citizenship
INSERT INTO "anv".reference (code, isactive, issystemreference, name, shared, sorder, parentid)
SELECT 'UZBEKISTAN',
       true,
       false,
       'Uzbekistan',
       true,
       1,
       id
FROM "anv".reference
WHERE code = 'CITIZENSHIP'
ORDER BY id DESC
LIMIT 1;

INSERT INTO "anv".reference (code, isactive, issystemreference, name, shared, sorder, parentid)
VALUES ('_CANDIDATE_QUESTIONS', true, true, 'Candidate Questions', true, 0, null);


-- Insert Candidate Hard Skills parent
INSERT INTO "anv".reference (code, isactive, issystemreference, name, shared, sorder, parentid)
VALUES ('_CANDIDATE_HARD_SKILLS', true, true, 'Candidate Hard Skills', true, 0, null);

-- Insert child for Candidate Hard Skills
INSERT INTO "anv".reference (code, isactive, issystemreference, name, shared, sorder, parentid)
SELECT 'PROMPT_ENGINEERING',
       true,
       false,
       'Prompt Engineering',
       true,
       1,
       id
FROM "anv".reference
WHERE code = '_CANDIDATE_HARD_SKILLS'
ORDER BY id DESC
LIMIT 1;


-- Insert Yes/No

INSERT INTO "anv".reference (code, isactive, issystemreference, name, shared, sorder, parentid)
VALUES ('_YES_NO_QUESTION', true, true, 'Yes No Questions', true, 0, null);

-- Insert child for Candidate Hard Skills
INSERT INTO "anv".reference (code, isactive, issystemreference, name, shared, sorder, parentid)
SELECT '_YES',
       true,
       false,
       'Yes',
       true,
       1,
       id
FROM "anv".reference
WHERE code = '_YES_NO_QUESTION'
ORDER BY id DESC
LIMIT 1;

INSERT INTO "anv".reference (code, isactive, issystemreference, name, shared, sorder, parentid)
SELECT '_NO',
       true,
       false,
       'No',
       true,
       2,
       id
FROM "anv".reference
WHERE code = '_YES_NO_QUESTION'
ORDER BY id DESC
LIMIT 1;