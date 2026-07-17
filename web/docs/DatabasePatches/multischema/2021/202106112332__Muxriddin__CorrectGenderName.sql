DROP function if EXISTS "anv".genderCorrect();
CREATE OR replace function "anv".genderCorrect()
    returns void AS
$$
DECLARE fold record;
BEGIN

    FOR fold IN (select id, gender from "anv".employeeprofile where gender is not null and lower(gender) != 'male' and lower(gender) != 'female')
        loop
            if(fold.gender='Мужской') or (fold.gender='Erkak') or (fold.gender='ذكر') then update "anv".employeeprofile set gender = 'Male' where id = fold.id;
			elsif(fold.gender='Женский') or (fold.gender='Ayol') or (fold.gender='أنثى') then update "anv".employeeprofile set gender = 'Female' where id = fold.id;
			END if;
        END loop;
END;
$$
    LANGUAGE plpgsql;
ALTER function "anv".genderCorrect() owner TO postgres;

UPDATE company SET selectFunctioncolumn =(SELECT "anv".genderCorrect()) WHERE  id=(SELECT id FROM company LIMIT 1);
