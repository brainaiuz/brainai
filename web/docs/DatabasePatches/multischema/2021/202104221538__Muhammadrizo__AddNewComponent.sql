
/* Add new component */
insert into "0".default_components(
    componentcode, componentname, modules
)values (
    'MY_FAVOURITE_REPORTS', 'My Favourite Reports',
    '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]'
    );

insert into "anv".default_components(
    componentcode, componentname, modules
)values (
            'MY_FAVOURITE_REPORTS', 'My Favourite Reports',
            '["ACCOUNTING","HRMS","MYWORKSPACE","PM","CRM"]'
        );
