delete from "anv".model where formid = 'BRIGADA_FORM' and title = 'brigada';

update "anv".model set title = 'Shift List View', viewname = 'ShiftList' where formid = 'SHIFT_FORM';
