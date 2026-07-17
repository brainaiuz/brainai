CREATE TABLE massMailParams
(
  id                    SERIAL                      NOT NULL,
  companyHost           CHARACTER VARYING(255)      UNIQUE,
  companyId             INTEGER                     UNIQUE,

  host                  CHARACTER VARYING(255)      NOT NULL,
  port                  CHARACTER VARYING(255)      NOT NULL,
  login                 CHARACTER VARYING(255)      NOT NULL,
  password              CHARACTER VARYING(255)      NOT NULL,
  bouncedEmail          CHARACTER VARYING(255)      NOT NULL,
  bouncedPassword       CHARACTER VARYING(255)      NOT NULL,
  abuseEmail            CHARACTER VARYING(255)      NOT NULL,
  tolerateText          CHARACTER VARYING(255)      NOT NULL,
  tolerateHtml          CHARACTER VARYING(255)      NOT NULL,
  unsubscribeText       CHARACTER VARYING(255)      NOT NULL,
  unsubscribeHtml       CHARACTER VARYING(255)      NOT NULL,
  ssl                   boolean                 DEFAULT true,
  smtpAuth              boolean                 DEFAULT true
) WITH (
OIDS = FALSE
);

ALTER TABLE massMailParams OWNER TO wftauth;

insert into massMailParams (companyHost,companyID,host,port,login,password,bouncedEmail,bouncedPassword,abuseEmail,tolerateText,tolerateHtml,unsubscribeText,unsubscribeHtml)
  values
  ('app.kpi.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.kpi.com','E9xv#PX@8F$cg','bounced@mailer.kpi.com','gE9xv@PX#8F$cgE','abuse@kpi.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.1erp.sa',null,'mail.1erp.sa','25','no-reply@1erp.sa','secret!2#','no-reply@1erp.sa','secret!2#','abuse@1erp.sa','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.activira.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.activira.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','support@activira.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('apps.alkawader.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.alkawader.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@alkawader.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('almasaood.kpi.com',null,'secure.emailsrvr.com','25','hrms@masaood.com','H@u890#?1','hrms@masaood.com','H@u890#?1','abuse@masaood.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('alraha.kpi.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.alraha.kpi.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@alraha.kpi.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('saifbelhasa.kpi.com',null,'smtp.gmail.com','587','belhasamailer@gmail.com','yAw3$9?gx4Awu5','hrsystem@saifbelhasagroup.com','hr@2017','abuse@saifbelhasagroup.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.b2xcg.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.b2xerp.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@b2xerp.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('kpi.developmentlogix.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.developmentlogix.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@developmentlogix.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.ebmconsultant.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.ebmconsultant.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@ebmconsultant.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.enfion.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.ebmconsultant.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@enfion.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.financialit.net',null,'smtp-mailer.kpi.com','25','no-reply@mailer.financialit.net','E9xv#PX@8F$cg','bounced@mailer.financialit.net','gE9xv@PX#8F$cgE','abuse@financialit.net','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.genesis-gifts.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.genesis-gifts.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@genesis-gifts.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.mynfra.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.mynfra.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@mynfra.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.passionerp.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.passionerp.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@passionerp.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('erp.technosolutions.net',null,'smtp-mailer.kpi.com','25','no-reply@mailer.technosolutions.net','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@technosolutions.net','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.tjilo.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.tjilo.com','E9xv#PX@8F$cg','bounced@mailer.tjilo.com','qwerty321#aqqa','abuse@thepmocompany.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.unisyserp.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.unisyserp.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@unisyserp.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('erp.upshott.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.afghanid.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','info@afghanid.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>'),

  ('app.vipworkspace.com',null,'smtp-mailer.kpi.com','25','no-reply@mailer.vipworkspace.com','E9xv#PX@8F$cg','pbounced@mailforcetrack.com','qwerty321#aqqa','abuse@vipworkspace.com','We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at {abuseemail}',
'We do not tolerate spam. If you suspect that this e-mail is spam, please contact our e-mail campaign partner at <a href="mailto:{abuseemail}">{abuseemail}</a>',
'\n\n\n\nIf you do not wish to receive any e-mails from our company, please Click here to unsubscribe http://{unsubscribeurl}/unsubscribe{encrypted}\n\n',
'<br><br><br><hr/><a href="http://{unsubscribeurl}/unsubscribe{encrypted}" style="font-size:12px"> Click here </a>to unsubscribe<br><br>');