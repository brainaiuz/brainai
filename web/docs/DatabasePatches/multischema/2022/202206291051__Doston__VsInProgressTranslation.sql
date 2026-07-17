update "anv".reference_locale
set russian = 'В процессе'
where russian = 'В ходе выполнения'
  and id = (select localeid from "anv".reference where code = 'VS_IN_PROGRESS');