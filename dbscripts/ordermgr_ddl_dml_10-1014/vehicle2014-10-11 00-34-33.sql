USE ordermgr;

CREATE TABLE `vehicle` (
  `vehicle_id` int(11) NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) DEFAULT NULL,
  `make` varchar(50) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `vin` varchar(50) DEFAULT NULL,
  `create_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created_by` varchar(20) DEFAULT NULL,
  `updated_by` varchar(20) DEFAULT NULL,
  `year` varchar(4) DEFAULT NULL,
  `mileage` varchar(8) DEFAULT NULL,
  PRIMARY KEY (`vehicle_id`),
  KEY `fk_cust_vehicle` (`customer_id`),
  CONSTRAINT `fk_cust_vehicle` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`CUSTOMER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;


insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (1,60,null,'x3','3434234234','2013-12-11 23:12:17','2014-01-16 01:08:02',null,null,'2014','343');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (2,53,null,'x3','qweqeqw','2013-11-19 22:55:55','2013-11-19 22:55:55',null,null,'2013','2323');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (3,59,null,'X3','434343434','2013-12-14 13:17:06','2013-12-15 19:56:50',null,null,'2013','3423');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (4,null,null,'535i','454FGFGF545','2013-12-11 23:47:29','2013-12-11 23:47:29',null,null,'2010','15663');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (5,null,null,'323s','','2013-12-11 23:48:07','2013-12-11 23:48:07',null,null,'2009','');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (8,null,null,'','','2013-12-12 00:02:23','2013-12-12 00:02:23',null,null,'344','');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (9,null,null,'','','2013-12-12 00:02:23','2013-12-12 00:02:23',null,null,'55','');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (10,null,null,'','','2013-12-12 00:02:23','2013-12-12 00:02:23',null,null,'545','');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (11,null,null,'hhh','','2013-12-12 09:39:59','2013-12-12 09:39:59',null,null,'2013','');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (12,null,null,'','','2013-12-12 09:40:33','2013-12-12 09:40:33',null,null,'2014','');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (13,null,null,'X3','','2013-12-15 08:56:40','2013-12-15 08:56:40',null,null,'2013','');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (14,null,null,'sds','','2013-12-15 15:21:50','2013-12-15 15:21:50',null,null,'2011','dfdf');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (15,null,null,'323i','676745rfrr4','2013-12-15 19:56:50','2013-12-15 19:56:50',null,null,'2010','');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (16,null,null,'X5','dfdfdf44542','2013-12-15 20:33:41','2013-12-15 20:33:41',null,null,'2009','55666');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (17,null,null,'X6','dfdsdsds343','2013-12-15 20:41:53','2013-12-15 20:41:53',null,null,'2012','2322');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (18,60,null,'X6','dgfsd234242','2013-12-15 20:47:08','2014-01-16 01:06:23',null,null,'2012','6665');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (19,66,null,'q','22','2013-12-18 15:29:37','2013-12-18 15:29:37',null,null,'2013','22');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (20,67,null,'x3','234','2013-12-28 01:16:54','2013-12-28 01:16:54',null,null,'2013','23423');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (21,68,null,'fgf','wr5sf33','2013-12-28 01:18:03','2013-12-28 01:18:03',null,null,'2014','w33');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (22,68,null,'wewe','234','2013-12-28 01:18:03','2013-12-28 01:18:03',null,null,'2010','24523');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (23,null,null,null,null,'2013-12-28 01:40:09','2013-12-28 01:40:09',null,null,null,null);
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (24,null,null,null,null,'2013-12-30 02:17:04','2013-12-30 02:17:04',null,null,null,null);
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (25,null,null,null,null,'2013-12-30 02:19:28','2013-12-30 02:19:28',null,null,null,null);
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (26,null,null,null,null,'2013-12-30 02:21:58','2013-12-30 02:21:58',null,null,null,null);
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (27,10,null,'212','12312323','2014-01-11 00:09:40','2014-01-11 00:09:40',null,null,'2001','2323');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (28,10,null,'212','12312323','2014-01-11 00:20:40','2014-01-11 00:20:40',null,null,'2001','2323');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (29,60,null,'343','2323232','2014-01-13 17:34:24','2014-01-13 17:34:24',null,null,'2014','34234');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (30,60,null,'sdsds','sdsdswewe','2014-01-13 17:48:00','2014-01-13 17:48:00',null,null,'2014','34343');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (31,60,null,'323jjj','34343434343','2014-01-13 18:12:01','2014-01-16 00:59:00',null,null,'2015','223533');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (32,26,null,'x5','21213232wae21','2014-01-16 21:31:28','2014-01-16 21:31:28',null,null,'2002','23232');
insert into `ordermgr`.`vehicle`(`vehicle_id`,`customer_id`,`make`,`model`,`vin`,`create_ts`,`update_ts`,`created_by`,`updated_by`,`year`,`mileage`) values (33,22,null,'','21213232wae21','2014-01-16 21:32:28','2014-01-16 23:51:36',null,null,'2002','6665');
