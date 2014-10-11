USE ordermgr;

CREATE TABLE `purchase_order_notes` (
  `notes_id` int(11) NOT NULL AUTO_INCREMENT,
  `PURCHASE_ORDER_ID` int(11) NOT NULL,
  `notes` varchar(200) DEFAULT NULL,
  `created_by` varchar(20) DEFAULT NULL,
  `created_on` datetime DEFAULT NULL,
  PRIMARY KEY (`notes_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


