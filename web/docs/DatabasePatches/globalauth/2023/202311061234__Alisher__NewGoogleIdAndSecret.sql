update hostbasedsetting  set
                             oauth2ConsumerKey = '508805038834-snuqpahkvtkvjlfudk611plkbn9c25hm.apps.googleusercontent.com',
                             oauth2ConsumerSecret = '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'
where hostname in ('aws.kpi.com', 'apps.kpi.com', 'accounts.kpi.com');
