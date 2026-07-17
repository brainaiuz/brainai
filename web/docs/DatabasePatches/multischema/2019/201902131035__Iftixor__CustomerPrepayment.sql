
  insert into "0".customerPrepaymentNote(comment, commentatorid)
  select note, userid from "0".invoicePayments where note is not null;

  insert into "anv".customerPrepaymentNote(comment, commentatorid)
  select note, userid from "anv".invoicePayments where note is not null;