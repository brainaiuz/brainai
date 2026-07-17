update "anv".paymentmethod
set code='CASH'
where name = 'Cash';

update "anv".paymentmethod
set code='CREDIT_CARD'
where name = 'Credit Card';

update "anv".paymentmethod
set code='DEBIT_CARD'
where name = 'Debit Card';

update "anv".paymentmethod
set code='WIRE_TRANSFER'
where name = 'Wire Transfer';

update "anv".paymentmethod
set code='CHEQUE'
where name = 'Cheque';

update "anv".paymentmethod
set code='BANK_TRANSFER'
where name = 'Bank Transfer'

