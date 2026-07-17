INSERT INTO "anv".reporting (code, description, fakereport, name, permissioncode, showhide, sorder,
                                targetlink, viewcode, viewname, folderid)
VALUES ('Trial_Balance_Detailed',
        'This report summarizes the debit and credit balances of each account on your chart of accounts during a period of time.',
        true, 'Trial Balance (Detailed)', 'REPORTING_SAVED_REPORT_Trial_Balance', true, 5,
        'Accounting.html#detailedreport|trialBalance/0', 'ACCOUNTING FAKE REPORT',
        'Accounting Fake Report',
        (select id from "anv".folders where categorycode = 'ACCOUNTING' and code = 'FINANCIALSTATEMENTS'));
