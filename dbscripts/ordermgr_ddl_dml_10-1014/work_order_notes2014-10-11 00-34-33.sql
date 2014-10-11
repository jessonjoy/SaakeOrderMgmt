USE ordermgr;

CREATE TABLE `work_order_notes` (
  `notes_id` int(11) NOT NULL AUTO_INCREMENT,
  `WORK_ORDER_ID` int(11) NOT NULL,
  `notes` varchar(200) DEFAULT NULL,
  `created_by` varchar(20) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  PRIMARY KEY (`notes_id`),
  KEY `WO_FK` (`WORK_ORDER_ID`),
  CONSTRAINT `WO_FK` FOREIGN KEY (`WORK_ORDER_ID`) REFERENCES `work_order` (`WORK_ORDER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


insert into `ordermgr`.`work_order_notes`(`notes_id`,`WORK_ORDER_ID`,`notes`,`created_by`,`created_on`) values (1,46,'dfdfd',null,null);
