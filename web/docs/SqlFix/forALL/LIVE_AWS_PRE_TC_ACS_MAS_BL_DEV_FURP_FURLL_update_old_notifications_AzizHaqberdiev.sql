update "anv".notification_msg set read=true, clicked=true where entity_type = 'LeaveRequests' and entity_id in 
(select sr.id from "anv".SickRequest sr left join "anv".reference r ON sr.overallStatus = r.id where r.code in ('DENIED','SS_APPROVED'));

update "anv".notification_msg set read=true, clicked=true where entity_type = 'ExpenseClaim' and entity_id in 
(select sr.id from "anv".expenseReport sr left join "anv".reference r ON sr.overallStatus = r.id 
where sr.isDeleted is true or sr.isDeleted is null or r.code in ('EXPENSE_DECLINED','EXPENSE_APPROVED'));

update "anv".notification_msg set read=true, clicked=true where entity_type = 'CashAdvance' and entity_id in 
(select sr.id from "anv".cashAdvance sr left join "anv".reference r ON sr.overallStatus = r.id 
where r.code in ('REJECTED','APPROVED'));