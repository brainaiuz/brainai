delete from "0".reference where parentid = (SELECT id
                                            FROM
                                                "0".reference
                                            WHERE code =
                                                  '_VAT_CATEGORY');
delete from "0".reference where code = '_VAT_CATEGORY';


INSERT INTO "0".reference (code, name) VALUES ('_VAT_CATEGORY', 'Tax exemption reason');

INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('الخدمات المالية', 'Financial services mentioned in Article 29 of the VAT Regulations', 'Финансовые услуги, упомянутые в статье 29 Положения о НДС', 'VATning 29-moddagi qoidalarida aytilgan moliyaviy xizmatlar');

INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-29', 'Financial services mentioned in Article 29 of the VAT Regulations', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                              '_VAT_CATEGORY'),
                                                                 0, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Financial services mentioned in Article 29 of the VAT Regulations'));

INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('عقد تأمين على الحياة', 'Life insurance services mentioned in Article 29 of the VAT Regulations', 'Страхование на случай смерти, упомянутое в статье 29 Положения о НДС', 'VATning 29-moddagi qoidalarida aytilgan hayotni sugurtalash xizmatlari');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-29-7', 'Life insurance services mentioned in Article 29 of the VAT Regulations', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    1, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Life insurance services mentioned in Article 29 of the VAT Regulations'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('الضريبةالتوريدات العقارية المعفاة من', 'Real estate transactions mentioned in Article 30 of the VAT Regulations', 'Сделки с недвижимостью, упомянутые в статье 30 Положения о НДС', 'QQS to''g''risidagi Nizomning 30-moddasida ko''rsatilgan ko''chmas mulk bilan bog''liq operatsiyalar');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-30', 'Real estate transactions mentioned in Article 30 of the VAT Regulations', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    2, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Real estate transactions mentioned in Article 30 of the VAT Regulations'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('صادرات السلع من المملكة', 'Export of goods', 'Экспорт товаров', 'Mamlakatdan tovarlar eksporti');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-32', 'Export of goods', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    3, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Export of goods'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('صادرات الخدمات من المملكة', 'Export of services', 'Экспорт услуг', 'Xizmatlarni eksport qilish');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-33', 'Export of services', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    4, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Export of services'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('النقل الدولي للسلع', 'The international transport of Goods', 'Международная перевозка товаров', 'Tovarlar xalqaro transporti');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-34-1', 'The international transport of Goods', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    5, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'The international transport of Goods'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('النقل الدولي للركاب', 'International transport of passengers', 'Международная перевозка пассажиров', 'Yo''lovchilar xalqaro transporti');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-34-2', 'International transport of passengers', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    6, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'International transport of passengers'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('الخدمات المرتبطة مباشرة أو عرضي ًا بتوريد النقل الدولي للركابالخدمات المرتبطة مباشرة أو عرضي ًا بتوريد النقل الدولي للركاب', 'Services directly connected and incidental to a Supply of international passenger transport', 'Услуги, непосредственно связанные и случайные с поставкой международного пассажирского транспорта', 'Xalqaro yo''lovchilar transportini ta''minlash bilan to''g''ri yoki tasodifiy ravishda bog''liq xizmatlar');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-34-3', 'Services directly connected and incidental to a Supply of international passenger transport', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    7, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Services directly connected and incidental to a Supply of international passenger transport'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('توريد وسائل النقل المؤهلة', 'Supply of a qualifying means of transport', 'Поставка квалифицированного транспортного средства', 'Malakaviy transport vositalarini ta''minlash');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-34-4', 'Supply of a qualifying means of transport', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    8, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Supply of a qualifying means of transport'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('الخدمات ذات الصلة بنقل السلع أو الركاب، وفق ًا للتعريف الوارد بالمادة الخامسة والعشرين من الالئحة التنفيذية لنظام ضريبة القيامة المضافة', 'Any services relating to Goods or passenger transportation, as defined in article twenty five of these Regulations', 'Любые услуги, связанные с перевозкой товаров или пассажиров, как определено в статье двадцать пять этих Положений', 'Tovarlar yoki yo''lovchilar transporti bilan bog''liq bo''lgan har qanday xizmatlar, ushbu Nizomning 25-moddasida belgilangan');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-34-5', 'Any services relating to Goods or passenger transportation, as defined in article twenty five of these Regulations', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    9, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Any services relating to Goods or passenger transportation, as defined in article twenty five of these Regulations'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('الأدوية والمعدات الطبية', 'Medicines and medical equipment', 'Лекарства и медицинское оборудование', 'Doriyoshlar va tibbiy vositalar');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-35', 'Medicines and medical equipment', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    10, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Medicines and medical equipment'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('المعادن المؤهلة', 'Qualifying metals', 'Квалифицированные металлы', 'Malakaviy metallar');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-36', 'Qualifying metals', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    11, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Qualifying metals'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('الخدمات التعليمية الخاصة للمواطنين', 'Private education to citizen', 'Частное образование гражданам', 'Fuqarolarga maxsus ta''lim xizmatlari');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-EDU', 'Private education to citizen', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    12, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Private education to citizen'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('الخدمات الصحية الخاصة للمواطنين', 'Private healthcare to citizen', 'Частное здравоохранение гражданам', 'Fuqarolarga maxsus sog''lik xizmatlari');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-HEA', 'Private healthcare to citizen', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    13, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Private healthcare to citizen'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('توريد السلع العسكرية المؤهلة', 'Supply of qualified military goods', 'Поставка квалифицированных военных товаров', 'Malakaviy harbiy tovarlar ta''minlash');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-MLTRY', 'Supply of qualified military goods', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    14, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'Supply of qualified military goods'));
INSERT INTO "0".reference_locale(arabic, english, russian, uzbek) VALUES ('السبụ يتم تزويده من قبل المكلف على أساس كل حالة على حدة', 'The reason is a free text, has to be provided by the taxpayer on case to case basis', 'Причина - свободный текст, должна быть предоставлена налогоплательщиком в каждом конкретном случае', 'Sabab - bepul matn, har bir holatda solishtirilishi kerak bo''lgan solishtiruvchi tomonidan taqdim etiladi');
INSERT INTO "0".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-OOS', 'The reason is a free text, has to be provided by the taxpayer on case to case basis', (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference
                                                                                                        WHERE code =
                                                                                                                '_VAT_CATEGORY'),
                                                                    15, (SELECT id
                                                                                                        FROM
                                                                                                            "0".reference_locale
                                                                                                        WHERE english =
                                                                                                                'The reason is a free text, has to be provided by the taxpayer on case to case basis'));






delete from "anv".reference where parentid = (SELECT id
                                              FROM
                                                  "anv".reference
                                              WHERE code =
                                                    '_VAT_CATEGORY');
delete from "anv".reference where code = '_VAT_CATEGORY';


INSERT INTO "anv".reference (code, name) VALUES ('_VAT_CATEGORY', 'Tax exemption reason');

INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('الخدمات المالية', 'Financial services mentioned in Article 29 of the VAT Regulations', 'Финансовые услуги, упомянутые в статье 29 Положения о НДС', 'VATning 29-moddagi qoidalarida aytilgan moliyaviy xizmatlar');

INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-29', 'Financial services mentioned in Article 29 of the VAT Regulations', (SELECT id
                                                                                                                                                                  FROM
                                                                                                                                                                      "anv".reference
                                                                                                                                                                  WHERE code =
                                                                                                                                                                        '_VAT_CATEGORY'),
                                                                             0, (SELECT id
                                                                                 FROM
                                                                                     "anv".reference_locale
                                                                                 WHERE english =
                                                                                       'Financial services mentioned in Article 29 of the VAT Regulations'));

INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('عقد تأمين على الحياة', 'Life insurance services mentioned in Article 29 of the VAT Regulations', 'Страхование на случай смерти, упомянутое в статье 29 Положения о НДС', 'VATning 29-moddagi qoidalarida aytilgan hayotni sugurtalash xizmatlari');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-29-7', 'Life insurance services mentioned in Article 29 of the VAT Regulations', (SELECT id
                                                                                                                                                                         FROM
                                                                                                                                                                             "anv".reference
                                                                                                                                                                         WHERE code =
                                                                                                                                                                               '_VAT_CATEGORY'),
                                                                             1, (SELECT id
                                                                                 FROM
                                                                                     "anv".reference_locale
                                                                                 WHERE english =
                                                                                       'Life insurance services mentioned in Article 29 of the VAT Regulations'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('الضريبةالتوريدات العقارية المعفاة من', 'Real estate transactions mentioned in Article 30 of the VAT Regulations', 'Сделки с недвижимостью, упомянутые в статье 30 Положения о НДС', 'QQS to''g''risidagi Nizomning 30-moddasida ko''rsatilgan ko''chmas mulk bilan bog''liq operatsiyalar');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-30', 'Real estate transactions mentioned in Article 30 of the VAT Regulations', (SELECT id
                                                                                                                                                                        FROM
                                                                                                                                                                            "anv".reference
                                                                                                                                                                        WHERE code =
                                                                                                                                                                              '_VAT_CATEGORY'),
                                                                             2, (SELECT id
                                                                                 FROM
                                                                                     "anv".reference_locale
                                                                                 WHERE english =
                                                                                       'Real estate transactions mentioned in Article 30 of the VAT Regulations'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('صادرات السلع من المملكة', 'Export of goods', 'Экспорт товаров', 'Mamlakatdan tovarlar eksporti');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-32', 'Export of goods', (SELECT id
                                                                                                                FROM
                                                                                                                    "anv".reference
                                                                                                                WHERE code =
                                                                                                                      '_VAT_CATEGORY'),
                                                                             3, (SELECT id
                                                                                 FROM
                                                                                     "anv".reference_locale
                                                                                 WHERE english =
                                                                                       'Export of goods'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('صادرات الخدمات من المملكة', 'Export of services', 'Экспорт услуг', 'Xizmatlarni eksport qilish');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-33', 'Export of services', (SELECT id
                                                                                                                   FROM
                                                                                                                       "anv".reference
                                                                                                                   WHERE code =
                                                                                                                         '_VAT_CATEGORY'),
                                                                             4, (SELECT id
                                                                                 FROM
                                                                                     "anv".reference_locale
                                                                                 WHERE english =
                                                                                       'Export of services'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('النقل الدولي للسلع', 'The international transport of Goods', 'Международная перевозка товаров', 'Tovarlar xalqaro transporti');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-34-1', 'The international transport of Goods', (SELECT id
                                                                                                                                       FROM
                                                                                                                                           "anv".reference
                                                                                                                                       WHERE code =
                                                                                                                                             '_VAT_CATEGORY'),
                                                                             5, (SELECT id
                                                                                 FROM
                                                                                     "anv".reference_locale
                                                                                 WHERE english =
                                                                                       'The international transport of Goods'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('النقل الدولي للركاب', 'International transport of passengers', 'Международная перевозка пассажиров', 'Yo''lovchilar xalqaro transporti');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-34-2', 'International transport of passengers', (SELECT id
                                                                                                                                        FROM
                                                                                                                                            "anv".reference
                                                                                                                                        WHERE code =
                                                                                                                                              '_VAT_CATEGORY'),
                                                                             6, (SELECT id
                                                                                 FROM
                                                                                     "anv".reference_locale
                                                                                 WHERE english =
                                                                                       'International transport of passengers'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('الخدمات المرتبطة مباشرة أو عرضي ًا بتوريد النقل الدولي للركابالخدمات المرتبطة مباشرة أو عرضي ًا بتوريد النقل الدولي للركاب', 'Services directly connected and incidental to a Supply of international passenger transport', 'Услуги, непосредственно связанные и случайные с поставкой международного пассажирского транспорта', 'Xalqaro yo''lovchilar transportini ta''minlash bilan to''g''ri yoki tasodifiy ravishda bog''liq xizmatlar');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-34-3', 'Services directly connected and incidental to a Supply of international passenger transport', (SELECT id
                                                                                                                                                                                              FROM
                                                                                                                                                                                                  "anv".reference
                                                                                                                                                                                              WHERE code =
                                                                                                                                                                                                    '_VAT_CATEGORY'),
                                                                             7, (SELECT id
                                                                                 FROM
                                                                                     "anv".reference_locale
                                                                                 WHERE english =
                                                                                       'Services directly connected and incidental to a Supply of international passenger transport'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('توريد وسائل النقل المؤهلة', 'Supply of a qualifying means of transport', 'Поставка квалифицированного транспортного средства', 'Malakaviy transport vositalarini ta''minlash');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-34-4', 'Supply of a qualifying means of transport', (SELECT id
                                                                                                                                            FROM
                                                                                                                                                "anv".reference
                                                                                                                                            WHERE code =
                                                                                                                                                  '_VAT_CATEGORY'),
                                                                             8, (SELECT id
                                                                                 FROM
                                                                                     "anv".reference_locale
                                                                                 WHERE english =
                                                                                       'Supply of a qualifying means of transport'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('الخدمات ذات الصلة بنقل السلع أو الركاب، وفق ًا للتعريف الوارد بالمادة الخامسة والعشرين من الالئحة التنفيذية لنظام ضريبة القيامة المضافة', 'Any services relating to Goods or passenger transportation, as defined in article twenty five of these Regulations', 'Любые услуги, связанные с перевозкой товаров или пассажиров, как определено в статье двадцать пять этих Положений', 'Tovarlar yoki yo''lovchilar transporti bilan bog''liq bo''lgan har qanday xizmatlar, ushbu Nizomning 25-moddasida belgilangan');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-34-5', 'Any services relating to Goods or passenger transportation, as defined in article twenty five of these Regulations', (SELECT id
                                                                                                                                                                                                                     FROM
                                                                                                                                                                                                                         "anv".reference
                                                                                                                                                                                                                     WHERE code =
                                                                                                                                                                                                                           '_VAT_CATEGORY'),
                                                                             9, (SELECT id
                                                                                 FROM
                                                                                     "anv".reference_locale
                                                                                 WHERE english =
                                                                                       'Any services relating to Goods or passenger transportation, as defined in article twenty five of these Regulations'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('الأدوية والمعدات الطبية', 'Medicines and medical equipment', 'Лекарства и медицинское оборудование', 'Doriyoshlar va tibbiy vositalar');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-35', 'Medicines and medical equipment', (SELECT id
                                                                                                                                FROM
                                                                                                                                    "anv".reference
                                                                                                                                WHERE code =
                                                                                                                                      '_VAT_CATEGORY'),
                                                                             10, (SELECT id
                                                                                  FROM
                                                                                      "anv".reference_locale
                                                                                  WHERE english =
                                                                                        'Medicines and medical equipment'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('المعادن المؤهلة', 'Qualifying metals', 'Квалифицированные металлы', 'Malakaviy metallar');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-36', 'Qualifying metals', (SELECT id
                                                                                                                  FROM
                                                                                                                      "anv".reference
                                                                                                                  WHERE code =
                                                                                                                        '_VAT_CATEGORY'),
                                                                             11, (SELECT id
                                                                                  FROM
                                                                                      "anv".reference_locale
                                                                                  WHERE english =
                                                                                        'Qualifying metals'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('الخدمات التعليمية الخاصة للمواطنين', 'Private education to citizen', 'Частное образование гражданам', 'Fuqarolarga maxsus ta''lim xizmatlari');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-EDU', 'Private education to citizen', (SELECT id
                                                                                                                              FROM
                                                                                                                                  "anv".reference
                                                                                                                              WHERE code =
                                                                                                                                    '_VAT_CATEGORY'),
                                                                             12, (SELECT id
                                                                                  FROM
                                                                                      "anv".reference_locale
                                                                                  WHERE english =
                                                                                        'Private education to citizen'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('الخدمات الصحية الخاصة للمواطنين', 'Private healthcare to citizen', 'Частное здравоохранение гражданам', 'Fuqarolarga maxsus sog''lik xizmatlari');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-HEA', 'Private healthcare to citizen', (SELECT id
                                                                                                                               FROM
                                                                                                                                   "anv".reference
                                                                                                                               WHERE code =
                                                                                                                                     '_VAT_CATEGORY'),
                                                                             13, (SELECT id
                                                                                  FROM
                                                                                      "anv".reference_locale
                                                                                  WHERE english =
                                                                                        'Private healthcare to citizen'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('توريد السلع العسكرية المؤهلة', 'Supply of qualified military goods', 'Поставка квалифицированных военных товаров', 'Malakaviy harbiy tovarlar ta''minlash');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-MLTRY', 'Supply of qualified military goods', (SELECT id
                                                                                                                                      FROM
                                                                                                                                          "anv".reference
                                                                                                                                      WHERE code =
                                                                                                                                            '_VAT_CATEGORY'),
                                                                             14, (SELECT id
                                                                                  FROM
                                                                                      "anv".reference_locale
                                                                                  WHERE english =
                                                                                        'Supply of qualified military goods'));
INSERT INTO "anv".reference_locale(arabic, english, russian, uzbek) VALUES ('السبụ يتم تزويده من قبل المكلف على أساس كل حالة على حدة', 'The reason is a free text, has to be provided by the taxpayer on case to case basis', 'Причина - свободный текст, должна быть предоставлена налогоплательщиком в каждом конкретном случае', 'Sabab - bepul matn, har bir holatda solishtirilishi kerak bo''lgan solishtiruvchi tomonidan taqdim etiladi');
INSERT INTO "anv".reference (code, name, parentid, sorder, localeid) VALUES ('VATEX-SA-OOS', 'The reason is a free text, has to be provided by the taxpayer on case to case basis', (SELECT id
                                                                                                                                                                                     FROM
                                                                                                                                                                                         "anv".reference
                                                                                                                                                                                     WHERE code =
                                                                                                                                                                                           '_VAT_CATEGORY'),
                                                                             15, (SELECT id
                                                                                  FROM
                                                                                      "anv".reference_locale
                                                                                  WHERE english =
                                                                                        'The reason is a free text, has to be provided by the taxpayer on case to case basis'));