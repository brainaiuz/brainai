update "anv".opportunity opp set quantitytotal = (select sum(item.qty) from "anv".opportunity_item item where item.opportunity_id = opp.id);
