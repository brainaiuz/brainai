update "78027".genericsettings set value = 'YES' where key = 'ENABLE_DETAILED_PRODUCT_LOCATION';

insert into "78027".reference (code, name) values ('WAREHOUSE_LOCATOINS','Warehouse Locations');


INSERT INTO "78027".reference (code, name, parentid, sorder) VALUES ('PART_ROOM','Part Room',(SELECT id FROM "78027".reference WHERE code = 'WAREHOUSE_LOCATOINS'),0);
INSERT INTO "78027".reference (code, name, parentid, sorder) VALUES ('SHOP','Shop',(SELECT id FROM "78027".reference WHERE code = 'WAREHOUSE_LOCATOINS'),1);
INSERT INTO "78027".reference (code, name, parentid, sorder) VALUES ('PR_END_AISLE','PR End Aisle',(SELECT id FROM "78027".reference WHERE code = 'WAREHOUSE_LOCATOINS'),2);
INSERT INTO "78027".reference (code, name, parentid, sorder) VALUES ('SH_END_AISLE','SH End Aisle',(SELECT id FROM "78027".reference WHERE code = 'WAREHOUSE_LOCATOINS'),3);


INSERT INTO "78027".reference (code, name, parentid, sorder) VALUES
                                                                 ('PART_ROOM_1','1',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),0),
                                                                 ('PART_ROOM_2','2',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),1),
                                                                 ('PART_ROOM_3','3',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),2),
                                                                 ('PART_ROOM_4','4',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),3),
                                                                 ('PART_ROOM_5','5',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),4),
                                                                 ('PART_ROOM_6','6',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),5),
                                                                 ('PART_ROOM_7','7',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),6),
                                                                 ('PART_ROOM_8','8',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),7),
                                                                 ('PART_ROOM_9','9',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),8),
                                                                 ('PART_ROOM_10','10',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),9),
                                                                 ('PART_ROOM_11','11',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),10),
                                                                 ('PART_ROOM_12','12',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),11),
                                                                 ('PART_ROOM_13','13',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),12),
                                                                 ('PART_ROOM_14','14',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),13),
                                                                 ('PART_ROOM_15','15',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),14),
                                                                 ('PART_ROOM_16','16',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM'),15);




INSERT INTO "78027".reference (code, name, parentid, sorder) VALUES
                                                                 ('SHOP_1','1',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),0),
                                                                 ('SHOP_2','2',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),1),
                                                                 ('SHOP_3','3',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),2),
                                                                 ('SHOP_4','4',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),3),
                                                                 ('SHOP_5','5',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),4),
                                                                 ('SHOP_6','6',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),5),
                                                                 ('SHOP_7','7',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),6),
                                                                 ('SHOP_8','8',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),7),
                                                                 ('SHOP_9','9',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),8),
                                                                 ('SHOP_10','10',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),9),
                                                                 ('SHOP_11','11',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),10),
                                                                 ('SHOP_12','12',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),11),
                                                                 ('SHOP_13','13',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),12),
                                                                 ('SHOP_14','14',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),13),
                                                                 ('SHOP_15','15',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),14),
                                                                 ('SHOP_16','16',(SELECT id FROM "78027".reference WHERE code = 'SHOP'),15);




INSERT INTO "78027".reference (code, name, parentid, sorder) VALUES
                                                                 ('PR_END_AISLE_1','1',(SELECT id FROM "78027".reference WHERE code = 'PR_END_AISLE'),0),
                                                                 ('PR_END_AISLE_2','2',(SELECT id FROM "78027".reference WHERE code = 'PR_END_AISLE'),1),
                                                                 ('PR_END_AISLE_3','3',(SELECT id FROM "78027".reference WHERE code = 'PR_END_AISLE'),2),
                                                                 ('PR_END_AISLE_4','4',(SELECT id FROM "78027".reference WHERE code = 'PR_END_AISLE'),3),
                                                                 ('PR_END_AISLE_5','5',(SELECT id FROM "78027".reference WHERE code = 'PR_END_AISLE'),4),
                                                                 ('PR_END_AISLE_6','6',(SELECT id FROM "78027".reference WHERE code = 'PR_END_AISLE'),5),
                                                                 ('PR_END_AISLE_7','7',(SELECT id FROM "78027".reference WHERE code = 'PR_END_AISLE'),6),
                                                                 ('PR_END_AISLE_8','8',(SELECT id FROM "78027".reference WHERE code = 'PR_END_AISLE'),7),
                                                                 ('PR_END_AISLE_9','9',(SELECT id FROM "78027".reference WHERE code = 'PR_END_AISLE'),8),
                                                                 ('PR_END_AISLE_10','10',(SELECT id FROM "78027".reference WHERE code = 'PR_END_AISLE'),9),
                                                                 ('PR_END_AISLE_11','11',(SELECT id FROM "78027".reference WHERE code = 'PR_END_AISLE'),10);





INSERT INTO "78027".reference (code, name, parentid, sorder) VALUES
                                                                 ('SH_END_AISLE_1','1',(SELECT id FROM "78027".reference WHERE code = 'SH_END_AISLE'),0),
                                                                 ('SH_END_AISLE_2','2',(SELECT id FROM "78027".reference WHERE code = 'SH_END_AISLE'),1),
                                                                 ('SH_END_AISLE_3','3',(SELECT id FROM "78027".reference WHERE code = 'SH_END_AISLE'),2),
                                                                 ('SH_END_AISLE_4','4',(SELECT id FROM "78027".reference WHERE code = 'SH_END_AISLE'),3);






INSERT INTO "78027".reference (code, name, parentid, sorder) VALUES
                                                                 ('PART_ROOM_1_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_1'),0),
                                                                 ('PART_ROOM_1_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_1'),1),
                                                                 ('PART_ROOM_1_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_1'),2),
                                                                 ('PART_ROOM_1_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_1'),3),
                                                                 ('PART_ROOM_1_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_1'),4),
                                                                 ('PART_ROOM_1_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_1'),5),
                                                                 ('PART_ROOM_1_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_1'),6);




INSERT INTO "78027".reference (code, name, parentid, sorder) VALUES
-- PART_ROOM_2
('PART_ROOM_2_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_2'),0),
('PART_ROOM_2_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_2'),1),
('PART_ROOM_2_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_2'),2),
('PART_ROOM_2_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_2'),3),
('PART_ROOM_2_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_2'),4),
('PART_ROOM_2_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_2'),5),
('PART_ROOM_2_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_2'),6),

-- PART_ROOM_3
('PART_ROOM_3_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_3'),0),
('PART_ROOM_3_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_3'),1),
('PART_ROOM_3_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_3'),2),
('PART_ROOM_3_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_3'),3),
('PART_ROOM_3_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_3'),4),
('PART_ROOM_3_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_3'),5),
('PART_ROOM_3_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_3'),6),

-- PART_ROOM_4
('PART_ROOM_4_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_4'),0),
('PART_ROOM_4_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_4'),1),
('PART_ROOM_4_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_4'),2),
('PART_ROOM_4_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_4'),3),
('PART_ROOM_4_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_4'),4),
('PART_ROOM_4_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_4'),5),
('PART_ROOM_4_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_4'),6),

-- PART_ROOM_5
('PART_ROOM_5_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_5'),0),
('PART_ROOM_5_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_5'),1),
('PART_ROOM_5_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_5'),2),
('PART_ROOM_5_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_5'),3),
('PART_ROOM_5_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_5'),4),
('PART_ROOM_5_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_5'),5),
('PART_ROOM_5_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_5'),6),

-- PART_ROOM_6
('PART_ROOM_6_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_6'),0),
('PART_ROOM_6_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_6'),1),
('PART_ROOM_6_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_6'),2),
('PART_ROOM_6_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_6'),3),
('PART_ROOM_6_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_6'),4),
('PART_ROOM_6_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_6'),5),
('PART_ROOM_6_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_6'),6),

-- PART_ROOM_7
('PART_ROOM_7_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_7'),0),
('PART_ROOM_7_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_7'),1),
('PART_ROOM_7_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_7'),2),
('PART_ROOM_7_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_7'),3),
('PART_ROOM_7_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_7'),4),
('PART_ROOM_7_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_7'),5),
('PART_ROOM_7_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_7'),6),

-- PART_ROOM_8
('PART_ROOM_8_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_8'),0),
('PART_ROOM_8_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_8'),1),
('PART_ROOM_8_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_8'),2),
('PART_ROOM_8_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_8'),3),
('PART_ROOM_8_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_8'),4),
('PART_ROOM_8_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_8'),5),
('PART_ROOM_8_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_8'),6),

-- PART_ROOM_9
('PART_ROOM_9_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_9'),0),
('PART_ROOM_9_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_9'),1),
('PART_ROOM_9_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_9'),2),
('PART_ROOM_9_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_9'),3),
('PART_ROOM_9_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_9'),4),
('PART_ROOM_9_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_9'),5),
('PART_ROOM_9_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_9'),6),

-- PART_ROOM_10
('PART_ROOM_10_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_10'),0),
('PART_ROOM_10_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_10'),1),
('PART_ROOM_10_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_10'),2),
('PART_ROOM_10_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_10'),3),
('PART_ROOM_10_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_10'),4),
('PART_ROOM_10_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_10'),5),
('PART_ROOM_10_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_10'),6),

-- PART_ROOM_11
('PART_ROOM_11_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_11'),0),
('PART_ROOM_11_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_11'),1),
('PART_ROOM_11_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_11'),2),
('PART_ROOM_11_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_11'),3),
('PART_ROOM_11_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_11'),4),
('PART_ROOM_11_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_11'),5),
('PART_ROOM_11_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_11'),6),

-- PART_ROOM_12
('PART_ROOM_12_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_12'),0),
('PART_ROOM_12_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_12'),1),
('PART_ROOM_12_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_12'),2),
('PART_ROOM_12_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_12'),3),
('PART_ROOM_12_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_12'),4),
('PART_ROOM_12_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_12'),5),
('PART_ROOM_12_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_12'),6),

-- PART_ROOM_13
('PART_ROOM_13_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_13'),0),
('PART_ROOM_13_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_13'),1),
('PART_ROOM_13_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_13'),2),
('PART_ROOM_13_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_13'),3),
('PART_ROOM_13_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_13'),4),
('PART_ROOM_13_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_13'),5),
('PART_ROOM_13_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_13'),6),

-- PART_ROOM_14
('PART_ROOM_14_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_14'),0),
('PART_ROOM_14_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_14'),1),
('PART_ROOM_14_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_14'),2),
('PART_ROOM_14_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_14'),3),
('PART_ROOM_14_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_14'),4),
('PART_ROOM_14_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_14'),5),
('PART_ROOM_14_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_14'),6),

-- PART_ROOM_15
('PART_ROOM_15_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_15'),0),
('PART_ROOM_15_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_15'),1),
('PART_ROOM_15_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_15'),2),
('PART_ROOM_15_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_15'),3),
('PART_ROOM_15_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_15'),4),
('PART_ROOM_15_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_15'),5),
('PART_ROOM_15_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_15'),6),

-- PART_ROOM_16
('PART_ROOM_16_A','A',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_16'),0),
('PART_ROOM_16_B','B',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_16'),1),
('PART_ROOM_16_C','C',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_16'),2),
('PART_ROOM_16_D','D',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_16'),3),
('PART_ROOM_16_E','E',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_16'),4),
('PART_ROOM_16_F','F',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_16'),5),
('PART_ROOM_16_G','G',(SELECT id FROM "78027".reference WHERE code = 'PART_ROOM_16'),6);


INSERT INTO "78027".reference (code, name, parentid, sorder) VALUES
-- SHOP_1
('SHOP_1_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_1'),0),
('SHOP_1_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_1'),1),
('SHOP_1_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_1'),2),
('SHOP_1_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_1'),3),
('SHOP_1_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_1'),4),
('SHOP_1_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_1'),5),

-- SHOP_2
('SHOP_2_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_2'),0),
('SHOP_2_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_2'),1),
('SHOP_2_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_2'),2),
('SHOP_2_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_2'),3),
('SHOP_2_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_2'),4),
('SHOP_2_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_2'),5),

-- SHOP_3
('SHOP_3_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_3'),0),
('SHOP_3_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_3'),1),
('SHOP_3_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_3'),2),
('SHOP_3_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_3'),3),
('SHOP_3_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_3'),4),
('SHOP_3_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_3'),5),

-- SHOP_4
('SHOP_4_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_4'),0),
('SHOP_4_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_4'),1),
('SHOP_4_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_4'),2),
('SHOP_4_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_4'),3),
('SHOP_4_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_4'),4),
('SHOP_4_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_4'),5),

-- SHOP_5
('SHOP_5_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_5'),0),
('SHOP_5_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_5'),1),
('SHOP_5_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_5'),2),
('SHOP_5_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_5'),3),
('SHOP_5_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_5'),4),
('SHOP_5_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_5'),5),

-- SHOP_6
('SHOP_6_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_6'),0),
('SHOP_6_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_6'),1),
('SHOP_6_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_6'),2),
('SHOP_6_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_6'),3),
('SHOP_6_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_6'),4),
('SHOP_6_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_6'),5),

-- SHOP_7
('SHOP_7_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_7'),0),
('SHOP_7_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_7'),1),
('SHOP_7_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_7'),2),
('SHOP_7_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_7'),3),
('SHOP_7_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_7'),4),
('SHOP_7_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_7'),5),

-- SHOP_8
('SHOP_8_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_8'),0),
('SHOP_8_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_8'),1),
('SHOP_8_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_8'),2),
('SHOP_8_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_8'),3),
('SHOP_8_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_8'),4),
('SHOP_8_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_8'),5),

-- SHOP_9
('SHOP_9_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_9'),0),
('SHOP_9_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_9'),1),
('SHOP_9_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_9'),2),
('SHOP_9_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_9'),3),
('SHOP_9_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_9'),4),
('SHOP_9_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_9'),5),

-- SHOP_10
('SHOP_10_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_10'),0),
('SHOP_10_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_10'),1),
('SHOP_10_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_10'),2),
('SHOP_10_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_10'),3),
('SHOP_10_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_10'),4),
('SHOP_10_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_10'),5),

-- SHOP_11
('SHOP_11_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_11'),0),
('SHOP_11_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_11'),1),
('SHOP_11_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_11'),2),
('SHOP_11_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_11'),3),
('SHOP_11_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_11'),4),
('SHOP_11_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_11'),5),

-- SHOP_12
('SHOP_12_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_12'),0),
('SHOP_12_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_12'),1),
('SHOP_12_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_12'),2),
('SHOP_12_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_12'),3),
('SHOP_12_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_12'),4),
('SHOP_12_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_12'),5),

-- SHOP_13
('SHOP_13_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_13'),0),
('SHOP_13_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_13'),1),
('SHOP_13_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_13'),2),
('SHOP_13_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_13'),3),
('SHOP_13_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_13'),4),
('SHOP_13_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_13'),5),

-- SHOP_14
('SHOP_14_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_14'),0),
('SHOP_14_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_14'),1),
('SHOP_14_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_14'),2),
('SHOP_14_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_14'),3),
('SHOP_14_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_14'),4),
('SHOP_14_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_14'),5),

-- SHOP_15
('SHOP_15_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_15'),0),
('SHOP_15_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_15'),1),
('SHOP_15_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_15'),2),
('SHOP_15_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_15'),3),
('SHOP_15_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_15'),4),
('SHOP_15_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_15'),5),

-- SHOP_16
('SHOP_16_A','A',(SELECT id FROM "78027".reference WHERE code = 'SHOP_16'),0),
('SHOP_16_B','B',(SELECT id FROM "78027".reference WHERE code = 'SHOP_16'),1),
('SHOP_16_C','C',(SELECT id FROM "78027".reference WHERE code = 'SHOP_16'),2),
('SHOP_16_D','D',(SELECT id FROM "78027".reference WHERE code = 'SHOP_16'),3),
('SHOP_16_E','E',(SELECT id FROM "78027".reference WHERE code = 'SHOP_16'),4),
('SHOP_16_FLOOR','FLOOR',(SELECT id FROM "78027".reference WHERE code = 'SHOP_16'),5);




