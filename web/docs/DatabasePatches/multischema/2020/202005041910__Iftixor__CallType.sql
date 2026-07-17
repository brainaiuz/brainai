
update "anv".event set outboundCall = true where outboundCall = false and inboundCall = false and missedCall = false  and activityType=2;
